package com.tranzvision.gd.util.cms.entity.main.base;

import java.io.Serializable;
/** 
* @author  caoy
* @version 创建时间：2016年9月6日 下午3:11:33 
* 类说明 
*/


@SuppressWarnings("serial")
public class BaseMenu implements Serializable {

	private String id;			//菜单ID
	private String siteId;		//站点ID
	private String parentId;	//父级菜单ID
	private String staticpath;	//静态页相对位置
	private String name;		//菜单名称
	private String show;		//是否显示
	private String type;		//菜单类型
	private Integer  priority;		//序号
	private String tmpId;		//菜单模板ID
	private String speId;		//专题页编号
	private String url;			//Page URL
	private String staticUrl;	//静态页面URL
	private String target;		//打开方式
	private String def;			//是否默认
	private String desc;		//描述
	
	private String shortname;	//菜单简称
	private String style;		//菜单样式
	
	public BaseMenu() {
		super();
		// TODO Auto-generated constructor stub
	}
	

	public BaseMenu(String id) {
		super();
		this.id = id;
	}



	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getShow() {
		return show;
	}
	public void setShow(String show) {
		this.show = show;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public String getTmpId() {
		return tmpId;
	}
	public void setTmpId(String tmpId) {
		this.tmpId = tmpId;
	}
	public String getSpeId() {
		return speId;
	}
	public void setSpeId(String speId) {
		this.speId = speId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getStaticUrl() {
		return staticUrl;
	}
	public void setStaticUrl(String staticUrl) {
		this.staticUrl = staticUrl;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getDef() {
		return def;
	}
	public void setDef(String def) {
		this.def = def;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getStaticpath() {
		return staticpath;
	}
	public void setStaticpath(String staticpath) {
		this.staticpath = staticpath;
	}


	public String getShortname() {
		return shortname;
	}


	public void setShortname(String shortname) {
		this.shortname = shortname;
	}


	public String getStyle() {
		return style;
	}


	public void setStyle(String style) {
		this.style = style;
	}
	
}
