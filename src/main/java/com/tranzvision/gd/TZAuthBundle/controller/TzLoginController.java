package com.tranzvision.gd.TZAuthBundle.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.security.TzFilterIllegalCharacter;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 登录前端控制器
 * 
 * @author SHIHUA
 * @since 2015-11-03
 */
@Controller
@RequestMapping(value = { "/login" })
public class TzLoginController {

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TzFilterIllegalCharacter tzFilterIllegalCharacter;

	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	@Autowired
	private TZGDObject tzGDObject;

	private String adminOrgId;

	private String adminOrgName = "创景云招生系统";

	private String adminOrgBjImg = "/statics/images/login/loginDefaultBg.jpg";

	@RequestMapping(value = { "", "/" })
	public ModelAndView userLogin(HttpServletRequest request, HttpServletResponse response) {

		adminOrgId = getSysHardCodeVal.getPlatformOrgID();

		try {
			/*
			 * String sql =
			 * "select A.TZ_JG_LOGIN_INFO,A.TZ_JG_LOGIN_COPR,A.TZ_ATTACHSYSFILENA,ifnull(B.TZ_ATT_A_URL,'') TZ_ATT_A_URL from PS_TZ_JG_BASE_T A left join PS_TZ_JG_LOGINBJ_T B on (A.TZ_ATTACHSYSFILENA=B.TZ_ATTACHSYSFILENA) where TZ_JG_EFF_STA='Y' and TZ_JG_ID=?"
			 * ;
			 */
			String sql = tzGDObject.getSQLText("SQL.TZAuthBundle.TzGetOrgInfo");

			Map<String, Object> mapAdmin = sqlQuery.queryForMap(sql, new Object[] { adminOrgId });

			String TZ_JG_LOGIN_INFO = adminOrgName;
			String TZ_JG_LOGIN_COPR = "";
			String orgLoginBjImgUrl = adminOrgBjImg;

			if (null != mapAdmin) {
				TZ_JG_LOGIN_INFO = String.valueOf(mapAdmin.get("TZ_JG_LOGIN_INFO"));
				TZ_JG_LOGIN_COPR = String.valueOf(mapAdmin.get("TZ_JG_LOGIN_COPR"));

				String TZ_ATT_A_URL = String.valueOf(mapAdmin.get("TZ_ATT_A_URL"));
				Object TZ_ATTACHSYSFILENA = mapAdmin.get("TZ_ATTACHSYSFILENA");
				String tzATTACHSYSFILENA = "";
				if (TZ_ATTACHSYSFILENA == null) {
					tzATTACHSYSFILENA = "";
				} else {
					tzATTACHSYSFILENA = String.valueOf(TZ_ATTACHSYSFILENA);
				}

				if (!"".equals(TZ_ATT_A_URL) && !"".equals(tzATTACHSYSFILENA)) {
					orgLoginBjImgUrl = TZ_ATT_A_URL + tzATTACHSYSFILENA;
				}
			}

			ModelAndView mv = new ModelAndView("login/managerLogin");
			mv.addObject("TZ_JG_LOGIN_INFO", TZ_JG_LOGIN_INFO);
			mv.addObject("TZ_JG_LOGIN_COPR", TZ_JG_LOGIN_COPR);
			mv.addObject("orgLoginBjImgUrl", orgLoginBjImgUrl);
			mv.addObject("locationOrgId", "");
			return mv;

		} catch (TzSystemException e) {
			e.printStackTrace();
		}

		ModelAndView mv = new ModelAndView("login/managerLogin");
		return mv;
	}

	@RequestMapping(value = { "/{orgid}" })
	public ModelAndView userLoginOrg(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "orgid") String orgid) {

		orgid = tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(orgid).toUpperCase();

		try {
			String sql = tzGDObject.getSQLText("SQL.TZAuthBundle.TzGetOrgInfo");

			Map<String, Object> mapOrg = sqlQuery.queryForMap(sql, new Object[] { orgid });

			String locationOrgId = orgid;
			String TZ_JG_LOGIN_INFO = adminOrgName;
			String TZ_JG_LOGIN_COPR = "";
			String orgLoginBjImgUrl = adminOrgBjImg;

			if (null != mapOrg) {
				TZ_JG_LOGIN_INFO = String.valueOf(mapOrg.get("TZ_JG_LOGIN_INFO"));
				TZ_JG_LOGIN_COPR = String.valueOf(mapOrg.get("TZ_JG_LOGIN_COPR"));

				String TZ_ATT_A_URL = String.valueOf(mapOrg.get("TZ_ATT_A_URL"));
				Object TZ_ATTACHSYSFILENA = mapOrg.get("TZ_ATTACHSYSFILENA");
				String tzATTACHSYSFILENA = "";
				if (TZ_ATTACHSYSFILENA == null) {
					tzATTACHSYSFILENA = "";
				} else {
					tzATTACHSYSFILENA = String.valueOf(TZ_ATTACHSYSFILENA);
				}

				if (!"".equals(TZ_ATT_A_URL) && !"".equals(tzATTACHSYSFILENA)) {
					orgLoginBjImgUrl = TZ_ATT_A_URL + tzATTACHSYSFILENA;
				}
			}

			ModelAndView mv = new ModelAndView("login/managerLogin");
			mv.addObject("TZ_JG_LOGIN_INFO", TZ_JG_LOGIN_INFO);
			mv.addObject("TZ_JG_LOGIN_COPR", TZ_JG_LOGIN_COPR);
			mv.addObject("orgLoginBjImgUrl", orgLoginBjImgUrl);
			mv.addObject("locationOrgId", locationOrgId);
			return mv;

		} catch (TzSystemException e) {
			e.printStackTrace();
		}

		ModelAndView mv = new ModelAndView("login/managerLogin");
		return mv;

	}

	/**
	 * 获取有效的机构记录
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "getorgdata", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getOrgData() {

		Map<String, Object> mapRet = new HashMap<String, Object>();

		String sql = "select TZ_JG_ID,TZ_JG_NAME from PS_TZ_JG_BASE_T where TZ_JG_EFF_STA='Y'";

		List<?> listOrgs = sqlQuery.queryForList(sql);

		ArrayList<Map<String, Object>> listOrgJson = new ArrayList<Map<String, Object>>();

		for (Object objOrg : listOrgs) {

			Map<String, Object> mapOrg = (Map<String, Object>) objOrg;

			Map<String, Object> mapOrgJson = new HashMap<String, Object>();

			mapOrgJson.put("orgValue", mapOrg.get("TZ_JG_ID"));
			mapOrgJson.put("orgDesc", mapOrg.get("TZ_JG_NAME"));

			listOrgJson.add(mapOrgJson);

		}

		mapRet.put("org", listOrgJson);

		JacksonUtil jacksonUtil = new JacksonUtil();
		return jacksonUtil.Map2json(mapRet);
	}

	@RequestMapping(value = "checkorgstatus", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String checkOrgStatus(@RequestParam(value = "orgid", required = true) String orgid) {

		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("success", "false");
		mapRet.put("orgId", "");

		String sql = "select 'Y' from PS_TZ_JG_BASE_T where TZ_JG_EFF_STA='Y' and TZ_JG_ID=?";

		String rst = sqlQuery.queryForObject(sql, new Object[] { orgid }, "String");

		if ("Y".equals(rst)) {
			mapRet.replace("success", "true");
			mapRet.replace("orgId", orgid);
		}

		JacksonUtil jacksonUtil = new JacksonUtil();
		return jacksonUtil.Map2json(mapRet);
	}

	@RequestMapping(value = "dologin", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String doLogin(HttpServletRequest request, HttpServletResponse response) {

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(request.getParameter("tzParams"));
		Map<String, Object> formData = jacksonUtil.getMap("comParams");

		String orgid = (String) formData.get("orgId");
		String userName = formData.get("userName")==null?"":String.valueOf(formData.get("userName")).trim();
		String userPwd = (String) formData.get("password");
		String code = (String) formData.get("yzm");

		ArrayList<String> aryErrorMsg = new ArrayList<String>();

		tzLoginServiceImpl.doLogin(request, response, orgid, userName, userPwd, code, aryErrorMsg);
		String loginStatus = aryErrorMsg.get(0);
		String errorMsg = aryErrorMsg.get(1);

		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("success", loginStatus);
		jsonMap.put("error", errorMsg);
		jsonMap.put("indexUrl", "/index");

		// {"success":"%bind(:1)","error":"%bind(:2)","indexUrl":"%bind(:3)"}

		return jacksonUtil.Map2json(jsonMap);
	}

	@RequestMapping(value = "logout")
	public String doLogout(HttpServletRequest request, HttpServletResponse response) {

		String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);

		tzLoginServiceImpl.doLogout(request, response);

		// String ctx = request.getContextPath();

		adminOrgId = getSysHardCodeVal.getPlatformOrgID();

		if (orgid.equals(adminOrgId)) {
			orgid = "";
		} else {
			orgid = orgid.toLowerCase();
		}

		String redirect = "redirect:" + "/login/" + orgid;

		return redirect;
	}

}
