package com.tranzvision.gd.util.wechart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tranzvision.gd.util.base.TzException;
import com.tranzvision.gd.util.httpclient.HttpClientService;

/**
 * 微信公众号模板消息发送接口
 * @author zhanglang
 *
 */
public class TzWxTemplateMessageUtil {

	//发送模板消息
	private static final String tz_sendTmpMessage_url = "https://api.weixin.qq.com/cgi-bin/message/template/send";
	
	
	/**
	 * 发送模板消息
	 * @param access_token
	 * @param opendId			//接收者openid
	 * @param template_id		//模板ID
	 * @param dataList			//模板数据 [{"key":"keynote1","value":"恭喜你购买成功！","color":"#173177"},......],color模板内容字体颜色，不填默认为黑色
	 * @return
	   {
           "errcode":0,
           "errmsg":"ok",
           "msgid":200228332
       }
	 * @throws TzException
	 */
	public static String sendTemplateMessage(String access_token, String opendId, String template_id, List<Map<String,String>> dataList) throws TzException{
		String returnStr = "";
		try{
			if(dataList == null || dataList.size() <=0){
				throw new TzException("模板数据为空");
			}
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("touser", opendId);
			paramsMap.put("template_id", template_id);
			
			Map<String, Object> dataMap = new HashMap<String, Object>();
			for(Map<String,String> dataKeyWordMap : dataList){
				if(dataKeyWordMap.containsKey("key") && dataKeyWordMap.containsKey("value")){
					Map<String, Object> keyWordMap = new HashMap<String, Object>();
					
					String key = dataKeyWordMap.get("key");
					String value = dataKeyWordMap.get("value");
					
					keyWordMap.put("value", value);
					if(dataKeyWordMap.containsKey("color") && !"".equals(dataKeyWordMap.get("color"))){
						keyWordMap.put("color", dataKeyWordMap.get("color"));
					}
					dataMap.put(key, keyWordMap);
				}else{
					throw new TzException("模板数据不正确");
				}
			}
			paramsMap.put("data", dataMap);
			
			
			String sendTmpMessageUrl = tz_sendTmpMessage_url + "?access_token=" + access_token;
			//http请求
			HttpClientService HttpClientService = new HttpClientService(sendTmpMessageUrl,"POST",paramsMap,"UTF-8");
			returnStr = HttpClientService.sendRequest();
		}catch(Exception e){
			throw new TzException("https请求失败："+e.getMessage());
		}
		return returnStr;
	}
	
	
	
	
	/**
	 * 发送模板消息,模板跳转指定url
	 * @param access_token
	 * @param opendId			//接收者openid
	 * @param template_id		//模板ID
	 * @param dataList			//模板数据 [{"key":"keynote1","value":"恭喜你购买成功！","color":"#173177"},......],color模板内容字体颜色，不填默认为黑色
	 * @param url				//模板跳转链接
	 * @return
	   {
           "errcode":0,
           "errmsg":"ok",
           "msgid":200228332
       }
	 * @throws TzException
	 */
	public static String sendTemplateMessage(String access_token, String opendId, String template_id, List<Map<String,String>>dataList, String url) throws TzException{
		String returnStr = "";
		try{
			if(dataList == null || dataList.size() <=0){
				throw new TzException("模板数据为空");
			}
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("touser", opendId);
			paramsMap.put("template_id", template_id);
			paramsMap.put("url", url);
			
			Map<String, Object> dataMap = new HashMap<String, Object>();
			for(Map<String,String> dataKeyWordMap : dataList){
				if(dataKeyWordMap.containsKey("key") && dataKeyWordMap.containsKey("value")){
					Map<String, Object> keyWordMap = new HashMap<String, Object>();
					
					String key = dataKeyWordMap.get("key");
					String value = dataKeyWordMap.get("value");
					
					keyWordMap.put("value", value);
					if(dataKeyWordMap.containsKey("color") && !"".equals(dataKeyWordMap.get("color"))){
						keyWordMap.put("color", dataKeyWordMap.get("color"));
					}
					dataMap.put(key, keyWordMap);
				}else{
					throw new TzException("模板数据不正确");
				}
			}
			paramsMap.put("data", dataMap);
			
			
			String sendTmpMessageUrl = tz_sendTmpMessage_url + "?access_token=" + access_token;
			//http请求
			HttpClientService HttpClientService = new HttpClientService(sendTmpMessageUrl,"POST",paramsMap,"UTF-8");
			returnStr = HttpClientService.sendRequest();
		}catch(Exception e){
			throw new TzException("https请求失败："+e.getMessage());
		}
		return returnStr;
	}
	
	
	
	
	/**
	 * 发送模板消息，模板跳转小程序
	 * @param access_token
	 * @param opendId			//接收者openid
	 * @param template_id		//模板ID
	 * @param dataList			//模板数据 [{"key":"keynote1","value":"恭喜你购买成功！","color":"#173177"},......],color模板内容字体颜色，不填默认为黑色
	 * @param appid				//所需跳转到的小程序appid（该小程序appid必须与发模板消息的公众号是绑定关联关系）
	 * @param pagepath			//所需跳转到小程序的具体页面路径，支持带参数,（示例index?foo=bar）
	 * @return
	   {
           "errcode":0,
           "errmsg":"ok",
           "msgid":200228332
       }
	 * @throws TzException
	 */
	public static String sendTemplateMessage(String access_token, String opendId, String template_id, List<Map<String,String>>dataList, String appid, String pagepath) throws TzException{
		String returnStr = "";
		try{
			if(dataList == null || dataList.size() <=0){
				throw new TzException("模板数据为空");
			}
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("touser", opendId);
			paramsMap.put("template_id", template_id);
			
			Map<String, Object> dataMap = new HashMap<String, Object>();
			for(Map<String,String> dataKeyWordMap : dataList){
				if(dataKeyWordMap.containsKey("key") && dataKeyWordMap.containsKey("value")){
					Map<String, Object> keyWordMap = new HashMap<String, Object>();
					
					String key = dataKeyWordMap.get("key");
					String value = dataKeyWordMap.get("value");
					
					keyWordMap.put("value", value);
					if(dataKeyWordMap.containsKey("color") && !"".equals(dataKeyWordMap.get("color"))){
						keyWordMap.put("color", dataKeyWordMap.get("color"));
					}
					dataMap.put(key, keyWordMap);
				}else{
					throw new TzException("模板数据不正确");
				}
			}
			paramsMap.put("data", dataMap);
			
			
			Map<String, Object> miniprogramMap = new HashMap<String, Object>();
			miniprogramMap.put("appid", appid);
			miniprogramMap.put("pagepath", pagepath);
			
			paramsMap.put("miniprogram", miniprogramMap);

			String sendTmpMessageUrl = tz_sendTmpMessage_url + "?access_token=" + access_token;
			//http请求
			HttpClientService HttpClientService = new HttpClientService(sendTmpMessageUrl,"POST",paramsMap,"UTF-8");
			returnStr = HttpClientService.sendRequest();
		}catch(Exception e){
			throw new TzException("https请求失败："+e.getMessage());
		}
		return returnStr;
	}
	
}
