package com.tranzvision.gd.workflow.engine;

import com.tranzvision.gd.TZCreateTaskBundle.service.imple.TZCreateTaskServiceImpl;
import com.tranzvision.gd.util.base.AnalysisClsMethod;
import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.base.TzException;
import com.tranzvision.gd.util.dynamicsBase.AnalysisDynaSysVar;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.workflow.base.TzEventsDesc;
import com.tranzvision.gd.workflow.base.TzWflObject;
import com.tranzvision.gd.workflow.base.TzWflParams;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 该类用于工作流事件执行处理
 * @author 张浪	2019-01-09
 *
 */
public class TzWflEvent {

	private SqlQuery sqlQuery;
	
	private TzWflObject tzWflObject;
	
	//工作流实例编号
	private String m_WflInstanceID;
	
	//步骤实例编号
	private String m_WflStpInsID;
	
	//事件执行用户ID
	private String m_userId;
	
	
	//记录日志
	private static final Logger logger = Logger.getLogger("WorkflowEngine");
	
	
	
	public void setM_WflInstanceID(String m_WflInstanceID) {
		this.m_WflInstanceID = m_WflInstanceID;
	}
	public void setM_WflStpInsID(String m_WflStpInsID) {
		this.m_WflStpInsID = m_WflStpInsID;
	}
	public void setM_userId(String m_userId) {
		this.m_userId = m_userId;
	}
	
	
	
	
	/**
	 * 构造函数
	 */
	public TzWflEvent() {
		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		sqlQuery = (SqlQuery) getSpringBeanUtil.getAutowiredSpringBean("SqlQuery");
		tzWflObject = (TzWflObject) getSpringBeanUtil.getAutowiredSpringBean("TzWflObject");
	}

	
	
	/**
	 * 执行工作流事件
	 * @param eventLevel	工作流事件级别
	 * 1-工作流级事件
	 * 2-步骤级事件
	 * 3-动作级事件
	 * 4-其他事件
	 * 5-指定步骤退回事件
	 * @param triggerPoint	事件触发时机
	 *  工作流级事件： 101-初始化前、102-初始化后、103-载入时、104-结束前、105-结束后、106-撤回时、108-退回时、109-补充意见时、110-转发时、111-提交时
	 *  步骤级事件：201-初始化前、202-初始化后、203-载入时、204-结束前、205-结束后、206-撤回时、207-签收时、208-退回时、209-转发时、210-提交时、211-退回前、212-同意时、213-拒绝时
	 *  动作级事件：301-流转前、302-流转后
	 *  其他事件：401-退回时、402-撤回时、403-转发时
	 *  指定步骤退回事件：501-退回时
	 * @param attId			参数
	 * @throws TzException
	 */
	public void ExecuteEventActions(String eventLevel, String triggerPoint, String attId) throws TzException {
		try {
			TzWflParams wflParamsRec;
			String tableName = "";
			String sqlCondition = "";
			
			switch (eventLevel) {
			case "1":	/*工作流级事件*/
				tableName = "tzms_rwfevt_t";
				sqlCondition = " and B.tzms_wfcldn_uniqueid=?";		//业务流程编号
				break;
			case "2":	/*步骤级事件*/
				tableName = "tzms_rstpev_t";
				sqlCondition = " and B.tzms_wflstp_uniqueid=?";		//步骤编号
				break;
			case "3":	/*动作级事件*/
				tableName = "tzms_ractev_t";
				sqlCondition = " and B.tzms_actcls_uniqueid=?";		//动作类编号
				break;
			case "4":	/*其他事件*/
				tableName = "tzms_rwfevt_t";
				sqlCondition = " and B.tzms_wfcldn_uniqueid=?";		//业务流程编号
				break;
			case "5":	/*指定步骤退回事件*/
				tableName = "tzms_wf_thbz_sj_t";
				sqlCondition = " and B.tzms_wf_thbz_uniqueid=?";	//退回步骤编号
				break;
			}
			
			if(StringUtils.isNotEmpty(tableName)){
				String sql = "select tzms_wfevdn_tid from tzms_wfevdn_t A," + tableName + " B where A.tzms_wfevdn_tid=B.tzms_wfevdn_uniqueid"
						+ sqlCondition + " and tzms_evlevelid=? and A.tzms_evtrgtmid=? order by B.tzms_event_xh";
				//循环执行工作流事件
				List<Map<String,Object>> eventList = sqlQuery.queryForList(sql, new Object[]{ attId, eventLevel, triggerPoint });
				
				if(eventList != null && eventList.size() > 0){
					
					String name = sqlQuery.queryForObject("select top 1 tzms_name from tzms_tea_defn_tBase where tzms_user_uniqueid=?", 
							new Object[] { m_userId }, "String");
					
					for(Map<String,Object> eventMap: eventList){
						String eventId = eventMap.get("tzms_wfevdn_tid").toString();
						
						logger.info("【事件执行开始】---> 工作流实例ID【" + m_WflInstanceID + "】，步骤实例ID【" + m_WflStpInsID 
								+ "】，事件级别【"+ TzEventsDesc.eventLevel(eventLevel) +"】，事件触发时机【"+ TzEventsDesc.eventTrigger(triggerPoint)
								+"】，事件触发执行人【"+ name +"】");
						
						//工作流相关参数
						wflParamsRec = new TzWflParams();
						wflParamsRec.setWflInsId(m_WflInstanceID);
						wflParamsRec.setWflStpInsId(m_WflStpInsID);
						wflParamsRec.setEventDefnId(eventId);
						
						try {
							this.Execute(wflParamsRec);
						} catch (Exception e) {
							e.printStackTrace();
							throw new TzException("【事件触发异常】" + e.getMessage(), e);
						}
					}
				}
			}else{
				//工作流事件级别无效
				throw new TzException("工作流事件级别无效");
			}
		}catch(TzException e) {
			logger.error("工作流事件执行出错，工作流实例ID："+ m_WflInstanceID +"，步骤实例ID："+ m_WflStpInsID 
					+"，事件级别【"+ TzEventsDesc.eventLevel(eventLevel) +"】，事件触发时机【"+ TzEventsDesc.eventTrigger(triggerPoint)
					+"】，异常信息", e);
			
			throw e;
		}catch (Exception e) {
			logger.error("工作流事件执行出错，工作流实例ID："+ m_WflInstanceID +"，步骤实例ID："+ m_WflStpInsID 
					+"，事件级别【"+ TzEventsDesc.eventLevel(eventLevel) +"】，事件触发时机【"+ TzEventsDesc.eventTrigger(triggerPoint)
					+"】，异常信息", e);
			
			throw new TzException("【事件触发异常】" + e.getMessage(), e);
		}
	}
	
	
	/**
	 * 执行事件
	 * @param eventId
	 * @throws TzException
	 */
	private void Execute(TzWflParams wflParamsRec) throws TzException {

		String eventId = wflParamsRec.getEventDefnId();
		
		Map<String,Object> eventMap = sqlQuery.queryForMap("select tzms_evactype,tzms_app_class_id,tzms_lngstrcont,tzms_task_tmp_uniqueid from tzms_wfevdn_t where tzms_wfevdn_tid=?", 
				new Object[]{ eventId });
		
		if(eventMap != null){
			//事件动作类型：1-执行SQL语句，2-执行应用程序类，3-调度系统任务
			String evactype = eventMap.get("tzms_evactype").toString();
			
			if("1".equals(evactype)){
				//SQL语句
				String sqlString = eventMap.get("tzms_lngstrcont") == null ? "" : eventMap.get("tzms_lngstrcont").toString();
				if(StringUtils.isNotEmpty(sqlString)){
					this.ParseEventID_SQL(sqlString, wflParamsRec);
				}else{
					throw new TzException("【系统配置错误】事件定义错误，事件类型为执行SQL语句，但SQL String不存在，事件编号【" + eventId + "】");
				}
			}else if("2".equals(evactype)){
				//应用程序类定义ID
				String appClassId = eventMap.get("tzms_app_class_id") == null ? "" : eventMap.get("tzms_app_class_id").toString();
				
				if(StringUtils.isNotEmpty(appClassId)){
					this.ParseEventID_APP(appClassId, wflParamsRec);
				}else{
					throw new TzException("【系统配置错误】事件定义错误，事件类型为执行应用程序类，但没有指定类定义，事件编号【" + eventId + "】");
				}
			}else if("3".equals(evactype)){
				//任务模板ID
				String taskTmpId = eventMap.get("tzms_task_tmp_uniqueid") == null ? "" : eventMap.get("tzms_task_tmp_uniqueid").toString();
				if(StringUtils.isNotEmpty(taskTmpId)){
					try {
						//创建任务
						TZCreateTaskServiceImpl tzCreateTask = tzWflObject.getTzCreateTask();
						tzCreateTask.createTask(taskTmpId, m_WflInstanceID, m_WflStpInsID);
					}catch(Exception e) {
						System.out.println("工作流事件编号【"+wflParamsRec.getEventDefnId()+"】执行出错，" + e.getMessage());
						logger.error("工作流事件编号【"+wflParamsRec.getEventDefnId()+"】执行出错，根据任务模板发送任务异常：", e);
						e.printStackTrace();
					}
				}else{
					throw new TzException("【系统配置错误】事件定义错误，事件类型为调度系统任务，但没有指定任务模板，事件编号：" + eventId);
				}
			}
		}
	}
	
	
	
	
	/**
	 * 取出指定SQL中的所有系统变量名称
	 * @param strSql
	 * @return
	 */
	private List<String> getSysVarArray(String strSql) {
		int count = 0;
		List<Integer> positionArr = new ArrayList<Integer>();
		List<String> sysVarArray = new ArrayList<String>();
		
		int Position = strSql.indexOf("$");
		while (Position >= 0) {
			positionArr.add(Position);
			Position = strSql.indexOf("$", Position + 1);

			count++;

			/* 将引用系统变量放入数组，只有$符号连续第奇偶个之间字符串才算作引用系统变量 */
			if ((Position > 0) && ((count % 2) != 0)) {
				String strArrItem = strSql.substring(positionArr.get(positionArr.size() - 1) + 1, Position);
				if (!sysVarArray.contains(strArrItem)) {
					sysVarArray.add(strArrItem);
				}
			}
		}
		
		return sysVarArray;
	}
	
	
	
	/**
	 * 执行事件SQL
	 * @param sqlString
	 * @param wflParamsRec
	 * @throws TzException
	 */
	private void ParseEventID_SQL(String sqlString, TzWflParams wflParamsRec) throws TzException {
		try {
			//系统变量
			List<String> SysVarID_List = new ArrayList<String>();
			//系统变量取值
			List<String> SysVarValue_List = new ArrayList<String>();
			
			//获取SQL中引用的系统变量
			SysVarID_List = this.getSysVarArray(sqlString);
			
			if(SysVarID_List != null && SysVarID_List.size() > 0){
				for(String SysVarID : SysVarID_List){
					//执行系统变量取值				
					/*系统变量解析开始----START*/
					AnalysisDynaSysVar analysisSysVar = new AnalysisDynaSysVar();
					analysisSysVar.setM_SysVarID(SysVarID);
					
					//业务数据ID
					String wflDateRecId = sqlQuery.queryForObject("select tzms_wflrecord_uniqueid from tzms_wflins_tbl where tzms_wflinsid=?", 
							new Object[]{ wflParamsRec.getWflInsId() }, "String");
					if(wflDateRecId == null) wflDateRecId = "";
					
					//设置参数，业务流程实例ID，步骤实例ID，事件定义ID
					String[] sysVarParam = { wflParamsRec.getWflInsId(), wflParamsRec.getWflStpInsId(), wflDateRecId, wflParamsRec.getEventDefnId() };
					analysisSysVar.setM_SysVarParam(sysVarParam);
					
					String sysVarValue = (String)analysisSysVar.GetVarValue();
					/*系统变量解析开始----END*/
					
					SysVarValue_List.add(sysVarValue);
				}
				
				int i;
				for(i=0; i< SysVarID_List.size(); i++){
					sqlString = sqlString.replaceAll("$"+ SysVarID_List.get(i) +"$", SysVarValue_List.get(i));
				}
			}
			
			logger.info("工作流事件事件SQL String: " + sqlString);
			
			sqlQuery.update(sqlString);
			
		}catch(Exception e) {
			e.printStackTrace();
			logger.error("工作流事件编号【"+wflParamsRec.getEventDefnId()+"】执行出错，异常信息", e);
			throw new TzException(e);
		}
	}
	
	
	
	/**
	 * 执行事件应用程序类
	 * @param eventId
	 * @param appClassId
	 * @param wflParamsRec
	 * @param s_UserList
	 * @throws TzException
	 */
	private void ParseEventID_APP(String appClassId, TzWflParams wflParamsRec) throws TzException {
		try {
			//初始化类方法解析引擎
			AnalysisClsMethod analysisCls = new AnalysisClsMethod(appClassId);
	
			//业务数据ID
			String wflDateRecId = sqlQuery.queryForObject("select tzms_wflrecord_uniqueid from tzms_wflins_tbl where tzms_wflinsid=?", 
					new Object[]{ wflParamsRec.getWflInsId() }, "String");
			if(wflDateRecId == null) wflDateRecId = "";
					
			logger.info("工作流事件事件应用程序类执行参数：1、【" + wflParamsRec.getWflInsId() 
				+ "】，2、【" + wflParamsRec.getWflStpInsId() 
				+ "】，3、【" + wflDateRecId 
				+ "】，4【" + wflParamsRec.getEventDefnId() + "】");
			
			//设置参数类型，如果是DLL类只能传入基本类型
			String[] parameterTypes = new String[] { "String", "String", "String", "String" };
			//设置参数数组，业务流程实例ID，步骤实例ID，事件定义ID
			Object[] arglist = new Object[] { wflParamsRec.getWflInsId(), wflParamsRec.getWflStpInsId(), wflDateRecId, wflParamsRec.getEventDefnId() };
			//设置类方法参数
			analysisCls.setJavaClsParameter(parameterTypes, arglist);
			
			//执行类方法
			analysisCls.execute();
		}catch(TzException e) {
			logger.error("工作流事件编号【"+wflParamsRec.getEventDefnId()+"】执行出错，异常信息", e);
			throw e;
		}catch(Exception e) {
			e.printStackTrace();
			logger.error("工作流事件编号【"+wflParamsRec.getEventDefnId()+"】执行出错，异常信息", e);
			throw new TzException(e);
		}
	}
	
}
