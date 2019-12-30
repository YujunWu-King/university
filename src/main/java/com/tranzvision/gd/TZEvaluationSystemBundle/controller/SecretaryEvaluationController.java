package com.tranzvision.gd.TZEvaluationSystemBundle.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tranzvision.gd.TZBaseBundle.service.impl.LogSaveServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.cookie.TzCookie;
import com.tranzvision.gd.util.security.TzFilterIllegalCharacter;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 面试秘书评审系统前端控制器
 *
 * @author ShaweYet
 * @since 2017/02/22
 */
@Controller
@RequestMapping(value = { "/evaluation" })
public class SecretaryEvaluationController {
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
	@Autowired
	private LogSaveServiceImpl logSaveServiceImpl;

	private final String cookieOrgId = "tzmo";


	@RequestMapping(value = { "/secretary/{orgid}" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String interviewEvaluationLogin(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "orgid") String orgid) {

		/*登录页面内容*/
		String loginHtml = "";
		try {
			orgid = tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(orgid).toUpperCase();

			String sql = "select 'Y' from PS_TZ_JG_BASE_T where TZ_JG_EFF_STA='Y' AND upper(TZ_JG_ID)=?";

			String orgExist = sqlQuery.queryForObject(sql,new Object[]{orgid},"String");

			if("Y".equals(orgExist)){
				loginHtml = tzGdObject.getHTMLText("HTML.TZEvaluationSystemBundle.TZ_SECRETARY_EVALUATION_LOGIN",request.getContextPath(),orgid);
			}else{
				loginHtml = "无效的机构："+orgid+"，请确认您输入的浏览器地址是否正确！";
			}


		} catch (TzSystemException e) {
			e.printStackTrace();
			loginHtml = "";
		}
		return loginHtml;
	}


	@RequestMapping(value = { "/secretary/index" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String interviewEvaluationTouchIndex(HttpServletRequest request, HttpServletResponse response) {

		String indexHtml = "";
		try {
			String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			System.out.println("oprid:" + oprid);
			String userName = sqlQuery.queryForObject("SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=?", new Object[]{oprid}, "String");
			String contactUrl = sqlQuery.queryForObject("SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?", new Object[]{"TZ_EVALUATION_CONTACT_URL"}, "String");

			String page = request.getParameter("page");

			System.out.println("page:" + page);
			if("batch".equals(page)||"evaluation".equals(page)){
				String classId = request.getParameter("classId");
				String batchId = request.getParameter("batchId");
				String appInsId = request.getParameter("appInsId");
				String msGroupId = request.getParameter("msGroupId");

				String appTplId = "",className = "",batchName="";

				Map<String,Object> map = sqlQuery.queryForMap("select A.TZ_CLASS_NAME,B.TZ_BATCH_NAME,A.TZ_PS_APP_MODAL_ID from PS_TZ_CLASS_INF_T A ,PS_TZ_CLS_BATCH_T B where A.TZ_CLASS_ID = B.TZ_CLASS_ID AND A.TZ_CLASS_ID=? AND B.TZ_BATCH_ID = ?",
						new Object[]{classId,batchId});
				if(map!=null){
					className = (String)map.get("TZ_CLASS_NAME");
					batchName = (String)map.get("TZ_BATCH_NAME");
					appTplId = (String)map.get("TZ_PS_APP_MODAL_ID");
				}

				//评委是否组长
				String pwzzFlag = sqlQuery.queryForObject("SELECT TZ_GROUP_LEADER FROM PS_TZ_MSPS_PW_TBL WHERE TZ_CLASS_ID=? AND TZ_APPLY_PC_ID=? AND TZ_PWEI_OPRID=?", new Object[]{classId,batchId,oprid},"String");
				if(pwzzFlag==null) {
					pwzzFlag = "";
				}

				//评委可评审的面试组
				String mszOption = "";
				if("batch".equals(page)) {
					List<Map<String,Object>> list_msgroup= new ArrayList<Map<String,Object>>();

					/*String sqlMsGroup= "SELECT DISTINCT A.TZ_PWEI_GRPID,B.TZ_GROUP_ID,B.TZ_GROUP_NAME";
					sqlMsGroup += " FROM PS_TZ_MSPS_KSH_TBL C,PS_TZ_MSPS_PW_TBL A,PS_TZ_INTEGROUP_T B";
					sqlMsGroup += " WHERE A.TZ_PWEI_GRPID=B.TZ_CLPS_GR_ID AND A.TZ_CLASS_ID=B.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID=B.TZ_APPLY_PC_ID";
					sqlMsGroup += " AND B.TZ_GROUP_ID=C.TZ_GROUP_ID AND A.TZ_CLASS_ID=B.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID=B.TZ_APPLY_PC_ID";
					sqlMsGroup += " AND A.TZ_CLASS_ID=? AND A.TZ_APPLY_PC_ID=? AND A.TZ_PWEI_OPRID=? AND A.TZ_PWEI_ZHZT<>'B'";
					sqlMsGroup += " ORDER BY B.TZ_GROUP_ID";*/

					String sqlMsGroup = "SELECT DISTINCT A.TZ_PWEI_GRPID, B.TZ_GROUP_ID, B.TZ_GROUP_NAME FROM PS_TZ_MSPS_KSH_TBL C, PS_TZ_MSPS_PW_TBL A, PS_TZ_INTEGROUP_T B, PS_TZ_MSPS_GR_TBL E, PS_TZ_AQ_YHXX_TBL F WHERE A.TZ_PWEI_GRPID = B.TZ_CLPS_GR_ID AND A.TZ_CLASS_ID = B.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID = B.TZ_APPLY_PC_ID AND B.TZ_GROUP_ID = C.TZ_GROUP_ID AND A.TZ_CLASS_ID = B.TZ_CLASS_ID AND A.TZ_APPLY_PC_ID = B.TZ_APPLY_PC_ID AND A.TZ_CLASS_ID =? AND A.TZ_APPLY_PC_ID =? AND A.TZ_PWEI_GRPID = E.TZ_CLPS_GR_ID AND E.TZ_ROLENAME = F.TZ_DLZH_ID AND F.OPRID = ? AND A.TZ_PWEI_ZHZT <> 'B' ORDER BY B.TZ_GROUP_ID;";

					List<Map<String, Object>> listMsGroup = sqlQuery.queryForList(sqlMsGroup, new Object[]{classId,batchId,oprid});
					for(Map<String, Object> mapMsGroup : listMsGroup) {
						String pwGroupId = mapMsGroup.get("TZ_PWEI_GRPID") == null ? "" : mapMsGroup.get("TZ_PWEI_GRPID").toString();
						String msGroupIdOne = mapMsGroup.get("TZ_GROUP_ID") == null ? "" : mapMsGroup.get("TZ_GROUP_ID").toString();
						String msGroupName = mapMsGroup.get("TZ_GROUP_NAME") == null ? "" : mapMsGroup.get("TZ_GROUP_NAME").toString();

						if(!"".equals(msGroupIdOne) && !"".equals(msGroupName)) {
							mszOption += "<option value='"+ msGroupIdOne + "'>"+ msGroupName + "</option>";
						}
					}
				}

				/* 当前考生的姓名和面试申请号 */
				/*
				String  mssqh = "", examineeName = "";
				Map<String, Object> mapKs = sqlQuery.queryForMap("SELECT B.TZ_REALNAME,B.TZ_MSH_ID FROM PS_TZ_FORM_WRK_T A,PS_TZ_AQ_YHXX_TBL B WHERE A.OPRID=B.OPRID AND A.TZ_APP_INS_ID=?", new Object[]{appInsId});
				if(mapKs!=null) {
					mssqh = (String) mapKs.get("TZ_MSH_ID");
					examineeName = (String) mapKs.get("TZ_REALNAME");
				}


				String ksIframeId = "bmb_iframe_" + classId + "_" + batchId + "_" + appInsId;
				*/

				indexHtml = "batch".equals(page)?
						tzGdObject.getHTMLText("HTML.TZEvaluationSystemBundle.TZ_SECRETARY_EVALUATION_TOUCH_BATCH",request.getContextPath(),orgid,userName,classId,batchId,className,batchName,contactUrl,pwzzFlag,mszOption)
						:tzGdObject.getHTMLText("HTML.TZEvaluationSystemBundle.TZ_INTERVIEW_EVALUATION_TOUCH_GRADE",request.getContextPath(),orgid,userName,classId,batchId,appInsId,className,batchName,contactUrl,appTplId,msGroupId,pwzzFlag);
			}else{
				indexHtml = tzGdObject.getHTMLText("HTML.TZEvaluationSystemBundle.TZ_SECRETARY_EVALUATION_TOUCH_INDEX",request.getContextPath(),orgid,userName,contactUrl);
			}

		} catch (TzSystemException e) {
			e.printStackTrace();
			indexHtml = e.toString();
		}
		return indexHtml;
	}

	@RequestMapping(value = { "/secretary/login" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String doLogin(HttpServletRequest request, HttpServletResponse response) {

		JacksonUtil jacksonUtil = new JacksonUtil();

		String orgId = request.getParameter("orgId");
		String userName = request.getParameter("userName");
		String userPwd = request.getParameter("password");
		String code = request.getParameter("yzm");
		String type = request.getParameter("type");
		String judgeType = "interview".equals(type)?"1":"2";
		String device = request.getParameter("device");

		ArrayList<String> aryErrorMsg = new ArrayList<String>();

		//验证评委帐号有效性
		String sqlAccount = "select 'Y' from PS_TZ_AQ_YHXX_TBL where upper(TZ_JG_ID)=? and TZ_DLZH_ID=? and TZ_JIHUO_ZT='Y' and exists(select 1 from PS_TZ_JUSR_REL_TBL where OPRID = PS_TZ_AQ_YHXX_TBL.OPRID AND TZ_JUGTYP_ID=?)";
		String accountExist = sqlQuery.queryForObject(sqlAccount,new Object[]{orgId,userName,judgeType},"String");

		String loginStatus,errorMsg;

		boolean isSuccess=false;

		if("Y".equals(accountExist)){
			isSuccess=tzLoginServiceImpl.doLogin(request, response, orgId, userName, userPwd, code, aryErrorMsg);
			loginStatus = aryErrorMsg.get(0);
			errorMsg = aryErrorMsg.get(1);
		}else{
			loginStatus = "1";
			errorMsg = "帐号不存在或者无效，请重新输入！";
		}

		//登录日志保存到数据库
		if(!"".equals(userName)&&null!=userName){
			String getOprid="select OPRID from PS_TZ_AQ_YHXX_TBL where TZ_DLZH_ID=?";
			String oprid=sqlQuery.queryForObject(getOprid,new Object[]{userName},"String");
			if(!"".equals(oprid)&&null!=oprid){
				System.out.println("loginOPRID=====>"+oprid);
				String inputParam="登录账号："+userName;
				String loginResult="";
				String failReason="";
				if(isSuccess){
					loginResult="登录成功！";
				}else {
					loginResult="登录失败！";
					failReason=errorMsg;
				}
				logSaveServiceImpl.SaveLogToDataBase(oprid,inputParam,"",loginResult,failReason,"","","");
			}
		}

		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("success", loginStatus);
		jsonMap.put("error", errorMsg);
		jsonMap.put("indexUrl", "/evaluation/secretary/index");

		return jacksonUtil.Map2json(jsonMap);
	}


	@RequestMapping(value = { "/secretary/logout" })
	public String doLogout(HttpServletRequest request, HttpServletResponse response) {

		String type = request.getParameter("type");
		type = "interview".equals(type)?type:"material";

		String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);

		if(orgid==null||"".equals(orgid)){
			orgid = tzCookie.getStringCookieVal(request, cookieOrgId);
		}

		tzLoginServiceImpl.doLogout(request, response);

		if(orgid!=null){
			orgid = orgid.toLowerCase();
		}

		String redirect = "redirect:" + "/secretary/"+type+"/"+orgid;

		return redirect;
	}
}
