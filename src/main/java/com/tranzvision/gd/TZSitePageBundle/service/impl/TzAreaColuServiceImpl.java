/**
 * 
 */
package com.tranzvision.gd.TZSitePageBundle.service.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.session.TzGetSetSessionValue;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 原PS: TZ_SITE_DECORATED_APP:TZ_AREA_COLU_CLS
 * 
 * @author SHIHUA
 * @since 2015-12-15
 */
@Service("com.tranzvision.gd.TZSitePageBundle.service.impl.TzAreaColuServiceImpl")
public class TzAreaColuServiceImpl extends FrameworkImpl {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzGDObject;

	@Autowired
	private SiteRepCssServiceImpl siteRepCssServiceImpl;

	@Autowired
	private TzGetSetSessionValue tzGetSetSessionValue;

	@Override
	public String tzGetHtmlContent(String strParams) {

		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			String strSiteId = request.getParameter("siteId");

			String strAreaId = request.getParameter("areaId");

			// String strAreaZone = request.getParameter("areaZone");

			String strAreaType = request.getParameter("areaType");

			String classid = request.getParameter("classid");

			if (null == classid || "".equals(classid)) {

				jacksonUtil.json2Map(strParams);

				strSiteId = jacksonUtil.getString("siteId");

				strAreaId = jacksonUtil.getString("areaId");

				// strAreaZone = jacksonUtil.getString("areaZone");

				strAreaType = jacksonUtil.getString("areaType");
			}

			String strMenuId = "";
			String sql;
			if (null != strAreaId && !"".equals(strAreaId)) {
				sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetMenuidByAreaid2");
				strMenuId = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strSiteId, strAreaId }, "String");
			} else {
				sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzAreaIdFromSiteidAreatype");
				strAreaId = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strAreaType }, "String");

				sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetMenuidByAreaid2");
				strMenuId = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strSiteId, strAreaId }, "String");
			}
			
			tzGetSetSessionValue.setTzSiteGloMenuId(strMenuId);

			sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteiTempPCcode2");
			// String strResultConten = sqlQuery.queryForObject(sql, new
			// Object[] { strSiteId, strMenuId }, "String");
			Map<String, Object> mapSiteiCode = sqlQuery.queryForMap(sql, new Object[] { strSiteId, strMenuId });
			String strResultContent = mapSiteiCode.get("TZ_TEMP_PCCODE") == null ? ""
					: String.valueOf(mapSiteiCode.get("TZ_TEMP_PCCODE"));
			String strScriptHtmlName = mapSiteiCode.get("TZ_PCTEMP_SCRIPT_HTML") == null ? ""
					: String.valueOf(mapSiteiCode.get("TZ_PCTEMP_SCRIPT_HTML"));

			String ctxPath = request.getContextPath();
			String strScripts = tzGDObject.getHTMLText("HTML.TZSitePageBundle." + strScriptHtmlName, ctxPath);

			sql = "select TZ_JG_ID,TZ_SITE_LANG,TZ_SKIN_ID from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=? and TZ_SITEI_ENABLE='Y'";
			Map<String, Object> mapData = sqlQuery.queryForMap(sql, new Object[] { strSiteId });
			String strOrgId = mapData.get("TZ_JG_ID") == null ? "" : String.valueOf(mapData.get("TZ_JG_ID"));
			String strLang = mapData.get("TZ_SITE_LANG") == null ? "" : String.valueOf(mapData.get("TZ_SITE_LANG"));
			String strSkinId = mapData.get("TZ_SKIN_ID") == null ? "" : String.valueOf(mapData.get("TZ_SKIN_ID"));

			//sql = "select TZ_MENU_NAME from PS_TZ_SITEI_MENU_T where TZ_SITEI_ID=? and TZ_MENU_ID=? and TZ_MENU_STATE='Y'";
			sql = "select TZ_MENU_NAME from PS_TZ_SITEI_MENU_T where TZ_SITEI_ID=? and TZ_MENU_ID=?";
			String strMenuName = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strMenuId }, "String");

			strResultContent = siteRepCssServiceImpl.repContextPath(strResultContent);
			strResultContent = siteRepCssServiceImpl.repSkinsImgPath(strResultContent, strSkinId);
			strResultContent = siteRepCssServiceImpl.repJavascriptTags(strResultContent, strScripts,"", strOrgId,
					strSiteId, "");

			strResultContent = siteRepCssServiceImpl.repTitle(strResultContent, strSiteId);
			strResultContent = siteRepCssServiceImpl.repCss(strResultContent, strSiteId);
			strResultContent = siteRepCssServiceImpl.repSiteid(strResultContent, strSiteId);
			strResultContent = siteRepCssServiceImpl.repJgid(strResultContent, strOrgId.toUpperCase());
			strResultContent = siteRepCssServiceImpl.repLang(strResultContent, strLang.toUpperCase());
			strResultContent = siteRepCssServiceImpl.repMenuName(strResultContent, strMenuName);

			return strResultContent;

		} catch (Exception e) {
			e.printStackTrace();

		}

		return strRet;

	}

}
