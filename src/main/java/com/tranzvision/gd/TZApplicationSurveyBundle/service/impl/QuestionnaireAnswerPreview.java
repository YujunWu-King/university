package com.tranzvision.gd.TZApplicationSurveyBundle.service.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
import com.tranzvision.gd.util.sql.type.TzObject;
@Service("com.tranzvision.gd.TZApplicationSurveyBundle.service.impl.QuestionnaireAnswerPreview")

public class QuestionnaireAnswerPreview extends FrameworkImpl {

	@Autowired
	private TZGDObject tzGdObject;
	
	@Autowired
	private SqlQuery jdbcTemplate;
	
	@Autowired
	private HttpServletRequest request;
	@Override
	public String tzGetHtmlContent(String strParams) {
		System.out.println("===在线查看答案===strParams："+strParams);
		String strReturn="";
		String strDivHtml="";
		String strDivTrHtml="";
		
		 /*参数*/
		JacksonUtil jacksonUtil=new JacksonUtil();
		Map<String,Object>jsonParams=new HashMap<String,Object>();
		jacksonUtil.json2Map(strParams);
		jsonParams=jacksonUtil.getMap();
		/*问卷编号*/
		String surveyID=null;
		/*调查问卷应用编号*/
		String str_appId = request.getParameter("classid");
		/*从参数中获取模板、问卷编号*/
		if(str_appId!=null){
			surveyID=request.getParameter("SURVEY_ID");
		}
		else{
			if(jsonParams.containsKey("SURVEY_ID")&&jsonParams.get("SURVEY_ID")!=null){
				surveyID=jsonParams.get("SURVEY_ID").toString();
			}
		}
		try {
			strReturn = tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_SURVEY_ANS_HTML",request.getContextPath());
		} catch (TzSystemException e) {
			strReturn="";
			e.printStackTrace();
		}
		String TZ_XXX_BH, TZ_TITLE, TZ_XXX_QID, TZ_COM_LMC, TZ_XXXKXZ_MS, TZ_XXXKXZ_QZ, TZ_XXXKXZ_MC, TZ_APP_S_TEXT, TZ_APP_L_TEXT, TZ_KXX_QTZ, TZ_APP_INS_ID, TZ_QU_CODE, TZ_QU_NAME, TZ_OPT_CODE, TZ_OPT_NAME, ATTACHSYSFILENAME, TZ_INDEX, ATTACHUSERFILE, ROW_ADDED_DTTM;
		String strMulChoiceHtml, strMulChoiceTrHtml, strComboBoxHtml, strComTempHtml, strRadioBoxHtml, strQuantifyQuHtml;
		   
		int countY, countN,count , score1, tempCountN;
		double score;
		double tempCount;
		//用来保留小数2位位数
		DecimalFormat decimalFormat = new DecimalFormat("#.00");
		//TZ_XXX_QID?
		//所有循环的索引都用index,i,j
		final String dcwjXxxPzSQL="select TZ_XXX_BH,TZ_TITLE,TZ_XXX_QID,TZ_COM_LMC from PS_TZ_DCWJ_XXXPZ_T where TZ_DC_WJ_ID=? and  TZ_COM_LMC not in ('PageNav') order by TZ_ORDER";
		List<Map<String,Object>>dcwjXxxPzDataList=new ArrayList<Map<String,Object>>();
		dcwjXxxPzDataList=jdbcTemplate.queryForList(dcwjXxxPzSQL, new Object[]{surveyID});
		if(dcwjXxxPzDataList!=null){
			for(int index=0;index<dcwjXxxPzDataList.size();index++){
				Map<String, Object> dcwjXxxPzMap = new HashMap<String,Object>();
				dcwjXxxPzMap=dcwjXxxPzDataList.get(index);
				
				TZ_XXX_BH=dcwjXxxPzMap.get("TZ_XXX_BH")==null?null:dcwjXxxPzMap.get("TZ_XXX_BH").toString();
				TZ_TITLE=dcwjXxxPzMap.get("TZ_TITLE")==null?"":dcwjXxxPzMap.get("TZ_TITLE").toString();
				TZ_XXX_QID=dcwjXxxPzMap.get("TZ_XXX_QID")==null?"":dcwjXxxPzMap.get("TZ_XXX_QID").toString();
				TZ_COM_LMC=dcwjXxxPzMap.get("TZ_COM_LMC")==null?null:dcwjXxxPzMap.get("TZ_COM_LMC").toString();
				
				//单选题
				if(TZ_COM_LMC!=null&&TZ_COM_LMC.equals("RadioBox")){
				final String radioBoxSQL="select TZ_XXXKXZ_MS,TZ_XXXKXZ_MC from PS_TZ_DCWJ_XXKXZ_T where TZ_DC_WJ_ID=? and  TZ_XXX_BH=? order by TZ_ORDER";
				List<Map<String,Object>>radioBoxDataList=new ArrayList<Map<String,Object>>();
				radioBoxDataList=jdbcTemplate.queryForList(radioBoxSQL, new Object[]{surveyID,TZ_XXX_BH});
				if(radioBoxDataList!=null){
					strRadioBoxHtml="";
					for(int i=0;i<radioBoxDataList.size();i++){
						Map<String,Object>radioBoxMap=new HashMap<String,Object>();
						radioBoxMap=radioBoxDataList.get(i);
						TZ_XXXKXZ_MS=radioBoxMap.get("TZ_XXXKXZ_MS")==null?null:radioBoxMap.get("TZ_XXXKXZ_MS").toString();
						TZ_XXXKXZ_MC=radioBoxMap.get("TZ_XXXKXZ_MC")==null?null:radioBoxMap.get("TZ_XXXKXZ_MC").toString();
								
						final String SQL1="select count(*) from PS_TZ_DC_DHCC_T WHERE TZ_APP_INS_ID in (select  TZ_APP_INS_ID from PS_TZ_DC_INS_T where TZ_DC_WJ_ID=?) and TZ_XXX_BH=?";
						count=jdbcTemplate.queryForObject(SQL1, new Object[]{surveyID,TZ_XXX_BH},"int");
						final String SQL2="select count(*) from PS_TZ_DC_DHCC_T WHERE TZ_APP_INS_ID in (select  TZ_APP_INS_ID from PS_TZ_DC_INS_T where TZ_DC_WJ_ID=?) and TZ_XXX_BH=? and TZ_XXXKXZ_MC=?";
						countY=jdbcTemplate.queryForObject(SQL2, new Object[]{surveyID,TZ_XXX_BH,TZ_XXXKXZ_MC},"int");
						
						if(count!=0)
							tempCount=Double.valueOf(decimalFormat.format((double)countY/(double)count*100));
						else
							tempCount=0;
						try {
							strRadioBoxHtml=strRadioBoxHtml+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_MULCHOICE_HTML", TZ_XXXKXZ_MS,tempCount+"%",String.valueOf(countY));
						} catch (TzSystemException e) {
							e.printStackTrace();
						}
					}
					try {
						strRadioBoxHtml=tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_DIV_HTML4",strRadioBoxHtml);
						strDivHtml=strDivHtml+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_DIV_HTML3",TZ_XXX_QID,TZ_TITLE,strRadioBoxHtml);
						System.out.println("===单选题====strRadioBoxHtml:"+strRadioBoxHtml);
					} catch (TzSystemException e) {
						e.printStackTrace();
					}
					strRadioBoxHtml="";
					}
				}
				//单选量表题
				if(TZ_COM_LMC!=null&&TZ_COM_LMC.equals("RadioBoxQu")){
					final String radioBoxQuSQL="select TZ_XXXKXZ_MS,TZ_XXXKXZ_QZ,TZ_XXXKXZ_MC from PS_TZ_DCWJ_XXKXZ_T where TZ_DC_WJ_ID=? and  TZ_XXX_BH=? order by TZ_ORDER";
					List<Map<String,Object>>radioBoxDataList=new ArrayList<Map<String,Object>>();
					radioBoxDataList=jdbcTemplate.queryForList(radioBoxQuSQL, new Object[]{surveyID, TZ_XXX_BH});
					if(radioBoxDataList!=null){
						score=0;
						for(int i=0;i<radioBoxDataList.size();i++){
							Map<String,Object>radioBoxQuMap=new HashMap<String,Object>();
							radioBoxQuMap=radioBoxDataList.get(i);
							
							TZ_XXXKXZ_MS=radioBoxQuMap.get("TZ_XXXKXZ_MS")==null?null:radioBoxQuMap.get("TZ_XXXKXZ_MS").toString();
							TZ_XXXKXZ_QZ=radioBoxQuMap.get("TZ_XXXKXZ_QZ")==null?null:radioBoxQuMap.get("TZ_XXXKXZ_QZ").toString();
							TZ_XXXKXZ_MC=radioBoxQuMap.get("TZ_XXXKXZ_MC")==null?null:radioBoxQuMap.get("TZ_XXXKXZ_MC").toString();
							
							final String SQL1="select count(*) from PS_TZ_DC_DHCC_T WHERE TZ_APP_INS_ID in (select  TZ_APP_INS_ID from PS_TZ_DC_INS_T where TZ_DC_WJ_ID=?) and TZ_XXX_BH=?";
							count=jdbcTemplate.queryForObject(SQL1, new Object[]{surveyID, TZ_XXX_BH},"int");
							final String SQL2="select count(*) from PS_TZ_DC_DHCC_T WHERE TZ_APP_INS_ID in (select  TZ_APP_INS_ID from PS_TZ_DC_INS_T where TZ_DC_WJ_ID=?) and TZ_XXX_BH=? and TZ_XXXKXZ_MC=?";
							countY=jdbcTemplate.queryForObject(SQL2, new Object[]{surveyID, TZ_XXX_BH, TZ_XXXKXZ_MC},"int");
							
							if(count!=0){
								tempCount=Double.valueOf(decimalFormat.format((double)countY/(double)count*100));
								score = score + countY / count * Double.valueOf(TZ_XXXKXZ_QZ);
							}
							else{
								tempCount=0;
							}
				            try {
								strDivTrHtml = strDivTrHtml+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_TR_HTML",TZ_XXXKXZ_MS,tempCount+"%",TZ_XXXKXZ_QZ+"分",String.valueOf(countY));
							} catch (TzSystemException e) {
								strDivTrHtml="";
								e.printStackTrace();
							}
						}
						try {
							strDivTrHtml=tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_DIV_HTML4", strDivTrHtml);
						} catch (TzSystemException e) {
							strDivTrHtml="";
							e.printStackTrace();
						}
				        System.out.println("====单选量表题===strDivTrHtml:"+strDivTrHtml);
				        try {
							strDivHtml=strDivHtml+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_ANSQU_HTML", TZ_XXX_QID,TZ_TITLE,score+"分",strDivTrHtml);
						} catch (TzSystemException e) {
							strDivHtml="";
							e.printStackTrace();
						}
					}
				}
				//多选题
				if(TZ_COM_LMC!=null&&TZ_COM_LMC.equals("MultipleChoice")){
					final String multiChoiceSQL="select TZ_XXXKXZ_MS,TZ_XXXKXZ_MC from PS_TZ_DCWJ_XXKXZ_T where TZ_DC_WJ_ID=? and  TZ_XXX_BH=? order by TZ_ORDER";
					strMulChoiceHtml=""; 
					List<Map<String,Object>>multiChoiceDataList=jdbcTemplate.queryForList(multiChoiceSQL, new Object[]{surveyID, TZ_XXX_BH});
					if(multiChoiceDataList!=null){
						for(int i=0;i<multiChoiceDataList.size();i++){
							Map<String,Object>multiChoiceMap=new HashMap<String,Object>();
							multiChoiceMap=multiChoiceDataList.get(i);
							
							TZ_XXXKXZ_MS=multiChoiceMap.get("TZ_XXXKXZ_MS")==null?null:multiChoiceMap.get("TZ_XXXKXZ_MS").toString();
							TZ_XXXKXZ_MC=multiChoiceMap.get("TZ_XXXKXZ_MC")==null?null:multiChoiceMap.get("TZ_XXXKXZ_MC").toString();
							
							final String SQL1="select count(*) from PS_TZ_DC_DHCC_T WHERE TZ_APP_INS_ID in (select  TZ_APP_INS_ID from PS_TZ_DC_INS_T where TZ_DC_WJ_ID=?) and TZ_XXX_BH=?";
							count=jdbcTemplate.queryForObject(SQL1, new Object[]{surveyID, TZ_XXX_BH},"int");
							final String SQL2="select count(*) from PS_TZ_DC_DHCC_T WHERE TZ_APP_INS_ID in (select  TZ_APP_INS_ID from PS_TZ_DC_INS_T where TZ_DC_WJ_ID=?) and TZ_XXX_BH=? and TZ_XXXKXZ_MC=?";
							countY=jdbcTemplate.queryForObject(SQL2, new Object[]{surveyID, TZ_XXX_BH, TZ_XXXKXZ_MC},"int");
							if(count!=0)
								tempCount=Double.valueOf(decimalFormat.format((double)countY/(double)count*100));
							
							else
								tempCount=0;
							try {
								System.out.println("===多选题==strMulChoiceHtml："+strMulChoiceHtml);
								strMulChoiceHtml = strMulChoiceHtml+ tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_MULCHOICE_HTML", TZ_XXXKXZ_MS,tempCount+"%",String.valueOf(countY));
							} catch (TzSystemException e) {
								strMulChoiceHtml="";
								e.printStackTrace();
							}
						}
					}
					try {
						strMulChoiceHtml=tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_DIV_HTML4", strMulChoiceHtml);
					} catch (TzSystemException e) {
						strMulChoiceHtml="";
						e.printStackTrace();
					}
					try {
						System.out.println("===多选题===strDivHtml:"+strDivHtml);
						strDivHtml=strDivHtml+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_DIV_HTML", TZ_XXX_QID,TZ_TITLE,strMulChoiceHtml);
					} catch (TzSystemException e) {
						strDivHtml="";
						e.printStackTrace();
					} 
				}
				// 下拉框
				if(TZ_COM_LMC!=null&&TZ_COM_LMC.equals("ComboBox")){
					final String comboBoxSQL="select TZ_XXXKXZ_MS,TZ_XXXKXZ_MC from PS_TZ_DCWJ_XXKXZ_T where TZ_DC_WJ_ID=? and  TZ_XXX_BH=? order by TZ_ORDER";
					List<Map<String,Object>>comboBoxDataList=new ArrayList<Map<String,Object>>();
					comboBoxDataList=jdbcTemplate.queryForList(comboBoxSQL, new Object[]{surveyID, TZ_XXX_BH});
					
					if(comboBoxDataList!=null){
						strComboBoxHtml="";
						for(int i=0;i<comboBoxDataList.size();i++){
							Map<String,Object>comboBoxMap=new HashMap<String,Object>();
							comboBoxMap=comboBoxDataList.get(i);
							
							TZ_XXXKXZ_MS=comboBoxMap.get("TZ_XXXKXZ_MS")==null?null:comboBoxMap.get("TZ_XXXKXZ_MS").toString();
							TZ_XXXKXZ_MC=comboBoxMap.get("TZ_XXXKXZ_MC")==null?null:comboBoxMap.get("TZ_XXXKXZ_MC").toString();
							
							System.out.println("===下拉框TZ_XXXKXZ_MS："+TZ_XXXKXZ_MS+"==TZ_XXXKXZ_MC:"+TZ_XXXKXZ_MC);
							final String SQL1="select count(*) from PS_TZ_DC_CC_T WHERE TZ_APP_INS_ID in (select  TZ_APP_INS_ID from PS_TZ_DC_INS_T where TZ_DC_WJ_ID=?) and TZ_XXX_BH=? and TZ_APP_S_TEXT !=' '";
							count=jdbcTemplate.queryForObject(SQL1, new Object[]{surveyID, TZ_XXX_BH},"int");
							final String SQL2="select count(*) from PS_TZ_DC_CC_T WHERE TZ_APP_INS_ID in (select  TZ_APP_INS_ID from PS_TZ_DC_INS_T where TZ_DC_WJ_ID=?) and TZ_XXX_BH=?  and TZ_APP_S_TEXT=?";
							countY=jdbcTemplate.queryForObject(SQL2, new Object[]{surveyID, TZ_XXX_BH,TZ_XXXKXZ_MC} ,"int");
							System.out.println("==count:"+count+"==countY:"+countY);
							if(count!=0)
								tempCount=Double.valueOf(decimalFormat.format(((double)countY/(double)count*100)));
							
							else
								tempCount=0;
							try {
								strComboBoxHtml=strComboBoxHtml+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_MULCHOICE_HTML", TZ_XXXKXZ_MS,tempCount+"%",String.valueOf(countY));
							} catch (TzSystemException e) {
								strComboBoxHtml="";
								e.printStackTrace();
							}
						}
						try {
							strComboBoxHtml=tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_DIV_HTML4", strComboBoxHtml);
						} catch (TzSystemException e) {
							strComboBoxHtml="";
							e.printStackTrace();
						}
						try {
							strDivHtml=strDivHtml+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_DIV_HTML3", TZ_XXX_QID,TZ_TITLE,strComboBoxHtml);
						} catch (TzSystemException e) {
							strDivHtml="";
							e.printStackTrace();
						}
					}
				}
				// 量表题
				if(TZ_COM_LMC!=null&&TZ_COM_LMC.equals("QuantifyQu")){
					final String quantifyQuSQL="select TZ_XXXKXZ_MS,TZ_XXXKXZ_QZ,TZ_XXXKXZ_MC from PS_TZ_DCWJ_XXKXZ_T where TZ_DC_WJ_ID=? and  TZ_XXX_BH=? order by TZ_ORDER";
					
					List<Map<String,Object>>quantifyQuDataList=new ArrayList<Map<String,Object>>();
					quantifyQuDataList=jdbcTemplate.queryForList(quantifyQuSQL, new Object[]{surveyID, TZ_XXX_BH});
					score=0;
					strQuantifyQuHtml="";
					if(quantifyQuDataList!=null){
						for(int i=0;i<quantifyQuDataList.size();i++){
							Map<String,Object>quantifyQuMap=new HashMap<String,Object>();
							quantifyQuMap=quantifyQuDataList.get(i);
							
							TZ_XXXKXZ_MS=quantifyQuMap.get("TZ_XXXKXZ_MS")==null?null:quantifyQuMap.get("TZ_XXXKXZ_MS").toString();
							TZ_XXXKXZ_QZ=quantifyQuMap.get("TZ_XXXKXZ_QZ")==null?null:quantifyQuMap.get("TZ_XXXKXZ_QZ").toString();
							TZ_XXXKXZ_MC=quantifyQuMap.get("TZ_XXXKXZ_MC")==null?null:quantifyQuMap.get("TZ_XXXKXZ_MC").toString();
							
							final String SQL1="select count(*) from PS_TZ_DC_CC_T WHERE TZ_APP_INS_ID in (select  TZ_APP_INS_ID from PS_TZ_DC_INS_T where TZ_DC_WJ_ID=?) and TZ_XXX_BH=? and TZ_APP_S_TEXT!=' '";
							count=jdbcTemplate.queryForObject(SQL1, new Object[]{surveyID, TZ_XXX_BH},"int");
							final String SQL2="select count(*) from PS_TZ_DC_CC_T WHERE TZ_APP_INS_ID in (select  TZ_APP_INS_ID from PS_TZ_DC_INS_T where TZ_DC_WJ_ID=?) and TZ_XXX_BH=? and TZ_APP_S_TEXT=?";
							countY=jdbcTemplate.queryForObject(SQL2, new Object[]{surveyID, TZ_XXX_BH,TZ_XXXKXZ_MC},"int");
							
							if(count!=0){
								tempCount=Double.valueOf(decimalFormat.format((double)countY/(double)count*100));
								score = score + countY / count * Double.valueOf(TZ_XXXKXZ_QZ);
							}
							else{
								tempCount=0;
								score=0;
							}
				            try {
								strQuantifyQuHtml = strQuantifyQuHtml+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_TR_HTML", TZ_XXXKXZ_MS,tempCount+"%",TZ_XXXKXZ_QZ+"分",String.valueOf(countY));
							} catch (TzSystemException e) {
								strQuantifyQuHtml="";
								e.printStackTrace();
							} 
						}
						try {
							strQuantifyQuHtml=tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_DIV_HTML4", strQuantifyQuHtml);
						} catch (TzSystemException e) {
							strQuantifyQuHtml="";
							e.printStackTrace();
						}
						try {
							strDivHtml=strDivHtml+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_ANSQU_HTML", TZ_XXX_QID,TZ_TITLE,score+"分",strQuantifyQuHtml);
						} catch (TzSystemException e) {
							strDivHtml="";
							e.printStackTrace();
						}
					}
				}
				//填空题,数字填空题,日期
				if(TZ_COM_LMC!=null&&(TZ_COM_LMC.equals("Completion")||TZ_COM_LMC.equals("DigitalCompletion")||TZ_COM_LMC.equals("DateBox"))){
					final String completionSQL="select distinct TZ_APP_S_TEXT from PS_TZ_DC_CC_T WHERE TZ_APP_INS_ID in (select  TZ_APP_INS_ID from PS_TZ_DC_INS_T where TZ_DC_WJ_ID=?) and TZ_XXX_BH=?";
					List<Map<String,Object>>completionDataList=new ArrayList<Map<String,Object>>();
					completionDataList=jdbcTemplate.queryForList(completionSQL, new Object[]{surveyID, TZ_XXX_BH});
					
					if(completionDataList!=null){
						String strComHtml="";
						for(int i=0;i<completionDataList.size();i++){
							Map<String,Object>completionMap=new HashMap<String,Object>();
							completionMap=completionDataList.get(i);
							
							TZ_APP_S_TEXT=completionMap.get("TZ_APP_S_TEXT")==null?null:completionMap.get("TZ_APP_S_TEXT").toString();
							if(!TZ_APP_S_TEXT.equals("")){
					               try {
									strComHtml = strComHtml+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_COM_HTML", TZ_APP_S_TEXT);
								} catch (TzSystemException e) {
									strComHtml="";
									e.printStackTrace();
								}
							}
						}
						 	try {
								strComHtml = tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_DIV_HTML4", strComHtml);
							} catch (TzSystemException e) {
								strComHtml="";
								e.printStackTrace();
							}
						 	try {
								strDivHtml=strDivHtml+ tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_DIV_HTML3", TZ_XXX_QID,TZ_TITLE,strComHtml);
							} catch (TzSystemException e) {
								strDivHtml="";
								e.printStackTrace();
							}
					}
				}
				// 问答题
				if(TZ_COM_LMC!=null&&TZ_COM_LMC.equals("EssayQu")){
					final String essayQuSQL="select TZ_APP_L_TEXT from PS_TZ_DC_CC_T WHERE TZ_APP_INS_ID in (select  TZ_APP_INS_ID from PS_TZ_DC_INS_T where TZ_DC_WJ_ID=?) and TZ_XXX_BH=?";
					String strEassyHtml="";
					
					List<Map<String,Object>>essayQuArraylist=new ArrayList<Map<String,Object>>();
					essayQuArraylist=jdbcTemplate.queryForList(essayQuSQL, new Object[]{surveyID, TZ_XXX_BH});
				
					if(essayQuArraylist!=null){
						for(int i=0;i<essayQuArraylist.size();i++){
							Map<String, Object> essayQuMap = new HashMap<String,Object>();
							essayQuMap=essayQuArraylist.get(i);
							
							TZ_APP_L_TEXT=essayQuMap.get("TZ_APP_L_TEXT")==null?null:essayQuMap.get("TZ_APP_L_TEXT").toString();
							if(!TZ_APP_L_TEXT.equals("")){
					               try {
									strEassyHtml = strEassyHtml+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_EASSYQU_HTML", TZ_APP_L_TEXT);
								} catch (TzSystemException e) {
									strEassyHtml="";
									e.printStackTrace();
								} 
							}
						}
						try {
							strEassyHtml =tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_DIV_HTML4", strEassyHtml);
						} catch (TzSystemException e) {
							strEassyHtml="";
							e.printStackTrace();
						}
						try {
							strDivHtml=strDivHtml+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_DIV_HTML3",TZ_XXX_QID,TZ_TITLE,strEassyHtml);
						} catch (TzSystemException e) {
							strDivHtml="";
							e.printStackTrace();
						} 
					}
				}
				//多行文本框
				if(TZ_COM_LMC!=null&&TZ_COM_LMC.equals("MultipleTextBox")){
					final String multipleTextSQL="select  TZ_APP_INS_ID from PS_TZ_DC_INS_T where TZ_DC_WJ_ID=?";
					List<Map<String,Object>>multipleTextDataList=new ArrayList<Map<String,Object>>();
					multipleTextDataList=jdbcTemplate.queryForList(multipleTextSQL, new Object[]{surveyID});
					
					String strMulTextBox=null;
					String strMulTextBox2=null;
					if(multipleTextDataList!=null){
						for(int i=0;i<multipleTextDataList.size();i++){
							Map<String,Object>multipleTextMap=new HashMap<String,Object>();
							multipleTextMap=multipleTextDataList.get(i);
							
							TZ_APP_INS_ID=multipleTextMap.get("TZ_APP_INS_ID")==null?null:multipleTextMap.get("TZ_APP_INS_ID").toString();
							
							final String mulTextBoxSQL="select TZ_APP_S_TEXT,TZ_KXX_QTZ from PS_TZ_DC_DHCC_T WHERE TZ_APP_INS_ID=? and TZ_XXX_BH=?";
							//String strMulTextBox, strMulTextBox2;
							List<Map<String,Object>>mulTextBoxDataList=new ArrayList<Map<String,Object>>();
							mulTextBoxDataList=jdbcTemplate.queryForList(mulTextBoxSQL,new Object[]{TZ_APP_INS_ID, TZ_XXX_BH});
							if(mulTextBoxDataList!=null){
								for(int j=0;j<mulTextBoxDataList.size();j++){
									Map<String,Object> mulTextBoxMap=new HashMap<String,Object>();
									mulTextBoxMap=mulTextBoxDataList.get(j);
									
									TZ_APP_S_TEXT=mulTextBoxMap.get("TZ_APP_S_TEXT")==null?null:mulTextBoxMap.get("TZ_APP_S_TEXT").toString();
									TZ_KXX_QTZ=mulTextBoxMap.get("TZ_KXX_QTZ")==null?null:mulTextBoxMap.get("TZ_KXX_QTZ").toString();
									
									if(TZ_KXX_QTZ!=null){
										 strMulTextBox = strMulTextBox+ "[" +TZ_APP_S_TEXT+ "    " + TZ_KXX_QTZ+ "]";
									}
								}
							}
							/*某一个实例的全部结果遍历结束*/
							if(strMulTextBox!=null){
								try {
									strMulTextBox2=strMulTextBox2+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_COM_HTML", strMulTextBox);
								} catch (TzSystemException e) {
									strMulTextBox2="";
									e.printStackTrace();
								}
								strMulTextBox="";
							}
						}
					}
					try {
						strMulTextBox2=tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_DIV_HTML4", strMulTextBox2);
					} catch (TzSystemException e) {
						strMulTextBox2="";
						e.printStackTrace();
					}
					try {
						strDivHtml=strDivHtml+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_DIV_HTML3", TZ_XXX_QID,TZ_TITLE, strMulTextBox2);
					} catch (TzSystemException e) {
						strDivHtml="";
						e.printStackTrace();
					}
				}
				
				//表格单选题,表格多选题
				if(TZ_COM_LMC!=null&&(TZ_COM_LMC.equals("GridSingleChoice")||TZ_COM_LMC.equals("GridMultipleChoice"))){
					  
					//rem 获得子问题;
					final String gridSinChoiceSQL="select TZ_QU_CODE,TZ_QU_NAME from PS_TZ_DCWJ_BGZWT_T where TZ_DC_WJ_ID=? and TZ_XXX_BH=? order by TZ_ORDER";
					String strMultiBox2="";
					List<Map<String,Object>>gridSinChoiceDataList=new ArrayList<Map<String,Object>>();
					gridSinChoiceDataList=jdbcTemplate.queryForList(gridSinChoiceSQL, new Object[]{surveyID, TZ_XXX_BH});
					for(int i=0;i<gridSinChoiceDataList.size();i++){
			            //rem 子问题下面的所有票数;
						Map<String,Object>gridSinChoiceMap=new HashMap<String,Object>();
						gridSinChoiceMap=gridSinChoiceDataList.get(i);
						
						TZ_QU_CODE=gridSinChoiceMap.get("TZ_QU_CODE")==null?null:gridSinChoiceMap.get("TZ_QU_CODE").toString();
						TZ_QU_NAME=gridSinChoiceMap.get("TZ_QU_NAME")==null?null:gridSinChoiceMap.get("TZ_QU_NAME").toString();
					
						final String SQL1="select count(*) from PS_TZ_DCDJ_BGT_T where  TZ_APP_INS_ID in (select  TZ_APP_INS_ID from PS_TZ_DC_INS_T where TZ_DC_WJ_ID=?) and  TZ_XXX_BH=? and TZ_XXXKXZ_WTMC=?";
						count=jdbcTemplate.queryForObject(SQL1, new Object[]{surveyID, TZ_XXX_BH, TZ_QU_CODE}, "int");
						
						//获得每一个问题下面的选项;
						final String gridChoiceXxSQL="select TZ_OPT_CODE,TZ_OPT_NAME from PS_TZ_DCWJ_BGKXZ_T where TZ_DC_WJ_ID=? and TZ_XXX_BH=?";
						List<Map<String,Object>>gridChoiceXxList=new ArrayList<Map<String,Object>>();
						gridChoiceXxList=jdbcTemplate.queryForList(gridChoiceXxSQL, new String[]{surveyID,TZ_XXX_BH});
						String strMultiBox="";
						for(int j=0;j<gridChoiceXxList.size();j++){
							Map<String,Object>gridChoiceXxMap=new HashMap<String,Object>();
							gridChoiceXxMap=gridChoiceXxList.get(j);
							TZ_OPT_CODE=gridChoiceXxMap.get("TZ_OPT_CODE")==null?null:gridChoiceXxMap.get("TZ_OPT_CODE").toString();
							TZ_OPT_NAME=gridChoiceXxMap.get("TZ_OPT_NAME")==null?null:gridChoiceXxMap.get("TZ_OPT_NAME").toString();
							
							final String SQL2="select count(*) from PS_TZ_DCDJ_BGT_T where  TZ_APP_INS_ID in (select  TZ_APP_INS_ID from PS_TZ_DC_INS_T where TZ_DC_WJ_ID=?) and  TZ_XXX_BH=? and TZ_XXXKXZ_WTMC=? and TZ_XXXKXZ_XXMC=?";
							countY=jdbcTemplate.queryForObject(SQL2, new Object[]{surveyID, TZ_XXX_BH, TZ_QU_CODE, TZ_OPT_CODE,}, "int");
							System.out.println("==表格单选题,表格多选题count:"+count+"==countY:"+countY);
							if(count!=0&&countY!=0)
								score=Double.valueOf(decimalFormat.format((double)countY/(double)count*100));
							else
								score=0;
							try {
								strMultiBox = strMultiBox+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_MULCHOICE_HTML", TZ_OPT_NAME,score+"%",String.valueOf(countY));
							} catch (TzSystemException e) {
								strMultiBox="";
								e.printStackTrace();
							}
						}
						try {
							strMultiBox2 = strMultiBox2 + tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_DIV_HTML2", TZ_QU_NAME, strMultiBox);
						} catch (TzSystemException e) {
							strMultiBox2="";
							e.printStackTrace();
						}
					}
			         try {
						strDivHtml = strDivHtml+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_DIV_HTML3", TZ_XXX_QID, TZ_TITLE, strMultiBox2);
					} catch (TzSystemException e) {
						strDivHtml="";
						e.printStackTrace();
					}
				}
			     //课程评估题CourseAssessment
				if(TZ_COM_LMC!=null&&(TZ_COM_LMC.equals("CourseAssessment"))){
	
				final String courseAssSQL="select TZ_QU_CODE,TZ_QU_NAME from PS_TZ_DCWJ_BGZWT_T where TZ_DC_WJ_ID=? and TZ_XXX_BH=? order by TZ_ORDER";
				String strCourseAss2="";
				double courScore=0;
				List<Map<String,Object>>courseAssDataList=new ArrayList<Map<String,Object>>();
				courseAssDataList=jdbcTemplate.queryForList(courseAssSQL, new Object[]{surveyID, TZ_XXX_BH});
				
				if(courseAssDataList!=null){
					for(int i=0;i<courseAssDataList.size();i++){
						Map<String,Object>courseAssMap=new HashMap<String,Object>();
						courseAssMap=courseAssDataList.get(i);
						
						TZ_QU_NAME=courseAssMap.get("TZ_QU_NAME")==null?null:courseAssMap.get("TZ_QU_NAME").toString();
						TZ_QU_CODE=courseAssMap.get("TZ_QU_CODE")==null?null:courseAssMap.get("TZ_QU_CODE").toString();
						//子问题下面的所有票数;
						final String SQL1="select count(*) from PS_TZ_DCDJ_BGT_T where  TZ_APP_INS_ID in (select  TZ_APP_INS_ID from PS_TZ_DC_INS_T where TZ_DC_WJ_ID=?) and  TZ_XXX_BH=? and TZ_XXXKXZ_WTMC=?";
						count=jdbcTemplate.queryForObject(SQL1, new Object[]{surveyID, TZ_XXX_BH, TZ_QU_CODE}, "int");

						//获得每一个问题下面的选项
						final String gridChoiceXxSQL2="select TZ_OPT_CODE,TZ_OPT_NAME from PS_TZ_DCWJ_BGKXZ_T where TZ_DC_WJ_ID=? and TZ_XXX_BH=? order by TZ_OPT_CODE+0";
						List<Map<String,Object>>gridChoiceXxDataList=new ArrayList<Map<String,Object>>();
						gridChoiceXxDataList=jdbcTemplate.queryForList(gridChoiceXxSQL2, new Object[]{surveyID, TZ_XXX_BH});
						if(gridChoiceXxDataList!=null){
							String strCourseAss = "";
							for(int j=0;j<gridChoiceXxDataList.size();j++){
								Map<String,Object>gridChoiceXxMap=new HashMap<String,Object>();
								gridChoiceXxMap=gridChoiceXxDataList.get(j);
								TZ_OPT_NAME=gridChoiceXxMap.get("TZ_OPT_NAME")==null?null:gridChoiceXxMap.get("TZ_OPT_NAME").toString();
								TZ_OPT_CODE=gridChoiceXxMap.get("TZ_OPT_CODE")==null?null:gridChoiceXxMap.get("TZ_OPT_CODE").toString();
								
								final String SQL2="select count(*) from PS_TZ_DCDJ_BGT_T where  TZ_APP_INS_ID in (select  TZ_APP_INS_ID from PS_TZ_DC_INS_T where TZ_DC_WJ_ID=?) and  TZ_XXX_BH=? and TZ_XXXKXZ_WTMC=? and TZ_XXXKXZ_XXMC=?";
					            countY=jdbcTemplate.queryForObject(SQL2, new Object[]{surveyID, TZ_XXX_BH, TZ_QU_CODE, TZ_OPT_CODE},"int");  
					            if(count!=0&&countY!=0)
									score=Double.valueOf(decimalFormat.format((double)countY/(double)count*100));
								else
									score=0;
					            
					            try {
									strCourseAss=strCourseAss+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_MULCHOICE_HTML", TZ_OPT_NAME, score + "%", String.valueOf(countY));
								} catch (TzSystemException e) {
									strCourseAss="";
									e.printStackTrace();
								}
					            courScore = courScore +Integer.valueOf( TZ_OPT_CODE) * score / 100;
							}
							try {
								strCourseAss2 = strCourseAss2 +tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_KC_SUR_DIV_HTML2", strCourseAss,String.valueOf(courScore));
							} catch (TzSystemException e) {
								strCourseAss2="";
								e.printStackTrace();
							}
						}
						//非整数取两位小数
				
					}
			         try {
						strDivHtml = strDivHtml +tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_DIV_HTML3", TZ_XXX_QID, TZ_TITLE, strCourseAss2);
					} catch (TzSystemException e) {
						strDivHtml="";
						e.printStackTrace();
					}
				}
				}
				  //附件上传,图片上传
				if(TZ_COM_LMC!=null&&(TZ_COM_LMC.equals("AttachUpload")||TZ_COM_LMC.equals("ImgUpload"))){
					final String attachUploadSQL="select  TZ_APP_INS_ID from PS_TZ_DC_INS_T where TZ_DC_WJ_ID=?";
					List<Map<String,Object>>attachUploadDataList=new ArrayList<Map<String,Object>>();
					attachUploadDataList=jdbcTemplate.queryForList(attachUploadSQL, new Object[]{surveyID});
					String strAttach="";
					String strAttach2="";
					if(attachUploadDataList!=null){
						for(int i=0;i<attachUploadDataList.size();i++){
							Map<String,Object>attachUploadMap=new HashMap<String,Object>();
							attachUploadMap=attachUploadDataList.get(i);
							TZ_APP_INS_ID=attachUploadMap.get("TZ_APP_INS_ID")==null?null:attachUploadMap.get("TZ_APP_INS_ID").toString();
							
						     
							final String attachSQL="select ATTACHSYSFILENAME,ATTACHUSERFILE,TZ_INDEX,ROW_ADDED_DTTM from PS_TZ_DC_WJATT_T WHERE TZ_APP_INS_ID =? and TZ_XXX_BH=?";
							List<Map<String,Object>>attachDataList=new ArrayList<Map<String,Object>>();
							attachDataList=jdbcTemplate.queryForList(attachSQL, new Object[]{ TZ_APP_INS_ID, TZ_XXX_BH});
							if(attachDataList!=null){
								for(int j=0;j<attachDataList.size();j++){
									Map<String,Object>attachMap=attachDataList.get(j);
									
									ATTACHSYSFILENAME=attachMap.get("ATTACHSYSFILENAME")==null?null:attachMap.get("ATTACHSYSFILENAME").toString();
									ATTACHUSERFILE=attachMap.get("ATTACHUSERFILE")==null?null:attachMap.get("ATTACHUSERFILE").toString();
									TZ_INDEX=attachMap.get("TZ_INDEX")==null?null:attachMap.get("TZ_INDEX").toString();
									//ROW_ADDED_DTTM=attachMap.get("ROW_ADDED_DTTM")==null?null:attachMap.get("ROW_ADDED_DTTM").toString();
									
									if(ATTACHUSERFILE!=null){
										//附件路径 上传路径待定
										 String fileUrl = jdbcTemplate.queryForObject(
													"select TZ_ATT_A_URL from PS_TZ_DC_WJATTCH_T WHERE TZ_ATTACHSYSFILENA=?", new Object[]{ATTACHSYSFILENAME},"String");
										if(!fileUrl.endsWith("/")){
											fileUrl+="/";
										}
										//日期字符串
										if(request.getServerPort()==80){
											fileUrl="Http://"+request.getServerName()+fileUrl+"/"+ATTACHSYSFILENAME;
										}
										else{
											fileUrl="Http://"+request.getServerName()+":"+request.getServerPort()+fileUrl+"/"+ATTACHSYSFILENAME;
										}
										try {
											strAttach=strAttach+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_ATT_HTML", "["+ATTACHUSERFILE+"]",fileUrl);
										} catch (TzSystemException e) {
											e.printStackTrace();
										}
									}
								}
						           /*某一个实例的全部结果遍历结束*/
								if(strAttach!=null&&!strAttach.equals("")){
									try {
										strAttach2=strAttach2+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_COM_HTML", strAttach);
									} catch (TzSystemException e) {
										e.printStackTrace();
									}
									strAttach="";
								}
							}
						}
						try {
							strAttach2=tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_DIV_HTML4", strAttach2);
						} catch (TzSystemException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							strDivHtml=strDivHtml+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_DIV_HTML3", TZ_XXX_QID,TZ_TITLE,strAttach2);
						} catch (TzSystemException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						strAttach2="";
					}
				}
				
			
//---
			}
		}
		   String strTitle=jdbcTemplate.queryForObject("select TZ_DC_WJBT from PS_TZ_DC_WJ_DY_T where TZ_DC_WJ_ID=?", new Object[]{surveyID}, "String");
		   System.out.println("==strDivHtml=="+strDivHtml);
		   System.out.println("==strTitle=="+strTitle);
		   try {
			strReturn = tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_SURVEY_ANS_HTML", request.getContextPath(),strDivHtml, strTitle);
		} catch (TzSystemException e) {
			strReturn="";
			e.printStackTrace();
		}
		   return strReturn;
		   
	}
	
}
