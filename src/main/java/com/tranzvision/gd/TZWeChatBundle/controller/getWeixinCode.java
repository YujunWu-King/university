package com.tranzvision.gd.TZWeChatBundle.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tranzvision.gd.TZWeChatBundle.service.impl.TzWeChartJSSDKSign;


/**
 * 获取微信公众号授权code，统一用这个中转解决一个公众号只能配置一个授权域名的问题
 * 1、访问链接：/weChart/getCode?appid=APPID&redirect_uri=REDIRECT_URI
 * 2、链接说明： APPID - 微信公众号的appid，
 * 			REDIRECT_URI - 要访问的URL，需要进行UrlEncode编码，否则REDIRECT_URI中的参数会被当成中转链接的参数
 * @author zhanglang
 * 2017-05-11
 */
@Controller
@RequestMapping(value = "/weChart")
public class getWeixinCode {
	@Autowired
	private TzWeChartJSSDKSign tzWeChartJSSDKSign;

	
	@RequestMapping(value = "getCode")
	public String getWeinxinCode(HttpServletRequest request, HttpServletResponse response) {
		
		String redirectUrl = "";
		
		String code = request.getParameter("code");
		String appid = request.getParameter("appid");
		String state = request.getParameter("state");
		String redirect_uri = request.getParameter("redirect_uri");
		
	
		if("".equals(code) || code == null){
			//第一步，没有拿到code，跳转至微信授权页面获取code
			String url = request.getRequestURL().toString();
			String queryString = request.getQueryString();
		    if (queryString != null) {
		    	url += "?"+ queryString;
		    }
		    
		    redirectUrl = tzWeChartJSSDKSign.getOAuthCodeUrl(appid, url);
		}else{
			if(redirect_uri.indexOf("?") > -1){
				redirectUrl = redirect_uri + "&code=" + code + "&state=" + state;
			}else{
				redirectUrl = redirect_uri + "?code=" + code + "&state=" + state;
			}
		}
		
		String redirect = "redirect:" + redirectUrl;
		
		return redirect;
	}
}
