package com.tranzvision.gd.ztest.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.ztest.service.AdminService;

import org.springframework.ui.ModelMap;

@Controller
@RequestMapping("/json")
public class JsonController {

	@Autowired
	private AdminService adminService;

	@SuppressWarnings("unchecked")
	@RequestMapping("parseJSON")
	public String parseJSON(@RequestParam(value = "name", required = false, defaultValue = "") String json) {

		//?name={"userImage":"Rm9vYmFyIQ==","num":1,"gender":"MALE","name":{"last":"Sixpack","first":"Joe"},"verified":false,"alluser":[{"id":1,"state":2,"nickname":"石华"},{"id":2,"state":2,"nickname":"石华"},{"id":3,"state":2,"nickname":"石华1"},{"id":4,"state":2,"nickname":"石华2"},{"id":5,"state":2,"nickname":"石华2"},{"id":6,"state":2,"nickname":"石华2"},{"id":7,"state":2,"nickname":"石华2"},{"id":8,"state":2,"nickname":"鐭冲崕"},{"id":9,"state":2,"nickname":"鐭冲崕"},{"id":10,"state":2,"nickname":"鐭冲崕"},{"id":11,"state":2,"nickname":"鐭冲崕"},{"id":12,"state":2,"nickname":"李刚测试"},{"id":13,"state":2,"nickname":"李刚测试"},{"id":14,"state":2,"nickname":"鐭冲崕"},{"id":15,"state":2,"nickname":"李刚测试"}]}
		
		if ("".equals(json)) {
			// json =
			// "{\"id\":\"1\",\"name\":\"测试\",\"val\":[{\"car\":\"vw\"},{\"car\":\"pc\"}]}";
			// json = "{\"id\":\"1\",\"name\":\"测试\",\"val\":[\"vw\",\"pc\"]}";
			json = "{\"abc\":[\"vw\",\"pc\"]}";
		}

		System.out.println(json);

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(json);

		System.out.println(jacksonUtil.getMap());
		System.out.println(jacksonUtil.getMap("name"));
		List<?> alluser = jacksonUtil.getList("alluser");
		System.out.println(jacksonUtil.getString("userImage"));
		System.out.println(jacksonUtil.getBoolean("verified"));
		System.out.println(alluser);
		
		try {
			System.out.println(jacksonUtil.getInt("num"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(jacksonUtil.containsKey("key"));
		
		return "";

	}

	@RequestMapping("printJSON")
	public String printJSON(ModelMap model) {

		String json = "";

		Map<String, Object> userData = new HashMap<String, Object>();
		Map<String, String> nameStruct = new HashMap<String, String>();
		List<?> UsersList = adminService.getAllAdmins();
		System.out.println(UsersList);
		nameStruct.put("first", "Joe");
		nameStruct.put("last", "Sixpack");
		userData.put("name", nameStruct);
		userData.put("gender", "MALE");
		userData.put("verified", Boolean.FALSE);
		userData.put("userImage", "Rm9vYmFyIQ==");
		userData.put("alluser", UsersList);

		JacksonUtil jacksonUtil = new JacksonUtil();
		json = jacksonUtil.Map2json(userData);

		System.out.println(json);

		model.addAttribute("json", json);

		return "printJSON";
	}

}