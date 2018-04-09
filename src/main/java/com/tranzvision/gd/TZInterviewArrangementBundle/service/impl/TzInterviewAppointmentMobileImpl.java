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
import com.tranzvision.gd.util.encrypt.DESUtil;
import com.tranzvision.gd.util.sql.MySqlLockService;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 闈㈣瘯棰勭害鎵嬫満鐗�
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
	
	@Autowired
	private SqlQuery sqlQuery;
	
	
	
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
			// 閫氱敤閾炬帴;
			String ZSGL_URL = contextPath + "/dispatcher";

			Map<String,Object> appoMap = this.tzGetAppointmentHtml();
			//璇存槑淇℃伅
			String appoDesc = appoMap.get("appoDesc").toString();
			//鍦ㄧ嚎棰勭害list
			String appoHtml = appoMap.get("appoHtml").toString();
			
			String noneAppoHtml = appoMap.get("noneAppoHtml").toString();
			//鏈烘瀯ID
			String JGID = sqlQuery.queryForObject("select TZ_JG_ID from PS_TZ_SITEI_DEFN_T WHERE TZ_SITEI_ID=?",new Object[]{siteId},"String");
			
			if (JGID.equals("SEM")) {
				JGID="";
			} else {
				JGID.toLowerCase();
			}
			
			if(!"".equals(noneAppoHtml)){
				interviewAppointHtml = tzGDObject.getHTMLText("HTML.TZInterviewAppointmentBundle.TZ_M_MS_NONE_APPO_MAIN_HTML",contextPath,ZSGL_URL,siteId,"1",noneAppoHtml,JGID);
			}else{
				interviewAppointHtml = tzGDObject.getHTMLText("HTML.TZInterviewAppointmentBundle.TZ_M_MS_APPOINT_MAIN_HTML",contextPath,appoDesc,appoHtml,ZSGL_URL,siteId,"1",JGID);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return "鏃犳硶鑾峰彇鐩稿叧鏁版嵁";
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
			errorMsg[1] = "鎿嶄綔寮傚父銆�" + e.getMessage();
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
			Date currDate = new Date();//褰撳墠鏃堕棿
			String tmpPwdKey = "TZGD_@_!_*_Interview_20170818_Tranzvision";
			
			String msPlanListHtml = "";
			String contextPath = request.getContextPath();
			
			String dttmFormat = getSysHardCodeVal.getDateTimeHMFormat();
			SimpleDateFormat dttmSimpleDateFormat = new SimpleDateFormat(dttmFormat);
			
			String sql = tzGDObject.getSQLText("SQL.TZInterviewAppointmentBundle.TzGdMsAppointPlan");
			List<Map<String,Object>> msPlanList = jdbcTemplate.queryForList(sql, new Object[]{oprid});
			
			String tmpClassID = "",tmpBatchID = "";
			//闈㈣瘯鎵规璁剧疆
			String showFront = "";
			String timeValid = "N";
			int planCount = 0;
			String msExplainInfo = "";
			for(Map<String,Object> msPlanMap : msPlanList){
				String submitType = "Y"; //鎸夐挳鎻愪氦浜嬩欢锛孻锛氶绾︼紝N锛氭挙閿�棰勭害
				
				String classID = msPlanMap.get("TZ_CLASS_ID") == null? "" : String.valueOf(msPlanMap.get("TZ_CLASS_ID"));
				String batchID = msPlanMap.get("TZ_BATCH_ID") == null? "" : String.valueOf(msPlanMap.get("TZ_BATCH_ID"));
				String msPlanSeq = msPlanMap.get("TZ_MS_PLAN_SEQ") == null? "" : String.valueOf(msPlanMap.get("TZ_MS_PLAN_SEQ"));
				int maxPerson = msPlanMap.get("TZ_MSYY_COUNT") == null? 0 : Integer.valueOf(String.valueOf(msPlanMap.get("TZ_MSYY_COUNT")));
				
				String msDate = msPlanMap.get("TZ_MS_DATE") == null? "" : String.valueOf(msPlanMap.get("TZ_MS_DATE"));
				String msStartTime = msPlanMap.get("TZ_START_TM") == null? "" : String.valueOf(msPlanMap.get("TZ_START_TM"));
				//String msEndTime = msPlanMap.get("TZ_END_TM") == null? "" : String.valueOf(msPlanMap.get("TZ_END_TM"));
				
				String location = msPlanMap.get("TZ_MS_LOCATION") == null? "" : String.valueOf(msPlanMap.get("TZ_MS_LOCATION"));
				String descr = msPlanMap.get("TZ_MS_ARR_DEMO") == null? "" : String.valueOf(msPlanMap.get("TZ_MS_ARR_DEMO"));
				
				//闈㈣瘯鎵规璁剧疆鍙傛暟
				if(!tmpClassID.equals(classID) || !tmpBatchID.equals(batchID)){
					
					String setSql = tzGDObject.getSQLText("SQL.TZInterviewAppointmentBundle.TzGdMsAppointSet");
					Map<String,Object> msyySetMap = jdbcTemplate.queryForMap(setSql, new Object[]{ currDate, currDate, currDate, classID, batchID });
					
					if(msyySetMap != null){
						//鏃堕棿闄愬埗锛孻-棰勭害鏃堕棿鍐咃紝B-棰勭害鏃堕棿鍓嶏紝A-棰勭害鏃堕棿鍚庯紝N-涓鸿缃绾�
						timeValid = msyySetMap.get("TZ_TIME_VALID").toString();
						showFront = msyySetMap.get("TZ_SHOW_FRONT") == null ? "N" : msyySetMap.get("TZ_SHOW_FRONT").toString();
						msExplainInfo = msyySetMap.get("TZ_DESCR") == null ? "" : msyySetMap.get("TZ_DESCR").toString();
					}
				}
				
				//棰勭害鏃堕棿缁撴潫鍚庯紝涓嶅啀鍓嶅彴鏄剧ず
				if(("A".equals(timeValid) && !"Y".equals(showFront)) || "N".equals(timeValid)){
					continue;
				}
				
				String buttonLabel = "绔嬪嵆棰勭害";
			    String listType = "1";/*1-鍙绾�,2-宸查绾�,3-涓嶅彲棰勭害*/
			    
			    String isAppoFlag; /*褰撳墠鎵规涓嬫槸鍚﹀凡缁忛绾�*/
			    sql = "SELECT 'Y' FROM PS_TZ_MSYY_KS_TBL WHERE TZ_CLASS_ID=? AND TZ_BATCH_ID=? AND OPRID=?";
			    isAppoFlag = jdbcTemplate.queryForObject(sql, new Object[]{ classID, batchID, oprid }, "String");
			    
			    int appoCount;//宸查绾︿汉鏁�
			    sql = "SELECT COUNT(1) FROM PS_TZ_MSYY_KS_TBL WHERE TZ_CLASS_ID=? AND TZ_BATCH_ID=? AND TZ_MS_PLAN_SEQ=?";
			    appoCount = jdbcTemplate.queryForObject(sql, new Object[]{ classID, batchID, msPlanSeq }, "int");
			    
			    Date startDatetime = dttmSimpleDateFormat.parse(msDate+" "+msStartTime);
			    if(currDate.after(startDatetime) || currDate.equals(startDatetime)){
			    	//鎶ュ埌鏃堕棿宸茶繃锛屼笉鍙绾�
			    	listType = "3";
			    }

			    if("Y".equals(isAppoFlag)){
			    	//褰撳墠瀛︾敓宸查绾﹁闈㈣瘯鎵规锛屼笉鍙绾�
			    	listType = "3";
			    	
			    	sql = "SELECT 'Y' FROM PS_TZ_MSYY_KS_TBL WHERE TZ_CLASS_ID=? AND TZ_BATCH_ID=? AND TZ_MS_PLAN_SEQ=? AND OPRID=?";
			    	String inCurrPlan = jdbcTemplate.queryForObject(sql, new Object[]{ classID, batchID, msPlanSeq, oprid }, "String");
			    	//褰撳墠瀛︾敓宸查绾﹀綋鍓嶉绾﹁鍒�
			    	if("Y".equals(inCurrPlan)){
			    		listType = "2";
			    		buttonLabel = "宸查绾︼紡鎾ら攢棰勭害";
			    		submitType = "N";
			    	}
			    }else{
			    	//褰撳墠瀛︾敓灏氭湭棰勭害
			    	if(appoCount>=maxPerson){
			    		//棰勭害浜烘暟宸叉弧锛屼笉鍙绾�
				    	listType = "3";
				    }
			    }
			    
			    if("B".equals(timeValid)){
			    	listType = "3";
			    	buttonLabel = "棰勭害鏈紑濮�";
			    }else if("A".equals(timeValid)){
			    	listType = "3";
			    	buttonLabel = "棰勭害宸茬粨鏉�";
			    }
			    
			    tmpClassID = classID;
			    tmpBatchID = batchID;
			    
			    planCount++;
			    msStartTime = dttmSimpleDateFormat.format(startDatetime);
			    String countSta = String.valueOf(appoCount)+"/"+String.valueOf(maxPerson);
			    String msKey = DESUtil.encrypt(classID + "==" + batchID + "==" + msPlanSeq +"==" + submitType, tmpPwdKey);
			    
			    msPlanListHtml = msPlanListHtml + tzGDObject.getHTMLText("HTML.TZInterviewAppointmentBundle.TZ_M_MS_APPOINT_LIST_HTML"+listType,contextPath,msStartTime,location,descr,countSta,msKey,buttonLabel);
			}
			
			//娌℃湁闈㈣瘯棰勭害鏄剧ず
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
	
	
	/**
	 * 璇ユ柟娉曞純鐢紝鏀圭敤PC鐗堢‘璁ら潰璇曢绾︽柟娉�
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
						if(del>0){
							errorMsg[0] = "0";
							errorMsg[1] = "鎾ら攢棰勭害鎴愬姛";
							
							//鎾ら攢鎴愬姛鍚庡彂閫佺珯鍐呬俊
							try{
								sql = "SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=?";
								String name = jdbcTemplate.queryForObject(sql, new Object[]{ oprid }, "String");
								//闈㈣瘯棰勭害鎴愬姛绔欏唴淇℃ā鏉�
								String znxModel = getHardCodePoint.getHardCodePointVal("TZ_MSYY_CX_ZNX_TMP");
								//褰撳墠鏈烘瀯
								String jgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
								
								//鍒涘缓閭欢浠诲姟瀹炰緥
								String taskId = createTaskServiceImpl.createTaskIns(jgid, znxModel, "ZNX", "A");
								// 鍒涘缓閭欢鍙戦�佸惉浼�
								String crtAudi = createTaskServiceImpl.createAudience(taskId,jgid,"闈㈣瘯棰勭害鎴愬姛绔欏唴淇￠�氱煡", "JSRW");
								//娣诲姞鍚紬鎴愬憳
								boolean bl = createTaskServiceImpl.addAudCy(crtAudi, name, "", "", "", "", "", pcId, oprid, classId, planId, "");
								if(bl){
									sendSmsOrMalServiceImpl.send(taskId, "");
								}
							}catch(NullPointerException nullEx){
								//娌℃湁閰嶇疆閭欢妯℃澘
								nullEx.printStackTrace();
							}
						}else{
							errorMsg[0] = "1";
							errorMsg[1] = "鎾ら攢棰勭害澶辫触";
						}
					}else{
						String otherSql = "select 'Y' from PS_TZ_MSYY_KS_TBL where TZ_CLASS_ID=? and TZ_BATCH_ID=? and OPRID=? limit 1";
						String existsOther = jdbcTemplate.queryForObject(otherSql, new Object[]{ classId, pcId, oprid }, "String");
						if("Y".equals(existsOther)){
							errorMsg[0] = "2";
							errorMsg[1] = "棰勭害澶辫触,浣犲凡棰勭害鍏朵粬鎶ュ埌鏃堕棿鐨勯潰璇�";
						}else{
							//閿佽〃
							mySqlLockService.lockRow(jdbcTemplate, "TZ_MSYY_KS_TBL");
							try{
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
											errorMsg[1] = "棰勭害鎴愬姛";
										}else{
											errorMsg[0] = "1";
											errorMsg[1] = "棰勭害澶辫触";
										}
									}else{
										errorMsg[0] = "2";
										errorMsg[1] = "棰勭害澶辫触锛岃鎶ュ埌鏃堕棿棰勭害浜烘暟宸叉弧锛岃棰勭害鍏朵粬鎶ュ埌鏃堕棿";
									}
								}
								// 瑙ｉ攣
								mySqlLockService.unlockRow(jdbcTemplate);
							}catch(Exception ee){
								ee.printStackTrace();
								errorMsg[0] = "1";
								errorMsg[1] = "棰勭害澶辫触锛屽け璐ュ師鍥狅細"+ee.getMessage();
								//鍥炴粴
								mySqlLockService.rollback(jdbcTemplate);
							}
							
							//闈㈣瘯棰勭害鎴愬姛锛屽彂閫佹垚鍔熼�氱煡绔欏唴淇�
							if("0".equals(errorMsg[0])){
								//棰勭害鎴愬姛鍚庡彂閫佺珯鍐呬俊
								try{
									sql = "SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=?";
									String name = jdbcTemplate.queryForObject(sql, new Object[]{ oprid }, "String");
									//闈㈣瘯棰勭害鎴愬姛绔欏唴淇℃ā鏉�
									String znxModel = getHardCodePoint.getHardCodePointVal("TZ_MSYY_CG_ZNX_TMP");
									//褰撳墠鏈烘瀯
									String jgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
									
									//鍒涘缓閭欢浠诲姟瀹炰緥
									String taskId = createTaskServiceImpl.createTaskIns(jgid, znxModel, "ZNX", "A");
									// 鍒涘缓閭欢鍙戦�佸惉浼�
									String crtAudi = createTaskServiceImpl.createAudience(taskId,jgid,"闈㈣瘯棰勭害鎴愬姛绔欏唴淇￠�氱煡", "JSRW");
									//娣诲姞鍚紬鎴愬憳
									boolean bl = createTaskServiceImpl.addAudCy(crtAudi, name, "", "", "", "", "", pcId, oprid, classId, planId, "");
									if(bl){
										sendSmsOrMalServiceImpl.send(taskId, "");
									}
								}catch(NullPointerException nullEx){
									//娌℃湁閰嶇疆閭欢妯℃澘
									nullEx.printStackTrace();
								}
							}
						}
					}

					Map<String,Object> appoMap = this.tzGetAppointmentHtml();
					//鍦ㄧ嚎棰勭害list
					String appoHtml = appoMap.get("appoHtml").toString();
					rtnMap.replace("appoHtml", appoHtml);
				}else{
					errorMsg[0] = "1";
					errorMsg[1] = "鎶辨瓑锛屾偍灏氭湭瀹夋帓鏈壒娆￠潰璇曪紝棰勭害澶辫触锛�";
				}
			}else{
				errorMsg[0] = "1";
				errorMsg[1] = "鎶辨瓑锛屾偍娌℃湁鍦ㄥ綋鍓嶇彮绾т腑鎶ュ悕锛屼笉鑳介绾︼紒";
			}
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "鎿嶄綔寮傚父銆�"+e.getMessage();
		}
		strRet = jacksonUtil.Map2json(rtnMap);
		return strRet;
	}
}