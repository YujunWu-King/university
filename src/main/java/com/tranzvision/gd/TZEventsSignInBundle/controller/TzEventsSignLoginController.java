package com.tranzvision.gd.TZEventsSignInBundle.controller;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZWeChatBundle.service.impl.TzWeChartJSSDKSign;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.security.TzFilterIllegalCharacter;
import com.tranzvision.gd.util.sql.TZGDObject;

@Controller
@RequestMapping(value = { "/signIn" })
public class TzEventsSignLoginController {
	
	@Autowired
	private TZGDObject tzGdObject;
	
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private TzFilterIllegalCharacter tzFilterIllegalCharacter;
	
	@Autowired
	private GetHardCodePoint getHardCodePoint;
	
	@Autowired
	private TzWeChartJSSDKSign tzWeChartJSSDKSign;
	
	
	/**
	 * 活动签到管理
	 * @param request
	 * @param response
	 * @param orgId
	 * @return
	 */
	@RequestMapping(value = { "/{orgId}" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String EventsSignInManagement(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "orgId") String orgId) {
		String eventSignInHtml = "";
		//机构ID
		orgId = tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(orgId).toUpperCase();
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			String contextPath = request.getContextPath();
			// 通用链接;
			String ZSGL_URL = contextPath + "/dispatcher";
			
			String currOgrId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			if(!"".equals(orgId) 
					&& !"".equals(currOgrId) 
					&& !currOgrId.equals(orgId)){
				//当前登录机构不是指定机构，退出登录
				tzLoginServiceImpl.doLogout(request, response);
			}
			
			String loginOprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			//System.out.println("------------->"+loginOprid);
			if("".equals(loginOprid)){
				//没有登录，进入登录页面
				eventSignInHtml = tzGdObject.getHTMLText("HTML.TZEventsSignInBundle.TZ_HDQD_LOGIN_HTML",
						contextPath,orgId);
			}else{
				String nonceStr = "";
				String timestamp = "0";
				String signature = "";
				
				String url;
				if("".equals(request.getQueryString()) || request.getQueryString() == null){
					url = request.getRequestURL().toString();
				}else{
					url = request.getRequestURL().toString()+"?"+request.getQueryString();
				}
			
				String corpid = "";
				String corpsecret = "";
				try {
					corpid = getHardCodePoint.getHardCodePointVal("TZ_WX_CORPID");
					corpsecret = getHardCodePoint.getHardCodePointVal("TZ_WX_SECRET");

					Map<String,String> signMap = tzWeChartJSSDKSign.sign(corpid, corpsecret, "QY", url);
					if(signMap != null){
						nonceStr = signMap.get("nonceStr");
						timestamp = signMap.get("timestamp");
						signature = signMap.get("signature");
					}
				} catch (NullPointerException nullE) {
					nullE.printStackTrace();
				}

				//构造签到url参数
				Map<String,Object> tzQdParamsMap = new HashMap<String,Object>();
				tzQdParamsMap.put("ComID", "TZ_HD_SIGN_COM");
				tzQdParamsMap.put("PageID", "TZ_HD_SIGNIN_STD");
				tzQdParamsMap.put("OperateType", "EJSON");
				tzQdParamsMap.put("comParams", new HashMap<String,String>());
				
				//微信扫一扫签到url
				String str_wxsys_qd_url = ZSGL_URL + "?tzParams=" + URLEncoder.encode(jacksonUtil.Map2json(tzQdParamsMap),"UTF-8");
				//签到人详情确认页URL
				String str_qdr_xq_url  = ZSGL_URL + "?classid=signConfirm";
				
				//签到码签到url
				String str_qdm_qd_url = ZSGL_URL + "?classid=signCode";
				//活动管理url
				String str_hdgl_url = ZSGL_URL + "?classid=eventsSignList";
				//手机签到URL
				String str_moblie_url=ZSGL_URL+"?classid=eventsSignList&mobileSign=true";
				//ajax请求url
				Map<String,Object> ajaxParamsMap = new HashMap<String,Object>();
				ajaxParamsMap.put("ComID", "TZ_HD_SIGN_COM");
				ajaxParamsMap.put("PageID", "TZ_HD_SIGN_STD");
				ajaxParamsMap.put("OperateType", "EJSON");
				ajaxParamsMap.put("comParams", new HashMap<String,String>());
				String str_ajax_url  =  ZSGL_URL + "?tzParams=" + URLEncoder.encode(jacksonUtil.Map2json(ajaxParamsMap),"UTF-8");
			
				eventSignInHtml = tzGdObject.getHTMLText("HTML.TZEventsSignInBundle.TZ_PHONE_ACTSIGN_HTML",
						contextPath,str_wxsys_qd_url,str_qdr_xq_url,str_qdm_qd_url,str_hdgl_url,corpid,timestamp,nonceStr,signature,str_ajax_url,str_moblie_url);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return eventSignInHtml;
	}

	
	
	/**
	 * 活动签到登录
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "dologin", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String doLogin(HttpServletRequest request, HttpServletResponse response) {
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		String orgid = request.getParameter("orgId");
		String userName = request.getParameter("userName");
		String userPwd = request.getParameter("password");
		String code = request.getParameter("yzm");
		
		ArrayList<String> aryErrorMsg = new ArrayList<String>();

		tzLoginServiceImpl.doLogin(request, response, orgid, userName, userPwd, code, aryErrorMsg);
		String loginStatus = aryErrorMsg.get(0);
		String errorMsg = aryErrorMsg.get(1);

		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("success", loginStatus);
		jsonMap.put("error", errorMsg);

		return jacksonUtil.Map2json(jsonMap);
	}
}
