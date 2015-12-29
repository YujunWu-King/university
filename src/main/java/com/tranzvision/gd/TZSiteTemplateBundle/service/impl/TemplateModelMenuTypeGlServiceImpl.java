package com.tranzvision.gd.TZSiteTemplateBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZSiteTemplateBundle.dao.PsTzSitemCdpfTMapper;
import com.tranzvision.gd.TZSiteTemplateBundle.dao.PsTzSitemMtypTMapper;
import com.tranzvision.gd.TZSiteTemplateBundle.model.PsTzSitemCdpfT;
import com.tranzvision.gd.TZSiteTemplateBundle.model.PsTzSitemMtypT;
import com.tranzvision.gd.TZSiteTemplateBundle.model.PsTzSitemMtypTKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 菜单类型；原：TZ_GD_ZDGL_PKG:TZ_MENUTYPEGL_CLS
 * 
 * @author tang
 * @since 2015-11-16
 * 
 */
@Service("com.tranzvision.gd.TZSiteTemplateBundle.service.impl.TemplateModelMenuTypeGlServiceImpl")
public class TemplateModelMenuTypeGlServiceImpl extends FrameworkImpl {
	@Autowired 
	private SqlQuery jdbcTemplate;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private PsTzSitemMtypTMapper psTzSitemMtypTMapper;
	@Autowired
	private PsTzSitemCdpfTMapper psTzSitemCdpfTMapper;
	
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
				
				String totalSQL = "SELECT COUNT(1) FROM PS_TZ_SITEM_MTYP_T where TZ_SITEM_ID = ?";
				int total = jdbcTemplate.queryForObject(totalSQL,new Object[]{siteId}, "Integer");
				String sql = "";
				List<Map<String, Object>> list = null;
				if(numLimit > 0){
					sql = " SELECT TZ_MENU_TYPE_ID,TZ_MENU_TYPE_NAME ,TZ_TYPE_STATE ,TZ_IS_ADD  ,TZ_SET_MENU_CODE ,TZ_SHOW_MENU_CODE FROM PS_TZ_SITEM_MTYP_T where TZ_SITEM_ID = ? LIMIT ?,?";
					list = jdbcTemplate.queryForList(sql,new Object[]{siteId,numStart,numLimit});
				}else{
					sql = " SELECT TZ_MENU_TYPE_ID,TZ_MENU_TYPE_NAME ,TZ_TYPE_STATE ,TZ_IS_ADD  ,TZ_SET_MENU_CODE ,TZ_SHOW_MENU_CODE FROM PS_TZ_SITEM_MTYP_T where TZ_SITEM_ID = ?";
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
		String strRet = "{}";
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
	            String menutypeId = String.valueOf(getSeqNum.getSeqNum("TZ_SITEM_MTYP_T", "TZ_MENU_TYPE_ID"));
	            PsTzSitemMtypT psTzSitemMtypT = new PsTzSitemMtypT();
	            psTzSitemMtypT.setTzSitemId(siteId);
	            psTzSitemMtypT.setTzMenuTypeId(menutypeId);
	            psTzSitemMtypT.setTzMenuTypeName(strMenuTypeName);
	            psTzSitemMtypT.setTzTypeState(strMenuTypeState);
	            psTzSitemMtypT.setTzIsAdd(strMenuIsAdd);
	            psTzSitemMtypT.setTzAddColu(strMenuColuAdd);
	            psTzSitemMtypT.setTzTypeDescr(strMenuTypeDescr);
	            psTzSitemMtypT.setTzTypeImg(strMenuTypeImg);
	            psTzSitemMtypT.setTzNowImg(strMenuNowImg);
	            psTzSitemMtypT.setTzSetMenuCode(strMenuSetCode);
	            psTzSitemMtypT.setTzShowMenuCode(strMenuShowCode);
	            psTzSitemMtypT.setTzColuType(menuColuType);
	            psTzSitemMtypT.setTzTempId(menuColuTmpl);
	            psTzSitemMtypT.setTzContType(menuContType);
	            psTzSitemMtypT.setTzContTemp(menuContTmpl);
	            int i = psTzSitemMtypTMapper.insertSelective(psTzSitemMtypT);
	            if(i > 0){
					returnJsonMap.replace("menutypeid", menutypeId);
					
					String siteIconCountSQL = " SELECT COUNT(1) FROM PS_TZ_SITEM_CDPF_T WHERE TZ_SITEM_ID=? and TZ_MENU_TYPE_ID=?";
					int siteIconCount = jdbcTemplate.queryForObject(siteIconCountSQL, new Object[]{siteId,menutypeId},"Integer");
					if(siteIconCount == 0){
						String skinSQL = "SELECT TZ_SKIN_ID,TZ_SKIN_STATE,TZ_SKIN_NAME FROM PS_TZ_SITEM_SKIN_T WHERE TZ_SITEM_ID=?";
						List<Map<String, Object>> skinList = jdbcTemplate.queryForList(skinSQL,new Object[]{siteId});
						if(skinList != null){
							for(int j = 0; j < skinList.size(); j++){
								PsTzSitemCdpfT psTzSitemCdpfT = new PsTzSitemCdpfT();
								psTzSitemCdpfT.setTzSitemId(siteId);
								psTzSitemCdpfT.setTzMenuTypeId(menutypeId);
								psTzSitemCdpfT.setTzSkinId((String)skinList.get(j).get("TZ_SKIN_ID"));
								psTzSitemCdpfT.setTzSkinState((String)skinList.get(j).get("TZ_SKIN_STATE"));
								psTzSitemCdpfT.setTzSkinName((String)skinList.get(j).get("TZ_SKIN_NAME"));
								psTzSitemCdpfTMapper.insert(psTzSitemCdpfT);
							}
						}
					}
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
				String menutypeId = jacksonUtil.getString("menutypeid");
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
	           
	            PsTzSitemMtypT psTzSitemMtypT = new PsTzSitemMtypT();
	            psTzSitemMtypT.setTzSitemId(siteId);
	            psTzSitemMtypT.setTzMenuTypeId(menutypeId);
	            psTzSitemMtypT.setTzMenuTypeName(strMenuTypeName);
	            psTzSitemMtypT.setTzTypeState(strMenuTypeState);
	            psTzSitemMtypT.setTzIsAdd(strMenuIsAdd);
	            psTzSitemMtypT.setTzAddColu(strMenuColuAdd);
	            psTzSitemMtypT.setTzTypeDescr(strMenuTypeDescr);
	            psTzSitemMtypT.setTzTypeImg(strMenuTypeImg);
	            psTzSitemMtypT.setTzNowImg(strMenuNowImg);
	            psTzSitemMtypT.setTzSetMenuCode(strMenuSetCode);
	            psTzSitemMtypT.setTzShowMenuCode(strMenuShowCode);
	            psTzSitemMtypT.setTzColuType(menuColuType);
	            psTzSitemMtypT.setTzTempId(menuColuTmpl);
	            psTzSitemMtypT.setTzContType(menuContType);
	            psTzSitemMtypT.setTzContTemp(menuContTmpl);
	            int i = psTzSitemMtypTMapper.updateByPrimaryKeyWithBLOBs(psTzSitemMtypT);
	            if(i > 0){
					returnJsonMap.replace("menutypeid", menutypeId);
					
					String siteIconCountSQL = " SELECT COUNT(1) FROM PS_TZ_SITEM_CDPF_T WHERE TZ_SITEM_ID=? and TZ_MENU_TYPE_ID=?";
					int siteIconCount = jdbcTemplate.queryForObject(siteIconCountSQL, new Object[]{siteId,menutypeId},"Integer");
					if(siteIconCount == 0){
						String skinSQL = "SELECT TZ_SKIN_ID,TZ_SKIN_STATE,TZ_SKIN_NAME FROM PS_TZ_SITEM_SKIN_T WHERE TZ_SITEM_ID=?";
						List<Map<String, Object>> skinList = jdbcTemplate.queryForList(skinSQL,new Object[]{siteId});
						if(skinList != null){
							for(int j = 0; j < skinList.size(); j++){
								PsTzSitemCdpfT psTzSitemCdpfT = new PsTzSitemCdpfT();
								psTzSitemCdpfT.setTzSitemId(siteId);
								psTzSitemCdpfT.setTzMenuTypeId(menutypeId);
								psTzSitemCdpfT.setTzSkinId((String)skinList.get(j).get("TZ_SKIN_ID"));
								psTzSitemCdpfT.setTzSkinState((String)skinList.get(j).get("TZ_SKIN_STATE"));
								psTzSitemCdpfT.setTzSkinName((String)skinList.get(j).get("TZ_SKIN_NAME"));
								psTzSitemCdpfTMapper.insert(psTzSitemCdpfT);
							}
						}
					}
				}else{
					errMsg[0] = "1";
					errMsg[1] = "站点菜单类型信息保持失败";
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
				
				PsTzSitemMtypTKey psTzSitemMtypTKey = new PsTzSitemMtypTKey();
				psTzSitemMtypTKey.setTzSitemId(siteId);
				psTzSitemMtypTKey.setTzMenuTypeId(menutypeid);
	
				PsTzSitemMtypT psTzSitemMtypT = psTzSitemMtypTMapper.selectByPrimaryKey(psTzSitemMtypTKey);
				if(psTzSitemMtypT != null){
					Map<String, Object> jsonMap = new HashMap<String, Object>();
					jsonMap.put("menutypeid",psTzSitemMtypT.getTzMenuTypeId() );
					jsonMap.put("menutypename",psTzSitemMtypT.getTzMenuTypeName() );
					jsonMap.put("menutypestate",psTzSitemMtypT.getTzTypeState() );
					jsonMap.put("menuisadd",psTzSitemMtypT.getTzIsAdd() );
					jsonMap.put("menutypedescr",psTzSitemMtypT.getTzTypeDescr() );
					jsonMap.put("menutypeimg",psTzSitemMtypT.getTzTypeImg() );
					jsonMap.put("menunowimg",psTzSitemMtypT.getTzNowImg() );
					jsonMap.put("menusetcode", psTzSitemMtypT.getTzSetMenuCode());
					jsonMap.put("menushowcode", psTzSitemMtypT.getTzShowMenuCode());
					jsonMap.put("menuColuType", psTzSitemMtypT.getTzColuType());
					jsonMap.put("menuColuTmpl", psTzSitemMtypT.getTzTempId());
					jsonMap.put("menuContType", psTzSitemMtypT.getTzContType());
					jsonMap.put("menuContTmpl", psTzSitemMtypT.getTzContTemp());
					jsonMap.put("menucoluadd", psTzSitemMtypT.getTzAddColu());
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
					String deletesql = "delete from PS_TZ_SITEM_MTYP_T where TZ_SITEM_ID=? and TZ_MENU_TYPE_ID=? ";
					jdbcTemplate.update(deletesql, new Object[]{siteId,menutypeid});
					deletesql = "delete from PS_TZ_SITEM_CDPF_T where TZ_SITEM_ID=? and TZ_MENU_TYPE_ID=?";
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
