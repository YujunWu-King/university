package com.tranzvision.gd.TZMaterialInterviewReviewBundle.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.tranzvision.gd.TZBaseBundle.service.impl.BatchProcessDetailsImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZMbaPwClpsBundle.dao.PsTzClpsGzTblMapper;
import com.tranzvision.gd.TZMbaPwClpsBundle.dao.PsTzClpsKshTblMapper;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzClpsGzTbl;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzClpsGzTblKey;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzClpsKshTbl;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzClpsKshTblKey;
import com.tranzvision.gd.batch.engine.base.BaseEngine;
import com.tranzvision.gd.batch.engine.base.EngineParameters;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.poi.excel.ExcelHandle;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 材料评审考生名单
 * 
 * @author LuYan 2017-3-22
 *
 */
@Service("com.tranzvision.gd.TZMaterialInterviewReviewBundle.service.impl.TzClpsExamineeServiceImpl")
public class TzClpsExamineeServiceImpl extends FrameworkImpl {

	@Autowired
	private TZGDObject tzSQLObject;
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	@Autowired
	private PsTzClpsKshTblMapper psTzClpsKshTblMapper;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private PsTzClpsGzTblMapper psTzClpsGzTblMapper;
	@Autowired
	private GetHardCodePoint getHardCodePoint;
	@Autowired
	private BatchProcessDetailsImpl batchProcessDetailsImpl;
	@Autowired
	private PsTzExcelDattTMapper psTzExcelDattTMapper;
	@Autowired
	private PsTzExcelDrxxTMapper psTzExcelDrxxTMapper;
	@Autowired
	private PsTzZdcsDcAetMapper psTzZdcsDcAetMapper;

	/* 材料评审基本信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String, Object> mapRet = new HashMap<String, Object>();
		Map<String, Object> mapData = new HashMap<String, Object>();

		try {
			jacksonUtil.json2Map(strParams);

			// 班级编号
			String classId = jacksonUtil.getString("classId");
			// 批次
			String batchId = jacksonUtil.getString("batchId");

			// 班级名称
			String className = "";
			// 批次名称
			String batchName = "";
			// 当前评审轮次
			Integer dqpsLunc = 0;
			// 当前评审状态
			String dqpsStatus = "";

			// 报考考生数量
			Integer bkksNum = 0;
			// 材料评审考生
			Integer clpsksNum = 0;
			// 每生评审人数
			String judgeNumSet = "";

			String sql = tzSQLObject.getSQLText("SQL.TZMaterialInterviewReviewBundle.material.TzGetMaterialRuleInfo");
			Map<String, Object> mapBasic = sqlQuery.queryForMap(sql, new Object[] { classId, batchId });

			if (mapBasic != null) {
				className = (String) mapBasic.get("TZ_CLASS_NAME");
				batchName = (String) mapBasic.get("TZ_BATCH_NAME");
				dqpsLunc = mapBasic.get("TZ_DQPY_LUNC") == null ? 0 : Integer.valueOf(mapBasic.get("TZ_DQPY_LUNC").toString());
				dqpsStatus = (String) mapBasic.get("TZ_DQPY_ZT");
				bkksNum = mapBasic.get("TZ_BKKS_NUM") == null ? 0 : Integer.valueOf(mapBasic.get("TZ_BKKS_NUM").toString());
				clpsksNum = mapBasic.get("TZ_CLPS_KS_NUM") == null ? 0 : Integer.valueOf(mapBasic.get("TZ_CLPS_KS_NUM").toString());
				judgeNumSet = mapBasic.get("TZ_MSPY_NUM") == null ? "" : String.valueOf(mapBasic.get("TZ_MSPY_NUM"));

				mapData.put("classId", classId);
				mapData.put("className", className);
				mapData.put("batchId", batchId);
				mapData.put("batchName", batchName);
				mapData.put("dqpsLunc", dqpsLunc);
				mapData.put("dqpsStatus", dqpsStatus);
				mapData.put("bkksNum", bkksNum);
				mapData.put("clpsksNum", clpsksNum);
				mapData.put("judgeNumSet", judgeNumSet);

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

	/* 考生名单列表 */
	@Override
	@SuppressWarnings({ "unchecked", "unused" })
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();

		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);

		try {
			jacksonUtil.json2Map(strParams);
			
			if(jacksonUtil.containsKey("type") //导出历史记录查询
					&& "expHistory".equals(jacksonUtil.getString("type"))){
				// 排序字段
				String[][] orderByArr = new String[][] {{"TZ_STARTTIME","DESC"}};

				// json数据要的结果字段;
				String[] resultFldArray = { "TZ_DR_LXBH", "TZ_DLZH_ID", "TZ_DR_TASK_DESC", "TZ_STARTTIME", "PROCESSINSTANCE",
						"PROCESS_STATUS","TZ_REALNAME"};

				// 可配置搜索通用函数;
				Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart, errMsg);

				if (obj != null && obj.length > 0) {

					ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

					for (int i = 0; i < list.size(); i++) {
						String[] rowList = list.get(i);

						Map<String, Object> mapList = new HashMap<String, Object>();
						mapList.put("classBatch", rowList[0]);
						mapList.put("loginId", rowList[1]);
						mapList.put("fileName", rowList[2]);
						mapList.put("bgTime", rowList[3]);
						mapList.put("procInsId", rowList[4]);
						mapList.put("procState", rowList[5]);
						mapList.put("czPerName", rowList[6]);
						
						String stateDescr = batchProcessDetailsImpl.getBatchProcessStatusDescsr(rowList[5]);
						mapList.put("procStaDescr", stateDescr);
						
						listData.add(mapList);
					}

					mapRet.replace("total", obj[0]);
					mapRet.replace("root", listData);
				}
			}else{
			
				// 排序字段
				String[][] orderByArr = new String[][] {{"TZ_MSSQH","ASC"}};
			
	
				// json数据要的结果字段
				String[] resultFldArray = { "TZ_CLASS_ID", "TZ_APPLY_PC_ID", "TZ_REALNAME", "TZ_MSSQH", "TZ_APP_INS_ID", "TZ_GENDER", "TZ_GENDER_DESC", "TZ_PW_LIST", "TZ_PW_ZF", "TZ_PWPS_ZT", "TZ_MSHI_ZGFLG", "TZ_MSHI_ZGFLG_DESC" };
	
				// 可配置搜索通用函数
				Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart, errMsg);
	
				String classId = "";
				String batchId = "";
				
				Integer mspsNum = 2;
				Integer dqpyLunc = 0;
				
				String sql = "";
				
				if (obj != null && obj.length > 0) {
					
					int numTotal = (int) obj[0];
					ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
					
					if(list !=null && list.size()>0) {
						String[] rowListTmp = list.get(0);
						classId = rowListTmp[0];
						batchId = rowListTmp[1];
						
						// 每生评审人数、当前评审轮次
						sql = "SELECT TZ_MSPY_NUM,TZ_DQPY_LUNC FROM PS_TZ_CLPS_GZ_TBL  WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
						Map<String, Object> mapRule = sqlQuery.queryForMap(sql, new Object[] { classId, batchId });
						String strMspsNum = mapRule.get("TZ_MSPY_NUM") == null ? "" : mapRule.get("TZ_MSPY_NUM").toString();
						if(!"".equals(strMspsNum) && strMspsNum!=null) {
							mspsNum = Integer.valueOf(strMspsNum);
						}
						String strDqpyLunc = mapRule.get("TZ_DQPY_LUNC") == null ? "" : mapRule.get("TZ_DQPY_LUNC").toString();
						if (!"".equals(strDqpyLunc) && strDqpyLunc != null) {
							dqpyLunc = Integer.valueOf(strDqpyLunc);
						}
					}
					
					//可配置搜索查询语句
					String[] resultFldArraySql = {"TZ_APP_INS_ID"};
					
					String searchSql = fliterForm.getQuerySQL(resultFldArraySql, orderByArr, strParams, errMsg);
					if(numLimit == 0 && numStart == 0) {
						
					} else {
						if (numLimit != 0) {
							searchSql += " limit " + numStart + "," + numLimit;
						} else {
							searchSql += " limit " + numStart + "," + (numTotal-numStart);
						}
					}
					
					//查询列表考生的所有评委和评审信息
					ArrayList<Map<String,Object>> listKsPwInfo = new ArrayList<Map<String,Object>>();
					
					String judgeSql = "SELECT A.TZ_PWEI_OPRID,A.TZ_SCORE_INS_ID,A.TZ_APP_INS_ID,E.TZ_SCORE_NUM,F.TZ_DLZH_ID,B.TZ_SUBMIT_YN ";
					judgeSql += " FROM PS_TZ_CP_PW_KS_TBL A LEFT JOIN PS_TZ_KSCLPSLS_TBL B ON A.TZ_CLASS_ID=B.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID=B.TZ_APPLY_PC_ID AND A.TZ_APP_INS_ID=B.TZ_APP_INS_ID AND A.TZ_PWEI_OPRID=B.TZ_PWEI_OPRID";
					judgeSql += " LEFT JOIN PS_TZ_CLPS_GZ_TBL C ON A.TZ_CLASS_ID=C.TZ_CLASS_ID AND B.TZ_CLPS_LUNC = C.TZ_DQPY_LUNC AND A.TZ_APPLY_PC_ID=C.TZ_APPLY_PC_ID";
					judgeSql += " LEFT JOIN PS_TZ_CJX_TBL E ON A.TZ_SCORE_INS_ID=E.TZ_SCORE_INS_ID AND E.TZ_SCORE_ITEM_ID=(SELECT F.TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT F WHERE F.TZ_HARDCODE_PNT='TZ_CLPS_ZFCJX_ID')";
					judgeSql += " LEFT JOIN PS_TZ_AQ_YHXX_TBL F ON A.TZ_PWEI_OPRID=F.OPRID";
					judgeSql += " WHERE B.TZ_CLASS_ID = ? AND B.TZ_APPLY_PC_ID = ? AND B.TZ_CLPS_LUNC=? AND A.TZ_APP_INS_ID IN ( SELECT X.TZ_APP_INS_ID FROM ( " + searchSql + " ) X ) AND B.TZ_SUBMIT_YN<>'C' ";
							
					List<Map<String, Object>> listKsPw = sqlQuery.queryForList(judgeSql,new Object[] { classId, batchId, dqpyLunc});
					
					for(Map<String, Object> mapKsPw : listKsPw) {
						String pwOprid = mapKsPw.get("TZ_PWEI_OPRID") == null ? "" : mapKsPw.get("TZ_PWEI_OPRID").toString();
						String scoreInsId = mapKsPw.get("TZ_SCORE_INS_ID") == null ? "" : mapKsPw.get("TZ_SCORE_INS_ID").toString();
						String appinsId = mapKsPw.get("TZ_APP_INS_ID") == null ? "" : mapKsPw.get("TZ_APP_INS_ID").toString();
						Float scoreNum = mapKsPw.get("TZ_SCORE_NUM") == null ? Float.valueOf("0") : Float.valueOf(mapKsPw.get("TZ_SCORE_NUM").toString());
						String pwDlzhId = mapKsPw.get("TZ_DLZH_ID") == null ? "" : mapKsPw.get("TZ_DLZH_ID").toString();
						String submitFlag = mapKsPw.get("TZ_SUBMIT_YN") == null ? "" : mapKsPw.get("TZ_SUBMIT_YN").toString();

						Map<String, Object> mapKsPwInfo = new HashMap<String,Object>();
						mapKsPwInfo.put("appinsId", appinsId);
						mapKsPwInfo.put("pwDlzhId", pwDlzhId);
						mapKsPwInfo.put("pwpsStatus", submitFlag);
						mapKsPwInfo.put("pwScore", scoreNum);
						
						listKsPwInfo.add(mapKsPwInfo);
					}
					
					
					for (int i = 0; i < list.size(); i++) {
						String[] rowList = list.get(i);
	
						classId = rowList[0];
						batchId = rowList[1];
						String appinsId = rowList[4];
	
						// 评委列表、评审状态
						String pwList = "", reviewStatusDesc = "";
						// 评委总分
						Float pwTotal = 0.00f;
						// 评委数
						Integer pwNum = 0;
						
						for(Map<String, Object> mapPwInfo : listKsPwInfo) {
							String appinsIdTmp = mapPwInfo.get("appinsId") == null ? "" : mapPwInfo.get("appinsId").toString();
							
							if(appinsId.equals(appinsIdTmp)) {
								
								String pwDlzhIdTmp = mapPwInfo.get("pwDlzhId") == null ? "" : mapPwInfo.get("pwDlzhId").toString();
								String pwpsStatusTmp = mapPwInfo.get("pwpsStatus") == null ? "" : mapPwInfo.get("pwpsStatus").toString();
								Float pwScoreTmp = mapPwInfo.get("pwScore") == null ? Float.valueOf("0") : Float.valueOf(mapPwInfo.get("pwScore").toString());
								
								if ("Y".equals(pwpsStatusTmp)) {
									// 已评审
									pwNum++;
								}
		
								if (!"".equals(pwList)) {
									pwList += "," + pwDlzhIdTmp;
								} else {
									pwList = pwDlzhIdTmp;
								}
								pwTotal += pwScoreTmp;
							}
						}
						
	
						/*
						sql = tzSQLObject.getSQLText("SQL.TZMaterialInterviewReviewBundle.material.TzGetMaterialKsPwInfo");
						List<Map<String, Object>> listPw = sqlQuery.queryForList(sql, new Object[] { classId, batchId, appinsId, dqpyLunc });
	
						for (Map<String, Object> mapPw : listPw) {
	
							String pwOprid = mapPw.get("TZ_PWEI_OPRID") == null ? "" : mapPw.get("TZ_PWEI_OPRID").toString();
							String pwDlzhId = mapPw.get("TZ_DLZH_ID") == null ? "" : mapPw.get("TZ_DLZH_ID").toString();
							String scoreInsId = mapPw.get("TZ_SCORE_INS_ID") == null ? "" : mapPw.get("TZ_SCORE_INS_ID").toString();
							Float scoreNum = mapPw.get("TZ_SCORE_NUM") == null ? Float.valueOf("0") : Float.valueOf(mapPw.get("TZ_SCORE_NUM").toString());
							String submitFlag = mapPw.get("TZ_SUBMIT_YN") == null ? "" : mapPw.get("TZ_SUBMIT_YN").toString();
	
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
						*/
	
						if (mspsNum.equals(pwNum)) {
							reviewStatusDesc = "已完成";
						} else {
							reviewStatusDesc = "未完成（" + pwNum + "/" + mspsNum + "）";
						}
	
						Map<String, Object> mapList = new HashMap<String, Object>();
						mapList.put("classId", classId);
						mapList.put("batchId", batchId);
						mapList.put("name", rowList[2]);
						mapList.put("mssqh", rowList[3]);
						mapList.put("appinsId", appinsId);
						mapList.put("sex", rowList[5]);
						mapList.put("sexDesc", rowList[6]);
						mapList.put("judgeList", pwList);
						mapList.put("judgeTotal", pwTotal);
						mapList.put("reviewStatusDesc", reviewStatusDesc);
						mapList.put("interviewStatus", rowList[10]);
						mapList.put("interviewStatusDesc", rowList[11]);
						listData.add(mapList);
					}
					mapRet.replace("total", obj[0]);
					mapRet.replace("root", listData);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		strRet = jacksonUtil.Map2json(mapRet);

		return strRet;
	}

	/* 新增 */
	@SuppressWarnings("unused")
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {

			int num = 0;
			for (num = 0; num < actData.length; num++) {
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);

				String typeFlag = jacksonUtil.getString("typeFlag");
				Map<String, Object> mapData = jacksonUtil.getMap("data");

				if ("RULE".equals(typeFlag)) {
					// 评审规则基本信息
					String strRule = saveRuleBasic(mapData, errMsg);
				}

				if ("EXAMINEE".equals(typeFlag)) {
					// 考生
					String strExaminee = saveExamineeInfo(mapData, errMsg);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRet;
	}

	/* 保存 */
	@SuppressWarnings("unused")
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {

			int num = 0;
			for (num = 0; num < actData.length; num++) {
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);

				String typeFlag = jacksonUtil.getString("typeFlag");
				Map<String, Object> mapData = jacksonUtil.getMap("data");

				if ("RULE".equals(typeFlag)) {
					// 评审规则基本信息
					String strRule = saveRuleBasic(mapData, errMsg);
				}

				if ("EXAMINEE".equals(typeFlag)) {
					// 考生
					String strExaminee = saveExamineeInfo(mapData, errMsg);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRet;
	}

	/* 删除考生数据 */
	@SuppressWarnings("unchecked")
	public String tzDelete(String[] actData, String[] errMsg) {
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {

			if (actData.length == 0 || actData == null) {
				return strRet;
			}

			int num = 0;
			for (num = 0; num < actData.length; num++) {
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);

				if(jacksonUtil.containsKey("type") //删除导出记录
						&& "delExpExcel".equals(jacksonUtil.getString("type"))){
					List<Map<String,Object>> delData = (List<Map<String, Object>>) jacksonUtil.getList("data");
					
					if(delData != null && delData.size()>0){
						for(Map<String,Object> delMap : delData){
							String strProcInsId = delMap.get("procInsId") == null ? "": delMap.get("procInsId").toString();
							if(!"".equals(strProcInsId) && strProcInsId != null){
								int procInsId = Integer.parseInt(strProcInsId);
								
								PsTzExcelDattT psTzExcelDattT = psTzExcelDattTMapper.selectByPrimaryKey(procInsId);
								if(psTzExcelDattT != null){
									String filePath = psTzExcelDattT.getTzFwqFwlj();
									if(!"".equals(filePath) && filePath != null){
										filePath = request.getServletContext().getRealPath(filePath);
										
										File file = new File(filePath);
										if(file.exists() && file.isFile()){
											file.delete();
										}
									}
								}
								psTzExcelDrxxTMapper.deleteByPrimaryKey(procInsId);
								psTzExcelDattTMapper.deleteByPrimaryKey(procInsId);
							}
						}
					}
				}else{
					String classId = jacksonUtil.getString("classId");
					String batchId = jacksonUtil.getString("batchId");
					String appinsId = jacksonUtil.getString("appinsId");

					/*删除材料评审考生表*/
					PsTzClpsKshTblKey psTzClpsKshTblKey = new PsTzClpsKshTblKey();
					psTzClpsKshTblKey.setTzClassId(classId);
					psTzClpsKshTblKey.setTzApplyPcId(batchId);
					psTzClpsKshTblKey.setTzAppInsId(Long.valueOf(appinsId));

					psTzClpsKshTblMapper.deleteByPrimaryKey(psTzClpsKshTblKey);

					/*删除材料评审评委考生关系表*/
					String sql = "DELETE FROM PS_TZ_CP_PW_KS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=?";
					sqlQuery.update(sql, new Object[] { classId, batchId, appinsId });
					
					/*删除材料评审考生评委信息表，卢艳添加，2017-11-27*/
					String sql_kspw = "DELETE FROM PS_TZ_CLPSKSPW_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=?";
					sqlQuery.update(sql_kspw, new Object[] { classId, batchId, appinsId });
					
					/*删除材料评审考生评审得分历史表，卢艳添加，2017-11-27*/
					String sql_ksls = "DELETE FROM PS_TZ_KSCLPSLS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=?";
					sqlQuery.update(sql_ksls, new Object[] { classId, batchId, appinsId });
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRet;
	}

	public String tzOther(String operateType, String strParams, String[] errMsg) {
		String strRet = "";

		try {
			// 导出选中考生评议数据
			if ("tzExportExaminee".equals(operateType)) {
				strRet = exportExaminee(strParams, errMsg);
			}
			// 导出查询结果考试评议数据
			if ("tzExportAllExaminee".equals(operateType)) {
				strRet = exportAllExaminee(strParams, errMsg);
			}
			//获取可配置搜索sql
			if("getSearchSql".equals(operateType)){
				strRet = this.tzGetSearchSql(strParams, errMsg);
			}
			
			if("EXPORT".equals(operateType)){
				strRet = this.tzExportExcelFile(strParams, errMsg);
			}
			
			if("DOWNLOAD".equals(operateType)){
				strRet = this.tzDownloadExpFile(strParams, errMsg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRet;
	}

	// 导出选中考生评议数据
	public String exportExaminee(String strParams, String[] errMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {
			jacksonUtil.json2Map(strParams);

			String classId = jacksonUtil.getString("classId");
			String batchId = jacksonUtil.getString("batchId");
			List<?> appinsIdList = jacksonUtil.getList("appinsIds");

			// 当前机构
			String currentOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			// 当前登录账号
			String currentDlzhId = tzLoginServiceImpl.getLoginedManagerDlzhid(request);

			String sql = "";

			// 报名表中信息项编号
			// 本/专科院校
			String schoolNameXxxId = getHardCodePoint.getHardCodePointVal("TZ_BMB_BZKYX_XXX_ID");
			// 最高学历
			String highestRecordXxxId = getHardCodePoint.getHardCodePointVal("TZ_BMB_ZGXL_XXX_ID");
			// 工作所在地
			String companyAddressXxxId = getHardCodePoint.getHardCodePointVal("TZ_BMB_GZSZD_XXX_ID");
			// 工作单位
			String companyNameXxxId = getHardCodePoint.getHardCodePointVal("TZ_BMB_GZDW_XXX_ID");
			// 所在部门
			String departmentXxxId = getHardCodePoint.getHardCodePointVal("TZ_BMB_SZBM_XXX_ID");
			// 工作职位
			String positionXxxId = getHardCodePoint.getHardCodePointVal("TZ_BMB_GZZW_XXX_ID");
			// 自主创业全称
			String selfEmploymentXxxId = getHardCodePoint.getHardCodePointVal("TZ_BMB_ZZCYQC_XXX_ID");

			// 获取文件存储路径
			String fileBasePath = getSysHardCodeVal.getDownloadPath();
			// 材料评审excel存储路径
			String clpsExcelPath = "/material/xlsx";
			// 完整的存储路径
			String fileDirPath = fileBasePath + clpsExcelPath;

			// 生成表头
			List<String[]> dataCellKeys = new ArrayList<String[]>();
			dataCellKeys.add(new String[] { "className", "报考方向" });
			dataCellKeys.add(new String[] { "nationId", "证件号" });
			dataCellKeys.add(new String[] { "mssqh", "面试申请号" });
			dataCellKeys.add(new String[] { "name", "姓名" });
			dataCellKeys.add(new String[] { "judgeDlzhId", "评委账号" });
			dataCellKeys.add(new String[] { "judgeNum", "评委评审人数" });
			dataCellKeys.add(new String[] { "rank", "考生评审排名" });

			// 成绩项
			List<String[]> listScoreItemField = new ArrayList<String[]>();
			sql = tzSQLObject.getSQLText("SQL.TZMaterialInterviewReviewBundle.material.TzGetMaterialScoreItemInfo");
			List<Map<String, Object>> listScoreItem = sqlQuery.queryForList(sql, new Object[] { currentOrgId, classId });

			for (Map<String, Object> mapScoreItem : listScoreItem) {
				String scoreItemId = mapScoreItem.get("TZ_SCORE_ITEM_ID") == null ? "" : mapScoreItem.get("TZ_SCORE_ITEM_ID").toString();
				String scoreItemName = mapScoreItem.get("DESCR") == null ? "" : mapScoreItem.get("DESCR").toString();
				String scoreItemType = mapScoreItem.get("TZ_SCORE_ITEM_TYPE") == null ? "" : mapScoreItem.get("TZ_SCORE_ITEM_TYPE").toString();
				dataCellKeys.add(new String[] { scoreItemId, scoreItemName });
				listScoreItemField.add(new String[] { scoreItemId, scoreItemType });
			}

			dataCellKeys.add(new String[] { "birthday", "出生日期" });
			dataCellKeys.add(new String[] { "age", "年龄" });
			dataCellKeys.add(new String[] { "sex", "性别" });
			dataCellKeys.add(new String[] { "examineeTag", "考生标签" });
			dataCellKeys.add(new String[] { "schoolName", "本/专科院校" });
			dataCellKeys.add(new String[] { "highestRecord", "最高学历" });
			dataCellKeys.add(new String[] { "companyAddress", "工作所在地" });
			dataCellKeys.add(new String[] { "companyName", "工作单位" });
			dataCellKeys.add(new String[] { "department", "所在部门" });
			dataCellKeys.add(new String[] { "position", "工作职位" });
			dataCellKeys.add(new String[] { "selfEmployment ", "自主创业全称" });

			// 生成数据
			List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();

			for (Object objAppinsId : appinsIdList) {

				String appinsId = String.valueOf(objAppinsId);

				String className = "", nationId = "", mssqh = "", name = "", judgeDlzhId = "", judgeNum = "", rank = "", birthday = "", age = "", sex = "", examineeTag = "", schoolName = "", highestRecord = "", companyAddress = "", companyName = "", department = "", position = "", selfEmployment = "";
				String scoreInsId = "";

				// 考生数据
				sql = tzSQLObject.getSQLText("SQL.TZMaterialInterviewReviewBundle.material.TzGetMaterialKspyInfo");
				Map<String, Object> mapExaminee = sqlQuery.queryForMap(sql, new Object[] { schoolNameXxxId, highestRecordXxxId, companyAddressXxxId, companyNameXxxId, departmentXxxId, positionXxxId, selfEmploymentXxxId, currentOrgId, appinsId, classId });
				if (mapExaminee != null) {
					className = mapExaminee.get("TZ_CLASS_NAME") == null ? "" : mapExaminee.get("TZ_CLASS_NAME").toString();
					nationId = mapExaminee.get("NATIONAL_ID") == null ? "" : mapExaminee.get("NATIONAL_ID").toString();
					mssqh = mapExaminee.get("TZ_MSH_ID") == null ? "" : mapExaminee.get("TZ_MSH_ID").toString();
					name = mapExaminee.get("TZ_REALNAME") == null ? "" : mapExaminee.get("TZ_REALNAME").toString();
					birthday = mapExaminee.get("BIRTHDATE") == null ? "" : mapExaminee.get("BIRTHDATE").toString();
					age = mapExaminee.get("AGE") == null ? "" : mapExaminee.get("AGE").toString();
					sex = mapExaminee.get("TZ_GENDER_DESC") == null ? "" : mapExaminee.get("TZ_GENDER_DESC").toString();
					schoolName = mapExaminee.get("TZ_SCHOOL_NAME") == null ? "" : mapExaminee.get("TZ_SCHOOL_NAME").toString();
					highestRecord = mapExaminee.get("TZ_ZGXL") == null ? "" : mapExaminee.get("TZ_ZGXL").toString();
					companyAddress = mapExaminee.get("TZ_GZ_SZD") == null ? "" : mapExaminee.get("TZ_GZ_SZD").toString();
					companyName = mapExaminee.get("TZ_COMPANY_NAME") == null ? "" : mapExaminee.get("TZ_COMPANY_NAME").toString();
					department = mapExaminee.get("TZ_DEPARTMENT") == null ? "" : mapExaminee.get("TZ_DEPARTMENT").toString();
					position = mapExaminee.get("TZ_POSITION") == null ? "" : mapExaminee.get("TZ_POSITION").toString();
					selfEmployment = mapExaminee.get("TZ_ZZCY_NAME") == null ? "" : mapExaminee.get("TZ_ZZCY_NAME").toString();

					//报考方向拼接批次
					String strBatchSQL = "SELECT TZ_BATCH_NAME FROM PS_TZ_CLS_BATCH_T WHERE TZ_CLASS_ID=? AND TZ_BATCH_ID=?";
					String strBatchName = sqlQuery.queryForObject(strBatchSQL, new Object[]{classId, batchId}, "String");
					if(strBatchName==null||"".equals(strBatchName)){
						/*doNothing*/
					}else{
						className = className + " " + strBatchName;
					}
					
					// 评委数据
					sql = tzSQLObject.getSQLText("SQL.TZMaterialInterviewReviewBundle.material.TzGetMaterialPwpyInfo");
					List<Map<String, Object>> listJudge = sqlQuery.queryForList(sql, new Object[] { classId, batchId, appinsId });

					for (Map<String, Object> mapJudge : listJudge) {

						judgeDlzhId = mapJudge.get("TZ_DLZH_ID") == null ? "" : mapJudge.get("TZ_DLZH_ID").toString();
						scoreInsId = mapJudge.get("TZ_SCORE_INS_ID") == null ? "" : mapJudge.get("TZ_SCORE_INS_ID").toString();
						judgeNum = mapJudge.get("TZ_PYKS_XX") == null ? "" : mapJudge.get("TZ_PYKS_XX").toString();
						rank = mapJudge.get("TZ_KSH_PSPM") == null ? "" : mapJudge.get("TZ_KSH_PSPM").toString();

						Map<String, Object> mapData = new HashMap<String, Object>();
						mapData.put("className", className);
						mapData.put("nationId", nationId);
						mapData.put("mssqh", mssqh);
						mapData.put("name", name);
						mapData.put("judgeDlzhId", judgeDlzhId);
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

							String scoreValue = sqlQuery.queryForObject(sql, new Object[] { scoreInsId, scoreItemId }, "String");
							if ("D".equals(scoreItemType)) {
								sql = "SELECT A.TZ_CJX_XLK_XXMC FROM PS_TZ_ZJCJXXZX_T A,PS_TZ_RS_MODAL_TBL B,PS_TZ_CLASS_INF_T C ";
								sql += " WHERE C.TZ_ZLPS_SCOR_MD_ID=B.TZ_SCORE_MODAL_ID AND A.TZ_JG_ID=B.TZ_JG_ID AND A.TREE_NAME=B.TREE_NAME";
								sql += " AND C.TZ_CLASS_ID=? AND A.TZ_JG_ID=? AND A.TZ_SCORE_ITEM_ID=? AND A.TZ_CJX_XLK_XXBH=?";
								scoreValue = sqlQuery.queryForObject(sql, new Object[] { classId, currentOrgId, scoreItemId, scoreValue }, "String");
							}

							mapData.put(scoreItemId, scoreValue);

						}

						mapData.put("birthday", birthday);
						mapData.put("age", age);
						mapData.put("sex", sex);

						// 考生标签：自动标签+手动标签
						sql = tzSQLObject.getSQLText("SQL.TZMaterialInterviewReviewBundle.material.TzGetMaterialKsBqInfo");
						List<Map<String, Object>> listBq = sqlQuery.queryForList(sql, new Object[] { appinsId, appinsId });
						for (Map<String, Object> mapBq : listBq) {
							String bqName = mapBq.get("TZ_BQ_NAME") == null ? "" : mapBq.get("TZ_BQ_NAME").toString();
							if (!"".equals(examineeTag)) {
								examineeTag += "," + bqName;
							} else {
								examineeTag = bqName;
							}
						}
						mapData.put("examineeTag", examineeTag);

						mapData.put("schoolName", schoolName);
						mapData.put("highestRecord", highestRecord);
						mapData.put("companyAddress", companyAddress);
						mapData.put("companyName", companyName);
						mapData.put("department", department);
						mapData.put("position", position);
						mapData.put("selfEmployment", selfEmployment);

						dataList.add(mapData);
					}
				}
			}

			// 生成本次导出的文件名
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			Random random = new Random();
			int max = 999999999;
			int min = 100000000;
			String fileName = simpleDateFormat.format(new Date()) + "_" + currentDlzhId.toUpperCase() + "_" + String.valueOf(random.nextInt(max) % (max - min + 1) + min) + ".xlsx";

			ExcelHandle excelHandle = new ExcelHandle(request, fileDirPath, currentOrgId, "apply");
			boolean rst = excelHandle.export2Excel(fileName, dataCellKeys, dataList);
			if (rst) {
				String fileUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + excelHandle.getExportExcelPath();
				mapRet.put("fileUrl", fileUrl);
				strRet = jacksonUtil.Map2json(mapRet);
			} else {
				errMsg[0] = "1";
				errMsg[1] = "导出失败";
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRet;
	}

	// 导出选中考生评议数据
	@SuppressWarnings("unchecked")
	public String exportAllExaminee(String strParams, String[] errMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {

			jacksonUtil.json2Map(strParams);

			String classId = jacksonUtil.getString("classId");
			String batchId = jacksonUtil.getString("batchId");

			// 当前机构
			String currentOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			// 当前登录账号
			String currentDlzhId = tzLoginServiceImpl.getLoginedManagerDlzhid(request);

			String sql = "";

			// 报名表中信息项编号
			// 本/专科院校
			String schoolNameXxxId = getHardCodePoint.getHardCodePointVal("TZ_BMB_BZKYX_XXX_ID");
			// 最高学历
			String highestRecordXxxId = getHardCodePoint.getHardCodePointVal("TZ_BMB_ZGXL_XXX_ID");
			// 工作所在地
			String companyAddressXxxId = getHardCodePoint.getHardCodePointVal("TZ_BMB_GZSZD_XXX_ID");
			// 工作单位
			String companyNameXxxId = getHardCodePoint.getHardCodePointVal("TZ_BMB_GZDW_XXX_ID");
			// 所在部门
			String departmentXxxId = getHardCodePoint.getHardCodePointVal("TZ_BMB_SZBM_XXX_ID");
			// 工作职位
			String positionXxxId = getHardCodePoint.getHardCodePointVal("TZ_BMB_GZZW_XXX_ID");
			// 自主创业全称
			String selfEmploymentXxxId = getHardCodePoint.getHardCodePointVal("TZ_BMB_ZZCYQC_XXX_ID");

			// 获取文件存储路径
			String fileBasePath = getSysHardCodeVal.getDownloadPath();
			// 材料评审excel存储路径
			String clpsExcelPath = "/material/xlsx";
			// 完整的存储路径
			String fileDirPath = fileBasePath + clpsExcelPath;

			// 生成表头
			List<String[]> dataCellKeys = new ArrayList<String[]>();
			dataCellKeys.add(new String[] { "className", "报考方向" });
			dataCellKeys.add(new String[] { "nationId", "证件号" });
			dataCellKeys.add(new String[] { "mssqh", "面试申请号" });
			dataCellKeys.add(new String[] { "name", "姓名" });
			dataCellKeys.add(new String[] { "judgeDlzhId", "评委账号" });
			dataCellKeys.add(new String[] { "judgeNum", "评委评审人数" });
			dataCellKeys.add(new String[] { "rank", "考生评审排名" });

			// 成绩项
			List<String[]> listScoreItemField = new ArrayList<String[]>();
			sql = tzSQLObject.getSQLText("SQL.TZMaterialInterviewReviewBundle.material.TzGetMaterialScoreItemInfo");
			List<Map<String, Object>> listScoreItem = sqlQuery.queryForList(sql, new Object[] { currentOrgId, classId });

			for (Map<String, Object> mapScoreItem : listScoreItem) {
				String scoreItemId = mapScoreItem.get("TZ_SCORE_ITEM_ID") == null ? "" : mapScoreItem.get("TZ_SCORE_ITEM_ID").toString();
				String scoreItemName = mapScoreItem.get("DESCR") == null ? "" : mapScoreItem.get("DESCR").toString();
				String scoreItemType = mapScoreItem.get("TZ_SCORE_ITEM_TYPE") == null ? "" : mapScoreItem.get("TZ_SCORE_ITEM_TYPE").toString();
				dataCellKeys.add(new String[] { scoreItemId, scoreItemName });
				listScoreItemField.add(new String[] { scoreItemId, scoreItemType });
			}

			dataCellKeys.add(new String[] { "birthday", "出生日期" });
			dataCellKeys.add(new String[] { "age", "年龄" });
			dataCellKeys.add(new String[] { "sex", "性别" });
			dataCellKeys.add(new String[] { "examineeTag", "考生标签" });
			dataCellKeys.add(new String[] { "schoolName", "本/专科院校" });
			dataCellKeys.add(new String[] { "highestRecord", "最高学历" });
			dataCellKeys.add(new String[] { "companyAddress", "工作所在地" });
			dataCellKeys.add(new String[] { "companyName", "工作单位" });
			dataCellKeys.add(new String[] { "department", "所在部门" });
			dataCellKeys.add(new String[] { "position", "工作职位" });
			dataCellKeys.add(new String[] { "selfEmployment ", "自主创业全称" });

			// 生成数据
			List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();

			String tzStoreParams = jacksonUtil.getString("tzStoreParams");
			String totalCount = jacksonUtil.getString("totalCount");

			int numStart = 0;
			int numLimit = Integer.valueOf(totalCount);
			// 排序字段
			String[][] orderByArr = new String[][] {};

			// json数据要的结果字段
			String[] resultFldArray = { "TZ_CLASS_ID", "TZ_APPLY_PC_ID", "TZ_REALNAME", "TZ_MSSQH", "TZ_APP_INS_ID", "TZ_GENDER", "TZ_GENDER_DESC", "TZ_PW_LIST", "TZ_PW_ZF", "TZ_PWPS_ZT", "TZ_MSHI_ZGFLG", "TZ_MSHI_ZGFLG_DESC" };

			// 可配置搜索通用函数
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, tzStoreParams, numLimit, numStart, errMsg);

			if (obj != null && obj.length > 0) {
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);

					classId = rowList[0];
					batchId = rowList[1];
					String appinsId = rowList[4];

					String className = "", nationId = "", mssqh = "", name = "", judgeDlzhId = "", judgeNum = "", rank = "", birthday = "", age = "", sex = "", examineeTag = "", schoolName = "", highestRecord = "", companyAddress = "", companyName = "", department = "", position = "", selfEmployment = "";
					String scoreInsId = "";

					// 考生数据
					sql = tzSQLObject.getSQLText("SQL.TZMaterialInterviewReviewBundle.material.TzGetMaterialKspyInfo");
					Map<String, Object> mapExaminee = sqlQuery.queryForMap(sql, new Object[] { schoolNameXxxId, highestRecordXxxId, companyAddressXxxId, companyNameXxxId, departmentXxxId, positionXxxId, selfEmploymentXxxId, currentOrgId, appinsId, classId });
					if (mapExaminee != null) {
						className = mapExaminee.get("TZ_CLASS_NAME") == null ? "" : mapExaminee.get("TZ_CLASS_NAME").toString();
						nationId = mapExaminee.get("NATIONAL_ID") == null ? "" : mapExaminee.get("NATIONAL_ID").toString();
						mssqh = mapExaminee.get("TZ_MSH_ID") == null ? "" : mapExaminee.get("TZ_MSH_ID").toString();
						name = mapExaminee.get("TZ_REALNAME") == null ? "" : mapExaminee.get("TZ_REALNAME").toString();
						birthday = mapExaminee.get("BIRTHDATE") == null ? "" : mapExaminee.get("BIRTHDATE").toString();
						age = mapExaminee.get("AGE") == null ? "" : mapExaminee.get("AGE").toString();
						sex = mapExaminee.get("TZ_GENDER_DESC") == null ? "" : mapExaminee.get("TZ_GENDER_DESC").toString();
						schoolName = mapExaminee.get("TZ_SCHOOL_NAME") == null ? "" : mapExaminee.get("TZ_SCHOOL_NAME").toString();
						highestRecord = mapExaminee.get("TZ_ZGXL") == null ? "" : mapExaminee.get("TZ_ZGXL").toString();
						companyAddress = mapExaminee.get("TZ_GZ_SZD") == null ? "" : mapExaminee.get("TZ_GZ_SZD").toString();
						companyName = mapExaminee.get("TZ_COMPANY_NAME") == null ? "" : mapExaminee.get("TZ_COMPANY_NAME").toString();
						department = mapExaminee.get("TZ_DEPARTMENT") == null ? "" : mapExaminee.get("TZ_DEPARTMENT").toString();
						position = mapExaminee.get("TZ_POSITION") == null ? "" : mapExaminee.get("TZ_POSITION").toString();
						selfEmployment = mapExaminee.get("TZ_ZZCY_NAME") == null ? "" : mapExaminee.get("TZ_ZZCY_NAME").toString();

						//报考方向拼接批次
						String strBatchSQL = "SELECT TZ_BATCH_NAME FROM PS_TZ_CLS_BATCH_T WHERE TZ_CLASS_ID=? AND TZ_BATCH_ID=?";
						String strBatchName = sqlQuery.queryForObject(strBatchSQL, new Object[]{classId, batchId}, "String");
						if(strBatchName==null||"".equals(strBatchName)){
							/*doNothing*/
						}else{
							className = className + " " + strBatchName;
						}
						// 评委数据
						sql = tzSQLObject.getSQLText("SQL.TZMaterialInterviewReviewBundle.material.TzGetMaterialPwpyInfo");
						List<Map<String, Object>> listJudge = sqlQuery.queryForList(sql, new Object[] { classId, batchId, appinsId });

						for (Map<String, Object> mapJudge : listJudge) {

							judgeDlzhId = mapJudge.get("TZ_DLZH_ID") == null ? "" : mapJudge.get("TZ_DLZH_ID").toString();
							scoreInsId = mapJudge.get("TZ_SCORE_INS_ID") == null ? "" : mapJudge.get("TZ_SCORE_INS_ID").toString();
							judgeNum = mapJudge.get("TZ_PYKS_XX") == null ? "" : mapJudge.get("TZ_PYKS_XX").toString();
							rank = mapJudge.get("TZ_KSH_PSPM") == null ? "" : mapJudge.get("TZ_KSH_PSPM").toString();

							Map<String, Object> mapData = new HashMap<String, Object>();
							mapData.put("className", className);
							mapData.put("nationId", nationId);
							mapData.put("mssqh", mssqh);
							mapData.put("name", name);
							mapData.put("judgeDlzhId", judgeDlzhId);
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

								String scoreValue = sqlQuery.queryForObject(sql, new Object[] { scoreInsId, scoreItemId }, "String");
								if ("D".equals(scoreItemType)) {
									sql = "SELECT A.TZ_CJX_XLK_XXMC FROM PS_TZ_ZJCJXXZX_T A,PS_TZ_RS_MODAL_TBL B,PS_TZ_CLASS_INF_T C ";
									sql += " WHERE C.TZ_ZLPS_SCOR_MD_ID=B.TZ_SCORE_MODAL_ID AND A.TZ_JG_ID=B.TZ_JG_ID AND A.TREE_NAME=B.TREE_NAME";
									sql += " AND C.TZ_CLASS_ID=? AND A.TZ_JG_ID=? AND A.TZ_SCORE_ITEM_ID=? AND A.TZ_CJX_XLK_XXBH=?";
									scoreValue = sqlQuery.queryForObject(sql, new Object[] { classId, currentOrgId, scoreItemId, scoreValue }, "String");
								}

								mapData.put(scoreItemId, scoreValue);

							}

							mapData.put("birthday", birthday);
							mapData.put("age", age);
							mapData.put("sex", sex);

							// 考生标签：自动标签+手动标签
							sql = tzSQLObject.getSQLText("SQL.TZMaterialInterviewReviewBundle.material.TzGetMaterialKsBqInfo");
							List<Map<String, Object>> listBq = sqlQuery.queryForList(sql, new Object[] { appinsId, appinsId });
							for (Map<String, Object> mapBq : listBq) {
								String bqName = mapBq.get("TZ_BQ_NAME") == null ? "" : mapBq.get("TZ_BQ_NAME").toString();
								if (!"".equals(examineeTag)) {
									examineeTag += "," + bqName;
								} else {
									examineeTag = bqName;
								}
							}
							mapData.put("examineeTag", examineeTag);

							mapData.put("schoolName", schoolName);
							mapData.put("highestRecord", highestRecord);
							mapData.put("companyAddress", companyAddress);
							mapData.put("companyName", companyName);
							mapData.put("department", department);
							mapData.put("position", position);
							mapData.put("selfEmployment", selfEmployment);

							dataList.add(mapData);
						}
					}
				}
			}

			/*for (Object objAppinsId : appinsIdList) {

				String appinsId = String.valueOf(objAppinsId);

				String className = "", nationId = "", mssqh = "", name = "", judgeDlzhId = "", judgeNum = "", rank = "", birthday = "", age = "", sex = "", examineeTag = "", schoolName = "", highestRecord = "", companyAddress = "", companyName = "", department = "", position = "", selfEmployment = "";
				String scoreInsId = "";

				// 考生数据
				sql = tzSQLObject.getSQLText("SQL.TZMaterialInterviewReviewBundle.material.TzGetMaterialKspyInfo");
				Map<String, Object> mapExaminee = sqlQuery.queryForMap(sql, new Object[] { schoolNameXxxId, highestRecordXxxId, companyAddressXxxId, companyNameXxxId, departmentXxxId, positionXxxId, selfEmploymentXxxId, currentOrgId, appinsId, classId });
				if (mapExaminee != null) {
					className = mapExaminee.get("TZ_CLASS_NAME") == null ? "" : mapExaminee.get("TZ_CLASS_NAME").toString();
					nationId = mapExaminee.get("NATIONAL_ID") == null ? "" : mapExaminee.get("NATIONAL_ID").toString();
					mssqh = mapExaminee.get("TZ_MSH_ID") == null ? "" : mapExaminee.get("TZ_MSH_ID").toString();
					name = mapExaminee.get("TZ_REALNAME") == null ? "" : mapExaminee.get("TZ_REALNAME").toString();
					birthday = mapExaminee.get("BIRTHDATE") == null ? "" : mapExaminee.get("BIRTHDATE").toString();
					age = mapExaminee.get("AGE") == null ? "" : mapExaminee.get("AGE").toString();
					sex = mapExaminee.get("TZ_GENDER_DESC") == null ? "" : mapExaminee.get("TZ_GENDER_DESC").toString();
					schoolName = mapExaminee.get("TZ_SCHOOL_NAME") == null ? "" : mapExaminee.get("TZ_SCHOOL_NAME").toString();
					highestRecord = mapExaminee.get("TZ_ZGXL") == null ? "" : mapExaminee.get("TZ_ZGXL").toString();
					companyAddress = mapExaminee.get("TZ_GZ_SZD") == null ? "" : mapExaminee.get("TZ_GZ_SZD").toString();
					companyName = mapExaminee.get("TZ_COMPANY_NAME") == null ? "" : mapExaminee.get("TZ_COMPANY_NAME").toString();
					department = mapExaminee.get("TZ_DEPARTMENT") == null ? "" : mapExaminee.get("TZ_DEPARTMENT").toString();
					position = mapExaminee.get("TZ_POSITION") == null ? "" : mapExaminee.get("TZ_POSITION").toString();
					selfEmployment = mapExaminee.get("TZ_ZZCY_NAME") == null ? "" : mapExaminee.get("TZ_ZZCY_NAME").toString();

					// 评委数据
					sql = tzSQLObject.getSQLText("SQL.TZMaterialInterviewReviewBundle.material.TzGetMaterialPwpyInfo");
					List<Map<String, Object>> listJudge = sqlQuery.queryForList(sql, new Object[] { classId, batchId, appinsId });

					for (Map<String, Object> mapJudge : listJudge) {

						judgeDlzhId = mapJudge.get("TZ_DLZH_ID") == null ? "" : mapJudge.get("TZ_DLZH_ID").toString();
						scoreInsId = mapJudge.get("TZ_SCORE_INS_ID") == null ? "" : mapJudge.get("TZ_SCORE_INS_ID").toString();
						judgeNum = mapJudge.get("TZ_PYKS_XX") == null ? "" : mapJudge.get("TZ_PYKS_XX").toString();
						rank = mapJudge.get("TZ_KSH_PSPM") == null ? "" : mapJudge.get("TZ_KSH_PSPM").toString();

						Map<String, Object> mapData = new HashMap<String, Object>();
						mapData.put("className", className);
						mapData.put("nationId", nationId);
						mapData.put("mssqh", mssqh);
						mapData.put("name", name);
						mapData.put("judgeDlzhId", judgeDlzhId);
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

							String scoreValue = sqlQuery.queryForObject(sql, new Object[] { scoreInsId, scoreItemId }, "String");
							if ("D".equals(scoreItemType)) {
								sql = "SELECT A.TZ_CJX_XLK_XXMC FROM PS_TZ_ZJCJXXZX_T A,PS_TZ_RS_MODAL_TBL B,PS_TZ_CLASS_INF_T C ";
								sql += " WHERE C.TZ_ZLPS_SCOR_MD_ID=B.TZ_SCORE_MODAL_ID AND A.TZ_JG_ID=B.TZ_JG_ID AND A.TREE_NAME=B.TREE_NAME";
								sql += " AND C.TZ_CLASS_ID=? AND A.TZ_JG_ID=? AND A.TZ_SCORE_ITEM_ID=? AND A.TZ_CJX_XLK_XXBH=?";
								scoreValue = sqlQuery.queryForObject(sql, new Object[] { classId, currentOrgId, scoreItemId, scoreValue }, "String");
							}

							mapData.put(scoreItemId, scoreValue);

						}

						mapData.put("birthday", birthday);
						mapData.put("age", age);
						mapData.put("sex", sex);

						// 考生标签：自动标签+手动标签
						sql = tzSQLObject.getSQLText("SQL.TZMaterialInterviewReviewBundle.material.TzGetMaterialKsBqInfo");
						List<Map<String, Object>> listBq = sqlQuery.queryForList(sql, new Object[] { appinsId, appinsId });
						for (Map<String, Object> mapBq : listBq) {
							String bqName = mapBq.get("TZ_BQ_NAME") == null ? "" : mapBq.get("TZ_BQ_NAME").toString();
							if (!"".equals(examineeTag)) {
								examineeTag += "," + bqName;
							} else {
								examineeTag = bqName;
							}
						}
						mapData.put("examineeTag", examineeTag);

						mapData.put("schoolName", schoolName);
						mapData.put("highestRecord", highestRecord);
						mapData.put("companyAddress", companyAddress);
						mapData.put("companyName", companyName);
						mapData.put("department", department);
						mapData.put("position", position);
						mapData.put("selfEmployment", selfEmployment);

						dataList.add(mapData);
					}
				}
			}*/

			// 生成本次导出的文件名
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			Random random = new Random();
			int max = 999999999;
			int min = 100000000;
			String fileName = simpleDateFormat.format(new Date()) + "_" + currentDlzhId.toUpperCase() + "_" + String.valueOf(random.nextInt(max) % (max - min + 1) + min) + ".xlsx";

			ExcelHandle excelHandle = new ExcelHandle(request, fileDirPath, currentOrgId, "apply");
			boolean rst = excelHandle.export2Excel(fileName, dataCellKeys, dataList);
			if (rst) {
				String fileUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + excelHandle.getExportExcelPath();
				mapRet.put("fileUrl", fileUrl);
				strRet = jacksonUtil.Map2json(mapRet);
			} else {
				errMsg[0] = "1";
				errMsg[1] = "导出失败";
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRet;
	}

	// 评审规则保存
	public String saveRuleBasic(Map<String, Object> mapParams, String[] errMsg) {
		String strRet = "";

		try {

			// 当前登录人
			String currentOprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

			String classId = (String) mapParams.get("classId");
			String batchId = (String) mapParams.get("batchId");
			// String dqpsStatus = (String) mapParams.get("dqpsStatus");

			PsTzClpsGzTblKey psTzClpsGzTblKey = new PsTzClpsGzTblKey();
			psTzClpsGzTblKey.setTzClassId(classId);
			psTzClpsGzTblKey.setTzApplyPcId(batchId);

			PsTzClpsGzTbl psTzClpsGzTbl = psTzClpsGzTblMapper.selectByPrimaryKey(psTzClpsGzTblKey);

			if (psTzClpsGzTbl == null) {
				psTzClpsGzTbl = new PsTzClpsGzTbl();
				psTzClpsGzTbl.setTzClassId(classId);
				psTzClpsGzTbl.setTzApplyPcId(batchId);
				// psTzClpsGzTbl.setTzDqpyZt(dqpsStatus);
				psTzClpsGzTbl.setRowAddedDttm(new Date());
				psTzClpsGzTbl.setRowAddedOprid(currentOprid);
				psTzClpsGzTbl.setRowLastmantDttm(new Date());
				psTzClpsGzTbl.setRowLastmantOprid(currentOprid);
				psTzClpsGzTblMapper.insertSelective(psTzClpsGzTbl);
			} else {
				/*
				 * psTzClpsGzTbl.setTzDqpyZt(dqpsStatus);
				 * psTzClpsGzTbl.setRowLastmantDttm(new Date());
				 * psTzClpsGzTbl.setRowLastmantOprid(currentOprid);
				 * psTzClpsGzTblMapper.updateByPrimaryKeySelective(psTzClpsGzTbl
				 * );
				 */
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRet;
	}

	// 考生数据保存
	public String saveExamineeInfo(Map<String, Object> mapParams, String[] errMsg) {
		String strRet = "";

		try {

			// 当前登录人
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

			String classId = (String) mapParams.get("classId");
			String batchId = (String) mapParams.get("batchId");
			String appinsId = (String) mapParams.get("appinsId");

			PsTzClpsKshTblKey psTzClpsKshTblKey = new PsTzClpsKshTblKey();
			psTzClpsKshTblKey.setTzClassId(classId);
			psTzClpsKshTblKey.setTzApplyPcId(batchId);
			psTzClpsKshTblKey.setTzAppInsId(Long.valueOf(appinsId));

			PsTzClpsKshTbl psTzClpsKshTbl = psTzClpsKshTblMapper.selectByPrimaryKey(psTzClpsKshTblKey);
			if (psTzClpsKshTbl == null) {
				psTzClpsKshTbl = new PsTzClpsKshTbl();
				psTzClpsKshTbl.setTzClassId(classId);
				psTzClpsKshTbl.setTzApplyPcId(batchId);
				psTzClpsKshTbl.setTzAppInsId(Long.valueOf(appinsId));
				psTzClpsKshTbl.setTzMshiZgflg("W"); // 待定
				psTzClpsKshTbl.setRowAddedDttm(new Date());
				psTzClpsKshTbl.setRowAddedOprid(oprid);
				psTzClpsKshTbl.setRowLastmantDttm(new Date());
				psTzClpsKshTbl.setRowLastmantOprid(oprid);
				psTzClpsKshTblMapper.insertSelective(psTzClpsKshTbl);
			} else {
				psTzClpsKshTblMapper.updateByPrimaryKeySelective(psTzClpsKshTbl);
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRet;
	}

	
	/**
	 * 可配置搜索sql
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	private String tzGetSearchSql(String strParams, String[] errorMsg){
		String strRet = "";
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("searchSql", "");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			/*可配置搜索查询语句*/
			String[] resultFldArray = {"TZ_APP_INS_ID"};
			
			String[][] orderByArr=null;
			
			String searchSql = fliterForm.getQuerySQL(resultFldArray,orderByArr,strParams,errorMsg);
			
			rtnMap.replace("searchSql", searchSql);
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.getMessage();
		}
		
		strRet = jacksonUtil.Map2json(rtnMap);
		return strRet;
	}
	
	/**
	 * 下载导出文件
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	private String tzDownloadExpFile(String strParams, String[] errorMsg){
		Map<String,Object> mapRet = new HashMap<String,Object>();
		mapRet.put("filePath", "");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try{
			jacksonUtil.json2Map(strParams);
			//下载导出excel
			String filePath = "";
			String strProcInsId = jacksonUtil.getString("procInsId");
			if(!"".equals(strProcInsId) && strProcInsId != null){
				int procInsId = Integer.parseInt(strProcInsId);
				
				PsTzExcelDattT psTzExcelDattT = psTzExcelDattTMapper.selectByPrimaryKey(procInsId);
				if(psTzExcelDattT != null){
					filePath = psTzExcelDattT.getTzFwqFwlj();
					if(!"".equals(filePath) && filePath != null){
						filePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
						+ request.getContextPath() + filePath;
					}
				}
			}
			mapRet.put("filePath", filePath);
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "系统错误。"+e.getMessage();
		}
		
		return jacksonUtil.Map2json(mapRet);
	}
	
	
	/**
	 * 导出考生评议数据
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	private String tzExportExcelFile(String strParams, String[] errorMsg){
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try{
			jacksonUtil.json2Map(strParams);
			
			// 班级ID
			String classId = jacksonUtil.getString("classId");
			String batchId = jacksonUtil.getString("batchId");
			String fileName = jacksonUtil.getString("fileName");
			
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			String downloadPath = getSysHardCodeVal.getDownloadPath();
			
			// excel存储路径
			String eventExcelPath = "/material/xlsx";
			
			// 完整的存储路径
			String expDirPath = downloadPath + eventExcelPath + "/" + getDateNow();
			String absexpDirPath = request.getServletContext().getRealPath(expDirPath);
			
			/*生成运行控制ID*/
			SimpleDateFormat dateFormate = new SimpleDateFormat("yyyyMMddHHmmss");
		    String s_dt = dateFormate.format(new Date());
			String runCntlId = "CLPSKS" + s_dt + "_" + getSeqNum.getSeqNum("TZ_REVIEW_CL_COM", "CLPSKS_EXPORT");
			
			//与自动初筛导出共用参数表
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
			
			BaseEngine tmpEngine = tzSQLObject.createEngineProcess(currentOrgId, "TZ_CLPSKS_EXP_PROC");
			//指定调度作业的相关参数
			EngineParameters schdProcessParameters = new EngineParameters();

			schdProcessParameters.setBatchServer("");
			schdProcessParameters.setCycleExpression("");
			schdProcessParameters.setLoginUserAccount(currentAccountId);
			schdProcessParameters.setPlanExcuteDateTime(new Date());
			schdProcessParameters.setRunControlId(runCntlId);
			
			//调度作业
			tmpEngine.schedule(schdProcessParameters);
			
			// 进程实例id;
			int processinstance = tmpEngine.getProcessInstanceID();
			if(processinstance>0){
				PsTzExcelDrxxT psTzExcelDrxxT = new PsTzExcelDrxxT();
				psTzExcelDrxxT.setProcessinstance(processinstance);
				psTzExcelDrxxT.setTzComId("TZ_REVIEW_CL_COM");
				psTzExcelDrxxT.setTzPageId("TZ_CLPS_KS_STD");
				//存放班级ID-批次ID
				psTzExcelDrxxT.setTzDrLxbh(classId+"-"+batchId);
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
				
				
			}
			
			
			
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "导出失败。" + e.getMessage();
		}

		return strRet;
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
