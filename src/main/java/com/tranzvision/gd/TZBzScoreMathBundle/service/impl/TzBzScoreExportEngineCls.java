package com.tranzvision.gd.TZBzScoreMathBundle.service.impl;


import java.util.ArrayList;
import java.util.Arrays;
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

public class TzBzScoreExportEngineCls extends BaseEngine {

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
	// 工龄
	private static String WORK_ON_YEAR_XXX_ID = "";

	@SuppressWarnings("unchecked")
	@Override
	public void OnExecute() throws Exception {
		JacksonUtil jacksonUtil = new JacksonUtil();

		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		// jdbctmplate;
		SqlQuery jdbcTemplate = (SqlQuery) getSpringBeanUtil.getAutowiredSpringBean("SqlQuery");

		TZGDObject tzGDObject = (TZGDObject) getSpringBeanUtil.getAutowiredSpringBean("tzGDObject");

		// 运行id;
		String runControlId = this.getRunControlID();
		int processinstance = this.getProcessInstanceID();

		try {
			// 导出参数
			Map<String, Object> paramsMap = jdbcTemplate.queryForMap(
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
				
				//System.out.println("strParams1="+strParams);
				String[] actData = null;
				// 操作数据;
				// JSONArray jsonArray = null;
				List<Map<String, Object>> jsonArray = null;
				int num1 = 0;

				if (jacksonUtil.containsKey("add")) {
					jsonArray = (List<Map<String, Object>>) jacksonUtil.getList("add");
					// System.out.println(jacksonUtil.Map2json(jsonArray));
					if (jsonArray != null && jsonArray.size() > 0) {
						actData = new String[jsonArray.size()];
						for (num1 = 0; num1 < jsonArray.size(); num1++) {
							actData[num1] = jacksonUtil.Map2json(jsonArray.get(num1));
							// System.out.println(actData[num1]);
						}

					}
				}
				String coulumdt = jacksonUtil.containsKey("coulumdt") ? jacksonUtil.getString("coulumdt") : null;

				
				this.exportstudeninfo(actData, Integer.valueOf(coulumdt),tzGDObject,jdbcTemplate,excelHandle,runControlId,processinstance);

				
			} else {
				this.logError("获取批处理参数失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.logError("系统错误，" + e.getMessage());
		}
	}

	/**
	 * 导出面试学生信息
	 * 
	 * @param actData
	 * @param errorMsg
	 * @return
	 */
	private void exportstudeninfo(String[] actData, Integer coulumdt, TZGDObject tzGDObject, SqlQuery jdbcTemplate,ExcelHandle2 excelHandle,String runControlId,int processinstance) {

		try {

			String hardcodeSql = "select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT where TZ_HARDCODE_PNT=?";
			schoolNameXxxId = jdbcTemplate.queryForObject(hardcodeSql, new Object[] { "TZ_BMB_BZKYX_XXX_ID" },
					"String");
			highestRecordXxxId = jdbcTemplate.queryForObject(hardcodeSql, new Object[] { "TZ_BMB_ZGXL_XXX_ID" },
					"String");
			highestRecordXlkId = jdbcTemplate.queryForObject(hardcodeSql, new Object[] { "TZ_BMB_ZGXL_XLK_ID" },
					"String");
			companyAddressXxxId = jdbcTemplate.queryForObject(hardcodeSql, new Object[] { "TZ_BMB_GZSZD_XXX_ID" },
					"String");
			companyNameXxxId = jdbcTemplate.queryForObject(hardcodeSql, new Object[] { "TZ_BMB_GZDW_XXX_ID" },
					"String");
			departmentXxxId = jdbcTemplate.queryForObject(hardcodeSql, new Object[] { "TZ_BMB_SZBM_XXX_ID" }, "String");
			positionXxxId = jdbcTemplate.queryForObject(hardcodeSql, new Object[] { "TZ_BMB_GZZW_XXX_ID" }, "String");
			selfEmploymentXxxId = jdbcTemplate.queryForObject(hardcodeSql, new Object[] { "TZ_BMB_ZZCYQC_XXX_ID" },
					"String");
			WORK_ON_YEAR_XXX_ID = jdbcTemplate.queryForObject(hardcodeSql, new Object[] { "TZ_BMB_GZGN_XXX_ID" },
					"String");

			String appModalId = "", nationId = "", mssqh = "", name = "", birthday = "", age = "", sex = "",
					schoolName = "", highestRecordId = "", companyAddress = "", companyName = "", department = "",
					position = "", selfEmployment = "", gzage = "", highestRecord = "";

		
			// DateUtil dateUtil = new DateUtil();


			String strForm = "";
			String xmid = "";
			String teamID = "";

			// 班级和批次Id
			String classId = "";
			String batchId = "";

			String scoreInsId = "";
			String sunrank = "";
			String sunscore = "";
			String clpszf = "";
	
			
			List<Map<String, Object>> listInfor=null;
	
			String bkzysql = "";
			// 报考志愿名 班级名加批次名
			String bkzyName = "";
			Map<String, Object> bkzylist = new HashMap<String, Object>();
			Map<String, Object> classBatchMap = new HashMap<String, Object>();

			int m = 0;
			String judge_id = "";
			JacksonUtil jacksonUtil = new JacksonUtil();

			jacksonUtil.json2Map(actData[0]);
			xmid = jacksonUtil.getString("xmid");
			teamID = jacksonUtil.getString("teamID");
			classId = jdbcTemplate.queryForObject("SELECT TZ_CLASS_ID FROM PS_TZ_FORM_WRK_T WHERE TZ_APP_INS_ID=?",
					new Object[] { xmid }, "String");

			// 生成数据

			// 表头
			List<String[]> dataCellKeys = new ArrayList<String[]>();
			ArrayList<String[]> list = new ArrayList<String[]>();
			ArrayList<String[]> pw_list = null;

			dataCellKeys.add(new String[] { "zjid", "证件号" });
			dataCellKeys.add(new String[] { "mssqid", "面试申请号" });
			dataCellKeys.add(new String[] { "name", "姓名" });
			dataCellKeys.add(new String[] { "bkzy", "报考志愿" });
			dataCellKeys.add(new String[] { "zpm", "总排名" });
			dataCellKeys.add(new String[] { "msz", "面试组" });
			dataCellKeys.add(new String[] { "zlpm", "面内排名组" });
			dataCellKeys.add(new String[] { "msz", "面试组" });
			dataCellKeys.add(new String[] { "bzzf", "标准总分" });
			dataCellKeys.add(new String[] { "clpsyszf", "材料评审原始总分" });
			dataCellKeys.add(new String[] { "birthday", "出身日期" });
			dataCellKeys.add(new String[] { "drxsln", "到入学时年龄" });

			// 成绩项
			List<String[]> listScoreItemField = new ArrayList<String[]>();
			/// String sql =
			/// tZGDObject.getSQLText("SQL.TZMaterialInterviewReviewBundle.material.TzGetMaterialScoreItemInfo");
			String sql = "SELECT C.TZ_SCORE_ITEM_ID,C.DESCR,C.TZ_SCORE_ITEM_TYPE FROM PSTREENODE D,PS_TZ_CLASS_INF_T A,PS_TZ_MODAL_DT_TBL C,PS_TZ_RS_MODAL_TBL B WHERE D.TREE_NAME=B.TREE_NAME AND D.TREE_NODE=C.TZ_SCORE_ITEM_ID AND B.TZ_JG_ID=C.TZ_JG_ID AND B.TREE_NAME=C.TREE_NAME AND A.TZ_MSCJ_SCOR_MD_ID=B.TZ_SCORE_MODAL_ID AND B.TZ_JG_ID=A.TZ_JG_ID AND A.TZ_CLASS_ID=? ORDER BY D.TREE_NODE_NUM";
			List<Map<String, Object>> listScoreItem = jdbcTemplate.queryForList(sql, new Object[] { classId });

			
			for (int n = 0; n < coulumdt; n++) {
				dataCellKeys.add(new String[] { "judg_" + (n + 1) + "_id", "评委" + (n + 1) + "账号" });
				dataCellKeys.add(new String[] { "judg_" + (n + 1) + "_zscore", "评委" + (n + 1) + "标准总分" });
				dataCellKeys.add(new String[] { "judg_" + (n + 1) + "_num", "评委" + (n + 1) + "评审人数" });
				dataCellKeys.add(new String[] { "judg_" + (n + 1) + "_rank", "评委" + (n + 1) + "考生评审排名" });

				for (Map<String, Object> mapScoreItem : listScoreItem) {
					String scoreItemId = mapScoreItem.get("TZ_SCORE_ITEM_ID") == null ? ""
							: mapScoreItem.get("TZ_SCORE_ITEM_ID").toString();
					String scoreItemName = mapScoreItem.get("DESCR") == null ? ""
							: mapScoreItem.get("DESCR").toString();
					String scoreItemType = mapScoreItem.get("TZ_SCORE_ITEM_TYPE") == null ? ""
							: mapScoreItem.get("TZ_SCORE_ITEM_TYPE").toString();
					dataCellKeys.add(new String[] { "num" + (n + 1) + scoreItemId, "评委" + (n + 1) + scoreItemName });
					listScoreItemField.add(new String[] { scoreItemId, scoreItemType });
				}

				/*
				 * for (int i = 0; i < list.size(); i++) { String[] row =
				 * list.get(i); dataCellKeys.add(new String[] { "num" + (n + 1)
				 * + row[0], "评委" + (n + 1) + row[1] });
				 * 
				 * }
				 */

			}
			dataCellKeys.add(new String[] { "sex", "性别" });
			dataCellKeys.add(new String[] { "bkzkyx", "本/专科院校" });
			dataCellKeys.add(new String[] { "zgxl", "最高学历" });
			dataCellKeys.add(new String[] { "gzage", "工龄" });
			dataCellKeys.add(new String[] { "gzdw", "工作单位" });
			dataCellKeys.add(new String[] { "gzszd", "工作所在地" });
			dataCellKeys.add(new String[] { "gzbm", "工作部门" });
			dataCellKeys.add(new String[] { "gzzw", "工作职位" });
			dataCellKeys.add(new String[] { "zhcyqc", "自主创业全称" });

			// 数据
			List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();

			Float[] sunscorelist = new Float[actData.length];

			for (int num_1 = 0; num_1 < actData.length; num_1++) {
				// 表单内容
				Map<String, Object> mapData = new HashMap<String, Object>();
				pw_list = new ArrayList<String[]>();
				strForm = actData[num_1];
				// 解析 json
				jacksonUtil.json2Map(strForm);
				xmid = jacksonUtil.getString("xmid");

				teamID = jacksonUtil.getString("teamID");
				sunrank = jacksonUtil.getString("judge_sunrank");

				classBatchMap = jdbcTemplate.queryForMap(
						"SELECT TZ_BATCH_ID,TZ_CLASS_ID FROM PS_TZ_FORM_WRK_T WHERE TZ_APP_INS_ID=?",
						new Object[] { xmid });
				classId = classBatchMap.get("TZ_CLASS_ID") == null ? "" : classBatchMap.get("TZ_CLASS_ID").toString();
				batchId = classBatchMap.get("TZ_BATCH_ID") == null ? "" : classBatchMap.get("TZ_BATCH_ID").toString();

				clpszf = jdbcTemplate.queryForObject(tzGDObject.getSQLText("SQL.TZBzScoreMathBundle.TzGetClpSumScore"),
						new Object[] { xmid }, "String");
				if (clpszf != null) {
					sunscorelist[num_1] = 9999999 - Float.valueOf(sunrank) * 100000 + Float.valueOf(clpszf);
				} else {
					sunscorelist[num_1] = 9999999 - Float.valueOf(sunrank) * 100000;
				}

			}

			for (int num = 0; num < actData.length; num++) {
				//long starttime = System.currentTimeMillis();
				//System.out.println("成绩项开始时间导出：" + System.currentTimeMillis());
				// 表单内容
				Map<String, Object> mapData = new HashMap<String, Object>();
				pw_list = new ArrayList<String[]>();
				strForm = actData[num];
				// 解析 json
				jacksonUtil.json2Map(strForm);
				xmid = jacksonUtil.getString("xmid");
				// System.out.println("这边的" + xmid);
				teamID = jacksonUtil.getString("teamID");
				sunrank = jacksonUtil.getString("judge_sunrank");
				sunscore = jacksonUtil.getString("judge_sunscore");

				mapData.put("msz", teamID);
				mapData.put("zlpm", sunrank);
				mapData.put("bzzf", sunscore);

				// 材料评审总分
				clpszf = jdbcTemplate.queryForObject(tzGDObject.getSQLText("SQL.TZBzScoreMathBundle.TzGetClpSumScore"),
						new Object[] { xmid }, "String");

				// 排名
				if (clpszf != null) {
					mapData.put("zpm", this.checksunRank(sunscorelist,
							9999999 - Float.valueOf(sunrank) * 100000 + Float.valueOf(clpszf)));
				} else {
					mapData.put("zpm", this.checksunRank(sunscorelist, 9999999 - Float.valueOf(sunrank) * 100000));
				}

				mapData.put("clpsyszf", clpszf);

				for (m = 0; m < coulumdt; m++) {

					mapData.put("judg_" + (m + 1) + "_id",
							jacksonUtil.getString("judge_" + String.valueOf(m + 1) + "_id"));
					mapData.put("judg_" + (m + 1) + "_zscore",
							jacksonUtil.getString("judge_" + String.valueOf(m + 1) + "_zscore"));
					mapData.put("judg_" + (m + 1) + "_num",
							jacksonUtil.getString("judge_" + String.valueOf(m + 1) + "_num"));
					mapData.put("judg_" + (m + 1) + "_rank",
							jacksonUtil.getString("judge_" + String.valueOf(m + 1) + "_rank"));
					judge_id = jacksonUtil.getString("judge_" + String.valueOf(m + 1) + "_id");

					// 评委数据
					try {

						sql = tzGDObject.getSQLText("SQL.TZBzScoreMathBundle.TZ_MSPS_SCOR_INS_SQL");

					} catch (TzSystemException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					scoreInsId = jdbcTemplate.queryForObject(sql, new Object[]{batchId,xmid,classId,judge_id}, "String");
							
							

					// 成绩项分数
					List<Map<String, Object>> listScore = jdbcTemplate.queryForList("SELECT TZ_CJX_XLK_XXBH,TZ_SCORE_PY_VALUE,TZ_SCORE_NUM,TZ_SCORE_ITEM_ID FROM PS_TZ_CJX_TBL  WHERE TZ_SCORE_INS_ID=?", new Object[] { scoreInsId });
					Map<String, Object> listScoreMap = new HashMap<String, Object>();
					for (Map<String, Object> map : listScore) {
						listScoreMap.put(
								 map.get("TZ_SCORE_ITEM_ID") == null ? ""
										: "D-" +map.get("TZ_SCORE_ITEM_ID").toString(),
								map.get("TZ_CJX_XLK_XXBH") == null ? "" : map.get("TZ_CJX_XLK_XXBH").toString());
						listScoreMap.put(
								 map.get("TZ_SCORE_ITEM_ID") == null ? ""
										: "C-" +map.get("TZ_SCORE_ITEM_ID").toString(),
								map.get("TZ_SCORE_PY_VALUE") == null ? "" : map.get("TZ_SCORE_PY_VALUE").toString());
						listScoreMap.put(
								 map.get("TZ_SCORE_ITEM_ID") == null ? ""
										: "E-" +map.get("TZ_SCORE_ITEM_ID").toString(),
								map.get("TZ_SCORE_NUM") == null ? "" : map.get("TZ_SCORE_NUM").toString());
					}
										
					// SELECT TZ_SCORE_NUM FROM PS_TZ_CJX_TBL WHERE
					// TZ_SCORE_INS_ID=?

					for (String[] scoreField : listScoreItemField) {
						String scoreItemId = scoreField[0];
						String scoreItemType = scoreField[1];
						// System.out.println("执行成绩项time="+(System.currentTimeMillis()-starttime));
						String scoreValue = "";
						if ("D".equals(scoreItemType)) {
							// 下拉框
							scoreValue = listScoreMap.get("D-" + scoreItemId)==null?"":listScoreMap.get("D-" + scoreItemId).toString();
							
						} else {
							if ("C".equals(scoreItemType)) {
								// 评语
								scoreValue = listScoreMap.get("C-" + scoreItemId)==null?"":listScoreMap.get("C-" + scoreItemId).toString();
								

							} else {
								scoreValue = listScoreMap.get("E-" + scoreItemId)==null?"":listScoreMap.get("E-" + scoreItemId).toString();
								
							}
						}

					
						if ("D".equals(scoreItemType)) {
							sql = "SELECT A.TZ_CJX_XLK_XXMC FROM PS_TZ_ZJCJXXZX_T A,PS_TZ_RS_MODAL_TBL B,PS_TZ_CLASS_INF_T C ";
							sql += " WHERE C.TZ_ZLPS_SCOR_MD_ID=B.TZ_SCORE_MODAL_ID AND A.TZ_JG_ID=B.TZ_JG_ID AND A.TREE_NAME=B.TREE_NAME";
							sql += " AND C.TZ_CLASS_ID=? AND A.TZ_JG_ID=C.TZ_JG_ID AND A.TZ_SCORE_ITEM_ID=? AND A.TZ_CJX_XLK_XXBH=?";
							scoreValue = jdbcTemplate.queryForObject(sql,
									new Object[] { classId, scoreItemId, scoreValue }, "String");

						}

						mapData.put("num" + (m + 1) + scoreItemId, scoreValue);

					}
					// System.out.println("time01="+(System.currentTimeMillis()-starttime));
				}
				try {
					long starttime1=System.currentTimeMillis();
											
					// 考生数据
					String sqlinfo = tzGDObject.getSQLText("SQL.TZBzScoreMathBundle.TZ_MSPS_STU_INFOSQL");
					Map<String, Object> mapExaminee = jdbcTemplate.queryForMap(sqlinfo, new Object[] { classId, xmid });
					//System.out.println("个了信息time=" + (System.currentTimeMillis() - starttime1));
					if (mapExaminee != null) {
						appModalId = mapExaminee.get("TZ_APP_MODAL_ID") == null ? ""
								: mapExaminee.get("TZ_APP_MODAL_ID").toString();
						nationId = mapExaminee.get("NATIONAL_ID") == null ? ""
								: mapExaminee.get("NATIONAL_ID").toString();
						mssqh = mapExaminee.get("TZ_MSH_ID") == null ? "" : mapExaminee.get("TZ_MSH_ID").toString();
						name = mapExaminee.get("TZ_REALNAME") == null ? "" : mapExaminee.get("TZ_REALNAME").toString();
						birthday = mapExaminee.get("BIRTHDATE") == null ? "" : mapExaminee.get("BIRTHDATE").toString();
						age = mapExaminee.get("AGE") == null ? "" : mapExaminee.get("AGE").toString();
						sex = mapExaminee.get("TZ_GENDER_DESC") == null ? ""
								: mapExaminee.get("TZ_GENDER_DESC").toString();
						
						String appSql = "SELECT TZ_XXX_BH,TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T WHERE TZ_APP_INS_ID=? ";
						listInfor = jdbcTemplate.queryForList(appSql, new Object[] { xmid });
						Map<String, Object> listInfoMap = new HashMap<String, Object>();
						for (Map<String, Object> map : listInfor) {
							listInfoMap.put(
									 map.get("TZ_XXX_BH") == null ? ""
											: map.get("TZ_XXX_BH").toString(),
									map.get("TZ_APP_S_TEXT") == null ? "" : map.get("TZ_APP_S_TEXT").toString());
						
						}
						
						schoolName =listInfoMap.get(schoolNameXxxId)==null?"":listInfoMap.get(schoolNameXxxId).toString();
																	
						highestRecordId =listInfoMap.get(highestRecordXxxId)==null?"":listInfoMap.get(highestRecordXxxId).toString();
							
						companyAddress = listInfoMap.get(companyAddressXxxId)==null?"":listInfoMap.get(companyAddressXxxId).toString();
								
						companyName = listInfoMap.get(companyNameXxxId)==null?"":listInfoMap.get(companyNameXxxId).toString();
								
						department = listInfoMap.get(departmentXxxId)==null?"":listInfoMap.get(departmentXxxId).toString();
								
						position =listInfoMap.get(positionXxxId)==null?"":listInfoMap.get(positionXxxId).toString();
				
						selfEmployment = listInfoMap.get(selfEmploymentXxxId)==null?"":listInfoMap.get(selfEmploymentXxxId).toString();
								
						gzage = listInfoMap.get(WORK_ON_YEAR_XXX_ID)==null?"":listInfoMap.get(WORK_ON_YEAR_XXX_ID).toString();
							
						// 最高学历描述
						String highestSql = "SELECT TZ_XXXKXZ_MS FROM PS_TZ_APPXXX_KXZ_T WHERE TZ_APP_TPL_ID=? AND TZ_XXX_BH=? AND TZ_XXXKXZ_MC=?";
						highestRecord = jdbcTemplate.queryForObject(highestSql,
								new Object[] { appModalId, highestRecordXlkId, highestRecordId }, "String");

						// 报考方向拼接批次
						// 查出报名志愿 班级名加批次名
						bkzysql = "SELECT CONCAT(B.TZ_CLASS_NAME,C.TZ_BATCH_NAME) AS A FROM PS_TZ_FORM_WRK_T A,PS_TZ_CLASS_INF_T B,PS_TZ_CLS_BATCH_T C WHERE B.TZ_CLASS_ID=A.TZ_CLASS_ID AND C.TZ_CLASS_ID=A.TZ_CLASS_ID AND  C.TZ_BATCH_ID=A.TZ_BATCH_ID AND TZ_APP_INS_ID=? ";
						bkzyName = jdbcTemplate.queryForObject(bkzysql, new Object[] { xmid }, "String");

						mapData.put("bkzy", bkzyName);

						// 材料评审总分
						try {
							clpszf = jdbcTemplate.queryForObject(
									tzGDObject.getSQLText("SQL.TZBzScoreMathBundle.TzGetClpSumScore"),
									new Object[] { xmid }, "String");
						} catch (TzSystemException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						mapData.put("mssqid", mssqh);
						mapData.put("sex", sex);
					
						mapData.put("name", name);
						
						mapData.put("zjid", nationId);
						
						mapData.put("bkzkyx", schoolName);

						mapData.put("birthday", birthday);
						
						mapData.put("drxsln", age);

						mapData.put("zgxl", highestRecord);
						mapData.put("gzdw", companyName);
						mapData.put("gzzw", position);
						mapData.put("gzbm", department);
						mapData.put("gzage", gzage);
						mapData.put("zhcyqc", selfEmployment);
					}
					

				} catch (Exception e) {
					e.printStackTrace();
					
				}
				
				

				dataList.add(mapData);
			}
			
			
			sql = "select TZ_SYSFILE_NAME from PS_TZ_EXCEL_DATT_T where PROCESSINSTANCE=?";
			String strUseFileName = jdbcTemplate.queryForObject(sql, new Object[] { processinstance }, "String");

			boolean rst = excelHandle.export2Excel(strUseFileName, dataCellKeys, dataList);
			if (rst) {
				String urlExcel = excelHandle.getExportExcelPath();
				try {
					jdbcTemplate.update("update  PS_TZ_EXCEL_DATT_T set TZ_FWQ_FWLJ = ? where PROCESSINSTANCE=?",
							new Object[] { urlExcel, processinstance });
				} catch (Exception e2) {
					
					e2.printStackTrace();
				}
			}
	
		}catch (TzSystemException e) {
			e.printStackTrace();
			this.logError("系统错误，" + e.getMessage());
			// TODO Auto-generated catch block
			
		}

	}
		/***
		 * 返回 排名
		 * 
		 * @param arrs
		 * @param score
		 * @return
		 */
		public String checksunRank(Float[] arrs, Float score) {

			Arrays.sort(arrs);
			int j = 1;
			while (!score.equals(arrs[arrs.length - j])) {
				j++;
			}
			return String.valueOf(j);

		}
}
