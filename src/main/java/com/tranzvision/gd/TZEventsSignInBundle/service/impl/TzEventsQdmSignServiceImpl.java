package com.tranzvision.gd.TZEventsSignInBundle.service.impl;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.TZGDObject;

@Service("com.tranzvision.gd.TZEventsSignInBundle.service.impl.TzEventsQdmSignServiceImpl")
public class TzEventsQdmSignServiceImpl extends FrameworkImpl{

	@Autowired
	private TZGDObject tzGdObject;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	
	@Override
	public String tzGetHtmlContent(String strParams) {
		String qdmInputHtml = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			//当前登录机构
			String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			
			String contextPath = request.getContextPath();
			// 通用链接;
			String ZSGL_URL = contextPath + "/dispatcher";
			
			//构造签到url参数
			Map<String,String> comQdParamsMap = new HashMap<String,String>();

			Map<String,Object> tzQdParamsMap = new HashMap<String,Object>();
			tzQdParamsMap.put("ComID", "TZ_HD_SIGN_COM");
			tzQdParamsMap.put("PageID", "TZ_HD_SIGNIN_STD");
			tzQdParamsMap.put("OperateType", "EJSON");
			tzQdParamsMap.put("comParams", comQdParamsMap);
			//签到码签到URL
			String str_sign_url = ZSGL_URL + "?tzParams=" + URLEncoder.encode(jacksonUtil.Map2json(tzQdParamsMap),"UTF-8");
			//返回主菜单
			//String str_main_url = ZSGL_URL + "?classid=eventsSign";
			String str_main_url = contextPath + "/signIn/"+orgId;
			
			//签到人详情确认页URL
			String str_qdr_xq_url  = ZSGL_URL + "?classid=signConfirm";
			
			//ajax请求url
			Map<String,Object> ajaxParamsMap = new HashMap<String,Object>();
			ajaxParamsMap.put("ComID", "TZ_HD_SIGN_COM");
			ajaxParamsMap.put("PageID", "TZ_HD_SIGN_STD");
			ajaxParamsMap.put("OperateType", "EJSON");
			ajaxParamsMap.put("comParams", new HashMap<String,String>());
			String str_ajax_url  =  ZSGL_URL + "?tzParams=" + URLEncoder.encode(jacksonUtil.Map2json(ajaxParamsMap),"UTF-8");
		
			qdmInputHtml = tzGdObject.getHTMLText("HTML.TZEventsSignInBundle.TZ_HDQD_QDM_INPUT_HTML",
					contextPath,str_sign_url,str_qdr_xq_url,str_main_url,str_ajax_url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return qdmInputHtml;
	}
}
