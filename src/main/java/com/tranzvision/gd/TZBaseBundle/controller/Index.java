package com.tranzvision.gd.TZBaseBundle.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;

import com.tranzvision.gd.TZBaseBundle.service.impl.GdKjComServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdKjInitServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.OperateType;
import com.tranzvision.gd.util.base.TZUtility;
import com.tranzvision.gd.util.sql.SqlQuery;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/")
public class Index {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private JacksonUtil jacksonUtil;
	@Autowired
	private GdKjComServiceImpl gdKjComService;
	@Autowired
	private GdKjInitServiceImpl gdKjInitService;

	@RequestMapping(value = "index")
	public String index(HttpServletRequest request, HttpServletResponse response) {

		// 获取当前提交的主题编号;
		String tmpSubmitThemeID = request.getParameter("theme");
		// 获取当前提交语言环境代码;
		String tmpSubmitLanguageCd = request.getParameter("language");
		/* 如果没有指定language参数，则获取当前登录会话语言环境代码 */
		if (tmpSubmitLanguageCd == null || "".equals(tmpSubmitLanguageCd)) {
			tmpSubmitLanguageCd = gdKjComService.getLoginLanguage(request, response);
		}

		/* 先清空当前访问组件、当前访问页面全局变量; */
		gdKjComService.setCurrentAccessComponentPage(request, "", "");

		String tmpLanguageCd = "";

		if (gdKjComService.isSessionValid(request) == true) {

			// 记住当前登录用户的主题设置;
			gdKjComService.setUserGxhTheme(request, response, tmpSubmitThemeID);
			// 记住当前登录用户的语言环境设置;
			gdKjComService.setUserGxhLanguage(request, response, tmpSubmitLanguageCd);

			tmpLanguageCd = gdKjComService.getUserGxhLanguage(request, response);
		}

		// 切换会话语言环境代码;
		gdKjComService.switchLanguageCd(request, response, tmpLanguageCd);

		String parth = request.getContextPath();
		request.setAttribute("tz_gdcp_interaction_url_20150612184830", parth + "/dispatcher");

		request.setAttribute("tz_gdcp_frmwrk_init_msgset_20150612184830",
				gdKjComService.getFrameworkDescriptionResources(request, response));

		request.setAttribute("tz_gdcp_theme_id_20150612184830",
				TZUtility.transFormchar(gdKjComService.getUserGxhTheme(request, response)));

		request.setAttribute("tz_gdcp_language_cd_20150612184830",
				TZUtility.transFormchar(gdKjComService.getUserGxhLanguage(request, response)));

		request.setAttribute("tz_gdcp_loginStyle_20150612184830", gdKjComService.getLogoStyle(request, response));

		return "index";
	}

	@RequestMapping(value = "dispatcher", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String dispatcher(HttpServletRequest request, HttpServletResponse response) {

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
			String pageSql = "SELECT TZ_COM_ID, TZ_PAGE_ID WHERE TZ_PAGE_REFCODE= ?";
			try {
				Map<String, Object> map = jdbcTemplate.queryForMap(pageSql, new Object[] { tmpClassId });
				tmpComId = (String) map.get("TZ_COM_ID");
				tmpPageId = (String) map.get("TZ_PAGE_ID");
			} catch (DataAccessException e) {

			}

			String tmpOperateType = request.getParameter("OperateType");
			if (tmpOperateType == null || "".equals(tmpOperateType)) {
				tmpOperateType = "HTML";
			} else {
				tmpOperateType = tmpOperateType.toUpperCase();
			}

			/*
			 * 检查comParams是否是一个合法的JSON字符串，如果不是，则将临时变量&tmpComParams赋值为空JSON对象字符串
			 */
			/*
			 * try { tmpComParams = request.getParameter("comParams");
			 * PaseJsonUtil.getJson(tmpComParams); } catch (Exception e) {
			 * e.printStackTrace(); tmpComParams = "{}"; }
			 */
			tmpComParams = request.getParameter("comParams");
			if (tmpComParams == null || "".equals(tmpComParams)) {
				tmpComParams = "{}";
			}

			strParams = "{\"ComID\":\"" + tmpComId + "\",\"PageID\":\"" + tmpPageId + "\",\"OperateType\":\""
					+ tmpOperateType + "\",\"comParams\":" + tmpComParams + "}";

		}

		// 将字符串转换成json;
		/*
		 * JSONObject CLASSJson = null; try { CLASSJson =
		 * PaseJsonUtil.getJson(strParams); } catch (Exception e) { //
		 * e.printStackTrace(); }
		 */

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
			// LOGOUT;
			case LOGOUT:
				// TODO;
				break;
			default:
				// 组件ID;
				String comID = jacksonUtil.getString("ComID");
				// 页面ID;
				String sPageID = jacksonUtil.getString("PageID");
				// 通用参数;
				// String sCommParams = CLASSJson.getString("comParams");
				map = jacksonUtil.getMap("comParams");

				String sCommParams = jacksonUtil.Map2json(map).toString();
				if ((sCommParams == null || "null".equals(sCommParams))) {
					// 错误描述;
					// strErrorDesc = sCommParams + "提交的json数据无效";
					// 错误码;
					// errorCode = "1";
					sCommParams = "{}";
				}
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
			if (!"1".equals(errorCode)) {
				strRetContent = strComContent;
			} else {
				if (gdKjComService.isSessionValid(request)) {
					strRetContent = strErrorDesc;
				} else {
					// TODO;
					// strRetContent =
					// this.getTimeoutHTML("TZGD_SQR_HTML_TIMEOUT");
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
					+ ",\"errdesc\": \"" + TZUtility.transFormchar(strErrorDesc) + "\",\"timeout\": " + tmpTimeOutFlag
					+ ",\"authorizedInfo\": {" + authorizedInfomation + "}}}";
		}
		return strRetContent;
	}
}
