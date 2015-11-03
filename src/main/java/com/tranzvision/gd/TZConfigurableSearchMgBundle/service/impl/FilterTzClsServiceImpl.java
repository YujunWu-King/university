package com.tranzvision.gd.TZConfigurableSearchMgBundle.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.PaseJsonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

import net.sf.json.JSONObject;

@Service("com.tranzvision.gd.TZConfigurableSearchMgBundle.service.impl.FilterTzClsServiceImpl")
public class FilterTzClsServiceImpl extends FrameworkImpl{
	@Autowired
	private SqlQuery jdbcTemplate;
	@Override
	public String tzGetHtmlData(String strParams) {
		String result = "";
		try{
			// 将字符串转换成json;
			JSONObject CLASSJson = PaseJsonUtil.getJson(strParams);
			// 组件编号;
			String str_com_id = CLASSJson.getString("ComID");
			//页面编号;
			String str_page_id = CLASSJson.getString("PageID");
			//视图;
			String str_view_name = CLASSJson.getString("ViewMc");
			
			String sql = "select 'Y' from PS_TZ_FILTER_DFN_T WHERE TZ_COM_ID=? AND  TZ_PAGE_ID=? AND  TZ_VIEW_NAME=? ";
			String isExist =  jdbcTemplate.queryForObject(sql, new Object[]{str_com_id,str_page_id,str_view_name},"String");
			if("Y".equals(isExist)){
				result = "{\"success\":1,\"message\":\"" + "试图添加的值已存在，请指定新值。" + "\"}";
			}else{
				result = "{\"success\":0,\"message\":\"\"}";
			}
			   
		}catch(Exception e){
			result = "{\"success\":1,\"message\":\"" + e.toString() + "\"}";
		}
		return result;
	}
}
