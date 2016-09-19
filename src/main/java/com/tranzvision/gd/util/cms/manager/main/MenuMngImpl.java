package com.tranzvision.gd.util.cms.manager.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.cms.entity.main.CmsMenu;

/**
 * @author caoy
 * @version 创建时间：2016年9月6日 下午3:09:47 类说明
 */
public class MenuMngImpl extends Manager implements MenuMng {

	Logger logger = Logger.getLogger(this.getClass());

	// 站点完整菜单List
	private List<Map<String, Object>> list;

	public MenuMngImpl(String siteId) {
		StringBuffer sql = new StringBuffer(
				"select TZ_MENU_ID,TZ_SITE_ID,TZ_MENU_NAME,TZ_MENU_TYPE,TZ_MENU_LEVEL,TZ_MENU_XH,");
		sql.append("ifnull(TZ_F_MENU_ID,\"\") TZ_F_MENU_ID,");
		sql.append("ifnull(TZ_D_MENU_ID,\"\") TZ_D_MENU_ID,");
		sql.append("ifnull(TZ_MENU_PATH,\"\") TZ_MENU_PATH,");
		sql.append("ifnull(TZ_TEMP_ID,\"\") TZ_TEMP_ID,");
		sql.append("ifnull(TZ_PAGE_NAME,\"\") TZ_PAGE_NAME");
		sql.append(" from PS_TZ_SITEI_MENU_T");
		sql.append("where TZ_SITEI_ID=? ");
		try {
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			this.list = jdbcTemplate.queryForList(sql.toString(), new Object[] { siteId });
		} catch (Exception e) {
			this.list = null;
			e.printStackTrace();
		}
	}

	@Override
	public List<CmsMenu> findList(String siteId, String id) {
		List<CmsMenu> rsList = new ArrayList<CmsMenu>();

		if (list != null) {
			if (id == null || id.equals("")) {
				id = findRootMenu(siteId).getId();
			}

			String TZ_F_MENU_ID = null;
			Map<String, Object> mapNode = null;
			for (Object objNode : list) {
				mapNode = (Map<String, Object>) objNode;
				TZ_F_MENU_ID = mapNode.get("TZ_F_MENU_ID").toString();
				if (TZ_F_MENU_ID.equals(id)) {
					try {
						rsList.add(transMenu(mapNode));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

		// 对rsList排序
		if (rsList != null && rsList.size() > 0) {
			Collections.sort(rsList, new Comparator<CmsMenu>() {
				public int compare(CmsMenu arg0, CmsMenu arg1) {
					return arg0.getPriority().compareTo(arg1.getPriority());
				}
			});
		}
		return rsList;
	}

	@Override
	public CmsMenu findMenu(String id, String siteId) {
		String menuId = null;
		if (list != null) {
			Map<String, Object> mapNode = null;
			for (Object objNode : list) {
				mapNode = (Map<String, Object>) objNode;
				menuId = mapNode.get("TZ_MENU_ID").toString();
				if (menuId.equals(id)) {
					try {
						return transMenu(mapNode);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}

	public CmsMenu findRootMenu(String siteId) {
		if (list != null) {
			String TZ_MENU_LEVEL = null;
			Map<String, Object> mapNode = null;
			for (Object objNode : list) {
				mapNode = (Map<String, Object>) objNode;
				TZ_MENU_LEVEL = mapNode.get("TZ_MENU_LEVEL").toString();
				if (TZ_MENU_LEVEL.equals("0")) {
					try {
						return transMenu(mapNode);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}

	/**
	 * 得到BOOK菜单的链接地址
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unused")
	private String getBookURL(Map<String, Object> map) {
		String url = null;
		String TZ_D_MENU_ID = map.get("TZ_D_MENU_ID").toString();
		String thisPath = map.get("TZ_MENU_PATH").toString();
		String TZ_MENU_LEVEL = null;
		String rootPath = null;
		String menuId = null;
		String menuType = null;
		String pageName = null;
		if (list != null && TZ_D_MENU_ID != null && !TZ_D_MENU_ID.equals("")) {
			Map<String, Object> mapNode = null;
			for (Object objNode : list) {
				mapNode = (Map<String, Object>) objNode;
				TZ_MENU_LEVEL = mapNode.get("TZ_MENU_LEVEL").toString();

				if (TZ_MENU_LEVEL.equals("0")) {
					rootPath = mapNode.get("TZ_MENU_PATH").toString();
				}

				menuId = mapNode.get("TZ_MENU_ID").toString();
				menuType = mapNode.get("TZ_MENU_TYPE").toString();
				if (menuId.equals(TZ_D_MENU_ID) && menuType.equals("A")) {
					pageName = mapNode.get("TZ_PAGE_NAME").toString();
				}
			}

			if (pageName != null && !pageName.equals("")) {
				return rootPath + thisPath + "/" + pageName;
			}
		}
		return url;
	}

	/**
	 * 得到PAGE菜单的链接地址
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unused")
	private String getPageURL(Map<String, Object> map) {
		String url = null;
		String TZ_F_MENU_ID = map.get("TZ_F_MENU_ID").toString();
		String thisPath = null;
		String TZ_MENU_LEVEL = null;
		String rootPath = null;
		String menuId = null;
		String menuType = null;
		String pageName = map.get("TZ_PAGE_NAME").toString();
		if (list != null && TZ_F_MENU_ID != null && !TZ_F_MENU_ID.equals("")) {
			Map<String, Object> mapNode = null;
			for (Object objNode : list) {
				mapNode = (Map<String, Object>) objNode;
				TZ_MENU_LEVEL = mapNode.get("TZ_MENU_LEVEL").toString();

				if (TZ_MENU_LEVEL.equals("0")) {
					rootPath = mapNode.get("TZ_MENU_PATH").toString();
				}

				menuId = mapNode.get("TZ_F_MENU_ID").toString();
				if (menuId.equals(TZ_F_MENU_ID)) {
					thisPath = mapNode.get("TZ_MENU_PATH").toString();
				}
			}

			if (thisPath != null && !thisPath.equals("")) {
				return rootPath + thisPath + "/" + pageName;
			}
		}
		return url;
	}

	/**
	 * 得到上级BOOK的 path
	 * 
	 * @param menuId
	 * @return
	 */
	private String getFPath(String FmenuId) {
		String TZ_MENU_ID = null;
		Map<String, Object> mapNode = null;
		for (Object objNode : list) {
			mapNode = (Map<String, Object>) objNode;
			TZ_MENU_ID = mapNode.get("TZ_MENU_ID").toString();
			if (TZ_MENU_ID.equals(FmenuId)) {
				return mapNode.get("TZ_MENU_PATH").toString();
			}
		}
		return "";
	}

	@SuppressWarnings("unused")
	private CmsMenu transMenu(Map<String, Object> map) throws IOException {
		CmsMenu menu = new CmsMenu();
		// 菜单ID
		menu.setId((String) map.get("TZ_MENU_ID"));
		// 站点ID
		menu.setSiteId((String) map.get("TZ_SITE_ID"));
		// 父级菜单ID
		menu.setParentId((String) map.get("TZ_PARMENU_ID"));

		// 菜单类型 PS版本是 0 PAGE 1 BOOK
		menu.setType((String) map.get("TZ_MENU_TYPE"));

		// 静态页相对位置 A:PAGE B:BOOK
		// 如果是BOOK 显示BOOK 的path
		// 如果是PAGE 显示他上级BOOK的 path
		if (menu.getType().equals("A")) {
			menu.setStaticpath(getFPath((String) map.get("TZ_F_MENU_ID")));
		} else {
			menu.setStaticpath((String) map.get("TZ_MENU_PATH"));
		}

		// 菜单名称
		menu.setName((String) map.get("TZ_MENU_NAME"));
		// 是否显示 0 是 1 否
		menu.setShow("0"); // 默认显示

		// 序号
		menu.setPriority(new Integer((String) map.get("TZ_MENU_XH")));
		// 菜单模板ID
		menu.setTmpId((String) map.get("TZ_TEMP_ID"));
		// 专题页编号
		menu.setSpeId("");

		// 静态页面URL 这个需要增加 A:PAGE B:BOOK
		if (menu.getType().equals("A")) {
			menu.setStaticUrl(getPageURL(map));
		} else {
			menu.setStaticUrl(getBookURL(map));
		}
		// 打开方式 0 不弹出 1弹出
		menu.setTarget("0"); // 默认不弹出
		// 是否默认 0 非默认 1默认
		// menu.setDef("0");
		// 描述
		menu.setDesc("");
		// 菜单简称
		menu.setShortname("");
		// 菜单样式
		menu.setStyle("");

		menu.setLevel((String) map.get("TZ_MENU_LEVEL"));
		
		menu.setPageName((String) map.get("TZ_PAGE_NAME"));

		menu.setDefaultId((String) map.get("TZ_D_MENU_ID"));

		return menu;
	}

}
