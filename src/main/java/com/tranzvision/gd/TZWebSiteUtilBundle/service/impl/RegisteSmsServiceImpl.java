package com.tranzvision.gd.TZWebSiteUtilBundle.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * @author tang
 *  招生网站考生申请人短信处理包
 *  原： TZ_SITE_UTIL_APP:TZ_SITE_SMS_CLS
 */
@Service("com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.RegisteSmsServiceImpl")
public class RegisteSmsServiceImpl extends FrameworkImpl{
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private ValidateUtil validateUtil;
	
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		String strSen = "";
		String strResponse = "\"failure\"";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try{
			jacksonUtil.json2Map(strParams);
			if(jacksonUtil.containsKey("sen")){
				strSen = jacksonUtil.getString("sen");
				//手机号码
				if("1".equals(strSen)){
					strResponse = this.smsVerifyByActive(strParams, errMsg);
				}
				//手机验证码;
				if("2".equals(strSen)){
					strResponse = this.validateSmsCode(strParams, errMsg);
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return strResponse;
	}
	
	public String smsVerifyByActive(String strParams,String[] errorMsg){
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
				strPhone = jacksonUtil.getString("phone").trim();
				strOrgid = jacksonUtil.getString("orgid").trim();
		      	strLang =  jacksonUtil.getString("lang").trim();
		      	
		      	
		      	//手机格式;
		      	ValidateUtil validateUtil = new ValidateUtil();
		      	boolean  bl = validateUtil.validatePhone(strPhone);
		      	if(bl == false){
		      		errorMsg[0] = "1";
		      		errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "47", "手机号码不正确", "The mobile phone is incorrect .");
		            return strResult;
		      	}
		      	
		      	//手机是否被占用
		      	String sql = "SELECT COUNT(1) FROM PS_TZ_AQ_YHXX_TBL WHERE LOWER(TZ_MOBILE) = LOWER(?) AND LOWER(TZ_JG_ID)=LOWER(?)";
		      	int count = jdbcTemplate.queryForObject(sql, new Object[]{strPhone,strOrgid},"Integer");
		      	if(count > 0){
		      		errorMsg[0] = "2";
		      		errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "49", "手机已注册，建议取回密码", "The mobile phone has been registered, proposed to retrieve Password");
		            return strResult;
		      	}
		      	strResult = "\"success\"";
		        return strResult;
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "100";
			errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "55", "获取数据失败，请联系管理员", "Get the data failed, please contact the administrator");
		}
		
		return strResult;
	}
	
	
	public String validateSmsCode(String strParams,String[] errorMsg){
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
				strPhone = jacksonUtil.getString("phone").trim();
				strOrgid = jacksonUtil.getString("orgid").trim();
		      	strLang =  jacksonUtil.getString("lang").trim();
		      	
		      	
		      	//手机格式;
		      	ValidateUtil validateUtil = new ValidateUtil();
		      	boolean  bl = validateUtil.validatePhone(strPhone);
		      	if(bl == false){
		      		errorMsg[0] = "1";
		      		errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "47", "手机号码不正确", "The mobile phone is incorrect .");
		            return strResult;
		      	}
		      	
		      	//手机是否被占用
		      	String sql = "SELECT COUNT(1) FROM PS_TZ_AQ_YHXX_TBL WHERE LOWER(TZ_MOBILE) = LOWER(?) AND LOWER(TZ_JG_ID)=LOWER(?)";
		      	int count = jdbcTemplate.queryForObject(sql, new Object[]{strPhone,strOrgid},"Integer");
		      	if(count > 0){
		      		errorMsg[0] = "2";
		      		errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "49", "邮箱已注册，建议取回密码", "The email address has been registered, proposed to retrieve Password");
		            return strResult;
		      	}
		      	strResult = "\"success\"";
		        return strResult;
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "100";
			errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE", "55", "获取数据失败，请联系管理员", "Get the data failed, please contact the administrator");
		}
		
		return strResult;
	}

}