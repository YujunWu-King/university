package com.tranzvision.gd.TZBaseBundle.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzWebsiteLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdKjComServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdKjInitServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdObjectServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.OperateType;
import com.tranzvision.gd.util.cookie.TzCookie;
import com.tranzvision.gd.util.encrypt.DESUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/")
public class Index {
	/**
	 * Cookie存储的系统语言信息
	 */
	//private final static String cookieLang = "tzlang";
	/**
	 * Cookie存储的机构id
	 */
	private final static String cookieJgId = "tzmo";

	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private GdKjComServiceImpl gdKjComService;
	@Autowired
	private GdKjInitServiceImpl gdKjInitService;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private TzCookie tzCookie;
	@Autowired
	private GdObjectServiceImpl gdObjectServiceImpl;
	@Autowired
	private TzWebsiteLoginServiceImpl tzWebsiteLoginServiceImpl;
	
	Logger logger = Logger.getLogger(this.getClass());

	@RequestMapping(value = "index")
	public String index(HttpServletRequest request, HttpServletResponse response) {
		
		// 获取当前提交的主题编号;
		String tmpSubmitThemeID = request.getParameter("theme");
		// 获取当前提交语言环境代码;
		//java高端产品后台暂时都是中文;
		//String tmpSubmitLanguageCd = request.getParameter("language");
		String tmpSubmitLanguageCd = "ZHS";
		/* 如果没有指定language参数，则获取当前登录会话语言环境代码 */
		if (tmpSubmitLanguageCd == null || "".equals(tmpSubmitLanguageCd)) {
			tmpSubmitLanguageCd = gdKjComService.getLoginLanguage(request, response);
		}

		/* 先清空当前访问组件、当前访问页面全局变量; */
		gdKjComService.setCurrentAccessComponentPage(request, "", "");

		String tmpLanguageCd = "";

		// 判断下用户有没有登录;
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		String zhid = tzLoginServiceImpl.getLoginedManagerDlzhid(request);
		if (oprid != null && !"".equals(oprid) && orgid != null && !"".equals(orgid) && zhid != null
				&& !"".equals(zhid)) {
			// 记住当前登录用户的主题设置;
			gdKjComService.setUserGxhTheme(request, response, tmpSubmitThemeID);
			// 记住当前登录用户的语言环境设置;
			gdKjComService.setUserGxhLanguage(request, response, tmpSubmitLanguageCd);

			tmpLanguageCd = gdKjComService.getUserGxhLanguage(request, response);

			// 切换会话语言环境代码;
			gdKjComService.switchLanguageCd(request, response, tmpLanguageCd);

			String parth = request.getContextPath();
			request.setAttribute("tz_gdcp_interaction_url_20150612184830", parth + "/dispatcher");

			request.setAttribute("tz_gdcp_frmwrk_init_msgset_20150612184830",
					gdKjComService.getFrameworkDescriptionResources(request, response));

			request.setAttribute("tz_gdcp_theme_id_20150612184830",
					gdKjComService.getUserGxhTheme(request, response));

			request.setAttribute("tz_gdcp_language_cd_20150612184830",
					gdKjComService.getUserGxhLanguage(request, response));

			request.setAttribute("tz_gdcp_loginStyle_20150612184830", gdKjComService.getLogoStyle(request, response));

			return "index";
		} else {
			String tmpLoginURL = "";
			// 得到机构的cookie;
			String tmpOrgID = tzCookie.getStringCookieVal(request, cookieJgId);
			// 得到语言;
			//java高端产品后台暂时都是中文;
			//tmpLanguageCd = tzCookie.getStringCookieVal(request, cookieLang);
			tmpLanguageCd = "ZHS";
			
			if (tmpOrgID != null && !"".equals(tmpOrgID)) {
				// 查询机构是不是存在;
				String sql = "SELECT count(1) FROM PS_TZ_JG_BASE_T WHERE TZ_JG_EFF_STA='Y' AND LOWER(TZ_JG_ID)=LOWER(?)";
				int count = jdbcTemplate.queryForObject(sql, new Object[] { tmpOrgID }, "Integer");
				if (count > 0) {
					tmpLoginURL = request.getContextPath() + "/login/" + tmpOrgID.toLowerCase();
				} else {
					tmpLoginURL = request.getContextPath() + "/login";
				}
			} else {
				tmpLoginURL = request.getContextPath() + "/login";
			}
			if (tmpLanguageCd != null) {
				String langSQL = "SELECT COUNT(1) FROM PS_TZ_PT_ZHZXX_TBL WHERE UPPER(TZ_ZHZJH_ID)=UPPER(?) AND TZ_ZHZ_ID=? AND TZ_EFF_DATE<= curdate()";

				int languageCount = jdbcTemplate.queryForObject(langSQL, new Object[] { tmpLanguageCd, tmpLanguageCd },
						"Integer");
				if (languageCount == 0) {
					tmpLanguageCd = gdObjectServiceImpl.getBaseLanguageCd();
				}
			} //else {
				// tmpLanguageCd = gdObjectServiceImpl.getBaseLanguageCd();
			//}

			String tempDefaultPrefixCN = "当前会话已超时或者非法访问，重新登录请点击";
			String tempDefaultPrefixEN = "The current session is timeout or the current access is invalid.<br>Please click";
			String tempDefaultMiddleCN = "这里";
			String tempDefaultMiddleEN = "here";
			String tempDefaultPostfixCN = "。";
			String tempDefaultPostfixEN = "to relogin.";

			String tmpInvalidSessionPrefix = gdObjectServiceImpl.getMessageTextWithLanguageCd(request,
					"TZGD_FWINIT_MSGSET", "TZGD_FWINIT_00037", tmpLanguageCd, tempDefaultPrefixCN, tempDefaultPrefixEN);
			String tmpInvalidSessionMiddle = gdObjectServiceImpl.getMessageTextWithLanguageCd(request,
					"TZGD_FWINIT_MSGSET", "TZGD_FWINIT_00038", tmpLanguageCd, tempDefaultMiddleCN, tempDefaultMiddleEN);
			String tmpInvalidSessionPostfix = gdObjectServiceImpl.getMessageTextWithLanguageCd(request,
					"TZGD_FWINIT_MSGSET", "TZGD_FWINIT_00039", tmpLanguageCd, tempDefaultPostfixCN,
					tempDefaultPostfixEN);

			request.setAttribute("tmpInvalidSessionPrefix", tmpInvalidSessionPrefix);
			request.setAttribute("tmpLoginURL", tmpLoginURL);
			request.setAttribute("tmpInvalidSessionMiddle", tmpInvalidSessionMiddle);
			request.setAttribute("tmpInvalidSessionPostfix", tmpInvalidSessionPostfix);
			return "invalid";
		}

	}

	@RequestMapping(value = "dispatcher", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String dispatcher(HttpServletRequest request, HttpServletResponse response)
	{
		/*
		 * String[] errMsg = {"0",""}; String content =
		 * registeServiceImpl.handleEnrollPage("5");
		 * registeServiceImpl.saveEnrollpage(content, "5",errMsg);
		 * registeServiceImpl.releasEnrollpage(content, "5",errMsg); return
		 * errMsg[0] + "====>" + errMsg[1];
		 */
		
		//logger.info("dispatcher---");
		JacksonUtil jacksonUtil = new JacksonUtil();
		// 组件配置的类引用ID;
		String tmpClassId = request.getParameter("classid");
		// 报文内容;
		String strComContent = "";
		// 错误描述;
		String strErrorDesc = "";
		// 错误码;
		String errorCode = "0";
		// 错误信息;
		String[] errMsgArr = { "0", "" };
		// 组件页面访问授权信息;
		String authorizedInfomation = "";
		// 统一参数;
		String strParams = "";
		// 返回值;
		String strRetContent = "";

		if (tmpClassId == null || "".equals(tmpClassId)) {
			strParams = request.getParameter("tzParams");
		} else {
			String tmpComId = "";
			String tmpPageId = "";
			String tmpComParams = "{}";
			// 根据应用编号查询组件id和页面id;
			String pageSql = "SELECT TZ_COM_ID, TZ_PAGE_ID FROM PS_TZ_AQ_PAGZC_TBL WHERE TZ_PAGE_REFCODE= ?";
			try {
				Map<String, Object> map = jdbcTemplate.queryForMap(pageSql, new Object[] { tmpClassId });
				if (map != null) {
					tmpComId = map.get("TZ_COM_ID") == null ? "" : String.valueOf(map.get("TZ_COM_ID"));
					tmpPageId = map.get("TZ_PAGE_ID") == null ? "" : String.valueOf(map.get("TZ_PAGE_ID"));
				}
			} catch (DataAccessException e) {

			}

			String tmpOperateType = request.getParameter("OperateType");
			if (tmpOperateType == null || "".equals(tmpOperateType)) {
				tmpOperateType = "HTML";
			} else {
				tmpOperateType = tmpOperateType.toUpperCase();
			}

			tmpComParams = request.getParameter("comParams");

			Map<String, Object> strParamsMap = new HashMap<>();
			strParamsMap.put("ComID", tmpComId);
			strParamsMap.put("PageID", tmpPageId);
			strParamsMap.put("OperateType", tmpOperateType);
			if (tmpComParams != null & !"".equals(tmpComParams)) {
				strParamsMap.put("comParams", tmpComParams);
			}

			strParams = jacksonUtil.Map2json(strParamsMap);
		}
		
		//logger.info("strParams:"+strParams);


		/*MBA报考服务系统手机版首页免登陆 卢艳添加，2017-4-15 begin*/
		//用于记录判断当前功能页是否手机版首页结果的变量
		Boolean mbaIndexM = false;
		String tmpSQLText = "";
		
		//会话是否有效
		Boolean bool = gdObjectServiceImpl.isSessionValid(request);
		Boolean isAnonymous = false;
		if(bool)
		{
			String tmpCurrentLoginUser = gdObjectServiceImpl.getOPRID(request);
			if("TZ_GUEST".equals(tmpCurrentLoginUser) == true)
			{
				isAnonymous = true;
			}
		}
		
		//用于控制是否将用户重定向到登录页的变量
		Boolean logoutFlag = false;
		
		//获取用于免登录的站点编号、组织机构编号和用户账号
		String tmpSiteId = "";
		String tmpOrgId = "";
		String tmpUserDlzh = "";
		
		String tmpPwdKey = "TZGD_@_!_*_20170420_Tranzvision";
		String tmpAutoLoginCookieValue = tzCookie.getStringCookieVal(request, "TZGD_AUTO_LOGIN");
		
		if(bool == false || isAnonymous == true)
		{
			tmpAutoLoginCookieValue = tmpAutoLoginCookieValue == null ? "" : DESUtil.decrypt(tmpAutoLoginCookieValue,tmpPwdKey);
			if(tmpAutoLoginCookieValue != null && "".equals(tmpAutoLoginCookieValue) == false)
			{
				String[] tmpLoginKeys = tmpAutoLoginCookieValue.split("\\|");
				String tmpElementValue = "";
				for(int i=0;i < tmpLoginKeys.length;i ++)
				{
					if(tmpLoginKeys[i].startsWith("A===") == true)
					{
						tmpOrgId = tmpLoginKeys[i].substring(4);
					}
					else if(tmpLoginKeys[i].startsWith("B===") == true)
					{
						tmpSiteId = tmpLoginKeys[i].substring(4);
					}
					else if(tmpLoginKeys[i].startsWith("C===") == true)
					{
						tmpUserDlzh = tmpLoginKeys[i].substring(4);
					}
				}
			}
			String tmpURLSiteId = request.getParameter("siteId");
			if(tmpSiteId == null || "".equals(tmpSiteId) == true)
			{
				tmpSiteId = tmpURLSiteId;
			}
			
			//判断是否在前端网站首页
			tmpSQLText = "SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?";
			String mobileUrlParams = jdbcTemplate.queryForObject(tmpSQLText, new Object[]{"TZ_MBA_BKXT_MURL_PARAMS"},"String");
			if(mobileUrlParams!=null && !"".equals(mobileUrlParams))
			{
				String classIdParam = "", siteIdParam = "", orgIdParam ="";
				
				String[] params = mobileUrlParams.split("&");
				for(int i=0;i<params.length;i++) {
					String[] paramsTmp = params[i].split("=");
					if("classid".equals(paramsTmp[0]))
					{
						classIdParam = paramsTmp[1];
					}
					if("siteId".equals(paramsTmp[0]))
					{
						siteIdParam = paramsTmp[1];
					}
					if("orgId".equals(paramsTmp[0]))
					{
						orgIdParam = paramsTmp[1];
					}
				}
				
				if(classIdParam != null && !"".equals(classIdParam)
						&& siteIdParam != null && !"".equals(siteIdParam))
				{
					if(classIdParam.equals(tmpClassId) && (siteIdParam.equals(tmpSiteId) || siteIdParam.equals(tmpURLSiteId)))
					{
						mbaIndexM = true;
					}
				}
			}
		}
		
		//如果当前功能页面为手机版首页且为匿名用户访问，则需要将用户重定向到登录页
		if(mbaIndexM == true && isAnonymous == true)
		{
			logoutFlag = true;
		}
		
		//判断是否需要根据免密cookie记录的信息进行自动登录
		if(logoutFlag == false)
		{
			//是否具备用于免登录使用的站点编号、机构编号和用户登录账号
			if(tmpSiteId != null && !"".equals(tmpSiteId))
			{
				if(tmpOrgId != null && !"".equals(tmpOrgId))
				{		
					if(!"".equals(tmpUserDlzh) && tmpUserDlzh != null)
					{
						ArrayList<String> aryErrorMsg = new ArrayList<String>();
						
						tmpSQLText = "SELECT OPERPSWD FROM PSOPRDEFN WHERE OPRID=(SELECT OPRID FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_DLZH_ID=?)";
						String passwordJm = jdbcTemplate.queryForObject(tmpSQLText, new Object[]{tmpUserDlzh},"String");
						String password = passwordJm == null ? "" : DESUtil.decrypt(passwordJm, "TZGD_Tranzvision");
						
						boolean boolResult = tzWebsiteLoginServiceImpl.doLogin(request, response, tmpOrgId, tmpSiteId,
								tmpUserDlzh, password, "", "ZHS", aryErrorMsg);
						
						if(boolResult == false)
						{
							if(mbaIndexM)
							{
								logoutFlag = true;
							}
						}
					}
					else
					{
						if(mbaIndexM)
						{
							logoutFlag = true;
						}
					}
				}
				else
				{
					if(mbaIndexM)
					{
						logoutFlag = true;
					}
				}
			}
		}
		
		//如果满足将用户重定向到登录的条件，则将用户重定向到登录页
		if(logoutFlag == true)
		{
			//rootPath;
			String ctxPath = request.getContextPath();
			
			//得到登录地址;
			String loginOutUrl = tzCookie.getStringCookieVal(request,"TZGD_LOGIN_URL");
			
			if(loginOutUrl == null || "".equals(loginOutUrl))
			{
				tmpSQLText = "SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?";
				String loginUrl = jdbcTemplate.queryForObject(tmpSQLText, new Object[]{"TZ_MBA_BKXT_MURL_LOGIN"},"String");												
				loginOutUrl = ctxPath + loginUrl;
			}
			
			try
			{
				response.sendRedirect(loginOutUrl);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		/*MBA报考服务系统手机版首页免登陆 卢艳添加，2017-4-15 end*/	
		

		// 操作类型;
		String strOprType = "";

		try {
			jacksonUtil.json2Map(strParams);

			strOprType = jacksonUtil.getString("OperateType");
			Map<String, Object> map = null;
			switch (OperateType.getOperateType(strOprType)) {
			// 框架资源;
			case KJZY:
				// 主题编号;
				String sThemeID = jacksonUtil.getString("themeID");
				// 获取框架资源信息;
				strComContent = gdKjInitService.getKJResources(request, response, sThemeID);
				break;
			// 主菜单;
			case ZCD:
				// 获取主菜单信息;
				// 获取主菜单信息;
				strComContent = gdKjInitService.getMenuList(request, response);
				break;
			// 菜单说明信息;
			case CDSM:
				// 菜单说明信息编号;
				String strDespID = jacksonUtil.getString("menuDescID");
				// 获取菜单说明信息内容;
				strComContent = gdKjInitService.getMenuDescription(request, response, strDespID);
				break;
			// 框架操作列表;
			case ZTYY:
				strComContent = gdKjInitService.getKJVersionInfo();
				break;
			// 标签控件;
			case BQ:
				// 标签ID;
				String sCID = jacksonUtil.getString("CID");
				// 默认内容名称;
				String sDefaultText = jacksonUtil.getString("DText");
				// 获取控件标签;
				strComContent = gdKjComService.getFieldTag(request, response, sCID, sDefaultText, errMsgArr);
				errorCode = errMsgArr[0];
				strErrorDesc = errMsgArr[1];
				break;
			// 组件资源;
			case ZJZY:
				// 组件ID;
				String sComID = jacksonUtil.getString("ComID");
				// 获取组件资源信息;
				strComContent = gdKjComService.getComResourses(request, response, sComID);
				break;
			// TransValue值;
			case TV:
				// 字段名称;
				String sFieldName = jacksonUtil.getString("fieldName");
				// 获取该字段下的TransValue值;
				strComContent = gdKjComService.getTransValue(request, response, sFieldName);
				break;
			// 下拉框值;
			case COMBOX:
				// 搜索表或视图名;
				String recname = jacksonUtil.getString("recname");
				// 搜索结果字段;
				String result = jacksonUtil.getString("result");
				// 搜索条件;
				// String condition = jacksonUtil.getString("condition");
				map = jacksonUtil.getMap("condition");
				String condition = jacksonUtil.Map2json(map).toString();

				// 根据搜索条件获取搜索表中的数据，供下拉框使用;
				strComContent = gdKjComService.getComboxValue(recname, condition, result, errMsgArr);
				break;
			// PROMPT搜索;
			case PROMPT:
				// 搜索表或视图名;
				String precname = jacksonUtil.getString("recname");
				// 搜索条件;
				// String pcondition = jacksonUtil.getString("condition");
				map = jacksonUtil.getMap("condition");
				String pcondition = jacksonUtil.Map2json(map).toString();
				// 搜索结果字段;
				String presult = jacksonUtil.getString("result");
				// 搜索最大记录数;
				String maxRow = jacksonUtil.getString("maxRow");

				int numLimit = 0, numStart = 0;
				String limit = request.getParameter("limit");
				String strStart = request.getParameter("start");

				if (limit != null && !"".equals(limit) && StringUtils.isNumeric(limit)) {
					numLimit = Integer.parseInt(limit);
				}

				if (strStart != null && !"".equals(strStart) && StringUtils.isNumeric(strStart)) {
					numStart = Integer.parseInt(strStart);
				}

				// 根据搜索条件获取搜索表中的数据，供下拉框使用;
				strComContent = gdKjComService.getPromptSearchList(precname, pcondition, presult, maxRow, numLimit,
						numStart, errMsgArr);
				break;
			// 获取hardcode值;
			case HARDCODE:
				// hardcode名称;
				String hardcodeName = jacksonUtil.getString("hardcodeName");
				// hardcode值;
				String hardcodeValue = gdKjInitService.getHardCodeValue(hardcodeName);
				strComContent = "'" + hardcodeValue + "'";
				break;
			default:
				// 组件ID;
				String comID = jacksonUtil.getString("ComID");
				// 页面ID;
				String sPageID = jacksonUtil.getString("PageID");

				// 通用参数;
				String sCommParams = "{}";
				if (jacksonUtil.containsKey("comParams")) {
					try {
						map = jacksonUtil.getMap("comParams");

						sCommParams = jacksonUtil.Map2json(map).toString();
						if ((sCommParams == null || "null".equals(sCommParams))) {
							sCommParams = "{}";
						}
					} catch (Exception e) {
						sCommParams = "{}";
					}
				}
				//logger.info("sCommParams:"+sCommParams);
				gdKjComService.setCurrentAccessComponentPage(request, comID, sPageID);
				strComContent = gdKjComService.userRequestDispatcher(request, response, comID, sPageID, strOprType,
						sCommParams, errMsgArr);
				errorCode = errMsgArr[0];
				strErrorDesc = errMsgArr[1];

				String tmpUserID = gdKjComService.getOPRID(request);
				authorizedInfomation = gdKjComService.getComAuthorizedInfo(tmpUserID, comID);

				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
			// 错误描述;
			strErrorDesc = e.toString();
			// 错误码;
			errorCode = "1";
		}
		if ("HTML".equals(strOprType)) {
			if ("0".equals(errorCode)) {
				strRetContent = strComContent;
			} else {
				if (gdKjComService.isSessionValid(request)) {
					strRetContent = strErrorDesc;
				} else {
					strRetContent = gdObjectServiceImpl.getTimeoutHTML(request,
							"HTML.TZBaseBundle.TZGD_SQR_HTML_TIMEOUT");
					if (strRetContent == null || "".equals(strRetContent)) {
						strRetContent = strErrorDesc;
					}
				}
			}
		} else {
			if (strComContent == null || "".equals(strComContent)) {
				strComContent = "{}";
			}
			String tmpTimeOutFlag = "false";
			if (!"0".equals(errorCode)) {
				if (gdKjComService.isSessionValid(request) == false) {
					tmpTimeOutFlag = "true";
				}
			}			
			strRetContent = "{\"comContent\": " + strComContent + ",\"state\":{\"errcode\":" + errorCode
					+ ",\"errdesc\": \"" + strErrorDesc + "\",\"timeout\": " + tmpTimeOutFlag
					+ ",\"authorizedInfo\": {" + authorizedInfomation + "}}}";
		}
		
		//logger.info("strRetContent:"+strRetContent);
		return strRetContent;
	}

}
