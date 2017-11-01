package com.tranzvision.gd.ztest.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.tranzvision.gd.util.base.Arith;
import com.tranzvision.gd.util.captcha.Patchca;
import com.tranzvision.gd.util.poi.excel.Test;
import com.tranzvision.gd.util.security.RegExpValidatorUtils;
import com.tranzvision.gd.util.session.TzSession;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.tsinghua.sms.SendSmsService;
import com.tranzvision.gd.ztest.model.Admin;
import com.tranzvision.gd.ztest.service.AdminService;

@Controller
@RequestMapping("/tranzvision/testurl")
public class TestController {

	@Autowired
	private AdminService adminService;
	
	@Autowired
	private SendSmsService sendSmsService;
	
	private Patchca patchaService;
	
	@Autowired
	private GetSeqNum getSeqNum;

	String message = "Admin!";

	private static final String TPLPrefix = "ztest/";
	
	@Autowired
	private Test testExcel;

	@RequestMapping("index")
	public String index(ModelMap model, HttpServletRequest request) {

		TzSession tzSession = new TzSession(request);
		patchaService = new Patchca();
		model.addAttribute("sessionid", tzSession.getSessionId());
		model.addAttribute("token", patchaService.getToken(request));

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
	public String addUser(ModelMap model) {
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
	public String getUser(ModelMap model) {

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

	/// *
	@SuppressWarnings("rawtypes")
	@RequestMapping("getalladmins")
	public ModelAndView getAllUser() {

		List UsersList = adminService.getAllAdmins();
		System.out.println(UsersList);

		ModelAndView model = new ModelAndView(TPLPrefix + "getalladmins");
		model.addObject("UsersList", UsersList);

		return model;
	}
	// */

	@RequestMapping("captcha")
	public void getCaptcha(HttpServletRequest request, HttpServletResponse response) {
		patchaService.genCaptcha(request, response);
	}
	
	@RequestMapping("exportexcel")
	public void exportExcel(HttpServletRequest request, HttpServletResponse response) {
		testExcel.exportDataSet();
	}
	
	@RequestMapping("exportexceltpl")
	public void exportExcelByTpl(HttpServletRequest request, HttpServletResponse response) {
		testExcel.exportDataByTemplate();
	}
	
	@RequestMapping("importexcel")
	public void importExcel(HttpServletRequest request, HttpServletResponse response) {
		testExcel.importData();
	}
	
	@RequestMapping("importexceltpl")
	public void importExcelTpl(HttpServletRequest request, HttpServletResponse response) {
		testExcel.importDataTpl();
	}
	
	@RequestMapping("testDivide")
	public void testDivide(HttpServletRequest request, HttpServletResponse response) {
		try {
			int a1 = 33;
			int b1 = 100;
			
			double a = Arith.div((double) a1, (double) b1, 4);
			
			System.out.println(a);
			System.out.println(a1/b1);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("sendsms")
	public void testSendSms(HttpServletRequest request, HttpServletResponse response) {
		try {
			//Map<String,String> mapRst = sendSmsService.doSendSms("18612929610", "你好，这是一条测试短信【清华经管】");
			//System.out.println(mapRst);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("regutil")
	public void testRegExpUtil(HttpServletRequest request, HttpServletResponse response) {
		try {
			System.out.println("判断是否合法字符："+ RegExpValidatorUtils.isEmail("shihua_23@163.com"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("testseq")
	public void testseqUtil(HttpServletRequest request, HttpServletResponse response) {
		
	}
	

	@RequestMapping(value = { "/seqNum/{steptype}" })
	@ResponseBody
	public void testGetSeqNum(HttpServletRequest request, HttpServletResponse response, 
			@PathVariable(value = "steptype") String steptype) {
		try {
			int i;
			for(i=0;i<200;i++){
				MyThread myThread = null;
				if("M".equals(steptype)){
					myThread = new MyThread("M");  
				}else{
					myThread = new MyThread("S");  
				}
				
				Thread thread = new Thread(myThread);  
				thread.start();  
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private class MyThread implements Runnable {
		private String t_stype_type = "S";
		
		public MyThread(String steptype){
			t_stype_type = steptype;
		}
		
		@Override
		public void run() {
			getSeqTest(t_stype_type);
		}  
		
		public void getSeqTest(String type){
			int index = 0;
			if("M".equals(type)){
				index = getSeqNum.getSeqNum("TEST_SEQ_NUM", "TEST5", 5, 10);
				System.out.println("自增5："+ index);
			}else{
				index = getSeqNum.getSeqNum("TEST_SEQ_NUM", "TEST1");
				System.out.println("自增1："+ index);
			}
		}
	}  
	
}