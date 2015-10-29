package com.tranzvision.gd.ztest.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.ModelMap;

import com.tranzvision.gd.ztest.model.Admin;
import com.tranzvision.gd.ztest.service.AdminService;
 
@Controller
@RequestMapping("/admin")
public class TestController {
	
	@Autowired
	private AdminService adminService;
	
	String message = "Admin!";
	
	private static final String TPLPrefix = "ztest/";
 
	@RequestMapping("index")
    public String index(ModelMap model){
    	model.addAttribute("nickname", "管理员");
    	return "ztest/hello";
    }
	
	@RequestMapping("hello")
	public ModelAndView showMessage(
			@RequestParam(value = "name", required = false, defaultValue = "World") String name) {
		System.out.println("in controller");
 
		ModelAndView mv = new ModelAndView(TPLPrefix + "hello");
		mv.addObject("message", message);
		mv.addObject("nickname", name + "!");
		return mv;
	}
	
	@RequestMapping("add")
    public String addUser(ModelMap model){
		String name = "管理员";
		Admin admin = new Admin();
		admin.setAdminName(name);
		admin.setAdminPwd("123456");
		admin.setAdminRealname("测试名字");
		int rst = adminService.insertAdmin(admin);
		
		model.addAttribute("rst", rst);
		model.addAttribute("name", name);
		
    	return TPLPrefix + "add";
    }
	
	@RequestMapping("get")
    public String getUser(ModelMap model){
		
		int id = 1;
		Admin admin;
		try {
			admin = adminService.getOneAdmin(id);
			model.addAttribute("id", admin.getAdminid());
			model.addAttribute("name", admin.getAdminName());
			model.addAttribute("state", admin.getAdminRealname());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
    	return TPLPrefix + "get";
    }
	
	///*
	@RequestMapping("getalladmins")
    public ModelAndView getAllUser(){
		
		List UsersList = adminService.getAllAdmins();
		System.out.println(UsersList);
		
		ModelAndView model = new ModelAndView(TPLPrefix + "getalladmins");
		model.addObject("UsersList", UsersList);
		
    	return model;
    }
    //*/

}
