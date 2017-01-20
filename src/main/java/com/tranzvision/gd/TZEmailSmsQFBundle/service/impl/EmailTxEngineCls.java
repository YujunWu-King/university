package com.tranzvision.gd.TZEmailSmsQFBundle.service.impl;

import java.util.List;
import java.util.Map;

import com.tranzvision.gd.batch.engine.base.BaseEngine;
import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

public class EmailTxEngineCls extends BaseEngine {
	@Override
	public void OnExecute() throws Exception {
		
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			// jdbctmplate;
			SqlQuery jdbcTemplate = (SqlQuery) getSpringBeanUtil.getAutowiredSpringBean("SqlQuery");
			// 邮件发送service;
			analysisBounceServiceImpl analysisBounceServiceImpl = (analysisBounceServiceImpl) getSpringBeanUtil
					.getSpringBeanByID("analysisBounceServiceImpl");
			// 运行id;
			String runControlId = this.getRunControlID();
			// 进程id;
			int processinstance = jdbcTemplate.queryForObject(
					"SELECT PRCSINSTANCE FROM PSPRCSRQST where RUN_ID = ? limit 0,1", new Object[] { runControlId },
					"Integer");

			jdbcTemplate.update("UPDATE PSPRCSRQST SET RUNSTATUS=? WHERE PRCSINSTANCE=?",
					new Object[] { "7", processinstance });
			jdbcTemplate.execute("commit");
			try{	
				String qfpcId = jdbcTemplate.queryForObject("select TZ_MLSM_QFPC_ID from PS_TZ_DXYJQF_TBL WHERE RUN_CNTL_ID=?", new Object[]{runControlId},"String");
				if(qfpcId != null && !"".equals(qfpcId)){
					String sql = "SELECT TZ_EML_SMS_TASK_ID,TZ_EMLSERV_ID FROM PS_TZ_DXYJFSRW_TBL WHERE TZ_MLSM_QFPC_ID=?";
					List<Map<String , Object>> list = jdbcTemplate.queryForList(sql, new Object[]{qfpcId});
					if(list != null && list.size() > 0){
						for(int j = 0; j < list.size(); j++){
							String mailServId = (String)list.get(j).get("TZ_EMLSERV_ID");
							analysisBounceServiceImpl.analysisBounceByMailServId(qfpcId, mailServId, processinstance);
						}
					}
				}
				jdbcTemplate.update("UPDATE PSPRCSRQST SET RUNSTATUS=? WHERE PRCSINSTANCE=?",
						new Object[] { "9", processinstance });
			}catch(Exception e){
				jdbcTemplate.update("UPDATE PSPRCSRQST SET RUNSTATUS=? WHERE PRCSINSTANCE=?",
						new Object[] { "10", processinstance });
			}
	}
}
