package com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 原PS：TZ_GD_BMGL_BMBSH_PKG:TZ_GD_BMGL_STU_CLS
 * 
 * @author tang 报名管理-报名表审核-学生列表
 */
@Service("com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl.TzGdBmglStuClsServiceImpl")
public class TzGdBmglStuClsServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;

	// 获取班级信息
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("formData", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			if (jacksonUtil.containsKey("classID")) {
				// 班级编号;
				String strClassID = jacksonUtil.getString("classID");
				// 班级名称，所属项目，所属项目名称;
				String strClassName = "", strProjectID = "", strProjectName = "", strAppModalID = "";

				// 获取班级名称、所属项目，所属项目名称，报名表模板ID;
				String sql = "SELECT TZ_CLASS_NAME,TZ_PRJ_ID,TZ_APP_MODAL_ID FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID=?";
				Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[] { strClassID });
				if (map != null) {
					strClassName = (String) map.get("TZ_CLASS_NAME");
					strProjectID = (String) map.get("TZ_PRJ_ID");
					strAppModalID = (String) map.get("TZ_APP_MODAL_ID");

					if (strProjectID != null && !"".equals(strProjectID)) {
						String prjSql = "SELECT TZ_PRJ_NAME FROM PS_TZ_PRJ_INF_T WHERE TZ_PRJ_ID=?";
						strProjectName = jdbcTemplate.queryForObject(prjSql, new Object[] { strProjectID }, "String");
						if (strProjectName == null) {
							strProjectName = "";
						}
					}

					Map<String, Object> hMap = new HashMap<>();
					hMap.put("classID", strClassID);
					hMap.put("className", strClassName);
					hMap.put("projectName", strProjectName);
					hMap.put("modalID", strAppModalID);
					returnJsonMap.replace("formData", hMap);
				}

			} else {
				errMsg[0] = "1";
				errMsg[1] = "请选择班级";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(returnJsonMap);
	}

	/* 获取学生信息列表 */
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {

			jacksonUtil.json2Map(comParams);
			// 班级编号;
			String strClassID = jacksonUtil.getString("classID");

			// 当前语言环境;
			String strLanguageId = tzLoginServiceImpl.getSysLanaguageCD(request);
			if (strLanguageId == null || "".equals(strLanguageId)) {
				strLanguageId = "ZHS";
			}
			// 当前机构id;
			String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);

			// 报名表状态描述修改;
			String tzAuditStateSQL = "(SELECT IF(B.TZ_ZHZ_DMS IS NULL,A.TZ_ZHZ_DMS,B.TZ_ZHZ_DMS) TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL A LEFT JOIN (SELECT * FROM PS_TZ_PT_ZHZXX_LNG WHERE TZ_LANGUAGE_ID='"
					+ strLanguageId
					+ "') B ON A.TZ_ZHZJH_ID=B.TZ_ZHZJH_ID AND A.TZ_ZHZ_ID=B.TZ_ZHZ_ID WHERE A.TZ_ZHZJH_ID ='TZ_AUDIT_STATE' AND A.TZ_ZHZ_ID=PS_TZ_APP_LIST_VW.TZ_AUDIT_STATE) TZ_AUDIT_STATE_DESC";
			// 报名表评审状态;
			String tzSubmitStateSQL = "(SELECT IF(B.TZ_ZHZ_DMS IS NULL,A.TZ_ZHZ_DMS,B.TZ_ZHZ_DMS) TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL A LEFT JOIN (SELECT * FROM PS_TZ_PT_ZHZXX_LNG WHERE TZ_LANGUAGE_ID='"
					+ strLanguageId
					+ "') B ON A.TZ_ZHZJH_ID=B.TZ_ZHZJH_ID AND A.TZ_ZHZ_ID=B.TZ_ZHZ_ID WHERE A.TZ_ZHZJH_ID ='TZ_APPFORM_STATE' AND A.TZ_ZHZ_ID=PS_TZ_APP_LIST_VW.TZ_SUBMIT_STATE) TZ_SUBMIT_STATE_DESC";
			// 面试管理-面试结果;
			String tzMsResultSQL = "(SELECT IF(B.TZ_ZHZ_DMS IS NULL,A.TZ_ZHZ_DMS,B.TZ_ZHZ_DMS) TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL A LEFT JOIN (SELECT * FROM PS_TZ_PT_ZHZXX_LNG WHERE TZ_LANGUAGE_ID='"
					+ strLanguageId
					+ "') B ON A.TZ_ZHZJH_ID=B.TZ_ZHZJH_ID AND A.TZ_ZHZ_ID=B.TZ_ZHZ_ID WHERE A.TZ_ZHZJH_ID ='TZ_MS_RESULT' AND A.TZ_ZHZ_ID=PS_TZ_APP_LIST_VW.TZ_MS_RESULT) TZ_MS_RESULT_DESC";

			// OPRID，报名表实例ID，学生姓名，提交状态，提交时间，评审状态，颜色类别,面试结果;
			String strOprID = "", strStudentName = "", strSubmitState = "", strSubmitDate = "", strAuditState = "",
					strColorType = "", strInterviewResult = "";

			long appInsID = 0;
			// 学生信息列表sql;
			String sqlStudentList = "";
			if (numLimit == 0) {
				sqlStudentList = "SELECT OPRID ,TZ_REALNAME ,TZ_APP_INS_ID ,TZ_AUDIT_STATE," + tzAuditStateSQL
						+ " ,TZ_COLOR_SORT_ID ,TZ_SUBMIT_STATE," + tzSubmitStateSQL
						+ " ,TZ_SUBMIT_DT_STR ,TZ_MS_RESULT," + tzMsResultSQL
						+ " FROM PS_TZ_APP_LIST_VW WHERE TZ_CLASS_ID=? ORDER BY TZ_APP_INS_ID DESC";
			} else {
				sqlStudentList = "SELECT OPRID ,TZ_REALNAME ,TZ_APP_INS_ID ,TZ_AUDIT_STATE," + tzAuditStateSQL
						+ " ,TZ_COLOR_SORT_ID ,TZ_SUBMIT_STATE," + tzSubmitStateSQL
						+ " ,TZ_SUBMIT_DT_STR ,TZ_MS_RESULT," + tzMsResultSQL
						+ " FROM PS_TZ_APP_LIST_VW WHERE TZ_CLASS_ID=? ORDER BY TZ_APP_INS_ID DESC LIMIT " + numStart
						+ "," + numLimit;
			}

			// 报名表模板;
			String strBmbTplSQL = "SELECT TZ_APP_MODAL_ID FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID=?";
			String strBmbTpl = jdbcTemplate.queryForObject(strBmbTplSQL, new Object[] { strClassID }, "String");
			if (strBmbTpl == null) {
				strBmbTpl = "";
			}

			// Hardcode定义取值视图;
			String viewNameSQL = "select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT where TZ_HARDCODE_PNT=?";
			String appFormInfoView = jdbcTemplate.queryForObject(viewNameSQL, new Object[] { "TZ_FORM_VAL_REC" },
					"String");

			// 报名表审批学生列表模板;
			String strAuditGridTplIDSQL = "SELECT B.TZ_EXPORT_TMP_ID FROM PS_TZ_CLASS_INF_T A,PS_TZ_EXPORT_TMP_T B WHERE A.TZ_APP_MODAL_ID=B.TZ_APP_MODAL_ID AND A.TZ_JG_ID = B.TZ_JG_ID AND A.TZ_CLASS_ID=? AND A.TZ_JG_ID=? AND B.TZ_EXP_TMP_STATUS='A' AND B.TZ_EXPORT_TMP_TYPE='1'";
			String strAuditGridTplID = jdbcTemplate.queryForObject(strAuditGridTplIDSQL,
					new Object[] { strClassID, orgId }, "String");
			if (strAuditGridTplID == null) {
				strAuditGridTplID = "";
			}

			List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlStudentList, new Object[] { strClassID });
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					strOprID = (String) list.get(i).get("OPRID");
					strStudentName = (String) list.get(i).get("TZ_REALNAME");
					appInsID = Long.valueOf(list.get(i).get("TZ_APP_INS_ID").toString());
					// strAuditState =
					// (String)list.get(i).get("TZ_AUDIT_STATE");
					strAuditState = (String) list.get(i).get("TZ_AUDIT_STATE_DESC");
					strColorType = (String) list.get(i).get("TZ_COLOR_SORT_ID");
					// strSubmitState =
					// (String)list.get(i).get("TZ_SUBMIT_STATE");
					strSubmitState = (String) list.get(i).get("TZ_SUBMIT_STATE_DESC");
					strSubmitDate = (String) list.get(i).get("TZ_SUBMIT_DT_STR");
					// strInterviewResult =
					// (String)list.get(i).get("TZ_MS_RESULT");
					strInterviewResult = (String) list.get(i).get("TZ_MS_RESULT_DESC");

					Map<String, Object> strGridColumnStoreMap = new HashMap<>();
					;
					if (strAuditGridTplID != null && !"".equals(strAuditGridTplID)) {
						/* 将模板中需要显示的数据全部查出存入数组 */
						String strInfoID = "", strInfoValue = "", strInfoDesc = "", strComClassName = "",
								strInfoSelectID = ""; /* 控件类名称，下拉存储描述信息项编号 */
						ArrayList<String[]> arrAppFormInfoData = new ArrayList<>();

						// 单行存储;
						String sqlAppFormDataSingle = "SELECT A.TZ_XXX_BH ,if(B.TZ_XXX_CCLX = 'S',A.TZ_APP_S_TEXT ,if(B.TZ_XXX_CCLX = 'L',A.TZ_APP_L_TEXT,'')) TZ_XXX_CCLX,A.TZ_APP_L_TEXT,B.TZ_COM_LMC,B.TZ_XXX_NO FROM PS_TZ_APP_CC_T A ,PS_TZ_TEMP_FIELD_V B WHERE B.TZ_APP_TPL_ID=? AND A.TZ_XXX_BH = B.TZ_XXX_BH AND A.TZ_APP_INS_ID =? AND B.TZ_XXX_BH IN(SELECT TZ_FORM_FLD_ID FROM PS_TZ_FRMFLD_GL_T WHERE TZ_EXPORT_TMP_ID=?)";
						List<Map<String, Object>> appFormDataSingleList = jdbcTemplate.queryForList(
								sqlAppFormDataSingle, new Object[] { strBmbTpl, appInsID, strAuditGridTplID });
						if (appFormDataSingleList != null) {
							for (int j = 0; j < appFormDataSingleList.size(); j++) {
								strInfoID = (String) appFormDataSingleList.get(j).get("TZ_XXX_BH");
								strInfoValue = (String) appFormDataSingleList.get(j).get("TZ_XXX_CCLX");
								strInfoDesc = (String) appFormDataSingleList.get(j).get("TZ_APP_L_TEXT");
								strComClassName = (String) appFormDataSingleList.get(j).get("TZ_COM_LMC");
								strInfoSelectID = (String) appFormDataSingleList.get(j).get("TZ_XXX_NO");
								if ("Select".equals(strComClassName) || "bmrBatch".equals(strComClassName)
										|| "bmrMajor".equals(strComClassName) || "CompanyNature".equals(strComClassName)
										|| "Degree".equals(strComClassName) || "Diploma".equals(strComClassName)) {
									String msSQL = "SELECT TZ_XXXKXZ_MS FROM PS_TZ_APPXXX_KXZ_T WHERE TZ_APP_TPL_ID = ? AND TZ_XXX_BH = ? AND TZ_XXXKXZ_MC = ?";
									strInfoValue = jdbcTemplate.queryForObject(msSQL,
											new Object[] { strBmbTpl, strInfoSelectID, strInfoValue }, "String");
								}

								if ("bmr".equals(strComClassName.substring(0, 3))) {
									strInfoValue = strInfoDesc;
								}

								arrAppFormInfoData.add(new String[] { strInfoID, strInfoValue });
							}
						}

						// 多行存储;
						String sqlAppFormDataMulti = " SELECT TZ_XXX_BH FROM PS_TZ_TEMP_FIELD_V WHERE TZ_APP_TPL_ID=? AND TZ_XXX_CCLX='D' AND TZ_XXX_BH IN(SELECT TZ_FORM_FLD_ID FROM PS_TZ_FRMFLD_GL_T WHERE TZ_EXPORT_TMP_ID=?)";
						List<Map<String, Object>> appFormDataMultiList = jdbcTemplate.queryForList(sqlAppFormDataMulti,
								new Object[] { strBmbTpl, strAuditGridTplID });
						if (appFormDataMultiList != null) {
							for (int j = 0; j < appFormDataMultiList.size(); j++) {
								strInfoID = (String) appFormDataMultiList.get(j).get("TZ_XXX_BH");
								String strInfoValueAll = "";
								String sqlMultiValue = "SELECT CONCAT(TZ_APP_S_TEXT,TZ_KXX_QTZ) TEXT_QTZ FROM PS_TZ_APP_DHCC_T WHERE TZ_APP_INS_ID=? AND TZ_XXX_BH=? AND TZ_IS_CHECKED='Y'";
								List<Map<String, Object>> multiValueList = jdbcTemplate.queryForList(sqlMultiValue,
										new Object[] { appInsID, strInfoID });
								if (multiValueList != null) {
									for (int k = 0; k < multiValueList.size(); k++) {
										strInfoValue = (String) multiValueList.get(k).get("TEXT_QTZ");
										if ("".equals(strInfoValueAll)) {
											strInfoValueAll = strInfoValue;
										} else {
											strInfoValueAll = strInfoValueAll + "," + strInfoValue;
										}
									}
								}

								arrAppFormInfoData.add(new String[] { strInfoID, strInfoValueAll });
							}
						}

						// Hardcode定义视图取值;
						if (appFormInfoView != null && !"".equals(appFormInfoView)) {
							String sqlAppFormDataView = "SELECT TZ_XXX_BH FROM PS_TZ_FORM_FIELD_V WHERE TZ_APP_TPL_ID=? AND TZ_XXX_CCLX='R' AND TZ_XXX_BH IN( SELECT TZ_FORM_FLD_ID FROM PS_TZ_FRMFLD_GL_T WHERE TZ_EXPORT_TMP_ID=?)";
							List<Map<String, Object>> appFormDataViewList = jdbcTemplate
									.queryForList(sqlAppFormDataView, new Object[] { strBmbTpl, strAuditGridTplID });
							if (appFormDataViewList != null) {
								for (int j = 0; j < appFormDataViewList.size(); j++) {
									strInfoID = (String) appFormDataViewList.get(j).get("TZ_XXX_BH");
									String sql = "SELECT " + strInfoID + " FROM " + appFormInfoView
											+ " WHERE TZ_APP_INS_ID=?";
									String strInfoValueAll = jdbcTemplate.queryForObject(sql, new Object[] { appInsID },
											"String");
									arrAppFormInfoData.add(new String[] { strInfoID, strInfoValueAll });
								}
							}
						}

						/* 查询grid需要显示的报名表字段并拼装模板数据 */
						String strColumnID = "", strColumnSpe = "", strColumnFieldID = "", strColumnFieldCodeTable = "";
						String sqlGridColumn = "SELECT TZ_DC_FIELD_ID ,TZ_DC_FIELD_NAME ,if(TZ_DC_FIELD_FGF='',',',if(TZ_DC_FIELD_FGF is null,',',TZ_DC_FIELD_FGF)) TZ_DC_FIELD_FGF FROM PS_TZ_EXP_FRMFLD_T WHERE TZ_EXPORT_TMP_ID=? ORDER BY TZ_SORT_NUM ASC";
						List<Map<String, Object>> gridColumnList = jdbcTemplate.queryForList(sqlGridColumn,
								new Object[] { strAuditGridTplID });
						if (gridColumnList != null) {
							for (int j = 0; j < gridColumnList.size(); j++) {
								strColumnID = (String) gridColumnList.get(j).get("TZ_DC_FIELD_ID");
								// strColumnName =
								// (String)gridColumnList.get(j).get("TZ_DC_FIELD_NAME");
								strColumnSpe = (String) gridColumnList.get(j).get("TZ_DC_FIELD_FGF");

								String strColumnValue = ""; /* 当前考生对应当前列的值 */
								String sqlGridColumnField = "SELECT TZ_FORM_FLD_ID,TZ_CODE_TABLE_ID FROM PS_TZ_FRMFLD_GL_T WHERE TZ_EXPORT_TMP_ID=? AND TZ_DC_FIELD_ID=? ORDER BY TZ_SORT_NUM ASC";
								List<Map<String, Object>> gridColumnFieldList = jdbcTemplate.queryForList(
										sqlGridColumnField, new Object[] { strAuditGridTplID, strColumnID });
								if (gridColumnFieldList != null) {
									for (int k = 0; k < gridColumnFieldList.size(); k++) {
										strColumnFieldID = (String) gridColumnFieldList.get(k).get("TZ_FORM_FLD_ID");
										strColumnFieldCodeTable = (String) gridColumnFieldList.get(k)
												.get("TZ_CODE_TABLE_ID");

										String strColumnFieldValue = "";
										for (int i8 = 0; i8 < arrAppFormInfoData.size(); i8++) {
											if (strColumnFieldID.equals(arrAppFormInfoData.get(i8)[0])) {
												strColumnFieldValue = arrAppFormInfoData.get(i8)[1];
												break;
											}
										}

										if (strColumnFieldValue != null && !"".equals(strColumnFieldValue)
												&& strColumnFieldCodeTable != null
												&& !"".equals(strColumnFieldCodeTable)) {
											String tzZhzDmsSQL = "SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID=? AND TZ_ZHZ_ID=? AND TZ_EFF_STATUS<>'I'";
											strColumnFieldValue = jdbcTemplate.queryForObject(tzZhzDmsSQL,
													new Object[] { strColumnFieldCodeTable, strColumnFieldValue },
													"String");
										}

										if (strColumnValue == null || "".equals(strColumnValue)) {
											strColumnValue = strColumnFieldValue;
										} else {
											strColumnValue = strColumnValue + strColumnSpe + strColumnFieldValue;
										}

									}
								}

								strGridColumnStoreMap.put(strColumnID, strColumnValue);
							}
						}
					}

					if (strGridColumnStoreMap == null || strGridColumnStoreMap.isEmpty()) {
						strGridColumnStoreMap.put("hasData", false);
					}

					strGridColumnStoreMap.put("classID", strClassID);
					strGridColumnStoreMap.put("oprID", strOprID);
					strGridColumnStoreMap.put("appInsID", appInsID);
					strGridColumnStoreMap.put("stuName", strStudentName);
					strGridColumnStoreMap.put("submitState", strSubmitState);
					strGridColumnStoreMap.put("submitDate", strSubmitDate);
					strGridColumnStoreMap.put("auditState", strAuditState);
					strGridColumnStoreMap.put("colorType", strColorType);
					strGridColumnStoreMap.put("interviewResult", strInterviewResult);

					listData.add(strGridColumnStoreMap);
				}
			}

			// 获取学生信息总数;
			String totalSQL = "SELECT COUNT(1) FROM PS_TZ_FORM_WRK_T WHERE TZ_CLASS_ID=?";
			int numTotal = jdbcTemplate.queryForObject(totalSQL, new Object[] { strClassID }, "Integer");
			mapRet.replace("total", numTotal);
			mapRet.replace("root", listData);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return jacksonUtil.Map2json(mapRet);
	}

	/* 修改学生类别信息 */
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {

		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		if (actData.length == 0) {
			return jacksonUtil.Map2json(returnJsonMap);
		}

		try {

			for (int num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				Map<String, Object> dataMap = jacksonUtil.getMap("data");

				// 班级编号;
				String strClassID = (String) dataMap.get("classID");
				// oprid;
				String strOprID = (String) dataMap.get("oprID");
				// 颜色类别编号;
				String strColorType = (String) dataMap.get("colorType");

				String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
				String updateSQL = "UPDATE PS_TZ_FORM_WRK_T SET TZ_COLOR_SORT_ID=?,ROW_LASTMANT_DTTM=now(),ROW_LASTMANT_OPRID=? WHERE TZ_CLASS_ID=? AND OPRID = ?";
				jdbcTemplate.update(updateSQL, new Object[] { strColorType, oprid, strClassID, strOprID });
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(returnJsonMap);
	}
	
	@Override
	public String tzOther(String oprType, String strParams, String[] errorMsg) {
		String reString = "";
		if("tzLoadGridColumns".equals(oprType)){
			reString = this.tzLoadGridColumns(strParams, errorMsg);
		}
		if("tzLoadExpandData".equals(oprType)){
			reString = this.tzLoadExpandData(strParams, errorMsg);
		}
		if("tzGetEmail".equals(oprType)){
			reString = this.tzGetEmail(strParams, errorMsg);
		}
		
		return reString;
	}

	/* 加载配置的列表项 */
	private String tzLoadGridColumns(String comParams, String[] errorMsg) {
		// 返回值;
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();

		JacksonUtil jacksonUtil = new JacksonUtil();

		try {
			jacksonUtil.json2Map(comParams);
			// 班级编号;
			String strClassID = jacksonUtil.getString("classID");
			String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			// 报名表审批学生列表模板;
			String strAuditGridTplIDSQL = "SELECT B.TZ_EXPORT_TMP_ID FROM PS_TZ_CLASS_INF_T A,PS_TZ_EXPORT_TMP_T B WHERE A.TZ_APP_MODAL_ID=B.TZ_APP_MODAL_ID AND A.TZ_JG_ID = B.TZ_JG_ID AND A.TZ_CLASS_ID=? AND A.TZ_JG_ID=? AND B.TZ_EXP_TMP_STATUS='A' AND B.TZ_EXPORT_TMP_TYPE='1'";
			String strAuditGridTplID = jdbcTemplate.queryForObject(strAuditGridTplIDSQL,
					new Object[] { strClassID, orgId }, "String");

			if (strAuditGridTplID != null && !"".equals(strAuditGridTplID)) {
				String sqlAuditGridColumn = "SELECT TZ_DC_FIELD_ID,TZ_DC_FIELD_NAME,TZ_DC_COL_WIDTH,TZ_DC_COL_FILTER FROM PS_TZ_EXP_FRMFLD_T WHERE TZ_EXPORT_TMP_ID=? ORDER BY TZ_SORT_NUM ASC";
				List<Map<String, Object>> auditGridColumnList = jdbcTemplate.queryForList(sqlAuditGridColumn,
						new Object[] { strAuditGridTplID });
				String sColumnID = "", sColumnName = "", sColumnFilter = "";
				int nColumnWidth = 0;
				if (auditGridColumnList != null) {
					for (int i = 0; i < auditGridColumnList.size(); i++) {
						sColumnID = (String) auditGridColumnList.get(i).get("TZ_DC_FIELD_ID");
						sColumnName = (String) auditGridColumnList.get(i).get("TZ_DC_FIELD_NAME");
						nColumnWidth = (int) auditGridColumnList.get(i).get("TZ_DC_COL_WIDTH");
						sColumnFilter = (String) auditGridColumnList.get(i).get("TZ_DC_COL_FILTER");
						boolean bl = false;
						Map<String, Object> sFilterContentMap = new HashMap<>();

						Map<String, Object> dynamicColumnsMap = new HashMap<>();
						if ("string".equals(sColumnFilter)) {
							sFilterContentMap.put("type", "string");
							bl = true;
						}
						if ("list".equals(sColumnFilter)) {
							sFilterContentMap.put("type", "list");
							bl = true;
						}
						if ("number".equals(sColumnFilter)) {
							sFilterContentMap.put("type", "number");

							dynamicColumnsMap.put("xtype", "numbercolumn");
							bl = true;
						}
						if ("boolean".equals(sColumnFilter)) {
							sFilterContentMap.put("type", "boolean");
							bl = true;
						}
						if ("date".equals(sColumnFilter)) {
							sFilterContentMap.put("type", "date");
							sFilterContentMap.put("dateFormat", "Y-m-d");
							sFilterContentMap.put("active", true);

							dynamicColumnsMap.put("xtype", "datecolumn");
							dynamicColumnsMap.put("format", "Y-m-d");
							bl = true;
						}
						if (bl != true) {
							sFilterContentMap.put("type", "string");
						}

						if (nColumnWidth == 0) {
							nColumnWidth = 100;
						}

						dynamicColumnsMap.put("dataIndex", sColumnID);
						dynamicColumnsMap.put("text",
								"<span data-qtip=\"" + sColumnName + "\">" + sColumnName + "</span>");
						dynamicColumnsMap.put("width", nColumnWidth);
						dynamicColumnsMap.put("filter", sFilterContentMap);
						dynamicColumnsMap.put("lockable", false);

						listData.add(dynamicColumnsMap);
					}
				}

			}
		} catch (Exception e) {
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		
		if (listData == null || listData.isEmpty()) {
			String text = "";
			if("ENG".equals(tzLoginServiceImpl.getSysLanaguageCD(request))){
				text = "<span style=font-weight:normal>Please configure the template</span>";
			}else{
				text = "<span style=font-weight:normal>请配置模板</span>";
			}
			Map<String, Object> dynamicColumnsMap = new HashMap<>();
			dynamicColumnsMap.put("dataIndex", "null");
			dynamicColumnsMap.put("text",text );
			dynamicColumnsMap.put("width", 600);
			dynamicColumnsMap.put("filter", new HashMap<>());
			dynamicColumnsMap.put("sortable", false);
			dynamicColumnsMap.put("lockable", false);
			listData.add(dynamicColumnsMap);
		}

		return jacksonUtil.List2json(listData);
	}
	
	
	 /*加载扩展信息*/
	private String tzLoadExpandData(String comParams, String[] errorMsg) {
		// 返回值;
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();

		JacksonUtil jacksonUtil = new JacksonUtil();

		try {
			jacksonUtil.json2Map(comParams);
			// 班级编号;
			String strClassID = jacksonUtil.getString("classID");
			// 报名表编号;
			long strAppInsID = Long.parseLong(jacksonUtil.getString("appInsID"));
		    
		    // 报名表审批摘要模板;
		    String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		    String strAuditGridTplIDSQL = "SELECT B.TZ_EXPORT_TMP_ID FROM PS_TZ_CLASS_INF_T A,PS_TZ_EXPORT_TMP_T B WHERE A.TZ_APP_MODAL_ID=B.TZ_APP_MODAL_ID AND A.TZ_JG_ID = B.TZ_JG_ID AND A.TZ_CLASS_ID=? AND A.TZ_JG_ID=? AND B.TZ_EXP_TMP_STATUS='A' AND B.TZ_EXPORT_TMP_TYPE='2'";
		    String strAuditGridTplID = jdbcTemplate.queryForObject(strAuditGridTplIDSQL, new Object[]{strClassID,orgId},"String");

		    // 标签;
		    String sqlTag = "SELECT TZ_LABEL_ID,(SELECT TZ_LABEL_NAME FROM PS_TZ_LABEL_DFN_T WHERE TZ_LABEL_ID=A.TZ_LABEL_ID ) TZ_LABEL_NAME FROM PS_TZ_FORM_LABEL_T A WHERE TZ_APP_INS_ID=?";
		    String strTagName = "", strTagNameContent = "";
		    List<Map<String, Object>> tagList = jdbcTemplate.queryForList(sqlTag,new Object[]{strAppInsID});
		    if(tagList != null){
		    	for(int i = 0; i < tagList.size(); i++){
		    		//strTagId = (String)tagList.get(i).get("TZ_LABEL_ID");
		    		strTagName = (String)tagList.get(i).get("TZ_LABEL_NAME");
		    		//strTagNameContent = strTagNameContent + tzGdObject.getHTMLText("HTML.TZApplicationVerifiedBundle.TZ_GD_TAG_DISPLAY_HTML", true,strTagName);
		    		strTagNameContent = strTagNameContent + "<li class=\"x-tagfield-item\"><div class=\"x-tagfield-item-text\" style=\"padding-right:4px;\">"+strTagName+"</div></li>";
		    	}
		    }
		    Map<String, Object> strGridExpandDataMap = new HashMap<>();
		    strGridExpandDataMap.put("itemName","标签");
		    strGridExpandDataMap.put("itemValue", strTagNameContent);
		    listData.add(strGridExpandDataMap);
		    
		    if(strAuditGridTplID != null && !"".equals(strAuditGridTplID)){
		    	// Hardcode定义取值视图;
				String viewNameSQL = "select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT where TZ_HARDCODE_PNT=?";
				String appFormInfoView = jdbcTemplate.queryForObject(viewNameSQL, new Object[] { "TZ_FORM_VAL_REC" },
						"String");
				
				/*将模板中需要显示的数据全部查出存入数组*/
				String strBmbTpl = "", strInfoID = "", strInfoValue = "", strInfoDesc = "", strComClassName = "", strInfoSelectID = ""; /*控件类名称，下拉存储描述信息项编号*/
				strBmbTpl = jdbcTemplate.queryForObject("SELECT TZ_APP_TPL_ID FROM PS_TZ_APP_INS_T WHERE TZ_APP_INS_ID=?", new Object[]{strAppInsID},"String");
				
				ArrayList<String[]> arrAppFormInfoData = new ArrayList<>();

				// 单行存储;
				String sqlAppFormDataSingle = "SELECT A.TZ_XXX_BH ,if(B.TZ_XXX_CCLX = 'S',A.TZ_APP_S_TEXT ,if(B.TZ_XXX_CCLX = 'L',A.TZ_APP_L_TEXT,'')) TZ_XXX_CCLX,A.TZ_APP_L_TEXT,B.TZ_COM_LMC,B.TZ_XXX_NO FROM PS_TZ_APP_CC_T A ,PS_TZ_TEMP_FIELD_V B WHERE B.TZ_APP_TPL_ID=? AND A.TZ_XXX_BH = B.TZ_XXX_BH AND A.TZ_APP_INS_ID =? AND B.TZ_XXX_BH IN(SELECT TZ_FORM_FLD_ID FROM PS_TZ_FRMFLD_GL_T WHERE TZ_EXPORT_TMP_ID=?)";
				List<Map<String, Object>> appFormDataSingleList = jdbcTemplate.queryForList(
						sqlAppFormDataSingle, new Object[] { strBmbTpl, strAppInsID, strAuditGridTplID });
				if (appFormDataSingleList != null) {
					for (int j = 0; j < appFormDataSingleList.size(); j++) {
						strInfoID = (String) appFormDataSingleList.get(j).get("TZ_XXX_BH");
						strInfoValue = (String) appFormDataSingleList.get(j).get("TZ_XXX_CCLX");
						strInfoDesc = (String) appFormDataSingleList.get(j).get("TZ_APP_L_TEXT");
						strComClassName = (String) appFormDataSingleList.get(j).get("TZ_COM_LMC");
						strInfoSelectID = (String) appFormDataSingleList.get(j).get("TZ_XXX_NO");
						if ("Select".equals(strComClassName) || "CompanyNature".equals(strComClassName)
								|| "Degree".equals(strComClassName) || "Diploma".equals(strComClassName)) {
							String msSQL = "SELECT TZ_XXXKXZ_MS FROM PS_TZ_APPXXX_KXZ_T WHERE TZ_APP_TPL_ID = ? AND TZ_XXX_BH = ? AND TZ_XXXKXZ_MC = ?";
							strInfoValue = jdbcTemplate.queryForObject(msSQL,
									new Object[] { strBmbTpl, strInfoSelectID, strInfoValue }, "String");
						}

						if ("bmr".equals(strComClassName.substring(0, 3))) {
							strInfoValue = strInfoDesc;
						}
						arrAppFormInfoData.add(new String[] { strInfoID, strInfoValue });
					}
				} 
				
				// 多行存储;
				String sqlAppFormDataMulti = " SELECT TZ_XXX_BH FROM PS_TZ_TEMP_FIELD_V WHERE TZ_APP_TPL_ID=? AND TZ_XXX_CCLX='D' AND TZ_XXX_BH IN(SELECT TZ_FORM_FLD_ID FROM PS_TZ_FRMFLD_GL_T WHERE TZ_EXPORT_TMP_ID=?)";
				List<Map<String, Object>> appFormDataMultiList = jdbcTemplate.queryForList(sqlAppFormDataMulti,
						new Object[] { strBmbTpl, strAuditGridTplID });
				if (appFormDataMultiList != null) {
					for (int j = 0; j < appFormDataMultiList.size(); j++) {
						strInfoID = (String) appFormDataMultiList.get(j).get("TZ_XXX_BH");
						String strInfoValueAll = "";
						String sqlMultiValue = "SELECT CONCAT(TZ_APP_S_TEXT,TZ_KXX_QTZ) TEXT_QTZ FROM PS_TZ_APP_DHCC_T WHERE TZ_APP_INS_ID=? AND TZ_XXX_BH=? AND TZ_IS_CHECKED='Y'";
						List<Map<String, Object>> multiValueList = jdbcTemplate.queryForList(sqlMultiValue,
								new Object[] { strAppInsID, strInfoID });
						if (multiValueList != null) {
							for (int k = 0; k < multiValueList.size(); k++) {
								strInfoValue = (String) multiValueList.get(k).get("TEXT_QTZ");
								if ("".equals(strInfoValueAll)) {
									strInfoValueAll = strInfoValue;
								} else {
									strInfoValueAll = strInfoValueAll + "," + strInfoValue;
								}
							}
						}

						arrAppFormInfoData.add(new String[] { strInfoID, strInfoValueAll });
					}
				}

				// Hardcode定义视图取值;
				if (appFormInfoView != null && !"".equals(appFormInfoView)) {
					String sqlAppFormDataView = "SELECT TZ_XXX_BH FROM PS_TZ_FORM_FIELD_V WHERE TZ_APP_TPL_ID=? AND TZ_XXX_CCLX='R' AND TZ_XXX_BH IN( SELECT TZ_FORM_FLD_ID FROM PS_TZ_FRMFLD_GL_T WHERE TZ_EXPORT_TMP_ID=?)";
					List<Map<String, Object>> appFormDataViewList = jdbcTemplate
							.queryForList(sqlAppFormDataView, new Object[] { strBmbTpl, strAuditGridTplID });
					if (appFormDataViewList != null) {
						for (int j = 0; j < appFormDataViewList.size(); j++) {
							strInfoID = (String) appFormDataViewList.get(j).get("TZ_XXX_BH");
							String sql = "SELECT " + strInfoID + " FROM " + appFormInfoView
									+ " WHERE TZ_APP_INS_ID=?";
							String strInfoValueAll = jdbcTemplate.queryForObject(sql, new Object[] { strAppInsID },
									"String");
							arrAppFormInfoData.add(new String[] { strInfoID, strInfoValueAll });
						}
					}
				}

				/* 查询grid需要显示的报名表字段并拼装模板数据 */
				String strColumnID = "", strColumnSpe = "", strColumnFieldID = "", strColumnFieldCodeTable = "";
				String sqlGridColumn = "SELECT TZ_DC_FIELD_ID ,TZ_DC_FIELD_NAME ,if(TZ_DC_FIELD_FGF='',',',if(TZ_DC_FIELD_FGF is null,',',TZ_DC_FIELD_FGF)) TZ_DC_FIELD_FGF FROM PS_TZ_EXP_FRMFLD_T WHERE TZ_EXPORT_TMP_ID=? ORDER BY TZ_SORT_NUM ASC";
				List<Map<String, Object>> gridColumnList = jdbcTemplate.queryForList(sqlGridColumn,
						new Object[] { strAuditGridTplID });
				if (gridColumnList != null) {
					for (int j = 0; j < gridColumnList.size(); j++) {
						strColumnID = (String) gridColumnList.get(j).get("TZ_DC_FIELD_ID");
						String strColumnName = (String)gridColumnList.get(j).get("TZ_DC_FIELD_NAME");
						strColumnSpe = (String) gridColumnList.get(j).get("TZ_DC_FIELD_FGF");

						String strColumnValue = ""; /* 当前考生对应当前列的值 */
						String sqlGridColumnField = "SELECT TZ_FORM_FLD_ID,TZ_CODE_TABLE_ID FROM PS_TZ_FRMFLD_GL_T WHERE TZ_EXPORT_TMP_ID=? AND TZ_DC_FIELD_ID=? ORDER BY TZ_SORT_NUM ASC";
						List<Map<String, Object>> gridColumnFieldList = jdbcTemplate.queryForList(
								sqlGridColumnField, new Object[] { strAuditGridTplID, strColumnID });
						if (gridColumnFieldList != null) {
							for (int k = 0; k < gridColumnFieldList.size(); k++) {
								strColumnFieldID = (String) gridColumnFieldList.get(k).get("TZ_FORM_FLD_ID");
								strColumnFieldCodeTable = (String) gridColumnFieldList.get(k)
										.get("TZ_CODE_TABLE_ID");

								String strColumnFieldValue = "";
								for (int i8 = 0; i8 < arrAppFormInfoData.size(); i8++) {
									if (strColumnFieldID.equals(arrAppFormInfoData.get(i8)[0])) {
										strColumnFieldValue = arrAppFormInfoData.get(i8)[1];
										break;
									}
								}

								if (strColumnFieldValue != null && !"".equals(strColumnFieldValue)
										&& strColumnFieldCodeTable != null
										&& !"".equals(strColumnFieldCodeTable)) {
									String tzZhzDmsSQL = "SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID=? AND TZ_ZHZ_ID=? AND TZ_EFF_STATUS<>'I'";
									strColumnFieldValue = jdbcTemplate.queryForObject(tzZhzDmsSQL,
											new Object[] { strColumnFieldCodeTable, strColumnFieldValue },
											"String");
								}

								if (strColumnValue == null || "".equals(strColumnValue)) {
									strColumnValue = strColumnFieldValue;
								} else {
									strColumnValue = strColumnValue + strColumnSpe + strColumnFieldValue;
								}

							}
						}
						strGridExpandDataMap = new HashMap<>();
						strGridExpandDataMap.put("itemName", strColumnName);
						strGridExpandDataMap.put("itemValue",strColumnValue);
						listData.add(strGridExpandDataMap);
					}
				}
				
				
		    }
		      
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		return jacksonUtil.List2json(listData);
	}
	
	/* 加载配置的列表项 */
	private String tzGetEmail(String comParams, String[] errorMsg) {
		// 返回值;
		Map<String, Object> returnMap = new HashMap<>();

		JacksonUtil jacksonUtil = new JacksonUtil();
		String strEmail = "";
		try {
			jacksonUtil.json2Map(comParams);
			// 班级编号;
			String strAppInsID = jacksonUtil.getString("appInsID");
			// 查询邮件地址;
		   strEmail = jdbcTemplate.queryForObject("SELECT TZ_ZY_EMAIL FROM PS_TZ_LXFSINFO_TBL WHERE TZ_LXFS_LY='ZSBM' AND TZ_LYDX_ID=?", new Object[]{strAppInsID},"String");
		   if(strEmail == null){
		    	strEmail = "";
		   }
		}catch(Exception e){
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
			strEmail = "";
		}
		returnMap.put("email", strEmail);
		return jacksonUtil.Map2json(returnMap);
	}
}
