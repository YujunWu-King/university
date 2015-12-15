/**
 * 
 */
package com.tranzvision.gd.batch.server;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.apache.log4j.FileAppender;

import java.io.File;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.RollingFileAppender;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.TransactionStatus;

import com.tranzvision.gd.util.sql.SqlParams;
import com.tranzvision.gd.util.sql.TZGDObject;
import com.tranzvision.gd.util.base.TzException;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.sql.type.TzRecord;
import com.tranzvision.gd.util.sql.type.TzSQLData;
import com.tranzvision.gd.util.sql.type.TzSQLObject;

/**
 * @author LiGang
 * 2015/12/01
 */
public class BaseJob implements Job
{
	private ApplicationContext acx;
	public static final String APPLICATION_CONTEXT_KEY = "applicationContextKey";
	
	private TZGDObject tzGDObject;
	public static final String TZGDOBJECT_BEAN_KEY = "tzGDObjectBeanKey";
	
	//批处理服务器名称
	private String batchServerName = "";
	private static final String BATCH_SERVER_NAME = "BatchServeName";
	
	private JobExecutionContext jobContext = null;
	
	//可否允许启动执行当前Job的标志
	private boolean canExecuteJob = false;
	
	//日志记录器属性配置
	private String loggerName = "";
	private String middlePath = "";
	private String logFileName = "";
	private boolean daily = true;
	
	private Logger logger = null;
	
	@Override
	final public void execute(JobExecutionContext context) throws JobExecutionException
	{
		jobContext = context;
		
		//获取当前Springs容器上下文环境对象
		acx = (ApplicationContext)context.getJobDetail().getJobDataMap().get(APPLICATION_CONTEXT_KEY);
		
		//获取执行SQL的实例对象
		tzGDObject = (TZGDObject)context.getJobDetail().getJobDataMap().get(TZGDOBJECT_BEAN_KEY);
		
		//获取批处理服务器名称
		batchServerName = (String)context.getJobDetail().getJobDataMap().get(BATCH_SERVER_NAME);
		batchServerName = batchServerName == null ? "" : batchServerName;
		
		//执行Job指令及内容
		canExecuteJob = true;
		execute();
	}
	
	final public JobExecutionContext getJobExecutionContext()
	{
		return jobContext;
	}
	
	final private void setApplicationContext(ApplicationContext acx)
	{
		this.acx = acx;
	}
	
	final public ApplicationContext getApplicationContext()
	{
		return acx;
	}
	
	final public boolean isApplicationContextNull()
	{
		return acx == null;
	}
	
	final private void setTZGDObject(TZGDObject tzGDObject)
	{
		this.tzGDObject = tzGDObject;
	}
	
	final public TZGDObject getTZGDObject()
	{
		return tzGDObject;
	}
	
	final public boolean isTZGDObjectNull()
	{
		return tzGDObject == null;
	}
	
	final public String getBatchServerName()
	{
		return batchServerName;
	}
	
	final public void setLogger(Logger logger)
	{
		this.logger = logger;
	}
	
	final public Logger getLogger()
	{
		return logger;
	}
	
	final public void info(String message)
	{
		if(logger == null)
		{
			if(createLogger() == false)
			{
				return;
			}
		}
		
		if(logger.isInfoEnabled() == true)
		{
			logger.info(message);
		}
	}
	
	final public void debug(String message)
	{
		if(logger == null)
		{
			if(createLogger() == false)
			{
				return;
			}
		}
		
		if(logger.isDebugEnabled() == true)
		{
			logger.debug(message);
		}
	}
	
	final public void error(String message)
	{
		if(logger == null)
		{
			if(createLogger() == false)
			{
				return;
			}
		}
		
		if(logger.isEnabledFor(Level.ERROR) == true)
		{
			logger.error(message);
		}
	}
	
	final public void fatal(String message)
	{
		if(logger == null)
		{
			if(createLogger() == false)
			{
				return;
			}
		}
		
		if(logger.isEnabledFor(Level.FATAL) == true)
		{
			logger.fatal(message);
		}
	}
	
	final public void warn(String message)
	{
		if(logger == null)
		{
			if(createLogger() == false)
			{
				return;
			}
		}
		
		if(logger.isEnabledFor(Level.WARN) == true)
		{
			logger.warn(message);
		}
	}
	
	final public void commit(TransactionStatus status)
	{
		tzGDObject.commit(status);
	}
	
	final public TzRecord createRecord(String tblName) throws TzException
	{
		return tzGDObject.createRecord(tblName);
	}
	
	final public TzSQLObject createSQLObject(String sqlText,Object...args) throws TzSystemException
	{
		return tzGDObject.createSQLObject(sqlText, args);
	}
	
	final public String getHTMLText(String htmlObjectName,boolean refreshFlag,String...args) throws TzSystemException
	{
		return tzGDObject.getHTMLText(htmlObjectName, refreshFlag, args);
	}
	
	final public String getOSType()
	{
		return tzGDObject.getOSType();
	}
	
	final public String getSQLText(String sqlObjectName) throws TzSystemException
	{
		return tzGDObject.getSQLText(sqlObjectName);
	}
	
	final public String getSQLText(String sqlObjectName,boolean refreshFlag) throws TzSystemException
	{
		return tzGDObject.getSQLText(sqlObjectName, refreshFlag);
	}
	
	final public TransactionStatus getTransaction()
	{
		return tzGDObject.getTransaction();
	}
	
	final public String getWebAppRootPath()
	{
		return tzGDObject.getWebAppRootPath();
	}
	
	final public void rollback(TransactionStatus status)
	{
		tzGDObject.rollback(status);
	}
	
	final private boolean createLogger()
	{
		if("".equals(loggerName) == true || "".equals(middlePath) == true || "".equals(logFileName) == true)
		{
			return false;
		}
		
		Logger tmpLogger = null;
		try
		{
			tmpLogger = Logger.getLogger(loggerName);
			
			if(tmpLogger.getAppender(loggerName) == null)
			{
				String filePathName = getWebAppRootPath();
				
				if(middlePath != null && middlePath.trim().equals("") == false)
				{
					filePathName += File.separator + "WEB-INF" + File.separator + "logs" + File.separator + middlePath.trim() + File.separator + logFileName;
				}
				else
				{
					filePathName += File.separator + "WEB-INF" + File.separator + "logs" + File.separator + logFileName;
				}
				
				DailyRollingFileAppender tmpAppender = (DailyRollingFileAppender)Logger.getLogger("BatchServerStart").getAppender("BatchServerStart");
				
				FileAppender appender = null;
				if(daily == true)
				{
					appender = new DailyRollingFileAppender(tmpAppender.getLayout(),filePathName,tmpAppender.getDatePattern());
				}
				else
				{
					appender = new RollingFileAppender(tmpAppender.getLayout(),filePathName,true);
				}
				
				appender.setName(loggerName);
				appender.setEncoding(tmpAppender.getEncoding());
				appender.setAppend(tmpAppender.getAppend());
				appender.setThreshold(tmpAppender.getThreshold());
				
				tmpLogger.addAppender(appender);
				
				logger = tmpLogger;
			}
		}
		catch(Exception e)
		{
			;
		}
		
		return true;
	}
	
	final public void setLogger(String loggerName,String middlePath,String logFileName,boolean daily)
	{
		if(loggerName == null || loggerName.trim().equals("") == true)
		{
			return;
		}
		
		if(logFileName == null || logFileName.trim().equals("") == true)
		{
			return;
		}
		
		this.loggerName = loggerName.trim();
		this.middlePath = middlePath.trim();
		this.logFileName = logFileName.trim();
		this.daily = daily;
	}
	
	final public void sqlExec(String sqlText,TzRecord rec) throws TzSystemException
	{
		tzGDObject.sqlExec(sqlText, rec);
	}
	
	final public void sqlExec(String sqlText,TzSQLData...args) throws TzSystemException
	{
		tzGDObject.sqlExec(sqlText, args);
	}
	
	final public void sqlExec(String sqlText,SqlParams params,TzRecord rec) throws TzSystemException
	{
		tzGDObject.sqlExec(sqlText, params, rec);
	}
	
	final public void sqlExec(String sqlText,SqlParams params,TzSQLData...args) throws TzSystemException
	{
		tzGDObject.sqlExec(sqlText, params, args);
	}
	
	/**
	 * 让当前实例睡眠指定时间的方法
	 */
	final public void sleep(int sec)
	{
    	try
    	{
    		Thread.sleep(sec);
    	}
    	catch(Exception e)
    	{
    		error("an error occurred when try to sleep " + sec + " seconds.\n" + e.toString());
    	}
	}
	
	//该方法应该被子类覆盖重写
	public void execute() throws JobExecutionException
	{
		if(canExecuteJob == false)
		{
			throw new JobExecutionException("the method \"execute\" of the current job process instance object can not be called from outside.");
		}
	}
}
