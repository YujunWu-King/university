package com.tranzvision.gd.TZAuthBundle.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author SHIHUA
 * @since 2015-11-03
 */
@Controller
@RequestMapping("/login")
public class TzLoginController {

	
	@RequestMapping(value={"/",""})
	public String userLogin(HttpServletRequest request,HttpServletResponse response) {
		
		return "login/managerLogin";
	}
	
	

}
