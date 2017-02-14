package com.tranzvision.gd.TZMbaPwClpsBundle.service.impl;

import java.util.Date;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

import org.apache.ibatis.jdbc.SQL;
import org.apache.jasper.compiler.AntCompiler.JasperAntLogger;
import org.apache.tomcat.util.bcel.classfile.ElementValue;
import org.apache.xmlbeans.SchemaStringEnumEntry;
import org.omg.CORBA.OBJ_ADAPTER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.google.common.collect.MapMaker;
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
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.MySqlLockService;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * MBA材料面试评审-评委系统-材料评审-打分页
 * 
 * @author LUYAN
 * @since 2017-02-08
 */

@Service("com.tranzvision.gd.TZMbaPwClpsBundle.service.impl.TzMbaPwClpsScoreServiceImpl")

public class TzMbaPwClpsScoreServiceImpl extends FrameworkImpl{

	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private HttpServletRequest request;	
	@Autowired
	private MySqlLockService mySqlLockService;
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
	
	
	@Override
	public String tzQuery(String strParams,String[] errMsg) {
		String strRtn = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			jacksonUtil.json2Map(strParams);
			
			String classId = jacksonUtil.getString("classId");
			String applyBatchId = jacksonUtil.getString("applyBatchId");
			String bmbId = jacksonUtil.getString("bmbId");
			
			if(classId != null && applyBatchId != null && bmbId != null) {
				strRtn = this.getExamineeScoreInfo(classId, applyBatchId, bmbId, errMsg);						
			}			
		} catch (Exception e) {
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
				jacksonUtil.parseJson2Map(strForm);
				
				String classId = jacksonUtil.getString("classId");
				String applyBatchId = jacksonUtil.getString("applyBatchId");
				String bmbId = jacksonUtil.getString("bmbId");
				String type = jacksonUtil.getString("type");
				
				//当前登录人
				String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
				
				String sql;
				
				//当前评审轮次、实时计算评委偏差
				sql = "SELECT TZ_DQPY_LUNC,TZ_REAL_TIME_PWPC FROM PS_TZ_CLPS_GZ_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
				Map<String, Object> mapData = jdbcTemplate.queryForMap(sql, new Object[]{classId,applyBatchId});
				String dqpyLunc = mapData.get("TZ_DQPY_LUNC").toString();
				String realPwpc = mapData.get("TZ_REAL_TIME_PWPC").toString();
						
				
				//判断当前评审轮次是否已提交，如果已提交，不允许修改
				sql = "SELECT TZ_SUBMIT_YN FROM PS_TZ_CLPWPSLS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_PWEI_OPRID=? AND TZ_CLPS_LUNC=?";
				String dqpyLuncState = jdbcTemplate.queryForObject(sql, new Object[]{classId,applyBatchId,oprid,dqpyLunc},"String");
				
				if(dqpyLuncState=="Y") {
					errMsg[0] = "1";
					errMsg[1] = "评议数据已经提交，不允许对考生数据进行修改";
				} else {
					//更新考生评审得分历史表
					sql = "UPDATE PS_TZ_KSCLPSLS_TBL SET TZ_SUBMIT_YN='Y'";
					sql = sql + " WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=? AND TZ_PWEI_OPRID=? AND TZ_CLPS_LUNC=?";
					jdbcTemplate.update(sql,new Object[]{classId,applyBatchId,bmbId,oprid,dqpyLunc});
					
					//保存成绩项
					String saveScoreItemRtn = this.scoreItemSave(classId,applyBatchId,bmbId,strForm,errMsg);
					jacksonUtil.json2Map(saveScoreItemRtn);
					String saveRtnResult = jacksonUtil.getString("result");
					
					if(saveRtnResult=="1") {
						errMsg[0] = "1";
						errMsg[1] = jacksonUtil.getString("resultMsg");
					} else {
						
						//根据配置判断是否实时计算评委间偏差
						if(realPwpc=="Y") {
							this.calculatePwDeviation(classId,applyBatchId,bmbId,dqpyLunc,oprid,errMsg);
						}
						
						//更新考生评审得分历史表
						this.examineeReviewHis(classId, applyBatchId, bmbId, oprid, dqpyLunc,errMsg);
						
						//计算排名
						this.examineeRank(classId,applyBatchId,oprid,errMsg);
						
						if(type=="SGN") {
							/*保存提交并获取下一个考生*/
							String getNext = this.getNextExaminee(classId,applyBatchId,oprid,errMsg);
							jacksonUtil.parseJson2Map(getNext);
							bmbId = jacksonUtil.getString("bmbIdNext");
						}
						
						strRtn = this.getExamineeScoreInfo(classId, applyBatchId, bmbId, errMsg);
					}
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
	 * 获取考生打分页信息
	 */
	private String getExamineeScoreInfo(String classId,String applyBatchId,String bmbId,String[] errMsg) {		
		String strRtn = "";
		Map<String,Object> mapRet = new HashMap<String,Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			
			/*当前登录人*/
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

			/*当前考生在当前评委下的成绩单ID*/
			String sql = "SELECT TZ_SCORE_INS_ID FROM PS_TZ_CP_PW_KS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=? AND TZ_PWEI_OPRID=?";
			String scoreInsId = jdbcTemplate.queryForObject(sql, new Object[]{classId, applyBatchId, bmbId, oprid}, "String");
			
			
			/*当前考生基本信息*/
			String sqlBasic = "SELECT A.TZ_CLASS_NAME,A.TZ_JG_ID,A.TZ_ZLPS_SCOR_MD_ID,A.TZ_APP_MODAL_ID,C.TZ_APP_FORM_STA,B.OPRID,D.TZ_REALNAME,E.TZ_MSSQH,F.TZ_BATCH_NAME";
			sqlBasic = sqlBasic + ",(SELECT G.TREE_NAME FROM PS_TZ_RS_MODAL_TBL G WHERE G.TZ_JG_ID=A.TZ_JG_ID AND G.TZ_SCORE_MODAL_ID=A.TZ_ZLPS_SCOR_MD_ID) TREE_NAME";
			sqlBasic = sqlBasic + " FROM PS_TZ_REG_USER_T E,PS_TZ_AQ_YHXX_TBL D,PS_TZ_APP_INS_T C,PS_TZ_FORM_WRK_T B,PS_TZ_CLS_BATCH_T F,PS_TZ_CLASS_INF_T A";
			sqlBasic = sqlBasic + " WHERE A.TZ_CLASS_ID=B.TZ_CLASS_ID AND B.TZ_APP_INS_ID = C.TZ_APP_INS_ID AND B.OPRID=D.OPRID AND B.OPRID=E.OPRID AND A.TZ_CLASS_ID=? AND A.TZ_CLASS_ID = F.TZ_CLASS_ID AND F.TZ_BATCH_ID=? AND B.TZ_APP_INS_ID=?";

			Map<String, Object> mapRootBasic = jdbcTemplate.queryForMap(sqlBasic,new Object[] { classId, applyBatchId, bmbId});
			
			if(mapRootBasic == null) {
				return strRtn;
			}
			
			//获取报名表链接
			String bmbUrl = "http://www.baidu.com";
			String applyBatchName = mapRootBasic.get("TZ_BATCH_NAME").toString();
			String name = mapRootBasic.get("TZ_REALNAME").toString();
			String interviewApplyId = mapRootBasic.get("TZ_MSSQH").toString();
			
			mapRet.put("classId", classId);
			mapRet.put("applyBatchId", applyBatchId);
			mapRet.put("applyBatchName", applyBatchName);
			mapRet.put("bmbId", bmbId);
			mapRet.put("bmbUrl", bmbUrl);
			mapRet.put("name", name);
			mapRet.put("interviewApplyId", interviewApplyId);
			
			/*成绩项*/
			ArrayList<Map<String, Object>> scoreItemJson = new ArrayList<Map<String,Object>>();
			
			String jgId = mapRootBasic.get("TZ_JG_ID").toString();
			String scoreModelId = mapRootBasic.get("TZ_ZLPS_SCOR_MD_ID").toString();
			String scoreTree = mapRootBasic.get("TREE_NAME").toString();
			
			String sqlScore = "A.TREE_NODE,B.DESCR,B.TZ_SCORE_ITEM_TYPE,B.TZ_SCORE_LIMITED,B.TZ_SCORE_LIMITED2,B.TZ_SCORE_PY_ZSLIM,B.TZ_SCORE_PY_ZSLIM0,B.TZ_SCORE_ITEM_DFSM,B.TZ_SCORE_ITEM_CKWT,B.TZ_SCORE_CKZL";
			sqlScore = sqlScore + " FROM PSTREENODE A,PS_TZ_MODAL_DT_TBL B";
			sqlScore = sqlScore + " WHERE A.TREE_NAME=B.TREE_NAME AND A.TREE_NODE=B.TZ_SCORE_ITEM_ID AND A.TREE_NAME=?";
			sqlScore = sqlScore + " ORDER BY A.TREE_NODE_NUM,A.TREE_NODE_NUM_END DESC";

			List<Map<String,Object>> scoreList = jdbcTemplate.queryForList(sqlScore,new Object[]{scoreTree});

			for (Map<String, Object>mapScore : scoreList) {
				
				Map<String, Object>mapScoreJson = new HashMap<String,Object>();
				
				String scoreItemId = mapScore.get("TZ_SCORE_ITEM_ID").toString();
				String scoreItemName = mapScore.get("DESCR").toString();
				String scoreItemType = mapScore.get("TZ_SCORE_ITEM_TYPE").toString();
				String scoreItemValueUpper = mapScore.get("TZ_SCORE_LIMITED").toString();
				String scoreItemValueLower = mapScore.get("TZ_SCORE_LIMITED2").toString();
				String scoreItemValue;
				String scoreItemCommentUpper = mapScore.get("TZ_SCORE_PY_ZSLIM").toString();
				String scoreItemCommentLower = mapScore.get("TZ_SCORE_PY_ZSLIM0").toString();
				String scoreItemComment;
				String scoreItemDfsm = mapScore.get("TZ_SCORE_ITEM_DFSM").toString();
				String scoreItemCkwt = mapScore.get("TZ_SCORE_ITEM_CKWT").toString();
				String scoreItemCkzl = mapScore.get("TZ_SCORE_CKZL").toString();
				
				/*查询成绩项分值和评语值*/
				String sqlScoreValue = "SELECT TZ_SCORE_NUM, TZ_SCORE_PY_VALUE FROM PS_TZ_CJX_TBL WHERE TZ_SCORE_INS_ID=? AND TZ_SCORE_ITEM_ID=?";
				Map<String, Object> mapScoreValue = jdbcTemplate.queryForMap(sqlScoreValue,new Object[] {scoreInsId,scoreItemId});
				scoreItemValue = mapScoreValue.get("TZ_SCORE_NUM").toString();
				scoreItemComment = mapScoreValue.get("TZ_SCORE_PY_VALUE").toString();

				/*如果成绩项类型为“D-下拉框”，则需要去下拉框值*/
				ArrayList<Map<String, Object>> optionListJson = new ArrayList<Map<String,Object>>();
				
				if(scoreItemType == "D") {
					String optionSql = "SELECT TZ_CJX_XLK_XXBH,TZ_CJX_XLK_XXMC,TZ_CJX_XLK_XXFZ,TZ_CJX_XLK_MRZ";
					optionSql = optionSql + " FROM PS_TZ_ZJCJXXZX_T";
					optionSql = optionSql + " WHERE TZ_JG_ID=? AND TREE_NAME=? AND TZ_SCORE_ITEM_ID=?";
					
					List<Map<String, Object>> optionList = jdbcTemplate.queryForList(optionSql,new Object[]{jgId,scoreTree,scoreItemId});
					
					for(Map<String, Object> mapOption : optionList) {
						Map<String, Object> mapOptionJson = new HashMap<String,Object>();
						
						String scoreItemOptionId = mapOption.get("TZ_CJX_XLK_XXBH").toString();
						String scoreItemOptionName = mapOption.get("TZ_CJX_XLK_XXMC").toString();
						String scoreItemOptionValue = mapOption.get("TZ_CJX_XLK_XXFZ").toString();
						String scoreItemOptionDefault = mapOption.get("TZ_CJX_XLK_MRZ").toString();
						
						mapOptionJson.put("itemId", scoreItemId);
						mapOptionJson.put("itemOptionId", scoreItemOptionId);
						mapOptionJson.put("itemOptionName", scoreItemOptionName);
						mapOptionJson.put("itemOptionValue", scoreItemOptionValue);
						mapOptionJson.put("itemOptionDefault", scoreItemOptionDefault);
						
						optionListJson.add(mapOptionJson);
					}
				}
				
				mapScoreJson.put("itemId", scoreItemId);
				mapScoreJson.put("itemName", scoreItemName);
				mapScoreJson.put("itemType", scoreItemType);
				mapScoreJson.put("itemUpperLimit", scoreItemValueUpper);
				mapScoreJson.put("itemLowerLimit", scoreItemValueLower);
				mapScoreJson.put("itemValue", scoreItemValue);
				mapScoreJson.put("itemCommentUpperLimit", scoreItemCommentUpper);
				mapScoreJson.put("itemCommentLowerLimit ", scoreItemCommentLower);
				mapScoreJson.put("itemComment", scoreItemComment);
				mapScoreJson.put("itemOptions", optionListJson);
				mapScoreJson.put("itemDfsm", scoreItemDfsm);
				mapScoreJson.put("itemCkwt", scoreItemCkwt);
				mapScoreJson.put("itemCkzl", scoreItemCkzl);
				
				scoreItemJson.add(mapScoreJson);
			}
			
			mapRet.put("scoreContent", scoreItemJson);
			
			/*考生列表*/
			ArrayList<Map<String, Object>> examineeJson = new ArrayList<Map<String,Object>>();
			
			String examineeSql = "SELECT A.TZ_APP_INS_ID,B.OPRID,C.TZ_REALNAME,C.TZ_MSSQH,A.TZ_KSH_PSPM,A.TZ_SCORE_INS_ID";
			examineeSql = examineeSql + " FROM PS_TZ_REG_USER_T C,PS_TZ_FORM_WRK_T B,PS_TZ_CP_PW_KS_TBL A";
			examineeSql = examineeSql + " WHERE A.TZ_APP_INS_ID=B.TZ_APP_INS_ID AND B.OPRID = C.OPRID AND A.TZ_CLASS_ID=? AND A.TZ_APPLY_PC_ID=? AND A.TZ_PWEI_OPRID=?"; 
			
			List<Map<String, Object>> examineeList = jdbcTemplate.queryForList(examineeSql,new Object[]{classId,applyBatchId,oprid});
			
			for(Map<String, Object> mapExaminee : examineeList) {
				Map<String, Object> examineeListJson = new HashMap<String, Object>();
				
				String examineeBmbId = mapExaminee.get("TZ_APP_INS_ID").toString();
				String examineeName = mapExaminee.get("TZ_REALNAME").toString();
				String examineeRank = mapExaminee.get("TZ_KSH_PSPM").toString();
				String examineeTotalScore;
				String examineeInterviewId = mapExaminee.get("TZ_MSSQH").toString();
				
				//查询总分，第一个成绩项的分数
				String totalScoreSql = "SELECT B.TZ_SCORE_NUM FROM PS_TZ_CJ_BPH_TBL A,PS_TZ_CJX_TBL B";
				totalScoreSql = totalScoreSql + " WHERE A.TZ_SCORE_ITEM_ID=B.TZ_SCORE_ITEM_ID AND B.TZ_SCORE_INS_ID=? AND A.TZ_JG_ID=? AND A.TZ_SCORE_MODAL_ID=?";
				totalScoreSql = totalScoreSql + " ORDER BY A.TZ_PX";
				examineeTotalScore = jdbcTemplate.queryForObject(totalScoreSql, new Object[]{scoreInsId,jgId,scoreModelId},"String");
				
				examineeListJson.put("examineeBmbId", examineeBmbId);
				examineeListJson.put("examineeName", examineeName);
				examineeListJson.put("examineeRank", examineeRank);
				examineeListJson.put("examineeTotalScore", examineeTotalScore);
				examineeListJson.put("examineeInterviewId", examineeInterviewId);
				
				examineeJson.add(examineeListJson);
			}
			
			mapRet.put("examineeList", examineeJson);
			
		} catch(Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
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
			
			jacksonUtil.parseJson2Map(formData);
			
			String result = "";
			String resultMsg = "";
			
			String sql;
			
			sql = "SELECT A.TZ_SCORE_ITEM_ID,A.DESCR,A.TZ_SCORE_ITEM_TYPE,A.TZ_SCORE_LIMITED,A.TZ_SCORE_LIMITED2,A.TZ_SCORE_PY_ZSLIM,A.TZ_SCORE_PY_ZSLIM0,A.TZ_M_FBDZ_MX_SX_JX,A.TZ_M_FBDZ_MX_XX_JX";
			sql = sql + " FROM PS_TZ_MODAL_DT_TBL A,PS_TZ_RS_MODAL_TBL B,PS_TZ_CLASS_INF_T C";
			sql = sql + " WHERE B.TZ_JG_ID=C.TZ_JG_ID AND B.TZ_SCORE_MODAL_ID=C.TZ_ZLPS_SCOR_MD_ID AND A.TREE_NAME=B.TREE_NAME AND C.TZ_CLASS_ID=?";

			List<Map<String, Object>> scoreList = jdbcTemplate.queryForList(sql,new Object[]{classId});
			for(Map<String, Object> mapScore : scoreList) {
								
				String scoreItemId = mapScore.get("TZ_SCORE_ITEM_ID").toString();
				String scoreItemName = mapScore.get("DESCR").toString();
				String scoreItemType = mapScore.get("TZ_SCORE_ITEM_TYPE").toString();
				Double scoreItemValueUpper = Double.valueOf(mapScore.get("TZ_SCORE_LIMITED").toString());
				Double scoreItemValueLower = Double.valueOf(mapScore.get("TZ_SCORE_LIMITED2").toString());
				Integer scoreItemCommentUpper = Integer.valueOf(mapScore.get("TZ_SCORE_PY_ZSLIM").toString());
				Integer scoreItemCommentLower = Integer.valueOf(mapScore.get("TZ_SCORE_PY_ZSLIM0").toString());
				String scoreItemUpperOperate = mapScore.get("TZ_M_FBDZ_MX_SX_JX").toString();
				String scoreItemLowerOperate = mapScore.get("TZ_M_FBDZ_MX_XX_JX").toString();
				
				String scoreItemValue = jacksonUtil.getString(scoreItemId);
				String scoreValid1 = "";
				String scoreValid2 = "";
				Double scoreItemValueD;
				
				//判断成绩及评语有效性
				if(scoreItemType=="B") {
					//数字成绩录入项
					scoreItemValueD = Double.valueOf(scoreItemValue);
					if(scoreItemUpperOperate=="<") {
						if(scoreItemLowerOperate==">") {
							if(scoreItemValueD>scoreItemValueLower && scoreItemValueD<scoreItemValueUpper) {
								scoreValid1="Y";
							}
						} else {
							if(scoreItemLowerOperate==">=") {
								if(scoreItemValueD>=scoreItemValueLower && scoreItemValueD<scoreItemValueUpper) {
									scoreValid1="Y";
								}
							}
						}
					} else {
						if(scoreItemUpperOperate=="<=") {
							if(scoreItemLowerOperate==">") {
								if(scoreItemValueD>scoreItemValueLower && scoreItemValueD<=scoreItemValueUpper) {
									scoreValid1="Y";
								}
							} else {
								if(scoreItemLowerOperate==">=") {
									if(scoreItemValueD>=scoreItemValueLower && scoreItemValueD<=scoreItemValueUpper) {
										scoreValid1="Y";
									}
								}
							}
						}
					}
				} else {
					if(scoreItemType=="C") {
						//评语
						if(scoreItemValue.length()>scoreItemCommentLower && scoreItemValue.length()<scoreItemCommentUpper) {
							scoreValid2="Y";
						}
					} 
				}
				
				if(scoreValid1=="Y") {
					//成绩校验成功
				} else {
					//成绩校验失败
					resultMsg = resultMsg + "【" + scoreItemName + "】分数填写错误，请重新填写！<br>";
				}
				
				if(scoreValid2=="Y") {
					//评语校验成功
				} else {
					//评语校验失败
					resultMsg = resultMsg + "【" + scoreItemName + "】评语超出指定字数，请重新填写！<br>";
				}	
			}
			
			if(resultMsg!="" && resultMsg!=null) {
				result = "1";
			} else {
				result = "0";
				//调用保存成绩项方法
				//.........................
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
				psTzKsclpslsTbl.setTzSubmitYn("Y");
				psTzKsclpslsTbl.setRowAddedDttm(new Date());
				psTzKsclpslsTbl.setRowAddedOprid(oprid);
				psTzKsclpslsTbl.setRowLastmantDttm(new Date());
				psTzKsclpslsTbl.setRowLastmantOprid(oprid);
				psTzKsclpslsTblMapper.insert(psTzKsclpslsTbl);
			} 
		} catch(Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
 		}
		
		return strRtn;
	}
	
	/**
	 * 计算排名
	 * 先校验成绩和评语的有效性，有效后调用封装方法
	 */
	public String examineeRank(String classId,String applyBatchId,String oprid,String[] errMsg) {
		String strRtn = "";
		
		try {
			String sql = "SELECT A.TZ_APP_INS_ID,A.TZ_SCORE_INS_ID,B.TZ_SCORE_MODAL_ID,C.TREE_NAME,D.TREE_NODE,E.TZ_SCORE_NUM";
			sql = sql + " FROM PSTREENODE D,PS_TZ_CP_PW_KS_TBL A ,PS_TZ_SRMBAINS_TBL B,PS_TZ_RS_MODAL_TBL C,PS_TZ_CJX_TBL E";
			sql = sql + " WHERE A.TZ_SCORE_INS_ID = B.TZ_SCORE_INS_ID AND B.TZ_SCORE_MODAL_ID=C.TZ_SCORE_MODAL_ID AND D.TREE_NAME=C.TREE_NAME AND D.PARENT_NODE_NUM=0 AND E.TZ_SCORE_INS_ID=A.TZ_APP_INS_ID AND E.TZ_SCORE_ITEM_ID=D.TREE_NODE";
			sql = sql + " AND A.TZ_CLASS_ID=? AND A.TZ_APPLY_PC_ID=? AND A.TZ_PWEI_OPRID=?";
			sql = sql + " ORDER BY E.TZ_SCORE_NUM DESC";
			
			List<Map<String, Object>> listData = jdbcTemplate.queryForList(sql,new Object[]{classId,applyBatchId,oprid});
			
			Integer rank = 0;
		
			for (Map<String, Object> mapData : listData) {

				Long bmbId = Long.valueOf(mapData.get("TZ_APP_INS_ID").toString());
				
				rank++;
				
				PsTzCpPwKsTblKey psTzCpPwKsTblKey = new PsTzCpPwKsTblKey();
				psTzCpPwKsTblKey.setTzClassId(classId);
				psTzCpPwKsTblKey.setTzApplyPcId(applyBatchId);
				psTzCpPwKsTblKey.setTzAppInsId(bmbId);
				psTzCpPwKsTblKey.setTzPweiOprid(oprid);
				
				PsTzCpPwKsTbl psTzCpPwKsTbl = psTzCpPwKsTblMapper.selectByPrimaryKey(psTzCpPwKsTblKey);
	
				if(psTzCpPwKsTbl == null) {
					
				} else {
					psTzCpPwKsTbl.setTzGuanxLeix("A");
					psTzCpPwKsTbl.setTzKshPspm(String.valueOf(rank));
					psTzCpPwKsTbl.setRowLastmantDttm(new Date());
					psTzCpPwKsTbl.setRowLastmantOprid(oprid);
					psTzCpPwKsTblMapper.updateByPrimaryKey(psTzCpPwKsTbl);
				}
			}
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
			String sql;
			
			//清除临时表数据
			sql = "DELETE FROM PS_TZ_PWKSPC_TMP_T";
			jdbcTemplate.update(sql);
			
			
			sql = "SELECT A.TZ_PWEI_OPRID,A.TZ_SCORE_INS_ID,C.TZ_SCORE_NUM";
			sql = sql + " FROM PS_TZ_CJX_TBL C,PS_TZ_CP_PW_KS_TBL A,PS_TZ_KSCLPSLS_TBL B";
			sql = sql + " WHERE A.TZ_SCORE_INS_ID=C.TZ_SCORE_INS_ID";
			sql = sql + " AND C.TZ_SCORE_ITEM_ID=(SELECT D.TREE_NODE FROM PSTREENODE D,PS_TZ_RS_MODAL_TBL E,PS_TZ_CLASS_INF_T FWHERE D.TREE_NAME=E.TREE_NAME AND E.TZ_SCORE_MODAL_ID=F.TZ_ZLPS_SCOR_MD_ID AND F.TZ_CLASS_ID=A.TZ_CLASS_ID AND D.PARENT_NODE_NUM=0) ";
			sql = sql + " AND B.TZ_CLASS_ID=A.TZ_CLASS_ID AND B.TZ_APPLY_PC_ID=A.TZ_APPLY_PC_ID AND B.TZ_APP_INS_ID=A.TZ_APP_INS_ID AND B.TZ_PWEI_OPRID=A.TZ_PWEI_OPRID";
			sql = sql + " AND B.TZ_CLPS_LUNC=? AND B.TZ_SUBMIT_YN<>'C' AND A.TZ_CLASS_ID=? AND A.TZ_APPLY_PC_ID=? AND A.TZ_APP_INS_ID=?";
			
			List<Map<String,Object>> scoreList = jdbcTemplate.queryForList(sql,new Object[]{dqpyLunc,classId,applyBatchId,bmbId});
			
			Double score = 0.0;
			Integer i = 0;
			
			for(Map<String, Object> mapScore : scoreList) {
				i++;
				score = Double.valueOf(mapScore.get("TZ_SCORE_NUM").toString());
				
				PsTzPwkspcTmpT psTzPwkspcTmpT = new PsTzPwkspcTmpT();
				psTzPwkspcTmpT.setTzId(i);
				psTzPwkspcTmpT.setTzScoreNum(BigDecimal.valueOf(score));
				psTzPwkspcTmpTMapper.insert(psTzPwkspcTmpT);
			}
			
			//计算偏差
			sql = "SELECT STDDEV(TZ_SCORE_NUM) FROM PS_TZ_PWKSPC_TMP_T";
			Double pcValue = Double.valueOf(jdbcTemplate.queryForObject(sql,"String"));
			
			
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
				psTzClpsKshTblMapper.updateByPrimaryKey(psTzClpsKshTbl);
			}
			
			
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
			
			List<Map<String, Object>> scoreList = jdbcTemplate.queryForList(sql,new Object[]{classId,applyBatchId,bmbId});
			
			Integer count = 0;
			Double score;
			Double sum = 0.00;
			
			for(Map<String, Object> mapScore : scoreList) {
				count++;
				score = Double.valueOf(mapScore.get("TZ_SCORE_NUM").toString());
				
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
			String sql;
			
			//是否存在没有评委组的评委
			sql = "SELECT 'Y' FROM PS_TZ_CLPS_PW_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_PWEI_ZHZT='A' AND TZ_PWZBH=' '";
			String noPwzFlag = jdbcTemplate.queryForObject(sql, new Object[]{classId,applyBatchId},"String");

			if(noPwzFlag=="Y") {
				bmbIdNext = "";
				messageCode = "1";
				message = "存在没有被分组的评委";
			} else {
				sql = "SELECT A.TZ_PYKS_XX,A.TZ_PWEI_ZHZT,A.TZ_PWZBH,B.TZ_DQPY_ZT,B.TZ_DQPY_LUNC,B.TZ_MSPY_NUM,C.TZ_SUBMIT_YN";
				sql = sql + ",(SELECT COUNT(1) FROM PS_TZ_CP_PW_KS_TBL E,PS_TZ_CLPS_KSH_TBL F";
				sql = sql + " WHERE E.TZ_CLASS_ID=F.TZ_CLASS_ID AND E.TZ_APPLY_PC_ID=F.TZ_APPLY_PC_ID AND E.TZ_APP_INS_ID=F.TZ_APP_INS_ID AND E.TZ_CLASS_ID=A.TZ_CLASS_ID AND E.TZ_APPLY_PC_ID=A.TZ_APPLY_PC_ID AND E.TZ_PWEI_OPRID=A.TZ_PWEI_OPRID) TZ_PWKS_NUM";
				sql = sql + ",(SELECT COUNT(distinct G.TZ_APP_INS_ID ) FROM PS_TZ_CP_PW_KS_TBL E,PS_TZ_KSCLPSLS_TBL G";
				sql = sql + " WHERE E.TZ_CLASS_ID=G.TZ_CLASS_ID AND E.TZ_APPLY_PC_ID=G.TZ_APPLY_PC_ID AND E.TZ_APP_INS_ID=G.TZ_APP_INS_ID AND E.TZ_CLASS_ID=A.TZ_CLASS_ID AND E.TZ_APPLY_PC_ID=A.TZ_APPLY_PC_ID AND G.TZ_PWEI_OPRID=A.TZ_PWEI_OPRID AND G.TZ_CLPS_LUNC=B.TZ_DQPY_LUNC AND G.TZ_SUBMIT_YN='Y') TZ_PWKS_SUB_NUM";
				sql = sql + " FROM PS_TZ_CLPWPSLS_TBL C,PS_TZ_CLPS_PW_TBL A ,PS_TZ_CLPS_GZ_TBL B";
				sql = sql + " WHERE A.TZ_CLASS_ID=B.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID=B.TZ_APPLY_PC_ID AND C.TZ_CLASS_ID=A.TZ_CLASS_ID AND C.TZ_APPLY_PC_ID=A.TZ_APPLY_PC_ID AND C.TZ_PWEI_OPRID=A.TZ_PWEI_OPRID AND C.TZ_CLPS_LUNC=B.TZ_DQPY_LUNC";
				sql = sql + " AND A.TZ_CLASS_ID=? AND A.TZ_APPLY_PC_ID=? AND A.TZ_PWEI_OPRID=?";

				Map<String, Object> mapData = jdbcTemplate.queryForMap(sql,new Object[]{classId,applyBatchId,oprid});

				Integer pyksNum = Integer.valueOf(mapData.get("TZ_PYKS_XX").toString());
				String pweiZhzt = mapData.get("TZ_PWEI_ZHZT").toString();
				String pwzbh = mapData.get("TZ_PWZBH").toString();
				String dqpyZt = mapData.get("TZ_DQPY_ZT").toString();
				Short dqpyLunc = Short.valueOf(mapData.get("TZ_DQPY_LUNC").toString());
				Integer mspyNum = Integer.valueOf(mapData.get("TZ_MSPY_NUM").toString());
				String submitAllFlag = mapData.get("TZ_SUBMIT_YN").toString();
				Integer pwksNum = Integer.valueOf(mapData.get("TZ_PWKS_NUM").toString());
				Integer pwksSubNum= Integer.valueOf(mapData.get("TZ_PWKS_SUB_NUM").toString());
				
				if(submitAllFlag=="Y") {
					bmbIdNext = "";
					messageCode = "1";
					message = "该轮次已经全部提交了,无法得到下一个考生";
				} else {
					if(dqpyZt=="A") {
						if(pweiZhzt=="A") {
							if(pwksNum>pwksSubNum) {
								bmbIdNext = "";
								messageCode = "1";
								message = "评委有“打分后未提交”的考生";
							} else {
								if(pwksNum>=pyksNum) {
									bmbIdNext = "";
									messageCode = "1";
									message = "该评委已经达到了评审的上限";
								} else {
									sql = "SELECT A.TZ_APP_INS_ID,ROUND(RAND()) SJS FROM PS_TZ_CLPS_KSH_TBL A";
									sql = sql + " WHERE NOT EXISTS (SELECT 'Y' FROM (SELECT M.TZ_CLASS_ID,M.TZ_APPLY_PC_ID,M.TZ_APP_INS_ID";
									sql = sql + " FROM (SELECT C.TZ_CLASS_ID,C.TZ_APPLY_PC_ID,C.TZ_APP_INS_ID,COUNT(1) ZDPWS FROM PS_TZ_CP_PW_KS_TBL B,PS_TZ_CLPS_KSH_TBL C";
									sql = sql + " WHERE C.TZ_CLASS_ID=B.TZ_CLASS_ID AND C.TZ_APPLY_PC_ID=B.TZ_APPLY_PC_ID AND C.TZ_APP_INS_ID=B.TZ_APP_INS_ID GROUP BY C.TZ_CLASS_ID,C.TZ_APPLY_PC_ID,C.TZ_APP_INS_ID) M WHERE M.ZDPWS>=?) X";
									sql = sql + " WHERE X.TZ_CLASS_ID=A.TZ_CLASS_ID AND X.TZ_APPLY_PC_ID=A.TZ_APPLY_PC_ID AND X.TZ_APP_INS_ID=A.TZ_APP_INS_ID)";
									sql = sql + " AND NOT EXISTS (SELECT 'Y' FROM (SELECT D.TZ_CLASS_ID,D.TZ_APPLY_PC_ID,D.TZ_APP_INS_ID FROM PS_TZ_CP_PW_KS_TBL D,PS_TZ_CLPS_PW_TBL E,PS_TZ_CLPS_KSH_TBL F";
									sql = sql + " WHERE D.TZ_CLASS_ID=F.TZ_CLASS_ID AND D.TZ_APPLY_PC_ID=F.TZ_APPLY_PC_ID AND D.TZ_APP_INS_ID=F.TZ_APP_INS_ID AND D.TZ_CLASS_ID=E.TZ_CLASS_ID AND D.TZ_APPLY_PC_ID=F.TZ_APPLY_PC_ID AND D.TZ_PWEI_OPRID=E.TZ_PWEI_OPRID AND E.TZ_PWZBH=?)) Y";
									sql = sql + " WHERE Y.TZ_CLASS_ID=A.TZ_CLASS_ID AND Y.TZ_APPLY_PC_ID=A.TZ_APPLY_PC_ID AND Y.TZ_APP_INS_ID=A.TZ_APP_INS_ID)";
									sql = sql + " AND A.TZ_CLASS_ID=? AND A.TZ_APPLY_PC_ID=? ORDER BY SJS";
									
									Map<String, Object> mapNext = jdbcTemplate.queryForMap(sql,new Object[]{mspyNum,pwzbh,classId,applyBatchId});
									Long tzAppInsId = Long.valueOf(mapNext.get("TZ_APP_INS_ID").toString());
									
									if(tzAppInsId>0) {
										//锁表PS_TZ_CLPS_KSH_TBL
										mySqlLockService.lockRow(jdbcTemplate, "TZ_CLPS_KSH_TBL");
										
										//再次查看有没有超过上限
										sql = "SELECT COUNT(1) FROM PS_TZ_CP_PW_KS_TBL A,PS_TZ_CLPS_KSH_TBL B WHERE A.TZ_CLASS_ID=B.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID=B.TZ_APPLY_PC_ID AND A.TZ_APP_INS_ID=B.TZ_APP_INS_ID AND A.TZ_CLASS_ID=? AND A.TZ_APPLY_PC_ID=? AND A.TZ_APP_INS_ID=?";
										Integer kspwNum = jdbcTemplate.queryForObject(sql, new Object[]{classId,applyBatchId,tzAppInsId},"Integer");
										
										//再次查看有没有被自己抽取过
										sql = "SELECT 'Y' FROM PS_TZ_CP_PW_KS_TBL A,PS_TZ_CLPS_KSH_TBL B WHERE A.TZ_CLASS_ID=B.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID=B.TZ_APPLY_PC_ID AND A.TZ_APP_INS_ID=B.TZ_APP_INS_ID AND A.TZ_CLASS_ID=? AND A.TZ_APPLY_PC_ID=? AND A.TZ_APP_INS_ID=? AND A.TZ_PWEI_OPRID=?";
										String myKsFlag = jdbcTemplate.queryForObject(sql, new Object[]{classId,applyBatchId,tzAppInsId,oprid}, "String");
										
										//再次查看有没有被同组人抽取过
										sql = "SELECT 'Y' FROM PS_TZ_CP_PW_KS_TBL A,PS_TZ_CLPS_KSH_TBL B,PS_TZ_CLPS_PW_TBL C WHERE A.TZ_CLASS_ID=B.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID=B.TZ_APPLY_PC_ID AND A.TZ_APP_INS_ID=B.TZ_APP_INS_ID AND A.TZ_CLASS_ID=C.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID=C.TZ_APPLY_PC_ID AND A.TZ_APP_INS_ID=C.TZ_PWEI_OPRID";
										sql = sql + " AND A.TZ_CLASS_ID=? AND A.TZ_APPLY_PC_ID=? AND A.TZ_APP_INS_ID=? AND C.TZ_PWZBH=?";
										String tzpwFlag = jdbcTemplate.queryForObject(sql, new Object[]{classId,applyBatchId,tzAppInsId,pwzbh}, "String");
										
										if(kspwNum>=mspyNum || myKsFlag=="Y" || tzpwFlag=="Y") {
											
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
											
											if(insertRow==1) {
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

												Map<String, Object> mapKsPwList = jdbcTemplate.queryForMap(sql,new Object[]{classId,applyBatchId,tzAppInsId});
												String ksPwList = mapKsPwList.get("TZ_PW_LIST").toString();
														
												
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
													psTzClpskspwTblMapper.updateByPrimaryKey(psTzClpskspwTbl);
												}
												
												bmbIdNext = String.valueOf(tzAppInsId);
												messageCode = "";
												message = "";
						
											} else {
												bmbIdNext = "";
												messageCode = "1";
												message = "得到下一个考生失败或则已经没有可评审的考生";
											}	
										}
										
										//解锁
										mySqlLockService.unlockRow(jdbcTemplate);
									} else {
										bmbIdNext = "";
										messageCode = "1";
										message = "同步MBA材料评审评委考生关系表失败";	
									}
											
								}
							}
						} else {
							bmbIdNext = "";
							messageCode = "1";
							message = "评委账号没有被启用";
						}
					} else {
						bmbIdNext = "";
						messageCode = "1";
						message = "当前批次的评审状态不在进行中";
					}
				}

			}
			
			mapRet.put("bmbIdNext", bmbIdNext);
			mapRet.put("messageCode", messageCode);
			mapRet.put("message", message);
			
		} catch(Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		strRtn = jacksonUtil.Map2json(mapRet);
		return strRtn;
	}
}
