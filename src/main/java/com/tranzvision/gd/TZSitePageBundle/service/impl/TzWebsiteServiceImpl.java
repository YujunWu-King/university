/**
 * 
 */
package com.tranzvision.gd.TZSitePageBundle.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzWebsiteLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdObjectServiceImpl;
import com.tranzvision.gd.TZSitePageBundle.service.TzWebsiteService;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * @author SHIHUA
 * @since 2016-01-06
 */
@Service
public class TzWebsiteServiceImpl implements TzWebsiteService {

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzGDObject;

	@Autowired
	private GdObjectServiceImpl gdObjectServiceImpl;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private TzWebsiteLoginServiceImpl tzWebsiteLoginServiceImpl;

	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	@Autowired
	private SiteRepCssServiceImpl siteRepCssServiceImpl;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tranzvision.gd.TZSitePageBundle.service.TzWebsiteService#checkOrgId(
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public boolean checkOrgId(String orgid, String loginOrgid) {

		try {

			String sql = "select 'Y' from PS_TZ_JG_BASE_T where TZ_JG_ID=? and TZ_JG_EFF_STA='Y'";

			String recExists = sqlQuery.queryForObject(sql, new Object[] { orgid }, "String");

			if ("Y".equals(recExists)) {
				// 未传loginOrgid，表明没有登录
				if (null != loginOrgid && !"".equals(loginOrgid)) {
					// 若已经登录，则要校验登录的机构和要访问的站点机构是否一致
					if (!orgid.equals(loginOrgid)) {
						// 不一致，则不允许访问
						return false;
					}
				}

				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public Map<String, Object> checkSiteIdAndGetLang(String orgid, String siteid) {
		String siteLang = getSysHardCodeVal.getSysDefaultLanguage();
		Map<String, Object> mapRtn = new HashMap<String, Object>();
		mapRtn.put("checkResult", false);
		mapRtn.put("siteLang", siteLang);
		try {

			String sql = "select 'Y' as rec,TZ_SITE_LANG from PS_TZ_SITEI_DEFN_T where TZ_JG_ID=? and TZ_SITEI_ID=? and TZ_SITEI_ENABLE='Y'";

			Map<String, Object> mapData = sqlQuery.queryForMap(sql, new Object[] { orgid, siteid });

			if (null != mapData) {

				siteLang = mapData.get("TZ_SITE_LANG") == null ? "" : String.valueOf(mapData.get("TZ_SITE_LANG"));
				if (!"".equals(siteLang.trim())) {
					mapRtn.replace("checkResult", true);
					mapRtn.replace("siteLang", siteLang);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapRtn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tranzvision.gd.TZSitePageBundle.service.TzWebsiteService#
	 * getIndexSaveCode(javax.servlet.http.HttpServletRequest, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public String getIndexSaveCode(HttpServletRequest request, String orgid, String siteid) {
		String strRtn = "";
		try {

			orgid = orgid.toUpperCase();

			Map<String, Object> mapRst = this.checkSiteIdAndGetLang(orgid, siteid);

			String siteLang = String.valueOf(mapRst.get("siteLang"));

			if (!(boolean) mapRst.get("checkResult")) {
				strRtn = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "", "", siteLang, "无效站点。",
						"Invalid siteid.");
				return strRtn;
			}

			String loginOrgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);

			if (!this.checkOrgId(orgid, loginOrgid)) {
				strRtn = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "", "", siteLang, "无效机构。",
						"Invalid orgid.");
				return strRtn;
			}

			String ctxPath = request.getContextPath();

			String sql = "select TZ_INDEX_SAVECODE,TZ_SKIN_ID from PS_TZ_SITEI_DEFN_T where TZ_JG_ID=? and TZ_SITEI_ID=? and TZ_SITEI_ENABLE='Y'";

			Map<String, Object> mapSiteiInfo = sqlQuery.queryForMap(sql, new Object[] { orgid, siteid });
			String strIndexHtml = "";
			String strSkinID = "";
			if (mapSiteiInfo != null) {
				strIndexHtml = mapSiteiInfo.get("TZ_INDEX_SAVECODE") == null ? ""
						: String.valueOf(mapSiteiInfo.get("TZ_INDEX_SAVECODE"));
				strSkinID = mapSiteiInfo.get("TZ_SKIN_ID") == null ? ""
						: String.valueOf(mapSiteiInfo.get("TZ_SKIN_ID"));

				strIndexHtml = siteRepCssServiceImpl.repSkinsImgPath(strIndexHtml, strSkinID);

			}

			strIndexHtml = siteRepCssServiceImpl.repContextPath(strIndexHtml);
			strIndexHtml = siteRepCssServiceImpl.repCss(strIndexHtml, siteid);
			strIndexHtml = siteRepCssServiceImpl.repTitle(strIndexHtml, siteid);

			String strSelfJavascripts = tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzScriptsIndex", ctxPath);
			String strBroadStyleJavascripts = tzGDObject.getHTMLText("HTML.TZSitePageBroadStyleBundle.TzScriptsIndex", ctxPath);
			strIndexHtml = siteRepCssServiceImpl.repJavascriptTags(strIndexHtml, strSelfJavascripts,strBroadStyleJavascripts, orgid, siteid,
					"Y");
			
			strRtn = strIndexHtml;

		} catch (Exception e) {
			e.printStackTrace();
			strRtn = "系统异常，请稍后再试。";
		}
		return strRtn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tranzvision.gd.TZSitePageBundle.service.TzWebsiteService#
	 * getIndexPreviewCode(javax.servlet.http.HttpServletRequest,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public String getIndexPreviewCode(HttpServletRequest request, String orgid, String siteid) {

		String strRtn = "";
		try {

			orgid = orgid.toUpperCase();

			Map<String, Object> mapRst = this.checkSiteIdAndGetLang(orgid, siteid);

			String siteLang = String.valueOf(mapRst.get("siteLang"));

			if (!(boolean) mapRst.get("checkResult")) {
				strRtn = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "", "", siteLang, "无效站点。",
						"Invalid siteid.");
				return strRtn;
			}

			String loginOrgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);

			if (!this.checkOrgId(orgid, loginOrgid)) {
				strRtn = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "", "", siteLang, "无效机构。",
						"Invalid orgid.");
				return strRtn;
			}

			String sql = "select TZ_INDEX_PRECODE from PS_TZ_SITEI_DEFN_T where TZ_JG_ID=? and TZ_SITEI_ID=? and TZ_SITEI_ENABLE='Y'";

			String strIndexHtml = sqlQuery.queryForObject(sql, new Object[] { orgid, siteid }, "String");

			strRtn = strIndexHtml;

		} catch (Exception e) {
			e.printStackTrace();
			strRtn = "系统异常，请稍后再试。";
		}
		return strRtn;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tranzvision.gd.TZSitePageBundle.service.TzWebsiteService#
	 * getIndexPublishCode(javax.servlet.http.HttpServletRequest,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public String getIndexPublishCode(HttpServletRequest request, String orgid, String siteid) {
		String strRtn = "";
		try {

			orgid = orgid.toUpperCase();

			Map<String, Object> mapRst = this.checkSiteIdAndGetLang(orgid, siteid);

			String siteLang = String.valueOf(mapRst.get("siteLang"));

			if (!(boolean) mapRst.get("checkResult")) {
				strRtn = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "", "", siteLang, "无效站点。",
						"Invalid siteid.");
				return strRtn;
			}

			String loginOrgid = tzWebsiteLoginServiceImpl.getLoginedUserOrgid(request);

			if (!this.checkOrgId(orgid, loginOrgid)) {
				strRtn = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "", "", siteLang, "无效机构。",
						"Invalid orgid.");
				return "errororg";
			}

			String sql = "select TZ_INDEX_PUBCODE from PS_TZ_SITEI_DEFN_T where TZ_JG_ID=? and TZ_SITEI_ID=? and TZ_SITEI_ENABLE='Y' and TZ_SITE_FBZT='Y'";

			String strIndexHtml = sqlQuery.queryForObject(sql, new Object[] { orgid, siteid }, "String");

			if (null == strIndexHtml || "".equals(strIndexHtml)) {
				strRtn = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "", "", "", "该站点未设置首页，请联系站点管理员。",
						"This site haven't the index page, please contact Administrator.");
			} else {
				strRtn = strIndexHtml;
			}

		} catch (Exception e) {
			e.printStackTrace();
			strRtn = "系统异常，请稍后再试。";
		}
		return strRtn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tranzvision.gd.TZSitePageBundle.service.TzWebsiteService#
	 * getLoginSaveCode(javax.servlet.http.HttpServletRequest, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public String getLoginSaveCode(HttpServletRequest request, String orgid, String siteid) {
		String strRtn = "";
		try {
			
			orgid = orgid.toUpperCase();

			Map<String, Object> mapRst = this.checkSiteIdAndGetLang(orgid, siteid);

			String siteLang = String.valueOf(mapRst.get("siteLang"));

			if (!(boolean) mapRst.get("checkResult")) {
				strRtn = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "", "", siteLang, "无效站点。",
						"Invalid siteid.");
				return strRtn;
			}

			String loginOrgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);

			if (!this.checkOrgId(orgid, loginOrgid)) {
				strRtn = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "", "", siteLang, "无效机构。",
						"Invalid orgid.");
				return strRtn;
			}

			String ctxPath = request.getContextPath();

			String sql = "select TZ_LONGIN_SAVECODE,TZ_SKIN_ID from PS_TZ_SITEI_DEFN_T where TZ_JG_ID=? and TZ_SITEI_ID=? and TZ_SITEI_ENABLE='Y'";

			Map<String, Object> mapSiteiInfo = sqlQuery.queryForMap(sql, new Object[] { orgid, siteid });
			String strLoginHtml = "";
			String strSkinID = "";
			if (mapSiteiInfo != null) {
				strLoginHtml = mapSiteiInfo.get("TZ_LONGIN_SAVECODE") == null ? ""
						: String.valueOf(mapSiteiInfo.get("TZ_LONGIN_SAVECODE"));
				strSkinID = mapSiteiInfo.get("TZ_SKIN_ID") == null ? ""
						: String.valueOf(mapSiteiInfo.get("TZ_SKIN_ID"));

				strLoginHtml = siteRepCssServiceImpl.repSkinsImgPath(strLoginHtml, strSkinID);

			}

			strLoginHtml = siteRepCssServiceImpl.repContextPath(strLoginHtml);
			strLoginHtml = siteRepCssServiceImpl.repCss(strLoginHtml, siteid);

			String strSelfJavascripts = tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzScriptsLogin", ctxPath);
			String strBroadStyleJavascripts = tzGDObject.getHTMLText("HTML.TZSitePageBroadStyleBundle.TzScriptsLogin", ctxPath);
			strLoginHtml = siteRepCssServiceImpl.repJavascriptTags(strLoginHtml, strSelfJavascripts,strBroadStyleJavascripts, orgid, siteid,
					"Y");

			strRtn = strLoginHtml;

		} catch (Exception e) {
			e.printStackTrace();
			strRtn = "系统异常，请稍后再试。";
		}
		return strRtn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tranzvision.gd.TZSitePageBundle.service.TzWebsiteService#
	 * getLoginPreviewCode(javax.servlet.http.HttpServletRequest,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public String getLoginPreviewCode(HttpServletRequest request, String orgid, String siteid) {
		String strRtn = "";
		try {

			orgid = orgid.toUpperCase();

			Map<String, Object> mapRst = this.checkSiteIdAndGetLang(orgid, siteid);

			String siteLang = String.valueOf(mapRst.get("siteLang"));

			if (!(boolean) mapRst.get("checkResult")) {
				strRtn = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "", "", siteLang, "无效站点。",
						"Invalid siteid.");
				return strRtn;
			}

			String loginOrgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);

			if (!this.checkOrgId(orgid, loginOrgid)) {
				strRtn = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "", "", siteLang, "无效机构。",
						"Invalid orgid.");
				return strRtn;
			}

			String sql = "select TZ_LOGIN_PRECODE from PS_TZ_SITEI_DEFN_T where TZ_JG_ID=? and TZ_SITEI_ID=? and TZ_SITEI_ENABLE='Y'";

			String strLoginHtml = sqlQuery.queryForObject(sql, new Object[] { orgid, siteid }, "String");

			strRtn = strLoginHtml;

		} catch (Exception e) {
			e.printStackTrace();
			strRtn = "系统异常，请稍后再试。";
		}
		return strRtn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tranzvision.gd.TZSitePageBundle.service.TzWebsiteService#
	 * getLoginPublishCode(javax.servlet.http.HttpServletRequest,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public String getLoginPublishCode(HttpServletRequest request, String orgid, String siteid) {
		String strRtn = "";
		try {

			orgid = orgid.toUpperCase();

			Map<String, Object> mapRst = this.checkSiteIdAndGetLang(orgid, siteid);

			String siteLang = String.valueOf(mapRst.get("siteLang"));

			if (!(boolean) mapRst.get("checkResult")) {
				strRtn = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "", "", siteLang, "无效站点。",
						"Invalid siteid.");
				return strRtn;
			}

			if (!this.checkOrgId(orgid, "")) {
				strRtn = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "", "", siteLang, "无效机构。",
						"Invalid orgid.");
				return strRtn;
			}

			String sql = "select TZ_LONGIN_PUBCODE from PS_TZ_SITEI_DEFN_T where TZ_JG_ID=? and TZ_SITEI_ID=? and TZ_SITEI_ENABLE='Y' and TZ_SITE_FBZT='Y'";

			String strLoginHtml = sqlQuery.queryForObject(sql, new Object[] { orgid, siteid }, "String");

			if (null == strLoginHtml || "".equals(strLoginHtml)) {
				strRtn = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "", "", "", "该站点未设置登录页，请联系站点管理员。",
						"This site haven't the login page, please contact Administrator.");
			} else {
				strRtn = strLoginHtml;
			}

		} catch (Exception e) {
			e.printStackTrace();
			strRtn = "系统异常，请稍后再试。";
		}
		return strRtn;
	}

	public String getMLoginPublishCode(HttpServletRequest request, String orgid, String siteid, String openid){
	    String strRtn = "";
	    try {
		orgid = orgid.toUpperCase();

		Map<String, Object> mapRst = this.checkSiteIdAndGetLang(orgid, siteid);
		String siteLang = String.valueOf(mapRst.get("siteLang"));
		if (!(boolean) mapRst.get("checkResult")) {
			strRtn = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "", "", siteLang, "无效站点。",
					"Invalid siteid.");
			return strRtn;
		}
		if (!this.checkOrgId(orgid, "")) {
			strRtn = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "", "", siteLang, "无效机构。",
					"Invalid orgid.");
			return strRtn;
		}
		String ctxPath = request.getContextPath();
		String JGID = sqlQuery.queryForObject("select TZ_JG_ID from PS_TZ_SITEI_DEFN_T WHERE TZ_SITEI_ID=?",new Object[]{siteid},"String");
		
		if (JGID.equals("SEM")) {
			JGID="";
		} else {
			JGID.toLowerCase();
		}
		strRtn = tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzSemMLoginRelease",ctxPath,orgid,siteid,openid,JGID);

	    } catch (Exception e) {
		e.printStackTrace();
		strRtn = "系统异常，请稍后再试。";
	    }
	    return strRtn;
	}
}
