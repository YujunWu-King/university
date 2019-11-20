package com.tranzvision.gd.TZMBAMsGroupBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 面试分组
 * 
 * @author caoy
 *
 */
@Service("com.tranzvision.gd.TZMBAMsGroupBundle.service.impl.TzMsGroupServiceImpl")
public class TzMsGroupServiceImpl extends FrameworkImpl {

	@Autowired
	private FliterForm fliterForm;

	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzSQLObject;

	/***
	 * 
	 * @param comParams
	 * @param numLimit
	 * @param numStart
	 * @param errorMsg
	 * @return
	 */

	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();


		// String pwsql = "SELECT group_concat(B.TZ_DLZH_ID) AS TZ_DLZH_ID FROM
		// PS_TZ_MP_PW_KS_TBL A, PS_TZ_AQ_YHXX_TBL B WHERE
		// A.TZ_PWEI_OPRID=B.OPRID AND A.TZ_CLASS_ID=? AND A.TZ_APPLY_PC_ID=?
		// AND A.TZ_APP_INS_ID=? GROUP BY
		// A.TZ_APPLY_PC_ID,A.TZ_CLASS_ID,A.TZ_APP_INS_ID";
		try {

			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] { { "TZ_APP_INS_ID", "ASC" } };

			String sql = tzSQLObject.getSQLText("SQL.TZMbaPwClps.TZ_GROUP");

			// json数据要的结果字段;
			String[] resultFldArray = { "OPRID", "TZ_APP_INS_ID", "TZ_CLASS_ID", "TZ_APPLY_PC_ID",
					"TZ_REALNAME", "TZ_GENDER", "TZ_MSH_ID", "TZ_MS_PLAN_SEQ"};

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, comParams, numLimit, numStart, errorMsg);
			Map<String, Object> map = null;
			String TZ_GROUP_ID = "";
			String TZ_GROUP_DATE = "";
			String TZ_ORDER = "";
			String TZ_GROUP_NAME = "";
			String TZ_CLPS_GR_ID="";
			String judgeGroupName = "";
			if (obj != null && obj.length > 0) {
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("classId", rowList[2]);
					mapList.put("batchId", rowList[3]);
					mapList.put("appInsId", rowList[1]);
					mapList.put("ksOprId", rowList[0]);
					mapList.put("ksName", rowList[4]);
					mapList.put("gender", rowList[5]);
					mapList.put("mshId", rowList[6]);
					mapList.put("msJxNo", rowList[7]);

					map = sqlQuery.queryForMap(sql, new Object[] { rowList[2], rowList[3], rowList[1] });

					if (map != null) {
						TZ_GROUP_ID = map.get("TZ_GROUP_ID") == null ? "" : map.get("TZ_GROUP_ID").toString();
						TZ_GROUP_DATE = map.get("TZ_GROUP_DATE") == null ? "" : map.get("TZ_GROUP_DATE").toString();
						TZ_ORDER = map.get("TZ_ORDER") == null ? "" : map.get("TZ_ORDER").toString();
						TZ_GROUP_NAME = map.get("TZ_GROUP_NAME") == null ? "" : map.get("TZ_GROUP_NAME").toString();
						judgeGroupName = map.get("TZ_CLPS_GR_NAME") == null ? ""
								: map.get("TZ_CLPS_GR_NAME").toString();
						TZ_CLPS_GR_ID = map.get("TZ_CLPS_GR_ID") == null ? ""
								: map.get("TZ_CLPS_GR_ID").toString();
						if (TZ_GROUP_DATE != null) {
						} else {
							TZ_GROUP_DATE = "暂无时间安排";
						}
						mapList.put("group_date", TZ_GROUP_DATE);
						if (!TZ_GROUP_NAME.equals("") && !TZ_ORDER.equals("")) {
							mapList.put("order", TZ_GROUP_NAME + "-" + TZ_ORDER);
							mapList.put("order", TZ_GROUP_NAME + "-" + TZ_ORDER);
						} else {
							mapList.put("order", "");
						}
						mapList.put("group_name", TZ_GROUP_NAME);
						mapList.put("group_id", TZ_GROUP_ID);
						mapList.put("pwgroup_name", judgeGroupName);
						mapList.put("pwgroup_id", TZ_CLPS_GR_ID);
					} else {
						mapList.put("group_date", "");
						mapList.put("order", "");
						mapList.put("group_name", "");
						mapList.put("pwgroup_name", "");
						mapList.put("group_id", "");
						mapList.put("pwgroup_id", "");
					}

					listData.add(mapList);
				}

				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);

			}
		} catch (Exception e) {
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
			e.printStackTrace();
		}
		return jacksonUtil.Map2json(mapRet);

	}

	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		HashMap<String, Object> mapRet = new HashMap<String, Object>();
		HashMap<String, Object> mapData = new HashMap<String, Object>();

		try {

			String dtFormat = getSysHardCodeVal.getDateFormat();
			String tmFormat = getSysHardCodeVal.getTimeHMFormat();

			SimpleDateFormat dateSimpleDateFormat = new SimpleDateFormat(dtFormat);
			SimpleDateFormat timeSimpleDateFormat = new SimpleDateFormat(tmFormat);

			jacksonUtil.json2Map(strParams);

			// 班级编号
			String classId = jacksonUtil.getString("classId");
			// 批次编号
			String batchId = jacksonUtil.getString("batchId");

			String sql = tzSQLObject.getSQLText("SQL.TZMbaPwClps.TZ_MSPS_SET_RELUER");
			Map<String, Object> mapBasic = sqlQuery.queryForMap(sql, new Object[] { classId, batchId });

			if (mapBasic != null) {
				String className = (String) mapBasic.get("TZ_CLASS_NAME");
				String batchName = (String) mapBasic.get("TZ_BATCH_NAME");
				Date startDate = mapBasic.get("TZ_PYKS_RQ") == null ? null
						: dateSimpleDateFormat.parse(String.valueOf(mapBasic.get("TZ_PYKS_RQ")));
				Date startTime = mapBasic.get("TZ_PYKS_SJ") == null ? null
						: timeSimpleDateFormat.parse(String.valueOf(mapBasic.get("TZ_PYKS_SJ")));
				Date endDate = mapBasic.get("TZ_PYJS_RQ") == null ? null
						: dateSimpleDateFormat.parse(String.valueOf(mapBasic.get("TZ_PYJS_RQ")));
				Date endTime = mapBasic.get("TZ_PYJS_SJ") == null ? null
						: timeSimpleDateFormat.parse(String.valueOf(mapBasic.get("TZ_PYJS_SJ")));
				String materialDesc = (String) mapBasic.get("TZ_MSPS_SM");
				String dqpsStatus = (String) mapBasic.get("TZ_DQPY_ZT");
				String dqpsStatusDesc = (String) mapBasic.get("TZ_DQPY_ZT_DESC");
				String bkksNum = mapBasic.get("TZ_BKKS_NUM") == null ? "" : String.valueOf(mapBasic.get("TZ_BKKS_NUM"));
				String mspsksNum = mapBasic.get("TZ_MSPS_KS_NUM") == null ? ""
						: String.valueOf(mapBasic.get("TZ_MSPS_KS_NUM"));

				String JGID = mapBasic.get("TZ_JG_ID") == null ? "" : String.valueOf(mapBasic.get("TZ_JG_ID"));

				String strStartDate = "";
				if (null != startDate) {
					strStartDate = dateSimpleDateFormat.format(startDate);
				}
				String strStartTime = "";
				if (null != startTime) {
					strStartTime = timeSimpleDateFormat.format(startTime);
				}
				String strEndDate = "";
				if (null != endDate) {
					strEndDate = dateSimpleDateFormat.format(endDate);
				}
				String strEndTime = "";
				if (null != endTime) {
					strEndTime = timeSimpleDateFormat.format(endTime);
				}

				mapData.put("classId", classId);
				mapData.put("batchId", batchId);
				mapData.put("className", className);
				mapData.put("batchName", batchName);
				mapData.put("ksNum", bkksNum);
				mapData.put("reviewKsNum", mspsksNum);
				mapData.put("dqpsStatus", dqpsStatus);
				mapData.put("desc", dqpsStatusDesc);
				mapData.put("StartDate", strStartDate);
				mapData.put("StartTime", strStartTime);
				mapData.put("EndDate", strEndDate);
				mapData.put("EndTime", strEndTime);
				mapData.put("desc", materialDesc);
				mapData.put("jgid", JGID);

				mapRet.put("formData", mapData);
			}else {
				String sql2 ="SELECT A.TZ_CLASS_NAME,B.TZ_BATCH_NAME FROM PS_TZ_CLASS_INF_T A LEFT JOIN PS_TZ_CLS_BATCH_T B ON A.TZ_CLASS_ID=B.TZ_CLASS_ID where A.TZ_CLASS_ID=? and B.TZ_BATCH_ID=? LIMIT 1 ;";
				Map<String , Object> strName = sqlQuery.queryForMap(sql2, new Object[] {classId,batchId});
				String className = (String)strName.get("TZ_CLASS_NAME");
				String batchName = (String)strName.get("TZ_BATCH_NAME");
				mapData.put("classId", classId);
				mapData.put("batchId", batchId);
				mapData.put("className", className);
				mapData.put("batchName", batchName);
				mapRet.put("formData", mapData);
			}

			strRet = jacksonUtil.Map2json(mapRet);

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRet;
	}
}
