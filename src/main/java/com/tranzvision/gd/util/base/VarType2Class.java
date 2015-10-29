/**
 * 
 */
package com.tranzvision.gd.util.base;

import java.sql.Date;
import java.sql.Time;

import org.springframework.stereotype.Service;

/**
 * @author SHIHUA
 * @since 2015-10-29
 */
@Service
public class VarType2Class {

	/**
	 * 
	 * @param type
	 * @return Class[]
	 */
	public Class[] change2Class(String[] type) {
		Class[] cs = null;
		if (type != null) {
			cs = new Class[type.length];
			for (int i = 0; i < cs.length; i++) {
				if (type[i] != null || !"".equals(type[i])) {
					cs[i] = this.change2Class(type[i]);
				}
			}
		}
		return cs;
	}

	/**
	 * 
	 * @param type
	 * @return <T> Class<?>
	 */
	public <T> Class<?> change2Class(String type) {

		Class<?> clazz = null;

		switch (type) {
		case "String":
		case "string":
			clazz = String.class;
			break;
		case "int":
			clazz = int.class;
			break;
		case "Integer":
			clazz = Integer.class;
			break;
		case "float":
			clazz = float.class;
			break;
		case "Float":
			clazz = Float.class;
			break;
		case "double":
			clazz = double.class;
			break;
		case "Double":
			clazz = Double.class;
			break;
		case "long":
			clazz = long.class;
			break;
		case "Long":
			clazz = Long.class;
			break;
		case "Object":
			clazz = Object.class;
			break;
		case "Date":
		case "date":
			clazz = Date.class;
			break;
		case "Time":
		case "time":
			clazz = Time.class;
			break;
		case "String[]":
			clazz = String[].class;
			break;
		case "int[]":
			clazz = int[].class;
			break;
		case "float[]":
			clazz = float[].class;
			break;
		case "double[]":
			clazz = double[].class;
			break;
		}

		return clazz;
	}

}
