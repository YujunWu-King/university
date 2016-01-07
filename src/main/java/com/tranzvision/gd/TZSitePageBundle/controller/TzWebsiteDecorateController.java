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

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZSitePageBundle.service.impl.TzWebsiteServiceImpl;

/**
 * 站点装修链接
 * 
 * @author SHIHUA
 * @since 2016-01-06
 */
@Controller
@RequestMapping(value = { "/decorate" })
public class TzWebsiteDecorateController {

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private TzWebsiteServiceImpl tzWebsiteServiceImpl;

	@RequestMapping(value = { "/index/{orgid}/{siteid}" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String decorateIndex(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "orgid") String orgid, @PathVariable(value = "siteid") String siteid) {

		if (!tzLoginServiceImpl.checkManagerLogin(request, response)) {
			orgid = orgid.toLowerCase();
			String redirect = "redirect:" + "/login/" + orgid + "/" + siteid;
			return redirect;
		}

		String strRet = tzWebsiteServiceImpl.getIndexSaveCode(request, orgid, siteid);

		return strRet;
	}

	@RequestMapping(value = { "/login/{orgid}/{siteid}" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String decorateLogin(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "orgid") String orgid, @PathVariable(value = "siteid") String siteid) {

		if (!tzLoginServiceImpl.checkManagerLogin(request, response)) {
			orgid = orgid.toLowerCase();
			String redirect = "redirect:" + "/login/" + orgid + "/" + siteid;
			return redirect;
		}

		String strRet = tzWebsiteServiceImpl.getLoginSaveCode(request, orgid, siteid);

		return strRet;
	}

}
