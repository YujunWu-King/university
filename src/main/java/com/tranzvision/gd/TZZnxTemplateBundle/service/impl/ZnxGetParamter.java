package com.tranzvision.gd.TZZnxTemplateBundle.service.impl;

import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.tranzvision.gd.util.base.GetSpringBeanUtil;

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
			System.out.println("琚峰测试:"+audId+","+audCyId+":"+bmbId);
			if (bmbId != null && !"".equals(bmbId)){
				String referrerSql = "SELECT TZ_REFERRER_NAME FROM PS_TZ_KS_TJX_TBL WHERE TZ_REF_LETTER_ID =?";
				//String referrerSql = "SELECT TZ_REFERRER_NAME FROM PS_TZ_KS_TJX_TBL WHERE TZ_APP_INS_ID = ?";
				String referrerName = jdbcTemplate.queryForObject(referrerSql, String.class, new Object[] { bmbId });
				System.out.println("琚峰测试2:"+referrerName);
				return referrerName;
			}else{
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	//获取预报名人姓名
		public String getYbmName(String[] paramters){
			try{
				GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
				JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
				//获取报名表ID;
				String bmbIdsql = "SELECT TZ_BMB_ID FROM PS_TZ_AUDCYUAN_T WHERE TZ_AUDIENCE_ID=? AND  TZ_AUDCY_ID=?";
				String audId = paramters[0];
				String audCyId = paramters[1];
				String bmbId = jdbcTemplate.queryForObject(bmbIdsql, String.class, new Object[] { audId, audCyId });
				if (bmbId == null || "".equals(bmbId)) {
					return "";
				} else {
					String appNameSql = "select TZ_REALNAME from PS_TZ_AQ_YHXX_TBL where OPRID = (SELECT OPRID FROM PS_TZ_FORM_WRK_T WHERE TZ_APP_INS_ID = ?)";
					String appName = jdbcTemplate.queryForObject(appNameSql, String.class, new Object[] { bmbId });
					return appName;
				}
			}catch(Exception e){
				e.printStackTrace();
				return "";
			}
		}
		
		//获取报名表提交项目
		public String getprjName(String[] paramters){
			try{
				GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
				JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
				//获取报名表ID;
				String bmbIdsql = "SELECT TZ_BMB_ID FROM PS_TZ_AUDCYUAN_T WHERE TZ_AUDIENCE_ID=? AND  TZ_AUDCY_ID=?";
				String audId = paramters[0];
				String audCyId = paramters[1];
				String bmbId = jdbcTemplate.queryForObject(bmbIdsql, String.class, new Object[] { audId, audCyId });
				if (bmbId == null || "".equals(bmbId)) {
					return "";
				} else {
					// 获取classId;
					String classIdSql = "SELECT TZ_CLASS_ID FROM PS_TZ_FORM_WRK_T WHERE TZ_APP_INS_ID = ?";
					String classId = jdbcTemplate.queryForObject(classIdSql, String.class, new Object[] { bmbId });
					if (classId == null|| "".equals(classId)){
						return "";
					}else{
						String prgSql = "SELECT TZ_PRJ_NAME FROM TZ_GD_BJGL_VW WHERE TZ_CLASS_ID = ?";
						String prgName = jdbcTemplate.queryForObject(prgSql, String.class, new Object[] { classId });
						return prgName;
					}
				}
			}catch(Exception e){
				e.printStackTrace();
				return "";
			}
		}
		
		//获取报名表提交批次
		public String getbatchName(String[] paramters){
			try{
				GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
				JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
				//获取报名表ID;
				String bmbIdsql = "SELECT TZ_BMB_ID FROM PS_TZ_AUDCYUAN_T WHERE TZ_AUDIENCE_ID=? AND  TZ_AUDCY_ID=?";
				String audId = paramters[0];
				String audCyId = paramters[1];
				String bmbId = jdbcTemplate.queryForObject(bmbIdsql, String.class, new Object[] { audId, audCyId });
				if (bmbId != null && !"".equals(bmbId)) {
					// 获取classId;
					String classIdSql = "SELECT TZ_CLASS_ID FROM PS_TZ_FORM_WRK_T WHERE TZ_APP_INS_ID = ?";
					String classId = jdbcTemplate.queryForObject(classIdSql, String.class, new Object[] { bmbId });
					if (classId != null && !"".equals(classId)){
						String batchIdSql = "SELECT TZ_BATCH_ID FROM PS_TZ_FORM_WRK_T WHERE TZ_APP_INS_ID = ?";
						String batchId = jdbcTemplate.queryForObject(batchIdSql, String.class, new Object[] { bmbId });
						if (batchId != null && !"".equals(batchId)){
							String batchNameSql = "SELECT TZ_BATCH_NAME FROM PS_TZ_CLS_BATCH_T WHERE TZ_CLASS_ID = ? AND TZ_BATCH_ID = ?";
							String batchName = jdbcTemplate.queryForObject(batchNameSql, String.class, new Object[] { classId,batchId });
							return batchName;	
						}else{
							return "";
						}
					}else{
						return "";
					}
				} else {
					return "";
				}
			}catch(Exception e){
				e.printStackTrace();
				return "";
			}
		}
		
		
		//获取面试预约说明,时间地点
		public String getMsDesc(String[] paramters) {
			String msyyDescr = "";
			try {
				GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
				JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
				String sql = "select TZ_WEIXIN,TZ_XSXS_ID,TZ_HUOD_ID from PS_TZ_AUDCYUAN_T where TZ_AUDIENCE_ID=? and  TZ_AUDCY_ID=?";
				String audId = paramters[0];
				String audCyId = paramters[1];
				String pcId = "";
				String classId = "";
				String planId = "";
				Map<String, Object> msyyMap  = jdbcTemplate.queryForMap(sql,new Object[] { audId, audCyId });
				if(msyyMap != null){
					pcId = msyyMap.get("TZ_WEIXIN") == null ? "" : (String)msyyMap.get("TZ_WEIXIN");
					classId = msyyMap.get("TZ_XSXS_ID") == null ? "" : (String)msyyMap.get("TZ_XSXS_ID");
					planId = msyyMap.get("TZ_HUOD_ID") == null ? "" : (String)msyyMap.get("TZ_HUOD_ID");
				
					String msDescSql = "SELECT concat(date_format(TZ_MS_DATE,'%Y-%m-%d'),' ',date_format(TZ_START_TM,'%H:%i'),' ', TZ_MS_LOCATION) as TZ_MS_DESC FROM PS_TZ_MSSJ_ARR_TBL WHERE TZ_CLASS_ID =? AND TZ_BATCH_ID =? AND TZ_MS_PLAN_SEQ =?";
					String msDescr = jdbcTemplate.queryForObject(msDescSql, new Object[]{ classId, pcId, planId }, String.class);

					if(!"".equals(msDescr) && msDescr != null){
						msyyDescr = msDescr;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return msyyDescr;
		}
}
