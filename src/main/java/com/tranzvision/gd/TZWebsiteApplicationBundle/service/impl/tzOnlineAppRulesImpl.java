package com.tranzvision.gd.TZWebsiteApplicationBundle.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tranzvision.gd.util.base.MessageTextServiceImpl;
import com.tranzvision.gd.util.encrypt.Sha3DesMD5;

@Service
public class tzOnlineAppRulesImpl {
	@Autowired
	private MessageTextServiceImpl messageTextServiceImpl;

	public String msg;

	/*
	 * 说明：校验密码正确性 参数： &rtncode string out 返回码 1 密码正确 0 无需密码 -1 密码错误 -99 问卷信息不存在
	 * &msg string out 描述信息 返回： boolean true 密码正确 false 密码错误
	 */
	@SuppressWarnings("unchecked")
	public boolean checkTJXPwd(String dataPwd, String language, String pwd) {
		boolean checkResult = true;
		this.msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_SURVEY_MSGSET", "PASS_NO", language,
				"无需密码", "Without the password ");

		if (dataPwd == null || dataPwd.equals("")) {
			this.msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_SURVEY_MSGSET", "PASS_MISTAKE",
					language, "密码错误", "Password mistake ");
			checkResult = false;
		}

		if (pwd == null || pwd.equals("")) {
			this.msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_SURVEY_MSGSET", "PASS_MISTAKE",
					language, "密码错误", "Password mistake ");
			checkResult = false;
		}

		String md5Pwd = Sha3DesMD5.md5(pwd);
		System.out.println("md5Pwd:"+md5Pwd);
		System.out.println("dataPwd:"+dataPwd);
		if (dataPwd.equals(md5Pwd)) {
			this.msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_SURVEY_MSGSET", "PASS_CORRECT",
					language, "密码正确", "Password is correct  ");
			checkResult = true;
		} else {
			this.msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_SURVEY_MSGSET", "PASS_MISTAKE",
					language, "密码错误", "Password mistake ");
			checkResult = false;
		}

		return checkResult;
	}
	
	
}
