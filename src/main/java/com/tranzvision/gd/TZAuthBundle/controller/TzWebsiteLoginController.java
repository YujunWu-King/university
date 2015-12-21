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
import org.springframework.web.servlet.ModelAndView;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzWebsiteLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdObjectServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
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
	private JacksonUtil jacksonUtil;

	@Autowired
	private TzFilterIllegalCharacter tzFilterIllegalCharacter;

	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	@Autowired
	private GdObjectServiceImpl gdObjectServiceImpl;

	@Autowired
	private TZGDObject tzGDObject;

	@RequestMapping(value = { "/{orgid}" })
	@ResponseBody
	public String userLoginOrg(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "orgid") String orgid) {

		orgid = tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(orgid).toUpperCase();

		if (null == orgid || "".equals(orgid)) {

		}

		return "";

	}

	@RequestMapping(value = "dologin", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String doLogin(HttpServletRequest request, HttpServletResponse response) {

		jacksonUtil.json2Map(request.getParameter("tzParams"));

		String strOrgId = jacksonUtil.getString("orgid");
		String strUserName = jacksonUtil.getString("userName");
		String strPassWord = jacksonUtil.getString("passWord");
		String strYzmCode = jacksonUtil.getString("yzmCode");
		String strLang = jacksonUtil.getString("lang");

		Map<String, Object> jsonMap = new HashMap<String, Object>();

		try {

			if (null != strOrgId && !"".equals(strOrgId)) {
				String sql = tzGDObject.getSQLText("SQL.TZAuthBundle.TzGetSiteidByOrgid");

				String strSiteId = sqlQuery.queryForObject(sql, new Object[] { strOrgId }, "String");

				if (null != strSiteId && !"".equals(strSiteId)) {

					sql = tzGDObject.getSQLText("SQL.TZAuthBundle.TzGetZcyhDlzhId");
					strUserName = sqlQuery.queryForObject(sql, new Object[] { strUserName, strUserName, strOrgId },
							"String");

					if (null != strUserName && !"".equals(strUserName)) {
						ArrayList<String> aryErrorMsg = new ArrayList<String>();

						boolean boolResult = tzWebsiteLoginServiceImpl.doLogin(request, response, strOrgId, strUserName, strPassWord,
								strYzmCode, strLang, aryErrorMsg);
						
						
						
						String loginStatus = aryErrorMsg.get(0);
						String errorMsg = aryErrorMsg.get(1);

						String indexUrl = "";

						jsonMap.put("success", loginStatus);
						jsonMap.put("error", errorMsg);
						jsonMap.put("indexUrl", indexUrl);
						
						
					} else {

						int errorCode = 2;
						String strErrorDesc = gdObjectServiceImpl.getMessageTextWithLanguageCd(request,
								"TZ_SITE_MESSAGE", "43", strLang, "您输入的用户名密码有误。",
								"The account or password you entered is incorrect.");
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

		String orgid = tzWebsiteLoginServiceImpl.getLoginedUserOrgid(request);

		tzWebsiteLoginServiceImpl.doLogout(request, response);

		// String ctx = request.getContextPath();

		orgid = orgid.toLowerCase();

		String redirect = "redirect:" + "/user/login/" + orgid;

		return redirect;
	}

}
