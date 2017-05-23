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
import com.tranzvision.gd.util.Calendar.CalendarUtil;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 
 */
@Service("com.tranzvision.gd.TZSitePageBundle.service.impl.TzMyActServiceImpl")
public class TzMyActServiceImpl extends FrameworkImpl {

	@Autowired
	private HttpServletRequest request;

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
		JacksonUtil jacksonUtil = new JacksonUtil();
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

			// String strAreaZone = "";

			String strAreaType = "";

			String strColuId = "";

			String strFrom = "";

			// String strPhone = "";

			int numPageRow = 10;
			/*
			strFrom = jacksonUtil.getString("qureyFrom");

			if ("M".equals(strFrom)) {

				strMenuId = jacksonUtil.getString("menuId");
				sql = "select TZ_MENU_COLUMN from PS_TZ_SITEI_MENU_T where TZ_SITEI_ID =? and TZ_MENU_ID =? and TZ_MENU_STATE = 'Y'";
				strColuId = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strMenuId }, "String");
			} else if ("A".equals(strFrom)) {
				strAreaId = jacksonUtil.getString("areaId");

				// strAreaZone = jacksonUtil.getString("areaZone");

				strAreaType = jacksonUtil.getString("areaType");

				if (null == strAreaId || "".equals(strAreaId)) {
					sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzAreaIdFromSiteidAreatype");
					strAreaId = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strAreaType }, "String");
				}

				sql = "SELECT TZ_COLU_ID FROM PS_TZ_SITEI_AREA_T WHERE TZ_SITEI_ID=? AND TZ_AREA_ID=? and TZ_AREA_STATE='Y'";
				strColuId = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strAreaId }, "String");
			}
			*/

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

			// 前台登录用户的oprid
			String oprid = tzWebsiteLoginServiceImpl.getLoginedUserOprid(request);
			
			//查看当前用户有没有设置范围;
			//如果没有设置范围，且没有报报名表则显示全部的;
			//注册是否开通了项目字段开外网显示;
			//其他的显示并集;
			//String jgId = tzWebsiteLoginServiceImpl.getLoginedUserOrgid(request); 
			//String isPrjShowWW = sqlQuery.queryForObject("select TZ_IS_SHOWWZSY from PS_TZ_REG_FIELD_T where TZ_JG_ID=? AND TZ_REG_FIELD_ID='TZ_PROJECT'", new Object[]{jgId},"String");
			//String isPrjShowWW = sqlQuery.queryForObject("select TZ_IS_SHOWWZSY from PS_TZ_REG_FIELD_T where TZ_SITEI_ID=? AND TZ_REG_FIELD_ID='TZ_PROJECT'", new Object[]{strSiteId},"String");
			String isPrjShowWW = "N";
			int haveBmCount = 0;
			int selectShowCount = 0;
			if("Y".equals(isPrjShowWW)){
				String haveBmCountSql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetCurBmbCountByOprid");
				//有没有报名已经开放班级;
				haveBmCount = sqlQuery.queryForObject(haveBmCountSql, new Object[]{oprid,strSiteId},"Integer");
				//有没有选择查看的范围;
				selectShowCount = sqlQuery.queryForObject("SELECT count(1) FROM PS_SHOW_PRJ_NEWS_T where OPRID=?", new Object[]{oprid}, "Integer");
			}

			switch (strType) {
			case "0":
				if(haveBmCount == 0 && selectShowCount==0){
					sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteHDCountNumByDateTimeGT");
					strOrderBy = "asc";
					// numTotalRow = sqlQuery.queryForObject(sql, new Object[] {
					// strSiteId, strColuId, strDateNow }, "int");
					//numTotalRow = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strColuId,strDateNow }, "int");
					numTotalRow = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strDateNow,oprid }, "int");
				}else{
					sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteHDCountNumByDateTimeGTAndProject");
					strOrderBy = "asc";
					numTotalRow = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strColuId,strDateNow,oprid,oprid }, "int");
				}
				break;
			case "1":
				if(haveBmCount == 0 && selectShowCount==0){
					sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteHDCountNumByOprid");
					strOrderBy = "asc";
					numTotalRow = sqlQuery.queryForObject(sql, new Object[] { strSiteId, oprid }, "int");
				}else{
					sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteHDCountNumByOpridAndProject");
					strOrderBy = "asc";
					numTotalRow = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strColuId, oprid,oprid,oprid }, "int");
				}
				break;
			case "2":
				if(haveBmCount == 0 && selectShowCount==0){
					sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteHDCountNumByDateTimeLT");
					strOrderBy = "desc";
					numTotalRow = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strDateNow,oprid }, "int");
				}else{
					sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteHDCountNumByDateTimeLTAndByProject");
					strOrderBy = "desc";
					numTotalRow = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strColuId, strDateNow,oprid,oprid }, "int");
				}
				break;
			default:
				if(haveBmCount == 0 && selectShowCount==0){
					sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteHDCountNumByDateTimeGT");
					strOrderBy = "asc";
					//numTotalRow = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strColuId, strDateNow }, "int");
					numTotalRow = sqlQuery.queryForObject(sql, new Object[] { strSiteId ,strDateNow,oprid}, "int");
				}else{
					sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetSiteHDCountNumByDateTimeGTAndProject");
					strOrderBy = "asc";
					numTotalRow = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strColuId,strDateNow,oprid,oprid }, "int");
				}
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

				if (numNowPage == i) {
					strPageNowClass = "class=\"now\"";
				}

				strDivPage = strDivPage + "<li " + strPageNowClass + " onclick=\"QueryColuHY(" + String.valueOf(i) + ","
						+ strType + ")\">" + String.valueOf(i) + "</li>";

				if (numNowPage > maxPageNum) {
					if (i >= numNowPage) {
						break;
					}
				} else if (i >= maxPageNum) {
					break;
				}

			}

			if (!"".equals(strDivPage)) {
				int setNowPageNum = 1;
				if (numNowPage > 2) {
					setNowPageNum = numNowPage - 1;
				}
				strDivPage = "<li onclick=\"QueryColuHY(" + String.valueOf(setNowPageNum) + "," + strType
						+ ")\">&lt;&lt;</li>" + strDivPage;

				int setTotalPageNum = numTotalPage;
				if ((numNowPage + 1) < numTotalPage) {
					setTotalPageNum = numNowPage + 1;
				}
				strDivPage = strDivPage + "<li onclick=\"QueryColuHY(" + String.valueOf(setTotalPageNum) + "," + strType
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
			// int numMaxRow = numNowPage * numPageRow;
			int numMinRow = (numNowPage - 1) * numPageRow;

			List<Map<String, Object>> listSiteActivities = new ArrayList<Map<String, Object>>();

			String strSQLName = "";
			switch (strType) {
			case "0":
				    //strSQLName = "TzGetSiteActNoticeList";
					if ("desc".equals(strOrderBy)) {
						strSQLName = "TzGetSiteHDListByDateTimeGTDesc";
					} else {
						strSQLName = "TzGetSiteHDListByDateTimeGT";
					}
					sql = tzGDObject.getSQLText("SQL.TZSitePageBundle." + strSQLName);
					listSiteActivities = sqlQuery.queryForList(sql,
							new Object[] { strSiteId,strDateNow,oprid, numMinRow, numPageRow});
				break;
			case "1":
					//strSQLName = "TzGetSiteRegistActList";
					if ("desc".equals(strOrderBy)) {
						strSQLName = "TzGetSiteHDListByOpridDesc";
					} else {
						strSQLName = "TzGetSiteHDListByOprid";
					}
					sql = tzGDObject.getSQLText("SQL.TZSitePageBundle." + strSQLName);
					//listSiteActivities = sqlQuery.queryForList(sql,new Object[] {oprid});
					listSiteActivities = sqlQuery.queryForList(sql,
							new Object[] { strSiteId, oprid, numMinRow, numPageRow });
				break;
			case "2":
				if(haveBmCount == 0 && selectShowCount==0){
					if ("desc".equals(strOrderBy)) {
						strSQLName = "TzGetSiteHDListByDateTimeLTDesc";
					} else {
						strSQLName = "TzGetSiteHDListByDateTimeLT";
					}
					sql = tzGDObject.getSQLText("SQL.TZSitePageBundle." + strSQLName);
					listSiteActivities = sqlQuery.queryForList(sql,
							new Object[] { strSiteId, strDateNow,oprid, numMinRow, numPageRow });
				}else{
					if ("desc".equals(strOrderBy)) {
						strSQLName = "TzGetSiteHDListByDateTimeLTDescAndByProject";
					} else {
						strSQLName = "TzGetSiteHDListByDateTimeLTAndByProject";
					}
					sql = tzGDObject.getSQLText("SQL.TZSitePageBundle." + strSQLName);
					listSiteActivities = sqlQuery.queryForList(sql,
							new Object[] { strSiteId, strColuId, strDateNow,oprid,oprid, numMinRow, numPageRow });
				}
				break;
			default:
				if(haveBmCount == 0 && selectShowCount==0){
					if ("desc".equals(strOrderBy)) {
						strSQLName = "TzGetSiteHDListByDateTimeGTDesc";
					} else {
						strSQLName = "TzGetSiteHDListByDateTimeGT";
					}
					sql = tzGDObject.getSQLText("SQL.TZSitePageBundle." + strSQLName);
					//listSiteActivities = sqlQuery.queryForList(sql, new Object[] { strSiteId, strColuId, strDateNow, numMinRow, numPageRow });
					listSiteActivities = sqlQuery.queryForList(sql, new Object[] { strSiteId, strColuId,strDateNow, numMinRow, numPageRow });
				}else{
					if ("desc".equals(strOrderBy)) {
						strSQLName = "TzGetSiteHDListByDateTimeGTDescAndByProject";
					} else {
						strSQLName = "TzGetSiteHDListByDateTimeGTAndByPorject";
					}
					sql = tzGDObject.getSQLText("SQL.TZSitePageBundle." + strSQLName);
					// listSiteActivities = sqlQuery.queryForList(sql, new Object[]
					// { strSiteId, strColuId, strDateNow, numMinRow, numPageRow });
					listSiteActivities = sqlQuery.queryForList(sql,
							new Object[] { strSiteId, strColuId,strDateNow,oprid,oprid, numMinRow, numPageRow });
				}
				break;
			}

			String strResultContent = "";
			String dispatcherUrl = request.getContextPath() + "/dispatcher";

			String strDateFormat = getSysHardCodeVal.getDateFormat();
			SimpleDateFormat dateFormat = new SimpleDateFormat(strDateFormat);

			Date dateNow = new Date();

			if (listSiteActivities != null) {
				for (Map<String, Object> mapActivity : listSiteActivities) {
					strColuId = mapActivity.get("TZ_COLU_ID") == null ? ""
							: String.valueOf(mapActivity.get("TZ_COLU_ID"));
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
					// String dtBmStart = mapActivity.get("TZ_APPF_DT") == null
					// ? "": String.valueOf(mapActivity.get("TZ_APPF_DT"));
					String timeBmStart = mapActivity.get("TZ_APPF_TM") == null ? ""
							: String.valueOf(mapActivity.get("TZ_APPF_TM"));
					// String dtBmEnd = mapActivity.get("TZ_APPE_DT") == null ?
					// "": String.valueOf(mapActivity.get("TZ_APPE_DT"));
					String timeBmEnd = mapActivity.get("TZ_APPE_TM") == null ? ""
							: String.valueOf(mapActivity.get("TZ_APPE_TM"));
					
					//活动开始时间
					String actStartTime = mapActivity.get("TZ_START_TM") == null ? ""
							: String.valueOf(mapActivity.get("TZ_START_TM"));
					

					String strUrl = dispatcherUrl + "?classid=art_view&operatetype=HTML&siteId=" + strSiteId
							+ "&columnId=" + strColuId + "&artId=" + strArtId + "&oprate=R";

					CalendarUtil calendarUtil = new CalendarUtil(dateFormat.parse(dtAct));

					strResultContent = strResultContent
							+ "<div class=\"main_mid_activity_list2\"><div class=\"main_mid_activity_list_date\"><div class=\"main_mid_activity_list_date_day\">"
							+ calendarUtil.getDateDay()
							+ "</div><div class=\"main_mid_activity_list_date_month\">" + calendarUtil.getDateMonthWord(strLang)
							+ "</div></div><div class=\"main_mid_activity_list_title2\"><a target=\"_blank\" href="
							+ strUrl + ">" + strActName + "</a><br /><span class=\"font_gray_14px\">【" + strActCity
							+ "】-</span><span class=\"font_gray_14px\">" + dtAct + "</span></div>";

					// 是否可以报名
					String strkBmFlg = "";
					//活动是否开始
					String isActStarted = "";
					if ("Y".equals(strKqbm)) {

						// 新逻辑
						try{
							if (datetimeformat.parse(timeBmStart).getTime() <= dateNow.getTime()
									&& datetimeformat.parse(timeBmEnd).getTime() > dateNow.getTime()) {
								strkBmFlg = "Y";
							}
						}catch(Exception e1){
							e1.printStackTrace();
						}
						
						//活动是否开始
						try{
							if (dateNow.getTime() < datetimeformat.parse(actStartTime).getTime()) {
								isActStarted = "N";
							}
						}catch(Exception e2){
							e2.printStackTrace();
						}

						/*
						 * 老的逻辑 if (dateFormat.parse(dtBmStart).getTime() <
						 * dateNow.getTime() &&
						 * dateFormat.parse(dtBmEnd).getTime() >
						 * dateNow.getTime()) { strkBmFlg = "Y"; } else {
						 * 
						 * if (dateFormat.parse(dtBmStart).getTime() ==
						 * dateNow.getTime() &&
						 * datetimeformat.parse(timeBmStart).getTime() <
						 * dateNow.getTime()) { strkBmFlg = "Y"; } else if
						 * (dateFormat.parse(dtBmEnd).getTime() ==
						 * dateNow.getTime() &&
						 * datetimeformat.parse(timeBmEnd).getTime() >=
						 * dateNow.getTime()) { strkBmFlg = "Y"; }
						 * 
						 * }
						 */

					}
					
					
					/************************添加报名状态--开始******************************/
					String regSql = "select 'Y' REG_FLAG,TZ_HD_BMR_ID,TZ_NREG_STAT FROM PS_TZ_NAUDLIST_T where OPRID=? and TZ_ART_ID=? and TZ_NREG_STAT IN('1','4')";
					Map<String, Object> mapBM = sqlQuery.queryForMap(regSql, new Object[] { oprid, strArtId });
					// 是否已注册报名标识
					String regFlag = "";
					// 报名人ID
					String strBmrId = "";
					//报名状态
					String applySta = "";
					if (mapBM != null) {
						regFlag = mapBM.get("REG_FLAG") == null ? "" : String.valueOf(mapBM.get("REG_FLAG"));
						strBmrId = mapBM.get("TZ_HD_BMR_ID") == null ? "" : String.valueOf(mapBM.get("TZ_HD_BMR_ID"));
						applySta = mapBM.get("TZ_NREG_STAT") == null ? "" : String.valueOf(mapBM.get("TZ_NREG_STAT"));
					}
					
					//显示报名状态
					String statusText = "";
					switch(applySta){
					case "1":
						statusText = "已报名";
						break;
					case "4":
						//等候席位数
						sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzGetWaitingNumber");
						int waitNum = sqlQuery.queryForObject(sql, new Object[]{ strArtId, strBmrId }, "int");
						statusText = "等候席第"+ waitNum +"位";
						break;
					}
					/************************添加报名状态--结束******************************/
					
					String statusClass = "positionRight10";//状态class
					
					switch (strType) {
					case "0":
						if ("Y".equals(strkBmFlg)) {
							statusClass = "";
//							sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzSiteHDBmrId");
//							String strBmrId = sqlQuery.queryForObject(sql, new Object[] { strArtId, oprid }, "String");
							
							if ("Y".equals(regFlag)) {
								strResultContent = strResultContent
										+ "<div class=\"main_mid_activity_list_button\"><a id=\"hdcx_" + strArtId
										+ "\" href=\"javascript:void(0);\" onclick=\"hdcx(" + strArtId + "," + strBmrId
										+ ",this)\"><div class=\"bt_blue\">" + strCancel + "</div></a></div>";
								//报名状态
								//strResultContent = strResultContent + "<div class=\"main_mid_activity_list_status\">"+ statusText +"</div>";
							} else {
								strResultContent = strResultContent
										+ "<div class=\"main_mid_activity_list_button\"><a id=\"hdbm_" + strArtId
										+ "\" href=\"javascript:void(0);\" onclick=\"hdbm(" + strArtId
										+ ",this)\"><div class=\"bt_blue\">" + strSignUp + "</div></a></div>";
							}

						}else if("Y".equals(strKqbm) 
								&& "Y".equals(regFlag) 
								&& "N".equals(isActStarted)){
							statusClass = "";
							//启动在线报名、且已报名、活动尚未开始，可用撤销
							strResultContent = strResultContent
									+ "<div class=\"main_mid_activity_list_button\"><a id=\"hdcx_" + strArtId
									+ "\" href=\"javascript:void(0);\" onclick=\"hdcx(" + strArtId + "," + strBmrId
									+ ",this)\"><div class=\"bt_blue\">" + strCancel + "</div></a></div>";
						}
						
						/*已报名活动报名状态一直显示*/
						//报名状态
						strResultContent = strResultContent + "<div class=\"main_mid_activity_list_status "+statusClass+"\">"+ statusText +"</div>";

						break;
					case "1":
						//报名时间内，或者活动未开始都可以撤销
						if ("Y".equals(strkBmFlg) || "N".equals(isActStarted)) {
							statusClass = "";
//							sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzSiteHDBmrId");
//							String strBmrId = sqlQuery.queryForObject(sql, new Object[] { strArtId, oprid }, "String");
							strResultContent = strResultContent
									+ "<div class=\"main_mid_activity_list_button\"><a id=\"hdcx_" + strArtId
									+ "\" href=\"javascript:void(0);\" onclick=\"hdcx(" + strArtId + "," + strBmrId
									+ ",this)\"><div class=\"bt_blue\">" + strCancel + "</div></a></div>";
							
							//报名状态
							//strResultContent = strResultContent + "<div class=\"main_mid_activity_list_status\">"+ statusText +"</div>";
						}
						
						/*已报名活动报名状态一直显示*/
						//报名状态
						strResultContent = strResultContent + "<div class=\"main_mid_activity_list_status "+statusClass+"\">"+ statusText +"</div>";
						
						break;
					case "2":
						/*已报名活动报名状态一直显示*/
						//报名状态
						strResultContent = strResultContent + "<div class=\"main_mid_activity_list_status "+statusClass+"\">"+ statusText +"</div>";
						break;

					default:

						break;

					}

					strResultContent = strResultContent + "</div>";

				}
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
