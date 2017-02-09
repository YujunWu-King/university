package com.tranzvision.gd.TZRecruitStuViewBundle.service.impl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.MessageTextServiceImpl;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 
 * 报考日历
 *
 */
@Service("com.tranzvision.gd.TZRecruitStuViewBundle.service.impl.ApplicationCenter3ServicerImpl")
public class ApplicationCenter3ServicerImpl extends FrameworkImpl {
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

	/****** 报考日历 ********/
	@Override
	public String tzGetHtmlContent(String strParams) {
		String applicationCenterHtml = "";
		JacksonUtil jacksonUtil = new JacksonUtil();

		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		try {
			jacksonUtil.json2Map(strParams);
			String strSiteId = "";
			String strAreaId="";
//			if (jacksonUtil.containsKey("areaId")) {
//				strAreaId = jacksonUtil.getString("areaId");
//			}
//
//			if (strAreaId == null || "".equals(strAreaId)) {
//				strAreaId = request.getParameter("areaId");
//			}
//			
//			if (jacksonUtil.containsKey("siteId")) {
//				strSiteId = jacksonUtil.getString("siteId");
//			}
//
//			if (strSiteId == null || "".equals(strSiteId)) {
//				strSiteId = request.getParameter("siteId");
//			}
			strSiteId = jacksonUtil.getString("siteId");
			strAreaId = jacksonUtil.getString("areaId");
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
						//根据siteid和areaId得到栏目id;
						String coluSQL = "select TZ_COLU_ID from PS_TZ_SITEI_AREA_T where TZ_SITEI_ID=? and TZ_AREA_ID=?";
						String coluId = jdbcTemplate.queryForObject(coluSQL, new Object[] { strSiteId,strAreaId }, "String");
						
						//报考日历;
						String registerCalendar = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_APPCENTER_MESSAGE", "1",language, "报考日历", "报考日历");
						
						//更多;
						String more = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_APPCENTER_MESSAGE", "2", language, "更多","more");
						//报考时间;
						String date = "";
						//报考地点;
						String place = "";
						
						//报考名称;
						String registerName = "";
						
//						//根据siteid和coluid得到artid
						String artIdSql = "select TZ_ART_ID from PS_TZ_LM_NR_GL_T where TZ_SITE_ID=? and TZ_COLU_ID=?";
						List<Map<String, Object>> artIdList = jdbcTemplate.queryForList(artIdSql,new Object[] { strSiteId,coluId });
						if (artIdList != null && artIdList.size()>0){
							for(int i=0;i<artIdList.size();i++){
								String artId = (String) artIdList.get(i).get("TZ_ART_ID");
								/**报考日历内容类型配置字段 TZ_LONG1：地点  ，TZ_LONG2：名称，TZ_DATE1：时间 */
								String bkrlSql = "select TZ_LONG1,TZ_LONG2,month(TZ_DATE1),day(TZ_DATE1) from PS_TZ_ART_REC_TBL where TZ_ART_ID=? order by ROW_ADDED_DTTM DESC limit 4";
								List<Map<String, Object>> coluMapList = jdbcTemplate.queryForList(bkrlSql,new Object[] { artId });
								if (coluMapList != null && coluMapList.size()>0) {
									for(int j=0;j<artIdList.size();j++){
									//date = (String) coluMap.get("TZ_DATE1");
									place = (String) coluMapList.get(j).get("TZ_LONG1");
									registerName = (String) coluMapList.get(j).get("TZ_LONG2");
									}
								}
								applicationCenterHtml = tzGDObject.getHTMLText("HTML.TZRecruitStuViewBundle.TZ_APPCENTER_BKRL_HTML", registerCalendar,
										more,date,place,"上海");
							}
						}else{
							applicationCenterHtml = tzGDObject.getHTMLText("HTML.TZRecruitStuViewBundle.TZ_APPCENTER_BKRL_NO",registerCalendar);
						}
			return applicationCenterHtml;
		} catch (Exception e) {
   
		}
		return "没有相关数据";
	}
}
