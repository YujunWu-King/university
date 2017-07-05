/**
 * 
 */
package com.tranzvision.gd.TZSitePageBundle.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzWebsiteLoginServiceImpl;
import com.tranzvision.gd.TZSitePageBundle.service.impl.TzWebsiteServiceImpl;
import com.tranzvision.gd.TZMobileWebsiteIndexBundle.service.impl.MobileWebsiteIndexServiceImpl;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 网站首页展示
 * 
 * @author SHIHUA
 * @since 2016-01-06
 */
@Controller
@RequestMapping(value = { "/site/index" })
public class TzWebsiteIndexController {

	@Autowired
	private TzWebsiteLoginServiceImpl tzWebsiteLoginServiceImpl;

	@Autowired
	private TzWebsiteServiceImpl tzWebsiteServiceImpl;

	@Autowired
	private TZGDObject tzGDObject;

	@Autowired
	private MobileWebsiteIndexServiceImpl mobileWebsiteIndexServiceImpl;
	
	@RequestMapping(value = { "/{orgid}/{siteid}" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String websiteIndex(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "orgid") String orgid, @PathVariable(value = "siteid") String siteid) {

		orgid = orgid.toLowerCase();
		String strRet = "";
		
		if(isMoblie(request)){
			String strParams = "{\"siteId\":\""+siteid+"\"}";
			strRet = mobileWebsiteIndexServiceImpl.tzGetHtmlContent(strParams);
		}else{
			if(tzWebsiteLoginServiceImpl.checkUserLogin(request, response) == false)
			{
				tzWebsiteLoginServiceImpl.autoLoginByCookie(request, response);
			}

			if(!tzWebsiteLoginServiceImpl.checkUserLogin(request, response)) {
				String redirectUrl = request.getContextPath() + "/user/login/" + orgid + "/" + siteid;
				try {
					strRet = tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzDoLoginRedirectScript", redirectUrl);
				} catch (TzSystemException e) {
					e.printStackTrace();
				}
				return strRet;
			}

			strRet = tzWebsiteServiceImpl.getIndexPublishCode(request, orgid, siteid);

			if ("errororg".equals(strRet)) {
				String redirectUrl = request.getContextPath() + "/user/login/" + orgid + "/" + siteid;
				try {
					strRet = tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzDoLoginRedirectScript", redirectUrl);
				} catch (TzSystemException e) {
					e.printStackTrace();
				}
				return strRet;
			}
		}

		return strRet;
	}

	public boolean isMoblie(HttpServletRequest request) {
		boolean isMoblie = false;
		String[] mobileAgents = { "iphone", "android", "phone", "mobile", "wap", "netfront", "java", "opera mobi",
				"opera mini", "ucweb", "windows ce", "symbian", "series", "webos", "sony", "blackberry", "dopod",
				"nokia", "samsung", "palmsource", "xda", "pieplus", "meizu", "midp", "cldc", "motorola", "foma",
				"docomo", "up.browser", "up.link", "blazer", "helio", "hosin", "huawei", "novarra", "coolpad", "webos",
				"techfaith", "palmsource", "alcatel", "amoi", "ktouch", "nexian", "ericsson", "philips", "sagem",
				"wellcom", "bunjalloo", "maui", "smartphone", "iemobile", "spice", "bird", "zte-", "longcos",
				"pantech", "gionee", "portalmmm", "jig browser", "hiptop", "benq", "haier", "^lct", "320x320",
				"240x320", "176x220", "w3c ", "acs-", "alav", "alca", "amoi", "audi", "avan", "benq", "bird", "blac",
				"blaz", "brew", "cell", "cldc", "cmd-", "dang", "doco", "eric", "hipt", "inno", "ipaq", "java", "jigs",
				"kddi", "keji", "leno", "lg-c", "lg-d", "lg-g", "lge-", "maui", "maxo", "midp", "mits", "mmef", "mobi",
				"mot-", "moto", "mwbp", "nec-", "newt", "noki", "oper", "palm", "pana", "pant", "phil", "play", "port",
				"prox", "qwap", "sage", "sams", "sany", "sch-", "sec-", "send", "seri", "sgh-", "shar", "sie-", "siem",
				"smal", "smar", "sony", "sph-", "symb", "t-mo", "teli", "tim-", "tosh", "tsm-", "upg1", "upsi", "vk-v",
				"voda", "wap-", "wapa", "wapi", "wapp", "wapr", "webc", "winw", "winw", "xda", "xda-",
				"Googlebot-Mobile" };
		if (request.getHeader("User-Agent") != null) {
			for (String mobileAgent : mobileAgents) {
				if (request.getHeader("User-Agent").toLowerCase().indexOf(mobileAgent) >= 0) {
					isMoblie = true;
					break;
				}
			}
		}
		return isMoblie;
	}
}
