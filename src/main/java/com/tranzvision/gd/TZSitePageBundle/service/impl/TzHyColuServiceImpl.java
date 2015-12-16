/**
 * 
 */
package com.tranzvision.gd.TZSitePageBundle.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdObjectServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 原PS：TZ_SITE_DECORATED_APP:TZ_HY_COLU_CLS
 * 
 * @author SHIHUA
 * @since 2015-12-16
 */
@Service("com.tranzvision.gd.TZSitePageBundle.service.impl.TzHyColuServiceImpl")
public class TzHyColuServiceImpl extends FrameworkImpl {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private JacksonUtil jacksonUtil;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzGDObject;

	@Autowired
	private GdObjectServiceImpl gdObjectServiceImpl;

	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		String strRet = "";

		try {

			jacksonUtil.json2Map(strParams);

			String strSiteId = jacksonUtil.getString("siteId");

			String sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteLang");
			String strLang = sqlQuery.queryForObject(sql, new Object[] { strSiteId }, "String");

			String strSignUp = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZ_SITE_MESSAGE", "21",
					strLang, "报名", "Sign Up");

			String strCancel = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZ_SITE_MESSAGE", "22",
					strLang, "撤销", "Cancel");

			String strNum = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZ_SITE_MESSAGE", "23", strLang,
					"第", "No.");

			String strTotal = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZ_SITE_MESSAGE", "24",
					strLang, "共", "Total");

			String strPage = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZ_SITE_MESSAGE", "25", strLang,
					"页", "Page");

			String strMenuId="";

			String strAreaId="";

			String strAreaZone="";

			String strAreaType="";

			String strColuId="";

			String strFrom="";

			String strPhone="";

			int numPageRow = 10;
			
			strFrom = jacksonUtil.getString("qureyFrom");
			
			if("M".equals(strFrom)){
				
				strMenuId = jacksonUtil.getString("menuId");
				sql = "select TZ_MENU_COLUMN from PS_TZ_SITEI_MENU_T where TZ_SITEI_ID =? and TZ_MENU_ID =? and TZ_MENU_STATE = 'Y'";
				strColuId = sqlQuery.queryForObject(sql, new Object[]{strSiteId, strMenuId}, "String");
			}else if("A".equals(strFrom)){
				strAreaId = jacksonUtil.getString("areaId");
		         
		        strAreaZone = jacksonUtil.getString("areaZone");
		         
		        strAreaType = jacksonUtil.getString("areaType");
		        
		        if(null==strAreaId || "".equals(strAreaId)){
		        	sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzAreaIdFromSiteidAreatype");
		        	strAreaId = sqlQuery.queryForObject(sql, new Object[]{strSiteId, strAreaId}, "String");
		        }
		        
		        sql = "SELECT TZ_COLU_ID FROM PS_TZ_SITEI_AREA_T WHERE TZ_SITEI_ID=? AND TZ_AREA_ID=? and TZ_AREA_STATE='Y'";
		        strColuId = sqlQuery.queryForObject(sql, new Object[]{strSiteId, strAreaId}, "String");
			}
			
			int numNowPage = jacksonUtil.getInt("page");
			int numPageSize = jacksonUtil.getInt("pagesize");
			String strType = jacksonUtil.getString("type");
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "";
		}

		return strRet;
	}

}
