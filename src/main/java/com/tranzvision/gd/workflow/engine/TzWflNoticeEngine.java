package com.tranzvision.gd.workflow.engine;

import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.SendSmsOrMalServiceImpl;
import com.tranzvision.gd.batch.engine.base.BaseEngine;
import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.workflow.base.TzWflObject;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;


/**
 * 工作流引擎短信、邮件通知批量发送
 * @author zhanglang
 * 2019年8月1日
 *
 */
public class TzWflNoticeEngine extends BaseEngine {

	
	//记录日志
	private static final Logger logger = Logger.getLogger("WorkflowEngine");
	
	
	
	@Override
	public void OnExecute() throws Exception {
		try {
			//获取自动注册的SpringBean
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			SqlQuery sqlQuery = (SqlQuery) getSpringBeanUtil.getAutowiredSpringBean("SqlQuery");
			TzWflObject tzWflObject = (TzWflObject) getSpringBeanUtil.getAutowiredSpringBean("TzWflObject");
			
			// 进程id;
			String processinstance = "" + this.getProcessInstanceID();

			List<String> taskList = sqlQuery.queryForList("select TZ_EML_SMS_TASK_ID from TZ_SMSEML_TASK_TBL where TZ_SEND_FLG is null or TZ_SEND_FLG<>'Y'","String");
			if(taskList != null && taskList.size() > 0) {
				
				logger.info("======================================开始批量执行发送业务流程短信邮件通知任务==========================================");
				
				for(String taskId : taskList) {
					logger.info("================>>>>>> 开始发送任务： " + taskId);
					//发送短信邮件任务
					SendSmsOrMalServiceImpl sendSmsOrMalService = tzWflObject.getSendSmsOrMalServiceImpl();
					sendSmsOrMalService.send(taskId, processinstance);
					
					sqlQuery.update("update TZ_SMSEML_TASK_TBL set TZ_SEND_FLG=?,TZ_SEND_DTTM=? where TZ_EML_SMS_TASK_ID=?", 
							new Object[] { "Y", new Date(), taskId });
				}
			}
		} catch (Exception e) {
			logger.error("发送工作流短信邮件通知异常。", e);
		}
	}
}
