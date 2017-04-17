/**
 * 
 */
package com.tranzvision.gd.TZSitePageBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAccountMgBundle.model.PsTzAqYhxxTbl;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzWebsiteLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdObjectServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cookie.TzCookie;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 原PS：TZ_SITE_DECORATED_APP:TZ_STU_LOGIN_CLS
 * 
 * @author SHIHUA
 * @since 2015-12-21
 */
@Service("com.tranzvision.gd.TZSitePageBundle.service.impl.TzStuLoginedIndexServiceImpl")
public class TzStuLoginedIndexServiceImpl extends FrameworkImpl {

	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private HttpServletResponse response;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private GdObjectServiceImpl gdObjectServiceImpl;

	@Autowired
	private TzWebsiteLoginServiceImpl tzWebsiteLoginServiceImpl;
	
	@Autowired
	private TzCookie tzCookie;

	@Override
	public String tzQuery(String strParams, String[] errMsg) {

		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			jacksonUtil.json2Map(strParams);

			String strTypeflg = jacksonUtil.getString("typeflg");

			if ("welcome".equals(strTypeflg)) {
				
				String strOrgId = jacksonUtil.getString("orgId").toUpperCase();

				String strSiteId = jacksonUtil.getString("siteId");

				if ((null == strOrgId || "".equals(strOrgId) && (null == strSiteId || "".equals(strSiteId)))) {
					strOrgId = tzWebsiteLoginServiceImpl.getLoginedUserOrgid(request);
				}

				if ((null != strOrgId && !"".equals(strOrgId) && (null == strSiteId || "".equals(strSiteId)))) {
					String sql = "select TZ_SITEI_ID from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ENABLE='Y' and TZ_JG_ID=?";
					strSiteId = sqlQuery.queryForObject(sql, new Object[] { strOrgId }, "String");
				}

				if ((null != strSiteId && !"".equals(strSiteId) && (null == strOrgId || "".equals(strOrgId)))) {
					String sql = "select TZ_JG_ID from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ENABLE='Y' and TZ_SITEI_ID=?";
					strOrgId = sqlQuery.queryForObject(sql, new Object[] { strSiteId }, "String");
				}
				
				if((null != strOrgId && !"".equals(strOrgId) && (null != strSiteId && !"".equals(strSiteId)))){
				
				String sql = "select TZ_SITE_LANG from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
				String strLang = sqlQuery.queryForObject(sql, new Object[] { strSiteId }, "String");
				
				String strWelcome = "";
				
				String oprid = tzWebsiteLoginServiceImpl.getLoginedUserOprid(request);
				
				if(null!=oprid && !"".equals(oprid)){
				
					PsTzAqYhxxTbl psTzAqYhxxTbl = tzWebsiteLoginServiceImpl.getLoginedUserInfo(request);
					
					String strUserRealName = psTzAqYhxxTbl.getTzRealname();
					
					if("ENG".equals(strLang)){
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
						strWelcome = "Welcome " + strUserRealName + ", " + dateFormat.format(new Date());
						strWelcome = strWelcome + "&nbsp;&nbsp;&nbsp;&nbsp;<a onclick='Logout()' href='javascript:void(0)'>Logout</a>";
					}else{
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
						strWelcome = "欢迎 " + strUserRealName + "，今天是:" + dateFormat.format(new Date());
						strWelcome = strWelcome + "&nbsp;&nbsp;&nbsp;&nbsp;<a onclick='Logout()' href='javascript:void(0)'>安全退出</a>";
					}
					
				}
				
				mapRet.put("success", "true");
				mapRet.put("welcome", strWelcome);
				
				}else{
				
					errMsg[0] = "1";
					errMsg[1] = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZ_SITE_MESSAGE", "44", "ZHS", "站点异常", "The website is abnormal .");
					mapRet.put("success", "false");
				}
				
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "数据异常！";
			mapRet.put("success", "false");
		}

		strRet = jacksonUtil.Map2json(mapRet);
		
		return strRet;

	}

	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			Date dateNow = new Date();
			String oprid = tzWebsiteLoginServiceImpl.getLoginedUserOprid(request);

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {

				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);
				
				//类型标志;
			      String strFlag = jacksonUtil.getString("typeFlag");
			      
			      String strArtId = jacksonUtil.getString("artId");
			      
			      

			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "";
		}

		return strRet;
	}

}
