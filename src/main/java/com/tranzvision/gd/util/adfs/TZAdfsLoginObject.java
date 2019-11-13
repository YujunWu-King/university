/*
 * 用于集成登录ADFS的Java类
 * 用于访问Dynamics CRM系统WebAPI接口时获取访问令牌
 */
package com.tranzvision.gd.util.adfs;


import com.tranzvision.gd.util.base.JacksonUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TZAdfsLoginObject
{
	//用于同步锁的对象
	static private Object lockObject = new Object();
	//同步放行标志
	static private boolean lockingFlag = false;
	
	//记录登录是否成功的标志
	static private boolean loginSuccessFlag = false;
	//记录登录令牌失效时间的变量
	static private Date tokenExpiredDatetime = new Date(0);
	
	//记录SSO登录令牌的变量
	static private String ssoTokenCookieValue = "";
	//记录SSO认证域名的变量
	static private String ssoAuthDomainName = "";
	
	//记录当前应用系统登录令牌的变量
	static private String appTokenCookieValue = "";
	static private String appTokenDomainName = "";
	
	//用于记录请求ID
	static private String requestClientId = "";
	
	//记录错误信息的变量
	private String errorMessage = "";
	
	//记录Dynamics CRM登录信息的变量
	private String appWebsiteURL = "";
	private String appLoginUserName = "";
	private String appLoginUserPassword = "";
	private int appTokenExpiredSeconds = -1;
	
	//临时记录WebAPI调用结果的变量
	private String appWebAPIResult = "";
	//记录WebAPI请求成功
	private boolean isRequestSuccess = true;
	
	//用户类型
	private String httpMethod = ""; 
	
	
	//记录日志
	private static final Logger logger = Logger.getLogger("DynamicWebCall");
	
	
	
	//实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法 
	private static TrustManager myX509TrustManager = new X509TrustManager()
  {
	  @Override
	  public X509Certificate[] getAcceptedIssuers()
	  {
		  return null;
	  }
	  
	  @Override
	  public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException
	  {
	  }
	  
	  @Override
	  public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException
	  {
	  }
  };
	
	//构造方法
	public TZAdfsLoginObject()
	{
		//初始化Dynamics CRM系统登录参数
		appWebsiteURL = "";
		appLoginUserName = "";
		appLoginUserPassword = "";
		appTokenExpiredSeconds = -1;
	
		//重置登录状态
		resetLoginStatus();
	}
	
	//重置登录状态的方法
	private void resetLoginStatus()
	{
		//重置登录状态变量
		errorMessage = "";
		loginSuccessFlag = false;
		tokenExpiredDatetime = new Date(0);
		ssoTokenCookieValue = "";
		appTokenCookieValue = "";
		ssoAuthDomainName = "";
		appTokenDomainName = "";
	}
	
	/**
	 * 设置请求方式
	 * @param method
	 */
	public void setHttpMethod(String method){
		httpMethod = method;
	}
	
	//获取错误消息的方法
	public String getErrorMessage()
	{
		return errorMessage;
	}
	
	//获取登录令牌
	public String getAppTokenCookieValue() {
		return appTokenCookieValue;
	}
	
	//获取Dynamics CRM系统Web API调用结果的方法
	public String getWebAPIResult()
	{
		return appWebAPIResult;
	}
	
	/**
	 * 获取请求结果
	 * @return
	 */
	public boolean isRequestSuccess() {
		return isRequestSuccess;
	}
	
	
	
	/**
	 * 获取调用者信息的方法
	 */
	private String getCallerName() {
		String callerName = "";

		StackTraceElement tmpstack[] = Thread.currentThread().getStackTrace();
		boolean findFlag = false;
		for (StackTraceElement stackitem : tmpstack) {
			if ((stackitem.getClassName().indexOf(getClass().getName())) >= 0) {
				findFlag = true;
			} else if (findFlag == true) {
				callerName = stackitem.getClassName() + "." + stackitem.getMethodName() +"["+ stackitem.getLineNumber() + " line]";
				break;
			}
		}

		return callerName;
	}

	
	//将Cookie对象转换成字符串的方法
	@SuppressWarnings("unused")
	private String getCookieString(String cookieName, String cookieValue, String domainName)
	{
		StringBuilder sb = new StringBuilder(); 
		
		
		//根据指定cookie名称、cookie值和域名生成cookie对象
		Cookie tmpCookieObject = new Cookie(cookieName, cookieValue);
		tmpCookieObject.setDomain(domainName);
		tmpCookieObject.setPath("/");
		tmpCookieObject.setSecure(true);
		
		
		sb.append(tmpCookieObject.getName()).append("=").append(tmpCookieObject.getValue());
		
		
		if(tmpCookieObject.getMaxAge() >= 0)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
			sb.append("; expires=").append(sdf.format(new Date(1000 * tmpCookieObject.getMaxAge())));
		}
		else
		{
			sb.append("; max-age=").append("" + tmpCookieObject.getMaxAge());
		}
		
		
		if(tmpCookieObject.getDomain() != null)
		{
			sb.append("; domain=").append(tmpCookieObject.getDomain().trim());
		}
		
		
		if(tmpCookieObject.getPath() != null)
		{
			sb.append("; path=").append(tmpCookieObject.getPath().trim());
		}
		
		
		if(tmpCookieObject.getSecure() == true)
		{
			sb.append("; Secure");
		}
		
		
		return sb.toString();
	}
	
	//打开URL地址获取HTTP连接对象的方法
	private HttpURLConnection openURL(String urlStr, String httpMethod, String charactorSet, boolean autoRedirect) throws Exception
	{
		HttpURLConnection httpConn = null;
		
		
		try
		{
			//检查是否走https协议
			boolean httpsConnection = urlStr.toLowerCase().startsWith("https://");
			
			
			//设置SSLContext
		  SSLContext sslcontext = null;
		  if(httpsConnection == true)
		  {
			  sslcontext = SSLContext.getInstance("TLS");
			  sslcontext.init(null, new TrustManager[]{myX509TrustManager}, null);
		  }
		  
		  
		  //打开连接
		  URL requestUrl = new URL(urlStr);
		  HttpsURLConnection httpsConn = null;
		  if(httpsConnection == true)
		  {
			  httpsConn = (HttpsURLConnection)requestUrl.openConnection();
		  }
		  else
		  {
			  httpConn = (HttpURLConnection)requestUrl.openConnection();
		  }
		  
		  
		  //设置套接工厂 
			if(httpsConnection == true)
			{
				httpsConn.setSSLSocketFactory(sslcontext.getSocketFactory());
			  httpConn = httpsConn;
			}
			
			
			//设置接受的字符集
			httpConn.addRequestProperty("Accept-Charset", charactorSet + ";");
			
			
			//设置HTTP请求方法
            if("PATCH".equalsIgnoreCase(httpMethod)) {
            	httpConn.setRequestMethod("POST");
            	httpConn.setRequestProperty("X-HTTP-Method-Override", "PATCH");
            }else {
            	httpConn.setRequestMethod(httpMethod);
            }
			
			
			//设置是否允许重定向自动跳转
			httpConn.setInstanceFollowRedirects(autoRedirect); 
	  }
	  catch(Exception e)
	  {
	  	throw e;
	  }
	  
	  
	  return httpConn;
	}
	
	
	
	/**
	 * 从指定HTTP连接对象获取服务器响应内容
	 * @param httpConn
	 * @param setReqResult	设置请求结果
	 * @return
	 * @throws Exception
	 */
	private String getResponseContent(HttpURLConnection httpConn, boolean setReqResult) throws Exception {
		String responseText = "";

		// 从指定对象httpConn中获取服务器响应内容
		try {
			// 根据指定的URL地址发起连接请求
			httpConn.connect();

			// 检查服务器返回的代码
			int code = httpConn.getResponseCode();
			System.out.println("code0000" + code);
			// 检查是否返回500错误
			if (HttpsURLConnection.HTTP_INTERNAL_ERROR == code) {
				throw new Exception(
						"The server returns an error code: 500, and please check if the parameter format is correct.");
			}
			// 检查服务器是否返回错误
			/*if (HttpsURLConnection.HTTP_OK != code && HttpsURLConnection.HTTP_NO_CONTENT != code && HttpsURLConnection.HTTP_BAD_REQUEST != code) {
				throw new Exception("the server returns an error code: " + code);
			}*/

			if (HttpsURLConnection.HTTP_OK == code || HttpsURLConnection.HTTP_CREATED == code) {
				// 获取输入流
				BufferedReader in = new BufferedReader(
						new InputStreamReader(httpConn.getInputStream(), Charset.forName("UTF-8")));

				String temp = in.readLine();
				StringBuilder sb = new StringBuilder();
				// 连接成一个字符串
				while (temp != null) {
					sb.append(temp);

					temp = in.readLine();
				}

				// 关闭流
				in.close();

				// 取出最终获取的响应内容
				responseText = sb.toString();
				
			} else if (HttpsURLConnection.HTTP_BAD_REQUEST == code) {
				//400 Bad Request
				if(setReqResult) {
					isRequestSuccess = false;
				}
				
				// 获取输入流
				BufferedReader in = new BufferedReader(
						new InputStreamReader(httpConn.getErrorStream(), Charset.forName("UTF-8")));

				String temp = in.readLine();
				StringBuilder sb = new StringBuilder();
				// 连接成一个字符串
				while (temp != null) {
					sb.append(temp);

					temp = in.readLine();
				}

				// 关闭流
				in.close();

				// 取出最终获取的响应内容
				responseText = sb.toString();
				logger.error("【400 Bad Request】: "+ responseText);
			}else if(HttpsURLConnection.HTTP_NO_CONTENT == code){
            	//无返回内容
            	responseText = "";
            }
			
			//其他的请求错误、服务器错误
			if(code > 400) {
				if(setReqResult) {
					isRequestSuccess = false;
				}
				
				// 获取输入流
				BufferedReader in = new BufferedReader(
						new InputStreamReader(httpConn.getErrorStream(), Charset.forName("UTF-8")));

				String temp = in.readLine();
				StringBuilder sb = new StringBuilder();
				// 连接成一个字符串
				while (temp != null) {
					sb.append(temp);

					temp = in.readLine();
				}

				// 关闭流
				in.close();

				// 取出最终获取的响应内容
				responseText = sb.toString();
				logger.error("【"+ code +" Bad Request】: "+ responseText);
			}
		} catch (Exception e) {
			throw e;
		}

		return responseText;
	}
	
	//从指定HTTP连接对象中获取指定Cookie的方法
	private String getCookieValue(HttpURLConnection httpConn, String cookieName)
	{
		String tmpCookieValue = "";
		
		
		//获取cookie
		if(cookieName != null)
		{
			Map<String,List<String>> map = httpConn.getHeaderFields();
			Set<String> set = map.keySet();
			for (Iterator iterator = set.iterator(); iterator.hasNext();)
			{
				String key = (String) iterator.next();
				if ("Set-Cookie".equals(key) == true)
				{
				  List<String> list = map.get(key);
				  
				  StringBuilder builder = new StringBuilder();
				  for (String str : list)
				  {
				    if(str != null && str.toLowerCase().replaceAll("\\s+","").startsWith(cookieName.toLowerCase() + "=") == true)
				    {
				    	int index = str.indexOf(";");
				    	
				    	tmpCookieValue = index >= 0 ? str.substring(cookieName.length() + 1, index) : str.substring(cookieName.length() + 1);
				    	
				    	break;
				    }
				  }
				  
				  break;
				}
			}
		}
		
		
		return tmpCookieValue;
	}
	
	//构建表单数据
	private String buildLoginFormData(String userName, String userPswd) throws Exception
	{
		HashMap<String, String> tmpFormKeyValues = new HashMap<String, String>();
		
		
		tmpFormKeyValues.put("UserName", userName);
		tmpFormKeyValues.put("Password", userPswd);
		tmpFormKeyValues.put("Kmsi", "true");
		tmpFormKeyValues.put("AuthMethod", "FormsAuthentication");
		
		
		return buildFormData(tmpFormKeyValues);
	}
	//构建表单数据
	private String buildFormData(HashMap<String, String> keyValues) throws Exception
	{
		StringBuffer params = new StringBuffer();
		
		
		if(keyValues != null)
		{
			for(String tmpkey:keyValues.keySet())
			{
				if(params.length() <= 0)
				{
					params.append(tmpkey).append("=").append(URLEncoder.encode(keyValues.get(tmpkey), "UTF-8"));
				}
				else
				{
					params.append("&").append(tmpkey).append("=").append(URLEncoder.encode(keyValues.get(tmpkey), "UTF-8"));
				}
			}
		}
		
		
		return params.toString();
	}
	
	//获取表单元素属性值的方法
	private String getFormElementAttributeValue(String formData, String tagName, String elementIdOrName, String elementIdOrNameValue, String attributeName) throws Exception
	{
		String tmpAttributeValue = "";
		
		
		String regText = "";
		if("".equals(elementIdOrName) == true || elementIdOrName == null)
		{
			regText  = "<" + tagName;
			regText += "(?:[^<>\"']*(?:(?:\"[^\"]*\")|(?:'[^']*'))[^<>\"']*)*";
			regText += "\\s*" + attributeName + "\\s*=\\s*(?:(?:\"([^\"]*)\")|(?:'([^']*)'))";
			regText += "(?:[^<>\"']*(?:(?:\"[^\"]*\")|(?:'[^']*'))[^<>\"']*)*";
			regText += "\\s*(?:/>|>)";
		}
		else
		{
			regText  = "<" + tagName;
			regText += "(?:[^<>\"']*(?:(?:\"[^\"]*\")|(?:'[^']*'))[^<>\"']*)*";
			regText += "\\s*" + elementIdOrName + "\\s*=\\s*(?:(?:\"(?:" + elementIdOrNameValue + ")\")|(?:'(?:" + elementIdOrNameValue + ")'))";
			regText += "(?:[^<>\"']*(?:(?:\"[^\"]*\")|(?:'[^']*'))[^<>\"']*)*";
			regText += "\\s*" + attributeName + "\\s*=\\s*(?:(?:\"([^\"]*)\")|(?:'([^']*)'))";
			regText += "(?:[^<>\"']*(?:(?:\"[^\"]*\")|(?:'[^']*'))[^<>\"']*)*";
			regText += "\\s*(?:/>|>)";
		}
		
		
		Matcher m = Pattern.compile(regText).matcher(formData);
		if(m.find() == true)
		{
			tmpAttributeValue = StringEscapeUtils.unescapeHtml4(m.group(1));
		}
		
		
		return tmpAttributeValue;
	}
	
	
	//通过ADFS登录Dynamics CRM系统的方法
	public boolean loginDynamicsCRMByADFS()
	{
		//重置登录状态
		resetLoginStatus();
		
		
		//读取Dynamics CRM系统登录参数
		String dycrmURL = appWebsiteURL;
		String userName = appLoginUserName;
		String userPsswd = appLoginUserPassword;
		int tokenExpiredSeconds = appTokenExpiredSeconds;
		
		
		//检查参数是否满足要求
		if(dycrmURL == null || "".equals(dycrmURL.trim()) == true)
		{
			errorMessage = "Invalid URL address to access Dynamics CRM system.";
			
			return false;
		}
		if(userName == null || "".equals(userName.trim()) == true)
		{
			errorMessage = "Invalid user name to access Dynamics CRM system.";
			
			return false;
		}
		if(userPsswd == null || "".equals(userPsswd.trim()) == true)
		{
			errorMessage = "Invalid user password to access Dynamics CRM system.";
			
			return false;
		}
		
		
		//设置令牌失效时长
		tokenExpiredSeconds = tokenExpiredSeconds <= 0 ? 28800 : tokenExpiredSeconds;
		
		
		//准备HTTP连接
		try
		{
			//记录Dynamics CRM系统域名
		  String tmpDynamicsCRMDomainName = (new URL(dycrmURL)).getAuthority();
		  int index0 = tmpDynamicsCRMDomainName.indexOf(":");
		  tmpDynamicsCRMDomainName = index0 >= 0 ? tmpDynamicsCRMDomainName.substring(0,index0) : tmpDynamicsCRMDomainName;
		  
		  
			//打开URL地址生成HTTP URL连接对象
			HttpURLConnection httpConn = openURL(dycrmURL, "GET", "UTF-8", false);
		  //根据指定的URL地址发起连接请求
		  httpConn.connect();
		  //获取重定向地址
		  String tmpRedirectURL = httpConn.getHeaderField("Location");
		  
		  
		  //检查获取重定向地址是否成功
		  if(tmpRedirectURL == null || "".equals(tmpRedirectURL.trim()) == true)
		  {
			  errorMessage = "Failed to acquired the redirect URL for accessing adfs system.";
			  
			  return false;
		  }
		  
		  
		  //获取ADFS登陆URL地址中的前半部分
		  int index = tmpRedirectURL.indexOf("/",8);
		  String tmpURL = "";
		  if(index >= 0)
		  {
		  	tmpURL = tmpRedirectURL.substring(0, index);
		  }
		  else
		  {
		  	errorMessage = "The obtained redirect URL is invalid.";
			  
			  return false;
		  }
		  
		  
		  //根据重定向URL地址访问ADFS获取登录页内容
		  httpConn.disconnect();
		  httpConn = openURL(tmpRedirectURL, "GET", "UTF-8", false);
		  
		  
		  //获取服务器端响应内容
		  String tmpResponseText = getResponseContent(httpConn, false);
		  
		  
		  //获取提交登录表单数据URL地址
		  String tmpActionURL = tmpURL + getFormElementAttributeValue(tmpResponseText, "form", "id", "loginForm", "action");;
		  
		  
		  //检查提交登录表单数据URL地址是否获取成功
		  if(tmpActionURL == null || "".equals(tmpActionURL.trim()) == true)
		  {
			  errorMessage = "Failed to acquired the form data submit URL for logining adfs system.";
			  
			  return false;
		  }
		  
		  
		  //使用指定用户和密码登录目标系统
		  httpConn.disconnect();
		  httpConn = openURL(tmpActionURL, "POST", "UTF-8", false);
		  //构建ADFS登录表单数据
		  String loginFormData = buildLoginFormData(userName, userPsswd);
		  //提交ADFS登录表单数据到HTTP连接对象
		  httpConn.setDoOutput(true);
		  httpConn.getOutputStream().write(loginFormData.getBytes(Charset.forName("UTF-8")));
		  
		  
		  //向指定的登录URL地址发起连接请求并提交登录数据
		  httpConn.connect();
		  
		  
		  //检查服务器返回的代码
		  int code = httpConn.getResponseCode();
		  if(HttpsURLConnection.HTTP_MOVED_TEMP != code)
		  {
			  throw new Exception("the server returns an error code: " + code);
		  }
		  
		  
		  //登录ADFS获取访问ADFS的令牌
		  String tmpSSOCookieValue = getCookieValue(httpConn, "MSISAuth");
		  
		  
		  //记录SSO认证域名
		  String tmpSSOAuthDomainName = (new URL(tmpRedirectURL)).getAuthority();
		  int index1 = tmpSSOAuthDomainName.indexOf(".");
		  int index2 = tmpSSOAuthDomainName.indexOf(":");
		  index2 = index2 >= 0 ? index2 : tmpSSOAuthDomainName.length();
		  tmpSSOAuthDomainName = (index1 >= 0 && index2 >= 0) ? tmpSSOAuthDomainName.substring(index1, index2) : tmpSSOAuthDomainName;
		  
		  
		  //通过SSO令牌访问ADFS获取登录应用的表单数据
		  httpConn.disconnect();
		  httpConn = openURL(tmpRedirectURL, "GET", "UTF-8", false);
		  
		  //设置SSO认证Cookie
		  httpConn.setRequestProperty("Cookie", "MSISAuth=" + tmpSSOCookieValue);
		  
		  //读取服务器响应内容
		  tmpResponseText = getResponseContent(httpConn, false);
		  //解析表单数据
		  tmpActionURL = getFormElementAttributeValue(tmpResponseText, "form", "name", "hiddenform", "action");
		  String tmpFormFieldWA = getFormElementAttributeValue(tmpResponseText, "input", "name", "wa", "value");
		  String tmpFormFieldWRESULT = getFormElementAttributeValue(tmpResponseText, "input", "name", "wresult", "value");
		  String tmpFormFieldWCTX = getFormElementAttributeValue(tmpResponseText, "input", "name", "wctx", "value");
		  
		  
		  //构建向服务器提交的表单数据
		  HashMap<String, String> tmpFormDataMap = new HashMap<String, String>();
		  tmpFormDataMap.put("wa", tmpFormFieldWA);
		  tmpFormDataMap.put("wresult", tmpFormFieldWRESULT);
		  tmpFormDataMap.put("wctx", tmpFormFieldWCTX);
		  loginFormData = buildFormData(tmpFormDataMap);
		  
		  //SSO登录Dynamics CRM应用
		  httpConn.disconnect();
		  httpConn = openURL(tmpActionURL, "POST", "UTF-8", false);
		  
		  //提交登录表单数据到HTTP连接对象
		  httpConn.setDoOutput(true);
		  httpConn.getOutputStream().write(loginFormData.getBytes(Charset.forName("UTF-8")));
		  
		  
		  //向指定的登录URL地址发起连接请求并提交登录数据
		  httpConn.connect();
		  
		  
		  //检查服务器返回的代码
		  code = httpConn.getResponseCode();
		  if(HttpsURLConnection.HTTP_MOVED_TEMP != code)
		  {
			  throw new Exception("the server returns an error code: " + code);
		  }
		  
		  
		  //读取应用服务器登录令牌
		  String tmpAppCookieValue1 = getCookieValue(httpConn, "MSISAuth");
		  String tmpAppCookieValue2 = getCookieValue(httpConn, "MSISAuth1");
		  
		  
		  //断开连接
		  httpConn.disconnect();
		  
		  
		  //设置登录结果
		  errorMessage = "Succeed in logining into the specified target application system.";
		  loginSuccessFlag = true;
		  Calendar tmpCalendar = Calendar.getInstance();
		  tmpCalendar.add(Calendar.SECOND, tokenExpiredSeconds);
		  tokenExpiredDatetime = tmpCalendar.getTime();
		  ssoTokenCookieValue = tmpSSOCookieValue;
		  appTokenCookieValue = tmpAppCookieValue1 + tmpAppCookieValue2;
		  ssoAuthDomainName = tmpSSOAuthDomainName;
		  appTokenDomainName = tmpDynamicsCRMDomainName;
		  
		  //设置请求ID
		  requestClientId = UUID.randomUUID().toString();
		}
		catch(Exception e)
	  {
		  //e.printStackTrace();
		  errorMessage = "An exception occurred when try to connection Dynamics CRM system by the specified URL.";
		  logger.error("Error loginDynamicsCRMByADFS: ", e);
		  return false;
	  }
			
		return true;
	}
	
	// 通过ADFS登录Dynamics CRM系统的方法
	public boolean isValidSession(String dynamicsUrl, String MSISAuthValue) {
		// 打开URL地址生成HTTP URL连接对象
		boolean isValid = false;
		try {
			
			HttpURLConnection httpConn = openURL(dynamicsUrl, "GET", "UTF-8", false);
			httpConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.154 Safari/537.36");	
			httpConn.setDoOutput(true);	
			httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");		
			httpConn.setRequestProperty("Content-Language", "zh-cn");	
			httpConn.setRequestProperty("Connection", "keep-alive");	
			httpConn.setRequestProperty("Cache-Control", "no-cache");
			//省略了其它部分
			httpConn.setRequestProperty("Cookie",  "MSISAuth=" + MSISAuthValue);	

			// 根据指定的URL地址发起连接请求
			httpConn.connect();
			InputStream inputStream = httpConn.getInputStream();                
			//获取响应                
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));         
			String strMessage = "";
			String line;                
			while ((line = reader.readLine()) != null){                    
				strMessage = strMessage + line;
			}
			reader.close();                
			//该干的都干完了,记得把连接断了
			httpConn.disconnect();
			
			try{
				Document doc = DocumentHelper.parseText(strMessage); // 将字符串转为XML
				
				Element rootElt = doc.getRootElement(); // 获取根节点notification				
				
				String validSessionData = rootElt.getData().toString();
				JacksonUtil jacksonUtil = new JacksonUtil();
				jacksonUtil.json2Map(validSessionData);
				if(jacksonUtil.containsKey("code")){
					String tmpCode = jacksonUtil.getString("code");
					if("0".equals(tmpCode)){
						isValid = true;
					}
				}				
			}catch(Exception e){				
				/*doNothing*/
			}			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			/*doNothing*/
		}
		return isValid;
	}

	//判断当前登录令牌是否超时的方法
	public boolean isTokenExpired()
	{
		//如果尚未登录Dynamics CRM系统，则以令牌超时论处
		if(loginSuccessFlag == false)
		{
			return true;
		}
		
		
		//检查令牌是否超时
		Date tmpCurrentDatetime = Calendar.getInstance().getTime();
		if(tokenExpiredDatetime.compareTo(tmpCurrentDatetime) <= 0)
		{
			return true;
		}
		
		
		return false;
	}
	
	//设置Dynamics CRM系统登录参数的方法
	public void setDynamicsLoginPrarameters(String dycrmURL, String userName, String userPsswd, int tokenExpiredSeconds)
	{
		//设置Dynamics CRM系统登录参数
		appWebsiteURL = dycrmURL;
		appLoginUserName = userName;
		appLoginUserPassword = userPsswd;
		appTokenExpiredSeconds = tokenExpiredSeconds;
	}
	
	//调用Dynamics CRM系统Web API的方法
	public boolean callDynamicsCRMWebAPI(String webapiURL)
	{
		return callDynamicsCRMWebAPI(webapiURL, null, null, false, false);
	}
	public boolean callDynamicsCRMWebAPIForJSONData(String webapiURL)
	{
		return callDynamicsCRMWebAPI(webapiURL, null, null, false, true);
	}
	public boolean callDynamicsCRMWebAPI(String webapiURL, String postData)
	{
		return callDynamicsCRMWebAPI(webapiURL, postData, null, false, false);
	}
	public boolean callDynamicsCRMWebAPIWithJSONData(String webapiURL, String postData)
	{
		return callDynamicsCRMWebAPI(webapiURL, postData, null, true, false);
	}
	public boolean callDynamicsCRMWebAPIForJSONData(String webapiURL, String postData)
	{
		return callDynamicsCRMWebAPI(webapiURL, postData, null, false, true);
	}
	public boolean callDynamicsCRMWebAPIForJSONDataWithJSONData(String webapiURL, String postData)
	{
		return callDynamicsCRMWebAPI(webapiURL, postData, null, true, true);
	}
	public boolean getAttachmentFromDynamicsCRM(String webapiURL, HttpServletResponse response)
	{
		return callDynamicsCRMWebAPI(webapiURL, null, response, false, false);
	}
	private boolean pGetAttachmentFromDynamicsCRM(HttpURLConnection httpConnection, HttpServletResponse response) throws Exception
	{
		boolean result = true;
		
		
		//根据指定的URL地址发起连接请求
	  httpConnection.connect();
	  
	  
	  //检查服务器返回的代码
	  int code = httpConnection.getResponseCode();
	  
	  //检查是否返回500错误
		if(HttpsURLConnection.HTTP_INTERNAL_ERROR == code)
		{
			throw new Exception("The server returns an error code: 500, and please check if the parameter format is correct.");
		}
		//检查服务器是否返回错误
	  if(HttpsURLConnection.HTTP_OK != code && HttpsURLConnection.HTTP_NO_CONTENT != code)
	  {
		  throw new Exception("the server returns an error code: " + code);
	  }
	  
	  
	  //将HTTP响应数据中的Header信息转写到HttpServletResponse对象中
	  for(int i=0;;i++)
	  {
	  	String tmpHeaderName = httpConnection.getHeaderFieldKey(i);
	  	String tmpHeaderValue = httpConnection.getHeaderField(i);
	  	
	  	if(tmpHeaderValue != null)
	  	{
	  		if(tmpHeaderName != null)
	  		{
	  			response.addHeader(tmpHeaderName, tmpHeaderValue);
	  			//System.out.println(tmpHeaderName + ": " + tmpHeaderValue);
	  		}
	  	}
	  	else
	  	{
	  		break;
	  	}
	  }
	  
	  
	  //将HTTP响应数据中的流数据转写到HttpServletResponse对象中
	  InputStream is = httpConnection.getInputStream();
	  OutputStream os = response.getOutputStream();
	  int tempbyte = -1;
	  while ((tempbyte = is.read()) != -1)
	  {
	  	os.write(tempbyte);
	  }
	  //刷新此输出流并强制写出所有缓冲的输出字节
	  os.flush();
	  
	  
	  //关闭流
	  is.close();
	  os.close();
		
		
		return result;
	}
	private boolean callDynamicsCRMWebAPI(String webapiURL, String postData, HttpServletResponse response, boolean requestDataIsJSONFormat, boolean responseDataIsJSONFormat)
	{
		boolean result = true;
		
		
		//先清空上次调用的结果
		appWebAPIResult = "";
		isRequestSuccess = true;
		errorMessage = "";
		
		
		//记录日志信息
		logger.info("【Call Dynamic Webapi Start】+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        logger.info("【Call at】: " + this.getCallerName());
        logger.info("【Webapi URL】: " + webapiURL);
        if(StringUtils.isNotBlank(postData)) {
        	logger.info("【Post Data】: " + postData);
        }
		
		//设置加锁标志
		synchronized(lockObject)
		{
			if(lockingFlag == false)
			{
				lockingFlag = true;
			}
		}
		
		
		//如果还没有登录Dynamics CRM系统或者登录令牌已超时，则重新登录Dynamics CRM系统
		if(lockingFlag == true)
		{
			try
			{
				if(isTokenExpired() == true)
				{
					if(loginDynamicsCRMByADFS() == false)
					{
						logger.error("【Error Login Message】: " + errorMessage);
						return false;
					}
				}
			}
			catch(Exception e)
			{
				logger.error("【Error Info】: ", e);
				throw e;
			}
			finally
			{
				//解除加锁标志
				lockingFlag = false;
			}
		}
		
		
		//检查参数是否满足要求
		if(webapiURL == null || "".equals(webapiURL.trim()) == true)
		{
			errorMessage = "Invalid URL address to access Dynamics CRM WebAPI.";
			
			return false;
		}

		
		//准备HTTP连接
		try
		{
			//打开URL地址生成HTTP URL连接对象
			HttpURLConnection httpConn = null;
			boolean postRequestFlag = false;

			if(!"".equals(httpMethod)){
				if("PATCH".equals(httpMethod)) {
					httpConn = openURL(webapiURL,"POST", "UTF-8", true);
					postRequestFlag = true;
					}
				else httpConn = openURL(webapiURL, httpMethod, "UTF-8", true);
				if("POST".contentEquals(httpMethod.toUpperCase())){
					postRequestFlag = true;
				}
			}else{
				if("".equals(postData) != true && postData != null)
				{
					httpConn = openURL(webapiURL, "POST", "UTF-8", true);
					postRequestFlag = true;
				}
				else
				{
					httpConn = openURL(webapiURL, "GET", "UTF-8", true);
				}
			}
			
			//设置会话Cookie
		  httpConn.addRequestProperty("Cookie", "MSISAuth=" + appTokenCookieValue + "; ReqClientId=" + requestClientId);
		  	//PATCH伪装POST
		  if("PATCH".equals(httpMethod)) {
			httpConn.setRequestProperty("X-HTTP-Method-Override", "PATCH");
			//httpConn.setRequestMethod("POST");
			}
		  
		  //设置header
		  httpConn.setRequestProperty("Connection","keep-alive");
		  //httpConn.setRequestProperty("Cache-Control","max-age=0");
		  //httpConn.setRequestProperty("Upgrade-Insecure-Requests","1");
		  httpConn.setRequestProperty("Accept-Language","zh-CN,zh;q=0.9,zh-TW;q=0.8");
		  
		  httpConn.setRequestProperty("Prefer","return=representation");
		  //设置请求数据格式及字符集
		  if(requestDataIsJSONFormat == true)
		  {
		  	//请求数据格式为JSON格式，字符集为“UTF-8”
		  	httpConn.setRequestProperty("Content-Type","application/json; charset=UTF-8");
		  }
		  else
		  {
		  	//请求数据格式非JSON格式，字符集为“UTF-8”
		  	if(postRequestFlag == true)
		  	{
		  		httpConn.setRequestProperty("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
		  	}
		  	else
		  	{
		  		httpConn.setRequestProperty("Content-Type","charset=UTF-8");
		  	}
		  }
		  
		  
		  //设置客户端接受的响应数据格式及字符集
		  if(responseDataIsJSONFormat == true)
		  {
		  	//请求JSON格式的响应数据
		  	httpConn.setRequestProperty("Accept","application/json");
		  }
		  else
		  {
		  	//请求非JSON格式的响应数据
		  	httpConn.setRequestProperty("Accept","application/json, text/javascript, */*; q=0.01");
		  }
		  
		  
		  //提交登录表单数据到HTTP连接对象
		  if("".equals(postData) != true && postData != null)
		  {
		  	httpConn.setDoOutput(true);
		  	httpConn.setDoInput(true);
		  	
		  	byte[] tmpPostData = postData.getBytes(Charset.forName("UTF-8"));
		  	
		  	httpConn.setRequestProperty("Content-Length","" + tmpPostData.length);
		  	httpConn.getOutputStream().write(tmpPostData);
		  	
		  	
		  	httpConn.getOutputStream().flush();
			}		  		 
          
		  //读取服务器响应内容
		  if(response == null)
		  {
		  	appWebAPIResult = getResponseContent(httpConn, true);
		  	logger.info("【Return result】: " + appWebAPIResult);
		  }
		  else
		  {
		  	result = pGetAttachmentFromDynamicsCRM(httpConn, response);
		  	logger.info("【Return result】: " + result);
		  }

		  //断开连接
		  httpConn.disconnect();
		}
		catch(Exception e)
		{
		  //e.printStackTrace();
		  if(response == null)
		  {
		  	errorMessage = "Failed to call the specified WebAPI : " + e.getMessage();
		  }
		  else
		  {
		  	errorMessage = "Failed to get the attachment from the specified URL address : " + e.getMessage();
		  }
		  logger.error("【Error message】: " + errorMessage);
		  logger.error("【Exception details】: ", e);
		  result = false;
		}

		return result;
	}
	
	
	
	
	public static void main(String args[]) throws IOException
	{
		TZAdfsLoginObject tmpADFSObject = new TZAdfsLoginObject();
		
		String tmpURL = "https://crmdev.saif.sjtu.edu.cn";
		String tmpUserName = "****\\*******";
		String tmpUserPswd = "*******";
		
		tmpADFSObject.setDynamicsLoginPrarameters(tmpURL,tmpUserName,tmpUserPswd,-1);
		/*
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		dataMap.put("name", "张浪");
		String postData = jacksonUtil.Map2json(dataMap);
				
		if(tmpADFSObject.callDynamicsCRMWebAPIForJSONDataWithJSONData("https://crmdev.saif.sjtu.edu.cn/api/data/v8.2/accounts", postData) == true) {
			if(tmpADFSObject.isRequestSuccess() == true) {
				//成功返回内容
				String result = tmpADFSObject.getWebAPIResult();
				//输出请求成功消息
				System.out.println(result);
			}else {
				//请求发生错误
				String badRequestResult = tmpADFSObject.getWebAPIResult();
				//此处错误信息需要根据具体接口返回进行解析，下面只是一个解析示例
				try {
					jacksonUtil.json2Map(badRequestResult);
					if(jacksonUtil.containsKey("error")) {
						String message = jacksonUtil.getMap("error").get("message").toString();
						if(StringUtils.isNotBlank(message)) {
							badRequestResult = message;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				//输出错误消息
				System.out.println(badRequestResult);
			}
		}else {
			//请求失败原因
			String errorMsg = tmpADFSObject.getErrorMessage();
			//输出请求失败原因
			System.out.println(errorMsg);
		}
		*/
		

		//if(tmpADFSObject.callDynamicsCRMWebAPI("http://dcrmdemo.jollow.com.cn:8080/services/ChooseOrChangeConcentration.asmx/Do", "studentid=48000746-FC8A-E811-80C9-005056B66569&concentration=0&force=true") == true)
		//if(tmpADFSObject.callDynamicsCRMWebAPIForJSONData("http://dcrmdemo.jollow.com.cn:8080/services/ChooseOrChangeConcentration.asmx/Do?studentid=48000746-FC8A-E811-80C9-005056B66569&concentration=0&force=true") == true)
		//if(tmpADFSObject.callDynamicsCRMWebAPIForJSONData("http://dcrmdemo.jollow.com.cn:8080/services/ChooseOrChangeConcentration.asmx/Do", "studentid=48000746-FC8A-E811-80C9-005056B66569&concentration=0&force=true") == true)
		//if(tmpADFSObject.callDynamicsCRMWebAPIForJSONDataWithJSONData("http://dcrmdemo.jollow.com.cn:8080/services/ChooseOrChangeConcentration.asmx/Do", "{studentid:\"48000746-FC8A-E811-80C9-005056B66569\",concentration:0,force:true}") == true)
		//if(tmpADFSObject.callDynamicsCRMWebAPIForJSONData("https://dynamics.sctu.dev/ISV/TZEMSServices/services/ChooseOrChangeConcentration.asmx/Do?studentid=10980682-638D-E811-80C9-005056B66569&concentration=0&force=true") == true)
		//if(tmpADFSObject.callDynamicsCRMWebAPI("https://dynamics.sctu.dev/ISV/TZEMSServices/services/ReviewPersonalAcademicPlan.aspx?studentid=48000746-FC8A-E811-80C9-005056B66569&includepending=Y") == true)
		
		//第一次测试
		System.out.println("HelloWorld==>>" + Calendar.getInstance().getTime());
		if(tmpADFSObject.getAttachmentFromDynamicsCRM("https://crmuat.saif.sjtu.edu.cn/ISV/tzsvc/services/AttachmentsManager.asmx/GetFile?uid=79037be7-c785-e911-a81e-81d2d679bb3e&downloadFlag=no&filename=none&s=0.65347878252589210", null) == true)
		{
		  if(tmpADFSObject.isRequestSuccess() == true) {
			  System.out.println("HelloWorld==>>Success!");
		  }else {
			  System.out.println("HelloWorld==>>" + tmpADFSObject.getWebAPIResult());
		  }
		}
		else
		{
		  System.out.println("HelloWorld==>>" + tmpADFSObject.getErrorMessage());
		}
	}
}