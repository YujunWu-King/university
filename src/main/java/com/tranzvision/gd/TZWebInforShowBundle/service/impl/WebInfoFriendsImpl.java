package com.tranzvision.gd.TZWebInforShowBundle.service.impl;

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
 * 友情链接区域加载显示
 *
 */
@Service("com.tranzvision.gd.TZWebInforShowBundle.service.impl.WebInfoFriendsImpl")
public class WebInfoFriendsImpl extends FrameworkImpl {
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
		String applicationCenterHtml = "";
		JacksonUtil jacksonUtil = new JacksonUtil();

		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		try {
			jacksonUtil.json2Map(strParams);
			String strSiteId = "";
//			if (jacksonUtil.containsKey("siteId")) {
//				strSiteId = jacksonUtil.getString("siteId");
//			}
//
//			if (strSiteId == null || "".equals(strSiteId)) {
//				strSiteId = request.getParameter("siteId");
//			}
			String strAreaId="";
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
			String coluSQL = "select TZ_COLU_ID from PS_TZ_SITEI_AREA_T where TZ_SITEI_ID=? and TZ_AREA_ID=?";
			String coluId = jdbcTemplate.queryForObject(coluSQL, new Object[] { strSiteId,strAreaId }, "String");
			// 友情链接;
			String friends = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_WEBINFOSHOW_MESSAGE", "1",language, "友情链接", "友情链接");
			
			//根据siteid和areaId得到栏目id;
			String artIdSql = "select TZ_ART_ID from PS_TZ_LM_NR_GL_T where TZ_SITE_ID=? and TZ_COLU_ID=?";
			String artId = jdbcTemplate.queryForObject(artIdSql, new Object[]{strSiteId,coluId}, "String");
			List<Map<String, Object>> artIdList = jdbcTemplate.queryForList(artIdSql,new Object[] { strSiteId,coluId });
			if (artIdList != null && artIdList.size()>0){
				for(int i=0;i<artIdList.size();i++){
					artId = (String) artIdList.get(i).get("TZ_ART_ID");
					/**友情链接内容类型配置字段 TZ_LONG1：友情链接 */
					String friendsLinkName1 ="";
					String friendsLinkName2 ="";
					String friendsLinkName3 ="";
					String friendsLinkName4 ="";
					String friendsLink1 ="";
					String friendsLink2 ="";
					String friendsLink3 ="";
					String friendsLink4 ="";
					String friendsSql = "select TZ_LONG1,TZ_OUT_ART_URL from PS_TZ_ART_REC_TBL where TZ_ART_ID=? order by ROW_ADDED_DTTM DESC limit 4";
					List<Map<String, Object>> coluMapList = jdbcTemplate.queryForList(friendsSql,new Object[] { artId });
					if (coluMapList != null && coluMapList.size()>0) {
						for(int j=0;j<coluMapList.size();j++){
						friendsLinkName1 = (String) coluMapList.get(j).get("TZ_LONG1");
						friendsLink1 = (String) coluMapList.get(j).get("TZ_OUT_ART_URL");
//						friendsLink2 = (String) coluMapList.get(2).get("TZ_LONG1");
//						friendsLink3 = (String) coluMapList.get(3).get("TZ_LONG1");
//						friendsLink4 = (String) coluMapList.get(4).get("TZ_LONG1");
						}
					}
					applicationCenterHtml = tzGDObject.getHTMLText("HTML.TZWebInforShowBundle.TZ_WEBINFO_FRIENDS",
							friends,friendsLink1,friendsLinkName1,friendsLink2,friendsLinkName2,friendsLink3,friendsLinkName3,friendsLink4,friendsLinkName4);
				}
			}
			return applicationCenterHtml;
			
		} catch (Exception e) {

		}
		return "没有显示的数据";
	}
}