/**
 * 
 */
package com.tranzvision.gd.TZSitePageBundle.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 站点页面设置功能，原PS：TZ_SITEHOME_PKG:TZ_SITEHOME_CLS
 * 
 * @author SHIHUA
 * @since 2015-12-07
 */
public class TzSitePageMgServiceImpl extends FrameworkImpl {

	@Autowired
	private JacksonUtil jacksonUtil;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Override
	public String tzGetJsonData(String strParams) {

		String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);

		String sql = "select TZ_SITEI_ID,TZ_SITEM_ID from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ENABLE='Y' and TZ_JG_ID=?";

		Map<String, Object> mapData = sqlQuery.queryForMap(sql, new Object[] { orgid });

		Map<String, Object> mapRet = new HashMap<String, Object>();

		if (null != mapData) {
			mapRet.put("siteId", mapData.get("TZ_SITEI_ID") == null ? "" : String.valueOf(mapData.get("TZ_SITEI_ID")));
			mapRet.put("siteMid", mapData.get("TZ_SITEM_ID") == null ? "" : String.valueOf(mapData.get("TZ_SITEM_ID")));
		} else {
			mapRet.put("siteId", "");
			mapRet.put("siteMid", "");
		}

		return jacksonUtil.Map2json(mapData);
	}

}
