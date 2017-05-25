package com.tranzvision.gd.TZMbaPwClpsBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZMbaPwClpsBundle.dao.PsTzMsPsksTblMapper;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzMsPsksTbl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/****
 * MBA材料面试评审-面试规则-添加考生
 * 
 * @author tzhjl
 * @since 2017-3-14
 */
@Service("com.tranzvision.gd.TZMbaPwClpsBundle.service.impl.TzReviewMsExamAddServiceImpl")
public class TzReviewMsExamAddServiceImpl extends FrameworkImpl {

	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private PsTzMsPsksTblMapper psTzMsPsksTblMapper;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private TZGDObject tzSQLObject;

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
			String[][] orderByArr = new String[][] { { "TZ_APP_INS_ID", "ASC" } };

			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_CLASS_ID", "TZ_APPLY_PC_ID", "TZ_APP_INS_ID", "TZ_MSHI_ZGFLG",
					"TZ_CLPS_PWJ_PC", "OPRID", "TZ_REALNAME", "TZ_GENDER", "TZ_MSH_ID", "TZ_CLASS_NAME",
					"TZ_BATCH_NAME" };

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, comParams, numLimit, numStart, errorMsg);

			String sql = "SELECT A.TZ_PWEI_OPRID,A.TZ_PSHEN_ZT,B.TZ_REALNAME FROM PS_TZ_MP_PW_KS_TBL A,PS_TZ_REG_USER_T B WHERE B.OPRID = A.TZ_PWEI_OPRID AND A.TZ_APP_INS_ID=? ";
			if (obj != null && obj.length > 0) {
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					Strjudename = "";
					revistus = "";
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("classId", rowList[0]);
					mapList.put("batchId", rowList[1]);
					mapList.put("appInsId", rowList[2]);
					List<Map<String, Object>> listMap = sqlQuery.queryForList(sql, new Object[] { rowList[2] });
					for (Map<String, Object> map : listMap) {
						if (Strjudename.equals("")) {
							Strjudename = map.get("TZ_REALNAME") == null ? "" : map.get("TZ_REALNAME").toString();
							System.out.println("Strjudename:" + Strjudename);
							revistus = map.get("TZ_PSHEN_ZT") == null ? "" : map.get("TZ_PSHEN_ZT").toString();
							System.out.println("revistus:" + revistus);
						} else {
							Strjudename = Strjudename + "," + map.get("TZ_REALNAME") == null ? ""
									: map.get("TZ_REALNAME").toString();

						}

					}
					// 评委列表、评审状态
					String pwList = "", reviewStatusDesc = "";
					// 评委总分
					Float pwTotal = 0.00f;
					// 评委数
					Integer pwNum = 0;

					String sql1 = "";
					// 每生评审人数、当前评审轮次
					sql1 = "SELECT TZ_MSPY_NUM,TZ_DQPY_LUNC FROM PS_TZ_CLPS_GZ_TBL  WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
					Map<String, Object> mapRule = sqlQuery.queryForMap(sql1, new Object[] { rowList[0], rowList[1] });
					String strMspsNum = mapRule.get("TZ_MSPY_NUM") == null ? "" : mapRule.get("TZ_MSPY_NUM").toString();
					Integer mspsNum = 2;
					if (!"".equals(strMspsNum) && strMspsNum != null) {
						mspsNum = Integer.valueOf(strMspsNum);
					}
					String strDqpyLunc = mapRule.get("TZ_DQPY_LUNC") == null ? ""
							: mapRule.get("TZ_DQPY_LUNC").toString();
					Integer dqpyLunc = 0;
					if (!"".equals(strDqpyLunc) && strDqpyLunc != null) {
						dqpyLunc = Integer.valueOf(strDqpyLunc);
					}

					sql1 = tzSQLObject.getSQLText("SQL.TZMaterialInterviewReviewBundle.material.TzGetMaterialKsPwInfo");
					List<Map<String, Object>> listPw = sqlQuery.queryForList(sql1,
							new Object[] { rowList[0], rowList[1], rowList[2], dqpyLunc });

					for (Map<String, Object> mapPw : listPw) {

						String pwOprid = mapPw.get("TZ_PWEI_OPRID") == null ? ""
								: mapPw.get("TZ_PWEI_OPRID").toString();
						String pwDlzhId = mapPw.get("TZ_DLZH_ID") == null ? "" : mapPw.get("TZ_DLZH_ID").toString();
						String scoreInsId = mapPw.get("TZ_SCORE_INS_ID") == null ? ""
								: mapPw.get("TZ_SCORE_INS_ID").toString();
						Float scoreNum = mapPw.get("TZ_SCORE_NUM") == null ? Float.valueOf("0")
								: Float.valueOf(mapPw.get("TZ_SCORE_NUM").toString());
						String submitFlag = mapPw.get("TZ_SUBMIT_YN") == null ? ""
								: mapPw.get("TZ_SUBMIT_YN").toString();

						if ("Y".equals(submitFlag)) {
							// 已评审
							pwNum++;
						}

						if (!"".equals(pwList)) {
							pwList += "," + pwDlzhId;
						} else {
							pwList = pwDlzhId;
						}
						pwTotal += scoreNum;
					}

					if (mspsNum.equals(pwNum)) {
						reviewStatusDesc = "已完成";
					} else {
						reviewStatusDesc = "未完成（" + pwNum + "/" + mspsNum + "）";
					}
					mapList.put("pwList", pwList);
					mapList.put("reviewStatusDesc", reviewStatusDesc);
					mapList.put("judges", Strjudename);
					mapList.put("judgeStatus", revistus);
					// mapList.put("ksOprId", rowList[4]);
					// mapList.put("jygzTsxx", rowList[4]);
					mapList.put("ksOprId", rowList[5]);
					mapList.put("ksName", rowList[6]);
					mapList.put("gender", rowList[7]);
					mapList.put("mshId", rowList[8]);
					mapList.put("className", rowList[9]);
					mapList.put("batchName", rowList[10]);
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

	// 添加考生
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		Date nowdate = new Date();
		Long appinsId = (long) 0;
		String ksName = "";
		String ksNameList = "";
		String appinsIdList = "";
		int count = 0;
		int count1 = 0;
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
				String sql1 = "SELECT  COUNT(1) FROM PS_TZ_APP_INS_T WHERE TZ_APP_INS_ID=?";
				ksName = jacksonUtil.getString("ksName");
				count1 = sqlQuery.queryForObject(sql1, new Object[] { appinsId }, "Integer");
				if (count1 > 0) {
					String sql = "SELECT COUNT(1) from PS_TZ_MSPS_KSH_TBL where TZ_CLASS_ID =? and TZ_APPLY_PC_ID =? and TZ_APP_INS_ID=?";
					count = sqlQuery.queryForObject(sql, new Object[] { classId, batchId, appinsId }, "Integer");
					if (count > 0) {
						ksNameList = ksNameList + ksName + ",";

					} else {
						PsTzMsPsksTbl psTzMsPsksTbl = new PsTzMsPsksTbl();
						psTzMsPsksTbl.setTzClassId(classId);
						psTzMsPsksTbl.setTzApplyPcId(batchId);
						psTzMsPsksTbl.setTzAppInsId(appinsId);
						psTzMsPsksTbl.setTzLuquZt("B");
						psTzMsPsksTbl.setRowAddedDttm(nowdate);
						psTzMsPsksTbl.setRowAddedOprid(Oprid);
						psTzMsPsksTbl.setRowLastmantDttm(nowdate);
						psTzMsPsksTbl.setRowLastmantOprid(Oprid);
						psTzMsPsksTblMapper.insertSelective(psTzMsPsksTbl);
					}

				} else {
					appinsIdList = appinsIdList + appinsId + ",";

				}

			}

			if (!"".equals(ksNameList) && "".equals(appinsIdList)) {
				errMsg[0] = "1";
				errMsg[1] = "考生:" + ksNameList + "已经在考生列表，不能重复添加！";
			}
			if (!"".equals(appinsIdList) && "".equals(ksNameList)) {
				errMsg[0] = "1";
				errMsg[1] = "报名表编号" + appinsId + "不存在，导入失败！";
			}
			if (!"".equals(appinsIdList) && !"".equals(ksNameList)) {
				errMsg[0] = "1";
				errMsg[1] = "报名表编号" + appinsId + "不存在，导入失败！" + "考生:" + ksNameList + "已经在考生列表，不能重复添加！";
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();

			// TODO: handle exception
		}

		return null;
	}

}
