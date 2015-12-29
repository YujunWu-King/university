/**
 * 
 */
package com.tranzvision.gd.TZSiteStyleBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 站点风格选择预览功能，原PS：TZ_SITE_DECORATED_APP:TZ_IMAGE_LL_CLS
 * 
 * @author SHIHUA
 * @since 2015-12-11
 */
@Service("com.tranzvision.gd.TZSiteStyleBundle.service.impl.TzSitePageImageView")
public class TzSitePageImageView extends FrameworkImpl {

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzSQLObject;

	@SuppressWarnings("unchecked")
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			
			jacksonUtil.json2Map(strParams);
			String siteId = jacksonUtil.getString("siteId");
			String skinId = jacksonUtil.getString("skinId");

			String sql = tzSQLObject.getSQLText("SQL.TZSitePageBundle.TzGetImageView");

			List<?> listData = sqlQuery.queryForList(sql, new Object[] { siteId, skinId });

			ArrayList<String> listJson = new ArrayList<String>();

			int i = 0;
			int currNum = 0;
			for (Object objData : listData) {

				Map<String, Object> mapData = (Map<String, Object>) objData;

				if (mapData.get("TZ_IMG_VIEW") != null) {
					listJson.add(String.valueOf(mapData.get("TZ_IMG_VIEW")));
				}

				if (skinId.equals(String.valueOf(mapData.get("TZ_SKIN_ID")))) {
					currNum = i;
				}

				i++;

			}

			Map<String, Object> mapRet = new HashMap<String, Object>();
			mapRet.put("current", currNum);
			mapRet.put("skinList", listJson);
			strRet = jacksonUtil.Map2json(mapRet);

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
}
