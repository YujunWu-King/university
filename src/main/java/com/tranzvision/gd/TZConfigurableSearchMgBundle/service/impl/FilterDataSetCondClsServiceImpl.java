package com.tranzvision.gd.TZConfigurableSearchMgBundle.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.dao.PsTzFltDstConTMapper;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.dao.PsTzFltDstFldTMapper;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.dao.PsTzFltdstRoleTMapper;
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

@Service("com.tranzvision.gd.TZConfigurableSearchMgBundle.service.impl.FilterDataSetCondClsServiceImpl")
public class FilterDataSetCondClsServiceImpl extends FrameworkImpl {
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
			String strCondOrder =  jacksonUtil.getString("orderCond");
			
			PsTzFltDstConTKey psTzFltDstConTKey = new PsTzFltDstConTKey();
			psTzFltDstConTKey.setTzComId(str_com_id);
			psTzFltDstConTKey.setTzPageId(str_page_id);
			psTzFltDstConTKey.setTzViewName(str_view_name);
			psTzFltDstConTKey.setTzFltdstOrder(Integer.valueOf(strFieldOrder));
			psTzFltDstConTKey.setTzFltdstCOrder(Integer.valueOf(strCondOrder));
			PsTzFltDstConT psTzFltDstConT = psTzFltDstConTMapper.selectByPrimaryKey(psTzFltDstConTKey);
			
			
			PsTzFltDstFldTKey psTzFltDstFldTKey = new PsTzFltDstFldTKey();
			psTzFltDstFldTKey.setTzComId(str_com_id);
			psTzFltDstFldTKey.setTzPageId(str_page_id);
			psTzFltDstFldTKey.setTzViewName(str_view_name);
			psTzFltDstFldTKey.setTzFltdstOrder(Integer.valueOf(strFieldOrder));
			PsTzFltDstFldT psTzFilterDfnT = psTzFltDstFldTMapper.selectByPrimaryKey(psTzFltDstFldTKey);
			
			if(psTzFltDstConT!=null && psTzFilterDfnT !=null){
				
				String strFltdstSrchRec = psTzFilterDfnT.getTzFltdstSrchRec() == null ? "" : psTzFilterDfnT.getTzFltdstSrchRec();
				String strFltdstDesc = psTzFilterDfnT.getTzFltdstDesc() == null ? "" : psTzFilterDfnT.getTzFltdstDesc();
				
				String strDstAndOr = psTzFltDstConT.getTzFltdstAndOr() == null ? "" : psTzFltDstConT.getTzFltdstAndOr();
				String strLeftParen = psTzFltDstConT.getTzFltdstLParen() == null ? "" : psTzFltDstConT.getTzFltdstLParen();
				String strDstCondFld = psTzFltDstConT.getTzFltdstConFld() == null ? "" : psTzFltDstConT.getTzFltdstConFld();
				String strDstOperator = psTzFltDstConT.getTzFltdstOperator() == null ? "" : psTzFltDstConT.getTzFltdstOperator();
				String strDstCondValueType = psTzFltDstConT.getTzFltdstFldVT() == null ? "" : psTzFltDstConT.getTzFltdstFldVT();
				String strDstCondFldValue = psTzFltDstConT.getTzFltdstFldVal() == null ? "" : psTzFltDstConT.getTzFltdstFldVal();
				String strRightParen = psTzFltDstConT.getTzFltdstRParen() == null ? "" : psTzFltDstConT.getTzFltdstRParen();
				String strUpOrLow = psTzFltDstConT.getTzNoUporlow() == null ? "" : psTzFltDstConT.getTzNoUporlow();
				
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("ComID", str_com_id);
				map.put("PageID", str_page_id);
				map.put("ViewMc", str_view_name);
				map.put("fieldOrder", strFieldOrder);
				map.put("searchRec", strFltdstSrchRec);
				map.put("fldDstDesc", strFltdstDesc);
				map.put("orderCond", strCondOrder);
				map.put("dstAndOr", strDstAndOr);
				map.put("leftParen", strLeftParen);
				map.put("dstCondFld", strDstCondFld);
				map.put("dstOperator", strDstOperator);
				map.put("dstCondValueType", strDstCondValueType);
				map.put("dstCondFldValue", strDstCondFldValue);
				map.put("rightParen", strRightParen);
				map.put("fltFldNoUpperLower", strUpOrLow);

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

	@Override
	/* 添加可配置搜索 */
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
		
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("ComID", "");
		returnJsonMap.put("PageID", "");
		returnJsonMap.put("ViewMc", "");
		returnJsonMap.put("fieldOrder", "");
		returnJsonMap.put("orderCond", "");

		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 将字符串转换成json;
				Map<String, Object> Json = jacksonUtil.getMap("data");
				
				String str_com_id = (String) Json.get("ComID");
				String str_page_id = (String) Json.get("PageID");
				String str_view_name = (String) Json.get("ViewMc");
				String strFieldOrder = (String) Json.get("fieldOrder");
				int numFieldOrder = Integer.parseInt(strFieldOrder);
				String strCondOrder = (String) Json.get("orderCond");
				
				String strDstAndOr = (String) Json.get("dstAndOr");
				String strLeftParen = (String) Json.get("leftParen");
				String strDstCondFld = (String) Json.get("dstCondFld");
				String strDstOperator = (String) Json.get("dstOperator");
				String strDstCondValueType = (String) Json.get("dstCondValueType");
				String strDstCondFldValue = (String) Json.get("dstCondFldValue");
				String strRightParen = (String) Json.get("rightParen");
				String strUpOrLowCase = (String) Json.get("fltFldNoUpperLower");

				String num_max = "";
				int num_max_num = 0;
				
				if("".equals(strCondOrder)){
					String sqlGetMaxNum = "select TZ_FLTDST_C_ORDER from PS_TZ_FLTDST_CON_T WHERE TZ_COM_ID=? AND TZ_PAGE_ID=? AND TZ_VIEW_NAME=? AND TZ_FLTDST_ORDER = ? ORDER BY TZ_FLTDST_C_ORDER DESC LIMIT 0,1";
					num_max = jdbcTemplate.queryForObject(sqlGetMaxNum, new Object[] { str_com_id, str_page_id, str_view_name,strFieldOrder },
							"String");
					if(num_max==null||"".equals(num_max)){
						num_max_num = 1;
					}else{
						num_max_num = Integer.parseInt(num_max) + 1;
					}
				
					PsTzFltDstConT psTzFltDstConT = new PsTzFltDstConT();
					psTzFltDstConT.setTzComId(str_com_id);
					psTzFltDstConT.setTzPageId(str_page_id);
					psTzFltDstConT.setTzViewName(str_view_name);
					psTzFltDstConT.setTzFltdstOrder(numFieldOrder);
					psTzFltDstConT.setTzFltdstCOrder(num_max_num);
					psTzFltDstConT.setTzFltdstAndOr(strDstAndOr);
					psTzFltDstConT.setTzFltdstLParen(strLeftParen);
					psTzFltDstConT.setTzFltdstConFld(strDstCondFld);
					psTzFltDstConT.setTzFltdstOperator(strDstOperator);
					psTzFltDstConT.setTzFltdstFldVal(strDstCondFldValue);
					psTzFltDstConT.setTzFltdstFldVT(strDstCondValueType);
					psTzFltDstConT.setTzFltdstRParen(strRightParen);
					psTzFltDstConT.setTzNoUporlow(strUpOrLowCase);
										
					
					int i = psTzFltDstConTMapper.insert(psTzFltDstConT);
					
					if (i > 0) {
						returnJsonMap.replace("ComID", str_com_id);
						returnJsonMap.replace("PageID", str_page_id);
						returnJsonMap.replace("ViewMc", str_view_name);
						returnJsonMap.replace("fieldOrder", strFieldOrder);
						returnJsonMap.replace("orderCond", num_max_num);
					} else {
						errMsg[0] = "1";
						errMsg[1] = "数据保存失败";
					}
				}else{
					num_max_num = Integer.parseInt(strCondOrder);
					PsTzFltDstConT psTzFltDstConT = new PsTzFltDstConT();
					psTzFltDstConT.setTzComId(str_com_id);
					psTzFltDstConT.setTzPageId(str_page_id);
					psTzFltDstConT.setTzViewName(str_view_name);
					psTzFltDstConT.setTzFltdstOrder(numFieldOrder);
					psTzFltDstConT.setTzFltdstCOrder(num_max_num);
					psTzFltDstConT.setTzFltdstAndOr(strDstAndOr);
					psTzFltDstConT.setTzFltdstLParen(strLeftParen);
					psTzFltDstConT.setTzFltdstConFld(strDstCondFld);
					psTzFltDstConT.setTzFltdstOperator(strDstOperator);
					psTzFltDstConT.setTzFltdstFldVal(strDstCondFldValue);
					psTzFltDstConT.setTzFltdstFldVT(strDstCondValueType);
					psTzFltDstConT.setTzFltdstRParen(strRightParen);
					psTzFltDstConT.setTzNoUporlow(strUpOrLowCase);
					
					int i = psTzFltDstConTMapper.updateByPrimaryKeySelective(psTzFltDstConT);
					
					if (i > 0) {
						returnJsonMap.replace("ComID", str_com_id);
						returnJsonMap.replace("PageID", str_page_id);
						returnJsonMap.replace("ViewMc", str_view_name);
						returnJsonMap.replace("fieldOrder", strFieldOrder);
						returnJsonMap.replace("orderCond", num_max_num);
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
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;

			for (num = 0; num < actData.length; num++) {
				
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				if (jacksonUtil.containsKey("CompID")&&jacksonUtil.containsKey("PageID")&&jacksonUtil.containsKey("ViewMc")&&jacksonUtil.containsKey("RoleID")) {
					
					String strCompID = jacksonUtil.getString("CompID");
					String strPageID = jacksonUtil.getString("PageID");
					String strViewID = jacksonUtil.getString("ViewMc");
					String strRoleIDAll = jacksonUtil.getString("RoleID");
					String strFieldOrder = jacksonUtil.getString("fieldOrder");
					int fieldOrder=Integer.valueOf(strFieldOrder);
					String[] strRoleID=strRoleIDAll.split(",");
					
					
					for (int i = 0; i < strRoleID.length; i++) {
						String isExistSql = "SELECT count(1) FROM PS_TZ_FLTDST_ROLE_T WHERE TZ_COM_ID=? and TZ_PAGE_ID=? and TZ_VIEW_NAME=? and ROLENAME=? and TZ_FLTDST_ORDER=?";
						int count = jdbcTemplate.queryForObject(isExistSql, new Object[] { strCompID, strPageID, strViewID , strRoleID[i],strFieldOrder}, "Integer");

						if (count == 1) {
							continue;
						} else {
							String strRoleNameSql = "SELECT DESCR FROM PSROLEDEFN_VW where ROLENAME=?";
							String strRoleName = jdbcTemplate.queryForObject(strRoleNameSql, new Object[] { strRoleID[i]}, "String");
							
							String num_max = "";
							int num_max_num = 0;
							String sqlGetMaxNum = "select TZ_FLTDST_ORDER from PS_TZ_FLTDST_ROLE_T WHERE TZ_COM_ID=? AND TZ_PAGE_ID=? AND TZ_VIEW_NAME=? ORDER BY TZ_FLTDST_ORDER DESC LIMIT 0,1";
							num_max = jdbcTemplate.queryForObject(sqlGetMaxNum, new Object[] { strCompID, strPageID, strViewID },
									"String");
							if(num_max==null||"".equals(num_max)){
								num_max_num = 1;
							}else{
								num_max_num = Integer.parseInt(num_max) + 1;
							}
							
							PsTzFltdstRoleT psTzFltdstRoleT = new PsTzFltdstRoleT();

							psTzFltdstRoleT.setTzComId(strCompID);
							psTzFltdstRoleT.setTzPageId(strPageID);
							psTzFltdstRoleT.setTzViewName(strViewID);
							psTzFltdstRoleT.setTzFltdstOrder(fieldOrder);
							psTzFltdstRoleT.setRolename(strRoleID[i]);
							psTzFltdstRoleT.setDescr(strRoleName);

							int k = psTzFltdstRoleTMapper.insert(psTzFltdstRoleT);

							if (k > 0) {
							} else {
								errMsg[0] = "1";
								errMsg[1] = "信息保存失败";
							}
						}
						
					}
					

	
				} else {
					errMsg[0] = "1";
					errMsg[1] = "参数错误";
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
	
}
