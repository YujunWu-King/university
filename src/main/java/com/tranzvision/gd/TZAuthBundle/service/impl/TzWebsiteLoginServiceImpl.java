/**
 * 
 */
package com.tranzvision.gd.TZAuthBundle.service.impl;

import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAccountMgBundle.dao.PsTzAqYhxxTblMapper;
import com.tranzvision.gd.TZAccountMgBundle.model.PsTzAqYhxxTbl;
import com.tranzvision.gd.TZAccountMgBundle.model.PsTzAqYhxxTblKey;
import com.tranzvision.gd.TZAuthBundle.service.TzWebsiteLoginService;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdObjectServiceImpl;
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
 * 机构网站用户登录业务
 * 
 * @author SHIHUA
 * @since 2015-12-21
 */
@Service("com.tranzvision.gd.TZAuthBundle.service.impl.TzWebsiteLoginServiceImpl")
public class TzWebsiteLoginServiceImpl implements TzWebsiteLoginService {

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

	/**
	 * Session存储的用户信息变量名称
	 */
	// public final String userSessionName = "loginUser";
	public final String userSessionName = "loginManager";

	/**
	 * Session存储的用户当前登录语言
	 */
	// public final String sysWebLanguage = "sysWebLanguage";
	public final String sysWebLanguage = "sysLanguage";

	/**
	 * Cookie存储的系统语言信息
	 */
	// public final String cookieWebLang = "tzweblang";
	public final String cookieWebLang = "tzlang";

	/**
	 * Cookie存储的当前访问站点的机构id
	 */
	// public final String cookieWebOrgId = "tzwo";
	public final String cookieWebOrgId = "tzmo";

	/**
	 * Cookie存储的当前访问站点的站点id
	 */
	public final String cookieWebSiteId = "tzws";

	/**
	 * Cookie存储的当前登录网站的用户登录账号
	 */
	// public final String cookieWebLoginedUserName = "tzwu";
	public final String cookieWebLoginedUserName = "tzmu";

	/**
	 * 记录登录类型，后台 - GLY；前台 - SQR；
	 */
	public final String cookieContextLoginType = "TZGD_CONTEXT_LOGIN_TYPE";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tranzvision.gd.TZAuthBundle.service.TzWebsiteLoginService#doLogin(
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.util.ArrayList)
	 */
	@Override
	public boolean doLogin(HttpServletRequest request, HttpServletResponse response, String orgid, String siteid,
			String userName, String userPwd, String code, String language, ArrayList<String> errorMsg) {

		// 校验验证码
		Patchca patchca = new Patchca();
		if (!patchca.verifyToken(request, code)) {
			errorMsg.add("3");
			errorMsg.add(gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_FWINIT_MSGSET",
					"TZGD_FWINIT_00040", language, "输入的验证码不正确。", "Security code is incorrect."));
			return false;
		}

		try {

			// 校验机构和站点的有效性
			String sql = "select TZ_JG_EFF_STA from PS_TZ_JG_BASE_T where TZ_JG_ID=?";
			String tzJgEffStu = sqlQuery.queryForObject(sql, new Object[] { orgid }, "String");
			if (!"Y".equals(tzJgEffStu)) {
				errorMsg.add("2");
				errorMsg.add("登录失败，无效的机构。");
				return false;
			}

			sql = "select TZ_SITEI_ENABLE from PS_TZ_SITEI_DEFN_T where TZ_JG_ID=? and TZ_SITEI_ID=?";
			String tzSiteEffStu = sqlQuery.queryForObject(sql, new Object[] { orgid, siteid }, "String");
			if (!"Y".equals(tzSiteEffStu)) {
				errorMsg.add("2");
				errorMsg.add("登录失败，无效的站点。");
				return false;
			}

			Map<String, Object> dataMap;
			// 校验用户名
			Object[] args = new Object[] { userName, orgid };

			dataMap = sqlQuery.queryForMap(tzSQLObject.getSQLText("SQL.TZAuthBundle.TzGetWebsiteUserOpridByID"), args);

			if (null == dataMap) {
				errorMsg.add("2");
				errorMsg.add(gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_FWINIT_MSGSET",
						"TZGD_FWINIT_00049", language, "登录失败，请确认用户名和密码是否正确。",
						"Email address or password is incorrect ."));
				return false;
			}
			//如果账号不完善，则调过激活步骤
			String strCmpl = dataMap.get("TZ_IS_CMPL") == null ? "" : String.valueOf(dataMap.get("TZ_IS_CMPL"));
			String strJhzt = dataMap.get("TZ_JIHUO_ZT") == null ? "" : String.valueOf(dataMap.get("TZ_JIHUO_ZT"));
			if ("Y"==strCmpl&&!"Y".equals(strJhzt)) {
				errorMsg.add("1");
				errorMsg.add(gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_FWINIT_MSGSET",
						"TZGD_FWINIT_00050", language, "此帐号暂未激活，请激活后重试。", "The account is not activated yet."));
				return false;
			}

			// 校验用户名、密码
			args = new Object[] { dataMap.get("OPRID"), DESUtil.encrypt(userPwd, "TZGD_Tranzvision") };
			String strFlag = sqlQuery.queryForObject(tzSQLObject.getSQLText("SQL.TZAuthBundle.TzCheckManagerPwd"), args,
					"String");

			if (!"Y".equals(strFlag)) {
				errorMsg.add("2");
				errorMsg.add(gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_FWINIT_MSGSET",
						"TZGD_FWINIT_00049", language, "登录失败，请确认用户名和密码是否正确。",
						"Email address or password is incorrect !"));
				return false;
			}

			// 读取用户信息
			PsTzAqYhxxTblKey psTzAqYhxxTblKey = new PsTzAqYhxxTblKey();
			psTzAqYhxxTblKey.setTzDlzhId(dataMap.get("TZ_DLZH_ID").toString());
			psTzAqYhxxTblKey.setTzJgId(dataMap.get("TZ_JG_ID").toString());
			PsTzAqYhxxTbl loginUser = psTzAqYhxxTblMapper.selectByPrimaryKey(psTzAqYhxxTblKey);

			// 设置Session
			TzSession tzSession = new TzSession(request);
			tzSession.addSession(userSessionName, loginUser);

			// 设置语言环境
			this.switchSysLanguage(request, response, language);

			// 设置cookie参数
			tzCookie.addCookie(response, cookieWebOrgId, psTzAqYhxxTblKey.getTzJgId(), 24 * 3600);
			tzCookie.addCookie(response, cookieWebSiteId, siteid, 24 * 3600);
			tzCookie.addCookie(response, cookieWebLoginedUserName, psTzAqYhxxTblKey.getTzDlzhId());
			tzCookie.addCookie(response, cookieContextLoginType, "SQR");

			errorMsg.add("0");
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
			lanaguageCd = tzCookie.getStringCookieVal(request, cookieWebLang);
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
		tzSession.addSession(sysWebLanguage, lanaguageCd);

		// 设置cookie变量
		int cookieMaxAge = 3600 * 24 * 30; // cookie期限是30天
		tzCookie.addCookie(response, cookieWebLang, lanaguageCd, cookieMaxAge);

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
	 * @see com.tranzvision.gd.TZAuthBundle.service.TzWebsiteLoginService#
	 * updateLoginedUserInfo(javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public boolean updateLoginedUserInfo(HttpServletRequest request, HttpServletResponse response) {

		boolean boolRet = false;

		String strDlzhId = this.getLoginedUserDlzhid(request);
		String strOrgId = this.getLoginedUserOrgid(request);

		// 读取用户信息
		PsTzAqYhxxTblKey psTzAqYhxxTblKey = new PsTzAqYhxxTblKey();
		psTzAqYhxxTblKey.setTzDlzhId(strDlzhId);
		psTzAqYhxxTblKey.setTzJgId(strOrgId);
		PsTzAqYhxxTbl loginUser = psTzAqYhxxTblMapper.selectByPrimaryKey(psTzAqYhxxTblKey);

		if (null != loginUser) {
			// 设置Session
			TzSession tzSession = new TzSession(request);
			tzSession.removeSession(userSessionName);
			tzSession.addSession(userSessionName, loginUser);
			boolRet = true;
		}

		return boolRet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tranzvision.gd.TZAuthBundle.service.TzWebsiteLoginService#
	 * getLoginedUserInfo(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public PsTzAqYhxxTbl getLoginedUserInfo(HttpServletRequest request) {
		// 从Session中获取登录用户信息
		TzSession tzSession = new TzSession(request);
		Object obj = tzSession.getSession(userSessionName);
		PsTzAqYhxxTbl psTzAqYhxxTbl;
		if (null == obj) {
			boolean debugging = getCookieSessionProps.getDebug();
			if (debugging) {
				psTzAqYhxxTbl = new PsTzAqYhxxTbl();
				psTzAqYhxxTbl.setTzRealname("管理员");
			} else {
				psTzAqYhxxTbl = null;
			}
		} else {
			psTzAqYhxxTbl = (PsTzAqYhxxTbl) obj;
		}
		return psTzAqYhxxTbl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tranzvision.gd.TZAuthBundle.service.TzWebsiteLoginService#
	 * getLoginedUserOprid(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public String getLoginedUserOprid(HttpServletRequest request) {
		// 从Session中获取登录用户信息，返回oprid
		TzSession tzSession = new TzSession(request);
		PsTzAqYhxxTbl psTzAqYhxxTbl = (PsTzAqYhxxTbl) tzSession.getSession(userSessionName);
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
	 * @see com.tranzvision.gd.TZAuthBundle.service.TzWebsiteLoginService#
	 * getLoginedUserDlzhid(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public String getLoginedUserDlzhid(HttpServletRequest request) {
		// 从Session中获取登录用户信息，返回orgid
		TzSession tzSession = new TzSession(request);
		PsTzAqYhxxTbl psTzAqYhxxTbl = (PsTzAqYhxxTbl) tzSession.getSession(userSessionName);
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
	 * @see com.tranzvision.gd.TZAuthBundle.service.TzWebsiteLoginService#
	 * getLoginedUserOrgid(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public String getLoginedUserOrgid(HttpServletRequest request) {
		// 从Session中获取登录用户信息，返回orgid
		TzSession tzSession = new TzSession(request);
		PsTzAqYhxxTbl psTzAqYhxxTbl = (PsTzAqYhxxTbl) tzSession.getSession(userSessionName);
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
		Object lang = tzSession.getSession(sysWebLanguage);
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
	 * @see com.tranzvision.gd.TZAuthBundle.service.TzWebsiteLoginService#
	 * checkUserLogin(javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public boolean checkUserLogin(HttpServletRequest request, HttpServletResponse response) {
		// 判断Session中是否存在登录用户信息
		TzSession tzSession = new TzSession(request);
		return tzSession.checkSession(userSessionName);
	}

	/**
	 * (non-Javadoc)
	 * @param userName
	 * @param OrgId
	 * @return
	 */
	public boolean getLoginIndex(String userName,String OrgId){
		//校验登录用户是否注册完整
		boolean infoIsCmpl = true;
		String sql = "SELECT TZ_IS_CMPL FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_DLZH_ID=? AND TZ_JG_ID=?";
		String tzIsCmpl = sqlQuery.queryForObject(sql, new Object[] { userName,OrgId }, "String");
		if(!"Y".equals(tzIsCmpl)){
			infoIsCmpl = false;
		}		
		return infoIsCmpl;
	}
}
