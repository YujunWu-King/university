package com.tranzvision.gd.util.base;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.*;

/**
 * @author caoy
 * @version 创建时间：2017年1月2日 下午10:19:41 类说明 工具类
 */
public class CommonUtil {

	public static String getIP(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
			// 多次反向代理后会有多个ip值，第一个ip才是真实ip
			int index = ip.indexOf(",");
			if (index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		}
		ip = request.getHeader("X-Real-IP");
		if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
			return ip;
		}
		return request.getRemoteAddr();
	}

	public static String changeCharset(String oldStr, String charset, String oldchar) {
		try {
			return new String(oldStr.getBytes(oldchar), charset);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return oldStr;
	}

	public static void change(String filepath) throws UnsupportedEncodingException, IOException {
		BufferedReader buf = null;
		OutputStreamWriter pw = null;
		String str = null;
		String allstr = "";

		// 用于输入换行符的字节码
		byte[] c = new byte[2];
		c[0] = 0x0d;
		c[1] = 0x0a;
		String t = new String(c);

		buf = new BufferedReader(new InputStreamReader(new FileInputStream(filepath), "GBK"));
		while ((str = buf.readLine()) != null) {
			allstr = allstr + str + t;
		}

		buf.close();

		pw = new OutputStreamWriter(new FileOutputStream(filepath), "UTF-8");
		pw.write(allstr);
		pw.close();
	}

}
