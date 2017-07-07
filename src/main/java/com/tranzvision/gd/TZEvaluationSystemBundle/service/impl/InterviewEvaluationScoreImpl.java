package com.tranzvision.gd.TZEvaluationSystemBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZMbaPwMspsBundle.dao.PsTzMpPwKsTblMapper;
import com.tranzvision.gd.TZMbaPwMspsBundle.dao.PsTzMspskspwTblMapper;
import com.tranzvision.gd.TZMbaPwMspsBundle.model.PsTzMpPwKsTbl;
import com.tranzvision.gd.TZMbaPwMspsBundle.model.PsTzMpPwKsTblKey;
import com.tranzvision.gd.TZMbaPwMspsBundle.model.PsTzMspskspwTbl;
import com.tranzvision.gd.TZMbaPwMspsBundle.model.PsTzMspskspwTblKey;
import com.tranzvision.gd.TZScoreModeManagementBundle.service.impl.TzScoreInsCalculationObject;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * MBA材料面试评审-评委系统-面试评审-打分页
 * 
 * @author LUYAN
 * @since 2017-02-20
 */

@Service("com.tranzvision.gd.TZEvaluationSystemBundle.service.impl.InterviewEvaluationScoreImpl")

public class InterviewEvaluationScoreImpl extends FrameworkImpl{

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
	private PsTzMspskspwTblMapper psTzMspskspwTblMapper;
	@Autowired
	private GetHardCodePoint getHardCodePoint;
	@Autowired
	private TzScoreInsCalculationObject tzScoreInsCalculationObject;
	
	
	@Override
	public String tzQuery(String strParams,String[] errMsg) {
		String strRtn = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			jacksonUtil.json2Map(strParams);
			
			String classId = jacksonUtil.getString("classId");
			String applyBatchId = jacksonUtil.getString("applyBatchId");
			String bmbId = jacksonUtil.getString("bmbId");
			String messageCode = "0";
			String message = "";
			
			if(classId != null && applyBatchId != null && bmbId != null) {
				strRtn = this.getExamineeScoreInfo(classId, applyBatchId, bmbId, messageCode,message,errMsg);						
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
				
				String classId = jacksonUtil.getString("ClassID");
				String applyBatchId = jacksonUtil.getString("BatchID");
				String bmbId = jacksonUtil.getString("KSH_BMBID");
				String type = jacksonUtil.getString("OperationType");
				
				//当前登录人
				String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
				//当前机构
				String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
				
				String messageCode = "0";
				String message = "";
				
				String sql;
				
			    sql = "SELECT A.TZ_SUBMIT_YN FROM PS_TZ_MSPWPSJL_TBL A WHERE A.TZ_CLASS_ID=? AND A.TZ_APPLY_PC_ID=? AND A.TZ_PWEI_OPRID=?";
			    String submitAllFlag = sqlQuery.queryForObject(sql, new Object[]{classId,applyBatchId,oprid},"String");
			    
			    sql = "SELECT B.TZ_PWEI_ZHZT FROM PS_TZ_MSPS_PW_TBL B WHERE B.TZ_CLASS_ID=? AND B.TZ_APPLY_PC_ID=? AND B.TZ_PWEI_OPRID=?";
			    String pweiZhzt = sqlQuery.queryForObject(sql, new Object[]{classId,applyBatchId,oprid},"String");
			    
			    sql = "SELECT C.TZ_DQPY_ZT FROM PS_TZ_MSPS_GZ_TBL C WHERE C.TZ_CLASS_ID=? AND C.TZ_APPLY_PC_ID=?";
			    String dqpyZt = sqlQuery.queryForObject(sql, new Object[]{classId,applyBatchId},"String");
			    
				
				if("Y".equals(submitAllFlag)) {
					messageCode = "1";
					message = "评议数据已经提交，不允许对考生数据进行修改";
				} else {
					if("B".equals(pweiZhzt)) {
						messageCode = "1";
						message = "您的账号暂时无法评议该批次，请与管理员联系";
					} else {
						if("B".equals(dqpyZt)) {
							messageCode = "1";
							message = "该批次的评审已关闭";
						} 
					}
				}
				
				sql = "SELECT A.TZ_MSCJ_SCOR_MD_ID ,B.TREE_NAME,C.OPRID FROM PS_TZ_FORM_WRK_T C,PS_TZ_RS_MODAL_TBL B,PS_TZ_CLASS_INF_T A";
				sql = sql + " WHERE A.TZ_MSCJ_SCOR_MD_ID = B.TZ_SCORE_MODAL_ID AND A.TZ_CLASS_ID=C.TZ_CLASS_ID AND B.TZ_JG_ID=? AND C.TZ_APP_INS_ID=? AND A.TZ_CLASS_ID=?";
				 
				Map<String, Object> mapData = sqlQuery.queryForMap(sql, new Object[] {orgId,bmbId,classId});
				String scoreModalId = mapData.get("TZ_MSCJ_SCOR_MD_ID") == null ? "" : mapData.get("TZ_MSCJ_SCOR_MD_ID").toString();
				String treeName = mapData.get("TREE_NAME") == null ? "" : mapData.get("TREE_NAME").toString();
				String bmrOprid = mapData.get("OPRID") == null ? "" : mapData.get("OPRID").toString();
				
				if("".equals(scoreModalId)) {
					messageCode = "1";
					message = "当前报考方向没有配置成绩模型";
				} else {
					if("".equals(treeName)) {
						messageCode = "1";
						message = "报考方向对应的成绩模型没有配置对应的成绩树";
					} else {
						if("".equals(bmrOprid)) {
							messageCode = "1";
							message = "不存在该考生";
						}
					}
				}

				if("1".equals(messageCode)) {
					
				} else {
					//保存成绩项
					String saveScoreItemRtn = this.scoreItemSave(classId,applyBatchId,bmbId,strForm,errMsg);
					jacksonUtil.json2Map(saveScoreItemRtn);
					messageCode = jacksonUtil.getString("result");
					message = jacksonUtil.getString("resultMsg");
					
					if("1".equals(messageCode)) {
						
					} else {
						
						//计算排名
						this.updateExamineeRank(classId, applyBatchId, oprid, bmbId);
						
						//更新考生评审得分历史表
						sql = "UPDATE PS_TZ_MP_PW_KS_TBL SET TZ_PSHEN_ZT='Y'";
						sql = sql + " WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=? AND TZ_PWEI_OPRID=?";
						sqlQuery.update(sql,new Object[]{classId,applyBatchId,bmbId,oprid});
					}
				}

				strRtn = this.getExamineeScoreInfo(classId, applyBatchId, bmbId, messageCode,message,errMsg);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRtn;
	}
	
	
	public String tzOther(String operateType,String strParams,String[] errMsg) {
		String strRet = "";
		
		try {
			if("tzSearchExaminee".equals(operateType)) {
				strRet = searchExaminee(strParams);
			}
			if("tzAddExaminee".equals(operateType)) {
				strRet = addExamineeForJudge(strParams);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return strRet;
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
			
			String examineeSql = "SELECT A.TZ_APP_INS_ID,B.OPRID,C.TZ_REALNAME,C.TZ_MSSQH,A.TZ_KSH_PSPM,A.TZ_SCORE_INS_ID,D.TZ_JG_ID,D.TZ_MSPS_SCOR_MD_ID";
			examineeSql = examineeSql + " FROM PS_TZ_REG_USER_T C,PS_TZ_FORM_WRK_T B,PS_TZ_MP_PW_KS_TBL A,PS_TZ_CLASS_INF_T D";
			examineeSql = examineeSql + " WHERE A.TZ_APP_INS_ID=B.TZ_APP_INS_ID AND B.OPRID = C.OPRID AND A.TZ_CLASS_ID=D.TZ_CLASS_ID AND A.TZ_CLASS_ID=? AND A.TZ_APPLY_PC_ID=? AND A.TZ_PWEI_OPRID=? AND A.TZ_DELETE_ZT='N'"; 
			examineeSql = examineeSql + " ORDER BY A.TZ_KSH_PSPM ASC";
			
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
				String scoreModelId = mapExaminee.get("TZ_MSPS_SCOR_MD_ID") == null ? "" : String.valueOf(mapExaminee.get("TZ_MSPS_SCOR_MD_ID"));
				
				//面试顺序号
				String examineeInterviewSeq = "";
				Integer seq = 0;
				
				String sql = "SELECT TZ_APP_INS_ID FROM PS_TZ_MP_PW_KS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_PWEI_OPRID=? AND TZ_DELETE_ZT='N' ORDER BY ROW_ADDED_DTTM ASC";
				List<String> listBmbId = new ArrayList<>();
				
				for(String bmbIdTmp : listBmbId) {
					seq++;
					if(bmbIdTmp.equals(examineeBmbId)) {
						examineeInterviewSeq = String.valueOf(seq);
						break;
					}
				}
				
				//查询总分，第一个成绩项的分数
				String totalScoreSql = "SELECT B.TZ_SCORE_NUM FROM PS_TZ_CJ_BPH_TBL A,PS_TZ_CJX_TBL B";
				totalScoreSql = totalScoreSql + " WHERE A.TZ_SCORE_ITEM_ID=B.TZ_SCORE_ITEM_ID AND B.TZ_SCORE_INS_ID=? AND A.TZ_JG_ID=? AND A.TZ_SCORE_MODAL_ID=?";
				totalScoreSql = totalScoreSql + " ORDER BY A.TZ_PX";
				examineeTotalScore = sqlQuery.queryForObject(totalScoreSql, new Object[]{scoreInsId,jgId,scoreModelId},"String");
				
				examineeListJson.put("classId", classId);
				examineeListJson.put("applyBatchId", applyBatchId);
				examineeListJson.put("examineeBmbId", examineeBmbId);
				examineeListJson.put("examineeInterviewId", examineeInterviewId);
				examineeListJson.put("examineeName", examineeName);
				examineeListJson.put("examineeInterviewSeq", examineeInterviewSeq);
				examineeListJson.put("examineeRank", examineeRank);
				examineeListJson.put("examineeTotalScore", examineeTotalScore);
				
				
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
	public String getExamineeScoreInfo(String classId,String applyBatchId,String bmbId,String messageCode,String message,String[] errMsg) {		
		String strRtn = "";
		Map<String,Object> mapRet = new HashMap<String,Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			
			/*当前登录人*/
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

			/*当前考生在当前评委下的成绩单ID*/
			String sql = "SELECT TZ_SCORE_INS_ID FROM PS_TZ_MP_PW_KS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=? AND TZ_PWEI_OPRID=?";
			String scoreInsId = sqlQuery.queryForObject(sql, new Object[]{classId, applyBatchId, bmbId, oprid}, "String");
			
			
			/*当前考生基本信息*/
			String sqlBasic = "SELECT A.TZ_CLASS_NAME,YEAR(A.TZ_START_DT) TZ_START_YEAR,A.TZ_JG_ID,A.TZ_PS_APP_MODAL_ID,A.TZ_ZLPS_SCOR_MD_ID,A.TZ_MSCJ_SCOR_MD_ID,A.TZ_APP_MODAL_ID,C.TZ_APP_FORM_STA,B.OPRID,D.TZ_REALNAME,D.TZ_MSH_ID,F.TZ_BATCH_NAME";
			sqlBasic = sqlBasic + ",(SELECT G.TREE_NAME FROM PS_TZ_RS_MODAL_TBL G WHERE G.TZ_JG_ID=A.TZ_JG_ID AND G.TZ_SCORE_MODAL_ID=A.TZ_ZLPS_SCOR_MD_ID) TREE_NAME_MATERIAL";
			sqlBasic = sqlBasic + ",(SELECT G.TREE_NAME FROM PS_TZ_RS_MODAL_TBL G WHERE G.TZ_JG_ID=A.TZ_JG_ID AND G.TZ_SCORE_MODAL_ID=A.TZ_MSCJ_SCOR_MD_ID) TREE_NAME,G.TZ_PRJ_NAME";
			sqlBasic = sqlBasic + " FROM PS_TZ_AQ_YHXX_TBL D,PS_TZ_APP_INS_T C,PS_TZ_FORM_WRK_T B,PS_TZ_CLS_BATCH_T F,PS_TZ_CLASS_INF_T A,PS_TZ_PRJ_INF_T G";
			sqlBasic = sqlBasic + " WHERE A.TZ_CLASS_ID=B.TZ_CLASS_ID AND B.TZ_APP_INS_ID = C.TZ_APP_INS_ID AND B.OPRID=D.OPRID AND A.TZ_PRJ_ID = G.TZ_PRJ_ID AND A.TZ_CLASS_ID=? AND A.TZ_CLASS_ID = F.TZ_CLASS_ID AND F.TZ_BATCH_ID=? AND B.TZ_APP_INS_ID=?";

			Map<String, Object> mapRootBasic = sqlQuery.queryForMap(sqlBasic,new Object[] { classId, applyBatchId, bmbId});
			
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
			String interviewApplyId = mapRootBasic.get("TZ_MSH_ID") == null ? "" : mapRootBasic.get("TZ_MSH_ID").toString();
			String jgId = mapRootBasic.get("TZ_JG_ID") == null ? "" : mapRootBasic.get("TZ_JG_ID").toString();
			String psBmbTplId = mapRootBasic.get("TZ_PS_APP_MODAL_ID") == null ? "" : mapRootBasic.get("TZ_PS_APP_MODAL_ID").toString();
			String scoreModelIdMaterial = mapRootBasic.get("TZ_ZLPS_SCOR_MD_ID") == null ? "" : mapRootBasic.get("TZ_ZLPS_SCOR_MD_ID").toString();
			String scoreTreeMaterial = mapRootBasic.get("TREE_NAME_MATERIAL") == null ? "" : mapRootBasic.get("TREE_NAME_MATERIAL").toString();
			String scoreModelId = mapRootBasic.get("TZ_MSCJ_SCOR_MD_ID") == null ? "" : mapRootBasic.get("TZ_MSCJ_SCOR_MD_ID").toString();
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
			mapRet.put("psBmbTplId", psBmbTplId);

			
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
			

			//材料评审成绩参考
			String ckcjxIdNum = getHardCodePoint.getHardCodePointVal("TZ_MSPS_CLPS_CKCJX_ID_NUM");
			String ckcjxIdNumTmp = ","+ckcjxIdNum+",";
			String ckcjxIdTxt = getHardCodePoint.getHardCodePointVal("TZ_MSPS_CLPS_CKCJX_ID_TXT");
			String[] ckcjxIdTxtArr = ckcjxIdTxt.split(",");
			String pyCjxId = getHardCodePoint.getHardCodePointVal("TZ_CLPS_PYCJX_ID");
			
			String materialReviewDesc = "";
			//PAD版使用
			String materialReviewDescPad = "<table width='100%'><tbody>";
			
			String sqlClps = "";
			String ckcjxNumInfo = "",ckcjxTxtInfo = "",ckcjxNumInfoPad = "";
			
			//数字成绩录入项
			sqlClps = "SELECT M.TZ_SCORE_ITEM_ID,M.AVG_NUM,N.DESCR";
			sqlClps += " FROM";
			sqlClps += " (SELECT B.TZ_SCORE_ITEM_ID,AVG(B.TZ_SCORE_NUM) AVG_NUM FROM PS_TZ_CJX_TBL B";
			sqlClps += " WHERE EXISTS (SELECT 'Y' FROM PS_TZ_CP_PW_KS_TBL C WHERE B.TZ_SCORE_INS_ID=C.TZ_SCORE_INS_ID AND C.TZ_CLASS_ID=? AND C.TZ_APPLY_PC_ID=? AND C.TZ_APP_INS_ID=?)";
			sqlClps += " AND EXISTS (SELECT 'Y' FROM PS_TZ_MODAL_DT_TBL D WHERE D.TZ_SCORE_ITEM_ID=B.TZ_SCORE_ITEM_ID AND D.TREE_NAME=? AND D.TZ_SCORE_ITEM_TYPE='B')";
			sqlClps += " GROUP BY B.TZ_SCORE_ITEM_ID) M,";
			sqlClps += " (SELECT A.TZ_SCORE_ITEM_ID,A.DESCR FROM PS_TZ_MODAL_DT_TBL A WHERE A.TREE_NAME=?) N";
			sqlClps += " WHERE M.TZ_SCORE_ITEM_ID=N.TZ_SCORE_ITEM_ID";

			List<Map<String, Object>> listClpsNum = sqlQuery.queryForList(sqlClps, new Object[] {classId,applyBatchId,bmbId,scoreTreeMaterial,scoreTreeMaterial});
			
			for(Map<String, Object> mapClpsNum : listClpsNum) {
				String scoreItemIdClps = mapClpsNum.get("TZ_SCORE_ITEM_ID") == null ? "" : mapClpsNum.get("TZ_SCORE_ITEM_ID").toString();
				String scoreItemNameClps = mapClpsNum.get("DESCR") == null ? "" : mapClpsNum.get("DESCR").toString();
				Double scoreAvgNum = mapClpsNum.get("AVG_NUM") == null ? 0.0 : Double.valueOf(mapClpsNum.get("AVG_NUM").toString());
				
				Integer findFlag = ckcjxIdNumTmp.indexOf(","+scoreItemIdClps+",");
				if("-1".equals(String.valueOf(findFlag))) {
					//未找到
				} else {	
					ckcjxNumInfo = ckcjxNumInfo + scoreItemNameClps+"【"+scoreAvgNum+"】&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
				}
			}
			
			if(!"".equals(ckcjxNumInfo)) {
				materialReviewDesc += "<tr height='25'><td style='font-weight:bold;' width='127px'>材料评审成绩参考：</td><td colspan='4'>" + ckcjxNumInfo + "</td></tr>";
				materialReviewDescPad +="<tr><td width='155px;'>材料评审成绩参考：</td><td>" + ckcjxNumInfo + "</td></tr>";
			}
			
			//评语类型
			int i=0;
			for(i=0;i<ckcjxIdTxtArr.length;i++) {
				String scoreItemIdClps = ckcjxIdTxtArr[i];
				
				sqlClps = "SELECT M.TZ_SCORE_PY_VALUE,N.DESCR";
				sqlClps += " FROM";
				sqlClps += " (SELECT B.TZ_SCORE_ITEM_ID,B.TZ_SCORE_INS_ID,B.TZ_SCORE_PY_VALUE FROM PS_TZ_CJX_TBL B";
				sqlClps += " WHERE EXISTS (SELECT 'Y' FROM PS_TZ_CP_PW_KS_TBL C WHERE B.TZ_SCORE_INS_ID=C.TZ_SCORE_INS_ID AND C.TZ_CLASS_ID=? AND C.TZ_APPLY_PC_ID=? AND C.TZ_APP_INS_ID=?";
				sqlClps += " AND B.TZ_SCORE_ITEM_ID=?)) M,";
				sqlClps += " (SELECT A.TZ_SCORE_ITEM_ID,A.DESCR FROM PS_TZ_MODAL_DT_TBL A WHERE A.TREE_NAME=?) N";
				sqlClps += " WHERE M.TZ_SCORE_ITEM_ID=N.TZ_SCORE_ITEM_ID";
				
				List<Map<String, Object>> listClpsTxt = sqlQuery.queryForList(sqlClps, new Object[] {classId,applyBatchId,bmbId,scoreItemIdClps,scoreTreeMaterial});
				
				int num = 0;
				String ckcjxTxtInfoTmp = "";
				
				for(Map<String, Object> mapClpsTxt : listClpsTxt) {
					String scoreItemNameClps = mapClpsTxt.get("DESCR") == null ? "" : mapClpsTxt.get("DESCR").toString();
					String scoreValue = mapClpsTxt.get("TZ_SCORE_PY_VALUE") == null ? "": mapClpsTxt.get("TZ_SCORE_PY_VALUE").toString();
					
					if(!"".equals(scoreValue)) {
						num++;
						
						if(scoreItemIdClps.equals(pyCjxId)) {
							//评语
							if(num==1) {
								ckcjxTxtInfoTmp = "评委"+String.valueOf(num)+"评语："+scoreValue;
							} else {
								ckcjxTxtInfoTmp +="</br>评委"+String.valueOf(num)+"评语："+scoreValue;
							}
						} else {
							if(num==1) {
								ckcjxTxtInfoTmp = "<div style='float:left;width:90px;'>" + scoreItemNameClps+"：</div><div style='float:left;'>"+scoreValue+"</div><br />";
							} else {
								//ckcjxTxtInfoTmp += "<div style='padding-left:90px;'>" + scoreValue + "</div>";
								ckcjxTxtInfoTmp += "<div>" + scoreValue + "</div>";
							}
						}	
					}
				}
				
				String trHeight = String.valueOf(num*25);
				
				if(!"".equals(ckcjxTxtInfoTmp)) {
					if(!"".equals(ckcjxNumInfo)) {
						ckcjxTxtInfo += "<tr height='"+trHeight+"'><td style='font-weight:bold;' width='127px'></td><td colspan='4' style='word-wrap:break-word;word-break:break-all;'>"+ ckcjxTxtInfoTmp +"</td></tr>";
						ckcjxNumInfoPad += "<tr><td></td><td>" + ckcjxTxtInfoTmp + "</td></tr>";
					} else {
						ckcjxTxtInfo += "<tr height='"+trHeight+"'><td style='font-weight:bold;' width='127px'>材料评审成绩参考：</td><td colspan='4'>"+ ckcjxTxtInfoTmp +"</td></tr>";
						ckcjxNumInfoPad +="<tr><td width='155px;'>材料评审成绩参考：</td><td>" + ckcjxTxtInfoTmp + "</td></tr>";
					}
				}

			}
			
			materialReviewDesc += ckcjxTxtInfo;
			materialReviewDescPad += ckcjxNumInfoPad + "</tbody></table>";
			
			mapRet.put("materialReviewDesc", materialReviewDesc);
			mapRet.put("materialReviewDescPad", materialReviewDescPad);

		
			/*成绩项*/
			ArrayList<Map<String, Object>> scoreItemJson = new ArrayList<Map<String,Object>>();
			
			String sqlScore = "SELECT A.TREE_NODE,A.PARENT_NODE_NAME,B.DESCR,B.TZ_SCORE_ITEM_TYPE,(SELECT 'Y' FROM PSTREENODE C WHERE C.TREE_NAME=A.TREE_NAME AND C.TREE_NODE_NUM>A.TREE_NODE_NUM AND C.TREE_NODE_NUM_END<A.TREE_NODE_NUM_END  LIMIT 1) TZ_NO_LEAF,A.TREE_LEVEL_NUM,B.TZ_SCORE_LIMITED,B.TZ_SCORE_LIMITED2,B.TZ_SCORE_PY_ZSLIM,B.TZ_SCORE_PY_ZSLIM0,B.TZ_SCORE_ITEM_DFSM,B.TZ_SCORE_ITEM_CKWT,B.TZ_SCORE_ITEM_MSFF,B.TZ_SCORE_CKZL";
			sqlScore = sqlScore + " FROM PSTREENODE A,PS_TZ_MODAL_DT_TBL B";
			sqlScore = sqlScore + " WHERE A.TREE_NAME=B.TREE_NAME AND A.TREE_NODE=B.TZ_SCORE_ITEM_ID AND A.TREE_NAME=?";
			sqlScore = sqlScore + " ORDER BY A.TREE_NODE_NUM,A.TREE_NODE_NUM_END DESC";

			List<Map<String,Object>> scoreList = sqlQuery.queryForList(sqlScore,new Object[]{scoreTree});

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
				String scoreItemDfsm = mapScore.get("TZ_SCORE_ITEM_DFSM") == null ? "" : mapScore.get("TZ_SCORE_ITEM_DFSM").toString();
				String scoreItemCkwt = mapScore.get("TZ_SCORE_ITEM_CKWT") == null ? "" : mapScore.get("TZ_SCORE_ITEM_CKWT").toString();
				String scoreItemMsff = mapScore.get("TZ_SCORE_ITEM_MSFF") == null ? "" : mapScore.get("TZ_SCORE_ITEM_MSFF").toString();
				String scoreItemCkzl = mapScore.get("TZ_SCORE_CKZL") == null ? "" : mapScore.get("TZ_SCORE_CKZL").toString();//参考资料
				
				/*查询成绩项分值、评语值、下拉框值*/
				String sqlScoreValue = "SELECT TZ_CJX_XLK_XXBH,TZ_SCORE_NUM, TZ_SCORE_PY_VALUE FROM PS_TZ_CJX_TBL WHERE TZ_SCORE_INS_ID=? AND TZ_SCORE_ITEM_ID=?";
				Map<String, Object> mapScoreValue = sqlQuery.queryForMap(sqlScoreValue,new Object[] {scoreInsId,scoreItemId});
				if(mapScoreValue==null) {
					
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
				mapScoreJson.put("itemMsff", scoreItemMsff);
				mapScoreJson.put("itemCkzl", scoreItemCkzl);
				mapScoreJson.put("scoreModelId", scoreModelId);
				
				scoreItemJson.add(mapScoreJson);
			}
			
			mapRet.put("scoreContent", scoreItemJson);
			
			//左侧考生列表header
			LinkedHashMap<String, Object> mapHeader = new LinkedHashMap<String,Object>();		
			mapHeader.put("ps_msh_id", "面试申请号");
			mapHeader.put("ps_ksh_xm", "姓名");
			mapHeader.put("ps_ksh_xh", "面试顺序");
			mapHeader.put("ps_ksh_ppm", "排名");
			mapHeader.put("col01", "总分");
			
			
			mapRet.put("ksGridHeader", mapHeader);
			
			
			//搜索考生区域“查找”按钮使用参数
			mapRet.put("ps_class_id", classId);
			mapRet.put("ps_pc_id", applyBatchId);

			
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
	 * 搜索考生-查找按钮
	 * 
	 * @param strParams
	 * @return
	 */
	public String searchExaminee(String strParams) {
		String strRet = "";
		
		Map<String, Object> mapRet = new HashMap<>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);
		
		try {
			
			String result = "";
			String resultMsg = "";
			
			String classId = jacksonUtil.getString("CLASSID");
			String applyBatchId = jacksonUtil.getString("APPLY_BATCHID");
			String searchMsid = jacksonUtil.getString("KSH_SEARCH_MSID");
			String searchName = jacksonUtil.getString("KSH_SEARCH_NAME");
			
			if("".equals(searchMsid) && "".equals(searchName)) {
				result = "1";
				resultMsg = "请输入查询关键字！";
			} else {
				List<Map<String, Object>> searchList = new ArrayList<>();
				Integer count = 0;
				String sql = "SELECT A.OPRID,A.TZ_APP_INS_ID,C.TZ_MSH_ID,C.TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL C,PS_TZ_FORM_WRK_T A,PS_TZ_MSPS_KSH_TBL B";
				sql = sql + " WHERE A.TZ_APP_INS_ID=B.TZ_APP_INS_ID AND A.OPRID=C.OPRID AND B.TZ_CLASS_ID=? AND B.TZ_APPLY_PC_ID=?";
				
				if(!"".equals(searchMsid) && !"".equals(searchName)){
					sql = sql + " AND C.TZ_MSH_ID=? AND C.TZ_REALNAME=?";
					searchList = sqlQuery.queryForList(sql, new Object[]{classId,applyBatchId,searchMsid,searchName});
				} else {
					if(!"".equals(searchMsid)) {
						sql = sql + " AND C.TZ_MSH_ID=?";
						searchList = sqlQuery.queryForList(sql, new Object[]{classId,applyBatchId,searchMsid});
					} else {
						if(!"".equals(searchName)) {
							sql = sql + " AND C.TZ_REALNAME=?";
							searchList = sqlQuery.queryForList(sql, new Object[]{classId,applyBatchId,searchName});
						}
					}
				}
				
				for(Map<String, Object> mapSearch : searchList) {
					count++;
					
					if(count>1) {
						break;
					} 
					
					String oprid = mapSearch.get("OPRID") == null ? "" : mapSearch.get("OPRID").toString();
					String bmbId = mapSearch.get("TZ_APP_INS_ID") == null ? "" : mapSearch.get("TZ_APP_INS_ID").toString();
					String mssqh = mapSearch.get("TZ_MSH_ID") == null ? "" : mapSearch.get("TZ_MSH_ID").toString();
					String name = mapSearch.get("TZ_REALNAME") == null ? "" : mapSearch.get("TZ_REALNAME").toString();
					
					result = "0";
					resultMsg = "";

					mapRet.put("oprid", oprid);
					mapRet.put("bmbId", bmbId);
					mapRet.put("mssqh", mssqh);
					mapRet.put("name", name);
					
				}
				
				if(count<1) {
					result = "1";
					resultMsg = "未查找到相关考生信息";
				} else {
					if(count>1) {
						result = "1";
						resultMsg = "查找到<b>多个</b>相关考生信息，请输入准确的检索条件";
					}
				}
			}
			
			mapRet.put("result",result);
			mapRet.put("resultMsg", resultMsg);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		strRet = jacksonUtil.Map2json(mapRet);
		return strRet;
	}
	
	/**
	 * 搜索考生-进行评审按钮
	 * 
	 * @param strParams
	 * @return
	 */
	public String addExamineeForJudge(String strParams) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<>();
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);
		
		String result = "0";
		String resultMsg = "";
		
		try {
			/*当前登录人*/
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			
			String classId = jacksonUtil.getString("CLASSID");
			String applyBatchId = jacksonUtil.getString("APPLY_BATCHID");
			String bmbId = jacksonUtil.getString("KSH_BMBID");
			
			String sql ;
			
			sql = "SELECT A.TZ_DQPY_ZT,A.TZ_MSPY_NUM,B.TZ_PWEI_ZHZT";
			sql = sql + ",(SELECT C.TZ_SUBMIT_YN FROM PS_TZ_MSPWPSJL_TBL C WHERE C.TZ_CLASS_ID=A.TZ_CLASS_ID AND C.TZ_APPLY_PC_ID=A.TZ_APPLY_PC_ID AND C.TZ_PWEI_OPRID=B.TZ_PWEI_OPRID) TZ_SUBMIT_YN";
			sql = sql + ",(SELECT 'Y' FROM PS_TZ_MP_PW_KS_TBL D WHERE D.TZ_CLASS_ID=A.TZ_CLASS_ID AND D.TZ_APPLY_PC_ID=A.TZ_APPLY_PC_ID AND D.TZ_PWEI_OPRID=B.TZ_PWEI_OPRID AND D.TZ_APP_INS_ID=? AND D.TZ_DELETE_ZT='N') TZ_IS_PWKS";
			sql = sql + ",(SELECT COUNT(1) FROM PS_TZ_MP_PW_KS_TBL D WHERE D.TZ_CLASS_ID=A.TZ_CLASS_ID AND D.TZ_APPLY_PC_ID=A.TZ_APPLY_PC_ID AND D.TZ_APP_INS_ID=? AND D.TZ_DELETE_ZT='N') TZ_KSDPW_NUM";
			sql = sql + " FROM PS_TZ_MSPS_PW_TBL B,PS_TZ_MSPS_GZ_TBL A";
			sql = sql + " WHERE A.TZ_CLASS_ID=B.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID=B.TZ_APPLY_PC_ID";
			sql = sql + " AND B.TZ_PWEI_OPRID=? AND A.TZ_CLASS_ID=? AND A.TZ_APPLY_PC_ID=?";
			
			Map<String, Object> mapBasic = sqlQuery.queryForMap(sql,new Object[]{bmbId,bmbId,oprid,classId,applyBatchId});
			
			String dqpyZt = mapBasic.get("TZ_DQPY_ZT") == null ? "" : mapBasic.get("TZ_DQPY_ZT").toString();
			Integer mspyNum = mapBasic.get("TZ_MSPY_NUM") == null ? 0 : Integer.valueOf(mapBasic.get("TZ_MSPY_NUM").toString());
			String pwewZhzt = mapBasic.get("TZ_PWEI_ZHZT") == null ? "" : mapBasic.get("TZ_PWEI_ZHZT").toString();
			String submitAllFlag = mapBasic.get("TZ_SUBMIT_YN") == null ? "" : mapBasic.get("TZ_SUBMIT_YN").toString();
			String pwksFlag = mapBasic.get("TZ_IS_PWKS") == null ? "" : mapBasic.get("TZ_IS_PWKS").toString();
			Integer ksdpwNum = mapBasic.get("TZ_KSDPW_NUM") == null ? 0 : Integer.valueOf(mapBasic.get("TZ_KSDPW_NUM").toString());
			
			if("B".equals(dqpyZt)) {
				result = "1";
				resultMsg = "该批次的评审已关闭";
			} else {
				if("Y".equals(submitAllFlag)) {
					result = "1";
					resultMsg = "评议数据已经提交，不允许新增考生评议数据";
				} else {
					if("B".equals(pwewZhzt)) {
						result = "1";
						resultMsg = "您的账号暂时无法评议该批次，请与管理员联系";
					} else {
						if("Y".equals(pwksFlag)) {
							//当前评委与考试的关系已存在，且并未移除
							result = "2";
							resultMsg = "";
						} else {
							//当前考生是否达到评委上线
							if(ksdpwNum>0) {
								if(ksdpwNum<mspyNum) {
									//当前评委与评议该考生的其他评委是否在同一组
									sql = "SELECT 'Y' FROM PS_TZ_MSPS_PW_TBL A";
									sql = sql + " WHERE EXISTS (SELECT 'Y' FROM (SELECT C.TZ_CLASS_ID,C.TZ_APPLY_PC_ID,C.TZ_PWEI_OPRID,B.TZ_PWEI_GRPID FROM PS_TZ_MP_PW_KS_TBL C ,PS_TZ_MSPS_PW_TBL B";
									sql = sql + " WHERE B.TZ_CLASS_ID=C.TZ_CLASS_ID AND B.TZ_APPLY_PC_ID=C.TZ_APPLY_PC_ID AND B.TZ_PWEI_OPRID=C.TZ_PWEI_OPRID AND C.TZ_APP_INS_ID=?) X";
									sql = sql + " WHERE X.TZ_CLASS_ID=A.TZ_CLASS_ID AND X.TZ_APPLY_PC_ID=A.TZ_APPLY_PC_ID AND X.TZ_PWEI_GRPID=A.TZ_PWEI_GRPID) AND A.TZ_CLASS_ID=? AND A.TZ_APPLY_PC_ID=? AND A.TZ_PWEI_OPRID=?" ;
									String sameFzFlag = sqlQuery.queryForObject(sql, new Object[]{bmbId,classId,applyBatchId,oprid},"String");
									
									if(!"Y".equals(sameFzFlag)) {
										result = "1";
										resultMsg = "您与其他评委不在同一组，无法评议该考生";	
									}
								} else {
									result = "1";
									resultMsg = "该考生已完成评审，无需再评审";
								}
							}
						}
					}
				}
			}
			
			if(!"1".equals(result) && !"2".equals(result)) {
				//面试评审评委考生关系表TZ_MP_PW_KS_TBL
				PsTzMpPwKsTblKey psTzMpPwKsTblKey = new PsTzMpPwKsTblKey();
				psTzMpPwKsTblKey.setTzClassId(classId);
				psTzMpPwKsTblKey.setTzApplyPcId(applyBatchId);
				psTzMpPwKsTblKey.setTzAppInsId(Long.valueOf(bmbId));
				psTzMpPwKsTblKey.setTzPweiOprid(oprid);
				
				PsTzMpPwKsTbl psTzMpPwKsTbl = psTzMpPwKsTblMapper.selectByPrimaryKey(psTzMpPwKsTblKey);
				if(psTzMpPwKsTbl==null) {
					psTzMpPwKsTbl = new PsTzMpPwKsTbl();
					psTzMpPwKsTbl.setTzClassId(classId);
					psTzMpPwKsTbl.setTzApplyPcId(applyBatchId);
					psTzMpPwKsTbl.setTzAppInsId(Long.valueOf(bmbId));
					psTzMpPwKsTbl.setTzPweiOprid(oprid);
					psTzMpPwKsTbl.setTzDeleteZt("N");
					psTzMpPwKsTbl.setTzMshiKssj(new Date());
					psTzMpPwKsTbl.setTzPshenZt("N");
					psTzMpPwKsTbl.setRowAddedDttm(new Date());
					psTzMpPwKsTbl.setRowAddedOprid(oprid);
					psTzMpPwKsTbl.setRowLastmantDttm(new Date());
					psTzMpPwKsTbl.setRowLastmantOprid(oprid);
					psTzMpPwKsTblMapper.insert(psTzMpPwKsTbl);
				} else {
					psTzMpPwKsTbl.setTzDeleteZt("N");
					psTzMpPwKsTbl.setTzMshiKssj(new Date());
					psTzMpPwKsTbl.setRowLastmantDttm(new Date());
					psTzMpPwKsTbl.setRowLastmantOprid(oprid);
					psTzMpPwKsTblMapper.updateByPrimaryKeySelective(psTzMpPwKsTbl);
				}
				
				//面试评审考生评委信息TZ_MSPSKSPW_TBL
				sql = "SELECT TZ_MSPW_LIST FROM PS_TZ_MSPSKSPW_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=?";
				String mspwList = sqlQuery.queryForObject(sql, new Object[]{classId,applyBatchId,bmbId},"String");
				String mspwListTmp = ","+mspwList+",";
				String strFind = ","+oprid+",";
				Integer findResult = mspwListTmp.indexOf(strFind);
				if("-1".equals(findResult)) {
					//未找到
					if("".equals(mspwList)) {
						mspwList = oprid;
					} else {
						mspwList = mspwList+","+oprid;
					}
				}
				
				PsTzMspskspwTblKey psTzMspskspwTblKey = new PsTzMspskspwTblKey();
				psTzMspskspwTblKey.setTzClassId(classId);
				psTzMspskspwTblKey.setTzApplyPcId(applyBatchId);
				psTzMspskspwTblKey.setTzAppInsId(Long.valueOf(bmbId));
				
				PsTzMspskspwTbl psTzMspskspwTbl = psTzMspskspwTblMapper.selectByPrimaryKey(psTzMspskspwTblKey);
				
				if(psTzMspskspwTbl==null) {
					psTzMspskspwTbl = new PsTzMspskspwTbl();
					psTzMspskspwTbl.setTzClassId(classId);
					psTzMspskspwTbl.setTzApplyPcId(applyBatchId);
					psTzMspskspwTbl.setTzAppInsId(Long.valueOf(bmbId));
					psTzMspskspwTbl.setTzMspwList(mspwList);
					psTzMspskspwTbl.setRowAddedDttm(new Date());
					psTzMspskspwTbl.setRowAddedOprid(oprid);
					psTzMspskspwTbl.setRowLastmantDttm(new Date());
					psTzMspskspwTbl.setRowLastmantOprid(oprid);
					psTzMspskspwTblMapper.insert(psTzMspskspwTbl);
				} else {
					psTzMspskspwTbl.setTzMspwList(mspwList);
					psTzMspskspwTbl.setRowLastmantDttm(new Date());
					psTzMspskspwTbl.setRowLastmantOprid(oprid);
					psTzMspskspwTblMapper.updateByPrimaryKeySelective(psTzMspskspwTbl);
				}
				
				//重新进行排名
				updateExamineeRank(classId,applyBatchId,oprid,bmbId);
				
			}
			
			mapRet.put("result", result);
			mapRet.put("resultMsg", resultMsg);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		strRet = jacksonUtil.Map2json(mapRet);
		return strRet;
	}
	
	
	/**
	 * 计算排名
	 * 
	 * @param 
	 * 	classId-班级编号
	 *  applyBatchId-批次编号
	 *  pwOprid-评委账号
	 * @return
	 */
	public void updateExamineeRank(String classId,String applyBatchId,String pwOprid,String currentBmbId) {

		try {
			/*当前机构*/
			String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			
			String sql = tzSQLObject.getSQLText("SQL.TZEvaluationSystemBundle.TzInterviewExamineeRank");	
			
			List<Map<String, Object>> listData = sqlQuery.queryForList(sql, new Object[] {orgId,classId,applyBatchId,pwOprid});
			
			for(Map<String, Object> mapData : listData) {
				
				Long bmbId = 0L;
				String strBmbId = mapData.get("TZ_APP_INS_ID") == null ? "" : mapData.get("TZ_APP_INS_ID").toString();
				if(!"".equals(strBmbId)) {
					bmbId = Long.valueOf(strBmbId);
				}
				 
				String strRank = mapData.get("RANK") == null ? "" : mapData.get("RANK").toString();
				if(strRank.indexOf(".")>0) {
					strRank = strRank.substring(0, strRank.indexOf("."));
				}
				
				PsTzMpPwKsTblKey psTzMpPwKsTblKey = new PsTzMpPwKsTblKey();
				psTzMpPwKsTblKey.setTzClassId(classId);
				psTzMpPwKsTblKey.setTzApplyPcId(applyBatchId);
				psTzMpPwKsTblKey.setTzAppInsId(bmbId);
				psTzMpPwKsTblKey.setTzPweiOprid(pwOprid);
				
				PsTzMpPwKsTbl psTzMpPwKsTbl = psTzMpPwKsTblMapper.selectByPrimaryKey(psTzMpPwKsTblKey);
				if(psTzMpPwKsTbl==null) {
					
				} else {
					psTzMpPwKsTbl.setTzKshPspm(strRank);
					if(bmbId.equals(currentBmbId)) {
						//当前打分考生更新评审时间
						psTzMpPwKsTbl.setRowLastmantDttm(new Date());
						psTzMpPwKsTbl.setRowLastmantOprid(pwOprid);
					}
					psTzMpPwKsTblMapper.updateByPrimaryKeySelective(psTzMpPwKsTbl);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
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
			sql = sql + " WHERE B.TZ_JG_ID=C.TZ_JG_ID AND B.TZ_SCORE_MODAL_ID=C.TZ_MSCJ_SCOR_MD_ID AND A.TREE_NAME=B.TREE_NAME AND C.TZ_CLASS_ID=?";

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
						if(scoreItemValue.length()>=scoreItemCommentLower && scoreItemValue.length()<=scoreItemCommentUpper) {
							scoreValid2="Y";
						}
						/*if(scoreItemValue.length()<=scoreItemCommentUpper) {
							scoreValid2="Y";
						} */
					} 
				}
				
				if("B".equals(scoreItemType)) {
					if("Y".equals(scoreValid1)) {	
						//成绩校验成功
					} else {
						//成绩校验失败
						resultMsg = resultMsg + "【" + scoreItemName + "】分数填写错误，请重新填写！";
					}
				}
				
				if("C".equals(scoreItemType)) {
					if("Y".equals(scoreValid2)) {
						//评语校验成功
					} else {
						//评语校验失败
						resultMsg = resultMsg + "【" + scoreItemName + "】评语字数范围不正确，请重新填写！";
					}
				}
				
				mapItemsScore.put(scoreItemId, scoreItemValue);
			}
			
			if(resultMsg!="" && resultMsg!=null) {
				result = "1";
			} else {
				//调用保存成绩项方法
				itemScoreParams = jacksonUtil.Map2json(mapItemsScore);
				String [] saveRet = tzScoreInsCalculationObject.PwdfSaveUpdate("MS", classId, applyBatchId, bmbId, itemScoreParams);
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
	
}
