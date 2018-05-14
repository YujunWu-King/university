package com.tranzvision.gd.TZMBAMsGroupBundle.service.impl;

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

@Service("com.tranzvision.gd.TZMBAMsGroupBundle.service.impl.TzYyGroupServiceImpl")
public class TzYyGroupServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery jdbcTemplate;

	@Autowired
	private TZGDObject tzGDObject;

	/**
	 * 获取预约面试列表
	 */
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", new ArrayList<Map<String, Object>>());

		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			String classID = jacksonUtil.getString("classID");
			String batchID = jacksonUtil.getString("batchID");

			if (null != classID && !"".equals(classID) && null != batchID && !"".equals(batchID)) {
				// 查询面试安排总数
				String sql = "SELECT COUNT(*) FROM PS_TZ_MSSJ_ARR_TBL WHERE TZ_CLASS_ID=? AND TZ_BATCH_ID=? AND TZ_MS_PUB_STA=? AND TZ_MS_OPEN_STA=?";
				int total = jdbcTemplate.queryForObject(sql, new Object[] { classID, batchID, "Y", "Y" }, "int");

				ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();
				if (total > 0) {
					sql = tzGDObject.getSQLText("SQL.TZMbaPwClps.TzGdMsPlanAppoPersonCount2");
					List<Map<String, Object>> listData = jdbcTemplate.queryForList(sql,
							new Object[] { classID, batchID, "Y", "Y", numStart, numLimit });

					for (Map<String, Object> mapData : listData) {
						Map<String, Object> mapJson = new HashMap<String, Object>();

						mapJson.put("classID", classID);
						mapJson.put("batchID", batchID);
						mapJson.put("msJxNo", mapData.get("TZ_MS_PLAN_SEQ"));
						mapJson.put("maxPerson", mapData.get("TZ_MSYY_COUNT"));
						mapJson.put("msDate", mapData.get("TZ_MS_DATE"));
						mapJson.put("bjMsStartTime", mapData.get("TZ_START_TM"));
						mapJson.put("bjMsEndTime", mapData.get("TZ_END_TM"));
						mapJson.put("msLocation", mapData.get("TZ_MS_LOCATION"));
						mapJson.put("msXxBz", mapData.get("TZ_MS_ARR_DEMO"));
						mapJson.put("appoPerson", mapData.get("TZ_YY_COUNT"));

						listJson.add(mapJson);
					}
					mapRet.replace("total", total);
					mapRet.replace("root", listJson);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "参数不正确！";
		}

		strRet = jacksonUtil.Map2json(mapRet);
		return strRet;
	}

}
