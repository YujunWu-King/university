package com.tranzvision.gd.TZMaterialInterviewReviewBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 材料评审考生名单-新增考生
 * @author LuYan
 * 2017-3-30
 *
 */
@Service("com.tranzvision.gd.TZMaterialInterviewReviewBundle.service.impl.TzClpsAddExamineeServiceImpl")
public class TzClpsAddExamineeServiceImpl extends FrameworkImpl {
	
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private TZGDObject tzSQLObject;
	@Autowired
	private SqlQuery sqlQuery;
	

	/*所有考生，默认显示进入的班级批次下的考生*/
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String strParams,int numLimit,int numStart,String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		Map<String, Object> mapRet = new HashMap<String,Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String,Object>>();
		mapRet.put("root", listData);

		try {
			
			//排序字段
			String[][] orderByArr = new String[][] {{"TZ_MSSQH","ASC"}};
			
			//json数据要的结果字段
			String[] resultFldArray = {
					"TZ_CLASS_ID","TZ_CLASS_NAME","TZ_APPLY_PC_ID","TZ_BATCH_NAME","TZ_REALNAME","TZ_MSSQH","TZ_APP_INS_ID","TZ_GENDER","TZ_GENDER_DESC"};
			
			//可配置搜索通用函数
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart, errMsg);
			
			
			String sql = "";
			
		    String classId = "";
		    String batchId = "";
		    
		    String strIsExist = "";
		    //每生评审人数
			Integer mspsNum = 0;
			//当前评审轮次
			Integer dqpyLunc = 0;
			
			
			if(obj!=null && obj.length>0) {
				
				int numTotal = (int) obj[0];
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
				
				if(list!=null && list.size()>0) {
					String[] rowListTmp = list.get(0);
					classId = rowListTmp[0];
					batchId = rowListTmp[2];
					
					sql = "SELECT 'Y' TZ_IS_EXIST,TZ_MSPY_NUM,TZ_DQPY_LUNC FROM PS_TZ_CLPS_GZ_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=?";
					Map<String, Object> mapRule = sqlQuery.queryForMap(sql, new Object[] {classId,batchId});
					if(mapRule!=null) {
						strIsExist = mapRule.get("TZ_IS_EXIST") == null ? "" : mapRule.get("TZ_IS_EXIST").toString();
					}
					
					if(!"Y".equals(strIsExist)) {
						//没有定义评审规则
						mspsNum = 2;
					} else {
						//定义了评审规则
						String strMspsNum = mapRule.get("TZ_MSPY_NUM") == null ? "" : mapRule.get("TZ_MSPY_NUM").toString();
						if(!"".equals(strMspsNum)) {
							mspsNum = Integer.valueOf(strMspsNum);
						}
						
						String strDqpyLunc = mapRule.get("TZ_DQPY_LUNC") == null ? "" : mapRule.get("TZ_DQPY_LUNC").toString();
						if(!"".equals(strDqpyLunc)) {
							dqpyLunc = Integer.valueOf(strDqpyLunc);
						}
					}
				}
				
				//可配置搜索查询语句
				String[] resultFldArraySql = {"TZ_APP_INS_ID"};
				
				String searchSql = fliterForm.getQuerySQL(resultFldArraySql, orderByArr, strParams, errMsg);
				
				if(numLimit == 0 && numStart == 0) {
					
				} else {
					if (numLimit != 0) {
						searchSql = searchSql + " limit " + numStart + "," + numLimit ;
					} else if (numLimit == 0 && numStart > 0) {							
						searchSql = searchSql + " limit " + numStart + "," + (numTotal - numStart) ;
					}
				}
								
				
				//查询列表考生的所有评委、评审信息、标签等
				ArrayList<Map<String, Object>> listKsOtherInfo = new ArrayList<Map<String, Object>>();
				
				String judgeSql = "SELECT A.TZ_PWEI_OPRID,A.TZ_SCORE_INS_ID,A.TZ_APP_INS_ID,E.TZ_SCORE_NUM,F.TZ_DLZH_ID,B.TZ_SUBMIT_YN,G.TZ_FMQD_NAME,M.TZ_ZDBQ_NAME,N.TZ_SDBQ_NAME ";
				judgeSql += " FROM PS_TZ_CP_PW_KS_TBL A LEFT JOIN PS_TZ_KSCLPSLS_TBL B ON A.TZ_CLASS_ID=B.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID=B.TZ_APPLY_PC_ID AND A.TZ_APP_INS_ID=B.TZ_APP_INS_ID AND A.TZ_PWEI_OPRID=B.TZ_PWEI_OPRID";
				judgeSql += " LEFT JOIN PS_TZ_CLPS_GZ_TBL C ON A.TZ_CLASS_ID=C.TZ_CLASS_ID AND B.TZ_CLPS_LUNC = C.TZ_DQPY_LUNC AND A.TZ_APPLY_PC_ID=C.TZ_APPLY_PC_ID";
				judgeSql += " LEFT JOIN PS_TZ_CJX_TBL E ON A.TZ_SCORE_INS_ID=E.TZ_SCORE_INS_ID AND E.TZ_SCORE_ITEM_ID=(SELECT F.TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT F WHERE F.TZ_HARDCODE_PNT='TZ_CLPS_ZFCJX_ID')";
				judgeSql += " LEFT JOIN PS_TZ_AQ_YHXX_TBL F ON A.TZ_PWEI_OPRID=F.OPRID";
				judgeSql += " LEFT JOIN PS_TZ_KSALL_FMQD_VW G ON A.TZ_CLASS_ID=G.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID=G.TZ_APPLY_PC_ID AND A.TZ_APP_INS_ID=G.TZ_APP_INS_ID ";
				judgeSql += " LEFT JOIN PS_TZ_KSALL_ZDBQ_VW M ON A.TZ_CLASS_ID=M.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID=M.TZ_APPLY_PC_ID AND A.TZ_APP_INS_ID=M.TZ_APP_INS_ID ";
				judgeSql += " LEFT JOIN PS_TZ_KSALL_SDBQ_VW N ON A.TZ_APP_INS_ID=N.TZ_APP_INS_ID ";
				judgeSql += " WHERE B.TZ_CLASS_ID = ? AND B.TZ_APPLY_PC_ID = ? AND B.TZ_CLPS_LUNC=? AND A.TZ_APP_INS_ID IN ( SELECT X.TZ_APP_INS_ID FROM ( " + searchSql + " ) X ) AND B.TZ_SUBMIT_YN<>'C' ";
					
				List<Map<String, Object>> listKsOther = sqlQuery.queryForList(judgeSql,new Object[] {classId,batchId,dqpyLunc});
				
				for(Map<String, Object> mapKsOther : listKsOther) {
					String appinsId = mapKsOther.get("TZ_APP_INS_ID") == null ? "" : mapKsOther.get("TZ_APP_INS_ID").toString();
					String pwDlzhId = mapKsOther.get("TZ_DLZH_ID") == null ? "" : mapKsOther.get("TZ_DLZH_ID").toString();
					String submitFlag = mapKsOther.get("TZ_SUBMIT_YN") == null ? "" : mapKsOther.get("TZ_SUBMIT_YN").toString();
					String fmqdName = mapKsOther.get("TZ_FMQD_NAME") == null ? "" : mapKsOther.get("TZ_FMQD_NAME").toString();
					String zdbqName = mapKsOther.get("TZ_ZDBQ_NAME") == null ? "" : mapKsOther.get("TZ_ZDBQ_NAME").toString();
					String sdbqName = mapKsOther.get("TZ_SDBQ_NAME") == null ? "" : mapKsOther.get("TZ_SDBQ_NAME").toString();
				
					Map<String, Object> mapKsOtherInfo = new HashMap<String,Object>();
					mapKsOtherInfo.put("appinsId", appinsId);
					mapKsOtherInfo.put("pwDlzhId", pwDlzhId);
					mapKsOtherInfo.put("pwpsStatus", submitFlag);
					mapKsOtherInfo.put("fmqdName", fmqdName);
					mapKsOtherInfo.put("zdbqName", zdbqName);
					mapKsOtherInfo.put("sdbqName", sdbqName);
					
					listKsOtherInfo.add(mapKsOtherInfo);
				}
				
				for(int i=0;i<list.size();i++) {
					String[] rowList = list.get(i);
					
					classId = rowList[0];
					batchId = rowList[2];
					String appinsId = rowList[6];
						
					//评委列表、评审状态
					String pwList = "",reviewStatusDesc = "";
					//评委数
					Integer pwNum = 0;
					//负面清单、自动标签、手动标签
					String fmqdName = "",zdbqName = "",sdbqName = "";
					
					for(Map<String, Object> mapPwInfo : listKsOtherInfo) {
						String appinsIdTmp = mapPwInfo.get("appinsId") == null ? "" : mapPwInfo.get("appinsId").toString();
						
						if(appinsId.equals(appinsIdTmp)) {
							
							String pwDlzhIdTmp = mapPwInfo.get("pwDlzhId") == null ? "" : mapPwInfo.get("pwDlzhId").toString();
							String pwpsStatusTmp = mapPwInfo.get("pwpsStatus") == null ? "" : mapPwInfo.get("pwpsStatus").toString();
							fmqdName = mapPwInfo.get("fmqdName") == null ? "" : mapPwInfo.get("fmqdName").toString();
							zdbqName = mapPwInfo.get("zdbqName") == null ? "" : mapPwInfo.get("zdbqName").toString();
							sdbqName = mapPwInfo.get("sdbqName") == null ? "" : mapPwInfo.get("sdbqName").toString();
							
							if ("Y".equals(pwpsStatusTmp)) {
								// 已评审
								pwNum++;
							}
	
							if (!"".equals(pwList)) {
								pwList += "," + pwDlzhIdTmp;
							} else {
								pwList = pwDlzhIdTmp;
							}
							
						}
					}
					

					/*
					if(!"Y".equals(strIsExist)) {
						//没有定义评审规则
					} else {
						//定义了评审规则
						sql = tzSQLObject.getSQLText("SQL.TZMaterialInterviewReviewBundle.material.TzGetMaterialKsPwInfo");
						List<Map<String, Object>> listPw = sqlQuery.queryForList(sql, new Object[] {classId,batchId,appinsId,dqpyLunc});
						
						for(Map<String, Object> mapPw : listPw) {

							//String pwOprid = mapPw.get("TZ_PWEI_OPRID")  == null ? "" : mapPw.get("TZ_PWEI_OPRID").toString();
							String pwDlzhId = mapPw.get("TZ_DLZH_ID") == null ? "" : mapPw.get("TZ_DLZH_ID").toString();
							String submitFlag = mapPw.get("TZ_SUBMIT_YN") == null ? "" : mapPw.get("TZ_SUBMIT_YN").toString();
							
							if("Y".equals(submitFlag)) {
								//已评审
								pwNum++;
							}
							
							if(!"".equals(pwList)) {
								pwList += "," + pwDlzhId;
							} else {
								pwList = pwDlzhId;
							}
						}
						
					}
					*/
					
					
					if(mspsNum.equals(pwNum)) {
						reviewStatusDesc = "已完成";
					} else {
						reviewStatusDesc = "未完成（"+pwNum+"/"+mspsNum+"）";
					}
					
					Map<String, Object> mapList = new HashMap<String,Object>();
					mapList.put("classId", classId);
					mapList.put("className", rowList[1]);
					mapList.put("batchId", batchId);
					mapList.put("batchName", rowList[3]);
					mapList.put("name", rowList[4]);
					mapList.put("mssqh", rowList[5]);
					mapList.put("appinsId", appinsId);
					mapList.put("sex", rowList[7]);
					mapList.put("sexDesc", rowList[8]);
					mapList.put("judgeList", pwList);
					mapList.put("reviewStatusDesc", reviewStatusDesc);
					mapList.put("negativeList", fmqdName);
					mapList.put("autoLabel", zdbqName);
					mapList.put("manualLabel", sdbqName);
					
					
					//负面清单
					/*
					String fmqdVal = "";
					String fmqdSql = "select TZ_FMQD_ID,TZ_FMQD_NAME from PS_TZ_CS_KSFM_T where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? and TZ_APP_INS_ID=?";
					List<Map<String,Object>> fmqdList = sqlQuery.queryForList(fmqdSql, new Object[]{ classId, batchId, appinsId });
					for(Map<String,Object> fmqdMap : fmqdList){
						String LabelDesc = fmqdMap.get("TZ_FMQD_NAME") == null ? "" : fmqdMap.get("TZ_FMQD_NAME").toString();
						if(!"".equals(LabelDesc)){
							if("".equals(fmqdVal)){
								fmqdVal = LabelDesc;
							}else{
								fmqdVal = fmqdVal + "|" + LabelDesc;
							}
						}
					}
					mapList.put("negativeList", fmqdVal);
					*/
					
					//自动标签
					/*
					String zdbqVal = "";
					String zdbqSql = "select TZ_ZDBQ_ID,TZ_BIAOQZ_NAME from PS_TZ_CS_KSBQ_T where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? and TZ_APP_INS_ID=?";
					List<Map<String,Object>> zdbqList = sqlQuery.queryForList(zdbqSql, new Object[]{ classId, batchId, appinsId });
					for(Map<String,Object> zdbqMap : zdbqList){
						String LabelDesc = zdbqMap.get("TZ_BIAOQZ_NAME") == null ? "" : zdbqMap.get("TZ_BIAOQZ_NAME").toString();
						if(!"".equals(LabelDesc)){
							if("".equals(zdbqVal)){
								zdbqVal = LabelDesc ;
							}else{
								zdbqVal = zdbqVal + "|" + LabelDesc ;
							}
						}
					}
					mapList.put("autoLabel", zdbqVal);
					*/
					
					//手动标签
					/*
					String sdbqVal = "";
					String sdbqSql = "select TZ_LABEL_NAME from PS_TZ_FORM_LABEL_T A,PS_TZ_LABEL_DFN_T B where A.TZ_LABEL_ID=B.TZ_LABEL_ID and TZ_APP_INS_ID=?";
					List<Map<String,Object>> sdbqList = sqlQuery.queryForList(sdbqSql, new Object[]{ appinsId });
					for(Map<String,Object> sdbqMap: sdbqList){
						String LabelDesc = sdbqMap.get("TZ_LABEL_NAME") == null ? "" : sdbqMap.get("TZ_LABEL_NAME").toString();
						if(!"".equals(LabelDesc)){
							if("".equals(sdbqVal)){
								sdbqVal = LabelDesc;
							}else{
								sdbqVal = sdbqVal + "|" + LabelDesc;
							}
						}
					}
					mapList.put("manualLabel", sdbqVal);
					*/
					
					
					
					listData.add(mapList);
				}
				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		strRet = jacksonUtil.Map2json(mapRet);
		
		return strRet;
	}
}
