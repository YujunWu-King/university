package com.tranzvision.gd.util.cms.entity.main;

import java.text.SimpleDateFormat;

import org.apache.commons.lang3.StringUtils;

import com.tranzvision.gd.util.cms.entity.main.base.BaseArticle;

public class CmsContent extends BaseArticle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
	public String rDate(String format) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		if (StringUtils.equals(format, "1")) {

		}
		if (StringUtils.equals(format, "2")) {

		}
		if (StringUtils.equals(format, "3")) {

		}
		return formatter.format(this.getOccurtime());
	}
	*/

}
