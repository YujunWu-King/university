package com.tranzvision.gd.util.base;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class PaseJsonUtil {
	public static JSONObject getJson(String jsonStr)
	{
		return JSONObject.fromObject(jsonStr);
	}

	public static JSONArray getJsonArray(String jsonStr)
	{
		return JSONArray.fromObject(jsonStr);
	}

}
