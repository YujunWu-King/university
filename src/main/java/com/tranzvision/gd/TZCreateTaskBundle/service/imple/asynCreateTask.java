package com.tranzvision.gd.TZCreateTaskBundle.service.imple;

import com.tranzvision.gd.TZAuthBundle.service.TzWebsiteLoginService;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 开启线程执行生成任务
 * 
 * @author ZWH 2019年9月11日
 *
 */
public class asynCreateTask extends Thread {

	private SqlQuery sqlQuery;
	
	private TzWebsiteLoginService tzWebsiteLoginService;
	//当前登陆人
	private String currOprid;
	
	private GetHardCodePoint getHardCodePoint;
	//任务模板id
	String tzms_task_template_tid = "";
	//业务流程id
	String tzms_wflinsid = "";
	//流程步骤id
	String stpInsId = "";
	public asynCreateTask(SqlQuery sqlQuery, TzWebsiteLoginService tzWebsiteLoginService, String currOprid, GetHardCodePoint getHardCodePoint, String templateid, String wflinsid, String stpid) {
		
		this.sqlQuery = sqlQuery;
		this.tzWebsiteLoginService = tzWebsiteLoginService;
		this.currOprid = currOprid;
		this.getHardCodePoint = getHardCodePoint;
		this.tzms_task_template_tid = templateid;
		this.tzms_wflinsid = wflinsid;
		this.stpInsId = stpid;
		
	}
	
	public void run() {
		try {
			System.out.println("-----start-----");
			
			TZCreateTaskServiceImpl tzCreatTask = new TZCreateTaskServiceImpl();
			tzCreatTask.createTaskYB(sqlQuery,tzWebsiteLoginService,currOprid,getHardCodePoint,tzms_task_template_tid, tzms_wflinsid, stpInsId);
			
			System.out.println("-----end-----");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
}
