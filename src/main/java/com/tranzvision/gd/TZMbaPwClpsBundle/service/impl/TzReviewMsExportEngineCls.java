package com.tranzvision.gd.TZMbaPwClpsBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tranzvision.gd.batch.engine.base.BaseEngine;
import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.poi.excel.ExcelHandle2;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

public class TzReviewMsExportEngineCls extends BaseEngine {

	// 本/专科院校
	private static String schoolNameXxxId = "";
	// 最高学历
	private static String highestRecordXxxId = "";
	// 最高学历下拉框定义id
	private static String highestRecordXlkId = "";
	// 工作所在地
	private static String companyAddressXxxId = "";
	// 工作单位
	private static String companyNameXxxId = "";
	// 所在部门
	private static String departmentXxxId = "";
	// 工作职位
	private static String positionXxxId = "";
	// 自主创业全称
	private static String selfEmploymentXxxId = "";

	@SuppressWarnings("unchecked")
	@Override
	public void OnExecute() throws Exception {
		JacksonUtil jacksonUtil = new JacksonUtil();

		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		// jdbctmplate;
		SqlQuery sqlQuery = (SqlQuery) getSpringBeanUtil.getAutowiredSpringBean("SqlQuery");

		TZGDObject tzGDObject = (TZGDObject) getSpringBeanUtil.getAutowiredSpringBean("tzGDObject");

		// 运行id;
		String runControlId = this.getRunControlID();
		int processinstance = this.getProcessInstanceID();

		try {
			// 导出参数
			Map<String, Object> paramsMap = sqlQuery.queryForMap(
					"select TZ_CLASS_ID,TZ_BATCH_ID,TZ_REL_URL,TZ_JD_URL,TZ_PARAMS_STR from PS_TZ_ZDCS_DC_AET where RUN_CNTL_ID=?",
					new Object[] { runControlId });

			if (paramsMap != null) {
				String classId = paramsMap.get("TZ_CLASS_ID").toString();
				String batchId = paramsMap.get("TZ_BATCH_ID").toString();
				String expDirPath = paramsMap.get("TZ_REL_URL").toString();
				String absexpDirPath = paramsMap.get("TZ_JD_URL").toString();
				String strParams = paramsMap.get("TZ_PARAMS_STR").toString();

				ExcelHandle2 excelHandle = new ExcelHandle2(expDirPath, absexpDirPath);
				List<String[]> dataCellKeys = new ArrayList<String[]>();

				jacksonUtil.json2Map(strParams);

				/******************************
				 * 1、生成标题行---开始
				 **************************************/
				dataCellKeys.add(new String[] { "className", "报考方向" });
				dataCellKeys.add(new String[] { "nationId", "证件号" });
				dataCellKeys.add(new String[] { "mssqh", "面试申请号" });
				dataCellKeys.add(new String[] { "name", "姓名" });
				dataCellKeys.add(new String[] { "judgeDlzhId", "评委账号" });
				dataCellKeys.add(new String[] { "judggroupName", "评委所属组" });
				dataCellKeys.add(new String[] { "judgeNum", "评委评审人数" });
				dataCellKeys.add(new String[] { "rank", "考生评审排名" });

				// 成绩项
				List<String[]> listScoreItemField = new ArrayList<String[]>();
				/// String sql =
				/// tZGDObject.getSQLText("SQL.TZMaterialInterviewReviewBundle.material.TzGetMaterialScoreItemInfo");
				String sql = "SELECT C.TZ_SCORE_ITEM_ID,C.DESCR,C.TZ_SCORE_ITEM_TYPE FROM PSTREENODE D,PS_TZ_CLASS_INF_T A,PS_TZ_MODAL_DT_TBL C,PS_TZ_RS_MODAL_TBL B WHERE D.TREE_NAME=B.TREE_NAME AND D.TREE_NODE=C.TZ_SCORE_ITEM_ID AND B.TZ_JG_ID=C.TZ_JG_ID AND B.TREE_NAME=C.TREE_NAME AND A.TZ_MSCJ_SCOR_MD_ID=B.TZ_SCORE_MODAL_ID AND B.TZ_JG_ID=A.TZ_JG_ID AND A.TZ_CLASS_ID=? ORDER BY D.TREE_NODE_NUM";
				List<Map<String, Object>> listScoreItem = sqlQuery.queryForList(sql, new Object[] { classId });

				for (Map<String, Object> mapScoreItem : listScoreItem) {
					String scoreItemId = mapScoreItem.get("TZ_SCORE_ITEM_ID") == null ? ""
							: mapScoreItem.get("TZ_SCORE_ITEM_ID").toString();
					String scoreItemName = mapScoreItem.get("DESCR") == null ? ""
							: mapScoreItem.get("DESCR").toString();
					String scoreItemType = mapScoreItem.get("TZ_SCORE_ITEM_TYPE") == null ? ""
							: mapScoreItem.get("TZ_SCORE_ITEM_TYPE").toString();
					dataCellKeys.add(new String[] { scoreItemId, scoreItemName });
					listScoreItemField.add(new String[] { scoreItemId, scoreItemType });
				}

				dataCellKeys.add(new String[] { "birthday", "出生日期" });
				dataCellKeys.add(new String[] { "age", "年龄" });
				dataCellKeys.add(new String[] { "sex", "性别" });
				// dataCellKeys.add(new String[] { "examineeTag", "考生标签" });
				dataCellKeys.add(new String[] { "schoolName", "本/专科院校" });
				dataCellKeys.add(new String[] { "highestRecord", "最高学历" });
				dataCellKeys.add(new String[] { "companyAddress", "工作所在地" });
				dataCellKeys.add(new String[] { "companyName", "工作单位" });
				dataCellKeys.add(new String[] { "gzage", "工龄" });
				dataCellKeys.add(new String[] { "department", "所在部门" });
				dataCellKeys.add(new String[] { "position", "工作职位" });
				dataCellKeys.add(new String[] { "selfEmployment ", "自主创业全称" });
				dataCellKeys.add(new String[] { "clpsyszf ", "材料评审总分" });

				/******************************
				 * 1、生成标题行---结束
				 **************************************/

				String hardcodeSql = "select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT where TZ_HARDCODE_PNT=?";
				schoolNameXxxId = sqlQuery.queryForObject(hardcodeSql, new Object[] { "TZ_BMB_BZKYX_XXX_ID" },
						"String");
				highestRecordXxxId = sqlQuery.queryForObject(hardcodeSql, new Object[] { "TZ_BMB_ZGXL_XXX_ID" },
						"String");
				highestRecordXlkId = sqlQuery.queryForObject(hardcodeSql, new Object[] { "TZ_BMB_ZGXL_XLK_ID" },
						"String");
				companyAddressXxxId = sqlQuery.queryForObject(hardcodeSql, new Object[] { "TZ_BMB_GZSZD_XXX_ID" },
						"String");
				companyNameXxxId = sqlQuery.queryForObject(hardcodeSql, new Object[] { "TZ_BMB_GZDW_XXX_ID" },
						"String");
				departmentXxxId = sqlQuery.queryForObject(hardcodeSql, new Object[] { "TZ_BMB_SZBM_XXX_ID" }, "String");
				positionXxxId = sqlQuery.queryForObject(hardcodeSql, new Object[] { "TZ_BMB_GZZW_XXX_ID" }, "String");
				selfEmploymentXxxId = sqlQuery.queryForObject(hardcodeSql, new Object[] { "TZ_BMB_ZZCYQC_XXX_ID" },
						"String");

				/******************************
				 * 2、考生信息数据---开始
				 **************************************/
				// 数据
				List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();

				// 导出选中考生自动初筛结果
				if (jacksonUtil.containsKey("appInsIds")) {
					List<String> listAppInsIdExport = (List<String>) jacksonUtil.getList("appInsIds");

					if (listAppInsIdExport != null && listAppInsIdExport.size() > 0) {
						for (String appInsId : listAppInsIdExport) {

							this.tzExportData(tzGDObject, sqlQuery, classId, batchId, appInsId, listScoreItemField,
									dataList);
						}
					}
				}

				// 导出搜索结果考生自动初筛结果
				if (jacksonUtil.containsKey("searchSql")) {
					String searchSql = jacksonUtil.getString("searchSql");
					List<Map<String, Object>> appInsList = sqlQuery.queryForList(searchSql);

					for (Map<String, Object> appInsMap : appInsList) {
						String appInsId = appInsMap.get("TZ_APP_INS_ID") == null ? ""
								: appInsMap.get("TZ_APP_INS_ID").toString();

						this.tzExportData(tzGDObject, sqlQuery, classId, batchId, appInsId, listScoreItemField,
								dataList);
					}
				}

				sql = "select TZ_SYSFILE_NAME from PS_TZ_EXCEL_DATT_T where PROCESSINSTANCE=?";
				String strUseFileName = sqlQuery.queryForObject(sql, new Object[] { processinstance }, "String");

				boolean rst = excelHandle.export2Excel(strUseFileName, dataCellKeys, dataList);
				if (rst) {
					String urlExcel = excelHandle.getExportExcelPath();
					try {
						sqlQuery.update("update  PS_TZ_EXCEL_DATT_T set TZ_FWQ_FWLJ = ? where PROCESSINSTANCE=?",
								new Object[] { urlExcel, processinstance });
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
				/******************************
				 * 2、考生信息数据---结束
				 **************************************/
			} else {
				this.logError("获取批处理参数失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.logError("系统错误，" + e.getMessage());
		}
	}

	/**
	 * 
	 * @param classId
	 * @param batchId
	 * @param appInsId
	 * @param scoreItemList
	 * @return
	 */
	private void tzExportData(TZGDObject tzGDObject, SqlQuery sqlQuery, String classId, String batchId, String appinsId,
			List<String[]> listScoreItemField, List<Map<String, Object>> dataList) {

		String className = "", appModalId = "", nationId = "", mssqh = "", name = "", judgeDlzhId = "", judgeNum = "",
				rank = "", birthday = "", age = "", sex = "", examineeTag = "", schoolName = "", highestRecordId = "",
				highestRecord = "", companyAddress = "", companyName = "", department = "", position = "",
				selfEmployment = "", clpszf = "", jugeId = "", judggroupName = "";
		String scoreInsId = "";

		// 考生数据
		String sql = "SELECT A.TZ_CLASS_NAME,A.TZ_APP_MODAL_ID,C.NATIONAL_ID,D.TZ_MSH_ID,C.TZ_REALNAME,DATE_FORMAT(C.BIRTHDATE,\"%Y-%m-%d\") BIRTHDATE,(year(now())-year(C.BIRTHDATE)) AGE,(SELECT X.TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL X WHERE X.TZ_ZHZ_ID=C.TZ_GENDER AND X.TZ_ZHZJH_ID='TZ_GENDER') TZ_GENDER_DESC FROM PS_TZ_AQ_YHXX_TBL D,PS_TZ_REG_USER_T C,PS_TZ_FORM_WRK_T B,PS_TZ_CLASS_INF_T A WHERE B.OPRID=D.OPRID AND B.OPRID=C.OPRID AND D.TZ_JG_ID=A.TZ_JG_ID AND A.TZ_CLASS_ID=? AND B.TZ_APP_INS_ID=?";
		Map<String, Object> mapExaminee = sqlQuery.queryForMap(sql, new Object[] { classId, appinsId });
		if (mapExaminee != null) {
			className = mapExaminee.get("TZ_CLASS_NAME") == null ? "" : mapExaminee.get("TZ_CLASS_NAME").toString();
			appModalId = mapExaminee.get("TZ_APP_MODAL_ID") == null ? ""
					: mapExaminee.get("TZ_APP_MODAL_ID").toString();
			nationId = mapExaminee.get("NATIONAL_ID") == null ? "" : mapExaminee.get("NATIONAL_ID").toString();
			mssqh = mapExaminee.get("TZ_MSH_ID") == null ? "" : mapExaminee.get("TZ_MSH_ID").toString();
			name = mapExaminee.get("TZ_REALNAME") == null ? "" : mapExaminee.get("TZ_REALNAME").toString();
			birthday = mapExaminee.get("BIRTHDATE") == null ? "" : mapExaminee.get("BIRTHDATE").toString();
			age = mapExaminee.get("AGE") == null ? "" : mapExaminee.get("AGE").toString();
			sex = mapExaminee.get("TZ_GENDER_DESC") == null ? "" : mapExaminee.get("TZ_GENDER_DESC").toString();

			String appSql = "SELECT TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T WHERE TZ_APP_INS_ID=? AND TZ_XXX_BH=?";
			schoolName = sqlQuery.queryForObject(appSql, new Object[] { appinsId, schoolNameXxxId }, "String");
			highestRecordId = sqlQuery.queryForObject(appSql, new Object[] { appinsId, highestRecordXxxId }, "String");
			companyAddress = sqlQuery.queryForObject(appSql, new Object[] { appinsId, companyAddressXxxId }, "String");
			companyName = sqlQuery.queryForObject(appSql, new Object[] { appinsId, companyNameXxxId }, "String");
			department = sqlQuery.queryForObject(appSql, new Object[] { appinsId, departmentXxxId }, "String");
			position = sqlQuery.queryForObject(appSql, new Object[] { appinsId, positionXxxId }, "String");
			selfEmployment = sqlQuery.queryForObject(appSql, new Object[] { appinsId, selfEmploymentXxxId }, "String");

			// 最高学历描述
			String highestSql = "SELECT TZ_XXXKXZ_MS FROM PS_TZ_APPXXX_KXZ_T WHERE TZ_APP_TPL_ID=? AND TZ_XXX_BH=? AND TZ_XXXKXZ_MC=?";
			highestRecord = sqlQuery.queryForObject(highestSql,
					new Object[] { appModalId, highestRecordXlkId, highestRecordId }, "String");

			// 报考方向拼接批次
			String strBatchSQL = "SELECT TZ_BATCH_NAME FROM PS_TZ_CLS_BATCH_T WHERE TZ_CLASS_ID=? AND TZ_BATCH_ID=?";
			String strBatchName = sqlQuery.queryForObject(strBatchSQL, new Object[] { classId, batchId }, "String");
			if (strBatchName == null || "".equals(strBatchName)) {
				/* doNothing */
			} else {
				className = className + " " + strBatchName;
			}

			// 材料评审总分
			try {
				clpszf = sqlQuery.queryForObject(tzGDObject.getSQLText("SQL.TZBzScoreMathBundle.TzGetClpSumScore"),
						new Object[] { appinsId }, "String");
			} catch (TzSystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// 评委数据
			try {
				sql = tzGDObject.getSQLText("SQL.TZMbaPwClps.TZ_KSMSPS_JUGZHU");
			} catch (TzSystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			List<Map<String, Object>> listJudge = sqlQuery.queryForList(sql,
					new Object[] { batchId, appinsId, classId });

			for (Map<String, Object> mapJudge : listJudge) {
				jugeId = mapJudge.get("TZ_PWEI_OPRID") == null ? "" : mapJudge.get("TZ_PWEI_OPRID").toString();
				judgeDlzhId = mapJudge.get("TZ_DLZH_ID") == null ? "" : mapJudge.get("TZ_DLZH_ID").toString();
				scoreInsId = mapJudge.get("TZ_SCORE_INS_ID") == null ? "" : mapJudge.get("TZ_SCORE_INS_ID").toString();
				judggroupName = mapJudge.get("TZ_CLPS_GR_NAME") == null ? ""
						: mapJudge.get("TZ_CLPS_GR_NAME").toString();
				rank = mapJudge.get("TZ_KSH_PSPM") == null ? "" : mapJudge.get("TZ_KSH_PSPM").toString();
				// 评委审批人数
				if (!jugeId.equals("")) {
					sql = "SELECT COUNT(1) FROM PS_TZ_MP_PW_KS_TBL  WHERE TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? and TZ_PWEI_OPRID=?";
					judgeNum = sqlQuery.queryForObject(sql, new Object[] { classId, batchId, jugeId }, "String");

				}
				Map<String, Object> mapData = new HashMap<String, Object>();
				mapData.put("className", className);
				mapData.put("nationId", nationId);
				mapData.put("mssqh", mssqh);
				mapData.put("name", name);
				mapData.put("judgeDlzhId", judgeDlzhId);
				mapData.put("judggroupName", judggroupName);
				mapData.put("judgeNum", judgeNum);
				mapData.put("rank", rank);

				// 成绩项分数
				for (String[] scoreField : listScoreItemField) {
					String scoreItemId = scoreField[0];
					String scoreItemType = scoreField[1];

					if ("D".equals(scoreItemType)) {
						// 下拉框
						sql = "SELECT TZ_CJX_XLK_XXBH FROM PS_TZ_CJX_TBL WHERE TZ_SCORE_INS_ID=? AND TZ_SCORE_ITEM_ID=?";
					} else {
						if ("C".equals(scoreItemType)) {
							// 评语
							sql = "SELECT TZ_SCORE_PY_VALUE FROM PS_TZ_CJX_TBL WHERE TZ_SCORE_INS_ID=? AND TZ_SCORE_ITEM_ID=?";
						} else {
							sql = "SELECT TZ_SCORE_NUM FROM PS_TZ_CJX_TBL WHERE TZ_SCORE_INS_ID=? AND TZ_SCORE_ITEM_ID=?";
						}
					}

					String scoreValue = sqlQuery.queryForObject(sql, new Object[] { scoreInsId, scoreItemId },
							"String");
					if ("D".equals(scoreItemType)) {
						sql = "SELECT A.TZ_CJX_XLK_XXMC FROM PS_TZ_ZJCJXXZX_T A,PS_TZ_RS_MODAL_TBL B,PS_TZ_CLASS_INF_T C ";
						sql += " WHERE C.TZ_ZLPS_SCOR_MD_ID=B.TZ_SCORE_MODAL_ID AND A.TZ_JG_ID=B.TZ_JG_ID AND A.TREE_NAME=B.TREE_NAME";
						sql += " AND C.TZ_CLASS_ID=? AND A.TZ_JG_ID=C.TZ_JG_ID AND A.TZ_SCORE_ITEM_ID=? AND A.TZ_CJX_XLK_XXBH=?";
						scoreValue = sqlQuery.queryForObject(sql, new Object[] { classId, scoreItemId, scoreValue },
								"String");
					}

					mapData.put(scoreItemId, scoreValue);

				}

				mapData.put("birthday", birthday);
				mapData.put("age", age);
				mapData.put("sex", sex);

				// 考生标签：自动标签+手动标签
				sql = "SELECT C.TZ_ZDBQ_ID TZ_BQ_ID,C.TZ_BIAOQZ_NAME TZ_BQ_NAME FROM PS_TZ_CS_KSBQ_T C WHERE C.TZ_APP_INS_ID=? UNION SELECT A.TZ_LABEL_ID TZ_BQ_ID,B.TZ_LABEL_NAME TZ_BQ_NAME FROM PS_TZ_FORM_LABEL_T A,PS_TZ_LABEL_DFN_T B WHERE A.TZ_LABEL_ID=B.TZ_LABEL_ID AND A.TZ_APP_INS_ID=?";
				List<Map<String, Object>> listBq = sqlQuery.queryForList(sql, new Object[] { appinsId, appinsId });
				for (Map<String, Object> mapBq : listBq) {
					String bqName = mapBq.get("TZ_BQ_NAME") == null ? "" : mapBq.get("TZ_BQ_NAME").toString();
					if (!"".equals(examineeTag)) {
						examineeTag += "," + bqName;
					} else {
						examineeTag = bqName;
					}
				}
				// mapData.put("examineeTag", examineeTag);

				mapData.put("schoolName", schoolName);
				mapData.put("highestRecord", highestRecord);
				mapData.put("companyAddress", companyAddress);
				mapData.put("companyName", companyName);
				mapData.put("department", department);
				mapData.put("position", position);
				mapData.put("selfEmployment", selfEmployment);

				/*
				 * mapData.put("zpm", this.checksunRank(sunscorelist, 9999999 -
				 * Float.valueOf(sunrank) * 100000 + Float.valueOf(clpszf)));
				 */
				mapData.put("clpsyszf", clpszf);

				dataList.add(mapData);
			}
		}
	}
}
