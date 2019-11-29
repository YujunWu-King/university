package com.tranzvision.gd.TZEvaluationSystemBundle.service.impl;

import java.util.Date;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

import org.mozilla.javascript.IdFunctionCall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZMbaPwClpsBundle.dao.PsTzClpsKshTblMapper;
import com.tranzvision.gd.TZMbaPwClpsBundle.dao.PsTzClpskspwTblMapper;
import com.tranzvision.gd.TZMbaPwClpsBundle.dao.PsTzCpPwKsTblMapper;
import com.tranzvision.gd.TZMbaPwClpsBundle.dao.PsTzKsclpslsTblMapper;
import com.tranzvision.gd.TZMbaPwClpsBundle.dao.PsTzPwkspcTmpTMapper;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzClpsKshTbl;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzClpsKshTblKey;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzClpskspwTbl;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzClpskspwTblKey;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzCpPwKsTbl;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzCpPwKsTblKey;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzKsclpslsTbl;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzKsclpslsTblKey;
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzPwkspcTmpT;
import com.tranzvision.gd.TZScoreModeManagementBundle.service.impl.TzScoreInsCalculationObject;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
import com.tranzvision.gd.util.sql.type.TzRecord;

/**
 * MBA材料面试评审-评委系统-材料评审-打分页
 *
 * @author LUYAN
 * @since 2017-02-08
 */

@Service("com.tranzvision.gd.TZEvaluationSystemBundle.service.impl.MaterialEvaluationScoreImpl")

public class MaterialEvaluationScoreImpl extends FrameworkImpl{

	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private PsTzCpPwKsTblMapper psTzCpPwKsTblMapper;
	@Autowired
	private PsTzKsclpslsTblMapper psTzKsclpslsTblMapper;
	@Autowired
	private PsTzPwkspcTmpTMapper psTzPwkspcTmpTMapper;
	@Autowired
	private PsTzClpsKshTblMapper psTzClpsKshTblMapper;
	@Autowired
	private PsTzClpskspwTblMapper psTzClpskspwTblMapper;
	@Autowired
	private TzScoreInsCalculationObject tzScoreInsCalculationObject;
	@Autowired
	private TZGDObject tzSQLObject;
	@Autowired
	private GetHardCodePoint getHardCodePoint;

	//用于同步评委抽取考生的信号变量，避免同一个服务内部对数据库资源的竞争
	private static Semaphore nextLock = new Semaphore(1,true);
	//用于控制访问量的信号变量，避免评委同时抽取考生对服务器造成过大压力
	private static Semaphore nextLockCounter = new Semaphore(10,true);


	@Override
	public String tzQuery(String strParams,String[] errMsg) {
		String strRtn = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {
			jacksonUtil.json2Map(strParams);

			String classId = jacksonUtil.getString("classId");
			String applyBatchId = jacksonUtil.getString("applyBatchId");
			String bmbId = jacksonUtil.getString("bmbId");
			String mshId = jacksonUtil.getString("mssqh");
			String messageCode = "0";
			String message = "";

			if(classId != null && applyBatchId != null && bmbId != null) {
				strRtn = this.getExamineeScoreInfo(classId, applyBatchId, bmbId, mshId,messageCode,message,errMsg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRtn;
	}

	@Override
	public String tzQueryList(String strParams,int numLimit,int numStart,String[] errMsg) {
		String strRtn="{}";
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {
			jacksonUtil.json2Map(strParams);

			String classId = jacksonUtil.getString("classId");
			String applyBatchId = jacksonUtil.getString("applyBatchId");
			String queryType = jacksonUtil.getString("queryType");

			if("KSLB".equals(queryType)) {
				//考生列表
				strRtn = this.getExamineeList(classId,applyBatchId,errMsg);
			}

		} catch(Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRtn;
	}


	@SuppressWarnings("unchecked")
	@Override
	public String tzUpdate(String[] actData,String[] errMsg) {
		String strRtn = "";
		Map<String, Object> mapRet = new HashMap<String,Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {

			int num = 0;
			for(num=0;num<actData.length;num++) {
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);

				String result = "";
				String resultMsg = "";

				String classId = jacksonUtil.getString("ClassID");
				String applyBatchId = jacksonUtil.getString("BatchID");
				String bmbId = jacksonUtil.getString("KSH_BMBID");
				String mshId = jacksonUtil.getString("KSH_MSSQH");
				String type = jacksonUtil.getString("OperationType");

				//当前登录人
				String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
				//当前机构
				String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);

				String sql;

				//当前评审轮次、实时计算评委偏差
				sql = "SELECT TZ_DQPY_LUNC,TZ_REAL_TIME_PWPC FROM PS_TZ_CLPS_GZ_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
				Map<String, Object> mapData = sqlQuery.queryForMap(sql, new Object[]{classId,applyBatchId});

				if(mapData!=null) {
					String dqpyLunc = mapData.get("TZ_DQPY_LUNC") == null ? "" : mapData.get("TZ_DQPY_LUNC").toString();
					String realPwpc = mapData.get("TZ_REAL_TIME_PWPC") == null ? "" : mapData.get("TZ_REAL_TIME_PWPC").toString();


					//判断当前评审轮次是否已提交，如果已提交，不允许修改
					sql = "SELECT TZ_SUBMIT_YN FROM PS_TZ_CLPWPSLS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_PWEI_OPRID=? AND TZ_CLPS_LUNC=?";
					String dqpyLuncState = sqlQuery.queryForObject(sql, new Object[]{classId,applyBatchId,oprid,dqpyLunc},"String");

					if("Y".equals(dqpyLuncState)) {
						result = "1";
						resultMsg = "评议数据已经提交，不允许对考生数据进行修改";
					} else {

						System.out.println("材料评审评委端打分页保存Begin");
						System.out.println("班级编号："+classId+"-->批次编号："+applyBatchId+"-->报名表编号："+bmbId+"-->评委OPRID："+oprid);

						//保存成绩项
						String saveScoreItemRtn = this.scoreItemSave(classId,applyBatchId,bmbId,strForm,errMsg);
						jacksonUtil.json2Map(saveScoreItemRtn);
						String saveRtnResult = jacksonUtil.getString("result");

						if("0".equals(saveRtnResult)) {

							System.out.println("保存成绩项成功");

							//根据配置判断是否实时计算评委间偏差
							if("Y".equals(realPwpc)) {
								this.calculatePwDeviation(classId,applyBatchId,bmbId,dqpyLunc,oprid,errMsg);
							}

							//更新考生评审得分历史表
							this.examineeReviewHis(classId, applyBatchId, bmbId, oprid, dqpyLunc,errMsg);

							//更新考生评审得分历史表
							sql = "UPDATE PS_TZ_KSCLPSLS_TBL SET TZ_SUBMIT_YN='Y'";
							sql = sql + " WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=? AND TZ_PWEI_OPRID=? AND TZ_CLPS_LUNC=?";
							sqlQuery.update(sql,new Object[]{classId,applyBatchId,bmbId,oprid,dqpyLunc});

							String messageCode = "0";
							String message = "";
							String bmbIdNext = "";

							if("SGN".equals(type)) {
								/*保存提交并获取下一个考生*/
								String getNext = this.getNextExaminee(classId,applyBatchId,oprid,errMsg);
								jacksonUtil.json2Map(getNext);
								bmbIdNext = jacksonUtil.getString("bmbIdNext");
								messageCode = jacksonUtil.getString("messageCode");
								message = jacksonUtil.getString("message");
							}

							//计算排名
							this.examineeRank(classId,applyBatchId,bmbId,oprid,orgId,dqpyLunc,errMsg);

							if(!"".equals(bmbIdNext)) {
								bmbId=bmbIdNext;
							}

							strRtn = this.getExamineeScoreInfo(classId, applyBatchId, bmbId,mshId, messageCode,message,errMsg);



						} else {
							result = "1";
							resultMsg = jacksonUtil.getString("resultMsg");
						}

						System.out.println("材料评审评委端打分页保存End");
					}
				}

				if("1".equals(result)) {
					mapRet.put("result", result);
					mapRet.put("resultMsg", resultMsg);
					strRtn = jacksonUtil.Map2json(mapRet);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}


		return strRtn;
	}

	/**
	 * 获取考生列表
	 */
	public String getExamineeList(String classId,String applyBatchId,String[] errMsg) {
		String strRtn="";
		JacksonUtil jacksonUtil = new JacksonUtil();

		Map<String, Object> mapRet = new HashMap<String,Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");

		try {
			//当前登录人
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

			ArrayList<Map<String, Object>> examineeJson = new ArrayList<Map<String,Object>>();
			Integer count = 0;

			String examineeSql = "SELECT A.TZ_APP_INS_ID,B.OPRID,C.TZ_REALNAME,C.TZ_MSSQH,A.TZ_KSH_PSPM,A.TZ_SCORE_INS_ID,D.TZ_JG_ID,D.TZ_ZLPS_SCOR_MD_ID";
			examineeSql = examineeSql + " FROM PS_TZ_REG_USER_T C,PS_TZ_FORM_WRK_T B,PS_TZ_CP_PW_KS_TBL A,PS_TZ_CLASS_INF_T D";
			examineeSql = examineeSql + " WHERE A.TZ_APP_INS_ID=B.TZ_APP_INS_ID AND B.OPRID = C.OPRID AND A.TZ_CLASS_ID=D.TZ_CLASS_ID AND A.TZ_CLASS_ID=? AND A.TZ_APPLY_PC_ID=? AND A.TZ_PWEI_OPRID=?";

			List<Map<String, Object>> examineeList = sqlQuery.queryForList(examineeSql,new Object[]{classId,applyBatchId,oprid});

			for(Map<String, Object> mapExaminee : examineeList) {
				count++;

				Map<String, Object> examineeListJson = new HashMap<String, Object>();

				String examineeBmbId = mapExaminee.get("TZ_APP_INS_ID") == null ? "" : String.valueOf(mapExaminee.get("TZ_APP_INS_ID"));
				String examineeName = mapExaminee.get("TZ_REALNAME") == null ? "" : String.valueOf(mapExaminee.get("TZ_REALNAME"));
				String examineeRank = mapExaminee.get("TZ_KSH_PSPM") == null ? "" : String.valueOf(mapExaminee.get("TZ_KSH_PSPM"));
				String examineeTotalScore;
				String examineeInterviewId = mapExaminee.get("TZ_MSSQH") == null ? "" : String.valueOf(mapExaminee.get("TZ_MSSQH"));
				String scoreInsId = mapExaminee.get("TZ_SCORE_INS_ID") == null ? "" : String.valueOf(mapExaminee.get("TZ_SCORE_INS_ID"));
				String jgId = mapExaminee.get("TZ_JG_ID") == null ? "" : String.valueOf(mapExaminee.get("TZ_JG_ID"));
				String scoreModelId = mapExaminee.get("TZ_ZLPS_SCOR_MD_ID") == null ? "" : String.valueOf(mapExaminee.get("TZ_ZLPS_SCOR_MD_ID"));

				//查询总分，第一个成绩项的分数
				String totalScoreSql = "SELECT B.TZ_SCORE_NUM FROM PS_TZ_CJ_BPH_TBL A,PS_TZ_CJX_TBL B";
				totalScoreSql = totalScoreSql + " WHERE A.TZ_SCORE_ITEM_ID=B.TZ_SCORE_ITEM_ID AND B.TZ_SCORE_INS_ID=? AND A.TZ_JG_ID=? AND A.TZ_SCORE_MODAL_ID=?";
				totalScoreSql = totalScoreSql + " ORDER BY A.TZ_PX";
				examineeTotalScore = sqlQuery.queryForObject(totalScoreSql, new Object[]{scoreInsId,jgId,scoreModelId},"String");

				examineeListJson.put("classId", classId);
				examineeListJson.put("applyBatchId", applyBatchId);
				examineeListJson.put("examineeBmbId", examineeBmbId);
				examineeListJson.put("examineeName", examineeName);
				examineeListJson.put("examineeRank", examineeRank);
				examineeListJson.put("examineeTotalScore", examineeTotalScore);
				examineeListJson.put("examineeInterviewId", examineeInterviewId);

				examineeJson.add(examineeListJson);
			}

			mapRet.replace("total", count);
			mapRet.replace("root", examineeJson);

		} catch(Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		strRtn = jacksonUtil.Map2json(mapRet);
		return strRtn;
	}


	/**
	 * 获取考生打分页信息
	 */
	public String getExamineeScoreInfo(String classId,String applyBatchId,String bmbId,String mshId,String messageCode,String message,String[] errMsg) {
		String strRtn = "";
		Map<String,Object> mapRet = new HashMap<String,Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {

			/*当前登录人*/
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

			/*自动初筛的成绩项ID*/
			String strBkxxId = getHardCodePoint.getHardCodePointVal("TZ_CLPS_ZDCS_BKXX_ID");
			String strZybjId = getHardCodePoint.getHardCodePointVal("TZ_CLPS_ZDCS_ZYBJ_ID");
			String strYyspId = getHardCodePoint.getHardCodePointVal("TZ_CLPS_ZDCS_XXHDJL_ID");
			String strXxhdjlId = getHardCodePoint.getHardCodePointVal("TZ_CLPS_ZDCS_YYSP_ID");


			/*当前考生在当前评委下的成绩单ID*/
			String sql = "SELECT TZ_SCORE_INS_ID FROM PS_TZ_CP_PW_KS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=? AND TZ_PWEI_OPRID=?";
			String scoreInsId = sqlQuery.queryForObject(sql, new Object[]{classId, applyBatchId, bmbId, oprid}, "String");


			/*当前考生基本信息*/
			/*String sqlBasic = "SELECT A.TZ_CLASS_NAME,YEAR(A.TZ_START_DT) TZ_START_YEAR,A.TZ_JG_ID,A.TZ_PS_APP_MODAL_ID,A.TZ_ZLPS_SCOR_MD_ID,A.TZ_APP_MODAL_ID,C.TZ_APP_FORM_STA,B.OPRID,E.TZ_REALNAME,E.TZ_MSSQH,F.TZ_BATCH_NAME";
			sqlBasic = sqlBasic + ",(SELECT G.TREE_NAME FROM PS_TZ_RS_MODAL_TBL G WHERE G.TZ_JG_ID=A.TZ_JG_ID AND G.TZ_SCORE_MODAL_ID=A.TZ_ZLPS_SCOR_MD_ID) TREE_NAME,G.TZ_PRJ_NAME";
			sqlBasic = sqlBasic + " FROM PS_TZ_REG_USER_T E,PS_TZ_APP_INS_T C,PS_TZ_FORM_WRK_T B,PS_TZ_CLS_BATCH_T F,PS_TZ_CLASS_INF_T A,PS_TZ_PRJ_INF_T G";
			sqlBasic = sqlBasic + " WHERE A.TZ_CLASS_ID=B.TZ_CLASS_ID AND B.TZ_APP_INS_ID = C.TZ_APP_INS_ID AND B.OPRID=E.OPRID AND A.TZ_PRJ_ID = G.TZ_PRJ_ID AND A.TZ_CLASS_ID=? AND A.TZ_CLASS_ID = F.TZ_CLASS_ID AND F.TZ_BATCH_ID=? AND B.TZ_APP_INS_ID=?";*/

			String sqlBasic="SELECT A.TZ_CLASS_NAME,YEAR (A.TZ_START_DT) TZ_START_YEAR,A.TZ_JG_ID,A.TZ_PS_APP_MODAL_ID,A.TZ_ZLPS_SCOR_MD_ID,A.TZ_APP_MODAL_ID,C.TZ_APP_FORM_STA,E.TZ_REALNAME, E.TZ_MSSQH, F.TZ_BATCH_NAME,"+
					"(SELECT G.TREE_NAME FROM PS_TZ_RS_MODAL_TBL G WHERE G.TZ_JG_ID = A.TZ_JG_ID AND G.TZ_SCORE_MODAL_ID = A.TZ_ZLPS_SCOR_MD_ID) TREE_NAME,G.TZ_PRJ_NAME FROM PS_TZ_REG_USER_T E,PS_TZ_APP_INS_T C, PS_TZ_CLS_BATCH_T F, PS_TZ_CLASS_INF_T A, PS_TZ_PRJ_INF_T G "+
					" WHERE E.TZ_MSSQH=? AND A.TZ_PRJ_ID = G.TZ_PRJ_ID AND A.TZ_CLASS_ID =? AND A.TZ_CLASS_ID = F.TZ_CLASS_ID AND F.TZ_BATCH_ID =? AND C.TZ_APP_INS_ID =?";

			Map<String, Object> mapRootBasic = sqlQuery.queryForMap(sqlBasic,new Object[] { mshId,classId, applyBatchId, bmbId});

			if(mapRootBasic == null) {
				mapRet.put("messageCode", "1");
				mapRet.put("message", "没有考生信息");
				strRtn = jacksonUtil.Map2json(mapRet);
				return strRtn;
			}

			String className = mapRootBasic.get("TZ_CLASS_NAME") == null ? "" : mapRootBasic.get("TZ_CLASS_NAME").toString();
			String classStartYear = mapRootBasic.get("TZ_START_YEAR") == null ? "" : mapRootBasic.get("TZ_START_YEAR").toString();
			String applyBatchName = mapRootBasic.get("TZ_BATCH_NAME") == null ? "" : mapRootBasic.get("TZ_BATCH_NAME").toString();
			String prgName = mapRootBasic.get("TZ_PRJ_NAME") == null ? "" : mapRootBasic.get("TZ_PRJ_NAME").toString();
			String name = mapRootBasic.get("TZ_REALNAME") == null ? "" : mapRootBasic.get("TZ_REALNAME").toString();
			String interviewApplyId = mapRootBasic.get("TZ_MSSQH") == null ? "" : mapRootBasic.get("TZ_MSSQH").toString();
			String jgId = mapRootBasic.get("TZ_JG_ID") == null ? "" : mapRootBasic.get("TZ_JG_ID").toString();
			String clpsBmbTplId = mapRootBasic.get("TZ_PS_APP_MODAL_ID") == null ? "" : mapRootBasic.get("TZ_PS_APP_MODAL_ID").toString();
			String scoreModelId = mapRootBasic.get("TZ_ZLPS_SCOR_MD_ID") == null ? "" : mapRootBasic.get("TZ_ZLPS_SCOR_MD_ID").toString();
			String scoreTree = mapRootBasic.get("TREE_NAME") == null ? "" : mapRootBasic.get("TREE_NAME").toString();

			mapRet.put("classId", classId);
			mapRet.put("className", className);
			mapRet.put("classStartYear", classStartYear);
			mapRet.put("applyBatchId", applyBatchId);
			mapRet.put("applyBatchName", applyBatchName);
			mapRet.put("prgName", prgName);
			mapRet.put("bmbId", bmbId);
			mapRet.put("name", name);
			mapRet.put("interviewApplyId", interviewApplyId);
			mapRet.put("clpsBmbTplId", clpsBmbTplId);



			//考生标签：显示自动初筛形成的标签&管理员后台手工添加的标签，负面清单标签不显示
			String examineeTag = "";

			String sqlBq = tzSQLObject.getSQLText("SQL.TZMaterialInterviewReviewBundle.material.TzGetMaterialKsBqInfo");
			List<Map<String, Object>> listBq = sqlQuery.queryForList(sqlBq, new Object[] {bmbId,bmbId});

			for (Map<String, Object> mapBq : listBq) {
				String bqId = mapBq.get("TZ_BQ_ID") == null ? "" : mapBq.get("TZ_BQ_ID").toString();
				String bqName= mapBq.get("TZ_BQ_NAME") == null ? "" : mapBq.get("TZ_BQ_NAME").toString();

				examineeTag = examineeTag + "【" + bqName + "】&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
			}
			mapRet.put("examineeTag", examineeTag);


			/*成绩项*/
			ArrayList<Map<String, Object>> scoreItemJson = new ArrayList<Map<String,Object>>();

			String sqlScore = "SELECT A.TREE_NODE,A.PARENT_NODE_NAME,B.DESCR,B.TZ_SCORE_ITEM_TYPE,(SELECT 'Y' FROM PSTREENODE C WHERE C.TREE_NAME=A.TREE_NAME AND C.TREE_NODE_NUM>A.TREE_NODE_NUM AND C.TREE_NODE_NUM_END<A.TREE_NODE_NUM_END  LIMIT 1) TZ_NO_LEAF,A.TREE_LEVEL_NUM,B.TZ_SCORE_LIMITED,B.TZ_SCORE_LIMITED2,B.TZ_SCORE_PY_ZSLIM,B.TZ_SCORE_PY_ZSLIM0,B.TZ_SCORE_ITEM_DFSM,B.TZ_SCORE_ITEM_CKWT,B.TZ_SCORE_CKZL";
			sqlScore = sqlScore + " FROM PSTREENODE A,PS_TZ_MODAL_DT_TBL B";
			sqlScore = sqlScore + " WHERE A.TREE_NAME=B.TREE_NAME AND A.TREE_NODE=B.TZ_SCORE_ITEM_ID AND A.TREE_NAME=?";
			sqlScore = sqlScore + " ORDER BY A.TREE_NODE_NUM,A.TREE_NODE_NUM_END DESC";

			List<Map<String,Object>> scoreList = sqlQuery.queryForList(sqlScore,new Object[]{scoreTree});

			if(scoreList!=null) {
				for (Map<String, Object>mapScore : scoreList) {

					Map<String, Object>mapScoreJson = new HashMap<String,Object>();

					String scoreItemId = mapScore.get("TREE_NODE") == null ? "" : mapScore.get("TREE_NODE").toString();
					String scoreItemParentId = mapScore.get("PARENT_NODE_NAME") == null ? "" : mapScore.get("PARENT_NODE_NAME").toString();
					String scoreItemName = mapScore.get("DESCR") == null ? "" : mapScore.get("DESCR").toString();
					String scoreItemType = mapScore.get("TZ_SCORE_ITEM_TYPE") == null ? "" : mapScore.get("TZ_SCORE_ITEM_TYPE").toString();
					String scoreItemIsLeaf = mapScore.get("TZ_NO_LEAF") == null ? "Y" : "N";
					String scoreItemLevel = mapScore.get("TREE_LEVEL_NUM") == null ? "" : mapScore.get("TREE_LEVEL_NUM").toString();
					String scoreItemValueUpper = mapScore.get("TZ_SCORE_LIMITED") == null ? "" : mapScore.get("TZ_SCORE_LIMITED").toString();
					String scoreItemValueLower = mapScore.get("TZ_SCORE_LIMITED2") == null ? "" : mapScore.get("TZ_SCORE_LIMITED2").toString();
					String scoreItemValue = "";
					String scoreItemCommentUpper = mapScore.get("TZ_SCORE_PY_ZSLIM") == null ? "" : mapScore.get("TZ_SCORE_PY_ZSLIM").toString();
					String scoreItemCommentLower = mapScore.get("TZ_SCORE_PY_ZSLIM0") == null ? "" : mapScore.get("TZ_SCORE_PY_ZSLIM0").toString();
					String scoreItemComment = "";
					String scoreItemXlkId = "";
					String scoreItemDfsm = mapScore.get("TZ_SCORE_ITEM_DFSM") == null ? "" : mapScore.get("TZ_SCORE_ITEM_DFSM").toString();//标准
					String scoreItemCkwt = mapScore.get("TZ_SCORE_ITEM_CKWT") == null ? "" : mapScore.get("TZ_SCORE_ITEM_CKWT").toString();//说明
					String scoreItemCkzl = mapScore.get("TZ_SCORE_CKZL") == null ? "" : mapScore.get("TZ_SCORE_CKZL").toString();//参考资料


					/*查询成绩项分值和评语值*/
					String sqlScoreValue = "SELECT TZ_CJX_XLK_XXBH,TZ_SCORE_NUM, TZ_SCORE_PY_VALUE FROM PS_TZ_CJX_TBL WHERE TZ_SCORE_INS_ID=? AND TZ_SCORE_ITEM_ID=?";
					Map<String, Object> mapScoreValue = sqlQuery.queryForMap(sqlScoreValue,new Object[] {scoreInsId,scoreItemId});
					if(mapScoreValue==null) {
						if(scoreItemId.equals(strBkxxId) || scoreItemId.equals(strZybjId) || scoreItemId.equals(strXxhdjlId) || scoreItemId.equals(strYyspId)) {
							//查询自动初筛的分数
							String sqlZdcx = "SELECT A.TZ_SCORE_NUM FROM PS_TZ_CJX_TBL A,PS_TZ_CS_KS_TBL B";
							sqlZdcx += " WHERE A.TZ_SCORE_INS_ID=B.TZ_SCORE_INS_ID AND A.TZ_SCORE_ITEM_ID=? AND B.TZ_CLASS_ID=? AND B.TZ_APPLY_PC_ID=? AND B.TZ_APP_INS_ID=?";
							scoreItemValue = sqlQuery.queryForObject(sqlZdcx, new Object[]{scoreItemId,classId,applyBatchId,bmbId},"String");
							//自动初筛分数小于最低打分，默认最低打分
							if("".equals(scoreItemValue) || scoreItemValue == null) {
								scoreItemValue = scoreItemValueLower;
							} else if(Double.valueOf(scoreItemValue)<Double.valueOf(scoreItemValueLower)) {
								scoreItemValue = scoreItemValueLower;
							}
						}

					} else {
						scoreItemXlkId = mapScoreValue.get("TZ_CJX_XLK_XXBH") == null ? "" : mapScoreValue.get("TZ_CJX_XLK_XXBH").toString();
						scoreItemValue = mapScoreValue.get("TZ_SCORE_NUM") == null ? "" : mapScoreValue.get("TZ_SCORE_NUM").toString();
						scoreItemComment = mapScoreValue.get("TZ_SCORE_PY_VALUE") == null ? "" : mapScoreValue.get("TZ_SCORE_PY_VALUE").toString();

						}
					/*如果成绩项类型为“D-下拉框”，则需要去下拉框值*/
					ArrayList<Map<String, Object>> optionListJson = new ArrayList<Map<String,Object>>();

					if("D".equals(scoreItemType)) {
						String optionSql = "SELECT TZ_CJX_XLK_XXBH,TZ_CJX_XLK_XXMC,TZ_CJX_XLK_XXFZ,TZ_CJX_XLK_MRZ";
						optionSql = optionSql + " FROM PS_TZ_ZJCJXXZX_T";
						optionSql = optionSql + " WHERE TZ_JG_ID=? AND TREE_NAME=? AND TZ_SCORE_ITEM_ID=?";

						List<Map<String, Object>> optionList = sqlQuery.queryForList(optionSql,new Object[]{jgId,scoreTree,scoreItemId});

						for(Map<String, Object> mapOption : optionList) {
							Map<String, Object> mapOptionJson = new HashMap<String,Object>();

							String scoreItemOptionId = mapOption.get("TZ_CJX_XLK_XXBH") == null ? "" : mapOption.get("TZ_CJX_XLK_XXBH").toString();
							String scoreItemOptionName = mapOption.get("TZ_CJX_XLK_XXMC") == null ? "" : mapOption.get("TZ_CJX_XLK_XXMC").toString();
							String scoreItemOptionValue = mapOption.get("TZ_CJX_XLK_XXFZ") == null ? "" : mapOption.get("TZ_CJX_XLK_XXFZ").toString();
							String scoreItemOptionDefault = mapOption.get("TZ_CJX_XLK_MRZ") == null ? "" : mapOption.get("TZ_CJX_XLK_MRZ").toString();

							mapOptionJson.put("itemId", scoreItemId);
							mapOptionJson.put("itemOptionId", scoreItemOptionId);
							mapOptionJson.put("itemOptionName", scoreItemOptionName);
							mapOptionJson.put("itemOptionValue", scoreItemOptionValue);
							mapOptionJson.put("itemOptionDefault", scoreItemOptionDefault);

							optionListJson.add(mapOptionJson);

							if(!"".equals(scoreItemXlkId)&&scoreItemXlkId!=null) {

							} else {
								if("Y".equals(scoreItemOptionDefault)) {
									scoreItemXlkId = scoreItemOptionId;
								}
							}
						}
					}

					mapScoreJson.put("itemId", scoreItemId);
					mapScoreJson.put("itemParentId", scoreItemParentId);
					mapScoreJson.put("itemName", scoreItemName);
					mapScoreJson.put("itemType", scoreItemType);
					mapScoreJson.put("itemIsLeaf", scoreItemIsLeaf);
					mapScoreJson.put("itemLevel", scoreItemLevel);
					mapScoreJson.put("itemUpperLimit", scoreItemValueUpper);
					mapScoreJson.put("itemLowerLimit", scoreItemValueLower);
					mapScoreJson.put("itemValue", scoreItemValue);
					mapScoreJson.put("itemCommentUpperLimit", scoreItemCommentUpper);
					mapScoreJson.put("itemCommentLowerLimit", scoreItemCommentLower);
					mapScoreJson.put("itemComment", scoreItemComment);
					mapScoreJson.put("itemXlkId", scoreItemXlkId);
					mapScoreJson.put("itemOptions", optionListJson);
					mapScoreJson.put("itemDfsm", scoreItemDfsm);
					mapScoreJson.put("itemCkwt", scoreItemCkwt);
					mapScoreJson.put("itemCkzl", scoreItemCkzl);
					mapScoreJson.put("scoreModelId", scoreModelId);

					scoreItemJson.add(mapScoreJson);
				}
			} else {
				mapRet.put("messageCode", "0");
				mapRet.put("message", "没有配置成绩模型");
			}
			mapRet.put("scoreContent", scoreItemJson);


			//左侧考生列表header
			Map<String, Object> mapHeader = new HashMap<String,Object>();
			mapHeader.put("col01", "总分");
			mapHeader.put("ps_msh_id", "面试申请号");
			mapHeader.put("ps_ksh_cpm", "排名");
			//mapHeader.put("ps_ksh_xh", "面试顺序");
			mapHeader.put("ps_ksh_xm", "姓名");

			mapRet.put("ksGridHeader", mapHeader);

			/*考生列表*/
			/*
			ArrayList<Map<String, Object>> examineeJson = new ArrayList<Map<String,Object>>();

			String examineeSql = "SELECT A.TZ_APP_INS_ID,B.OPRID,C.TZ_REALNAME,C.TZ_MSSQH,A.TZ_KSH_PSPM,A.TZ_SCORE_INS_ID";
			examineeSql = examineeSql + " FROM PS_TZ_REG_USER_T C,PS_TZ_FORM_WRK_T B,PS_TZ_CP_PW_KS_TBL A";
			examineeSql = examineeSql + " WHERE A.TZ_APP_INS_ID=B.TZ_APP_INS_ID AND B.OPRID = C.OPRID AND A.TZ_CLASS_ID=? AND A.TZ_APPLY_PC_ID=? AND A.TZ_PWEI_OPRID=?";

			List<Map<String, Object>> examineeList = sqlQuery.queryForList(examineeSql,new Object[]{classId,applyBatchId,oprid});

			for(Map<String, Object> mapExaminee : examineeList) {
				Map<String, Object> examineeListJson = new HashMap<String, Object>();

				String examineeBmbId = mapExaminee.get("TZ_APP_INS_ID") == null ? "" : mapExaminee.get("TZ_APP_INS_ID").toString();
				String examineeName = mapExaminee.get("TZ_REALNAME") == null ? "" : mapExaminee.get("TZ_REALNAME").toString();
				String examineeRank = mapExaminee.get("TZ_KSH_PSPM") == null ? "" : mapExaminee.get("TZ_KSH_PSPM").toString();
				String scoreInsIdM = mapExaminee.get("TZ_SCORE_INS_ID") == null ? "" : mapExaminee.get("TZ_SCORE_INS_ID").toString();
				String examineeTotalScore = "";
				String examineeInterviewId = mapExaminee.get("TZ_MSSQH") == null ? "" : mapExaminee.get("TZ_MSSQH").toString();

				//查询总分，第一个成绩项的分数
				String totalScoreSql = "SELECT B.TZ_SCORE_NUM FROM PS_TZ_CJ_BPH_TBL A,PS_TZ_CJX_TBL B";
				totalScoreSql = totalScoreSql + " WHERE A.TZ_SCORE_ITEM_ID=B.TZ_SCORE_ITEM_ID AND B.TZ_SCORE_INS_ID=? AND A.TZ_JG_ID=? AND A.TZ_SCORE_MODAL_ID=?";
				totalScoreSql = totalScoreSql + " ORDER BY A.TZ_PX";
				examineeTotalScore = sqlQuery.queryForObject(totalScoreSql, new Object[]{scoreInsIdM,jgId,scoreModelId},"String");

				examineeListJson.put("examineeBmbId", examineeBmbId);
				examineeListJson.put("examineeName", examineeName);
				examineeListJson.put("examineeRank", examineeRank);
				examineeListJson.put("examineeTotalScore", examineeTotalScore);
				examineeListJson.put("examineeInterviewId", examineeInterviewId);

				examineeJson.add(examineeListJson);
			}

			mapRet.put("examineeList", examineeJson);
			*/

			mapRet.put("messageCode", messageCode);
			mapRet.put("message", message);


		} catch(Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			mapRet.put("messageCode", "1");
			mapRet.put("message", e.toString());
		}

		strRtn = jacksonUtil.Map2json(mapRet);
		return strRtn;
	}


	/**
	 * 保存成绩项信息
	 * 先校验成绩和评语的有效性，有效后调用封装方法
	 */
	public String scoreItemSave(String classId,String applyBatchId,String bmbId,String formData,String[] errMsg) {
		String strRtn = "";
		Map<String, Object> mapRet = new HashMap<String,Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {

			jacksonUtil.json2Map(formData);

			String result = "";
			String resultMsg = "";

			Map<String,Object> mapItemsScore = new HashMap<>();
			String itemScoreParams = "";

			String sql;

			sql = "SELECT A.TZ_SCORE_ITEM_ID,A.DESCR,A.TZ_SCORE_ITEM_TYPE,A.TZ_SCORE_LIMITED,A.TZ_SCORE_LIMITED2,A.TZ_SCORE_PY_ZSLIM,A.TZ_SCORE_PY_ZSLIM0,A.TZ_M_FBDZ_MX_SX_JX,A.TZ_M_FBDZ_MX_XX_JX";
			sql = sql + " FROM PS_TZ_MODAL_DT_TBL A,PS_TZ_RS_MODAL_TBL B,PS_TZ_CLASS_INF_T C";
			sql = sql + " WHERE B.TZ_JG_ID=C.TZ_JG_ID AND B.TZ_SCORE_MODAL_ID=C.TZ_ZLPS_SCOR_MD_ID AND A.TREE_NAME=B.TREE_NAME AND C.TZ_CLASS_ID=?";

			List<Map<String, Object>> scoreList = sqlQuery.queryForList(sql,new Object[]{classId});
			for(Map<String, Object> mapScore : scoreList) {

				String scoreItemId = mapScore.get("TZ_SCORE_ITEM_ID") == null ? "" : mapScore.get("TZ_SCORE_ITEM_ID").toString();
				String scoreItemName = mapScore.get("DESCR") == null ? "" : mapScore.get("DESCR").toString();
				String scoreItemType = mapScore.get("TZ_SCORE_ITEM_TYPE") == null ? "" : mapScore.get("TZ_SCORE_ITEM_TYPE").toString();
				Double scoreItemValueUpper = mapScore.get("TZ_SCORE_LIMITED") == null ? 0.0 : Double.valueOf(mapScore.get("TZ_SCORE_LIMITED").toString());
				Double scoreItemValueLower = mapScore.get("TZ_SCORE_LIMITED2") == null ? 0.0 : Double.valueOf(mapScore.get("TZ_SCORE_LIMITED2").toString());
				Integer scoreItemCommentUpper = mapScore.get("TZ_SCORE_PY_ZSLIM") == null ? 0 : Integer.valueOf(mapScore.get("TZ_SCORE_PY_ZSLIM").toString());
				Integer scoreItemCommentLower = mapScore.get("TZ_SCORE_PY_ZSLIM0") == null ? 0 : Integer.valueOf(mapScore.get("TZ_SCORE_PY_ZSLIM0").toString());
				String scoreItemUpperOperate = mapScore.get("TZ_M_FBDZ_MX_SX_JX") == null ? "" : mapScore.get("TZ_M_FBDZ_MX_SX_JX").toString();
				String scoreItemLowerOperate = mapScore.get("TZ_M_FBDZ_MX_XX_JX") == null ? "" : mapScore.get("TZ_M_FBDZ_MX_XX_JX").toString();

				String scoreItemValue = jacksonUtil.getString(scoreItemId);
				String scoreValid1 = "";
				String scoreValid2 = "";
				Double scoreItemValueD;

				//判断成绩及评语有效性
				if("B".equals(scoreItemType)) {
					//数字成绩录入项
					scoreItemValueD = scoreItemValue == null ? 0.0 : Double.valueOf(scoreItemValue);
					if("<".equals(scoreItemUpperOperate)) {
						if(">".equals(scoreItemLowerOperate)) {
							if(scoreItemValueD>scoreItemValueLower && scoreItemValueD<scoreItemValueUpper) {
								scoreValid1="Y";
							}
						} else {
							if(">=".equals(scoreItemLowerOperate)) {
								if(scoreItemValueD>=scoreItemValueLower && scoreItemValueD<scoreItemValueUpper) {
									scoreValid1="Y";
								}
							}
						}
					} else {
						if("<=".equals(scoreItemUpperOperate)) {
							if(">".equals(scoreItemLowerOperate)) {
								if(scoreItemValueD>scoreItemValueLower && scoreItemValueD<=scoreItemValueUpper) {
									scoreValid1="Y";
								}
							} else {
								if(">=".equals(scoreItemLowerOperate)) {
									if(scoreItemValueD>=scoreItemValueLower && scoreItemValueD<=scoreItemValueUpper) {
										scoreValid1="Y";
									}
								}
							}
						}
					}

				} else {
					if("C".equals(scoreItemType)) {
						//评语
						//if(scoreItemValue.length()>scoreItemCommentLower && scoreItemValue.length()<scoreItemCommentUpper) {
							//scoreValid2="Y";
						//}
						if(scoreItemValue.length()<scoreItemCommentUpper) {
							scoreValid2="Y";
						}
					}
				}

				if("B".equals(scoreItemType)) {
					if("Y".equals(scoreValid1)) {
						//成绩校验成功
					} else {
						//成绩校验失败
						resultMsg = resultMsg + "【" + scoreItemName + "】分数填写错误，请重新填写！<br>";
					}
				}

				if("C".equals(scoreItemType)) {
					if("Y".equals(scoreValid2)) {
						//评语校验成功
					} else {
						//评语校验失败
						resultMsg = resultMsg + "【" + scoreItemName + "】评语超出指定字数，请重新填写！<br>";
					}
				}

				mapItemsScore.put(scoreItemId, scoreItemValue);
			}

			if(resultMsg!="" && resultMsg!=null) {
				result = "1";
				System.out.println("调用保存成绩项的方法前校验发生错误："+resultMsg);
			} else {
				//调用保存成绩项方法
				itemScoreParams = jacksonUtil.Map2json(mapItemsScore);
				String [] saveRet = tzScoreInsCalculationObject.PwdfSaveUpdate("CL", classId, applyBatchId, bmbId, itemScoreParams);
				result = saveRet[0];
				resultMsg = saveRet[1];
			}

			mapRet.put("result", result);
			mapRet.put("resultMsg", resultMsg);

		} catch(Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		strRtn = jacksonUtil.Map2json(mapRet);
		return strRtn;
	}

	/**
	 * 更新考生评审得分历史表，如果没有数据新增，否则不操作
	 */
	private String examineeReviewHis(String classId,String applyBatchId,String bmbId,String oprid,String dqpyLunc,String[] errMsg) {
		String strRtn = "";

		try {

			System.out.println("更新考生评审得分历史表Begin");

			PsTzKsclpslsTblKey psTzKsclpslsTblKey = new PsTzKsclpslsTblKey();
			psTzKsclpslsTblKey.setTzClassId(classId);
			psTzKsclpslsTblKey.setTzApplyPcId(applyBatchId);
			psTzKsclpslsTblKey.setTzAppInsId(Long.valueOf(bmbId));
			psTzKsclpslsTblKey.setTzPweiOprid(oprid);
			psTzKsclpslsTblKey.setTzClpsLunc(Short.valueOf(dqpyLunc));

			PsTzKsclpslsTbl psTzKsclpslsTbl = psTzKsclpslsTblMapper.selectByPrimaryKey(psTzKsclpslsTblKey);

			if(psTzKsclpslsTbl == null) {
				psTzKsclpslsTbl = new PsTzKsclpslsTbl();
				psTzKsclpslsTbl.setTzClassId(classId);
				psTzKsclpslsTbl.setTzApplyPcId(applyBatchId);
				psTzKsclpslsTbl.setTzAppInsId(Long.valueOf(bmbId));
				psTzKsclpslsTbl.setTzPweiOprid(oprid);
				psTzKsclpslsTbl.setTzClpsLunc(Short.valueOf(dqpyLunc));
				psTzKsclpslsTbl.setTzSubmitYn("N");
				psTzKsclpslsTbl.setTzIsPwFp("N");
				psTzKsclpslsTbl.setRowAddedDttm(new Date());
				psTzKsclpslsTbl.setRowAddedOprid(oprid);
				psTzKsclpslsTbl.setRowLastmantDttm(new Date());
				psTzKsclpslsTbl.setRowLastmantOprid(oprid);
				psTzKsclpslsTblMapper.insert(psTzKsclpslsTbl);
				System.out.println("新增-考生评审得分历史表");
			} else {
				psTzKsclpslsTbl.setTzIsPwFp("Y");
				psTzKsclpslsTbl.setRowLastmantDttm(new Date());
				psTzKsclpslsTbl.setRowLastmantOprid(oprid);
				psTzKsclpslsTblMapper.updateByPrimaryKeySelective(psTzKsclpslsTbl);

				String sql = "UPDATE PS_TZ_KSCLPSLS_TBL SET TZ_SUBMIT_YN='N' WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=? AND TZ_PWEI_OPRID=? AND TZ_CLPS_LUNC=? AND TZ_SUBMIT_YN NOT IN ('C','Y')";
				sqlQuery.update(sql,new Object[]{classId,applyBatchId,bmbId,oprid,dqpyLunc});

				System.out.println("更新-考生评审得分历史表");
			}

			System.out.println("更新考生评审得分历史表End");

		} catch(Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
 		}

		return strRtn;
	}

	/**
	 * 计算排名
	 */
	public String examineeRank(String classId,String applyBatchId,String currentBmbId,String oprid,String orgId,String dqpyLunc,String[] errMsg) {
		String strRtn = "";

		try {
			System.out.println("计算排名Begin");

			String sql = tzSQLObject.getSQLText("SQL.TZEvaluationSystemBundle.TzMaterialExamineeRank");
			List<Map<String, Object>> listData = sqlQuery.queryForList(sql,new Object[]{orgId,classId,applyBatchId,oprid});

			for (Map<String, Object> mapData : listData) {
				Long bmbId = 0L;
				String strBmbId = mapData.get("TZ_APP_INS_ID") == null ? "" : mapData.get("TZ_APP_INS_ID").toString();
				if(!"".equals(strBmbId)) {
					bmbId = Long.valueOf(strBmbId);
				}

				String strRank = mapData.get("RANK") == null ? "" : mapData.get("RANK").toString();
				if(strRank.indexOf(".")>0) {
					strRank = strRank.substring(0, strRank.indexOf("."));
				}

				PsTzCpPwKsTblKey psTzCpPwKsTblKey = new PsTzCpPwKsTblKey();
				psTzCpPwKsTblKey.setTzClassId(classId);
				psTzCpPwKsTblKey.setTzApplyPcId(applyBatchId);
				psTzCpPwKsTblKey.setTzAppInsId(bmbId);
				psTzCpPwKsTblKey.setTzPweiOprid(oprid);

				PsTzCpPwKsTbl psTzCpPwKsTbl = psTzCpPwKsTblMapper.selectByPrimaryKey(psTzCpPwKsTblKey);

				if(psTzCpPwKsTbl == null) {

				} else {
					psTzCpPwKsTbl.setTzGuanxLeix("A");
					psTzCpPwKsTbl.setTzKshPspm(strRank);
					psTzCpPwKsTbl.setRowLastmantDttm(new Date());
					psTzCpPwKsTbl.setRowLastmantOprid(oprid);
					psTzCpPwKsTblMapper.updateByPrimaryKeySelective(psTzCpPwKsTbl);
				}


				PsTzKsclpslsTblKey psTzKsclpslsTblKey = new PsTzKsclpslsTblKey();
				psTzKsclpslsTblKey.setTzClassId(classId);
				psTzKsclpslsTblKey.setTzApplyPcId(applyBatchId);
				psTzKsclpslsTblKey.setTzAppInsId(Long.valueOf(bmbId));
				psTzKsclpslsTblKey.setTzPweiOprid(oprid);
				psTzKsclpslsTblKey.setTzClpsLunc(Short.valueOf(dqpyLunc));

				PsTzKsclpslsTbl psTzKsclpslsTbl = psTzKsclpslsTblMapper.selectByPrimaryKey(psTzKsclpslsTblKey);

				if(psTzKsclpslsTbl == null) {
					psTzKsclpslsTbl = new PsTzKsclpslsTbl();
					psTzKsclpslsTbl.setTzClassId(classId);
					psTzKsclpslsTbl.setTzApplyPcId(applyBatchId);
					psTzKsclpslsTbl.setTzAppInsId(Long.valueOf(bmbId));
					psTzKsclpslsTbl.setTzPweiOprid(oprid);
					psTzKsclpslsTbl.setTzClpsLunc(Short.valueOf(dqpyLunc));
					psTzKsclpslsTbl.setTzKshPspm(strRank);
					psTzKsclpslsTbl.setRowAddedDttm(new Date());
					psTzKsclpslsTbl.setRowAddedOprid(oprid);
					psTzKsclpslsTbl.setRowLastmantDttm(new Date());
					psTzKsclpslsTbl.setRowLastmantOprid(oprid);
					psTzKsclpslsTblMapper.insert(psTzKsclpslsTbl);
				} else {
					psTzKsclpslsTbl.setTzKshPspm(strRank);
					if(currentBmbId.equals(bmbId)) {
						//当前打分考生更新评审时间
						psTzKsclpslsTbl.setRowLastmantDttm(new Date());
						psTzKsclpslsTbl.setRowLastmantOprid(oprid);
					}
					psTzKsclpslsTblMapper.updateByPrimaryKeySelective(psTzKsclpslsTbl);
				}
			}

			System.out.println("计算排名End");

		} catch(Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
 		}

		return strRtn;
	}

	/**
	 * 计算评委间偏差
	 */
	private String calculatePwDeviation(String classId,String applyBatchId,String bmbId,String dqpyLunc,String oprid,String[] errMsg) {
		String strRtn = "";

		try {

			System.out.println("计算评委间偏差Begin");

			/*当前机构*/
			String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);

			String sql;

			//清除临时表数据
			sql = "DELETE FROM PS_TZ_PWKSPC_TMP_T";
			sqlQuery.update(sql);


			sql = "SELECT A.TZ_PWEI_OPRID,A.TZ_SCORE_INS_ID,C.TZ_SCORE_NUM";
			sql = sql + " FROM PS_TZ_CJX_TBL C,PS_TZ_CP_PW_KS_TBL A,PS_TZ_KSCLPSLS_TBL B";
			sql = sql + " WHERE A.TZ_SCORE_INS_ID=C.TZ_SCORE_INS_ID";
			sql = sql + " AND C.TZ_SCORE_ITEM_ID=(SELECT D.TREE_NODE FROM PSTREENODE D,PS_TZ_RS_MODAL_TBL E,PS_TZ_CLASS_INF_T F WHERE D.TREE_NAME=E.TREE_NAME AND E.TZ_SCORE_MODAL_ID=F.TZ_ZLPS_SCOR_MD_ID AND E.TZ_JG_ID=? AND F.TZ_CLASS_ID=A.TZ_CLASS_ID AND D.PARENT_NODE_NUM=0) ";
			sql = sql + " AND B.TZ_CLASS_ID=A.TZ_CLASS_ID AND B.TZ_APPLY_PC_ID=A.TZ_APPLY_PC_ID AND B.TZ_APP_INS_ID=A.TZ_APP_INS_ID AND B.TZ_PWEI_OPRID=A.TZ_PWEI_OPRID";
			sql = sql + " AND B.TZ_CLPS_LUNC=? AND B.TZ_SUBMIT_YN<>'C' AND A.TZ_CLASS_ID=? AND A.TZ_APPLY_PC_ID=? AND A.TZ_APP_INS_ID=?";

			List<Map<String,Object>> scoreList = sqlQuery.queryForList(sql,new Object[]{orgId,dqpyLunc,classId,applyBatchId,bmbId});

			Double score = 0.0;
			Integer i = 0;

			for(Map<String, Object> mapScore : scoreList) {
				i++;
				score = mapScore.get("TZ_SCORE_NUM") == null ? 0.0 : Double.valueOf(mapScore.get("TZ_SCORE_NUM").toString());

				PsTzPwkspcTmpT psTzPwkspcTmpT = new PsTzPwkspcTmpT();
				psTzPwkspcTmpT.setTzId(i);
				psTzPwkspcTmpT.setTzScoreNum(BigDecimal.valueOf(score));
				psTzPwkspcTmpTMapper.insert(psTzPwkspcTmpT);
			}

			//计算偏差
			sql = "SELECT STDDEV(TZ_SCORE_NUM) FROM PS_TZ_PWKSPC_TMP_T";
			Double pcValue = Double.valueOf(sqlQuery.queryForObject(sql,"String"));


			PsTzClpsKshTblKey psTzClpsKshTblKey = new PsTzClpsKshTblKey();
			psTzClpsKshTblKey.setTzClassId(classId);
			psTzClpsKshTblKey.setTzApplyPcId(applyBatchId);
			psTzClpsKshTblKey.setTzAppInsId(Long.valueOf(bmbId));

			PsTzClpsKshTbl psTzClpsKshTbl = psTzClpsKshTblMapper.selectByPrimaryKey(psTzClpsKshTblKey);

			if(psTzClpsKshTbl==null) {
				psTzClpsKshTbl = new PsTzClpsKshTbl();
				psTzClpsKshTbl.setTzClassId(classId);
				psTzClpsKshTbl.setTzApplyPcId(applyBatchId);
				psTzClpsKshTbl.setTzAppInsId(Long.valueOf(bmbId));
				psTzClpsKshTbl.setTzClpsPwjPc(BigDecimal.valueOf(pcValue));
				psTzClpsKshTbl.setRowAddedDttm(new Date());
				psTzClpsKshTbl.setRowAddedOprid(oprid);
				psTzClpsKshTbl.setRowLastmantDttm(new Date());
				psTzClpsKshTbl.setRowLastmantOprid(oprid);
				psTzClpsKshTblMapper.insert(psTzClpsKshTbl);
			} else {
				psTzClpsKshTbl.setTzClpsPwjPc(BigDecimal.valueOf(pcValue));
				psTzClpsKshTbl.setRowLastmantDttm(new Date());
				psTzClpsKshTbl.setRowLastmantOprid(oprid);
				psTzClpsKshTblMapper.updateByPrimaryKeySelective(psTzClpsKshTbl);
			}

			System.out.println("计算评委间偏差End");


		} catch(Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRtn;
	}

	/**
	 * 计算考生平均分
	 */
	private String calculateAverage(String classId,String applyBatchId,String bmbId,String[] errMsg) {
		String strRtn = "";

		try {

			String sql;
			sql = "SELECT A.TZ_SCORE_INS_ID ,B.TZ_SCORE_NUM FROM PS_TZ_CP_PW_KS_TBL A LEFT JOIN PS_TZ_CJX_TBL B ON A.TZ_SCORE_INS_ID = B.TZ_SCORE_INS_ID";
			sql = sql + " WHERE B.TZ_SCORE_ITEM_ID = (SELECT C.TREE_NODE FROM PSTREENODE C,PS_TZ_RS_MODAL_TBL D,PS_TZ_CLASS_INF_T E ";
			sql = sql + " WHERE C.TREE_NAME=D.TREE_NAME AND D.TZ_SCORE_MODAL_ID=E.TZ_ZLPS_SCOR_MD_ID AND E.TZ_CLASS_ID=A.TZ_CLASS_ID AND C.PARENT_NODE_NUM=0)";
			sql = sql + " AND A.TZ_CLASS_ID=? AND A.TZ_APPLY_PC_ID=? AND A.TZ_APP_INS_ID=?";

			List<Map<String, Object>> scoreList = sqlQuery.queryForList(sql,new Object[]{classId,applyBatchId,bmbId});

			Integer count = 0;
			Double score;
			Double sum = 0.00;

			for(Map<String, Object> mapScore : scoreList) {
				count++;
				score = mapScore.get("TZ_SCORE_NUM") == null ? 0.0 : Double.valueOf(mapScore.get("TZ_SCORE_NUM").toString());

				sum = sum + score;
			}

			DecimalFormat df = new DecimalFormat("0.00");

			strRtn = df.format(sum/count).toString();


		} catch(Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRtn;
	}

	/**
	 * 获取下一个考生
	 * 从所有的材料评审考生中随机获取
	 */
	public String getNextExaminee(String classId,String applyBatchId,String oprid,String[] errMsg) {
		String strRtn = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String, Object> mapRet = new HashMap<String,Object>();

		String bmbIdNext = "";
		String messageCode = "";
		String message = "";

		try {
			System.out.println("获取下一个考生Begin");

			String sql;

			//是否存在没有评委组的评委
			sql = "SELECT 'Y' FROM PS_TZ_CLPS_PW_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_PWEI_ZHZT='A' AND TZ_PWZBH=' '";
			String noPwzFlag = sqlQuery.queryForObject(sql, new Object[]{classId,applyBatchId},"String");

			if("Y".equals(noPwzFlag)) {
				bmbIdNext = "";
				messageCode = "1";
				message = "存在没有被分组的评委。";
			} else {
				//同一个应用服务内只允许10次评委抽取考生排队，否则报系统忙，请稍候再试。
				if(nextLockCounter.tryAcquire(500,TimeUnit.MILLISECONDS) == false)
				{
					System.out.println("评委"+ oprid + "线程启动，超过最大次数，不能抽取");
					bmbIdNext = "";
					messageCode = "1";
					message = "系统忙，请稍候再试。";
				} else {

				//线程间同步，防止同一个应用服务内的抽取竞争，减少数据库服务器加锁的压力
				nextLock.acquire();
				System.out.println("评委"+ oprid + "线程启动，进行抽取");

				try {
					sql = "SELECT A.TZ_PYKS_XX,A.TZ_PWEI_ZHZT,A.TZ_PWZBH,B.TZ_DQPY_ZT,B.TZ_DQPY_LUNC,B.TZ_MSPY_NUM";
					sql = sql + ",(SELECT C.TZ_SUBMIT_YN FROM PS_TZ_CLPWPSLS_TBL C";
					sql = sql + " WHERE C.TZ_CLASS_ID=A.TZ_CLASS_ID AND C.TZ_APPLY_PC_ID=A.TZ_APPLY_PC_ID AND C.TZ_PWEI_OPRID=A.TZ_PWEI_OPRID AND C.TZ_CLPS_LUNC=B.TZ_DQPY_LUNC) TZ_SUBMIT_YN";
					sql = sql + ",(SELECT COUNT(1) FROM PS_TZ_CP_PW_KS_TBL E,PS_TZ_CLPS_KSH_TBL F";
					sql = sql + " WHERE E.TZ_CLASS_ID=F.TZ_CLASS_ID AND E.TZ_APPLY_PC_ID=F.TZ_APPLY_PC_ID AND E.TZ_APP_INS_ID=F.TZ_APP_INS_ID AND E.TZ_CLASS_ID=A.TZ_CLASS_ID AND E.TZ_APPLY_PC_ID=A.TZ_APPLY_PC_ID AND E.TZ_PWEI_OPRID=A.TZ_PWEI_OPRID) TZ_PWKS_NUM";
					sql = sql + ",(SELECT COUNT(distinct G.TZ_APP_INS_ID ) FROM PS_TZ_CP_PW_KS_TBL E,PS_TZ_KSCLPSLS_TBL G";
					sql = sql + " WHERE E.TZ_CLASS_ID=G.TZ_CLASS_ID AND E.TZ_APPLY_PC_ID=G.TZ_APPLY_PC_ID AND E.TZ_APP_INS_ID=G.TZ_APP_INS_ID AND E.TZ_CLASS_ID=A.TZ_CLASS_ID AND E.TZ_APPLY_PC_ID=A.TZ_APPLY_PC_ID AND G.TZ_PWEI_OPRID=A.TZ_PWEI_OPRID AND G.TZ_CLPS_LUNC=B.TZ_DQPY_LUNC AND G.TZ_SUBMIT_YN='Y') TZ_PWKS_SUB_NUM";
					sql = sql + " FROM PS_TZ_CLPS_PW_TBL A ,PS_TZ_CLPS_GZ_TBL B";
					sql = sql + " WHERE A.TZ_CLASS_ID=B.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID=B.TZ_APPLY_PC_ID";
					sql = sql + " AND A.TZ_CLASS_ID=? AND A.TZ_APPLY_PC_ID=? AND A.TZ_PWEI_OPRID=?";

					Map<String, Object> mapData = sqlQuery.queryForMap(sql,new Object[]{classId,applyBatchId,oprid});

					if(mapData!=null) {

						Integer pyksNum = mapData.get("TZ_PYKS_XX") == null ? 0 : Integer.valueOf(mapData.get("TZ_PYKS_XX").toString());
						String pweiZhzt = mapData.get("TZ_PWEI_ZHZT") == null ? "" : mapData.get("TZ_PWEI_ZHZT").toString();
						String pwzbh = mapData.get("TZ_PWZBH") == null ? "" : mapData.get("TZ_PWZBH").toString();
						String dqpyZt = mapData.get("TZ_DQPY_ZT") == null ? "" : mapData.get("TZ_DQPY_ZT").toString();
						Short dqpyLunc = mapData.get("TZ_DQPY_LUNC") == null ? 0 : Short.valueOf(mapData.get("TZ_DQPY_LUNC").toString());
						Integer mspyNum = mapData.get("TZ_MSPY_NUM") == null ? 0 : Integer.valueOf(mapData.get("TZ_MSPY_NUM").toString());
						String submitAllFlag = mapData.get("TZ_SUBMIT_YN") == null ? "" : mapData.get("TZ_SUBMIT_YN").toString();
						Integer pwksNum = mapData.get("TZ_PWKS_NUM") == null ? 0 : Integer.valueOf(mapData.get("TZ_PWKS_NUM").toString());
						Integer pwksSubNum= mapData.get("TZ_PWKS_SUB_NUM") == null ? 0 : Integer.valueOf(mapData.get("TZ_PWKS_SUB_NUM").toString());

						if("Y".equals(submitAllFlag)) {
							bmbIdNext = "";
							messageCode = "1";
							message = "该轮次已经全部提交了,无法得到下一个考生。";
						} else {
							if("A".equals(dqpyZt)) {
								if("A".equals(pweiZhzt)) {
									if(pwksNum>pwksSubNum) {
										bmbIdNext = "";
										messageCode = "1";
										message = "存在未打分的考生。请先为考生打分，然后再获取新的考生。";
									} else {
										if(pwksNum>=pyksNum) {
											bmbIdNext = "";
											messageCode = "1";
											message = "该评委已经达到了评审的上限。";
										} else {
											sql = tzSQLObject.getSQLText("SQL.TZEvaluationSystemBundle.TzMaterialGetNext");
											Map<String, Object> mapNext = sqlQuery.queryForMap(sql,new Object[]{mspyNum,pwzbh,classId,applyBatchId});

											if(mapNext!=null){
												Long tzAppInsId = Long.valueOf(mapNext.get("TZ_APP_INS_ID") == null ? "" : mapNext.get("TZ_APP_INS_ID").toString());

												if(tzAppInsId>0) {

													String lockFlag = "";

													try {

														TzRecord lockRecord = tzSQLObject.createRecord("PS_TZ_CLPS_LOCK_T");
														lockRecord.setColumnValue("TZ_CLASS_ID", classId);
														lockRecord.setColumnValue("TZ_APPLY_PC_ID", applyBatchId);
														lockRecord.setColumnValue("TZ_APP_INS_ID", tzAppInsId);
														lockRecord.setColumnValue("TZ_PWZBH", pwzbh);
														lockRecord.setColumnValue("TZ_PWEI_OPRID", oprid);

														if(lockRecord.insert()==false) {
															bmbIdNext = "";
															messageCode = "1";
															message = "获取下一个考生失败，请重新抽取。";
														} else {

															lockFlag="Y";

															//再次查看有没有超过上限
															sql = "SELECT COUNT(1) FROM PS_TZ_CP_PW_KS_TBL A,PS_TZ_CLPS_KSH_TBL B WHERE A.TZ_CLASS_ID=B.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID=B.TZ_APPLY_PC_ID AND A.TZ_APP_INS_ID=B.TZ_APP_INS_ID AND A.TZ_CLASS_ID=? AND A.TZ_APPLY_PC_ID=? AND A.TZ_APP_INS_ID=?";
															Integer kspwNum = sqlQuery.queryForObject(sql, new Object[]{classId,applyBatchId,tzAppInsId},"Integer");

															//再次查看有没有被自己抽取过
															sql = "SELECT 'Y' FROM PS_TZ_CP_PW_KS_TBL A,PS_TZ_CLPS_KSH_TBL B WHERE A.TZ_CLASS_ID=B.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID=B.TZ_APPLY_PC_ID AND A.TZ_APP_INS_ID=B.TZ_APP_INS_ID AND A.TZ_CLASS_ID=? AND A.TZ_APPLY_PC_ID=? AND A.TZ_APP_INS_ID=? AND A.TZ_PWEI_OPRID=?";
															String myKsFlag = sqlQuery.queryForObject(sql, new Object[]{classId,applyBatchId,tzAppInsId,oprid}, "String");

															//再次查看有没有被同组人抽取过
															sql = "SELECT 'Y' FROM PS_TZ_CP_PW_KS_TBL A,PS_TZ_CLPS_KSH_TBL B,PS_TZ_CLPS_PW_TBL C WHERE A.TZ_CLASS_ID=B.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID=B.TZ_APPLY_PC_ID AND A.TZ_APP_INS_ID=B.TZ_APP_INS_ID AND A.TZ_CLASS_ID=C.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID=C.TZ_APPLY_PC_ID AND A.TZ_APP_INS_ID=C.TZ_PWEI_OPRID";
															sql = sql + " AND A.TZ_CLASS_ID=? AND A.TZ_APPLY_PC_ID=? AND A.TZ_APP_INS_ID=? AND C.TZ_PWZBH=?";
															String tzpwFlag = sqlQuery.queryForObject(sql, new Object[]{classId,applyBatchId,tzAppInsId,pwzbh}, "String");

															if(kspwNum>=mspyNum) {
																bmbIdNext = "";
																messageCode = "1";
																message = "该评委已经达到了评审的上限。";
															} else if("Y".equals(myKsFlag)) {
																bmbIdNext = "";
																messageCode = "1";
																message = "该考生已经被抽取。";
															} else if("Y".equals(tzpwFlag)) {
																bmbIdNext = "";
																messageCode = "1";
																message = "该考生已经被同组的其他评委抽取。";
															} else {
																//材料评审评委考生关系表
																int insertRow;

																PsTzCpPwKsTbl psTzCpPwKsTbl = new PsTzCpPwKsTbl();
																psTzCpPwKsTbl.setTzClassId(classId);
																psTzCpPwKsTbl.setTzApplyPcId(applyBatchId);
																psTzCpPwKsTbl.setTzAppInsId(tzAppInsId);
																psTzCpPwKsTbl.setTzPweiOprid(oprid);
																psTzCpPwKsTbl.setTzGuanxLeix("A");
																psTzCpPwKsTbl.setRowAddedDttm(new Date());
																psTzCpPwKsTbl.setRowAddedOprid(oprid);
																psTzCpPwKsTbl.setRowLastmantDttm(new Date());
																psTzCpPwKsTbl.setRowLastmantOprid(oprid);
																insertRow = psTzCpPwKsTblMapper.insert(psTzCpPwKsTbl);

																if("1".equals(String.valueOf(insertRow))) {
																	//材料评审考生评审得分历史
																	PsTzKsclpslsTblKey psTzKsclpslsTblKey = new PsTzKsclpslsTblKey();
																	psTzKsclpslsTblKey.setTzClassId(classId);
																	psTzKsclpslsTblKey.setTzApplyPcId(applyBatchId);
																	psTzKsclpslsTblKey.setTzAppInsId(tzAppInsId);
																	psTzKsclpslsTblKey.setTzPweiOprid(oprid);
																	psTzKsclpslsTblKey.setTzClpsLunc(dqpyLunc);

																	PsTzKsclpslsTbl psTzKsclpslsTbl = psTzKsclpslsTblMapper.selectByPrimaryKey(psTzKsclpslsTblKey);

																	if(psTzKsclpslsTbl==null) {
																		psTzKsclpslsTbl = new PsTzKsclpslsTbl();
																		psTzKsclpslsTbl.setTzClassId(classId);
																		psTzKsclpslsTbl.setTzApplyPcId(applyBatchId);
																		psTzKsclpslsTbl.setTzAppInsId(tzAppInsId);
																		psTzKsclpslsTbl.setTzPweiOprid(oprid);
																		psTzKsclpslsTbl.setTzClpsLunc(dqpyLunc);
																		psTzKsclpslsTbl.setTzGuanxLeix("A");
																		psTzKsclpslsTbl.setTzSubmitYn("U");
																		psTzKsclpslsTbl.setRowAddedDttm(new Date());
																		psTzKsclpslsTbl.setRowAddedOprid(oprid);
																		psTzKsclpslsTbl.setRowLastmantDttm(new Date());
																		psTzKsclpslsTbl.setRowLastmantOprid(oprid);
																		psTzKsclpslsTblMapper.insert(psTzKsclpslsTbl);
																	} else {
																		psTzKsclpslsTbl.setTzKshPspm("");
																		psTzKsclpslsTbl.setTzSubmitYn("U");
																		psTzKsclpslsTbl.setRowLastmantDttm(new Date());
																		psTzKsclpslsTbl.setRowLastmantOprid(oprid);
																		psTzKsclpslsTblMapper.updateByPrimaryKey(psTzKsclpslsTbl);
																	}

																	//材料评审考生评委信息

																	sql = "SELECT TZ_CLASS_ID,TZ_APPLY_PC_ID,TZ_APP_INS_ID,group_concat(TZ_PWEI_OPRID SEPARATOR',') AS TZ_PW_LIST FROM PS_TZ_CP_PW_KS_TBL";
																	sql = sql + " WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=? GROUP BY TZ_CLASS_ID,TZ_APPLY_PC_ID,TZ_APP_INS_ID";

																	Map<String, Object> mapKsPwList = sqlQuery.queryForMap(sql,new Object[]{classId,applyBatchId,tzAppInsId});
																	String ksPwList = mapKsPwList.get("TZ_PW_LIST") == null ? "" : mapKsPwList.get("TZ_PW_LIST").toString();


																	PsTzClpskspwTblKey psTzClpskspwTblKey = new PsTzClpskspwTblKey();
																	psTzClpskspwTblKey.setTzClassId(classId);
																	psTzClpskspwTblKey.setTzApplyPcId(applyBatchId);
																	psTzClpskspwTblKey.setTzAppInsId(tzAppInsId);
																	psTzClpskspwTblKey.setTzClpsLunc(dqpyLunc);

																	PsTzClpskspwTbl psTzClpskspwTbl = psTzClpskspwTblMapper.selectByPrimaryKey(psTzClpskspwTblKey);

																	if(psTzClpskspwTbl==null) {
																		psTzClpskspwTbl = new PsTzClpskspwTbl();
																		psTzClpskspwTbl.setTzClassId(classId);
																		psTzClpskspwTbl.setTzApplyPcId(applyBatchId);
																		psTzClpskspwTbl.setTzAppInsId(tzAppInsId);
																		psTzClpskspwTbl.setTzClpsLunc(dqpyLunc);
																		psTzClpskspwTbl.setTzClpwList(ksPwList);
																		psTzClpskspwTbl.setRowAddedDttm(new Date());
																		psTzClpskspwTbl.setRowAddedOprid(oprid);
																		psTzClpskspwTbl.setRowLastmantDttm(new Date());
																		psTzClpskspwTbl.setRowLastmantOprid(oprid);
																		psTzClpskspwTblMapper.insert(psTzClpskspwTbl);
																	} else {
																		psTzClpskspwTbl.setTzClpwList(ksPwList);
																		psTzClpskspwTbl.setRowLastmantDttm(new Date());
																		psTzClpskspwTbl.setRowLastmantOprid(oprid);
																		psTzClpskspwTblMapper.updateByPrimaryKeySelective(psTzClpskspwTbl);
																	}

																	bmbIdNext = String.valueOf(tzAppInsId);
																	messageCode = "0";
																	message = "";

																} else {
																	bmbIdNext = "";
																	messageCode = "1";
																	message = "同步MBA材料评审评委考生关系表失败。";
																}
															}
														}

													} catch(Exception e) {
														bmbIdNext = "";
														messageCode = "1";
														message = "获取下一个考生失败，请重新抽取。";
													} finally {
														//如果锁表成功，则删除数据，没成功，则有可能是其他人正在执行，不要删除别人的数据
														if("Y".equals(lockFlag)) {
															sqlQuery.update("DELETE FROM PS_TZ_CLPS_LOCK_T WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=? AND TZ_PWZBH=?",new Object[]{classId,applyBatchId,tzAppInsId,pwzbh});
														}
													}

												} else {
													bmbIdNext = "";
													messageCode = "1";
													message = "获取下一个考生失败或则已经没有可评审的考生。";
												}
											} else {
												bmbIdNext = "";
												messageCode = "1";
												message = "没有可获取的考生。";
											}
										}
									}
								} else {
									bmbIdNext = "";
									messageCode = "1";
									message = "评委账号没有被启用。";
								}
							} else {
								bmbIdNext = "";
								messageCode = "1";
								message = "当前批次的评审状态不在进行中。";
							}
						}
					}

				} catch(Exception e) {
					bmbIdNext = "";
					messageCode = "1";
					message = e.toString();
				} finally {
					System.out.println("评委"+ oprid + "线程结束，抽取考生报名表编号："+bmbIdNext);
					nextLock.release();
					nextLockCounter.release();
					System.out.println("信号量当前可用许可数："+nextLock.availablePermits());
				}
			}
			}

			mapRet.put("bmbIdNext", bmbIdNext);
			mapRet.put("messageCode", messageCode);
			mapRet.put("message", message);

			System.out.println("获取下一个考生返回数据。下一个考生的报名表实例编号："+bmbIdNext+"-->messageCode："+messageCode+"-->message："+message);

			System.out.println("获取下一个考生End");

		} catch(Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			mapRet.put("messageCode", "1");
			mapRet.put("message", e.toString());
		}

		strRtn = jacksonUtil.Map2json(mapRet);
		return strRtn;
	}
}
