package com.tranzvision.gd.TZActivityEnrollmentBundle.service.impl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.MessageTextServiceImpl;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 招生网站我的活动_活动预约管理
 * @author 琚峰
 * @since 2017-2-10
 */
@Service("com.tranzvision.gd.TZActivityEnrollmentBundle.service.impl.ActivityReservationServiceImpl")
public class ActivityReservationServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private MessageTextServiceImpl messageTextServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TZGDObject tzGDObject;
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private SiteRepCssServiceImpl siteRepCssServiceImpl;

	/****** 活动预约 ********/
	@Override
	public String tzGetHtmlContent(String strParams) {
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

			// 项目跟目录;
			String rootPath = request.getContextPath();
			// 通用链接;
			String ZSGL_URL = request.getContextPath() + "/dispatcher";
			
			// 根据siteid得到机构id;
			String str_jg_id = "";
			// language;
			String language = "";
			String strCssDir = "";
			String siteSQL = "select TZ_JG_ID,TZ_SKIN_STOR,TZ_SITE_LANG from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
			Map<String, Object> siteMap = jdbcTemplate.queryForMap(siteSQL, new Object[] { strSiteId });
			if (siteMap != null) {
				str_jg_id = (String) siteMap.get("TZ_JG_ID");
				String skinstor = (String) siteMap.get("TZ_SKIN_STOR");
				language = (String) siteMap.get("TZ_SITE_LANG");
				String websitePath = getSysHardCodeVal.getWebsiteCssPath();

				String strRandom = String.valueOf(10 * Math.random());
				if ("".equals(skinstor) || skinstor == null) {
					strCssDir = request.getContextPath() + websitePath + "/" + str_jg_id.toLowerCase() + "/" + strSiteId
							+ "/" + "style_" + str_jg_id.toLowerCase() + ".css?v=" + strRandom;
				} else {
					strCssDir = request.getContextPath() + websitePath + "/" + str_jg_id.toLowerCase() + "/" + strSiteId
							+ "/" + skinstor + "/" + "style_" + str_jg_id.toLowerCase() + ".css?v=" + strRandom;
				}
			}
			if (language == null || "".equals(language)) {   
				language = "ZHS";
			}

			// 活动预约双语化;
			String strActReservation = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_ACTMG_MESSAGE", "1", language, "活动预约","活动预约");
			String strActNotice = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_ACTMG_MESSAGE", "2", language, "活动预告","活动预告");
			String strActRegistered = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_ACTMG_MESSAGE", "3", language, "已报名活动","已报名活动");
			String strToReview = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_ACTMG_MESSAGE", "4", language, "往期回顾","往期回顾");		
			String actContentTabsHtml = "";
			String actContentHtml = "";
			// 活动预约信息;
			actContentTabsHtml = tzGDObject.getHTMLText("HTML.TZActivityEnrollmentBundle.TZ_ACT_BOOKING_HTML",
					true,strActReservation,strActNotice,strActRegistered,strToReview);
			// 展示页面;
			actContentHtml = tzGDObject.getHTMLTextForDollar("HTML.TZActivityEnrollmentBundle.TZ_ACT_CENTENT_HTML",
					true,request.getContextPath(), ZSGL_URL, strCssDir, actContentTabsHtml, str_jg_id, strSiteId);
			
			actContentHtml = siteRepCssServiceImpl.repTitle(actContentHtml, strSiteId);
			actContentHtml = siteRepCssServiceImpl.repCss(actContentHtml, strSiteId);
			return actContentHtml;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}