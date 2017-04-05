package com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.jdbc.core.JdbcTemplate;
import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.poi.excel.ExcelHandle2;

public class TzGdBmgDcExcelClass {

	public void tzGdDcBmbExcel(String runCntlId) {
		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
		int processinstance = 0;
		try{
			processinstance = jdbcTemplate.queryForObject("SELECT PRCSINSTANCE FROM PSPRCSRQST where RUN_ID = ? limit 0,1", new Object[]{runCntlId},Integer.class);
		}catch(Exception e){
			e.printStackTrace();
			
			processinstance = 0;
		}

		try {
			
			jdbcTemplate.update("UPDATE PSPRCSRQST SET RUNSTATUS=? WHERE PRCSINSTANCE=?",
						new Object[] { "7", processinstance });
			jdbcTemplate.execute("commit");
			
			String expDirPath = "", absexpDirPath = "";
			String sql = "SELECT TZ_AUD_LIST ,RUN_CNTL_ID ,TZ_APP_TPL_ID ,TZ_EXPORT_TMP_ID ,TZ_EXCEL_NAME,TZ_REL_URL,TZ_JD_URL FROM PS_TZ_BMB_DCE_T WHERE RUN_CNTL_ID= ?";
			Map<String, Object> map = null;
			try {
				
				map = jdbcTemplate.queryForMap(sql, new Object[] { runCntlId });
			} catch (Exception e) {
				map = null;
			}
			if (map != null) {
				//String excelName = (String) map.get("TZ_EXCEL_NAME");
				String excelTpl = (String) map.get("TZ_EXPORT_TMP_ID");
				String appFormModalID = (String) map.get("TZ_APP_TPL_ID");
				String appInsIdList = (String) map.get("TZ_AUD_LIST");
				expDirPath = (String) map.get("TZ_REL_URL");
				absexpDirPath = (String) map.get("TZ_JD_URL");

				/* 将文件上传之前，先重命名该文件 */
				Date dt = new Date();
				SimpleDateFormat datetimeFormate = new SimpleDateFormat("yyyyMMddHHmmss");
				String sDttm = datetimeFormate.format(dt);

				String strUseFileName = sDttm + "_" + (new Random().nextInt(99999 - 10 + 1) + 10000) + "." + "xlsx";

				int colum = 0;
				ExcelHandle2 excelHandle = new ExcelHandle2(expDirPath, absexpDirPath);
				List<String[]> dataCellKeys = new ArrayList<String[]>();
				dataCellKeys.add(new String[] { "id" + colum, "序号" });

				String sqlExcelTpl = "SELECT TZ_DC_FIELD_ID,TZ_DC_FIELD_NAME,TZ_DC_FIELD_FGF,TZ_APPCLS_ID FROM PS_TZ_EXP_FRMFLD_T WHERE TZ_EXPORT_TMP_ID=? ORDER BY TZ_SORT_NUM ASC";
				List<Map<String, Object>> list = null;
				try {
					list = jdbcTemplate.queryForList(sqlExcelTpl, new Object[] { excelTpl });
				} catch (Exception e) {
					list = null;
				}

				String strFieldID = "", strFieldName = "", strFieldSep = "",strAppclsId = "";
				// 存储模板字段的数组;
				ArrayList<String[]> arrExcelTplField = new ArrayList<>();
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						strFieldID = (String) list.get(i).get("TZ_DC_FIELD_ID");
						strFieldName = (String) list.get(i).get("TZ_DC_FIELD_NAME");
						strFieldSep = (String) list.get(i).get("TZ_DC_FIELD_FGF");
						strAppclsId = (String) list.get(i).get("TZ_APPCLS_ID");
						String[] str = new String[] { strFieldID, strFieldName, strFieldSep, strAppclsId };
						colum++;
						dataCellKeys.add(new String[] { "id" + colum, strFieldName });
						arrExcelTplField.add(str);
					}
				}

				/* 将需要导出报名表的编号存入数组 */
				String[] arrAppInsID = appInsIdList.split(",");
				int row_count = arrAppInsID.length; /* 导出的数据的行数 */

				/*
				 * 参数分别代表的意思 参数1：包含数据的数组对象 参数2：当前行行数 参数3：当前行行数 参数4：需要导出的数据的总行数
				 * 参数5：打开的Excel文件对象
				 */
				List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();

				// Hardcode定义页面展现取值视图（该部分数据也可导入到Excel中）;
				String appFormInfoViewSQL = "select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT where TZ_HARDCODE_PNT=?";
				String appFormInfoView = "";
				try {
					appFormInfoView = jdbcTemplate.queryForObject(appFormInfoViewSQL,
							new Object[] { "TZ_FORM_VAL_REC" }, String.class);
				} catch (Exception e) {
					appFormInfoView = "";
				}

				// Hardcode定义导出到Excel取值视图;
				String exportExcelViewSQL = "select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT where TZ_HARDCODE_PNT=?";
				String exportExcelView = "";
				try {
					exportExcelView = jdbcTemplate.queryForObject(exportExcelViewSQL,
							new Object[] { "TZ_FORM_EXL_REC" }, String.class);
				} catch (Exception e) {
					exportExcelView = "";
				}
				
				for (int i = 0; i < row_count; i++) {
					colum = 0;
					Map<String, Object> mapData = new HashMap<String, Object>();
					mapData.put("id" + colum, String.valueOf(i + 1));

					/* 根据模板配置取出每个报名表的数据 */
					for (int j = 0; j < arrExcelTplField.size(); j++) {
						String strAppFormFieldValues = "";
						strAppclsId = arrExcelTplField.get(j)[3];
						if(strAppclsId==null || "".equals(strAppclsId) || "null".equals(strAppclsId)){
							//String sqlAppFormField = "SELECT A.TZ_FORM_FLD_ID,A.TZ_CODE_TABLE_ID,B.TZ_XXX_CCLX,B.TZ_COM_LMC,B.TZ_XXX_NO FROM PS_TZ_FRMFLD_GL_T A ,PS_TZ_FORM_FIELD_V B WHERE B.TZ_APP_TPL_ID=? AND B.TZ_XXX_BH =A.TZ_FORM_FLD_ID AND A.TZ_EXPORT_TMP_ID=? AND A.TZ_DC_FIELD_ID=? ORDER BY A.TZ_SORT_NUM ASC";
							String sqlAppFormField = "SELECT A.TZ_FORM_FLD_ID,A.TZ_CODE_TABLE_ID,B.TZ_XXX_CCLX,B.TZ_COM_LMC,B.TZ_XXX_NO FROM PS_TZ_FRMFLD_GL_T A ,PS_TZ_TEMP_FIELD_V B WHERE B.TZ_APP_TPL_ID=? AND B.TZ_XXX_BH =A.TZ_FORM_FLD_ID AND A.TZ_EXPORT_TMP_ID=? AND A.TZ_DC_FIELD_ID=? ORDER BY A.TZ_SORT_NUM ASC";
							List<Map<String, Object>> appFormFieldList = null;
							try {
								appFormFieldList = jdbcTemplate.queryForList(sqlAppFormField,new Object[] { appFormModalID, excelTpl, arrExcelTplField.get(j)[0] });
							} catch (Exception e) {
								appFormFieldList = null;
							}
							
							//不是报名表字段看看是不是工作表中的字段;
							if(appFormFieldList == null || appFormFieldList.size() == 0){
								sqlAppFormField = "select A.TZ_FORM_FLD_ID,A.TZ_CODE_TABLE_ID,'R' AS TZ_XXX_CCLX,'' AS TZ_COM_LMC,'' AS TZ_XXX_NO FROM PS_TZ_FRMFLD_GL_T A,((SELECT COLUMN_NAME FROM `INFORMATION_SCHEMA`.`COLUMNS`WHERE (`information_schema`.`COLUMNS`.`TABLE_SCHEMA` = DATABASE()) AND TABLE_NAME=?)) B  where B.COLUMN_NAME =A.TZ_FORM_FLD_ID AND A.TZ_EXPORT_TMP_ID=? AND A.TZ_DC_FIELD_ID=? ORDER BY A.TZ_SORT_NUM ASC";
								try {
									appFormFieldList = jdbcTemplate.queryForList(sqlAppFormField,new Object[] { appFormInfoView,excelTpl, arrExcelTplField.get(j)[0] });
								} catch (Exception e) {
									appFormFieldList = null;
								}
							}

							//是否是导出到Excel中的字段;
							if(appFormFieldList == null || appFormFieldList.size() == 0){
								sqlAppFormField = "select A.TZ_FORM_FLD_ID,A.TZ_CODE_TABLE_ID,'E' AS TZ_XXX_CCLX,'' AS TZ_COM_LMC,'' AS TZ_XXX_NO FROM PS_TZ_FRMFLD_GL_T A,((SELECT COLUMN_NAME FROM `INFORMATION_SCHEMA`.`COLUMNS`WHERE (`information_schema`.`COLUMNS`.`TABLE_SCHEMA` = DATABASE()) AND TABLE_NAME=?)) B  where B.COLUMN_NAME =A.TZ_FORM_FLD_ID AND A.TZ_EXPORT_TMP_ID=? AND A.TZ_DC_FIELD_ID=? ORDER BY A.TZ_SORT_NUM ASC";
								try {
									appFormFieldList = jdbcTemplate.queryForList(sqlAppFormField,new Object[] { exportExcelView,excelTpl, arrExcelTplField.get(j)[0] });
								} catch (Exception e) {
									appFormFieldList = null;
								}
							
							}
							
							/*
							 * 报名表字段，码表，存储类型，控件类名称，
							 * 下拉存储描述信息项编号
							 */
							String strAppFormField = "", strCodeTable = "", strSaveType = "", strComClassName = "",
									strInfoSelectID = ""; 
							
							if (appFormFieldList != null && list.size() > 0) {
								for (int k = 0; k < appFormFieldList.size(); k++) {
									
									strAppFormField = (String) appFormFieldList.get(k).get("TZ_FORM_FLD_ID");
									strCodeTable = (String) appFormFieldList.get(k).get("TZ_CODE_TABLE_ID");
									strSaveType = (String) appFormFieldList.get(k).get("TZ_XXX_CCLX");
									strComClassName = (String) appFormFieldList.get(k).get("TZ_COM_LMC");
									strInfoSelectID = (String) appFormFieldList.get(k).get("TZ_XXX_NO");

									String strAppFormFieldValue = "";
									String strSelectField = "";
									// strSelectField执行sql是否要传参数;
									boolean strSelectFieldBoolean = false;

									boolean isSingleValue = true; /* 一个信息项是否对应单个值 */
									boolean isDisplayRecordValue = false; /* 是否是展现在页面中的值 */
									boolean isExportRecordValue = false; /* 是否是导出到Excel的值 */
									
									// 是否其他类型
									boolean strSaveTypeBoolean = true;
									
									/* 短文本和长文本  */
									if ("S".equals(strSaveType)||"L".equals(strSaveType)) {
										strSaveTypeBoolean = false;
										strSelectField = "IF(TZ_APP_L_TEXT='',TZ_APP_S_TEXT,IF(TZ_APP_L_TEXT IS NULL,TZ_APP_S_TEXT,TZ_APP_L_TEXT)) AS TZ_APP_TEXT";
										
										if (strCodeTable != null &&!"".equals(strCodeTable)) {
											strSelectField = "(SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID=? AND (TZ_ZHZ_ID=A.TZ_APP_S_TEXT OR TZ_ZHZ_ID=A.TZ_APP_L_TEXT) AND TZ_EFF_STATUS<>'I' AND TZ_EFF_DATE<=now() ORDER BY TZ_ZHZ_DMS DESC limit 0,1)";
											strSelectFieldBoolean = true;
										}
									}
									/* 可选框 */
									if ("D".equals(strSaveType)) {
										strSaveTypeBoolean = false;
										isSingleValue = false;
									}
									/* 表存储 ：管理端展现数据*/
									if ("R".equals(strSaveType)) {
										strSaveTypeBoolean = false;
										isSingleValue = false;
										isDisplayRecordValue = true;
									}
									/* 表存储 ：导出到Excel数据*/
									if ("E".equals(strSaveType)) {
										strSaveTypeBoolean = false;
										isSingleValue = false;
										isExportRecordValue = true;
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
										sql1 = "SELECT " + strSelectField
												+ " FROM PS_TZ_APP_CC_T A WHERE TZ_APP_INS_ID=? AND TZ_XXX_BH=?";
										if (strSelectFieldBoolean == true) {
											try {
												strAppFormFieldValue = jdbcTemplate
														.queryForObject(
																sql1, new Object[] { strCodeTable,
																		Long.parseLong(arrAppInsID[i]), strAppFormField },
																String.class);
											} catch (Exception e) {
												strAppFormFieldValue = "";
											}

										} else {
											try {
												strAppFormFieldValue = jdbcTemplate.queryForObject(sql1,
														new Object[] { Long.parseLong(arrAppInsID[i]), strAppFormField },
														String.class);
												//没有配置码表，而且是以下控件类名称则从可选值选项里面取描述
												if ("Select".equals(strComClassName) || "CompanyNature".equals(strComClassName)
														|| "Degree".equals(strComClassName) || "Diploma".equals(strComClassName)) {
													try {
														strAppFormFieldValue = jdbcTemplate.queryForObject(
																"SELECT TZ_XXXKXZ_MS FROM PS_TZ_APPXXX_KXZ_T WHERE TZ_APP_TPL_ID = ? AND TZ_XXX_BH = ? AND TZ_XXXKXZ_MC = ?",
																new Object[] { appFormModalID, strInfoSelectID,
																		strAppFormFieldValue },
																String.class);
													} catch (Exception e) {
														strAppFormFieldValue = "";
													}
												}
											} catch (Exception e) {
												strAppFormFieldValue = "";
											}

										}

									} else {
										if (isDisplayRecordValue||isExportRecordValue) {
											
											//数据展现视图取值
											if (appFormInfoView != null && !"".equals(appFormInfoView)&&isDisplayRecordValue) {
												String sql3 = "SELECT " + strAppFormField + " FROM " + appFormInfoView
														+ " WHERE TZ_APP_INS_ID=?";
												try {
													strAppFormFieldValue = jdbcTemplate.queryForObject(sql3,
															new Object[] { Long.parseLong(arrAppInsID[i]) }, String.class);
												} catch (Exception e) {
													strAppFormFieldValue = "";
												}
											}
											
											//导出到Excel视图取值
											if (exportExcelView != null && !"".equals(exportExcelView)&&isExportRecordValue) {
												String sql3 = "SELECT " + strAppFormField + " FROM " + exportExcelView
														+ " WHERE TZ_APP_INS_ID=?";
												try {
													strAppFormFieldValue = jdbcTemplate.queryForObject(sql3,
															new Object[] { Long.parseLong(arrAppInsID[i]) }, String.class);
												} catch (Exception e) {
													strAppFormFieldValue = "";
												}
											}
											
											//获取转换值描述
											if (strCodeTable != null && !"".equals(strCodeTable)&&strAppFormFieldValue != null && !"".equals(strAppFormFieldValue)) {
												try {
													strAppFormFieldValue = jdbcTemplate.queryForObject(
															"SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID=? AND TZ_ZHZ_ID=? AND TZ_EFF_STATUS<>'I' AND TZ_EFF_DATE<=now()",
															new Object[] { strCodeTable, strAppFormFieldValue },
															String.class);
												} catch (Exception e) {
													strAppFormFieldValue = "";
												}
											}
											
										} else {
											// 多选框和单选框;
											String sqlMutilValue = "SELECT TZ_APP_S_TEXT,TZ_KXX_QTZ FROM PS_TZ_APP_DHCC_T WHERE TZ_APP_INS_ID=? AND TZ_XXX_BH=? AND TZ_IS_CHECKED='Y'";
											String strCheckBoxRadioValue = "", strCheckBoxRadioOtherValue = "";
											List<Map<String, Object>> list2 = jdbcTemplate.queryForList(sqlMutilValue,
													new Object[] { Long.parseLong(arrAppInsID[i]), strAppFormField });
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
														strAppFormFieldValue = strAppFormFieldValue + ","
																+ strCheckBoxRadioValue;
													}
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
										if (arrExcelTplField.get(j)[2] != null && !"".equals(arrExcelTplField.get(j)[2])) {
											strAppFormFieldValues = strAppFormFieldValues + arrExcelTplField.get(j)[2]
													+ strAppFormFieldValue;
										} else {
											strAppFormFieldValues = strAppFormFieldValues + "," + strAppFormFieldValue;
										}
									}
								}

							}

							
						}else{
							//根据classid去获取对应的appclass类，并获取类的返回值
							//获得这个类  
							String strGetClsInfo = "SELECT TZ_APPCLS_NAME,TZ_APPCLS_PATH,TZ_APPCLS_METHOD FROM PS_TZ_APPCLS_TBL WHERE TZ_APPCLS_ID = ?";
							Map<String, Object> mapClsInfo = jdbcTemplate.queryForMap(strGetClsInfo,new Object[] {  strAppclsId });
							if(mapClsInfo != null){
								try{
									String strAppClsName = "",strAppClsPath = "",strAppClsMethod = "";
									strAppClsName = (String)mapClsInfo.get("TZ_APPCLS_NAME");
									strAppClsPath = (String)mapClsInfo.get("TZ_APPCLS_PATH");
									strAppClsMethod = (String)mapClsInfo.get("TZ_APPCLS_METHOD");
									Object myMethod = Class.forName(strAppClsPath + "." + strAppClsName).newInstance();  
							        //获得这个类中名叫say的参数为Integer的方法  
							        Method method = myMethod.getClass().getMethod(strAppClsMethod, String.class, String.class);  
							        //调用这个方法，12是参数，String与之类似  
							        strAppFormFieldValues = (String) method.invoke(myMethod, arrAppInsID[i],appFormModalID);
								}catch (Exception e) {
									e.printStackTrace();
								} 
							}
						}
						// arr_data.add(strAppFormFieldValues);
						colum = colum + 1;
						mapData.put("id" + colum, strAppFormFieldValues);
					}
				
					dataList.add(mapData);
				}

				boolean rst = excelHandle.export2Excel(strUseFileName, dataCellKeys, dataList);
				if (rst) {
					String urlExcel = excelHandle.getExportExcelPath();
					try {
						jdbcTemplate.update(
								"update  PS_TZ_EXCEL_DATT_T set TZ_FWQ_FWLJ = ? where PROCESSINSTANCE=?",
								new Object[] { urlExcel, processinstance });
					} catch (Exception e) {

					}

					try {
						jdbcTemplate.update("UPDATE PSPRCSRQST SET RUNSTATUS=? WHERE PRCSINSTANCE=?",
								new Object[] { "9", processinstance });
					} catch (Exception e) {
						e.printStackTrace();
					}
					/*
					 * PsTzExcelDattT psTzExcelDattT =
					 * psTzExcelDattTMapper.selectByPrimaryKey(
					 * processinstance); if(psTzExcelDattT != null){
					 * psTzExcelDattT.setTzFwqFwlj(urlExcel);
					 * psTzExcelDattTMapper.updateByPrimaryKeySelective(
					 * psTzExcelDattT); }
					 * 
					 * Psprcsrqst psprcsrqst =
					 * psprcsrqstMapper.selectByPrimaryKey(processinstance);
					 * if(psprcsrqst != null){ psprcsrqst.setRunstatus("9");
					 * psprcsrqstMapper.updateByPrimaryKey(psprcsrqst); }
					 */
				} else {
					try {
						jdbcTemplate.update("UPDATE PSPRCSRQST SET RUNSTATUS=? WHERE PRCSINSTANCE=?",
								new Object[] { "10", processinstance });
					} catch (Exception e) {
						e.printStackTrace();
					}
					/*
					 * Psprcsrqst psprcsrqst =
					 * psprcsrqstMapper.selectByPrimaryKey(processinstance);
					 * if(psprcsrqst != null){
					 * psprcsrqst.setRunstatus("10");
					 * psprcsrqstMapper.updateByPrimaryKey(psprcsrqst); }
					 */
				}
			}
		} catch (Exception e) {
			try {
				e.printStackTrace();
				jdbcTemplate.update("UPDATE PSPRCSRQST SET RUNSTATUS=? WHERE PRCSINSTANCE=?",
						new Object[] { "10", processinstance });
			} catch (Exception e1) {
				e.printStackTrace();
			}
		}
	}

}
