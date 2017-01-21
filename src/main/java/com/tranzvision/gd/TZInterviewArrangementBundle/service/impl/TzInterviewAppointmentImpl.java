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
import com.tranzvision.gd.TZInterviewArrangementBundle.dao.PsTzMsyySetTblMapper;
import com.tranzvision.gd.TZInterviewArrangementBundle.model.PsTzMsyyKsTbl;
import com.tranzvision.gd.TZInterviewArrangementBundle.model.PsTzMsyyKsTblKey;
import com.tranzvision.gd.TZInterviewArrangementBundle.model.PsTzMsyySetTbl;
import com.tranzvision.gd.TZInterviewArrangementBundle.model.PsTzMsyySetTblKey;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.MySqlLockService;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

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
	
	@Autowired
	private PsTzMsyySetTblMapper psTzMsyySetTblMapper;
	
	
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
			
			
			String dtFormat = getSysHardCodeVal.getDateFormat();
			String tmFormat = getSysHardCodeVal.getTimeHMFormat();
			String dttmFormat = getSysHardCodeVal.getDateTimeHMFormat();
			
			SimpleDateFormat dtSimpleDateFormat = new SimpleDateFormat(dtFormat);
			SimpleDateFormat tmSimpleDateFormat = new SimpleDateFormat(tmFormat);
			SimpleDateFormat dttmSimpleDateFormat = new SimpleDateFormat(dttmFormat);
			
			String sql = tzGDObject.getSQLText("SQL.TZInterviewAppointmentBundle.TzGdMsAppointPlan");
			List<Map<String,Object>> msPlanList = jdbcTemplate.queryForList(sql, new Object[]{oprid});
			
			String tmpClassID = "",tmpBatchID = "";
			//面试批次设置
			String showFront = "";
			String inDateTime = "N";
			String afterCloseDate = "N";
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
					PsTzMsyySetTblKey psTzMsyySetTblKey =  new PsTzMsyySetTblKey();
					psTzMsyySetTblKey.setTzClassId(classID);
					psTzMsyySetTblKey.setTzBatchId(batchID);
					PsTzMsyySetTbl psTzMsyySetTbl = psTzMsyySetTblMapper.selectByPrimaryKey(psTzMsyySetTblKey);
					if(psTzMsyySetTbl != null){
						Date openDate = psTzMsyySetTbl.getTzOpenDt();
						Date openTime = psTzMsyySetTbl.getTzOpenTm();
						Date closeDate = psTzMsyySetTbl.getTzCloseDt();
						Date closeTime = psTzMsyySetTbl.getTzCloseTm();
						showFront = psTzMsyySetTbl.getTzShowFront();
						msExplainInfo = psTzMsyySetTbl.getTzDescr();
						
						String oDate = dtSimpleDateFormat.format(openDate);
						String cDate = dtSimpleDateFormat.format(closeDate);
						String oTime = tmSimpleDateFormat.format(openTime);
						String cTime = tmSimpleDateFormat.format(closeTime);
						
						Date openDatetime = dttmSimpleDateFormat.parse(oDate+" "+oTime);
						Date closeDatetime = dttmSimpleDateFormat.parse(cDate+" "+cTime);
						
						if(currDate.after(openDatetime) && currDate.before(closeDatetime) || currDate.equals(openDatetime) || currDate.equals(closeDatetime)){
							inDateTime = "Y";
						}else if(currDate.after(closeDatetime)){
							afterCloseDate = "Y";
						}
					}
				}
				
				//预约时间结束后，仍显示在学生前台
				if("Y".equals(showFront)){
					if("Y".equals(inDateTime) || "Y".equals(afterCloseDate)){
					}else{
						continue;
					}
				}else{
					//只有在有效日期下显示
					if(!"Y".equals(inDateTime)){
						continue;
					}
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
			    	disabledClass = "btn-disabled full_icon";
			    }
			    
			    if(appoCount>=maxPerson){
			    	legendClass = "full";
			    	disabledClass = "btn-disabled full_icon";
			    }else{
			    	legendClass = "order_p";
			    }
			    
			    if("Y".equals(isAppoFlag)){
			    	disabledClass = "btn-disabled full_icon";
			    	
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
			    
			    tmpClassID = classID;
			    tmpBatchID = batchID;
			    
			    planCount++;
			    msStartTime = tmSimpleDateFormat.format(startDatetime);
			    msPlanTrHtml = msPlanTrHtml + tzGDObject.getHTMLText("HTML.TZInterviewAppointmentBundle.TZ_GD_MS_APPOINT_TR_HTML",msDate,msStartTime,location,descr,legendClass,String.valueOf(appoCount),String.valueOf(maxPerson),classID, batchID, msPlanSeq,disabledClass,buttonLabel);
			}
			
			//没有面试预约显示
			String noAppointmentText = "您暂时没有可预约的面试，如果有面试预约，我们将会邮件通知您。";
			
			if(planCount == 0){
				msPlanTrHtml = noAppointmentText;
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
						psTzMsyyKsTblMapper.deleteByPrimaryKey(psTzMsyyKsTblKey);
						errorMsg[0] = "1";
						errorMsg[1] = "撤销预约成功";
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
						if(rtn != 0){
							errorMsg[0] = "0";
							errorMsg[1] = "预约成功";
							
							//预约成功后给发送邮件
							sql = "SELECT TZ_ZY_EMAIL FROM PS_TZ_LXFSINFO_TBL WHERE TZ_LXFS_LY='ZSBM' AND TZ_LYDX_ID=?";
							String mainEmail = jdbcTemplate.queryForObject(sql, new Object[]{ appInsId }, "String");
							if(!"".equals(mainEmail) && mainEmail !=null){
								sql = "SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=?";
								String name = jdbcTemplate.queryForObject(sql, new Object[]{ oprid }, "String");
								
								//面试预约成功通知邮件模板
								String mailModel = getHardCodePoint.getHardCodePointVal("TZ_MS_APPO_MAIL_TMP");
								//当前机构
								String jgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
								
								//创建邮件任务实例
								String taskId = createTaskServiceImpl.createTaskIns(jgid, mailModel, "MAL", "A");
								// 创建邮件发送听众
								String crtAudi = createTaskServiceImpl.createAudience(taskId,jgid,"面试预约通知邮件", "JSRW");
								//添加听众成员
								boolean bl = createTaskServiceImpl.addAudCy(crtAudi, name, "", "", "", mainEmail, "", pcId, oprid, classId, planId, "");
								if(bl){
									sendSmsOrMalServiceImpl.send(taskId, "");
								}
							}
						}
					}
					
					// 解锁
					mySqlLockService.unlockRow(jdbcTemplate);
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
	public String getEmlInterviewDate(String audId,String audCyId){
		String strRet = "";
		
		try{
			String sql = "SELECT TZ_XSXS_ID,TZ_WEIXIN,TZ_HUOD_ID FROM PS_TZ_AUDCYUAN_T WHERE TZ_AUDIENCE_ID=? AND TZ_AUDCY_ID=?";
			Map<String,Object> audMap = jdbcTemplate.queryForMap(sql, new Object[]{ audId, audCyId });
			
			String classID = String.valueOf(audMap.get("TZ_XSXS_ID"));
			String batchID = String.valueOf(audMap.get("TZ_WEIXIN"));
			String msPlanSeq = String.valueOf(audMap.get("TZ_HUOD_ID"));

			sql = "SELECT TZ_MS_DATE,TZ_START_TM,TZ_END_TM FROM PS_TZ_MSSJ_ARR_TBL WHERE TZ_CLASS_ID=? AND TZ_BATCH_ID=? AND TZ_MS_PLAN_SEQ=?";
			Map<String,Object> msDateMap = jdbcTemplate.queryForMap(sql, new Object[]{ classID, batchID, msPlanSeq });
			
			String msDate = String.valueOf(msDateMap.get("TZ_MS_DATE"));
			String msStartTime = String.valueOf(msDateMap.get("TZ_START_TM"));
			String msEndTime = String.valueOf(msDateMap.get("TZ_END_TM"));
			/*
			String dtFormat = getSysHardCodeVal.getDateFormat();
			String tmFormat = getSysHardCodeVal.getTimeHMFormat();

			SimpleDateFormat dateSimpleDateFormat = new SimpleDateFormat(dtFormat);
			SimpleDateFormat timeSimpleDateFormat = new SimpleDateFormat(tmFormat);
			*/
			strRet = msDate+" "+msStartTime+" 至 "+ msDate+" "+msEndTime;
		}catch(Exception e){
			e.printStackTrace();
		}
		return strRet;
	}
	
}
