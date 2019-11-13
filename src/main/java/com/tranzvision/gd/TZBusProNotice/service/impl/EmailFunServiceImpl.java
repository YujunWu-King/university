package com.tranzvision.gd.TZBusProNotice.service.impl;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.CreateTaskServiceImpl;
import com.tranzvision.gd.util.base.TzException;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.workflow.base.TzWorkflowFunc;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 
 *ClassName: EmailFunServiceImpl
 * @author 吴玉军
 * @version 1.0
 * Create Time: 2019年1月30日 下午4:05:25 
 * Description: 邮件审批 发送邮件+通过系统变量生成url功能
 */
@Service("com.tranzvision.gd.TZBusProNotice.service.impl.EmailFunServiceImpl")
public class EmailFunServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private GetHardCodePoint getHardCodePoint;
	@Autowired
	private CreateTaskServiceImpl createTaskServiceImpl;
	@Autowired
	private GetSeqNum getSeqNum;

	
	
	
	/**
	 * 
	* Description:邮件审批---发送邮件
	* Create Time: 2019年1月29日 下午4:00:31
	* @author 吴玉军
	* @author 张浪，2019-06-28改写
	* @param wflInsId	流程实例ID
	* @param stpInsId	步骤实例ID
	* @param userId		发送人员ID列表
	* @return
	 * @throws TzException
	 */
	public void sendEmail(String wflInsId, String stpInsId, List<String> userIds) throws TzException {
		try {
			//邮件审批模板
			String tmpId = sqlQuery.queryForObject("select B.tzms_emailap_tmpid from tzms_stpins_tbl A LEFT JOIN tzms_wflstp_tBase B on A.tzms_wflstpid=B.tzms_wflstp_tid where A.tzms_wflinsid=? and A.tzms_stpinsid=? ", 
					new Object[] { wflInsId, stpInsId }, "String");
			if(StringUtils.isBlank(tmpId)) {
				//使用默认邮件审批模板
				tmpId = getHardCodePoint.getHardCodePointVal("TZ_WFL_EML_SP_TPL");
				
				if(StringUtils.isBlank(tmpId)) {
					throw new TzException("没有找到对应的邮件审批模板");
				}
			}
			
			// 1、创建邮件发送任务;
			String taskId = createTaskServiceImpl.createTaskIns("SAIF", tmpId, "MAL", "A");
			if (StringUtils.isBlank(taskId)) {
				throw new TzException("创建邮件发送任务失败");
			}
			
			// 2、创建短信、邮件发送的听众(audId听众ID);
			String createAudience = createTaskServiceImpl.createAudience(taskId, "SAIF", "高金业务流程审批邮件", "JSRW");
			if(StringUtils.isBlank(createAudience)) {
				throw new TzException("创建邮件发送的听众失败");
			}
			
			//3、查询任务号，并更新邮件主题
			String wflTaskId = sqlQuery.queryForObject("select tzms_taskid from tzms_wflins_tbl where tzms_wflinsid=?", 
					new Object[] { wflInsId }, "String");
			if(StringUtils.isNotBlank(wflTaskId)) {
				String emailSub = "任务号：" + wflTaskId + " " + createTaskServiceImpl.getEmailSendTitle(taskId);
				createTaskServiceImpl.updateEmailSendTitle(taskId, emailSub);
			}
			
			//4、添加听众成员
			if(userIds != null && userIds.size() > 0) {
				//获取听众成员ID
				int audCyId = getSeqNum.getSeqNum("TZ_AUDCYUAN_T", "TZ_AUDCY_ID", userIds.size(), 0);
				//添加听众
				for (int i = 0; i < userIds.size(); i++) {
					String userId = userIds.get(i);
					
					String strEmail  = "";
					String name = "";
					String oprid = "";
					
					TzWorkflowFunc wflFunc = new TzWorkflowFunc();
					Map<String,String> contachMap = wflFunc.getTeaContactInfoByUserId(userId);
					if(contachMap != null) {
						oprid = contachMap.get("oprid");
						name = contachMap.get("name");
						strEmail = contachMap.get("email");
					}
					
					if (strEmail == null || "".equals(strEmail)) {
						continue; 
					}
	
					// 为听众添加听众成员;
					boolean addAudCy = createTaskServiceImpl.addAudCy2(createAudience, name, name, "", "", strEmail, "", "",oprid, wflInsId, stpInsId, userId, audCyId);
					if(addAudCy == true) {
						audCyId --;
						/*
						//获取听众成员ID
						String audCyId = sqlQuery.queryForObject("SELECT TZ_AUDCY_ID from ps_tz_audcyuan_t where TZ_AUDIENCE_ID=? and OPRID=?", 
								new Object[] {createAudience,oprid}, "String");
						//新增几个操作链接 1:登陆Java系统 2:打开处理表单 3：在邮件中直接审批
						SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
						String typeId = String.valueOf(Math.random() * 1000000 + 1) + format.format(new Date());
						typeId = DESUtil.encrypt(typeId, "TZGD_Tranzvision");
						
						String TZ_XH_ID = String.valueOf(getSeqNum.getSeqNum("TZ_APPROVE_FUN_T", "TZ_XH_ID"));
			
						// 业务流程--关联业务实体
						String entityName = sqlQuery.queryForObject("select B.tzms_entity_name from tzms_wflins_tbl A LEFT JOIN tzms_wfcldn_tBase B ON A.tzms_wfcldn_uniqueid = B.tzms_wfcldn_tid where A.tzms_wflinsid=? ",  new Object[] {wflInsId}, "String");
						// 业务流程实例--业务数据ID
						String entityId = sqlQuery.queryForObject("select tzms_wflrecord_uniqueid from tzms_wflins_tbl where tzms_wflinsid=?", new Object[] {wflInsId}, "String");
						// 业务流程步骤 ID
						String tzms_wflstp_tid = sqlQuery.queryForObject("select tzms_wflstpid from tzms_stpins_tbl where tzms_wflinsid=? and tzms_stpinsid=?", new Object[] {wflInsId,stpInsId},"String");
						// 流程实例 ID
						String tzms_wflinsid = wflInsId;
						// 步骤实例ID
						String tzms_stpinsid = stpInsId;
						// 处理意见
						String suggest_in = sqlQuery.queryForObject("select tzms_tskprorec from tzms_stpins_tbl where tzms_wflinsid=? and tzms_stpinsid=?", new Object[] {wflInsId,stpInsId},"String");
	
						
						String sql = "insert into TZ_APPROVE_FUN_T VALUES(?,?,?,?,?,?,?,?,?,?,?)";
						sqlQuery.update(sql, new Object[] { TZ_XH_ID, typeId, oprid, entityName, entityId, tzms_wflstp_tid, tzms_wflinsid, tzms_stpinsid, suggest_in, createAudience, audCyId });
						*/					
					}
				}
				//发送邮件任务
				//sendSmsOrMalServiceImpl.send(taskId, "");
				//改用批处理发送
				sqlQuery.update("insert into TZ_SMSEML_TASK_TBL (TZ_EML_SMS_TASK_ID) values(?)", new Object[] { taskId });
			}
		} catch (TzException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new TzException("发送审批邮件异常", e);
		}
	}	
}
