package com.tranzvision.gd.ztest.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.tranzvision.gd.ztest.service.impl.TestThreadServiceImpl;

import org.springframework.ui.ModelMap;

import com.tranzvision.gd.util.sql.TZGDObject;
import com.tranzvision.gd.util.sql.SqlParams;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.type.*;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.cfgdata.GetCookieSessionProps;
 
@Controller
@RequestMapping("/testuser")
public class UserController {
	@Autowired
	private SqlQuery sqlQuery;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TZGDObject tzSQLObject;
	
	@Autowired
	private ApplicationContext acx;
	
	public static Integer intg = new Integer(1); 
	
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
	
	@RequestMapping("testThread")
	public void testThread(HttpServletRequest request, HttpServletResponse response){
		String test = request.getParameter("test");
		for(int i = 0; i < 15; i++){
			Thread thread = new TestThreadServiceImpl();
			
			thread.start();
//			synchronized (test) {
//				try {
//					Thread.sleep(2000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
//				System.out.println(i + "=====>" + df.format(new Date()));// new Date()为获取当前系统时间
//			}
//			String tblName = "TEST_TABLE";
//			String fldName = "LOCK_ROWNUM";
//			sqlQuery.execute("LOCK TABLE PS_TZ_SEQNUM_T write");
//			String sql = "update PS_TZ_SEQNUM_T set TZ_SEQNUM=TZ_SEQNUM+1 where TZ_TABLE_NAME = ? and TZ_COL_NAME = ?";
//			sqlQuery.update(sql, new Object[] { tblName, fldName });
//			try {
//				System.out.println("===================>"+i);
//				Thread.sleep(2000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			sqlQuery.execute("UNLOCK TABLES");
		}
	}
	
}