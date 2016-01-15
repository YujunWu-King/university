/**
 * 
 */
package com.tranzvision.gd.TZSitePageBundle.service.impl;

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
 * 原PS：TZ_SITE_DECORATED_APP:TZ_COLU_TYPE_CLS
 * 
 * @author SHIHUA
 * @since 2015-12-15
 */
@Service("com.tranzvision.gd.TZSitePageBundle.service.impl.TzColuTypeServiceImpl")
public class TzColuTypeServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzGDObject;

	@Autowired
	private TzPhDecoratedServiceImpl tzPhDecoratedServiceImpl;

	@SuppressWarnings("unchecked")
	@Override
	public String tzQuery(String strParams, String[] errMsg) {

		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			jacksonUtil.json2Map(strParams);
			String strSiteId = jacksonUtil.getString("siteId");

			String strMenuId = jacksonUtil.getString("menuId");

			String strType = jacksonUtil.getString("type");

			String strColuId = "";
			String strColuConT = "";

			String sql = "";

			if ("some".equals(strType)) {
				if (null != strMenuId && !"".equals(strMenuId)) {

					sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetMenuColumnId");
					strColuId = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strMenuId }, "String");

					if (null != strColuId && !"".equals(strColuId)) {
						sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetMenuColumnType");
						strColuConT = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strColuId }, "String");
					}

				}
			}

			if (null != strSiteId && !"".equals(strSiteId)) {

				List<?> listData = new ArrayList<>();
				if ("some".equals(strType)) {
					sql = "select TZ_COLU_ID,TZ_COLU_NAME from PS_TZ_SITEI_COLU_T where TZ_SITEI_ID=? and TZ_CONT_TYPE=? AND ltrim(rtrim(TZ_CONT_TYPE))<>''";
					listData = sqlQuery.queryForList(sql, new Object[] { strSiteId, strColuConT });
				} else {
					sql = "select TZ_COLU_ID,TZ_COLU_NAME from PS_TZ_SITEI_COLU_T where TZ_SITEI_ID=?";
					listData = sqlQuery.queryForList(sql, new Object[] { strSiteId });
				}

				ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();

				for (Object obj : listData) {

					Map<String, Object> mapData = (Map<String, Object>) obj;

					Map<String, Object> mapJson = new HashMap<String, Object>();

					strColuId = mapData.get("TZ_COLU_ID") == null ? "" : String.valueOf(mapData.get("TZ_COLU_ID"));
					String strColuName = mapData.get("TZ_COLU_NAME") == null ? ""
							: String.valueOf(mapData.get("TZ_COLU_NAME"));

					mapJson.put("value", strColuId);
					mapJson.put("text", strColuName);
					mapJson.put("addmenu", "");
					mapJson.put("addcolu", "");
					mapJson.put("typedescr", "");
					mapJson.put("menutypeimg", "");
					mapJson.put("menunowimg", "");

					listJson.add(mapJson);

				}

				strRet = jacksonUtil.List2json(listJson);

			} else {
				mapRet.put("success", false);
				strRet = jacksonUtil.Map2json(mapRet);
				errMsg[0] = "1";
				errMsg[1] = "请先开通站点！";
			}

		} catch (Exception e) {
			e.printStackTrace();
			mapRet.put("success", false);
			strRet = jacksonUtil.Map2json(mapRet);
			errMsg[0] = "1";
			errMsg[1] = "请求编辑区域异常！";
		}

		return strRet;

	}

	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {

				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				// 类型标志;
				// String strFlag = jacksonUtil.getString("typeFlag");

				Map<String, Object> mapData = jacksonUtil.getMap("data");

				String strAreaId = mapData.get("areaId") == null ? "" : String.valueOf(mapData.get("areaId"));

				// String strAreaZone = mapData.get("areaZone") == null ? "" :
				// String.valueOf(mapData.get("areaZone"));

				String strAreaType = mapData.get("areaType") == null ? "" : String.valueOf(mapData.get("areaType"));

				String strAreaTypeId = "";

				if (null != strAreaId && !"".equals(strAreaId)) {
					String sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetAreatypeidByAreaid");
					strAreaTypeId = sqlQuery.queryForObject(sql, new Object[] { strAreaId }, "String");
				} else {
					String sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetAreatypeidByAreatype");
					strAreaTypeId = sqlQuery.queryForObject(sql, new Object[] { strAreaType }, "String");
				}

				if (null != strAreaTypeId && !"".equals(strAreaTypeId)) {

					String paramStr = jacksonUtil.Map2json(mapData);
					String[] paramAry = new String[] { paramStr };

					strRet = tzPhDecoratedServiceImpl.tzUpdate(paramAry, errMsg);

					errMsg[0] = "0";

					return strRet;

				} else {
					mapRet.put("success", false);
					errMsg[0] = "1";
					errMsg[1] = "请求编辑的区域不存在！";
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "0";
			errMsg[1] = "";
		}

		return strRet;
	}

}
