package com.tranzvision.gd.workflow.method;

import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.base.TzException;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
import com.tranzvision.gd.util.sql.type.TzRecord;
import org.apache.commons.lang.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *  EE内训项目合同审批通过后同步付款缴费计划
 * @author zhanglang
 * 2019年7月11日
 *
 */
public class TzProjectContractPaymentPlanSync {

	private SqlQuery sqlQuery;
	
	private TZGDObject tzGDObject;
	
	private GetSeqNum getSeqNum;
	
	
	public TzProjectContractPaymentPlanSync() {
		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		sqlQuery = (SqlQuery) getSpringBeanUtil.getAutowiredSpringBean("SqlQuery");
		tzGDObject = (TZGDObject) getSpringBeanUtil.getAutowiredSpringBean("TZGDObject");
		getSeqNum = (GetSeqNum) getSpringBeanUtil.getAutowiredSpringBean("GetSeqNum");
	}
	
	
	
	
	/**
	 * 	同步合同缴费计划
	 * @param wflInsId	工作流实例ID
	 * @param stpInsId	步骤实例ID
	 * @param wflRecId	业务数据ID
	 * @param eventId	事件ID
	 */
	public void syncContractPaymentPlan(String wflInsId, String stpInsId, String wflRecId, String eventId) {
		
		//流程责任人oprid
		String oprid = sqlQuery.queryForObject("select top 1 tzms_oprid from tzms_tea_defn_tBase where CAST(tzms_user_uniqueid as varchar(36))=(select tzms_stpproid from tzms_stpins_tbl where tzms_wflinsid=? and tzms_stpinsid=?)",
						new Object[] { wflInsId, stpInsId }, "String");
				
		Map<String,Object> xmMap = sqlQuery.queryForMap("select tzms_name,tzms_agreement_unique,tzms_item_num from tzms_approval_tBase where tzms_approval_tId=?", 
				new Object[] { wflRecId });
				
		if(xmMap != null) {
			//项目名称
			String xmName = xmMap.get("tzms_name") == null ? "" : xmMap.get("tzms_name").toString();
			//合同ID
			String contractId = xmMap.get("tzms_agreement_unique") == null ? "" : xmMap.get("tzms_agreement_unique").toString();
			//立项编号
			String lxID = xmMap.get("tzms_item_num") == null ? "" : xmMap.get("tzms_item_num").toString();
			
			if(StringUtils.isNotBlank(contractId) 
					&& StringUtils.isNotBlank(lxID)) {
				
				List<Map<String,Object>> planList = sqlQuery.queryForList("select tzms_fk_jine,tzms_fk_date,tzms_notes from tzms_nxxm_fkjh_def_tBase where tzms_nxht_def_uniqueid=?", 
						new Object[] { contractId });
				
				if(planList != null && planList.size() > 0) {
					//先删除立项合同付款计划
					sqlQuery.update("delete from TZ_JF_PLAN_T where TZ_OBJ_TYPE='2' and TZ_OBJ_PRJ_ID=?", new Object[] { lxID });
					
					for(Map<String,Object> planMap : planList) {
						try {
							double amount = planMap.get("tzms_fk_jine") == null ? 0  : (double) planMap.get("tzms_fk_jine");
							Date fkData = planMap.get("tzms_fk_date") == null ? null  : (Date) planMap.get("tzms_fk_date");
							String notes = planMap.get("tzms_notes") == null ? "" : planMap.get("tzms_notes").toString();
							
							TzRecord tmp_jfPlanRecord = tzGDObject.createRecord("TZ_JF_PLAN_T");
							
							String jfplId = "0000000000" + getSeqNum.getSeqNum("TZ_JF_PLAN_T", "TZ_JFPL_ID");
							jfplId = jfplId.substring(jfplId.length() - 8);
							
							tmp_jfPlanRecord.setColumnValue("TZ_JFPL_ID", jfplId);
							tmp_jfPlanRecord.setColumnValue("TZ_OBJ_ID", lxID);
							tmp_jfPlanRecord.setColumnValue("TZ_OBJ_TYPE", "2");	//内训项目
							tmp_jfPlanRecord.setColumnValue("TZ_OBJ_PRJ_ID", lxID);
							tmp_jfPlanRecord.setColumnValue("TZ_OBJ_NAME", xmName);
							tmp_jfPlanRecord.setColumnValue("TZ_JF_TYPE", "2");		//学费
							tmp_jfPlanRecord.setColumnValue("TZ_JF_DATE", fkData);
							tmp_jfPlanRecord.setColumnValue("TZ_JF_BZ_JE", amount);
							tmp_jfPlanRecord.setColumnValue("TZ_JF_TZ_JE", 0);
							tmp_jfPlanRecord.setColumnValue("TZ_JF_JM_JE", 0);
							tmp_jfPlanRecord.setColumnValue("TZ_JF_BQYS", amount);
							tmp_jfPlanRecord.setColumnValue("TZ_JF_BQYT", 0);
							tmp_jfPlanRecord.setColumnValue("TZ_JF_BQSS", 0);
							tmp_jfPlanRecord.setColumnValue("TZ_JF_BQYJ", 0);
							tmp_jfPlanRecord.setColumnValue("TZ_JF_STAT", "1");
							tmp_jfPlanRecord.setColumnValue("TZ_REMARKS", notes);
							tmp_jfPlanRecord.setColumnValue("ROW_ADDED_OPRID", oprid);
							tmp_jfPlanRecord.setColumnValue("ROW_ADDED_DTTM", new Date());
							tmp_jfPlanRecord.setColumnValue("ROW_LASTMANT_OPRID", oprid);
							tmp_jfPlanRecord.setColumnValue("ROW_LASTMANT_DTTM", new Date());
							
							tmp_jfPlanRecord.insert();
							
						} catch (TzException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}		
	}
}
