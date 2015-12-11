package com.tranzvision.gd.util.cms.entity.main.base;

import java.io.Serializable;

@SuppressWarnings("serial")
public class BaseAttachment implements Serializable {
	private String id;		//附件ID
	private String artid;	//文章ID
	private String title;	//附件标题
	private String url;		//附件访问地址
	private String purl;		//附件存放地址
	

	public BaseAttachment() {
		super();
		// TODO Auto-generated constructor stub
	}
	public BaseAttachment(String id, String artid, String title, String url,String purl) {
		super();
		this.id = id;
		this.artid = artid;
		this.title = title;
		this.url = url;
		this.purl = purl;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getArtid() {
		return artid;
	}
	public void setArtid(String artid) {
		this.artid = artid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public void setPurl(String purl) {
		this.purl = purl;
	}
	public String getPurl() {
		return purl;
	}
	
	
}
