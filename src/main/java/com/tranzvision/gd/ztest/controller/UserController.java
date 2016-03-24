package com.tranzvision.gd.ztest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;

import com.tranzvision.gd.ztest.model.User;
import com.tranzvision.gd.ztest.service.UserService;

import org.springframework.ui.ModelMap;

import com.tranzvision.gd.util.sql.TZGDObject;
import com.tranzvision.gd.util.sql.SqlParams;
import com.tranzvision.gd.util.sql.type.*;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.cfgdata.GetCookieSessionProps;
 
@Controller
@RequestMapping("/testuser")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TZGDObject tzSQLObject;
	
	@Autowired
	private ApplicationContext acx;
	
	@Autowired
	private GetCookieSessionProps getCookieSessionProps;
	
	String message = "Welcome to Spring MVC!";
 
	@RequestMapping("index")
    public String index(ModelMap model){
		
		User tmpUser = null;
		Class<?> tmpClass = null;
		String tmpStr = "LiGang";
		try
		{
			tmpClass = Class.forName("com.tranzvision.gd.ztest.service.impl.UserServiceImpl",true,this.getClass().getClassLoader());
			tmpStr = "HelloWorld==>>111111";
			Object tmpObject = acx.getBean(tmpClass.newInstance().getClass());
			tmpStr = "HelloWorld==>>222222";
			java.lang.reflect.Method tmpMethod = tmpObject.getClass().getMethod("getOneUser", int.class);
			tmpStr = "HelloWorld==>>333333";
			tmpUser = (User)tmpMethod.invoke(tmpObject, 1);
			tmpStr = "HelloWorld==>>444444";
			//tmpUser = tmpObject.getOneUser(1);
	    	//model.addAttribute("nickname", "测试Spring");
			//tmpStr = "LiGang ==>>" + UserService.class.isInterface();
			//tmpStr = tmpClass.getName();
			
			TzString tmps1 = new TzString();
			TzInt tmps2 = new TzInt();
			TzInt tmps3 = new TzInt();
			tzSQLObject.sqlExec("select * from user where id=?", new SqlParams(1),tmps2,tmps3,tmps1);
			tmpStr = "HelloWorld===>>>000>>>" + tmps1.getValue();
			
			tzSQLObject.sqlExec("select * from user",tmps2,tmps3,tmps1);
			tmpStr += "<br>HelloWorld===>>>222>>>" + tmps1.getValue();
			
			tzSQLObject.sqlExec("select * from user where 1=2",tmps2,tmps3,tmps1);
			tmpStr += "<br>HelloWorld===>>>333>>>" + tmps1.getValue();
			
			tzSQLObject.sqlExec("update user set id=id where id=1");
			tmpStr += "<br>HelloWorld===>>>444>>>";
			
			tzSQLObject.sqlExec("delete from user where 1=2");
			tmpStr += "<br>HelloWorld===>>>555>>>";
			
			TzRecord tmps4 = new TzRecord();
			tzSQLObject.sqlExec("select * from user where id=?", new SqlParams(1),tmps4);
			tmpStr += "<br>HelloWorld===>>>111>>>" + tmps4.getTzString("Nickname").getValue();
			
			tmpStr += "<br>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>";
			TzSQLObject tmps5 = tzSQLObject.createSQLObject("select * from user");
			while(tmps5.fetch(tmps2,tmps3,tmps1) == true)
			{
				tmpStr += "<br>NickName==>>" + tmps1.getValue() + " &nbsp;&nbsp;id==>>" + tmps2.getValue() + " &nbsp;&nbsp;state==>>" + tmps3.getValue();
			}
			
			tmpStr += "<br>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>";
			TzSQLObject tmps6 = tzSQLObject.createSQLObject("select * from user");
			while(tmps6.fetch(tmps4) == true)
			{
				tmpStr += "<br>NickName==>>" + tmps4.getTzString("Nickname").getValue() + " &nbsp;&nbsp;id==>>" + tmps4.getTzInt("id").getValue() + " &nbsp;&nbsp;state==>>" + tmps4.getTzInt("state").getValue();
			}
			
			tmpStr += "<br>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>";
			tmpStr += "<br>HelloWorld1==>>" + tzSQLObject.getSQLText("SQL.test.HelloWorld1");
			tmpStr += "<br>HelloWorld2==>>" + tzSQLObject.getSQLText("SQL.test.HelloWorld2");
			tmpStr += "<br>HelloWorld3==>>" + tzSQLObject.getSQLText("SQL.test.HelloWorld3");
			
			tmpStr += "<br>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>";
			tmpStr += "<br>HelloWorld1==>>" + tzSQLObject.getHTMLText("HTML.test.HelloWorld1","LiGang");
			tmpStr += "<br>HelloWorld2==>>" + tzSQLObject.getHTMLText("HTML.test.HelloWorld2");
			tmpStr += "<br>HelloWorld3==>>" + tzSQLObject.getHTMLText("HTML.test.HelloWorld3");
		}
		catch(TzSystemException e)
		{
			tmpStr += "<br>" + e.toString();
		}
		catch(Exception e)
		{
			tmpStr = e.toString();
		}
		
		tmpStr += "<br>Hello World==>>Path==>>" + System.getProperty(getCookieSessionProps.getWebAppRootKey());
		model.addAttribute("nickname",tmpStr);

    	return "ztest/index";
    }
	
	@RequestMapping("hello")
	public ModelAndView showMessage(
			@RequestParam(value = "name", required = false, defaultValue = "World") String name) {
		System.out.println("in controller");
 
		//ModelAndView mv = new ModelAndView("ztest/hello");
		ModelAndView mv = new ModelAndView("ztest/hello");
		mv.addObject("message", message);
		mv.addObject("nickname", name + "!");
		return mv;
	}
	
	@RequestMapping("add")
    public String addUser(ModelMap model){
		String name = "李刚测试";
		User user = new User();
        user.setNickname(name);
        user.setState(2);
		int rst = userService.insertUser(user); 
		
		model.addAttribute("rst", rst);
		model.addAttribute("name", name);
		
    	return "ztest/add";
    }
	
	@RequestMapping("get")
    public String getUser(ModelMap model){
		
		int id = 1;
		User user = userService.getOneUser(id);
		model.addAttribute("id", user.getId());
		model.addAttribute("name", user.getNickname());
		model.addAttribute("state", user.getState());
		
    	return "ztest/get";
    }
	
}