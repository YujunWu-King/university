/**
 * 
 */
package com.tranzvision.gd.TZSitePageBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiAreaTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiMenuTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiMnpfTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiAreaTWithBLOBs;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiMenuT;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiMnpfT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.session.TzGetSetSessionValue;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 原PS：TZ_SITE_DECORATED_APP:TZ_MENU_SETED_CLS
 * 
 * @author SHIHUA
 * @since 2015-12-17
 */
@Service("com.tranzvision.gd.TZSitePageBundle.service.impl.TzMenuSetedServiceImpl")
public class TzMenuSetedServiceImpl extends FrameworkImpl {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzGDObject;

	@Autowired
	private TzGetSetSessionValue tzGetSetSessionValue;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private GetSeqNum getSeqNum;

	@Autowired
	private PsTzSiteiMnpfTMapper psTzSiteiMnpfTMapper;

	@Autowired
	private PsTzSiteiMenuTMapper psTzSiteiMenuTMapper;

	@Autowired
	private PsTzSiteiAreaTMapper psTzSiteiAreaTMapper;

	@Override
	public String tzQuery(String strParams, String[] errMsg) {

		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();

		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			jacksonUtil.json2Map(strParams);

			String strSiteId = jacksonUtil.getString("siteId");

			if (null != strSiteId && !"".equals(strSiteId)) {
				ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();

				String sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteMenus");
				List<Map<String, Object>> listMenus = sqlQuery.queryForList(sql, new Object[] { strSiteId });

				String strMenuHtml = "";
				int menu_i = 0;

				String ctxPath = request.getContextPath();

				for (Map<String, Object> mapMenu : listMenus) {
					menu_i++;
					String strMenuId = mapMenu.get("TZ_MENU_ID") == null ? ""
							: String.valueOf(mapMenu.get("TZ_MENU_ID"));
					String strMenuName = mapMenu.get("TZ_MENU_NAME") == null ? ""
							: String.valueOf(mapMenu.get("TZ_MENU_NAME"));
					String strTypeImg = mapMenu.get("TZ_TYPE_IMG") == null ? ""
							: String.valueOf(mapMenu.get("TZ_TYPE_IMG"));
					String strNowImg = mapMenu.get("TZ_NOW_IMG") == null ? ""
							: String.valueOf(mapMenu.get("TZ_NOW_IMG"));
					String strIsDel = mapMenu.get("TZ_IS_DEL") == null ? "" : String.valueOf(mapMenu.get("TZ_IS_DEL"));
					String strOpenType = mapMenu.get("TZ_MENU_OPURL_TYPE") == null ? ""
							: String.valueOf(mapMenu.get("TZ_MENU_OPURL_TYPE"));

					sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteMenuImgs");
					Map<String, Object> mapMenuImg = sqlQuery.queryForMap(sql,
							new Object[] { strSiteId, strMenuId, strSiteId });

					if (null != mapMenuImg) {

						strTypeImg = mapMenuImg.get("TZ_TYPE_IMG") == null ? ""
								: String.valueOf(mapMenuImg.get("TZ_TYPE_IMG"));
						strNowImg = mapMenuImg.get("TZ_NOW_IMG") == null ? ""
								: String.valueOf(mapMenuImg.get("TZ_NOW_IMG"));

					}

					String strMenuImg = ctxPath + strTypeImg;

					Map<String, Object> mapJson = new HashMap<String, Object>();
					mapJson.put("id", strMenuId);
					mapJson.put("text", strMenuName);
					mapJson.put("leaf", "true");
					mapJson.put("icon", strMenuImg);
					mapJson.put("iconCls", "iconMenu");
					mapJson.put("isDel", strIsDel);

					listJson.add(mapJson);

					String strGloMenuId = tzGetSetSessionValue.getTzSiteGloMenuId();
					String strMenuNowCls = "";
					if(("".equals(strGloMenuId) || null==strGloMenuId) && menu_i==1){
						strMenuImg = ctxPath + strNowImg;
						strMenuNowCls = "main_left_nav0_c";
					}else if (strMenuId.equals(strGloMenuId)) {
						strMenuImg = ctxPath + strNowImg;
						strMenuNowCls = "main_left_nav0_c";
					} else {
						strMenuImg = ctxPath + strTypeImg;
						strMenuNowCls = "main_left_nav0";
					}

					strMenuHtml = strMenuHtml + tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzMenuDiv", strMenuId,
							strMenuName, strMenuImg, strMenuNowCls, strSiteId, strOpenType.trim());
				}

				/*
				 * strMenuHtml = strMenuHtml.replace((char) (10), ' ');
				 * strMenuHtml = strMenuHtml.replace((char) (13), ' ');
				 * strMenuHtml = strMenuHtml.replace("\\", "\\\\"); strMenuHtml
				 * = strMenuHtml.replace("'", "\\'"); strMenuHtml =
				 * strMenuHtml.replace("\"", "\\\"");
				 */

				Map<String, Object> mapJson = new HashMap<String, Object>();
				mapJson.put("id", "add");
				mapJson.put("text",
						"<span style=\"color:orange;font-size:26px\">&nbsp;&nbsp;&nbsp;+&nbsp;&nbsp;&nbsp;</span>");
				mapJson.put("leaf", "true");
				mapJson.put("icon", " ");
				mapJson.put("iconCls", "");
				listJson.add(mapJson);

				Map<String, Object> mapRoot = new HashMap<String, Object>();
				mapRoot.put("expanded", "true");
				mapRoot.put("children", listJson);

				mapRet.put("root", mapRoot);
				mapRet.put("divHtml", strMenuHtml);

				return jacksonUtil.Map2json(mapRet);

			} else {
				errMsg[0] = "1";
				errMsg[1] = "请先开通站点！";
				mapRet.put("success", false);
				strRet = jacksonUtil.Map2json(mapRet);
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "请求编辑区域异常！";
			mapRet.put("success", false);
			strRet = jacksonUtil.Map2json(mapRet);
		}

		return strRet;

	}

	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();

		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			Date dateNow = new Date();
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {

				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				String strReplaceCtxPath = request.getContextPath() + "/";

				// 类型标志;
				String strFlag = jacksonUtil.getString("typeFlag");

				// 信息内容;
				Map<String, Object> mapData = jacksonUtil.getMap("data");

				String strSiteId = mapData.get("siteId") == null ? "" : String.valueOf(mapData.get("siteId"));

				String strMenuId = mapData.get("menuId") == null ? "" : String.valueOf(mapData.get("menuId"));

				String strMenuName = mapData.get("menuName") == null ? ""
						: String.valueOf(mapData.get("menuName")).trim();

				String strMenuColu = mapData.get("menuColu") == null ? "" : String.valueOf(mapData.get("menuColu"));

				String strMenuTypeId = mapData.get("menuTypeId") == null ? ""
						: String.valueOf(mapData.get("menuTypeId"));

				String strMenuURL = mapData.get("menuLink") == null ? "" : String.valueOf(mapData.get("menuLink"));

				String strMenuTypeImg = mapData.get("menutypeimg") == null ? ""
						: String.valueOf(mapData.get("menutypeimg")).replace(strReplaceCtxPath, "/");

				String strMenuNowImg = mapData.get("menunowimg") == null ? ""
						: String.valueOf(mapData.get("menunowimg")).replace(strReplaceCtxPath, "/");

				String strMenuOPType = mapData.get("linkTarget") == null ? ""
						: String.valueOf(mapData.get("linkTarget"));

				if (!"".equals(strMenuURL)) {
					strMenuURL = "http://" + strMenuURL;
					strMenuURL = strMenuURL.replace("http://http://", "http://");
				}

				if ("save".equals(strFlag)) {

					if (null == strMenuTypeId || "".equals(strMenuTypeId)) {
						mapRet.put("success", false);
						errMsg[0] = "1";
						errMsg[1] = "请选择功能类型！";
						return jacksonUtil.Map2json(mapRet);
					}

					String sql = "select 'Y' from PS_TZ_SITEI_MENU_T where TZ_SITEI_ID=? and TZ_MENU_NAME=? and TZ_MENU_ID <> ? and TZ_MENU_STATE = 'Y'";
					String strCheckMenuName = sqlQuery.queryForObject(sql,
							new Object[] { strSiteId, strMenuName, strMenuId }, "String");
					if ("Y".equals(strCheckMenuName)) {
						mapRet.put("success", false);
						errMsg[0] = "1";
						errMsg[1] = "菜单名称重复，请重填。";
						return jacksonUtil.Map2json(mapRet);
					}

					if ((null != strSiteId && !"".equals(strSiteId)) && (null != strMenuId && !"".equals(strMenuId))) {

						if ("add".equals(strMenuId)) {

							sql = "select TZ_IS_ADD from PS_TZ_SITEI_MTYP_T where TZ_SITEI_ID=? and TZ_MENU_TYPE_ID=? and TZ_TYPE_STATE='Y'";
							String strCanAddMenu = sqlQuery.queryForObject(sql,
									new Object[] { strSiteId, strMenuTypeId }, "String");

							if (!"on".equals(strCanAddMenu)) {
								mapRet.put("success", false);
								errMsg[0] = "1";
								errMsg[1] = "很抱歉，该功能类型不能添加菜单，请重选！";
								return jacksonUtil.Map2json(mapRet);
							}

							String strNewMenuId = String.valueOf(getSeqNum.getSeqNum("TZ_SITEI_MENU_T", "TZ_MENU_ID"));

							PsTzSiteiMenuT psTzSiteiMenuT = new PsTzSiteiMenuT();
							psTzSiteiMenuT.setTzSiteiId(strSiteId);
							psTzSiteiMenuT.setTzMenuId(strNewMenuId);
							psTzSiteiMenuT.setTzMenuName(strMenuName);
							psTzSiteiMenuT.setTzMenuColumn(strMenuColu);
							psTzSiteiMenuT.setTzMenuTypeId(strMenuTypeId);
							psTzSiteiMenuT.setTzMenuUrl(strMenuURL);
							psTzSiteiMenuT.setTzMenuOpurlType(strMenuOPType);
							psTzSiteiMenuT.setTzMenuState("Y");
							psTzSiteiMenuT.setTzAddedDttm(dateNow);
							psTzSiteiMenuT.setTzAddedOprid(oprid);
							psTzSiteiMenuT.setTzLastmantDttm(dateNow);
							psTzSiteiMenuT.setTzLastmantOprid(oprid);

							sql = "select max(TZ_MENU_XH) from PS_TZ_SITEI_MENU_T where TZ_SITEI_ID=? and TZ_MENU_STATE='Y'";
							int numxh = sqlQuery.queryForObject(sql, new Object[] { strSiteId }, "int");
							psTzSiteiMenuT.setTzMenuXh(numxh + 1);

							psTzSiteiMenuTMapper.insertSelective(psTzSiteiMenuT);

							// 根据所选菜单类型为菜单添加栏目等
							sql = "select TZ_SKIN_ID from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID = ?";
							String strSkinId = sqlQuery.queryForObject(sql, new Object[] { strSiteId }, "String");

							PsTzSiteiMnpfT psTzSiteiMnpfT = new PsTzSiteiMnpfT();
							psTzSiteiMnpfT.setTzSiteiId(strSiteId);
							psTzSiteiMnpfT.setTzMenuId(strNewMenuId);
							psTzSiteiMnpfT.setTzSkinId(strSkinId);

							if ((null != strMenuTypeImg && !"".equals(strMenuTypeImg))
									|| (null != strMenuNowImg && !"".equals(strMenuNowImg))) {
								psTzSiteiMnpfT.setTzTypeImg(strMenuTypeImg);
								psTzSiteiMnpfT.setTzNowImg(strMenuNowImg);
							} else {
								sql = "select TZ_TYPE_IMG,TZ_NOW_IMG from PS_TZ_SITEI_CDPF_T where TZ_SITEI_ID=? and TZ_MENU_TYPE_ID=? and TZ_SKIN_ID=?";
								Map<String, Object> mapMenuImg = sqlQuery.queryForMap(sql,
										new Object[] { strSiteId, strMenuTypeId, strSkinId });
								strMenuTypeImg = mapMenuImg.get("TZ_TYPE_IMG") == null ? ""
										: String.valueOf(mapMenuImg.get("TZ_TYPE_IMG"));
								strMenuNowImg = mapMenuImg.get("TZ_NOW_IMG") == null ? ""
										: String.valueOf(mapMenuImg.get("TZ_NOW_IMG"));
								psTzSiteiMnpfT.setTzTypeImg(strMenuTypeImg);
								psTzSiteiMnpfT.setTzNowImg(strMenuNowImg);
							}

							sql = "select 'Y' from PS_TZ_SITEI_MNPF_T where TZ_SITEI_ID=? and TZ_MENU_ID=? and TZ_SKIN_ID=?";
							String recExists = sqlQuery.queryForObject(sql,
									new Object[] { strSiteId, strNewMenuId, strSkinId }, "String");
							if ("Y".equals(recExists)) {
								psTzSiteiMnpfTMapper.updateByPrimaryKeySelective(psTzSiteiMnpfT);
							} else {
								psTzSiteiMnpfTMapper.insertSelective(psTzSiteiMnpfT);
							}

							String strMenuJson = this.tzQuery(jacksonUtil.Map2json(mapData), errMsg);

							jacksonUtil.json2Map(strMenuJson);

							String strDivHtml = jacksonUtil.getString("divHtml");

							sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzAreaIdFromSiteidAreatypeStateY");
							String strAreaId = sqlQuery.queryForObject(sql, new Object[] { strSiteId, "ZC" }, "String");

							PsTzSiteiAreaTWithBLOBs psTzSiteiAreaTWithBLOBs = new PsTzSiteiAreaTWithBLOBs();
							psTzSiteiAreaTWithBLOBs.setTzSiteiId(strSiteId);
							psTzSiteiAreaTWithBLOBs.setTzAreaId(strAreaId);
							psTzSiteiAreaTWithBLOBs.setTzAreaSavecode(strDivHtml);

							psTzSiteiAreaTMapper.updateByPrimaryKeySelective(psTzSiteiAreaTWithBLOBs);

							return strMenuJson + ",\"menuId\":\"" + strNewMenuId + "\"";

						} else {

							PsTzSiteiMenuT psTzSiteiMenuT = new PsTzSiteiMenuT();
							psTzSiteiMenuT.setTzSiteiId(strSiteId);
							psTzSiteiMenuT.setTzMenuId(strMenuId);
							psTzSiteiMenuT.setTzLastmantDttm(dateNow);
							psTzSiteiMenuT.setTzLastmantOprid(oprid);

							if (null != strMenuName && !"".equals(strMenuName)) {
								psTzSiteiMenuT.setTzMenuName(strMenuName);
							}

							if (null != strMenuColu && !"".equals(strMenuColu)) {
								psTzSiteiMenuT.setTzMenuColumn(strMenuColu);
							}

							if (null != strMenuTypeId && !"".equals(strMenuTypeId)) {
								psTzSiteiMenuT.setTzMenuTypeId(strMenuTypeId);
							}

							if (null != strMenuURL && !"".equals(strMenuURL)) {
								psTzSiteiMenuT.setTzMenuUrl(strMenuURL);
							}

							if (null != strMenuOPType && !"".equals(strMenuOPType)) {
								psTzSiteiMenuT.setTzMenuOpurlType(strMenuOPType);
							}

							psTzSiteiMenuTMapper.updateByPrimaryKeySelective(psTzSiteiMenuT);

							sql = "select TZ_SKIN_ID from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID = ?";
							String strSkinId = sqlQuery.queryForObject(sql, new Object[] { strSiteId }, "String");

							PsTzSiteiMnpfT psTzSiteiMnpfT = new PsTzSiteiMnpfT();
							psTzSiteiMnpfT.setTzSiteiId(strSiteId);
							psTzSiteiMnpfT.setTzMenuId(strMenuId);
							psTzSiteiMnpfT.setTzSkinId(strSkinId);
							psTzSiteiMnpfT.setTzTypeImg(strMenuTypeImg);
							psTzSiteiMnpfT.setTzNowImg(strMenuNowImg);

							sql = "select 'Y' from PS_TZ_SITEI_MNPF_T where TZ_SITEI_ID=? and TZ_MENU_ID=? and TZ_SKIN_ID=?";
							String recExists = sqlQuery.queryForObject(sql,
									new Object[] { strSiteId, strMenuId, strSkinId }, "String");
							if ("Y".equals(recExists)) {
								psTzSiteiMnpfTMapper.updateByPrimaryKeySelective(psTzSiteiMnpfT);
							} else {
								psTzSiteiMnpfTMapper.insertSelective(psTzSiteiMnpfT);
							}

							String strMenuJson = this.tzQuery(jacksonUtil.Map2json(mapData), errMsg);

							jacksonUtil.json2Map(strMenuJson);

							String strDivHtml = jacksonUtil.getString("divHtml");

							sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzAreaIdFromSiteidAreatypeStateY");
							String strAreaId = sqlQuery.queryForObject(sql, new Object[] { strSiteId, "ZC" }, "String");

							PsTzSiteiAreaTWithBLOBs psTzSiteiAreaTWithBLOBs = new PsTzSiteiAreaTWithBLOBs();
							psTzSiteiAreaTWithBLOBs.setTzSiteiId(strSiteId);
							psTzSiteiAreaTWithBLOBs.setTzAreaId(strAreaId);
							psTzSiteiAreaTWithBLOBs.setTzAreaSavecode(strDivHtml);

							psTzSiteiAreaTMapper.updateByPrimaryKeySelective(psTzSiteiAreaTWithBLOBs);

							return strMenuJson + ",\"menuId\":\"" + strMenuId + "\"";

						}

					} else {
						errMsg[0] = "1";
						errMsg[1] = "菜单编辑异常！参数不完整。";
						mapRet.put("success", false);
						strRet = jacksonUtil.Map2json(mapRet);
					}

				} else {

					errMsg[0] = "1";
					errMsg[1] = "菜单编辑异常！";
					mapRet.put("success", false);
					strRet = jacksonUtil.Map2json(mapRet);

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "菜单编辑异常！";
			mapRet.put("success", false);
			strRet = jacksonUtil.Map2json(mapRet);
		}

		return strRet;
	}

}
