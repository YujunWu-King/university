/**
 * 
 */
package com.tranzvision.gd.TZWebSiteRegisteBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.CreateTaskServiceImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.SendSmsOrMalServiceImpl;
import com.tranzvision.gd.TZSelfInfoBundle.dao.PsTzShjiYzmTblMapper;
import com.tranzvision.gd.TZSelfInfoBundle.model.PsTzShjiYzmTbl;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.ValidateUtil;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 修改手机，原PS：TZ_GD_USERMG_PKG:TZ_CHANGE_MOBILE
 * 
 * @author SHIHUA
 * @since 2016-02-04
 */
@Service("com.tranzvision.gd.TZWebSiteRegisteBundle.service.impl.ChangeMobileServiceImpl")
public class ChangeMobileServiceImpl extends FrameworkImpl {

	@Autowired
	private ValidateUtil validateUtil;
	
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	@Autowired
	private TZGDObject tzGDObject;
	
	@Autowired
	private CreateTaskServiceImpl createTaskServiceImpl;
	
	@Autowired
	private SendSmsOrMalServiceImpl sendSmsOrMalServiceImpl;
	
	@Autowired
	private PsTzShjiYzmTblMapper psTzShjiYzmTblMapper;

	@Override
	public String tzGetHtmlContent(String strParams) {

		String strRet = "";

		try {

			JacksonUtil jacksonUtil = new JacksonUtil();
			jacksonUtil.json2Map(strParams);

			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

			String ctxPath = request.getContextPath();
			String commonUrl = ctxPath + "/dispatcher";

			String siteId = jacksonUtil.getString("siteId");
			String sql = "select TZ_JG_ID from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
			String orgid = sqlQuery.queryForObject(sql, new Object[] { siteId }, "String");
			
			String sqlGetLanguage = "select TZ_SITE_LANG from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
			String language = sqlQuery.queryForObject(sqlGetLanguage, new Object[] { siteId }, "String");

			String RegisterInit_url = commonUrl;
			String SureTel_url = commonUrl;

			sql = "select TZ_ZY_SJ from PS_TZ_LXFSINFO_TBL where TZ_LXFS_LY='ZCYH' and TZ_LYDX_ID=?";
			String mobile = sqlQuery.queryForObject(sql, new Object[] { oprid }, "String");

			if("ENG".equals(language)){
				strRet = tzGDObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_WDZH_MOBILE_ENG", ctxPath, RegisterInit_url,
						SureTel_url, mobile, orgid);
			}else{
				strRet = tzGDObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_WDZH_MOBILE", ctxPath, RegisterInit_url,
						SureTel_url, mobile, orgid);
			}
		} catch (Exception e) {
			e.printStackTrace();
			strRet = "发生异常。" + e.getMessage();
		}

		return strRet;

	}

	@Override
	public String tzOther(String oprType, String strParams, String[] errorMsg) {

		String strRet = "";

		switch (oprType) {
		case "INIT":
			strRet = this.RegisterInit(strParams);
			break;	
		case "SEND":
			strRet = this.sendMessage(strParams,errorMsg);
			break;

		case "SURE":
			strRet = this.SureTel(strParams,errorMsg);
			break;
		}

		return strRet;

	}
	
	//发送验证码-注册
	public String sendMessage(String strParams,String[] errorMsg){
		
		String strPhone = "";   
		String strOrgid = "";
		String strLang = "";
		   
		String strResult = "\"failure\"";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try{
			jacksonUtil.json2Map(strParams);
			if(jacksonUtil.containsKey("phone") 
					&& jacksonUtil.containsKey("orgid") 
					&& jacksonUtil.containsKey("lang"))
				strPhone = String.valueOf(jacksonUtil.getString("phone").trim()).toLowerCase();
				strOrgid = String.valueOf(jacksonUtil.getString("orgid").trim());
		      	strLang =  String.valueOf(jacksonUtil.getString("lang").trim());
		      	
		      	//手机格式;
		      	boolean  bl = validateUtil.validatePhone(strPhone);
		      	if(bl == false){
		      		errorMsg[0] = "1";
		      		errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", 
							"47",  "您填写的手机号码有误", "Malformed phone.");
		            return strResult;
		      	}
		      	
		        //手机是否被占用
		      	String sql = "SELECT COUNT(1) FROM PS_TZ_AQ_YHXX_TBL WHERE LOWER(TZ_MOBILE) = LOWER(?) AND LOWER(TZ_JG_ID)=LOWER(?)";
		      	int count = sqlQuery.queryForObject(sql, new Object[]{strPhone,strOrgid},"Integer");
		      	if(count > 0){
		      		errorMsg[0] = "2";
		      		errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", 
		      				"49", "手机已注册，建议取回密码", "The phone has been registered, proposed to retrieve Password");
		      		return strResult;
		      	}
		      	
		      	//校验验证码的有效期
		      	//DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		      	Date dtYzmValidDate = null;
		      	Date dtYzmAddDate = null;
		      	String strYzm = "";
		      	Date nowDate = new Date();
		      	
		      	boolean sendYzmFlag = true;
		      	
		      	String sqlGetYzmInfo = "SELECT TZ_CNTLOG_ADDTIME,TZ_YZM_YXQ,TZ_SJYZM FROM PS_TZ_SHJI_YZM_TBL WHERE TZ_EFF_FLAG='Y' AND TZ_JG_ID=? AND TZ_MOBILE_PHONE=?  ORDER BY TZ_CNTLOG_ADDTIME DESC LIMIT 0,1";
				Map<String, Object> MapGetYzmInfo = sqlQuery.queryForMap(sqlGetYzmInfo, 
						new Object[] { strOrgid,strPhone });
				if(MapGetYzmInfo == null){
					//发送验证码
				}else{
					try{
						//dtYzmValidDate = format.parse(String.valueOf(MapGetYzmInfo.get("TZ_YZM_YXQ")));
				      	//dtYzmAddDate = format.parse(String.valueOf(MapGetYzmInfo.get("TZ_CNTLOG_ADDTIME")));
						dtYzmValidDate = (Date)(MapGetYzmInfo.get("TZ_YZM_YXQ"));
						dtYzmAddDate = (Date)(MapGetYzmInfo.get("TZ_CNTLOG_ADDTIME"));
				      	strYzm = MapGetYzmInfo.get("TZ_SJYZM") == null ? "" : String.valueOf(MapGetYzmInfo.get("TZ_SJYZM"));
				      	
				      	if(!"".equals(strYzm)){
				      		if(dtYzmValidDate.after(nowDate)){
				      			//发送时间间隔太短
				      			errorMsg[0] = "10";
					      		errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", 
										"128", "发送时间间隔太短,请等待一段时间后再试", "Try again later.");
				      			sendYzmFlag = false;
				      			return strResult;
				      		}else{
				      			if(dtYzmAddDate != null){
				      				Object[] args = new Object[] { strOrgid,strPhone,dtYzmAddDate };
				      				sqlQuery.update("UPDATE PS_TZ_SHJI_YZM_TBL SET TZ_EFF_FLAG='N' WHERE TZ_JG_ID=? AND TZ_MOBILE_PHONE=? AND TZ_CNTLOG_ADDTIME=?", args);
				      			}
				      		}
				      	}
				      	
					}catch(Exception e){
						e.printStackTrace();
					}	
				}
				
				//发送验证码
				if(sendYzmFlag){
					strYzm = String.valueOf("0000" + (int)(Math.random()*100000));
					strYzm = strYzm.substring(strYzm.length()-4, strYzm.length());
					
					PsTzShjiYzmTbl psTzShjiYzmTbl = new PsTzShjiYzmTbl();
					psTzShjiYzmTbl.setTzJgId(strOrgid);
					psTzShjiYzmTbl.setTzMobilePhone(strPhone);
					psTzShjiYzmTbl.setTzCntlogAddtime(new Date());
					psTzShjiYzmTbl.setTzSjyzm(strYzm);
					Calendar ca=Calendar.getInstance();
					ca.setTime(new Date());
					ca.add(Calendar.MINUTE, 1);
					psTzShjiYzmTbl.setTzYzmYxq(ca.getTime());
					psTzShjiYzmTbl.setTzEffFlag("Y");
					psTzShjiYzmTblMapper.insert(psTzShjiYzmTbl);
					
					String getSmsSuffiSql = "SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT = ? LIMIT 1";
					String strSmsSuffix = sqlQuery.queryForObject(getSmsSuffiSql, new Object[] { "TZ_SMS_SEND_SUFFIX" }, "String");
					if(strSmsSuffix == null) strSmsSuffix = "";
					//给当前填写的手机号码发送验证码
					String strSmsContent = "本次验证码为：" + strYzm + strSmsSuffix;
					String strUserName = "";
							
					String oprid = "";
					oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
					
					//得到注册用户姓名;
					String relenameSQL = "SELECT TZ_REALNAME FROM PS_TZ_REG_USER_T WHERE OPRID=? limit 0,1";
					try{
						strUserName = sqlQuery.queryForObject(relenameSQL, new Object[]{oprid},"String");
					}catch(Exception e){
						e.printStackTrace();
					}
					
					//创建邮件短信发送任务
					String taskId = createTaskServiceImpl.createTaskIns(strOrgid, "TZ_SMS_N_001", "SMS", "A");
					if(taskId==null || "".equals(taskId)){
						errorMsg[0] = "30";
						errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "127", "短信发送失败", "Failed to send SMS。");
						return strResult;
					}
					// 创建短信、邮件发送的听众;
					String createAudience = createTaskServiceImpl.createAudience(taskId,strOrgid,"考生申请用户注册手机验证", "JSRW");
					if(createAudience == null || "".equals(createAudience)){
						errorMsg[0] = "31";
						errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "127", "短信发送失败", "Failed to send SMS。");
						return strResult;
					}
					// 为听众添加听众成员;
					boolean addAudCy = createTaskServiceImpl.addAudCy(createAudience,strUserName, strUserName, strPhone, "", "", "", "", oprid, "", "", "");
					if(addAudCy == false){
						errorMsg[0] = "32";
						errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "127", "短信发送失败", "Failed to send SMS。");
						return strResult;
					}
					
					boolean updateSmsSendContent = createTaskServiceImpl.updateSmsSendContent(taskId,strSmsContent);
					if(updateSmsSendContent == false){
						errorMsg[0] = "33";
						errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "127", "短信发送失败", "Failed to send SMS。");
						return strResult;
					}
					sendSmsOrMalServiceImpl.send(taskId, "");
					strResult = "\"success\"";
			        return strResult;
				}    	
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "100";
			errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "55", "获取数据失败，请联系管理员", "Get the data failed, please contact the administrator");
		}
		
		return strResult;
	}

	private String RegisterInit(String strParams) {
		String strRet = "";
		String msg = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			String strMoble = jacksonUtil.getString("");
			String orgid = jacksonUtil.getString("strJgid");

			// 校验手机格式
			//String todo;
			boolean boolPhone = true;

			if (boolPhone) {
				String sql = "select 'Y' from PS_TZ_AQ_YHXX_TBL where TZ_JIHUO_ZT = 'Y' and TZ_MOBILE = ? and TZ_JG_ID=? limit 0,1";
				String phoneExists = sqlQuery.queryForObject(sql, new Object[] { strMoble, orgid }, "String");

				if ("Y".equals(phoneExists)) {
					msg = "该手机已被占用，请重新输入！";
				} else {

					sql = "select TZ_CNTLOG_ADDTIME,TZ_YZM_YXQ,TZ_SJYZM from PS_TZ_SHJI_YZM_TBL where TZ_EFF_FLAG='Y' and TZ_JG_ID=? and TZ_MOBILE_PHONE=? order by TZ_CNTLOG_ADDTIME desc limit 0,1";
					Map<String, Object> mapData = sqlQuery.queryForMap(sql, new Object[] { orgid, strMoble });

					if (mapData != null) {
						String dtFormat = getSysHardCodeVal.getDateTimeFormat();
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dtFormat);

						String strTzSjyzm = mapData.get("TZ_SJYZM") == null ? ""
								: String.valueOf(mapData.get("TZ_SJYZM"));
						Date dateAddTime = simpleDateFormat.parse(mapData.get("TZ_CNTLOG_ADDTIME") == null ? ""
								: String.valueOf(mapData.get("TZ_CNTLOG_ADDTIME")));
						Date dateYXQ = simpleDateFormat.parse(
								mapData.get("TZ_YZM_YXQ") == null ? "" : String.valueOf(mapData.get("TZ_YZM_YXQ")));

						Date dateNow = new Date();

						if (!"".equals(strTzSjyzm)) {

							if (dateYXQ.getTime() > dateNow.getTime()) {
								msg = "shtime";
							} else {
								sql = "update PS_TZ_SHJI_YZM_TBL set TZ_EFF_FLAG='N' where TZ_JG_ID=? and TZ_MOBILE_PHONE=? and TZ_CNTLOG_ADDTIME=?";
								sqlQuery.update(sql, new Object[] { strMoble, orgid, dateAddTime });

								msg = this.registerSave(strMoble, orgid);

							}

						} else {
							msg = this.registerSave(strMoble, orgid);
						}

					} else {
						msg = this.registerSave(strMoble, orgid);
					}

				}

			} else {
				msg = "请输入正确的手机号码！";
			}

		} catch (Exception e) {
			e.printStackTrace();
			msg = "修改失败。" + e.getMessage();
		}

		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("success", msg);
		strRet = jacksonUtil.Map2json(mapRet);

		return strRet;
	}

	/**
	 * 确认修改手机号
	 * 
	 * @param strParams
	 * @return String
	 */
	private String SureTel(String strParams,String[] errorMsg) {
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		String strPhone = "";
		   
		String strOrgid = "";
		String strLang = "";
		String strYzm = ""; 
		String strResult = "\"failure\"";
		
		try {

			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

			jacksonUtil.json2Map(strParams);

			strPhone = jacksonUtil.getString("phone").trim();
			strOrgid = jacksonUtil.getString("orgid").trim();
	      	strLang =  jacksonUtil.getString("lang").trim();
	      	strYzm = jacksonUtil.getString("yzm").trim();

			String sql = "select TZ_YZM_YXQ,TZ_SJYZM from PS_TZ_SHJI_YZM_TBL where TZ_EFF_FLAG='Y' and TZ_JG_ID=? and TZ_MOBILE_PHONE=?";
			Map<String, Object> mapData = sqlQuery.queryForMap(sql, new Object[] { strOrgid, strPhone });

			if (mapData != null) {

				String strTzSjyzm = mapData.get("TZ_SJYZM") == null ? "" : String.valueOf(mapData.get("TZ_SJYZM"));

				if (!"".equals(strTzSjyzm)) {
					Date dtYxq = (Date) mapData.get("TZ_YZM_YXQ");
		      		Date curDate = new Date();
					if (curDate.before(dtYxq)) {
						errorMsg[0] = "10";
			      		errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "130",
			      				"验证码已失效,请重新发送验证码到手机。", "Security Code has timed out!");
					}

					// 校验验证码是否正确
					if (strYzm.toUpperCase().equals(strTzSjyzm.toUpperCase())) {
						// 如果绑定了手机，则修改用户的主要手机时，则要同时修改绑定手机，同时要判断新的绑定手机是否在该机构下重复，如果重复，则修改失败，同时要提示用户;
						sql = "select 'Y' from PS_TZ_AQ_YHXX_TBL where TZ_JG_ID=? and TZ_RYLX=? and TZ_MOBILE=? and OPRID<>?";
						String phoneUsed = sqlQuery.queryForObject(sql, new Object[] { strOrgid, "ZCYH", strPhone, oprid },
								"String");

						if ("Y".equals(phoneUsed)) {
							errorMsg[0] = "10";
				      		errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "131",
				      				"该手机号已被占用，请选择其他手机号。", "The Phone number has been occupied,please change the other phone number!");
						}

						sql = "update PS_TZ_AQ_YHXX_TBL set TZ_MOBILE=?, TZ_SJBD_BZ='Y' where OPRID=? and TZ_JG_ID=? and TZ_RYLX=?";
						sqlQuery.update(sql, new Object[] { strPhone, oprid, strOrgid, "ZCYH" });

						sql = "update PS_TZ_SHJI_YZM_TBL set TZ_EFF_FLAG='N' where TZ_EFF_FLAG='Y' and TZ_JG_ID=? and TZ_MOBILE_PHONE=?";
						sqlQuery.update(sql, new Object[] { strOrgid, strPhone });
						
						strResult = "\"success\"";

					} else {
						errorMsg[0] = "20";
			      		errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "50",
			      				"验证码不正确", "Wrong Verification Code!");
					}
				}
			} else {
				errorMsg[0] = "30";
	      		errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "56",
	      				"参数错误", "Parameters Error !");
			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "100";
			errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "55", 
					"获取数据失败，请联系管理员", "Get the data failed, please contact the administrator");
		}

		return strResult;
	}

	/**
	 * 保存并发送手机验证码
	 * 
	 * @param strMoble
	 * @param orgid
	 * @return String
	 */
	private String registerSave(String strMoble, String orgid) {
		String strRet = "";
		try {

			Random random = new Random();
			int authCode = random.nextInt(9999) % (9999 - 1000 + 1) + 1000;

			String dtFormat = getSysHardCodeVal.getDateTimeFormat();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dtFormat);

			Date dateNow = new Date();

			Date tzYzmYxq = simpleDateFormat.parse(String.valueOf(dateNow.getTime() + 60 * 1000));

			PsTzShjiYzmTbl psTzShjiYzmTbl = new PsTzShjiYzmTbl();
			psTzShjiYzmTbl.setTzJgId(orgid);
			psTzShjiYzmTbl.setTzMobilePhone(strMoble);
			psTzShjiYzmTbl.setTzCntlogAddtime(dateNow);
			psTzShjiYzmTbl.setTzSjyzm(String.valueOf(authCode));
			psTzShjiYzmTbl.setTzYzmYxq(tzYzmYxq);
			psTzShjiYzmTbl.setTzEffFlag("Y");
			psTzShjiYzmTblMapper.insert(psTzShjiYzmTbl);

			// 生成发送内容
			String content = "本次验证码为：" + authCode + "";

			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			String sql = "select TZ_REALNAME from PS_TZ_REG_USER_T where OPRID=?";
			String realname = sqlQuery.queryForObject(sql, new Object[] { oprid }, "String");

			String taskId  = createTaskServiceImpl.createTaskIns(orgid, "TZ_SMS_N_001", "SMS", "A");
			if (taskId == null || "".equals(taskId)) {
				return "创建短信发送任务失败！";
			}

			String createAudience = createTaskServiceImpl.createAudience(taskId,orgid,"高端产品用户手机修改", "JSRW");
			if (null == createAudience || "".equals(createAudience)) {
				return "创建短信发送的听众失败！";
			}

			boolean addAudCy = createTaskServiceImpl.addAudCy(createAudience,realname, realname, strMoble, "", "", "", "", oprid, "",
					"", "");
			if (!addAudCy) {
				return "为听众添加听众成员失败！";
			}

			boolean updateSmsSendContent = createTaskServiceImpl.updateSmsSendContent(taskId,content);
			if (!updateSmsSendContent) {
				return "修改发送内容失败！";
			}

			if (null == taskId || "".equals(taskId)) {
				return "创建任务失败！";
			}

			// 发送短信
			sendSmsOrMalServiceImpl.send(taskId, "");

			strRet = "验证码已发送到您手机，请注意查收！";

		} catch (Exception e) {
			e.printStackTrace();
			strRet = "操作失败。" + e.getMessage();
		}
		return strRet;

	}

}
