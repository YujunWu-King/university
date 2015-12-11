package com.tranzvision.gd.TZEmailSmsSendBundle.service.impl;

import org.springframework.jdbc.core.JdbcTemplate;

import com.tranzvision.gd.util.base.GetSpringBeanUtil;

/**
 * PS类:  TZ_GD_COM_EMLSMS_APP:emlSmsGetParamter
 * @author tang
 * 邮件短信获得公共参数的功能
 * 
 */
public class EmlSmsGetParamter {
	//所有邮件、短信发送的参数都以各自创建TZ_AUDCYUAN_T表听众数据中的听众id和听众成员id为参数;
	public String getName(String[] paramters){
		try{
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil(); 
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			String sql = "select TZ_AUD_XM from PS_TZ_AUDCYUAN_T where TZ_AUDIENCE_ID=? and  TZ_AUDCY_ID=?";
			String audId = paramters[0];
			String audCyId = paramters[1];
			String name = jdbcTemplate.queryForObject(sql, String.class, new Object[]{audId,audCyId});
			
			return name;
		}catch(Exception e){
			return "";
		}
	}
}
