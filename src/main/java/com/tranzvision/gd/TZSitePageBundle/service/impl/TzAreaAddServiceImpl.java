/**
 * 
 */
package com.tranzvision.gd.TZSitePageBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 添加区域，原PS：TZ_SITE_DECORATED_APP:TZ_AREA_ADD_CLS
 * 
 * @author SHIHUA
 * @since 2015-12-14
 */
@Service("com.tranzvision.gd.TZSitePageBundle.service.impl.TzAreaAddServiceImpl")
public class TzAreaAddServiceImpl extends FrameworkImpl {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TzPhDecoratedServiceImpl tzPhDecoratedServiceImpl;

	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	@SuppressWarnings("unchecked")
	@Override
	public String tzQuery(String strParams, String[] errMsg) {

		Map<String, Object> mapRet = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			jacksonUtil.json2Map(strParams);
			String typeflg = jacksonUtil.getString("typeflg");
			String strSiteId = jacksonUtil.getString("siteId");
			String strAreaZone = "";

			switch (typeflg) {
			case "all":
				strAreaZone = jacksonUtil.getString("areaZone");
				if (null != strSiteId && !"".equals(strSiteId)) {
					String sql = "select TZ_AREA_ID,TZ_AREA_NAME from PS_TZ_SITEI_AREA_T where TZ_SITEI_ID=? and TZ_AREA_POSITION=? and TZ_AREA_STATE='Y' order by TZ_AREA_XH";
					List<Object> listData = sqlQuery.queryForList(sql, new Object[] { strSiteId, strAreaZone });

					ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();

					for (Object obj : listData) {

						Map<String, Object> mapData = (Map<String, Object>) obj;

						String strAreaId = mapData.get("TZ_AREA_ID") == null ? ""
								: String.valueOf(mapData.get("TZ_AREA_ID"));
						String strAreaName = mapData.get("TZ_AREA_NAME") == null ? ""
								: String.valueOf(mapData.get("TZ_AREA_NAME"));

						Map<String, Object> mapJson = new HashMap<String, Object>();
						mapJson.put("areaName", strAreaName);
						mapJson.put("areaId", strAreaId);
						mapJson.put("areaImg", "tranzvision/kitchensink/app/view/website/set/images/kitchensink.gif");
						mapJson.put("areaDesc", "");

						listJson.add(mapJson);

					}

					mapRet.put("topics", listJson);

				} else {
					errMsg[0] = "1";
					errMsg[1] = "请求添加区域异常！";
					mapRet.put("success", false);
				}

				break;

			case "add":

				String strAreaId = jacksonUtil.getString("areaId");

				String sql = "select TZ_AREA_CODE from PS_TZ_SITEI_AREA_T where TZ_SITEI_ID=? and TZ_AREA_ID=?";
				String strAreaCode = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strAreaId }, "String");

				if (null != strAreaCode && !"".equals(strAreaCode)) {

					strAreaCode = strAreaCode.replace((char) (10), ' ');
					strAreaCode = strAreaCode.replace((char) (13), ' ');
					strAreaCode = strAreaCode.replace("\\", "\\\\");
					strAreaCode = strAreaCode.replace("'", "\\'");
					strAreaCode = strAreaCode.replace("\"", "\\\"");

					String ctxPath = request.getContextPath();
					strAreaCode = strAreaCode.replace("{ContextPath}", ctxPath);

					sql = "select TZ_SKIN_ID from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
					String skinId = sqlQuery.queryForObject(sql, new Object[] { strSiteId }, "String");
					String skinImgPath = ctxPath + getSysHardCodeVal.getWebsiteSkinsImgPath() + "/" + skinId;
					strAreaCode = strAreaCode.replace("{ContextSkinPath}", skinImgPath);

					return "\"" + strAreaCode + "\"";

				} else {
					errMsg[0] = "1";
					errMsg[1] = "请求添加区域异常！";
					mapRet.put("success", false);
				}

				break;

			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "请求添加区域异常！";
			mapRet.put("success", false);
		}

		return jacksonUtil.Map2json(mapRet);
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

				// String typeFlag = jacksonUtil.getString("typeFlag");
				Map<String, Object> formData = jacksonUtil.getMap("data");

				String strAreaId = formData.get("areaId") == null ? "" : String.valueOf(formData.get("areaId"));
				// String strAreaZone = formData.get("areaZone") == null ? "" :
				// String.valueOf(formData.get("areaZone"));
				String strAreaType = formData.get("areaType") == null ? "" : String.valueOf(formData.get("areaType"));

				String strAreaTypeId = "";

				if ("".equals(strAreaId)) {
					String sql = "select TZ_AREA_TYPE_ID from PS_TZ_SITEI_AREA_T where TZ_AREA_TYPE=?";
					strAreaTypeId = sqlQuery.queryForObject(sql, new Object[] { strAreaType }, "String");
				} else {
					String sql = "select TZ_AREA_TYPE_ID from PS_TZ_SITEI_AREA_T where TZ_AREA_ID=?";
					strAreaTypeId = sqlQuery.queryForObject(sql, new Object[] { strAreaId }, "String");
				}

				if (null != strAreaTypeId && !"".equals(strAreaTypeId)) {

					String[] updFromData = new String[] { jacksonUtil.Map2json(formData) };

					strRet = tzPhDecoratedServiceImpl.tzUpdate(updFromData, errMsg);

				} else {
					errMsg[0] = "1";
					errMsg[1] = "请求编辑的区域不存在！";
					mapRet.put("success", false);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			mapRet.put("success", false);
		}

		if ("".equals(strRet)) {
			strRet = jacksonUtil.Map2json(mapRet);
		}

		return strRet;

	}

}
