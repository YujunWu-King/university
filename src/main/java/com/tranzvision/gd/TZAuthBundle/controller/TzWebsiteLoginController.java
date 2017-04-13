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
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteEnrollClsServiceImpl;
import com.tranzvision.gd.util.base.Global;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.cookie.TzCookie;
import com.tranzvision.gd.util.security.TzFilterIllegalCharacter;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
import com.tranzvision.gd.util.encrypt.DESUtil;
import com.tranzvision.gd.util.httpclient.CommonUtils;

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
	private SiteEnrollClsServiceImpl siteEnrollClsServiceImpl;

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

				String loginHtml = "";
				Boolean isMobile = CommonUtils.isMobile(request);
				if (isMobile) {
					loginHtml = tzWebsiteServiceImpl.getMLoginPublishCode(request, orgid, siteid);
					strRet = loginHtml;
				} else {
					loginHtml = tzWebsiteServiceImpl.getLoginPublishCode(request, orgid, siteid);
					strRet = loginHtml;
				}

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

	@RequestMapping(value = { "/{orgid}" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String userLoginWebsiteByQHMBA(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "orgid") String orgid) {

		String strRet = "";

		try {
			orgid = tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(orgid).toUpperCase();
			String siteid="72";
			siteid = tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(siteid);

			if (null != orgid && !"".equals(orgid) && null != siteid && !"".equals(siteid)) {

				String loginHtml = "";
				Boolean isMobile = CommonUtils.isMobile(request);
				if (isMobile) {
					loginHtml = tzWebsiteServiceImpl.getMLoginPublishCode(request, orgid, siteid);
					strRet = loginHtml;
				} else {
					loginHtml = tzWebsiteServiceImpl.getLoginPublishCode(request, orgid, siteid);
					strRet = loginHtml;
				}

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
		String strSiteId = mapData.get("siteid") == null ? "" : String.valueOf(mapData.get("siteid"));
		String isMobile = mapData.get("isMobile") == null ? "" : String.valueOf(mapData.get("isMobile"));

		String classIdParams = request.getParameter("classIdParams");
		System.out.println("classIdParams:" + classIdParams);

		Map<String, Object> jsonMap = new HashMap<String, Object>();

		try {

			if (null != strOrgId && !"".equals(strOrgId)) {
				strOrgId = strOrgId.toUpperCase();
				String sql = "";
				// String sql =
				// tzGDObject.getSQLText("SQL.TZAuthBundle.TzGetSiteidByOrgid");

				// String strSiteId = sqlQuery.queryForObject(sql, new Object[]
				// { strOrgId }, "String");

				if (!"".equals(strUserName)) {
					strUserName = strUserName.toLowerCase();
				}

				if (null != strSiteId && !"".equals(strSiteId)) {
					sql = tzGDObject.getSQLText("SQL.TZAuthBundle.TzGetZcyhDlzhId");
					strUserName = sqlQuery.queryForObject(sql,
							new Object[] { strUserName, strUserName, strUserName, strOrgId }, "String");
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
							// 如果信息未完善，则跳转到待完善页面
							String indexUrl = "";
							boolean infoIsCmpl = tzWebsiteLoginServiceImpl.getLoginIndex(strUserName, strOrgId);
							if (infoIsCmpl) {

								// 跳转会原来的页面
								if (classIdParams != null && !classIdParams.equals("")) {
									// 以___分割
									String[] cparams = Global.split(classIdParams, "___");
									if (cparams != null && cparams.length >= 1) {
										String type = cparams[0];

										// 活动新闻浏览
										if (type.equals("art_view")) {
											if (cparams.length == 3) {
												indexUrl = ctxPath
														+ "/dispatcher?classid=art_view&operatetype=HTML&siteId="
														+ strSiteId + "&columnId=" + cparams[1] + "&artId="
														+ cparams[2];
											} else {
												if ("Y".equals(isMobile)) {
													indexUrl = ctxPath + "/dispatcher?classid=mIndex&siteId="
															+ strSiteId;
												} else {
													indexUrl = ctxPath + "/site/index/" + strOrgId.toLowerCase() + "/"
															+ strSiteId;
												}
											}
										}
									} else {
										if ("Y".equals(isMobile)) {
											indexUrl = ctxPath + "/dispatcher?classid=mIndex&siteId=" + strSiteId;
										} else {
											indexUrl = ctxPath + "/site/index/" + strOrgId.toLowerCase() + "/"
													+ strSiteId;
										}
									}

								} else {
									// 如果为手机，则跳转到手机页面-待完成
									if ("Y".equals(isMobile)) {
										indexUrl = ctxPath + "/dispatcher?classid=mIndex&siteId=" + strSiteId;
									} else {
										indexUrl = ctxPath + "/site/index/" + strOrgId.toLowerCase() + "/" + strSiteId;
									}
								}
							} else {
								String strParams = "";
								if ("Y".equals(isMobile)) {
									strParams = "{\"siteid\":\"" + strSiteId + "\",\"sen\":\"8\",\"isMobile\":\"Y\"}";
								} else {
									strParams = "{\"siteid\":\"" + strSiteId + "\",\"sen\":\"8\",\"isMobile\":\"N\"}";
								}
								String completeInfoUrl = siteEnrollClsServiceImpl.getCompleteUrl(strParams);

								jacksonUtil.json2Map(completeInfoUrl);
								indexUrl = jacksonUtil.getString("url");
								String encryUserName = DESUtil.encrypt(strUserName, "TZ_GD_TRANZVISION");
								indexUrl = indexUrl + "?userName=" + encryUserName;
							}

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
		// System.out.println("orgid:"+orgid);
		// System.out.println("siteid:"+siteid);

		tzWebsiteLoginServiceImpl.doLogout(request, response);

		// String ctx = request.getContextPath();

		orgid = orgid.toLowerCase();

		String redirect = "redirect:" + "/user/login/" + orgid + "/" + siteid;
		// System.out.println("redirect:"+redirect);

		return redirect;
	}

	@RequestMapping(value = "completeInfo/{orgid}/{siteid}", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String completeInfo(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "orgid") String orgid, @PathVariable(value = "siteid") String siteid) {
		String strRet = "";

		try {
			orgid = tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(orgid).toUpperCase();

			siteid = tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(siteid);

			if (null != siteid && !"".equals(siteid)) {

				String strParams = "{\"site\":\"" + siteid + "\",\"sen\":\"8\"}";
				String completeInfoUrl = siteEnrollClsServiceImpl.getEnrollUrl(strParams);
				strRet = "{\"url\":\"" + completeInfoUrl + "\"}";
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
}
