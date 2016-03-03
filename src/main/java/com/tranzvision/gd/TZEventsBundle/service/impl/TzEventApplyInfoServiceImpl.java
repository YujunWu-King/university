/**
 * 
 */
package com.tranzvision.gd.TZEventsBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdObjectServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.security.TzFilterIllegalCharacter;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 查看已报名信息，原PS：TZ_APPONLINE_PKG:ViewAppInfo
 * 
 * @author SHIHUA
 * @since 2016-03-01
 */
@Service("com.tranzvision.gd.TZEventsBundle.service.impl.TzEventApplyInfoServiceImpl")
public class TzEventApplyInfoServiceImpl extends FrameworkImpl {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TZGDObject tzGDObject;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TzFilterIllegalCharacter tzFilterIllegalCharacter;

	@Autowired
	private GdObjectServiceImpl gdObjectServiceImpl;

	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	@Override
	public String tzGetHtmlContent(String strParams) {

		String strRet = "";

		try {

			JacksonUtil jacksonUtil = new JacksonUtil();
			jacksonUtil.json2Map(strParams);

			// 活动ID
			String strApplyId = jacksonUtil.getString("APPLYID");
			strApplyId = tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(strApplyId);

			String str_apply_sta = "已报名";

			// 双语化
			String sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzGetSiteLang");
			String tzSiteLang = sqlQuery.queryForObject(sql, new Object[] { strApplyId }, "String");

			String appliedSuccess = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPLICATION_MSG",
					"MSG23", tzSiteLang, "已报名", "Applied Successful");
			String number = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPLICATION_MSG", "MSG24",
					tzSiteLang, "序号", "No.");
			String realName = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPLICATION_MSG", "MSG25",
					tzSiteLang, "姓名", "Name");
			String appliedTime = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPLICATION_MSG",
					"MSG26", tzSiteLang, "报名时间", "Application Period");
			String status = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPLICATION_MSG", "MSG27",
					tzSiteLang, "状态", "Status");
			String goback = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPLICATION_MSG", "MSG13",
					tzSiteLang, "返回", "Back");

			str_apply_sta = appliedSuccess;

			sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzGetEventApplyList");
			List<Map<String, Object>> listApply = sqlQuery.queryForList(sql, new Object[] { strApplyId });

			String dtFormat = getSysHardCodeVal.getDateTimeFormat();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dtFormat);
			int rownum = 0;
			String listHtml = "";
			for (Map<String, Object> mapApply : listApply) {
				rownum++;
				String strName = mapApply.get("TZ_CYR_NAME") == null ? "" : String.valueOf(mapApply.get("TZ_CYR_NAME"));
				String applyDt = mapApply.get("TZ_REG_TIME") == null ? ""
						: simpleDateFormat.format(mapApply.get("TZ_REG_TIME"));
				listHtml = listHtml + tzGDObject.getHTMLText("HTML.TZEventsBundle.TZ_APPLY_ONLINE_LIST_BODY",
						String.valueOf(rownum), strName, applyDt, str_apply_sta);
			}

			strRet = tzGDObject.getHTMLText("HTML.TZEventsBundle.TZ_APPLY_ONLINE_LIST_HEAD", listHtml, number, realName,
					appliedTime, status, goback);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return strRet;

	}

}
