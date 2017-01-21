package com.tranzvision.gd.TZApplicationCenterBundle.service.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.MessageTextServiceImpl;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 
 * 新报名中心，根据清华切图
 *
 */
@Service("com.tranzvision.gd.TZApplicationCenterBundle.service.impl.ApplicationCenter2ServicerImpl")
public class ApplicationCenter2ServicerImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private MessageTextServiceImpl messageTextServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TZGDObject tzGDObject;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	/****** 报名中心 ********/
	@Override
	public String tzGetHtmlContent(String strParams) {
		String applicationCenterHtml = "";
		JacksonUtil jacksonUtil = new JacksonUtil();

		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		try {
			jacksonUtil.json2Map(strParams);
			String strSiteId = "";
			if (jacksonUtil.containsKey("siteId")) {
				strSiteId = jacksonUtil.getString("siteId");
			}

			if (strSiteId == null || "".equals(strSiteId)) {
				strSiteId = request.getParameter("siteId");
			}

			// 根据siteid得到机构id;
			String str_jg_id = "";
			// language;
			String language = "";

			String siteSQL = "select TZ_JG_ID,TZ_SKIN_STOR,TZ_SITE_LANG from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
			Map<String, Object> siteMap = jdbcTemplate.queryForMap(siteSQL, new Object[] { strSiteId });
			if (siteMap != null) {
				str_jg_id = (String) siteMap.get("TZ_JG_ID");
				// String skinstor = (String)siteMap.get("TZ_SKIN_STOR");
				language = (String) siteMap.get("TZ_SITE_LANG");
			}

			if (language == null || "".equals(language)) {
				language = "ZHS";
			}
			
			// 报名中心;
			String ApplicationCenter = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_APPCENTER_MESSAGE", "1",language, "面试申请", "面试申请");
			
			//开始新申请;
			String addNewSqBtDesc = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_APPCENTER_MESSAGE", "2", language, "立即申请","立即申请");
			
			//查看历史;
			String viewHistoryDesc = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_APPCENTER_MESSAGE", "3", language, "查看历史纪录","查看历史纪录");
			
			//查看报名表;
			String viewBmbDesc = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_APPCENTER_MESSAGE", "4", language, "查看报名表","查看报名表");
			
			//是否开通了班级;
			String totalSQL = "SELECT count(1) FROM  PS_TZ_CLASS_INF_T where TZ_PRJ_ID IN (SELECT TZ_PRJ_ID FROM PS_TZ_PROJECT_SITE_T WHERE TZ_SITEI_ID=?) AND TZ_JG_ID=? and TZ_IS_APP_OPEN='Y' and TZ_APP_START_DT IS NOT NULL AND TZ_APP_START_TM IS NOT NULL AND TZ_APP_END_DT IS NOT NULL AND TZ_APP_END_TM IS NOT NULL AND str_to_date(concat(DATE_FORMAT(TZ_APP_START_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(TZ_APP_START_TM,'%H:%i'),':00'),'%Y/%m/%d %H:%i:%s') <= now() AND str_to_date(concat(DATE_FORMAT(TZ_APP_END_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(TZ_APP_END_TM,'%H:%i'),':59'),'%Y/%m/%d %H:%i:%s') >= now()";
			int totalNum = jdbcTemplate.queryForObject(totalSQL, new Object[] { strSiteId,str_jg_id }, "Integer");
			//有开通的班级；
			if(totalNum > 0){
				//是否已经报名了当前开通的班级;
				String appinsSQL = "select TZ_APP_INS_ID from PS_TZ_FORM_WRK_T where OPRID=? and TZ_CLASS_ID in (SELECT TZ_CLASS_ID FROM  PS_TZ_CLASS_INF_T where TZ_PRJ_ID IN (SELECT TZ_PRJ_ID FROM PS_TZ_PROJECT_SITE_T WHERE TZ_SITEI_ID=?) AND TZ_JG_ID=? and TZ_IS_APP_OPEN='Y' and TZ_APP_START_DT IS NOT NULL AND TZ_APP_START_TM IS NOT NULL AND TZ_APP_END_DT IS NOT NULL AND TZ_APP_END_TM IS NOT NULL AND str_to_date(concat(DATE_FORMAT(TZ_APP_START_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(TZ_APP_START_TM,'%H:%i'),':00'),'%Y/%m/%d %H:%i:%s') <= now() AND str_to_date(concat(DATE_FORMAT(TZ_APP_END_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(TZ_APP_END_TM,'%H:%i'),':59'),'%Y/%m/%d %H:%i:%s') >= now()) order by ROW_LASTMANT_DTTM desc limit 0,1";
				long TZ_APP_INS_ID = 0;
				try{
					TZ_APP_INS_ID = jdbcTemplate.queryForObject(appinsSQL,new Object[] { oprid,strSiteId,str_jg_id  }, "Long");
				}catch(NullPointerException nullException){
					TZ_APP_INS_ID = 0;
				}
				//已经报名了，显示报名流程;
				if(TZ_APP_INS_ID > 0){
					
					applicationCenterHtml = tzGDObject.getHTMLText(
							"HTML.TZApplicationCenterBundle.TZ_APPCENTER_LC_HTML", ApplicationCenter,
							viewHistoryDesc,viewBmbDesc);
					return applicationCenterHtml;
				}else{
					//未报名的显示开始申请按钮;
					//1:是否允许报名,"Y"表示不允许报名;
					String isAllowedApp = "Y";
					//TODO需要查询考生允许报名表；
					if("Y".equals(isAllowedApp)){
						//不允许报名
						applicationCenterHtml = tzGDObject.getHTMLText(
								"HTML.TZApplicationCenterBundle.TZ_APPCENTER_CANTNOT_APPLY", ApplicationCenter,
								addNewSqBtDesc);
						return applicationCenterHtml;
					}else{
						//允许报名
						applicationCenterHtml = tzGDObject.getHTMLText(
								"HTML.TZApplicationCenterBundle.TZ_APPCENTER_CANT_APPLY", ApplicationCenter,
								addNewSqBtDesc);
						return applicationCenterHtml;
					}
				}
			
			}else{
				//没有开通的班级；
				applicationCenterHtml = tzGDObject.getHTMLText(
						"HTML.TZApplicationCenterBundle.TZ_APPCENTER_CANTNOT_APPLY", ApplicationCenter,
						addNewSqBtDesc);
				return applicationCenterHtml;
			}

		} catch (Exception e) {

		}
		return "测试";
	}
}
