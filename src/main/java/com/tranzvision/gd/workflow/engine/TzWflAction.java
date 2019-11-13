package com.tranzvision.gd.workflow.engine;

import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.base.TzException;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
import com.tranzvision.gd.util.sql.type.TzRecord;
import com.tranzvision.gd.workflow.base.CommonFun;
import com.tranzvision.gd.workflow.base.TzStpActInfo;
import com.tranzvision.gd.workflow.base.TzWorkflowFunc;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 工作流动作
 * @author 张浪	2019-01-14
 *
 */
public class TzWflAction {

	private TZGDObject tzGDObject;
	
	private SqlQuery sqlQuery;
	
	
	//流程退回重填动作类编号
	private static final String WFL_RETURN_REFILL_ACTCLSID = "RETURN_REFILL_20190123";
	
	//流程退回某步动作类编号
	private static final String WFL_RETURN_STEP_ACTCLSID = "RETURN_STEP_20190123";
	
	//流程撤回动作类编号
	private static final String WFL_RETURN_BACK_ACTCLSID = "RETURN_BACK_20190123";
	
	
	
	private String m_CurrUserId;
	//业务流程定义编号
	private String m_BusProcessDefID;
	//工作流实例编号
	private String m_WflInstanceID;
	//步骤实例编号
	private String m_WflStpInsID;
	//步骤编号
	private String m_WflStpID;
	//流转事件异常信息
	private List<TzException> flowEventsException;
	//是否不发送通知
	private boolean isNotSendNotice = false;
	
	//记录日志
	private static final Logger logger = Logger.getLogger("WorkflowEngine");
	
	
	
	
	public String getM_BusProcessDefID() {
		return m_BusProcessDefID;
	}
	public String getM_WflInstanceID() {
		return m_WflInstanceID;
	}
	public String getM_WflStpInsID() {
		return m_WflStpInsID;
	}
	public String getM_WflStpID() {
		return m_WflStpID;
	}
	public List<TzException> getFlowEventsException() {
		return flowEventsException;
	}
	public void setNotSendNotice(boolean isNotSendNotice) {
		this.isNotSendNotice = isNotSendNotice;
	}
	
	
	
	
	
	/**
	 * 构造函数
	 * @param s_BusProcessDefID
	 * @param s_WflInstanceID
	 * @param s_WflStpInsID
	 * @param s_WflStpID
	 */
	public TzWflAction(String s_BusProcessDefID, String s_WflInstanceID, String s_WflStpInsID, String s_WflStpID, String UserId) {
		
		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		tzGDObject = (TZGDObject) getSpringBeanUtil.getAutowiredSpringBean("TZGDObject");
		sqlQuery = (SqlQuery) getSpringBeanUtil.getAutowiredSpringBean("SqlQuery");
		
		this.m_BusProcessDefID = s_BusProcessDefID;
		this.m_WflInstanceID = s_WflInstanceID;
		this.m_WflStpInsID = s_WflStpInsID;
		this.m_WflStpID = s_WflStpID;
		this.m_CurrUserId = UserId;
		
		flowEventsException = new ArrayList<TzException>();
	}
	

	
	
	/**
	 * 创建动作实例
	 * @param s_NextStepInsID	//下一步骤实例编号
	 * @param s_NextStpID		//下一步骤编号
	 * @param s_actClsID		//动作类编号
	 * @param s_ActStaCode		//动作实例状态
	 * @throws TzException
	 */
	private void CreateActionInstance(String s_NextStepInsID, String s_NextStpID, String s_actClsID, String s_ActStaCode) throws TzException {
		//1、检查输入的参数
		if(StringUtils.isEmpty(s_ActStaCode)){
			throw new TzException("【系统运行错误】输入参数不符合要求。");
		}
		//2、检查环境
		if(StringUtils.isEmpty(m_WflInstanceID) 
				|| StringUtils.isEmpty(m_WflStpInsID) 
				|| StringUtils.isEmpty(m_WflStpID)){
			throw new TzException("【系统运行错误】数据环境不符合要求。");
		}
		//3、创建动作实例编号
		CommonFun commonFun = new CommonFun();
		String l_actInsID = commonFun.GenerateGUID_1("ACTINS", 30);
		
		//4、创建动作实例
		TzRecord tmp_actIndRecord = tzGDObject.createRecord("tzms_actins_tbl");
		//动作实例ID
		tmp_actIndRecord.setColumnValue("tzms_actinsid", l_actInsID);
		//流转动作ID
		tmp_actIndRecord.setColumnValue("tzms_actclsid", s_actClsID);
		//流程实例ID
		tmp_actIndRecord.setColumnValue("tzms_wflinsid", m_WflInstanceID);
		//步骤实例ID
		tmp_actIndRecord.setColumnValue("tzms_stpinsid", m_WflStpInsID);
		//下一步步骤实例ID
		tmp_actIndRecord.setColumnValue("tzms_nextstpinsid", s_NextStepInsID);
		//下一步步骤ID
		tmp_actIndRecord.setColumnValue("tzms_nextstpid", s_NextStpID);
		//动作实例状态
		tmp_actIndRecord.setColumnValue("tzms_actinsstate", s_ActStaCode);

		if(tmp_actIndRecord.insert() == false){
			throw new TzException("【系统运行错误】创建动作实例失败。");
		}
		
		if(StringUtils.isNotBlank(s_NextStepInsID)) {
			//插入步骤实例流转关系tzms_stpins_pre_tbl
			TzRecord stpPre_TempRecord = tzGDObject.createRecord("tzms_stpins_pre_tbl");
			stpPre_TempRecord.setColumnValue("tzms_stpinsid", s_NextStepInsID);
			stpPre_TempRecord.setColumnValue("tzms_prestpinsid", m_WflStpInsID);
			stpPre_TempRecord.insert();
		}
		
		//5、若Action Status为"FIN"，则还要修改路由到同一NextStepID上的、Action State为"WAT"的其他Action Instance，将其Action Status设为"FIN" 
		if("FIN".equals(s_ActStaCode)){
			sqlQuery.update("update tzms_actins_tbl set tzms_actinsstate='FIN',tzms_nextstpinsid=? where tzms_wflinsid=? and tzms_nextstpid=? and tzms_actinsstate='WAT'", 
					new Object[]{ s_NextStepInsID, m_WflInstanceID, s_NextStpID });
		}
	}
	
	
	/**
	 * 复制动作实例
	 * @param s_FromStpInsId
	 * @param s_ToStpInsId
	 */
	private void CopyActionInstance(String s_FromStpInsId, String s_ToStpInsId){
		
		List<Map<String,Object>> actInsList = sqlQuery.queryForList("select tzms_actinsid from tzms_actins_tbl where tzms_nextstpinsid=? and tzms_actinsstate='FIN'", new Object[]{ s_FromStpInsId });
		for(Map<String,Object> actInsMap: actInsList){
			String l_actInsID = actInsMap.get("tzms_actinsid").toString();
			
			TzRecord ActIns_TempRecord = null;
			try {
				ActIns_TempRecord = tzGDObject.createRecord("tzms_actins_tbl");
				ActIns_TempRecord.setColumnValue("tzms_actinsid", l_actInsID, true);
				if(ActIns_TempRecord.selectByKey() == false){
					throw new TzException("获取工作流业务类别信息失败");
				}
				
				//创建动作实例编号
				CommonFun commonFun = new CommonFun();
				String new_actInsID = commonFun.GenerateGUID_1("ACTINS", 30);
				
				TzRecord new_ActIns_Record = tzGDObject.createRecord("tzms_actins_tbl");
				new_ActIns_Record.setColumnValue("tzms_actinsid", new_actInsID);
				new_ActIns_Record.setColumnValue("tzms_actclsid", ActIns_TempRecord.getTzString("tzms_actclsid").getValue());
				new_ActIns_Record.setColumnValue("tzms_wflinsid", ActIns_TempRecord.getTzString("tzms_wflinsid").getValue());
				new_ActIns_Record.setColumnValue("tzms_stpinsid", ActIns_TempRecord.getTzString("tzms_stpinsid").getValue());
				new_ActIns_Record.setColumnValue("tzms_nextstpid", ActIns_TempRecord.getTzString("tzms_nextstpid").getValue());
				new_ActIns_Record.setColumnValue("tzms_actinsstate", ActIns_TempRecord.getTzString("tzms_actinsstate").getValue());
				new_ActIns_Record.setColumnValue("tzms_nextstpinsid", s_ToStpInsId);
				
				new_ActIns_Record.insert();
				
			} catch (TzException e) {
				e.printStackTrace();
				logger.error("并发路由复制动作实例失败", e);
			}
		}
	}
	
	
	
	/**
	 * 路由条件检查
	 * @param s_BusProcessID
	 * @param l_conditionID
	 * @return
	 */
	private boolean checkConditionAction(String l_conditionID){
		boolean checkOK = true;
		
		Map<String,Object> conActMap = sqlQuery.queryForMap("select tzms_actcon_flg,tzms_stp_sta_uniqueid,tzms_cond_sta_uniqueid from tzms_wf_actcls_t where tzms_wfl_uniqueid=? and tzms_cond_end_uniqueid=?", 
				new Object[]{ m_BusProcessDefID, l_conditionID });
		if(conActMap != null){
			//启用路由条件
			boolean l_actconFlg = conActMap.get("tzms_actcon_flg") == null ? false : (boolean) conActMap.get("tzms_actcon_flg");
			
			if(l_actconFlg == true){
				/*条件路由*/
				String l_condID = conActMap.get("tzms_cond_sta_uniqueid").toString();
				
				checkOK = this.checkConditionAction(l_condID);
			}else{
				/*步骤路由*/
				String l_stepID = conActMap.get("tzms_stp_sta_uniqueid").toString();
				
				//查询该步骤是否有存活步骤实例，需要去除抄送实例
				/*1-激活、2-撤回、3-处理中、4-结束、5-提前终止、6-未签收、7-退回、8-已签收、9-转发*/
				int aliveStpInsCount = sqlQuery.queryForObject("select count(*) from tzms_stpins_tbl where tzms_wflinsid=? and tzms_wflstpid=? and tzms_stpinsid<>? and tzms_asgmethod<>'1' and tzms_stpinsstat in('1','3','6','8')", 
						new Object[]{ m_WflInstanceID, l_stepID, m_WflStpInsID }, "int");
				
				if(aliveStpInsCount > 0){
					return false;
				}
				
				//继续检查上一步骤
				checkOK = this.checkContinueFlowNextStep(l_stepID);
				if(checkOK == false){
					return checkOK;
				}
			}
		}
		
		return checkOK;
	}
	
	
	/**
	 * 检查流程是否继续向下流转
	 * 如果下一步骤的前续步骤中存在非当前存活步骤实例，则当前步骤不允许继续流转
	 * @param s_NextStpID		下一步骤ID
	 * @return
	 */
	private boolean checkContinueFlowNextStep(String s_NextStpID){
		
		boolean checkOK = true;

		//查询查询前续步骤
		List<Map<String,Object>> preActList = sqlQuery.queryForList("select tzms_actcon_flg,tzms_stp_sta_uniqueid,tzms_cond_sta_uniqueid from tzms_wf_actcls_t where tzms_wfl_uniqueid=? and tzms_stp_end_uniqueid=?", 
				new Object[]{ m_BusProcessDefID, s_NextStpID });
		
		for(Map<String,Object> preActMap: preActList){
			//启用路由条件
			boolean l_actconFlg = preActMap.get("tzms_actcon_flg") == null ? false : (boolean) preActMap.get("tzms_actcon_flg");
			
			if(l_actconFlg == true){
				/*条件路由*/
				String l_conditionID = preActMap.get("tzms_cond_sta_uniqueid").toString();
				
				checkOK = this.checkConditionAction(l_conditionID);
			}else{
				/*步骤路由*/
				String l_stepID = preActMap.get("tzms_stp_sta_uniqueid").toString();
				
				//查询该步骤是否有存活步骤实例，需要去除抄送实例
				/*1-激活、2-撤回、3-处理中、4-结束、5-提前终止、6-未签收、7-退回、8-已签收、9-转发*/
				int aliveStpInsCount = sqlQuery.queryForObject("select count(*) from tzms_stpins_tbl where tzms_wflinsid=? and tzms_wflstpid=? and tzms_stpinsid<>? and tzms_asgmethod<>'1' and tzms_stpinsstat in('1','3','6','8')", 
						new Object[]{ m_WflInstanceID, l_stepID, m_WflStpInsID }, "int");
				
				if(aliveStpInsCount > 0){
					return false;
				}
				
				//继续检查上一步骤
				checkOK = this.checkContinueFlowNextStep(l_stepID);
			}
			
			if(checkOK == false){
				return checkOK;
			}
		}
		
		return checkOK;
	}
	
	
	
	
	/**
	 * 获取指定步骤是否为并发
	 * 只要指向该步骤的动作中有一个为并发路由，则该步骤为并发，需要为每个责任人生成步骤实例
	 * @param s_BusProcessID
	 * @param s_StpID
	 * @return
	 */
	private boolean getConcurrentRouFlag(String s_BusProcessID, String s_StpID){
	
		int count = sqlQuery.queryForObject("select count(*) from tzms_wf_actcls_t where tzms_wfl_uniqueid=? and (tzms_stp_end_uniqueid=? or tzms_cond_end_uniqueid=?) and tzms_endact_flg=0 and tzms_coroutflg=1", 
				new Object[]{ s_BusProcessID, s_StpID, s_StpID }, "int");
		
		if(count > 0){
			return true;
		}else{
			return false;
		}
	}
	
	
	
	/**
	 * 插入步骤实例流转关系表
	 * @param s_stpInsID	步骤实例ID
	 */
	private void insertStpFlowRelation(String s_stpInsID){
		List<Map<String,Object>> stpInsList = sqlQuery.queryForList("select tzms_stpinsid from tzms_actins_tbl where tzms_wflinsid=? and tzms_nextstpinsid=? and tzms_actinsstate='FIN'", 
				new Object[]{ m_WflInstanceID, s_stpInsID });
		
		for(Map<String,Object> stpInsMap: stpInsList){
			String l_preStpInsId = stpInsMap.get("tzms_stpinsid").toString();
			
			String exists = sqlQuery.queryForObject("select 'Y' from tzms_stpins_pre_tbl where tzms_stpinsid=? and tzms_prestpinsid=?", 
					new Object[]{ s_stpInsID, l_preStpInsId }, "String");
			
			if(!"Y".equals(exists)){
				try {
					TzRecord tmp_preStpInsRecord = tzGDObject.createRecord("tzms_stpins_pre_tbl");
					//步骤实例ID
					tmp_preStpInsRecord.setColumnValue("tzms_stpinsid", s_stpInsID);
					//上一步骤实例ID
					tmp_preStpInsRecord.setColumnValue("tzms_prestpinsid", l_preStpInsId);
					
					tmp_preStpInsRecord.insert();
					
				} catch (Exception e) {
					//e.printStackTrace();
					logger.error("插入步骤实例流转关系表失败", e);
				}
			}
		}
	}
	
	
	/**
	 * 工作流动作触发函数
	 * @param o_WflCalendar		日历本
	 * @param s_UserID			动作触发用户ID
	 * @throws TzException
	 */
	public List<String> TriggerAction(TzWflCalendar o_WflCalendar, String s_UserID, TzStpActInfo actInfo) 
			throws TzException {
		List<String> rtnNextStpInsList = null;
		
		//进入下一步的动作类定义
		String l_actClsId = actInfo.getM_ActClsId();
		
		List<String> userList = new ArrayList<String>();
		userList.add(s_UserID);
		
		//动作路径
		List<String> l_RouAllPath = actInfo.getM_RouAllPathList();
		if(l_RouAllPath != null){
			for(String rouActClsId : l_RouAllPath){
				//创建事件接口
				TzWflEvent tzWflEvent = new TzWflEvent();
				tzWflEvent.setM_WflInstanceID(m_WflInstanceID);
				tzWflEvent.setM_WflStpInsID(m_WflStpInsID);
				tzWflEvent.setM_userId(m_CurrUserId);
				
				if(!rouActClsId.equals(l_actClsId)){
					//判断路由，分别触发动作流转前、后事件
					try {
						tzWflEvent.ExecuteEventActions("3", "301", rouActClsId);
						tzWflEvent.ExecuteEventActions("3", "302", rouActClsId);
					}catch(TzException e) {
						flowEventsException.add(e);
					}
				}else{
					//进入下一步步骤路由
					String l_NextStpID = actInfo.getM_NextStepId();
					List<String> l_NextStpUsers = actInfo.getM_NextUserList();
					
					//检查是否继续流转
					if(this.checkContinueFlowNextStep(l_NextStpID) == true){
						/*1、触发动作流转前事件*/
						try {
							tzWflEvent.ExecuteEventActions("3", "301", rouActClsId);
						}catch(TzException e) {
							flowEventsException.add(e);
						}
						
						/*2、初始化下一步步骤实例*/
						List<String> nextStpInsList = this.initializeNextStep(o_WflCalendar, rouActClsId, l_NextStpID, l_NextStpUsers);
						
						/*3、触发动作流转后事件*/
						try {
							tzWflEvent.ExecuteEventActions("3", "302", rouActClsId);
						}catch(TzException e) {
							flowEventsException.add(e);
						}
						
						if(nextStpInsList.size() > 0) {
							rtnNextStpInsList = nextStpInsList;
						}
					}else {
						//不流转
						this.TriggerNotRoutAction(o_WflCalendar, s_UserID, actInfo);
					}
				}
			}
		}
		
		return rtnNextStpInsList;
	} 
	
	
	
	
	
	/**
	 * 工作流非路由动作触发事件，不用继续流转
	 * @param o_WflCalendar
	 * @param s_UserID
	 * @throws TzException
	 */
	public void TriggerNotRoutAction(TzWflCalendar o_WflCalendar, String s_UserID, TzStpActInfo actInfo) throws TzException {
		
		//进入下一步的动作类定义
		String l_actClsId = actInfo.getM_ActClsId();
		
		List<String> userList = new ArrayList<String>();
		userList.add(s_UserID);
		
		//动作路径
		List<String> l_RouAllPath = actInfo.getM_RouAllPathList();
		for(String rouActClsId : l_RouAllPath){
			//创建事件接口
			TzWflEvent tzWflEvent = new TzWflEvent();
			tzWflEvent.setM_WflInstanceID(m_WflInstanceID);
			tzWflEvent.setM_WflStpInsID(m_WflStpInsID);
			tzWflEvent.setM_userId(m_CurrUserId);
			
			if(!rouActClsId.equals(l_actClsId)){
				//判断路由，分别触发动作流转前、后事件
				try {
					tzWflEvent.ExecuteEventActions("3", "301", rouActClsId);
					tzWflEvent.ExecuteEventActions("3", "302", rouActClsId);
				}catch(TzException e) {
					flowEventsException.add(e);
				}
			}else{
				String l_NextStpID = actInfo.getM_NextStepId();
				//等待动作
				this.CreateActionInstance("", l_NextStpID, rouActClsId, "WAT");
				
				TzRecord wflStp_TempRecord = null;
				wflStp_TempRecord = tzGDObject.createRecord("tzms_wflstp_tBase");
				wflStp_TempRecord.setColumnValue("tzms_wflstp_tid", m_WflStpID, true);
				if(wflStp_TempRecord.selectByKey() == true){
					boolean l_UseCpyFlg = wflStp_TempRecord.getTzBoolean("tzms_dpycpyflg").getValue();
					
					//如果启用抄送功能，执行抄送
					if(l_UseCpyFlg == true){
						this.CopyNextStep(o_WflCalendar, actInfo.isM_IsEndAction(), l_NextStpID);
					}
				}
			}
		}
	}
	
	
	
	
	/**
	 * 初始化并创建下一步步骤实例
	 * @param o_WflCalendar		日历本
	 * @param s_actClsID		动作类编号
	 * @throws TzException
	 */
	private List<String> initializeNextStep(TzWflCalendar o_WflCalendar, String s_actClsID, String s_NextStpID, List<String> s_NextStpUsers) 
			throws TzException {
		List<String> nextStpInsList = new ArrayList<String>();
		
		//1、获取工作流业务类别配置信息
		TzRecord actCls_TempRecord = null;
		actCls_TempRecord = tzGDObject.createRecord("tzms_wf_actcls_tBase");
		actCls_TempRecord.setColumnValue("tzms_wf_actcls_tid", s_actClsID, true);
		if(actCls_TempRecord.selectByKey() == false){
			throw new TzException("获取工作流动作类定义信息失败");
		}
		
		//是否并发路由
		//boolean isCoRou = actCls_TempRecord.getTzBoolean("tzms_coroutflg").getValue();
		boolean isCoRou = this.getConcurrentRouFlag(m_BusProcessDefID, s_NextStpID);
		
		//是否结束动作
		boolean isEndAct = actCls_TempRecord.getTzBoolean("tzms_endact_flg").getValue();
		
		//查询当前步骤是否启用抄送功能
//		boolean l_UseCpyFlg = sqlQuery.queryForObject("select tzms_dpycpyflg from tzms_wflstp_t where tzms_wflstp_tid=?", 
//				new Object[]{ m_WflStpID }, "boolean");
		
		TzRecord wflStp_TempRecord = null;
		wflStp_TempRecord = tzGDObject.createRecord("tzms_wflstp_tBase");
		wflStp_TempRecord.setColumnValue("tzms_wflstp_tid", m_WflStpID, true);
		if(wflStp_TempRecord.selectByKey() == false){
			throw new TzException("获取工作流步骤定义失败");
		}
		boolean l_UseCpyFlg = wflStp_TempRecord.getTzBoolean("tzms_dpycpyflg").getValue();
		
		
		if(isEndAct){
			//结束动作，不做任何操作
			this.CreateActionInstance("", "", s_actClsID, "FIN");
			
			//如果启用抄送功能，执行抄送
			if(l_UseCpyFlg == true){
				this.CopyNextStep(o_WflCalendar, isEndAct, s_NextStpID);
			}
		}else{
			//获取责任人列表
			List<String> l_NextUsers = new ArrayList<String>();
			l_NextUsers = s_NextStpUsers;
			
			if(l_NextUsers.size() <= 0){
				String l_stpName = sqlQuery.queryForObject("select tzms_wflstpname from tzms_wflstp_t where tzms_wflstp_tid=?", 
						new Object[]{ s_NextStpID }, "String");
				throw new TzException("下一步骤【"+ l_stpName +"】责任人为空");
			}
			
			
			//判断下一步步骤是否为多步骤并发汇合步骤，如果是汇合步骤，那么需要判断其他并发步骤都完成了才能流转至下一步
//			if(this.CheckRouteJoinPointSubmited(s_actClsID, s_NextStpID) == true){
				
				//获取当前步骤实例优先级
				String l_PriLevelID = sqlQuery.queryForObject("select tzms_pridtlid from tzms_stpins_tbl where tzms_wflinsid=? and tzms_stpinsid=?", 
						new Object[]{ m_WflInstanceID, m_WflStpInsID }, "String");
				
				/*
				 * 如果下一步骤责任人有存活实例（例如并发退回，其他为退回步骤实例仍然存活），不用重复生成步骤实例
				 */
				if(isCoRou){
					//并发，给每一个步骤责任人创建一个步骤实例
					String l_first_stpInsID = "";	//用于设置其他并发责任人动作实例
					int count = 0;
					for(String l_NextUser: l_NextUsers){
						//已存在下一步骤实例
						String existsNextStpIns = sqlQuery.queryForObject("select 'Y' from tzms_stpins_tbl A where tzms_wflinsid=? and tzms_wflstpid=? and tzms_asgmethod='4' and ((tzms_stpinsstat in('1','3','8') and tzms_stpproid=?) or (tzms_stpinsstat='6' and exists(select 'Y' from tzms_acclst_tbl where tzms_stpinsid=A.tzms_stpinsid and tzms_stpproid=?)))", 
								new Object[] { m_WflInstanceID, s_NextStpID, l_NextUser, l_NextUser }, "String");
						
						if(!"Y".equals(existsNextStpIns)) {
							List<String> userList = new ArrayList<String>();
							userList.add(l_NextUser);
							
							TzWflStep nextWflStepInstance = new TzWflStep(m_CurrUserId);
							nextWflStepInstance.CreateWorkflowStep1(o_WflCalendar, m_BusProcessDefID, m_WflInstanceID, s_NextStpID, "", l_PriLevelID, "6", "4", m_WflStpInsID, true, userList);
							String l_NextStpInsID = nextWflStepInstance.getM_WflStpInsID();
							if(count == 0){
								//创建步骤动作实例
								this.CreateActionInstance(l_NextStpInsID, s_NextStpID, s_actClsID, "FIN");
								
								l_first_stpInsID = l_NextStpInsID;
							}else{
								//复制动作实例
								this.CopyActionInstance(l_first_stpInsID, l_NextStpInsID);
							}
							
							//插入步骤实例流转关系
							this.insertStpFlowRelation(l_NextStpInsID);
							
							nextStpInsList.add(l_NextStpInsID);
							
							//是否不发送通知
							if(isNotSendNotice != true) {
								/***发送通知***/
								List<String> nextUserList = nextWflStepInstance.getM_stepUserList();
								TzWflNotice wflNotice = new TzWflNotice(m_WflInstanceID, l_NextStpInsID, s_NextStpID);
								wflNotice.SendWorkflowNotice(nextUserList);
							}
							
							count++;
						}
					}
				}else{
					//非并发，仅创建一个步骤实例
					
					/**
					 * 判断是否存在竞争签收用户都存在的未完成任务，如果存在，不重新创建下一步骤任务实例
					 */
					String whereSql = "";
					for(String l_NextUser: l_NextUsers) {	
						whereSql += " and exists(select 'Y' from tzms_acclst_tbl where tzms_stpinsid=A.tzms_stpinsid and tzms_stpproid='"+ l_NextUser +"')";
					}
					//已存在下一步骤实例
					String existsNextStpIns = sqlQuery.queryForObject("select 'Y' from tzms_stpins_tbl A where tzms_wflinsid=? and tzms_wflstpid=? and tzms_asgmethod='4' and tzms_stpinsstat in('1','3','6','8') " + whereSql, 
							new Object[] { m_WflInstanceID, s_NextStpID }, "String");
					
					if(!"Y".equals(existsNextStpIns)) {
						TzWflStep nextWflStepInstance = new TzWflStep(m_CurrUserId);
						nextWflStepInstance.CreateWorkflowStep0(o_WflCalendar, m_BusProcessDefID, m_WflInstanceID, s_NextStpID, "", l_PriLevelID, "6", "4", m_WflStpInsID, l_NextUsers);
						String l_NextStpInsID = nextWflStepInstance.getM_WflStpInsID();
						
						//创建步骤动作实例
						this.CreateActionInstance(l_NextStpInsID, s_NextStpID, s_actClsID, "FIN");
						
						//插入步骤实例流转关系
						this.insertStpFlowRelation(l_NextStpInsID);
						
						nextStpInsList.add(l_NextStpInsID);
						
						//是否不发送通知
						if(isNotSendNotice != true) {
							/***发送通知***/					
							List<String> nextUserList = nextWflStepInstance.getM_stepUserList();
							TzWflNotice wflNotice = new TzWflNotice(m_WflInstanceID, l_NextStpInsID, s_NextStpID);
							wflNotice.SendWorkflowNotice(nextUserList);
						}
					}
				}
				
				//如果启用抄送功能，执行抄送
				if(l_UseCpyFlg == true){
					this.CopyNextStep(o_WflCalendar, isEndAct, s_NextStpID);
				}
//			}else{
//				/*
//				 * 工作流汇合节点条件不满足，工作流必须等待其它子工作流都完成才可以往下流转
//				 */
//				//创建步骤动作实例
//				this.CreateActionInstance("", s_NextStpID, s_actClsID, "WAT");
//			}
		}
		
		return nextStpInsList;
	}
	
	
	
	
	/**
	 * 检查多路由并发汇合步骤是否都提交完成
	 * @param s_actClsID	//动作类编号
	 * @param s_WflStpID	//步骤编号
	 * @return
	 */
	@SuppressWarnings("unused")
	private boolean CheckRouteJoinPointSubmited(String s_actClsID, String s_WflStpID){
		//1、查询步骤是否为多路由汇合步骤
		int l_rouCount = sqlQuery.queryForObject("select count(*) from tzms_wf_actcls_t where tzms_wfl_uniqueid=? and tzms_stp_end_uniqueid=?", 
				new Object[]{ m_WflInstanceID, s_WflStpID }, "Integer");
		
		//不是多路由并发汇合步骤直接返回true
		if(l_rouCount <= 1){
			return true;	
		}
		
		//2、检查其他并发动作，是否都已完成，只要有一条路由未完成返回false
		List<Map<String, Object>> l_othActList = sqlQuery.queryForList("select tzms_wf_actcls_tid from tzms_wf_actcls_t where tzms_wfl_uniqueid=? and tzms_stp_end_uniqueid=? and tzms_wf_actcls_tid<>?", 
				new Object[]{ m_WflInstanceID, s_WflStpID, s_actClsID });
		
		for(Map<String,Object> l_othActMap: l_othActList){
			String l_othActClsID = l_othActMap.get("tzms_wf_actcls_tid").toString();
			
			int count = sqlQuery.queryForObject("select count(*) from tzms_actins_tbl where tzms_wflinsid=? and tzms_actclsid=? and tzms_nextstpid=? and tzms_actinsstate<>'FIN'", 
					new Object[]{ m_WflInstanceID, l_othActClsID, s_WflStpID }, "Integer");
			
			if(count <= 0){
				return false;
			}
		}
		
		return true;
	}

	
	
	
	/**
	 * 工作流步骤实例抄送
	 * @param o_WflCalendar
	 * @param s_isEndAct	是否结束动作
	 * @param s_NextStpID	下一步步骤编号
	 */
	private void CopyNextStep(TzWflCalendar o_WflCalendar, boolean s_isEndAct, String s_NextStpID){
		//步骤实例
		TzWflStep l_WflStepInstance;
		
		//1、获取抄送人
		List<String> l_CpyUsers = this.GetCpyUserList();
		
		if(l_CpyUsers.size() > 0){
			for(String l_CypUser: l_CpyUsers){
				//为每一个抄送人创建下一步步骤实例
				List<String> userList = new ArrayList<String>();
				userList.add(l_CypUser);
				
				l_WflStepInstance = new TzWflStep(m_CurrUserId);
				
				if(s_isEndAct){
					l_WflStepInstance.CreateWorkflowStep0(o_WflCalendar, m_BusProcessDefID, m_WflInstanceID, m_WflStpID, "", "", "6", "1", m_WflStpInsID, userList);
					s_NextStpID = m_WflStpID;
				}else{
					l_WflStepInstance.CreateWorkflowStep0(o_WflCalendar, m_BusProcessDefID, m_WflInstanceID, s_NextStpID, "", "", "6", "1", m_WflStpInsID, userList);
				}
				
				//是否不发送通知
				if(isNotSendNotice != true) {
					/***发送通知***/
					String New_StpInsId = l_WflStepInstance.getM_WflStpInsID();
					List<String> nextUserList = l_WflStepInstance.getM_stepUserList();
					TzWflNotice wflNotice = new TzWflNotice(m_WflInstanceID, New_StpInsId, s_NextStpID);
					wflNotice.setCopyRw(true);
					
					wflNotice.SendWorkflowNotice(nextUserList);
				}
			}
		}
	}
	
	
	
	/**
	 * 获取抄送人列表
	 * @return
	 */
	private List<String> GetCpyUserList(){
		
		List<String> l_CpyList = new ArrayList<String>();
		
		List<Map<String,Object>> l_CpyUserList = sqlQuery.queryForList("select tzms_stpproid from tzms_not_psn_tbl where tzms_stpinsid=?", 
				new Object[]{ m_WflStpInsID });
		
		if(l_CpyUserList != null && l_CpyUserList.size() > 0){
			for(Map<String,Object> l_CpyUserMap: l_CpyUserList){
				
				String copyUserid = l_CpyUserMap.get("tzms_stpproid").toString();
				if(StringUtils.isNotBlank(copyUserid) && !l_CpyList.contains(copyUserid)) {
					l_CpyList.add(copyUserid);
				}
			}
		}
		/*不从知会角色中解析知会人，知会角色会在生成步骤实例的时候*/
//		else{
//			//从配置的知会人角色取值
//			String l_CpyRoleID = sqlQuery.queryForObject("select tzms_cpyrole_uniqueid from tzms_wflstp_t where tzms_wflstp_tid=?", 
//					new Object[]{ m_WflStpID }, "String");
//			
//			if(StringUtils.isNotEmpty(l_CpyRoleID)){				
//				AnalysisDynaRole analysisRole = new AnalysisDynaRole();
//				l_CpyList = analysisRole.getUserIds(m_WflInstanceID, m_WflStpInsID, m_CurrUserId, l_CpyRoleID);
//			}
//		}
		
		return l_CpyList;
	}
	
	

	
	/**
	 * 检查退回条件，如果当前步骤实例已流转，不能退回
	 * @return
	 */
	private boolean checkReturnCondition(){
		
		//判断当前步骤实例是否有下一步骤实例
		int count = sqlQuery.queryForObject("select count(*) from tzms_stpins_tbl A,tzms_stpins_pre_tbl B where A.tzms_stpinsid=B.tzms_stpinsid and A.tzms_stpinsstat not in('2') and A.tzms_wflinsid=? and B.tzms_prestpinsid=?",
				new Object[]{ m_WflInstanceID, m_WflStpInsID }, "Integer");
		
		if(count > 0){
			return false;
		}
		return true;
	}


	
	/**
	 * 退回到指定步骤，
	 * 需要判断指定的步骤是否在流程流转步骤中，否则报错
	 * @param o_WflCalendar
	 * @param s_stepId
	 * @return
	 * @throws TzException
	 */
	public boolean BackToChonseStep(TzWflCalendar o_WflCalendar, String s_StepID) throws TzException {
		boolean l_BackOK = true;
		try{
			//判断退回条件
			if(this.checkReturnCondition() == false){
				throw new TzException("【操作错误】工作流步骤已经开始流转，无法被退回。");
			}else{
				//从当前步骤向前回溯，检查指定步骤是否在流转步骤中
				TzWorkflowFunc workflowFunc = new TzWorkflowFunc();
				if(workflowFunc.checkStpidInWorkflowStep(m_WflInstanceID, m_WflStpInsID, s_StepID) == true){
					
					//获取当前步骤实例信息
					TzRecord stpIns_TempRecord = tzGDObject.createRecord("tzms_stpins_tbl");
					stpIns_TempRecord.setColumnValue("tzms_wflinsid", m_WflInstanceID, true);
					stpIns_TempRecord.setColumnValue("tzms_stpinsid", m_WflStpInsID, true);
					if(stpIns_TempRecord.selectByKey() == false){
						throw new TzException("获取当前步骤实例记录失败");
					}
					
					//获取当前步骤实例优先级
					String l_PriLevelID = sqlQuery.queryForObject("select tzms_pridtlid from tzms_stpins_tbl where tzms_wflinsid=? and tzms_stpinsid=?", 
							new Object[]{ m_WflInstanceID, m_WflStpInsID }, "String");
	
					/*步骤退回责任人*/
					List<String> userList = new ArrayList<String>();
					
					//查询是不是退回起始步骤
					String isStartStp = sqlQuery.queryForObject("select 'Y' from tzms_wflstp_t where tzms_wflstp_tid=? and tzms_stptype=1", 
							new Object[]{ s_StepID }, "String");
					
					if("Y".equals(isStartStp)){
						//获取首步骤责任人
						String l_FirstUserID = sqlQuery.queryForObject("select tzms_stpproid from tzms_stpins_tbl where tzms_wflinsid=? and tzms_wflstpid=? and tzms_asgmethod='3'", 
								new Object[]{ m_WflInstanceID, s_StepID }, "String");
						userList.add(l_FirstUserID);
					}else{
						//查询指定步骤最新非抄送和会签的实例
						String lastPreStpInsID = sqlQuery.queryForObject("select top 1 tzms_prestpinsid from tzms_stpins_tbl where tzms_wflinsid=? and tzms_wflstpid=? and tzms_asgmethod not in('1','2') order by ROW_ADDED_DTTM desc", 
								new Object[]{ m_WflInstanceID, s_StepID }, "String");
						
						List<Map<String,Object>> stpOpridList = sqlQuery.queryForList("select tzms_stpproid from tzms_stpins_tbl A where tzms_wflinsid=? and tzms_asgmethod in('4','5') and exists(select 'Y' from tzms_stpins_pre_tbl where tzms_prestpinsid=? and tzms_stpinsid=A.tzms_stpinsid)", 
								new Object[]{ m_WflInstanceID, lastPreStpInsID });
						for(Map<String,Object> stpOpridMap: stpOpridList){
							String l_stpOprid = stpOpridMap.get("tzms_stpproid").toString();
							if(!userList.contains(l_stpOprid)){
								userList.add(l_stpOprid);
							}
						}
					}
					
					
					/**事件触发开始**/
					TzWflEvent tzWflEvent = new TzWflEvent();
					tzWflEvent.setM_WflInstanceID(m_WflInstanceID);
					tzWflEvent.setM_WflStpInsID(m_WflStpInsID);
					tzWflEvent.setM_userId(m_CurrUserId);
					
					//触发步骤211-退回前事件
					tzWflEvent.ExecuteEventActions("2", "211", m_WflStpID);
					

					//重新生成退回步骤对应步骤实例
					TzWflStep tzWflStepInstance = new TzWflStep(m_CurrUserId);
					boolean createOK = tzWflStepInstance.CreateWorkflowStep0(o_WflCalendar, m_BusProcessDefID, m_WflInstanceID, s_StepID, "", l_PriLevelID, "6", "4", m_WflStpInsID, userList);
					
					if(createOK){
						//设置退回步骤实例
						String New_StpInsId = tzWflStepInstance.getM_WflStpInsID();
						sqlQuery.update("update tzms_stpins_tbl set tzms_rtn_stpinsid=? where tzms_wflinsid=? and tzms_stpinsid=?", 
								new Object[]{ m_WflStpInsID, m_WflInstanceID, New_StpInsId });
						
						//步骤责任人
						String tzms_stpproid = stpIns_TempRecord.getTzString("tzms_stpproid").getValue();
						if(StringUtils.isBlank(tzms_stpproid)) {
							//如果没有步骤责任人（管理员后台处理），默认设置一个责任人为步骤责任人
							tzms_stpproid = sqlQuery.queryForObject("select top 1 tzms_stpproid from tzms_acclst_tbl where tzms_stpinsid=? order by tzms_proidtype desc", 
									new Object[] { m_WflStpInsID }, "String");
							
							stpIns_TempRecord.setColumnValue("tzms_stpproid", tzms_stpproid);
						}
						
						//设置动作摘要-退回某步
						stpIns_TempRecord.setColumnValue("tzms_action_type", "退回");
						//设置当前步骤实例状态为“退回”
						stpIns_TempRecord.setColumnValue("tzms_stpinsstat", "7");
						//结束时间
						stpIns_TempRecord.setColumnValue("tzms_stpenddt", new Date());
						stpIns_TempRecord.update();
						
						//插入步骤实例流转关系tzms_stpins_pre_tbl
						TzRecord stpPre_TempRecord = tzGDObject.createRecord("tzms_stpins_pre_tbl");
						stpPre_TempRecord.setColumnValue("tzms_stpinsid", New_StpInsId);
						stpPre_TempRecord.setColumnValue("tzms_prestpinsid", m_WflStpInsID);
						stpPre_TempRecord.insert();
						

						//创建到新步骤实例的动作实例
						CommonFun commonFun = new CommonFun();
						String actionInsID = commonFun.GenerateGUID_1("ACTINS", 30);

						//创建动作实例表
						TzRecord tmp_actIndRecord = tzGDObject.createRecord("tzms_actins_tbl");
						//动作实例ID
						tmp_actIndRecord.setColumnValue("tzms_actinsid", actionInsID);
						//流转动作ID
						tmp_actIndRecord.setColumnValue("tzms_actclsid", WFL_RETURN_STEP_ACTCLSID);
						//流程实例ID
						tmp_actIndRecord.setColumnValue("tzms_wflinsid", m_WflInstanceID);
						//步骤实例ID
						tmp_actIndRecord.setColumnValue("tzms_stpinsid", m_WflStpInsID);
						//下一步步骤实例ID
						tmp_actIndRecord.setColumnValue("tzms_nextstpinsid", New_StpInsId);
						//下一步步骤ID
						tmp_actIndRecord.setColumnValue("tzms_nextstpid", s_StepID);
						//动作实例状态
						tmp_actIndRecord.setColumnValue("tzms_actinsstate", "RTN");
		
						tmp_actIndRecord.insert();
						
						
						
						//指定步骤退回事件退回事件
						tzWflEvent.ExecuteEventActions("5", "501", s_StepID);
						
						//触发步骤208-退回时事件
						tzWflEvent.ExecuteEventActions("2", "208", m_WflStpID);

						//触发其他事件401-退回时事件
						//tzWflEvent.ExecuteEventActions("4", "401", m_BusProcessDefID);
						
						//触发工作流108-退回时事件
						tzWflEvent.ExecuteEventActions("1", "108", m_BusProcessDefID);

						//是否不发送通知
						if(isNotSendNotice != true) {
							/***发送通知***/
							List<String> nextUserList = tzWflStepInstance.getM_stepUserList();
							TzWflNotice wflNotice = new TzWflNotice(m_WflInstanceID, New_StpInsId, s_StepID);
							wflNotice.SendWorkflowNotice(nextUserList);
						}
					}else {
						throw new TzException("退回失败，创建退回步骤任务实例失败");
					}
				}else{
					throw new TzException("【配置错误】当前业务流程没有从指定步骤流转，不能退回指定步骤");
				}
			}
		}catch (TzException e) {
			throw e;
		}catch (Exception e){
			throw new TzException("系统错误" + e.toString());
		}
		
		return l_BackOK;
	}
	
	
	
	
	/*退回重填
	 * 如果是并发或加签步骤实例退回，仅退回当前实例步骤到首步骤，其他实例不变
	 * 如果已流转下一步，不可退回
	 * */
	public boolean BackAndReFill(TzWflCalendar o_WflCalendar) throws TzException {
		boolean l_BackOK = true;
		
		//获取首步骤
		String l_FirstStpID = sqlQuery.queryForObject("select tzms_wflstp_tid from tzms_wflstp_t where tzms_wfcldn_uniqueid=? and tzms_stptype=1", 
				new Object[]{ m_BusProcessDefID }, "String");
		
		try{
			if(this.checkReturnCondition() == false){
				throw new TzException("【操作错误】工作流步骤已经开始流转，无法被退回。");
			}else{
				//获取当前步骤实例信息
				TzRecord stpIns_TempRecord = tzGDObject.createRecord("tzms_stpins_tbl");
				stpIns_TempRecord.setColumnValue("tzms_wflinsid", m_WflInstanceID, true);
				stpIns_TempRecord.setColumnValue("tzms_stpinsid", m_WflStpInsID, true);
				if(stpIns_TempRecord.selectByKey() == false){
					throw new TzException("获取当前步骤实例记录失败");
				}
				
				if(StringUtils.isNotEmpty(l_FirstStpID)){
					//如果当前步骤就是首步骤，不允许退回
					if(!l_FirstStpID.equals(m_WflStpID)){
						//1----设置当前步骤Action实例状态
						//获取当前步骤实例优先级
						String l_PriLevelID = sqlQuery.queryForObject("select tzms_pridtlid from tzms_stpins_tbl where tzms_wflinsid=? and tzms_stpinsid=?", 
								new Object[]{ m_WflInstanceID, m_WflStpInsID }, "String");
						
						//获取首步骤责任人
						String l_FirstUserID = sqlQuery.queryForObject("select tzms_stpproid from tzms_stpins_tbl where tzms_wflinsid=? and tzms_wflstpid=? and tzms_asgmethod='3'", 
								new Object[]{ m_WflInstanceID, l_FirstStpID }, "String");
						List<String> userList = new ArrayList<String>();
						userList.add(l_FirstUserID);
						
						
						/**事件触发开始**/
						TzWflEvent tzWflEvent = new TzWflEvent();
						tzWflEvent.setM_WflInstanceID(m_WflInstanceID);
						tzWflEvent.setM_WflStpInsID(m_WflStpInsID);
						tzWflEvent.setM_userId(m_CurrUserId);
						
						//触发步骤211-退回前事件
						tzWflEvent.ExecuteEventActions("2", "211", m_WflStpID);
						
						
						//创建步骤实例
						TzWflStep tzWflStpInstance = new TzWflStep(m_CurrUserId);
						boolean createOK = tzWflStpInstance.CreateWorkflowStep0(o_WflCalendar, m_BusProcessDefID, m_WflInstanceID, l_FirstStpID, "", l_PriLevelID, "6", "4", m_WflStpInsID, userList);
						
						if(createOK){
							//设置退回步骤实例
							String New_StpInsId = tzWflStpInstance.getM_WflStpInsID();
							sqlQuery.update("update tzms_stpins_tbl set tzms_rtn_stpinsid=? where tzms_wflinsid=? and tzms_stpinsid=?", new Object[]{ m_WflStpInsID, m_WflInstanceID, New_StpInsId });
							
							//步骤责任人
							String tzms_stpproid = stpIns_TempRecord.getTzString("tzms_stpproid").getValue();
							if(StringUtils.isBlank(tzms_stpproid)) {
								//如果没有步骤责任人（管理员后台处理），默认设置一个责任人为步骤责任人
								tzms_stpproid = sqlQuery.queryForObject("select top 1 tzms_stpproid from tzms_acclst_tbl where tzms_stpinsid=? order by tzms_proidtype desc", 
										new Object[] { m_WflStpInsID }, "String");
								
								stpIns_TempRecord.setColumnValue("tzms_stpproid", tzms_stpproid);
							}
							
							//设置动作摘要-退回重填
							stpIns_TempRecord.setColumnValue("tzms_action_type", "退回重填");
							//设置当前步骤实例状态为“退回”
							stpIns_TempRecord.setColumnValue("tzms_stpinsstat", "7");
							//结束时间
							stpIns_TempRecord.setColumnValue("tzms_stpenddt", new Date());
							stpIns_TempRecord.update();
							
							//插入步骤实例流转关系tzms_stpins_pre_tbl
							TzRecord stpPre_TempRecord = tzGDObject.createRecord("tzms_stpins_pre_tbl");
							stpPre_TempRecord.setColumnValue("tzms_stpinsid", New_StpInsId);
							stpPre_TempRecord.setColumnValue("tzms_prestpinsid", m_WflStpInsID);
							stpPre_TempRecord.insert();
							

							//创建到新步骤实例的动作实例
							CommonFun commonFun = new CommonFun();
							String actionInsID = commonFun.GenerateGUID_1("ACTINS", 30);

							//创建动作实例表
							TzRecord tmp_actIndRecord = tzGDObject.createRecord("tzms_actins_tbl");
							//动作实例ID
							tmp_actIndRecord.setColumnValue("tzms_actinsid", actionInsID);
							//流转动作ID
							tmp_actIndRecord.setColumnValue("tzms_actclsid", WFL_RETURN_REFILL_ACTCLSID);
							//流程实例ID
							tmp_actIndRecord.setColumnValue("tzms_wflinsid", m_WflInstanceID);
							//步骤实例ID
							tmp_actIndRecord.setColumnValue("tzms_stpinsid", m_WflStpInsID);
							//下一步步骤实例ID
							tmp_actIndRecord.setColumnValue("tzms_nextstpinsid", New_StpInsId);
							//下一步步骤ID
							tmp_actIndRecord.setColumnValue("tzms_nextstpid", l_FirstStpID);
							//动作实例状态
							tmp_actIndRecord.setColumnValue("tzms_actinsstate", "RTN");
			
							tmp_actIndRecord.insert();

							
							//触发步骤208-退回时事件
							tzWflEvent.ExecuteEventActions("2", "208", m_WflStpID);
							
							//触发其他事件401-退回时事件
							//tzWflEvent.ExecuteEventActions("4", "401", m_BusProcessDefID);

							//触发工作流108-退回时事件
							tzWflEvent.ExecuteEventActions("1", "108", m_BusProcessDefID);
							
							//是否不发送通知
							if(isNotSendNotice != true) {
								/***发送通知***/
								List<String> nextUserList = tzWflStpInstance.getM_stepUserList();
								TzWflNotice wflNotice = new TzWflNotice(m_WflInstanceID, New_StpInsId, l_FirstStpID);
								wflNotice.SendWorkflowNotice(nextUserList);
							}
						}
					}else{
						/*当前步骤就是首步骤，不允许退回*/
						l_BackOK = false;
						throw new TzException("当前步骤为首步骤，无法退回重填");
					}
				}
			}
		}catch (TzException e) {
			logger.error("退回重填错误，工作流实例ID："+ m_WflInstanceID +"，步骤实例ID："+ m_WflStpInsID +"，异常信息：", e);
			throw e;
		}catch (Exception e){
			logger.error("退回重填错误，工作流实例ID："+ m_WflInstanceID +"，步骤实例ID："+ m_WflStpInsID +"，异常信息：", e);
			throw new TzException("【系统错误】，请联系管理员解决，" + e.getMessage());
		}
		
		return l_BackOK;
	}
	
	
	
	
	
	/**
	 * 撤回
	 * 已提交但尚未被后续步骤责任签收的任务可以由当前步骤责任人主动撤回
	 * @return
	 * @throws TzException
	 */
	public boolean ExecuteWithdraw(TzWflCalendar o_WflCalendar) throws TzException {
		boolean withdrawOK = false;
		
		try {
			//流程状态：0-新建、1-激活、2-处理中、3-结束、4-提前终止、5-已撤销
			String wflStatus = sqlQuery.queryForObject("select tzms_wflstatus from tzms_wflins_tbl where tzms_wflinsid=?", 
					new Object[] { m_WflInstanceID }, "String");
			if("3".equals(wflStatus) 
					|| "4".equals(wflStatus) 
					|| "5".equals(wflStatus)) {
				throw new TzException("流程任务已结束，无法撤回");
			}
			
			//1---查询下一步已被签收的步骤实例状态
			String sql = "select tzms_stpinsstat from tzms_stpins_tbl A,tzms_actins_tbl B where B.tzms_wflinsid=? and B.tzms_stpinsid=? and A.tzms_wflinsid=B.tzms_wflinsid and B.tzms_nextstpinsid=A.tzms_stpinsid and tzms_stpproid is not null";
			List<Map<String,Object>> l_NextStpList = sqlQuery.queryForList(sql, new Object[]{ m_WflInstanceID, m_WflStpInsID });
			
			if(l_NextStpList != null && l_NextStpList.size() > 0){
				for(Map<String,Object> l_NextStpMap : l_NextStpList){
					String l_StpInsSta = l_NextStpMap.get("tzms_stpinsstat").toString();
					
					//下一步骤只要有不是未签收步骤实例，则不能撤回
					if(!"2".equals(l_StpInsSta) && !"6".equals(l_StpInsSta)){
						throw new TzException("【操作失败】流转任务已被签收，无法撤回。");
					}
				}
			}
			
			
			List<String> bakStpInsList = new ArrayList<String>();
			//2----查询所有未签收的步骤实例，并设置步骤实例状态为“撤回”
			sql = "select A.tzms_stpinsid,A.tzms_wflstpid from tzms_stpins_tbl A,tzms_actins_tbl B where B.tzms_wflinsid=? and B.tzms_stpinsid=? and A.tzms_wflinsid=B.tzms_wflinsid and B.tzms_nextstpinsid=A.tzms_stpinsid and tzms_stpinsstat='6'";
			l_NextStpList = sqlQuery.queryForList(sql, new Object[]{ m_WflInstanceID, m_WflStpInsID });
			if(l_NextStpList != null && l_NextStpList.size() > 0) {
				for(Map<String,Object> l_NextStpMap: l_NextStpList){
					String l_StpInsID = l_NextStpMap.get("tzms_stpinsid").toString();
					//String l_WflStepID = l_NextStpMap.get("tzms_wflstpid").toString();
					
					int num = sqlQuery.update("update tzms_stpins_tbl set tzms_stpinsstat='2',tzms_action_type=?,tzms_stpenddt=? where tzms_wflinsid=? and tzms_stpinsid=?", 
							new Object[]{ "被撤回", new Date(), m_WflInstanceID, l_StpInsID });
					if(num > 0) {
						bakStpInsList.add(l_StpInsID);
					}
				}
			}
			
			//3---将当前步骤实例设置为“处理中”
			//sqlQuery.update("update tzms_stpins_tbl set tzms_stpinsstat='3' where tzms_wflinsid=? and tzms_stpinsid=?", new Object[]{ m_WflInstanceID, m_WflStpInsID });
			
			//3---重新生成当前步骤实例
			String userId = sqlQuery.queryForObject("select tzms_stpproid from tzms_stpins_tbl where tzms_wflinsid=? and tzms_stpinsid=?", 
					new Object[]{ m_WflInstanceID, m_WflStpInsID }, "String");
	
			if(StringUtils.isNotBlank(userId)) {
				
				//查询当前步骤是否有撤回人的存活实例，如果有则不重新生成实例
				int liveCount = sqlQuery.queryForObject("select count(*) from tzms_stpins_tbl A where tzms_wflinsid=? and tzms_wflstpid=? and tzms_stpinsstat in('1','3','6','8') and (tzms_stpproid=? or (tzms_stpproid='' and exists(select 'Y' from tzms_acclst_tbl B where B.tzms_stpinsid=A.tzms_stpinsid and B.tzms_stpproid=?)))", 
						new Object[] { m_WflInstanceID, m_WflStpID, userId, userId }, "int");
				if(liveCount > 0) {
					return true;
				}
				
				//重新生成当前步骤实例
				List<String> userList = new ArrayList<String>();
				userList.add(userId);
				
				//获取当前步骤实例优先级
				String l_PriLevelID = sqlQuery.queryForObject("select tzms_pridtlid from tzms_stpins_tbl where tzms_wflinsid=? and tzms_stpinsid=?", 
						new Object[]{ m_WflInstanceID, m_WflStpInsID }, "String");
				
				TzWflStep NewTzWflStep = new TzWflStep(m_CurrUserId);
				boolean createOk = NewTzWflStep.CreateWorkflowStep0(o_WflCalendar, m_BusProcessDefID, m_WflInstanceID, m_WflStpID, "", l_PriLevelID, "6", "4", m_WflStpInsID, userList);
				if(createOk) {
					String New_StpInsId = NewTzWflStep.getM_WflStpInsID();
					withdrawOK = true;
					
					for(String bakStpIns: bakStpInsList) {
						//3、创建到新步骤实例的动作实例
						CommonFun commonFun = new CommonFun();
						String actionInsID = commonFun.GenerateGUID_1("ACTINS", 30);
						
						//创建动作实例表
						TzRecord tmp_actIndRecord = tzGDObject.createRecord("tzms_actins_tbl");
						//动作实例ID
						tmp_actIndRecord.setColumnValue("tzms_actinsid", actionInsID);
						//流转动作ID
						tmp_actIndRecord.setColumnValue("tzms_actclsid", WFL_RETURN_BACK_ACTCLSID);
						//流程实例ID
						tmp_actIndRecord.setColumnValue("tzms_wflinsid", m_WflInstanceID);
						//步骤实例ID
						tmp_actIndRecord.setColumnValue("tzms_stpinsid", bakStpIns);
						//下一步步骤实例ID
						tmp_actIndRecord.setColumnValue("tzms_nextstpinsid", New_StpInsId);
						//下一步步骤ID
						tmp_actIndRecord.setColumnValue("tzms_nextstpid", m_WflStpID);
						//动作实例状态
						tmp_actIndRecord.setColumnValue("tzms_actinsstate", "FIN");
	
						tmp_actIndRecord.insert();
					}
	
					//是否不发送通知
					if(isNotSendNotice != true) {
						/***发送通知***/
						List<String> nextUserList = NewTzWflStep.getM_stepUserList();
						TzWflNotice wflNotice = new TzWflNotice(m_WflInstanceID, New_StpInsId, m_WflStpID);
						wflNotice.SendWorkflowNotice(nextUserList);
					}
				}
			}
		}catch(TzException e) {
			logger.error("撤回错误，工作流实例ID："+ m_WflInstanceID +"，步骤实例ID："+ m_WflStpInsID +"，异常信息：", e);
			throw e;
		}catch(Exception e) {
			logger.error("撤回错误，工作流实例ID："+ m_WflInstanceID +"，步骤实例ID："+ m_WflStpInsID +"，异常信息：", e);
			throw new TzException("撤回发生错误，请联系管理员解决，" + e.getMessage());
		}
		
		return withdrawOK;
	}

}
