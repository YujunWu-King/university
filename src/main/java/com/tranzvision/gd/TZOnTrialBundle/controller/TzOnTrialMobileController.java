package com.tranzvision.gd.TZOnTrialBundle.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tranzvision.gd.TZOnTrialBundle.service.impl.TzOnTrialMobileServiceImpl;
//import com.tranzvision.gd.TZOnTrialBundle.service.impl.TzOnTrialServiceImpl;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.sql.TZGDObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/")
public class TzOnTrialMobileController {
	//@Autowired
	//private TzOnTrialServiceImpl tzOnTrialServiceImpl;
	
	@Autowired
	private TZGDObject tzGDObject;
	
	@Autowired
	private TzOnTrialMobileServiceImpl tzOnTrialMobileServiceImpl;
	
	//申请试用表单
	/*
	@RequestMapping(value = "m/tranzvision", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String trial(HttpServletRequest request, HttpServletResponse response) {
		String htmlStr = tzOnTrialServiceImpl.tzMApply();
		
		return htmlStr;
	}
	*/
	
	//提交
	@RequestMapping(value = "m/trialSubmit", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String trialSubmit(HttpServletRequest request, HttpServletResponse response) {
		return tzOnTrialMobileServiceImpl.applyOnTrialMobileInfo();
	}
	
	//提交成功跳转页面
	@RequestMapping(value = "m/trialSuccess", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String trialSuccess(HttpServletRequest request, HttpServletResponse response) {
		String contextPath = request.getContextPath();
		String loginHtml = "";
		try {
			loginHtml = tzGDObject.getHTMLText("HTML.TZOnTrialBundle.TZ_TRIAL_M_SUCCESS_HTML", true,contextPath,"试用申请已提交");
		} catch (TzSystemException e) {
			e.printStackTrace();
			loginHtml = "申请试用访问失败，请于管理员联系";
		}
		return loginHtml;
	}
}
