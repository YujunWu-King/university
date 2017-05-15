package com.tranzvision.gd.TZAutomaticScreenBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tranzvision.gd.batch.engine.base.BaseEngine;
import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.poi.excel.ExcelHandle2;
import com.tranzvision.gd.util.sql.SqlQuery;

public class TzAutoScreenExportEngineCls extends BaseEngine {

	@SuppressWarnings("unchecked")
	@Override
	public void OnExecute() throws Exception {
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		// jdbctmplate;
		SqlQuery sqlQuery = (SqlQuery) getSpringBeanUtil.getAutowiredSpringBean("SqlQuery");

		// 运行id;
		String runControlId = this.getRunControlID();
		int processinstance =  this.getProcessInstanceID();
		
		try{
			//导出参数
			Map<String,Object> paramsMap = sqlQuery.queryForMap("select TZ_CLASS_ID,TZ_BATCH_ID,TZ_REL_URL,TZ_JD_URL,TZ_PARAMS_STR from PS_TZ_ZDCS_DC_AET where RUN_CNTL_ID=?"
					, new Object[] { runControlId });

			if(paramsMap != null){
				String classId = paramsMap.get("TZ_CLASS_ID").toString();
				String batchId = paramsMap.get("TZ_BATCH_ID").toString();
				String expDirPath = paramsMap.get("TZ_REL_URL").toString();
				String absexpDirPath = paramsMap.get("TZ_JD_URL").toString();
				String strParams = paramsMap.get("TZ_PARAMS_STR").toString();
				
				
				ExcelHandle2 excelHandle = new ExcelHandle2(expDirPath, absexpDirPath);
				List<String[]> dataCellKeys = new ArrayList<String[]>();
				
				jacksonUtil.json2Map(strParams);
				
				/******************************1、生成标题行---开始**************************************/
				dataCellKeys.add(new String[] { "msApplyNo", "面试申请号" });
				dataCellKeys.add(new String[] { "kshName", "姓名" });
				
				List<Map<String,String>> scoreItemList = new ArrayList<Map<String,String>>();
				if(jacksonUtil.containsKey("itemColumns")){
					scoreItemList = (List<Map<String,String>>) jacksonUtil.getList("itemColumns");
					if(scoreItemList != null && scoreItemList.size() > 0){
						//循环所有自动打分成绩项
						for(Map<String,String> scoreItemMap : scoreItemList){
							String itemId = scoreItemMap.get("columnId");
							String itemDescr = scoreItemMap.get("columnDescr");
							
							dataCellKeys.add(new String[] { "SCORE-"+itemId, itemDescr });
							dataCellKeys.add(new String[] { "DFGC-"+itemId, itemDescr + "打分过程" });
						}
					}
				}
				dataCellKeys.add(new String[] { "Total-score", "总分" });
				dataCellKeys.add(new String[] { "Total-Ranking", "排名" });
				dataCellKeys.add(new String[] { "Negative-List", "负面清单" });
				dataCellKeys.add(new String[] { "Auto-Tagging", "自动标签" });
				dataCellKeys.add(new String[] { "Manual-Label", "手工标签" });
				
				dataCellKeys.add(new String[] { "result", "是否淘汰" });
				/******************************1、生成标题行---结束**************************************/
				
				
				/******************************2、生成自动初筛考生信息数据---开始**************************************/
				// 数据
				List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
				
				//导出选中考生自动初筛结果
				if(jacksonUtil.containsKey("appInsIds")){
					List<String> listAppInsIdExport = (List<String>) jacksonUtil.getList("appInsIds");
					
					if(listAppInsIdExport != null && listAppInsIdExport.size() > 0){
						for(String appInsId : listAppInsIdExport){
							
							Map<String, Object> mapData = this.tzExportData(sqlQuery,classId,batchId,appInsId,scoreItemList);
							dataList.add(mapData);
						}
					}
				}
				
				
				//导出搜索结果考生自动初筛结果
				if(jacksonUtil.containsKey("searchSql")){
					String searchSql = jacksonUtil.getString("searchSql");
					List<Map<String,Object>> appInsList = sqlQuery.queryForList(searchSql);
					
					for(Map<String,Object> appInsMap: appInsList){
						String appInsId = appInsMap.get("TZ_APP_INS_ID") == null ? "" : appInsMap.get("TZ_APP_INS_ID").toString();
						
						Map<String, Object> mapData = this.tzExportData(sqlQuery,classId,batchId,appInsId,scoreItemList);
						dataList.add(mapData);
					}
				}
				
				String sql = "select TZ_SYSFILE_NAME from PS_TZ_EXCEL_DATT_T where PROCESSINSTANCE=?";
				String strUseFileName = sqlQuery.queryForObject(sql, new Object[]{ processinstance }, "String");
				
				boolean rst = excelHandle.export2Excel(strUseFileName, dataCellKeys, dataList);
				if (rst) {
					String urlExcel = excelHandle.getExportExcelPath();
					try {
						sqlQuery.update(
								"update  PS_TZ_EXCEL_DATT_T set TZ_FWQ_FWLJ = ? where PROCESSINSTANCE=?",
								new Object[] { urlExcel, processinstance });
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
				/******************************2、生成自动初筛考生信息数据---结束**************************************/
			}else{
				this.logError("获取批处理参数失败");
			}
		}catch(Exception e){
			e.printStackTrace();
			this.logError("系统错误，"+e.getMessage());
		}
	}
	
	
	/**
	 * 
	 * @param classId
	 * @param batchId
	 * @param appInsId
	 * @param scoreItemList
	 * @return
	 */
	private Map<String, Object> tzExportData(SqlQuery sqlQuery,String classId,String batchId,String appInsId,List<Map<String,String>> scoreItemList){
		Map<String, Object> mapData = new HashMap<String, Object>();
		
		String sql = "select TZ_MSH_ID,TZ_REALNAME,TZ_TOTAL_SCORE,TZ_KSH_PSPM,TZ_KSH_CSJG,TZ_SCORE_INS_ID from PS_TZ_CS_STU_VW where TZ_CLASS_ID=? and TZ_BATCH_ID=? and TZ_APP_INS_ID=?";
		Map<String,Object>  stuMap = sqlQuery.queryForMap(sql, new Object[]{ classId, batchId, appInsId });
		
		
		String msAppNo = "";//面试申请号
		String name = "";	//姓名
		String totalScore = "";	//总分
		String paiM  = "";	//排名
		String result = "";	//初筛结果
		String scoreInsId = "";	//成绩单实例ID
		
		if(stuMap != null){
			msAppNo = stuMap.get("TZ_MSH_ID") == null ? "": stuMap.get("TZ_MSH_ID").toString();
			name = stuMap.get("TZ_REALNAME") == null ? "": stuMap.get("TZ_REALNAME").toString();
			totalScore = stuMap.get("TZ_TOTAL_SCORE") == null ? "": stuMap.get("TZ_TOTAL_SCORE").toString();
			paiM = stuMap.get("TZ_KSH_PSPM") == null ? "": stuMap.get("TZ_KSH_PSPM").toString();
			result = stuMap.get("TZ_KSH_CSJG") == null ? "": stuMap.get("TZ_KSH_CSJG").toString();
			scoreInsId = stuMap.get("TZ_SCORE_INS_ID") == null ? "": stuMap.get("TZ_SCORE_INS_ID").toString();
			
			if(!"".equals(result)){
				String resultSql = "SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID='TZ_ZDCS_JG' AND TZ_ZHZ_ID=?";
				result = sqlQuery.queryForObject(resultSql, new Object[]{ result }, "String");
			}
		}
		
		
		mapData.put("msApplyNo", msAppNo);
		mapData.put("kshName", name);
		mapData.put("Total-score", totalScore);
		mapData.put("Total-Ranking", paiM);
		mapData.put("result", result);

		
		if(scoreItemList != null && scoreItemList.size() > 0){
			//循环所有自动打分成绩项
			for(Map<String,String> scoreItemMap : scoreItemList){
				String itemId = scoreItemMap.get("columnId");
				
				String itemSql = "select TZ_SCORE_NUM,TZ_SCORE_DFGC from PS_TZ_CJX_TBL where TZ_SCORE_INS_ID=? and TZ_SCORE_ITEM_ID=?";
				Map<String,Object> scoreMap = sqlQuery.queryForMap(itemSql, new Object[]{ scoreInsId, itemId });
				
				String scoreNum = "0";
				String scoreGc = "";
				if(scoreMap != null){
					scoreNum = scoreMap.get("TZ_SCORE_NUM") == null? "0" : scoreMap.get("TZ_SCORE_NUM").toString();
					//打分过程
					scoreGc = scoreMap.get("TZ_SCORE_DFGC").toString();
				}
				mapData.put("SCORE-"+itemId, scoreNum);
				mapData.put("DFGC-"+itemId, scoreGc);
			}
		}
		
		//自动标签
		String zdbqVal = "";
		String zdbqSql = "select group_concat(TZ_BIAOQZ_NAME SEPARATOR '|') as TZ_ZDBQ from PS_TZ_CS_KSBQ_T where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? and TZ_APP_INS_ID=?";
		zdbqVal = sqlQuery.queryForObject(zdbqSql, new Object[]{ classId, batchId, appInsId } , "String");
		mapData.put("Auto-Tagging", zdbqVal);
		
		//负面清单
		String fmqdVal = "";
		String fmqdSql = "select group_concat(TZ_FMQD_NAME SEPARATOR '|') as TZ_FMQD from PS_TZ_CS_KSFM_T where TZ_CLASS_ID=? and TZ_APPLY_PC_ID=? and TZ_APP_INS_ID=?";
		fmqdVal = sqlQuery.queryForObject(fmqdSql, new Object[]{ classId, batchId, appInsId } , "String");
		mapData.put("Negative-List", fmqdVal);
		
		//手动标签
		String sdbqVal = "";
		String sdbqSql = "select group_concat(TZ_LABEL_NAME SEPARATOR '|') as TZ_LABEL_NAME from PS_TZ_FORM_LABEL_T A,PS_TZ_LABEL_DFN_T B where A.TZ_LABEL_ID=B.TZ_LABEL_ID and TZ_APP_INS_ID=?";
		sdbqVal = sqlQuery.queryForObject(sdbqSql, new Object[]{ appInsId } , "String");
		mapData.put("Manual-Label", sdbqVal);
		
		return mapData;
	}
}
