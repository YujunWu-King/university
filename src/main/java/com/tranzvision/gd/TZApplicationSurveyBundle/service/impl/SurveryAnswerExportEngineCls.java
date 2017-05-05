package com.tranzvision.gd.TZApplicationSurveyBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tranzvision.gd.batch.engine.base.BaseEngine;
import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.poi.excel.ExcelHandle2;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 在线调查-导出调查结果
 * @author zhanglang
 * 2017-05-04
 */
public class SurveryAnswerExportEngineCls extends BaseEngine{

	@Override
	public void OnExecute() throws Exception {
		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		// jdbctmplate;
		SqlQuery sqlQuery = (SqlQuery) getSpringBeanUtil.getAutowiredSpringBean("SqlQuery");

		// 运行id;
		String runControlId = this.getRunControlID();
		int processinstance =  this.getProcessInstanceID();
		
		try{
			//导出参数
			Map<String,Object> paramsMap = sqlQuery.queryForMap("select TZ_DC_WJ_ID,TZ_REL_URL,TZ_JD_URL from PS_TZ_DCWJ_DC_AET where RUN_CNTL_ID=?", new Object[] { runControlId });

			if(paramsMap != null){
				String wjId = paramsMap.get("TZ_DC_WJ_ID").toString();
				String expDirPath = paramsMap.get("TZ_REL_URL").toString();
				String absexpDirPath = paramsMap.get("TZ_JD_URL").toString();
				
				ExcelHandle2 excelHandle = new ExcelHandle2(expDirPath, absexpDirPath);
				List<String[]> dataCellKeys = new ArrayList<String[]>();
				List<String[]> listDataFields = new ArrayList<String[]>();
				
				String sql = "select TZ_XXX_MC,TZ_XXX_CCLX,TZ_XXX_BH,TZ_COM_LMC from PS_TZ_DCWJ_XXXPZ_T where TZ_DC_WJ_ID=? and TZ_XXX_CCLX<>'F' and TZ_XXX_CCLX is not null and (TZ_IS_DOWNLOAD is null or TZ_IS_DOWNLOAD <> 'N') order by TZ_ORDER";
				List<Map<String,Object>> wjItemsList =  sqlQuery.queryForList(sql, new Object[]{ wjId });
			
				//生成导出excel标题行
				dataCellKeys.add(new String[]{ "tz-name-20170504","姓名" });
				dataCellKeys.add(new String[]{ "tz-mssqh-20170504","面试申请号" });
				dataCellKeys.add(new String[]{ "tz-status-20170504","完成状态" });
				
				for(Map<String,Object> itemMap: wjItemsList){
					String itemName = itemMap.get("TZ_XXX_MC").toString();
					String storeType = itemMap.get("TZ_XXX_CCLX").toString();
					String itemId = itemMap.get("TZ_XXX_BH").toString();
					String className = itemMap.get("TZ_COM_LMC").toString();
					
					//如果是表格题，按子问题导出
					if("T".equals(storeType)){
						String bgSql = "select TZ_QU_CODE,TZ_QU_NAME from PS_TZ_DCWJ_BGZWT_T where TZ_DC_WJ_ID=? and TZ_XXX_BH=? order by TZ_ORDER";
						List<Map<String,Object>> bgtList =  sqlQuery.queryForList(bgSql, new Object[]{ wjId, itemId });
						
						for(Map<String,Object> bgtMap: bgtList){
							String subQueId = bgtMap.get("TZ_QU_CODE").toString();
							String subQueName = bgtMap.get("TZ_QU_NAME") == null ? "" 
									: bgtMap.get("TZ_QU_NAME").toString();
							
							
							dataCellKeys.add(new String[] { itemId + "-" + subQueId, subQueName });
						}
					}else{
						dataCellKeys.add(new String[] { itemId , itemName });
					}
					
					listDataFields.add(new String[] { itemId , storeType, className });
				}
				
				
				// 数据
				List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
				
				String cyrSql = "select TZ_APP_INS_ID,TZ_DC_WC_STA,PERSON_ID from ps_TZ_DC_INS_T where TZ_DC_WJ_ID=? order by TZ_APP_INS_ID";
				List<Map<String,Object>> cyrList =  sqlQuery.queryForList(cyrSql, new Object[]{ wjId });
				//循环所有调查参与人
				for(Map<String,Object> cyrMap: cyrList){
					Map<String, Object> mapData = new HashMap<String, Object>();
					
					String wjInsId = cyrMap.get("TZ_APP_INS_ID").toString();
					String wjInsSta = cyrMap.get("TZ_DC_WC_STA") == null ? "": cyrMap.get("TZ_DC_WC_STA").toString();
					String cyrOprid = cyrMap.get("PERSON_ID") == null ? "": cyrMap.get("PERSON_ID").toString();
					
					if(!"".equals(cyrOprid)){
						//非匿名调查，导出姓名和面试申请号
						sql = "select TZ_MSSQH from PS_TZ_REG_USER_T where OPRID=?";
						String msSqh = sqlQuery.queryForObject(sql, new Object[]{ cyrOprid }, "String");
						sql = "select TZ_REALNAME from PS_TZ_AQ_YHXX_TBL where OPRID=? limit 1";
						String name = sqlQuery.queryForObject(sql, new Object[]{ cyrOprid }, "String");
						
						mapData.put("tz-name-20170504", name);
						mapData.put("tz-mssqh-20170504", msSqh);
					}else{
						mapData.put("tz-name-20170504", "匿名");
						mapData.put("tz-mssqh-20170504", "");
					}
					
					if("0".equals(wjInsSta)){
						mapData.put("tz-status-20170504", "已完成");
					}else{
						mapData.put("tz-status-20170504", "未完成");
					}
					
					//导出在线调查问题
					for(String[] expField: listDataFields){
						/********************************
						*	信息项存储类型对应查询表
						*	S=>TZ_DC_CC_T.TZ_APP_S_TEXT
						*	L=>TZ_DC_CC_T.TZ_APP_L_TEXT
						*	D=>TZ_APP_DHCC_T
						*	T=>TZ_DCDJ_BGT_T
						********************************/
						
						String itemId = expField[0];
						String storeType = expField[1];
						String className = expField[2];
						String answerSql;
						
						switch(storeType){
						case "S":
							String answerS = "";
							//下拉框和量表题，取描述值
							if("ComboBox".equals(className) || "QuantifyQu".equals(className)){
								String optDescSql = "select B.TZ_XXXKXZ_MS from PS_TZ_DC_CC_T A,PS_TZ_DCWJ_XXKXZ_T B where A.TZ_XXX_BH=B.TZ_XXX_BH  and A.TZ_APP_S_TEXT=B.TZ_XXXKXZ_MC and B.TZ_DC_WJ_ID=? and A.TZ_XXX_BH=? and A.TZ_APP_INS_ID=?";
								answerS = sqlQuery.queryForObject(optDescSql, new Object[]{ wjId, itemId, wjInsId }, "String");
							}else{
								answerSql = "select TZ_APP_S_TEXT from ps_TZ_DC_CC_T where TZ_APP_INS_ID=? and TZ_XXX_BH=?"; 
								answerS = sqlQuery.queryForObject(answerSql, new Object[]{ wjInsId, itemId }, "String");
							}
							mapData.put(itemId, answerS);
							break;
						case "L":
							String answerL = "";
							answerSql = "select TZ_APP_L_TEXT from ps_TZ_DC_CC_T where TZ_APP_INS_ID=? and TZ_XXX_BH=?"; 
							answerL = sqlQuery.queryForObject(answerSql, new Object[]{ wjInsId, itemId }, "String");
							mapData.put(itemId, answerL);
							break;
						case "D":
							String answerD = "";
							answerSql = "select TZ_APP_S_TEXT,TZ_KXX_QTZ from ps_TZ_DC_DHCC_T where TZ_APP_INS_ID=? and TZ_XXX_BH=? and TZ_IS_CHECKED='Y' order by TZ_XXXKXZ_MC";
							List<Map<String,Object>> xztList = sqlQuery.queryForList(answerSql, new Object[]{ wjInsId, itemId });
							for(Map<String,Object> xztMap: xztList){
								String value = xztMap.get("TZ_APP_S_TEXT")== null ? "":xztMap.get("TZ_APP_S_TEXT").toString();
								String otherVal = xztMap.get("TZ_KXX_QTZ")== null ? "":xztMap.get("TZ_KXX_QTZ").toString();
								
								if(!"".equals(answerD)){
									answerD = answerD + "，";
								}
								if(!"".equals(otherVal)){
									answerD = answerD + otherVal;
								}else{
									answerD = answerD + value;
								}
							}
							mapData.put(itemId, answerD);
							break;
						case "T":
							String bgSql = "select TZ_QU_CODE from PS_TZ_DCWJ_BGZWT_T where TZ_DC_WJ_ID=? and TZ_XXX_BH=? order by TZ_ORDER";
							List<Map<String,Object>> bgtList =  sqlQuery.queryForList(bgSql, new Object[]{ wjId, itemId });
							
							for(Map<String,Object> bgtMap: bgtList){
								String answerT = "";
								String subQueId = bgtMap.get("TZ_QU_CODE").toString();
								
								String subQueSql = "select group_concat(TZ_OPT_NAME SEPARATOR '，') from ps_TZ_DCDJ_BGT_T A,PS_TZ_DCWJ_BGKXZ_T B where B.TZ_DC_WJ_ID=? and TZ_APP_INS_ID=? and A.TZ_XXX_BH=B.TZ_XXX_BH and A.TZ_XXXKXZ_XXMC=B.TZ_OPT_CODE and B.TZ_XXX_BH=? and TZ_XXXKXZ_WTMC=? and TZ_APP_S_TEXT='Y'";
								answerT = sqlQuery.queryForObject(subQueSql, new Object[]{ wjId, wjInsId, itemId, subQueId }, "String");
								
								mapData.put(itemId + "-" + subQueId, answerT);
							}
							break;
						}
					}
					dataList.add(mapData);
				}
				
				
				sql = "select TZ_SYSFILE_NAME from PS_TZ_EXCEL_DATT_T where PROCESSINSTANCE=?";
				String strUseFileName = sqlQuery.queryForObject(sql, new Object[]{ processinstance }, "String");
				
				boolean rst = excelHandle.export2Excel(strUseFileName, dataCellKeys, dataList);
				if (rst) {
					String urlExcel = excelHandle.getExportExcelPath();
					try {
						sqlQuery.update("update  PS_TZ_EXCEL_DATT_T set TZ_FWQ_FWLJ = ? where PROCESSINSTANCE=?",
								new Object[] { urlExcel, processinstance });
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
