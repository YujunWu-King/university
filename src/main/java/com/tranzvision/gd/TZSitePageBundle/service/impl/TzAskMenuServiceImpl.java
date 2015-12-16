/**
 * 
 */
package com.tranzvision.gd.TZSitePageBundle.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.session.TzGetSetSessionValue;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 原PS：TZ_SITE_DECORATED_APP:TZ_AREA_COLU_CLS
 * 
 * @author SHIHUA
 * @since 2015-12-15
 */
@Service("com.tranzvision.gd.TZSitePageBundle.service.impl.TzAskMenuServiceImpl")
public class TzAskMenuServiceImpl extends FrameworkImpl {

	@Autowired
	private ApplicationContext ctx;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private JacksonUtil jacksonUtil;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzGDObject;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private TzGetSetSessionValue tzGetSetSessionValue;

	@Override
	public String tzGetHtmlContent(String strParams) {

		String strRet = "";

		try {

			String strSiteId = request.getParameter("siteId");
			String strMenuId = request.getParameter("menuId");
			String oprate = request.getParameter("oprate");
			String classid = request.getParameter("classid");

			String comId = "TZ_SITEI_SETED_COM";
			String pageId = "TZ_ASK_MENU_STD";

			if (null != classid && !"".equals(classid)) {
				if (null == oprate || "".equals(oprate)) {
					oprate = "R";
				}
				String sql = "select TZ_COM_ID,TZ_PAGE_ID from PS_TZ_AQ_PAGZC_TBL where TZ_PAGE_REFCODE=?";
				Map<String, Object> mapData = sqlQuery.queryForMap(sql, new Object[] { classid });
				comId = mapData.get("TZ_COM_ID") == null ? comId : String.valueOf(mapData.get("TZ_COM_ID"));
				pageId = mapData.get("TZ_PAGE_ID") == null ? pageId : String.valueOf(mapData.get("TZ_PAGE_ID"));

				Map<String, Object> mapParams = new HashMap<String, Object>();
				mapParams.put("siteId", strSiteId);
				mapParams.put("menuId", strMenuId);
				mapParams.put("oprate", oprate);

				strParams = jacksonUtil.Map2json(mapParams);

			} else {
				jacksonUtil.json2Map(strParams);
				strSiteId = jacksonUtil.getString("siteId");
				strMenuId = jacksonUtil.getString("menuId");
				oprate = jacksonUtil.getString("oprate");
				if (null == oprate || "".equals(oprate)) {
					oprate = "R";
				}
			}

			tzGetSetSessionValue.setTzSiteGloMenuId(strMenuId);

			String userid = tzLoginServiceImpl.getLoginedManagerOprid(request);

			// 判断当前人员是否有权限
			String sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetUserEditPermit");
			String tzUserEditFlg = sqlQuery.queryForObject(sql, new Object[] { userid, comId, pageId }, "String");

			sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetUserDisplayOnlyPermit");
			String tzUserDisplayOnly = sqlQuery.queryForObject(sql, new Object[] { userid, comId, pageId }, "String");

			boolean canShow = true;
			switch (oprate) {
			case "R":
				if (!"1".equals(tzUserDisplayOnly) && !"1".equals(tzUserEditFlg)) {
					canShow = false;
				}
				break;

			case "D":
				if (!"1".equals(tzUserEditFlg)) {
					canShow = false;
				}
				break;

			case "P":
				if (!"1".equals(tzUserEditFlg)) {
					canShow = false;
				}
				break;
			}

			if (!canShow) {
				return "无权访问";
			}

			sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetShowMenuCode");
			String strMenuAppCls = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strMenuId }, "String");
			if (null != strMenuAppCls && !"".equals(strMenuAppCls)) {

				Object appClsObj = ctx.getBean(strMenuAppCls);
				if (appClsObj != null) {

					FrameworkImpl tzFrameworkImplObj = (FrameworkImpl) appClsObj;

					strRet = tzFrameworkImplObj.tzGetHtmlContent(strParams);

					return strRet;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return strRet;

	}

}
