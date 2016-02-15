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
 * 获取活动图片集信息，原PS：TZ_GD_HDGL:ActivityPics
 * 
 * @author SHIHUA
 * @since 2016-02-04
 */
@Service("com.tranzvision.gd.TZEventsBundle.service.impl.TzEventsPicsServiceImpl")
public class TzEventsPicsServiceImpl extends FrameworkImpl {

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

			String sql = "select count(*) from PS_TZ_ART_PIC_T where TZ_ART_ID=?";
			int total = sqlQuery.queryForObject(sql, new Object[] { strArtId }, "int");

			ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();

			if (total > 0) {
				sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzGetPicsList");
				List<Map<String, Object>> listData = sqlQuery.queryForList(sql, new Object[] { strArtId });

				for (Map<String, Object> mapData : listData) {

					String tzImgUrl = mapData.get("TZ_IMG_URL") == null ? ""
							: String.valueOf(mapData.get("TZ_IMG_URL"));
					String sysImageName = mapData.get("TZ_ATTSYSFILENAME") == null ? ""
							: String.valueOf(mapData.get("TZ_ATTSYSFILENAME"));
					if (tzImgUrl.indexOf('/') > 0) {
						tzImgUrl = tzImgUrl + sysImageName;
					} else {
						tzImgUrl = tzImgUrl + "/" + sysImageName;
					}

					Map<String, Object> mapJson = new HashMap<String, Object>();

					mapJson.put("imageId",
							mapData.get("TZ_IMG_ID") == null ? "" : String.valueOf(mapData.get("TZ_IMG_ID")));

					mapJson.put("imagePriority", mapData.get("TZ_PRIORITY") == null ? 0
							: Integer.parseInt(String.valueOf(mapData.get("TZ_PRIORITY"))));

					mapJson.put("imageUrl", tzImgUrl);

					mapJson.put("imageDesc",
							mapData.get("TZ_IMG_DESCR") == null ? "" : String.valueOf(mapData.get("TZ_IMG_DESCR")));

					mapJson.put("imageTrsUrl",
							mapData.get("TZ_IMG_TRS_URL") == null ? "" : String.valueOf(mapData.get("TZ_IMG_TRS_URL")));

					mapJson.put("imageTrsTitle",
							mapData.get("TZ_TITLE_200") == null ? "" : String.valueOf(mapData.get("TZ_TITLE_200")));

					mapJson.put("sysImageName", sysImageName);

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
