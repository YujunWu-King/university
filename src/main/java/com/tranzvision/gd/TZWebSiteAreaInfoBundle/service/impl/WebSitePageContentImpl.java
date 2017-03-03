package com.tranzvision.gd.TZWebSiteAreaInfoBundle.service.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 招生报名系统首页，单页显示数据
 * 
 * @author 琚峰
 * @since 2017-2-10
 */
@Service("com.tranzvision.gd.TZWebSiteAreaInfoBundle.service.impl.WebSitePageContentImpl")
public class WebSitePageContentImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TZGDObject tzGDObject;
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	@Autowired
	private SiteRepCssServiceImpl siteRepCssServiceImpl;

	@Override
	public String tzGetHtmlContent(String strParams) {
		JacksonUtil jacksonUtil = new JacksonUtil();
//		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		try {
			jacksonUtil.json2Map(strParams);
			String strSiteId = "";
			String strAreaId = "";
			String strColumnId = "";
			if (jacksonUtil.containsKey("siteId")) {
				strSiteId = jacksonUtil.getString("siteId");
			}

			if (strSiteId == null || "".equals(strSiteId)) {
				strSiteId = request.getParameter("siteId");
			}
			if (jacksonUtil.containsKey("areaId")) {
				strAreaId = jacksonUtil.getString("areaId");
			}

			if (strAreaId == null || "".equals(strAreaId)) {
				strAreaId = request.getParameter("areaId");
			}
			if (jacksonUtil.containsKey("columnId")) {
				strColumnId = jacksonUtil.getString("columnId");
			}

			if (strColumnId == null || "".equals(strColumnId)) {
				strColumnId = request.getParameter("columnId");
			}
			// 项目跟目录;
//			String rootPath = request.getContextPath();
			// 通用链接;
			String ZSGL_URL = request.getContextPath() + "/dispatcher";
			
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
//			//获取栏目编号;
//			String columnSQL = "SELECT TZ_COLU_ID FROM PS_TZ_SITEI_AREA_T WHERE TZ_SITEI_ID=? AND TZ_AREA_ID=?";
//			String columnId = jdbcTemplate.queryForObject(columnSQL, new Object[] { strSiteId,strAreaId }, "String");
//			String registerCalListHtml = "";
//			String registerCalContentHtml = "";
//			if(columnId!=null&&!"".equals(columnId)){
//				String[] columns = columnId.split(",");
//			for(int i=0;i<columns.length;i++){
//				String currentColumnId = columns[i];
//			
//			//获取栏目名称;
//			String columnNameSQL = "SELECT TZ_COLU_NAME FROM PS_TZ_SITEI_COLU_T WHERE TZ_SITEI_ID=? and TZ_COLU_ID=?";
//			String columnName = jdbcTemplate.queryForObject(columnNameSQL, new Object[] { strSiteId,currentColumnId }, "String");
//			// 招生网站单页列表;
//			registerCalListHtml = tzGDObject.getHTMLText("HTML.TZWebSiteAreaInfoBundle.TZ_SITE_MORE_BKRL_LI_HTML",true,columnName);
//			}
//			}
			//获取栏目名称;
			String columnNameSQL = "SELECT TZ_COLU_NAME FROM PS_TZ_SITEI_COLU_T WHERE TZ_SITEI_ID=? and TZ_COLU_ID=?";
			String columnName = jdbcTemplate.queryForObject(columnNameSQL, new Object[] { strSiteId,strColumnId }, "String");
			String registerCalListHtml = "";
			String registerCalContentHtml = "";

			registerCalListHtml = tzGDObject.getHTMLText("HTML.TZWebSiteAreaInfoBundle.TZ_SITE_MORE_BKRL_LI_HTML",columnName);
			// 展示页面;
			registerCalContentHtml = tzGDObject.getHTMLText("HTML.TZWebSiteAreaInfoBundle.TZ_SITE_MORE_BKRL_HTML",
					request.getContextPath(), ZSGL_URL, strCssDir, registerCalListHtml, str_jg_id, strSiteId);
			
			registerCalContentHtml = siteRepCssServiceImpl.repTitle(registerCalContentHtml, strSiteId);
			registerCalContentHtml = siteRepCssServiceImpl.repCss(registerCalContentHtml, strSiteId);
			return registerCalContentHtml;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}