/**
 * 
 */
package com.tranzvision.gd.TZSitePageBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzWebsiteLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdObjectServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
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

	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	@Autowired
	private TzWebsiteLoginServiceImpl tzWebsiteLoginServiceImpl;

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

			String strMenuId = "";

			String strAreaId = "";

			//String strAreaZone = "";

			String strAreaType = "";

			String strColuId = "";

			String strFrom = "";

			//String strPhone = "";

			int numPageRow = 10;

			strFrom = jacksonUtil.getString("qureyFrom");

			if ("M".equals(strFrom)) {

				strMenuId = jacksonUtil.getString("menuId");
				sql = "select TZ_MENU_COLUMN from PS_TZ_SITEI_MENU_T where TZ_SITEI_ID =? and TZ_MENU_ID =? and TZ_MENU_STATE = 'Y'";
				strColuId = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strMenuId }, "String");
			} else if ("A".equals(strFrom)) {
				strAreaId = jacksonUtil.getString("areaId");

				//strAreaZone = jacksonUtil.getString("areaZone");

				strAreaType = jacksonUtil.getString("areaType");

				if (null == strAreaId || "".equals(strAreaId)) {
					sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzAreaIdFromSiteidAreatype");
					strAreaId = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strAreaType }, "String");
				}

				sql = "SELECT TZ_COLU_ID FROM PS_TZ_SITEI_AREA_T WHERE TZ_SITEI_ID=? AND TZ_AREA_ID=? and TZ_AREA_STATE='Y'";
				strColuId = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strAreaId }, "String");
			}

			int numNowPage = jacksonUtil.getInt("page");
			int numPageSize = jacksonUtil.getInt("pagesize");
			String strType = jacksonUtil.getString("type");
			if (numNowPage <= 0) {
				numNowPage = 1;
			}
			if (numPageSize > 0) {
				numPageRow = numPageSize;
			}
			if (null == strType || "".equals(strType)) {
				strType = "0";
			}

			/* 取得总条数 */
			String strOrderBy = "";
			int numTotalRow = 0;
			int numTotalPage = 0;
			String dtFormat = getSysHardCodeVal.getDateTimeFormat();
			SimpleDateFormat datetimeformat = new SimpleDateFormat(dtFormat);
			String strDateNow = datetimeformat.format(new Date());

			//前台登录用户的oprid
			String oprid = tzWebsiteLoginServiceImpl.getLoginedUserOprid(request);

			switch (strType) {
			case "0":
				sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteHDCountNumByDateTimeGT");
				strOrderBy = "asc";
				numTotalRow = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strColuId, strDateNow }, "int");
				break;
			case "1":
				sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteHDCountNumByOprid");
				strOrderBy = "asc";
				numTotalRow = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strColuId, oprid }, "int");
				break;
			case "2":
				sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteHDCountNumByDateTimeLT");
				strOrderBy = "desc";
				numTotalRow = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strColuId, strDateNow }, "int");
				break;
			default:
				sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteHDCountNumByDateTimeGT");
				strOrderBy = "asc";
				numTotalRow = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strColuId, strDateNow }, "int");
				break;
			}

			// 计算总页数
			if (Math.floorMod(numTotalRow, numPageRow) == 0) {
				numTotalPage = numTotalRow / numPageRow;
			} else {
				numTotalPage = (int) (Math.ceil(numTotalRow / numPageRow) + 1);
			}

			int numIndex = 1;
			String strDivPage = "";
			int maxPageNum = 6;

			if (numNowPage > maxPageNum) {
				numIndex = numNowPage - maxPageNum + 1;
			}

			for (int i = numIndex; i <= numTotalPage; i++) {
				String strPageNowClass = "";

				if (numNowPage == numIndex) {
					strPageNowClass = "class=\"now\"";
				}

				strDivPage = strDivPage + "<li " + strPageNowClass + " onclick=\"QueryColuHY("
						+ String.valueOf(numIndex) + ")\">" + String.valueOf(numIndex) + "</li>";

				if (numNowPage > maxPageNum) {
					if (numIndex >= numNowPage) {
						break;
					}
				} else if (numIndex >= maxPageNum) {
					break;
				}

			}

			if (!"".equals(strDivPage)) {
				int setNowPageNum = 1;
				if (numNowPage > 2) {
					setNowPageNum = numNowPage - 1;
				}
				strDivPage = "<li onclick=\"QueryColuHY(" + String.valueOf(setNowPageNum) + ")\">&lt;&lt;</li>"
						+ strDivPage;

				int setTotalPageNum = numTotalPage;
				if ((numNowPage + 1) < numTotalPage) {
					setTotalPageNum = numNowPage + 1;
				}
				strDivPage = strDivPage + "<li onclick=\"QueryColuHY(" + String.valueOf(setTotalPageNum)
						+ ")\">&gt;&gt;</li>";

			}

			/*
			 * strDivPage = strDivPage.replace((char) (10), ' '); strDivPage =
			 * strDivPage.replace((char) (13), ' '); strDivPage =
			 * strDivPage.replace("\\", "\\\\"); strDivPage =
			 * strDivPage.replace("'", "\\'"); strDivPage =
			 * strDivPage.replace("\"", "\\\"");
			 */

			// 当前第几页
			String strNowPageDesc = "";
			if ("ENG".equals(strLang)) {
				strNowPageDesc = strPage + " <span id=\"nowpage\">" + String.valueOf(numNowPage) + "</span>  "
						+ strTotal + " " + String.valueOf(numTotalPage);
			} else {
				strNowPageDesc = strNum + " <span id=\"nowpage\">" + String.valueOf(numNowPage) + "</span>/"
						+ String.valueOf(numTotalPage) + " " + strPage;
			}

			/*
			 * strNowPageDesc = strNowPageDesc.replace((char) (10), ' ');
			 * strNowPageDesc = strNowPageDesc.replace((char) (13), ' ');
			 * strNowPageDesc = strNowPageDesc.replace("\\", "\\\\");
			 * strNowPageDesc = strNowPageDesc.replace("'", "\\'");
			 * strNowPageDesc = strNowPageDesc.replace("\"", "\\\"");
			 */

			// 查询的最大行，最小行
			//int numMaxRow = numNowPage * numPageRow;
			int numMinRow = (numNowPage - 1) * numPageRow + 1;

			List<Map<String, Object>> listSiteActivities = new ArrayList<Map<String, Object>>();

			switch (strType) {
			case "0":
				sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteHDListByDateTimeGT");
				listSiteActivities = sqlQuery.queryForList(sql,
						new Object[] { strSiteId, strColuId, strDateNow, strOrderBy, numMinRow, numPageRow });
				break;
			case "1":
				sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteHDListByOprid");
				listSiteActivities = sqlQuery.queryForList(sql,
						new Object[] { strSiteId, strColuId, oprid, strOrderBy, numMinRow, numPageRow });
				break;
			case "2":
				sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteHDListByDateTimeLT");
				listSiteActivities = sqlQuery.queryForList(sql,
						new Object[] { strSiteId, strColuId, strDateNow, strOrderBy, numMinRow, numPageRow });
				break;
			default:
				sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteHDListByDateTimeGT");
				listSiteActivities = sqlQuery.queryForList(sql,
						new Object[] { strSiteId, strColuId, strDateNow, strOrderBy, numMinRow, numPageRow });
				break;
			}

			String strResultContent = "";
			String dispatcherUrl = request.getContextPath() + "/dispatcher";

			String strDateFormat = getSysHardCodeVal.getDateFormat();
			SimpleDateFormat dateFormat = new SimpleDateFormat(strDateFormat);
			SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
			SimpleDateFormat dayFormat = new SimpleDateFormat("dd");

			Date dateNow = new Date();

			for (Map<String, Object> mapActivity : listSiteActivities) {
				strColuId = mapActivity.get("TZ_COLU_ID") == null ? "" : String.valueOf(mapActivity.get("TZ_COLU_ID"));
				String strArtId = mapActivity.get("TZ_ART_ID") == null ? ""
						: String.valueOf(mapActivity.get("TZ_ART_ID"));
				String strActName = mapActivity.get("TZ_NACT_NAME") == null ? ""
						: String.valueOf(mapActivity.get("TZ_NACT_NAME"));
				String dtAct = mapActivity.get("TZ_START_DT") == null ? ""
						: String.valueOf(mapActivity.get("TZ_START_DT"));
				String strActCity = mapActivity.get("TZ_HD_CS") == null ? ""
						: String.valueOf(mapActivity.get("TZ_HD_CS"));
				String strKqbm = mapActivity.get("TZ_QY_ZXBM") == null ? ""
						: String.valueOf(mapActivity.get("TZ_QY_ZXBM"));
				String dtBmStart = mapActivity.get("TZ_APPF_DT") == null ? ""
						: String.valueOf(mapActivity.get("TZ_APPF_DT"));
				String timeBmStart = mapActivity.get("TZ_APPF_TM") == null ? ""
						: String.valueOf(mapActivity.get("TZ_APPF_TM"));
				String dtBmEnd = mapActivity.get("TZ_APPE_DT") == null ? ""
						: String.valueOf(mapActivity.get("TZ_APPE_DT"));
				String timeBmEnd = mapActivity.get("TZ_APPE_TM") == null ? ""
						: String.valueOf(mapActivity.get("TZ_APPE_TM"));

				String strUrl = dispatcherUrl + "?classid=art_view&operatetype=HTML&siteId=" + strSiteId + "&columnId="
						+ strColuId + "&artId=" + strArtId + "&oprate=R";

				strResultContent = strResultContent
						+ "<div class=\"main_mid_activity_list2\"><div class=\"main_mid_activity_list_date\"><div class=\"main_mid_activity_list_date_month\">"
						+ monthFormat.format(monthFormat.parse(dtAct))
						+ "</div><div class=\"main_mid_activity_list_date_day\">"
						+ dayFormat.format(dayFormat.parse(dtAct))
						+ "</div></div><div class=\"main_mid_activity_list_title2\"><a target=\"_blank\" href=" + strUrl
						+ ">" + strActName + "</a><br /><span class=\"font_gray_14px\">【" + strActCity
						+ "】-</span><span class=\"font_gray_14px\">" + dtAct + "</span></div>";

				// 是否可以报名
				String strkBmFlg = "";
				if ("Y".equals(strKqbm)) {

					if (dateFormat.parse(dtBmStart).getTime() < dateNow.getTime()
							&& dateFormat.parse(dtBmEnd).getTime() > dateNow.getTime()) {
						strkBmFlg = "Y";
					} else {

						if (dateFormat.parse(dtBmStart).getTime() == dateNow.getTime()
								&& datetimeformat.parse(timeBmStart).getTime() < dateNow.getTime()) {
							strkBmFlg = "Y";
						} else if (dateFormat.parse(dtBmEnd).getTime() == dateNow.getTime()
								&& datetimeformat.parse(timeBmEnd).getTime() >= dateNow.getTime()) {
							strkBmFlg = "Y";
						}

					}

				}

				switch (strType) {
				case "0":

					if ("Y".equals(strkBmFlg)) {
						sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzSiteHDBmrId");
						String strBmrId = sqlQuery.queryForObject(sql, new Object[] {strArtId, oprid}, "String");
						if (null != strBmrId && !"".equals(strBmrId)) {
							strResultContent = strResultContent
									+ "<div class=\"main_mid_activity_list_button\"><a id=\"hdcx_" + strArtId
									+ "\" href=\"javascript:void(0);\" onclick=\"hdcx(" + strArtId + "," + strBmrId
									+ ",this)\"><div class=\"bt_blue\">" + strCancel + "</div></a></div>";
						} else {
							strResultContent = strResultContent
									+ "<div class=\"main_mid_activity_list_button\"><a id=\"hdbm_" + strArtId
									+ "\" href=\"javascript:void(0);\" onclick=\"hdbm(" + strArtId
									+ ",this)\"><div class=\"bt_blue\">" + strSignUp + "</div></a></div>";
						}

					}

					break;
				case "1":
					if ("Y".equals(strkBmFlg)) {
						sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzSiteHDBmrId");
						String strBmrId = sqlQuery.queryForObject(sql, new Object[] {strArtId, oprid}, "String");
						strResultContent = strResultContent
								+ "<div class=\"main_mid_activity_list_button\"><a id=\"hdcx_" + strArtId
								+ "\" href=\"javascript:void(0);\" onclick=\"hdcx(" + strArtId + "," + strBmrId
								+ ",this)\"><div class=\"bt_blue\">" + strCancel + "</div></a></div>";
					}
					break;
				case "2":

					break;

				default:

					break;

				}
				
				strResultContent = strResultContent + "</div>";

			}

			if ("".equals(strResultContent)) {
				switch (strType) {
				case "0":
					strResultContent = "<div style='font-size:16px;text-align:center;font-weight:bold;margin-top:20px;'>"
							+ gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZ_SITE_MESSAGE", "70",
									strLang, "暂时没有相关活动！", "There is no activity recently.")
							+ "</div>";
					break;
				case "1":
					strResultContent = "<div style='font-size:16px;text-align:center;font-weight:bold;margin-top:20px;'>"
							+ gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZ_SITE_MESSAGE", "72",
									strLang, "暂时没有相关活动！", "There is no activity registered.")
							+ "</div>";
					break;
				case "2":
					strResultContent = "<div style='font-size:16px;text-align:center;font-weight:bold;margin-top:20px;'>"
							+ gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZ_SITE_MESSAGE", "70",
									strLang, "暂时没有相关活动！", "There is no activity.")
							+ "</div>";
					break;
				default:
					strResultContent = "<div style='font-size:16px;text-align:center;font-weight:bold;margin-top:20px;'>"
							+ gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZ_SITE_MESSAGE", "70",
									strLang, "暂时没有相关活动！", "There is no activity recently.")
							+ "</div>";
					break;

				}

			}

			/*
			 * strResultContent = strResultContent.replace((char) (10), ' ');
			 * strResultContent = strResultContent.replace((char) (13), ' ');
			 * strResultContent = strResultContent.replace("\\", "\\\\");
			 * strResultContent = strResultContent.replace("'", "\\'");
			 * strResultContent = strResultContent.replace("\"", "\\\"");
			 */

			Map<String, Object> mapRet = new HashMap<String, Object>();
			mapRet.put("coluItem", strResultContent);
			mapRet.put("divPage", strDivPage);
			mapRet.put("nowPageDesc", strNowPageDesc);

			strRet = jacksonUtil.Map2json(mapRet);

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "获取数据失败！";
		}

		return strRet;
	}

}