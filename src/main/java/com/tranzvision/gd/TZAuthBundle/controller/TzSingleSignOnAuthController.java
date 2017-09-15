package com.tranzvision.gd.TZAuthBundle.controller;

import java.security.MessageDigest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzSingleSignOnAuthServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 单点登录登录认证控制器
 * 
 * @author ShaweYet
 * @since 2017-09-12
 */
@Controller
@RequestMapping(value = { "/sso/auth" })
public class TzSingleSignOnAuthController {

	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private TzSingleSignOnAuthServiceImpl ssoImpl;
	
	/*
	 * 错误代码：
	 * 1001：参数不正确
	 * 1002：不存在此应用ID
	 * 1003：找不到该用户或者用户未启用
	 * 1004：Ticket已经被使用
	 * 1005：验证失败
	 * 1006：登录失败
	 * 1099：未知的错误
	 * */
	public static final String TZ_PARAS_WRONG = "1001";
	public static final String TZ_APPID_INVALID = "1002";
	public static final String TZ_USER_INVALID = "1003";
	public static final String TZ_TICKET_REPEAT = "1004";
	public static final String TZ_AUTH_FAILURE = "1005";
	public static final String TZ_LOGIN_FAILURE = "1006";
	public static final String TZ_UNSPECIFIED_ERROR = "1099";
	
	private final String strErrorForward = "forward:/sso/auth/result";
	private final String strSsoAuthCode = "TZ_SSO_AUTH_CODE";
	
	@RequestMapping(value = { "", "/" }, produces = "text/html;charset=UTF-8")
	public String auth(HttpServletRequest request, HttpServletResponse response) {

		/*
		 * strApplicationId 字符串，应用分配appid
		 * strUserName		字符串，应用分配定义下的用户名
		 * strTicket		字符串，第三方认证自定义唯一标签，建议使用当前时间毫秒+随机数，该ticket同一appid和用户只能使用一次
		 * strAccessToken	字符串，由上面三个参数混合加密而得。
		 * */
		String strApplicationId	=	request.getParameter("appId");
		String strUserName 		=	request.getParameter("userName");
		String strTicket 		=	request.getParameter("ticket");
		String strAccessToken 	=	request.getParameter("accessToken");

		try {
			if(strApplicationId==null||strUserName==null||strTicket==null||strAccessToken==null
					||"".equals(strApplicationId)||"".equals(strUserName)||"".equals(strTicket)||"".equals(strAccessToken)){
				request.setAttribute(strSsoAuthCode,TZ_PARAS_WRONG);
				return strErrorForward;
			}
		
			String strOrgId = "" ,strAppSecret = "";
			Map<String,Object> appMap= sqlQuery.queryForMap("SELECT TZ_JG_ID,TZ_TRANZ_APPSECRET FROM PS_TZ_TRANZ_APP_TBL WHERE TZ_TRANZ_APPID=? LIMIT 0,1",
					new Object[]{strApplicationId});
			if(appMap==null){
				request.setAttribute(strSsoAuthCode,TZ_APPID_INVALID);
				return strErrorForward;
			}else{
				 strOrgId= (String)appMap.get("TZ_JG_ID");
				 strAppSecret= (String)appMap.get("TZ_TRANZ_APPSECRET");
			}
			
			String strUserAccount = "";
			Map<String,Object> userMap = sqlQuery.queryForMap("SELECT TZ_DLZH_ID FROM PS_TZ_TAPP_USER_TBL WHERE TZ_TRANZ_APPID=? AND TZ_OTH_USER=? AND TZ_ENABLE='Y' LIMIT 0,1",
					new Object[]{strApplicationId,strUserName});
			if(userMap==null){
				request.setAttribute(strSsoAuthCode,TZ_USER_INVALID);
				return strErrorForward;
			}else{
				strUserAccount= (String)userMap.get("TZ_DLZH_ID");
			}
			
			String strTicketRepeat = sqlQuery.queryForObject("SELECT 'Y' FROM TZ_SSO_AUTH_TBL WHERE TZ_APP_ID=? AND TZ_TICKET=? AND TZ_USER=? LIMIT 0,1",
					new Object[]{strApplicationId,strTicket,strUserName},"String");
			if("Y".equals(strTicketRepeat)){
				request.setAttribute(strSsoAuthCode,TZ_TICKET_REPEAT);
				return strErrorForward;
			}
			
			//加密验证
			String strBeforeEncryption = strApplicationId+strAppSecret+strUserName+strTicket;
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] encryptionBytes = strBeforeEncryption.getBytes();
			byte[] digestBytes = md.digest(encryptionBytes);
			String strEncryption = TzSingleSignOnAuthServiceImpl.bytes2Hex(digestBytes);
			if(!strEncryption.equals(strAccessToken)){
				request.setAttribute(strSsoAuthCode,TZ_AUTH_FAILURE);
				return strErrorForward;
			}
			
			//开始登录操作
			boolean loginReturn = ssoImpl.login(request, response, strOrgId, strUserAccount);
			if(loginReturn==false){
				request.setAttribute(strSsoAuthCode,TZ_LOGIN_FAILURE);
				return strErrorForward;
			}else{
				//单点登录认证记录
				sqlQuery.update("INSERT INTO TZ_SSO_AUTH_TBL(TZ_APP_ID,TZ_TICKET,TZ_USER,TZ_DATETIME) VALUES(?,?,?,?)",
						new Object[]{strApplicationId,strTicket,strUserName,new Date()});
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute(strSsoAuthCode,TZ_UNSPECIFIED_ERROR);
			return strErrorForward;
		}
		
		return "redirect:/index";
	}


	@RequestMapping(value = "result", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String authResult(HttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("success", "false");
		mapRet.put("code", request.getAttribute(strSsoAuthCode));
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		return jacksonUtil.Map2json(mapRet);
	}

}
