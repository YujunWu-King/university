package com.tranzvision.gd.TZZnxQfBundle.service.impl;

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
 * 站内信历史
 *
 */
@Service("com.tranzvision.gd.TZZnxQfBundle.service.impl.TzZnxViewHistoryClsServiceImpl")
public class TzZnxViewHistoryClsServiceImpl extends FrameworkImpl {
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
	

	// 邮件历史查看
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
		String znxQfId = jacksonUtil.getString("znxQfId");
		String str_TZ_MLSM_QFPC_ID = znxQfId;

		// 邮件或短信任务ID;
		String taskID = "";

		int num_total = 0;
		if ("history".equals(strStoreType)) {
			/* 模板ID，任务发送状态，总数，成功数，失败数，重复数，发送时间，操作人 */
			String TZ_RWZX_ZT_DESC = "", TZ_SEND_COUNT = "", TZ_SEND_SUC_COUNT = "", TZ_SEND_FAIL_COUNT = "",
					TZ_SEND_RPT_COUNT = "", TZ_RWZX_DT_STR = "", TZ_REALNAME = "";
			String TZ_RWZX_ZT = "";

			String sql_FSRW = "SELECT TZ_EML_SMS_TASK_ID FROM PS_TZ_DXYJFSRW_TBL WHERE TZ_MLSM_QFPC_ID=? ORDER BY CAST(TZ_EML_SMS_TASK_ID as SIGNED) DESC";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql_FSRW, new Object[] { str_TZ_MLSM_QFPC_ID });
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					taskID = (String) list.get(i).get("TZ_EML_SMS_TASK_ID");

					//String sql1 = "select TZ_TMPL_ID,TZ_RWZX_ZT,TZ_RWZX_ZT_DESC,TZ_SEND_COUNT,TZ_SEND_SUC_COUNT,TZ_SEND_FAIL_COUNT,TZ_SEND_RPT_COUNT,TZ_RWZX_DT_STR,TZ_REALNAME from PS_TZ_ZNX_HIS_TJ_V where TZ_EML_SMS_TASK_ID=? and TZ_JG_ID=?";
					String sql1 = "select `A`.`TZ_EML_SMS_TASK_ID` AS `TZ_EML_SMS_TASK_ID`,`A`.`TZ_JG_ID` AS `TZ_JG_ID`,`A`.`TZ_TMPL_ID` AS `TZ_TMPL_ID`,`A`.`TZ_RWZX_ZT` AS `TZ_RWZX_ZT`,(select `PS_TZ_PT_ZHZXX_TBL`.`TZ_ZHZ_DMS` from `PS_TZ_PT_ZHZXX_TBL` where ((`PS_TZ_PT_ZHZXX_TBL`.`TZ_ZHZJH_ID` = 'TZ_RWZX_ZT') and (`PS_TZ_PT_ZHZXX_TBL`.`TZ_ZHZ_ID` = `A`.`TZ_RWZX_ZT`) and (`PS_TZ_PT_ZHZXX_TBL`.`TZ_EFF_STATUS` = 'A'))) AS `TZ_RWZX_ZT_DESC`,(select count(1) from `PS_TZ_AUDCYUAN_T` where (`PS_TZ_AUDCYUAN_T`.`TZ_AUDIENCE_ID` = `A`.`TZ_AUDIENCE_ID`)) AS `TZ_SEND_COUNT`,(select count(1) from `PS_TZ_ZNXFSLSHI_TBL` where ((`PS_TZ_ZNXFSLSHI_TBL`.`TZ_EML_SMS_TASK_ID` = `A`.`TZ_EML_SMS_TASK_ID`) and (`PS_TZ_ZNXFSLSHI_TBL`.`TZ_FS_ZT` = 'SUC'))) AS `TZ_SEND_SUC_COUNT`,(select count(1) from `PS_TZ_ZNXFSLSHI_TBL` where ((`PS_TZ_ZNXFSLSHI_TBL`.`TZ_EML_SMS_TASK_ID` = `A`.`TZ_EML_SMS_TASK_ID`) and (`PS_TZ_ZNXFSLSHI_TBL`.`TZ_FS_ZT` = 'FAIL'))) AS `TZ_SEND_FAIL_COUNT`,(select count(1) from `PS_TZ_ZNXFSLSHI_TBL` where ((`PS_TZ_ZNXFSLSHI_TBL`.`TZ_EML_SMS_TASK_ID` = `A`.`TZ_EML_SMS_TASK_ID`) and (`PS_TZ_ZNXFSLSHI_TBL`.`TZ_FS_ZT` = 'RPT'))) AS `TZ_SEND_RPT_COUNT`,date_format(`A`.`TZ_RWTJ_DT`,'%Y-%m-%d %H:%i:%s') AS `TZ_RWZX_DT_STR`,`B`.`TZ_REALNAME` AS `TZ_REALNAME` from (`PS_TZ_DXYJFSRW_TBL` `A` left join `PS_TZ_AQ_YHXX_TBL` `B` on((`A`.`ROW_ADDED_OPRID` = `B`.`OPRID`))) WHERE A.TZ_EML_SMS_TASK_ID=? and A.TZ_JG_ID=?";
					Map<String, Object> map1 = jdbcTemplate.queryForMap(sql1, new Object[] { taskID, orgID });
					if (map1 != null) {
						num_total = num_total + 1;
						// TZ_TMPL_ID = map1.get("TZ_TMPL_ID") == null ? "" :
						// (String)map1.get("TZ_TMPL_ID");
						TZ_RWZX_ZT = map1.get("TZ_RWZX_ZT") == null ? "" : (String) map1.get("TZ_RWZX_ZT");
						TZ_RWZX_ZT_DESC = map1.get("TZ_RWZX_ZT_DESC") == null ? ""
								: (String) map1.get("TZ_RWZX_ZT_DESC");
						TZ_SEND_COUNT = String.valueOf(map1.get("TZ_SEND_COUNT"));
						TZ_SEND_SUC_COUNT = String.valueOf(map1.get("TZ_SEND_SUC_COUNT"));
						TZ_SEND_FAIL_COUNT = String.valueOf(map1.get("TZ_SEND_FAIL_COUNT"));
						TZ_SEND_RPT_COUNT = String.valueOf(map1.get("TZ_SEND_RPT_COUNT"));
						TZ_RWZX_DT_STR = map1.get("TZ_RWZX_DT_STR") == null ? "" : (String) map1.get("TZ_RWZX_DT_STR");
						TZ_REALNAME = map1.get("TZ_REALNAME") == null ? "" : (String) map1.get("TZ_REALNAME");

						Map<String, Object> reMap1 = new HashMap<>();
						reMap1.put("statusID", TZ_RWZX_ZT);
						reMap1.put("status", TZ_RWZX_ZT_DESC);
						reMap1.put("sendNum", TZ_SEND_COUNT);
						reMap1.put("sendSucNum", TZ_SEND_SUC_COUNT);
						reMap1.put("sendFailNum", TZ_SEND_FAIL_COUNT);
						reMap1.put("sendRptNum", TZ_SEND_RPT_COUNT);
						reMap1.put("sendDt", TZ_RWZX_DT_STR);
						reMap1.put("operator", TZ_REALNAME);
						reMap1.put("taskID", taskID);
						reMap1.put("znxQfId", znxQfId);

						returnArrayList.add(reMap1);

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
		
		if ("viewEachZnx".equals(type)) {
			strResultConten = this.viewEachZnx(comParams, errorMsg);
		}

		
		return strResultConten;
	}
	
	private String viewEachZnx(String comParams, String[] errorMsg){
		Map<String, Object> returnMap = new HashMap<>();
		returnMap.put("formData", ""); 
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);
		
	    // 发件箱，收件箱，邮件主题;
	    String OPRID = "", TZ_ZNX_ZT = "";
	    // 邮件发送日期，邮件正文;
	    String TZ_ZNX_ZHWEN = "";
	      
	    String rwsl_ID = jacksonUtil.getString("rwsl_ID");
	    String taskId = "";
	    
	    String senderName = "",znxSjr="";
	    
	    Map<String,Object> map = jdbcTemplate.queryForMap("select TZ_EML_SMS_TASK_ID,TZ_AUDCY_ID,OPRID,TZ_ZNX_ZT from PS_TZ_ZNXFSLSHI_TBL where TZ_RWSL_ID=?", new Object[]{rwsl_ID});
	    if (map != null){
	    	 taskId = map.get("TZ_EML_SMS_TASK_ID") == null ? "" : (String)map.get("TZ_EML_SMS_TASK_ID");
	    	 //audCyId = map.get("TZ_AUDCY_ID") == null ? "" :(String)map.get("TZ_AUDCY_ID");
	    	 OPRID = map.get("OPRID") == null ? "" :(String)map.get("OPRID");
	    	 TZ_ZNX_ZT = map.get("TZ_ZNX_ZT") == null ? "" :(String)map.get("TZ_ZNX_ZT");
	    	 
	    	 String senderOprid = jdbcTemplate.queryForObject("SELECT A.TZ_ZNX_SENDID FROM PS_TZ_ZNX_MSG_T A,PS_TZ_DXYJFSRW_TBL B WHERE A.TZ_MLSM_QFPC_ID=B.TZ_MLSM_QFPC_ID and B.TZ_EML_SMS_TASK_ID=?", new Object[]{taskId},"String");
	    	 
	    	 String sql = "select TZ_REALNAME from PS_TZ_AQ_YHXX_TBL where OPRID=?";
	    	 znxSjr = jdbcTemplate.queryForObject(sql, new Object[]{OPRID},"String");
	    	 if(znxSjr == null){
	    		 znxSjr = "";
	    	 }
	    	 senderName = jdbcTemplate.queryForObject(sql, new Object[]{senderOprid},"String");
	    	 if(senderName == null){
	    		 senderName = "";
	    	 }
	    }
	    
	
	    TZ_ZNX_ZHWEN = jdbcTemplate.queryForObject("SELECT TZ_ZNX_ZHWEN FROM PS_TZ_ZNXZWLSHI_TBL WHERE TZ_RWSL_ID=?", new Object[]{rwsl_ID},"String");
	    
	    Map<String, Object> rMap = new HashMap<>();
	    
	    rMap.put("senderName", senderName);
	    rMap.put("znxSjr", znxSjr);
	    rMap.put("znxTheme", TZ_ZNX_ZT);
	    rMap.put("znxContentHtml", TZ_ZNX_ZHWEN);
	    
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
			String runCntlId = "ZNX" + s_dtm + "_" + getSeqNum.getSeqNum("PSPRCSRQST", "RUN_ID");

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
