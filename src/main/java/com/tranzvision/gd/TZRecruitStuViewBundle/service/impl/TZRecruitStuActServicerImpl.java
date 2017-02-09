package com.tranzvision.gd.TZRecruitStuViewBundle.service.impl;

import java.util.List;
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
 * 清华MBA招生网站活动通知
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

	/****** 活动通知********/
	@Override
	public String tzGetHtmlContent(String strParams) {
		String applicationCenterHtml = "";
		JacksonUtil jacksonUtil = new JacksonUtil();

		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		try {
			jacksonUtil.json2Map(strParams);
			String strSiteId = "";
			String strAreaId = "";
			strAreaId = jacksonUtil.getString("siteId");
			strAreaId = jacksonUtil.getString("areaId");

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
			
			//根据siteid和areaId得到栏目id;
			String coluSQL = "select TZ_COLU_ID from PS_TZ_SITEI_AREA_T where TZ_SITEI_ID=? and TZ_AREA_ID=?";
			String coluId = jdbcTemplate.queryForObject(coluSQL, new Object[] { strSiteId,strAreaId }, "String");
			String actColuId = "";
			String applyNoticeColuId = "";
			String dataAreaColuId = "";
			actColuId = coluId.substring(0,coluId.indexOf(",")+1);
			System.out.println(actColuId+"test");
			// 招生活动;
			String recruitAct = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_WEBACT_MESSAGE", "1",language, "招生活动", "招生活动");
			
			//报考通知;
			String applyNotice = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_WEBACT_MESSAGE", "2",language, "报考通知", "报考通知");
			
			//资料专区;
			String dataArea = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_WEBACT_MESSAGE", "3",language, "资料专区", "资料专区");
			
			//更多;
			String more = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_WEBACTMESSAGE", "4", language, "更多","more");
			
			//根据siteid和coluid得到artid
			String artIdSql = "select TZ_ART_ID from PS_TZ_LM_NR_GL_T where TZ_SITE_ID=? and TZ_COLU_ID=?";
			String artId = jdbcTemplate.queryForObject(artIdSql, new Object[]{strSiteId,coluId}, "String");
			List<Map<String, Object>> artIdList = jdbcTemplate.queryForList(artIdSql,new Object[] { strSiteId,coluId });
			
			String recruitActHtml = "";
			
			String applyNoticeHtml = "";
			
			String dataAreaHtml = "";
			
			recruitActHtml = tzGDObject.getHTMLText("HTML.TZRecruitStuViewBundle.TZRecruitStuAct");
			
			applyNoticeHtml = tzGDObject.getHTMLText("HTML.TZRecruitStuViewBundle.TZRecruitStuNotice");
			
			dataAreaHtml = tzGDObject.getHTMLText("HTML.TZRecruitStuViewBundle.TZRecruitStuData");
			
			applicationCenterHtml = tzGDObject.getHTMLText(
					"HTML.TZRecruitStuViewBundle.TZRecruitStuInfoView", recruitAct,applyNotice,dataArea,
					more,recruitActHtml,applyNoticeHtml,dataAreaHtml);
			return applicationCenterHtml;
		} catch (Exception e) {

		}
		return "没有相关数据";
	}
}
