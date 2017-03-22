package com.tranzvision.gd.TZMbaPwClpsBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZMbaPwClpsBundle.dao.PsTzMsPsksTblMapper;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzMsPsksTbl;
import com.tranzvision.gd.util.Calendar.DateUtil;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.poi.excel.ExcelHandle;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/****
 * MBA材料面试评审-面试规则-考生查看
 * 
 * @author tzhjl
 * @since 2017-3-14
 */
@Service("com.tranzvision.gd.TZMbaPwClpsBundle.service.impl.TzReviewMsExamServiceImpl")
public class TzReviewMsExamServiceImpl extends FrameworkImpl {
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private PsTzMsPsksTblMapper psTzMsPsksTblMapper;
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	@Autowired
	private TZGDObject TzGDObject;

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
		try {
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] { { "TZ_APP_INS_ID", "ASC" } };

			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_CLASS_ID", "TZ_APPLY_PC_ID", "TZ_APP_INS_ID", "TZ_MSPS_PWJ_PC",
					"TZ_LUQU_ZT", "OPRID", "TZ_REALNAME", "TZ_GENDER", "TZ_MSH_ID" };

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, comParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("classId", rowList[0]);
					mapList.put("batchId", rowList[1]);
					mapList.put("appInsId", rowList[2]);
					mapList.put("ksOprId", rowList[3]);
					mapList.put("passState", rowList[4]);
					mapList.put("ksOprId", rowList[5]);
					mapList.put("ksName", rowList[6]);
					mapList.put("gender", rowList[7]);
					mapList.put("mshId", rowList[8]);
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
	public String tzDelete(String[] actData, String[] errMsg) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		Date nowdate = new Date();
		Long appinsId = (long) 0;
		String ksName = "";
		int count = 0;
		try {
			String Oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			jacksonUtil.json2Map(actData[0]);
			String classId = jacksonUtil.getString("classId");
			jacksonUtil.json2Map(actData[1]);
			String batchId = jacksonUtil.getString("batchId");
			for (int i = 2; i < actData.length; i++) {
				// 表单内容
				String strForm = actData[i];
				// 解析 json
				jacksonUtil.json2Map(strForm);
				appinsId = Long.valueOf(jacksonUtil.getString("appInsId"));
				ksName = jacksonUtil.getString("ksName");
				String sql = "SELECT COUNT(1) from PS_TZ_MSPS_KSH_TBL where TZ_CLASS_ID =? and TZ_APPLY_PC_ID =? and TZ_APP_INS_ID=?";
				count = sqlQuery.queryForObject(sql, new Object[] { classId, batchId, appinsId }, "Integer");
				if (count > 0) {
					PsTzMsPsksTbl psTzMsPsksTbl = new PsTzMsPsksTbl();
					psTzMsPsksTbl.setTzClassId(classId);
					psTzMsPsksTbl.setTzApplyPcId(batchId);
					psTzMsPsksTbl.setTzAppInsId(appinsId);

					psTzMsPsksTblMapper.deleteByPrimaryKey(psTzMsPsksTbl);

				} else {

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();

			// TODO: handle exception
		}

		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		Date nowdate = new Date();
		Long appinsId = (long) 0;
		String ksName = "";
		String passState = "";
		int count = 0;
		try {
			String Oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			jacksonUtil.json2Map(actData[0]);
			String classId = jacksonUtil.getString("classId");
			jacksonUtil.json2Map(actData[1]);
			String batchId = jacksonUtil.getString("batchId");
			for (int i = 2; i < actData.length; i++) {
				// 表单内容
				String strForm = actData[i];
				// 解析 json
				jacksonUtil.json2Map(strForm);
				appinsId = Long.valueOf(jacksonUtil.getString("appInsId"));
				ksName = jacksonUtil.getString("ksName");
				passState = jacksonUtil.getString("passState");
				String sql = "SELECT COUNT(1) from PS_TZ_MSPS_KSH_TBL where TZ_CLASS_ID =? and TZ_APPLY_PC_ID =? and TZ_APP_INS_ID=?";
				count = sqlQuery.queryForObject(sql, new Object[] { classId, batchId, appinsId }, "Integer");
				if (count > 0) {
					PsTzMsPsksTbl psTzMsPsksTbl = new PsTzMsPsksTbl();
					psTzMsPsksTbl.setTzClassId(classId);
					psTzMsPsksTbl.setTzApplyPcId(batchId);
					psTzMsPsksTbl.setTzAppInsId(appinsId);
					psTzMsPsksTbl.setTzLuquZt(passState);
					psTzMsPsksTbl.setRowLastmantOprid(Oprid);
					psTzMsPsksTbl.setRowLastmantDttm(nowdate);

					psTzMsPsksTblMapper.updateByPrimaryKeySelective(psTzMsPsksTbl);

				} else {

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();

			// TODO: handle exception
		}

		// TODO Auto-generated method stub
		return null;
	}

	/***
	 * 导出选中的考生信息
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

			if (jacksonUtil.containsKey("export")) {
				jsonArray = (List<Map<String, Object>>) jacksonUtil.getList("export");
				// System.out.println(jacksonUtil.Map2json(jsonArray));
				if (jsonArray != null && jsonArray.size() > 0) {
					actData = new String[jsonArray.size()];
					for (num1 = 0; num1 < jsonArray.size(); num1++) {
						actData[num1] = jacksonUtil.Map2json(jsonArray.get(num1));
						System.out.println(actData[num1]);
					}

				}
			}
			String classId = jacksonUtil.containsKey("classId") ? jacksonUtil.getString("classId") : null;
			String batchId = jacksonUtil.containsKey("batchId") ? jacksonUtil.getString("batchId") : null;

			switch (strType) {
			case "EXPORT":
				String filepath = this.exportstudeninfo(actData, classId, batchId, errorMsg);

				mapRet.put("fileUrl", filepath);

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

	/**
	 * 导出面试学生信息
	 * 
	 * @param actData
	 * @param errorMsg
	 * @return
	 */
	public String exportstudeninfo(String[] actData, String classId, String batchId, String[] errorMsg) {

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
			int judgenum = 0;
			String[] rowList = null;
			String[] pw_rowList = null;
			String[] pw_row = null;
			String[] daterow = null;
			int daterow_i = 0;
			String bkzysql = "";
			String[] bkzyArray = null;

			// 评委面试人数
			String groupNumsql = "";
			int groupNum = 0;

			int bkzyArray_i = 0;
			Map<String, Object> bkzylist = new HashMap<String, Object>();

			int m = 0;
			int pwsocre_i = 0;
			String judge_id = "";
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
			// dataCellKeys.add(new String[] { "zpm", "总排名" });
			dataCellKeys.add(new String[] { "clpsyszf", "材料评审原始总分" });
			dataCellKeys.add(new String[] { "csrq", "出身日期" });
			dataCellKeys.add(new String[] { "drxsln", "年龄" });

			// 在数据库中查询 描述 根据 循环生成 表头
			List<Map<String, Object>> treename = sqlQuery.queryForList(
					TzGDObject.getSQLText("SQL.TZBzScoreMathBundle.TZ_TREE_EXCEL_HEADER"), new Object[] { xmid });

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
			// 得到每个学生做多几个评委 设置表头
			String mspwnumSQL = "SELECT TZ_MSPY_NUM FROM PS_TZ_MSPS_GZ_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
			System.out.println("classId:" + classId + "batchId:" + batchId);
			int mspwnum = sqlQuery.queryForObject(mspwnumSQL, new Object[] { classId, batchId }, "Integer");
			for (int n = 0; n < mspwnum; n++) {
				dataCellKeys.add(new String[] { "judg_" + (n + 1) + "_id", "评委" + (n + 1) + "账号" });
				dataCellKeys.add(new String[] { "judg_" + (n + 1) + "_group", "评委" + (n + 1) + "所属组" });

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

			for (int num = 0; num < actData.length; num++) {
				// 表单内容
				Map<String, Object> mapData = new HashMap<String, Object>();
				pw_list = new ArrayList<String[]>();
				strForm = actData[num];
				// 解析 json
				jacksonUtil.json2Map(strForm);
				xmid = jacksonUtil.getString("appInsId");
				System.out.println("这边的" + xmid);

				mapData.put("mssqid", xmid);

				// 查出报名志愿

				mapData.put("bkzy", classId + batchId);

				clpszf = sqlQuery.queryForObject(TzGDObject.getSQLText("SQL.TZBzScoreMathBundle.TzGetClpSumScore"),
						new Object[] { xmid }, "String");

				/*
				 * mapData.put("zpm", this.checksunRank(sunscorelist, 9999999 -
				 * Float.valueOf(sunrank) * 100000 + Float.valueOf(clpszf)));
				 */
				mapData.put("clpsyszf", clpszf);

				List<Map<String, Object>> pwsteam = sqlQuery.queryForList(
						TzGDObject.getSQLText("SQL.TZMbaPwClps.TZ_KSMSPS_JUGZHU"),
						new Object[] { batchId, xmid, classId });
				// 判断考生的评委数和设置的评委数的大小
				// 若大于设置的就去设置的大小
				// 若小于则取pwsteam.size();
				if (pwsteam.size() > mspwnum) {
					judgenum = mspwnum;

				} else {
					judgenum = pwsteam.size();
				}

				for (m = 0; m < judgenum; m++) {
					Map<String, Object> pwsteamMap = pwsteam.get(m);

					// 评委ID
					judge_id = pwsteamMap.get("TZ_PWEI_OPRID") == null ? ""
							: pwsteamMap.get("TZ_PWEI_OPRID").toString();
					mapData.put("judg_" + (m + 1) + "_id", judge_id);
					// 评委组名称
					mapData.put("judg_" + (m + 1) + "_group", pwsteamMap.get("TZ_CLPS_GR_NAME") == null ? ""
							: pwsteamMap.get("TZ_CLPS_GR_NAME").toString());
					// 评委审批人数
					if (!judge_id.equals("")) {
						groupNumsql = "SELECT COUNT(1) FROM PS_TZ_MP_PW_KS_TBL  WHERE TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? and TZ_PWEI_OPRID=?";
						groupNum = sqlQuery.queryForObject(groupNumsql, new Object[] { classId, batchId, judge_id },
								"Integer");
						mapData.put("judg_" + (m + 1) + "_num", groupNum);
					}
					// 评审排名
					mapData.put("judg_" + (m + 1) + "_rank",
							pwsteamMap.get("TZ_KSH_PSPM") == null ? "" : pwsteamMap.get("TZ_KSH_PSPM").toString());

					// 根据报名表id和品味id 循环得到考生 成绩项得分数
					List<Map<String, Object>> pwsocre = sqlQuery.queryForList(
							TzGDObject.getSQLText("SQL.TZBzScoreMathBundle.TzGetpwoneScore"),
							new Object[] { xmid, judge_id });

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
					Map<String, Object> studentInfo = sqlQuery.queryForMap(
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

}
