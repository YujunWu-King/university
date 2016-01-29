/**
 * 
 */
package com.tranzvision.gd.TZSitePageBundle.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiAreaTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiAreaTWithBLOBs;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 原PS：TZ_SITE_DECORATED_APP:TZ_HY_DECORATED_CLS
 * 
 * @author SHIHUA
 * @since 2016-01-14
 */
@Service("com.tranzvision.gd.TZSitePageBundle.service.impl.TzHyDecoratedActionServiceImpl")
public class TzHyDecoratedActionServiceImpl extends TzSiteActionServiceImpl {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzGDObject;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private PsTzSiteiAreaTMapper psTzSiteiAreaTMapper;

	@Autowired
	private SiteRepCssServiceImpl siteRepCssServiceImpl;

	@Override
	public String tzSaveArea(Map<String, Object> mapActData, String[] errMsg) {

		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			Date dateNow = new Date();
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

			String strSiteId = mapActData.get("siteId") == null ? "" : String.valueOf(mapActData.get("siteId"));

			String strAreaId = mapActData.get("areaId") == null ? "" : String.valueOf(mapActData.get("areaId"));

			// String strAreaZone =
			// mapActData.get("areaZone")==null?"":String.valueOf(mapActData.get("areaZone"));

			String strAreaType = mapActData.get("areaType") == null ? "" : String.valueOf(mapActData.get("areaType"));

			String strAreaCode = mapActData.get("areaCode") == null ? "" : String.valueOf(mapActData.get("areaCode"));

			if (null == strAreaId || "".equals(strAreaId)) {
				String sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzAreaIdFromSiteidAreatype");
				strAreaId = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strAreaType }, "String");
			}

			if (null != strAreaCode && !"".equals(strAreaCode)) {

				String sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteSkinId");
				String strSkinId = sqlQuery.queryForObject(sql, new Object[] { strSiteId }, "String");

				strAreaCode = siteRepCssServiceImpl.repResetContextPath(strAreaCode);
				strAreaCode = siteRepCssServiceImpl.repResetSkinsImgPath(strAreaCode, strSkinId);

				PsTzSiteiAreaTWithBLOBs psTzSiteiAreaTWithBLOBs = new PsTzSiteiAreaTWithBLOBs();

				psTzSiteiAreaTWithBLOBs.setTzSiteiId(strSiteId);
				psTzSiteiAreaTWithBLOBs.setTzAreaId(strAreaId);
				psTzSiteiAreaTWithBLOBs.setTzAreaSavecode(strAreaCode);
				psTzSiteiAreaTWithBLOBs.setTzLastmantDttm(dateNow);
				psTzSiteiAreaTWithBLOBs.setTzLastmantOprid(oprid);

				psTzSiteiAreaTMapper.updateByPrimaryKeySelective(psTzSiteiAreaTWithBLOBs);
			}

			mapRet.put("success", true);
			errMsg[0] = "0";

		} catch (Exception e) {
			e.printStackTrace();
			mapRet.put("success", false);
			errMsg[0] = "1";
			errMsg[1] = "保存失败！";
		}
		strRet = jacksonUtil.Map2json(mapRet);
		return strRet;

	}

	@Override
	public String tzReleaseArea(Map<String, Object> mapActData, String[] errMsg) {

		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			Date dateNow = new Date();
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

			String strSiteId = mapActData.get("siteId") == null ? "" : String.valueOf(mapActData.get("siteId"));

			String strAreaId = mapActData.get("areaId") == null ? "" : String.valueOf(mapActData.get("areaId"));

			// String strAreaZone =
			// mapActData.get("areaZone")==null?"":String.valueOf(mapActData.get("areaZone"));

			String strAreaType = mapActData.get("areaType") == null ? "" : String.valueOf(mapActData.get("areaType"));

			String strAreaCode = mapActData.get("areaCode") == null ? "" : String.valueOf(mapActData.get("areaCode"));

			if (null == strAreaId || "".equals(strAreaId)) {
				String sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzAreaIdFromSiteidAreatype");
				strAreaId = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strAreaType }, "String");
			}

			if (null != strAreaCode && !"".equals(strAreaCode)) {
				PsTzSiteiAreaTWithBLOBs psTzSiteiAreaTWithBLOBs = new PsTzSiteiAreaTWithBLOBs();

				psTzSiteiAreaTWithBLOBs.setTzSiteiId(strSiteId);
				psTzSiteiAreaTWithBLOBs.setTzAreaId(strAreaId);
				psTzSiteiAreaTWithBLOBs.setTzAreaPubcode(strAreaCode);
				psTzSiteiAreaTWithBLOBs.setTzLastmantDttm(dateNow);
				psTzSiteiAreaTWithBLOBs.setTzLastmantOprid(oprid);

				psTzSiteiAreaTMapper.updateByPrimaryKeySelective(psTzSiteiAreaTWithBLOBs);
			}

			mapRet.put("success", true);
			errMsg[0] = "0";

		} catch (Exception e) {
			e.printStackTrace();
			mapRet.put("success", false);
			errMsg[0] = "1";
			errMsg[1] = "发布失败！";
		}
		strRet = jacksonUtil.Map2json(mapRet);
		return strRet;

	}

}
