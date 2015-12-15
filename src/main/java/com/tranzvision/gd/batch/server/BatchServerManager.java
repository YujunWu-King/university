/**
 * 
 */
package com.tranzvision.gd.batch.server;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.quartz.DateBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;

import com.tranzvision.gd.util.sql.SqlParams;
import com.tranzvision.gd.util.sql.type.TzRecord;
import com.tranzvision.gd.util.sql.type.TzSQLObject;

/**
 * @author LiGang
 * 2015/11/25
 */
public class BatchServerManager extends BaseJob
{
	private boolean exitFlag = false;
	private int heartBeatCount = 0;
	
	//当前服务器IP地址
	Set<String> batchServerIPSet = null;
	
	private static SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();  
    private static String JOB_GROUP_NAME_PREFIX = "BATCH_SERVER_";
    private static String JOB_GROUP_NAME_SURFIX = "_JOBGROUP_NAME";  
    private static String TRIGGER_GROUP_NAME_PREFIX = "BATCH_SERVER_";
    private static String TRIGGER_GROUP_NAME_SURFIX = "_TRIGGERGROUP_NAME";
    
    private static final String BATCH_SERVER_ORGANIZATION_ID = "BatchServerOrganizationID";
    private static final String BATCH_SERVER_NAME = "BatchServeName";
    private static final String BATCH_SERVER_IP_ADDRESS = "BatchServeIPAddress";
	
	public BatchServerManager()
	{
		this.setLogger(Logger.getLogger("BatchServerStart"));
		
		try
		{
			Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
			batchServerIPSet = new HashSet<String>();
	        while(e.hasMoreElements() == true)
	        {
	        	Enumeration<InetAddress> addresses = e.nextElement().getInetAddresses();
	        	while(addresses.hasMoreElements() == true)
	        	{
	        		batchServerIPSet.add(addresses.nextElement().getHostAddress());
	        	}
	        }
		}
		catch(Exception e)
		{
			error("warning: can't get the IP address of the current machine.");
		}
	}
	
	@Override
	public void execute() throws JobExecutionException
	{
		info("the batch server manager has been started at " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ".");
		
		try
		{
			if(isApplicationContextNull() == true)
			{
				fatal("failed to get the application context object about springs container and the batch server manager will exit.");
				return;
			}
			
			if(isTZGDObjectNull() == true)
			{
				fatal("failed to get the bean abount TZGDObject and the batch server manager will exit.");
				return;
			}
			
			info("success for getting the application context object about springs container.");
		}
		catch(Exception e)
		{
			fatal("an fatal error occurred when tried to start the batch server manager.\n" + e.toString());
			return;
		}
		
		//开始运行批处理服务器管理进程
		run();
		
		info("the batch server manager has been shut down at " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ".");
	}
	
	private void beatHeart(int count)
	{
		heartBeatCount ++;
		
    	if(heartBeatCount >= count)
    	{
    		info("the batch server manager is living.");
    		heartBeatCount = 0;
    	}
	}
	
	private void run()
	{
		Scheduler sched = null;
		try
		{
			//获取调度器
			sched = gSchedulerFactory.getScheduler();
			info("the batch server manager succeeded in getting the scheduler object.");
			
			//启动调度器
			sched.start();
			info("the batch server manager succeeded in starting the scheduler.");
		}
		catch(Exception e)
		{
			error("fatal error: the current batch server manager failed to get the scheduler object.\n" + e.toString());
			return;
		}
		
        while(true)
        {
        	//如果收到退出指令，则退出循环
        	if(exitFlag == true)
        	{
        		break;
        	}
        	
        	//开始循环扫描批处理服务器
        	scanBatchServers(sched);
        	
        	//每隔10次循环打印一条心跳记录
        	beatHeart(10);
        	
        	//完成一个循环，休眠60秒
        	sleep(60000);
        }
        
        try
        {
        	//停止调度器
			sched.shutdown();
			info("the batch server manager succeeded in shutting down the scheduler.");
        }
        catch(Exception e)
        {
        	error("error: the current batch server manager failed to shut down the scheduler.\n" + e.toString());
			return;
        }
	}
	
	private void scanBatchServers(Scheduler sched)
	{
    	//开始读取当前服务器可以运行的批处理服务器
		try
		{
			String ipAddress = "";
			
			Iterator<String> iter = batchServerIPSet.iterator();
			while(iter.hasNext() == true)
			{
				ipAddress = iter.next();
				TzSQLObject tmpSQLObject = createSQLObject(getSQLText("SQL.TZBatchServer.TzGetBatchServer"), ipAddress);
				
				TzRecord tmpRecord = new TzRecord();
				while(tmpSQLObject.fetch(tmpRecord) == true)
				{
					String tmpOrgId = tmpRecord.getTzString("TZ_JG_ID").getValue();
					String tmpBSName = tmpRecord.getTzString("TZ_JCFWQ_MC").getValue();
					String tmpBSStatus = tmpRecord.getTzString("TZ_YXZT").getValue();
					
					tmpBSStatus = tmpBSStatus == null ? "" : tmpBSStatus.trim().toUpperCase();
					
					if(tmpBSStatus.equals("STARTING") == true || tmpBSStatus.equals("RUNNING") == true)
					{
						if(tmpOrgId != null && tmpBSName != null && tmpOrgId.trim().equals("") == false && tmpBSName.trim().equals("") == false)
						{
							if(tmpBSStatus.equals("RUNNING") == true)
							{
								warn("the batch server[" + tmpOrgId + "." + tmpBSName + "] has no heartbeat in latest two hours and it will be restarted.");
							}
							
							//构建调度任务
							String jobName = "com.tranzvision.gd.batch.server.BatchServer." + tmpOrgId.trim().toUpperCase() + "." + tmpBSName.trim().toUpperCase();
							String grpName = JOB_GROUP_NAME_PREFIX + tmpOrgId.trim().toUpperCase() + "." + tmpBSName.trim().toUpperCase() + JOB_GROUP_NAME_SURFIX;
							JobDetail jobDetail = newJob(BatchServer.class).withIdentity(jobName,grpName).build();
							info("the batch server manager succeeded in creating the job for the batch server[" + tmpOrgId + "." + tmpBSName + "].");
							
							//将当前springs容器的上下文对象传递给调度任务对象
							jobDetail.getJobDataMap().put(APPLICATION_CONTEXT_KEY, getApplicationContext());
							info("the batch server manager succeeded in setting the context object for the job of the batch server[" + tmpOrgId + "." + tmpBSName + "].");
							
							//将当前TZGDObject对应的Bean实例传递给调度任务对象
							jobDetail.getJobDataMap().put(TZGDOBJECT_BEAN_KEY, getTZGDObject());
							info("the batch server manager succeeded in setting the TZGDObject object for the job of the batch server[" + tmpOrgId + "." + tmpBSName + "].");
							
							//为Job上下文设置当前组织机构ID
							jobDetail.getJobDataMap().put(BATCH_SERVER_ORGANIZATION_ID, tmpOrgId);
							
							//为Job上下文设置当前Batch Server名称
							jobDetail.getJobDataMap().put(BATCH_SERVER_NAME, tmpBSName);
							
							//为Job上下文设置当前Batch Server的IP地址
							jobDetail.getJobDataMap().put(BATCH_SERVER_IP_ADDRESS, ipAddress);
							
							//生成调度时间
							Date startTime = DateBuilder.nextGivenSecondDate(null, 10);
							info("the batch server manager succeeded in the creating scheduling time for the job of the batch server[" + tmpOrgId + "." + tmpBSName + "].");
							
							//生成触发器对象
							String triggerName = "com.tranzvision.gd.batch.server.BatchServer." + tmpOrgId.trim().toUpperCase() + "." + tmpBSName.trim().toLowerCase();
							String triggerGrpName = TRIGGER_GROUP_NAME_PREFIX + tmpOrgId.trim().toUpperCase() + "." + tmpBSName.trim().toLowerCase() + TRIGGER_GROUP_NAME_SURFIX;
							SimpleTrigger trigger = (SimpleTrigger) newTrigger().withIdentity(triggerName,triggerGrpName).startAt(startTime).build();
							info("the batch server manager succeeded in creating the trigger for the job of the batch server[" + tmpOrgId + "." + tmpBSName + "].");
							
							//开始调度任务，启动BatchServer管理进程
							Date schedDateTime = sched.scheduleJob(jobDetail, trigger);
							info("the batch server manager succeeded in scheduling the job of the batch server[" + tmpOrgId + "." + tmpBSName + "] at" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(schedDateTime) + ".");
						}
					}
					else if(tmpBSStatus.equals("STOPPING") == true)
					{
						//如果Batch Server的运行状态为“RUNNING”或者“STOPPING”，则说明当前Batch Server两个小时内没有心跳，可以已死亡，则将状态更新为“STOPPED”
						String tmpSqlText = "UPDATE TZ_JC_FWQDX_T SET TZ_YXZT='STOPPED' WHERE TZ_JG_ID=? AND TZ_JCFWQ_MC=?";
						sqlExec(tmpSqlText,new SqlParams(tmpOrgId,tmpBSName));
						
						warn("the batch server[" + tmpOrgId + "." + tmpBSName + "] has no heartbeat in latest two hours and it's running status is updated from \"" + tmpBSStatus + "\" to \"STOPPED\".");
					}
				}
			}
		}
		catch(Exception e)
		{
			error("an error occurred during the batch server manager scheduling the batch server.\n" + e.toString());
		}
	}
}
