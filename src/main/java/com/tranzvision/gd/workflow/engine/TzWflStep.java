package com.tranzvision.gd.workflow.engine;

import com.tranzvision.gd.WorkflowActionsBundle.service.impl.ExpressionEngineImpl;
import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.base.TzException;
import com.tranzvision.gd.util.dynamicsBase.AnalysisDynaRole;
import com.tranzvision.gd.util.dynamicsBase.AnalysisDynaSysVar;
import com.tranzvision.gd.util.dynamicsBase.AnalysisFlowFunction;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
import com.tranzvision.gd.util.sql.type.TzRecord;
import com.tranzvision.gd.workflow.base.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * 用于管理工作流步骤实例
 * @author 张浪	2019-01-14
 * @version 1.0
 * 动作摘要说明：1-同意、2-拒绝、3-转发、4-提交、5-撤销、6-撤回、7-退回某步、8-退回重填、9-已阅、10-激活任务、11-终止任务
 */
public class TzWflStep {
	
	private TZGDObject tzGDObject;
	
	private SqlQuery sqlQuery;
	
	private TzWflObject tzWflObject;
	
	
	//流程直送动作类定义编号固定
	private static final String WFL_DIRECT_SEND_ACTCLSID = "DIRECT_SEND_20190123";
	//流程转发动作类定义编号固定
	private static final String WFL_TRANSPOND_ACTCLSID = "TRANSPOND_SEND_20190123";
	//加签动作类定义编号固定
	private static final String WFL_ADD_SIGN_ACTCLSID = "ADD_SIGN_20190123";
	
	
	private String m_CurrUserId;
	
	//工作流步骤事件接口
	private TzWflEvent m_WflStepEventInterface;
	
	//当前步骤实例记录
	private TzRecord m_WflStepInsRecord;
	
	//当前步骤配置信息记录
	private TzRecord m_WflStepCfgRecord;
	   
	//工作流实例编号
	private String m_WflInstanceID;
	
	//步骤实例编号
	private String m_WflStpInsID;
	
	//当前步骤编号
	private String m_WflStepID;
	
	//提交动作路径信息，路径不唯一时需要
	private TzStpActInfo m_WorkflowActionInfo;
	
	private List<String> m_stepUserList = null;
	
	//是否不发生通知
	private boolean isNotSendNotice = false;
	
	//记录日志
	private static final Logger logger = Logger.getLogger("WorkflowEngine");

	
	
	
	
	public TzWflEvent getM_WflStepEventInterface() {
		return m_WflStepEventInterface;
	}
	public TzRecord getM_WflStepInsRecord() {
		return m_WflStepInsRecord;
	}
	public TzRecord getM_WflStepCfgRecord() {
		return m_WflStepCfgRecord;
	}
	public String getM_WflInstanceID() {
		return m_WflInstanceID;
	}
	public String getM_WflStpInsID() {
		return m_WflStpInsID;
	}
	public String getM_WflStepID() {
		return m_WflStepID;
	}
	public TzStpActInfo getM_WorkflowActionInfo() {
		return m_WorkflowActionInfo;
	}
	public void setM_WorkflowActionInfo(TzStpActInfo m_WorkflowActionInfo) {
		this.m_WorkflowActionInfo = m_WorkflowActionInfo;
	}
	public List<String> getM_stepUserList() {
		return m_stepUserList;
	}
	public void setNotSendNotice(boolean isNotSendNotice) {
		this.isNotSendNotice = isNotSendNotice;
	}
	
	
	
	
	
	
	/**
	 * 构造函数
	 */
	public TzWflStep(String UserId){
		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		
		tzGDObject = (TZGDObject) getSpringBeanUtil.getAutowiredSpringBean("TZGDObject");
		sqlQuery = (SqlQuery) getSpringBeanUtil.getAutowiredSpringBean("SqlQuery");
		tzWflObject = (TzWflObject) getSpringBeanUtil.getAutowiredSpringBean("TzWflObject");
		
		m_CurrUserId = UserId;
		m_WorkflowActionInfo = null;
	}
	
	
	
	/**
	 * 检查当前步骤实例结束状态
	 * @throws TzException
	 */
	public void checkWflStpInstanceStatus() throws TzException {
		//判断当前步骤实例状态
		String stpInsSta = sqlQuery.queryForObject("select tzms_stpinsstat from tzms_stpins_tbl where tzms_wflinsid=? and tzms_stpinsid=?", 
				new Object[]{ m_WflInstanceID, m_WflStpInsID }, "String");
		
		if("5".equals(stpInsSta)){
			throw new TzException("当前任务实例已经被终止，请退出当前任务处理页面。您对当前任务所做的修改均将被取消。");
		}else if("4".equals(stpInsSta)){
			throw new TzException("当前任务实例已经完成，请退出当前任务处理页面。您对当前任务所做的修改均将被取消。");
		}else if("2".equals(stpInsSta)){
			throw new TzException("当前任务实例已经被撤回，请退出当前任务处理页面。您对当前任务所做的修改均将被取消。");
		}else if("7".equals(stpInsSta)){
			throw new TzException("当前任务实例已经被退回，请退出当前任务处理页面。您对当前任务所做的修改均将被取消。");
		}else if("9".equals(stpInsSta)){
			throw new TzException("当前任务实例已经被转发，请退出当前任务处理页面。您对当前任务所做的修改均将被取消。");
		}
	}
	
	
	/**
	 * 设置动作摘要
	 * @param actType
	 * @return
	 */
	public void setActAbstract(String actType) {
		TzRecord wflStp_InsRecord = null;
		
		try {
			if(m_WflStepInsRecord == null) {
				wflStp_InsRecord = tzGDObject.createRecord("tzms_stpins_tbl");
				wflStp_InsRecord.setColumnValue("tzms_wflinsid", m_WflInstanceID, true);
				wflStp_InsRecord.setColumnValue("tzms_stpinsid", m_WflStpInsID, true);
				
				if(wflStp_InsRecord.selectByKey() == false){
					throw new TzException("获取工作流步骤实例失败");
				}
			}else {
				wflStp_InsRecord = m_WflStepInsRecord;
			}
			
			if(wflStp_InsRecord != null) {
				
				if(StringUtils.isBlank(actType)) {
					//流程步骤
					String stpID = wflStp_InsRecord.getTzString("tzms_wflstpid").getValue();
					actType = sqlQuery.queryForObject("select tzms_buttonname from tzms_wflstp_tBase where tzms_wflstp_tid=?", 
							new Object[] { stpID }, "String");
				}
				
				wflStp_InsRecord.setColumnValue("tzms_action_type", actType);
				wflStp_InsRecord.update();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 根据加签人步骤实例获取流转责任人步骤步骤实例
	 * 需要考虑加签人添加加签的情况
	 * @param asignStpInsID		加签人步骤实例
	 * @return
	 */
	private String getWflZrrByAsignStpInsID(String asignStpInsID){
		
		String zrrStpInsID = sqlQuery.queryForObject("select tzms_stpinsid from tzms_asign_psn_tbl where tzms_asign_stpinsid=?", 
				new Object[]{ m_WflStpInsID }, "String");
		
		//判断步骤实例的任务分配方式
		String l_asgmethod = sqlQuery.queryForObject("select tzms_asgmethod from tzms_stpins_tbl where tzms_wflinsid=? and tzms_stpinsid=?", 
				new Object[]{ m_WflInstanceID, zrrStpInsID }, "String");
		
		if("2".equals(l_asgmethod)){
			zrrStpInsID = this.getWflZrrByAsignStpInsID(zrrStpInsID);
		}
		
		return zrrStpInsID;
	}
	

	
	/**
	 * 当前步骤实例终止
	 * @param stpStatus
	 * @throws TzException
	 */
	public void WflStepTerminate(String stpStatus) throws TzException {
		//判断当前步骤实例状态		
		this.checkWflStpInstanceStatus();
		
		/*触发步骤终止前事件*/
		m_WflStepEventInterface.ExecuteEventActions("2", "204", m_WflStepID);
		
		/*设置步骤实例状态*/		
		try{
			TzRecord stpIns_TempRecord = tzGDObject.createRecord("tzms_stpins_tbl");
			stpIns_TempRecord.setColumnValue("tzms_wflinsid", m_WflInstanceID, true);
			stpIns_TempRecord.setColumnValue("tzms_stpinsid", m_WflStpInsID, true);
			
			if(stpIns_TempRecord.selectByKey() == true){
				//步骤实例状态
				stpIns_TempRecord.setColumnValue("tzms_stpinsstat", stpStatus);
				//任务处理人
				stpIns_TempRecord.setColumnValue("tzms_tskproid", m_CurrUserId);
				//任务处理时间
				stpIns_TempRecord.setColumnValue("tzms_tskprodt", new Date());
				
				if("4".equals(stpStatus) || "9".equals(stpStatus)){
					//步骤责任人
					String tzms_stpproid = stpIns_TempRecord.getTzString("tzms_stpproid").getValue();
					if(StringUtils.isBlank(tzms_stpproid)) {
						//如果没有步骤责任人（管理员后台处理），默认设置一个责任人为步骤责任人
						tzms_stpproid = sqlQuery.queryForObject("select top 1 tzms_stpproid from tzms_acclst_tbl where tzms_stpinsid=? order by tzms_proidtype desc", 
								new Object[] { m_WflStpInsID }, "String");
						
						stpIns_TempRecord.setColumnValue("tzms_stpproid", tzms_stpproid);
					}
					
					//结束时间
					stpIns_TempRecord.setColumnValue("tzms_stpenddt", new Date());
				}
				if(stpIns_TempRecord.update() == true) {
					/*触发步骤结束后事件*/
					m_WflStepEventInterface.ExecuteEventActions("2", "205", m_WflStepID);
				}
			}
		}catch(TzException e) {
			logger.error("终止步骤实例失败，步骤实例ID："+ m_WflStpInsID +"，异常信息", e);
			throw e;
		}catch(Exception e){
			logger.error("终止步骤实例失败，步骤实例ID："+ m_WflStpInsID +"，异常信息", e);
			throw new TzException("业务流程步骤实例状态更新失败", e);
		}
	}
	
	
	
	
	/**
	 * 解析判断条件路由
	 * @param s_BusProcessDefId		业务流程编号
	 * @param s_rouEndConId			条件动作编号
	 * @param out_StpActList
	 * @param s_ActPath
	 */
	private void analysisConditionRou(String s_BusProcessDefId, String s_rouEndConId, List<TzStpActInfo> out_StpActList, List<String> s_ActPath){
		//查询条件路由
		List<Map<String,Object>> l_ConRouList = sqlQuery.queryForList("select tzms_wf_actcls_tid,tzms_actclsname,tzms_actcon_type,tzms_sysvar_uniqueid,tzms_endact_flg,tzms_is_condition,tzms_stp_end_uniqueid,tzms_cond_end_uniqueid from tzms_wf_actcls_t where tzms_wfl_uniqueid=? and tzms_cond_sta_uniqueid=? and tzms_actcon_flg=1 order by tzms_actcon_type", 
				new Object[]{ s_BusProcessDefId, s_rouEndConId });
		
		int RouCount = 0;
		List<String> l_actPath = new ArrayList<String>();
		
		for(Map<String,Object> l_ConRouMap: l_ConRouList){
			//动作类ID
			String l_actClsID = l_ConRouMap.get("tzms_wf_actcls_tid").toString();
			String l_actClaName = l_ConRouMap.get("tzms_actclsname") == null ? "" : l_ConRouMap.get("tzms_actclsname").toString();
			//路由条件类型
			String l_RouConType = l_ConRouMap.get("tzms_actcon_type").toString();
			//是否进入条件判断
			boolean l_is_condition = (boolean) l_ConRouMap.get("tzms_is_condition");
			//是否结束动作
			boolean l_is_endAction = (boolean) l_ConRouMap.get("tzms_endact_flg");
			
			
			boolean bool_RouTrue = false;
			l_actPath.add(l_actClsID);
			
			switch (l_RouConType) {
			case "1":	//无条件
				bool_RouTrue = true;
				break;
			case "2":	//定制开发接口
				//系统变量
				String l_SysVarId = l_ConRouMap.get("tzms_sysvar_uniqueid") == null ? "" : l_ConRouMap.get("tzms_sysvar_uniqueid").toString();
				if(!"".equals(l_SysVarId)){
					try {						
						/*系统变量解析开始----START*/
						AnalysisDynaSysVar analysisSysVar = new AnalysisDynaSysVar();
						analysisSysVar.setM_SysVarID(l_SysVarId);
						
						//业务数据ID
						String wflDateRecId = sqlQuery.queryForObject("select tzms_wflrecord_uniqueid from tzms_wflins_tbl where tzms_wflinsid=?", 
								new Object[]{ m_WflInstanceID }, "String");
						if(wflDateRecId == null) wflDateRecId = "";
						
						//设置参数
						String[] sysVarParam = { m_WflInstanceID, m_WflStpInsID, wflDateRecId };
						analysisSysVar.setM_SysVarParam(sysVarParam);
						
						Object result = analysisSysVar.GetVarValue();
						
						if(result instanceof String) {
							bool_RouTrue = Boolean.valueOf((String) result);
						}else {
							try {
								bool_RouTrue = (boolean) result;
							}catch(ClassCastException e) {
								bool_RouTrue = false;
								logger.error("路由条件解析失败-定制开发接口解析失败，工作流实例【"+ m_WflInstanceID +"】，步骤实例【"+ m_WflStpInsID +"】，动作类ID【"+ l_actClsID +"】， 返回值不是布尔型", e);
							}
						}
						/*系统变量解析开始----END*/
					} catch (Exception e) {
						bool_RouTrue = false;
						logger.error("路由条件解析失败-定制开发接口解析失败，工作流实例【"+ m_WflInstanceID +"】，步骤实例【"+ m_WflStpInsID +"】，动作类ID【"+ l_actClsID +"】", e);
					}
				}
				break;
			case "3":	//设置表达式
				ExpressionEngineImpl expressionEngine = tzWflObject.getExpressionEngine();
				try {
					bool_RouTrue = expressionEngine.expressionEngine(l_actClsID, m_WflInstanceID, m_WflStpInsID);
				}catch (Exception e) {
					logger.error("路由条件解析失败-设置表达式解析失败，工作流实例【"+ m_WflInstanceID +"】，步骤实例【"+ m_WflStpInsID +"】，动作类ID【"+ l_actClsID +"】", e);
					bool_RouTrue = false;
				}
				break;
			case "4":	//其他
				if(RouCount == 0){
					bool_RouTrue = true;
				}
				break;
			default:
				break;
			}
			
			if(bool_RouTrue){
				RouCount ++;

				List<String> l_actAllPath = new ArrayList<String>();
				l_actAllPath.addAll(s_ActPath);
				l_actAllPath.addAll(l_actPath);
				
				if(l_is_condition && !l_is_endAction){
					//路由结束条件信息
					String l_rouEndConId = l_ConRouMap.get("tzms_cond_end_uniqueid").toString();

					this.analysisConditionRou(s_BusProcessDefId, l_rouEndConId, out_StpActList, l_actAllPath);
				}else{
					//路由结束步骤编号
					String l_rouEndStpId = l_ConRouMap.get("tzms_stp_end_uniqueid") == null ? "" 
							: l_ConRouMap.get("tzms_stp_end_uniqueid").toString();
					
					TzStpActInfo tzStpActInfo = new TzStpActInfo();
					tzStpActInfo.setM_ActClsId(l_actClsID);
					tzStpActInfo.setM_ActClsName(l_actClaName);
					tzStpActInfo.setM_IsEndAction(l_is_endAction);
					tzStpActInfo.setM_IsConAction(true);
					
					if(!l_is_endAction){
						tzStpActInfo.setM_NextStepId(l_rouEndStpId);
						//下一步骤责任人列表
						List<String> userList = this.getNextStepRoleUserList(l_rouEndStpId);
						tzStpActInfo.setM_NextUserList(userList);
					}
					
					tzStpActInfo.setM_RouAllPathList(l_actAllPath);
					
					out_StpActList.add(tzStpActInfo);
				}
			}
		}
	}
	
	
	/**
	 * 获取当前步骤下一步骤角色人员列表
	 * @param s_EndStepId	下一步骤编号
	 * @return
	 */
	private List<String> getNextStepRoleUserList(String s_EndStepId){
		
		List<String> userList = new ArrayList<String>();
		
		//获取步骤责任人角色
		String l_StpRoleID = sqlQuery.queryForObject("select tzms_wfrole_uniqueid from tzms_wflstp_t where tzms_wflstp_tid=?", 
				new Object[]{ s_EndStepId }, "String");
		
		if(StringUtils.isNotEmpty(l_StpRoleID)){
			//创建角色对象			
			AnalysisDynaRole analysisRole = new AnalysisDynaRole();
			userList = analysisRole.getUserIds(m_WflInstanceID, m_WflStpInsID, m_CurrUserId, l_StpRoleID);
			if(userList == null) {
				userList = new ArrayList<String>();
			}
		}
		
		return userList;
	}
	

	
	/**
	 * 生成会签人步骤实例
	 * @param o_Calendar		日历本
	 * @param s_BusProcessID	业务流程ID
	 * @param s_asignUerID		加签人用户ID
	 * @param pre_stpInsID		加签步骤上一步骤实例编号
	 * @param zrr_stpInsID		加签步骤责任人步骤实例编号
	 * @param s_PriLevelID		步骤优先级
	 * @return
	 * @throws TzException
	 */
	private boolean CreateHQRWorkflowStep(TzWflCalendar o_Calendar, String s_BusProcessID, String s_asignUerID, String pre_stpInsID, String zrr_stpInsID, String s_PriLevelID) throws TzException {
		List<String> userList = new ArrayList<String>();
		userList.add(s_asignUerID);
		
		//创建下一顺序加签人生成步骤实例
		TzWflStep aSignTzWflStep = new TzWflStep(m_CurrUserId);
		boolean createOK = aSignTzWflStep.CreateWorkflowStep1(o_Calendar, s_BusProcessID, m_WflInstanceID, m_WflStepID, "", s_PriLevelID, "6", "2", pre_stpInsID, true, userList);
		
		if(createOK){
			String asign_stepInsID = aSignTzWflStep.getM_WflStpInsID();
			//更新加签人列表
			sqlQuery.update("update tzms_asign_psn_tbl set tzms_asign_stpinsid=? where tzms_stpinsid=? and tzms_stpproid=?", 
					new Object[]{ asign_stepInsID, zrr_stpInsID, s_asignUerID });
			
			
			//插入步骤实例流转关系tzms_stpins_pre_tbl
			TzRecord stpPre_TempRecord = tzGDObject.createRecord("tzms_stpins_pre_tbl");
			stpPre_TempRecord.setColumnValue("tzms_stpinsid", asign_stepInsID);
			stpPre_TempRecord.setColumnValue("tzms_prestpinsid", pre_stpInsID);
			stpPre_TempRecord.insert();
			
			
			//创建会签步骤成功后，创建步骤动作实例
			CommonFun commonFun = new CommonFun();
			String actionInsID = commonFun.GenerateGUID_1("ACTINS", 30);
			
			//工作流实例编号、步骤实例编号、上一步步骤实例编号
			String tzms_wflinsid = m_WflInstanceID;
			//String tzms_stpinsid = zrr_stpInsID;
			String tzms_prestpinsid = pre_stpInsID;
			
//			String tzms_actclsid = sqlQuery.queryForObject("select tzms_actclsid from tzms_actins_tbl where tzms_wflinsid=? and tzms_stpinsid=? and tzms_nextstpinsid=?", 
//					new Object[]{ tzms_wflinsid, tzms_prestpinsid, tzms_stpinsid }, "String");

			//创建动作实例表
			TzRecord tmp_actIndRecord = tzGDObject.createRecord("tzms_actins_tbl");
			//动作实例ID
			tmp_actIndRecord.setColumnValue("tzms_actinsid", actionInsID);
			//流转动作ID
			//tmp_actIndRecord.setColumnValue("tzms_actclsid", tzms_actclsid);
			tmp_actIndRecord.setColumnValue("tzms_actclsid", WFL_ADD_SIGN_ACTCLSID);
			//流程实例ID
			tmp_actIndRecord.setColumnValue("tzms_wflinsid", tzms_wflinsid);
			//步骤实例ID
			tmp_actIndRecord.setColumnValue("tzms_stpinsid", tzms_prestpinsid);
			//下一步步骤实例ID
			tmp_actIndRecord.setColumnValue("tzms_nextstpinsid", asign_stepInsID);
			//下一步步骤ID
			tmp_actIndRecord.setColumnValue("tzms_nextstpid", m_WflStepID);
			//动作实例状态
			tmp_actIndRecord.setColumnValue("tzms_actinsstate", "FIN");

			tmp_actIndRecord.insert();
			
			/***发送通知***/
			List<String> nextUserList = aSignTzWflStep.getM_stepUserList();
			TzWflNotice wflNotice = new TzWflNotice(m_WflInstanceID, asign_stepInsID, m_WflStepID);
			wflNotice.SendWorkflowNotice(nextUserList);
		}

		return true;
	}
	
	
	
	/**
	 * 工作流自动同意
	 * @param o_Calendar
	 * @param userList
	 * @throws Exception 
	 */
	private void WorkflowAutoAgree(TzWflCalendar o_Calendar) throws TzException {
		
		List<String> userList = m_stepUserList;
		boolean l_autoAgreeFlg = m_WflStepCfgRecord.getTzBoolean("tzms_dpyauto_agree").getValue();
		
		boolean l_autoAgree = false;
		if(l_autoAgreeFlg){
			/*此处调用自动同意功能解析引擎*/
			AnalysisFlowFunction analysisWflFlow = new AnalysisFlowFunction();
			l_autoAgree = analysisWflFlow.analysisAutoAgree(m_WflInstanceID, m_WflStpInsID); 
			
			if(l_autoAgree && userList.size() == 1){
				//自动签收当前生成的步骤
//				TzWflStep tzWflStep = new TzWflStep(userList.get(0));
//				tzWflStep.CreateWorkflowStep2(m_WflInstanceID, m_WflStpInsID);
//				
//				ErrorMessage signErrMsg = new ErrorMessage();
//				boolean signOK = tzWflStep.WorkflowStepSignByUser(userList.get(0), signErrMsg);
//				if(signOK){
//					//提交当前步骤
//					String l_BusProcessID = m_WflStepCfgRecord.getTzString("tzms_wfcldn_uniqueid").getValue();
//					
//					tzWflStep.WorkflowSubmitOrAgree(o_Calendar, l_BusProcessID, "1");
//				}
				try {
					TzWorkflow tzWorkflow = new TzWorkflow(userList.get(0));
					//设置自动同意不发送通知
					tzWorkflow.setNotSendNotice(true);
					tzWorkflow.CreateWorkflow1(m_WflInstanceID, m_WflStpInsID);
					
					ErrorMessage signErrMsg = new ErrorMessage();
					boolean signOK = tzWorkflow.WorkflowSign(signErrMsg);
					if(signOK) {
						ErrorMessage ErrorMsg_OUT = new ErrorMessage();
						boolean agreeOk = tzWorkflow.WorkflowSubmit("同意", ErrorMsg_OUT);
						if(agreeOk == false) {
							logger.error("自动同意失败");
						}
					}else {
						logger.error("自动同意失败，签收失败");
					}
				}catch (Exception e) {
					e.printStackTrace();
					logger.error("自动同意失败，异常信息：", e);
				}
			}
		}
	}

	
	/**
	 * 检查步骤责任人
	 * @param actInfo
	 * @throws TzException
	 */
	private void checkNextStepUser(TzStpActInfo actInfo)throws TzException {
		//进入下一步步骤路由
		String l_NextStpID = actInfo.getM_NextStepId();
		List<String> l_NextStpUsers = actInfo.getM_NextUserList();
		//结束动作
		boolean isEndAction= actInfo.isM_IsEndAction();

		if(isEndAction == false 
				&& (l_NextStpUsers == null || l_NextStpUsers.size() <= 0)) {
			String l_stpName = sqlQuery.queryForObject("select tzms_wflstpname from tzms_wflstp_t where tzms_wflstp_tid=?", 
					new Object[]{ l_NextStpID }, "String");
			throw new TzException("下一步骤【"+ l_stpName +"】责任人为空");
		}
	}
	
	
	/**
	 * 检查下一步步骤责任人
	 * @param s_stpActInfoList
	 * @throws TzException
	 */
	private void checkNextStepsUserExists(List<TzStpActInfo> s_stpActInfoList) throws TzException {
		for(TzStpActInfo actInfo: s_stpActInfoList){
			if(m_WorkflowActionInfo != null){	//多路径
				if(actInfo.isM_IsConAction() != true){
					this.checkNextStepUser(actInfo);
				}
			}else{
				this.checkNextStepUser(actInfo);
			}
		}
		if(m_WorkflowActionInfo != null){
			this.checkNextStepUser(m_WorkflowActionInfo);
		}
	}
	
	
	/**
	 * 路由条件检查，弃用
	 * @param s_BusProcessID
	 * @param l_conditionID
	 * @return
	 */
	@SuppressWarnings("unused")
	private boolean checkConditionAction(String s_BusProcessID, String s_WflInsID, String l_conditionID){
		boolean checkOK = true;
		
		Map<String,Object> conActMap = sqlQuery.queryForMap("select tzms_actcon_flg,tzms_stp_sta_uniqueid,tzms_cond_sta_uniqueid from tzms_wf_actcls_t where tzms_wfl_uniqueid=? and tzms_cond_end_uniqueid=?", 
				new Object[]{ s_BusProcessID, l_conditionID });
		if(conActMap != null){
			//启用路由条件
			boolean l_actconFlg = conActMap.get("tzms_actcon_flg") == null ? false : (boolean) conActMap.get("tzms_actcon_flg");
			
			if(l_actconFlg == true){
				/*条件路由*/
				String l_condID = conActMap.get("tzms_cond_sta_uniqueid").toString();
				
				checkOK = this.checkConditionAction(s_BusProcessID, s_WflInsID, l_condID);
			}else{
				/*步骤路由*/
				String l_stepID = conActMap.get("tzms_stp_sta_uniqueid").toString();
				
				//查询该步骤是否有存活步骤实例，需要去除抄送实例
				/*1-激活、2-撤回、3-处理中、4-结束、5-提前终止、6-未签收、7-退回、8-已签收、9-转发*/
				int aliveStpInsCount = sqlQuery.queryForObject("select count(*) from tzms_stpins_tbl where tzms_wflinsid=? and tzms_wflstpid=? and tzms_asgmethod<>'1' and tzms_stpinsstat in('1','3','6','8')", 
						new Object[]{ s_WflInsID, l_stepID }, "int");
				
				if(aliveStpInsCount > 0){
					return false;
				}
				
				//继续检查上一步骤
				checkOK = this.checkOperatingAuthorization(s_WflInsID, l_stepID);
				if(checkOK == false){
					return checkOK;
				}
			}
		}
		
		return checkOK;
	}
	
	
	
	/**
	 * 检查流程步骤操作权限
	 * 如果存在当前步骤实例的前续存活步骤实例，则不允许操作（除了“保存”和“已阅”，其他操作都不允许）。
	 * @param s_WflInsID		业务流程实例ID
	 * @param s_StpID			要检查的步骤ID
	 * @return
	 */
	public boolean checkOperatingAuthorization(String s_WflInsID, String s_StpID){
		boolean checkOK = true;
		
		//如果当前步骤是起始步骤，直接返回true
		String isFirstStp = sqlQuery.queryForObject("select 'Y' from tzms_wflstp_t where tzms_wflstp_tid=? and tzms_stptype=1", 
				new Object[]{ s_StpID }, "String");
		if("Y".equals(isFirstStp)){
			return checkOK;
		}
		
		//查询业务流程ID
		String s_BusProcessID = sqlQuery.queryForObject("select tzms_wfcldn_uniqueid from tzms_wflins_tbl where tzms_wflinsid=?", 
				new Object[]{ s_WflInsID }, "String");
		
//		//根据当前步骤查询前续步骤
//		List<Map<String,Object>> preActList = sqlQuery.queryForList("select tzms_actcon_flg,tzms_stp_sta_uniqueid,tzms_cond_sta_uniqueid from tzms_wf_actcls_t where tzms_wfl_uniqueid=? and tzms_stp_end_uniqueid=?", 
//				new Object[]{ s_BusProcessID, s_StpID });
//		
//		for(Map<String,Object> preActMap: preActList){
//			//启用路由条件
//			boolean l_actconFlg = preActMap.get("tzms_actcon_flg") == null ? false : (boolean) preActMap.get("tzms_actcon_flg");
//			
//			if(l_actconFlg == true){
//				/*条件路由*/
//				String l_conditionID = preActMap.get("tzms_cond_sta_uniqueid").toString();
//				
//				checkOK = this.checkConditionAction(s_BusProcessID, s_WflInsID, l_conditionID);
//			}else{
//				/*步骤路由*/
//				String l_stepID = preActMap.get("tzms_stp_sta_uniqueid").toString();
//				
//				//查询该步骤是否有存活步骤实例，需要去除抄送实例
//				/*1-激活、2-撤回、3-处理中、4-结束、5-提前终止、6-未签收、7-退回、8-已签收、9-转发*/
//				int aliveStpInsCount = sqlQuery.queryForObject("select count(*) from tzms_stpins_tbl where tzms_wflinsid=? and tzms_wflstpid=? and tzms_asgmethod<>'1' and tzms_stpinsstat in('1','3','6','8')", 
//						new Object[]{ s_WflInsID, l_stepID }, "int");
//				
//				if(aliveStpInsCount > 0){
//					return false;
//				}
//				
//				//继续检查上一步骤
//				checkOK = this.checkOperatingAuthorization(s_WflInsID, l_stepID);
//			}
//			
//			if(checkOK == false){
//				return checkOK;
//			}
//		}
		
		//获取所有前续步骤
		TzWorkflowFunc TzWorkflowFunc = new TzWorkflowFunc();
		Set<String> preStpSet = TzWorkflowFunc.getAllBeforeStepByStepID(s_BusProcessID, s_StpID);
		
		if(preStpSet != null && preStpSet.size() > 0) {
			for(String stpId: preStpSet) {
				//查询该步骤是否有存活步骤实例，需要去除抄送实例
				/*1-激活、2-撤回、3-处理中、4-结束、5-提前终止、6-未签收、7-退回、8-已签收、9-转发*/
				int aliveStpInsCount = sqlQuery.queryForObject("select count(*) from tzms_stpins_tbl where tzms_wflinsid=? and tzms_wflstpid=? and tzms_asgmethod<>'1' and tzms_stpinsstat in('1','3','6','8')", 
						new Object[]{ s_WflInsID, stpId }, "int");
				
				if(aliveStpInsCount > 0){
					checkOK = false;
					break;
				}
			}
		}
		
		return checkOK;
	}
	
	
	
	/**
	 * 杀死所有动作条件后续步骤的所有存活步骤实例，弃用
	 * @param s_WflInsID
	 * @param s_BusProcessID
	 * @param s_condID
	 */
	@SuppressWarnings("unused")
	private void excConditionAction(String s_WflInsID, String s_BusProcessID, String s_condID){
		//查询条件动作
		List<Map<String,Object>> condActList = sqlQuery.queryForList("select tzms_is_condition,tzms_stp_end_uniqueid,tzms_cond_end_uniqueid from tzms_wf_actcls_t where tzms_wfl_uniqueid=? and tzms_cond_sta_uniqueid=? and tzms_endact_flg=0", 
				new Object[]{ s_BusProcessID, s_condID });
		
		for(Map<String,Object> condActMap: condActList){
			boolean c_isCondition = condActMap.get("tzms_is_condition") == null ? false 
					: (boolean) condActMap.get("tzms_is_condition");
			
			if(c_isCondition == true){
				String l_condID = condActMap.get("tzms_cond_end_uniqueid").toString();
				
				this.excConditionAction(s_WflInsID, s_BusProcessID, l_condID);
			}else{
				String l_stpID = condActMap.get("tzms_stp_end_uniqueid").toString();
				
				//杀死该步骤下所有存活的进程实例
				sqlQuery.update("update tzms_stpins_tbl set tzms_stpinsstat='4' where tzms_wflinsid=? and tzms_wflstpid=? and tzms_asgmethod<>'1' and tzms_stpinsstat in('1','3','6','8')", 
						new Object[]{ s_WflInsID, l_stpID });
				
				//继续下一步骤
				this.killAllAliveStpInsAfterStpID(s_WflInsID, l_stpID);
			}
		}
	}
	
	
	
	
	/**
	 * 杀死所有当前步骤后续步骤的所有存活步骤实例
	 * @param s_WflInsID		业务流程实例ID
	 * @param s_StpID			步骤ID
	 */
	public void killAllAliveStpInsAfterStpID(String s_WflInsID, String s_StpID){
		//查询业务流程ID
		String s_BusProcessID = sqlQuery.queryForObject("select tzms_wfcldn_uniqueid from tzms_wflins_tbl where tzms_wflinsid=?", 
				new Object[]{ s_WflInsID }, "String");
		
//		//根据非结束动作查询后续步骤
//		List<Map<String,Object>> aftActionList = sqlQuery.queryForList("select tzms_wf_actcls_tid,tzms_is_condition,tzms_stp_end_uniqueid,tzms_cond_end_uniqueid from tzms_wf_actcls_t where tzms_wfl_uniqueid=? and tzms_stp_sta_uniqueid=? and tzms_endact_flg=0", 
//				new Object[]{ s_BusProcessID, s_StpID });
//		
//		for(Map<String,Object> aftActionMap: aftActionList){
//			//是否进入进入条件判断
//			boolean isCondition = aftActionMap.get("tzms_is_condition") == null ? false 
//					: (boolean) aftActionMap.get("tzms_is_condition");
//			
//			if(isCondition == true){
//				String l_condID = aftActionMap.get("tzms_cond_end_uniqueid").toString();
//				
//				this.excConditionAction(s_WflInsID, s_BusProcessID, l_condID);
//			}else{
//				String l_actClsId = aftActionMap.get("tzms_wf_actcls_tid").toString();
//				String l_stpID = aftActionMap.get("tzms_stp_end_uniqueid").toString();
//				
//				//杀死该步骤下所有存活的进程实例
//				sqlQuery.update("update tzms_stpins_tbl set tzms_stpinsstat='5' where tzms_wflinsid=? and tzms_wflstpid=? and tzms_asgmethod<>'1' and tzms_stpinsstat in('1','3','6','8')", 
//						new Object[]{ s_WflInsID, l_stpID });
//				
//				//设置“等待”动作实例为“结束”
//				sqlQuery.update("update tzms_actins_tbl set tzms_actinsstate='FIN' where tzms_wflinsid=? and tzms_actclsid=?", 
//						new Object[]{ s_WflInsID, l_actClsId });
//				
//				//继续下一步骤
//				this.killAllAliveStpInsAfterStpID(s_WflInsID, l_stpID);
//			}
//		}
		
		//获取所有后续步骤
		TzWorkflowFunc TzWorkflowFunc = new TzWorkflowFunc();
		Set<String> afterStpSet = TzWorkflowFunc.getAllAfterStepByStepID(s_BusProcessID, s_StpID);
		
		if(afterStpSet != null && afterStpSet.size() > 0) {
			for(String stepId: afterStpSet) {
				//杀死后续步骤下所有存活的进程实例
				sqlQuery.update("update tzms_stpins_tbl set tzms_stpinsstat='4' where tzms_wflinsid=? and tzms_wflstpid=? and tzms_asgmethod<>'1' and tzms_stpinsstat in('1','3','6','8')", 
						new Object[]{ s_WflInsID, stepId });
				
				//设置“等待”动作实例为“结束”
				sqlQuery.update("update tzms_actins_tbl set tzms_actinsstate='FIN' where tzms_wflinsid=? and tzms_nextstpid=?", 
						new Object[]{ s_WflInsID, stepId });
			}
		}
	}
	
	
	


	/**
	 * 根据指定步骤编号实例化工作流步骤
	 * @param o_Calendar		日历本
	 * @param s_BusProcessID	业务流程编号
	 * @param s_WorkflowInsID	工作流实例编号
	 * @param s_WflStepID		步骤编号
	 * @param s_UserID			用户ID，如果有值，直接被该用户签收
	 * @param priorityLevel		优先级明细
	 * @param s_StpStatus		步骤实例存活状态：1-激活、2-撤回、3-处理中、4-结束、5-提前终止、6-未签收、7-退回、8-已签收、9-转发
	 * @param s_AsgMed			任务分配方式：1-抄送、2-会签人、3-新建、4-流转、5-转发
	 * @param s_PreStpInsID		上一步骤实例编号
	 * @param userList			责任人用户列表
	 * @return
	 */
	public boolean CreateWorkflowStep0(TzWflCalendar o_Calendar, String s_BusProcessID, String s_WorkflowInsID, String s_WflStepID, 
			String s_UserID, String s_PriLevelID, String s_StpStatus, String s_AsgMed, String s_PreStpInsID, List<String> userList){
		try{
			if(userList == null || userList.size() == 0) {
				throw new TzException("步骤责任人为空，创建业务流程实例失败");
			}
			
			//生成工作流步骤实例编号
			CommonFun commonFun = new CommonFun();
			String tmp_WflStpInsID = commonFun.GenerateSeqGUID("SPINS", 30);
			
			 //工作流步骤实例属性：工作流实例编号、步骤实例编号及步骤编号赋值
			m_WflInstanceID = s_WorkflowInsID;
			m_WflStpInsID = tmp_WflStpInsID;
			m_WflStepID = s_WflStepID;
			

			//创建事件接口对象
			m_WflStepEventInterface = new TzWflEvent();
			m_WflStepEventInterface.setM_WflInstanceID(s_WorkflowInsID);
			m_WflStepEventInterface.setM_WflStpInsID(tmp_WflStpInsID);
			m_WflStepEventInterface.setM_userId(m_CurrUserId);
			
			/*【执行步骤初始化前事件】*/
			m_WflStepEventInterface.ExecuteEventActions("2", "201", s_WflStepID);
			
			//获取工作流业务类别配置信息
			TzRecord wflStp_TempRecord = null;
			try {
				wflStp_TempRecord = tzGDObject.createRecord("tzms_wflstp_tBase");
				wflStp_TempRecord.setColumnValue("tzms_wflstp_tid", s_WflStepID, true);
				
				if(wflStp_TempRecord.selectByKey() == false){
					throw new TzException("【系统配置错误】获取业务流程步骤配置信息失败");
				}
				
				m_WflStepCfgRecord = wflStp_TempRecord;
			} catch (TzException e) {
				e.printStackTrace();
				return false;
			}
			
			//查询步骤超时
			int l_StpOverTime = 0;
			Integer l_StpOver = null;
//			if(StringUtils.isNotEmpty(s_PriLevelID)){
//				l_StpOver = sqlQuery.queryForObject("select tzms_wflovertime_value from tzms_stpovertime_t where tzms_wflcldnid=? and tzms_wflstpid=? and tzms_pridtlid=? and statecode=1", 
//						new Object[]{ s_BusProcessID, s_WflStepID, s_PriLevelID }, "Integer");
//			}
//			if(l_StpOver != null && l_StpOver > 0){
//				l_StpOverTime = l_StpOver;
//			}
			
			//是否启用步骤超时
			boolean timeout_flg = wflStp_TempRecord.getTzBoolean("tzms_stp_timeout_flg").getValue();
			if(timeout_flg == true) {
				l_StpOver = wflStp_TempRecord.getTzInt("tzms_stp_timeout_days").getValue();
				if(l_StpOver != null && l_StpOver > 0){
					l_StpOverTime = l_StpOver;
				}
			}
			
			//创建工作流步骤实例
			TzRecord tmp_wflStpInsRecord = tzGDObject.createRecord("tzms_stpins_tbl");
			//设置工作流实例编号
			tmp_wflStpInsRecord.setColumnValue("tzms_wflinsid", s_WorkflowInsID);
			//设置步骤实例编号
			tmp_wflStpInsRecord.setColumnValue("tzms_stpinsid", tmp_WflStpInsID);
			//步骤实例存活状态
			tmp_wflStpInsRecord.setColumnValue("tzms_stpinsstat", s_StpStatus);
			//步骤开始时间
			tmp_wflStpInsRecord.setColumnValue("tzms_stpstartdt", new Date());
			//任务分配方式
			tmp_wflStpInsRecord.setColumnValue("tzms_asgmethod", s_AsgMed);
			//工作流步骤编号
			tmp_wflStpInsRecord.setColumnValue("tzms_wflstpid", s_WflStepID);
			//上一步工作流步骤实例编号
			tmp_wflStpInsRecord.setColumnValue("tzms_prestpinsid", s_PreStpInsID);
			//优先级
			tmp_wflStpInsRecord.setColumnValue("tzms_pridtlid", s_PriLevelID);
			//处理时长
			tmp_wflStpInsRecord.setColumnValue("tzms_tskprotm", l_StpOverTime);
			
			//步骤任务责任人编号
			if(StringUtils.isNotEmpty(s_UserID)){
				tmp_wflStpInsRecord.setColumnValue("tzms_stpproid", s_UserID);
				//任务处理签收时间
				tmp_wflStpInsRecord.setColumnValue("tzms_stpsigndt", new Date());
			}
			//步骤超时设置
			if(o_Calendar != null && l_StpOverTime > 0){
				Date l_OverTime = o_Calendar.GetEndTimingDate(new Date(), l_StpOverTime);
				//截止超时时间
				tmp_wflStpInsRecord.setColumnValue("tzms_tmoutdt", l_OverTime);
			}
			tmp_wflStpInsRecord.setColumnValue("tzms_tmouflg", "N");
			
			//创建人
			tmp_wflStpInsRecord.setColumnValue("ROW_ADDED_OPRID", m_CurrUserId);
			//创建时间
			tmp_wflStpInsRecord.setColumnValue("ROW_ADDED_DTTM", new Date());
			//最后修改人
			tmp_wflStpInsRecord.setColumnValue("ROW_LASTMANT_OPRID", m_CurrUserId);
			//最后修改时间
			tmp_wflStpInsRecord.setColumnValue("ROW_LASTMANT_DTTM", new Date());
			
			//生成步骤任务实例摘要信息
			TzWflAbstract tzWflAbstract = new TzWflAbstract(m_WflInstanceID, m_WflStpInsID);
			String l_abstractStr = tzWflAbstract.getAbstractString();
			
			tmp_wflStpInsRecord.setColumnValue("tzms_abstract", l_abstractStr);
			
			//保存工作流步骤实例表
			m_WflStepInsRecord = tmp_wflStpInsRecord;
			if(tmp_wflStpInsRecord.insert() == false){
				throw new TzException("保存业务流程任务失败");
			}
			
			//将指定的用户列表插入到步骤实例访问用户关系表中
			List<String> s_UserIdList = this.InserUserListStpInstance(userList, s_BusProcessID, tmp_WflStpInsID, s_AsgMed);
			
			//步骤用户列表，可签收步骤任务实例
			m_stepUserList = s_UserIdList;
			
			//解析步骤知会角色，并将解析后的知会人员插入到知会人列表
			this.InsertCopyRoleToStpInstance();
			
			/*【执行步骤初始化后事件】*/
			m_WflStepEventInterface.ExecuteEventActions("2", "202", s_WflStepID);

		}catch (Exception e) {
			e.printStackTrace();
			logger.error("创建步骤实例失败，业务流程编号："+ s_BusProcessID +"，工作流实例ID："+ s_WorkflowInsID +"，流程步骤编号："+ s_WflStepID +"，异常信息：", e);
			return false;
		}
		return true;
	}
	
	
	
	
	/**
	 * 根据指定步骤编号实例化工作流步骤
	 * @param o_Calendar		日历本
	 * @param s_BusProcessID	业务流程编号
	 * @param s_WorkflowInsID	工作流实例编号
	 * @param s_WflStepID		步骤编号
	 * @param s_UserID			用户ID
	 * @param s_StpStatus		步骤实例存活状态：1-激活、2-撤回、3-处理中、4-结束、5-提前终止、6-未签收、7-退回、8-已签收、9-转发
	 * @param s_AsgMed			任务分配方式：1-抄送、2-会签人、3-新建、4-流转、5-转发
	 * @param s_PreStpInsID		上一步骤实例编号
	 * @param s_is_BF			是否并发
	 * @param userList			用户列表
	 * @return
	 */
	public boolean CreateWorkflowStep1(TzWflCalendar o_Calendar, String s_BusProcessID, String s_WorkflowInsID, String s_WflStepID, 
			String s_UserID, String s_PriLevelID, String s_StpStatus, String s_AsgMed, String s_PreStpInsID, boolean bool_is_BF, List<String> userList){
		try{
			if(userList == null || userList.size() == 0) {
				throw new TzException("步骤责任人为空，创建业务流程实例失败");
			}
			
			//生成工作流步骤实例编号
			CommonFun commonFun = new CommonFun();
			String tmp_WflStpInsID = commonFun.GenerateSeqGUID("SPINS", 30);
			
			 //工作流步骤实例属性：工作流实例编号、步骤实例编号及步骤编号赋值
			m_WflInstanceID = s_WorkflowInsID;
			m_WflStpInsID = tmp_WflStpInsID;
			m_WflStepID = s_WflStepID;
			

			//创建事件接口对象
			m_WflStepEventInterface = new TzWflEvent();
			m_WflStepEventInterface.setM_WflInstanceID(s_WorkflowInsID);
			m_WflStepEventInterface.setM_WflStpInsID(tmp_WflStpInsID);
			m_WflStepEventInterface.setM_userId(m_CurrUserId);
			
			/*【执行步骤初始化前事件】*/
			m_WflStepEventInterface.ExecuteEventActions("2", "201", s_WflStepID);
			
			//获取工作流业务类别配置信息
			TzRecord wflStp_TempRecord = null;
			try {
				wflStp_TempRecord = tzGDObject.createRecord("tzms_wflstp_tBase");
				wflStp_TempRecord.setColumnValue("tzms_wflstp_tid", s_WflStepID, true);
				
				if(wflStp_TempRecord.selectByKey() == false){
					throw new TzException("【系统配置错误】获取业务流程步骤配置信息失败");
				}
				
				m_WflStepCfgRecord = wflStp_TempRecord;
			} catch (TzException e) {
				e.printStackTrace();
				return false;
			}
			
			//查询步骤超时
			int l_StpOverTime = 0;
			Integer l_StpOver = null;
//			if(StringUtils.isNotEmpty(s_PriLevelID)){
//				l_StpOver = sqlQuery.queryForObject("select tzms_wflovertime_value from tzms_stpovertime_t where tzms_wflcldnid=? and tzms_wflstpid=? and tzms_pridtlid=? and statecode=1", 
//						new Object[]{ s_BusProcessID, s_WflStepID, s_PriLevelID }, "Integer");
//			}
//			if(l_StpOver != null && l_StpOver > 0){
//				l_StpOverTime = l_StpOver;
//			}
			
			//是否启用步骤超时
			boolean timeout_flg = wflStp_TempRecord.getTzBoolean("tzms_stp_timeout_flg").getValue();
			if(timeout_flg == true) {
				l_StpOver = wflStp_TempRecord.getTzInt("tzms_stp_timeout_days").getValue();
				if(l_StpOver != null && l_StpOver > 0){
					l_StpOverTime = l_StpOver;
				}
			}
			
			//创建工作流步骤实例
			TzRecord tmp_wflStpInsRecord = tzGDObject.createRecord("tzms_stpins_tbl");
			//设置工作流实例编号
			tmp_wflStpInsRecord.setColumnValue("tzms_wflinsid", s_WorkflowInsID);
			//设置步骤实例编号
			tmp_wflStpInsRecord.setColumnValue("tzms_stpinsid", tmp_WflStpInsID);
			//步骤实例存活状态
			tmp_wflStpInsRecord.setColumnValue("tzms_stpinsstat", s_StpStatus);
			//步骤开始时间
			tmp_wflStpInsRecord.setColumnValue("tzms_stpstartdt", new Date());
			//任务分配方式
			tmp_wflStpInsRecord.setColumnValue("tzms_asgmethod", s_AsgMed);
			//工作流步骤编号
			tmp_wflStpInsRecord.setColumnValue("tzms_wflstpid", s_WflStepID);
			//上一步工作流步骤实例编号
			tmp_wflStpInsRecord.setColumnValue("tzms_prestpinsid", s_PreStpInsID);
			//优先级
			tmp_wflStpInsRecord.setColumnValue("tzms_pridtlid", s_PriLevelID);
			
			//处理时长
			tmp_wflStpInsRecord.setColumnValue("tzms_tskprotm", l_StpOverTime);
			
			//步骤任务责任人编号
			if(StringUtils.isNotEmpty(s_UserID)){
				tmp_wflStpInsRecord.setColumnValue("tzms_stpproid", s_UserID);
				//任务处理签收时间
				tmp_wflStpInsRecord.setColumnValue("tzms_stpsigndt", new Date());
			}
			//步骤超时设置
			if(o_Calendar != null && l_StpOverTime > 0){
				Date l_OverTime = o_Calendar.GetEndTimingDate(new Date(), l_StpOverTime);
				//截止超时时间
				tmp_wflStpInsRecord.setColumnValue("tzms_tmoutdt", l_OverTime);
			}
			tmp_wflStpInsRecord.setColumnValue("tzms_tmouflg", "N");
			
			//是否并发
			tmp_wflStpInsRecord.setColumnValue("tzms_is_bf", bool_is_BF);
			//创建人
			tmp_wflStpInsRecord.setColumnValue("ROW_ADDED_OPRID", m_CurrUserId);
			//创建时间
			tmp_wflStpInsRecord.setColumnValue("ROW_ADDED_DTTM", new Date());
			//最后修改人
			tmp_wflStpInsRecord.setColumnValue("ROW_LASTMANT_OPRID", m_CurrUserId);
			//最后修改时间
			tmp_wflStpInsRecord.setColumnValue("ROW_LASTMANT_DTTM", new Date());
			
			
			
			//生成步骤任务实例摘要信息
			TzWflAbstract tzWflAbstract = new TzWflAbstract(m_WflInstanceID, m_WflStpInsID);
			String l_abstractStr = tzWflAbstract.getAbstractString();
			
			tmp_wflStpInsRecord.setColumnValue("tzms_abstract", l_abstractStr);
			
			//保存工作流步骤实例表
			m_WflStepInsRecord = tmp_wflStpInsRecord;
			if(tmp_wflStpInsRecord.insert() == false){
				throw new TzException("保存业务流程任务失败");
			}
			
			//将指定的用户列表插入到步骤实例访问用户关系表中
			List<String> s_UserIdList = this.InserUserListStpInstance(userList, s_BusProcessID, m_WflStpInsID, s_AsgMed);
			
			//步骤用户列表，可签收步骤任务实例
			m_stepUserList = s_UserIdList;
			
			//解析步骤知会角色，并将解析后的知会人员插入到知会人列表
			this.InsertCopyRoleToStpInstance();
			
			/*【执行步骤初始化后事件】*/
			m_WflStepEventInterface.ExecuteEventActions("2", "202", s_WflStepID);
		
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("创建步骤实例失败，业务流程编号："+ s_BusProcessID +"，工作流实例ID："+ s_WorkflowInsID +"，流程步骤编号："+ s_WflStepID +"，异常信息：", e);
			return false;
		}
		return true;
	}
	
	
	
	
	/**
	 * 根据步骤实例编号构建步骤实例对象
	 * @param s_WflInsID	工作流实例编号
	 * @param s_WflStepInsID	步骤实例编号
	 * @return
	 * @throws Exception 
	 */
	public boolean CreateWorkflowStep2(String s_WflInsID, String s_WflStepInsID) throws TzException {
		try{
			m_WflInstanceID = s_WflInsID;
			m_WflStpInsID = s_WflStepInsID;
			
			/*获取步骤实例信息*/
			TzRecord stpIns_TempRecord = tzGDObject.createRecord("tzms_stpins_tbl");
			stpIns_TempRecord.setColumnValue("tzms_wflinsid", s_WflInsID, true);
			stpIns_TempRecord.setColumnValue("tzms_stpinsid", s_WflStepInsID, true);
			if(stpIns_TempRecord.selectByKey() == false){
				throw new TzException("获取业务流程步骤实例失败");
			}
			m_WflStepInsRecord = stpIns_TempRecord;
			
			//流程步骤编号
			m_WflStepID = stpIns_TempRecord.getTzString("tzms_wflstpid").getValue();
			
			/*获取步骤配置信息*/
			TzRecord stpCfg_TempRecord = tzGDObject.createRecord("tzms_wflstp_tBase");
			stpCfg_TempRecord.setColumnValue("tzms_wflstp_tid", m_WflStepID, true);
			if(stpCfg_TempRecord.selectByKey() == false){
				throw new TzException("获取业务流程步骤配置信息失败");
			}
			m_WflStepCfgRecord = stpCfg_TempRecord;
			
			//步骤责任人
			List<Map<String,Object>> stpUserList = sqlQuery.queryForList("select tzms_stpproid from tzms_acclst_tbl where tzms_stpinsid=?", 
					new Object[] {s_WflStepInsID});
			if(stpUserList != null && stpUserList.size() > 0) {
				if(m_stepUserList == null) {
					m_stepUserList = new ArrayList<String>();
				}
				for(Map<String,Object> stpUserMap: stpUserList) {
					m_stepUserList.add(stpUserMap.get("tzms_stpproid").toString());
				}
			}
			
			/*创建步骤事件接口对象*/
			m_WflStepEventInterface = new TzWflEvent();
			m_WflStepEventInterface.setM_WflInstanceID(s_WflInsID);
			m_WflStepEventInterface.setM_WflStpInsID(s_WflStepInsID);
			m_WflStepEventInterface.setM_userId(m_CurrUserId);
			
			/*【执行步骤载入时事件】*/
			m_WflStepEventInterface.ExecuteEventActions("2", "203", m_WflStepID);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("步骤实例初始化失败，工作流实例ID："+ s_WflInsID +"，流程步骤编号："+ s_WflStepInsID +"，异常信息：", e);
			throw new TzException(e);
		}
		return true;
	}
	
	
	

	
	/**
	 * 将指定用户列表插入到步骤任务责任人表中
	 * @param userIdList		用户列表
	 * @param wflBusProcessID	业务流程ID
	 * @param wflStpInsID		步骤实例编号
	 * @param s_AsgMed			任务分配方式: 1-抄送、2-会签人、3-新建、4-流转、5-转发
	 * @throws TzException
	 */
	private List<String> InserUserListStpInstance(List<String> userIdList, String wflBusProcessID, String wflStpInsID, String s_AsgMed) throws TzException {
		List<String> asg_userList = new ArrayList<String>();
		String errMsg = "";
		try{
			int userCount = userIdList.size();
			switch (s_AsgMed) {
			case "1":
				//抄送情况
				if(userCount <= 0){
					errMsg = "没有指定抄送人";
				}
				break;
			case "2":
				if(userCount <= 0){
					errMsg = "没有指定会签人";
				}			
				break;
			case "3":
				//新建工作流的情况
				if(userCount <= 0){
					errMsg = "没有指定工作流新建人";
				}
				break;
			case "4":
				//工作流正常流转的情况
				if(userCount <= 0){
					errMsg = "没有指定责任人";
				}
				break;
			case "5":
				//工作流转发
				if(userCount <= 0){
					errMsg = "没有指定转发人";
				}
				break;
			default:
				errMsg = "不能识别工作流流转模式";
				break;
			}
			if(!"".equals(errMsg)){
				throw new TzException(errMsg);
			}
			
			
			while (userIdList.size() > 0) {
				//获取队列中第一个人
				String l_userId = userIdList.get(0);
				
				if("3".equals(s_AsgMed)){	//新建
					TzRecord stp_acclstRecord = tzGDObject.createRecord("tzms_acclst_tbl");
					stp_acclstRecord.setColumnValue("tzms_stpinsid", wflStpInsID, true);
					stp_acclstRecord.setColumnValue("tzms_stpproid", l_userId, true);
					
					if(stp_acclstRecord.selectByKey() == false){
						stp_acclstRecord.setColumnValue("tzms_stpinsid", wflStpInsID);
						stp_acclstRecord.setColumnValue("tzms_stpproid", l_userId);
						stp_acclstRecord.setColumnValue("tzms_asgmethod", s_AsgMed);
						stp_acclstRecord.setColumnValue("tzms_proidtype", "DTY");	//责任人类型-责任人
						//stp_acclstRecord.setColumnValue("tzms_actstatus", "SGN");	//步骤责任人状态-已签收
						
						if(stp_acclstRecord.insert() == true){
							asg_userList.add(l_userId);
						}
					}
				}else{
					/*正常流转的情况，抄送、转发、添加会签人都需要流转给对应的委托人*/
					TzWflUser o_WflUser = new TzWflUser(l_userId);
					List<TzClientageInfo> l_ClintageList = o_WflUser.GetUserConsignedInfo(wflBusProcessID);
					if(l_ClintageList.size() > 0){
						//被委托给其他人
						for(TzClientageInfo l_ClintageInfo: l_ClintageList){
							TzRecord stp_acclstRecord = tzGDObject.createRecord("tzms_acclst_tbl");
							stp_acclstRecord.setColumnValue("tzms_stpinsid", wflStpInsID, true);
							stp_acclstRecord.setColumnValue("tzms_stpproid", l_ClintageInfo.getM_Assignee(), true);
							
							if(stp_acclstRecord.selectByKey() == false){
								stp_acclstRecord.setColumnValue("tzms_stpinsid", wflStpInsID);
								stp_acclstRecord.setColumnValue("tzms_stpproid", l_ClintageInfo.getM_Assignee());
								stp_acclstRecord.setColumnValue("tzms_asgmethod", s_AsgMed);
								//stp_acclstRecord.setColumnValue("tzms_actstatus", "ASG");	//步骤责任人状态-已分配
								stp_acclstRecord.setColumnValue("tzms_proidtype", "ASG");	//责任人类型-受托人
								
								if(stp_acclstRecord.insert() == true){
									asg_userList.add(l_ClintageInfo.getM_Assignee());
								}
							}
						}
					}else{
						//没有被委托出去
						TzRecord stp_acclstRecord = tzGDObject.createRecord("tzms_acclst_tbl");
						stp_acclstRecord.setColumnValue("tzms_stpinsid", wflStpInsID, true);
						stp_acclstRecord.setColumnValue("tzms_stpproid", l_userId, true);
						
						if(stp_acclstRecord.selectByKey() == false){
							stp_acclstRecord.setColumnValue("tzms_stpinsid", wflStpInsID);
							stp_acclstRecord.setColumnValue("tzms_stpproid", l_userId);
							stp_acclstRecord.setColumnValue("tzms_asgmethod", s_AsgMed);
							stp_acclstRecord.setColumnValue("tzms_proidtype", "DTY");	//责任人类型-责任人
							//stp_acclstRecord.setColumnValue("tzms_actstatus", "ASG");	//步骤责任人状态-已签收
							
							if(stp_acclstRecord.insert() == true){
								asg_userList.add(l_userId);
							}
						}
					}
				}
				
				userIdList.remove(0);
			}
		}catch (Exception e) {
			e.printStackTrace();
			if("".equals(errMsg)){
				errMsg = "系统错误：将指定用户列表插入到步骤任务责任人表中失败，" + e.getMessage(); 
			}
			throw new TzException(errMsg);
		}
		
		return asg_userList;
	}
	
	
	
	/**
	 * 解析知会人角色并添加知会人列表
	 */
	private void InsertCopyRoleToStpInstance() {
		//从配置的知会人角色取值
		String l_CpyRoleID = sqlQuery.queryForObject("select tzms_cpyrole_uniqueid from tzms_wflstp_t where tzms_wflstp_tid=?", 
				new Object[]{ m_WflStepID }, "String");
		
		if(StringUtils.isNotEmpty(l_CpyRoleID)){				
			AnalysisDynaRole analysisRole = new AnalysisDynaRole();
			List<String> l_CpyList = analysisRole.getUserIds(m_WflInstanceID, m_WflStpInsID, m_CurrUserId, l_CpyRoleID);
			
			if(l_CpyList != null && l_CpyList.size() > 0) {
				for(String userId: l_CpyList) {
					try {
						//创建知会人列表record
						TzRecord tmp_notPsnRecord = tzGDObject.createRecord("tzms_not_psn_tbl");
						//步骤实例ID
						tmp_notPsnRecord.setColumnValue("tzms_stpinsid", m_WflStpInsID);
						//知会责任人ID
						tmp_notPsnRecord.setColumnValue("tzms_stpproid", userId);
						//添加时间
						tmp_notPsnRecord.setColumnValue("tzms_asign_dtime", new Date());
						
						if(tmp_notPsnRecord.insert() == false){
							throw new TzException("添加知会人失败");
						}
					} catch (TzException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	
	
	
	
	/**
	 * 工作流步骤实例竞争签收
	 * @param o_WflCalendae
	 * @param isAdminOperate 是否管理员操作
	 * @param signErrMsg
	 * @return
	 */
	public boolean WorkflowStepSign(TzWflCalendar o_WflCalendae, boolean isAdminOperate, ErrorMessage ErrorMsg_OUT){
		boolean Sign_OK = false;
		//当前登录人ID
		String curr_userId = m_CurrUserId;
		
		//获取当前步骤实例责任人
		String l_UserId = m_WflStepInsRecord.getTzString("tzms_stpproid").getValue();
		
		if(StringUtils.isEmpty(l_UserId)){
			Semaphore tmpSemaphore = null;
			boolean hasSemaphore = false;
			
			try{
				Map.Entry<String,Semaphore> tmpSemaphoreObject = tzGDObject.getSemaphore(m_WflStpInsID);
				
				if(tmpSemaphoreObject == null || tmpSemaphoreObject.getKey() == null || tmpSemaphoreObject.getValue() == null)
				{
					//如果返回的信号灯为空，报系统忙，请稍后再试
					throw new TzException("系统繁忙，请您稍候重试。");
				}else{
					tmpSemaphore = tmpSemaphoreObject.getValue();
					
					//通过获取的信号灯将每个预约时间段间并行执行
					tmpSemaphore.acquire();
					
					//获取许可，完成后需要释放
					hasSemaphore = true;
				}
				
				
				String can_SignFlg = sqlQuery.queryForObject("select 'Y' from tzms_acclst_tbl where tzms_stpinsid=? and tzms_stpproid=?", 
						new Object[]{ m_WflStpInsID, curr_userId }, "String");
				
				String stpInsStutus = sqlQuery.queryForObject("select tzms_stpinsstat from tzms_stpins_tbl where tzms_wflinsid=? and tzms_stpinsid=?", 
						new Object[]{ m_WflInstanceID, m_WflStpInsID }, "String");
				
				boolean stepEnd = false;
				if("4".equals(stpInsStutus) 				//FIN-结束
						|| "5".equals(stpInsStutus) 		//KIL-提前终止
						|| "2".equals(stpInsStutus) 		//BAK-撤回
						|| "7".equals(stpInsStutus) 		//RTN-退回
						|| "9".equals(stpInsStutus)){		//TRF-转发
					
					stepEnd = true;
					String l_msg = "4".equals(stpInsStutus) ? "结束" 
							: ("5".equals(stpInsStutus) ? "提前终止" 
									: ("2".equals(stpInsStutus) ? "被撤回" 
											: ("7".equals(stpInsStutus) ? "被退回" 
													: ("9".equals(stpInsStutus) ? "被转发" : ""))));
					Sign_OK = false;
					ErrorMsg_OUT.setErrorCode("1");
					ErrorMsg_OUT.setErrorMsg("当前业务流程任务已" + l_msg);
				}
				
				if("Y".equals(can_SignFlg)){
					if(!stepEnd){
						try {
							m_WflStepInsRecord.setColumnValue("tzms_stpproid", curr_userId);
							m_WflStepInsRecord.setColumnValue("tzms_stpsigndt", new Date());
							m_WflStepInsRecord.setColumnValue("tzms_stpinsstat", "8");
							
							Date timeoutDt = m_WflStepInsRecord.getTzDate("tzms_tmoutdt").getValue();
							if(timeoutDt != null && new Date().before(timeoutDt)){
								m_WflStepInsRecord.setColumnValue("tzms_tmouflg", "N");
							}
							
							if(m_WflStepInsRecord.update() == true){
								Sign_OK = true;
								
								//执行步骤签收时事件
								m_WflStepEventInterface.ExecuteEventActions("2", "207", m_WflStepID);
							}else{
								Sign_OK = false;
								ErrorMsg_OUT.setErrorCode("1");
								ErrorMsg_OUT.setErrorMsg("签收失败");
							}
						} catch (TzException e) {
							e.printStackTrace();
							Sign_OK = false;
							ErrorMsg_OUT.setErrorCode("1");
							ErrorMsg_OUT.setErrorMsg("业务流程签收失败：" + e.getMessage());
							logger.error("工作流签收失败，异常信息：", e);
						}
					}
				}else{
					Sign_OK = false;
					ErrorMsg_OUT.setErrorCode("1");
					ErrorMsg_OUT.setErrorMsg("您无权签收当前工作流实例任务");
				}
			}catch(Exception e){
				e.printStackTrace();
				Sign_OK = false;
				ErrorMsg_OUT.setErrorCode("1");
				ErrorMsg_OUT.setErrorMsg("工作流系统流转错误：" + e.getMessage());
			}finally{
				//释放信号量
				if(hasSemaphore){
					tmpSemaphore.release();
				}
			}
		}else{
			//步骤实例已有责任人
			if(l_UserId.equals(curr_userId)){
				String can_SignFlg = sqlQuery.queryForObject("select 'Y' from tzms_acclst_tbl where tzms_stpinsid=? and tzms_stpproid=?", 
						new Object[]{ m_WflStpInsID, curr_userId }, "String");
				if("Y".equals(can_SignFlg)) {
					String stpInsStutus = sqlQuery.queryForObject("select tzms_stpinsstat from tzms_stpins_tbl where tzms_wflinsid=? and tzms_stpinsid=?", 
							new Object[]{ m_WflInstanceID, m_WflStpInsID }, "String");
					
					if("6".equals(stpInsStutus)) {
						try {
							m_WflStepInsRecord.setColumnValue("tzms_stpsigndt", new Date());
							m_WflStepInsRecord.setColumnValue("tzms_stpinsstat", "8");
							
							Date timeoutDt = m_WflStepInsRecord.getTzDate("tzms_tmoutdt").getValue();
							if(timeoutDt != null && new Date().before(timeoutDt)){
								m_WflStepInsRecord.setColumnValue("tzms_tmouflg", "N");
							}
							
							if(m_WflStepInsRecord.update() == true){
								Sign_OK = true;
								
								//执行步骤签收时事件
								m_WflStepEventInterface.ExecuteEventActions("2", "207", m_WflStepID);
							}
						}catch(TzException e) {
							e.printStackTrace();
							Sign_OK = false;
							ErrorMsg_OUT.setErrorCode("1");
							ErrorMsg_OUT.setErrorMsg("签收失败：" + e.toString());
							logger.error("工作流签收失败，异常信息：", e);
						}
					}else {
						Sign_OK = true;
					}
				}
			}else{
				Sign_OK = false;
				ErrorMsg_OUT.setErrorCode("1");
				ErrorMsg_OUT.setErrorMsg("当前业务流程任务已被其他人签收");
			}
		}
		
		return Sign_OK;
	}
	
	
	
	/**
	 * 根据用户编号签收工作流
	 * @param s_UserId
	 * @param ErrorMsg_OUT
	 * @return
	 */
	public boolean WorkflowStepSignByUser(String s_UserId, ErrorMessage ErrorMsg_OUT){
		boolean Sign_OK = false;
		//签收人
		String curr_userId = s_UserId;
		
		//获取当前步骤实例责任人
		String l_UserId = m_WflStepInsRecord.getTzString("tzms_stpproid").getValue();
		
		if(StringUtils.isEmpty(l_UserId)){
			Semaphore tmpSemaphore = null;
			boolean hasSemaphore = false;
			
			try{
				Map.Entry<String,Semaphore> tmpSemaphoreObject = tzGDObject.getSemaphore(m_WflStpInsID);
				
				if(tmpSemaphoreObject == null || tmpSemaphoreObject.getKey() == null || tmpSemaphoreObject.getValue() == null)
				{
					//如果返回的信号灯为空，报系统忙，请稍后再试
					throw new TzException("系统繁忙，请您稍候重试。");
				}else{
					tmpSemaphore = tmpSemaphoreObject.getValue();
					
					//通过获取的信号灯将每个预约时间段间并行执行
					tmpSemaphore.acquire();
					
					//获取许可，完成后需要释放
					hasSemaphore = true;
				}
				
				
				String can_SignFlg = sqlQuery.queryForObject("select 'Y' from tzms_acclst_tbl where tzms_stpinsid=? and tzms_stpproid=?", 
						new Object[]{ m_WflStpInsID, curr_userId }, "String");
				
				String stpInsStutus = sqlQuery.queryForObject("select tzms_stpinsstat from tzms_stpins_tbl where tzms_wflinsid=? and tzms_stpinsid=?", 
						new Object[]{ m_WflInstanceID, m_WflStpInsID }, "String");
				
				boolean stepEnd = false;
				if("4".equals(stpInsStutus) 				//FIN-结束
						|| "5".equals(stpInsStutus) 		//KIL-提前终止
						|| "2".equals(stpInsStutus) 		//BAK-撤回
						|| "7".equals(stpInsStutus) 		//RTN-退回
						|| "9".equals(stpInsStutus)){		//TRF-转发
					
					stepEnd = true;
					String l_msg = "4".equals(stpInsStutus) ? "结束" 
							: ("5".equals(stpInsStutus) ? "提前终止" 
									: ("6".equals(stpInsStutus) ? "被撤回" 
											: ("7".equals(stpInsStutus) ? "被退回" 
													: ("9".equals(stpInsStutus) ? "被转发" : ""))));
					Sign_OK = false;
					ErrorMsg_OUT.setErrorCode("1");
					ErrorMsg_OUT.setErrorMsg("当前业务流程任务已" + l_msg);
				}
				
				if("Y".equals(can_SignFlg)){
					if(!stepEnd){
						try {
							m_WflStepInsRecord.setColumnValue("tzms_stpproid", curr_userId);
							m_WflStepInsRecord.setColumnValue("tzms_stpsigndt", new Date());
							m_WflStepInsRecord.setColumnValue("tzms_stpinsstat", "8");
							
							Date timeoutDt = m_WflStepInsRecord.getTzDate("tzms_tmoutdt").getValue();
							if(timeoutDt != null && new Date().before(timeoutDt)){
								m_WflStepInsRecord.setColumnValue("tzms_tmouflg", "N");
							}
							
							if(m_WflStepInsRecord.update() == true){
								Sign_OK = true;
								
								//执行步骤签收时事件
								m_WflStepEventInterface.ExecuteEventActions("2", "207", m_WflStepID);
							}else{
								Sign_OK = false;
								ErrorMsg_OUT.setErrorCode("1");
								ErrorMsg_OUT.setErrorMsg("签收失败");
							}
						} catch (TzException e) {
							e.printStackTrace();
							Sign_OK = false;
							ErrorMsg_OUT.setErrorCode("1");
							ErrorMsg_OUT.setErrorMsg("业务流程签收失败：" + e.getMessage());
							logger.error("工作流签收失败，异常信息：", e);
						}
					}
				}else{
					Sign_OK = false;
					ErrorMsg_OUT.setErrorCode("1");
					ErrorMsg_OUT.setErrorMsg("您无权签收当前工作流实例任务");
				}
			}catch(Exception e){
				e.printStackTrace();
				Sign_OK = false;
				ErrorMsg_OUT.setErrorCode("1");
				ErrorMsg_OUT.setErrorMsg("业务流程签收失败：" + e.getMessage());
				logger.error("工作流签收失败，异常信息：", e);
			}finally{
				//释放信号量
				if(hasSemaphore){
					tmpSemaphore.release();
				}
			}
		}else{
			//步骤实例已有责任人
			if(l_UserId.equals(curr_userId)){
				String can_SignFlg = sqlQuery.queryForObject("select 'Y' from tzms_acclst_tbl where tzms_stpinsid=? and tzms_stpproid=?", 
						new Object[]{ m_WflStpInsID, curr_userId }, "String");
				if("Y".equals(can_SignFlg)) {
					String stpInsStutus = sqlQuery.queryForObject("select tzms_stpinsstat from tzms_stpins_tbl where tzms_wflinsid=? and tzms_stpinsid=?", 
							new Object[]{ m_WflInstanceID, m_WflStpInsID }, "String");
					
					if("6".equals(stpInsStutus)) {
						try {
							m_WflStepInsRecord.setColumnValue("tzms_stpsigndt", new Date());
							m_WflStepInsRecord.setColumnValue("tzms_stpinsstat", "8");
							
							Date timeoutDt = m_WflStepInsRecord.getTzDate("tzms_tmoutdt").getValue();
							if(timeoutDt != null && new Date().before(timeoutDt)){
								m_WflStepInsRecord.setColumnValue("tzms_tmouflg", "N");
							}
							
							if(m_WflStepInsRecord.update() == true){
								Sign_OK = true;
								
								//执行步骤签收时事件
								m_WflStepEventInterface.ExecuteEventActions("2", "207", m_WflStepID);
							}
						}catch(TzException e) {
							e.printStackTrace();
							Sign_OK = false;
							ErrorMsg_OUT.setErrorCode("1");
							ErrorMsg_OUT.setErrorMsg("签收失败：" + e.toString());
							logger.error("工作流签收失败，异常信息：", e);
						}
					}else {
						Sign_OK = true;
					}
				}
			}else{
				Sign_OK = false;
				ErrorMsg_OUT.setErrorCode("1");
				ErrorMsg_OUT.setErrorMsg("当前工作流实例任务已被他人签收");
			}
		}
		
		return Sign_OK;
	}
	
	
	
	
	/**
	 * 解析当前步骤流转的动作及下一步骤责任人列表
	 * @param s_BusProcessDefId		业务类别编号
	 */
	public List<TzStpActInfo> analysisNextActionAndUserList(String s_BusProcessDefId){
		
		List<TzStpActInfo> stpActList = new ArrayList<TzStpActInfo>();
		
		List<Map<String,Object>> stp_ActList = sqlQuery.queryForList("select tzms_wf_actcls_tid,tzms_actclsname,tzms_actcon_flg,tzms_actcon_type,tzms_endact_flg,tzms_is_condition,tzms_stp_end_uniqueid,tzms_cond_end_uniqueid from tzms_wf_actcls_t where tzms_wfl_uniqueid=? and tzms_stp_sta_uniqueid=?", 
				new Object[]{ s_BusProcessDefId, m_WflStepID });
		
		for(Map<String,Object> stp_ActMap: stp_ActList){
			//动作类编号
			String l_actClsID = stp_ActMap.get("tzms_wf_actcls_tid").toString();
			String l_actClaName = stp_ActMap.get("tzms_actclsname") == null ? "" : stp_ActMap.get("tzms_actclsname").toString();
			//是否进入条件判断
			boolean l_is_condition = (boolean) stp_ActMap.get("tzms_is_condition");
			//是否结束动作
			boolean l_is_endAction = (boolean) stp_ActMap.get("tzms_endact_flg");
			
			//动作路径
			List<String> l_actPath = new ArrayList<String>();
			l_actPath.add(l_actClsID);

			if(l_is_condition && !l_is_endAction){
				//路由结束条件信息
				String l_rouEndConId = stp_ActMap.get("tzms_cond_end_uniqueid").toString();
				
				//解析判断条件路由
				this.analysisConditionRou(s_BusProcessDefId, l_rouEndConId, stpActList, l_actPath);
			}else{
				TzStpActInfo tzStpActInfo = new TzStpActInfo();
				tzStpActInfo.setM_ActClsId(l_actClsID);
				tzStpActInfo.setM_ActClsName(l_actClaName);
				tzStpActInfo.setM_IsEndAction(l_is_endAction);
				tzStpActInfo.setM_IsConAction(false);
				
				if(!l_is_endAction){	//结束动作
					//路由结束步骤编号
					String l_rouEndStpId = stp_ActMap.get("tzms_stp_end_uniqueid").toString();
					
					tzStpActInfo.setM_NextStepId(l_rouEndStpId);
					//下一步骤责任人列表
					List<String> userList = this.getNextStepRoleUserList(l_rouEndStpId);
					tzStpActInfo.setM_NextUserList(userList);
				}
				
				tzStpActInfo.setM_RouAllPathList(l_actPath);
				
				stpActList.add(tzStpActInfo);
			}
		}
		
		return stpActList;
	}

	
	
	/**
	 * 提交当前步骤实例并创建工作流下一步步骤实例
	 * 加签说明：
	 * 1、判断当前步骤实例是否为加签步骤（平行核签&顺序核签）
	 *	平行核签：是否有其他核签步骤实例没有提交，如果有，直接提交并结束当前步骤，否则提交并流转下一步骤
	 *	顺序核签：是否有后续核签人，如果有，提交并结束当前步骤实例并重新生成当前步骤实例给下一核签人，都在提交并流转下一步骤
	 * 2、如果当前步骤是并发步骤，是否有其他并发步骤实例没有提交，如果有，直接提交并结束当前步骤，否则提交并流转下一步骤
	 * 
	 * 思路：
	 * STP1、检查当前步骤实例状态，如果已完成不许提交；
	 * STP2、检查当前步骤操作权限，当前步骤的前续步骤中是否有存活的步骤实例，如果有不允许操作；
	 * STP3、判断当前步骤是否启用加签，判断加签类型，如果是平行加签，步骤责任人提交后给所有加签人生成步骤实例；如果是顺序加签，步骤责任人提交后按顺序给第一个加签人生成步骤实例，第一个加签人提交后给第二个加签人生成步骤实例，按顺序依次创建；
	 * STP4、如果当前步骤还有非当前步骤实例的其他存回步骤实例，流程不往下流转并结束当前步骤实例；
	 * STP5、所有加签人步骤创建完成后，最后一个存活步骤实例提交时，如果是退回步骤实例且满足直送条件，直接生成退回步骤实例，不满足直送时，杀死全部后续步骤存活实例后继续向下流转；
	 * STP6、检查流程是否继续向下流转，如果下一步骤的前续步骤中存在非当前存活步骤实例，则当前步骤不允许继续流转；
	 * 
	 * @param o_WflCalendar			日历本
	 * @param s_BusProcessDefId		业务流程定义编号
	 * @param type			动作摘要  AGREE-同意、SUBMIT-提交
	 * @throws TzException
	 */
	public void WorkflowSubmitOrAgree(TzWflCalendar o_WflCalendar, String s_BusProcessDefId, String type) throws TzException {
		//是否继续流转
		boolean isContinueFlow = true;
		//是否生成会签
		boolean isCreateHqIns = false;
		//查询加签类型，加签后续操作
		String tzms_asign_type = "";
		String tzms_asign_operte = "";
		//当前步骤责任人
		String zrrStpInsID = "";
		
		
		/*【STP1】---检查当前步骤实例状态*/
		this.checkWflStpInstanceStatus();
		
		
		/*【STP2】---检查当前步骤操作权限*/
		if(this.checkOperatingAuthorization(m_WflInstanceID, m_WflStepID) == false){
			throw new TzException("业务流程前续步骤尚未处理完成，请耐心等待");
		}
		
		
		//当前步骤是否启用加签功能
		boolean bool_addSign = m_WflStepCfgRecord.getTzBoolean("tzms_dpyhqrflg").getValue();
		//当前步骤实例责任人
		String stpInsUserID = m_WflStepInsRecord.getTzString("tzms_stpproid").getValue();
		//获取当前步骤实例优先级
		String l_PriLevelID = m_WflStepInsRecord.getTzString("tzms_pridtlid").getValue();
		
		
		/*【STP3】---判断是否启用加签功能，如果启用，需要创建加签人步骤实例，并不向下流转*/
		if(bool_addSign){
			//任务分配方式，2-会签人
			String l_asgMethod = m_WflStepInsRecord.getTzString("tzms_asgmethod").getValue();
			
			//当前步骤对应责任人步骤实例ID
			if("2".equals(l_asgMethod)){	//当前步骤实例为会签人步骤实例
				zrrStpInsID = this.getWflZrrByAsignStpInsID(m_WflStpInsID);
			}else{
				zrrStpInsID = m_WflStpInsID;
			}
			
			
			/**如果当前步骤实例是步骤责任人实例，提交时需要检查加签人并生成当前步骤的加签人步骤实例**/
			//查询加签类型，加签后续操作
			Map<String,Object> asignMap = sqlQuery.queryForMap("select tzms_asign_type,tzms_asign_operte from tzms_stpins_tbl where tzms_wflinsid=? and tzms_stpinsid=?", 
					new Object[]{ m_WflInstanceID, zrrStpInsID });
			if(asignMap != null){
				tzms_asign_type = asignMap.get("tzms_asign_type") == null ? "" : asignMap.get("tzms_asign_type").toString();
				tzms_asign_operte = asignMap.get("tzms_asign_operte") == null ? "" : asignMap.get("tzms_asign_operte").toString();
			}
			
			
			if(StringUtils.isNotEmpty(tzms_asign_type) 
					&& StringUtils.isNotEmpty(tzms_asign_operte)){
				
				if("2".equals(l_asgMethod)){
					//加签人提交，如果是顺序加签，生成下一加签人步骤实例
					if("2".equals(tzms_asign_type)){
						String nextSign_UserId = sqlQuery.queryForObject("select top 1 tzms_stpproid from tzms_asign_psn_tbl where tzms_stpinsid=? and tzms_step_xh>(select tzms_step_xh from tzms_asign_psn_tbl where tzms_stpinsid=? and tzms_stpproid=?) order by tzms_step_xh", 
								new Object[]{ zrrStpInsID, zrrStpInsID, stpInsUserID }, "String");
						
						if(StringUtils.isNotEmpty(nextSign_UserId)){
							//创建加签人步骤实例
							//this.CreateHQRWorkflowStep(o_WflCalendar, s_BusProcessDefId, nextSign_UserId, Pre_stpInsID, zrrStpInsID, l_PriLevelID);
							this.CreateHQRWorkflowStep(o_WflCalendar, s_BusProcessDefId, nextSign_UserId, m_WflStpInsID, zrrStpInsID, l_PriLevelID);
							
							isContinueFlow = false;
							isCreateHqIns = true;
						}
					}
				}else{
					//步骤责任人提交，如果有加签人，生成加签人步骤实例
					if("1".equals(tzms_asign_type)){	//平行加签
						//获取加签人列表
						List<Map<String,Object>> aSignList = sqlQuery.queryForList("select tzms_stpproid,tzms_asign_stpinsid from tzms_asign_psn_tbl where tzms_stpinsid=?",
								new Object[]{ zrrStpInsID });
						if(aSignList != null && aSignList.size() > 0){
							for(Map<String,Object> aSignMap: aSignList){
								String aSign_UserId = aSignMap.get("tzms_stpproid").toString();

								//创建加签人步骤实例
								//this.CreateHQRWorkflowStep(o_WflCalendar, s_BusProcessDefId, aSign_UserId, Pre_stpInsID, zrrStpInsID, l_PriLevelID);
								this.CreateHQRWorkflowStep(o_WflCalendar, s_BusProcessDefId, aSign_UserId, m_WflStpInsID, zrrStpInsID, l_PriLevelID);
								
								isContinueFlow = false;
								isCreateHqIns = true;
							}
						}
					}else if("2".equals(tzms_asign_type)){	//顺序加签
						//生成加签人列表中序号最小加签人对应当前步骤的步骤实例
						String aSign_UserId = sqlQuery.queryForObject("select top 1 tzms_stpproid from tzms_asign_psn_tbl where tzms_stpinsid=? order by tzms_step_xh", 
								new Object[]{ zrrStpInsID }, "String");
						
						if(StringUtils.isNotEmpty(aSign_UserId)){
							//创建加签人步骤实例
							//this.CreateHQRWorkflowStep(o_WflCalendar, s_BusProcessDefId, aSign_UserId, Pre_stpInsID, zrrStpInsID, l_PriLevelID);
							this.CreateHQRWorkflowStep(o_WflCalendar, s_BusProcessDefId, aSign_UserId, m_WflStpInsID, zrrStpInsID, l_PriLevelID);
							
							isContinueFlow = false;
							isCreateHqIns = true;
						}
					}
				}
			}
		}else{
			zrrStpInsID = m_WflStpInsID;
		}
		
		
		/*【STP4】---当前步骤是否还有其他并发存活实例*/
		if(isContinueFlow == true){
			//当前步骤下其他分配方式不是“抄送”，任务状态存活的步骤实例
			String exists_bf_ins = sqlQuery.queryForObject("select top 1 'Y' from tzms_stpins_tbl where tzms_wflinsid=? and tzms_wflstpid=? and tzms_stpinsid<>? and tzms_asgmethod<>'1' and tzms_stpinsstat in('1','3','6','8')",
					new Object[]{ m_WflInstanceID, m_WflStepID, m_WflStpInsID }, "String");
			if("Y".equals(exists_bf_ins)){
				isContinueFlow = false;
			}
		}
		
		//所有下一步骤实例
		List<String> allNextStpInsList = new ArrayList<String>();
		//事件异常信息
		List<TzException> exceptionList = new ArrayList<TzException>();
		
		/*【STP4】---如果当前步骤实例添加了加签人，并创建加签实例，结束当前步骤实例并不向下流转*/
		if(isContinueFlow){
			//流转下一步，
			/*
			 * 如果启用了直送功能，判断直送条件
			 */
			//启用直送功能
			boolean bool_directFlg = m_WflStepCfgRecord.getTzBoolean("tzms_wfl_directflg").getValue();
			//直送条件
			String directConfition = m_WflStepCfgRecord.getTzString("tzms_direc_var_uniqueid").getValue();
			//退回步骤实例ID
			String backStpInsID = sqlQuery.queryForObject("select tzms_rtn_stpinsid from tzms_stpins_tbl where tzms_wflinsid=? and tzms_stpinsid=?", 
					new Object[]{ m_WflInstanceID, zrrStpInsID }, "String");
			
			boolean bool_dirSend = false;
			if(bool_directFlg == true && StringUtils.isNotEmpty(backStpInsID)){
				//流程直送功能解析
				/*此处需要调用流程直送功能解析引擎*/
				AnalysisFlowFunction analysisWflFlow = new AnalysisFlowFunction();
				bool_dirSend = analysisWflFlow.analysisFlowZS(m_WflInstanceID);	
				
				if(bool_dirSend == true && StringUtils.isNotEmpty(directConfition)){	
					/*如果直送条件不为空，解析直送条件*/
					
					/*系统变量解析开始----START*/
					AnalysisDynaSysVar analysisSysVar = new AnalysisDynaSysVar();
					analysisSysVar.setM_SysVarID(directConfition);
					
					//业务数据ID
					String wflDateRecId = sqlQuery.queryForObject("select tzms_wflrecord_uniqueid from tzms_wflins_tbl where tzms_wflinsid=?", 
							new Object[]{ m_WflInstanceID }, "String");
					if(wflDateRecId == null) wflDateRecId = "";
					
					//设置参数
					String[] sysVarParam = { m_WflInstanceID, m_WflStpInsID, wflDateRecId };
					analysisSysVar.setM_SysVarParam(sysVarParam);
					
					try {
						Object result = analysisSysVar.GetVarValue();
						
						if(result instanceof String) {
							bool_dirSend = Boolean.valueOf((String) result);
						}else {
							try {
								bool_dirSend = (boolean) result;
							}catch(ClassCastException e) {
								logger.error("解析流程直送条件错误， 返回值不是布尔型", e);
							}
						}
						
					}catch (Exception e) {
						//e.printStackTrace();
						logger.error("解析流程直送条件错误", e);
					}
					/*系统变量解析开始----END*/
				}
			}
			

			if(bool_dirSend){
				/*直送至退回步骤，如果该步骤有多个退回实例，需要重新生成多个退回实例*/
				this.WorkflowDirectSend(o_WflCalendar, s_BusProcessDefId, backStpInsID);
			}else{
				/*如果不是直送，先杀死当前步骤的后续步骤所有存活实例*/
				if(bool_directFlg == true 
						&& StringUtils.isNotEmpty(backStpInsID)){
					this.killAllAliveStpInsAfterStpID(m_WflInstanceID, m_WflStepID);
				}
				
				//查询加签后续操作
				String l_asignOperte = "";
				String l_UserId = "";
				Map<String,Object> zrrStpInsMap = sqlQuery.queryForMap("select tzms_stpproid,tzms_asign_operte from tzms_stpins_tbl where tzms_wflinsid=? and tzms_stpinsid=?", 
						new Object[]{ m_WflInstanceID, zrrStpInsID });
				if(zrrStpInsMap != null){
					l_asignOperte = zrrStpInsMap.get("tzms_asign_operte") == null ? "" : zrrStpInsMap.get("tzms_asign_operte").toString();
					l_UserId = zrrStpInsMap.get("tzms_stpproid") == null ? "" : zrrStpInsMap.get("tzms_stpproid").toString();
				}
				
				
				if("1".equals(l_asignOperte)){	/*退回到本关卡，重新为当前步骤生产步骤实例*/
					TzWflStep wflStepInstance = new TzWflStep(m_CurrUserId);
					
					List<String> userList = new ArrayList<String>();
					userList.add(l_UserId);
					
					boolean creakOk = wflStepInstance.CreateWorkflowStep0(o_WflCalendar, s_BusProcessDefId, m_WflInstanceID, m_WflStepID, "", l_PriLevelID, "6", "4", m_WflStpInsID, userList);
					if(creakOk) {
						
						String New_stpinsId = wflStepInstance.getM_WflStpInsID();
						//创建到新步骤实例的动作实例
						CommonFun commonFun = new CommonFun();
						String actionInsID = commonFun.GenerateGUID_1("ACTINS", 30);
						
						//创建动作实例表
						TzRecord tmp_actIndRecord = tzGDObject.createRecord("tzms_actins_tbl");
						//动作实例ID
						tmp_actIndRecord.setColumnValue("tzms_actinsid", actionInsID);
						//流转动作ID
						tmp_actIndRecord.setColumnValue("tzms_actclsid", WFL_ADD_SIGN_ACTCLSID);
						//流程实例ID
						tmp_actIndRecord.setColumnValue("tzms_wflinsid", m_WflInstanceID);
						//步骤实例ID
						tmp_actIndRecord.setColumnValue("tzms_stpinsid", m_WflStpInsID);
						//下一步步骤实例ID
						tmp_actIndRecord.setColumnValue("tzms_nextstpinsid", New_stpinsId);
						//下一步步骤ID
						tmp_actIndRecord.setColumnValue("tzms_nextstpid", m_WflStepID);
						//动作实例状态
						tmp_actIndRecord.setColumnValue("tzms_actinsstate", "FIN");
		
						tmp_actIndRecord.insert();
						
						
						//插入步骤实例流转关系tzms_stpins_pre_tbl
						TzRecord stpPre_TempRecord = tzGDObject.createRecord("tzms_stpins_pre_tbl");
						stpPre_TempRecord.setColumnValue("tzms_stpinsid", New_stpinsId);
						stpPre_TempRecord.setColumnValue("tzms_prestpinsid", m_WflStpInsID);
						stpPre_TempRecord.insert();
						
						//是否不发送通知
						if(isNotSendNotice != true) {
							/***发送通知***/
							List<String> nextUserList = wflStepInstance.getM_stepUserList();
							TzWflNotice wflNotice = new TzWflNotice(m_WflInstanceID, New_stpinsId, m_WflStepID);
							wflNotice.SendWorkflowNotice(nextUserList);
						}
					}
				}else{
					//获取动作路由信息
					List<TzStpActInfo> l_stpActInfoList = this.analysisNextActionAndUserList(s_BusProcessDefId);
					
					//检查下一步步骤责任人是否都存在
					this.checkNextStepsUserExists(l_stpActInfoList);
					
					for(TzStpActInfo actInfo: l_stpActInfoList){
						if(m_WorkflowActionInfo != null){	//路径不唯一
							if(actInfo.isM_IsConAction() != true){
								//流转其他并发步骤路径
								TzWflAction tzWflAction = new TzWflAction(s_BusProcessDefId, m_WflInstanceID, m_WflStpInsID, m_WflStepID, m_CurrUserId);
								tzWflAction.setNotSendNotice(isNotSendNotice);
								
								List<String> nextStpInsList = tzWflAction.TriggerAction(o_WflCalendar, stpInsUserID, actInfo);
								if(nextStpInsList != null) {
									allNextStpInsList.addAll(nextStpInsList);
								}
								//流转动作事件异常信息
								if(tzWflAction.getFlowEventsException().size() > 0) {
									exceptionList.addAll(tzWflAction.getFlowEventsException());
								}
							}
						}else{
							//路径唯一，直接流转下一步骤
							TzWflAction tzWflAction = new TzWflAction(s_BusProcessDefId, m_WflInstanceID, m_WflStpInsID, m_WflStepID, m_CurrUserId);
							tzWflAction.setNotSendNotice(isNotSendNotice);
							
							List<String> nextStpInsList = tzWflAction.TriggerAction(o_WflCalendar, stpInsUserID, actInfo);
							if(nextStpInsList != null) {
								allNextStpInsList.addAll(nextStpInsList);
							}
							//流转动作事件异常信息
							if(tzWflAction.getFlowEventsException().size() > 0) {
								exceptionList.addAll(tzWflAction.getFlowEventsException());
							}
						}
					}
					if(m_WorkflowActionInfo != null){
						//流转选择的路径
						TzWflAction tzWflAction = new TzWflAction(s_BusProcessDefId, m_WflInstanceID, m_WflStpInsID, m_WflStepID, m_CurrUserId);
						tzWflAction.setNotSendNotice(isNotSendNotice);
						
						List<String> nextStpInsList = tzWflAction.TriggerAction(o_WflCalendar, stpInsUserID, m_WorkflowActionInfo);
						if(nextStpInsList != null) {
							allNextStpInsList.addAll(nextStpInsList);
						}
						//流转动作事件异常信息
						if(tzWflAction.getFlowEventsException().size() > 0) {
							exceptionList.addAll(tzWflAction.getFlowEventsException());
						}
					}
					
					//成功流转下一步，给指定角色发送邮件
					//启用提交邮件通知指定角色
					boolean bool_emailnotice_flg = m_WflStepCfgRecord.getTzBoolean("tzms_stp_emailnotice_flg").getValue();
					if(allNextStpInsList.size() > 0 && bool_emailnotice_flg == true) {
						//邮件通知角色
						String tzms_emailrole_uniqueid = m_WflStepCfgRecord.getTzString("tzms_emailrole_uniqueid").getValue();
						//邮件通知模板
						String tzms_stp_emailtmp_uniqueid = m_WflStepCfgRecord.getTzString("tzms_stp_emailtmp_uniqueid").getValue();
						
						if(StringUtils.isNotBlank(tzms_emailrole_uniqueid) 
								&& StringUtils.isNotBlank(tzms_stp_emailtmp_uniqueid)) {
							
							//发送邮件通知
							TzWflNotice wflNotice = new TzWflNotice(m_WflInstanceID, m_WflStpInsID, m_WflStepID);
							wflNotice.stepSubmitSendEmailToRole(tzms_emailrole_uniqueid, tzms_stp_emailtmp_uniqueid, m_CurrUserId);
						}
					}
				}
			}
		}else{
			/*不流转下一步*/
			if(isCreateHqIns == false) {	//生成了会签步骤直接结束
				List<TzStpActInfo> l_stpActInfoList = this.analysisNextActionAndUserList(s_BusProcessDefId);
				for(TzStpActInfo actInfo: l_stpActInfoList){
					//触发动作流转前事件
					TzWflAction tzWflAction = new TzWflAction(s_BusProcessDefId, m_WflInstanceID, m_WflStpInsID, m_WflStepID, m_CurrUserId);
					tzWflAction.setNotSendNotice(isNotSendNotice);
					
					tzWflAction.TriggerNotRoutAction(o_WflCalendar, stpInsUserID, actInfo);
					
					//流转动作事件异常信息
					if(tzWflAction.getFlowEventsException().size() > 0) {
						exceptionList.addAll(tzWflAction.getFlowEventsException());
					}
				}
			}
		}
		
		//提交成功后终止当前步骤实例
		this.WflStepTerminate("4");
		
		//设置动作摘要
		String actAbstract = "";
		if(m_WflStpInsID.equals(zrrStpInsID) && isCreateHqIns == true) {
			actAbstract = "加签";
		}
		this.setActAbstract(actAbstract);
		
		//检查流转动作事件是否存在异常，如果存在异常需要抛出异常
		if(exceptionList.size() > 0) {
			String exceptionMsg = "";
			for(TzException tzEx: exceptionList) {
				exceptionMsg += "".equals(exceptionMsg) ? tzEx.getMessage() : ("，" + tzEx.getMessage());
			}
			throw new TzException("业务流程事件执行异常：" + exceptionMsg);
		}
		
		if("SUBMIT".equals(type)) {
			//执行步骤提交时事件
			m_WflStepEventInterface.ExecuteEventActions("2", "210", m_WflStepID);
		}else {
			//执行步骤同意时事件
			m_WflStepEventInterface.ExecuteEventActions("2", "212", m_WflStepID);
		}
		
		//下一步骤实例自动同意
		if(allNextStpInsList.size() > 0) {
			for(String nextStpInsId: allNextStpInsList) {
				//下一步骤自动同意
				TzWflStep tzWflStepObj = new TzWflStep("");
				boolean stpCreate = tzWflStepObj.CreateWorkflowStep2(m_WflInstanceID, nextStpInsId);
				if(stpCreate == true) {
					tzWflStepObj.WorkflowAutoAgree(o_WflCalendar);
				}
			}
		}
	}
	
	

	
	/**
	 * 工作流直送
	 * @param o_WflCalendar			日历本
	 * @param s_BusProcessDefId		业务流程ID
	 * @param backStpInsID			退回步骤实例
	 * @throws TzException
	 */
	private void WorkflowDirectSend(TzWflCalendar o_WflCalendar, String s_BusProcessDefId, String backStpInsID) throws TzException {
		try{
			logger.info("※满足流程直送条件，流程开始直送到退回步骤，退回步骤实例：" + backStpInsID);
			//获取步骤实例责任人
			Map<String,Object> l_StpInsMap = sqlQuery.queryForMap("select tzms_wflstpid,tzms_stpproid,tzms_pridtlid from tzms_stpins_tbl where tzms_wflinsid=? and tzms_stpinsid=?", 
					new Object[]{ m_WflInstanceID, backStpInsID });
			
			if(l_StpInsMap != null){
				//步骤编号
				String l_stpId = l_StpInsMap.get("tzms_wflstpid").toString();
				//步骤责任人
				String l_stpproid = l_StpInsMap.get("tzms_stpproid").toString();
				//优先级
				String l_priLevelId = l_StpInsMap.get("tzms_pridtlid")==null ? "" : l_StpInsMap.get("tzms_pridtlid").toString();
				
				List<String> userList = new ArrayList<String>();
				userList.add(l_stpproid);
				
				//创建步骤实例
				TzWflStep tzWflStep = new TzWflStep(m_CurrUserId);
				boolean createStepOK = tzWflStep.CreateWorkflowStep0(o_WflCalendar, s_BusProcessDefId, m_WflInstanceID, l_stpId, "", l_priLevelId, "6", "4", m_WflStpInsID, userList);
	            
				//获取新生成的步骤实例ID
				String l_StpInsID = tzWflStep.getM_WflStpInsID();
				
				if(createStepOK){
					logger.info("流程直送成功");
					
					//创建到新步骤实例的动作实例
					CommonFun commonFun = new CommonFun();
					String actionInsID = commonFun.GenerateGUID_1("ACTINS", 30);
					
					//工作流实例编号、步骤实例编号、上一步步骤实例编号
					String tzms_wflinsid = m_WflStepInsRecord.getTzString("tzms_wflinsid").getValue();
					String tzms_stpinsid = m_WflStepInsRecord.getTzString("tzms_stpinsid").getValue();
	
					//创建动作实例表
					TzRecord tmp_actIndRecord = tzGDObject.createRecord("tzms_actins_tbl");
					//动作实例ID
					tmp_actIndRecord.setColumnValue("tzms_actinsid", actionInsID);
					//流转动作ID
					tmp_actIndRecord.setColumnValue("tzms_actclsid", WFL_DIRECT_SEND_ACTCLSID);
					//流程实例ID
					tmp_actIndRecord.setColumnValue("tzms_wflinsid", tzms_wflinsid);
					//步骤实例ID
					tmp_actIndRecord.setColumnValue("tzms_stpinsid", tzms_stpinsid);
					//下一步步骤实例ID
					tmp_actIndRecord.setColumnValue("tzms_nextstpinsid", l_StpInsID);
					//下一步步骤ID
					tmp_actIndRecord.setColumnValue("tzms_nextstpid", l_stpId);
					//动作实例状态
					tmp_actIndRecord.setColumnValue("tzms_actinsstate", "FIN");
	
					tmp_actIndRecord.insert();
					
					
					//插入步骤实例流转关系tzms_stpins_pre_tbl
					TzRecord stpPre_TempRecord = tzGDObject.createRecord("tzms_stpins_pre_tbl");
					stpPre_TempRecord.setColumnValue("tzms_stpinsid", l_StpInsID);
					stpPre_TempRecord.setColumnValue("tzms_prestpinsid", tzms_stpinsid);
					stpPre_TempRecord.insert();
					
					//是否不发送通知
					if(isNotSendNotice != true) {
						/***发送通知***/
						List<String> nextUserList = tzWflStep.getM_stepUserList();
						TzWflNotice wflNotice = new TzWflNotice(m_WflInstanceID, l_StpInsID, l_stpId);
						wflNotice.SendWorkflowNotice(nextUserList);
					}
				}
			}
		}catch (Exception e) {
			logger.error("业务流程直送失败", e);
			throw new TzException("业务流程直送失败", e);
		}
	}
	
	
	
	
	/**
	 * 工作流转发
	 * 当前实例步骤转发指定用户
	 * @param o_WflCalendar
	 * @param New_UserID
	 * @return
	 * @throws TzException
	 */
	public boolean WorkflowTransfer(TzWflCalendar o_WflCalendar, String trf_UserId) throws TzException {
		
		logger.info("※工作流转发开始，转发人员ID: " + trf_UserId);
		
		//1、终止当前步骤实例
		this.WflStepTerminate("9");
		//设置动作摘要为-转发
		this.setActAbstract("转发");
		
		//业务流程编号
		String l_BusProcessID = m_WflStepCfgRecord.getTzString("tzms_wfcldn_uniqueid").getValue();
		//上一步骤实例编号
		String l_PreStpInsID = m_WflStepInsRecord.getTzString("tzms_prestpinsid").getValue();
		//优先级
		String l_PriLevelID = m_WflStepInsRecord.getTzString("tzms_pridtlid").getValue();
		
		/**转发时，如果是会签步骤或并发步骤转发如何处理**/
		
		List<String> UserList = new ArrayList<String>();
		UserList.add(trf_UserId);
		
		//2、重新创建本步骤实例
		TzWflStep NewTzWflStep = new TzWflStep(m_CurrUserId);
		boolean createStepOK = NewTzWflStep.CreateWorkflowStep0(o_WflCalendar, l_BusProcessID, m_WflInstanceID, m_WflStepID, "", l_PriLevelID, "6", "5", l_PreStpInsID, UserList);
		
		if(createStepOK){
			logger.info("转发成功");
			
			String New_StpInsId = NewTzWflStep.getM_WflStpInsID();
			
			//插入步骤实例流转关系tzms_stpins_pre_tbl
			TzRecord stpPre_TempRecord = tzGDObject.createRecord("tzms_stpins_pre_tbl");
			stpPre_TempRecord.setColumnValue("tzms_stpinsid", New_StpInsId);
			stpPre_TempRecord.setColumnValue("tzms_prestpinsid", m_WflStpInsID);
			stpPre_TempRecord.insert();
			
			
			//3、创建到新步骤实例的动作实例
			CommonFun commonFun = new CommonFun();
			String actionInsID = commonFun.GenerateGUID_1("ACTINS", 30);
			
			//创建动作实例表
			TzRecord tmp_actIndRecord = tzGDObject.createRecord("tzms_actins_tbl");
			//动作实例ID
			tmp_actIndRecord.setColumnValue("tzms_actinsid", actionInsID);
			//流转动作ID
			tmp_actIndRecord.setColumnValue("tzms_actclsid", WFL_TRANSPOND_ACTCLSID);
			//流程实例ID
			tmp_actIndRecord.setColumnValue("tzms_wflinsid", m_WflInstanceID);
			//步骤实例ID
			tmp_actIndRecord.setColumnValue("tzms_stpinsid", m_WflStpInsID);
			//下一步步骤实例ID
			tmp_actIndRecord.setColumnValue("tzms_nextstpinsid", New_StpInsId);
			//下一步步骤ID
			tmp_actIndRecord.setColumnValue("tzms_nextstpid", m_WflStepID);
			//动作实例状态
			tmp_actIndRecord.setColumnValue("tzms_actinsstate", "FIN");

			tmp_actIndRecord.insert();
			
			
			//是否不发送通知
			if(isNotSendNotice != true) {
				/***发送通知***/
				List<String> nextUserList = NewTzWflStep.getM_stepUserList();
				TzWflNotice wflNotice = new TzWflNotice(m_WflInstanceID, New_StpInsId, m_WflStepID);
				wflNotice.SendWorkflowNotice(nextUserList);
			}
			
			return true;
		}else{
			return false;
		}
	}	
	
	
	
	
	/**
	 * 工作流抄送任务实例“已阅”处理
	 * @return
	 */
	public boolean TaskReadOver(){
		boolean l_Teaded = false;
		
		try {
			Map<String,Object> stepInsMap = sqlQuery.queryForMap("select tzms_asgmethod,tzms_stpinsstat from tzms_stpins_tbl where tzms_wflinsid=? and tzms_stpinsid=?", 
					new Object[]{ m_WflInstanceID, m_WflStpInsID });
			if(stepInsMap != null){
				//任务分配方式
				String AsgMethod = stepInsMap.get("tzms_asgmethod") == null ? "" : stepInsMap.get("tzms_asgmethod").toString();
				//步骤实例状态
				String stepInsSta = stepInsMap.get("tzms_stpinsstat") == null ? "" : stepInsMap.get("tzms_stpinsstat").toString();
				
				//抄送
				if("1".equals(AsgMethod)){
					if("4".equals(stepInsSta)){
						l_Teaded = true;
					}else{
						try{
							TzRecord wflStp_InsRecord = null;
							
							if(m_WflStepInsRecord == null) {
								wflStp_InsRecord = tzGDObject.createRecord("tzms_stpins_tbl");
								wflStp_InsRecord.setColumnValue("tzms_wflinsid", m_WflInstanceID, true);
								wflStp_InsRecord.setColumnValue("tzms_stpinsid", m_WflStpInsID, true);
								
								if(wflStp_InsRecord.selectByKey() == false){
									throw new TzException("获取工作流步骤实例失败");
								}
							}else {
								wflStp_InsRecord = m_WflStepInsRecord;
							}
							
							if(wflStp_InsRecord != null) {
								//步骤责任人
								String tzms_stpproid = wflStp_InsRecord.getTzString("tzms_stpproid").getValue();
								if(StringUtils.isBlank(tzms_stpproid)) {
									//如果没有步骤责任人（管理员后台处理），默认设置一个责任人为步骤责任人
									tzms_stpproid = sqlQuery.queryForObject("select top 1 tzms_stpproid from tzms_acclst_tbl where tzms_stpinsid=? order by tzms_proidtype desc", 
											new Object[] { m_WflStpInsID }, "String");
									
									wflStp_InsRecord.setColumnValue("tzms_stpproid", tzms_stpproid);
								}
								
								//动作摘要为-已阅
								wflStp_InsRecord.setColumnValue("tzms_action_type", "已阅");
								//步骤实例状态
								wflStp_InsRecord.setColumnValue("tzms_stpinsstat", "4");
								//任务处理人
								wflStp_InsRecord.setColumnValue("tzms_tskproid", m_CurrUserId);
								//任务处理时间
								wflStp_InsRecord.setColumnValue("tzms_tskprodt", new Date());
								//结束时间
								wflStp_InsRecord.setColumnValue("tzms_stpenddt", new Date());
								wflStp_InsRecord.update();
								
								l_Teaded = true;
							}
						}catch(Exception e){
							e.printStackTrace();
							logger.error("知会任务已阅异常，异常信息：", e);
						}
					}
				}
			}
		}catch (Exception e) {
			logger.error("知会任务已阅异常，异常信息：", e);
		}

		return l_Teaded;
	}

	
	
	
	/**
	 * 工作流拒绝
	 * 业务流程返回首步骤，用于通知业务流程发起人
	 * @return
	 * @throws TzException
	 */
	public boolean WorkflowReject(TzWflCalendar o_WflCalendar) throws TzException {
		boolean rejectOK = false;
		try {
			//业务流程ID
			String BusProcessDefID = m_WflStepCfgRecord.getTzString("tzms_wfcldn_uniqueid").getValue();
			
			//获取首步骤
			String l_FirstStpID = sqlQuery.queryForObject("select tzms_wflstp_tid from tzms_wflstp_t where tzms_wfcldn_uniqueid=? and tzms_stptype=1", 
					new Object[]{ BusProcessDefID }, "String");
			
			if(StringUtils.isNotEmpty(l_FirstStpID)){
				//如果当前步骤就是首步骤
				if(!l_FirstStpID.equals(m_WflStepID)){
					//获取首步骤责任人
					String l_FirstUserID = sqlQuery.queryForObject("select tzms_stpproid from tzms_stpins_tbl where tzms_wflinsid=? and tzms_wflstpid=? and tzms_asgmethod='3'", 
							new Object[]{ m_WflInstanceID, l_FirstStpID }, "String");
					
					List<String> userList = new ArrayList<String>();
					userList.add(l_FirstUserID);

					//获取当前步骤实例优先级
					String l_PriLevelID = sqlQuery.queryForObject("select tzms_pridtlid from tzms_stpins_tbl where tzms_wflinsid=? and tzms_stpinsid=?", 
							new Object[]{ m_WflInstanceID, m_WflStpInsID }, "String");
					
					//创建步骤实例
					TzWflStep tzWflStpInstance = new TzWflStep(m_CurrUserId);
					boolean createOK = tzWflStpInstance.CreateWorkflowStep0(o_WflCalendar, BusProcessDefID, m_WflInstanceID, l_FirstStpID, "", l_PriLevelID, "6", "4", m_WflStpInsID, userList);
					
					if(createOK){
						String new_StpInsID = tzWflStpInstance.getM_WflStpInsID();
						
						//获取当前步骤实例信息
						TzRecord stpIns_TempRecord = tzGDObject.createRecord("tzms_stpins_tbl");
						stpIns_TempRecord.setColumnValue("tzms_wflinsid", m_WflInstanceID, true);
						stpIns_TempRecord.setColumnValue("tzms_stpinsid", m_WflStpInsID, true);
						if(stpIns_TempRecord.selectByKey() == false){
							throw new TzException("获取当前步骤实例失败");
						}
						
						//步骤责任人
						String tzms_stpproid = stpIns_TempRecord.getTzString("tzms_stpproid").getValue();
						if(StringUtils.isBlank(tzms_stpproid)) {
							//如果没有步骤责任人（管理员后台处理），默认设置一个责任人为步骤责任人
							tzms_stpproid = sqlQuery.queryForObject("select top 1 tzms_stpproid from tzms_acclst_tbl where tzms_stpinsid=? order by tzms_proidtype desc", 
									new Object[] { m_WflStpInsID }, "String");
							
							stpIns_TempRecord.setColumnValue("tzms_stpproid", tzms_stpproid);
						}
						
						//设置当前步骤实例
						stpIns_TempRecord.setColumnValue("tzms_action_type", "拒绝");
						stpIns_TempRecord.setColumnValue("tzms_stpinsstat", "5");
						//任务处理时间
						stpIns_TempRecord.setColumnValue("tzms_tskprodt", new Date());
						//结束时间
						stpIns_TempRecord.setColumnValue("tzms_stpenddt", new Date());
						stpIns_TempRecord.update();
						
						//终止首步骤后续存活实例
						this.killAllAliveStpInsAfterStpID(m_WflInstanceID, l_FirstStpID);
						
						//执行步骤级-拒绝时事件
						m_WflStepEventInterface.ExecuteEventActions("2", "213", m_WflStepID);
						
						//是否不发送通知
						if(isNotSendNotice != true) {
							/***发送通知***/
							List<String> nextUserList = tzWflStpInstance.getM_stepUserList();
							TzWflNotice wflNotice = new TzWflNotice(m_WflInstanceID, new_StpInsID, l_FirstStpID);
							wflNotice.SendWorkflowNotice(nextUserList);
						}
						
						rejectOK = true;
					}
				}else{
					throw new TzException("【系统配置错误】起始步骤不能配置拒绝操作");
				}
			}else {
				throw new TzException("【系统配置错误】没有配置起始步骤");
			}
		}catch (TzException e) {
			throw e;
		}catch (Exception e) {
			throw new TzException(e);
		}
		
		return rejectOK;
	}
	
	
	
	
	/**
	 * 工作流步骤实例激活
	 * 步骤实例状态：1-激活、2-撤回、3-处理中、4-结束、5-提前终止、6-未签收、7-退回、8-已签收、9-转发
	 * @throws TzException
	 */
	public void WflStepActivate() throws TzException {
		//获取当前步骤实例状态
		String l_StpInsSta = m_WflStepInsRecord.getTzString("tzms_stpinsstat").getValue();
		
		if("4".equals(l_StpInsSta) 
				|| "5".equals(l_StpInsSta) 
				|| "7".equals(l_StpInsSta) 
				|| "9".equals(l_StpInsSta)){
			try{
				m_WflStepInsRecord.setColumnValue("tzms_action_type", "激活任务");
				m_WflStepInsRecord.setColumnValue("tzms_stpinsstat", "1");
				m_WflStepInsRecord.setColumnValue("tzms_stpenddt", null);
				
				m_WflStepInsRecord.update();
			}catch(Exception e){
				e.printStackTrace();
				logger.error("步骤实例激活错误，工作流实例ID："+ m_WflInstanceID +"，步骤实例ID："+ m_WflStpInsID +"，异常信息：", e);
				throw new TzException("业务流程步骤实例激活失败");
			}
		}
	}
}
