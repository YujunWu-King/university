/**
 * 
 */
package com.tranzvision.gd.TZAuthBundle.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tranzvision.gd.TZAccountMgBundle.model.PsTzAqYhxxTbl;

/**
 * @author SHIHUA
 * @since 2015-11-03
 */
public interface TzLoginService {

	/**
	 * 用户登录认证
	 * 
	 * @param request
	 * @param response
	 * @param orgid
	 * @param userName
	 * @param userPwd
	 * @param code
	 * @return boolean
	 */
	public boolean doLogin(HttpServletRequest request, HttpServletResponse response, String orgid, String userName,
			String userPwd, String code);

	/**
	 * 用户登出
	 * 
	 * @param request
	 * @param response
	 */
	public void doLogout(HttpServletRequest request, HttpServletResponse response);

	/**
	 * 获取登录用户的数据对象
	 * 
	 * @param request
	 * @return PsTzAqYhxxTbl
	 */
	public PsTzAqYhxxTbl getLoginedManagerInfo(HttpServletRequest request);

	/**
	 * 检查登录状态
	 * 
	 * @param request
	 * @param response
	 * @return boolean
	 */
	public boolean checkManagerLogin(HttpServletRequest request, HttpServletResponse response);

}
