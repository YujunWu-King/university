/**
 * 
 */
package com.tranzvision.gd.TZSitePageBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FileManageServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiDefnTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiDefnTWithBLOBs;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 站点页面处理程序，原PS：TZ_SITE_DECORATED_APP:TZ_SITE_MG_CLS
 * 
 * @author SHIHUA
 * @since 2015-12-11
 */
@Service("com.tranzvision.gd.TZSitePageBundle.service.impl.TzSiteMgServiceImpl")
public class TzSiteMgServiceImpl extends FrameworkImpl {

	@Autowired
	private ApplicationContext ctx;

	@Autowired
	private JacksonUtil jacksonUtil;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private PsTzSiteiDefnTMapper psTzSiteiDefnTMapper;

	@Autowired
	private FileManageServiceImpl fileManageServiceImpl;

	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	@Autowired
	private SiteRepCssServiceImpl siteRepCssServiceImpl;
	
	@Autowired
	private TZGDObject tzGDObject;

	@SuppressWarnings("unchecked")
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {

		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");

		ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();

		try {

			String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);

			String sql = "select TZ_SITEI_ID from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ENABLE='Y' and TZ_JG_ID=?";
			String strSiteId = sqlQuery.queryForObject(sql, new Object[] { orgid }, "String");

			sql = "select * from PS_TZ_SITE_FUNC_T where STATE='Y'";
			List<?> listData = sqlQuery.queryForList(sql);

			for (Object obj : listData) {

				Map<String, Object> mapData = (Map<String, Object>) obj;

				Map<String, Object> mapJson = new HashMap<String, Object>();

				mapJson.put("siteId", strSiteId);
				mapJson.put("text", String.valueOf(mapData.get("FUNC_NAME")));
				mapJson.put("type", String.valueOf(mapData.get("FUNC_TYPE")));
				mapJson.put("url", String.valueOf(mapData.get("URL")));
				mapJson.put("icon", String.valueOf(mapData.get("ICON")));
				mapJson.put("desc", String.valueOf(mapData.get("DESCR")));

				listJson.add(mapJson);
			}

			mapRet.replace("total", listJson.size());
			mapRet.replace("root", listJson);

		} catch (Exception e) {
			e.printStackTrace();
			mapRet.clear();
			mapRet.put("success", false);
			errorMsg[0] = "1";
			errorMsg[1] = "获取功能列表数据出错！";
		}

		return jacksonUtil.Map2json(mapRet);

	}

	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "{}";

		try {

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "获取功能列表数据出错！";
		}

		return strRet;
	}

	@Override
	@Transactional
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		try {

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				String typeFlag = jacksonUtil.getString("typeFlag");
				Map<String, Object> formData = jacksonUtil.getMap("data");

				switch (typeFlag) {
				case "save":
					strRet = this.savePage(formData, errMsg);
					break;
				case "release":
					strRet = this.releasePage(formData, errMsg);
					break;
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

	/**
	 * 保存首页、登录页、注册页
	 * 
	 * @param formData
	 * @param errMsg
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	private String savePage(Map<String, Object> formData, String[] errMsg) {

		String strRet = "";

		Map<String, Object> mapRet = new HashMap<String, Object>();

		try {
			String strSiteIdSrc = formData.get("siteId") == null ? "" : String.valueOf(formData.get("siteId"));
			String strSiteId = "";
			if ("".equals(strSiteIdSrc)) {

				return strRet;
			}

			String strSaveContent = formData.get("savecontent") == null ? ""
					: String.valueOf(formData.get("savecontent"));
			String strPagetype = formData.get("pagetype") == null ? "" : String.valueOf(formData.get("pagetype"));
			strPagetype = strPagetype.toLowerCase();

			ArrayList<Map<String, Object>> listActData = (ArrayList<Map<String, Object>>) formData.get("dataArea");

			for (Map<String, Object> mapActData : listActData) {

				strSiteId = mapActData.get("siteId") == null ? "" : String.valueOf(mapActData.get("siteId"));
				if ("".equals(strSiteId)) {
					continue;
				}

				String sql = "select TZ_JG_ID from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
				//String strOrgId = sqlQuery.queryForObject(sql, new Object[] { strSiteId }, "String");

				String strAreaId = mapActData.get("areaId") == null ? "" : String.valueOf(mapActData.get("areaId"));
				//String strAreaZone = mapActData.get("areaZone") == null ? "": String.valueOf(mapActData.get("areaZone"));
				String strAreaType = mapActData.get("areaType") == null ? ""
						: String.valueOf(mapActData.get("areaType"));

				String strAreaTypeId = "";
				if ("".equals(strAreaId)) {
					sql = "select TZ_AREA_TYPE_ID from PS_TZ_SITEI_AREA_T where TZ_SITEI_ID=? and TZ_AREA_TYPE=? and TZ_AREA_STATE='Y'";
					strAreaTypeId = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strAreaType }, "String");
				} else {
					sql = "select TZ_AREA_TYPE_ID from PS_TZ_SITEI_AREA_T where TZ_SITEI_ID=? and TZ_AREA_ID=? and TZ_AREA_STATE='Y'";
					strAreaTypeId = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strAreaId }, "String");
				}

				if (null != strAreaTypeId && !"".equals(strAreaTypeId)) {
					sql = "select TZ_AREA_SET_CODE from PS_TZ_SITEI_ATYP_T where TZ_SITEI_ID=? AND TZ_AREA_TYPE_ID=?";
					String strAreaServiceImpl = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strAreaTypeId },
							"String");

					if (null != strAreaServiceImpl && !"".equals(strAreaServiceImpl)) {
						TzSiteActionServiceImpl areaServiceImpl = (TzSiteActionServiceImpl) ctx
								.getBean(strAreaServiceImpl);
						areaServiceImpl.tzSaveArea(mapActData, errMsg);
					}

				}

			}

			if (!"".equals(strSaveContent)) {

				boolean boolResult = false;

				String strHomePageCode = "";
				String strLoginPageCode = "";
				String strEnrollPageCode = "";

				String sql = "select 'Y' from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
				String recExists = sqlQuery.queryForObject(sql, new Object[] { strSiteId }, "String");

				if ("Y".equals(recExists)) {

					switch (strPagetype) {

					case "homepage":
						boolResult = this.saveHomepage(strSaveContent, strSiteId, errMsg);
						if (boolResult) {
							strLoginPageCode = this.handleLoginPage(strSiteId);
							if (strLoginPageCode != null && !"".equals(strLoginPageCode)) {
								boolResult = this.saveLoginpage(strLoginPageCode, strSiteId, errMsg);
								if (boolResult) {
									strEnrollPageCode = this.handleEnrollPage(strSiteId);
									if (strEnrollPageCode != null && !"".equals(strEnrollPageCode)) {
										boolResult = this.saveEnrollpage(strEnrollPageCode, strSiteId, errMsg);
										if (boolResult) {
											errMsg[0] = "0";
											errMsg[1] = "站点保存完成！";
											mapRet.put("success", true);
											strRet = jacksonUtil.Map2json(mapRet);
										} else {
											errMsg[0] = "1";
											errMsg[1] = "站点注册页保存失败！";
										}
									} else {
										errMsg[0] = "1";
										errMsg[1] = "站点注册页保存失败！";
									}
								} else {
									errMsg[0] = "1";
									errMsg[1] = "站点登录页保存失败！";
								}
							} else {
								errMsg[0] = "1";
								errMsg[1] = "站点登录页保存失败！";
							}
						} else {
							errMsg[0] = "1";
							errMsg[1] = "站点首页保存失败！";
						}

						break;

					case "loginpage":
						boolResult = this.saveLoginpage(strSaveContent, strSiteId, errMsg);
						if (boolResult) {
							strHomePageCode = this.handleHomePage(strSiteId);
							if (strHomePageCode != null && !"".equals(strHomePageCode)) {
								boolResult = this.saveHomepage(strHomePageCode, strSiteId, errMsg);
								if (boolResult) {
									strEnrollPageCode = this.handleEnrollPage(strSiteId);
									if (strEnrollPageCode != null && !"".equals(strEnrollPageCode)) {
										boolResult = this.saveEnrollpage(strEnrollPageCode, strSiteId, errMsg);
										if (boolResult) {
											errMsg[0] = "0";
											errMsg[1] = "站点保存完成！";
											mapRet.put("success", true);
											strRet = jacksonUtil.Map2json(mapRet);
										} else {
											errMsg[0] = "1";
											errMsg[1] = "站点注册页保存失败！";
										}
									} else {
										errMsg[0] = "1";
										errMsg[1] = "站点注册页保存失败！";
									}
								} else {
									errMsg[0] = "1";
									errMsg[1] = "站点首页保存失败！";
								}
							} else {
								errMsg[0] = "1";
								errMsg[1] = "站点首页保存失败！";
							}
						} else {
							errMsg[0] = "1";
							errMsg[1] = "站点登录页保存失败！";
						}

						break;

					case "enrollpage":

						strEnrollPageCode = this.handleEnrollPage(strSiteId);
						if (strEnrollPageCode != null && !"".equals(strEnrollPageCode)) {
							boolResult = this.saveEnrollpage(strEnrollPageCode, strSiteId, errMsg);
							if (boolResult) {
								strHomePageCode = this.handleHomePage(strSiteId);
								if (strHomePageCode != null && !"".equals(strHomePageCode)) {
									boolResult = this.saveHomepage(strHomePageCode, strSiteId, errMsg);
									if (boolResult) {
										strLoginPageCode = this.handleLoginPage(strSiteId);
										if (strLoginPageCode != null && !"".equals(strLoginPageCode)) {
											boolResult = this.saveLoginpage(strLoginPageCode, strSiteId, errMsg);
											if (boolResult) {
												errMsg[0] = "0";
												errMsg[1] = "站点保存完成！";
												mapRet.put("success", true);
												strRet = jacksonUtil.Map2json(mapRet);
											} else {
												errMsg[0] = "1";
												errMsg[1] = "站点登录页保存失败！";
											}
										} else {
											errMsg[0] = "1";
											errMsg[1] = "站点登录页保存失败！";
										}
									} else {
										errMsg[0] = "1";
										errMsg[1] = "站点首页保存失败！";
									}
								} else {
									errMsg[0] = "1";
									errMsg[1] = "站点首页保存失败！";
								}
							} else {
								errMsg[0] = "1";
								errMsg[1] = "站点注册页保存失败！";
							}
						} else {
							errMsg[0] = "1";
							errMsg[1] = "站点注册页保存失败！";
						}

						break;
					}

				} else {
					errMsg[0] = "1";
					errMsg[1] = "获取参数失败！";
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "保存失败！" + e.toString();
		}

		return strRet;

	}

	/**
	 * 发布首页、登录页、注册页
	 * 
	 * @param formData
	 * @param errMsg
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	private String releasePage(Map<String, Object> formData, String[] errMsg) {

		String strRet = "";

		Map<String, Object> mapRet = new HashMap<String, Object>();

		try {
			String strSiteIdSrc = formData.get("siteId") == null ? "" : String.valueOf(formData.get("siteId"));
			String strSiteId = "";
			if ("".equals(strSiteIdSrc)) {

				return strRet;
			}

			String strReleaseContent = formData.get("releasecontent") == null ? ""
					: String.valueOf(formData.get("releasecontent"));
			String strPagetype = formData.get("pagetype") == null ? "" : String.valueOf(formData.get("pagetype"));

			ArrayList<Map<String, Object>> listActData = (ArrayList<Map<String, Object>>) formData.get("dataArea");

			for (Map<String, Object> mapActData : listActData) {

				strSiteId = mapActData.get("siteId") == null ? "" : String.valueOf(mapActData.get("siteId"));
				if ("".equals(strSiteId)) {
					continue;
				}

				String strAreaId = mapActData.get("areaId") == null ? "" : String.valueOf(mapActData.get("areaId"));
				//String strAreaZone = mapActData.get("areaZone") == null ? "": String.valueOf(mapActData.get("areaZone"));
				String strAreaType = mapActData.get("areaType") == null ? ""
						: String.valueOf(mapActData.get("areaType"));

				String sql = "select TZ_JG_ID from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
				//String strOrgId = sqlQuery.queryForObject(sql, new Object[] { strSiteId }, "String");

				String strAreaTypeId = "";
				if (!"".equals(strAreaId)) {
					sql = "select TZ_AREA_TYPE_ID from PS_TZ_SITEI_AREA_T where TZ_SITEI_ID=? and TZ_AREA_ID=? and TZ_AREA_STATE='Y'";
					strAreaTypeId = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strAreaId }, "String");
				} else {
					sql = "select TZ_AREA_TYPE_ID from PS_TZ_SITEI_ATYP_T where TZ_SITEI_ID=? and TZ_AREA_TYPE=? and TZ_AREA_TYPE_STATE='Y'";
					strAreaTypeId = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strAreaType }, "String");
				}

				if (strAreaTypeId != null && !"".equals(strAreaTypeId)) {
					sql = "select TZ_AREA_SET_CODE from PS_TZ_SITEI_ATYP_T where TZ_SITEI_ID=? and TZ_AREA_TYPE_ID=?";
					String strAreaServiceImpl = sqlQuery.queryForObject(sql, new Object[] { strSiteId, strAreaTypeId },
							"String");

					if (null != strAreaServiceImpl && !"".equals(strAreaServiceImpl)) {
						TzSiteActionServiceImpl areaServiceImpl = (TzSiteActionServiceImpl) ctx
								.getBean(strAreaServiceImpl);
						areaServiceImpl.tzReleaseArea(mapActData, errMsg);
					}

				}

			}

			if (!"".equals(strReleaseContent)) {

				boolean boolResult = false;

				String strHomePageCode = "";
				String strLoginPageCode = "";
				String strEnrollPageCode = "";

				String sql = "select 'Y' from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
				String recExists = sqlQuery.queryForObject(sql, new Object[] { strSiteId }, "String");

				if ("Y".equals(recExists)) {

					switch (strPagetype) {

					case "homepage":
						boolResult = this.releasHomepage(strReleaseContent, strSiteId, errMsg);
						if (boolResult) {
							strLoginPageCode = this.handleLoginPage(strSiteId);
							if (strLoginPageCode != null && !"".equals(strLoginPageCode)) {
								boolResult = this.releasLoginpage(strLoginPageCode, strSiteId, errMsg);
								if (boolResult) {
									strEnrollPageCode = this.handleEnrollPage(strSiteId);
									if (strEnrollPageCode != null && !"".equals(strEnrollPageCode)) {
										boolResult = this.releasEnrollpage(strEnrollPageCode, strSiteId, errMsg);
										if (boolResult) {
											errMsg[0] = "0";
											errMsg[1] = "站点发布完成！";
											mapRet.put("success", true);
											strRet = jacksonUtil.Map2json(mapRet);
										} else {
											errMsg[0] = "1";
											errMsg[1] = "站点注册页发布失败！";
										}
									} else {
										errMsg[0] = "1";
										errMsg[1] = "站点注册页发布失败！";
									}
								} else {
									errMsg[0] = "1";
									errMsg[1] = "站点登录页发布失败！";
								}
							} else {
								errMsg[0] = "1";
								errMsg[1] = "站点登录页发布失败！";
							}
						} else {
							errMsg[0] = "1";
							errMsg[1] = "站点首页发布失败！";
						}

						break;

					case "loginpage":
						boolResult = this.releasLoginpage(strReleaseContent, strSiteId, errMsg);
						if (boolResult) {
							strHomePageCode = this.handleHomePage(strSiteId);
							if (strHomePageCode != null && !"".equals(strHomePageCode)) {
								boolResult = this.releasHomepage(strHomePageCode, strSiteId, errMsg);
								if (boolResult) {
									strEnrollPageCode = this.handleEnrollPage(strSiteId);
									if (strEnrollPageCode != null && !"".equals(strEnrollPageCode)) {
										boolResult = this.releasEnrollpage(strEnrollPageCode, strSiteId, errMsg);
										if (boolResult) {
											errMsg[0] = "0";
											errMsg[1] = "站点发布完成！";
											mapRet.put("success", true);
											strRet = jacksonUtil.Map2json(mapRet);
										} else {
											errMsg[0] = "1";
											errMsg[1] = "站点注册页发布失败！";
										}
									} else {
										errMsg[0] = "1";
										errMsg[1] = "站点注册页发布失败！";
									}
								} else {
									errMsg[0] = "1";
									errMsg[1] = "站点首页发布失败！";
								}
							} else {
								errMsg[0] = "1";
								errMsg[1] = "站点首页发布失败！";
							}
						} else {
							errMsg[0] = "1";
							errMsg[1] = "站点登录页发布失败！";
						}

						break;

					case "enrollpage":

						strEnrollPageCode = this.handleEnrollPage(strSiteId);
						if (strEnrollPageCode != null && !"".equals(strEnrollPageCode)) {
							boolResult = this.releasEnrollpage(strEnrollPageCode, strSiteId, errMsg);
							if (boolResult) {
								strHomePageCode = this.handleHomePage(strSiteId);
								if (strHomePageCode != null && !"".equals(strHomePageCode)) {
									boolResult = this.releasHomepage(strHomePageCode, strSiteId, errMsg);
									if (boolResult) {
										strLoginPageCode = this.handleLoginPage(strSiteId);
										if (strLoginPageCode != null && !"".equals(strLoginPageCode)) {
											boolResult = this.releasLoginpage(strLoginPageCode, strSiteId, errMsg);
											if (boolResult) {
												errMsg[0] = "0";
												errMsg[1] = "站点发布完成！";
												mapRet.put("success", true);
												strRet = jacksonUtil.Map2json(mapRet);
											} else {
												errMsg[0] = "1";
												errMsg[1] = "站点登录页发布失败！";
											}
										} else {
											errMsg[0] = "1";
											errMsg[1] = "站点登录页发布失败！";
										}
									} else {
										errMsg[0] = "1";
										errMsg[1] = "站点首页发布失败！";
									}
								} else {
									errMsg[0] = "1";
									errMsg[1] = "站点首页发布失败！";
								}
							} else {
								errMsg[0] = "1";
								errMsg[1] = "站点注册页发布失败！";
							}
						} else {
							errMsg[0] = "1";
							errMsg[1] = "站点注册页发布失败！";
						}

						break;
					}

				} else {
					errMsg[0] = "1";
					errMsg[1] = "获取参数失败！";
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "发布失败！" + e.toString();
		}

		return strRet;
	}

	/**
	 * 首页保存程序
	 * 
	 * @param strSaveContent
	 * @param strSiteId
	 * @param errMsg
	 * @return boolean
	 */
	@Transactional
	private boolean saveHomepage(String strSaveContent, String strSiteId, String[] errMsg) {

		boolean boolRet = false;

		try {
			String sql = "select TZ_JG_ID,TZ_SITE_LANG from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
			Map<String, Object> mapSiteiData = sqlQuery.queryForMap(sql, new Object[] { strSiteId });
			if (null != mapSiteiData) {

				String orgid = mapSiteiData.get("TZ_JG_ID") == null ? ""
						: String.valueOf(mapSiteiData.get("TZ_JG_ID")).toUpperCase();
				String siteLang = mapSiteiData.get("TZ_SITE_LANG") == null ? ""
						: String.valueOf(mapSiteiData.get("TZ_SITE_LANG")).toUpperCase();

				PsTzSiteiDefnTWithBLOBs psTzSiteiDefnTWithBLOBs = new PsTzSiteiDefnTWithBLOBs();

				String strContent = tzGDObject.getHTMLText("HTML.TZSitePageBundle.SiteSaveTpl", false, strSaveContent);
				
				String strSavedContent = strContent;
				strSavedContent = siteRepCssServiceImpl.repWelcome(strSavedContent, "");
				strSavedContent = siteRepCssServiceImpl.repSiteid(strSavedContent, strSiteId);
				strSavedContent = siteRepCssServiceImpl.repJgid(strSavedContent, orgid);
				strSavedContent = siteRepCssServiceImpl.repLang(strSavedContent, siteLang);
				psTzSiteiDefnTWithBLOBs.setTzIndexSavecode(strSavedContent);

				String strPreviewContent = strContent;
				strPreviewContent = siteRepCssServiceImpl.repWelcome(strPreviewContent, "");
				strPreviewContent = siteRepCssServiceImpl.repSdkbar(strPreviewContent, "");
				strPreviewContent = siteRepCssServiceImpl.repSiteid(strPreviewContent, strSiteId);
				strPreviewContent = siteRepCssServiceImpl.repJgid(strPreviewContent, orgid);
				strPreviewContent = siteRepCssServiceImpl.repLang(strPreviewContent, siteLang);
				psTzSiteiDefnTWithBLOBs.setTzIndexPrecode(strPreviewContent);

				psTzSiteiDefnTWithBLOBs.setTzSiteiId(strSiteId);
				psTzSiteiDefnTMapper.updateByPrimaryKeySelective(psTzSiteiDefnTWithBLOBs);

				boolRet = true;
			} else {
				errMsg[0] = "1";
				errMsg[1] = "站点不存在！";
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "站点首页保存异常！";
		}

		return boolRet;

	}

	/**
	 * 登录页保存程序
	 * 
	 * @param strSaveContent
	 * @param strSiteId
	 * @param errMsg
	 * @return boolean
	 */
	@Transactional
	private boolean saveLoginpage(String strSaveContent, String strSiteId, String[] errMsg) {

		boolean boolRet = false;

		try {
			String sql = "select TZ_JG_ID,TZ_SITE_LANG from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
			Map<String, Object> mapSiteiData = sqlQuery.queryForMap(sql, new Object[] { strSiteId });
			if (null != mapSiteiData) {

				String orgid = mapSiteiData.get("TZ_JG_ID") == null ? ""
						: String.valueOf(mapSiteiData.get("TZ_JG_ID")).toUpperCase();
				String siteLang = mapSiteiData.get("TZ_SITE_LANG") == null ? ""
						: String.valueOf(mapSiteiData.get("TZ_SITE_LANG")).toUpperCase();

				PsTzSiteiDefnTWithBLOBs psTzSiteiDefnTWithBLOBs = new PsTzSiteiDefnTWithBLOBs();

				String strSavedContent = tzGDObject.getHTMLText("HTML.TZSitePageBundle.SiteSaveTpl", false, strSaveContent);
				strSavedContent = siteRepCssServiceImpl.repWelcome(strSavedContent, "");
				strSavedContent = siteRepCssServiceImpl.repSiteid(strSavedContent, strSiteId);
				strSavedContent = siteRepCssServiceImpl.repJgid(strSavedContent, orgid);
				strSavedContent = siteRepCssServiceImpl.repLang(strSavedContent, siteLang);
				psTzSiteiDefnTWithBLOBs.setTzLonginSavecode(strSavedContent);

				String strPreviewContent = tzGDObject.getHTMLText("HTML.TZSitePageBundle.SiteReleaseTpl", false, strSaveContent);
				strPreviewContent = siteRepCssServiceImpl.repWelcome(strPreviewContent, "");
				strPreviewContent = siteRepCssServiceImpl.repSdkbar(strPreviewContent, "");
				strPreviewContent = siteRepCssServiceImpl.repSiteid(strPreviewContent, strSiteId);
				strPreviewContent = siteRepCssServiceImpl.repJgid(strPreviewContent, orgid);
				strPreviewContent = siteRepCssServiceImpl.repLang(strPreviewContent, siteLang);
				psTzSiteiDefnTWithBLOBs.setTzLoginPrecode(strPreviewContent);

				psTzSiteiDefnTWithBLOBs.setTzSiteiId(strSiteId);
				psTzSiteiDefnTMapper.updateByPrimaryKeySelective(psTzSiteiDefnTWithBLOBs);

				boolRet = true;
			} else {
				errMsg[0] = "1";
				errMsg[1] = "站点不存在！";
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "站点登录页保存异常！";
		}

		return boolRet;

	}

	/**
	 * 注册页保存程序
	 * 
	 * @param strSaveContent
	 * @param strSiteId
	 * @param errMsg
	 * @return boolean
	 */
	@Transactional
	private boolean saveEnrollpage(String strSaveContent, String strSiteId, String[] errMsg) {

		boolean boolRet = false;

		try {
			String sql = "select TZ_JG_ID,TZ_SITE_LANG from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
			Map<String, Object> mapSiteiData = sqlQuery.queryForMap(sql, new Object[] { strSiteId });
			if (null != mapSiteiData) {

				String orgid = mapSiteiData.get("TZ_JG_ID") == null ? ""
						: String.valueOf(mapSiteiData.get("TZ_JG_ID")).toUpperCase();
				String siteLang = mapSiteiData.get("TZ_SITE_LANG") == null ? ""
						: String.valueOf(mapSiteiData.get("TZ_SITE_LANG")).toUpperCase();

				PsTzSiteiDefnTWithBLOBs psTzSiteiDefnTWithBLOBs = new PsTzSiteiDefnTWithBLOBs();

				String strSavedContent = tzGDObject.getHTMLText("HTML.TZSitePageBundle.SiteSaveTpl", false, strSaveContent);
				strSavedContent = siteRepCssServiceImpl.repWelcome(strSavedContent, "");
				strSavedContent = siteRepCssServiceImpl.repSiteid(strSavedContent, strSiteId);
				strSavedContent = siteRepCssServiceImpl.repJgid(strSavedContent, orgid);
				strSavedContent = siteRepCssServiceImpl.repLang(strSavedContent, siteLang);
				psTzSiteiDefnTWithBLOBs.setTzEnrollSavecode(strSavedContent);

				String strPreviewContent = tzGDObject.getHTMLText("HTML.TZSitePageBundle.SiteReleaseTpl", false, strSaveContent);
				strPreviewContent = siteRepCssServiceImpl.repWelcome(strPreviewContent, "");
				strPreviewContent = siteRepCssServiceImpl.repSdkbar(strPreviewContent, "");
				strPreviewContent = siteRepCssServiceImpl.repSiteid(strPreviewContent, strSiteId);
				strPreviewContent = siteRepCssServiceImpl.repJgid(strPreviewContent, orgid);
				strPreviewContent = siteRepCssServiceImpl.repLang(strPreviewContent, siteLang);
				psTzSiteiDefnTWithBLOBs.setTzEnrollPrecode(strPreviewContent);

				psTzSiteiDefnTWithBLOBs.setTzSiteiId(strSiteId);
				psTzSiteiDefnTMapper.updateByPrimaryKeySelective(psTzSiteiDefnTWithBLOBs);

				boolRet = true;
			} else {
				errMsg[0] = "1";
				errMsg[1] = "站点不存在！";
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "站点注册页保存异常！";
		}

		return boolRet;

	}

	/**
	 * 发布站点首页
	 * 
	 * @param strReleaseContent
	 * @param strSiteId
	 * @param errMsg
	 * @return boolean
	 */
	@Transactional
	private boolean releasHomepage(String strReleaseContent, String strSiteId, String[] errMsg) {

		boolean boolRet = false;

		try {
			String sql = "select TZ_JG_ID,TZ_SITE_LANG from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
			Map<String, Object> mapSiteiData = sqlQuery.queryForMap(sql, new Object[] { strSiteId });
			if (null != mapSiteiData) {

				String orgid = mapSiteiData.get("TZ_JG_ID") == null ? ""
						: String.valueOf(mapSiteiData.get("TZ_JG_ID")).toUpperCase();
				String siteLang = mapSiteiData.get("TZ_SITE_LANG") == null ? ""
						: String.valueOf(mapSiteiData.get("TZ_SITE_LANG")).toUpperCase();

				PsTzSiteiDefnTWithBLOBs psTzSiteiDefnTWithBLOBs = new PsTzSiteiDefnTWithBLOBs();

				String strReleasedContent = tzGDObject.getHTMLText("HTML.TZSitePageBundle.SiteReleaseTpl", false, strReleaseContent);
				strReleasedContent = siteRepCssServiceImpl.repTitle(strReleasedContent, strSiteId);
				strReleasedContent = siteRepCssServiceImpl.repCss(strReleasedContent, strSiteId);
				strReleasedContent = siteRepCssServiceImpl.repWelcome(strReleasedContent, "");
				strReleasedContent = siteRepCssServiceImpl.repSdkbar(strReleasedContent, "");
				strReleasedContent = siteRepCssServiceImpl.repSiteid(strReleasedContent, strSiteId);
				strReleasedContent = siteRepCssServiceImpl.repJgid(strReleasedContent, orgid);
				strReleasedContent = siteRepCssServiceImpl.repLang(strReleasedContent, siteLang);
				psTzSiteiDefnTWithBLOBs.setTzIndexPubcode(strReleasedContent);
				psTzSiteiDefnTWithBLOBs.setTzIndexPrecode(strReleasedContent);

				psTzSiteiDefnTWithBLOBs.setTzSiteiId(strSiteId);
				psTzSiteiDefnTMapper.updateByPrimaryKeySelective(psTzSiteiDefnTWithBLOBs);

				boolRet = true;
			} else {
				errMsg[0] = "1";
				errMsg[1] = "站点不存在！";
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "站点首页发布异常！";
		}

		return boolRet;

	}

	/**
	 * 发布站点登录页
	 * 
	 * @param strReleaseContent
	 * @param strSiteId
	 * @param errMsg
	 * @return boolean
	 */
	@Transactional
	private boolean releasLoginpage(String strReleaseContent, String strSiteId, String[] errMsg) {

		boolean boolRet = false;

		try {
			String sql = "select TZ_JG_ID,TZ_SITE_LANG from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
			Map<String, Object> mapSiteiData = sqlQuery.queryForMap(sql, new Object[] { strSiteId });
			if (null != mapSiteiData) {

				String orgid = mapSiteiData.get("TZ_JG_ID") == null ? ""
						: String.valueOf(mapSiteiData.get("TZ_JG_ID")).toUpperCase();
				String siteLang = mapSiteiData.get("TZ_SITE_LANG") == null ? ""
						: String.valueOf(mapSiteiData.get("TZ_SITE_LANG")).toUpperCase();

				PsTzSiteiDefnTWithBLOBs psTzSiteiDefnTWithBLOBs = new PsTzSiteiDefnTWithBLOBs();

				String strReleasedContent = tzGDObject.getHTMLText("HTML.TZSitePageBundle.SiteReleaseTpl", false, strReleaseContent);
				strReleasedContent = siteRepCssServiceImpl.repTitle(strReleasedContent, strSiteId);
				strReleasedContent = siteRepCssServiceImpl.repCss(strReleasedContent, strSiteId);
				strReleasedContent = siteRepCssServiceImpl.repWelcome(strReleasedContent, "");
				strReleasedContent = siteRepCssServiceImpl.repSdkbar(strReleasedContent, "");
				strReleasedContent = siteRepCssServiceImpl.repSiteid(strReleasedContent, strSiteId);
				strReleasedContent = siteRepCssServiceImpl.repJgid(strReleasedContent, orgid);
				strReleasedContent = siteRepCssServiceImpl.repLang(strReleasedContent, siteLang);
				psTzSiteiDefnTWithBLOBs.setTzLonginPubcode(strReleasedContent);
				psTzSiteiDefnTWithBLOBs.setTzLoginPrecode(strReleasedContent);

				psTzSiteiDefnTWithBLOBs.setTzSiteiId(strSiteId);
				psTzSiteiDefnTMapper.updateByPrimaryKeySelective(psTzSiteiDefnTWithBLOBs);

				boolRet = true;
			} else {
				errMsg[0] = "1";
				errMsg[1] = "站点不存在！";
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "站点登录页发布异常！";
		}

		return boolRet;

	}

	/**
	 * 发布站点注册页
	 * 
	 * @param strReleaseContent
	 * @param strSiteId
	 * @param errMsg
	 * @return boolean
	 */
	@Transactional
	private boolean releasEnrollpage(String strReleaseContent, String strSiteId, String[] errMsg) {

		boolean boolRet = false;

		try {
			String sql = "select TZ_JG_ID,TZ_SITE_LANG from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
			Map<String, Object> mapSiteiData = sqlQuery.queryForMap(sql, new Object[] { strSiteId });
			if (null != mapSiteiData) {

				String orgid = mapSiteiData.get("TZ_JG_ID") == null ? ""
						: String.valueOf(mapSiteiData.get("TZ_JG_ID")).toUpperCase();
				String siteLang = mapSiteiData.get("TZ_SITE_LANG") == null ? ""
						: String.valueOf(mapSiteiData.get("TZ_SITE_LANG")).toUpperCase();

				PsTzSiteiDefnTWithBLOBs psTzSiteiDefnTWithBLOBs = new PsTzSiteiDefnTWithBLOBs();

				String strReleasedContent = tzGDObject.getHTMLText("HTML.TZSitePageBundle.SiteReleaseTpl", false, strReleaseContent);
				strReleasedContent = siteRepCssServiceImpl.repTitle(strReleasedContent, strSiteId);
				strReleasedContent = siteRepCssServiceImpl.repCss(strReleasedContent, strSiteId);
				strReleasedContent = siteRepCssServiceImpl.repWelcome(strReleasedContent, "");
				strReleasedContent = siteRepCssServiceImpl.repSdkbar(strReleasedContent, "");
				strReleasedContent = siteRepCssServiceImpl.repSiteid(strReleasedContent, strSiteId);
				strReleasedContent = siteRepCssServiceImpl.repJgid(strReleasedContent, orgid);
				strReleasedContent = siteRepCssServiceImpl.repLang(strReleasedContent, siteLang);
				psTzSiteiDefnTWithBLOBs.setTzEnrollPubcode(strReleasedContent);
				psTzSiteiDefnTWithBLOBs.setTzEnrollPrecode(strReleasedContent);

				psTzSiteiDefnTWithBLOBs.setTzSiteiId(strSiteId);
				psTzSiteiDefnTMapper.updateByPrimaryKeySelective(psTzSiteiDefnTWithBLOBs);

				/* 注册页面静态化 */
				boolRet = this.staticFile(strReleasedContent, orgid, strSiteId, "enroll.html", errMsg);

			} else {
				errMsg[0] = "1";
				errMsg[1] = "站点不存在！";
			}

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "站点注册页发布异常！";
		}

		return boolRet;

	}

	/**
	 * 生成站点静态页面
	 * 
	 * @param strReleaseContent
	 * @param orgid
	 * @param strSiteId
	 * @param filename
	 * @param errMsg
	 * @return
	 */
	private boolean staticFile(String strReleaseContent, String orgid, String strSiteId, String filename,
			String[] errMsg) {

		boolean boolRet = false;

		try {

			String parentPath = getSysHardCodeVal.getWebsiteEnrollPath() + "/" + orgid.toLowerCase() + "/"
					+ strSiteId.toLowerCase();

			byte[] fileBytes = strReleaseContent.getBytes();

			boolRet = fileManageServiceImpl.CreateFile(parentPath, filename, fileBytes);

		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "3";
			errMsg[1] = "静态化文件时异常！";
		}

		return boolRet;

	}

	/**
	 * 获取首页主体内容
	 * 
	 * @param strSiteId
	 * @return String
	 * @throws Exception
	 */
	private String handleHomePage(String strSiteId) throws Exception {

		String strRet = "";

		try {

			String sql = "select TZ_INDEX_SAVECODE from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
			String strContent = sqlQuery.queryForObject(sql, new Object[] { strSiteId }, "String");

			if (null != strContent && !"".equals(strContent)) {

				Pattern p = Pattern.compile("<body>(.*)</body>");
				Matcher m = p.matcher(strContent);
				while (m.find()) {
					System.out.println(m.group(1));
					strRet = m.group(1);
					break;
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}

		return strRet;

	}

	/**
	 * 处理要发布的登录页内容
	 * 
	 * @param strSiteId
	 * @return String
	 * @throws Exception
	 */
	private String handleLoginPage(String strSiteId) throws Exception {

		String strRet = "";

		try {

			String sql = "select TZ_LONGIN_SAVECODE from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
			String strContent = sqlQuery.queryForObject(sql, new Object[] { strSiteId }, "String");

			if (null != strContent && !"".equals(strContent)) {

				Pattern p = Pattern.compile("<body>(.*)</body>");
				Matcher m = p.matcher(strContent);
				while (m.find()) {
					System.out.println(m.group(1));
					strRet = m.group(1);
					break;
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}

		return strRet;

	}

	/**
	 * 处理要发布的注册页内容
	 * 
	 * @param strSiteId
	 * @return String
	 * @throws Exception
	 */
	private String handleEnrollPage(String strSiteId) throws Exception {

		String strRet = "";

		try {

			String sql = "select TZ_JG_ID from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
			String strOrgId = sqlQuery.queryForObject(sql, new Object[] { strSiteId }, "String");

			String strContent = "";
			// Local TZ_GD_USERMG_PKG:TZ_GD_USER_REG &object = create
			// TZ_GD_USERMG_PKG:TZ_GD_USER_REG();

			// String strContent = &object.userRegister(strOrgId, strSiteId);

			// strContent = GetHTMLText(HTML.TZ_SETREGISTEPAGE_HTML,
			// strContent);

			if (null != strContent && !"".equals(strContent)) {

				Pattern p = Pattern.compile("<body>(.*)</body>");
				Matcher m = p.matcher(strContent);
				while (m.find()) {
					System.out.println(m.group(1));
					strRet = m.group(1);
					break;
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}

		return strRet;

	}

}
