package com.tranzvision.gd.util.cms.entity.main.base;

import java.io.Serializable;

@SuppressWarnings("serial")
public class BaseTemplate implements Serializable {
	
	private String siteId; // 站点编号
	private String id; // 模板编号
	private String name; // 模板名称
	private String state; // 模板状态
	private String type; // 模板类型
	private String pcContent; // pc版本模板内容
	private String msContent; // 手机版本模板内容
	
	public BaseTemplate() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPcContent() {
		return pcContent;
	}

	public void setPcContent(String pcContent) {
		this.pcContent = pcContent;
	}
	
	public String getMsContent() {
		return msContent;
	}

	public void setMsContent(String msContent) {
		this.msContent = msContent;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getState() {
		return state;
	}
}
