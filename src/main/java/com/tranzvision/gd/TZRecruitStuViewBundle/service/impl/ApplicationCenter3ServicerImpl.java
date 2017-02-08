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
 * 报考日历
 *
 */
@Service("com.tranzvision.gd.TZRecruitStuViewBundle.service.impl.ApplicationCenter3ServicerImpl")
public class ApplicationCenter3ServicerImpl extends FrameworkImpl {
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

	/****** 报考日历 ********/
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
			String strAreaId="";
			strAreaId = request.getParameter("artId");
			
			// 根据siteid得到机构id;
			String str_jg_id = "";
			// language;
			String language = "";

//			String siteSQL = "select TZ_JG_ID,TZ_SKIN_STOR,TZ_SITE_LANG from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
//			Map<String, Object> siteMap = jdbcTemplate.queryForMap(siteSQL, new Object[] { strSiteId });
//			if (siteMap != null) {
//				str_jg_id = (String) siteMap.get("TZ_JG_ID");
//				// String skinstor = (String)siteMap.get("TZ_SKIN_STOR");
//				language = (String) siteMap.get("TZ_SITE_LANG");
//			}
//
//			if (language == null || "".equals(language)) {
//				language = "ZHS";
//			}
			//根据siteid和areaId得到栏目id;
			String coluSQL = "select TZ_COLU_ID from PS_TZ_SITEI_AREA_T where TZ_SITEI_ID=? and TZ_AREA_ID=?";
			String coluId = jdbcTemplate.queryForObject(coluSQL, new Object[] { strSiteId,strAreaId }, "String");
			
			// 报考日历;
			String registerCalendar = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_APPCENTER_MESSAGE", "1",language, "报考日历", "报考日历");
			
			//更多;
			String more = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_APPCENTER_MESSAGE", "2", language, "更多","more");
			
			//报考时间;
			String date = "";
			
			//报考地点;
			String place = "";
			
			//报考名称;
			String registerName = "";
			
			
			String siteSQL = "select TZ_FBZ,TZ_BLT_DEPT,TZ_LASTMANT_OPRID from PS_TZ_LM_NR_GL_T where TZ_COLU_ID=? order by TZ_LASTMANT_DTTM DESC limit 4";
			Map<String, Object> coluMap = jdbcTemplate.queryForMap(siteSQL, new Object[] { coluId });
			if (coluMap != null) {
				date = (String) coluMap.get("TZ_FBZ");
				place = (String) coluMap.get("TZ_BLT_DEPT");
				registerName = (String) coluMap.get("TZ_LASTMANT_OPRID");
			}
			applicationCenterHtml = tzGDObject.getHTMLText(
					"HTML.TZApplicationCenterBundle.TZ_APPCENTER_BKRL_HTML", registerCalendar,
					more);
			return applicationCenterHtml;
		} catch (Exception e) {

		}
		return "测试";
	}
}
