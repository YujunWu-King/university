/**
 * 
 */
package com.tranzvision.gd.util.cookie;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;
import org.springframework.web.util.CookieGenerator;

import com.tranzvision.gd.util.cfgdata.GetCookieSessionProps;

/**
 * @author SHIHUA
 * @since 2015-11-02
 */
@Service
public class TzCookie {

	@Autowired
	private GetCookieSessionProps getCookieProps;

	/**
	 * 新增一个Cookie（完整参数）
	 * 
	 * @param response
	 * @param cookieName
	 * @param cookieVal
	 * @param cookieMaxAge
	 * @param cookieDomain
	 * @param cookiePath
	 * @param cookieHttpOnly
	 * @param cookieSecure
	 */
	public void addCookie(HttpServletResponse response, String cookieName, Object cookieVal, int cookieMaxAge,
			String cookieDomain, String cookiePath, boolean cookieHttpOnly, boolean cookieSecure) {

		CookieGenerator cookieGen = new CookieGenerator();

		cookieGen.setCookieName(cookieName);
		cookieGen.setCookieDomain(cookieDomain);
		cookieGen.setCookiePath(cookiePath);
		cookieGen.setCookieMaxAge(cookieMaxAge);
		cookieGen.setCookieHttpOnly(cookieHttpOnly);
		cookieGen.setCookieSecure(cookieSecure);

		cookieGen.addCookie(response, cookieVal == null ? null : String.valueOf(cookieVal));

	}

	/**
	 * 新增一个Cookie；会话cookie，关闭浏览器后失效；
	 * 
	 * @param response
	 * @param cookieName
	 * @param cookieVal
	 */
	public void addCookie(HttpServletResponse response, String cookieName, Object cookieVal) {
		int cookieMaxAge = getCookieProps.getCookieMaxAge();
		String cookieDomain = getCookieProps.getCookieDomain();
		String cookiePath = getCookieProps.getCookiePath();
		boolean cookieHttpOnly = getCookieProps.getCookieHttpOnly();
		boolean cookieSecure = getCookieProps.getCookieSecure();

		this.addCookie(response, cookieName, cookieVal, cookieMaxAge, cookieDomain, cookiePath, cookieHttpOnly,
				cookieSecure);
	}

	/**
	 * 新增一个Cookie-2
	 * 
	 * @param response
	 * @param cookieName
	 * @param cookieVal
	 * @param cookieMaxAge
	 */
	public void addCookie(HttpServletResponse response, String cookieName, Object cookieVal, int cookieMaxAge) {
		String cookieDomain = getCookieProps.getCookieDomain();
		String cookiePath = getCookieProps.getCookiePath();
		boolean cookieHttpOnly = getCookieProps.getCookieHttpOnly();
		boolean cookieSecure = getCookieProps.getCookieSecure();
		this.addCookie(response, cookieName, cookieVal, cookieMaxAge, cookieDomain, cookiePath, cookieHttpOnly,
				cookieSecure);
	}

	/**
	 * 新增一个Cookie-3
	 * 
	 * @param response
	 * @param cookieName
	 * @param cookieVal
	 * @param cookieMaxAge
	 * @param cookieDomain
	 * @param cookiePath
	 */
	public void addCookie(HttpServletResponse response, String cookieName, Object cookieVal, int cookieMaxAge,
			String cookieDomain, String cookiePath) {
		cookieDomain = cookieDomain == null ? getCookieProps.getCookieDomain() : cookieDomain;
		cookiePath = cookiePath == null ? getCookieProps.getCookiePath() : cookiePath;
		boolean cookieHttpOnly = getCookieProps.getCookieHttpOnly();
		boolean cookieSecure = getCookieProps.getCookieSecure();
		this.addCookie(response, cookieName, cookieVal, cookieMaxAge, cookieDomain, cookiePath, cookieHttpOnly,
				cookieSecure);
	}

	/**
	 * 私有方法，获取一个Cookie的值
	 * 
	 * @param request
	 * @param cookieName
	 * @return String
	 */
	private String getCookieVal(HttpServletRequest request, String cookieName) {
		Cookie cookie = WebUtils.getCookie(request, cookieName);
		if (cookie != null) {
			return cookie.getValue();
		}
		return null;
	}

	/**
	 * 获取一个String类型的Cookie值
	 * 
	 * @param request
	 * @param cookieName
	 * @return String
	 */
	public String getStringCookieVal(HttpServletRequest request, String cookieName) {
		return this.getCookieVal(request, cookieName);
	}

	/**
	 * 获取一个int类型的Cookie值
	 * 
	 * @param request
	 * @param cookieName
	 * @return int
	 * @throws Exception
	 */
	public int getIntCookieVal(HttpServletRequest request, String cookieName) throws Exception {
		try {
			return Integer.parseInt(this.getCookieVal(request, cookieName));
		} catch (NumberFormatException nfe) {
			throw new Exception(nfe.getMessage());
		}
	}

	/**
	 * 获取一个boolean类型的Cookie值
	 * 
	 * @param request
	 * @param cookieName
	 * @return String
	 */
	public boolean getBoolCookieVal(HttpServletRequest request, String cookieName) {
		return Boolean.parseBoolean(this.getCookieVal(request, cookieName));
	}

	/**
	 * 获取一个指定格式的日期类型Cookie值
	 * 
	 * @param request
	 * @param cookieName
	 * @param dateFormat
	 * @return Date
	 */
	public Date getDateCookieVal(HttpServletRequest request, String cookieName, String dateFormat) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
			return formatter.parse(this.getCookieVal(request, cookieName));
		} catch (java.text.ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取默认日期类型（yyyy-MM-dd）的Cookie值
	 * 
	 * @param request
	 * @param cookieName
	 * @return Date
	 */
	public Date getDate(HttpServletRequest request, String cookieName) {
		return this.getDateCookieVal(request, cookieName, "yyyy-MM-dd");
	}

	/**
	 * 获取默认日期时间类型（yyyy-MM-dd HH:mm:ss）的Cookie值
	 * 
	 * @param request
	 * @param cookieName
	 * @return Date
	 */
	public Date getDateTime(HttpServletRequest request, String cookieName) {
		return this.getDateCookieVal(request, cookieName, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 删除一个Cookie（完整参数）
	 * 
	 * @param response
	 * @param cookieName
	 */
	public void removeCookie(HttpServletResponse response, String cookieName, String cookieDomain, String cookiePath,
			boolean cookieHttpOnly, boolean cookieSecure) {
		addCookie(response, cookieName, null, 0, cookieDomain, cookiePath, cookieHttpOnly, cookieSecure);
	}

	/**
	 * 删除一个Cookie-1
	 * 
	 * @param response
	 * @param cookieName
	 */
	public void removeCookie(HttpServletResponse response, String cookieName) {
		addCookie(response, cookieName, null, 0);
	}

	/**
	 * 删除一个Cookie-2
	 * 
	 * @param response
	 * @param cookieName
	 * @param cookieDomain
	 * @param cookiePath
	 */
	public void removeCookie(HttpServletResponse response, String cookieName, String cookieDomain, String cookiePath) {
		addCookie(response, cookieName, null, 0, cookieDomain, cookiePath);
	}

}
