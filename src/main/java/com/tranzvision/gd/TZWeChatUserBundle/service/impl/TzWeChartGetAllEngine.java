package com.tranzvision.gd.TZWeChatUserBundle.service.impl;

import java.util.Map;

import com.tranzvision.gd.batch.engine.base.BaseEngine;
import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 从微信端获取全量用户
 * @author LuYan 2017-8-30
 *
 */

public class TzWeChartGetAllEngine extends BaseEngine {

	@SuppressWarnings("unchecked")
	@Override
	public void OnExecute()  throws Exception {
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		
		SqlQuery sqlQuery = (SqlQuery) getSpringBeanUtil.getAutowiredSpringBean("SqlQuery");
		
		//运行ID
		String runControlId = this.getRunControlID();
		int processinstance = this.getProcessInstanceID();
		
		try {
			
			String sql = "SELECT TZ_JG_ID,TZ_WX_APPID FROM PS_TZ_WX_USER_AET WHERE RUN_CNTL_ID=?";
			Map<String, Object> mapData = sqlQuery.queryForMap(sql,new Object[]{runControlId});
			if(mapData!=null) {
				String jgId = (String) mapData.get("TZ_JG_ID");
				String wxAppId = (String) mapData.get("TZ_WX_APPID");
				
				String[] errMsg = new String[2];
				
				TzWeChatUserMgServiceImpl tzWeChatUserMgServiceImpl = (TzWeChatUserMgServiceImpl) getSpringBeanUtil.getSpringBeanByID("TzWeChatUserMgServiceImpl");
				tzWeChatUserMgServiceImpl.getUserAllAe(jgId,wxAppId,errMsg);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			this.logError("系统错误，"+e.getMessage());
		}
	}
}
