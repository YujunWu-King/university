package com.tranzvision.gd.TZEmailSmsQFBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZApplicationVerifiedBundle.dao.PsprcsrqstMapper;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.Psprcsrqst;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.dao.PsTzDxyjfsrwTblMapper;
import com.tranzvision.gd.TZEmailSmsSendBundle.dao.PsTzEmlTaskAetMapper;
import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzDxyjfsrwTbl;
import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzEmlTaskAet;
import com.tranzvision.gd.batch.engine.base.BaseEngine;
import com.tranzvision.gd.batch.engine.base.EngineParameters;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 
 * TZ_SMS_VIEWHISTORY_PKG:TZ_SMS_VIEWHISTORY_CLS
 * 短信发送历史查看
 *
 */
@Service("com.tranzvision.gd.TZEmailSmsQFBundle.service.impl.TzSmsViewHistoryClsServiceImpl")
public class TzSmsViewHistoryClsServiceImpl extends FrameworkImpl{
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private SqlQuery jdbcTemplate;
	
	@Autowired
	private GetSeqNum getSeqNum;
	
	@Autowired
	private PsprcsrqstMapper psprcsrqstMapper;
	
	@Autowired
	private PsTzEmlTaskAetMapper psTzEmlTaskAetMapper;
	
	@Autowired
	private TZGDObject tZGDObject;
	
	@Autowired
	private PsTzDxyjfsrwTblMapper psTzDxyjfsrwTblMapper;
	
	//获取短信发送历史grid列表
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		Map<String, Object> returnMap = new HashMap<>();
		ArrayList<Map<String, Object>> returnArrayList = new ArrayList<>();
		returnMap.put("total", 0);
		returnMap.put("root", returnArrayList);
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);
		
		String orgID = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		
		// store类型;
	    String strStoreType = jacksonUtil.getString("storeType");
	    String strSmsQfID = jacksonUtil.getString("smsQfID");
	    String str_TZ_MLSM_QFPC_ID = strSmsQfID;
	    
	    // 邮件或短信任务ID;
	    String taskID = "";

	    int num_total = 0;
	    if("history".equals(strStoreType)){
	    	/*模板ID，任务发送状态，总数，成功数，失败数，重复数，发送时间，操作人*/
	        String  TZ_RWZX_ZT_DESC  = "", TZ_SEND_COUNT  = "", TZ_SEND_SUC_COUNT  = "", TZ_SEND_FAIL_COUNT = "", TZ_SEND_RPT_COUNT  = "", TZ_RWZX_DT_STR  = "", TZ_REALNAME = "";
	        String TZ_RWZX_ZT  ="";
	        
	        String sql_FSRW = "SELECT TZ_EML_SMS_TASK_ID FROM PS_TZ_DXYJFSRW_TBL WHERE TZ_MLSM_QFPC_ID=? ORDER BY CAST(TZ_EML_SMS_TASK_ID as SIGNED) DESC";
	        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql_FSRW,new Object[]{str_TZ_MLSM_QFPC_ID});
	        if(list != null && list.size() > 0){
	        	for(int i = 0; i < list.size(); i++){
	        		taskID = (String)list.get(i).get("TZ_EML_SMS_TASK_ID");
	        		
	        		//String sql1 = "select TZ_TMPL_ID,TZ_RWZX_ZT,TZ_RWZX_ZT_DESC,TZ_SEND_COUNT,TZ_SEND_SUC_COUNT,TZ_SEND_FAIL_COUNT,TZ_SEND_RPT_COUNT,TZ_RWZX_DT_STR,TZ_REALNAME from PS_TZ_SMS_HIS_TJ_V where TZ_EML_SMS_TASK_ID=? and TZ_JG_ID=?";
	        		String sql1 = "select `A`.`TZ_EML_SMS_TASK_ID` AS `TZ_EML_SMS_TASK_ID`,`A`.`TZ_JG_ID` AS `TZ_JG_ID`,`A`.`TZ_TMPL_ID` AS `TZ_TMPL_ID`,`A`.`TZ_RWZX_ZT` AS `TZ_RWZX_ZT`,(select `PS_TZ_PT_ZHZXX_TBL`.`TZ_ZHZ_DMS` from `PS_TZ_PT_ZHZXX_TBL` where ((`PS_TZ_PT_ZHZXX_TBL`.`TZ_ZHZJH_ID` = 'TZ_RWZX_ZT') and (`PS_TZ_PT_ZHZXX_TBL`.`TZ_ZHZ_ID` = `A`.`TZ_RWZX_ZT`) and (`PS_TZ_PT_ZHZXX_TBL`.`TZ_EFF_STATUS` = 'A'))) AS `TZ_RWZX_ZT_DESC`,(select count(1) from `PS_TZ_AUDCYUAN_T` where (`PS_TZ_AUDCYUAN_T`.`TZ_AUDIENCE_ID` = `A`.`TZ_AUDIENCE_ID`)) AS `TZ_SEND_COUNT`,(select count(1) from `PS_TZ_DXFSLSHI_TBL` where ((`PS_TZ_DXFSLSHI_TBL`.`TZ_EML_SMS_TASK_ID` = `A`.`TZ_EML_SMS_TASK_ID`) and (`PS_TZ_DXFSLSHI_TBL`.`TZ_FS_ZT` = 'SUC'))) AS `TZ_SEND_SUC_COUNT`,(select count(1) from `PS_TZ_DXFSLSHI_TBL` where ((`PS_TZ_DXFSLSHI_TBL`.`TZ_EML_SMS_TASK_ID` = `A`.`TZ_EML_SMS_TASK_ID`) and (`PS_TZ_DXFSLSHI_TBL`.`TZ_FS_ZT` = 'FAIL'))) AS `TZ_SEND_FAIL_COUNT`,(select count(1) from `PS_TZ_DXFSLSHI_TBL` where ((`PS_TZ_DXFSLSHI_TBL`.`TZ_EML_SMS_TASK_ID` = `A`.`TZ_EML_SMS_TASK_ID`) and (`PS_TZ_DXFSLSHI_TBL`.`TZ_FS_ZT` = 'RPT'))) AS `TZ_SEND_RPT_COUNT`,date_format(`A`.`TZ_RWZX_DT`,'%Y-%m-%d %H:%i:%s') AS `TZ_RWZX_DT_STR`,`B`.`TZ_REALNAME` AS `TZ_REALNAME` from (`PS_TZ_DXYJFSRW_TBL` `A` left join `PS_TZ_AQ_YHXX_TBL` `B` on((`A`.`ROW_ADDED_OPRID` = `B`.`OPRID`))) WHERE A.TZ_EML_SMS_TASK_ID=? and A.TZ_JG_ID=?";
	        		Map<String, Object> map1 = jdbcTemplate.queryForMap(sql1,new Object[]{taskID, orgID});
	        		if(map1 != null){
	        			num_total = num_total + 1;
	        			//TZ_TMPL_ID = map1.get("TZ_TMPL_ID") == null ? "" : (String)map1.get("TZ_TMPL_ID");
	        			TZ_RWZX_ZT = map1.get("TZ_RWZX_ZT") == null ? "" : (String)map1.get("TZ_RWZX_ZT");
	        			TZ_RWZX_ZT_DESC = map1.get("TZ_RWZX_ZT_DESC") == null ? "" : (String)map1.get("TZ_RWZX_ZT_DESC");
	        			TZ_SEND_COUNT = String.valueOf(map1.get("TZ_SEND_COUNT"));
	        			TZ_SEND_SUC_COUNT =  String.valueOf(map1.get("TZ_SEND_SUC_COUNT"));
	        			TZ_SEND_FAIL_COUNT = String.valueOf(map1.get("TZ_SEND_FAIL_COUNT"));
	        			TZ_SEND_RPT_COUNT = String.valueOf(map1.get("TZ_SEND_RPT_COUNT"));
	        			TZ_RWZX_DT_STR = map1.get("TZ_RWZX_DT_STR") == null ? "" : (String)map1.get("TZ_RWZX_DT_STR");
	        			TZ_REALNAME = map1.get("TZ_REALNAME") == null ? "" : (String)map1.get("TZ_REALNAME");
	        			
	        			Map<String, Object> reMap1 = new HashMap<>();
	        			reMap1.put("statusID", TZ_RWZX_ZT);
	        			reMap1.put("status", TZ_RWZX_ZT_DESC);
	        			reMap1.put("sendNum", TZ_SEND_COUNT );
	        			reMap1.put("sendSucNum", TZ_SEND_SUC_COUNT );
	        			reMap1.put("sendFailNum", TZ_SEND_FAIL_COUNT );
	        			reMap1.put("sendRptNum", TZ_SEND_RPT_COUNT);
	        			reMap1.put("sendDt", TZ_RWZX_DT_STR);
	        			reMap1.put("operator", TZ_REALNAME );
	        			reMap1.put("taskID", taskID);
	        			reMap1.put("smsQfID", strSmsQfID);
	        			reMap1.put("smsSubmitNum", TZ_SEND_COUNT);
	        			
	        			returnArrayList.add(reMap1);
	        			
	        		}
	        	}
	        	
	        }
	    }
	    
	    if("historyDetail".equals(strStoreType)){
	    	taskID = jacksonUtil.getString("taskID");
	    	if(taskID != null && !"".equals(taskID)){
	    		//发送任务表中的任务ID;
	            String str_TZ_AUDIENCE_ID = jdbcTemplate.queryForObject("select TZ_AUDIENCE_ID from PS_TZ_DXYJFSRW_TBL where TZ_EML_SMS_TASK_ID=?", new Object[]{taskID},"String");
	            if(str_TZ_AUDIENCE_ID != null && !"".equals(str_TZ_AUDIENCE_ID)){
	            	// 听众成员ID，听众姓名，听众成员邮箱，听众成员oprid;
		            String TZ_AUDCY_ID = "", TZ_AUD_XM = "", TZ_ZY_SJ = "", OPRID = "", TZ_AUDCY_SEND_STATUS = "";
		            String sql_AUDCY = "select TZ_AUDCY_ID,TZ_AUD_XM,TZ_ZY_SJ,OPRID from PS_TZ_AUDCYUAN_T where TZ_AUDIENCE_ID=?";
		            List<Map<String, Object>> list2 = jdbcTemplate.queryForList(sql_AUDCY,new Object[]{str_TZ_AUDIENCE_ID});
		            if(list2 != null && list2.size() > 0){
		            	for(int i = 0; i < list2.size(); i++){
		            		Map<String, Object> map2 = list2.get(i);
		            		if(map2 != null){
		            			TZ_AUDCY_ID = map2.get("TZ_AUDCY_ID") == null ? "" : (String)map2.get("TZ_AUDCY_ID");
		            			TZ_AUD_XM = map2.get("TZ_AUD_XM") == null ? "" : (String)map2.get("TZ_AUD_XM");
		            			TZ_ZY_SJ = map2.get("TZ_ZY_SJ") == null ? "" : (String)map2.get("TZ_ZY_SJ");
		            			OPRID = map2.get("OPRID") == null ? "" : (String)map2.get("OPRID");
		            			String str_TZ_RWSL_ID = "", strSendDttm = "";
		            			TZ_AUDCY_SEND_STATUS = "";
		            			
		            			Map<String, Object> map3 = jdbcTemplate.queryForMap("select TZ_RWSL_ID,(select TZ_ZHZ_DMS from PS_TZ_PT_ZHZXX_TBL  where TZ_ZHZJH_ID='TZ_RWZX_ZT' and TZ_ZHZ_ID=TZ_FS_ZT) TZ_FS_ZT,date_format(TZ_FS_DT,'%Y-%m-%d %H:%i:%s') TZ_FS_DT from PS_TZ_DXFSLSHI_TBL WHERE TZ_EML_SMS_TASK_ID = ? AND TZ_SJ_HM = ? limit 0,1",new Object[]{taskID, TZ_ZY_SJ});
		            			num_total = num_total + 1;
		            			if(map3 != null){
		            				str_TZ_RWSL_ID = map3.get("TZ_RWSL_ID") == null ? "" : (String)map3.get("TZ_RWSL_ID");
		            				TZ_AUDCY_SEND_STATUS = map3.get("TZ_FS_ZT")  == null ? "" : (String)map3.get("TZ_FS_ZT");
		            				strSendDttm = map3.get("TZ_FS_DT")  == null ? "" : (String)map3.get("TZ_FS_DT");
		            			}
		            			
		            			Map<String, Object> reMap2 = new HashMap<>();
		            			reMap2.put("taskID", taskID);
		            			reMap2.put("audID", str_TZ_AUDIENCE_ID);
		            			reMap2.put("audCyID", TZ_AUDCY_ID);
		            			reMap2.put("AddresseeName", TZ_AUD_XM);
		            			reMap2.put("AddresseePhone", TZ_ZY_SJ);
		            			reMap2.put("audCyOprID", OPRID);
		            			reMap2.put("rwslID", str_TZ_RWSL_ID);
		            			reMap2.put("status", TZ_AUDCY_SEND_STATUS);
		            			reMap2.put("sendTime", strSendDttm);
		            			
		            			returnArrayList.add(reMap2);
		            		}
		            		
		            	}
		            }
	            }
	            
	    	}
	    }
	    
	    returnMap.put("total", num_total);
		returnMap.put("root", returnArrayList);
		
	    return jacksonUtil.Map2json(returnMap);
	}
	
	/* 获取页面信息 */
	@Override
	public String tzOther(String OperateType, String comParams, String[] errorMsg) {
		// 返回值;
		String strResultConten = "";
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);
		String type = "";
		if(jacksonUtil.containsKey("type")){
			type = jacksonUtil.getString("type");
		}
		
		if ("viewEachSMS".equals(type)) {
			strResultConten = this.viewEachSMS(comParams, errorMsg);
		}

		
		return strResultConten;
	}
	
	private String viewEachSMS(String comParams, String[] errorMsg){
		Map<String, Object> returnMap = new HashMap<>();
		returnMap.put("formData", ""); 
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);
		
	    // 发件箱，收件箱，邮件主题;
	    String TZ_SJ_HM = "";
	    // 邮件发送日期，邮件正文;
	    String TZ_YJFS_RQ = "", TZ_DX_ZHWEN = "";
	      
	    String rwsl_ID = jacksonUtil.getString("rwsl_ID");
	    String sql = "select a.TZ_SJ_HM,DATE_FORMAT(b.TZ_YJFS_RQ, '%Y-%m-%d %H:%i:%s') TZ_YJFS_RQ ,b.TZ_DX_ZHWEN from PS_TZ_DXFSLSHI_TBL a, PS_TZ_DXZWLSHI_TBL b where a.TZ_RWSL_ID=b.TZ_RWSL_ID and a.TZ_RWSL_ID=?";
	    Map<String,Object> map = jdbcTemplate.queryForMap(sql,new Object[]{rwsl_ID});
	    if(map != null){
	    	TZ_SJ_HM = map.get("TZ_SJ_HM") == null ? "" : (String)map.get("TZ_SJ_HM");
	    	TZ_YJFS_RQ = map.get("TZ_YJFS_RQ") == null ? "" : (String)map.get("TZ_YJFS_RQ");
	    	TZ_DX_ZHWEN = map.get("TZ_DX_ZHWEN") == null ? "" : (String)map.get("TZ_DX_ZHWEN");
	    }
	    
	    Map<String, Object> rMap = new HashMap<>();
	    rMap.put("AddresseePhone", TZ_SJ_HM);
	    rMap.put("sendDatetime", TZ_YJFS_RQ);
	    rMap.put("SmsContent", TZ_DX_ZHWEN);

	    returnMap.replace("formData", rMap);
	    
	    return jacksonUtil.Map2json(returnMap);
	}
	
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		
		Map<String, Object> returnmap = new HashMap<>();
		returnmap.put("success", "发送失败");
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		int num = 0;
		for (num = 0; num < actData.length; num++) {
			// 表单内容;
			String strForm = actData[num];
			jacksonUtil.json2Map(strForm);
			// 重新发送任务ID;
	        String taskId = jacksonUtil.getString("taskId");
	        //String smsQfID = jacksonUtil.getString("smsQfID");
	        PsTzDxyjfsrwTbl psTzDxyjfsrwTbl = psTzDxyjfsrwTblMapper.selectByPrimaryKey(taskId);
	        psTzDxyjfsrwTbl.setTzRwzxZt("A");
	        psTzDxyjfsrwTblMapper.updateByPrimaryKey(psTzDxyjfsrwTbl);
	         
			int processInstance = getSeqNum.getSeqNum("PSPRCSRQST", "PROCESSINSTANCE");
			// 当前用户;
			String currentOprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			// 生成运行控制ID;
			SimpleDateFormat datetimeFormate = new SimpleDateFormat("yyyyMMddHHmmss");
			String s_dtm = datetimeFormate.format(new Date());
			String runCntlId = "SMS" + s_dtm + "_" + getSeqNum.getSeqNum("PSPRCSRQST", "RUN_ID");

			Psprcsrqst psprcsrqst = new Psprcsrqst();
			psprcsrqst.setPrcsinstance(processInstance);
			psprcsrqst.setRunId(runCntlId);
			psprcsrqst.setOprid(currentOprid);
			psprcsrqst.setRundttm(new Date());
			psprcsrqst.setRunstatus("5");
			psprcsrqstMapper.insert(psprcsrqst);

			PsTzEmlTaskAet psTzEmlTaskAet = new PsTzEmlTaskAet();
			psTzEmlTaskAet.setRunId(runCntlId);
			psTzEmlTaskAet.setTzEmlSmsTaskId(taskId);
			psTzEmlTaskAetMapper.insert(psTzEmlTaskAet);

			try {
				String currentAccountId = tzLoginServiceImpl.getLoginedManagerDlzhid(request);
				String currentOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
				
				BaseEngine tmpEngine = tZGDObject.createEngineProcess(currentOrgId, "TZGD_QF_MS_AE");
				// 指定调度作业的相关参数
				EngineParameters schdProcessParameters = new EngineParameters();

				schdProcessParameters.setBatchServer("");
				schdProcessParameters.setCycleExpression("");
				schdProcessParameters.setLoginUserAccount(currentAccountId);
				schdProcessParameters.setPlanExcuteDateTime(new Date());
				schdProcessParameters.setRunControlId(runCntlId);

				// 调度作业
				tmpEngine.schedule(schdProcessParameters);
				returnmap.replace("success", "短信已发送");
				
			} catch (Exception e) {
			}
			
		}
		return jacksonUtil.Map2json(returnmap);
		
	}
}
