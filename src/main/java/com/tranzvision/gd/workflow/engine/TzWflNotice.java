package com.tranzvision.gd.workflow.engine;

import com.tranzvision.gd.TZBusProNotice.service.impl.EmailFunServiceImpl;
import com.tranzvision.gd.TZBusProNotice.service.impl.NoticeFunctionServiceImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.CreateTaskServiceImpl;
import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.dynamicsBase.AnalysisDynaRole;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.workflow.base.TzWflObject;
import com.tranzvision.gd.workflow.base.TzWorkflowFunc;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

public class TzWflNotice {
	
	private SqlQuery sqlQuery;
	private TzWflObject tzWflObject;
	private GetSeqNum getSeqNum;
	
	//工作流实例编号
	private String m_WflInstanceID;
	//步骤实例编号
	private String m_WflStpInsID;
	//步骤编号
	private String m_WflStpID;
	//是否抄送任务， 抄送任务只发送邮件通知
	private boolean isCopyRw = false;
	//是否为催办通知，如果是催办需要发送邮件通知
	private boolean isHastenWork = false;
	
	//记录日志
	private static final Logger logger = Logger.getLogger("WorkflowEngine");
	
	
	/**
	 * 设置是否抄送任务
	 * @param isCopyRw
	 */
	public void setCopyRw(boolean isCopyRw) {
		this.isCopyRw = isCopyRw;
	}
	/**
	 * 设置是否为催办通知
	 * @param isHastenWork
	 */
	public void setHastenWork(boolean isHastenWork) {
		this.isHastenWork = isHastenWork;
	}


	/**
	 * 构造方法
	 * @param m_BusProcessDefID
	 * @param m_WflInstanceID
	 * @param m_WflStpInsID
	 * @param m_WflStpID
	 */
	public TzWflNotice(String WflInstanceID, String WflStpInsID, String WflStpID) {
		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		sqlQuery = (SqlQuery) getSpringBeanUtil.getAutowiredSpringBean("SqlQuery");
		tzWflObject = (TzWflObject) getSpringBeanUtil.getAutowiredSpringBean("TzWflObject");
		getSeqNum = (GetSeqNum) getSpringBeanUtil.getAutowiredSpringBean("GetSeqNum");
		
		this.m_WflInstanceID = WflInstanceID;
		this.m_WflStpInsID = WflStpInsID;
		this.m_WflStpID = WflStpID;
	}




	/**
	 * 发送业务流程通知
	 * @param StepId
	 * @param StepInsId
	 * @param userList
	 */
	public void SendWorkflowNotice(List<String> userList){
		
		if(userList != null && userList.size() > 0) {
			//查询步骤配置信息
			Map<String,Object> stpCfgMap = sqlQuery.queryForMap("select tzms_email_rem_flg,tzms_wechat_rem_flg,tzms_outlook_rem_flg,tzms_dpyemail_aflg from tzms_wflstp_t where tzms_wflstp_tid=?", 
					new Object[]{ m_WflStpID });	
			
			if(stpCfgMap != null){
				//启用邮件通知
				boolean emailFlag = stpCfgMap.get("tzms_email_rem_flg") == null ? false : (boolean) stpCfgMap.get("tzms_email_rem_flg");
				//启用微信通知
				boolean wechatFlag = stpCfgMap.get("tzms_wechat_rem_flg") == null ? false : (boolean) stpCfgMap.get("tzms_wechat_rem_flg");
				//启用outlook通知
				boolean outlookFlag = stpCfgMap.get("tzms_outlook_rem_flg") == null ? false : (boolean) stpCfgMap.get("tzms_outlook_rem_flg");
				//启用邮件审批
				boolean emailApprovalFlag = stpCfgMap.get("tzms_dpyemail_aflg") == null ? false : (boolean) stpCfgMap.get("tzms_dpyemail_aflg");
				
				//发送通知接口
				NoticeFunctionServiceImpl noticeFunction = tzWflObject.getNoticeFunction();
				
				
				logger.info("=====>>> 发送流程通知开始：流程实例ID："+ m_WflInstanceID +"，步骤实例ID："+ m_WflStpInsID +"，发送用户ID：" + userList.toString());
				//是否发送了邮件审批通知，如果发送了则不发送邮件通知邮件
				boolean isSendSpEmail = false;
				
				if(isCopyRw == false) {	//抄送任务不发送
					if(wechatFlag == true){
						try {
							//发送微信通知
							noticeFunction.sendWeChat(m_WflInstanceID, m_WflStpInsID, userList);
						}catch (Exception e) {
							//e.printStackTrace();
							logger.error("发送微信通知异常，步骤实例编号："+ m_WflStpInsID +"，异常信息：", e);
						}
					}
					
					if(outlookFlag == true){
						try {
							//发送任务提醒	
							noticeFunction.sentOutlook(m_WflInstanceID, m_WflStpInsID, userList);
						}catch (Exception e) {
							//e.printStackTrace();
							logger.error("发送任务提醒异常，步骤实例编号："+ m_WflStpInsID +"，异常信息：", e);
						}
					}
					
					if(emailApprovalFlag == true || isHastenWork == true){
						try {
							//发送工作流审批邮件
							EmailFunServiceImpl emailFun = tzWflObject.getEmailSpFun();
							emailFun.sendEmail(m_WflInstanceID, m_WflStpInsID, userList);
							
							isSendSpEmail = true;
						}catch (Exception e) {
							//e.printStackTrace();
							logger.error("发送工作流审批邮件异常，步骤实例编号："+ m_WflStpInsID +"，异常信息：", e);
						}
					}
				}else {
					//抄送任务，只要启用邮件审批通知，也发送邮件通知
					if(emailApprovalFlag == true) {
						emailFlag = true;
					}
				}
				
				//邮件通知
				if(emailFlag == true && isSendSpEmail == false){
					try {
						//发送邮件通知
						noticeFunction.sendEmail(m_WflInstanceID, m_WflStpInsID, userList);
					}catch (Exception e) {
						//e.printStackTrace();
						logger.error("发送邮件通知异常，步骤实例编号："+ m_WflStpInsID +"，异常信息：", e);
					}
				}
			}
		}
	}
	
	
	
	
	/**
	 * 步骤提交给指定角色发送邮件
	 * @param roleId		角色ID
	 * @param emailTmp		邮件模板ID
	 * @param currUserId	当前责任人
	 */
	public void stepSubmitSendEmailToRole(String roleId, String emailTmp, String currUserId) {
		logger.info("=====>>> 给指定角色发送邮件开始：流程实例ID："+ m_WflInstanceID +"，步骤实例ID："+ m_WflStpInsID);
		logger.info("=====>>> 角色ID： " + roleId);
		logger.info("=====>>> 邮件模板ID： " + emailTmp);
		
		try {
			AnalysisDynaRole analysisRole = new AnalysisDynaRole();
			List<String> userList = analysisRole.getUserIds(m_WflInstanceID, m_WflStpInsID, currUserId, roleId);

			if(userList != null && userList.size() > 0) {
				int sendCount = 0;
				CreateTaskServiceImpl createTaskServiceImpl = tzWflObject.getCreateTaskServiceImpl();
				//1、创建邮件发送任务
				String taskId = createTaskServiceImpl.createTaskIns("SAIF", emailTmp, "MAL", "A");
				//2、创建发送听众
				String audienceId = createTaskServiceImpl.createAudience(taskId, "SAIF", "业务流程通知", "JSRW");
				
				if(StringUtils.isNotBlank(taskId) && StringUtils.isNotBlank(audienceId)) {
					//获取听众成员ID
					int audCyId = getSeqNum.getSeqNum("TZ_AUDCYUAN_T", "TZ_AUDCY_ID", userList.size(), 0);
					
					//查询任务号，并更新邮件主题
					String wflTaskId = sqlQuery.queryForObject("select tzms_taskid from tzms_wflins_tbl where tzms_wflinsid=?", 
							new Object[] { m_WflInstanceID }, "String");
					if(StringUtils.isNotBlank(wflTaskId)) {
						String emailSub = "任务号：" + wflTaskId + " " + createTaskServiceImpl.getEmailSendTitle(taskId);
						createTaskServiceImpl.updateEmailSendTitle(taskId, emailSub);
					}
					
					//3、添加听众成员
					for(String userId : userList) {
						String oprid = "";
						String strEmail = "";
						String name = "";
						
						TzWorkflowFunc wflFunc = new TzWorkflowFunc();
						Map<String,String> contachMap = wflFunc.getTeaContactInfoByUserId(userId);
						if(contachMap != null) {
							oprid = contachMap.get("oprid");
							name = contachMap.get("name");
							strEmail = contachMap.get("email");
						}
						
						if(StringUtils.isNotBlank(strEmail)) {
							// 为听众添加听众成员;
							boolean addAudCy = createTaskServiceImpl.addAudCy2(audienceId, name, name, "", "", strEmail, "", "", oprid, m_WflInstanceID, m_WflStpInsID, userId, audCyId);
							if(addAudCy == true) {
								sendCount ++;
								audCyId --;
							}
						}
					}
					
					if(sendCount > 0) {
						//批处理发送
						sqlQuery.update("insert into TZ_SMSEML_TASK_TBL (TZ_EML_SMS_TASK_ID) values(?)", new Object[] { taskId });
					}
				}else {
					logger.error("给指定角色发送邮件失败，步骤实例ID：" + m_WflStpInsID + "，创建任务或听众失败");
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("给指定角色发送邮件失败，步骤实例ID：" + m_WflStpInsID, e);
		}
	}
}
