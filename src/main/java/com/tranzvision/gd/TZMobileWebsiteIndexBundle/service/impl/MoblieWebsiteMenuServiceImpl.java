package com.tranzvision.gd.TZMobileWebsiteIndexBundle.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 
 * @author tang 加载手机招生网站底部公用菜单
 */
@Service("com.tranzvision.gd.TZMobileWebsiteIndexBundle.service.impl.MoblieWebsiteMenuServiceImpl")
public class MoblieWebsiteMenuServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TZGDObject tzGDObject;

	@Override
	// 加载手机招生网站底部公用菜单
	public String tzGetHtmlContent(String strParams) {
		// rootPath;
		String ctxPath = request.getContextPath();

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);
		String siteId = "";
		String menuId = "1";
		if (jacksonUtil.containsKey("siteId") && jacksonUtil.containsKey("menuId")) {
			siteId = jacksonUtil.getString("siteId");
			menuId = jacksonUtil.getString("menuId");
		} else {
			siteId = request.getParameter("siteId");
			menuId = request.getParameter("menuId");
		}
		if (menuId == null || "".equals(menuId)) {
			menuId = "1";
		}

		int menuNum = 1;
		String[] classOn = { "", "", "", "", "" };
		try {
			menuNum = Integer.parseInt(menuId);
			if (menuNum <= 0 || menuNum > 5) {
				menuNum = 1;
			}
			classOn[menuNum - 1] = "on";
		} catch (Exception e) {
			menuNum = 1;
		}

		String JGID = sqlQuery.queryForObject("select TZ_JG_ID from PS_TZ_SITEI_DEFN_T WHERE TZ_SITEI_ID=?",
				new Object[] { siteId }, "String");

		// 首页地址;
		String indexUrl = ctxPath + "/dispatcher?classid=mIndex&siteId=" + siteId;
		// 招生日历
		String columnId = "";
		if (JGID != null) {
			if (JGID.equals("SEM")) {
				columnId = sqlQuery.queryForObject(
						"select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?",
						new Object[] { "TZ_M_WEB_ZSRL" }, "String");
			} else if (JGID.equals("MEM")) {
				columnId = sqlQuery.queryForObject(
						"select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?",
						new Object[] { "TZ_M_WEB_ZSRL_MEM" }, "String");
			}
		}
		String rlUrl = ctxPath + "/dispatcher?classid=mZsrl&siteId=" + siteId + "&columnId=" + columnId;
		// 状态查询;
		String statusUrl = ctxPath + "/dispatcher?classid=mAppstatus&siteId=" + siteId;
		// 联系我们;
		String lxUrl = "";

		if (JGID != null) {
			if (JGID.equals("SEM")) {
				lxUrl = sqlQuery.queryForObject("select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?",
						new Object[] { "TZ_M_LXWM_URL" }, "String");
			} else if (JGID.equals("MEM")) {
				lxUrl = sqlQuery.queryForObject("select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?",
						new Object[] { "TZ_M_LXWM_MEM_URL" }, "String");
			}
		}
		// String lxUrl = sqlQuery.queryForObject("select TZ_HARDCODE_VAL from
		// PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?", new
		// Object[]{"TZ_M_LXWM_URL"},"String");
		// 我的
		String myUrl = ctxPath + "/dispatcher?classid=mMy&siteId=" + siteId;

		String[] url = { indexUrl, rlUrl, statusUrl, lxUrl, myUrl };
		// url[menuNum-1] = "javascript:void(0);";

		String menuHtml = "";
		try {
			menuHtml = tzGDObject.getHTMLTextForDollar("HTML.TZMobileWebsiteIndexBundle.TZ_MOBILE_BASE_MENU_HTML",
					classOn[0], classOn[1], classOn[2], classOn[3], classOn[4], url[0], url[1], url[2], url[3], url[4]);
		} catch (TzSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return menuHtml;
	}
}
