package com.tranzvision.gd.TZSelfInfoBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.CreateTaskServiceImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.SendSmsOrMalServiceImpl;
import com.tranzvision.gd.TZWebSiteRegisteBundle.dao.PsTzDzyxYzmTblMapper;
import com.tranzvision.gd.TZWebSiteRegisteBundle.model.PsTzDzyxYzmTbl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 
 * @author tang
 * TZ_GD_SELF_PKG:TZ_GD_EMAIL_CLS
 */
@Service("com.tranzvision.gd.TZSelfInfoBundle.service.impl.SelfUpdateEmailServiceImp")
public class SelfUpdateEmailServiceImp extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TZGDObject tzGdObject;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private PsTzDzyxYzmTblMapper psTzDzyxYzmTblMapper;
	@Autowired
	private CreateTaskServiceImpl createTaskServiceImpl;
	@Autowired
	private SendSmsOrMalServiceImpl sendSmsOrMalServiceImpl;
	
	@Override
	//点击邮件中 确认修改邮箱 的链接，修改邮箱;
	public String tzGetHtmlContent (String strParams) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);
		String tokenCode = request.getParameter("TZ_TOKEN_CODE");
		if(tokenCode != null && !"".equals(tokenCode)){
			//String tokenCode = jacksonUtil.getString("TZ_TOKEN_CODE");
			//验证是否通过;
			String tokenCodeSQL = "SELECT TZ_EFF_FLAG,TZ_DLZH_ID,TZ_JG_ID,TZ_CNTLOG_ADDTIME,TZ_EMAIL FROM PS_TZ_DZYX_YZM_TBL WHERE TZ_TOKEN_CODE=? ";
			Map<String, Object> map = jdbcTemplate.queryForMap(tokenCodeSQL,new Object[]{tokenCode});
			if(map != null){
				String effFlg = (String)map.get("TZ_EFF_FLAG");
				String dlzhId = (String)map.get("TZ_DLZH_ID");
				String jgId = (String)map.get("TZ_JG_ID");
				Date cntlogTm =  (Date)map.get("TZ_CNTLOG_ADDTIME");
				String email = (String)map.get("TZ_EMAIL");
				//存在
				if("Y".equals(effFlg) && cntlogTm != null){
					Calendar ca=Calendar.getInstance();
					ca.setTime(cntlogTm);
					ca.add(Calendar.MINUTE, 30);
					cntlogTm = ca.getTime();
					Date curDate = new Date();
					if(cntlogTm.before(curDate)){
						//验证码超时;
						try {
							return tzGdObject.getHTMLText("HTML.TZSelfInfoBundle.TZ_GD_EMAIL_NOTICE_HTML",request.getContextPath(),request.getContextPath()+"/statics/images/tranzvision/error_001.jpg","邮箱绑定失败，验证已超时" );
						} catch (TzSystemException e) {
							e.printStackTrace();
							return "邮箱绑定失败，验证已超时";
						}
					}else{
						// 查看是否绑定了邮箱，如果绑定了邮箱，则要同时修改绑定邮箱，同时要判断新的绑定邮箱是否在该机构下重复，如果重复，则修改失败，同时要提示用户;
						String sql = "select OPRID from PS_TZ_AQ_YHXX_TBL where TZ_DLZH_ID=? and TZ_JG_ID=?";
						String oprid = jdbcTemplate.queryForObject(sql, new Object[]{dlzhId,jgId},"String");
						if(oprid==null || "".equals(oprid)){
							try {
								return tzGdObject.getHTMLText("HTML.TZSelfInfoBundle.TZ_GD_EMAIL_NOTICE_HTML",request.getContextPath(),request.getContextPath()+"/statics/images/tranzvision/error_001.jpg","邮箱绑定失败，验证已超时" );
							} catch (TzSystemException e) {
								e.printStackTrace();
								return "邮箱绑定失败，验证已超时";
							}
							
						}
						
						String repSQL = "SELECT COUNT(1) FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_JIHUO_ZT = 'Y' AND LOWER(TZ_EMAIL) = LOWER(?) AND TZ_JG_ID=? and TZ_YXBD_BZ='Y' and OPRID<>?";
						int repInt = jdbcTemplate.queryForObject(repSQL, new Object[]{email,jgId,oprid},"Integer");
						if(repInt > 0){
							try {
								return tzGdObject.getHTMLText("HTML.TZSelfInfoBundle.TZ_GD_EMAIL_NOTICE_HTML",request.getContextPath(),request.getContextPath()+"/statics/images/tranzvision/error_001.jpg","邮箱绑定失败，该邮箱已被占用" );
							} catch (TzSystemException e) {
								e.printStackTrace();
								return "邮箱绑定失败，该邮箱已被占用";
							}
						}else{
							String updateSQL = "UPDATE PS_TZ_AQ_YHXX_TBL SET TZ_EMAIL=?,TZ_YXBD_BZ='Y' WHERE TZ_JIHUO_ZT='Y' AND TZ_DLZH_ID=? AND TZ_JG_ID=?";
							jdbcTemplate.update(updateSQL,new Object[]{email,dlzhId,jgId});
							updateSQL = "UPDATE PS_TZ_DZYX_YZM_TBL SET TZ_EFF_FLAG='N' WHERE TZ_TOKEN_CODE=? and TZ_EFF_FLAG='Y'";
							jdbcTemplate.update(updateSQL,new Object[]{tokenCode});
							try {
								return tzGdObject.getHTMLText("HTML.TZSelfInfoBundle.TZ_GD_EMAIL_NOTICE_HTML",request.getContextPath(),request.getContextPath()+"/statics/images/tranzvision/right_green.png","邮箱绑定成功" );
							} catch (TzSystemException e) {
								e.printStackTrace();
								return "邮箱绑定成功";
							}
						}
					}
				}else{
					try {
						return tzGdObject.getHTMLText("HTML.TZSelfInfoBundle.TZ_GD_EMAIL_NOTICE_HTML",request.getContextPath(),request.getContextPath()+"/statics/images/tranzvision/error_001.jpg","邮箱绑定失败，验证码已失效" );
					} catch (TzSystemException e) {
						e.printStackTrace();
						return "邮箱绑定失败，验证码已失效";
					}
				}
						
			}else{
				try {
					return tzGdObject.getHTMLText("HTML.TZSelfInfoBundle.TZ_GD_EMAIL_NOTICE_HTML",request.getContextPath(),request.getContextPath()+"/statics/images/tranzvision/error_001.jpg","邮箱绑定失败，验证码已失效");
				} catch (TzSystemException e) {
					e.printStackTrace();
					return "邮箱绑定失败，验证码已失效";
				}
			}
		}else{
			try {
				return tzGdObject.getHTMLText("HTML.TZSelfInfoBundle.TZ_GD_EMAIL_NOTICE_HTML",request.getContextPath(),request.getContextPath()+"/statics/images/tranzvision/error_001.jpg","邮箱绑定失败，验证码已失效" );
			} catch (TzSystemException e) {
				e.printStackTrace();
				return "邮箱绑定失败，验证码已失效";
			}
		}
	}
	
	@Override
	//发送修改邮件到新邮箱
	public String tzGetHtmlData(String strParams) {
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		String jgId= tzLoginServiceImpl.getLoginedManagerOrgid(request);
		if(oprid == null || "".equals(oprid)||"TZ_GUEST".equals(oprid)
				|| jgId == null || "".equals(jgId)){
			return "\"请先登录再修改邮箱\"" ;
		}
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);
		//获得新邮箱;
		String strEmail = jacksonUtil.getString("newEmail");
		//判断邮箱格式;
		String repSQL = "SELECT COUNT(1) FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_JIHUO_ZT = 'Y' AND LOWER(TZ_EMAIL) = LOWER(?) AND TZ_JG_ID=? and TZ_YXBD_BZ='Y' and OPRID<>? ";
		int rptInt = jdbcTemplate.queryForObject(repSQL, new Object[]{strEmail,jgId,oprid},"Integer");
		if(rptInt > 0){
			return "\"该邮箱已被占用，请重新输入！\"";
		}else{
			String tokenSQL = "SELECT TZ_CNTLOG_ADDTIME,TZ_YZM_YXQ,TZ_TOKEN_CODE FROM PS_TZ_DZYX_YZM_TBL WHERE  TZ_EFF_FLAG='Y' AND TZ_JG_ID=? AND TZ_EMAIL=? ORDER BY TZ_CNTLOG_ADDTIME DESC limit 0,1";
			Map<String, Object> map = jdbcTemplate.queryForMap(tokenSQL,new Object[]{jgId, strEmail});
			if(map != null){
				String yzm = (String)map.get("TZ_TOKEN_CODE");
				Date yxq = (Date)map.get("TZ_YZM_YXQ");
				Date curDate = new Date();
				if(curDate.before(yxq)){
					return "\"发送时间间隔太短,请等待一段时间后再试。\"";
				}else{
					String updateTokenSQL = "UPDATE PS_TZ_DZYX_YZM_TBL SET TZ_EFF_FLAG='N' WHERE TZ_TOKEN_CODE=?";
					jdbcTemplate.update(updateTokenSQL,new Object[]{yzm});
					String message = this.toSaveSendEmail(strEmail, jgId);
					return "\""+message+"\"";
				}
			}else{
				String message =  this.toSaveSendEmail(strEmail, jgId);
				return "\""+message+"\"";
			}
		}
	}
	
	public String toSaveSendEmail(String strEmail,String strJgid){
		String strYZM = "";
		String mess = "";
		String dlzhId = "";
		
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		
		String zhSQL = "SELECT TZ_DLZH_ID FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_JG_ID=? AND OPRID=?";
		dlzhId = jdbcTemplate.queryForObject(zhSQL, new Object[]{strJgid,oprid},"String");
		String updateYzm = "update PS_TZ_DZYX_YZM_TBL set TZ_EFF_FLAG='N' where TZ_DLZH_ID=? and TZ_JG_ID=?";
		jdbcTemplate.update(updateYzm, new Object[]{dlzhId,strJgid});

		try{
		      	// 生成邮件发送令牌;
		      	String strSeq = "00000000" + String.valueOf(getSeqNum.getSeqNum("TZ_GD_AUTHCODE", "TZ_GD_EMAIL"));
		      	strSeq = strSeq.substring(strSeq.length()-8, strSeq.length());
		      	SimpleDateFormat datetimeFormate = new SimpleDateFormat("yyyyMMddHHmmss");
				strYZM = String.valueOf((int)(Math.random()*100000)) + datetimeFormate.format(new Date())+strSeq;

				
				PsTzDzyxYzmTbl psTzDzyxYzmTbl = new PsTzDzyxYzmTbl();
				psTzDzyxYzmTbl.setTzDlzhId(dlzhId);
				psTzDzyxYzmTbl.setTzJgId(strJgid);
				psTzDzyxYzmTbl.setTzTokenCode(strYZM);
				psTzDzyxYzmTbl.setTzTokenType("EDIT");
				psTzDzyxYzmTbl.setTzEmail(strEmail);
				Date currentDate = new Date();
				psTzDzyxYzmTbl.setTzCntlogAddtime(currentDate);

				Calendar ca=Calendar.getInstance();
				ca.setTime(new Date());
				ca.add(Calendar.MINUTE, 1);
				Date yxqDate = ca.getTime();
				psTzDzyxYzmTbl.setTzYzmYxq(yxqDate);
				
				psTzDzyxYzmTbl.setTzEffFlag("Y");
				psTzDzyxYzmTblMapper.insert(psTzDzyxYzmTbl);
				
				// 发送邮件;
				String taskId = createTaskServiceImpl.createTaskIns(strJgid, "TZ_EML_BINDEMAI", "MAL", "A");
				if(taskId == null || "".equals(taskId)){
					mess = "创建邮件发送任务失败！";
				    return mess;
				}
				
				// 创建短信、邮件发送的听众;
				String createAudience = createTaskServiceImpl.createAudience(taskId,strJgid,"高端产品用户邮箱修改", "JSRW");
				if(createAudience == null || "".equals(createAudience)){
					mess = "创建邮件发送的听众失败！";
				    return mess;
				}
				
				//得到注册用户姓名;
				String relenameSQL = "SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=? limit 0,1";
				String strUserName = "";
				try{
					strUserName = jdbcTemplate.queryForObject(relenameSQL, new Object[]{oprid},"String");
				}catch(Exception e){
					e.printStackTrace();
				}
				
				// 为听众添加听众成员;
				boolean addAudCy = createTaskServiceImpl.addAudCy(createAudience,strUserName, strUserName, "", "", strEmail, "", "", oprid, "", "", "");
				if(addAudCy == false){
					mess = "为听众添加听众成员失败！";
				    return mess;
				}
				
				// 得到创建的任务ID;
		        if(taskId == null || "".equals(taskId)){
		        	mess = "创建任务ID失败！";
				    return mess;
		        }
		        
		        sendSmsOrMalServiceImpl.send(taskId, "");
		        mess = "SUCCESS";
		        return mess;
				
			
		}catch(Exception e){
			e.printStackTrace();
			return "发送邮件失败";
		}
	}
	
	
}
