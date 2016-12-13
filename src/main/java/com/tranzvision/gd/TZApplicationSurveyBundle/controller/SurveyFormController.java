package com.tranzvision.gd.TZApplicationSurveyBundle.controller;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tranzvision.gd.TZApplicationSurveyBundle.service.impl.QuestionnaireEditClsServiceImpl;
import com.tranzvision.gd.TZApplicationSurveyBundle.service.impl.SurveyEditClsServiceImpl;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzComPageAuthServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdKjComServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdObjectServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;

/**
 * @author caoy
 * @version 创建时间：2016年8月4日 下午3:05:33 类说明 问卷模板配置控制器
 */
@Controller
@RequestMapping(value = { "/admission" })
public class SurveyFormController {

	@Autowired
	private GdKjComServiceImpl gdKjComService;

	@Autowired
	private TzComPageAuthServiceImpl tzComPageAuthServiceImpl;

	@Autowired
	private GdObjectServiceImpl gdObjectServiceImpl;

	@Autowired
	private SurveyEditClsServiceImpl surveyEditClsServiceImpl;

	@Autowired
	private QuestionnaireEditClsServiceImpl questionnaireEditClsServiceImpl;

	/**
	 * 模版设置页面打开
	 * 
	 * @param request
	 * @param response
	 * @param tplId
	 * @return
	 */
	@RequestMapping(value = { "/surveyform/{tplId}" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String dispatcher(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "tplId") String tplId) {
		// 组件名称
		String comName = "TZ_ZXDC_MBGL_COM";
		// 页面名称
		String pageName = "TZ_ZXDC_EDIT_STD";
		// OPRID
		String oprid = gdKjComService.getOPRID(request);
		// 报文内容;
		String strComContent = "";
		// 错误描述;
		String strErrorDesc = "";
		// 错误码;
		String errorCode = "0";
		// 返回值;
		String strRetContent = "";

		// 错误信息
		String[] errorMsg = { "0", "" };
		// 校验组件页面的读写访问权限
		boolean permission = tzComPageAuthServiceImpl.checkUpdatePermission(oprid, comName, pageName, errorMsg);

		if (permission) {
			// 更新权限
			if (StringUtils.isBlank(tplId)) {
				errorCode = "1";
				strErrorDesc = "参数-报名表模板ID为空！";
			} else {
				Map<String, Object> comParams = new HashMap<String, Object>();
				comParams.put("ZXDC_TPL_ID", tplId);
				JacksonUtil jacksonUtil = new JacksonUtil();
				jacksonUtil.Map2json(comParams);
				strComContent = surveyEditClsServiceImpl.tzGetHtmlContent(jacksonUtil.Map2json(comParams));
			}
		} else {
			// 无更新权限
			errorCode = errorMsg[0];
			strErrorDesc = errorMsg[1];
		}

		if ("0".equals(errorCode)) {
			strRetContent = strComContent;
		} else {
			if (gdKjComService.isSessionValid(request)) {
				strRetContent = strErrorDesc;
			} else {
				strRetContent = gdObjectServiceImpl.getTimeoutHTML(request, "HTML.TZBaseBundle.TZGD_SQR_HTML_TIMEOUT");
				if (strRetContent == null || "".equals(strRetContent)) {
					strRetContent = strErrorDesc;
				}
			}
		}

		return strRetContent;
	}

	/**
	 * 问卷设置页面打开
	 * 
	 * @param request
	 * @param response
	 * @param tplId
	 * @return
	 */
	@RequestMapping(value = { "/wjform/{wjId}" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportAppForm(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "wjId") String wjId) {
		// 组件名称
		// TZ_ZXDC_WJGL_COM","PageID":"TZ_ZXDC_EDIT_STD
		String comName = "TZ_ZXDC_WJGL_COM";
		// 页面名称
		String pageName = "TZ_ZXDC_EDIT_STD";
		// OPRID
		String oprid = gdKjComService.getOPRID(request);
		// 报文内容;
		String strComContent = "";
		// 错误描述;
		String strErrorDesc = "";
		// 错误码;
		String errorCode = "0";
		// 返回值;
		String strRetContent = "";

		// 错误信息
		String[] errorMsg = { "0", "" };
		// 校验组件页面的读写访问权限
		boolean permission = tzComPageAuthServiceImpl.checkUpdatePermission(oprid, comName, pageName, errorMsg);

		if (permission) {
			// 更新权限
			if (StringUtils.isBlank(wjId)) {
				errorCode = "1";
				strErrorDesc = "参数-调查问卷ID为空！";
			} else {
				Map<String, Object> comParams = new HashMap<String, Object>();
				comParams.put("ZXDC_WJ_ID", wjId);
				JacksonUtil jacksonUtil = new JacksonUtil();
				jacksonUtil.Map2json(comParams);
				strComContent = questionnaireEditClsServiceImpl.tzGetHtmlContent(jacksonUtil.Map2json(comParams));
			}
		} else {
			// 无更新权限
			errorCode = errorMsg[0];
			strErrorDesc = errorMsg[1];
		}

		if ("0".equals(errorCode)) {
			strRetContent = strComContent;
		} else {
			if (gdKjComService.isSessionValid(request)) {
				strRetContent = strErrorDesc;
			} else {
				strRetContent = gdObjectServiceImpl.getTimeoutHTML(request, "HTML.TZBaseBundle.TZGD_SQR_HTML_TIMEOUT");
				if (strRetContent == null || "".equals(strRetContent)) {
					strRetContent = strErrorDesc;
				}
			}
		}

		return strRetContent;
	}
}
