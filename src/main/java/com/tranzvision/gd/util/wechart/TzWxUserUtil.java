package com.tranzvision.gd.util.wechart;

import java.util.HashMap;
import java.util.Map;

import com.tranzvision.gd.util.base.TzException;
import com.tranzvision.gd.util.httpclient.HttpClientService;

/**
 * 微信用户相关操作API
 * @author zhanglang
 *
 */
public class TzWxUserUtil {
	
	//获取微信公众号用户列表get请求url
	private static final String tz_getUserList_url = "https://api.weixin.qq.com/cgi-bin/user/get";
	
	//获取用户基本信息
	private static final String tz_getUserInfo_url = "https://api.weixin.qq.com/cgi-bin/user/info";
	
	
	/**
	 * 获取用户列表
	 * @param access_token
	 * @param nextOpenid
	 * @return
	 * @throws TzException 
	 * 参数说明
	 *		access_token	调用接口凭证
	 *		next_openid		第一个拉取的OPENID，不填默认从头开始拉取
	 */
	public static String getUserList(String access_token, String nextOpenid) throws TzException {
		String returnStr = "";
		try{
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("access_token", access_token);
			//nextOpenid不填，默认从头开始拉取
			if(nextOpenid != null && !"".equals(nextOpenid)){
				paramsMap.put("next_openid", nextOpenid);
			}
			
			//http请求
			HttpClientService HttpClientService = new HttpClientService(tz_getUserList_url,"GET",paramsMap,"UTF-8");
			returnStr = HttpClientService.sendRequest();
		}catch(Exception e){
			throw new TzException("https请求失败："+e.getMessage());
		}
		return returnStr;
	}
	
	
	
	/**
	 * 获取用户信息
	 * @param access_token
	 * @param openid
	 * @param lang
	 * @return
	 * @throws TzException 
	 * 参数说明
	 *		access_token	调用接口凭证
	 *		openid	普通用户的标识，对当前公众号唯一
	 *		lang	返回国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语
	 */
	public static String getUserInfo(String access_token, String openid, String lang) throws TzException{
		String returnStr = "";
		try{
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("access_token", access_token);
			paramsMap.put("openid", openid);
			
			//如果语言没有填写，默认返回简体
			if(lang == null || "".equals(lang)){
				lang = "zh_CN";
			}
			paramsMap.put("lang", lang);
			
			
			//http请求
			HttpClientService HttpClientService = new HttpClientService(tz_getUserInfo_url,"GET",paramsMap,"UTF-8");
			returnStr = HttpClientService.sendRequest();
		}catch(Exception e){
			throw new TzException("https请求失败："+e.getMessage());
		}
		return returnStr;
	}
}
