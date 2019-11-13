package com.tranzvision.gd.workflow.engine;

import com.tranzvision.gd.util.base.AnalysisClsMethod;
import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzException;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
import com.tranzvision.gd.util.sql.type.TzRecord;
import com.tranzvision.gd.workflow.base.CommonFun;
import com.tranzvision.gd.workflow.base.ErrorMessage;
import com.tranzvision.gd.workflow.base.TzStpActInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 工作流类-用于管理工作流类实例
 * @author 张浪	2019-01-09
 * @version 1.0
 */
public class TzWorkflow {
	
	private TZGDObject tzGDObject;
	
	private SqlQuery sqlQuery;
	
	
	
	/*当前登录人Dynamic用户ID*/
	private String m_CurrUserId;
	
	/*工作流事件接口*/
	private TzWflEvent m_WflEventInterface;
	
	/*工作流实例编号*/
	private String m_WflInstanceID;
	
	/*步骤实例编号*/
	private String m_WflStpInsID;
	
	//业务流程定义编号
	private String m_BusProcessDefID;
	
	/*工作流业务流程配置信息表*/
	private TzRecord m_WorkflowCfgRecord;
	
	/*工作流日历*/
	private TzWflCalendar m_Canlendar;
	
	/*工作流实例信息表*/
	private TzRecord m_WorkflowInsRecord;
	
	/*工作流步骤实例*/
	private TzWflStep m_WorkflowStepInstance;

	//可供选择的下一路径
	private List<TzStpActInfo> ChoosableNextActionPath;
	
	//是否管理员操作
	private boolean isAdminOperate = false;
	//是否不发送通知
	private boolean isNotSendNotice = false;
	
	
	//记录日志
	private static final Logger logger = Logger.getLogger("WorkflowEngine");
	
	

	
	public String getM_WflInstanceID() {
		return m_WflInstanceID;
	}
	public TzRecord getM_WorkflowCfgRecord() {
		return m_WorkflowCfgRecord;
	}
	public TzWflCalendar getM_Canlendar() {
		return m_Canlendar;
	}
	public TzWflEvent getM_WflEventInterface() {
		return m_WflEventInterface;
	}
	public TzRecord getM_WorkflowInsRecord() {
		return m_WorkflowInsRecord;
	}
	public TzWflStep getM_WorkflowStepInstance() {
		return m_WorkflowStepInstance;
	}
	public String getM_WflStpInsID() {
		return m_WflStpInsID;
	}
	public void setAdminOperate(boolean isAdminOperate) {
		this.isAdminOperate = isAdminOperate;
	}
	public void setNotSendNotice(boolean isNotSendNotice) {
		this.isNotSendNotice = isNotSendNotice;
	}
	
	
	
	
	
	
	/**
	 * 构造函数
	 * @param UserId	Dynamics系统用户ID
	 */
	public TzWorkflow(String UserId){
		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		
		tzGDObject = (TZGDObject) getSpringBeanUtil.getAutowiredSpringBean("TZGDObject");
		sqlQuery = (SqlQuery) getSpringBeanUtil.getAutowiredSpringBean("SqlQuery");
		
		m_CurrUserId = UserId;
	}
	
	
	
	
	/**
	 * 根据指定业务流程定义编号实例化工作流对象
	 * @param busProcessId	业务流程定义编号
	 * @param busDataID		业务数据ID
	 * @return
	 * @throws Exception 
	 */
	public boolean CreateWorkflow0(String busProcessId, String busDataID) throws TzException {
		boolean createOK = false;
		try{
			//判断业务数据是否已经创建业务流程
			int count = sqlQuery.queryForObject("select count(*) from tzms_wflins_tbl where tzms_wfcldn_uniqueid=? and tzms_wflrecord_uniqueid=?", 
					new Object[] { busProcessId, busDataID }, "int");
			if(count > 0) {
				throw new TzException("创建业务流程失败，该任务号对应业务单据已经创建过业务流程");
			}
			
			m_BusProcessDefID = busProcessId; 
			
			//生成工作流实例编号
			CommonFun commonFun = new CommonFun();
			m_WflInstanceID = commonFun.GenerateSeqGUID("WFINS", 30);
			
			//创建事件接口对象
			m_WflEventInterface = new TzWflEvent();
			m_WflEventInterface.setM_WflInstanceID(m_WflInstanceID);
			m_WflEventInterface.setM_userId(m_CurrUserId);
			
			/*【执行工作流初始化前事件】*/
			m_WflEventInterface.ExecuteEventActions("1", "101", busProcessId);
			
			//获取工作流业务类别配置信息
			TzRecord buspro_TempRecord = null;
			try {
				buspro_TempRecord = tzGDObject.createRecord("tzms_wfcldn_tBase");
				buspro_TempRecord.setColumnValue("tzms_wfcldn_tid", busProcessId, true);
				
				if(buspro_TempRecord.selectByKey() == false){
					throw new TzException("【系统配置错误】获取业务流程配置信息失败");
				}
				
				m_WorkflowCfgRecord = buspro_TempRecord;
			} catch (TzException e) {
				e.printStackTrace();
				return false;
			}
			
			//关联业务实体
			String entityName = m_WorkflowCfgRecord.getTzString("tzms_entity_name").getValue();
			//工作流业务类别
			String wflBusinessID = m_WorkflowCfgRecord.getTzString("tzms_business_uniqueid").getValue();
			//优先级别编号
			String priLevelID = m_WorkflowCfgRecord.getTzString("tzms_pritpyid").getValue();
			//日历编号
			String calendarid = m_WorkflowCfgRecord.getTzString("tzms_calendarid").getValue();
			if(StringUtils.isNotEmpty(calendarid)){
				//初始化工作流日历本
				m_Canlendar = new TzWflCalendar(calendarid);
			}
			
			
			//创建工作流实例
			TzRecord tmp_wflinsRecord = tzGDObject.createRecord("tzms_wflins_tbl");
			//设置工作流实例编号
			tmp_wflinsRecord.setColumnValue("tzms_wflinsid", m_WflInstanceID);
			//设置工作流开始时间
			tmp_wflinsRecord.setColumnValue("tzms_wfstartdt", new Date());
			//设置工作流发起人编号
			tmp_wflinsRecord.setColumnValue("tzms_startproid", m_CurrUserId);
			//设置工作流业务类别编号
			tmp_wflinsRecord.setColumnValue("tzms_bustypeid", wflBusinessID);
			//设置业务流程编号
			tmp_wflinsRecord.setColumnValue("tzms_wfcldn_uniqueid", busProcessId);
			//设置业务数据ID
			tmp_wflinsRecord.setColumnValue("tzms_wflrecord_uniqueid", busDataID);
			//设置工作流存活状态,2-处理中
			tmp_wflinsRecord.setColumnValue("tzms_wflstatus", 2);
			//设置创建人
			tmp_wflinsRecord.setColumnValue("ROW_ADDED_OPRID", m_CurrUserId);
			//设置创建时间
			tmp_wflinsRecord.setColumnValue("ROW_ADDED_DTTM", new Date());
			//设置最后修改人
			tmp_wflinsRecord.setColumnValue("ROW_LASTMANT_OPRID", m_CurrUserId);
			//设置最后修改时间
			tmp_wflinsRecord.setColumnValue("ROW_LASTMANT_DTTM", new Date());
			
			//设置流程TaskID
			if(StringUtils.isNotBlank(entityName)) {
				String taskId = sqlQuery.queryForObject("select tzms_task_number from " + entityName + "Base where " + entityName + "Id=?", 
						new Object[] { busDataID }, "String");
				tmp_wflinsRecord.setColumnValue("tzms_taskid", taskId);
			}
			
			/**************流程超时计算***************/
			Integer l_OverTime = sqlQuery.queryForObject("select tzms_wflovertime_value from tzms_wflovertime_t where tzms_wflcldnid=? and tzms_defaultflg=1 and statecode=1", 
					new Object[]{ busProcessId }, "Integer");
			tmp_wflinsRecord.setColumnValue("tzms_tmoutlimit", l_OverTime);
			
			if(l_OverTime != null && l_OverTime > 0 
					&& StringUtils.isNotEmpty(calendarid)){
				
				Date l_OutTime = m_Canlendar.GetEndTimingDate(new Date(), l_OverTime);
				
				tmp_wflinsRecord.setColumnValue("tzms_outmdt", l_OutTime);
				if(new Date().before(l_OutTime)){
					tmp_wflinsRecord.setColumnValue("tzms_tmouflg", false);
				}else{
					tmp_wflinsRecord.setColumnValue("tzms_tmouflg", true);
				}
			}
			
			//查询默认优先级类别
			String l_PriLevelXm = "";
			if(StringUtils.isNotEmpty(priLevelID)){
				l_PriLevelXm = sqlQuery.queryForObject("select tzms_wf_rpcdtl_tid from tzms_wf_rpcdtl_t where tzms_pritpy_uniqueid=? and tzms_is_default=1", 
						new Object[]{ priLevelID }, "String");
				
				//设置默认有限级别
				tmp_wflinsRecord.setColumnValue("tzms_pridtlid", l_PriLevelXm);
			}
			
			
			/*保存工作流实例*/
			m_WorkflowInsRecord = tmp_wflinsRecord;
			if(tmp_wflinsRecord.insert() == false){
				throw new TzException("保存业务流程实例失败");
			}else {
				logger.info("★★★ 创建业务流程实例成功，业务流程名称：" + m_WorkflowCfgRecord.getTzString("tzms_wfclname").getValue() 
						+ "，工作流实例ID:" + m_WflInstanceID);
			}
			
			//获取起始步骤编号
			String l_FirstStepID = this.GetWflStartStepID(busProcessId);
			List<String> l_userList = new ArrayList<String>();
			l_userList.add(m_CurrUserId);
			
			/******************生成步骤实例*********************/
			m_WorkflowStepInstance = new TzWflStep(m_CurrUserId);
			m_WorkflowStepInstance.CreateWorkflowStep0(m_Canlendar, busProcessId, m_WflInstanceID, l_FirstStepID, m_CurrUserId, l_PriLevelXm, "3", "3", "", l_userList);
			
			//设置工作流步骤实例编号
			m_WflStpInsID = m_WorkflowStepInstance.getM_WflStpInsID();
			
			/*【执行工作流初始化后事件】*/
			m_WflEventInterface.ExecuteEventActions("1", "102", busProcessId);
			
			createOK = true;
		}catch(TzException e){
			//e.printStackTrace();
			logger.error("创建工作流实例失败，异常信息：", e);
			throw e;
		}catch(Exception e){
			//e.printStackTrace();
			logger.error("创建工作流实例失败，异常信息：", e);
			throw new TzException("业务流程创建失败，请稍后重试");
		}
		
		return createOK;
	}
	
	
	
	
	
	/**
	 * 根据指定的当前工作流实例编号以及当前步骤实例编号构建实例对象
	 * @param s_WflInsID		工作流实例编号
	 * @param s_WflStpInsID		步骤实例编号
	 * @return
	 * @throws Exception
	 */
	public boolean CreateWorkflow1(String s_WflInsID, String s_WflStpInsID) throws TzException {
		boolean createOK = false;
		try{
			if(StringUtils.isNotBlank(s_WflInsID) 
					&& StringUtils.isNotBlank(s_WflStpInsID)) {
				
				m_WflInstanceID = s_WflInsID;
				m_WflStpInsID = s_WflStpInsID;
				
				/*读取当前工作流实例记录*/
				TzRecord wflIns_TempRecord = tzGDObject.createRecord("tzms_wflins_tbl");
				wflIns_TempRecord.setColumnValue("tzms_wflinsid", s_WflInsID, true);
				if(wflIns_TempRecord.selectByKey() == false){
					throw new TzException("获取业务流程实例失败");
				}
				m_WorkflowInsRecord = wflIns_TempRecord;
				
				//业务流程ID
				String wflBusProcessID = wflIns_TempRecord.getTzString("tzms_wfcldn_uniqueid").getValue();
				m_BusProcessDefID = wflBusProcessID;
				
				/*读取工作流配置信息*/
				TzRecord buspro_TempRecord = tzGDObject.createRecord("tzms_wfcldn_tBase");
				buspro_TempRecord.setColumnValue("tzms_wfcldn_tid", wflBusProcessID, true);

				if(buspro_TempRecord.selectByKey() == false){
					throw new TzException("【系统配置错误】获取业务流程配置信息失败");
				}
				m_WorkflowCfgRecord = buspro_TempRecord;
				
				/*根据步骤实例编号构建步骤实例对象*/
				m_WorkflowStepInstance = new TzWflStep(m_CurrUserId);
				m_WorkflowStepInstance.setNotSendNotice(isNotSendNotice);
				m_WorkflowStepInstance.CreateWorkflowStep2(s_WflInsID, s_WflStpInsID);
				
				//读取步骤实例责任人
				//String l_UserID = m_WorkflowStepInstance.getM_WflStepInsRecord().getTzString("tzms_stpproid").getValue();
				
				/*初始化工作流日历本*/
				String calendarid = m_WorkflowCfgRecord.getTzString("tzms_calendarid").getValue();
				if(StringUtils.isNotEmpty(calendarid)){
					//初始化工作流日历本
					m_Canlendar = new TzWflCalendar(calendarid);
				}
				
				/*创建事件接口对象*/
				m_WflEventInterface = new TzWflEvent();
				m_WflEventInterface.setM_WflInstanceID(s_WflInsID);
				m_WflEventInterface.setM_WflStpInsID(s_WflStpInsID);
				m_WflEventInterface.setM_userId(m_CurrUserId);
				
				/*【执行工作流载入时事件】*/
				m_WflEventInterface.ExecuteEventActions("1", "103", wflBusProcessID);
				
				//当前登录人是否为步骤责任人，如果不是则视为管理员，不发送通知
				String inStpOpr = sqlQuery.queryForObject("select 'Y' from tzms_acclst_tbl where tzms_stpinsid=? and tzms_stpproid=?", 
						new Object[] { s_WflStpInsID, m_CurrUserId }, "String");
				if(!"Y".equals(inStpOpr)) {
					isNotSendNotice = true;
				}
				
				createOK = true;
			}else {
				throw new TzException("业务流程参数错误");
			}
		} catch (TzException e) {
			logger.error("工作流初始化失败，工作流实例编号："+ s_WflInsID +"，步骤实例编号："+ s_WflStpInsID +"，异常信息：", e);
			throw e;
		} catch(Exception e){
			logger.error("工作流初始化失败，工作流实例编号："+ s_WflInsID +"，步骤实例编号："+ s_WflStpInsID +"，异常信息：", e);
			throw new TzException("业务流程初始化失败:"+ e.toString());
		}
		
		return createOK;
	}
	
	
	
	
	/**
	 * 创建业务流程并提交
	 * 该方法主要用于直接创建流程申请，需要提前创建业务表单数据，创建的任务会自动提交
	 * @param busProcessId	业务流程ID
	 * @param busDataID		业务数据ID
	 * @return
	 * @throws TzException
	 */
	public boolean CreateWorkflowAndSubmit(String busProcessId, String busDataID) throws TzException {
		boolean createOk = this.CreateWorkflow0(busProcessId, busDataID);
		if(createOk) {
			ErrorMessage ErrorMsg_OUT = new ErrorMessage();
			createOk = this.WorkflowSubmitOrAgree("", ErrorMsg_OUT, "SUBMIT");
		}
		return createOk;
	}
	
	
	
	
	
	/**
	 * 根据业务流程编号获取工作流起始步骤编号
	 * @param busProDfnId	业务流程定义编号
	 * @return
	 * @throws TzException
	 */
	public String GetWflStartStepID(String busProDfnId) throws TzException {
		//业务流程名称
		String l_WflName = m_WorkflowCfgRecord.getTzString("tzms_wfclname").getValue();
		
		//查询工作流起始步骤数量
		int l_FirstStepCount = sqlQuery.queryForObject("select count(*) from tzms_wflstp_t where tzms_wfcldn_uniqueid=? and tzms_stptype=1", 
				new Object[]{ busProDfnId }, "int");
		if(l_FirstStepCount > 1){
			throw new TzException("【系统配置错误】业务流程[" + l_WflName + "]配置了多个起始步骤，请联系系统管理员配置。");
		}
		
		//查询起始步骤编号
		String l_WflStepID = sqlQuery.queryForObject("select tzms_wflstp_tid from tzms_wflstp_t where tzms_wfcldn_uniqueid=? and tzms_stptype=1", 
				new Object[]{ busProDfnId }, "String");
		if(StringUtils.isEmpty(l_WflStepID)){
			throw new TzException("【系统配置错误】业务流程[" + l_WflName + "]没有配置起始步骤，请联系系统管理员配置。");
		}
		
		return l_WflStepID;
	}
	
	
	
	/**
	 * 检查操作权限
	 * @param wflStpId		步骤ID
	 * @param operateType	操作类型，SUB-提交，AGR-同意，TRN-退回重填，BAK-退回某步，TRA-转发,
	 * 						WIT-撤回，CAN-撤销，REJ-拒绝
	 * @return
	 */
	private boolean checkOperateAuth(String wflStpId, String operateType) {
		boolean checkOk = true;
		String used = "";
		switch (operateType) {
		case "SUB":	//提交
			used = sqlQuery.queryForObject("select 'Y' from tzms_wflstp_t where tzms_wflstp_tid=? and tzms_dpysubmitflg=1", 
					new Object[] { wflStpId }, "String");
			if(!"Y".equals(used)) {
				checkOk = false;
			}
			break;
		case "AGR":	//同意
			used = sqlQuery.queryForObject("select 'Y' from tzms_wflstp_t where tzms_wflstp_tid=? and tzms_dpyapproveflg=1", 
					new Object[] { wflStpId }, "String");
			if(!"Y".equals(used)) {
				checkOk = false;
			}
			break;
		case "RTN":	//退回重填
			used = sqlQuery.queryForObject("select 'Y' from tzms_wflstp_t where tzms_wflstp_tid=? and tzms_dpybak_reflg=1", 
					new Object[] { wflStpId }, "String");
			if(!"Y".equals(used)) {
				checkOk = false;
			}
			break;
		case "BAK":	//退回某步
			used = sqlQuery.queryForObject("select 'Y' from tzms_wflstp_t where tzms_wflstp_tid=? and tzms_dpybakflg=1", 
					new Object[] { wflStpId }, "String");
			if(!"Y".equals(used)) {
				checkOk = false;
			}
			break;
		case "TRA":	//转发
			used = sqlQuery.queryForObject("select 'Y' from tzms_wflstp_t where tzms_wflstp_tid=? and tzms_dpytsfflg=1", 
					new Object[] { wflStpId }, "String");
			if(!"Y".equals(used)) {
				checkOk = false;
			}
			break;
		case "WIT":	//撤回
			used = sqlQuery.queryForObject("select 'Y' from tzms_wflstp_t where tzms_wflstp_tid=? and tzms_dpycalflg=1", 
					new Object[] { wflStpId }, "String");
			if(!"Y".equals(used)) {
				checkOk = false;
			}
			break;
		case "CAN":	//撤销
			used = sqlQuery.queryForObject("select 'Y' from tzms_wflstp_t where tzms_wflstp_tid=? and tzms_dpyrevokeflg=1", 
					new Object[] { wflStpId }, "String");
			if(!"Y".equals(used)) {
				checkOk = false;
			}
			break;
		case "REJ":	//拒绝
			used = sqlQuery.queryForObject("select 'Y' from tzms_wflstp_t where tzms_wflstp_tid=? and tzms_dpybatrefuseflg=1", 
					new Object[] { wflStpId }, "String");
			if(!"Y".equals(used)) {
				checkOk = false;
			}
			break;
		default:
			break;
		}
		
		return checkOk;
	}
	
	
	
	/**
	 * 工作流签收
	 * @param signErrMsg_OUT	失败信息
	 * @return
	 */
	public boolean WorkflowSign(ErrorMessage ErrorMsg_OUT){
		boolean Sign_OK;
		Sign_OK = m_WorkflowStepInstance.WorkflowStepSign(m_Canlendar, isAdminOperate, ErrorMsg_OUT);
			
		return Sign_OK;
	}
	
	
	
	/**
	 * 工作流转发
	 * @param trf_UserId	转发用户ID
	 * @return
	 * @throws TzException
	 */
	 public boolean WorkflowTransfer(String trf_UserId) throws TzException {
		boolean TransferOK = true;
		try {
			/*【STP1】---检查当前步骤实例状态*/
			m_WorkflowStepInstance.checkWflStpInstanceStatus();
			
			/*【STP2】---检查当前步骤操作权限*/
			String l_WflStepID = m_WorkflowStepInstance.getM_WflStepID();
			
			if(m_WorkflowStepInstance.checkOperatingAuthorization(m_WflInstanceID, l_WflStepID) == false){
				throw new TzException("当前业务流程前续步骤有未处理的任务，请您耐心等待处理完成后执行转发操作");
			}
			
			if(isAdminOperate || this.checkOperateAuth(l_WflStepID, "TRA") == true) {
				TransferOK = m_WorkflowStepInstance.WorkflowTransfer(m_Canlendar, trf_UserId);
				
				if(TransferOK) {
					//业务流程编号
					String BusProcessDefID =  m_WorkflowCfgRecord.getTzString("tzms_wfcldn_tid").getValue();
					
					//触发步骤转发事件
					TzWflEvent wflStpEvent =  m_WorkflowStepInstance.getM_WflStepEventInterface();
					wflStpEvent.ExecuteEventActions("2", "209", l_WflStepID);
					
					/*触发其他事件-转发时*/
					//m_WflEventInterface.ExecuteEventActions("4", "403", BusProcessDefID);
					
					/*触发工作流转发事件*/
					m_WflEventInterface.ExecuteEventActions("1", "110", BusProcessDefID);
				}
			}else {
				throw new TzException("当前业务流程步骤没有启用转发功能，您不能执行转发操作");
			}
		}catch(Exception e) {
			logger.error("工作流转发失败，工作流实例编号："+ m_WflInstanceID +"，步骤实例编号："+ m_WflStpInsID +"，转发用户ID："+ trf_UserId +"，异常信息：", e);
			throw e;
		}
		
		return TransferOK;
	}
	
	 
	 
	 
	/**
	 * 保存处理意见
	 * @param s_WflInsID		业务流程实例编号
	 * @param s_StpInsID		步骤实例编号
	 * @param suggestText		处理意见
	 * @param defaultSuggest	默认处理意见
	 * @throws TzException
	 */
	public void SaveWorkflowSuggest(String s_WflInsID, String s_StpInsID, String suggestText, String defaultSuggest) throws TzException {
		if(StringUtils.isEmpty(suggestText)){
			suggestText = defaultSuggest;
		}else if(!StringUtils.startsWithIgnoreCase(suggestText, defaultSuggest)){
			suggestText = ("".equals(defaultSuggest) ? "" : (defaultSuggest + "：")) + suggestText;
		}
		
		try{
			TzRecord stpIns_TempRecord = tzGDObject.createRecord("tzms_stpins_tbl");
			stpIns_TempRecord.setColumnValue("tzms_wflinsid", s_WflInsID, true);
			stpIns_TempRecord.setColumnValue("tzms_stpinsid", s_StpInsID, true);
			
			if(stpIns_TempRecord.selectByKey() == true){
				//任务处理意见
				stpIns_TempRecord.setColumnValue("tzms_tskprorec", suggestText);
				//任务处理人
				stpIns_TempRecord.setColumnValue("tzms_tskproid", m_CurrUserId);
				//任务处理时间
				stpIns_TempRecord.setColumnValue("tzms_tskprodt", new Date());
				
				stpIns_TempRecord.setColumnValue("ROW_LASTMANT_DTTM", new Date());
				
				stpIns_TempRecord.update();
			}
		}catch(Exception e){
			logger.error("保存处理意见失败，异常信息：", e);
			throw new TzException("处理意见保存失败");
		}
	}
	
	
	
	/**
	 * 解析当前步骤实例下一动作路径是否唯一
	 * @return
	 * @throws TzException
	 */
	public boolean NextActionIsOnlyPath() throws TzException {
		int count = 0;
		List<TzStpActInfo> actPathList = m_WorkflowStepInstance.analysisNextActionAndUserList(m_BusProcessDefID);
		
		//暂存可选的动作路径信息
		ChoosableNextActionPath = new ArrayList<TzStpActInfo>();
		
		if(actPathList != null && actPathList.size() > 0){
			for(TzStpActInfo stpActInfo: actPathList){
				if(stpActInfo.isM_IsConAction() == true){
					ChoosableNextActionPath.add(stpActInfo);
					count ++;
				}
			}
		}else{
			throw new TzException("【系统配置错误】当前步骤没有可流转的后续步骤，请联系系统管理员配置");
		}
		
		if(count > 1){
			return false;
		}
		return true;
	}
	
	
	
	/**
	 * 获取可供选择的动作及用户
	 * @return
	 */
	public List<TzStpActInfo> getNextActionAndUserList(){
		return ChoosableNextActionPath;
	}
	
	
	
	/**
	 * 	设置工作流流转路径
	 * 	只有在提交或同意时，动作路径不唯一时，需要设置此参数
	 * @param actInfo
	 * @throws TzException
	 */
	public void SetWorkflowActionPathInfo(TzStpActInfo actInfo) throws TzException {
		if(m_WorkflowStepInstance == null){
			throw new TzException("请先初始化业务流程实例");
		}
		m_WorkflowStepInstance.setM_WorkflowActionInfo(actInfo);
	}
	
	
	/**
	 * 工作流提交
	 * @param Suggest_IN		处理意见
	 * @param ErrorMsg_OUT		错误输出
	 * @return
	 */
	public boolean WorkflowSubmit(String Suggest_IN, ErrorMessage ErrorMsg_OUT){
		try {
			/*【STP1】---检查当前步骤实例状态*/
			m_WorkflowStepInstance.checkWflStpInstanceStatus();
			
			/*【STP2】---检查当前步骤操作权限*/
			String l_WflStepID = m_WorkflowStepInstance.getM_WflStepID();
			
			if(m_WorkflowStepInstance.checkOperatingAuthorization(m_WflInstanceID, l_WflStepID) == false){
				throw new TzException("当前业务流程前续步骤有未处理的任务，请您耐心等待处理完成后执行提交操作");
			}
			
			if(this.checkOperateAuth(l_WflStepID, "SUB") == true) {
				return this.WorkflowSubmitOrAgree(Suggest_IN, ErrorMsg_OUT, "SUBMIT");
			}else {
				throw new TzException("当前业务流程步骤没有启用提交功能，您不能执行提交操作");
			}
		}catch(TzException e) {
			ErrorMsg_OUT.setErrorCode("1");
			ErrorMsg_OUT.setErrorMsg(e.getMessage());
			return false;
		}
	}
	
	
	/**
	 * 工作流同意
	 * @param Suggest_IN		处理意见
	 * @param ErrorMsg_OUT		错误输出
	 * @return
	 */
	public boolean WorkflowAgree(String Suggest_IN, ErrorMessage ErrorMsg_OUT){
		try {
			/*【STP1】---检查当前步骤实例状态*/
			m_WorkflowStepInstance.checkWflStpInstanceStatus();
			
			/*【STP2】---检查当前步骤操作权限*/
			String l_WflStepID = m_WorkflowStepInstance.getM_WflStepID();
			
			if(m_WorkflowStepInstance.checkOperatingAuthorization(m_WflInstanceID, l_WflStepID) == false){
				throw new TzException("当前业务流程前续步骤有未处理的任务，请您耐心等待处理完成后执行审批同意操作");
			}
			
			if(this.checkOperateAuth(l_WflStepID, "AGR") == true) {
				return this.WorkflowSubmitOrAgree(Suggest_IN, ErrorMsg_OUT, "AGREE");
			}else {
				throw new TzException("当前业务流程步骤没有启用审批同意功能，您不能执行审批同意操作");
			}
		}catch(TzException e) {
			ErrorMsg_OUT.setErrorCode("1");
			ErrorMsg_OUT.setErrorMsg(e.getMessage());
			return false;
		}
	}
	
	
	
	/**
	 * 检查提交条件
	 * @return
	 * @throws TzException
	 */
	private boolean checkStepSubmitCondition() throws TzException {
		boolean checkOK = true;
		
		//工作流步骤配置
		TzRecord l_stpCfgRec = m_WorkflowStepInstance.getM_WflStepCfgRecord();
		//启用提交条件检查
		boolean l_qySubmit = l_stpCfgRec.getTzBoolean("tzms_dpsubmit").getValue();
		//提交条件类定义
		String submitClsId = l_stpCfgRec.getTzString("tzms_appcls_uniqueid").getValue();
		
		if(l_qySubmit == true){
			if(StringUtils.isNotEmpty(submitClsId)){
				//初始化类方法解析引擎
				AnalysisClsMethod analysisCls = new AnalysisClsMethod(submitClsId);

				//设置参数类型，如果是DLL类只能传入基本类型
				String[] parameterTypes = new String[] { "String", "String", "String" };
				
				//业务数据ID
				String wflDateRecId =  m_WorkflowInsRecord.getTzString("tzms_wflrecord_uniqueid").getValue();
				
				//设置参数数组
				Object[] arglist = new Object[] { m_WflInstanceID, m_WflStpInsID, wflDateRecId};
				//设置类方法参数
				analysisCls.setJavaClsParameter(parameterTypes, arglist);
				
				logger.info("【提交条件检查】参数：【"+ m_WflInstanceID +"】【"+ m_WflStpInsID +"】【"+ wflDateRecId +"】");
				try {
					//执行类方法
					String result = (String) analysisCls.execute();
					
					logger.info("【提交条件检查】业务流程提交检查条件返回： " + result);

					JacksonUtil jacksonUtil = new JacksonUtil();
					jacksonUtil.json2Map(result);
				
					checkOK = jacksonUtil.getBoolean("checkResult");
					
					if(checkOK != true){
						throw new TzException(jacksonUtil.getString("errorMsg"));
					}
				}catch(TzException e) {
					throw e;
				}catch (Exception e) {
					throw new TzException("【系统配置错误】提交检查条件配置的类方法错误，类方法定义编号：[" + submitClsId + "]，请联系管理员重新配置");
				}
			}else{
				throw new TzException("【系统配置错误】应用程序类定义不存在，类方法定义编号：[" + submitClsId + "]，请联系管理员重新配置");
			}
		}		
		return checkOK;
	}
	
	
	
	/**
	 * 工作流提交或同意
	 * @param Suggest_IN		处理意见
	 * @param ErrorMsg_OUT		错误信息输出
	 * @param type			SUBMIT、AGREE
	 * @return
	 */
	private boolean WorkflowSubmitOrAgree(String Suggest_IN, ErrorMessage ErrorMsg_OUT, String type){
		
		boolean bool_sub = true;

		try {
			String defaultSuggest = "";
			int tzms_stptype = m_WorkflowStepInstance.getM_WflStepCfgRecord().getTzInt("tzms_stptype").getValue();
			if(tzms_stptype != 1) {
				//非起始步骤按钮名称作为默认意见
				String tzms_buttonname = m_WorkflowStepInstance.getM_WflStepCfgRecord().getTzString("tzms_buttonname").getValue();
				defaultSuggest = tzms_buttonname;
			}
			
			//保存处理意见
			this.SaveWorkflowSuggest(m_WflInstanceID, m_WflStpInsID, Suggest_IN, defaultSuggest);
			
			//检查步骤提交条件判断
			this.checkStepSubmitCondition();
			
			//提交当前步骤实例
			m_WorkflowStepInstance.WorkflowSubmitOrAgree(m_Canlendar, m_BusProcessDefID, type);
			
			//执行工作流提交时事件
			m_WflEventInterface.ExecuteEventActions("1", "111", m_BusProcessDefID);
			
			//检查当前工作流实例所有活动的步骤实例数，如果没有活动步骤实例，则终止当前工作流实例
			int l_liveStpIns = sqlQuery.queryForObject("select count(*) from tzms_stpins_tbl where tzms_wflinsid=? and tzms_stpinsstat in('1','3','6','8') and tzms_asgmethod in('2','3','4','5')", 
					new Object[]{ m_WflInstanceID }, "int");
			if(l_liveStpIns <= 0){
				//触发工作流结束结束前事件
				m_WflEventInterface.ExecuteEventActions("1", "104", m_BusProcessDefID);
				
				//结束工作流
				sqlQuery.update("update tzms_wflins_tbl set tzms_wflstatus='3',tzms_wfenddt=? where tzms_wflinsid=?", 
						new Object[]{ new Date(), m_WflInstanceID });
				
				//触发工作流结束后事件
				m_WflEventInterface.ExecuteEventActions("1", "105", m_BusProcessDefID);
			}
		} catch (TzException e) {
			e.printStackTrace();
			ErrorMsg_OUT.setErrorCode("1");
			ErrorMsg_OUT.setErrorMsg(e.getMessage());
			bool_sub = false;
			logger.error("工作流提交或同意失败，工作流实例ID："+ m_WflInstanceID +"，步骤实例ID："+ m_WflStpInsID +"，异常信息：", e);
		} catch(Exception e){
			e.printStackTrace();
			ErrorMsg_OUT.setErrorCode("1");
			ErrorMsg_OUT.setErrorMsg("【系统错误】" + e.toString());
			bool_sub = false;
			logger.error("工作流提交或同意失败，工作流实例ID："+ m_WflInstanceID +"，步骤实例ID："+ m_WflStpInsID +"，异常信息：", e);
		}

		return bool_sub;
	}
	

	
	
	/**
	 * 知会任务已阅处理
	 * @return
	 */
	public boolean TaskReadOver(){
		boolean isReaded;
		
		isReaded = m_WorkflowStepInstance.TaskReadOver();
		
		return isReaded;
	}
	
	
	
	/**
	 * 工作流拒绝
	 * @param Suggest_IN	拒绝处理意见
	 * @param ErrorMsg_OUT	返回错误信息
	 * @return
	 */
	public boolean WorkflowReject(String Suggest_IN, ErrorMessage ErrorMsg_OUT){
		boolean isReject = false;
		
		try {
			/*【STP1】---检查当前步骤实例状态*/
			m_WorkflowStepInstance.checkWflStpInstanceStatus();
			
			/*【STP2】---检查当前步骤操作权限*/
			String l_WflStepID = m_WorkflowStepInstance.getM_WflStepID();
			
			if(m_WorkflowStepInstance.checkOperatingAuthorization(m_WflInstanceID, l_WflStepID) == false){
				throw new TzException("当前业务流程前续步骤有未处理的任务，请您耐心等待处理完成后执行拒绝操作");
			}
			
			if(this.checkOperateAuth(l_WflStepID, "REJ") == true) {
				//保存拒绝意见
				this.SaveWorkflowSuggest(m_WflInstanceID, m_WflStpInsID, Suggest_IN, "拒绝");
				
				isReject = m_WorkflowStepInstance.WorkflowReject(m_Canlendar);
			}else {
				throw new TzException("当前业务流程步骤没有启用拒绝功能，您不能执行拒绝操作");
			}
		} catch (TzException e) {
			e.printStackTrace();
			isReject = false;
			ErrorMsg_OUT.setErrorCode("1");
			ErrorMsg_OUT.setErrorMsg(e.getMessage());
			logger.error("工作流拒绝错误，工作流实例ID："+ m_WflInstanceID +"，步骤实例ID："+ m_WflStpInsID +"，异常信息：", e);
		}
		
		return isReject;
	}
	
	
	
	/**
	 * 退回重填
	 * 将当前步骤实例结束，并重新创建首步骤实例，如果当前步骤是并发或会签步骤，其他步骤实例状态不变
	 * @param	Suggest_IN		处理意见
	 * @return
	 * @throws TzException
	 */
	public boolean BackAndReFill(String Suggest_IN) throws TzException {
		boolean backSuccess = true;

		/*【STP1】---检查当前步骤实例状态*/
		m_WorkflowStepInstance.checkWflStpInstanceStatus();
		
		/*【STP2】---检查当前步骤操作权限*/
		String l_WflStepID = m_WorkflowStepInstance.getM_WflStepID();
		
		if(m_WorkflowStepInstance.checkOperatingAuthorization(m_WflInstanceID, l_WflStepID) == false){
			throw new TzException("当前业务流程前续步骤有未处理的任务，请您耐心等待处理完成后执行退回重填操作");
		}
		
		if(this.checkOperateAuth(l_WflStepID, "TRN") == true) {
			//保存处理意见
			this.SaveWorkflowSuggest(m_WflInstanceID, m_WflStpInsID, Suggest_IN, "退回重填");

			/*【STP3】---创建动作实例*/
			TzWflAction tzWflAction = new TzWflAction(m_BusProcessDefID, m_WflInstanceID, m_WflStpInsID, l_WflStepID, m_CurrUserId);
			/*【STP4】---执行退回重填动作*/
			backSuccess = tzWflAction.BackAndReFill(m_Canlendar);
		}else {
			backSuccess = false;
			throw new TzException("当前业务流程步骤没有启用退回重填功能，您不能执行退回重填操作");
		}
		
		return backSuccess;
	}
	
	
	
	/**
	 * 退回至指定步骤
	 * @param Suggest_IN	处理意见
	 * @param stepId		退回步骤ID
	 * @return
	 * @throws TzException
	 */
	public boolean BackToChonseStep(String Suggest_IN, String s_StepID) throws TzException {
		boolean backOk = true;

		try {
			/*【STP1】---检查当前步骤实例状态*/
			m_WorkflowStepInstance.checkWflStpInstanceStatus();
			
			/*【STP2】---检查当前步骤操作权限*/
			String l_WflStepID = m_WorkflowStepInstance.getM_WflStepID();
			
			if(m_WorkflowStepInstance.checkOperatingAuthorization(m_WflInstanceID, l_WflStepID) == false){
				throw new TzException("当前业务流程前续步骤有未处理的任务，请您耐心等待处理完成后执行退回操作");
			}
			
			if(this.checkOperateAuth(l_WflStepID, "BAK") == true) {
				//保存处理意见
				this.SaveWorkflowSuggest(m_WflInstanceID, m_WflStpInsID, Suggest_IN, "退回");
				
				/*【STP3】---创建动作实例*/
				TzWflAction tzWflAction = new TzWflAction(m_BusProcessDefID, m_WflInstanceID, m_WflStpInsID, l_WflStepID, m_CurrUserId);
				/*【STP4】---执行退回指定步骤动作*/
				tzWflAction.BackToChonseStep(m_Canlendar, s_StepID);
			}else {
				backOk = false;
				throw new TzException("当前业务流程步骤没有启用退回某步功能，您不能执行退回操作");
			}
		}catch (TzException e) {
			backOk = false;
			throw e;
		}catch (Exception e) {
			backOk = false;
		}
		
		return backOk;
	}
	
	

	
	/**
	 * 首步骤撤销
	 * 当业务流程处于首步骤时，可以直接撤销，相当于删除或者关闭业务流程，直接将业务流程终止、关闭
	 * @return
	 * @throws TzException
	 */
	public boolean WorkflowFirstStepCancel() throws Exception{
		boolean cancelOK = false;
		try {
			/*【STP1】---检查当前步骤实例状态*/
			m_WorkflowStepInstance.checkWflStpInstanceStatus();
			
			/*【STP2】---检查当前步骤操作权限*/
			String l_WflStepID = m_WorkflowStepInstance.getM_WflStepID();
			
			if(m_WorkflowStepInstance.checkOperatingAuthorization(m_WflInstanceID, l_WflStepID) == false){
				throw new TzException("当前业务流程前续步骤有未处理的任务，请您耐心等待处理完成后执行撤销操作");
			}
			
			//获取当前步骤类型
			int l_StpType = m_WorkflowStepInstance.getM_WflStepCfgRecord().getTzInt("tzms_stptype").getValue();
			
			if(l_StpType == 1){
				if(this.checkOperateAuth(l_WflStepID, "CAN") == true) {
					//执行当前步骤实例
					String l_StpID = m_WorkflowStepInstance.getM_WflStepCfgRecord().getTzString("tzms_wflstp_tid").getValue();
					//杀死当前步骤的所有后续步骤存活实例
					m_WorkflowStepInstance.killAllAliveStpInsAfterStpID(m_WflInstanceID, l_StpID);
					
					
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
					stpIns_TempRecord.setColumnValue("tzms_action_type", "撤销");
					stpIns_TempRecord.setColumnValue("tzms_stpinsstat", "5");
					//任务处理时间
					stpIns_TempRecord.setColumnValue("tzms_tskprodt", new Date());
					//结束时间
					stpIns_TempRecord.setColumnValue("tzms_stpenddt", new Date());
					
					if(stpIns_TempRecord.update() == true) {				
						//设置业务流程实例状态为“撤销”
						sqlQuery.update("update tzms_wflins_tbl set tzms_wflstatus='5',tzms_wfenddt=? where tzms_wflinsid=?", 
								new Object[]{ new Date(), m_WflInstanceID });
						
						cancelOK = true;
					}else {
						throw new TzException("撤销失败");
					}
				}else{
					throw new TzException("当前业务流程步骤没有启用撤销功能，您不能执行撤销操作");
				}
			}else{
				throw new TzException("【系统配置错误】只有业务流程起始步骤才能执行撤销操作");
			}
		}catch (TzException e) {
			logger.error("首步骤撤销错误，工作流实例ID："+ m_WflInstanceID +"，步骤实例ID："+ m_WflStpInsID +"，异常信息：", e);
			throw e;
		}catch (Exception e) {
			logger.error("首步骤撤销错误，工作流实例ID："+ m_WflInstanceID +"，步骤实例ID："+ m_WflStpInsID +"，异常信息：", e);
			throw new TzException("撤销失败！", e);
		}

		return cancelOK;
	}
	
	
	/**
	 * 撤回
	 * @return
	 * @throws TzException
	 */
	public boolean ExecuteWithdraw() throws TzException {
		boolean withdrawOk = true;
		
		try {
			/*【STP1】---检查当前步骤实例状态，不检查*/
			//m_WorkflowStepInstance.checkWflStpInstanceStatus();
			
			/*【STP2】---检查当前步骤操作权限*/
			String l_WflStepID = m_WorkflowStepInstance.getM_WflStepID();
			
			if(m_WorkflowStepInstance.checkOperatingAuthorization(m_WflInstanceID, l_WflStepID) == false){
				withdrawOk = false;
				throw new TzException("当前业务流程前续步骤有未处理的任务，请您耐心等待处理完成后执行撤回操作");
			}
			
			if(this.checkOperateAuth(l_WflStepID, "WIT") == true) {
				/*【STP3】---创建动作实例*/
				TzWflAction tzWflAction = new TzWflAction(m_BusProcessDefID, m_WflInstanceID, m_WflStpInsID, l_WflStepID, m_CurrUserId);
				tzWflAction.setNotSendNotice(true);		//撤回不发送通知
				
				/*【STP4】---执行退回指定步骤动作*/
				withdrawOk = tzWflAction.ExecuteWithdraw(m_Canlendar);
				
				if(withdrawOk == true) {
					TzWflEvent stpWflEvent = m_WorkflowStepInstance.getM_WflStepEventInterface();
					//触发步骤撤回事件
					stpWflEvent.ExecuteEventActions("2", "206", l_WflStepID);
					
					//触发工作流撤回事件
					m_WflEventInterface.ExecuteEventActions("1", "106", m_BusProcessDefID);
				}else {
					throw new TzException("撤回失败");
				}
			}else {
				withdrawOk = false;
				throw new TzException("当前业务流程步骤没有启用撤回功能，您不能执行撤回操作");
			}
		}catch (TzException e) {
			withdrawOk = false;
			logger.error("工作流撤回错误，工作流实例ID："+ m_WflInstanceID +"，步骤实例ID："+ m_WflStpInsID +"，异常信息：", e);
			throw e;
		}catch (Exception e) {
			withdrawOk = false;
			logger.error("工作流撤回错误，工作流实例ID："+ m_WflInstanceID +"，步骤实例ID："+ m_WflStpInsID +"，异常信息：", e);
			throw new TzException("撤回失败！", e);
		}
		
		return withdrawOk;
	}
	
	
	
	/**
	 * 	业务流程激活
	 * @return
	 * @throws TzException
	 */
	public boolean WorkflowActivate() throws TzException {
		boolean activeOK = false;
		//激活步骤实例
		m_WorkflowStepInstance.WflStepActivate();
		
		//激活当前业务流程实例
		String l_WflStatua = m_WorkflowInsRecord.getTzString("tzms_wflstatus").getValue();
		
		/*0-新建、1-激活、2-处理中、3-结束、4-提前终止、5-已撤销*/
		if("3".equals(l_WflStatua) 
				|| "4".equals(l_WflStatua) 
				|| "5".equals(l_WflStatua)){
			try{
				//设置动作摘要为-激活任务
				m_WorkflowInsRecord.setColumnValue("tzms_wflstatus", "1");
				m_WorkflowInsRecord.setColumnValue("tzms_wfenddt", null);
				
				if(m_WorkflowInsRecord.update() == true) {
					activeOK = true;
				}else {
					throw new TzException("业务流程实例激活失败");
				}
			}catch(TzException e) {
				logger.error("业务流程激活错误，工作流实例ID："+ m_WflInstanceID +"，步骤实例ID："+m_WflStpInsID+"，异常信息：", e);
				throw e;
			}catch(Exception e){
				logger.error("业务流程激活错误，工作流实例ID："+ m_WflInstanceID +"，步骤实例ID："+m_WflStpInsID+"，异常信息：", e);
				throw new TzException("业务流程实例激活失败",e);
			}
		}else {
			//已经是存活状态
			activeOK = true;
		}
		 
		return activeOK;
	}
	
	
	
	/**
	 * 	关闭工作流步骤实例并触发事件
	 * @return
	 * @throws TzException
	 */
	public boolean CloseWflStpInstance() throws TzException {
		boolean closeOK = false;
		
		try {
			//提前结束步骤实例
			m_WorkflowStepInstance.WflStepTerminate("5");
			//设置动作摘要
			m_WorkflowStepInstance.setActAbstract("终止任务");
			
			//查询是否还有存活的非知会任务，如果没有，更新流程状态为提前终止
			int count = sqlQuery.queryForObject("select count(*) from tzms_stpins_tbl where tzms_wflinsid=? and tzms_stpinsstat in('1','3','6','8') and tzms_asgmethod<>'1'", 
					new Object[] { m_WflInstanceID }, "int");
			if(count == 0) {
				//激活当前业务流程实例
				String l_WflStatua = m_WorkflowInsRecord.getTzString("tzms_wflstatus").getValue();
				
				/*0-新建、1-激活、2-处理中、3-结束、4-提前终止、5-已撤销*/
				if("0".equals(l_WflStatua) 
						|| "1".equals(l_WflStatua) 
						|| "2".equals(l_WflStatua)){
					try{
						m_WorkflowInsRecord.setColumnValue("tzms_wflstatus", "4");
						m_WorkflowInsRecord.setColumnValue("tzms_wfenddt", new Date());
						
						if(m_WorkflowInsRecord.update() != true) {
							throw new TzException("业务流程实例终止失败");
						}
					}catch(TzException e) {
						logger.error("业务流程终止错误，工作流实例ID："+ m_WflInstanceID +"，异常信息：", e);
						throw e;
					}catch(Exception e){
						logger.error("业务流程终止错误，工作流实例ID："+ m_WflInstanceID +"，异常信息：", e);
						throw new TzException("业务流程实例终止失败");
					}
				}
			}
			
			closeOK = true;
		}catch (TzException e) {
			logger.error("业务流程终止错误，工作流实例ID："+ m_WflInstanceID +"，步骤实例ID："+m_WflStpInsID+"，异常信息：", e);
			throw e;
		}catch(Exception e){
			logger.error("业务流程终止错误，工作流实例ID："+ m_WflInstanceID +"，步骤实例ID："+m_WflStpInsID+"，异常信息：", e);
			throw new TzException("业务流程任务终止失败",e);
		}
		
		return closeOK;
	}
	
	
	/**
	 * 当前业务流程终止
	 * 终止所有存活步骤实例、终止业务流程实例
	 * @param s_WflInsID	业务流程实例ID
	 * @return
	 */
	public boolean WorkflowTerminate(String s_WflInsID){
		try {
			//终止所有存活步骤实例
			sqlQuery.update("update tzms_stpins_tbl set tzms_stpinsstat='4',tzms_action_type=?,tzms_stpenddt=? where tzms_wflinsid=? and tzms_stpinsstat in('1','3','6','8')", 
					new Object[]{ "终止任务", new Date(), s_WflInsID });
			
			//终止业务流程实例
			sqlQuery.update("update tzms_wflins_tbl set tzms_wflstatus='3',tzms_wfenddt=? where tzms_wflinsid=?", 
					new Object[]{ new Date(), s_WflInsID });
		}catch (Exception e) {
			logger.error("业务流程终止失败，业务流程实例ID:"+ s_WflInsID +"，异常信息：", e);
			return false;
		}
		return true;
	}
}
