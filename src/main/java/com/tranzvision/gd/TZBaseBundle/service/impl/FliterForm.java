package com.tranzvision.gd.TZBaseBundle.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
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
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	/* 获取组件注册信息 */
	public String tzQuery(String strParams, String[] errorMsg) {
		// 返回值;
		String strRet = "{}";
		String strRetDataSet = "{}";
		String isDisplay="N";

		try {
			JacksonUtil jacksonUtil = new JacksonUtil();
			// JSONObject CLASSJson = PaseJsonUtil.getJson(strParams);
			// String cfgSrhId = CLASSJson.getString("cfgSrhId");
			jacksonUtil.json2Map(strParams);
			String cfgSrhId = "";
			String currentUser = "";
			String currentrOganization = "";
			try{
				cfgSrhId = jacksonUtil.getString("cfgSrhId");
				currentUser=jacksonUtil.getString("currentUser");
				currentrOganization=jacksonUtil.getString("currentrOganization");
				System.out.println("currentUser========"+currentUser);
			}catch(Exception e){
				errorMsg[0] = "1";
				errorMsg[1] = "未获取对应的可配置搜索，请与管理员联系";
				return strRet;
			}

			String[] cfgArr = cfgSrhId.split("\\.");
			if (cfgArr == null || cfgArr.length != 3) {
				errorMsg[0] = "1";
				errorMsg[1] = "可配置搜索获取搜索条件失败，请与管理员联系";
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
				System.out.println(map);
				if (map!=null) {
					isExist = (String) map.get("Exist");
					TZ_ADVANCE_MODEL = (String) map.get("TZ_ADVANCE_MODEL");
				}
				
				//TZ_RESULT_MAX_NUM = (long) map.get("TZ_RESULT_MAX_NUM");
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
			
			//DeepQuery
			//是否DeepQuery字段；DeepQuery视图；DeepQuery关联字段
			String strDqFlg,strDqView,strDqFld;
			
			// 配置的搜索字段;
			String cfgFldSql = "SELECT TZ_FILTER_FLD,TZ_FILTER_FLD_DESC,TZ_RESULT_MAX_NUM,TZ_FLD_READONLY,TZ_FLD_HIDE,TZ_PROMPT_TBL_NAME,TZ_PROMPT_FLD,TZ_PROMPT_DESC_FLD,TZ_ISDOWN_FLD,TZ_FLT_FLD_QZ_TYPE,TZ_ZHZJH_ID,TZ_DEEPQUERY_FLG,TZ_DEEPQUERY_VIEW,TZ_DEEPQUERY_FLD from PS_TZ_FILTER_FLD_T where TZ_COM_ID=? and TZ_PAGE_ID=? and TZ_VIEW_NAME=? order by TZ_SORT_NUM";
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
					
					strDqFlg=(String) list.get(list_i).get("TZ_DEEPQUERY_FLG");
					strDqView=(String) list.get(list_i).get("TZ_DEEPQUERY_VIEW");
					strDqFld=(String) list.get(list_i).get("TZ_DEEPQUERY_FLD");

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
						
						if("Y".equals(strDqFlg)){
							//DeepQuery字段
							String strDeepQueryFldTypeSql="SELECT  DATA_TYPE from information_schema.COLUMNS WHERE TABLE_NAME=? and COLUMN_NAME=?";
							String strDeepQueryFlgType = jdbcTemplate.queryForObject(strDeepQueryFldTypeSql, new Object[] {strDqView,fieldName }, "String");
							if(!"".equals(strDeepQueryFlgType) ){
								
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
								
							}else{
								errorMsg[0] = "1";
								errorMsg[1] = "字段：" + fieldName + "在DeepQuery视图中不存在，请与管理员联系";
								return strRet;
							}							
							
							
						}else{
							//非DeepQuery字段
						
							int index = resultFldTypeList.indexOf(fieldName);
							if (index < 0) {
								errorMsg[0] = "1";
								errorMsg[1] = "字段：" + fieldName + "在可配置搜索中不存在，请与管理员联系";
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
			/*数据集*/
			
			String strDataSetRole;
			String roleOprId;
			String roleExisted;
			String distinctNum;
			
			String RoleOprIdSql = "SELECT OPRID from PS_TZ_AQ_YHXX_TBL where TZ_DLZH_ID=? and TZ_JG_ID=?";
			roleOprId = jdbcTemplate.queryForObject(RoleOprIdSql, new Object[] { currentUser,currentrOganization }, "String");
			
			String uniqueList="";
			String DataSetRoleSql = "SELECT * FROM PSROLEUSER WHERE ROLEUSER=?";
			List<Map<String, Object>> listDateSetRole = jdbcTemplate.queryForList(DataSetRoleSql,new Object[] {roleOprId});
			if(listDateSetRole!=null && listDateSetRole.size()>0){
				for (Map<String, Object> mapDataSetRole : listDateSetRole) {
					strDataSetRole = mapDataSetRole.get("ROLENAME") == null ? "" : String.valueOf(mapDataSetRole.get("ROLENAME"));
//					System.out.println("登陆用户的角色有==="+strDataSetRole);
					
					String isRoleExistSql = "SELECT * FROM PS_TZ_FLTDST_ROLE_T where TZ_COM_ID=? and TZ_PAGE_ID=? and TZ_VIEW_NAME=?";
					List<Map<String, Object>> isRoleExist = jdbcTemplate.queryForList(isRoleExistSql,new Object[] { comId, pageId, viewName });
					if(isRoleExist!=null && isRoleExist.size()>0){
						for (Map<String, Object> mapIsRoleExist : isRoleExist) {
							roleExisted = mapIsRoleExist.get("ROLENAME") == null ? "" : String.valueOf(mapIsRoleExist.get("ROLENAME"));
							distinctNum = mapIsRoleExist.get("TZ_FLTDST_ORDER") == null ? "" : String.valueOf(mapIsRoleExist.get("TZ_FLTDST_ORDER"));
//							System.out.println("数据集的角色有==="+roleExisted);
							if (roleExisted.equals(strDataSetRole)) {
								isDisplay="Y";
								if (uniqueList.length()==0) {
									uniqueList=distinctNum;
								}else{
									uniqueList=uniqueList.concat(";").concat(distinctNum);
								}
//								System.out.println("特征值为"+uniqueList);
							}
						}						
					}
				}
			}
						
			ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
			String strFltDstOrder,strFltDstFld,strFltDstSrchRec,strFltDstDesc,strFltDstDefault;
			
			String[] uniqueListNum=uniqueList.split(";");
			String orderCondition="";
			if (uniqueList.contains(";")) {
				for (int i = 0; i < uniqueListNum.length; i++) {
					//  and (TZ_FLTDST_ORDER ='1' or TZ_FLTDST_ORDER ='3')
					if (i == 0) {
						orderCondition = "and (TZ_FLTDST_ORDER ='" + uniqueListNum[0] + "' ";
					} else {
						orderCondition = orderCondition.concat("or TZ_FLTDST_ORDER ='" + uniqueListNum[i] + "' ");
					}
				}
				orderCondition = orderCondition.concat(")");
			}else{
				orderCondition="and (TZ_FLTDST_ORDER ='"+uniqueList+"')";
			}
			
			
			String cfgDataSetSql = "SELECT TZ_FLTDST_ORDER,TZ_FLTDST_FLD,TZ_FLTDST_SRCH_REC,TZ_FLTDST_DESC,TZ_FLTDST_DEFAULT from PS_TZ_FLTDST_FLD_T where TZ_COM_ID=? and TZ_PAGE_ID=? and TZ_VIEW_NAME=? AND TZ_FLTDST_STATUS = 'Y' "+orderCondition+" order by TZ_FLTDST_ORDER";
			List<Map<String, Object>> listDateSet = jdbcTemplate.queryForList(cfgDataSetSql,
					new Object[] { comId, pageId, viewName });
			if(listDateSet!=null && listDateSet.size()>0){
				for (Map<String, Object> mapDataSet : listDateSet) {
					strFltDstOrder = mapDataSet.get("TZ_FLTDST_ORDER") == null ? "" : String.valueOf(mapDataSet.get("TZ_FLTDST_ORDER"));
					strFltDstFld = mapDataSet.get("TZ_FLTDST_FLD") == null ? "" : String.valueOf(mapDataSet.get("TZ_FLTDST_FLD"));
					strFltDstSrchRec = mapDataSet.get("TZ_FLTDST_SRCH_REC") == null ? "" : String.valueOf(mapDataSet.get("TZ_FLTDST_SRCH_REC"));
					strFltDstDesc = mapDataSet.get("TZ_FLTDST_DESC") == null ? "" : String.valueOf(mapDataSet.get("TZ_FLTDST_DESC"));
					strFltDstDefault = mapDataSet.get("TZ_FLTDST_DEFAULT") == null ? "" : String.valueOf(mapDataSet.get("TZ_FLTDST_DEFAULT"));
					Map<String, Object> mapDataRet = new HashMap<String, Object>();
					mapDataRet.put("TZ_FLTDST_ORDER", strFltDstOrder);
					mapDataRet.put("TZ_FLTDST_FLD", strFltDstFld);
					mapDataRet.put("TZ_FLTDST_SRCH_REC", strFltDstSrchRec);
					mapDataRet.put("TZ_FLTDST_DESC", strFltDstDesc);
					mapDataRet.put("TZ_FLTDST_DEFAULT", strFltDstDefault);
					listData.add(mapDataRet);
				}
				strRetDataSet =  jacksonUtil.List2json(listData);
			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		
		//mapRet.put("root", listData);
		
		strRet = "{\"formData\":" + strRet + ",\"formDataSet\":" + strRetDataSet +",\"isDisplay\":\"" + isDisplay + "\"}";
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
			errorMsg[1] = "可配置搜索无返回字段，请与管理员联系";
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
				errorMsg[1] = "可配置搜索参数有误，请与管理员联系";
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
				errorMsg[1] = "可配置搜索未配置【" + cfgSrhId + "】，请与管理员联系";
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
					errorMsg[1] = "结果字段：" + resultFld + "在可配置搜索中不存在，请与管理员联系";
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
			String sqlDeepQuery="";
			String sqlDataSet="";
			boolean dataSetFlag = false; 
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
								errorMsg[1] = "可配置搜索配置错误，请与管理员联系";
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
//								fldValue = (String) conditionJson.get(fldKey);
								fldValue = conditionJson.get(fldKey).toString();
							}
							
							if (fldValue == null) {
								
								fldValue = "";
								
							}
							
							//查看DeepQuery字段
							//是否DeepQuery字段；DeepQuery视图；DeepQuery关联字段
							String strDqFlg="";
							String strDqView="";
							String strDqFld="";
							String strDqSql = "SELECT TZ_DEEPQUERY_FLG,TZ_DEEPQUERY_VIEW,TZ_DEEPQUERY_FLD from PS_TZ_FILTER_FLD_T where TZ_COM_ID=? and TZ_PAGE_ID=? and TZ_VIEW_NAME=? and TZ_FILTER_FLD=?";
							Map<String, Object> mapDeepQuery = jdbcTemplate.queryForMap(strDqSql,new Object[]{comId, pageId, recname, fieldName});
							
							if (mapDeepQuery != null) {
								strDqFlg = (String) mapDeepQuery.get("TZ_DEEPQUERY_FLG");
								strDqView = (String) mapDeepQuery.get("TZ_DEEPQUERY_VIEW");
								strDqFld = (String) mapDeepQuery.get("TZ_DEEPQUERY_FLD");
							}
							if ("Y".equals(strDqFlg)){
								if("".equals(strDqView)||"".equals(strDqFld)){
									errorMsg[0] = "1";
									errorMsg[1] = "字段：" + fieldName + "在可配置搜索中未配置DeepQuery视图或关联字段，请与管理员联系";
									return strRet;	
								}
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

							if("Y".equals(strDqFlg)){
								//是DeepQuery字段,拼装DeepQuery查询SQL;;
								
								String strDeepQueryFldTypeSql="SELECT  DATA_TYPE from information_schema.COLUMNS WHERE TABLE_NAME=? and COLUMN_NAME=?";
								String strDeepQueryFlgType = jdbcTemplate.queryForObject(strDeepQueryFldTypeSql, new Object[] {strDqView,fieldName }, "String");

								if (intTypeString.contains(strDeepQueryFlgType)) {
									// 数字;
									value = fldValue;
								} else if ("DATE".equals(strDeepQueryFlgType)) {
									// 是否是日期;
									value = " str_to_date('" + fldValue + "','%Y-%m-%d')";
								} else if ("TIME".equals(strDeepQueryFlgType)) {
									// 是否是时间;
									value = " str_to_date('" + fldValue + "','%H:%i')";
								} else if ("DATETIME".equals(strDeepQueryFlgType) || "TIMESTAMP".equals(strDeepQueryFlgType)) {
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

								if ("".equals(sqlDeepQuery)) {
									sqlDeepQuery="  (EXISTS (SELECT 'X' FROM "+strDqView+" WHERE "+strDqView+"."+strDqFld+" ="+tableName+"."+strDqFld+" AND ";
								} else {
									sqlDeepQuery = sqlDeepQuery + ")) AND  (EXISTS (SELECT 'X' FROM "+strDqView+" WHERE "+strDqView+"."+strDqFld+" ="+tableName+"."+strDqFld+" AND ";
								}

								// 操作符;
								if ("0".equals(operate.substring(0, 1))) {
									operate = operate.substring(1);
								}

								switch (Integer.parseInt(operate)) {
								case 1:
									// 等于;
									operate = "=";
									sqlDeepQuery = sqlDeepQuery + fieldName + operate + value;
									break;
								case 2:
									// 不等于;
									operate = "<>";
									sqlDeepQuery = sqlDeepQuery + fieldName + operate + value;
									break;
								case 3:
									// 大于;
									operate = ">";
									sqlDeepQuery = sqlDeepQuery + fieldName + operate + value;
									break;
								case 4:
									// 大于等于;
									operate = ">=";
									sqlDeepQuery = sqlDeepQuery + fieldName + operate + value;
									break;
								case 5:
									// 小于;
									operate = "<";
									sqlDeepQuery = sqlDeepQuery + fieldName + operate + value;
									break;
								case 6:
									// 小于等于;
									operate = "<=";
									sqlDeepQuery = sqlDeepQuery + fieldName + operate + value;
									break;
								case 7:
									// 包含;
									if ("A".equals(noUpOrLow)) {
										value = "'%" + fldValue.toUpperCase() + "%'";
									} else {
										value = "'%" + fldValue + "%'";
									}
									sqlDeepQuery = sqlDeepQuery + fieldName + " LIKE " + value;
									break;
								case 8:
									// 开始于…;
									if ("A".equals(noUpOrLow)) {
										value = "'" + fldValue.toUpperCase() + "%'";
									} else {
										value = "'" + fldValue + "%'";
									}
									sqlDeepQuery = sqlDeepQuery + fieldName + " LIKE " + value;
									break;
								case 9:
									// 结束于…;
									if ("A".equals(noUpOrLow)) {
										value = "'%" + fldValue.toUpperCase() + "'";
									} else {
										value = "'%" + fldValue + "'";
									}
									sqlDeepQuery = sqlDeepQuery + fieldName + " LIKE " + value;
									break;
								case 10:
									//在......之内
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

									sqlDeepQuery = sqlDeepQuery + fieldName + " IN " + value;
									break;
								case 11:
									// 为空;
									/**
									 * if("Y".equals(isChar)){ sqlWhere =
									 * sqlWhere + fieldName + " = ' '"; }else{
									 * sqlWhere = sqlWhere + fieldName +
									 * " IS NULL"; }
									 **/
									sqlDeepQuery = sqlDeepQuery + fieldName + " IS NULL";
									break;
								case 12:
									// 不为空;
									/***
									 * if("Y".equals(isChar)){ sqlWhere =
									 * sqlWhere + fieldName + " <> ' '"; }else{
									 * sqlWhere = sqlWhere + fieldName +
									 * " IS NOT NULL"; }
									 ***/
									sqlDeepQuery = sqlDeepQuery + fieldName + " IS NOT NULL";
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

									sqlDeepQuery = sqlDeepQuery + fieldName + " NOT IN " + value;
									break;
								default:
									sqlDeepQuery = sqlDeepQuery + fieldName + "=" + value;
									break;
								}
							}else{
								
								int index = resultFldTypeList.indexOf(fieldName);
								if (index < 0) {
									errorMsg[0] = "1";
									errorMsg[1] = "字段：" + fieldName + "在可配置搜索中不存在，请与管理员联系";
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
										// 在......之内
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

										sqlWhere = sqlWhere + fieldName + " NOT IN " + value;
										break;
									default:
										sqlWhere = sqlWhere + fieldName + "=" + value;
										break;
									}
									/*System.out.println("fieldName   :"+fieldName);
									System.out.println("value   :"+value);
									System.out.println("sqlWhere   :"+sqlWhere);*/
									
									
									
								}
								
							}	
						}
						
						/**/
						/*拼接数据集开始*/
						if (key.indexOf("-dataset") > 0) {
							//sqlWhere = sqlWhere + " AND EXISTS";
							try{
								dataSetFlag = true;
								String DataSetFieldValue = key.replaceAll("-dataset", "");
								Map<String, Object> dataSetFieldMap = new HashMap<String, Object>();
								String dateSetField,dateSetSrchRec;
								dataSetFieldMap = jdbcTemplate.queryForMap("SELECT TZ_FLTDST_FLD,TZ_FLTDST_SRCH_REC FROM PS_TZ_FLTDST_FLD_T WHERE TZ_COM_ID= ? AND TZ_PAGE_ID=? AND TZ_VIEW_NAME= ? AND TZ_FLTDST_ORDER = ? LIMIT 0,1", 
										new Object[] { comId,pageId,recname,Integer.parseInt(DataSetFieldValue) });
								if(dataSetFieldMap!=null){
									
									dateSetField = dataSetFieldMap.get("TZ_FLTDST_FLD").toString();
									dateSetSrchRec = dataSetFieldMap.get("TZ_FLTDST_SRCH_REC").toString();
									String dataSetWhere = "";
									dataSetWhere = dataSetWhere + "(EXISTS (";
									dataSetWhere = dataSetWhere + " SELECT 'Y' FROM " + dateSetSrchRec + " ";
									dataSetWhere = dataSetWhere + "WHERE " + dateSetSrchRec + "." + dateSetField + "=" + tableName + "." + dateSetField;
									
									//ArrayList<String> dataSetCondList = new ArrayList<String>();
									String dataSetCondSql = "SELECT TZ_FLTDST_C_ORDER,TZ_FLTDST_AND_OR,TZ_FLTDST_L_PAREN,TZ_FLTDST_CON_FLD,TZ_FLTDST_OPERATOR,TZ_FLTDST_FLD_V_T,TZ_FLTDST_FLD_VAL,TZ_FLTDST_R_PAREN,TZ_NO_UPORLOW "
											+ "FROM PS_TZ_FLTDST_CON_T "
											+ "WHERE TZ_COM_ID= ? AND TZ_PAGE_ID=? AND TZ_VIEW_NAME= ? AND TZ_FLTDST_ORDER = ? "
											+ "ORDER BY TZ_FLTDST_C_ORDER";
									List<Map<String, Object>> dataSetCondList = jdbcTemplate.queryForList(dataSetCondSql,
											new Object[] { comId,pageId,recname,Integer.parseInt(DataSetFieldValue) });
									if (dataSetCondList != null && dataSetCondList.size()>0) {
										String strDataSetFldTypeSql="SELECT UPPER(DATA_TYPE) from information_schema.COLUMNS WHERE TABLE_NAME=? and COLUMN_NAME=?";
										String strDataSetFlgType = jdbcTemplate.queryForObject(strDataSetFldTypeSql, new Object[] {dateSetSrchRec,dateSetField }, "String");

										for (int i = 0; i < dataSetCondList.size(); i++) {
											String strDstAndOr =  dataSetCondList.get(i).get("TZ_FLTDST_AND_OR") == null ? "" : String.valueOf(dataSetCondList.get(i).get("TZ_FLTDST_AND_OR"));
											String strLeftParen = dataSetCondList.get(i).get("TZ_FLTDST_L_PAREN") == null ? "" : String.valueOf(dataSetCondList.get(i).get("TZ_FLTDST_L_PAREN"));
											String strDstCondFld = dataSetCondList.get(i).get("TZ_FLTDST_CON_FLD") == null ? "" : String.valueOf(dataSetCondList.get(i).get("TZ_FLTDST_CON_FLD"));
											String strDstOperator= dataSetCondList.get(i).get("TZ_FLTDST_OPERATOR") == null ? "" : String.valueOf(dataSetCondList.get(i).get("TZ_FLTDST_OPERATOR"));
											String strDstCondValueType = dataSetCondList.get(i).get("TZ_FLTDST_FLD_V_T") == null ? "" : String.valueOf(dataSetCondList.get(i).get("TZ_FLTDST_FLD_V_T"));
											String strDstCondFldValue = dataSetCondList.get(i).get("TZ_FLTDST_FLD_VAL") == null ? "" : String.valueOf(dataSetCondList.get(i).get("TZ_FLTDST_FLD_VAL"));
											String strRightParen = dataSetCondList.get(i).get("TZ_FLTDST_R_PAREN") == null ? "" : String.valueOf(dataSetCondList.get(i).get("TZ_FLTDST_R_PAREN"));
											String strUpOrLow = dataSetCondList.get(i).get("TZ_NO_UPORLOW") == null ? "" : String.valueOf(dataSetCondList.get(i).get("TZ_NO_UPORLOW"));
											if(i==0){
												dataSetWhere = dataSetWhere + " AND ";
											}
											/*AND和OR*/
											if(!"".equals(strDstAndOr)){
												dataSetWhere = dataSetWhere + strDstAndOr + " ";
											}
											/*左括号*/
											if(!"".equals(strLeftParen)){
												dataSetWhere = dataSetWhere + strLeftParen + " ";
											}
											/*条件字段*/
											if(!"".equals(strDstCondFld)){
												dataSetWhere = dataSetWhere + dateSetSrchRec + "." +strDstCondFld + " ";
											}
											/*字段取值*/
											String strDstCondValue = "";
											if("A".equals(strDstCondValueType)){
												strDstCondValue = tzLoginServiceImpl.getLoginedManagerDlzhid(request);
											}else if("B".equals(strDstCondValueType)){
												String formatType = "";
												if ("DATE".equals(strDataSetFldTypeSql)){
													formatType = "yyyy-MM-dd";
												}else if ("TIME".equals(strDataSetFldTypeSql)) {
													formatType = "HH:mm:ss";
												}else {
													formatType = "yyyy-MM-dd HH:mm:ss";
												}
												DateFormat formatDate = new SimpleDateFormat(formatType);
												strDstCondValue = formatDate.format(new Date());
											}else{
												strDstCondValue = strDstCondFldValue;
											}

											if (intTypeString.contains(strDataSetFldTypeSql)) {
												// 数字;
											} else if ("DATE".equals(strDataSetFldTypeSql)) {
												// 是否是日期;
												strDstCondValue = " str_to_date('" + strDstCondValue + "','%Y-%m-%d')";
											} else if ("TIME".equals(strDataSetFldTypeSql)) {
												// 是否是时间;
												strDstCondValue = " str_to_date('" + strDstCondValue + "','%H:%i')";
											} else if ("DATETIME".equals(strDataSetFldTypeSql) || "TIMESTAMP".equals(strDataSetFlgType)) {
												// 是否日期时间;
												strDstCondValue = " str_to_date('" + strDstCondValue + "','%Y-%m-%d %H:%i')";
											} else {
												// 字符串;
												if ("A".equals(strUpOrLow)) {
													strDstCondValue = "'" + strDstCondValue.toUpperCase() + "'";
//													System.out.println("大写吧====="+strDstCondValue);
												}else {
													strDstCondValue = "'" + strDstCondValue + "'";
												}												
											}
											
											/*操作符*/
											switch (strDstOperator) {
												case "01":
													dataSetWhere = dataSetWhere + "= " + strDstCondValue + " ";
													break;
												case "02":
													dataSetWhere = dataSetWhere + "<> " + strDstCondValue + " ";
													break;
												case "03":
													dataSetWhere = dataSetWhere + "> " + strDstCondValue + " ";
													break;
												case "04":
													dataSetWhere = dataSetWhere + ">= " + strDstCondValue + " ";
													break;
												case "05":
													dataSetWhere = dataSetWhere + "< " + strDstCondValue + " ";
													break;
												case "06":
													dataSetWhere = dataSetWhere + "<= " + strDstCondValue + " ";
													break;
												case "07":
													dataSetWhere = dataSetWhere + " LIKE " + strDstCondValue + " ";
													break;
												default:
													dataSetWhere = dataSetWhere + "= " + strDstCondValue + " ";
													break;
											}
											/*右括号*/
											if(!"".equals(strRightParen)){
												dataSetWhere = dataSetWhere + strRightParen + " ";
											}
										}
									}
									if(!"".equals(dataSetWhere)){
										dataSetWhere = dataSetWhere + "))";
										if(!"".equals(sqlDataSet)){
											sqlDataSet = sqlDataSet + " OR " + dataSetWhere;
										}else{
											sqlDataSet = sqlDataSet + " " + dataSetWhere;
										}
										
									}
									//System.out.println("Hello:"+dataSetWhere);
									//System.out.println("World:"+sqlDataSet);
								}
							}catch(Exception e){
								e.printStackTrace();
							}
						}
						/*拼接数据集结束*/
					}
				}
			}
			
			
			/*如果是默认进入，则执行默认的搜索条件*/
			if(!dataSetFlag){
				try{
					String DataSetFieldValue = "";
					Map<String, Object> dataSetFieldMap = new HashMap<String, Object>();
					String dateSetField,dateSetSrchRec;
					dataSetFieldMap = jdbcTemplate.queryForMap("SELECT TZ_FLTDST_ORDER,TZ_FLTDST_FLD,TZ_FLTDST_SRCH_REC FROM PS_TZ_FLTDST_FLD_T WHERE TZ_COM_ID= ? AND TZ_PAGE_ID=? AND TZ_VIEW_NAME= ? AND TZ_FLTDST_DEFAULT = ? LIMIT 0,1", 
							new Object[] { comId,pageId,recname,"on" });
					if(dataSetFieldMap!=null){
						DataSetFieldValue = dataSetFieldMap.get("TZ_FLTDST_ORDER").toString();
						dateSetField = dataSetFieldMap.get("TZ_FLTDST_FLD").toString();
						dateSetSrchRec = dataSetFieldMap.get("TZ_FLTDST_SRCH_REC").toString();
						String dataSetWhere = "";
						dataSetWhere = dataSetWhere + "(EXISTS (";
						dataSetWhere = dataSetWhere + " SELECT 'Y' FROM " + dateSetSrchRec + " ";
						dataSetWhere = dataSetWhere + "WHERE " + dateSetSrchRec + "." + dateSetField + "=" + tableName + "." + dateSetField;
						
						//ArrayList<String> dataSetCondList = new ArrayList<String>();
						String dataSetCondSql = "SELECT TZ_FLTDST_C_ORDER,TZ_FLTDST_AND_OR,TZ_FLTDST_L_PAREN,TZ_FLTDST_CON_FLD,TZ_FLTDST_OPERATOR,TZ_FLTDST_FLD_V_T,TZ_FLTDST_FLD_VAL,TZ_FLTDST_R_PAREN,TZ_NO_UPORLOW "
								+ "FROM PS_TZ_FLTDST_CON_T "
								+ "WHERE TZ_COM_ID= ? AND TZ_PAGE_ID=? AND TZ_VIEW_NAME= ? AND TZ_FLTDST_ORDER = ? "
								+ "ORDER BY TZ_FLTDST_C_ORDER";
						List<Map<String, Object>> dataSetCondList = jdbcTemplate.queryForList(dataSetCondSql,
								new Object[] { comId,pageId,recname,Integer.parseInt(DataSetFieldValue) });
						if (dataSetCondList != null && dataSetCondList.size()>0) {
							String strDataSetFldTypeSql="SELECT UPPER(DATA_TYPE) from information_schema.COLUMNS WHERE TABLE_NAME=? and COLUMN_NAME=?";
							String strDataSetFlgType = jdbcTemplate.queryForObject(strDataSetFldTypeSql, new Object[] {dateSetSrchRec,dateSetField }, "String");
	
							for (int i = 0; i < dataSetCondList.size(); i++) {
								String strDstAndOr =  dataSetCondList.get(i).get("TZ_FLTDST_AND_OR") == null ? "" : String.valueOf(dataSetCondList.get(i).get("TZ_FLTDST_AND_OR"));
								String strLeftParen = dataSetCondList.get(i).get("TZ_FLTDST_L_PAREN") == null ? "" : String.valueOf(dataSetCondList.get(i).get("TZ_FLTDST_L_PAREN"));
								String strDstCondFld = dataSetCondList.get(i).get("TZ_FLTDST_CON_FLD") == null ? "" : String.valueOf(dataSetCondList.get(i).get("TZ_FLTDST_CON_FLD"));
								String strDstOperator= dataSetCondList.get(i).get("TZ_FLTDST_OPERATOR") == null ? "" : String.valueOf(dataSetCondList.get(i).get("TZ_FLTDST_OPERATOR"));
								String strDstCondValueType = dataSetCondList.get(i).get("TZ_FLTDST_FLD_V_T") == null ? "" : String.valueOf(dataSetCondList.get(i).get("TZ_FLTDST_FLD_V_T"));
								String strDstCondFldValue = dataSetCondList.get(i).get("TZ_FLTDST_FLD_VAL") == null ? "" : String.valueOf(dataSetCondList.get(i).get("TZ_FLTDST_FLD_VAL"));
								String strRightParen = dataSetCondList.get(i).get("TZ_FLTDST_R_PAREN") == null ? "" : String.valueOf(dataSetCondList.get(i).get("TZ_FLTDST_R_PAREN"));
								String strUpOrLow = dataSetCondList.get(i).get("TZ_NO_UPORLOW") == null ? "" : String.valueOf(dataSetCondList.get(i).get("TZ_NO_UPORLOW"));
								if(i==0){
									dataSetWhere = dataSetWhere + " AND ";
								}
								/*AND和OR*/
								if(!"".equals(strDstAndOr)){
									dataSetWhere = dataSetWhere + strDstAndOr + " ";
								}
								/*左括号*/
								if(!"".equals(strLeftParen)){
									dataSetWhere = dataSetWhere + strLeftParen + " ";
								}
								/*条件字段*/
								if(!"".equals(strDstCondFld)){
									dataSetWhere = dataSetWhere + dateSetSrchRec + "." +strDstCondFld + " ";
								}
								/*字段取值*/
								String strDstCondValue = "";
								if("A".equals(strDstCondValueType)){
									strDstCondValue = tzLoginServiceImpl.getLoginedManagerDlzhid(request);
								}else if("B".equals(strDstCondValueType)){
									String formatType = "";
									if ("DATE".equals(strDataSetFldTypeSql)){
										formatType = "yyyy-MM-dd";
									}else if ("TIME".equals(strDataSetFldTypeSql)) {
										formatType = "HH:mm:ss";
									}else {
										formatType = "yyyy-MM-dd HH:mm:ss";
									}
									DateFormat formatDate = new SimpleDateFormat(formatType);
									strDstCondValue = formatDate.format(new Date());
								}else{
									// 字符串;
									if ("A".equals(strUpOrLow)) {
										strDstCondValue = strDstCondFldValue.toUpperCase();
									}else {
										strDstCondValue = strDstCondFldValue;
									}
								}
	
								if (intTypeString.contains(strDataSetFldTypeSql)) {
									// 数字;
								} else if ("DATE".equals(strDataSetFldTypeSql)) {
									// 是否是日期;
									strDstCondValue = " str_to_date('" + strDstCondValue + "','%Y-%m-%d')";
								} else if ("TIME".equals(strDataSetFldTypeSql)) {
									// 是否是时间;
									strDstCondValue = " str_to_date('" + strDstCondValue + "','%H:%i')";
								} else if ("DATETIME".equals(strDataSetFldTypeSql) || "TIMESTAMP".equals(strDataSetFlgType)) {
									// 是否日期时间;
									strDstCondValue = " str_to_date('" + strDstCondValue + "','%Y-%m-%d %H:%i')";
								} else {
									// 字符串;
									strDstCondValue = "'" + strDstCondValue + "'";
								}
								
								/*操作符*/
								switch (strDstOperator) {
									case "01":
										dataSetWhere = dataSetWhere + "= " + strDstCondValue + " ";
										break;
									case "02":
										dataSetWhere = dataSetWhere + "<> " + strDstCondValue + " ";
										break;
									case "03":
										dataSetWhere = dataSetWhere + "> " + strDstCondValue + " ";
										break;
									case "04":
										dataSetWhere = dataSetWhere + ">= " + strDstCondValue + " ";
										break;
									case "05":
										dataSetWhere = dataSetWhere + "< " + strDstCondValue + " ";
										break;
									case "06":
										dataSetWhere = dataSetWhere + "<= " + strDstCondValue + " ";
										break;
									case "07":
										dataSetWhere = dataSetWhere + " LIKE " + strDstCondValue + " ";
										break;
									default:
										dataSetWhere = dataSetWhere + "= " + strDstCondValue + " ";
										break;
								}
								/*右括号*/
								if(!"".equals(strRightParen)){
									dataSetWhere = dataSetWhere + strRightParen + " ";
								}
							}
						}
						if(!"".equals(dataSetWhere)){
							dataSetWhere = dataSetWhere + "))";
							if(!"".equals(sqlDataSet)){
								sqlDataSet = sqlDataSet + " OR " + dataSetWhere;
							}else{
								sqlDataSet = sqlDataSet + " " + dataSetWhere;
							}
						}
						//System.out.println("Hello:"+dataSetWhere);
						//System.out.println("World:"+sqlDataSet);
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			
			if (!"".equals(sqlDeepQuery)){
				sqlDeepQuery=sqlDeepQuery+"))";
			}

			String orderby = "";

			if (orderByArr != null && orderByArr.length != 0) {
				for (int orderbyNum = 0; orderbyNum < orderByArr.length; orderbyNum++) {
					orderby = orderby + ", " + orderByArr[orderbyNum][0] + " " + orderByArr[orderbyNum][1];
				}
				orderby = orderby.substring(1);
				orderby = " ORDER BY " + orderby;
			}
			
			if("".equals(sqlWhere)){
				if(!"".equals(sqlDeepQuery)){
					sqlWhere=" WHERE "+sqlDeepQuery;
				}
			}else{
				if(!"".equals(sqlDeepQuery)){
					sqlWhere=sqlWhere+" AND"+sqlDeepQuery;
				}
			}
			
			if("".equals(sqlWhere)){
				if(!"".equals(sqlDataSet)){
					sqlWhere=" WHERE " + "(" + sqlDataSet + ")";
				}
			}else{
				if(!"".equals(sqlDataSet)){
					sqlWhere=sqlWhere+" AND" + "(" + sqlDataSet + ")";
				}
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
		Map<String, Object> returMap = new HashMap<String, Object>();
		try {
			JacksonUtil jacksonUtil = new JacksonUtil();
			// JSONObject CLASSJson = PaseJsonUtil.getJson(comParams);
			// String cfgSrhId = CLASSJson.getString("cfgSrhId");
			if("getQuerySQL".equals(strOperateType)){
				
			
				try{
					/*
					ArrayList<String[]> list = new ArrayList<String[]>();

					int numTotal = 0;
					int maxNum = 0;

				
					// JSONObject CLASSJson = PaseJsonUtil.getJson(strParams);
					// String cfgSrhId = CLASSJson.getString("cfgSrhId");
					jacksonUtil.json2Map(comParams);
					String cfgSrhId = jacksonUtil.getString("cfgSrhId");
					
				
					String[] comPageRecArr = cfgSrhId.split("\\.");
					if (comPageRecArr == null || comPageRecArr.length != 3) {
						errorMsg[0] = "1";
						errorMsg[1] = "可配置搜索参数有误，请与管理员联系";
						return strRet;
					}


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
						System.out.println(jacksonUtil.getString("condition"));

						
						for (Map.Entry<String, Object> entry : conditionJson.entrySet()) {
							String key = entry.getKey();
							
							if (key.indexOf("-value") > 0) {
								fldKey = key;
								String fieldName = "";
								try {
									fieldName = fldKey.replaceAll("-value", "");

									operateKey = fieldName + "-operator";
									operate = (String) conditionJson.get(operateKey);
									System.out.println("operate= "+operate);
								} catch (Exception e) {
									errorMsg[0] = "1";
									errorMsg[1] = "可配置搜索配置错误，请与管理员联系";
									return strRet;
								}
								System.out.println("fieldName= "+fieldName);
								
								fldValue = (String) conditionJson.get(fldKey);
								String value=fldValue;
								if (fldValue == null) {
									fldValue = "";
								}
								System.out.println("fldValue= "+fldValue);
								
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
									
									if(value==""){
										value = "'%" + fldValue + "%'";
										sqlWhere = sqlWhere + fieldName + " LIKE " + value;
									}else{
									operate = "=";
									value = "'" + fldValue + "'";
									sqlWhere = sqlWhere + fieldName + operate + value;
									}
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
									
										value = "'%" + fldValue + "%'";
									
									sqlWhere = sqlWhere + fieldName + " LIKE " + value;
									break;
								case 8:
									// 开始于…;
									
										value = "'" + fldValue + "%'";
									
									sqlWhere = sqlWhere + fieldName + " LIKE " + value;
									break;
								case 9:
									// 结束于…;
									
										value = "'%" + fldValue + "'";
									
									sqlWhere = sqlWhere + fieldName + " LIKE " + value;
									break;
								case 10:
									//默认为字符
									String isChar = "Y";
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

									sqlWhere = sqlWhere + fieldName + " IN " + value;
									break;
								case 11:
									// 为空;
				
									sqlWhere = sqlWhere + fieldName + " IS NULL";
									break;
								case 12:
									// 不为空;
			
									sqlWhere = sqlWhere + fieldName + " IS NOT NULL";
									break;

								default:
									sqlWhere = sqlWhere + fieldName + "=" + value;
									break;
								}
								
							}
							

						}
						
					}
				
					
					*/
					
					
					String orderby = "";
					
					// 查询结果;
					String sqlList = "";
					//sqlList = "SELECT OPRID FROM PS_TZ_REG_USE2_V" + sqlWhere + orderby;
					
					String[] resultFldArray = {"OPRID"};

					String[][] orderByArr=null;
					
					sqlList=this.getQuerySQL(resultFldArray,orderByArr,comParams,errorMsg);
					
					strRet=sqlList;

					returMap.put("SQL", sqlList);
					System.out.println(sqlList);
					
					
					
					
				} catch (Exception e) {
					e.printStackTrace();
					errorMsg[0] = "1";
					errorMsg[1] = e.toString();
				}
			//	return strRet;
				return jacksonUtil.Map2json(returMap);
				
			}else{
			
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
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
			strRet = "{\"success\": \"false\"}";
		}

		return strRet;
		
	}
		
	
	//获取可配置搜索SQL
	/*
	public String getQuerySQL(String[] resultFldArray,String[][] orderByArr, String strParams,String[] errorMsg){
		JacksonUtil jacksonUtil= new JacksonUtil();
		String strRet = null;
		
		// 结果值;
				
				
		
		return strRet;
	}
	*/
	
	public String getQuerySQL(String[] resultFldArray,String[][] orderByArr, String strParams,
			String[] errorMsg) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		// 返回值;
		String strRet = "";

		// 结果值;
		ArrayList<String[]> list = new ArrayList<String[]>();

		if (resultFldArray == null) {
			errorMsg[0] = "1";
			errorMsg[1] = "无返回字段";
			return strRet;
		}

		// 返回多少个字段;
		int resultFldNum = resultFldArray.length;

		// 列表内容;
		//String strContent = "";

		try {
			// JSONObject CLASSJson = PaseJsonUtil.getJson(strParams);
			// String cfgSrhId = CLASSJson.getString("cfgSrhId");
			jacksonUtil.json2Map(strParams);
			String cfgSrhId = jacksonUtil.getString("cfgSrhId");

			String[] comPageRecArr = cfgSrhId.split("\\.");
			if (comPageRecArr == null || comPageRecArr.length != 3) {
				errorMsg[0] = "1";
				errorMsg[1] = "搜索参数有误";
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
			} catch (Exception e) {

			}

			if (!"Y".equals(exist)) {
				errorMsg[0] = "1";
				errorMsg[1] = "可配置搜索未配置【" + cfgSrhId + "】，请与管理员联系";
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
					errorMsg[1] = "结果字段：" + resultFld + "在可配置搜索中不存在，请与管理员联系";
					return strRet;
				} else {
					String fieldType = resultFldTypeList.get(index + 1);

					if (intTypeString.contains(fieldType)) {
						// 数字;
						result = result + ", CONCAT(ifnull(" + resultFld + ",0),'') "+resultFld;
					} else if ("DATE".equals(fieldType)) {
						// 是否是日期;
						result = result + ", ifnull(date_format(" + resultFld + ",'%Y-%m-%d'),'') "+resultFld;

					} else if ("TIME".equals(fieldType)) {
						// 是否是时间;
						result = result + ", ifnull(date_format(" + resultFld + ",'%H:%i'),'') "+resultFld;

					} else if ("DATETIME".equals(fieldType) || "TIMESTAMP".equals(fieldType)) {
						// 是否日期时间;
						result = result + ", ifnull(date_format(" + resultFld + ",'%Y-%m-%d %H:%i'),'') "+resultFld;
					} else {
						// 字符串;
						result = result + ", ifnull(" + resultFld + ",'') "+resultFld;
					}

					resultUnChange = resultUnChange + ", " + resultFld;
				}

			}

			resultUnChange = resultUnChange.substring(1);
			result = result.substring(1);

			String sqlWhere = "";
			String sqlDeepQuery="";
			
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
								errorMsg[1] = "可配置搜索配置错误，请与管理员联系";
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
							
							//查看DeepQuery字段
							//是否DeepQuery字段；DeepQuery视图；DeepQuery关联字段
							String strDqFlg="";
							String strDqView="";
							String strDqFld="";
							String strDqSql = "SELECT TZ_DEEPQUERY_FLG,TZ_DEEPQUERY_VIEW,TZ_DEEPQUERY_FLD from PS_TZ_FILTER_FLD_T where TZ_COM_ID=? and TZ_PAGE_ID=? and TZ_VIEW_NAME=? and TZ_FILTER_FLD=?";
							Map<String, Object> mapDeepQuery = jdbcTemplate.queryForMap(strDqSql,new Object[]{comId, pageId, recname, fieldName});
							
							if (mapDeepQuery != null) {
								strDqFlg = (String) mapDeepQuery.get("TZ_DEEPQUERY_FLG");
								strDqView = (String) mapDeepQuery.get("TZ_DEEPQUERY_VIEW");
								strDqFld = (String) mapDeepQuery.get("TZ_DEEPQUERY_FLD");
							}
							
							if ("Y".equals(strDqFlg)){
								if("".equals(strDqView)||"".equals(strDqFld)){
									errorMsg[0] = "1";
									errorMsg[1] = "字段：" + fieldName + "在可配置搜索中未配置DeepQuery视图或关联字段，请与管理员联系";
									return strRet;	
								}
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
							
							if("Y".equals(strDqFlg)){
								//是DeepQuery字段,拼装DeepQuery查询SQL;;
								
								String strDeepQueryFldTypeSql="SELECT  DATA_TYPE from information_schema.COLUMNS WHERE TABLE_NAME=? and COLUMN_NAME=?";
								String strDeepQueryFlgType = jdbcTemplate.queryForObject(strDeepQueryFldTypeSql, new Object[] {strDqView,fieldName }, "String");

								if (intTypeString.contains(strDeepQueryFlgType)) {
									// 数字;
									value = fldValue;
								} else if ("DATE".equals(strDeepQueryFlgType)) {
									// 是否是日期;
									value = " str_to_date('" + fldValue + "','%Y-%m-%d')";
								} else if ("TIME".equals(strDeepQueryFlgType)) {
									// 是否是时间;
									value = " str_to_date('" + fldValue + "','%H:%i')";
								} else if ("DATETIME".equals(strDeepQueryFlgType) || "TIMESTAMP".equals(strDeepQueryFlgType)) {
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

								if ("".equals(sqlDeepQuery)) {
									sqlDeepQuery="  (EXISTS (SELECT 'X' FROM "+strDqView+" WHERE "+strDqView+"."+strDqFld+" ="+tableName+"."+strDqFld+" AND ";
								} else {
									sqlDeepQuery = sqlDeepQuery + ")) AND  (EXISTS (SELECT 'X' FROM "+strDqView+" WHERE "+strDqView+"."+strDqFld+" ="+tableName+"."+strDqFld+" AND ";
								}

								// 操作符;
								if ("0".equals(operate.substring(0, 1))) {
									operate = operate.substring(1);
								}

								switch (Integer.parseInt(operate)) {
								case 1:
									// 等于;
									operate = "=";
									sqlDeepQuery = sqlDeepQuery + fieldName + operate + value;
									break;
								case 2:
									// 不等于;
									operate = "<>";
									sqlDeepQuery = sqlDeepQuery + fieldName + operate + value;
									break;
								case 3:
									// 大于;
									operate = ">";
									sqlDeepQuery = sqlDeepQuery + fieldName + operate + value;
									break;
								case 4:
									// 大于等于;
									operate = ">=";
									sqlDeepQuery = sqlDeepQuery + fieldName + operate + value;
									break;
								case 5:
									// 小于;
									operate = "<";
									sqlDeepQuery = sqlDeepQuery + fieldName + operate + value;
									break;
								case 6:
									// 小于等于;
									operate = "<=";
									sqlDeepQuery = sqlDeepQuery + fieldName + operate + value;
									break;
								case 7:
									// 包含;
									if ("A".equals(noUpOrLow)) {
										value = "'%" + fldValue.toUpperCase() + "%'";
									} else {
										value = "'%" + fldValue + "%'";
									}
									sqlDeepQuery = sqlDeepQuery + fieldName + " LIKE " + value;
									break;
								case 8:
									// 开始于…;
									if ("A".equals(noUpOrLow)) {
										value = "'" + fldValue.toUpperCase() + "%'";
									} else {
										value = "'" + fldValue + "%'";
									}
									sqlDeepQuery = sqlDeepQuery + fieldName + " LIKE " + value;
									break;
								case 9:
									// 结束于…;
									if ("A".equals(noUpOrLow)) {
										value = "'%" + fldValue.toUpperCase() + "'";
									} else {
										value = "'%" + fldValue + "'";
									}
									sqlDeepQuery = sqlDeepQuery + fieldName + " LIKE " + value;
									break;
								case 10:
									// 在......之内
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

									sqlDeepQuery = sqlDeepQuery + fieldName + " IN " + value;
									break;
								case 11:
									// 为空;
									/**
									 * if("Y".equals(isChar)){ sqlWhere =
									 * sqlWhere + fieldName + " = ' '"; }else{
									 * sqlWhere = sqlWhere + fieldName +
									 * " IS NULL"; }
									 **/
									sqlDeepQuery = sqlDeepQuery + fieldName + " IS NULL";
									break;
								case 12:
									// 不为空;
									/***
									 * if("Y".equals(isChar)){ sqlWhere =
									 * sqlWhere + fieldName + " <> ' '"; }else{
									 * sqlWhere = sqlWhere + fieldName +
									 * " IS NOT NULL"; }
									 ***/
									sqlDeepQuery = sqlDeepQuery + fieldName + " IS NOT NULL";
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

									sqlDeepQuery = sqlDeepQuery + fieldName + " NOT IN " + value;
									break;
								default:
									sqlDeepQuery = sqlDeepQuery + fieldName + "=" + value;
									break;
								}
								}else{
	
								int index = resultFldTypeList.indexOf(fieldName);
								if (index < 0) {
									errorMsg[0] = "1";
									errorMsg[1] = "字段：" + fieldName + "在可配置搜索中不存在，请与管理员联系";
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
										// 在......之内
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

										sqlWhere = sqlWhere + fieldName + " NOT IN " + value;
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
			}

			if (!"".equals(sqlDeepQuery)){
				sqlDeepQuery=sqlDeepQuery+"))";
			}
			
			String orderby = "";

			if (orderByArr != null && orderByArr.length != 0) {
				for (int orderbyNum = 0; orderbyNum < orderByArr.length; orderbyNum++) {
					orderby = orderby + ", " + orderByArr[orderbyNum][0] + " " + orderByArr[orderbyNum][1];
				}
				orderby = orderby.substring(1);
				orderby = " ORDER BY " + orderby;
			}
			
			
			if("".equals(sqlWhere)){
				if(!"".equals(sqlDeepQuery)){
					sqlWhere=" WHERE "+sqlDeepQuery;
				}
			}else{
				if(!"".equals(sqlDeepQuery)){
					sqlWhere=sqlWhere+" AND"+sqlDeepQuery;
				}
			}


			// 查询结果;
			strRet = "SELECT " + result + " FROM " + tableName + sqlWhere + orderby;
			return strRet;

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
			return "";
		}
	}
}
