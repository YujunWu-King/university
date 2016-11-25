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
import com.tranzvision.gd.util.cms.entity.main.CmsContent;
import com.tranzvision.gd.util.cms.entity.main.CmsMenu;

/**
 * @author caoy
 * @version 创建时间：2016年9月6日 下午3:09:47 类说明
 */
public class MenuMngImpl extends Manager implements MenuMng {

	Logger logger = Logger.getLogger(this.getClass());

	// 站点完整菜单List
	private List<Map<String, Object>> list;

	public MenuMngImpl() {

	}

	public String getSitePath(String siteId) {
		String sql = "select TZ_SITEI_PATH from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=? ";
		try {
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			List<Map<String, Object>> pathList = jdbcTemplate.queryForList(sql.toString(), new Object[] { siteId });
			if (pathList != null && pathList.size() == 1) {
				Map<String, Object> mapNode = pathList.get(0);
				if (mapNode.get("TZ_SITEI_PATH") != null) {
					String path = mapNode.get("TZ_SITEI_PATH").toString();
					if (!path.equals("/")) {
						if (!path.endsWith("/")) {
							path = path + "/";
						}
					}
					return path;
				} else {
					return "";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public MenuMngImpl(String siteId) {
		StringBuffer sql = new StringBuffer(
				"select TZ_MENU_ID,TZ_SITEI_ID,TZ_MENU_NAME,TZ_MENU_TYPE,TZ_MENU_LEVEL,TZ_MENU_XH,");
		sql.append("ifnull(TZ_F_MENU_ID,\"\") TZ_F_MENU_ID,");
		sql.append("ifnull(TZ_D_MENU_ID,\"\") TZ_D_MENU_ID,");
		sql.append("ifnull(TZ_MENU_PATH,\"\") TZ_MENU_PATH,");
		sql.append("ifnull(TZ_TEMP_ID,\"\") TZ_TEMP_ID,");
		sql.append("ifnull(TZ_PAGE_NAME,\"\") TZ_PAGE_NAME,");
		sql.append("ifnull(TZ_MENU_STYLE,\"\") TZ_MENU_STYLE,");
		sql.append("ifnull(TZ_MENU_SHOW,\"\") TZ_MENU_SHOW,");
		sql.append("ifnull(TZ_ATTACHSYSFILENA,\"\") TZ_ATTACHSYSFILENA,");
		sql.append("ifnull(TZ_IMAGE_TITLE,\"\") TZ_IMAGE_TITLE,");
		sql.append("ifnull(TZ_IMAGE_DESC,\"\") TZ_IMAGE_DESC");
		sql.append(" from PS_TZ_SITEI_MENU_T");
		sql.append(" where TZ_SITEI_ID=?");
		try {
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			this.list = jdbcTemplate.queryForList(sql.toString(), new Object[] { siteId });
		} catch (Exception e) {
			this.list = null;
			e.printStackTrace();
		}
	}

	public MenuMngImpl(List<Map<String, Object>> menu) {
		this.list = menu;
	}

	public List<CmsMenu> findRecallList(String siteId, String id) {
		List<CmsMenu> rsList = new ArrayList<CmsMenu>();
		if (list != null) {
			findRecall(id, siteId, rsList);

			// 对rsList排序
			if (rsList != null && rsList.size() > 0) {
				Collections.sort(rsList, new Comparator<CmsMenu>() {
					public int compare(CmsMenu arg0, CmsMenu arg1) {
						return arg0.getLevel().compareTo(arg1.getLevel());
					}
				});
			}
		}
		return rsList;
	}

	/**
	 * 回溯递归 ，根据子找到所有上层父
	 * 
	 * @param id
	 * @param siteId
	 * @param rsList
	 */
	private void findRecall(String id, String siteId, List<CmsMenu> rsList) {
		CmsMenu cm = findMenu(id, siteId);
		rsList.add(cm);
		if (cm.getLevel().equals("1")) {
			return;
		} else {
			findRecall(cm.getParentId(), siteId, rsList);
		}
	}

	/**
	 * 找到菜单标题图路径
	 * 
	 * @param siteId
	 * @return
	 */
	private String getTitle(String titleSysFileId) {
		String titleSysFileUrl = null;
		// 标题图;
		if (titleSysFileId != null && !"".equals(titleSysFileId)) {
			String titleSQL = "select C.TZ_ATTACHFILE_NAME," + " C.TZ_ATT_P_URL,C.TZ_ATT_A_URL,"
					+ " C.TZ_YS_ATTACHSYSNAM,C.TZ_SL_ATTACHSYSNAM" + " from PS_TZ_ART_TITIMG_T C "
					+ " where TZ_ATTACHSYSFILENA=?";
			try {
				GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
				JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
				Map<String, Object> titleMap = jdbcTemplate.queryForMap(titleSQL, new Object[] { titleSysFileId });
				if (titleMap != null) {

					String imagePathA = (String) titleMap.get("TZ_ATT_A_URL");
					if (!imagePathA.trim().endsWith("/")) {
						imagePathA = imagePathA + "/";
					}
					titleSysFileUrl = imagePathA + titleSysFileId;

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//System.out.println("titleSysFileUrl:" + titleSysFileUrl);
		return titleSysFileUrl;
	}

	@Override
	public List<CmsMenu> findList(String siteId, String id) {
		List<CmsMenu> rsList = new ArrayList<CmsMenu>();

		if (list != null) {
			if (id == null || id.equals("")) {
				id = findRootMenu(siteId).getId();
			}

			String TZ_F_MENU_ID = null;
			String TZ_MENU_SHOW = null;
			Map<String, Object> mapNode = null;
			for (Object objNode : list) {
				mapNode = (Map<String, Object>) objNode;
				TZ_F_MENU_ID = mapNode.get("TZ_F_MENU_ID").toString();
				TZ_MENU_SHOW = mapNode.get("TZ_MENU_SHOW").toString();
				if (TZ_F_MENU_ID.equals(id) && TZ_MENU_SHOW.equals("Y")) {
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
		if (list != null && id != null && !id.equals("")) {
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
	 * 链接地址
	 * 
	 * @param path
	 * @param menuType
	 *            // A:PAGE B:BOOK
	 * @param menuId
	 * @param strFileName
	 * @return
	 */
	private String getURL(String path, String menuType, String menuId, String strFileName) {
		String url = null;
		String pageNme = null;

		// System.out.println("menuType：" + menuType);
		if (menuType.equals("A")) {
			pageNme = strFileName;
			// 如果是http开头的 说明是外部连接 直接返回
			if (strFileName != null && !strFileName.equals("")) {
				if (strFileName.toLowerCase().startsWith("http")) {
					return strFileName;
				}
			}
		} else {
			// 如果是BOOK 需要找到他默认页面的 页面名称
			// System.out.println("menuId：" + menuId);

			Map<String, Object> mapNode = null;
			String TZ_MENU_ID = null;
			String TZ_MENU_TYPE = null;
			for (Object objNode : list) {
				mapNode = (Map<String, Object>) objNode;

				TZ_MENU_ID = mapNode.get("TZ_MENU_ID").toString();
				TZ_MENU_TYPE = mapNode.get("TZ_MENU_TYPE").toString();
				if (TZ_MENU_ID.equals(menuId) && TZ_MENU_TYPE.equals("A")) {
					pageNme = mapNode.get("TZ_PAGE_NAME").toString();
				}
			}
		}

		// System.out.println("pageNme：" + pageNme);
		if (pageNme != null && !pageNme.equals("")) {
			if (!pageNme.toLowerCase().endsWith(".html")) {
				pageNme = pageNme + ".html";
			}
			url = path + "/" + pageNme;
		}
		// System.out.println("url:" + url);
		return url;
	}

	/**
	 * 得到path
	 * 
	 * @param FmenuId
	 * @param menuType
	 *            A:PAGE B:BOOK
	 * @return
	 */
	private String getPath(String menuId, String menuType, String menuPath) {
		// 根路径
		String strBasePath = "";
		// 本级栏目路径
		String strMenuPath = "";

		if (menuType.equals("B")) {
			strMenuPath = menuPath;
		}

		int flag = -1;

		String TZ_MENU_ID = null;
		String TZ_MENU_LEVEL = null;
		String TZ_MENU_TYPE = null;
		Map<String, Object> mapNode = null;
		for (Object objNode : list) {
			mapNode = (Map<String, Object>) objNode;
			TZ_MENU_LEVEL = mapNode.get("TZ_MENU_LEVEL").toString();
			// 获取根目录
			if (TZ_MENU_LEVEL.equals("0")) {
				strBasePath = mapNode.get("TZ_MENU_PATH").toString();
				TZ_MENU_ID = mapNode.get("TZ_MENU_ID").toString();
				// 根目录
				if (TZ_MENU_ID.equals(menuId)) {
					flag = 1;
				}
			}

			// 如果是PAGE 需要获取上级BOOK的路径
			if (menuType.equals("A")) {
				TZ_MENU_ID = mapNode.get("TZ_MENU_ID").toString();
				TZ_MENU_TYPE = mapNode.get("TZ_MENU_TYPE").toString();
				if (TZ_MENU_ID.equals(menuId) && TZ_MENU_TYPE.equals("B")) {
					strMenuPath = mapNode.get("TZ_MENU_PATH").toString();
				}
			}
		}

		// 静态化路径
		String strFilePath = "";
		if (strBasePath != null && !"".equals(strBasePath)) {
			if (!strBasePath.startsWith("/")) {
				strBasePath = "/" + strBasePath;
			}
			if (strBasePath.endsWith("/")) {
				strBasePath = strBasePath.substring(0, strBasePath.length() - 1);
			}
		}
		if (strMenuPath != null && !"".equals(strMenuPath)) {
			if (!strMenuPath.startsWith("/")) {
				strMenuPath = "/" + strMenuPath;
			}
			if (strMenuPath.endsWith("/")) {
				strMenuPath = strMenuPath.substring(0, strMenuPath.length() - 1);
			}
		}
		if (flag == 1) {
			// 根目录 不需要再加了
			strFilePath = strBasePath;
		} else {
			strFilePath = strBasePath + strMenuPath;
		}

		// System.out.println("strFilePath:" + strFilePath);
		return strFilePath;
	}

	private CmsMenu transMenu(Map<String, Object> map) throws IOException {
		CmsMenu menu = new CmsMenu();
		// 菜单ID
		menu.setId((String) map.get("TZ_MENU_ID"));
		// 站点ID
		menu.setSiteId((String) map.get("TZ_SITE_ID"));
		// 父级菜单ID
		menu.setParentId((String) map.get("TZ_F_MENU_ID"));

		// 菜单类型 PS版本是 0 PAGE 1 BOOK
		menu.setType((String) map.get("TZ_MENU_TYPE"));

		// 静态页相对位置 A:PAGE B:BOOK
		// 如果是BOOK 显示BOOK 的path
		// 如果是PAGE 显示他上级BOOK的 path
		if (menu.getType().equals("A")) {
			menu.setStaticpath(getPath(map.get("TZ_F_MENU_ID").toString(), "A", ""));
		} else {
			menu.setStaticpath(getPath(menu.getId(), "B", map.get("TZ_MENU_PATH").toString()));
		}

		// 菜单名称
		menu.setName((String) map.get("TZ_MENU_NAME"));
		// 是否显示 0 是 1 否
		menu.setShow("0"); // 默认显示

		// 序号
		menu.setPriority(new Integer(map.get("TZ_MENU_XH").toString()));
		// 菜单模板ID
		menu.setTmpId((String) map.get("TZ_TEMP_ID"));
		// 专题页编号
		menu.setSpeId("");

		menu.setPageName((String) map.get("TZ_PAGE_NAME"));
		menu.setDefaultId((String) map.get("TZ_D_MENU_ID"));

		// 静态页面URL 这个需要增加 A:PAGE B:BOOK
		// private String getURL(String path, String menuType, String menuId,
		// String strFileName)
		if (menu.getType().equals("A")) {
			menu.setStaticUrl(getURL(menu.getStaticpath(), "A", "", menu.getPageName()));
		} else {
			menu.setStaticUrl(getURL(menu.getStaticpath(), "B", menu.getDefaultId(), ""));
		}
		// 打开方式 0 不弹出 1弹出
		// 如果是http开头的连接，默认 弹出
		if (menu.getStaticUrl() != null && !menu.getStaticUrl().equals("")
				&& menu.getStaticUrl().toLowerCase().startsWith("http")) {
			menu.setTarget("1");
		} else {
			menu.setTarget("0"); // 默认不弹出
		}

		// 是否默认 0 非默认 1默认
		// menu.setDef("0");
		// 描述
		menu.setDesc("");
		// 菜单简称
		menu.setShortname("");
		// 菜单样式
		if (map.get("TZ_MENU_STYLE") != null) {
			menu.setStyle(map.get("TZ_MENU_STYLE").toString());
		}

		menu.setLevel(map.get("TZ_MENU_LEVEL").toString());

		// 标题图地址
		if (map.get("TZ_ATTACHSYSFILENA") != null) {

			String titleSysFileId = map.get("TZ_ATTACHSYSFILENA").toString();
			// System.out.println("titleSysFileId:"+titleSysFileId);
			if (titleSysFileId != null && !titleSysFileId.equals("")) {
				menu.setTitleUrl(this.getTitle(titleSysFileId));
			}
		}
		if (map.get("TZ_IMAGE_TITLE") != null) {
			menu.setTitle(map.get("TZ_IMAGE_TITLE").toString());
		}
		if (map.get("TZ_IMAGE_DESC") != null) {
			menu.setTitleDes(map.get("TZ_IMAGE_DESC").toString());
		}

		return menu;
	}

}
