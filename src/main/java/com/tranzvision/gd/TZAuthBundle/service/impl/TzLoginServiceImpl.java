/**
 * 
 */
package com.tranzvision.gd.TZAuthBundle.service.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAccountMgBundle.dao.PsTzAqYhxxTblMapper;
import com.tranzvision.gd.TZAccountMgBundle.model.PsTzAqYhxxTbl;
import com.tranzvision.gd.TZAccountMgBundle.model.PsTzAqYhxxTblKey;
import com.tranzvision.gd.TZAuthBundle.service.TzLoginService;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.captcha.Patchca;
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
	
	private Patchca patchca;

	private String loginStatus;

	private String errorMsg;

	/**
	 * Session存储的用户信息变量名称
	 */
	final static String managerSessionName = "loginManager";

	/**
	 * 获取登录结果
	 * 
	 * @return String 登录结果：成功 - success ；失败 - 其他值
	 */
	public String getLoginStatus() {
		return loginStatus;
	}

	/**
	 * 获取错误描述信息
	 * 
	 * @return String
	 */
	public String getErrorMsg() {
		return errorMsg;
	}

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
			String userPwd, String code) {

		// 校验验证码
		patchca = new Patchca();
		if (!patchca.verifyToken(request, code)) {
			loginStatus = "2";
			errorMsg = "输入的验证码不正确。";
			return false;
		}

		try {
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
				loginStatus = "2";
				errorMsg = "登录失败，请确认用户名和密码是否正确。";
				return false;
			}

			// 校验用户名、密码
			args = new Object[] { dataMap.get("OPRID"), DESUtil.encrypt(userPwd, "TZGD_Tranzvision") };
			String strFlag = sqlQuery.queryForObject(tzSQLObject.getSQLText("SQL.TZAuthBundle.TzCheckManagerPwd"), args,
					"String");

			if (!"Y".equals(strFlag)) {
				loginStatus = "2";
				errorMsg = "登录失败，请确认用户名和密码是否正确。";
				return false;
			}

			// 读取用户信息
			PsTzAqYhxxTblKey psTzAqYhxxTblKey = new PsTzAqYhxxTblKey();
			psTzAqYhxxTblKey.setTzDlzhId(dataMap.get("TZ_DLZH_ID").toString());
			psTzAqYhxxTblKey.setTzJgId(dataMap.get("TZ_JG_ID").toString());
			PsTzAqYhxxTbl loginManager = psTzAqYhxxTblMapper.selectByPrimaryKey(psTzAqYhxxTblKey);

			// 设置Session
			TzSession tzSession = new TzSession(request);
			tzSession.addSession(managerSessionName, loginManager);

			loginStatus = "success";
			errorMsg = "";
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
