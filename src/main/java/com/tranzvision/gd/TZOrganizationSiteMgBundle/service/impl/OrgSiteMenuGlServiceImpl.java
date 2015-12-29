package com.tranzvision.gd.TZOrganizationSiteMgBundle.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiMenuTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiMnpfTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiMenuT;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiMenuTKey;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiMnpfT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 菜单设置；原：TZ_GD_ZDDY_PKG:TZ_GD_MENUGL_CLS
 * 
 * @author tang
 * @since 2015-11-18
 * 
 */
@Service("com.tranzvision.gd.TZOrganizationSiteMgBundle.service.impl.OrgSiteMenuGlServiceImpl")
public class OrgSiteMenuGlServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private PsTzSiteiMenuTMapper psTzSiteiMenuTMapper;
	@Autowired
	private PsTzSiteiMnpfTMapper psTzSiteiMnpfTMapper;
	
	/* 添加站点菜单 */
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("menuId", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				String siteId = jacksonUtil.getString("siteId");
				String menuname = jacksonUtil.getString("menuname");
				String menustate = jacksonUtil.getString("menustate");
				String menutypeid = jacksonUtil.getString("menutypeid");
				String menulm = jacksonUtil.getString("menulm");
				String menuisdel = jacksonUtil.getString("menuisdel");
				String menuisedit = jacksonUtil.getString("menuisedit");
				String menuxhStr = jacksonUtil.getString("menuxh");
				int menuxh = 0;
	            if(menuxhStr != null && !"".equals(menuxhStr.trim())){
	            	menuxh = Integer.parseInt(menuxhStr);
	            }
	            
	            //String menutypeimg = jacksonUtil.getString("menutypeimg");
	            //String menunowimg = jacksonUtil.getString("menunowimg");
	            
	            String menuId = String.valueOf(getSeqNum.getSeqNum("TZ_SITEI_MENU_T", "TZ_MENU_ID"));
	            
	            PsTzSiteiMenuT psTzSiteiMenuT = new PsTzSiteiMenuT();
	            psTzSiteiMenuT.setTzSiteiId(siteId);
	            psTzSiteiMenuT.setTzMenuId(menuId);
	            psTzSiteiMenuT.setTzMenuName(menuname);
	            psTzSiteiMenuT.setTzMenuState(menustate);
	            psTzSiteiMenuT.setTzMenuColumn(menulm);
	            psTzSiteiMenuT.setTzMenuTypeId(menutypeid);
	            psTzSiteiMenuT.setTzIsDel(menuisdel);
	            psTzSiteiMenuT.setTzIsEditor(menuisedit);
	            psTzSiteiMenuT.setTzMenuXh(menuxh);
	            
	            String imgSql = "SELECT TZ_TYPE_IMG,TZ_NOW_IMG FROM PS_TZ_SITEI_MTYP_T WHERE TZ_SITEI_ID=? AND TZ_MENU_TYPE_ID=?";
	            Map<String, Object> map = jdbcTemplate.queryForMap(imgSql,new Object[]{siteId,menutypeid});
	            if(map != null){
	            	psTzSiteiMenuT.setTzTypeImg((String)map.get("TZ_TYPE_IMG"));
	            	psTzSiteiMenuT.setTzNowImg((String)map.get("TZ_NOW_IMG"));
	            }
	            String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
	            psTzSiteiMenuT.setTzAddedDttm(new Date());
	            psTzSiteiMenuT.setTzAddedOprid(oprid);
	            psTzSiteiMenuT.setTzLastmantDttm(new Date());
	            psTzSiteiMenuT.setTzLastmantOprid(oprid);
	            
				int i = psTzSiteiMenuTMapper.insert(psTzSiteiMenuT);
				if(i > 0){
					//复制菜单类型图标到菜单下;
					String menuIconSQL = "SELECT TZ_SKIN_ID,TZ_SKIN_STATE,TZ_SKIN_NAME,TZ_TYPE_IMG,TZ_NOW_IMG FROM PS_TZ_SITEI_CDPF_T WHERE TZ_SITEI_ID=? AND TZ_MENU_TYPE_ID=?";
					List<Map<String, Object>> list = jdbcTemplate.queryForList(menuIconSQL,new Object[]{siteId,menutypeid});
					if(list != null && list.size()>0){
						for(int j = 0; j<list.size();j++){
							PsTzSiteiMnpfT psTzSiteiMnpfT = new PsTzSiteiMnpfT();
							psTzSiteiMnpfT.setTzSiteiId(siteId);
							psTzSiteiMnpfT.setTzMenuId(menuId);
							psTzSiteiMnpfT.setTzSkinId((String)list.get(j).get("TZ_SKIN_ID"));
							psTzSiteiMnpfT.setTzSkinState((String)list.get(j).get("TZ_SKIN_STATE"));
							psTzSiteiMnpfT.setTzSkinName((String)list.get(j).get("TZ_SKIN_NAME"));
							psTzSiteiMnpfT.setTzTypeImg((String)list.get(j).get("TZ_TYPE_IMG"));
							psTzSiteiMnpfT.setTzNowImg((String)list.get(j).get("TZ_NOW_IMG"));
							psTzSiteiMnpfTMapper.insert(psTzSiteiMnpfT);
						}
					}
					
					returnJsonMap.replace("menuId", menuId);
				}else{
					errMsg[0] = "1";
					errMsg[1] = "站点模板菜单信息保存失败";
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
	
	/* 添加站点菜单 */
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("menuId", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				String siteId = jacksonUtil.getString("siteId");
				String menuId = jacksonUtil.getString("menuid");
				String menuname = jacksonUtil.getString("menuname");
				String menustate = jacksonUtil.getString("menustate");
				String menutypeid = jacksonUtil.getString("menutypeid");
				String menulm = jacksonUtil.getString("menulm");
				String menuisdel = jacksonUtil.getString("menuisdel");
				String menuisedit = jacksonUtil.getString("menuisedit");
				String menuxhStr = jacksonUtil.getString("menuxh");
				int menuxh = 0;
	            if(menuxhStr != null && !"".equals(menuxhStr.trim())){
	            	menuxh = Integer.parseInt(menuxhStr);
	            }
	            
	            String menutypeimg = jacksonUtil.getString("menutypeimg");
	            String menunowimg = jacksonUtil.getString("menunowimg");

	            PsTzSiteiMenuT psTzSiteiMenuT = new PsTzSiteiMenuT();
	            psTzSiteiMenuT.setTzSiteiId(siteId);
	            psTzSiteiMenuT.setTzMenuId(menuId);
	            psTzSiteiMenuT.setTzMenuName(menuname);
	            psTzSiteiMenuT.setTzMenuState(menustate);
	            psTzSiteiMenuT.setTzMenuColumn(menulm);
	            psTzSiteiMenuT.setTzMenuTypeId(menutypeid);
	            psTzSiteiMenuT.setTzIsDel(menuisdel);
	            psTzSiteiMenuT.setTzIsEditor(menuisedit);
	            psTzSiteiMenuT.setTzMenuXh(menuxh);
	            psTzSiteiMenuT.setTzTypeImg(menutypeimg);
            	psTzSiteiMenuT.setTzNowImg(menunowimg);
            	String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
	            psTzSiteiMenuT.setTzLastmantDttm(new Date());
	            psTzSiteiMenuT.setTzLastmantOprid(oprid);
	            
				int i = psTzSiteiMenuTMapper.updateByPrimaryKeySelective(psTzSiteiMenuT);
				if(i > 0){
					returnJsonMap.replace("menuId", menuId);
				}else{
					errMsg[0] = "1";
					errMsg[1] = "站点模板菜单信息保存失败";
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

			if (jacksonUtil.containsKey("siteId") && jacksonUtil.containsKey("menuid")) {
				// 站点id 菜单id;
				String siteId = jacksonUtil.getString("siteId");
				String menuid = jacksonUtil.getString("menuid");
			
				PsTzSiteiMenuTKey psTzSiteiMenuTKey = new PsTzSiteiMenuTKey();
				psTzSiteiMenuTKey.setTzSiteiId(siteId);
				psTzSiteiMenuTKey.setTzMenuId(menuid);
				
				PsTzSiteiMenuT psTzSiteiMenuT = psTzSiteiMenuTMapper.selectByPrimaryKey(psTzSiteiMenuTKey);
				if(psTzSiteiMenuT != null){
					Map<String, Object> jsonMap = new HashMap<String, Object>();
					jsonMap.put("menuid", menuid);
					jsonMap.put("menuname", psTzSiteiMenuT.getTzMenuName());
					jsonMap.put("menutypeid",psTzSiteiMenuT.getTzMenuTypeId() );
					jsonMap.put("menustate", psTzSiteiMenuT.getTzMenuState());
					jsonMap.put("menulm", psTzSiteiMenuT.getTzMenuColumn());
					jsonMap.put("menuisdel",psTzSiteiMenuT.getTzIsDel());
					jsonMap.put("menuisedit",psTzSiteiMenuT.getTzIsEditor());
					jsonMap.put("menutypeimg",psTzSiteiMenuT.getTzTypeImg());
					jsonMap.put("menunowimg",psTzSiteiMenuT.getTzNowImg());
					jsonMap.put("menuxh",psTzSiteiMenuT.getTzMenuXh());
					returnJsonMap.replace("formData", jsonMap);
				}else{
					errMsg[0] = "1";
					errMsg[1] = "未找到对应菜单信息";
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
	
	/* 删除站点菜单 */
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
				String menuid = jacksonUtil.getString("menuid");
				if (siteId != null && !"".equals(siteId) && menuid != null && !"".equals(menuid)) {
					//删除菜单;
					String deletesql = "delete from PS_TZ_SITEI_MENU_T where TZ_SITEI_ID=? AND TZ_MENU_ID=? ";
					jdbcTemplate.update(deletesql, new Object[]{siteId,menuid});
					
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
	
}
