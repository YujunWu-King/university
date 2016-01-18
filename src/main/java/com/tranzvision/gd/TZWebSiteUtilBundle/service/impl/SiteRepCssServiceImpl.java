package com.tranzvision.gd.TZWebSiteUtilBundle.service.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 原ps包：TZ_SITE_UTIL_APP
 * @author tang
 * 站点页面设置，替换css
 * 20151210
 */
@Service("com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl")
public class SiteRepCssServiceImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	@Autowired
	private HttpServletRequest request;
	
	//替换title; PS类：TZ_SITE_REP_CSS.repTitle;
	public String repTitle(String strConent, String strSiteId){
		if(strSiteId == null || "".equals(strSiteId) || strConent == null || "".equals(strConent)){
			return strConent;
		}
		String sql = "SELECT TZ_SITEI_NAME FROM PS_TZ_SITEI_DEFN_T WHERE TZ_SITEI_ID=? AND TZ_SITEI_ENABLE='Y'";
		String strSiteName = jdbcTemplate.queryForObject(sql, new Object[]{strSiteId},"String");
		if(strSiteName != null){
			int numCharstart = strConent.indexOf("<title>");
			if(numCharstart >= 0){
				int numCharend = strConent.indexOf("</title>",numCharstart);
				if(numCharend > numCharstart){
					String strCharsub = strConent.substring(numCharstart, numCharend + 8);
					strConent = strConent.replaceAll(strCharsub, "<title>" + strSiteName + "</title>");
				}
			}
		}
		return strConent;
	}
	
	//替换css; PS类：TZ_SITE_REP_CSS.repCss;
	public String repCss(String strConent, String strSiteId){
		if(strSiteId == null || "".equals(strSiteId) || strConent == null || "".equals(strConent)){
			return strConent;
		}
		String sql = "SELECT TZ_SKIN_STOR,TZ_JG_ID FROM PS_TZ_SITEI_DEFN_T WHERE TZ_SITEI_ID=?";
		Map<String, Object> map = jdbcTemplate.queryForMap(sql,new Object[]{strSiteId});
		if(map != null ){
			String skinstor = (String)map.get("TZ_SKIN_STOR");
			String strJgId = (String)map.get("TZ_JG_ID");
			String websitePath = getSysHardCodeVal.getWebsiteCssPath();
			String strCssDir = request.getContextPath();
			if("".equals(skinstor) || skinstor == null){
				strCssDir = strCssDir + websitePath + "/" + strJgId.toLowerCase() + "/" + strSiteId + "/" ;
			}else{
				strCssDir = strCssDir + websitePath + "/" + strJgId.toLowerCase() + "/" + strSiteId + "/" + skinstor + "/";
			}
			
			if(strJgId != null && !"".equals(strJgId)){
				strConent = strConent.replaceAll("page_stylecss",  strCssDir + "style_" + strJgId.toLowerCase() + ".css");
			}else{
				strConent = strConent.replaceAll("page_stylecss",  strCssDir + "style.css");
			}
			
		}
		
		return strConent;
	}
	
	//替换css; PS类：TZ_SITE_REP_CSS.repCssByJg;
	public String repCssByJg(String strConent, String strJgId){
		
		if(strJgId == null || "".equals(strJgId) || strConent == null || "".equals(strConent)){
			return strConent;
		}
		String sql = "SELECT TZ_SITEI_ID,TZ_SKIN_STOR FROM PS_TZ_SITEI_DEFN_T WHERE TZ_JG_ID=? and TZ_SITEI_ENABLE='Y' order by (cast(TZ_SITEI_ID as signed)) desc limit 0,1";
		Map<String, Object> map = jdbcTemplate.queryForMap(sql,new Object[]{strJgId});
		if(map != null ){
			String skinstor = (String)map.get("TZ_SKIN_STOR");
			String strSiteId = (String)map.get("TZ_SITEI_ID");
			String websitePath = getSysHardCodeVal.getWebsiteFileUploadPath();
			String strCssDir = request.getContextPath();
			if("".equals(skinstor) || skinstor == null){
				strCssDir = strCssDir + websitePath + "/" + strJgId + "/" + strSiteId + "/" ;
			}else{
				strCssDir = strCssDir + websitePath + "/" + strJgId + "/" + strSiteId + "/" + skinstor + "/";
			}
			
			if(strJgId != null && !"".equals(strJgId)){
				strConent = strConent.replaceAll("page_stylecss",  strCssDir + "style_" + strJgId.toLowerCase() + ".css");
			}else{
				strConent = strConent.replaceAll("page_stylecss",  strCssDir + "style.css");
			}
			
		}
		
		return strConent;
	}
	
	//替换站点id值; PS类：TZ_SITE_REP_CSS.repSiteid;
	public String repSiteid(String strConent, String strSiteId){
		if(strConent == null || "".equals(strConent)){
			return strConent;
		}
		
		int numCharstart = strConent.indexOf("<input id=\"siteid\" name=\"siteid\"");
		if(numCharstart >= 0){
			int numCharend = strConent.indexOf(">",numCharstart);
			if(numCharend > numCharstart){
				String strCharsub = strConent.substring(numCharstart, numCharend + 1);
				strConent = strConent.replaceAll(strCharsub,  "<input id=\"siteid\" name=\"siteid\" type=\"hidden\" value=\"" + strSiteId + "\">");
			}
		}
		
		return strConent;
	}
	
	
	//替换机构id值; PS类：TZ_SITE_REP_CSS.repJgid;
	public String repJgid(String strConent, String strJgId){
		if(strConent == null || "".equals(strConent)){
				return strConent;
		}
			
		int numCharstart = strConent.indexOf("<input id=\"jgid\" name=\"jgid\"");
		if(numCharstart >= 0){
			int numCharend = strConent.indexOf(">",numCharstart);
			if(numCharend > numCharstart){
				String strCharsub = strConent.substring(numCharstart, numCharend + 1);
				strConent = strConent.replaceAll(strCharsub,  "<input id=\"jgid\" name=\"jgid\" type=\"hidden\" value=\"" + strJgId + "\">");
			}
		}
			
		return strConent;
	}
	
	//替换language值; PS类：TZ_SITE_REP_CSS.repLang;
	public String repLang(String strConent, String strLang){
		if(strConent == null || "".equals(strConent)){
				return strConent;
		}
				
		int numCharstart = strConent.indexOf("<input id=\"lang\" name=\"lang\"");
		if(numCharstart >= 0){
			int numCharend = strConent.indexOf(">",numCharstart);
			if(numCharend > numCharstart){
				String strCharsub = strConent.substring(numCharstart, numCharend + 1);
				strConent = strConent.replaceAll(strCharsub, "<input id=\"lang\" name=\"lang\" type=\"hidden\" value=\"" + strLang + "\">");
			}
		}
				
		return strConent;
	}
	
	//替换menuname值; PS类：TZ_SITE_REP_CSS.repMenuName;
	public String repMenuName(String strConent, String strMenuName){
		if(strConent == null || "".equals(strConent)){
				return strConent;
		}
					
		int numCharstart = strConent.indexOf("<span id=\"menuName\">");
		if(numCharstart >= 0){
			int numCharend = strConent.indexOf("</span>",numCharstart);
			if(numCharend > numCharstart){
				String strCharsub = strConent.substring(numCharstart, numCharend + 7);
				strConent = strConent.replaceAll(strCharsub, "<span id=\"menuName\">" + strMenuName + "</span>");
			}
		}
					
		return strConent;
	}
	
	//替换repOpr值; PS类：TZ_SITE_REP_CSS.repOpr;
	public String repOpr(String strConent, String strOperator){
		if(strConent == null || "".equals(strConent)){
			return strConent;
		}
						
		int numCharstart = strConent.indexOf("<input id=\"operator\" name=\"operator\"");
		if(numCharstart >= 0){
			int numCharend = strConent.indexOf(">",numCharstart);
			if(numCharend > numCharstart){
				String strCharsub = strConent.substring(numCharstart, numCharend + 1);
				strConent = strConent.replaceAll(strCharsub, "<input id=\"operator\" name=\"operator\" type=\"hidden\" value=\"" + strOperator + "\">");
			}
		}
						
		return strConent;
	}
	
	//替换repWelcome值; PS类：TZ_SITE_REP_CSS.repWelcome;
	public String repWelcome(String strConent, String strWelcome){
		if(strConent == null || "".equals(strConent)){
			return strConent;
		}
							
		int numCharstart = strConent.indexOf("<span id=\"welcome\">");
		if(numCharstart >= 0){
			int numCharend = strConent.indexOf("</span>",numCharstart);
			if(numCharend > numCharstart){
				String strCharsub = strConent.substring(numCharstart, numCharend + 7);
				strConent = strConent.replaceAll(strCharsub, strWelcome);
			}
		}
							
		return strConent;
	}
	
	//替换sdkbar值; PS类：TZ_SITE_REP_CSS.repSdkbar;
	public String repSdkbar(String strConent, String strSdkbar){
		if(strConent == null || "".equals(strConent)){
			return strConent;
		}
		
		int numCharstart = strConent.indexOf("<div id=\"sdkbar\">");
		if(numCharstart >= 0){
			int numCharend = strConent.indexOf("</div>",numCharstart);
			//numCharend = strConent.indexOf("</div>",numCharend + 6);
			if(numCharend > numCharstart){
				String startContent = strConent.substring(0,numCharstart);
				String endContent = strConent.substring(numCharend + 6, strConent.length());
				strConent = startContent + strSdkbar + endContent;
			}
		}
								
		return strConent;
	}
}
