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

import freemarker.template.utility.StringUtil;

/**
 * jackson JSON鎿嶄綔鏂规硶绫�
 * 
 * @author SHIHUA
 * @since 2015-10-30
 */
public class JacksonUtil {

	private Map<String, Object> jsonMap;

	/**
	 * 鏋勯�犲嚱鏁�
	 */
	public JacksonUtil() {

	}

	/**
	 * 鏋勯�犲嚱鏁帮紝鎶奐SON瀛楃涓茶浆鎹㈡垚 Map
	 * 
	 * @param jsonStr
	 */
	public JacksonUtil(String jsonStr) {
		jsonMap = this.parseJson(jsonStr);
	}

	/**
	 * 灏咼SON瀛楃涓茶浆鎹㈡垚Map
	 * 
	 * @param jsonStr
	 */
	public void json2Map(String jsonStr) {
		jsonMap = this.parseJson(jsonStr);
	}

	/**
	 * 绉佹湁鏂规硶锛岃皟鐢╦ackson鎺ュ彛鏂规硶灏咼SON瀛楃涓茶浆鎹㈡垚Map
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
	 * 灏咼SON瀛楃涓茶浆鎹㈡垚json骞惰繑鍥烇紝浣嗕笉瀛樺湪鎴愬憳鍙橀噺涓�
	 * 
	 * @param jsonStr
	 * @return Map<String, Object> or null
	 */
	public Map<String, Object> parseJson2Map(String jsonStr) {
		return this.parseJson(jsonStr);
	}

	/**
	 * 灏哅ap杞崲鎴怱tring骞惰繑鍥�
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
	 * 灏咥rrayList杞崲鎴怱tring骞惰繑鍥�
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
	 * 鑾峰彇閫氳繃鏋勯�犲嚱鏁版垨json2Map杞崲寰楀埌鐨凪ap
	 * 
	 * @return Map<String, Object> or null
	 */
	public Map<String, Object> getMap() {
		return jsonMap;
	}

	/**
	 * 鏍规嵁key鍊艰幏鍙朚ap鍊�
	 * 
	 * @param key
	 * @return Map<String, Object> or null
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getMap(String key) {
		if (jsonMap == null) {
			return null;
		} else if (jsonMap.get(key) == null || jsonMap.get(key).equals("")) {
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
	 * 鏍规嵁key鍊艰幏鍙朙ist鍊�
	 * 
	 * @param key
	 * @return List<?> or null
	 */
	public List<?> getList(String key) {
		if (jsonMap == null) {
			return null;
		} else if (jsonMap.get(key) == null || jsonMap.get(key).equals("")) {
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
	 * 鏍规嵁key鍊艰幏鍙朣tring鍊�
	 * 
	 * @param key
	 * @return String or null
	 */
	public String getString(String key) {
		if (jsonMap == null) {
			return null;
		} else if (jsonMap.get(key) == null) {
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
	 * 鏍规嵁key鍊艰幏鍙杋nt鍊�
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
	 * 鏍规嵁key鍊艰幏鍙杔ong鍊�
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
	 * 鏍规嵁key鍊艰幏鍙杁ouble鍊�
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
	 * 鏍规嵁key鍊艰幏鍙朾oolean鍊�
	 * 
	 * @param key
	 * @return boolean
	 */
	public boolean getBoolean(String key) {
		return Boolean.parseBoolean(jsonMap.get(key).toString());
	}

	/**
	 * 鏍规嵁key鑾峰彇缁欏畾鏍煎紡鐨勬棩鏈熸椂闂存暟鍊�
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
	 * 鏍规嵁key鑾峰彇鎸囧畾鏍煎紡锛坹yyy-MM-dd锛夌殑鏃ユ湡鍊�
	 * 
	 * @param key
	 * @return Date or null
	 */
	public Date getDate(String key) {
		return this.getDate(key, "yyyy-MM-dd");
	}

	/**
	 * 鏍规嵁key鑾峰彇鎸囧畾鏍煎紡锛坹yyy-MM-dd HH:mm:ss锛夌殑鏃ユ湡鏃堕棿鍊�
	 * 
	 * @param key
	 * @return Date or null
	 */
	public Date getDateTime(String key) {
		return this.getDate(key, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 鍒ゆ柇Map涓槸鍚﹀瓨鍦ㄦ煇涓敭鍊�
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
	
	/**
	 * 将Object转换为json并返回
	 * @param o
	 * @return
	 */
	public String Object2Json(Object o){
		String jsonStr = "";
		ObjectMapper MAPPER = new ObjectMapper();
		try {
			jsonStr = MAPPER.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
		return jsonStr;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*JacksonUtil jacksonUtil = new JacksonUtil();
		//String a = "{\"tplID\":\"100\",\"fileName\":\"aaa\",\"storDates\":[{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_1\",\"fieldName\":\"鏂囧瓧璇存槑\",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-1\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_2r_name\",\"fieldName\":\"濮撳悕\",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-2\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_2r_company\",\"fieldName\":\"宸ヤ綔鍗曚綅\",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-3\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_2r_post\",\"fieldName\":\"鑱屽姟\",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-4\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_2r_phone\",\"fieldName\":\"鑱旂郴鐢佃瘽\",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-5\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_2r_email\",\"fieldName\":\"鐢靛瓙閭欢\",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-6\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_2r_relation\",\"fieldName\":\"鐢宠浜哄叧绯籠",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-7\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_2r_language\",\"fieldName\":\"鎺ㄨ崘淇¤闊砛",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-8\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_2r_by1\",\"fieldName\":\"涓庤�冪敓鍏崇郴\",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-9\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_2r_by2\",\"fieldName\":\"澶囩敤瀛楁浜孿",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-10\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_2r_by3\",\"fieldName\":\"澶囩敤瀛楁涓塡",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-11\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_2r_by4\",\"fieldName\":\"澶囩敤瀛楁鍥沑",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-12\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_2r_by5\",\"fieldName\":\"澶囩敤瀛楁浜擻",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-13\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_2r_sex\",\"fieldName\":\"鎬у埆\",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-14\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_8\",\"fieldName\":\"鎺ㄨ崘浜哄鍚峔",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-15\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_6\",\"fieldName\":\"琚帹鑽愯�冪敓濮撳悕\",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-16\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_3\",\"fieldName\":\"鎮ㄨ璇嗚�冪敓鐨勬柟寮忋�佽璇嗙殑鏃堕棿鍜屼簡瑙ｇ▼搴︼紝瀵硅�冪敓鎬濇兂鍝佸痉銆侀亾寰蜂慨鍏绘柟闈㈢殑浠嬬粛锛歕",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-17\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_4\",\"fieldName\":\"瀵硅�冪敓瀛︽湳姘村钩銆佺鐮旇兘鍔涖�佸疄璺佃兘鍔涖�佺爺绌舵垚鏋溿�佺煡璇嗙粨鏋勩�佸鍥借姘村钩绛夌殑浠嬬粛锛歕",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-18\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_5\",\"fieldName\":\"鑰冪敓鐨勭壒闀垮拰寮辩偣锛岃鑰冪敓鏄惁鍏锋湁鍒涙柊鐨勬綔鍔涳紝鏈夋棤缁х画鍩瑰吇鐨勫墠閫旓紝瀵硅�冪敓鎶ヨ�冩竻鍗庡ぇ瀛︽帹鑽愬厤璇曠爺绌剁敓鐨勬剰瑙侊細\",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-19\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_APP_INS_ID\",\"fieldName\":\"鎶ュ悕琛ㄥ疄渚嬬紪鍙穃",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-20\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_APP_FORM_STA\",\"fieldName\":\"鎶ュ悕琛ㄦ彁浜ょ姸鎬乗",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-21\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_FORM_SP_STA\",\"fieldName\":\"鍒濆鐘舵�乗",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-22\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_APP_SUB_DTTM\",\"fieldName\":\"鎻愪氦鏃堕棿\",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-23\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_ZL_AUDIT_STATUS\",\"fieldName\":\"璧勬枡瀹℃牳鐘舵�乗",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-24\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_COLOR_SORT_ID\",\"fieldName\":\"棰滆壊绫诲埆缂栧彿\",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-25\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_COLOR_NAME\",\"fieldName\":\"棰滆壊绫诲埆\",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-26\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_REMARK\",\"fieldName\":\"澶囨敞\",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-27\"}},{\"data\":{\"tplID\":\"100\",\"fieldID\":\"TZ_REMARK_SHORT\",\"fieldName\":\"鐭娉╘",\"pdffield1\":\"\",\"pdffield2\":\"\",\"pdffield3\":\"\",\"id\":\"KitchenSink.view.template.bmb.myBmbPdfModel-28\"}}]}";
		String a = "{bmrClass={instanceId=bmrClass, itemId=CC_Project, itemName=鎶ヨ�冩柟鍚�, title=鎶ヨ�冩柟鍚�, orderby=1, value=76, StorageType=S, wzsm=鎶ュ悕琛ㄦ祴璇�, classname=baseComponent}, bmrBatch={instanceId=bmrBatch, itemId=CC_Batch, itemName=闈㈣瘯鎵规, title=闈㈣瘯鎵规, orderby=2, value=, StorageType=S, option={A151442202={txt=鐝骇1鎵规1, other=N, code=45, defaultval=N, orderby=0, weight=0}, A646431964={txt=鐝骇1鎵规2, other=N, code=46, defaultval=N, orderby=0, weight=0}}, wzsm=, classname=Select}}";
		
		
		
		
		jacksonUtil.json2Map(a);
		String tplID = jacksonUtil.getString("tplID");
		String fileName = jacksonUtil.getString("fileName");
		String storDates = jacksonUtil.getString("storDates");

		System.out.println("tplID锛�" + tplID);
		System.out.println("fileName锛�" + fileName);
		System.out.println("storDates锛�" + storDates);

		// jacksonUtil.json2Map(storDates);
		List<Map<String, Object>> jsonArray = (List<Map<String, Object>>) jacksonUtil.getList("storDates");
		System.out.println("jsonArray锛�" + jsonArray.size()); */
		
		String acc = "{\"classId\":147,\"batchId\":108,\"data\":{\"groupName\":\"2缁刓\",\"groupID\":\"2\",\"check\":true,\"suNum\":0,\"id\":\"KitchenSink.view.enrollmentManagement.interviewGroup.interviewGroupModel-2\"}}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(acc);
		Map<String, Object> data = jacksonUtil.getMap("data");
		System.out.println(data.get("check"));

		
	}

}
