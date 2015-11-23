package com.tranzvision.gd.TZOrganizationSiteMgBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiAtypTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiAtypT;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiAtypTKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 高端产品-站点区域类型设置；原：TZ_GD_ZDDY_PKG:TZ_AREATYPEGL_CLS
 * 
 * @author tang
 * @since 2015-11-18
 * 
 */
@Service("com.tranzvision.gd.TZOrganizationSiteMgBundle.service.impl.OrgSiteAreaTypeGlServiceImpl")
public class OrgSiteAreaTypeGlServiceImpl extends FrameworkImpl {
	@Autowired 
	private SqlQuery jdbcTemplate;
	@Autowired
	private JacksonUtil jacksonUtil;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private PsTzSiteiAtypTMapper psTzSiteiAtypTMapper;
	
	/* 查询区域类型管理列表 */
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("total", 0);
		returnJsonMap.put("root", "[]");
		
		try {
			jacksonUtil.json2Map(comParams);
			if(jacksonUtil.containsKey("siteId")){
				String siteId = jacksonUtil.getString("siteId");
				
				String totalSQL = "SELECT COUNT(1) FROM PS_TZ_SITEI_ATYP_T where TZ_SITEI_ID = ?";
				int total = jdbcTemplate.queryForObject(totalSQL,new Object[]{siteId}, "Integer");
				String sql = "";
				List<Map<String, Object>> list = null;
				if(numLimit > 0){
					sql = "SELECT  TZ_AREA_TYPE_ID ,TZ_AREA_TYPE_NAME ,TZ_AREA_TYPE_STATE ,TZ_AREA_SET_CODE ,TZ_AREA_HTML_CODE  FROM PS_TZ_SITEI_ATYP_T where TZ_SITEI_ID=? ORDER BY TZ_AREA_TYPE_ID ASC LIMIT ?,?";
					list = jdbcTemplate.queryForList(sql,new Object[]{siteId,numStart,numLimit});
				}else{
					sql = "SELECT  TZ_AREA_TYPE_ID ,TZ_AREA_TYPE_NAME ,TZ_AREA_TYPE_STATE ,TZ_AREA_SET_CODE ,TZ_AREA_HTML_CODE  FROM PS_TZ_SITEI_ATYP_T where TZ_SITEI_ID=? ORDER BY TZ_AREA_TYPE_ID ASC";
					list = jdbcTemplate.queryForList(sql,new Object[]{siteId});
				}
				if(list != null){
					ArrayList<Map<String, Object>> arraylist = new ArrayList<Map<String, Object>>();
					for(int i = 0; i<list.size();i++){
						Map<String, Object> jsonMap = new HashMap<String, Object>();
						jsonMap.put("siteId", siteId);
						jsonMap.put("areatypeid", list.get(i).get("TZ_AREA_TYPE_ID"));
						jsonMap.put("areatypename", list.get(i).get("TZ_AREA_TYPE_NAME"));
						jsonMap.put("areatypestate", list.get(i).get("TZ_AREA_TYPE_STATE"));
						jsonMap.put("areasetcode", list.get(i).get("TZ_AREA_SET_CODE"));
						jsonMap.put("areahtmlcode", list.get(i).get("TZ_AREA_HTML_CODE"));
						arraylist.add(jsonMap);
					}
					returnJsonMap.replace("total", total);
					returnJsonMap.replace("root", arraylist);
					strRet = jacksonUtil.Map2json(returnJsonMap);
				}
			}
		} catch (Exception e) {
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
	
	
	/* 添加区域类型设置 */
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "{}";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("areatypeid", "");

		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				String siteId = jacksonUtil.getString("siteId");
				String areatypename = jacksonUtil.getString("areatypename");
				String areatypestate = jacksonUtil.getString("areatypestate");
				String areasetcode = jacksonUtil.getString("areasetcode");
				String areahtmlcode = jacksonUtil.getString("areahtmlcode");
				String areatypearea = jacksonUtil.getString("areatypearea");
				String areatypeid = String.valueOf(getSeqNum.getSeqNum("TZ_SITEI_ATYP_T", "TZ_AREA_TYPE_ID"));
				
				
				PsTzSiteiAtypT psTzSiteiAtypT = new PsTzSiteiAtypT();
				psTzSiteiAtypT.setTzSiteiId(siteId);
				psTzSiteiAtypT.setTzAreaTypeId(areatypeid);
				psTzSiteiAtypT.setTzAreaTypeName(areatypename);
				psTzSiteiAtypT.setTzAreaTypeState(areatypestate);
				psTzSiteiAtypT.setTzAreaSetCode(areasetcode);
				psTzSiteiAtypT.setTzAreaHtmlCode(areahtmlcode);
				psTzSiteiAtypT.setTzAreaType(areatypearea);
				int i = psTzSiteiAtypTMapper.insert(psTzSiteiAtypT);
				if(i > 0){
					returnJsonMap.replace("areatypeid", areatypeid);
				}else{
					errMsg[0] = "1";
					errMsg[1] = "站点区域类型信息保存失败";
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
	
	/* 修改区域类型设置 */
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("areatypeid", "");

		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				String siteId = jacksonUtil.getString("siteId");
				String areatypeid = jacksonUtil.getString("areatypeid");
				String areatypename = jacksonUtil.getString("areatypename");
				String areatypestate = jacksonUtil.getString("areatypestate");
				String areasetcode = jacksonUtil.getString("areasetcode");
				String areahtmlcode = jacksonUtil.getString("areahtmlcode");
				String areatypearea = jacksonUtil.getString("areatypearea");

				PsTzSiteiAtypT psTzSiteiAtypT = new PsTzSiteiAtypT();
				psTzSiteiAtypT.setTzSiteiId(siteId);
				psTzSiteiAtypT.setTzAreaTypeId(areatypeid);
				psTzSiteiAtypT.setTzAreaTypeName(areatypename);
				psTzSiteiAtypT.setTzAreaTypeState(areatypestate);
				psTzSiteiAtypT.setTzAreaSetCode(areasetcode);
				psTzSiteiAtypT.setTzAreaHtmlCode(areahtmlcode);
				psTzSiteiAtypT.setTzAreaType(areatypearea);
				int i = psTzSiteiAtypTMapper.updateByPrimaryKey(psTzSiteiAtypT);
				if(i > 0){
					returnJsonMap.replace("areatypeid", areatypeid);
				}else{
					errMsg[0] = "1";
					errMsg[1] = "站点区域类型信息保存失败";
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
	
	/* 查询表单信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("formData", "{}");

		try {
			jacksonUtil.json2Map(strParams);

			if (jacksonUtil.containsKey("siteId")) {
				// 站点id, 区域类型ID;
				String siteId = jacksonUtil.getString("siteId");
				String areatypeid = jacksonUtil.getString("areatypeid");
				
				PsTzSiteiAtypTKey psTzSiteiAtypTKey = new PsTzSiteiAtypTKey();
				psTzSiteiAtypTKey.setTzSiteiId(siteId);
				psTzSiteiAtypTKey.setTzAreaTypeId(areatypeid);
	
				PsTzSiteiAtypT psTzSiteiAtypT = psTzSiteiAtypTMapper.selectByPrimaryKey(psTzSiteiAtypTKey);
				if(psTzSiteiAtypT != null){
					Map<String, Object> jsonMap = new HashMap<String, Object>();
					jsonMap.put("siteId", siteId);
					jsonMap.put("areatypeid", areatypeid);
					jsonMap.put("areatypename", psTzSiteiAtypT.getTzAreaTypeName());
					jsonMap.put("areatypestate", psTzSiteiAtypT.getTzAreaTypeState());
					jsonMap.put("areasetcode",psTzSiteiAtypT.getTzAreaSetCode());
					jsonMap.put("areahtmlcode",psTzSiteiAtypT.getTzAreaHtmlCode());
					jsonMap.put("areatypearea",psTzSiteiAtypT.getTzAreaType());
					returnJsonMap.replace("formData", jsonMap);
				}else{
					errMsg[0] = "1";
					errMsg[1] = "未找到对应区域类型信息";
				}
			} else {
				errMsg[0] = "1";
				errMsg[1] = "参数不正确！";
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
	
	/* 删除站点模板 */
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";

		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}

		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 提交信息
				String strComInfo = actData[num];
				jacksonUtil.json2Map(strComInfo);
				// 站点ID;
				String siteId = jacksonUtil.getString("siteId");
				// 站点ID;
				String areatypeid = jacksonUtil.getString("areatypeid");
				if (siteId != null && !"".equals(siteId) && areatypeid != null && !"".equals(areatypeid)) {
					PsTzSiteiAtypTKey psTzSiteiAtypTKey = new PsTzSiteiAtypTKey();
					psTzSiteiAtypTKey.setTzSiteiId(siteId);
					psTzSiteiAtypTKey.setTzAreaTypeId(areatypeid);
					psTzSiteiAtypTMapper.deleteByPrimaryKey(psTzSiteiAtypTKey);
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
	
}
