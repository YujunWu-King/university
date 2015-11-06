package com.tranzvision.gd.TZConfigurableSearchMgBundle.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.dao.PsTzFilterDfnTMapper;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.dao.PsTzFilterFldTMapper;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.model.PsTzFilterDfnT;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.model.PsTzFilterDfnTKey;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.model.PsTzFilterFldT;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.model.PsTzFilterFldTKey;
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
	private SqlQuery jdbcTemplate;
	@Autowired
	private JacksonUtil jacksonUtil;

	/* 查询表单 */

	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "{}";
		try {
			jacksonUtil.json2Map(strParams);
			String str_com_id = jacksonUtil.getString("ComID");
			String str_page_id = jacksonUtil.getString("PageID");
			String str_view_name = jacksonUtil.getString("ViewMc");
			PsTzFilterDfnTKey psTzFilterDfnTKey = new PsTzFilterDfnTKey();
			psTzFilterDfnTKey.setTzComId(str_com_id);
			;
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
			strRet = jacksonUtil.Map2json(map); 
			strRet = "{\"formData\":" + strRet + "}";
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

	/* 查询可配置搜索字段列表 */
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		String strRet = "{\"total\":0,\"root\":[]}";
		String strContent = "";
		try {

			// 将字符串转换成json;
			jacksonUtil.json2Map(comParams);
			String str_com_id = jacksonUtil.getString("ComID");
			String str_page_id = jacksonUtil.getString("PageID");
			String str_view_name = jacksonUtil.getString("ViewMc");

			int total = 0;
			// 查询总数;
			String totalSQL = "SELECT COUNT('Y') FROM PS_TZ_FILTER_FLD_T where TZ_COM_ID=? and TZ_PAGE_ID=? and TZ_VIEW_NAME=?";
			total = jdbcTemplate.queryForObject(totalSQL, new Object[] { str_com_id, str_page_id, str_view_name },
					"Integer");

			String sql = "SELECT TZ_FILTER_FLD,TZ_FILTER_FLD_DESC,TZ_SORT_NUM,TZ_FLD_READONLY,TZ_FLD_HIDE,TZ_PROMPT_TBL_NAME,TZ_PROMPT_FLD FROM PS_TZ_FILTER_FLD_T where TZ_COM_ID=? and TZ_PAGE_ID=? and TZ_VIEW_NAME=? order by TZ_SORT_NUM asc limit ?,?";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,
					new Object[] { str_com_id, str_page_id, str_view_name, numStart, numLimit });
			if (list == null) {
				strRet = "{\"total\":0,\"root\":[]}";
			} else {
				for (int i = 0; i < list.size(); i++) {
					String FieldMc = (String) list.get(i).get("TZ_FILTER_FLD");
					String fieldDesc = (String) list.get(i).get("TZ_FILTER_FLD_DESC");
					long num_xh = (long) list.get(i).get("TZ_SORT_NUM");
					String str_fld_readonly = (String) list.get(i).get("TZ_FLD_READONLY");
					String str_fld_hide = (String) list.get(i).get("TZ_FLD_HIDE");
					String str_prompt_tbl = (String) list.get(i).get("TZ_PROMPT_TBL_NAME");
					String str_prompt_fld = (String) list.get(i).get("TZ_PROMPT_FLD");
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

					strContent = strContent + "," + jacksonUtil.Map2json(map);
				}
				if (!"".equals(strContent)) {
					strContent = strContent.substring(1);
				}

				strRet = "{\"total\":" + total + ",\"root\":[" + strContent + "]}";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strRet;
	}

	@Override
	/* 添加可配置搜索 */
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "{}";
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
	/* 修改可配置定义信息 */
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
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
		String strRet = "{}";

		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 信息内容;
				String str_com_id = jacksonUtil.getString("ComID");
				String str_page_id = jacksonUtil.getString("PageID");
				String str_view_name = jacksonUtil.getString("ViewMc");
				String str_field_name = jacksonUtil.getString("FieldMc");
				PsTzFilterFldTKey psTzFilterFldTKey = new PsTzFilterFldTKey();
				psTzFilterFldTKey.setTzComId(str_com_id);
				psTzFilterFldTKey.setTzPageId(str_page_id);
				psTzFilterFldTKey.setTzViewName(str_view_name);
				psTzFilterFldTKey.setTzFilterFld(str_field_name);
				psTzFilterFldTMapper.deleteByPrimaryKey(psTzFilterFldTKey);

			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
}
