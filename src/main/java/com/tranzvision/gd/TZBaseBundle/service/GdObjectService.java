package com.tranzvision.gd.TZBaseBundle.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 高端产品顶层service父类
 * 
 * @author tang
 * @version 1.0, 2015/09/28
 */
public interface GdObjectService {
	
	/* 获取当前登录会话语言代码的方法 */
	public String getLoginLanguage(HttpServletRequest request,
			HttpServletResponse response);
	
	/* 获取当前登录人归属机构的方法 */
	public String getLoginOrgID(HttpServletRequest request,
			HttpServletResponse response);
	
	/* 获取LOGO样式表的方法 */
	public String getLogoStyle(HttpServletRequest request,
			HttpServletResponse response);
	
	/* 获取个性化主题编号 */
	public String getUserGxhTheme(HttpServletRequest request,
			HttpServletResponse response);
	
	/* 获取个性化环境语言代码 */
	public String getUserGxhLanguage(HttpServletRequest request,
			HttpServletResponse response);
	
	/* 根据指定消息集合号和消息ID获取消息文本的方法 */
	public String getMessageText(HttpServletRequest request,
			HttpServletResponse response, String msgSetId, String msgId,
			String defaultCNMsg, String defaultENMsg);
	
	/* 设置当前用户访问的组件编号和页面编号 */
	public void setCurrentAccessComponentPage(HttpServletRequest request,String accessComponentID,
			String accessPageID);
	
	/* 判断当前登录会话是否超时失效的方法*/
	public boolean isSessionValid(HttpServletRequest request);
	
	/* 判断主题是否合法的方法 */
	public boolean isThemeIDValid(String theme);
	
	/* 保存指定用户个性化属性设置值的方法 */
	public boolean setUserGXHSXValue(HttpServletRequest request,
			HttpServletResponse response, String userGxhsxName,
			String userGxhsxValue);
	
	/* 设置个性化主题编号 */
	public void setUserGxhTheme(HttpServletRequest request,HttpServletResponse response, String theme);
	
	/* 设置个性化环境语言代码 */
	public void setUserGxhLanguage(HttpServletRequest request,
			HttpServletResponse response, String language);
	
	/* 检查语言环境是否有效*/
	public boolean isLanguageCdValid(String language);
	
	/* 切换当前登录人上下文信息语言环境代码的方法 */
	public void switchLanguageCd(HttpServletRequest request,
			HttpServletResponse response,String loginLanguageCD);
	
	/* 获取当前登录人账号的方法 */
	public String getLoginAccount(HttpServletRequest request,
			HttpServletResponse response);
	
	/*获取当前登录人对应的OPRID的方法 */
	public String getOPRID(HttpServletRequest request);
	
	/*获取超级机构ID的方法*/
	public String getSuperOrgId(HttpServletRequest request, HttpServletResponse response);

	/*根据HardCode代码获取HardCode值的方法*/
	public String getHardCodeValue(String hCode);
	
}
