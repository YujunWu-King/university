package com.tranzvision.gd.util.elasticSearch;

import java.io.IOException;
import java.util.Map;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClients;

import com.tranzvision.gd.util.base.JacksonUtil;


public class PutJsonDataUtil {
	
	private static final String USER_AGENT = "Mozilla/5.0";

	/**
	 * 发送PUT请求
	 */
	
	public static String putJsonData (String url,Map<String,Object> jsonMap) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		ResponseHandler<String> responseHandler = new BasicResponseHandler();  
		
		try {
			//第一步：创建HttpClient对象  
			HttpClient httpClient = HttpClients.createDefault();
			//第二步：创建httpPut对象 
			HttpPut httpPut = new HttpPut(url);
			
			String json = jacksonUtil.Map2json(jsonMap);
			System.out.println("---->  "+json);
			//第三步：给httpPost设置JSON格式的参数  
	        StringEntity requestEntity = new StringEntity(json,"utf-8");  
	        requestEntity.setContentEncoding("UTF-8");
	        requestEntity.setContentType("application/json");
			
	        httpPut.setHeader("User-Agent", USER_AGENT);
	        httpPut.setHeader("cache-control", "no-cache");
	        httpPut.setHeader("Content-Type", "application/json");  
	        httpPut.setEntity(requestEntity);
		
	        //第四步：发送HttpPut请求，获取返回值  
	        strRet = httpClient.execute(httpPut,responseHandler); 
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return strRet;
	}
}
