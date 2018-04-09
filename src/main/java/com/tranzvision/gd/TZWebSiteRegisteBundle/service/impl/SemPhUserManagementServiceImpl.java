package com.tranzvision.gd.TZWebSiteRegisteBundle.service.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiDefnTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiDefnT;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 
 * @author yuds复制原有UserManagementServiceImpl
 */
@Service("com.tranzvision.gd.TZWebSiteRegisteBundle.service.impl.SemPhUserManagementServiceImpl")
public class SemPhUserManagementServiceImpl extends FrameworkImpl { 
	@Autowired
	private TZGDObject tzGdObject;
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private SiteRepCssServiceImpl objRep;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private PsTzSiteiDefnTMapper psTzSiteiDefnTMapper;		

	@Override
	public String tzGetHtmlContent(String strParams) {
		try {
			String strSiteId = request.getParameter("siteId");
			String strResultConten = "";

			String contextPath = request.getContextPath();
			String commonUrl = contextPath + "/dispatcher";
			
			String language = "", jgId = "";
			String siteSQL = "SELECT TZ_JG_ID,TZ_SITE_LANG,TZ_SKIN_ID FROM PS_TZ_SITEI_DEFN_T WHERE TZ_SITEI_ID=? AND TZ_SITEI_ENABLE='Y'";
			Map<String, Object> siteMap = jdbcTemplate.queryForMap(siteSQL, new Object[] { strSiteId });
			if (siteMap != null) {
				language = (String) siteMap.get("TZ_SITE_LANG");
				jgId = (String) siteMap.get("TZ_JG_ID");
				if (language == null || "".equals(language)) {
					language = "ZHS";
				}
				if (jgId == null || "".equals(jgId)) {
					jgId = "ADMIN";
				}
			}
			String JGID = sqlQuery.queryForObject("select TZ_JG_ID from PS_TZ_SITEI_DEFN_T WHERE TZ_SITEI_ID=?",new Object[]{strSiteId},"String");
			
			if (JGID.equals("SEM")) {
				JGID="";
			} else {
				JGID.toLowerCase();
			}
			String strHeadHtml = tzGdObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_MOBILE_ZHGL_HEAD_HTML",contextPath,commonUrl,jgId,strSiteId,language,JGID);
			String indexUrl = commonUrl + "?classid=mIndex&siteId=" + strSiteId;
			String strMainHtml = tzGdObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_MOBILE_ZHGL_HTML",contextPath,indexUrl);

			String strOrgId = "";
			String strLang = "";

			PsTzSiteiDefnT psTzSiteiDefnT = psTzSiteiDefnTMapper.selectByPrimaryKey(strSiteId);
			if (psTzSiteiDefnT != null) {
				strOrgId = psTzSiteiDefnT.getTzJgId();
				strLang = psTzSiteiDefnT.getTzSiteLang();
			} else {
				return "站点不存在";
			}

			try {		
				String JGID = sqlQuery.queryForObject("select TZ_JG_ID from PS_TZ_SITEI_DEFN_T WHERE TZ_SITEI_ID=?",new Object[]{strSiteId},"String");
				
				if (JGID.equals("SEM")) {
					JGID="";
				} else {
					JGID.toLowerCase();
				}
				strResultConten = tzGdObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_MOBILE_BASE_HTML", "",contextPath, strHeadHtml,strSiteId,"5", strMainHtml,JGID);
			} catch (TzSystemException e) {
				e.printStackTrace();
				return "【TZ_MOBILE_BASE_HTML】html对象未定义";
			}

			strResultConten = objRep.repTitle(strResultConten, strSiteId);
			strResultConten = objRep.repCss(strResultConten, strSiteId);

			strResultConten = objRep.repSiteid(strResultConten, strSiteId);
			strResultConten = objRep.repJgid(strResultConten, strOrgId);
			strResultConten = objRep.repLang(strResultConten, strLang);

			return strResultConten;

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

}
