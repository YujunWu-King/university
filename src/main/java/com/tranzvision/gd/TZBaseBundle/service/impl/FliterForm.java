package com.tranzvision.gd.TZBaseBundle.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * @author tang
 * @version 1.0, 2015-10-13
 * @功能：可配置搜索；原PS类： TZ_GD_CFG:FILTER_FORM
 */
@Service("com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm")
public class FliterForm extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	
	/* 获取组件注册信息 */
	public String tzQuery(String strParams, String[] errorMsg) {
		// 返回值;
		String strRet = "{}";

		try {
			JacksonUtil jacksonUtil = new JacksonUtil();
			// JSONObject CLASSJson = PaseJsonUtil.getJson(strParams);
			// String cfgSrhId = CLASSJson.getString("cfgSrhId");
			jacksonUtil.json2Map(strParams);
			String cfgSrhId = "";
			try{
				cfgSrhId = jacksonUtil.getString("cfgSrhId");
			}catch(Exception e){
				errorMsg[0] = "1";
				errorMsg[1] = "未获取对应的可配置搜索，请于管理员联系";
				return strRet;
			}

			String[] cfgArr = cfgSrhId.split("\\.");
			if (cfgArr == null || cfgArr.length != 3) {
				errorMsg[0] = "1";
				errorMsg[1] = "可配置搜索获取搜索条件失败，请于管理员联系";
				return strRet;
			}

			// 组件ID;
			String comId = cfgArr[0];
			// 页面ID;
			String pageId = cfgArr[1];
			// viewname;
			String viewName = cfgArr[2];

			int tableNameCount = 0;
			String tableName = viewName;
			String tableNameSql = "select COUNT(1) from information_schema.tables where TABLE_NAME=?";

			tableNameCount = jdbcTemplate.queryForObject(tableNameSql, new Object[] { viewName }, "Integer");
			if (tableNameCount <= 0) {
				tableName = "PS_" + viewName;
			}

			// 是否高级模式;
			String TZ_ADVANCE_MODEL = "";
			//long TZ_RESULT_MAX_NUM = 0;
			// 操作符是否只读;
			String operatorReadOnly = "true";
			// 是否存在;
			String isExist = "";
			String sql = "select 'Y' Exist ,TZ_ADVANCE_MODEL,TZ_RESULT_MAX_NUM from PS_TZ_FILTER_DFN_T where TZ_COM_ID=? and TZ_PAGE_ID=? and TZ_VIEW_NAME=?";

			Map<String, Object> map = null;
			try {
				map = jdbcTemplate.queryForMap(sql, new Object[] { comId, pageId, viewName });
				isExist = (String) map.get("Exist");
				TZ_ADVANCE_MODEL = (String) map.get("TZ_ADVANCE_MODEL");
				//TZ_RESULT_MAX_NUM = (long) map.get("TZ_RESULT_MAX_NUM");
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (!"Y".equals(isExist)) {
				errorMsg[0] = "1";
				errorMsg[1] = "找不到对应的配置搜索，请于管理员联系";
				return strRet;
			}

			if ("1".equals(TZ_ADVANCE_MODEL)) {
				operatorReadOnly = "false";
			}

			// 表字段类型;
			// 类型为number的值;
			String intTypeString = "TINYINT,SMALLINT,MEDIUMINT,INT,INTEGER,BIGINT,FLOAT,DOUBLE,DECIMAL";

			ArrayList<String> resultFldTypeList = new ArrayList<String>();
			String resultFldTypeSQL = "SELECT  COLUMN_NAME, DATA_TYPE from information_schema.COLUMNS WHERE TABLE_NAME=?";
			String columnNanme = "";
			String dateType = "";
			try {
				/** 字段类型 **/
				List<Map<String, Object>> list = jdbcTemplate.queryForList(resultFldTypeSQL,
						new Object[] { tableName });
				for (int list_i = 0; list_i < list.size(); list_i++) {
					columnNanme = (String) list.get(list_i).get("COLUMN_NAME");
					//dateType = ((String) list.get(list_i).get("DATA_TYPE")).toUpperCase();
					dateType = (list.get(list_i).get("DATA_TYPE").toString()).toUpperCase();
					resultFldTypeList.add(columnNanme);
					resultFldTypeList.add(dateType);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			String fieldName, fieldLabel, readOnly, hiden, promptTable, promptTableFld, promptTableDescFld, isDowm;
			//long maxRows = 0;
			// 取值类型:TZ_FLT_FLD_QZ_TYPE;
			String fldQzType;
			// 转换值字段:TZ_ZHZJH_ID;
			String zhzJhId;
			// 配置的搜索字段;
			String cfgFldSql = "SELECT TZ_FILTER_FLD,TZ_FILTER_FLD_DESC,TZ_RESULT_MAX_NUM,TZ_FLD_READONLY,TZ_FLD_HIDE,TZ_PROMPT_TBL_NAME,TZ_PROMPT_FLD,TZ_PROMPT_DESC_FLD,TZ_ISDOWN_FLD,TZ_FLT_FLD_QZ_TYPE,TZ_ZHZJH_ID from PS_TZ_FILTER_FLD_T where TZ_COM_ID=? and TZ_PAGE_ID=? and TZ_VIEW_NAME=? order by TZ_SORT_NUM";
			String fldJson = "";
			try {
				/** 字段类型 **/
				List<Map<String, Object>> list = jdbcTemplate.queryForList(cfgFldSql,
						new Object[] { comId, pageId, viewName });
				for (int list_i = 0; list_i < list.size(); list_i++) {
					fieldName = (String) list.get(list_i).get("TZ_FILTER_FLD");
					fieldLabel = (String) list.get(list_i).get("TZ_FILTER_FLD_DESC");
					//maxRows = (long) list.get(list_i).get("TZ_RESULT_MAX_NUM");
					readOnly = (String) list.get(list_i).get("TZ_FLD_READONLY");
					hiden = (String) list.get(list_i).get("TZ_FLD_HIDE");
					promptTable = (String) list.get(list_i).get("TZ_PROMPT_TBL_NAME");
					promptTableFld = (String) list.get(list_i).get("TZ_PROMPT_FLD");
					promptTableDescFld = (String) list.get(list_i).get("TZ_PROMPT_DESC_FLD");
					isDowm = (String) list.get(list_i).get("TZ_ISDOWN_FLD");
					fldQzType = (String) list.get(list_i).get("TZ_FLT_FLD_QZ_TYPE");
					zhzJhId = (String) list.get(list_i).get("TZ_ZHZJH_ID");

					String fldReadOnly = "";
					if ("1".equals(readOnly)) {
						fldReadOnly = "true";
					} else {
						fldReadOnly = "false";
					}

					String fldHidden = "";
					if ("1".equals(hiden)) {
						fldHidden = "true";
					} else {
						fldHidden = "false";
					}

					// 运算符:TZ_FILTER_YSF, 描述:TZ_ZHZ_DMS;
					String filterYsf, zhzDms;
					String trans = "";
					// 搜索操作号;
					String operateSql = "select A.TZ_FILTER_YSF,B.TZ_ZHZ_DMS from PS_TZ_FILTER_YSF_T A ,PS_TZ_PT_ZHZXX_TBL B where A.TZ_COM_ID=? and A.TZ_PAGE_ID=? and A.TZ_VIEW_NAME=? and A.TZ_FILTER_FLD=? and A.TZ_FILTER_BDY_QY='1' AND B.TZ_ZHZJH_ID='TZ_FILTER_YSF' AND A.TZ_FILTER_YSF=B.TZ_ZHZ_ID order by A.TZ_IS_DEF_OPRT desc, A.TZ_FILTER_YSF asc";
					List<Map<String, Object>> operateList = jdbcTemplate.queryForList(operateSql,
							new Object[] { comId, pageId, viewName, fieldName });
					for (int operateList_i = 0; operateList_i < operateList.size(); operateList_i++) {
						filterYsf = (String) operateList.get(operateList_i).get("TZ_FILTER_YSF");
						zhzDms = (String) operateList.get(operateList_i).get("TZ_ZHZ_DMS");
						trans = trans + ",{\"transId\":\"" + filterYsf + "\",\"transDesc\":\"" + zhzDms + "\"}";
					}

					trans = trans.substring(1);

					String promptTableFldLabel = "";
					String promptTableDescFldLabel = "";
					String fldType = "01";
					String promptGlJson = "";

					if ("B".equals(fldQzType)) {
						promptTable = "PS_TZ_CFG_TRS_VW";
						promptTableFld = "TZ_ZHZ_ID";
						promptTableDescFld = "TZ_ZHZ_DMS";
						isDowm = "1";
					}

					if ((promptTable != null && !"".equals(promptTable.trim()))
							&& (promptTableFld != null && !"".equals(promptTableFld.trim()))
							&& (promptTableDescFld != null && !"".equals(promptTableDescFld.trim()))) {
						if ("1".equals(isDowm)) {
							fldType = "06";
						} else {
							fldType = "05";
						}
						/******* promptTable搜索字段的label如何取，TODO ***********/
						promptTableFldLabel = "搜索值";
						promptTableDescFldLabel = "搜索值描述";
						// prompttable的默认搜索字段 TZ_FILTER_GL_FLD;
						String filterGlFld = "";
						String promptDefFldSql = "select TZ_FILTER_GL_FLD from PS_TZ_FLTPRM_FLD_T where TZ_COM_ID=? and TZ_PAGE_ID=? and TZ_VIEW_NAME=? and TZ_FILTER_FLD=? order by TZ_FILTER_ORDER";
						List<Map<String, Object>> promptDefFldList = jdbcTemplate.queryForList(promptDefFldSql,
								new Object[] { comId, pageId, viewName, fieldName });
						for (int promptDefFldList_i = 0; promptDefFldList_i < promptDefFldList
								.size(); promptDefFldList_i++) {
							filterGlFld = (String) promptDefFldList.get(promptDefFldList_i).get("TZ_FILTER_GL_FLD");
							if (!"".equals(promptGlJson)) {
								promptGlJson = promptGlJson + ",{\"TZ_FILTER_GL_FLD\":\"" + filterGlFld + "\"}";
							} else {
								promptGlJson = "{\"TZ_FILTER_GL_FLD\":\"" + filterGlFld + "\"}";
							}
						}

					} else {
						int index = resultFldTypeList.indexOf(fieldName);
						if (index < 0) {
							errorMsg[0] = "1";
							errorMsg[1] = "字段：" + fieldName + "在可配置搜索中不存在，请于管理员联系";
							return strRet;
						} else {
							String fieldType = resultFldTypeList.get(index + 1);

							if (intTypeString.contains(fieldType)) {
								// 数字;
								fldType = "02";
							} else if ("DATE".equals(fieldType)) {
								// 是否是日期;
								fldType = "03";
							} else if ("TIME".equals(fieldType)) {
								// 是否是时间;
								fldType = "04";
							} else {
								// 字符串;
								fldType = "01";
							}
						}
					}

					fldJson = fldJson + ",\"" + fieldName + "\":{\"operator\":[" + trans + "],\"operatorReadOnly\":"
							+ operatorReadOnly + ",\"fldDesc\":\"" + fieldLabel + "\",\"fldType\":\"" + fldType
							+ "\",\"fldReadOnly\":" + fldReadOnly + ",\"fldHidden\":" + fldHidden
							+ ",\"promptTable\":\"" + promptTable + "\",\"promptTableFld\":\"" + promptTableFld
							+ "\",\"promptTableFldDesc\":\"" + promptTableDescFld + "\",\"promptTableDefaultFld\":["
							+ promptGlJson + "],\"promptTableFldLabel\":\"" + promptTableFldLabel
							+ "\",\"promptTableDescFldLabel\":\"" + promptTableDescFldLabel + "\",\"translateFld\":\""
							+ zhzJhId + "\"}";
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			if (!"".equals(fldJson)) {
				fldJson = fldJson.substring(1);
			}

			strRet = "{" + fldJson + "}";

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}

		strRet = "{\"formData\":" + strRet + "}";
		return strRet;
	}

	/******* 可配置搜索,返回搜索结果JSON **********/
	@SuppressWarnings("unchecked")
	public Object[] searchFilter(String[] resultFldArray,String[][] orderByArr, String strParams, int numLimit, int numStart,
			String[] errorMsg) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		// 返回值;
		Object[] strRet = null;

		// 结果值;
		ArrayList<String[]> list = new ArrayList<String[]>();

		if (resultFldArray == null) {
			errorMsg[0] = "1";
			errorMsg[1] = "可配置搜索无返回字段，请于管理员联系";
			return strRet;
		}

		// 返回多少个字段;
		int resultFldNum = resultFldArray.length;

		// 列表内容;
		//String strContent = "";
		int numTotal = 0;
		int maxNum = 0;

		try {
			// JSONObject CLASSJson = PaseJsonUtil.getJson(strParams);
			// String cfgSrhId = CLASSJson.getString("cfgSrhId");
			jacksonUtil.json2Map(strParams);
			String cfgSrhId = jacksonUtil.getString("cfgSrhId");

			String[] comPageRecArr = cfgSrhId.split("\\.");
			if (comPageRecArr == null || comPageRecArr.length != 3) {
				errorMsg[0] = "1";
				errorMsg[1] = "可配置搜索参数有误，请于管理员联系";
				return strRet;
			}

			/*** 获取可配置搜索组件、页面和view ****/
			String comId = comPageRecArr[0];
			String pageId = comPageRecArr[1];
			String recname = comPageRecArr[2];

			// 得到总条数;
			int tableNameCount = 0;
			String tableName = recname;
			String tableNameSql = "select COUNT(1) from information_schema.tables where TABLE_NAME=?";

			tableNameCount = jdbcTemplate.queryForObject(tableNameSql, new Object[] { recname }, "Integer");
			if (tableNameCount <= 0) {
				tableName = "PS_" + recname;
			}

			String exist = "";
			String existSQL = "SELECT 'Y' EXIST,TZ_RESULT_MAX_NUM from PS_TZ_FILTER_DFN_T where TZ_COM_ID=? and TZ_PAGE_ID=? and TZ_VIEW_NAME=?";
			Map<String, Object> map = null;
			try {
				map = jdbcTemplate.queryForMap(existSQL, new Object[] { comId, pageId, recname });
				exist = (String) map.get("EXIST");
				maxNum = (int) map.get("TZ_RESULT_MAX_NUM");
			} catch (Exception e) {

			}

			if (!"Y".equals(exist)) {
				errorMsg[0] = "1";
				errorMsg[1] = "可配置搜索未配置【" + cfgSrhId + "】，请于管理员联系";
				return strRet;
			}

			// 需要输出的字段(转换后);
			String result = "";
			// 不转换;
			String resultUnChange = "";

			// 类型为number的值;
			String intTypeString = "TINYINT,SMALLINT,MEDIUMINT,INT,INTEGER,BIGINT,FLOAT,DOUBLE,DECIMAL";

			ArrayList<String> resultFldTypeList = new ArrayList<String>();
			String resultFldTypeSQL = "SELECT  COLUMN_NAME, DATA_TYPE from information_schema.COLUMNS WHERE TABLE_NAME=?";
			List<Map<String, Object>> typelist = jdbcTemplate.queryForList(resultFldTypeSQL,
					new Object[] { tableName });
			String columnNanme = "";
			String dateType = "";
			/** 字段类型 **/
			for (int typelist_i = 0; typelist_i < typelist.size(); typelist_i++) {
				columnNanme = (String) typelist.get(typelist_i).get("COLUMN_NAME");
				//dateType = ((String) typelist.get(typelist_i).get("DATA_TYPE")).toUpperCase();
				dateType = (typelist.get(typelist_i).get("DATA_TYPE").toString()).toUpperCase();
				resultFldTypeList.add(columnNanme);
				resultFldTypeList.add(dateType);
			}

			int resultFldArray_i = 0;
			/******* 搜索结果sql查询转化 ****/
			for (resultFldArray_i = 0; resultFldArray_i < resultFldArray.length; resultFldArray_i++) {
				String resultFld = resultFldArray[resultFldArray_i];
				int index = resultFldTypeList.indexOf(resultFld);
				if (index < 0) {
					errorMsg[0] = "1";
					errorMsg[1] = "结果字段：" + resultFld + "在可配置搜索中不存在，请于管理员联系";
					return strRet;
				} else {
					String fieldType = resultFldTypeList.get(index + 1);

					if (intTypeString.contains(fieldType)) {
						// 数字;
						result = result + ", CONCAT(ifnull(" + resultFld + ",0),'')";
					} else if ("DATE".equals(fieldType)) {
						// 是否是日期;
						result = result + ", ifnull(date_format(" + resultFld + ",'%Y-%m-%d'),'')";

					} else if ("TIME".equals(fieldType)) {
						// 是否是时间;
						result = result + ", ifnull(date_format(" + resultFld + ",'%H:%i'),'')";

					} else if ("DATETIME".equals(fieldType) || "TIMESTAMP".equals(fieldType)) {
						// 是否日期时间;
						result = result + ", ifnull(date_format(" + resultFld + ",'%Y-%m-%d %H:%i'),'')";
					} else {
						// 字符串;
						result = result + ", ifnull(" + resultFld + ",'')";
					}

					resultUnChange = resultUnChange + ", " + resultFld;
				}

			}

			resultUnChange = resultUnChange.substring(1);
			result = result.substring(1);

			String sqlWhere = "";
			// 搜索条件;
			if (jacksonUtil.containsKey("condition")) {

				// 得到搜索的操作符;
				String operateKey = "";
				String operate = "";

				// 得到搜索的值;
				String fldKey = "";
				String fldValue = "";

				Map<String, Object> conditionJson = jacksonUtil.getMap("condition");

				if (conditionJson != null) {
					for (Map.Entry<String, Object> entry : conditionJson.entrySet()) {
						String key = entry.getKey();

						if (key.indexOf("-value") > 0) {
							fldKey = key;
							String fieldName = "";
							try {
								fieldName = fldKey.replaceAll("-value", "");

								operateKey = fieldName + "-operator";
								operate = (String) conditionJson.get(operateKey);
							} catch (Exception e) {
								errorMsg[0] = "1";
								errorMsg[1] = "可配置搜索配置错误，请于管理员联系";
								return strRet;
							}
							

							// 是不是下拉框;
							String isSelect = "";
							String isDropDownSql = "SELECT 'Y' from PS_TZ_FILTER_FLD_T where TZ_COM_ID=? and TZ_PAGE_ID=? and TZ_VIEW_NAME=? and TZ_FILTER_FLD=? and (TZ_ISDOWN_FLD = '1' or (TZ_ZHZJH_ID is not null and TZ_ZHZJH_ID<>'') )";
							isSelect = jdbcTemplate.queryForObject(isDropDownSql,
									new Object[] { comId, pageId, recname, fieldName }, "String");

							if ("Y".equals(isSelect)) {
								fldValue = "";
								List<String> jsonArray = null;
								try {
									jsonArray = (List<String>) conditionJson.get(fldKey);
									if(jsonArray != null){
										for (int jsonNum = 0; jsonNum < jsonArray.size(); jsonNum++) {
											if ("".equals(fldValue)) {
												fldValue = jsonArray.get(jsonNum);
											} else {
												fldValue = fldValue + "," + jsonArray.get(jsonNum);
											}
										}
									}
								} catch (Exception e) {
									fldValue = (String) conditionJson.get(fldKey);
								}
							} else {
								fldValue = (String) conditionJson.get(fldKey);
								
							}
							
							if (fldValue == null) {
								
								fldValue = "";
								
							}

							// 查看搜索字段是不是不区分大小写:TZ_NO_UPORLOW;
							String noUpOrLow = "";
							String noUpOrLowSql = "SELECT TZ_NO_UPORLOW from PS_TZ_FILTER_FLD_T where TZ_COM_ID=? and TZ_PAGE_ID=? and TZ_VIEW_NAME=? and TZ_FILTER_FLD=?";
							noUpOrLow = jdbcTemplate.queryForObject(noUpOrLowSql,
									new Object[] { comId, pageId, recname, fieldName }, "String");

							fldValue = fldValue.trim();
							if ("".equals(fldValue) && !"11".equals(operate) && !"12".equals(operate)) {
								continue;
							}

							String value = "";
							String isChar = "";

							int index = resultFldTypeList.indexOf(fieldName);
							if (index < 0) {
								errorMsg[0] = "1";
								errorMsg[1] = "字段：" + fieldName + "在可配置搜索中不存在，请于管理员联系";
								return strRet;
							} else {
								String fieldType = resultFldTypeList.get(index + 1);

								if (intTypeString.contains(fieldType)) {
									// 数字;
									value = fldValue;
								} else if ("DATE".equals(fieldType)) {
									// 是否是日期;
									value = " str_to_date('" + fldValue + "','%Y-%m-%d')";
								} else if ("TIME".equals(fieldType)) {
									// 是否是时间;
									value = " str_to_date('" + fldValue + "','%H:%i')";
								} else if ("DATETIME".equals(fieldType) || "TIMESTAMP".equals(fieldType)) {
									// 是否日期时间;
									value = " str_to_date('" + fldValue + "','%Y-%m-%d %H:%i')";
								} else {
									// 字符串;
									isChar = "Y";
									fldValue = fldValue.replaceAll("'", "''");
									value = "'" + fldValue + "'";
									if ("A".equals(noUpOrLow) && !"10".equals(operate) && !"11".equals(operate)
											&& !"12".equals(operate)) {
										value = "upper(" + value + ")";
										fieldName = "upper(" + fieldName + ")";
									}
								}

								if ("".equals(sqlWhere)) {
									sqlWhere = " WHERE ";
								} else {
									sqlWhere = sqlWhere + " AND ";
								}

								// 操作符;
								if ("0".equals(operate.substring(0, 1))) {
									operate = operate.substring(1);
								}

								switch (Integer.parseInt(operate)) {
								case 1:
									// 等于;
									operate = "=";
									sqlWhere = sqlWhere + fieldName + operate + value;
									break;
								case 2:
									// 不等于;
									operate = "<>";
									sqlWhere = sqlWhere + fieldName + operate + value;
									break;
								case 3:
									// 大于;
									operate = ">";
									sqlWhere = sqlWhere + fieldName + operate + value;
									break;
								case 4:
									// 大于等于;
									operate = ">=";
									sqlWhere = sqlWhere + fieldName + operate + value;
									break;
								case 5:
									// 小于;
									operate = "<";
									sqlWhere = sqlWhere + fieldName + operate + value;
									break;
								case 6:
									// 小于等于;
									operate = "<=";
									sqlWhere = sqlWhere + fieldName + operate + value;
									break;
								case 7:
									// 包含;
									if ("A".equals(noUpOrLow)) {
										value = "'%" + fldValue.toUpperCase() + "%'";
									} else {
										value = "'%" + fldValue + "%'";
									}
									sqlWhere = sqlWhere + fieldName + " LIKE " + value;
									break;
								case 8:
									// 开始于…;
									if ("A".equals(noUpOrLow)) {
										value = "'" + fldValue.toUpperCase() + "%'";
									} else {
										value = "'" + fldValue + "%'";
									}
									sqlWhere = sqlWhere + fieldName + " LIKE " + value;
									break;
								case 9:
									// 结束于…;
									if ("A".equals(noUpOrLow)) {
										value = "'%" + fldValue.toUpperCase() + "'";
									} else {
										value = "'%" + fldValue + "'";
									}
									sqlWhere = sqlWhere + fieldName + " LIKE " + value;
									break;
								case 10:
									fldValue = fldValue.replaceAll(" ", "");
									fldValue = fldValue.trim();
									String[] inArr = fldValue.split(",");

									int inArrLen = inArr.length;
									if (inArrLen > 0) {
										value = "";
										if ("Y".equals(isChar)) {
											for (int ii = 0; ii < inArrLen; ii++) {
												value = value + ",'" + inArr[ii] + "'";
											}

										} else {
											for (int ii = 0; ii < inArrLen; ii++) {
												value = value + "," + inArr[ii];
											}
										}
										value = value.substring(1);
										value = "(" + value + ")";
									}

									sqlWhere = sqlWhere + fieldName + " IN " + value;
									break;
								case 11:
									// 为空;
									/**
									 * if("Y".equals(isChar)){ sqlWhere =
									 * sqlWhere + fieldName + " = ' '"; }else{
									 * sqlWhere = sqlWhere + fieldName +
									 * " IS NULL"; }
									 **/
									sqlWhere = sqlWhere + fieldName + " IS NULL";
									break;
								case 12:
									// 不为空;
									/***
									 * if("Y".equals(isChar)){ sqlWhere =
									 * sqlWhere + fieldName + " <> ' '"; }else{
									 * sqlWhere = sqlWhere + fieldName +
									 * " IS NOT NULL"; }
									 ***/
									sqlWhere = sqlWhere + fieldName + " IS NOT NULL";
									break;

								default:
									sqlWhere = sqlWhere + fieldName + "=" + value;
									break;
								}

							}
						}
					}
				}
			}

			String orderby = "";

			if (orderByArr != null && orderByArr.length != 0) {
				for (int orderbyNum = 0; orderbyNum < orderByArr.length; orderbyNum++) {
					orderby = orderby + ", " + orderByArr[orderbyNum][0] + " " + orderByArr[orderbyNum][1];
				}
				orderby = orderby.substring(1);
				orderby = " ORDER BY " + orderby;
			}

			// 得到总条数;
			String totalSQL = "SELECT COUNT(1) FROM " + tableName + sqlWhere;
			numTotal = jdbcTemplate.queryForObject(totalSQL, "Integer");

			// 总数;
			if (maxNum > 0 && numTotal > maxNum) {
				numTotal = maxNum;
			}

			if (numTotal == 0 || numStart >= numTotal) {
				numStart = 0;
			}

			// 查看开始行数+限制的行数是否大于最大显示行数;
			if (maxNum > 0 && (numStart + numLimit) > maxNum) {
				numLimit = maxNum - numStart;
			}

			// 查询结果;
			String sqlList = "";
			if (numLimit == 0 && numStart == 0) {
				sqlList = "SELECT " + result + " FROM " + tableName + sqlWhere + orderby;
			} else {
				sqlList = "SELECT " + result + " FROM " + tableName + sqlWhere + orderby + " limit ?,?";
			}

			try {
				List<Map<String, Object>> resultlist = null;
				if (numLimit != 0) {
					resultlist = jdbcTemplate.queryForList(sqlList, new Object[] { numStart, numLimit });
				} else if (numLimit == 0 && numStart > 0) {
					resultlist = jdbcTemplate.queryForList(sqlList, new Object[] { numStart, numTotal - numStart });
				} else {
					resultlist = jdbcTemplate.queryForList(sqlList);
				}
				for (int resultlist_i = 0; resultlist_i < resultlist.size(); resultlist_i++) {
					Map<String, Object> resultMap = resultlist.get(resultlist_i);
					String[] rowList = new String[resultFldNum];
					int j = 0;
					for (Object value : resultMap.values()) {

						rowList[j] = (String) value;
						j++;
					}
					list.add(rowList);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}

		strRet = new Object[] { numTotal, list };
		return strRet;
	}

	public String tzOther(String strOperateType, String comParams, String[] errorMsg) {
		String strRet = "{}";
		try {
			JacksonUtil jacksonUtil = new JacksonUtil();
			// JSONObject CLASSJson = PaseJsonUtil.getJson(comParams);
			// String cfgSrhId = CLASSJson.getString("cfgSrhId");
			jacksonUtil.json2Map(comParams);
			String cfgSrhId = jacksonUtil.getString("cfgSrhId");

			String[] cfgArr = cfgSrhId.split("\\.");
			// 组件ID;
			String comId = cfgArr[0];
			// 页面ID;
			String pageId = cfgArr[1];
			// viewname;
			String viewName = cfgArr[2];

			String fldName = jacksonUtil.getString("fldName");
			String fldValue = jacksonUtil.getString("fldValue");

			// &TZ_PROMPT_TBL_NAME, &TZ_PROMPT_FLD;
			String promptTblName = "", promptFld = "";
			String sql = "select TZ_PROMPT_TBL_NAME,TZ_PROMPT_FLD from PS_TZ_FILTER_FLD_T where TZ_COM_ID=? and TZ_PAGE_ID=? and TZ_VIEW_NAME=? and TZ_FILTER_FLD=?";
			Map<String, Object> map = null;
			try {
				map = jdbcTemplate.queryForMap(sql, new Object[] { comId, pageId, viewName, fldName });
				promptTblName = (String) map.get("TZ_PROMPT_TBL_NAME");
				promptFld = (String) map.get("TZ_PROMPT_FLD");
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (!"".equals(promptTblName) && !"".equals(promptFld)) {
				if (!"".equals(fldValue)) {
					String[] fldValueArr = fldValue.split(",");

					int tableNameCount = 0;
					String tableName = promptTblName;
					String tableNameSql = "select COUNT(1) from information_schema.tables where TABLE_NAME=?";

					tableNameCount = jdbcTemplate.queryForObject(tableNameSql, new Object[] { promptTblName },
							"Integer");
					if (tableNameCount <= 0) {
						tableName = "PS_" + promptTblName;
					}

					for (int i = 0; i < fldValueArr.length; i++) {
						String fldValueStr = fldValueArr[i].trim();
						int isExistNum = 0;
						String sqlStr = "select count(1) from " + tableName + " where " + promptFld + " = ?";

						isExistNum = jdbcTemplate.queryForObject(sqlStr, new Object[] { fldValueStr }, "Integer");

						if (isExistNum <= 0) {
							strRet = "{\"success\": \"false\"}";
							return strRet;
						}
					}
					strRet = "{\"success\": \"true\"}";
				} else {
					strRet = "{\"success\": \"false\"}";
				}
			} else {
				strRet = "{\"success\": \"true\"}";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
			strRet = "{\"success\": \"false\"}";
		}

		return strRet;
	}
}
