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

	private String titleUrl; // 标题图路径

	private String title; // 标题图名称

	private String titleDes; // 标题图描述

	public String getTitleUrl() {
		return titleUrl;
	}

	public void setTitleUrl(String titleUrl) {
		this.titleUrl = titleUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitleDes() {
		return titleDes;
	}

	public void setTitleDes(String titleDes) {
		this.titleDes = titleDes;
	}

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
