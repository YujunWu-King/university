/**
 * 
 */
package com.tranzvision.gd.TZEventsBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FileManageServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZEventsBundle.dao.PsTzArtHdTblMapper;
import com.tranzvision.gd.TZEventsBundle.dao.PsTzZxbmXxxETMapper;
import com.tranzvision.gd.TZEventsBundle.dao.PsTzZxbmXxxTMapper;
import com.tranzvision.gd.TZEventsBundle.model.PsTzArtHdTbl;
import com.tranzvision.gd.TZEventsBundle.model.PsTzZxbmXxxET;
import com.tranzvision.gd.TZEventsBundle.model.PsTzZxbmXxxT;
import com.tranzvision.gd.TZEventsBundle.model.PsTzZxbmXxxTKey;
import com.tranzvision.gd.TZWebSiteInfoBundle.service.impl.ArtContentHtml;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.dao.PsTzArtFileTMapper;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.dao.PsTzArtFjjTMapper;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.dao.PsTzArtPicTMapper;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.dao.PsTzArtRecTblMapper;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.dao.PsTzArtTitimgTMapper;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.dao.PsTzArtTpjTMapper;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.dao.PsTzLmNrGlTMapper;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.model.PsTzArtFileTKey;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.model.PsTzArtFjjT;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.model.PsTzArtPicT;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.model.PsTzArtPicTKey;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.model.PsTzArtRecTbl;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.model.PsTzArtRecTblWithBLOBs;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.model.PsTzArtTitimgT;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.model.PsTzArtTpjT;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.model.PsTzLmNrGlT;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.model.PsTzLmNrGlTWithBLOBs;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.ResizeImageUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.MySqlLockService;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 活动基本信息，原PS：TZ_GD_HDGL:ActivityInfo
 * 
 * @author SHIHUA
 * @since 2016-02-04
 */
@Service("com.tranzvision.gd.TZEventsBundle.service.impl.TzEventsInfoServiceImpl")
public class TzEventsInfoServiceImpl extends FrameworkImpl {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private PsTzLmNrGlTMapper psTzLmNrGlTMapper;

	@Autowired
	private TZGDObject tzGDObject;

	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	@Autowired
	private GetSeqNum getSeqNum;

	@Autowired
	private MySqlLockService mySqlLockService;

	@Autowired
	private ResizeImageUtil resizeImageUtil;

	@Autowired
	private TzEventsItemOptionsServiceImpl tzEventsItemOptionsServiceImpl;

	@Autowired
	private FileManageServiceImpl fileManageServiceImpl;

	@Autowired
	private PsTzArtTitimgTMapper psTzArtTitimgTMapper;

	@Autowired
	private PsTzArtFjjTMapper psTzArtFjjTMapper;

	@Autowired
	private PsTzArtTpjTMapper psTzArtTpjTMapper;

	@Autowired
	private PsTzArtHdTblMapper psTzArtHdTblMapper;

	@Autowired
	private PsTzArtRecTblMapper psTzArtRecTblMapper;

	@Autowired
	private PsTzZxbmXxxTMapper psTzZxbmXxxTMapper;

	@Autowired
	private PsTzZxbmXxxETMapper psTzZxbmXxxETMapper;

	@Autowired
	private PsTzArtFileTMapper psTzArtFileTMapper;

	@Autowired
	private PsTzArtPicTMapper psTzArtPicTMapper;
	
	@Autowired
	private ArtContentHtml artContentHtml;

	/**
	 * 获取活动信息
	 */
	@Override
	public String tzQuery(String strParams, String[] errorMsg) {
		String strRet = "";
		String sql = "";
		try {
			JacksonUtil jacksonUtil = new JacksonUtil();
			jacksonUtil.json2Map(strParams);

			String ctxPath = request.getContextPath();

			String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);

			String enabledApply = "N";

			String activityId = jacksonUtil.getString("activityId");
			String siteId = jacksonUtil.getString("siteId");
			String coluId = jacksonUtil.getString("coluId");

			if (null == siteId || "".equals(siteId)) {
				sql = "select TZ_SITEI_ID from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ENABLE='Y' and TZ_JG_ID=? ORDER BY TZ_ADDED_DTTM DESC";
				siteId = sqlQuery.queryForObject(sql, new Object[] { orgid }, "String");

				if (null == siteId || "".equals(siteId)) {
					errorMsg[0] = "1";
					errorMsg[1] = "未配置活动站点";
					strRet = this.genEventJsonString("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
							"", "", "", "", "", "", "", "", "", "");
					return strRet;
				}
			}

			if (null == coluId || "".equals(coluId)) {
				sql = "select TZ_COLU_ID from PS_TZ_SITEI_COLU_T where TZ_SITEI_ID=? and TZ_CONT_TYPE='A'";
				coluId = sqlQuery.queryForObject(sql, new Object[] { siteId }, "String");

				if (null == coluId || "".equals(coluId)) {
					errorMsg[0] = "1";
					errorMsg[1] = "未配置活动栏目";
					strRet = this.genEventJsonString("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
							"", "", "", "", "", "", "", "", "", "");
					return strRet;
				}
			}

			sql = "select TZ_IMG_VIEW,TZ_ATTS_VIEW from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
			Map<String, Object> mapData = sqlQuery.queryForMap(sql, new Object[] { siteId });

			String saveImageAccessUrl = "";
			String saveAttachAccessUrl = "";
			if (mapData != null) {
				saveImageAccessUrl = mapData.get("TZ_IMG_VIEW") == null ? ""
						: String.valueOf(mapData.get("TZ_IMG_VIEW"));
				saveAttachAccessUrl = mapData.get("TZ_ATTS_VIEW") == null ? ""
						: String.valueOf(mapData.get("TZ_ATTS_VIEW"));
			}
			if (null == activityId || "".equals(activityId)) {

				strRet = this.genEventJsonString("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
						"", "", "", "", "", "", saveImageAccessUrl, saveAttachAccessUrl, "");
				return strRet;
			}

			// 获取活动信息
			sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzGetEventInfo");
			Map<String, Object> mapEventInfo = sqlQuery.queryForMap(sql, new Object[] { activityId, siteId, coluId });

			if (null == mapEventInfo) {
				errorMsg[0] = "1";
				errorMsg[1] = "活动数据不存在。";
				strRet = this.genEventJsonString("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
						"", "", "", "", "", "", "", "", "");
				return strRet;
			}

			enabledApply = mapEventInfo.get("TZ_QY_ZXBM") == null ? "" : String.valueOf(mapEventInfo.get("TZ_QY_ZXBM"));

			String attachSysFile = mapEventInfo.get("TZ_ATTACHSYSFILENA") == null ? ""
					: String.valueOf(mapEventInfo.get("TZ_ATTACHSYSFILENA"));
			String titleImageUrl = "";
			if (!"".equals(attachSysFile)) {
				sql = "select TZ_ATT_A_URL from PS_TZ_ART_TITIMG_T where TZ_ATTACHSYSFILENA=?";
				String acessUrl = sqlQuery.queryForObject(sql, new Object[] { attachSysFile }, "String");
				titleImageUrl = acessUrl + "/" + attachSysFile;
				titleImageUrl.replace("//", "/");
			}

			String activityName = mapEventInfo.get("TZ_NACT_NAME") == null ? ""
					: String.valueOf(mapEventInfo.get("TZ_NACT_NAME"));
			String contentInfo = mapEventInfo.get("TZ_ART_CONENT") == null ? ""
					: String.valueOf(mapEventInfo.get("TZ_ART_CONENT"));
			String titleImageTitle = mapEventInfo.get("TZ_IMAGE_TITLE") == null ? ""
					: String.valueOf(mapEventInfo.get("TZ_IMAGE_TITLE"));
			String titleImageDesc = mapEventInfo.get("TZ_IMAGE_DESC") == null ? ""
					: String.valueOf(mapEventInfo.get("TZ_IMAGE_DESC"));
			String activityPlace = mapEventInfo.get("TZ_NACT_ADDR") == null ? ""
					: String.valueOf(mapEventInfo.get("TZ_NACT_ADDR"));
			String activityCity = mapEventInfo.get("TZ_HD_CS") == null ? ""
					: String.valueOf(mapEventInfo.get("TZ_HD_CS"));

			String publishStatus = mapEventInfo.get("TZ_ART_PUB_STATE") == null ? ""
					: String.valueOf(mapEventInfo.get("TZ_ART_PUB_STATE"));
			String publishUrl = "";
			if ("Y".equals(publishStatus)) {
				sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzGetEventViewClassId");
				String classId = sqlQuery.queryForObject(sql, "String");
				publishUrl = ctxPath + "/dispatcher?classid=" + classId + "&operatetype=HTML&siteId=" + siteId
						+ "&columnId=" + coluId + "&artId=" + activityId + "&oprate=R";
			}

			String externalLink = mapEventInfo.get("TZ_OUT_ART_URL") == null ? ""
					: String.valueOf(mapEventInfo.get("TZ_OUT_ART_URL"));
			String applyNum = mapEventInfo.get("TZ_XWS") == null ? "" : String.valueOf(mapEventInfo.get("TZ_XWS"));
			String showModel = mapEventInfo.get("TZ_XSMS") == null ? "" : String.valueOf(mapEventInfo.get("TZ_XSMS"));
			String artType = mapEventInfo.get("TZ_ART_TYPE1") == null ? ""
					: String.valueOf(mapEventInfo.get("TZ_ART_TYPE1"));

			String dttFormat = getSysHardCodeVal.getDateTimeFormat();
			String dtFormat = getSysHardCodeVal.getDateFormat();
			String tmFormat = getSysHardCodeVal.getTimeHMFormat();

			SimpleDateFormat dtimeSimpleDateFormat = new SimpleDateFormat(dttFormat);
			SimpleDateFormat dateSimpleDateFormat = new SimpleDateFormat(dtFormat);
			SimpleDateFormat timeSimpleDateFormat = new SimpleDateFormat(tmFormat);

			Date activityStartDate = mapEventInfo.get("TZ_START_DT") == null ? null
					: dtimeSimpleDateFormat.parse(String.valueOf(mapEventInfo.get("TZ_START_DT")));
			Date activityStartTime = mapEventInfo.get("TZ_START_TM") == null ? null
					: dtimeSimpleDateFormat.parse(String.valueOf(mapEventInfo.get("TZ_START_TM")));
			Date activityEndDate = mapEventInfo.get("TZ_END_DT") == null ? null
					: dtimeSimpleDateFormat.parse(String.valueOf(mapEventInfo.get("TZ_END_DT")));
			Date activityEndTime = mapEventInfo.get("TZ_END_TM") == null ? null
					: dtimeSimpleDateFormat.parse(String.valueOf(mapEventInfo.get("TZ_END_TM")));
			Date applyStartDate = mapEventInfo.get("TZ_APPF_DT") == null ? null
					: dtimeSimpleDateFormat.parse(String.valueOf(mapEventInfo.get("TZ_APPF_DT")));
			Date applyStartTime = mapEventInfo.get("TZ_APPF_TM") == null ? null
					: dtimeSimpleDateFormat.parse(String.valueOf(mapEventInfo.get("TZ_APPF_TM")));
			Date applyEndDate = mapEventInfo.get("TZ_APPE_DT") == null ? null
					: dtimeSimpleDateFormat.parse(String.valueOf(mapEventInfo.get("TZ_APPE_DT")));
			Date applyEndTime = mapEventInfo.get("TZ_APPE_TM") == null ? null
					: dtimeSimpleDateFormat.parse(String.valueOf(mapEventInfo.get("TZ_APPE_TM")));

			String strActivityStartDate = "";
			if (null != activityStartDate) {
				strActivityStartDate = dateSimpleDateFormat.format(activityStartDate);
			}

			String strActivityStartTime = "";
			if (null != activityStartTime) {
				strActivityStartTime = timeSimpleDateFormat.format(activityStartTime);
			}

			String strActivityEndDate = "";
			if (null != activityEndDate) {
				strActivityEndDate = dateSimpleDateFormat.format(activityEndDate);
			}

			String strActivityEndTime = "";
			if (null != activityEndTime) {
				strActivityEndTime = timeSimpleDateFormat.format(activityEndTime);
			}

			String strApplyStartDate = "";
			if (null != applyStartDate) {
				strApplyStartDate = dateSimpleDateFormat.format(applyStartDate);
			}

			String strApplyStartTime = "";
			if (null != applyStartTime) {
				strApplyStartTime = timeSimpleDateFormat.format(applyStartTime);
			}

			String strApplyEndDate = "";
			if (null != applyEndDate) {
				strApplyEndDate = dateSimpleDateFormat.format(applyEndDate);
			}

			String strApplyEndTime = "";
			if (null != applyEndTime) {
				strApplyEndTime = timeSimpleDateFormat.format(applyEndTime);
			}

			strRet = this.genEventJsonString(activityId, activityName, strActivityStartDate, strActivityStartTime,
					strActivityEndDate, strActivityEndTime, activityPlace, activityCity, externalLink, contentInfo,
					titleImageTitle, titleImageDesc, titleImageUrl, enabledApply, strApplyStartDate, strApplyStartTime,
					strApplyEndDate, strApplyEndTime, applyNum, showModel, publishStatus, publishUrl, siteId, coluId,
					saveImageAccessUrl, saveAttachAccessUrl, artType);

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "查询失败！" + e.getMessage();
			strRet = this.genEventJsonString("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
					"", "", "", "", "", "", "", "");
		}

		return strRet;
	}

	/**
	 * 生成活动基本信息的json报文
	 * 
	 * @param activityId
	 * @param activityName
	 * @param activityStartDate
	 * @param activityStartTime
	 * @param activityEndDate
	 * @param activityEndTime
	 * @param activityPlace
	 * @param activityCity
	 * @param externalLink
	 * @param contentInfo
	 * @param titleImageTitle
	 * @param titleImageDesc
	 * @param titleImageUrl
	 * @param enabledApply
	 * @param applyStartDate
	 * @param applyStartTime
	 * @param applyEndDate
	 * @param applyEndTime
	 * @param applyNum
	 * @param showModel
	 * @param publishStatus
	 * @param publishUrl
	 * @param siteId
	 * @param coluId
	 * @param saveImageAccessUrl
	 * @param saveAttachAccessUrl
	 * @param artType
	 * @return
	 */
	private String genEventJsonString(String activityId, String activityName, String strActivityStartDate,
			String strActivityStartTime, String strActivityEndDate, String strActivityEndTime, String activityPlace,
			String activityCity, String externalLink, String contentInfo, String titleImageTitle, String titleImageDesc,
			String titleImageUrl, String enabledApply, String strApplyStartDate, String strApplyStartTime,
			String strApplyEndDate, String strApplyEndTime, String applyNum, String showModel, String publishStatus,
			String publishUrl, String siteId, String coluId, String saveImageAccessUrl, String saveAttachAccessUrl,
			String artType) {
		String strRet = "";

		Map<String, Object> mapJson = new HashMap<String, Object>();
		mapJson.put("activityId", activityId);
		mapJson.put("activityName", activityName);
		mapJson.put("activityStartDate", strActivityStartDate);
		mapJson.put("activityStartTime", strActivityStartTime);
		mapJson.put("activityEndDate", strActivityEndDate);
		mapJson.put("activityEndTime", strActivityEndTime);
		mapJson.put("activityPlace", activityPlace);
		mapJson.put("activityCity", activityCity);
		mapJson.put("externalLink", externalLink);
		mapJson.put("contentInfo", contentInfo);
		mapJson.put("titleImageTitle", titleImageTitle);
		mapJson.put("titleImageDesc", titleImageDesc);
		mapJson.put("titleImageUrl", titleImageUrl);
		mapJson.put("enabledApply", enabledApply);
		mapJson.put("applyStartDate", strApplyStartDate);
		mapJson.put("applyStartTime", strApplyStartTime);
		mapJson.put("applyEndDate", strApplyEndDate);
		mapJson.put("applyEndTime", strApplyEndTime);
		mapJson.put("applyNum", applyNum);
		mapJson.put("showModel", showModel);
		mapJson.put("publishStatus", publishStatus);
		mapJson.put("publishUrl", publishUrl);
		mapJson.put("siteId", siteId);
		mapJson.put("coluId", coluId);
		mapJson.put("saveImageAccessUrl", saveImageAccessUrl);
		mapJson.put("saveAttachAccessUrl", saveAttachAccessUrl);
		mapJson.put("artType", artType);

		JacksonUtil jacksonUtil = new JacksonUtil();

		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("formData", mapJson);

		strRet = jacksonUtil.Map2json(mapRet);

		return strRet;
	}

	/**
	 * 新增活动信息
	 */
	@Override
	@Transactional
	public String tzAdd(String[] actData, String[] errorMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			Date dateNow = new Date();
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

			String ctxPath = request.getContextPath();

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {

				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				String strFlag = jacksonUtil.getString("typeFlag");
				Map<String, Object> mapData = jacksonUtil.getMap("data");

				String activityId = "";
				if ("ACTINFO".equals(strFlag)) {
					activityId = this.saveActivity(mapData, errorMsg);
				}

				String siteId = mapData.get("siteId") == null ? "" : String.valueOf(mapData.get("siteId"));
				String coluId = mapData.get("coluId") == null ? "" : String.valueOf(mapData.get("coluId"));
				String publishStatus = mapData.get("publishClick") == null ? ""
						: String.valueOf(mapData.get("publishClick"));

				String sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzGetEventViewClassId");
				String classId = sqlQuery.queryForObject(sql, "String");
				String publishUrl = ctxPath + "/dispatcher?classid=" + classId + "&operatetype=HTML&siteId=" + siteId
						+ "&columnId=" + coluId + "&artId=" + activityId + "&oprate=R";

				sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzGetEventPreviewClassId");
				classId = sqlQuery.queryForObject(sql, "String");
				String viewUrl = ctxPath + "/dispatcher?classid=" + classId + "&operatetype=HTML&siteId=" + siteId
						+ "&columnId=" + coluId + "&artId=" + activityId + "&oprate=R";

				Map<String, Object> mapRet = new HashMap<String, Object>();
				mapRet.put("activityId", activityId);
				mapRet.put("siteId", siteId);
				mapRet.put("coluId", coluId);
				mapRet.put("publishUrl", publishUrl);
				mapRet.put("viewUrl", viewUrl);

				strRet = jacksonUtil.Map2json(mapRet);

				// 生成页面代码
				String tzArtHtml = artContentHtml.getContentHtml(siteId, coluId, activityId);

				// 更新文章内容关联表
				PsTzLmNrGlTWithBLOBs psTzLmNrGlTWithBLOBs = new PsTzLmNrGlTWithBLOBs();
				psTzLmNrGlTWithBLOBs.setTzSiteId(siteId);
				psTzLmNrGlTWithBLOBs.setTzColuId(coluId);
				psTzLmNrGlTWithBLOBs.setTzArtId(activityId);
				psTzLmNrGlTWithBLOBs.setTzArtHtml(tzArtHtml);
				psTzLmNrGlTWithBLOBs.setTzLastmantDttm(dateNow);
				psTzLmNrGlTWithBLOBs.setTzLastmantOprid(oprid);

				if ("Y".equals(publishStatus)) {
					psTzLmNrGlTWithBLOBs.setTzArtConentScr(tzArtHtml);
				} else if ("N".equals(publishStatus)) {
					psTzLmNrGlTWithBLOBs.setTzArtConentScr("");
				}

				psTzLmNrGlTMapper.updateByPrimaryKeySelective(psTzLmNrGlTWithBLOBs);

			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "添加失败。" + e.getMessage();
		}

		return strRet;
	}

	/**
	 * 更新活动信息
	 */
	@Override
	@Transactional
	public String tzUpdate(String[] actData, String[] errorMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			String ctxPath = request.getContextPath();

			Date dateNow = new Date();
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {

				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				String strFlag = jacksonUtil.getString("typeFlag");
				Map<String, Object> mapParams = jacksonUtil.getMap("data");

				String activityId = "";
				switch (strFlag) {
				case "ACTINFO":
					// 活动基本信息
					activityId = this.saveActivity(mapParams, errorMsg);
					break;
				case "ACTAPPLYINFO":
					// 活动信息项信息
					activityId = this.saveActivityApplyInfo(mapParams, errorMsg);
					break;

				case "ARTATTACHINFO":
					// 活动附件集信息
					activityId = this.saveArtAttachment(mapParams, errorMsg);
					break;
				case "ARTTPJ":
					// 图片集
					activityId = this.saveArtTpj(jacksonUtil, errorMsg);
					break;

				}

				String siteId = mapParams.get("siteId") == null ? "" : String.valueOf(mapParams.get("siteId"));
				String coluId = mapParams.get("coluId") == null ? "" : String.valueOf(mapParams.get("coluId"));
				String publishStatus = mapParams.get("publishClick") == null ? ""
						: String.valueOf(mapParams.get("publishClick"));

				String sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzGetEventViewClassId");
				String classId = sqlQuery.queryForObject(sql, "String");
				String publishUrl = ctxPath + "/dispatcher?classid=" + classId + "&operatetype=HTML&siteId=" + siteId
						+ "&columnId=" + coluId + "&artId=" + activityId + "&oprate=R";

				sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzGetEventPreviewClassId");
				classId = sqlQuery.queryForObject(sql, "String");
				String viewUrl = ctxPath + "/dispatcher?classid=" + classId + "&operatetype=HTML&siteId=" + siteId
						+ "&columnId=" + coluId + "&artId=" + activityId + "&oprate=R";

				Map<String, Object> mapRet = new HashMap<String, Object>();
				mapRet.put("activityId", activityId);
				mapRet.put("siteId", siteId);
				mapRet.put("coluId", coluId);
				mapRet.put("publishUrl", publishUrl);
				mapRet.put("viewUrl", viewUrl);

				strRet = jacksonUtil.Map2json(mapRet);

				// 生成页面代码
				String tzArtHtml = artContentHtml.getContentHtml(siteId, coluId, activityId);
				
				String toGenPhoneHtml;
				//String[] contentPhoneHtml = new String[] {};

				// 更新文章内容关联表
				PsTzLmNrGlTWithBLOBs psTzLmNrGlTWithBLOBs = new PsTzLmNrGlTWithBLOBs();
				psTzLmNrGlTWithBLOBs.setTzSiteId(siteId);
				psTzLmNrGlTWithBLOBs.setTzColuId(coluId);
				psTzLmNrGlTWithBLOBs.setTzArtId(activityId);
				psTzLmNrGlTWithBLOBs.setTzArtHtml(tzArtHtml);
				psTzLmNrGlTWithBLOBs.setTzArtSjHtml("");
				psTzLmNrGlTWithBLOBs.setTzLastmantDttm(dateNow);
				psTzLmNrGlTWithBLOBs.setTzLastmantOprid(oprid);

				if ("Y".equals(publishStatus)) {
					psTzLmNrGlTWithBLOBs.setTzArtConentScr(tzArtHtml);
					psTzLmNrGlTWithBLOBs.setTzArtSjContScr("");
				} else if ("N".equals(publishStatus)) {
					psTzLmNrGlTWithBLOBs.setTzArtConentScr("");
					psTzLmNrGlTWithBLOBs.setTzArtSjContScr("");
				}

				psTzLmNrGlTMapper.updateByPrimaryKeySelective(psTzLmNrGlTWithBLOBs);

			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "";
		}

		return strRet;
	}

	@Override
	@Transactional
	public String tzDelete(String[] actData, String[] errorMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {

				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				String strFlag = jacksonUtil.getString("typeFlag");
				String activityId = jacksonUtil.getString("activityId");
				Map<String, Object> mapParams = jacksonUtil.getMap("data");

				switch (strFlag) {
				case "ACTAPPLYINFO":
					// 活动信息项信息
					this.deleteActivityApplyInfo(activityId, mapParams, errorMsg);
					break;

				case "ARTATTACHINFO":
					// 活动附件集信息
					this.deleteArtAttachment(activityId, mapParams, errorMsg);
					break;
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "删除失败。" + e.getMessage();
		}

		return strRet;
	}

	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {

		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");

		// ArrayList<Map<String, Object>> listData = new ArrayList<Map<String,
		// Object>>();

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);

		// 活动编号
		// String activityId = jacksonUtil.getString("activityId");
		// 信息项id
		// String applyItemId = jacksonUtil.getString("applyItemId");

		try {

			tzEventsItemOptionsServiceImpl.tzQueryList(strParams, numLimit, numStart, errorMsg);

		} catch (Exception e) {

			// 这里的程序是否还有必要？
			String toCheck;

		}

		return jacksonUtil.Map2json(mapRet);

	}

	@Override
	public String tzGetHtmlContent(String strParams) {
		String strRet = "";
		String errorDesc = "";
		String newSysFile = "";
		String minSysFile = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);
		try {

			Map<String, Object> mapParams = jacksonUtil.getMap("data");
			String attachmentType = jacksonUtil.getString("attachmentType");

			if (null != attachmentType && !"".equals(attachmentType) && null != mapParams) {

				attachmentType = attachmentType.toUpperCase();

				String path = mapParams.get("path") == null ? "" : String.valueOf(mapParams.get("path"));
				String sysFileName = mapParams.get("sysFileName") == null ? ""
						: String.valueOf(mapParams.get("sysFileName"));
				String filename = mapParams.get("filename") == null ? "" : String.valueOf(mapParams.get("filename"));
				String accessPath = mapParams.get("accessPath") == null ? ""
						: String.valueOf(mapParams.get("accessPath"));

				String fileDir = "";

				switch (attachmentType) {
				case "IMG":

					if (path.endsWith("/")) {
						fileDir = path;
					} else {
						fileDir = path + "/";
					}

					// 缩小图片
					String flg1 = this.resize(fileDir, sysFileName, 100);
					String flg2 = this.resizeANDbyte(fileDir, sysFileName, 1000);

					if ("Y".equals(flg1)) {
						minSysFile = "mini_" + sysFileName;
					} else {
						minSysFile = sysFileName;
					}

					if ("Y".equals(flg2)) {
						newSysFile = "new_" + sysFileName;
					} else {
						newSysFile = sysFileName;
					}

					PsTzArtTitimgT psTzArtTitimgT = new PsTzArtTitimgT();
					psTzArtTitimgT.setTzAttachsysfilena(sysFileName);
					psTzArtTitimgT.setTzAttachfileName(filename);
					psTzArtTitimgT.setTzAttPUrl(path);
					psTzArtTitimgT.setTzAttAUrl(accessPath);
					psTzArtTitimgT.setTzYsAttachsysnam(newSysFile);
					psTzArtTitimgT.setTzSlAttachsysnam(minSysFile);

					int rstImg = psTzArtTitimgTMapper.insert(psTzArtTitimgT);
					if (rstImg == 0) {
						errorDesc = "保存数据时发生错误";
					}

					break;

				case "ATTACHMENT":

					PsTzArtFjjT psTzArtFjjT = new PsTzArtFjjT();
					psTzArtFjjT.setTzAttachsysfilena(sysFileName);
					psTzArtFjjT.setTzAttachfileName(filename);
					psTzArtFjjT.setTzAttPUrl(path);
					psTzArtFjjT.setTzAttAUrl(accessPath);

					int rstAtt = psTzArtFjjTMapper.insert(psTzArtFjjT);
					if (rstAtt == 0) {
						errorDesc = "保存数据时发生错误";
					}

					break;

				case "TPJ":

					if (path.endsWith("/")) {
						fileDir = path;
					} else {
						fileDir = path + "/";
					}

					// 缩小图片
					String flg3 = this.resize(fileDir, sysFileName, 100);
					String flg4 = this.resizeANDbyte(fileDir, sysFileName, 1000);

					if ("Y".equals(flg3)) {
						minSysFile = "mini_" + sysFileName;
					} else {
						minSysFile = sysFileName;
					}

					if ("Y".equals(flg4)) {
						newSysFile = "new_" + sysFileName;
					} else {
						newSysFile = sysFileName;
					}

					PsTzArtTpjT psTzArtTpjT = new PsTzArtTpjT();
					psTzArtTpjT.setTzAttachsysfilena(sysFileName);
					psTzArtTpjT.setTzAttachfileName(filename);
					psTzArtTpjT.setTzAttPUrl(path);
					psTzArtTpjT.setTzAttAUrl(accessPath);
					psTzArtTpjT.setTzYsAttachsysnam(newSysFile);
					psTzArtTpjT.setTzSlAttachsysnam(minSysFile);

					int rstTpj = psTzArtTpjTMapper.insert(psTzArtTpjT);
					if (rstTpj == 0) {
						errorDesc = "保存数据时发生错误";
					}

					break;
				}

			} else {
				errorDesc = "参数错误。";
			}

		} catch (Exception e) {
			e.printStackTrace();
			errorDesc = "系统异常。" + e.getMessage();
		}

		int errorCode = 0;
		if (!"".equals(errorDesc)) {
			errorCode = 1;
		}
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("success", errorCode);
		mapRet.put("message", errorDesc);
		mapRet.put("minPicSysFileName", minSysFile);

		strRet = jacksonUtil.Map2json(mapRet);

		return strRet;
	}

	/**
	 * 保存活动信息
	 * 
	 * @param mapParams
	 * @param errorMsg
	 * @return
	 */
	private String saveActivity(Map<String, Object> mapParams, String[] errorMsg) {

		String strRet = "";
		try {

			boolean boolRst = false;
			String sql = "";

			String dtFormat = getSysHardCodeVal.getDateFormat();
			String tmFormat = getSysHardCodeVal.getTimeFormat();

			SimpleDateFormat dtSimpleDateFormat = new SimpleDateFormat(dtFormat);
			SimpleDateFormat tmSimpleDateFormat = new SimpleDateFormat(tmFormat);

			Date dateNow = new Date();
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

			String siteId = mapParams.get("siteId") == null ? "" : String.valueOf(mapParams.get("siteId"));
			String coluId = mapParams.get("coluId") == null ? "" : String.valueOf(mapParams.get("coluId"));
			// 活动编号
			String activityId = mapParams.get("activityId") == null ? "" : String.valueOf(mapParams.get("activityId"));
			// 活动名称
			String activityName = mapParams.get("activityName") == null ? ""
					: String.valueOf(mapParams.get("activityName"));
			// 活动开始日期
			String strActStartDate = mapParams.get("activityStartDate") == null ? ""
					: String.valueOf(mapParams.get("activityStartDate"));
			Date activityStartDate = dtSimpleDateFormat.parse(strActStartDate);
			// 活动开始时间
			String strActStartTime = mapParams.get("activityStartTime") == null ? ""
					: String.valueOf(mapParams.get("activityStartTime"));
			Date activityStartTime = tmSimpleDateFormat.parse(strActStartTime);
			// 活动结束日期
			String strActEndDate = mapParams.get("activityEndDate") == null ? ""
					: String.valueOf(mapParams.get("activityEndDate"));
			Date activityEndDate = dtSimpleDateFormat.parse(strActEndDate);
			// 活动结束时间
			String strActEndTime = mapParams.get("activityEndTime") == null ? ""
					: String.valueOf(mapParams.get("activityEndTime"));
			Date activityEndTime = tmSimpleDateFormat.parse(strActEndTime);
			// 地点
			String activityPlace = mapParams.get("activityPlace") == null ? ""
					: String.valueOf(mapParams.get("activityPlace"));
			// 城市
			String activityCity = mapParams.get("activityCity") == null ? ""
					: String.valueOf(mapParams.get("activityCity"));
			// 文章类型
			String artType = mapParams.get("artType") == null ? "" : String.valueOf(mapParams.get("artType"));
			// 外部URL
			String externalLink = mapParams.get("externalLink") == null ? ""
					: String.valueOf(mapParams.get("externalLink"));
			// 文章内容
			String contentInfo = mapParams.get("contentInfo") == null ? ""
					: String.valueOf(mapParams.get("contentInfo"));
			// 标题图的标题
			String titleImageTitle = mapParams.get("titleImageTitle") == null ? ""
					: String.valueOf(mapParams.get("titleImageTitle"));
			// 标题图的描述
			String titleImageDesc = mapParams.get("titleImageDesc") == null ? ""
					: String.valueOf(mapParams.get("titleImageDesc"));
			// 标题图的URL
			String titleImageUrl = mapParams.get("titleImageUrl") == null ? ""
					: String.valueOf(mapParams.get("titleImageUrl"));
			// 标题图文件名
			String sysFileName = "";
			if (!"".equals(titleImageUrl)) {
				String[] imgAry = titleImageUrl.split("/");
				sysFileName = imgAry[imgAry.length - 1];
			}
			// 是否启用在线报名
			String enabledApply = mapParams.get("enabledApply") == null ? "N"
					: String.valueOf(mapParams.get("enabledApply"));
			// 报名开始日期
			String strApplyStartDate = mapParams.get("applyStartDate") == null ? ""
					: String.valueOf(mapParams.get("applyStartDate"));
			Date applyStartDate = dtSimpleDateFormat.parse(strApplyStartDate);
			// 报名开始时间
			String strApplyStartTime = mapParams.get("applyStartTime") == null ? ""
					: String.valueOf(mapParams.get("applyStartTime"));
			Date applyStartTime = tmSimpleDateFormat.parse(strApplyStartTime);
			// 报名结束日期
			String strApplyEndDate = mapParams.get("applyEndDate") == null ? ""
					: String.valueOf(mapParams.get("applyEndDate"));
			Date applyEndDate = dtSimpleDateFormat.parse(strApplyEndDate);
			// 报名结束时间
			String strApplyEndTime = mapParams.get("applyEndTime") == null ? ""
					: String.valueOf(mapParams.get("applyEndTime"));
			Date applyEndTime = tmSimpleDateFormat.parse(strApplyEndTime);
			// 席位数
			int applyNum = mapParams.get("applyNum") == null ? 0
					: Integer.parseInt(String.valueOf(mapParams.get("applyNum")));
			// 显示模式
			String showModel = mapParams.get("showModel") == null ? "" : String.valueOf(mapParams.get("showModel"));
			// 发布状态
			String publishStatus = mapParams.get("publishStatus") == null ? ""
					: String.valueOf(mapParams.get("publishStatus"));
			// 是否点击发布或撤销发布按钮
			String publishClick = mapParams.get("publishClick") == null ? ""
					: String.valueOf(mapParams.get("publishClick"));
			// 是否点击了置顶
			String upArtClick = mapParams.get("upArtClick") == null ? "" : String.valueOf(mapParams.get("upArtClick"));

			// 活动基本信息表
			PsTzArtHdTbl psTzArtHdTbl = new PsTzArtHdTbl();
			psTzArtHdTbl.setTzNactName(activityName);
			psTzArtHdTbl.setTzStartDt(activityStartDate);
			psTzArtHdTbl.setTzStartTm(activityStartTime);
			psTzArtHdTbl.setTzEndDt(activityEndDate);
			psTzArtHdTbl.setTzEndTm(activityEndTime);
			psTzArtHdTbl.setTzNactAddr(activityPlace);
			psTzArtHdTbl.setTzHdCs(activityCity);
			psTzArtHdTbl.setTzQyZxbm(enabledApply);
			psTzArtHdTbl.setTzAppfDt(applyStartDate);
			psTzArtHdTbl.setTzAppfTm(applyStartTime);
			psTzArtHdTbl.setTzAppeDt(applyEndDate);
			psTzArtHdTbl.setTzAppeTm(applyEndTime);
			psTzArtHdTbl.setTzXws(applyNum);
			psTzArtHdTbl.setTzXsms(showModel);

			// 文章内容表
			PsTzArtRecTblWithBLOBs psTzArtRecTblWithBLOBs = new PsTzArtRecTblWithBLOBs();
			psTzArtRecTblWithBLOBs.setTzArtTitle(activityName);
			psTzArtRecTblWithBLOBs.setTzArtTitleStyle(activityName);
			psTzArtRecTblWithBLOBs.setTzArtConent(contentInfo);
			psTzArtRecTblWithBLOBs.setTzArtName(activityName);
			psTzArtRecTblWithBLOBs.setTzArtType1(artType);
			psTzArtRecTblWithBLOBs.setTzOutArtUrl(externalLink);
			psTzArtRecTblWithBLOBs.setTzImageTitle(titleImageTitle);
			psTzArtRecTblWithBLOBs.setTzImageDesc(titleImageDesc);
			psTzArtRecTblWithBLOBs.setTzAttachsysfilena(sysFileName);
			psTzArtRecTblWithBLOBs.setTzStartDate(applyStartDate);
			psTzArtRecTblWithBLOBs.setTzStartTime(applyStartTime);
			psTzArtRecTblWithBLOBs.setTzEndDate(applyEndDate);
			psTzArtRecTblWithBLOBs.setTzEndTime(applyEndTime);
			psTzArtRecTblWithBLOBs.setRowLastmantDttm(dateNow);
			psTzArtRecTblWithBLOBs.setRowLastmantOprid(oprid);

			// 文章内容关联表
			PsTzLmNrGlTWithBLOBs psTzLmNrGlTWithBLOBs = new PsTzLmNrGlTWithBLOBs();
			psTzLmNrGlTWithBLOBs.setTzSiteId(siteId);
			psTzLmNrGlTWithBLOBs.setTzColuId(coluId);

			if ("Y".equals(publishClick)) {
				psTzLmNrGlTWithBLOBs.setTzArtPubState(publishStatus);
				if ("Y".equals(publishStatus)) {
					psTzLmNrGlTWithBLOBs.setTzArtNewsDt(dateNow);
				}
			}

			if ("Y".equals(upArtClick)) {
				sql = "select max(TZ_MAX_ZD_SEQ) from PS_TZ_LM_NR_GL_T where TZ_SITE_ID=? and TZ_COLU_ID=? and TZ_ART_ID<>?";
				int maxSEQ = sqlQuery.queryForObject(sql, new Object[] { siteId, coluId, activityId }, "int");
				psTzLmNrGlTWithBLOBs.setTzMaxZdSeq(maxSEQ + 1);
			}

			// 判断记录是否存在
			sql = "select 'Y' from PS_TZ_ART_HD_TBL where TZ_ART_ID=?";
			String recExists1 = sqlQuery.queryForObject(sql, new Object[] { activityId }, "String");

			sql = "select 'Y' from PS_TZ_ART_REC_TBL where TZ_ART_ID=?";
			String recExists2 = sqlQuery.queryForObject(sql, new Object[] { activityId }, "String");

			// 判断是新增还是更新
			if (null == activityId || "".equals(activityId) || "NEXT".equals(activityId.toUpperCase())
					|| (!"Y".equals(recExists1) && !"Y".equals(recExists2))) {

				activityId = String.valueOf(getSeqNum.getSeqNum("TZ_ART_REC_TBL", "TZ_ART_ID"));

				psTzArtHdTbl.setTzArtId(activityId);
				int rst = psTzArtHdTblMapper.insert(psTzArtHdTbl);
				if (rst > 0) {
					boolRst = true;
				}

				psTzArtRecTblWithBLOBs.setTzArtId(activityId);
				psTzArtRecTblWithBLOBs.setRowAddedDttm(dateNow);
				psTzArtRecTblWithBLOBs.setRowAddedOprid(oprid);
				psTzArtRecTblMapper.insert(psTzArtRecTblWithBLOBs);

				psTzLmNrGlTWithBLOBs.setTzArtId(activityId);
				psTzLmNrGlTWithBLOBs.setTzLastmantDttm(dateNow);
				psTzLmNrGlTWithBLOBs.setTzLastmantOprid(oprid);
				psTzLmNrGlTMapper.insertSelective(psTzLmNrGlTWithBLOBs);

			} else if ("Y".equals(recExists1) && "Y".equals(recExists2)) {

				psTzArtHdTbl.setTzArtId(activityId);
				int rst = psTzArtHdTblMapper.updateByPrimaryKey(psTzArtHdTbl);
				if (rst > 0) {
					boolRst = true;
				}

				psTzArtRecTblWithBLOBs.setTzArtId(activityId);
				psTzArtRecTblMapper.updateByPrimaryKeySelective(psTzArtRecTblWithBLOBs);

				psTzLmNrGlTWithBLOBs.setTzArtId(activityId);
				psTzLmNrGlTMapper.updateByPrimaryKeySelective(psTzLmNrGlTWithBLOBs);

			} else {
				return strRet;
			}

			if (boolRst) {
				// 同步报名人数,先锁表操作，避免同时更新引起报名人数和等候人数数据不对
				mySqlLockService.lockRow(sqlQuery, "TZ_NAUDLIST_T");

				sql = "SELECT COUNT(1) FROM PS_TZ_NAUDLIST_T WHERE TZ_ART_ID=? AND TZ_NREG_STAT='1'";
				int appliedNum = sqlQuery.queryForObject(sql, new Object[] { activityId }, "int");

				sql = "SELECT COUNT(1) FROM PS_TZ_NAUDLIST_T WHERE TZ_ART_ID=? AND TZ_NREG_STAT='4'";
				int waitingNum = sqlQuery.queryForObject(sql, new Object[] { activityId }, "int");

				if (applyNum < appliedNum) {
					// 席位数小于已报名数
					sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzUpdateHDBmrToWaiting");
					sqlQuery.update(sql, new Object[] { activityId, activityId, appliedNum - applyNum });
				} else if (applyNum > appliedNum && waitingNum > 0) {
					// 席位数大于已报名人数，且存在排队人数，则自动进补
					sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzUpdateHDBmrToApplied");
					sqlQuery.update(sql, new Object[] { activityId, activityId, applyNum - appliedNum });
				}

				// 解锁
				mySqlLockService.unlockRow(sqlQuery);

				strRet = activityId;

			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "保存活动信息失败。" + e.getMessage();
		}

		return strRet;

	}

	/**
	 * 保存活动信息项信息
	 * 
	 * @param mapParams
	 * @param errorMsg
	 * @return
	 */
	private String saveActivityApplyInfo(Map<String, Object> mapParams, String[] errorMsg) {
		String strRet = "";
		try {

			String activityId = mapParams.get("activityId") == null ? "" : String.valueOf(mapParams.get("activityId"));

			// 信息项ID
			String applyItemId = mapParams.get("applyItemId") == null ? ""
					: String.valueOf(mapParams.get("applyItemId"));
			// 信息项排序序号
			int applyItemNum = mapParams.get("applyItemNum") == null ? 0
					: Integer.parseInt(String.valueOf(mapParams.get("applyItemNum")));
			// 信息项名称
			String applyItemName = mapParams.get("applyItemName") == null ? ""
					: String.valueOf(mapParams.get("applyItemName"));
			// 是否必填
			String applyItemRequired = mapParams.get("applyItemRequired") == null ? ""
					: String.valueOf(mapParams.get("applyItemRequired"));
			// 控件类型
			String applyItemType = mapParams.get("applyItemType") == null ? ""
					: String.valueOf(mapParams.get("applyItemType"));
			// 是否有双语
			String applyItemNameEng = mapParams.get("applyItemNameEng") == null ? ""
					: String.valueOf(mapParams.get("applyItemNameEng"));

			PsTzZxbmXxxT psTzZxbmXxxT = new PsTzZxbmXxxT();
			psTzZxbmXxxT.setTzArtId(activityId);
			psTzZxbmXxxT.setTzZxbmXxxId(applyItemId);
			psTzZxbmXxxT.setTzPxXh(applyItemNum);
			psTzZxbmXxxT.setTzZxbmXxxName(applyItemName);
			psTzZxbmXxxT.setTzZxbmXxxBt(applyItemRequired);
			psTzZxbmXxxT.setTzZxbmXxxZsxs(applyItemType);

			String sql = "select 'Y' from PS_TZ_ZXBM_XXX_T where TZ_ART_ID=? and TZ_ZXBM_XXX_ID=?";
			String recExists = sqlQuery.queryForObject(sql, new Object[] { activityId, applyItemId }, "String");
			if ("Y".equals(recExists)) {
				psTzZxbmXxxTMapper.updateByPrimaryKey(psTzZxbmXxxT);
			} else {
				psTzZxbmXxxTMapper.insert(psTzZxbmXxxT);
			}

			if (!"".equals(applyItemNameEng)) {
				PsTzZxbmXxxET psTzZxbmXxxET = new PsTzZxbmXxxET();
				psTzZxbmXxxET.setTzArtId(activityId);
				psTzZxbmXxxET.setTzZxbmXxxId(applyItemId);
				psTzZxbmXxxET.setLanguageCd("ENG");
				psTzZxbmXxxET.setTzZxbmXxxName(applyItemNameEng);

				sql = "select 'Y' from PS_TZ_ZXBM_XXX_E_T where TZ_ART_ID=? and TZ_ZXBM_XXX_ID=? and LANGUAGE_CD=?";
				recExists = sqlQuery.queryForObject(sql, new Object[] { activityId, applyItemId, "ENG" }, "String");
				if ("Y".equals(recExists)) {
					psTzZxbmXxxETMapper.updateByPrimaryKey(psTzZxbmXxxET);
				} else {
					psTzZxbmXxxETMapper.insert(psTzZxbmXxxET);
				}

			}

			strRet = activityId;

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "保存信息项失败。" + e.getMessage();
		}

		return strRet;
	}

	/**
	 * 保存活动附件集信息
	 * 
	 * @param mapParams
	 * @param errorMsg
	 * @return
	 */
	private String saveArtAttachment(Map<String, Object> mapParams, String[] errorMsg) {
		String strRet = "";
		try {

			String activityId = mapParams.get("activityId") == null ? "" : String.valueOf(mapParams.get("activityId"));

			// 附件系统文件名
			String attachmentID = mapParams.get("attachmentID") == null ? ""
					: String.valueOf(mapParams.get("attachmentID"));

			PsTzArtFileTKey psTzArtFileTKey = new PsTzArtFileTKey();
			psTzArtFileTKey.setTzArtId(activityId);
			psTzArtFileTKey.setTzAttachsysfilena(attachmentID);

			String sql = "select 'Y' from PS_TZ_ART_FILE_T where TZ_ART_ID=? and TZ_ATTACHSYSFILENA=?";
			String recExists = sqlQuery.queryForObject(sql, new Object[] { activityId, attachmentID }, "String");
			if (!"Y".equals(recExists)) {
				psTzArtFileTMapper.insert(psTzArtFileTKey);
			}

			strRet = activityId;

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "保存活动附件集失败。" + e.getMessage();
		}

		return strRet;
	}

	/**
	 * 保存图片集
	 * 
	 * @param jacksonUtil
	 * @param errorMsg
	 * @return
	 */
	private String saveArtTpj(JacksonUtil jacksonUtil, String[] errorMsg) {
		String strRet = "";
		try {

			boolean doLoop = true;
			ArrayList<String> sysFileNameArr = new ArrayList<String>();
			int num = 0;

			String activityId = jacksonUtil.getString("activityId");

			while (doLoop) {

				num++;

				String keyName = "data" + String.valueOf(num);
				Map<String, Object> mapParams = jacksonUtil.getMap(keyName);

				if (null == mapParams) {
					break;
				}

				// 系统文件名
				String sysFileName = mapParams.get("sysFileName") == null ? ""
						: String.valueOf(mapParams.get("sysFileName"));
				// 序号
				int index = mapParams.get("index") == null ? 0
						: Integer.parseInt(String.valueOf(mapParams.get("index")));
				// 路径
				// String src = mapParams.get("src") == null ? "" :
				// String.valueOf(mapParams.get("src"));
				// 描述
				String caption = mapParams.get("caption") == null ? "" : String.valueOf(mapParams.get("caption"));
				// 跳转URL
				String picURL = mapParams.get("picURL") == null ? "" : String.valueOf(mapParams.get("picURL"));

				PsTzArtPicT psTzArtPicT = new PsTzArtPicT();
				psTzArtPicT.setTzArtId(activityId);
				psTzArtPicT.setTzAttachsysfilena(sysFileName);
				psTzArtPicT.setTzPriority(index);
				psTzArtPicT.setTzImgDescr(caption);
				psTzArtPicT.setTzImgTrsUrl(picURL);

				String sql = "select 'Y' from PS_TZ_ART_PIC_T where TZ_ART_ID=? and TZ_ATTACHSYSFILENA=?";
				String recExists = sqlQuery.queryForObject(sql, new Object[] { activityId, sysFileName }, "String");

				int rst = 0;
				if ("Y".equals(recExists)) {
					rst = psTzArtPicTMapper.updateByPrimaryKey(psTzArtPicT);
				} else {
					rst = psTzArtPicTMapper.insert(psTzArtPicT);
				}

				if (rst > 0) {
					sysFileNameArr.add(sysFileName);
				}

			}

			if (jacksonUtil.containsKey("data0")) {
				String data0 = jacksonUtil.getString("data0");
				if ("deleteAll".equals(data0)) {
					sysFileNameArr.clear();
				}
			}

			if (null != sysFileNameArr && sysFileNameArr.size() > 0) {
				String sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzGetEventTPJs");
				List<Map<String, Object>> listData = sqlQuery.queryForList(sql, new Object[] { activityId });

				for (Map<String, Object> mapData : listData) {
					String sysFname = mapData.get("TZ_ATTACHSYSFILENA") == null ? ""
							: String.valueOf(mapData.get("TZ_ATTACHSYSFILENA"));
					if (!sysFileNameArr.contains(sysFname)) {
						// 若数据库中有，而提交的数据中没有，则删除表中记录，并删除物理文件
						PsTzArtPicTKey psTzArtPicTKey = new PsTzArtPicTKey();
						psTzArtPicTKey.setTzArtId(activityId);
						psTzArtPicTKey.setTzAttachsysfilena(sysFname);
						psTzArtPicTMapper.deleteByPrimaryKey(psTzArtPicTKey);

						PsTzArtTpjT psTzArtTpjT = psTzArtTpjTMapper.selectByPrimaryKey(sysFname);

						if (null == psTzArtTpjT) {
							continue;
						}

						String delFilePath = "";

						String tzAttPUrl = psTzArtTpjT.getTzAttPUrl();
						String tzYsAttachsysnam = psTzArtTpjT.getTzYsAttachsysnam();
						String tzSlAttachsysnam = psTzArtTpjT.getTzSlAttachsysnam();

						if (null != tzAttPUrl && !"".equals(tzAttPUrl)) {
							if (!tzAttPUrl.endsWith("/")) {
								tzAttPUrl = tzAttPUrl + "/";
							}

							delFilePath = tzAttPUrl + sysFname;
							fileManageServiceImpl.DeleteFile(delFilePath);

							if (null != tzYsAttachsysnam && !"".equals(tzYsAttachsysnam)) {
								delFilePath = tzAttPUrl + tzYsAttachsysnam;
								fileManageServiceImpl.DeleteFile(delFilePath);
							}

							if (null != tzSlAttachsysnam && !"".equals(tzSlAttachsysnam)) {
								delFilePath = tzAttPUrl + tzSlAttachsysnam;
								fileManageServiceImpl.DeleteFile(delFilePath);
							}

						}

					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "保存图片集失败。" + e.getMessage();
		}

		return strRet;
	}

	/**
	 * 删除活动信息项信息
	 * 
	 * @param activityId
	 * @param mapParams
	 * @param errorMsg
	 * @return
	 */
	private String deleteActivityApplyInfo(String activityId, Map<String, Object> mapParams, String[] errorMsg) {
		String strRet = "";
		try {

			String applyItemId = mapParams.get("applyItemId") == null ? ""
					: String.valueOf(mapParams.get("applyItemId"));

			PsTzZxbmXxxTKey psTzZxbmXxxTKey = new PsTzZxbmXxxTKey();
			psTzZxbmXxxTKey.setTzArtId(activityId);
			psTzZxbmXxxTKey.setTzZxbmXxxId(applyItemId);

			psTzZxbmXxxTMapper.deleteByPrimaryKey(psTzZxbmXxxTKey);

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "活动信息项失败。" + e.getMessage();
		}

		return strRet;
	}

	/**
	 * 删除活动附件集信息
	 * 
	 * @param activityId
	 * @param mapParams
	 * @param errorMsg
	 * @return
	 */
	private String deleteArtAttachment(String activityId, Map<String, Object> mapParams, String[] errorMsg) {
		String strRet = "";
		try {

			String attachmentID = mapParams.get("attachmentID") == null ? ""
					: String.valueOf(mapParams.get("attachmentID"));

			if (null != activityId && !"".equals(activityId)) {
				PsTzArtFileTKey psTzArtFileTKey = new PsTzArtFileTKey();
				psTzArtFileTKey.setTzArtId(activityId);
				psTzArtFileTKey.setTzAttachsysfilena(attachmentID);
				psTzArtFileTMapper.deleteByPrimaryKey(psTzArtFileTKey);
			}

			PsTzArtFjjT psTzArtFjjT = psTzArtFjjTMapper.selectByPrimaryKey(attachmentID);
			if (null != psTzArtFjjT) {
				String tzAttPUrl = psTzArtFjjT.getTzAttPUrl();
				if (null != tzAttPUrl && !"".equals(tzAttPUrl)) {
					if (!tzAttPUrl.endsWith("/")) {
						tzAttPUrl = tzAttPUrl + "/";
					}
					String delFilePath = tzAttPUrl + attachmentID;
					fileManageServiceImpl.DeleteFile(delFilePath);
				}

				psTzArtFjjTMapper.deleteByPrimaryKey(attachmentID);
			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "删除附件集失败。" + e.getMessage();
		}

		return strRet;
	}

	/**
	 * 压缩图片为指定宽度（小图）
	 * 
	 * @param fileDir
	 * @param attachSysName
	 * @param width
	 * @return
	 */
	private String resize(String fileDir, String attachSysName, int width) {

		String strRet = resizeImageUtil.resize(fileDir + attachSysName, fileDir, "mini_" + attachSysName, width);

		return strRet;
	}

	/**
	 * 压缩图片为指定宽度（大图）
	 * 
	 * @param fileDir
	 * @param attachSysName
	 * @param width
	 * @return
	 */
	private String resizeANDbyte(String fileDir, String attachSysName, int width) {

		String strRet = resizeImageUtil.resize(fileDir + attachSysName, fileDir, "new_" + attachSysName, width);

		return strRet;
	}

}
