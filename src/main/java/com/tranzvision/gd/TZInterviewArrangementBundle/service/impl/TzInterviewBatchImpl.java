package com.tranzvision.gd.TZInterviewArrangementBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZInterviewArrangementBundle.service.impl.TzInterviewBatchImpl")
public class TzInterviewBatchImpl extends FrameworkImpl{
	
	@Autowired
	private SqlQuery jdbcTemplate;
	
	/**
	 * 班级批次列表
	 */
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {

		String strRet = "";

		JacksonUtil jacksonUtil = new JacksonUtil();

		try {

			jacksonUtil.json2Map(strParams);
			String classID = jacksonUtil.getString("classID");
			
//			int start = Integer.parseInt(request.getParameter("start"));
//			int limit = Integer.parseInt(request.getParameter("limit"));
			
			
			if (null != classID && !"".equals(classID)) {
//				if (limit > 0) {
					String sql = "SELECT COUNT(*) FROM PS_TZ_CLS_BATCH_T WHERE TZ_CLASS_ID=?";
					int total = jdbcTemplate.queryForObject(sql, new Object[] { classID }, "int");
					
					ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();

					if (total > 0) {

						sql = "SELECT TZ_BATCH_ID,TZ_BATCH_NAME,date_format(TZ_START_DT,'%Y-%m-%d') as TZ_START_DT,date_format(TZ_END_DT,'%Y-%m-%d') as TZ_END_DT FROM PS_TZ_CLS_BATCH_T A WHERE TZ_CLASS_ID=? ORDER BY TZ_START_DT ASC";

						List<Map<String, Object>> listData = jdbcTemplate.queryForList(sql, new Object[] { classID });

						for (Map<String, Object> mapData : listData) {
							Map<String, Object> mapJson = new HashMap<String, Object>();

							mapJson.put("classID", classID);

							mapJson.put("batchID", mapData.get("TZ_BATCH_ID") == null ? "0" : String.valueOf(mapData.get("TZ_BATCH_ID")));

							mapJson.put("batchName", mapData.get("TZ_BATCH_NAME") == null ? "" : String.valueOf(mapData.get("TZ_BATCH_NAME")));

							mapJson.put("startDate", mapData.get("TZ_START_DT") == null ? "" : String.valueOf(mapData.get("TZ_START_DT")));
							
							mapJson.put("endDate", mapData.get("TZ_END_DT") == null ? "" : String.valueOf(mapData.get("TZ_END_DT")));

							listJson.add(mapJson);
						}
						
						Map<String, Object> mapRet = new HashMap<String, Object>();
						mapRet.put("total", total);
						mapRet.put("root", listJson);

						strRet = jacksonUtil.Map2json(mapRet);
					}
//				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "参数不正确！";
		}

		return strRet;
	}
}
