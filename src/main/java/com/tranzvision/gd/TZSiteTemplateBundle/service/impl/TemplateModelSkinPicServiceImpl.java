package com.tranzvision.gd.TZSiteTemplateBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 站点皮肤图片设置；原：TZ_GD_ZDGL_PKG:TZ_GD_SKINPIC_CLS
 * 
 * @author tang
 * 
 */
@Service("com.tranzvision.gd.TZSiteTemplateBundle.service.impl.TemplateModelSkinPicServiceImpl")
public class TemplateModelSkinPicServiceImpl extends FrameworkImpl  {
	@Autowired
	private JacksonUtil jacksonUtil;
	@Autowired 
	private SqlQuery jdbcTemplate;
	
	/* 查询站点皮肤图片列表 */
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("total", 0);
		returnJsonMap.put("root", "[]");
		
		try {
			jacksonUtil.json2Map(comParams);
			if(jacksonUtil.containsKey("siteId") && jacksonUtil.containsKey("skinId")){
				String siteId = jacksonUtil.getString("siteId");
				String skinId = jacksonUtil.getString("skinId");
				
				String totalSQL = "SELECT COUNT(1) FROM PS_TZ_SITEM_IMG_T WHERE TZ_SITEM_ID=? and TZ_SKIN_ID=?";
				int total = jdbcTemplate.queryForObject(totalSQL,new Object[]{siteId,skinId}, "Integer");
				String sql = "";
				List<Map<String, Object>> list = null;
				if(numLimit > 0){
					sql = "SELECT TZ_IMG_ID ,TZ_IMG_XH ,TZ_IMG_NAME ,TZ_IMG_VIEW from PS_TZ_SITEM_IMG_T WHERE TZ_SITEM_ID=? and TZ_SKIN_ID=? ORDER BY TZ_IMG_XH ASC LIMIT ?,?";
					list = jdbcTemplate.queryForList(sql,new Object[]{siteId,skinId,numStart,numLimit});
				}else{
					sql = "SELECT TZ_IMG_ID ,TZ_IMG_XH ,TZ_IMG_NAME ,TZ_IMG_VIEW from PS_TZ_SITEM_IMG_T WHERE TZ_SITEM_ID=? and TZ_SKIN_ID=? ORDER BY TZ_IMG_XH ASC";
					list = jdbcTemplate.queryForList(sql,new Object[]{siteId,skinId});
				}
				
				if(list != null){
					ArrayList<Map<String, Object>> arraylist = new ArrayList<Map<String, Object>>();
					for(int i = 0; i<list.size();i++){
						Map<String, Object> jsonMap = new HashMap<String, Object>();
						jsonMap.put("imgid", list.get(i).get("TZ_IMG_ID"));
						jsonMap.put("imgxh", list.get(i).get("TZ_IMG_XH"));
						jsonMap.put("imgname", list.get(i).get("TZ_IMG_NAME"));
						jsonMap.put("imgurl", list.get(i).get("TZ_IMG_VIEW"));
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
}
