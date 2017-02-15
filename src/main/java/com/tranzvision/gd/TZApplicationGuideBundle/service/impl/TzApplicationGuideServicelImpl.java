package com.tranzvision.gd.TZApplicationGuideBundle.service.impl;

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
 * 
 * 清华MBA招生网站_申请指导
 * @author JF
 * @since 2016-01-14
 */
@Service("com.tranzvision.gd.TZApplicationGuideBundle.service.impl.TzApplicationGuideServicelImpl")
public class TzApplicationGuideServicelImpl extends FrameworkImpl {
	
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
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	@Autowired
	private SiteRepCssServiceImpl siteRepCssServiceImpl;

	@Override
	public String tzGetHtmlContent(String strParams) {
		String applicationGuideHtml = "";
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
			// 通用链接;
			String ZSGL_URL = request.getContextPath() + "/dispatcher";

			// 1.申请指导;
			String stationLetter = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_APP_GUIDE_MESSAGE", "1",
					language, "申请指导", "申请指导");

			// 获取数据失败，请联系管理员;
			applicationGuideHtml = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_APP_GUIDE_MESSAGE", "2",
					language, "获取数据失败，请联系管理员", "获取数据失败，请联系管理员");
			
			// 展示页面;
			applicationGuideHtml = tzGDObject.getHTMLText("HTML.TZApplicationGuideBundle.TZ_APP_GUIDE_HTML",
					request.getContextPath(), ZSGL_URL, strCssDir, applicationGuideHtml, str_jg_id, strSiteId,stationLetter);

			applicationGuideHtml = siteRepCssServiceImpl.repTitle(applicationGuideHtml, strSiteId);
			applicationGuideHtml = siteRepCssServiceImpl.repCss(applicationGuideHtml, strSiteId);

		} catch (Exception e) {
			e.printStackTrace();
			return "无法获取相关数据";
		}	
		return applicationGuideHtml;
	}

}
