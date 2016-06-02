/**
 * 
 */
package com.tranzvision.gd.TZAuthBundle.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzWebsiteLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdObjectServiceImpl;
import com.tranzvision.gd.TZSitePageBundle.service.impl.TzWebsiteServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.cookie.TzCookie;
import com.tranzvision.gd.util.security.TzFilterIllegalCharacter;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 机构网站登录前端控制器
 * 
 * @author SHIHUA
 * @since 2015-12-17
 */
@Controller
@RequestMapping(value = { "/user/login" })
public class TzWebsiteLoginController {

	@Autowired
	private TzWebsiteLoginServiceImpl tzWebsiteLoginServiceImpl;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TzFilterIllegalCharacter tzFilterIllegalCharacter;

	@Autowired
	private GdObjectServiceImpl gdObjectServiceImpl;

	@Autowired
	private TZGDObject tzGDObject;

	@Autowired
	private TzCookie tzCookie;

	@Autowired
	private TzWebsiteServiceImpl tzWebsiteServiceImpl;

	@RequestMapping(value = { "/{orgid}/{siteid}" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String userLoginWebsite(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "orgid") String orgid, @PathVariable(value = "siteid") String siteid) {

		String strRet = "";

		try {
			orgid = tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(orgid).toUpperCase();

			siteid = tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(siteid);

			if (null != orgid && !"".equals(orgid) && null != siteid && !"".equals(siteid)) {

				String loginHtml = tzWebsiteServiceImpl.getLoginPublishCode(request, orgid, siteid);
				strRet = loginHtml;
			} else {

				strRet = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "", "", "", "访问站点异常，请检查您访问的地址是否正确。",
						"Can not visit the site.Please check the url.");

			}
		} catch (Exception e) {
			e.printStackTrace();
			strRet = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "", "", "", "访问站点异常，请检查您访问的地址是否正确。",
					"Can not visit the site.Please check the url.");
		}

		return strRet;

	}

	@RequestMapping(value = "dologin", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String doLogin(HttpServletRequest request, HttpServletResponse response) {

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(request.getParameter("tzParams"));

		Map<String, Object> mapData = jacksonUtil.getMap("comParams");

		String strOrgId = mapData.get("orgid") == null ? "" : String.valueOf(mapData.get("orgid"));
		String strUserName = mapData.get("userName") == null ? "" : String.valueOf(mapData.get("userName")).trim();
		String strPassWord = mapData.get("passWord") == null ? "" : String.valueOf(mapData.get("passWord"));
		String strYzmCode = mapData.get("yzmCode") == null ? "" : String.valueOf(mapData.get("yzmCode"));
		String strLang = mapData.get("lang") == null ? "" : String.valueOf(mapData.get("lang"));

		Map<String, Object> jsonMap = new HashMap<String, Object>();

		try {

			if (null != strOrgId && !"".equals(strOrgId)) {
				strOrgId = strOrgId.toUpperCase();
				String sql = tzGDObject.getSQLText("SQL.TZAuthBundle.TzGetSiteidByOrgid");

				String strSiteId = sqlQuery.queryForObject(sql, new Object[] { strOrgId }, "String");

				if (!"".equals(strUserName)) {
					strUserName = strUserName.toLowerCase();
				}

				if (null != strSiteId && !"".equals(strSiteId)) {
					sql = tzGDObject.getSQLText("SQL.TZAuthBundle.TzGetZcyhDlzhId");
					strUserName = sqlQuery.queryForObject(sql, new Object[] { strUserName, strUserName, strOrgId },
							"String");
					if (null != strUserName && !"".equals(strUserName)) {
						ArrayList<String> aryErrorMsg = new ArrayList<String>();

						boolean boolResult = tzWebsiteLoginServiceImpl.doLogin(request, response, strOrgId, strSiteId,
								strUserName, strPassWord, strYzmCode, strLang, aryErrorMsg);

						String loginStatus = aryErrorMsg.get(0);
						String errorMsg = aryErrorMsg.get(1);
						if (boolResult) {
							jsonMap.put("success", "true");
						} else {
							jsonMap.put("success", "false");
						}

						jsonMap.put("errorCode", loginStatus);
						jsonMap.put("errorDesc", errorMsg);

						if (boolResult) {

							String ctxPath = request.getContextPath();

							String indexUrl = ctxPath + "/site/index/" + strOrgId.toLowerCase() + "/" + strSiteId;

							jsonMap.put("url", indexUrl);

						}

					} else {

						int errorCode = 2;
						String strErrorDesc = gdObjectServiceImpl.getMessageTextWithLanguageCd(request,
								"TZGD_FWINIT_MSGSET", "TZGD_FWINIT_00049", strLang, "登录失败，请确认用户名和密码是否正确。",
								"Email address or password is incorrect .");
						jsonMap.put("success", "false");
						jsonMap.put("errorCode", errorCode);
						jsonMap.put("errorDesc", strErrorDesc);

					}

				} else {
					int errorCode = 5;
					String strErrorDesc = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZ_SITE_MESSAGE",
							"44", strLang, "站点异常", "The website is abnormal .");
					jsonMap.put("success", "false");
					jsonMap.put("errorCode", errorCode);
					jsonMap.put("errorDesc", strErrorDesc);
				}
			} else {
				int errorCode = 5;
				String strErrorDesc = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZ_SITE_MESSAGE", "45",
						strLang, "机构异常", "The organization is abnormal .");
				jsonMap.put("success", "false");
				jsonMap.put("errorCode", errorCode);
				jsonMap.put("errorDesc", strErrorDesc);
			}
		} catch (TzSystemException e) {
			e.printStackTrace();
		}

		// {"success":"%bind(:1)","error":"%bind(:2)","indexUrl":"%bind(:3)"}

		return jacksonUtil.Map2json(jsonMap);
	}

	@RequestMapping(value = "logout")
	public String doLogout(HttpServletRequest request, HttpServletResponse response) {

		String orgid = tzCookie.getStringCookieVal(request, tzWebsiteLoginServiceImpl.cookieWebOrgId);

		String siteid = tzCookie.getStringCookieVal(request, tzWebsiteLoginServiceImpl.cookieWebSiteId);

		tzWebsiteLoginServiceImpl.doLogout(request, response);

		// String ctx = request.getContextPath();

		orgid = orgid.toLowerCase();

		String redirect = "redirect:" + "/user/login/" + orgid + "/" + siteid;

		return redirect;
	}

}
