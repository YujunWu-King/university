package com.tranzvision.gd.TZEmailSmsSendBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import java.io.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZEmailParameterBundle.dao.PsTzEmlsDefTblMapper;
import com.tranzvision.gd.TZEmailParameterBundle.model.PsTzEmlsDefTbl;
import com.tranzvision.gd.TZEmailSmsQFBundle.dao.PsTzZnxAttchTBLMapper;
import com.tranzvision.gd.TZEmailSmsQFBundle.dao.PsTzZnxMsgTMapper;
import com.tranzvision.gd.TZEmailSmsQFBundle.dao.PsTzZnxRecTMapper;
import com.tranzvision.gd.TZEmailSmsQFBundle.dao.PsTzZnxfslshiTblMapper;
import com.tranzvision.gd.TZEmailSmsQFBundle.dao.PsTzZnxzwlshiTblMapper;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzDxyjQfDyTWithBLOBs;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzZnxAttchTBL;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzZnxMsgT;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzZnxRecT;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzZnxfslshiTbl;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzZnxzwlshiTbl;
import com.tranzvision.gd.TZEmailSmsSendBundle.dao.PsTzDxfslshiTblMapper;
import com.tranzvision.gd.TZEmailSmsSendBundle.dao.PsTzDxmbshliTblMapper;
import com.tranzvision.gd.TZEmailSmsSendBundle.dao.PsTzDxyjfsrwTblMapper;
import com.tranzvision.gd.TZEmailSmsSendBundle.dao.PsTzDxyjrwmxTblMapper;
import com.tranzvision.gd.TZEmailSmsSendBundle.dao.PsTzDxzwlshiTblMapper;
import com.tranzvision.gd.TZEmailSmsSendBundle.dao.PsTzMalBcAddTMapper;
import com.tranzvision.gd.TZEmailSmsSendBundle.dao.PsTzMalCcAddTMapper;
import com.tranzvision.gd.TZEmailSmsSendBundle.dao.PsTzRwzxshilTblMapper;
import com.tranzvision.gd.TZEmailSmsSendBundle.dao.PsTzYjfjlshiTblMapper;
import com.tranzvision.gd.TZEmailSmsSendBundle.dao.PsTzYjfslshiTblMapper;
import com.tranzvision.gd.TZEmailSmsSendBundle.dao.PsTzYjfsrizhTblMapper;
import com.tranzvision.gd.TZEmailSmsSendBundle.dao.PsTzYjmbshliTblMapper;
import com.tranzvision.gd.TZEmailSmsSendBundle.dao.PsTzYjzwlshiTblMapper;
import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzDxfslshiTbl;
import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzDxmbshliTbl;
import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzDxyjfsrwTbl;
import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzDxyjrwmxTblKey;
import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzDxzwlshiTbl;
import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzMalBcAddT;
import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzMalCcAddT;
import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzRwzxshilTbl;
import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzYjfslshiTbl;
import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzYjfsrizhTbl;
import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzYjmbshliTbl;
import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzYjzwlshiTbl;
import com.tranzvision.gd.util.base.AnalysisSysVar;
import com.tranzvision.gd.util.mailer.TranzvisionMail;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.tsinghua.sms.SendSmsService;
import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzYjfjlshiTbl;

/**
 * 邮件短信发送；原：TZ_SMS_MAL:SendSmsOrMal
 * 
 * @author tang
 * @since 2015-11-30
 */
@Service("com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.SendSmsOrMalServiceImpl")
public class SendSmsOrMalServiceImpl {
	// private TranzvisionMail mailer;

	// private String senderEmail;

	// private String PRCSINSTANCE;

	// 邮件发送主题
	// private String malSubjectContent;

	// 邮件发送内容
	// private String malContent;

	// 短信发送内容
	// private String smsContent;

	// 机构id
	// private String strJgId;

	// 模板id
	// private String strMbId;

	// 发送邮件短信元模板Id
	// private String strYmbId;

	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private SendSmsService sendSmsService;
	@Autowired
	private PsTzEmlsDefTblMapper psTzEmlsDefTblMapper;
	@Autowired
	private PsTzYjfsrizhTblMapper psTzYjfsrizhTblMapper;
	@Autowired
	private PsTzDxyjfsrwTblMapper psTzDxyjfsrwTblMapper;
	@Autowired
	private PsTzMalCcAddTMapper psTzMalCcAddTMapper;
	@Autowired
	private PsTzMalBcAddTMapper psTzMalBcAddTMapper;
	@Autowired
	private PsTzYjfslshiTblMapper psTzYjfslshiTblMapper;
	@Autowired
	private PsTzYjzwlshiTblMapper psTzYjzwlshiTblMapper;
	@Autowired
	private PsTzYjfjlshiTblMapper psTzYjfjlshiTblMapper;
	@Autowired
	private PsTzDxyjrwmxTblMapper psTzDxyjrwmxTblMapper;
	@Autowired
	private PsTzRwzxshilTblMapper psTzRwzxshilTblMapper;
	@Autowired
	private PsTzYjmbshliTblMapper psTzYjmbshliTblMapper;
	@Autowired
	private PsTzDxmbshliTblMapper psTzDxmbshliTblMapper;
	@Autowired
	private PsTzDxfslshiTblMapper psTzDxfslshiTblMapper;
	@Autowired
	private PsTzDxzwlshiTblMapper psTzDxzwlshiTblMapper;
	@Autowired
	private PsTzZnxfslshiTblMapper psTzZnxfslshiTblMapper;
	@Autowired
	private PsTzZnxzwlshiTblMapper psTzZnxzwlshiTblMapper;
	@Autowired
	private PsTzZnxMsgTMapper psTzZnxMsgTMapper;
	@Autowired
	private PsTzZnxRecTMapper psTzZnxRecTMapper;
	@Autowired
	private PsTzZnxAttchTBLMapper psTzZnxAttchTBLMapper;

	// 连接邮件服务器
	public boolean connectToMailServer(TranzvisionMail mailer, String emailServerId, String strTaskId) {
		boolean isconectSuccess = false;
		try {
			PsTzEmlsDefTbl psTzEmlsDefTbl = psTzEmlsDefTblMapper.selectByPrimaryKey(emailServerId);
			if (psTzEmlsDefTbl == null) {
				this.writeTaskLog(strTaskId, "", "E", "读取邮件服务配置信息失败");
				return false;
			}

			String smtpAddr = psTzEmlsDefTbl.getTzSmtpAddr();
			String userName = psTzEmlsDefTbl.getTzUsrName();
			String password = psTzEmlsDefTbl.getTzUsrPwd();
			// 设置邮件服务器名称;
			mailer.setMailHost(smtpAddr);
			// 设置用户名;
			mailer.setUserName(userName);
			// 设置密码;
			mailer.setUserPwd(password);

			// 设置发送地址
			String emailSender, senderAlias;
			PsTzDxyjfsrwTbl psTzDxyjfsrwTbl = psTzDxyjfsrwTblMapper.selectByPrimaryKey(strTaskId);
			emailSender = psTzDxyjfsrwTbl.getTzEmailSender();
			senderAlias = psTzDxyjfsrwTbl.getTzSenderAlias();
			if (emailSender != null && !"".equals(emailSender)) {
				// senderEmail = emailSender;
				if (senderAlias == null || "".equals(senderAlias)) {
					mailer.setFromAddress(emailSender);
				} else {
					mailer.setFromAddress(emailSender, senderAlias);
				}

				/* 设置字符集 */
				mailer.setCharset("UTF-8", "UTF-8", "UTF-8", "UTF-8");
				isconectSuccess = mailer.openConnect();
				if (isconectSuccess == false) {
					this.writeTaskLog(strTaskId, "", "E", "邮件服务器链接失败");
				}
			} else {
				this.writeTaskLog(strTaskId, "", "E", "发件人邮箱为空");
			}
		} catch (Exception e) {
			return isconectSuccess;
		}
		return isconectSuccess;
	}

	// 写入日志错误信息,参数：任务ID:strTaskId,任务实例ID：strRwSlId,错误类型：strLogType,错误内容：strError
	public void writeTaskLog(String strTaskId, String strRwSlId, String strLogType, String strError) {
		String tzRzjlId = String.valueOf(getSeqNum.getSeqNum("TZ_YJFSRIZH_TBL", "TZ_RZJL_ID"));
		PsTzYjfsrizhTbl psTzYjfsrizhTbl = new PsTzYjfsrizhTbl();
		psTzYjfsrizhTbl.setTzRzjlId(tzRzjlId);
		psTzYjfsrizhTbl.setTzEmlSmsTaskId(strTaskId);
		psTzYjfsrizhTbl.setTzRwslId(strRwSlId);
		psTzYjfsrizhTbl.setTzRzjlDt(new Date());
		psTzYjfsrizhTbl.setTzYjcwLx(strLogType);
		psTzYjfsrizhTbl.setTzRzjlMs(strError);

		psTzYjfsrizhTblMapper.insert(psTzYjfsrizhTbl);
	}

	public void send(String strTaskId, String prcsinstanceId) {
		// PRCSINSTANCE = prcsinstanceId;

		PsTzDxyjfsrwTbl psTzDxyjfsrwTbl = new PsTzDxyjfsrwTbl();
		psTzDxyjfsrwTbl.setTzEmlSmsTaskId(strTaskId);
		psTzDxyjfsrwTbl.setTzRwzxZt("B");
		psTzDxyjfsrwTbl.setTzRwzxDt(new Date());
		psTzDxyjfsrwTblMapper.updateByPrimaryKeySelective(psTzDxyjfsrwTbl);

		psTzDxyjfsrwTbl = psTzDxyjfsrwTblMapper.selectByPrimaryKey(strTaskId);
		String strJgId = psTzDxyjfsrwTbl.getTzJgId();
		String strMbId = psTzDxyjfsrwTbl.getTzTmplId();
		String strTaskLx = psTzDxyjfsrwTbl.getTzTaskLx();
		// 如果是任务为邮件则链接邮件服务器，否则连接短信服务器;
		if ("MAL".equals(strTaskLx)) {
			// 如果历史表中不存在，则把听众写入短信邮件任务明细表;
			String existNumSQL = "select count(1) from PS_TZ_YJFSLSHI_TBL where TZ_EML_SMS_TASK_ID=?";
			int yjlsExistNum = jdbcTemplate.queryForObject(existNumSQL, new Object[] { strTaskId }, "Integer");
			if (yjlsExistNum == 0) {
				String deleteSQL = "delete from PS_TZ_DXYJRWMX_TBL where TZ_EML_SMS_TASK_ID=?";
				jdbcTemplate.update(deleteSQL, new Object[] { strTaskId });
				String inserSQL = "insert into PS_TZ_DXYJRWMX_TBL (select a.TZ_EML_SMS_TASK_ID,a.TZ_AUDIENCE_ID,c.TZ_AUDCY_ID from PS_TZ_DXYJFSRW_TBL a,PS_TZ_AUDIENCE_T b,PS_TZ_AUDCYUAN_T c where a.TZ_EML_SMS_TASK_ID=? and a.TZ_AUDIENCE_ID=b.TZ_AUDIENCE_ID and b.TZ_AUDIENCE_ID=c.TZ_AUDIENCE_ID )";
				jdbcTemplate.update(inserSQL, new Object[] { strTaskId });
			}
			// 原模板
			String ymbSQL = "select TZ_YMB_ID from PS_TZ_EMALTMPL_TBL where TZ_JG_ID=? and TZ_TMPL_ID=?";
			String strYmbId = jdbcTemplate.queryForObject(ymbSQL, new Object[] { strJgId, strMbId }, "String");

			PsTzYjmbshliTbl psTzYjmbshliTbl = psTzYjmbshliTblMapper.selectByPrimaryKey(strTaskId);
			// 邮件主题;
			String malSubjectContent = "";
			// 邮件内容;
			String malContent = "";
			if (psTzYjmbshliTbl != null) {
				malSubjectContent = psTzYjmbshliTbl.getTzMalSubjuect();
				malContent = psTzYjmbshliTbl.getTzMalContent();
			}
			this.sendEmail(strTaskId, prcsinstanceId, strJgId, strYmbId, malSubjectContent, malContent);
		} else {
			if ("SMS".equals(strTaskLx)) {
				// 如果历史表中不存在，则把听众写入短信邮件任务明细表;
				String existNumSQL = "select count(1) from PS_TZ_DXFSLSHI_TBL where TZ_EML_SMS_TASK_ID=?";
				int dxlsExistNum = jdbcTemplate.queryForObject(existNumSQL, new Object[] { strTaskId }, "Integer");
				if (dxlsExistNum == 0) {
					String deleteSQL = "delete from PS_TZ_DXYJRWMX_TBL where TZ_EML_SMS_TASK_ID=?";
					jdbcTemplate.update(deleteSQL, new Object[] { strTaskId });
					String inserSQL = "insert into PS_TZ_DXYJRWMX_TBL (select a.TZ_EML_SMS_TASK_ID,a.TZ_AUDIENCE_ID,c.TZ_AUDCY_ID from PS_TZ_DXYJFSRW_TBL a,PS_TZ_AUDIENCE_T b,PS_TZ_AUDCYUAN_T c where a.TZ_EML_SMS_TASK_ID=? and a.TZ_AUDIENCE_ID=b.TZ_AUDIENCE_ID and b.TZ_AUDIENCE_ID=c.TZ_AUDIENCE_ID )";
					jdbcTemplate.update(inserSQL, new Object[] { strTaskId });
				}

				// 原模板
				String ymbSQL = "select TZ_YMB_ID from PS_TZ_SMSTMPL_TBL where TZ_JG_ID=? and TZ_TMPL_ID=?";
				String strYmbId = jdbcTemplate.queryForObject(ymbSQL, new Object[] { strJgId, strMbId }, "String");

				String smsContent = "";
				PsTzDxmbshliTbl psTzDxmbshliTbl = psTzDxmbshliTblMapper.selectByPrimaryKey(strTaskId);
				if (psTzDxmbshliTbl != null) {
					smsContent = psTzDxmbshliTbl.getTzSmsContent();
				}

				this.sendSms(strTaskId, prcsinstanceId, strJgId, strYmbId, smsContent);
			}else if("ZNX".equals(strTaskLx)){
				// 如果历史表中不存在，则把听众写入短信邮件任务明细表;
				String existNumSQL = "select count(1) from PS_TZ_ZNXFSLSHI_TBL where TZ_EML_SMS_TASK_ID=?";
				int yjlsExistNum = jdbcTemplate.queryForObject(existNumSQL, new Object[] { strTaskId }, "Integer");
				if (yjlsExistNum == 0) {
					String deleteSQL = "delete from PS_TZ_DXYJRWMX_TBL where TZ_EML_SMS_TASK_ID=?";
					jdbcTemplate.update(deleteSQL, new Object[] { strTaskId });
					String inserSQL = "insert into PS_TZ_DXYJRWMX_TBL (select a.TZ_EML_SMS_TASK_ID,a.TZ_AUDIENCE_ID,c.TZ_AUDCY_ID from PS_TZ_DXYJFSRW_TBL a,PS_TZ_AUDIENCE_T b,PS_TZ_AUDCYUAN_T c where a.TZ_EML_SMS_TASK_ID=? and a.TZ_AUDIENCE_ID=b.TZ_AUDIENCE_ID and b.TZ_AUDIENCE_ID=c.TZ_AUDIENCE_ID )";
					jdbcTemplate.update(inserSQL, new Object[] { strTaskId });
				}
				
				// 原模板
				String ymbSQL = "select TZ_YMB_ID from PS_TZ_ZNXTMPL_TBL where TZ_JG_ID=? and TZ_TMPL_ID=?";
				String strYmbId = jdbcTemplate.queryForObject(ymbSQL, new Object[] { strJgId, strMbId }, "String");

				PsTzYjmbshliTbl psTzYjmbshliTbl = psTzYjmbshliTblMapper.selectByPrimaryKey(strTaskId);
				// 邮件主题;
				String znxSubjectContent = "";
				// 邮件内容;
				String znxContent = "";
				if (psTzYjmbshliTbl != null) {
					znxSubjectContent = psTzYjmbshliTbl.getTzMalSubjuect();
					znxContent = psTzYjmbshliTbl.getTzMalContent();
				}
				
				this.sendZnx(strTaskId, prcsinstanceId, strJgId, strYmbId, znxSubjectContent, znxContent);
			}
		}

	}

	public void sendSms(String strTaskId, String prcsinstanceId, String strJgId, String strYmbId, String smsContent) {
		// 任务发送状态，TZ_RWZX_ZT： A:未处理， B：正在处理， C:成功； D：失败;
		String updateStateSQL = "update PS_TZ_DXYJFSRW_TBL set TZ_RWZX_ZT=? WHERE TZ_EML_SMS_TASK_ID=?";

		int totalSendNum = jdbcTemplate.queryForObject(
				"select count(1) from PS_TZ_DXYJFSRW_TBL a, PS_TZ_DXYJRWMX_TBL b where a.TZ_EML_SMS_TASK_ID=b.TZ_EML_SMS_TASK_ID and a.TZ_AUDIENCE_ID=b.TZ_AUDIENCE_ID and a.TZ_EML_SMS_TASK_ID=?",
				new Object[] { strTaskId }, "Integer");

		// 发送实例开始时间
		Date slStartTime = new Date();
		// 成功发送数;
		int successNum = 0;
		try {
			String isDynamicFlg = "", sendSmsType = "", mainPhone = "", secondphone = "", audId = "", audCyId = "";
			// 循环听众任务信息;
			String sql = "select a.TZ_DYNAMIC_FLAG,a.TZ_SYSJ_LX,a.TZ_JG_ID,c.TZ_AUD_XM,c.TZ_ZY_SJ,c.TZ_CY_SJ,c.TZ_AUDIENCE_ID,c.TZ_AUDCY_ID from PS_TZ_DXYJFSRW_TBL a, PS_TZ_DXYJRWMX_TBL b, PS_TZ_AUDCYUAN_T c WHERE a.TZ_EML_SMS_TASK_ID = b.TZ_EML_SMS_TASK_ID and a.TZ_AUDIENCE_ID=b.TZ_AUDIENCE_ID and b.TZ_AUDCY_ID=c.TZ_AUDCY_ID and b.TZ_AUDIENCE_ID = c.TZ_AUDIENCE_ID and a.TZ_EML_SMS_TASK_ID=?";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[] { strTaskId });
			// 是否已经解析;
			boolean bl = false;
			// 发送内容;
			String content = "";
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					isDynamicFlg = (String) list.get(i).get("TZ_DYNAMIC_FLAG");
					sendSmsType = (String) list.get(i).get("TZ_SYSJ_LX");
					// jgId = (String)list.get(i).get("TZ_JG_ID");
					// xm = (String)list.get(i).get("TZ_AUD_XM");
					mainPhone = (String) list.get(i).get("TZ_ZY_SJ");
					secondphone = (String) list.get(i).get("TZ_CY_SJ");
					audId = (String) list.get(i).get("TZ_AUDIENCE_ID");
					audCyId = (String) list.get(i).get("TZ_AUDCY_ID");

					// 是否发送成功;
					boolean sendSuccess = false;
					// 发送的手机号码;
					String sendPhone = "";
					// 任务实例编号;
					String strRwSlId = String.valueOf(getSeqNum.getSeqNum("TZ_DXFSLSHI_TBL", "TZ_RWSL_ID"));
					boolean blRept = false;

					if ("Y".equals(isDynamicFlg)) {
						if (bl == false) {
							content = this.analysisEmlOrSmsContent(strJgId, strYmbId, audId, audCyId, "SMS", "",
									smsContent,"");
							//添加短信签名
							content = content + "【清华经管】";
							bl = true;
						}
					} else {
						content = this.analysisEmlOrSmsContent(strJgId, strYmbId, audId, audCyId, "SMS", "",
								smsContent,"");
						//添加短信签名
						content = content + "【清华经管】";
					}
					Map<String, String> mapRst = new HashMap<String, String>();
					String errCode = "", errMsg = "";
					// 主要手机;
					if ("A".equals(sendSmsType)) {
						blRept = this.checkIsSendSms(strTaskId, mainPhone);
						if (blRept) {
							// 重复写邮件发送历史表,删除【TZ_DXYJRWMX_TBL】中的发送听众*******/
							this.writeLsSmsData(strRwSlId, mainPhone, "", "RPT", strTaskId, prcsinstanceId);
							this.deleteTaskAud(strTaskId, audId, audCyId);
							continue;
						} else {
							if (mainPhone != null && !"".equals(mainPhone)) {
								sendPhone = mainPhone;
								// 发送短信;
								mapRst = sendSmsService.doSendSms(mainPhone, content);
								if (mapRst.get("msg") != null && !"".equals(mapRst.get("msg")) && !"发送成功".equals(mapRst.get("msg"))) {
									errCode = mapRst.get("code");
									errMsg = mapRst.get("msg");
									this.writeTaskLog(strTaskId, strRwSlId, errCode, errMsg);
								} else {
									sendSuccess = true;
								}

							} else {
								// 为空;
								this.writeLsSmsData(strRwSlId, mainPhone, "", "NULL", strTaskId, prcsinstanceId);
								this.deleteTaskAud(strTaskId, audId, audCyId);
								continue;
							}
						}
					}

					// 次要手机;;
					if ("B".equals(sendSmsType)) {
						blRept = this.checkIsSendSms(strTaskId, secondphone);
						if (blRept) {
							// 重复写邮件发送历史表,删除【TZ_DXYJRWMX_TBL】中的发送听众*******/
							this.writeLsSmsData(strRwSlId, secondphone, "", "RPT", strTaskId, prcsinstanceId);
							this.deleteTaskAud(strTaskId, audId, audCyId);
							continue;
						} else {
							if (secondphone != null && !"".equals(secondphone)) {
								sendPhone = secondphone;
								// 发送短信
								mapRst = sendSmsService.doSendSms(secondphone, content);
								if (mapRst.get("msg") != null && !"".equals(mapRst.get("msg"))) {
									errCode = mapRst.get("code");
									errMsg = mapRst.get("msg");
									this.writeTaskLog(strTaskId, strRwSlId, errCode, errMsg);
								} else {
									sendSuccess = true;
								}
							} else {
								// 为空;
								this.writeLsSmsData(strRwSlId, secondphone, "", "NULL", strTaskId, prcsinstanceId);
								this.deleteTaskAud(strTaskId, audId, audCyId);
								continue;
							}
						}
					}

					// 所有手机;
					if ("C".equals(sendSmsType)) {
						// 主要手机是否重复;
						boolean blRept1 = this.checkIsSendSms(strTaskId, mainPhone);
						// 次要手机是否重复;
						boolean blRept2 = this.checkIsSendSms(strTaskId, secondphone);
						if (blRept1 && blRept2) {
							// 重复写邮件发送历史表,删除【TZ_DXYJRWMX_TBL】中的发送听众*******/
							this.writeLsSmsData(strRwSlId, mainPhone + "," + secondphone, "", "RPT", strTaskId,
									prcsinstanceId);
							this.deleteTaskAud(strTaskId, audId, audCyId);
							continue;
						} else {
							if (mainPhone != null && !"".equals(mainPhone) && secondphone != null
									&& !"".equals(secondphone)) {
								if (mainPhone != null && !"".equals(mainPhone)) {
									sendPhone = mainPhone;
									// 发送短信接口
									mapRst = sendSmsService.doSendSms(mainPhone, content);
									if (mapRst.get("msg") != null && !"".equals(mapRst.get("msg"))) {
										errMsg = mapRst.get("msg");
									} else {
										sendSuccess = true;
									}
								}

								if (secondphone != null && !"".equals(secondphone)) {
									if ("".equals(sendPhone)) {
										sendPhone = secondphone;
									} else {
										sendPhone = sendPhone + "," + secondphone;
									}

									// 发送短信;
									mapRst = sendSmsService.doSendSms(secondphone, content);
									if (mapRst.get("msg") != null && !"".equals(mapRst.get("msg"))) {
										if ("".equals(errCode)) {
											errCode = mapRst.get("code");
										} else {
											errCode = errCode + ";" + mapRst.get("code");
										}
										if ("".equals(errMsg)) {
											errMsg = mapRst.get("msg");
										} else {
											errMsg = errMsg + ";" + mapRst.get("msg");
										}

									} else {
										sendSuccess = true;
									}

								}
								if (sendSuccess != true) {
									this.writeTaskLog(strTaskId, strRwSlId, errCode, errMsg);
								}

							} else {
								// 为空;
								this.writeLsSmsData(strRwSlId, secondphone, "", "NULL", strTaskId, prcsinstanceId);
								this.deleteTaskAud(strTaskId, audId, audCyId);
								continue;
							}

						}
					}

					if (sendSuccess == true) {
						this.writeLsSmsData(strRwSlId, sendPhone, content, "SUC", strTaskId, prcsinstanceId);
						this.deleteTaskAud(strTaskId, audId, audCyId);
						successNum = successNum + 1;
					} else {
						this.writeLsSmsData(strRwSlId, sendPhone, content, "FAIL", strTaskId, prcsinstanceId);
					}

				}
			}

			// 成功;
			jdbcTemplate.update(updateStateSQL, new Object[] { "C", strTaskId });

			// 写发送历史表;
			PsTzRwzxshilTbl psTzRwzxshilTbl = new PsTzRwzxshilTbl();
			psTzRwzxshilTbl.setTzZdBh(String.valueOf(getSeqNum.getSeqNum("TZ_RWZXSHIL_TBL", "TZ_ZD_BH")));
			psTzRwzxshilTbl.setTzJcslId(prcsinstanceId);
			psTzRwzxshilTbl.setTzJcslZt("SUC");
			psTzRwzxshilTbl.setTzEmlSmsTaskId(strTaskId);
			psTzRwzxshilTbl.setTzSltjDt(slStartTime);
			psTzRwzxshilTbl.setTzSlksDt(slStartTime);
			psTzRwzxshilTbl.setTzSljsDt(new Date());
			psTzRwzxshilTbl.setTzSuccNum(successNum);
			psTzRwzxshilTbl.setTzFailNum(totalSendNum - successNum);
			psTzRwzxshilTbl.setTzJgId(strJgId);
			psTzRwzxshilTblMapper.insert(psTzRwzxshilTbl);

		} catch (Exception e) {
			// 错误日志;
			this.writeTaskLog(strTaskId, "", "D", e.toString());
			// 失败;
			jdbcTemplate.update(updateStateSQL, new Object[] { "D", strTaskId });
		}
	}

	public void sendEmail(String strTaskId, String prcsinstanceId, String strJgId, String strYmbId,
			String malSubjectContent, String malContent) {

		// 任务发送状态，TZ_RWZX_ZT： A:未处理， B：正在处理， C:成功； D：失败;
		String updateStateSQL = "update PS_TZ_DXYJFSRW_TBL set TZ_RWZX_ZT=? WHERE TZ_EML_SMS_TASK_ID=?";
		try {
			// 得到发送的任务;
			PsTzDxyjfsrwTbl psTzDxyjfsrwTbl = psTzDxyjfsrwTblMapper.selectByPrimaryKey(strTaskId);
			if (psTzDxyjfsrwTbl != null) {
				// 查询抄送地址和密送地址;
				String cc_Address = "", bc_Address = "";
				PsTzMalCcAddT psTzMalCcAddT = psTzMalCcAddTMapper.selectByPrimaryKey(strTaskId);
				PsTzMalBcAddT psTzMalBcAddT = psTzMalBcAddTMapper.selectByPrimaryKey(strTaskId);
				if (psTzMalCcAddT != null && psTzMalCcAddT.getTzMalCcAddr() != null) {
					cc_Address = psTzMalCcAddT.getTzMalCcAddr();
				}
				if (psTzMalBcAddT != null && psTzMalBcAddT.getTzMalBcAddr() != null) {
					bc_Address = psTzMalBcAddT.getTzMalBcAddr();
				}
				// 查询总的发送数量;
				String totalSQL = "select count(1) from PS_TZ_DXYJFSRW_TBL a, PS_TZ_DXYJRWMX_TBL b where a.TZ_EML_SMS_TASK_ID=b.TZ_EML_SMS_TASK_ID and a.TZ_AUDIENCE_ID=b.TZ_AUDIENCE_ID and a.TZ_EML_SMS_TASK_ID=?";
				int totalSendNum = jdbcTemplate.queryForObject(totalSQL, new Object[] { strTaskId }, "Integer");
				// 实例开始时间;
				Date slStartTime = new Date();

				TranzvisionMail mailer = new TranzvisionMail();
				// 邮件服务器;
				String strYjFwqId = psTzDxyjfsrwTbl.getTzEmlservId();
				// 链接邮件服务器
				boolean isConnect = this.connectToMailServer(mailer, strYjFwqId, strTaskId);
				if (isConnect == false) {
					// 失败;
					jdbcTemplate.update(updateStateSQL, new Object[] { "D", strTaskId });
					return;
				}

				// 是否已经解析;
				boolean bl = false;
				// 发送内容;
				String content = "";
				// 发送成功数;
				int successNum = 0;
				// 循环听众任务信息;
				String sql = "select a.TZ_WEBMAL_FLAG,a.TZ_DYNAMIC_FLAG,a.TZ_SYYX_LX,a.TZ_JG_ID,c.TZ_AUD_XM,c.TZ_ZY_EMAIL,c.TZ_CY_EMAIL,c.TZ_AUDIENCE_ID,c.TZ_AUDCY_ID,a.TZ_EML_IF_PRT from PS_TZ_DXYJFSRW_TBL a, PS_TZ_DXYJRWMX_TBL b, PS_TZ_AUDCYUAN_T c WHERE a.TZ_EML_SMS_TASK_ID = b.TZ_EML_SMS_TASK_ID and a.TZ_AUDIENCE_ID=b.TZ_AUDIENCE_ID and b.TZ_AUDCY_ID=c.TZ_AUDCY_ID and b.TZ_AUDIENCE_ID = c.TZ_AUDIENCE_ID and a.TZ_EML_SMS_TASK_ID=?";
				List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[] { strTaskId });
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						// 听众id;
						String audId = (String) list.get(i).get("TZ_AUDIENCE_ID");
						// 听众参与人ID;
						String audCyId = (String) list.get(i).get("TZ_AUDCY_ID");
						// 是否以HTML格式发送;
						String strIsSendAsHtml = (String) list.get(i).get("TZ_WEBMAL_FLAG");
						// 是否动态内容;
						String isDynamicFlg = (String) list.get(i).get("TZ_DYNAMIC_FLAG");
						// 发送给什么邮箱: 主要邮箱，次要邮箱，所有邮箱;
						String sendEmailType = (String) list.get(i).get("TZ_SYYX_LX");
						// 机构ID;
						// String jgId = (String) list.get(i).get("TZ_JG_ID");
						// 姓名;
						// String xm = (String) list.get(i).get("TZ_AUD_XM");
						// 主要邮箱;
						String mainEmail = (String) list.get(i).get("TZ_ZY_EMAIL");
						// 次要邮箱;
						String secondEmail = (String) list.get(i).get("TZ_CY_EMAIL");
						// 是否判重;
						String emlIfRpt = (String) list.get(i).get("TZ_EML_IF_PRT");
						// 任务实例编号;
						String strRwSlId = String.valueOf(getSeqNum.getSeqNum("TZ_YJFSLSHI_TBL", "TZ_RWSL_ID"));

						boolean blRept = false;

						// 邮件接收地址
						String emailAddrAdd = "";
						// 主要邮箱;
						if ("A".equals(sendEmailType)) {
							emailAddrAdd = "";
							if ("Y".equals(emlIfRpt)) {
								blRept = this.checkIsSendEmail(strTaskId, mainEmail);
							}
							if (blRept) {
								// 发送成功写邮件发送历史表，附件历史表，删除【TZ_DXYJRWMX_TBL】中的发送听众
								this.writeLsMalData(strRwSlId, mainEmail, "", "", "RPT", strTaskId, prcsinstanceId,audCyId);
								this.writeLsMalAttchData(strRwSlId, strTaskId);
								this.deleteTaskAud(strTaskId, audId, audCyId);
							} else {
								emailAddrAdd = mainEmail;
							}
						}

						// 次要邮箱;
						if ("B".equals(sendEmailType)) {
							emailAddrAdd = "";
							if ("Y".equals(emlIfRpt)) {
								blRept = this.checkIsSendEmail(strTaskId, secondEmail);
							}
							if (blRept) {
								// 发送成功写邮件发送历史表，附件历史表，删除【TZ_DXYJRWMX_TBL】中的发送听众
								this.writeLsMalData(strRwSlId, secondEmail, "", "", "RPT", strTaskId, prcsinstanceId,audCyId);
								this.writeLsMalAttchData(strRwSlId, strTaskId);
								this.deleteTaskAud(strTaskId, audId, audCyId);
							} else {
								emailAddrAdd = secondEmail;
							}
						}

						// 所有邮箱;
						if ("C".equals(sendEmailType)) {
							emailAddrAdd = "";
							if ("Y".equals(emlIfRpt)) {
								blRept = this.checkIsSendEmail(strTaskId, mainEmail);
							}
							if (blRept) {
								// 发送成功写邮件发送历史表，附件历史表，删除【TZ_DXYJRWMX_TBL】中的发送听众
								this.writeLsMalData(strRwSlId, mainEmail, "", "", "RPT", strTaskId, prcsinstanceId,audCyId);
								this.writeLsMalAttchData(strRwSlId, strTaskId);
								this.deleteTaskAud(strTaskId, audId, audCyId);
							} else {
								emailAddrAdd = mainEmail;
							}

							if ("Y".equals(emlIfRpt)) {
								blRept = this.checkIsSendEmail(strTaskId, secondEmail);
							}
							if (blRept) {
								// 发送成功写邮件发送历史表，附件历史表，删除【TZ_DXYJRWMX_TBL】中的发送听众
								this.writeLsMalData(strRwSlId, secondEmail, "", "", "RPT", strTaskId, prcsinstanceId,audCyId);
								this.writeLsMalAttchData(strRwSlId, strTaskId);
								this.deleteTaskAud(strTaskId, audId, audCyId);
							} else {
								if (emailAddrAdd == null || "".equals(emailAddrAdd)) {
									emailAddrAdd = secondEmail;
								} else {
									emailAddrAdd = emailAddrAdd + ";" + secondEmail;
								}
							}
						}

						if (emailAddrAdd == null || "".equals(emailAddrAdd)) {
							this.writeLsMalData(strRwSlId, "", "", "", "FAIL", strTaskId, prcsinstanceId,audCyId);
							this.writeLsMalAttchData(strRwSlId, strTaskId);
							this.deleteTaskAud(strTaskId, audId, audCyId);
							continue;
						}

						// 添加附件;
						String fjSQL = "SELECT TZ_FJIAN_ID,TZ_FJIAN_MC,TZ_FJIAN_LJ from PS_TZ_RW_FJIAN_TBL where TZ_EML_SMS_TASK_ID=?";
						List<Map<String, Object>> fjList = jdbcTemplate.queryForList(fjSQL, new Object[] { strTaskId });
						if (fjList != null && fjList.size() > 0) {
							for (int fjnum = 0; fjnum < fjList.size(); fjnum++) {
								String fjlj = (String) fjList.get(fjnum).get("TZ_FJIAN_LJ");
								String fjmc = (String) fjList.get(fjnum).get("TZ_FJIAN_MC");
								if (fjlj != null && !"".equals(fjlj) && fjmc != null && !"".equals(fjmc)) {
									fjlj = request.getSession().getServletContext().getRealPath(fjlj);
									File file = new File(fjlj);
									if (file.exists() && file.isFile()) {
										mailer.setAttachment(fjlj, fjmc);
									} else {
										// 错误日志
										this.writeTaskLog(strTaskId, strRwSlId, "D", "不存在该路径:" + fjlj);
									}
								}
							}
						}

						if ("Y".equals(isDynamicFlg)) {
							if (bl == false) {
								content = this.analysisEmlOrSmsContent(strJgId, strYmbId, audId, audCyId, "MAL",
										malContent, "","");
								bl = true;
							}
						} else {
							content = this.analysisEmlOrSmsContent(strJgId, strYmbId, audId, audCyId, "MAL", malContent,
									"","");
						}
						// 设置收件人
						mailer.setToAddress(emailAddrAdd);
						// 设置抄送人
						if (cc_Address != null && !"".equals(cc_Address)) {
							mailer.setToCCAddr(cc_Address);
						}
						// 设置密送人
						if (bc_Address != null && !"".equals(bc_Address)) {
							mailer.setToBCCAddr(bc_Address);
						}

						mailer.setMailSubject(malSubjectContent);

						if ("Y".equals(strIsSendAsHtml)) {
							mailer.setMailBody(content, true);
						} else {
							mailer.setMailBody(content, false);
						}

						boolean ismail = mailer.sendMail();

						if (ismail) {
							// 发送成功写邮件发送历史表，附件历史表，删除【TZ_DXYJRWMX_TBL】中的发送听众
							this.writeLsMalData(strRwSlId, emailAddrAdd, malSubjectContent, content, "SUC", strTaskId,
									prcsinstanceId,audCyId);
							this.writeLsMalAttchData(strRwSlId, strTaskId);
							this.deleteTaskAud(strTaskId, audId, audCyId);
							successNum = successNum + 1;
						} else {
							String logEmailSendFalseMsg = mailer.getErrorInfo();
							this.writeTaskLog(strTaskId, strRwSlId, "D", logEmailSendFalseMsg);
							// 发送失败写邮件发送历史表，附件历史表
							this.writeLsMalData(strRwSlId, emailAddrAdd, malSubjectContent, content, "FAIL", strTaskId,
									prcsinstanceId,audCyId);
							this.writeLsMalAttchData(strRwSlId, strTaskId);
						}

					}
				}

				// 成功;
				jdbcTemplate.update(updateStateSQL, new Object[] { "C", strTaskId });
				// 写发送历史表;
				PsTzRwzxshilTbl psTzRwzxshilTbl = new PsTzRwzxshilTbl();
				psTzRwzxshilTbl.setTzZdBh(String.valueOf(getSeqNum.getSeqNum("TZ_RWZXSHIL_TBL", "TZ_ZD_BH")));
				psTzRwzxshilTbl.setTzJcslId(prcsinstanceId);
				psTzRwzxshilTbl.setTzJcslZt("SUC");
				psTzRwzxshilTbl.setTzEmlSmsTaskId(strTaskId);
				psTzRwzxshilTbl.setTzSltjDt(slStartTime);
				psTzRwzxshilTbl.setTzSlksDt(slStartTime);
				psTzRwzxshilTbl.setTzSljsDt(new Date());
				psTzRwzxshilTbl.setTzSuccNum(successNum);
				psTzRwzxshilTbl.setTzFailNum(totalSendNum - successNum);
				psTzRwzxshilTbl.setTzJgId(strJgId);
				psTzRwzxshilTblMapper.insert(psTzRwzxshilTbl);

			} else {
				// 错误日志
				this.writeTaskLog(strTaskId, "", "D", "为找到任务id为：[" + strTaskId + "],相应的邮件发送任务");
				// 失败;
				jdbcTemplate.update(updateStateSQL, new Object[] { "D", strTaskId });
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 错误日志;
			this.writeTaskLog(strTaskId, "", "D", e.toString());
			// 失败;
			jdbcTemplate.update(updateStateSQL, new Object[] { "D", strTaskId });
		}

	}
	
	
	public void sendZnx(String strTaskId, String prcsinstanceId, String strJgId, String strYmbId,
			String znxSubjectContent, String znxContent) {

		// 任务发送状态，TZ_RWZX_ZT： A:未处理， B：正在处理， C:成功； D：失败;
		String updateStateSQL = "update PS_TZ_DXYJFSRW_TBL set TZ_RWZX_ZT=? WHERE TZ_EML_SMS_TASK_ID=?";
		try {
			// 得到发送的任务;
			PsTzDxyjfsrwTbl psTzDxyjfsrwTbl = psTzDxyjfsrwTblMapper.selectByPrimaryKey(strTaskId);
			if (psTzDxyjfsrwTbl != null) {

				// 查询总的发送数量;
				String totalSQL = "select count(1) from PS_TZ_DXYJFSRW_TBL a, PS_TZ_DXYJRWMX_TBL b where a.TZ_EML_SMS_TASK_ID=b.TZ_EML_SMS_TASK_ID and a.TZ_AUDIENCE_ID=b.TZ_AUDIENCE_ID and a.TZ_EML_SMS_TASK_ID=?";
				int totalSendNum = jdbcTemplate.queryForObject(totalSQL, new Object[] { strTaskId }, "Integer");
				// 实例开始时间;
				Date slStartTime = new Date();
				
				// 是否已经解析;
				boolean bl = false;
				// 发送内容;
				String content = "";
				// 发送成功数;
				int successNum = 0;
				
				String tzZnxMsgid = String.valueOf(getSeqNum.getSeqNum("PS_TZ_ZNX_MSG_T", "TZ_ZNX_MSGID"));
				
				//任务添加人;
				String sendOPRID = "";
				sendOPRID = jdbcTemplate.queryForObject("select ROW_ADDED_OPRID from PS_TZ_DXYJFSRW_TBL where TZ_EML_SMS_TASK_ID=?", new Object[]{strTaskId},"String");
				// 循环听众任务信息;
				String sql = "select a.TZ_DYNAMIC_FLAG,a.TZ_JG_ID,c.TZ_AUD_XM,c.OPRID,c.TZ_AUDIENCE_ID,c.TZ_AUDCY_ID,a.TZ_EML_IF_PRT from PS_TZ_DXYJFSRW_TBL a, PS_TZ_DXYJRWMX_TBL b, PS_TZ_AUDCYUAN_T c WHERE a.TZ_EML_SMS_TASK_ID = b.TZ_EML_SMS_TASK_ID and a.TZ_AUDIENCE_ID=b.TZ_AUDIENCE_ID and b.TZ_AUDCY_ID=c.TZ_AUDCY_ID and b.TZ_AUDIENCE_ID = c.TZ_AUDIENCE_ID and a.TZ_EML_SMS_TASK_ID=?";
				List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[] { strTaskId });
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						// 听众id;
						String audId = (String) list.get(i).get("TZ_AUDIENCE_ID");
						// 听众参与人ID;
						String audCyId = (String) list.get(i).get("TZ_AUDCY_ID");
						// 是否动态内容;
						String isDynamicFlg = (String) list.get(i).get("TZ_DYNAMIC_FLAG");

						// 机构ID;
						// String jgId = (String) list.get(i).get("TZ_JG_ID");
						// 姓名;
						// String xm = (String) list.get(i).get("TZ_AUD_XM");
						// 收件人;
						String oprid = (String) list.get(i).get("OPRID");
						
						// 是否判重;
						String emlIfRpt = (String) list.get(i).get("TZ_EML_IF_PRT");
						// 任务实例编号;
						String strRwSlId = String.valueOf(getSeqNum.getSeqNum("TZ_YJFSLSHI_TBL", "TZ_RWSL_ID"));

						boolean blRept = false;
						
						//是否为空;
						if(oprid == null || "".equals(oprid)){
							this.writeLsZnxData(strRwSlId, oprid, "", "", "NULL", strTaskId, prcsinstanceId,audCyId);
							this.writeLsMalAttchData(strRwSlId, strTaskId);
							this.deleteTaskAud(strTaskId, audId, audCyId);
							continue;
						}
						
						
						blRept = this.checkIsSendZnx(strTaskId, oprid);
						
						if (blRept) {
							// 发送成功写邮件发送历史表，附件历史表，删除【TZ_DXYJRWMX_TBL】中的发送听众
							this.writeLsZnxData(strRwSlId, oprid, "", "", "RPT", strTaskId, prcsinstanceId,audCyId);
							this.writeLsMalAttchData(strRwSlId, strTaskId);
							this.deleteTaskAud(strTaskId, audId, audCyId);
							continue;
						}
						
						if ("Y".equals(isDynamicFlg)) {
							if (bl == false) {
								content = this.analysisEmlOrSmsContent(strJgId, strYmbId, audId, audCyId, "ZNX","", "",znxContent);
								bl = true;
							}
						} else {
							content = this.analysisEmlOrSmsContent(strJgId, strYmbId, audId, audCyId, "ZNX", "","",znxContent);
						}

						//写站内信信息表；
						PsTzZnxMsgT psTzZnxMsgT = psTzZnxMsgTMapper.selectByPrimaryKey(tzZnxMsgid);
					    if(psTzZnxMsgT == null){
					    	psTzZnxMsgT = new PsTzZnxMsgT();
							
							psTzZnxMsgT.setTzZnxMsgid(tzZnxMsgid);
							psTzZnxMsgT.setTzZnxSendid(sendOPRID);
							psTzZnxMsgT.setTzMsgSubject(znxSubjectContent);
							psTzZnxMsgT.setRowAddedDttm(new Date());
							psTzZnxMsgT.setRowAddedOprid(sendOPRID);
							psTzZnxMsgT.setRowLastmantDttm(new Date());
							psTzZnxMsgT.setRowLastmantOprid(sendOPRID);
							psTzZnxMsgTMapper.insert(psTzZnxMsgT);
					    }
						
						PsTzZnxRecT psTzZnxRecT = new PsTzZnxRecT();
						psTzZnxRecT.setTzZnxMsgid(tzZnxMsgid);
						psTzZnxRecT.setTzZnxRecid(oprid);
						psTzZnxRecT.setTzZnxStatus("N");
						psTzZnxRecT.setTzRecDelstatus("N");
						psTzZnxRecT.setTzMsgText(content);
						int insert = psTzZnxRecTMapper.insert(psTzZnxRecT);

						// 添加附件;
						String fjSQL = "SELECT TZ_FJIAN_ID,TZ_FJIAN_MC,TZ_FJIAN_LJ from PS_TZ_RW_FJIAN_TBL where TZ_EML_SMS_TASK_ID=?";
						List<Map<String, Object>> fjList = jdbcTemplate.queryForList(fjSQL, new Object[] { strTaskId });
						if (fjList != null && fjList.size() > 0) {
							for (int fjnum = 0; fjnum < fjList.size(); fjnum++) {
								String fjId = (String) fjList.get(fjnum).get("TZ_FJIAN_ID");
								String fjlj = (String) fjList.get(fjnum).get("TZ_FILE_PATH");
								String fjmc = (String) fjList.get(fjnum).get("TZ_FJIAN_MC");
								if (fjlj != null && !"".equals(fjlj) && fjmc != null && !"".equals(fjmc)) {
									PsTzZnxAttchTBL psTzZnxAttchTBL = new PsTzZnxAttchTBL();
									psTzZnxAttchTBL.setTzZnxMsgid(tzZnxMsgid);
									psTzZnxAttchTBL.setTzFjianId(fjId);
									psTzZnxAttchTBL.setTzFjianMc(fjmc);
									psTzZnxAttchTBL.setTzFjianLj(fjlj);
									psTzZnxAttchTBLMapper.insert(psTzZnxAttchTBL);
								}
							}
						}

						if (insert > 0) {
							// 发送成功写邮件发送历史表，附件历史表，删除【TZ_DXYJRWMX_TBL】中的发送听众
							this.writeLsZnxData(strRwSlId, oprid, znxSubjectContent, content, "SUC", strTaskId,
									prcsinstanceId,audCyId);
							this.writeLsMalAttchData(strRwSlId, strTaskId);
							this.deleteTaskAud(strTaskId, audId, audCyId);
							successNum = successNum + 1;
						} else {
							this.writeTaskLog(strTaskId, strRwSlId, "D", "数据插入失败");
							// 发送失败写邮件发送历史表，附件历史表
							this.writeLsZnxData(strRwSlId, oprid, znxSubjectContent, content, "FAIL", strTaskId,
									prcsinstanceId,audCyId);
							this.writeLsMalAttchData(strRwSlId, strTaskId);
						}
					}
				}

				// 成功;
				jdbcTemplate.update(updateStateSQL, new Object[] { "C", strTaskId });
				// 写发送历史表;
				PsTzRwzxshilTbl psTzRwzxshilTbl = new PsTzRwzxshilTbl();
				psTzRwzxshilTbl.setTzZdBh(String.valueOf(getSeqNum.getSeqNum("TZ_RWZXSHIL_TBL", "TZ_ZD_BH")));
				psTzRwzxshilTbl.setTzJcslId(prcsinstanceId);
				psTzRwzxshilTbl.setTzJcslZt("SUC");
				psTzRwzxshilTbl.setTzEmlSmsTaskId(strTaskId);
				psTzRwzxshilTbl.setTzSltjDt(slStartTime);
				psTzRwzxshilTbl.setTzSlksDt(slStartTime);
				psTzRwzxshilTbl.setTzSljsDt(new Date());
				psTzRwzxshilTbl.setTzSuccNum(successNum);
				psTzRwzxshilTbl.setTzFailNum(totalSendNum - successNum);
				psTzRwzxshilTbl.setTzJgId(strJgId);
				psTzRwzxshilTblMapper.insert(psTzRwzxshilTbl);

			} else {
				// 错误日志
				this.writeTaskLog(strTaskId, "", "D", "为找到任务id为：[" + strTaskId + "],相应的邮件发送任务");
				// 失败;
				jdbcTemplate.update(updateStateSQL, new Object[] { "D", strTaskId });
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 错误日志;
			this.writeTaskLog(strTaskId, "", "D", e.toString());
			// 失败;
			jdbcTemplate.update(updateStateSQL, new Object[] { "D", strTaskId });
		}

	}

	// *检查在某个任务中某个邮箱是否已经发送过邮件;
	private boolean checkIsSendEmail(String strTaskId, String emailAddress) {
		String sql = "select count(1) from PS_TZ_YJFSLSHI_TBL where TZ_EML_SMS_TASK_ID=? and TZ_JS_EMAIL like ? and TZ_FS_ZT = 'SUC'";
		int count = jdbcTemplate.queryForObject(sql, new Object[] { strTaskId, "%" + emailAddress + "%" }, "Integer");
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	// 写邮件历史数据表
	private void writeLsMalData(String strRwSlId, String sjxEmail, String tj, String content, String strFsZt,
			String strTaskId, String prcsinstanceId,String tzAudcyId) {
		PsTzDxyjfsrwTbl psTzDxyjfsrwTbl = psTzDxyjfsrwTblMapper.selectByPrimaryKey(strTaskId);
		String senderEmail = "";
		if (psTzDxyjfsrwTbl != null) {
			senderEmail = psTzDxyjfsrwTbl.getTzEmailSender();
		}
		// 邮件发送历史表;
		PsTzYjfslshiTbl psTzYjfslshiTbl = new PsTzYjfslshiTbl();
		psTzYjfslshiTbl.setTzRwslId(strRwSlId);
		psTzYjfslshiTbl.setTzFsEmail(senderEmail);
		psTzYjfslshiTbl.setTzJsEmail(sjxEmail);
		psTzYjfslshiTbl.setTzEmZhuti(tj);
		psTzYjfslshiTbl.setTzFsDt(new Date());
		psTzYjfslshiTbl.setTzEmlSmsTaskId(strTaskId);
		psTzYjfslshiTbl.setTzFsZt(strFsZt);
		psTzYjfslshiTbl.setTzJcslId(prcsinstanceId);
		psTzYjfslshiTbl.setTzAudcyId(tzAudcyId);
		psTzYjfslshiTblMapper.insert(psTzYjfslshiTbl);

		// 邮件发送内容历史表
		PsTzYjzwlshiTbl psTzYjzwlshiTbl = new PsTzYjzwlshiTbl();
		psTzYjzwlshiTbl.setTzRwslId(strRwSlId);
		psTzYjzwlshiTbl.setTzYjfsRq(new Date());
		psTzYjzwlshiTbl.setTzYjZhwen(content);
		psTzYjzwlshiTblMapper.insert(psTzYjzwlshiTbl);
	}
	
	// 写站内信历史数据表
	private void writeLsZnxData(String strRwSlId, String oprid, String tj, String content, String strFsZt,
					String strTaskId, String prcsinstanceId,String tzAudcyId) {
		// 站内信发送历史表;
		PsTzZnxfslshiTbl psTzZnxfslshiTbl = new PsTzZnxfslshiTbl();
		psTzZnxfslshiTbl.setTzRwslId(strRwSlId);
		psTzZnxfslshiTbl.setOprid(oprid);
		psTzZnxfslshiTbl.setTzZnxZt(tj);
		psTzZnxfslshiTbl.setTzFsDt(new Date());
		psTzZnxfslshiTbl.setTzEmlSmsTaskId(strTaskId);
		psTzZnxfslshiTbl.setTzFsZt(strFsZt);
		psTzZnxfslshiTbl.setTzJcslId(prcsinstanceId);
		psTzZnxfslshiTbl.setTzAudcyId(tzAudcyId);
		psTzZnxfslshiTblMapper.insert(psTzZnxfslshiTbl);

		// 站内信发送内容历史表
		PsTzZnxzwlshiTbl psTzZnxzwlshiTbl = new PsTzZnxzwlshiTbl();
		psTzZnxzwlshiTbl.setTzRwslId(strRwSlId);
		psTzZnxzwlshiTbl.setTzYjfsRq(new Date());
		psTzZnxzwlshiTbl.setTzZnxZhwen(content);
		psTzZnxzwlshiTblMapper.insert(psTzZnxzwlshiTbl);
	}

	// 写邮件附件历史数据表
	private void writeLsMalAttchData(String strRwSlId, String strTaskId) {
		String sql = "select TZ_FJIAN_ID,TZ_FJIAN_MC,TZ_FJIAN_LJ from PS_TZ_RW_FJIAN_TBL WHERE TZ_EML_SMS_TASK_ID=? AND TZ_FJIAN_ID NOT IN (SELECT TZ_FJIAN_ID FROM PS_TZ_YJFJLSHI_TBL WHERE TZ_EML_SMS_TASK_ID=?)";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[] { strTaskId, strTaskId });
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				PsTzYjfjlshiTbl psTzYjfjlshiTbl = new PsTzYjfjlshiTbl();
				psTzYjfjlshiTbl.setTzRwslId(strRwSlId);
				psTzYjfjlshiTbl.setTzFjianId((String) list.get(i).get("TZ_FJIAN_ID"));
				psTzYjfjlshiTbl.setTzFjianLj((String) list.get(i).get("TZ_FJIAN_LJ"));
				psTzYjfjlshiTbl.setTzFjianMc((String) list.get(i).get("TZ_FJIAN_MC"));
				psTzYjfjlshiTblMapper.insert(psTzYjfjlshiTbl);
			}
		}
	}

	// 发送成功后删除【短信邮件任务明细表】中的听众;
	private void deleteTaskAud(String strTaskId, String audId, String audCyId) {
		PsTzDxyjrwmxTblKey psTzDxyjrwmxTblKey = new PsTzDxyjrwmxTblKey();
		psTzDxyjrwmxTblKey.setTzEmlSmsTaskId(strTaskId);
		psTzDxyjrwmxTblKey.setTzAudienceId(audId);
		psTzDxyjrwmxTblKey.setTzAudcyId(audCyId);
		psTzDxyjrwmxTblMapper.deleteByPrimaryKey(psTzDxyjrwmxTblKey);
	}

	// 解析发送短信邮件的内容:参数：任务ID，audId：听众成员ID, msgType发送的类型： SMS为短信， MAL为电子邮件;
	private String analysisEmlOrSmsContent(String strJgId, String strYmbId, String audId, String audCyrId,
			String msgType, String malContent, String smsContent,String znxContent) {
		String content = "";
		if ("MAL".equals(msgType)) {
			content = malContent;
		}

		if ("SMS".equals(msgType)) {
			content = smsContent;
		}
		
		if ("ZNX".equals(msgType)) {
			content = znxContent;
		}

		ArrayList<String[]> arrayList = this.ayalyMbVar(strJgId, strYmbId, audId, audCyrId);
		if (arrayList != null && arrayList.size() > 0) {
			for (int i = 0; i < arrayList.size(); i++) {
				String[] str = arrayList.get(i);
				String name = str[0];
				String value = str[1];
				
				content = content.replace(name, value);
			}
		}
		return content;
	}

	// 解析邮件中的系统变量
	public ArrayList<String[]> ayalyMbVar(String strJgId, String strYmbId, String audId, String audCyId) {
		ArrayList<String[]> arrayList = new ArrayList<String[]>();
		String sql = "select a.TZ_YMB_CSLBM,b.TZ_PARA_ID,b.TZ_PARA_ALIAS,b.TZ_SYSVARID from PS_TZ_TMP_DEFN_TBL a,PS_TZ_TMP_PARA_TBL b where a.TZ_JG_ID=? and a.TZ_YMB_ID=? and a.TZ_JG_ID=b.TZ_JG_ID and a.TZ_YMB_ID=b.TZ_YMB_ID";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[] { strJgId, strYmbId });
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = list.get(i);
				String ymbCslbm =  map.get("TZ_YMB_CSLBM") == null ? "" : (String) map.get("TZ_YMB_CSLBM");
				String ymbParaId = map.get("TZ_PARA_ID")  == null ? "" : (String) map.get("TZ_PARA_ID");
				String ymbParaAlias = map.get("TZ_PARA_ALIAS")  == null ? "" : (String) map.get("TZ_PARA_ALIAS");
				String sysvarId = map.get("TZ_SYSVARID")  == null ? "" : (String) map.get("TZ_SYSVARID");
				String[] sysVarParam = { audId, audCyId };
				AnalysisSysVar analysisSysVar = new AnalysisSysVar();
				analysisSysVar.setM_SysVarID(sysvarId);
				analysisSysVar.setM_SysVarParam(sysVarParam);
				Object obj = analysisSysVar.GetVarValue();
				
				/*
				ymbCslbm = ymbCslbm.replaceAll("\\(", "\\\\("); 
			   	ymbCslbm = ymbCslbm.replaceAll("\\)", "\\\\)"); 
			   	
			   	ymbParaId = ymbParaId.replaceAll("\\(", "\\\\("); 
			   	ymbParaId = ymbParaId.replaceAll("\\)", "\\\\)"); 
			   	   
			   	ymbParaAlias = ymbParaAlias.replaceAll("\\(", "\\\\("); 
			   	ymbParaAlias = ymbParaAlias.replaceAll("\\)", "\\\\)"); 

				String name = "\\[" + ymbCslbm + "\\." + ymbParaId + "\\." + ymbParaAlias + "\\]";
				*/
				String name = "[" + ymbCslbm + "." + ymbParaId + "." + ymbParaAlias + "]";
				String value = obj == null ? "" : (String) obj;
				String[] returnString = { name, value };
				arrayList.add(returnString);
			}
		}
		return arrayList;
	}

	private boolean checkIsSendSms(String strTaskId, String phoneNum) {
		String sql = "select count(1) from PS_TZ_DXFSLSHI_TBL where TZ_EML_SMS_TASK_ID=? and TZ_SJ_HM like ? and TZ_FS_ZT='SUC'";
		int count = jdbcTemplate.queryForObject(sql, new Object[] { strTaskId, "%" + phoneNum + "%" }, "Integer");
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	private void writeLsSmsData(String strRwSlId, String phone, String content, String strFsZt, String strTaskId,
			String prcsinstanceId) {
		// 短信发送历史表;
		PsTzDxfslshiTbl psTzDxfslshiTbl = new PsTzDxfslshiTbl();
		psTzDxfslshiTbl.setTzRwslId(strRwSlId);
		psTzDxfslshiTbl.setTzSjHm(phone);
		psTzDxfslshiTbl.setTzFsDt(new Date());
		psTzDxfslshiTbl.setTzFsZt(strFsZt);
		psTzDxfslshiTbl.setTzEmlSmsTaskId(strTaskId);
		psTzDxfslshiTbl.setTzJcslId(prcsinstanceId);
		psTzDxfslshiTblMapper.insert(psTzDxfslshiTbl);
		// 短信发送内容表;
		PsTzDxzwlshiTbl psTzDxzwlshiTbl = new PsTzDxzwlshiTbl();
		psTzDxzwlshiTbl.setTzRwslId(strRwSlId);
		psTzDxzwlshiTbl.setTzYjfsRq(new Date());
		psTzDxzwlshiTbl.setTzDxZhwen(content);
		psTzDxzwlshiTblMapper.insert(psTzDxzwlshiTbl);
	}
	
	// *检查在某个任务中某个人员是否已经发送过站内信;
	private boolean checkIsSendZnx(String strTaskId, String oprid) {
		String sql = "select count(1) from PS_TZ_ZNXFSLSHI_TBL where TZ_EML_SMS_TASK_ID=? and OPRID = ? and TZ_FS_ZT = 'SUC'";
		int count = jdbcTemplate.queryForObject(sql, new Object[] { strTaskId, oprid}, "Integer");
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

}
