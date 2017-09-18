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
import com.tranzvision.gd.util.httpclient.CommonUtils;

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
		
		if(CommonUtils.isMobile(request)){
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

}
