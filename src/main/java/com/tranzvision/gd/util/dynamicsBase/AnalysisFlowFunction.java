package com.tranzvision.gd.util.dynamicsBase;

import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.workflow.base.TzWorkflowFunc;

import java.util.Map;
import java.util.Set;

/**
 * ClassName: AnalysisFlowFunction
 * @author zhongcg 
 * @version 1.0 
 * Create Time: 2019年1月21日 上午9:52:31  
 * Description: dynamics工作流 流程功能解析
 */
public class AnalysisFlowFunction {
	
	private SqlQuery jdbcTemplate;
	
	

	public AnalysisFlowFunction() {
		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		jdbcTemplate = (SqlQuery) getSpringBeanUtil.getAutowiredSpringBean("SqlQuery");
	}
	
	

   /**
	* 
	* Description:流程直送功能解析
	* Create Time: 2019年1月21日 上午9:54:55
	* @author zhongcg 
	* @param tzms_wflinsid 业务流程实例ID
	* @return 
	*/
	public boolean analysisFlowZS(String tzms_wflinsid) {
		try {
			//查询关联实体
			String sql = "SELECT B.tzms_entity_name,A.tzms_wflrecord_uniqueid FROM tzms_wflins_tbl A,tzms_wfcldn_tBase B WHERE A.tzms_wfcldn_uniqueid = B.tzms_wfcldn_tid AND A.tzms_wflinsid=?";
			Map<String, Object> flowMap = jdbcTemplate.queryForMap(sql, new Object[] {tzms_wflinsid});
			
			//业务关联实体
			String tzms_entity_name = "";
			//是否开启流程直送功能
			boolean tzms_wfl_directflg = false;
			if(flowMap == null) {
				return false;
			} 
			tzms_entity_name = flowMap.get("tzms_entity_name") == null ? "" : flowMap.get("tzms_entity_name").toString();
			//业务实体ID
			String tzms_entity_Id = flowMap.get("tzms_wflrecord_uniqueid") == null ? null : flowMap.get("tzms_wflrecord_uniqueid").toString();
			
			if("".equals(tzms_entity_name)) {
				return false;
			}
			
			//查询是否开启流程直送功能
			sql = "SELECT tzms_wfl_directflg FROM " + tzms_entity_name + " where " + tzms_entity_name + "Id=?";
			Map<String,Object> directMap = jdbcTemplate.queryForMap(sql, new Object[] { tzms_entity_Id });
			if(directMap != null) {
				tzms_wfl_directflg = directMap.get("tzms_wfl_directflg") == null ? false : (boolean) directMap.get("tzms_wfl_directflg");
			}
			
			return tzms_wfl_directflg;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
   /**
	* 
	* Description: 自动同意功能解析
	* Create Time: 2019年1月21日 上午9:54:55
	* @author zhongcg 
	* @param tzms_wflinsid 业务流程实例ID
	* @param tzms_stpinsid 流程步骤实例ID
	* @return 
	*/
	public boolean analysisAutoAgree(String tzms_wflinsid, String tzms_stpinsid) {
		try {
			
			String sql = "SELECT A.tzms_wflstp_tId,A.tzms_dpyauto_agree,A.tzms_autoag_var_uniqueid FROM tzms_wflstp_tBase A,tzms_stpins_tbl B WHERE A.tzms_wflstp_tid = B.tzms_wflstpid AND B.tzms_wflinsid=? AND B.tzms_stpinsid=?";
			Map<String, Object> flowMap = jdbcTemplate.queryForMap(sql, new Object[] {tzms_wflinsid, tzms_stpinsid});
			//是否开启自动同意功能
			boolean tzms_dpyauto_agree = false;
			//自动同意条件
			String tzms_autoag_var_uniqueid = "";
			//步骤责任人
			String tzms_stpproid = "";
			if(flowMap == null) {
				return false;
			} 
			
			tzms_dpyauto_agree = flowMap.get("tzms_dpyauto_agree") == null ? false : Boolean.parseBoolean(flowMap.get("tzms_dpyauto_agree").toString());
			tzms_autoag_var_uniqueid = flowMap.get("tzms_autoag_var_uniqueid") == null ? "" : flowMap.get("tzms_autoag_var_uniqueid").toString();
			System.out.println("自动同意功能：" + tzms_dpyauto_agree);
			
			//从责任人表中查询步骤实例责任人
			tzms_stpproid = jdbcTemplate.queryForObject("select top 1 tzms_stpproid from tzms_acclst_tbl where tzms_stpinsid=?", 
					new Object[] { tzms_stpinsid }, "String");
			System.out.println("步骤责任人：" + tzms_stpproid); 
			
			//如果没有开启
			if(!tzms_dpyauto_agree) {
				return false;
			}
			
			
			//业务数据ID,业务流程ID
			String wflDateRecId = "";
			String busProcId = "";
			Map<String,Object> wflInsMap = jdbcTemplate.queryForMap("select tzms_wflrecord_uniqueid, tzms_wfcldn_uniqueid from tzms_wflins_tbl where tzms_wflinsid=?", 
					new Object[]{ tzms_wflinsid });
			if(wflInsMap != null) {
				wflDateRecId = wflInsMap.get("tzms_wflrecord_uniqueid") == null ? "" : wflInsMap.get("tzms_wflrecord_uniqueid").toString();
				busProcId = wflInsMap.get("tzms_wfcldn_uniqueid") == null ? "" : wflInsMap.get("tzms_wfcldn_uniqueid").toString();
			}			
			
			
			boolean flag = false;
			//如果没有配置条件，认为是不需要配置条件
			if("".equals(tzms_autoag_var_uniqueid)) {
				flag = true;
			}else {
				//解析系统变量：自动同意条件。
				AnalysisDynaSysVar analysisDynaSysVar = new AnalysisDynaSysVar();
				analysisDynaSysVar.setM_SysVarID(tzms_autoag_var_uniqueid);
				
				String[] param = { tzms_wflinsid, tzms_stpinsid, wflDateRecId };
				analysisDynaSysVar.setM_SysVarParam(param);
				
				try {
					flag = (boolean) analysisDynaSysVar.GetVarValue();
				}catch (Exception e) {
					flag = false;
				}
			}
			
			//步骤ID
			String wflStpId = flowMap.get("tzms_wflstp_tId").toString();
			
			TzWorkflowFunc tzWorkflowFunc = new TzWorkflowFunc();
			Set<String> set = tzWorkflowFunc.getAllBeforeStepByStepID(busProcId, wflStpId);
			
			boolean flag2 = false;
			//判断之前的步骤中是否有该负责人，并且步骤是正常结束的
			sql = "SELECT count(*) FROM tzms_stpins_tbl WHERE tzms_wflinsid=? AND tzms_wflstpid=? and tzms_stpproid=? AND tzms_stpinsstat='4' and tzms_asgmethod not in('1')";
			for (String stpId : set) {
				int num = jdbcTemplate.queryForObject(sql, new Object[] {tzms_wflinsid, stpId, tzms_stpproid}, "int");
				if(num > 0) {
					flag2 = true;
					break;
				}
			}
			
			if(flag && flag2) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
