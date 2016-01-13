/**
 * 
 */
package com.tranzvision.gd.TZSitePageBundle.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiAreaTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiColuTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiMenuTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiAreaTWithBLOBs;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiColuT;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiMenuT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 原PS：TZ_SITE_DECORATED_APP:TZ_MENU_EDITED_CLS
 * 
 * @author SHIHUA
 * @since 2015-12-17
 */
@Service("com.tranzvision.gd.TZSitePageBundle.service.impl.TzMenuEditedServiceImpl")
public class TzMenuEditedServiceImpl extends FrameworkImpl {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzGDObject;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private PsTzSiteiMenuTMapper psTzSiteiMenuTMapper;

	@Autowired
	private TzMenuSetedServiceImpl tzMenuSetedServiceImpl;

	@Autowired
	private PsTzSiteiAreaTMapper psTzSiteiAreaTMapper;

	@Autowired
	private PsTzSiteiColuTMapper psTzSiteiColuTMapper;

	@Autowired
	private GetSeqNum getSeqNum;

	@Override
	public String tzQuery(String strParams, String[] errMsg) {

		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("success", false);
		mapRet.put("data", "");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			jacksonUtil.json2Map(strParams);

			String strMenuTypeId = "";
			String strMenuName = "";
			String strMenuColu = "";
			String strMenuDesc = "";
			String strIsDel = "";
			String strIsEdit = "";
			String strMenuTypeImg = "";
			String strMenuNowImg = "";
			String strMenuURL = "";
			String strMenuOPType = "";
			String strCanAddMenu = "Y";
			String strCanAddColu = "N";
			String strSiteId = jacksonUtil.getString("siteId");
			String strMenuId = jacksonUtil.getString("menuId");

			if (null != strSiteId && !"".equals(strSiteId)) {

				if (null == strMenuId || "".equals(strMenuId)) {
					String sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetMenuidBySiteidStateY");
					strMenuId = sqlQuery.queryForObject(sql, new Object[] { strSiteId }, "String");
				}

				if ("add".equals(strMenuId)) {
					strRet = tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzMenuFormLoad", strSiteId, strMenuId,
							strMenuName, strMenuColu, strMenuTypeId, strMenuDesc, strIsDel, strIsEdit, strCanAddMenu,
							strCanAddColu, strMenuURL, strMenuOPType, "", "");
				} else {
					String sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteMenuInfo");
					List<Map<String, Object>> listMenus = sqlQuery.queryForList(sql,
							new Object[] { strSiteId, strMenuId });

					String comma = "";
					for (Map<String, Object> mapMenu : listMenus) {

						strMenuName = mapMenu.get("TZ_MENU_NAME") == null ? ""
								: String.valueOf(mapMenu.get("TZ_MENU_NAME"));
						strMenuTypeId = mapMenu.get("TZ_MENU_TYPE_ID") == null ? ""
								: String.valueOf(mapMenu.get("TZ_MENU_TYPE_ID"));
						strMenuColu = mapMenu.get("TZ_MENU_COLUMN") == null ? ""
								: String.valueOf(mapMenu.get("TZ_MENU_COLUMN"));
						strMenuTypeImg = mapMenu.get("TZ_TYPE_IMG") == null ? ""
								: String.valueOf(mapMenu.get("TZ_TYPE_IMG"));
						strMenuNowImg = mapMenu.get("TZ_NOW_IMG") == null ? ""
								: String.valueOf(mapMenu.get("TZ_NOW_IMG"));
						strMenuDesc = mapMenu.get("TZ_TYPE_DESCR") == null ? ""
								: String.valueOf(mapMenu.get("TZ_TYPE_DESCR"));
						strCanAddMenu = mapMenu.get("TZ_IS_ADD") == null ? ""
								: String.valueOf(mapMenu.get("TZ_IS_ADD"));
						strCanAddColu = mapMenu.get("TZ_ADD_COLU") == null ? ""
								: String.valueOf(mapMenu.get("TZ_ADD_COLU"));
						strIsDel = mapMenu.get("TZ_IS_DEL") == null ? "" : String.valueOf(mapMenu.get("TZ_IS_DEL"));
						strIsEdit = mapMenu.get("TZ_IS_EDITOR") == null ? ""
								: String.valueOf(mapMenu.get("TZ_IS_EDITOR"));
						strMenuURL = mapMenu.get("TZ_MENU_URL") == null ? ""
								: String.valueOf(mapMenu.get("TZ_MENU_URL"));
						strMenuOPType = mapMenu.get("TZ_MENU_OPURL_TYPE") == null ? ""
								: String.valueOf(mapMenu.get("TZ_MENU_OPURL_TYPE"));

						sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteMenuImgs");
						Map<String, Object> mapData = sqlQuery.queryForMap(sql,
								new Object[] { strSiteId, strMenuId, strSiteId });
						if (null != mapData) {
							strMenuTypeImg = mapData.get("TZ_TYPE_IMG") == null ? ""
									: String.valueOf(mapData.get("TZ_TYPE_IMG"));
							strMenuNowImg = mapData.get("TZ_NOW_IMG") == null ? ""
									: String.valueOf(mapData.get("TZ_NOW_IMG"));
						}

						Map<String, Object> mapJson = new HashMap<String, Object>();

						mapJson.put("siteId", strSiteId);
						mapJson.put("menuId", strMenuId);
						mapJson.put("menuName", strMenuName);
						mapJson.put("menuColu", strMenuColu);
						mapJson.put("menuTypeId", strMenuTypeId);
						mapJson.put("menuDescr", strMenuDesc);
						mapJson.put("menuDel", strIsDel);
						mapJson.put("menuEdit", strIsEdit);
						mapJson.put("addmenu", strCanAddMenu);
						mapJson.put("addcolu", strCanAddColu);
						mapJson.put("menuLink", strMenuURL);
						mapJson.put("linkTarget", strMenuOPType);
						mapJson.put("menutypeimg", strMenuTypeImg);
						mapJson.put("menunowimg", strMenuNowImg);

						mapRet.replace("success", true);
						mapRet.replace("data", mapJson);

						strRet = strRet + comma + jacksonUtil.Map2json(mapRet);

						comma = ",";

					}

				}
			} else {
				errMsg[0] = "1";
				errMsg[1] = "请先开通站点！";
				strRet = jacksonUtil.Map2json(mapRet);
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "操作失败！" + e.getMessage();
			strRet = jacksonUtil.Map2json(mapRet);
		}

		return strRet;

	}

	@Override
	@Transactional
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("success", false);
		
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

				// 类型标志;
				String strFlag = jacksonUtil.getString("typeFlag");
				// 信息内容;
				Map<String, Object> mapData = jacksonUtil.getMap("data");

				if ("delete".equals(strFlag)) {
					String strSiteId = mapData.get("siteId") == null ? ""
							: String.valueOf(mapData.get("siteId")).trim();
					String strMenuId = mapData.get("menuId") == null ? ""
							: String.valueOf(mapData.get("menuId")).trim();

					if (!"".equals(strSiteId) && !"".equals(strMenuId)) {

						String sql = "select TZ_MENU_NAME, TZ_IS_DEL From PS_TZ_SITEI_MENU_T PT where TZ_SITEI_ID = ? AND TZ_MENU_ID=?";
						Map<String, Object> mapMenu = sqlQuery.queryForMap(sql, new Object[] { strSiteId, strMenuId });
						String strMenuIsDel = mapMenu.get("TZ_IS_DEL") == null ? ""
								: String.valueOf(mapMenu.get("TZ_IS_DEL"));
						String strMenuName = mapMenu.get("TZ_MENU_NAME") == null ? ""
								: String.valueOf(mapMenu.get("TZ_MENU_NAME"));

						if ("N".equals(strMenuIsDel)) {
							strRet = jacksonUtil.Map2json(mapRet);
							errMsg[0] = "1";
							errMsg[1] = "菜单[" + strMenuName + "]不允许删除！";
							return strRet;
						} else {
							PsTzSiteiMenuT psTzSiteiMenuT = new PsTzSiteiMenuT();
							psTzSiteiMenuT.setTzSiteiId(strSiteId);
							psTzSiteiMenuT.setTzMenuId(strMenuId);
							psTzSiteiMenuT.setTzMenuState("N");
							psTzSiteiMenuT.setTzLastmantDttm(dateNow);
							psTzSiteiMenuT.setTzLastmantOprid(oprid);
							psTzSiteiMenuTMapper.updateByPrimaryKeySelective(psTzSiteiMenuT);

							Map<String, Object> mapParam = new HashMap<String, Object>();
							mapParam.put("siteId", strSiteId);
							mapParam.put("menuId", "");

							String strReturns = this.tzQuery(jacksonUtil.Map2json(mapParam), errMsg);

							String strMenuSeted = tzMenuSetedServiceImpl.tzQuery(jacksonUtil.Map2json(mapData), errMsg);
							jacksonUtil.json2Map(strMenuSeted);
							String strDivHtml = jacksonUtil.getString("divHtml");

							sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzAreaIdFromSiteidAreatypeStateY");
							String strAreaId = sqlQuery.queryForObject(sql, new Object[] { strSiteId, "ZC" }, "String");

							PsTzSiteiAreaTWithBLOBs psTzSiteiAreaTWithBLOBs = new PsTzSiteiAreaTWithBLOBs();
							psTzSiteiAreaTWithBLOBs.setTzSiteiId(strSiteId);
							psTzSiteiAreaTWithBLOBs.setTzAreaId(strAreaId);
							psTzSiteiAreaTWithBLOBs.setTzAreaSavecode(strDivHtml);

							psTzSiteiAreaTMapper.updateByPrimaryKeySelective(psTzSiteiAreaTWithBLOBs);

							strRet = strMenuSeted + "\"data\":" + strReturns;

							return strRet;
						}

					} else {
						errMsg[0] = "1";
						errMsg[1] = "删除[" + strMenuId + "]异常！";
						strRet = jacksonUtil.Map2json(mapRet);
						return strRet;
					}

				} else if ("sort".equals(strFlag)) {

					String strSiteId = mapData.get("siteId") == null ? ""
							: String.valueOf(mapData.get("siteId")).trim();
					String strMenus = mapData.get("menus") == null ? "" : String.valueOf(mapData.get("menus")).trim();

					String[] aryMenus = strMenus.split(",");

					if (null != strSiteId && !"".equals(strSiteId)) {
						int menuNum = 1;
						for (String menuid : aryMenus) {
							String sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.");
							sqlQuery.update(sql, new Object[] { menuNum, strSiteId, menuid });
							menuNum++;
						}
					}

					String strMenuSeted = tzMenuSetedServiceImpl.tzQuery(jacksonUtil.Map2json(mapData), errMsg);

					return strMenuSeted;

				} else if ("changeColu".equals(strFlag)) {

					String strSiteId = mapData.get("siteId") == null ? "" : String.valueOf(mapData.get("siteId"));
					String strMenuId = mapData.get("menuId") == null ? "" : String.valueOf(mapData.get("menuId"));
					String strColuId = mapData.get("coluId") == null ? "" : String.valueOf(mapData.get("coluId"));

					PsTzSiteiMenuT psTzSiteiMenuT = new PsTzSiteiMenuT();
					psTzSiteiMenuT.setTzSiteiId(strSiteId);
					psTzSiteiMenuT.setTzMenuId(strMenuId);
					psTzSiteiMenuT.setTzMenuColumn(strColuId);
					psTzSiteiMenuT.setTzLastmantDttm(dateNow);
					psTzSiteiMenuT.setTzLastmantOprid(oprid);

					psTzSiteiMenuTMapper.updateByPrimaryKeySelective(psTzSiteiMenuT);

					mapRet.replace("success", true);
					return jacksonUtil.Map2json(mapRet);

				} else if ("addColu".equals(strFlag)) {

					String strSiteId = mapData.get("siteId") == null ? "" : String.valueOf(mapData.get("siteId"));
					String strColuName = mapData.get("coluName") == null ? "" : String.valueOf(mapData.get("coluName"));
					String strMenuType = mapData.get("menuTypeId") == null ? ""
							: String.valueOf(mapData.get("menuTypeId"));

					String sql = "select TZ_COLU_TYPE,TZ_TEMP_ID,TZ_CONT_TYPE,TZ_CONT_TEMP from PS_TZ_SITEI_MTYP_T where TZ_SITEI_ID=? and TZ_MENU_TYPE_ID=? and TZ_TYPE_STATE='Y'";
					Map<String, Object> mapMenuType = sqlQuery.queryForMap(sql,
							new Object[] { strSiteId, strMenuType });

					PsTzSiteiColuT psTzSiteiColuT = new PsTzSiteiColuT();

					if (null != mapMenuType) {
						psTzSiteiColuT.setTzColuType(mapMenuType.get("TZ_COLU_TYPE") == null ? ""
								: String.valueOf(mapMenuType.get("TZ_COLU_TYPE")));
						psTzSiteiColuT.setTzTempId(mapMenuType.get("TZ_TEMP_ID") == null ? ""
								: String.valueOf(mapMenuType.get("TZ_TEMP_ID")));
						psTzSiteiColuT.setTzContType(mapMenuType.get("TZ_CONT_TYPE") == null ? ""
								: String.valueOf(mapMenuType.get("TZ_CONT_TYPE")));
						psTzSiteiColuT.setTzContTemp(mapMenuType.get("TZ_CONT_TEMP") == null ? ""
								: String.valueOf(mapMenuType.get("TZ_CONT_TEMP")));
					}

					String strColuId = String.valueOf(getSeqNum.getSeqNum("TZ_SITEI_COLU_T", "TZ_COLU_ID"));

					psTzSiteiColuT.setTzSiteiId(strSiteId);
					psTzSiteiColuT.setTzColuId(strColuId);
					psTzSiteiColuT.setTzColuName(strColuName);
					psTzSiteiColuT.setTzColuState("Y");

					psTzSiteiColuTMapper.insertSelective(psTzSiteiColuT);

					Map<String, Object> mapJson = new HashMap<String, Object>();
					mapJson.put("colu_id", strColuId);

					mapRet.replace("success", true);
					mapRet.put("data", mapJson);

					return jacksonUtil.Map2json(mapRet);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "操作失败！" + e.getMessage();
			strRet = jacksonUtil.Map2json(mapRet);
		}

		return strRet;
	}

}
