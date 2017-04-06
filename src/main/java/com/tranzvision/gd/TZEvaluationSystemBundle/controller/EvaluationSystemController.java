package com.tranzvision.gd.TZEvaluationSystemBundle.controller;

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

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.security.TzFilterIllegalCharacter;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 材料面试评审系统前端控制器
 * 
 * @author ShaweYet
 * @since 2017/02/22
 */
@Controller
@RequestMapping(value = { "/evaluation" })
public class EvaluationSystemController {
	@Autowired
	private TZGDObject tzGdObject;
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private TzFilterIllegalCharacter tzFilterIllegalCharacter;

	@RequestMapping(value = { "/material/{orgid}" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String materialEvaluationLogin(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "orgid") String orgid) {
		
		/*登录页面内容*/
		String loginHtml = "";
		try {
			orgid = tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(orgid).toUpperCase();

			String sql = "select 'Y' from PS_TZ_JG_BASE_T where TZ_JG_EFF_STA='Y' AND upper(TZ_JG_ID)=?";

			String orgExist = sqlQuery.queryForObject(sql,new Object[]{orgid},"String");
			
			if("Y".equals(orgExist)){
				loginHtml = tzGdObject.getHTMLText("HTML.TZEvaluationSystemBundle.TZ_METERIAL_EVALUATION_LOGIN",request.getContextPath(),orgid);
			}else{
				loginHtml = "无效的机构："+orgid+"，请确认您输入的浏览器地址是否正确！";
			}

			
		} catch (TzSystemException e) {
			e.printStackTrace();
			loginHtml = "";
		}
		return loginHtml;
	}
	
	@RequestMapping(value = { "/material/index" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String materialEvaluationIndex(HttpServletRequest request, HttpServletResponse response) {

		String indexHtml = "";
		try {
			String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			String userName = sqlQuery.queryForObject("SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=?", new Object[]{oprid}, "String");
			
			indexHtml = tzGdObject.getHTMLText("HTML.TZEvaluationSystemBundle.TZ_METERIAL_EVALUATION_INDEX",request.getContextPath(),orgid,userName);

			if(null==request.getSession(false)){ 
				if(true==request.getSession(true).isNew()){
					
				}else{ 
					indexHtml = "redirect:" + "/evaluation/material/" + orgid;
				} 
			}
			
		} catch (TzSystemException e) {
			e.printStackTrace();
			indexHtml = "";
		}
		return indexHtml;
	}
	
	@RequestMapping(value = { "/interview/{orgid}" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String interviewEvaluationLogin(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "orgid") String orgid) {
		
		/*登录页面内容*/
		String loginHtml = "";
		try {
			orgid = tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(orgid).toUpperCase();
			
			loginHtml = tzGdObject.getHTMLText("HTML.TZEvaluationSystemBundle.TZ_INTERVIEW_EVALUATION_LOGIN",request.getContextPath(),orgid);
			
		} catch (TzSystemException e) {
			e.printStackTrace();
			loginHtml = "";
		}
		return loginHtml;
	}
	
	@RequestMapping(value = { "/login" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String doLogin(HttpServletRequest request, HttpServletResponse response) {
		
		JacksonUtil jacksonUtil = new JacksonUtil();

		String orgId = request.getParameter("orgId");
		String userName = request.getParameter("userName");
		String userPwd = request.getParameter("password");
		String code = request.getParameter("yzm");
		String type = request.getParameter("type");
		String judgeType = "inteview".equals(type)?"1":"2";
		
		ArrayList<String> aryErrorMsg = new ArrayList<String>();

		//验证评委帐号有效性		
		String sqlAccount = "select 'Y' from PS_TZ_YHZH_VW where upper(TZ_JG_ID)=? and TZ_DLZH_ID=? and exists(select 1 from PS_TZ_JUSR_REL_TBL where OPRID = PS_TZ_YHZH_VW.OPRID AND TZ_JUGTYP_ID=?)";
		String accountExist = sqlQuery.queryForObject(sqlAccount,new Object[]{orgId,userName,judgeType},"String");
		
		String loginStatus,errorMsg;
		
		if("Y".equals(accountExist)){
			tzLoginServiceImpl.doLogin(request, response, orgId, userName, userPwd, code, aryErrorMsg);
			loginStatus = aryErrorMsg.get(0);
			errorMsg = aryErrorMsg.get(1);
		}else{
			loginStatus = "1";
			errorMsg = "帐号不存在，请重新输入！";
		}
				
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("success", loginStatus);
		jsonMap.put("error", errorMsg);
		jsonMap.put("indexUrl", "/evaluation/"+type+"/index");

		return jacksonUtil.Map2json(jsonMap);
	}
	
	@RequestMapping(value = { "/logout" })
	public String doLogout(HttpServletRequest request, HttpServletResponse response) {
		
		String type = request.getParameter("type");
		type = "interview".equals(type)?type:"material";
		
		String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		
		tzLoginServiceImpl.doLogout(request, response);

		if(orgid!=null){
			orgid = orgid.toLowerCase();
		}

		String redirect = "redirect:" + "/evaluation/"+type+"/"+orgid;

		return redirect;
	}
}
