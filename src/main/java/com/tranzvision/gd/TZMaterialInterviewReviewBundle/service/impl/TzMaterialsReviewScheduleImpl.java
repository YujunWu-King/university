package com.tranzvision.gd.TZMaterialInterviewReviewBundle.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
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
import com.tranzvision.gd.TZMaterialInterviewReviewBundle.dao.PsTzCpfbBzhTblMapper;
import com.tranzvision.gd.TZMaterialInterviewReviewBundle.dao.PsTzQttjBzhTblMapper;
import com.tranzvision.gd.TZMaterialInterviewReviewBundle.dao.psTzClpsPwTblMapper;
import com.tranzvision.gd.TZMaterialInterviewReviewBundle.dao.psTzClpwpslsTblMapper;
import com.tranzvision.gd.TZMaterialInterviewReviewBundle.dao.PsTzClpsLogTblMapper;

import com.tranzvision.gd.TZMaterialInterviewReviewBundle.model.PsTzCpfbBzhTbl;
import com.tranzvision.gd.TZMaterialInterviewReviewBundle.model.PsTzQttjBzhTbl;
import com.tranzvision.gd.TZMaterialInterviewReviewBundle.model.psTzClpsPwTbl;
import com.tranzvision.gd.TZMaterialInterviewReviewBundle.model.psTzClpwpslsTbl;
import com.tranzvision.gd.TZMaterialInterviewReviewBundle.model.PsTzClpsLogTbl;

import com.tranzvision.gd.TZMbaPwClpsBundle.dao.PsTzClpsGzTblMapper;
import com.tranzvision.gd.TZMbaPwClpsBundle.dao.PsTzClpsKshTblMapper;
import com.tranzvision.gd.TZMbaPwClpsBundle.dao.PsTzKsclpslsTblMapper;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzClpsGzTbl;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzClpsKshTbl;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzKsclpslsTbl;

import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.security.TzFilterIllegalCharacter;

/**
 * 材料评审进度，原TZ_GD_CLPS_PKG:TZ_GD_SCHE_CLS
 * 
 * @author yuds 时间：2017年2月28日
 */
@Service("com.tranzvision.gd.TZMaterialInterviewReviewBundle.service.impl.TzMaterialsReviewScheduleImpl")
public class TzMaterialsReviewScheduleImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	@Autowired
	private TZGDObject tzGdObject;
	@Autowired
	private psTzClpwpslsTblMapper psTzClpwpslsTblMapper;
	@Autowired
	private psTzClpsPwTblMapper psTzClpsPwTblMapper;
	@Autowired
	private PsTzClpsGzTblMapper PsTzClpsGzTblMapper;
	@Autowired
	private PsTzKsclpslsTblMapper PsTzKsclpslsTblMapper;
	@Autowired
	private PsTzClpsKshTblMapper PsTzClpsKshTblMapper;
	@Autowired
	private PsTzCpfbBzhTblMapper PsTzCpfbBzhTblMapper;
	@Autowired
	private PsTzQttjBzhTblMapper psTzQttjBzhTblMapper;
	@Autowired
	private PsTzClpsLogTblMapper psTzClpsLogTblMapper;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private FliterForm fliterForm;
	
	@SuppressWarnings("unchecked")
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errMsg) {
		String strResponse = "\"failure\"";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			String strType = jacksonUtil.getString("type");
			switch (strType) {
			case "judgeInfo":
				strResponse = this.queryJudgeInfo(strParams, errMsg);
				break;
			case "stuList":
				strResponse = this.queryStuList(strParams, numLimit, numStart, errMsg);
				break;
			case "chart":
				strResponse = this.queryPwDfChartData(strParams, errMsg);
				break;
			case "getPwDfData":
				strResponse = this.tzGetPwDfTotalPjfGridData(strParams, errMsg);
				break;
			default:
				/* 传入错误的类型 */
			}
		} catch (Exception e) {
			/* doNothing */
		}
		return strResponse;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		try {
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

			JacksonUtil jacksonUtil = new JacksonUtil();
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				String strClassID = jacksonUtil.getString("classID");
				String strBatchID = jacksonUtil.getString("batchID");
				String strDelibCount = jacksonUtil.getString("delibCount");
				// 评委信息内容;
				List<?> judgeInfoUtil = jacksonUtil.getList("judgeInfoUpdate");
				if (judgeInfoUtil != null && judgeInfoUtil.size() > 0) {
					for (Object obj : judgeInfoUtil) {
						Map<String, Object> mapFormData = (Map<String, Object>) obj;
						String strPwOprid = String.valueOf(mapFormData.get("judgeOprId"));

						String strSubmitYN = String.valueOf(mapFormData.get("submitYN"));
						String accountStatus = String.valueOf(mapFormData.get("accountStatus"));
						// 账户状态
						String strExist = "";
						String strExistSql = "SELECT 'Y' FROM PS_TZ_CLPWPSLS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_PWEI_OPRID=? AND TZ_CLPS_LUNC=?";
						strExist = sqlQuery.queryForObject(strExistSql, new Object[] { strClassID, strBatchID, strPwOprid, strDelibCount }, "String");
						psTzClpwpslsTbl psTzClpwpslsTbl = new psTzClpwpslsTbl();
						psTzClpwpslsTbl.setTzClassId(strClassID);
						psTzClpwpslsTbl.setTzApplyPcId(strBatchID);
						psTzClpwpslsTbl.setTzClpsLunc(Short.valueOf(strDelibCount));
						psTzClpwpslsTbl.setTzPweiOprid(strPwOprid);
						psTzClpwpslsTbl.setRowLastmantDttm(new Date());
						psTzClpwpslsTbl.setRowLastmantOprid(oprid);
						if (strSubmitYN != null && !"".equals(strSubmitYN)) {
							psTzClpwpslsTbl.setTzSubmitYn(strSubmitYN);
						}

						if ("Y".equals(strExist)) {
							psTzClpwpslsTblMapper.updateByPrimaryKeySelective(psTzClpwpslsTbl);
						} else {
							psTzClpwpslsTbl.setRowAddedDttm(new Date());
							psTzClpwpslsTbl.setRowAddedOprid(oprid);
							psTzClpwpslsTblMapper.insert(psTzClpwpslsTbl);
						}
						// 评委提交状态
						strExistSql = "SELECT 'Y' FROM PS_TZ_CLPS_PW_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_PWEI_OPRID=?";
						strExist = sqlQuery.queryForObject(strExistSql, new Object[] { strClassID, strBatchID, strPwOprid }, "String");
						psTzClpsPwTbl psTzClpsPwTbl = new psTzClpsPwTbl();
						psTzClpsPwTbl.setTzClassId(strClassID);
						psTzClpsPwTbl.setTzApplyPcId(strBatchID);
						psTzClpsPwTbl.setTzPweiOprid(strPwOprid);
						if (accountStatus != null && !"".equals(accountStatus)) {
							psTzClpsPwTbl.setTzPweiZhzt(accountStatus);
						}
						psTzClpsPwTbl.setRowLastmantDttm(new Date());
						psTzClpsPwTbl.setRowLastmantOprid(oprid);

						if ("Y".equals(strExist)) {
							psTzClpsPwTblMapper.updateByPrimaryKeySelective(psTzClpsPwTbl);
						} else {
							psTzClpsPwTbl.setRowAddedDttm(new Date());
							psTzClpsPwTbl.setRowAddedOprid(oprid);
							psTzClpsPwTblMapper.insert(psTzClpsPwTbl);
						}
					}
				}
				// 评委偏差内容;
				List<?> studentUtil = jacksonUtil.getList("studentInfo");
				if (studentUtil != null && studentUtil.size() > 0) {
					for (Object stuObj : studentUtil) {
						Map<String, Object> stuMap = (Map<String, Object>) stuObj;
						String strAppInsId = String.valueOf(stuMap.get("appInsId"));
						String strPwePc = String.valueOf(stuMap.get("pweiPC"));
						Double douPwiPc = Double.valueOf(strPwePc);
						String strExistsSql = "SELECT 'Y' FROM PS_TZ_CLPS_KSH_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=?";
						String strExFlg = sqlQuery.queryForObject(strExistsSql, new Object[] { strClassID, strBatchID, strAppInsId }, "String");
						PsTzClpsKshTbl psTzClpsKshTbl = new PsTzClpsKshTbl();
						psTzClpsKshTbl.setTzClassId(strClassID);
						psTzClpsKshTbl.setTzApplyPcId(strBatchID);
						psTzClpsKshTbl.setTzAppInsId(Long.parseLong(strAppInsId));
						psTzClpsKshTbl.setTzClpsPwjPc(BigDecimal.valueOf(douPwiPc));
						psTzClpsKshTbl.setRowLastmantDttm(new Date());
						psTzClpsKshTbl.setRowLastmantOprid(oprid);
						if ("Y".equals(strExFlg)) {
							PsTzClpsKshTblMapper.updateByPrimaryKeySelective(psTzClpsKshTbl);
						} else {
							psTzClpsKshTbl.setRowAddedDttm(new Date());
							psTzClpsKshTbl.setRowAddedOprid(oprid);
							PsTzClpsKshTblMapper.insert(psTzClpsKshTbl);
						}

					}
				}
				// 实时计算偏差、评委可见偏差、评委可见分布图
				String strCalPwPanC = jacksonUtil.getString("calPwPanC");
				if (!"Y".equals(strCalPwPanC)) {
					strCalPwPanC = "N";
				}
				String strJudgePanCFlg = jacksonUtil.getString("judgePanCFlg");
				if (!"Y".equals(strJudgePanCFlg)) {
					strJudgePanCFlg = "N";
				}
				String strJudgePyDataFlg = jacksonUtil.getString("judgePyDataFlg");
				if (!"Y".equals(strJudgePyDataFlg)) {
					strJudgePyDataFlg = "N";
				}

				PsTzClpsGzTbl psTzClpsGzTbl = new PsTzClpsGzTbl();
				psTzClpsGzTbl.setTzClassId(strClassID);
				psTzClpsGzTbl.setTzApplyPcId(strBatchID);
				psTzClpsGzTbl.setTzRealTimePwpc(strCalPwPanC);
				psTzClpsGzTbl.setTzPwkjPch(strJudgePanCFlg);
				psTzClpsGzTbl.setTzPwkjFbt(strJudgePyDataFlg);
				psTzClpsGzTbl.setRowLastmantDttm(new Date());
				psTzClpsGzTbl.setRowLastmantOprid(oprid);
				PsTzClpsGzTblMapper.updateByPrimaryKeySelective(psTzClpsGzTbl);
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		String strResponse = "\"failure\"";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			String strClassID = jacksonUtil.getString("classID");
			String strBatchID = jacksonUtil.getString("batchID");
			String strCurrentOrg = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			;

			String strScoreModalSql = "SELECT TZ_ZLPS_SCOR_MD_ID FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID=?";
			String strScoreModalId = sqlQuery.queryForObject(strScoreModalSql, new Object[] { strClassID }, "String");
			// 当前班级批次下已提交报名表报考人数
			String strTotalStudentSql = "SELECT COUNT(1) FROM PS_TZ_FORM_WRK_T A,PS_TZ_APP_INS_T B WHERE A.TZ_APP_INS_ID=B.TZ_APP_INS_ID AND B.TZ_APP_FORM_STA='U' AND A.TZ_CLASS_ID=? AND A.TZ_BATCH_ID=?";
			String strTotalStudentCount = sqlQuery.queryForObject(strTotalStudentSql, new Object[] { strClassID, strBatchID }, "String");
			// 当前批次人数
			String strCurBatchStuSql = "SELECT COUNT(*) FROM PS_TZ_CLPS_KSH_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
			String strCurBatchStuCount = sqlQuery.queryForObject(strCurBatchStuSql, new Object[] { strClassID, strBatchID }, "String");

			// 状态以及日期相关信息
			String strStatusSql = "SELECT TZ_PYKS_RQ,TZ_PYKS_SJ,TZ_PYJS_RQ,TZ_PYJS_SJ,(CASE TZ_DQPY_ZT WHEN 'A' THEN '进行中' WHEN 'B' THEN '已关闭' WHEN 'N' THEN '未开始' ELSE '未开始' END) STATUS,TZ_DQPY_LUNC,TZ_PWKJ_TJB,TZ_PWKJ_FBT,TZ_REAL_TIME_PWPC,TZ_PWKJ_PCH FROM PS_TZ_CLPS_GZ_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";

			String strStatus = "未开始", strDelibCount = "0";
			String strPwkjPch = "", strPwkjFbt = "", strPwRealTime = "";
			Map<String, Object> list2Map = sqlQuery.queryForMap(strStatusSql, new Object[] { strClassID, strBatchID });
			if (list2Map != null && list2Map.size() > 0) {
				strStatus = list2Map.get("STATUS") == null ? "" : String.valueOf(list2Map.get("STATUS"));
				strDelibCount = list2Map.get("TZ_DQPY_LUNC") == null ? "0" : String.valueOf(list2Map.get("TZ_DQPY_LUNC"));
				strPwkjPch = list2Map.get("TZ_PWKJ_PCH") == null ? "" : String.valueOf(list2Map.get("TZ_PWKJ_PCH"));
				strPwkjFbt = list2Map.get("TZ_PWKJ_FBT") == null ? "" : String.valueOf(list2Map.get("TZ_PWKJ_FBT"));
				strPwRealTime = list2Map.get("TZ_REAL_TIME_PWPC") == null ? "" : String.valueOf(list2Map.get("TZ_REAL_TIME_PWPC"));
			}
			/* 获取当前批次下考生的数量 */
			String sql10 = "SELECT COUNT(*) FROM PS_TZ_CLPS_KSH_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
			Integer materialStudents = sqlQuery.queryForObject(sql10, new Object[] { strClassID, strBatchID }, "Integer");
			// 每位考生要求评审人次
			String strTotalSql = "SELECT ifnull(TZ_MSPY_NUM,0) FROM PS_TZ_CLPS_GZ_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
			Integer judgeCount = sqlQuery.queryForObject(strTotalSql, new Object[] { strClassID, strBatchID }, "Integer");
			if (judgeCount == null) {
				judgeCount = 0;
			}
			// 要求评审总人次
			Integer numtotal = materialStudents * judgeCount;
			// 当前选择评审人次
			String strCurrentSumSQL = "SELECT SUM(TZ_PYKS_XX) FROM PS_TZ_CLPS_PW_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
			Integer intCurrentSum = sqlQuery.queryForObject(strCurrentSumSQL, new Object[] { strClassID, strBatchID }, "Integer");
			if (intCurrentSum == null) {
				intCurrentSum = 0;
			}

			// 得出当前班级，当前批次，当前轮次的已评审人次数
			String strClpsCountSql = "SELECT COUNT(*) FROM PS_TZ_KSCLPSLS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_SUBMIT_YN='Y' AND TZ_CLPS_LUNC=?";
			String strClpsCount = sqlQuery.queryForObject(strClpsCountSql, new Object[] { strClassID, strBatchID, strDelibCount }, "String");
			if (strClpsCount == null) {
				strClpsCount = "0";
			}
			String strProgress = strClpsCount + "/" + numtotal;
			//当前选择评委评议人次
			String strCurrentReviewSQL = "SELECT SUM(TZ_PYKS_XX) FROM PS_TZ_CLPS_PW_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
			String strCurrentReviewCount = sqlQuery.queryForObject(strCurrentReviewSQL, new Object[] { strClassID, strBatchID }, "String");
			if(strCurrentReviewCount==null){
				strCurrentReviewCount = "0";
			}

			Map<String, Object> mapData = new HashMap<String, Object>();
			mapData.put("classID", strClassID);
			mapData.put("batchID", strBatchID);
			mapData.put("totalStudents", strTotalStudentCount);
			mapData.put("materialStudents", strCurBatchStuCount);
			//状态
			mapData.put("status", strStatus);
			//评审进度
			mapData.put("progress", strProgress);
			//当前评审轮次
			mapData.put("delibCount", strDelibCount);
			//每个考生被几个评委评审
			mapData.put("reviewCount", judgeCount);
			//实时计算评委偏差
			mapData.put("calPwPanC", strPwRealTime);
			//评委可见偏差
			mapData.put("judgePanCFlg", strPwkjPch);
			//评委可见评议标准
			mapData.put("judgePyDataFlg", strPwkjFbt);
			// 要求评审人次
			mapData.put("requiredCount", numtotal);
			//当前选择评委评委人次
			mapData.put("currentReviewCount", strCurrentReviewCount);
			
			strResponse = jacksonUtil.Map2json(mapData);
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "100";
			errMsg[1] = e.toString();
		}
		return strResponse;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String tzOther(String strOprType, String strParams, String[] errMsg) {
		String strResponse = "\"failure\"";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			String strType = jacksonUtil.getString("type");

			switch (strType) {
			case "isJiSuanFenZhi":
				// 计算分值
				// strResponse = this.isJiSuanFenZhi(strParams, errMsg);
				break;
			case "check":
				// 计算平均差
				break;
			case "revoke":
				// 撤销评议数据
				strResponse = this.removeJudgeData(strParams, errMsg);
				break;
			case "calculate":
				// 计算偏差
				strResponse = this.calculate(strParams, errMsg);
				break;
			case "reStartNewOnclick":
				// 开启新一轮评审
				strResponse = this.btnClick(strParams, errMsg);
				break;
			case "closeReviewOnclick":
				// 关闭评审
				strResponse = this.btnClick(strParams, errMsg);
				break;
			case "reStartReviewOnclick":
				// 继续评审
				strResponse = this.btnClick(strParams, errMsg);
				break;
			case "savePyData":
				// 保存评议数据
				strResponse = this.savePyData(strParams, errMsg);
				break;
			case "submitPweiData":
				// 提交评委数据
				strResponse = this.saveSubmitPwData(strParams, errMsg);
				break;
			case "tmpImportData":
				//临时导入评议数据
				strResponse = this.tmpImportData();
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "100";
			errMsg[1] = e.toString();
		}
		return strResponse;
	}

	@SuppressWarnings("unchecked")
	public String isJiSuanFenZhi(String strParams, String[] errMsg) {
		String strResponse = "\"failure\"";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			String strClassID = jacksonUtil.getString("classID");
			String strBatchID = jacksonUtil.getString("batchID");
			String strCurrentOrg = tzLoginServiceImpl.getLoginedManagerOrgid(request);

			String strScoreModalSql = "SELECT TZ_ZLPS_SCOR_MD_ID FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID=?";
			String strScoreModalId = sqlQuery.queryForObject(strScoreModalSql, new Object[] { strClassID }, "String");

			String strTreeSql = "SELECT TREE_NAME FROM PS_TZ_RS_MODAL_TBL WHERE TZ_SCORE_MODAL_ID=? AND TZ_JG_ID=?";
			String strTreeName = sqlQuery.queryForObject(strTreeSql, new Object[] { strScoreModalId, strCurrentOrg }, "String");

			// String strTreeNodeSql = "SELECT TREE_NODE FROM PSTREENODE WHERE
			// TREE_NAME=? and PARENT_NODE_NUM=0";
			// String strScoreItemId = sqlQuery.queryForObject(strTreeNodeSql,
			// new Object[] { strTreeName }, "String");

			// 是否计算分数，默认为是
			// String strZfzSql = "select ifnull(A.TZ_JSFS,0) from
			// PS_TZ_RS_MODAL_TBL A,PS_TZ_BPH_VW1 b where A.TREE_NAME=? and
			// A.TZ_SCORE_MODAL_ID=B.TZ_SCORE_MODAL_ID AND B.TZ_SCORE_MODAL_ID=?
			// AND ROWNUM=1";
			// String strZfz = sqlQuery.queryForObject(strZfzSql, new Object[] {
			// strTreeName, strScoreModalId }, "String");
			String strZfz = "Y";
			Map<String, Object> mapData = new HashMap<String, Object>();
			mapData.put("ZFZ", strZfz);
			strResponse = jacksonUtil.Map2json(mapData);

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "100";
			errMsg[1] = e.toString();
		}
		return strResponse;
	}

	@SuppressWarnings("unchecked")
	public String tzGetPwDfTotalPjfGridData(String strParams, String[] errMsg) {
		String strResponse = "\"failure\"";

		JacksonUtil jacksonUtil = new JacksonUtil();

		try {
			String strCurrentOrg = tzLoginServiceImpl.getLoginedManagerOrgid(request);

			jacksonUtil.json2Map(strParams);
			String strClassID = jacksonUtil.getString("classID");
			String strBatchID = jacksonUtil.getString("batchID");
			String strPwLists = "";
			String[] selectPwList = new String[] {};
			try {
				/* 计算选中评委的分布 */
				strPwLists = jacksonUtil.getString("pwIds");
				selectPwList = strPwLists.split(",");
			} catch (Exception e) {
				/* 首次进入页面 */
			}
			String strCurrentOrgID = tzLoginServiceImpl.getLoginedManagerOrgid(request);

			String strScoreModalSql = "SELECT TZ_ZLPS_SCOR_MD_ID FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID=?";
			String strScoreModalId = sqlQuery.queryForObject(strScoreModalSql, new Object[] { strClassID }, "String");

			String strTreeName = "", strJsfs = "";
			/*
			 * String strTreeNameSql =
			 * "SELECT TREE_NAME,TZ_JSFS FROM PS_TZ_RS_MODAL_TBL WHERE TZ_SCORE_MODAL_ID=?"
			 * ; List<Map<String, Object>> treeList =
			 * sqlQuery.queryForList(strTreeNameSql, new Object[] {
			 * strScoreModalId }); if (treeList != null && treeList.size() > 0)
			 * { for (int i = 0; i < treeList.size(); i++) { strTreeName =
			 * (String) treeList.get(i).get("TREE_NAME"); strJsfs = (String)
			 * treeList.get(i).get("TZ_JSFS"); } }
			 */
			String strTreeNameSql = "SELECT TREE_NAME FROM PS_TZ_RS_MODAL_TBL WHERE TZ_SCORE_MODAL_ID=? AND TZ_JG_ID=?";
			strTreeName = sqlQuery.queryForObject(strTreeNameSql, new Object[] { strScoreModalId, strCurrentOrg }, "String");

			String strScoreItemId = "";
			String strScoreItemSql = "SELECT TREE_NODE FROM PSTREENODE WHERE TREE_NAME=? and PARENT_NODE_NUM=0";
			strScoreItemId = sqlQuery.queryForObject(strScoreItemSql, new Object[] { strTreeName }, "String");

			// 根据报考班级和批次取得当前轮次;
			Integer intDqpyLunc = 0;
			String strDqpyLuncSql = "SELECT ifnull(TZ_DQPY_LUNC,0) FROM PS_TZ_CLPS_GZ_TBL WHERE TZ_CLASS_ID = ? and TZ_APPLY_PC_ID=?";
			intDqpyLunc = sqlQuery.queryForObject(strDqpyLuncSql, new Object[] { strClassID, strBatchID }, "Integer");

			String strPwPjfHtml = "";
			int intFzNum = 4;
			/*
			 * if ("Y".equals(strJsfs)) { strPwPjfHtml = "{\"col05\":\"平均分\"}";
			 * intFzNum = intFzNum + 1; }
			 */
			strPwPjfHtml = ",{\"col05\":\"平均分\"}";
			intFzNum = intFzNum + 1;

			String strGridColHTML = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DF_FBDZ_BASE_HTML", strPwPjfHtml);

			int intFbzbNum = 2;
			String strGridGoalColHTML = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DF_FBZB_BASE_HTML");
			String strGridGoalColHTML2 = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DF_FBZB_BASE_HTML");
			strGridGoalColHTML2 = strGridGoalColHTML2 + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DF_FBDZ_ITEM_HTML", "col02", "平均分");

			String strGridGoalColHTML3 = "";

			String strBlHtml = "", strWcHtml = "";
			String strCjModalId = "", strFbdzId = "";
			if (strClassID != null && !"".equals(strClassID)) {
				String strCjModalSql = "SELECT TZ_ZLPS_SCOR_MD_ID FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID=?";
				strCjModalId = sqlQuery.queryForObject(strCjModalSql, new Object[] { strClassID }, "String");
				if (strCjModalId != null && !"".equals(strCjModalId)) {
					String strFbdzSql = "SELECT TZ_M_FBDZ_ID FROM PS_TZ_RS_MODAL_TBL WHERE TZ_SCORE_MODAL_ID=? AND TZ_JG_ID=?";
					strFbdzId = sqlQuery.queryForObject(strFbdzSql, new Object[] { strCjModalId, strCurrentOrgID }, "String");
					if (strFbdzId != null && !"".equals(strFbdzId)) {
						String strFbdzMxId = "", strFbdzMxSm = "";
						String strFzdzSql = "SELECT TZ_M_FBDZ_MX_ID,TZ_M_FBDZ_MX_SM FROM PS_TZ_FBDZ_MX_TBL WHERE TZ_M_FBDZ_ID=? ORDER BY TZ_M_FBDZ_MX_XH ASC";
						List<Map<String, Object>> fzdzList = sqlQuery.queryForList(strFzdzSql, new Object[] { strFbdzId });
						if (fzdzList != null && fzdzList.size() > 0) {
							for (Object obj1 : fzdzList) {
								Map<String, Object> result1 = (Map<String, Object>) obj1;
								strFbdzMxId = result1.get("TZ_M_FBDZ_MX_ID") == null ? "" : String.valueOf(result1.get("TZ_M_FBDZ_MX_ID"));
								strFbdzMxSm = result1.get("TZ_M_FBDZ_MX_SM") == null ? "" : String.valueOf(result1.get("TZ_M_FBDZ_MX_SM"));
								intFzNum = intFzNum + 1;

								String colXh = "0" + intFzNum;
								String strFzStr = "col" + this.right(colXh, 2);
								strGridColHTML = strGridColHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DF_FBDZ_ITEM_HTML", strFzStr, strFbdzMxSm);

								intFbzbNum = intFbzbNum + 1;
								colXh = "0" + intFbzbNum;
								strFzStr = "col" + this.right(colXh, 2);
								strGridGoalColHTML = strGridGoalColHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DF_FBDZ_ITEM_HTML", strFzStr, strFbdzMxSm);
								strGridGoalColHTML2 = strGridGoalColHTML2 + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DF_FBDZ_ITEM_HTML", strFzStr, "mx_" + strFbdzMxId);
								if ("".equals(strGridGoalColHTML3)) {
									strGridGoalColHTML3 = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DF_FBDZ_ITEM_HTML", "mx_" + strFbdzMxId, strFbdzMxSm);
								} else {
									strGridGoalColHTML3 = strGridGoalColHTML3 + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DF_FBDZ_ITEM_HTML", "mx_" + strFbdzMxId, strFbdzMxSm);
								}
								String strSql1 = "SELECT TZ_BZFB_BL,cast(TZ_YXWC_NUM as SIGNED) TZ_YXWC_NUM FROM PS_TZ_CPFB_BZH_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_SCORE_MODAL_ID=? AND TZ_SCORE_ITEM_ID=? AND TZ_M_FBDZ_ID=? AND TZ_M_FBDZ_MX_ID=?";
								List<Map<String, Object>> fbDataList = sqlQuery.queryForList(strSql1, new Object[] { strClassID, strBatchID, strCjModalId, strScoreItemId, strFbdzId, strFbdzMxId });
								String strBl = "", strWc = "";
								if (fbDataList != null && fbDataList.size() > 0) {
									for (Object objFb : fbDataList) {
										Map<String, Object> resultFb = (Map<String, Object>) objFb;
										strBl = resultFb.get("TZ_BZFB_BL") == null ? "" : String.valueOf(resultFb.get("TZ_BZFB_BL"));
										strWc = resultFb.get("TZ_YXWC_NUM") == null ? "" : String.valueOf(resultFb.get("TZ_YXWC_NUM"));
									}
								}
								// strBlHtml = strBlHtml + "," +
								// tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DF_FBDZ_ITEM_HTML",
								// strFzStr,strBl);
								strBlHtml = strBlHtml + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DF_FBDZ_ITEM_HTML", "mx_" + strFbdzMxId, strBl);
								// strWcHtml = strWcHtml + "," +
								// tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DF_FBDZ_ITEM_HTML",
								// strFzStr,strBl);
								strWcHtml = strWcHtml + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DF_FBDZ_ITEM_HTML", "mx_" + strFbdzMxId, strWc);
							}
						}
					}
				}

				String strChartFieldsHTML = "";
				String strGridHtml = "";
				String strGridGoalHtml = "";
				String strGridDataHTML = "";

				String strPwOprid = "", strPwDlId = "", strFirstName = "";
				String strPwSql = "SELECT TZ_PWEI_OPRID,(SELECT TZ_DLZH_ID FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=A.TZ_PWEI_OPRID limit 0,1) TZ_DLZH_ID,(SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL R WHERE R.OPRID=A.TZ_PWEI_OPRID) TZ_REALNAME FROM PS_TZ_CLPS_PW_TBL A WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
				List<Map<String, Object>> pwList = sqlQuery.queryForList(strPwSql, new Object[] { strClassID, strBatchID });
				double saveScore = 0.0;
				if (pwList != null && pwList.size() > 0) {
					int intTb = 0;
					// 为最后一行做准备
					int intSize = 1;
					int intTotalWc = 0, intTotal = 0;
					double aveScoreTotal = 0.0;
					String strLastGridDataHTML = "";
					Map<String, Integer> sMaps = new HashMap<String, Integer>();
					Integer totalCounts = 0;
					for (Object obj2 : pwList) {
						strPwOprid = "";
						strPwDlId = "";
						strFirstName = "";

						Map<String, Object> result2 = (Map<String, Object>) obj2;
						strPwOprid = result2.get("TZ_PWEI_OPRID") == null ? "" : String.valueOf(result2.get("TZ_PWEI_OPRID"));
						strPwDlId = result2.get("TZ_DLZH_ID") == null ? "" : String.valueOf(result2.get("TZ_DLZH_ID"));
						strFirstName = result2.get("TZ_PWEI_OPRID") == null ? "" : String.valueOf(result2.get("TZ_REALNAME"));

						intFzNum = 1;
						String colName = "0" + intFzNum;
						String strFzValue = "col" + this.right(colName, 2);
						// 登录账号
						strGridDataHTML = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DF_FBDZ_ITEM_HTML", strFzValue, strPwDlId);
						if (intSize == pwList.size()) {
							strLastGridDataHTML = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DF_FBDZ_ITEM_HTML", strFzValue, "");
						}
						// 评委姓名
						intFzNum = intFzNum + 1;
						colName = "0" + intFzNum;
						strFzValue = "col" + this.right(colName, 2);
						strGridDataHTML = strGridDataHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DF_FBDZ_ITEM_HTML", strFzValue, strFirstName);
						if (intSize == pwList.size()) {
							strLastGridDataHTML = strLastGridDataHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DF_FBDZ_ITEM_HTML", strFzValue, "评委（" + selectPwList.length + "）");
						}
						// 完成数量
						String strWc, tmpWc;
						String strWcNumSql = "SELECT COUNT(*) FROM PS_TZ_KSCLPSLS_TBL WHERE TZ_CLASS_ID = ? AND TZ_APPLY_PC_ID = ? AND TZ_PWEI_OPRID = ? AND TZ_CLPS_LUNC=? AND TZ_SUBMIT_YN <> 'C'";
						strWc = sqlQuery.queryForObject(strWcNumSql, new Object[] { strClassID, strBatchID, strPwOprid, intDqpyLunc }, "String");
						if (strWc == null) {
							strWc = "0";
							tmpWc = "0";
						} else {
							tmpWc = strWc;
						}
						/*// 要求评委评审考生数量
						String strTmpSql1 = "SELECT ifnull(TZ_PYKS_XX,0) FROM PS_TZ_CLPS_PW_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_PWEI_OPRID=?";
						Integer intTmp1 = sqlQuery.queryForObject(strTmpSql1, new Object[] { strClassID, strBatchID, strPwOprid }, "Integer");
						if (intTmp1 == null) {
							intTmp1 = 0;
						}
						// 要求每个考生被评审数量
						String strTmpSql2 = "SELECT ifnull(TZ_MSPY_NUM,0) FROM PS_TZ_CLPS_GZ_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
						Integer intTmp2 = sqlQuery.queryForObject(strTmpSql2, new Object[] { strClassID, strBatchID }, "Integer");
						if (intTmp2 == null) {
							intTmp2 = 0;
						}*/
						//提交数量
						String strTmpSql01 = "SELECT COUNT(DISTINCT TZ_APP_INS_ID) FROM PS_TZ_KSCLPSLS_TBL WHERE TZ_CLASS_ID = ? AND TZ_APPLY_PC_ID = ? AND TZ_PWEI_OPRID = ? AND TZ_CLPS_LUNC = ? AND  TZ_SUBMIT_YN = 'Y'";
						Integer intSubmitCount = sqlQuery.queryForObject(strTmpSql01, new Object[]{strClassID, strBatchID, strPwOprid, intDqpyLunc}, "Integer");
						if(intSubmitCount==null){
							intSubmitCount = 0;
						}
						// 如果该评委被选中计算平均分布，拼接最后一行数据
						if (selectPwList.length > 0) {
							for (String sA : selectPwList) {
								String tmpPwOprid = sqlQuery.queryForObject("SELECT OPRID FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_DLZH_ID=?", new Object[] { sA }, "String");
								if (tmpPwOprid.equals(strPwOprid)) {

									intTotalWc = intTotalWc + Integer.valueOf(strWc);
									intTotal = intTotal + intSubmitCount;
									break;
								}
							}
						}
						
						/*//完成数量
						String strTmpSql02 = "SELECT COUNT(DISTINCT TZ_APP_INS_ID) FROM PS_TZ_KSCLPSLS_TBL WHERE TZ_CLASS_ID = ? AND TZ_APPLY_PC_ID = ? AND TZ_PWEI_OPRID = ? AND TZ_CLPS_LUNC = ? AND  TZ_SUBMIT_YN <> 'C'";
						Integer intCompleteCount = sqlQuery.queryForObject(strTmpSql02, new Object[]{strClassID, strBatchID, strPwOprid, intDqpyLunc}, "Integer");
						if(intCompleteCount==null){
							intCompleteCount = 0;
						}*/
						strWc = intSubmitCount + "/" + tmpWc;
						//strWc = strWc + "/" + (intTmp1 * intTmp2);
						intFzNum = intFzNum + 1;
						colName = "0" + intFzNum;
						strFzValue = "col" + this.right(colName, 2);
						strGridDataHTML = strGridDataHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DF_FBDZ_ITEM_HTML", strFzValue, strWc);

						if (intSize == pwList.size()) {
							strLastGridDataHTML = strLastGridDataHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DF_FBDZ_ITEM_HTML", strFzValue, intTotal + "/" + intTotalWc);
						}
						// 提交状态
						String strSubmitZt = "", strSubmitZtDesc = "未提交";
						String strSubmitSql = "SELECT TZ_SUBMIT_YN FROM PS_TZ_CLPWPSLS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_PWEI_OPRID=? AND TZ_CLPS_LUNC=?";
						strSubmitZt = sqlQuery.queryForObject(strSubmitSql, new Object[] { strClassID, strBatchID, strPwOprid, intDqpyLunc }, "String");
						if ("Y".equals(strSubmitZt)) {
							strSubmitZtDesc = "已提交";
						}
						intFzNum = intFzNum + 1;
						colName = "0" + intFzNum;
						strFzValue = "col" + this.right(colName, 2);
						strGridDataHTML = strGridDataHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DF_FBDZ_ITEM_HTML", strFzValue, strSubmitZtDesc);
						if (intSize == pwList.size()) {
							strLastGridDataHTML = strLastGridDataHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DF_FBDZ_ITEM_HTML", strFzValue, "-");
						}
						// 平均分
						intFzNum = intFzNum + 1;
						colName = "0" + intFzNum;
						strFzValue = "col" + this.right(colName, 2);
						String strAveSql = "SELECT SUM( B.TZ_SCORE_NUM) FROM PS_TZ_CP_PW_KS_TBL A,PS_TZ_CJX_TBL B ,PS_TZ_KSCLPSLS_TBL C WHERE A.TZ_SCORE_INS_ID = B.TZ_SCORE_INS_ID AND A.TZ_APPLY_PC_ID = C.TZ_APPLY_PC_ID AND A.TZ_PWEI_OPRID = C.TZ_PWEI_OPRID AND A.TZ_APP_INS_ID = C.TZ_APP_INS_ID AND C.TZ_SUBMIT_YN <> 'C' AND A.TZ_CLASS_ID=?  AND A.TZ_APPLY_PC_ID = ? AND A.TZ_PWEI_OPRID = ? AND B.TZ_SCORE_ITEM_ID = ? AND C.TZ_CLPS_LUNC = ?";
						Double sumScore = sqlQuery.queryForObject(strAveSql, new Object[] { strClassID, strBatchID, strPwOprid, "Total", intDqpyLunc }, "Double");
						if (sumScore == null) {
							sumScore = 0.0;
						}
						DecimalFormat df = new DecimalFormat("######0.00");
						String tmpPjf2 = "";
						if ("0".equals(tmpWc) || sumScore == 0) {
							tmpPjf2 = "0";
						} else {
							int sI = Integer.valueOf(tmpWc);
							Double tmoD = sumScore / Double.valueOf(sI);
							tmpPjf2 = df.format(tmoD);
						}

						if (strPwLists != null && this.find(selectPwList, strPwDlId) >= 0) {
							aveScoreTotal = aveScoreTotal + Double.valueOf(tmpPjf2);
						}

						strGridDataHTML = strGridDataHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DF_FBDZ_ITEM_HTML", strFzValue, tmpPjf2);
						if (intSize == pwList.size()) {
							
							if (selectPwList.length > 0) {
								saveScore = aveScoreTotal / selectPwList.length;
							}
							strLastGridDataHTML = strLastGridDataHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DF_FBDZ_ITEM_HTML", strFzValue, df.format(saveScore));
						}
						/*
						 * if ("Y".equals(strJsfs)) { intFzNum = intFzNum + 1;
						 * colName = "0" + intFzNum; strFzValue = "col" +
						 * this.right(colName,2); strGridDataHTML =
						 * strGridDataHTML + "," + tzGdObject.getHTMLText(
						 * "HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DF_FBDZ_ITEM_HTML",
						 * strFzValue,"1.20"); }
						 */

						// 取得分布统计人数
						String strMFbdzMxId = "", strMFbdzMxName = "", strMFbdzMxXX = "", strMFbdzMxXxJx = "", strMFbdzMxSx = "", strMFbdzMxSxJx = "";
						String strFbdzSql = "SELECT  TZ_M_FBDZ_MX_ID,TZ_M_FBDZ_MX_NAME,TZ_M_FBDZ_MX_XX,TZ_M_FBDZ_MX_XX_JX,TZ_M_FBDZ_MX_SX_JX,TZ_M_FBDZ_MX_SX  FROM PS_TZ_FBDZ_MX_TBL WHERE TZ_M_FBDZ_ID = ? ORDER BY TZ_M_FBDZ_MX_XH ASC";
						List<Map<String, Object>> fbdzList = sqlQuery.queryForList(strFbdzSql, new Object[] { strFbdzId });

						if (fbdzList != null && fbdzList.size() > 0) {
							// 汇总项分布及比率
							Integer intAveIns = 0;

							for (Object obj3 : fbdzList) {
								Map<String, Object> result3 = (Map<String, Object>) obj3;
								strMFbdzMxId = result3.get("TZ_M_FBDZ_MX_ID") == null ? "" : String.valueOf(result3.get("TZ_M_FBDZ_MX_ID"));
								strMFbdzMxName = result3.get("TZ_M_FBDZ_MX_NAME") == null ? "" : String.valueOf(result3.get("TZ_M_FBDZ_MX_NAME"));
								strMFbdzMxXX = result3.get("TZ_M_FBDZ_MX_XX") == null ? "" : String.valueOf(result3.get("TZ_M_FBDZ_MX_XX"));
								strMFbdzMxXxJx = result3.get("TZ_M_FBDZ_MX_XX_JX") == null ? "" : String.valueOf(result3.get("TZ_M_FBDZ_MX_XX_JX"));
								strMFbdzMxSx = result3.get("TZ_M_FBDZ_MX_SX") == null ? "" : String.valueOf(result3.get("TZ_M_FBDZ_MX_SX"));
								strMFbdzMxSxJx = result3.get("TZ_M_FBDZ_MX_ID") == null ? "" : String.valueOf(result3.get("TZ_M_FBDZ_MX_SX_JX"));

								String tmpTotalSql = "SELECT ifnull(COUNT(A.TZ_APP_INS_ID),0) FROM PS_TZ_CP_PW_KS_TBL A ,PS_TZ_CJX_TBL B ,PS_TZ_KSCLPSLS_TBL C  WHERE A.TZ_SCORE_INS_ID = B.TZ_SCORE_INS_ID  AND A.TZ_CLASS_ID = C.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID = C.TZ_APPLY_PC_ID AND A.TZ_PWEI_OPRID = C.TZ_PWEI_OPRID AND A.TZ_APP_INS_ID = C.TZ_APP_INS_ID AND C.TZ_SUBMIT_YN <> 'C'  AND A.TZ_CLASS_ID = ? AND A.TZ_APPLY_PC_ID = ? AND A.TZ_PWEI_OPRID =? AND B.TZ_SCORE_ITEM_ID = ?  AND C.TZ_CLPS_LUNC = ?";
								Integer tmpTotal = sqlQuery.queryForObject(tmpTotalSql, new Object[] { strClassID, strBatchID, strPwOprid, strScoreItemId, intDqpyLunc }, "Integer");

								String strDange = "0";
								String numDangeSql = "SELECT ifnull(COUNT(A.TZ_APP_INS_ID),0) FROM PS_TZ_CP_PW_KS_TBL A ,PS_TZ_CJX_TBL B ,PS_TZ_KSCLPSLS_TBL C  WHERE A.TZ_SCORE_INS_ID = B.TZ_SCORE_INS_ID  AND A.TZ_CLASS_ID = C.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID = C.TZ_APPLY_PC_ID AND A.TZ_PWEI_OPRID = C.TZ_PWEI_OPRID AND A.TZ_APP_INS_ID = C.TZ_APP_INS_ID AND C.TZ_SUBMIT_YN <> 'C'  AND A.TZ_CLASS_ID = ? AND A.TZ_APPLY_PC_ID = ? AND A.TZ_PWEI_OPRID =? AND B.TZ_SCORE_ITEM_ID = ?  AND C.TZ_CLPS_LUNC = ? AND B.TZ_SCORE_NUM " + strMFbdzMxXxJx + strMFbdzMxXX + "AND B.TZ_SCORE_NUM " + strMFbdzMxSxJx + strMFbdzMxSx;
								strDange = sqlQuery.queryForObject(numDangeSql, new Object[] { strClassID, strBatchID, strPwOprid, strScoreItemId, intDqpyLunc }, "String");

								intFzNum = intFzNum + 1;
								colName = "0" + intFzNum;
								strFzValue = "col" + this.right(colName, 2);
								// 计算该分布比率个数在总总分占比中所占个数
								String douPercent = "0";

								if (tmpTotal == 0 || "0".equals(strDange)) {

								} else {
									Double x = Integer.valueOf(strDange) * 1.0;
									Double tmpPercent = x / tmpTotal;
									tmpPercent = tmpPercent * 100;
									douPercent = df.format(tmpPercent) + "%";
								}

								if (strPwLists != null && this.find(selectPwList, strPwDlId) >= 0) {
									Integer tmpInt = sMaps.get(strMFbdzMxId) == null ? 0 : sMaps.get(strMFbdzMxId);
									tmpInt = tmpInt + Integer.valueOf(strDange);

									totalCounts = totalCounts + Integer.valueOf(strDange);
									sMaps.put(strMFbdzMxId, tmpInt);
								}
								strGridDataHTML = strGridDataHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DF_FBDZ_ITEM_HTML", strFzValue, strDange + "（" + douPercent + "）");
								if (intSize == pwList.size()) {
									Integer sInt = sMaps.get(strMFbdzMxId) == null ? 0 : sMaps.get(strMFbdzMxId);
									String stmpPercent = "0";

									if (sInt == 0 || intTotal == 0) {

									} else {
										double sDoubleVe = sInt * 1.0 / intTotal;
										double dtmpPercent = sDoubleVe * 100;
										stmpPercent = df.format(dtmpPercent) + "%";
									}
									
									strLastGridDataHTML = strLastGridDataHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DF_FBDZ_ITEM_HTML", strFzValue, sInt + "（" + stmpPercent + "）");
								}
							}
						}
						if (strGridHtml != null && !"".equals(strGridHtml)) {
							strGridHtml = strGridHtml + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DATA_IT_HTML", strPwOprid, strGridDataHTML);
						} else {
							strGridHtml = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DATA_IT_HTML", strPwOprid, strGridDataHTML);
						}
						if (intSize == pwList.size()) {
							strGridHtml = strGridHtml + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DATA_IT_HTML", "-", strLastGridDataHTML);
						}
						// 图表
						intTb = intTb + 1;
						intSize = intSize + 1;
						if (strChartFieldsHTML != null && !"".equals(strChartFieldsHTML)) {
							strChartFieldsHTML = strChartFieldsHTML + ",{" + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMS_PWDFTJ_CHART_ITEM_HTML", "pw_field", "data" + intTb);
						} else {
							strChartFieldsHTML = "{" + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMS_PWDFTJ_CHART_ITEM_HTML", "pw_field", "data" + intTb);
						}
						strChartFieldsHTML = strChartFieldsHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMS_PWDFTJ_CHART_ITEM_HTML", "pw_name", strFirstName);
						strChartFieldsHTML = strChartFieldsHTML + "}";
					}
				}
				String strAveSql = "SELECT TZ_TJL_BZH,TZ_TJL_WCZ FROM PS_TZ_QTTJ_BZH_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_SCORE_MODAL_ID=? AND TZ_SCORE_ITEM_ID=? AND TZ_TJGN_ID=?";
				Map<String, Object> s2Map = sqlQuery.queryForMap(strAveSql, new Object[] { strClassID, strBatchID, strCjModalId, "TOTAL", "001" });
				String strAve2 = "0.00", strWcAve2 = "0.00";
				if (s2Map != null) {
					strAve2 = s2Map.get("TZ_TJL_BZH") == null ? "0.00" : String.valueOf(s2Map.get("TZ_TJL_BZH"));
					strWcAve2 = s2Map.get("TZ_TJL_WCZ") == null ? "0.00" : String.valueOf(s2Map.get("TZ_TJL_WCZ"));
				}
				strGridGoalHtml = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_TJ_JSON_HTML", "bl", "比率", strBlHtml, strAve2) + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_TJ_JSON_HTML", "wc", "误差", strWcHtml, strWcAve2);
				;
				strResponse = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMSPS_PW_DF_JSON_HTML", strClassID, strBatchID, strGridColHTML, strGridHtml, strChartFieldsHTML, strGridGoalColHTML, strGridGoalHtml, strGridGoalColHTML2, strGridGoalColHTML3);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
		return strResponse;
	}

	@SuppressWarnings("unchecked")
	public String queryJudgeInfo(String strParams, String[] errMsg) {
		String strResponse = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			String strClassID = jacksonUtil.getString("classID");
			String strBatchID = jacksonUtil.getString("batchID");
			String strCurrentOrg = tzLoginServiceImpl.getLoginedManagerOrgid(request);

			String strScoreModalSql = "SELECT TZ_ZLPS_SCOR_MD_ID FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID=?";
			String strScoreModalId = sqlQuery.queryForObject(strScoreModalSql, new Object[] { strClassID }, "String");

			String strTreeName = "";
			String strTreeNameSql = "SELECT TREE_NAME FROM PS_TZ_RS_MODAL_TBL WHERE TZ_SCORE_MODAL_ID=? AND TZ_JG_ID=?";
			strTreeName = sqlQuery.queryForObject(strTreeNameSql, new Object[] { strScoreModalId, strCurrentOrg }, "String");

			String strScoreItemId = "";
			String strScoreItemSql = "SELECT TREE_NODE FROM PSTREENODE WHERE TREE_NAME=? and PARENT_NODE_NUM=0";
			strScoreItemId = sqlQuery.queryForObject(strScoreItemSql, new Object[] { strTreeName }, "String");

			String strJudgeAccount = "", strJudgeAccountID = "", strJudgeStatus = "", strJudgeGroup = "", strJudgeStuSX = "", strJugeStuXX = "", strJudgeDescript = "", strSubmitYN = "", strTotalNum = "0";
			String strPwSql = "SELECT TZ_PWEI_OPRID,TZ_PWEI_ZHZT,TZ_PWZBH,TZ_PYKS_XX FROM PS_TZ_CLPS_PW_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
			List<Map<String, Object>> pwList = sqlQuery.queryForList(strPwSql, new Object[] { strClassID, strBatchID });
			if (pwList != null && pwList.size() > 0) {
				for (Object obj : pwList) {
					Map<String, Object> result = (Map<String, Object>) obj;
					strJudgeAccount = result.get("TZ_PWEI_OPRID") == null ? "" : String.valueOf(result.get("TZ_PWEI_OPRID"));
					strJudgeStatus = result.get("TZ_PWEI_ZHZT") == null ? "" : String.valueOf(result.get("TZ_PWEI_ZHZT"));
					strJudgeGroup = result.get("TZ_PWZBH") == null ? "" : String.valueOf(result.get("TZ_PWZBH"));
					strJugeStuXX = result.get("TZ_PYKS_XX") == null ? "" : String.valueOf(result.get("TZ_PYKS_XX"));

					String strGroupNameSql = "SELECT TZ_CLPS_GR_NAME FROM PS_TZ_CLPS_GR_TBL WHERE TZ_JG_ID=? AND TZ_CLPS_GR_ID=?";
					String strGroupName = sqlQuery.queryForObject(strGroupNameSql, new Object[] { strCurrentOrg, strJudgeGroup }, "String");
					if (strGroupName == null) {
						strGroupName = "";
					}
					// 根据报考班级和批次取得当前轮次;
					Integer intDqpyLunc = 0;
					String strDqpyLuncSql = "SELECT ifnull(TZ_DQPY_LUNC,0) FROM PS_TZ_CLPS_GZ_TBL WHERE TZ_CLASS_ID = ? and TZ_APPLY_PC_ID=?";
					intDqpyLunc = sqlQuery.queryForObject(strDqpyLuncSql, new Object[] { strClassID, strBatchID }, "Integer");

					String strSubSql = "SELECT TZ_SUBMIT_YN FROM PS_TZ_CLPWPSLS_TBL WHERE TZ_APPLY_PC_ID=? AND TZ_CLASS_ID=? AND TZ_PWEI_OPRID=? AND TZ_CLPS_LUNC=?";
					strSubmitYN = sqlQuery.queryForObject(strSubSql, new Object[] { strBatchID, strClassID, strJudgeAccount, intDqpyLunc }, "String");

					String strPwNameSql = "SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_JG_ID=? AND OPRID=?";
					String strJudgeName = sqlQuery.queryForObject(strPwNameSql, new Object[] { strCurrentOrg, strJudgeAccount }, "String");
					// 考生材料评审历史表（里面记录了各个轮次评委的打分情况）
					String strPwIdSql = "SELECT TZ_DLZH_ID FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_JG_ID=? AND OPRID=?";
					strJudgeAccountID = sqlQuery.queryForObject(strPwIdSql, new Object[] { strCurrentOrg, strJudgeAccount }, "String");

					String strSql1 = "SELECT COUNT(DISTINCT TZ_APP_INS_ID) FROM PS_TZ_KSCLPSLS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_PWEI_OPRID=? AND TZ_CLPS_LUNC=? AND TZ_SUBMIT_YN='Y'";
					String strHasSubmited = sqlQuery.queryForObject(strSql1, new Object[] { strClassID, strBatchID, strJudgeAccount, intDqpyLunc }, "String");

					String strSql2 = "SELECT COUNT(DISTINCT TZ_APP_INS_ID) FROM PS_TZ_KSCLPSLS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_PWEI_OPRID=? AND TZ_CLPS_LUNC=? AND TZ_SUBMIT_YN<>'C'";
					String strNeedSubmit = sqlQuery.queryForObject(strSql2, new Object[] { strClassID, strBatchID, strJudgeAccount, intDqpyLunc }, "String");

					String strSubmited = strNeedSubmit + "/" + strHasSubmited;

					if (strResponse != null && !"".equals(strResponse)) {
						strResponse = strResponse + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_GD_CLPS_PWINFO_HTML", strClassID, strBatchID, strJudgeAccountID, strJudgeName, strGroupName, strSubmitYN, strJudgeStatus, strSubmited, strJudgeStuSX, strJugeStuXX, strJudgeAccount);
					} else {
						strResponse = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_GD_CLPS_PWINFO_HTML", strClassID, strBatchID, strJudgeAccountID, strJudgeName, strGroupName, strSubmitYN, strJudgeStatus, strSubmited, strJudgeStuSX, strJugeStuXX, strJudgeAccount);
					}
				}
			}

			String strSql3 = "SELECT COUNT(1) FROM PS_TZ_CLPS_PW_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
			strTotalNum = sqlQuery.queryForObject(strSql3, new Object[] { strClassID, strBatchID }, "String");

			strResponse = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_GD_BASE_JSON_HTML", strTotalNum, strResponse);

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "100";
			errMsg[1] = e.toString();
		}
		return strResponse;
	}

	@SuppressWarnings("unchecked")
	public String queryStuList(String strParams, int numLimit, int numStart, String[] errMsg) {
		String strResponse = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {			
			jacksonUtil.json2Map(strParams);
			String strClassID = jacksonUtil.getString("classID");
			String strBatchID = jacksonUtil.getString("batchID");
			
			//搜索进入
			String strSearchSQL = "";
			try{
				strSearchSQL = jacksonUtil.getString("searchSQL");
			}catch(Exception tzE){
				/**/
			}
			String strCurrentOrg = tzLoginServiceImpl.getLoginedManagerOrgid(request);

			DecimalFormat df = new DecimalFormat("######0.00");

			String strScoreModalSql = "SELECT TZ_ZLPS_SCOR_MD_ID FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID=?";
			String strScoreModalId = sqlQuery.queryForObject(strScoreModalSql, new Object[] { strClassID }, "String");

			String strTreeName = "";
			String strTreeNameSql = "SELECT TREE_NAME FROM PS_TZ_RS_MODAL_TBL WHERE TZ_SCORE_MODAL_ID=? AND TZ_JG_ID=?";
			strTreeName = sqlQuery.queryForObject(strTreeNameSql, new Object[] { strScoreModalId, strCurrentOrg }, "String");

			String strScoreItemId = "";
			String strScoreItemSql = "SELECT TREE_NODE FROM PSTREENODE WHERE TREE_NAME=? and PARENT_NODE_NUM=0";
			strScoreItemId = sqlQuery.queryForObject(strScoreItemSql, new Object[] { strTreeName }, "String");

			Integer intDqpyLunc = 0;
			String strDqpyLuncSql = "SELECT ifnull(TZ_DQPY_LUNC,0) FROM PS_TZ_CLPS_GZ_TBL WHERE TZ_CLASS_ID = ? and TZ_APPLY_PC_ID=?";
			intDqpyLunc = sqlQuery.queryForObject(strDqpyLuncSql, new Object[] { strClassID, strBatchID }, "Integer");

			Integer numZfz = 0;
			String strZfzSql = "SELECT COUNT(*) FROM PS_TZ_MODAL_DT_TBL A,PS_TZ_CJ_BPH_TBL B WHERE A.TREE_NAME=? AND A.TZ_SCORE_ITEM_ID=B.TZ_SCORE_ITEM_ID AND B.TZ_SCORE_MODAL_ID=? AND A.TZ_SCR_TO_SCORE='Y' AND B.TZ_ITEM_S_TYPE='Y'";
			numZfz = sqlQuery.queryForObject(strZfzSql, new Object[] { strTreeName, strScoreModalId }, "Integer");

			// 报名表编号 姓名 性别 面试资格 评委间偏差 评委信息 评审状态 操作人 平均分;
			String strAppInsID = "", strName = "", strGender = "", strViewQua = "", strPweiPc = "", strJudgeInfo = "", strJudgeStatus = "", strOprID = "";
			String strSql1 = "";
			String strSql3 = "";
			if(!"".equals(strSearchSQL)&&strSearchSQL!=null){
				strSql1 = "SELECT OPRID,TZ_APP_INS_ID,TZ_MSHI_ZGFLG,TZ_CLPS_PWJ_PC FROM PS_TZ_PSKSH_VW WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND OPRID IN (" + strSearchSQL + ") limit " + numStart + "," + numLimit;
				strSql3 = "SELECT COUNT(1) FROM PS_TZ_PSKSH_VW WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND OPRID IN (" + strSearchSQL + ")";				
			}else{
				strSql1 = "SELECT OPRID,TZ_APP_INS_ID,TZ_MSHI_ZGFLG,TZ_CLPS_PWJ_PC FROM PS_TZ_PSKSH_VW WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? limit " + numStart + "," + numLimit;
				strSql3 = "SELECT COUNT(1) FROM PS_TZ_PSKSH_VW WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
			}
			List<Map<String, Object>> mapList1 = sqlQuery.queryForList(strSql1, new Object[] { strClassID, strBatchID });
			if (mapList1 != null && mapList1.size() > 0) {
				for (Object obj1 : mapList1) {
					Map<String, Object> result = (Map<String, Object>) obj1;
					strOprID = result.get("OPRID") == null ? "" : String.valueOf(result.get("OPRID"));
					strAppInsID = result.get("TZ_APP_INS_ID") == null ? "" : String.valueOf(result.get("TZ_APP_INS_ID"));
					strViewQua = result.get("TZ_MSHI_ZGFLG") == null ? "" : String.valueOf(result.get("TZ_MSHI_ZGFLG"));
					strPweiPc = result.get("TZ_CLPS_PWJ_PC") == null ? "" : String.valueOf(result.get("TZ_CLPS_PWJ_PC"));
					
					String sql2 = "SELECT TZ_REALNAME,TZ_GENDER FROM PS_TZ_REG_USER_T WHERE OPRID=?";
					List<Map<String, Object>> mapList2 = sqlQuery.queryForList(sql2, new Object[] { strOprID});
					if (mapList2 != null && mapList2.size() > 0) {
						for (Object obj2 : mapList2) {
							Map<String, Object> result2 = (Map<String, Object>) obj2;
							strName = result2.get("TZ_REALNAME") == null ? "" : String.valueOf(result2.get("TZ_REALNAME"));
							strGender = result2.get("TZ_GENDER") == null ? "" : String.valueOf(result2.get("TZ_GENDER"));
						}
					}
					String strPwList = "";
					String sql3 = "SELECT TZ_PWEI_OPRID FROM PS_TZ_CP_PW_KS_TBL  WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=?";
					List<Map<String, Object>> mapList3 = sqlQuery.queryForList(sql3, new Object[] { strClassID, strBatchID, strAppInsID });
					if (mapList3 != null && mapList3.size() > 0) {
						for (Object obj2 : mapList3) {
							Map<String, Object> result3 = (Map<String, Object>) obj2;
							String strPWeiOprid = result3.get("TZ_PWEI_OPRID") == null ? "" : String.valueOf(result3.get("TZ_PWEI_OPRID"));

							String strPwNameSql = "SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_JG_ID=? AND OPRID=?";
							String strJudgeName = sqlQuery.queryForObject(strPwNameSql, new Object[] { strCurrentOrg, strPWeiOprid }, "String");
							if (strPwList != null && !"".equals(strPwList)) {
								strPwList = strPwList + "," + strJudgeName;
							} else {
								strPwList = strJudgeName;
							}
						}
					}
					if ("Y".equals(strViewQua)) {
						strViewQua = "有面试资格";
					} else if ("N".equals(strViewQua)) {
						strViewQua = "无面试资格";
					} else {
						strViewQua = "待定";
					}

					Integer intTotalSub = 0;
					String strSql4 = "SELECT COUNT(*) FROM PS_TZ_KSCLPSLS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_SUBMIT_YN='Y' AND TZ_CLPS_LUNC=? AND TZ_APP_INS_ID=?";
					intTotalSub = sqlQuery.queryForObject(strSql4, new Object[] { strClassID, strBatchID, intDqpyLunc, strAppInsID }, "Integer");

					Integer intClpyNum = 0;
					String strSql5 = "SELECT ifnull(TZ_MSPY_NUM,0) FROM PS_TZ_CLPS_GZ_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
					intClpyNum = sqlQuery.queryForObject(strSql5, new Object[] { strClassID, strBatchID }, "Integer");

					Integer intFlg = intClpyNum - intTotalSub;

					String strJudgeProgress = "", strStuProgress = "";
					if (intFlg != 0) {
						strJudgeStatus = "N";
						strJudgeProgress = intTotalSub + "/" + intClpyNum;
						strStuProgress = "未完成" + "(" + strJudgeProgress + ")";
					} else {
						strJudgeStatus = "N";
						strJudgeProgress = "";
						strStuProgress = "已完成";
					}
					// 平均分
					String strAveScore = "0";

					String strCountSQL = "SELECT COUNT(1) FROM PS_TZ_CP_PW_KS_TBL A,PS_TZ_KSCLPSLS_TBL B WHERE A.TZ_CLASS_ID=B.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID=B.TZ_APPLY_PC_ID AND A.TZ_APP_INS_ID=B.TZ_APP_INS_ID AND A.TZ_PWEI_OPRID=B.TZ_PWEI_OPRID AND B.TZ_CLASS_ID = ? AND B.TZ_APPLY_PC_ID=? and B.TZ_APP_INS_ID = ?  AND B.TZ_CLPS_LUNC = ? AND B.TZ_SUBMIT_YN<>'C'";
					Integer count = sqlQuery.queryForObject(strCountSQL, new Object[] { strClassID, strBatchID, strAppInsID, intDqpyLunc }, "Integer");
					if (count == null) {
						count = 0;
					}
					String strTotalScoreSQL = "SELECT SUM(C.TZ_SCORE_NUM) FROM PS_TZ_CP_PW_KS_TBL A,PS_TZ_KSCLPSLS_TBL B,PS_TZ_CJX_TBL C WHERE A.TZ_CLASS_ID=B.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID=B.TZ_APPLY_PC_ID AND A.TZ_APP_INS_ID=B.TZ_APP_INS_ID AND A.TZ_PWEI_OPRID=B.TZ_PWEI_OPRID AND A.TZ_SCORE_INS_ID=C.TZ_SCORE_INS_ID AND B.TZ_CLASS_ID = ? AND B.TZ_APPLY_PC_ID=? and B.TZ_APP_INS_ID = ? AND TZ_SCORE_ITEM_ID=? AND B.TZ_CLPS_LUNC = ? AND B.TZ_SUBMIT_YN<>'C'";
					Double douTotalScore = sqlQuery.queryForObject(strTotalScoreSQL, new Object[] { strClassID, strBatchID, strAppInsID, strScoreItemId, intDqpyLunc }, "Double");
					if (douTotalScore == null) {
						douTotalScore = 0.0;
					}
					double doubleTmp = 0.0;
					BigDecimal data1 = new BigDecimal(doubleTmp); 
					BigDecimal data2 = new BigDecimal(douTotalScore); 
					int resultCompare = data1.compareTo(data2);
					if (count == 0 || resultCompare == 0) {
						strAveScore = "0.00";
					} else {
						double tmpDouble = douTotalScore / count;
						strAveScore = df.format(tmpDouble);
					}

					if (!"".equals(strResponse) && strResponse != null) {
						strResponse = strResponse + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_GD_CLPS_KSINFO_HTML", strAppInsID, strName, strGender, strPweiPc, strPwList, strStuProgress, strViewQua, strAveScore, strStuProgress, strClassID, strBatchID, strOprID);
					} else {
						strResponse = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_GD_CLPS_KSINFO_HTML", strAppInsID, strName, strGender, strPweiPc, strPwList, strStuProgress, strViewQua, String.valueOf(strAveScore), strStuProgress, strClassID, strBatchID, strOprID);
					}
				}
			}
			
			String strTotalNum = sqlQuery.queryForObject(strSql3, new Object[] { strClassID, strBatchID }, "String");

			strResponse = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_GD_BASE_JSON_HTML", strTotalNum, strResponse);
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "100";
			errMsg[1] = e.toString();
		}
		return strResponse;
	}

	@SuppressWarnings("unchecked")
	public String queryPwDfChartData(String strParams, String[] errMsg) {
		String strResponse = "\"failure\"";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			String strClassID = jacksonUtil.getString("classID");
			String strBatchID = jacksonUtil.getString("batchID");
			String strCurrentOrg = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			String strPwDlzhIDs = jacksonUtil.getString("pw_ids");

			String[] strPwZhArray = strPwDlzhIDs.split(",");
			ArrayList<String> strPwOprIDList = new ArrayList<String>();

			String strScoreModalID = "";
			String strSql1 = "SELECT TZ_ZLPS_SCOR_MD_ID FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID=?";
			strScoreModalID = sqlQuery.queryForObject(strSql1, new Object[] { strClassID }, "String");

			String strFbdzID = "", strJsfs = "", strTreeName = "";
			String strSql2 = "SELECT TZ_M_FBDZ_ID,TREE_NAME FROM PS_TZ_RS_MODAL_TBL WHERE TZ_SCORE_MODAL_ID=? AND TZ_JG_ID=?";
			List<Map<String, Object>> mapList1 = sqlQuery.queryForList(strSql2, new Object[] { strClassID, strCurrentOrg });
			if (mapList1 != null && mapList1.size() > 0) {
				for (Object obj1 : mapList1) {
					Map<String, Object> result1 = (Map<String, Object>) obj1;
					strFbdzID = result1.get("TZ_M_FBDZ_ID") == null ? "" : String.valueOf(result1.get("TZ_M_FBDZ_ID"));
					strTreeName = result1.get("TREE_NAME") == null ? "" : String.valueOf(result1.get("TREE_NAME"));
				}
			}

			String strScoreItemID = "";
			String strScoreItemSql = "SELECT TREE_NODE FROM PSTREENODE WHERE TREE_NAME=? and PARENT_NODE_NUM=0";
			strScoreItemID = sqlQuery.queryForObject(strScoreItemSql, new Object[] { strTreeName }, "String");

			String strTreeNode = "";
			String strSql3 = "SELECT TREE_NODE FROM PSTREENODE WHERE TREE_NAME=? AND PARENT_NODE_NUM=0";
			strTreeNode = sqlQuery.queryForObject(strSql3, new Object[] { strTreeName }, "String");

			// 柱状图数据
			String strColumnChartHTML = "";
			// 图表数据
			String strChartFieldsHTML = "";

			int intTb = 0;
			String strPwOprid = "", strPwDlzhID = "", strPwName = "";
			String strSql4 = "SELECT TZ_PWEI_OPRID,(SELECT TZ_DLZH_ID FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=A.TZ_PWEI_OPRID limit 0,1) TZ_DLZH_ID,(SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL R WHERE R.OPRID=A.TZ_PWEI_OPRID) TZ_REALNAME FROM PS_TZ_CLPS_PW_TBL A WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
			List<Map<String, Object>> mapList2 = sqlQuery.queryForList(strSql4, new Object[] { strClassID, strBatchID });
			if (mapList2 != null && mapList2.size() > 0) {
				for (Object obj2 : mapList2) {
					Map<String, Object> result2 = (Map<String, Object>) obj2;
					strPwOprid = result2.get("TZ_PWEI_OPRID") == null ? "" : String.valueOf(result2.get("TZ_PWEI_OPRID"));
					strPwDlzhID = result2.get("TZ_DLZH_ID") == null ? "" : String.valueOf(result2.get("TZ_DLZH_ID"));
					strPwName = result2.get("TZ_REALNAME") == null ? "" : String.valueOf(result2.get("TZ_REALNAME"));

					if (this.find(strPwZhArray, strPwDlzhID) >= 0) {
						strPwOprIDList.add(strPwOprid);
						// 总分平均分-待完成
						double douPjf = 12.5;

						if (strColumnChartHTML != null && !"".equals(strColumnChartHTML)) {
							strColumnChartHTML = strColumnChartHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMS_PWDFTJ_CHART_ITEM_HTML", "name", strPwName);
						} else {
							strColumnChartHTML = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMS_PWDFTJ_CHART_ITEM_HTML", "name", strPwName);
						}
						strColumnChartHTML = strColumnChartHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMS_PWDFTJ_CHART_NUM_HTML", "data1", String.valueOf(douPjf));
						strColumnChartHTML = strColumnChartHTML + "}";

						// 图表字段
						intTb = intTb + 1;
						if (strChartFieldsHTML != null && !"".equals(strColumnChartHTML)) {
							strChartFieldsHTML = strChartFieldsHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMS_PWDFTJ_CHART_ITEM_HTML", "pw_field", "data" + intTb);
						} else {
							strChartFieldsHTML = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMS_PWDFTJ_CHART_ITEM_HTML", "pw_field", "data" + intTb);
						}
						strChartFieldsHTML = strChartFieldsHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMS_PWDFTJ_CHART_ITEM_HTML", "pw_name", strPwName);
						strChartFieldsHTML = strChartFieldsHTML + "}";
					}
				}
			}
			// 添加所有评委总分平均分字段名称
			intTb = intTb + 1;
			if (strChartFieldsHTML != null && !"".equals(strColumnChartHTML)) {
				strChartFieldsHTML = strChartFieldsHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMS_PWDFTJ_CHART_ITEM_HTML", "pw_field", "data" + intTb);
			} else {
				strChartFieldsHTML = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMS_PWDFTJ_CHART_ITEM_HTML", "pw_field", "data" + intTb);
			}
			strChartFieldsHTML = strChartFieldsHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMS_PWDFTJ_CHART_ITEM_HTML", "pw_name", "标准曲线");
			strChartFieldsHTML = strChartFieldsHTML + "}";

			// 根据报考班级和批次取得当前轮次
			Integer intDqpyLunc = 0;
			String strDqpyLuncSql = "SELECT ifnull(TZ_DQPY_LUNC,0) FROM PS_TZ_CLPS_GZ_TBL WHERE TZ_CLASS_ID = ? and TZ_APPLY_PC_ID=?";
			intDqpyLunc = sqlQuery.queryForObject(strDqpyLuncSql, new Object[] { strClassID, strBatchID }, "Integer");

			// 总分平均分-待完成
			double douZfPjf = 0.0;
			String strTotalName = "标准平均分";

			if (strColumnChartHTML != null && !"".equals(strColumnChartHTML)) {
				strColumnChartHTML = strColumnChartHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMS_PWDFTJ_CHART_ITEM_HTML", "name", strTotalName);
			} else {
				strColumnChartHTML = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMS_PWDFTJ_CHART_ITEM_HTML", "name", strTotalName);
			}
			strColumnChartHTML = strColumnChartHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMS_PWDFTJ_CHART_NUM_HTML", "data1", String.valueOf(douZfPjf));
			strColumnChartHTML = strColumnChartHTML + "}";

			// 评委完成评审考生数量
			int intWc = 0;
			String strLineChartHTML = "";

			String strMFbdzMxId = "", strMFbdzMxName = "", strMFbdzMxXX = "", strMFbdzMxXxJx = "", strMFbdzMxSx = "", strMFbdzMxSxJx = "";
			String strFbdzSql = "SELECT  TZ_M_FBDZ_MX_ID,TZ_M_FBDZ_MX_NAME,TZ_M_FBDZ_MX_XX,TZ_M_FBDZ_MX_XX_JX,TZ_M_FBDZ_MX_SX_JX,TZ_M_FBDZ_MX_SX  FROM PS_TZ_FBDZ_MX_TBL WHERE TZ_M_FBDZ_ID = ? ORDER BY TZ_M_FBDZ_MX_XH ASC";
			List<Map<String, Object>> mapList3 = sqlQuery.queryForList(strFbdzSql, new Object[] { strFbdzID });
			if (mapList3 != null && mapList3.size() > 0) {
				for (Object obj3 : mapList3) {
					Map<String, Object> result3 = (Map<String, Object>) obj3;
					strPwOprid = result3.get("TZ_M_FBDZ_MX_ID") == null ? "" : String.valueOf(result3.get("TZ_M_FBDZ_MX_ID"));
					strMFbdzMxName = result3.get("TZ_M_FBDZ_MX_NAME") == null ? "" : String.valueOf(result3.get("TZ_M_FBDZ_MX_NAME"));
					strMFbdzMxXX = result3.get("TZ_M_FBDZ_MX_XX") == null ? "" : String.valueOf(result3.get("TZ_M_FBDZ_MX_XX"));
					strMFbdzMxXxJx = result3.get("TZ_M_FBDZ_MX_XX_JX") == null ? "" : String.valueOf(result3.get("TZ_M_FBDZ_MX_XX_JX"));
					strMFbdzMxSx = result3.get("TZ_M_FBDZ_MX_SX") == null ? "" : String.valueOf(result3.get("TZ_M_FBDZ_MX_SX"));
					strMFbdzMxSxJx = result3.get("TZ_M_FBDZ_MX_SX_JX") == null ? "" : String.valueOf(result3.get("TZ_M_FBDZ_MX_SX_JX"));

					if (strLineChartHTML != null && !"".equals(strLineChartHTML)) {
						strLineChartHTML = strLineChartHTML + ",{" + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMS_PWDFTJ_CHART_ITEM_HTML", "fbName", strMFbdzMxName);
					} else {
						strLineChartHTML = "{" + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMS_PWDFTJ_CHART_ITEM_HTML", "fbName", strMFbdzMxName);
					}

					int ii = 0;
					String strSearchWhere = "";
					if (strPwOprIDList.size() > 0) {
						strSearchWhere = " AND (";
						for (ii = 0; ii < strPwOprIDList.size(); ii++) {
							String tmpOprid = strPwOprIDList.get(ii);
							if (ii == 0) {
								strSearchWhere = strSearchWhere + " A.TZ_PWEI_OPRID = '" + tmpOprid + "'";
							} else {
								strSearchWhere = strSearchWhere + " OR A.TZ_PWEI_OPRID = '" + tmpOprid + "'";
							}

							String strSql5 = "SELECT COUNT(TZ_APP_INS_ID) FROM PS_TZ_KSCLPSLS_TBL WHERE TZ_CLASS_ID = ? AND TZ_APPLY_PC_ID = ? AND TZ_PWEI_OPRID = ? AND TZ_CLPS_LUNC = ? AND  TZ_SUBMIT_YN <> 'C'";
							intWc = sqlQuery.queryForObject(strSql5, new Object[] { strClassID, strBatchID, tmpOprid, intDqpyLunc }, "int");

							int intDange = 0;
							String strSql6 = "SELECT COUNT(A.TZ_APP_INS_ID) FROM PS_TZ_CP_PW_KS_TBL A ,PS_TZ_CJX_TBL B ,PS_TZ_KSCLPSLS_TBL C  WHERE A.TZ_SCORE_INS_ID = B.TZ_SCORE_INS_ID  AND A.TZ_CLASS_ID = C.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID = C.TZ_APPLY_PC_ID AND A.TZ_PWEI_OPRID = C.TZ_PWEI_OPRID AND A.TZ_APP_INS_ID = C.TZ_APP_INS_ID AND C.TZ_SUBMIT_YN <> 'C'  AND A.TZ_CLASS_ID = ? AND A.TZ_APPLY_PC_ID = ? AND A.TZ_PWEI_OPRID =? AND B.TZ_SCORE_ITEM_ID = ?  AND C.TZ_CLPS_LUNC = ? AND B.TZ_SCORE_NUM " + strMFbdzMxXxJx + strMFbdzMxXX + "AND B.TZ_SCORE_NUM " + strMFbdzMxSxJx + strMFbdzMxSx;
							intDange = sqlQuery.queryForObject(strSql6, new Object[] { strClassID, strBatchID, strPwOprid, strScoreItemID, intDqpyLunc }, "int");
							if (intDange == 0 || intWc == 0) {
								strLineChartHTML = strLineChartHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMS_PWDFTJ_CHART_NUM_HTML", "data" + ii, "0");
							} else {
								DecimalFormat df = new DecimalFormat("######0.00");
								double tmpDouble = (double) (intDange / intWc) * 100;
								String tmpAveScore = df.format(tmpDouble);
								strLineChartHTML = strLineChartHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMS_PWDFTJ_CHART_NUM_HTML", "data" + ii, tmpAveScore);
							}
						}
						strSearchWhere = strSearchWhere + " )";
					}

					int intWc2 = 0;
					String strSql7 = "SELECT COUNT(TZ_APP_INS_ID) FROM PS_TZ_KSCLPSLS_TBL A WHERE TZ_CLASS_ID = ? AND TZ_APPLY_PC_ID = ? AND TZ_CLPS_LUNC = ? AND  TZ_SUBMIT_YN <> 'C'" + strSearchWhere;
					intWc2 = sqlQuery.queryForObject(strSql7, new Object[] { strClassID, strBatchID, intDqpyLunc }, "int");
					int intDange2 = 0;
					String strSql8 = "SELECT COUNT(A.TZ_APP_INS_ID) FROM PS_TZ_CP_PW_KS_TBL A ,PS_TZ_CJX_TBL B ,PS_TZ_KSCLPSLS_TBL C  WHERE A.TZ_SCORE_INS_ID = B.TZ_SCORE_INS_ID  AND A.TZ_CLASS_ID = C.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID = C.TZ_APPLY_PC_ID AND A.TZ_PWEI_OPRID = C.TZ_PWEI_OPRID AND A.TZ_APP_INS_ID = C.TZ_APP_INS_ID AND C.TZ_SUBMIT_YN <> 'C'  AND A.TZ_CLASS_ID = ? AND A.TZ_APPLY_PC_ID = ?  AND B.TZ_SCORE_ITEM_ID = ?  AND C.TZ_CLPS_LUNC = ? AND B.TZ_SCORE_NUM  " + strMFbdzMxXxJx + strMFbdzMxXX + "AND B.TZ_SCORE_NUM " + strMFbdzMxSxJx + strMFbdzMxSx + strSearchWhere;
					intDange2 = sqlQuery.queryForObject(strSql8, new Object[] { strClassID, strBatchID, strScoreItemID, intDqpyLunc }, "int");
					if (intDange2 == 0 || intWc2 == 0) {
						strLineChartHTML = strLineChartHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMS_PWDFTJ_CHART_NUM_HTML", "data" + ii, "0");
					} else {
						DecimalFormat df = new DecimalFormat("######0.00");
						double tmpDouble = (double) (intDange2 / intWc2) * 100;
						String tmpAveScore = df.format(tmpDouble);
						strLineChartHTML = strLineChartHTML + "," + tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMS_PWDFTJ_CHART_NUM_HTML", "data" + ii, tmpAveScore);
					}
					strLineChartHTML = strLineChartHTML + "}";
				}
			}
			strResponse = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_CLMS_CHART_JSON_HTML", strColumnChartHTML, strLineChartHTML, strChartFieldsHTML, strJsfs);
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "100";
			errMsg[1] = e.toString();
		}
		return strResponse;
	}

	@SuppressWarnings("unchecked")
	public String btnClick(String strParams, String[] errMsg) {
		String strResponse = "\"failure\"";
		JacksonUtil jacksonUtil = new JacksonUtil();
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

		try {
			jacksonUtil.json2Map(strParams);
			String strClassID = jacksonUtil.getString("classID");
			String strBatchID = jacksonUtil.getString("batchID");
			String strDelibCount = jacksonUtil.getString("delibCount");

			String strBtnType = jacksonUtil.getString("type");

			//日志表
			PsTzClpsLogTbl psTzClpsLogTbl = new PsTzClpsLogTbl();
			
			String strExistsSql = "SELECT 'Y' FROM PS_TZ_CLPS_GZ_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
			String strExistsFlg = sqlQuery.queryForObject(strExistsSql, new Object[] { strClassID, strBatchID }, "String");
			PsTzClpsGzTbl psTzClpsGzTbl = new PsTzClpsGzTbl();
			switch (strBtnType) {
			case "reStartNewOnclick":
				// 开启新一轮评审
				// 获取当前批次下考生的数量
				String strSql1 = "SELECT COUNT(*) FROM PS_TZ_CLPS_KSH_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
				Integer numStu = sqlQuery.queryForObject(strSql1, new Object[] { strClassID, strBatchID }, "Integer");
				// 每位考生需要被多少位评委评审
				String strSql2 = "SELECT ifnull(TZ_MSPY_NUM,0) FROM PS_TZ_CLPS_GZ_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
				Integer judgeCount = sqlQuery.queryForObject(strSql2, new Object[] { strClassID, strBatchID }, "Integer");
				int numTotal = 0;
				if (numStu != null && judgeCount != null) {
					numTotal = numStu * judgeCount;
				}

				/* 规则待定 */
				psTzClpsGzTbl.setTzClassId(strClassID);
				psTzClpsGzTbl.setTzApplyPcId(strBatchID);
				Short delibCount = Short.valueOf(strDelibCount);
				int numSclu = Integer.valueOf(strDelibCount) - 1;

				psTzClpsGzTbl.setTzDqpyLunc(delibCount);
				psTzClpsGzTbl.setTzScpyLunc(Short.parseShort(String.valueOf(numSclu)));
				psTzClpsGzTbl.setTzDqpyZt("A");
				if ("Y".equals(strExistsFlg)) {
					PsTzClpsGzTblMapper.updateByPrimaryKeySelective(psTzClpsGzTbl);
				} else {
					PsTzClpsGzTblMapper.insert(psTzClpsGzTbl);
				}
				// 将上一轮次的考生信息向新轮次insert数据，并且将状态都设置为“未提交”
				String strSql3 = "SELECT TZ_APP_INS_ID,TZ_PWEI_OPRID,TZ_GUANX_LEIX,TZ_KSH_PSPM FROM PS_TZ_CP_PW_KS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
				List<Map<String, Object>> mapList = sqlQuery.queryForList(strSql3, new Object[] { strClassID, strBatchID });
				if (mapList != null && mapList.size() > 0) {
					for (Object obj : mapList) {
						Map<String, Object> result = (Map<String, Object>) obj;
						String strAppInsId = result.get("TZ_APP_INS_ID") == null ? "" : String.valueOf(result.get("TZ_APP_INS_ID"));
						String strPwOprid = result.get("TZ_PWEI_OPRID") == null ? "" : String.valueOf(result.get("TZ_PWEI_OPRID"));
						String strGuanXLeix = result.get("TZ_GUANX_LEIX") == null ? "" : String.valueOf(result.get("TZ_GUANX_LEIX"));
						String strKShPspm = result.get("TZ_KSH_PSPM") == null ? "" : String.valueOf(result.get("TZ_KSH_PSPM"));

						PsTzKsclpslsTbl PsTzKsclpslsTbl = new PsTzKsclpslsTbl();
						PsTzKsclpslsTbl.setTzClassId(strClassID);
						PsTzKsclpslsTbl.setTzApplyPcId(strBatchID);
						PsTzKsclpslsTbl.setTzPweiOprid(strPwOprid);
						PsTzKsclpslsTbl.setTzAppInsId(Long.parseLong(strAppInsId));
						PsTzKsclpslsTbl.setTzClpsLunc(delibCount);
						PsTzKsclpslsTbl.setTzGuanxLeix(strGuanXLeix);
						PsTzKsclpslsTbl.setTzKshPspm(strKShPspm);
						PsTzKsclpslsTbl.setTzSubmitYn("N");
						PsTzKsclpslsTbl.setRowAddedDttm(new Date());
						PsTzKsclpslsTbl.setRowAddedOprid(oprid);
						PsTzKsclpslsTbl.setRowLastmantDttm(new Date());
						PsTzKsclpslsTbl.setRowLastmantOprid(oprid);
						PsTzKsclpslsTblMapper.insert(PsTzKsclpslsTbl);
					}
				}
				//记录历史
				Integer intRzlsNum = getSeqNum.getSeqNum("TZ_CLPS_LOG_TBL", "TZ_RZLS_NUM");
				psTzClpsLogTbl.setTzRzlsNum(String.valueOf(intRzlsNum));
				psTzClpsLogTbl.setTzClassId(strClassID);
				psTzClpsLogTbl.setTzBatchId(strBatchID);
				psTzClpsLogTbl.setTzClpsLunc(delibCount);
				//A-开放，B-关闭，C-开启新一轮评审
				psTzClpsLogTbl.setTzOprType("C");
				psTzClpsLogTbl.setTzOprid(oprid);
				psTzClpsLogTbl.setTzOprDttm(new Date());
				psTzClpsLogTblMapper.insert(psTzClpsLogTbl);
				// 成功开启，返回总人数
				strResponse = String.valueOf(numTotal);

				break;
			case "closeReviewOnclick":
				// 关闭本轮评审
				psTzClpsGzTbl.setTzClassId(strClassID);
				psTzClpsGzTbl.setTzApplyPcId(strBatchID);
				psTzClpsGzTbl.setTzDqpyZt("B");
				if ("Y".equals(strExistsFlg)) {
					PsTzClpsGzTblMapper.updateByPrimaryKeySelective(psTzClpsGzTbl);
				} else {
					PsTzClpsGzTblMapper.insert(psTzClpsGzTbl);
				}
				
				//记录历史
				Integer intRzlsNum2 = getSeqNum.getSeqNum("TZ_CLPS_LOG_TBL", "TZ_RZLS_NUM");
				psTzClpsLogTbl.setTzRzlsNum(String.valueOf(intRzlsNum2));
				psTzClpsLogTbl.setTzClassId(strClassID);
				psTzClpsLogTbl.setTzBatchId(strBatchID);
				psTzClpsLogTbl.setTzClpsLunc(Short.valueOf(strDelibCount));
				//A-开放，B-关闭，C-开启新一轮评审
				psTzClpsLogTbl.setTzOprType("B");
				psTzClpsLogTbl.setTzOprid(oprid);
				psTzClpsLogTbl.setTzOprDttm(new Date());
				psTzClpsLogTblMapper.insert(psTzClpsLogTbl);
				
				strResponse = "{\"status\":\"B\"}";
				break;
			case "reStartReviewOnclick":
				// 重新开启本轮评审
				psTzClpsGzTbl.setTzClassId(strClassID);
				psTzClpsGzTbl.setTzApplyPcId(strBatchID);
				psTzClpsGzTbl.setTzDqpyZt("A");
				if ("Y".equals(strExistsFlg)) {
					PsTzClpsGzTblMapper.updateByPrimaryKeySelective(psTzClpsGzTbl);
				} else {
					PsTzClpsGzTblMapper.insert(psTzClpsGzTbl);
				}
				
				//记录历史
				Integer intRzlsNum3 = getSeqNum.getSeqNum("TZ_CLPS_LOG_TBL", "TZ_RZLS_NUM");
				psTzClpsLogTbl.setTzRzlsNum(String.valueOf(intRzlsNum3));
				psTzClpsLogTbl.setTzClassId(strClassID);
				psTzClpsLogTbl.setTzBatchId(strBatchID);
				psTzClpsLogTbl.setTzClpsLunc(Short.valueOf(strDelibCount));
				//A-开放，B-关闭，C-开启新一轮评审
				psTzClpsLogTbl.setTzOprType("A");
				psTzClpsLogTbl.setTzOprid(oprid);
				psTzClpsLogTbl.setTzOprDttm(new Date());
				psTzClpsLogTblMapper.insert(psTzClpsLogTbl);
				
				strResponse = "{\"status\":\"A\"}";
				break;
			}
			errMsg[0] = "0";
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strResponse;
	}

	@SuppressWarnings("unchecked")
	public String removeJudgeData(String strParams, String[] errMsg) {
		String strResponse = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		try {
			String strCurrentOrg = tzLoginServiceImpl.getLoginedManagerOrgid(request);

			jacksonUtil.json2Map(strParams);

			List<?> removeList = jacksonUtil.getList("remove");
			if (removeList != null && removeList.size() > 0) {
				for (Object obj : removeList) {
					Map<String, Object> mapFormData = (Map<String, Object>) obj;

					String strClassID = mapFormData.get("classID") == null ? "" : String.valueOf(mapFormData.get("classID"));
					String strBatchID = mapFormData.get("batchID") == null ? "" : String.valueOf(mapFormData.get("batchID"));
					String strJudgeId = mapFormData.get("judgeID") == null ? "" : String.valueOf(mapFormData.get("judgeID"));
					// 获取当前班级批次下的轮次
					String strSql1 = "SELECT TZ_DQPY_LUNC FROM PS_TZ_CLPS_GZ_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
					Integer numLunc = sqlQuery.queryForObject(strSql1, new Object[] { strClassID, strBatchID }, "Integer");
					if (numLunc == null) {
						numLunc = 0;
					}
					// 评委OPRID
					String strPweOSQL = "SELECT OPRID FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_DLZH_ID=?";
					String strPweiOprid = sqlQuery.queryForObject(strPweOSQL, new Object[] { strJudgeId }, "String");
					// 评委列表
					String strSql2 = "SELECT TZ_APP_INS_ID FROM PS_TZ_CP_PW_KS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID = ? and TZ_PWEI_OPRID = ?";
					List<Map<String, Object>> mapList = sqlQuery.queryForList(strSql2, new Object[] { strClassID, strBatchID, strPweiOprid });
					if (mapList != null && mapList.size() > 0) {
						for (Object pwObj : mapList) {
							Map<String, Object> result = (Map<String, Object>) pwObj;

							String strAppInsId = result.get("TZ_APP_INS_ID") == null ? "" : String.valueOf(result.get("TZ_APP_INS_ID"));

							String strSql3 = "SELECT TZ_CLPS_LUNC,TZ_CLPW_LIST FROM PS_TZ_CLPSKSPW_TBL WHERE TZ_CLASS_ID = ? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID = ?";
							List<Map<String, Object>> mapList2 = sqlQuery.queryForList(strSql3, new Object[] { strClassID, strBatchID, strAppInsId });
							if (mapList2 != null && mapList2.size() > 0) {
								String strClpsLunc = result.get("TZ_CLPS_LUNC") == null ? "" : String.valueOf(result.get("TZ_CLPW_LIST"));
								String strJudgeList = result.get("TZ_CLPW_LIST") == null ? "" : String.valueOf(result.get("TZ_CLPW_LIST"));
								// 从所有当前班级和批次的考生的评委列表中查找出当前评委并删除
								if (strJudgeList != null && !"".equals(strJudgeList)) {
									if (strJudgeList.contains(strJudgeId + ",")) {
										strJudgeList.replace(strJudgeId + ",", "");
									} else if (strJudgeList.contains("," + strJudgeId)) {
										strJudgeList.replace("," + strJudgeId, "");
									} else {
										strJudgeList.replace(strJudgeId, "");
									}
									// 更新考生的评委列表;
									String strUpdateSql = "UPDATE PS_TZ_CLPSKSPW_TBL SET TZ_CLPW_LIST=? WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=? AND TZ_CLPS_LUNC=?";
									sqlQuery.update(strUpdateSql, new Object[] { strJudgeList, strClassID, strBatchID, strAppInsId, strClpsLunc });
								}
							}
						}
					}
					// 删除MBA材料评审评委考生关系表 中的数据
					String strDeleteSql1 = "DELETE FROM PS_TZ_CP_PW_KS_TBL WHERE TZ_APPLY_PC_ID = ? AND TZ_PWEI_OPRID = ? AND TZ_CLASS_ID=?";
					sqlQuery.update(strDeleteSql1, new Object[] { strBatchID, strPweiOprid, strClassID });
					// 奖评委考生记录表中的数据设置为撤销
					String strUpdateSql1 = "UPDATE PS_TZ_KSCLPSLS_TBL SET TZ_SUBMIT_YN='C' WHERE TZ_APPLY_PC_ID = ? AND TZ_PWEI_OPRID = ? AND TZ_CLASS_ID=?";
					sqlQuery.update(strUpdateSql1, new Object[] { strBatchID, strPweiOprid, strClassID });
					// 更新 MBA材料评审评委评审历史 中的数据为撤销
					String strUpdateSql2 = "UPDATE PS_TZ_CLPWPSLS_TBL SET TZ_SUBMIT_YN = 'C' WHERE TZ_APPLY_PC_ID = ? AND TZ_PWEI_OPRID = ? AND TZ_CLASS_ID=?";
					sqlQuery.update(strUpdateSql2, new Object[] { strBatchID, strPweiOprid, strClassID });
				}
			}
			errMsg[0] = "0";
		} catch (Exception e) {
			errMsg[0] = "100";
			errMsg[1] = e.toString();
		}
		return strResponse;
	}
	@SuppressWarnings("unchecked")
	public String calculate(String strParams, String[] errMsg) {
		String strResponse = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

		try {
			String strCurrentOrg = tzLoginServiceImpl.getLoginedManagerOrgid(request);

			jacksonUtil.json2Map(strParams);
			String strClassID = jacksonUtil.getString("classID");
			String strBatchID = jacksonUtil.getString("batchID");

			String strContent = "";
			List<?> listAppID = jacksonUtil.getList("appID");

			if (listAppID != null && listAppID.size() > 0) {

				for (Object pwObj : listAppID) {
					ArrayList<String> mapScore = new ArrayList();
					String strAppInsID = String.valueOf(pwObj);

					String strDeleteSql = "DELETE FROM PS_TZ_PW_KS_PC_TBL";
					sqlQuery.update(strDeleteSql);

					double doublePianCha = this.caluatePianch(strClassID, strBatchID, strAppInsID);

					DecimalFormat df = new DecimalFormat("######0.00");
					String tmpAveScore = df.format(doublePianCha);
					if ("".equals(strContent)) {
						strContent = "{\"appInsID\":\"" + strAppInsID + "\",\"standardDeviation\":\"" + tmpAveScore + "\"}";
					} else {
						strContent = strContent + ",{\"appInsID\":\"" + strAppInsID + "\",\"standardDeviation\":\"" + tmpAveScore + "\"}";
					}
				}
			}

			strResponse = tzGdObject.getHTMLText("HTML.TZMaterialInterviewReviewBundle.TZ_GD_BASE_JSON_HTML", String.valueOf(listAppID.size()), strContent);

		} catch (Exception e) {
			errMsg[0] = "100";
			errMsg[1] = e.toString();
		}
		return strResponse;
	}
	@SuppressWarnings("unchecked")
	public String savePyData(String strParams, String[] errMsg) {
		String strResponse = "\"failure\"";
		JacksonUtil jacksonUtil = new JacksonUtil();
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

		try {
			String strCurrentOrg = tzLoginServiceImpl.getLoginedManagerOrgid(request);

			jacksonUtil.json2Map(strParams);
			String strClassID = jacksonUtil.getString("classID");
			String strBatchID = jacksonUtil.getString("batchID");

			String strContent = "";
			List<?> pyData = jacksonUtil.getList("pyInfoUpdate");
			if (pyData != null && pyData.size() > 0) {
				String strScoreModalID = "";
				String strSql1 = "SELECT TZ_ZLPS_SCOR_MD_ID FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID=?";
				strScoreModalID = sqlQuery.queryForObject(strSql1, new Object[] { strClassID }, "String");

				String strFbdzID = "", strJsfs = "", strTreeName = "";
				String strSql4 = "SELECT TZ_M_FBDZ_ID,TREE_NAME FROM PS_TZ_RS_MODAL_TBL WHERE TZ_SCORE_MODAL_ID=? AND TZ_JG_ID=?";
				Map<String, Object> sMap = sqlQuery.queryForMap(strSql4, new Object[] { strScoreModalID, strCurrentOrg });
				if (sMap != null) {
					strFbdzID = sMap.get("TZ_M_FBDZ_ID") == null ? "" : String.valueOf(sMap.get("TZ_M_FBDZ_ID"));
					strTreeName = sMap.get("TREE_NAME") == null ? "" : String.valueOf(sMap.get("TREE_NAME"));
				}
				String strTreeNode = "";
				String strSql3 = "SELECT TREE_NODE FROM PSTREENODE WHERE TREE_NAME=? AND PARENT_NODE_NUM=0";
				strTreeNode = sqlQuery.queryForObject(strSql3, new Object[] { strTreeName }, "String");

				String listSql = "SELECT  TZ_M_FBDZ_MX_ID,TZ_M_FBDZ_MX_NAME  FROM PS_TZ_FBDZ_MX_TBL WHERE TZ_M_FBDZ_ID = ? ORDER BY TZ_M_FBDZ_MX_XH ASC";
				List<Map<String, Object>> sMxList = sqlQuery.queryForList(listSql, new Object[] { strFbdzID });
				if (sMxList != null && sMxList.size() > 0) {
					for (Object sObj : sMxList) {
						Map<String, Object> resultMap = (Map<String, Object>) sObj;
						String strFbMxId = resultMap.get("TZ_M_FBDZ_MX_ID") == null ? "" : String.valueOf(resultMap.get("TZ_M_FBDZ_MX_ID"));
						String strColId = "mx_" + strFbMxId;

						String strBl = "", strWc = "";
						for (Object pwObj : pyData) {
							Map<String, Object> result = (Map<String, Object>) pwObj;
							try {
								String colName = String.valueOf(result.get("col00"));
								if ("比率".equals(colName)) {
									strBl = result.get(strColId) == null ? "" : String.valueOf(result.get(strColId));
								} else {
									strWc = result.get(strColId) == null ? "" : String.valueOf(result.get(strColId));
								}
							} catch (Exception e) {
								/* 未做过修改，doNothing */
							}
						}
						if ((!"".equals(strBl) && strBl != null) || (!"".equals(strWc) && strWc != null)) {

							// 做过修改，更新数据库
							String strExistSql = "SELECT 'Y' FROM PS_TZ_CPFB_BZH_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_SCORE_MODAL_ID=? AND TZ_SCORE_ITEM_ID=? AND TZ_M_FBDZ_ID=? AND TZ_M_FBDZ_MX_ID=?";
							String strExistFlg = sqlQuery.queryForObject(strExistSql, new Object[] { strClassID, strBatchID, strScoreModalID, "Total", strFbdzID, strFbMxId }, "String");
							PsTzCpfbBzhTbl psTzCpfbBzhTbl = new PsTzCpfbBzhTbl();
							psTzCpfbBzhTbl.setTzClassId(strClassID);
							psTzCpfbBzhTbl.setTzApplyPcId(strBatchID);
							psTzCpfbBzhTbl.setTzScoreModalId(strScoreModalID);
							psTzCpfbBzhTbl.setTzScoreItemId("Total");
							psTzCpfbBzhTbl.setTzMFbdzId(strFbdzID);
							psTzCpfbBzhTbl.setTzMFbdzMxId(strFbMxId);
							if (!"".equals(strBl) && strBl != null) {
								Double douBl = Double.valueOf(strBl);
								psTzCpfbBzhTbl.setTzBzfbBl(BigDecimal.valueOf(douBl));
							}
							if (!"".equals(strWc) && strWc != null) {
								Double douWc = Double.valueOf(strWc);
								psTzCpfbBzhTbl.setTzYxwcNum(BigDecimal.valueOf(douWc));
							}

							if ("Y".equals(strExistFlg)) {
								PsTzCpfbBzhTblMapper.updateByPrimaryKeySelective(psTzCpfbBzhTbl);
							} else {
								PsTzCpfbBzhTblMapper.insert(psTzCpfbBzhTbl);
							}
						}
					}
				}
				for (Object pwObj : pyData) {
					Map<String, Object> result = (Map<String, Object>) pwObj;
					try {
						String colName = String.valueOf(result.get("col00"));
						String strBlAve = "", strWcAve = "";
						// 比率平均和误差平均列，为col02
						String strColId = "col01";
						if ("比率".equals(colName)) {
							strBlAve = result.get(strColId) == null ? "0" : String.valueOf(result.get(strColId));
						} else {
							strWcAve = result.get(strColId) == null ? "0.00" : String.valueOf(result.get(strColId));
						}

						String str_ScoreItemId = "TOTAL";
						String str_TjgnID = "001";
						String strExistSql = "SELECT 'Y' FROM PS_TZ_QTTJ_BZH_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_SCORE_MODAL_ID=? AND TZ_SCORE_ITEM_ID=? AND TZ_TJGN_ID=?";
						String strExistFlg = sqlQuery.queryForObject(strExistSql, new Object[] { strClassID, strBatchID, strScoreModalID, str_ScoreItemId, str_TjgnID }, "String");

						PsTzQttjBzhTbl psTzQttjTbl = new PsTzQttjBzhTbl();
						psTzQttjTbl.setTzClassId(strClassID);
						psTzQttjTbl.setTzApplyPcId(strBatchID);
						psTzQttjTbl.setTzScoreModalId(strScoreModalID);
						psTzQttjTbl.setTzScoreItemId(str_ScoreItemId);
						psTzQttjTbl.setTzTjgnId(str_TjgnID);

						if (!"".equals(strBlAve)) {
							double doubleBlAve = Double.valueOf(strBlAve);
							psTzQttjTbl.setTzTjlBzh(BigDecimal.valueOf(doubleBlAve));
						}
						if (!"".equals(strWcAve)) {
							double doubleWcAve = Double.valueOf(strWcAve);
							psTzQttjTbl.setTzTjlWcz(BigDecimal.valueOf(doubleWcAve));
						}

						if ("Y".equals(strExistFlg)) {
							psTzQttjBzhTblMapper.updateByPrimaryKeySelective(psTzQttjTbl);
						} else {
							psTzQttjBzhTblMapper.insert(psTzQttjTbl);
						}
					} catch (Exception e) {
						/* 未做过修改，doNothing */
					}
				}

				// 重新获取数据
				String strSql = "SELECT TZ_M_FBDZ_MX_ID,TZ_BZFB_BL,cast(TZ_YXWC_NUM as SIGNED) TZ_YXWC_NUM FROM PS_TZ_CPFB_BZH_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_SCORE_MODAL_ID=? AND TZ_SCORE_ITEM_ID='Total' AND TZ_M_FBDZ_ID=?";
				List<Map<String, Object>> dataList = sqlQuery.queryForList(strSql, new Object[] { strClassID, strBatchID, strScoreModalID, strFbdzID });

				ArrayList toAr = new ArrayList();
				
				ArrayList blAr = new ArrayList();
				String strAveSql = "SELECT TZ_TJL_BZH,TZ_TJL_WCZ FROM PS_TZ_QTTJ_BZH_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_SCORE_MODAL_ID=? AND TZ_SCORE_ITEM_ID=? AND TZ_TJGN_ID=?";
				Map<String, Object> s2Map = sqlQuery.queryForMap(strAveSql, new Object[] { strClassID, strBatchID, strScoreModalID, "TOTAL", "001" });
				String strAve2 = "0.00", strWcAve2 = "0.00";
				if (s2Map != null) {
					strAve2 = s2Map.get("TZ_TJL_BZH") == null ? "0.00" : String.valueOf(s2Map.get("TZ_TJL_BZH"));
					strWcAve2 = s2Map.get("TZ_TJL_WCZ") == null ? "0.00" : String.valueOf(s2Map.get("TZ_TJL_WCZ"));
				}
				blAr.add("比率");
				blAr.add(strAve2);
				ArrayList wcAr = new ArrayList();
				wcAr.add("误差");
				wcAr.add(strWcAve2);
				if (dataList != null && dataList.size() > 0) {
					
					for (Object resObj : dataList) {
						Map<String, Object> sMapA = (Map<String, Object>) resObj;
						String strB = sMapA.get("TZ_BZFB_BL") == null ? "" : String.valueOf(sMapA.get("TZ_BZFB_BL"));
						String strW = sMapA.get("TZ_YXWC_NUM") == null ? "" : String.valueOf(sMapA.get("TZ_YXWC_NUM"));
						blAr.add(strB);
						wcAr.add(strW);
					}
									
				}
				toAr.add(blAr);
				toAr.add(wcAr);	
				strResponse = jacksonUtil.List2json(toAr);
				return strResponse;
			}

			strResponse = "\"success\"";

		} catch (Exception e) {
			errMsg[0] = "100";
			errMsg[1] = e.toString();
		}
		return strResponse;
	}
	@SuppressWarnings("unchecked")
	public double caluatePianch(String strClassID, String strBatchID, String strAppInsID) {

		String strCurrentOrg = tzLoginServiceImpl.getLoginedManagerOrgid(request);

		String strScoreModalID = "";
		String strSql1 = "SELECT TZ_ZLPS_SCOR_MD_ID FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID=?";
		strScoreModalID = sqlQuery.queryForObject(strSql1, new Object[] { strClassID }, "String");

		String strTreeName = "";
		String strSql2 = "SELECT TREE_NAME FROM PS_TZ_RS_MODAL_TBL WHERE TZ_SCORE_MODAL_ID=? AND TZ_JG_ID=?";
		strTreeName = sqlQuery.queryForObject(strSql2, new Object[] { strScoreModalID, strCurrentOrg }, "String");

		String strScoreItemID = "";
		String strScoreItemSql = "SELECT TREE_NODE FROM PSTREENODE WHERE TREE_NAME=? and PARENT_NODE_NUM=0";
		strScoreItemID = sqlQuery.queryForObject(strScoreItemSql, new Object[] { strTreeName }, "String");

		String strTreeNode = "";
		String strSql3 = "SELECT TREE_NODE FROM PSTREENODE WHERE TREE_NAME=? AND PARENT_NODE_NUM=0";
		strTreeNode = sqlQuery.queryForObject(strSql3, new Object[] { strTreeName }, "String");

		int numTotal = 0;
		String strListSql1 = "SELECT DISTINCT TZ_PWEI_OPRID FROM PS_TZ_CP_PW_KS_TBL WHERE TZ_APPLY_PC_ID = ? AND TZ_APP_INS_ID = ?";
		List<Map<String, Object>> mapList1 = sqlQuery.queryForList(strListSql1, new Object[] { strBatchID, strAppInsID });

		if (mapList1 != null && mapList1.size() > 0) {
			int pw_num = 1;
			for (Object mapObj : mapList1) {
				Map<String, Object> result = (Map<String, Object>) mapObj;
				String str_PwOprid = result.get("TZ_PWEI_OPRID") == null ? "" : String.valueOf(result.get("TZ_PWEI_OPRID"));

				// 获取当前班级批次下的轮次
				String strSql12 = "SELECT TZ_DQPY_LUNC FROM PS_TZ_CLPS_GZ_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
				Integer numLunc = sqlQuery.queryForObject(strSql12, new Object[] { strClassID, strBatchID }, "Integer");
				if (numLunc == null) {
					numLunc = 0;
				}

				String strMapSql = "SELECT TZ_SUBMIT_YN FROM PS_TZ_KSCLPSLS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID = ? AND TZ_APP_INS_ID = ? AND TZ_PWEI_OPRID = ? AND TZ_CLPS_LUNC = ?";
				String strSubmitStatus = sqlQuery.queryForObject(strMapSql, new Object[] { strClassID, strBatchID, strAppInsID, str_PwOprid, numLunc }, "String");
				if (!"C".equals(strSubmitStatus)) {
					String strSql4 = "SELECT B.TZ_SCORE_NUM FROM PS_TZ_CJX_TBL B,PS_TZ_CP_PW_KS_TBL A,PS_TZ_CLPS_GZ_TBL C WHERE A.TZ_SCORE_INS_ID=B.TZ_SCORE_INS_ID AND A.TZ_CLASS_ID=C.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID=C.TZ_APPLY_PC_ID AND C.TZ_CLASS_ID=? AND C.TZ_APPLY_PC_ID=? AND C.TZ_DQPY_LUNC=? AND A.TZ_APP_INS_ID=? AND A.TZ_PWEI_OPRID=? AND B.TZ_SCORE_ITEM_ID=?";
					Double intScoreNum = sqlQuery.queryForObject(strSql4, new Object[] { strClassID, strBatchID, numLunc, strAppInsID, str_PwOprid, strTreeNode }, "Double");
					if (intScoreNum != null) {
						String strInsertSql = "INSERT INTO PS_TZ_PW_KS_PC_TBL VALUES(" + pw_num + ",'" + intScoreNum + "')";
						sqlQuery.update(strInsertSql);
						pw_num = pw_num + 1;
					}
					/*
					 * String strListSql2 =
					 * "SELECT B.TZ_SCORE_MODAL_ID,A.TZ_SCORE_INS_ID,A.TZ_SCORE_ITEM_ID,A.TZ_SCORE_NUM,TZ_SCORE_PY_VALUE FROM PS_TZ_CP_PW_KS_TBL C JOIN PS_TZ_CJX_TBL A ON A.TZ_SCORE_INS_ID=C.TZ_SCORE_INS_ID JOIN PS_TZ_SRMBAINS_TBL B ON A.TZ_SCORE_INS_ID=B.TZ_SCORE_INS_ID WHERE C.TZ_CLASS_ID=? AND C.TZ_APPLY_PC_ID=? AND C.TZ_APP_INS_ID=? AND C.TZ_PWEI_OPRID=?"
					 * ; List<Map<String, Object>> mapList2 =
					 * sqlQuery.queryForList(strListSql2, new Object[] {
					 * strClassID, strBatchID, strAppInsID, str_PwOprid }); if
					 * (mapList2 != null && mapList2.size() > 0) { for (Object
					 * sObj : mapList2) { Map<String, Object> resultMap =
					 * (Map<String, Object>) sObj; String strScoreItemId =
					 * String.valueOf(resultMap.get("TZ_SCORE_ITEM_ID")); String
					 * strScoreNum = resultMap.get("TZ_SCORE_NUM") == null ? "0"
					 * : String.valueOf(resultMap.get("TZ_SCORE_NUM")); String
					 * strScorePyValue =
					 * String.valueOf(resultMap.get("TZ_SCORE_PY_VALUE"));
					 * 
					 * String strMap2Sql =
					 * "SELECT TZ_ITEM_S_TYPE FROM PS_TZ_CJ_BPH_TBL WHERE TZ_JG_ID=? AND TZ_SCORE_MODAL_ID=? AND TZ_SCORE_ITEM_ID=?"
					 * ; String strItemType =
					 * sqlQuery.queryForObject(strMap2Sql, new Object[] {
					 * strCurrentOrg, strScoreModalID, strScoreItemId },
					 * "String"); if ("C".equals(strItemType)) {
					 * 
					 * } else {
					 * 
					 * } pw_num = pw_num + 1; String strInsertSql =
					 * "INSERT INTO PS_TZ_PW_KS_PC_TBL VALUES(" + pw_num + ",'"
					 * + strScoreNum + "')"; sqlQuery.update(strInsertSql); }
					 * 
					 * }
					 */
				}
			}
		}

		// 偏差
		String strPianChaSql = "SELECT stddev(TZ_SCORE_NUM) FROM PS_TZ_PW_KS_PC_TBL";
		Double doublePianCha = sqlQuery.queryForObject(strPianChaSql, "Double");
		if (doublePianCha == null) {
			doublePianCha = 0.00;
		}
		return doublePianCha;
	}

	// 平均分计算（包括：单个评委和多个评委的情况）
	@SuppressWarnings("unchecked")
	public String caluateAverage(String strClassID, String strBatchID, String strClpsLunc, String StrScoreModalID, String strScoreItemID, String[] pwList) {

		String strCurrentOrg = tzLoginServiceImpl.getLoginedManagerOrgid(request);

		String str_pjf = "";

		try {

			String strFbdzID = "", strJsfs = "", strTreeName = "";
			String strSql4 = "SELECT TZ_M_FBDZ_ID,TREE_NAME FROM PS_TZ_RS_MODAL_TBL WHERE TZ_SCORE_MODAL_ID=? AND TZ_JG_ID=?";
			Map<String, Object> sMap = sqlQuery.queryForMap(strSql4, new Object[] { StrScoreModalID, strCurrentOrg });
			if (sMap != null) {
				strFbdzID = sMap.get("TZ_M_FBDZ_ID") == null ? "" : String.valueOf(sMap.get("TZ_M_FBDZ_ID"));
				strTreeName = sMap.get("TREE_NAME") == null ? "" : String.valueOf(sMap.get("TREE_NAME"));
			}
			String strTreeNode = "";
			String strSql3 = "SELECT TREE_NODE FROM PSTREENODE WHERE TREE_NAME=? AND PARENT_NODE_NUM=0";
			strTreeNode = sqlQuery.queryForObject(strSql3, new Object[] { strTreeName }, "String");
			// 单个评委编号
			String str_tmp_pw = "";
			// 某个报考方向下：单个评委在某个成绩项的总分
			int dgpw_all_num = 0;
			// 某个报考方向下：单个评委完成的数量;
			int dgpw_wc_num = 0;
			// 循环数组的变量;
			int i = 0;
			// 多个评委完成的总数量;
			int more_pw_wc_num = 0;
			// 某个报考方向下：多个评委在某个成绩项的总分;
			int more_pw_all_num = 0;
			if (pwList != null && pwList.length > 0) {
				DecimalFormat df = new DecimalFormat("######0.00");
				// 评委计算平均分
				if (pwList.length == 1) {
					str_tmp_pw = pwList[1];
					// 某个报考方向下：单个评委在某个成绩项的总分
					dgpw_all_num = 0;
					String strSql1 = "SELECT SUM( B.TZ_SCORE_NUM) FROM PS_TZ_CP_PW_KS_TBL A,PS_TZ_CJX_TBL B ,PS_TZ_KSCLPSLS_TBL C  WHERE A.TZ_SCORE_INS_ID = B.TZ_SCORE_INS_ID AND A.TZ_APPLY_DIRC_ID = C.TZ_APPLY_DIRC_ID AND A.TZ_PWEI_OPRID = C.TZ_PWEI_OPRID AND A.TZ_APP_INS_ID = C.TZ_APP_INS_ID AND C.TZ_SUBMIT_YN <> 'C' AND A.TZ_APPLY_DIRC_ID = ? AND A.TZ_PWEI_OPRID = ? AND B.TZ_SCORE_ITEM_ID = ? AND C.TZ_CLPS_LUNC = ?";
					dgpw_all_num = sqlQuery.queryForObject(strSql1, new Object[] { strBatchID, str_tmp_pw, strScoreItemID, strClpsLunc }, "Integer");
					// 完成数量
					dgpw_wc_num = 0;
					String strSql2 = "SELECT COUNT(DISTINCT TZ_APP_INS_ID) FROM PS_TZ_KSCLPSLS_TBL WHERE TZ_APPLY_DIRC_ID = ? AND TZ_PWEI_OPRID = ? AND TZ_CLPS_LUNC = ? AND  TZ_SUBMIT_YN <> 'C'";
					dgpw_wc_num = sqlQuery.queryForObject(strSql2, new Object[] { strBatchID, str_tmp_pw, strClpsLunc }, "Integer");
					if (dgpw_all_num == 0 || dgpw_wc_num == 0) {
						str_pjf = "0";
					} else {
						double tmpDouble = (double) (dgpw_all_num / dgpw_wc_num);
						str_pjf = df.format(tmpDouble);
					}
				} else {
					for (i = 0; i < pwList.length; i++) {
						str_tmp_pw = "";
						str_tmp_pw = pwList[i];
						// 某个报考方向下：单个评委在某个成绩项的总分
						dgpw_all_num = 0;
						String strTmpSql3 = "SELECT SUM( B.TZ_SCORE_NUM) FROM PS_TZ_CP_PW_KS_TBL A,PS_TZ_MBA_CJX_TBL B ,PS_TZ_KSCLPSLS_TBL C WHERE A.TZ_SCORE_INS_ID = B.TZ_SCORE_INS_ID AND A.TZ_APPLY_DIRC_ID = C.TZ_APPLY_DIRC_ID AND A.TZ_PWEI_OPRID = C.TZ_PWEI_OPRID AND A.TZ_APP_INS_ID = C.TZ_APP_INS_ID AND C.TZ_SUBMIT_YN <> 'C' AND A.TZ_APPLY_DIRC_ID = ? AND A.TZ_PWEI_OPRID = ? AND B.TZ_SCORE_ITEM_ID = ? AND C.TZ_CLPS_LUNC = ?";
						dgpw_all_num = sqlQuery.queryForObject(strTmpSql3, new Object[] { strBatchID, str_tmp_pw, strScoreItemID, strClpsLunc }, "Integer");
						// 完成数量
						dgpw_wc_num = 0;
						String strSql2 = "SELECT COUNT(DISTINCT TZ_APP_INS_ID) FROM PS_TZ_KSCLPSLS_TBL WHERE TZ_APPLY_DIRC_ID = ? AND TZ_PWEI_OPRID = ? AND TZ_CLPS_LUNC = ? AND TZ_SUBMIT_YN <> 'C'SELECT COUNT(DISTINCT TZ_APP_INS_ID) FROM PS_TZ_KSCLPSLS_TBL WHERE TZ_APPLY_DIRC_ID = ? AND TZ_PWEI_OPRID = ? AND TZ_CLPS_LUNC = ? AND TZ_SUBMIT_YN <> 'C'";
						dgpw_wc_num = sqlQuery.queryForObject(strSql2, new Object[] { strBatchID, str_tmp_pw, strClpsLunc, strBatchID, str_tmp_pw, strClpsLunc }, "Integer");
						// 多个评委完成的总数量
						more_pw_wc_num = more_pw_wc_num + dgpw_wc_num;
						// 某个报考方向下：多个评委在某个成绩项的总分
						more_pw_all_num = more_pw_all_num + dgpw_all_num;
					}
					if (more_pw_wc_num == 0 || more_pw_all_num == 0) {
						str_pjf = "0";
					} else {
						double tmpDouble = (double) (more_pw_all_num / more_pw_wc_num);
						str_pjf = df.format(tmpDouble);
					}
				}
			}
		} catch (Exception e) {

		}
		return str_pjf;
	}
	@SuppressWarnings("unchecked")
	public String saveSubmitPwData(String strParams, String[] errMsg) {
		String strResponse = "\"success\"";
		JacksonUtil jacksonUtil = new JacksonUtil();
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

		try {
			String strCurrentOrg = tzLoginServiceImpl.getLoginedManagerOrgid(request);

			jacksonUtil.json2Map(strParams);
			String strClassID = jacksonUtil.getString("classID");
			String strBatchID = jacksonUtil.getString("batchID");

			String strContent = "";
			String strJudgeOpridList = jacksonUtil.getString("pwOpridList");
			String[] judgeOpridList = strJudgeOpridList.split(";");

			if (judgeOpridList != null && judgeOpridList.length > 0) {

				String debCountSQL = "SELECT ifnull(TZ_DQPY_LUNC,0) FROM PS_TZ_CLPS_GZ_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
				Integer debCount = sqlQuery.queryForObject(debCountSQL, new Object[] { strClassID, strBatchID }, "Integer");
				for (int i = 0; i < judgeOpridList.length; i++) {
					String strPwOprID = judgeOpridList[i];
					String strSql1 = "SELECT COUNT(TZ_APP_INS_ID) FROM PS_TZ_KSCLPSLS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID= ? AND TZ_PWEI_OPRID = ? AND TZ_CLPS_LUNC = ? AND TZ_SUBMIT_YN <> 'Y' AND TZ_SUBMIT_YN <> 'C'";
					Integer TZ_APP_INS_ID_NUM = sqlQuery.queryForObject(strSql1, new Object[] { strClassID, strBatchID, strPwOprID, debCount }, "Integer");
					if (TZ_APP_INS_ID_NUM != 0) {
						String strRealName = "";
						String strSql2 = "SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=?";
						strRealName = sqlQuery.queryForObject(strSql2, new Object[] { strPwOprID }, "String");

						strResponse = "\"评委 " + strRealName + " 有未提交考生数据，无法提交。\"";
						return strResponse;
					}
				}
			}

			errMsg[0] = "0";

		} catch (Exception e) {
			errMsg[0] = "100";
			errMsg[1] = e.toString();
		}
		return strResponse;
	}
	
	@SuppressWarnings("unchecked")
	public String tmpImportData(){
		String strClassId = "136";
		String strBatchId = "65";
		String strScoreModalId = "TZ_CLPS_MODEL";
		
		/*int i = 1;
		String strListSQL = "SELECT DISTINCT TZ_MSH_ID FROM PS_TZ_CLPS_TMP_T";
		List<Map<String, Object>> mapList1 = sqlQuery.queryForList(strListSQL);
		for (Object mapObj : mapList1) {
			Map<String, Object> result = (Map<String, Object>) mapObj;
			String strMshId = result.get("TZ_MSH_ID")==null?"":String.valueOf(result.get("TZ_MSH_ID"));
			String strSQL = "SELECT TZ_APP_INS_ID FROM PS_TZ_CLPS_TMP_T WHERE TZ_MSH_ID=? LIMIT ?,?";
			String strTmpAppInsId = sqlQuery.queryForObject(strSQL, new Object[]{strMshId,0,1}, "String");
			
			
			String strUpdateSQL = "UPDATE PS_TZ_CLPS_TMP_T SET TZ_APP_INS_ID=? WHERE TZ_MSH_ID=?";
			sqlQuery.update(strUpdateSQL, new Object[]{strTmpAppInsId,strMshId});
			System.out.println("-------处理第" + i + "条数据-------，面试申请号：" + strMshId +"，新报名表编号：" + strTmpAppInsId);
			i = i + 1;
		}*/
		String strListSQL = "SELECT TZ_APP_INS_ID, TZ_PWEI_OPRID,TZ_REAL_NAME,TZ_PWEI_NUM, TZ_KSH_PM, TZ_TOTAL, TZ_SCORE1, TZ_SCORE2, TZ_SCORE3, TZ_SCORE4, TZ_SCORE5, TZ_SCORE6, TZ_SCORE7, TZ_SCORE8, TZ_SCORE9, TZ_PY, TZ_GRPX, TZ_KSH_JY, TZ_TSQK FROM PS_TZ_CLPS_TMP_T limit 0,3000";
		List<Map<String, Object>> mapList1 = sqlQuery.queryForList(strListSQL);

		//评审规则轮次从1开始
		String strUpdateSQL1 = "UPDATE PS_TZ_CLPS_GZ_TBL SET TZ_DQPY_LUNC=1,TZ_SCPY_LUNC=0 WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
		sqlQuery.update(strUpdateSQL1, new Object[]{strClassId,strBatchId});
		
		//删除考生表
		String strDeleteSQL1 = "DELETE FROM PS_TZ_CLPS_KSH_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
		sqlQuery.update(strDeleteSQL1, new Object[]{strClassId,strBatchId});
		//删除考生关系表
		String strDeleteSQL2 = "DELETE FROM PS_TZ_CP_PW_KS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
		sqlQuery.update(strDeleteSQL2, new Object[]{strClassId,strBatchId});
		//考生评审历史
		String strDeleteSQL3 = "DELETE FROM PS_TZ_KSCLPSLS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
		sqlQuery.update(strDeleteSQL3, new Object[]{strClassId,strBatchId});
		//考生评审历史
		String strDeleteSQL4 = "DELETE FROM PS_TZ_CLPWPSLS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
		sqlQuery.update(strDeleteSQL4, new Object[]{strClassId,strBatchId});	
		int i=1;
		if (mapList1 != null && mapList1.size() > 0) {
			for (Object mapObj : mapList1) {
				Map<String, Object> result = (Map<String, Object>) mapObj;
				String strAppInsId = result.get("TZ_APP_INS_ID")==null?"":String.valueOf(result.get("TZ_APP_INS_ID"));
				String strPweiDLZH = result.get("TZ_PWEI_OPRID")==null?"":String.valueOf(result.get("TZ_PWEI_OPRID"));
				String strStuRealName = result.get("TZ_REAL_NAME")==null?"":String.valueOf(result.get("TZ_REAL_NAME"));
				String strKshPm = result.get("TZ_KSH_PM")==null?"0":String.valueOf(result.get("TZ_KSH_PM"));
				String strTotal = result.get("TZ_TOTAL")==null?"":String.valueOf(result.get("TZ_TOTAL"));
				String strScore1 = result.get("TZ_SCORE1")==null?"":String.valueOf(result.get("TZ_SCORE1"));
				String strScore2 = result.get("TZ_SCORE2")==null?"":String.valueOf(result.get("TZ_SCORE2"));
				String strScore3 = result.get("TZ_SCORE3")==null?"":String.valueOf(result.get("TZ_SCORE3"));
				String strScore4 = result.get("TZ_SCORE4")==null?"":String.valueOf(result.get("TZ_SCORE4"));
				String strScore5 = result.get("TZ_SCORE5")==null?"":String.valueOf(result.get("TZ_SCORE5"));
				String strScore6 = result.get("TZ_SCORE6")==null?"":String.valueOf(result.get("TZ_SCORE6"));
				String strScore7 = result.get("TZ_SCORE7")==null?"":String.valueOf(result.get("TZ_SCORE7"));
				String strScore8 = result.get("TZ_SCORE8")==null?"":String.valueOf(result.get("TZ_SCORE8"));
				String strScore9 = result.get("TZ_SCORE9")==null?"":String.valueOf(result.get("TZ_SCORE9"));
				String strPy = result.get("TZ_PY")==null?"":String.valueOf(result.get("TZ_PY"));
				String strGrpx = result.get("TZ_GRPX")==null?"":String.valueOf(result.get("TZ_GRPX"));
				String strKshJy = result.get("TZ_KSH_JY")==null?"":String.valueOf(result.get("TZ_KSH_JY"));
				String strTsqk = result.get("TZ_TSQK")==null?"":String.valueOf(result.get("TZ_TSQK"));
				
				//评委oprid
				String strQuerySQL1 = "SELECT OPRID FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_DLZH_ID=?";
				String strPwOprid = sqlQuery.queryForObject(strQuerySQL1, new Object[]{strPweiDLZH}, "String");
				//插入考生表
				String strExist = sqlQuery.queryForObject("SELECT'Y' FROM PS_TZ_CLPS_KSH_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=?", new Object[]{strClassId,strBatchId,strAppInsId}, "String");
				if("Y".equals(strExist)){
					
				}else{
					String strInsertSQL1 = "INSERT INTO PS_TZ_CLPS_KSH_TBL(TZ_CLASS_ID,TZ_APPLY_PC_ID,TZ_APP_INS_ID) VALUES(?,?,?)";
					if(strAppInsId!=null&&!"".equals(strAppInsId)){
						sqlQuery.update(strInsertSQL1, new Object[]{strClassId,strBatchId,strAppInsId});
					}
				}
				
				//插入评审历史表
				String strExist2 = sqlQuery.queryForObject("SELECT'Y' FROM PS_TZ_KSCLPSLS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=? AND TZ_PWEI_OPRID=? AND TZ_CLPS_LUNC=1", new Object[]{strClassId,strBatchId,strAppInsId,strPwOprid}, "String");
				if("Y".equals(strExist2)){
					
				}else{
					String strInsertSQL2 = "INSERT INTO PS_TZ_KSCLPSLS_TBL(TZ_CLASS_ID,TZ_APPLY_PC_ID,TZ_APP_INS_ID,TZ_PWEI_OPRID,TZ_CLPS_LUNC,TZ_GUANX_LEIX,TZ_KSH_PSPM,TZ_SUBMIT_YN) VALUES(?,?,?,?,?,?,?,?)";
					if(strAppInsId!=null&&!"".equals(strAppInsId)){
						sqlQuery.update(strInsertSQL2, new Object[]{strClassId,strBatchId,strAppInsId,strPwOprid,"1","A",strKshPm,"Y"});
					}
				}
				
				//评审历史记录
				String strExist3 = sqlQuery.queryForObject("SELECT'Y' FROM PS_TZ_CLPWPSLS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_PWEI_OPRID=? AND TZ_CLPS_LUNC=1", new Object[]{strClassId,strBatchId,strPwOprid}, "String");
				if("Y".equals(strExist3)){
					
				}else{
					String strInsertSQL4 = "INSERT INTO PS_TZ_CLPWPSLS_TBL(TZ_CLASS_ID,TZ_APPLY_PC_ID,TZ_PWEI_OPRID,TZ_CLPS_LUNC,TZ_SUBMIT_YN) VALUES(?,?,?,?,?)";
					sqlQuery.update(strInsertSQL4, new Object[]{strClassId,strBatchId,strPwOprid,"1","Y"});
				}
											
				//插入考生评委关系表
				String strInsertSQL3 = "INSERT INTO PS_TZ_CP_PW_KS_TBL(TZ_CLASS_ID,TZ_APPLY_PC_ID,TZ_APP_INS_ID,TZ_PWEI_OPRID,TZ_GUANX_LEIX,TZ_SCORE_INS_ID,TZ_KSH_PSPM) VALUES(?,?,?,?,?,?,?)";
				if(strAppInsId!=null&&!"".equals(strAppInsId)){
					//成绩单编号
					int scoreInsId = getSeqNum.getSeqNum("TZ_SRMBAINS_TBL", "TZ_SCORE_INS_ID");
					//成绩单实例表
					String strInsertSQL31 = "INSERT INTO PS_TZ_SRMBAINS_TBL(TZ_SCORE_INS_ID,TZ_SCORE_MODAL_ID,TZ_SCORE_INS_DATE) VALUES(?,?,?)";
					sqlQuery.update(strInsertSQL31, new Object[]{scoreInsId,strScoreModalId,"2099-12-31"});
					//考生关系表
					sqlQuery.update(strInsertSQL3, new Object[]{strClassId,strBatchId,strAppInsId,strPwOprid,"A",scoreInsId,strKshPm});
					
					//成绩单表
					String strTotalScoreItemID = "Total";//总分
					String strScoreItemId1 = "XXZB";//显性指标
					String strScoreItemId2 = "BKXX";//本科学校和学习情况
					String strScoreItemId3 = "XXHDJL";//英语水平
					String strScoreItemId4 = "YYSP";//国际化工作背景
					String strScoreItemId5 = "ZYBJ";//职业背景
					String strScoreItemId6 = "YXZB";//隐性指标
					String strScoreItemId7 = "CJDJ";//领导力
					String strScoreItemId8 = "SWJC";//思维决策
					String strScoreItemId9 = "TDHZ";//沟通和建立关系的能力
					
					String strScoreItemId10 = "PY";//沟通和建立关系的能力
					String strScoreItemId11 = "GRPX";//个人品行问题
					String strScoreItemId12 = "KSJY";//给考生的建议
					String strScoreItemId13 = "TSQKTJ";//特殊情况推荐
					//成绩实例表
					String strInsertCJSQL = "INSERT INTO PS_TZ_CJX_TBL(TZ_SCORE_INS_ID,TZ_SCORE_ITEM_ID,TZ_SCORE_NUM,TZ_SCORE_PY_VALUE) VALUES(?,?,?,?)";
					if(strTotal==null||"".equals(strTotal)){
						strTotal = "0";
					}
					sqlQuery.update(strInsertCJSQL, new Object[]{scoreInsId,strTotalScoreItemID,strTotal,""});
					if(strScore1==null||"".equals(strScore1)){
						strScore1 = "0";
					}
					sqlQuery.update(strInsertCJSQL, new Object[]{scoreInsId,strScoreItemId1,strScore1,""});
					if(strScore2==null||"".equals(strScore2)){
						strScore2 = "0";
					}
					sqlQuery.update(strInsertCJSQL, new Object[]{scoreInsId,strScoreItemId2,strScore2,""});
					if(strScore3==null||"".equals(strScore3)){
						strScore3 = "0";
					}
					sqlQuery.update(strInsertCJSQL, new Object[]{scoreInsId,strScoreItemId3,strScore3,""});
					if(strScore4==null||"".equals(strScore4)){
						strScore4 = "0";
					}
					sqlQuery.update(strInsertCJSQL, new Object[]{scoreInsId,strScoreItemId4,strScore4,""});
					if(strScore5==null||"".equals(strScore5)){
						strScore5 = "0";
					}
					sqlQuery.update(strInsertCJSQL, new Object[]{scoreInsId,strScoreItemId5,strScore5,""});
					if(strScore6==null||"".equals(strScore6)){
						strScore6 = "0";
					}
					sqlQuery.update(strInsertCJSQL, new Object[]{scoreInsId,strScoreItemId6,strScore6,""});
					if(strScore7==null||"".equals(strScore7)){
						strScore7 = "0";
					}
					sqlQuery.update(strInsertCJSQL, new Object[]{scoreInsId,strScoreItemId7,strScore7,""});
					if(strScore8==null||"".equals(strScore8)){
						strScore8 = "0";
					}
					sqlQuery.update(strInsertCJSQL, new Object[]{scoreInsId,strScoreItemId8,strScore8,""});
					if(strScore9==null||"".equals(strScore9)){
						strScore9 = "0";
					}
					sqlQuery.update(strInsertCJSQL, new Object[]{scoreInsId,strScoreItemId9,strScore9,""});
					
					//相关评语
					sqlQuery.update(strInsertCJSQL, new Object[]{scoreInsId,strScoreItemId10,"0",strPy});
					sqlQuery.update(strInsertCJSQL, new Object[]{scoreInsId,strScoreItemId11,"0",strGrpx});
					sqlQuery.update(strInsertCJSQL, new Object[]{scoreInsId,strScoreItemId12,"0",strKshJy});
					sqlQuery.update(strInsertCJSQL, new Object[]{scoreInsId,strScoreItemId13,"0",strTsqk});
					
				}
				System.out.println("-------正在处理第" + i + "条数据，报考人为：" + strStuRealName + "-------");				
				i = i + 1;
			}
		}
		
		return "";
	}
	
	@SuppressWarnings("unchecked")
	public String right(String strValue, int num) {
		String returnValue = "";
		try {
			if (strValue != null && !"".equals(strValue)) {
				if (num > 0) {
					int strLen = strValue.length();
					int beginIndex = strLen - num;
					if (beginIndex < 0) {
						beginIndex = 0;
					}
					returnValue = strValue.substring(beginIndex);
				}
			}
		} catch (Exception e) {
			/**/
		}
		return returnValue;
	}
	@SuppressWarnings("unchecked")
	public int find(String[] arr, String strValue) {
		int index = -1;
		if (arr != null && arr.length > 0) {
			for (int i = 0; i < arr.length; i++) {
				String tmpValue = arr[i];
				if (strValue.equals(tmpValue)) {
					return i;
				}
			}
		}
		return index;
	}
}
