package com.tranzvision.gd.TZApplicationSurveyBundle.service.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	public String wxOAuthOnSurveyInit(HttpServletRequest request, HttpServletResponse response, String surveyID){
		String result = "";
		try{
			Boolean isWeChart = CommonUtils.isWeChartBrowser(request);
			if(isWeChart){
				GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
				TzWeChartOAuth2 tzWeChartOAuth2 = (TzWeChartOAuth2) getSpringBeanUtil.getSpringBeanByID("tzWeChartOAuth2");
				
				result = tzWeChartOAuth2.wxOAuthOnSurveyInit(request, response, surveyID);
			}
		}catch(Exception e){
			e.printStackTrace();
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
	public String surveySubmitToSalesforce(String msg,String strAppInsId, String openId, Integer currPageId, Integer targetPageId, String jsonParams){
		String result = "";
		try{
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			TzWeChartOAuth2 tzWeChartOAuth2 = (TzWeChartOAuth2) getSpringBeanUtil.getSpringBeanByID("tzWeChartOAuth2");

			String resultArr[] = tzWeChartOAuth2.surveySubmitToSalesforce(strAppInsId, openId);
			result = resultArr[0];
			msg = resultArr[1];
		}catch(Exception e){
			e.printStackTrace();
			result = "1";
			msg = e.getMessage();
		}
		
		return result;
	}
}
