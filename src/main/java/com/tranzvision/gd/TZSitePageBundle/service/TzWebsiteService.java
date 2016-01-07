/**
 * 
 */
package com.tranzvision.gd.TZSitePageBundle.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * 定义招生网站展示相关操作的方法
 * 
 * @author SHIHUA
 * @since 2016-01-06
 */
public interface TzWebsiteService {

	/**
	 * 校验机构是否有效
	 * 
	 * @param orgid
	 * @param loginOrgid
	 * @return boolean
	 */
	public boolean checkOrgId(String orgid, String loginOrgid);

	/**
	 * 校验站点是否有效，并查询站点语言
	 * 
	 * @param orgid
	 * @param siteid
	 * @return Map<String, Object>
	 */
	public Map<String, Object> checkSiteIdAndGetLang(String orgid, String siteid);

	/**
	 * 获取站点首页装修用的html代码
	 * 
	 * @param request
	 * @param orgid
	 * @param siteid
	 * @return String
	 */
	public String getIndexSaveCode(HttpServletRequest request, String orgid, String siteid);

	/**
	 * 获取站点首页预览的html代码
	 * 
	 * @param request
	 * @param orgid
	 * @param siteid
	 * @return String
	 */
	public String getIndexPreviewCode(HttpServletRequest request, String orgid, String siteid);

	/**
	 * 获取站点首页发布后的html代码
	 * 
	 * @param request
	 * @param orgid
	 * @param siteid
	 * @return String
	 */
	public String getIndexPublishCode(HttpServletRequest request, String orgid, String siteid);

	/**
	 * 获取登录页装修用的html代码
	 * 
	 * @param request
	 * @param orgid
	 * @param siteid
	 * @return String
	 */
	public String getLoginSaveCode(HttpServletRequest request, String orgid, String siteid);

	/**
	 * 获取登录页装预览的html代码
	 * 
	 * @param request
	 * @param orgid
	 * @param siteid
	 * @return String
	 */
	public String getLoginPreviewCode(HttpServletRequest request, String orgid, String siteid);

	/**
	 * 获取登录页发布后的html代码
	 * 
	 * @param request
	 * @param orgid
	 * @param siteid
	 * @return String
	 */
	public String getLoginPublishCode(HttpServletRequest request, String orgid, String siteid);

}
