package com.tranzvision.gd.TZWebSiteInfoMgBundle.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FileManageServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiDefnTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiDefnTWithBLOBs;
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
import com.tranzvision.gd.TZWebSiteInfoMgBundle.model.PsTzLmNrGlTKey;
import com.tranzvision.gd.TZWebSiteInfoMgBundle.model.PsTzLmNrGlTWithBLOBs;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.ResizeImageUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 内容发布；原：TZ_GD_ARTGL:ArtInfo
 * 
 * @author tang
 * @since 2015-11-23
 */
@Service("com.tranzvision.gd.TZWebSiteInfoMgBundle.service.impl.ArtInfoNewServiceImpl")
public class ArtInfoNewServiceImpl extends FrameworkImpl {
	/* 站点id,栏目id,内容id,是否发布 */
	private String instanceArtId;

	private String instanceSiteId;

	private String instanceColuId;

	private String ins_isPublish;

	private String ins_staticName;

	@Autowired
	private SqlQuery jdbcTemplate;

	@Autowired
	private FileManageServiceImpl fileManageServiceImpl;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private ArtContentHtml artContentHtml;
	@Autowired
	private ResizeImageUtil resizeImageUtil;
	@Autowired
	private PsTzArtRecTblMapper psTzArtRecTblMapper;
	@Autowired
	private PsTzLmNrGlTMapper psTzLmNrGlTMapper;
	@Autowired
	private PsTzSiteiDefnTMapper psTzSiteiDefnTMapper;
	@Autowired
	private PsTzArtTitimgTMapper psTzArtTitimgTMapper;
	@Autowired
	private PsTzArtFjjTMapper psTzArtFjjTMapper;
	@Autowired
	private PsTzArtTpjTMapper psTzArtTpjTMapper;
	@Autowired
	private PsTzArtPicTMapper psTzArtPicTMapper;
	@Autowired
	private PsTzArtFileTMapper psTzArtFileTMapper;
	@Autowired
	private PsTzArtPrjTMapper psTzArtPrjTMapper;
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	/* 查询表单信息 */
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();

		Map<String, Object> map = new HashMap<>();
		map.put("artId", "");
		map.put("artTitle", "");
		map.put("artShortTitle", "");
		map.put("artSubHead", "");
		map.put("artAbout", "");
		map.put("artMetaDesc", "");
		map.put("artMetaKeys", "");
		map.put("tztxt1", "");
		map.put("tztxt1Enabled", "");
		map.put("tztxt1Label", "");
		map.put("tztxt2", "");
		map.put("tztxt2Enabled", "");
		map.put("tztxt2Label", "");
		map.put("tztxt3", "");
		map.put("tztxt3Enabled", "");
		map.put("tztxt3Label", "");
		map.put("tztxt4", "");
		map.put("tztxt4Enabled", "");
		map.put("tztxt4Label", "");
		map.put("tzlong1", "");
		map.put("tzlong1Enabled", "");
		map.put("tzlong1Label", "");
		map.put("tzlong2", "");
		map.put("tzlong2Enabled", "");
		map.put("tzlong2Label", "");
		map.put("tzlong3", "");
		map.put("tzlong3Enabled", "");
		map.put("tzlong3Label", "");
		map.put("tzdate1", "");
		map.put("tzdate1Enabled", "");
		map.put("tzdate1Label", "");
		map.put("tzdate2", "");
		map.put("tzdate2Enabled", "");
		map.put("tzdate2Label", "");
		map.put("artType", "");
		map.put("externalLink", "");
		map.put("contentInfo", "");
		map.put("artFbz", "");
		map.put("artFbBm", "");
		map.put("startDate", "");
		map.put("startTime", "");
		map.put("endDate", "");
		map.put("endTime", "");
		map.put("titleImageTitle", "");
		map.put("titleImageDesc", "");
		map.put("titleImageUrl", "");
		map.put("publishStatus", "");
		map.put("publishUrl", "");
		map.put("siteId", "");
		map.put("coluId", "");
		map.put("siteType", "");
		map.put("staticPath", "");
		map.put("saveImageAccessUrl", "");
		map.put("saveAttachAccessUrl", "");
		map.put("artNewsDT", "");
		map.put("limit", "");
		map.put("projects", "");
		map.put("staticName", "");
		map.put("autoStaticName", "");

		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			jacksonUtil.json2Map(strParams);
			if (jacksonUtil.containsKey("artId")) {
				// 邮件服务器参数id;
				String strArtId = jacksonUtil.getString("artId");
				// 获取登录的机构;
				String strJgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);

				if (!jacksonUtil.containsKey("coluId")) {
					errMsg[0] = "1";
					errMsg[1] = "请选择添加内容的栏目！";
					returnJsonMap.put("formData", map);
					strRet = jacksonUtil.Map2json(returnJsonMap);
					return strRet;
				}

				String coluId = jacksonUtil.getString("coluId");

				if ("".equals(coluId) || (coluId == null)) {
					errMsg[0] = "1";
					errMsg[1] = "请选择添加内容的栏目！";
					returnJsonMap.put("formData", map);
					strRet = jacksonUtil.Map2json(returnJsonMap);
					return strRet;
				}

				// 获取机构对应的站点；
				String siteSQL = " SELECT TZ_SITEI_ID FROM PS_TZ_SITEI_COLU_T WHERE TZ_COLU_ID=?";
				String siteId = jdbcTemplate.queryForObject(siteSQL, new Object[] { coluId }, "String");
				if (siteId == null || "".equals(siteId)) {
					errMsg[0] = "1";
					errMsg[1] = "请选择添加内容的站点！";
					returnJsonMap.put("formData", map);
					strRet = jacksonUtil.Map2json(returnJsonMap);
					return strRet;
				}

				// 获取内容模板字段是否启用
				String artTypeItemSQL = "SELECT B.TZ_FIELD_VALUE,B.TZ_FIELD_DESC FROM PS_TZ_SITEI_COLU_T A,PS_TZ_CONT_FLDEF_T B "
						+ "WHERE A.TZ_COLU_ID = ? AND A.TZ_ART_TYPE_ID = B.TZ_ART_TYPE_ID AND IS_ENABLED_FLG = 'Y'";
				List<Map<String, Object>> artTypeItemlist = jdbcTemplate.queryForList(artTypeItemSQL,
						new Object[] { coluId });
				if (artTypeItemlist != null && artTypeItemlist.size() > 0) {
					for (int i = 0; i < artTypeItemlist.size(); i++) {
						String itemValue = String.valueOf(artTypeItemlist.get(i).get("TZ_FIELD_VALUE"));
						String itemLabel = String.valueOf(artTypeItemlist.get(i).get("TZ_FIELD_DESC"));
						switch (itemValue) {
						case "TZ_TXT1":
							map.replace("tztxt1Enabled", "Y");
							map.replace("tztxt1Label", itemLabel);
							break;

						case "TZ_TXT2":
							map.replace("tztxt2Enabled", "Y");
							map.replace("tztxt2Label", itemLabel);
							break;

						case "TZ_TXT3":
							map.replace("tztxt3Enabled", "Y");
							map.replace("tztxt3Label", itemLabel);
							break;

						case "TZ_TXT4":
							map.replace("tztxt4Enabled", "Y");
							map.replace("tztxt4Label", itemLabel);
							break;

						case "TZ_LONG1":
							map.replace("tzlong1Enabled", "Y");
							map.replace("tzlong1Label", itemLabel);
							break;

						case "TZ_LONG2":
							map.replace("tzlong2Enabled", "Y");
							map.replace("tzlong2Label", itemLabel);
							break;

						case "TZ_LONG3":
							map.replace("tzlong3Enabled", "Y");
							map.replace("tzlong3Label", itemLabel);
							break;

						case "TZ_DATE1":
							map.replace("tzdate1Enabled", "Y");
							map.replace("tzdate1Label", itemLabel);
							break;

						case "TZ_DATE2":
							map.replace("tzdate2Enabled", "Y");
							map.replace("tzdate2Label", itemLabel);
							break;

						default:
							break;
						}
					}
				}

				/* 获取站点类型 */
				String sqlGetSiteType = "select TZ_SITEI_TYPE from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";

				String strSiteType = jdbcTemplate.queryForObject(sqlGetSiteType, new Object[] { siteId }, "String");

				map.replace("siteType", strSiteType);

				if ("".equals(strArtId)) {
					String imageSQL = "select TZ_IMG_STOR,TZ_ATTS_STOR from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
					Map<String, Object> imagePathMap = jdbcTemplate.queryForMap(imageSQL, new Object[] { siteId });
					map.replace("siteId", siteId);
					map.replace("coluId", coluId);
					map.replace("saveImageAccessUrl", imagePathMap.get("TZ_IMG_STOR"));
					map.replace("saveAttachAccessUrl", imagePathMap.get("TZ_ATTS_STOR"));

					returnJsonMap.put("formData", map);
					strRet = jacksonUtil.Map2json(returnJsonMap);
					return strRet;
				}

				// 文章表;
				PsTzArtRecTblWithBLOBs psTzArtRecTbl = psTzArtRecTblMapper.selectByPrimaryKey(strArtId);
				String acessUrl = "";
				String titleImageUrl = "";
				String attachSysFile = psTzArtRecTbl.getTzAttachsysfilena();
				if (attachSysFile != null && !"".equals(attachSysFile)) {
					String arraurlSQL = "select TZ_ATT_A_URL from PS_TZ_ART_TITIMG_T where TZ_ATTACHSYSFILENA=?";
					acessUrl = jdbcTemplate.queryForObject(arraurlSQL, new Object[] { attachSysFile }, "String");

					if (acessUrl != null && !"".equals(acessUrl)) {
						if ((acessUrl.lastIndexOf("/") + 1) != acessUrl.length()) {
							titleImageUrl = acessUrl + "/" + attachSysFile;
						} else {
							titleImageUrl = acessUrl + attachSysFile;
						}
					}
				}
				// 内容站点关联表;
				PsTzLmNrGlTKey psTzLmNrGlTKey = new PsTzLmNrGlTKey();
				psTzLmNrGlTKey.setTzSiteId(siteId);
				psTzLmNrGlTKey.setTzColuId(coluId);
				psTzLmNrGlTKey.setTzArtId(strArtId);
				PsTzLmNrGlTWithBLOBs psTzLmNrGlT = psTzLmNrGlTMapper.selectByPrimaryKey(psTzLmNrGlTKey);

				// 站点表;
				PsTzSiteiDefnTWithBLOBs psTzSiteiDefnT = psTzSiteiDefnTMapper.selectByPrimaryKey(siteId);
				String saveImageAccessUrl = psTzSiteiDefnT.getTzImgStor();
				String saveAttachAccessUrl = psTzSiteiDefnT.getTzAttsStor();

				SimpleDateFormat datetimeFormate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat timeFormate = new SimpleDateFormat("HH:mm");
				String startDate = "", startTime = "", endDate = "", endTime = "", artNewsDT = "", artYlDt1 = "",
						artYlDt2 = "";
				try {
					startDate = dateFormate.format(psTzArtRecTbl.getTzStartDate());
				} catch (Exception e) {
				}
				try {
					startTime = timeFormate.format(psTzArtRecTbl.getTzStartTime());
				} catch (Exception e) {
				}
				try {
					endDate = dateFormate.format(psTzArtRecTbl.getTzEndDate());
				} catch (Exception e) {
				}
				try {
					endTime = timeFormate.format(psTzArtRecTbl.getTzEndTime());
				} catch (Exception e) {
				}
				try {
					artNewsDT = datetimeFormate.format(psTzLmNrGlT.getTzArtNewsDt());
				} catch (Exception e) {
				}

				try {
					artYlDt1 = dateFormate.format(psTzArtRecTbl.getTzDate1());
				} catch (Exception e) {
				}

				try {
					artYlDt2 = dateFormate.format(psTzArtRecTbl.getTzDate2());
				} catch (Exception e) {
				}

				String artStaticUrl = "";
				artStaticUrl = psTzLmNrGlT.getTzStaticArtUrl();
				String artUrl = "";
				String publicState = psTzLmNrGlT.getTzArtPubState();
				if ("Y".equals(publicState)) {
					String rootparth = "http://" + request.getServerName() + ":" + request.getServerPort()
							+ request.getContextPath();
					String classIdSQL = "select TZ_PAGE_REFCODE from PS_TZ_AQ_PAGZC_TBL where TZ_COM_ID=? and TZ_PAGE_ID=?";
					String publishUrlClassId = jdbcTemplate.queryForObject(classIdSQL,
							new Object[] { "TZ_ART_VIEW_COM", "TZ_ART_VIEW_STD" }, "String");
					artUrl = rootparth + "/dispatcher?classid=" + publishUrlClassId + "&operatetype=HTML&siteId="
							+ siteId + "&columnId=" + coluId + "&artId=" + strArtId;
					if ("A".equals(strSiteType) || "B".equals(strSiteType)) {
						artUrl = artStaticUrl;
					}
				}

				map.replace("artId", strArtId);
				map.replace("artTitle", psTzArtRecTbl.getTzArtTitle());
				map.replace("artShortTitle", psTzArtRecTbl.getTzArtShorttitle());
				map.replace("artSubHead", psTzArtRecTbl.getTzSubhead());
				map.replace("artAbout", psTzArtRecTbl.getTzAbout());
				map.replace("artMetaDesc", psTzArtRecTbl.getTzMetadesc());
				map.replace("artMetaKeys", psTzArtRecTbl.getTzMetakeys());
				map.replace("tztxt1", psTzArtRecTbl.getTzTxt1());
				map.replace("tztxt2", psTzArtRecTbl.getTzTxt2());
				map.replace("tztxt3", psTzArtRecTbl.getTzTxt3());
				map.replace("tztxt4", psTzArtRecTbl.getTzTxt4());
				map.replace("tzlong1", psTzArtRecTbl.getTzLong1());
				map.replace("tzlong2", psTzArtRecTbl.getTzLong2());
				map.replace("tzlong3", psTzArtRecTbl.getTzLong3());
				map.replace("tzdate1", artYlDt1);
				map.replace("tzdate2", artYlDt2);
				map.replace("artType", psTzArtRecTbl.getTzArtType1());
				map.replace("externalLink", psTzArtRecTbl.getTzOutArtUrl());
				map.replace("contentInfo", psTzArtRecTbl.getTzArtConent());
				map.replace("artFbz", psTzLmNrGlT.getTzFbz());
				map.replace("artFbBm", psTzLmNrGlT.getTzBltDept());
				map.replace("startDate", startDate);
				map.replace("startTime", startTime);
				map.replace("endDate", endDate);
				map.replace("endTime", endTime);
				map.replace("titleImageTitle", psTzArtRecTbl.getTzImageTitle());
				map.replace("titleImageDesc", psTzArtRecTbl.getTzImageDesc());
				map.replace("titleImageUrl", titleImageUrl);
				map.replace("publishStatus", psTzLmNrGlT.getTzArtPubState());
				map.replace("publishUrl", artUrl);
				map.replace("siteId", siteId);
				map.replace("coluId", coluId);
				map.replace("saveImageAccessUrl", saveImageAccessUrl);
				map.replace("saveAttachAccessUrl", saveAttachAccessUrl);
				map.replace("artNewsDT", artNewsDT);
				map.replace("staticName", psTzLmNrGlT.getTzStaticName());
				map.replace("autoStaticName", psTzLmNrGlT.getTzStaticAotoName());
				String limit = psTzArtRecTbl.getTzProjectLimit();
				if (limit == null) {
					limit = "";
				}
				map.replace("limit", limit);

				List<Map<String, Object>> prjList = jdbcTemplate.queryForList(
						"select TZ_PRJ_ID from PS_TZ_ART_PRJ_T where TZ_ART_ID=?", new Object[] { strArtId });
				if (prjList != null) {
					ArrayList<String> projects = new ArrayList<>();
					for (int h = 0; h < prjList.size(); h++) {
						String projectId = (String) prjList.get(h).get("TZ_PRJ_ID");
						projects.add(projectId);
					}
					map.replace("projects", projects);
				}

				returnJsonMap.put("formData", map);
			} else {
				errMsg[0] = "1";
				errMsg[1] = "参数不正确！";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}

	/* 新增站点内容文章信息 */
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("artId", "");
		returnJsonMap.put("siteId", "");
		returnJsonMap.put("publishUrl", "");
		returnJsonMap.put("viewUrl", "");

		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			// 发布的站点;
			String siteId = "";
			// 发布的栏目
			String coluId = "";

			String artId = "";

			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);

				// 类型标志
				String typeFlag = jacksonUtil.getString("typeFlag");

				if ("ACTINFO".equals(typeFlag)) {
					Map<String, Object> dataMap = jacksonUtil.getMap("data");

					// 发布的站点;
					siteId = (String) dataMap.get("siteId");

					// 发布的栏目;
					coluId = (String) dataMap.get("coluId");

					if (siteId != null && !"".equals(siteId) && coluId != null && !"".equals(coluId)) {
						// 静态化名称
						String strStaticName = dataMap.get("staticName") == null ? ""
								: String.valueOf(dataMap.get("staticName"));
						this.ins_staticName = strStaticName;
						/* 查看名称是否已经存在 */
						if (strStaticName != null && !"".equals(strStaticName)) {
							String sqlGetStaticNameCount = "SELECT COUNT(1) FROM PS_TZ_LM_NR_GL_T "
									+ "WHERE TZ_SITE_ID = ? AND TZ_COLU_ID = ? AND UPPER(TZ_STATIC_NAME) = UPPER(?)";
							int staticNameCount = jdbcTemplate.queryForObject(sqlGetStaticNameCount,
									new Object[] { siteId, coluId, strStaticName }, "Integer");
							if (staticNameCount > 0) {
								errMsg[0] = "1";
								errMsg[1] = "静态化文件名" + strStaticName + "已经存在，请更换静态化文件名";
								strRet = jacksonUtil.Map2json(returnJsonMap);
								return strRet;
							}
						}

						// 标题;
						String artTitle = (String) dataMap.get("artTitle");
						// 简短标题
						String artShortTitle = (String) dataMap.get("artShortTitle");
						// 副标题
						String artSubHead = (String) dataMap.get("artSubHead");
						// 文章简介
						String artAbout = (String) dataMap.get("artAbout");
						// meta描述
						String artMetaDesc = (String) dataMap.get("artMetaDesc");
						// meta关键字
						String artMetaKeys = (String) dataMap.get("artMetaKeys");
						// 预留字段4个txt;3个long类型，2个日期类型
						String tztxt1 = (String) dataMap.get("tztxt1");
						String tztxt2 = (String) dataMap.get("tztxt2");
						String tztxt3 = (String) dataMap.get("tztxt3");
						String tztxt4 = (String) dataMap.get("tztxt4");
						String tzlong1 = (String) dataMap.get("tzlong1");
						String tzlong2 = (String) dataMap.get("tzlong2");
						String tzlong3 = (String) dataMap.get("tzlong3");
						String tzdate1 = (String) dataMap.get("tzdate1");
						String tzdate2 = (String) dataMap.get("tzdate2");
						// 文章类型;
						String artType = (String) dataMap.get("artType");
						// 外部URL;
						String outArtUrl = (String) dataMap.get("externalLink");
						// 文章内容;
						String artContent = (String) dataMap.get("contentInfo");
						// 发布者;
						String artFbz = (String) dataMap.get("artFbz");
						// 发布部门;
						String artFbBm = (String) dataMap.get("artFbBm");
						// 发布时间;
						String artNewsDT = (String) dataMap.get("artNewsDT");

						// 查看范围;
						String limit = "";
						if (dataMap.containsKey("limit")) {
							limit = (String) dataMap.get("limit");
						}
						// 限定的项目;
						ArrayList<String> projects = new ArrayList<>();
						if (dataMap.get("projects") != null && !"".equals(dataMap.get("projects"))) {
							projects = (ArrayList<String>) dataMap.get("projects");
						}

						Date fbdt = null;
						SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
						if (artNewsDT != null && !"".equals(artNewsDT)) {
							try {
								fbdt = datetimeFormat.parse(artNewsDT);
							} catch (Exception e) {
								fbdt = null;
							}
						}

						Date yldt1 = null;
						Date yldt2 = null;

						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
						if (tzdate1 != null && !"".equals(tzdate1)) {
							try {
								yldt1 = dateFormat.parse(tzdate1);
							} catch (Exception e) {
								yldt1 = null;
							}
						}
						if (tzdate2 != null && !"".equals(tzdate2)) {
							try {
								yldt2 = dateFormat.parse(tzdate2);
							} catch (Exception e) {
								yldt2 = null;
							}
						}

						// 标题图标题;
						String titleImageTitle = (String) dataMap.get("titleImageTitle");
						// 标题图描述;
						String titleImageDesc = (String) dataMap.get("titleImageDesc");
						// 标题图URL;
						String titleImageUrl = (String) dataMap.get("titleImageUrl");
						String sysFileName = "";
						if (titleImageUrl != null && !"".equals(titleImageUrl)) {
							String arr[] = titleImageUrl.split("/");
							sysFileName = arr[arr.length - 1];
						}
						// 发布状态;
						String publishStatus = (String) dataMap.get("publishStatus");
						// 是否点击发布或撤销发布按钮;
						String publishClick = (String) dataMap.get("publishClick");
						// 是否点击了置顶;
						String upArtClick = (String) dataMap.get("upArtClick");
						// 文章id;
						artId = String.valueOf(getSeqNum.getSeqNum("TZ_ART_REC_TBL", "TZ_ART_ID"));

						// 内容表;
						PsTzArtRecTblWithBLOBs PsTzArtRecTbl = new PsTzArtRecTblWithBLOBs();
						PsTzArtRecTbl.setTzArtId(artId);
						PsTzArtRecTbl.setTzArtTitle(artTitle);
						PsTzArtRecTbl.setTzArtShorttitle(artShortTitle);
						PsTzArtRecTbl.setTzSubhead(artSubHead);
						PsTzArtRecTbl.setTzAbout(artAbout);
						PsTzArtRecTbl.setTzMetadesc(artMetaDesc);
						PsTzArtRecTbl.setTzMetakeys(artMetaKeys);
						PsTzArtRecTbl.setTzTxt1(tztxt1);
						PsTzArtRecTbl.setTzTxt2(tztxt2);
						PsTzArtRecTbl.setTzTxt3(tztxt3);
						PsTzArtRecTbl.setTzTxt4(tztxt4);
						PsTzArtRecTbl.setTzLong1(tzlong1);
						PsTzArtRecTbl.setTzLong2(tzlong2);
						PsTzArtRecTbl.setTzLong3(tzlong3);
						PsTzArtRecTbl.setTzDate1(yldt1);
						PsTzArtRecTbl.setTzDate2(yldt2);
						PsTzArtRecTbl.setTzArtTitleStyle(artTitle);
						PsTzArtRecTbl.setTzArtConent(artContent);
						PsTzArtRecTbl.setTzArtName(artTitle);
						PsTzArtRecTbl.setTzProjectLimit(limit);
						PsTzArtRecTbl.setTzArtType1(artType);
						PsTzArtRecTbl.setTzOutArtUrl(outArtUrl);
						PsTzArtRecTbl.setTzImageTitle(titleImageTitle);
						PsTzArtRecTbl.setTzImageDesc(titleImageDesc);
						PsTzArtRecTbl.setTzAttachsysfilena(sysFileName);
						PsTzArtRecTbl.setRowAddedDttm(new Date());
						PsTzArtRecTbl.setRowAddedOprid(oprid);
						PsTzArtRecTbl.setRowLastmantDttm(new Date());
						PsTzArtRecTbl.setRowLastmantOprid(oprid);
						psTzArtRecTblMapper.insert(PsTzArtRecTbl);

						// 内容站点关联表;
						PsTzLmNrGlTWithBLOBs psTzLmNrGlT = new PsTzLmNrGlTWithBLOBs();
						psTzLmNrGlT.setTzSiteId(siteId);
						psTzLmNrGlT.setTzColuId(coluId);
						psTzLmNrGlT.setTzArtId(artId);
						psTzLmNrGlT.setTzFbz(artFbz);
						psTzLmNrGlT.setTzBltDept(artFbBm);
						psTzLmNrGlT.setTzArtNewsDt(fbdt);
						psTzLmNrGlT.setTzStaticName(strStaticName);
						if ("Y".equals(publishClick)) {
							psTzLmNrGlT.setTzArtPubState(publishStatus);
							// 如果发布，且发布时间未填写则当前时间为发布时间;
							if ("Y".equals(publishStatus)) {
								this.ins_isPublish = "Y";
								if (fbdt == null) {
									fbdt = new Date();
									psTzLmNrGlT.setTzArtNewsDt(fbdt);
								}

							} else {
								this.ins_isPublish = "N";
							}
						} else {
							this.ins_isPublish = "";
						}
						// 置顶
						int maxSEQ = 0;
						if ("Y".equals(upArtClick)) {
							String maxSQL = "SELECT MAX(TZ_MAX_ZD_SEQ) FROM PS_TZ_LM_NR_GL_T WHERE TZ_SITE_ID=? AND TZ_COLU_ID=? AND TZ_ART_ID<>?";
							try {
								maxSEQ = jdbcTemplate.queryForObject(maxSQL, new Object[] { siteId, coluId, artId },
										"Integer");
							} catch (Exception e) {
								maxSEQ = 0;
							}
							psTzLmNrGlT.setTzMaxZdSeq(maxSEQ + 1);
						}

						psTzLmNrGlT.setTzLastmantDttm(new Date());
						psTzLmNrGlT.setTzLastmantOprid(oprid);
						psTzLmNrGlTMapper.insert(psTzLmNrGlT);

						// 删除限定的项目;
						jdbcTemplate.update("delete from PS_TZ_ART_PRJ_T where TZ_ART_ID=?", new Object[] { artId });
						if (projects != null && projects.size() > 0) {
							for (int k = 0; k < projects.size(); k++) {
								String prjId = (String) projects.get(k);
								PsTzArtPrjTKey psTzArtPrjTKey = new PsTzArtPrjTKey();
								psTzArtPrjTKey.setTzArtId(artId);
								psTzArtPrjTKey.setTzPrjId(prjId);
								psTzArtPrjTMapper.insert(psTzArtPrjTKey);
							}
						}

						this.instanceArtId = artId;
						this.instanceColuId = coluId;
						this.instanceSiteId = siteId;
					} else {
						errMsg[0] = "1";
						errMsg[1] = "未找到对应的栏目或站点";
						strRet = jacksonUtil.Map2json(returnJsonMap);
						return strRet;
					}
				}

			}

			String rootparth = "http://" + request.getServerName() + ":" + request.getServerPort()
					+ request.getContextPath();
			String classIdSQL = "select TZ_PAGE_REFCODE from PS_TZ_AQ_PAGZC_TBL where TZ_COM_ID=? and TZ_PAGE_ID=?";
			String viewUrlClassId = jdbcTemplate.queryForObject(classIdSQL,
					new Object[] { "TZ_ART_VIEW_COM", "TZ_ART_PREVIEW_STD" }, "String");
			String viewUrl = rootparth + "/dispatcher?classid=" + viewUrlClassId + "&operatetype=HTML&siteId=" + siteId
					+ "&columnId=" + coluId + "&artId=" + artId + "&oprate=R";

			String publishUrlClassId = jdbcTemplate.queryForObject(classIdSQL,
					new Object[] { "TZ_ART_VIEW_COM", "TZ_ART_VIEW_STD" }, "String");
			String publishUrl = rootparth + "/dispatcher?classid=" + publishUrlClassId + "&operatetype=HTML&siteId="
					+ siteId + "&columnId=" + coluId + "&artId=" + artId;

			returnJsonMap.replace("artId", artId);
			returnJsonMap.replace("siteId", siteId);
			returnJsonMap.replace("publishUrl", publishUrl);
			returnJsonMap.replace("viewUrl", viewUrl);
			System.out.println("执行add方法");
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}

	/* 新增站点内容文章信息 */
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("artId", "");
		returnJsonMap.put("siteId", "");
		returnJsonMap.put("publishUrl", "");
		returnJsonMap.put("viewUrl", "");

		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			// 发布的站点;
			String siteId = "";
			// 发布的栏目
			String coluId = "";
			// 发布的内容id;
			String artId = "";
			// 站点类型
			String strSiteType = "";

			String strStaticName = "";
			// 静态化名称自动编号
			String strAutoStaticName = "";

			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);

				// 类型标志
				String typeFlag = jacksonUtil.getString("typeFlag");

				if ("ACTINFO".equals(typeFlag)) {
					Map<String, Object> dataMap = jacksonUtil.getMap("data");

					// 发布的站点;
					siteId = (String) dataMap.get("siteId");

					// 发布的栏目;
					coluId = (String) dataMap.get("coluId");

					// 发布的内容id;
					artId = (String) dataMap.get("artId");

					this.instanceArtId = artId;
					this.instanceSiteId = siteId;
					this.instanceColuId = coluId;

					if (siteId != null && !"".equals(siteId) && coluId != null && !"".equals(coluId) && artId != null
							&& !"".equals(artId)) {

						// 静态化名称
						strStaticName = dataMap.get("staticName") == null ? ""
								: String.valueOf(dataMap.get("staticName"));
						ins_staticName = strStaticName;
						/* 查看名称是否已经存在 */
						if (strStaticName != null && !"".equals(strStaticName)) {
							String sqlGetStaticNameCount = "SELECT COUNT(1) FROM PS_TZ_LM_NR_GL_T "
									+ "WHERE TZ_SITE_ID = ? AND TZ_COLU_ID = ? AND TZ_ART_ID <> ? AND UPPER(TZ_STATIC_NAME) = UPPER(?)";
							int staticNameCount = jdbcTemplate.queryForObject(sqlGetStaticNameCount,
									new Object[] { siteId, coluId, artId, strStaticName }, "Integer");
							if (staticNameCount > 0) {
								errMsg[0] = "1";
								errMsg[1] = "静态化文件名" + strStaticName + "已经存在，请更换静态化文件名";
								strRet = jacksonUtil.Map2json(returnJsonMap);
								return strRet;
							}
						}

						// 标题;
						String artTitle = (String) dataMap.get("artTitle");
						// 简短标题
						String artShortTitle = (String) dataMap.get("artShortTitle");
						// 副标题
						String artSubHead = (String) dataMap.get("artSubHead");
						// 文章简介
						String artAbout = (String) dataMap.get("artAbout");
						// meta描述
						String artMetaDesc = (String) dataMap.get("artMetaDesc");
						// meta关键字
						String artMetaKeys = (String) dataMap.get("artMetaKeys");
						// 预留字段4个txt;3个long类型，2个日期类型
						String tztxt1 = (String) dataMap.get("tztxt1");
						String tztxt2 = (String) dataMap.get("tztxt2");
						String tztxt3 = (String) dataMap.get("tztxt3");
						String tztxt4 = (String) dataMap.get("tztxt4");
						String tzlong1 = (String) dataMap.get("tzlong1");
						String tzlong2 = (String) dataMap.get("tzlong2");
						String tzlong3 = (String) dataMap.get("tzlong3");
						String tzdate1 = (String) dataMap.get("tzdate1");
						String tzdate2 = (String) dataMap.get("tzdate2");
						// 文章类型;
						String artType = (String) dataMap.get("artType");
						// 外部URL;
						String outArtUrl = (String) dataMap.get("externalLink");
						// 文章内容;
						String artContent = (String) dataMap.get("contentInfo");
						// 发布者;
						String artFbz = (String) dataMap.get("artFbz");
						// 发布部门;
						String artFbBm = (String) dataMap.get("artFbBm");
						// 发布时间;
						String artNewsDT = (String) dataMap.get("artNewsDT");

						// 查看范围;
						String limit = "";
						if (dataMap.containsKey("limit")) {
							limit = (String) dataMap.get("limit");
						}
						// 限定的项目;
						ArrayList<String> projects = new ArrayList<>();
						if (dataMap.get("projects") != null && !"".equals(dataMap.get("projects"))) {
							projects = (ArrayList<String>) dataMap.get("projects");
						}

						Date fbdt = null;
						SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
						if (artNewsDT != null && !"".equals(artNewsDT)) {
							try {
								fbdt = datetimeFormat.parse(artNewsDT);
							} catch (Exception e) {
								fbdt = null;
							}
						}

						Date yldt1 = null;
						Date yldt2 = null;

						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
						if (tzdate1 != null && !"".equals(tzdate1)) {
							try {
								yldt1 = dateFormat.parse(tzdate1);
							} catch (Exception e) {
								yldt1 = null;
							}
						}
						if (tzdate2 != null && !"".equals(tzdate2)) {
							try {
								yldt2 = dateFormat.parse(tzdate2);
							} catch (Exception e) {
								yldt2 = null;
							}
						}

						// 标题图标题;
						String titleImageTitle = (String) dataMap.get("titleImageTitle");
						// 标题图描述;
						String titleImageDesc = (String) dataMap.get("titleImageDesc");
						// 标题图URL;
						String titleImageUrl = (String) dataMap.get("titleImageUrl");
						String sysFileName = "";
						if (titleImageUrl != null && !"".equals(titleImageUrl)) {
							String arr[] = titleImageUrl.split("/");
							sysFileName = arr[arr.length - 1];
						}
						// 查询图片是否发生改变;
						String imgtitleUrlSQL = "SELECT TZ_ATTACHSYSFILENA FROM PS_TZ_ART_REC_TBL WHERE TZ_ART_ID=? ";
						String originTitlFileName = jdbcTemplate.queryForObject(imgtitleUrlSQL, new Object[] { artId },
								"String");
						if (originTitlFileName != null && !"".equals(originTitlFileName)
								&& !originTitlFileName.equals(sysFileName)) {
							// 删除原上传的图片;
							String imageTitleDirSQL = "SELECT TZ_ATT_P_URL FROM PS_TZ_ART_TITIMG_T WHERE TZ_ATTACHSYSFILENA=?";
							String imageTitleDir = jdbcTemplate.queryForObject(imageTitleDirSQL,
									new Object[] { originTitlFileName }, "String");
							if (imageTitleDir != null && !"".equals(imageTitleDir.trim()) && originTitlFileName != null
									&& !"".equals(originTitlFileName.trim())) {
								if ((imageTitleDir.lastIndexOf(File.separator) + 1) != imageTitleDir.length()) {
									imageTitleDir = imageTitleDir + File.separator;
								}
								// 删除图片;
								File file = new File(imageTitleDir + originTitlFileName);
								if (file.exists() && file.isFile()) {
									file.delete();
								}
								// 删除压缩图片;
								file = new File(imageTitleDir + "MINI_" + originTitlFileName);
								if (file.exists() && file.isFile()) {
									file.delete();
								}
								file = new File(imageTitleDir + "NEW_" + originTitlFileName);
								if (file.exists() && file.isFile()) {
									file.delete();
								}
							}
							psTzArtTitimgTMapper.deleteByPrimaryKey(originTitlFileName);
						}
						// 发布状态;
						String publishStatus = (String) dataMap.get("publishStatus");
						// 是否点击发布或撤销发布按钮;
						String publishClick = (String) dataMap.get("publishClick");
						// 是否点击了置顶;
						String upArtClick = (String) dataMap.get("upArtClick");

						// 内容表;
						PsTzArtRecTblWithBLOBs PsTzArtRecTbl = new PsTzArtRecTblWithBLOBs();
						PsTzArtRecTbl.setTzArtId(artId);
						PsTzArtRecTbl.setTzArtTitle(artTitle);
						PsTzArtRecTbl.setTzArtShorttitle(artShortTitle);
						PsTzArtRecTbl.setTzSubhead(artSubHead);
						PsTzArtRecTbl.setTzAbout(artAbout);
						PsTzArtRecTbl.setTzMetadesc(artMetaDesc);
						PsTzArtRecTbl.setTzMetakeys(artMetaKeys);
						PsTzArtRecTbl.setTzTxt1(tztxt1);
						PsTzArtRecTbl.setTzTxt2(tztxt2);
						PsTzArtRecTbl.setTzTxt3(tztxt3);
						PsTzArtRecTbl.setTzTxt4(tztxt4);
						PsTzArtRecTbl.setTzLong1(tzlong1);
						PsTzArtRecTbl.setTzLong2(tzlong2);
						PsTzArtRecTbl.setTzLong3(tzlong3);
						PsTzArtRecTbl.setTzDate1(yldt1);
						PsTzArtRecTbl.setTzDate2(yldt2);
						PsTzArtRecTbl.setTzArtTitleStyle(artTitle);
						PsTzArtRecTbl.setTzArtConent(artContent);
						PsTzArtRecTbl.setTzArtName(artTitle);
						PsTzArtRecTbl.setTzProjectLimit(limit);
						PsTzArtRecTbl.setTzArtType1(artType);
						PsTzArtRecTbl.setTzOutArtUrl(outArtUrl);
						PsTzArtRecTbl.setTzImageTitle(titleImageTitle);
						PsTzArtRecTbl.setTzImageDesc(titleImageDesc);
						PsTzArtRecTbl.setTzAttachsysfilena(sysFileName);
						PsTzArtRecTbl.setRowLastmantDttm(new Date());
						PsTzArtRecTbl.setRowLastmantOprid(oprid);
						psTzArtRecTblMapper.updateByPrimaryKeySelective(PsTzArtRecTbl);

						// 内容站点关联表;
						PsTzLmNrGlTWithBLOBs psTzLmNrGlT = new PsTzLmNrGlTWithBLOBs();
						psTzLmNrGlT.setTzSiteId(siteId);
						psTzLmNrGlT.setTzColuId(coluId);
						psTzLmNrGlT.setTzArtId(artId);
						psTzLmNrGlT.setTzFbz(artFbz);
						psTzLmNrGlT.setTzBltDept(artFbBm);
						psTzLmNrGlT.setTzArtNewsDt(fbdt);

						if ("Y".equals(publishClick)) {
							psTzLmNrGlT.setTzArtPubState(publishStatus);
							// 如果发布，且发布时间未填写则当前时间为发布时间;
							if ("Y".equals(publishStatus)) {
								this.ins_isPublish = "Y";
								if (fbdt == null) {
									fbdt = new Date();
									psTzLmNrGlT.setTzArtNewsDt(fbdt);
								}
							} else {
								this.ins_isPublish = "N";
							}
						} else {
							this.ins_isPublish = "";
						}
						// 置顶
						int maxSEQ = 0;
						if ("Y".equals(upArtClick)) {
							String maxSQL = "SELECT MAX(TZ_MAX_ZD_SEQ) FROM PS_TZ_LM_NR_GL_T WHERE TZ_SITE_ID=? AND TZ_COLU_ID=? AND TZ_ART_ID<>?";
							try {
								maxSEQ = jdbcTemplate.queryForObject(maxSQL, new Object[] { siteId, coluId, artId },
										"Integer");
							} catch (Exception e) {
								e.printStackTrace();
								maxSEQ = 0;
							}
							psTzLmNrGlT.setTzMaxZdSeq(maxSEQ + 1);
						}

						psTzLmNrGlT.setTzLastmantDttm(new Date());
						psTzLmNrGlT.setTzLastmantOprid(oprid);
						// null未设置的不更新;
						psTzLmNrGlTMapper.updateByPrimaryKeySelective(psTzLmNrGlT);

						// 如果发布时间未空，则更新;
						if (fbdt == null) {
							String updateFbDtSQL = "update PS_TZ_LM_NR_GL_T set TZ_ART_NEWS_DT=null where TZ_ART_ID=? ";
							jdbcTemplate.update(updateFbDtSQL, new Object[] { artId });
						}

						// 删除限定的项目;
						jdbcTemplate.update("delete from PS_TZ_ART_PRJ_T where TZ_ART_ID=?", new Object[] { artId });
						if (projects != null && projects.size() > 0) {
							for (int k = 0; k < projects.size(); k++) {
								String prjId = (String) projects.get(k);
								PsTzArtPrjTKey psTzArtPrjTKey = new PsTzArtPrjTKey();
								psTzArtPrjTKey.setTzArtId(artId);
								psTzArtPrjTKey.setTzPrjId(prjId);
								psTzArtPrjTMapper.insert(psTzArtPrjTKey);
							}
						}
					} else {
						errMsg[0] = "1";
						errMsg[1] = "未找到对应的栏目或站点";
						strRet = jacksonUtil.Map2json(returnJsonMap);
						return strRet;
					}
				}

				if ("ARTTPJ".equals(typeFlag)) {
					int tpjNum = 0;
					ArrayList<String> tpjList = new ArrayList<>();
					if (jacksonUtil.containsKey("data0")) {
						String deleteAll = jacksonUtil.getString("data0");
						if ("deleteAll".equals(deleteAll)) {
							tpjList.add("");
						}
					} else {
						boolean BOOL = true;
						while (BOOL) {
							try {
								tpjNum++;
								String infoData = "data" + tpjNum;
								Map<String, Object> dataMap = jacksonUtil.getMap(infoData);
								if (dataMap == null) {
									BOOL = false;
								} else {
									// 系统文件名;
									String sysFileName = (String) dataMap.get("sysFileName");
									// 序号;
									int index = (int) dataMap.get("index");
									// 路径;
									// String src = (String) dataMap.get("src");
									// 描述;
									String caption = (String) dataMap.get("caption");
									// 跳转URL;
									String picURL = (String) dataMap.get("picURL");

									PsTzArtPicT psTzArtPicT = new PsTzArtPicT();
									psTzArtPicT.setTzArtId(this.instanceArtId);
									psTzArtPicT.setTzAttachsysfilena(sysFileName);
									psTzArtPicT.setTzPriority(index);
									psTzArtPicT.setTzImgDescr(caption);
									psTzArtPicT.setTzImgTrsUrl(picURL);

									String isExistImgSQL = "SELECT COUNT(1) FROM PS_TZ_ART_PIC_T where TZ_ART_ID=? and TZ_ATTACHSYSFILENA=?";
									int isExistImgCount = jdbcTemplate.queryForObject(isExistImgSQL,
											new Object[] { this.instanceArtId, sysFileName }, "Integer");
									int insertOrUpdateSuccess = 0;
									if (isExistImgCount > 0) {
										insertOrUpdateSuccess = psTzArtPicTMapper
												.updateByPrimaryKeyWithBLOBs(psTzArtPicT);
									} else {
										insertOrUpdateSuccess = psTzArtPicTMapper.insert(psTzArtPicT);
									}

									if (insertOrUpdateSuccess > 0) {
										tpjList.add(sysFileName);
									}

								}
							} catch (Exception e) {
								BOOL = false;
							}
						}
					}

					if (tpjList != null && tpjList.size() > 0) {
						// 删除图片;
						String tpjSQL = "SELECT a.TZ_ATTACHSYSFILENA from PS_TZ_ART_PIC_T a, PS_TZ_ART_TPJ_T b where a.TZ_ART_ID=? and a.TZ_ATTACHSYSFILENA=b.TZ_ATTACHSYSFILENA";
						List<Map<String, Object>> tpjAllList = jdbcTemplate.queryForList(tpjSQL,
								new Object[] { this.instanceArtId });
						if (tpjAllList != null && tpjAllList.size() > 0) {
							for (int i = 0; i < tpjAllList.size(); i++) {
								String sysFname = (String) tpjAllList.get(i).get("TZ_ATTACHSYSFILENA");
								if (!tpjList.contains(sysFname)) {

									PsTzArtPicTKey psTzArtPicTKey = new PsTzArtPicTKey();
									psTzArtPicTKey.setTzArtId(this.instanceArtId);
									psTzArtPicTKey.setTzAttachsysfilena(sysFname);
									psTzArtPicTMapper.deleteByPrimaryKey(psTzArtPicTKey);

									// 删除图片集;
									PsTzArtTpjT psTzArtTpjT = psTzArtTpjTMapper.selectByPrimaryKey(sysFname);
									if (psTzArtTpjT != null) {
										String imageDir = psTzArtTpjT.getTzAttPUrl();
										if ((imageDir.lastIndexOf(File.separator) + 1) != imageDir.length()) {
											imageDir = imageDir + File.separator;
										}
										// 删除图片;
										File file = new File(imageDir + sysFname);
										if (file.exists() && file.isFile()) {
											file.delete();
										}
										// 删除压缩图片;
										file = new File(imageDir + "MINI_" + sysFname);
										if (file.exists() && file.isFile()) {
											file.delete();
										}
										file = new File(imageDir + "NEW_" + sysFname);
										if (file.exists() && file.isFile()) {
											file.delete();
										}

										psTzArtTpjTMapper.deleteByPrimaryKey(sysFname);
									}

								}
							}
						}
					}
				}

				if ("ARTATTACHINFO".equals(typeFlag)) {
					Map<String, Object> dataMap = jacksonUtil.getMap("data");
					// 附件系统文件名;
					String attachmentID = (String) dataMap.get("attachmentID");
					String isAttchInsertSQL = "SELECT COUNT(1) FROM PS_TZ_ART_FILE_T WHERE TZ_ART_ID=? AND TZ_ATTACHSYSFILENA=?";
					int isAttchInsertNum = jdbcTemplate.queryForObject(isAttchInsertSQL,
							new Object[] { this.instanceArtId, attachmentID }, "Integer");
					if (isAttchInsertNum <= 0) {
						PsTzArtFileTKey psTzArtFileTKey = new PsTzArtFileTKey();
						psTzArtFileTKey.setTzArtId(this.instanceArtId);
						psTzArtFileTKey.setTzAttachsysfilena(attachmentID);
						psTzArtFileTMapper.insert(psTzArtFileTKey);
					}
				}

			}

			// 发布的站点;
			siteId = this.instanceSiteId;
			// 发布的栏目
			coluId = this.instanceColuId;
			// 发布的内容id;
			artId = this.instanceArtId;

			/* 获取站点类型 */
			String sqlGetSiteType = "select TZ_SITEI_TYPE from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
			strSiteType = jdbcTemplate.queryForObject(sqlGetSiteType, new Object[] { this.instanceSiteId }, "String");
			// 获取静态路径地址
			String strBasePath = "";
			String getBasePathSql = "SELECT TZ_COLU_PATH FROM PS_TZ_SITEI_COLU_T WHERE TZ_SITEI_ID = ? AND TZ_COLU_LEVEL = '0' LIMIT 1";
			strBasePath = jdbcTemplate.queryForObject(getBasePathSql, new Object[] { this.instanceSiteId }, "String");
			String strColuPath = "";
			String getColuPathSql = "SELECT TZ_COLU_PATH FROM PS_TZ_SITEI_COLU_T WHERE TZ_SITEI_ID = ? AND TZ_COLU_ID = ? LIMIT 1";
			strColuPath = jdbcTemplate.queryForObject(getColuPathSql,
					new Object[] { this.instanceSiteId, this.instanceColuId }, "String");
			String getAutoStaticNameSql = "SELECT TZ_STATIC_AOTO_NAME FROM PS_TZ_LM_NR_GL_T WHERE TZ_SITE_ID = ? AND TZ_COLU_ID = ? AND TZ_ART_ID = ? LIMIT 1";
			strAutoStaticName = jdbcTemplate.queryForObject(getAutoStaticNameSql,
					new Object[] { this.instanceSiteId, this.instanceColuId, this.instanceArtId }, "String");
			String getOriginStaticNameSql = "SELECT TZ_STATIC_NAME FROM PS_TZ_LM_NR_GL_T WHERE TZ_SITE_ID = ? AND TZ_COLU_ID = ? AND TZ_ART_ID = ? LIMIT 1";
			String strOriginStaticName = jdbcTemplate.queryForObject(getOriginStaticNameSql,
					new Object[] { this.instanceSiteId, this.instanceColuId, this.instanceArtId }, "String");

			String strFileName = "";
			String strFilePath = "";
			String strFilePathAccess = "";

			if (strBasePath != null && !"".equals(strBasePath)) {
				if (!strBasePath.startsWith("/")) {
					strBasePath = "/" + strBasePath;
				}
				if (strBasePath.endsWith("/")) {
					strBasePath = strBasePath.substring(0, strBasePath.length() - 1);
				}
			}

			if (strColuPath != null && !"".equals(strColuPath)) {
				if (!strColuPath.startsWith("/")) {
					strColuPath = "/" + strColuPath;
				}
				if (strColuPath.endsWith("/")) {
					strColuPath = strColuPath.substring(0, strColuPath.length() - 1);
				}
			}

			String dir = getSysHardCodeVal.getWebsiteEnrollPath();
			dir = request.getServletContext().getRealPath(dir);
			strFilePath = dir + strBasePath + strColuPath;
			strFilePathAccess = strBasePath + strColuPath;

			String rootparth = "http://" + request.getServerName() + ":" + request.getServerPort()
					+ request.getContextPath();
			String classIdSQL = "select TZ_PAGE_REFCODE from PS_TZ_AQ_PAGZC_TBL where TZ_COM_ID=? and TZ_PAGE_ID=?";
			String viewUrlClassId = jdbcTemplate.queryForObject(classIdSQL,
					new Object[] { "TZ_ART_VIEW_COM", "TZ_ART_PREVIEW_STD" }, "String");
			String viewUrl = rootparth + "/dispatcher?classid=" + viewUrlClassId + "&operatetype=HTML&siteId="
					+ this.instanceSiteId + "&columnId=" + this.instanceColuId + "&artId=" + this.instanceArtId
					+ "&oprate=R";
			String publishUrlClassId = jdbcTemplate.queryForObject(classIdSQL,
					new Object[] { "TZ_ART_VIEW_COM", "TZ_ART_VIEW_STD" }, "String");
			String publishUrl = rootparth + "/dispatcher?classid=" + publishUrlClassId + "&operatetype=HTML&siteId="
					+ this.instanceSiteId + "&columnId=" + this.instanceColuId + "&artId=" + this.instanceArtId;
			// 解析的模板内容;
			System.out.println(siteId + "--" + coluId + "--" + artId);
			String contentHtml = artContentHtml.getContentHtml(siteId, coluId, artId);

			// contentHtml = "测试文章静态11";

			PsTzLmNrGlTWithBLOBs psTzLmNrGlTWithBLOBs = new PsTzLmNrGlTWithBLOBs();
			psTzLmNrGlTWithBLOBs.setTzSiteId(this.instanceSiteId);
			psTzLmNrGlTWithBLOBs.setTzColuId(this.instanceColuId);
			psTzLmNrGlTWithBLOBs.setTzArtId(this.instanceArtId);
			psTzLmNrGlTWithBLOBs.setTzArtHtml(contentHtml);
			if ("Y".equals(this.ins_isPublish)) {
				psTzLmNrGlTWithBLOBs.setTzArtConentScr(contentHtml);

				// 如果是外网站点，则静态话
				if ("A".equals(strSiteType) || "B".equals(strSiteType)) {
					// 静态化
					if (ins_staticName != null && !"".equals(ins_staticName)) {
						strFileName = ins_staticName + ".html";
						if (!strOriginStaticName.equals(ins_staticName)) {
							if (strOriginStaticName != null && !"".equals(strOriginStaticName)) {
								fileManageServiceImpl.DeleteFile(strFilePath, strOriginStaticName + ".html");
							}
						}
						if (strAutoStaticName != null && !"".equals(strAutoStaticName)) {
							fileManageServiceImpl.DeleteFile(strFilePath, strAutoStaticName + ".html");
						}
					} else {

						if (strAutoStaticName != null && !"".equals(strAutoStaticName)) {
						} else {
							strAutoStaticName = String
									.valueOf(getSeqNum.getSeqNum("TZ_LM_NR_GL_T", "TZ_STATIC_A_NAME"));
							psTzLmNrGlTWithBLOBs.setTzStaticAotoName(strAutoStaticName);
						}
						strFileName = strAutoStaticName + ".html";
					}
					// 修改 bu caoy 地址没有加系统变量
					publishUrl = strFilePathAccess + "/" + strFileName;
					psTzLmNrGlTWithBLOBs.setTzStaticArtUrl(publishUrl);
					psTzLmNrGlTWithBLOBs.setTzStaticName(ins_staticName);
					psTzLmNrGlTMapper.updateByPrimaryKeySelective(psTzLmNrGlTWithBLOBs);

					System.out.println("路劲" + strFilePath);
					boolean b = artContentHtml.staticFile(contentHtml, strFilePath, strFileName);
					artContentHtml.staticSiteInfoByChannel(siteId, coluId);
				} else {
					psTzLmNrGlTWithBLOBs.setTzStaticName(ins_staticName);
					psTzLmNrGlTMapper.updateByPrimaryKeySelective(psTzLmNrGlTWithBLOBs);
				}
			} else {
				if ("N".equals(this.ins_isPublish)) {
					psTzLmNrGlTWithBLOBs.setTzArtConentScr("");
					if (strAutoStaticName != null && !"".equals(strAutoStaticName)) {

						fileManageServiceImpl.DeleteFile(strFilePath, strAutoStaticName + ".html");
					}
					if (strOriginStaticName != null && !"".equals(strOriginStaticName)) {

						fileManageServiceImpl.DeleteFile(strFilePath, strOriginStaticName + ".html");
					}
					psTzLmNrGlTWithBLOBs.setTzStaticName(ins_staticName);
					psTzLmNrGlTMapper.updateByPrimaryKeySelective(psTzLmNrGlTWithBLOBs);
					artContentHtml.staticSiteInfoByChannel(siteId, coluId);
				} else {
					psTzLmNrGlTWithBLOBs.setTzStaticName(ins_staticName);
					psTzLmNrGlTMapper.updateByPrimaryKeySelective(psTzLmNrGlTWithBLOBs);
				}
			}
			returnJsonMap.replace("artId", this.instanceArtId);
			returnJsonMap.replace("siteId", this.instanceSiteId);
			returnJsonMap.replace("publishUrl", publishUrl);
			returnJsonMap.replace("viewUrl", viewUrl);
			// System.out.println("执行update方法");
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}

	@Override
	public String tzGetHtmlContent(String strParams) {
		// 返回值;
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		String desc = "";
		String minSysFile = "";
		String newSysFile = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			if (jacksonUtil.containsKey("attachmentType") && jacksonUtil.containsKey("data")) {
				String attachmentType = jacksonUtil.getString("attachmentType");

				if ("IMG".equals(attachmentType)) {
					Map<String, Object> dataMap = jacksonUtil.getMap("data");
					if (dataMap != null) {
						String accessPath = (String) dataMap.get("accessPath");
						// 根据相对路径得到物理路径;
						String path = request.getServletContext().getRealPath(accessPath);
						String sysFileName1 = (String) dataMap.get("sysFileName");

						/* 压缩图片 */
						String separator = File.separator;

						String flg = "";
						if ((path.lastIndexOf(separator) + 1) == path.length()) {
							flg = resizeImageUtil.resize(path + sysFileName1, path, "MINI_" + sysFileName1, 100);
						} else {
							flg = resizeImageUtil.resize(path + separator + sysFileName1, path, "MINI_" + sysFileName1,
									100);
						}
						if ("Y".equals(flg)) {
							minSysFile = "MINI_" + sysFileName1;
						} else {
							minSysFile = sysFileName1;
						}
						if ((path.lastIndexOf(separator) + 1) == path.length()) {
							flg = resizeImageUtil.resize(path + sysFileName1, path, "NEW_" + sysFileName1, 1000);
						} else {
							flg = resizeImageUtil.resize(path + separator + sysFileName1, path, "NEW_" + sysFileName1,
									1000);
						}
						if ("Y".equals(flg)) {
							newSysFile = "NEW_" + sysFileName1;
						} else {
							newSysFile = sysFileName1;
						}

						PsTzArtTitimgT psTzArtTitimgT = new PsTzArtTitimgT();
						psTzArtTitimgT.setTzAttachsysfilena(sysFileName1);
						psTzArtTitimgT.setTzAttachfileName((String) dataMap.get("filename"));
						psTzArtTitimgT.setTzAttPUrl(path);
						psTzArtTitimgT.setTzAttAUrl(accessPath);
						psTzArtTitimgT.setTzYsAttachsysnam(newSysFile);
						psTzArtTitimgT.setTzSlAttachsysnam(minSysFile);

						psTzArtTitimgTMapper.insert(psTzArtTitimgT);
					}
				}

				if ("ATTACHMENT".equals(attachmentType)) {
					Map<String, Object> dataMap = jacksonUtil.getMap("data");
					if (dataMap != null) {
						String accessPath = (String) dataMap.get("accessPath");
						// 根据相对路径得到物理路径;
						String path = request.getServletContext().getRealPath(accessPath);
						PsTzArtFjjT psTzArtFjjT = new PsTzArtFjjT();
						psTzArtFjjT.setTzAttachsysfilena((String) dataMap.get("sysFileName"));
						psTzArtFjjT.setTzAttachfileName((String) dataMap.get("filename"));
						psTzArtFjjT.setTzAttPUrl(path);
						psTzArtFjjT.setTzAttAUrl(accessPath);
						psTzArtFjjTMapper.insert(psTzArtFjjT);
					}
				}

				if ("TPJ".equals(attachmentType)) {
					// int order = jacksonUtil.getInt("order");
					Map<String, Object> dataMap = jacksonUtil.getMap("data");
					if (dataMap != null) {
						String accessPath = (String) dataMap.get("accessPath");
						// 根据相对路径得到物理路径;
						String path = request.getServletContext().getRealPath(accessPath);
						String sysFileName = (String) dataMap.get("sysFileName");

						/* 压缩图片 */
						String separator = File.separator;
						String flg = "";
						if ((path.lastIndexOf(separator) + 1) == path.length()) {
							flg = resizeImageUtil.resize(path + sysFileName, path, "MINI_" + sysFileName, 100);
						} else {
							flg = resizeImageUtil.resize(path + separator + sysFileName, path, "MINI_" + sysFileName,
									100);
						}
						if ("Y".equals(flg)) {
							minSysFile = "MINI_" + sysFileName;
						} else {
							minSysFile = sysFileName;
						}

						if ((path.lastIndexOf(separator) + 1) == path.length()) {
							flg = resizeImageUtil.resize(path + sysFileName, path, "NEW_" + sysFileName, 1000);
						} else {
							flg = resizeImageUtil.resize(path + separator + sysFileName, path, "NEW_" + sysFileName,
									1000);
						}
						if ("Y".equals(flg)) {
							newSysFile = "NEW_" + sysFileName;
						} else {
							newSysFile = sysFileName;
						}

						PsTzArtTpjT psTzArtTpjT = new PsTzArtTpjT();
						psTzArtTpjT.setTzAttachsysfilena(sysFileName);
						psTzArtTpjT.setTzAttachfileName((String) dataMap.get("filename"));
						psTzArtTpjT.setTzAttPUrl(path);
						psTzArtTpjT.setTzAttAUrl(accessPath);
						psTzArtTpjT.setTzYsAttachsysnam(newSysFile);
						psTzArtTpjT.setTzSlAttachsysnam(minSysFile);

						psTzArtTpjTMapper.insert(psTzArtTpjT);
					}
				}
			} else {
				desc = "参数不正确";
			}
		} catch (Exception e) {
			e.printStackTrace();
			desc = e.toString();
		}

		if (!"".equals(desc)) {
			returnJsonMap.put("success", "1");
			returnJsonMap.put("message", desc);
			returnJsonMap.put("minPicSysFileName", minSysFile);
		} else {
			returnJsonMap.put("success", "0");
			returnJsonMap.put("message", "");
			returnJsonMap.put("minPicSysFileName", minSysFile);
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}

	/* 查询内容列表 */
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			if (jacksonUtil.containsKey("artId") && jacksonUtil.containsKey("gridTyp")) {
				String artId = jacksonUtil.getString("artId");
				String gridTyp = jacksonUtil.getString("gridTyp");
				String path = request.getContextPath();
				int total = 0;
				// 附件集
				if ("FJ".equals(gridTyp)) {
					String fjjSQL = "select a.TZ_ATTACHSYSFILENA,b.TZ_ATTACHFILE_NAME,b.TZ_ATT_A_URL from PS_TZ_ART_FILE_T a, PS_TZ_ART_FJJ_T b where a.TZ_ART_ID = ? and a.TZ_ATTACHSYSFILENA=b.TZ_ATTACHSYSFILENA";
					List<Map<String, Object>> list = jdbcTemplate.queryForList(fjjSQL, new Object[] { artId });
					if (list != null) {
						for (int i = 0; i < list.size(); i++) {
							total = total + 1;
							Map<String, Object> fjmapList = new HashMap<String, Object>();
							String sysName = (String) list.get(i).get("TZ_ATTACHSYSFILENA");
							String name = (String) list.get(i).get("TZ_ATTACHFILE_NAME");
							String url = (String) list.get(i).get("TZ_ATT_A_URL");
							if ((url.lastIndexOf("/") + 1) != url.length()) {
								url = url + "/";
							}
							String attachmentName = name + "|" + path + url + sysName;
							String attachmentUrl = url + sysName;
							fjmapList.put("attachmentID", sysName);
							fjmapList.put("attachmentName", attachmentName);
							fjmapList.put("attachmentUrl", attachmentUrl);

							listData.add(fjmapList);
						}
						mapRet.replace("total", total);
						mapRet.replace("root", listData);
					}

				}

				// 图片集
				if ("TPJ".equals(gridTyp)) {
					String tpqSQL = "SELECT a.TZ_ATTACHSYSFILENA,a.TZ_PRIORITY,b.TZ_ATT_A_URL,a.TZ_IMG_DESCR,a.TZ_IMG_TRS_URL,b.TZ_SL_ATTACHSYSNAM from PS_TZ_ART_PIC_T a,PS_TZ_ART_TPJ_T b where a.TZ_ART_ID=? and a.TZ_ATTACHSYSFILENA=b.TZ_ATTACHSYSFILENA order by a.TZ_PRIORITY ASC";
					List<Map<String, Object>> list = jdbcTemplate.queryForList(tpqSQL, new Object[] { artId });
					if (list != null) {
						for (int i = 0; i < list.size(); i++) {
							total = total + 1;
							String url = (String) list.get(i).get("TZ_ATT_A_URL");
							if ((url.lastIndexOf("/") + 1) != url.length()) {
								url = url + "/";
							}

							Map<String, Object> tpjmapList = new HashMap<String, Object>();
							tpjmapList.put("sysFileName", (String) list.get(i).get("TZ_ATTACHSYSFILENA"));
							tpjmapList.put("index", (long) list.get(i).get("TZ_PRIORITY"));
							tpjmapList.put("src", url + (String) list.get(i).get("TZ_ATTACHSYSFILENA"));
							tpjmapList.put("caption", (String) list.get(i).get("TZ_IMG_DESCR"));
							tpjmapList.put("picURL", (String) list.get(i).get("TZ_IMG_TRS_URL"));
							tpjmapList.put("sltUrl", path + url + (String) list.get(i).get("TZ_SL_ATTACHSYSNAM"));

							listData.add(tpjmapList);
						}

						mapRet.replace("total", total);
						mapRet.replace("root", listData);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return jacksonUtil.Map2json(mapRet);
	}

	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "";

		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 提交信息
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);

				String typeFlag = jacksonUtil.getString("typeFlag");
				String artId = jacksonUtil.getString("artId");
				Map<String, Object> data = jacksonUtil.getMap("data");
				if ("ARTATTACHINFO".equals(typeFlag) && !"".equals(artId) && data != null) {
					// 附件系统文件名;
					String attachmentID = (String) data.get("attachmentID");
					if (attachmentID != null && !"".equals(attachmentID)) {
						PsTzArtFileTKey psTzArtFileTKey = new PsTzArtFileTKey();
						psTzArtFileTKey.setTzArtId(artId);
						psTzArtFileTKey.setTzAttachsysfilena(attachmentID);
						psTzArtFileTMapper.deleteByPrimaryKey(psTzArtFileTKey);

						// 删除附件;
						PsTzArtFjjT psTzArtFjjT = psTzArtFjjTMapper.selectByPrimaryKey(attachmentID);
						if (psTzArtFjjT != null) {
							String fjDir = psTzArtFjjT.getTzAttPUrl();
							if ((fjDir.lastIndexOf(File.separator) + 1) != fjDir.length()) {
								fjDir = fjDir + File.separator;
							}
							// 删除附件;
							File file = new File(fjDir + attachmentID);
							if (file.exists() && file.isFile()) {
								file.delete();
							}

							psTzArtFjjTMapper.deleteByPrimaryKey(attachmentID);
						}
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRet;
	}
}