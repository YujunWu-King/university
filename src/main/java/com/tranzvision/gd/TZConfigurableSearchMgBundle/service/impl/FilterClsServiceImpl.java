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
import com.tranzvision.gd.TZConfigurableSearchMgBundle.dao.PsTzFltDstFldTMapper;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.model.PsTzFilterDfnT;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.model.PsTzFilterDfnTKey;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.model.PsTzFilterFldT;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.model.PsTzFilterFldTKey;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.model.PsTzFilterYsfT;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.model.PsTzFilterYsfTKey;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.model.PsTzFltDstFldTKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 可配置搜索配置页面
 * 
 * @author tang 原PS类 TZ_GD_FILTERGL_PKG:TZ_GD_FILTER_CLS
 */

@Service("com.tranzvision.gd.TZConfigurableSearchMgBundle.service.impl.FilterClsServiceImpl")
public class FilterClsServiceImpl extends FrameworkImpl {
	@Autowired
	private PsTzFilterDfnTMapper psTzFilterDfnTMapper;

	@Autowired
	private PsTzFilterFldTMapper psTzFilterFldTMapper;
	
	@Autowired
	private PsTzFltDstFldTMapper psTzFltDstFldTMapper;

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
			PsTzFilterDfnTKey psTzFilterDfnTKey = new PsTzFilterDfnTKey();
			psTzFilterDfnTKey.setTzComId(str_com_id);
			psTzFilterDfnTKey.setTzPageId(str_page_id);
			psTzFilterDfnTKey.setTzViewName(str_view_name);
			PsTzFilterDfnT psTzFilterDfnT = psTzFilterDfnTMapper.selectByPrimaryKey(psTzFilterDfnTKey);
			// 组件名称;
			String comMcSQL = "select TZ_COM_MC from PS_TZ_AQ_COMZC_TBL where TZ_COM_ID=?";
			String str_com_mc = jdbcTemplate.queryForObject(comMcSQL, new Object[] { str_com_id }, "String");
			if (str_com_mc == null) {
				str_com_mc = "";
			}
			// 页面名称;
			String pageMcSQL = "select TZ_PAGE_MC from PS_TZ_AQ_PAGZC_TBL where TZ_COM_ID=? and TZ_PAGE_ID=?";
			String str_page_mc = jdbcTemplate.queryForObject(pageMcSQL, new Object[] { str_com_id, str_page_id },
					"String");
			if (str_page_mc == null) {
				str_page_mc = "";
			}

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("ComID", str_com_id);
			map.put("comMc", str_com_mc);
			map.put("PageID", str_page_id);
			map.put("pageMc", str_page_mc);
			map.put("ViewMc", str_view_name);
			if(psTzFilterDfnT != null){
				map.put("maxNum", psTzFilterDfnT.getTzResultMaxNum());
				map.put("advModel", psTzFilterDfnT.getTzAdvanceModel());
				map.put("baseSchEdit", psTzFilterDfnT.getTzBaseSchEdit());
			}
			returnJsonMap.replace("formData", map);
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
			String str_com_id = jacksonUtil.getString("ComID");
			String str_page_id = jacksonUtil.getString("PageID");
			String str_view_name = jacksonUtil.getString("ViewMc");
			String queryID = jacksonUtil.getString("queryID");
			int total = 0;
			
			if ("1".equals(queryID)) {
				String strDqFlgDesc="";
				String strDqFlg="";
				String strDqView="";
				String strDqFld="";
				
				// 查询总数;
				String totalSQL = "SELECT COUNT('Y') FROM PS_TZ_FILTER_FLD_T where TZ_COM_ID=? and TZ_PAGE_ID=? and TZ_VIEW_NAME=?";
				total = jdbcTemplate.queryForObject(totalSQL, new Object[] { str_com_id, str_page_id, str_view_name },
						"Integer");
	
				String sql = "SELECT TZ_FILTER_FLD,TZ_FILTER_FLD_DESC,TZ_SORT_NUM,TZ_FLD_READONLY,TZ_FLD_HIDE,TZ_PROMPT_TBL_NAME,TZ_PROMPT_FLD,ifnull(TZ_DEEPQUERY_FLG,'N')TZ_DEEPQUERY_FLG,TZ_DEEPQUERY_VIEW,TZ_DEEPQUERY_FLD FROM PS_TZ_FILTER_FLD_T where TZ_COM_ID=? and TZ_PAGE_ID=? and TZ_VIEW_NAME=? order by TZ_SORT_NUM asc limit ?,?";
				List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,
						new Object[] { str_com_id, str_page_id, str_view_name, numStart, numLimit });
				if (list != null && list.size()>0) {
					for (int i = 0; i < list.size(); i++) {
						String FieldMc = (String) list.get(i).get("TZ_FILTER_FLD");
						String fieldDesc = (String) list.get(i).get("TZ_FILTER_FLD_DESC");
						long num_xh = (long) list.get(i).get("TZ_SORT_NUM");
						String str_fld_readonly = (String) list.get(i).get("TZ_FLD_READONLY");
						String str_fld_hide = (String) list.get(i).get("TZ_FLD_HIDE");
						String str_prompt_tbl = (String) list.get(i).get("TZ_PROMPT_TBL_NAME");
						String str_prompt_fld = (String) list.get(i).get("TZ_PROMPT_FLD");
						
						strDqFlg="";
						strDqView="";
						strDqFld="";
						strDqFlgDesc="";
						
						 strDqFlg=(String) list.get(i).get("TZ_DEEPQUERY_FLG");
						 strDqView=(String) list.get(i).get("TZ_DEEPQUERY_VIEW");
						 strDqFld=(String) list.get(i).get("TZ_DEEPQUERY_FLD");
						 
						 if ("Y".equals(strDqFlg)){
							 strDqFlgDesc="是";
						 }else{
							 strDqFlgDesc="否";
						 }
						
											
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("ComID", str_com_id);
						map.put("PageID", str_page_id);
						map.put("ViewMc", str_view_name);
						map.put("FieldMc", FieldMc);
						map.put("fieldDesc", fieldDesc);
						map.put("fldReadonly", str_fld_readonly);
						map.put("fldHide", str_fld_hide);
						map.put("promptTab", str_prompt_tbl);
						map.put("promptFld", str_prompt_fld);
						map.put("orderNum", num_xh);
						
						map.put("deepQueryFlg", strDqFlg);
						map.put("deepQueryFlgDesc", strDqFlgDesc);
						map.put("deepQueryView", strDqView);
						map.put("deepQueryFld", strDqFld);
	
						listData.add(map);
					}
					
					mapRet.replace("total",total);
					mapRet.replace("root", listData);
				}
			}
			
			
			if ("2".equals(queryID)) {
				
				// 查询总数;
				String totalSQL = "SELECT COUNT('Y') FROM PS_TZ_FLTDST_FLD_T where TZ_COM_ID=? and TZ_PAGE_ID=? and TZ_VIEW_NAME=?";
				total = jdbcTemplate.queryForObject(totalSQL, new Object[] { str_com_id, str_page_id, str_view_name },
						"Integer");
	
				String sql = "SELECT TZ_FLTDST_ORDER,TZ_FLTDST_FLD,TZ_FLTDST_SRCH_REC,TZ_FLTDST_DESC,TZ_FLTDST_STATUS,ifnull(TZ_FLTDST_DEFAULT,'N')TZ_FLTDST_DEFAULT FROM PS_TZ_FLTDST_FLD_T where TZ_COM_ID=? and TZ_PAGE_ID=? and TZ_VIEW_NAME=? order by TZ_FLTDST_ORDER asc limit ?,?";
				List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,
						new Object[] { str_com_id, str_page_id, str_view_name, numStart, numLimit });
				if (list != null && list.size()>0) {
					for (int i = 0; i < list.size(); i++) {
						String strFltdstFld = (String) list.get(i).get("TZ_FLTDST_FLD");
						String strFltdstSrchRec = (String) list.get(i).get("TZ_FLTDST_SRCH_REC");
						int num_xh = (int) list.get(i).get("TZ_FLTDST_ORDER");
						String strFltdstDesc = (String) list.get(i).get("TZ_FLTDST_DESC");
						String strValidStatus = (String) list.get(i).get("TZ_FLTDST_STATUS");
						if("Y".equals(strValidStatus)){
							strValidStatus = "有效";
						}else{
							strValidStatus = "无效";
						}
						String strDefault = (String) list.get(i).get("TZ_FLTDST_DEFAULT");
											
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("ComID", str_com_id);
						map.put("PageID", str_page_id);
						map.put("ViewMc", str_view_name);
						map.put("fieldOrder", num_xh);
						map.put("dataSetFld", strFltdstFld);
						map.put("searchRec", strFltdstSrchRec);
						map.put("fldDstDesc", strFltdstDesc);
						map.put("fldDstStatus", strValidStatus);
						if("on".equals(strDefault)){
							map.put("fldDstDefault", true);
						}else{
							map.put("fldDstDefault", false);
						}

						listData.add(map);
					}
					mapRet.replace("total",total);
					mapRet.replace("root", listData);
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

					String isExist = "";
					String sql = "select 'Y' from PS_TZ_FILTER_DFN_T WHERE TZ_COM_ID=? AND TZ_PAGE_ID=? AND TZ_VIEW_NAME=?";
					isExist = jdbcTemplate.queryForObject(sql, new Object[] { str_com_id, str_page_id, str_view_name },
							"String");
					if ("Y".equals(isExist)) {
						errMsg[0] = "1";
						errMsg[1] = "试图添加的值已存在，请指定新值。";
						return strRet;
					} else {
						String num_max = (String) Json.get("maxNum");
						int num_max_num = 0;
						if (!"".equals(num_max) && StringUtils.isNumeric(num_max)) {
							num_max_num = Integer.parseInt(num_max);
						}
						String str_adv_model = (String) Json.get("advModel");
						String str_base_sch_edit = (String) Json.get("baseSchEdit");

						PsTzFilterDfnT psTzFilterDfnT = new PsTzFilterDfnT();
						psTzFilterDfnT.setTzComId(str_com_id);
						psTzFilterDfnT.setTzPageId(str_page_id);
						psTzFilterDfnT.setTzViewName(str_view_name);
						psTzFilterDfnT.setTzResultMaxNum(num_max_num);
						psTzFilterDfnT.setTzAdvanceModel(str_adv_model);
						psTzFilterDfnT.setTzBaseSchEdit(str_base_sch_edit);
						psTzFilterDfnTMapper.insert(psTzFilterDfnT);
					}
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

	@Override
	@SuppressWarnings("unchecked")
	/* 修改可配置定义信息 */
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
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
					String num_max = (String) Json.get("maxNum");
					int num_max_num = 0;
					if (!"".equals(num_max) && StringUtils.isNumeric(num_max)) {
						num_max_num = Integer.parseInt(num_max);
					}
					String str_adv_model = (String) Json.get("advModel");
					String str_base_sch_edit = (String) Json.get("baseSchEdit");

					PsTzFilterDfnT psTzFilterDfnT = new PsTzFilterDfnT();
					psTzFilterDfnT.setTzComId(str_com_id);
					psTzFilterDfnT.setTzPageId(str_page_id);
					psTzFilterDfnT.setTzViewName(str_view_name);
					psTzFilterDfnT.setTzResultMaxNum(num_max_num);
					psTzFilterDfnT.setTzAdvanceModel(str_adv_model);
					psTzFilterDfnT.setTzBaseSchEdit(str_base_sch_edit);
					psTzFilterDfnTMapper.updateByPrimaryKey(psTzFilterDfnT);
				}

				if (jacksonUtil.containsKey("updateList")) {
					
					List<Map<String, Object>> jsonArray = (List<Map<String, Object>>) jacksonUtil.getList("updateList");
					if(jsonArray != null && jsonArray.size()>0){
						for (int j = 0; j < jsonArray.size(); j++) {
							// 将字符串转换成json;
							Map<String, Object> Json2 = jsonArray.get(j);
	
							String str_com_id = (String) Json2.get("ComID");
							String str_page_id = (String) Json2.get("PageID");
							String str_view_name = (String) Json2.get("ViewMc");
							String str_field_name = (String) Json2.get("FieldMc");
							int num_order = (int) Json2.get("orderNum");
							PsTzFilterFldT psTzFilterFldT = new PsTzFilterFldT();
							psTzFilterFldT.setTzComId(str_com_id);
							psTzFilterFldT.setTzPageId(str_page_id);
							psTzFilterFldT.setTzViewName(str_view_name);
							psTzFilterFldT.setTzFilterFld(str_field_name);
							psTzFilterFldT.setTzSortNum(num_order);
							psTzFilterFldTMapper.updateByPrimaryKeySelective(psTzFilterFldT);
						}
					}
				}

			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
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
				// 类型标志;
				String strFlag = jacksonUtil.getString("typeFlag");
				// 信息内容;
				if ("FILTER".equals(strFlag)) {
					
					if (jacksonUtil.containsKey("removeList")) {
						
						List<Map<String, Object>> jsonArray = (List<Map<String, Object>>) jacksonUtil.getList("removeList");
						if(jsonArray != null && jsonArray.size()>0){
							for (int j = 0; j < jsonArray.size(); j++) {
								// 将字符串转换成json;
								Map<String, Object> Json2 = jsonArray.get(j);
		
								String str_com_id = (String) Json2.get("ComID");
								String str_page_id = (String) Json2.get("PageID");
								String str_view_name = (String) Json2.get("ViewMc");
								String str_field_name = (String) Json2.get("FieldMc");
								PsTzFilterFldTKey psTzFilterFldTKey = new PsTzFilterFldTKey();
								psTzFilterFldTKey.setTzComId(str_com_id);
								psTzFilterFldTKey.setTzPageId(str_page_id);
								psTzFilterFldTKey.setTzViewName(str_view_name);
								psTzFilterFldTKey.setTzFilterFld(str_field_name);
								psTzFilterFldTMapper.deleteByPrimaryKey(psTzFilterFldTKey);
								//Mabc，20170412，add-删除字段同时删除TZ_FILTER_YSF_T表
								String strDelYsfSql = "delete from PS_TZ_FILTER_YSF_T where TZ_COM_ID=? and TZ_PAGE_ID=? and TZ_VIEW_NAME=? and TZ_FILTER_FLD=?";
								jdbcTemplate.update(strDelYsfSql,new Object[] { str_com_id, str_page_id, str_view_name ,str_field_name});
								
								//Mabc，20170412，add-删除字段同时删除TZ_FLTPRM_FLD_T表
								String strDelPrmTblSql = "delete from PS_TZ_FLTPRM_FLD_T where TZ_COM_ID=? and TZ_PAGE_ID=? and TZ_VIEW_NAME=? and TZ_FILTER_FLD=?";
								jdbcTemplate.update(strDelPrmTblSql,new Object[] { str_com_id, str_page_id, str_view_name ,str_field_name});
							}
						}
					}
				}
				
				if ("DataSet".equals(strFlag)) {
					
					if (jacksonUtil.containsKey("removeList")) {
						
						List<Map<String, Object>> jsonArray = (List<Map<String, Object>>) jacksonUtil.getList("removeList");
						if(jsonArray != null && jsonArray.size()>0){
							for (int j = 0; j < jsonArray.size(); j++) {
								// 将字符串转换成json;
								Map<String, Object> Json2 = jsonArray.get(j);
		
								String str_com_id = (String) Json2.get("ComID");
								String str_page_id = (String) Json2.get("PageID");
								String str_view_name = (String) Json2.get("ViewMc");
								int num_field_Order = Integer.parseInt(String.valueOf(Json2.get("fieldOrder")));
								PsTzFltDstFldTKey psTzFltDstFldTKey = new PsTzFltDstFldTKey();
								psTzFltDstFldTKey.setTzComId(str_com_id);
								psTzFltDstFldTKey.setTzPageId(str_page_id);
								psTzFltDstFldTKey.setTzViewName(str_view_name);
								psTzFltDstFldTKey.setTzFltdstOrder(num_field_Order);
								
								psTzFltDstFldTMapper.deleteByPrimaryKey(psTzFltDstFldTKey);
								
								//删除字段同时删除PS_TZ_FLTDST_CON_T表
								String strDelFltCondSql = "delete from PS_TZ_FLTDST_CON_T where TZ_COM_ID=? and TZ_PAGE_ID=? and TZ_VIEW_NAME=? and TZ_FLTDST_ORDER=?";
								jdbcTemplate.update(strDelFltCondSql,new Object[] { str_com_id, str_page_id, str_view_name ,num_field_Order});
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
