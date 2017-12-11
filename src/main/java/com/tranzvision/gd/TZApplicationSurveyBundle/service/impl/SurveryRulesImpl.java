package com.tranzvision.gd.TZApplicationSurveyBundle.service.impl;

import java.util.Date;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcWjDyTWithBLOBs;
import com.tranzvision.gd.util.Calendar.DateUtil;
import com.tranzvision.gd.util.base.MessageTextServiceImpl;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 问卷调查规则校验相关方法定义
 * 
 * @author CAOY
 *
 */
@Service
public class SurveryRulesImpl {

	private final String PSGuest = "TZ_GUEST";

	private final String computerCookieName = "TZSURVERYCOOKIE";

	private final String computerCookieVal = "Y";

	@Autowired
	private SqlQuery jdbcTemplate;

	public String msg;

	@Autowired
	private MessageTextServiceImpl messageTextServiceImpl;

	@SuppressWarnings("unchecked")
	public boolean checkSurveryStatus(PsTzDcWjDyTWithBLOBs psTzDcWjDyTWithBLOBs, String language) {
		boolean checkResult = true;
		this.msg = "";
		if (psTzDcWjDyTWithBLOBs == null) {
			this.msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_SURVEY_MSGSET", "SURVEY_NO_EXIST",
					language, "问卷信息不存在", "The questionnaire information does not exist!");
			return false;
		}
		String TZ_DC_WJ_ZT = psTzDcWjDyTWithBLOBs.getTzDcWjZt();
		if (TZ_DC_WJ_ZT == null) {
			this.msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_SURVEY_MSGSET", "STATE_START",
					language, "未开始", "This survey has not yet started");
			return false;
		}
		if (TZ_DC_WJ_ZT.equals("0")) {
			this.msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_SURVEY_MSGSET", "STATE_START",
					language, "未开始", "This survey has not yet started");
			return false;
		}
		if (TZ_DC_WJ_ZT.equals("2")) {
			this.msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_SURVEY_MSGSET", "STATE_START",
					language, "暂停中", "This survey is suspended for the present");
			return false;
		}
		if (TZ_DC_WJ_ZT.equals("3")) {
			this.msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_SURVEY_MSGSET", "STATE_START",
					language, "已结束", "This survey is already closed");
			return false;
		}

		return checkResult;
	}

	/*
	 * 说明：检查开始/结束日期 参数
	 */
	@SuppressWarnings("unchecked")
	public boolean checkSurveryDate(PsTzDcWjDyTWithBLOBs psTzDcWjDyTWithBLOBs, String language) {
		boolean checkResult = true;
		this.msg = "";

		if (psTzDcWjDyTWithBLOBs == null) {
			this.msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_SURVEY_MSGSET", "SURVEY_NO_EXIST",
					language, "问卷信息不存在", "The questionnaire information does not exist!");
			return false;
		}

		Date TZ_DC_WJ_KSRQ = psTzDcWjDyTWithBLOBs.getTzDcWjKsrq();
		Date TZ_DC_WJ_KSSJ = psTzDcWjDyTWithBLOBs.getTzDcWjKssj();

		Date TZ_DC_WJ_JSRQ = psTzDcWjDyTWithBLOBs.getTzDcWjJsrq();
		Date TZ_DC_WJ_JSSJ = psTzDcWjDyTWithBLOBs.getTzDcWjJssj();

		// 时间转换以及比较
		String beginTime = DateUtil.formatDate(TZ_DC_WJ_KSRQ, "yyyy-MM-dd") + " "
				+ DateUtil.formatDate(TZ_DC_WJ_KSSJ, "HH:mm:ss");
		String endTime = DateUtil.formatDate(TZ_DC_WJ_JSRQ, "yyyy-MM-dd") + " "
				+ DateUtil.formatDate(TZ_DC_WJ_JSSJ, "HH:mm:ss");

		// System.out.println("beginTime:" + beginTime);
		// System.out.println("endTime:" + endTime);

		Date begin = DateUtil.parseTimeStamp(beginTime);
		Date end = DateUtil.parseTimeStamp(endTime);
		Date now = new Date();

		// System.out.println("1:" + now.before(begin));
		if (now.before(begin)) {
			this.msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_SURVEY_MSGSET", "STATE_START",
					language, "未开始", "This survey has not yet started");
			return false;
		}

		// System.out.println("2:" + now.before(begin));
		if (now.after(end)) {
			this.msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_SURVEY_MSGSET", "STATE_START",
					language, "已结束", "This survey is already closed");
			return false;
		}
		return checkResult;
	}

	@SuppressWarnings("unchecked")
	public boolean checkCanAnswer(PsTzDcWjDyTWithBLOBs psTzDcWjDyTWithBLOBs, String language, String strPersonId) {
		boolean checkResult = true;
		this.msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_SURVEY_MSGSET", "ANSWER", language, "可答卷",
				"You can answer");

		if (psTzDcWjDyTWithBLOBs == null) {
			this.msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_SURVEY_MSGSET", "SURVEY_NO_EXIST",
					language, "问卷信息不存在", "The questionnaire information does not exist!");
			return false;
		}
		if (psTzDcWjDyTWithBLOBs.getTzDcWjDlzt() != null && psTzDcWjDyTWithBLOBs.getTzDcWjDlzt().equals("N")
				&& strPersonId.equals(PSGuest)) {
			this.msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_SURVEY_MSGSET", "ANSWER_NO_ANY",
					language, "不可匿名答卷", "An anonymous questionnaire");
			return false;
		}

		if (strPersonId.equals(PSGuest)) {
			this.msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_SURVEY_MSGSET", "ANSWER_YES_ANY",
					language, "可匿名答卷", "Can be anonymous questionnaire");
		}
		return checkResult;
	}

	/*
	 * 说明：检查IP、电脑、账号只能回答一次 参数： &objRequest 传%Request &rtncode string out 返回码 0
	 * 同一个IP只能回答一次 1 同一台电脑只能参与一次 2 无限制 3 同一个账号只能参与一次 -99 问卷信息不存在 &msg string out
	 * 描述信息 返回： boolean true 可以答卷 false 不可以答卷
	 */
	@SuppressWarnings("unchecked")
	public boolean checkSingleAnswerRules(PsTzDcWjDyTWithBLOBs psTzDcWjDyTWithBLOBs, String language,
			HttpServletRequest request, String strPersonId) {
		boolean checkResult = true;
		this.msg = "";
		if (psTzDcWjDyTWithBLOBs == null) {
			this.msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_SURVEY_MSGSET", "SURVEY_NO_EXIST",
					language, "问卷信息不存在", "The questionnaire information does not exist!");
			return false;
		}
		if (psTzDcWjDyTWithBLOBs.getTzDcWjIpgz() != null && psTzDcWjDyTWithBLOBs.getTzDcWjIpgz().equals("0")) {
			// String checkIP;
			String RemoteIPAddr = request.getRemoteAddr();
			int check = jdbcTemplate.queryForObject(
					"SELECT count('Y') FROM PS_TZ_DC_INS_T WHERE TZ_DC_INS_IP=? AND TZ_DC_WJ_ID=?",
					new Object[] { RemoteIPAddr, psTzDcWjDyTWithBLOBs.getTzDcWjId() }, "int");
			if (check > 0) {
				checkResult = false;
			} else {
				checkResult = true;
			}
			this.msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_SURVEY_MSGSET", "DTGZ_IP", language,
					"同一个IP只能回答一次", "The same IP can only be answered onc");
			return checkResult;
		}
		if (psTzDcWjDyTWithBLOBs.getTzDcWjIpgz() != null && psTzDcWjDyTWithBLOBs.getTzDcWjIpgz().equals("1")) {
			String cookieVal = null;
			String strCookieName = computerCookieName + psTzDcWjDyTWithBLOBs.getTzDcWjId();
			Cookie[] cookie = request.getCookies();
			for (int i = 0; i < cookie.length; i++) {
				Cookie cook = cookie[i];
				if (cook.getName().equalsIgnoreCase(strCookieName)) { // 获取键
					cookieVal = cook.getValue(); // 获取值
				}
			}
			if (cookieVal != null && cookieVal.equals(computerCookieVal)) {
				checkResult = false;
			} else {
				checkResult = true;
			}
			this.msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_SURVEY_MSGSET", "DTGZ_PC", language,
					"同一台电脑只能参与一次", "One computer can only participate in the survey for once");
			return checkResult;
		}
		if (psTzDcWjDyTWithBLOBs.getTzDcWjIpgz() != null && psTzDcWjDyTWithBLOBs.getTzDcWjIpgz().equals("2")) {
			this.msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_SURVEY_MSGSET", "DTGZ_ANY", language,
					"无限制", "unlimited");
		}
		if (psTzDcWjDyTWithBLOBs.getTzDcWjIpgz() != null && psTzDcWjDyTWithBLOBs.getTzDcWjIpgz().equals("3")) {
			int check = jdbcTemplate.queryForObject(
					"SELECT count('Y') FROM PS_TZ_DC_INS_T WHERE ROW_ADDED_OPRID=? AND TZ_DC_WJ_ID=?",
					new Object[] { strPersonId, psTzDcWjDyTWithBLOBs.getTzDcWjId() }, "int");
			if (check > 0) {
				checkResult = false;
			} else {
				checkResult = true;
			}
			this.msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_SURVEY_MSGSET", "DTGZ_ACCOUNT",
					language, "同一个账号只能参与一次", "You may only complete this survey once from your account.");
			return checkResult;
		}
		return checkResult;
	}

	@SuppressWarnings("unchecked")
	public boolean checkSurveryNeedsPwd(PsTzDcWjDyTWithBLOBs psTzDcWjDyTWithBLOBs, String language) {
		boolean checkResult = false;
		this.msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_SURVEY_MSGSET", "PASS_NO", language,
				"无需密码", "Without the password ");

		if (psTzDcWjDyTWithBLOBs == null) {
			this.msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_SURVEY_MSGSET", "SURVEY_NO_EXIST",
					language, "问卷信息不存在", "The questionnaire information does not exist!");
			return checkResult;
		}

		if (psTzDcWjDyTWithBLOBs.getTzDcWjNeedpwd() != null && psTzDcWjDyTWithBLOBs.getTzDcWjNeedpwd().equals("Y")) {
			this.msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_SURVEY_MSGSET", "PASS_YES", language,
					"需要密码", "Need a password ");
			return true;
		}
		return checkResult;
	}

	/*
	 * 说明：设置“控制一台电脑只能回答一次”规则的Cookie 参数： &objResponse 传%response 返回： boolean true
	 * 设置成功 false 设置失败
	 */
	@SuppressWarnings("unchecked")
	public boolean setComputerCookie(HttpServletResponse response, String strWJID) {
		boolean boolResult = true;
		String strCookieName = computerCookieName + strWJID;
		Cookie cookie = new Cookie(strCookieName, "");

		cookie.setMaxAge(30 * 24 * 60 * 60);
		cookie.setValue(computerCookieVal);
		response.addCookie(cookie);
		return boolResult;
	}

	/*
	 * 说明：校验密码正确性 参数： &rtncode string out 返回码 1 密码正确 0 无需密码 -1 密码错误 -99 问卷信息不存在
	 * &msg string out 描述信息 返回： boolean true 密码正确 false 密码错误
	 */
	@SuppressWarnings("unchecked")
	public boolean checkSurveryPwd(PsTzDcWjDyTWithBLOBs psTzDcWjDyTWithBLOBs, String language, String pwd) {
		boolean checkResult = true;
		String rtncode = "0";
		this.msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_SURVEY_MSGSET", "PASS_NO", language,
				"无需密码", "Without the password ");

		if (psTzDcWjDyTWithBLOBs == null) {
			this.msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_SURVEY_MSGSET", "SURVEY_NO_EXIST",
					language, "问卷信息不存在", "The questionnaire information does not exist!");
			checkResult = false;
		}

		if (!StringUtils.isEmpty(psTzDcWjDyTWithBLOBs.getTzDcWjPwd())) {
			if (psTzDcWjDyTWithBLOBs.getTzDcWjPwd().equals(pwd)) {
				rtncode = "1";
				this.msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_SURVEY_MSGSET", "PASS_CORRECT",
						language, "密码正确", "Password is correct  ");
			} else {
				rtncode = "-1";
				this.msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_SURVEY_MSGSET", "PASS_MISTAKE",
						language, "密码错误", "Password mistake ");
				checkResult = false;
			}
		}
		return checkResult;
	}
	
	/**
	 * 根据听众列表判断是否能访问，卢艳添加，2017-12-1
	 * @param psTzDcWjDyTWithBLOBs
	 * @param language
	 * @param pwd
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean checkSurveryAudience(PsTzDcWjDyTWithBLOBs psTzDcWjDyTWithBLOBs, String language, String audCyFlag) {
		Boolean checkResult = true;
		this.msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_SURVEY_MSGSET", "ANSWER", language, "可答卷",
				"You can answer");

		if (psTzDcWjDyTWithBLOBs == null) {
			this.msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_SURVEY_MSGSET", "SURVEY_NO_EXIST",
					language, "问卷信息不存在", "The questionnaire information does not exist!");
			return false;
		}
		//不可匿名访问，设置了听众，当前登录人不在听众列表中
		if (!"Y".equals(psTzDcWjDyTWithBLOBs.getTzDcWjDlzt())
				&& !"Y".equals(audCyFlag)) {
			this.msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_SURVEY_MSGSET", "ANSWER_NO_PERMISSION",
					language, "您无权限查看", "You do not have permission to view!");
			return false;
		}

		return checkResult;
	}
}
