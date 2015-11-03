package com.tranzvision.gd.TZBaseBundle.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*功能说明：高端产品-招生系统框架初始化类*/
public interface GdKjInitService {
	/*功能说明：根据主题编号获取该主题下的JS和CSS资源列表*/
	public String getKJResources(HttpServletRequest request,
			HttpServletResponse response, String strThemeID);
	
	/* 获取当前登录人有权限访问的菜单列表 */
	public String getMenuList(HttpServletRequest request,
			HttpServletResponse response);
	
	/* 根据菜单编号获取该菜单下的下级菜单 */
	public String getSubMenuList(HttpServletRequest request,
			HttpServletResponse response, String strMenuID);
	
	/* 获取框架操作列表信息 */
	public String getKJVersionInfo();
	
	/*根据菜单说明信息编号获取菜单说明信息*/
	public String getMenuDescription(HttpServletRequest request, HttpServletResponse response,String strDescID);
	
	
}
