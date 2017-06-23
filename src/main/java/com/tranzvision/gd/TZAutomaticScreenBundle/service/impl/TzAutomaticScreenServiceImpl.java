package com.tranzvision.gd.TZAutomaticScreenBundle.service.impl;

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
import com.tranzvision.gd.TZAutomaticScreenBundle.dao.PsTzCsJcAetMapper;
import com.tranzvision.gd.TZAutomaticScreenBundle.dao.PsTzCsJcTMapper;
import com.tranzvision.gd.TZAutomaticScreenBundle.dao.PsTzCsKsTblMapper;
import com.tranzvision.gd.TZAutomaticScreenBundle.dao.PsTzCsLsjcTMapper;
import com.tranzvision.gd.TZAutomaticScreenBundle.dao.PsTzZdcsDcAetMapper;
import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCsJcAet;
import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCsJcT;
import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCsJcTKey;
import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCsKsTbl;
import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCsKsTblKey;
import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCsLsjcTKey;
import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzZdcsDcAet;
import com.tranzvision.gd.TZBaseBundle.service.impl.BatchProcessDetailsImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.batch.engine.base.BaseEngine;
import com.tranzvision.gd.batch.engine.base.EngineParameters;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/***
 * 自动初筛
 * @author zhanglang
 * 2017/02/14
 */
@Service("com.tranzvision.gd.TZAutomaticScreenBundle.service.impl.TzAutomaticScreenServiceImpl")
public class TzAutomaticScreenServiceImpl extends FrameworkImpl{

	@Autowired
	private FliterForm fliterForm;
	
	@Autowired
	private SqlQuery sqlQuery;
	
	@Autowired
	private TZGDObject tzSQLObject;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private GetSeqNum getSeqNum;
	
	@Autowired
	private PsTzCsJcTMapper psTzCsJcTMapper;
	
	@Autowired
	private TZGDObject tzGDObject;
	
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	
	@Autowired
	private PsTzCsKsTblMapper psTzCsKsTblMapper;
	
	@Autowired
	private PsTzCsJcAetMapper psTzCsJcAetMapper;
	
	@Autowired
	private PsTzCsLsjcTMapper psTzCsLsjcTMapper;
	
	@Autowired
	private BatchProcessDetailsImpl batchProcessDetailsImpl;
	
	@Autowired
	private PsTzExcelDattTMapper psTzExcelDattTMapper;
	
	@Autowired
	private PsTzExcelDrxxTMapper psTzExcelDrxxTMapper;
	
	@Autowired
	private PsTzZdcsDcAetMapper psTzZdcsDcAetMapper;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {

		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("total", 0);
		mapRet.put("root", listData);
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			
			if(jacksonUtil.containsKey("type") 
					&& "expHistory".equals(jacksonUtil.getString("type"))){
				// 排序字段
				String[][] orderByArr = new String[][] {{"TZ_STARTTIME","DESC"}};

				// json数据要的结果字段;
				String[] resultFldArray = { "TZ_CLASS_BATCH", "TZ_DLZH_ID", "TZ_DR_TASK_DESC", "TZ_STARTTIME", "PROCESSINSTANCE",
						"PROCESS_STATUS","TZ_REALNAME"};

				// 可配置搜索通用函数;
				Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart, errorMsg);

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
			
				/*排序*/
				String[][] orderByArr;
				
				String sort = request.getParameter("sort");
				if(!"".equals(sort) && sort != null){
					sort = "{\"sort\":"+ sort +"}";
					
					jacksonUtil.json2Map(sort);
					List<Map<String,String>> sortList = (List<Map<String, String>>) jacksonUtil.getList("sort");
	
					List<String[]> orderList = new ArrayList<String[]>();
					for(Map<String,String> sortMap: sortList){
						String columnField = sortMap.get("property");
						String sortStr = sortMap.get("direction");
						
						if("ranking".equals(columnField)){
							orderList.add(new String[]{ "TZ_KSH_PSPM", sortStr });
						}
						if("total".equals(columnField)){
							orderList.add(new String[]{ "TZ_TOTAL_SCORE", sortStr });
						}
					}
					
					orderByArr = new String[orderList.size()][2];
					for(int i=0; i<orderList.size(); i++){
						orderByArr[i] = orderList.get(i);
					}
				}else{
					//默认按排名排序
					orderByArr = new String[][] {new String[] { "TZ_KSH_PSPM", "ASC" }};
				}
				
				jacksonUtil.json2Map(strParams);
				//成绩项
				List<String> itemsList = (List<String>) jacksonUtil.getList("items");
				
				//TZ_AUTO_SCREEN_COM.TZ_AUTO_SCREEN_STD.TZ_CS_STU_VW
				// json数据要的结果字段;
				String[] resultFldArray = {"TZ_CLASS_ID","TZ_BATCH_ID","TZ_APP_INS_ID","TZ_REALNAME","TZ_MSH_ID","TZ_KSH_CSJG","TZ_KSH_PSPM","TZ_SCORE_INS_ID","TZ_TOTAL_SCORE"};
	
				// 可配置搜索通用函数;
				Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, strParams, numLimit, numStart, errorMsg);
	
				if (obj != null && obj.length > 0) {
	
					ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
	
					int num = list.size();
					for (int i = 0; i < num; i++) {
						String[] rowList = list.get(i);
	
						Map<String, Object> mapList = new HashMap<String, Object>();
						String classId = rowList[0];
						
						mapList.put("classId", classId);
						mapList.put("batchId", rowList[1]);
						mapList.put("appId", rowList[2]);
						mapList.put("name", rowList[3]);
						mapList.put("msApplyId", rowList[4]);
						
						boolean bool_status;
						if("N".equals(rowList[5])){
							bool_status = true;
						}else{
							bool_status = false;
						}
						mapList.put("status", bool_status);
						mapList.put("ranking", rowList[6]);
						
						//成绩单ID
						String scoreInsId = rowList[7];
						mapList.put("scoreInsId", scoreInsId);
						//总分
						mapList.put("total", rowList[8]);
						
						/*自动打分项*/
						for(String itemId : itemsList){
							String sql = "select TZ_SCORE_NUM,TZ_SCORE_DFGC from PS_TZ_CJX_TBL where TZ_SCORE_INS_ID=? and TZ_SCORE_ITEM_ID=?";
							Map<String,Object> scoreMap = sqlQuery.queryForMap(sql, new Object[]{ scoreInsId, itemId });
							
							String scoreNum = "0.00";
							String scoreGc = "";
							
							if(scoreMap != null){
								scoreNum = scoreMap.get("TZ_SCORE_NUM") == null? "0.00" : scoreMap.get("TZ_SCORE_NUM").toString();
								//打分过程
								scoreGc = scoreMap.get("TZ_SCORE_DFGC").toString();
							}
	
							mapList.put(itemId, scoreNum);
							mapList.put(itemId+"_label", scoreGc);
						}
	
						//自动标签
						String zdbqVal = "";
						/*
						String zdbqSql = "select TZ_ZDBQ_ID,TZ_BIAOQZ_NAME from PS_TZ_CS_KSBQ_T where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? and TZ_APP_INS_ID=?";
						List<Map<String,Object>> zdbqList = sqlQuery.queryForList(zdbqSql, new Object[]{ classId, rowList[1], rowList[2] });
						for(Map<String,Object> zdbqMap : zdbqList){
							String LabelDesc = zdbqMap.get("TZ_BIAOQZ_NAME") == null ? "" : zdbqMap.get("TZ_BIAOQZ_NAME").toString();
							if(!"".equals(LabelDesc)){
								if("".equals(zdbqVal)){
									zdbqVal = LabelDesc ;
								}else{
									zdbqVal = zdbqVal + "|" + LabelDesc ;
								}
							}
						}*/
						String zdbqSql = "select group_concat(TZ_BIAOQZ_NAME SEPARATOR '|') as TZ_ZDBQ from PS_TZ_CS_KSBQ_T where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? and TZ_APP_INS_ID=?";
						zdbqVal = sqlQuery.queryForObject(zdbqSql, new Object[]{ classId, rowList[1], rowList[2] } , "String");
						mapList.put("autoLabel", zdbqVal);
						
						//负面清单
						String fmqdVal = "";
						/*
						String fmqdSql = "select TZ_FMQD_ID,TZ_FMQD_NAME from PS_TZ_CS_KSFM_T where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? and TZ_APP_INS_ID=?";
						List<Map<String,Object>> fmqdList = sqlQuery.queryForList(fmqdSql, new Object[]{ classId, rowList[1], rowList[2] });
						for(Map<String,Object> fmqdMap : fmqdList){
							String LabelDesc = fmqdMap.get("TZ_FMQD_NAME") == null ? "" : fmqdMap.get("TZ_FMQD_NAME").toString();
							if(!"".equals(LabelDesc)){
								if("".equals(fmqdVal)){
									fmqdVal = LabelDesc;
								}else{
									fmqdVal = fmqdVal + "|" + LabelDesc;
								}
							}
						}*/
						String fmqdSql = "select group_concat(TZ_FMQD_NAME SEPARATOR '|') as TZ_FMQD from PS_TZ_CS_KSFM_T where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? and TZ_APP_INS_ID=?";
						fmqdVal = sqlQuery.queryForObject(fmqdSql, new Object[]{ classId, rowList[1], rowList[2] } , "String");
						mapList.put("negativeList", fmqdVal);
						
						//手动标签
						String sdbqVal = "";
						/*
						String sdbqSql = "select TZ_LABEL_NAME from PS_TZ_FORM_LABEL_T A,PS_TZ_LABEL_DFN_T B where A.TZ_LABEL_ID=B.TZ_LABEL_ID and TZ_APP_INS_ID=?";
						List<Map<String,Object>> sdbqList = sqlQuery.queryForList(sdbqSql, new Object[]{ rowList[2] });
						for(Map<String,Object> sdbqMap: sdbqList){
							String LabelDesc = sdbqMap.get("TZ_LABEL_NAME") == null ? "" : sdbqMap.get("TZ_LABEL_NAME").toString();
							if(!"".equals(LabelDesc)){
								if("".equals(sdbqVal)){
									sdbqVal = LabelDesc;
								}else{
									sdbqVal = sdbqVal + "|" + LabelDesc;
								}
							}
						}*/
						String sdbqSql = "select group_concat(TZ_LABEL_NAME SEPARATOR '|') as TZ_LABEL_NAME from PS_TZ_FORM_LABEL_T A,PS_TZ_LABEL_DFN_T B where A.TZ_LABEL_ID=B.TZ_LABEL_ID and TZ_APP_INS_ID=?";
						sdbqVal = sqlQuery.queryForObject(sdbqSql, new Object[]{ rowList[2] } , "String");
						mapList.put("manualLabel", sdbqVal);
						
						
						listData.add(mapList);
					}
				}
				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jacksonUtil.Map2json(mapRet);
	}
	
	
	
	@Override
	public String tzOther(String strType, String strParams, String[] errorMsg) {
		String strRet = "{}";
		try {
			switch (strType) {
				case "queryScoreColumns":	//获取成绩项动态列
					strRet = this.queryScoreColumns(strParams,errorMsg);
					break;
				case "queryWeedOutInfo":	//获取设置淘汰信息
					strRet = this.queryWeedOutInfo(strParams,errorMsg);
					break;
				case "setWeedOutByOrder":	//设置批量淘汰
					strRet = this.setWeedOutByOrder(strParams,errorMsg);
					break;
				case "getLastEngineStatus":	//获取最后一次自动初筛引擎运行状态
					strRet = this.getLastEngineStatus(strParams,errorMsg);
					break;
				case "runBatchProcess":	//运行自动初筛引擎
					strRet = this.tzRunBatchProcess(strParams,errorMsg);
					break;		
				case "getSearchSql":
					strRet = this.tzGetSearchSql(strParams,errorMsg);
					break;
				case "setSearchPsjgStatus":
					strRet = this.tzSetSearchPsjgStatus(strParams,errorMsg);
					break;
				case "EXPORT":
					strRet = this.tzExportZdcsResult(strParams,errorMsg);
					break;
				case "DOWNLOAD":
					strRet = this.tzDownloadExpFile(strParams,errorMsg);
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。" + e.getMessage();
		}
		return strRet;
	}
	
	
	/**
	 * 获取成绩项动态列
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	private String queryScoreColumns(String strParams, String[] errorMsg){
		String strRet = "";
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		ArrayList<Map<String,String>> columnsList = new ArrayList<Map<String,String>>();
		rtnMap.put("className", "");
		rtnMap.put("columns", columnsList);
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			//班级ID
			String classId = jacksonUtil.getString("classId");
			String sql = tzSQLObject.getSQLText("SQL.TZAutomaticScreenBundle.TzClassAutoScreenInfo");
			
			Map<String,Object> classMap = sqlQuery.queryForMap(sql, new Object[]{ classId });
			if(classMap != null){
				String className = classMap.get("TZ_CLASS_NAME") == null ? "" 
						: classMap.get("TZ_CLASS_NAME").toString();
				String orgId = classMap.get("TZ_JG_ID") == null ? "" 
						: classMap.get("TZ_JG_ID").toString();
				String csTreeName = classMap.get("TREE_NAME") == null ? "" 
						: classMap.get("TREE_NAME").toString();
				
				if(!"".equals(csTreeName) && csTreeName != null){
					
					//查询初筛模型中成绩项类型为“数字成绩录入项”且启用自动初筛的成绩项
					sql = tzSQLObject.getSQLText("SQL.TZAutomaticScreenBundle.TzAutoScreenScoreItems");
					List<Map<String,Object>> itemsList = sqlQuery.queryForList(sql, new Object[]{ orgId, csTreeName });
					
					for(Map<String,Object> itemMap : itemsList){
						String itemId = itemMap.get("TZ_SCORE_ITEM_ID").toString();
						String itemName = itemMap.get("DESCR").toString();
						
						Map<String,String> colMap = new HashMap<String,String>();
						colMap.put("columnId", itemId);
						colMap.put("columnDescr", itemName);
						
						columnsList.add(colMap);
					}
					rtnMap.replace("columns", columnsList);
				}
				rtnMap.replace("className", className);
			}
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。"+e.getMessage();
		}
		
		strRet = jacksonUtil.Map2json(rtnMap);
		return strRet;
	}
	
	
	/**
	 * 获取设置淘汰信息
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	private String queryWeedOutInfo(String strParams, String[] errorMsg){
		String strRet = "";
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("totalNum", "0");
		rtnMap.put("screenNum", "0");
		rtnMap.put("lastNum", "0");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			//班级ID
			String classId = jacksonUtil.getString("classId");
			String batchId = jacksonUtil.getString("batchId");
			
			if(!"".equals(classId) && classId != null 
					&& !"".equals(batchId) && batchId != null){
				//报考总数量
				String sql = "select count(1) from PS_TZ_FORM_WRK_T A where TZ_CLASS_ID=? and TZ_BATCH_ID=? and exists(select 'x' from PS_TZ_APP_INS_T where TZ_APP_INS_ID=A.TZ_APP_INS_ID and TZ_APP_FORM_STA='U')";
				int totalNum = sqlQuery.queryForObject(sql, new Object[]{ classId,batchId }, "Integer");
				//参与初筛人数
				sql = "select count(1) from PS_TZ_CS_STU_VW where TZ_CLASS_ID=? and TZ_BATCH_ID=?";
				int screenNum = sqlQuery.queryForObject(sql, new Object[]{ classId,batchId }, "Integer");
				
				//淘汰比率
				sql = "select TZ_TT_BL from PS_TZ_CLASS_INF_T where TZ_CLASS_ID=?";
				Float outBl = sqlQuery.queryForObject(sql, new Object[]{ classId }, "Float");
				//淘汰人数
				int lastNum = (int) (screenNum*outBl/100);
				
				rtnMap.replace("totalNum", totalNum);
				rtnMap.replace("screenNum", screenNum);
				rtnMap.replace("lastNum", lastNum);
			}
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。"+e.getMessage();
		}
		
		strRet = jacksonUtil.Map2json(rtnMap);
		return strRet;
	}
	
	/**
	 * 设置批量淘汰
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	private String setWeedOutByOrder(String strParams, String[] errorMsg){
		String strRet = "";
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("result", "");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			//班级ID
			String classId = jacksonUtil.getString("classId");
			String batchId = jacksonUtil.getString("batchId");
			short outNum = Short.valueOf(jacksonUtil.getString("outNum"));
			
			if(!"".equals(classId) && classId != null 
					&& !"".equals(batchId) && batchId != null){
				/**淘汰后N名考生**/
				
				/*1、先将所有非自动初筛淘汰的考生设置成未淘汰*/
				String setSql = tzSQLObject.getSQLText("SQL.TZAutomaticScreenBundle.TzSetNotAutoOutStudents");
				sqlQuery.update(setSql, new Object[]{ classId, batchId });
				/*
				List<Map<String,Object>> setList = sqlQuery.queryForList(setSql, new Object[]{ classId, batchId });
				if(setList != null){
					for(Map<String,Object> setMap: setList){
						String appInsId = setMap.get("TZ_APP_INS_ID") == null ? "":setMap.get("TZ_APP_INS_ID").toString();
						if(!"".equals(appInsId)){
							sqlQuery.update("update PS_TZ_CS_KS_TBL set TZ_KSH_CSJG='Y' where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? and TZ_APP_INS_ID=?"
									, new Object[]{ classId, batchId, appInsId });
						}
					}
				}*/
				
				//最后名次
				String sql = "select max(TZ_KSH_PSPM) from PS_TZ_CS_KS_TBL where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=?";
				int lastMc = 0;
				String lastMci = sqlQuery.queryForObject(sql, new Object[]{ classId, batchId }, "String");
				if(!"".equals(lastMci) && lastMci != null){
					lastMc = Integer.valueOf(lastMci);
				}
				
				int i;
				int passNum = 0;
				for(i=0;i<outNum;i++){
					//淘汰名次
					int outMc = lastMc - i;
					if(outMc>0){
						sql = "select count(1) from PS_TZ_CS_KS_TBL where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? and TZ_KSH_PSPM=?";
						int existsNum = sqlQuery.queryForObject(sql, new Object[]{ classId, batchId, outMc }, "int");
						
						if(existsNum > 0){
							if((passNum+existsNum) > outNum){
								//排名重复人数大于删除人数，不删除
								break;
							}else{
								sqlQuery.update("update PS_TZ_CS_KS_TBL set TZ_KSH_CSJG='N' where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? and TZ_KSH_PSPM=?", new Object[]{ classId, batchId, outMc });
								passNum = passNum + existsNum;
							}
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。"+e.getMessage();
		}
		
		strRet = jacksonUtil.Map2json(rtnMap);
		return strRet;
	}
	
	
	/**
	 * 获取最后一次自动初筛引擎运行状态
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	private String getLastEngineStatus(String strParams, String[] errorMsg){
		String strRet = "";
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("status", "");
		rtnMap.put("processIns", "");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			//班级ID
			String classId = jacksonUtil.getString("classId");
			String batchId = jacksonUtil.getString("batchId");
			
			if(!"".equals(classId) && classId != null 
					&& !"".equals(batchId) && batchId != null){
				String sql = "select PRCSINSTANCE from PS_TZ_CS_JC_T where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=?";
				Map<String,Object> procMap = sqlQuery.queryForMap(sql, new Object[]{ classId,batchId });
				
				if(procMap != null){
					int processIns = Integer.valueOf(procMap.get("PRCSINSTANCE").toString());
					
					sql = "select TZ_JOB_YXZT from TZ_JC_SHLI_T where TZ_JCSL_ID=?";
					String status = sqlQuery.queryForObject(sql, new Object[]{ processIns }, "String");
					
					rtnMap.replace("processIns", processIns);
					rtnMap.replace("status", status);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。"+e.getMessage();
		}
		
		strRet = jacksonUtil.Map2json(rtnMap);
		return strRet;
	}
	
	
	/**
	 * 运行自动初筛引擎
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	private String tzRunBatchProcess(String strParams, String[] errorMsg){
		String strRet = "";
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("status", "");
		rtnMap.put("processIns", "");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			//班级ID
			String classId = jacksonUtil.getString("classId");
			String batchId = jacksonUtil.getString("batchId");
			
			if(!"".equals(classId) && classId != null 
					&& !"".equals(batchId) && batchId != null){
				//当前用户;
				String currentOprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
				/*生成运行控制ID*/
				SimpleDateFormat datetimeFormate = new SimpleDateFormat("yyyyMMddHHmmss");
			    String s_dtm = datetimeFormate.format(new Date());
				String runCntlId = "ZDCS" + s_dtm + "_" + getSeqNum.getSeqNum("PSPRCSRQST", "RUN_ID");
				
				PsTzCsJcAet psTzCsJcAet = new PsTzCsJcAet();
				psTzCsJcAet.setRunId(runCntlId);
				psTzCsJcAet.setTzClassId(classId);
				psTzCsJcAet.setTzApplyPcId(batchId);
				psTzCsJcAet.setOprid(currentOprid);
				psTzCsJcAetMapper.insert(psTzCsJcAet);
				
				String currentAccountId = tzLoginServiceImpl.getLoginedManagerDlzhid(request);
				String currentOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
				
				BaseEngine tmpEngine = tzGDObject.createEngineProcess(currentOrgId, "TZ_AUTO_SCREEN_PROC");
				//指定调度作业的相关参数
				EngineParameters schdProcessParameters = new EngineParameters();

				schdProcessParameters.setBatchServer("");
				schdProcessParameters.setCycleExpression("");
				schdProcessParameters.setLoginUserAccount(currentAccountId);
				schdProcessParameters.setPlanExcuteDateTime(new Date());
				schdProcessParameters.setRunControlId(runCntlId);
				
				//调度作业
				tmpEngine.schedule(schdProcessParameters);
				
				// 进程id;
				//int processinstance = sqlQuery.queryForObject("SELECT TZ_JCSL_ID FROM TZ_JC_SHLI_T where TZ_YUNX_KZID = ? limit 0,1", new Object[] { runCntlId },"Integer");
				int processinstance = tmpEngine.getProcessInstanceID();
				if(processinstance>0){
					PsTzCsJcTKey psTzCsJcTKey = new PsTzCsJcTKey();
					psTzCsJcTKey.setTzClassId(classId);
					psTzCsJcTKey.setTzApplyPcId(batchId);
					PsTzCsJcT psTzCsJcT = psTzCsJcTMapper.selectByPrimaryKey(psTzCsJcTKey);
					if(psTzCsJcT != null){
						psTzCsJcT.setPrcsinstance(processinstance);
						psTzCsJcTMapper.updateByPrimaryKey(psTzCsJcT);
					}else{
						psTzCsJcT = new PsTzCsJcT();
						psTzCsJcT.setTzClassId(classId);
						psTzCsJcT.setTzApplyPcId(batchId);
						psTzCsJcT.setPrcsinstance(processinstance);
						psTzCsJcTMapper.insert(psTzCsJcT);
					}
					
					PsTzCsLsjcTKey psTzCsLsjcTKey = new PsTzCsLsjcTKey();
					psTzCsLsjcTKey.setTzClassId(classId);
					psTzCsLsjcTKey.setTzApplyPcId(batchId);
					psTzCsLsjcTKey.setPrcsinstance(processinstance);
					psTzCsLsjcTMapper.insert(psTzCsLsjcTKey);
					
				}
				
				
				rtnMap.replace("processIns", processinstance);
			}
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。"+e.getMessage();
		}
		
		strRet = jacksonUtil.Map2json(rtnMap);
		return strRet;
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("result", 0);
		mapRet.put("formData", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			//当前登录人
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);

				String type = jacksonUtil.getString("type");
				//保存grid修改
				if("GRIDSAVE".equals(type)){
					List<Map<String,String>> stuList = (List<Map<String, String>>) jacksonUtil.getList("data");
					for(Map<String,String> stuMap: stuList){
						String classId = stuMap.get("classId");
						String batchId = stuMap.get("batchId");
						Long appId = Long.valueOf(stuMap.get("appId"));
						String status = stuMap.get("status");
						
						PsTzCsKsTblKey psTzCsKsTblKey = new PsTzCsKsTblKey();
						psTzCsKsTblKey.setTzClassId(classId);
						psTzCsKsTblKey.setTzApplyPcId(batchId);
						psTzCsKsTblKey.setTzAppInsId(appId);
						PsTzCsKsTbl psTzCsKsTbl = psTzCsKsTblMapper.selectByPrimaryKey(psTzCsKsTblKey);
						
						if(psTzCsKsTbl != null){
							psTzCsKsTbl.setTzKshCsjg(status);
							psTzCsKsTbl.setRowLastmantDttm(new Date());
							psTzCsKsTbl.setRowLastmantOprid(oprid);
							psTzCsKsTbl.setRowLastmantDttm(new Date());
							psTzCsKsTbl.setRowLastmantOprid(oprid);
							psTzCsKsTblMapper.updateByPrimaryKey(psTzCsKsTbl);
						}else{
							psTzCsKsTbl = new PsTzCsKsTbl();
							psTzCsKsTbl.setTzClassId(classId);
							psTzCsKsTbl.setTzApplyPcId(batchId);
							psTzCsKsTbl.setTzAppInsId(appId);
							psTzCsKsTbl.setTzKshCsjg(status);
							psTzCsKsTbl.setRowAddedDttm(new Date());
							psTzCsKsTbl.setRowAddedOprid(oprid);
							psTzCsKsTbl.setRowLastmantDttm(new Date());
							psTzCsKsTbl.setRowLastmantOprid(oprid);
							psTzCsKsTblMapper.insert(psTzCsKsTbl);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.getMessage();
		}
		
		strRet = jacksonUtil.Map2json(mapRet);
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
	
	
	private String tzSetSearchPsjgStatus(String strParams, String[] errorMsg){
		String strRet = "";
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("result", "");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			//当前登录人
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			
			jacksonUtil.json2Map(strParams);
			
			if(jacksonUtil.containsKey("searchSql")){
				String searchSql = jacksonUtil.getString("searchSql");
				String classId = jacksonUtil.getString("classId");
				String batchId = jacksonUtil.getString("batchId");
				String setType = jacksonUtil.getString("setType");
				
				List<Map<String,Object>> setList = sqlQuery.queryForList(searchSql);
				
				for(Map<String,Object> setMap: setList){
					long appInsId = setMap.get("TZ_APP_INS_ID") == null ? 0 
							: Long.valueOf(setMap.get("TZ_APP_INS_ID").toString());
					
					if(appInsId > 0){
						PsTzCsKsTblKey psTzCsKsTblKey = new PsTzCsKsTblKey();
						psTzCsKsTblKey.setTzClassId(classId);
						psTzCsKsTblKey.setTzApplyPcId(batchId);
						psTzCsKsTblKey.setTzAppInsId(appInsId);
						PsTzCsKsTbl psTzCsKsTbl = psTzCsKsTblMapper.selectByPrimaryKey(psTzCsKsTblKey);
						
						Date currDate = new Date();
						if(psTzCsKsTbl != null){
							psTzCsKsTbl.setTzKshCsjg(setType);
							psTzCsKsTbl.setRowLastmantDttm(currDate);
							psTzCsKsTbl.setRowLastmantOprid(oprid);
							psTzCsKsTblMapper.updateByPrimaryKey(psTzCsKsTbl);
						}else{
							psTzCsKsTbl = new PsTzCsKsTbl();
							psTzCsKsTbl.setTzClassId(classId);
							psTzCsKsTbl.setTzApplyPcId(batchId);
							psTzCsKsTbl.setTzAppInsId(appInsId);
							psTzCsKsTbl.setTzKshCsjg(setType);
							psTzCsKsTbl.setRowAddedOprid(oprid);
							psTzCsKsTbl.setRowAddedDttm(currDate);
							psTzCsKsTbl.setRowLastmantDttm(currDate);
							psTzCsKsTbl.setRowLastmantOprid(oprid);
							psTzCsKsTblMapper.insert(psTzCsKsTbl);
						}
					}
				}
			}

		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "系统错误。"+e.getMessage();
		}
		
		strRet = jacksonUtil.Map2json(rtnMap);
		return strRet;
	}
	
	
	/**
	 * 导出自动初筛结果
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	private String tzExportZdcsResult(String strParams, String[] errorMsg){
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
			
			// 自动初筛excel存储路径
			String eventExcelPath = "/autoScreen/xlsx";
			
			// 完整的存储路径
			String expDirPath = downloadPath + eventExcelPath + "/" + getDateNow();
			String absexpDirPath = request.getServletContext().getRealPath(expDirPath);
			
			/*生成运行控制ID*/
			SimpleDateFormat dateFormate = new SimpleDateFormat("yyyyMMddHHmmss");
		    String s_dt = dateFormate.format(new Date());
			String runCntlId = "ZDCSJG" + s_dt + "_" + getSeqNum.getSeqNum("TZ_AUTO_SCREEN_COM", "ZDCS_EXPORT");
			
			
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
			
			BaseEngine tmpEngine = tzGDObject.createEngineProcess(currentOrgId, "TZ_ZDCSJG_EXP_PROC");
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
			
			
			PsTzExcelDrxxT psTzExcelDrxxT = new PsTzExcelDrxxT();
			psTzExcelDrxxT.setProcessinstance(processinstance);
			psTzExcelDrxxT.setTzComId("TZ_AUTO_SCREEN_COM");
			psTzExcelDrxxT.setTzPageId("TZ_AUTO_SCREEN_STD");
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
			
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "导出失败。" + e.getMessage();
		}

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
	
	
	/***
	 * 删除导出历史记录
	 */
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);
				//进程实例ID
				int procInsId = Integer.parseInt(jacksonUtil.getString("procInsId"));
				
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
			errMsg[0] = "0";
			errMsg[1] = "保存成功。";
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "保存失败。" + e.getMessage();
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
