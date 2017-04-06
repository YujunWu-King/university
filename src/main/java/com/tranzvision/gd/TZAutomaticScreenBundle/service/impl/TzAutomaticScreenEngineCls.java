package com.tranzvision.gd.TZAutomaticScreenBundle.service.impl;

import java.util.Map;

import com.tranzvision.gd.batch.engine.base.BaseEngine;
import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

public class TzAutomaticScreenEngineCls extends BaseEngine {

	@Override
	public void OnExecute() throws Exception {
		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		// jdbctmplate;
		SqlQuery jdbcTemplate = (SqlQuery) getSpringBeanUtil.getAutowiredSpringBean("SqlQuery");
		
		// 自动评审service;
		TzAutoScreenEngineServiceImpl tzAutoScreenEngine = (TzAutoScreenEngineServiceImpl) getSpringBeanUtil
				.getSpringBeanByID("tzAutoScreenEngineServiceImpl");

		// 运行id;
		String runControlId = this.getRunControlID();
		
		// 班级、批次；
		Map<String,Object> paramsMap = jdbcTemplate.queryForMap("select TZ_CLASS_ID,TZ_APPLY_PC_ID,OPRID from PS_TZ_CS_JC_AET where RUN_ID =?", new Object[] { runControlId });
		// 发送邮件;
		if (paramsMap != null) {
			String classId = paramsMap.get("TZ_CLASS_ID").toString();
			String batchId = paramsMap.get("TZ_APPLY_PC_ID").toString();
			String oprId = paramsMap.get("OPRID").toString();
			
			tzAutoScreenEngine.autoScreen(classId, batchId, oprId,this);
		} 
	}
}
