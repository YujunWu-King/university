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

import com.tranzvision.gd.util.cfgdata.GetCookieProps;

/**
 * @author SHIHUA
 * @since 2015-11-02
 */
@Service
public class TzCookie {

	@Autowired
	private GetCookieProps getCookieProps;

	private CookieGenerator cookieGen;

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

		cookieGen = new CookieGenerator();
		
		cookieGen.setCookieName(cookieName);
		cookieGen.setCookieDomain(cookieDomain);
		cookieGen.setCookiePath(cookiePath);
		cookieGen.setCookieMaxAge(cookieMaxAge);
		cookieGen.setCookieHttpOnly(cookieHttpOnly);
		cookieGen.setCookieSecure(cookieSecure);
		
		cookieGen.addCookie(response, cookieVal.toString());

	}

	/**
	 * 新增一个Cookie
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
	 * 新增一个Cookie
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
	 * 新增一个Cookie
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
	 * @param name
	 * @return String
	 */
	private String getCookieVal(HttpServletRequest request, String name) {
		Cookie cookie = WebUtils.getCookie(request, name);
		return cookie.getValue();
	}

	/**
	 * 获取一个String类型的Cookie值
	 * 
	 * @param request
	 * @param name
	 * @return String
	 */
	public String getStringCookieVal(HttpServletRequest request, String name) {
		return this.getCookieVal(request, name);
	}

	/**
	 * 获取一个int类型的Cookie值
	 * 
	 * @param request
	 * @param name
	 * @return int
	 * @throws Exception
	 */
	public int getIntCookieVal(HttpServletRequest request, String name) throws Exception {
		try {
			return Integer.parseInt(this.getCookieVal(request, name));
		} catch (NumberFormatException nfe) {
			throw new Exception(nfe.getMessage());
		}
	}

	/**
	 * 获取一个boolean类型的Cookie值
	 * 
	 * @param request
	 * @param name
	 * @return String
	 */
	public boolean getBoolCookieVal(HttpServletRequest request, String name) {
		return Boolean.parseBoolean(this.getCookieVal(request, name));
	}

	/**
	 * 获取一个指定格式的日期类型Cookie值
	 * 
	 * @param request
	 * @param name
	 * @param dateFormat
	 * @return Date
	 */
	public Date getDateCookieVal(HttpServletRequest request, String name, String dateFormat) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
			return formatter.parse(this.getCookieVal(request, name));
		} catch (java.text.ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取默认日期类型（yyyy-MM-dd）的Cookie值
	 * 
	 * @param request
	 * @param name
	 * @return Date
	 */
	public Date getDate(HttpServletRequest request, String name) {
		return this.getDateCookieVal(request, name, "yyyy-MM-dd");
	}

	/**
	 * 获取默认日期时间类型（yyyy-MM-dd HH:mm:ss）的Cookie值
	 * 
	 * @param request
	 * @param name
	 * @return Date
	 */
	public Date getDateTime(HttpServletRequest request, String name) {
		return this.getDateCookieVal(request, name, "yyyy-MM-dd HH:mm:ss");
	}

}
