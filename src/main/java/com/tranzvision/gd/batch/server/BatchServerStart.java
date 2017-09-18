/**
 * 
 */
package com.tranzvision.gd.batch.server;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.DateBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * @author LiGang
 * 2015/11/24
 */

public class BatchServerStart implements ApplicationListener<ContextRefreshedEvent>
{
	@Autowired
	private ApplicationContext acx;
	private static final String APPLICATION_CONTEXT_KEY = "applicationContextKey";
	
	@Autowired
	private TZGDObject tzGDObject;
	private static final String TZGDOBJECT_BEAN_KEY = "tzGDObjectBeanKey";
	
	private static final Logger logger = Logger.getLogger("BatchServerStart");
	
	private static SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();  
    private static String JOB_GROUP_NAME = "BATCH_SERVER_MANAGER_JOBGROUP_NAME";  
    private static String TRIGGER_GROUP_NAME = "BATCH_SERVER_MANAGER_TRIGGERGROUP_NAME";
	
	public BatchServerStart()
	{
		;
	}
	
    /**
	 * 当spring容器初始化完成后，自动启动BatchServer管理进程
	 */
	public void onApplicationEvent(ContextRefreshedEvent event)
    {
        //当spring容器初始化完成后,启动BatchServer管理进程
		if(event.getApplicationContext().getParent() == null)
		{
		    startBatchServerMonitor();
		}
    }
	
	/**
	 * 启动BatchServer管理进程的方法
	 */
	private void startBatchServerMonitor()
	{
		if(logger.isInfoEnabled() == true)
		{
			logger.info("begin to schedule the batch server manager...");
		}
		
		//开始调度启动BatchServer管理进程
		try
		{
			//获取调度器
			Scheduler sched = gSchedulerFactory.getScheduler();
			logger.info("success for getting the scheduler about the batch server manager.");
			
			//启动调度器
			sched.start();
			logger.info("success for starting the scheduler about the batch server manager.");
			
			//构建调度任务
			JobDetail jobDetail = newJob(BatchServerManager.class).withIdentity("com.tranzvision.gd.batch.server.BatchServerManager", JOB_GROUP_NAME).build();
			logger.info("success for building the job for scheduling the batch server manager.");
			
			//将当前springs容器的上下文对象传递给调度任务对象
			jobDetail.getJobDataMap().put(APPLICATION_CONTEXT_KEY, acx);
			
			//将当前TZGDObject对应的Bean实例传递给调度任务对象
			jobDetail.getJobDataMap().put(TZGDOBJECT_BEAN_KEY, tzGDObject);
			
			//生成调度时间
			Date startTime = DateBuilder.nextGivenSecondDate(null, 10);
			logger.info("the batch server manager will be started at " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime) + ".");
			
			//生成触发器对象
			SimpleTrigger trigger = (SimpleTrigger) newTrigger().withIdentity("BATCH_SERVER_MANAGER_TRIGGER", TRIGGER_GROUP_NAME).startAt(startTime).build();
			logger.info("success for building the trigger for scheduling the batch server manager.");
			
			//开始调度任务，启动BatchServer管理进程
			Date schedDateTime = sched.scheduleJob(jobDetail, trigger);
			logger.info("success for adding the job to start the batch server manager into the scheduler at " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(schedDateTime) + ".");
		}
		catch(Exception e)
		{
			if(logger.isInfoEnabled() == true)
			{
				logger.error("failed to schedule the batch server manager.\n" + e.toString());
			}
		}
			
		if(logger.isInfoEnabled() == true)
		{
			logger.info("success for scheduling the batch server manager.");
		}
	}
	
	protected void finalize() throws Throwable
	{
		try
		{
			//获取调度器
			Scheduler sched = gSchedulerFactory.getScheduler();
			
			//关闭调度器
			sched.shutdown(true);
			
			if(logger.isInfoEnabled() == true)
			{
				logger.error("the scheduler about the batch server manager has been shut down.");
			}
		}
		catch(Exception e)
		{
			if(logger.isInfoEnabled() == true)
			{
				logger.error("failed to shutdown the scheduler about the batch server manager.\n" + e.toString());
			}
		}
	}
}