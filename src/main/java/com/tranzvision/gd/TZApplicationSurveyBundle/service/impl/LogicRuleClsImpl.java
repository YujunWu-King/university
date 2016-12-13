package com.tranzvision.gd.TZApplicationSurveyBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;

@Service("com.tranzvision.gd.TZApplicationSurveyBundle.service.impl.LogicRuleClsImpl")
public class LogicRuleClsImpl extends FrameworkImpl {
	@Autowired
	private LogicEditorEngineImpl logicEditorEngineImpl;

	@Autowired
	private HttpServletRequest request;

	/* 保存逻辑规则 */
	@SuppressWarnings("unchecked")
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		String strRet = "{}";
		for (int i = 0; i < actData.length; i++) {
			// 逻辑规则内容
			String strLogic = actData[i];

			System.out.println("strLogic" + i + ":" + strLogic);

			// 将字符串转换成json
			Map<String, Object> objJsonMap = new HashMap<String, Object>();
			jacksonUtil.json2Map(strLogic);
			objJsonMap = jacksonUtil.getMap();
			// 在线调查模板ID
			String strSurvTid = objJsonMap.get("SURVEY_ID") == null ? null : objJsonMap.get("SURVEY_ID").toString();
			// 逻辑规则JSON数据
			List<Map<String, Object>> jsonDataList = new ArrayList<Map<String, Object>>();
			if (objJsonMap.get("data") != null) {
				jsonDataList = (List<Map<String, Object>>) objJsonMap.get("data");
			}
			
			strRet = logicEditorEngineImpl.saveLogicDefn(strSurvTid, jsonDataList, "WJ");
		}

		return strRet;
	}

	@Override
	public String tzGetHtmlContent(String strParams) {
		String editHtml;

		//模板逻辑 和问卷逻辑调用同一个类 将模板逻辑中传递的参数TZ_APP_TPL_ID(JS页面中)改为TZ_DC_WJ_ID
		//模板逻辑 多传递一个EXECUTE参数作为标志
		String tplId = request.getParameter("TZ_DC_WJ_ID");
		System.out.println("==执行逻辑页面生成,tplId=" + tplId);
		if(request.getParameter("EXECUTE")!=null&&request.getParameter("EXECUTE").equals("MB")){
			editHtml=logicEditorEngineImpl.init(tplId);
		}
		else{	
			editHtml = logicEditorEngineImpl.wjInit(tplId);}
		// System.out.println(editHtml);
		return editHtml;
	}

}
