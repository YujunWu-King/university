package com.tranzvision.gd.TZZnxTemplateBundle.service.impl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.encrypt.DESUtil;

/**
 * PS类: TZ_GD_COM_EMLSMS_APP:emlSmsGetParamter
 * 
 * @author tang 站内信模板定义发送获得公共参数的功能
 * 
 */
public class ZnxGetParamter {
	// 所有邮件、短信发送的参数都以各自创建TZ_AUDCYUAN_T表听众数据中的听众id和听众成员id为参数;
	public String getName(String[] paramters) {
		try {
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			String sql = "select TZ_AUD_XM from PS_TZ_AUDCYUAN_T where TZ_AUDIENCE_ID=? and  TZ_AUDCY_ID=?";
			String audId = paramters[0];
			String audCyId = paramters[1];
			String name = jdbcTemplate.queryForObject(sql, String.class, new Object[] { audId, audCyId });
			return name;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	// 获取活动报名活动时间;
	public String getActTime(String[] paramters) {
		try {
			String audId = paramters[0];
			String audCyId = paramters[1];

			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			//活动ID;
			String opridSQL = "SELECT TZ_HUOD_ID FROM PS_TZ_AUDCYUAN_T WHERE TZ_AUDIENCE_ID=? AND TZ_AUDCY_ID=?";
			String strActId = jdbcTemplate.queryForObject(opridSQL, String.class, new Object[] { audId, audCyId });
			if (strActId != null && !"".equals(strActId)) {
				String actDtSql = "SELECT DATE_FORMAT( TZ_START_DT, '%Y-%m-%d') FROM PS_TZ_ART_HD_TBL WHERE TZ_ART_ID =?";
				String actDt = jdbcTemplate.queryForObject(actDtSql, String.class, new Object[] { strActId });
				return actDt;
			}else{
				return "";
			} 
		}catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	// 获取活动报名活动地点;
	public String getActLocation(String[] paramters) {
		try {
			String audId = paramters[0];
			String audCyId = paramters[1];

			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			// 活动ID;
			String opridSQL = "SELECT TZ_HUOD_ID FROM PS_TZ_AUDCYUAN_T WHERE TZ_AUDIENCE_ID=? AND TZ_AUDCY_ID=?";
			String strActId = jdbcTemplate.queryForObject(opridSQL, String.class, new Object[] { audId, audCyId });
			if (strActId != null && !"".equals(strActId)) {
				String actAddrsql = "SELECT TZ_NACT_ADDR FROM PS_TZ_ART_HD_TBL WHERE TZ_ART_ID =?";
				String actAddr = jdbcTemplate.queryForObject(actAddrsql, String.class, new Object[] { strActId });
				return actAddr;
			} else {
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	// 获取活动报名活动名称;
	public String getActName(String[] paramters) {
		try {
			String audId = paramters[0];
			String audCyId = paramters[1];

			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			// 活动ID;
			String opridSQL = "SELECT TZ_HUOD_ID FROM PS_TZ_AUDCYUAN_T WHERE TZ_AUDIENCE_ID=? AND TZ_AUDCY_ID=?";
			String strActId = jdbcTemplate.queryForObject(opridSQL, String.class, new Object[] { audId, audCyId });
			if (strActId != null && !"".equals(strActId)) {
				String actNamesql = "SELECT TZ_NACT_NAME FROM PS_TZ_ART_HD_TBL WHERE TZ_ART_ID =?";
				String actName = jdbcTemplate.queryForObject(actNamesql, String.class, new Object[] { strActId });
				return actName;
			} else {
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	// 获得推荐人姓名;
	public String getTjxName(String[] paramters) {
		try {
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			//获取报名表ID;
			String sql = "SELECT TZ_BMB_ID FROM PS_TZ_AUDCYUAN_T WHERE TZ_AUDIENCE_ID=? AND  TZ_AUDCY_ID=?";
			String audId = paramters[0];
			String audCyId = paramters[1];
			String bmbId = jdbcTemplate.queryForObject(sql, String.class, new Object[] { audId, audCyId });
			if (bmbId == null || "".equals(bmbId)) {
				return "";
			} else {
				String referrerSql = "SELECT TZ_REFERRER_NAME FROM PS_TZ_KS_TJX_TBL WHERE TZ_REF_LETTER_ID =?";
				String referrerName = jdbcTemplate.queryForObject(referrerSql, String.class, new Object[] { bmbId });
				if (referrerName == null|| "".equals(referrerName)){
					return "";
				}else{
					return referrerName;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

}
