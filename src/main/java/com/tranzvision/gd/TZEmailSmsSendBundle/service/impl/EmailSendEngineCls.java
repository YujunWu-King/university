package com.tranzvision.gd.TZEmailSmsSendBundle.service.impl;

import com.tranzvision.gd.batch.engine.base.BaseEngine;
import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

public class EmailSendEngineCls extends BaseEngine {

	@Override
	public void OnExecute() throws Exception {
		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		// jdbctmplate;
		SqlQuery jdbcTemplate = (SqlQuery) getSpringBeanUtil.getAutowiredSpringBean("SqlQuery");
		// 邮件发送service;
		SendSmsOrMalServiceImpl sendSmsOrMalServiceImpl = (SendSmsOrMalServiceImpl) getSpringBeanUtil
				.getSpringBeanByID("sendSmsOrMalServiceImpl");
		// 运行id;
		String runControlId = this.getRunControlID();
		// 进程id;
		int processinstance = jdbcTemplate.queryForObject(
				"SELECT PRCSINSTANCE FROM PSPRCSRQST where RUN_ID = ? limit 0,1", new Object[] { runControlId },
				"Integer");

		jdbcTemplate.update("UPDATE PSPRCSRQST SET RUNSTATUS=? WHERE PRCSINSTANCE=?",
				new Object[] { "7", processinstance });
		jdbcTemplate.execute("commit");

		// 任务id；
		String strTaskId = jdbcTemplate.queryForObject(
				" select TZ_EML_SMS_TASK_ID from PS_TZ_EML_TASK_AET where RUN_ID =?", new Object[] { runControlId },
				"String");
		// 发送邮件;
		if (strTaskId != null && !"".equals(strTaskId)) {
			sendSmsOrMalServiceImpl.send(strTaskId, String.valueOf(processinstance));
		} else {
			sendSmsOrMalServiceImpl.send(strTaskId, String.valueOf(processinstance));
		}
		
		jdbcTemplate.update("UPDATE PSPRCSRQST SET RUNSTATUS=? WHERE PRCSINSTANCE=?",
				new Object[] { "10", processinstance });

	}
}
