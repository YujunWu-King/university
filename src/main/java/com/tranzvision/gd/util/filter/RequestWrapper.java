package com.tranzvision.gd.util.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * @author caoy
 * @version 创建时间：2016年11月17日 下午3:50:07 类说明
 */
public final class RequestWrapper extends HttpServletRequestWrapper {

	public RequestWrapper() {
		super(null);
	}

	public RequestWrapper(HttpServletRequest httpservletrequest) {
		super(httpservletrequest);
	}

	public String[] getParameterValues(String s) {
		String str[] = super.getParameterValues(s);
		if (str == null) {
			return null;
		}
		int i = str.length;
		String as1[] = new String[i];
		for (int j = 0; j < i; j++) {
			as1[j] = cleanXSS(cleanSQLInject(str[j]));
		}

		return as1;
	}

	public String getParameter(String s) {
		String s1 = super.getParameter(s);
		if (s1 == null) {
			return null;
		} else {
			return cleanXSS(cleanSQLInject(s1));
		}
	}

	public String getHeader(String s) {
		String s1 = super.getHeader(s);
		if (s1 == null) {
			return null;
		} else {
			return cleanXSS(cleanSQLInject(s1));
		}
	}

	public String cleanXSS(String src) {
		String temp = src;

		System.out.println("xss---temp-->" + src);
		src = src.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		// if (src.indexOf("address")==-1)
		// {
		src = src.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
		// }

		src = src.replaceAll("'", "&#39;");

		Pattern pattern = Pattern.compile("(eval\\((.*)\\)|script)", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(src);
		src = matcher.replaceAll("");

		pattern = Pattern.compile("[\\\"\\'][\\s]*javascript:(.*)[\\\"\\']", Pattern.CASE_INSENSITIVE);
		matcher = pattern.matcher(src);
		src = matcher.replaceAll("\"\"");

		// 增加脚本
		src = src.replaceAll("script", "").replaceAll(";", "").replaceAll("\"", "").replaceAll("@", "")
				.replaceAll("0x0d", "").replaceAll("0x0a", "").replaceAll(",", "");

		if (!temp.equals(src)) {
			System.out.println("输入信息存在xss攻击！");
			System.out.println("原始输入信息-->" + temp);
			System.out.println("处理后信息-->" + src);
		}
		return src;
	}

	// 需要增加通配，过滤大小写组合
	public String cleanSQLInject(String src) {
		String temp = src;
		src = src.replaceAll(" insert ", "forbidI").replaceAll(" select ", "forbidS").replaceAll(" update ", "forbidU")
				.replaceAll(" delete ", "forbidD").replaceAll(" and ", "forbidA").replaceAll(" or ", "forbidO");

		if (!temp.equals(src)) {
			System.out.println("输入信息存在SQL攻击！");
			System.out.println("原始输入信息-->" + temp);
			System.out.println("处理后信息-->" + src);
		}
		return src;
	}
}
