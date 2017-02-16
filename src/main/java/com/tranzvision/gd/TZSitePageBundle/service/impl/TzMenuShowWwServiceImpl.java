/**
 * 
 */
package com.tranzvision.gd.TZSitePageBundle.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 原PS：TZ_SITE_DECORATED_APP:TZ_MENU_SHOWWW_CLS
 * 
 * @author SHIHUA
 * @since 2015-12-18
 */
@Service("com.tranzvision.gd.TZSitePageBundle.service.impl.TzMenuShowWwServiceImpl")
public class TzMenuShowWwServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzGDObject;

	@Override
	public String tzGetHtmlContent(String strParams) {

		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			jacksonUtil.json2Map(strParams);

			String strSiteId = jacksonUtil.getString("siteId");
			String strMenuId = jacksonUtil.getString("menuId");

			if ((null != strSiteId && !"".equals(strSiteId)) && (null != strMenuId && !"".equals(strMenuId))) {

				String sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteMenuUrl");
				Map<String, Object> mapMenuUrl = sqlQuery.queryForMap(sql, new Object[] { strSiteId, strMenuId });

				if (null != mapMenuUrl) {

					String strUrl = mapMenuUrl.get("TZ_MENU_URL") == null ? ""
							: String.valueOf(mapMenuUrl.get("TZ_MENU_URL"));
					String strOpenType = mapMenuUrl.get("TZ_MENU_OPURL_TYPE") == null ? ""
							: String.valueOf(mapMenuUrl.get("TZ_MENU_OPURL_TYPE"));

					if (!"".equals(strUrl)) {
						// 这里需要跳转
						// %Response.RedirectURL(&strUrl);
//						strRet = tzGDObject.getHTMLText("SQL.TZSitePageBundle.TzWinLocationAndOpenWin", strOpenType, strUrl);
						strRet = tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzWinLocationAndOpenWin", strOpenType, strUrl);
					} else {
						return "参数错误！";
					}

				} else {
					return "站点菜单不存在！";
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
