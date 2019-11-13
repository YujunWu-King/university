package com.tranzvision.gd.workflow.method;

import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import org.apache.commons.lang.StringUtils;


/**
 * 	工作流通用摘要系统变量封装类
 * @author zhanglang
 * 2019年7月18日
 *
 */
public class TzWflCommonAbstract {

	private SqlQuery sqlQuery;
	

	public TzWflCommonAbstract() {
		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		sqlQuery = (SqlQuery) getSpringBeanUtil.getAutowiredSpringBean("SqlQuery");
	}
	
	
	
	
	
	/**
	 * 	获取业务流程名称
	 * @param wflInsId
	 * @param stpInsId
	 * @param wflRecId
	 * @return
	 */
	public String getWflName(String wflInsId, String stpInsId, String wflRecId) {
		
		//获取流程名称
		String wflName = sqlQuery.queryForObject("select tzms_wfclname from tzms_wfcldn_tBase A where exists(select 'Y' from tzms_wflins_tbl where tzms_wflinsid=? and tzms_wfcldn_uniqueid=A.tzms_wfcldn_tid)", 
				new Object[] { wflInsId }, "String");
		
		return wflName;
	}
	
	
	
	
	
	
	/**
	 * 	获取业务流程任务号摘要
	 * @param wflInsId	业务流程实例ID
	 * @param stpInsId	步骤实例ID
	 * @param wflRecId	业务数据ID
	 * @return	#Taskid
	 */
	public String getWflTaskID(String wflInsId, String stpInsId, String wflRecId) {
		
		//获取流程关联实体
		String entityName = sqlQuery.queryForObject("select tzms_entity_name from tzms_wfcldn_tBase A where exists(select 'Y' from tzms_wflins_tbl where tzms_wflinsid=? and tzms_wfcldn_uniqueid=A.tzms_wfcldn_tid)", 
				new Object[] { wflInsId }, "String");
		
		if(StringUtils.isNotBlank(entityName)) {
			try {
				String taskSql = "select tzms_task_number from "+ entityName +"Base where "+ entityName +"Id=?"; 
				String taskID = sqlQuery.queryForObject(taskSql , new Object[] { wflRecId }, "String");
				
				if(StringUtils.isNotBlank(taskID)) {
					return "#" + taskID;
				}else {
					return "";
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return "";
	}
	
	
	
	
	/**
	 * 	获取业务流程申请人姓名
	 * @param wflInsId	业务流程实例ID
	 * @param stpInsId	步骤实例ID
	 * @param wflRecId	业务数据ID
	 * @return	
	 */
	public String getApplyUserName(String wflInsId, String stpInsId, String wflRecId) {
		
		//获取流程关联实体
		String entityName = sqlQuery.queryForObject("select tzms_entity_name from tzms_wfcldn_tBase A where exists(select 'Y' from tzms_wflins_tbl where tzms_wflinsid=? and tzms_wfcldn_uniqueid=A.tzms_wfcldn_tid)", 
				new Object[] { wflInsId }, "String");
		
		if(StringUtils.isNotBlank(entityName)) {
			try {
				String taskSql = "select tzms_apply_user from "+ entityName +"Base where "+ entityName +"Id=?"; 
				String applyUser = sqlQuery.queryForObject(taskSql , new Object[] { wflRecId }, "String");
				
				if(StringUtils.isNotBlank(applyUser)) {
					String name = sqlQuery.queryForObject("select top 1 tzms_name from tzms_tea_defn_tBase where tzms_user_uniqueid=?", 
							new Object[] { applyUser }, "String");
					
					return name;
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return "";
	}
}
