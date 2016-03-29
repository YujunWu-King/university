package com.tranzvision.gd.TZOnTrialBundle.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.tranzvision.gd.TZOnTrialBundle.service.impl.TzOnTrialServiceImpl;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.sql.TZGDObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/")
public class TzOnTrialController {
	@Autowired
	private TzOnTrialServiceImpl tzOnTrialServiceImpl;
	
	@Autowired
	private TZGDObject tzGDObject;
	
	@RequestMapping(value = "tranzvision", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String trial(HttpServletRequest request, HttpServletResponse response) {
		String htmlStr = tzOnTrialServiceImpl.tzApply();
		
		return htmlStr;
	}
	
	@RequestMapping(value = "trialSuccess", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String trialSuccess(HttpServletRequest request, HttpServletResponse response) {
		String contextPath = request.getContextPath();
		String loginHtml = "";
		try {
			loginHtml = tzGDObject.getHTMLText("HTML.TZOnTrialBundle.TZ_TRIAL_SUCCESS_HTML", true,contextPath,"试用申请成功，请等待管理员的审核，谢谢！");
		} catch (TzSystemException e) {
			e.printStackTrace();
			loginHtml = "申请试用访问失败，请于管理员联系";
		}
		return loginHtml;
	}
}