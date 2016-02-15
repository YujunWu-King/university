/**
 * 
 */
package com.tranzvision.gd.TZEventsBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.dao.PsTzLmNrGlTMapper;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.model.PsTzLmNrGlT;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.model.PsTzLmNrGlTWithBLOBs;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
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
				String todoGenHtml;
				String[] contentHtml = new String[] {};

				// 更新文章内容关联表
				PsTzLmNrGlTWithBLOBs psTzLmNrGlTWithBLOBs = new PsTzLmNrGlTWithBLOBs();
				psTzLmNrGlTWithBLOBs.setTzSiteId(siteId);
				psTzLmNrGlTWithBLOBs.setTzColuId(coluId);
				psTzLmNrGlTWithBLOBs.setTzArtId(activityId);
				psTzLmNrGlTWithBLOBs.setTzArtHtml(contentHtml[2]);
				psTzLmNrGlTWithBLOBs.setTzLastmantDttm(dateNow);
				psTzLmNrGlTWithBLOBs.setTzLastmantOprid(oprid);

				if ("Y".equals(publishStatus)) {
					psTzLmNrGlTWithBLOBs.setTzArtConentScr(contentHtml[2]);
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
					activityId = this.saveArtTpj(mapParams, errorMsg);
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
				String todoGenHtml;
				String[] contentHtml = new String[] {};
				String[] contentPhoneHtml = new String[] {};

				// 更新文章内容关联表
				PsTzLmNrGlTWithBLOBs psTzLmNrGlTWithBLOBs = new PsTzLmNrGlTWithBLOBs();
				psTzLmNrGlTWithBLOBs.setTzSiteId(siteId);
				psTzLmNrGlTWithBLOBs.setTzColuId(coluId);
				psTzLmNrGlTWithBLOBs.setTzArtId(activityId);
				psTzLmNrGlTWithBLOBs.setTzArtHtml(contentHtml[2]);
				psTzLmNrGlTWithBLOBs.setTzArtSjHtml(contentPhoneHtml[2]);
				psTzLmNrGlTWithBLOBs.setTzLastmantDttm(dateNow);
				psTzLmNrGlTWithBLOBs.setTzLastmantOprid(oprid);

				if ("Y".equals(publishStatus)) {
					psTzLmNrGlTWithBLOBs.setTzArtConentScr(contentHtml[2]);
					psTzLmNrGlTWithBLOBs.setTzArtSjContScr(contentPhoneHtml[2]);
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
			errorMsg[1] = "";
		}

		return strRet;
	}
	
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {

		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");

		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();

		try {
			
			
			

			mapRet.replace("total", 0);
			mapRet.replace("root", listData);

			
		} catch (Exception e) {
			e.printStackTrace();
		}

		JacksonUtil jacksonUtil = new JacksonUtil();
		return jacksonUtil.Map2json(mapRet);

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
		Map<String, Object> mapRet = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			Date dateNow = new Date();
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "";
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
		Map<String, Object> mapRet = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			Date dateNow = new Date();
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "";
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
		Map<String, Object> mapRet = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			Date dateNow = new Date();
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "";
		}

		return strRet;
	}

	/**
	 * 保存图片集
	 * 
	 * @param mapParams
	 * @param errorMsg
	 * @return
	 */
	private String saveArtTpj(Map<String, Object> mapParams, String[] errorMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			Date dateNow = new Date();
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "";
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
		Map<String, Object> mapRet = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			Date dateNow = new Date();
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "";
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
		Map<String, Object> mapRet = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			Date dateNow = new Date();
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "";
		}

		return strRet;
	}

}
