package com.tranzvision.gd.TZEvaluationSystemBundle.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.captcha.Patchca;
import com.tranzvision.gd.util.cookie.TzCookie;
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
@RequestMapping(value = { "/evaluation" },method={RequestMethod.POST,RequestMethod.GET})
@CrossOrigin(origins="*")


public class EvaluationSystemController {
	@Autowired
	private TZGDObject tzGdObject;
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private TzFilterIllegalCharacter tzFilterIllegalCharacter;
	@Autowired
	private TzCookie tzCookie;
	
	private final String cookieOrgId = "tzmo";
	
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
			String zhid = tzLoginServiceImpl.getLoginedManagerDlzhid(request);
			String userName = sqlQuery.queryForObject("SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=?", new Object[]{oprid}, "String");
			String timeOut = "false";
			
			// 判断下用户有没有登录;
			if (oprid == null || "".equals(oprid) || orgid == null || "".equals(orgid) || zhid == null
					|| "".equals(zhid)) {
				timeOut = "true";
				
				if(orgid==null||"".equals(orgid)){
					orgid = tzCookie.getStringCookieVal(request, cookieOrgId);
				}
			}
						
			String contactUrl = sqlQuery.queryForObject("SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?", new Object[]{"TZ_EVALUATION_CONTACT_URL"}, "String");
			String evaluationDescriptionUrl = sqlQuery.queryForObject("SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?", new Object[]{"TZ_EVALUATION_M_DESCRIPTION_URL"}, "String");
			indexHtml = tzGdObject.getHTMLText("HTML.TZEvaluationSystemBundle.TZ_METERIAL_EVALUATION_INDEX",request.getContextPath(),orgid,timeOut,userName,contactUrl==null?"javascript:void(0)":contactUrl,evaluationDescriptionUrl==null?"javascript:void(0)":evaluationDescriptionUrl);
			
		} catch (TzSystemException e) {
			e.printStackTrace();
			indexHtml = e.toString();
		}
		return indexHtml;
	}
	
	

	/**
	 * 
	* Description:改造--校验机构有效性
	* Create Time: 2019年11月19日 下午3:06:54
	* @author yujun.wu
	* @param orgid	请求路径中传回的机构id
	* @param errorMsg
	* @return
	 */
	public void checkOrgidIsValid(String orgid,String[] errorMsg) {
		try {
			orgid = tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(orgid).toUpperCase();

			String sql = "select 'Y' from PS_TZ_JG_BASE_T where TZ_JG_EFF_STA='Y' AND upper(TZ_JG_ID)=?";

			String orgExist = sqlQuery.queryForObject(sql,new Object[]{orgid},"String");
			
			if("Y".equals(orgExist)){
				errorMsg[0]="0";
			}else{
				errorMsg[0]="1";
				errorMsg[1]="无效的机构："+orgid+"，请确认您输入的浏览器地址是否正确！";
			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0]="1";
			errorMsg[1]="校验机构有消息出错！错误描述："+e.getMessage();
		}
	}
	
	
	/**
	 * 
	* Description:从高金迁移过来--面试评审登录页
	* Create Time: 2019年11月19日 上午10:11:56
	* @author yujun.wu
	* @param request
	* @param response
	* @return
	 */
	@RequestMapping(value = { "/interview/{orgid}/index" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String interviewEvaluationIndex(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("orgid") String orgid) {
		/**
		 * 校验机构id
		 */
		String[] errorMsg = new String[2];
		this.checkOrgidIsValid(orgid, errorMsg);
		if("1".equals(errorMsg[0])) {
			return errorMsg[1];
		}
		String indexHtml = "";
		try {
			String sql = "select TZ_JG_NAME from PS_TZ_JG_BASE_T where TZ_JG_EFF_STA='Y' AND upper(TZ_JG_ID)=?";

			String TZ_JG_NAME = sqlQuery.queryForObject(sql,new Object[]{orgid},"String");
			String title = TZ_JG_NAME+"面试系统";
			indexHtml = tzGdObject.getHTMLText("HTML.TZEvaluationSystemBundle.TZ_VUE_INTERVIEW_LOGIN",true,title,orgid.toUpperCase());
			
		} catch (Exception e) {
			e.printStackTrace();
			indexHtml = e.toString();
		}
		return indexHtml;
	}
	
	@RequestMapping(value = { "/login" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String doLogin(HttpServletRequest request, HttpServletResponse response) {

		JacksonUtil jacksonUtil = new JacksonUtil();

		String orgId = request.getParameter("orgId");
		String userName = request.getParameter("userName");
		String userPwd = request.getParameter("password");
		String code = new Patchca().getToken(request);
		String type = request.getParameter("type");
		String judgeType = "interview".equals(type) ? "1" : "2";

		ArrayList<String> aryErrorMsg = new ArrayList<String>();

		// 验证评委帐号有效性
		String sqlAccount = "select 'Y' from PS_TZ_AQ_YHXX_TBL where upper(TZ_JG_ID)=? and TZ_DLZH_ID=? and TZ_JIHUO_ZT='Y' "
				+ "and (exists(select 1 from PS_TZ_JUSR_REL_TBL where OPRID = PS_TZ_AQ_YHXX_TBL.OPRID AND TZ_JUGTYP_ID=?)"
				+ "or exists(select 1 from tz_interview_admin_t where oprid = PS_TZ_AQ_YHXX_TBL.OPRID and type='QD' and status='A'))";
		String accountExist = sqlQuery.queryForObject(sqlAccount, new Object[] { orgId, userName, judgeType },
				"String");

		String loginStatus, errorMsg;

		if ("Y".equals(accountExist)) {
			tzLoginServiceImpl.doLogin(request, response, orgId, userName, userPwd, code, aryErrorMsg);
			loginStatus = aryErrorMsg.get(0);
			errorMsg = aryErrorMsg.get(1);
		} else {
			loginStatus = "1";
			errorMsg = "帐号不存在或者无效，请重新输入！";
		}

		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("success", loginStatus);
		jsonMap.put("error", errorMsg);

		return jacksonUtil.Map2json(jsonMap);
	}

	@RequestMapping(value = { "/logout" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String doLogout(HttpServletRequest request, HttpServletResponse response) {

		String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);

		if (orgid == null || "".equals(orgid)) {
			orgid = tzCookie.getStringCookieVal(request, cookieOrgId);
		}

		tzLoginServiceImpl.doLogout(request, response);

		if (orgid != null) {
			orgid = orgid.toLowerCase();
		}

		return "{\"success\":true}";
	}
	@RequestMapping(value = {"/interview/{orgid}/static/js/{source}", "/interview/static/js/{source}", "/static/js/{source}" }, produces = "application/javascript;charset=utf-8")
	public void javascriptSource(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "source") String source) {
		forwardSource(request, response);
	}

	@RequestMapping(value = { "/interview/{orgid}/static/css/{source}","/interview/static/css/{source}", "/static/css/{source}" }, produces = "text/css;charset=utf-8")
	public void styleSource(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "source") String source) {
		forwardSource(request, response);
	}

	@RequestMapping(value = { "/interview/{orgid}/static/img/{source}","/interview/static/img/{source}","/interview/static/img/{source}", "/static/img/{source}", "/interview/favicon.ico" }, produces = "image/*;charset=utf-8")
	public void imageSource(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "source") String source) {
		forwardSource(request, response);
	}
	private void forwardSource(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.getRequestDispatcher("/statics/js" + request.getRequestURI()).forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
