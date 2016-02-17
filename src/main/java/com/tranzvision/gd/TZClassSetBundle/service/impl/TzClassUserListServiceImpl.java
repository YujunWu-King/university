/**
 * 
 */
package com.tranzvision.gd.TZClassSetBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 人员选择列表，原PS：TZ_GD_BJGL_CLS:TZ_GD_USER_CLS
 * 
 * @author SHIHUA
 * @since 2016-02-03
 */
@Service("com.tranzvision.gd.TZClassSetBundle.service.impl.TzClassUserListServiceImpl")
public class TzClassUserListServiceImpl extends FrameworkImpl {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzGDObject;

	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {

		// 返回值;
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			jacksonUtil.json2Map(strParams);

			String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);

			String sql = tzGDObject.getSQLText("SQL.TZClassSetBundle.TzGetUserListCount");
			int total = sqlQuery.queryForObject(sql, new Object[] { orgid }, "int");

			sql = tzGDObject.getSQLText("SQL.TZClassSetBundle.TzGetUserList");

			List<Map<String, Object>> listData;

			if (numStart > 0) {
				sql = sql + " limit ?,?";
				listData = sqlQuery.queryForList(sql, new Object[] { orgid, numStart + numLimit, numLimit });
			} else {
				listData = sqlQuery.queryForList(sql, new Object[] { orgid });
			}

			ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();

			for (Map<String, Object> mapData : listData) {

				Map<String, Object> mapJson = new HashMap<String, Object>();

				mapJson.put("userOprid", mapData.get("OPRID") == null ? "" : String.valueOf(mapData.get("OPRID")));
				mapJson.put("userName",
						mapData.get("TZ_REALNAME") == null ? "" : String.valueOf(mapData.get("TZ_REALNAME")));
				mapJson.put("userPhone",
						mapData.get("TZ_MOBILE") == null ? "" : String.valueOf(mapData.get("TZ_MOBILE")));
				mapJson.put("userEmail",
						mapData.get("TZ_EMAIL") == null ? "" : String.valueOf(mapData.get("TZ_EMAIL")));
				mapJson.put("accountNum", "");
				mapJson.put("orgId", orgid);

				listJson.add(mapJson);

			}

			Map<String, Object> mapRet = new HashMap<String, Object>();
			mapRet.put("total", total);
			mapRet.put("root", listJson);

			strRet = jacksonUtil.Map2json(mapRet);

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "查询失败！" + e.getMessage();
			Map<String, Object> mapRet = new HashMap<String, Object>();
			mapRet.put("total", 0);
			mapRet.put("root", new ArrayList<Map<String, Object>>());
			strRet = jacksonUtil.Map2json(mapRet);
		}

		return strRet;

	}

}
