package com.tranzvision.gd.TZOrganizationSiteMgBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiMnpfTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiMnpfT;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiMnpfTKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 菜单图标皮肤设置；原：TZ_GD_ZDDY_PKG:TZ_GD_ICON2_CLS
 * 
 * @author tang
 * @since 2015-11-18
 * 
 */
@Service("com.tranzvision.gd.TZOrganizationSiteMgBundle.service.impl.OrgSiteMenuIcon2ServiceImple")
public class OrgSiteMenuIcon2ServiceImple extends FrameworkImpl{
	@Autowired 
	private SqlQuery jdbcTemplate;
	@Autowired
	private JacksonUtil jacksonUtil;
	@Autowired
	private PsTzSiteiMnpfTMapper psTzSiteiMnpfTMapper;
	
	
	/* 查询菜单图标皮肤列表 */
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("total", 0);
		ArrayList<Map<String, Object>> arraylist = new ArrayList<Map<String, Object>>();
		returnJsonMap.put("root", arraylist);
		
		try {
			
			jacksonUtil.json2Map(comParams);
			if(jacksonUtil.containsKey("siteId") && jacksonUtil.containsKey("menuId") ){
				String siteId = jacksonUtil.getString("siteId");
				String menuId = jacksonUtil.getString("menuId");

				String siteIconCountSQL = " SELECT COUNT(1) FROM PS_TZ_SITEI_MNPF_T WHERE TZ_SITEI_ID=? and TZ_MENU_ID=?";
				int siteIconCount = jdbcTemplate.queryForObject(siteIconCountSQL, new Object[]{siteId,menuId},"Integer");
				if(siteIconCount == 0){
					String skinSQL = "SELECT TZ_SKIN_ID,TZ_SKIN_STATE,TZ_SKIN_NAME FROM PS_TZ_SITEM_SKIN_T WHERE TZ_SITEM_ID=(SELECT TZ_SITEM_ID FROM PS_TZ_SITEI_DEFN_T WHERE TZ_SITEI_ID=?)";
					List<Map<String, Object>> skinList = jdbcTemplate.queryForList(skinSQL,new Object[]{siteId});
					if(skinList != null){
						for(int i = 0; i < skinList.size(); i++){
							PsTzSiteiMnpfT psTzSiteiMnpfT = new PsTzSiteiMnpfT();
							psTzSiteiMnpfT.setTzSiteiId(siteId);
							psTzSiteiMnpfT.setTzMenuId(menuId);
							psTzSiteiMnpfT.setTzSkinId((String)skinList.get(i).get("TZ_SKIN_ID"));
							psTzSiteiMnpfT.setTzSkinName((String)skinList.get(i).get("TZ_SKIN_NAME"));
							psTzSiteiMnpfT.setTzSkinState((String)skinList.get(i).get("TZ_SKIN_STATE"));
							psTzSiteiMnpfTMapper.insert(psTzSiteiMnpfT);
						}
					}
				}
				
				String totalSQL ="SELECT COUNT(1) from PS_TZ_SITEI_MNPF_T WHERE TZ_SITEI_ID=? and TZ_MENU_ID=?";
				int total = jdbcTemplate.queryForObject(totalSQL,new Object[]{siteId,menuId}, "Integer");
				String sql = "";
				List<Map<String, Object>> list = null;
				if(numLimit > 0){
					sql = "SELECT TZ_SKIN_ID,TZ_SKIN_STATE,TZ_SKIN_NAME FROM PS_TZ_SITEI_MNPF_T where TZ_SITEI_ID = ? and TZ_MENU_ID = ? ORDER BY cast(TZ_SKIN_ID as unsigned int) ASC LIMIT ?,?";
					list = jdbcTemplate.queryForList(sql,new Object[]{siteId,menuId,numStart,numLimit});
				}else{
					sql = "SELECT TZ_SKIN_ID,TZ_SKIN_STATE,TZ_SKIN_NAME FROM PS_TZ_SITEI_MNPF_T where TZ_SITEI_ID = ? and TZ_MENU_ID = ? ORDER BY cast(TZ_SKIN_ID as unsigned int) ASC";
					list = jdbcTemplate.queryForList(sql,new Object[]{siteId,menuId});
				}
				String zhzSQL = "SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID=? AND TZ_ZHZ_ID=? AND TZ_EFF_STATUS='A'";
				if(list != null){
					
					for(int i = 0; i<list.size();i++){
						Map<String, Object> jsonMap = new HashMap<String, Object>();
						String skinState = (String) list.get(i).get("TZ_SKIN_STATE");
						skinState = jdbcTemplate.queryForObject(zhzSQL, new Object[]{"TZ_SKIN_STATE",skinState},"String");
						jsonMap.put("skinId", list.get(i).get("TZ_SKIN_ID"));
						jsonMap.put("skinName", list.get(i).get("TZ_SKIN_NAME"));
						jsonMap.put("skinStatus", skinState);
						arraylist.add(jsonMap);
					}
					returnJsonMap.replace("total", total);
					returnJsonMap.replace("root", arraylist);
					strRet = jacksonUtil.Map2json(returnJsonMap);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
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

		try {
			jacksonUtil.json2Map(strParams);

			if (jacksonUtil.containsKey("siteId")) {
				// 站点id, 菜单ID，皮肤id;
				String siteId = jacksonUtil.getString("siteId");
				String menuId = jacksonUtil.getString("menuId");
				String skinId = jacksonUtil.getString("skinId");
				
				PsTzSiteiMnpfTKey psTzSiteiMnpfTKey = new PsTzSiteiMnpfTKey();
				psTzSiteiMnpfTKey.setTzSiteiId(siteId);
				psTzSiteiMnpfTKey.setTzMenuId(menuId);
				psTzSiteiMnpfTKey.setTzSkinId(skinId);
				
				PsTzSiteiMnpfT psTzSiteiMnpfT = psTzSiteiMnpfTMapper.selectByPrimaryKey(psTzSiteiMnpfTKey);
	
				if(psTzSiteiMnpfT != null){
					Map<String, Object> jsonMap = new HashMap<String, Object>();
					
					String menutypenameSQL = "SELECT TZ_MENU_NAME FROM PS_TZ_SITEI_MENU_T WHERE TZ_SITEI_ID=? AND TZ_MENU_ID=? AND TZ_MENU_STATE='Y'";
					String menuname = jdbcTemplate.queryForObject(menutypenameSQL, new Object[]{siteId,menuId},"String");
					jsonMap.put("siteid", siteId);
					jsonMap.put("menuid", menuId);
					jsonMap.put("skinid",skinId );
					jsonMap.put("menuname",menuname);
					jsonMap.put("skinname",psTzSiteiMnpfT.getTzSkinName() );
					jsonMap.put("menutypeimg",psTzSiteiMnpfT.getTzTypeImg());
					jsonMap.put("menunowimg",psTzSiteiMnpfT.getTzNowImg() );
					returnJsonMap.replace("formData", jsonMap);
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
	
	/* 修改菜单类型设置 */
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";

		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				String siteid = jacksonUtil.getString("siteid");
				String menuid = jacksonUtil.getString("menuid");
				String skinid = jacksonUtil.getString("skinid");
				String menutypeimg = jacksonUtil.getString("menutypeimg");
				String menunowimg = jacksonUtil.getString("menunowimg");
				
				String updateSQL = "update PS_TZ_SITEI_MNPF_T set TZ_TYPE_IMG=? ,TZ_NOW_IMG=? where TZ_SITEI_ID = ? and TZ_MENU_ID = ? and TZ_SKIN_ID = ?";
				jdbcTemplate.update(updateSQL,new Object[]{menutypeimg,menunowimg,siteid,menuid,skinid});
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
	
	
	@Override
	public String tzGetJsonData(String comParams){
			Map<String, Object> returnJsonMap = new HashMap<String, Object>();
			returnJsonMap.put("success", 0);
			returnJsonMap.put("msg", "");
			
			try{
				jacksonUtil.json2Map(comParams);
				String siteId = jacksonUtil.getString("siteId");
				String menuId = jacksonUtil.getString("menuId");
				String skinId = jacksonUtil.getString("skinId");
				String path = jacksonUtil.getString("path");
				String imgtype = jacksonUtil.getString("imgtype");
				
				PsTzSiteiMnpfT psTzSiteiMnpfT = new PsTzSiteiMnpfT();
				psTzSiteiMnpfT.setTzSiteiId(siteId);
				psTzSiteiMnpfT.setTzMenuId(menuId);
				psTzSiteiMnpfT.setTzSkinId(skinId);
				if("TZ_TYPE_IMG".equals(imgtype)){
					psTzSiteiMnpfT.setTzTypeImg(path);
				}else if("TZ_NOW_IMG".equals(imgtype)){
					psTzSiteiMnpfT.setTzNowImg(path);
				}
				int i = psTzSiteiMnpfTMapper.updateByPrimaryKeySelective(psTzSiteiMnpfT);

				if(i > 0){
					returnJsonMap.replace("success", 0);
					returnJsonMap.replace("msg", "");
				}else{
					returnJsonMap.replace("success", 1);
					returnJsonMap.replace("msg", "保存失败");
				}
			}catch(Exception e){
				returnJsonMap.replace("success", 1);
				returnJsonMap.replace("msg", e.toString());
				e.printStackTrace();
			}
			return jacksonUtil.Map2json(returnJsonMap);
		}
	
}
