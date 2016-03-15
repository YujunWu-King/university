/**
 * 
 */
package com.tranzvision.gd.util.httpclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

/**
 * 实现Http的Get、Post请求，获取响应内容
 * 
 * @author SHIHUA
 * @since 2016-03-15
 */
public class HttpClientService {

	private final String USER_AGENT = "Mozilla/5.0";

	/**
	 * 请求的地址
	 */
	private String url;

	/**
	 * 发送请求的类型：GET、POST
	 */
	private String requestType = "GET";

	/**
	 * 请求参数
	 */
	private Map<String, Object> paramsMap;

	/**
	 * 请求参数使用的编码，默认UTF-8
	 */
	private String charset = "UTF-8";

	/**
	 * Get请求拼接的参数
	 */
	private String getParameters = "?";

	/**
	 * Post请求的参数
	 */
	private List<NameValuePair> postParameters = new ArrayList<NameValuePair>();

	/**
	 * 请求响应码
	 */
	private int responseCode;

	public HttpClientService() {

	}

	/**
	 * 构造函数
	 * 
	 * @param url
	 *            请求地址
	 * @param requestType
	 *            参数传递方式：GET、POST
	 * @param paramsMap
	 *            请求参数名及参数值
	 * @param charset
	 *            对方要求的参数字符编码
	 */
	public HttpClientService(String url, String requestType, Map<String, Object> paramsMap, String charset) {
		this.url = url;
		this.requestType = requestType.toUpperCase();
		this.paramsMap = paramsMap;
		this.charset = charset;
	}

	/**
	 * 构造函数，默认参数传递方式为GET
	 * 
	 * @param url
	 *            请求地址
	 * @param paramsMap
	 *            请求参数名及参数值
	 * @param charset
	 *            对方要求的参数字符编码
	 */
	public HttpClientService(String url, Map<String, Object> paramsMap, String charset) {
		this.url = url;
		this.paramsMap = paramsMap;
		this.charset = charset;
	}

	/**
	 * 构造函数，默认传递参数编码为UTF-8
	 * 
	 * @param url
	 *            请求地址
	 * @param requestType
	 *            参数传递方式：GET、POST
	 * @param paramsMap
	 *            请求参数名及参数值
	 */
	public HttpClientService(String url, String requestType, Map<String, Object> paramsMap) {
		this.url = url;
		this.requestType = requestType.toUpperCase();
		this.paramsMap = paramsMap;
	}

	/**
	 * 构造函数，默认参数传递方式为GET，默认传递参数编码为UTF-8
	 * 
	 * @param url
	 *            请求地址
	 * @param paramsMap
	 *            请求参数名及参数值
	 */
	public HttpClientService(String url, Map<String, Object> paramsMap) {
		this.url = url;
		this.paramsMap = paramsMap;
	}

	/**
	 * 设置请求地址
	 * 
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 设置请求参数传递方式：GET、POST
	 * 
	 * @param requestType
	 */
	public void setRequestType(String requestType) {
		this.requestType = requestType.toUpperCase();
	}

	/**
	 * 设置请求参数名及参数值
	 * 
	 * @param paramsMap
	 */
	public void setParamsMap(Map<String, Object> paramsMap) {
		this.paramsMap = paramsMap;
	}

	/**
	 * 设置参数传递使用的编码
	 * 
	 * @param charset
	 */
	public void setCharset(String charset) {
		this.charset = charset;
	}

	/**
	 * 获取请求的响应码
	 * 
	 * @return
	 */
	public int getResponseCode() {
		return this.responseCode;
	}

	/**
	 * 发送请求，并返回响应的内容
	 * 
	 * @return
	 */
	public String sendRequest() {

		if (url == null || "".equals(url)) {
			return "invalid url.";
		}

		String urlcheck = url.toLowerCase();

		if (!urlcheck.startsWith("http://") && !urlcheck.startsWith("https://")) {
			return "invalid url.";
		}

		String content = "";
		try {

			String urlParamSep = "";
			// 根据请求类型，拼装参数
			Set<String> keySets = paramsMap.keySet();
			for (String key : keySets) {
				// 将参数值转换为指定编码
				String val = new String(String.valueOf(paramsMap.get(key)).getBytes(), charset);
				if ("GET".equals(requestType)) {
					getParameters = getParameters + urlParamSep + key + "=" + URLEncoder.encode(val, charset);
					if ("".equals(urlParamSep)) {
						urlParamSep = "&";
					}
				} else if ("POST".equals(requestType)) {
					postParameters.add(new BasicNameValuePair(key, val));
				}
			}

			if ("GET".equals(requestType)) {
				content = this.sendGet();
			} else if ("POST".equals(requestType)) {
				content = this.sendPost();
			}

		} catch (Exception e) {
			e.printStackTrace();
			content = "Request Error.";
		}

		return content;
	}

	// HTTP GET request
	private String sendGet() throws Exception {

		String requestUrl = url;
		if (!"?".equals(getParameters)) {
			requestUrl = requestUrl + getParameters;
		}

		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(requestUrl);

		// add request header
		request.addHeader("User-Agent", USER_AGENT);

		HttpResponse response = client.execute(request);

		responseCode = response.getStatusLine().getStatusCode();

		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}

		String content = result.toString();
		System.out.println(content);

		return content;

	}

	// HTTP POST request
	private String sendPost() throws Exception {

		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);

		// add header
		post.setHeader("User-Agent", USER_AGENT);

		post.setEntity(new UrlEncodedFormEntity(postParameters));

		HttpResponse response = client.execute(post);
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + post.getEntity());
		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}

		String content = result.toString();
		System.out.println(content);

		return content;

	}

}
