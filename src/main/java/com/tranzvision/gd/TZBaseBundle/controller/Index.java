package com.tranzvision.gd.TZBaseBundle.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tranzvision.gd.TZBaseBundle.service.impl.LogSaveServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Strings;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzWebsiteLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdKjComServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdKjInitServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdObjectServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.OperateType;
import com.tranzvision.gd.util.cookie.TzCookie;
import com.tranzvision.gd.util.sql.SqlQuery;

@Controller
@RequestMapping(value = "/",method={RequestMethod.POST,RequestMethod.GET})
@CrossOrigin(origins="*")
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
	@Autowired
	private LogSaveServiceImpl logSaveServiceImpl;

	Logger logger = Logger.getLogger(this.getClass());

	@RequestMapping(value = {"index"})
	public String index(HttpServletRequest request, HttpServletResponse response) {
		String url = "";
        url = request.getScheme() +"://" + request.getServerName() + ":" +request.getServerPort()+ request.getServletPath();
       if(url.toLowerCase().indexOf("jsp")!=-1||url.indexOf("::")!=-1) {
    	   return "login/errorLogin";
       }
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

		//查询注册类型，如果是ZCYH类型，视为未登录,NBYH才允许
		String queryRylxSql = "select TZ_RYLX from PS_TZ_AQ_YHXX_TBL where OPRID = ? and TZ_JG_ID = ? and TZ_DLZH_ID=?";
		String TZ_RYLX = jdbcTemplate.queryForObject(queryRylxSql, new Object[] {oprid, orgid, zhid}, "String");

		if (oprid != null && !"".equals(oprid) && orgid != null && !"".equals(orgid) && zhid != null
				&& !"".equals(zhid) && "NBYH".equals(TZ_RYLX)) {
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

	private boolean checkOrgid(String orgid) {
		// 查询机构是不是存在;
		String sql = "SELECT count(1) FROM PS_TZ_JG_BASE_T WHERE TZ_JG_EFF_STA='Y' AND LOWER(TZ_JG_ID)=LOWER(?)";
		int count = jdbcTemplate.queryForObject(sql, new Object[] { orgid.toLowerCase() }, "Integer");
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	private boolean checkSite(String siteid) {
		// 查询机构是不是存在;
		String sql = "select count(1) from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=? AND TZ_SITEI_ENABLE='Y'";
		int count = jdbcTemplate.queryForObject(sql, new Object[] { siteid }, "Integer");
		if (count > 0) {
			return true;
		} else {
			return false;
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
		/** 校验dispatcher分发ajax请求Begin **/
		// 校验逻辑修改，1.如果是ajax请求，一定需要校验
		// 2. 参数里面 且strOprType为html的不校验
		// 4. 其他都需要校验
		boolean isCheck = false;
		String requestedWith = request.getHeader("x-requested-with");
		if (requestedWith != null && requestedWith.equalsIgnoreCase("XMLHttpRequest")) {
			isCheck = true;
		}
		/** 校验dispatcher分发ajax请求End **/

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
		//组件ID
		String comID="";
		//页面ID
		String sPageID="";

		if (tmpClassId == null || "".equals(tmpClassId)) {
			strParams = request.getParameter("tzParams");
		} else {
			// 组件配置的类引用ID的情况下 需要对orgid和siteid 进行校验
			Map<String, String[]> parameterMap = request.getParameterMap();
			String[] keyValue = null;
			for (String key : parameterMap.keySet()) {
				if (key.toLowerCase().equals("orgid") || key.toLowerCase().equals("jgid")) {
					keyValue = parameterMap.get(key);
					for (String str : keyValue) {
						System.out.println("str:" + str);
						if (!checkOrgid(str)) {

							// strRetContent = "{\"comContent\":
							// \"\",\"state\":{\"errcode\":1,\"errdesc\":
							// \"非法链接\",\"timeout\": "
							// + false + ",\"authorizedInfo\": {}}}";
							response.setStatus(404);
							return null;
						}
					}
				}
				if (key.toLowerCase().equals("siteid")) {
					keyValue = parameterMap.get(key);
					for (String str : keyValue) {
						System.out.println("str:" + str);
						try {
							Integer.parseInt(str);
							if (!checkSite(str)) {
								// strRetContent = "{\"comContent\":
								// \"\",\"state\":{\"errcode\":1,\"errdesc\":
								// \"非法链接\",\"timeout\": "
								// + false + ",\"authorizedInfo\": {}}}";
								// return strRetContent;
								response.setStatus(404);
								return null;
							}
						} catch (Exception e) {
							// strRetContent = "{\"comContent\":
							// \"\",\"state\":{\"errcode\":1,\"errdesc\":
							// \"非法链接\",\"timeout\": "
							// + false + ",\"authorizedInfo\": {}}}";
							// return strRetContent;

							response.setStatus(404);
							return null;
						}
					}
				}
			}
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
		tzWebsiteLoginServiceImpl.autoLoginByCookie(request, response);
		/*MBA报考服务系统手机版首页免登陆 卢艳添加，2017-4-15 end*/

		// 操作类型;
		String strOprType = "";

		try {
			jacksonUtil.json2Map(strParams);

			strOprType = jacksonUtil.getString("OperateType");
			if (!isCheck) {
				if (strOprType.toLowerCase().equals("html")) {
					isCheck = false;
				} else {
					isCheck = true;
				}
			}

			if (isCheck) {
				String verification = request.getParameter("verification");
				boolean errorFlag = false;
				if (verification != null) {
					errorFlag = GetVerificationController.VERIFICATION_LIST.contains(verification);
					if (errorFlag)
						GetVerificationController.VERIFICATION_LIST.remove(verification);
				}
				if (!errorFlag) {
					response.setStatus(404);
					return null;
				}
			}

			/* 校验ComID、PageID */
			String ComID = jacksonUtil.getString("ComID");
			String PageID = jacksonUtil.getString("PageID");
			// System.out.println("ComID==============" + ComID + ",PageID================"
			// + PageID);
			if (!Strings.isNullOrEmpty(ComID)) {
				if (!Strings.isNullOrEmpty(PageID)) {
					/* ComID和PageID都不空 */
					String ComPageExistSql = "SELECT 'Y' FROM PS_TZ_AQ_PAGZC_TBL WHERE TZ_COM_ID=? AND TZ_PAGE_ID=? LIMIT 0,1";
					String ComPageExist = jdbcTemplate.queryForObject(ComPageExistSql, new Object[] { ComID, PageID },
							"String");
					if (!"Y".equals(ComPageExist)) {
						response.setStatus(404);
						return null;
					}
				} else {
					String ComExistSql = "SELECT 'Y' FROM PS_TZ_AQ_COMZC_TBL WHERE TZ_COM_ID=? LIMIT 0,1";
					String ComExist = jdbcTemplate.queryForObject(ComExistSql, new Object[] { ComID }, "String");
					if (!"Y".equals(ComExist)) {
						response.setStatus(404);
						return null;
					}
				}

			}

			/* 校验ComID、PageID */

		} catch (Exception e) {
			response.setStatus(404);
			return null;
		}

		/* 防止参数携带js攻击 */
		if (strParams.toLowerCase().contains("<script")) {
			 strRetContent = "{\"comContent\":"+
			 "\"\",\"state\":{\"errcode\":1,\"errdesc\": \"非法输入参数\",\"timeout\":"+
			 "false"+
			 ",\"authorizedInfo\": {}}}";
			 return strRetContent;
			//response.setStatus(404);
			//return null;
		}
		// 检查所有的传入参数，是否有<script modity by caoy
		Map<String, String[]> parameterMap = request.getParameterMap();
		// 遍历map中的值
		for (String[] value : parameterMap.values()) {
			for (String str : value) {
				//System.out.println("str:" + str);
				if (str.toLowerCase().contains("<script")) {
					strRetContent = "{\"comContent\":"+
					 "\"\",\"state\":{\"errcode\":1,\"errdesc\": \"非法输入参数\",\"timeout\":"+
					 "false"+
					 ",\"authorizedInfo\": {}}}";
					return strRetContent;
					/*response.setStatus(404);
					return null;*/
				}
			}
		}
		/* 防止参数携带js攻击 */

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
				comID = jacksonUtil.getString("ComID");
				// 页面ID;
				sPageID = jacksonUtil.getString("PageID");

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
		String OPRID=tzLoginServiceImpl.getLoginedManagerOprid(request);
		System.out.println("OPRID=====>"+OPRID);
		if(!"".equals(OPRID)&&null!=OPRID){
			logSaveServiceImpl.SaveLogToDataBase(OPRID,strParams,strRetContent,"","",comID,sPageID,strOprType);
		}

		return strRetContent;
	}

}
