package com.tranzvision.gd.TZConfigurableSearchMgBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.dao.PsTzFilterDfnTMapper;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.dao.PsTzFilterFldTMapper;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.dao.PsTzFltDstConTMapper;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.dao.PsTzFltDstFldTMapper;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.dao.PsTzFltdstRoleTMapper;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.model.PsTzFilterDfnT;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.model.PsTzFilterDfnTKey;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.model.PsTzFilterFldT;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.model.PsTzFilterFldTKey;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.model.PsTzFilterYsfT;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.model.PsTzFilterYsfTKey;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.model.PsTzFltDstConT;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.model.PsTzFltDstConTKey;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.model.PsTzFltDstFldT;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.model.PsTzFltDstFldTKey;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.model.PsTzFltdstRoleT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 可配置搜索配置页面-数据集
 * 
 */

@Service("com.tranzvision.gd.TZConfigurableSearchMgBundle.service.impl.FilterDataSetClsServiceImpl")
public class FilterDataSetClsServiceImpl extends FrameworkImpl {
	@Autowired
	private PsTzFltDstFldTMapper psTzFltDstFldTMapper;

	@Autowired
	private PsTzFltDstConTMapper psTzFltDstConTMapper;
	
	@Autowired
	private PsTzFltdstRoleTMapper psTzFltdstRoleTMapper;
	

	@Autowired
	private SqlQuery jdbcTemplate;

	/* 查询表单 */

	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("formData", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			String str_com_id = jacksonUtil.getString("ComID");
			String str_page_id = jacksonUtil.getString("PageID");
			String str_view_name = jacksonUtil.getString("ViewMc");
			String strFieldOrder = jacksonUtil.getString("fieldOrder");
			
			PsTzFltDstFldTKey psTzFltDstFldTKey = new PsTzFltDstFldTKey();
			psTzFltDstFldTKey.setTzComId(str_com_id);
			psTzFltDstFldTKey.setTzPageId(str_page_id);
			psTzFltDstFldTKey.setTzViewName(str_view_name);
			psTzFltDstFldTKey.setTzFltdstOrder(Integer.valueOf(strFieldOrder));
			PsTzFltDstFldT psTzFilterDfnT = psTzFltDstFldTMapper.selectByPrimaryKey(psTzFltDstFldTKey);

			if(psTzFilterDfnT!=null){
				String strFltdstFld = psTzFilterDfnT.getTzFltdstFld() == null ? "" : psTzFilterDfnT.getTzFltdstFld();
				String strFltdstSrchRec = psTzFilterDfnT.getTzFltdstSrchRec() == null ? "" : psTzFilterDfnT.getTzFltdstSrchRec();
				String strFltdstDesc = psTzFilterDfnT.getTzFltdstDesc() == null ? "" : psTzFilterDfnT.getTzFltdstDesc();
				String strValidStatus = psTzFilterDfnT.getTzFltdstStatus() == null ? "" : psTzFilterDfnT.getTzFltdstStatus();
				String strDefault = psTzFilterDfnT.getTzFltdstDefault() == null ? "" : psTzFilterDfnT.getTzFltdstDefault();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("ComID", str_com_id);
				map.put("PageID", str_page_id);
				map.put("ViewMc", str_view_name);
				map.put("fieldOrder", strFieldOrder);
				map.put("dataSetFld", strFltdstFld);
				map.put("searchRec", strFltdstSrchRec);
				map.put("fldDstDesc", strFltdstDesc);
				map.put("fldDstStatus", strValidStatus);
				map.put("fldDstDefault", strDefault);

				returnJsonMap.replace("formData", map);
			}else{
				errMsg[0] = "1";
				errMsg[1] = "加载信息失败。";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(returnJsonMap);
	}

	/* 查询可配置搜索字段列表 */
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			// 将字符串转换成json;
			jacksonUtil.json2Map(comParams);
			if(jacksonUtil.containsKey("ComID") && 
					jacksonUtil.containsKey("PageID") &&
					jacksonUtil.containsKey("ViewMc") &&
					jacksonUtil.containsKey("queryID") &&
					jacksonUtil.containsKey("fieldOrder")){
				String str_com_id = jacksonUtil.getString("ComID");
				String str_page_id = jacksonUtil.getString("PageID");
				String str_view_name = jacksonUtil.getString("ViewMc");
				String queryID = jacksonUtil.getString("queryID");
				String strFieldOrder = jacksonUtil.getString("fieldOrder");
				int numFieldOrder = Integer.parseInt(strFieldOrder);
				int total = 0;
				
				if ("1".equals(queryID)) {
					// 查询总数;
					String totalSQL = "SELECT COUNT('Y') FROM PS_TZ_FLTDST_CON_T where TZ_COM_ID=? and TZ_PAGE_ID=? and TZ_VIEW_NAME=?";
					total = jdbcTemplate.queryForObject(totalSQL, new Object[] { str_com_id, str_page_id, str_view_name },
							"Integer");
	
					String sql = "SELECT TZ_FLTDST_C_ORDER,TZ_FLTDST_AND_OR,TZ_FLTDST_L_PAREN,TZ_FLTDST_CON_FLD,TZ_FLTDST_OPERATOR,TZ_FLTDST_FLD_V_T,TZ_FLTDST_FLD_VAL,TZ_FLTDST_R_PAREN FROM PS_TZ_FLTDST_CON_T where TZ_COM_ID=? and TZ_PAGE_ID=? and TZ_VIEW_NAME=? AND TZ_FLTDST_ORDER = ? order by TZ_FLTDST_C_ORDER asc limit ?,?";
					List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,
							new Object[] { str_com_id, str_page_id, str_view_name,numFieldOrder, numStart, numLimit });
					if (list != null && list.size()>0) {
						for (int i = 0; i < list.size(); i++) {
							String strOrderCond =  String.valueOf(list.get(i).get("TZ_FLTDST_C_ORDER"));
							String strDstAndOr = (String) list.get(i).get("TZ_FLTDST_AND_OR");
							String strLeftParen = (String) list.get(i).get("TZ_FLTDST_L_PAREN");
							String strDstCondFld = (String) list.get(i).get("TZ_FLTDST_CON_FLD");
							String strDstOperator= (String) list.get(i).get("TZ_FLTDST_OPERATOR");
							String strDstCondValueType = (String) list.get(i).get("TZ_FLTDST_FLD_V_T");
							String strDstCondFldValue = (String) list.get(i).get("TZ_FLTDST_FLD_VAL");
							String strRightParen = (String) list.get(i).get("TZ_FLTDST_R_PAREN");
							
												
							String strDstOperatorDesc = jdbcTemplate.queryForObject("SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID='TZ_FLTDST_OPERATOR' AND TZ_ZHZ_ID = ? AND TZ_EFF_STATUS='A' limit 0,1", 
									new Object[]{strDstOperator},"String");
							
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("ComID", str_com_id);
							map.put("PageID", str_page_id);
							map.put("ViewMc", str_view_name);
							map.put("fieldOrder", strFieldOrder);
							map.put("orderCond", strOrderCond);
							map.put("dstAndOr", strDstAndOr);
							map.put("leftParen", strLeftParen);
							map.put("dstCondFld", strDstCondFld);
							map.put("dstOperator", strDstOperator);
							map.put("dstOperatorDesc", strDstOperatorDesc);
							map.put("dstCondValueType", strDstCondValueType);
							map.put("dstCondFldValue", strDstCondFldValue);
							map.put("rightParen", strRightParen);
							listData.add(map);
						}
						
						mapRet.replace("total",total);
						mapRet.replace("root", listData);
					}
				}
				
				if ("2".equals(queryID)) {
					// 查询总数;
					String totalSQL = "SELECT COUNT('Y') FROM PS_TZ_FLTDST_ROLE_T where TZ_COM_ID=? and TZ_PAGE_ID=? and TZ_VIEW_NAME=? and TZ_FLTDST_ORDER=?";
					
					total = jdbcTemplate.queryForObject(totalSQL, new Object[] { str_com_id, str_page_id, str_view_name,numFieldOrder },
							"Integer");
	
					String sql = "SELECT * FROM PS_TZ_FLTDST_ROLE_T where TZ_COM_ID=? and TZ_PAGE_ID=? and TZ_VIEW_NAME=? and TZ_FLTDST_ORDER=?";
					
					List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,
							new Object[] { str_com_id, str_page_id, str_view_name,numFieldOrder});
					if (list != null && list.size()>0) {
						for (int i = 0; i < list.size(); i++) {
							String strRoleName = (String) list.get(i).get("ROLENAME");
							String strRoleDesc = (String) list.get(i).get("DESCR");
							
							
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("ComID", str_com_id);
							map.put("PageID", str_page_id);
							map.put("ViewID", str_view_name);
							map.put("roleID", strRoleName);
							map.put("roleDesc", strRoleDesc);
							map.put("orderNum", numFieldOrder);
							listData.add(map);
						}
						
						mapRet.replace("total",total);
						mapRet.replace("root", listData);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jacksonUtil.Map2json(mapRet);
	}

	@Override
	/* 添加可配置搜索 */
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "";
		
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("ComID", "");
		returnJsonMap.put("PageID", "");
		returnJsonMap.put("ViewMc", "");
		returnJsonMap.put("fieldOrder", "");
		returnJsonMap.put("dataSetFld", "");
		returnJsonMap.put("searchRec", "");
		returnJsonMap.put("fldDstDesc", "");
		returnJsonMap.put("fldDstStatus", "");
		returnJsonMap.put("fldDstDefault", "");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 类型标志;
				String strFlag = jacksonUtil.getString("typeFlag");
				
				if ("FILTER".equals(strFlag)) {
					// 将字符串转换成json;
					Map<String, Object> Json = jacksonUtil.getMap("data");
					
					String str_com_id = (String) Json.get("ComID");
					String str_page_id = (String) Json.get("PageID");
					String str_view_name = (String) Json.get("ViewMc");
					String strFltdstFld = (String) Json.get("dataSetFld");
					String strFltdstSrchRec = (String) Json.get("searchRec");
					String strFltdstDesc = (String) Json.get("fldDstDesc");
					String strValidStatus = (String) Json.get("fldDstStatus");
					String strDefault = "";
					if(Json.containsKey("fldDstDefault")){
						strDefault = (String) Json.get("fldDstDefault");
					}else{
						strDefault = "";
					}

					/*
					String isExist = "";
					String sql = "select 'Y' from PS_TZ_FLTDST_FLD_T WHERE TZ_COM_ID=? AND TZ_PAGE_ID=? AND TZ_VIEW_NAME=? AND TZ_FLTDST_FLD = ?";
					isExist = jdbcTemplate.queryForObject(sql, new Object[] { str_com_id, str_page_id, str_view_name,strFltdstFld },
							"String");
					if ("Y".equals(isExist)) {
						errMsg[0] = "1";
						errMsg[1] = "试图添加的值已存在，请指定新值。";
						return strRet;
					} else {
					}*/
					String num_max = "";
					int num_max_num = 0;
					String sqlGetMaxNum = "select TZ_FLTDST_ORDER from PS_TZ_FLTDST_FLD_T WHERE TZ_COM_ID=? AND TZ_PAGE_ID=? AND TZ_VIEW_NAME=? ORDER BY TZ_FLTDST_ORDER DESC LIMIT 0,1";
					num_max = jdbcTemplate.queryForObject(sqlGetMaxNum, new Object[] { str_com_id, str_page_id, str_view_name },
							"String");
					if(num_max==null||"".equals(num_max)){
						num_max_num = 1;
					}else{
						num_max_num = Integer.parseInt(num_max) + 1;
					}
				
					PsTzFltDstFldT psTzFltDstFldT = new PsTzFltDstFldT();
					psTzFltDstFldT.setTzComId(str_com_id);
					psTzFltDstFldT.setTzPageId(str_page_id);
					psTzFltDstFldT.setTzViewName(str_view_name);
					psTzFltDstFldT.setTzFltdstOrder(num_max_num);
					psTzFltDstFldT.setTzFltdstFld(strFltdstFld);
					psTzFltDstFldT.setTzFltdstSrchRec(strFltdstSrchRec);
					psTzFltDstFldT.setTzFltdstDesc(strFltdstDesc);
					psTzFltDstFldT.setTzFltdstStatus(strValidStatus);
					psTzFltDstFldT.setTzFltdstDefault(strDefault);
					int i = psTzFltDstFldTMapper.insert(psTzFltDstFldT);
					
					if (i > 0) {
						returnJsonMap.replace("ComID", str_com_id);
						returnJsonMap.replace("PageID", str_page_id);
						returnJsonMap.replace("ViewMc", str_view_name);
						returnJsonMap.replace("fieldOrder", num_max_num);
						returnJsonMap.replace("dataSetFld", strFltdstFld);
						returnJsonMap.replace("searchRec", strFltdstSrchRec);
						returnJsonMap.replace("fldDstDesc", strFltdstDesc);
						returnJsonMap.replace("fldDstStatus", strValidStatus);
						returnJsonMap.replace("fldDstDefault", strDefault);
					} else {
						errMsg[0] = "1";
						errMsg[1] = "数据保存失败";
					}
					
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}

	@Override
	@SuppressWarnings("unchecked")
	/* 修改可配置定义信息 */
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("ComID", "");
		returnJsonMap.put("PageID", "");
		returnJsonMap.put("ViewMc", "");
		returnJsonMap.put("fieldOrder", "");
		returnJsonMap.put("dataSetFld", "");
		returnJsonMap.put("searchRec", "");
		returnJsonMap.put("fldDstDesc", "");
		returnJsonMap.put("fldDstStatus", "");
		returnJsonMap.put("fldDstDefault", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 类型标志;
				String strFlag = jacksonUtil.getString("typeFlag");
				
				if ("FILTER".equals(strFlag)) {
					// 将字符串转换成json;
					Map<String, Object> Json = jacksonUtil.getMap("data");
					String str_com_id = (String) Json.get("ComID");
					String str_page_id = (String) Json.get("PageID");
					String str_view_name = (String) Json.get("ViewMc");
					String num_max = (String) Json.get("fieldOrder");
					int num_max_num = 0;
					if (!"".equals(num_max) && StringUtils.isNumeric(num_max)) {
						num_max_num = Integer.parseInt(num_max);
					}
					String strFltdstFld = (String) Json.get("dataSetFld");
					String strFltdstSrchRec = (String) Json.get("searchRec");
					String strFltdstDesc = (String) Json.get("fldDstDesc");
					String strValidStatus = (String) Json.get("fldDstStatus");
					String strDefault = "";
					if(Json.containsKey("fldDstDefault")){
						strDefault = (String) Json.get("fldDstDefault");
					}else{
						strDefault = "";
					}

					PsTzFltDstFldT psTzFltDstFldT = new PsTzFltDstFldT();
					psTzFltDstFldT.setTzComId(str_com_id);
					psTzFltDstFldT.setTzPageId(str_page_id);
					psTzFltDstFldT.setTzViewName(str_view_name);
					psTzFltDstFldT.setTzFltdstOrder(num_max_num);
					psTzFltDstFldT.setTzFltdstFld(strFltdstFld);
					psTzFltDstFldT.setTzFltdstSrchRec(strFltdstSrchRec);
					psTzFltDstFldT.setTzFltdstDesc(strFltdstDesc);
					psTzFltDstFldT.setTzFltdstStatus(strValidStatus);
					psTzFltDstFldT.setTzFltdstDefault(strDefault);
					int i = psTzFltDstFldTMapper.updateByPrimaryKeySelective(psTzFltDstFldT);
					if (i > 0) {
						returnJsonMap.replace("ComID", str_com_id);
						returnJsonMap.replace("PageID", str_page_id);
						returnJsonMap.replace("ViewMc", str_view_name);
						returnJsonMap.replace("fieldOrder", num_max_num);
						returnJsonMap.replace("dataSetFld", strFltdstFld);
						returnJsonMap.replace("searchRec", strFltdstSrchRec);
						returnJsonMap.replace("fldDstDesc", strFltdstDesc);
						returnJsonMap.replace("fldDstStatus", strValidStatus);
						returnJsonMap.replace("fldDstDefault", strDefault);
					} else {
						errMsg[0] = "1";
						errMsg[1] = "数据保存失败";
					}
				}

				if (jacksonUtil.containsKey("updateList")) {
					
				}

			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}

	@Override
	/* 修改类定义信息 */
	public String tzDelete(String[] actData, String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				
				String strFlag = jacksonUtil.getString("typeFlag");
				// 信息内容;
				if ("Condition".equals(strFlag)) {
					if (jacksonUtil.containsKey("removeList")) {
						List<Map<String, Object>> jsonArray = (List<Map<String, Object>>) jacksonUtil.getList("removeList");
						if(jsonArray != null && jsonArray.size()>0){
							for (int j = 0; j < jsonArray.size(); j++) {
								Map<String, Object> Json2 = jsonArray.get(j);
								
								// 信息内容;
								String str_com_id = (String) Json2.get("ComID");
								String str_page_id = (String) Json2.get("PageID");
								String str_view_name = (String) Json2.get("ViewMc");
								String strFieldOrder = (String) Json2.get("fieldOrder");
								String strCondOrder =  (String) Json2.get("orderCond");
								PsTzFltDstConTKey psTzFltDstConTKey = new PsTzFltDstConTKey();
								psTzFltDstConTKey.setTzComId(str_com_id);
								psTzFltDstConTKey.setTzPageId(str_page_id);
								psTzFltDstConTKey.setTzViewName(str_view_name);
								psTzFltDstConTKey.setTzFltdstOrder(Integer.valueOf(strFieldOrder));
								psTzFltDstConTKey.setTzFltdstCOrder(Integer.valueOf(strCondOrder));
								psTzFltDstConTMapper.deleteByPrimaryKey(psTzFltDstConTKey);
							}
						}
					}
				}
				
				if ("Roles".equals(strFlag)) {
					if (jacksonUtil.containsKey("removeList")) {
						List<Map<String, Object>> jsonArray = (List<Map<String, Object>>) jacksonUtil.getList("removeList");
						if(jsonArray != null && jsonArray.size()>0){
							for (int j = 0; j < jsonArray.size(); j++) {
								Map<String, Object> Json2 = jsonArray.get(j);
								
								// 信息内容;
								String str_com_id = (String) Json2.get("ComID");
								String str_page_id = (String) Json2.get("PageID");
								String str_view_name = (String) Json2.get("ViewID");
								int strOrderNum = (int) Json2.get("orderNum");
								String strRoleID =  (String) Json2.get("roleID");
								String strRoleDesc =  (String) Json2.get("roleDesc");

								System.out.println("strRoleDesc======"+strRoleDesc);
								
								PsTzFltdstRoleT psTzFltdstRoleT=new PsTzFltdstRoleT();
								psTzFltdstRoleT.setTzComId(str_com_id);
								psTzFltdstRoleT.setTzPageId(str_page_id);
								psTzFltdstRoleT.setTzViewName(str_view_name);
								psTzFltdstRoleT.setTzFltdstOrder(Integer.valueOf(strOrderNum));
								psTzFltdstRoleT.setRolename(strRoleID);
								psTzFltdstRoleT.setDescr(strRoleDesc);
								
								psTzFltdstRoleTMapper.deleteByPrimaryKey(psTzFltdstRoleT);
							}
						}
					}
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
}
