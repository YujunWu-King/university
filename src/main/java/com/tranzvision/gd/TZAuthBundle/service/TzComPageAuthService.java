/**
 * 
 */
package com.tranzvision.gd.TZAuthBundle.service;

/**
 * 组件、页面访问许可权接口定义
 * 
 * @author SHIHUA
 * @since 2016-01-29
 */
public interface TzComPageAuthService {

	/**
	 * 校验组件页面的访问许可权
	 * 
	 * @param oprid
	 *            当前用户的oprid
	 * @param comName
	 *            访问的组件名称
	 * @param pageName
	 *            访问的页面名称
	 * @param vType
	 *            访问类别：R-只读、W-读写
	 * @param errorMsg
	 *            错误信息
	 * @return boolean
	 */
	public boolean checkPermission(String oprid, String comName, String pageName, String vType, String[] errorMsg);

	/**
	 * 校验组件页面的只读访问权限
	 * 
	 * @param oprid
	 *            当前用户的oprid
	 * @param comName
	 *            访问的组件名称
	 * @param pageName
	 *            访问的页面名称
	 * @param errorMsg
	 *            错误信息
	 * @return boolean
	 */
	public boolean checkReadPermission(String oprid, String comName, String pageName, String[] errorMsg);

	/**
	 * 校验组件页面的读写访问权限
	 * 
	 * @param oprid
	 *            当前用户的oprid
	 * @param comName
	 *            访问的组件名称
	 * @param pageName
	 *            访问的页面名称
	 * @param errorMsg
	 *            错误信息
	 * @return boolean
	 */
	public boolean checkUpdatePermission(String oprid, String comName, String pageName, String[] errorMsg);

}
