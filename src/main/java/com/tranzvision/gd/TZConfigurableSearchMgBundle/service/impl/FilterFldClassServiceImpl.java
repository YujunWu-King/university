package com.tranzvision.gd.TZConfigurableSearchMgBundle.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.dao.PsTzFilterFldTMapper;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.dao.PsTzFilterYsfTMapper;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.dao.PsTzFltprmFldTMapper;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.model.PsTzFilterFldT;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.model.PsTzFilterFldTKey;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.model.PsTzFilterYsfT;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.model.PsTzFilterYsfTKey;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.model.PsTzFltprmFldT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TZUtility;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 可配置搜索配置页面
 * 
 * @author tang 原PS类 TZ_GD_FILTERGL_PKG:TZ_GD_FILTERFLD_CLS
 */

@Service("com.tranzvision.gd.TZConfigurableSearchMgBundle.service.impl.FilterFldClassServiceImpl")
public class FilterFldClassServiceImpl extends FrameworkImpl {
	@Autowired
	private PsTzFilterFldTMapper psTzFilterFldTMapper;
	@Autowired
	private PsTzFilterYsfTMapper psTzFilterYsfTMapper;
	@Autowired
	private PsTzFltprmFldTMapper psTzFltprmFldTMapper;
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private JacksonUtil jacksonUtil;
	

	/* 查询表单 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {

		// 返回值;
		String strRet = "{}";
		try {
			jacksonUtil.json2Map(strParams);
			
			String str_com_id = jacksonUtil.getString("ComID");
			String str_page_id = jacksonUtil.getString("PageID");
			String str_view_name = jacksonUtil.getString("ViewMc");
			String str_field_name = jacksonUtil.getString("FieldMc");

			PsTzFilterFldT psTzFilterFldT = new PsTzFilterFldT();
			PsTzFilterFldTKey psTzFilterFldTKey = new PsTzFilterFldTKey();
			psTzFilterFldTKey.setTzComId(str_com_id);
			psTzFilterFldTKey.setTzPageId(str_page_id);
			psTzFilterFldTKey.setTzViewName(str_view_name);
			psTzFilterFldTKey.setTzFilterFld(str_field_name);
			psTzFilterFldT = psTzFilterFldTMapper.selectByPrimaryKey(psTzFilterFldTKey);

			if (psTzFilterFldT != null) {
				int maxNum = 0;
				if (psTzFilterFldT.getTzResultMaxNum() != null) {
					maxNum = psTzFilterFldT.getTzResultMaxNum();
				}
				strRet = "{\"ComID\":\"" + psTzFilterFldT.getTzComId() + "\",\"PageID\":\""
						+ psTzFilterFldT.getTzPageId() + "\",\"ViewMc\":\"" + psTzFilterFldT.getTzViewName()
						+ "\",\"FieldMc\":\"" + psTzFilterFldT.getTzFilterFld() + "\",\"fieldDesc\":\""
						+ TZUtility.transFormchar(psTzFilterFldT.getTzFilterFldDesc()) + "\",\"promptTab\":\""
						+ TZUtility.transFormchar(psTzFilterFldT.getTzPromptTblName()) + "\",\"promptFld\":\""
						+ TZUtility.transFormchar(psTzFilterFldT.getTzPromptFld()) + "\",\"promptDesc\":\""
						+ TZUtility.transFormchar(psTzFilterFldT.getTzPromptDescFld()) + "\",\"maxNum\":\"" + maxNum
						+ "\",\"fldReadonly\":\"" + TZUtility.transFormchar(psTzFilterFldT.getTzFldReadonly())
						+ "\",\"fldHide\":\"" + TZUtility.transFormchar(psTzFilterFldT.getTzFldHide())
						+ "\",\"fldIsDown\":\"" + TZUtility.transFormchar(psTzFilterFldT.getTzIsdownFld())
						+ "\",\"fltFldQzLx\":\"" + TZUtility.transFormchar(psTzFilterFldT.getTzFltFldQzType())
						+ "\",\"translateValueFld\":\"" + TZUtility.transFormchar(psTzFilterFldT.getTzZhzjhId())
						+ "\",\"fltFldNoUpperLower\": \"" + TZUtility.transFormchar(psTzFilterFldT.getTzNoUporlow())
						+ "\"}";
				strRet = "{\"formData\":" + strRet + "}";
			}

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
		String strRet = "";
		try {

			// 将字符串转换成json;
			jacksonUtil.json2Map(comParams);

			String str_com_id = jacksonUtil.getString("ComID");
			String str_page_id = jacksonUtil.getString("PageID");
			String str_view_name = jacksonUtil.getString("ViewMc");
			String str_field_name = jacksonUtil.getString("FieldMc");
			String queryID = jacksonUtil.getString("queryID");
			int total = 0;
			String totalSQL = "";
			String sql = "";
			if ("1".equals(queryID)) {
				totalSQL = "SELECT COUNT('Y') FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID='TZ_FILTER_YSF' AND TZ_EFF_STATUS='A'";
				total = jdbcTemplate.queryForObject(totalSQL, "Integer");
				sql = "SELECT TZ_ZHZ_ID,TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID='TZ_FILTER_YSF' AND TZ_EFF_STATUS='A' limit ?,?";
				List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[] { numStart, numLimit });
				if (list != null) {
					for (int i = 0; i < list.size(); i++) {
						String str_field_ysfid = (String) list.get(i).get("TZ_ZHZ_ID");
						String str_field_ysf = (String) list.get(i).get("TZ_ZHZ_DMS");
						PsTzFilterYsfTKey psTzFilterYsfTKey = new PsTzFilterYsfTKey();
						psTzFilterYsfTKey.setTzComId(str_com_id);
						psTzFilterYsfTKey.setTzPageId(str_page_id);
						psTzFilterYsfTKey.setTzViewName(str_view_name);
						psTzFilterYsfTKey.setTzFilterFld(str_field_name);
						psTzFilterYsfTKey.setTzFilterYsf(str_field_ysfid);
						PsTzFilterYsfT psTzFilterYsfT = psTzFilterYsfTMapper.selectByPrimaryKey(psTzFilterYsfTKey);

						String str_is_qy = "", str_is_oprt = "";
						if (psTzFilterYsfT != null) {
							str_is_qy = TZUtility.transFormchar(psTzFilterYsfT.getTzFilterBdyQy());
							str_is_oprt = TZUtility.transFormchar(psTzFilterYsfT.getTzIsDefOprt());
						}
						strRet = strRet + ",{\"ComID\":\"" + str_com_id + "\",\"PageID\":\"" + str_page_id
								+ "\",\"ViewMc\":\"" + str_view_name + "\",\"FieldMc\":\"" + str_field_name
								+ "\",\"orderNum\":\"" + (i + 1) + "\",\"FieldYsfID\":\"" + str_field_ysfid
								+ "\",\"FieldYsf\":\"" + str_field_ysf + "\",\"isQy\":\"" + str_is_qy
								+ "\",\"isOprt\":\"" + str_is_oprt + "\"}";
					}
					if (!"".equals(strRet)) {
						strRet = strRet.substring(1);
					}
					strRet = "{\"total\":" + total + ",\"root\":[" + strRet + "]}";
				}

			}

			if ("2".equals(queryID)) {
				totalSQL = "SELECT COUNT('Y') FROM PS_TZ_FLTPRM_FLD_T where TZ_COM_ID=? and TZ_PAGE_ID=? and TZ_VIEW_NAME=? and TZ_FILTER_FLD=?";
				total = jdbcTemplate.queryForObject(totalSQL,
						new Object[] { str_com_id, str_page_id, str_view_name, str_field_name }, "Integer");
				sql = "SELECT TZ_FILTER_GL_FLD,TZ_FILTER_ORDER FROM PS_TZ_FLTPRM_FLD_T where TZ_COM_ID=? and TZ_PAGE_ID=? and TZ_VIEW_NAME=? and TZ_FILTER_FLD=? order by TZ_FILTER_ORDER asc limit ?,?";
				List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,
						new Object[] { str_com_id, str_page_id, str_view_name, str_field_name, numStart, numLimit });
				
				if (list != null) {
					for (int i = 0; i < list.size(); i++) {
						long num_glxh = 0;
						try{
							num_glxh = (long) list.get(i).get("TZ_FILTER_ORDER");
						}catch(Exception e){
							num_glxh = 0;
						}
						
						String str_fieldgl_mc = (String) list.get(i).get("TZ_FILTER_GL_FLD");
						String str_fieldgl_desc = "";

						strRet = strRet + ",{\"ComID\":\"" + str_com_id + "\",\"PageID\":\"" + str_page_id
								+ "\",\"ViewMc\":\"" + str_view_name + "\",\"FieldMc\":\"" + str_field_name
								+ "\",\"orderNum\":\"" + num_glxh + "\",\"FieldGL\":\"" + str_fieldgl_mc
								+ "\",\"fieldDesc\":\"" + str_fieldgl_desc + "\"}";
					}
					if (strRet != null && !"".equals(strRet)) {
						strRet = strRet.substring(1);
					}
					strRet = "{\"total\":" + total + ",\"root\":[" + strRet + "]}";
				}
				
			}

		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return strRet;
	}

	@Override
	/* 修改 */
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				
				jacksonUtil.json2Map(strForm);
				System.out.println("------------werwerwe------>"+strForm);
				// 信息内容;

				Map<String, Object> jsonObject = jacksonUtil.getMap("update");
				String str_com_id = (String) jsonObject.get("ComID");
				String str_page_id = (String) jsonObject.get("PageID");
				String str_view_name = (String) jsonObject.get("ViewMc");
				String str_field_name = (String) jsonObject.get("FieldMc");
				String str_field_desc = (String) jsonObject.get("fieldDesc");
				String str_prompt_tbl = (String) jsonObject.get("promptTab");
				String str_prompt_fld = (String) jsonObject.get("promptFld");
				String str_prompt_desc = (String) jsonObject.get("promptDesc");
				String num_max_String = (String) jsonObject.get("maxNum");
				int num_max_num = 0;
				if (!"".equals(num_max_String) && StringUtils.isNumeric(num_max_String)) {
					num_max_num = Integer.parseInt(num_max_String);
				}
				String str_readonly = (String) jsonObject.get("fldReadonly");
				String str_hide = (String) jsonObject.get("fldHide");
				String fldIsDown = (String) jsonObject.get("fldIsDown");

				String fltFldQzLx = (String) jsonObject.get("fltFldQzLx");
				String translateValueFld = (String) jsonObject.get("translateValueFld");
				String fltFldNoUpperLower = (String) jsonObject.get("fltFldNoUpperLower");

				PsTzFilterFldT psTzFilterFldT = new PsTzFilterFldT();
				psTzFilterFldT.setTzComId(str_com_id);
				psTzFilterFldT.setTzPageId(str_page_id);
				psTzFilterFldT.setTzViewName(str_view_name);
				psTzFilterFldT.setTzFilterFld(str_field_name);
				psTzFilterFldT.setTzFilterFldDesc(str_field_desc);
				psTzFilterFldT.setTzPromptTblName(str_prompt_tbl);
				psTzFilterFldT.setTzPromptFld(str_prompt_fld);
				psTzFilterFldT.setTzPromptDescFld(str_prompt_desc);
				psTzFilterFldT.setTzResultMaxNum(num_max_num);
				psTzFilterFldT.setTzFldReadonly(str_readonly);
				psTzFilterFldT.setTzFldHide(str_hide);
				psTzFilterFldT.setTzIsdownFld(fldIsDown);
				psTzFilterFldT.setTzFltFldQzType(fltFldQzLx);
				psTzFilterFldT.setTzZhzjhId(translateValueFld);
				psTzFilterFldT.setTzNoUporlow(fltFldNoUpperLower);
				psTzFilterFldTMapper.updateByPrimaryKeySelective(psTzFilterFldT);
				
				Map<String, Object> jObj = null;
				Map<String, Object> updateListJson = jacksonUtil.getMap("updateList");
				try {
					List<Map<String, Object>> jsonArray2 = (List<Map<String, Object>>) updateListJson.get("data1");
					if (jsonArray2 != null && jsonArray2.size() > 0) {
						for (int i = 0; i < jsonArray2.size(); i++) {
							jObj = jsonArray2.get(i);
							String str_fldysf_name = (String) jObj.get("FieldYsfID");
							boolean is_qy = (boolean) jObj.get("isQy");
							String str_is_qy = "";
							String str_is_oprt = (String) jObj.get("isOprt");
							if (is_qy == true) {
								str_is_qy = "1";
							} else {
								str_is_qy = "0";
							}

							String isExist = "";
							String isExistSQL = "select 'Y' from PS_TZ_FILTER_YSF_T WHERE TZ_COM_ID=? AND  TZ_PAGE_ID=? AND TZ_VIEW_NAME=? AND TZ_FILTER_FLD=? AND TZ_FILTER_YSF=?";
							isExist = jdbcTemplate.queryForObject(isExistSQL, new Object[] { str_com_id, str_page_id,
									str_view_name, str_field_name, str_fldysf_name }, "String");

							PsTzFilterYsfT psTzFilterYsfT = new PsTzFilterYsfT();
							psTzFilterYsfT.setTzComId(str_com_id);
							psTzFilterYsfT.setTzPageId(str_page_id);
							psTzFilterYsfT.setTzViewName(str_view_name);
							psTzFilterYsfT.setTzFilterFld(str_field_name);
							psTzFilterYsfT.setTzFilterYsf(str_fldysf_name);
							psTzFilterYsfT.setTzFilterBdyQy(str_is_qy);
							psTzFilterYsfT.setTzIsDefOprt(str_is_oprt);

							if ("Y".equals(isExist)) {
								psTzFilterYsfTMapper.updateByPrimaryKeySelective(psTzFilterYsfT);
							} else {
								psTzFilterYsfTMapper.insert(psTzFilterYsfT);
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				try {
					List<Map<String, Object>> jsonArray4 = (List<Map<String, Object>>) updateListJson.get("data2");
					
					if (jsonArray4 != null && jsonArray4.size() > 0) {
						String deleteSQL = "delete from PS_TZ_FLTPRM_FLD_T where TZ_COM_ID=? and TZ_PAGE_ID=? and TZ_VIEW_NAME=? and TZ_FILTER_FLD=?";
						jdbcTemplate.update(deleteSQL,new Object[]{str_com_id,str_page_id,str_view_name,str_field_name});
						for (int i = 0; i < jsonArray4.size(); i++) {
							jObj = jsonArray4.get(i);
							String str_fldgl_name = (String) jObj.get("FieldGL");
							
							PsTzFltprmFldT psTzFltprmFld = new PsTzFltprmFldT();
							psTzFltprmFld.setTzComId(str_com_id);
							psTzFltprmFld.setTzPageId(str_page_id);
							psTzFltprmFld.setTzViewName(str_view_name);
							psTzFltprmFld.setTzFilterFld(str_field_name);
							psTzFltprmFld.setTzFilterGlFld(str_fldgl_name);
							psTzFltprmFld.setTzFilterOrder(i +1);
							psTzFltprmFldTMapper.insert(psTzFltprmFld);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				try {
					
					List<Map<String, Object>> jsonArray3 = (List<Map<String, Object>>) updateListJson.get("data3");
					if (jsonArray3 != null && jsonArray3.size() > 0) {
						
						for (int i = 0; i < jsonArray3.size(); i++) {
							jObj = jsonArray3.get(i);
							String str_field_gl = (String) jObj.get("FieldGL");
							
							String deleteSQL = "delete from PS_TZ_FLTPRM_FLD_T where TZ_COM_ID=? and TZ_PAGE_ID=? and TZ_VIEW_NAME=? and TZ_FILTER_FLD=? and TZ_FILTER_GL_FLD=?";
							jdbcTemplate.update(deleteSQL,new Object[]{str_com_id,str_page_id,str_view_name,str_field_name,str_field_gl});
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
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
