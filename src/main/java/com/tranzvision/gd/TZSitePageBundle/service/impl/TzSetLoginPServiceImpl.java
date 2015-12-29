/**
 * 
 */
package com.tranzvision.gd.TZSitePageBundle.service.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 原PS：TZ_SITE_DECORATED_APP:TZ_SET_LOGINP_CLS
 * 
 * @author SHIHUA
 * @since 2015-12-18
 */
@Service("com.tranzvision.gd.TZSitePageBundle.service.impl.TzSetLoginPServiceImpl")
public class TzSetLoginPServiceImpl extends FrameworkImpl {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzGDObject;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private SiteRepCssServiceImpl siteRepCssServiceImpl;

	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	
	@Override
	public String tzGetHtmlContent(String strParams) {

		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			String strSiteId = request.getParameter("siteId");
			String oprate = request.getParameter("oprate");
			String classid = request.getParameter("classid");

			String comId = "TZ_SITEI_SETED_COM";
			String pageId = "TZ_SET_LOGINP_STD";

			if (null != classid && !"".equals(classid)) {
				String sql = "select TZ_COM_ID,TZ_PAGE_ID from PS_TZ_AQ_PAGZC_TBL where TZ_PAGE_REFCODE=?";
				Map<String, Object> mapCom = sqlQuery.queryForMap(sql, new Object[] { classid });
				comId = mapCom.get("TZ_COM_ID") == null ? comId : String.valueOf(mapCom.get("TZ_COM_ID"));
				pageId = mapCom.get("TZ_PAGE_ID") == null ? pageId : String.valueOf(mapCom.get("TZ_PAGE_ID"));

			} else {
				jacksonUtil.json2Map(strParams);
				strSiteId = jacksonUtil.getString("siteId");
				oprate = jacksonUtil.getString("oprate");
			}

			if (null == oprate || "".equals(oprate)) {
				oprate = "R";
			}

			String userid = tzLoginServiceImpl.getLoginedManagerOprid(request);

			// 判断当前人员是否有权限
			String sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetUserEditPermit");
			String tzUserEditFlg = sqlQuery.queryForObject(sql, new Object[] { userid, comId, pageId }, "String");

			sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetUserDisplayOnlyPermit");
			String tzUserDisplayOnly = sqlQuery.queryForObject(sql, new Object[] { userid, comId, pageId }, "String");

			sql = "select TZ_JG_ID,TZ_SKIN_STOR,TZ_LOGIN_PRECODE,TZ_LONGIN_SAVECODE,TZ_LONGIN_PUBCODE,TZ_SITE_LANG from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
			Map<String, Object> mapData = sqlQuery.queryForMap(sql, new Object[] { strSiteId });

			if (mapData != null) {

				String strResultContent = "";
				boolean canShow = true;
				switch (oprate) {
				case "R":
					if (!"1".equals(tzUserDisplayOnly) && !"1".equals(tzUserEditFlg)) {
						canShow = false;
					} else {
						strResultContent = mapData.get("TZ_LONGIN_PUBCODE") == null ? ""
								: String.valueOf(mapData.get("TZ_LONGIN_PUBCODE"));
					}
					break;

				case "D":
					if (!"1".equals(tzUserEditFlg)) {
						canShow = false;
					} else {
						strResultContent = mapData.get("TZ_LONGIN_SAVECODE") == null ? ""
								: String.valueOf(mapData.get("TZ_LONGIN_SAVECODE"));
					}
					break;

				case "P":
					if (!"1".equals(tzUserEditFlg)) {
						canShow = false;
					} else {
						strResultContent = mapData.get("TZ_LOGIN_PRECODE") == null ? ""
								: String.valueOf(mapData.get("TZ_LOGIN_PRECODE"));
					}
					break;
				}

				if (!canShow) {
					return "无权访问";
				}

				String orgid = mapData.get("TZ_JG_ID") == null ? ""
						: String.valueOf(mapData.get("TZ_JG_ID")).toLowerCase();
				String siteLang = mapData.get("TZ_SITE_LANG") == null ? ""
						: String.valueOf(mapData.get("TZ_SITE_LANG"));

				String strCssDir = getSysHardCodeVal.getWebsiteCssPath() + "/" + orgid + "/" + strSiteId.toLowerCase();
				String strCssFilePath = strCssDir + "/style_" + orgid + ".css";

				strResultContent = strResultContent.replace("page_stylecss", strCssFilePath);
				
				strResultContent = strResultContent.replace("{TZ_GD_LOGIN_SITEI_ORG_CODE}", orgid);

				strResultContent = siteRepCssServiceImpl.repTitle(strResultContent, strSiteId);
				strResultContent = siteRepCssServiceImpl.repCss(strResultContent, strSiteId);
				strResultContent = siteRepCssServiceImpl.repSiteid(strResultContent, strSiteId);
				strResultContent = siteRepCssServiceImpl.repJgid(strResultContent, orgid.toUpperCase());
				strResultContent = siteRepCssServiceImpl.repLang(strResultContent, siteLang.toUpperCase());
				strResultContent = siteRepCssServiceImpl.repOpr(strResultContent, oprate.toUpperCase());

				return strResultContent;
			} else {
				strRet = "请求内容不存在！";
			}

		} catch (Exception e) {
			e.printStackTrace();
			strRet = "请求内容失败！" + e.getMessage();
		}

		return strRet;

	}
	
}
