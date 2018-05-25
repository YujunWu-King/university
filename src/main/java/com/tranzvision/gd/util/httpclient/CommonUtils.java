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
		if (StringUtils.equals("Y", isM)) {
			return true;
		}
		String sUserAgent = request.getHeader("User-Agent");

		// 是否移动设备访问
		boolean isMobile = false;

		System.out.println("sUserAgent:" + sUserAgent);

		// sUserAgent:Mozilla/5.0 (Linux; U; Android 4.4.4; zh-cn; MI PAD
		// Build/KTU84P) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0
		// Chrome/53.0.2785.146 Mobile Safari/537.36 XiaoMi/MiuiBrowser/9.3.2
		
		// sUserAgent:Mozilla/5.0 (Linux; Android 8.0.0; MIX 2
		// Build/OPR1.170623.027; wv) AppleWebKit/537.36 (KHTML, like Gecko)
		// Version/4.0 Chrome/62.0.3202.84 Mobile Safari/537.36
		// MicroMessenger/6.6.6.1300(0x26060637) NetType/WIFI Language/zh_CN
		String[] mobileAgents = { "Windows CE", "iPod", "Symbian", "iPhone", "BlackBerry", "Android", "Windows Phone" };
		if (sUserAgent.indexOf("Android") > -1 && (sUserAgent.indexOf("ERD79) > -1 || sUserAgent.indexOf('MZ60") > -1
				|| sUserAgent.indexOf("GT-P7") > -1 || sUserAgent.indexOf("SCH-P7") > -1)) {
		} else {
			for (int i = 0; i < mobileAgents.length; i++) {
				if (sUserAgent.indexOf("XiaoMi") > -1) {
					break;
				}

				if (sUserAgent.indexOf(mobileAgents[i]) > -1) {
					isMobile = true;
					break;
				}
			}
		}
		return isMobile;
	}

	/**
	 * 判断客户端请求是否是微信内置浏览器
	 * 
	 * @param request
	 * @return
	 */
	public static boolean isWeChartBrowser(HttpServletRequest request) {
		boolean isWeChart = false;

		String sUserAgent = request.getHeader("User-Agent").toLowerCase();
		if (sUserAgent.indexOf("micromessenger") > -1) {
			isWeChart = true;
		}

		return isWeChart;
	}
}
