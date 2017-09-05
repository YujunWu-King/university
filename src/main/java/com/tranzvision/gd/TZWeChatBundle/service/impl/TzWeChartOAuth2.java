package com.tranzvision.gd.TZWeChatBundle.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcInsTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcInsT;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZWeChatUserBundle.dao.PsTzWxUserTblMapper;
import com.tranzvision.gd.TZWeChatUserBundle.model.PsTzWxUserTbl;
import com.tranzvision.gd.TZWeChatUserBundle.model.PsTzWxUserTblKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzException;
import com.tranzvision.gd.util.httpclient.HttpClientService;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;


@SuppressWarnings("deprecation")
@Service
public class TzWeChartOAuth2 {
	
	private static final String getUserinfoUrl = "https://api.weixin.qq.com/sns/userinfo";
	
	private static final String OAuth2Url = "https://open.weixin.qq.com/connect/oauth2/authorize";
	
	private static final String getAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token";
	
	private static final String authTokenUrl = "https://api.weixin.qq.com/sns/auth";
	
	private static final String USER_AGENT = "Mozilla/5.0";
	
	@Autowired
	private SqlQuery sqlQuery;
	
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private TZGDObject tzGDObject;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private PsTzWxUserTblMapper psTzWxUserTblMapper;
	
	@Autowired
	private PsTzDcInsTMapper psTzDcInsTMapper;
	
	
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
			System.out.println("通过code换取token返回========>>>>>"+strHttpResult);
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
	
	
	
//	 public static String postJsonData(String url,Map<String,Object> jsonMap, String access_token){
//		 String returnValue = "";
//		 CloseableHttpClient httpClient = null;  
//	     //ResponseHandler<String> responseHandler = new BasicResponseHandler();  
//	     JacksonUtil jacksonUtil = new JacksonUtil();
//	     try{
//	    	 //第一步：创建HttpClient对象  
//	    	 httpClient = HttpClients.createDefault();
//			 //第二步：创建httpPost对象 
//			 HttpPost httpPost = new HttpPost(url);
//	    	 
//			 
//			 String json = jacksonUtil.Map2json(jsonMap);
//			 System.out.println("---->  "+json);
//			 //第三步：给httpPost设置JSON格式的参数  
//	         StringEntity requestEntity = new StringEntity(json,"utf-8");  
//	         requestEntity.setContentEncoding("UTF-8");
//	         requestEntity.setContentType("application/json");
//	         
//	         httpPost.setHeader("User-Agent", USER_AGENT);
//	         httpPost.setHeader("cache-control", "no-cache");
//	         httpPost.setHeader("Content-Type", "application/json");  
//	         httpPost.setHeader("Authorization", "Bearer "+access_token);
//	         httpPost.setEntity(requestEntity);  
//	         
//	         System.out.println(httpPost.toString());
//	         
//	         //创建响应处理器处理服务器响应内容  
//	         ResponseHandler<String> responseHandler = new ResponseHandler<String>(){
//                 public String handleResponse(final HttpResponse response) throws ClientProtocolException,IOException{
//                     int status = response.getStatusLine().getStatusCode();
//                     System.out.println(status);
//                     if (status >= 200 && status < 300){
//                         HttpEntity entity = response.getEntity();
//                         return entity !=null ? EntityUtils.toString(entity) : null;
//                     }else{
//                         throw new ClientProtocolException("Unexpected response status: " + status);
//                     }
//                 }
//             };
//
//	         //第四步：发送HttpPost请求，获取返回值  
//	         returnValue = httpClient.execute(httpPost,responseHandler); 
//	     }catch(Exception e){
//	    	 e.printStackTrace();
//	     }finally {  
//	           try {  
//	               httpClient.close();  
//	           } catch (IOException e) {  
//	               e.printStackTrace();  
//	           }  
//	     }
//	     
//	     return returnValue;  
//	 }
	
	
	

	
	@SuppressWarnings({ "resource" })
	public String postJsonData(String url, Map<String,Object> jsonMap, String access_token) throws TzException{
		 
		 HttpClient client = new DefaultHttpClient();  
         HttpPost post = new HttpPost(url);
         
         post.setHeader("User-Agent", USER_AGENT);
         post.setHeader("cache-control", "no-cache");
         post.setHeader("Content-Type", "application/json");
         post.addHeader("Authorization", "Bearer "+access_token);
         String result = "";
         JacksonUtil jacksonUtil = new JacksonUtil();
         try {
        	 String json = jacksonUtil.Map2json(jsonMap);
        	 System.out.println(json);
        	 
             StringEntity s = new StringEntity(json, "utf-8");
             s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json"));
             post.setEntity(s);
 
             // 发送请求
             HttpResponse httpResponse = client.execute(post);
 
             // 获取响应输入流
             InputStream inStream = httpResponse.getEntity().getContent();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "utf-8"));
             StringBuilder strber = new StringBuilder();
             String line = null;
             while ((line = reader.readLine()) != null)
                 strber.append(line + "\n");
             inStream.close();
 
             result = strber.toString();
             System.out.println(result);
             
             if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                 System.out.println("请求服务器成功，做相应处理");
             } else {
                 System.out.println("请求服务端失败");
             }
         } catch (Exception e) {
             //System.out.println("请求异常");
             throw new TzException("请求异常："+e.getMessage());
         }
 
         return result;
   }
	
	
	/**
	 * 在线调查初始化事件获取微信用户openid
	 * @param request
	 * @param response
	 * @return
	 */
	public String wxOAuthOnSurveyInit(HttpServletRequest request, HttpServletResponse response, String surveyID){
		String openidHtml = "";
		try{
			String appid = "";
			String appsecret = "";
			String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			if(orgId == null || "".equals(orgId)){
				orgId = sqlQuery.queryForObject("select TZ_JG_ID from PS_TZ_DC_WJ_DY_T where TZ_DC_WJ_ID=?", new Object[]{ surveyID }, "String");
			}
			String sql = "select TZ_WX_APPID,TZ_WX_SECRET from PS_TZ_WX_APPSE_TBL where TZ_JG_ID=? and TZ_WX_STATE='Y' limit 1";
			Map<String,Object> wxMap = sqlQuery.queryForMap(sql, new Object[]{ orgId });
			if(wxMap != null){
				appid = wxMap.get("TZ_WX_APPID") == null ? "" : wxMap.get("TZ_WX_APPID").toString();
				appsecret = wxMap.get("TZ_WX_SECRET") == null ? "" : wxMap.get("TZ_WX_SECRET").toString();
			}
			System.out.println("appid:"+appid+",  appsecret:"+appsecret);
			if(!"".equals(appid) && !"".equals(appsecret)){
				String code = request.getParameter("code");
				System.out.println("CODE---------"+code);
				if(code == null || "".equals(code)){
					String url = request.getRequestURL().toString();
					String queryString = request.getQueryString();
					
				    if (queryString != null) {
				    	url += "?"+ queryString;
				    }
				    
				    String OAuthUrl = this.getOAuthCodeUrl(appid, url, "OAuth");
				    try {
						response.sendRedirect(OAuthUrl);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else{
					Map<String,String> accessTokenMap = this.getOAuthAccessTokenByCode(appid, appsecret, code);
					System.out.println("TOKEN_MAP======> "+accessTokenMap.toString());
					if(accessTokenMap != null){
						String errcode = accessTokenMap.get("errcode");
						if("0".equals(errcode)){
							String openid = accessTokenMap.get("openid");
							String accessToken = accessTokenMap.get("access_token");
							
							sql = "select 'Y' from PS_TZ_WX_USER_TBL where TZ_JG_ID=? and TZ_OPEN_ID=? limit 1";
							String userExistsFlg = sqlQuery.queryForObject(sql, new Object[]{ orgId, openid },"String");
							
							if("Y".equals(userExistsFlg)){
							}else{
								Map<String, String> userInfoMap = this.getUserInfo(accessToken, openid);
								errcode = userInfoMap.get("errcode");
								if("0".equals(errcode)){
									String nickname = userInfoMap.get("nickname");
									String headimgurl = userInfoMap.get("headimgurl");
									
									PsTzWxUserTblKey psTzWxUserTblKey = new PsTzWxUserTblKey();
									psTzWxUserTblKey.setTzJgId(orgId);
									psTzWxUserTblKey.setTzWxAppid(appid);
									psTzWxUserTblKey.setTzOpenId(openid);
									PsTzWxUserTbl  psTzWxUserTbl = psTzWxUserTblMapper.selectByPrimaryKey(psTzWxUserTblKey);
									if(psTzWxUserTbl != null){
										psTzWxUserTbl.setTzNickname(nickname);
										psTzWxUserTbl.setTzImageUrl(headimgurl);
										psTzWxUserTblMapper.updateByPrimaryKey(psTzWxUserTbl);
									}else{
										psTzWxUserTbl = new PsTzWxUserTbl();
										psTzWxUserTbl.setTzJgId(orgId);
										psTzWxUserTbl.setTzWxAppid(appid);
										psTzWxUserTbl.setTzOpenId(openid);
										psTzWxUserTbl.setTzNickname(nickname);
										psTzWxUserTbl.setTzImageUrl(headimgurl);
										psTzWxUserTblMapper.insert(psTzWxUserTbl);
									}
								}
							}
							
							openidHtml = tzGDObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_WX_OPENID_INPUT_HTML", openid);
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return openidHtml;
	}
	
	
	
	
	/**
	 * 在线报名提交给salesforce
	 * @param appInsId
	 * @param openId
	 * @param currPageId
	 * @param targetPageId
	 * @param jsonParams
	 */
	public String[] surveySubmitToSalesforce(String strAppInsId, String openId){
		String resultArr[] = new String [2];
		Map<String,Object> submitMap = new HashMap<String,Object>();
		submitMap.put("openid", "");
		submitMap.put("nickname", "");
		submitMap.put("headimgurl", "");
		submitMap.put("appid", "");
		submitMap.put("subflg", "");
		submitMap.put("data", new ArrayList<Map<String,Object>>());
		try{
			JacksonUtil jacksonUtil = new JacksonUtil();
			String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			if(orgId == null || "".equals(orgId)){
				String jgSql = "select TZ_JG_ID from PS_TZ_DC_WJ_DY_T A where exists(select 'X' from PS_TZ_DC_INS_T where TZ_DC_WJ_ID=A.TZ_DC_WJ_ID and TZ_APP_INS_ID=?)";
				orgId = sqlQuery.queryForObject(jgSql, new Object[]{ strAppInsId }, "String");
			}
			
			Long appInsId = Long.parseLong(strAppInsId);
			if(appInsId != null && appInsId > 0){
				
				if(!"".equals(openId)){
					submitMap.replace("openid", openId);
					
					String sql = "select TZ_NICKNAME,TZ_IMAGE_URL,TZ_WX_APPID,TZ_SUBSCRIBE from PS_TZ_WX_USER_TBL where TZ_JG_ID=? and TZ_OPEN_ID=? limit 1";
					Map<String,Object> wxMap = sqlQuery.queryForMap(sql, new Object[]{ orgId, openId });
					
					if(wxMap != null){
						String nickname = wxMap.get("TZ_NICKNAME") == null ? "" : wxMap.get("TZ_NICKNAME").toString();
						String headImgUrl = wxMap.get("TZ_IMAGE_URL") == null ? "" : wxMap.get("TZ_IMAGE_URL").toString();
						String appid = wxMap.get("TZ_WX_APPID") == null ? "" : wxMap.get("TZ_WX_APPID").toString();
						String subflg = wxMap.get("TZ_SUBSCRIBE") == null ? "" : wxMap.get("TZ_SUBSCRIBE").toString();
						
						submitMap.replace("nickname", nickname);
						submitMap.replace("headimgurl", headImgUrl);
						submitMap.replace("appid", appid);
						submitMap.replace("subflg", subflg);
					}
				}
				
				PsTzDcInsT psTzDcInsT = psTzDcInsTMapper.selectByPrimaryKey(appInsId);
				if(psTzDcInsT != null){
					String wjID = psTzDcInsT.getTzDcWjId();
					
					String sql = "select TZ_XXX_BH,TZ_XXX_CCLX,TZ_COM_LMC from PS_TZ_DCWJ_XXXPZ_T where TZ_DC_WJ_ID=? and TZ_XXX_CCLX in('S','L') and TZ_COM_LMC not in('TextExplain') order by TZ_ORDER";
					List<Map<String,Object>> itemsList = sqlQuery.queryForList(sql, new Object[]{ wjID });
					
					if(itemsList != null && itemsList.size() > 0){
						List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
						for(Map<String,Object> itemMap : itemsList){
							String itemId = itemMap.get("TZ_XXX_BH").toString();
							String storeType = itemMap.get("TZ_XXX_CCLX").toString();
							String className = itemMap.get("TZ_COM_LMC").toString();
							String answerSql = "";
							
							Map<String,Object> itemValMap = new HashMap<String,Object>();
							itemValMap.put("itemId", itemId);
							
							switch(storeType){
							case "S":
								String answerS = "";
								//下拉框和量表题，取描述值
								if("ComboBox".equals(className) || "QuantifyQu".equals(className)){
									String optDescSql = "select B.TZ_XXXKXZ_MS from PS_TZ_DC_CC_T A,PS_TZ_DCWJ_XXKXZ_T B where A.TZ_XXX_BH=B.TZ_XXX_BH  and A.TZ_APP_S_TEXT=B.TZ_XXXKXZ_MC and B.TZ_DC_WJ_ID=? and A.TZ_XXX_BH=? and A.TZ_APP_INS_ID=?";
									answerS = sqlQuery.queryForObject(optDescSql, new Object[]{ wjID, itemId, appInsId }, "String");
								}else{
									answerSql = "select TZ_APP_S_TEXT from PS_TZ_DC_CC_T where TZ_APP_INS_ID=? and TZ_XXX_BH=?"; 
									answerS = sqlQuery.queryForObject(answerSql, new Object[]{ appInsId, itemId }, "String");
								}
								itemValMap.put("value", answerS);
								break;
							case "L":
								String answerL = "";
								answerSql = "select TZ_APP_L_TEXT from PS_TZ_DC_CC_T where TZ_APP_INS_ID=? and TZ_XXX_BH=?"; 
								answerL = sqlQuery.queryForObject(answerSql, new Object[]{ appInsId, itemId }, "String");
								itemValMap.put("value", answerL);
								break;
							}
							
							dataList.add(itemValMap);
						}
						
						submitMap.replace("data", dataList);
					}
				}
				System.out.println(jacksonUtil.Map2json(submitMap));
				//提交给salesforce
				
				Map<String,Object> paramsMap = new HashMap<String,Object>(); 
				paramsMap.put("grant_type", "password");
				paramsMap.put("client_id", "3MVG959Nd8JMmavTev8h5e.29zl0I6K_8d4oWM59mfs7O_ijvGI677b4x_dc73EsZup3GpuqWrC5nUrQXDLlg");
				paramsMap.put("client_secret", "2116159123044178337");
				paramsMap.put("username", "weixin@resmed.com.au.test");
				paramsMap.put("password", "abcd-1234");
				HttpClientService HttpClientService = new HttpClientService("https://test.salesforce.com/services/oauth2/token","POST",paramsMap,"UTF-8");
				String strHttpResult = HttpClientService.sendRequest();
				
				System.out.println(strHttpResult);
				
				jacksonUtil.json2Map(strHttpResult);
				if(jacksonUtil.containsKey("access_token")){
					String access_token = jacksonUtil.getString("access_token");
					
					Map<String,Object> paramMap = new HashMap<String,Object>();
					paramMap.put("param", jacksonUtil.Map2json(submitMap));
					
					String url = "https://resmedcn--test.cs57.my.salesforce.com/services/apexrest/VerifyWeiXin/";
					String result = this.postJsonData(url, paramMap, access_token);
					
					System.out.println(result);
					jacksonUtil.json2Map(result);
					if(jacksonUtil.containsKey("responseType")){
						String responseType = jacksonUtil.getString("responseType");
						String message = jacksonUtil.getString("message");
						resultArr[1] = message;
						if("success".equals(responseType)){
							resultArr[0] = "0";
						}else{
							resultArr[0] = "1";
						}
					}else{
						resultArr[0] = "1";
						resultArr[1] = "提交数据到salesforce失败";
					}
				}else{
					resultArr[0] = "1";
					resultArr[1] = "获取access_token失败";
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			resultArr[0] = "1";
			resultArr[1] = e.getMessage();
		}
		
		return resultArr;
	}
	
}
