package com.tranzvision.gd.TZApplicationTemplateBundle.controller;

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

import com.tranzvision.gd.TZApplicationTemplateBundle.service.impl.AppFormListClsServiceImpl;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzComPageAuthServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdKjComServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdObjectServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;

/**
 * 报名表模板配置控制器
 * 
 * @author WRL
 * @since 2016-02-25
 */
@Controller
@RequestMapping(value = { "/admission" })
public class AppFormController {
	@Autowired
	private AppFormListClsServiceImpl appFormListClsServiceImpl;
	
	@Autowired
	private TzComPageAuthServiceImpl tzComPageAuthServiceImpl;
	
	@Autowired
	private GdKjComServiceImpl gdKjComService;
	
	@Autowired
	private GdObjectServiceImpl gdObjectServiceImpl;
	
	@RequestMapping(value = { "/diyform/{tplId}" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String dispatcher(HttpServletRequest request, HttpServletResponse response, @PathVariable(value = "tplId") String tplId) {
		//组件名称 
		String comName = "TZ_ONLINE_REG_COM";
		//页面名称
		String pageName = "TZ_ONREG_EDIT_STD";
		//OPRID
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
		//校验组件页面的读写访问权限
		boolean permission = tzComPageAuthServiceImpl.checkUpdatePermission(oprid, comName, pageName, errorMsg);

		if(permission){
			//更新权限
			if(StringUtils.isBlank(tplId)){
				errorCode = "1";
				strErrorDesc = "参数-报名表模板ID为空！";
			}else{
				Map<String, Object> comParams = new HashMap<String, Object>();
				comParams.put("TZ_APP_TPL_ID", tplId);
				JacksonUtil jacksonUtil = new JacksonUtil();
				jacksonUtil.Map2json(comParams);
				strComContent = appFormListClsServiceImpl.tzGetHtmlContent(jacksonUtil.Map2json(comParams));
			}
		}else{
			//无更新权限
			errorCode = errorMsg[0];
			strErrorDesc = errorMsg[1];
		}
		
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
		
		return strRetContent;
	}
}
