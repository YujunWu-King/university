package com.tranzvision.gd.util.cms.entity.main.base;

import java.io.Serializable;

@SuppressWarnings("serial")
public class BaseArtImage implements Serializable {
	private String id;			//图片编号
	private String artId;		//文章编号
	private long priority;		//序号
	private String desc;		//图片描述
	private String url;			//图片URL
	private String purl;		//图片存放url
	private String link;		//图片链接（跳转URL）
	private String title;		//图片标题
	private String ysName;		//压缩图
	private String slName;		//缩略图
	private String smallImageUrl;	//压缩图URL
	private String bigImageUrl;		//原图URL
	
	public String getTitle() {
		return title;
	}
	public void setYsName(String ysName) {
		this.ysName = ysName;
	}
	public String getYsName() {
		return ysName;
	}
	public void setSlName(String slName) {
		this.slName = slName;
	}
	public String getSlName() {
		return slName;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public BaseArtImage() {
		super();
	}
	public BaseArtImage(String id, String artId, int priority, String title, String desc,
			String url,String purl,String link) {
		super();
		this.id = id;
		this.artId = artId;
		this.priority = priority;
		this.title = title;
		this.desc = desc;
		this.url = url;
		this.link = link;
		this.purl = purl;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getArtId() {
		return artId;
	}
	public void setArtId(String artId) {
		this.artId = artId;
	}
	public long getPriority() {
		return priority;
	}
	public void setPriority(long priority) {
		this.priority = priority;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public void setPurl(String purl) {
		this.purl = purl;
	}
	public String getPurl() {
		return purl;
	}
	public void setSmallImageUrl(String smallImageUrl) {
		this.smallImageUrl = smallImageUrl;
	}
	public String getSmallImageUrl() {
		return smallImageUrl;
	}
	public void setBigImageUrl(String bigImageUrl) {
		this.bigImageUrl = bigImageUrl;
	}
	public String getBigImageUrl() {
		return bigImageUrl;
	}

}
