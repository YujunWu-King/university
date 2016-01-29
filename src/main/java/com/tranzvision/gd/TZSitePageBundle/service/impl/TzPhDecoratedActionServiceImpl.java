/**
 * 
 */
package com.tranzvision.gd.TZSitePageBundle.service.impl;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzWebsiteLoginServiceImpl;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiAreaTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiAreaTWithBLOBs;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 原PS：TZ_SITE_DECORATED_APP:TZ_PH_DECORATED_CLS
 * 
 * @author SHIHUA
 * @since 2016-01-14
 */
@Service("com.tranzvision.gd.TZSitePageBundle.service.impl.TzPhDecoratedActionServiceImpl")
public class TzPhDecoratedActionServiceImpl extends TzSiteActionServiceImpl {

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzGDObject;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private TzWebsiteLoginServiceImpl tzWebsiteLoginServiceImpl;

	@Autowired
	private PsTzSiteiAreaTMapper psTzSiteiAreaTMapper;
	
	@Autowired
	private SiteRepCssServiceImpl siteRepCssServiceImpl;

	@Override
	public String tzSaveArea(Map<String, Object> mapActData, String[] errMsg) {
		String strRet = "";

		try {

			String strSiteId = mapActData.get("siteId") == null ? "" : String.valueOf(mapActData.get("siteId"));
			String strAreaId = mapActData.get("areaId") == null ? "" : String.valueOf(mapActData.get("areaId"));
			// String strAreaZone =
			// mapActData.get("areaZone")==null?"":String.valueOf(mapActData.get("areaZone"));
			String strAreaType = mapActData.get("areaType") == null ? "" : String.valueOf(mapActData.get("areaType"));
			String strAreaCode = mapActData.get("areaCode") == null ? "" : String.valueOf(mapActData.get("areaCode"));

			if (null == strAreaId || "".equals(strAreaId)) {
				String sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzAreaIdFromSiteidAreatypeStateY");
				strAreaId = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strAreaType }, "String");
			}

			if (null != strAreaCode && !"".equals(strAreaCode)) {
				
				strAreaCode = siteRepCssServiceImpl.repWelcome(strAreaCode, "");
				
				String sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteSkinId");
				String strSkinId = sqlQuery.queryForObject(sql, new Object[]{strSiteId}, "String");
				
				strAreaCode = siteRepCssServiceImpl.repResetContextPath(strAreaCode);
				strAreaCode = siteRepCssServiceImpl.repResetSkinsImgPath(strAreaCode, strSkinId);

				PsTzSiteiAreaTWithBLOBs psTzSiteiAreaTWithBLOBs = new PsTzSiteiAreaTWithBLOBs();

				psTzSiteiAreaTWithBLOBs.setTzSiteiId(strSiteId);
				psTzSiteiAreaTWithBLOBs.setTzAreaId(strAreaId);
				psTzSiteiAreaTWithBLOBs.setTzAreaSavecode(strAreaCode);
				psTzSiteiAreaTWithBLOBs.setTzLastmantDttm(new Date());
				psTzSiteiAreaTWithBLOBs.setTzLastmantOprid(tzLoginServiceImpl.getLoginedManagerOprid(request));

				psTzSiteiAreaTMapper.updateByPrimaryKeySelective(psTzSiteiAreaTWithBLOBs);

			}

			errMsg[0] = "0";
			strRet = "{\"success\":true}";

		} catch (Exception e) {
			e.printStackTrace();
			strRet = "{\"success\":false}";
			errMsg[0] = "1";
			errMsg[1] = "页头区域保存时异常！";
		}

		return strRet;
	}

	@Override
	public String tzReleaseArea(Map<String, Object> mapActData, String[] errMsg) {
		String strRet = "";

		try {

			String strSiteId = mapActData.get("siteId") == null ? "" : String.valueOf(mapActData.get("siteId"));
			String strAreaId = mapActData.get("areaId") == null ? "" : String.valueOf(mapActData.get("areaId"));
			// String strAreaZone =
			// mapActData.get("areaZone")==null?"":String.valueOf(mapActData.get("areaZone"));
			String strAreaType = mapActData.get("areaType") == null ? "" : String.valueOf(mapActData.get("areaType"));
			String strAreaCode = mapActData.get("areaCode") == null ? "" : String.valueOf(mapActData.get("areaCode"));

			if (null == strAreaId || "".equals(strAreaId)) {
				String sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzAreaIdFromSiteidAreatypeStateY");
				strAreaId = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strAreaType }, "String");
			}

			if (null != strAreaCode && !"".equals(strAreaCode)) {

				strAreaCode = siteRepCssServiceImpl.repWelcome(strAreaCode, "");
				
				PsTzSiteiAreaTWithBLOBs psTzSiteiAreaTWithBLOBs = new PsTzSiteiAreaTWithBLOBs();

				psTzSiteiAreaTWithBLOBs.setTzSiteiId(strSiteId);
				psTzSiteiAreaTWithBLOBs.setTzAreaId(strAreaId);
				psTzSiteiAreaTWithBLOBs.setTzAreaPubcode(strAreaCode);
				psTzSiteiAreaTWithBLOBs.setTzLastmantDttm(new Date());
				psTzSiteiAreaTWithBLOBs.setTzLastmantOprid(tzLoginServiceImpl.getLoginedManagerOprid(request));

				psTzSiteiAreaTMapper.updateByPrimaryKeySelective(psTzSiteiAreaTWithBLOBs);

			}

			errMsg[0] = "0";
			strRet = "{\"success\":true}";

		} catch (Exception e) {
			e.printStackTrace();
			strRet = "{\"success\":false}";
			errMsg[0] = "1";
			errMsg[1] = "页头区域保存时异常！";
		}

		return strRet;
	}

	@Override
	public String tzGetHtmlContent(String strParams) {

		String strRet = "";

		try {

			JacksonUtil jacksonUtil = new JacksonUtil();

			jacksonUtil.json2Map(strParams);

			String strOrgId = jacksonUtil.getString("orgId");
			String strSiteId = jacksonUtil.getString("siteId");
			String strAreaId = jacksonUtil.getString("areaId");
			// String strAreaZone = jacksonUtil.getString("areaZone");
			String strAreaType = jacksonUtil.getString("areaType");
			String strOprate = jacksonUtil.getString("oprate");

			if ((null == strOrgId || "".equals(strOrgId)) && (null == strSiteId || "".equals(strSiteId))) {
				strOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
				if (null == strOrgId || "".equals(strOrgId)) {
					strOrgId = tzWebsiteLoginServiceImpl.getLoginedUserOrgid(request);
					if (null == strOrgId || "".equals(strOrgId)) {
						return "false";
					}
				}
			}

			String sql = "";

			if ((null != strOrgId && !"".equals(strOrgId)) && (null == strSiteId || "".equals(strSiteId))) {
				sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteidByOrgid");
				strSiteId = sqlQuery.queryForObject(sql, new Object[] { strOrgId }, "String");
			}

			if (null == strAreaId || "".equals(strAreaId)) {
				sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzAreaIdFromSiteidAreatypeStateY");
				strAreaId = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strAreaType }, "String");
			}

			switch (strOprate) {
			case "R":
				sql = "select TZ_AREA_PUBCODE from PS_TZ_SITEI_AREA_T where TZ_SITEI_ID=? and TZ_AREA_ID=? and TZ_AREA_STATE='Y'";
				strRet = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strAreaId }, "String");
				break;
			case "P":
			case "D":
				sql = "select TZ_AREA_SAVECODE from PS_TZ_SITEI_AREA_T where TZ_SITEI_ID=? and TZ_AREA_ID=? and TZ_AREA_STATE='Y'";
				strRet = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strAreaId }, "String");
				
				sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteSkinId");
				String strSkinID = sqlQuery.queryForObject(sql, new Object[] { strSiteId }, "String");
				
				strRet = siteRepCssServiceImpl.repSkinsImgPath(strRet, strSkinID);
				
				break;
			}

			if (null == strRet || "".equals(strRet)) {
				strRet = "false";
			} else {

				strRet = siteRepCssServiceImpl.repContextPath(strRet);

			}

		} catch (Exception e) {
			e.printStackTrace();
			strRet = "false";
		}

		return strRet;

	}

}
