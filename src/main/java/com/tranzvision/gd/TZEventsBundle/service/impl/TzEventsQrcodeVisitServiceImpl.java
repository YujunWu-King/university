/**
 * 
 */
package com.tranzvision.gd.TZEventsBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.qrcode.CreateQRCode;
import com.tranzvision.gd.util.session.TzGetSetSessionValue;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 活动手机发布二维码页面，原PS：TZ_GD_HDGL:ActivityView
 * 
 * @author SHIHUA
 * @since 2016-02-04
 */
@Service("com.tranzvision.gd.TZEventsBundle.service.impl.TzEventsQrcodeVisitServiceImpl")
public class TzEventsQrcodeVisitServiceImpl extends FrameworkImpl {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzGDObject;

	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	@Autowired
	private TzGetSetSessionValue tzGetSetSessionValue;
	
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private CreateQRCode createQRCode;

	@Override
	public String tzGetHtmlContent(String strParams) {

		String strRet = "";

		try {

			String siteId = request.getParameter("siteId");
			String columnId = request.getParameter("columnId");
			String artId = request.getParameter("artId");

			if (null != siteId && !"".equals(siteId) && null != columnId && !"".equals(columnId) && null != artId
					&& !"".equals(artId)) {

				String sql = "select TZ_MENU_ID from PS_TZ_SITEI_MENU_T where TZ_SITEI_ID=? and TZ_MENU_COLUMN=? and TZ_MENU_STATE='Y' and TZ_MENU_COLUMN<>'' and  TZ_MENU_COLUMN is not null";
				String menuId = sqlQuery.queryForObject(sql, new Object[] { siteId, columnId }, "String");
				tzGetSetSessionValue.setTzSiteGloMenuId(menuId);

				sql = "select TZ_ART_TYPE1,TZ_OUT_ART_URL from PS_TZ_ART_REC_TBL where TZ_ART_ID = ?";
				Map<String, Object> mapData = sqlQuery.queryForMap(sql, new Object[] { artId });

				if (mapData != null) {
					String artType = mapData.get("TZ_ART_TYPE1") == null ? ""
							: String.valueOf(mapData.get("TZ_ART_TYPE1"));
					String artOutUrl = mapData.get("TZ_OUT_ART_URL") == null ? ""
							: String.valueOf(mapData.get("TZ_OUT_ART_URL"));

					if ("B".equals(artType)) {
						if (!"".equals(artOutUrl)) {
							strRet = tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzDoLoginRedirectScript", artOutUrl);
						} else {
							strRet = "未定义外部链接";
						}
					} else {
						sql = "select TZ_ART_NEWS_DT,TZ_ART_SJ_CONT_SCR from PS_TZ_LM_NR_GL_T where TZ_SITE_ID=? and TZ_COLU_ID=? and TZ_ART_ID=?";

						Map<String, Object> mapArt = sqlQuery.queryForMap(sql,
								new Object[] { siteId, columnId, artId });

						String dtFormat = getSysHardCodeVal.getDateTimeFormat();
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dtFormat);

						String dtArtNews = mapArt.get("TZ_ART_NEWS_DT") == null ? ""
								: String.valueOf(mapArt.get("TZ_ART_NEWS_DT"));

						Date dtArt = simpleDateFormat.parse(dtArtNews);
						Date dtNow = new Date();

						if (dtArt.getTime() < dtNow.getTime()) {
							strRet = mapArt.get("TZ_ART_SJ_CONT_SCR") == null ? ""
									: String.valueOf(mapArt.get("TZ_ART_SJ_CONT_SCR"));
						} else {
							strRet = "当前时间不可查看该内容";
						}
					}

				} else {
					strRet = "参数错误，请联系系统管理员。";
				}

			} else {
				strRet = "参数错误，请联系系统管理员。";
			}

		} catch (Exception e) {
			e.printStackTrace();
			strRet = "系统异常，请联系系统管理员。";
		}

		return strRet;
	}

	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			
			String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);

			jacksonUtil.json2Map(strParams);

			String activityId = jacksonUtil.getString("activityId");
			String siteId = jacksonUtil.getString("siteId");
			String coluId = jacksonUtil.getString("coluId");

			String qrcodeFileName = "TZ_ART_EVENT_" + siteId + "_" + coluId + "_" + activityId + ".png";

			String ctxPath = request.getContextPath();

			String qrcodeUrl = request.getProtocol() + request.getServerName() + ":"
					+ String.valueOf(request.getServerPort()) + ctxPath + "/event/m/" + siteId + "/" + coluId + "/"
					+ activityId;

			String qrcodeFilePath = createQRCode.encodeQRCode(orgid, qrcodeUrl, qrcodeFileName);

			Map<String, Object> mapJson = new HashMap<String, Object>();
			mapJson.put("codeImage", qrcodeFilePath);
			mapJson.put("phonePreviewUrl", qrcodeUrl);

			mapRet.put("formData", mapJson);
			strRet = jacksonUtil.Map2json(mapJson);

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "查询失败！" + e.getMessage();
			Map<String, Object> mapJson = new HashMap<String, Object>();
			mapJson.put("codeImage", e.getMessage());
			mapJson.put("phonePreviewUrl", e.getMessage());

			mapRet.put("formData", mapJson);
			strRet = jacksonUtil.Map2json(mapJson);
		}

		return strRet;
	}

}
