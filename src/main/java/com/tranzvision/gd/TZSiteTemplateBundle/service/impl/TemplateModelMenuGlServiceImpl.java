package com.tranzvision.gd.TZSiteTemplateBundle.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZSiteTemplateBundle.dao.PsTzSitemMenuTMapper;
import com.tranzvision.gd.TZSiteTemplateBundle.model.PsTzSitemMenuT;
import com.tranzvision.gd.TZSiteTemplateBundle.model.PsTzSitemMenuTKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 菜单设置；原：TZ_GD_ZDGL_PKG:TZ_GD_MENUGL_CLS
 * 
 * @author tang
 * @since 2015-11-16
 * 
 */
@Service("com.tranzvision.gd.TZSiteTemplateBundle.service.impl.TemplateModelMenuGlServiceImpl")
public class TemplateModelMenuGlServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private PsTzSitemMenuTMapper psTzSitemMenuTMapper;
	
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
				//String menuxhStr = jacksonUtil.getString("menuxh");
				//int menuxh = 0;
	            //if(menuxhStr != null && !"".equals(menuxhStr.trim())){
	            	//menuxh = Integer.parseInt(menuxhStr);
	            //}
	            
	            String menuId = String.valueOf(getSeqNum.getSeqNum("TZ_SITEM_MENU_T", "TZ_MENU_ID"));;
	            
	            PsTzSitemMenuT psTzSitemMenuT = new PsTzSitemMenuT();
	            psTzSitemMenuT.setTzSitemId(siteId);
	            psTzSitemMenuT.setTzMenuId(menuId);
	            psTzSitemMenuT.setTzMenuName(menuname);
	            psTzSitemMenuT.setTzMenuState(menustate);
	            psTzSitemMenuT.setTzMenuColumn(menulm);
	            psTzSitemMenuT.setTzMenuTypeId(menutypeid);
	            psTzSitemMenuT.setTzIsDel(menuisdel);
	            psTzSitemMenuT.setTzIsEditor(menuisedit);
	           // psTzSitemMenuT.setTzMenuXh(menuxh);
	            
				int i = psTzSitemMenuTMapper.insert(psTzSitemMenuT);
				if(i > 0){
					returnJsonMap.replace("menuId", menuId);
				}else{
					errMsg[0] = "1";
					errMsg[1] = "站点模板菜单信息保存失败";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
	
	/* 修改站点菜单 */
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
				//String menuxhStr = jacksonUtil.getString("menuxh");
				//int menuxh = 0;
	            //if(menuxhStr != null && !"".equals(menuxhStr.trim())){
	            //	menuxh = Integer.parseInt(menuxhStr);
	            //}

	            PsTzSitemMenuT psTzSitemMenuT = new PsTzSitemMenuT();
	            psTzSitemMenuT.setTzSitemId(siteId);
	            psTzSitemMenuT.setTzMenuId(menuId);
	            psTzSitemMenuT.setTzMenuName(menuname);
	            psTzSitemMenuT.setTzMenuState(menustate);
	            psTzSitemMenuT.setTzMenuColumn(menulm);
	            psTzSitemMenuT.setTzMenuTypeId(menutypeid);
	            psTzSitemMenuT.setTzIsDel(menuisdel);
	            psTzSitemMenuT.setTzIsEditor(menuisedit);
	            //psTzSitemMenuT.setTzMenuXh(menuxh);
	            
				int i = psTzSitemMenuTMapper.updateByPrimaryKey(psTzSitemMenuT);
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
				
				PsTzSitemMenuTKey psTzSitemMenuTKey = new PsTzSitemMenuTKey();
				psTzSitemMenuTKey.setTzSitemId(siteId);
				psTzSitemMenuTKey.setTzMenuId(menuid);
				
				PsTzSitemMenuT psTzSitemMenuT = psTzSitemMenuTMapper.selectByPrimaryKey(psTzSitemMenuTKey);
				if(psTzSitemMenuT != null){
					Map<String, Object> jsonMap = new HashMap<String, Object>();
					jsonMap.put("menuid", menuid);
					jsonMap.put("menuname", psTzSitemMenuT.getTzMenuName());
					jsonMap.put("menutypeid",psTzSitemMenuT.getTzMenuTypeId() );
					jsonMap.put("menustate", psTzSitemMenuT.getTzMenuState());
					jsonMap.put("menulm", psTzSitemMenuT.getTzMenuColumn());
					jsonMap.put("menuisdel",psTzSitemMenuT.getTzIsDel());
					jsonMap.put("menuisedit",psTzSitemMenuT.getTzIsEditor());
					jsonMap.put("menuxh",psTzSitemMenuT.getTzMenuXh());
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
		String strRet = "{}";

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
					String deletesql = "delete from PS_TZ_SITEM_MENU_T where TZ_SITEM_ID=? AND TZ_MENU_ID=? ";
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
