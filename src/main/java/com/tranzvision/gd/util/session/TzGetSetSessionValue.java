/**
 * 
 */
package com.tranzvision.gd.util.session;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 设置、获取指定Session变量的类方法
 * 
 * @author SHIHUA
 * @since 2015-12-15
 */
@Service
public class TzGetSetSessionValue {

	@Autowired
	private HttpServletRequest request;

	private String getSessionVal(String sessionName) {
		TzSession tzSession = new TzSession(request);
		Object objSessionVal = tzSession.getSession(sessionName);
		return objSessionVal == null ? null : String.valueOf(objSessionVal);
	}

	private void setSessionVal(String sessionName, String sessionValue) {
		TzSession tzSession = new TzSession(request);
		tzSession.addSession(sessionName, sessionValue);
	}

	public String getTzSiteGloMenuId() {
		return this.getSessionVal(TzSessionName.TzSiteGloMenuId);
	}

	public void setTzSiteGloMenuId(String sessionValue) {
		this.setSessionVal(TzSessionName.TzSiteGloMenuId, sessionValue);
	}

	public String getTzAddingNewActivity() {
		return this.getSessionVal(TzSessionName.TzAddingNewActivity);
	}

	public void setTzAddingNewActivity(String sessionValue) {
		this.setSessionVal(TzSessionName.TzAddingNewActivity, sessionValue);
	}

}
