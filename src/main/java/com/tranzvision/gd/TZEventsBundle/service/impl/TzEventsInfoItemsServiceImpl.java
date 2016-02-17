/**
 * 
 */
package com.tranzvision.gd.TZEventsBundle.service.impl;

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
 * 获取活动报名信息项，原PS：TZ_GD_HDGL:ActivityInfoItems
 * 
 * @author SHIHUA
 * @since 2016-02-04
 */
@Service("com.tranzvision.gd.TZEventsBundle.service.impl.TzEventsInfoItemsServiceImpl")
public class TzEventsInfoItemsServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzGDObject;

	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		String strRet = "";

		try {
			JacksonUtil jacksonUtil = new JacksonUtil();
			jacksonUtil.json2Map(strParams);

			String strArtId = jacksonUtil.getString("activityId");

			String sql = "select count(*) from PS_TZ_ZXBM_XXX_T where TZ_ART_ID=?";
			int total = sqlQuery.queryForObject(sql, new Object[] { strArtId }, "int");

			ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();

			if (total > 0) {
				sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzGetEventAttsList");
				List<Map<String, Object>> listData = sqlQuery.queryForList(sql, new Object[] { strArtId });

				for (Map<String, Object> mapData : listData) {

					Map<String, Object> mapJson = new HashMap<String, Object>();

					mapJson.put("applyItemID",
							mapData.get("TZ_ZXBM_XXX_ID") == null ? "" : String.valueOf(mapData.get("TZ_ZXBM_XXX_ID")));

					mapJson.put("applyItemNum", mapData.get("TZ_PX_XH") == null ? 0
							: Integer.parseInt(String.valueOf(mapData.get("TZ_PX_XH"))));

					mapJson.put("applyItemName", mapData.get("TZ_ZXBM_XXX_NAME") == null ? ""
							: String.valueOf(mapData.get("TZ_ZXBM_XXX_NAME")));

					mapJson.put("applyItemRequired",
							mapData.get("TZ_ZXBM_XXX_BT") == null ? "" : String.valueOf(mapData.get("TZ_ZXBM_XXX_BT")));

					mapJson.put("applyItemType", mapData.get("TZ_ZXBM_XXX_ZSXS") == null ? ""
							: String.valueOf(mapData.get("TZ_ZXBM_XXX_ZSXS")));

				}

			}

			Map<String, Object> mapRet = new HashMap<String, Object>();
			mapRet.put("total", total);
			mapRet.put("root", listJson);
			strRet = jacksonUtil.Map2json(mapRet);

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "查询失败！" + e.getMessage();
		}

		return strRet;
	}

}
