package com.tranzvision.gd.TZWebSiteStationLetterBundle.service.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.MessageTextServiceImpl;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 招生网站站内信详细信息
 *
 */
@Service("com.tranzvision.gd.TZWebSiteStationLetterBundle.service.impl.StationLetterInfoServiceImpl")
public class StationLetterInfoServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private MessageTextServiceImpl messageTextServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TZGDObject tzGDObject;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	@Autowired
	private SiteRepCssServiceImpl siteRepCssServiceImpl;
	@Autowired
	private FliterForm fliterForm;

	/****** 站内信详情 ********/
	@Override
	public String tzGetHtmlContent(String strParams) {
		//返回值
		String znxCenterHtml = "";
		JacksonUtil jacksonUtil = new JacksonUtil();

		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		try {
			jacksonUtil.json2Map(strParams);
			String strSiteId = "";
			String strMailId = "";
			strSiteId = request.getParameter("siteId");
			strMailId = request.getParameter("mailId");

			// 根据siteid得到机构id;
			String str_jg_id = "";
			// language;
			String language = "";
			String strCssDir = "";
			String siteSQL = "select TZ_JG_ID,TZ_SKIN_STOR,TZ_SITE_LANG from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
			Map<String, Object> siteMap = jdbcTemplate.queryForMap(siteSQL, new Object[] { strSiteId });
			if (siteMap != null) {
				str_jg_id = (String) siteMap.get("TZ_JG_ID");
				String skinstor = (String) siteMap.get("TZ_SKIN_STOR");
				language = (String) siteMap.get("TZ_SITE_LANG");
				String websitePath = getSysHardCodeVal.getWebsiteCssPath();

				String strRandom = String.valueOf(10 * Math.random());
				if ("".equals(skinstor) || skinstor == null) {
					strCssDir = request.getContextPath() + websitePath + "/" + str_jg_id.toLowerCase() + "/" + strSiteId
							+ "/" + "style_" + str_jg_id.toLowerCase() + ".css?v=" + strRandom;
				} else {
					strCssDir = request.getContextPath() + websitePath + "/" + str_jg_id.toLowerCase() + "/" + strSiteId
							+ "/" + skinstor + "/" + "style_" + str_jg_id.toLowerCase() + ".css?v=" + strRandom;
				}
			}

			if (language == null || "".equals(language)) {   
				language = "ZHS";
			}
			//站内信内容页面双语化
			String znxContent = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_ZNX_INFO_MESSAGE", "1",
					language, "站内信内容", "站内信内容");
			String znxReturn = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_ZNX_INFO_MESSAGE", "2",
					language, "返回", "返回");
			String znxPrev = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_ZNX_INFO_MESSAGE", "3",
					language, "上一个", "上一个");
			String znxNext = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_ZNX_INFO_MESSAGE", "4",
					language, "下一个", "下一个");
			// 通用链接;
			String dispatcher = request.getContextPath() + "/dispatcher";
			String znxRecId = "";
			String znxSendName = "";
			String znxSubject = "";
			String znxAddTime = "";
			String znxText = "";
			String znxInfoSQL = "select TZ_ZNX_RECID,TZ_ZNX_SENDNAME,TZ_MSG_SUBJECT,ROW_ADDED_DTTM,TZ_MSG_TEXT from PS_TZ_ZNX_MSG_VW where TZ_ZNX_MSGID=?";
			Map<String, Object> znxInfoMap = jdbcTemplate.queryForMap(znxInfoSQL, new Object[] { strMailId });
			if (znxInfoMap != null){
				znxRecId = (String) znxInfoMap.get("TZ_ZNX_RECID");
				znxSendName = (String) znxInfoMap.get("TZ_ZNX_SENDNAME");
				znxSubject = (String) znxInfoMap.get("TZ_MSG_SUBJECT");
				znxAddTime = (String) znxInfoMap.get("ROW_ADDED_DTTM").toString();
				znxText = (String) znxInfoMap.get("TZ_MSG_TEXT");
			}
			String znxStatusSql = "select TZ_ZNX_STATUS from PS_TZ_ZNX_REC_T WHERE TZ_ZNX_MSGID = ? and TZ_ZNX_SENDID = ?";
			String znxStatus = jdbcTemplate.queryForObject(znxStatusSql, new Object[] { strMailId,oprid },"String");
			if (znxStatus!= null && znxStatus.equals("N")){
				String updateStatusSql = "UPDATE PS_TZ_ZNX_REC_T SET TZ_ZNX_STATUS = 'Y' WHERE TZ_ZNX_MSGID = ? and TZ_ZNX_SENDID = ?";
				jdbcTemplate.update(updateStatusSql,new Object[]{strMailId,oprid});
			}
			//站内信内容
			String znxInfoHtml = tzGDObject.getHTMLText("HTML.TZWebStationLetterMgBundle.TZ_WEB_ZNX_INFO_CONTENT",
					true,request.getContextPath(),znxContent,znxNext,znxPrev,znxReturn,znxSendName,znxSubject,znxAddTime,znxText,strMailId);
			// 展示页面;
			znxCenterHtml = tzGDObject.getHTMLText("HTML.TZWebStationLetterMgBundle.TZ_WEB_ZNX_VIEW_HTML",
					true,request.getContextPath(), dispatcher,strCssDir,znxInfoHtml, str_jg_id, strSiteId);

			znxCenterHtml = siteRepCssServiceImpl.repTitle(znxCenterHtml, strSiteId);
			znxCenterHtml = siteRepCssServiceImpl.repCss(znxCenterHtml, strSiteId);
			
			return znxCenterHtml;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

}
