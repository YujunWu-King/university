/**
 * 
 */
package com.tranzvision.tsinghua.sms;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.util.httpclient.HttpClientService;

/**
 * 清华经管短信发送接口实现类
 * 
 * @author SHIHUA
 * @since 2016-03-15
 */
@Service
public class SendSmsService {

	private String url;

	private String sn;

	private String pwd;

	private String md5pwd = "";

	public SendSmsService() {

		Resource resource = new ClassPathResource("conf/tsinghuasms.properties");

		try {
			Properties smsProps = PropertiesLoaderUtils.loadProperties(resource);

			url = smsProps.getProperty("SendSmsUrl");

			sn = smsProps.getProperty("sn");

			pwd = smsProps.getProperty("pwd");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/*
	 * 方法名称：getMD5 功 能：字符串MD5加密 参 数：待转换字符串 返 回 值：加密之后字符串
	 */
	public String getMD5(String sourceStr) throws UnsupportedEncodingException {
		String resultStr = "";
		try {
			byte[] temp = sourceStr.getBytes();
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(temp);
			// resultStr = new String(md5.digest());
			byte[] b = md5.digest();
			for (int i = 0; i < b.length; i++) {
				char[] digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
				char[] ob = new char[2];
				ob[0] = digit[(b[i] >>> 4) & 0X0F];
				ob[1] = digit[b[i] & 0X0F];
				resultStr += new String(ob);
			}
			return resultStr;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 短信单发接口
	 * 
	 * @param mobile
	 *            手机号
	 * @param content
	 *            短信内容
	 * @return
	 */
	public Map<String, String> doSendSms(String mobile, String content) {
		Map<String, String> mapRet = new HashMap<String, String>();

		try {

			if ("".equals(md5pwd)) {
				md5pwd = this.getMD5(sn + pwd);
			}

			Map<String, Object> mapParams = new HashMap<String, Object>();
			mapParams.put("sn", sn);
			mapParams.put("pwd", md5pwd);
			mapParams.put("mobile", mobile);
			mapParams.put("content", content);

			HttpClientService httpClientService = new HttpClientService();
			httpClientService.setUrl(url);
			httpClientService.setParamsMap(mapParams);
			httpClientService.setCharset("GB2312");

			String rst = httpClientService.sendRequest();
			String msg = "";
			switch (rst) {
			case "0":
				msg = "无状态数据";
				break;
			case "1":
				msg = "发送成功。";
				break;
			case "-1":
				msg = "余额不足，发送失败";
				break;
			case "-2":
				msg = "参数错误";
				break;
			case "-3":
				msg = "序列号密码不正确";
				break;
			}

			mapRet.put("code", rst);
			mapRet.put("msg", msg);

		} catch (Exception e) {
			e.printStackTrace();
			mapRet.put("code", "-9999999");
			mapRet.put("msg", e.getMessage());
		}

		return mapRet;
	}

}
