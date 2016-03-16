/**
 * 
 */
package com.tranzvision.gd.util.tsinghua.sms;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
			mapParams.put("ext", "");
			mapParams.put("stime", "");
			mapParams.put("rrid", "");
			mapParams.put("msgfmt", "");

			System.out.println(mapParams);

			HttpClientService httpClientService = new HttpClientService();
			httpClientService.setUrl(url);
			httpClientService.setParamsMap(mapParams);

			String rst = httpClientService.sendRequest();

			Pattern pattern = Pattern.compile("<string(.*)>(.*)</string>");
			Matcher matcher = pattern.matcher(rst);
			while (matcher.find()) {
				rst = matcher.group(2);
			}

			String msg = "";
			if (!rst.startsWith("-")) {
				msg = "发送成功";
			} else {
				switch (rst) {
				case "-2":
					msg = "帐号/密码不正确";
					break;
				case "-4":
					msg = "余额不足支持本次发送";
					break;
				case "-5":
					msg = "数据格式错误";
					break;
				case "-6":
					msg = "参数有误";
					break;
				case "-7":
					msg = "权限受限";
					break;
				case "-8":
					msg = "流量控制错误";
					break;
				case "-9":
					msg = "扩展码权限错误";
					break;
				case "-10":
					msg = "内容长度过长";
					break;
				case "-11":
					msg = "内部数据库错误";
					break;
				case "-12":
					msg = "序列号状态错误";
					break;
				case "-14":
					msg = "服务器写文件失败";
					break;
				case "-17":
					msg = "没有权限";
					break;
				case "-19":
					msg = "禁止同时使用多个接口地址";
					break;
				case "-20":
					msg = "相同手机号，相同内容重复提交";
					break;
				case "-22":
					msg = "Ip鉴权失败";
					break;
				case "-23":
					msg = "缓存无此序列号信息";
					break;
				case "-601":
					msg = "序列号为空，参数错误";
					break;
				case "-602":
					msg = "序列号格式错误，参数错误";
					break;
				case "-603":
					msg = "密码为空，参数错误";
					break;
				case "-604":
					msg = "手机号码为空，参数错误";
					break;
				case "-605":
					msg = "内容为空，参数错误";
					break;
				case "-606":
					msg = "ext长度大于9，参数错误";
					break;
				case "-607":
					msg = "参数错误 扩展码非数字";
					break;
				case "-608":
					msg = "参数错误 定时时间非日期格式";
					break;
				case "-609":
					msg = "rrid长度大于18,参数错误";
					break;
				case "-610":
					msg = "参数错误 rrid非数字";
					break;
				case "-611":
					msg = "参数错误 内容编码不符合规范";
					break;
				case "-623":
					msg = "手机个数与内容个数不匹配";
					break;
				case "-624":
					msg = "扩展个数与手机个数不匹配";
					break;
				case "-625":
					msg = "定时时间个数与手机个数不匹配";
					break;
				case "-626":
					msg = "rrid个数与手机个数不匹配";
					break;
				}
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
