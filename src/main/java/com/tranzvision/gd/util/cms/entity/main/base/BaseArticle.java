package com.tranzvision.gd.util.cms.entity.main.base;

import java.io.Serializable;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.tranzvision.gd.util.cms.web.FrontUtils;

@SuppressWarnings("serial")
public class BaseArticle implements Serializable {

	private String siteId;		//站点编号
	private String chnlId;      //栏目编号
	private String chnlName;      //栏目编号
	private String id; // 文章ID
	private String title; // 文章标题
	private String styleTile; // 带样式的文章标题
	private String content; // 文章内容
	private String name; // 文章名称
	private String type; // 文章类型
	private String outUrl; // 外部链接URL
	private String titleImageSysfileName;/*标题图系统文件名*/
	private String imageTitle;/*标题图标题*/
	private String imageDesc;/*标题图描述*/
	private String imageName;/*标题图名称*/
	private String imagePurl;/*标题图存储地址*/
	private String imageAurl;/*标题图访问地址*/
	private String ysName;		//压缩图
	private String slName;		//缩略图
	
	private Date startDate; // 开始日期
	private Date endDate; // 结束日期
	private Date startTime; // 开始时间
	private Date endTime; // 结束时间
	
	private String pubstate; // 文章发布状态
	private Date published; // 发布时间
	private String artUrl; // 文章URL
	private String url; // 文章静态URL
	private String html; // 文章HTML代码
	private String contentSCR; // 文章发布HTML代码

	private int order; // 顺序权重
	private long maxOrder; // 顺序权重
	
	private String publisher;/*发布人*/
	private String artDept;/*发布部门*/
	private String Modifier;/*修改人*/
	private Date Updated;/*更新时间*/

	private String digest; // 摘要
	private String about; // 简介
	
	private String nactid; // 活动编号
	
	private String openActApp;// 是否启用活动报名
	
	public BaseArticle() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



	public String getChnlId() {
		return chnlId;
	}

	public void setChnlId(String chnlId) {
		this.chnlId = chnlId;
	}



	public String getPubstate() {
		return pubstate;
	}

	public void setPubstate(String pubstate) {
		this.pubstate = pubstate;
	}

	public Date getPublished() {
		if (published == null) {
			published = new Date();
		}
		return published;
	}

	public void setPublished(Date published) {
		this.published = published;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getStyleTile() {
		return styleTile;
	}

	public void setStyleTile(String styleTile) {
		this.styleTile = styleTile;
	}

	public String getOutUrl() {
		return outUrl;
	}

	public void setOutUrl(String outUrl) {
		this.outUrl = outUrl;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContentSCR() {
		return contentSCR;
	}

	public void setContentSCR(String contentSCR) {
		this.contentSCR = contentSCR;
	}

	public String getDigest() {
		return digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}

	public String getArtUrl() {
		return artUrl;
	}

	public void setArtUrl(String artUrl) {
		this.artUrl = artUrl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAbout() {
		if (StringUtils.isBlank(about)){
			about = FrontUtils.replaceHtml(this.content);
			about = StringUtils.substring(about, 0, 500);
			return StringUtils.replace(about, "&nbsp;", " ");
		}
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getNactid() {
		return nactid;
	}

	public void setNactid(String nactid) {
		this.nactid = nactid;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setTitleImageSysfileName(String titleImageSysfileName) {
		this.titleImageSysfileName = titleImageSysfileName;
	}

	public String getTitleImageSysfileName() {
		return titleImageSysfileName;
	}

	

	public void setImageTitle(String imageTitle) {
		this.imageTitle = imageTitle;
	} 

	public String getImageTitle() {
		return imageTitle;
	}

	public void setImageDesc(String imageDesc) {
		this.imageDesc = imageDesc;
	}

	public String getImageDesc() {
		return imageDesc;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImagePurl(String imagePurl) {
		this.imagePurl = imagePurl;
	}

	public String getImagePurl() {
		return imagePurl;
	}

	public void setImageAurl(String imageAurl) {
		this.imageAurl = imageAurl;
	}

	public String getImageAurl() {
		return imageAurl;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setMaxOrder(long maxOrder) {
		this.maxOrder = maxOrder;
	}

	public long getMaxOrder() {
		return maxOrder;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setArtDept(String artDept) {
		this.artDept = artDept;
	}

	public String getArtDept() {
		return artDept;
	}

	public void setModifier(String modifier) {
		Modifier = modifier;
	}

	public String getModifier() {
		return Modifier;
	}

	public void setUpdated(Date updated) {
		Updated = updated;
	}

	public void setOpenActApp(String openActApp) {
		this.openActApp = openActApp;
	}

	public String getOpenActApp() {
		return openActApp;
	}

	public Date getUpdated() {
		return Updated;
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

	public void setChnlName(String chnlName) {
		this.chnlName = chnlName;
	}

	public String getChnlName() {
		return chnlName;
	}

}
