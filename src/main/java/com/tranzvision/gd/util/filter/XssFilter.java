package com.tranzvision.gd.util.filter;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author caoy
 * @version 创建时间：2016年11月17日 下午3:46:43 类说明 注入式攻击过滤器
 */
public class XssFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest servletrequest, ServletResponse servletresponse, FilterChain filterchain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		// flag = true 只做URL验证; flag = false 做所有字段的验证;
		boolean flag = true;
		if (flag) {
			// 只对URL做xss校验
			HttpServletRequest httpServletRequest = (HttpServletRequest) servletrequest;
			HttpServletResponse httpServletResponse = (HttpServletResponse) servletresponse;

			String requesturi = httpServletRequest.getRequestURL().toString();
			requesturi = URLDecoder.decode(requesturi, "UTF-8");

			System.out.println("requesturi." + requesturi);

			// 以下是不需要过滤的地址
			if (requesturi != null && requesturi.indexOf("AAA.html") != -1) {
				filterchain.doFilter(servletrequest, servletresponse);
				return;
			}

			RequestWrapper rw = new RequestWrapper(httpServletRequest);
			String param = httpServletRequest.getQueryString();

			if (!"".equals(param) && param != null) {
				param = URLDecoder.decode(param, "UTF-8");
				System.out.println("param." + param);
				//String originalurl = requesturi + param;

				String sqlParam = param;
				// 添加sql注入的判断 只针对特出页面
				if (requesturi.endsWith("/AAA.html")) {
					sqlParam = rw.cleanSQLInject(param);
				}

				String xssParam = rw.cleanXSS(sqlParam);
				requesturi += "?" + xssParam;

				if (!xssParam.equals(param)) {
					System.out.println("requesturi::::::" + requesturi);
					httpServletResponse.sendRedirect(requesturi);
					System.out.println("no entered.");
					return;
				}
			}
			filterchain.doFilter(servletrequest, servletresponse);
		} else {
			// 对请求中的所有东西都做校验，包括表单。此功能校验比较严格容易屏蔽表单正常输入，使用此功能请注意。
			filterchain.doFilter(new RequestWrapper((HttpServletRequest) servletrequest), servletresponse);
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
