package com.tranzvision.gd.TZWeChatBundle.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.stereotype.Service;

import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.httpclient.HttpClientService;

@Service
public class TzWeChartOAuth2 {
	
	private static final String getUserinfoUrl = "https://api.weixin.qq.com/sns/userinfo";
	
	private static final String OAuth2Url = "https://open.weixin.qq.com/connect/oauth2/authorize";
	
	private static final String getAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token";
	
	private static final String authTokenUrl = "https://api.weixin.qq.com/sns/auth";
	
	
	
    /**
     * 获取OAuth授权URL
     * @param appid
     * @param url
     * @return
     */
    public String getOAuthCodeUrl(String appid, String url, String state){
    	try {
			url = URLEncoder.encode(url, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    	
    	String oauthurl = OAuth2Url 
    			+ "?appid="+ appid 
    			+"&redirect_uri="+ url 
    			+"&response_type=code&scope=snsapi_userinfo"
    			+"&state="+state
    			+"#wechat_redirect";
    	
    	return oauthurl;
    }
	
    
    
    /**
     * 通过code换取网页授权access_token
     * @param appid
     * @param secret
     * @param code
     * @return
     */
    public Map<String, String> getOAuthAccessTokenByCode(String appid, String secret, String code){
    	Map<String, String> tokenData = new HashMap<String,String>();
    	tokenData.put("errcode", "0");
    	tokenData.put("errmsg", "");
    	
    	String url = getAccessTokenUrl;
    	JacksonUtil jacksonUtil = new JacksonUtil();
    	try {
    		Map<String, Object> paramsMap = new TreeMap<String, Object>();
            paramsMap.put("appid", appid);
			paramsMap.put("secret", secret);
			paramsMap.put("code", code);
			paramsMap.put("grant_type", "authorization_code");
            
			/*通过微信接口获取token信息和有效时间*/
			HttpClientService HttpClientService = new HttpClientService(url,"GET",paramsMap,"UTF-8");
			String strHttpResult = HttpClientService.sendRequest();
			jacksonUtil.json2Map(strHttpResult);
			
			if(jacksonUtil.containsKey("errcode")){
				tokenData.replace("errcode", "1");
				tokenData.replace("errmsg", jacksonUtil.getString("errmsg"));
			}else{
				tokenData.put("access_token", jacksonUtil.getString("access_token"));
				tokenData.put("expires_in", jacksonUtil.getString("expires_in"));
				tokenData.put("refresh_token", jacksonUtil.getString("refresh_token"));
				tokenData.put("openid", jacksonUtil.getString("openid"));
				tokenData.put("scope", jacksonUtil.getString("scope"));
			}
    	}catch (Exception e) {
    		tokenData.replace("errcode", "1");
			tokenData.replace("errmsg", "获取授权access_token失败，"+e.getMessage());
    	}
    	 
    	return tokenData;
    }
    
    
	
    /**
     * 获取用户信息
     * @param accessToken
     * @param openId
     * @return
     */
	public Map<String, String> getUserInfo(String accessToken, String openId) {
        Map<String, String> userData = new HashMap<String,String>();
        userData.put("errcode", "0");
        userData.put("errmsg", "");
    	
        String url = getUserinfoUrl;
        JacksonUtil jacksonUtil = new JacksonUtil();
        try {
            Map<String, Object> paramsMap = new TreeMap<String, Object>();
            paramsMap.put("access_token", accessToken);
			paramsMap.put("openid", openId);
			paramsMap.put("lang", "zh_CN");
            
			/*通过微信接口获取token信息和有效时间*/
			HttpClientService HttpClientService = new HttpClientService(url,"GET",paramsMap,"UTF-8");
			String strHttpResult = HttpClientService.sendRequest();
			jacksonUtil.json2Map(strHttpResult);
            
			if(jacksonUtil.containsKey("errcode")){
				userData.replace("errcode", "1");
				userData.replace("errmsg", jacksonUtil.getString("errmsg"));
			}else{
				userData.put("nickname", jacksonUtil.getString("nickname"));
				userData.put("sex", jacksonUtil.getString("sex"));
				userData.put("province", jacksonUtil.getString("province"));
				userData.put("city", jacksonUtil.getString("city"));
				userData.put("country", jacksonUtil.getString("country"));
				userData.put("headimgurl", jacksonUtil.getString("headimgurl"));
			}
			
        } catch (Exception e) {
        	userData.replace("errcode", "1");
        	userData.replace("errmsg", "拉取用户信息失败，"+e.getMessage());
        }
        return userData;
    }
	
	
	/**
	 * 检验授权凭证（access_token）是否有效
	 * @param accessToken
	 * @param openid
	 * @return
	 */
	public boolean authToken(String accessToken,String openid){  
		boolean bool = false;
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		Map<String,Object> params = new TreeMap<String,Object>();  
	    params.put("access_token",accessToken);  
	    params.put("openid", openid);  
	    
	    HttpClientService HttpClientService = new HttpClientService(authTokenUrl,"GET",params,"UTF-8");
		String strHttpResult = HttpClientService.sendRequest();
		jacksonUtil.json2Map(strHttpResult);
		
		if(jacksonUtil.containsKey("errcode")){
			try {
				if(jacksonUtil.getInt("errcode") == 0){
					bool = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	    
	    return bool;
	}
	
}
