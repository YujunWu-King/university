package com.tranzvision.gd.TZWebSiteUtilBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * @author tang
 *  招生网站考生申请人邮件处理包
 *  原： TZ_SITE_UTIL_APP:TZ_SITE_MAIL_CLS
 */
@Service("com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.RegisteMalServiceImpl")
public class RegisteMalServiceImpl extends FrameworkImpl{
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private ValidateUtil validateUtil;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private PsTzDzyxYzmTblMapper psTzDzyxYzmTblMapper;
	@Autowired
	private CreateTaskServiceImpl createTaskServiceImpl;
	@Autowired
	private SendSmsOrMalServiceImpl sendSmsOrMalServiceImpl;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		String strSen = "";
		String strResponse = "\"failure\"";
		JacksonUtil jacksonUtil = new JacksonUtil();   
		try{
			jacksonUtil.json2Map(strParams);
			if(jacksonUtil.containsKey("sen")){
				strSen = jacksonUtil.getString("sen");
				if("1".equals(strSen)){
					return this.emailVerifyByEnroll(strParams, errMsg);
				}
				
				if("2".equals(strSen)){
					return this.emailSendByEnroll(strParams, errMsg);
				}
				
				if("3".equals(strSen)){
					return this.emailVerifyByActive(strParams, errMsg);
				}
				
				if("4".equals(strSen)){
					return this.emailSendByActive(strParams, errMsg);
				}
				
				if("5".equals(strSen)){
					return this.emailVerifyByForget(strParams, errMsg);
				}
				
				if("6".equals(strSen)){
					return this.emailSendByPass(strParams, errMsg);
				}
				
				if("7".equals(strSen)){
					return this.emailVerifyByChange(strParams, errMsg);
				}
				
				if("8".equals(strSen)){
					return this.emailSendByChange(strParams, errMsg);
				}
				//以下为清华MBA手机版香港
				if("11".equals(strSen)){
					return this.emailVerifyByMEnroll(strParams, errMsg);
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return strResponse;
	}
	
	//校验注册邮箱格式以及是否被占用
	public String emailVerifyByEnroll(String strParams,String[] errorMsg){
		String strEmail = "";
		   
		String strOrgid = "";
		String strLang = "";
		   
		String strResult = "\"failure\"";
		JacksonUtil jacksonUtil = new JacksonUtil();   
		try{
			jacksonUtil.json2Map(strParams);
			if(jacksonUtil.containsKey("email") 
					&& jacksonUtil.containsKey("orgid") 
					&& jacksonUtil.containsKey("lang")){
				strEmail = jacksonUtil.getString("email").trim();
		      	strOrgid = jacksonUtil.getString("orgid").trim();
		      	strLang =  jacksonUtil.getString("lang").trim();
		      	
		      	//校验邮箱长度;
		      	if("".equals(strEmail) || strEmail.length()<6 || strEmail.length()>70 ){
		      		errorMsg[0] = "1";
		      		errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "52", "邮箱长度需满足6-70个字符", "Email length required to meet 6-70 characters");
		            return strResult;
		      	}
		      	//校验邮箱格式;
		      	ValidateUtil validateUtil = new ValidateUtil();
		      	boolean  bl = validateUtil.validateEmail(strEmail);
		      	if(bl == false){
		      		errorMsg[0] = "2";
		      		errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "53", "邮箱格式不正确", "Mailbox format is not correct.");
		            return strResult;
		      	}
		      	
		      	//邮箱是否被占用
		      	String sql = "SELECT COUNT(1) FROM PS_TZ_AQ_YHXX_TBL WHERE LOWER(TZ_EMAIL) = LOWER(?) AND LOWER(TZ_JG_ID)=LOWER(?)";
		      	int count = jdbcTemplate.queryForObject(sql, new Object[]{strEmail,strOrgid},"Integer");
		      	if(count > 0){
		      		errorMsg[0] = "3";
		      		errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "48", "邮箱已注册，建议取回密码", "It has been occupied!");
		            return strResult;
		      	}
		      	strResult = "\"success\"";
		        return strResult;
			}else{
				errorMsg[0] = "100";
				errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "55", "获取数据失败，请联系管理员", "Get the data failed, please contact the administrator");
			}
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "100";
			errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "55", "获取数据失败，请联系管理员", "Get the data failed, please contact the administrator");
		}
		
		return strResult;
	}
	
	public String emailVerifyByMEnroll(String strParams,String[] errorMsg){
		String strEmail = "";
		   
		String strOrgid = "";
		String strLang = "";
		   
		String strResult = "\"failure\"";
		JacksonUtil jacksonUtil = new JacksonUtil();   
		try{
			jacksonUtil.json2Map(strParams);
			if(jacksonUtil.containsKey("email") 
					&& jacksonUtil.containsKey("orgid") 
					&& jacksonUtil.containsKey("lang")){
				strEmail = jacksonUtil.getString("email").trim();
		      	strOrgid = jacksonUtil.getString("orgid").trim();
		      	strLang =  jacksonUtil.getString("lang").trim();
		      	
		      	//校验邮箱长度;
		      	if("".equals(strEmail) || strEmail.length()<6 || strEmail.length()>70 ){
		      		errorMsg[0] = "1";
		      		errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "52", "邮箱长度需满足6-70个字符", "Email length required to meet 6-70 characters");
		            return strResult;
		      	}
		      	//校验邮箱格式;
		      	ValidateUtil validateUtil = new ValidateUtil();
		      	boolean  bl = validateUtil.validateEmail(strEmail);
		      	if(bl == false){
		      		errorMsg[0] = "2";
		      		errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "53", "邮箱格式不正确", "Mailbox format is not correct.");
		            return strResult;
		      	}
		      	
		      	//邮箱是否被占用
		      	String sql = "SELECT COUNT(1) FROM PS_TZ_AQ_YHXX_TBL WHERE LOWER(TZ_EMAIL) = LOWER(?) AND LOWER(TZ_JG_ID)=LOWER(?)";
		      	int count = jdbcTemplate.queryForObject(sql, new Object[]{strEmail,strOrgid},"Integer");
		      	if(count > 0){
		      		errorMsg[0] = "3";
		      		errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "48", "邮箱已注册，建议取回密码", "It has been occupied!");
		            return strResult;
		      	}
		      	strResult = "\"success\"";
		        return strResult;
			}else{
				errorMsg[0] = "100";
				errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "55", "获取数据失败，请联系管理员", "Get the data failed, please contact the administrator");
			}
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "100";
			errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "55", "获取数据失败，请联系管理员", "Get the data failed, please contact the administrator");
		}
		
		return strResult;
	}
	//发送邮箱激活邮件
	public String emailSendByEnroll(String strParams,String[] errorMsg){
		String strEmail = "";  
		String strOrgid = "";
		String strDlzhid = "";
		String strLang = "";
	    String strUserName = "";
	    String siteid = "";

		String strResult = "\"failure\"";
		JacksonUtil jacksonUtil = new JacksonUtil();   
		try{
			jacksonUtil.json2Map(strParams);
			if(jacksonUtil.containsKey("email") 
					&& jacksonUtil.containsKey("orgid") 
					&& jacksonUtil.containsKey("lang")
					&& jacksonUtil.containsKey("dlzhid")){
				strEmail = jacksonUtil.getString("email").trim();
				strOrgid = jacksonUtil.getString("orgid").trim();
				strLang =  jacksonUtil.getString("lang").trim();
		      	strDlzhid =  jacksonUtil.getString("dlzhid").trim();
		      	siteid =  jacksonUtil.getString("siteid").trim();
		      	
		      	// 生成邮件发送令牌;
		      	String strSeq = "00000000" + String.valueOf(getSeqNum.getSeqNum("TZ_GD_REGCODE", "TZ_GD_EMAIL"));
		      	strSeq = strSeq.substring(strSeq.length()-8, strSeq.length());
		      	SimpleDateFormat datetimeFormate = new SimpleDateFormat("yyyyMMddHHmmss");
				String strYZM = String.valueOf((int)(Math.random()*100000)) + datetimeFormate.format(new Date())+strSeq;
				
				PsTzDzyxYzmTbl psTzDzyxYzmTbl = new PsTzDzyxYzmTbl();
				psTzDzyxYzmTbl.setTzDlzhId(strDlzhid);
				psTzDzyxYzmTbl.setTzJgId(strOrgid);
				psTzDzyxYzmTbl.setTzTokenCode(strYZM);
				psTzDzyxYzmTbl.setTzTokenType("REG");
				psTzDzyxYzmTbl.setTzEmail(strEmail);
				Date currentDate = new Date();
				psTzDzyxYzmTbl.setTzCntlogAddtime(currentDate);

				Calendar ca=Calendar.getInstance();
				ca.setTime(new Date());
				ca.add(Calendar.MINUTE, 30);
				Date yxqDate = ca.getTime();
				psTzDzyxYzmTbl.setTzYzmYxq(yxqDate);
				
				psTzDzyxYzmTbl.setTzEffFlag("Y");
				psTzDzyxYzmTblMapper.insert(psTzDzyxYzmTbl);
				
				// 发送邮件;
				String taskId = "";
				if("ENG".equals(strLang)){
					taskId = createTaskServiceImpl.createTaskIns(strOrgid, "TZ_EML_ACT_EN", "MAL", "A");
				}else{
					taskId = createTaskServiceImpl.createTaskIns(strOrgid, "TZ_EML_ACT_ZH", "MAL", "A");
				}
				if(taskId == null || "".equals(taskId)){
					errorMsg[0] = "30";
					errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "57", "邮件发送失败", "Failed to send mail");
					return strResult;
				}
				
				// 创建短信、邮件发送的听众;
				String createAudience = createTaskServiceImpl.createAudience(taskId,strOrgid,"考生申请人注册邮箱激活", "JSRW");
				if(createAudience == null || "".equals(createAudience)){
					errorMsg[0] = "31";
					errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "57", "邮件发送失败", "Failed to send mail");
					return strResult;
				}
				
				//得到注册用户姓名;
				String relenameSQL = "SELECT TZ_REALNAME FROM PS_TZ_REG_USER_T WHERE OPRID=? limit 0,1";
				try{
					strUserName = jdbcTemplate.queryForObject(relenameSQL, new Object[]{strDlzhid},"String");
				}catch(Exception e){
					e.printStackTrace();
				}
				
				// 为听众添加听众成员;
				boolean addAudCy = createTaskServiceImpl.addAudCy(createAudience,strUserName, strUserName, "", "", strEmail, "", "", strDlzhid, "", "", "");
				if(addAudCy == false){
					errorMsg[0] = "32";
					errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "57", "邮件发送失败", "Failed to send mail");
					return strResult;
				}
				
				// 得到创建的任务ID;
		        if(taskId == null || "".equals(taskId)){
		        	errorMsg[0] = "32";
					errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "57", "邮件发送失败", "Failed to send mail");
					return strResult;
		        }
		        
		        sendSmsOrMalServiceImpl.send(taskId, "");
		        strResult = "\"success\"";
		        return strResult;
				
			}else{
				errorMsg[0] = "100";
				errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "55", "获取数据失败，请联系管理员", "Get the data failed, please contact the administrator");
			}
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "100";
			errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "55", "获取数据失败，请联系管理员", "Get the data failed, please contact the administrator");
		}
		
		return strResult;
	}
	
	public String emailVerifyByActive(String strParams,String[] errorMsg){
		String strEmail = "";
		String strOrgid = "";
		String strLang = "";
		String strResult = "\"failure\"";
		JacksonUtil jacksonUtil = new JacksonUtil(); 
		try{
			jacksonUtil.json2Map(strParams);
			if(jacksonUtil.containsKey("email") 
					&& jacksonUtil.containsKey("orgid") 
					&& jacksonUtil.containsKey("lang")){
				strEmail = jacksonUtil.getString("email");
			    strOrgid = jacksonUtil.getString("orgid");
			    strLang = jacksonUtil.getString("lang");
			    
			    //校验邮箱长度
			    if(strEmail == null || "".equals(strEmail)
			    	|| strEmail.length() < 6
			    	|| strEmail.length() > 70){
			    	errorMsg[0] = "1";
					errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "52", "邮箱长度需满足6-70个字符", "Email length required to meet 6-70 characters");
					return strResult;
			    }
			    
			    //校验邮箱格式
			    boolean bl = validateUtil.validateEmail(strEmail);
			    if(bl == false){
			    	errorMsg[0] = "2";
					errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "53", "邮箱格式不正确", "Mailbox format is not correct.");
					return strResult;
			    }
			    
			   //邮箱是否存在;
		      	String sql = "SELECT COUNT(1) FROM PS_TZ_AQ_YHXX_TBL WHERE LOWER(TZ_EMAIL) = LOWER(?) AND LOWER(TZ_JG_ID)=LOWER(?) AND TZ_JIHUO_ZT <>'Y'";
		      	int count = jdbcTemplate.queryForObject(sql, new Object[]{strEmail,strOrgid},"Integer");
		      	if(count <= 0){
		      		errorMsg[0] = "3";
		      		errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "54", "邮箱不存在，请先注册", "The mailbox does not exist, please register");
		            return strResult;
		      	}
		      	
		      	strResult = "\"success\"";
		        return strResult;
			}else{
				errorMsg[0] = "100";
				errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "55", "获取数据失败，请联系管理员", "Get the data failed, please contact the administrator");
				return strResult;
			}
							
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "100";
			errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "55", "获取数据失败，请联系管理员", "Get the data failed, please contact the administrator");
			return strResult;
		}
		
	}
	
	
	public String emailSendByActive(String strParams,String[] errorMsg){
		String strEmail = "";  
		String strOrgid = "";
		String strDlzhid = "";
		String strLang = "";
	    String strUserName = "";

		String strResult = "\"failure\"";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try{
			jacksonUtil.json2Map(strParams);
			if(jacksonUtil.containsKey("email") 
					&& jacksonUtil.containsKey("orgid") 
					&& jacksonUtil.containsKey("lang")){
				strEmail = jacksonUtil.getString("email").trim();
				strOrgid = jacksonUtil.getString("orgid").trim();
				strLang =  jacksonUtil.getString("lang").trim();
		      	
		      	
		      	// 生成邮件发送令牌;
		      	String strSeq = "00000000" + String.valueOf(getSeqNum.getSeqNum("TZ_GD_ACTCODE", "TZ_GD_EMAIL"));
		      	strSeq = strSeq.substring(strSeq.length()-8, strSeq.length());
		      	SimpleDateFormat datetimeFormate = new SimpleDateFormat("yyyyMMddHHmmss");
				String strYZM = String.valueOf((int)(Math.random()*100000)) + datetimeFormate.format(new Date())+strSeq;
				
				String zhSQL = "SELECT TZ_DLZH_ID FROM PS_TZ_AQ_YHXX_TBL WHERE LOWER(TZ_EMAIL) = LOWER(?) AND LOWER(TZ_JG_ID)=LOWER(?) AND TZ_JIHUO_ZT <>'Y'";
				strDlzhid = jdbcTemplate.queryForObject(zhSQL, new Object[]{strEmail,strOrgid},"String");
				
				PsTzDzyxYzmTbl psTzDzyxYzmTbl = new PsTzDzyxYzmTbl();
				psTzDzyxYzmTbl.setTzDlzhId(strDlzhid);
				psTzDzyxYzmTbl.setTzJgId(strOrgid);
				psTzDzyxYzmTbl.setTzTokenCode(strYZM);
				psTzDzyxYzmTbl.setTzTokenType("REG");
				psTzDzyxYzmTbl.setTzEmail(strEmail);
				Date currentDate = new Date();
				psTzDzyxYzmTbl.setTzCntlogAddtime(currentDate);

				Calendar ca=Calendar.getInstance();
				ca.setTime(new Date());
				ca.add(Calendar.MINUTE, 30);
				Date yxqDate = ca.getTime();
				psTzDzyxYzmTbl.setTzYzmYxq(yxqDate);
				
				psTzDzyxYzmTbl.setTzEffFlag("Y");
				psTzDzyxYzmTblMapper.insert(psTzDzyxYzmTbl);
				
				// 发送邮件;
				String taskId = "";
				if("ENG".equals(strLang)){
					taskId = createTaskServiceImpl.createTaskIns(strOrgid, "TZ_EML_ACT_EN", "MAL", "A");
				}else{
					taskId = createTaskServiceImpl.createTaskIns(strOrgid, "TZ_EML_ACT_ZH", "MAL", "A");
				}
				if(taskId == null || "".equals(taskId)){
					errorMsg[0] = "30";
					errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "57", "邮件发送失败", "Failed to send mail");
					return strResult;
				}
				
				// 创建短信、邮件发送的听众;
				String createAudience = createTaskServiceImpl.createAudience(taskId,strOrgid,"考生申请人注册邮箱激活", "JSRW");
				if(createAudience == null || "".equals(createAudience)){
					errorMsg[0] = "31";
					errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "57", "邮件发送失败", "Failed to send mail");
					return strResult;
				}
				
				//得到注册用户姓名;
				String relenameSQL = "SELECT TZ_REALNAME FROM PS_TZ_REG_USER_T WHERE OPRID=? limit 0,1";
				try{
					strUserName = jdbcTemplate.queryForObject(relenameSQL, new Object[]{strDlzhid},"String");
				}catch(Exception e){
					e.printStackTrace();
				}
				
				// 为听众添加听众成员;
				boolean addAudCy = createTaskServiceImpl.addAudCy(createAudience,strUserName, strUserName, "", "", strEmail, "", "", strDlzhid, "", "", "");
				if(addAudCy == false){
					errorMsg[0] = "32";
					errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "57", "邮件发送失败", "Failed to send mail");
					return strResult;
				}
				
				// 得到创建的任务ID;
		        if(taskId == null || "".equals(taskId)){
		        	errorMsg[0] = "32";
					errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "57", "邮件发送失败", "Failed to send mail");
					return strResult;
		        }
		        
		        sendSmsOrMalServiceImpl.send(taskId, "");
		        strResult = "\"success\"";
		        return strResult;
				
			}else{
				errorMsg[0] = "100";
				errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "55", "获取数据失败，请联系管理员", "Get the data failed, please contact the administrator");
			}
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "100";
			errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "55", "获取数据失败，请联系管理员", "Get the data failed, please contact the administrator");
		}
		
		return strResult;
		
	}
	
	
	public String emailVerifyByForget(String strParams,String[] errorMsg){
		String strEmail = "";
		String strOrgid = "";
		String strLang = "";
		String strResult = "\"failure\"";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try{
			jacksonUtil.json2Map(strParams);
			if(jacksonUtil.containsKey("email") 
					&& jacksonUtil.containsKey("orgid") 
					&& jacksonUtil.containsKey("lang")){
				strEmail = jacksonUtil.getString("email");
			    strOrgid = jacksonUtil.getString("orgid");
			    strLang = jacksonUtil.getString("lang");
			    
			    //校验邮箱长度
			    if(strEmail == null || "".equals(strEmail)
			    	|| strEmail.length() < 6
			    	|| strEmail.length() > 70){
			    	errorMsg[0] = "1";
					errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "52", "邮箱长度需满足6-70个字符", "Email length required to meet 6-70 characters");
					return strResult;
			    }
			    
			    //校验邮箱格式
			    boolean bl = validateUtil.validateEmail(strEmail);
			    if(bl == false){
			    	errorMsg[0] = "2";
					errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "53", "邮箱格式不正确", "Mailbox format is not correct.");
					return strResult;
			    }
			    
			   //邮箱是否存在
		      	String sql = "SELECT COUNT(1) FROM PS_TZ_AQ_YHXX_TBL A WHERE LOWER(A.TZ_EMAIL) = LOWER(?) AND LOWER(A.TZ_JG_ID)=LOWER(?) AND A.TZ_JIHUO_ZT ='Y' AND A.TZ_YXBD_BZ='Y'";
		      	int count = jdbcTemplate.queryForObject(sql, new Object[]{strEmail,strOrgid},"Integer");
		      	if(count <= 0){
		      		errorMsg[0] = "3";
		      		errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "54", "邮箱不存在，请先注册", "The mailbox does not exist, please register");
		            return strResult;
		      	}
		      	
		      	//判断该账号是否已锁定
		      	sql = "SELECT COUNT(1) FROM PS_TZ_AQ_YHXX_TBL A WHERE LOWER(A.TZ_EMAIL) = LOWER(?) AND LOWER(A.TZ_JG_ID)=LOWER(?) AND A.TZ_JIHUO_ZT ='Y' AND exists(SELECT ACCTLOCK FROM PSOPRDEFN WHERE OPRID=A.OPRID AND ACCTLOCK='0')";
		      	count = jdbcTemplate.queryForObject(sql, new Object[]{strEmail,strOrgid},"Integer");
		      	if(count <= 0){
		      		errorMsg[0] = "3";
		      		errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "133", "抱歉，该账号已锁定。", "Sorry,this account has been locked.");
		            return strResult;
		      	}
		      	
		      	strResult = "\"success\"";
		        return strResult;
			}else{
				errorMsg[0] = "100";
				errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "55", "获取数据失败，请联系管理员", "Get the data failed, please contact the administrator");
				return strResult;
			}
							
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "100";
			errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "55", "获取数据失败，请联系管理员", "Get the data failed, please contact the administrator");
			return strResult;
		}
		
	}
	
	
	public String emailSendByPass(String strParams,String[] errorMsg){
		String strEmail = "";  
		String strOrgid = "";
		String strDlzhid = "";
		String strLang = "";
	    String strUserName = "";

		String strResult = "\"failure\"";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try{
			jacksonUtil.json2Map(strParams);
			if(jacksonUtil.containsKey("email") 
					&& jacksonUtil.containsKey("orgid") 
					&& jacksonUtil.containsKey("lang")){
				strEmail = jacksonUtil.getString("email").trim();
				strOrgid = jacksonUtil.getString("orgid").trim();
				strLang =  jacksonUtil.getString("lang").trim();
		      	
		      	
		      	// 生成邮件发送令牌;
		      	String strSeq = "00000000" + String.valueOf(getSeqNum.getSeqNum("TZ_GD_ACTCODE", "TZ_GD_EMAIL"));
		      	strSeq = strSeq.substring(strSeq.length()-8, strSeq.length());
		      	SimpleDateFormat datetimeFormate = new SimpleDateFormat("yyyyMMddHHmmss");
				String strYZM = String.valueOf((int)(Math.random()*100000)) + datetimeFormate.format(new Date())+strSeq;
				
				String zhSQL = "SELECT TZ_DLZH_ID FROM PS_TZ_AQ_YHXX_TBL WHERE LOWER(TZ_EMAIL) = LOWER(?) AND LOWER(TZ_JG_ID)=LOWER(?) AND TZ_JIHUO_ZT ='Y'";
				strDlzhid = jdbcTemplate.queryForObject(zhSQL, new Object[]{strEmail,strOrgid},"String");
				
				PsTzDzyxYzmTbl psTzDzyxYzmTbl = new PsTzDzyxYzmTbl();
				psTzDzyxYzmTbl.setTzDlzhId(strDlzhid);
				psTzDzyxYzmTbl.setTzJgId(strOrgid);
				psTzDzyxYzmTbl.setTzTokenCode(strYZM);
				psTzDzyxYzmTbl.setTzTokenType("EDIT");
				psTzDzyxYzmTbl.setTzEmail(strEmail);
				Date currentDate = new Date();
				psTzDzyxYzmTbl.setTzCntlogAddtime(currentDate);

				Calendar ca=Calendar.getInstance();
				ca.setTime(new Date());
				ca.add(Calendar.MINUTE, 30);
				Date yxqDate = ca.getTime();
				psTzDzyxYzmTbl.setTzYzmYxq(yxqDate);
				
				psTzDzyxYzmTbl.setTzEffFlag("Y");
				psTzDzyxYzmTblMapper.insert(psTzDzyxYzmTbl);
				
				// 发送邮件;
				String taskId = "";
				if("ENG".equals(strLang)){
					taskId = createTaskServiceImpl.createTaskIns(strOrgid, "TZ_EML_PASS_EN", "MAL", "A");
				}else{
					taskId = createTaskServiceImpl.createTaskIns(strOrgid, "TZ_EML_PASS_ZH", "MAL", "A");
				}
				if(taskId == null || "".equals(taskId)){
					errorMsg[0] = "30";
					errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "57", "邮件发送失败", "Failed to send mail");
					return strResult;
				}
				
				// 创建短信、邮件发送的听众;
				String createAudience = createTaskServiceImpl.createAudience(taskId,strOrgid,"考生申请人注册邮箱激活", "JSRW");
				if(createAudience == null || "".equals(createAudience)){
					errorMsg[0] = "31";
					errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "57", "邮件发送失败", "Failed to send mail");
					return strResult;
				}
				
				//得到注册用户姓名;
				String relenameSQL = "SELECT TZ_REALNAME FROM PS_TZ_REG_USER_T WHERE OPRID=? limit 0,1";
				try{
					strUserName = jdbcTemplate.queryForObject(relenameSQL, new Object[]{strDlzhid},"String");
				}catch(Exception e){
					e.printStackTrace();
				}
				
				// 为听众添加听众成员;
				boolean addAudCy = createTaskServiceImpl.addAudCy(createAudience,strUserName, strUserName, "", "", strEmail, "", "", strDlzhid, "", "", "");
				if(addAudCy == false){
					errorMsg[0] = "32";
					errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "57", "邮件发送失败", "Failed to send mail");
					return strResult;
				}
				
				// 得到创建的任务ID;
		        if(taskId == null || "".equals(taskId)){
		        	errorMsg[0] = "32";
					errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "57", "邮件发送失败", "Failed to send mail");
					return strResult;
		        }
		        
		        sendSmsOrMalServiceImpl.send(taskId, "");
		        strResult = "\"success\"";
		        return strResult;
				
			}else{
				errorMsg[0] = "100";
				errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "55", "获取数据失败，请联系管理员", "Get the data failed, please contact the administrator");
			}
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "100";
			errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "55", "获取数据失败，请联系管理员", "Get the data failed, please contact the administrator");
		}
		
		return strResult;
		
	}
	
	
	public String emailVerifyByChange(String strParams,String[] errorMsg){
		String strEmail = "";
		String strOrgid = "";
		String strLang = "";
		String strResult = "\"failure\"";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try{
			jacksonUtil.json2Map(strParams);
			if(jacksonUtil.containsKey("email") 
					&& jacksonUtil.containsKey("orgid") 
					&& jacksonUtil.containsKey("lang")){
				strEmail = jacksonUtil.getString("email");
			    strOrgid = jacksonUtil.getString("orgid");
			    strLang = jacksonUtil.getString("lang");
			    
			    //校验邮箱长度
			    if(strEmail == null || "".equals(strEmail)
			    	|| strEmail.length() < 6
			    	|| strEmail.length() > 70){
			    	errorMsg[0] = "1";
					errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "52", "邮箱长度需满足6-70个字符", "Email length required to meet 6-70 characters");
					return strResult;
			    }
			    
			    //校验邮箱格式
			    boolean bl = validateUtil.validateEmail(strEmail);
			    if(bl == false){
			    	errorMsg[0] = "2";
					errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "53", "邮箱格式不正确", "Mailbox format is not correct.");
					return strResult;
			    }
			    
			   //邮箱是否存在
		      	String sql = "SELECT COUNT(1) FROM PS_TZ_AQ_YHXX_TBL WHERE LOWER(TZ_EMAIL) = LOWER(?) AND LOWER(TZ_JG_ID)=LOWER(?) AND TZ_YXBD_BZ='Y'";
		      	int count = jdbcTemplate.queryForObject(sql, new Object[]{strEmail,strOrgid},"Integer");
		      	if(count > 0){
		      		errorMsg[0] = "3";
		      		errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "67", "邮箱已被占用，请更换", "Mailbox already exists, please change");
		            return strResult;
		      	}
		      	
		      	strResult = "\"success\"";
		        return strResult;
			}else{
				errorMsg[0] = "100";
				errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "55", "获取数据失败，请联系管理员", "Get the data failed, please contact the administrator");
				return strResult;
			}
							
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "100";
			errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "55", "获取数据失败，请联系管理员", "Get the data failed, please contact the administrator");
			return strResult;
		}
		
	}
	
	
	public String emailSendByChange(String strParams,String[] errorMsg){
		String strEmail = "";  
		String strOrgid = "";
		String strDlzhid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		String strLang = "";
	    String strUserName = "";

		String strResult = "\"failure\"";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try{
			jacksonUtil.json2Map(strParams);
			if(jacksonUtil.containsKey("email") 
					&& jacksonUtil.containsKey("orgid") 
					&& jacksonUtil.containsKey("lang")){
				strEmail = jacksonUtil.getString("email").trim();
				strOrgid = jacksonUtil.getString("orgid").trim();
				strLang =  jacksonUtil.getString("lang").trim();
		      	
		      	
		      	// 生成邮件发送令牌;
		      	String strSeq = "00000000" + String.valueOf(getSeqNum.getSeqNum("TZ_GD_CHANGECODE", "TZ_GD_EMAIL"));
		      	strSeq = strSeq.substring(strSeq.length()-8, strSeq.length());
		      	SimpleDateFormat datetimeFormate = new SimpleDateFormat("yyyyMMddHHmmss");
				String strYZM = String.valueOf((int)(Math.random()*100000)) + datetimeFormate.format(new Date())+strSeq;
				
				PsTzDzyxYzmTbl psTzDzyxYzmTbl = new PsTzDzyxYzmTbl();
				psTzDzyxYzmTbl.setTzDlzhId(strDlzhid);
				psTzDzyxYzmTbl.setTzJgId(strOrgid);
				psTzDzyxYzmTbl.setTzTokenCode(strYZM);
				psTzDzyxYzmTbl.setTzTokenType("EDIT");
				psTzDzyxYzmTbl.setTzEmail(strEmail);
				Date currentDate = new Date();
				psTzDzyxYzmTbl.setTzCntlogAddtime(currentDate);

				Calendar ca=Calendar.getInstance();
				ca.setTime(new Date());
				ca.add(Calendar.MINUTE, 30);
				Date yxqDate = ca.getTime();
				psTzDzyxYzmTbl.setTzYzmYxq(yxqDate);
				
				psTzDzyxYzmTbl.setTzEffFlag("Y");
				psTzDzyxYzmTblMapper.insert(psTzDzyxYzmTbl);
				
				// 发送邮件;
				String taskId = "";
				if("ENG".equals(strLang)){
					taskId = createTaskServiceImpl.createTaskIns(strOrgid, "TZ_EML_CHG_EN", "MAL", "A");
				}else{
					taskId = createTaskServiceImpl.createTaskIns(strOrgid, "TZ_EML_CHG_EN", "MAL", "A");
				}
				if(taskId == null || "".equals(taskId)){
					errorMsg[0] = "30";
					errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "57", "邮件发送失败", "Failed to send mail");
					return strResult;
				}
				
				// 创建短信、邮件发送的听众;
				String createAudience = createTaskServiceImpl.createAudience(taskId,strOrgid,"考生申请人注册邮箱激活", "JSRW");
				if(createAudience == null || "".equals(createAudience)){
					errorMsg[0] = "31";
					errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "57", "邮件发送失败", "Failed to send mail");
					return strResult;
				}
				
				//得到注册用户姓名;
				String relenameSQL = "SELECT TZ_REALNAME FROM PS_TZ_REG_USER_T WHERE OPRID=? limit 0,1";
				try{
					strUserName = jdbcTemplate.queryForObject(relenameSQL, new Object[]{strDlzhid},"String");
				}catch(Exception e){
					e.printStackTrace();
				}
				
				// 为听众添加听众成员;
				boolean addAudCy = createTaskServiceImpl.addAudCy(createAudience,strUserName, strUserName, "", "", strEmail, "", "", strDlzhid, "", "", "");
				if(addAudCy == false){
					errorMsg[0] = "32";
					errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "57", "邮件发送失败", "Failed to send mail");
					return strResult;
				}
				
				// 得到创建的任务ID;
		        if(taskId == null || "".equals(taskId)){
		        	errorMsg[0] = "32";
					errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "57", "邮件发送失败", "Failed to send mail");
					return strResult;
		        }
		        
		        sendSmsOrMalServiceImpl.send(taskId, "");
		        strResult = "\"success\"";
		        return strResult;
				
			}else{
				errorMsg[0] = "100";
				errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "55", "获取数据失败，请联系管理员", "Get the data failed, please contact the administrator");
			}
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "100";
			errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "55", "获取数据失败，请联系管理员", "Get the data failed, please contact the administrator");
		}
		
		return strResult;
		
	}
	

	
	//生成注册用户发送的邮件内容: 已经写到com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.EmlSmsGetParamter类下;
    //public String createUrlforEnroll;

}
