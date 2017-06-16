package com.tranzvision.gd.TZBzScoreMathBundle.service.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZApplicationVerifiedBundle.dao.PsTzExcelDattTMapper;
import com.tranzvision.gd.TZApplicationVerifiedBundle.dao.PsTzExcelDrxxTMapper;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzExcelDattT;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzExcelDrxxT;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZAutomaticScreenBundle.dao.PsTzZdcsDcAetMapper;
import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzZdcsDcAet;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.batch.engine.base.BaseEngine;
import com.tranzvision.gd.batch.engine.base.EngineParameters;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.poi.excel.ExcelHandle;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

@Service("com.tranzvision.gd.TZBzScoreMathBundle.service.impl.TzBzScoreMathbasetoWindowServiceImpl")
public class TzBzScoreMathbasetoWindowServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	@Autowired
	private TZGDObject TzGDObject;
	@Autowired
	private PsTzExcelDattTMapper psTzExcelDattTMapper;
	@Autowired
	private PsTzZdcsDcAetMapper psTzZdcsDcAetMapper;
	@Autowired
	private GetSeqNum GetSeqNum;
	@Autowired
	private PsTzExcelDrxxTMapper psTzExcelDrxxTMapper;
	

	
	

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

	String strRet = "";

	@Override
	@SuppressWarnings({ "unchecked", "null" })
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		// System.out.println(comParams);
		DecimalFormat decimalFormat = new DecimalFormat(".00");
		mapRet.put("total", 0);
		// 获取当前登陆人机构ID
		// String Orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);

		JacksonUtil jacksonUtil = new JacksonUtil();

		ArrayList<String[]> list = new ArrayList<String[]>();
		jacksonUtil.json2Map(comParams);

		String[] actData = null;
		// 操作数据;
		// JSONArray jsonArray = null;
		List<Map<String, Object>> jsonArray = null;
		int num1 = 0;
		// System.out.println(jacksonUtil.containsKey("add"));
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
		Float maxscore = jacksonUtil.containsKey("maxscore") ? Float.valueOf(jacksonUtil.getString("maxscore")) : null;
		Float minscore = jacksonUtil.containsKey("minscore") ? Float.valueOf(jacksonUtil.getString("minscore")) : null;
		String coulumdt = jacksonUtil.containsKey("coulumdt") ? jacksonUtil.getString("coulumdt") : null;
		// System.out.println(coulumdt);
		try {
			int num = 0;
			String xmid = "";
			String xmName = "";
			String teamID = "";
			String strForm = "";
			String strForm_a = "";
			String  fg=",";
			String key_pw = "";
			String value_pw = "";
			String value_pw_1 = "";
			String find_key = "";
			String[] bb = null;
			
			Map<String, String> map_pw = new HashMap<String, String>();
			


			String[][] judge_id = new String[Integer.valueOf(coulumdt)][5];
			String[][] judge_id_a = new String[Integer.valueOf(coulumdt)][5];
			Float[] sunscorelist = new Float[actData.length];
			Float aFloat = (float) 0.0000;
			Float sunfloat = null;

			int con_i = 0;
			int cou_j = 0;
			// 先把所有标准很的和放到一个数组中，用于后面的组内排名

			for (int num_1 = 0; num_1 < actData.length; num_1++) {
				strForm_a = actData[num_1];
				jacksonUtil.json2Map(strForm_a);
				sunfloat = (float) 0.0000;
				xmid = jacksonUtil.getString("xmid");
				xmName = jacksonUtil.getString("xmName");
				teamID = jacksonUtil.getString("teamID");
				for (cou_j = 0; cou_j < Integer.valueOf(coulumdt); cou_j++) {
					if (jacksonUtil.getString("judge_" + String.valueOf(cou_j + 1) + "_id") == null) {

					} else {
						judge_id_a[cou_j][0] = jacksonUtil.getString("judge_" + String.valueOf(cou_j + 1) + "_id");

						judge_id_a[cou_j][1] = jacksonUtil.getString("judge_" + String.valueOf(cou_j + 1) + "_num");

						judge_id_a[cou_j][2] = jacksonUtil.getString("judge_" + String.valueOf(cou_j + 1) + "_score");

						judge_id_a[cou_j][3] = jacksonUtil.getString("judge_" + String.valueOf(cou_j + 1) + "_rank");

						// 算出标准成绩
						if (judge_id_a[cou_j][1].equals("1")) {
							aFloat = maxscore;

						} else {
							aFloat = maxscore - (Integer.valueOf(judge_id_a[cou_j][3]) - 1)
									* ((maxscore - minscore) / (Integer.valueOf(judge_id_a[cou_j][1]) - 1));
						}

						sunfloat = sunfloat + aFloat;

						judge_id_a[cou_j][4] = String.valueOf(aFloat);

					}
				}
				
				key_pw = teamID ;
				value_pw = "9999999999999" + fg + String.valueOf(sunfloat);
				value_pw_1 = String.valueOf(sunfloat);
				if (map_pw.get(key_pw) == null) {
					map_pw.put(key_pw, value_pw);

				} else {
					map_pw.replace(key_pw, map_pw.get(key_pw).toString() + fg + value_pw_1);

				}
				sunscorelist[num_1] = sunfloat;
				
				
				

			}

			for (num = 0; num < actData.length; num++) {
				// 提交信息
				strForm = actData[num];
				// System.out.println(strForm);
				jacksonUtil.json2Map(strForm);
				sunfloat = (float) 0.00;
				Map<String, Object> mapList = new HashMap<String, Object>();
				xmid = jacksonUtil.getString("xmid");
				xmName = jacksonUtil.getString("xmName");
				teamID = jacksonUtil.getString("teamID");
				mapList.put("xmid", xmid);
				mapList.put("xmName", xmName);
				mapList.put("teamID", teamID);
				bb = map_pw.get(teamID).split(",");

				for (con_i = 0; con_i < Integer.valueOf(coulumdt); con_i++) {
					if (jacksonUtil.getString("judge_" + String.valueOf(con_i + 1) + "_id") == null) {
						mapList.put("judge_" + String.valueOf(con_i + 1) + "_id", null);
						mapList.put("judge_" + String.valueOf(con_i + 1) + "_num", null);
						mapList.put("judge_" + String.valueOf(con_i + 1) + "_score", null);
						mapList.put("judge_" + String.valueOf(con_i + 1) + "_rank", null);
						mapList.put("judge_" + String.valueOf(con_i + 1) + "_zscore", null);
					} else {
						judge_id[con_i][0] = jacksonUtil.getString("judge_" + String.valueOf(con_i + 1) + "_id");
						mapList.put("judge_" + String.valueOf(con_i + 1) + "_id", judge_id[con_i][0]);

						judge_id[con_i][1] = jacksonUtil.getString("judge_" + String.valueOf(con_i + 1) + "_num");
						mapList.put("judge_" + String.valueOf(con_i + 1) + "_num", judge_id[con_i][1]);

						judge_id[con_i][2] = jacksonUtil.getString("judge_" + String.valueOf(con_i + 1) + "_score");
						mapList.put("judge_" + String.valueOf(con_i + 1) + "_score", judge_id[con_i][2]);

						judge_id[con_i][3] = jacksonUtil.getString("judge_" + String.valueOf(con_i + 1) + "_rank");
						mapList.put("judge_" + String.valueOf(con_i + 1) + "_rank", judge_id[con_i][3]);

						// 算出标准成绩
						// System.out.println("评委数" + cou_j + ":" +
						// judge_id_a[con_i][1]);

						if (judge_id[con_i][1].equals("1")) {
							aFloat = maxscore;

						} else {
							aFloat = maxscore - (Integer.valueOf(judge_id[con_i][3]) - 1)
									* ((maxscore - minscore) / (Integer.valueOf(judge_id[con_i][1]) - 1));
						}

						/*
						 * aFloat = maxscore -
						 * (Integer.valueOf(judge_id[con_i][3]) - 1) ((maxscore
						 * - minscore) / (Integer.valueOf(judge_id[con_i][1]) -
						 * 1));
						 */

						sunfloat = sunfloat + aFloat;

						judge_id[con_i][4] = String.valueOf(aFloat);
						mapList.put("judge_" + String.valueOf(con_i + 1) + "_zscore", judge_id[con_i][4]);

					}

				}
				
				mapList.put("judge_sunscore", sunfloat);
				mapList.put("judge_sunrank", this.checkpeRank(bb, String.valueOf(sunfloat)));
				listData.add(mapList);
			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。" + e.getMessage();
			// TODO: handle exception
		}

		mapRet.replace("total", 0);
		mapRet.replace("root", listData);
		return jacksonUtil.Map2json(mapRet);

	}

	// 检查人员编号时候合法
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		// 返回值
		Map<String, Object> mapRet = new HashMap<String, Object>();
		String xmid = "";
		String strForm = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		String sql = "";
		String is_Y = "";
		String personid = "";
		try {
			for (int num = 0; num < actData.length; num++) {
				// 表单内容
				strForm = actData[num];
				// 解析 json
				jacksonUtil.json2Map(strForm);
				xmid = jacksonUtil.getString("xmid");
				sql = "SELECT 'Y' FROM PS_TZ_APP_INS_T WHERE TZ_APP_INS_ID=?";
				// System.out.println("第" + num + 1 + ":" + xmid);
				is_Y = jdbcTemplate.queryForObject(sql, new Object[] { xmid }, "String");

				if (is_Y == null || !is_Y.equals("Y")) {

					if (personid.equals("")) {
						personid = xmid;

					} else {
						personid = personid + "," + xmid;
					}

				} else {

				}

			}

			mapRet.put("personid", personid);

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "发生异常。" + e.getMessage();
			// TODO: handle exception
		}
		return jacksonUtil.Map2json(mapRet);
	}

	/**
	 * 标准成绩导出操作
	 * 
	 * @param strType
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String tzOther(String strType, String strParams, String[] errorMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			String[] actData = null;
			// 操作数据;
			// JSONArray jsonArray = null;
			List<Map<String, Object>> jsonArray = null;
			int num1 = 0;
             //System.out.println("strParams="+strParams);
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

			switch (strType) {
			case "EXPORT":
				
				strRet = this.tzExportExcelFile(strParams, errorMsg);
				
				break;
			case "MAXCOULUM":
				String max_jud = this.maxjudnum(actData, errorMsg);
				// System.out.println(max_jud);
				mapRet.put("maxnum", max_jud);
				strRet = jacksonUtil.Map2json(mapRet);

				break;

			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。" + e.getMessage();
			// TODO: handle exception
		}

		return strRet;
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
	
	
	/***
	 * 对字符串数组进行转换再排名 返回 排名
	 * 
	 * @param arrs
	 * @param score
	 * @return
	 */
	public String checkpeRank(String[] arrs, String score) {
		float[] ints = new float[arrs.length];
		for (int i = 0; i < arrs.length; i++) {
			ints[i] = Float.parseFloat(arrs[i]);
		}
		Arrays.sort(ints);
		int j = 2;
		while (!Float.valueOf(score).equals(ints[ints.length - j])) {
			j++;
		}
		return String.valueOf(j - 1);

	}

	/**
	 * 导出面试学生信息
	 * 
	 * @param actData
	 * @param errorMsg
	 * @return
	 */
	public String exportstudeninfo(String[] actData, Integer coulumdt, String[] errorMsg) {

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

			String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

			String appModalId = "", nationId = "", mssqh = "", name = "", birthday = "", age = "", sex = "",
					schoolName = "", highestRecordId = "", companyAddress = "", companyName = "", department = "",
					position = "", selfEmployment = "", gzage = "", highestRecord = "";

			// 获取文件存储路径
			String fileBasePath = getSysHardCodeVal.getDownloadPath();
			// DateUtil dateUtil = new DateUtil();

			// 活动报名信息excel存储路径
			String eventExcelPath = "/events/xlsx";

			// 完整的存储路径
			String fileDirPath = fileBasePath + eventExcelPath;
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
			String[] rowList = null;
			String[] pw_rowList = null;
			String[] pw_row = null;
			String[] daterow = null;
			
			List<Map<String, Object>> listInfor=null;
			int daterow_i = 0;
			String bkzysql = "";
			// 报考志愿名 班级名加批次名
			String bkzyName = "";
			int bkzyArray_i = 0;
			Map<String, Object> bkzylist = new HashMap<String, Object>();
			Map<String, Object> classBatchMap = new HashMap<String, Object>();

			int m = 0;
			int pwsocre_i = 0;
			String judge_id = "";
			String judgeid = "";
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

			/*
			 * for (Map<String, Object> mapScoreItem : listScoreItem) { String
			 * scoreItemId = mapScoreItem.get("TZ_SCORE_ITEM_ID") == null ? "" :
			 * mapScoreItem.get("TZ_SCORE_ITEM_ID").toString(); String
			 * scoreItemName = mapScoreItem.get("DESCR") == null ? "" :
			 * mapScoreItem.get("DESCR").toString(); String scoreItemType =
			 * mapScoreItem.get("TZ_SCORE_ITEM_TYPE") == null ? "" :
			 * mapScoreItem.get("TZ_SCORE_ITEM_TYPE").toString();
			 * dataCellKeys.add(new String[] { scoreItemId, scoreItemName });
			 * listScoreItemField.add(new String[] { scoreItemId, scoreItemType
			 * }); }
			 */

			// 在数据库中查询 描述 根据 循环生成 表头
			/*
			 * List<Map<String, Object>> treename = jdbcTemplate.queryForList(
			 * TzGDObject.getSQLText(
			 * "SQL.TZBzScoreMathBundle.TZ_TREE_EXCEL_HEADER"), new Object[] {
			 * Integer.valueOf(xmid), orgid });
			 */

			/*
			 * for (int treename_i = 0; treename_i < treename.size();
			 * treename_i++) { Map<String, Object> resultMap =
			 * treename.get(treename_i); rowList = new String[2]; int j = 0; for
			 * (Object value : resultMap.values()) { rowList[j] =
			 * value.toString(); j++; } list.add(rowList); }
			 */
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

				clpszf = jdbcTemplate.queryForObject(TzGDObject.getSQLText("SQL.TZBzScoreMathBundle.TzGetClpSumScore"),
						new Object[] { xmid }, "String");
				if (clpszf != null) {
					sunscorelist[num_1] = 9999999 - Float.valueOf(sunrank) * 100000 + Float.valueOf(clpszf);
				} else {
					sunscorelist[num_1] = 9999999 - Float.valueOf(sunrank) * 100000;
				}

			}

			for (int num = 0; num < actData.length; num++) {
				long starttime = System.currentTimeMillis();
				System.out.println("成绩项开始时间导出：" + System.currentTimeMillis());
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
				clpszf = jdbcTemplate.queryForObject(TzGDObject.getSQLText("SQL.TZBzScoreMathBundle.TzGetClpSumScore"),
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

						sql = TzGDObject.getSQLText("SQL.TZBzScoreMathBundle.TZ_MSPS_SCOR_INS_SQL");

					} catch (TzSystemException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					scoreInsId = jdbcTemplate.queryForObject(sql, new Object[]{batchId,xmid,classId,judge_id}, "String");
							
							//jdbcTemplate.queryForObject(sql, new Object[]{batchId,xmid,classId,judge_id},"String");
					// System.out.println("listJudge执行time="+(System.currentTimeMillis()-starttime));
					/*
					 * for (Map<String, Object> mapJudge : listJudge) {
					 * scoreInsId = mapJudge.get("TZ_SCORE_INS_ID") == null ? ""
					 * : mapJudge.get("TZ_SCORE_INS_ID").toString();
					 */

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
					 System.out.println("time01="+(System.currentTimeMillis()-starttime));
				}
				try {
					long starttime1=System.currentTimeMillis();
											
					// 考生数据
					String sqlinfo = TzGDObject.getSQLText("SQL.TZBzScoreMathBundle.TZ_MSPS_STU_INFOSQL");
					Map<String, Object> mapExaminee = jdbcTemplate.queryForMap(sqlinfo, new Object[] { classId, xmid });
					System.out.println("个了信息time=" + (System.currentTimeMillis() - starttime1));
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
									TzGDObject.getSQLText("SQL.TZBzScoreMathBundle.TzGetClpSumScore"),
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
						mapData.put("gzbm", gzage);
						mapData.put("zhcyqc", selfEmployment);
					}
					

				} catch (Exception e) {
					e.printStackTrace();
					errorMsg[0] = "1";
					errorMsg[1] = "导出失败。" + e.getMessage();
					// TODO: handle exception
				}
				// System.out.println("一条结束时间："+System.currentTimeMillis());
				

				dataList.add(mapData);
			}

			// 生成本次导出的文件名
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			Random random = new Random();
			int max = 999999999;
			int min = 100000000;
			String fileName = simpleDateFormat.format(new Date()) + "_" + oprid.toUpperCase() + "_"
					+ String.valueOf(random.nextInt(max) % (max - min + 1) + min) + ".xlsx";

			ExcelHandle excelHandle = new ExcelHandle(request, fileDirPath, orgid, "apply");
			boolean rst = excelHandle.export2Excel(fileName, dataCellKeys, dataList);
			if (rst) {
				// System.out.println("---------生成的excel文件路径----------");
				strRet = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
						+ request.getContextPath() + excelHandle.getExportExcelPath();
				// System.out.println(strRet);
			} else {
				System.out.println("导出失败！");
			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "导出失败。" + e.getMessage();
		}

		return strRet;

	}
	
	
	/**
	 * 导出面试考生评议数据
	 * 
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	private String tzExportExcelFile(String strParams, String[] errorMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			System.out.println("strParams="+strParams);
			jacksonUtil.json2Map(strParams);

			// 班级ID
			String classId = "bzcjdc";
			String batchId = "bzcjdc";
			String fileName = jacksonUtil.getString("fileName");
			

			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			String downloadPath = getSysHardCodeVal.getDownloadPath();

			// excel存储路径
			String eventExcelPath = "/material/xlsx";

			// 完整的存储路径
			String expDirPath = downloadPath + eventExcelPath + "/" + getDateNow();
			String absexpDirPath = request.getServletContext().getRealPath(expDirPath);

			/* 生成运行控制ID */
			SimpleDateFormat dateFormate = new SimpleDateFormat("yyyyMMddHHmmss");
			String s_dt = dateFormate.format(new Date());
			String runCntlId = "BZCJDC" + s_dt + "_" + GetSeqNum.getSeqNum("TZ_REVIEW_CL_COM", "CLPSKS_EXPORT");

			// 与自动初筛导出共用参数表
			PsTzZdcsDcAet psTzZdcsDcAet = new PsTzZdcsDcAet();
			psTzZdcsDcAet.setRunCntlId(runCntlId);
			psTzZdcsDcAet.setTzClassId(classId);
			psTzZdcsDcAet.setTzBatchId(batchId);
			psTzZdcsDcAet.setTzRelUrl(expDirPath);
			psTzZdcsDcAet.setTzJdUrl(absexpDirPath);
			psTzZdcsDcAet.setTzParamsStr(strParams);
			psTzZdcsDcAetMapper.insert(psTzZdcsDcAet);

			String currentAccountId = tzLoginServiceImpl.getLoginedManagerDlzhid(request);
			String currentOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);

			BaseEngine tmpEngine = TzGDObject.createEngineProcess(currentOrgId, "TZ_BZCJXS_EXP_PROC");
			// 指定调度作业的相关参数
			EngineParameters schdProcessParameters = new EngineParameters();

			schdProcessParameters.setBatchServer("");
			schdProcessParameters.setCycleExpression("");
			schdProcessParameters.setLoginUserAccount(currentAccountId);
			schdProcessParameters.setPlanExcuteDateTime(new Date());
			schdProcessParameters.setRunControlId(runCntlId);

			// 调度作业
			tmpEngine.schedule(schdProcessParameters);

			// 进程实例id;
			int processinstance = tmpEngine.getProcessInstanceID();

			PsTzExcelDrxxT psTzExcelDrxxT = new PsTzExcelDrxxT();
			psTzExcelDrxxT.setProcessinstance(processinstance);
			psTzExcelDrxxT.setTzComId("TZ_BZCJ_SRC_COM");
			psTzExcelDrxxT.setTzPageId("TZ_BZCJ_JIS_STD");
			// 存放班级ID-批次ID
			psTzExcelDrxxT.setTzDrLxbh(classId + "-" + batchId);
			psTzExcelDrxxT.setTzDrTaskDesc(fileName);
			psTzExcelDrxxT.setTzStartDtt(new Date());
			psTzExcelDrxxT.setOprid(oprid);
			psTzExcelDrxxT.setTzIsViewAtt("Y");
			psTzExcelDrxxTMapper.insert(psTzExcelDrxxT);

			// 生成本次导出的文件名
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			Random random = new Random();
			int max = 999999999;
			int min = 100000000;
			String sysFileName = simpleDateFormat.format(new Date()) + "_" + oprid.toUpperCase() + "_"
					+ String.valueOf(random.nextInt(max) % (max - min + 1) + min) + ".xlsx";

			PsTzExcelDattT psTzExcelDattT = new PsTzExcelDattT();
			psTzExcelDattT.setProcessinstance(processinstance);
			psTzExcelDattT.setTzSysfileName(sysFileName);
			psTzExcelDattT.setTzFileName(fileName);
			psTzExcelDattT.setTzCfLj("A");
			psTzExcelDattT.setTzFjRecName("");
			psTzExcelDattT.setTzFwqFwlj("");
			psTzExcelDattTMapper.insert(psTzExcelDattT);

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "导出失败。" + e.getMessage();
		}

		return strRet;
	}

	// 根据生日计算年纪
	public int getAge(Date dateOfBirth) {
		int age = 0;
		Calendar born = Calendar.getInstance();
		Calendar now = Calendar.getInstance();
		if (dateOfBirth != null) {
			now.setTime(new Date());
			born.setTime(dateOfBirth);
			if (born.after(now)) {
				throw new IllegalArgumentException("年龄不能超过当前日期");
			}
			age = now.get(Calendar.YEAR) - born.get(Calendar.YEAR);
			int nowDayOfYear = now.get(Calendar.DAY_OF_YEAR);
			int bornDayOfYear = born.get(Calendar.DAY_OF_YEAR);
			// System.out.println("nowDayOfYear:" + nowDayOfYear + "
			// bornDayOfYear:" + bornDayOfYear);
			if (nowDayOfYear < bornDayOfYear) {
				age -= 1;
			}
		}
		return age;
	}

	/**
	 * 返回评委最大提示数
	 * 
	 * @param actData
	 * @param errorMsg
	 * @return
	 */
	public String maxjudnum(String[] actData, String[] errorMsg) {
		String restr = "";
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {

			int num = 0;
			String xmid = "";
			String xmName = "";

			int[] bb = null;
			int i = 0;
			int i_two = 0;
			String[] xmlist = new String[actData.length];
			for (num = 0; num < actData.length; num++) {
				// 提交信息
				String strForm = actData[num];
				// System.out.println(strForm);
				jacksonUtil.json2Map(strForm);
				// 主题 ID;

				xmid = jacksonUtil.getString("xmid");
				xmName = jacksonUtil.getString("xmName");
				xmlist[num] = xmid + xmName;

			}
			Map<String, Integer> map_pw = new HashMap<String, Integer>();
			String key_value = "";
			Integer count = 0;
			for (i = 0; i < xmlist.length; i++) {
				key_value = xmlist[i];
				count = map_pw.get(key_value);
				if (count == null) {
					map_pw.put(key_value, 1);

				} else {
					map_pw.put(key_value, count + 1);
				}

			}
			bb = new int[map_pw.size()];
			int index = 0;
			for (Entry<String, Integer> entry : map_pw.entrySet()) {
				bb[index] = entry.getValue();
				index++;
				// System.out.println("key= " + entry.getKey() + " and value= "
				// + entry.getValue());
			}
			i_two = bb[0];
			for (int j = 1; j < bb.length; j++) {
				if (bb[j] > i_two) {
					i_two = bb[j];
				}
			}
			restr = String.valueOf(i_two);
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
			// TODO: handle exception
		}

		return restr;
	}
	/**
	 * 创建日期目录名
	 * 
	 * @return
	 */
	private String getDateNow() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(1);
		int month = cal.get(2) + 1;
		int day = cal.get(5);
		return (new StringBuilder()).append(year).append(month).append(day).toString();
	}
	

}
