package com.tranzvision.gd.TZOrganizationSiteMgBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiMtypTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiMtypT;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiMtypTKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 菜单类型；原：TZ_GD_ZDDY_PKG:TZ_MENUTYPEGL_CLS
 * 
 * @author tang
 * @since 2015-11-18
 * 
 */
@Service("com.tranzvision.gd.TZOrganizationSiteMgBundle.service.impl.OrgSiteMenuTypeGlServiceImpl")
public class OrgSiteMenuTypeGlServiceImpl extends FrameworkImpl {
	@Autowired 
	private SqlQuery jdbcTemplate;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private PsTzSiteiMtypTMapper psTzSiteiMtypTMapper;
	
	/* 查询菜单类型管理列表 */
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("total", 0);
		ArrayList<Map<String, Object>> arraylist = new ArrayList<Map<String, Object>>();
		returnJsonMap.put("root", arraylist);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(comParams);
			if(jacksonUtil.containsKey("siteId")){
				String siteId = jacksonUtil.getString("siteId");
				
				String totalSQL = "SELECT COUNT(1) FROM PS_TZ_SITEI_MTYP_T where TZ_SITEI_ID = ?";
				int total = jdbcTemplate.queryForObject(totalSQL,new Object[]{siteId}, "Integer");
				String sql = "";
				List<Map<String, Object>> list = null;
				if(numLimit > 0){
					sql = " SELECT TZ_MENU_TYPE_ID,TZ_MENU_TYPE_NAME ,TZ_TYPE_STATE ,TZ_IS_ADD  ,TZ_SET_MENU_CODE ,TZ_SHOW_MENU_CODE FROM PS_TZ_SITEI_MTYP_T where TZ_SITEI_ID = ? ORDER BY TZ_MENU_TYPE_ID ASC LIMIT ?,?";
					list = jdbcTemplate.queryForList(sql,new Object[]{siteId,numStart,numLimit});
				}else{
					sql = " SELECT TZ_MENU_TYPE_ID,TZ_MENU_TYPE_NAME ,TZ_TYPE_STATE ,TZ_IS_ADD  ,TZ_SET_MENU_CODE ,TZ_SHOW_MENU_CODE FROM PS_TZ_SITEI_MTYP_T where TZ_SITEI_ID = ? ORDER BY TZ_MENU_TYPE_ID ASC";
					list = jdbcTemplate.queryForList(sql,new Object[]{siteId});
				}
				String zzSQL = "SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID='TZ_TYPE_STATE' AND TZ_ZHZ_ID=? AND TZ_EFF_STATUS='A'";
				if(list != null){
					
					for(int i = 0; i<list.size();i++){
						Map<String, Object> jsonMap = new HashMap<String, Object>();
						String menutypestate = (String) list.get(i).get("TZ_TYPE_STATE");
						menutypestate = jdbcTemplate.queryForObject(zzSQL, new Object[]{menutypestate},"String");
						jsonMap.put("siteId", siteId);
						jsonMap.put("menutypeid", list.get(i).get("TZ_MENU_TYPE_ID"));
						jsonMap.put("menutypename", list.get(i).get("TZ_MENU_TYPE_NAME"));
						jsonMap.put("menutypestate", menutypestate);
						jsonMap.put("menuisadd", list.get(i).get("TZ_IS_ADD"));
						jsonMap.put("menusetcode", list.get(i).get("TZ_SET_MENU_CODE"));
						jsonMap.put("menushowcode", list.get(i).get("TZ_SHOW_MENU_CODE"));
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
	
	/* 添加菜单类型设置 */
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("menutypeid", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				String siteId = jacksonUtil.getString("siteId");
				String strMenuTypeName = jacksonUtil.getString("menutypename");
				String strMenuTypeState = jacksonUtil.getString("menutypestate");
				String strMenuIsAdd = "";
				if(jacksonUtil.containsKey("menuisadd")){
					strMenuIsAdd = jacksonUtil.getString("menuisadd");
				} 
	            String strMenuColuAdd = jacksonUtil.getString("menucoluadd");
	            String strMenuTypeDescr = jacksonUtil.getString("menutypedescr");
	            String strMenuTypeImg = jacksonUtil.getString("menutypeimg");
	            String strMenuNowImg = jacksonUtil.getString("menunowimg");
	            String strMenuSetCode = jacksonUtil.getString("menusetcode");
	            String strMenuShowCode = jacksonUtil.getString("menushowcode");
	            String menuColuType = jacksonUtil.getString("menuColuType");
	            String menuColuTmpl = jacksonUtil.getString("menuColuTmpl");
	            String menuContType = jacksonUtil.getString("menuContType");
	            String menuContTmpl = jacksonUtil.getString("menuContTmpl");
	            String menutypeid = String.valueOf(getSeqNum.getSeqNum("TZ_SITEI_MTYP_T", "TZ_MENU_TYPE_ID"));
	            PsTzSiteiMtypT psTzSiteiMtypT = new PsTzSiteiMtypT();
	            psTzSiteiMtypT.setTzSiteiId(siteId);
	            psTzSiteiMtypT.setTzMenuTypeId(menutypeid);
	            psTzSiteiMtypT.setTzMenuTypeName(strMenuTypeName);
	            psTzSiteiMtypT.setTzTypeState(strMenuTypeState);
	            psTzSiteiMtypT.setTzIsAdd(strMenuIsAdd);
	            psTzSiteiMtypT.setTzAddColu(strMenuColuAdd);
	            psTzSiteiMtypT.setTzTypeDescr(strMenuTypeDescr);
	            psTzSiteiMtypT.setTzTypeImg(strMenuTypeImg);
	            psTzSiteiMtypT.setTzNowImg(strMenuNowImg);
	            psTzSiteiMtypT.setTzSetMenuCode(strMenuSetCode);
	            psTzSiteiMtypT.setTzShowMenuCode(strMenuShowCode);
	            psTzSiteiMtypT.setTzColuType(menuColuType);
	            psTzSiteiMtypT.setTzTempId(menuColuTmpl);
	            psTzSiteiMtypT.setTzContType(menuContType);
	            psTzSiteiMtypT.setTzContTemp(menuContTmpl);
	            String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
	            psTzSiteiMtypT.setTzAddedDttm(new Date());
	            psTzSiteiMtypT.setTzAddedOprid(oprid);
	            psTzSiteiMtypT.setTzLastmantDttm(new Date());
	            psTzSiteiMtypT.setTzLastmantOprid(oprid);
	            int i = psTzSiteiMtypTMapper.insert(psTzSiteiMtypT);
	            if(i > 0){
					returnJsonMap.replace("menutypeid", menutypeid);
				}else{
					errMsg[0] = "1";
					errMsg[1] = "站点菜单类型信息保存失败";
				}
				
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
	
	/* 修改菜单类型设置 */
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("menutypeid", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				String siteId = jacksonUtil.getString("siteId");
				String menutypeid = jacksonUtil.getString("menutypeid");
				String strMenuTypeName = jacksonUtil.getString("menutypename");
				String strMenuTypeState = jacksonUtil.getString("menutypestate");
				String strMenuIsAdd = "";
				if(jacksonUtil.containsKey("menuisadd")){
					strMenuIsAdd = jacksonUtil.getString("menuisadd");
				} 
	            String strMenuColuAdd = jacksonUtil.getString("menucoluadd");
	            String strMenuTypeDescr = jacksonUtil.getString("menutypedescr");
	            String strMenuTypeImg = jacksonUtil.getString("menutypeimg");
	            String strMenuNowImg = jacksonUtil.getString("menunowimg");
	            String strMenuSetCode = jacksonUtil.getString("menusetcode");
	            String strMenuShowCode = jacksonUtil.getString("menushowcode");
	            String menuColuType = jacksonUtil.getString("menuColuType");
	            String menuColuTmpl = jacksonUtil.getString("menuColuTmpl");
	            String menuContType = jacksonUtil.getString("menuContType");
	            String menuContTmpl = jacksonUtil.getString("menuContTmpl");
	            
	            PsTzSiteiMtypT psTzSiteiMtypT = new PsTzSiteiMtypT();
	            psTzSiteiMtypT.setTzSiteiId(siteId);
	            psTzSiteiMtypT.setTzMenuTypeId(menutypeid);
	            psTzSiteiMtypT.setTzMenuTypeName(strMenuTypeName);
	            psTzSiteiMtypT.setTzTypeState(strMenuTypeState);
	            psTzSiteiMtypT.setTzIsAdd(strMenuIsAdd);
	            psTzSiteiMtypT.setTzAddColu(strMenuColuAdd);
	            psTzSiteiMtypT.setTzTypeDescr(strMenuTypeDescr);
	            psTzSiteiMtypT.setTzTypeImg(strMenuTypeImg);
	            psTzSiteiMtypT.setTzNowImg(strMenuNowImg);
	            psTzSiteiMtypT.setTzSetMenuCode(strMenuSetCode);
	            psTzSiteiMtypT.setTzShowMenuCode(strMenuShowCode);
	            psTzSiteiMtypT.setTzColuType(menuColuType);
	            psTzSiteiMtypT.setTzTempId(menuColuTmpl);
	            psTzSiteiMtypT.setTzContType(menuContType);
	            psTzSiteiMtypT.setTzContTemp(menuContTmpl);
	            String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
	            psTzSiteiMtypT.setTzLastmantDttm(new Date());
	            psTzSiteiMtypT.setTzLastmantOprid(oprid);
	            int i = psTzSiteiMtypTMapper.updateByPrimaryKeyWithBLOBs(psTzSiteiMtypT);
	            if(i > 0){
					returnJsonMap.replace("menutypeid", menutypeid);
				}else{
					errMsg[0] = "1";
					errMsg[1] = "站点菜单类型信息保存失败";
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
		returnJsonMap.put("formData", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			if (jacksonUtil.containsKey("siteId")) {
				// 站点id, 区域类型ID;
				String siteId = jacksonUtil.getString("siteId");
				String menutypeid = jacksonUtil.getString("menutypeid");
				
				PsTzSiteiMtypTKey psTzSiteiMtypTKey = new PsTzSiteiMtypTKey();
				psTzSiteiMtypTKey.setTzSiteiId(siteId);
				psTzSiteiMtypTKey.setTzMenuTypeId(menutypeid);
	
				PsTzSiteiMtypT psTzSiteiMtypT = psTzSiteiMtypTMapper.selectByPrimaryKey(psTzSiteiMtypTKey);
				if(psTzSiteiMtypT != null){
					Map<String, Object> jsonMap = new HashMap<String, Object>();
					jsonMap.put("menutypeid",psTzSiteiMtypT.getTzMenuTypeId() );
					jsonMap.put("menutypename",psTzSiteiMtypT.getTzMenuTypeName() );
					jsonMap.put("menutypestate",psTzSiteiMtypT.getTzTypeState() );
					jsonMap.put("menuisadd",psTzSiteiMtypT.getTzIsAdd() );
					jsonMap.put("menutypedescr",psTzSiteiMtypT.getTzTypeDescr() );
					jsonMap.put("menutypeimg",psTzSiteiMtypT.getTzTypeImg() );
					jsonMap.put("menunowimg",psTzSiteiMtypT.getTzNowImg() );
					jsonMap.put("menusetcode", psTzSiteiMtypT.getTzSetMenuCode());
					jsonMap.put("menushowcode", psTzSiteiMtypT.getTzShowMenuCode());
					jsonMap.put("menuColuType", psTzSiteiMtypT.getTzColuType());
					jsonMap.put("menuColuTmpl", psTzSiteiMtypT.getTzTempId());
					jsonMap.put("menuContType", psTzSiteiMtypT.getTzContType());
					jsonMap.put("menuContTmpl", psTzSiteiMtypT.getTzContTemp());
					jsonMap.put("menucoluadd", psTzSiteiMtypT.getTzAddColu());
					returnJsonMap.replace("formData", jsonMap);
				}else{
					errMsg[0] = "1";
					errMsg[1] = "未找到对应菜单类型信息";
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
	
	/* 删除站点菜单类型 */
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "";

		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 提交信息
				String strComInfo = actData[num];
				jacksonUtil.json2Map(strComInfo);
				// 站点ID;
				String siteId = jacksonUtil.getString("siteId");
				String menutypeid = jacksonUtil.getString("menutypeid");
				if (siteId != null && !"".equals(siteId) && menutypeid != null && !"".equals(menutypeid)) {
					//删除菜单类型;
					String deletesql = "delete from PS_TZ_SITEI_MTYP_T where TZ_SITEI_ID=? and TZ_MENU_TYPE_ID=? ";
					jdbcTemplate.update(deletesql, new Object[]{siteId,menutypeid});
					deletesql = "delete from PS_TZ_SITEI_CDPF_T where TZ_SITEI_ID=? and TZ_MENU_TYPE_ID=?";
					jdbcTemplate.update(deletesql, new Object[]{siteId,menutypeid});
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
}
