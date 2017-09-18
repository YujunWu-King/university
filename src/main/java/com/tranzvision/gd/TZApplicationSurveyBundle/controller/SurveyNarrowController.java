package com.tranzvision.gd.TZApplicationSurveyBundle.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tranzvision.gd.TZApplicationSurveyBundle.service.impl.QuestionnaireFillImpl;
import com.tranzvision.gd.util.base.JacksonUtil;

@Controller
@RequestMapping(value = { "/" })
public class SurveyNarrowController {

	@Autowired
	private QuestionnaireFillImpl questionnaireFillImpl;
	
	@RequestMapping(value = { "s" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String dispatcher(HttpServletRequest request, HttpServletResponse response) {
		
		// 报文内容;
		String strComContent = "";
		// 错误描述;
		String strErrorDesc = "";
		// 错误码;
		String errorCode = "0";
		// 返回值;
		String strRetContent = "";

		// 错误信息
		//String[] errorMsg = { "0", "" };
		
		String tplId2=request.getParameter("SURVEY_WJ_ID");
		
		String tplId=request.getQueryString();
		if (tplId.indexOf("unique")>-1) {
			
			Map<String, Object> comParams = new HashMap<String, Object>();
			comParams.put("SURVEY_WJ_ID", tplId2);
			
			String uniqueNum = String.valueOf(((int) (Math.random() * 100)) * 951)
					+ String.valueOf(((int) (Math.random() * 100)) * 233)
					+ String.valueOf(((int) (Math.random() * 100)) * 5713)
					+ String.valueOf(((int) (Math.random() * 100)) * 35771) + "000000000000000";
			uniqueNum = uniqueNum.substring(0, 15);
			
			
			comParams.put("unique", uniqueNum);
			JacksonUtil jacksonUtil = new JacksonUtil();
			jacksonUtil.Map2json(comParams);
			strComContent = questionnaireFillImpl.tzGetHtmlContent(jacksonUtil.Map2json(comParams));
			strRetContent=strComContent;
			
		}else{
			if (StringUtils.isBlank(tplId2)) {
				errorCode = "1";
				strErrorDesc = "问卷ID为空！";
			} else {
				Map<String, Object> comParams = new HashMap<String, Object>();
				comParams.put("SURVEY_WJ_ID", tplId2);
//				comParams.put("classid", "surveyapp");
				JacksonUtil jacksonUtil = new JacksonUtil();
				jacksonUtil.Map2json(comParams);
				strComContent = questionnaireFillImpl.tzGetHtmlContent(jacksonUtil.Map2json(comParams));
				strRetContent=strComContent;
			}
		}
/*
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
*/
		
		//System.out.println("strComContent====="+strRetContent);
		return strRetContent;
	}
	
	
	
	
	
}
