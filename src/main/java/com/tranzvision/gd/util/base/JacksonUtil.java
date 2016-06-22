package com.tranzvision.gd.util.base;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * jackson JSON操作方法类
 * 
 * @author SHIHUA
 * @since 2015-10-30
 */
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
		mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		try {
			jMap = mapper.readValue(jsonStr, Map.class);
		} catch (IOException e) {
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
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * 将ArrayList转换成String并返回
	 * 
	 * @param listData
	 * @return String
	 */
	public String List2json(ArrayList<?> listData) {
		String json = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			json = mapper.writeValueAsString(listData);
		} catch (JsonProcessingException e) {
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
			if (jsonMap.containsKey(key)) {
				return String.valueOf(jsonMap.get(key));
			}
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
	 * @throws Exception
	 */
	public int getInt(String key) throws Exception {
		try {
			return Integer.parseInt(jsonMap.get(key).toString());
		} catch (NumberFormatException nfe) {
			throw new Exception(nfe.getMessage());
		}
	}

	/**
	 * 根据key值获取long值
	 * 
	 * @param key
	 * @return long
	 * @throws Exception
	 */
	public long getLong(String key) throws Exception {
		try {
			return Long.parseLong(jsonMap.get(key).toString());
		} catch (NumberFormatException nfe) {
			throw new Exception(nfe.getMessage());
		}
	}

	/**
	 * 根据key值获取double值
	 * 
	 * @param key
	 * @return double
	 * @throws Exception
	 */
	public double getDouble(String key) throws Exception {
		try {
			return Double.parseDouble(jsonMap.get(key).toString());
		} catch (NullPointerException npe) {
			throw new Exception(npe.getMessage());
		} catch (NumberFormatException nfe) {
			throw new Exception(nfe.getMessage());
		}
	}

	/**
	 * 根据key值获取boolean值
	 * 
	 * @param key
	 * @return boolean
	 */
	public boolean getBoolean(String key) {
		return Boolean.parseBoolean(jsonMap.get(key).toString());
	}

	/**
	 * 根据key获取给定格式的日期时间数值
	 * 
	 * @param key
	 * @param dateFormat
	 * @return Date or null
	 */
	public Date getDate(String key, String dateFormat) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
			return formatter.parse((String) jsonMap.get(key));
		} catch (java.text.ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据key获取指定格式（yyyy-MM-dd）的日期值
	 * 
	 * @param key
	 * @return Date or null
	 */
	public Date getDate(String key) {
		return this.getDate(key, "yyyy-MM-dd");
	}

	/**
	 * 根据key获取指定格式（yyyy-MM-dd HH:mm:ss）的日期时间值
	 * 
	 * @param key
	 * @return Date or null
	 */
	public Date getDateTime(String key) {
		return this.getDate(key, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 判断Map中是否存在某个键值
	 * 
	 * @param key
	 * @return boolean
	 */
	public boolean containsKey(String key) {
		if (jsonMap == null) {
			return false;
		}
		return jsonMap.containsKey(key);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JacksonUtil jacksonUtil = new JacksonUtil();
		String a = "{\"tplID\":\"100\",\"fileName\":\"aaa\",\"storDates\":[{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_1\",\"fieldName\":\"文字说明\",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-1\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_2r_name\",\"fieldName\":\"姓名\",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-2\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_2r_company\",\"fieldName\":\"工作单位\",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-3\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_2r_post\",\"fieldName\":\"职务\",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-4\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_2r_phone\",\"fieldName\":\"联系电话\",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-5\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_2r_email\",\"fieldName\":\"电子邮件\",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-6\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_2r_relation\",\"fieldName\":\"申请人关系\",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-7\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_2r_language\",\"fieldName\":\"推荐信语音\",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-8\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_2r_by1\",\"fieldName\":\"与考生关系\",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-9\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_2r_by2\",\"fieldName\":\"备用字段二\",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-10\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_2r_by3\",\"fieldName\":\"备用字段三\",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-11\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_2r_by4\",\"fieldName\":\"备用字段四\",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-12\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_2r_by5\",\"fieldName\":\"备用字段五\",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-13\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_2r_sex\",\"fieldName\":\"性别\",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-14\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_8\",\"fieldName\":\"推荐人姓名\",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-15\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_6\",\"fieldName\":\"被推荐考生姓名\",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-16\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_3\",\"fieldName\":\"您认识考生的方式、认识的时间和了解程度，对考生思想品德、道德修养方面的介绍：\",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-17\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_4\",\"fieldName\":\"对考生学术水平、科研能力、实践能力、研究成果、知识结构、外国语水平等的介绍：\",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-18\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_5\",\"fieldName\":\"考生的特长和弱点，该考生是否具有创新的潜力，有无继续培养的前途，对考生报考清华大学推荐免试研究生的意见：\",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-19\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_APP_INS_ID\",\"fieldName\":\"报名表实例编号\",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-20\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_APP_FORM_STA\",\"fieldName\":\"报名表提交状态\",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-21\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_FORM_SP_STA\",\"fieldName\":\"初审状态\",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-22\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_APP_SUB_DTTM\",\"fieldName\":\"提交时间\",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-23\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_ZL_AUDIT_STATUS\",\"fieldName\":\"资料审核状态\",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-24\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_COLOR_SORT_ID\",\"fieldName\":\"颜色类别编号\",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-25\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_COLOR_NAME\",\"fieldName\":\"颜色类别\",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-26\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_REMARK\",\"fieldName\":\"备注\",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-27\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_REMARK_SHORT\",\"fieldName\":\"短备注\",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-28\"}}]}";
		jacksonUtil.json2Map(a);
		String tplID = jacksonUtil.getString("tplID");
		String fileName = jacksonUtil.getString("fileName");
		String storDates = jacksonUtil.getString("storDates");

		System.out.println("tplID：" + tplID);
		System.out.println("fileName：" + fileName);
		System.out.println("storDates：" + storDates);
		
		//jacksonUtil.json2Map(storDates);
		List<Map<String, Object>> jsonArray = (List<Map<String, Object>>) jacksonUtil.getList("storDates");
		System.out.println("jsonArray：" + jsonArray.size());
	}

}
