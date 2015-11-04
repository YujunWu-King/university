/**
 * 
 */
package com.tranzvision.gd.TZAuthBundle.service;

/**
 * @author SHIHUA
 * @since 2015-11-03
 */
public interface TzLoginService {

	/**
	 * 用户登录认证
	 * @param userName
	 * @param userPwd
	 * @return boolean
	 */
	public boolean doLogin(String userName, String userPwd);
	
	/**
	 * 用户登出
	 */
	public void doLogout();
	
}
