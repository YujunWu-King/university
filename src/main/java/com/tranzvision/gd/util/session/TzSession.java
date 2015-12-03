/**
 * 
 */
package com.tranzvision.gd.util.session;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.cfgdata.GetCookieSessionProps;
import com.tranzvision.gd.util.cookie.TzCookie;

/**
 * Http Session 的基本操作类
 * 
 * @author SHIHUA
 * @since 2015-11-02
 */
public class TzSession {

	private GetCookieSessionProps getSessionProps;

	private TzCookie tzCookie;

	private HttpSession session;

	/**
	 * 构造函数
	 * 
	 * @param request
	 */
	public TzSession(HttpServletRequest request) {
		this.session = request.getSession();
		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		
		getSessionProps = (GetCookieSessionProps) getSpringBeanUtil.getAutowiredSpringBean("GetCookieSessionProps");
		
		tzCookie = (TzCookie) getSpringBeanUtil.getAutowiredSpringBean("TzCookie");
		
	}

	/**
	 * 添加一个Session变量值
	 * 
	 * @param sessionName
	 * @param sessionValue
	 */
	public void addSession(String sessionName, Object sessionValue) {
		session.setAttribute(sessionName, sessionValue);
		int t = getSessionProps.getSessionMaxInactive();
		session.setMaxInactiveInterval(t);
	}

	/**
	 * 获取一个Session变量值
	 * 
	 * @param sessionName
	 * @return
	 */
	public Object getSession(String sessionName) {
		return session.getAttribute(sessionName);
	}

	/**
	 * 获取所有Session变量的名称
	 * 
	 * @return Enumeration<String>
	 */
	public Enumeration<String> getAttributeNames() {
		return session.getAttributeNames();
	}

	/**
	 * 删除一个Session变量
	 * 
	 * @param sessionName
	 */
	public void removeSession(String sessionName) {
		session.removeAttribute(sessionName);
	}

	/**
	 * 销毁整个Session
	 * 
	 * @param request
	 * @param response
	 */
	public void invalidate(HttpServletRequest request, HttpServletResponse response) {
		session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}

		tzCookie.removeCookie(response, "JSESSIONID");
	}

	/**
	 * 判断指定的Session变量是否存在（null和空值均为false）
	 * 
	 * @param sessionName
	 * @return boolean
	 */
	public boolean checkSession(String sessionName) {
		Object val = this.getSession(sessionName);
		boolean debugging = getSessionProps.getDebug();
		if (null == val || "".equals(val.toString().trim())) {
			if (!debugging) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 获取SessionId
	 * 
	 * @return String
	 */
	public String getSessionId() {
		return session.getId();
	}

	/**
	 * 获取最近一次更新Session的时间戳
	 * 
	 * @return long
	 */
	public long getLastAccessedTime() {
		return session.getLastAccessedTime();
	}

	/**
	 * 获取Session的创建时间
	 * 
	 * @return long
	 */
	public long getCreationTime() {
		return session.getCreationTime();
	}

}
