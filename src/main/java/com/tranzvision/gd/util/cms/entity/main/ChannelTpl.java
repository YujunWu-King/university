package com.tranzvision.gd.util.cms.entity.main;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ChannelTpl implements Serializable {
	private String chnlid;
	private String tplid;
	private String siteId;
	
	
	
	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getChnlid() {
		return chnlid;
	}

	public void setChnlid(String chnlid) {
		this.chnlid = chnlid;
	}

	public String getTplid() {
		return tplid;
	}

	public void setTplid(String tplid) {
		this.tplid = tplid;
	}

	public ChannelTpl(String chnlid, String tplid) {
		super();
		this.chnlid = chnlid;
		this.tplid = tplid;
	}

	public ChannelTpl() {
		super();
		// TODO Auto-generated constructor stub
	}

}
