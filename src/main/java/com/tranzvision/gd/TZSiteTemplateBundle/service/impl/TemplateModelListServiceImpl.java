package com.tranzvision.gd.TZSiteTemplateBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZSiteTemplateBundle.dao.PsTzSitemDefnTMapper;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/*
 * 站点模板设置， 原PS类：TZ_GD_ZDLM_PKG:TZ_GD_MBGL_CLS
 * 描述：高端产品-站点模板list
 * @author tang
 */
@Service("com.tranzvision.gd.TZSiteTemplateBundle.service.impl.TemplateModelListServiceImpl")
public class TemplateModelListServiceImpl extends FrameworkImpl{
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private PsTzSitemDefnTMapper psTzSitemDefnTMapper;
	
	/* 查询许可权列表 */
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
			String totalSQL = "SELECT COUNT(1) FROM PS_TZ_SITEM_DEFN_T";
			int total = jdbcTemplate.queryForObject(totalSQL, "Integer");
			String sql = "";
			List<Map<String, Object>> list = null;
			if(numLimit > 0){
				sql = "SELECT TZ_SITEM_ID,TZ_SITEM_NAME,TZ_SITEM_DESCR FROM PS_TZ_SITEM_DEFN_T LIMIT ?,?";
				list = jdbcTemplate.queryForList(sql,new Object[]{numStart,numLimit});
			}else{
				sql = "SELECT TZ_SITEM_ID,TZ_SITEM_NAME,TZ_SITEM_DESCR FROM PS_TZ_SITEM_DEFN_T";
				list = jdbcTemplate.queryForList(sql);
			}
			if(list != null){
				for(int i = 0; i<list.size();i++){
					Map<String, Object> jsonMap = new HashMap<String, Object>();
					jsonMap.put("siteId", list.get(i).get("TZ_SITEM_ID"));
					jsonMap.put("sitetemplateName", list.get(i).get("TZ_SITEM_NAME"));
					jsonMap.put("explanation", list.get(i).get("TZ_SITEM_DESCR"));
					arraylist.add(jsonMap);
				}
				returnJsonMap.replace("total", total);
				returnJsonMap.replace("root", arraylist);
				strRet = jacksonUtil.Map2json(returnJsonMap);
			}
		} catch (Exception e) {
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
	
	/* 删除站点模板 */
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
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
				if (siteId != null && !"".equals(siteId)) {
					//删除站点模板基本信息;
					psTzSitemDefnTMapper.deleteByPrimaryKey(siteId);
					//删除站点皮肤基本信息;
					String deletesql = "delete from PS_TZ_SITEM_SKIN_T where TZ_SITEM_ID=? ";
					jdbcTemplate.update(deletesql, new Object[]{siteId});
					//删除站点皮肤图片信息;
					deletesql = "delete from PS_TZ_SITEM_IMG_T where TZ_SITEM_ID=? ";
					jdbcTemplate.update(deletesql, new Object[]{siteId});
					//删除站点集合;
					deletesql = "delete from PS_TZ_SITEM_TEMP_T where TZ_SITEM_ID=? ";
					jdbcTemplate.update(deletesql, new Object[]{siteId});
					//删除站点栏目;
					deletesql = "delete from PS_TZ_SITEM_COLU_T where TZ_SITEM_ID=? ";
					jdbcTemplate.update(deletesql, new Object[]{siteId});
					//删除站点区域类型;
					deletesql = "delete from PS_TZ_SITEM_ATYP_T where TZ_SITEM_ID=? ";
					jdbcTemplate.update(deletesql, new Object[]{siteId});
					//删除站点区域;
					deletesql = "delete from PS_TZ_SITEM_AREA_T where TZ_SITEM_ID=? ";
					jdbcTemplate.update(deletesql, new Object[]{siteId});
					//删除菜单类型;
					deletesql = "delete from PS_TZ_SITEM_MTYP_T where TZ_SITEM_ID=? ";
					jdbcTemplate.update(deletesql, new Object[]{siteId});
					deletesql = "delete from PS_TZ_SITEM_CDPF_T where TZ_SITEM_ID=? ";
					jdbcTemplate.update(deletesql, new Object[]{siteId});
					//删除菜单;
					deletesql = "delete from PS_TZ_SITEM_MENU_T where TZ_SITEM_ID=? ";
					jdbcTemplate.update(deletesql, new Object[]{siteId});
					
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
}

