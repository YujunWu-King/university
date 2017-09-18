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

import com.tranzvision.gd.TZAuthBundle.service.impl.TzCallCenterLoginServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.encrypt.DESUtil;
import com.tranzvision.gd.util.security.TzFilterIllegalCharacter;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 电话盒子登录
 * 
 * @author yuds
 * @since 2017-06-21
 */
@Controller
@RequestMapping(value = { "/callcenter/login" })
public class TzCallCenterLoginController {

	@Autowired
	private TzCallCenterLoginServiceImpl tzCallCenterLoginServiceImpl;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TzFilterIllegalCharacter tzFilterIllegalCharacter;

	@Autowired
	private TZGDObject tzGDObject;

	
	@RequestMapping(value = { "/{orgid}" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String userLoginWebsiteByQHMBA(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "orgid") String orgid) {

		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			orgid = tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(orgid).toUpperCase();

			String userId = request.getParameter("id");
			String userPwd = request.getParameter("pwd");
			
			ArrayList<String> aryErrorMsg = new ArrayList<String>();
			
			tzCallCenterLoginServiceImpl.doLogin(request, response, orgid, userId, userPwd, "", aryErrorMsg);
			String loginStatus = aryErrorMsg.get(0);
			String errorMsg = aryErrorMsg.get(1);
			if("success".equals(loginStatus)){
				jsonMap.put("code", "0");
				
				String strName = sqlQuery.queryForObject("SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_DLZH_ID=?", new Object[]{userId}, "String");
				jsonMap.put("name", strName);
				
				String strOprid = sqlQuery.queryForObject("SELECT OPRID FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_DLZH_ID=?", new Object[]{userId}, "String");
				jsonMap.put("oprid", strOprid);
				
				String strKey = DESUtil.encrypt(userId, "TZGDSSOFLG");
				
				jsonMap.put("desc", strKey);
				
			}else{
				jsonMap.put("code", "1");
				jsonMap.put("desc", errorMsg);
			}					
			
		} catch (Exception e) {
			jsonMap.put("code", "1");
			jsonMap.put("desc", e.toString());			
		}

		return jacksonUtil.Map2json(jsonMap);

	}
}
