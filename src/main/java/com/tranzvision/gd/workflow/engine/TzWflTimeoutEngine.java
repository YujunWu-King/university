package com.tranzvision.gd.workflow.engine;

import com.tranzvision.gd.batch.engine.base.BaseEngine;
import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.base.TzException;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.workflow.base.ErrorMessage;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;


/**
 * 工作流步骤超时定时跑批，步骤超时后默认提交处理
 * @author zhanglang
 * 2019年10月22日
 *
 */
public class TzWflTimeoutEngine extends BaseEngine {

	
	//记录日志
	private static final Logger logger = Logger.getLogger("WorkflowEngine");
	
	
	
	@Override
	public void OnExecute() throws Exception {
		try {
			//获取自动注册的SpringBean
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			SqlQuery sqlQuery = (SqlQuery) getSpringBeanUtil.getAutowiredSpringBean("SqlQuery");

			//获取所有超时任务
			List<Map<String, Object>> timeoutWflList = sqlQuery.queryForList("select tzms_wflinsid,tzms_stpinsid,tzms_stpinsstat,tzms_stpproid from tzms_stpins_tbl A where tzms_asgmethod not in('1','3') and tzms_stpinsstat in('1','3','6','8') and tzms_tmoutdt is not null and tzms_tmoutdt<getDate() and exists(select 'Y' from tzms_wflstp_t where tzms_wflstp_tid=A.tzms_wflstpid and tzms_stptype<>1 and tzms_stp_timeout_flg=1 and tzms_stp_timeout_days>0)");
			
			if(timeoutWflList != null && timeoutWflList.size() > 0) {
				for(Map<String, Object> timeoutWflMap: timeoutWflList) {
					String tzms_wflinsid = timeoutWflMap.get("tzms_wflinsid").toString();
					String tzms_stpinsid = timeoutWflMap.get("tzms_stpinsid").toString();
					String tzms_stpinsstat = timeoutWflMap.get("tzms_stpinsstat").toString();
					String tzms_stpproid = timeoutWflMap.get("tzms_stpproid") == null ? "" : timeoutWflMap.get("tzms_stpproid").toString();
					
					try {
						if("6".equals(tzms_stpinsstat)) {
							tzms_stpproid = sqlQuery.queryForObject("select top 1 tzms_stpproid from tzms_acclst_tbl where tzms_stpinsid=? order by tzms_proidtype desc", 
									new Object[] { tzms_stpinsid }, "String");
						}

						TzWorkflow tzWorkflow = new TzWorkflow(tzms_stpproid);
						tzWorkflow.CreateWorkflow1(tzms_wflinsid, tzms_stpinsid);
						
						if("6".equals(tzms_stpinsstat)) {
							ErrorMessage signErrMsg = new ErrorMessage();
							tzWorkflow.WorkflowSign(signErrMsg);
						}
						
						ErrorMessage ErrorMsg_OUT = new ErrorMessage();
						boolean agreeOk = tzWorkflow.WorkflowSubmit("任务超时自动提交", ErrorMsg_OUT);
						if(agreeOk == false) {
							logger.error("任务超时自动提交失败");
						}
					}catch (TzException e) {
						logger.error("业务流程超时处理异常，" + e.getMessage(), e);
					}catch (Exception e) {
						e.printStackTrace();
						logger.error("业务流程超时处理异常", e);
					}
				}
			}
		} catch (Exception e) {
			logger.error("业务流程超时处理异常。", e);
		}
	}
}
