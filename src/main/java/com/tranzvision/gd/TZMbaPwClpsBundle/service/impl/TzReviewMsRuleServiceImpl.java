
package com.tranzvision.gd.TZMbaPwClpsBundle.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZMbaPwClpsBundle.dao.PsTzMsPsGzTblMapper;
import com.tranzvision.gd.TZMbaPwClpsBundle.dao.PsTzPwExtTblMapper;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzMsPsGzTbl;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzPwExtTbl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.poi.excel.ExcelHandle;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * MBA材料面试评审-面试规则-面试规则设置
 * 
 * @author tzhjl
 * @since 2017-3-20
 */

@Service("com.tranzvision.gd.TZMbaPwClpsBundle.service.impl.TzReviewMsRuleServiceImpl")
public class TzReviewMsRuleServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	@Autowired
	private PsTzPwExtTblMapper psTzPwExtTblMapper;
	@Autowired
	private PsTzMsPsGzTblMapper psTzMsPsGzTblMapper;
	@Autowired
	private TZGDObject tzSQLObject;

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
				String clpsksNum = mapBasic.get("TZ_CLPS_KS_NUM") == null ? ""
						: String.valueOf(mapBasic.get("TZ_CLPS_KS_NUM"));
				String mspsksNum = mapBasic.get("TZ_MSPS_KS_NUM") == null ? ""
						: String.valueOf(mapBasic.get("TZ_MSPS_KS_NUM"));
				String judgeNumSet = mapBasic.get("TZ_MSPY_NUM") == null ? ""
						: String.valueOf(mapBasic.get("TZ_MSPY_NUM"));

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

				String count[] = this.StudentJugeNum(classId, batchId, errMsg);

				mapData.put("classId", classId);
				mapData.put("batchId", batchId);
				mapData.put("className", className);
				mapData.put("batchName", batchName);
				mapData.put("ksNum", bkksNum);
				mapData.put("reviewClpsKsNum", clpsksNum);
				mapData.put("reviewKsNum", mspsksNum);
				mapData.put("dqpsStatus", dqpsStatus);
				mapData.put("desc", dqpsStatusDesc);
				mapData.put("StartDate", strStartDate);
				mapData.put("StartTime", strStartTime);
				mapData.put("EndDate", strEndDate);
				mapData.put("EndTime", strEndTime);
				mapData.put("desc", materialDesc);
				mapData.put("judgeNumSet", judgeNumSet);
				mapData.put("kspwnum", count[0]);
				mapData.put("pwTeamnum", count[1]);

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

	// 面试规则 其他操作
	@Override
	public String tzOther(String oprType, String strParams, String[] errorMsg) {
		Map<String, Object> returMap = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {

			switch (oprType) {
			case "JUDGROUPALL":
				String alljudgidandname = this.judggroupteamall(errorMsg);
				returMap.put("judggroup", alljudgidandname);
				break;

			// 面试评委页面 评委组
			case "JUDGROUP":
				jacksonUtil.json2Map(strParams);
				String classId = jacksonUtil.getString("classId");
				// System.out.println("classId:" + classId);

				String batchId = jacksonUtil.getString("batchId");
				// System.out.println("batchId:" + batchId);

				String judgidandname = this.judggroupteam(classId, batchId, errorMsg);
				returMap.put("judggroup", judgidandname);

				break;
			// 面试评委信息导出
			case "EXPORT":
				String fileUrl = this.exportpwinform(strParams, errorMsg);
				returMap.put("fileUrl", fileUrl);
				break;
			// 评委重置密码
			case "RESETPWD":
				this.ChangPassWord(strParams, errorMsg);

				break;
			case "NUMCOUNT":
				jacksonUtil.json2Map(strParams);
				String classID = jacksonUtil.getString("classId");
				// System.out.println("classId:" + classID);

				String batchID = jacksonUtil.getString("batchId");
				// System.out.println("batchId:" + batchID);

				String count[] = this.StudentJugeNum(classID, batchID, errorMsg);
				returMap.put("kspwnum", count[0]);
				returMap.put("pwTeamnum", count[1]);

			default:

			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
			// TODO: handle exception
		}
		return jacksonUtil.Map2json(returMap);
	}

	public String[] StudentJugeNum(String classID, String batchID, String[] errorMsg) {
		String[] reStr = new String[2];

		try {
			String sql = "SELECT TZ_MSPY_NUM,TZ_GRP_COUNT FROM PS_TZ_MSPS_GZ_TBL WHERE  TZ_CLASS_ID=? AND  TZ_APPLY_PC_ID=?";
			Map<String, Object> map = sqlQuery.queryForMap(sql, new Object[] { classID, batchID });
			/* System.out.println("map.get:" + map.get("TZ_MSPY_NUM")); */
			if (map != null) {

				reStr[0] = map.get("TZ_MSPY_NUM") == null ? "" : map.get("TZ_MSPY_NUM").toString();
				reStr[1] = map.get("TZ_GRP_COUNT") == null ? "" : map.get("TZ_GRP_COUNT").toString();

			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
			// TODO: handle exception
		}
		return reStr;

	}

	// 所有的
	public String judggroupteamall(String[] errorMsg) {
		String RetrnStr = "";
		String Strjudegid = "";
		String Strjudename = "";
		try {
			String Orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);

			String sql = "SELECT TZ_CLPS_GR_ID,TZ_CLPS_GR_NAME FROM PS_TZ_MSPS_GR_TBL WHERE TZ_JG_ID=? ";
			List<Map<String, Object>> listMap = sqlQuery.queryForList(sql, new Object[] { Orgid });
			for (Map<String, Object> map : listMap) {
				if (Strjudegid.equals("") && Strjudename.equals("")) {
					Strjudegid = map.get("TZ_CLPS_GR_ID").toString();
					Strjudename = map.get("TZ_CLPS_GR_NAME").toString();
				} else {
					Strjudegid = Strjudegid + "," + map.get("TZ_CLPS_GR_ID").toString();
					Strjudename = Strjudename + "," + map.get("TZ_CLPS_GR_NAME").toString();

				}

			}
			RetrnStr = Strjudegid + "|" + Strjudename;

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
			// TODO: handle exception
		}
		return RetrnStr;

	}

	public String judggroupteam(String classId, String batchId, String[] errorMsg) {
		String RetrnStr = "";
		String Strjudegid = "";
		String Strjudename = "";
		int count = 0;
		try {
			String Orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			String teamsql = "SELECT TZ_GRP_COUNT FROM PS_TZ_MSPS_GZ_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? ";

			String count1 = sqlQuery.queryForObject(teamsql, new Object[] { classId, batchId }, "String");
			if (count1 == null) {
				count = 999999999;
			} else {
				count = Integer.valueOf(count1);

			}

			String sql = "SELECT TZ_CLPS_GR_ID,TZ_CLPS_GR_NAME FROM PS_TZ_MSPS_GR_TBL WHERE TZ_JG_ID=? ORDER BY  CAST(TZ_CLPS_GR_ID AS UNSIGNED INTEGER) ASC  LIMIT ? ";
			List<Map<String, Object>> listMap = sqlQuery.queryForList(sql, new Object[] { Orgid, count });
			for (Map<String, Object> map : listMap) {
				if (Strjudegid.equals("") && Strjudename.equals("")) {
					Strjudegid = map.get("TZ_CLPS_GR_ID").toString();
					Strjudename = map.get("TZ_CLPS_GR_NAME").toString();
				} else {
					Strjudegid = Strjudegid + "," + map.get("TZ_CLPS_GR_ID").toString();
					Strjudename = Strjudename + "," + map.get("TZ_CLPS_GR_NAME").toString();

				}

			}
			RetrnStr = Strjudegid + "|" + Strjudename;

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
			// TODO: handle exception
		}

		return RetrnStr;
	}

	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		String Strjudename = "";
		String revistus = "";

		try {
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] { { "TZ_CLASS_ID", "ASC" } };

			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_CLASS_ID", "TZ_APPLY_PC_ID", "TZ_PWEI_OPRID", "TZ_PWEI_GRPID",
					"TZ_REALNAME", "TZ_DLZH_ID" };

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, comParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					Strjudename = "";
					revistus = "";
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("judgId", rowList[2]);
					mapList.put("judgGroupId", rowList[3]);
					mapList.put("judgName", rowList[4]);
					mapList.put("judzhxx", rowList[5]);

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

	@SuppressWarnings("unchecked")
	public String exportpwinform(String strParams, String[] errorMsg) {
		String RetrnStr = "";
		String strForm = "";
		String judgId = "";
		String judgGroupId = "";
		String judgName = "";
		String pwgroup = "";
		String pwpwd = "";
		String pwgroupsql = "SELECT TZ_CLPS_GR_NAME FROM  PS_TZ_MSPS_GR_TBL WHERE TZ_CLPS_GR_ID=?";

		String pwpwdsql = "SELECT TZ_CS_PASSWORD FROM PS_TZ_PW_EXT_T  WHERE OPRID=? AND TZ_JG_ID=?";
		// String

		try {
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			// 获取文件存储路径
			String fileBasePath = getSysHardCodeVal.getDownloadPath();
			// 评委信息excel存储路径
			String eventExcelPath = "/judgInf/xlsx";
			List<Map<String, Object>> jsonArray = null;

			// 完整的存储路径
			String fileDirPath = fileBasePath + eventExcelPath;
			JacksonUtil jacksonUtil = new JacksonUtil();
			String[] actData = null;

			// 设置表头
			List<String[]> dataCellKeys = new ArrayList<String[]>();

			dataCellKeys.add(new String[] { "pwoprid", "评委账号" });
			dataCellKeys.add(new String[] { "pwname", "评委姓名" });
			dataCellKeys.add(new String[] { "pwgroup", "评委组" });
			dataCellKeys.add(new String[] { "pwpassword", "评委初始密码" });

			// 数据
			List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();

			// 解析前台返回的Json 成数组
			jacksonUtil.json2Map(strParams);
			if (jacksonUtil.containsKey("export")) {
				jsonArray = (List<Map<String, Object>>) jacksonUtil.getList("export");
				if (jsonArray != null && jsonArray.size() > 0) {
					actData = new String[jsonArray.size()];
					for (int i = 0; i < jsonArray.size(); i++) {
						actData[i] = jacksonUtil.Map2json(jsonArray.get(i));
					}

				}
			}
			for (int i = 0; i < actData.length; i++) {
				// 表单内容
				Map<String, Object> mapData = new HashMap<String, Object>();
				strForm = actData[i];
				jacksonUtil.json2Map(strForm);
				judgId = jacksonUtil.getString("judgId");
				judgName = jacksonUtil.getString("judgName");
				judgGroupId = jacksonUtil.getString("judgGroupId");

				pwgroup = sqlQuery.queryForObject(pwgroupsql, new Object[] { judgGroupId }, "String");

				pwpwd = sqlQuery.queryForObject(pwpwdsql, new Object[] { judgId, orgid }, "String");

				mapData.put("pwoprid", judgId);
				mapData.put("pwname", judgName);
				mapData.put("pwgroup", pwgroup);
				mapData.put("pwpassword", pwpwd);

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
				RetrnStr = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
						+ request.getContextPath() + excelHandle.getExportExcelPath();
				// System.out.println(strRet);
			} else {
				System.out.println("导出失败！");
			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
			// TODO: handle exception
		}

		return RetrnStr;
	}

	@SuppressWarnings("unchecked")
	public String ChangPassWord(String strParams, String[] errorMsg) {
		String strForm = "";
		String judgId = "";
		int count = 0;
		int max = 9999;
		int min = 1000;
		String newpawd = "";

		String oldpwdisY = "SELECT COUNT(1) FROM PS_TZ_PW_EXT_T WHERE TZ_JG_ID=? AND OPRID=?";
		try {
			Random random = new Random();
			String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			List<Map<String, Object>> jsonArray = null;
			JacksonUtil jacksonUtil = new JacksonUtil();
			String[] actData = null;
			// 解析前台返回的Json 成数组
			jacksonUtil.json2Map(strParams);
			if (jacksonUtil.containsKey("export")) {
				jsonArray = (List<Map<String, Object>>) jacksonUtil.getList("export");
				if (jsonArray != null && jsonArray.size() > 0) {
					actData = new String[jsonArray.size()];
					for (int i = 0; i < jsonArray.size(); i++) {
						actData[i] = jacksonUtil.Map2json(jsonArray.get(i));
					}

				}
			}
			for (int i = 0; i < actData.length; i++) {
				// 表单内容
				Map<String, Object> mapData = new HashMap<String, Object>();
				strForm = actData[i];
				jacksonUtil.json2Map(strForm);
				judgId = jacksonUtil.getString("judgId");
				newpawd = String.valueOf(random.nextInt(max) % (max - min + 1) + min);

				PsTzPwExtTbl psTzPwExtTbl = new PsTzPwExtTbl();
				psTzPwExtTbl.setTzJgId(orgid);
				psTzPwExtTbl.setOprid(judgId);
				psTzPwExtTbl.setTzCsPassword(newpawd);

				count = sqlQuery.queryForObject(oldpwdisY, new Object[] { orgid, judgId }, "Integer");
				if (count > 0) {

					psTzPwExtTblMapper.updateByPrimaryKey(psTzPwExtTbl);

				} else {
					psTzPwExtTblMapper.insertSelective(psTzPwExtTbl);

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
			// TODO: handle exception
		}

		return null;

	}

	/**
	 * 修改附加字段信息
	 */
	@Override
	@Transactional
	public String tzAdd(String[] actData, String[] errMsg) {

		String strRet = "{}";
		String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		String oprId = tzLoginServiceImpl.getLoginedManagerOprid(request);
		if (StringUtils.isBlank(orgId)) {
			errMsg[0] = "1";
			errMsg[1] = "您不属于任何机构，不能修改附加字段定义！";
			return strRet;
		}
		System.out.println(actData);

		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int dataLength = actData.length;
			Date dateNow = new Date();
			DateFormat timeFormat = new SimpleDateFormat("HH:mm");
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			for (int num = 0; num < dataLength; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				/* 类型标志 */
				String strFlag = jacksonUtil.getString("typeFlag");
				System.out.println(strFlag);
				/* 评审规则 */
				if (StringUtils.equals("RULE", strFlag)) {
					// 信息内容
					Map<String, Object> infoData = jacksonUtil.getMap("data");

					String classID = infoData.get("classId") == null ? "" : String.valueOf(infoData.get("classId"));
					System.out.println("classID:" + classID);
					String batchID = infoData.get("batchId") == null ? "" : String.valueOf(infoData.get("batchId"));
					String strKSDate = infoData.get("StartDate") == null ? null
							: String.valueOf(infoData.get("StartDate"));
					Date startDate = null;
					if (StringUtils.isNotBlank(strKSDate)) {
						startDate = dateFormat.parse(strKSDate);
					}
					Date startTime = infoData.get("StartTime") == null ? timeFormat.parse("08:30")
							: timeFormat.parse(infoData.get("StartTime").toString());

					String strJSDate = infoData.get("EndDate") == null ? null : String.valueOf(infoData.get("EndDate"));
					Date endDate = null;
					if (StringUtils.isNotBlank(strJSDate)) {
						endDate = dateFormat.parse(strJSDate);
					}
					Date endTime = infoData.get("EndTime") == null ? timeFormat.parse("17:30")
							: timeFormat.parse(infoData.get("EndTime").toString());
					String desc = infoData.get("desc") == null ? "" : String.valueOf(infoData.get("desc"));
					String reviewCount = infoData.get("ksNum") == null ? "0" : String.valueOf(infoData.get("ksNum"));

					String sqlExists = "SELECT 'Y' FROM PS_TZ_MSPS_GZ_TBL WHERE TZ_CLASS_ID = ? AND TZ_APPLY_PC_ID = ?";
					String isExists = sqlQuery.queryForObject(sqlExists, new Object[] { classID, batchID }, "String");

					if (StringUtils.equals("Y", isExists)) {
						PsTzMsPsGzTbl psTzMsPsGzTbl = new PsTzMsPsGzTbl();
						psTzMsPsGzTbl.setTzClassId(classID);
						psTzMsPsGzTbl.setTzApplyPcId(batchID);

						psTzMsPsGzTbl.setTzPyksRq(startDate);
						psTzMsPsGzTbl.setTzPyksSj(startTime);
						psTzMsPsGzTbl.setTzPyjsRq(endDate);
						psTzMsPsGzTbl.setTzPyjsSj(endTime);
						psTzMsPsGzTbl.setTzMspsSm(desc);

						psTzMsPsGzTblMapper.updateByPrimaryKeySelective(psTzMsPsGzTbl);
					} else {
						PsTzMsPsGzTbl psTzMsPsGzTbl = new PsTzMsPsGzTbl();
						psTzMsPsGzTbl.setTzClassId(classID);
						psTzMsPsGzTbl.setTzApplyPcId(batchID);

						psTzMsPsGzTbl.setTzPyksRq(startDate);
						psTzMsPsGzTbl.setTzPyksSj(startTime);
						psTzMsPsGzTbl.setTzPyjsRq(endDate);
						psTzMsPsGzTbl.setTzPyjsSj(endTime);
						psTzMsPsGzTbl.setTzMspsSm(desc);
						psTzMsPsGzTblMapper.insertSelective(psTzMsPsGzTbl);
						;
					}
				}
				/* 每组评委数，组数 */
				if (StringUtils.equals("PWTEAMNUM", strFlag)) {

					// 信息内容
					Map<String, Object> infoData = jacksonUtil.getMap("data");
					String classId = infoData.get("classId") == null ? "" : String.valueOf(infoData.get("classId"));
					// System.out.println("classID:" + classId);
					String batchId = infoData.get("batchId") == null ? "" : String.valueOf(infoData.get("batchId"));
					String kspwnum = infoData.get("kspwnum") == null ? "" : String.valueOf(infoData.get("kspwnum"));
					String pwTeamnum = infoData.get("pwTeamnum") == null ? ""
							: String.valueOf(infoData.get("pwTeamnum"));

					String sqlExists = "SELECT 'Y' FROM PS_TZ_MSPS_GZ_TBL WHERE TZ_CLASS_ID = ? AND TZ_APPLY_PC_ID = ?";
					String isExists = sqlQuery.queryForObject(sqlExists, new Object[] { classId, batchId }, "String");
					if (StringUtils.equals("Y", isExists)) {
						PsTzMsPsGzTbl psTzMsPsGzTbl = new PsTzMsPsGzTbl();
						psTzMsPsGzTbl.setTzClassId(classId);
						psTzMsPsGzTbl.setTzApplyPcId(batchId);
						psTzMsPsGzTbl.setTzGrpCount(Short.valueOf(pwTeamnum));
						psTzMsPsGzTbl.setTzMspyNum(Integer.valueOf(kspwnum));

						psTzMsPsGzTblMapper.updateByPrimaryKeySelective(psTzMsPsGzTbl);
					} else {
						PsTzMsPsGzTbl psTzMsPsGzTbl = new PsTzMsPsGzTbl();
						psTzMsPsGzTbl.setTzClassId(classId);
						psTzMsPsGzTbl.setTzApplyPcId(batchId);

						psTzMsPsGzTbl.setTzGrpCount(Short.valueOf(pwTeamnum));
						psTzMsPsGzTbl.setTzMspyNum(Integer.valueOf(kspwnum));

						psTzMsPsGzTblMapper.insertSelective(psTzMsPsGzTbl);
						;
					}
					/*
					 * rem 将字符串转换成json; Local JavaObject &judgeJson =
					 * &jsonUtil.getJson(&infoData);
					 * 
					 * &strClassID = &judgeJson.getString("classID");
					 * &strBatchID = &judgeJson.getString("batchID"); Local
					 * string &strJudgeID = &judgeJson.getString("judgeID"); rem
					 * 评委oprid; Local string &strOprID; SQLExec(
					 * "SELECT OPRID FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_DLZH_ID=:1 AND TZ_JG_ID=:2"
					 * , &strJudgeID, &orgID, &strOprID); Local boolean
					 * &bolGroupID; Local string &strUpper =
					 * &judgeJson.getString("upper");
					 * 
					 * Local string &strLower = &judgeJson.getString("lower");
					 * try Local string &strGroupID =
					 * &judgeJson.getString("judgeGroup"); &bolGroupID = True;
					 * catch Exception &e7 &bolGroupID = False; end-try; Local
					 * string &strJudgeStatus =
					 * &judgeJson.getString("judgeStatus"); Local Record
					 * &TZ_CLPS_PW_TBL = CreateRecord(Record.TZ_CLPS_PW_TBL);
					 * &TZ_CLPS_PW_TBL.TZ_CLASS_ID.Value = &strClassID;
					 * &TZ_CLPS_PW_TBL.TZ_APPLY_PC_ID.Value = &strBatchID;
					 * &TZ_CLPS_PW_TBL.TZ_PWEI_OPRID.Value = &strOprID; If
					 * All(&strClassID) And All(&strBatchID) And All(&strOprID)
					 * Then If &TZ_CLPS_PW_TBL.SelectByKey() Then
					 * &TZ_CLPS_PW_TBL.TZ_CLASS_ID.Value = &strClassID;
					 * &TZ_CLPS_PW_TBL.TZ_APPLY_PC_ID.Value = &strBatchID;
					 * &TZ_CLPS_PW_TBL.TZ_PWEI_OPRID.Value = &strOprID;
					 * &TZ_CLPS_PW_TBL.TZ_PWZBH.Value = &strGroupID;
					 * &TZ_CLPS_PW_TBL.TZ_PYKS_XX.Value = &strLower;
					 * &TZ_CLPS_PW_TBL.TZ_PYKS_SX.Value = &strUpper;
					 * &TZ_CLPS_PW_TBL.TZ_PWEI_ZHZT.Value = &strJudgeStatus;
					 * &TZ_CLPS_PW_TBL.ROW_LASTMANT_DTTM.Value = %Datetime;
					 * &TZ_CLPS_PW_TBL.ROW_LASTMANT_OPRID.Value = %UserId;
					 * &TZ_CLPS_PW_TBL.Update() Else
					 * &TZ_CLPS_PW_TBL.TZ_CLASS_ID.Value = &strClassID;
					 * &TZ_CLPS_PW_TBL.TZ_APPLY_PC_ID.Value = &strBatchID;
					 * &TZ_CLPS_PW_TBL.TZ_PWEI_OPRID.Value = &strOprID;
					 * &TZ_CLPS_PW_TBL.TZ_PWZBH.Value = &strGroupID;
					 * &TZ_CLPS_PW_TBL.TZ_PYKS_XX.Value = &strLower;
					 * &TZ_CLPS_PW_TBL.TZ_PYKS_SX.Value = &strUpper;
					 * &TZ_CLPS_PW_TBL.TZ_PWEI_ZHZT.Value = &strJudgeStatus;
					 * &TZ_CLPS_PW_TBL.ROW_ADDED_DTTM.Value = %Datetime;
					 * &TZ_CLPS_PW_TBL.ROW_ADDED_OPRID.Value = %UserId;
					 * &TZ_CLPS_PW_TBL.ROW_LASTMANT_DTTM.Value = %Datetime;
					 * &TZ_CLPS_PW_TBL.ROW_LASTMANT_OPRID.Value = %UserId;
					 * &TZ_CLPS_PW_TBL.Insert() End-If; End-If; End-If;
					 */
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			e.printStackTrace();
		}
		return strRet;
	}

}
