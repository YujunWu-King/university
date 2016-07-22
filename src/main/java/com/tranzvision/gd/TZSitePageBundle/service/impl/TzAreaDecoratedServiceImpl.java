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
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 原PS：TZ_SITE_DECORATED_APP:TZ_AREA_DECORATED_CLS
 * 
 * @author SHIHUA
 * @since 2015-12-15
 */
@Service("com.tranzvision.gd.TZSitePageBundle.service.impl.TzAreaDecoratedServiceImpl")
public class TzAreaDecoratedServiceImpl extends FrameworkImpl {

	@Autowired
	private ApplicationContext ctx;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzGDObject;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Override
	public String tzQuery(String strParams, String[] errMsg) {

		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();

		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			jacksonUtil.json2Map(strParams);

			String strSiteId = jacksonUtil.getString("siteId");

			String strAreaId = jacksonUtil.getString("areaId");

			// String strAreaZone = jacksonUtil.getString("areaZone");

			String strAreaType = jacksonUtil.getString("areaType");

			String strAreaTypeId = "";
			if (null != strSiteId && !"".equals(strSiteId)) {
				if (null != strAreaId && !"".equals(strAreaId)) {
					String sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetAreatypeidBySiteidAreaid");
					strAreaTypeId = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strAreaId }, "String");
				} else {
					String sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetAreatypeidBySiteidAreatype");
					strAreaTypeId = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strAreaType }, "String");
				}
			} else {
				mapRet.put("success", false);
				errMsg[0] = "1";
				errMsg[1] = "请先开通站点！";
			}

			if (null != strAreaTypeId && !"".equals(strAreaTypeId)) {
				String sql = "select TZ_AREA_SET_CODE from PS_TZ_SITEI_ATYP_T where TZ_SITEI_ID=? and TZ_AREA_TYPE_ID=?";
				String strAreaAppCls = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strAreaTypeId },
						"String");
				if (null != strAreaAppCls && !"".equals(strAreaAppCls)) {

					Object appClsObj = ctx.getBean(strAreaAppCls);
					if (appClsObj != null) {

						FrameworkImpl tzFrameworkImplObj = (FrameworkImpl) appClsObj;

						strRet = tzFrameworkImplObj.tzQuery(strParams, errMsg);

						errMsg[0] = "0";

						return strRet;
					}

				} else {
					mapRet.put("success", false);
					errMsg[0] = "1";
					errMsg[1] = "请求编辑的区域不存在！";
				}
			} else {
				mapRet.put("success", false);
				errMsg[0] = "1";
				errMsg[1] = "请求编辑的区域不存在！";
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "请求编辑的区域失败!" + e.getMessage();
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

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {

				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				// 类型标志;
				// String strFlag = jacksonUtil.getString("typeFlag");
				// 信息内容;
				Map<String, Object> mapData = jacksonUtil.getMap("data");

				String strSiteId = mapData.get("siteId") == null ? "" : String.valueOf(mapData.get("siteId"));

				String strAreaId = mapData.get("areaId") == null ? "" : String.valueOf(mapData.get("areaId"));

				// String strAreaZone = mapData.get("areaZone") == null ? "" :
				// String.valueOf(mapData.get("areaZone"));

				String strAreaType = mapData.get("areaType") == null ? "" : String.valueOf(mapData.get("areaType"));

				String strAreaTypeId = "";
				if (null != strSiteId && !"".equals(strSiteId)) {
					if (null != strAreaId && !"".equals(strAreaId)) {
						String sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetAreatypeidBySiteidAreaid");
						strAreaTypeId = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strAreaId }, "String");
					} else {
						String sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetAreatypeidBySiteidAreatype");
						strAreaTypeId = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strAreaType }, "String");
					}
				} else {
					mapRet.put("success", false);
					errMsg[0] = "1";
					errMsg[1] = "请先开通站点！";
				}

				if (null != strAreaTypeId && !"".equals(strAreaTypeId)) {
					String sql = "select TZ_AREA_SET_CODE from PS_TZ_SITEI_ATYP_T where TZ_SITEI_ID=? and TZ_AREA_TYPE_ID=?";
					String strAreaAppCls = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strAreaTypeId },
							"String");
					if (null != strAreaAppCls && !"".equals(strAreaAppCls)) {
						Object appClsObj = ctx.getBean(strAreaAppCls);
						if (appClsObj != null) {

							String paramStr = jacksonUtil.Map2json(mapData);
							String[] paramAry = new String[] { paramStr };

							FrameworkImpl tzFrameworkImplObj = (FrameworkImpl) appClsObj;

							strRet = tzFrameworkImplObj.tzUpdate(paramAry, errMsg);
							errMsg[0] = "0";

							return strRet;
						}

					} else {
						mapRet.put("success", false);
						errMsg[0] = "1";
						errMsg[1] = "请求编辑的区域不存在！";
					}
				} else {
					mapRet.put("success", false);
					errMsg[0] = "1";
					errMsg[1] = "请求编辑的区域不存在！";
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "0";
			errMsg[1] = "";
		}

		return strRet;

	}

	@Override
	public String tzGetHtmlContent(String strParams) {

		String strRet = "";

		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			jacksonUtil.json2Map(strParams);

			String strSiteId = jacksonUtil.getString("siteId");

			String strOrgId = jacksonUtil.getString("orgId");

			String strAreaId = jacksonUtil.getString("areaId");

			//String strAreaZone = jacksonUtil.getString("areaZone");

			String strAreaType = jacksonUtil.getString("areaType");

			
			if((null==strOrgId || "".equals(strOrgId)) && (null==strSiteId || "".equals(strSiteId))){
				strOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			}
			
			if((null!=strOrgId && !"".equals(strOrgId)) && (null==strSiteId || "".equals(strSiteId))){
				String sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteidByOrgid");
				strSiteId = sqlQuery.queryForObject(sql, new Object[]{strOrgId}, "String");
			}
			
			String strAreaTypeId = "";
			if (null != strSiteId && !"".equals(strSiteId)) {
				if (null != strAreaId && !"".equals(strAreaId)) {
					String sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetAreatypeidBySiteidAreaid");
					strAreaTypeId = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strAreaId }, "String");
				} else {
					String sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetAreatypeidBySiteidAreatype");
					strAreaTypeId = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strAreaType }, "String");
				}
			} else {
				strRet = "false";
			}
			
			if (null != strAreaTypeId && !"".equals(strAreaTypeId)) {
				String sql = "select TZ_AREA_HTML_CODE from PS_TZ_SITEI_ATYP_T where TZ_SITEI_ID=? and TZ_AREA_TYPE_ID=?";
				String strAreaAppCls = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strAreaTypeId },
						"String");
				if (null != strAreaAppCls && !"".equals(strAreaAppCls)) {

					Object appClsObj = ctx.getBean(strAreaAppCls);
					if (appClsObj != null) {

						TzSiteActionServiceImpl tzSiteActionServiceImplObj = (TzSiteActionServiceImpl) appClsObj;

						strRet = tzSiteActionServiceImplObj.tzGetHtmlContent(strParams);

					}

				} else {
					strRet = "false";
				}
			} else {
				strRet = "false";
			}

		} catch (Exception e) {
			e.printStackTrace();
			strRet = "false";
		}

		return strRet;
	}

}
