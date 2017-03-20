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
		try{
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
		String TZ_XXX_BH,  TZ_COM_LMC, TZ_XXXKXZ_MS, TZ_XXXKXZ_QZ, TZ_XXXKXZ_MC, TZ_APP_S_TEXT, TZ_APP_L_TEXT, TZ_KXX_QTZ, TZ_APP_INS_ID, TZ_QU_CODE, TZ_QU_NAME, TZ_OPT_CODE, TZ_OPT_NAME, ATTACHSYSFILENAME, TZ_INDEX, ATTACHUSERFILE, ROW_ADDED_DTTM;
		String strMulChoiceHtml, strMulChoiceTrHtml, strComboBoxHtml, strComTempHtml, strRadioBoxHtml,strRadioBoxHtml2, strQuantifyQuHtml;
		//是否计算分值字段TZ_IS_AVG
		String TZ_IS_AVG="N";
		String TZ_XXX_QID="";
		String TZ_TITLE="";
		//保存平均分
		double avgScore=0;
		int countY, countN,count , score1, tempCountN;
		double score;
		double tempCount;
		//用来保留小数2位位数
		DecimalFormat decimalFormat = new DecimalFormat("#.00");
		//TZ_XXX_QID?
		//所有循环的索引都用index,i,j
		final String dcwjXxxPzSQL="select TZ_XXX_BH,TZ_TITLE,TZ_XXX_QID,TZ_COM_LMC,TZ_IS_AVG  from PS_TZ_DCWJ_XXXPZ_T where TZ_DC_WJ_ID=? and  TZ_COM_LMC not in ('PageNav') order by TZ_ORDER";
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
				
				TZ_IS_AVG=dcwjXxxPzMap.get("TZ_IS_AVG")==null?"N":dcwjXxxPzMap.get("TZ_IS_AVG").toString();
				
				//单选题
				if(TZ_COM_LMC!=null&&TZ_COM_LMC.equals("RadioBox")){
				//平均分清零
				avgScore=0;
				strRadioBoxHtml="";
				strRadioBoxHtml2="";
				
				final String radioBoxSQL="select TZ_XXXKXZ_MS,TZ_XXXKXZ_MC,TZ_XXXKXZ_QZ from PS_TZ_DCWJ_XXKXZ_T where TZ_DC_WJ_ID=? and  TZ_XXX_BH=? order by TZ_ORDER";
				List<Map<String,Object>>radioBoxDataList=new ArrayList<Map<String,Object>>();
				radioBoxDataList=jdbcTemplate.queryForList(radioBoxSQL, new Object[]{surveyID,TZ_XXX_BH});
				
				if(radioBoxDataList!=null){
					for(int i=0;i<radioBoxDataList.size();i++){
						Map<String,Object>radioBoxMap=new HashMap<String,Object>();
						radioBoxMap=radioBoxDataList.get(i);
						//单选题 可选值描述(這里实际是选项名称)
						TZ_XXXKXZ_MS=radioBoxMap.get("TZ_XXXKXZ_MS")==null?null:radioBoxMap.get("TZ_XXXKXZ_MS").toString();
						//单选题 可选值名称(这里实际是选项题号)
						TZ_XXXKXZ_MC=radioBoxMap.get("TZ_XXXKXZ_MC")==null?null:radioBoxMap.get("TZ_XXXKXZ_MC").toString();
								
						final String SQL1="select count(*) from PS_TZ_DC_DHCC_T WHERE TZ_APP_INS_ID in (select  TZ_APP_INS_ID from PS_TZ_DC_INS_T where TZ_DC_WJ_ID=?) and TZ_XXX_BH=?";
						count=jdbcTemplate.queryForObject(SQL1, new Object[]{surveyID,TZ_XXX_BH},"int");
						final String SQL2="select count(*) from PS_TZ_DC_DHCC_T WHERE TZ_APP_INS_ID in (select  TZ_APP_INS_ID from PS_TZ_DC_INS_T where TZ_DC_WJ_ID=?) and TZ_XXX_BH=? and TZ_XXXKXZ_MC=?";
						countY=jdbcTemplate.queryForObject(SQL2, new Object[]{surveyID,TZ_XXX_BH,TZ_XXXKXZ_MC},"int");
						//单选题分值:TZ_XXXKXZ_QZ 数据库中的数据类型为demical
						TZ_XXXKXZ_QZ=radioBoxMap.get("TZ_XXXKXZ_QZ")==null?"0":radioBoxMap.get("TZ_XXXKXZ_QZ").toString();
						//单选题总分
						System.out.println("单选题分值："+TZ_XXXKXZ_QZ);
						//如果投票的人数大于0,则计算投票人数占总参与人数的百分比，和投票平均得分
						if(count>0){
							//投票百分比
							tempCount=Double.valueOf(decimalFormat.format((double)countY/(double)count*100));
							//单选题平均得分
							avgScore=Double.valueOf(decimalFormat.format(avgScore+(double)countY/(double)count*Double.valueOf(TZ_XXXKXZ_QZ)));//decimalFormat用于取2位小数，乘除法运算实用
						}
						else{
							tempCount=0;
							avgScore=avgScore+0;
						}
						//TZ_IS_AVG控制是否显示分值,'Y'则显示
						if(TZ_IS_AVG.equals("Y"))
						{
							//strRadioBoxHtml 记录{选项名称+分数,投票数}用于饼状图显示数据
							if(!strRadioBoxHtml.equals("")){
								strRadioBoxHtml=strRadioBoxHtml+","+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUB_TB2_HTML", TZ_XXXKXZ_MS+"("+TZ_XXXKXZ_QZ+"分)",String.valueOf(countY));
							}else{
								strRadioBoxHtml=tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUB_TB2_HTML", TZ_XXXKXZ_MS+"("+TZ_XXXKXZ_QZ+"分)",String.valueOf(countY));
							}
							//strRadioBoxHtml2 记录选项名称+分数  投票数 百分比   ->用于界面中表格统计数据
							strRadioBoxHtml2=strRadioBoxHtml2+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUB_TB3_HTML",  TZ_XXXKXZ_MS+"("+TZ_XXXKXZ_QZ+"分)",String.valueOf(countY),tempCount+"%");
						}
						else{
							if(!strRadioBoxHtml.equals("")){
								strRadioBoxHtml=strRadioBoxHtml+","+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUB_TB2_HTML", TZ_XXXKXZ_MS,String.valueOf(countY));
							}else{
								strRadioBoxHtml=tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUB_TB2_HTML", TZ_XXXKXZ_MS,String.valueOf(countY));
							}
								strRadioBoxHtml2=strRadioBoxHtml2+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUB_TB3_HTML",  TZ_XXXKXZ_MS,String.valueOf(countY),tempCount+"%");
						}
						System.out.println("===单选题====strRadioBoxHtml:"+strRadioBoxHtml);
						System.out.println("===单选题====strRadioBoxHtml:"+strRadioBoxHtml2);
					}
						//拼最终统计 单选题结果的Html
						if(TZ_IS_AVG.equals("Y")){
							strDivHtml=strDivHtml+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_TB_HTML",  TZ_XXX_QID+":" +TZ_TITLE,"单选题",avgScore+"分",TZ_XXX_BH,strRadioBoxHtml,strRadioBoxHtml2);
						}else{
							strDivHtml=strDivHtml+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_TB_HTML",  TZ_XXX_QID+":" +TZ_TITLE,"单选题","",TZ_XXX_BH,strRadioBoxHtml,strRadioBoxHtml2);
						}
						//strRadioBoxHtml,strRadioBoxHtml2变量通用于所有控件 每次用完要进行初始化
						strRadioBoxHtml="";
						strRadioBoxHtml2="";
					}
				System.out.println("单选题终strDivHtml=="+strDivHtml);
				}
				//单选量表题
				if(TZ_COM_LMC!=null&&TZ_COM_LMC.equals("RadioBoxQu")){
					avgScore=0;
					strRadioBoxHtml="";
					strRadioBoxHtml2="";
					final String radioBoxQuSQL="select TZ_XXXKXZ_MS,TZ_XXXKXZ_QZ,TZ_XXXKXZ_MC from PS_TZ_DCWJ_XXKXZ_T where TZ_DC_WJ_ID=? and  TZ_XXX_BH=? order by TZ_ORDER";
					List<Map<String,Object>>radioBoxDataList=new ArrayList<Map<String,Object>>();
					radioBoxDataList=jdbcTemplate.queryForList(radioBoxQuSQL, new Object[]{surveyID, TZ_XXX_BH});
					if(radioBoxDataList!=null){
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
							
							if(count>0){
								//百分比
								tempCount=Double.valueOf(decimalFormat.format((double)countY/(double)count*100));
								//平均分
								avgScore = Double.valueOf(decimalFormat.format(avgScore + (double)countY /(double)count * Double.valueOf(TZ_XXXKXZ_QZ)));
							}
							else{
								tempCount=0;
								avgScore=avgScore+0;
							}
							//TZ_IS_AVG控制是否显示分值,'Y'则显示
							if(TZ_IS_AVG.equals("Y"))
							{
								//strRadioBoxHtml 记录{选项名称+分数,投票数}用于饼状图显示数据
								if(!strRadioBoxHtml.equals("")){
									strRadioBoxHtml=strRadioBoxHtml+","+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUB_TB2_HTML", TZ_XXXKXZ_MS+"("+TZ_XXXKXZ_QZ+"分)",String.valueOf(countY));
								}else{
									strRadioBoxHtml=tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUB_TB2_HTML", TZ_XXXKXZ_MS+"("+TZ_XXXKXZ_QZ+"分)",String.valueOf(countY));
								}
								//strRadioBoxHtml2 记录选项名称+分数  投票数 百分比   ->用于界面中表格统计数据
								strRadioBoxHtml2=strRadioBoxHtml2+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUB_TB3_HTML",  TZ_XXXKXZ_MS+"("+TZ_XXXKXZ_QZ+"分)",String.valueOf(countY),tempCount+"%");
							}
							else{
								if(!strRadioBoxHtml.equals("")){
									strRadioBoxHtml=strRadioBoxHtml+","+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUB_TB2_HTML", TZ_XXXKXZ_MS,String.valueOf(countY));
								}else{
									strRadioBoxHtml=tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUB_TB2_HTML", TZ_XXXKXZ_MS,String.valueOf(countY));
								}
								strRadioBoxHtml2=strRadioBoxHtml2+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUB_TB3_HTML",  TZ_XXXKXZ_MS,String.valueOf(countY),tempCount+"%");
							}
						}
							//拼最终统计 单选题结果的Html
							if(TZ_IS_AVG.equals("Y")){
								strDivHtml=strDivHtml+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_TB_HTML",  TZ_XXX_QID+":" +TZ_TITLE,"单选量表题",avgScore+"分",TZ_XXX_BH,strRadioBoxHtml,strRadioBoxHtml2);
							}else{
								strDivHtml=strDivHtml+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_TB_HTML",  TZ_XXX_QID+":" +TZ_TITLE,"单选量表题","",TZ_XXX_BH,strRadioBoxHtml,strRadioBoxHtml2);
							}
							//strRadioBoxHtml,strRadioBoxHtml2变量通用于所有控件 每次用完要进行初始化
							strRadioBoxHtml="";
							strRadioBoxHtml2="";
					}
				}
				//多选题
				if(TZ_COM_LMC!=null&&TZ_COM_LMC.equals("MultipleChoice")){
					avgScore=0;
					strRadioBoxHtml="";
					strRadioBoxHtml2="";
					strMulChoiceHtml=""; 
					final String multiChoiceSQL="select TZ_XXXKXZ_MS,TZ_XXXKXZ_MC ,TZ_XXXKXZ_QZ from PS_TZ_DCWJ_XXKXZ_T where TZ_DC_WJ_ID=? and  TZ_XXX_BH=? order by TZ_ORDER";
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
							if(count>0){
								//百分比
								tempCount=Double.valueOf(decimalFormat.format((double)countY/(double)count*100));
							}
							else{
								tempCount=0;
							}
							//量表题不显示分数
							if(!strRadioBoxHtml.equals("")){
								strRadioBoxHtml=strRadioBoxHtml+","+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUB_TB2_HTML", TZ_XXXKXZ_MS,String.valueOf(countY));
							}else{
								strRadioBoxHtml=tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUB_TB2_HTML", TZ_XXXKXZ_MS,String.valueOf(countY));
							}
							strRadioBoxHtml2=strRadioBoxHtml2+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUB_TB3_HTML",  TZ_XXXKXZ_MS,String.valueOf(countY),tempCount+"%");
						}
					}
					//拼最终统计 单选题结果的Html
					strDivHtml=strDivHtml+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_TB_HTML",  TZ_XXX_QID+":" +TZ_TITLE,"多选题","",TZ_XXX_BH,strRadioBoxHtml,strRadioBoxHtml2);
					//strRadioBoxHtml,strRadioBoxHtml2变量通用于所有控件 每次用完要进行初始化
					strRadioBoxHtml="";
					strRadioBoxHtml2="";
				}
				// 下拉框
				if(TZ_COM_LMC!=null&&TZ_COM_LMC.equals("ComboBox")){
					strRadioBoxHtml="";
					strRadioBoxHtml2="";
					final String comboBoxSQL="select TZ_XXXKXZ_MS,TZ_XXXKXZ_MC from PS_TZ_DCWJ_XXKXZ_T where TZ_DC_WJ_ID=? and  TZ_XXX_BH=? order by TZ_ORDER";
					List<Map<String,Object>>comboBoxDataList=new ArrayList<Map<String,Object>>();
					comboBoxDataList=jdbcTemplate.queryForList(comboBoxSQL, new Object[]{surveyID, TZ_XXX_BH});
					
					if(comboBoxDataList!=null){
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
							if(count>0){
								//百分比
								tempCount=Double.valueOf(decimalFormat.format((double)countY/(double)count*100));
							}
							else{
								tempCount=0;
							}
							//下拉框不显示分数
							if(!strRadioBoxHtml.equals("")){
								strRadioBoxHtml=strRadioBoxHtml+","+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUB_TB2_HTML", TZ_XXXKXZ_MS,String.valueOf(countY));
							}else{
								strRadioBoxHtml=tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUB_TB2_HTML", TZ_XXXKXZ_MS,String.valueOf(countY));
							}
							strRadioBoxHtml2=strRadioBoxHtml2+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUB_TB3_HTML",  TZ_XXXKXZ_MS,String.valueOf(countY),tempCount+"%");						
							}
						//拼最终统计 单选题结果的Html
						strDivHtml=strDivHtml+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_TB_HTML",  TZ_XXX_QID+":" +TZ_TITLE,"下拉框","",TZ_XXX_BH,strRadioBoxHtml,strRadioBoxHtml2);
						//strRadioBoxHtml,strRadioBoxHtml2变量通用于所有控件 每次用完要进行初始化
						strRadioBoxHtml="";
						strRadioBoxHtml2="";
					}
				}
				// 量表题
				if(TZ_COM_LMC!=null&&TZ_COM_LMC.equals("QuantifyQu")){
					avgScore=0;
					strRadioBoxHtml="";
					strRadioBoxHtml2="";
					final String quantifyQuSQL="select TZ_XXXKXZ_MS,TZ_XXXKXZ_QZ,TZ_XXXKXZ_MC from PS_TZ_DCWJ_XXKXZ_T where TZ_DC_WJ_ID=? and  TZ_XXX_BH=? order by TZ_ORDER";
					
					List<Map<String,Object>>quantifyQuDataList=new ArrayList<Map<String,Object>>();
					quantifyQuDataList=jdbcTemplate.queryForList(quantifyQuSQL, new Object[]{surveyID, TZ_XXX_BH});
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
							
							if(count>0){
								//投票百分比
								tempCount=Double.valueOf(decimalFormat.format((double)countY/(double)count*100));
								//单选题平均得分
								avgScore=Double.valueOf(decimalFormat.format(avgScore+(double)countY/(double)count*Double.valueOf(TZ_XXXKXZ_QZ)));//decimalFormat用于取2位小数，乘除法运算实用
							}
							else{
								tempCount=0;
								avgScore=avgScore+0;
							}
							//TZ_IS_AVG控制是否显示分值,'Y'则显示
							if(TZ_IS_AVG.equals("Y"))
							{
								if(!strRadioBoxHtml.equals("")){
									//strRadioBoxHtml 记录{选项名称+分数,投票数}用于饼状图显示数据
									strRadioBoxHtml=strRadioBoxHtml+","+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUB_TB2_HTML", TZ_XXXKXZ_MS+"("+TZ_XXXKXZ_QZ+"分)",String.valueOf(countY));
								}else{
									strRadioBoxHtml=tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUB_TB2_HTML", TZ_XXXKXZ_MS+"("+TZ_XXXKXZ_QZ+"分)",String.valueOf(countY));
								}
								//strRadioBoxHtml2 记录选项名称+分数  投票数 百分比   ->用于界面中表格统计数据
								strRadioBoxHtml2=strRadioBoxHtml2+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUB_TB3_HTML",  TZ_XXXKXZ_MS+"("+TZ_XXXKXZ_QZ+"分)",String.valueOf(countY),tempCount+"%");
							}
							else{
								if(!strRadioBoxHtml.equals("")){
									strRadioBoxHtml=strRadioBoxHtml+","+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUB_TB2_HTML", TZ_XXXKXZ_MS,String.valueOf(countY));
								}else{
									strRadioBoxHtml=tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUB_TB2_HTML", TZ_XXXKXZ_MS,String.valueOf(countY));
								}
								strRadioBoxHtml2=strRadioBoxHtml2+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUB_TB3_HTML",  TZ_XXXKXZ_MS,String.valueOf(countY),tempCount+"%");
							}
						}
						//拼最终统计 单选题结果的Html
						if(TZ_IS_AVG.equals("Y")){
							strDivHtml=strDivHtml+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_TB_HTML",  TZ_XXX_QID+":" +TZ_TITLE,"量表题",avgScore+"分",TZ_XXX_BH,strRadioBoxHtml,strRadioBoxHtml2);
						}else{
							strDivHtml=strDivHtml+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_TB_HTML",  TZ_XXX_QID+":" +TZ_TITLE,"量表题","",TZ_XXX_BH,strRadioBoxHtml,strRadioBoxHtml2);
						}
						//strRadioBoxHtml,strRadioBoxHtml2变量通用于所有控件 每次用完要进行初始化
						strRadioBoxHtml="";
						strRadioBoxHtml2="";
					}
				}
				//填空题,数字填空题,日期
				if(TZ_COM_LMC!=null&&(TZ_COM_LMC.equals("Completion")||TZ_COM_LMC.equals("DigitalCompletion")||TZ_COM_LMC.equals("DateBox"))){
					strRadioBoxHtml="";
					strRadioBoxHtml2="";
					//type用于后面合成最终结果html中 显示控件类型
					String type="";
					if(TZ_COM_LMC.equals("Completion")){
						type="填空题";
					}
					else if(TZ_COM_LMC.equals("DigitalCompletion")){
						type="数字填空题";
					}
					else if(TZ_COM_LMC.equals("DateBox")){
						type="日期";
					}
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
					         
							strComHtml = strComHtml+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_COM_HTML", TZ_APP_S_TEXT);
					
							}
						}
						strDivHtml=strDivHtml+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_TXT_HTML", TZ_XXX_QID+ ":"+ TZ_TITLE, type, "", strComHtml);
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
								strEassyHtml = strEassyHtml+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_EASSYQU_HTML", TZ_APP_L_TEXT);
							}
						}
						strDivHtml=strDivHtml+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_TXT_HTML",  TZ_XXX_QID +":"+TZ_TITLE, "问答题", "", strEassyHtml);
					}
				}
				//多行文本框
				if(TZ_COM_LMC!=null&&TZ_COM_LMC.equals("MultipleTextBox")){
			        strRadioBoxHtml = "";
			        strRadioBoxHtml2="";
					final String multipleTextSQL="select  TZ_APP_INS_ID from PS_TZ_DC_INS_T where TZ_DC_WJ_ID=?";
					List<Map<String,Object>>multipleTextDataList=new ArrayList<Map<String,Object>>();
					multipleTextDataList=jdbcTemplate.queryForList(multipleTextSQL, new Object[]{surveyID});
					
					String strMulTextBox="";
					String strMulTextBox2="";
					if(multipleTextDataList!=null){
						for(int i=0;i<multipleTextDataList.size();i++){
							Map<String,Object>multipleTextMap=new HashMap<String,Object>();
							multipleTextMap=multipleTextDataList.get(i);
							TZ_APP_INS_ID=multipleTextMap.get("TZ_APP_INS_ID")==null?null:multipleTextMap.get("TZ_APP_INS_ID").toString();
							final String mulTextBoxSQL="select TZ_APP_S_TEXT,TZ_KXX_QTZ from PS_TZ_DC_DHCC_T WHERE TZ_APP_INS_ID=? and TZ_XXX_BH=?";
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
							if(strMulTextBox!=null&&!strMulTextBox.equals("")){
								strMulTextBox2=strMulTextBox2+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_COM_HTML", strMulTextBox);
								strMulTextBox="";
							}
						}
						strDivHtml=strDivHtml+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_TXT_HTML", TZ_XXX_QID + ":" + TZ_TITLE, "多行文本框", "", strMulTextBox2);
					}
				}
				
				//表格单选题,表格多选题,课程评估
				if(TZ_COM_LMC!=null&&(TZ_COM_LMC.equals("GridSingleChoice")||TZ_COM_LMC.equals("GridMultipleChoice"))||TZ_COM_LMC.equals("CourseAssessment")){
			        String type="";
			        if(TZ_COM_LMC.equals("GridSingleChoice")){
			        	type="单选表格题";
			        }
			        else if(TZ_COM_LMC.equals("GridMultipleChoice")){
			        	type="表格多选题";
			        }
			        else if(TZ_COM_LMC.equals("CourseAssessment")){
			        	type="课程评估";
			        }
					//rem 获得子问题;
					final String gridSinChoiceSQL="select TZ_QU_CODE,TZ_QU_NAME  from PS_TZ_DCWJ_BGZWT_T where TZ_DC_WJ_ID=? and TZ_XXX_BH=? order by TZ_ORDER";
					String strMultiBox="";
					List<Map<String,Object>>gridSinChoiceDataList=new ArrayList<Map<String,Object>>();
					gridSinChoiceDataList=jdbcTemplate.queryForList(gridSinChoiceSQL, new Object[]{surveyID, TZ_XXX_BH});
					for(int i=0;i<gridSinChoiceDataList.size();i++){
						avgScore=0;
						strRadioBoxHtml = "";
						strRadioBoxHtml2="";
			            //rem 子问题下面的所有票数;
						Map<String,Object>gridSinChoiceMap=new HashMap<String,Object>();
						gridSinChoiceMap=gridSinChoiceDataList.get(i);
						
						TZ_QU_CODE=gridSinChoiceMap.get("TZ_QU_CODE")==null?null:gridSinChoiceMap.get("TZ_QU_CODE").toString();
						TZ_QU_NAME=gridSinChoiceMap.get("TZ_QU_NAME")==null?null:gridSinChoiceMap.get("TZ_QU_NAME").toString();
					
						final String SQL1="select count(*) from PS_TZ_DCDJ_BGT_T where  TZ_APP_INS_ID in (select  TZ_APP_INS_ID from PS_TZ_DC_INS_T where TZ_DC_WJ_ID=?) and  TZ_XXX_BH=? and TZ_XXXKXZ_WTMC=?";
						count=jdbcTemplate.queryForObject(SQL1, new Object[]{surveyID, TZ_XXX_BH, TZ_QU_CODE}, "int");
						
						//获得每一个问题下面的选项;
						final String gridChoiceXxSQL="select TZ_OPT_CODE,TZ_OPT_NAME,TZ_WEIGHT from PS_TZ_DCWJ_BGKXZ_T where TZ_DC_WJ_ID=? and TZ_XXX_BH=?";
						List<Map<String,Object>>gridChoiceXxList=new ArrayList<Map<String,Object>>();
						gridChoiceXxList=jdbcTemplate.queryForList(gridChoiceXxSQL, new String[]{surveyID,TZ_XXX_BH});
						for(int j=0;j<gridChoiceXxList.size();j++){
							Map<String,Object>gridChoiceXxMap=new HashMap<String,Object>();
							gridChoiceXxMap=gridChoiceXxList.get(j);
							TZ_OPT_CODE=gridChoiceXxMap.get("TZ_OPT_CODE")==null?null:gridChoiceXxMap.get("TZ_OPT_CODE").toString();
							TZ_OPT_NAME=gridChoiceXxMap.get("TZ_OPT_NAME")==null?null:gridChoiceXxMap.get("TZ_OPT_NAME").toString();
							
							//表格题的分值：TZ_WEIGHT 单选题的分值：TZ_WEIGHT 数据类型为：varchar
							TZ_XXXKXZ_QZ=gridChoiceXxMap.get("TZ_WEIGHT")==null?"0":gridChoiceXxMap.get("TZ_WEIGHT").toString();
							final String SQL2="select count(*) from PS_TZ_DCDJ_BGT_T where  TZ_APP_INS_ID in (select  TZ_APP_INS_ID from PS_TZ_DC_INS_T where TZ_DC_WJ_ID=?) and  TZ_XXX_BH=? and TZ_XXXKXZ_WTMC=? and TZ_XXXKXZ_XXMC=?";
							countY=jdbcTemplate.queryForObject(SQL2, new Object[]{surveyID, TZ_XXX_BH, TZ_QU_CODE, TZ_OPT_CODE,}, "int");
							System.out.println("==表格单选题,表格多选题count:"+count+"==countY:"+countY);
							if(count>0&&countY>0){
								//投票百分比
								tempCount=Double.valueOf(decimalFormat.format((double)countY/(double)count*100));
								//单选题平均得分
								avgScore=Double.valueOf(decimalFormat.format(avgScore+(double)countY/(double)count*Double.valueOf(TZ_XXXKXZ_QZ)));//decimalFormat用于取2位小数，乘除法运算实用
							}
							else{
								tempCount=0;
								avgScore=avgScore+0;
							}
							//TZ_IS_AVG控制是否显示分值,'Y'则显示
							if(TZ_IS_AVG.equals("Y"))
							{
								if(!strRadioBoxHtml.equals("")){
									//strRadioBoxHtml 记录{选项名称+分数,投票数}用于饼状图显示数据
									strRadioBoxHtml=strRadioBoxHtml+","+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUB_TB2_HTML", TZ_OPT_NAME+"("+TZ_XXXKXZ_QZ+"分)",String.valueOf(countY));
								}else{
									strRadioBoxHtml=tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUB_TB2_HTML", TZ_OPT_NAME+"("+TZ_XXXKXZ_QZ+"分)",String.valueOf(countY));
								}	//strRadioBoxHtml2 记录选项名称+分数  投票数 百分比   ->用于界面中表格统计数据
									strRadioBoxHtml2=strRadioBoxHtml2+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUB_TB3_HTML",  TZ_OPT_NAME+"("+TZ_XXXKXZ_QZ+"分)",String.valueOf(countY),tempCount+"%");
							}
							else{
								if(!strRadioBoxHtml.equals("")){
									strRadioBoxHtml=strRadioBoxHtml+","+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUB_TB2_HTML", TZ_OPT_NAME,String.valueOf(countY));
								}else{
									strRadioBoxHtml=tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUB_TB2_HTML", TZ_OPT_NAME,String.valueOf(countY));

								}
								strRadioBoxHtml2=strRadioBoxHtml2+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUB_TB3_HTML",  TZ_OPT_NAME,String.valueOf(countY),tempCount+"%");
							}
						}
						if(TZ_IS_AVG.equals("Y")){
							strMultiBox=strMultiBox+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_BGTB2_HTML",TZ_XXX_BH +"_" +TZ_QU_CODE, strRadioBoxHtml, strRadioBoxHtml2, TZ_QU_NAME,avgScore+ "分");
						}
						else{
							strMultiBox=strMultiBox+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_BGTB2_HTML",TZ_XXX_BH +"_" +TZ_QU_CODE, strRadioBoxHtml, strRadioBoxHtml2, TZ_QU_NAME,"");
						}
					}
						strDivHtml=strDivHtml+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_BGTB_HTML",  TZ_XXX_QID +":" +TZ_TITLE, type, strMultiBox);
						strRadioBoxHtml = "";
					    strRadioBoxHtml2="";
				}
				  //附件上传,图片上传
				if(TZ_COM_LMC!=null&&(TZ_COM_LMC.equals("AttachUpload")||TZ_COM_LMC.equals("ImgUpload"))){
					final String attachUploadSQL="select  TZ_APP_INS_ID from PS_TZ_DC_INS_T where TZ_DC_WJ_ID=?";
					List<Map<String,Object>>attachUploadDataList=new ArrayList<Map<String,Object>>();
					attachUploadDataList=jdbcTemplate.queryForList(attachUploadSQL, new Object[]{surveyID});
					String type="";
					if(TZ_COM_LMC.equals("AttachUpload")){
						type="附件上传";
					}
					else if(TZ_COM_LMC.equals("ImgUpload")){
						type="图片上传";
					}
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
											strAttach=strAttach+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_ATT_HTML", "["+ATTACHUSERFILE+"]",fileUrl);
									}
								}
						           /*某一个实例的全部结果遍历结束*/
								if(strAttach!=null&&!strAttach.equals("")){
									strAttach2=strAttach2+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_COM_HTML", strAttach);
									strAttach="";
								}
							}
						}
							//     &strDivHtml = &strDivHtml | GetHTMLText(HTML.TZ_GD_SUR_TXT_HTML, &TZ_XXX_QID | ":" | &TZ_TITLE, &type, "", &strAttach2);
							strDivHtml=strDivHtml+tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_GD_SUR_TXT_HTML",TZ_XXX_QID +":"+TZ_TITLE, type, "", strAttach2);
					}
				}
				
			
				//---
			}
		}
			//整合结果html 
			System.out.println("strDivHtml最终值："+strDivHtml);
		   String strTitle=jdbcTemplate.queryForObject("select TZ_DC_WJBT from PS_TZ_DC_WJ_DY_T where TZ_DC_WJ_ID=?", new Object[]{surveyID}, "String");
		   int totalCount=jdbcTemplate.queryForObject("select count(*) from PS_TZ_DC_INS_T where TZ_DC_WJ_ID=?",new Object[]{surveyID},"int");
			strReturn = tzGdObject.getHTMLText("HTML.TZApplicationSurveyBundle.TZ_SURVEY_ANS_NEW_HTML",request.getContextPath(), strTitle, String.valueOf(totalCount), strDivHtml);
		   return strReturn;
		}
		catch(Exception e){
			//最外层 try catch捕捉所有异常
			e.printStackTrace();
			return null;
		}
	
	}
	
}
