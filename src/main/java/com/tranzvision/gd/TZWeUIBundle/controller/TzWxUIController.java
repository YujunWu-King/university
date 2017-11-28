package com.tranzvision.gd.TZWeUIBundle.controller;

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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZWeChatAppBundle.service.impl.TzWeChartSign;
import com.tranzvision.gd.TZWeChatBundle.dao.PsTzWxGzhcsTMapper;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

@Controller
@RequestMapping(value = { "/weUI" })
public class TzWxUIController {

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

	@RequestMapping(value = "tst", produces = "text/html;charset=UTF-8")
	public String weixinRedirect(HttpServletRequest request, HttpServletResponse response) {
		return "redirect:https://www.baidu.com";
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param orgId
	 * @return
	 */
	@RequestMapping(value = { "example" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String EventsSignInManagement(HttpServletRequest request, HttpServletResponse response) {
		String eventHtml = "";
		try {
			
			String contextPath = request.getContextPath();
			eventHtml = tzGdObject.getHTMLText("HTML.TZWeUIBundle.TZ_WEUI_PHONE_HTML", true, contextPath);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return eventHtml;

	}
}
