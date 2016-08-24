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
 * 原PS：TZ_SITE_DECORATED_APP:TZ_MENU_TYPE_CLS
 * 
 * @author SHIHUA
 * @since 2015-12-18
 */
@Service("com.tranzvision.gd.TZSitePageBundle.service.impl.TzMenuTypeServiceImpl")
public class TzMenuTypeServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzGDObject;

	@Autowired
	private TzPhDecoratedServiceImpl tzPhDecoratedServiceImpl;

	@Override
	public String tzQuery(String strParams, String[] errMsg) {

		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			jacksonUtil.json2Map(strParams);

			String strSiteId = jacksonUtil.getString("siteId");

			if (null != strSiteId && !"".equals(strSiteId)) {

				String sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteMenuTypes");
				List<Map<String, Object>> listMenuTypes = sqlQuery.queryForList(sql, new Object[] { strSiteId });

				ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();

				for (Map<String, Object> mapMenuType : listMenuTypes) {

					String strMenuTypeId = mapMenuType.get("TZ_MENU_TYPE_ID") == null ? ""
							: String.valueOf(mapMenuType.get("TZ_MENU_TYPE_ID"));
					String strMenuTypeName = mapMenuType.get("TZ_MENU_TYPE_NAME") == null ? ""
							: String.valueOf(mapMenuType.get("TZ_MENU_TYPE_NAME"));
					String strCanAddMenu = mapMenuType.get("TZ_IS_ADD") == null ? ""
							: String.valueOf(mapMenuType.get("TZ_IS_ADD"));
					String strCanAddCOLU = mapMenuType.get("TZ_ADD_COLU") == null ? ""
							: String.valueOf(mapMenuType.get("TZ_ADD_COLU"));
					String strTypeDescr = mapMenuType.get("TZ_TYPE_DESCR") == null ? ""
							: String.valueOf(mapMenuType.get("TZ_TYPE_DESCR"));

					sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteMenuImgsFromCDPF");
					Map<String, Object> mapMenuImgs = sqlQuery.queryForMap(sql,
							new Object[] { strSiteId, strMenuTypeId, strSiteId });
					String strMenuTypeImg = "";
					String strMenuNowImg = "";
					if(mapMenuImgs != null){
						strMenuTypeImg = mapMenuImgs.get("TZ_TYPE_IMG") == null ? ""
								: String.valueOf(mapMenuImgs.get("TZ_TYPE_IMG"));
						strMenuNowImg = mapMenuImgs.get("TZ_NOW_IMG") == null ? ""
								: String.valueOf(mapMenuImgs.get("TZ_NOW_IMG"));
					}
					

					// 此处要找到替代方法
					String todoCheck;
					// strTypeDescr = EscapeJavascriptString(strTypeDescr);

					Map<String, Object> mapJson = new HashMap<String, Object>();
					mapJson.put("value", strMenuTypeId);
					mapJson.put("text", strMenuTypeName);
					mapJson.put("addmenu", strCanAddMenu);
					mapJson.put("addcolu", strCanAddCOLU);
					mapJson.put("typedescr", strTypeDescr);
					mapJson.put("menutypeimg", strMenuTypeImg);
					mapJson.put("menunowimg", strMenuNowImg);

					listJson.add(mapJson);
				}

				return jacksonUtil.List2json(listJson);

			} else {

				mapRet.put("success", false);
				errMsg[0] = "1";
				errMsg[1] = "请先开通站点！";

			}

		} catch (Exception e) {
			e.printStackTrace();
			mapRet.put("success", false);
			errMsg[0] = "1";
			errMsg[1] = "请求编辑区域异常！";

		}

		strRet = jacksonUtil.Map2json(mapRet);

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
				// 信息内容;
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

					String[] aryParam = new String[] { jacksonUtil.Map2json(mapData) };

					strRet = tzPhDecoratedServiceImpl.tzUpdate(aryParam, errMsg);
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
			mapRet.put("success", false);
			errMsg[0] = "1";
			errMsg[1] = "请求编辑的区域异常！";
		}

		strRet = jacksonUtil.Map2json(mapRet);

		return strRet;
	}

}
