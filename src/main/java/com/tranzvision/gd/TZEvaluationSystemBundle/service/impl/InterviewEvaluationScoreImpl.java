package com.tranzvision.gd.TZEvaluationSystemBundle.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.poifs.storage.BATBlock;
import org.bouncycastle.math.raw.Mod;
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
				strRtn = this.getExamineeListYuan(classId,applyBatchId,errMsg);
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
				
				String scoreModalId = "",treeName = "",bmrOprid = "";
				
				sql = "SELECT A.TZ_MSCJ_SCOR_MD_ID ,B.TREE_NAME,C.OPRID FROM PS_TZ_FORM_WRK_T C,PS_TZ_RS_MODAL_TBL B,PS_TZ_CLASS_INF_T A";
				sql = sql + " WHERE A.TZ_MSCJ_SCOR_MD_ID = B.TZ_SCORE_MODAL_ID AND B.TZ_JG_ID=? AND C.TZ_APP_INS_ID=? AND A.TZ_CLASS_ID=?";
				 
				Map<String, Object> mapData = sqlQuery.queryForMap(sql, new Object[] {orgId,bmbId,classId});
				if(mapData!=null) {
					scoreModalId = mapData.get("TZ_MSCJ_SCOR_MD_ID") == null ? "" : mapData.get("TZ_MSCJ_SCOR_MD_ID").toString();
					treeName = mapData.get("TREE_NAME") == null ? "" : mapData.get("TREE_NAME").toString();
					bmrOprid = mapData.get("OPRID") == null ? "" : mapData.get("OPRID").toString();
				}
				
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
						//this.updateExamineeRank(classId, applyBatchId, oprid, bmbId);
						
						
						//更新考生评审得分历史表
						PsTzMpPwKsTblKey psTzMpPwKsTblKey = new PsTzMpPwKsTblKey();
						psTzMpPwKsTblKey.setTzClassId(classId);
						psTzMpPwKsTblKey.setTzApplyPcId(applyBatchId);
						psTzMpPwKsTblKey.setTzAppInsId(Long.valueOf(bmbId));
						psTzMpPwKsTblKey.setTzPweiOprid(oprid);
						
						PsTzMpPwKsTbl psTzMpPwKsTbl = psTzMpPwKsTblMapper.selectByPrimaryKey(psTzMpPwKsTblKey);
						if(psTzMpPwKsTbl==null) {
							
						} else {
							psTzMpPwKsTbl.setTzPshenZt("Y");
							psTzMpPwKsTbl.setRowLastmantDttm(new Date());
							psTzMpPwKsTbl.setRowLastmantOprid(oprid);
							psTzMpPwKsTblMapper.updateByPrimaryKeySelective(psTzMpPwKsTbl);
						}
						
						//更新考生评审得分历史表
						//sql = "UPDATE PS_TZ_MP_PW_KS_TBL SET TZ_PSHEN_ZT='Y',ROW_LASTMANT_DTTM=?,ROW_LASTMANT_OPRID=?";
						//sql = sql + " WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=? AND TZ_PWEI_OPRID=?";
						//sqlQuery.update(sql,new Object[]{oprid,new Date(),applyBatchId,bmbId,oprid});
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
			if("tzGetExamineeList".equals(operateType)){
				strRet = getExamineeList(strParams);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return strRet;
	}
	
	
	/**
	 * 考生列表
	 */
	public String getExamineeList(String strParams) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String,Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			
			String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			
			jacksonUtil.json2Map(strParams);
			String classId = jacksonUtil.getString("CLASSID");
			String batchId = jacksonUtil.getString("APPLY_BATCHID");
			String msGroupId = jacksonUtil.getString("MS_GROUPID");
						
			
			//面试评审成绩模型
			String TZ_MSPS_SCOR_MD_ID = sqlQuery.queryForObject("SELECT TZ_MSCJ_SCOR_MD_ID FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID=?", new Object[]{classId},"String");
			
			if("".equals(TZ_MSPS_SCOR_MD_ID)) {
				mapRet.put("error_code", "1");
				mapRet.put("error_msg", "当前报考班级没有配置成绩模型。");
				strRet = jacksonUtil.Map2json(mapRet);
				return strRet;
			}
			
			
			//成绩树
			String TREE_NAME = sqlQuery.queryForObject(
					"SELECT TREE_NAME FROM PS_TZ_RS_MODAL_TBL WHERE TZ_SCORE_MODAL_ID=? AND TZ_JG_ID=?",
					new Object[] { TZ_MSPS_SCOR_MD_ID ,orgId},"String");
			
			if(TREE_NAME==null || "".equals(TREE_NAME)) {
				mapRet.put("error_code", "1");
				mapRet.put("error_msg", "报考班级对应的成绩模型没有配置对应的成绩树。");
				strRet = jacksonUtil.Map2json(mapRet);
				return strRet;
			}
			
			/*评委类型：英语A或其他O*/
			String pwType = sqlQuery.queryForObject("SELECT TZ_PWEI_TYPE FROM PS_TZ_MSPS_PW_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_PWEI_OPRID=?", new Object[] {classId,batchId,oprid},"String");
			
			//成绩模型中英语成绩项ID
			String englishCjxId = getHardCodePoint.getHardCodePointVal("TZ_MSPS_YYNL_CJX_ID");
			
			
			Map<String,Object> dyColThMap= new HashMap<String,Object>();
			List<Map<String,Object>> dyRowValue= new ArrayList<Map<String,Object>>();
			
			//考生列表需要显示的成绩项列名称
			int dyColNum2 = 0;
			
			//String cjxNameSql = "SELECT TZ_SCORE_ITEM_ID,TZ_XS_MC FROM PS_TZ_CJ_BPH_TBL WHERE TZ_SCORE_MODAL_ID=? AND TZ_JG_ID=? AND TZ_ITEM_S_TYPE='A' ORDER BY TZ_PX DESC";
			//List<Map<String, Object>> listHeader = sqlQuery.queryForList(cjxNameSql,new Object[]{TZ_MSPS_SCOR_MD_ID,orgId});
			String cjxNameSql = "SELECT A.TREE_NODE, B.DESCR,B.TZ_SCORE_ITEM_TYPE";
			cjxNameSql += " FROM PSTREENODE A,PS_TZ_MODAL_DT_TBL B";
			cjxNameSql += " WHERE A.TREE_NAME=B.TREE_NAME AND A.TREE_NODE=B.TZ_SCORE_ITEM_ID AND A.TREE_NAME=? AND A.TREE_LEVEL_NUM=2";
			cjxNameSql += "	ORDER BY A.TREE_NODE_NUM,A.TREE_NODE_NUM_END DESC" ;
			List<Map<String, Object>> listHeader = sqlQuery.queryForList(cjxNameSql, new Object[] { TREE_NAME});
			for(Map<String, Object> mapHeader : listHeader) {
				String TREE_NODE = mapHeader.get("TREE_NODE") == null ? "" : mapHeader.get("TREE_NODE").toString();
				String TZ_XS_MC = mapHeader.get("DESCR") == null ? "" : mapHeader.get("DESCR").toString();
				
				Boolean boolShow = true;
				if(englishCjxId.equals(TREE_NODE)) {
					//英语成绩项
					if("A".equals(pwType)) {
						
					} else {
						boolShow = false;
					}
				} 
				
				if(boolShow) {
					dyColNum2 = dyColNum2 + 1;
					String strDyColNum2 = "0" + dyColNum2;
					dyColThMap.put("col"+strDyColNum2.substring(strDyColNum2.length() - 2), TZ_XS_MC);
				}
			}
			
			
			//考生信息
			String ksListSql = tzSQLObject.getSQLText("SQL.TZEvaluationSystemBundle.TzInterviewExamineeListByGroup");
			List<Map<String, Object>> listValue = sqlQuery.queryForList(ksListSql, new Object[]{msGroupId,classId,batchId,oprid});									
			for(Map<String, Object> mapValue : listValue) {
				Map<String, Object> dyRowValueItem = new HashMap<String, Object>();
				
				String appInsId = mapValue.get("TZ_APP_INS_ID") == null ? "" : mapValue.get("TZ_APP_INS_ID").toString();
				String scoreInsId = mapValue.get("TZ_SCORE_INS_ID") == null ? "" : mapValue.get("TZ_SCORE_INS_ID").toString();
				String groupId = mapValue.get("TZ_GROUP_ID") == null ? "" : mapValue.get("TZ_GROUP_ID").toString();
				String groupName = mapValue.get("TZ_GROUP_NAME") == null ? "" : mapValue.get("TZ_GROUP_NAME").toString();
				String order = mapValue.get("TZ_ORDER") == null ? "0" : mapValue.get("TZ_ORDER").toString();
				String mssqh = mapValue.get("TZ_MSH_ID") == null ? "" : mapValue.get("TZ_MSH_ID").toString();
				String realname = mapValue.get("TZ_REALNAME") == null ? "" : mapValue.get("TZ_REALNAME").toString();
				String genderDesc = mapValue.get("TZ_GENDER_DESC") == null ? "" : mapValue.get("TZ_GENDER_DESC").toString();
				
				String xuHao = groupName + "-" + order;
				
				dyRowValueItem.put("appInsId", appInsId);
				dyRowValueItem.put("scoreInsId", scoreInsId);
				dyRowValueItem.put("xuHao", xuHao);
				dyRowValueItem.put("mssqh", mssqh);
				dyRowValueItem.put("realname", realname);
				dyRowValueItem.put("genderDesc", genderDesc);
				
				//动态列-成绩项取值				
				String TZ_SCORE_ITEM_ID = "";
				int dyColNum = 0;
				if(listHeader!=null) {
					for (int i = 0; i < listHeader.size(); i++) {
						//TZ_SCORE_ITEM_ID = (String) listHeader.get(i).get("TZ_SCORE_ITEM_ID");
						TZ_SCORE_ITEM_ID = (String) listHeader.get(i).get("TREE_NODE");

						String TZ_SCORE_ITEM_TYPE = "",TZ_SCR_TO_SCORE = "";
						Map<String, Object> mapCjxType= sqlQuery.queryForMap(
								"SELECT TZ_SCORE_ITEM_TYPE,TZ_SCR_TO_SCORE FROM PS_TZ_MODAL_DT_TBL WHERE TREE_NAME=? AND TZ_SCORE_ITEM_ID=?",
								new Object[] { TREE_NAME, TZ_SCORE_ITEM_ID });
						if(mapCjxType!=null){
							TZ_SCORE_ITEM_TYPE = (String) mapCjxType.get("TZ_SCORE_ITEM_TYPE");
							TZ_SCR_TO_SCORE = (String) mapCjxType.get("TZ_SCR_TO_SCORE");
						}
						
						Boolean boolShowValue = true;
						if(englishCjxId.equals(TZ_SCORE_ITEM_ID)) {
							//英语成绩项
							if("A".equals(pwType)) {
								
							} else {
								boolShowValue = false;
							}
						} 
						
						if(boolShowValue) {
						
							dyColNum = dyColNum + 1;
							
							// 得到分值;
							double TZ_SCORE_NUM = 0;
							String TZ_SCORE_PY_VALUE = "",TZ_CJX_XLK_XXBH = "";
							
							Map<String,Object> mapCjxValue = sqlQuery.queryForMap("SELECT TZ_SCORE_NUM,TZ_SCORE_PY_VALUE,TZ_CJX_XLK_XXBH FROM PS_TZ_CJX_TBL WHERE TZ_SCORE_INS_ID=? AND TZ_SCORE_ITEM_ID=?",new Object[]{scoreInsId,TZ_SCORE_ITEM_ID});
							if(mapCjxValue!=null) {
								Object OBJ_TZ_SCORE_NUM = mapCjxValue.get("TZ_SCORE_NUM");
								TZ_SCORE_NUM = OBJ_TZ_SCORE_NUM!=null?((BigDecimal)OBJ_TZ_SCORE_NUM).doubleValue():0;
								TZ_SCORE_PY_VALUE = mapCjxValue.get("TZ_SCORE_PY_VALUE") == null ? "" : mapCjxValue.get("TZ_SCORE_PY_VALUE").toString();
								TZ_CJX_XLK_XXBH = mapCjxValue.get("TZ_CJX_XLK_XXBH") == null ? "" : mapCjxValue.get("TZ_CJX_XLK_XXBH").toString();
							}
						
							String strDyColNum = "0" + dyColNum;
							strDyColNum = strDyColNum.substring(strDyColNum.length() - 2);
						
							/*成绩项类型：A-数字成绩汇总项,B-数字成绩录入项,C-评语,D-下拉框*/
							if ("A".equals(TZ_SCORE_ITEM_TYPE) || "B".equals(TZ_SCORE_ITEM_TYPE)
									||("D".equals(TZ_SCORE_ITEM_TYPE)&&"Y".equals(TZ_SCR_TO_SCORE))) {
								
								//成绩录入项、成绩汇总项、下拉框且转换为分值   取分数，否则取评语;
						        dyRowValueItem.put("col"+strDyColNum,String.valueOf(TZ_SCORE_NUM));
						        
							} else {
								if ("D".equals(TZ_SCORE_ITEM_TYPE)) {
									// 如果是下拉框，需要取得下拉框名称，此处存的是下拉框编号;
									String str_xlk_desc = sqlQuery.queryForObject(
											"select TZ_CJX_XLK_XXMC from PS_TZ_ZJCJXXZX_T where TREE_NAME=? and TZ_SCORE_ITEM_ID=? and TZ_CJX_XLK_XXBH=?",
											new Object[] { TREE_NAME, TZ_SCORE_ITEM_ID, TZ_CJX_XLK_XXBH },
											"String");
	
									TZ_SCORE_PY_VALUE = str_xlk_desc;
								}
						        dyRowValueItem.put("col"+strDyColNum,TZ_SCORE_PY_VALUE);
							}
						}
					}
				}				
				
				
				//其他评委打分情况
				/*
				List<Map<String, Object>> otherPwList = new ArrayList<Map<String,Object>>();
				
		        String otherPwSql = "SELECT  A.TZ_PWEI_OPRID,B.TZ_DLZH_ID,B.TZ_REALNAME,A.TZ_SCORE_INS_ID,C.TZ_PWEI_TYPE";
		        otherPwSql += " FROM PS_TZ_AQ_YHXX_TBL B,PS_TZ_MSPS_PW_TBL C,PS_TZ_MP_PW_KS_TBL A";
		        otherPwSql += " WHERE A.TZ_PWEI_OPRID=B.OPRID AND A.TZ_CLASS_ID=C.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID=C.TZ_APPLY_PC_ID AND A.TZ_PWEI_OPRID=C.TZ_PWEI_OPRID";
		        otherPwSql += " AND C.TZ_PWEI_ZHZT<>'B' AND A.TZ_CLASS_ID=? AND A.TZ_APPLY_PC_ID=? AND A.TZ_APP_INS_ID=? AND A.TZ_PWEI_OPRID<>? AND A.TZ_DELETE_ZT<>'Y'";
		        
		        List<Map<String, Object>> listOtherPw = sqlQuery.queryForList(otherPwSql, new Object[]{classId,batchId,appInsId,oprid});
		        for(Map<String, Object> mapOtherPw : listOtherPw) {
		        	
					Map<String, Object> otherPwItem = new HashMap<String,Object>();
		        	
		        	String otherPwOprid = mapOtherPw.get("TZ_PWEI_OPRID") == null ? "" : mapOtherPw.get("TZ_PWEI_OPRID").toString();
		        	String otherPwDlzhId = mapOtherPw.get("TZ_DLZH_ID") == null ? "" : mapOtherPw.get("TZ_DLZH_ID").toString();
		        	String otherPwName = mapOtherPw.get("TZ_REALNAME") == null ? "" : mapOtherPw.get("TZ_REALNAME").toString();
		        	String otherPwScoreInsId = mapOtherPw.get("TZ_SCORE_INS_ID") == null ? "" : mapOtherPw.get("TZ_SCORE_INS_ID").toString();
		        	String otherPwType = mapOtherPw.get("TZ_PWEI_TYPE") == null ? "" : mapOtherPw.get("TZ_PWEI_TYPE").toString();
		        
		        	String otherPwTypeDesc = "其他评委";
		        	if("A".equals(otherPwType)) {
		        		otherPwTypeDesc = "英语评委";
		        	}
		        	
		        	otherPwItem.put("otherPwDlzhId", otherPwDlzhId);
		        	otherPwItem.put("otherPwName", otherPwName);
		        	otherPwItem.put("otherPwTypeDesc", otherPwTypeDesc);
		        	
		        	
		        	int dyColNumOther = 0;
		        	if(listHeader!=null) {
						for (int i = 0; i < listHeader.size(); i++) {
							//String TZ_SCORE_ITEM_ID_OTH = (String) listHeader.get(i).get("TZ_SCORE_ITEM_ID");
							String TZ_SCORE_ITEM_ID_OTH = (String) listHeader.get(i).get("TREE_NODE");

							// 判断成绩项的类型，下拉框转换为分值;
							String TZ_SCORE_ITEM_TYPE_OTH = "",TZ_SCR_TO_SCORE_OTH = "";
							Map<String, Object> mapOther= sqlQuery.queryForMap(
									"select TZ_SCORE_ITEM_TYPE,TZ_SCR_TO_SCORE from PS_TZ_MODAL_DT_TBL where TREE_NAME=? and TZ_SCORE_ITEM_ID=?",
									new Object[] { TREE_NAME, TZ_SCORE_ITEM_ID_OTH });
							if(mapOther!=null){
								TZ_SCORE_ITEM_TYPE_OTH = (String)mapOther.get("TZ_SCORE_ITEM_TYPE");
								TZ_SCR_TO_SCORE_OTH = (String)mapOther.get("TZ_SCR_TO_SCORE");
							}
							
							dyColNumOther = dyColNumOther + 1;

							// 得到分值;
							double TZ_SCORE_NUM_OTH = 0;
							String TZ_SCORE_PY_VALUE_OTH = "";

							Map<String, Object> mapOtherValue = sqlQuery.queryForMap(
									"select TZ_SCORE_NUM,TZ_SCORE_PY_VALUE from PS_TZ_CJX_TBL where TZ_SCORE_INS_ID=? and TZ_SCORE_ITEM_ID=?",
									new Object[] { otherPwScoreInsId, TZ_SCORE_ITEM_ID_OTH});
							if (mapOtherValue != null) {
								Object OBJ_TZ_SCORE_NUM_OTH = mapOtherValue.get("TZ_SCORE_NUM");
								TZ_SCORE_NUM_OTH = OBJ_TZ_SCORE_NUM_OTH!=null?((BigDecimal)OBJ_TZ_SCORE_NUM_OTH).doubleValue():0;
								TZ_SCORE_PY_VALUE_OTH = (String) mapOtherValue.get("TZ_SCORE_PY_VALUE");
							}

							String strDyColNumOther = "0" + dyColNumOther;
							strDyColNumOther = strDyColNumOther.substring(strDyColNumOther.length() - 2);

							// 成绩项类型：A-数字成绩汇总项,B-数字成绩录入项,C-评语,D-下拉框
							if ("A".equals(TZ_SCORE_ITEM_TYPE_OTH) || "B".equals(TZ_SCORE_ITEM_TYPE_OTH)
									||("D".equals(TZ_SCORE_ITEM_TYPE_OTH)&&"Y".equals(TZ_SCR_TO_SCORE_OTH))) {
								//成绩录入项、成绩汇总项、下拉框且转换为分值   取分数，否则取评语;
								otherPwItem.put("col"+strDyColNumOther,String.valueOf(TZ_SCORE_NUM_OTH));
							} else {
								if ("D".equals(TZ_SCORE_ITEM_TYPE_OTH)) {
									// 如果是下拉框，需要取得下拉框名称，此处存的是下拉框编号;
									String str_xlk_desc_other = sqlQuery.queryForObject(
											"select TZ_CJX_XLK_XXMC from PS_TZ_ZJCJXXZX_T where TREE_NAME=? and TZ_SCORE_ITEM_ID=? and TZ_CJX_XLK_XXBH=?",
											new Object[] { TREE_NAME, TZ_SCORE_ITEM_ID_OTH, TZ_SCORE_PY_VALUE_OTH },
											"String");

									TZ_SCORE_PY_VALUE_OTH = str_xlk_desc_other;

								}
								otherPwItem.put("col"+strDyColNumOther,TZ_SCORE_PY_VALUE_OTH);
							}
						}
		        	}
		        	
		        	otherPwList.add(otherPwItem);
		        }
		        
	        	dyRowValueItem.put("ps_other_pw", otherPwList);
	        	*/
	        	
				dyRowValue.add(dyRowValueItem);
 			}
			
			mapRet.put("ksh_list_headers", dyColThMap);
			mapRet.put("ksh_list_contents", dyRowValue);
			mapRet.put("error_code", "0");
			mapRet.put("error_msg", "");
			
			strRet = jacksonUtil.Map2json(mapRet);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return strRet;
	}
	
	
	/**
	 * 获取考生列表（原来）
	 */
	public String getExamineeListYuan(String classId,String applyBatchId,String[] errMsg) {
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
			
			String examineeSql = "SELECT A.TZ_APP_INS_ID,B.OPRID,IFNULL(E.TZ_REALNAME,C.TZ_REALNAME) TZ_REALNAME,C.TZ_MSH_ID,A.TZ_KSH_PSPM,A.TZ_SCORE_INS_ID,D.TZ_JG_ID,D.TZ_MSPS_SCOR_MD_ID";
			examineeSql = examineeSql + " FROM PS_TZ_AQ_YHXX_TBL C,PS_TZ_FORM_WRK_T B,PS_TZ_MP_PW_KS_TBL A,PS_TZ_CLASS_INF_T D,PS_TZ_REG_USER_T E";
			examineeSql = examineeSql + " WHERE A.TZ_APP_INS_ID=B.TZ_APP_INS_ID AND B.OPRID = C.OPRID AND A.TZ_CLASS_ID=D.TZ_CLASS_ID AND A.TZ_CLASS_ID=? AND A.TZ_APPLY_PC_ID=? AND A.TZ_PWEI_OPRID=? AND A.TZ_DELETE_ZT='N' AND C.OPRID = E.OPRID"; 
			examineeSql = examineeSql + " ORDER BY A.TZ_KSH_PSPM ASC";
			
			List<Map<String, Object>> examineeList = sqlQuery.queryForList(examineeSql,new Object[]{classId,applyBatchId,oprid});
			
			for(Map<String, Object> mapExaminee : examineeList) {
				count++;
				
				Map<String, Object> examineeListJson = new HashMap<String, Object>();
				
				String examineeBmbId = mapExaminee.get("TZ_APP_INS_ID") == null ? "" : String.valueOf(mapExaminee.get("TZ_APP_INS_ID"));
				String examineeName = mapExaminee.get("TZ_REALNAME") == null ? "" : String.valueOf(mapExaminee.get("TZ_REALNAME"));
				String examineeRank = mapExaminee.get("TZ_KSH_PSPM") == null ? "" : String.valueOf(mapExaminee.get("TZ_KSH_PSPM"));
				String examineeTotalScore;
				String examineeInterviewId = mapExaminee.get("TZ_MSH_ID") == null ? "" : String.valueOf(mapExaminee.get("TZ_MSH_ID"));
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
			String sqlBasic = "SELECT A.TZ_CLASS_NAME,YEAR(A.TZ_START_DT) TZ_START_YEAR,A.TZ_JG_ID,A.TZ_PS_APP_MODAL_ID,A.TZ_CS_SCOR_MD_ID,A.TZ_MSCJ_SCOR_MD_ID,A.TZ_APP_MODAL_ID,C.TZ_APP_FORM_STA,B.OPRID,H.TZ_REALNAME,D.TZ_MSH_ID,F.TZ_BATCH_NAME";
			sqlBasic = sqlBasic + ",(SELECT G.TREE_NAME FROM PS_TZ_RS_MODAL_TBL G WHERE G.TZ_JG_ID=A.TZ_JG_ID AND G.TZ_SCORE_MODAL_ID=A.TZ_CS_SCOR_MD_ID) TREE_NAME_ZDDF";
			sqlBasic = sqlBasic + ",(SELECT G.TREE_NAME FROM PS_TZ_RS_MODAL_TBL G WHERE G.TZ_JG_ID=A.TZ_JG_ID AND G.TZ_SCORE_MODAL_ID=A.TZ_MSCJ_SCOR_MD_ID) TREE_NAME,G.TZ_PRJ_NAME";
			sqlBasic = sqlBasic + " FROM PS_TZ_AQ_YHXX_TBL D,PS_TZ_APP_INS_T C,PS_TZ_FORM_WRK_T B,PS_TZ_CLS_BATCH_T F,PS_TZ_CLASS_INF_T A,PS_TZ_PRJ_INF_T G,PS_TZ_REG_USER_T H";
			sqlBasic = sqlBasic + " WHERE B.TZ_APP_INS_ID = C.TZ_APP_INS_ID AND B.OPRID=D.OPRID AND D.OPRID=H.OPRID AND A.TZ_PRJ_ID = G.TZ_PRJ_ID AND A.TZ_CLASS_ID=? AND A.TZ_CLASS_ID = F.TZ_CLASS_ID AND F.TZ_BATCH_ID=? AND B.TZ_APP_INS_ID=?";

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
			String scoreModelIdAuto = mapRootBasic.get("TZ_CS_SCOR_MD_ID") == null ? "" : mapRootBasic.get("TZ_CS_SCOR_MD_ID").toString();
			String scoreTreeAuto = mapRootBasic.get("TREE_NAME_ZDDF") == null ? "" : mapRootBasic.get("TREE_NAME_ZDDF").toString();
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

			
			/*自动打分成绩参考*/
			String autoScoreDesc = "";
			List<String> listAutoScoreInfo = new ArrayList<String>();
			
			//自动打分成绩单编号
			String scoreInsIdAuto = sqlQuery.queryForObject("SELECT TZ_SCORE_INS_ID FROM PS_TZ_CS_KS_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=?", new Object[]{classId,applyBatchId,bmbId},"String");
			if(scoreInsIdAuto!=null && !"".equals(scoreInsIdAuto) && !"0".equals(scoreInsIdAuto)) {				 
				//自动打分成绩项
				String zzdfCjxSql = "SELECT A.TREE_NODE,A.PARENT_NODE_NAME,B.DESCR,B.TZ_SCORE_ITEM_TYPE,B.TZ_SCR_TO_SCORE";
				zzdfCjxSql += " FROM PSTREENODE A,PS_TZ_MODAL_DT_TBL B";
				zzdfCjxSql += " WHERE A.TREE_NAME=B.TREE_NAME AND A.TREE_NODE=B.TZ_SCORE_ITEM_ID AND A.TREE_NAME=? AND A.PARENT_NODE_NUM<>'0'";
				zzdfCjxSql += " ORDER BY A.TREE_NODE_NUM,A.TREE_NODE_NUM_END DESC";
				
				List<Map<String, Object>> listAutoScore = sqlQuery.queryForList(zzdfCjxSql,new Object[]{scoreTreeAuto});
				for(Map<String,Object> mapAutoScore : listAutoScore) {
					String scoreItemIdAuto = mapAutoScore.get("TREE_NODE") == null ? "" : mapAutoScore.get("TREE_NODE").toString();
					String scoreItemNameAuto = mapAutoScore.get("DESCR") == null ? "" : mapAutoScore.get("DESCR").toString();
					String scoreItemTypeAuto = mapAutoScore.get("TZ_SCORE_ITEM_TYPE") == null ? "" : mapAutoScore.get("TZ_SCORE_ITEM_TYPE").toString();
					String scoreItemXlkToSAuto = mapAutoScore.get("TZ_SCR_TO_SCORE") == null ? "" : mapAutoScore.get("TZ_SCR_TO_SCORE").toString();
				
					//查询成绩项分值、评语值、下拉框值
					String scoreItemXlkIdAuto = "",scoreItemValueAuto="",scoreItemCommentAuto="";
					String sqlScoreValue = "SELECT TZ_CJX_XLK_XXBH,TZ_SCORE_NUM, TZ_SCORE_PY_VALUE FROM PS_TZ_CJX_TBL WHERE TZ_SCORE_INS_ID=? AND TZ_SCORE_ITEM_ID=?";
					Map<String, Object> mapScoreValue = sqlQuery.queryForMap(sqlScoreValue,new Object[] {scoreInsIdAuto,scoreItemIdAuto});
					if(mapScoreValue==null) {
						
					} else {
						scoreItemXlkIdAuto = mapScoreValue.get("TZ_CJX_XLK_XXBH") == null ? "" : mapScoreValue.get("TZ_CJX_XLK_XXBH").toString();
						scoreItemValueAuto = mapScoreValue.get("TZ_SCORE_NUM") == null ? "" : mapScoreValue.get("TZ_SCORE_NUM").toString();
						scoreItemCommentAuto = mapScoreValue.get("TZ_SCORE_PY_VALUE") == null ? "" : mapScoreValue.get("TZ_SCORE_PY_VALUE").toString();
						
						String scoreInfo = "";
						if("A".equals(scoreItemTypeAuto) || "B".equals(scoreItemTypeAuto) || ("D".equals(scoreItemTypeAuto)&&"Y".equals(scoreItemXlkToSAuto))) {
							scoreInfo = scoreItemNameAuto+"【"+scoreItemValueAuto+"】&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
						} else if ("C".equals(scoreItemTypeAuto)) {
							scoreInfo = scoreItemNameAuto+"【"+scoreItemCommentAuto+"】&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
						} else if("D".equals(scoreItemTypeAuto)) {
							// 如果是下拉框，需要取得下拉框名称，此处存的是下拉框编号;
							String scoreItemXlkNameAuto = sqlQuery.queryForObject(
									"select TZ_CJX_XLK_XXMC from PS_TZ_ZJCJXXZX_T where TREE_NAME=? and TZ_SCORE_ITEM_ID=? and TZ_CJX_XLK_XXBH=?",
									new Object[] { scoreTreeAuto, scoreItemIdAuto, scoreItemXlkIdAuto },
									"String");
							if(scoreItemXlkNameAuto!=null && !"".equals(scoreItemXlkNameAuto)) {
								scoreInfo = scoreItemNameAuto+"【"+scoreItemXlkNameAuto+"】&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
							}
						}
						
						if(!"".equals(scoreInfo)) {
							listAutoScoreInfo.add(scoreInfo);
						}
					}				
				}
				
				if(listAutoScoreInfo.size()>0) {
					autoScoreDesc = "<table width='100%'><tbody>";
					
					Integer allNum = listAutoScoreInfo.size();
					//每行显示4个
					Integer rowNum = allNum/4;
					Integer modNum = allNum%4;
					if(modNum==0) {
						
					} else {
						rowNum=rowNum+1;
					}
					
					for(int row=0;row<rowNum;row++) {
						if(row==0) {
							autoScoreDesc += "<tr><td width='155px;'>自动打分成绩参考：</td>";
						} else {
							autoScoreDesc += "<tr><td width='155px;'></td>";
						}
						
						int start = row*4; 
						int end = start+4;
						if(row==(rowNum-1)) {
							end = allNum;
						}
						 			
						for(int i=start;i<end;i++) {
							autoScoreDesc +="<td width='230px;'>"+listAutoScoreInfo.get(i)+"</td>";
						}
						
						autoScoreDesc += "</tr>";
					}
					
					autoScoreDesc += "</tbody></table>";
				}
				 
			} 
			
			mapRet.put("autoScoreDesc", autoScoreDesc);
			
			
			/*评委类型：英语A或其他O*/
			String pwType = sqlQuery.queryForObject("SELECT TZ_PWEI_TYPE FROM PS_TZ_MSPS_PW_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_PWEI_OPRID=?", new Object[] {classId,applyBatchId,oprid},"String");
			
			//成绩模型中英语成绩项ID
			String englishCjxId = getHardCodePoint.getHardCodePointVal("TZ_MSPS_YYNL_CJX_ID");
			
			
			DecimalFormat df = new DecimalFormat("0.0");
			
			/*成绩项*/
			ArrayList<Map<String, Object>> scoreItemJson = new ArrayList<Map<String,Object>>();
			
			String sqlScore = "SELECT A.TREE_NODE,A.PARENT_NODE_NAME,B.DESCR,B.TZ_SCORE_ITEM_TYPE,(SELECT 'Y' FROM PSTREENODE C WHERE C.TREE_NAME=A.TREE_NAME AND C.TREE_NODE_NUM>A.TREE_NODE_NUM AND C.TREE_NODE_NUM_END<A.TREE_NODE_NUM_END  LIMIT 1) TZ_NO_LEAF,A.TREE_LEVEL_NUM,B.TZ_SCORE_LIMITED,B.TZ_SCORE_LIMITED2,B.TZ_SCORE_PY_ZSLIM,B.TZ_SCORE_PY_ZSLIM0,B.TZ_SCORE_ITEM_DFSM,B.TZ_SCORE_ITEM_CKWT,B.TZ_SCORE_ITEM_MSFF,B.TZ_SCORE_CKZL";
			sqlScore = sqlScore + " FROM PSTREENODE A,PS_TZ_MODAL_DT_TBL B";
			sqlScore = sqlScore + " WHERE A.TREE_NAME=B.TREE_NAME AND A.TREE_NODE=B.TZ_SCORE_ITEM_ID AND A.TREE_NAME=?";
			sqlScore = sqlScore + " ORDER BY A.TREE_NODE_NUM,A.TREE_NODE_NUM_END DESC";

			List<Map<String,Object>> scoreList = sqlQuery.queryForList(sqlScore,new Object[]{scoreTree});

			for (Map<String, Object>mapScore : scoreList) {
				
				Boolean scoreItemShow = true;
				Map<String, Object>mapScoreJson = new HashMap<String,Object>();
				
				String scoreItemId = mapScore.get("TREE_NODE") == null ? "" : mapScore.get("TREE_NODE").toString(); 
				String scoreItemParentId = mapScore.get("PARENT_NODE_NAME") == null ? "" : mapScore.get("PARENT_NODE_NAME").toString();
				String scoreItemName = mapScore.get("DESCR") == null ? "" : mapScore.get("DESCR").toString();
				String scoreItemType = mapScore.get("TZ_SCORE_ITEM_TYPE") == null ? "" : mapScore.get("TZ_SCORE_ITEM_TYPE").toString();
				String scoreItemIsLeaf = mapScore.get("TZ_NO_LEAF") == null ? "Y" : "N";
				String scoreItemLevel = mapScore.get("TREE_LEVEL_NUM") == null ? "" : mapScore.get("TREE_LEVEL_NUM").toString();
				String scoreItemValueUpper = mapScore.get("TZ_SCORE_LIMITED") == null ? "0" : mapScore.get("TZ_SCORE_LIMITED").toString();
				String scoreItemValueLower = mapScore.get("TZ_SCORE_LIMITED2") == null ? "0" : mapScore.get("TZ_SCORE_LIMITED2").toString();
				String scoreItemValue = "";
				String scoreItemCommentUpper = mapScore.get("TZ_SCORE_PY_ZSLIM") == null ? "" : mapScore.get("TZ_SCORE_PY_ZSLIM").toString();
				String scoreItemCommentLower = mapScore.get("TZ_SCORE_PY_ZSLIM0") == null ? "" : mapScore.get("TZ_SCORE_PY_ZSLIM0").toString();
				String scoreItemComment = "";
				String scoreItemXlkId = "";
				String scoreItemDfsm = mapScore.get("TZ_SCORE_ITEM_DFSM") == null ? "" : mapScore.get("TZ_SCORE_ITEM_DFSM").toString();
				String scoreItemCkwt = mapScore.get("TZ_SCORE_ITEM_CKWT") == null ? "" : mapScore.get("TZ_SCORE_ITEM_CKWT").toString();
				String scoreItemMsff = mapScore.get("TZ_SCORE_ITEM_MSFF") == null ? "" : mapScore.get("TZ_SCORE_ITEM_MSFF").toString();
				String scoreItemCkzl = mapScore.get("TZ_SCORE_CKZL") == null ? "" : mapScore.get("TZ_SCORE_CKZL").toString();//参考资料
				
				if(!"".equals(scoreItemValueUpper)) {
					if(scoreItemValueUpper.indexOf(".")!=-1) {
						scoreItemValueUpper = scoreItemValueUpper.substring(0, scoreItemValueUpper.indexOf("."));
					}
				}
				if(!"".equals(scoreItemValueLower)) {
					if(scoreItemValueLower.indexOf(".")!=-1) {
						scoreItemValueLower = scoreItemValueLower.substring(0, scoreItemValueLower.indexOf("."));
					}
				}
				
				/*查询成绩项分值、评语值、下拉框值*/
				String sqlScoreValue = "SELECT TZ_CJX_XLK_XXBH,TZ_SCORE_NUM, TZ_SCORE_PY_VALUE FROM PS_TZ_CJX_TBL WHERE TZ_SCORE_INS_ID=? AND TZ_SCORE_ITEM_ID=?";
				Map<String, Object> mapScoreValue = sqlQuery.queryForMap(sqlScoreValue,new Object[] {scoreInsId,scoreItemId});
				if(mapScoreValue==null) {
					//没有成绩单时，分数取值默认中间值
					/*
					Double dbValueUpper = Double.valueOf(scoreItemValueUpper);
					Double dbValueLower = Double.valueOf(scoreItemValueLower);
					
					scoreItemValue = df.format((dbValueUpper+dbValueLower)/2);
					*/
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
		
				if(englishCjxId.equals(scoreItemId) || englishCjxId.equals(scoreItemParentId)) {
					if("A".equals(pwType)) {
						//评委类型是英语的可见英语成绩项相关
						scoreItemShow = true;
					} else {
						scoreItemShow = false;
					}
				} else {
					scoreItemShow = true;
				}
				
				
				if(scoreItemShow) {
					
					if(!"".equals(scoreItemValue)) {
						if(scoreItemValue.indexOf(".")!=-1) {
							scoreItemValue = scoreItemValue.substring(0, scoreItemValue.indexOf("."));
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
				
			}
			
			mapRet.put("scoreContent", scoreItemJson);
			
			
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
				String sql = "SELECT A.OPRID,A.TZ_APP_INS_ID,C.TZ_MSH_ID,D.TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL C,PS_TZ_FORM_WRK_T A,PS_TZ_MSPS_KSH_TBL B,PS_TZ_REG_USER_T D";
				sql = sql + " WHERE A.TZ_APP_INS_ID=B.TZ_APP_INS_ID AND A.OPRID=C.OPRID AND C.OPRID=D.OPRID AND B.TZ_CLASS_ID=? AND B.TZ_APPLY_PC_ID=?";
				
				if(!"".equals(searchMsid) && !"".equals(searchName)){
					sql = sql + " AND C.TZ_MSH_ID=? AND D.TZ_REALNAME=?";
					searchList = sqlQuery.queryForList(sql, new Object[]{classId,applyBatchId,searchMsid,searchName});
				} else {
					if(!"".equals(searchMsid)) {
						sql = sql + " AND C.TZ_MSH_ID=?";
						searchList = sqlQuery.queryForList(sql, new Object[]{classId,applyBatchId,searchMsid});
					} else {
						if(!"".equals(searchName)) {
							sql = sql + " AND D.TZ_REALNAME=?";
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
				
				if (jacksonUtil.containsKey(scoreItemId)) {
					
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
