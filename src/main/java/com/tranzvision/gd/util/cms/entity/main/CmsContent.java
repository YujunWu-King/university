package com.tranzvision.gd.util.cms.entity.main;

import java.util.Date;
import java.text.SimpleDateFormat;

import org.apache.commons.lang3.StringUtils;

import com.tranzvision.gd.util.cms.entity.main.base.BaseArticle;

public class CmsContent extends BaseArticle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String metakeys;
	private String metadesc;
	private String art_shorttitle;
	private String subhead;
	private String txt1;
	private String txt2;
	private String txt3;
	private String txt4;
	private String long1;
	private String long2;
	private String long3;
	private Date date1;
	private Date date2;
	private Date date3;

	private String hd_city; // 活动城市
	private String hd_address; // 活动地点

	private int hd_totalNumber; // 活动总数
	private int hd_activeNumber; // 活动报名人数

	private int hd_watingNumber; // 可报名人数

	public int getHd_totalNumber() {
		return hd_totalNumber;
	}

	public void setHd_totalNumber(int hd_totalNumber) {
		this.hd_totalNumber = hd_totalNumber;
	}

	public int getHd_activeNumber() {
		return hd_activeNumber;
	}

	public void setHd_activeNumber(int hd_activeNumber) {
		this.hd_activeNumber = hd_activeNumber;
	}

	public int getHd_watingNumber() {
		return hd_watingNumber;
	}

	public void setHd_watingNumber(int hd_watingNumber) {
		this.hd_watingNumber = hd_watingNumber;
	}

	public String getHd_city() {
		return hd_city;
	}

	public void setHd_city(String hd_city) {
		this.hd_city = hd_city;
	}

	public String getHd_address() {
		return hd_address;
	}

	public void setHd_address(String hd_address) {
		this.hd_address = hd_address;
	}

	public String getMetakeys() {
		return metakeys;
	}

	public void setMetakeys(String metakeys) {
		this.metakeys = metakeys;
	}

	public String getMetadesc() {
		return metadesc;
	}

	public void setMetadesc(String metadesc) {
		this.metadesc = metadesc;
	}

	public String getArt_shorttitle() {
		return art_shorttitle;
	}

	public void setArt_shorttitle(String art_shorttitle) {
		this.art_shorttitle = art_shorttitle;
	}

	public String getSubhead() {
		return subhead;
	}

	public void setSubhead(String subhead) {
		this.subhead = subhead;
	}

	public String getTxt1() {
		return txt1;
	}

	public void setTxt1(String txt1) {
		this.txt1 = txt1;
	}

	public String getTxt2() {
		return txt2;
	}

	public void setTxt2(String txt2) {
		this.txt2 = txt2;
	}

	public String getTxt3() {
		return txt3;
	}

	public void setTxt3(String txt3) {
		this.txt3 = txt3;
	}

	public String getTxt4() {
		return txt4;
	}

	public void setTxt4(String txt4) {
		this.txt4 = txt4;
	}

	public String getLong1() {
		return long1;
	}

	public void setLong1(String long1) {
		this.long1 = long1;
	}

	public String getLong2() {
		return long2;
	}

	public void setLong2(String long2) {
		this.long2 = long2;
	}

	public String getLong3() {
		return long3;
	}

	public void setLong3(String long3) {
		this.long3 = long3;
	}

	public Date getDate1() {
		return date1;
	}

	public void setDate1(Date date1) {
		this.date1 = date1;
	}

	public Date getDate2() {
		return date2;
	}

	public void setDate2(Date date2) {
		this.date2 = date2;
	}

	public Date getDate3() {
		return date3;
	}

	public void setDate3(Date date3) {
		this.date3 = date3;
	}

	/**
	 * 发布时间
	 * 
	 * @param format
	 * @return
	 */
	public String date(String format) {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		if (StringUtils.equals(format, "1")) {

		}
		if (StringUtils.equals(format, "2")) {

		}
		if (StringUtils.equals(format, "3")) {

		}
		return formatter.format(this.getPublished());
	}

	/**
	 * 发生时间
	 * 
	 * @param format
	 * @return
	 */
	/*
	 * public String rDate(String format) { SimpleDateFormat formatter = new
	 * SimpleDateFormat("yyyy-MM-dd");
	 * 
	 * if (StringUtils.equals(format, "1")) {
	 * 
	 * } if (StringUtils.equals(format, "2")) {
	 * 
	 * } if (StringUtils.equals(format, "3")) {
	 * 
	 * } return formatter.format(this.getOccurtime()); }
	 */

}
