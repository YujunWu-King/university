package com.tranzvision.gd.TZApplicationCenterBundle.service.impl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.MessageTextServiceImpl;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 查看历史
 *
 */
@Service("com.tranzvision.gd.TZApplicationCenterBundle.service.impl.Class2HistoryServiceImpl")
public class Class2HistoryServiceImpl extends FrameworkImpl {
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
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	@Autowired
	private SiteRepCssServiceImpl siteRepCssServiceImpl;

	/****** 班级选择器 ********/
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
			// 项目跟目录;
			String rootPath = request.getContextPath();

			// 根据siteid得到机构id;
			String str_jg_id = "";
			// language;
			String language = "";
			String strCssDir = "";
			String siteSQL = "select TZ_JG_ID,TZ_SKIN_STOR,TZ_SITE_LANG from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
			Map<String, Object> siteMap = jdbcTemplate.queryForMap(siteSQL, new Object[] { strSiteId });
			if (siteMap != null) {
				str_jg_id = (String) siteMap.get("TZ_JG_ID");
				String skinstor = (String) siteMap.get("TZ_SKIN_STOR");
				language = (String) siteMap.get("TZ_SITE_LANG");
				String websitePath = getSysHardCodeVal.getWebsiteCssPath();

				String strRandom = String.valueOf(10 * Math.random());
				if ("".equals(skinstor) || skinstor == null) {
					strCssDir = request.getContextPath() + websitePath + "/" + str_jg_id.toLowerCase() + "/" + strSiteId
							+ "/" + "style_" + str_jg_id.toLowerCase() + ".css?v=" + strRandom;
				} else {
					strCssDir = request.getContextPath() + websitePath + "/" + str_jg_id.toLowerCase() + "/" + strSiteId
							+ "/" + skinstor + "/" + "style_" + str_jg_id.toLowerCase() + ".css?v=" + strRandom;
				}
			}

			if (language == null || "".equals(language)) {
				language = "ZHS";
			}
			// 通用链接;
			String ZSGL_URL = request.getContextPath() + "/dispatcher";

			// 查看报名表;
			String viewBmbDesc = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_APPCENTER_MESSAGE", "4",
					language, "查看报名表", "查看报名表");

			// 获取数据失败，请联系管理员;
			applicationCenterHtml = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_APPCENTER_MESSAGE", "8",
					language, "获取数据失败，请联系管理员", "获取数据失败，请联系管理员");

			// 历史纪录;
			String historyDesc = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_APPCENTER_MESSAGE", "9",
					language, "历史报名", "历史报名");
			// 查看流程信息;
			String viewLcInfoDesc = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_APPCENTER_MESSAGE", "10",
					language, "查看流程信息", "查看流程信息");
			
			//提交时间;
			String submitTimeDesc = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_APPCENTER_MESSAGE", "11",
					language, "提交时间:", "提交时间:");
			
			//无历史报名记录;
			String noHistoryDesc = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_APPCENTER_MESSAGE", "11",
					language, "无历史报名记录", "无历史报名记录");
			

			String listBody = "";

			String sql = "select a.TZ_APP_INS_ID, a.TZ_CLASS_ID,DATE_FORMAT(b.ROW_LASTMANT_DTTM,'%Y-%m-%d') ROW_LASTMANT_DTTM, c.TZ_CLASS_NAME from PS_TZ_FORM_WRK_T a,PS_TZ_APP_INS_T b,PS_TZ_CLASS_INF_T c where a.OPRID=? and a.TZ_APP_INS_ID=b.TZ_APP_INS_ID and a.TZ_CLASS_ID = c.TZ_CLASS_ID and c.TZ_PRJ_ID in (SELECT TZ_PRJ_ID FROM PS_TZ_PROJECT_SITE_T WHERE TZ_SITEI_ID=?) and c.TZ_JG_ID=? order by b.ROW_LASTMANT_DTTM desc";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,new Object[]{oprid,strSiteId,str_jg_id});
			if(list != null && list.size() > 0){
				for(int i = 0; i < list.size(); i++){
					long TZ_APP_INS_ID = Long.parseLong(String.valueOf(list.get(i).get("TZ_APP_INS_ID")));
					String classId = (String)list.get(i).get("TZ_CLASS_ID");
					String className = (String)list.get(i).get("TZ_CLASS_NAME");
					String submitDate = submitTimeDesc + (String)list.get(i).get("ROW_LASTMANT_DTTM");
	
					// 报名表链接;
					String applyFromUrl = ZSGL_URL + "?classid=appId&TZ_CLASS_ID=" + classId + "&SITE_ID=" + strSiteId;
					
					// 报名流程模型实例是否存在;
					int bmlcTotalNum = jdbcTemplate.queryForObject(
							"select count(1) from PS_TZ_CLS_BMLC_T where TZ_CLASS_ID=?", new Object[] { classId },
							"Integer");
					String recordList = "",recordListBody = "";
					//有流程;
					if(bmlcTotalNum > 0){
						String bmlcSql = "select a.TZ_APPPRO_ID,a.TZ_APPPRO_NAME,b.TZ_APPPRO_HF_BH,b.TZ_APPPRO_RST from PS_TZ_CLS_BMLC_T a left join (select * from PS_TZ_APPPRO_RST_T where TZ_APP_INS_ID=? and TZ_CLASS_ID=?) b on a.TZ_APPPRO_ID=b.TZ_APPPRO_ID where a.TZ_CLASS_ID=? order by a.TZ_SORT_NUM asc";
						List<Map<String, Object>> bmlcList = jdbcTemplate.queryForList(bmlcSql,
								new Object[] { TZ_APP_INS_ID, classId, classId });
						int step = 0;
						
						//上个流程是不是发布了;
						boolean sgIsFb = false;
						String jdHtml = "";
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
									//解析邮件里的系统变量;
									String[] result =  analysisLcResult.analysisLc(type,String.valueOf(TZ_APP_INS_ID) , rootPath, TZ_APPPRO_RST,"N");

									isFb = result[0];
									TZ_APPPRO_RST = result[1];
								}
								if(TZ_APPPRO_RST == null){
									TZ_APPPRO_RST = "";
								}
								
								if("Y".equals(isFb)){
									jdHtml = jdHtml + tzGDObject.getHTMLText("HTML.TZApplicationCenterBundle.TZ_CLASS2_HISTORY_LIST_JD_FB",TZ_APPPRO_NAME,TZ_APPPRO_RST);
								}else{
									//如果是未发布且第一步直接紫色
									if(step == 1){
										jdHtml = jdHtml + tzGDObject.getHTMLText("HTML.TZApplicationCenterBundle.TZ_CLASS2_HISTORY_LIST_JD_ING",TZ_APPPRO_NAME,String.valueOf(step),TZ_APPPRO_RST);
									}else{
										if(sgIsFb){
											jdHtml = jdHtml + tzGDObject.getHTMLText("HTML.TZApplicationCenterBundle.TZ_CLASS2_HISTORY_LIST_JD_ING",TZ_APPPRO_NAME,String.valueOf(step),TZ_APPPRO_RST);
										}else{
											jdHtml = jdHtml + tzGDObject.getHTMLText("HTML.TZApplicationCenterBundle.TZ_CLASS2_HISTORY_LIST_JD",TZ_APPPRO_NAME,String.valueOf(step),TZ_APPPRO_RST);
										}
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
						
						recordList = tzGDObject.getHTMLText("HTML.TZApplicationCenterBundle.TZ_CLASS2_HISTORY_LIST",className,submitDate,viewBmbDesc,viewLcInfoDesc,applyFromUrl);
						recordListBody = tzGDObject.getHTMLText("HTML.TZApplicationCenterBundle.TZ_CLASS2_HISTORY_LIST_BODY",jdHtml);
					}else{
						//没有流程;
						recordList = tzGDObject.getHTMLText("HTML.TZApplicationCenterBundle.TZ_CLASS2_HISTORY_LIST_NOLC",className,submitDate,viewBmbDesc,viewLcInfoDesc,applyFromUrl);
						recordListBody = "";
					}
					listBody = listBody + recordList + recordListBody;
				}
			}else{
				//没有历史报名记录;
				listBody = tzGDObject.getHTMLText("HTML.TZApplicationCenterBundle.TZ_CLASS2_HISTORY_NO",noHistoryDesc);
			}
			
			// 历史记录;
			applicationCenterHtml = tzGDObject.getHTMLText("HTML.TZApplicationCenterBundle.TZ_CLASS2_HISTORY_CONTENT",
					historyDesc, listBody);

			// 展示页面;
			applicationCenterHtml = tzGDObject.getHTMLText("HTML.TZApplicationCenterBundle.TZ_GD_CLASS2_HISTORY_HTML",
					request.getContextPath(), ZSGL_URL, strCssDir, applicationCenterHtml, str_jg_id, strSiteId);

			applicationCenterHtml = siteRepCssServiceImpl.repTitle(applicationCenterHtml, strSiteId);
			applicationCenterHtml = siteRepCssServiceImpl.repCss(applicationCenterHtml, strSiteId);

		} catch (Exception e) {
			e.printStackTrace();

			return "无法获取相关数据";
		}
		return applicationCenterHtml;
	}
}
