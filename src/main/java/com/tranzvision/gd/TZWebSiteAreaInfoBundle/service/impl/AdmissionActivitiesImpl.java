package com.tranzvision.gd.TZWebSiteAreaInfoBundle.service.impl;

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
 * 招生报名系统首页，活动通知tab区域
 * 
 * @author 叶少威
 * @since 2017-2-10
 */
@Service("com.tranzvision.gd.TZWebSiteAreaInfoBundle.service.impl.AdmissionActivitiesImpl")
public class AdmissionActivitiesImpl extends FrameworkImpl {
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

	@Override
	public String tzGetHtmlContent(String strParams) {
		String areaContentHtml = "";
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {
			jacksonUtil.json2Map(strParams);
			String strSiteId = "";
			String strAreaId = "";
			if (jacksonUtil.containsKey("areaId")) {
				strAreaId = jacksonUtil.getString("areaId");
			}
			
			if (jacksonUtil.containsKey("siteId")) {
				strSiteId = jacksonUtil.getString("siteId");
			}
			
			// language;
			String language = "";
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			String siteSQL = "select TZ_SITE_LANG from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
			Map<String, Object> siteMap = jdbcTemplate.queryForMap(siteSQL, new Object[] { strSiteId });
			if (siteMap != null) {
				language = (String) siteMap.get("TZ_SITE_LANG");
			}

			if (language == null || "".equals(language)) {
				language = "ZHS";
			}
			
			String contextPath = request.getContextPath();
			
			//TZWebSiteAreaInfo。TZ_SITE_AREA_HDTZ_TAB_LI_HTML 占位符内容
			String firstTabColumnId = "";
			String firstTabName = "";
			String otherTabsHeader = "";
			String more = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_WEBACT_MESSAGE", "1", language, "更多","More");
			StringBuffer sbArtContentTabsHtml = new StringBuffer("");			
			
			//获取栏目编号;
			String columnSQL = "SELECT TZ_COLU_ID FROM PS_TZ_SITEI_AREA_T WHERE TZ_SITEI_ID=? AND TZ_AREA_ID=?";
			String columnId = jdbcTemplate.queryForObject(columnSQL, new Object[] { strSiteId,strAreaId }, "String");
			
			if(columnId!=null&&!"".equals(columnId)){
				String[] columns = columnId.split(",");
				
				for(int i=0;i<columns.length;i++){
					String currentColumnId = columns[i];
					
					//获取栏目名称;
					String columnNameSQL = "SELECT TZ_COLU_NAME FROM PS_TZ_SITEI_COLU_T WHERE TZ_SITEI_ID=? and TZ_COLU_ID=?";
					String columnName = jdbcTemplate.queryForObject(columnNameSQL, new Object[] { strSiteId,currentColumnId }, "String");
					
					if(columnName!=null){
						if(i==0){
							firstTabColumnId = currentColumnId;
							firstTabName = columnName;
						}else{
							otherTabsHeader = otherTabsHeader+"<li tab-col=\""+currentColumnId+"\">"+columnName+"</li>";
						}
					}
					
					//根据栏目下已发布的文章列表，每个栏目限制5条
//					String artListSql = "SELECT B.TZ_COLU_ID,A.TZ_ART_ID,A.TZ_ART_TITLE,A.TZ_ART_TITLE_STYLE,DATE_FORMAT(B.TZ_ART_NEWS_DT,'%Y-%m-%d') AS TZ_ART_NEWS_DT FROM PS_TZ_ART_REC_TBL A "
//							+ "INNER JOIN PS_TZ_LM_NR_GL_T B ON(A.TZ_ART_ID=B.TZ_ART_ID AND B.TZ_SITE_ID=? AND B.TZ_ART_PUB_STATE='Y' AND B.TZ_COLU_ID=?) "
//							+ "ORDER BY B.TZ_ART_SEQ,B.TZ_ART_NEWS_DT DESC LIMIT 5";
					String artListSql = tzGDObject.getSQLText("SQL.TZWebSiteAreaInfoBundle.TZ_ADM_ACT_ART_LIST");
							
					List<Map<String, Object>> artList = jdbcTemplate.queryForList(artListSql,new Object[] { strSiteId,currentColumnId,oprid });
					
					StringBuffer artContentTabLisHtml = null;
					if (artList != null && artList.size()>0){
						artContentTabLisHtml= new StringBuffer("");
						
						for(int j=0;j<artList.size();j++){	
							String artId = (String) artList.get(j).get("TZ_ART_ID");
							String artTitle = (String) artList.get(j).get("TZ_ART_TITLE");
							String artTitleStyle = (String) artList.get(j).get("TZ_ART_TITLE_STYLE");
							String artDate = (String) artList.get(j).get("TZ_ART_NEWS_DT");
							
							String hotTagDisplay = "none";
							String newTagDisplay = "none";
							
							if(artTitleStyle!=null&&!"".equals(artTitleStyle)){
								hotTagDisplay = artTitleStyle.indexOf("HOT")>-1?"block":hotTagDisplay;
								newTagDisplay = artTitleStyle.indexOf("NEW")>-1?"block":newTagDisplay;
							}
							
							StringBuffer sbArtUrl = new StringBuffer(contextPath).append("/dispatcher?classid=art_view&operatetype=HTML&siteId=")
									.append(strSiteId).append("&columnId=").append(currentColumnId).append("&artId=").append(artId);
							
							artContentTabLisHtml = artContentTabLisHtml.append(tzGDObject.getHTMLText("HTML.TZWebSiteAreaInfoBundle.TZ_SITE_AREA_HDTZ_TAB_LI_HTML", sbArtUrl.toString(),
									artTitle,artDate,hotTagDisplay,newTagDisplay));
						}
					}
					sbArtContentTabsHtml.append(tzGDObject.getHTMLText("HTML.TZWebSiteAreaInfoBundle.TZ_SITE_AREA_HDTZ_TAB_HTML",i==0?"dis_block":"dis_none", artContentTabLisHtml!=null?artContentTabLisHtml.toString():""));
				}
				
			}
			areaContentHtml = tzGDObject.getHTMLText("HTML.TZWebSiteAreaInfoBundle.TZ_SITE_AREA_HDTZ_HTML",firstTabColumnId,firstTabName,otherTabsHeader,more,sbArtContentTabsHtml.toString());
			return areaContentHtml;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
