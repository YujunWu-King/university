package com.tranzvision.gd.TZWeChatAppBundle.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZWeChatBundle.dao.PsTzWxGzhcsTMapper;
import com.tranzvision.gd.TZWeChatBundle.model.PsTzWxGzhcsTKey;
import com.tranzvision.gd.TZWeChatAppBundle.service.impl.TzWeChartSign;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

@Controller
@RequestMapping(value = { "/ibeaconSignIn" })
public class TZWeChatSignInController {
	@Autowired
	private SqlQuery jdbcTemplate;
	
	@Autowired
	private TZGDObject tzGdObject;
	
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
		
	@Autowired
	private TzWeChartSign tzWeChartJSSDKSign;
	
	@Autowired
	private PsTzWxGzhcsTMapper psTzWxGzhcsTMapper;
	
	private static String APP_ID="wx5bdecf7575803a8d";
	
	//private static String APP_SECRET="97544f0089684e88e0faf4bcaaa1f57a";
	private static String APP_SECRET="d7c378300fcf1afd9d1525c4e2689c1e";
	
	/**
	 * redirect
	 * 
	 */
	 @RequestMapping(value = "redirect",  produces = "text/html;charset=UTF-8")
     public String weixinRedirect(HttpServletRequest request, HttpServletResponse response) {
		String url;
		if("".equals(request.getQueryString()) || request.getQueryString() == null){
			url = request.getRequestURL().toString();
		}else{
			url = request.getRequestURL().toString()+"?"+request.getQueryString();
		}
		String getServletPath = request.getServletPath();
		url = url.replaceAll(getServletPath,"/ibeaconSignIn/ibeacon");
		int state =  new Random().nextInt(10000);//随机state 避免相同code
		System.out.println(url);
        return "redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid="+APP_ID+"&redirect_uri="+url+"&response_type=code&scope=snsapi_userinfo&state="+state+"&connect_redirect=1#wechat_redirect";
     }
	
	
	/**
	 * 活动签到管理
	 * @param request
	 * @param response
	 * @param orgId
	 * @return
	 */
	@RequestMapping(value = { "ibeacon" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String EventsSignInManagement(HttpServletRequest request, HttpServletResponse response
			) {
		String eventSignInHtml = "";
		String code = request.getParameter("code");
		String url;
		if("".equals(request.getQueryString()) || request.getQueryString() == null){
			url = request.getRequestURL().toString();
		}else{
			url = request.getRequestURL().toString()+"?"+request.getQueryString();
		}
		if(code==null||"".equals(code)){//未获取到code
			String getServletPath = request.getServletPath();
			url = url.replaceAll(getServletPath,"/ibeaconSignIn/redirect");
			return "redirect:"+url;
		}else{
			try {
				String contextPath = request.getContextPath();
				// 通用链接;
				String nonceStr = "";
				String timestamp = "0";
				String signature = "";
				
				String corpid = "";
				String corpsecret = "";
				String openId="";
				String nickName="";
				try {
					corpid = APP_ID;
					corpsecret = APP_SECRET;
					Map<String,String> signMap = tzWeChartJSSDKSign.sign(corpid, corpsecret, "GZ", url);
					//获取OpenId;
					System.out.println("appid==="+corpid);
					System.out.println("secret==="+corpsecret);
					System.out.println("code==="+code);
					Map<String,Object> accessMap = tzWeChartJSSDKSign.getOauthAccessOpenId(corpid, corpsecret, code);
					System.out.println("accessMap==="+accessMap);
					openId=accessMap.get("openid").toString();
					System.out.println("openid==="+openId);
					//获取昵称
					nickName = tzWeChartJSSDKSign.getOauthAccessInfo(accessMap).get("nickname").toString();
					if(signMap != null){
						nonceStr = signMap.get("nonceStr");
						timestamp = signMap.get("timestamp");
						signature = signMap.get("signature");
					}
				} catch (Exception nullE) {
					nullE.printStackTrace();
				}
				//获取token
				PsTzWxGzhcsTKey key = new PsTzWxGzhcsTKey();
				key.setTzWxAppid(corpid);
				key.setTzAppsecret(corpsecret);
				String accessToken = psTzWxGzhcsTMapper.selectByPrimaryKey(key).getTzAccessToken();
				eventSignInHtml = tzGdObject.getHTMLText("HTML.TZWeChatAppBundle.TZ_PHONE_SIGN_HTML",true,
						contextPath,corpid,timestamp,nonceStr,signature,accessToken,openId,nickName);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return eventSignInHtml;
		}
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
	
	

	/**
	 * 签到信息展示
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "showInfo", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String showInfo(HttpServletRequest request, HttpServletResponse response) {
		String data = request.getParameter("data");
		String openId = request.getParameter("openId");
		String nickName = request.getParameter("nickName");
		System.out.println("==========");
		System.out.println("openId ="+openId);
		System.out.println("nickName ="+nickName);
		JacksonUtil jacksonUtil = new JacksonUtil();
		String msg = "";
		jacksonUtil.json2Map(data);
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try{		
			ArrayList<LinkedHashMap> list=(ArrayList<LinkedHashMap>) jacksonUtil.getList("A");
			String ibeaconName ="";
			String accuracy = "";//设备距离
			for(LinkedHashMap map:list){
				//通过设备major，minor和UUID查询设备信息
				String major = map.get("major").toString();
				String minor =map.get("minor").toString();
				String uuid = map.get("uuid").toString();
				String acc=map.get("accuracy").toString();
				if(accuracy==""){
					accuracy = acc.substring(0, acc.indexOf(".")+3);//设备距离,精确到厘米
				}else{
					accuracy =accuracy+" , "+acc.substring(0, acc.indexOf(".")+3);//设备距离,精确到厘米
				}
				String sql1 = "SELECT IBEACON_NAME  FROM IBEACON_INFO WHERE MAJOR= ? AND MINOR=? AND UUID=?";
				if(ibeaconName==""){
					ibeaconName =jdbcTemplate.queryForObject(sql1, new Object[] { major, minor, uuid },"String");
				}else{
					ibeaconName =ibeaconName+" , "+jdbcTemplate.queryForObject(sql1, new Object[] { major, minor, uuid },"String");
				}
				System.out.println("===========");
				System.out.println(ibeaconName + " " + map.values());
			}
			if(!"".equals(ibeaconName)){
				//把签到信息写入签到表
				String sql2 = "INSERT INTO SIGN_INFO (OPENID,NICK_NAME,IBEACON_NAME,SIGN_TIME,SIGN_ACCURACY) VALUES (?,?,?,?,?)";
				jdbcTemplate.update(sql2,  new Object[]{openId,nickName,ibeaconName,new Date(),accuracy});
				msg = ibeaconName;//签到设备信息放入msg
			}
			jsonMap.put("success", "success");
		}catch(Exception e){
			jsonMap.put("error", "error");
		}

		if("".equals(msg)){
			msg="签到失败！未进入签到区域";
		}else{
			msg = msg + " 签到成功！";
		}
		jsonMap.put("msg", msg);
		System.out.println(msg);
		return jacksonUtil.Map2json(jsonMap);
	}
	
}
