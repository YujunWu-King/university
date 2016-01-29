/**
 * 
 */
package com.tranzvision.gd.TZSitePageBundle.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiAreaTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiAreaTWithBLOBs;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 原PS：TZ_SITE_DECORATED_APP:TZ_PT_DECORATED_CLS
 * 
 * @author SHIHUA
 * @since 2016-01-14
 */
@Service("com.tranzvision.gd.TZSitePageBundle.service.impl.TzPtDecoratedActionServiceImpl")
public class TzPtDecoratedActionServiceImpl extends TzSiteActionServiceImpl {
	
	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzGDObject;

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
			
			String strSiteId = mapActData.get("siteId") == null ? "" : String.valueOf(mapActData.get("siteId"));
			String strAreaId = mapActData.get("areaId") == null ? "" : String.valueOf(mapActData.get("areaId"));
			// String strAreaZone =
			// mapActData.get("areaZone")==null?"":String.valueOf(mapActData.get("areaZone"));
			String strAreaType = mapActData.get("areaType") == null ? "" : String.valueOf(mapActData.get("areaType"));
			String strAreaCode = mapActData.get("areaCode") == null ? "" : String.valueOf(mapActData.get("areaCode"));

			if (null != strAreaCode && !"".equals(strAreaCode)) {

				if (null == strAreaId || "".equals(strAreaId)) {
					String sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzAreaIdFromSiteidAreatype");
					strAreaId = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strAreaType }, "String");
				}
				
				String sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteSkinId");
				String strSkinId = sqlQuery.queryForObject(sql, new Object[]{strSiteId}, "String");
				
				strAreaCode = siteRepCssServiceImpl.repResetContextPath(strAreaCode);
				strAreaCode = siteRepCssServiceImpl.repResetSkinsImgPath(strAreaCode, strSkinId);

				PsTzSiteiAreaTWithBLOBs psTzSiteiAreaTWithBLOBs = new PsTzSiteiAreaTWithBLOBs();

				psTzSiteiAreaTWithBLOBs.setTzSiteiId(strSiteId);
				psTzSiteiAreaTWithBLOBs.setTzAreaId(strAreaId);
				psTzSiteiAreaTWithBLOBs.setTzAreaSavecode(strAreaCode);

				psTzSiteiAreaTMapper.updateByPrimaryKeySelective(psTzSiteiAreaTWithBLOBs);

			}

			errMsg[0] = "0";
			mapRet.put("success", true);
			strRet = jacksonUtil.Map2json(mapRet);

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "保存时异常！" + e.getMessage();
			mapRet.put("success", false);
			strRet = jacksonUtil.Map2json(mapRet);
		}

		return strRet;

	}

	@Override
	public String tzReleaseArea(Map<String, Object> mapActData, String[] errMsg) {

		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			
			String strSiteId = mapActData.get("siteId") == null ? "" : String.valueOf(mapActData.get("siteId"));
			String strAreaId = mapActData.get("areaId") == null ? "" : String.valueOf(mapActData.get("areaId"));
			// String strAreaZone =
			// mapActData.get("areaZone")==null?"":String.valueOf(mapActData.get("areaZone"));
			String strAreaType = mapActData.get("areaType") == null ? "" : String.valueOf(mapActData.get("areaType"));
			String strAreaCode = mapActData.get("areaCode") == null ? "" : String.valueOf(mapActData.get("areaCode"));

			if (null != strAreaCode && !"".equals(strAreaCode)) {

				if (null == strAreaId || "".equals(strAreaId)) {
					String sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzAreaIdFromSiteidAreatype");
					strAreaId = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strAreaType }, "String");
				}

				PsTzSiteiAreaTWithBLOBs psTzSiteiAreaTWithBLOBs = new PsTzSiteiAreaTWithBLOBs();

				psTzSiteiAreaTWithBLOBs.setTzSiteiId(strSiteId);
				psTzSiteiAreaTWithBLOBs.setTzAreaId(strAreaId);
				psTzSiteiAreaTWithBLOBs.setTzAreaPubcode(strAreaCode);

				psTzSiteiAreaTMapper.updateByPrimaryKeySelective(psTzSiteiAreaTWithBLOBs);

			}

			errMsg[0] = "0";
			mapRet.put("success", true);
			strRet = jacksonUtil.Map2json(mapRet);

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "发布时异常！" + e.getMessage();
			mapRet.put("success", false);
			strRet = jacksonUtil.Map2json(mapRet);
		}

		return strRet;

	}
	
	@Override
	public String tzGetHtmlContent(String strParams) {

		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			jacksonUtil.json2Map(strParams);

			String strSiteId = jacksonUtil.getString("siteId");

			String strAreaId = jacksonUtil.getString("areaId");

			//String strAreaZone = jacksonUtil.getString("areaZone");

			String strAreaType = jacksonUtil.getString("areaType");

			String strOprate = jacksonUtil.getString("oprate");

			if (null == strAreaId || "".equals(strAreaId)) {
				String sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzAreaIdFromSiteidAreatype");
				strAreaId = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strAreaType }, "String");
			}

			String strResultContent = "";

			String sql = "select TZ_AREA_PUBCODE,TZ_AREA_SAVECODE from PS_TZ_SITEI_AREA_T where TZ_SITEI_ID=? and TZ_AREA_ID=?";
			Map<String, Object> mapData = sqlQuery.queryForMap(sql, new Object[] { strSiteId, strAreaId });

			if (null != mapData) {

				if ("P".equals(strOprate)) {
					strResultContent = mapData.get("TZ_AREA_SAVECODE") == null ? ""
							: String.valueOf(mapData.get("TZ_AREA_SAVECODE"));
				} else if ("D".equals(strOprate)) {
					strResultContent = mapData.get("TZ_AREA_SAVECODE") == null ? ""
							: String.valueOf(mapData.get("TZ_AREA_SAVECODE"));
				} else if("R".equals(strOprate)){
					strResultContent = mapData.get("TZ_AREA_PUBCODE") == null ? ""
							: String.valueOf(mapData.get("TZ_AREA_PUBCODE"));
				}
				
				sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteSkinId");
				String strSkinID = sqlQuery.queryForObject(sql, new Object[] { strSiteId }, "String");
				
				strResultContent = siteRepCssServiceImpl.repContextPath(strResultContent);
				strResultContent = siteRepCssServiceImpl.repSkinsImgPath(strResultContent, strSkinID);

			} else {
				strResultContent = "false";
			}

			return strResultContent;

		} catch (Exception e) {
			e.printStackTrace();
			strRet = "false";
		}

		return strRet;

	}
	
}
