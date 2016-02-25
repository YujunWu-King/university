/**
 * 
 */
package com.tranzvision.gd.TZAuthBundle.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.TzComPageAuthService;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 组件、页面访问许可权校验实现类
 * 
 * @author SHIHUA
 * @since 2016-01-29
 */
@Service("com.tranzvision.gd.TZAuthBundle.service.impl.TzComPageAuthServiceImpl")
public class TzComPageAuthServiceImpl implements TzComPageAuthService {

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzGDObject;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tranzvision.gd.TZAuthBundle.service.TzComPageAuthService#
	 * checkPermission(java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String[])
	 */
	@Override
	public boolean checkPermission(String oprid, String comName, String pageName, String vType, String[] errorMsg) {

		if (comName == null || "".equals(comName) || pageName == null || "".equals(pageName)) {
			errorMsg[0] = "1";
			errorMsg[1] = "非法访问，未指定组件ID或者页面ID。";
			return false;
		}

		try {

			String isZcSQL = "select 'Y' from PS_TZ_AQ_COMZC_TBL where TZ_COM_ID=?";
			String isExistCom = sqlQuery.queryForObject(isZcSQL, new Object[] { comName }, "String");

			if (!"Y".equals(isExistCom)) {
				errorMsg[0] = "1";
				errorMsg[1] = "非法访问，该组件[" + comName + "]未注册。";
				return false;
			}

			String isPageSQL = "select 'Y' from PS_TZ_AQ_PAGZC_TBL where TZ_COM_ID=? and TZ_PAGE_ID=?";
			String isExistPage = sqlQuery.queryForObject(isPageSQL, new Object[] { comName, pageName }, "String");

			if (!"Y".equals(isExistPage)) {
				errorMsg[0] = "1";
				errorMsg[1] = "非法访问，组件页面[" + comName + "][" + pageName + "]未注册。";
				return false;
			}

			/* 权限校验 */
			int existComQx = 0;
			String existSQL = "";
			switch (vType) {
			case "R":
				existSQL = tzGDObject.getSQLText("SQL.TZAuthBundle.TzCheckUserComPgReadPermit");
				break;

			case "W":
			default:
				existSQL = tzGDObject.getSQLText("SQL.TZAuthBundle.TzCheckUserComPgUpdatePermit");
				break;
			}

			try {
				existComQx = sqlQuery.queryForObject(existSQL,
						new Object[] { oprid, comName, comName + "$%", pageName }, "Integer");
			} catch (Exception ei) {
				existComQx = 0;
			}

			if (existComQx != 1) {
				errorMsg[0] = "1";
				errorMsg[1] = "非法访问，您对组件页面[" + comName + "][" + pageName + "]的访问未获得授权。";
				return false;
			}

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "非法访问，您对组件页面[" + comName + "][" + pageName + "]的访问未获得授权。";
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tranzvision.gd.TZAuthBundle.service.TzComPageAuthService#
	 * checkReadPermission(java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String[])
	 */
	@Override
	public boolean checkReadPermission(String oprid, String comName, String pageName, String[] errorMsg) {

		return this.checkPermission(oprid, comName, pageName, "R", errorMsg);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tranzvision.gd.TZAuthBundle.service.TzComPageAuthService#
	 * checkUpdatePermission(java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.String[])
	 */
	@Override
	public boolean checkUpdatePermission(String oprid, String comName, String pageName, String[] errorMsg) {

		return this.checkPermission(oprid, comName, pageName, "W", errorMsg);
	}

}
