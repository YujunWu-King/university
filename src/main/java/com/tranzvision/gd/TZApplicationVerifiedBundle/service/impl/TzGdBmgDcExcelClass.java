package com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

			String expDirPath = "", absexpDirPath = "";
			String sql = "SELECT TZ_AUD_LIST ,RUN_CNTL_ID ,TZ_APP_TPL_ID ,TZ_EXPORT_TMP_ID ,TZ_EXCEL_NAME,TZ_REL_URL,TZ_JD_URL FROM PS_TZ_BMB_DCE_T WHERE RUN_CNTL_ID= ?";
			Map<String, Object> map = null;
			try {
				map = jdbcTemplate.queryForMap(sql, new Object[] { runCntlId });
			} catch (Exception e) {
				map = null;
			}
			if (map != null) {
				String excelName = (String) map.get("TZ_EXCEL_NAME");
				String excelTpl = (String) map.get("TZ_EXPORT_TMP_ID");
				String appFormModalID = (String) map.get("TZ_APP_TPL_ID");
				String appInsIdList = (String) map.get("TZ_AUD_LIST");
				expDirPath = (String) map.get("TZ_REL_URL");
				absexpDirPath = (String) map.get("TZ_JD_URL");

				/* 将文件上传之前，先重命名该文件 */
				Date dt = new Date();
				SimpleDateFormat datetimeFormate = new SimpleDateFormat("yyyyMMddHHmmss");
				String sDttm = datetimeFormate.format(dt);

				String strUseFileName = sDttm + "_" + excelName + "." + "xlsx";

				int colum = 0;
				ExcelHandle2 excelHandle = new ExcelHandle2(expDirPath, absexpDirPath);
				List<String[]> dataCellKeys = new ArrayList<String[]>();
				dataCellKeys.add(new String[] { "id" + colum, "序号" });

				String sqlExcelTpl = "SELECT TZ_DC_FIELD_ID,TZ_DC_FIELD_NAME,TZ_DC_FIELD_FGF FROM PS_TZ_EXP_FRMFLD_T WHERE TZ_EXPORT_TMP_ID=? ORDER BY TZ_SORT_NUM ASC";
				List<Map<String, Object>> list = null;
				try {
					list = jdbcTemplate.queryForList(sqlExcelTpl, new Object[] { excelTpl });
				} catch (Exception e) {
					list = null;
				}

				String strFieldID = "", strFieldName = "", strFieldSep = "";
				// 存储模板字段的数组;
				ArrayList<String[]> arrExcelTplField = new ArrayList<>();
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						strFieldID = (String) list.get(i).get("TZ_DC_FIELD_ID");
						strFieldName = (String) list.get(i).get("TZ_DC_FIELD_NAME");
						strFieldSep = (String) list.get(i).get("TZ_DC_FIELD_FGF");
						String[] str = new String[] { strFieldID, strFieldName, strFieldSep };
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

				// Hardcode定义取值视图;
				String appFormInfoViewSQL = "select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT where TZ_HARDCODE_PNT=?";
				String appFormInfoView = "";
				try {
					appFormInfoView = jdbcTemplate.queryForObject(appFormInfoViewSQL,
							new Object[] { "TZ_FORM_VAL_REC" }, String.class);
				} catch (Exception e) {
					appFormInfoView = "";
				}

				for (int i = 0; i < row_count; i++) {
					colum = 0;
					Map<String, Object> mapData = new HashMap<String, Object>();
					mapData.put("id" + colum, String.valueOf(i + 1));

					/* 根据模板配置取出每个报名表的数据 */
					for (int j = 0; j < arrExcelTplField.size(); j++) {
						
						String sqlAppFormField = "SELECT A.TZ_FORM_FLD_ID,A.TZ_CODE_TABLE_ID,B.TZ_XXX_CCLX,B.TZ_COM_LMC,B.TZ_XXX_NO FROM PS_TZ_FRMFLD_GL_T A ,PS_TZ_FORM_FIELD_V B WHERE B.TZ_APP_TPL_ID=? AND B.TZ_XXX_BH =A.TZ_FORM_FLD_ID AND A.TZ_EXPORT_TMP_ID=? AND A.TZ_DC_FIELD_ID=? ORDER BY A.TZ_SORT_NUM ASC";
						List<Map<String, Object>> appFormFieldList = null;
						try {
							appFormFieldList = jdbcTemplate.queryForList(sqlAppFormField,
									new Object[] { appFormModalID, excelTpl, arrExcelTplField.get(j)[0] });
						} catch (Exception e) {
							appFormFieldList = null;
						}

						String strAppFormField = "", strCodeTable = "", strSaveType = "", strComClassName = "",
								strInfoSelectID = ""; /*
														 * 报名表字段，码表，存储类型，控件类名称，
														 * 下拉存储描述信息项编号
														 */
						String strAppFormFieldValues = "";
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
								boolean isRecordValue = false; /* 是否从Hardcode定义的表中取值 */

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
								/* 表存储 */
								if ("R".equals(strSaveType)) {
									strSaveTypeBoolean = false;

									isSingleValue = false;
									isRecordValue = true;
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
										} catch (Exception e) {
											strAppFormFieldValue = "";
										}

									}
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

									if ("bmr".equals(strComClassName.substring(0, 3))) {
										String sql2 = "SELECT "
												+ strSelectField.replaceAll("TZ_APP_S_TEXT", "TZ_APP_L_TEXT")
												+ " FROM PS_TZ_APP_CC_T A WHERE TZ_APP_INS_ID=? AND TZ_XXX_BH=?";
										if (strSelectFieldBoolean == true) {
											try {
												strAppFormFieldValue = jdbcTemplate.queryForObject(
														sql2, new Object[] { strCodeTable,
																Long.parseLong(arrAppInsID[i]), strAppFormField },
														String.class);
											} catch (Exception e) {
												strAppFormFieldValue = "";
											}

										} else {
											try {
												strAppFormFieldValue = jdbcTemplate.queryForObject(sql2, new Object[] {
														Long.parseLong(arrAppInsID[i]), strAppFormField },
														String.class);
											} catch (Exception e) {
												strAppFormFieldValue = "";
											}

										}
									}
								} else {
									if (isRecordValue) {
										if (appFormInfoView != null && !"".equals(appFormInfoView)) {
											String sql3 = "SELECT " + strAppFormField + " FROM " + appFormInfoView
													+ " WHERE TZ_APP_INS_ID=?";
											try {
												strAppFormFieldValue = jdbcTemplate.queryForObject(sql3,
														new Object[] { Long.parseLong(arrAppInsID[i]) }, String.class);
											} catch (Exception e) {
												strAppFormFieldValue = "";
											}

											if (strCodeTable != null && !"".equals(strCodeTable)) {
												try {
													strAppFormFieldValue = jdbcTemplate.queryForObject(
															"SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID=? AND TZ_ZHZ_ID=? AND TZ_EFF_STATUS<>'I' AND TZ_EFF_DATE<=now()",
															new Object[] { strCodeTable, strAppFormFieldValue },
															String.class);
												} catch (Exception e) {
													strAppFormFieldValue = "";
												}
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

						// arr_data.add(strAppFormFieldValues);
						colum = colum + 1;
						mapData.put("id" + colum, strAppFormFieldValues);

					}
					dataList.add(mapData);
					// dealWithExcel.crExcel(arr_data, i + 1, i + 1, row_count +
					// 1, wwb);
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
			}
		} catch (Exception e) {
			try {
				jdbcTemplate.update("UPDATE PSPRCSRQST SET RUNSTATUS=? WHERE PRCSINSTANCE=?",
						new Object[] { "10", processinstance });
			} catch (Exception e1) {
				e.printStackTrace();
			}
		}
	}

}