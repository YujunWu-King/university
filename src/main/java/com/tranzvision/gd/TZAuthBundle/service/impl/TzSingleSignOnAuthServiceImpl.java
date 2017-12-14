/**
 * 
 */
package com.tranzvision.gd.TZAuthBundle.service.impl;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAccountMgBundle.dao.PsTzAqYhxxTblMapper;
import com.tranzvision.gd.TZAccountMgBundle.model.PsTzAqYhxxTbl;
import com.tranzvision.gd.TZAccountMgBundle.model.PsTzAqYhxxTblKey;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.cookie.TzCookie;
import com.tranzvision.gd.util.session.TzSession;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

@Service
public class TzSingleSignOnAuthServiceImpl {

	@Autowired
	private TZGDObject tzSQLObject;
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private PsTzAqYhxxTblMapper psTzAqYhxxTblMapper;
	@Autowired
	private TzCookie tzCookie;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
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
	 * 加密类型：
	 * MD2
	 * MD5
	 * SHA-1
	 * SHA-256
	 * SHA-384
	 * SHA-512
	 * */
	public static String encrypt(String encryptName,String srcString){
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance(encryptName);
            messageDigest.update(srcString.getBytes("UTF-8"));
            encodeStr = bytes2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} 
        return encodeStr;
    }

	/**
	 * 二进制转十六进制
	 * @param bytes
	 * @return
	 */
	public static String bytes2Hex(byte[] bytes) {
		StringBuffer md5str = new StringBuffer();
		//把数组每一字节换成16进制连成md5字符串
		int digital;
		for (int i = 0; i < bytes.length; i++) {
			 digital = bytes[i];

			if(digital < 0) {
				digital += 256;
			}
			if(digital < 16){
				md5str.append("0");
			}
			md5str.append(Integer.toHexString(digital));
		}
		return md5str.toString().toUpperCase();
	}
	
	public boolean login(HttpServletRequest request, HttpServletResponse response,String orgId, String userName) {

		try {

			Map<String, Object> dataMap;

			// 校验用户名
			Object[] args = new Object[] { userName, orgId };

			dataMap = sqlQuery.queryForMap(tzSQLObject.getSQLText("SQL.TZAuthBundle.TzGetOpridByID"), args);
			if (null == dataMap) {
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

			// 设置语言环境
			tzLoginServiceImpl.switchSysLanguage(request, response, "");

			// 设置cookie参数
			tzCookie.addCookie(response, cookieOrgId, psTzAqYhxxTblKey.getTzJgId());
			tzCookie.addCookie(response, cookieLoginedAdminName, psTzAqYhxxTblKey.getTzDlzhId());
			tzCookie.addCookie(response, cookieContextLoginType, "GLY");

			return true;

		} catch (TzSystemException tze) {
			tze.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

}
