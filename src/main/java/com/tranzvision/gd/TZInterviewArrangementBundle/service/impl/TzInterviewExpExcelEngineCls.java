package com.tranzvision.gd.TZInterviewArrangementBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tranzvision.gd.batch.engine.base.BaseEngine;
import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.poi.excel.ExcelHandle2;
import com.tranzvision.gd.util.sql.SqlQuery;

public class TzInterviewExpExcelEngineCls extends BaseEngine{

	@SuppressWarnings("unchecked")
	@Override
	public void OnExecute() throws Exception {
		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		// jdbctmplate;
		SqlQuery sqlQuery = (SqlQuery) getSpringBeanUtil.getAutowiredSpringBean("SqlQuery");

		// 运行id;
		String runControlId = this.getRunControlID(); 
		
		// 进程id;
		int processinstance = sqlQuery.queryForObject("SELECT TZ_JCSL_ID FROM TZ_JC_SHLI_T where TZ_YUNX_KZID = ? limit 0,1", new Object[] { runControlId },"Integer");
		
		//参数；
		Map<String,Object> paramsMap = sqlQuery.queryForMap("select TZ_REL_URL,TZ_JD_URL,TZ_EXP_PARAMS_STR from PS_TZ_MSARRDCE_AET where RUN_ID=?", new Object[] { runControlId });
		//;
		if (paramsMap != null) {
			String expDirPath = paramsMap.get("TZ_REL_URL").toString();
			String absexpDirPath = paramsMap.get("TZ_JD_URL").toString();
			String expParams = paramsMap.get("TZ_EXP_PARAMS_STR").toString();
			System.out.println("------->"+expDirPath);
			System.out.println("------->"+absexpDirPath);
			System.out.println("------->"+expParams);
			
			JacksonUtil jacksonUtil = new JacksonUtil();
			jacksonUtil.json2Map(expParams);
			
			String excelName = jacksonUtil.getString("excelName");
			String expTmpId = jacksonUtil.getString("excelTpl");//导出模板
			String appFormModalID = jacksonUtil.getString("appFormModalID");//报名表模板
			String classID = jacksonUtil.getString("classID");
			String batchID = jacksonUtil.getString("batchID");
			List<String> msPlanList = (List<String>) jacksonUtil.getList("selList");
			

			// 表头
			List<String[]> dataCellKeys = new ArrayList<String[]>();
			dataCellKeys.add(new String[] { "interviewDate", "面试日期" });
			dataCellKeys.add(new String[] { "interviewTime", "面试时间" });
			
			//导出模板
			String sql = "SELECT TZ_DC_FIELD_ID,TZ_DC_FIELD_NAME,TZ_DC_FIELD_FGF FROM PS_TZ_EXP_FRMFLD_T WHERE TZ_EXPORT_TMP_ID=? ORDER BY TZ_SORT_NUM ASC";
			List<Map<String,Object>> itemsList = sqlQuery.queryForList(sql, new Object[]{ expTmpId });
			
			List<String[]> listDataFields = new ArrayList<String[]>();
			for(Map<String,Object> itemMap : itemsList){
				String filedId = itemMap.get("TZ_DC_FIELD_ID") == null ? "" :  itemMap.get("TZ_DC_FIELD_ID").toString();
				String filedName = itemMap.get("TZ_DC_FIELD_NAME") == null ? "" :  itemMap.get("TZ_DC_FIELD_NAME").toString();
				String filedSep = itemMap.get("TZ_DC_FIELD_FGF") == null ? "" :  itemMap.get("TZ_DC_FIELD_FGF").toString();
				if("".equals(filedId)){
					break;
				}
				if("".equals(filedName)){
					filedName = filedId;
				}
				
				dataCellKeys.add(new String[] { filedId, filedName });
				listDataFields.add(new String[] { filedId, filedSep });
			}
			

			// 数据
			List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
			
			//循环面试计划并导出面试预约学生
			for(String msPlanSeq : msPlanList){
				Map<String, Object> mapData = new HashMap<String, Object>();
				
				sql = "SELECT TZ_MS_DATE,date_format(TZ_START_TM,'%H:%i') AS TZ_START_TM FROM PS_TZ_MSSJ_ARR_TBL WHERE TZ_CLASS_ID=? AND TZ_BATCH_ID=? AND TZ_MS_PLAN_SEQ=?";
				Map<String,Object> msArrMap = sqlQuery.queryForMap(sql, new Object[]{ classID,batchID,msPlanSeq });
				//面试日期
				String strMsDate = "";
				//面试时间
				String strMsTime = "";
				if(msArrMap != null){
					strMsDate = msArrMap.get("TZ_MS_DATE").toString();
					strMsTime = msArrMap.get("TZ_START_TM").toString();
				}

				//面试计划下预约考生
				sql = "select OPRID from PS_TZ_MSYY_KS_TBL where TZ_CLASS_ID=? and TZ_BATCH_ID=? and TZ_MS_PLAN_SEQ=?";
				List<Map<String,Object>> opridList = sqlQuery.queryForList(sql, new Object[]{ classID,batchID,msPlanSeq });
				for(Map<String,Object> opridMap: opridList){
					String oprid = opridMap.get("OPRID").toString();
					//面试日期、时间
					mapData.put("interviewDate", strMsDate);
					mapData.put("interviewTime", strMsTime);
					
					//报名表
					sql = "SELECT TZ_APP_INS_ID FROM PS_TZ_FORM_WRK_T WHERE TZ_CLASS_ID=? AND OPRID=?";
					String appInsId="";
					try{
						appInsId = sqlQuery.queryForObject(sql, new Object[]{ classID,oprid }, "String");
					}catch(Exception e){
						System.out.println("----->报名表ID："+appInsId);
						e.printStackTrace();
					}
					for(String [] itemArr : listDataFields){
						sql="SELECT A.TZ_FORM_FLD_ID,A.TZ_CODE_TABLE_ID,B.TZ_XXX_CCLX,B.TZ_COM_LMC,B.TZ_XXX_NO FROM PS_TZ_FRMFLD_GL_T A ,PS_TZ_FORM_FIELD_V B WHERE B.TZ_APP_TPL_ID=? AND B.TZ_XXX_BH =A.TZ_FORM_FLD_ID AND A.TZ_EXPORT_TMP_ID=? AND A.TZ_DC_FIELD_ID=? ORDER BY A.TZ_SORT_NUM ASC";
						List<Map<String,Object>> appItemList = sqlQuery.queryForList(sql,new Object[]{ appFormModalID,expTmpId, itemArr[0] });
						
							
						//不是报名表字段看看是不是工作表中的字段;
						if(appItemList == null || appItemList.size() == 0){
							sql = "select A.TZ_FORM_FLD_ID,A.TZ_CODE_TABLE_ID,'R' AS TZ_XXX_CCLX,'' AS TZ_COM_LMC,'' AS TZ_XXX_NO FROM PS_TZ_FRMFLD_GL_T A,PS_TZ_FORM_DC_VW B  where B.FILED1 =A.TZ_FORM_FLD_ID AND A.TZ_EXPORT_TMP_ID=? AND A.TZ_DC_FIELD_ID=? ORDER BY A.TZ_SORT_NUM ASC";
							try {
								appItemList = sqlQuery.queryForList(sql,new Object[] { expTmpId, itemArr[0] });
							} catch (Exception e) {
								appItemList = null;
							}
						
						}
						
						/*
						 * 报名表字段，码表，存储类型，控件类名称，
						 * 下拉存储描述信息项编号
						 */
						String strAppFormField = "", 
								strCodeTable = "", 
								strSaveType = "", 
								strComClassName = "",
								strInfoSelectID = ""; 
						
						String strAppFormFieldValues = "";
						if (appItemList != null && itemsList.size() > 0) {
							for (Map<String,Object> formFieldMap : appItemList) {
								strAppFormField = formFieldMap.get("TZ_FORM_FLD_ID").toString();
								strCodeTable = formFieldMap.get("TZ_CODE_TABLE_ID").toString();
								strSaveType = formFieldMap.get("TZ_XXX_CCLX").toString();
								strComClassName = formFieldMap.get("TZ_COM_LMC").toString();
								strInfoSelectID = formFieldMap.get("TZ_XXX_NO").toString();

								String strAppFormFieldValue = "";
								String strSelectField = "";
								// strSelectField执行sql是否要传参数;
								boolean strSelectFieldBoolean = false;

								boolean isSingleValue = true; /* 一个信息项是否对应单个值 */

								// 是否其他类型
								boolean strSaveTypeBoolean = true;
								/* 短文本 */
								if ("S".equals(strSaveType)) {
									strSaveTypeBoolean = false;

									if ("bmr".equals(strComClassName.substring(0, 3))) {
										strSelectField = "TZ_APP_L_TEXT"; /* 报名人相关控件取值从长字符串取描述 */
									} else {
										if (strCodeTable == null || "".equals(strCodeTable)) {
											strSelectField = "TZ_APP_S_TEXT";
										} else {
											strSelectField = "(SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID=? AND TZ_ZHZ_ID=A.TZ_APP_S_TEXT AND TZ_EFF_STATUS<>'I' AND TZ_EFF_DATE<=now())";
											strSelectFieldBoolean = true;
										}
									}
								}
								/* 长文本 */
								if ("L".equals(strSaveType)) {
									strSaveTypeBoolean = false;
									if (strCodeTable == null || "".equals(strCodeTable)) {
										strSelectField = "TZ_APP_L_TEXT";
									} else {
										strSelectField = "(SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID=? AND TZ_ZHZ_ID=A.TZ_APP_L_TEXT AND TZ_EFF_STATUS<>'I' AND TZ_EFF_DATE<=now())";
										strSelectFieldBoolean = true;
									}
								}
								/* 可选框 */
								if ("D".equals(strSaveType)) {
									strSaveTypeBoolean = false;

									isSingleValue = false;
								}
								/* 其他值 */
								if (strSaveTypeBoolean) {
									if (strCodeTable == null || "".equals(strCodeTable)) {
										strSelectField = "TZ_APP_S_TEXT";
									} else {
										strSelectField = "(SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID=? AND TZ_ZHZ_ID=A.TZ_APP_S_TEXT AND TZ_EFF_STATUS<>'I' AND TZ_EFF_DATE <= now())";
										strSelectFieldBoolean = true;
									}
								}

								String sql1 = "";
								if (isSingleValue) {
									sql1 = "SELECT " + strSelectField + " FROM PS_TZ_APP_CC_T A WHERE TZ_APP_INS_ID=? AND TZ_XXX_BH=?";
									if (strSelectFieldBoolean == true) {
										try {
											strAppFormFieldValue = sqlQuery.queryForObject(sql1, new Object[] { strCodeTable,Long.parseLong(appInsId), strAppFormField },"String");
										} catch (Exception e) {
											strAppFormFieldValue = "";
										}

									} else {
										try {
											strAppFormFieldValue = sqlQuery.queryForObject(sql1,new Object[] { Long.parseLong(appInsId), strAppFormField },"String");
										} catch (Exception e) {
											strAppFormFieldValue = "";
										}

									}
									if ("Select".equals(strComClassName) || "CompanyNature".equals(strComClassName)
											|| "Degree".equals(strComClassName) || "Diploma".equals(strComClassName)) {
										try {
											strAppFormFieldValue = sqlQuery.queryForObject("SELECT TZ_XXXKXZ_MS FROM PS_TZ_APPXXX_KXZ_T WHERE TZ_APP_TPL_ID = ? AND TZ_XXX_BH = ? AND TZ_XXXKXZ_MC = ?",new Object[] { appFormModalID, strInfoSelectID,strAppFormFieldValue },"String");
										} catch (Exception e) {
											strAppFormFieldValue = "";
										}

									}

									if ("bmr".equals(strComClassName.substring(0, 3))) {
										String sql2 = "SELECT "
												+ strSelectField.replaceAll("TZ_APP_S_TEXT", "TZ_APP_L_TEXT")
												+ " FROM PS_TZ_APP_CC_T A WHERE TZ_APP_INS_ID=? AND TZ_XXX_BH=?";
										if (strSelectFieldBoolean == true) {
											try {
												strAppFormFieldValue = sqlQuery.queryForObject(sql2, new Object[] { strCodeTable,Long.parseLong(appInsId), strAppFormField },"String");
											} catch (Exception e) {
												strAppFormFieldValue = "";
											}

										} else {
											try {
												strAppFormFieldValue = sqlQuery.queryForObject(sql2, new Object[] {Long.parseLong(appInsId), strAppFormField },"String");
											} catch (Exception e) {
												strAppFormFieldValue = "";
											}

										}
									}
								} else {
										// 多选框和单选框;
										String sqlMutilValue = "SELECT TZ_APP_S_TEXT,TZ_KXX_QTZ FROM PS_TZ_APP_DHCC_T WHERE TZ_APP_INS_ID=? AND TZ_XXX_BH=? AND TZ_IS_CHECKED='Y'";
										String strCheckBoxRadioValue = "", strCheckBoxRadioOtherValue = "";
										List<Map<String, Object>> list2 = sqlQuery.queryForList(sqlMutilValue,new Object[] { Long.parseLong(appInsId), strAppFormField });
										if (list2 != null && list2.size() > 0) {
											for (int h = 0; h < list2.size(); h++) {
												strCheckBoxRadioValue = (String) list2.get(h).get("TZ_APP_S_TEXT");
												if (strCheckBoxRadioValue == null) {
													strCheckBoxRadioValue = "";
												}
												strCheckBoxRadioOtherValue = (String) list2.get(h).get("TZ_KXX_QTZ");
												/* 如果有可选项其他值则取该值 */
												if (strCheckBoxRadioOtherValue != null
														&& !"".equals(strCheckBoxRadioOtherValue)) {
													strCheckBoxRadioValue = strCheckBoxRadioOtherValue;
												}

												if (strAppFormFieldValue == null || "".equals(strAppFormFieldValue)) {
													strAppFormFieldValue = strCheckBoxRadioValue;
												} else {
													strAppFormFieldValue = strAppFormFieldValue + ","+ strCheckBoxRadioValue;
												}
											}
										}
								}

								/* 拼装报名表字段值 */
								if (strAppFormFieldValue == null) {
									strAppFormFieldValue = "";
								}
								if (strAppFormFieldValues == null || "".equals(strAppFormFieldValues)) {
									strAppFormFieldValues = strAppFormFieldValue;
								} else {
									
									if (itemArr[1] != null && !"".equals(itemArr[1])) {
										strAppFormFieldValues = strAppFormFieldValues + itemArr[1]
												+ strAppFormFieldValue;
									} else {
										strAppFormFieldValues = strAppFormFieldValues + "," + strAppFormFieldValue;
									}
								}
							}//end-for
	
							mapData.put(itemArr[0], strAppFormFieldValues);
						}//end-if
					}
					dataList.add(mapData);
				}
			}
			
			
			/* 将文件上传之前，先重命名该文件 */
			Date dt = new Date();
			SimpleDateFormat datetimeFormate = new SimpleDateFormat("yyyyMMddHHmmss");
			String sDttm = datetimeFormate.format(dt);

			String strUseFileName = sDttm + "_" + excelName + "." + "xlsx";
			
			ExcelHandle2 excelHandle = new ExcelHandle2(expDirPath, absexpDirPath);
			boolean rst = excelHandle.export2Excel(strUseFileName, dataCellKeys, dataList);
			if (rst) {
				String urlExcel = excelHandle.getExportExcelPath();
				try {
					sqlQuery.update("update PS_TZ_EXCEL_DATT_T set TZ_FWQ_FWLJ = ? where PROCESSINSTANCE=?",new Object[] { urlExcel, processinstance });
				} catch (Exception e) {

				}

			}
		} 
	}
}
