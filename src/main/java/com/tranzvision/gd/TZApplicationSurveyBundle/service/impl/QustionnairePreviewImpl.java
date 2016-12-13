package com.tranzvision.gd.TZApplicationSurveyBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdKjComServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdObjectServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.MessageTextServiceImpl;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

@Service("com.tranzvision.gd.TZApplicationSurveyBundle.service.impl.QustionnairePreviewImpl")
public class QustionnairePreviewImpl extends FrameworkImpl {

	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private SqlQuery jdbcTemplate;
	
	@Autowired
	private GdObjectServiceImpl gdObjectServiceImpl;
	
	@Autowired
	private HttpServletResponse response;


	@Autowired
	private QuestionnaireEditorEngineImpl questionnaireEditorEngineImpl;

	@Autowired
	private MessageTextServiceImpl messageTextServiceImpl;

	
	@Autowired
	private TempEditorEngineImpl tempEditorEngineImpl;
	
	@Autowired
	private GdKjComServiceImpl gdKjComService;
	
	@Autowired
	private TZGDObject tzGdObject;
	
	@Autowired
	private LogicControlImpl logicControlImpl;
	//预览 实际上是动态生成一个HTML
	@Override
	public String tzGetHtmlContent(String strParams) {
		System.out.println("====QustionnairePreviewImpl====strParams"+strParams);
		JacksonUtil jacksonUtil=new JacksonUtil();
		jacksonUtil.json2Map(strParams);
		Map<String,Object>dataMap=jacksonUtil.getMap();

		//数据如何获取 request中是否存在？
		System.out.println("===QustionnairePreviewImpl===classid:"+request.getParameter("classid"));
		//问卷ID 类型  问卷实例ID
		String survyID="";
		String type="P";
		int surveyInsId=0; 
		String surveyLogic=null;
		//调查问卷应用编号 
		String str_appId=request.getParameter("classid");
		//应用ID不为空 则从request中取数据，否则从Map中取数据？
		if(str_appId!=null){
			survyID=request.getParameter("SURVEY_ID");
			type=request.getParameter("TYPE")==null?"TPL":request.getParameter("TYPE");
			surveyInsId=Integer.valueOf(request.getParameter("SURVEY_INS_ID").toString());
		}
		else{
			if(dataMap==null)
				return null;
			if(dataMap.containsKey("SURVEY_ID"))
				survyID=dataMap.get("SURVEY_ID").toString();
			if(dataMap.containsKey("TYPE")&&dataMap.get("TYPE")!=null)
				type=dataMap.get("TYPE").toString();
			if(dataMap.containsKey("SURVEY_INS_ID")&&dataMap.get("SURVEY_INS_ID")!=null)
				surveyInsId=Integer.valueOf(dataMap.get("SURVEY_INS_ID").toString());
		}
		//是否可以填写调查问卷 ?是否绝对的不许改？
		System.out.println("==预览type:=="+type);
		String language;
		Map<String,Object>survyMap=new HashMap<String,Object>();
		if(type.equals("TPL")){
			String SQL="SELECT TZ_DC_JTNR,TZ_DC_JWNR,TZ_APP_TPL_LAN,TZ_APP_TPL_MC FROM PS_TZ_DC_DY_T WHERE TZ_APP_TPL_ID = ?";
			survyMap=jdbcTemplate.queryForMap(SQL,new Object[]{survyID});
		}
		else{
			String SQL="SELECT TZ_DC_JTNR,TZ_DC_JWNR,TZ_APP_TPL_LAN,TZ_DC_WJBT FROM PS_TZ_DC_WJ_DY_T WHERE TZ_DC_WJ_ID =?";
			survyMap=jdbcTemplate.queryForMap(SQL, new Object[]{survyID});
		}
		//如果语言是null则从登陆中取值
		language=survyMap.get("TZ_APP_TPL_LAN")==null?"ZHS":survyMap.get("TZ_APP_TPL_LAN").toString();
		survyMap.replace("TZ_APP_TPL_LAN", language);		

	     
		//分页条 上一页  下一页 中文和英语  具体分页条提示数据在哪？
		//String strPre=language.equals("ZHS")?"上一页":"prePage";
		//String strNext=language.equals("ZHS")?"下一页":"nextPage";
		//String strROnly=language.equals("ZHS")?"只读":"readOnly";
		//String strROnlyDesc=language.equals("ZHS")?"您只能查看历史在线调查":"you can only read online";
		//String strView=language.equals("ZHS")?"预览模式":"preview";
		//String strViewDesc=language.equals("ZHS")?"您无法提交任何数据":"you can not submit any data";
		
		String strPre=messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_SURVEY_MSGSET", "SURVEY_PRE", language, "上一页", "Pre");
		String strNext=messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_SURVEY_MSGSET", "SURVEY_NEXT", language, "下一页", "Next");
		String strROnly=messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_SURVEY_MSGSET", "SURVEY_READONLY", language, "只读模式", "ReadOnly");
		String strROnlyDesc=messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_SURVEY_MSGSET", "SURVEY_READONLY_DESC", language, "您只能查看历史在线调查", "Save");
		String strView=messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_SURVEY_MSGSET", "SURVEY_VIEW", language, "预览模式", "View");
		String strViewDesc=messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_SURVEY_MSGSET", "SURVEY_VIEW_DESC", language, "您无法提交任何数据", "View");
		//文件报文数据
		String surveyData=null;
		String surveyInsData=null;
		if(type.equals("TPL")){
			final String SQL1="SELECT TZ_APPTPL_JSON_STR FROM PS_TZ_DC_DY_T WHERE TZ_APP_TPL_ID = ?";
			surveyData=jdbcTemplate.queryForObject(SQL1, new Object[]{survyID},"String");
		}
		else{
			final String SQL2="SELECT TZ_APPTPL_JSON_STR FROM PS_TZ_DC_WJ_DY_T WHERE TZ_DC_WJ_ID = ?";
			surveyData=jdbcTemplate.queryForObject(SQL2, new Object[]{survyID},"String");
		}
		//surveyInsId可以取0么？
		if(surveyInsId!=0){
			final String SQL="SELECT TZ_APPINS_JSON_STR FROM PS_TZ_DC_INS_T WHERE TZ_DC_WJ_ID = ? AND TZ_APP_INS_ID = ?";
			surveyInsData=jdbcTemplate.queryForObject(SQL, new Object[]{survyID,surveyInsId},"String");
		}
		Object numMaxPage;
		if(type.equals("TPL")){
			final String SQL="SELECT MAX(TZ_PAGE_NO) + 1 FROM PS_TZ_DC_XXXPZ_T WHERE TZ_APP_TPL_ID =?";
			numMaxPage=jdbcTemplate.queryForObject(SQL, new Object[]{survyID},"Object");
		}
		else{
			final String SQL="SELECT MAX(TZ_PAGE_NO) + 1 FROM PS_TZ_DCWJ_XXXPZ_T WHERE TZ_DC_WJ_ID =?";
			
			numMaxPage=jdbcTemplate.queryForObject(SQL, new Object[]{survyID},"Object");
		}
	    if(numMaxPage==null)
	    	numMaxPage=1;
		//控件信息
		String strComRegInfo=null;
		if(type.equals("TPL")){
			strComRegInfo=tempEditorEngineImpl.getComponentData(survyID);
		}


		else{
			strComRegInfo=questionnaireEditorEngineImpl.getComponentData();
		}


		Map<String,Object>componentDfnMap=new HashMap<String,Object>();
		JacksonUtil util=new JacksonUtil();
		util.json2Map(strComRegInfo);
		componentDfnMap=util.getMap();
		
		strComRegInfo=util.List2json((ArrayList<?>) componentDfnMap.get("componentDfn"));	
		if(surveyInsData==null||surveyInsData.equals("")){
			surveyInsData="''";
		}
		System.out.println("===surveyInsData===x="+surveyInsData);
		if(surveyInsId>0){
			//逻辑处理 先没做
			surveyLogic=logicControlImpl.getSurveyLogicJson(String.valueOf(surveyInsId));
		}
		System.out.println("==surveyLogic:=="+surveyLogic);
		if(surveyLogic==null){
			surveyLogic="''";
		}
		//请求URL如何获取
		//String tzGeneralURL="";
		
		//问卷标题 问卷语言
		final String sql="SELECT TZ_DC_WJBT,TZ_APP_TPL_LAN FROM PS_TZ_DC_WJ_DY_T WHERE TZ_DC_WJ_ID =?";

		String strTitle="";
		Map<String,Object>resultMap=jdbcTemplate.queryForMap(sql, new Object[]{survyID});
		if(resultMap!=null){
			language=resultMap.get("TZ_APP_TPL_LAN")==null?null:resultMap.get("TZ_APP_TPL_LAN").toString();
			strTitle=resultMap.get("TZ_DC_WJBT")==null?null:resultMap.get("TZ_DC_WJBT").toString();
		}


		JacksonUtil jsonUtil=new JacksonUtil();
		String tplHtml;
		
		//url
		String contextUrl = request.getContextPath();
		String tzGeneralURL = contextUrl + "/dispatcher";
		
		//msg不知道干嘛的
		String strMsgSet = gdObjectServiceImpl.getMessageSetByLanguageCd(request, response, "TZGD_SURVEY_MSGSET", language);
		if (StringUtils.isBlank(language)) {
			language = gdKjComService.getLoginLanguage(request, response);
		}
		if(StringUtils.isBlank(language)){
			language="ZHS";
		}
		System.out.println("===预览0==strMsgSet："+strMsgSet);
		System.out.println("==strLang:=="+language);
		jsonUtil.json2Map(strMsgSet);
		if (jsonUtil.containsKey(language)) {
			Map<String, Object> msgLang = jsonUtil.getMap(language);
			strMsgSet = jsonUtil.Map2json(msgLang);
			System.out.println("===预览===strMsgSet:"+strMsgSet);
			
		}
		String URL=request.getContextPath();
		String header=survyMap.get("TZ_DC_JTNR")==null?"":survyMap.get("TZ_DC_JTNR").toString();
		String footer=survyMap.get("TZ_DC_JWNR")==null?"":survyMap.get("TZ_DC_JWNR").toString();
		try {
			tplHtml = tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_SURVEY_VIEW_HTML", header,footer,
					strComRegInfo,surveyData, surveyInsData,String.valueOf(numMaxPage), language, strTitle, strPre, strNext, strROnly, strROnlyDesc, strView, strViewDesc, String.valueOf(surveyInsId), surveyLogic, tzGeneralURL, strMsgSet,URL);
		} catch (TzSystemException e) {
			e.printStackTrace();
			tplHtml = "";
		}


		System.out.println("===surveyData:"+surveyData);
		System.out.println("======strComRegInfo:"+strComRegInfo);
		return tplHtml;
	}


}
