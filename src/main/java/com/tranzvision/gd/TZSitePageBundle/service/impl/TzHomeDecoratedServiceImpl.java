/**
 * 
 */
package com.tranzvision.gd.TZSitePageBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiDefnTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiDefnTWithBLOBs;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.session.TzGetSetSessionValue;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 原PS：TZ_SITE_DECORATED_APP:TZ_HOME_DECORATED_CLS
 * 
 * @author SHIHUA
 * @since 2015-12-16
 */
@Service("com.tranzvision.gd.TZSitePageBundle.service.impl.TzHomeDecoratedServiceImpl")
public class TzHomeDecoratedServiceImpl extends FrameworkImpl {

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

	@Autowired
	private TzGetSetSessionValue tzGetSetSessionValue;

	@Autowired
	private SiteRepCssServiceImpl siteRepCssServiceImpl;

	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	@Autowired
	private PsTzSiteiDefnTMapper psTzSiteiDefnTMapper;

	@Override
	public String tzGetHtmlContent(String strParams) {

		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			String strSiteId = request.getParameter("siteId");
			String oprate = request.getParameter("oprate");
			String classid = request.getParameter("classid");

			String comId = "TZ_HOME_SETED_COM";
			String pageId = "TZ_HOME_SETED_STD";

			if (null != classid && !"".equals(classid)) {
				String sql = "select TZ_COM_ID,TZ_PAGE_ID from PS_TZ_AQ_PAGZC_TBL where TZ_PAGE_REFCODE=?";
				Map<String, Object> mapData = sqlQuery.queryForMap(sql, new Object[] { classid });
				comId = mapData.get("TZ_COM_ID") == null ? comId : String.valueOf(mapData.get("TZ_COM_ID"));
				pageId = mapData.get("TZ_PAGE_ID") == null ? pageId : String.valueOf(mapData.get("TZ_PAGE_ID"));

			} else {
				jacksonUtil.json2Map(strParams);
				strSiteId = jacksonUtil.getString("siteId");
				oprate = jacksonUtil.getString("oprate");
			}

			if (null == oprate || "".equals(oprate)) {
				oprate = "R";
			}

			String userid = tzLoginServiceImpl.getLoginedManagerOprid(request);

			// 判断当前人员是否有权限
			String sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetUserEditPermit");
			String tzUserEditFlg = sqlQuery.queryForObject(sql, new Object[] { userid, comId, pageId }, "String");

			sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetUserDisplayOnlyPermit");
			String tzUserDisplayOnly = sqlQuery.queryForObject(sql, new Object[] { userid, comId, pageId }, "String");

			sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetMenuidBySiteid");
			String strMenuId = sqlQuery.queryForObject(sql, new Object[] { strSiteId }, "String");

			tzGetSetSessionValue.setTzSiteGloMenuId(strMenuId);

			sql = "select TZ_JG_ID,TZ_SKIN_STOR,TZ_INDEX_PRECODE,TZ_INDEX_SAVECODE,TZ_INDEX_PUBCODE,TZ_SITE_LANG from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
			Map<String, Object> mapData = sqlQuery.queryForMap(sql, new Object[] { strSiteId });

			if (mapData != null) {
				String orgid = mapData.get("TZ_JG_ID") == null ? ""
						: String.valueOf(mapData.get("TZ_JG_ID")).toLowerCase();
				String strResultContent = "";
				boolean canShow = true;
				switch (oprate) {
				case "R":
					if (!"1".equals(tzUserDisplayOnly) && !"1".equals(tzUserEditFlg)) {
						canShow = false;
					} else {
						/*
						strResultContent = mapData.get("TZ_INDEX_PUBCODE") == null ? ""
								: String.valueOf(mapData.get("TZ_INDEX_PUBCODE"));
						*/
						String indexUrl = request.getContextPath() + "/site/index/" + orgid + "/" + strSiteId;
						strResultContent = tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzWinLocationAndOpenWin", "_self", indexUrl);
					}
					break;

				case "D":
					if (!"1".equals(tzUserEditFlg)) {
						canShow = false;
					} else {
						strResultContent = mapData.get("TZ_INDEX_SAVECODE") == null ? ""
								: String.valueOf(mapData.get("TZ_INDEX_SAVECODE"));
					}
					break;

				case "P":
					if (!"1".equals(tzUserEditFlg)) {
						canShow = false;
					} else {
						strResultContent = mapData.get("TZ_INDEX_PRECODE") == null ? ""
								: String.valueOf(mapData.get("TZ_INDEX_PRECODE"));
					}
					break;
				}

				if (!canShow) {
					return "无权访问";
				}

				if(!"R".equals(oprate)){
					String siteLang = mapData.get("TZ_SITE_LANG") == null ? ""
							: String.valueOf(mapData.get("TZ_SITE_LANG"));
	
					String strCssDir = getSysHardCodeVal.getWebsiteCssPath() + "/" + orgid + "/" + strSiteId.toLowerCase();
					String strCssFilePath = strCssDir + "/style_" + orgid + ".css";
	
					strResultContent = strResultContent.replace("page_stylecss", strCssFilePath);
	
					strResultContent = siteRepCssServiceImpl.repTitle(strResultContent, strSiteId);
					strResultContent = siteRepCssServiceImpl.repCss(strResultContent, strSiteId);
					strResultContent = siteRepCssServiceImpl.repSiteid(strResultContent, strSiteId);
					strResultContent = siteRepCssServiceImpl.repJgid(strResultContent, orgid.toUpperCase());
					strResultContent = siteRepCssServiceImpl.repLang(strResultContent, siteLang.toUpperCase());
					strResultContent = siteRepCssServiceImpl.repOpr(strResultContent, oprate.toUpperCase());
				}
				return strResultContent;
			}

		} catch (Exception e) {
			e.printStackTrace();
			strRet = e.getMessage();
		}

		return strRet;

	}

	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {

				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				// 类型标志;
				String strFlag = jacksonUtil.getString("typeFlag");
				// 信息内容;
				Map<String, Object> mapData = jacksonUtil.getMap("data");

				switch (strFlag) {
				case "save":
					strRet = this.saveHomepage(mapData, errMsg);
					break;
				case "release":
					strRet = this.releaseHomepage(mapData, errMsg);
					break;
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.getMessage();
		}

		return strRet;
	}

	@SuppressWarnings("unchecked")
	private String saveHomepage(Map<String, Object> mapData, String[] errMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			// String strSiteIdSrc = mapData.get("siteId") == null ? "" :
			// String.valueOf(mapData.get("siteId"));
			String strSaveContent = mapData.get("savecontent") == null ? ""
					: String.valueOf(mapData.get("savecontent"));
			String strSaveContent2 = mapData.get("savecontent") == null ? ""
					: String.valueOf(mapData.get("savecontent"));
			String strDecoratedplugincss = mapData.get("decoratedplugincss") == null ? ""
					: String.valueOf(mapData.get("decoratedplugincss"));
			String strDecoratedpluginjs = mapData.get("decoratedpluginjs") == null ? ""
					: String.valueOf(mapData.get("decoratedpluginjs"));
			String strDecoratedpluginbar = mapData.get("decoratedpluginbar") == null ? ""
					: String.valueOf(mapData.get("decoratedpluginbar"));
			// String strPagetype = mapData.get("pagetype") == null ? "" :
			// String.valueOf(mapData.get("pagetype"));

			ArrayList<Map<String, Object>> listParams = (ArrayList<Map<String, Object>>) mapData.get("dataArea");

			String strSiteId = "";
			for (Map<String, Object> mapParam : listParams) {
				strSiteId = mapParam.get("siteId") == null ? "" : String.valueOf(mapData.get("siteId"));

				String strAreaId = mapParam.get("areaId") == null ? "" : String.valueOf(mapData.get("areaId"));

				// String strAreaZone = mapParam.get("areaZone") == null ? "" :
				// String.valueOf(mapData.get("areaZone"));

				String strAreaType = mapParam.get("areaType") == null ? "" : String.valueOf(mapData.get("areaType"));

				String strAreaTypeId = "";
				if ("".equals(strAreaId)) {
					String sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetAreatypeidBySiteidAreaid");
					strAreaTypeId = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strAreaId }, "String");
				} else {
					String sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetAreatypeidBySiteidAreatype");
					strAreaTypeId = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strAreaType }, "String");
				}

				if (null != strAreaTypeId && !"".equals(strAreaTypeId)) {
					String sql = "select TZ_AREA_SET_CODE from PS_TZ_SITEI_ATYP_T where TZ_SITEI_ID=? and TZ_AREA_TYPE_ID=?";
					String strAreaAppCls = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strAreaType },
							"String");
					if (null != strAreaAppCls && !"".equals(strAreaAppCls)) {
						Object objAppCls = ctx.getBean(strAreaAppCls);
						if (null != objAppCls) {

							String[] aryParam = new String[] { jacksonUtil.Map2json(mapParam) };

							FrameworkImpl objFrameworkImpl = (FrameworkImpl) objAppCls;
							objFrameworkImpl.tzUpdate(aryParam, errMsg);
						}
					}
				}

			}

			if (null != strSaveContent && !"".equals(strSaveContent)) {

				strSaveContent = strSaveContent.replace(strDecoratedplugincss, "");
				strSaveContent = strSaveContent.replace(strDecoratedpluginjs, "");
				strSaveContent = strSaveContent.replace(strDecoratedpluginbar, "");

				PsTzSiteiDefnTWithBLOBs psTzSiteiDefnTWithBLOBs = new PsTzSiteiDefnTWithBLOBs();
				psTzSiteiDefnTWithBLOBs.setTzSiteiId(strSiteId);
				psTzSiteiDefnTWithBLOBs.setTzIndexPrecode(strSaveContent);
				psTzSiteiDefnTWithBLOBs.setTzIndexSavecode(strSaveContent2);

				String sql = "select 'Y' from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
				String recExists = sqlQuery.queryForObject(sql, new Object[] { strSiteId }, "String");

				if ("Y".equals(recExists)) {
					psTzSiteiDefnTMapper.updateByPrimaryKeySelective(psTzSiteiDefnTWithBLOBs);
				} else {
					psTzSiteiDefnTMapper.insertSelective(psTzSiteiDefnTWithBLOBs);
				}

				mapRet.put("success", true);
				errMsg[0] = "0";
				errMsg[1] = "已保存";
				strRet = jacksonUtil.Map2json(mapRet);
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "保存失败！" + e.getMessage();
			mapRet.put("success", false);
			strRet = jacksonUtil.Map2json(mapRet);
		}

		return strRet;

	}

	@SuppressWarnings("unchecked")
	private String releaseHomepage(Map<String, Object> mapData, String[] errMsg) {

		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			// String strSiteIdSrc = mapData.get("siteId") == null ? "" :
			// String.valueOf(mapData.get("siteId"));
			String strSaveContent = mapData.get("savecontent") == null ? ""
					: String.valueOf(mapData.get("savecontent"));
			String strDecoratedplugincss = mapData.get("decoratedplugincss") == null ? ""
					: String.valueOf(mapData.get("decoratedplugincss"));
			String strDecoratedpluginjs = mapData.get("decoratedpluginjs") == null ? ""
					: String.valueOf(mapData.get("decoratedpluginjs"));
			String strDecoratedpluginbar = mapData.get("decoratedpluginbar") == null ? ""
					: String.valueOf(mapData.get("decoratedpluginbar"));
			// String strPagetype = mapData.get("pagetype") == null ? "" :
			// String.valueOf(mapData.get("pagetype"));

			ArrayList<Map<String, Object>> listParams = (ArrayList<Map<String, Object>>) mapData.get("dataArea");

			String strSiteId = "";
			for (Map<String, Object> mapParam : listParams) {
				strSiteId = mapParam.get("siteId") == null ? "" : String.valueOf(mapData.get("siteId"));

				String strAreaId = mapParam.get("areaId") == null ? "" : String.valueOf(mapData.get("areaId"));

				// String strAreaZone = mapParam.get("areaZone") == null ? "" :
				// String.valueOf(mapData.get("areaZone"));

				String strAreaType = mapParam.get("areaType") == null ? "" : String.valueOf(mapData.get("areaType"));

				String strAreaTypeId = "";
				if ("".equals(strAreaId)) {
					String sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetAreatypeidBySiteidAreaid");
					strAreaTypeId = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strAreaId }, "String");
				} else {
					String sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetAreatypeidBySiteidAreatype");
					strAreaTypeId = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strAreaType }, "String");
				}

				if (null != strAreaTypeId && !"".equals(strAreaTypeId)) {
					String sql = "select TZ_AREA_SET_CODE from PS_TZ_SITEI_ATYP_T where TZ_SITEI_ID=? and TZ_AREA_TYPE_ID=?";
					String strAreaAppCls = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strAreaType },
							"String");
					if (null != strAreaAppCls && !"".equals(strAreaAppCls)) {
						Object objAppCls = ctx.getBean(strAreaAppCls);
						if (null != objAppCls) {

							String[] aryParam = new String[] { jacksonUtil.Map2json(mapParam) };

							FrameworkImpl objFrameworkImpl = (FrameworkImpl) objAppCls;
							objFrameworkImpl.tzUpdate(aryParam, errMsg);
						}
					}
				}

			}

			if (null != strSaveContent && !"".equals(strSaveContent)) {

				strSaveContent = strSaveContent.replace(strDecoratedplugincss, "");
				strSaveContent = strSaveContent.replace(strDecoratedpluginjs, "");
				strSaveContent = strSaveContent.replace(strDecoratedpluginbar, "");

				PsTzSiteiDefnTWithBLOBs psTzSiteiDefnTWithBLOBs = new PsTzSiteiDefnTWithBLOBs();
				psTzSiteiDefnTWithBLOBs.setTzSiteiId(strSiteId);
				psTzSiteiDefnTWithBLOBs.setTzIndexPrecode(strSaveContent);
				psTzSiteiDefnTWithBLOBs.setTzIndexPubcode(strSaveContent);

				String sql = "select 'Y' from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
				String recExists = sqlQuery.queryForObject(sql, new Object[] { strSiteId }, "String");

				if ("Y".equals(recExists)) {
					psTzSiteiDefnTMapper.updateByPrimaryKeySelective(psTzSiteiDefnTWithBLOBs);
				} else {
					psTzSiteiDefnTMapper.insertSelective(psTzSiteiDefnTWithBLOBs);
				}

				mapRet.put("success", true);
				errMsg[0] = "0";
				errMsg[1] = "已发布";
				strRet = jacksonUtil.Map2json(mapRet);
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "发布失败！" + e.getMessage();
			mapRet.put("success", false);
			strRet = jacksonUtil.Map2json(mapRet);
		}

		return strRet;

	}

}
