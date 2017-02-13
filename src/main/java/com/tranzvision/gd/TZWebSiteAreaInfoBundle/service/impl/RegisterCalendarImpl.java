package com.tranzvision.gd.TZWebSiteAreaInfoBundle.service.impl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.MessageTextServiceImpl;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 招生报名系统首页，报考日历
 * 
 * @author 叶少威
 * @since 2017-2-10
 */
@Service("com.tranzvision.gd.TZWebSiteAreaInfoBundle.service.impl.RegisterCalendarImpl")
public class RegisterCalendarImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private MessageTextServiceImpl messageTextServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TZGDObject tzGDObject;

	@Override
	public String tzGetHtmlContent(String strParams) {
		String registerCalendarHtml = "";
		StringBuffer registerCalendarLisHtml = null;
		
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {
			jacksonUtil.json2Map(strParams);
			String strSiteId = "";
			String strAreaId="";
			if (jacksonUtil.containsKey("areaId")) {
				strAreaId = jacksonUtil.getString("areaId");
			}
			
			if (jacksonUtil.containsKey("siteId")) {
				strSiteId = jacksonUtil.getString("siteId");
			}

			// language;
			String language = "";

			String siteSQL = "select TZ_SITE_LANG from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
			Map<String, Object> siteMap = jdbcTemplate.queryForMap(siteSQL, new Object[] { strSiteId });
			if (siteMap != null) {
				language = (String) siteMap.get("TZ_SITE_LANG");
			}

			if (language == null || "".equals(language)) {
				language = "ZHS";
			}
			
			String contextPath = request.getContextPath();
			
			//报考日历;
			String registerCalendar = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_REGCALENDAR_MESSAGE", "1",language, "报考日历", "Register Calendar");
			
			//更多;
			String more = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_REGCALENDAR_MESSAGE", "2", language, "更多","More");
			
			//查看更多报考日历 jufeng 增加2017-02-10;
			StringBuffer moreRegisterCalUrl = new StringBuffer(contextPath).append("/dispatcher?classid=moreBkrl&siteId=").append(strSiteId);
			//根据站点和区域获取栏目的文章列表:报考日历内容类型配置字段 TZ_LONG1：地点
			String artListSql = "SELECT B.TZ_COLU_ID,A.TZ_ART_ID,A.TZ_ART_TITLE,A.TZ_LONG1 AS TZ_ART_ADDR,month(B.TZ_ART_NEWS_DT) AS TZ_ART_MONTH,day(B.TZ_ART_NEWS_DT) AS TZ_ART_DAY FROM PS_TZ_ART_REC_TBL A "
					+ "INNER JOIN PS_TZ_LM_NR_GL_T B ON(A.TZ_ART_ID=B.TZ_ART_ID AND B.TZ_SITE_ID=? AND B.TZ_ART_PUB_STATE='Y') "
					+ "INNER JOIN PS_TZ_SITEI_AREA_T C ON (B.TZ_COLU_ID = C.TZ_COLU_ID AND C.TZ_SITEI_ID=B.TZ_SITE_ID AND C.TZ_AREA_ID=?) "
					+ "ORDER BY B.TZ_ART_SEQ,B.TZ_ART_NEWS_DT DESC LIMIT 4";
					
			List<Map<String, Object>> artList = jdbcTemplate.queryForList(artListSql,new Object[] { strSiteId,strAreaId });
			if (artList != null && artList.size()>0){
				for(int i=0;i<artList.size();i++){
					
					String columnId = (String) artList.get(i).get("TZ_COLU_ID");
					String artId = (String) artList.get(i).get("TZ_ART_ID");
					String artTitle = (String) artList.get(i).get("TZ_ART_TITLE");
					String artAddr = (String) artList.get(i).get("TZ_ART_ADDR");
					String artMonth = artList.get(i).get("TZ_ART_MONTH").toString();
					String artDay = artList.get(i).get("TZ_ART_DAY").toString();
					StringBuffer sbArtUrl = new StringBuffer(contextPath).append("/dispatcher?classid=art_preview&operatetype=HTML&siteId=")
							.append(strSiteId).append("&columnId=").append(columnId).append("&artId=").append(artId);
					if(registerCalendarLisHtml==null){
						registerCalendarLisHtml = new StringBuffer("<ul>").append(tzGDObject.getHTMLText("HTML.TZWebSiteAreaInfoBundle.TZ_SITE_AREA_BKRL_LI_HTML", sbArtUrl.toString(),
								artDay,artMonth,artTitle,artAddr));
					}else{
						if(i==2){
							registerCalendarLisHtml.append("</ul><ul>");
						}
						registerCalendarLisHtml.append(tzGDObject.getHTMLText("HTML.TZWebSiteAreaInfoBundle.TZ_SITE_AREA_BKRL_LI_HTML", sbArtUrl.toString(),
										artDay,artMonth,artTitle,artAddr));
					}
				}
				registerCalendarLisHtml.append("</ul>");
			}
			
			registerCalendarHtml = tzGDObject.getHTMLText("HTML.TZWebSiteAreaInfoBundle.TZ_SITE_AREA_BKRL_HTML", registerCalendar,
					more,registerCalendarLisHtml==null?"":registerCalendarLisHtml.toString(),moreRegisterCalUrl.toString()); 
			
			return registerCalendarHtml;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "";
	}
}
