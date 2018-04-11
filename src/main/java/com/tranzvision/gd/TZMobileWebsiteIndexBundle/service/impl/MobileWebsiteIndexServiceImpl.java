package com.tranzvision.gd.TZMobileWebsiteIndexBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZApplicationCenterBundle.service.impl.AnalysisLcResult;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzWebsiteLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdObjectServiceImpl;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.ValidateUtil;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.cookie.TzCookie;
import com.tranzvision.gd.util.encrypt.DESUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 手机版招生网站首页 classId: mIndex
 * 
 * @author tang
 *
 */
@Service("com.tranzvision.gd.TZMobileWebsiteIndexBundle.service.impl.MobileWebsiteIndexServiceImpl")
public class MobileWebsiteIndexServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	@Autowired
	private TZGDObject tzGDObject;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private ValidateUtil validateUtil;
	@Autowired
	private TzCookie tzCookie;
	@Autowired
	private TzWebsiteLoginServiceImpl tzWebsiteLoginServiceImpl;
	@Autowired
	private GetHardCodePoint getHardCodePoint;
	@Autowired
	private GdObjectServiceImpl gdObjectServiceImpl;

	/* 手机版招生网站首页 */
	@Override
	public String tzGetHtmlContent(String strParams) {
		String indexHtml = "";
		Map<String, Object> map = null;

		String m_curOPRID = tzLoginServiceImpl.getLoginedManagerOprid(request);
		// 测试用
		// m_curOPRID = "TZ_14026";

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);
		String siteId = "";
		if (jacksonUtil.containsKey("siteId")) {
			siteId = jacksonUtil.getString("siteId");
		} else {
			siteId = request.getParameter("siteId");
		}

		AnalysisLcResult analysisLcResult = new AnalysisLcResult();

		try {

			// rootPath;
			String ctxPath = request.getContextPath();

			// 获取语言和机构;
			String siteSQL = "select TZ_SITE_LANG,TZ_JG_ID from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
			Map<String, Object> mapSiteiInfo = sqlQuery.queryForMap(siteSQL, new Object[] { siteId });
			String strLangID = "";
			String orgId = "";
			if (mapSiteiInfo != null) {
				strLangID = mapSiteiInfo.get("TZ_SITE_LANG") == null ? "ZHS"
						: String.valueOf(mapSiteiInfo.get("TZ_SITE_LANG"));
				orgId = mapSiteiInfo.get("TZ_JG_ID") == null ? "" : String.valueOf(mapSiteiInfo.get("TZ_JG_ID"));
			}

			// 得到登录链接的url
			String loginOutUrl = "";

			// 得到登录地址;
			loginOutUrl = tzCookie.getStringCookieVal(request, "TZGD_LOGIN_URL");

			if (loginOutUrl == null || "".equals(loginOutUrl)) {
				loginOutUrl = ctxPath + "/user/login/" + orgId.toLowerCase() + "/" + siteId;
			}

			// 个人维护信息双语
			String strModifyLabel = validateUtil.getMessageTextWithLanguageCd(orgId, strLangID, "TZ_INDEXSITE_MESSAGE",
					"1", "修改", "Modify");
			String strMshXhLabel = validateUtil.getMessageTextWithLanguageCd(orgId, strLangID, "TZ_INDEXSITE_MESSAGE",
					"2", "申请号", "Application number");
			String strRegEmailLabel = validateUtil.getMessageTextWithLanguageCd(orgId, strLangID,
					"TZ_INDEXSITE_MESSAGE", "3", "邮箱", "Registered mail");
			String strCityLabel = validateUtil.getMessageTextWithLanguageCd(orgId, strLangID, "TZ_INDEXSITE_MESSAGE",
					"4", "所在城市", "City");
			String strSiteMsgLabel = validateUtil.getMessageTextWithLanguageCd(orgId, strLangID, "TZ_INDEXSITE_MESSAGE",
					"5", "系统消息", "System message");
			String strMyActLabel = validateUtil.getMessageTextWithLanguageCd(orgId, strLangID, "TZ_INDEXSITE_MESSAGE",
					"6", "我的活动", "My activities");

			String strViewJdLabel = validateUtil.getMessageTextWithLanguageCd(orgId, strLangID, "TZ_INDEXSITE_MESSAGE",
					"7", "查看进度", "查看进度");

			String strAppHisLabel = validateUtil.getMessageTextWithLanguageCd(orgId, strLangID, "TZ_INDEXSITE_MESSAGE",
					"8", "查看历史报名", "查看历史报名");
			String strViewBmbDesc = validateUtil.getMessageTextWithLanguageCd(orgId, strLangID, "TZ_INDEXSITE_MESSAGE",
					"ckbm", "查看报名表", "查看报名表");
			// title;
			String title = "清华MBA招生";
			// css和js
			String jsCss = tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_INDEX_JS_CSS", ctxPath);
			// 头部 ;
			String topHtml = tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_INDEX_TOP", ctxPath,
					loginOutUrl);
			// 查看进度链接;
			String viewJdUrl = ctxPath + "/dispatcher?classid=mAppstatus&siteId=" + siteId;
			// 查看历史报名
			String viewAppHisUrl = ctxPath + "/dispatcher?classid=mAppHistory&siteId=" + siteId;
			/* 个人信息 */;
			// 账户管理链接;
			String accountMngUrl = ctxPath + "/dispatcher?classid=phZhgl&siteId=" + siteId;
			// 系统消息;
			String znxListUrl = ctxPath + "/dispatcher?classid=znxList&siteId=" + siteId;
			// 已经报名的活动;
			String myActivityYetUrl = ctxPath + "/dispatcher?classid=myActivity&siteId=" + siteId;

			// 照片;
			String imgSql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetUserHeadImg");
			Map<String, Object> mapUserHeadImg = sqlQuery.queryForMap(imgSql, new Object[] { m_curOPRID });
			String strPhoto = "";
			if (null != mapUserHeadImg) {
				String strPhotoDir = mapUserHeadImg.get("TZ_ATT_A_URL") == null ? ""
						: String.valueOf(mapUserHeadImg.get("TZ_ATT_A_URL"));
				String strPhotoName = mapUserHeadImg.get("TZ_ATTACHSYSFILENA") == null ? ""
						: String.valueOf(mapUserHeadImg.get("TZ_ATTACHSYSFILENA"));

				if (!"".equals(strPhotoDir) && !"".equals(strPhotoName)) {
					if (strPhotoDir.lastIndexOf("/") == strPhotoDir.length() - 1) {
						strPhoto = ctxPath + strPhotoDir + strPhotoName;
					} else {
						strPhoto = ctxPath + strPhotoDir + "/" + strPhotoName;
					}

				}

			}
			if ("".equals(strPhoto)) {
				strPhoto = ctxPath + "/statics/css/website/m/images/defaultPhone.png";
			}

			String strName = "";
			String strApplicationNum = "";
			String strRegEmail = "";
			String strCity = "";
			String infoSql = "SELECT OPRID,TZ_REALNAME,TZ_EMAIL,TZ_MSH_ID FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_DLZH_ID=? AND TZ_JG_ID=?";
			Map<String, Object> siteMap = sqlQuery.queryForMap(infoSql, new Object[] { m_curOPRID, orgId });

			if (siteMap != null) {
				strName = (String) siteMap.get("TZ_REALNAME");
				strRegEmail = (String) siteMap.get("TZ_EMAIL");
				strApplicationNum = (String) siteMap.get("TZ_MSH_ID");
			}

			String cityInfo = "select TZ_LEN_PROID from PS_TZ_REG_USER_T where OPRID=?";
			strCity = sqlQuery.queryForObject(cityInfo, new Object[] { m_curOPRID }, "String");
			// 未读站内信数量
			int MsgCount = 0;
			String MsgSql = "select count(1) from PS_TZ_ZNX_REC_T where TZ_ZNX_RECID=? and TZ_ZNX_STATUS='N' and TZ_REC_DELSTATUS<>'Y'";
			MsgCount = sqlQuery.queryForObject(MsgSql, new Object[] { m_curOPRID }, "int");
			String strMsgCount = "";

			// 我已报名但未过期的活动数量
			int actCount = 0;
			String actSql = "select count(1) from PS_TZ_ART_HD_TBL A,PS_TZ_NAUDLIST_T B  where A.TZ_ART_ID=B.TZ_ART_ID and B.TZ_NREG_STAT in ('1','4') and A.TZ_START_DT IS NOT NULL AND A.TZ_START_TM IS NOT NULL AND A.TZ_END_DT IS NOT NULL AND A.TZ_END_TM IS NOT NULL  AND str_to_date(concat(DATE_FORMAT(A.TZ_START_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(A.TZ_START_TM,'%H:%i'),':00'),'%Y/%m/%d %H:%i:%s') <= now()  AND str_to_date(concat(DATE_FORMAT(A.TZ_END_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(A.TZ_END_TM,'%H:%i'),':59'),'%Y/%m/%d %H:%i:%s') >= now() AND B.OPRID=?";
			actCount = sqlQuery.queryForObject(actSql, new Object[] { m_curOPRID }, "int");
			String strActCount = "";
			// System.out.println("strActCount=" + strActCount);
			if (MsgCount > 0) {
				strMsgCount = "<div id=\"msgCount\" class=\"circle\"><span class=\"circle_span\" id=\"msgCountNum\">"
						+ MsgCount + "</span></div>";
			} else {
				strMsgCount = "<div id=\"msgCount\" class=\"circle\" style=\"display:none\"><span class=\"circle_span\" id=\"msgCountNum\">"
						+ MsgCount + "</span></div>";
			}

			if (actCount > 0) {
				strActCount = "<div id=\"actCount\" class=\"circle\"><span class=\"circle_span\" id=\"actCountNum\">"
						+ actCount + "</span></div>";
			} else {
				strActCount = "<div id=\"actCount\" class=\"circle\" style=\"display:none\"><span class=\"circle_span\" id=\"actCountNum\">"
						+ actCount + "</span></div>";
			}

			String personHtml = tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_INDEX_GRXX_INFO_HTML",
					strModifyLabel, strRegEmailLabel, strMshXhLabel, strCityLabel, strSiteMsgLabel, strMyActLabel,
					strPhoto, StringEscapeUtils.escapeHtml(strName), StringEscapeUtils.escapeHtml(strRegEmail),
					strApplicationNum, StringEscapeUtils.escapeHtml(strCity), strMsgCount, strActCount, accountMngUrl,
					znxListUrl, myActivityYetUrl);

			/* 招生进度 */
			String xmjdHtml = "";

			// 添加逻辑：只有不在黑名单中且可以申请报名的,如果有没报名的班级则直接显示在线报名;
			// 1:是否允许报名,"N"表示不允许报名;
			String isAllowedApp = "";
			// 需要查询考生允许报名表 ；
			isAllowedApp = sqlQuery.queryForObject("select TZ_ALLOW_APPLY from PS_TZ_REG_USER_T where OPRID=?",
					new Object[] { m_curOPRID }, "String");
			// 黑名单
			String isBlack = sqlQuery.queryForObject("select TZ_BLACK_NAME from PS_TZ_REG_USER_T where OPRID=?",
					new Object[] { m_curOPRID }, "String");
			// 报考方向选择链接;
			String bkfxUrl = ctxPath + "/dispatcher?classid=ChosBatch&siteId=" + siteId;
			// 是否已经显示报名中心内容申请按钮;
			boolean isShowApply = false;
			if (!"Y".equals(isBlack) && "Y".equals(isAllowedApp)) {
				// 是否有没报名的班级;
				String isNoBmClassSQL = "SELECT count(1) FROM  PS_TZ_CLASS_INF_T where TZ_PRJ_ID IN (SELECT TZ_PRJ_ID FROM PS_TZ_PROJECT_SITE_T WHERE TZ_SITEI_ID=?) AND TZ_JG_ID=? and TZ_IS_APP_OPEN='Y' and TZ_APP_START_DT IS NOT NULL AND TZ_APP_START_TM IS NOT NULL AND TZ_APP_END_DT IS NOT NULL AND TZ_APP_END_TM IS NOT NULL AND str_to_date(concat(DATE_FORMAT(TZ_APP_START_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(TZ_APP_START_TM,'%H:%i'),':00'),'%Y/%m/%d %H:%i:%s') <= now() AND str_to_date(concat(DATE_FORMAT(TZ_APP_END_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(TZ_APP_END_TM,'%H:%i'),':59'),'%Y/%m/%d %H:%i:%s') >= now() AND TZ_CLASS_ID NOT IN (select TZ_CLASS_ID from PS_TZ_FORM_WRK_T where OPRID=?)";
				int hasNoBmNum = sqlQuery.queryForObject(isNoBmClassSQL, new Object[] { siteId, orgId, m_curOPRID },
						"Integer");
				if (hasNoBmNum > 0) {
					/*
					 * int hasBmTotal = sqlQuery.queryForObject(
					 * "select count(1) from PS_TZ_FORM_WRK_T where OPRID=? and TZ_CLASS_ID in (SELECT TZ_CLASS_ID FROM  PS_TZ_CLASS_INF_T where TZ_PRJ_ID IN  (SELECT TZ_PRJ_ID FROM PS_TZ_PROJECT_SITE_T WHERE TZ_SITEI_ID=?)  AND TZ_JG_ID=?)"
					 * ,new Object[] { m_curOPRID,siteId,orgId },"Integer");
					 * //有没有历史报名; if(hasBmTotal > 0){ String hisUrl = ctxPath +
					 * "/dispatcher?classid=applyHis&siteId="+siteId;
					 * 
					 * }else{
					 * 
					 * }
					 */
					// 是否报名;
					String appinsSQL = "select count(1) from PS_TZ_FORM_WRK_T where OPRID=? and TZ_CLASS_ID in (SELECT TZ_CLASS_ID FROM  PS_TZ_CLASS_INF_T where TZ_PRJ_ID IN (SELECT TZ_PRJ_ID FROM PS_TZ_PROJECT_SITE_T WHERE TZ_SITEI_ID=?) AND TZ_JG_ID=?)";
					int zxbmHis = sqlQuery.queryForObject(appinsSQL, new Object[] { m_curOPRID, siteId, orgId },
							"Integer");
					if (zxbmHis > 0) {
						xmjdHtml = tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_INDEX_ZSJD_APPLY_HTML",
								ctxPath, bkfxUrl, strAppHisLabel, viewAppHisUrl);
					} else {
						xmjdHtml = tzGDObject.getHTMLText(
								"HTML.TZMobileWebsiteIndexBundle.TZ_M_INDEX_ZSJD_APPLY_NO_HIS_HTML", ctxPath, bkfxUrl);
					}

					isShowApply = true;
				}
			}

			if (isShowApply == false) {
				// 是否开通了班级;
				String totalSQL = "SELECT count(1) FROM  PS_TZ_CLASS_INF_T where TZ_PRJ_ID IN (SELECT TZ_PRJ_ID FROM PS_TZ_PROJECT_SITE_T WHERE TZ_SITEI_ID=?) AND TZ_JG_ID=? and TZ_IS_APP_OPEN='Y' and TZ_APP_START_DT IS NOT NULL AND TZ_APP_START_TM IS NOT NULL AND TZ_APP_END_DT IS NOT NULL AND TZ_APP_END_TM IS NOT NULL AND str_to_date(concat(DATE_FORMAT(TZ_APP_START_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(TZ_APP_START_TM,'%H:%i'),':00'),'%Y/%m/%d %H:%i:%s') <= now() AND str_to_date(concat(DATE_FORMAT(TZ_APP_END_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(TZ_APP_END_TM,'%H:%i'),':59'),'%Y/%m/%d %H:%i:%s') >= now()";
				int totalNum = sqlQuery.queryForObject(totalSQL, new Object[] { siteId, orgId }, "Integer");
				// 是否报名;
				String appinsSQL = "select TZ_APP_INS_ID,TZ_CLASS_ID,TZ_BATCH_ID from PS_TZ_FORM_WRK_T where OPRID=? and TZ_CLASS_ID in (SELECT TZ_CLASS_ID FROM  PS_TZ_CLASS_INF_T where TZ_PRJ_ID IN (SELECT TZ_PRJ_ID FROM PS_TZ_PROJECT_SITE_T WHERE TZ_SITEI_ID=?) AND TZ_JG_ID=?) order by ROW_LASTMANT_DTTM desc limit 0,1";
				long TZ_APP_INS_ID = 0;
				String classId = "";
				String msPcId = "";
				String msPcName = "";
				Map<String, Object> classAndBmbMap = new HashMap<String, Object>();
				try {
					classAndBmbMap = sqlQuery.queryForMap(appinsSQL, new Object[] { m_curOPRID, siteId, orgId });
					if (classAndBmbMap != null) {
						TZ_APP_INS_ID = Long.parseLong(String.valueOf(classAndBmbMap.get("TZ_APP_INS_ID")));
						classId = String.valueOf(classAndBmbMap.get("TZ_CLASS_ID"));
						msPcId = String.valueOf(classAndBmbMap.get("TZ_BATCH_ID"));
						if (classId != null && !"".equals(classId) && msPcId != null && !"".equals(msPcId)) {
							msPcName = sqlQuery.queryForObject(
									"select TZ_BATCH_NAME from PS_TZ_CLS_BATCH_T where TZ_CLASS_ID=? and TZ_BATCH_ID=?",
									new Object[] { classId, msPcId }, "String");
							if (msPcName == null) {
								msPcName = "";
							}
						}
					}
				} catch (NullPointerException nullException) {
					TZ_APP_INS_ID = 0;
					classId = "";
				}
				if (classId == null) {
					classId = "";
				}

				// 已经报名
				if (TZ_APP_INS_ID > 0 && !"".equals(classId)) {
					// 班级名称；
					String className = sqlQuery.queryForObject(
							"select TZ_CLASS_NAME from PS_TZ_CLASS_INF_T where TZ_CLASS_ID =?",
							new Object[] { classId }, "String");

					// 报名流程模型实例是否存在;
					int bmlcTotalNum = sqlQuery.queryForObject(
							"select count(1) from PS_TZ_CLS_BMLC_T where TZ_CLASS_ID=?", new Object[] { classId },
							"Integer");
					String stepHtml = "";
					// 报名表Url;
					String applyFromUrl = ctxPath + "/dispatcher?classid=appId&TZ_CLASS_ID=" + classId + "&SITE_ID="
							+ siteId;

					if (bmlcTotalNum > 0) {

						String bmlcSql = "select a.TZ_APPPRO_ID,a.TZ_APPPRO_NAME,b.TZ_APPPRO_HF_BH,b.TZ_APPPRO_RST from PS_TZ_CLS_BMLC_T a left join (select * from PS_TZ_APPPRO_RST_T where TZ_APP_INS_ID=? and TZ_CLASS_ID=?) b on a.TZ_APPPRO_ID=b.TZ_APPPRO_ID where a.TZ_CLASS_ID=? order by a.TZ_SORT_NUM asc";
						List<Map<String, Object>> bmlcList = sqlQuery.queryForList(bmlcSql,
								new Object[] { TZ_APP_INS_ID, classId, classId });
						int step = 0;

						// 未发布的一个流程紫色，后面的灰色;
						boolean lcZsBl = false;
						// 上个流程是不是发布了;
						boolean sgIsFb = false;
						if (bmlcList != null && bmlcList.size() > 0) {
							for (int j = 0; j < bmlcList.size(); j++) {
								step = step + 1;
								// 是否发布;
								String isFb = "";

								String TZ_APPPRO_ID = (String) bmlcList.get(j).get("TZ_APPPRO_ID");
								String TZ_APPPRO_NAME = (String) bmlcList.get(j).get("TZ_APPPRO_NAME");
								String TZ_APPPRO_HF_BH = (String) bmlcList.get(j).get("TZ_APPPRO_HF_BH");
								String TZ_APPPRO_RST = (String) bmlcList.get(j).get("TZ_APPPRO_RST");
								if (TZ_APPPRO_NAME == null) {
									TZ_APPPRO_NAME = "";
								}
								if (TZ_APPPRO_HF_BH == null) {
									TZ_APPPRO_HF_BH = "";
								}
								if (TZ_APPPRO_RST == null) {
									TZ_APPPRO_RST = "";
								}

								String TZ_SYSVAR = "";

								// 没有发布回复短语则统一取默认的
								// update by caoy @2018-3-14 回复短语如果有配置系统变量
								// 就显示内容，有系统变量就显示 系统变量获取的东西
								if (TZ_APPPRO_HF_BH == null || "".equals(TZ_APPPRO_HF_BH)) {
									map = sqlQuery.queryForMap(
											"select TZ_APPPRO_CONTENT,TZ_SYSVAR from PS_TZ_CLS_BMLCHF_T where TZ_CLASS_ID=? and TZ_APPPRO_ID=? and TZ_WFB_DEFALT_BZ='on'",
											new Object[] { classId, TZ_APPPRO_ID });
									if (map != null) {
										TZ_APPPRO_RST = map.get("TZ_APPPRO_CONTENT") == null ? ""
												: map.get("TZ_APPPRO_CONTENT").toString();
										TZ_SYSVAR = map.get("TZ_SYSVAR") == null ? "" : map.get("TZ_SYSVAR").toString();
									}
								}

								// 有系统变量 优先显示系统变量，如果没有，显示内容
								if (TZ_SYSVAR != null && !"".equals(TZ_SYSVAR)) {
									String type = "A";
									// 解析系统变量;
									String[] result = analysisLcResult.analysisLcNOZWF(type, String.valueOf(TZ_APP_INS_ID),
											ctxPath, TZ_SYSVAR, "Y", siteId);

									isFb = result[0];
									if (result[1] != null && !"".equals(result[1])) {
										TZ_APPPRO_RST = result[1];
									}
								}

								String stepLi = "";
								String styleClassName = "", stepNum = "";
								String styleLine = "";
								if ("Y".equals(isFb)) {
									// 流程打勾样式
									styleClassName = " step_ed";
									stepNum = "";
								} else {
									// 未发布的一个流程紫色，或则前面的流程是发布的;
									if (lcZsBl == false || sgIsFb == true) {
										styleClassName = " step_ing";
										stepNum = String.valueOf(step);
										lcZsBl = true;
									} else {
										styleClassName = "";
										stepNum = String.valueOf(step);
									}

								}

								if (sgIsFb == true) {
									styleLine = " step_lined";
								} else {
									styleLine = "";
								}

								if (step == 1) {
									stepLi = tzGDObject.getHTMLText(
											"HTML.TZMobileWebsiteIndexBundle.TZ_M_APPCENTER_LC_SETP1", styleClassName,
											stepNum, TZ_APPPRO_NAME);
								} else {
									stepLi = tzGDObject.getHTMLText(
											"HTML.TZMobileWebsiteIndexBundle.TZ_M_APPCENTER_LC_SETP", styleClassName,
											stepNum, TZ_APPPRO_NAME, styleLine);
								}

								stepHtml = stepHtml + stepLi;

								if ("Y".equals(isFb)) {
									sgIsFb = true;
								} else {
									sgIsFb = false;
								}

							}
						}

					}
					xmjdHtml = tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_INDEX_ZSJD_HTML",
							className + msPcName, stepHtml, strViewBmbDesc, applyFromUrl, strAppHisLabel, viewAppHisUrl,
							viewJdUrl);
				} else {
					// 不可报名；
					xmjdHtml = tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_INDEX_NO_ZSJD_HTML");
				}
			}

			// 招生活动,报考通知;
			// 取栏目id;
			String columnIds = sqlQuery.queryForObject(
					"select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT='TZ_M_WEB_NOTICE'", "String");
			String hdHtml = "";
			String hdheadLabel = "";
			String hdTitle = "";
			if (columnIds != null && !"".equals(columnIds)) {
				String[] columns = columnIds.split(",");
				for (int i = 0; i < columns.length; i++) {
					String currentColumnId = columns[i];

					// 获取栏目名称;
					String columnNameSQL = "SELECT TZ_COLU_NAME FROM PS_TZ_SITEI_COLU_T WHERE TZ_SITEI_ID=? and TZ_COLU_ID=?";
					String columnName = sqlQuery.queryForObject(columnNameSQL, new Object[] { siteId, currentColumnId },
							"String");

					if (columnName != null) {
						if (i == 0) {
							hdheadLabel = "<li class=\"list_on\" date-column=\"" + currentColumnId + "\">" + columnName
									+ "</li>";
						} else {
							hdheadLabel = hdheadLabel + "<li date-column=\"" + currentColumnId + "\">" + columnName
									+ "</li>";
						}
					}

					// 根据栏目下已发布的文章列表，每个栏目限制3条
					String artListSql = tzGDObject
							.getSQLText("SQL.TZMobileWebsiteIndexBundle.TZ_HD_TZ_ART_LIST_BY_LIMIT");

					List<Map<String, Object>> artList = sqlQuery.queryForList(artListSql,
							new Object[] { siteId, currentColumnId, m_curOPRID, 3 });

					String titleLi = "";
					if (artList != null && artList.size() > 0) {

						for (int j = 0; j < artList.size(); j++) {
							String artId = (String) artList.get(j).get("TZ_ART_ID");
							String artTitle = (String) artList.get(j).get("TZ_ART_TITLE");
							String artTitleStyle = (String) artList.get(j).get("TZ_ART_TITLE_STYLE");

							// 活动通知链接;
							String artUrl = (String) artList.get(j).get("TZ_ART_URL");
							if (artUrl == null) {
								artUrl = "";
							}

							String hotAndNewImg = "";
							int showImgNum = 0;
							if (artTitleStyle != null && !"".equals(artTitleStyle)) {
								if (artTitleStyle.indexOf("HOT") > -1) {
									hotAndNewImg = "<img class=\"fr add_hot\" src=\"" + ctxPath
											+ "/statics/css/website/m/images/hot.png\">";
									showImgNum++;
								}
								if (artTitleStyle.indexOf("NEW") > -1) {
									hotAndNewImg = hotAndNewImg + "<img class=\"fr add_hot\" src=\"" + ctxPath
											+ "/statics/css/website/m/images/new.png\">";
									showImgNum++;
								}
							}

							if (showImgNum == 2) {
								titleLi = titleLi + "<li><a href=\"" + artUrl + "\"><p>" + artTitle + "</p>"
										+ hotAndNewImg + "</a></li>";
							} else {
								if (showImgNum == 1) {
									titleLi = titleLi + "<li><a href=\"" + artUrl + "\"><p style=\"width:83%\">"
											+ artTitle + "</p>" + hotAndNewImg + "</a></li>";
								} else {
									titleLi = titleLi + "<li><a href=\"" + artUrl + "\"><p style=\"width:91%\">"
											+ artTitle + "</p></a></li>";
								}
							}

							/*
							 * String hotTagDisplay = "none"; String
							 * newTagDisplay = "none";
							 * 
							 * if(artTitleStyle!=null&&!"".equals(artTitleStyle)
							 * ){ hotTagDisplay =
							 * artTitleStyle.indexOf("HOT")>-1?"block":
							 * hotTagDisplay; newTagDisplay =
							 * artTitleStyle.indexOf("NEW")>-1?"block":
							 * newTagDisplay; }
							 * 
							 * StringBuffer sbArtUrl = new
							 * StringBuffer(contextPath).append(
							 * "/dispatcher?classid=art_preview&operatetype=HTML&siteId=")
							 * .append(strSiteId).append("&columnId=").append(
							 * currentColumnId).append("&artId=").append(artId);
							 * 
							 * artContentTabLisHtml =
							 * artContentTabLisHtml.append(tzGDObject.
							 * getHTMLText(
							 * "HTML.TZWebSiteAreaInfoBundle.TZ_SITE_AREA_HDTZ_TAB_LI_HTML",
							 * sbArtUrl.toString(),
							 * artTitle,artDate,hotTagDisplay,newTagDisplay));
							 */
						}
					}

					if ("".equals(titleLi)) {
						titleLi = "暂无";
					}
					if (i == 0) {
						hdTitle = hdTitle + "<div class=\"list_body dis_block\">" + titleLi + "</div>";
					} else {
						hdTitle = hdTitle + "<div class=\"list_body dis_none\">" + titleLi + "</div>";
					}

				}
				hdHtml = tzGDObject.getHTMLTextForDollar("HTML.TZMobileWebsiteIndexBundle.TZ_M_INDEX_HD_HTML",
						hdheadLabel, hdTitle, siteId);
			}

			// 快捷菜单;
			// 申请指导;
			// 对应的栏目id;
			String sqzdColumnId = sqlQuery.queryForObject(
					"select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?",
					new Object[] { "TZ_M_SQZD_COLUMN_ID" }, "String");
			// 对应的文章id;
			String sqzdArtid = sqlQuery.queryForObject(
					"select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?",
					new Object[] { "TZ_M_SQZD_ART_ID" }, "String");
			String sqzd = sqlQuery.queryForObject(
					"SELECT case A.TZ_ART_TYPE1 when 'B' then A.TZ_OUT_ART_URL  else  B.TZ_ART_URL END TZ_ART_URL FROM PS_TZ_ART_REC_TBL A INNER JOIN PS_TZ_LM_NR_GL_T B ON(A.TZ_ART_ID=B.TZ_ART_ID AND B.TZ_SITE_ID=? AND B.TZ_ART_PUB_STATE='Y' AND B.TZ_COLU_ID=? AND A.TZ_ART_ID=?)",
					new Object[] { siteId, sqzdColumnId, sqzdArtid }, "String");
			// 报考自测;
			// String bkzc = ctxPath + "/exam/"+orgId.toLowerCase()+"/"+siteId;
			// String bkzc = ctxPath +
			// "/dispatcher?tzParams={\"ComID\":\"TZ_CAN_TSINGHUA_COM\",\"PageID\":\"TZ_CAN_TSING_STD\",\"OperateType\":\"HTML\",\"comParams\":{\"isMobile\":\"Y\"}}";
			String bkzc = ctxPath + "/dispatcher?classid=canInTsing&isMobile=Y";
			// 在线预约;
			String zxyy = ctxPath + "/dispatcher?classid=InterviewMobile&siteId=" + siteId;
			// 资料专区;
			// 对应的栏目id;
			String zlzqColumnId = sqlQuery.queryForObject(
					"select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?",
					new Object[] { "TZ_ZLZQ_COLUMN_ID" }, "String");
			String zlzq = ctxPath + "/dispatcher?classid=mEventNotice&siteId=" + siteId + "&columnId=" + zlzqColumnId;
			// 常见问题;
			String cjwt = sqlQuery.queryForObject(
					"select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?",
					new Object[] { "TZ_M_CJWT_URL" }, "String");
			String kjcdHtml = tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_INDEX_KJCD_HTML", ctxPath,
					sqzd, bkzc, viewJdUrl, zxyy, zlzq, cjwt);

			// 展示内容
			String content = topHtml + personHtml + xmjdHtml + hdHtml + kjcdHtml;
			content = tzGDObject.getHTMLTextForDollar("HTML.TZMobileWebsiteIndexBundle.TZ_M_INDEX_CONTENT_HTML",
					content);
			String JGID = sqlQuery.queryForObject("select TZ_JG_ID from PS_TZ_SITEI_DEFN_T WHERE TZ_SITEI_ID=?",new Object[]{siteId},"String");
			
			if (JGID.equals("SEM")) {
				JGID="";
			} else {
				JGID.toLowerCase();
			}
			System.out.println(this.getClass().getName()+":"+JGID);
			indexHtml = tzGDObject.getHTMLTextForDollar("HTML.TZMobileWebsiteIndexBundle.TZ_MOBILE_BASE_HTML", title,
					ctxPath, jsCss, siteId, "1", content,JGID);
		} catch (TzSystemException e) {
			// TODO Auto-generated catch block
			indexHtml = "";
			e.printStackTrace();
		}

		return indexHtml;
	}

	public String tzOther(String operateType, String strParams, String[] errMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);

		try {

			String orgId = jacksonUtil.getString("orgId");
			String siteId = jacksonUtil.getString("siteId");

			if ("verify".equals(operateType)) {
				String success = "", url = "";

				String ctxPath = request.getContextPath();

				String dlzhId = tzCookie.getStringCookieVal(request, "TZGD_TOKEN_DLZH");
				String loginUrl = tzCookie.getStringCookieVal(request, "TZGD_LOGIN_URL");

				if (loginUrl == null || "".equals(loginUrl)) {
					loginUrl = ctxPath + "/user/login/" + orgId.toLowerCase() + "/" + siteId;
				}
				if (!"".equals(dlzhId) && dlzhId != null) {
					ArrayList<String> aryErrorMsg = new ArrayList<String>();
					String sql = "SELECT OPERPSWD FROM PSOPRDEFN WHERE OPRID=(SELECT OPRID FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_DLZH_ID=?)";
					String passwordJm = sqlQuery.queryForObject(sql, new Object[] { dlzhId }, "String");
					String password = DESUtil.decrypt(passwordJm, "TZGD_Tranzvision");

					boolean boolResult = tzWebsiteLoginServiceImpl.doLogin(request, response, orgId, siteId, dlzhId,
							password, "", "ZHS", aryErrorMsg);
					if (boolResult) {
						success = "true";
						url = "";
					} else {
						success = "false";
						url = loginUrl;
					}
				} else {
					success = "false";
					url = loginUrl;
				}

				mapRet.put("success", success);
				mapRet.put("url", url);
				strRet = jacksonUtil.Map2json(mapRet);
			} else {
				if ("ZNXHDNUM".equals(operateType)) {
					// 刷新报名数和站内信未读数量;
					String m_curOPRID = tzLoginServiceImpl.getLoginedManagerOprid(request);
					// 未读站内信数量
					int msgCount = 0;
					String MsgSql = "select count(1) from PS_TZ_ZNX_REC_T where TZ_ZNX_RECID=? and TZ_ZNX_STATUS='N' and TZ_REC_DELSTATUS<>'Y'";
					msgCount = sqlQuery.queryForObject(MsgSql, new Object[] { m_curOPRID }, "int");

					// 我已报名但未过期的活动数量
					int actCount = 0;
					String actSql = "select count(1) from PS_TZ_ART_HD_TBL A,PS_TZ_NAUDLIST_T B  where A.TZ_ART_ID=B.TZ_ART_ID and B.TZ_NREG_STAT in ('1','4') and A.TZ_START_DT IS NOT NULL AND A.TZ_START_TM IS NOT NULL AND A.TZ_END_DT IS NOT NULL AND A.TZ_END_TM IS NOT NULL  AND str_to_date(concat(DATE_FORMAT(A.TZ_START_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(A.TZ_START_TM,'%H:%i'),':00'),'%Y/%m/%d %H:%i:%s') <= now()  AND str_to_date(concat(DATE_FORMAT(A.TZ_END_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(A.TZ_END_TM,'%H:%i'),':59'),'%Y/%m/%d %H:%i:%s') >= now() AND B.OPRID=?";
					actCount = sqlQuery.queryForObject(actSql, new Object[] { m_curOPRID }, "int");
					mapRet.put("msgCount", msgCount);
					mapRet.put("actCount", actCount);
					strRet = jacksonUtil.Map2json(mapRet);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return strRet;
	}
}
