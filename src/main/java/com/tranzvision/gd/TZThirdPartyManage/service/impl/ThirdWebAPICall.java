package com.tranzvision.gd.TZThirdPartyManage.service.impl;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.util.adfs.TZAdfsLoginObject;
import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * 	第三方系统webapi调用
 * @author zhanglang
 *
 */
public class ThirdWebAPICall {
    //记录日志
    private static final Logger logger = Logger.getLogger("DynamicWebCall");

    
    //实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法 
    private static TrustManager myX509TrustManager = new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain,
                String authType) throws CertificateException {
            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain,
                String authType) throws CertificateException {
            }
        };

    private SqlQuery sqlQuery;
    private TzLoginServiceImpl tzLoginService;

    //第三方系统ID
    private String systemId;

    //返回结构
    private String appWebAPIResult = "";

    //错误消息
    private String errorMessage = "";

    //记录WebAPI请求成功
    private boolean isRequestSuccess = true;

    //记录Admin获取到的auth cookie
    private String admin_MSISAuth = "";

    //是否存储MSISAuth
    private boolean storeAuthCookie = false;

    /**
     *	构造方法
     * @param systemId        第三方系统编号
     */
    public ThirdWebAPICall(String systemId) {
        this.systemId = systemId;

        GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
        sqlQuery = (SqlQuery) getSpringBeanUtil.getAutowiredSpringBean("SqlQuery");
        tzLoginService = (TzLoginServiceImpl) getSpringBeanUtil.getAutowiredSpringBean("TzLoginServiceImpl");
    }

    /**
 	 *	构造方法，Dynamic适用
     */
    public ThirdWebAPICall() {
        GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
        sqlQuery = (SqlQuery) getSpringBeanUtil.getAutowiredSpringBean("SqlQuery");
        tzLoginService = (TzLoginServiceImpl) getSpringBeanUtil.getAutowiredSpringBean("TzLoginServiceImpl");
    }

    /**
     * 	设置
     * @param storeAuthCookie
     */
    public void setStoreAuthCookie(boolean storeAuthCookie) {
        this.storeAuthCookie = storeAuthCookie;
    }

    /**
     * 	获取返回值
     * @return
     */
    public String getWebAPIResult() {
        return appWebAPIResult;
    }

    /**
     * 	获取错误信息
     * @return
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * 	获取请求结果
     * @return
     */
    public boolean isRequestSuccess() {
        return isRequestSuccess;
    }

    /**
     * 	获取调用者信息的方法
     */
    private String getCallerName() {
        String callerName = "";

        StackTraceElement[] tmpstack = Thread.currentThread().getStackTrace();
        boolean findFlag = false;

        for (StackTraceElement stackitem : tmpstack) {
            if ((stackitem.getClassName().indexOf(getClass().getName())) >= 0) {
                findFlag = true;
            } else if (findFlag == true) {
                callerName = stackitem.getClassName() + "." + stackitem.getMethodName() + "[" + stackitem.getLineNumber() + " line]";
                break;
            }
        }

        return callerName;
    }

    //打开URL地址获取HTTP连接对象的方法
    private HttpURLConnection openURL(String urlStr, String httpMethod, String charactorSet, boolean autoRedirect) throws Exception {
        HttpURLConnection httpConn = null;

        try {
            //检查是否走https协议
            boolean httpsConnection = urlStr.toLowerCase().startsWith("https://");

            //设置SSLContext
            SSLContext sslcontext = null;

            if (httpsConnection == true) {
                sslcontext = SSLContext.getInstance("TLS");
                sslcontext.init(null, new TrustManager[] { myX509TrustManager }, null);
            }

            //打开连接
            URL requestUrl = new URL(urlStr);
            HttpsURLConnection httpsConn = null;

            if (httpsConnection == true) {
                httpsConn = (HttpsURLConnection) requestUrl.openConnection();
            } else {
                httpConn = (HttpURLConnection) requestUrl.openConnection();
            }

            //设置套接工厂 
            if (httpsConnection == true) {
                httpsConn.setSSLSocketFactory(sslcontext.getSocketFactory());
                httpConn = httpsConn;
            }

            //设置接受的字符集
            httpConn.addRequestProperty("Accept-Charset", charactorSet + ";");

            //设置HTTP请求方法
            if ("PATCH".equalsIgnoreCase(httpMethod)) {
                httpConn.setRequestMethod("POST");
                httpConn.setRequestProperty("X-HTTP-Method-Override", "PATCH");
            } else {
                httpConn.setRequestMethod(httpMethod);
            }

            //设置是否允许重定向自动跳转
            httpConn.setInstanceFollowRedirects(autoRedirect);
        } catch (Exception e) {
            throw e;
        }

        return httpConn;
    }

    //从指定HTTP连接对象获取服务器响应内容
    private String getResponseContent(HttpURLConnection httpConn) throws Exception {
        String responseText = "";

        // 从指定对象httpConn中获取服务器响应内容
        try {
            // 根据指定的URL地址发起连接请求
            httpConn.connect();

            // 检查服务器返回的代码
            int code = httpConn.getResponseCode();

            // 检查是否返回500错误
            if (HttpsURLConnection.HTTP_INTERNAL_ERROR == code) {
                throw new Exception("The server returns an error code: 500, and please check if the parameter format is correct.");
            }

            if ((HttpsURLConnection.HTTP_OK == code) ||
                    (HttpsURLConnection.HTTP_CREATED == code)) {
                // 获取输入流
                BufferedReader in = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), Charset.forName("UTF-8")));

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
                isRequestSuccess = false;

                // 获取输入流
                BufferedReader in = new BufferedReader(new InputStreamReader(httpConn.getErrorStream(), Charset.forName("UTF-8")));

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
                logger.error("【Bad Request】: " + responseText);
            } else if (HttpsURLConnection.HTTP_NO_CONTENT == code) {
                //无返回内容
                responseText = "";
            }

            //其他的请求错误、服务器错误
            if (code > 400) {
                isRequestSuccess = false;

                // 获取输入流
                BufferedReader in = new BufferedReader(new InputStreamReader(httpConn.getErrorStream(), Charset.forName("UTF-8")));

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
                logger.error("【" + code + " Bad Request】: " + responseText);
            }
        } catch (Exception e) {
            throw e;
        }
        return responseText;
    }

    //获取请求cookie
    public String getCookieValue(HttpServletRequest request, String cookieName) {
        String tmpCookieValue = "";

        //获取cookie
        if (cookieName != null) {
            String cookieStr = request.getHeader("Cookie");

            if (StringUtils.isNotBlank(cookieStr)) {
                String[] cookieArr = cookieStr.split(";");

                for (String cookie : cookieArr) {
                    if (cookie.toLowerCase().replaceAll("\\s+", "").startsWith(cookieName.toLowerCase() + "=") == true) {
                        tmpCookieValue = cookie.substring(cookieName.length() + 2);
                        break;
                    }
                }
            }
        }

        return tmpCookieValue;
    }

    /**
     * 	调用第三方系统webapi
     * @param webapiURL						webapi地址
     * @param postData						提交参数
     * @param request						request请求
     * @param httpMethod					提交类型，默认POST
     * @param requestDataIsJSONFormat		请求参数是否JSON格式
     * @param responseDataIsJSONFormat		返回参数是否JSON格式
     * @return
     */
    public boolean callThirdWebServiceAPI(String webapiURL, String postData, HttpServletRequest request, String httpMethod,
        boolean requestDataIsJSONFormat, boolean responseDataIsJSONFormat) {
        boolean result = true;
        //调用的结果
        appWebAPIResult = "";

        if ((webapiURL == null) || ("".equals(webapiURL.trim()) == true)) {
            errorMessage = "Invalid URL address to access SSO WebAPI.";
            return false;
        }

        //准备HTTP连接
        try {
            //打开URL地址生成HTTP URL连接对象
            HttpURLConnection httpConn = null;
            boolean postRequestFlag = false;

            if (!"".equals(httpMethod)) {
                httpConn = openURL(webapiURL, httpMethod, "UTF-8", true);
                if ("POST".contentEquals(httpMethod.toUpperCase())) {
                    postRequestFlag = true;
                }
            } else {
                if (("".equals(postData) != true) && (postData != null)) {
                    httpConn = openURL(webapiURL, "POST", "UTF-8", true);
                    postRequestFlag = true;
                } else {
                    httpConn = openURL(webapiURL, "GET", "UTF-8", true);
                }
            }

            //设置请求cookie
            List<Map<String, Object>> cookieList = sqlQuery.queryForList("select TZ_COOKIE from TZ_OSYS_COOKIES_T where OSYS_ID=?",
                    new Object[] { systemId });

            if ((cookieList != null) && (cookieList.size() > 0)) {
                String reqCookie = "";

                for (Map<String, Object> cookieMap : cookieList) {
                    String cookie = cookieMap.get("TZ_COOKIE").toString();
                    String cookieValue = this.getCookieValue(request, cookie);

                    if (StringUtils.isNotEmpty(cookieValue)) {
                        if ("".equals(reqCookie)) {
                            reqCookie = cookie + "=" + cookieValue;
                        } else {
                            reqCookie += (";" + cookie + "=" + cookieValue);
                        }
                    }
                }

                if (!"".equals(reqCookie)) {
                    //设置请求ID
                    String requestClientId = UUID.randomUUID().toString();
                    //设置会话Cookie
                    httpConn.addRequestProperty("Cookie", reqCookie + "; ReqClientId=" + requestClientId);
                }
            }

            //设置header
            httpConn.setRequestProperty("Connection", "keep-alive");
            httpConn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9,zh-TW;q=0.8");
            httpConn.setRequestProperty("Prefer", "return=representation");

            //设置请求数据格式及字符集
            if (requestDataIsJSONFormat == true) {
                //请求数据格式为JSON格式，字符集为“UTF-8”
                httpConn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            } else {
                //请求数据格式非JSON格式，字符集为“UTF-8”
                if (postRequestFlag == true) {
                    httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                } else {
                    httpConn.setRequestProperty("Content-Type", "charset=UTF-8");
                }
            }

            //设置客户端接受的响应数据格式及字符集
            if (responseDataIsJSONFormat == true) {
                //请求JSON格式的响应数据
                httpConn.setRequestProperty("Accept", "application/json");
            } else {
                //请求非JSON格式的响应数据
                httpConn.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
            }

            //提交登录表单数据到HTTP连接对象
            if (("".equals(postData) != true) && (postData != null)) {
                httpConn.setDoOutput(true);
                httpConn.setDoInput(true);

                byte[] tmpPostData = postData.getBytes(Charset.forName("UTF-8"));

                httpConn.setRequestProperty("Content-Length", "" + tmpPostData.length);
                httpConn.getOutputStream().write(tmpPostData);

                httpConn.getOutputStream().flush();
            }

            //读取服务器响应内容
            appWebAPIResult = getResponseContent(httpConn);
        } catch (Exception e) {
            e.printStackTrace();
            errorMessage = e.getMessage();

            return false;
        }

        return result;
    }

    /**
     * 	 设置指定dynamic用户登录
     * @param httpConn
     */
    private void setDynamicAdminCookieToken(HttpURLConnection httpConn) {
        try {
            String sql = "select TZ_HARDCODE_VAL from ps_tz_hardcd_pnt where TZ_HARDCODE_PNT=?";

            String dynURL = sqlQuery.queryForObject(sql, new Object[] { "TZ_DYNAMICS_LOGIN_URL" }, "String");
            String dynUserName = sqlQuery.queryForObject(sql, new Object[] { "TZ_DYNAMICS_USER_NAME" }, "String");
            String dynUserPswd = sqlQuery.queryForObject(sql, new Object[] { "TZ_DYNAMICS_USER_PSWD" }, "String");

            if (StringUtils.isNotBlank(dynURL) &&
                    StringUtils.isNotBlank(dynUserName) &&
                    StringUtils.isNotBlank(dynUserPswd)) {
                TZAdfsLoginObject tzADFSObject = new TZAdfsLoginObject();
                tzADFSObject.setDynamicsLoginPrarameters(dynURL, dynUserName, dynUserPswd, -1);

                if (tzADFSObject.loginDynamicsCRMByADFS() == true) {
                    String dynamicAppToken = tzADFSObject.getAppTokenCookieValue();

                    //设置请求ID
                    String requestClientId = UUID.randomUUID().toString();
                    //设置会话Cookie
                    httpConn.addRequestProperty("Cookie", "MSISAuth=" + dynamicAppToken + "; ReqClientId=" + requestClientId);

                    if (storeAuthCookie == true) {
                        admin_MSISAuth = dynamicAppToken;
                    }
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 	调用Dynamic webapi
     * @param webapiURL					webapi地址
     * @param postData					提交参数
     * @param request					request请求
     * @param httpMethod				提交类型，默认POST
     * @param requestDataIsJSONFormat	请求参数是否JSON格式
     * @param responseDataIsJSONFormat	返回参数是否JSON格式
     * @return
     */
    public boolean callDynamicWebServiceAPI(String webapiURL, String postData, HttpServletRequest request, String httpMethod,
        boolean requestDataIsJSONFormat, boolean responseDataIsJSONFormat) {
        boolean result = true;
        //调用的结果
        appWebAPIResult = "";
        errorMessage = "";
        isRequestSuccess = true;

        //记录日志信息
        logger.info("【Call Dynamic Webapi Start】+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        logger.info("【Call At】: " + this.getCallerName());

        if ((tzLoginService != null) && (request != null)) {
            String currOprid = tzLoginService.getLoginedManagerOprid(request);
            String currName = sqlQuery.queryForObject("select TZ_REALNAME from PS_TZ_AQ_YHXX_TBL where OPRID=?",
                    new Object[] { currOprid }, "String");
            logger.info("【Call User】: " + currOprid + " - " + currName);
        }
        logger.info("【Webapi URL】: " + webapiURL);

        if (StringUtils.isNotBlank(postData)) {
            logger.info("【Post Data】: " + postData);
        }

        if ((webapiURL == null) || ("".equals(webapiURL.trim()) == true)) {
            errorMessage = "Invalid URL address to access SSO WebAPI.";
            return false;
        }

        //准备HTTP连接
        try {
            //打开URL地址生成HTTP URL连接对象
            HttpURLConnection httpConn = null;
            boolean postRequestFlag = false;

            if (!"".equals(httpMethod)) {
                httpConn = openURL(webapiURL, httpMethod, "UTF-8", true);
                if ("POST".contentEquals(httpMethod.toUpperCase()) ||
                        "PATCH".contentEquals(httpMethod.toUpperCase())) {
                    postRequestFlag = true;
                }
            } else {
                if (("".equals(postData) != true) && (postData != null)) {
                    httpConn = openURL(webapiURL, "POST", "UTF-8", true);
                    postRequestFlag = true;
                } else {
                    httpConn = openURL(webapiURL, "GET", "UTF-8", true);
                }
            }

            //            设置请求cookie            
            //            String cookieValue = this.getCookieValue(request, "TZ_MSISAuth");
            //            String cookieValue = this.getCookieValue(request, "MSISAuth") + this.getCookieValue(request, "MSISAuth1");
            //            String cookieValue = "";
            //            if(StringUtils.isNotEmpty(cookieValue)) {
            //            	//设置请求ID
            //            	String dyReqClientId = this.getCookieValue(request, "TZ_REQ_CLIENTID");
            //      		  	//设置会话Cookie
            //                httpConn.addRequestProperty("Cookie", "MSISAuth=" + cookieValue + "; ReqClientId=" + dyReqClientId);
            //            }else 
            if (!"".equals(admin_MSISAuth) && (storeAuthCookie == true)) {
                //设置请求ID
            	String requestClientId = UUID.randomUUID().toString();
                //设置会话Cookie
                httpConn.addRequestProperty("Cookie", "MSISAuth=" + admin_MSISAuth + "; ReqClientId=" + requestClientId);
            } else {
                this.setDynamicAdminCookieToken(httpConn);
            }

            //设置header
            httpConn.setRequestProperty("Connection", "keep-alive");
            httpConn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9,zh-TW;q=0.8");
            httpConn.setRequestProperty("Prefer", "return=representation");

            //设置请求数据格式及字符集
            if (requestDataIsJSONFormat == true) {
                //请求数据格式为JSON格式，字符集为“UTF-8”
                httpConn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            } else {
                //请求数据格式非JSON格式，字符集为“UTF-8”
                if (postRequestFlag == true) {
                    httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                } else {
                    httpConn.setRequestProperty("Content-Type", "charset=UTF-8");
                }
            }

            //设置客户端接受的响应数据格式及字符集
            if (responseDataIsJSONFormat == true) {
                //请求JSON格式的响应数据
                httpConn.setRequestProperty("Accept", "application/json");
            } else {
                //请求非JSON格式的响应数据
                httpConn.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
            }

            //提交登录表单数据到HTTP连接对象
            if (("".equals(postData) != true) && (postData != null)) {
                httpConn.setDoOutput(true);
                httpConn.setDoInput(true);

                byte[] tmpPostData = postData.getBytes(Charset.forName("UTF-8"));

                httpConn.setRequestProperty("Content-Length", "" + tmpPostData.length);
                httpConn.getOutputStream().write(tmpPostData);
                httpConn.getOutputStream().flush();
            }

            //读取服务器响应内容
            appWebAPIResult = getResponseContent(httpConn);
            logger.info("【Return result】: " + appWebAPIResult);
            //断开连接
            httpConn.disconnect();
        } catch (Exception e) {
            //e.printStackTrace();
            errorMessage = "Failed to call the specified WebAPI : " + e.getMessage();
            logger.error("【Error message】: " + errorMessage);
            logger.error("【Exception details】: ", e);
            result = false;
        }

        return result;
    }

    /**
     * 	  获取dynamic附件
     */
    public boolean getAttachmentFromDynamicsCRM(String webapiURL, HttpServletRequest request, HttpServletResponse response, String fileName) {
        boolean result = true;

        //调用的结果
        appWebAPIResult = "";
        errorMessage = "";
        isRequestSuccess = true;

        if ((webapiURL == null) || ("".equals(webapiURL.trim()) == true)) {
            errorMessage = "Invalid URL address to access SSO WebAPI.";
            return false;
        }

        //准备HTTP连接
        try {
            //记录日志信息
            logger.info("【Call Dynamic Attachment Webapi Start】++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            logger.info("【Call at】: " + this.getCallerName());

            if ((tzLoginService != null) && (request != null)) {
                String currOprid = tzLoginService.getLoginedManagerOprid(request);
                String currName = sqlQuery.queryForObject("select TZ_REALNAME from PS_TZ_AQ_YHXX_TBL where OPRID=?",
                        new Object[] { currOprid }, "String");
                logger.info("【Call User】: " + currOprid + " - " + currName);
            }
            logger.info("【Webapi URL】: " + webapiURL);

            //打开URL地址生成HTTP URL连接对象
            HttpURLConnection httpConn = openURL(webapiURL, "GET", "UTF-8", true);

            //            设置请求cookie            
            //            String cookieValue = this.getCookieValue(request, "TZ_MSISAuth");
            //            String cookieValue = this.getCookieValue(request, "MSISAuth") + this.getCookieValue(request, "MSISAuth1");
            //            String cookieValue = "";
            //            if(StringUtils.isNotEmpty(cookieValue)) {
            //            	//设置请求ID
            //      		  	String requestClientId = UUID.randomUUID().toString();
            //      		  	//设置会话Cookie
            //                httpConn.addRequestProperty("Cookie", "MSISAuth=" + cookieValue + "; ReqClientId=" + requestClientId);
            //            }else 
            if (!"".equals(admin_MSISAuth) && (storeAuthCookie == true)) {
                //设置请求ID
                String requestClientId = UUID.randomUUID().toString();
                //设置会话Cookie
                httpConn.addRequestProperty("Cookie", "MSISAuth=" + admin_MSISAuth + "; ReqClientId=" + requestClientId);
            } else {
                this.setDynamicAdminCookieToken(httpConn);
            }

            //设置header
            httpConn.setRequestProperty("Connection", "keep-alive");
            httpConn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9,zh-TW;q=0.8");
            httpConn.setRequestProperty("Prefer", "return=representation");
            httpConn.setRequestProperty("Content-Type", "charset=UTF-8");

            //请求非JSON格式的响应数据
            httpConn.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");

            //根据指定的URL地址发起连接请求
            httpConn.connect();

            //检查服务器返回的代码
            int code = httpConn.getResponseCode();

            //检查是否返回500错误
            if (HttpsURLConnection.HTTP_INTERNAL_ERROR == code) {
                throw new Exception("The server returns an error code: 500, and please check if the parameter format is correct.");
            }

            //检查服务器是否返回错误
            if ((HttpsURLConnection.HTTP_OK != code) &&
                    (HttpsURLConnection.HTTP_NO_CONTENT != code)) {
                throw new Exception("the server returns an error code: " + code);
            }

            //将HTTP响应数据中的Header信息转写到HttpServletResponse对象中
            for (int i = 0;; i++) {
                String tmpHeaderName = httpConn.getHeaderFieldKey(i);
                String tmpHeaderValue = httpConn.getHeaderField(i);

                if ((tmpHeaderValue != null) && (tmpHeaderName != null)) {
                    response.addHeader(tmpHeaderName, tmpHeaderValue);
                } else {
                    break;
                }
            }

            // 2.设置文件头：最后一个参数是设置下载文件名
            if (StringUtils.isNotBlank(fileName)) {
                try {
                    String userAgent = request.getHeader("User-Agent").toUpperCase();
                    if ((userAgent != null) &&
                            ((userAgent.indexOf("MSIE") > 0) || (userAgent.indexOf("LIKE GECKO") > 0) || (userAgent.indexOf("CHROME") > 0))) {
                        fileName = URLEncoder.encode(fileName, "UTF-8");
                    } else {
                        if (userAgent.indexOf("Firefox".toUpperCase()) > 0) { // Firefox
                            fileName = "=?UTF-8?B?" + (new String(org.apache.commons.codec.binary.Base64.encodeBase64(fileName.getBytes("UTF-8")))) + "?=";
                        } else {
                            fileName = new String(fileName.getBytes("UTF-8"),"ISO8859-1");
                        }
                    }
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }

                response.setContentType("multipart/form-data");
                String headerKey = "Content-Disposition";
                String headerValue = String.format("attachment; filename=\"%s\"", fileName);
                response.setHeader(headerKey, headerValue);
            }

            //将HTTP响应数据中的流数据转写到HttpServletResponse对象中
            InputStream is = httpConn.getInputStream();
            OutputStream os = response.getOutputStream();
            int tempbyte = -1;

            while ((tempbyte = is.read()) != -1) {
                os.write(tempbyte);
            }

            //刷新此输出流并强制写出所有缓冲的输出字节
            os.flush();
            //关闭流
            is.close();
            os.close();
        } catch (Exception e) {
            errorMessage = e.getMessage();
            logger.error("【Error message】: " + errorMessage);
            logger.error("【Exception details】: ", e);
            return false;
        }
        return result;
    }

    /**
     * 	存储dynamic附件到指定目录
     */
    public boolean StoreAttachmentFromDynamicsCRM2Path(String webapiURL,
        HttpServletRequest request, String filePath) {
        boolean result = true;

        //调用的结果
        appWebAPIResult = "";
        errorMessage = "";
        isRequestSuccess = true;

        if ((webapiURL == null) || ("".equals(webapiURL.trim()) == true)) {
            errorMessage = "Invalid URL address to access SSO WebAPI.";
            return false;
        }

        //准备HTTP连接
        try {
            //记录日志信息
            logger.info("【Call Dynamic Attachment Webapi Start】++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            logger.info("【Call at】: " + this.getCallerName());

            if ((tzLoginService != null) && (request != null)) {
                String currOprid = tzLoginService.getLoginedManagerOprid(request);
                String currName = sqlQuery.queryForObject("select TZ_REALNAME from PS_TZ_AQ_YHXX_TBL where OPRID=?",
                        new Object[] { currOprid }, "String");
                logger.info("【Call User】: " + currOprid + " - " + currName);
            }
            logger.info("【Webapi URL】: " + webapiURL);

            //打开URL地址生成HTTP URL连接对象
            HttpURLConnection httpConn = openURL(webapiURL, "GET", "UTF-8", true);

            //设置请求cookie            
            if (!"".equals(admin_MSISAuth) && (storeAuthCookie == true)) {
                //设置请求ID
                String requestClientId = UUID.randomUUID().toString();
                //设置会话Cookie
                httpConn.addRequestProperty("Cookie", "MSISAuth=" + admin_MSISAuth + "; ReqClientId=" + requestClientId);
            } else {
                this.setDynamicAdminCookieToken(httpConn);
            }

            //设置header
            httpConn.setRequestProperty("Connection", "keep-alive");
            httpConn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9,zh-TW;q=0.8");
            httpConn.setRequestProperty("Prefer", "return=representation");
            httpConn.setRequestProperty("Content-Type", "charset=UTF-8");

            //请求非JSON格式的响应数据
            httpConn.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");

            //根据指定的URL地址发起连接请求
            httpConn.connect();

            //检查服务器返回的代码
            int code = httpConn.getResponseCode();

            //检查是否返回500错误
            if (HttpsURLConnection.HTTP_INTERNAL_ERROR == code) {
                throw new Exception("The server returns an error code: 500, and please check if the parameter format is correct.");
            }

            //检查服务器是否返回错误
            if ((HttpsURLConnection.HTTP_OK != code) &&
                    (HttpsURLConnection.HTTP_NO_CONTENT != code)) {
                throw new Exception("the server returns an error code: " + code);
            }

            //将HTTP响应数据中的流数据转写到HttpServletResponse对象中
            InputStream in = httpConn.getInputStream();

            try {
                File file = new File(filePath);
                if (!file.exists()) {
                    //文件目录是否存在
                    File parentDir = file.getParentFile();
                    if (!parentDir.exists()) {
                        parentDir.mkdirs();
                    }
                }

                FileOutputStream out = new FileOutputStream(file);
                byte[] buffer = new byte[2097152];
                int readByte = 0;

                while ((readByte = in.read(buffer)) != -1) {
                    out.write(buffer, 0, readByte);
                }
                in.close();
                out.close();
                result = true;
            } catch (IOException e) {
                logger.error("读取照片失败！", e);
                return false;
            }
        } catch (Exception e) {
            errorMessage = e.getMessage();
            logger.error("【Error message】: " + errorMessage);
            logger.error("【Exception details】: ", e);
            return false;
        }
        return result;
    }
}
