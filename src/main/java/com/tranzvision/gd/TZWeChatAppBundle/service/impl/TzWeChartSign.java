package com.tranzvision.gd.TZWeChatAppBundle.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZWeChatBundle.dao.PsTzWxGzhcsTMapper;
import com.tranzvision.gd.TZWeChatBundle.model.PsTzWxGzhcsT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.httpclient.HttpClientService;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;  

/**
 * 微信公众号开发相关
 * @author zhanglang
 *
 */
@Service
public class TzWeChartSign {
    
	@Autowired
	private SqlQuery sqlQuery;
	
	@Autowired
	private TZGDObject tzGdObject;
	
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	
	@Autowired
	private GetHardCodePoint getHardCodePoint;
	
	@Autowired
	private PsTzWxGzhcsTMapper psTzWxGzhcsTMapper;
	
	//企业号获取access_token URL
	private final String qy_gettoken_url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken";
	//企业号获取jsapi_ticket URL
	private final String qy_getticket_url = "https://qyapi.weixin.qq.com/cgi-bin/get_jsapi_ticket";
	//公众号获取access_token URL
	private final String gz_gettoken_url = "https://api.weixin.qq.com/cgi-bin/token";
	//公众号获取jsapi_ticket URL
	private final String gz_getticket_url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";
	//获取opnid
	private final String gz_getopenid_url = "https://api.weixin.qq.com/sns/oauth2/access_token";
	//拉取用户信息(需scope为 snsapi_userinfo)
	private final String gz_getuserinfo_url = "https://api.weixin.qq.com/sns/userinfo";
	//微信小程序通过code 换取 session_key
	private final String xcx_getSessionKey_url = "https://api.weixin.qq.com/sns/jscode2session";

	/**
	 * 获取微信公众号的全局唯一票据access_token
	 * @param appId		企业Id / 公众号appId
	 * @param secret	企业号管理组的凭证密钥 / 公众号应用密钥	
	 * @param wxType	QY：企业号，GZ：公众号
	 * @return
	 */
	public String getAccessToken(String appId,String secret ,String wxType){
    	String strAccessToken = "";
    	String strExpires_in = "";

    	Date getTokenTime;
    	String getStrTokenTime = "";
    	String sqlGetWxInfo;
    	boolean getWxTokenFlag = false;
		try {
			sqlGetWxInfo = tzGdObject.getSQLText("SQL.TZWeChatBundle.TzGetWxGzhCsInfo");
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap = sqlQuery.queryForMap(sqlGetWxInfo, new Object[] { appId,secret });
			if (dataMap != null) {
				String dtFormat = getSysHardCodeVal.getDateTimeFormat();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dtFormat);
				strAccessToken = dataMap.get("TZ_ACCESS_TOKEN") == null ? "" : String.valueOf(dataMap.get("TZ_ACCESS_TOKEN"));
				strExpires_in = dataMap.get("TZ_TOKEN_YXQ") == null ? "" : String.valueOf(dataMap.get("TZ_TOKEN_YXQ"));
				getStrTokenTime = dataMap.get("TZ_UPDATE_DTTM") == null ? "" : String.valueOf(dataMap.get("TZ_UPDATE_DTTM"));
				if(!"".equals(getStrTokenTime) && !"".equals(strAccessToken)){
					getTokenTime = simpleDateFormat.parse(getStrTokenTime);
					getTokenTime = new Date(getTokenTime.getTime() +  Integer.parseInt(strExpires_in) * 1000);
					Date dateNow = new Date();
					if (getTokenTime.getTime() >= dateNow.getTime()) {
						/*直接返回token*/
					}else{
						/*通过微信接口获取token*/
						getWxTokenFlag = true;
					}
				}else{
					/*通过微信接口获取token*/
					getWxTokenFlag = true;
				}
				
			}else{
				/*通过微信接口获取token*/
				getWxTokenFlag = true;
			}
			if(getWxTokenFlag){
				/*通过微信接口获取token*/
				String getTokenUrl;
				Map<String, Object> paramsMap = new HashMap<String, Object>();
				if("QY".equals(wxType)){
					 /*企业号*/
					try{
						getTokenUrl = getHardCodePoint.getHardCodePointVal("TZ_WXQYH_GETTOKEN_URL");
					}catch(Exception e){
						e.printStackTrace();
						getTokenUrl = "";
					}
					if("".equals(getTokenUrl) || getTokenUrl == null){
						getTokenUrl = qy_gettoken_url;
					}
					
					paramsMap.put("corpid", appId);
					paramsMap.put("corpsecret", secret);
				}else{
					 /*公众号*/
					try{
						getTokenUrl = getHardCodePoint.getHardCodePointVal("TZ_WXGZH_GETTOKEN_URL");
					}catch(Exception e){
						e.printStackTrace();
						getTokenUrl = "";
					}
					if("".equals(getTokenUrl) || getTokenUrl == null){
						getTokenUrl = gz_gettoken_url;
					}
					
					System.out.println("=========url========"+getTokenUrl);
					paramsMap.put("grant_type", "client_credential");
					paramsMap.put("appid", appId);
					paramsMap.put("secret", secret);
				}
				
				/*通过微信接口获取token信息和有效时间*/
				HttpClientService HttpClientService = new HttpClientService(getTokenUrl,"GET",paramsMap,"UTF-8");
				String strHttpResult = HttpClientService.sendRequest();
				JacksonUtil jacksonUtil = new JacksonUtil();
				jacksonUtil.json2Map(strHttpResult);
				System.out.println("====getToken==="+strHttpResult);
				strAccessToken = jacksonUtil.getString("access_token").trim();
				strExpires_in = jacksonUtil.getString("expires_in").trim();
				/*插入数据或者更新微信参数信息表*/
				PsTzWxGzhcsT psTzWxGzhcsT = new PsTzWxGzhcsT();
				psTzWxGzhcsT.setTzWxAppid(appId);
				psTzWxGzhcsT.setTzAppsecret(secret);
				psTzWxGzhcsT.setTzAccessToken(strAccessToken);
				psTzWxGzhcsT.setTzUpdateDttm(new Date());
				psTzWxGzhcsT.setTzTokenYxq(Integer.parseInt(strExpires_in));
				
				if (dataMap != null) {
					psTzWxGzhcsTMapper.updateByPrimaryKeySelective(psTzWxGzhcsT);
				}else{
					psTzWxGzhcsTMapper.insertSelective(psTzWxGzhcsT);
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	System.out.println("accessToken====="+strAccessToken);
    	return strAccessToken;
    }
    

	/**
	 * 获取微信JS接口的临时票据jsapi_ticket
	 * @param appId		企业Id / 公众号appId
	 * @param secret	企业号管理组的凭证密钥 / 公众号应用密钥	
	 * @param wxType	QY：企业号，GZ：公众号
	 * @return jsapi_ticket
	 */
	private String getWxJsapiTicket(String appId,String secret ,String wxType){
    	String strAccessToken = "";
    	String strJsapiTicket = "";
    	String strExpires_in = "";

    	Date getTicketTime;
    	String strGetTicketTime = "";
    	String sqlGetWxInfo;
    	boolean getWxTicketFlag = false;
		try {
			sqlGetWxInfo = tzGdObject.getSQLText("SQL.TZWeChatBundle.TzGetWxGzhCsInfo");
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap = sqlQuery.queryForMap(sqlGetWxInfo, new Object[] { appId,secret });
			if (dataMap != null) {
				String dtFormat = getSysHardCodeVal.getDateTimeFormat();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dtFormat);
				strJsapiTicket = dataMap.get("TZ_JSAPI_TICKET") == null ? "" : String.valueOf(dataMap.get("TZ_JSAPI_TICKET"));
				strExpires_in = dataMap.get("TZ_TICKET_YXQ") == null ? "" : String.valueOf(dataMap.get("TZ_TICKET_YXQ"));
				strGetTicketTime = dataMap.get("TZ_UPDATE_DTTM2") == null ? "" : String.valueOf(dataMap.get("TZ_UPDATE_DTTM2"));
				if(!"".equals(strJsapiTicket) && !"".equals(strGetTicketTime))
				{
					getTicketTime = simpleDateFormat.parse(strGetTicketTime);
					getTicketTime = new Date((getTicketTime.getTime() +  Integer.parseInt(strExpires_in) * 1000));
					Date dateNow = new Date();
					if (getTicketTime.getTime() >= dateNow.getTime()) {
						/*直接返回ticket*/
					}else{
						/*通过微信接口获取ticket*/
						getWxTicketFlag = true;
					}
				}else{
					/*通过微信接口获取ticket*/
					getWxTicketFlag = true;
				}
				
			}else{
				/*通过微信接口获取token*/
				getWxTicketFlag = true;
			}
			if(getWxTicketFlag){
				/*通过微信接口获取ticket*/
				strAccessToken = this.getAccessToken(appId, secret, wxType);
				String getTicketUrl;
				Map<String, Object> paramsMap = new HashMap<String, Object>();
				if("QY".equals(wxType)){
					 /*企业号*/
					try{
						getTicketUrl = getHardCodePoint.getHardCodePointVal("TZ_WXQYH_JSAPI_TICKET_URL");
					}catch(Exception e){
						e.printStackTrace();
						getTicketUrl = "";
					}
					if("".equals(getTicketUrl) || getTicketUrl == null){
						getTicketUrl = qy_getticket_url;
					}
					
					paramsMap.put("access_token", strAccessToken);
				}else{
					 /*公众号*/
					try{
						getTicketUrl = getHardCodePoint.getHardCodePointVal("TZ_WXGZH_JSAPI_TICKET_URL");
					}catch(Exception e){
						e.printStackTrace();
						getTicketUrl = "";
					}
					if("".equals(getTicketUrl) || getTicketUrl == null){
						getTicketUrl = gz_getticket_url;
					}
					
					paramsMap.put("access_token", strAccessToken);
					paramsMap.put("type", "jsapi");
				}
				
				/*通过微信接口获取token信息和有效时间*/
				HttpClientService HttpClientService = new HttpClientService(getTicketUrl,"GET",paramsMap,"UTF-8");
				String strHttpResult = HttpClientService.sendRequest();
				JacksonUtil jacksonUtil = new JacksonUtil();
				jacksonUtil.json2Map(strHttpResult);
				String strErrorCode = "";
				strErrorCode = jacksonUtil.getString("errcode");
				if("0".equals(strErrorCode)){
					strJsapiTicket = jacksonUtil.getString("ticket").trim();
					strExpires_in = jacksonUtil.getString("expires_in").trim();
					/*插入数据或者更新微信参数信息表*/
					PsTzWxGzhcsT psTzWxGzhcsT = new PsTzWxGzhcsT();
					psTzWxGzhcsT.setTzWxAppid(appId);
					psTzWxGzhcsT.setTzAppsecret(secret);
					psTzWxGzhcsT.setTzJsapiTicket(strJsapiTicket);
					psTzWxGzhcsT.setTzUpdateDttm2(new Date());
					psTzWxGzhcsT.setTzTicketYxq(Integer.parseInt(strExpires_in));
					
					int ret = psTzWxGzhcsTMapper.updateByPrimaryKeySelective(psTzWxGzhcsT);
					if (ret == 0) {
						psTzWxGzhcsTMapper.insertSelective(psTzWxGzhcsT);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return strJsapiTicket;
    }


	
	/**
     * 获取微信公众号JS-SDK使用权限签名
     * 签名生成规则如下：参与签名的字段包括noncestr（随机字符串）, 有效的jsapi_ticket, timestamp（时间戳）, url（当前网页的URL，
     * 		不包含#及其后面部分） 。对所有待签名参数按照字段名的ASCII 码从小到大排序（字典序）后，使用URL键值对的格式（即key1=value1&key2=value2…）
     * 		拼接成字符串string1。这里需要注意的是所有参数名均为小写字符。对string1作sha1加密，字段名和字段值都采用原始值，不进行URL 转义。
     * 参数说明：
     * @param appId		企业Id / 公众号appId
	 * @param secret	企业号管理组的凭证密钥 / 公众号应用密钥	
	 * @param wxType	QY：企业号，GZ：公众号
	 * @param url		当前网页的URL，不包含#及其后面部分
	 * @return
     */
    public Map<String, String> sign(String appId,String secret ,String wxType, String url) {
    	
    	String jsapi_ticket = "";
    	jsapi_ticket = this.getWxJsapiTicket(appId, secret, wxType);
    	
        Map<String, String> ret = new HashMap<String, String>();
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String string1;
        String signature = "";

        //注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsapi_ticket +
                  "&noncestr=" + nonce_str +
                  "&timestamp=" + timestamp +
                  "&url=" + url;
        try{
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }

        ret.put("url", url);
        ret.put("jsapi_ticket", jsapi_ticket);
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);

        return ret;
    }
    
    /**
     * 获取微信公众号JS-SDK使用权限签名
     * 签名生成规则如下：参与签名的字段包括noncestr（随机字符串）, 有效的jsapi_ticket, timestamp（时间戳）, url（当前网页的URL，
     * 		不包含#及其后面部分） 。对所有待签名参数按照字段名的ASCII 码从小到大排序（字典序）后，使用URL键值对的格式（即key1=value1&key2=value2…）
     * 		拼接成字符串string1。这里需要注意的是所有参数名均为小写字符。对string1作sha1加密，字段名和字段值都采用原始值，不进行URL 转义。
     * 参数说明：
     * @param jsapi_ticket 公众号用于调用微信JS接口的临时票据
     * @param url 当前网页的URL，不包含#及其后面部分
     * @return
     */
    public Map<String, String> sign(String jsapi_ticket, String url) {
        Map<String, String> ret = new HashMap<String, String>();
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String string1;
        String signature = "";

        //注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsapi_ticket +
                  "&noncestr=" + nonce_str +
                  "&timestamp=" + timestamp +
                  "&url=" + url;
        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }

        ret.put("url", url);
        ret.put("jsapi_ticket", jsapi_ticket);
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);

        return ret;
    }
    
    
    /**
     * 重新获取Code的跳转链接
     * @param appid
     * @param url
     * @return
     */
    public String getOAuthCodeUrl(String appid,String url){
    	
    	try {
			url = URLEncoder.encode(url, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	String codeUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+ appid 
    			+"&redirect_uri="+ url 
    			+"&response_type=code&scope=snsapi_base&state=1#wechat_redirect";
    	
    	return codeUrl;
    }
    
    
    /**
     * 获取微信公众号openid 和 accessToken edit by liujing
     * @param appid
     * @param secret
     * @param code
     * @return
     */
    public  Map<String, Object> getOauthAccessOpenId(String appid, String secret, String code){
    	
    	String Openid = "";
    	String access_token = "";
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("appid", appid);
        paramsMap.put("secret", secret);
        paramsMap.put("code", code);
        paramsMap.put("grant_type", "authorization_code");
        
        HttpClientService HttpClientService = new HttpClientService(gz_getopenid_url,"GET",paramsMap,"UTF-8");
		String strHttpResult = HttpClientService.sendRequest();
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strHttpResult);
		System.out.println("getopenid===================="+strHttpResult);
		
		if(jacksonUtil.containsKey("openid")){
			Openid = jacksonUtil.getString("openid").trim();
			paramsMap.put("openid", Openid);
		}
		if(jacksonUtil.containsKey("access_token")){
			access_token = jacksonUtil.getString("access_token").trim();
			paramsMap.put("access_token", access_token);
		}
        
        return paramsMap;
    }
    
    /**
     * 获取微信个人信息  
     * @author liujing 
     * @param appid
     * @param secret
     * @param code
     * @return
     */
    public Map<String, Object> getOauthAccessInfo(Map<String, Object> map){
    	
    	String nickname="";
    	map.put("lang", "zh_CN");//返回国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语
    	HttpClientService HttpClientService = new HttpClientService(gz_getuserinfo_url,"GET",map,"UTF-8");
    	String strHttpResult = HttpClientService.sendRequest();
    	
    	JacksonUtil jacksonUtil = new JacksonUtil();
    	jacksonUtil.json2Map(strHttpResult);
    	Map<String, Object> infoMap = new HashMap<String, Object>();
    	if(jacksonUtil.containsKey("nickname")){//取昵称
    		nickname = jacksonUtil.getString("nickname").trim();
    		infoMap.put("nickname", nickname);
    	}
    	return infoMap;
    }
    
    
    /**
     * 获取微信小程序 openid 和 session_key edit by liujing
     * @param appid
     * @param secret
     * @param code
     * @return
     */
    public  Map<String, Object> getWxXCXOpenId(String appid, String secret, String jscode){
    	
    	String Openid = "";
    	String session_key = "";
    	String unionid = "";
    	String errcode = "";
    	String errmsg = "";
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("appid", appid);
        paramsMap.put("secret", secret);
        paramsMap.put("js_code", jscode);
        paramsMap.put("grant_type", "authorization_code");
        
        HttpClientService HttpClientService = new HttpClientService(xcx_getSessionKey_url,"GET",paramsMap,"UTF-8");
		String strHttpResult = HttpClientService.sendRequest();
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strHttpResult);
		Map<String, Object> map = new HashMap<String, Object>();
		if(jacksonUtil.containsKey("openid")){
			Openid = jacksonUtil.getString("openid").trim();
			map.put("openid", Openid);
		}
		if(jacksonUtil.containsKey("session_key")){
			session_key = jacksonUtil.getString("session_key").trim();
			map.put("session_key", session_key);
		}
		if(jacksonUtil.containsKey("unionid")){
			unionid = jacksonUtil.getString("unionid").trim();
			map.put("unionid", unionid);
		}
		if(jacksonUtil.containsKey("errcode")){
			errcode = jacksonUtil.getString("errcode").trim();
			map.put("errcode", errcode);
		}
		if(jacksonUtil.containsKey("errmsg")){
			unionid = jacksonUtil.getString("errmsg").trim();
			map.put("errmsg", errmsg);
		}
        return map;
    }
    

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
}
