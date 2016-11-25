package com.tranzvision.gd.util.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * @author caoy
 * @version 创建时间：2016年11月17日 下午3:39:35 类说明 过滤起 设置返回 http头里面添加 x-frame-options:
 *          SAMEORIGIN
 */
public class XframeFilter implements Filter {

	// private FilterConfig filterConfig;

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletResponse hr = (HttpServletResponse) response;
		hr.addHeader("x-frame-options", "SAMEORIGIN");
		chain.doFilter(request, response);// 放行。让其走到下个链或目标资源中
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		System.out.println("XframeFilter-Init");
		// this.filterConfig = filterConfig;
	}

	public void destroy() {
		System.out.println("XframeFilter-Destroy");
	}
}
