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

	@RequestMapping(value = { "/{orgid}/{siteid}" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String websiteIndex(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "orgid") String orgid, @PathVariable(value = "siteid") String siteid) {

		if (!tzWebsiteLoginServiceImpl.checkUserLogin(request, response)) {
			orgid = orgid.toLowerCase();
			String redirect = "redirect:" + "/user/login/" + orgid + "/" + siteid;
			return redirect;
		}

		String strRet = tzWebsiteServiceImpl.getIndexPublishCode(request, orgid, siteid);

		return strRet;
	}

}
