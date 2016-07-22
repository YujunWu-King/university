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
import com.tranzvision.gd.TZWebSiteInfoMgBundle.dao.PsTzArtPrjTMapper;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.dao.PsTzArtRecTblMapper;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.dao.PsTzArtTitimgTMapper;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.dao.PsTzArtTpjTMapper;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.dao.PsTzLmNrGlTMapper;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.model.PsTzArtFileTKey;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.model.PsTzArtFjjT;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.model.PsTzArtPicT;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.model.PsTzArtPicTKey;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.model.PsTzArtPrjTKey;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.model.PsTzArtRecTblWithBLOBs;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.model.PsTzArtTitimgT;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.model.PsTzArtTpjT;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.model.PsTzLmNrGlTWithBLOBs;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.ResizeImageUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.session.TzGetSetSessionValue;
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

	@Autowired
	private TzGetSetSessionValue tzGetSetSessionValue;
	
	@Autowired
	private PsTzArtPrjTMapper psTzArtPrjTMapper;

	private String sessSiteId = "siteId";

	private String sessColuId = "coluId";

	private String sessActivityId = "activityId";

	private String sessPublishClick = "publishClick";

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
			String ctxServerName = request.getScheme() + "://" + request.getServerName() + ":"
					+ request.getServerPort();

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
							"", "", "", "", "", "", "", "", "", "", "", "");
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
							"", "", "", "", "", "", "", "", "", "", "", "");
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
				saveImageAccessUrl = saveImageAccessUrl.equals("null") ? "" : saveImageAccessUrl;
				saveAttachAccessUrl = mapData.get("TZ_ATTS_VIEW") == null ? ""
						: String.valueOf(mapData.get("TZ_ATTS_VIEW"));
				saveAttachAccessUrl = saveAttachAccessUrl.equals("null") ? "" : saveAttachAccessUrl;
			}
			if (null == activityId || "".equals(activityId)) {

				strRet = this.genEventJsonString("", "", "", "", "", "", "", "", "", "", "", "", "", enabledApply, "",
						"", "", "", "0", "", "", "", siteId, coluId, saveImageAccessUrl, saveAttachAccessUrl, "", "", "");
				return strRet;
			}

			// 获取活动信息
			sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzGetEventInfo");
			Map<String, Object> mapEventInfo = sqlQuery.queryForMap(sql, new Object[] { activityId, siteId, coluId });

			if (null == mapEventInfo) {
				errorMsg[0] = "1";
				errorMsg[1] = "活动数据不存在。";
				strRet = this.genEventJsonString("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
						"", "", "", "", "", "", "", "", "", "", "");
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
				publishUrl = ctxServerName + ctxPath + "/dispatcher?classid=" + classId + "&operatetype=HTML&siteId="
						+ siteId + "&columnId=" + coluId + "&artId=" + activityId + "&oprate=R";
			}

			String externalLink = mapEventInfo.get("TZ_OUT_ART_URL") == null ? ""
					: String.valueOf(mapEventInfo.get("TZ_OUT_ART_URL"));
			String applyNum = mapEventInfo.get("TZ_XWS") == null ? "" : String.valueOf(mapEventInfo.get("TZ_XWS"));
			String showModel = mapEventInfo.get("TZ_XSMS") == null ? "" : String.valueOf(mapEventInfo.get("TZ_XSMS"));
			String artType = mapEventInfo.get("TZ_ART_TYPE1") == null ? ""
					: String.valueOf(mapEventInfo.get("TZ_ART_TYPE1"));

			String dtFormat = getSysHardCodeVal.getDateFormat();
			String tmFormat = getSysHardCodeVal.getTimeHMFormat();

			SimpleDateFormat dateSimpleDateFormat = new SimpleDateFormat(dtFormat);
			SimpleDateFormat timeSimpleDateFormat = new SimpleDateFormat(tmFormat);

			Date activityStartDate = mapEventInfo.get("TZ_START_DT") == null ? null
					: dateSimpleDateFormat.parse(String.valueOf(mapEventInfo.get("TZ_START_DT")));
			Date activityStartTime = mapEventInfo.get("TZ_START_TM") == null ? null
					: timeSimpleDateFormat.parse(String.valueOf(mapEventInfo.get("TZ_START_TM")));
			Date activityEndDate = mapEventInfo.get("TZ_END_DT") == null ? null
					: dateSimpleDateFormat.parse(String.valueOf(mapEventInfo.get("TZ_END_DT")));
			Date activityEndTime = mapEventInfo.get("TZ_END_TM") == null ? null
					: timeSimpleDateFormat.parse(String.valueOf(mapEventInfo.get("TZ_END_TM")));
			Date applyStartDate = mapEventInfo.get("TZ_APPF_DT") == null ? null
					: dateSimpleDateFormat.parse(String.valueOf(mapEventInfo.get("TZ_APPF_DT")));
			Date applyStartTime = mapEventInfo.get("TZ_APPF_TM") == null ? null
					: timeSimpleDateFormat.parse(String.valueOf(mapEventInfo.get("TZ_APPF_TM")));
			Date applyEndDate = mapEventInfo.get("TZ_APPE_DT") == null ? null
					: dateSimpleDateFormat.parse(String.valueOf(mapEventInfo.get("TZ_APPE_DT")));
			Date applyEndTime = mapEventInfo.get("TZ_APPE_TM") == null ? null
					: timeSimpleDateFormat.parse(String.valueOf(mapEventInfo.get("TZ_APPE_TM")));

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
			
			String limit = (String)mapEventInfo.get("TZ_PROJECT_LIMIT");
			if(limit == null){
				limit = "";
			}
			
			ArrayList<String> projects = new ArrayList<>();
			List<Map<String, Object>> prjList = sqlQuery.queryForList("select TZ_PRJ_ID from PS_TZ_ART_PRJ_T where TZ_ART_ID=?",new Object[]{activityId});
			if(prjList != null){
				for(int h = 0; h < prjList.size(); h++){
					String projectId = (String)prjList.get(h).get("TZ_PRJ_ID");
					projects.add(projectId);
				}
			}
			
			if(projects != null && projects.size()>0){
				strRet = this.genEventJsonString(activityId, activityName, strActivityStartDate, strActivityStartTime,
						strActivityEndDate, strActivityEndTime, activityPlace, activityCity, externalLink, contentInfo,
						titleImageTitle, titleImageDesc, titleImageUrl, enabledApply, strApplyStartDate, strApplyStartTime,
						strApplyEndDate, strApplyEndTime, applyNum, showModel, publishStatus, publishUrl, siteId, coluId,
						saveImageAccessUrl, saveAttachAccessUrl, artType,limit,projects);
			}else{
				strRet = this.genEventJsonString(activityId, activityName, strActivityStartDate, strActivityStartTime,
						strActivityEndDate, strActivityEndTime, activityPlace, activityCity, externalLink, contentInfo,
						titleImageTitle, titleImageDesc, titleImageUrl, enabledApply, strApplyStartDate, strApplyStartTime,
						strApplyEndDate, strApplyEndTime, applyNum, showModel, publishStatus, publishUrl, siteId, coluId,
						saveImageAccessUrl, saveAttachAccessUrl, artType,limit,"");
			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "查询失败！" + e.getMessage();
			strRet = this.genEventJsonString("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
					"", "", "", "", "", "", "", "", "", "");
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
			String artType,String limit, Object projects) {
		String strRet = "";

		if ("".equals(strApplyStartTime)) {
			strApplyStartTime = "08:30";
		}

		if ("".equals(strApplyEndTime)) {
			strApplyEndTime = "17:30";
		}

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
		mapJson.put("limit", limit);
		mapJson.put("projects", projects);

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
			String ctxServerName = request.getScheme() + "://" + request.getServerName() + ":"
					+ request.getServerPort();

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
				String publishUrl = ctxServerName + ctxPath + "/dispatcher?classid=" + classId
						+ "&operatetype=HTML&siteId=" + siteId + "&columnId=" + coluId + "&artId=" + activityId
						+ "&oprate=R";

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
			String ctxServerName = request.getScheme() + "://" + request.getServerName() + ":"
					+ request.getServerPort();

			Date dateNow = new Date();
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			String activityId = "";

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {

				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				String strFlag = jacksonUtil.getString("typeFlag");
				Map<String, Object> mapParams = jacksonUtil.getMap("data");

				activityId = "";
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

			}

			// 从session中读取参数
			String strEventInfoSession = tzGetSetSessionValue.getTzAddingNewActivity();
			if ("".equals(strEventInfoSession) || null == strEventInfoSession) {
				errorMsg[0] = "1";
				errorMsg[1] = "机构站点参数错误！";
				return strRet;
			}

			Map<String, Object> mapEventInfoSession = jacksonUtil.parseJson2Map(strEventInfoSession);

			String siteId = mapEventInfoSession.get(sessSiteId) == null ? ""
					: String.valueOf(mapEventInfoSession.get(sessSiteId));
			String coluId = mapEventInfoSession.get(sessColuId) == null ? ""
					: String.valueOf(mapEventInfoSession.get(sessColuId));
			String publishStatus = mapEventInfoSession.get(sessPublishClick) == null ? ""
					: String.valueOf(mapEventInfoSession.get(sessPublishClick));

			String sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzGetEventViewClassId");
			String classId = sqlQuery.queryForObject(sql, "String");
			String publishUrl = ctxServerName + ctxPath + "/dispatcher?classid=" + classId + "&operatetype=HTML&siteId="
					+ siteId + "&columnId=" + coluId + "&artId=" + activityId + "&oprate=R";

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
			// String[] contentPhoneHtml = new String[] {};

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

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "保存失败！" + e.getMessage();
		}

		// 清空session中的活动编号等参数，避免出现更新错误的情况。
		tzGetSetSessionValue.setTzAddingNewActivity("");

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
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");

		// ArrayList<Map<String, Object>> listData = new ArrayList<Map<String,
		// Object>>();

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);

		// 活动编号
		String activityId = jacksonUtil.getString("activityId");
		// 信息项id
		String applyItemId = jacksonUtil.getString("applyItemId");

		try {

			// 如果传了信息项id，则取信息项的值
			if (null != applyItemId && !"".equals(applyItemId)) {
				strRet = tzEventsItemOptionsServiceImpl.tzQueryList(strParams, numLimit, numStart, errorMsg);
				return strRet;
			}

			// 没传信息项id，则取活动基本信息列表
			String gridTyp = jacksonUtil.getString("gridTyp");
			if (null != gridTyp && !"".equals(gridTyp)) {
				gridTyp = gridTyp.toUpperCase();
			}

			String sql = "";
			ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();
			int total = 0;

			switch (gridTyp) {
			case "FJ":
				// 获取附件信息
				sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzGetEventAttachsList");
				List<Map<String, Object>> listFj = sqlQuery.queryForList(sql, new Object[] { activityId });
				total = 0;

				for (Map<String, Object> mapFj : listFj) {

					String tzATTACHSYSFILENA = mapFj.get("TZ_ATTACHSYSFILENA") == null ? ""
							: String.valueOf(mapFj.get("TZ_ATTACHSYSFILENA"));
					String tzATTACHFILENAME = mapFj.get("TZ_ATTACHFILE_NAME") == null ? ""
							: String.valueOf(mapFj.get("TZ_ATTACHFILE_NAME"));
					String tzAttAUrl = mapFj.get("TZ_ATT_A_URL") == null ? ""
							: String.valueOf(mapFj.get("TZ_ATT_A_URL"));

					if (!tzAttAUrl.endsWith("/")) {
						tzAttAUrl = tzAttAUrl + "/";
					}

					tzATTACHFILENAME = tzATTACHFILENAME + "|" + tzAttAUrl + tzATTACHSYSFILENA;

					String attachmentUrl = tzAttAUrl + tzATTACHSYSFILENA;

					Map<String, Object> mapJson = new HashMap<String, Object>();
					mapJson.put("attachmentID", tzATTACHSYSFILENA);
					mapJson.put("attachmentName", tzATTACHFILENAME);
					mapJson.put("attachmentUrl", attachmentUrl);

					listJson.add(mapJson);
				}

				sql = "select count(1) from PS_TZ_ART_FILE_T a, PS_TZ_ART_FJJ_T b where a.TZ_ART_ID = ? and a.TZ_ATTACHSYSFILENA=b.TZ_ATTACHSYSFILENA";
				total = sqlQuery.queryForObject(sql, new Object[] { activityId }, "int");

				mapRet.replace("total", total);
				mapRet.replace("root", listJson);

				strRet = jacksonUtil.Map2json(mapRet);

				break;

			case "BMX":
				// 获取报名信息项
				sql = "select count(1) from PS_TZ_ZXBM_XXX_T where TZ_ART_ID=?";
				total = sqlQuery.queryForObject(sql, new Object[] { activityId }, "int");

				sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzGetEventBmXxxList");
				List<Map<String, Object>> listBmx = sqlQuery.queryForList(sql, new Object[] { activityId });
				for (Map<String, Object> mapBmx : listBmx) {

					String tzArtId = mapBmx.get("TZ_ART_ID") == null ? "" : String.valueOf(mapBmx.get("TZ_ART_ID"));

					String tzZxbmXxxId = mapBmx.get("TZ_ZXBM_XXX_ID") == null ? ""
							: String.valueOf(mapBmx.get("TZ_ZXBM_XXX_ID"));

					int applyItemNum = mapBmx.get("TZ_PX_XH") == null ? 0
							: Integer.parseInt(String.valueOf(mapBmx.get("TZ_PX_XH")));

					String applyItemName = mapBmx.get("TZ_ZXBM_XXX_NAME") == null ? ""
							: String.valueOf(mapBmx.get("TZ_ZXBM_XXX_NAME"));

					String applyItemRequired = mapBmx.get("TZ_ZXBM_XXX_BT") == null ? ""
							: String.valueOf(mapBmx.get("TZ_ZXBM_XXX_BT"));

					String applyItemType = mapBmx.get("TZ_ZXBM_XXX_ZSXS") == null ? ""
							: String.valueOf(mapBmx.get("TZ_ZXBM_XXX_ZSXS"));

					String applyItemNameEng = mapBmx.get("EN_NAME") == null ? ""
							: String.valueOf(mapBmx.get("EN_NAME"));

					Map<String, Object> mapJson = new HashMap<String, Object>();
					mapJson.put("activityId", tzArtId);
					mapJson.put("applyItemId", tzZxbmXxxId);
					mapJson.put("applyItemNum", applyItemNum);
					mapJson.put("applyItemName", applyItemName);
					mapJson.put("applyItemRequired", applyItemRequired);
					mapJson.put("applyItemType", applyItemType);
					mapJson.put("applyItemNameEng", applyItemNameEng);

					listJson.add(mapJson);
				}

				mapRet.replace("total", total);
				mapRet.replace("root", listJson);

				strRet = jacksonUtil.Map2json(mapRet);

				break;

			case "TPJ":
				// 获取图片集
				sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzGetEventTpjsList");
				List<Map<String, Object>> listTpj = sqlQuery.queryForList(sql, new Object[] { activityId });

				for (Map<String, Object> mapTpj : listTpj) {

					total++;

					String sysfileName = mapTpj.get("TZ_ATTACHSYSFILENA") == null ? ""
							: String.valueOf(mapTpj.get("TZ_ATTACHSYSFILENA"));

					String priorityNum = mapTpj.get("TZ_PRIORITY") == null ? "0"
							: String.valueOf(mapTpj.get("TZ_PRIORITY"));

					String aatAccUrl = mapTpj.get("TZ_ATT_A_URL") == null ? ""
							: String.valueOf(mapTpj.get("TZ_ATT_A_URL"));

					String desc = mapTpj.get("TZ_IMG_DESCR") == null ? "" : String.valueOf(mapTpj.get("TZ_IMG_DESCR"));

					String imgtrsUrl = mapTpj.get("TZ_IMG_TRS_URL") == null ? ""
							: String.valueOf(mapTpj.get("TZ_IMG_TRS_URL"));

					String sltPicName = mapTpj.get("TZ_SL_ATTACHSYSNAM") == null ? ""
							: String.valueOf(mapTpj.get("TZ_SL_ATTACHSYSNAM"));

					if (!aatAccUrl.endsWith("/")) {
						aatAccUrl = aatAccUrl + "/";
					}

					Map<String, Object> mapJson = new HashMap<String, Object>();
					mapJson.put("sysFileName", sysfileName);
					mapJson.put("index", priorityNum);
					mapJson.put("src", aatAccUrl + sysfileName);
					mapJson.put("caption", desc);
					mapJson.put("picURL", imgtrsUrl);
					mapJson.put("sltUrl", aatAccUrl + sltPicName);

					listJson.add(mapJson);

				}

				mapRet.replace("total", total);
				mapRet.replace("root", listJson);

				strRet = jacksonUtil.Map2json(mapRet);

				break;

			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "查询失败。" + e.getMessage();
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

				String fileDir = request.getSession().getServletContext().getResource(accessPath).getPath();
				path = fileDir;

				switch (attachmentType) {
				case "IMG":

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

			String dtFormat = getSysHardCodeVal.getDateTimeHMFormat();

			SimpleDateFormat dtSimpleDateFormat = new SimpleDateFormat(dtFormat);

			Date dateNow = new Date();
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

			String siteId = mapParams.get("siteId") == null ? "" : String.valueOf(mapParams.get("siteId"));
			String coluId = mapParams.get("coluId") == null ? "" : String.valueOf(mapParams.get("coluId"));
			// 活动编号
			String activityId = mapParams.get("activityId") == null ? "" : String.valueOf(mapParams.get("activityId"));

			// 活动名称
			String activityName = mapParams.get("activityName") == null ? ""
					: String.valueOf(mapParams.get("activityName"));

			// 活动开始日期时间
			String strActStartDate = mapParams.get("activityStartDate") == null ? ""
					: String.valueOf(mapParams.get("activityStartDate"));
			String strActStartTime = mapParams.get("activityStartTime") == null ? ""
					: String.valueOf(mapParams.get("activityStartTime"));
			Date activityStartDateTime = null;
			if (!"".equals(strActStartDate)) {
				if ("".equals(strActStartTime)) {
					activityStartDateTime = dtSimpleDateFormat.parse(strActStartDate);
				} else {
					activityStartDateTime = dtSimpleDateFormat.parse(strActStartDate + " " + strActStartTime);
				}
			}

			// 活动结束日期时间
			String strActEndDate = mapParams.get("activityEndDate") == null ? ""
					: String.valueOf(mapParams.get("activityEndDate"));
			String strActEndTime = mapParams.get("activityEndTime") == null ? ""
					: String.valueOf(mapParams.get("activityEndTime"));
			Date activityEndDateTime = null;
			if (!"".equals(strActEndDate)) {
				if ("".equals(strActEndTime)) {
					activityEndDateTime = dtSimpleDateFormat.parse(strActEndDate);
				} else {
					activityEndDateTime = dtSimpleDateFormat.parse(strActEndDate + " " + strActEndTime);
				}
			}

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
			// 查看范围;
			String limit = "";
			if(mapParams.containsKey("limit")){
				limit = (String) mapParams.get("limit");
			}
			// 限定的项目;
			ArrayList<String> projects = new ArrayList<>();
			if (mapParams.get("projects") != null && !"".equals(mapParams.get("projects"))) {
				projects = (ArrayList<String>) mapParams.get("projects");
			}
			
			// 是否启用在线报名
			String enabledApply = mapParams.get("enabledApply") == null ? "N"
					: String.valueOf(mapParams.get("enabledApply"));
			// 报名开始日期时间
			String strApplyStartDate = mapParams.get("applyStartDate") == null ? ""
					: String.valueOf(mapParams.get("applyStartDate"));
			String strApplyStartTime = mapParams.get("applyStartTime") == null ? ""
					: String.valueOf(mapParams.get("applyStartTime"));
			Date applyStartDateTime = null;
			if (!"".equals(strApplyStartDate)) {
				if ("".equals(strApplyStartTime)) {
					applyStartDateTime = dtSimpleDateFormat.parse(strApplyStartDate);
				} else {
					applyStartDateTime = dtSimpleDateFormat.parse(strApplyStartDate + " " + strApplyStartTime);
				}
			}

			// 报名结束日期时间
			String strApplyEndDate = mapParams.get("applyEndDate") == null ? ""
					: String.valueOf(mapParams.get("applyEndDate"));
			String strApplyEndTime = mapParams.get("applyEndTime") == null ? ""
					: String.valueOf(mapParams.get("applyEndTime"));
			Date applyEndDateTime = null;
			if (!"".equals(strApplyEndDate)) {
				if ("".equals(strApplyEndTime)) {
					applyEndDateTime = dtSimpleDateFormat.parse(strApplyEndDate);
				} else {
					applyEndDateTime = dtSimpleDateFormat.parse(strApplyEndDate + " " + strApplyEndTime);
				}
			}

			// 席位数
			int applyNum = 0;
			String strApplyNum = mapParams.get("applyNum") == null ? "" : String.valueOf(mapParams.get("applyNum"));
			if (!"".equals(strApplyNum)) {
				applyNum = Integer.parseInt(strApplyNum);
			}

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
			psTzArtHdTbl.setTzStartDt(activityStartDateTime);
			psTzArtHdTbl.setTzStartTm(activityStartDateTime);
			psTzArtHdTbl.setTzEndDt(activityEndDateTime);
			psTzArtHdTbl.setTzEndTm(activityEndDateTime);
			psTzArtHdTbl.setTzNactAddr(activityPlace);
			psTzArtHdTbl.setTzHdCs(activityCity);
			psTzArtHdTbl.setTzQyZxbm(enabledApply);
			psTzArtHdTbl.setTzAppfDt(applyStartDateTime);
			psTzArtHdTbl.setTzAppfTm(applyStartDateTime);
			psTzArtHdTbl.setTzAppeDt(applyEndDateTime);
			psTzArtHdTbl.setTzAppeTm(applyEndDateTime);
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
			psTzArtRecTblWithBLOBs.setTzProjectLimit(limit);
			psTzArtRecTblWithBLOBs.setTzImageTitle(titleImageTitle);
			psTzArtRecTblWithBLOBs.setTzImageDesc(titleImageDesc);
			psTzArtRecTblWithBLOBs.setTzAttachsysfilena(sysFileName);
			psTzArtRecTblWithBLOBs.setTzStartDate(applyStartDateTime);
			psTzArtRecTblWithBLOBs.setTzStartTime(applyStartDateTime);
			psTzArtRecTblWithBLOBs.setTzEndDate(applyEndDateTime);
			psTzArtRecTblWithBLOBs.setTzEndTime(applyEndDateTime);
			psTzArtRecTblWithBLOBs.setRowLastmantDttm(dateNow);
			psTzArtRecTblWithBLOBs.setRowLastmantOprid(oprid);

			// 文章内容关联表
			PsTzLmNrGlTWithBLOBs psTzLmNrGlTWithBLOBs = new PsTzLmNrGlTWithBLOBs();
			psTzLmNrGlTWithBLOBs.setTzSiteId(siteId);
			psTzLmNrGlTWithBLOBs.setTzColuId(coluId);
			psTzLmNrGlTWithBLOBs.setTzFbz("");

			if ("Y".equals(publishClick)) {
				psTzLmNrGlTWithBLOBs.setTzArtPubState(publishStatus);
				if ("Y".equals(publishStatus)) {
					psTzLmNrGlTWithBLOBs.setTzArtNewsDt(dateNow);
				}
			}

			if ("Y".equals(upArtClick)) {
				sql = "select max(TZ_MAX_ZD_SEQ) from PS_TZ_LM_NR_GL_T where TZ_SITE_ID=? and TZ_COLU_ID=? and TZ_ART_ID<>?";
				String strMaxSEQ = sqlQuery.queryForObject(sql, new Object[] { siteId, coluId, activityId }, "String");
				int maxSEQ = 0;
				if (null != strMaxSEQ) {
					maxSEQ = Integer.parseInt(strMaxSEQ);
				}
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

				sql = "select TZ_ATTACHSYSFILENA from PS_TZ_ART_REC_TBL where TZ_ART_ID=?";
				String titleImgFileName = sqlQuery.queryForObject(sql, new Object[] { activityId }, "String");

				if (null != titleImgFileName && !"".equals(titleImgFileName)) {
					// 如果存在标题图，并且与新的标题图文件名不一致，则删除旧的标题图片
					if (!titleImgFileName.equals(sysFileName)) {

						PsTzArtTitimgT psTzArtTitimgT = psTzArtTitimgTMapper.selectByPrimaryKey(titleImgFileName);

						if (null != psTzArtTitimgT) {
							String delArtTitleImgPath = "";

							String tzAttAUrl = psTzArtTitimgT.getTzAttAUrl();
							String tzYsAttachsysnam = psTzArtTitimgT.getTzYsAttachsysnam();
							String tzSlAttachsysnam = psTzArtTitimgT.getTzSlAttachsysnam();

							if (null != tzAttAUrl && !"".equals(tzAttAUrl)) {
								if (!tzAttAUrl.endsWith("/")) {
									tzAttAUrl = tzAttAUrl + "/";
								}

								delArtTitleImgPath = tzAttAUrl + titleImgFileName;
								fileManageServiceImpl.DeleteFile(delArtTitleImgPath);

								if (null != tzYsAttachsysnam && !"".equals(tzYsAttachsysnam)) {
									delArtTitleImgPath = tzAttAUrl + tzYsAttachsysnam;
									fileManageServiceImpl.DeleteFile(delArtTitleImgPath);
								}

								if (null != tzSlAttachsysnam && !"".equals(tzSlAttachsysnam)) {
									delArtTitleImgPath = tzAttAUrl + tzSlAttachsysnam;
									fileManageServiceImpl.DeleteFile(delArtTitleImgPath);
								}

								psTzArtTitimgTMapper.deleteByPrimaryKey(titleImgFileName);
							}

						}

					}
				}

				psTzArtHdTbl.setTzArtId(activityId);
				int rst = psTzArtHdTblMapper.updateByPrimaryKey(psTzArtHdTbl);
				if (rst > 0) {
					boolRst = true;
				}

				psTzArtRecTblWithBLOBs.setTzArtId(activityId);
				psTzArtRecTblWithBLOBs.setRowLastmantDttm(dateNow);
				psTzArtRecTblWithBLOBs.setRowLastmantOprid(oprid);
				psTzArtRecTblMapper.updateByPrimaryKeySelective(psTzArtRecTblWithBLOBs);

				psTzLmNrGlTWithBLOBs.setTzArtId(activityId);
				psTzLmNrGlTWithBLOBs.setTzLastmantDttm(dateNow);
				psTzLmNrGlTWithBLOBs.setTzLastmantOprid(oprid);
				psTzLmNrGlTMapper.updateByPrimaryKeySelective(psTzLmNrGlTWithBLOBs);

			} else {
				return strRet;
			}
			
			//删除限定的项目;
			sqlQuery.update("delete from PS_TZ_ART_PRJ_T where TZ_ART_ID=?",new Object[]{activityId});
			if (projects != null && projects.size() > 0) {
				for (int k = 0; k < projects.size(); k++) {
					String prjId = (String)projects.get(k);
					PsTzArtPrjTKey psTzArtPrjTKey = new PsTzArtPrjTKey();
					psTzArtPrjTKey.setTzArtId(activityId);
					psTzArtPrjTKey.setTzPrjId(prjId);
					psTzArtPrjTMapper.insert(psTzArtPrjTKey);
				}
			}

			if (boolRst) {

				// 设置session变量，在更新时用到
				Map<String, Object> mapEventInfoSession = new HashMap<String, Object>();
				mapEventInfoSession.put(sessSiteId, siteId);
				mapEventInfoSession.put(sessColuId, coluId);
				mapEventInfoSession.put(sessActivityId, activityId);
				mapEventInfoSession.put(sessPublishClick, publishClick);

				// 将活动参数写入session中
				JacksonUtil jacksonUtil = new JacksonUtil();
				String strEventInfoSession = jacksonUtil.Map2json(mapEventInfoSession);
				tzGetSetSessionValue.setTzAddingNewActivity(strEventInfoSession);

				// 同步报名人数,先锁表操作，避免同时更新引起报名人数和等候人数数据不对
				mySqlLockService.lockRow(sqlQuery, "TZ_NAUDLIST_T");

				sql = "SELECT COUNT(1) FROM PS_TZ_NAUDLIST_T WHERE TZ_ART_ID=? AND TZ_NREG_STAT='1'";
				int appliedNum = sqlQuery.queryForObject(sql, new Object[] { activityId }, "int");

				sql = "SELECT COUNT(1) FROM PS_TZ_NAUDLIST_T WHERE TZ_ART_ID=? AND TZ_NREG_STAT='4'";
				int waitingNum = sqlQuery.queryForObject(sql, new Object[] { activityId }, "int");

				if (applyNum < appliedNum) {
					// 席位数小于已报名数，将已报名的变为等待
					// 获取要更新的用户
					sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzSelectBmrForUpdateToWaiting");
					List<Map<String, Object>> listBmrids = sqlQuery.queryForList(sql,
							new Object[] { activityId, appliedNum - applyNum });

					String strBmrids = "";
					for (Map<String, Object> mapBmrid : listBmrids) {
						String bmrid = mapBmrid.get("TZ_HD_BMR_ID") == null ? ""
								: String.valueOf(mapBmrid.get("TZ_HD_BMR_ID"));
						if ("".equals(strBmrids)) {
							strBmrids = "'" + bmrid + "'";
						} else {
							strBmrids = strBmrids + ",'" + bmrid + "'";
						}
					}

					// 更新用户为等待状态
					sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzUpdateHDBmrToWaiting");
					sql = sql.replace(":BMRIDS", strBmrids);

					sqlQuery.update(sql, new Object[] { activityId });
				} else if (applyNum > appliedNum && waitingNum > 0) {
					// 席位数大于已报名人数，且存在排队人数，则自动进补
					// 获取要更新的用户
					sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzSelectBmrForUpdateToApplied");
					List<Map<String, Object>> listBmrids = sqlQuery.queryForList(sql,
							new Object[] { activityId, applyNum - appliedNum });

					String strBmrids = "";
					for (Map<String, Object> mapBmrid : listBmrids) {
						String bmrid = mapBmrid.get("TZ_HD_BMR_ID") == null ? ""
								: String.valueOf(mapBmrid.get("TZ_HD_BMR_ID"));
						if ("".equals(strBmrids)) {
							strBmrids = "'" + bmrid + "'";
						} else {
							strBmrids = strBmrids + ",'" + bmrid + "'";
						}
					}

					// 更新用户状态为已报名
					sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzUpdateHDBmrToApplied");
					sql = sql.replace(":BMRIDS", strBmrids);

					sqlQuery.update(sql, new Object[] { activityId });
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
			if ("".equals(activityId)) {
				// 从session中读取活动编号
				String strEventInfoSession = tzGetSetSessionValue.getTzAddingNewActivity();
				if ("".equals(strEventInfoSession) || null == strEventInfoSession) {
					errorMsg[0] = "1";
					errorMsg[1] = "机构站点参数错误！";
					return strRet;
				}

				JacksonUtil jacksonUtil = new JacksonUtil();
				Map<String, Object> mapEventInfoSession = jacksonUtil.parseJson2Map(strEventInfoSession);

				activityId = mapEventInfoSession.get(sessActivityId) == null ? ""
						: String.valueOf(mapEventInfoSession.get(sessActivityId));
			}
			if ("".equals(activityId) || null == activityId) {
				errorMsg[0] = "1";
				errorMsg[1] = "参数错误。活动编号为空。";
				return "";
			}

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
			if ("".equals(activityId)) {
				// 从session中读取活动编号
				String strEventInfoSession = tzGetSetSessionValue.getTzAddingNewActivity();
				if ("".equals(strEventInfoSession) || null == strEventInfoSession) {
					errorMsg[0] = "1";
					errorMsg[1] = "机构站点参数错误！";
					return strRet;
				}

				JacksonUtil jacksonUtil = new JacksonUtil();
				Map<String, Object> mapEventInfoSession = jacksonUtil.parseJson2Map(strEventInfoSession);

				activityId = mapEventInfoSession.get(sessActivityId) == null ? ""
						: String.valueOf(mapEventInfoSession.get(sessActivityId));
			}
			if ("".equals(activityId) || null == activityId) {
				errorMsg[0] = "1";
				errorMsg[1] = "参数错误。活动编号为空。";
				return "";
			}

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
			if ("".equals(activityId) || null == activityId || "null".equals(activityId)) {
				// 从session中读取活动编号
				String strEventInfoSession = tzGetSetSessionValue.getTzAddingNewActivity();
				if ("".equals(strEventInfoSession) || null == strEventInfoSession) {
					errorMsg[0] = "1";
					errorMsg[1] = "机构站点参数错误！";
					return strRet;
				}

				Map<String, Object> mapEventInfoSession = jacksonUtil.parseJson2Map(strEventInfoSession);

				activityId = mapEventInfoSession.get(sessActivityId) == null ? ""
						: String.valueOf(mapEventInfoSession.get(sessActivityId));
			}
			if ("".equals(activityId) || null == activityId) {
				errorMsg[0] = "1";
				errorMsg[1] = "参数错误。活动编号为空。";
				return "";
			}
			strRet = activityId;

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
					rst = psTzArtPicTMapper.updateByPrimaryKeyWithBLOBs(psTzArtPicT);
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

			if (null != sysFileNameArr && sysFileNameArr.size() >= 0) {
				String sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzGetEventTPJs");
				List<Map<String, Object>> listData = sqlQuery.queryForList(sql, new Object[] { activityId });

				for (Map<String, Object> mapData : listData) {
					String sysFname = mapData.get("TZ_ATTACHSYSFILENA") == null ? ""
							: String.valueOf(mapData.get("TZ_ATTACHSYSFILENA"));
					if (!sysFileNameArr.contains(sysFname)) {
						// 若数据库中有，而提交的数据中没有，则删除表中记录，并删除物理文件

						PsTzArtTpjT psTzArtTpjT = psTzArtTpjTMapper.selectByPrimaryKey(sysFname);

						if (null == psTzArtTpjT) {
							continue;
						}

						String delFilePath = "";

						String tzAttAUrl = psTzArtTpjT.getTzAttAUrl();
						String tzYsAttachsysnam = psTzArtTpjT.getTzYsAttachsysnam();
						String tzSlAttachsysnam = psTzArtTpjT.getTzSlAttachsysnam();

						if (null != tzAttAUrl && !"".equals(tzAttAUrl)) {
							if (!tzAttAUrl.endsWith("/")) {
								tzAttAUrl = tzAttAUrl + "/";
							}

							delFilePath = tzAttAUrl + sysFname;
							fileManageServiceImpl.DeleteFile(delFilePath);

							if (null != tzYsAttachsysnam && !"".equals(tzYsAttachsysnam)) {
								delFilePath = tzAttAUrl + tzYsAttachsysnam;
								fileManageServiceImpl.DeleteFile(delFilePath);
							}

							if (null != tzSlAttachsysnam && !"".equals(tzSlAttachsysnam)) {
								delFilePath = tzAttAUrl + tzSlAttachsysnam;
								fileManageServiceImpl.DeleteFile(delFilePath);
							}

							PsTzArtPicTKey psTzArtPicTKey = new PsTzArtPicTKey();
							psTzArtPicTKey.setTzArtId(activityId);
							psTzArtPicTKey.setTzAttachsysfilena(sysFname);
							psTzArtPicTMapper.deleteByPrimaryKey(psTzArtPicTKey);

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
				String tzAttAUrl = psTzArtFjjT.getTzAttAUrl();
				if (null != tzAttAUrl && !"".equals(tzAttAUrl)) {
					if (!tzAttAUrl.endsWith("/")) {
						tzAttAUrl = tzAttAUrl + "/";
					}
					String delFilePath = tzAttAUrl + attachmentID;
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
