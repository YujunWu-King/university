package com.tranzvision.gd.util.base;

import java.util.HashMap;
import java.util.Map;

/**
 * 内存数据化存储
 * 
 * @author caoy
 *
 */
public class Memoryparameter {

	/**
	 * 消息集合 结构 Key:TZ_XXJH_ID@TZ_JG_ID
	 * value:map(key:TZ_MSG_ID@TZ_LANGUAGE_ID,value:TZ_MSG_TEXT)
	 */
	public static Map<String, Map<String, String>> messageText = new HashMap<String, Map<String, String>>();

}
