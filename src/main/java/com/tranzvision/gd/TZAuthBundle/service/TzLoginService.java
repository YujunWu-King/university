/**
 * 
 */
package com.tranzvision.gd.TZAuthBundle.service;

import java.util.ArrayList;

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
	 * @param errorMsg
	 * @return boolean
	 */
	public boolean doLogin(HttpServletRequest request, HttpServletResponse response, String orgid, String userName,
			String userPwd, String code, ArrayList<String> errorMsg);

	/**
	 * 切换系统语言
	 * 
	 * @param request
	 * @param response
	 * @param lanaguageCd
	 */
	public void switchSysLanguage(HttpServletRequest request, HttpServletResponse response, String lanaguageCd);

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
	 * 获取登录用户的oprid
	 * 
	 * @param request
	 * @return String
	 */
	public String getLoginedManagerOprid(HttpServletRequest request);

	/**
	 * 获取登录用户的登录账号
	 * 
	 * @param request
	 * @return
	 */
	public String getLoginedManagerDlzhid(HttpServletRequest request);

	/**
	 * 获取登录用户的机构id
	 * 
	 * @param request
	 * @return String
	 */
	public String getLoginedManagerOrgid(HttpServletRequest request);
	
	/**
	 * 获取当前使用的系统语言
	 * 
	 * @param request
	 * @return String
	 */
	public String getSysLanaguageCD(HttpServletRequest request);

	/**
	 * 检查登录状态
	 * 
	 * @param request
	 * @param response
	 * @return boolean
	 */
	public boolean checkManagerLogin(HttpServletRequest request, HttpServletResponse response);

}
