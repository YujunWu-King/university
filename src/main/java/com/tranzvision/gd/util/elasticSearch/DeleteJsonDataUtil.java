package com.tranzvision.gd.util.elasticSearch;

import java.util.Map;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClients;

import com.tranzvision.gd.util.base.JacksonUtil;

public class DeleteJsonDataUtil {

	private static final String USER_AGENT = "Mozilla/5.0";

	/**
	 * 发送PUT请求
	 */
	
	public static String deleteJsonData (String url) {
		String strRet = "";
		ResponseHandler<String> responseHandler = new BasicResponseHandler();  
		
		try {
			//第一步：创建HttpClient对象  
			HttpClient httpClient = HttpClients.createDefault();
			//第二步：创建httpPut对象 
			HttpDelete httpDelete = new HttpDelete(url);
			
			//第三步：给httpDelete设置J参数  			
	        httpDelete.setHeader("User-Agent", USER_AGENT);
	        httpDelete.setHeader("cache-control", "no-cache");
	        httpDelete.setHeader("Content-Type", "application/json");  
		
	        //第四步：发送HttpPut请求，获取返回值  
	        strRet = httpClient.execute(httpDelete,responseHandler); 
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return strRet;
	}
}
