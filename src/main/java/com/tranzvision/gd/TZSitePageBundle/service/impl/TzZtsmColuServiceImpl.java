/**
 * 
 */
package com.tranzvision.gd.TZSitePageBundle.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 原PS；TZ_SITE_DECORATED_APP:TZ_ZTSM_COLU_CLS
 * 
 * @author SHIHUA
 * @since 2015-12-21
 */
@Service("com.tranzvision.gd.TZSitePageBundle.service.impl.TzZtsmColuServiceImpl")
public class TzZtsmColuServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzGDObject;

	@Override
	public String tzQuery(String strParams, String[] errMsg) {

		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			jacksonUtil.json2Map(strParams);

			String strSiteId = jacksonUtil.getString("siteId");
			String strMenuId = jacksonUtil.getString("menuId");
			// int numNowPage = jacksonUtil.getInt("page");

			String sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteArtContent");
			String strContent = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strSiteId, strMenuId },
					"String");

			/*
			 * strContent = strContent.replace((char) (10), ' '); strContent =
			 * strContent.replace((char) (13), ' '); strContent =
			 * strContent.replace("\\", "\\\\"); strContent =
			 * strContent.replace("'", "\\'"); strContent =
			 * strContent.replace("\"", "\\\"");
			 */

			Map<String, Object> mapJson = new HashMap<String, Object>();
			mapJson.put("htmlCont", strContent);

			strRet = jacksonUtil.Map2json(mapJson);

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "获取数据失败！";
		}

		return strRet;

	}

}
