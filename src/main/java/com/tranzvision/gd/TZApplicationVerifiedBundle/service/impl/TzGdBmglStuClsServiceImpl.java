package com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
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
	@Autowired
	private FliterForm fliterForm;

	// 获取班级信息
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("formData", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			if (jacksonUtil.containsKey("classID") && jacksonUtil.containsKey("batchID")) {
				// 班级编号;
				String strClassID = jacksonUtil.getString("classID");
				// 批次编号;
				String strBatchID = jacksonUtil.getString("batchID");
				// 班级名称，报名表模板编号，批次名称;
				String strClassName = "", strAppModalID = "", strBatchName = "";

				// 获取班级名称，报名表模板ID，批次名称;
				String sql = "SELECT A.TZ_CLASS_NAME,A.TZ_APP_MODAL_ID,B.TZ_BATCH_NAME FROM PS_TZ_CLASS_INF_T A INNER JOIN PS_TZ_CLS_BATCH_T B ON(A.TZ_CLASS_ID=B.TZ_CLASS_ID AND B.TZ_BATCH_ID=?) WHERE A.TZ_CLASS_ID=?";
				Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[] { strBatchID, strClassID });
				// 批次信息不存在
				if(StringUtils.isBlank(strBatchID)) {
					sql = "SELECT A.TZ_CLASS_NAME,A.TZ_APP_MODAL_ID FROM PS_TZ_CLASS_INF_T A WHERE A.TZ_CLASS_ID=?";
					map = jdbcTemplate.queryForMap(sql, new Object[] { strClassID });
				}
				if (map != null) {
					strClassName = (String) map.get("TZ_CLASS_NAME");
					strAppModalID = (String) map.get("TZ_APP_MODAL_ID");
					if(map.containsKey("TZ_BATCH_NAME")) {
						strBatchName = (String) map.get("TZ_BATCH_NAME");
					}

					Map<String, Object> hMap = new HashMap<>();
					hMap.put("classID", strClassID);
					hMap.put("className", strClassName);
					hMap.put("modalID", strAppModalID);
					hMap.put("batchID", strBatchID);
					hMap.put("batchName", strBatchName);
					returnJsonMap.replace("formData", hMap);
				}

			} else {
				errMsg[0] = "1";
				errMsg[1] = "请选择报考方向和批次";
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
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {
			// 当前机构id;
			String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);

			jacksonUtil.json2Map(comParams);
			// 班级编号;
			String strClassID = jacksonUtil.getString("classID");

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

			int leng = 0;
			if (!strBmbTpl.equals("")) {
				// 报名表总页数
				// 情况1：双层报名表
				viewNameSQL = "select count(1) from PS_TZ_APP_XXXPZ_T where  TZ_APP_TPL_ID=? and TZ_COM_LMC=? and TZ_FPAGE_BH !=?";
				leng = jdbcTemplate.queryForObject(viewNameSQL, new Object[] { strBmbTpl, "Page", "" }, "Integer");
				// 情况2：单层报名表
				if (leng == 0) {
					viewNameSQL = "select count(1) from PS_TZ_APP_XXXPZ_T where  TZ_APP_TPL_ID=? and TZ_COM_LMC=?";
					leng = jdbcTemplate.queryForObject(viewNameSQL, new Object[] { strBmbTpl, "Page" }, "Integer");
				}
			}
			// 最后一页不算
			// if (leng > 1) {
			// leng = leng - 1;
			// }
			System.out.println("leng:" + leng);

			// 报名表审批学生列表模板;
			String strAuditGridTplIDSQL = "SELECT B.TZ_EXPORT_TMP_ID FROM PS_TZ_CLASS_INF_T A,PS_TZ_EXPORT_TMP_T B WHERE A.TZ_APP_MODAL_ID=B.TZ_APP_MODAL_ID AND A.TZ_JG_ID = B.TZ_JG_ID AND A.TZ_CLASS_ID=? AND A.TZ_JG_ID=? AND B.TZ_EXP_TMP_STATUS='A' AND B.TZ_EXPORT_TMP_TYPE='1'";
			String strAuditGridTplID = jdbcTemplate.queryForObject(strAuditGridTplIDSQL,
					new Object[] { strClassID, orgId }, "String");
			if (strAuditGridTplID == null) {
				strAuditGridTplID = "";
			}

			// 多行存储;
			String sqlAppFormDataMulti = " SELECT TZ_XXX_BH FROM PS_TZ_TEMP_FIELD_V WHERE TZ_APP_TPL_ID=? AND TZ_XXX_CCLX='D' AND TZ_XXX_BH IN(SELECT TZ_FORM_FLD_ID FROM PS_TZ_FRMFLD_GL_T WHERE TZ_EXPORT_TMP_ID=?)";
			List<Map<String, Object>> appFormDataMultiList = null;
			if (!"".equals(strAuditGridTplID)) {
				appFormDataMultiList = jdbcTemplate.queryForList(sqlAppFormDataMulti,
						new Object[] { strBmbTpl, strAuditGridTplID });
			}

			// 表存储
			String sqlAppFormDataView = "SELECT TZ_XXX_BH FROM PS_TZ_FORM_FIELD_V WHERE TZ_APP_TPL_ID=? AND TZ_XXX_CCLX='R' AND TZ_XXX_BH IN( SELECT TZ_FORM_FLD_ID FROM PS_TZ_FRMFLD_GL_T WHERE TZ_EXPORT_TMP_ID=?)";
			List<Map<String, Object>> appFormDataViewList = null;
			if (!"".equals(strAuditGridTplID)) {
				appFormDataViewList = jdbcTemplate.queryForList(sqlAppFormDataView,
						new Object[] { strBmbTpl, strAuditGridTplID });
			}

			/* 开始执行可配置搜索 */

			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] { { "TZ_APP_INS_ID", "DESC" } };

			// json数据要的结果字段;
			String[] resultFldArray = { "OPRID", "TZ_REALNAME", "TZ_APP_INS_ID", "TZ_MSH_ID", "NATIONAL_ID",
					"TZ_AUDIT_STATE", "TZ_COLOR_SORT_ID", "TZ_SUBMIT_STATE", "TZ_SUBMIT_DT_STR", "TZ_MS_RESULT",
					"TZ_FILL_PROPORTION" };

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, comParams, numLimit, numStart, errorMsg);
			String TZ_FILL_PROPORTION = "";
			if (obj != null) {
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					mapList.put("classID", strClassID);
					mapList.put("oprID", rowList[0]);
					mapList.put("stuName", rowList[1]);
					
					String sqlClue = "select TZ_LEAD_ID from PS_TZ_XSXS_BMB_T where TZ_APP_INS_ID = ?";
					String clueId = jdbcTemplate.queryForObject(sqlClue,
							new Object[] { rowList[2] },"String");
					mapList.put("clueID", clueId);
					
					mapList.put("appInsID", rowList[2]);
					mapList.put("interviewApplicationID", rowList[3]);
					mapList.put("nationalID", rowList[4]);
					mapList.put("auditState", rowList[5]);
					mapList.put("colorType", rowList[6]);
					mapList.put("submitState", rowList[7]);
					mapList.put("submitDate", rowList[8]);

			//		mapList.put("interviewResult", rowList[9]);
					String sql2="select A.TZ_RESULT_CODE from TZ_IMP_MSJG_TBL A,PS_TZ_APP_LIST_VW B where A.TZ_APP_INS_ID=B.TZ_APP_INS_ID and A.TZ_APP_INS_ID=? ";
					String interviewResult=jdbcTemplate.queryForObject(sql2,new Object[]{rowList[2]},"String");
					mapList.put("interviewResult",interviewResult);
					String sql1="select B.TZ_REMARK from PS_TZ_APP_LIST_VW A,PS_TZ_FORM_WRK_T B where A.OPRID=B.OPRID and A.OPRID=?";
					String note=jdbcTemplate.queryForObject(sql1, new Object[]{rowList[0]},"String");
					if(note!=null){
					note=TzGdBmglStuClsServiceImpl.stripHtml(note);
					}
					mapList.put("note",note);

					

					/* 根据模板配置显示报名表信息 */
					String appInsID = rowList[2];
					TZ_FILL_PROPORTION = rowList[10];
					// System.out.println("classID"+strClassID);
					mapList.put("fillProportion", getBMBFillProportion(appInsID, leng, TZ_FILL_PROPORTION));

					if (strAuditGridTplID != null && !"".equals(strAuditGridTplID)) {
						// 将模板中需要显示的数据全部查出存入数组
						String strInfoID = "", strInfoValue = "",
								/* strInfoDesc = "", */ strComClassName = "", strInfoSelectID = "";
						// 控件类名称，下拉存储描述信息项编号;
						ArrayList<String[]> arrAppFormInfoData = new ArrayList<>();

						// 单行存储;
						String sqlAppFormDataSingle = "SELECT A.TZ_XXX_BH ,IF(TZ_APP_L_TEXT='',TZ_APP_S_TEXT,IF(TZ_APP_L_TEXT IS NULL,TZ_APP_S_TEXT,TZ_APP_L_TEXT)) TZ_APP_TEXT,A.TZ_APP_L_TEXT,B.TZ_COM_LMC,B.TZ_XXX_NO FROM PS_TZ_APP_CC_T A ,PS_TZ_TEMP_FIELD_V B WHERE B.TZ_APP_TPL_ID=? AND A.TZ_XXX_BH = B.TZ_XXX_BH AND A.TZ_APP_INS_ID =? AND B.TZ_XXX_BH IN(SELECT TZ_FORM_FLD_ID FROM PS_TZ_FRMFLD_GL_T WHERE TZ_EXPORT_TMP_ID=?)";
						List<Map<String, Object>> appFormDataSingleList = jdbcTemplate.queryForList(
								sqlAppFormDataSingle, new Object[] { strBmbTpl, appInsID, strAuditGridTplID });
						if (appFormDataSingleList != null) {
							for (int j = 0; j < appFormDataSingleList.size(); j++) {
								strInfoID = (String) appFormDataSingleList.get(j).get("TZ_XXX_BH");
								strInfoValue = (String) appFormDataSingleList.get(j).get("TZ_APP_TEXT");
								// strInfoDesc = (String)
								// appFormDataSingleList.get(j).get("TZ_APP_L_TEXT");
								strComClassName = (String) appFormDataSingleList.get(j).get("TZ_COM_LMC");
								strInfoSelectID = (String) appFormDataSingleList.get(j).get("TZ_XXX_NO");
								if ("Select".equals(strComClassName) || "bmrBatch".equals(strComClassName)
										|| "bmrMajor".equals(strComClassName) || "CompanyNature".equals(strComClassName)
										|| "Degree".equals(strComClassName) || "Diploma".equals(strComClassName)) {
									String msSQL = "SELECT TZ_XXXKXZ_MS FROM PS_TZ_APPXXX_KXZ_T WHERE TZ_APP_TPL_ID = ? AND TZ_XXX_BH = ? AND TZ_XXXKXZ_MC = ?";
									String strInfoValueTmp = jdbcTemplate.queryForObject(msSQL,
											new Object[] { strBmbTpl, strInfoSelectID, strInfoValue }, "String");

									if (strInfoValueTmp != null && !"".equals(strInfoValueTmp)) {
										strInfoValue = strInfoValueTmp;
									}
								}

								arrAppFormInfoData.add(new String[] { strInfoID, strInfoValue });
							}
						}

						// 多行存储;
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

						// 查询grid需要显示的报名表字段并拼装模板数据
						String strColumnID = "", strColumnSpe = "", strColumnFieldID = "", strColumnFieldCodeTable = "";
						String sqlGridColumn = "SELECT TZ_DC_FIELD_ID ,TZ_DC_FIELD_NAME ,if(TZ_DC_FIELD_FGF='',',',if(TZ_DC_FIELD_FGF is null,',',TZ_DC_FIELD_FGF)) TZ_DC_FIELD_FGF FROM PS_TZ_EXP_FRMFLD_T WHERE TZ_EXPORT_TMP_ID=? ORDER BY TZ_SORT_NUM ASC";
						List<Map<String, Object>> gridColumnList = jdbcTemplate.queryForList(sqlGridColumn,
								new Object[] { strAuditGridTplID });
						if (gridColumnList != null) {
							for (int j = 0; j < gridColumnList.size(); j++) {
								strColumnID = (String) gridColumnList.get(j).get("TZ_DC_FIELD_ID");

								strColumnSpe = (String) gridColumnList.get(j).get("TZ_DC_FIELD_FGF");
								// 当前考生对应当前列的值
								String strColumnValue = "";
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

								mapList.put(strColumnID, strColumnValue);
							}
						}

					}

					listData.add(mapList);
				}
				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jacksonUtil.Map2json(mapRet);
	}

	// 去除html标签 by linhong
	public static String stripHtml(String content) {
		if (content != null) {
			// <p>段落替换为换行
			content = content.replaceAll("<p .*?>", "");
			// <br><br/>替换为换行
			content = content.replaceAll("<br\\s*/?>", "");
			// 去掉其它的<>之间的东西
			content = content.replaceAll("\\<.*?>", "");

			return content;
		} else {
			return "";
		}
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
		if ("tzLoadGridColumns".equals(oprType)) {
			reString = this.tzLoadGridColumns(strParams, errorMsg);
		}
		if ("tzLoadExpandData".equals(oprType)) {
			reString = this.tzLoadExpandData(strParams, errorMsg);
		}
		if ("tzGetEmail".equals(oprType)) {
			reString = this.tzGetEmail(strParams, errorMsg);
		}
		if ("tzGetSms".equals(oprType)) {
			reString = this.tzGetSms(strParams, errorMsg);
		}
		// 将搜索结果批量打包，此处只返回报名表编号
		if ("tzExportAll".equals(oprType)) {
			reString = this.tzGetAppList(strParams, errorMsg);
		}
		if ("tzGetAppIdAndOprID".equals(oprType)) {
			reString = this.tzGetAppIdAndOprID(strParams, errorMsg);
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
			if ("ENG".equals(tzLoginServiceImpl.getSysLanaguageCD(request))) {
				text = "<span style=font-weight:normal>Please configure the template</span>";
			} else {
				text = "<span style=font-weight:normal>请配置模板</span>";
			}
			Map<String, Object> dynamicColumnsMap = new HashMap<>();
			dynamicColumnsMap.put("dataIndex", "null");
			dynamicColumnsMap.put("text", text);
			dynamicColumnsMap.put("width", 600);
			dynamicColumnsMap.put("filter", new HashMap<>());
			dynamicColumnsMap.put("sortable", false);
			dynamicColumnsMap.put("lockable", false);
			listData.add(dynamicColumnsMap);
		}

		return jacksonUtil.List2json(listData);
	}

	/* 加载扩展信息 */
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
			String strAuditGridTplID = jdbcTemplate.queryForObject(strAuditGridTplIDSQL,
					new Object[] { strClassID, orgId }, "String");

			// 标签;
			String sqlTag = "SELECT TZ_LABEL_ID,(SELECT TZ_LABEL_NAME FROM PS_TZ_LABEL_DFN_T WHERE TZ_LABEL_ID=A.TZ_LABEL_ID ) TZ_LABEL_NAME FROM PS_TZ_FORM_LABEL_T A WHERE TZ_APP_INS_ID=?";
			String strTagName = "", strTagNameContent = "";
			List<Map<String, Object>> tagList = jdbcTemplate.queryForList(sqlTag, new Object[] { strAppInsID });
			if (tagList != null) {
				for (int i = 0; i < tagList.size(); i++) {
					// strTagId = (String)tagList.get(i).get("TZ_LABEL_ID");
					strTagName = (String) tagList.get(i).get("TZ_LABEL_NAME");
					// strTagNameContent = strTagNameContent +
					// tzGdObject.getHTMLText("HTML.TZApplicationVerifiedBundle.TZ_GD_TAG_DISPLAY_HTML",
					// true,strTagName);
					strTagNameContent = strTagNameContent
							+ "<li class=\"x-tagfield-item\"><div class=\"x-tagfield-item-text\" style=\"padding-right:4px;\">"
							+ strTagName + "</div></li>";
				}
			}
			Map<String, Object> strGridExpandDataMap = new HashMap<>();
			strGridExpandDataMap.put("itemName", "标签");
			strGridExpandDataMap.put("itemValue", strTagNameContent);
			listData.add(strGridExpandDataMap);

			if (strAuditGridTplID != null && !"".equals(strAuditGridTplID)) {
				// Hardcode定义取值视图;
				String viewNameSQL = "select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT where TZ_HARDCODE_PNT=?";
				String appFormInfoView = jdbcTemplate.queryForObject(viewNameSQL, new Object[] { "TZ_FORM_VAL_REC" },
						"String");

				/* 将模板中需要显示的数据全部查出存入数组 */
				String strBmbTpl = "", strInfoID = "", strInfoValue = "",
						/* strInfoDesc = "", */ strComClassName = "",
						strInfoSelectID = ""; /* 控件类名称，下拉存储描述信息项编号 */
				strBmbTpl = jdbcTemplate.queryForObject(
						"SELECT TZ_APP_TPL_ID FROM PS_TZ_APP_INS_T WHERE TZ_APP_INS_ID=?", new Object[] { strAppInsID },
						"String");

				ArrayList<String[]> arrAppFormInfoData = new ArrayList<>();

				// 单行存储;
				String sqlAppFormDataSingle = "SELECT A.TZ_XXX_BH ,IF(TZ_APP_L_TEXT='',TZ_APP_S_TEXT,IF(TZ_APP_L_TEXT IS NULL,TZ_APP_S_TEXT,TZ_APP_L_TEXT)) TZ_APP_TEXT,A.TZ_APP_L_TEXT,B.TZ_COM_LMC,B.TZ_XXX_NO FROM PS_TZ_APP_CC_T A ,PS_TZ_TEMP_FIELD_V B WHERE B.TZ_APP_TPL_ID=? AND A.TZ_XXX_BH = B.TZ_XXX_BH AND A.TZ_APP_INS_ID =? AND B.TZ_XXX_BH IN(SELECT TZ_FORM_FLD_ID FROM PS_TZ_FRMFLD_GL_T WHERE TZ_EXPORT_TMP_ID=?)";
				List<Map<String, Object>> appFormDataSingleList = jdbcTemplate.queryForList(sqlAppFormDataSingle,
						new Object[] { strBmbTpl, strAppInsID, strAuditGridTplID });
				if (appFormDataSingleList != null) {
					for (int j = 0; j < appFormDataSingleList.size(); j++) {
						strInfoID = (String) appFormDataSingleList.get(j).get("TZ_XXX_BH");
						strInfoValue = (String) appFormDataSingleList.get(j).get("TZ_APP_TEXT");
						// strInfoDesc = (String)
						// appFormDataSingleList.get(j).get("TZ_APP_L_TEXT");
						strComClassName = (String) appFormDataSingleList.get(j).get("TZ_COM_LMC");
						strInfoSelectID = (String) appFormDataSingleList.get(j).get("TZ_XXX_NO");
						if ("Select".equals(strComClassName) || "CompanyNature".equals(strComClassName)
								|| "Degree".equals(strComClassName) || "Diploma".equals(strComClassName)) {
							String msSQL = "SELECT TZ_XXXKXZ_MS FROM PS_TZ_APPXXX_KXZ_T WHERE TZ_APP_TPL_ID = ? AND TZ_XXX_BH = ? AND TZ_XXXKXZ_MC = ?";
							String strInfoValueTmp = jdbcTemplate.queryForObject(msSQL,
									new Object[] { strBmbTpl, strInfoSelectID, strInfoValue }, "String");

							if (strInfoValueTmp != null && !"".equals(strInfoValueTmp)) {
								strInfoValue = strInfoValueTmp;
							}
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
					List<Map<String, Object>> appFormDataViewList = jdbcTemplate.queryForList(sqlAppFormDataView,
							new Object[] { strBmbTpl, strAuditGridTplID });
					if (appFormDataViewList != null) {
						for (int j = 0; j < appFormDataViewList.size(); j++) {
							strInfoID = (String) appFormDataViewList.get(j).get("TZ_XXX_BH");
							String sql = "SELECT " + strInfoID + " FROM " + appFormInfoView + " WHERE TZ_APP_INS_ID=?";
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
						String strColumnName = (String) gridColumnList.get(j).get("TZ_DC_FIELD_NAME");
						strColumnSpe = (String) gridColumnList.get(j).get("TZ_DC_FIELD_FGF");

						String strColumnValue = ""; /* 当前考生对应当前列的值 */
						String sqlGridColumnField = "SELECT TZ_FORM_FLD_ID,TZ_CODE_TABLE_ID FROM PS_TZ_FRMFLD_GL_T WHERE TZ_EXPORT_TMP_ID=? AND TZ_DC_FIELD_ID=? ORDER BY TZ_SORT_NUM ASC";
						List<Map<String, Object>> gridColumnFieldList = jdbcTemplate.queryForList(sqlGridColumnField,
								new Object[] { strAuditGridTplID, strColumnID });
						if (gridColumnFieldList != null) {
							for (int k = 0; k < gridColumnFieldList.size(); k++) {
								strColumnFieldID = (String) gridColumnFieldList.get(k).get("TZ_FORM_FLD_ID");
								strColumnFieldCodeTable = (String) gridColumnFieldList.get(k).get("TZ_CODE_TABLE_ID");

								String strColumnFieldValue = "";
								for (int i8 = 0; i8 < arrAppFormInfoData.size(); i8++) {
									if (strColumnFieldID.equals(arrAppFormInfoData.get(i8)[0])) {
										strColumnFieldValue = arrAppFormInfoData.get(i8)[1];
										break;
									}
								}

								if (strColumnFieldValue != null && !"".equals(strColumnFieldValue)
										&& strColumnFieldCodeTable != null && !"".equals(strColumnFieldCodeTable)) {
									String tzZhzDmsSQL = "SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID=? AND TZ_ZHZ_ID=? AND TZ_EFF_STATUS<>'I'";
									strColumnFieldValue = jdbcTemplate.queryForObject(tzZhzDmsSQL,
											new Object[] { strColumnFieldCodeTable, strColumnFieldValue }, "String");
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
						strGridExpandDataMap.put("itemValue", strColumnValue);
						listData.add(strGridExpandDataMap);
					}
				}

			}

		} catch (Exception e) {
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
			strEmail = jdbcTemplate.queryForObject(
					"SELECT TZ_ZY_EMAIL FROM PS_TZ_LXFSINFO_TBL WHERE TZ_LXFS_LY='ZSBM' AND TZ_LYDX_ID=?",
					new Object[] { strAppInsID }, "String");
			if (strEmail == null) {
				strEmail = "";
			}
		} catch (Exception e) {
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
			strEmail = "";
		}
		returnMap.put("email", strEmail);
		return jacksonUtil.Map2json(returnMap);
	}
	
	private String tzGetSms(String comParams, String[] errorMsg) {
		// 返回值;
		Map<String, Object> returnMap = new HashMap<>();

		JacksonUtil jacksonUtil = new JacksonUtil();
		String mobile = "";
		try {
			jacksonUtil.json2Map(comParams);
			// 班级编号;
			String strAppInsID = jacksonUtil.getString("appInsID");
			// 查询邮件地址;
			mobile = jdbcTemplate.queryForObject(
					"SELECT TZ_ZY_SJ FROM PS_TZ_LXFSINFO_TBL WHERE TZ_LXFS_LY='ZSBM' AND TZ_LYDX_ID=?",
					new Object[] { strAppInsID }, "String");
			if (mobile == null) {
				mobile = "";
			}
		} catch (Exception e) {
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
			mobile = "";
		}
		returnMap.put("mobile", mobile);
		return jacksonUtil.Map2json(returnMap);
	}

	/**
	 * 返回报名表填写比例
	 * 
	 * @param appInsID
	 * @return
	 */
	public String getBMBFillProportion(String appInsID, int leng, String TZ_FILL_PROPORTION) {

		// System.out.println("appInsID:"+appInsID);
		// System.out.println("leng:"+leng);
		// System.out.println("TZ_FILL_PROPORTION:"+TZ_FILL_PROPORTION);

		String sql = "SELECT TZ_APP_FORM_STA FROM PS_TZ_APP_INS_T WHERE  TZ_APP_INS_ID=?";
		String status = jdbcTemplate.queryForObject(sql, new Object[] { appInsID }, "String");

		// 提交不计算 直接返回100%
		if (status != null && status.equals("U")) {
			if (TZ_FILL_PROPORTION.equals("100.00")) {
				double f = Double.parseDouble(TZ_FILL_PROPORTION);
				return String.format("%.2f", f) + "%";
			} else {
				TZ_FILL_PROPORTION = "100.00";
				sql = "UPDATE PS_TZ_APP_INS_T SET TZ_FILL_PROPORTION=? WHERE TZ_APP_INS_ID=?";
				jdbcTemplate.update(sql, new Object[] { new BigDecimal(TZ_FILL_PROPORTION), appInsID });
				double f = Double.parseDouble(TZ_FILL_PROPORTION);
				return String.format("%.2f", f) + "%";
			}
		}

		// 不是100的需要计算，用户可能修改过
		if (TZ_FILL_PROPORTION == null || TZ_FILL_PROPORTION.equals("") || TZ_FILL_PROPORTION.equals("0.00")
				|| !TZ_FILL_PROPORTION.equals("100.00")) {

			sql = "select count(1) from PS_TZ_APP_COMP_TBL where TZ_APP_INS_ID=? and (TZ_HAS_COMPLETE=? or TZ_HAS_COMPLETE=?) ";
			int fill = jdbcTemplate.queryForObject(sql, new Object[] { appInsID, "Y", "B" }, "Integer");
			// System.out.println("fill:" + fill);
			double f = 0;
			if (leng == 0) {
				f = 0.00;
			} else {
				f = (double) fill / leng;
				f = f * 100;
			}
			// System.out.println("f:"+f);
			sql = "UPDATE PS_TZ_APP_INS_T SET TZ_FILL_PROPORTION=? WHERE TZ_APP_INS_ID=?";
			jdbcTemplate.update(sql, new Object[] { new BigDecimal(f), appInsID });

			return String.format("%.2f", f) + "%";
		} else {
			try {
				double f = Double.parseDouble(TZ_FILL_PROPORTION);
				// System.out.println("f:"+f);
				return String.format("%.2f", f) + "%";
			} catch (Exception e) {
				e.printStackTrace();
				return "0.00%";
			}
		}
	}

	private String tzGetAppList(String comParams, String[] errorMsg) {
		// 返回值;
		Map<String, Object> returnMap = new HashMap<>();

		JacksonUtil jacksonUtil = new JacksonUtil();

		String strAppInsList = "";
		try {
			jacksonUtil.json2Map(comParams);

			String tzStoreParams = jacksonUtil.getString("tzStoreParams");
			String totalCount = jacksonUtil.getString("totalCount");

			int numStart = 0;
			int numLimit = Integer.valueOf(totalCount);

			// 排序字段
			String[][] orderByArr = new String[][] {};

			// json数据要的结果字段
			String[] resultFldArray = { "TZ_APP_INS_ID" };

			// 可配置搜索通用函数
			Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, tzStoreParams, numLimit, numStart,
					errorMsg);

			if (obj != null && obj.length > 0) {
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);

					if ("".equals(strAppInsList)) {
						strAppInsList = rowList[0];
					} else {
						strAppInsList = strAppInsList + ";" + rowList[0];
					}
				}
			}
		} catch (Exception e) {
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
			e.printStackTrace();
		}
		returnMap.put("result", strAppInsList);
		return jacksonUtil.Map2json(returnMap);
	}

	/* 获取AppID及OprID */
	private String tzGetAppIdAndOprID(String comParams, String[] errorMsg) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		String strAppID = "";

		String OprID = "";
		String AppID = "";
		Map<String, Object> returnMap = new HashMap<>();

		try {
			// jacksonUtil.json2Map(comParams);
			// String AppIdSQL = (String) jacksonUtil.getString("getAppIdSQL");

			jacksonUtil.json2Map(comParams);
			String configSearchCondition = (String) comParams;
			String strClassID = "";
			String strBatchID = "";
			if (jacksonUtil.containsKey("condition")) {
				Map<String, Object> conditionJson = jacksonUtil.getMap("condition");
				if (conditionJson != null) {
					for (Map.Entry<String, Object> entry : conditionJson.entrySet()) {
						String key = entry.getKey();
						if (key.indexOf("TZ_CLASS_ID-value") >= 0) {
							strClassID = (String) conditionJson.get(key);
						}
						if (key.indexOf("TZ_BATCH_ID-value") >= 0) {
							strBatchID = (String) conditionJson.get(key);
						}
					}
				}
			}
			String strRet = "";
			String[] resultFldArray = { "OPRID" };
			String[][] orderByArr = new String[][] {};
			String[] errorMessage = { "0" };
			String originalSql = fliterForm.getQuerySQL(resultFldArray, orderByArr, configSearchCondition,
					errorMessage);
			strRet = originalSql;

			if (strClassID.length() > 0) {
				strRet = "SELECT TZ_APP_INS_ID FROM PS_TZ_APP_LIST_VW WHERE OPRID = ANY(" + originalSql
						+ ") AND TZ_CLASS_ID='" + strClassID + "'";
			} else {
				strRet = "SELECT TZ_APP_INS_ID FROM PS_TZ_APP_LIST_VW WHERE OPRID = ANY(" + originalSql + ")";
			}

			if (strBatchID.length() > 0) {
				System.out.println("strBatchID exist");
				strRet = strRet + " AND TZ_BATCH_ID='" + strBatchID + "'";
			}

			String AppIdSQL = strRet.replaceAll("TZ_APP_INS_ID", "OPRID,TZ_APP_INS_ID");

			System.out.println(AppIdSQL);
			List<Map<String, Object>> SqlCon2 = jdbcTemplate.queryForList(AppIdSQL);

			for (Map<String, Object> map2 : SqlCon2) {

				AppID = map2.get("TZ_APP_INS_ID").toString();
				OprID = map2.get("OPRID").toString();

				if (strAppID.equals("")) {
					strAppID = AppID + "+" + OprID;
				} else {
					strAppID = strAppID + ";" + AppID + "+" + OprID;

				}

			}

		} catch (Exception e) {
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
			strAppID = "";
		}

		returnMap.put("AppID", strAppID);
		return jacksonUtil.Map2json(returnMap);

	}

}
