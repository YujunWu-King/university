package com.tranzvision.gd.TZMobileWebsiteIndexBundle.service.impl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZApplicationCenterBundle.service.impl.AnalysisLcResult;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.ValidateUtil;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 历史报名记录列表状态查询
 * @author tang
 * classid： mAppHistory
 *
 */
@Service("com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.ValidateUtil.MobileAppHistoryServiceImpl")
public class MobileAppHistoryServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TZGDObject tzGDObject;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private ValidateUtil validateUtil;
	
	/*手机版招生网站报名表状态查询*/
	@Override
	public String tzGetHtmlContent(String strParams) {
		String html = "";
		
		//rootPath;
		String ctxPath = request.getContextPath();
		
		String m_curOPRID = tzLoginServiceImpl.getLoginedManagerOprid(request);
		//测试用
		//m_curOPRID = "TZ_14026";
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);
		String siteId = "";
		if(jacksonUtil.containsKey("siteId")){
			siteId = jacksonUtil.getString("siteId");
		}else{
			siteId = request.getParameter("siteId");
		}
		
		AnalysisLcResult analysisLcResult = new AnalysisLcResult();
		
		try {
			//title;
			String title = "历史申请记录 ";
			//css和js
			String jsCss = tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_APP_STATUS_JS_CSS",ctxPath);
			
			//获取语言和机构;
			String siteSQL = "select TZ_SITE_LANG,TZ_JG_ID from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
			Map<String, Object> mapSiteiInfo = sqlQuery.queryForMap(siteSQL,new Object[]{siteId});
			String strLangID = "";
			String orgId = "";
			if (mapSiteiInfo != null) {
				strLangID = mapSiteiInfo.get("TZ_SITE_LANG") == null ? "ZHS": String.valueOf(mapSiteiInfo.get("TZ_SITE_LANG"));
				orgId = mapSiteiInfo.get("TZ_JG_ID") == null ? ""	: String.valueOf(mapSiteiInfo.get("TZ_JG_ID"));
			}
			
			
			String submitTimeDesc = "提交时间:";
			String content = "";
			//循环当前考生的所有报名;
			String appinsSQL = "select TZ_APP_INS_ID,TZ_CLASS_ID,DATE_FORMAT(ROW_LASTMANT_DTTM,'%Y-%m-%d') ROW_LASTMANT_DTTM from PS_TZ_FORM_WRK_T where OPRID=? and TZ_CLASS_ID in (SELECT TZ_CLASS_ID FROM  PS_TZ_CLASS_INF_T where TZ_PRJ_ID IN (SELECT TZ_PRJ_ID FROM PS_TZ_PROJECT_SITE_T WHERE TZ_SITEI_ID=?) AND TZ_JG_ID=?) order by ROW_LASTMANT_DTTM desc";
			long TZ_APP_INS_ID = 0;
			String classId = "";
			List<Map<String, Object>> list = sqlQuery.queryForList(appinsSQL,new Object[]{m_curOPRID,siteId,orgId});
			if(list != null && list.size() > 0){
				for(int i = 0; i < list.size(); i++){
					//流程内容;
					String xmjdHtml = "";
					Map<String, Object> classAndBmbMap = list.get(i);
					if(classAndBmbMap != null){
						TZ_APP_INS_ID = Long.parseLong(String.valueOf(classAndBmbMap.get("TZ_APP_INS_ID")));
						classId = String.valueOf(classAndBmbMap.get("TZ_CLASS_ID"));
						submitTimeDesc = submitTimeDesc + String.valueOf(classAndBmbMap.get("ROW_LASTMANT_DTTM"));
					}
				
					if(classId == null){
						classId = "";
					}
					String className = "";
					
					//已经报名
					if(TZ_APP_INS_ID > 0 && !"".equals(classId)){
						//班级名称；
						className = sqlQuery.queryForObject("select TZ_CLASS_NAME from PS_TZ_CLASS_INF_T where TZ_CLASS_ID =?",new Object[]{classId},"String");
						
						// 报名流程模型实例是否存在;
						int bmlcTotalNum = sqlQuery.queryForObject(
								"select count(1) from PS_TZ_CLS_BMLC_T where TZ_CLASS_ID=?", new Object[] { classId },
								"Integer");

						if(bmlcTotalNum > 0){

							String bmlcSql = "select a.TZ_APPPRO_ID,a.TZ_APPPRO_NAME,b.TZ_APPPRO_HF_BH,b.TZ_APPPRO_RST from PS_TZ_CLS_BMLC_T a left join (select * from PS_TZ_APPPRO_RST_T where TZ_APP_INS_ID=? and TZ_CLASS_ID=?) b on a.TZ_APPPRO_ID=b.TZ_APPPRO_ID where a.TZ_CLASS_ID=? order by a.TZ_SORT_NUM asc";
							List<Map<String, Object>> bmlcList = sqlQuery.queryForList(bmlcSql,
									new Object[] { TZ_APP_INS_ID, classId, classId });
							int step = 0;
							String stepHtml = "";
							//未发布的一个流程紫色，后面的灰色;
							boolean lcZsBl = false;
							//上个流程是不是发布了;
							String[] jdInfo = {"一","二","三","四","五","六","七","八","九","十"};
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
										TZ_APPPRO_RST = sqlQuery.queryForObject(
												"select TZ_APPPRO_CONTENT from PS_TZ_CLS_BMLCHF_T where TZ_CLASS_ID=? and TZ_APPPRO_ID=? and TZ_WFB_DEFALT_BZ='on'",
												new Object[] { classId, TZ_APPPRO_ID }, "String");
										if(TZ_APPPRO_RST == null){
											TZ_APPPRO_RST = "";
										}
									}
									
								
									if(TZ_APPPRO_RST != null && !"".equals(TZ_APPPRO_RST)){
										String type = "A";
										//解析系统变量;
										String[] result =  analysisLcResult.analysisLc(type,String.valueOf(TZ_APP_INS_ID) , ctxPath, TZ_APPPRO_RST,"Y",siteId);

										isFb = result[0];
										TZ_APPPRO_RST = result[1];
									}

									if("Y".equals(isFb)){
										xmjdHtml = xmjdHtml + tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_APP_STATUS_INNER_DIV","ed","阶段"+jdInfo[step-1]+":"+TZ_APPPRO_NAME,TZ_APPPRO_RST," circled","");
									}else{
										//如果是未发布且第一步直接紫色
										if(step == 1 || sgIsFb){
											xmjdHtml = xmjdHtml + tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_APP_STATUS_INNER_DIV","","阶段"+jdInfo[step-1]+":"+TZ_APPPRO_NAME,TZ_APPPRO_RST," circling",String.valueOf(step));
										}else{
											xmjdHtml = xmjdHtml + tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_APP_STATUS_INNER_DIV","","阶段"+jdInfo[step-1]+":"+TZ_APPPRO_NAME,TZ_APPPRO_RST,"",String.valueOf(step));
										}
									}
									
									//标记上一步是不是发布;
									if("Y".equals(isFb)){
										sgIsFb = true;
									}else{
										sgIsFb = false;
									}
									
								}
							} 
						}
						
						content = content + tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_HISTORY_APP_STATUS_LI",className,submitTimeDesc,xmjdHtml);
					}
				}
			}
			
			content = tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_HISTORY_APP_STATUS_HTML",title,content);
			html = tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_MOBILE_BASE_HTML",title,ctxPath,jsCss,siteId,"1",content);
		} catch (TzSystemException e) {
			// TODO Auto-generated catch block
			html = "";
			e.printStackTrace();
		}
		return html;
	}
	
}
