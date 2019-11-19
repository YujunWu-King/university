package com.tranzvision.gd.TZWebSiteUtilBundle.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAccountMgBundle.dao.PsoprdefnMapper;
import com.tranzvision.gd.TZAccountMgBundle.model.Psoprdefn;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.CreateTaskServiceImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.SendSmsOrMalServiceImpl;
import com.tranzvision.gd.TZSelfInfoBundle.model.PsTzShjiYzmTbl;
import com.tranzvision.gd.TZSelfInfoBundle.dao.PsTzShjiYzmTblMapper;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.captcha.PasswordCheck;
import com.tranzvision.gd.util.captcha.Patchca;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.encrypt.DESUtil;
import com.tranzvision.gd.util.security.RegExpValidatorUtils;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
import java.util.UUID;
/**
 * 
 * @author tang 招生网站考生申请人短信处理包 原： TZ_SITE_UTIL_APP:TZ_SITE_SMS_CLS
 */
@Service("com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.RegisteSmsServiceImpl")
public class RegisteSmsServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private ValidateUtil validateUtil;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private PsTzShjiYzmTblMapper psTzShjiYzmTblMapper;
	@Autowired
	private CreateTaskServiceImpl createTaskServiceImpl;
	@Autowired
	private SendSmsOrMalServiceImpl sendSmsOrMalServiceImpl;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private PsoprdefnMapper psoprdefnMapper;
	@Autowired
	private SiteEnrollClsServiceImpl siteEnrollClsServiceImpl;
	@Autowired
	private TZGDObject tzGdObject;
	@Autowired
	private SiteRepCssServiceImpl objRep;
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	@Autowired
	private HttpServletResponse response;
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		String strSen = "";
		String strResponse = "\"failure\"";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			if (jacksonUtil.containsKey("sen")) {
				strSen = jacksonUtil.getString("sen");
				// 手机号码
				if ("1".equals(strSen)) {
					strResponse = this.smsVerifyByActive(strParams, errMsg);
				}
				// 手机验证码;
				if ("2".equals(strSen)) {
					strResponse = this.sendMessage(strParams, errMsg);
				}
				if ("3".equals(strSen)) {
					strResponse = this.checkMobileCode(strParams, errMsg);
				}
				if ("5".equals(strSen)) {
					strResponse = this.modifyPasswordByPass(strParams, errMsg);
				}
				if ("6".equals(strSen)) {
					strResponse = this.phoneVerifyByForget(strParams, errMsg);
				}
				if ("7".equals(strSen)) {
					strResponse = this.sendMessageForPass(strParams, errMsg);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return strResponse;
	}

	@Override
	public String tzGetHtmlContent(String strParams) {
		String strSiteid = request.getParameter("siteid");
		String strOrgid = request.getParameter("orgid");
		String strPhone = request.getParameter("phone");
		String strYzm = request.getParameter("yzm");
		String strLang = request.getParameter("lang");
		String strSen = request.getParameter("sen");
		String classid = request.getParameter("classid");
		String isMobile = request.getParameter("isMobile");
		String id =request.getParameter("id");
		System.out.print("id"+id);
		String strResponse = "获取数据失败，请联系管理员";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			/* 对lang参数做限制，只能是ZHS或ENG，不单从URL中获取，卢艳添加，2017-10-9 */
			if (strLang != null && !"".equals(strLang)) {
				if ("ZHS".equals(strLang.toUpperCase()) || "ENG".equals(strLang.toUpperCase())) {
					strLang = strLang.toUpperCase();
				} else {
					strLang = "ZHS";
				}
			}

			if (classid != null && !"".equals(classid)) {
				strParams = "{\"siteid\":\"" + strSiteid + "\",\"orgid\":\"" + strOrgid + "\",\"lang\":\"" + strLang
						+ "\",\"yzm\":\"" + strYzm + "\",\"phone\":\"" + strPhone + "\",\"sen\":\"" + strSen
						+ "\",\"isMobile\":\"" + isMobile + "\",\"id\":\"" + id + "\"}";
			} else {
				jacksonUtil.json2Map(strParams);
				strOrgid = jacksonUtil.getString("orgid");
				strLang = jacksonUtil.getString("lang");
				strSen = jacksonUtil.getString("sen");
			}

			if ("4".equals(strSen)) {
				return this.createPageForFixPass(strParams);
			}

			String strMessage = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "119",
					"链接错误，请确认您输入的URL地址无误！", "Url is invalid, please makesure the url is valid!");
			return strMessage;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return strResponse;
	}

	// 校验手机号码
	public String smsVerifyByActive(String strParams, String[] errorMsg) {
		String strPhone = "";

		String strOrgid = "";
		String strLang = "";

		String strResult = "\"failure\"";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			if (jacksonUtil.containsKey("phone") && jacksonUtil.containsKey("orgid") && jacksonUtil.containsKey("lang"))
				strPhone = jacksonUtil.getString("phone").trim().toLowerCase();
			strOrgid = jacksonUtil.getString("orgid").trim();
			strLang = jacksonUtil.getString("lang").trim();

			// 手机格式;
			ValidateUtil validateUtil = new ValidateUtil();
			boolean bl = RegExpValidatorUtils.isMobile(strPhone);
			if (bl == false) {
				errorMsg[0] = "1";
				errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "47",
						"手机号码不正确", "The mobile phone is incorrect .");
				return strResult;
			}

			// 手机是否被占用
			String sql = "SELECT COUNT(1) FROM PS_TZ_AQ_YHXX_TBL WHERE LOWER(TZ_MOBILE) = LOWER(?) AND LOWER(TZ_JG_ID)=LOWER(?) AND TZ_SJBD_BZ='Y'";
			int count = jdbcTemplate.queryForObject(sql, new Object[] { strPhone, strOrgid }, "Integer");
			if (count > 0) {
				errorMsg[0] = "2";
				errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "49",
						"手机已注册，建议取回密码", "The mobile phone has been registered, proposed to retrieve Password");
				return strResult;
			}
			strResult = "\"success\"";
			return strResult;
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "100";
			errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "55",
					"获取数据失败，请联系管理员", "Get the data failed, please contact the administrator");
		}

		return strResult;
	}

	public String validateSmsCode(String strParams, String[] errorMsg) {
		String strPhone = "";

		String strOrgid = "";
		String strLang = "";

		String strResult = "\"failure\"";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			if (jacksonUtil.containsKey("phone") && jacksonUtil.containsKey("orgid") && jacksonUtil.containsKey("lang"))
				strPhone = jacksonUtil.getString("phone").trim().toLowerCase();
			strOrgid = jacksonUtil.getString("orgid").trim();
			strLang = jacksonUtil.getString("lang").trim();

			// 手机格式;
			ValidateUtil validateUtil = new ValidateUtil();
			boolean bl = RegExpValidatorUtils.isMobile(strPhone);
			if (bl == false) {
				errorMsg[0] = "1";
				errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "47",
						"手机号码不正确", "The mobile phone is incorrect .");
				return strResult;
			}

			// 手机是否被占用
			String sql = "SELECT COUNT(1) FROM PS_TZ_AQ_YHXX_TBL WHERE LOWER(TZ_MOBILE) = LOWER(?) AND LOWER(TZ_JG_ID)=LOWER(?) AND TZ_SJBD_BZ='Y'";
			int count = jdbcTemplate.queryForObject(sql, new Object[] { strPhone, strOrgid.toLowerCase() }, "Integer");
			if (count > 0) {
				errorMsg[0] = "2";
				errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "49",
						"手机已注册，建议取回密码", "The mobile phone has been registered, proposed to retrieve Password");
				return strResult;
			}
			strResult = "\"success\"";
			return strResult;
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "100";
			errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "55",
					"获取数据失败，请联系管理员", "Get the data failed, please contact the administrator");
		}

		return strResult;
	}

	// 发送验证码-注册
	public String sendMessage(String strParams, String[] errorMsg) {

		String strPhone = "";
		String strOrgid = "";
		String strLang = "";

		String strResult = "\"failure\"";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			if (jacksonUtil.containsKey("phone") && jacksonUtil.containsKey("orgid") && jacksonUtil.containsKey("lang"))
				strPhone = String.valueOf(jacksonUtil.getString("phone").trim()).toLowerCase();
			strOrgid = String.valueOf(jacksonUtil.getString("orgid").trim());
			strLang = String.valueOf(jacksonUtil.getString("lang").trim());

			// 手机格式;
			ValidateUtil validateUtil = new ValidateUtil();
			boolean bl = RegExpValidatorUtils.isMobile(strPhone);
			if (bl == false) {
				errorMsg[0] = "1";
				errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "47",
						"您填写的手机号码有误", "Malformed phone.");
				return strResult;
			}

			// 手机是否被占用
			String sql = "SELECT COUNT(1) FROM PS_TZ_AQ_YHXX_TBL WHERE LOWER(TZ_MOBILE) = LOWER(?) AND LOWER(TZ_JG_ID)=LOWER(?) AND TZ_SJBD_BZ='Y'";
			int count = jdbcTemplate.queryForObject(sql, new Object[] { strPhone, strOrgid }, "Integer");
			if (count > 0) {
				errorMsg[0] = "2";
				errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "49",
						"手机已注册，建议取回密码", "The phone has been registered, proposed to retrieve Password");
				return strResult;
			}

			// 校验验证码的有效期
			// DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date dtYzmValidDate = null;
			Date dtYzmAddDate = null;
			String strYzm = "";
			Date nowDate = new Date();

			boolean sendYzmFlag = true;

			String sqlGetYzmInfo = "SELECT TZ_CNTLOG_ADDTIME,TZ_YZM_YXQ,TZ_SJYZM FROM PS_TZ_SHJI_YZM_TBL WHERE TZ_EFF_FLAG='Y' AND TZ_JG_ID=? AND TZ_MOBILE_PHONE=?  ORDER BY TZ_CNTLOG_ADDTIME DESC LIMIT 0,1";
			Map<String, Object> MapGetYzmInfo = jdbcTemplate.queryForMap(sqlGetYzmInfo,
					new Object[] { strOrgid, strPhone });
			if (MapGetYzmInfo == null) {
				// 发送验证码
			} else {
				try {
					// dtYzmValidDate =
					// format.parse(String.valueOf(MapGetYzmInfo.get("TZ_YZM_YXQ")));
					// dtYzmAddDate =
					// format.parse(String.valueOf(MapGetYzmInfo.get("TZ_CNTLOG_ADDTIME")));
					dtYzmValidDate = (Date) (MapGetYzmInfo.get("TZ_YZM_YXQ"));
					dtYzmAddDate = (Date) (MapGetYzmInfo.get("TZ_CNTLOG_ADDTIME"));
					strYzm = MapGetYzmInfo.get("TZ_SJYZM") == null ? "" : String.valueOf(MapGetYzmInfo.get("TZ_SJYZM"));

					if (!"".equals(strYzm)) {
						if (dtYzmValidDate.after(nowDate)) {
							// 发送时间间隔太短
							errorMsg[0] = "10";
							errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,
									"TZ_SITE_MESSAGE", "128", "发送时间间隔太短,请等待一段时间后再试", "Try again later.");
							sendYzmFlag = false;
						} else {
							if (dtYzmAddDate != null) {
								Object[] args = new Object[] { strOrgid, strPhone, dtYzmAddDate };
								jdbcTemplate.update(
										"UPDATE PS_TZ_SHJI_YZM_TBL SET TZ_EFF_FLAG='N' WHERE TZ_JG_ID=? AND TZ_MOBILE_PHONE=? AND TZ_CNTLOG_ADDTIME=?",
										args);
							}
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// 发送验证码
			if (sendYzmFlag) {
				strYzm = String.valueOf("0000" + (int) (Math.random() * 100000));
				strYzm = strYzm.substring(strYzm.length() - 4, strYzm.length());

				PsTzShjiYzmTbl psTzShjiYzmTbl = new PsTzShjiYzmTbl();
				psTzShjiYzmTbl.setTzJgId(strOrgid);
				psTzShjiYzmTbl.setTzMobilePhone(strPhone);
				psTzShjiYzmTbl.setTzCntlogAddtime(new Date());
				psTzShjiYzmTbl.setTzSjyzm(strYzm);
				Calendar ca = Calendar.getInstance();
				ca.setTime(new Date());
				ca.add(Calendar.MINUTE, 1);
				psTzShjiYzmTbl.setTzYzmYxq(ca.getTime());
				psTzShjiYzmTbl.setTzEffFlag("Y");
				psTzShjiYzmTblMapper.insert(psTzShjiYzmTbl);

				String getSmsSuffiSql = "SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT = ? LIMIT 1";
				String strSmsSuffix = jdbcTemplate.queryForObject(getSmsSuffiSql, new Object[] { "TZ_SMS_SEND_SUFFIX" },
						"String");
				if (strSmsSuffix == null)
					strSmsSuffix = "";

				String strSmsPrefix = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE",
						"132", "本次验证码为：", "Verification Code:");
				// 给当前填写的手机号码发送验证码
				String strSmsContent = strSmsPrefix + strYzm + strSmsSuffix;
				String strUserName = "";

				String oprid = "";
				oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

				// 得到注册用户姓名;
				String relenameSQL = "SELECT TZ_REALNAME FROM PS_TZ_REG_USER_T WHERE OPRID=? limit 0,1";
				try {
					strUserName = jdbcTemplate.queryForObject(relenameSQL, new Object[] { oprid }, "String");
				} catch (Exception e) {
					e.printStackTrace();
				}
				String getSmsSendTmpSql = "SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT = ? LIMIT 1";
				String strSmsSendTmp = jdbcTemplate.queryForObject(getSmsSendTmpSql, new Object[] { "TZ_SMS_SEND_TPL" },
						"String");
				if (strSmsSendTmp == null) {
					errorMsg[0] = "32";
					errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "127",
							"短信发送失败", "Failed to send SMS。");
					return strResult;
				}
				// 创建邮件短信发送任务
				String taskId = createTaskServiceImpl.createTaskIns(strOrgid, strSmsSendTmp, "SMS", "A");
				if (taskId == null || "".equals(taskId)) {
					errorMsg[0] = "30";
					errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "127",
							"短信发送失败", "Failed to send SMS。");
					return strResult;
				}
				// 创建短信、邮件发送的听众;
				String createAudience = createTaskServiceImpl.createAudience(taskId, strOrgid, "考生申请用户注册手机验证", "JSRW");
				if (createAudience == null || "".equals(createAudience)) {
					errorMsg[0] = "31";
					errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "127",
							"短信发送失败", "Failed to send SMS。");
					return strResult;
				}
				// 为听众添加听众成员;
				boolean addAudCy = createTaskServiceImpl.addAudCy(createAudience, strUserName, strUserName, strPhone,
						"", "", "", "", oprid, "", "", "");
				if (addAudCy == false) {
					errorMsg[0] = "32";
					errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "127",
							"短信发送失败", "Failed to send SMS。");
					return strResult;
				}

				boolean updateSmsSendContent = createTaskServiceImpl.updateSmsSendContent(taskId, strSmsContent);
				if (updateSmsSendContent == false) {
					errorMsg[0] = "33";
					errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "127",
							"短信发送失败", "Failed to send SMS。");
					return strResult;
				}
				sendSmsOrMalServiceImpl.send(taskId, "");
				strResult = "\"success\"";
				return strResult;
			} else {
				errorMsg[0] = "100";
				errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "55",
						"获取数据失败，请联系管理员", "Get the data failed, please contact the administrator");
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "100";
			errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "55",
					"获取数据失败，请联系管理员", "Get the data failed, please contact the administrator");
		}

		return strResult;
	}

	// 手机找回密码-发送验证码
	public String sendMessageForPass(String strParams, String[] errorMsg) {

		String strPhone = "";
		String strOrgid = "";
		String strLang = "";

		String strResult = "\"failure\"";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			if (jacksonUtil.containsKey("phone") && jacksonUtil.containsKey("orgid") && jacksonUtil.containsKey("lang"))
				strPhone = String.valueOf(jacksonUtil.getString("phone").trim()).toLowerCase();
			strOrgid = String.valueOf(jacksonUtil.getString("orgid").trim());
			strLang = String.valueOf(jacksonUtil.getString("lang").trim());

			// 手机格式;
			ValidateUtil validateUtil = new ValidateUtil();
			boolean bl = RegExpValidatorUtils.isMobile(strPhone);
			if (bl == false) {
				errorMsg[0] = "1";
				errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "47",
						"您填写的手机号码有误", "Malformed phone.");
				return strResult;
			}

			// 手机是否被占用
			String sql = "SELECT COUNT(1) FROM PS_TZ_AQ_YHXX_TBL WHERE LOWER(TZ_MOBILE) = LOWER(?) AND LOWER(TZ_JG_ID)=LOWER(?) AND TZ_JIHUO_ZT = 'Y' AND TZ_SJBD_BZ='Y'";
			int count = jdbcTemplate.queryForObject(sql, new Object[] { strPhone, strOrgid }, "Integer");
			if (count <= 0) {
				errorMsg[0] = "2";
				errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "129",
						"手机不存在，请先注册", "The mobile phone does not exist, please register");
				return strResult;
			}

			// 校验验证码的有效期
			// DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date dtYzmValidDate = null;
			Date dtYzmAddDate = null;
			String strYzm = "";
			Date nowDate = new Date();

			boolean sendYzmFlag = true;

			String sqlGetYzmInfo = "SELECT TZ_CNTLOG_ADDTIME,TZ_YZM_YXQ,TZ_SJYZM FROM PS_TZ_SHJI_YZM_TBL WHERE TZ_EFF_FLAG='Y' AND TZ_JG_ID=? AND TZ_MOBILE_PHONE=?  ORDER BY TZ_CNTLOG_ADDTIME DESC LIMIT 0,1";
			Map<String, Object> MapGetYzmInfo = jdbcTemplate.queryForMap(sqlGetYzmInfo,
					new Object[] { strOrgid, strPhone });
			if (MapGetYzmInfo == null) {
				// 发送验证码
			} else {
				try {
					// dtYzmValidDate =
					// format.parse(String.valueOf(MapGetYzmInfo.get("TZ_YZM_YXQ")));
					// dtYzmAddDate =
					// format.parse(String.valueOf(MapGetYzmInfo.get("TZ_CNTLOG_ADDTIME")));
					dtYzmValidDate = (Date) (MapGetYzmInfo.get("TZ_YZM_YXQ"));
					dtYzmAddDate = (Date) (MapGetYzmInfo.get("TZ_CNTLOG_ADDTIME"));
					strYzm = MapGetYzmInfo.get("TZ_SJYZM") == null ? "" : String.valueOf(MapGetYzmInfo.get("TZ_SJYZM"));

					if (!"".equals(strYzm)) {
						if (dtYzmValidDate.after(nowDate)) {
							// 发送时间间隔太短
							errorMsg[0] = "10";
							errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,
									"TZ_SITE_MESSAGE", "128", "发送时间间隔太短,请等待一段时间后再试", "Try again later.");
							sendYzmFlag = false;
							return strResult;
						} else {
							if (dtYzmAddDate != null) {
								Object[] args = new Object[] { strOrgid, strPhone, dtYzmAddDate };
								jdbcTemplate.update(
										"UPDATE PS_TZ_SHJI_YZM_TBL SET TZ_EFF_FLAG='N' WHERE TZ_JG_ID=? AND TZ_MOBILE_PHONE=? AND TZ_CNTLOG_ADDTIME=?",
										args);
							}
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// 发送验证码
			if (sendYzmFlag) {
				strYzm = String.valueOf("0000" + (int) (Math.random() * 100000));
				strYzm = strYzm.substring(strYzm.length() - 4, strYzm.length());

				PsTzShjiYzmTbl psTzShjiYzmTbl = new PsTzShjiYzmTbl();
				psTzShjiYzmTbl.setTzJgId(strOrgid);
				psTzShjiYzmTbl.setTzMobilePhone(strPhone);
				psTzShjiYzmTbl.setTzCntlogAddtime(new Date());
				psTzShjiYzmTbl.setTzSjyzm(strYzm);
				Calendar ca = Calendar.getInstance();
				ca.setTime(new Date());
				ca.add(Calendar.MINUTE, 1);
				psTzShjiYzmTbl.setTzYzmYxq(ca.getTime());

				psTzShjiYzmTbl.setTzEffFlag("Y");
				psTzShjiYzmTblMapper.insert(psTzShjiYzmTbl);

				String getSmsSuffiSql = "SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT = ? LIMIT 1";
				String strSmsSuffix = jdbcTemplate.queryForObject(getSmsSuffiSql, new Object[] { "TZ_SMS_SEND_SUFFIX" },
						"String");
				if (strSmsSuffix == null)
					strSmsSuffix = "";

				String strSmsPrefix = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE",
						"132", "本次验证码为：", "Verification Code:");

				// 给当前填写的手机号码发送验证码
				String strSmsContent = strSmsPrefix + strYzm + strSmsSuffix;
				String strUserName = "";

				String oprid = "";
				oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

				// 得到注册用户姓名;
				String relenameSQL = "SELECT TZ_REALNAME FROM PS_TZ_REG_USER_T WHERE OPRID=? limit 0,1";
				try {
					strUserName = jdbcTemplate.queryForObject(relenameSQL, new Object[] { oprid }, "String");
				} catch (Exception e) {
					e.printStackTrace();
				}

				String getSmsSendTmpSql = "SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT = ? LIMIT 1";
				String strSmsSendTmp = jdbcTemplate.queryForObject(getSmsSendTmpSql, new Object[] { "TZ_SMS_SEND_TPL" },
						"String");
				if (strSmsSendTmp == null) {
					errorMsg[0] = "32";
					errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "127",
							"短信发送失败", "Failed to send SMS。");
					return strResult;
				}
				// 创建邮件短信发送任务
				String taskId = createTaskServiceImpl.createTaskIns(strOrgid, strSmsSendTmp, "SMS", "A");
				if (taskId == null || "".equals(taskId)) {
					errorMsg[0] = "30";
					errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "127",
							"短信发送失败", "Failed to send SMS。");
					return strResult;
				}
				// 创建短信、邮件发送的听众;
				String createAudience = createTaskServiceImpl.createAudience(taskId, strOrgid, "考生申请用户注册手机验证", "JSRW");
				if (createAudience == null || "".equals(createAudience)) {
					errorMsg[0] = "31";
					errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "127",
							"短信发送失败", "Failed to send SMS。");
					return strResult;
				}
				// 为听众添加听众成员;
				boolean addAudCy = createTaskServiceImpl.addAudCy(createAudience, strUserName, strUserName, strPhone,
						"", "", "", "", oprid, "", "", "");
				if (addAudCy == false) {
					errorMsg[0] = "32";
					errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "127",
							"短信发送失败", "Failed to send SMS。");
					return strResult;
				}

				boolean updateSmsSendContent = createTaskServiceImpl.updateSmsSendContent(taskId, strSmsContent);
				if (updateSmsSendContent == false) {
					errorMsg[0] = "33";
					errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "127",
							"短信发送失败", "Failed to send SMS。");
					return strResult;
				}
				
				
				sendSmsOrMalServiceImpl.send(taskId, "");
				strResult = "\"success\"";
				return strResult;
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "100";
			errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "55",
					"获取数据失败，请联系管理员", "Get the data failed, please contact the administrator");
		}

		return strResult;
	}

	// 忘记密码-手机找回-下一步验证手机验证码
	public String checkMobileCode(String strParams, String[] errorMsg) {

		String contextUrl = request.getContextPath();
		String strTzGeneralURL = contextUrl + "/dispatcher";

		String strPhone = "";

		String strOrgid = "";
		String strLang = "";
		String strYzm = "";
		String picyzm="";
		//String strResult = "\"failure\"";
		Map<String,Object> resMap = new HashMap<String,Object>();
		resMap.put("result", "failure");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			if (jacksonUtil.containsKey("phone") && jacksonUtil.containsKey("orgid") && jacksonUtil.containsKey("lang")
					&& jacksonUtil.containsKey("yzm"))
				strPhone = jacksonUtil.getString("phone").trim();
			strOrgid = jacksonUtil.getString("orgid").trim();
			strLang = jacksonUtil.getString("lang").trim();
			strYzm = jacksonUtil.getString("yzm").trim();
			picyzm = jacksonUtil.getString("picyzm").trim();
			Patchca patcha = new Patchca();
			if (strPhone != null && !"".equals(strPhone) && strOrgid != null && !"".equals(strOrgid) && strYzm != null
					&& !"".equals(strYzm)&&picyzm != null&& !"".equals(picyzm)) {
				System.out.println("patchca"+picyzm);
				// 校验验证码
				//Patchca patchca = new Patchca();
				if (!patcha.verifyToken(request, picyzm)) {
					errorMsg[0] = "2";
					errorMsg[1] ="图片验证码不正确";
					
					patcha.removeToken(request);
					return jacksonUtil.Map2json(resMap);
				}
				System.out.println("sjyzm===");
				// 是否存在有效验证码
				String sql = "SELECT TZ_YZM_YXQ FROM PS_TZ_SHJI_YZM_TBL  WHERE TZ_EFF_FLAG = 'Y' AND TZ_JG_ID = ? AND TZ_MOBILE_PHONE = ? and TZ_SJYZM= ? LIMIT 0,1";
				Map<String, Object> yzmMap = jdbcTemplate.queryForMap(sql, new Object[] { strOrgid, strPhone, strYzm });

				if (yzmMap != null) {
					Date dtYxq = (Date) yzmMap.get("TZ_YZM_YXQ");
					Date curDate = new Date();
					if (curDate.before(dtYxq)) {
						String uuid = UUID.randomUUID().toString().replaceAll("-","");
						jdbcTemplate.update("INSERT INTO TZ_RESET_UUID_T(TZ_UUID,TZ_PHONE,TZ_YZM) VALUES(?,?,?)",new Object[]{uuid,strPhone,strYzm});
						
						resMap.replace("result", "success");
						resMap.put("uuid",uuid);
//						strResult = "\"success\"";
						errorMsg[0] = "0";

					} else {
						patcha.removeToken(request);
					
						errorMsg[0] = "10";
						errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE",
								"130", "验证码已失效,请重新发送验证码到手机。", "Verification Code has timed out!");
					}
				} else {
					patcha.removeToken(request);
					errorMsg[0] = "20";
					errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "50",
							"验证码不正确", "Wrong Verification Code!");
				}
			} else {
				patcha.removeToken(request);
				errorMsg[0] = "30";
				errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "56",
						"参数错误", "Parameters Error !");
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "100";
			errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "55",
					"获取数据失败，请联系管理员", "Get the data failed, please contact the administrator");
		}
		System.out.println(jacksonUtil.Map2json(resMap));
		return jacksonUtil.Map2json(resMap);
	}

	// 忘记密码-手机找回-修改密码
	public String modifyPasswordByPass(String strParams, String[] errorMsg) {

		String strPwd = "";
		String strRePwd = "";
		String strCheckCode = "";
		String strOprid = "";

		String strPhone = "";
		String strOrgid = "";
		String strLang = "";
		String siteid = "";
		String strResult = "\"failure\"";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			if (jacksonUtil.containsKey("phone") && jacksonUtil.containsKey("orgid") && jacksonUtil.containsKey("lang")
					&& jacksonUtil.containsKey("pwd") && jacksonUtil.containsKey("repwd")
					&& jacksonUtil.containsKey("checkCode") && jacksonUtil.containsKey("siteid"))

				strPhone = jacksonUtil.getString("phone").trim();
			strOrgid = jacksonUtil.getString("orgid").trim();
			strLang = jacksonUtil.getString("lang").trim();
			strCheckCode = jacksonUtil.getString("checkCode").trim();
			strPwd = jacksonUtil.getString("pwd").trim();
			PasswordCheck passwordCheck = new PasswordCheck("",strPwd,strPwd);
			if(StringUtils.isNoneEmpty(strPhone)){
				passwordCheck.setUserName(strPhone);
				if(!passwordCheck.weakLoginPassword()){
					errorMsg[0] = "6";
					errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang,"TZ_SITE_MESSAGE",
							"847",
							"校验失败，非法新密码", "Check failed,Illegal new password");
					return strResult;
				}
			}
			strRePwd = jacksonUtil.getString("repwd").trim();
			strRePwd = jacksonUtil.getString("repwd").trim();
			siteid = jacksonUtil.getString("siteid").trim();

			// 是否存在有效验证码
			String sql = "SELECT TZ_DLZH_ID FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_MOBILE = ? AND TZ_JG_ID = ? AND TZ_RYLX = 'ZCYH' and TZ_SJBD_BZ='Y'";
			strOprid = jdbcTemplate.queryForObject(sql, new Object[] { strPhone, strOrgid }, "String");
			if (strOprid != null && !"".equals(strOprid)) {

				if (strCheckCode != null && !"".equals(strCheckCode)) {
					String strCheckeCodeParas = "{\"checkCode\":\"" + strCheckCode + "\",\"lang\":\"" + strLang
							+ "\",\"jgid\":\"" + strOrgid + "\",\"sen\":\"3\"}";
					String strCheckeCodeResult = siteEnrollClsServiceImpl.tzQuery(strCheckeCodeParas, errorMsg);
					if (!"0".equals(errorMsg[0])) {
						strResult = strCheckeCodeResult;
						return strResult;
					}
				} else {
					errorMsg[0] = "5";
					errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "50",
							"验证码不正确", "The security code is incorrect!");
					return strResult;
				}

				if (strPwd == null || "".equals(strPwd)) {
					errorMsg[0] = "1";
					errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "60",
							"新密码不能为空", "The new password can not be empty!");
					return strResult;
				}

				if (strRePwd == null || "".equals(strRePwd)) {
					errorMsg[0] = "2";
					errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "61",
							"确认密码不能为空", "The confirm password can not be empty!");
					return strResult;
				}

				if (!strPwd.equals(strRePwd)) {
					errorMsg[0] = "3";
					errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "28",
							"新密码和确认密码不一致", "New Password and Confirm Password is not consistent!");
					return strResult;
				}

				// 修改用户密码
				String password = DESUtil.encrypt(strPwd, "TZGD_Tranzvision");
				Psoprdefn psoprdefn = new Psoprdefn();
				psoprdefn.setOprid(strOprid);
				psoprdefn.setOperpswd(password);
				psoprdefnMapper.updateByPrimaryKeySelective(psoprdefn);
				// 把验证码失效;
				String yzmSQL = "UPDATE PS_TZ_SHJI_YZM_TBL SET TZ_EFF_FLAG='N' WHERE TZ_JG_ID=? AND TZ_MOBILE_PHONE=?";
				jdbcTemplate.update(yzmSQL, new Object[] { strOrgid, strPhone });

				Map<String, Object> returnMap = new HashMap<>();
				returnMap.put("result", "success");

				// 修改登录链接
				// String siteIdSQL = "SELECT TZ_SITEI_ID FROM
				// PS_TZ_SITEI_DEFN_T WHERE TZ_JG_ID=? AND TZ_SITEI_ENABLE = 'Y'
				// limit 0,1";
				// String strSiteId = jdbcTemplate.queryForObject(siteIdSQL, new
				// Object[]{strOrgid},"String");
				String strJumUrl = request.getContextPath() + "/user/login/" + strOrgid.toLowerCase() + "/" + siteid;
				returnMap.put("jumpurl", strJumUrl);
				strResult = jacksonUtil.Map2json(returnMap);
				return strResult;
			} else {
				errorMsg[0] = "4";
				errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "62",
						"修改密码失败", "Failed to modify password!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "100";
			errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "55",
					"获取数据失败，请联系管理员", "Get the data failed, please contact the administrator");
		}
		return strResult;
	}

	public String createPageForFixPass(String strParams) {
		String strOrgid = "";
		String strSiteId = "";
		String strLang = "";
		String strPhone = "";
		String strYzm = "";
		String strResult = "";
		String siteid = "";
		String isMobile = "";
		String uuid="";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			strOrgid = jacksonUtil.getString("orgid");
			strLang = jacksonUtil.getString("lang");
//			strPhone = jacksonUtil.getString("phone").trim();
//			strYzm = jacksonUtil.getString("yzm").trim();
			siteid = jacksonUtil.getString("siteid").trim();
			uuid = jacksonUtil.getString("id").trim();
			isMobile = jacksonUtil.getString("isMobile").trim();
			
			strResult = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "55",
					"获取数据失败，请联系管理员", "Get the data failed, please contact the administrator");
			/*
			 * String strStrongMsg =
			 * validateUtil.getMessageTextWithLanguageCd(strOrgid,
			 * strLang,"TZ_SITE_MESSAGE", "122", "密码强度不够",
			 * "Stronger password needed.");
			 */
			String strStrongMsg = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "134",
					"密码格式不符合要求", "Wrong password format.");
			String strNotice = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "123",
					"请重置新密码", "Please Enter New Password.");

			// String siteidSQL = "SELECT TZ_SITEI_ID,TZ_SKIN_ID FROM
			// PS_TZ_SITEI_DEFN_T WHERE TZ_JG_ID=? AND TZ_SITEI_ENABLE='Y'";
			// Map<String, Object> siteMap = jdbcTemplate.queryForMap(siteidSQL,
			// new Object[]{strOrgid});
			String siteidSQL = "SELECT TZ_SITEI_ID,TZ_SKIN_ID FROM PS_TZ_SITEI_DEFN_T WHERE TZ_SITEI_ID=? AND TZ_SITEI_ENABLE='Y'";
			Map<String, Object> siteMap = jdbcTemplate.queryForMap(siteidSQL, new Object[] { siteid });
			strSiteId = (String) siteMap.get("TZ_SITEI_ID");
			String skinId = (String) siteMap.get("TZ_SKIN_ID");
			String contextPath = request.getContextPath();
			String strBeginUrl = contextPath + "/dispatcher";
			/**/
			String imgPath = getSysHardCodeVal.getWebsiteSkinsImgPath();
			imgPath = request.getContextPath() + imgPath + "/" + skinId;
			String str_content = "";
			String loginUrl = contextPath + "/user/login/" + strOrgid.toLowerCase() + "/" + strSiteId;
			Map<String , Object> resMap = jdbcTemplate.queryForMap("select TZ_PHONE,TZ_YZM FROM TZ_RESET_UUID_T WHERE TZ_UUID=?", new Object[]{uuid});
			if(resMap!=null){
				strPhone = resMap.get("TZ_PHONE").toString();
				strYzm = resMap.get("TZ_YZM").toString();
			}else{
				String message =  validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "55",
						"获取数据失败，请联系管理员", "Get the data failed, please contact the administrator");
				str_content = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_TIMEOUT_TIP_HMTL", message,
						strBeginUrl);
				return str_content;
			}
			
			String yzmSQL = "SELECT COUNT(1) FROM PS_TZ_SHJI_YZM_TBL WHERE TZ_EFF_FLAG = 'Y' AND TZ_JG_ID = ? AND TZ_MOBILE_PHONE = ? and TZ_SJYZM= ? LIMIT 0,1";

			int count = jdbcTemplate.queryForObject(yzmSQL, new Object[] { strOrgid, strPhone, strYzm }, "Integer");

			String JGID = jdbcTemplate.queryForObject("select TZ_JG_ID from PS_TZ_SITEI_DEFN_T WHERE TZ_SITEI_ID=?",
					new Object[] { siteid }, "String");

			if (JGID.equals("SEM")) {
				JGID = "";
			} else {
				JGID.toLowerCase();
			}
			System.out.println(this.getClass().getName()+":"+JGID);

			if (count > 0) {
				// 有效；
				if ("ENG".equals(strLang)) {
					if ("Y".equals(isMobile)) {
						str_content = tzGdObject.getHTMLText(
								"HTML.TZWebSiteMRegisteBundle.TZ_GD_MUPDATE_PWD_MB_ENG_HTML", strBeginUrl, strPhone,
								strLang, strOrgid, strStrongMsg, strNotice, contextPath, imgPath, loginUrl, strSiteId,JGID);
					} else {
						str_content = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_UPDATE_PWD_MB_ENG_HTML",
								strBeginUrl, strPhone, strLang, strOrgid, strStrongMsg, strNotice, contextPath, imgPath,
								loginUrl, strSiteId);
					}
				} else {
					if ("Y".equals(isMobile)) {
						str_content = tzGdObject.getHTMLText("HTML.TZWebSiteMRegisteBundle.TZ_GD_MUPDATE_PWD_MB_HTML",
								strBeginUrl, strPhone, strLang, strOrgid, strStrongMsg, strNotice, contextPath, imgPath,
								loginUrl, strSiteId,JGID);
					} else {
						str_content = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_UPDATE_PWD_MB_HTML",
								strBeginUrl, strPhone, strLang, strOrgid, strStrongMsg, strNotice, contextPath, imgPath,
								loginUrl, strSiteId);
					}

				}

				str_content = objRep.repTitle(str_content, strSiteId);
				str_content = objRep.repCss(str_content, strSiteId);
				return str_content;
			} else {
				// 无效；
				if ("Y".equals(isMobile)) {
					strBeginUrl = strBeginUrl + "?classid=enrollCls&siteid=" + strSiteId + "&orgid=" + strOrgid
							+ "&lang=" + strLang + "&sen=10";
				} else {
					strBeginUrl = strBeginUrl + "?classid=enrollCls&siteid=" + strSiteId + "&orgid=" + strOrgid
							+ "&lang=" + strLang + "&sen=4";
				}
				String message = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "130",
						"验证码已失效，请重新发送手机验证码！", "Security Code has timed out, please resend message!");
				str_content = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_TIMEOUT_TIP_HMTL", message,
						strBeginUrl);
				return str_content;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return strResult;
		}
	}

	// 校验手机号码
	public String phoneVerifyByForget(String strParams, String[] errorMsg) {
		String strPhone = "";

		String strOrgid = "";
		String strLang = "";

		String strResult = "\"failure\"";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			if (jacksonUtil.containsKey("phone") && jacksonUtil.containsKey("orgid") && jacksonUtil.containsKey("lang"))
				strPhone = jacksonUtil.getString("phone").trim().toLowerCase();
			strOrgid = jacksonUtil.getString("orgid").trim();
			strLang = jacksonUtil.getString("lang").trim();

			// 手机格式;
			ValidateUtil validateUtil = new ValidateUtil();
			boolean bl = RegExpValidatorUtils.isMobile(strPhone);
			if (bl == false) {
				errorMsg[0] = "1";
				errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "47",
						"手机号码不正确", "The mobile phone is incorrect .");
				return strResult;
			}

			// 是否绑定手机
			String sql = "SELECT COUNT(1) FROM PS_TZ_AQ_YHXX_TBL WHERE LOWER(TZ_MOBILE) = LOWER(?) AND LOWER(TZ_JG_ID)=LOWER(?) AND TZ_JIHUO_ZT ='Y' AND TZ_SJBD_BZ='Y'";
			int count = jdbcTemplate.queryForObject(sql, new Object[] { strPhone, strOrgid }, "Integer");
			if (count <= 0) {
				errorMsg[0] = "2";
				errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "129",
						"手机不存在，请先注册", "The mobile phone does not exist, please register");
				return strResult;
			}

			// 判断该账号是否已锁定
			sql = "SELECT COUNT(1) FROM PS_TZ_AQ_YHXX_TBL A WHERE LOWER(A.TZ_MOBILE) = LOWER(?) AND LOWER(A.TZ_JG_ID)=LOWER(?) AND A.TZ_JIHUO_ZT ='Y' AND exists(SELECT ACCTLOCK FROM PSOPRDEFN WHERE OPRID=A.OPRID AND ACCTLOCK='0')";
			count = jdbcTemplate.queryForObject(sql, new Object[] { strPhone, strOrgid }, "Integer");
			if (count <= 0) {
				errorMsg[0] = "3";
				errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "133",
						"抱歉，该账号已锁定。", "Sorry,this account has been locked.");
				return strResult;
			}

			strResult = "\"success\"";
			return strResult;
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "100";
			errorMsg[1] = validateUtil.getMessageTextWithLanguageCd(strOrgid, strLang, "TZ_SITE_MESSAGE", "55",
					"获取数据失败，请联系管理员", "Get the data failed, please contact the administrator");
		}

		return strResult;
	}

}