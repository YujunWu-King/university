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
				String sql = "SELECT DISTINCT TZ_MS_PLAN_SEQ FROM PS_TZ_MSYY_KS_TBL WHERE OPRID IN ( SELECT A.OPRID FROM PS_TZ_FORM_WRK_T A, PS_TZ_MSPS_KSH_TBL B WHERE B.TZ_CLASS_ID = ? AND B.TZ_APPLY_PC_ID = ? AND B.TZ_APP_INS_ID = A.TZ_APP_INS_ID ) AND TZ_MS_PLAN_SEQ IN ( SELECT TZ_MS_PLAN_SEQ FROM PS_TZ_MSSJ_ARR_TBL WHERE TZ_MS_PUB_STA = ? AND TZ_MS_OPEN_STA = ? )";

				List<Map<String, Object>> listData = jdbcTemplate.queryForList(sql,
						new Object[] { classID, batchID, "Y", "Y" });

				int total = listData.size();
				ArrayList<String> l = new ArrayList<String>();
				Map<String, Object> mapJson = null;

				String TZ_MS_PLAN_SEQ = "";
				String TZ_MS_PLAN_SEQs = "";
				for (Map<String, Object> mapData : listData) {
					mapJson = new HashMap<String, Object>();
					TZ_MS_PLAN_SEQ = mapData.get("TZ_MS_PLAN_SEQ").toString();
					if (TZ_MS_PLAN_SEQs.equals("")) {
						TZ_MS_PLAN_SEQs = "'" + TZ_MS_PLAN_SEQ + "'";
					} else {
						TZ_MS_PLAN_SEQs = TZ_MS_PLAN_SEQs + "," + "'" + TZ_MS_PLAN_SEQ + "'";
					}
				}
				System.out.println("TZ_MS_PLAN_SEQs:" + TZ_MS_PLAN_SEQs);
				sql = "SELECT TZ_MS_PLAN_SEQ, TZ_MSYY_COUNT, TZ_MS_DATE , DATE_FORMAT(TZ_START_TM, '%H:%i') AS TZ_START_TM , DATE_FORMAT(TZ_END_TM, '%H:%i') AS TZ_END_TM, TZ_MS_LOCATION , TZ_MS_ARR_DEMO, B.TZ_BATCH_NAME , ( SELECT COUNT(1) FROM PS_TZ_MSYY_KS_TBL WHERE TZ_MS_PLAN_SEQ = A.TZ_MS_PLAN_SEQ ) AS TZ_YY_COUNT FROM PS_TZ_MSSJ_ARR_TBL A, PS_TZ_CLS_BATCH_T B WHERE A.TZ_MS_PLAN_SEQ IN ("
						+ TZ_MS_PLAN_SEQs
						+ ") AND A.TZ_CLASS_ID = B.TZ_CLASS_ID AND A.TZ_BATCH_ID = B.TZ_BATCH_ID ORDER BY TZ_MS_DATE ASC LIMIT ?, ?";
				ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();
				if (total > 0) {
					listData = jdbcTemplate.queryForList(sql, new Object[] { numStart, numLimit });

					for (Map<String, Object> mapData : listData) {
						mapJson = new HashMap<String, Object>();

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
						mapJson.put("batchName", mapData.get("TZ_BATCH_NAME"));
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
