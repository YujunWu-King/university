/**
 * 
 */
package com.tranzvision.gd.TZSitePageBundle.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 原PS：TZ_SITE_DECORATED_APP:TZ_MENU_SHOW_CLS
 * 
 * @author SHIHUA
 * @since 2015-12-18
 */
@Service("com.tranzvision.gd.TZSitePageBundle.service.impl.TzMenuShowServiceImpl")
public class TzMenuShowServiceImpl extends FrameworkImpl {

	@Autowired
	private JacksonUtil jacksonUtil;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzGDObject;
	
	@Autowired
	private SiteRepCssServiceImpl siteRepCssServiceImpl;

	@Override
	public String tzGetHtmlContent(String strParams) {
		
		String strRet = "";
		
		try{
			
			jacksonUtil.json2Map(strParams);
			
			String strSiteId = jacksonUtil.getString("siteId");
			String strMenuId = jacksonUtil.getString("menuId");

			if ((null != strSiteId && !"".equals(strSiteId)) && (null != strMenuId && !"".equals(strMenuId))) {

				String sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteTplPcCodeByMenuid");
				String strResultContent = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strMenuId }, "String");

				if (null != strResultContent) {

					strResultContent=siteRepCssServiceImpl.repTitle(strResultContent, strSiteId);
					strResultContent=siteRepCssServiceImpl.repTitle(strResultContent, strSiteId);
					
					return strResultContent;

				} else {
					return "内容不存在！";
				}

			} else {
				return "参数错误！";
			}
			
		}catch(Exception e){
			e.printStackTrace();
			strRet = "异常退出！";
		}
		
		
		return strRet;
		
	}
	
}
