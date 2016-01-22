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
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 原PS：TZ_SITE_DECORATED_APP:TZ_MENU_SHOW_CLS
 * 
 * @author SHIHUA
 * @since 2015-12-18
 */
@Service("com.tranzvision.gd.TZSitePageBundle.service.impl.TzMenuShowEventListServiceImpl")
public class TzMenuShowEventListServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzGDObject;

	@Autowired
	private SiteRepCssServiceImpl siteRepCssServiceImpl;

	@Autowired
	private HttpServletRequest request;

	@Override
	public String tzGetHtmlContent(String strParams) {

		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			jacksonUtil.json2Map(strParams);

			String strSiteId = jacksonUtil.getString("siteId");
			String strMenuId = jacksonUtil.getString("menuId");

			if ((null != strSiteId && !"".equals(strSiteId)) && (null != strMenuId && !"".equals(strMenuId))) {

				String sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteTplPcCodeByMenuid");
				String strResultContent = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strMenuId }, "String");

				if (null != strResultContent) {

					String ctxPath = request.getContextPath();

					sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetOrgidSiteLangBySiteid");
					Map<String, Object> mapSiteOrg = sqlQuery.queryForMap(sql, new Object[] { strSiteId });
					String orgid = "";
					String orgidLower = "";
					if (mapSiteOrg != null) {
						orgid = mapSiteOrg.get("TZ_JG_ID") == null ? "" : String.valueOf(mapSiteOrg.get("TZ_JG_ID"));
						orgidLower = orgid.toLowerCase();
					}

					strResultContent = siteRepCssServiceImpl.repTitle(strResultContent, strSiteId);

					strResultContent = strResultContent.replace("{page_stylecss}",
							orgidLower + "/" + strSiteId + "/" + "style_" + orgidLower + ".css");

					strResultContent = strResultContent.replace("{ContextPath}", ctxPath);

					String strJavascripts = tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzScriptsGlobalVar", ctxPath,
							orgid, strSiteId, "Y")
							+ tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzScriptsEventList", ctxPath);

					strResultContent = strResultContent.replace("<!--#{javascripts}#-->", strJavascripts);

					return strResultContent;

				} else {
					return "内容不存在！";
				}

			} else {
				return "参数错误！";
			}

		} catch (Exception e) {
			e.printStackTrace();
			strRet = "异常退出！";
		}

		return strRet;

	}

}
