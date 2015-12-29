package com.tranzvision.gd.TZSiteTemplateBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZSiteTemplateBundle.dao.PsTzSitemCdpfTMapper;
import com.tranzvision.gd.TZSiteTemplateBundle.model.PsTzSitemCdpfT;
import com.tranzvision.gd.TZSiteTemplateBundle.model.PsTzSitemCdpfTKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 菜单图标皮肤设置；原：TZ_GD_DZMB_PKG:TZ_GD_MENUICON_CLS
 * 
 * @author tang
 * @since 2015-11-16
 * 
 */
@Service("com.tranzvision.gd.TZSiteTemplateBundle.service.impl.TemplateModelMenuIconServiceImple")
public class TemplateModelMenuIconServiceImple extends FrameworkImpl {
	@Autowired 
	private SqlQuery jdbcTemplate;
	@Autowired
	private PsTzSitemCdpfTMapper psTzSitemCdpfTMapper;
	
	/* 查询区域类型管理列表 */
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
			if(jacksonUtil.containsKey("siteId") && jacksonUtil.containsKey("menutypeid")){
				String siteId = jacksonUtil.getString("siteId");
				String menutypeid = jacksonUtil.getString("menutypeid");
				
				String siteIconCountSQL = " SELECT COUNT(1) FROM PS_TZ_SITEM_CDPF_T WHERE TZ_SITEM_ID=? and TZ_MENU_TYPE_ID=?";
				int siteIconCount = jdbcTemplate.queryForObject(siteIconCountSQL, new Object[]{siteId,menutypeid},"Integer");
				if(siteIconCount == 0){
					String skinSQL = "SELECT TZ_SKIN_ID,TZ_SKIN_STATE,TZ_SKIN_NAME FROM PS_TZ_SITEM_SKIN_T WHERE TZ_SITEM_ID=?";
					List<Map<String, Object>> skinList = jdbcTemplate.queryForList(skinSQL,new Object[]{siteId});
					if(skinList != null){
						for(int i = 0; i < skinList.size(); i++){
							PsTzSitemCdpfT psTzSitemCdpfT = new PsTzSitemCdpfT();
							psTzSitemCdpfT.setTzSitemId(siteId);
							psTzSitemCdpfT.setTzMenuTypeId(menutypeid);
							psTzSitemCdpfT.setTzSkinId((String)skinList.get(i).get("TZ_SKIN_ID"));
							psTzSitemCdpfT.setTzSkinState((String)skinList.get(i).get("TZ_SKIN_STATE"));
							psTzSitemCdpfT.setTzSkinName((String)skinList.get(i).get("TZ_SKIN_NAME"));
							psTzSitemCdpfTMapper.insert(psTzSitemCdpfT);
						}
					}
				}
				
				String totalSQL ="SELECT COUNT(1) FROM PS_TZ_SITEM_CDPF_T where TZ_SITEM_ID = ? and TZ_MENU_TYPE_ID = ?";
				int total = jdbcTemplate.queryForObject(totalSQL,new Object[]{siteId,menutypeid}, "Integer");
				String sql = "";
				List<Map<String, Object>> list = null;
				if(numLimit > 0){
					sql = "SELECT TZ_SKIN_ID,TZ_SKIN_STATE,TZ_SKIN_NAME FROM PS_TZ_SITEM_CDPF_T where TZ_SITEM_ID = ? and TZ_MENU_TYPE_ID = ? LIMIT ?,?";
					list = jdbcTemplate.queryForList(sql,new Object[]{siteId,menutypeid,numStart,numLimit});
				}else{
					sql = "SELECT TZ_SKIN_ID,TZ_SKIN_STATE,TZ_SKIN_NAME FROM PS_TZ_SITEM_CDPF_T where TZ_SITEM_ID = ? and TZ_MENU_TYPE_ID = ? ";
					list = jdbcTemplate.queryForList(sql,new Object[]{siteId,menutypeid});
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
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			if (jacksonUtil.containsKey("siteId")) {
				// 站点id, 区域类型ID;
				String siteId = jacksonUtil.getString("siteId");
				String menutypeid = jacksonUtil.getString("menutypeid");
				String skinId = jacksonUtil.getString("skinId");
				
				PsTzSitemCdpfTKey psTzSitemCdpfTKey = new PsTzSitemCdpfTKey();
				psTzSitemCdpfTKey.setTzSitemId(siteId);
				psTzSitemCdpfTKey.setTzMenuTypeId(menutypeid);
				psTzSitemCdpfTKey.setTzSkinId(skinId);
				PsTzSitemCdpfT psTzSitemCdpfT = psTzSitemCdpfTMapper.selectByPrimaryKey(psTzSitemCdpfTKey);
	
				if(psTzSitemCdpfT != null){
					Map<String, Object> jsonMap = new HashMap<String, Object>();
					String menutypename = psTzSitemCdpfT.getTzMenuTypeId();
					String menutypenameSQL = "SELECT TZ_MENU_TYPE_NAME FROM PS_TZ_SITEM_MTYP_T WHERE TZ_SITEM_ID=? AND TZ_MENU_TYPE_ID=? AND TZ_TYPE_STATE='Y'";
					menutypename = jdbcTemplate.queryForObject(menutypenameSQL, new Object[]{siteId,menutypeid},"String");
					jsonMap.put("siteid", siteId);
					jsonMap.put("menutypeid", menutypeid);
					jsonMap.put("skinid",skinId );
					jsonMap.put("menutypename",menutypename);
					jsonMap.put("skinname",psTzSitemCdpfT.getTzSkinName() );
					jsonMap.put("menutypeimg",psTzSitemCdpfT.getTzTypeImg());
					jsonMap.put("menunowimg",psTzSitemCdpfT.getTzNowImg() );
					returnJsonMap.replace("formData", jsonMap);
				}else{
					errMsg[0] = "1";
					errMsg[1] = "未找到对应菜单类型图标信息";
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
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				String siteid = jacksonUtil.getString("siteid");
				String menutypeid = jacksonUtil.getString("menutypeid");
				String skinid = jacksonUtil.getString("skinid");
				String menutypeimg = jacksonUtil.getString("menutypeimg");
				String menunowimg = jacksonUtil.getString("menunowimg");
				
				String updateSQL = "update PS_TZ_SITEM_CDPF_T set TZ_TYPE_IMG=? ,TZ_NOW_IMG=? where TZ_SITEM_ID = ? and TZ_MENU_TYPE_ID = ? and TZ_SKIN_ID = ?";
				jdbcTemplate.update(updateSQL,new Object[]{menutypeimg,menunowimg,siteid,menutypeid,skinid});
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
			JacksonUtil jacksonUtil = new JacksonUtil();
			try{
				jacksonUtil.json2Map(comParams);
				String siteId = jacksonUtil.getString("siteId");
				String menutypeid = jacksonUtil.getString("menutypeid");
				String skinId = jacksonUtil.getString("skinId");
				String path = jacksonUtil.getString("path");
				String imgtype = jacksonUtil.getString("imgtype");
				
				PsTzSitemCdpfT psTzSitemCdpfT = new PsTzSitemCdpfT();
				psTzSitemCdpfT.setTzSitemId(siteId);
				psTzSitemCdpfT.setTzMenuTypeId(menutypeid);
				psTzSitemCdpfT.setTzSkinId(skinId);
				if("TZ_TYPE_IMG".equals(imgtype)){
					psTzSitemCdpfT.setTzTypeImg(path);
				}else if("TZ_NOW_IMG".equals(imgtype)){
					psTzSitemCdpfT.setTzNowImg(path);
				}
				int i = psTzSitemCdpfTMapper.updateByPrimaryKeySelective(psTzSitemCdpfT);

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
