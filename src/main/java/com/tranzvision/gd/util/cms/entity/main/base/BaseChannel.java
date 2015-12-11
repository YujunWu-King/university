package com.tranzvision.gd.util.cms.entity.main.base;

import java.io.Serializable;

@SuppressWarnings("serial")
public class BaseChannel implements Serializable {
	
	private String siteId;		//站点编号
	private String id;			//栏目ID
	private String coluName;		//栏目名称
	private String coluTplid;		//栏目模板ID
	private String coluType;	//栏目类型
	private String artTplId;	//内容模板ID
	private String artType;		//内容类型
	private String menuTypeId;		//所属菜单类型

	
	
	public BaseChannel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getArtTplId() {
		return artTplId;
	}

	public void setArtTplId(String artTplId) {
		this.artTplId = artTplId;
	}

	public String getArtType() {
		return artType;
	}

	public void setArtType(String artType) {
		this.artType = artType;
	}


	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public void setColuName(String coluName) {
		this.coluName = coluName;
	}

	public String getColuName() {
		return coluName;
	}

	public void setColuTplid(String coluTplid) {
		this.coluTplid = coluTplid;
	}

	public String getColuTplid() {
		return coluTplid;
	}

	public void setColuType(String coluType) {
		this.coluType = coluType;
	}

	public String getColuType() {
		return coluType;
	}

	public void setMenuTypeId(String menuTypeId) {
		this.menuTypeId = menuTypeId;
	}

	public String getMenuTypeId() {
		return menuTypeId;
	}


}
