package com.tranzvision.gd.TZInterviewArrangementBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.MySqlLockService;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 面试预约手机版
 * @author zhanglang
 *
 */
@Service("com.tranzvision.gd.TZInterviewArrangementBundle.service.impl.TzInterviewAppointmentMobileImpl")
public class TzInterviewAppointmentMobileImpl extends FrameworkImpl{
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
	private MySqlLockService mySqlLockService;
	
	@Autowired
	private GetHardCodePoint getHardCodePoint;
	
	@Autowired
	private CreateTaskServiceImpl createTaskServiceImpl;
	
	@Autowired
	private SendSmsOrMalServiceImpl sendSmsOrMalServiceImpl;
	
	@Autowired
	private PsTzMsyyKsTblMapper psTzMsyyKsTblMapper;
	
	
	
	@Override
	public String tzGetHtmlContent(String strParams) {
		String interviewAppointHtml = "";
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {
			jacksonUtil.json2Map(strParams);
			String siteId = "";
			if(jacksonUtil.containsKey("siteId")){
				siteId = jacksonUtil.getString("siteId");
			}else{
				siteId = request.getParameter("siteId");
			}
			
			String contextPath = request.getContextPath();
			// 通用链接;
			String ZSGL_URL = contextPath + "/dispatcher";

			Map<String,Object> appoMap = this.tzGetAppointmentHtml();
			//说明信息
			String appoDesc = appoMap.get("appoDesc").toString();
			//在线预约list
			String appoHtml = appoMap.get("appoHtml").toString();
			
			String noneAppoHtml = appoMap.get("noneAppoHtml").toString();
			
			if(!"".equals(noneAppoHtml)){
				interviewAppointHtml = tzGDObject.getHTMLText("HTML.TZInterviewAppointmentBundle.TZ_M_MS_NONE_APPO_MAIN_HTML",contextPath,ZSGL_URL,siteId,"1",noneAppoHtml);
			}else{
				interviewAppointHtml = tzGDObject.getHTMLText("HTML.TZInterviewAppointmentBundle.TZ_M_MS_APPOINT_MAIN_HTML",contextPath,appoDesc,appoHtml,ZSGL_URL,siteId,"1");
			}
			
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
	
	
	
	public Map<String,Object> tzGetAppointmentHtml(){
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("appoDesc", "");
		rtnMap.put("appoHtml", "");
		rtnMap.put("noneAppoHtml", "");

		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		try {
			Date currDate = new Date();//当前时间
			
			String msPlanListHtml = "";
			String contextPath = request.getContextPath();
			
			String dttmFormat = getSysHardCodeVal.getDateTimeHMFormat();
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
				
				String buttonLabel = "立即预约";
			    String listType = "1";/*1-可预约,2-已预约,3-不可预约*/
			    
			    String isAppoFlag; /*当前批次下是否已经预约*/
			    sql = "SELECT 'Y' FROM PS_TZ_MSYY_KS_TBL WHERE TZ_CLASS_ID=? AND TZ_BATCH_ID=? AND OPRID=?";
			    isAppoFlag = jdbcTemplate.queryForObject(sql, new Object[]{ classID, batchID, oprid }, "String");
			    
			    int appoCount;//已预约人数
			    sql = "SELECT COUNT(1) FROM PS_TZ_MSYY_KS_TBL WHERE TZ_CLASS_ID=? AND TZ_BATCH_ID=? AND TZ_MS_PLAN_SEQ=?";
			    appoCount = jdbcTemplate.queryForObject(sql, new Object[]{ classID, batchID, msPlanSeq }, "int");
			    
			    Date startDatetime = dttmSimpleDateFormat.parse(msDate+" "+msStartTime);
			    if(currDate.after(startDatetime) || currDate.equals(startDatetime)){
			    	//报到时间已过，不可预约
			    	listType = "3";
			    }

			    if("Y".equals(isAppoFlag)){
			    	//当前学生已预约该面试批次，不可预约
			    	listType = "3";
			    	
			    	sql = "SELECT 'Y' FROM PS_TZ_MSYY_KS_TBL WHERE TZ_CLASS_ID=? AND TZ_BATCH_ID=? AND TZ_MS_PLAN_SEQ=? AND OPRID=?";
			    	String inCurrPlan = jdbcTemplate.queryForObject(sql, new Object[]{ classID, batchID, msPlanSeq, oprid }, "String");
			    	//当前学生已预约当前预约计划
			    	if("Y".equals(inCurrPlan)){
			    		listType = "2";
			    		buttonLabel = "已预约／撤销预约";
			    	}
			    }else{
			    	//当前学生尚未预约
			    	if(appoCount>=maxPerson){
			    		//预约人数已满，不可预约
				    	listType = "3";
				    }
			    }
			    
			    if("B".equals(timeValid)){
			    	listType = "3";
			    	buttonLabel = "预约未开始";
			    }else if("A".equals(timeValid)){
			    	listType = "3";
			    	buttonLabel = "预约已结束";
			    }
			    
			    tmpClassID = classID;
			    tmpBatchID = batchID;
			    
			    planCount++;
			    msStartTime = dttmSimpleDateFormat.format(startDatetime);
			    String countSta = String.valueOf(appoCount)+"/"+String.valueOf(maxPerson);
			    
			    msPlanListHtml = msPlanListHtml + tzGDObject.getHTMLText("HTML.TZInterviewAppointmentBundle.TZ_M_MS_APPOINT_LIST_HTML"+listType,contextPath,msStartTime,location,descr,countSta,classID, batchID, msPlanSeq,buttonLabel);
			}
			
			//没有面试预约显示
			String noAppointmentHtml = "";
			if(planCount == 0){
				noAppointmentHtml = tzGDObject.getHTMLText("HTML.TZInterviewAppointmentBundle.TZ_M_MS_NONE_APPOINT_HTML",contextPath);
			}
			
			rtnMap.replace("appoDesc", msExplainInfo);
			rtnMap.replace("appoHtml", msPlanListHtml);
			rtnMap.replace("noneAppoHtml", noAppointmentHtml);
		}catch(Exception e){
			e.printStackTrace();
		}
		return rtnMap;
	}
	
	
	
	private String tzConfirmAppointment(String strParams, String[] errorMsg){
		String strRet = "";
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("appoHtml", "");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
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
					mySqlLockService.lockRow(jdbcTemplate, "TZ_MSYY_KS_TBL");
					
					PsTzMsyyKsTblKey psTzMsyyKsTblKey = new PsTzMsyyKsTblKey();
					psTzMsyyKsTblKey.setTzClassId(classId);
					psTzMsyyKsTblKey.setTzBatchId(pcId);
					psTzMsyyKsTblKey.setTzMsPlanSeq(planId);
					psTzMsyyKsTblKey.setOprid(oprid);
					
					PsTzMsyyKsTbl psTzMsyyKsTbl = psTzMsyyKsTblMapper.selectByPrimaryKey(psTzMsyyKsTblKey);
					if(psTzMsyyKsTbl != null){
						int del = psTzMsyyKsTblMapper.deleteByPrimaryKey(psTzMsyyKsTblKey);
						if(del>0){
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
						}else{
							errorMsg[0] = "1";
							errorMsg[1] = "预约失败";
						}
					}
					// 解锁
					mySqlLockService.unlockRow(jdbcTemplate);
					
					Map<String,Object> appoMap = this.tzGetAppointmentHtml();
					//在线预约list
					String appoHtml = appoMap.get("appoHtml").toString();
					rtnMap.replace("appoHtml", appoHtml);
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
}