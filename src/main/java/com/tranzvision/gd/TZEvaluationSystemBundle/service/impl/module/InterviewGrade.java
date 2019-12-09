package com.tranzvision.gd.TZEvaluationSystemBundle.service.impl.module;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZAutomaticScreenBundle.dao.PsTzCjxTblMapper;
import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCjxTblKey;
import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCjxTblWithBLOBs;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZMbaPwMspsBundle.dao.PsTzMpPwKsTblMapper;
import com.tranzvision.gd.TZMbaPwMspsBundle.model.PsTzMpPwKsTbl;
import com.tranzvision.gd.TZMbaPwMspsBundle.model.PsTzMpPwKsTblKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 面试评审-打分
 * 
 * @author ShaweYet
 * @since 2019-08-01
 */
@Service
public class InterviewGrade extends FrameworkImpl {

	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private TZGDObject tzSQLObject;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private PsTzMpPwKsTblMapper psTzMpPwKsTblMapper;
	@Autowired
	private PsTzCjxTblMapper psTzCjxTblMapper;
	@Autowired
	private GetSeqNum getSeqNum;

	private final String EMPTY_JSON_OBJECT = "{}";

	/**
	 * 业务处理器
	 * 
	 * @param strParams 请求报文json字符串
	 * 
	 * @param errorMsg 错误信息 errorMsg[0] String 错误编码，"0"表示正常，"1"表示错误 errorMsg[1]
	 * String 错误消息
	 * 
	 * @return String 务必返回json格式的字符串
	 * 
	 * @description 通过解析strParams中的如type参数 或者通过request中的type参数判断用户的具体操作执行不同的方法
	 *
	 */
	public String processor(String strParams, String[] errorMsg) {
		String strRtn = EMPTY_JSON_OBJECT;
		JacksonUtil jacksonUtil = new JacksonUtil();

		jacksonUtil.json2Map(strParams);

		String type = jacksonUtil.getString("type");

		try {
			if ("model".equals(type)) {
				strRtn = this.getModel(jacksonUtil, errorMsg);
			}
			if ("submit".equals(type)) {
				strRtn = this.submitScore(jacksonUtil, errorMsg);
			}
			
			// 静默保存，出错不显示
			if("false".equals(jacksonUtil.getString("toast"))){
				errorMsg[0] = "0";
			}
			
		} catch (Exception e) {
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}

		return strRtn;
	}

	/**
	 * 获取模型数据
	 *
	 * @return String
	 */
	private String getModel(JacksonUtil jacksonUtil, String[] errorMsg) {

		// 基础参数校验
		String classId = jacksonUtil.getString("classId");
		String batchId = jacksonUtil.getString("batchId");
		String appInsId = jacksonUtil.getString("appInsId");

		if (null == classId || "".equals(classId) || null == batchId || "".equals(batchId) || null == batchId
				|| "".equals(batchId)) {
			errorMsg[0] = "1";
			errorMsg[1] = "参数不正确，请检查后重试！";
			return EMPTY_JSON_OBJECT;
		}

		String currentOprId = tzLoginServiceImpl.getLoginedManagerOprid(request);
		String currentOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);

		// 评委权限校验
		if (!checkPermission(classId, batchId, currentOprId)) {
			errorMsg[0] = "1";
			errorMsg[1] = "您没有权限访问！";
			return EMPTY_JSON_OBJECT;
		}

		// 判断考生签到状态
		if (!checkCheckinStatus(classId, batchId, appInsId)) {
			errorMsg[0] = "1";
			errorMsg[1] = "该学员尚未签到，无法进行打分";
			return EMPTY_JSON_OBJECT;
		}
		
		// 返回值Map对象
		Map<String, Object> returnMap = new HashMap<String, Object>();

		// 考生信息：姓名，单位，职位，成绩单编号
		String scoreInsId = "";
		String applicantSql = "SELECT A.TZ_REALNAME,A.TZ_COMPANY_NAME,A.TZ_POSITION,B.TZ_SCORE_INS_ID "
				+ "FROM PS_TZ_MSPS_KS_VW A,PS_TZ_MP_PW_KS_TBL B "
				+ "WHERE A.TZ_CLASS_ID=? AND A.TZ_APPLY_PC_ID=? AND A.TZ_APP_INS_ID=? "
				+ "AND A.TZ_CLASS_ID=B.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID=B.TZ_APPLY_PC_ID AND A.TZ_APP_INS_ID=B.TZ_APP_INS_ID "
				+ "AND B.TZ_PWEI_OPRID=?";
		Map<String, Object> applicantMap = sqlQuery.queryForMap(applicantSql,
				new Object[] { classId, batchId, appInsId, currentOprId });

		if (null != applicantMap) {
			scoreInsId = strVal(applicantMap.get("TZ_SCORE_INS_ID"));
			returnMap.put("name", applicantMap.get("TZ_REALNAME"));
			returnMap.put("company", applicantMap.get("TZ_COMPANY_NAME"));
			returnMap.put("position", applicantMap.get("TZ_POSITION"));
		}

		// 评委批次整体提交状态;
		String strSubmitState = sqlQuery.queryForObject(
				"SELECT TZ_SUBMIT_YN FROM PS_TZ_MSPWPSJL_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_PWEI_OPRID=?",
				new Object[] { classId, batchId, currentOprId }, "String");
		returnMap.put("submitState", strSubmitState);

		// 面试成绩模型;
		String interviewScoreModel = sqlQuery.queryForObject(
				"SELECT TZ_MSCJ_SCOR_MD_ID FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID=?", new Object[] { classId },
				"String");
		if (null == interviewScoreModel || "".equals(interviewScoreModel)) {
			errorMsg[0] = "1";
			errorMsg[1] = "没有设置面试评审成绩模型，请联系管理员！";
			return EMPTY_JSON_OBJECT;
		}

		// 面试成绩模型树;
		String treeName = sqlQuery.queryForObject("SELECT TREE_NAME FROM PS_TZ_RS_MODAL_TBL WHERE TZ_SCORE_MODAL_ID=?",
				new Object[] { interviewScoreModel }, "String");
		if (null == treeName || "".equals(treeName)) {
			errorMsg[0] = "1";
			errorMsg[1] = "没有为面试成绩模型配置成绩树，请联系管理员！";
			return EMPTY_JSON_OBJECT;
		}

		// 评审说明：查找分布对照表，生成评审说明;
		String disCtrlTable = sqlQuery.queryForObject(
				"SELECT TZ_M_FBDZ_ID FROM PS_TZ_RS_MODAL_TBL WHERE TZ_JG_ID=? AND TZ_SCORE_MODAL_ID=?",
				new Object[] { currentOrgId, interviewScoreModel }, "String");

		String disCtrlSql = "SELECT TZ_M_FBDZ_MX_SM AS description FROM PS_TZ_FBDZ_MX_TBL WHERE TZ_M_FBDZ_ID=? ORDER BY TZ_M_FBDZ_MX_XH ASC";
		List<Map<String, Object>> disCtrlList = sqlQuery.queryForList(disCtrlSql, new Object[] { disCtrlTable });
		if (null != disCtrlList && disCtrlList.size() > 0) {
			returnMap.put("evaluationDesc", disCtrlList);
		}

		// 评审打分模型
		String modelSql = null;
		try {
			modelSql = tzSQLObject.getSQLText("SQL.TZEvaluationSystemBundle.TzInterviewModel");
			List<Map<String, Object>> modelList = sqlQuery.queryForList(modelSql, new Object[] { interviewScoreModel });
			if (null != modelList && modelList.size() > 0) {
				for (int i = 0; i < modelList.size(); i++) {
					/*
					 * FIELDS: TREE_NODE,PARENT_NODE_NAME,TZ_XS_MC
					 * TZ_SCORE_ITEM_TYPE,TZ_NO_LEAF,TREE_LEVEL_NUM
					 * TZ_SCORE_LIMITED,TZ_SCORE_LIMITED2,TZ_SCORE_PY_ZSLIM
					 * TZ_SCORE_PY_ZSLIM0,TZ_SCORE_ITEM_DFSM,TZ_SCORE_ITEM_CKWT
					 * TZ_SCORE_ITEM_MSFF,TZ_SCORE_HZ,TZ_SCORE_QZ,TZ_SCR_SQZ
					 **/
					Map<String, Object> modelMap = modelList.get(i);

					// 查询成绩项分值、评语值、下拉框选项值
					String scoreItemValue = "", scoreItemComment = "", scoreSelectItemId = "";
					Map<String, Object> scoreItemMap = sqlQuery.queryForMap(
							"SELECT TZ_SCORE_NUM,TZ_SCORE_PY_VALUE,TZ_CJX_XLK_XXBH FROM PS_TZ_CJX_TBL WHERE TZ_SCORE_INS_ID=? AND TZ_SCORE_ITEM_ID=?",
							new Object[] { scoreInsId, modelMap.get("TREE_NODE") });
					if (scoreItemMap != null) {
						scoreItemValue = strVal(scoreItemMap.get("TZ_SCORE_NUM"));
						scoreItemComment = strVal(scoreItemMap.get("TZ_SCORE_PY_VALUE"));
						scoreSelectItemId = strVal(scoreItemMap.get("TZ_CJX_XLK_XXBH"));
					}

					// 下拉框
					if ("D".equals(modelMap.get("TZ_SCORE_ITEM_TYPE"))) {
						List<Map<String, Object>> selectItemList = sqlQuery.queryForList(
								"SELECT TZ_CJX_XLK_XXBH,TZ_CJX_XLK_XXMC,TZ_CJX_XLK_MRZ,TZ_CJX_XLK_XXFZ FROM PS_TZ_ZJCJXXZX_T WHERE TREE_NAME=? AND TZ_SCORE_ITEM_ID=?",
								new Object[] { treeName, modelMap.get("TREE_NODE") });
						if (selectItemList != null) {
							modelMap.put("TZ_SCORE_SELECT_ITEM_LIST", selectItemList);

							for (int j = 0; j < selectItemList.size(); j++) {
								Map<String, Object> selectItemMap = selectItemList.get(j);
								// 赋默认值
								if ("Y".equals(selectItemMap.get("TZ_CJX_XLK_MRZ")) && none(scoreSelectItemId)) {
									scoreItemValue = strVal(selectItemMap.get("TZ_CJX_XLK_XXFZ"));
									scoreSelectItemId = strVal(selectItemMap.get("TZ_CJX_XLK_XXBH"));
								}
							}
						}
					}

					modelMap.put("TZ_SCORE_NUM", Double.parseDouble(0 + scoreItemValue));
					modelMap.put("TZ_SCORE_COMMENT", scoreItemComment);
					modelMap.put("TZ_SCORE_SELECT_ITEM_ID", scoreSelectItemId);
				}
			}

			returnMap.put("model", modelList);
		} catch (TzSystemException e) {
			errorMsg[0] = "1";
			errorMsg[1] = "业务处理异常，错误信息：" + e.toString();
			e.printStackTrace();
		}

		return jacksonUtil.Map2json(returnMap);
	}

	/**
	 * 保存打分信息
	 * 
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	private String submitScore(JacksonUtil jacksonUtil, String[] errorMsg) {

		// 基础参数校验
		String classId = jacksonUtil.getString("classId");
		String batchId = jacksonUtil.getString("batchId");
		String appInsId = jacksonUtil.getString("appInsId");

		if (null == classId || "".equals(classId) || null == batchId || "".equals(batchId) || null == appInsId
				|| "".equals(appInsId)) {
			errorMsg[0] = "1";
			errorMsg[1] = "参数不正确，请检查后重试！";
			return EMPTY_JSON_OBJECT;
		}

		String currentOprId = tzLoginServiceImpl.getLoginedManagerOprid(request);

		// 评委权限校验
		if (!checkPermission(classId, batchId, currentOprId)) {
			errorMsg[0] = "1";
			errorMsg[1] = "您没有权限访问！";
			return EMPTY_JSON_OBJECT;
		}

		// 判断当前批次总体评审状态，若未开放，则不能添加;
		String batchStatus = sqlQuery.queryForObject(
				"SELECT TZ_DQPY_ZT FROM PS_TZ_MSPS_GZ_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?",
				new Object[] { classId, batchId }, "String");
		if (!"A".equals(batchStatus)) {
			errorMsg[0] = "1";
			errorMsg[1] = "该批次的评审未开放。";
			return EMPTY_JSON_OBJECT;
		}

		// 判断考生签到状态
		if (!checkCheckinStatus(classId, batchId, appInsId)) {
			errorMsg[0] = "1";
			errorMsg[1] = "该学员尚未签到，无法进行打分";
			return EMPTY_JSON_OBJECT;
		}
		
		// 评委批次整体提交状态;
		String strSubmitState = sqlQuery.queryForObject(
				"SELECT TZ_SUBMIT_YN FROM PS_TZ_MSPWPSJL_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_PWEI_OPRID=?",
				new Object[] { classId, batchId, currentOprId }, "String");
		if ("Y".equals(strSubmitState)) {
			errorMsg[0] = "1";
			errorMsg[1] = "批次已经整体提交，无法保存评审数据。";
			return EMPTY_JSON_OBJECT;
		}

		// 面试成绩模型;
		String interviewScoreModel = sqlQuery.queryForObject(
				"SELECT TZ_MSCJ_SCOR_MD_ID FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID=?", new Object[] { classId },
				"String");
		if (null == interviewScoreModel || "".equals(interviewScoreModel)) {
			errorMsg[0] = "1";
			errorMsg[1] = "没有设置面试评审成绩模型，请联系管理员！";
			return EMPTY_JSON_OBJECT;
		}

		// 面试成绩模型树;
		String treeName = sqlQuery.queryForObject("SELECT TREE_NAME FROM PS_TZ_RS_MODAL_TBL WHERE TZ_SCORE_MODAL_ID=?",
				new Object[] { interviewScoreModel }, "String");
		if (null == treeName || "".equals(treeName)) {
			errorMsg[0] = "1";
			errorMsg[1] = "没有为面试成绩模型配置成绩树，请联系管理员！";
			return EMPTY_JSON_OBJECT;
		}

		// 保存评议数据
		List<Map<String, Object>> model = (List<Map<String, Object>>)jacksonUtil.getList("model");
		if( null != model){
			saveScoreItems(classId, batchId, appInsId, currentOprId, interviewScoreModel,model, errorMsg);
		}else{
			errorMsg[0] = "1";
			errorMsg[1] = "缺少评审数据，请检查后重试！";
			return EMPTY_JSON_OBJECT;
		}
		
		// 计算排名和平均分
		if("0".equals(errorMsg[0])){
			updateRank(classId, batchId, appInsId,currentOprId);
			updateTally(classId, batchId, appInsId);
		}		
		
		// 返回值Map对象
		Map<String, Object> returnMap = new HashMap<String, Object>();

		return jacksonUtil.Map2json(returnMap);
	}

	/**
	 * 保存打分项
	 * 
	 * @return
	 */
	private void saveScoreItems(String classId, String batchId, String appInsId, String currentOprId,
			String interviewScoreModel, List<Map<String, Object>> model, String[] errorMsg) {

		// 考生在当前评委下的成绩单ID;
		long scoreInsId = sqlQuery.queryForObject(
				"SELECT TZ_SCORE_INS_ID FROM PS_TZ_MP_PW_KS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=? AND TZ_PWEI_OPRID=?",
				new Object[] { classId, batchId, appInsId, currentOprId }, "Long");

		try {
			Timestamp currentTimestamp = new Timestamp(new Date().getTime());

			if (scoreInsId == 0) {
				scoreInsId = creatScoreInsId();
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Timestamp deadline = new Timestamp(dateFormat.parse("2099-12-31").getTime());

				sqlQuery.update(
						"INSERT INTO PS_TZ_SRMBAINS_TBL(TZ_SCORE_INS_ID,TZ_SCORE_MODAL_ID,TZ_SCORE_INS_DATE,ROW_ADDED_DTTM,ROW_ADDED_OPRID,ROW_LASTMANT_DTTM,ROW_LASTMANT_OPRID) "
								+ "VALUES(?,?,?,?,?,?,?)",
						new Object[] { scoreInsId, interviewScoreModel, deadline, currentTimestamp, currentOprId,
								currentTimestamp, currentOprId });
			}

			// 更新面试评审评委考生关系表
			sqlQuery.update(
					"UPDATE PS_TZ_MP_PW_KS_TBL SET TZ_DELETE_ZT='N',TZ_PSHEN_ZT='Y',TZ_SCORE_INS_ID=? ,ROW_LASTMANT_DTTM=?,ROW_LASTMANT_OPRID=? WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=? AND TZ_PWEI_OPRID=?",
					new Object[] { scoreInsId, currentTimestamp, currentOprId, classId, batchId, appInsId,
							currentOprId });

			// TODO 成绩项校验，已由前端进行校验，服务端暂不进行校验

			// 开始保存成绩项
			for (int i = 0; i < model.size(); i++) {
				Map<String, Object> scoreItem = model.get(i);

				String itemId = strVal(scoreItem.get("TREE_NODE"));

				sqlQuery.update("DELETE FROM PS_TZ_CJX_TBL WHERE TZ_SCORE_INS_ID=? AND TZ_SCORE_ITEM_ID= ?",
						new Object[] { scoreInsId, itemId });

				PsTzCjxTblKey PsTzCjxTblKey = new PsTzCjxTblKey();
				PsTzCjxTblKey.setTzScoreInsId(scoreInsId);
				PsTzCjxTblKey.setTzScoreItemId(itemId);
				PsTzCjxTblWithBLOBs psTzCjxTbl = psTzCjxTblMapper.selectByPrimaryKey(PsTzCjxTblKey);
				if (psTzCjxTbl == null) {
					psTzCjxTbl = new PsTzCjxTblWithBLOBs();
					psTzCjxTbl.setTzScoreInsId(scoreInsId);
					psTzCjxTbl.setTzScoreItemId(itemId);
				}

				// 分值
				BigDecimal scoreNum = BigDecimal.valueOf(Double.valueOf(strVal(scoreItem.get("TZ_SCORE_NUM"))));
				psTzCjxTbl.setTzScoreNum(scoreNum);

				// 评语值
				String comment = strVal(scoreItem.get("TZ_SCORE_COMMENT"));
				psTzCjxTbl.setTzScorePyValue(comment);

				// 选项编号
				String selectOption = strVal(scoreItem.get("TZ_SCORE_SELECT_ITEM_ID"));
				psTzCjxTbl.setTzCjxXlkXxbh(selectOption);

				psTzCjxTblMapper.insert(psTzCjxTbl);
			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "保存失败：" + e.toString();
		}

	}

	/**
	 * 检查评委账号的权限
	 * 
	 * @return boolean 
	 */
	private boolean checkPermission(String classId, String batchId, String currentOprId) {
		String permission = sqlQuery.queryForObject(
				"SELECT 'Y' from PS_TZ_MSPS_PW_TBL "
						+ " WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? and tz_pwei_zhzt='A' and TZ_pwei_oprid = ?",
				new Object[] { classId, batchId, currentOprId }, "String");

		return "Y".equals(permission);
	}
	
	/**
	 * 检查考生是否签到
	 * 
	 * @return boolean 
	 */
	private boolean checkCheckinStatus(String classId, String batchId, String appInsId) {
		String checkedIn = sqlQuery.queryForObject(
				"SELECT 'Y' from PS_TZ_MSPS_KSH_TBL "
						+ " WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=? AND TZ_CHECKIN_DTTM IS NOT NULL",
				new Object[] { classId, batchId, appInsId }, "String");

		return "Y".equals(checkedIn);
	}
	
	
	/**
	 * 对象转字符串
	 * 
	 * @return String 
	 */
	private String strVal(Object o) {
		if (null == o) {
			return "";
		} else {
			return String.valueOf(o);
		}
	}

	/**
	 * 判断对象是否为空
	 * 
	 * @return boolean
	 */
	private boolean none(Object o) {
		return (null == o || "".equals(o));
	}

	/**
	 * 生成成绩单编号
	 * 
	 * @return
	 */
	private Long creatScoreInsId(){
		return (long) getSeqNum.getSeqNum("TZ_SRMBAINS_TBL", "TZ_SCORE_INS_ID");
	}
	
	/**
	 * 计算排名
	 * 
	 * @return
	 */
	private void updateRank(String classId, String batchId, String appInsId,String currentOprId) {

		try {
			/* 当前机构 */
			String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);

			String sql = tzSQLObject.getSQLText("SQL.TZEvaluationSystemBundle.TzInterviewExamineeRank");

			List<Map<String, Object>> listData = sqlQuery.queryForList(sql,
					new Object[] { orgId, classId, batchId, currentOprId });

			for (Map<String, Object> mapData : listData) {

				Long bmbId = 0L;
				String strBmbId = mapData.get("TZ_APP_INS_ID") == null ? "" : mapData.get("TZ_APP_INS_ID").toString();
				if (!"".equals(strBmbId)) {
					bmbId = Long.valueOf(strBmbId);
				}

				String strRank = mapData.get("XUHAO") == null ? "" : mapData.get("XUHAO").toString();
				if (strRank.indexOf(".") > 0) {
					strRank = strRank.substring(0, strRank.indexOf("."));
				}

				PsTzMpPwKsTblKey psTzMpPwKsTblKey = new PsTzMpPwKsTblKey();
				psTzMpPwKsTblKey.setTzClassId(classId);
				psTzMpPwKsTblKey.setTzApplyPcId(batchId);
				psTzMpPwKsTblKey.setTzAppInsId(bmbId);
				psTzMpPwKsTblKey.setTzPweiOprid(currentOprId);

				PsTzMpPwKsTbl psTzMpPwKsTbl = psTzMpPwKsTblMapper.selectByPrimaryKey(psTzMpPwKsTblKey);
				if (psTzMpPwKsTbl == null) {

				} else {
					psTzMpPwKsTbl.setTzKshPspm(strRank);
					if (bmbId.equals(appInsId)) {
						psTzMpPwKsTbl.setRowLastmantDttm(new Date());
						psTzMpPwKsTbl.setRowLastmantOprid(currentOprId);
					}
					psTzMpPwKsTblMapper.updateByPrimaryKeySelective(psTzMpPwKsTbl);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 计算考生面试总分
	 * 
	 * @return
	 */
	private void updateTally(String classId, String batchId, String appInsId) {

		try {
			/* 当前机构 */
			String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);

			String sql = tzSQLObject.getSQLText("SQL.TZEvaluationSystemBundle.TzInterviewExamineeTally");
			sql += " HAVING TZ_APP_INS_ID=?";
			
			List<Map<String, Object>> listData = sqlQuery.queryForList(sql,
					new Object[] { orgId, classId, batchId, appInsId });

			for (Map<String, Object> mapData : listData) {
				Long appInsIdLong = (Long) mapData.get("TZ_APP_INS_ID");
				BigDecimal tally = ((BigDecimal) mapData.get("TZ_SCORE")).setScale(1,BigDecimal.ROUND_HALF_UP);
				sqlQuery.update("UPDATE PS_TZ_MSPS_KSH_TBL SET TZ_SCORE=? WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=?",
						new Object[]{tally,classId,batchId,appInsIdLong});
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
