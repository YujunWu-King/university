package com.tranzvision.gd.TZInterviewArrangementBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.CreateTaskServiceImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.SendSmsOrMalServiceImpl;
import com.tranzvision.gd.TZInterviewArrangementBundle.dao.PsTzMsyyKsTblMapper;
import com.tranzvision.gd.TZInterviewArrangementBundle.model.PsTzMsyyKsTbl;
import com.tranzvision.gd.TZInterviewArrangementBundle.model.PsTzMsyyKsTblKey;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl;
import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzException;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
import com.tranzvision.gd.util.sql.type.TzRecord;

/**
 * 面试预约-前台预约
 * @author 张浪
 * 原PS:TZ_GD_MS_ARR_PKG:TZ_GD_MS_APPOINT_CLS
 */
@Service("com.tranzvision.gd.TZInterviewArrangementBundle.service.impl.TzInterviewAppointmentImpl")
public class TzInterviewAppointmentImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery jdbcTemplate;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private TZGDObject tzGDObject;
	
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	
	@Autowired
	private SiteRepCssServiceImpl siteRepCssServiceImpl;
	
//	@Autowired
//	private MySqlLockService mySqlLockService;
	
	@Autowired
	private GetHardCodePoint getHardCodePoint;
	
	@Autowired
	private CreateTaskServiceImpl createTaskServiceImpl;
	
	@Autowired
	private SendSmsOrMalServiceImpl sendSmsOrMalServiceImpl;
	
	@Autowired
	private PsTzMsyyKsTblMapper psTzMsyyKsTblMapper;
	
	@Autowired
	private TzInterviewAppointmentMobileImpl tzInterviewAppointmentMobileImpl;
	
	//用于同步面试预约过程的信号变量，避免同一个服务内部对数据库资源的竞争
	private static Semaphore appointmentLock = new Semaphore(1,true);
	//用于控制访问量的信号变量，避免考生面试预约过度竞争对服务器造成过大压力
	private static Semaphore appointmentLockCounter = new Semaphore(10,true);
	
	
	@Override
	public String tzGetHtmlContent(String strParams) {
		String interviewAppointHtml = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		//String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		try {
			jacksonUtil.json2Map(strParams);
			
			String strSiteId = "";
			if (jacksonUtil.containsKey("siteId")) {
				strSiteId = jacksonUtil.getString("siteId");
			}

			if (strSiteId == null || "".equals(strSiteId)) {
				strSiteId = request.getParameter("siteId");
			}

			// 根据siteid得到机构id;
			String str_jg_id = "";
			// language;
			String language = "";
			String strCssDir = "";
			String siteSQL = "select TZ_JG_ID,TZ_SKIN_STOR,TZ_SITE_LANG from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
			Map<String, Object> siteMap = jdbcTemplate.queryForMap(siteSQL, new Object[] { strSiteId });
			if (siteMap != null) {
				str_jg_id = (String)siteMap.get("TZ_JG_ID");
				String skinstor = (String)siteMap.get("TZ_SKIN_STOR");
				language = (String) siteMap.get("TZ_SITE_LANG");
				String websitePath = getSysHardCodeVal.getWebsiteCssPath();
				
				String strRandom = String.valueOf(10*Math.random());
				if("".equals(skinstor) || skinstor == null){
					strCssDir = request.getContextPath() + websitePath + "/" + str_jg_id.toLowerCase() + "/" + strSiteId + "/"+ "style_" + str_jg_id.toLowerCase() + ".css?v=" + strRandom ;
				}else{
					strCssDir = request.getContextPath() + websitePath + "/" + str_jg_id.toLowerCase() + "/" + strSiteId + "/" + skinstor + "/"+ "style_" + str_jg_id.toLowerCase() + ".css?v=" + strRandom;
				}
			}

			if (language == null || "".equals(language)) {
				language = "ZHS";
			}
			
			// 通用链接;
			String ZSGL_URL = request.getContextPath() + "/dispatcher";
			
			/*
			String noAppointmentText = "Interview appointment information unavailable at this time. If and when you are short-listed, we will notify you via email and you can make an appointment here.";
			
			String sql = tzGDObject.getSQLText("SQL.TZInterviewAppointmentBundle.TzGdMsAppoPlanCount");
			int count = jdbcTemplate.queryForObject(sql, new Object[] { oprid }, "int");
			
			String contentStr = "";
			if(count==0){
				contentStr = noAppointmentText;
			}else{
				contentStr = tzGDObject.getHTMLText("HTML.TZInterviewAppointmentBundle.TZ_GD_MS_APPOINT_TABLE_HTML");
			}
			*/
			interviewAppointHtml = tzGDObject.getHTMLText("HTML.TZInterviewAppointmentBundle.TZ_GD_MS_APPOINT_MAIN_HTML",  strCssDir, "面试预约", ZSGL_URL,str_jg_id, strSiteId,request.getContextPath());
			interviewAppointHtml = siteRepCssServiceImpl.repTitle(interviewAppointHtml, strSiteId);
			interviewAppointHtml=siteRepCssServiceImpl.repCss(interviewAppointHtml, strSiteId);
			
			interviewAppointHtml = siteRepCssServiceImpl.repSiteid(interviewAppointHtml, strSiteId);
			interviewAppointHtml = siteRepCssServiceImpl.repJgid(interviewAppointHtml, str_jg_id);
			interviewAppointHtml = siteRepCssServiceImpl.repLang(interviewAppointHtml, language);
			
		} catch (Exception e) {
			e.printStackTrace();
			return "无法获取相关数据";
		}
		return interviewAppointHtml;
	}
	
	
	@Override
	public String tzOther(String strType, String strParams, String[] errorMsg) {
		String strRet = "";
		try {
			switch (strType) {
				case "tzGetAppointmentHtml":
					//保存批次面试预约安排 
					strRet = this.tzGetAppointmentHtml(strParams,errorMsg);
					break;
				case "tzConfirmAppointment":
					strRet = this.tzConfirmAppointment(strParams,errorMsg);
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。" + e.getMessage();
		}
		return strRet;
	}
	
	
	
	private String tzGetAppointmentHtml(String strParams, String[] errorMsg){
		String strRet = "";
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("appoHtml", "");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		try {
			jacksonUtil.json2Map(strParams);
			
			//String timeZone = jacksonUtil.getString("timeZone");
			Date currDate = new Date();//当前时间
			
			String msPlanThHtml = tzGDObject.getHTMLText("HTML.TZInterviewAppointmentBundle.TZ_GD_MS_APPOINT_TH_HTML");
			String msPlanTrHtml = "";
			msPlanTrHtml = msPlanTrHtml+msPlanThHtml;
			
			
			String tmFormat = getSysHardCodeVal.getTimeHMFormat();
			String dttmFormat = getSysHardCodeVal.getDateTimeHMFormat();
			
			SimpleDateFormat tmSimpleDateFormat = new SimpleDateFormat(tmFormat);
			SimpleDateFormat dttmSimpleDateFormat = new SimpleDateFormat(dttmFormat);
			
			String sql = tzGDObject.getSQLText("SQL.TZInterviewAppointmentBundle.TzGdMsAppointPlan");
			List<Map<String,Object>> msPlanList = jdbcTemplate.queryForList(sql, new Object[]{oprid});
			
			String tmpClassID = "",tmpBatchID = "";
			//面试批次设置
			String showFront = "";
			String timeValid = "N";
			int planCount = 0;
			String msExplainInfo = "";

			for(Map<String,Object> msPlanMap : msPlanList){
				String classID = msPlanMap.get("TZ_CLASS_ID") == null? "" : String.valueOf(msPlanMap.get("TZ_CLASS_ID"));
				String batchID = msPlanMap.get("TZ_BATCH_ID") == null? "" : String.valueOf(msPlanMap.get("TZ_BATCH_ID"));
				String msPlanSeq = msPlanMap.get("TZ_MS_PLAN_SEQ") == null? "" : String.valueOf(msPlanMap.get("TZ_MS_PLAN_SEQ"));
				int maxPerson = msPlanMap.get("TZ_MSYY_COUNT") == null? 0 : Integer.valueOf(String.valueOf(msPlanMap.get("TZ_MSYY_COUNT")));
				
				String msDate = msPlanMap.get("TZ_MS_DATE") == null? "" : String.valueOf(msPlanMap.get("TZ_MS_DATE"));
				String msStartTime = msPlanMap.get("TZ_START_TM") == null? "" : String.valueOf(msPlanMap.get("TZ_START_TM"));
				//String msEndTime = msPlanMap.get("TZ_END_TM") == null? "" : String.valueOf(msPlanMap.get("TZ_END_TM"));
				
				String location = msPlanMap.get("TZ_MS_LOCATION") == null? "" : String.valueOf(msPlanMap.get("TZ_MS_LOCATION"));
				String descr = msPlanMap.get("TZ_MS_ARR_DEMO") == null? "" : String.valueOf(msPlanMap.get("TZ_MS_ARR_DEMO"));
				
				//面试批次设置参数
				if(!tmpClassID.equals(classID) || !tmpBatchID.equals(batchID)){
					
					String setSql = tzGDObject.getSQLText("SQL.TZInterviewAppointmentBundle.TzGdMsAppointSet");
					Map<String,Object> msyySetMap = jdbcTemplate.queryForMap(setSql, new Object[]{ currDate, currDate, currDate, classID, batchID });
					
					if(msyySetMap != null){
						//时间限制，Y-预约时间内，B-预约时间前，A-预约时间后，N-为设置预约
						timeValid = msyySetMap.get("TZ_TIME_VALID").toString();
						showFront = msyySetMap.get("TZ_SHOW_FRONT") == null ? "N" : msyySetMap.get("TZ_SHOW_FRONT").toString();
						msExplainInfo = msyySetMap.get("TZ_DESCR") == null ? "" : msyySetMap.get("TZ_DESCR").toString();
					}
				}
				
				//预约时间结束后，不再前台显示
				if(("A".equals(timeValid) && !"Y".equals(showFront)) || "N".equals(timeValid)){
					continue;
				}
				
				String disabledClass = ""; /*按钮是否可用*/
			    String legendClass = ""; /*颜色标识*/
			    String buttonLabel = "预约";
			    
			    /*
			    String className;//班级名称
			    sql = "SELECT TZ_CLASS_NAME FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID=?";
			    className = jdbcTemplate.queryForObject(sql, new Object[]{ classID }, "String");
			    */
			    
			    String isAppoFlag; /*当前批次下是否已经预约*/
			    sql = "SELECT 'Y' FROM PS_TZ_MSYY_KS_TBL WHERE TZ_CLASS_ID=? AND TZ_BATCH_ID=? AND OPRID=?";
			    isAppoFlag = jdbcTemplate.queryForObject(sql, new Object[]{ classID, batchID, oprid }, "String");
			    
			    int appoCount;//已预约人数
			    sql = "SELECT COUNT(1) FROM PS_TZ_MSYY_KS_TBL WHERE TZ_CLASS_ID=? AND TZ_BATCH_ID=? AND TZ_MS_PLAN_SEQ=?";
			    appoCount = jdbcTemplate.queryForObject(sql, new Object[]{ classID, batchID, msPlanSeq }, "int");
			    
			    
			    Date startDatetime = dttmSimpleDateFormat.parse(msDate+" "+msStartTime);
			    if(currDate.after(startDatetime) || currDate.equals(startDatetime)){
			    	disabledClass = "btn-disabled not-allowed_icon";
			    }
		    	
			    if("Y".equals(isAppoFlag)){
			    	disabledClass = "btn-disabled not-allowed_icon";
			    	
			    	if(appoCount>=maxPerson){
				    	legendClass = "full";
				    	disabledClass = "btn-disabled full_icon";
				    }else{
				    	legendClass = "order_p";
				    }
			    	
			    	sql = "SELECT 'Y' FROM PS_TZ_MSYY_KS_TBL WHERE TZ_CLASS_ID=? AND TZ_BATCH_ID=? AND TZ_MS_PLAN_SEQ=? AND OPRID=?";
			    	String inCurrPlan = jdbcTemplate.queryForObject(sql, new Object[]{ classID, batchID, msPlanSeq, oprid }, "String");
			    	//当前学生已预约当前预约计划
			    	if("Y".equals(inCurrPlan)){
			    		legendClass = "my_order";
			    		disabledClass = "admit_icon";
			    		buttonLabel = "已预约/撤销预约";
			    	}
			    }else{
			    	//当前学生尚未预约
			    	if(appoCount>=maxPerson){
				    	legendClass = "full";
				    	disabledClass = "btn-disabled full_icon";
				    }else{
				    	legendClass = "order_p";
				    }
			    }
		    	
			    
			    if("B".equals(timeValid)){
			    	disabledClass = "btn-disabled not-allowed_icon";
			    	buttonLabel = "预约未开始";
			    }else if("A".equals(timeValid)){
			    	disabledClass = "btn-disabled not-allowed_icon";
			    	buttonLabel = "预约已结束";
			    }

			    
			    tmpClassID = classID;
			    tmpBatchID = batchID;
			    
			    planCount++;
			    msStartTime = tmSimpleDateFormat.format(startDatetime);
			    msPlanTrHtml = msPlanTrHtml + tzGDObject.getHTMLText("HTML.TZInterviewAppointmentBundle.TZ_GD_MS_APPOINT_TR_HTML",msDate,msStartTime,location,descr,legendClass,String.valueOf(appoCount),String.valueOf(maxPerson),classID, batchID, msPlanSeq,disabledClass,buttonLabel);
			}
			
			//没有面试预约显示
			String noAppointmentText = "您暂时没有可预约的面试，如果有面试预约，我们将会邮件通知您。";
			try{
				noAppointmentText = getHardCodePoint.getHardCodePointVal("TZ_NO_INTERVIEW_DESCR");
			}catch(Exception e2){
				e2.printStackTrace();
			}
			
			if(planCount == 0){
				msPlanTrHtml = tzGDObject.getHTMLText("HTML.TZInterviewAppointmentBundle.TZ_GD_MS_APPOINT_DESCR_HTML",noAppointmentText);
			}else{
				msPlanTrHtml = tzGDObject.getHTMLText("HTML.TZInterviewAppointmentBundle.TZ_GD_MS_APPOINT_TABLE_HTML",msPlanTrHtml,msExplainInfo);
			}
			
			rtnMap.replace("appoHtml", msPlanTrHtml);
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。"+e.getMessage();
		}
		strRet = jacksonUtil.Map2json(rtnMap);
		return strRet;
	}
	
	
	/**
	 * 面试预约确认
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	private String tzConfirmAppointment(String strParams, String[] errorMsg){
		String strRet = "";
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("appoHtml", "");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		String Type = request.getParameter("type");
		try {
			jacksonUtil.json2Map(strParams);
			
			String classId = jacksonUtil.getString("classId");
			String pcId = jacksonUtil.getString("pcId");
			String planId = jacksonUtil.getString("planId");
			
			String sql = "SELECT 'Y' AS TZ_IN_FORM,TZ_APP_INS_ID FROM PS_TZ_FORM_WRK_T WHERE TZ_CLASS_ID=? AND OPRID=?";
			Map<String,Object> formWrkMap = jdbcTemplate.queryForMap(sql, new Object[]{ classId, oprid });
			String isInClass = String.valueOf(formWrkMap.get("TZ_IN_FORM"));
			String appInsId = String.valueOf(formWrkMap.get("TZ_APP_INS_ID"));
			
			if("Y".equals(isInClass)){
				sql = "SELECT 'Y' FROM PS_TZ_MSPS_KSH_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_APP_INS_ID=?";
				String isInAppoPlan = jdbcTemplate.queryForObject(sql, new Object[]{classId,pcId,appInsId}, "String");
				
				if("Y".equals(isInAppoPlan)){
					PsTzMsyyKsTblKey psTzMsyyKsTblKey = new PsTzMsyyKsTblKey();
					psTzMsyyKsTblKey.setTzClassId(classId);
					psTzMsyyKsTblKey.setTzBatchId(pcId);
					psTzMsyyKsTblKey.setTzMsPlanSeq(planId);
					psTzMsyyKsTblKey.setOprid(oprid);
					
					PsTzMsyyKsTbl psTzMsyyKsTbl = psTzMsyyKsTblMapper.selectByPrimaryKey(psTzMsyyKsTblKey);
					if(psTzMsyyKsTbl != null){
						int del = psTzMsyyKsTblMapper.deleteByPrimaryKey(psTzMsyyKsTblKey);
						if(del > 0){
							errorMsg[0] = "0";
							errorMsg[1] = "撤销预约成功";
							
							//撤销成功后发送站内信
							try{
								sql = "SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=?";
								String name = jdbcTemplate.queryForObject(sql, new Object[]{ oprid }, "String");
								//面试预约成功站内信模板
								String znxModel = getHardCodePoint.getHardCodePointVal("TZ_MSYY_CX_ZNX_TMP");
								//当前机构
								String jgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
								
								//创建邮件任务实例
								String taskId = createTaskServiceImpl.createTaskIns(jgid, znxModel, "ZNX", "A");
								// 创建邮件发送听众
								String crtAudi = createTaskServiceImpl.createAudience(taskId,jgid,"面试预约成功站内信通知", "JSRW");
								//添加听众成员
								boolean bl = createTaskServiceImpl.addAudCy(crtAudi, name, "", "", "", "", "", pcId, oprid, classId, planId, "");
								if(bl){
									sendSmsOrMalServiceImpl.send(taskId, "");
								}
							}catch(NullPointerException nullEx){
								//没有配置邮件模板
								nullEx.printStackTrace();
							}
						}else{
							errorMsg[0] = "1";
							errorMsg[1] = "撤销预约失败";
						}
					}else{
						String otherSql = "select 'Y' from PS_TZ_MSYY_KS_TBL where TZ_CLASS_ID=? and TZ_BATCH_ID=? and OPRID=? limit 1";
						String existsOther = jdbcTemplate.queryForObject(otherSql, new Object[]{ classId, pcId, oprid }, "String");
						if("Y".equals(existsOther)){
							errorMsg[0] = "1";
							errorMsg[1] = "预约失败,你已预约其他报到时间的面试";
						}
						else
						{
							try
							{
								//同一个应用服务内只允许10个考生同时进入面试预约排队，否则报系统忙，请稍候再试。
								if(appointmentLockCounter.tryAcquire(500,TimeUnit.MILLISECONDS) == false)
								{
									throw new Exception("系统忙，请稍候再试。");
								}
							
								//线程间同步，防止同一个应用服务内的预约竞争，减少数据库服务器加锁的压力
								appointmentLock.acquire();
								
								boolean isLocked = false;
								try
								{
									//对指定记录进行加锁
									//mySqlLockService.lockRow(jdbcTemplate, "TZ_MSYY_KS_TBL");锁表无效
									
									//利用主键冲突异常来控制同一时刻只能有一个人来预约某个时间段
									try
									{
										TzRecord lockRecord = tzGDObject.createRecord("PS_TZ_MSYY_LOCK_TBL");
										lockRecord.setColumnValue("TZ_CLASS_ID", classId);
										lockRecord.setColumnValue("TZ_BATCH_ID", pcId);
										lockRecord.setColumnValue("TZ_MS_PLAN_SEQ", planId);
										lockRecord.setColumnValue("OPRID", oprid);
										
										if(lockRecord.insert() == false){
											throw new TzException("系统忙，请稍候再试。");
										}else{
											isLocked = true;
										}
									}
									catch(Exception e)
									{
										 throw new TzException("系统忙，请稍候再试。");
									}
									
									
									String numSql = tzGDObject.getSQLText("SQL.TZInterviewAppointmentBundle.TzGdMsAppointPersonNumbers");
									Map<String,Object> numMap = jdbcTemplate.queryForMap(numSql, new Object[]{ classId, pcId, planId });
									if(numMap != null){
										int msYyLimit = Integer.parseInt(numMap.get("TZ_MSYY_COUNT").toString());
										int msYyCount = Integer.parseInt(numMap.get("TZ_YY_COUNT").toString());
										
										if(msYyLimit > msYyCount){
											psTzMsyyKsTbl = new PsTzMsyyKsTbl();
											psTzMsyyKsTbl.setTzClassId(classId);
											psTzMsyyKsTbl.setTzBatchId(pcId);
											psTzMsyyKsTbl.setTzMsPlanSeq(planId);
											psTzMsyyKsTbl.setOprid(oprid);
											psTzMsyyKsTbl.setRowAddedOprid(oprid);
											psTzMsyyKsTbl.setRowAddedDttm(new Date());
											psTzMsyyKsTbl.setRowLastmantOprid(oprid);
											psTzMsyyKsTbl.setRowLastmantDttm(new Date());
											int rtn = psTzMsyyKsTblMapper.insert(psTzMsyyKsTbl);
											if(rtn > 0){
												errorMsg[0] = "0";
												errorMsg[1] = "预约成功";
											}else{
												errorMsg[0] = "1";
												errorMsg[1] = "预约失败";
											}
										}else{
											errorMsg[0] = "1";
											errorMsg[1] = "预约失败，该报到时间预约人数已满，请预约其他报到时间";
										}
									}
									//对指定记录进行解锁
									//mySqlLockService.unlockRow(jdbcTemplate);
								}
								catch(Exception ee)
								{
									ee.printStackTrace();
									errorMsg[0] = "1";
									errorMsg[1] = "预约失败，"+ee.getMessage();
									//回滚
									//mySqlLockService.rollback(jdbcTemplate);
								}
								finally
								{
									if(isLocked){
										//预约完成后删除插入PS_TZ_MSYY_LOCK_TBL中的数据
										jdbcTemplate.update("delete from PS_TZ_MSYY_LOCK_TBL where TZ_CLASS_ID=? and TZ_BATCH_ID=? and TZ_MS_PLAN_SEQ=?"
												, new Object[]{ classId, pcId, planId });
									}
									//释放信号量
									appointmentLock.release();
									appointmentLockCounter.release();
								}
							}
							catch(Exception e)
							{
								e.printStackTrace();
								errorMsg[0] = "1";
								errorMsg[1] = "系统忙，请稍候再试。";
							}
						}
						
						//面试预约成功，发送成功通知站内信
						if("0".equals(errorMsg[0])){
							//预约成功后发送站内信
							try{
								sql = "SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=?";
								String name = jdbcTemplate.queryForObject(sql, new Object[]{ oprid }, "String");
								//面试预约成功站内信模板
								String znxModel = getHardCodePoint.getHardCodePointVal("TZ_MSYY_CG_ZNX_TMP");
								//当前机构
								String jgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
								
								//创建邮件任务实例
								String taskId = createTaskServiceImpl.createTaskIns(jgid, znxModel, "ZNX", "A");
								// 创建邮件发送听众
								String crtAudi = createTaskServiceImpl.createAudience(taskId,jgid,"面试预约成功站内信通知", "JSRW");
								//添加听众成员
								boolean bl = createTaskServiceImpl.addAudCy(crtAudi, name, "", "", "", "", "", pcId, oprid, classId, planId, "");
								if(bl){
									sendSmsOrMalServiceImpl.send(taskId, "");
								}
							}catch(NullPointerException nullEx){
								//没有配置邮件模板
								nullEx.printStackTrace();
							}
						}
					}
					
					if("M".equals(Type)){
						Map<String,Object> appoMap = tzInterviewAppointmentMobileImpl.tzGetAppointmentHtml();
						//在线预约list
						String appoHtml = appoMap.get("appoHtml").toString();
						rtnMap.replace("appoHtml", appoHtml);
					}
				}else{
					errorMsg[0] = "1";
					errorMsg[1] = "抱歉，您尚未安排本批次面试，预约失败！";
				}
			}else{
				errorMsg[0] = "1";
				errorMsg[1] = "抱歉，您没有在当前班级中报名，不能预约！";
			}
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。"+e.getMessage();
		}
		strRet = jacksonUtil.Map2json(rtnMap);
		return strRet;
	}
	
	
	
	/**
	 * 面试通知邮件获取面试日期
	 * @param audId
	 * @param audCyId
	 * @return
	 */
	public String getEmlInterviewDate(String[] paramters){
		String strRet = "";
		try{
			String audId = paramters[0];
			String audCyId = paramters[1];
			
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			SqlQuery sqlQuery = (SqlQuery) getSpringBeanUtil.getSpringBeanByID("sqlQuery");
			GetSysHardCodeVal getSysHCVal = (GetSysHardCodeVal) getSpringBeanUtil.getSpringBeanByID("getSysHardCodeVal");
			
			String sql = "SELECT TZ_XSXS_ID,TZ_WEIXIN,TZ_HUOD_ID FROM PS_TZ_AUDCYUAN_T WHERE TZ_AUDIENCE_ID=? AND TZ_AUDCY_ID=?";
			Map<String,Object> audMap = sqlQuery.queryForMap(sql, new Object[]{ audId, audCyId });
			
			String classID = String.valueOf(audMap.get("TZ_XSXS_ID"));
			String batchID = String.valueOf(audMap.get("TZ_WEIXIN"));
			String msPlanSeq = String.valueOf(audMap.get("TZ_HUOD_ID"));

			sql = "SELECT TZ_MS_DATE,TZ_START_TM,TZ_END_TM FROM PS_TZ_MSSJ_ARR_TBL WHERE TZ_CLASS_ID=? AND TZ_BATCH_ID=? AND TZ_MS_PLAN_SEQ=?";
			Map<String,Object> msDateMap = sqlQuery.queryForMap(sql, new Object[]{ classID, batchID, msPlanSeq });
			
			Date msDate = (Date) msDateMap.get("TZ_MS_DATE");
			Date msStartTime = (Date) msDateMap.get("TZ_START_TM");
			//String msEndTime = String.valueOf(msDateMap.get("TZ_END_TM"));
			
			String dtFormat = getSysHCVal.getDateFormat();
			String tmFormat = getSysHCVal.getTimeHMFormat();

			SimpleDateFormat dateSimpleDateFormat = new SimpleDateFormat(dtFormat);
			SimpleDateFormat timeSimpleDateFormat = new SimpleDateFormat(tmFormat);
			
			strRet = dateSimpleDateFormat.format(msDate)+" "+timeSimpleDateFormat.format(msStartTime);
		}catch(Exception e){
			e.printStackTrace();
		}
		return strRet;
	}
	
	
	/**
	 * 面试地点
	 * @param audId
	 * @param audCyId
	 * @return
	 */
	public String getInterviewLocation(String[] paramters){
		String strRet = "";
		try{
			String audId = paramters[0];
			String audCyId = paramters[1];
			
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			SqlQuery sqlQuery = (SqlQuery) getSpringBeanUtil.getSpringBeanByID("sqlQuery");
			
			String sql = "SELECT TZ_XSXS_ID,TZ_WEIXIN,TZ_HUOD_ID FROM PS_TZ_AUDCYUAN_T WHERE TZ_AUDIENCE_ID=? AND TZ_AUDCY_ID=?";
			Map<String,Object> audMap = sqlQuery.queryForMap(sql, new Object[]{ audId, audCyId });
			
			String classID = String.valueOf(audMap.get("TZ_XSXS_ID"));
			String batchID = String.valueOf(audMap.get("TZ_WEIXIN"));
			String msPlanSeq = String.valueOf(audMap.get("TZ_HUOD_ID"));

			sql = "SELECT TZ_MS_LOCATION FROM PS_TZ_MSSJ_ARR_TBL WHERE TZ_CLASS_ID=? AND TZ_BATCH_ID=? AND TZ_MS_PLAN_SEQ=?";
			Map<String,Object> msMap = sqlQuery.queryForMap(sql, new Object[]{ classID, batchID, msPlanSeq });
			
			if(msMap != null){
				strRet = msMap.get("TZ_MS_LOCATION").toString();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return strRet;
	}
}
