package com.tranzvision.gd.TZMobileWebsiteIndexBundle.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZApplicationCenterBundle.service.impl.AnalysisLcResult;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.ValidateUtil;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 报名状态 classId: mAppstatus
 * 
 * @author tang
 *
 */
@Service("com.tranzvision.gd.TZMobileWebsiteIndexBundle.service.impl.MobileWebsiteAppStatusServiceImpl")
public class MobileWebsiteAppStatusServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TZGDObject tzGDObject;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private ValidateUtil validateUtil;

	/* 手机版招生网站报名表状态查询 */
	@Override
	public String tzGetHtmlContent(String strParams) {
		String html = "";
		Map<String, Object> map = null;
		// rootPath;
		String ctxPath = request.getContextPath();

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
			// title;
			String title = "状态查询 ";
			// css和js
			String jsCss = tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_APP_STATUS_JS_CSS", ctxPath);

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

			// 内容
			String xmjdHtml = "";
			String submitTimeDesc = "提交时间:";
			// 是否开通了班级;
			String totalSQL = "SELECT count(1) FROM  PS_TZ_CLASS_INF_T where TZ_PRJ_ID IN (SELECT TZ_PRJ_ID FROM PS_TZ_PROJECT_SITE_T WHERE TZ_SITEI_ID=?) AND TZ_JG_ID=? and TZ_IS_APP_OPEN='Y' and TZ_APP_START_DT IS NOT NULL AND TZ_APP_START_TM IS NOT NULL AND TZ_APP_END_DT IS NOT NULL AND TZ_APP_END_TM IS NOT NULL AND str_to_date(concat(DATE_FORMAT(TZ_APP_START_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(TZ_APP_START_TM,'%H:%i'),':00'),'%Y/%m/%d %H:%i:%s') <= now() AND str_to_date(concat(DATE_FORMAT(TZ_APP_END_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(TZ_APP_END_TM,'%H:%i'),':59'),'%Y/%m/%d %H:%i:%s') >= now()";
			int totalNum = sqlQuery.queryForObject(totalSQL, new Object[] { siteId, orgId }, "Integer");
			// 是否报名;
			String appinsSQL = "select TZ_APP_INS_ID,TZ_CLASS_ID,DATE_FORMAT(ROW_LASTMANT_DTTM,'%Y-%m-%d') ROW_LASTMANT_DT from PS_TZ_FORM_WRK_T where OPRID=? and TZ_CLASS_ID in (SELECT TZ_CLASS_ID FROM  PS_TZ_CLASS_INF_T where TZ_PRJ_ID IN (SELECT TZ_PRJ_ID FROM PS_TZ_PROJECT_SITE_T WHERE TZ_SITEI_ID=?) AND TZ_JG_ID=?) order by ROW_LASTMANT_DTTM desc limit 0,1";
			long TZ_APP_INS_ID = 0;
			String classId = "";
			Map<String, Object> classAndBmbMap = new HashMap<String, Object>();
			try {
				classAndBmbMap = sqlQuery.queryForMap(appinsSQL, new Object[] { m_curOPRID, siteId, orgId });
				if (classAndBmbMap != null) {
					TZ_APP_INS_ID = Long.parseLong(String.valueOf(classAndBmbMap.get("TZ_APP_INS_ID")));
					classId = String.valueOf(classAndBmbMap.get("TZ_CLASS_ID"));
					submitTimeDesc = submitTimeDesc + String.valueOf(classAndBmbMap.get("ROW_LASTMANT_DT"));
				}
			} catch (NullPointerException nullException) {
				TZ_APP_INS_ID = 0;
				classId = "";
			}
			if (classId == null) {
				classId = "";
			}
			String className = "";
			String content = "";
			// 已经报名
			if (TZ_APP_INS_ID > 0 && !"".equals(classId)) {
				// 班级名称；
				className = sqlQuery.queryForObject("select TZ_CLASS_NAME from PS_TZ_CLASS_INF_T where TZ_CLASS_ID =?",
						new Object[] { classId }, "String");

				// 报名流程模型实例是否存在;
				int bmlcTotalNum = sqlQuery.queryForObject("select count(1) from PS_TZ_CLS_BMLC_T where TZ_CLASS_ID=?",
						new Object[] { classId }, "Integer");

				if (bmlcTotalNum > 0) {

					String bmlcSql = "select a.TZ_APPPRO_ID,a.TZ_APPPRO_NAME,b.TZ_APPPRO_HF_BH,b.TZ_APPPRO_RST from PS_TZ_CLS_BMLC_T a left join (select * from PS_TZ_APPPRO_RST_T where TZ_APP_INS_ID=? and TZ_CLASS_ID=?) b on a.TZ_APPPRO_ID=b.TZ_APPPRO_ID where a.TZ_CLASS_ID=? order by a.TZ_SORT_NUM asc";
					List<Map<String, Object>> bmlcList = sqlQuery.queryForList(bmlcSql,
							new Object[] { TZ_APP_INS_ID, classId, classId });
					int step = 0;
					String stepHtml = "";
					// 未发布的一个流程紫色，后面的灰色;
					boolean lcZsBl = false;
					// 上个流程是不是发布了;
					String[] jdInfo = { "一", "二", "三", "四", "五", "六", "七", "八", "九", "十" };
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

							if ("Y".equals(isFb)) {
								xmjdHtml = xmjdHtml + tzGDObject.getHTMLTextForDollar(
										"HTML.TZMobileWebsiteIndexBundle.TZ_M_APP_STATUS_INNER_DIV", "ed",
										"阶段" + jdInfo[step - 1] + ":" + TZ_APPPRO_NAME, TZ_APPPRO_RST, " circled", "");
							} else {
								// 如果是未发布且第一步直接紫色
								if (step == 1 || sgIsFb) {
									xmjdHtml = xmjdHtml + tzGDObject.getHTMLTextForDollar(
											"HTML.TZMobileWebsiteIndexBundle.TZ_M_APP_STATUS_INNER_DIV", "",
											"阶段" + jdInfo[step - 1] + ":" + TZ_APPPRO_NAME, TZ_APPPRO_RST, " circling",
											String.valueOf(step));
								} else {
									xmjdHtml = xmjdHtml + tzGDObject.getHTMLTextForDollar(
											"HTML.TZMobileWebsiteIndexBundle.TZ_M_APP_STATUS_INNER_DIV", "",
											"阶段" + jdInfo[step - 1] + ":" + TZ_APPPRO_NAME, TZ_APPPRO_RST, "",
											String.valueOf(step));
								}

							}

							// 标记上一步是不是发布;
							if ("Y".equals(isFb)) {
								sgIsFb = true;
							} else {
								sgIsFb = false;
							}

						}
					}
				}
				content = tzGDObject.getHTMLTextForDollar("HTML.TZMobileWebsiteIndexBundle.TZ_M_APP_STATUS_HTML", title,
						className, submitTimeDesc, xmjdHtml);
			} else {
				// 手机版暂不开通申请
				content = tzGDObject.getHTMLTextForDollar("HTML.TZMobileWebsiteIndexBundle.TZ_M_NO_APP_HTML", ctxPath);
			}
			String JGID = sqlQuery.queryForObject("select TZ_JG_ID from PS_TZ_SITEI_DEFN_T WHERE TZ_SITEI_ID=?",new Object[]{siteId},"String");
			
			if (JGID.equals("SEM")) {
				JGID="";
			} else {
				JGID.toLowerCase();
			}
			System.out.println(this.getClass().getName()+":"+JGID);
			html = tzGDObject.getHTMLTextForDollar("HTML.TZMobileWebsiteIndexBundle.TZ_MOBILE_BASE_HTML", title,
					ctxPath, jsCss, siteId, "3", content,JGID);
		} catch (TzSystemException e) {
			// TODO Auto-generated catch block
			html = "";
			e.printStackTrace();
		}
		return html;
	}

}
