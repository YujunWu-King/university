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

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.Calendar.DateUtil;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.poi.excel.ExcelHandle;
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
				sunfloat = (float) 0.00;
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
				mapList.put("judge_sunrank", this.checksunRank(sunscorelist, sunfloat));
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
				System.out.println("第" + num + 1 + ":" + xmid);
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

			if (jacksonUtil.containsKey("add")) {
				jsonArray = (List<Map<String, Object>>) jacksonUtil.getList("add");
				// System.out.println(jacksonUtil.Map2json(jsonArray));
				if (jsonArray != null && jsonArray.size() > 0) {
					actData = new String[jsonArray.size()];
					for (num1 = 0; num1 < jsonArray.size(); num1++) {
						actData[num1] = jacksonUtil.Map2json(jsonArray.get(num1));
						System.out.println(actData[num1]);
					}

				}
			}
			String coulumdt = jacksonUtil.containsKey("coulumdt") ? jacksonUtil.getString("coulumdt") : null;

			switch (strType) {
			case "EXPORT":
				String filepath = this.exportstudeninfo(actData, Integer.valueOf(coulumdt), errorMsg);

				mapRet.put("fileUrl", filepath);

				strRet = jacksonUtil.Map2json(mapRet);

				break;
			case "MAXCOULUM":
				String max_jud = this.maxjudnum(actData, errorMsg);
				System.out.println(max_jud);
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

	/**
	 * 导出面试学生信息
	 * 
	 * @param actData
	 * @param errorMsg
	 * @return
	 */
	public String exportstudeninfo(String[] actData, Integer coulumdt, String[] errorMsg) {

		String strRet = "";

		try {

			String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

			// 获取文件存储路径
			String fileBasePath = getSysHardCodeVal.getDownloadPath();
			DateUtil dateUtil = new DateUtil();

			// 活动报名信息excel存储路径
			String eventExcelPath = "/events/xlsx";

			// 完整的存储路径
			String fileDirPath = fileBasePath + eventExcelPath;
			String strForm = "";
			String xmid = "";
			String teamID = "";
			String sunrank = "";
			String sunscore = "";
			String clpszf = "";
			String[] rowList = null;
			String[] pw_rowList = null;
			String[] pw_row = null;
			String[] daterow = null;
			int daterow_i = 0;
			String bkzysql = "";
			String[] bkzyArray = null;
			int bkzyArray_i = 0;
			Map<String, Object> bkzylist = new HashMap<String, Object>();

			int m = 0;
			int pwsocre_i = 0;
			String judge_id = "";
			String judgeid = "";
			JacksonUtil jacksonUtil = new JacksonUtil();

			jacksonUtil.json2Map(actData[0]);
			xmid = jacksonUtil.getString("xmid");
			teamID = jacksonUtil.getString("teamID");

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
			dataCellKeys.add(new String[] { "csrq", "出身日期" });
			dataCellKeys.add(new String[] { "drxsln", "到入学时年龄" });

			// 在数据库中查询 描述 根据 循环生成 表头
			List<Map<String, Object>> treename = jdbcTemplate.queryForList(
					TzGDObject.getSQLText("SQL.TZBzScoreMathBundle.TZ_TREE_EXCEL_HEADER"),
					new Object[] { Integer.valueOf(xmid), orgid });

			for (int treename_i = 0; treename_i < treename.size(); treename_i++) {
				Map<String, Object> resultMap = treename.get(treename_i);
				rowList = new String[2];
				int j = 0;
				for (Object value : resultMap.values()) {
					rowList[j] = value.toString();
					j++;
				}
				list.add(rowList);
			}
			for (int n = 0; n < coulumdt; n++) {
				dataCellKeys.add(new String[] { "judg_" + (n + 1) + "_id", "评委" + (n + 1) + "账号" });
				dataCellKeys.add(new String[] { "judg_" + (n + 1) + "_zscore", "评委" + (n + 1) + "标准总分" });
				dataCellKeys.add(new String[] { "judg_" + (n + 1) + "_num", "评委" + (n + 1) + "评审人数" });
				dataCellKeys.add(new String[] { "judg_" + (n + 1) + "_rank", "评委" + (n + 1) + "考生评审排名" });

				for (int i = 0; i < list.size(); i++) {
					String[] row = list.get(i);
					dataCellKeys.add(new String[] { "num" + (n + 1) + row[0], "评委" + (n + 1) + row[1] });

				}

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

				clpszf = jdbcTemplate.queryForObject(TzGDObject.getSQLText("SQL.TZBzScoreMathBundle.TzGetClpSumScore"),
						new Object[] { xmid }, "String");
				if (clpszf != null) {
					sunscorelist[num_1] = 9999999 - Float.valueOf(sunrank) * 100000 + Float.valueOf(clpszf);
				} else {
					sunscorelist[num_1] = 9999999 - Float.valueOf(sunrank) * 100000;
				}

			}

			for (int num = 0; num < actData.length; num++) {
				// 表单内容
				Map<String, Object> mapData = new HashMap<String, Object>();
				pw_list = new ArrayList<String[]>();
				strForm = actData[num];
				// 解析 json
				jacksonUtil.json2Map(strForm);
				xmid = jacksonUtil.getString("xmid");
				System.out.println("这边的" + xmid);
				teamID = jacksonUtil.getString("teamID");
				sunrank = jacksonUtil.getString("judge_sunrank");
				sunscore = jacksonUtil.getString("judge_sunscore");
				mapData.put("mssqid", xmid);
				mapData.put("msz", teamID);
				mapData.put("zlpm", sunrank);
				mapData.put("bzzf", sunscore);

				// 查出报名志愿
				bkzysql = "SELECT TZ_CLASS_ID,TZ_BATCH_ID FROM PS_TZ_FORM_WRK_T WHERE TZ_APP_INS_ID=? ";
				bkzylist = jdbcTemplate.queryForMap(bkzysql, new Object[] { xmid });
				bkzyArray = new String[2];
				bkzyArray_i = 0;
				for (Entry<String, Object> entry : bkzylist.entrySet()) {

					bkzyArray[bkzyArray_i] = entry.getValue() == null ? null : entry.getValue().toString();

					bkzyArray_i++;

				}

				mapData.put("bkzy", bkzyArray[0] + bkzyArray[1]);

				clpszf = jdbcTemplate.queryForObject(TzGDObject.getSQLText("SQL.TZBzScoreMathBundle.TzGetClpSumScore"),
						new Object[] { xmid }, "String");

				if (clpszf != null) {
					mapData.put("zpm", this.checksunRank(sunscorelist,
							9999999 - Float.valueOf(sunrank) * 100000 + Float.valueOf(clpszf)));
				} else {
					mapData.put("zpm", this.checksunRank(sunscorelist, 9999999 - Float.valueOf(sunrank) * 100000));
				}

				/*
				 * mapData.put("zpm", this.checksunRank(sunscorelist, 9999999 -
				 * Float.valueOf(sunrank) * 100000 + Float.valueOf(clpszf)));
				 */
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

					judgeid = jdbcTemplate.queryForObject("SELECT OPRID FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_DLZH_ID=? ",
							new Object[] { judge_id }, "String");

					List<Map<String, Object>> pwsocre = jdbcTemplate.queryForList(
							TzGDObject.getSQLText("SQL.TZBzScoreMathBundle.TzGetpwoneScore"),
							new Object[] { xmid, judgeid });

					for (pwsocre_i = 0; pwsocre_i < pwsocre.size(); pwsocre_i++) {

						Map<String, Object> pwscoreMap = pwsocre.get(pwsocre_i);

						pw_rowList = new String[13];
						int j = 0;
						for (Object value : pwscoreMap.values()) {
							pw_rowList[j] = value.toString();
							j++;
						}
						pw_list.add(pw_rowList);
					}

					for (int i = 0; i < pw_list.size(); i++) {
						pw_row = pw_list.get(i);
						mapData.put("num" + (m + 1) + pw_row[1], pw_row[0]);

					}

				}

				try {
					Map<String, Object> studentInfo = jdbcTemplate.queryForMap(
							TzGDObject.getSQLText("SQL.TZBzScoreMathBundle.TzGetStudentInfo"), new Object[] { xmid });

					daterow_i = 0;
					daterow = new String[9];
					for (Entry<String, Object> entry : studentInfo.entrySet()) {

						daterow[daterow_i] = entry.getValue() == null ? null : entry.getValue().toString();

						daterow_i++;

					}

					mapData.put("sex", daterow[0].equals("M") ? "男" : "女");
					// System.out.println("性别：" + (daterow[0].equals("M") ? "男"
					// : "女"));

					// System.out.println(daterow[0]);
					mapData.put("name", daterow[1]);

					// System.out.println(daterow[1]);
					mapData.put("zjid", daterow[2]);

					// System.out.println(daterow[2]);
					mapData.put("bkzkyx", daterow[3]);

					mapData.put("csrq", daterow[4]);
					// System.out.println("出生日期：" + daterow[4]);
					mapData.put("drxsln", daterow[4] == null ? null : this.getAge(DateUtil.parse(daterow[4])));

					mapData.put("zgxl", daterow[5]);
					mapData.put("gzdw", daterow[6]);
					mapData.put("gzzw", daterow[7]);
					mapData.put("gzbm", daterow[8]);

				} catch (Exception e) {
					e.printStackTrace();
					errorMsg[0] = "1";
					errorMsg[1] = "导出失败。" + e.getMessage();
					// TODO: handle exception
				}

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
			System.out.println("nowDayOfYear:" + nowDayOfYear + " bornDayOfYear:" + bornDayOfYear);
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
				System.out.println(strForm);
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
				System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
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

}
