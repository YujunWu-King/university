package com.tranzvision.gd.TZApplicationCenterBundle.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.MessageTextServiceImpl;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 
 * 新报名中心，根据清华切图
 *
 */
@Service("com.tranzvision.gd.TZApplicationCenterBundle.service.impl.ApplicationCenter2ServicerImpl")
public class ApplicationCenter2ServicerImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private MessageTextServiceImpl messageTextServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TZGDObject tzGDObject;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	/****** 报名中心 ********/
	@Override
	public String tzGetHtmlContent(String strParams) {
		String applicationCenterHtml = "";
		JacksonUtil jacksonUtil = new JacksonUtil();

		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		try {
			jacksonUtil.json2Map(strParams);
			String strSiteId = "";
			if (jacksonUtil.containsKey("siteId")) {
				strSiteId = jacksonUtil.getString("siteId");
			}

			if (strSiteId == null || "".equals(strSiteId)) {
				strSiteId = request.getParameter("siteId");
			}
			
			AnalysisLcResult analysisLcResult = new AnalysisLcResult();
			
			//项目跟目录;
			String rootPath = request.getContextPath();
			// 根据siteid得到机构id;
			String str_jg_id = "";
			// language;
			String language = "";

			String siteSQL = "select TZ_JG_ID,TZ_SKIN_STOR,TZ_SITE_LANG from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
			Map<String, Object> siteMap = jdbcTemplate.queryForMap(siteSQL, new Object[] { strSiteId });
			if (siteMap != null) {
				str_jg_id = (String) siteMap.get("TZ_JG_ID");
				// String skinstor = (String)siteMap.get("TZ_SKIN_STOR");
				language = (String) siteMap.get("TZ_SITE_LANG");
			}

			if (language == null || "".equals(language)) {
				language = "ZHS";
			}

			// 通用链接;
			String ZSGL_URL = request.getContextPath() + "/dispatcher";
						
			// 报名中心;
			String ApplicationCenter = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_APPCENTER_MESSAGE", "1",language, "面试申请", "面试申请");
			
			//开始新申请;
			String addNewSqBtDesc = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_APPCENTER_MESSAGE", "2", language, "立即申请","立即申请");
			
			//查看历史;
			String viewHistoryDesc = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_APPCENTER_MESSAGE", "3", language, "查看历史报名","查看历史报名");
			
			//查看报名表;
			String viewBmbDesc = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_APPCENTER_MESSAGE", "4", language, "查看报名表","查看报名表");
			
			//选择报考方向;
			String selectBkfxDesc = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_APPCENTER_MESSAGE", "5", language, "选择报考方向","选择报考方向");
			//取消;
			String cancleDesc = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_APPCENTER_MESSAGE", "6", language, "取消","取消");
			//选择报考方向;
			String okDesc = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_APPCENTER_MESSAGE", "7", language, "确认","确认");
			
			//获取数据失败，请联系管理员;
			applicationCenterHtml = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_APPCENTER_MESSAGE", "8",language, "获取数据失败，请联系管理员", "获取数据失败，请联系管理员");
			
			//不可报名时的按钮title描述：暂时未开放申请，请耐心等待...;
			String noApplyTitle = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_APPCENTER_MESSAGE", "100",language, "暂时未开放申请，请耐心等待...","暂时未开放申请，请耐心等待...");
			
			//添加逻辑：只有不在黑名单中且可以申请报名的,如果有没报名的班级则直接显示在线报名;
			//1:是否允许报名,"N"表示不允许报名;
			String isAllowedApp = "";
			//需要查询考生允许报名表 ；
			isAllowedApp = jdbcTemplate.queryForObject("select TZ_ALLOW_APPLY from PS_TZ_REG_USER_T where OPRID=?", new Object[]{oprid},"String");
			//黑名单
			String isBlack = jdbcTemplate.queryForObject("select TZ_BLACK_NAME from PS_TZ_REG_USER_T where OPRID=?", new Object[]{oprid},"String");
			if(!"Y".equals(isBlack) && "Y".equals(isAllowedApp)){
				//是否有没报名的班级;
				String isNoBmClassSQL = "SELECT count(1) FROM  PS_TZ_CLASS_INF_T where TZ_PRJ_ID IN (SELECT TZ_PRJ_ID FROM PS_TZ_PROJECT_SITE_T WHERE TZ_SITEI_ID=?) AND TZ_JG_ID=? and TZ_IS_APP_OPEN='Y' and TZ_APP_START_DT IS NOT NULL AND TZ_APP_START_TM IS NOT NULL AND TZ_APP_END_DT IS NOT NULL AND TZ_APP_END_TM IS NOT NULL AND str_to_date(concat(DATE_FORMAT(TZ_APP_START_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(TZ_APP_START_TM,'%H:%i'),':00'),'%Y/%m/%d %H:%i:%s') <= now() AND str_to_date(concat(DATE_FORMAT(TZ_APP_END_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(TZ_APP_END_TM,'%H:%i'),':59'),'%Y/%m/%d %H:%i:%s') >= now() AND TZ_CLASS_ID NOT IN (select TZ_CLASS_ID from PS_TZ_FORM_WRK_T where OPRID=?)";
				int hasNoBmNum = jdbcTemplate.queryForObject(isNoBmClassSQL, new Object[] { strSiteId,str_jg_id,oprid }, "Integer");
				if(hasNoBmNum > 0){
					int hasBmTotal = jdbcTemplate.queryForObject("select count(1) from PS_TZ_FORM_WRK_T where OPRID=? and TZ_CLASS_ID in (SELECT TZ_CLASS_ID FROM  PS_TZ_CLASS_INF_T where TZ_PRJ_ID IN  (SELECT TZ_PRJ_ID FROM PS_TZ_PROJECT_SITE_T WHERE TZ_SITEI_ID=?)  AND TZ_JG_ID=?)",new Object[] { oprid,strSiteId,str_jg_id  },"Integer");
					//有没有历史报名;
					if(hasBmTotal > 0){
						String hisUrl = rootPath + "/dispatcher?classid=applyHis&siteId="+strSiteId;
						applicationCenterHtml = tzGDObject.getHTMLText(
								"HTML.TZApplicationCenterBundle.TZ_APPCENTER_CAN_APPLY2", ApplicationCenter,
								addNewSqBtDesc,hisUrl,viewHistoryDesc);
					}else{
						applicationCenterHtml = tzGDObject.getHTMLText(
								"HTML.TZApplicationCenterBundle.TZ_APPCENTER_CAN_APPLY", ApplicationCenter,
								addNewSqBtDesc);
					}
					
					//加载班级选择div;
					String classDiv = "";
					String classselect = "";
					//循坏允许报名的班级;
					String classSQL = "SELECT TZ_CLASS_ID, TZ_CLASS_NAME FROM  PS_TZ_CLASS_INF_T where TZ_PRJ_ID IN (SELECT TZ_PRJ_ID FROM PS_TZ_PROJECT_SITE_T WHERE TZ_SITEI_ID=?) AND TZ_JG_ID=? and TZ_IS_APP_OPEN='Y' and TZ_APP_START_DT IS NOT NULL AND TZ_APP_START_TM IS NOT NULL AND TZ_APP_END_DT IS NOT NULL AND TZ_APP_END_TM IS NOT NULL AND str_to_date(concat(DATE_FORMAT(TZ_APP_START_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(TZ_APP_START_TM,'%H:%i'),':00'),'%Y/%m/%d %H:%i:%s') <= now() AND str_to_date(concat(DATE_FORMAT(TZ_APP_END_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(TZ_APP_END_TM,'%H:%i'),':59'),'%Y/%m/%d %H:%i:%s') >= now() AND TZ_CLASS_ID NOT IN (select TZ_CLASS_ID from PS_TZ_FORM_WRK_T where OPRID=?)";
					List<Map<String, Object>> classList = jdbcTemplate.queryForList(classSQL,new Object[]{strSiteId,str_jg_id,oprid});
					if(classList != null && classList.size() > 0){
						for(int j = 0; j < classList.size();j++ ){
							String TZ_CLASS_ID = (String)classList.get(j).get("TZ_CLASS_ID");
							String TZ_CLASS_NAME = (String)classList.get(j).get("TZ_CLASS_NAME");
							
							if(classselect != null && !"".equals(classselect)){
								classselect = classselect + tzGDObject.getHTMLText(
										"HTML.TZApplicationCenterBundle.TZ_APPCENTER_CLASSS", TZ_CLASS_ID,
										TZ_CLASS_NAME);
							}else{
								classselect = tzGDObject.getHTMLText(
										"HTML.TZApplicationCenterBundle.TZ_APPCENTER_CLASSS", TZ_CLASS_ID,
										TZ_CLASS_NAME);
							}
							
						}
					}
					classDiv = tzGDObject.getHTMLText(
							"HTML.TZApplicationCenterBundle.TZ_APPCENTER_CLASSSELECT_HTML", selectBkfxDesc,cancleDesc,okDesc,language,classselect);
					applicationCenterHtml = applicationCenterHtml + classDiv;
					return applicationCenterHtml;
				}
			}
			
			//是否开通了班级;
			String totalSQL = "SELECT count(1) FROM  PS_TZ_CLASS_INF_T where TZ_PRJ_ID IN (SELECT TZ_PRJ_ID FROM PS_TZ_PROJECT_SITE_T WHERE TZ_SITEI_ID=?) AND TZ_JG_ID=? and TZ_IS_APP_OPEN='Y' and TZ_APP_START_DT IS NOT NULL AND TZ_APP_START_TM IS NOT NULL AND TZ_APP_END_DT IS NOT NULL AND TZ_APP_END_TM IS NOT NULL AND str_to_date(concat(DATE_FORMAT(TZ_APP_START_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(TZ_APP_START_TM,'%H:%i'),':00'),'%Y/%m/%d %H:%i:%s') <= now() AND str_to_date(concat(DATE_FORMAT(TZ_APP_END_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(TZ_APP_END_TM,'%H:%i'),':59'),'%Y/%m/%d %H:%i:%s') >= now()";
			int totalNum = jdbcTemplate.queryForObject(totalSQL, new Object[] { strSiteId,str_jg_id }, "Integer");
			
			//是否已经报名了;
			long hasAppIns = 0;
			String hasClassId = "";
			String hasMsPcId = "";
			String hasMsPcName = "";
			Map<String, Object> hasAppInsMap = jdbcTemplate.queryForMap("select TZ_APP_INS_ID,TZ_CLASS_ID,TZ_BATCH_ID from PS_TZ_FORM_WRK_T where OPRID=? and TZ_CLASS_ID in (SELECT TZ_CLASS_ID FROM  PS_TZ_CLASS_INF_T where TZ_PRJ_ID IN  (SELECT TZ_PRJ_ID FROM PS_TZ_PROJECT_SITE_T WHERE TZ_SITEI_ID=?)  AND TZ_JG_ID=?) order by ROW_LASTMANT_DTTM desc limit 0,1",new Object[] { oprid,strSiteId,str_jg_id  });
			if(hasAppInsMap !=null){
				hasAppIns = Long.parseLong(String.valueOf(hasAppInsMap.get("TZ_APP_INS_ID")));
				hasClassId = String.valueOf(hasAppInsMap.get("TZ_CLASS_ID"));
				hasMsPcId = String.valueOf(hasAppInsMap.get("TZ_BATCH_ID"));
				if(hasClassId != null && !"".equals(hasClassId) && hasMsPcId != null && !"".equals(hasMsPcId)){
					hasMsPcName = jdbcTemplate.queryForObject("select TZ_BATCH_NAME from PS_TZ_CLS_BATCH_T where TZ_CLASS_ID=? and TZ_BATCH_ID=?", new Object[]{hasClassId,hasMsPcId},"String");
					if(hasMsPcName == null){
						hasMsPcName = "";
					}
				}
			}
			
			//有开通的班级；
			if(totalNum > 0){
				//是否已经报名了当前开通的班级;
				String appinsSQL = "select TZ_APP_INS_ID,TZ_CLASS_ID,TZ_BATCH_ID from PS_TZ_FORM_WRK_T where OPRID=? and TZ_CLASS_ID in (SELECT TZ_CLASS_ID FROM  PS_TZ_CLASS_INF_T where TZ_PRJ_ID IN (SELECT TZ_PRJ_ID FROM PS_TZ_PROJECT_SITE_T WHERE TZ_SITEI_ID=?) AND TZ_JG_ID=? and TZ_IS_APP_OPEN='Y' and TZ_APP_START_DT IS NOT NULL AND TZ_APP_START_TM IS NOT NULL AND TZ_APP_END_DT IS NOT NULL AND TZ_APP_END_TM IS NOT NULL AND str_to_date(concat(DATE_FORMAT(TZ_APP_START_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(TZ_APP_START_TM,'%H:%i'),':00'),'%Y/%m/%d %H:%i:%s') <= now() AND str_to_date(concat(DATE_FORMAT(TZ_APP_END_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(TZ_APP_END_TM,'%H:%i'),':59'),'%Y/%m/%d %H:%i:%s') >= now()) order by ROW_LASTMANT_DTTM desc limit 0,1";
				long TZ_APP_INS_ID = 0;
				String classId = "";
				String msPcId = "";
				String msPcName = "";
				Map<String, Object> classAndBmbMap = new HashMap<String, Object>();
				try{
					classAndBmbMap = jdbcTemplate.queryForMap(appinsSQL,new Object[] { oprid,strSiteId,str_jg_id  });
					if(classAndBmbMap != null){
						TZ_APP_INS_ID = Long.parseLong(String.valueOf(classAndBmbMap.get("TZ_APP_INS_ID")));
						classId = String.valueOf(classAndBmbMap.get("TZ_CLASS_ID"));
						msPcId = String.valueOf(hasAppInsMap.get("TZ_BATCH_ID"));
						if(classId != null && !"".equals(classId) && msPcId != null && !"".equals(msPcId)){
							msPcName = jdbcTemplate.queryForObject("select TZ_BATCH_NAME from PS_TZ_CLS_BATCH_T where TZ_CLASS_ID=? and TZ_BATCH_ID=?", new Object[]{classId,msPcId},"String");
							if(msPcName == null){
								msPcName = "";
							}
						}
					}
				}catch(NullPointerException nullException){
					TZ_APP_INS_ID = 0;
					classId = "";
				}
				
				//classId = "84";
				//TZ_APP_INS_ID = 231;
				//已经报名了开通的班级，显示报名流程;
				if(TZ_APP_INS_ID > 0){
					applicationCenterHtml = this.getBmlc(TZ_APP_INS_ID, classId, strSiteId, language,msPcName);
				}else{
					//没报名当前班级,如果允许报名则显示报名按钮;
					
					if("Y".equals(isBlack) || !"Y".equals(isAllowedApp)){
						//不允许报名，是否有以前的报名表，有则显示报名;
						if(hasAppIns > 0){
							applicationCenterHtml = this.getBmlc(hasAppIns, hasClassId, strSiteId, language,hasMsPcName);
						}else{
							applicationCenterHtml = tzGDObject.getHTMLText(
									"HTML.TZApplicationCenterBundle.TZ_APPCENTER_CANTNOT_APPLY", ApplicationCenter,
									addNewSqBtDesc,noApplyTitle);
						}
						
						return applicationCenterHtml;
					}else{
						//允许报名
						
						//有没有历史报名;
						if(hasAppIns > 0){
							String hisUrl = rootPath + "/dispatcher?classid=applyHis&siteId="+strSiteId;
							applicationCenterHtml = tzGDObject.getHTMLText(
									"HTML.TZApplicationCenterBundle.TZ_APPCENTER_CAN_APPLY2", ApplicationCenter,
									addNewSqBtDesc,hisUrl,viewHistoryDesc);
						}else{
							applicationCenterHtml = tzGDObject.getHTMLText(
									"HTML.TZApplicationCenterBundle.TZ_APPCENTER_CAN_APPLY", ApplicationCenter,
									addNewSqBtDesc);
						}
						
						//加载班级选择div;
						String classDiv = "";
						String classselect = "";
						//循坏允许报名的班级;
						String classSQL = "SELECT TZ_CLASS_ID, TZ_CLASS_NAME FROM  PS_TZ_CLASS_INF_T where TZ_PRJ_ID IN (SELECT TZ_PRJ_ID FROM PS_TZ_PROJECT_SITE_T WHERE TZ_SITEI_ID=?) AND TZ_JG_ID=? and TZ_IS_APP_OPEN='Y' and TZ_APP_START_DT IS NOT NULL AND TZ_APP_START_TM IS NOT NULL AND TZ_APP_END_DT IS NOT NULL AND TZ_APP_END_TM IS NOT NULL AND str_to_date(concat(DATE_FORMAT(TZ_APP_START_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(TZ_APP_START_TM,'%H:%i'),':00'),'%Y/%m/%d %H:%i:%s') <= now() AND str_to_date(concat(DATE_FORMAT(TZ_APP_END_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(TZ_APP_END_TM,'%H:%i'),':59'),'%Y/%m/%d %H:%i:%s') >= now()";
						List<Map<String, Object>> classList = jdbcTemplate.queryForList(classSQL,new Object[]{strSiteId,str_jg_id});
						if(classList != null && classList.size() > 0){
							for(int j = 0; j < classList.size();j++ ){
								String TZ_CLASS_ID = (String)classList.get(j).get("TZ_CLASS_ID");
								String TZ_CLASS_NAME = (String)classList.get(j).get("TZ_CLASS_NAME");
								
								if(classselect != null && !"".equals(classselect)){
									classselect = classselect + tzGDObject.getHTMLText(
											"HTML.TZApplicationCenterBundle.TZ_APPCENTER_CLASSS", TZ_CLASS_ID,
											TZ_CLASS_NAME);
								}else{
									classselect = tzGDObject.getHTMLText(
											"HTML.TZApplicationCenterBundle.TZ_APPCENTER_CLASSS", TZ_CLASS_ID,
											TZ_CLASS_NAME);
								}
								
							}
						}
						classDiv = tzGDObject.getHTMLText(
								"HTML.TZApplicationCenterBundle.TZ_APPCENTER_CLASSSELECT_HTML", selectBkfxDesc,cancleDesc,okDesc,language,classselect);
						applicationCenterHtml = applicationCenterHtml + classDiv;
						return applicationCenterHtml;
					}
				}
			
			}else{
				//没有开通的班级,有没有历史报名表；
				if(hasAppIns > 0){
					applicationCenterHtml = this.getBmlc(hasAppIns, hasClassId, strSiteId, language,hasMsPcName);
				}else{
					applicationCenterHtml = tzGDObject.getHTMLText(
							"HTML.TZApplicationCenterBundle.TZ_APPCENTER_CANTNOT_APPLY", ApplicationCenter,
							addNewSqBtDesc,noApplyTitle);
				}
				
				
				return applicationCenterHtml;
			}

		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return applicationCenterHtml;
	}
	
	
	private String getBmlc(long TZ_APP_INS_ID,String classId,String strSiteId,String language,String msPcName){
		
		String applicationCenterHtml = "";
		AnalysisLcResult analysisLcResult = new AnalysisLcResult();
		//项目跟目录;
		String rootPath = request.getContextPath();
		
		// 通用链接;
		String ZSGL_URL = request.getContextPath() + "/dispatcher";
		
		// 报名中心;
		String ApplicationCenter = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_APPCENTER_MESSAGE", "1",language, "面试申请", "面试申请");
		//查看历史;
		String viewHistoryDesc = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_APPCENTER_MESSAGE", "3", language, "查看历史报名","查看历史报名");
					
		//查看报名表;
		String viewBmbDesc = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_APPCENTER_MESSAGE", "4", language, "查看报名表","查看报名表");		
		// 报名表链接;
		String applyFromUrl = ZSGL_URL + "?classid=appId&TZ_CLASS_ID=" + classId + "&SITE_ID=" + strSiteId;
		
		String className = jdbcTemplate.queryForObject("SELECT TZ_CLASS_NAME FROM  PS_TZ_CLASS_INF_T where TZ_CLASS_ID=?", new Object[]{classId},"String");
		try{
			// 报名流程模型实例是否存在;
			int bmlcTotalNum = jdbcTemplate.queryForObject(
					"select count(1) from PS_TZ_CLS_BMLC_T where TZ_CLASS_ID=?", new Object[] { classId },
					"Integer");
			String stepHtml = "";
			String lcContentHtml = "";
			//最新发布的step;
			int showLcStepNum = 0;
			// 循环报名流程;
			if (bmlcTotalNum > 0) {
				String bmlcSql = "select a.TZ_APPPRO_ID,a.TZ_APPPRO_NAME,b.TZ_APPPRO_HF_BH,b.TZ_APPPRO_RST from PS_TZ_CLS_BMLC_T a left join (select * from PS_TZ_APPPRO_RST_T where TZ_APP_INS_ID=? and TZ_CLASS_ID=?) b on a.TZ_APPPRO_ID=b.TZ_APPPRO_ID where a.TZ_CLASS_ID=? order by a.TZ_SORT_NUM asc";
				List<Map<String, Object>> bmlcList = jdbcTemplate.queryForList(bmlcSql,
						new Object[] { TZ_APP_INS_ID, classId, classId });
				int step = 0;
				
				//未发布的一个流程紫色，后面的灰色;
				boolean lcZsBl = false;
				//上个流程是不是发布了;
				boolean sgIsFb = false;
				if (bmlcList != null && bmlcList.size() > 0) {
					for (int j = 0; j < bmlcList.size(); j++) {
						step = step + 1;
						//是否发布;
						String isFb = "";
						
						String TZ_APPPRO_ID = (String) bmlcList.get(j).get("TZ_APPPRO_ID");
						String TZ_APPPRO_NAME = (String) bmlcList.get(j).get("TZ_APPPRO_NAME");
						String TZ_APPPRO_HF_BH = (String) bmlcList.get(j).get("TZ_APPPRO_HF_BH");
						String TZ_APPPRO_RST = (String) bmlcList.get(j).get("TZ_APPPRO_RST");
						if(TZ_APPPRO_NAME == null){
							TZ_APPPRO_NAME = "";
						}
						if(TZ_APPPRO_HF_BH == null){
							TZ_APPPRO_HF_BH = "";
						}
						if(TZ_APPPRO_RST == null){
							TZ_APPPRO_RST = "";
						}
						
						//没有发布回复短语则统一取默认的
						if (TZ_APPPRO_HF_BH == null || "".equals(TZ_APPPRO_HF_BH)) {
							TZ_APPPRO_RST = jdbcTemplate.queryForObject(
									"select TZ_APPPRO_CONTENT from PS_TZ_CLS_BMLCHF_T where TZ_CLASS_ID=? and TZ_APPPRO_ID=? and TZ_WFB_DEFALT_BZ='on'",
									new Object[] { classId, TZ_APPPRO_ID }, "String");
							if(TZ_APPPRO_RST == null){
								TZ_APPPRO_RST = "";
							}
						}
						
					
						if(TZ_APPPRO_RST != null && !"".equals(TZ_APPPRO_RST)){
							String type = "A";
							//解析系统变量;
							String[] result =  analysisLcResult.analysisLc(type,String.valueOf(TZ_APP_INS_ID) , rootPath, TZ_APPPRO_RST,"N",strSiteId);
	
							isFb = result[0];
							TZ_APPPRO_RST = result[1];
						}
						
						//流程发布内容;
						if(lcContentHtml == null || "".equals(lcContentHtml)){
							//lcContentHtml = tzGDObject.getHTMLText("HTML.TZApplicationCenterBundle.TZ_APPCENTER_LC_CONTENT",TZ_APPPRO_RST,"triangle","triangle_span");
							lcContentHtml = tzGDObject.getHTMLText("HTML.TZApplicationCenterBundle.TZ_APPCENTER_LC_CONTENT",TZ_APPPRO_RST);
						}else{
							//TZ_APPPRO_RST = tzGDObject.getHTMLText("HTML.TZApplicationCenterBundle.TZ_APPCENTER_LC_CONTENT2",TZ_APPPRO_RST,"triangle"+(step-1),"triangle_span"+(step-1));
							TZ_APPPRO_RST = tzGDObject.getHTMLText("HTML.TZApplicationCenterBundle.TZ_APPCENTER_LC_CONTENT2",TZ_APPPRO_RST);
							lcContentHtml = lcContentHtml + TZ_APPPRO_RST;
						}
						
						String stepLi = "";
						String styleClassName = "",stepNum = "";
						if("Y".equals(isFb)){
							//流程打勾样式
							styleClassName = " sq_steped";
							stepNum = "";
							showLcStepNum = step;
						}else{
							//未发布的一个流程紫色，或则前面的流程是发布的;
							if(lcZsBl == false || sgIsFb == true){
								styleClassName = " sq_step step_ing";
								stepNum = String.valueOf(step);
								lcZsBl = true;
							}else{
								styleClassName = " sq_step";
								stepNum = String.valueOf(step);
							}
							
						}
						
						if(step == 1){
							stepLi = tzGDObject.getHTMLText("HTML.TZApplicationCenterBundle.TZ_APPCENTER_LC_SETP1",styleClassName,stepNum,TZ_APPPRO_NAME);
						}else{
							stepLi = tzGDObject.getHTMLText("HTML.TZApplicationCenterBundle.TZ_APPCENTER_LC_SETP",styleClassName,stepNum,TZ_APPPRO_NAME);
						}
						
						//第一步前面不需要加横线;
						if(step == 1){
							stepHtml = stepLi;
						}else{
							String stepLine = "";
							if(styleClassName.indexOf("step_ing") > 0 || sgIsFb == true){
								stepLine = tzGDObject.getHTMLText("HTML.TZApplicationCenterBundle.TZ_APPCENTER_LC_SETP_LINE"," line_ed");
							}else{
								stepLine = tzGDObject.getHTMLText("HTML.TZApplicationCenterBundle.TZ_APPCENTER_LC_SETP_LINE","");
							}
							stepHtml = stepHtml + stepLine + stepLi;
							
						}
						
						if("Y".equals(isFb)){
							sgIsFb = true;
						}else{
							sgIsFb = false;
						}
						
					}
				}
			}
			lcContentHtml = "<div class=\"step_Note\">"+lcContentHtml+"</div>";
			//历史报名页面;
			String hisUrl = rootPath + "/dispatcher?classid=applyHis&siteId="+strSiteId;
			String showLcStepString = "0";
			if(showLcStepNum > 0){
				showLcStepString = String.valueOf((showLcStepNum-1)*2);
			}
			applicationCenterHtml = tzGDObject.getHTMLText("HTML.TZApplicationCenterBundle.TZ_APPCENTER_LC_HTML", ApplicationCenter,viewHistoryDesc,viewBmbDesc,lcContentHtml,stepHtml,applyFromUrl,hisUrl,showLcStepString,className+msPcName);
		}catch(Exception e){
			e.printStackTrace();
			applicationCenterHtml = "";
		}
		return applicationCenterHtml;
	}
}
