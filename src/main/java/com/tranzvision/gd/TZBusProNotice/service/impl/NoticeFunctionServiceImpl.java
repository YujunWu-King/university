package com.tranzvision.gd.TZBusProNotice.service.impl;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.CreateTaskServiceImpl;
import com.tranzvision.gd.TZThirdPartyManage.service.impl.ThirdWebAPICall;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzException;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.workflow.base.TzWorkflowFunc;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 
 * ClassName: NoticeFunctionServiceImpl
 * 
 * @author 吴玉军
 * @version 1.0 Create Time: 2019年1月15日 上午11:09:52
 *          Description:业务流程通知（1：邮件通知功能，2：微信通知功能，3：生产Outlook任务功能）
 */
@Service("com.tranzvision.gd.TZBusProNotice.service.impl.NoticeFunctionServiceImpl")
public class NoticeFunctionServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private CreateTaskServiceImpl createTaskServiceImpl;
	@Autowired
	private GetHardCodePoint getHardCodePoint;
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;


	/**
	 * 
	* Description:发送邮件
	* Create Time: 2019年1月29日 下午4:00:31
	* @author 吴玉军
	* @param wflInsId	流程实例ID
	* @param stpInsId	步骤实例ID
	* @param userId		发送人员ID列表
	* @return
	 * @throws TzException
	 */
	public void sendEmail(String wflInsId, String stpInsId, List<String> userIds) throws TzException {
		try {
			// 邮件模板ID
			String tmpId = sqlQuery.queryForObject("select B.tzms_email_tmp_id from tzms_stpins_tbl A LEFT JOIN tzms_wflstp_tBase B on A.tzms_wflstpid=B.tzms_wflstp_tid where A.tzms_wflinsid=? and A.tzms_stpinsid=? ", 
					new Object[] { wflInsId, stpInsId }, "String");
			if(StringUtils.isBlank(tmpId)) {
				//使用默认的邮件通知模板
				tmpId = getHardCodePoint.getHardCodePointVal("TZ_WFL_EML_NOTICE_TPL");
				
				if(StringUtils.isBlank(tmpId)) {
					throw new TzException("没有找到对应的邮件审批模板");
				}
			}
			
			//1、创建邮件发送任务;
			String taskId = createTaskServiceImpl.createTaskIns("SAIF", tmpId, "MAL", "A");
			if (StringUtils.isBlank(taskId)) {
				throw new TzException("创建邮件发送任务失败");
			}
			
			// 2、创建短信、邮件发送的听众(audId听众ID);
			String createAudience = createTaskServiceImpl.createAudience(taskId, "SAIF", "高金业务流程通知邮件", "JSRW");
			if(StringUtils.isBlank(createAudience)) {
				throw new TzException("创建邮件发送的听众失败");
			}
			
			//3、查询任务号，并更新邮件主题
			String wflTaskId = sqlQuery.queryForObject("select tzms_taskid from tzms_wflins_tbl where tzms_wflinsid=?", 
					new Object[] { wflInsId }, "String");
			if(StringUtils.isNotBlank(wflTaskId)) {
				String emailSub = "任务号：" + wflTaskId + " " + createTaskServiceImpl.getEmailSendTitle(taskId);
				createTaskServiceImpl.updateEmailSendTitle(taskId, emailSub);
			}
			
			//添加听众成员
			int sendCount = 0;
			if(userIds != null && userIds.size() > 0) {
				//获取听众成员ID
				int audCyId = getSeqNum.getSeqNum("TZ_AUDCYUAN_T", "TZ_AUDCY_ID", userIds.size(), 0);
				
				for (int i = 0; i < userIds.size(); i++) {
					String userId = userIds.get(i);
	
					String oprid = "";
					String strEmail = "";
					String name = "";
					
					TzWorkflowFunc wflFunc = new TzWorkflowFunc();
					Map<String,String> contachMap = wflFunc.getTeaContactInfoByUserId(userId);
					if(contachMap != null) {
						oprid = contachMap.get("oprid");
						name = contachMap.get("name");
						strEmail = contachMap.get("email");
					}
					
					if(StringUtils.isNotBlank(strEmail)) {
						// 为听众添加听众成员;
						boolean addAudCy = createTaskServiceImpl.addAudCy2(createAudience, name, name, "", "", strEmail, "", "", oprid, wflInsId, stpInsId, userId, audCyId);
						if(addAudCy == true) {
							sendCount ++;
							audCyId --;
							
							// 步骤责任人ID
							String tzms_stpproid = sqlQuery.queryForObject("select tzms_stpproid from tzms_stpins_tbl where tzms_wflinsid=? and tzms_stpinsid=?", new Object[] {wflInsId,stpInsId},"String");
							// Dynamics活动ID(邮件写的是任务ID)
							String tzms_activity_uniqueid = taskId;
							// 步骤实例ID
							String tzms_stpinsid = stpInsId;
	
							//内容;
							String content = sqlQuery.queryForObject("select TZ_MAL_CONTENT from PS_TZ_EMALTMPL_TBL where TZ_TMPL_ID=?", 
									new Object[] { tmpId }, "String");
							
							//写邮件通知历史表 
							this.writeEmailNoticeData(tzms_stpinsid, tzms_stpproid, "1", tmpId, content, oprid, new Date(), tzms_activity_uniqueid);
						}
					}
				}
				
				if(sendCount > 0) {	
					//发送任务
					//sendSmsOrMalServiceImpl.send(taskId, "");
					//改用批处理发送
					sqlQuery.update("insert into TZ_SMSEML_TASK_TBL (TZ_EML_SMS_TASK_ID) values(?)", new Object[] { taskId });
				}
			}
		} catch (TzException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new TzException("发送审批邮件异常", e);
		}
	}
	
	
	

	/**
	 * 
	* Description:发送微信消息
	* Create Time: 2019年1月29日 下午5:44:00
	* @author 吴玉军
	* @param wflInsId	流程实例ID
	* @param stpInsId	步骤实例ID
	* @param userId		发送人员ID列表
	* @return
	 */
	public String sendWeChat(String wflInsId,String stpInsId,List<String> userIds) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("status", "");
		returnMap.put("msg", "");

		JacksonUtil jacksonUtil = new JacksonUtil();

		/**
		 * 1:首先根据企业号的corpid+corpsecret得到access_token
		 */
		String corpid = getHardCodePoint.getHardCodePointVal("TZ_WX_CORPID");
		String corpsecret = getHardCodePoint.getHardCodePointVal("TZ_WX_SECRET");
		String access_token = getATByCC(corpid,corpsecret);
		if(access_token==null) {
			returnMap.put("msg",  "获取企业access_token出错");
			returnMap.put("status", "fail");
			return jacksonUtil.Map2json(returnMap);
		}
		StringBuffer touserStrB= new StringBuffer();
		String touserStr = "";
		Set<String> touserSet = new HashSet<String>();
		for(int i=0;i<userIds.size();i++) {
			String userId = userIds.get(i);
			List<String> openIDList = sqlQuery.queryForList("select tzms_wx_userid from tzms_tea_defn_tBase  where tzms_user_uniqueid=?",new Object[] {userId});
			Set<String> set = new HashSet<>(openIDList);
			if(openIDList!=null) {
				touserSet.addAll(set);
			}
		}
		if(touserSet!=null) {
			while(touserSet.iterator().hasNext()) {
				touserStrB.append(touserSet.iterator().next());
				touserStrB.append("|");
			}
			touserStr = touserStr.substring(0, touserStr.length()-1);
		}
		if("".equals(touserStr)) {
			touserStr = "@all";
		}
		System.out.println("微信接受者id:"+touserStr);
		JSONObject jsparam = new JSONObject();
		
		String content = "你的快递已到，请携带工卡前往邮件中心领取。\n出发前可查看<a href=\"http://work.weixin.qq.com\">邮件中心视频实况</a>，聪明避开排队。";
	    jsparam.put("content",content );
	    JSONObject jsparam2 = new JSONObject();
	    jsparam2.put("touser", touserStr);
	    jsparam2.put("msgtype", "text");
	    jsparam2.put("agentid", corpid);
	    jsparam2.put("text", jsparam);
	    jsparam2.put("safe", "0");
		
		String sendruslt= sendData(access_token,jsparam2);
		if("".equals(sendruslt)) {
			returnMap.put("msg",  "消息失败");
			returnMap.put("status", "fail");
			return jacksonUtil.Map2json(returnMap);
		}
		// 步骤实例ID
		String tzms_stpinsid = stpInsId;
		// 步骤责任人ID
		String tzms_stpproid = sqlQuery.queryForObject("select tzms_stpproid from tzms_stpins_tbl where tzms_wflinsid=? and tzms_stpinsid=?", new Object[] {wflInsId,stpInsId},"String");
		String tzms_activity_uniqueid = jacksonUtil.getString("tzms_activity_uniqueid");// Dynamics活动ID
		
		Boolean wirteStatus = this.writeEmailNoticeData(tzms_stpinsid, tzms_stpproid, "2", "", content, access_token, new Date(), tzms_activity_uniqueid);
		if (!wirteStatus) {
			returnMap.put("msg",  "写入历史表错误");
			returnMap.put("status", "fail");
			return jacksonUtil.Map2json(returnMap);
		}
		// 微信消息发送日志
		returnMap.put("msg", "微信消息发送成功");
		returnMap.put("status", "success");
		return jacksonUtil.Map2json(returnMap);
	}

	/**
	 * 
	* Description:生成OutLook任务
	* Create Time: 2019年1月29日 下午6:36:38
	* @author 吴玉军
	* @param wflInsId	流程实例ID
	* @param stpInsId	步骤实例ID
	* @param userId		发送人员ID列表
	* @return
	 */
	public String sentOutlook(String wflInsId,String stpInsId,List<String> userIds) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("status", "");
		returnMap.put("msg", "");

		JacksonUtil jacksonUtil = new JacksonUtil();
		try {			
			ThirdWebAPICall thirdWebAPICall = new ThirdWebAPICall();
			String dynDomain = getSysHardCodeVal.getDynamicDomainUrl();

//			String url = dynDomain + "/api/data/v8.2/tasks?return=representation";
			String url = dynDomain + "/api/data/v8.2/tasks";
			// 主题，取工作流摘要
			String subject = sqlQuery.queryForObject("select tzms_abstract from tzms_stpins_tbl where tzms_wflinsid=? and tzms_stpinsid=?", 
					new Object[] {wflInsId,stpInsId}, "String");
			// 知识文章在系统中ID
//			String regardingobjectid = "";
			
			//持续时间，int 默认3天 配置在hashCode中
			int actualdurationminutes =  Integer.valueOf(getHardCodePoint.getHardCodePointVal("TZ_DEFAULT_END_DAY"));
			// 截止日期
			String scheduledend = "";
			SimpleDateFormat TZformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			Date scheduledendDT = sqlQuery.queryForObject("select tzms_stpenddt from tzms_stpins_tbl where tzms_wflinsid=? and tzms_stpinsid=?", 
					new Object[] {wflInsId,stpInsId}, "Date");
			if(scheduledendDT==null) {
				//超时时间为空，则超时时间=任务开始时间+任务默认结束天数
				String default_day = getHardCodePoint.getHardCodePointVal("TZ_DEFAULT_END_DAY");
				String tzms_stpstartdt = sqlQuery.queryForObject("select tzms_stpstartdt from tzms_stpins_tbl where tzms_wflinsid=? and tzms_stpinsid=?", 
						new Object[] {wflInsId,stpInsId}, "String");
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				Date date = format.parse(tzms_stpstartdt, new ParsePosition(0));
		        Calendar calendar = Calendar.getInstance();
		        calendar.setTime(date);
		        // add方法中的第二个参数n中，正数表示该日期后n天，负数表示该日期的前n天
		        calendar.add(Calendar.DATE, Integer.valueOf(default_day));
		        Date date1 = calendar.getTime();
		        scheduledend = TZformat.format(date1);
			}else {
				scheduledend = TZformat.format(scheduledendDT);
			}
			
			//优先级 int 0：低 1：正常 2：高
			int prioritycode =1;
//			String tzms_pridtlid = sqlQuery.queryForObject("select tzms_pridtlid from tzms_stpins_tbl where tzms_wflinsid=? and tzms_stpinsid=?", new Object[] {wflInsId,stpInsId}, "String");
//			String tzms_pritpy_uniqueid = sqlQuery.queryForObject("select tzms_pritpy_uniqueid from  tzms_wf_rpcdtl_tBase where tzms_wf_rpcdtl_tid=?", new Object[] {tzms_pridtlid}, "String");
//			String tzms_pritpydesc = sqlQuery.queryForObject("select  tzms_pritpydesc from  tzms_wfl_pritpy_tBase where tzms_wfl_pritpy_tid =?", new Object[] {tzms_pritpy_uniqueid}, "String");
//			if(tzms_pritpydesc!=null) {
//				prioritycode = Integer.valueOf(tzms_pritpydesc);
//			}
			
			for(String userId : userIds) {
				if(StringUtils.isNotBlank(userId)) {
					//生成GUID
					String guid = UUID.randomUUID().toString().toUpperCase();
					
					Map<String, Object> map = new HashMap<>();
					map.put("activityid", guid);// 任务系统ID可以为空
					map.put("subject", subject);// 主题
					map.put("description", subject);// 说明
					//map.put("regardingobjectid", regardingobjectid);// 知识文章在系统中ID
					map.put("actualdurationminutes", actualdurationminutes);// 持续时间，int
					map.put("scheduledend", scheduledend);// 截止日期
					map.put("prioritycode", prioritycode); // 优先级 int 0：低 1：正常 2：高
					map.put("ownerid@odata.bind", "/systemusers(" + userId + ")"); 
					
					String postData = jacksonUtil.Map2json(map);
					
					if(thirdWebAPICall.callDynamicWebServiceAPI(url, postData, request, "POST", true, true) == true) {
						if(thirdWebAPICall.isRequestSuccess() == true) {
							String result = thirdWebAPICall.getWebAPIResult();
							
							System.out.println("-----------tasks result---------" + result);
							jacksonUtil.json2Map(result);
							
							
							// 步骤实例ID
							String tzms_stpinsid = stpInsId;
							// 步骤责任人ID
							String tzms_stpproid = sqlQuery.queryForObject("select tzms_stpproid from tzms_stpins_tbl where tzms_wflinsid=? and tzms_stpinsid=?", 
									new Object[] {wflInsId,stpInsId},"String");
							this.writeEmailNoticeData(tzms_stpinsid, tzms_stpproid, "3", "", "", "", new Date(), guid);
							
						}else {
							returnMap.put("status", "fail");
							returnMap.put("msg", "创建Outlook任务失败！");
							return jacksonUtil.Map2json(returnMap);
						}
					}else {
						returnMap.put("status", "fail");
						returnMap.put("msg", "创建Outlook任务失败！");
						return jacksonUtil.Map2json(returnMap);
					}
				}
			}
		} catch (Exception e) {
			returnMap.put("status", "fail");
			returnMap.put("msg", "系统错误！");
			e.printStackTrace();
		}

		return jacksonUtil.Map2json(returnMap);
	}
	
	

	/**
	 * 
	 * Description:同步邮件、WeChat、OutLook通知历史表
	 * Create Time: 2019年1月16日 上午10:46:55
	 * @author 吴玉军
	 * @param tzms_stpinsid 步骤实例ID
	 * @param tzms_stpproid 步骤责任人ID
	 * @param noticeType	通知类型    
	 * @param tmpId			通知模板
	 * @param content		通知内容
	 * @param oprid			收件人
	 * @param sendDate		通知时间
	 * @param tzms_activity_uniqueid	Dynamics活动ID
	 */
	private Boolean writeEmailNoticeData(String tzms_stpinsid, String tzms_stpproid, String noticeType, String tmpId,
			String content, String oprid, Date sendDate, String tzms_activity_uniqueid) {
		String tzms_notice_xh = String.valueOf(getSeqNum.getSeqNum("tzms_wfl_tzrhis_tbl", "tzms_notice_xh"));
		String sql = "INSERT INTO tzms_wfl_tzrhis_tbl (tzms_notice_xh, tzms_stpinsid, tzms_stpproid, tzms_notice_type, tzms_notice_tmpid, tzms_notice_content, tzms_recieve_psn, tzms_notice_time, tzms_activity_uniqueid) VALUES (?,?,?,?,?,?,?,?,?)";
		
//		Boolean status = sqlQuery.equals(sql);
		
		int rtn = sqlQuery.update(sql, new Object[] { tzms_notice_xh, tzms_stpinsid, tzms_stpproid, noticeType, tmpId, content, oprid, sendDate, tzms_activity_uniqueid });
		return rtn > 0;
	}
	
	/**
	 * 
	 * Description:获取企业的access_token 根据corpid、corpsecret
	 * Create Time: 2019年1月26日 上午10:46:55
	 * @author 吴玉军
	 * @param corpid 企业号corpid
	 * @param corpsecret 企业号corpsecret
	 * @return 企业号的access_token
	 */
	private String getATByCC(String corpid,String corpsecret) {
		String result = null;
        BufferedReader in = null;
        Map<String, String> maps = new HashMap<>();
        try {
            String urlNameString = getHardCodePoint.getHardCodePointVal("TZ_WXQYH_URL")+"gettoken?corpid=" + corpid 
                    + "&corpsecret=" + corpsecret;
            URL realUrl = new URL(urlNameString);
            System.out.println(realUrl);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("Content-Type", "appliction/json;charset=UTF-8");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            //http请求属性
            System.out.println("http请求属性："+connection.getRequestProperty("Content-Type"));
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println("http响应头文件："+key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result=line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        String[] ss = result.substring(1, result.length()-1).split(",");
        List<String> ls = Arrays.asList(ss);
        System.out.println(Arrays.toString(ss));
        for(String temp:ls){
            maps.put(temp.split(":")[0].replaceAll("\"",""), temp.split(":")[1].replaceAll("\"",""));
        }
        result=maps.get("access_token");
        return result;
	}

	/**
	 * 
	 * Description:WeChat发送消息
	 * Create Time: 2019年1月26日 上午10:46:55
	 * @author 吴玉军
	 * @param access_token 企业号access_token
	 * @param jsparam 消息报头
	 * @return 是否响应
	 */
	private String sendData(String access_token,JSONObject jsparam){
        String url=getHardCodePoint.getHardCodePointVal("TZ_WXQYH_URL")+"message/send?access_token="+access_token;
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        System.out.println("post请求josn串："+jsparam.toString());
        try {
            URL realUrl = new URL(url);
            System.out.println("post请求地址："+realUrl);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("Content-Type", "appliction/json;charset=UTF-8");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());

            // 发送请求参数
            out.print(jsparam);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        //System.out.println(result.toString());
        return result;
    } 
}
