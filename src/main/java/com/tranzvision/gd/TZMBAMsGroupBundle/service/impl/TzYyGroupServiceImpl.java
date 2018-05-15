package com.tranzvision.gd.TZMBAMsGroupBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

@Service("com.tranzvision.gd.TZMBAMsGroupBundle.service.impl.TzYyGroupServiceImpl")
public class TzYyGroupServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery jdbcTemplate;

	@Autowired
	private TZGDObject tzGDObject;

	@Autowired
	private GetSeqNum getSeqNum;

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

				cerateGroup(classID, batchID);
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "参数不正确！";
		}

		strRet = jacksonUtil.Map2json(mapRet);
		return strRet;
	}

	/**
	 * 生成默认的2个分组
	 * 
	 * @param classId
	 * @param batchId
	 */
	public void cerateGroup(String classId, String batchId) {
		String sql = "SELECT TZ_CLPS_GR_ID FROM PS_TZ_PWZ_VW WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
		List<Map<String, Object>> appInsList = jdbcTemplate.queryForList(sql, new Object[] { classId, batchId });
		String TZ_CLPS_GR_ID = "";
		int count = 0;
		int id = 0;
		for (Map<String, Object> appInsMap : appInsList) {
			TZ_CLPS_GR_ID = appInsMap.get("TZ_CLPS_GR_ID").toString();
			count = jdbcTemplate.queryForObject(
					"select count(1) from PS_TZ_INTEGROUP_T where TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? and TZ_CLPS_GR_ID=?",
					new Object[] { classId, batchId, TZ_CLPS_GR_ID }, "Integer");
			// 一条都没有默认添加20个分组
			if (count <= 0) {
				sql = "INSERT INTO PS_TZ_INTEGROUP_T(TZ_GROUP_ID,TZ_GROUP_NAME,TZ_CLPS_GR_ID,TZ_CLASS_ID,TZ_APPLY_PC_ID) VALUES(?,?,?,?,?)";
				for (int i = 1; i <= 20; i++) {
					id = getSeqNum.getSeqNum("TZ_INTEGROUP_T", "TZ_GROUP_ID");
					jdbcTemplate.update(sql, new Object[] { id, i + "组", TZ_CLPS_GR_ID, classId, batchId });
				}
			}
		}
	}

}
