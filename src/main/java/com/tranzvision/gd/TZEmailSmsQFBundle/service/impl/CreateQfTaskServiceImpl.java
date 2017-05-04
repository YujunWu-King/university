package com.tranzvision.gd.TZEmailSmsQFBundle.service.impl;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.dao.PsTzAudcyuanTMapper;
import com.tranzvision.gd.TZEmailSmsSendBundle.dao.PsTzAudienceTMapper;
import com.tranzvision.gd.TZEmailSmsSendBundle.dao.PsTzDxyjfsrwTblMapper;
import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzAudcyuanT;
import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzAudienceT;
import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzDxyjfsrwTbl;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 原： TZ_SMSMAL_QF_PKG:createTask()
 * 
 * @author Administrator
 *
 */
@Service("com.tranzvision.gd.TZEmailSmsQFBundle.service.impl.CreateQfTaskServiceImpl")
public class CreateQfTaskServiceImpl {
	// 发送任务ID;
	// private String taskId;
	// 听众ID；
	// private String audId;
	// 听众ID；
	// private String jgId;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private PsTzDxyjfsrwTblMapper psTzDxyjfsrwTblMapper;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private PsTzAudienceTMapper psTzAudienceTMapper;
	@Autowired
	private PsTzAudcyuanTMapper psTzAudcyuanTMapper;
	@Autowired
	private mailContentHandlerServiceImpl mailContentHandlerServiceImpl;

	// 创建发送任务***参数：strJgId：机构ID；strTmpId:邮件或短信模板ID；taskType：发送类型，邮件MAL,短信SMS；sendtype:使用的发送手机或邮箱,A主要，B次要，C所有;
	@SuppressWarnings("deprecation")
	public String createTaskIns(String strJgId, String strPicId, String taskType, String sendtype) {
		String taskId = "";
		try {
			if (strJgId == null || "".equals(strJgId) || strPicId == null || "".equals(strPicId) || taskType == null
					|| "".equals(taskType) || sendtype == null || "".equals(sendtype)) {
				return "";
			}

			taskId = String.valueOf(getSeqNum.getSeqNum("TZ_DXYJFSRW_TBL", "TZ_EML_SMS_TASK_ID"));
			PsTzDxyjfsrwTbl psTzDxyjfsrwTbl = new PsTzDxyjfsrwTbl();
			psTzDxyjfsrwTbl.setTzEmlSmsTaskId(taskId);
			psTzDxyjfsrwTbl.setTzTaskLx(taskType);
			psTzDxyjfsrwTbl.setTzJgId(strJgId);
			
			psTzDxyjfsrwTbl.setTzBatchBz("Y");
			
			String mailContent = "";
			String dynimicFlg = "", webmalFlg = "Y", emlServId = "", TZ_EML_IF_PRT = "";
			String mailSender = "", sendModel = "", strTmpId = "";
			 
			Map<String, Object> map1 = jdbcTemplate.queryForMap("SELECT TZ_EMAIL_SENDER,TZ_SEND_MODEL,TZ_TMPL_ID FROM PS_TZ_DXYJQF_DY_T WHERE TZ_MLSM_QFPC_ID=?",new Object[]{strPicId});
			if(map1 != null){
				mailSender = (String)map1.get("TZ_EMAIL_SENDER");
				sendModel = (String)map1.get("TZ_SEND_MODEL");
				strTmpId = (String)map1.get("TZ_TMPL_ID");
			}
			psTzDxyjfsrwTbl.setTzMlsmQfpcId(strPicId);
			psTzDxyjfsrwTbl.setTzTmplId(strTmpId);
			if ("MAL".equals(taskType)) {
				psTzDxyjfsrwTbl.setTzSyyxLx(sendtype);
				// 如果邮件群发设置了邮件模板，发件箱为模板中配置的邮箱，如果没有配置模板，则发件箱为当前登录人对应SETID对应配置的邮箱;
				if(strTmpId != null && !"".equals(strTmpId)){
					Map<String, Object> tmpMap= jdbcTemplate.queryForMap("select a.TZ_DYNAMIC_FLAG, a.TZ_WEBMAL_FLAG , a.TZ_EMLSERV_ID, a.TZ_EML_IF_PRT from PS_TZ_EMALTMPL_TBL a, PS_TZ_TMP_DEFN_TBL b where a.TZ_YMB_ID=b.TZ_YMB_ID and a.TZ_JG_ID=? and a.TZ_TMPL_ID=?",new Object[]{strJgId, strTmpId});
					if(tmpMap != null && !"".equals(strTmpId)){
						dynimicFlg = (String)tmpMap.get("TZ_DYNAMIC_FLAG");
						webmalFlg = (String)tmpMap.get("TZ_WEBMAL_FLAG");
						emlServId = (String)tmpMap.get("TZ_EMLSERV_ID"); 
						TZ_EML_IF_PRT = (String)tmpMap.get("TZ_EML_IF_PRT");
					}
					psTzDxyjfsrwTbl.setTzDynamicFlag(dynimicFlg);
					psTzDxyjfsrwTbl.setTzEmlIfPrt(TZ_EML_IF_PRT);
				}
				psTzDxyjfsrwTbl.setTzWebmalFlag(webmalFlg);
				
				/*发件人为系统配置的邮件服务和当前登录人邮箱*/
				if(mailSender.contains("@")){
					psTzDxyjfsrwTbl.setTzEmailSender(mailSender);
					if(emlServId == null || "".equals(emlServId)){
						emlServId = jdbcTemplate.queryForObject("SELECT TZ_EMLSERV_ID FROM PS_TZ_EMLS_DEF_TBL WHERE TZ_JG_ID=? and TZ_IS_DEFAULT='Y' limit 0,1", new Object[]{strJgId},"String");
					}
					psTzDxyjfsrwTbl.setTzEmlservId(emlServId);
				}else{
					psTzDxyjfsrwTbl.setTzEmlservId(mailSender);
					String emailSenderAddr = jdbcTemplate.queryForObject("select TZ_EML_ADDR100 from PS_TZ_EMLS_DEF_TBL WHERE TZ_EMLSERV_ID=? limit 0,1", new Object[]{mailSender},"String");
					psTzDxyjfsrwTbl.setTzEmailSender(emailSenderAddr);
				}
				
				if("EXC".equals(sendModel)){
					psTzDxyjfsrwTbl.setTzDynamicFlag("N");
					psTzDxyjfsrwTbl.setTzEmlIfPrt("N");
				}

		        mailContent = mailContentHandlerServiceImpl.emailConPreprocess(strPicId, taskId);
		        if(mailContent == null){
		        	mailContent = "";
		        }
		        String serverHost = request.getScheme() + "://" + request.getServerName() + ":"
						+ request.getServerPort();
		        mailContent = mailContent.replace("<img src=\"/", "<img src=\"" + serverHost + "/");
				
		        psTzDxyjfsrwTbl.setTzMalContent(mailContent);
			}else{
				if("SMS".equals(taskType)){
					psTzDxyjfsrwTbl.setTzSysjLx(sendtype);
					// 如果短信群发设置了短信模板，短信发送服务器为模板中配置的服务器，如果没有配置模板，则短信服务器为当前登录人对应SETID对应配置的服务器;
					if(strTmpId != null && !"".equals(strTmpId)){
						String smsServId = "";
			            Map<String, Object> smsMap = jdbcTemplate.queryForMap("select a.TZ_DYNAMIC_FLAG , a.TZ_SMS_SERV_ID from PS_TZ_SMSTMPL_TBL a, PS_TZ_TMP_DEFN_TBL b where a.TZ_YMB_ID=b.TZ_YMB_ID and a.TZ_JG_ID=? and a.TZ_TMPL_ID=?", new Object[]{strJgId,strTmpId});
			            if(smsMap != null){
			            	dynimicFlg = (String)smsMap.get("TZ_DYNAMIC_FLAG");
			            	smsServId = (String)smsMap.get("TZ_SMS_SERV_ID");
			            }
			            psTzDxyjfsrwTbl.setTzSmsServId(smsServId);
			            psTzDxyjfsrwTbl.setTzDynamicFlag(dynimicFlg);
					}
					
					/*导入Excel发送模式都包含动态个性化内容,需要判重*/
					if("EXC".equals(sendModel)){
						psTzDxyjfsrwTbl.setTzDynamicFlag("N");
						psTzDxyjfsrwTbl.setTzEmlIfPrt("N");
					}
					
				}else{
					if("ZNX".equals(taskType)){
						psTzDxyjfsrwTbl.setTzSysjLx(sendtype);
						if(strTmpId != null && !"".equals(strTmpId)){
							String smsServId = "";
				            Map<String, Object> smsMap = jdbcTemplate.queryForMap("select a.TZ_DYNAMIC_FLAG from PS_TZ_ZNXTMPL_TBL a, PS_TZ_TMP_DEFN_TBL b where a.TZ_YMB_ID=b.TZ_YMB_ID and a.TZ_JG_ID=? and a.TZ_TMPL_ID=?", new Object[]{strJgId,strTmpId});
				            if(smsMap != null){
				            	dynimicFlg = (String)smsMap.get("TZ_DYNAMIC_FLAG");
				            }
				            psTzDxyjfsrwTbl.setTzDynamicFlag(dynimicFlg);
						}
					}
				}
			}
			
			
			psTzDxyjfsrwTbl.setTzRwtjDt(new Date());
			psTzDxyjfsrwTbl.setTzRwksDt(new Date(1900, 1, 1));
			psTzDxyjfsrwTbl.setTzRwjsDt(new Date(2900, 12, 31));
			psTzDxyjfsrwTbl.setTzRwzxZt("A");
			psTzDxyjfsrwTbl.setRowAddedDttm(new Date());
			psTzDxyjfsrwTbl.setRowAddedOprid(tzLoginServiceImpl.getLoginedManagerOprid(request));
			int i = psTzDxyjfsrwTblMapper.insert(psTzDxyjfsrwTbl);
			if (i > 0) {
				return taskId;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

		return "";
	}
	
	public boolean updateTaskIns(String strPicId,String strTaskId){
		boolean bl = false;
		PsTzDxyjfsrwTbl psTzDxyjfsrwTbl = psTzDxyjfsrwTblMapper.selectByPrimaryKey(strTaskId);
		if(psTzDxyjfsrwTbl != null){
			String taskType = psTzDxyjfsrwTbl.getTzTaskLx();
			String strJgId = psTzDxyjfsrwTbl.getTzJgId();
		      
			String mailContent = "";
			String dynimicFlg = "", webmalFlg = "Y", emlServId = "", TZ_EML_IF_PRT = "";
			
			String mailSender = "", sendModel = "", strTmpId = "";
			
			Map<String, Object> map1 = jdbcTemplate.queryForMap("SELECT TZ_EMAIL_SENDER,TZ_SEND_MODEL,TZ_TMPL_ID FROM PS_TZ_DXYJQF_DY_T WHERE TZ_MLSM_QFPC_ID=?",new Object[]{strPicId});
			if(map1 != null){
				mailSender = (String)map1.get("TZ_EMAIL_SENDER");
				sendModel = (String)map1.get("TZ_SEND_MODEL");
				strTmpId = (String)map1.get("TZ_TMPL_ID");
			}
			psTzDxyjfsrwTbl.setTzMlsmQfpcId(strPicId);
			psTzDxyjfsrwTbl.setTzTmplId(strTmpId);
			if ("MAL".equals(taskType)) {
				// 如果邮件群发设置了邮件模板，发件箱为模板中配置的邮箱，如果没有配置模板，则发件箱为当前登录人对应SETID对应配置的邮箱;
				if(strTmpId != null && !"".equals(strTmpId)){
					Map<String, Object> tmpMap= jdbcTemplate.queryForMap("select a.TZ_DYNAMIC_FLAG, a.TZ_WEBMAL_FLAG , a.TZ_EMLSERV_ID, a.TZ_EML_IF_PRT from PS_TZ_EMALTMPL_TBL a, PS_TZ_TMP_DEFN_TBL b where a.TZ_YMB_ID=b.TZ_YMB_ID and a.TZ_JG_ID=? and a.TZ_TMPL_ID=?",new Object[]{strJgId, strTmpId});
					if(tmpMap != null && !"".equals(tmpMap)){
						dynimicFlg = (String)map1.get("TZ_DYNAMIC_FLAG");
						webmalFlg = (String)map1.get("TZ_WEBMAL_FLAG");
						emlServId = (String)map1.get("TZ_EMLSERV_ID"); 
						TZ_EML_IF_PRT = (String)map1.get("TZ_EML_IF_PRT");
					}
					
					psTzDxyjfsrwTbl.setTzDynamicFlag(dynimicFlg);
					psTzDxyjfsrwTbl.setTzEmlIfPrt(TZ_EML_IF_PRT);
				}
				psTzDxyjfsrwTbl.setTzWebmalFlag(webmalFlg);
				
				/*发件人为系统配置的邮件服务和当前登录人邮箱*/
				if(mailSender.contains("@")){
					psTzDxyjfsrwTbl.setTzEmailSender(mailSender);
					if(emlServId == null || "".equals(emlServId)){
						emlServId = jdbcTemplate.queryForObject("SELECT TZ_EMLSERV_ID FROM PS_TZ_EMLS_DEF_TBL WHERE TZ_JG_ID=? and TZ_IS_DEFAULT='Y' limit 0,1", new Object[]{strJgId},"String");
					}
					psTzDxyjfsrwTbl.setTzEmlservId(emlServId);
				}else{
					psTzDxyjfsrwTbl.setTzEmlservId(mailSender);
					String emailSenderAddr = jdbcTemplate.queryForObject("select TZ_EML_ADDR100 from PS_TZ_EMLS_DEF_TBL WHERE TZ_EMLSERV_ID=? limit 0,1", new Object[]{mailSender},"String");
					psTzDxyjfsrwTbl.setTzEmailSender(emailSenderAddr);
				}
				
				if("EXC".equals(sendModel)){
					psTzDxyjfsrwTbl.setTzDynamicFlag("N");
					psTzDxyjfsrwTbl.setTzEmlIfPrt("N");
				}
				
				mailContent = mailContentHandlerServiceImpl.emailConPreprocess(strPicId, strTaskId);
			    psTzDxyjfsrwTbl.setTzMalContent(mailContent);
			    
			    psTzDxyjfsrwTbl.setTzEmailSender(mailSender);
				
			}else{
				if("SMS".equals(taskType)){
					// 如果短信群发设置了短信模板，短信发送服务器为模板中配置的服务器，如果没有配置模板，则短信服务器为当前登录人对应SETID对应配置的服务器;
					if(strTmpId != null && !"".equals(strTmpId)){
						String smsServId = "";
			            Map<String, Object> smsMap = jdbcTemplate.queryForMap("select a.TZ_DYNAMIC_FLAG , a.TZ_SMS_SERV_ID from PS_TZ_SMSTMPL_TBL a, PS_TZ_TMP_DEFN_TBL b where a.TZ_YMB_ID=b.TZ_YMB_ID and a.TZ_JG_ID=? and a.TZ_TMPL_ID=?", new Object[]{strJgId,strTmpId});
			            if(smsMap != null){
			            	dynimicFlg = (String)smsMap.get("TZ_DYNAMIC_FLAG");
			            	smsServId = (String)smsMap.get("TZ_SMS_SERV_ID");
			            }
			            psTzDxyjfsrwTbl.setTzSmsServId(smsServId);
			            psTzDxyjfsrwTbl.setTzDynamicFlag(dynimicFlg);
					}
					
					/*导入Excel发送模式都包含动态个性化内容,需要判重*/
					if("EXC".equals(sendModel)){
						psTzDxyjfsrwTbl.setTzDynamicFlag("N");
						psTzDxyjfsrwTbl.setTzEmlIfPrt("N");
					}
				}
			}
			int i = psTzDxyjfsrwTblMapper.updateByPrimaryKey(psTzDxyjfsrwTbl);
			if(i > 0){
				bl = true;
			}
		}
		return bl;
	}

	// 创建听众
	public String createAudience(String taskId, String strJgId, String strAudienceDesc, String strAudLy) {
		String audId = "";
		try {
			audId = String.valueOf(getSeqNum.getSeqNum("TZ_AUDIENCE_T", "TZ_AUDIENCE_ID"));
			PsTzAudienceT psTzAudienceT = new PsTzAudienceT();
			psTzAudienceT.setTzAudienceId(audId);
			psTzAudienceT.setTzAudMs(strAudienceDesc);
			psTzAudienceT.setTzAudLy(strAudLy);
			psTzAudienceT.setTzJgId(strJgId);
			psTzAudienceT.setRowAddedDttm(new Date());
			psTzAudienceT.setRowAddedOprid(tzLoginServiceImpl.getLoginedManagerOprid(request));
			psTzAudienceT.setRowLastmantDttm(new Date());
			psTzAudienceT.setRowLastmantOprid(tzLoginServiceImpl.getLoginedManagerOprid(request));
			int i = psTzAudienceTMapper.insert(psTzAudienceT);
			if (i <= 0) {
				audId = "";
			} else {
				if (taskId != null && !"".equals(taskId)) {
					PsTzDxyjfsrwTbl psTzDxyjfsrwTbl = psTzDxyjfsrwTblMapper.selectByPrimaryKey(taskId);
					if(psTzDxyjfsrwTbl != null){
						psTzDxyjfsrwTbl.setTzAudienceId(audId);
					}
					psTzDxyjfsrwTblMapper.updateByPrimaryKey(psTzDxyjfsrwTbl);
				}

			}
		} catch (Exception e) {
			audId = "";
		}
		return audId;
	}

	// 添加听众成员;
	public boolean addAudCy(String audId,String audCyId, String name, String ch, String mainPhone, String cyPhone, String mainEmail,
			String cyEmail, String wxh, String oprId, String xsxxId, String hdId, String bmbId) {
		boolean bl = false;
		try {
			if(audCyId == null || "".equals(audCyId)){
				audCyId = String.valueOf(getSeqNum.getSeqNum("TZ_AUDCYUAN_T", "TZ_AUDCY_ID"));
			}
			PsTzAudcyuanT psTzAudcyuanT = new PsTzAudcyuanT();
			psTzAudcyuanT.setTzAudienceId(audId);
			psTzAudcyuanT.setTzAudcyId(audCyId);
			psTzAudcyuanT.setTzAudXm(name);
			psTzAudcyuanT.setTzAudCh(ch);
			psTzAudcyuanT.setTzZySj(mainPhone);
			psTzAudcyuanT.setTzCySj(cyPhone);
			psTzAudcyuanT.setTzZyEmail(mainEmail);
			psTzAudcyuanT.setTzCyEmail(cyEmail);
			psTzAudcyuanT.setTzWeixin(wxh);
			psTzAudcyuanT.setOprid(oprId);
			psTzAudcyuanT.setTzXsxsId(xsxxId);
			psTzAudcyuanT.setTzHuodId(hdId);
			psTzAudcyuanT.setTzBmbId(bmbId);
			int i = psTzAudcyuanTMapper.insert(psTzAudcyuanT);
			if (i > 0) {
				bl = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			bl = false;
		}

		return bl;
	}

}
