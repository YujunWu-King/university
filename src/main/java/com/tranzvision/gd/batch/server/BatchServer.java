/**
 * 
 */
package com.tranzvision.gd.batch.server;

import org.springframework.transaction.TransactionStatus;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.Scheduler;
import org.quartz.SimpleTrigger;
import org.quartz.JobExecutionException;
import org.quartz.DateBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;

import com.tranzvision.gd.util.sql.SqlParams;
import com.tranzvision.gd.util.sql.type.TzRecord;
import com.tranzvision.gd.util.sql.type.TzSQLObject;
import com.tranzvision.gd.util.sql.type.TzField;
import com.tranzvision.gd.util.base.TzException;
import com.tranzvision.gd.util.sql.type.TzString;
import com.tranzvision.gd.batch.engine.base.BaseEngine;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import java.util.Map;
import java.util.Properties;

/**
 * @author LiGang
 * 2015/11/27
 */
public class BatchServer extends BaseJob
{
	//机构ID
	private String organizationID = "";
	private static final String BATCH_SERVER_ORGANIZATION_ID = "BatchServerOrganizationID";
	
	//批处理服务器IP地址
	private String batchServerIP = "";
	private static final String BATCH_SERVER_IP_ADDRESS = "BatchServeIPAddress";
	
	//批处理服务器名称引用别名
	private static final String BATCH_SERVER_NAME = "BatchServeName";
	
	//Job进程实例组织机构ID和Job进程实例ID对应的引用名称
	private static final String ENGINE_JOB_ORGANIZATION_ID = "EngineJobOrganizationID";
	private static final String ENGINE_JOB_PROCESS_INSTANCE_ID = "EngineJobProcessInstanceID";
	
	//批处理服务器任务循环间隔时间
	private int taskScanTime = 10;
	//最大并行任务数
	private int parallelTaskCount = 20;
	//批处理服务器运行状态
	private String batchServerStatus = "";
	//批处理服务器实例ID
	private String bsInstanceID = UUID.randomUUID().toString();
	
	//用于维护Batch Server状态的记录对象
	private TzRecord batchServerCfg = null;
	
	//发生错误的最大次数
	private int maxErrorTimes = 0;
	
	//记录当前
	private Map<String,JobKey> jobList;
	
	//调度器
	private Scheduler sched = null;
	
	public BatchServer()
	{
		jobList = new HashMap<String,JobKey>();
	}
	
	@Override
	final public void execute() throws JobExecutionException
	{
		//先执行父类的“execute”方法
		super.execute();
		
		organizationID = (String)getJobExecutionContext().getJobDetail().getJobDataMap().get(BATCH_SERVER_ORGANIZATION_ID);
		
		batchServerIP = (String)getJobExecutionContext().getJobDetail().getJobDataMap().get(BATCH_SERVER_IP_ADDRESS);
		
		//开始调度Batch Server
		schedule();
	}
	
	/**
	 * 用于调度Batch Server的入口方法
	 */
	final private void schedule()
	{
		if(organizationID.trim().equals("") == true)
		{
			return;
		}
		
		if(getBatchServerName().trim().equals("") == true)
		{
			return;
		}
		
		//设置日志记录器
		String middleFolder = "BatchServer/" + organizationID + "." + getBatchServerName();
		setLogger(organizationID + "." + getBatchServerName(),middleFolder,"batchserver.log",true);
		
		info("begin to start the batch server [" + organizationID + "." + getBatchServerName() + "].");
		if(isApplicationContextNull() == true)
		{
			fatal("the application context object about springs container is null and can't start the batch server [" + organizationID + "." + getBatchServerName() + "].");
			return;
		}
		
		if(isTZGDObjectNull() == true)
		{
			fatal("the TZGDObject object is null and can't start the batch server [" + organizationID + "." + getBatchServerName() + "].");
			return;
		}
		
		try
		{
			//开始启动Batch Server
			start();
			
			//开始运行Batch Server
			run();
		}
		catch(Exception e)
		{
			fatal(e.toString());
		}
	}
	
	/**
	 * 读取批处理服务器配置数据的方法
	 */
	private void readConfiguration() throws TzException
	{
		try
		{
			batchServerCfg = createRecord("TZ_JC_FWQDX_T");
			
			batchServerCfg.selectByKey(new TzField("TZ_JG_ID",organizationID),new TzField("TZ_JCFWQ_MC", getBatchServerName()));
			
			if(organizationID.equals(batchServerCfg.getTzString("TZ_JG_ID").getValue()) == false)
			{
				throw new Exception("can't find the configuration record by the specified organization ID [" + organizationID + "].");
			}
			
			if(getBatchServerName().equals(batchServerCfg.getTzString("TZ_JCFWQ_MC").getValue()) == false)
			{
				throw new Exception("can't find the configuration record by the specified batch server name [" + getBatchServerName() + "].");
			}
			
			if(batchServerIP.equals(batchServerCfg.getTzString("TZ_FWQ_IP").getValue()) == false)
			{
				throw new Exception("can't find the configuration record by the specified IP address [" + batchServerIP + "].");
			}
			
			//读取批处理服务器任务循环间隔时间
			try
			{
				int tmpIntValue1 = (int)batchServerCfg.getTzLong("TZ_RWXH_JG").getValue();
				taskScanTime = tmpIntValue1 <= 0 ? 10 : tmpIntValue1;
			}
			catch(Exception e)
			{
				taskScanTime = 10;
				warn("can't get the task scan cycle interval and the default value(10 seconds) will be used.\n" + e.toString());
			}
			
			//读取批处理服务器能处理的最大并行任务数
			try
			{
				int tmpIntValue2 = (int)batchServerCfg.getTzLong("TZ_ZDBX_RWS").getValue(); 
				parallelTaskCount = tmpIntValue2 <= 0 ? 20 : tmpIntValue2;
			}
			catch(Exception e)
			{
				parallelTaskCount = 20;
				warn("can't get the max number of parallel tasks and the default value(20 tasks) will be used.\n" + e.toString());
			}
			
			//读取批处理服务器的运行状态
			batchServerStatus = batchServerCfg.getTzString("TZ_YXZT").getValue();
			batchServerStatus = batchServerStatus == null ? "" : batchServerStatus;
		}
		catch(Exception e)
		{
			throw new TzException("failed to read the configuration about the batch server [" + organizationID + "." + getBatchServerName() + "].\n" + e.toString());
		}
	}
	
	/**
	 * 启动Batch Server的方法
	 */
	private void start() throws Exception
	{
		//开始事务
		TransactionStatus status = getTransaction();
		
		try
		{
			//锁定当前批处理服务器
			sqlExec("UPDATE TZ_JC_FWQDX_T SET TZ_JG_ID=TZ_JG_ID WHERE TZ_JG_ID=? AND TZ_JCFWQ_MC=?",new SqlParams(organizationID,getBatchServerName()));
			
			//读取配置信息
			readConfiguration();
			
			//设置quartz调度线程池参数
			Properties props = new Properties();
			props.put("org.quartz.scheduler.instanceName", organizationID + "." + getBatchServerName());
			props.put("org.quartz.threadPool.threadCount", new Integer(parallelTaskCount).toString());
			
			//生成调度器
			StdSchedulerFactory factory = new StdSchedulerFactory();
			factory.initialize(props);
			sched = factory.getScheduler();
			
			//启动调度器
			sched.start();
			
			//判断当前批处理服务器是否能被调度
			TzString canStartFlag = new TzString();
			sqlExec(getSQLText("SQL.TZBatchServer.TzCanStartBatchServer"),new SqlParams(organizationID,getBatchServerName(),batchServerIP),canStartFlag);
			if("X".equals(canStartFlag.getValue()) == true)
			{
				//更新Batch Server的运行状态为RUNNING
				updateStatus("RUNNING");
			}
			else
			{
				throw new TzException("the batch server [" + organizationID + "." + getBatchServerName() + "] has been scheduled by other monitors and the current monitor will not schedule it again.");
			}
			
			//提交事务
			commit(status);
		}
		catch(TzException e)
		{
			//回滚事务
			rollback(status);
			throw (Exception)e;
		}
		catch(Exception e)
		{
			//回滚事务
			rollback(status);
			throw new Exception("a fatal error occurred during the batch server [" + organizationID + "." + getBatchServerName() + "] was running.\n" + e.toString());
		}
	}
	
	/**
	 * 更新Batch Server的状态的方法
	 */
	final private void updateStatus(String runStatus) throws TzException
	{
		if("RUNNING".equals(runStatus) == true)
		{
			//更新运行状态为“RUNNING”
			batchServerCfg.setColumnValue("TZ_YXZT","RUNNING");
			//更新心跳时间为当前系统时间
			batchServerCfg.setColumnValue("TZ_ZJXTSJ",new Date());
			//更新当前实例识别ID
			batchServerCfg.setColumnValue("TZ_BS_SLID",bsInstanceID);
			//更新当前Batch Server所运行平台的操作系统类型
			batchServerCfg.setColumnValue("TZ_CZXT_LX",getOSType());
			//更新当前实例对象的状态为“RUNNING”
			batchServerStatus = "RUNNING";
			
			//执行更新记录的操作
			batchServerCfg.update();
		}
		else if("STOPPED".equals(runStatus) == true)
		{
			//更新运行状态为“STOPPED”
			batchServerCfg.setColumnValue("TZ_YXZT","STOPPED");
			//更新心跳时间为当前系统时间
			batchServerCfg.setColumnValue("TZ_ZJXTSJ",new Date());
			//更新当前实例对象的状态为“STOPPED”
			batchServerStatus = "STOPPED";
			
			//执行更新记录的操作
			batchServerCfg.update();
		}
	}
	
	/**
	 * 更新Batch Server心跳时间的方法
	 */
	final private void updateHeartBeatDateTime()
	{
		try
		{
			//更新心跳时间为当前系统时间
			batchServerCfg.setColumnValue("TZ_ZJXTSJ",new Date());
			
			//执行更新记录的操作
			batchServerCfg.update();
		}
		catch(Exception e)
		{
			error("an error occured when try to record the heartbeat time of the batch server[" + organizationID + "." + getBatchServerName() + "].\n" + e.toString());
		}
	}
	
	/**
	 * 检查当前实例运行状态的方法
	 */
	final private boolean checkRunStatus()
	{
		//获取当前数据库实例表中当前实例对应的运行状态和实例识别ID
		TzString runStatus = new TzString("");
		TzString instanceID = new TzString("");
		String sqlText = "SELECT TZ_YXZT,TZ_BS_SLID FROM TZ_JC_FWQDX_T WHERE TZ_JG_ID=? AND TZ_JCFWQ_MC=?";
		
		try
		{
			sqlExec(sqlText, new SqlParams(organizationID,getBatchServerName()),runStatus,instanceID);
		}
		catch(Exception e)
		{
			if(maxErrorTimes <= 100)
			{
				error("an error occured during batch server[" + organizationID + "." + getBatchServerName() + "] running.\n" + e.toString());
				maxErrorTimes ++;
				return true;
			}
			else
			{
				error("there are too many errors occured during batch server[" + organizationID + "." + getBatchServerName() + "] running and it will exit automatically.");
				return false;
			}
		}
		
		//检查当前Batch Server实例在数据库中对应的运行状态是否为“RUNNING”
		if("RUNNING".equals(runStatus.getValue()) == false)
		{
			info("the status of the current batch server [" + organizationID + "." + getBatchServerName() + "] has changed to \"" + runStatus.getValue() + "\" and it will exit automatically.");
			return false;
		}
		
		//检查当前Batch Server实例在数据库中对应的实例识别ID与当前实例对象的实例识别ID是否一致
		if(bsInstanceID.equals(instanceID.getValue()) == false)
		{
			warn("warning: the current batch server [" + organizationID + "." + getBatchServerName() + "]'s instance ID[" + bsInstanceID + "] is inconsistent with the ID in database[" + instanceID.getValue() + "] and it will exit automatically.");
			return false;
		}
		
		return true;
	}
	
	/**
	 * 持续运行Batch Server的入口方法
	 */
	final private void run()
	{
		info("the batch server [" + organizationID + "." + getBatchServerName() + "] is running.");
		
		while(checkRunStatus() == true)
		{
			//扫描需要运行的Jobs
			scanJobs();
			
			//记录心跳时间
			updateHeartBeatDateTime();
			
			//睡眠指定时长后再运行(单位：秒)
			sleep(1000 * taskScanTime);
		}
		
		try
		{
			//更新Batch Server的运行状态为“STOPPED”
			updateStatus("STOPPED");
		}
		catch(Exception e)
		{
			error("an error occurred when tried to update the status of [" + organizationID + "." + getBatchServerName() + "] from \"STOPPING\" to \"STOPPED\".");
		}
		
		info("the batch server [" + organizationID + "." + getBatchServerName() + "] has exited from running status.");
	}
	
	final private void updateObsoleteJobs()
	{
		//将两个小时内都没有心跳的Job进程的状态更新为"DEAD";
		try
		{
			/*张浪注释，2017-07-12，为防止死锁，改用根据主键逐条更新
			String sqlText = getSQLText("SQL.TZBatchServer.TzUpdateDeadJobStatus");
			sqlExec(sqlText,new SqlParams(organizationID));
			*/
			TzSQLObject tmpSQLObject = createSQLObject(getSQLText("SQL.TZBatchServer.TzGetAllDeadJobs"),organizationID);
			
			TzRecord tmpRecord = new TzRecord();
			while(tmpSQLObject.fetch(tmpRecord) == true)
			{
				long jicInsID = 0;
				try{
					jicInsID = tmpRecord.getTzLong("TZ_JCSL_ID").getValue();
					
					String sqlText = getSQLText("SQL.TZBatchServer.TzUpdateDeadJobStatus");
					sqlExec(sqlText,new SqlParams(organizationID,jicInsID));
				}catch(Exception e){
					e.printStackTrace();
					warn("failed to update the status of the dead job processes["+jicInsID+"] from \"STARTED\",\"RUNNING\" or \"STOPPING\" to \"DEAD\".\n" + e.toString());
				}
			}
		}
		catch(Exception e)
		{
			warn("failed to update the status of the dead job processes from \"STARTED\",\"RUNNING\" or \"STOPPING\" to \"DEAD\".\n" + e.toString());
		}
	}
	
	//检查当前Batch Server上运行的Job进程，并将已运行完毕的Job进程从Job进程列表中移除
	final private void updateRunningJobStatus()
	{
		Map<String,JobKey> removedJobList = new HashMap<String,JobKey>();
		
		Iterator<Map.Entry<String,JobKey>> iter = jobList.entrySet().iterator();
		while(iter.hasNext() == true)
		{
			Map.Entry<String,JobKey> entry = iter.next();
			String key = entry.getKey();
			JobKey job = entry.getValue();
			
			try
			{
				if(sched.checkExists(job) == false)
				{
					removedJobList.put(key, job);
				}
			}
			catch(Exception e)
			{
				warn("can't check the status of the job process[" + key + "].\n" + e.toString());
			}
		}
		
		iter = removedJobList.entrySet().iterator();
		while(iter.hasNext() == true)
		{
			jobList.remove(iter.next().getKey());
		}
		removedJobList.clear();
	}
	
	final private JobDetail startJob(String orgId,Integer procInstanceId,String jobClass,String procName)
	{
		try
		{
			//获取Job进程对应的Class类定义
			Class<?> tmpJobClass = Class.forName(jobClass);
			if(BaseEngine.class.isAssignableFrom(tmpJobClass) == false)
			{
				throw new TzException("the class for the job process[" + orgId + "." + procInstanceId + "] must inherit from the class \"com.tranzvision.gd.batch.engine.base.BaseEngine\".");
			}
			
			//构建调度任务
			String jobName = tmpJobClass.getName() + "." + orgId + "." + procInstanceId;
			String grpName = tmpJobClass.getName() + "." + orgId;
			JobDetail jobDetail = newJob(tmpJobClass.asSubclass(com.tranzvision.gd.batch.engine.base.BaseEngine.class)).withIdentity(jobName, grpName).build();
			info("the batch server has create the job task for the job process[" + orgId + "." + procInstanceId + "] successfully.");
			
			//将当前springs容器的上下文对象传递给调度任务对象
			jobDetail.getJobDataMap().put(APPLICATION_CONTEXT_KEY, getApplicationContext());
			info("the batch server has succeeded in setting the context object for the job process[" + orgId + "." + procInstanceId + "].");
			
			//将当前TZGDObject对应的Bean实例传递给调度任务对象
			jobDetail.getJobDataMap().put(TZGDOBJECT_BEAN_KEY, getTZGDObject());
			info("the batch server has succeeded in setting the TZGDObject object for the job process[" + orgId + "." + procInstanceId + "].");
			
			//为Job上下文设置当前组织机构ID
			jobDetail.getJobDataMap().put(ENGINE_JOB_ORGANIZATION_ID, orgId);
			
			//为Job上下文设置当前批处理服务器名称
			jobDetail.getJobDataMap().put(BATCH_SERVER_NAME, getBatchServerName());
			
			//设置Job进程实例对应的实例ID
			jobDetail.getJobDataMap().put(ENGINE_JOB_PROCESS_INSTANCE_ID, procInstanceId);
			
			//生成调度时间
			Date startTime = DateBuilder.nextGivenSecondDate(null, 10);
			info("the batch server has succeeded in the creating scheduling time for the job process[" + orgId + "." + procInstanceId + "].");
			
			//生成触发器对象
			String triggerName = "Trigger." + tmpJobClass.getName() + "." + orgId + "." + getBatchServerName() + "." + procName + "." + procInstanceId;
			String triggerGrpName = "TriggerGroup." + tmpJobClass.getName() + "." + orgId + getBatchServerName() + "." + procName;
			SimpleTrigger trigger = (SimpleTrigger) newTrigger().withIdentity(triggerName,triggerGrpName).startAt(startTime).build();
			info("the batch server has succeeded in creating the trigger for the job process[" + orgId + "." + procInstanceId + "].");
			
			//开始调度任务，启动BatchServer管理进程
			Date schedDateTime = sched.scheduleJob(jobDetail, trigger);
			info("the batch server has succeeded in scheduling the job process[" + orgId + "." + procInstanceId + "] at" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(schedDateTime) + ".");
			
			return jobDetail;
		}
		catch(Exception e)
		{
			error("failed to start the job process[" + orgId + "." + procInstanceId + "].\n" + e.toString());
		}
		
		return null;
	}
	
	/**
	 * 扫描Job作业的的方法
	 */
	final private void scanJobs()
	{
		//将两个小时内都没有心跳的Job进程的状态更新为"DEAD";
		updateObsoleteJobs();
		
		//扫描所有在运行的Job任务的执行状态，并将已完成的Job任务从任务列表中移除
		updateRunningJobStatus();
		
		//读取所有已达到计划执行时间可以开始由当前批处理服务器执行的Job
		if(jobList.size() < parallelTaskCount)
		{
			try
			{
				TzSQLObject tmpSQLObject = createSQLObject(getSQLText("SQL.TZBatchServer.TzGetExecutableJobs"),getBatchServerName(),getBatchServerName(),getBatchServerName(),getOSType(),organizationID);
				
				TzRecord tmpRecord = new TzRecord();
				while(jobList.size() < parallelTaskCount && tmpSQLObject.fetch(tmpRecord) == true)
				{
					String tmpOrgId = "";
					Integer tmpInstanceId = 0;
					String tmpJobClass = "";
					String tmpProcName = "";
					
					try
					{
						tmpOrgId = tmpRecord.getTzString("TZ_JG_ID").getValue();
						tmpInstanceId = tmpRecord.getTzInt("TZ_JCSL_ID").getValue();
						tmpJobClass = tmpRecord.getTzString("TZ_JAVA_CLASS").getValue();
						tmpProcName = tmpRecord.getTzString("TZ_JC_MC").getValue();
						
						if(tmpOrgId != null && tmpOrgId.trim().equals("") == false && tmpInstanceId > 0)
						{
							//开始事务
							TransactionStatus status = getTransaction();
							
							try
							{
								JobDetail tmpJob = null;
								
								info("try to schedule the job process[" + tmpOrgId + "." + tmpInstanceId + "] ...");
								
								//先锁定当前Job进程记录
								String sqlText = "UPDATE TZ_JC_SHLI_T SET TZ_JOB_YXZT=TZ_JOB_YXZT WHERE TZ_JG_ID=? AND TZ_JCSL_ID=?";
								sqlExec(sqlText,new SqlParams(tmpOrgId,tmpInstanceId));
								
								//读取当前Job进程的运行状态
								TzString jobProcessStatus = new TzString();
								sqlText = "SELECT TZ_JOB_YXZT FROM TZ_JC_SHLI_T WHERE TZ_JG_ID=? AND TZ_JCSL_ID=?";
								sqlExec(sqlText,new SqlParams(tmpOrgId,tmpInstanceId),jobProcessStatus);
								if("QUENED".equals(jobProcessStatus.getValue()) == true)
								{
									//如果成功锁定当前Job进程，则此时读取出来的Job进程状态应该为“QUENED”，此时更新Job进程状态为“”，同时更新心跳时间
									sqlText = "UPDATE TZ_JC_SHLI_T SET TZ_JOB_YXZT=?,TZ_ZJXTSJ=NOW() WHERE TZ_JG_ID=? AND TZ_JCSL_ID=?";
									sqlExec(sqlText,new SqlParams("SCHEDULED",tmpOrgId,tmpInstanceId));
									
									info("succeeded in scheduling the job process[" + tmpOrgId + "." + tmpInstanceId + "].");
									
									//成功锁定并更新当前Job进程的状态为“SCHEDULED”
									tmpJob = startJob(tmpOrgId.trim(),tmpInstanceId,tmpJobClass.trim(),tmpProcName);
								}
								else
								{
									info("failed to lock the job process[" + tmpOrgId + "." + tmpInstanceId + "] and it might have been scheduled by the other batch server.");
								}
								
								//记录已调度的Job
								if(tmpJob != null)
								{
									jobList.put(tmpJob.getKey().getName(),tmpJob.getKey());
								}
								
								//提交事务
								commit(status);
								
								//随机睡眠0～500毫秒
								sleep((int)(500 * Math.random()));
							}
							catch(Exception e)
							{
								//发生异常，回滚事务
								this.rollback(status);
								//记录日志
								error("an error occurred when tried to schedule the job process[" + tmpOrgId + "." + tmpInstanceId + "].\n" + e.toString());
							}
						}
					}
					catch(Exception e)
					{
						warn("failed to schedule the job process[" + tmpOrgId + "." + tmpInstanceId + "]\n." + e.toString());
					}
				}
			}
			catch(Exception e)
			{
				warn("an error occurred during scheduling job processes.\n" + e.toString());
			}
		}
	}
}
