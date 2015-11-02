package com.tranzvision.gd.ztest.service.impl;
 
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import com.tranzvision.gd.TZBaseBundle.service.Framework;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.ztest.dao.AdminMapper;
import com.tranzvision.gd.ztest.model.Admin;
import com.tranzvision.gd.ztest.service.AdminService;
import com.tranzvision.gd.ztest.service.TestService;

@Service
public class AdminServiceImpl implements AdminService{
 
	private static final String[] String = null;

	@Autowired
    private ApplicationContext ctx;
	
    @Autowired
    private AdminMapper AdminMapper;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    private SqlQuery sqlQuery;
    
    public int insertAdmin(Admin admin) {
        // TODO Auto-generated method stub
        return AdminMapper.insert(admin);
    }
    
    public Admin getOneAdmin(int id){
    	/*
    	Framework obj = (Framework)ctx.getBean("com.tranzvision.gd.ztest.service.impl.TestServiceImpl");
    	String str = obj.testMethod(1);
    	System.out.println("----------------------------------");
    	System.out.println(str);
		System.out.println("----------------------------------");
		
		Framework obj2 = (Framework)ctx.getBean("com.tranzvision.gd.ztest.service.impl.TestFramework");
    	String str2 = obj2.testMethod(2);
    	System.out.println("----------------------------------");
    	System.out.println(str2);
		System.out.println("----------------------------------");
    	*/
    	
    	/*
    	Object obj = (Object)ctx.getBean("abc");
    	Class<?> clazz = obj.getClass();
    	System.out.println("----------------------------------");
		System.out.println(clazz);
		System.out.println("----------------------------------");
		String[] type = {"int"}; 
		Class partypes[] = this.getMethodClass(type);
    	Method method = BeanUtils.findMethod(clazz,"testMethod",partypes);
    	System.out.println("----------------------------------");
		System.out.println(method);
		System.out.println("----------------------------------");
		Object[] ex = new Object[1];
		ex[0] = 1;
		Object retobj = ReflectionUtils.invokeMethod(method, obj, ex);
		System.out.println("----------------------------------");
		System.out.println(retobj);
		System.out.println("----------------------------------");
		System.out.println("----------------------------------");
		String adminRealname = (String)retobj;
		System.out.println("realname:" + adminRealname);
		System.out.println("----------------------------------");
    	*/
		return AdminMapper.selectByPrimaryKey(id);
    	
    }
    
    ///*
    public List getAllAdmins() {
		System.out.println("jdbcTemplate=" + jdbcTemplate);
		
		/*
		String testvar = jdbcTemplate.queryForObject("SELECT 'Y' FROM admin WHERE adminid=1", String.class);
		System.out.println(testvar);
		
		int num = jdbcTemplate.queryForObject("SELECT count(*) FROM admin WHERE adminid=1", int.class);
		System.out.println(num);
		
		
		return jdbcTemplate
				.queryForList("SELECT * FROM admin WHERE 1=1");
		*/
		Object[] args = new Object[] {new Integer(50)};
		
		
		String testvar = sqlQuery.queryForObject("SELECT 'Y' FROM admin WHERE adminid=?", args, "String");
		System.out.println(testvar);
		
		String testString = sqlQuery.queryForObject("SELECT 'Y' FROM admin WHERE adminid=1", "String");
		System.out.println(testString);
		
		int testInt = sqlQuery.queryForObject("SELECT count(*) FROM admin", "int");
		System.out.println(testInt);
		
		Map<String, Object> testmap = sqlQuery.queryForMap("SELECT * FROM admin WHERE adminid=1");
		System.out.println(testmap);
		System.out.println(testmap.get("admin_name"));
		//{adminid=1, admin_name=管理员, admin_pwd=123456, admin_realname=测试名字1}
		
		List<?> testlist = sqlQuery.queryForList("SELECT * FROM admin WHERE adminid=1");
		System.out.println(testlist);
		//[{adminid=1, admin_name=管理员, admin_pwd=123456, admin_realname=测试名字1}]
		
		return sqlQuery.queryForList("SELECT * FROM admin WHERE adminid<>?", args);
		
	}
 	//*/
    
	public Class[] getMethodClass(String[] type) {
		Class[] cs = null;
		if (type != null) {
			cs = new Class[type.length];
			for (int i = 0; i < cs.length; i++) {
				if (type[i] != null || !"".equals(type[i])) {
					if ("String".equals(type[i])) {
						cs[i] = String.class;
					} else if ("int".equals(type[i])) {
						cs[i] = int.class;
					} else if ("Integer".equals(type[i])) {
						cs[i] = Integer.class;
					} else if ("float".equals(type[i])) {
						cs[i] = float.class;
					} else if ("Float".equals(type[i])) {
						cs[i] = Float.class;
					} else if ("double".equals(type[i])) {
						cs[i] = double.class;
					} else if ("Double".equals(type[i])) {
						cs[i] = Double.class;
					} else if ("String[]".equals(type[i])) {
						cs[i] = String[].class;
					} else if ("int[]".equals(type[i])) {
						cs[i] = int[].class;
					} else if ("float[]".equals(type[i])) {
						cs[i] = float[].class;
					} else if ("double[]".equals(type[i])) {
						cs[i] = double[].class;
					} else if ("Object".equals(type[i])) {
						cs[i] = Object.class;
					}
				}
			}
		}
		return cs;
	}
    
}