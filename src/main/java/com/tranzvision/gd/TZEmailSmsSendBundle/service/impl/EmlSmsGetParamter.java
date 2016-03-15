package com.tranzvision.gd.TZEmailSmsSendBundle.service.impl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.tranzvision.gd.util.base.GetSpringBeanUtil;

/**
 * PS类: TZ_GD_COM_EMLSMS_APP:emlSmsGetParamter
 * 
 * @author tang 邮件短信获得公共参数的功能
 * 
 */
public class EmlSmsGetParamter {
	// 所有邮件、短信发送的参数都以各自创建TZ_AUDCYUAN_T表听众数据中的听众id和听众成员id为参数;
	public String getName(String[] paramters) {
		try {
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			String sql = "select TZ_AUD_XM from PS_TZ_AUDCYUAN_T where TZ_AUDIENCE_ID=? and  TZ_AUDCY_ID=?";
			String audId = paramters[0];
			String audCyId = paramters[1];
			String name = jdbcTemplate.queryForObject(sql, String.class, new Object[] { audId, audCyId });
			return name;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	// 生成注册用户发送的邮件内容;
	public String createUrlforEnroll(String[] paramters) {
		try {
			String audId = paramters[0];
			String audCyId = paramters[1];

			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			String opridSQL = "SELECT OPRID FROM PS_TZ_AUDCYUAN_T WHERE TZ_AUDIENCE_ID=? AND TZ_AUDCY_ID=?";
			String strOprId = jdbcTemplate.queryForObject(opridSQL, String.class, new Object[] { audId, audCyId });
			if (strOprId != null && !"".equals(strOprId)) {
				String jgSQL = "SELECT TZ_JG_ID FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=?";
				String strOrgid = jdbcTemplate.queryForObject(jgSQL, String.class, new Object[] { strOprId });

				String langSQL = "SELECT TZ_SITE_LANG FROM PS_TZ_SITEI_DEFN_T WHERE TZ_JG_ID=? AND TZ_SITEI_ENABLE='Y'";
				String strLang = jdbcTemplate.queryForObject(langSQL, String.class, new Object[] { strOrgid });

				String tokenCodeSQL = "SELECT TZ_TOKEN_CODE FROM PS_TZ_DZYX_YZM_TBL WHERE TZ_DLZH_ID=? AND TZ_JG_ID=? AND TZ_TOKEN_TYPE='REG' AND TZ_EFF_FLAG='Y' ORDER BY TZ_CNTLOG_ADDTIME DESC limit 0,1";
				String strTokenSign = jdbcTemplate.queryForObject(tokenCodeSQL, String.class,
						new Object[] { strOprId, strOrgid, });
				if (strTokenSign != null && !"".equals(strTokenSign)) {
					HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
							.getRequestAttributes()).getRequest();
					String serv = "http://" + request.getServerName() + ":" + request.getServerPort()
							+ request.getContextPath();
					String strActUrl = serv + "/dispatcher";
					strActUrl = strActUrl + "?classid=enrollCls&tokensign=" + strTokenSign + "&orgid=" + strOrgid
							+ "&lang=" + strLang + "&sen=2";
					return strActUrl;
				} else {
					return "";
				}
			} else {
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	// 后台自助信息获取绑定邮箱的URL
	public String getBindEmailUrl(String[] paramters) {
		try {
			String audId = paramters[0];
			String audCyId = paramters[1];
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
					.getRequest();
			String serv = "http://" + request.getServerName() + ":" + request.getServerPort()
					+ request.getContextPath();
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			String opridSQL = "select TZ_TOKEN_CODE from PS_TZ_AUDCYUAN_T a,PS_TZ_DZYX_YZM_TBL b,PS_TZ_AQ_YHXX_TBL c where a.OPRID=c.OPRID and b.TZ_JG_ID=c.TZ_JG_ID and c.TZ_DLZH_ID= b.TZ_DLZH_ID and a.TZ_AUDIENCE_ID=? and a.TZ_AUDCY_ID=? and b.TZ_EFF_FLAG='Y' and b.TZ_TOKEN_TYPE='EDIT'";
			String tokencode = jdbcTemplate.queryForObject(opridSQL, String.class, new Object[] { audId, audCyId });
			String bindEmailUrl = serv + "/dispatcher";
			bindEmailUrl = bindEmailUrl + "?classid=selfBindEmail&TZ_TOKEN_CODE=" + tokencode;
			return bindEmailUrl;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	// 考生申请人忘记密码url;
	public String createUrlforPass(String[] paramters) {
		try {
			String audId = paramters[0];
			String audCyId = paramters[1];

			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			String opridSQL = "SELECT OPRID FROM PS_TZ_AUDCYUAN_T WHERE TZ_AUDIENCE_ID=? AND TZ_AUDCY_ID=?";
			String strOprId = jdbcTemplate.queryForObject(opridSQL, String.class, new Object[] { audId, audCyId });
			if (strOprId != null && !"".equals(strOprId)) {
				String jgSQL = "SELECT TZ_JG_ID FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=?";
				String strOrgid = jdbcTemplate.queryForObject(jgSQL, String.class, new Object[] { strOprId });

				String langSQL = "SELECT TZ_SITE_LANG FROM PS_TZ_SITEI_DEFN_T WHERE TZ_JG_ID=? AND TZ_SITEI_ENABLE='Y'";
				String strLang = jdbcTemplate.queryForObject(langSQL, String.class, new Object[] { strOrgid });

				String tokenCodeSQL = "SELECT TZ_TOKEN_CODE FROM PS_TZ_DZYX_YZM_TBL WHERE TZ_DLZH_ID=? AND TZ_JG_ID=? AND TZ_TOKEN_TYPE='EDIT' AND TZ_EFF_FLAG='Y' ORDER BY TZ_CNTLOG_ADDTIME DESC limit 0,1";
				String strTokenSign = jdbcTemplate.queryForObject(tokenCodeSQL, String.class,
						new Object[] { strOprId, strOrgid, });
				if (strTokenSign != null && !"".equals(strTokenSign)) {
					HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
							.getRequestAttributes()).getRequest();
					String serv = "http://" + request.getServerName() + ":" + request.getServerPort()
							+ request.getContextPath();
					String strActUrl = serv + "/dispatcher";
					strActUrl = strActUrl + "?classid=enrollCls&tokensign=" + strTokenSign + "&orgid=" + strOrgid
							+ "&lang=" + strLang + "&sen=5";
					return strActUrl;
				} else {
					return "";
				}
			} else {
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	// 获得递交资料料内容;
	/* 生成发送内容方法 */
	public String getDjzlContent(String[] paramters) {
		String strDjzlList = "";
		try {
			String audId = paramters[0];
			String audCyId = paramters[1];

			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");

			String sAppInsID = jdbcTemplate.queryForObject(
					"SELECT TZ_BMB_ID FROM PS_TZ_AUDCYUAN_T WHERE TZ_AUDIENCE_ID=? AND TZ_AUDCY_ID=?",
					new Object[] { audId, audCyId }, String.class);
			String strClassID = jdbcTemplate.queryForObject(
					"SELECT TZ_CLASS_ID FROM PS_TZ_FORM_WRK_T WHERE TZ_APP_INS_ID=?",
					new Object[] { Long.valueOf(sAppInsID) }, String.class);
			// String strLanguageId =
			// tzLoginServiceImpl.getSysLanaguageCD(request);

			String strLanguageId = jdbcTemplate.queryForObject(
					"SELECT TZ_APP_TPL_LAN FROM PS_TZ_APP_INS_T A ,PS_TZ_APPTPL_DY_T B WHERE A.TZ_APP_INS_ID=? AND A.TZ_APP_TPL_ID=B.TZ_APP_TPL_ID limit 0,1",
					new Object[] { Long.valueOf(sAppInsID) }, String.class);

			if ("ENG".equals(strLanguageId)) {
				strLanguageId = "ENG";
			} else {
				strLanguageId = "ZHS";
			}

			String sqlContent = "SELECT TZ_SBMINF_ID,TZ_CONT_INTRO,TZ_REMARK FROM PS_TZ_CLS_DJZL_T WHERE TZ_CLASS_ID=? ORDER BY TZ_SORT_NUM ";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlContent, new Object[] { strClassID });
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					String strFileID = (String) list.get(i).get("TZ_SBMINF_ID");
					String strContentIntro = (String) list.get(i).get("TZ_CONT_INTRO");
					// String strRemark = (String)list.get(i).get("TZ_REMARK");

					String strAuditState = "", strAuditStateDesc = "", strFailedReason = "";
					Map<String, Object> map = jdbcTemplate.queryForMap(
							"SELECT TZ_ZL_AUDIT_STATUS,TZ_AUDIT_NOPASS_RS FROM PS_TZ_FORM_ZLSH_T A WHERE TZ_APP_INS_ID=? AND TZ_SBMINF_ID=?",
							new Object[] { sAppInsID, strFileID });
					if (map != null) {
						strAuditState = (String) map.get("TZ_ZL_AUDIT_STATUS");
						strFailedReason = (String) map.get("TZ_AUDIT_NOPASS_RS");

						strAuditStateDesc = jdbcTemplate.queryForObject(
								"select if(B.TZ_ZHZ_CMS IS NULL ,A.TZ_ZHZ_CMS,B.TZ_ZHZ_CMS) TZ_ZHZ_CMS FROM PS_TZ_PT_ZHZXX_TBL A LEFT JOIN (select * from PS_TZ_PT_ZHZXX_LNG where TZ_LANGUAGE_ID=?) B ON A.TZ_ZHZJH_ID=B.TZ_ZHZJH_ID AND A.TZ_ZHZ_ID = B.TZ_ZHZ_ID WHERE A.TZ_EFF_STATUS='A' AND A.TZ_ZHZJH_ID = 'TZ_BMB_DJWJSPZT' AND A.TZ_ZHZ_ID = ?",
								new Object[] { strLanguageId, strAuditState, }, String.class);

						strDjzlList = strDjzlList + "<tr>"
								+ "<td style=\"	border-width: 1px;padding: 8px;font-size:'10px';border-style: solid;border-color: #666666;background-color: #ffffff;\">"
								+ strContentIntro + "</td>"
								+ "<td style=\"	border-width: 1px;padding: 8px;font-size:'10px';border-style: solid;border-color: #666666;background-color: #ffffff;\">"
								+ strAuditStateDesc + "</td>"
								+ "<td style=\"	border-width: 1px;padding: 8px;font-size:'10px';border-style: solid;border-color: #666666;background-color: #ffffff;\">"
								+ strFailedReason + "</td>" + "</tr>";

					}
				}
			}
			return "<table style=\"font-family: verdana,arial,sans-serif;font-size:11px;color:#333333;border-width: 1px;border-color: #666666;border-collapse: collapse;\"><tr><th style=\"border-width: 1px;padding: 8px;font-size:'10px';border-style: solid;border-color: #666666;background-color: #dedede;\">Materials Submitted</th><th style=\"border-width: 1px;padding: 8px;font-size:'10px';border-style: solid;border-color: #666666;background-color: #dedede;\">Status</th><th style=\"border-width: 1px;padding: 8px;font-size:'10px';border-style: solid;border-color: #666666;background-color: #dedede;\">Comments</th></tr>"
					+ strDjzlList + "</table>";

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	// 推荐信中班级名称;
	public String getClassName(String[] paramters) {
		try {
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			String sql = "SELECT TZ_BMB_ID FROM PS_TZ_AUDCYUAN_T WHERE TZ_AUDIENCE_ID=? AND  TZ_AUDCY_ID=?";
			String audId = paramters[0];
			String audCyId = paramters[1];
			String str_bmb_id = jdbcTemplate.queryForObject(sql, String.class, new Object[] { audId, audCyId });
			if (str_bmb_id == null || "".equals(str_bmb_id)) {
				return "";
			}
			String classSQL = "SELECT B.TZ_CLASS_NAME FROM PS_TZ_FORM_WRK_T A,PS_TZ_CLASS_INF_T B WHERE A.TZ_APP_INS_ID=? AND A.TZ_CLASS_ID=B.TZ_CLASS_ID";
			String str_class_name = jdbcTemplate.queryForObject(classSQL, String.class,
					new Object[] { Long.parseLong(str_bmb_id) });
			if (str_class_name == null) {
				return "";
			}
			return str_class_name;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	// 获得发送推荐信邮件被推荐人姓名;;
	public String getRecBName(String[] paramters) {
		try {
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			String sql = "SELECT OPRID FROM PS_TZ_AUDCYUAN_T WHERE TZ_AUDIENCE_ID=? AND  TZ_AUDCY_ID=?";
			String audId = paramters[0];
			String audCyId = paramters[1];
			String str_oprid = jdbcTemplate.queryForObject(sql, String.class, new Object[] { audId, audCyId });
			if (str_oprid == null || "".equals(str_oprid)) {
				return "";
			}
			String tjrSQL = "SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=?";
			String name = jdbcTemplate.queryForObject(tjrSQL, String.class, new Object[] { str_oprid });
			if (name == null) {
				return "";
			}
			return name;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	// 获得发送推荐信邮件推荐人姓名;
	public String getRecName(String[] paramters) {
		try {
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			String sql = "SELECT TZ_AUD_XM FROM PS_TZ_AUDCYUAN_T WHERE TZ_AUDIENCE_ID=? AND  TZ_AUDCY_ID=?";
			String audId = paramters[0];
			String audCyId = paramters[1];
			String name = jdbcTemplate.queryForObject(sql, String.class, new Object[] { audId, audCyId });
			if (name == null || "".equals(name)) {
				return "";
			} else {
				return name;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	// 获得推荐人姓名（推荐信提醒邮件）;
	public String getTjxName(String[] paramters) {
		try {
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			String sql = "SELECT TZ_AUD_XM FROM PS_TZ_AUDCYUAN_T WHERE TZ_AUDIENCE_ID=? AND  TZ_AUDCY_ID=?";
			String audId = paramters[0];
			String audCyId = paramters[1];
			String name = jdbcTemplate.queryForObject(sql, String.class, new Object[] { audId, audCyId });
			if (name == null || "".equals(name)) {
				return "";
			} else {
				return name;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	// 获得推荐人姓名（推荐信提醒邮件）;
	public String getTjxUrl(String[] paramters) {
		try {
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			String sql = "SELECT TZ_BMB_ID,OPRID,TZ_ZY_EMAIL FROM PS_TZ_AUDCYUAN_T WHERE TZ_AUDIENCE_ID=? AND  TZ_AUDCY_ID=?";
			String audId = paramters[0];
			String audCyId = paramters[1];
			Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[] { audId, audCyId });

			String str_oprid = "", str_bmb_id = "", str_email = "";
			if (map != null) {
				str_bmb_id = (String) map.get("TZ_BMB_ID");
				str_oprid = (String) map.get("OPRID");
				str_email = (String) map.get("TZ_ZY_EMAIL");

				String str_ref_id = jdbcTemplate.queryForObject(
						"SELECT TZ_REF_LETTER_ID FROM PS_TZ_KS_TJX_TBL WHERE TZ_APP_INS_ID=? AND TZ_MBA_TJX_YX='Y' AND TZ_EMAIL=? AND OPRID=?",
						new Object[] { Long.parseLong(str_bmb_id), str_email, str_oprid }, String.class);
				HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
						.getRequest();
				String serv = "http://" + request.getServerName() + ":" + request.getServerPort()
						+ request.getContextPath();
				String strTzUrl = serv + "/dispatcher";
				String str_url = strTzUrl + "?classid=refLetterApp&TZ_REF_LETTER_ID=" + str_ref_id + "&OPRID="
						+ str_oprid;
				return str_url;
			} else {
				return "链接生产失败，请联系管理员";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	// 获得推荐人姓名（推荐信提醒邮件）;
	public String getTjxEmail(String[] paramters) {
		try {
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			String sql = "SELECT TZ_BMB_ID,OPRID,TZ_HUOD_ID FROM PS_TZ_AUDCYUAN_T WHERE TZ_AUDIENCE_ID=? AND TZ_AUDCY_ID=?";
			String audId = paramters[0];
			String audCyId = paramters[1];
			Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[] { audId, audCyId });

			String str_oprid = "", str_bmb_id = "", str_tjr_id = "";
			if (map != null) {
				str_bmb_id = (String) map.get("TZ_BMB_ID");
				str_oprid = (String) map.get("OPRID");
				str_tjr_id = (String) map.get("TZ_HUOD_ID");

				String str_email = jdbcTemplate.queryForObject(
						"SELECT TZ_EMAIL FROM PS_TZ_KS_TJX_TBL WHERE TZ_APP_INS_ID=? AND TZ_MBA_TJX_YX='Y' AND TZ_TJR_ID=? AND OPRID=?",
						new Object[] { Long.parseLong(str_bmb_id), str_tjr_id, str_oprid }, String.class);
				if (str_email == null) {
					return "";
				}
				return str_email;
			} else {
				return "链接生产失败，请联系管理员";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	// 获得发送推荐信邮件被推荐人姓名;;
	public String getFirstName(String[] paramters) {
		try {
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			String sql = "SELECT OPRID FROM PS_TZ_AUDCYUAN_T WHERE TZ_AUDIENCE_ID=? AND TZ_AUDCY_ID=?";
			String audId = paramters[0];
			String audCyId = paramters[1];
			String str_oprid = jdbcTemplate.queryForObject(sql, String.class, new Object[] { audId, audCyId });
			if (str_oprid == null || "".equals(str_oprid)) {
				return "";
			}
			String regSQL = "SELECT TZ_FIRST_NAME FROM PS_TZ_REG_USER_T WHERE OPRID=?";
			String name = jdbcTemplate.queryForObject(regSQL, String.class, new Object[] { str_oprid });
			if (name == null) {
				return "";
			}
			return name;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	// 获得考生（推荐信后台催促邮件）;
	public String getTjxKshtName(String[] paramters) {
		try {
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			String sql = "SELECT TZ_AUD_XM FROM PS_TZ_AUDCYUAN_T WHERE TZ_AUDIENCE_ID=? AND  TZ_AUDCY_ID=?";
			String audId = paramters[0];
			String audCyId = paramters[1];
			String name = jdbcTemplate.queryForObject(sql, String.class, new Object[] { audId, audCyId });
			if (name == null || "".equals(name)) {
				return "";
			} else {
				return name;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	// 获得推荐信未完成人员姓名（推荐信后台催促邮件）;
	public String getTjxWwcName(String[] paramters) {
		try {
			String str_ref_name = "";
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			String str_none_blank = jdbcTemplate.queryForObject(
					"SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT = ?",
					new Object[] { "TZ_REF_TITLE_NONE_BLANK" }, String.class);

			String audId = paramters[0];
			String audCyId = paramters[1];
			String str_bmb_id = "";
			Map<String, Object> map = jdbcTemplate.queryForMap(
					"SELECT OPRID,TZ_BMB_ID FROM PS_TZ_AUDCYUAN_T WHERE TZ_AUDIENCE_ID=? AND TZ_AUDCY_ID=?",
					new Object[] { audId, audCyId });
			if (map != null) {
				// str_oprid = (String)map.get("OPRID");
				str_bmb_id = (String) map.get("TZ_BMB_ID");
			}
			if (str_bmb_id == null || "".equals(str_bmb_id)) {
				str_bmb_id = "0";
			}

			String sql = "SELECT TZ_TJX_APP_INS_ID,TZ_TJX_TITLE,TZ_REFERRER_NAME,TZ_REFERRER_GNAME,ATTACHSYSFILENAME FROM PS_TZ_KS_TJX_TBL WHERE TZ_APP_INS_ID=? AND TZ_MBA_TJX_YX='Y'";
			List<Map<String, Object>> list = null;

			try {
				list = jdbcTemplate.queryForList(sql, new Object[] { Long.parseLong(str_bmb_id) });
			} catch (Exception e) {
				list = null;
			}
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					long str_tjx_bmb_id = 0L;
					try {
						str_tjx_bmb_id = Long.parseLong(list.get(i).get("TZ_TJX_APP_INS_ID").toString());
					} catch (Exception e) {
						str_tjx_bmb_id = 0L;
					}
					String str_tjr_title = (String) list.get(i).get("TZ_TJX_TITLE");
					String str_ref_name_1 = (String) list.get(i).get("TZ_REFERRER_NAME");
					String str_tjr_gname = (String) list.get(i).get("TZ_REFERRER_GNAME");
					String str_file_name = (String) list.get(i).get("ATTACHSYSFILENAME");

					String str_name_suff = "";

					if (str_tjr_title != null && !"".equals(str_tjr_title) && str_tjr_title.equals(str_none_blank)) {
						str_name_suff = str_tjr_title;
					}

					if (str_tjr_gname != null && !"".equals(str_tjr_gname)) {
						if (str_name_suff != null && !"".equals(str_name_suff)) {
							str_name_suff = str_name_suff + " " + str_tjr_gname;
						} else {
							str_name_suff = str_tjr_gname;
						}
					}

					if (str_ref_name_1 == null) {
						str_ref_name_1 = "";
					}

					if (str_name_suff != null && !"".equals(str_name_suff)) {
						str_ref_name_1 = str_name_suff + " " + str_ref_name_1;
					}

					if (str_file_name == null || "".equals(str_file_name)) {
						String str_states = "";
						try {
							str_states = jdbcTemplate.queryForObject(
									"SELECT TZ_APP_FORM_STA FROM PS_TZ_APP_INS_T WHERE TZ_APP_INS_ID=?",
									new Object[] { str_tjx_bmb_id }, String.class);
						} catch (Exception e) {
							str_states = "";
						}
						if (!"U".equals(str_states)) {
							if (str_ref_name == null || "".equals(str_ref_name)) {
								str_ref_name = str_ref_name_1;
							} else {
								str_ref_name = str_ref_name + ", " + str_ref_name_1;
							}
						}
					}
				}
			}

			return str_ref_name;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	// 获得推荐信已完成人员姓名（推荐信后台催促邮件）;
	public String getTjxYwcName(String[] paramters) {
		try {
			String str_ref_name = "";
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			String str_none_blank = jdbcTemplate.queryForObject(
					"SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT = ?",
					new Object[] { "TZ_REF_TITLE_NONE_BLANK" }, String.class);

			String audId = paramters[0];
			String audCyId = paramters[1];
			String str_bmb_id = "";
			Map<String, Object> map = jdbcTemplate.queryForMap(
					"SELECT OPRID,TZ_BMB_ID FROM PS_TZ_AUDCYUAN_T WHERE TZ_AUDIENCE_ID=? AND TZ_AUDCY_ID=?",
					new Object[] { audId, audCyId });
			if (map != null) {
				// str_oprid = (String)map.get("OPRID");
				str_bmb_id = (String) map.get("TZ_BMB_ID");
			}
			if (str_bmb_id == null || "".equals(str_bmb_id)) {
				str_bmb_id = "0";
			}

			String sql = "SELECT TZ_TJX_APP_INS_ID,TZ_TJX_TITLE,TZ_REFERRER_NAME,TZ_REFERRER_GNAME,ATTACHSYSFILENAME FROM PS_TZ_KS_TJX_TBL WHERE TZ_APP_INS_ID=? AND TZ_MBA_TJX_YX='Y'";
			List<Map<String, Object>> list = null;
			try {
				list = jdbcTemplate.queryForList(sql, new Object[] { Long.parseLong(str_bmb_id) });
			} catch (Exception e) {
				list = null;
			}
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					long str_tjx_bmb_id = 0L;
					try {
						str_tjx_bmb_id = Long.parseLong(list.get(i).get("TZ_TJX_APP_INS_ID").toString());
					} catch (Exception e) {
						str_tjx_bmb_id = 0L;
					}
					String str_tjr_title = (String) list.get(i).get("TZ_TJX_TITLE");
					String str_ref_name_1 = (String) list.get(i).get("TZ_REFERRER_NAME");
					String str_tjr_gname = (String) list.get(i).get("TZ_REFERRER_GNAME");
					String str_file_name = (String) list.get(i).get("ATTACHSYSFILENAME");

					String str_name_suff = "";

					if (str_tjr_title != null && !"".equals(str_tjr_title) && str_tjr_title.equals(str_none_blank)) {
						str_name_suff = str_tjr_title;
					}

					if (str_tjr_gname != null && !"".equals(str_tjr_gname)) {
						if (str_name_suff != null && !"".equals(str_name_suff)) {
							str_name_suff = str_name_suff + " " + str_tjr_gname;
						} else {
							str_name_suff = str_tjr_gname;
						}
					}

					if (str_ref_name_1 == null) {
						str_ref_name_1 = "";
					}

					if (str_name_suff != null && !"".equals(str_name_suff)) {
						str_ref_name_1 = str_name_suff + " " + str_ref_name_1;
					}

					if (str_file_name != null && !"".equals(str_file_name)) {
						if (str_ref_name == null || "".equals(str_ref_name)) {
							str_ref_name = str_ref_name_1;
						} else {
							str_ref_name = str_ref_name + ", " + str_ref_name_1;
						}
					} else {
						String str_states = "";
						try {
							str_states = jdbcTemplate.queryForObject(
									"SELECT TZ_APP_FORM_STA FROM PS_TZ_APP_INS_T WHERE TZ_APP_INS_ID=?",
									new Object[] { str_tjx_bmb_id }, String.class);
						} catch (Exception e) {
							str_states = "";
						}
						if ("U".equals(str_states)) {
							if (str_ref_name == null || "".equals(str_ref_name)) {
								str_ref_name = str_ref_name_1;
							} else {
								str_ref_name = str_ref_name + ", " + str_ref_name_1;
							}
						}
					}
				}
			}
			return str_ref_name;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	// 获得考生姓名（推荐信感谢邮件）;
	public String getTjxKsName(String[] paramters) {
		try {
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			String sql = "SELECT OPRID FROM PS_TZ_AUDCYUAN_T WHERE TZ_AUDIENCE_ID=? AND TZ_AUDCY_ID=?";
			String audId = paramters[0];
			String audCyId = paramters[1];
			String str_oprid = jdbcTemplate.queryForObject(sql, String.class, new Object[] { audId, audCyId });
			if (str_oprid != null && !"".equals(str_oprid)) {
				String str_tjxname = jdbcTemplate.queryForObject(
						"SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=?", new Object[] { str_oprid },
						String.class);
				if (str_tjxname != null && !"".equals(str_tjxname)) {
					return str_tjxname;
				} else {
					return "";
				}
			} else {
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	// 获得推荐人姓名（推荐信感谢邮件）;
	public String getTjxThanksName(String[] paramters) {
		try {
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			String sql = "SELECT TZ_AUD_XM FROM PS_TZ_AUDCYUAN_T WHERE TZ_AUDIENCE_ID=? AND  TZ_AUDCY_ID=?";
			String audId = paramters[0];
			String audCyId = paramters[1];
			String name = jdbcTemplate.queryForObject(sql, String.class, new Object[] { audId, audCyId });
			if (name == null || "".equals(name)) {
				return "";
			} else {
				return name;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	// 生成修改密码的URL;
	public String createUrlforChange(String[] paramters) {
		try {
			String audId = paramters[0];
			String audCyId = paramters[1];

			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			String opridSQL = "SELECT OPRID FROM PS_TZ_AUDCYUAN_T WHERE TZ_AUDIENCE_ID=? AND TZ_AUDCY_ID=?";
			String strOprId = jdbcTemplate.queryForObject(opridSQL, String.class, new Object[] { audId, audCyId });
			if (strOprId != null && !"".equals(strOprId)) {
				String jgSQL = "SELECT TZ_JG_ID FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=?";
				String strOrgid = jdbcTemplate.queryForObject(jgSQL, String.class, new Object[] { strOprId });

				String langSQL = "SELECT TZ_SITE_LANG FROM PS_TZ_SITEI_DEFN_T WHERE TZ_JG_ID=? AND TZ_SITEI_ENABLE='Y'";
				String strLang = jdbcTemplate.queryForObject(langSQL, String.class, new Object[] { strOrgid });

				String tokenCodeSQL = "SELECT TZ_TOKEN_CODE FROM PS_TZ_DZYX_YZM_TBL WHERE TZ_DLZH_ID=? AND TZ_JG_ID=? AND TZ_TOKEN_TYPE='EDIT' AND TZ_EFF_FLAG='Y' ORDER BY TZ_CNTLOG_ADDTIME DESC limit 0,1";
				String strTokenSign = jdbcTemplate.queryForObject(tokenCodeSQL, String.class,
						new Object[] { strOprId, strOrgid, });
				if (strTokenSign != null && !"".equals(strTokenSign)) {
					HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
							.getRequestAttributes()).getRequest();
					String serv = "http://" + request.getServerName() + ":" + request.getServerPort()
							+ request.getContextPath();
					String strActUrl = serv + "/dispatcher";
					strActUrl = strActUrl + "?classid=enrollCls&tokensign=" + strTokenSign + "&orgid=" + strOrgid
							+ "&lang=" + strLang + "&sen=7";
					return strActUrl;
				} else {
					return "";
				}
			} else {
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}
