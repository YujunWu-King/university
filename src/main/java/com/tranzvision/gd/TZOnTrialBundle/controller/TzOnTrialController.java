package com.tranzvision.gd.TZOnTrialBundle.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.tranzvision.gd.TZOnTrialBundle.service.impl.TzOnTrialServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/")
public class TzOnTrialController {
	@Autowired
	private TzOnTrialServiceImpl tzOnTrialServiceImpl;
	
	@RequestMapping(value = "tranzvision", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String trial(HttpServletRequest request, HttpServletResponse response) {
		String htmlStr = tzOnTrialServiceImpl.tzApply();
		
		return htmlStr;
	}
}
