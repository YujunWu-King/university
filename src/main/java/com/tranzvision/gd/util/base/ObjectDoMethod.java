package com.tranzvision.gd.util.base;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ObjectDoMethod {
	
	/**
	 * 类方法调用
	 * @param cName			类名称		
	 * @param MethodName	方法名称
	 * @param type			参数类型，eg:	String[] paramsTypes = new String[] { "String" };
	 * @param arglist		参数，eg:		Object[] arglist = new Object[] { "123" };
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object Load(String cName, String MethodName, String[] type,
			Object[] arglist) {
		Object retobj = null;
		try {
			// 加载指定的Java类
			System.out.println(cName+"==="+MethodName+"===="+arglist);
			Class cls = Class.forName(cName);
			Object obj = cls.newInstance();
			Class partypes[] = ObjectDoMethod.getMethodClass(type);
			Method meth = cls.getMethod(MethodName, partypes);
			System.out.println(meth.getName());
			retobj = meth.invoke(obj, arglist);
			System.out.println("retobj=====>"+retobj);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return retobj;
	}
	
	
	
	/**
	 * 捕获被调用方法抛出的异常并抛出TzException异常
	 * @author zhanglang	2019-08-19
	 * 
	 * @param cName			类名称
	 * @param MethodName	方法名称
	 * @param type			参数类型，eg:	String[] paramsTypes = new String[] { "String" };
	 * @param arglist		参数，eg:		Object[] arglist = new Object[] { "123" };
	 * @return
	 * @throws TzException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object TzLoad(String cName, String MethodName, String[] type,
			Object[] arglist) throws TzException {
		Object retobj = null;
		try {
			// 加载指定的Java类
			System.out.println(cName+"==="+MethodName+"===="+arglist);
			Class cls = Class.forName(cName);
			Object obj = cls.newInstance();
			Class partypes[] = ObjectDoMethod.getMethodClass(type);
			Method meth = cls.getMethod(MethodName, partypes);
			System.out.println(meth.getName());
			retobj = meth.invoke(obj, arglist);
			System.out.println("retobj=====>"+retobj);
		} catch(InvocationTargetException e) {
			//此处接收被调用方法内部未被捕获的异常
			Throwable tzEx = e.getTargetException();
			throw new TzException(tzEx.getMessage(), tzEx);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return retobj;
	}
	
	

	@SuppressWarnings("rawtypes")
	public static Class[] getMethodClass(String[] type) {
		Class[] cs = null;
		if (type != null) {
			cs = new Class[type.length];
			for (int i = 0; i < cs.length; i++) {
				if (type[i] != null || !"".equals(type[i])) {
					if ("String".equals(type[i])) {
						cs[i] = String.class;
					} else if ("long".equals(type[i])) {
						cs[i] = long.class;
					}else if ("Long".equals(type[i])) {
						cs[i] = Long.class;
					}else if ("int".equals(type[i])) {
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
					} else {
						try {
							//可传类型名称，张浪添加，20190227
							cs[i] = Class.forName(type[i]);
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return cs;
	}
}
