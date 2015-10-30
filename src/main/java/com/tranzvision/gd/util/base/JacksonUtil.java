package com.tranzvision.gd.util.base;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * jackson JSON操作方法类
 * 
 * @author SHIHUA
 * @since 2015-10-30
 */
@Service
public class JacksonUtil {

	private Map<String, Object> jsonMap;

	/**
	 * 构造函数
	 */
	public JacksonUtil() {

	}

	/**
	 * 构造函数，把JSON字符串转换成 Map
	 * 
	 * @param jsonStr
	 */
	public JacksonUtil(String jsonStr) {
		jsonMap = this.parseJson(jsonStr);
	}

	/**
	 * 将JSON字符串转换成Map
	 * 
	 * @param jsonStr
	 */
	public void json2Map(String jsonStr) {
		jsonMap = this.parseJson(jsonStr);
	}

	/**
	 * 私有方法，调用jackson接口方法将JSON字符串转换成Map
	 * 
	 * @param jsonStr
	 * @return Map<String, Object> or null
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> parseJson(String jsonStr) {

		if ("".equals(jsonStr) || jsonStr == null) {
			return null;
		}
		Map<String, Object> jMap = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			jMap = mapper.readValue(jsonStr, Map.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jMap;
	}

	/**
	 * 将JSON字符串转换成json并返回，但不存在成员变量中
	 * 
	 * @param jsonStr
	 * @return Map<String, Object> or null
	 */
	public Map<String, Object> parseJson2Map(String jsonStr) {
		return this.parseJson(jsonStr);
	}

	/**
	 * 将Map转换成String并返回
	 * 
	 * @param mapData
	 * @return String
	 */
	public String Map2json(Map<String, Object> mapData) {
		String json = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			json = mapper.writeValueAsString(mapData);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * 获取通过构造函数或json2Map转换得到的Map
	 * 
	 * @return Map<String, Object> or null
	 */
	public Map<String, Object> getMap() {
		return jsonMap;
	}

	/**
	 * 根据key值获取Map值
	 * 
	 * @param key
	 * @return Map<String, Object> or null
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getMap(String key) {
		if (jsonMap == null) {
			return null;
		}
		try {
			return (Map<String, Object>) jsonMap.get(key);
		} catch (NullPointerException ex) {
			ex.printStackTrace();
		} catch (ClassCastException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 根据key值获取List值
	 * 
	 * @param key
	 * @return List<?> or null
	 */
	public List<?> getList(String key) {
		if (jsonMap == null) {
			return null;
		}
		try {
			return (List<?>) jsonMap.get(key);
		} catch (NullPointerException ex) {
			ex.printStackTrace();
		} catch (ClassCastException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 根据key值获取String值
	 * 
	 * @param key
	 * @return String or null
	 */
	public String getString(String key) {
		if (jsonMap == null) {
			return null;
		}
		try {
			return (String) jsonMap.get(key);
		} catch (NullPointerException ex) {
			ex.printStackTrace();
		} catch (ClassCastException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 根据key值获取int值
	 * 
	 * @param key
	 * @return int
	 */
	public int getInt(String key) {
		return (int) jsonMap.get(key);
	}

	/**
	 * 根据key值获取long值
	 * 
	 * @param key
	 * @return long
	 */
	public long getLong(String key) {
		return (long) jsonMap.get(key);
	}

	/**
	 * 根据key值获取double值
	 * 
	 * @param key
	 * @return double
	 */
	public double getDouble(String key) {
		return (double) jsonMap.get(key);
	}

	/**
	 * 根据key值获取boolean值
	 * 
	 * @param key
	 * @return boolean
	 */
	public boolean getBoolean(String key) {
		return (boolean) jsonMap.get(key);
	}

	/**
	 * 判断Map中是否存在某个键值
	 * 
	 * @param key
	 * @return boolean
	 */
	public boolean containsKey(String key) {
		return jsonMap.containsKey(key);
	}

}
