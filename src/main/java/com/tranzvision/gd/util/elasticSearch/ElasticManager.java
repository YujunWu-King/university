package com.tranzvision.gd.util.elasticSearch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tranzvision.gd.util.httpclient.HttpClientService;
import com.tranzvision.gd.util.wechart.PostJsonDataUtil;

public class ElasticManager {

	public static String putMovie() {
		String strRet = "";
		try {
			String putUrl = "http://localhost:9200/movies/movie/7";
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("title", "77");
			paramsMap.put("director", "88");
			paramsMap.put("year", 1989);
			paramsMap.put("genres", new String[]{"Drama"});
			//http请求
			/*HttpClientService HttpClientService = new HttpClientService(createUrl,"POST",paramsMap,"UTF-8");
			strRet = HttpClientService.sendRequest();*/
			
			strRet = PutJsonDataUtil.putJsonData(putUrl, paramsMap);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return strRet;
	}
	
	public static String getMovie() {
		String strRet = "";
		try {
			String getUrl = "http://localhost:9200/movies/movie/1";
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			//http请求
			HttpClientService HttpClientService = new HttpClientService(getUrl,"GET",paramsMap,"UTF-8");
			strRet = HttpClientService.sendRequest();
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return strRet;
	}
	
	public static String deleteMovie() {
		String strRet = "";
		try {
			String deleteUrl = "http://localhost:9200/movies/movie/7";
			strRet = DeleteJsonDataUtil.deleteJsonData(deleteUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return strRet;
	}
	
	public static String queryMovie() {
		String strRet = "";
		try {
			String queryUrl = "http://localhost:9200/movies/movie/_search";
			Map<String, Object> queryMap = new HashMap<String, Object>();
			queryMap.put("query", "drama");
			Map<String, Object> queryStringMap = new HashMap<String, Object>();
			queryStringMap.put("query_string",queryMap);
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("query", queryStringMap);
			//http请求
			/*
			HttpClientService HttpClientService = new HttpClientService(queryUrl,"POST",paramsMap,"UTF-8");
			strRet = HttpClientService.sendRequest();
			*/
			strRet = PostJsonDataUtil.postJsonData(queryUrl, paramsMap);
	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strRet;
	}
	
	
	public static String createIndex(String url,Map<String, Object> mapParams) {
		String strRet = "";		
		try {	
			strRet = PutJsonDataUtil.putJsonData(url, mapParams);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strRet;
	}
	
	
	public static String queryArticle(String searchText) {
		String strRet = "";
		try {
			String queryUrl = "http://202.120.24.114:9200/cms/_search";
			Map<String, Object> queryMap = new HashMap<String, Object>();
			queryMap.put("query", searchText);
			Map<String, Object> queryStringMap = new HashMap<String, Object>();
			queryStringMap.put("query_string",queryMap);
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("query", queryStringMap);
			
			strRet = PostJsonDataUtil.postJsonData(queryUrl, paramsMap);
	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strRet;
	}
	
}
