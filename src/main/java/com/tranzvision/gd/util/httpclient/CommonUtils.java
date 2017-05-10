package com.tranzvision.gd.util.httpclient;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.StringUtils;

/**
 * 常用工具类
 * 
 * @author WRL
 *
 */
public class CommonUtils {
	
	/**
	 * 判断客户端请求是否来自移动设备
	 * 
	 * @param request
	 * @return
	 */
	public static boolean isMobile(HttpServletRequest request) {
		String isM = request.getParameter("isMobile");
		if(StringUtils.equals("Y", isM)){
			return true;
		}
		String sUserAgent = request.getHeader("User-Agent");
		
		//是否移动设备访问
		boolean isMobile = false;
		
		String[] mobileAgents = {"Windows CE","iPod","Symbian","iPhone","BlackBerry","Android","Windows Phone"};
		if(sUserAgent.indexOf("Android") > -1 && (sUserAgent.indexOf("ERD79) > -1 || sUserAgent.indexOf('MZ60") > -1 || sUserAgent.indexOf("GT-P7") > -1 || sUserAgent.indexOf("SCH-P7") > -1)){
		}else{
			for( int i = 0; i < mobileAgents.length; i++){
				if(sUserAgent.indexOf(mobileAgents[i])>-1){
					isMobile = true;
					break;
				}
			}
		}
		return isMobile;
	}
	
	
	/**
	 * 判断客户端请求是否是微信内置浏览器
	 * @param request
	 * @return
	 */
	public static boolean isWeChartBrowser(HttpServletRequest request){
		boolean isWeChart = false;
		
		String sUserAgent = request.getHeader("User-Agent").toLowerCase();
		if(sUserAgent.indexOf("micromessenger") > -1){
			isWeChart = true;
		}
		
		return isWeChart;
	}
}
