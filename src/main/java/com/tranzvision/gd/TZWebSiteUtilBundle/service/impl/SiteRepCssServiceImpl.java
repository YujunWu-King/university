package com.tranzvision.gd.TZWebSiteUtilBundle.service.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 原ps包：TZ_SITE_UTIL_APP
 * 
 * @author tang 站点页面设置，替换css 20151210
 */
@Service("com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl")
public class SiteRepCssServiceImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TZGDObject tzGDObject;

	/**
	 * 替换页面上的 {ContextPath} 占位符
	 * 
	 * @author SHIHUA
	 * @param strContent
	 * @return
	 */
	public String repContextPath(String strContent) {
		if (null == strContent || "".equals(strContent)) {
			return strContent;
		}

		String ctxPath = request.getContextPath();

		strContent = strContent.replace("{ContextPath}", ctxPath);

		return strContent;
	}

	/**
	 * 将环境路径替换回占位符
	 * 
	 * @param strContent
	 * @return
	 */
	public String repResetContextPath(String strContent) {
		if (null == strContent || "".equals(strContent)) {
			return strContent;
		}

		String ctxPath = request.getContextPath();
		if (!"".equals(ctxPath)) {
			strContent = strContent.replace(ctxPath + "/", "{ContextPath}/");
		}

		return strContent;
	}

	/**
	 * 替换页面上的 <!--#{javascripts}#--> 占位符
	 * 
	 * @author SHIHUA
	 * @param strContent
	 *            原始html内容
	 * @param strScripts
	 *            该页面要引入的js文件
	 * @param orgid
	 *            机构编号
	 * @param siteid
	 *            站点编号
	 * @param isDecorate
	 *            是否“装修”页面：Y-是；N-否；
	 * @return String
	 */
	public String repJavascriptTags(String strContent, String strScripts, String orgid, String siteid,
			String isDecorate) {
		if (null == strContent || "".equals(strContent)) {
			return strContent;
		}

		String ctxPath = request.getContextPath();
		orgid = orgid.toUpperCase();
		try {
			String gblScripts = tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzScriptsGlobalVar", ctxPath, orgid,
					siteid, isDecorate);
			strContent = strContent.replace("<!--#{javascripts}#-->", gblScripts + strScripts);
		} catch (TzSystemException e) {
			e.printStackTrace();
		}

		return strContent;

	}

	/**
	 * 替换页面上的图片路径（每套皮肤都有完整的图片，包括通用图片）
	 * 
	 * @param strConent
	 * @param strSkinId
	 * @return
	 */
	public String repSkinsImgPath(String strConent, String strSkinId) {
		String ctxPath = request.getContextPath();
		String strSkinImgPath = ctxPath + getSysHardCodeVal.getWebsiteSkinsImgPath() + "/" + strSkinId + "/";

		strConent = strConent.replace("{ContextSkinPath}", strSkinImgPath);

		return strConent;
	}

	/**
	 * 将页面上的图片路径替换回占位符
	 * 
	 * @param strConent
	 * @param strSkinId
	 * @return
	 */
	public String repResetSkinsImgPath(String strConent, String strSkinId) {
		String ctxPath = request.getContextPath();
		String strSkinImgPath = ctxPath + getSysHardCodeVal.getWebsiteSkinsImgPath() + "/" + strSkinId + "/";

		strConent = strConent.replace(strSkinImgPath, "{ContextSkinPath}/");

		return strConent;
	}

	// 替换title; PS类：TZ_SITE_REP_CSS.repTitle;
	public String repTitle(String strConent, String strSiteId) {
		if (strSiteId == null || "".equals(strSiteId) || strConent == null || "".equals(strConent)) {
			return strConent;
		}
		String sql = "SELECT TZ_SITEI_NAME FROM PS_TZ_SITEI_DEFN_T WHERE TZ_SITEI_ID=? AND TZ_SITEI_ENABLE='Y'";
		String strSiteName = jdbcTemplate.queryForObject(sql, new Object[] { strSiteId }, "String");
		if (strSiteName != null) {
			int numCharstart = strConent.indexOf("<title>");
			if (numCharstart >= 0) {
				int numCharend = strConent.indexOf("</title>", numCharstart);
				if (numCharend > numCharstart) {
					String strCharsub = strConent.substring(numCharstart, numCharend + 8);
					strConent = strConent.replace(strCharsub, "<title>" + strSiteName + "</title>");
				}
			}
		}
		return strConent;
	}

	// 替换css; PS类：TZ_SITE_REP_CSS.repCss;
	public String repCss(String strConent, String strSiteId) {
		if (strSiteId == null || "".equals(strSiteId) || strConent == null || "".equals(strConent)) {
			return strConent;
		}
		String sql = "SELECT TZ_SKIN_STOR,TZ_JG_ID FROM PS_TZ_SITEI_DEFN_T WHERE TZ_SITEI_ID=?";
		Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[] { strSiteId });
		if (map != null) {
			String skinstor = map.get("TZ_SKIN_STOR") == null ? "" : String.valueOf(map.get("TZ_SKIN_STOR"));
			String strJgId = (String) map.get("TZ_JG_ID");
			String websitePath = getSysHardCodeVal.getWebsiteCssPath();
			String strCssDir = request.getContextPath();
			if ("".equals(skinstor) || skinstor == null) {
				strCssDir = strCssDir + websitePath + "/" + strJgId.toLowerCase() + "/" + strSiteId + "/";
			} else {
				strCssDir = strCssDir + websitePath + "/" + strJgId.toLowerCase() + "/" + strSiteId + "/" + skinstor
						+ "/";
			}

			String strRandom = String.valueOf(10*Math.random());
			if (strJgId != null && !"".equals(strJgId)) {
				strConent = strConent.replace("page_stylecss",
						strCssDir + "style_" + strJgId.toLowerCase() + ".css?v=" + strRandom);
			} else {
				strConent = strConent.replace("page_stylecss", strCssDir + "style.css?v=" + strRandom);
			}

		}

		return strConent;
	}

	// 替换css; PS类：TZ_SITE_REP_CSS.repCssByJg;
	public String repCssByJg(String strConent, String strJgId) {

		if (strJgId == null || "".equals(strJgId) || strConent == null || "".equals(strConent)) {
			return strConent;
		}
		String sql = "SELECT TZ_SITEI_ID,TZ_SKIN_STOR FROM PS_TZ_SITEI_DEFN_T WHERE TZ_JG_ID=? and TZ_SITEI_ENABLE='Y' order by (cast(TZ_SITEI_ID as signed)) desc limit 0,1";
		Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[] { strJgId });
		if (map != null) {
			String skinstor = (String) map.get("TZ_SKIN_STOR");
			String strSiteId = (String) map.get("TZ_SITEI_ID");
			String websitePath = getSysHardCodeVal.getWebsiteFileUploadPath();
			String strCssDir = request.getContextPath();
			if ("".equals(skinstor) || skinstor == null) {
				strCssDir = strCssDir + websitePath + "/" + strJgId + "/" + strSiteId + "/";
			} else {
				strCssDir = strCssDir + websitePath + "/" + strJgId + "/" + strSiteId + "/" + skinstor + "/";
			}

			String strRandom = String.valueOf(10*Math.random());
			if (strJgId != null && !"".equals(strJgId)) {
				strConent = strConent.replace("page_stylecss",
						strCssDir + "style_" + strJgId.toLowerCase() + ".css?v=" + strRandom);
			} else {
				strConent = strConent.replace("page_stylecss", strCssDir + "style.css?v=" + strRandom);
			}

		}

		return strConent;
	}

	// 替换站点id值; PS类：TZ_SITE_REP_CSS.repSiteid;
	public String repSiteid(String strConent, String strSiteId) {
		if (strConent == null || "".equals(strConent)) {
			return strConent;
		}

		int numCharstart = strConent.indexOf("<input id=\"siteid\" name=\"siteid\"");
		if (numCharstart >= 0) {
			int numCharend = strConent.indexOf(">", numCharstart);
			if (numCharend > numCharstart) {
				String strCharsub = strConent.substring(numCharstart, numCharend + 1);
				strConent = strConent.replace(strCharsub,
						"<input id=\"siteid\" name=\"siteid\" type=\"hidden\" value=\"" + strSiteId + "\">");
			}
		}

		return strConent;
	}

	// 替换机构id值; PS类：TZ_SITE_REP_CSS.repJgid;
	public String repJgid(String strConent, String strJgId) {
		if (strConent == null || "".equals(strConent)) {
			return strConent;
		}

		int numCharstart = strConent.indexOf("<input id=\"jgid\" name=\"jgid\"");
		if (numCharstart >= 0) {
			int numCharend = strConent.indexOf(">", numCharstart);
			if (numCharend > numCharstart) {
				String strCharsub = strConent.substring(numCharstart, numCharend + 1);
				strConent = strConent.replace(strCharsub,
						"<input id=\"jgid\" name=\"jgid\" type=\"hidden\" value=\"" + strJgId + "\">");
			}
		}

		return strConent;
	}

	// 替换language值; PS类：TZ_SITE_REP_CSS.repLang;
	public String repLang(String strConent, String strLang) {
		if (strConent == null || "".equals(strConent)) {
			return strConent;
		}

		int numCharstart = strConent.indexOf("<input id=\"lang\" name=\"lang\"");
		if (numCharstart >= 0) {
			int numCharend = strConent.indexOf(">", numCharstart);
			if (numCharend > numCharstart) {
				String strCharsub = strConent.substring(numCharstart, numCharend + 1);
				strConent = strConent.replace(strCharsub,
						"<input id=\"lang\" name=\"lang\" type=\"hidden\" value=\"" + strLang + "\">");
			}
		}

		return strConent;
	}

	// 替换menuname值; PS类：TZ_SITE_REP_CSS.repMenuName;
	public String repMenuName(String strConent, String strMenuName) {
		if (strConent == null || "".equals(strConent)) {
			return strConent;
		}

		int numCharstart = strConent.indexOf("<span id=\"menuName\">");
		if (numCharstart >= 0) {
			int numCharend = strConent.indexOf("</span>", numCharstart);
			if (numCharend > numCharstart) {
				String strCharsub = strConent.substring(numCharstart, numCharend + 7);
				strConent = strConent.replace(strCharsub, "<span id=\"menuName\">" + strMenuName + "</span>");
			}
		}

		return strConent;
	}

	// 替换repOpr值; PS类：TZ_SITE_REP_CSS.repOpr;
	public String repOpr(String strConent, String strOperator) {
		if (strConent == null || "".equals(strConent)) {
			return strConent;
		}

		int numCharstart = strConent.indexOf("<input id=\"operator\" name=\"operator\"");
		if (numCharstart >= 0) {
			int numCharend = strConent.indexOf(">", numCharstart);
			if (numCharend > numCharstart) {
				String strCharsub = strConent.substring(numCharstart, numCharend + 1);
				strConent = strConent.replace(strCharsub,
						"<input id=\"operator\" name=\"operator\" type=\"hidden\" value=\"" + strOperator + "\">");
			}
		}

		return strConent;
	}

	// 替换repWelcome值; PS类：TZ_SITE_REP_CSS.repWelcome;
	public String repWelcome(String strConent, String strWelcome) {
		if (strConent == null || "".equals(strConent)) {
			return strConent;
		}

		strWelcome = "<span id=\"welcome\"></span>";

		int numCharstart = strConent.indexOf("<span id=\"welcome\">");
		if (numCharstart >= 0) {
			int numCharend = strConent.indexOf("</span>", numCharstart);
			if (numCharend > numCharstart) {
				String strCharsub = strConent.substring(numCharstart, numCharend + 7);
				strConent = strConent.replace(strCharsub, strWelcome);
			}
		}

		return strConent;
	}

	// 替换sdkbar值; PS类：TZ_SITE_REP_CSS.repSdkbar;
	public String repSdkbar(String strConent, String strSdkbar) {
		if (strConent == null || "".equals(strConent)) {
			return strConent;
		}

		int numCharstart = strConent.indexOf("<div id=\"sdkbar\">");
		if (numCharstart >= 0) {
			int numCharend = strConent.indexOf("</div>", numCharstart);
			if (numCharend > numCharstart) {
				String startContent = strConent.substring(0, numCharstart);

				int numCharend1 = strConent.indexOf("</div>", numCharend + 6);

				String endContent = strConent.substring(numCharend1 + 6, strConent.length());
				strConent = startContent + strSdkbar + endContent;
			}
		}

		return strConent;
	}
}
