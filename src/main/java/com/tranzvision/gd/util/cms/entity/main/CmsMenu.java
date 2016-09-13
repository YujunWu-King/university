package com.tranzvision.gd.util.cms.entity.main;

import com.tranzvision.gd.util.cms.entity.main.base.BaseMenu;

/**
 * @author caoy
 * @version 创建时间：2016年9月6日 下午3:10:48 类说明
 */

@SuppressWarnings("serial")
public class CmsMenu extends BaseMenu {
	private String level; // 层级

	private String pageName; // PAGE名称

	private String defaultId; // 默认下级Page的菜单ID

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public CmsMenu() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CmsMenu(String id) {
		super(id);
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public String getDefaultId() {
		return defaultId;
	}

	public void setDefaultId(String defaultId) {
		this.defaultId = defaultId;
	}

}
