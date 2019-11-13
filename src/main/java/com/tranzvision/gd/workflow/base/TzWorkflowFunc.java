package com.tranzvision.gd.workflow.base;

import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import org.apache.commons.lang.StringUtils;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 工作流通用方法函数
 * @author zhanglang
 * 2019年1月21日
 *
 */
public class TzWorkflowFunc {

	private SqlQuery sqlQuery;
	
	
	/**
	 * 构造方法
	 */
	public TzWorkflowFunc(){
		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		sqlQuery = (SqlQuery) getSpringBeanUtil.getAutowiredSpringBean("SqlQuery");
	}
	
	
	
	/**
	 * 获取指定条件动作前续步骤
	 * @param s_BusProcessID	业务流程编号
	 * @param s_conditionID		条件编号
	 * @return
	 */
	private Set<String> getBeforeStepByConditionId(String s_BusProcessID, String s_conditionID, Set<String> preStpSet){
		
		List<Map<String,Object>> conActList = sqlQuery.queryForList("select tzms_actcon_flg,tzms_stp_sta_uniqueid,tzms_cond_sta_uniqueid from tzms_wf_actcls_t where tzms_wfl_uniqueid=? and tzms_cond_end_uniqueid=?", 
				new Object[]{ s_BusProcessID, s_conditionID });
		if(conActList != null && conActList.size() > 0){
			for(Map<String,Object> conActMap: conActList) {
				//启用路由条件
				boolean l_actconFlg = conActMap.get("tzms_actcon_flg") == null ? false : (boolean) conActMap.get("tzms_actcon_flg");
				
				if(l_actconFlg == true){
					/*条件路由*/
					String l_condID = conActMap.get("tzms_cond_sta_uniqueid").toString();
					
					Set<String> stpIdSet = this.getBeforeStepByConditionId(s_BusProcessID, l_condID, preStpSet);
					if(stpIdSet.size() > 0){
						preStpSet.addAll(stpIdSet);
					}
				}else{
					/*步骤路由*/
					String l_stepID = conActMap.get("tzms_stp_sta_uniqueid").toString();
					if(preStpSet.contains(l_stepID)) {
						continue;
					}
					
					preStpSet.add(l_stepID);
					//继续检查上一步骤
					Set<String> stpIdSet = this.getBeforeStepByStepID(s_BusProcessID, l_stepID, preStpSet);
					if(stpIdSet.size() > 0){
						preStpSet.addAll(stpIdSet);
					}
				}
			}
		}
		
		return preStpSet;
	}
	
	
	
	
	/**
	 * 获取指定条件动作后续步骤
	 * @param s_BusProcessID	业务流程编号
	 * @param s_conditionID		条件编号
	 * @return
	 */
	private Set<String> getAfterStepByConditionId(String s_BusProcessID, String s_conditionID, Set<String> afterStpSet){
		
		List<Map<String,Object>> afterActList = sqlQuery.queryForList("select tzms_endact_flg,tzms_is_condition,tzms_stp_end_uniqueid,tzms_cond_end_uniqueid from tzms_wf_actcls_t where tzms_wfl_uniqueid=? and tzms_cond_sta_uniqueid=?", 
				new Object[]{ s_BusProcessID, s_conditionID });
		if(afterActList != null && afterActList.size() > 0){
			for(Map<String,Object> afterActMap: afterActList) {
				//是否结束动作
				boolean l_endFlg = afterActMap.get("tzms_endact_flg") == null ? false : (boolean) afterActMap.get("tzms_endact_flg");
				//是否进入条件判断
				boolean l_isConditFlg = afterActMap.get("tzms_is_condition") == null ? false : (boolean) afterActMap.get("tzms_is_condition");
				
				if(l_endFlg == true) {
					continue;
				}
				
				if(l_isConditFlg == true){
					/*条件路由*/
					String l_condID = afterActMap.get("tzms_cond_end_uniqueid").toString();
					
					Set<String> stpIdSet = this.getAfterStepByConditionId(s_BusProcessID, l_condID, afterStpSet);
					if(stpIdSet.size() > 0){
						afterStpSet.addAll(stpIdSet);
					}
				}else{
					/*步骤路由*/
					String l_stepID = afterActMap.get("tzms_stp_end_uniqueid").toString();
					if(afterStpSet.contains(l_stepID)) {
						continue;
					}
					
					afterStpSet.add(l_stepID);
					//继续检查上一步骤
					Set<String> stpIdSet = this.getAfterStepByStepID(s_BusProcessID, l_stepID, afterStpSet);
					if(stpIdSet.size() > 0){
						afterStpSet.addAll(stpIdSet);
					}
				}
			}
		}
		
		return afterStpSet;
	}
	
	
	
	
	/**
	 * 获取指定步骤的所有前序步骤
	 * @param s_BusProcessID	业务流程编号
	 * @param s_StpID			步骤编号
	 * @return 前序步骤集合
	 */
	private Set<String> getBeforeStepByStepID(String s_BusProcessID, String s_StpID, Set<String> preStpSet){
		if(preStpSet == null) {
			preStpSet = new HashSet<String>();
		}

		//查询前续步骤
		List<Map<String,Object>> preActList = sqlQuery.queryForList("select tzms_actcon_flg,tzms_stp_sta_uniqueid,tzms_cond_sta_uniqueid from tzms_wf_actcls_t where tzms_wfl_uniqueid=? and tzms_stp_end_uniqueid=?", 
				new Object[]{ s_BusProcessID, s_StpID });
		
		for(Map<String,Object> preActMap: preActList){
			//启用路由条件
			boolean l_actconFlg = preActMap.get("tzms_actcon_flg") == null ? false : (boolean) preActMap.get("tzms_actcon_flg");
			
			if(l_actconFlg == true){
				/*条件路由*/
				String l_conditionID = preActMap.get("tzms_cond_sta_uniqueid").toString();
				
				Set<String> stpIdSet = this.getBeforeStepByConditionId(s_BusProcessID, l_conditionID, preStpSet);
				if(stpIdSet.size() > 0){
					preStpSet.addAll(stpIdSet);
				}
			}else{
				/*步骤路由*/
				String l_stepID = preActMap.get("tzms_stp_sta_uniqueid").toString();
				if(preStpSet.contains(l_stepID)) {
					continue;
				}
				
				preStpSet.add(l_stepID);
				//继续检查上一步骤
				Set<String> stpIdSet = this.getBeforeStepByStepID(s_BusProcessID, l_stepID, preStpSet);
				if(stpIdSet.size() > 0){
					preStpSet.addAll(stpIdSet);
				}
			}
		}
		
		return preStpSet;
	}
	
	

	/**
	 * 获取指定步骤的所有后序步骤
	 * @param s_BusProcessID	业务流程编号
	 * @param s_StpID			步骤编号
	 * @return 后序步骤集合
	 */
	private Set<String> getAfterStepByStepID(String s_BusProcessID, String s_StpID, Set<String> afterStpSet){
		if(afterStpSet == null) {
			afterStpSet = new HashSet<String>();
		}

		//查询后续步骤
		List<Map<String,Object>> afterActList = sqlQuery.queryForList("select tzms_endact_flg,tzms_is_condition,tzms_stp_end_uniqueid,tzms_cond_end_uniqueid from tzms_wf_actcls_t where tzms_wfl_uniqueid=? and tzms_stp_sta_uniqueid=?", 
				new Object[]{ s_BusProcessID, s_StpID });
		
		for(Map<String,Object> afterActMap: afterActList){
			//是否结束动作
			boolean l_endFlg = afterActMap.get("tzms_endact_flg") == null ? false : (boolean) afterActMap.get("tzms_endact_flg");
			//是否进入条件判断
			boolean l_isConditFlg = afterActMap.get("tzms_is_condition") == null ? false : (boolean) afterActMap.get("tzms_is_condition");
			
			if(l_endFlg == true) {
				continue;
			}
			
			if(l_isConditFlg == true){
				/*进入条件判断*/
				String l_conditionID = afterActMap.get("tzms_cond_end_uniqueid").toString();
				
				Set<String> stpIdSet = this.getAfterStepByConditionId(s_BusProcessID, l_conditionID, afterStpSet);
				if(stpIdSet.size() > 0){
					afterStpSet.addAll(stpIdSet);
				}
			}else{
				/*下一步骤*/
				String l_stepID = afterActMap.get("tzms_stp_end_uniqueid").toString();
				if(afterStpSet.contains(l_stepID)) {
					continue;
				}
				
				afterStpSet.add(l_stepID);
				//继续检查下一步骤
				Set<String> stpIdSet = this.getAfterStepByStepID(s_BusProcessID, l_stepID, afterStpSet);
				if(stpIdSet.size() > 0){
					afterStpSet.addAll(stpIdSet);
				}
			}
		}
		
		return afterStpSet;
	}
	
	
	
	
	/**
	 * 获取业务流程指定步骤的所有前续步骤
	 * @param s_BusProcessID
	 * @param s_StpID
	 * @return
	 */
	public Set<String> getAllBeforeStepByStepID(String s_BusProcessID, String s_StpID){
		Set<String> beforeStpSet = this.getBeforeStepByStepID(s_BusProcessID, s_StpID, null);
		if(beforeStpSet.contains(s_StpID)) {
			beforeStpSet.remove(s_StpID);
		}
		return beforeStpSet;
	}
	
	
	/**
	 * 获取业务流程指定步骤的所有后续步骤
	 * @param s_BusProcessID
	 * @param s_StpID
	 * @return
	 */
	public Set<String> getAllAfterStepByStepID(String s_BusProcessID, String s_StpID){
		Set<String> afterStpSet = this.getAfterStepByStepID(s_BusProcessID, s_StpID, null);
		if(afterStpSet.contains(s_StpID)) {
			afterStpSet.remove(s_StpID);
		}
		return afterStpSet;
	}
	
	
	
	
	
	/**
	 * 判断工作流任务退回步骤是否流转过
	 * @param wflInsId
	 * @param stpInsID
	 * @param s_stpID
	 * @return
	 */
	public boolean checkStpidInWorkflowStep(String wflInsId, String stpInsID, String s_stpID){
		boolean checkOK = false;
		
		List<Map<String,Object>> l_PreStpInsList = sqlQuery.queryForList("select tzms_stpinsid from tzms_actins_tbl where tzms_wflinsid=? and tzms_nextstpinsid=?", 
				new Object[]{ wflInsId, stpInsID });
		if(l_PreStpInsList != null && l_PreStpInsList.size() > 0){
			for(Map<String,Object> l_PreStpInsMap: l_PreStpInsList){
				
				String l_PreStpInsID = l_PreStpInsMap.get("tzms_stpinsid").toString();
				
				String stpExists = sqlQuery.queryForObject("select 'Y' from tzms_stpins_tbl where tzms_wflinsid=? and tzms_stpinsid=? and tzms_wflstpid=?", 
						new Object[]{ wflInsId, l_PreStpInsID, s_stpID }, "String");
				if("Y".equals(stpExists)){

					return true;
				}else{
					checkOK = this.checkStpidInWorkflowStep(wflInsId, l_PreStpInsID, s_stpID);
				}
				
				if(checkOK){
					break;
				}
			}
		}
		
		return checkOK;
	}
	
	
	
	/**
	 * 获取可退回的步骤
	 * @param wflInsId		流程实例ID
	 * @param currBackStpId		当前要退回的步骤
	 * @param stpInsID		步骤实例ID
	 * @param backSteps		返回退回步骤
	 * @return
	 */
	public List<String> getCanBackSteps(String wflInsId, String currBackStpId, String stpInsID, Set<String> containsStps, List<String> backSteps){
		if(backSteps == null) {
			backSteps = new ArrayList<String>();
		}
		
		if(containsStps == null) {
			//业务流程定义ID
			String s_BusProcessID = sqlQuery.queryForObject("select tzms_wfcldn_uniqueid from tzms_wflins_tbl where tzms_wflinsid=?", 
					new Object[] { wflInsId }, "String");
			//获取所有前续步骤
			containsStps = this.getAllBeforeStepByStepID(s_BusProcessID, currBackStpId);
		}
		
		List<Map<String,Object>> l_PreStpInsList = sqlQuery.queryForList("select tzms_stpinsid from tzms_actins_tbl where tzms_wflinsid=? and tzms_nextstpinsid=?", 
				new Object[]{ wflInsId, stpInsID });
		if(l_PreStpInsList != null && l_PreStpInsList.size() > 0){
			for(Map<String,Object> l_PreStpInsMap: l_PreStpInsList){
				
				String l_PreStpInsID = l_PreStpInsMap.get("tzms_stpinsid").toString();
				
				//步骤ID
				String s_stpID = sqlQuery.queryForObject("select tzms_wflstpid from tzms_stpins_tbl where tzms_wflinsid=? and tzms_stpinsid=?", 
						new Object[]{ wflInsId, l_PreStpInsID }, "String");
				
				if(StringUtils.isNotBlank(s_stpID)){
					if(!backSteps.contains(s_stpID) && !s_stpID.equals(currBackStpId) && containsStps.contains(s_stpID)) {
						backSteps.add(s_stpID);
					}
					
					backSteps = this.getCanBackSteps(wflInsId, currBackStpId, l_PreStpInsID, containsStps, backSteps);
				}
			}
		}
		
		return backSteps;
	}
	
	

	
	
	/**
	 * 获取教职员联系信息
	 * @param userId	用户ID
	 * @return
	 */
	public Map<String,String> getTeaContactInfoByUserId(String userId){
		Map<String,String> contactMap = new HashMap<String,String>();
		//根据用户id查询教职员 
		Map<String,Object> teaMap = sqlQuery.queryForMap("select tzms_tea_defn_tId,tzms_name,tzms_oprid,tzms_domain_zhid from tzms_tea_defn_tBase where statecode='0' and CAST(tzms_user_uniqueid as varchar(36))=?", 
				new Object[] { userId });
		
		String strEmail = "";
		String strPhone = ""; 
		if(teaMap != null) {
			String teaDefnId = teaMap.get("tzms_tea_defn_tId").toString();
			String saifId = teaMap.get("tzms_domain_zhid") == null ? "" : teaMap.get("tzms_domain_zhid").toString();
			String name = teaMap.get("tzms_name") == null ? "" : teaMap.get("tzms_name").toString();
			String oprid = teaMap.get("tzms_oprid") == null ? "" : teaMap.get("tzms_oprid").toString();
			
			//查询教职员邮箱
			strEmail = sqlQuery.queryForObject("select top 1 tzms_email_addr from tzms_tea_email_t where tzms_person_id=? order by tzms_main_flg desc,tzms_email_type asc", 
					new Object[] { teaDefnId }, "String");

			//查询教职员电话
			strPhone = sqlQuery.queryForObject("select top 1 tzms_phone from tzms_tea_phone_t where tzms_person_id=? order by tzms_main_flg desc,tzms_phone_type asc", 
					new Object[] { teaDefnId }, "String");
			
			contactMap.put("saifId", saifId);
			contactMap.put("oprid", oprid);
			contactMap.put("name", name);
			contactMap.put("email", strEmail);
			contactMap.put("phone", strPhone);
		}else {
			contactMap = null;
		}
		
		return contactMap;
	}
	
	
	/**
	 * 查询教职员所属机构
	 * @param defnID	教职员ID
	 * @return	返回机构描述，多个机构用逗号分隔
	 */
	public String getTeaAllOrgDescr(String defnID) {
		//查询所属组织机构
		String orgName = "";
		List<Map<String,Object>> deptList = sqlQuery.queryForList("select org.tzms_org_name from tzms_org_structure_tree_t org,tzms_org_user_t ou where org.tzms_org_structure_tree_tid=ou.tzms_org_uniqueid and ou.tzms_tea_defnid=?", 
				new Object[] { defnID });
		if(deptList != null && deptList.size() > 0) {
			for(Map<String,Object> deptMap: deptList) {
				String deptName = (String)deptMap.get("tzms_org_name");
				orgName += "".equals(orgName) ? deptName : ("，" + deptName);
			}
		}
		
		return orgName;
	}
	
	
	
	/**
	 * 获取工作流流转信息
	 * @param wflInsId	工作流实例ID
	 * @return	序号、流转步骤、签名、时间、处理意见、处理状态
	 */
	public List<Map<String,String>> getWorkflowTransferInfo(String wflInsId){
		
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		
		List<Map<String, Object>> processList = sqlQuery.queryForList("select tzms_stpinsid,tzms_wflstpname,tzms_stpproid,tzms_stpenddt,tzms_tskprorec,tzms_stpinsstat_descr from TZ_WF_PROCESS_VW WHERE tzms_wflinsid=? order by tzms_stpstartdt", 
				new Object[]{ wflInsId });
		
		if (processList!=null && processList.size() > 0) {
			for (Map<String,Object> processMap : processList) {
				Map<String,String> map = new HashMap<String,String>();
				
				String tzms_stpinsid = processMap.get("tzms_stpinsid").toString();
				String tzms_wflstpname = processMap.get("tzms_wflstpname") == null ? "" : processMap.get("tzms_wflstpname").toString();
				//步骤实例签收责任人
				String tzms_stpproid = processMap.get("tzms_stpproid") == null ? "" : processMap.get("tzms_stpproid").toString();
				String tzms_tskprorec = processMap.get("tzms_tskprorec") == null ? "" : processMap.get("tzms_tskprorec").toString();
				String tzms_stpenddt = processMap.get("tzms_stpenddt") == null ? "" : processMap.get("tzms_stpenddt").toString();
				String tzms_stpinsstat_descr = processMap.get("tzms_stpinsstat_descr") == null ? "" : processMap.get("tzms_stpinsstat_descr").toString();
				
				
				String stpOprName = "";
				if(StringUtils.isNotBlank(tzms_stpproid)) {
					stpOprName = sqlQuery.queryForObject("select top 1 tzms_name from tzms_tea_defn_t where tzms_user_uniqueid=?", 
							new Object[] { tzms_stpproid }, "String");
				}else {
					//查找责任人表
					List<Map<String,Object>> useList = sqlQuery.queryForList("select tzms_name from tzms_acclst_tbl a join tzms_tea_defn_t b on(a.tzms_stpproid=CAST(b.tzms_user_uniqueid AS VARCHAR(36))) where tzms_stpinsid=?", 
							new Object[] { tzms_stpinsid });
					if(useList != null && useList.size() > 0) {
						for(Map<String,Object> useMap : useList) {
							if(useMap.get("tzms_name") != null) {
								if("".equals(stpOprName)) {
									stpOprName += useMap.get("tzms_name").toString();
								}else {
									stpOprName += "，" + useMap.get("tzms_name").toString();
								}
							}
						}
					}
				}
				
				map.put("stpInsId", tzms_stpinsid);			//步骤实例ID
				map.put("stepName", tzms_wflstpname);		//流转步骤
				map.put("personName", stpOprName);			//签名
				map.put("endTime", tzms_stpenddt);			//时间
				map.put("suggest", tzms_tskprorec);			//处理意见
				map.put("status", tzms_stpinsstat_descr);	//处理状态
				list.add(map);
			}
		}
		
		return list;
	}
	
	
	
	
	/**
	 * 获取流程当前流转步骤
	 * @param wflInsId
	 * @param returnType	ID - 返回步骤ID， NAME - 返回步骤名称
	 * @return
	 */
	public List<String> getWorkflowCurrentStep(String wflInsId, String returnType) {
		List<String> stpList = new ArrayList<String>();
		//查询当前流程流转节点
		List<String> currStpList = sqlQuery.queryForList("select distinct tzms_wflstpid from tzms_stpins_tbl A where tzms_wflinsid=? and tzms_asgmethod not in('1') and (tzms_stpinsstat in('1','3','6','8') " + 
				"or exists(select 'Y' from tzms_actins_tbl where tzms_wflinsid=A.tzms_wflinsid and tzms_stpinsid=A.tzms_stpinsid and tzms_nextstpinsid='' and tzms_actinsstate='WAT') " +
				"or exists(select 'Y' from tzms_actins_tbl B,tzms_wf_actcls_t C,tzms_wflins_tbl D where B.tzms_wflinsid=A.tzms_wflinsid and B.tzms_stpinsid=A.tzms_stpinsid and B.tzms_wflinsid=D.tzms_wflinsid and B.tzms_actclsid=cast(C.tzms_wf_actcls_tid as nvarchar(36)) and C.tzms_endact_flg=1 and D.tzms_wflstatus in('0','1','2')) " +
				"or (tzms_stpinsstat='5' and not exists(select 'Y' from tzms_wflins_tbl B where B.tzms_wflinsid=A.tzms_wflinsid and tzms_wflstatus in('3','4','5'))))",
				new Object[] { wflInsId }, "String");
		if(currStpList == null) {
			currStpList = new ArrayList<String>();
		}
		
		if("ID".equalsIgnoreCase(returnType)) {
			return currStpList;
		}else {
			if(currStpList != null && currStpList.size() > 0) {
				for(String stpid: currStpList) {
					String stpName = sqlQuery.queryForObject("select tzms_wflstpname from tzms_wflstp_t where tzms_wflstp_tid=?", 
							new Object[] { stpid }, "String");
					
					if(StringUtils.isNotBlank(stpName)) {
						List<Map<String,Object>> oprList = sqlQuery.queryForList("select tzms_stpinsid,tzms_stpinsstat,tzms_stpproid from tzms_stpins_tbl A where tzms_wflinsid=? and tzms_wflstpid=? and tzms_asgmethod not in('1') and (tzms_stpinsstat in('1','3','6','8') " + 
								"or exists(select 'Y' from tzms_actins_tbl where tzms_wflinsid=A.tzms_wflinsid and tzms_stpinsid=A.tzms_stpinsid and tzms_nextstpinsid='' and tzms_actinsstate='WAT') " + 
								"or exists(select 'Y' from tzms_actins_tbl B,tzms_wf_actcls_t C,tzms_wflins_tbl D where B.tzms_wflinsid=A.tzms_wflinsid and B.tzms_stpinsid=A.tzms_stpinsid and B.tzms_wflinsid=D.tzms_wflinsid and B.tzms_actclsid=cast(C.tzms_wf_actcls_tid as nvarchar(36)) and C.tzms_endact_flg=1 and D.tzms_wflstatus in('0','1','2')) " + 
								"or (tzms_stpinsstat='5' and not exists(select 'Y' from tzms_wflins_tbl B where B.tzms_wflinsid=A.tzms_wflinsid and tzms_wflstatus in('3','4','5'))))",
								new Object[] { wflInsId, stpid });
						if(oprList != null && oprList.size() > 0) {
							//步骤责任人
							List<String> userNames = new ArrayList<String>();
							for(Map<String, Object> oprMap: oprList) {
								String tzms_stpinsid = oprMap.get("tzms_stpinsid").toString();
								String tzms_stpinsstat = oprMap.get("tzms_stpinsstat").toString();
								String tzms_stpproid = oprMap.get("tzms_stpproid") == null ? "" : oprMap.get("tzms_stpproid").toString();
								
								if(!"6".equals(tzms_stpinsstat) && StringUtils.isNotBlank(tzms_stpproid)) {
									String name = sqlQuery.queryForObject("select top 1 tzms_name from tzms_tea_defn_tBase where tzms_user_uniqueid=?", new Object[] { tzms_stpproid }, "String");
									if(StringUtils.isNotBlank(name) && !userNames.contains(name)) {
										userNames.add(name);
									}
								}else {
									List<String> nameList = sqlQuery.queryForList("select distinct tzms_name from tzms_acclst_tbl A join tzms_tea_defn_t B on(A.tzms_stpproid=cast(B.tzms_user_uniqueid as nvarchar(36))) where tzms_stpinsid=?", 
											new Object[] { tzms_stpinsid }, "String");
									if(nameList != null && nameList.size() > 0) {
										userNames.addAll(nameList);
									}
								}
							}
							
							//去重
							userNames = userNames.stream().distinct().collect(Collectors.toList());
							if(userNames.size() > 0) {
								stpName += "["+ StringUtils.join(userNames, "、") +"]";
							}
						}
						stpList.add(stpName);
					}
				}
			}
			return stpList;
		}
	}
}
