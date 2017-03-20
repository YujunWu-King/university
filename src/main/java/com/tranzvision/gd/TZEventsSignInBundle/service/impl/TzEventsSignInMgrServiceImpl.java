package com.tranzvision.gd.TZEventsSignInBundle.service.impl;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZWeChatBundle.service.impl.TzWeChartJSSDKSign;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 活动签到管理
 * @author zhanglang
 *
 */
@Service("com.tranzvision.gd.TZEventsSignInBundle.service.impl.TzEventsSignInMgrServiceImpl")
public class TzEventsSignInMgrServiceImpl extends FrameworkImpl{
	
	@Autowired
	private TZGDObject tzGdObject;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private HttpServletResponse response;
	
	@Autowired
	private GetHardCodePoint getHardCodePoint;
	
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private TzWeChartJSSDKSign tzWeChartJSSDKSign;

	
	/**
	 * 该方法弃用，改用controller
	 */
	@Override
	public String tzGetHtmlContent(String strParams) {
		String eventSignInHtml = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			String orgId = request.getParameter("orgId");
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
				String timestamp = "";
				String signature = "";
				
				String url;
				if("".equals(request.getQueryString())){
					url = request.getRequestURL().toString();
				}else{
					url = request.getRequestURL().toString()+"?"+request.getQueryString();
				}
			
				String corpid = getHardCodePoint.getHardCodePointVal("TZ_WX_CORPID");
				String corpsecret = getHardCodePoint.getHardCodePointVal("TZ_WX_SECRET");
				
				
				Map<String,String> signMap = tzWeChartJSSDKSign.sign(corpid, corpsecret, "QY", url);
				if(signMap != null){
					nonceStr = signMap.get("nonceStr");
					timestamp = signMap.get("timestamp");
					signature = signMap.get("signature");
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
				
				//ajax请求url
				Map<String,Object> ajaxParamsMap = new HashMap<String,Object>();
				ajaxParamsMap.put("ComID", "TZ_HD_SIGN_COM");
				ajaxParamsMap.put("PageID", "TZ_HD_SIGN_STD");
				ajaxParamsMap.put("OperateType", "EJSON");
				ajaxParamsMap.put("comParams", new HashMap<String,String>());
				String str_ajax_url  =  ZSGL_URL + "?tzParams=" + URLEncoder.encode(jacksonUtil.Map2json(ajaxParamsMap),"UTF-8");
			
				eventSignInHtml = tzGdObject.getHTMLText("HTML.TZEventsSignInBundle.TZ_PHONE_ACTSIGN_HTML",
						contextPath,str_wxsys_qd_url,str_qdr_xq_url,str_qdm_qd_url,str_hdgl_url,corpid,timestamp,nonceStr,signature,str_ajax_url);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return eventSignInHtml;
	}
	
	
	
	/**
	 * 用于ajax请求，防止登录过期
	 */
	@Override
	public String tzGetJsonData(String strParams) {
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("refresh", "true");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		return jacksonUtil.Map2json(rtnMap);
	}
}
