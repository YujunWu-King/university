package com.tranzvision.gd.TZBaseBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.FliterFormForClassService;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 可配置搜索,采用CLASS类模式处理类 ClassName: FliterForClassEngine
 * 
 * @author caoy
 * @version 1.0 Create Time: 2019年11月21日 下午1:51:32 Description: 类的具体作用描述
 */
@Service
public class FliterForClassEngine {

	@Autowired
	private SqlQuery jdbcTemplate;

	@Autowired
	private ApplicationContext ctx;

	/**
	 * 
	 * Description: 显示可配置查询页面的内容 Create Time: 2019年11月21日 下午4:45:16
	 * 
	 * @author caoy
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	public String tzQuery(String strParams, String[] errorMsg) {
		String strRet = "{}";
		String strRetDataSet = "{}";
		String isDisplay = "N";

		String intTypeString = "TINYINT,SMALLINT,MEDIUMINT,INT,INTEGER,BIGINT,FLOAT,DOUBLE,DECIMAL";

		JacksonUtil jacksonUtil = new JacksonUtil();
		// JSONObject CLASSJson = PaseJsonUtil.getJson(strParams);
		// String cfgSrhId = CLASSJson.getString("cfgSrhId");
		jacksonUtil.json2Map(strParams);

		String cfgSrhId = jacksonUtil.getString("cfgSrhId");
		String currentUser = jacksonUtil.getString("currentUser");
		String currentrOganization = jacksonUtil.getString("currentrOganization");
		String[] cfgArr = cfgSrhId.split("\\.");

		// 组件ID;
		String comId = cfgArr[0];
		// 页面ID;
		String pageId = cfgArr[1];
		// viewname;
		String viewName = cfgArr[2];
		
		
		viewName = viewName.substring(viewName.indexOf("_")+1, viewName.length());

		// 是否高级模式;
		String TZ_ADVANCE_MODEL = "";
		// long TZ_RESULT_MAX_NUM = 0;
		// 操作符是否只读;
		String operatorReadOnly = "true";
		// 是否存在;
		String isExist = "";
		String sql = "select 'Y' Exist ,TZ_ADVANCE_MODEL from PS_TZ_FILTER_DFN_T where TZ_COM_ID=? and TZ_PAGE_ID=? and TZ_VIEW_NAME=? and TZ_TYPE=1";

		Map<String, Object> map = null;
		try {
			map = jdbcTemplate.queryForMap(sql, new Object[] { comId, pageId, viewName });
			System.out.println(map);
			if (map != null) {
				isExist = map.get("Exist") == null ? "" : map.get("Exist").toString();
				TZ_ADVANCE_MODEL = map.get("TZ_ADVANCE_MODEL") == null ? "" : map.get("TZ_ADVANCE_MODEL").toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!"Y".equals(isExist)) {
			errorMsg[0] = "1";
			errorMsg[1] = "找不到对应的配置搜索，请与管理员联系";
			return strRet;
		}

		if ("1".equals(TZ_ADVANCE_MODEL)) {
			operatorReadOnly = "false";
		}

		String fieldName, fieldLabel, readOnly, hiden, promptTable, promptTableFld, promptTableDescFld, isDowm;
		// long maxRows = 0;
		// 取值类型:TZ_FLT_FLD_QZ_TYPE;
		String fldQzType;
		// 转换值字段:TZ_ZHZJH_ID;
		String zhzJhId;

		// DeepQuery
		// 是否DeepQuery字段；DeepQuery视图；DeepQuery关联字段
		String strDqFlg, strDqView, strDqFld, TZ_FIELD_TYPE = "";

		// 配置的搜索字段;
		String cfgFldSql = "SELECT TZ_FILTER_FLD,TZ_FIELD_TYPE,TZ_FILTER_FLD_DESC,TZ_RESULT_MAX_NUM,TZ_FLD_READONLY,TZ_FLD_HIDE,TZ_PROMPT_TBL_NAME,TZ_PROMPT_FLD,TZ_PROMPT_DESC_FLD,TZ_ISDOWN_FLD,TZ_FLT_FLD_QZ_TYPE,TZ_ZHZJH_ID,TZ_DEEPQUERY_FLG,TZ_DEEPQUERY_VIEW,TZ_DEEPQUERY_FLD from PS_TZ_FILTER_FLD_T where TZ_COM_ID=? and TZ_PAGE_ID=? and TZ_VIEW_NAME=? order by TZ_SORT_NUM";
		String fldJson = "";
		try {
			/** 字段类型 **/
			List<Map<String, Object>> list = jdbcTemplate.queryForList(cfgFldSql,
					new Object[] { comId, pageId, viewName });
			for (int list_i = 0; list_i < list.size(); list_i++) {
				TZ_FIELD_TYPE = list.get(list_i).get("TZ_FIELD_TYPE") == null ? ""
						: list.get(list_i).get("TZ_FIELD_TYPE").toString();
				fieldName = list.get(list_i).get("TZ_FILTER_FLD") == null ? ""
						: list.get(list_i).get("TZ_FILTER_FLD").toString();
				fieldLabel = list.get(list_i).get("TZ_FILTER_FLD_DESC") == null ? ""
						: list.get(list_i).get("TZ_FILTER_FLD_DESC").toString();
				readOnly = list.get(list_i).get("TZ_FLD_READONLY") == null ? ""
						: list.get(list_i).get("TZ_FLD_READONLY").toString();
				hiden = list.get(list_i).get("TZ_FLD_HIDE") == null ? ""
						: list.get(list_i).get("TZ_FLD_HIDE").toString();
				promptTable = list.get(list_i).get("TZ_PROMPT_TBL_NAME") == null ? ""
						: list.get(list_i).get("TZ_PROMPT_TBL_NAME").toString();
				promptTableFld = list.get(list_i).get("TZ_PROMPT_FLD") == null ? ""
						: list.get(list_i).get("TZ_PROMPT_FLD").toString();
				promptTableDescFld = list.get(list_i).get("TZ_PROMPT_DESC_FLD") == null ? ""
						: list.get(list_i).get("TZ_PROMPT_DESC_FLD").toString();
				isDowm = list.get(list_i).get("TZ_ISDOWN_FLD") == null ? ""
						: list.get(list_i).get("TZ_ISDOWN_FLD").toString();
				fldQzType = list.get(list_i).get("TZ_FLT_FLD_QZ_TYPE") == null ? ""
						: list.get(list_i).get("TZ_FLT_FLD_QZ_TYPE").toString();
				zhzJhId = list.get(list_i).get("TZ_ZHZJH_ID") == null ? ""
						: list.get(list_i).get("TZ_ZHZJH_ID").toString();

				strDqFlg = list.get(list_i).get("TZ_DEEPQUERY_FLG") == null ? ""
						: list.get(list_i).get("TZ_DEEPQUERY_FLG").toString();
				strDqView = list.get(list_i).get("TZ_DEEPQUERY_VIEW") == null ? ""
						: list.get(list_i).get("TZ_DEEPQUERY_VIEW").toString();
				strDqFld = list.get(list_i).get("TZ_DEEPQUERY_FLD") == null ? ""
						: list.get(list_i).get("TZ_DEEPQUERY_FLD").toString();

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

					if ("Y".equals(strDqFlg)) {
						// DeepQuery字段
						String strDeepQueryFldTypeSql = "SELECT DATA_TYPE from information_schema.COLUMNS WHERE TABLE_NAME=? and COLUMN_NAME=? limit 1";
						String strDeepQueryFlgType = jdbcTemplate.queryForObject(strDeepQueryFldTypeSql,
								new Object[] { strDqView, fieldName }, "String");
						if (!"".equals(strDeepQueryFlgType)) {

							if (intTypeString.contains(strDeepQueryFlgType)) {
								// 数字;
								fldType = "02";
							} else if ("DATE".equals(strDeepQueryFlgType)) {
								// 是否是日期;
								fldType = "03";
							} else if ("TIME".equals(strDeepQueryFlgType)) {
								// 是否是时间;
								fldType = "04";
							} else {
								// 字符串;
								fldType = "01";
							}

						} else {
							errorMsg[0] = "1";
							errorMsg[1] = "字段：" + fieldName + "在DeepQuery视图中不存在，请与管理员联系";
							return strRet;
						}

					} else {
						// 非DeepQuery字段 类型从表里面获取 01数字 02 日期 03 时间 04字符串 05日期时间

						fldType = "01";

						if ("01".equals(TZ_FIELD_TYPE)) {
							// 数字;
							fldType = "02";
						} else if ("02".equals(TZ_FIELD_TYPE)) {
							// 是否是日期;
							fldType = "03";
						} else if ("03".equals(TZ_FIELD_TYPE)) {
							// 是否是时间;
							fldType = "04";
						} else if ("04".equals(TZ_FIELD_TYPE)) {
							// 字符串;
							fldType = "01";
						}

					}
				}

				fldJson = fldJson + ",\"" + fieldName + "\":{\"operator\":[" + trans + "],\"operatorReadOnly\":"
						+ operatorReadOnly + ",\"fldDesc\":\"" + fieldLabel + "\",\"fldType\":\"" + fldType
						+ "\",\"fldReadOnly\":" + fldReadOnly + ",\"fldHidden\":" + fldHidden + ",\"promptTable\":\""
						+ promptTable + "\",\"promptTableFld\":\"" + promptTableFld + "\",\"promptTableFldDesc\":\""
						+ promptTableDescFld + "\",\"promptTableDefaultFld\":[" + promptGlJson
						+ "],\"promptTableFldLabel\":\"" + promptTableFldLabel + "\",\"promptTableDescFldLabel\":\""
						+ promptTableDescFldLabel + "\",\"translateFld\":\"" + zhzJhId + "\"}";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!"".equals(fldJson)) {
			fldJson = fldJson.substring(1);
		}

		strRet = "{" + fldJson + "}";
		/* 数据集 */

		String strDataSetRole;
		String roleOprId;
		String roleExisted;
		String distinctNum;

		String RoleOprIdSql = "SELECT OPRID from PS_TZ_AQ_YHXX_TBL where TZ_DLZH_ID=? and TZ_JG_ID=?";
		System.out.println("currentUser是什么？？？" + currentUser);
		System.out.println("currentrOganization是什么？？？" + currentrOganization);
		roleOprId = jdbcTemplate.queryForObject(RoleOprIdSql, new Object[] { currentUser, currentrOganization },
				"String");
		System.out.println("是什么？？？" + roleOprId);

		String uniqueList = "";
		String DataSetRoleSql = "SELECT * FROM PSROLEUSER WHERE ROLEUSER=?";
		List<Map<String, Object>> listDateSetRole = jdbcTemplate.queryForList(DataSetRoleSql,
				new Object[] { roleOprId });
		if (listDateSetRole != null && listDateSetRole.size() > 0) {
			for (Map<String, Object> mapDataSetRole : listDateSetRole) {
				strDataSetRole = mapDataSetRole.get("ROLENAME") == null ? ""
						: String.valueOf(mapDataSetRole.get("ROLENAME"));
				System.out.println("登陆用户的角色有===" + strDataSetRole);

				String isRoleExistSql = "SELECT * FROM PS_TZ_FLTDST_ROLE_T where TZ_COM_ID=? and TZ_PAGE_ID=? and TZ_VIEW_NAME=?";
				List<Map<String, Object>> isRoleExist = jdbcTemplate.queryForList(isRoleExistSql,
						new Object[] { comId, pageId, viewName });
				if (isRoleExist != null && isRoleExist.size() > 0) {
					for (Map<String, Object> mapIsRoleExist : isRoleExist) {
						roleExisted = mapIsRoleExist.get("ROLENAME") == null ? ""
								: String.valueOf(mapIsRoleExist.get("ROLENAME"));
						distinctNum = mapIsRoleExist.get("TZ_FLTDST_ORDER") == null ? ""
								: String.valueOf(mapIsRoleExist.get("TZ_FLTDST_ORDER"));
						System.out.println("数据集的角色有===" + roleExisted);
						if (roleExisted.equals(strDataSetRole)) {
							isDisplay = "Y";
							if (uniqueList.length() == 0) {
								uniqueList = distinctNum;
							} else {
								uniqueList = uniqueList.concat(";").concat(distinctNum);
							}
							// System.out.println("特征值为"+uniqueList);
						}
					}
				}
			}
		}

		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		String strFltDstOrder, strFltDstFld, strFltDstSrchRec, strFltDstDesc, strFltDstDefault;

		String[] uniqueListNum = uniqueList.split(";");
		String orderCondition = "";
		if (uniqueList.contains(";")) {
			for (int i = 0; i < uniqueListNum.length; i++) {
				// and (TZ_FLTDST_ORDER ='1' or TZ_FLTDST_ORDER ='3')
				if (i == 0) {
					orderCondition = "and (TZ_FLTDST_ORDER ='" + uniqueListNum[0] + "' ";
				} else {
					orderCondition = orderCondition.concat("or TZ_FLTDST_ORDER ='" + uniqueListNum[i] + "' ");
				}
			}
			orderCondition = orderCondition.concat(")");
		} else {
			orderCondition = "and (TZ_FLTDST_ORDER ='" + uniqueList + "')";
		}

		String cfgDataSetSql = "SELECT TZ_FLTDST_ORDER,TZ_FLTDST_FLD,TZ_FLTDST_SRCH_REC,TZ_FLTDST_DESC,TZ_FLTDST_DEFAULT from PS_TZ_FLTDST_FLD_T where TZ_COM_ID=? and TZ_PAGE_ID=? and TZ_VIEW_NAME=? AND TZ_FLTDST_STATUS = 'Y' "
				+ orderCondition + " order by TZ_FLTDST_ORDER";
		List<Map<String, Object>> listDateSet = jdbcTemplate.queryForList(cfgDataSetSql,
				new Object[] { comId, pageId, viewName });
		if (listDateSet != null && listDateSet.size() > 0) {
			for (Map<String, Object> mapDataSet : listDateSet) {
				strFltDstOrder = mapDataSet.get("TZ_FLTDST_ORDER") == null ? ""
						: String.valueOf(mapDataSet.get("TZ_FLTDST_ORDER"));
				strFltDstFld = mapDataSet.get("TZ_FLTDST_FLD") == null ? ""
						: String.valueOf(mapDataSet.get("TZ_FLTDST_FLD"));
				strFltDstSrchRec = mapDataSet.get("TZ_FLTDST_SRCH_REC") == null ? ""
						: String.valueOf(mapDataSet.get("TZ_FLTDST_SRCH_REC"));
				strFltDstDesc = mapDataSet.get("TZ_FLTDST_DESC") == null ? ""
						: String.valueOf(mapDataSet.get("TZ_FLTDST_DESC"));
				strFltDstDefault = mapDataSet.get("TZ_FLTDST_DEFAULT") == null ? ""
						: String.valueOf(mapDataSet.get("TZ_FLTDST_DEFAULT"));
				Map<String, Object> mapDataRet = new HashMap<String, Object>();
				mapDataRet.put("TZ_FLTDST_ORDER", strFltDstOrder);
				mapDataRet.put("TZ_FLTDST_FLD", strFltDstFld);
				mapDataRet.put("TZ_FLTDST_SRCH_REC", strFltDstSrchRec);
				mapDataRet.put("TZ_FLTDST_DESC", strFltDstDesc);
				mapDataRet.put("TZ_FLTDST_DEFAULT", strFltDstDefault);
				listData.add(mapDataRet);
			}
			strRetDataSet = jacksonUtil.List2json(listData);
		}
		strRet = "{\"formData\":" + strRet + ",\"formDataSet\":" + strRetDataSet + ",\"isDisplay\":\"" + isDisplay
				+ "\"}";
		return strRet;
	}

	// mapRet.put("root", listData);

	/**
	 * 
	 * Description:可配置搜索,采用CLASS类模式处理方法 Create Time: 2019年11月21日 下午2:22:39
	 * 
	 * @author caoy
	 * @param resultFldArray
	 * @param orderByArr
	 * @param strParams
	 * @param numLimit
	 * @param numStart
	 * @param errorMsg
	 * @return
	 */
	public Object[] searchFilter(String[] resultFldArray, String[][] orderByArr, String strParams, int numLimit,
			int numStart, String[] errorMsg) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		int maxNum = 0;
		// 返回值;
		Object[] strRet = null;
		jacksonUtil.json2Map(strParams);
		String cfgSrhId = jacksonUtil.getString("cfgSrhId");
		String[] comPageRecArr = cfgSrhId.split("\\.");
		String comId = comPageRecArr[0];
		String pageId = comPageRecArr[1];
		String recname = comPageRecArr[2];

		// 分割recname
		recname = recname.substring(recname.indexOf("_")+1, recname.length());

		String exist = "";
		String TZ_APP_CLASS_NAME = "";
		String existSQL = "SELECT 'Y' EXIST,TZ_RESULT_MAX_NUM,TZ_APP_CLASS_NAME from PS_TZ_FILTER_DFN_T where TZ_COM_ID=? and TZ_PAGE_ID=? and TZ_VIEW_NAME=?  and TZ_TYPE=1";
		Map<String, Object> map = null;
		try {
			map = jdbcTemplate.queryForMap(existSQL, new Object[] { comId, pageId, recname });
			exist = map.get("EXIST") == null ? "" : map.get("EXIST").toString();
			maxNum = map.get("TZ_RESULT_MAX_NUM") == null ? 0
					: Integer.parseInt(map.get("TZ_RESULT_MAX_NUM").toString());
			TZ_APP_CLASS_NAME = map.get("TZ_APP_CLASS_NAME") == null ? "" : map.get("TZ_APP_CLASS_NAME").toString();
		} catch (Exception e) {

		}

		if (!"Y".equals(exist)) {
			errorMsg[0] = "1";
			errorMsg[1] = "可配置搜索未配置【" + cfgSrhId + "】，请与管理员联系";
			return strRet;
		}
		List<ConditionBean> conditionList = new ArrayList<ConditionBean>();

		// 解析JSON获取条件，凭借成
		if (jacksonUtil.containsKey("condition")) {
			// 得到搜索的操作符;
			String operateKey = "";
			String operate = "";

			// 得到搜索的值;
			String fldKey = "";
			String fldValue = "";

			String SQL = "";

			Map<String, Object> conditionJson = jacksonUtil.getMap("condition");

			ConditionBean cb = null;
			if (conditionJson != null) {
				for (Map.Entry<String, Object> entry : conditionJson.entrySet()) {
					cb = new ConditionBean();
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
							errorMsg[1] = "可配置搜索配置错误，请与管理员联系";
							return strRet;
						}

						// 是不是下拉框;
						String isSelect = "";
						String isDropDownSql = "SELECT 'Y' from PS_TZ_FILTER_FLD_T where TZ_COM_ID=? and TZ_PAGE_ID=? and TZ_VIEW_NAME=? and TZ_FILTER_FLD=? and (TZ_ISDOWN_FLD = '1' or (TZ_ZHZJH_ID is not null and TZ_ZHZJH_ID<>'') )";
						isSelect = jdbcTemplate.queryForObject(isDropDownSql,
								new Object[] { comId, pageId, recname, fieldName }, "String");
						// 如果是下拉框，可能放回多个需要用,分割
						if ("Y".equals(isSelect)) {
							fldValue = "";
							List<String> jsonArray = null;
							try {
								jsonArray = (List<String>) conditionJson.get(fldKey);
								if (jsonArray != null) {
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
							fldValue = conditionJson.get(fldKey).toString();
						}

						if (fldValue == null) {

							fldValue = "";

						}

						// 查看搜索字段是不是不区分大小写:TZ_NO_UPORLOW;
						String noUpOrLow = "";
						String noUpOrLowSql = "SELECT TZ_NO_UPORLOW,TZ_FIELD_TYPE from PS_TZ_FILTER_FLD_T where TZ_COM_ID=? and TZ_PAGE_ID=? and TZ_VIEW_NAME=? and TZ_FILTER_FLD=?";
						String TZ_FIELD_TYPE = "";
						try {
							map = jdbcTemplate.queryForMap(noUpOrLowSql,
									new Object[] { comId, pageId, recname, fieldName });
							noUpOrLow = map.get("TZ_NO_UPORLOW") == null ? "" : map.get("TZ_NO_UPORLOW").toString();
							TZ_FIELD_TYPE = map.get("TZ_FIELD_TYPE") == null ? "" : map.get("TZ_FIELD_TYPE").toString();
						} catch (Exception e) {

						}

						fldValue = fldValue.trim();
						if ("".equals(fldValue) && !"11".equals(operate) && !"12".equals(operate)) {
							continue;
						}

						String value = "";
						String isChar = "";

						// 采用类处理的，查询字段的类型是可设置 01数字 02 日期 03 时间 04字符串 05日期时间
						if ("01".equals(TZ_FIELD_TYPE)) {
							// 数字;
							value = fldValue;
						} else if ("02".equals(TZ_FIELD_TYPE)) {
							// 是否是日期;
							value = " str_to_date('" + fldValue + "','%Y-%m-%d')";
						} else if ("03".equals(TZ_FIELD_TYPE)) {
							// 是否是时间;
							value = " str_to_date('" + fldValue + "','%H:%i')";
						} else if ("05".equals(TZ_FIELD_TYPE)) {
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

						// 操作符;
						if ("0".equals(operate.substring(0, 1))) {
							operate = operate.substring(1);
						}

						switch (Integer.parseInt(operate)) {
						case 1:
							// 等于;
							operate = "=";
							SQL = fieldName + operate + value;
							break;
						case 2:
							// 不等于;
							operate = "<>";
							SQL = fieldName + operate + value;
							break;
						case 3:
							// 大于;
							operate = ">";
							SQL = fieldName + operate + value;
							break;
						case 4:
							// 大于等于;
							operate = ">=";
							SQL = fieldName + operate + value;
							break;
						case 5:
							// 小于;
							operate = "<";
							SQL = fieldName + operate + value;
							break;
						case 6:
							// 小于等于;
							operate = "<=";
							SQL = fieldName + operate + value;
							break;
						case 7:
							// 包含;
							if ("A".equals(noUpOrLow)) {
								value = "'%" + fldValue.toUpperCase() + "%'";
							} else {
								value = "'%" + fldValue + "%'";
							}
							operate = "7";
							SQL = fieldName + " LIKE " + value;
							break;
						case 8:
							// 开始于…;
							operate = "8";
							if ("A".equals(noUpOrLow)) {
								value = "'" + fldValue.toUpperCase() + "%'";
							} else {
								value = "'" + fldValue + "%'";
							}
							SQL = fieldName + " LIKE " + value;
							break;
						case 9:
							// 结束于…;
							operate = "9";
							if ("A".equals(noUpOrLow)) {
								value = "'%" + fldValue.toUpperCase() + "'";
							} else {
								value = "'%" + fldValue + "'";
							}
							SQL = fieldName + " LIKE " + value;
							break;
						case 10:
							// 在......之内
							operate = "10";
							fldValue = fldValue.replaceAll(" ", ",");
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

							SQL = fieldName + " IN " + value;
							break;
						case 11:
							// 为空;
							/**
							 * if("Y".equals(isChar)){ sqlWhere = sqlWhere +
							 * fieldName + " = ' '"; }else{ sqlWhere = sqlWhere
							 * + fieldName + " IS NULL"; }
							 **/
							operate = "11";
							SQL = fieldName + " IS NULL";
							break;
						case 12:
							// 不为空;
							/***
							 * if("Y".equals(isChar)){ sqlWhere = sqlWhere +
							 * fieldName + " <> ' '"; }else{ sqlWhere = sqlWhere
							 * + fieldName + " IS NOT NULL"; }
							 ***/
							operate = "12";
							SQL = fieldName + " IS NOT NULL";
							break;
						case 13:
							// 不在......之内
							fldValue = fldValue.replaceAll(" ", ",");
							fldValue = fldValue.trim();
							String[] notArr = fldValue.split(",");

							int notArrLen = notArr.length;
							if (notArrLen > 0) {
								value = "";
								if ("Y".equals(isChar)) {
									for (int ii = 0; ii < notArrLen; ii++) {
										value = value + ",'" + notArr[ii] + "'";
									}

								} else {
									for (int ii = 0; ii < notArrLen; ii++) {
										value = value + "," + notArr[ii];
									}
								}
								value = value.substring(1);
								value = "(" + value + ")";
							}
							operate = "13";
							SQL = fieldName + " NOT IN " + value;
							break;
						default:
							operate = "=";
							SQL = fieldName + "=" + value;
							break;
						}

						cb.setOperator(operate);
						cb.setSql(SQL);
						cb.setValue(value);
						cb.setFild(fieldName);
						conditionList.add(cb);

					}

				}
			}

		}

		int resultFldArray_i = 0;
		String resultFld = "";
		for (resultFldArray_i = 0; resultFldArray_i < resultFldArray.length; resultFldArray_i++) {
			if ("".equals(resultFld)) {
				resultFld = "T." + resultFldArray[resultFldArray_i];
			} else {
				resultFld = resultFld + ",T." + resultFldArray[resultFldArray_i];
			}
		}
		resultFld = "SELECT " + resultFld + " FROM ";

		String orderby = "";

		if (orderByArr != null && orderByArr.length != 0) {
			for (int orderbyNum = 0; orderbyNum < orderByArr.length; orderbyNum++) {
				orderby = orderby + ", T." + orderByArr[orderbyNum][0] + " " + orderByArr[orderbyNum][1];
			}
			orderby = orderby.substring(1);
			orderby = " ORDER BY " + orderby;
		}

		FliterFormForClassService fc = (FliterFormForClassService) ctx.getBean(TZ_APP_CLASS_NAME);

		String[] strArry = fc.searchFilter(resultFld, orderby, conditionList, numLimit, numStart, maxNum, errorMsg);
		int numTotal = 0;
		// 结果值;
		ArrayList<String[]> list = new ArrayList<String[]>();
		int resultFldNum = resultFldArray.length;
		if (strArry != null && strArry.length == 2) {

			// 得到总条数;
			numTotal = jdbcTemplate.queryForObject(strArry[0], "Integer");

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
			try {
				List<Map<String, Object>> resultlist = null;
				if (numLimit != 0) {
					resultlist = jdbcTemplate.queryForList(strArry[1], new Object[] { numStart, numLimit });
				} else if (numLimit == 0 && numStart > 0) {
					resultlist = jdbcTemplate.queryForList(strArry[1], new Object[] { numStart, numTotal - numStart });
				} else {
					resultlist = jdbcTemplate.queryForList(strArry[1]);
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
				errorMsg[0] = "1";
				errorMsg[1] = e.toString();
			}
		} else {
			errorMsg[0] = "1";
			errorMsg[1] = "可配置搜索【" + cfgSrhId + "】查询失败，请与管理员联系";
			return strRet;
		}

		strRet = new Object[] { numTotal, list };
		return strRet;
	}

}
