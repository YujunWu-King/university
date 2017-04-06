package com.tranzvision.gd.TZRecommendationBundle.service.impl;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.CreateTaskServiceImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.SendSmsOrMalServiceImpl;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzKsTjxTblMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzKsTjxTbl;
import com.tranzvision.gd.util.base.MessageTextServiceImpl;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * PS:TZ_GD_TJX_PKG:TZ_TJX_CLS
 * 
 * @author tang
 *
 */
@Service("com.tranzvision.gd.TZRecommendationBundle.service.impl.TzTjxClsServiceImpl")
public class TzTjxClsServiceImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private MessageTextServiceImpl messageTextServiceImpl;
	@Autowired
	private PsTzKsTjxTblMapper psTzKsTjxTblMapper;
	@Autowired
	private CreateTaskServiceImpl createTaskServiceImpl;
	@Autowired
	private SendSmsOrMalServiceImpl sendSmsOrMalServiceImpl;

	// 保存推荐信信息;
	public String saveTJX(long numAppinsId, String strOprid, String strTjrId, String strEmail, String strTjxType,
			String strTitle, String strGname, String strName, String strCompany, String strPosition,
			String strPhone_area, String strPhone_no, String strGender, String strAdd1, String strAdd2, String strAdd3,
			String strAdd4, String strAdd5, String strAdd6, String strAdd7, String strAdd8, String strAdd9,
			String strAdd10, String strTjrgx, String str_sysfilename, String str_filename, String str_refLetterType,
			String str_tjx_valid, String accessPath, String attAUrl) {
		boolean b_flag = true;
		String strRtn = "";
		String strTjxId = "";
		String strEmailflg = jdbcTemplate.queryForObject(
				"select 'Y' from PS_TZ_KS_TJX_TBL A  where  A.TZ_APP_INS_ID=? AND A.TZ_TJR_ID<>? AND A.OPRID=? and TZ_MBA_TJX_YX='Y' and upper(A.TZ_EMAIL)=upper(?) limit 0,1",
				new Object[] { numAppinsId, strTjrId, strOprid, strEmail }, "String");
		String str_language = jdbcTemplate.queryForObject(
				"SELECT B.TZ_APP_TPL_LAN FROM PS_TZ_APP_INS_T A,PS_TZ_APPTPL_DY_T B WHERE A.TZ_APP_INS_ID=? AND A.TZ_APP_TPL_ID=B.TZ_APP_TPL_ID limit 0,1",
				new Object[] { numAppinsId }, "String");
		if ("Y".equals(strEmailflg)) {
			// 邮箱重复;
			strRtn = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_APPONLINE_MSGSET", "REF_E_DIF",
					str_language, "", "");
		} else {
			strTjxId = jdbcTemplate.queryForObject(
					"select TZ_REF_LETTER_ID from PS_TZ_KS_TJX_TBL where TZ_APP_INS_ID=? and OPRID=?  and TZ_TJR_ID=? and TZ_MBA_TJX_YX='Y' limit 0,1",
					new Object[] { numAppinsId, strOprid, strTjrId }, "String");
			;
			if ((strTjxId == null || "".equals(strTjxId)) && "N".equals(str_tjx_valid)) {
				b_flag = false;
			}

			if (b_flag) {
				if (strTjxId == null || "".equals(strTjxId)) {
					String str_seq1 = String.valueOf((int) (Math.random() * 10000000));
					String str_seq2 = "00000000000000"
							+ String.valueOf(getSeqNum.getSeqNum("TZ_KS_TJX_TBL", "TZ_REF_LETTER_ID"));
					str_seq2 = str_seq2.substring(str_seq2.length() - 15, str_seq2.length());
					strTjxId = str_seq1 + str_seq2;
				}

				PsTzKsTjxTbl psTzKsTjxTbl = psTzKsTjxTblMapper.selectByPrimaryKey(strTjxId);
				if (psTzKsTjxTbl == null) {
					psTzKsTjxTbl = new PsTzKsTjxTbl();
					psTzKsTjxTbl.setTzRefLetterId(strTjxId);
					psTzKsTjxTbl.setTzAppInsId(numAppinsId);
					psTzKsTjxTbl.setOprid(strOprid);
					psTzKsTjxTbl.setTzTjxType(strTjxType);
					psTzKsTjxTbl.setTzTjrId(strTjrId);
					psTzKsTjxTbl.setTzMbaTjxYx(str_tjx_valid);
					psTzKsTjxTbl.setTzTjxTitle(strTitle);
					psTzKsTjxTbl.setTzReferrerGname(strGname);
					psTzKsTjxTbl.setTzReferrerName(strName);
					psTzKsTjxTbl.setTzCompCname(strCompany);
					psTzKsTjxTbl.setTzPosition(strPosition);
					psTzKsTjxTbl.setTzEmail(strEmail);
					psTzKsTjxTbl.setTzPhoneArea(strPhone_area);
					psTzKsTjxTbl.setTzPhone(strPhone_no);
					psTzKsTjxTbl.setTzGender(strGender);
					psTzKsTjxTbl.setTzReflettertype(str_refLetterType);

					psTzKsTjxTbl.setTzTjxYl1(strAdd1);
					psTzKsTjxTbl.setTzTjxYl2(strAdd2);
					psTzKsTjxTbl.setTzTjxYl3(strAdd3);
					psTzKsTjxTbl.setTzTjxYl4(strAdd4);
					psTzKsTjxTbl.setTzTjxYl5(strAdd5);
					psTzKsTjxTbl.setTzTjxYl6(strAdd6);
					psTzKsTjxTbl.setTzTjxYl7(strAdd7);
					psTzKsTjxTbl.setTzTjxYl8(strAdd8);
					psTzKsTjxTbl.setTzTjxYl9(strAdd9);
					psTzKsTjxTbl.setTzTjxYl10(strAdd10);
					psTzKsTjxTbl.setTzTjrGx(strTjrgx);

					psTzKsTjxTbl.setAttachsysfilename(str_sysfilename);
					psTzKsTjxTbl.setAttachuserfile(str_filename);
					psTzKsTjxTbl.setTzAccessPath(accessPath);
					psTzKsTjxTbl.setTzAttAUrl(attAUrl);

					psTzKsTjxTbl.setRowAddedDttm(new Date());
					psTzKsTjxTbl.setRowAddedOprid(strOprid);
					psTzKsTjxTbl.setRowLastmantDttm(new Date());
					psTzKsTjxTbl.setRowLastmantOprid(strOprid);
					psTzKsTjxTblMapper.insert(psTzKsTjxTbl);
				} else {
					psTzKsTjxTbl = new PsTzKsTjxTbl();
					psTzKsTjxTbl.setTzRefLetterId(strTjxId);
					psTzKsTjxTbl.setTzAppInsId(numAppinsId);
					psTzKsTjxTbl.setOprid(strOprid);
					psTzKsTjxTbl.setTzTjxType(strTjxType);
					psTzKsTjxTbl.setTzTjrId(strTjrId);
					psTzKsTjxTbl.setTzMbaTjxYx(str_tjx_valid);
					psTzKsTjxTbl.setTzTjxTitle(strTitle);
					psTzKsTjxTbl.setTzReferrerGname(strGname);
					psTzKsTjxTbl.setTzReferrerName(strName);
					psTzKsTjxTbl.setTzCompCname(strCompany);
					psTzKsTjxTbl.setTzPosition(strPosition);
					psTzKsTjxTbl.setTzEmail(strEmail);
					psTzKsTjxTbl.setTzPhoneArea(strPhone_area);
					psTzKsTjxTbl.setTzPhone(strPhone_no);
					psTzKsTjxTbl.setTzGender(strGender);
					psTzKsTjxTbl.setTzReflettertype(str_refLetterType);

					psTzKsTjxTbl.setTzTjxYl1(strAdd1);
					psTzKsTjxTbl.setTzTjxYl2(strAdd2);
					psTzKsTjxTbl.setTzTjxYl3(strAdd3);
					psTzKsTjxTbl.setTzTjxYl4(strAdd4);
					psTzKsTjxTbl.setTzTjxYl5(strAdd5);
					psTzKsTjxTbl.setTzTjxYl6(strAdd6);
					psTzKsTjxTbl.setTzTjxYl7(strAdd7);
					psTzKsTjxTbl.setTzTjxYl8(strAdd8);
					psTzKsTjxTbl.setTzTjxYl9(strAdd9);
					psTzKsTjxTbl.setTzTjxYl10(strAdd10);

					psTzKsTjxTbl.setTzTjrGx(strTjrgx);

					psTzKsTjxTbl.setAttachsysfilename(str_sysfilename);
					psTzKsTjxTbl.setAttachuserfile(str_filename);
					psTzKsTjxTbl.setTzAccessPath(accessPath);
					psTzKsTjxTbl.setTzAttAUrl(attAUrl);

					psTzKsTjxTbl.setRowLastmantDttm(new Date());
					psTzKsTjxTbl.setRowLastmantOprid(strOprid);
					psTzKsTjxTblMapper.updateByPrimaryKeySelective(psTzKsTjxTbl);
				}
			}
			strRtn = "SUCCESS";
		}

		return strRtn;
	}

	// 更换推荐人信息;
	public String changeTJR(long numAppinsId, String strTjrId, String strOprid) {
		String strRtn = "";
		String str_language = "";
		if (numAppinsId > 0 && (strTjrId != null && !"".equals(strTjrId))
				&& (strOprid != null && !"".equals(strOprid))) {
			// 判断该推荐人的推荐信是否已提交;
			String strTjxState = jdbcTemplate.queryForObject(
					"select B.TZ_APP_FORM_STA from PS_TZ_KS_TJX_TBL A,PS_TZ_APP_INS_T B where A.TZ_APP_INS_ID=? AND A.TZ_TJR_ID=? AND A.OPRID=? AND A.TZ_MBA_TJX_YX='Y' AND A.TZ_TJX_APP_INS_ID=B.TZ_APP_INS_ID limit 0,1",
					new Object[] { numAppinsId, strTjrId, strOprid }, "String");
			str_language = jdbcTemplate.queryForObject(
					"SELECT B.TZ_APP_TPL_LAN FROM PS_TZ_APP_INS_T A,PS_TZ_APPTPL_DY_T B WHERE A.TZ_APP_INS_ID=? AND A.TZ_APP_TPL_ID=B.TZ_APP_TPL_ID",
					new Object[] { numAppinsId }, "String");

			if ("U".equals(strTjxState)) {
				// 推荐信已提交;
				strRtn = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_APPONLINE_MSGSET", "REF_NO_C",
						str_language, "", "");
			} else {
				// 设置推荐信失效;
				jdbcTemplate.update(
						"update PS_TZ_KS_TJX_TBL A set TZ_MBA_TJX_YX='N' where  A.TZ_APP_INS_ID=? AND A.TZ_TJR_ID=? AND A.OPRID=?",
						new Object[] { numAppinsId, strTjrId, strOprid });
				strRtn = "SUCCESS";
			}
		} else {
			// 参数不完整;
			strRtn = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_APPONLINE_MSGSET", "P_INCOM",
					str_language, "", "");
		}
		return strRtn;

	}

	// 记录推荐信发送历史
	public void sendTJXLog(long numAppinsId, String strOprid, String strTjrId, String strEmail, String TZ_FS_ZT) {
		// N:发送给自己 Y：发送给推荐人
		String TZ_TJX_SEND_ID = String.valueOf(getSeqNum.getSeqNum("TZ_TJX_FSRZ_TBL", "TZ_TJX_SEND_ID"));

		String sql = "insert into PS_TZ_TJX_FSRZ_TBL values (?,?,?,now(),?,?)";
		jdbcTemplate.update(sql, new Object[] { TZ_TJX_SEND_ID, numAppinsId, strOprid, TZ_FS_ZT, strEmail });
	}

	/**
	 * 发送推荐信 后发送站内信
	 * 
	 * @param numAppInsId
	 * @param siteEmailID
	 * @param strAudienceDesc
	 * @param strAudLy
	 * @return
	 */
	public String sendSiteEmail(long numAppInsId, String siteEmailID, String strAudienceDesc, String strAudLy) {
		String sql = "";

		sql = "SELECT OPRID FROM PS_TZ_KS_TJX_TBL WHERE TZ_APP_INS_ID=?";
		String strAppOprId = jdbcTemplate.queryForObject(sql, new Object[] { String.valueOf(numAppInsId) }, "String");

		sql = "SELECT TZ_JG_ID FROM PS_TZ_APPTPL_DY_T A,PS_TZ_APP_INS_T B WHERE A.TZ_APP_TPL_ID=B.TZ_APP_TPL_ID AND B.TZ_APP_INS_ID=?";
		String strAppOrgId = jdbcTemplate.queryForObject(sql, new Object[] { String.valueOf(numAppInsId) }, "String");
		System.out.println("numAppInsId:" + numAppInsId);
		System.out.println("strAppOprId:" + strAppOprId);
		System.out.println("strAppOrgId:" + strAppOrgId);

		String returnMsg = "true";
		// 收件人姓名
		String strName = "";
		sql = "SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=?";
		strName = jdbcTemplate.queryForObject(sql, new Object[] { strAppOprId }, "String");

		// 创建站内信发送任务 创建任务的时候，类型为“ZNX”， oprid是收站内信的人。 手机和邮箱为空字符串就可以了
		String strTaskId = createTaskServiceImpl.createTaskIns(strAppOrgId, siteEmailID, "ZNX", "A");
		if (strTaskId == null || "".equals(strTaskId)) {
			return "false";
		}
		// 创建听众;
		String createAudience = createTaskServiceImpl.createAudience(strTaskId, strAppOrgId, strAudienceDesc, strAudLy);
		if ("".equals(createAudience) || createAudience == null) {
			return "false";
		}
		// 为听众添加听众成员
		boolean addAudCy = createTaskServiceImpl.addAudCy(createAudience, strName, strName, "", "", "", "", "",
				strAppOprId, "", "", String.valueOf(numAppInsId));
		if (!addAudCy) {
			return "false";
		}
		// 得到创建的任务ID
		if ("".equals(strTaskId) || strTaskId == null) {
			return "false";
		} else {
			// 发送
			sendSmsOrMalServiceImpl.send(strTaskId, "");
		}

		return returnMsg;
	}

	// 发送推荐信邮件;
	public String sendTJX(long numAppinsId, String strOprid, String strTjrId, String sendFlag) {
		String strRtn = "";
		// String strMyName = "";
		String str_opremail = "";
		Map<String, Object> yhxxMap = jdbcTemplate.queryForMap(
				"select TZ_REALNAME,TZ_EMAIL from PS_TZ_AQ_YHXX_TBL where OPRID=? limit 0,1",
				new Object[] { strOprid });
		if (yhxxMap != null) {
			// strMyName = (String)yhxxMap.get("TZ_REALNAME");
			str_opremail = (String) yhxxMap.get("TZ_EMAIL");
		}
		// String strTjxId = "";
		String str_tjr_gname = "";
		String str_tjr_title = "";
		String str_name_suff = "";

		String strName = "";
		// String strGender = "";
		String strEmail = "";
		String strTjxType = "";
		Map<String, Object> ksTjxMap = jdbcTemplate.queryForMap(
				"select TZ_REF_LETTER_ID,TZ_TJX_TITLE,TZ_REFERRER_NAME,TZ_REFERRER_GNAME,TZ_GENDER,TZ_EMAIL,TZ_TJX_TYPE from PS_TZ_KS_TJX_TBL where TZ_APP_INS_ID=? and TZ_TJR_ID=? and OPRID=? and TZ_MBA_TJX_YX='Y'",
				new Object[] { numAppinsId, strTjrId, strOprid });
		if (ksTjxMap != null) {
			// strTjxId = (String)ksTjxMap.get("TZ_REF_LETTER_ID");
			str_tjr_title = (String) ksTjxMap.get("TZ_TJX_TITLE");
			strName = (String) ksTjxMap.get("TZ_REFERRER_NAME");
			str_tjr_gname = (String) ksTjxMap.get("TZ_REFERRER_GNAME");
			// strGender = (String)ksTjxMap.get("TZ_GENDER");
			strEmail = (String) ksTjxMap.get("TZ_EMAIL");
			strTjxType = (String) ksTjxMap.get("TZ_TJX_TYPE");
		}

		String str_none_blank = jdbcTemplate.queryForObject(
				"SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT = ?",
				new Object[] { "TZ_REF_TITLE_NONE_BLANK" }, "String");
		String str_none_blank_c = jdbcTemplate.queryForObject(
				"SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT = ?",
				new Object[] { "TZ_REF_TITLE_NONE_BLANK_C" }, "String");

		if (str_tjr_title != null && !"".equals(str_tjr_title) && !str_tjr_title.equals(str_none_blank)
				&& !str_tjr_title.equals(str_none_blank_c)) {
			str_name_suff = str_tjr_title;
		}

		if (str_tjr_gname != null && !"".equals(str_tjr_gname)) {
			if (str_name_suff != null && !"".equals(str_name_suff)) {
				str_name_suff = str_name_suff + " " + str_tjr_gname;
			} else {
				str_name_suff = str_tjr_gname;
			}
		}

		if (str_name_suff != null && !"".equals(str_name_suff)) {
			strName = str_name_suff + " " + strName;
		}

		// 报名表模板 机构ID;
		String strJgid = "";
		// String strCmodal = "";
		// String strEmodal = "";
		String strCcontent = "";
		String strEcontent = "";

		String strExistFlg = "";
		Map<String, Object> map = jdbcTemplate.queryForMap(
				"select 'Y' TZ_EXIST,TZ_JG_ID,TZ_CHN_MODAL_ID,TZ_ENG_MODAL_ID,TZ_CH_M_CONTENT,TZ_EN_M_CONTENT from PS_TZ_APP_INS_T A,PS_TZ_APPTPL_DY_T B WHERE A.TZ_APP_TPL_ID=B.TZ_APP_TPL_ID AND A.TZ_APP_INS_ID=? LIMIT 0,1",
				new Object[] { numAppinsId });
		if (map != null) {
			strExistFlg = (String) map.get("TZ_EXIST");
			strJgid = (String) map.get("TZ_JG_ID");
			// strCmodal = (String)map.get("TZ_CHN_MODAL_ID");
			// strEmodal = (String)map.get("TZ_ENG_MODAL_ID");
			strCcontent = (String) map.get("TZ_CH_M_CONTENT");
			strEcontent = (String) map.get("TZ_EN_M_CONTENT");
		}
		String str_language = jdbcTemplate.queryForObject(
				"SELECT B.TZ_APP_TPL_LAN FROM PS_TZ_APP_INS_T A,PS_TZ_APPTPL_DY_T B WHERE A.TZ_APP_INS_ID=? AND A.TZ_APP_TPL_ID=B.TZ_APP_TPL_ID limit 0,1",
				new Object[] { numAppinsId }, "String");
		String mess = "";

		// System.out.println("str_opremail:" + str_opremail);
		// System.out.println("strEmail:" + strEmail);
		if (str_opremail != null && str_opremail.equals(strEmail)) {
			mess = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_APPONLINE_MSGSET", "SAME_EMAIL",
					str_language, "", "");
			return mess;
		}

		if (!"Y".equals(strExistFlg)) {
			strRtn = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_APPONLINE_MSGSET", "NO_INS_ID",
					str_language, "", "");
			return strRtn;
		}

		// 跳转到推荐信报名表的链接;
		// String strTzUrl = "http://" + request.getServerName() + ":" +
		// request.getServerPort() + request.getContextPath() + "/dispatcher";
		// 需要配置hardcode点;
		String str_email_mb = "";
		if ("E".equals(strTjxType)) {
			// 英文;
			str_email_mb = strEcontent;
		} else {
			// 中文;
			str_email_mb = strCcontent;
		}

		// 给邮箱发送邮件---开始;
		// 创建邮件短信发送任务;
		String taskId = createTaskServiceImpl.createTaskIns(strJgid, str_email_mb, "MAL", "A");
		// System.out.println("taskId:" + taskId);
		if (taskId == null || "".equals(taskId)) {
			mess = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_APPONLINE_MSGSET", "CR_E_FAIL",
					str_language, "", "");
			return mess;
		}

		// 创建短信、邮件发送的听众;

		String createAudience = createTaskServiceImpl.createAudience(taskId, strJgid, "推荐信", "TJX");
		// System.out.println("createAudience:" + createAudience);
		if (createAudience == null || "".equals(createAudience)) {
			mess = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_APPONLINE_MSGSET", "CR_L_FAIL",
					str_language, "", "");
			return mess;
		}

		// N:发送给自己 Y：发送给推荐人
		if (sendFlag.equals("N")) {
			strEmail = jdbcTemplate.queryForObject(
					"SELECT TZ_ZY_EMAIL FROM PS_TZ_LXFSINFO_TBL WHERE TZ_LXFS_LY='ZCYH' AND TZ_LYDX_ID=?",
					new Object[] { strOprid }, "String");
		}

		// System.out.println("strEmail:" + strEmail);
		// 为听众添加听众成员 modity by caoy 应为需要 转发给自己，所以需要在听众表里面 填入 strTjrId
		boolean addAudCy = createTaskServiceImpl.addAudCy(createAudience, strName, strName, "", "", strEmail, "", "",
				strOprid, "", strTjrId, String.valueOf(numAppinsId));
		if (addAudCy == false) {
			mess = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_APPONLINE_MSGSET", "ADD_L_FAIL",
					str_language, "", "");
			return mess;
		}

		if (taskId == null || "".equals(taskId)) {
			mess = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_APPONLINE_MSGSET", "CR_ID_FAIL",
					str_language, "", "");
			return mess;
		}

		sendSmsOrMalServiceImpl.send(taskId, "");

		mess = "SUCCESS";
		return mess;
	}

	// 发送邮件通知给申请人;
	public String sendTZ(long numAppinsId, String strOprid, String strTjrId) {
		String strRtn = "";
		// String strMyName = "";
		String str_opremail = "";
		Map<String, Object> yhxxMap = jdbcTemplate.queryForMap(
				"select TZ_REALNAME,TZ_EMAIL from PS_TZ_AQ_YHXX_TBL where OPRID=? limit 0,1",
				new Object[] { strOprid });
		if (yhxxMap != null) {
			// strMyName = (String)yhxxMap.get("TZ_REALNAME");
			str_opremail = (String) yhxxMap.get("TZ_EMAIL");
		}
		// String strTjxId = "";
		String str_tjr_gname = "";
		String str_tjr_title = "";
		String str_name_suff = "";

		String strName = "";
		// String strGender = "";
		String strEmail = "";
		String strTjxType = "";
		Map<String, Object> ksTjxMap = jdbcTemplate.queryForMap(
				"select TZ_REF_LETTER_ID,TZ_TJX_TITLE,TZ_REFERRER_NAME,TZ_REFERRER_GNAME,TZ_GENDER,TZ_EMAIL,TZ_TJX_TYPE from PS_TZ_KS_TJX_TBL where TZ_APP_INS_ID=? and TZ_TJR_ID=? and OPRID=? and TZ_MBA_TJX_YX='Y'",
				new Object[] { numAppinsId, strTjrId, strOprid });
		if (ksTjxMap != null) {
			// strTjxId = (String)ksTjxMap.get("TZ_REF_LETTER_ID");
			str_tjr_title = (String) ksTjxMap.get("TZ_TJX_TITLE");
			strName = (String) ksTjxMap.get("TZ_REFERRER_NAME");
			str_tjr_gname = (String) ksTjxMap.get("TZ_REFERRER_GNAME");
			// strGender = (String)ksTjxMap.get("TZ_GENDER");
			strEmail = (String) ksTjxMap.get("TZ_EMAIL");
			strTjxType = (String) ksTjxMap.get("TZ_TJX_TYPE");
		}

		String str_none_blank = jdbcTemplate.queryForObject(
				"SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT = ?",
				new Object[] { "TZ_REF_TITLE_NONE_BLANK" }, "String");
		String str_none_blank_c = jdbcTemplate.queryForObject(
				"SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT = ?",
				new Object[] { "TZ_REF_TITLE_NONE_BLANK_C" }, "String");

		if (str_tjr_title != null && !"".equals(str_tjr_title) && !str_tjr_title.equals(str_none_blank)
				&& !str_tjr_title.equals(str_none_blank_c)) {
			str_name_suff = str_tjr_title;
		}

		if (str_tjr_gname != null && !"".equals(str_tjr_gname)) {
			if (str_name_suff != null && !"".equals(str_name_suff)) {
				str_name_suff = str_name_suff + " " + str_tjr_gname;
			} else {
				str_name_suff = str_tjr_gname;
			}
		}

		if (str_name_suff != null && !"".equals(str_name_suff)) {
			strName = str_name_suff + " " + strName;
		}

		// 报名表模板 机构ID;
		String strJgid = "";
		// String strCmodal = "";
		// String strEmodal = "";
		// String strCcontent = "";
		// String strEcontent = "";

		String strExistFlg = "";
		Map<String, Object> map = jdbcTemplate.queryForMap(
				"select 'Y' TZ_EXIST,TZ_JG_ID,TZ_CHN_MODAL_ID,TZ_ENG_MODAL_ID,TZ_CH_M_CONTENT,TZ_EN_M_CONTENT from PS_TZ_APP_INS_T A,PS_TZ_APPTPL_DY_T B WHERE A.TZ_APP_TPL_ID=B.TZ_APP_TPL_ID AND A.TZ_APP_INS_ID=? LIMIT 0,1",
				new Object[] { numAppinsId });
		if (map != null) {
			strExistFlg = (String) map.get("TZ_EXIST");
			strJgid = (String) map.get("TZ_JG_ID");
			// strCmodal = (String)map.get("TZ_CHN_MODAL_ID");
			// strEmodal = (String)map.get("TZ_ENG_MODAL_ID");
			// strCcontent = (String)map.get("TZ_CH_M_CONTENT");
			// strEcontent = (String)map.get("TZ_EN_M_CONTENT");
		}
		String str_language = jdbcTemplate.queryForObject(
				"SELECT B.TZ_APP_TPL_LAN FROM PS_TZ_APP_INS_T A,PS_TZ_APPTPL_DY_T B WHERE A.TZ_APP_INS_ID=? AND A.TZ_APP_TPL_ID=B.TZ_APP_TPL_ID limit 0,1",
				new Object[] { numAppinsId }, "String");
		String mess = "";
		if (str_opremail != null && str_opremail.equals(strEmail)) {
			mess = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_APPONLINE_MSGSET", "SAME_EMAIL",
					str_language, "", "");
			return mess;
		}

		if (!"Y".equals(strExistFlg)) {
			strRtn = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_APPONLINE_MSGSET", "NO_INS_ID",
					str_language, "", "");
			return strRtn;
		}

		// 邮箱更改为申请人的邮箱;
		strEmail = jdbcTemplate.queryForObject(
				"SELECT TZ_ZY_EMAIL FROM PS_TZ_LXFSINFO_TBL WHERE TZ_LXFS_LY='ZCYH' AND TZ_LYDX_ID=?",
				new Object[] { strOprid }, "String");

		// 跳转到推荐信报名表的链接;
		// String strTzUrl = "http://" + request.getServerName() + ":" +
		// request.getServerPort() + request.getContextPath() + "/dispatcher";
		// 需要配置hardcode点;
		String str_tx_email_id = "";
		if ("E".equals(strTjxType)) {
			// 英文;
			str_tx_email_id = "TZ_REC_MAL_T_E";
		} else {
			// 中文;
			str_tx_email_id = "TZ_REC_MAL_T_C";
		}

		// 给邮箱发送邮件---开始;
		// 创建邮件短信发送任务;
		String taskId = createTaskServiceImpl.createTaskIns(strJgid, str_tx_email_id, "MAL", "A");
		if (taskId == null || "".equals(taskId)) {
			mess = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_APPONLINE_MSGSET", "CR_E_FAIL",
					str_language, "", "");
			return mess;
		}

		// 创建短信、邮件发送的听众;
		String createAudience = createTaskServiceImpl.createAudience(taskId, strJgid, "推荐信", "TJX");
		if (createAudience == null || "".equals(createAudience)) {
			mess = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_APPONLINE_MSGSET", "CR_L_FAIL",
					str_language, "", "");
			return mess;
		}

		// 为听众添加听众成员;
		boolean addAudCy = createTaskServiceImpl.addAudCy(createAudience, strName, strName, "", "", strEmail, "", "",
				strOprid, "", strTjrId, String.valueOf(numAppinsId));
		if (addAudCy == false) {
			mess = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_APPONLINE_MSGSET", "ADD_L_FAIL",
					str_language, "", "");
			return mess;
		}

		// 得到创建的任务ID;
		if (taskId == null || "".equals(taskId)) {
			mess = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_APPONLINE_MSGSET", "CR_ID_FAIL",
					str_language, "", "");
			return mess;
		}

		sendSmsOrMalServiceImpl.send(taskId, "");
		mess = "SUCCESS";

		return mess;
	}
}
