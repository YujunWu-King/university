package com.tranzvision.gd.TZAuthBundle.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.ModelMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.ztest.service.AdminService;

/**
 * @author SHIHUA
 * @since 2015-11-03
 */
@Controller
@RequestMapping("/login")
public class TzLoginController {

	
	@RequestMapping(value={"/",""})
	public String userLogin(HttpServletRequest request, HttpServletResponse response) {

		
		
		return "login/managerLogin.jsp";
	}
	
	

}
