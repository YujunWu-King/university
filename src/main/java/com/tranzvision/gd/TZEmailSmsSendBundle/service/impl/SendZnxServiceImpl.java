package com.tranzvision.gd.TZEmailSmsSendBundle.service.impl;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZEmailSmsQFBundle.dao.PsTzZnxAttchTBLMapper;
import com.tranzvision.gd.TZEmailSmsQFBundle.dao.PsTzZnxMsgTMapper;
import com.tranzvision.gd.TZEmailSmsQFBundle.dao.PsTzZnxRecTMapper;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzZnxAttchTBL;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzZnxMsgT;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzZnxRecT;
import com.tranzvision.gd.util.sql.GetSeqNum;

/*
 * 不用模板，直接发送站内信
 */
@Service
public class SendZnxServiceImpl {
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private PsTzZnxMsgTMapper psTzZnxMsgTMapper;
	@Autowired
	private PsTzZnxRecTMapper psTzZnxRecTMapper;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private PsTzZnxAttchTBLMapper psTzZnxAttchTBLMapper;

	// 发送站内信;
	public String sendZnx(String oprid, String znxSubjectContent, String content) {
		String tzZnxMsgid = String.valueOf(getSeqNum.getSeqNum("PS_TZ_ZNX_MSG_T", "TZ_ZNX_MSGID"));

		String sendOPRID = tzLoginServiceImpl.getLoginedManagerOprid(request);
		int insert = 0;
		// 写站内信信息表；
		PsTzZnxMsgT psTzZnxMsgT = new PsTzZnxMsgT();
		psTzZnxMsgT.setTzZnxMsgid(tzZnxMsgid);
		psTzZnxMsgT.setTzZnxSendid(sendOPRID);
		// 主题;
		psTzZnxMsgT.setTzMsgSubject(znxSubjectContent);
		psTzZnxMsgT.setRowAddedDttm(new Date());
		psTzZnxMsgT.setRowAddedOprid(sendOPRID);
		psTzZnxMsgT.setRowLastmantDttm(new Date());
		psTzZnxMsgT.setRowLastmantOprid(sendOPRID);
		insert = psTzZnxMsgTMapper.insert(psTzZnxMsgT);
		if (insert > 0) {
			// 内容;
			PsTzZnxRecT psTzZnxRecT = new PsTzZnxRecT();
			psTzZnxRecT.setTzZnxMsgid(tzZnxMsgid);
			psTzZnxRecT.setTzZnxRecid(oprid);
			psTzZnxRecT.setTzZnxStatus("N");
			psTzZnxRecT.setTzRecDelstatus("N");
			// 内容;
			psTzZnxRecT.setTzMsgText(content);
			insert = psTzZnxRecTMapper.insert(psTzZnxRecT);
		}

		if (insert > 0) {
			return tzZnxMsgid;
		} else {
			return "";
		}
	}

	// 添加附件;
	public boolean sendZnxAttach(String tzZnxMsgid, String fjmc, String fjlj) {
		String fjId = String.valueOf(getSeqNum.getSeqNum("PS_TZ_ZNX_ATTCH_TBL", "TZ_FJIAN_ID"));

		PsTzZnxAttchTBL psTzZnxAttchTBL = new PsTzZnxAttchTBL();
		psTzZnxAttchTBL.setTzZnxMsgid(tzZnxMsgid);
		psTzZnxAttchTBL.setTzFjianId(fjId);
		// 附件名称;
		psTzZnxAttchTBL.setTzFjianMc(fjmc);
		// 附件相对路径；
		psTzZnxAttchTBL.setTzFjianLj(fjlj);
		int insert = psTzZnxAttchTBLMapper.insert(psTzZnxAttchTBL);
		if (insert > 0) {
			return true;
		} else {
			return false;
		}
	}
}
