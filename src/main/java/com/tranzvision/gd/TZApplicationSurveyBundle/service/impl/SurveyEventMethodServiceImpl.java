package com.tranzvision.gd.TZApplicationSurveyBundle.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.tranzvision.gd.TZWeChatBundle.service.impl.TzWeChartOAuth2;
import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.httpclient.CommonUtils;

public class SurveyEventMethodServiceImpl {
	
	
	
	/**
	 * 在线报名问卷初始化事件，获取openid
	 * @param request
	 * @param response
	 * @return
	 */
	public Map<String, Object> wxOAuthOnSurveyInit(HttpServletRequest request, HttpServletResponse response, String surveyID){
		Map<String, Object> result = new HashMap<String, Object>();
		try{
			Boolean isWeChart = CommonUtils.isWeChartBrowser(request);
			if(isWeChart){
				GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
				TzWeChartOAuth2 tzWeChartOAuth2 = (TzWeChartOAuth2) getSpringBeanUtil.getSpringBeanByID("tzWeChartOAuth2");
				
				String resMsg = tzWeChartOAuth2.wxOAuthOnSurveyInit(request, response, surveyID);
				System.out.println("------初始化："+resMsg);
				if(StringUtils.isNotBlank(resMsg)){
					result.put("code", "0");
					result.put("msg", resMsg);
				}else{
					result.put("code", "1");
					result.put("msg", resMsg);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			result.put("code", "1");
			result.put("msg", e.getMessage());
		}
		
		return result;
	}
	
	
	
	/**
	 * 在线报名提交给salesforce
	 * @param appInsId
	 * @param openId
	 * @param currPageId
	 * @param targetPageId
	 * @param jsonParams
	 */
	public Map<String, Object> surveySubmitToSalesforce(String strAppInsId, String openId, Integer currPageId, Integer targetPageId, String jsonParams){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", "0");
		result.put("msg", "");
		try{
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			TzWeChartOAuth2 tzWeChartOAuth2 = (TzWeChartOAuth2) getSpringBeanUtil.getSpringBeanByID("tzWeChartOAuth2");

			if(openId != null && !"".equals(openId)){
				String resultArr[] = tzWeChartOAuth2.surveySubmitToSalesforce(strAppInsId, openId);
				result.replace("code", resultArr[0]);
				result.replace("msg", resultArr[1]);
			}
		}catch(Exception e){
			e.printStackTrace();
			result.replace("code", "1");
			result.replace("msg", e.getMessage());
		}
		
		return result;
	}
}
