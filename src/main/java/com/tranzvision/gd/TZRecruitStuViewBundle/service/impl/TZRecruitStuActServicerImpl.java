package com.tranzvision.gd.TZRecruitStuViewBundle.service.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.MessageTextServiceImpl;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 
 * 招生活动
 *
 */
@Service("com.tranzvision.gd.TZRecruitStuViewBundle.service.impl.TZRecruitStuActServicerImpl")
public class TZRecruitStuActServicerImpl extends FrameworkImpl {
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

	/****** 招生活动********/
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
			
			// 报考日历;
			String registerCalendar = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_APPCENTER_MESSAGE", "1",language, "招生活动", "招生活动");
			
			//更多;
			String more = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_APPCENTER_MESSAGE", "2", language, "更多","more");
			
			//报考时间;
			String date = "";
			
			//报考地点;
			String place = "";
			
			//报考名称;
			String registerName = "";
			
			
			String totalSQL = "SELECT count(1) FROM  PS_TZ_CLASS_INF_T where TZ_PRJ_ID IN (SELECT TZ_PRJ_ID FROM PS_TZ_PROJECT_SITE_T WHERE TZ_SITEI_ID=?) AND TZ_JG_ID=? and TZ_IS_APP_OPEN='Y' and TZ_APP_START_DT IS NOT NULL AND TZ_APP_START_TM IS NOT NULL AND TZ_APP_END_DT IS NOT NULL AND TZ_APP_END_TM IS NOT NULL AND str_to_date(concat(DATE_FORMAT(TZ_APP_START_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(TZ_APP_START_TM,'%H:%i'),':00'),'%Y/%m/%d %H:%i:%s') <= now() AND str_to_date(concat(DATE_FORMAT(TZ_APP_END_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(TZ_APP_END_TM,'%H:%i'),':59'),'%Y/%m/%d %H:%i:%s') >= now()";
			int totalNum = jdbcTemplate.queryForObject(totalSQL, new Object[] { strSiteId,str_jg_id }, "Integer");
			
			applicationCenterHtml = tzGDObject.getHTMLText(
					"HTML.TZRecruitStuViewBundle.TZRecruitStuInfoView", registerCalendar,
					more);
			return applicationCenterHtml;
		} catch (Exception e) {

		}
		return "测试";
	}
}
