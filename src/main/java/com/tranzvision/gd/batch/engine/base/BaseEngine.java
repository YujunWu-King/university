/**
 * 
 */
package com.tranzvision.gd.batch.engine.base;

import org.springframework.transaction.TransactionStatus;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import org.quartz.TriggerUtils;
import org.quartz.JobExecutionException;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.quartz.spi.OperableTrigger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.List;

import com.tranzvision.gd.batch.server.BaseJob;
import com.tranzvision.gd.util.base.TzException;
import com.tranzvision.gd.util.sql.type.TzRecord;
import com.tranzvision.gd.util.sql.type.TzString;
import com.tranzvision.gd.util.sql.type.TzInt;
import com.tranzvision.gd.util.sql.SqlParams;

/**
 * @author LiGang
 * 2015/12/02
 */
public class BaseEngine extends BaseJob implements Runnable
{
	//归属机构ID
	private String organizationID = "";
	private static final String ENGINE_JOB_ORGANIZATION_ID = "EngineJobOrganizationID";
	
	//作业进程实例ID
	private Integer processInstanceID = 0;
	private static final String ENGINE_JOB_PROCESS_INSTANCE_ID = "EngineJobProcessInstanceID";
	
	//作业进程名称
	private String jobProcessName = "";
	
	//日志流水号
	private int logNumber = 1;
	
	//作业进程运行控制ID
	private String processRunControlID = "";
	
	//作业进程实例对象记录
	private TzRecord processInstanceRecord = null;
	
	//循环表达式
	private String cronCycleName = "";
	private String cronExpression = "";
	
	//用于新增日志的对象记录
	private TzRecord loggerRecord = null;
	
	//当前实例的计划执行时间
	private Date planExecuteDateTime = null;
	//下次循环计划执行时间
	private Date nextExecuteDateTime = null;
	
	//可否启动多线程执行的标志
	private boolean canStartThread = false;
	
	//是否退出执行并终止执行任务的标志，此标志用与通知子类的OnExecute方法主动结束任务
	private boolean exitFlag = false;
	
	//记录实例运行状态的变量
	private String processRunStatus = "QUENED";
	
	//构造方法
	public BaseEngine()
	{
		;
	}
		
	//构造方法
	public BaseEngine(String orgId,Integer procInstanceId)
	{
		organizationID = orgId == null ? "" : orgId.trim();
		processInstanceID = procInstanceId == null ? 0 : (procInstanceId <= 0 ? 0 : procInstanceId);
	}
	
	//子类的OnExecute方法可调用此方法来获取是否强制退出任务执行的标志
	final public boolean isForceExit()
	{
		return exitFlag;
	}
	
	//用于多线程执行的例子
	final public void run()
	{
		//不允许外部直接调用这个run方法
		if(canStartThread == false)
		{
			return ;
		}
		
		//不允许外部直接调用这个run方法
		canStartThread = false;
		
		//开始事务
		TransactionStatus status = getTransaction();
		
		//执行Job进程的主要业务逻辑
		try
		{
			OnExecute();
			
			commit(status);
		}
		catch(Exception e)
		{
			rollback(status);
			
			//将运行状态由“RUNNING”更新为“ERROR”
			updateStatus("ERROR");
						
			//记录日志
			logError("an error occurred during the job process running.\n" + e.toString());
			//将日志写到日志文件中
			error("an error occurred during the job process running.\n" + e.toString());
		}
	}
	
	public String getOrganziationID()
	{
		return organizationID;
	}
	
	private void setOrganziationID(String orgId)
	{
		organizationID = orgId == null ? "" : orgId.trim();
	}
	
	public String getProcessName()
	{
		return jobProcessName;
	}
	
	private void setProcessName(String procName)
	{
		jobProcessName = procName == null ? "" : procName.trim();
	}
	
	public String getRunControlID()
	{
		return processRunControlID;
	}
	
	//初始化当前Job进程的方法
	final private boolean initialize()
	{
		try
		{
			if(organizationID.equals("") == true)
			{
				throw new TzException("the specifed organization ID for the job process is null.");
			}
			
			if(processInstanceID <= 0)
			{
				throw new TzException("the specifed instance ID for the job process is null.");
			}
			
			//根据指定机构ID和进程名称初始化进程实例
			processInstanceRecord = createRecord("TZ_JC_SHLI_T");
			
			//读取进程实例记录
			processInstanceRecord.setColumnValue("TZ_JG_ID", organizationID,true);
			
			processInstanceRecord.setColumnValue("TZ_JCSL_ID", processInstanceID,true);
			
			if(processInstanceRecord.selectByKey() == false)
			{
				throw new TzException("can't get the record object of the job process by the specified organization ID[" + organizationID + "] and process name[" + jobProcessName + "].");
			}
			
			//检查是否有进程名称，如果没有进程名称，则抛出异常
			String tmpProcessName = processInstanceRecord.getTzString("TZ_JC_MC").getValue();
			if(tmpProcessName == null || tmpProcessName.trim().equals("") == true)
			{
				throw new TzException("can't get the name of the job process [" + organizationID + "." + processInstanceID + "].");
			}
			
			//判断进程名称是否有效
			TzString validProcessFlag = new TzString();
			String sqlText = "SELECT 'X' VALID_FLAG FROM TZ_JINC_DY_T WHERE TZ_JG_ID=? AND TZ_JC_MC=?";
			this.sqlExec(sqlText,new SqlParams(organizationID,tmpProcessName), validProcessFlag);
			if("X".equals(validProcessFlag.getValue()) == false)
			{
				throw new TzException("the specified process for the job process [" + organizationID + "." + processInstanceID + "] is not valid.");
			}
			
			//检查是否有运行控制ID，如果没有运行控制ID，则抛出异常
			String tmpRunCtrlId = processInstanceRecord.getTzString("TZ_YUNX_KZID").getValue();
			if(tmpRunCtrlId == null || tmpRunCtrlId.trim().equals("") == true)
			{
				throw new TzException("can't get the run control ID of the job process [" + organizationID + "." + processInstanceID + "].");
			}
			
			//读取计划执行时间
			Date tmpPlanExeDatetime = processInstanceRecord.getTzDate("TZ_JHZX_DTTM").getValue();
			if(tmpPlanExeDatetime == null)
			{
				throw new TzException("the datetime to plan to execite the job process [" + organizationID + "." + processInstanceID + "] is not specified.");
			}
			
			//获取循环取值表达式
			cronCycleName = processInstanceRecord.getTzString("TZ_XH_QZBDS").getValue();
			//判断是否存在与循环取值表达式相同的循环名称
			TzString tmpCronExpression = new TzString();
			sqlText = "SELECT TZ_XH_QZBDS FROM TZ_XUNH_DEFN_T WHERE TZ_JG_ID=? AND TZ_XH_MC=?";
			this.sqlExec(sqlText,new SqlParams(organizationID,cronCycleName), tmpCronExpression);
			if("".equals(tmpCronExpression.getValue().trim()) == true)
			{
				//如果不存在对应的循环定义，则直接将其作为cronExpression表达式进行处理
				cronExpression = cronCycleName;
			}
			else
			{
				//存在对应的循环定义，则将循环定义对应的cronExpression表达式取出来进行处理
				cronExpression = tmpCronExpression.getValue().trim();
			}
			cronExpression = cronExpression == null ? "" : cronExpression.trim();
			
			//保存初始化状态变量
			jobProcessName = tmpProcessName;
			processRunControlID = tmpRunCtrlId;
			//trigger = tmpTrigger;
			planExecuteDateTime = tmpPlanExeDatetime;
			
			//生成下次计划时间
			if(cronExpression.equals("") == false)
			{
				CronTriggerImpl tmpCronTrigger = new CronTriggerImpl();
				tmpCronTrigger.setCronExpression(cronExpression.trim());
				
				Date tmpNextDate = tmpCronTrigger.getFireTimeAfter(planExecuteDateTime);
				Date fromDate = planExecuteDateTime;
				Date toDate = new Date(tmpNextDate.getTime() + tmpNextDate.getTime() - planExecuteDateTime.getTime());
				
				//生成未来两三个执行时间计划列表
				List<Date> schdPlanDateList = TriggerUtils.computeFireTimesBetween((OperableTrigger)tmpCronTrigger,null,fromDate,toDate);
				if(planExecuteDateTime.before(schdPlanDateList.get(0)) == true)
				{
					nextExecuteDateTime = schdPlanDateList.get(0);
				}
				else
				{
					nextExecuteDateTime = schdPlanDateList.get(1);
				}
			}
		}
		catch(TzException e)
		{
			logFatal(e.toString());
			return false;
		}
		catch(Exception e)
		{
			logFatal("a fatal error occurred when tried to initialize the job process [" + organizationID + "." + processInstanceID + "].\n" + e.toString());
			return false;
		}
		
		return true;
	}
	
	//向进程运行日志表[TZ_JCYX_LOG_T]记录“ERROR”级别日志的方法
	final public void logError(String message)
	{
		logRecord(message,"ERROR");
	}
	
	//向进程运行日志表[TZ_JCYX_LOG_T]记录“FATAL”级别日志的方法
	final public void logFatal(String message)
	{
		logRecord(message,"FATAL");
	}
	
	//向进程运行日志表[TZ_JCYX_LOG_T]记录“INFO”级别日志的方法
	final public void logInfo(String message)
	{
		logRecord(message,"INFO");
	}
	
	//向进程运行日志表[TZ_JCYX_LOG_T]记录“WARN”级别日志的方法
	final public void logWarn(String message)
	{
		logRecord(message,"WARN");
	}
	
	final synchronized private void logRecord(String message,String level)
	{
		try
		{
			if(loggerRecord == null)
			{
				return;
			}
			
			if(message == null || message.trim().equals("") == true)
			{
				return;
			}
			
			message = message.trim();
			
			loggerRecord.setColumnValue("TZ_JG_ID", organizationID);
			loggerRecord.setColumnValue("TZ_JCSL_ID", processInstanceID);
			loggerRecord.setColumnValue("TZ_RZ_LSH", logNumber);
			loggerRecord.setColumnValue("TZ_RZ_JB", level);
			loggerRecord.setColumnValue("TZ_RZ_DTTM", new Date());
			loggerRecord.setColumnValue("TZ_RZ_NR", message.length() > 500 ? message.substring(0, 500) : message);
			
			loggerRecord.insert();
			logNumber ++;
		}
		catch(Exception e)
		{
			error("failed to write the message into the log table.\nmessage[" + level + "]:\n" + message + "\n" + e.toString());
		}
	}
	
	//创建日志记录Record
	final private void createLoggerRecord() throws TzException
	{
		try
		{
			//创建文件日志记录对象
			String middleFolder = "BatchServer/" + organizationID + "." + getBatchServerName() + "/" + new SimpleDateFormat("yyyyMM").format(new Date());
			setLogger(organizationID + "." + getBatchServerName() + "." + processInstanceID,middleFolder,"bp_" + processInstanceID + ".log",false);
			
			//创建数据库日志记录对象
			loggerRecord = createRecord("TZ_JCYX_LOG_T");
			
			//获取当前Job进程实例的最大日志号
			TzInt tmpLogNumber = new TzInt();
			String sqlText = "SELECT MAX(TZ_RZ_LSH) + 1 MAX_RZ_LSH FROM TZ_JCYX_LOG_T WHERE TZ_JG_ID=? AND TZ_JCSL_ID=?";
			this.sqlExec(sqlText,new SqlParams(organizationID,processInstanceID),tmpLogNumber);
			logNumber = tmpLogNumber.getValue() > logNumber ? tmpLogNumber.getValue() : logNumber;
		}
		catch(Exception e)
		{
			throw new TzException("can't create the logger record object for the current process instance[" + organizationID + "." + processInstanceID + "].");
		}
	}
	
	final synchronized private boolean updateStatus(String status)
	{
		try
		{
			switch(status)
			{
			//已启动
			case "STARTED":
				processInstanceRecord.setColumnValue("TZ_JCFWQ_MC",getBatchServerName());
				processInstanceRecord.setColumnValue("TZ_JOB_YXZT","STARTED");
				processInstanceRecord.setColumnValue("TZ_ZJXTSJ", new Date());
				processInstanceRecord.setColumnValue("TZ_JCKS_DTTM", new Date());
				break;
			
			//正在运行中
			case "RUNNING":
				processInstanceRecord.setColumnValue("TZ_JOB_YXZT", "RUNNING");
				processInstanceRecord.setColumnValue("TZ_ZJXTSJ", new Date());
				break;
			
			//成功完成
			case "SUCCEEDED":
				processInstanceRecord.setColumnValue("TZ_JOB_YXZT", "SUCCEEDED");
				processInstanceRecord.setColumnValue("TZ_ZJXTSJ", new Date());
				processInstanceRecord.setColumnValue("TZ_JCJS_DTTM", new Date());
				break;
			
			//发生错误
			case "ERROR":
				processInstanceRecord.setColumnValue("TZ_JOB_YXZT", "ERROR");
				processInstanceRecord.setColumnValue("TZ_JCJS_DTTM", new Date());
				break;
			
			//发生严重错误
			case "FATAL":
				processInstanceRecord.setColumnValue("TZ_JOB_YXZT", "FATAL");
				processInstanceRecord.setColumnValue("TZ_JCJS_DTTM", new Date());
				break;
			
			//正在停止
			case "STOPPING":
				processInstanceRecord.setColumnValue("TZ_JOB_YXZT", "STOPPING");
				processInstanceRecord.setColumnValue("TZ_ZJXTSJ", new Date());
				break;
			
			//已强行终止
			case "TERMINATED":
				processInstanceRecord.setColumnValue("TZ_JOB_YXZT", "TERMINATED");
				processInstanceRecord.setColumnValue("TZ_JCJS_DTTM", new Date());
				break;
			
			//默认情况
			default:
				return false;
			}
			
			//记录最新的运行状态
			processRunStatus = status;
			
			//执行更新操作
			processInstanceRecord.update();
		}
		catch(Exception e)
		{
			warn("failed to update the status of the process job[" + organizationID + "." + processInstanceID + "] to \"" + status + "\".\n" + e.toString());
			logWarn("failed to update the status of the process job[" + organizationID + "." + processInstanceID + "] to \"" + status + "\".\n" + e.toString());
			
			return false;
		}
		
		return true;
	}
	
	final private void heartBeat()
	{
		try
		{
			//设置心跳时间
			processInstanceRecord.setColumnValue("TZ_ZJXTSJ", new Date());
			
			//更新心跳时间
			processInstanceRecord.update();
		}
		catch(Exception e)
		{
			warn("failed to record the heartbeat time for the job process[" + organizationID + "." + processInstanceID + "]\n" + e.toString());
		}
	}
	
	final private String getDataBaseJobStatus()
	{
		//获取当前的Job进程在数据库中的最新状态
		try
		{
			TzString status = new TzString();
			String sqlText = "SELECT TZ_JOB_YXZT FLAG FROM TZ_JC_SHLI_T WHERE TZ_JG_ID=? AND TZ_JCSL_ID=?";
			
			this.sqlExec(sqlText, new SqlParams(organizationID,processInstanceID), status);
			
			return status.getValue() == null ? "" : status.getValue().trim();
		}
		catch(Exception e)
		{
			warn("failed to check the status of the job process[" + organizationID + "." + processInstanceID + "].\n" + e.toString());
		}
		
		return "RUNNING";
	}
	
	//进行下次调度
	final private void scheduleNext()
	{
		//如果指定了循环对象，则进行下一次调度
		if(cronExpression.equals("") == false)
		{
			try
			{
				//调度下一个循环作业任务
				EngineParameters schdProcessParameters = new EngineParameters();
				schdProcessParameters.setBatchServer(getBatchServerName());
				schdProcessParameters.setCycleExpression(cronCycleName);
				schdProcessParameters.setLoginUserAccount(processInstanceRecord.getTzString("TZ_DLZH_ID").getValue());
				schdProcessParameters.setPlanExcuteDateTime(nextExecuteDateTime);
				schdProcessParameters.setRunControlId(processRunControlID);
				
				//进行调度
				schedule(schdProcessParameters);
			}
			catch(Exception e)
			{
				error("an error occured when try to schudule the next cycle task[" + organizationID + "." + jobProcessName + "].\n" + e.toString());
			}
		}
	}
	
	//启动运行当前Job进程的方法
	final public void execute() throws JobExecutionException
	{
		//先执行父类的“execute”方法
		super.execute();
		
		//从上下文环境中获取机构ID
		organizationID = (String)getJobExecutionContext().getJobDetail().getJobDataMap().get(ENGINE_JOB_ORGANIZATION_ID);
		organizationID = organizationID == null ? "" : organizationID.trim();
		
		//从上下文环境中获取Job进程实例ID
		processInstanceID = (Integer)getJobExecutionContext().getJobDetail().getJobDataMap().get(ENGINE_JOB_PROCESS_INSTANCE_ID);
		processInstanceID = processInstanceID <= 0 ? 0 : processInstanceID;
		
		//创建日志记录对象
		try
		{
			createLoggerRecord();
		}
		catch(Exception e)
		{
			fatal(e.toString());
			throw new JobExecutionException(e.toString());
		}
		
		//开始初始化作业进程
		logInfo("begin to initialize the job process instance ...");
		if(initialize() == false)
		{
			updateStatus("FATAL");
			
			return;
		}
		logInfo("succeeded in initializing  the job process instance.");
		
		//运行状态从“QUENED”更新为“STARTED”
		if(updateStatus("STARTED") == false)
		{
			return;
		}
		
		//启动Job时，先记录心跳的时间
		try
		{
			heartBeat();
		}
		catch(Exception e)
		{
			//运行状态从“RUNNING”更新为“FATAL”
			updateStatus("FATAL");
			
			//将日志写到日志文件中
			fatal("an fatal error occurred during the job process running.\n" + e.toString());
			//记录日志
			logFatal("an fatal error occurred during the job process running.\n" + e.toString());
			
			return;
		}
		
		//启动一个线程来执行Job进程的主要业务逻辑
		Thread t = new Thread(this);
		canStartThread = true;
		t.start();
		
		//运行状态从“STARTED”更新为“RUNNING”
		if(updateStatus("RUNNING") == false)
		{
			return;
		}
		
		//循环等待作业线程执行完成
		String tmpRunningStatus = "SUCCEEDED";
		int hearBeatTime = 0;
		while(processRunStatus.equals("RUNNING") == true && t.isAlive() == true)
		{
			//记录心跳时间，每30次循环记录一次心跳时间
			if(hearBeatTime >= 30)
			{
				heartBeat();
				hearBeatTime = 0;
			}
			
			//获取当前Job任务在数据库中的状态
			String jobDBStatus = getDataBaseJobStatus();
			
			//检查当前Job任务在数据库中的状态是否设置了强制终止任务的标志
			if("STOPPING".equals(jobDBStatus) == true)
			{
				//强行终止任务
				try
				{
					exitFlag = true;
					tmpRunningStatus = "TERMINATED";
					t.interrupt();
				}
				catch(Exception e)
				{
					warn("an error occurred when try to terminate the process job[" + organizationID + "." + processInstanceID + "].\n" + e.toString());
				}
			}
			else if("RUNNING".equals(jobDBStatus) == false)
			{
				//如果数据库状态不是“RUNNING”则表示当前Job任务已被要求强制退出或者已经死亡
				exitFlag = true;
				
				//直接返回
				return ;
			}
			
			//心跳间隔循环计次
			hearBeatTime ++;
			
			//睡眠2秒钟
			sleep(2000);
		}
		
		//运行状态从“RUNNING”更新为“SUCCEEDED”
		if(updateStatus(tmpRunningStatus) == false)
		{
			return;
		}
		
		//进行下一次调度
		if("SUCCEEDED".equals(tmpRunningStatus) == true)
		{
			scheduleNext();
		}
		else if("TERMINATED".equals(tmpRunningStatus) == true)
		{
			//记录强行终止日志
			logInfo("the process job[" + organizationID + "." + processInstanceID + "] has been forcibly terminated.");
		}
	}
	
	//检查指定用户是否有权向运行当前指定的Job进程
	final private void checkPermissionList(String orgId,String procName,String loginUser) throws TzException
	{
		try
		{
			TzString aqFlag = new TzString();
			String sqlText = getSQLText("SQL.TZBatchServer.TzCanExecuteJob");
			
			this.sqlExec(sqlText,new SqlParams(orgId,procName,loginUser),aqFlag);
			
			if("X".equals(aqFlag.getValue().trim()) == false)
			{
				throw new TzException("the current user[" + loginUser + "] has no permission to run the currently specified job[" + orgId + "." + procName + "].");
			}
		}
		catch(Exception e)
		{
			throw new TzException("an error occured when check the current user's[" + loginUser + "] permission to run the currently specified job[" + orgId + "." + procName + "].\n" + e.toString());
		}
	}
	
	//发起一个调度Job进程的方法，参数schdDateTime指定了Job进程的调度执行时间
	final private void check(EngineParameters schdProcessParameters) throws TzException
	{
		//创建Job进程调度请求时
		if(schdProcessParameters == null)
		{
			throw new TzException("not specify the required parameters when try to create a request to run an engine process.");
		}
		
		//机构ID不能为空
		if(organizationID == null || organizationID.trim().equals("") == true)
		{
			throw new TzException("not specify the organization ID for the job process.");
		}
		
		//检查运行控制ID，如果没有指定运行控制ID，则创建一个运行控制ID
		if(schdProcessParameters.getRunControlId().equals("") == true)
		{
			schdProcessParameters.setRunControlId(UUID.randomUUID().toString());
		}
		
		//必须指定进程请求发起人
		if(schdProcessParameters.getLoginUserAccount().equals("") == true)
		{
			throw new TzException("couldn't get the user who initiated the request to run an engine process[" + organizationID + "." + getProcessName() + "].");
		}
		
		//检查当前指定的Job进程
		if(getProcessName().equals("") == true)
		{
			throw new TzException("not specify the process name for the request to run an engine process.");
		}
		
		//检查指定用户是否有权向运行当前指定的Job进程
		checkPermissionList(organizationID,getProcessName(),schdProcessParameters.getLoginUserAccount());
		
		//检查循环表达式是否正确
		if(schdProcessParameters.getCycleExpression().equals("") == false)
		{
			try
			{
				String tmpCronExpression = "";
				
				//先检查是否存在与指定参数值相同的循环对象，如果存在，则从循环对象中取循环取值表达式
				String sqlText = "SELECT 'X' EXISTS_FLAG,TZ_XH_QZBDS FROM TZ_XUNH_DEFN_T WHERE TZ_JG_ID=? AND TZ_XH_MC=?";
				TzRecord tmpRecord = new TzRecord();
				sqlExec(sqlText, new SqlParams(organizationID,schdProcessParameters.getCycleExpression()), tmpRecord);
				if("X".equals(tmpRecord.getTzString("EXISTS_FLAG").getValue()) == true)
				{
					tmpCronExpression = tmpRecord.getTzString("TZ_XH_QZBDS").getValue();
					tmpCronExpression = tmpCronExpression == null ? "" : tmpCronExpression.trim();
				}
				else
				{
					//否则将当前参数值作为循环表达式进行解析，如果解析失败则抛出异常
					tmpCronExpression = schdProcessParameters.getCycleExpression();
				}
				
				//Build一个CronScheduleBuilder实例，以检查循环取值表达是是否合法
				cronSchedule(tmpCronExpression);
			}
			catch(Exception e)
			{
				throw new TzException("the specified cycle[" + schdProcessParameters.getCycleExpression() + "] for the process job is not valid.\n" + e.toString());
			}
		}
		
		//检查指定的Batch Server是否合法
		if(schdProcessParameters.getBatchServer().equals("") == false)
		{
			try
			{
				//检查是否存在指定的Batch Server
				String sqlText = "SELECT 'X' EXISTS_FLAG FROM TZ_JC_FWQDX_T WHERE TZ_JG_ID=? AND TZ_JCFWQ_MC=?";
				TzString existsFlag = new TzString();
				sqlExec(sqlText,new SqlParams(organizationID,schdProcessParameters.getBatchServer()),existsFlag);
				if("X".equals(existsFlag.getValue()) == false)
				{
					throw new TzException("the specified batch server[" + schdProcessParameters.getBatchServer() + "] for the job process doesn't exist.");
				}
			}
			catch(Exception e)
			{
				throw new TzException("an error occurred when try to validate the spcified batch server[" + schdProcessParameters.getBatchServer() + "].");
			}
		}
	}
	
	final private Integer getNewProcessInstanceId(String orgId) throws TzException
	{
		Integer tmpProcessInstanceId = 0;
		
		//开始事务
		TransactionStatus status = getTransaction();
		
		try
		{
			//先判断是否存在指定机构对应的种子
			TzString existsFlag = new TzString();
			String sqlText = "SELECT 'X' EXISTS_FLAG FROM TZ_JCSL_IDZZ_T WHERE TZ_JG_ID=?";
			sqlExec(sqlText,new SqlParams(orgId),existsFlag);
			if("X".equals(existsFlag.getValue()) == false)
			{
				//不存在则插入一条种子记录
				try
				{
					this.sqlExec("INSERT INTO TZ_JCSL_IDZZ_T(TZ_JG_ID,TZ_JCSL_IDSEED) VALUES(?,?)",new SqlParams(orgId,100000000));
				}
				catch(Exception e)
				{
					//如果插入冲突，则啥都不做
				}
			}
			
			//先锁定当前机构对应的进程实例种子记录
			sqlExec("UPDATE TZ_JCSL_IDZZ_T SET TZ_JCSL_IDSEED=TZ_JCSL_IDSEED + 1 WHERE TZ_JG_ID=?",new SqlParams(orgId));
			
			//获取当前机构的进程实例种子号
			TzInt tmpId = new TzInt();
			sqlText = "SELECT TZ_JCSL_IDSEED FROM TZ_JCSL_IDZZ_T WHERE TZ_JG_ID=?";
			sqlExec(sqlText,new SqlParams(orgId),tmpId);
			tmpProcessInstanceId = tmpId.getValue();
			
			commit(status);
		}
		catch(Exception e)
		{
			rollback(status);
			throw new TzException("can't get a new process instance ID for the specified process job.\n" + e.toString());
		}
		
		return tmpProcessInstanceId;
	}
	
	//发起一个调度Job进程的方法，参数schdDateTime指定了Job进程的调度执行时间
	final public boolean schedule(EngineParameters schdProcessParameters) throws TzException
	{
		try
		{
			//必须是使用TZGDObject的createEngineProcess方法创建的对象才可以执行schedule方法
			if(this.isApplicationContextNull() == true || this.isTZGDObjectNull() == true)
			{
				throw new TzException("the job process instance[" + this.getClass().getName() + "] is not valid and please use the method createEngineProcess of the class com.tranzvision.gd.util.sql.TZGDObject to create the scheduler object.");
			}
			
			//先检查Job进程调度参数是否满足要求
			check(schdProcessParameters);
			
			//创建Job进程实例对象对应的记录
			TzRecord tmpProcessInstanceRecord = createRecord("TZ_JC_SHLI_T");
			
			//获取当前请求用户账号
			String tmpLoginUserAccount = schdProcessParameters.getLoginUserAccount();
			
			//根据循环表达式参数获取计划执行时间
			Date tmpPlanExecuteDateTime = schdProcessParameters.getPlanExcuteDateTime();
			String tmpCronExpression = "";
			if(schdProcessParameters.getCycleExpression().equals("") == false)
			{
				//读取循环表达式
				String sqlText = "SELECT TZ_XH_QZBDS FROM TZ_XUNH_DEFN_T WHERE TZ_JG_ID=? AND TZ_XH_MC=?";
				TzString tzTmpCronExpression = new TzString();
				sqlExec(sqlText, new SqlParams(organizationID,schdProcessParameters.getCycleExpression()), tzTmpCronExpression);
				
				tmpCronExpression = tzTmpCronExpression.getValue();
				tmpCronExpression = (tmpCronExpression == null || tmpCronExpression.trim().equals("") == true) ? schdProcessParameters.getCycleExpression() : tmpCronExpression.trim();
				
				//如果没有为Job进程指定计划执行时间，则根据循环表达式计算出当前时间以后的第一个可执行时间作为计划执行时间
				if(tmpPlanExecuteDateTime == null)
				{
					CronTriggerImpl tmpCronTrigger = new CronTriggerImpl();
					tmpCronTrigger.setCronExpression(tmpCronExpression.trim());
					
					tmpPlanExecuteDateTime = tmpCronTrigger.getFireTimeAfter(new Date());
				}
			}
			else
			{
				//既没有为Job进程指定计划执行时间，又没有指定据循环表达式，则以当前系统时间作为计划执行时间
				if(tmpPlanExecuteDateTime == null)
				{
					tmpPlanExecuteDateTime = new Date();
				}
			}
			
			//生成Job进程实例ID
			Integer tmpProcessInstanceID = getNewProcessInstanceId(organizationID);
			
			//为Job进程实例对象的各个属性赋值
			tmpProcessInstanceRecord.setColumnValue("TZ_JG_ID", getOrganziationID());
			tmpProcessInstanceRecord.setColumnValue("TZ_JCSL_ID", tmpProcessInstanceID);
			tmpProcessInstanceRecord.setColumnValue("TZ_YUNX_KZID", schdProcessParameters.getRunControlId());
			tmpProcessInstanceRecord.setColumnValue("TZ_DLZH_ID", tmpLoginUserAccount);
			tmpProcessInstanceRecord.setColumnValue("TZ_JC_MC", getProcessName());
			tmpProcessInstanceRecord.setColumnValue("TZ_XH_QZBDS", schdProcessParameters.getCycleExpression());
			tmpProcessInstanceRecord.setColumnValue("TZ_JCFWQ_MC", schdProcessParameters.getBatchServer());
			tmpProcessInstanceRecord.setColumnValue("TZ_JOB_YXZT", "QUENED");
			tmpProcessInstanceRecord.setColumnValue("TZ_QQCJ_DTTM", new Date());
			tmpProcessInstanceRecord.setColumnValue("TZ_JHZX_DTTM", tmpPlanExecuteDateTime);
			
			//将任务提交到数据库中
			tmpProcessInstanceRecord.insert();
			
			processInstanceID = tmpProcessInstanceID;
		}
		catch(TzException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			return false;
		}
		
		return true;
	}
	
	//返回当前进程实例ID，当调度成功时返回进程实例ID，如果调度失败返回的进程实例ID为0
	public Integer getProcessInstanceID()
	{
		return processInstanceID;
	}
	
	//该方法应该由子类继承覆盖，该方法主要用于子类编写需要完成的业务逻辑的代码
	public void OnExecute() throws Exception
	{
		;
	}
}