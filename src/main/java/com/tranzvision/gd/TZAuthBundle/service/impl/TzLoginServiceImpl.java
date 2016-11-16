/**
 * 
 */
package com.tranzvision.gd.TZAuthBundle.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAccountMgBundle.dao.PsTzAqYhxxTblMapper;
import com.tranzvision.gd.TZAccountMgBundle.model.PsTzAqYhxxTbl;
import com.tranzvision.gd.TZAccountMgBundle.model.PsTzAqYhxxTblKey;
import com.tranzvision.gd.TZAuthBundle.service.TzLoginService;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdObjectServiceImpl;
import com.tranzvision.gd.TZOnTrialBundle.dao.PsTzOnTrialTMapper;
import com.tranzvision.gd.TZOnTrialBundle.model.PsTzOnTrialTWithBLOBs;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.captcha.Patchca;
import com.tranzvision.gd.util.cfgdata.GetCookieSessionProps;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.cookie.TzCookie;
import com.tranzvision.gd.util.encrypt.DESUtil;
import com.tranzvision.gd.util.session.TzSession;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 后台用户登录业务
 * 
 * @author SHIHUA
 * @since 2015-11-05
 */
@Service("com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl")
public class TzLoginServiceImpl implements TzLoginService {

	@Autowired
	private TZGDObject tzSQLObject;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private PsTzAqYhxxTblMapper psTzAqYhxxTblMapper;

	@Autowired
	private GetCookieSessionProps getCookieSessionProps;

	@Autowired
	private TzCookie tzCookie;

	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	@Autowired
	private GdObjectServiceImpl gdObjectServiceImpl;

	@Autowired
	private PsTzOnTrialTMapper psTzOnTrialTMapper;

	/**
	 * Session存储的用户信息变量名称
	 */
	public final String managerSessionName = "loginManager";

	/**
	 * Session存储的用户当前登录语言
	 */
	public final String sysLanguage = "sysLanguage";

	/**
	 * Cookie存储的系统语言信息
	 */
	public final String cookieLang = "tzlang";

	/**
	 * Cookie存储的当前登录机构id
	 */
	public final String cookieOrgId = "tzmo";

	/**
	 * Cookie存储的当前登录用户登录名
	 */
	public final String cookieLoginedAdminName = "tzmu";

	/**
	 * 记录登录类型，后台 - GLY；前台 - SQR；
	 */
	public final String cookieContextLoginType = "TZGD_CONTEXT_LOGIN_TYPE";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tranzvision.gd.TZAuthBundle.service.TzLoginService#doLogin(javax.
	 * servlet.http.HttpServletRequest,javax.servlet.http.HttpServletResponse,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean doLogin(HttpServletRequest request, HttpServletResponse response, String orgid, String userName,
			String userPwd, String code, ArrayList<String> errorMsg) {

		// 校验验证码
		Patchca patchca = new Patchca();
		if (!patchca.verifyToken(request, code)) {
			errorMsg.add("2");
			errorMsg.add("输入的验证码不正确。");
			return false;
		}

		try {

			// 校验机构的有效性
			String sql = "select TZ_JG_EFF_STA from PS_TZ_JG_BASE_T where TZ_JG_ID=?";
			String tzJgEffStu = sqlQuery.queryForObject(sql, new Object[] { orgid }, "String");
			if (!"Y".equals(tzJgEffStu)) {
				errorMsg.add("2");
				errorMsg.add("登录失败，无效的机构。");
				return false;
			}

			Map<String, Object> dataMap;

			// 校验用户名
			Object[] args = new Object[] { userName, orgid };

			dataMap = sqlQuery.queryForMap(tzSQLObject.getSQLText("SQL.TZAuthBundle.TzGetOpridByID"), args);

			if (null == dataMap) {
				dataMap = sqlQuery.queryForMap(tzSQLObject.getSQLText("SQL.TZAuthBundle.TzGetOpridByEmail"), args);
			}

			if (null == dataMap) {
				dataMap = sqlQuery.queryForMap(tzSQLObject.getSQLText("SQL.TZAuthBundle.TzGetOpridByMobile"), args);
			}

			if (null == dataMap) {
				errorMsg.add("2");
				errorMsg.add("登录失败，请确认用户名和密码是否正确。");
				return false;
			}

			// 校验用户名、密码
			args = new Object[] { dataMap.get("OPRID"), DESUtil.encrypt(userPwd, "TZGD_Tranzvision") };
			String strFlag = sqlQuery.queryForObject(tzSQLObject.getSQLText("SQL.TZAuthBundle.TzCheckManagerPwd"), args,
					"String");

			if (!"Y".equals(strFlag)) {
				errorMsg.add("2");
				errorMsg.add("登录失败，请确认用户名和密码是否正确。");
				return false;
			}

			// 判断是不是试用账号;
			int seqNum = 0;
			try {
				seqNum = sqlQuery.queryForObject(
						"select TZ_SEQ_NUM from PS_TZ_ON_TRIAL_T where TZ_JG_ID=? AND TZ_DLZH_ID=? limit 0,1",
						new Object[] { orgid, userName }, "Integer");
			} catch (Exception e) {
				seqNum = 0;
			}
			if (seqNum > 0) {
				// 是使用账号，判断是不是第一次登录;
				PsTzOnTrialTWithBLOBs psTzOnTrialT = psTzOnTrialTMapper.selectByPrimaryKey(seqNum);
				Date startTime = psTzOnTrialT.getTzStartTime();
				Date endTime = psTzOnTrialT.getTzEndTime();
				if (startTime == null || endTime == null) {
					// 第一次登录;
					Calendar c = Calendar.getInstance();
					startTime = c.getTime();
					c.add(Calendar.DAY_OF_MONTH, 14);
					endTime = c.getTime();
					psTzOnTrialT.setTzStartTime(startTime);
					psTzOnTrialT.setTzEndTime(endTime);
					psTzOnTrialTMapper.updateByPrimaryKeySelective(psTzOnTrialT);
				} else {
					//判断是不是已经购买了;
					int count = 0;
					try {
						count = sqlQuery.queryForObject(
								"select count(1) from PS_TZ_ON_TRIAL_T where TZ_JG_ID=? AND TZ_DLZH_ID=? and TZ_SF_SALE='Y' limit 0,1",
								new Object[] { orgid, userName }, "Integer");
					} catch (Exception e) {
						count = 0;
					}
					if(count <= 0){
						// 如果没有购买则判断试用时间是不是失效;
						Date currentDate = new Date();
						if (currentDate.after(endTime)) {
							errorMsg.add("3");
							errorMsg.add("试用账号已经过期。");
							return false;
						}
					}
					
				}

			}

			// 读取用户信息
			PsTzAqYhxxTblKey psTzAqYhxxTblKey = new PsTzAqYhxxTblKey();
			psTzAqYhxxTblKey.setTzDlzhId(dataMap.get("TZ_DLZH_ID").toString());
			psTzAqYhxxTblKey.setTzJgId(dataMap.get("TZ_JG_ID").toString());
			PsTzAqYhxxTbl loginManager = psTzAqYhxxTblMapper.selectByPrimaryKey(psTzAqYhxxTblKey);

			// 设置Session
			TzSession tzSession = new TzSession(request);
			tzSession.addSession(managerSessionName, loginManager);

			// 设置语言环境
			this.switchSysLanguage(request, response, "");

			// 设置cookie参数
			tzCookie.addCookie(response, cookieOrgId, psTzAqYhxxTblKey.getTzJgId());
			tzCookie.addCookie(response, cookieLoginedAdminName, psTzAqYhxxTblKey.getTzDlzhId());
			tzCookie.addCookie(response, cookieContextLoginType, "GLY");

			errorMsg.add("success");
			errorMsg.add("");
			return true;

		} catch (TzSystemException tze) {
			tze.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tranzvision.gd.TZAuthBundle.service.TzLoginService#switchSysLanguage(
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse, java.lang.String)
	 */
	public void switchSysLanguage(HttpServletRequest request, HttpServletResponse response, String lanaguageCd) {

		if (null == lanaguageCd || "".equals(lanaguageCd)) {

			// 从cookie中获取用户上次使用的系统语言
			lanaguageCd = tzCookie.getStringCookieVal(request, cookieLang);
			if (null == lanaguageCd || "".equals(lanaguageCd)) {
				// 若cookie中没有或无效，则从系统记录表中获取用户上次登录的系统语言
				lanaguageCd = gdObjectServiceImpl.getUserGxhLanguage(request, response);
			}

		}

		// 校验语言的有效性
		if (!gdObjectServiceImpl.isLanguageCdValid(lanaguageCd)) {
			lanaguageCd = "";
		}

		// 若没有有效的语言，则取默认语言
		if ("".equals(lanaguageCd)) {
			lanaguageCd = getSysHardCodeVal.getSysDefaultLanguage();
		}

		// 设置session变量
		TzSession tzSession = new TzSession(request);
		tzSession.addSession(sysLanguage, lanaguageCd);

		// 设置cookie变量
		int cookieMaxAge = 3600 * 24 * 30; // cookie期限是30天
		tzCookie.addCookie(response, cookieLang, lanaguageCd, cookieMaxAge);

		// 更新系统记录
		gdObjectServiceImpl.setUserGXHSXValue(request, response, "LANGUAGECD", lanaguageCd);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tranzvision.gd.TZAuthBundle.service.TzLoginService#doLogout(javax.
	 * servlet.http.HttpServletRequest,javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void doLogout(HttpServletRequest request, HttpServletResponse response) {
		// 销毁session，登出
		TzSession tzSession = new TzSession(request);
		tzSession.invalidate(request, response);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tranzvision.gd.TZAuthBundle.service.TzLoginService#
	 * getLoginedManagerInfo(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public PsTzAqYhxxTbl getLoginedManagerInfo(HttpServletRequest request) {
		// 从Session中获取登录用户信息
		TzSession tzSession = new TzSession(request);
		return (PsTzAqYhxxTbl) tzSession.getSession(managerSessionName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tranzvision.gd.TZAuthBundle.service.TzLoginService#
	 * getLoginedManagerOprid(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public String getLoginedManagerOprid(HttpServletRequest request) {
		// 从Session中获取登录用户信息，返回oprid  用户ID
		TzSession tzSession = new TzSession(request);
		PsTzAqYhxxTbl psTzAqYhxxTbl = (PsTzAqYhxxTbl) tzSession.getSession(managerSessionName);
		if (null != psTzAqYhxxTbl) {
			return psTzAqYhxxTbl.getOprid();
		} else {
			if (getCookieSessionProps == null) {
				getCookieSessionProps = new GetCookieSessionProps();
			}
			boolean debugging = getCookieSessionProps.getDebug();
			String strRtn = "";
			if (debugging) {
				strRtn = "TZ_7";
			}
			return strRtn;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tranzvision.gd.TZAuthBundle.service.TzLoginService#
	 * getLoginedManagerDlzhid(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public String getLoginedManagerDlzhid(HttpServletRequest request) {
		// 从Session中获取登录用户信息，返回orgid  机构登陆名
		TzSession tzSession = new TzSession(request);
		PsTzAqYhxxTbl psTzAqYhxxTbl = (PsTzAqYhxxTbl) tzSession.getSession(managerSessionName);
		if (null != psTzAqYhxxTbl) {
			return psTzAqYhxxTbl.getTzDlzhId();
		} else {
			if (getCookieSessionProps == null) {
				getCookieSessionProps = new GetCookieSessionProps();
			}
			boolean debugging = getCookieSessionProps.getDebug();
			String strRtn = "";
			if (debugging) {
				strRtn = "Admin";
			}
			return strRtn;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tranzvision.gd.TZAuthBundle.service.TzLoginService#
	 * getLoginedManagerOrgid(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public String getLoginedManagerOrgid(HttpServletRequest request) {
		// 从Session中获取登录用户信息，返回orgid 机构ID
		TzSession tzSession = new TzSession(request);
		PsTzAqYhxxTbl psTzAqYhxxTbl = (PsTzAqYhxxTbl) tzSession.getSession(managerSessionName);
		if (null != psTzAqYhxxTbl) {
			return psTzAqYhxxTbl.getTzJgId();
		} else {
			if (getCookieSessionProps == null) {
				getCookieSessionProps = new GetCookieSessionProps();
			}
			boolean debugging = getCookieSessionProps.getDebug();
			String strRtn = "";
			if (debugging) {
				strRtn = "ADMIN";
				// strRtn = "SHTEST";
			}
			return strRtn;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tranzvision.gd.TZAuthBundle.service.TzLoginService#getSysLanaguageCD(
	 * javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public String getSysLanaguageCD(HttpServletRequest request) {
		// 从Session中获取当前用户正在使用的系统语言
		TzSession tzSession = new TzSession(request);
		Object lang = tzSession.getSession(sysLanguage);
		if (null != lang) {
			return String.valueOf(lang);
		} else {
			if (getCookieSessionProps == null) {
				getCookieSessionProps = new GetCookieSessionProps();
			}
			boolean debugging = getCookieSessionProps.getDebug();
			String strRtn = "";
			if (debugging) {
				strRtn = getSysHardCodeVal.getSysDefaultLanguage();
			}
			return strRtn;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tranzvision.gd.TZAuthBundle.service.TzLoginService#checkManagerLogin(
	 * javax.servlet.http.HttpServletRequest,javax.servlet.http.
	 * HttpServletResponse)
	 */
	@Override
	public boolean checkManagerLogin(HttpServletRequest request, HttpServletResponse response) {
		// 判断Session中是否存在登录用户信息
		TzSession tzSession = new TzSession(request);
		return tzSession.checkSession(managerSessionName);
	}

}
