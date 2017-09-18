package com.tranzvision.gd.TZWebsiteApplicationBundle.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdKjComServiceImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.CreateTaskServiceImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.SendSmsOrMalServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.MessageTextServiceImpl;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.encrypt.Sha3DesMD5;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/*
 * @Author raosheng
 * 用于推荐信“忘记密码” 
 * */
@Service("com.tranzvision.gd.TZWebsiteApplicationBundle.service.impl.tzResetPasswordImpl")
public class tzResetPasswordImpl extends FrameworkImpl {
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private SqlQuery jdbcTemplate;

	@Autowired
	private TZGDObject tzSQLObject;

	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	// 邮件发送参数注入:
	@Autowired
	private CreateTaskServiceImpl createTaskServiceImpl;

	@Autowired
	private MessageTextServiceImpl messageTextServiceImpl;

	@Autowired
	private SendSmsOrMalServiceImpl sendSmsOrMalServiceImpl;

	@Autowired
	private GdKjComServiceImpl gdKjComServiceImpl;

	// private static String page_stylecss;
	// 一.从推荐信填写"忘记密码"页面,发送邮件:
	@Override
	public String tzOther(String oprType, String strParams, String[] errorMsg) {
		// System.out.println("进入com.tranzvision.gd.TZWebsiteApplicationBundle.service.impl.tzResetPasswordImpl的tzOther()方法");
		// System.out.println("strParams:"+strParams);
		// 返回值:
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", "0");
		map.put("msg", "");
		try {
			// 1.取出参数数据
			JacksonUtil jacksonUtil = new JacksonUtil();
			Map<String, Object> paramMap = new HashMap<String, Object>();
			jacksonUtil.json2Map(strParams);
			paramMap = jacksonUtil.getMap();
			// 2.发送邮件
			if (oprType.equals("RESET_TJX_PASS")) {
				// 2.1推荐信ID(主键)
				String letterId = paramMap.get("letterId").toString();
				String msg = "";
				String strJgid = "";
				String language = "";
				String strOprid = "";
				String str_tjr_title = "";
				String strName = "";
				String str_tjr_gname = "";
				String strEmail = "";
				String strTjrId = "";
				String numAppinsId="";

				StringBuffer sb = new StringBuffer();
				sb.append(
						"SELECT A.TZ_JG_ID,A.TZ_APP_TPL_LAN,C.OPRID,C.TZ_REFERRER_NAME,C.TZ_EMAIL,C.TZ_TJX_TITLE,C.TZ_REFERRER_GNAME,C.TZ_TJR_ID,C.TZ_APP_INS_ID ");
				sb.append("FROM PS_TZ_APPTPL_DY_T A,PS_TZ_APP_INS_T B,PS_TZ_KS_TJX_TBL C ");
				sb.append("WHERE A.TZ_APP_TPL_ID=B.TZ_APP_TPL_ID ");
				sb.append("AND B.TZ_APP_INS_ID=C.TZ_TJX_APP_INS_ID ");
				sb.append("AND C.TZ_REF_LETTER_ID=? ");

				Map<String, Object> sysMap = new HashMap<String, Object>();
				sysMap = jdbcTemplate.queryForMap(sb.toString(), new Object[] { letterId });
				if (sysMap != null) {
					strJgid = sysMap.get("TZ_JG_ID").toString();
					language = sysMap.get("TZ_APP_TPL_LAN").toString();
					strOprid = sysMap.get("OPRID").toString();
					str_tjr_title = (String) sysMap.get("TZ_TJX_TITLE");
					strName = (String) sysMap.get("TZ_REFERRER_NAME");
					str_tjr_gname = (String) sysMap.get("TZ_REFERRER_GNAME");
					// strGender = (String)ksTjxMap.get("TZ_GENDER");
					strEmail = (String) sysMap.get("TZ_EMAIL");
					strTjrId = (String) sysMap.get("TZ_TJR_ID");
					numAppinsId = String.valueOf(sysMap.get("TZ_APP_INS_ID"));
				}
				
				
				
				String str_name_suff = "";
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

				if (language == null || language.equals("")) {
					language = "ZHS";
				}

				String str_email_mb = "";
				// 邮件模版编号写死
				if ("ZHS".equals(language)) {
					// 中文
					str_email_mb = "TZ_TJXPAS_C";
				} else {
					// 英文
					str_email_mb = "TZ_TJXPAS_E";
				}
				
				
				System.out.println("strJgid:"+strJgid);
				System.out.println("str_email_mb:"+str_email_mb);
				
				// 给邮箱发送邮件---开始;
				// 创建邮件短信发送任务;
				String taskId = createTaskServiceImpl.createTaskIns(strJgid, str_email_mb, "MAL", "A");
				System.out.println("taskId:"+taskId);
				// System.out.println("taskId:" + taskId);
				if (taskId == null || "".equals(taskId)) {
					map.replace("success", "1");
					msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_DATA_REQUEST_MSG", "7", language,
							"发送失败", "发送失败");
					map.replace("msg", msg);
					return jacksonUtil.Map2json(map);
				}

				// 创建短信、邮件发送的听众;

				String createAudience = createTaskServiceImpl.createAudience(taskId, strJgid, "推荐信密码重置", "TJXP");
				// System.out.println("createAudience:" + createAudience);
				System.out.println("createAudience:"+createAudience);
				if (createAudience == null || "".equals(createAudience)) {
					map.replace("success", "1");
					msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_DATA_REQUEST_MSG", "7", language,
							"发送失败", "发送失败");
					map.replace("msg", msg);
					return jacksonUtil.Map2json(map);
				}

				// System.out.println("strEmail:" + strEmail);
				// 为听众添加听众成员 modity by caoy 应为需要 转发给自己，所以需要在听众表里面 填入 strTjrId
				boolean addAudCy = createTaskServiceImpl.addAudCy(createAudience, strName, strName, "", "", strEmail,
						"", "", strOprid, "", strTjrId, numAppinsId);
				System.out.println("addAudCy:"+addAudCy);
				if (addAudCy == false) {
					map.replace("success", "1");
					msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_DATA_REQUEST_MSG", "7",
							language, "发送失败", "发送失败");
					map.replace("msg", msg);
				}


				sendSmsOrMalServiceImpl.send(taskId, "");



				// 2.2查询 考生ID和 推荐人邮件地址:
//				String sql = "SELECT TZ_TJX_APP_INS_ID,OPRID,TZ_EMAIL,TZ_REFERRER_NAME FROM PS_TZ_KS_TJX_TBL WHERE TZ_REF_LETTER_ID=?";
//				Map<String, Object> userData = jdbcTemplate.queryForMap(sql, new Object[] { letterId });
//				// String userId=userData.get("OPRID").toString();
//				String tjrEmail = userData.get("TZ_EMAIL").toString();
//				String tjrName = userData.get("TZ_REFERRER_NAME").toString();
//				String tjxInsId = userData.get("TZ_TJX_APP_INS_ID").toString();
//
//
//				// 2.4推荐人名称:
//				// 2.5发送邮件:
//				String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
//						+ request.getContextPath() + "/dispatcher?classid=resetTjxPass&letterId=" + letterId;
//
//				String content = "<p>" + tjrName + "，您好！</p>"
//				// +"<p>您收到此封邮件是因为"+userName+"选择您作为他/她的推荐人，重置推荐信密码。</p>"
//						+ "<p>请您复制以下地址，并使用IE9或以上版本浏览器打开，重置推荐信密码。<a href='" + url + "'> " + url + "</a></p>"
//						+ "<p>如不是本人操作，请忽略本邮件。</p>";
//
//				// 推荐信模版ID 在报名表实例表中:
//				sql = "SELECT TZ_APP_TPL_ID FROM PS_TZ_APP_INS_T WHERE TZ_APP_INS_ID=?";
//				String tjxTpl = jdbcTemplate.queryForObject(sql, new Object[] { tjxInsId }, "String");
//				// 机构ID 系统语言，用推荐信模版语言PS_TZ_APPTPL_DY_T
//				sql = "SELECT TZ_JG_ID,TZ_APP_TPL_LAN FROM PS_TZ_APPTPL_DY_T WHERE TZ_APP_TPL_ID=?";
//				Map<String, Object> sysMap = new HashMap<String, Object>();
//				sysMap = jdbcTemplate.queryForMap(sql, new Object[] { tjxTpl });
//
//
//				String strJgid = sysMap.get("TZ_JG_ID").toString();
//				String language = sysMap.get("TZ_APP_TPL_LAN").toString();
//				String taskId = createTaskServiceImpl.createTaskIns(strJgid, "TZ_EML_N_001", "MAL", "A");
//				String msg = "";
//				if (taskId == null || "".equals(taskId)) {
//					map.replace("success", "1");
//					msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_DATA_REQUEST_MSG", "7", language,
//							"发送失败", "发送失败");
//					map.replace("msg", msg);
//					return jacksonUtil.Map2json(map);
//				}
//
//				// 创建短信、邮件发送的听众;
//				String createAudience = createTaskServiceImpl.createAudience(taskId, strJgid, "高端产品用户邮箱修改", "JSRW");
//				if (createAudience == null || "".equals(createAudience)) {
//					map.replace("success", "1");
//					msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_DATA_REQUEST_MSG", "7", language,
//							"发送失败", "发送失败");
//					map.replace("msg", msg);
//					return jacksonUtil.Map2json(map);
//				}
//
//				// 为听众添加听众成员;
//				int sendNum = 0;
//				String[] arr = tjrEmail.split(";");
//				for (int i = 0; i < arr.length; i++) {
//					String sjrEmail = arr[i];
//					if (sjrEmail != null && !"".equals(sjrEmail)) {
//						boolean addAudCy = createTaskServiceImpl.addAudCy(createAudience, "管理员", "管理员", "", "",
//								sjrEmail, "", "", "", "", "", "");
//						if (addAudCy == false) {
//							map.replace("success", "1");
//							msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_DATA_REQUEST_MSG", "7",
//									language, "发送失败", "发送失败");
//							map.replace("msg", msg);
//							return jacksonUtil.Map2json(map);
//						} else {
//							sendNum++;
//						}
//					}
//				}
//
//				if (sendNum == 0) {
//					map.replace("success", "1");
//					msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_DATA_REQUEST_MSG", "9", language,
//							"收件人为空", "收件人为空");
//					map.replace("msg", msg);
//
//					return jacksonUtil.Map2json(map);
//				}
//
//				// 修改主题;
//				boolean bl = createTaskServiceImpl.updateEmailSendTitle(taskId, "重置推荐信密码");
//				if (bl == false) {
//					map.replace("success", "1");
//					msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_DATA_REQUEST_MSG", "7", language,
//							"发送失败", "发送失败");
//					map.replace("msg", msg);
//
//					return jacksonUtil.Map2json(map);
//				}
//
//				// 修改内容;
//				bl = createTaskServiceImpl.updateEmailSendContent(taskId, content);
//				if (bl == false) {
//					map.replace("success", "1");
//					msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_DATA_REQUEST_MSG", "7", language,
//							"发送失败", "发送失败");
//					map.replace("msg", msg);
//
//					return jacksonUtil.Map2json(map);
//				}
//
//				// 得到创建的任务ID;
//				if (taskId == null || "".equals(taskId)) {
//					map.replace("success", "1");
//					msg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_DATA_REQUEST_MSG", "7", language,
//							"发送失败", "发送失败");
//					map.replace("msg", msg);
//
//					return jacksonUtil.Map2json(map);
//				}
//				sendSmsOrMalServiceImpl.send(taskId, "");
				return jacksonUtil.Map2json(map);
			}

		} catch (Exception e) {
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
			e.printStackTrace();
		}

		return null;
	}

	// 二.从邮件链接到"修改密码"页面:
	@Override
	public String tzGetHtmlContent(String strParams) {
		// System.out.println("进入com.tranzvision.gd.TZWebsiteApplicationBundle.service.impl.tzResetPasswordImpl的tzGetHtmlContent()方法");
		// System.out.println("strParams:"+strParams);
		String letterId = request.getParameter("letterId").toString();

		String sql = "SELECT TZ_TJX_APP_INS_ID FROM PS_TZ_KS_TJX_TBL WHERE TZ_REF_LETTER_ID=?";
		String letterInsId = jdbcTemplate.queryForObject(sql, new Object[] { letterId }, "String");

		// 根据推荐信ID得到 推荐信模版 语言
		sql = "select A.TZ_APP_TPL_LAN from PS_TZ_APPTPL_DY_T A,PS_TZ_APP_INS_T B WHERE A.TZ_APP_TPL_ID=B.TZ_APP_TPL_ID AND B.TZ_APP_INS_ID=?";
		String strLanguage = jdbcTemplate.queryForObject(sql, new Object[] { letterInsId }, "String");

		String moityPWD = gdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET",
				"MODITYTJXPWD", strLanguage, "修改密码", "Modify password");

		String pwdError = gdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET",
				"TJXSETPWDError", strLanguage, "请填写密码", "Please fill in the password");

		String pwdError2 = gdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET",
				"TJXSETPWDError", strLanguage, "密码和确认密码不一致", "Password and confirm password inconsistent");

		String setPwd = gdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET", "TJXSETPWD",
				strLanguage, "设置密码", "Set Password");
		String setPwd2 = gdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET", "TJXSETPWD2",
				strLanguage, "重新输入密码", "Re Password");

		String strSubmit = gdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET", "CONFIRM",
				strLanguage, "确认", "Confirm");

		String strPWDSTR = gdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET", "PWDSTR",
				strLanguage, "密码强度", "Password strength");

		String strPWDSTRtitle1 = gdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET",
				"PWDSTRT1", strLanguage, "6-32个字符", "6-32 characters");
		String strPWDSTRtitle2 = gdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET",
				"PWDSTRT2", strLanguage, "只能包含字母、数字以及下划线", "Only letters, numbers, and underscores can be included");
		String strPWDSTRtitle3 = gdKjComServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPONLINE_MSGSET",
				"PWDSTRT3", strLanguage, "字母、数字和下划线至少包含2种", "Letters, numbers, and underscores contain at least 2");

		// System.out.println("letterId:"+letterId);
		String url = request.getContextPath();
		String generalUrl = request.getContextPath() + "/dispatcher";
		try {
			String html = tzSQLObject.getHTMLText("HTML.TZWebsiteApplicationBundle.TZ_ONLINE_PAGE_RESET_PASS_HTML",
					moityPWD, setPwd, setPwd2, pwdError, pwdError2, strSubmit, strPWDSTR, strPWDSTRtitle1,
					strPWDSTRtitle2, strPWDSTRtitle3, url, generalUrl, letterId, letterInsId);
			// System.out.println("样式路径page_stylecss:"+page_stylecss);
			sql = "SELECT TZ_APP_INS_ID FROM PS_TZ_KS_TJX_TBL WHERE TZ_REF_LETTER_ID=?";
			// 报名表InsId
			String insId = jdbcTemplate.queryForObject(sql, new Object[] { letterId }, "String");
			// 班级Id:
			sql = "SELECT TZ_CLASS_ID FROM PS_TZ_FORM_WRK_T WHERE TZ_APP_INS_ID=?";
			String clsssId = jdbcTemplate.queryForObject(sql, new Object[] { insId }, "String");
			// 机构ID 项目ID:
			sql = "SELECT TZ_JG_ID,TZ_PRJ_ID FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID=?";
			Map<String, Object> prjMap = jdbcTemplate.queryForMap(sql, new Object[] { clsssId });
			String orgid = prjMap.get("TZ_JG_ID").toString();
			String prjId = prjMap.get("TZ_PRJ_ID").toString();
			// 站点ID:
			sql = "SELECT TZ_SITEI_ID FROM PS_TZ_PROJECT_SITE_T WHERE TZ_PRJ_ID=?";
			String siteId = jdbcTemplate.queryForObject(sql, new Object[] { prjId }, "String");

			orgid = orgid.toLowerCase();
			// <link
			// href="/statics/css/website/orgs/sem/72/style_sem.css?v=8.448362827488486"
			// rel="stylesheet" type="text/css" />
			String strCssDir = getSysHardCodeVal.getWebsiteCssPath() + "/" + orgid + "/" + siteId.toLowerCase();
			String strCssFilePath = strCssDir + "/style_" + orgid + ".css";
			html = html.replace("page_stylecss", strCssFilePath);
			return html;
		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;
	}

	// 三.页面重置密码“修改密码"到数据库":
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		// System.out.println("进入com.tranzvision.gd.TZWebsiteApplicationBundle.service.impl.tzResetPasswordImpl的tzUpdate()方法");
		// System.out.println("actData[0]"+actData[0]);
		Map<String, Object> returnMap = new HashMap<String, Object>();
		try {
			JacksonUtil jacksonUtil = new JacksonUtil();
			Map<String, Object> dataMap = new HashMap<String, Object>();
			jacksonUtil.json2Map(actData[0]);
			dataMap = jacksonUtil.getMap();
			String letterId = dataMap.get("letterId").toString();
			String password = dataMap.get("password").toString();
			password = Sha3DesMD5.md5(password);

			// 查询推荐信表实例ID:
			// String sql="SELECT TZ_APP_INS_ID FROM PS_TZ_KS_TJX_TBL WHERE
			// TZ_REF_LETTER_ID=?";
			String sql = "SELECT TZ_TJX_APP_INS_ID FROM PS_TZ_KS_TJX_TBL WHERE TZ_REF_LETTER_ID=?";
			String tjxInsId = jdbcTemplate.queryForObject(sql, new Object[] { letterId }, "String");
			sql = "UPDATE PS_TZ_APP_INS_T SET TZ_PWD=? WHERE TZ_APP_INS_ID=?";
			jdbcTemplate.update(sql, new Object[] { password, tjxInsId });
			// System.out.println("修改成功");
			returnMap.put("result", "success");
			// classid=appId&TZ_APP_INS_ID=357&TZ_REF_LETTER_ID=9256956000000000000251#
			return jacksonUtil.Map2json(returnMap);
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			e.printStackTrace();
			return null;
		}
	}

}
