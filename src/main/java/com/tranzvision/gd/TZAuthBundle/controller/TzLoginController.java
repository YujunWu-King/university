package com.tranzvision.gd.TZAuthBundle.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;

/**
 * @author SHIHUA
 * @since 2015-11-03
 */
@Controller
@RequestMapping("/login")
public class TzLoginController {

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private JacksonUtil jacksonUtil;

	@RequestMapping(value = { "/", "" })
	public String userLogin(HttpServletRequest request, HttpServletResponse response) {

		return "login/managerLogin";
	}

	@RequestMapping(value = "getorgdata", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getOrgData() {

		return "";
	}

	@RequestMapping(value = "dologin", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String doLogin(HttpServletRequest request, HttpServletResponse response) {

		jacksonUtil.json2Map(request.getParameter("tzParams"));
		Map<String, Object> formData = jacksonUtil.getMap("comParams");
		
		String orgid = (String)formData.get("orgId");
		String userName = (String)formData.get("userName");
		String userPwd = (String)formData.get("password");
		String code = (String)formData.get("yzm");

		tzLoginServiceImpl.doLogin(request, response, orgid, userName, userPwd, code);
		String loginStatus = tzLoginServiceImpl.getLoginStatus();
		String errorMsg = tzLoginServiceImpl.getErrorMsg();

		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("success", loginStatus);
		jsonMap.put("error", errorMsg);
		jsonMap.put("indexUrl", "/index");

		// {"success":"%bind(:1)","error":"%bind(:2)","indexUrl":"%bind(:3)"}

		return jacksonUtil.Map2json(jsonMap);
	}

}
