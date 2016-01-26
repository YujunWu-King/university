/**
 * 
 */
package com.tranzvision.gd.TZSitePageBundle.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiAreaTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiAreaTWithBLOBs;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 原PS：TZ_SITE_DECORATED_APP:TZ_GW_DECORATED_CLS
 * 
 * @author SHIHUA
 * @since 2016-01-14
 */
@Service("com.tranzvision.gd.TZSitePageBundle.service.impl.TzGwDecoratedActionServiceImpl")
public class TzGwDecoratedActionServiceImpl extends TzSiteActionServiceImpl {

	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzGDObject;

	@Autowired
	private PsTzSiteiAreaTMapper psTzSiteiAreaTMapper;
	
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

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
				
				String ctxPath = request.getContextPath();
				if(!"".equals(ctxPath)){
					strAreaCode = strAreaCode.replace(ctxPath + "/", "{ContextPath}/");
				}
				
				String strSkinImgPath = ctxPath + getSysHardCodeVal.getWebsiteSkinsImgPath() + "/" + strSkinId + "/"; 
				strAreaCode = strAreaCode.replace(strSkinImgPath, "{ContextSkinPath}/");

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

}
