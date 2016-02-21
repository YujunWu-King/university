package com.tranzvision.gd.util.base;

import java.lang.reflect.*;

public class ObjectDoMethod {
	public static Object Load(String cName, String MethodName, String[] type,
			Object[] arglist) {
		Object retobj = null;
		try {
			// 加载指定的Java类
			Class cls = Class.forName(cName);
			Object obj = cls.newInstance();
			Class partypes[] = ObjectDoMethod.getMethodClass(type);
			Method meth = cls.getMethod(MethodName, partypes);
			retobj = meth.invoke(obj, arglist);
		} catch (Throwable e) {
		}
		return retobj;
	}

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
					}
				}
			}
		}
		return cs;
	}

}
