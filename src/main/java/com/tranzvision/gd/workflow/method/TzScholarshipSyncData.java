package com.tranzvision.gd.workflow.method;

import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.base.TzException;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
import com.tranzvision.gd.util.sql.type.TzRecord;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 	奖学金政策审批通过同步奖学金相关表
 * @author zhanglang
 * 2019年7月9日
 *
 */
public class TzScholarshipSyncData {

	private SqlQuery sqlQuery;
	
	private TZGDObject tzGDObject;
	
	private GetSeqNum getSeqNum;
	
	
	public TzScholarshipSyncData() {
		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		sqlQuery = (SqlQuery) getSpringBeanUtil.getAutowiredSpringBean("SqlQuery");
		tzGDObject = (TZGDObject) getSpringBeanUtil.getAutowiredSpringBean("TZGDObject");
		getSeqNum = (GetSeqNum) getSpringBeanUtil.getAutowiredSpringBean("GetSeqNum");
	}
	
	
	
	/**
	 * 工作流事件 - 奖学金政审流程通过后同步
	 * @param wflInsId
	 * @param stpInsId
	 * @param wflRecId
	 * @param eventId
	 * @throws TzException
	 */
	public void syncScholarshipData(String wflInsId, String stpInsId, String wflRecId, String eventId) 
			throws TzException {
		
		System.out.println("------ 奖学金政审流程通过后同步开始执行--------->> "+ wflInsId + " --- " + stpInsId + " --- " + wflRecId + " --- " + eventId);
		try {
			//流程申请人oprid
			String oprid = sqlQuery.queryForObject("select top 1 tzms_oprid from tzms_tea_defn_tBase where tzms_user_uniqueid=(select tzms_stpproid from tzms_stpins_tbl where tzms_wflinsid=? and tzms_asgmethod='3')",
					new Object[] { wflInsId }, "String");
			
			Map<String,Object> policyMap = sqlQuery.queryForMap("select tzms_fellow_policy_approvalId,tzms_school_year,tzms_fellow_amount_percentage from tzms_fellow_policy_approvalBase where tzms_fellow_policy_approvalId=?", 
					new Object[] { wflRecId });
			if(policyMap != null) {
				//奖学金政策ID
				String approvalId = policyMap.get("tzms_fellow_policy_approvalId").toString();
				//学年信息
				String tzms_school_year = policyMap.get("tzms_school_year").toString();
				//奖学金整体比例
				Double percent = (Double) policyMap.get("tzms_fellow_amount_percentage");
				
				
				//奖学金政策明细
				List<Map<String,Object>> typeList = sqlQuery.queryForList("select tzms_name,tzms_fellow_amount,tzms_explain from tzms_fellow_policy_infoBase where tzms_fellow_policy_approval=? ", 
						new Object[] { approvalId });
				
				//奖学金所属项目
				List<Map<String,Object>> prjList = sqlQuery.queryForList("select tzms_name,tzms_project_type from tzms_jxj_protype_tBase where tzms_jxzzc_uniqueid=?", 
						new Object[] { approvalId });
				
				if(typeList != null && typeList.size() > 0 
						&& prjList != null && prjList.size() > 0) {
					
					/**************************************************
					 * 	同步奖学金控制表开始
					 **************************************************/
					for(Map<String,Object> prjMap : prjList) {
						String prjtype =  prjMap.get("tzms_project_type").toString();
						
						try {
							//创建奖学金控制表
							TzRecord tmp_JxjCtrRecord = tzGDObject.createRecord("TZ_JXJ_CTR_T");
							tmp_JxjCtrRecord.setColumnValue("TZ_XN_ID", tzms_school_year, true);
							tmp_JxjCtrRecord.setColumnValue("TZ_JXJ_PRJ_TYPE", prjtype, true);
							if(tmp_JxjCtrRecord.selectByKey() == true) {
								//总额占比
								tmp_JxjCtrRecord.setColumnValue("TZ_JXJ_ZEZB", percent);
								//创建时间
								tmp_JxjCtrRecord.setColumnValue("TZ_ADD_DT", new Date());
								
								tmp_JxjCtrRecord.update();
							}else {
								//学年
								tmp_JxjCtrRecord.setColumnValue("TZ_XN_ID", tzms_school_year);
								//所属项目分类
								tmp_JxjCtrRecord.setColumnValue("TZ_JXJ_PRJ_TYPE", prjtype);
								//总额占比
								tmp_JxjCtrRecord.setColumnValue("TZ_JXJ_ZEZB", percent);
								//创建时间
								tmp_JxjCtrRecord.setColumnValue("TZ_ADD_DT", new Date());
	
								tmp_JxjCtrRecord.insert();
							}
						}catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					
					/**************************************************
					 * 	同步奖学金类别定义表和奖学金定义表开始
					 **************************************************/
					for(Map<String,Object> typeMap : typeList) {
						//奖学金政策类别名称
						String typeName = typeMap.get("tzms_name") == null ? "" : typeMap.get("tzms_name").toString();
						//政审说明概要
						String explain = typeMap.get("tzms_explain") == null ? "" : typeMap.get("tzms_explain").toString();
						//类别金额
						Double typeAmount = (Double) typeMap.get("tzms_fellow_amount");
						
						for(Map<String,Object> prjMap : prjList) {
							
							String prjtype =  prjMap.get("tzms_project_type").toString();
							
							try {
								TzRecord tmp_JxjTypeRecord = tzGDObject.createRecord("TZ_JXJ_TYPE_T");
								
								//生成奖学金类别ID
								String jxjTypeID = "" + getSeqNum.getSeqNum("TZ_JXJ_TYPE_T", "TZ_JXJ_TYPE_ID");
								
								tmp_JxjTypeRecord.setColumnValue("TZ_JXJ_TYPE_ID", jxjTypeID);
								tmp_JxjTypeRecord.setColumnValue("TZ_JXJ_TYPE_NAME", typeName);
								tmp_JxjTypeRecord.setColumnValue("TZ_PRJ_TYPE", prjtype);
								tmp_JxjTypeRecord.setColumnValue("TZ_XN_ID", tzms_school_year);
								
								if(tmp_JxjTypeRecord.insert() == true) {
									if(typeAmount != null && typeAmount > 0) {
										//如果金额大于0，自动生成奖学金定义
										TzRecord tmp_JxjDefnRecord = tzGDObject.createRecord("TZ_JXJ_DEFN_T");
										
										tmp_JxjDefnRecord.setColumnValue("TZ_JXJ_ID", getSeqNum.getSeqNum("TZ_JXJ_DEFN_T", "TZ_JXJ_ID"));
										tmp_JxjDefnRecord.setColumnValue("TZ_JXJ_NAME", typeName);
										tmp_JxjDefnRecord.setColumnValue("TZ_JXJ_TYPE_ID", jxjTypeID);
										tmp_JxjDefnRecord.setColumnValue("TZ_JXJ_JE", typeAmount);
										tmp_JxjDefnRecord.setColumnValue("TZ_JXJ_YX_STAT", "1");
										tmp_JxjDefnRecord.setColumnValue("TZ_ADD_TYPE", "1");
										tmp_JxjDefnRecord.setColumnValue("TZ_NOTE", explain);
										tmp_JxjDefnRecord.setColumnValue("ROW_ADDED_OPRID", oprid);
										tmp_JxjDefnRecord.setColumnValue("ROW_ADDED_DTTM", new Date());
										tmp_JxjDefnRecord.setColumnValue("ROW_LASTMANT_OPRID", oprid);
										tmp_JxjDefnRecord.setColumnValue("ROW_LASTMANT_DTTM", new Date());
										tmp_JxjDefnRecord.insert();
									}
								}
							}catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}catch (Exception e) {
			throw new TzException("写入奖学金数据失败！", e);
		}
	}
}
