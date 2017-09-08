package com.tranzvision.gd.util.wechart;

import java.io.IOException;
import java.util.Map;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.tranzvision.gd.util.base.JacksonUtil;


/**
 * Post JSON
 * @author zhanglang
 *
 */
public class PostJsonDataUtil {
	
	private static final String USER_AGENT = "Mozilla/5.0";

	/**
	 * 发送post json请求
	 * @param url
	 * @param jsonMap
	 * @return
	 * @throws Exception 
	 */
	public static String postJsonData(String url,Map<String,Object> jsonMap) throws Exception{
		 String returnValue = "";
		 CloseableHttpClient httpClient = null;  
	     ResponseHandler<String> responseHandler = new BasicResponseHandler();  
	     JacksonUtil jacksonUtil = new JacksonUtil();
	     try{
	    	 //第一步：创建HttpClient对象  
	    	 httpClient = HttpClients.createDefault();
			 //第二步：创建httpPost对象 
			 HttpPost httpPost = new HttpPost(url);
	    	 
			 String json = jacksonUtil.Map2json(jsonMap);
			 System.out.println("---->  "+json);
			 //第三步：给httpPost设置JSON格式的参数  
	         StringEntity requestEntity = new StringEntity(json,"utf-8");  
	         requestEntity.setContentEncoding("UTF-8");
	         requestEntity.setContentType("application/json");
	         
	         httpPost.setHeader("User-Agent", USER_AGENT);
	         httpPost.setHeader("cache-control", "no-cache");
	         httpPost.setHeader("Content-Type", "application/json");  
	         httpPost.setEntity(requestEntity);  

	         //创建响应处理器处理服务器响应内容  
//	         ResponseHandler<String> responseHandler = new ResponseHandler<String>(){
//                public String handleResponse(final HttpResponse response) throws ClientProtocolException,IOException{
//                    int status = response.getStatusLine().getStatusCode();
//                    System.out.println(status);
//                    if (status >= 200 && status < 300){
//                        HttpEntity entity = response.getEntity();
//                        return entity !=null ? EntityUtils.toString(entity) : null;
//                    }else{
//                        throw new ClientProtocolException("Unexpected response status: " + status);
//                    }
//                }
//            };

	         //第四步：发送HttpPost请求，获取返回值  
	         returnValue = httpClient.execute(httpPost,responseHandler); 
	     }catch(Exception e){
	    	 throw e;
	     }finally {  
	           try {  
	               httpClient.close();  
	           } catch (IOException e) {  
	               e.printStackTrace();  
	           }  
	     }
	     
	     return returnValue;  
	 }
}
