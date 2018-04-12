package com.tranzvision.gd.TZWebSiteRegisteBundle.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiDefnTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiDefnT;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.ValidateUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzWebsiteLoginServiceImpl;

/**
 * 
 * @author yuds复制原有UserManagementServiceImpl
 */
@Service("com.tranzvision.gd.TZWebSiteRegisteBundle.service.impl.SemPhUserManagementServiceImpl")
public class SemPhUserManagementServiceImpl extends FrameworkImpl { 
	@Autowired
	private TZGDObject tzGdObject;
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private SiteRepCssServiceImpl objRep;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private PsTzSiteiDefnTMapper psTzSiteiDefnTMapper;		
	@Autowired
	private ValidateUtil validateUtil;
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	@Autowired
	private GetHardCodePoint GetHardCodePoint;
	@Autowired
	private TzWebsiteLoginServiceImpl tzWebsiteLoginServiceImpl;
	
	@Override
	public String tzGetHtmlContent(String strParams) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			
			String strSiteId = request.getParameter("siteId");
			String strResultConten = "";
			
			String contextPath = request.getContextPath();
			String commonUrl = contextPath + "/dispatcher";
			
			String language = "", jgId = "";
			String siteSQL = "SELECT TZ_JG_ID,TZ_SITE_LANG,TZ_SKIN_ID FROM PS_TZ_SITEI_DEFN_T WHERE TZ_SITEI_ID=? AND TZ_SITEI_ENABLE='Y'";
			Map<String, Object> siteMap = jdbcTemplate.queryForMap(siteSQL, new Object[] { strSiteId });
			if (siteMap != null) {
				language = (String) siteMap.get("TZ_SITE_LANG");
				jgId = (String) siteMap.get("TZ_JG_ID");
				if (language == null || "".equals(language)) {
					language = "ZHS";
				}
				if (jgId == null || "".equals(jgId)) {
					jgId = "ADMIN";
				}
			}
			String JGID = sqlQuery.queryForObject("select TZ_JG_ID from PS_TZ_SITEI_DEFN_T WHERE TZ_SITEI_ID=?",new Object[]{strSiteId},"String");
			
			if (JGID.equals("SEM")) {
				JGID="";
			} else {
				JGID.toLowerCase();
			}
			
			System.out.println(this.getClass().getName()+":"+JGID);
			String strHeadHtml = tzGdObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_MOBILE_ZHGL_HEAD_HTML",contextPath,commonUrl,jgId,strSiteId,language,JGID);
			String indexUrl = commonUrl + "?classid=mIndex&siteId=" + strSiteId;
			//String strMainHtml = tzGdObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_MOBILE_ZHGL_HTML",contextPath,indexUrl);
			String strMainHtml = this.userPhoneInformation(strSiteId);

			String strOrgId = "";
			String strLang = "";

			PsTzSiteiDefnT psTzSiteiDefnT = psTzSiteiDefnTMapper.selectByPrimaryKey(strSiteId);
			if (psTzSiteiDefnT != null) {
				strOrgId = psTzSiteiDefnT.getTzJgId();
				strLang = psTzSiteiDefnT.getTzSiteLang();
			} else {
				return "站点不存在";
			}

			try {		
				strResultConten = tzGdObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_MOBILE_BASE_HTML", "",contextPath, strHeadHtml,strSiteId,"5", strMainHtml,JGID);
			} catch (TzSystemException e) {
				e.printStackTrace();
				return "【TZ_MOBILE_BASE_HTML】html对象未定义";
			}
			
			
			strResultConten = objRep.repPhoneCss(strResultConten, strSiteId);
			
			strResultConten = objRep.repTitle(strResultConten, strSiteId);
			strResultConten = objRep.repCss(strResultConten, strSiteId);

			strResultConten = objRep.repSiteid(strResultConten, strSiteId);
			strResultConten = objRep.repJgid(strResultConten, strOrgId);
			strResultConten = objRep.repLang(strResultConten, strLang);

			return strResultConten;
			
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	// 个人信息管理;
		public String userPhoneInformation(String siteId) {
			try {
				String language = "", jgId = "", skinId = "";
				String siteSQL = "SELECT TZ_JG_ID,TZ_SITE_LANG,TZ_SKIN_ID FROM PS_TZ_SITEI_DEFN_T WHERE TZ_SITEI_ID=? AND TZ_SITEI_ENABLE='Y'";
				Map<String, Object> siteMap = jdbcTemplate.queryForMap(siteSQL, new Object[] { siteId });
				if (siteMap != null) {
					language = (String) siteMap.get("TZ_SITE_LANG");
					jgId = (String) siteMap.get("TZ_JG_ID");
					skinId = (String) siteMap.get("TZ_SKIN_ID");
					if (language == null || "".equals(language)) {
						language = "ZHS";
					}
					if (jgId == null || "".equals(jgId)) {
						jgId = "ADMIN";
					}

					/* 账号管理页面文字双语化 */
					String strColuTitle = validateUtil.getMessageTextWithLanguageCd(jgId, language, "TZ_SITE_MESSAGE", "1",
							"账号管理", "Account Management");
					String strTab1 = validateUtil.getMessageTextWithLanguageCd(jgId, language, "TZ_SITE_MESSAGE", "2",
							"个人信息", "Personal Information");
					String strTab2 = validateUtil.getMessageTextWithLanguageCd(jgId, language, "TZ_SITE_MESSAGE", "3",
							"修改密码", "Change Password");
					String strTab3 = validateUtil.getMessageTextWithLanguageCd(jgId, language, "TZ_SITE_MESSAGE", "4",
							"通知设置", "Notification Setting");
					String strTab4 = validateUtil.getMessageTextWithLanguageCd(jgId, language, "TZ_SITE_MESSAGE", "5",
							"账号绑定", "Account Binding");
					String strPhoto = validateUtil.getMessageTextWithLanguageCd(jgId, language, "TZ_SITE_MESSAGE", "6",
							"上传照片", "Upload");
					String strSaveBtn = validateUtil.getMessageTextWithLanguageCd(jgId, language, "TZ_SITE_MESSAGE", "7",
							"保存", "Save");

					String strSMSRemind = validateUtil.getMessageTextWithLanguageCd(jgId, language, "TZ_SITE_MESSAGE", "12",
							"接受短信提醒", "SMS Remind");
					String strEmailRemind = validateUtil.getMessageTextWithLanguageCd(jgId, language, "TZ_SITE_MESSAGE",
							"11", "接受邮件提醒", "Email Remind");

					String strTurnOn = validateUtil.getMessageTextWithLanguageCd(jgId, language, "TZ_SITE_MESSAGE", "13",
							"好的", "ON");
					String strTurnOff = validateUtil.getMessageTextWithLanguageCd(jgId, language, "TZ_SITE_MESSAGE", "14",
							"不，谢谢", "OFF");

					String strBind = validateUtil.getMessageTextWithLanguageCd(jgId, language, "TZ_SITE_MESSAGE", "15",
							"绑定", "Bind");
					String strRelease = validateUtil.getMessageTextWithLanguageCd(jgId, language, "TZ_SITE_MESSAGE", "16",
							"解除", "Release");

					String strChange = validateUtil.getMessageTextWithLanguageCd(jgId, language, "TZ_SITE_MESSAGE", "17",
							"变更", "Change");

					String strAbsence = validateUtil.getMessageTextWithLanguageCd(jgId, language, "TZ_SITE_MESSAGE", "18",
							"无", "Absence");

					String strPhone = validateUtil.getMessageTextWithLanguageCd(jgId, language, "TZ_SITE_MESSAGE", "19",
							"手机", "Phone");

					String strEmail = validateUtil.getMessageTextWithLanguageCd(jgId, language, "TZ_SITE_MESSAGE", "20",
							"邮箱", "Email");

					String strWeChat = validateUtil.getMessageTextWithLanguageCd(jgId, language, "TZ_SITE_MESSAGE", "221",
							"微信", "WeChat");

					String strWeChatBind = validateUtil.getMessageTextWithLanguageCd(jgId, language, "TZ_SITE_MESSAGE",
							"222", "扫二维码，绑定微信账号", "Scan the QR code,bind your wechat account.");

					String strPassSucTips = validateUtil.getMessageTextWithLanguageCd(jgId, language, "TZ_SITE_MESSAGE",
							"29", "修改成功", "The modification is successful");

					// 通用链接;
					String contextPath = request.getContextPath();
					String commonUrl = contextPath + "/dispatcher";
					String imgPath = getSysHardCodeVal.getWebsiteSkinsImgPath();
					imgPath = contextPath + imgPath + "/" + skinId;

					// 修改密码;
					String updpassword = this.userPhoneFixPasswordHTML(imgPath, jgId, language);

					// 通知设置;
					String msgmail_html = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_MM_HTML",
							strEmailRemind, strSMSRemind, strTurnOn, strTurnOff, strSaveBtn);
					// 保存修改;
					String saveActivate_url = commonUrl;
					// 国家选择器链接;
					String countryUrl = commonUrl
							+ "?tzParams={\"ComID\":\"TZ_COMMON_COM\",\"PageID\":\"TZ_COUNTRY_STD\",\"OperateType\":\"HTML\",\"comParams\":{\"siteId\":\""
							+ siteId + "\"}}";
					// 上传并处理照片;
					String phoUrl = commonUrl
							+ "?tzParams={\"ComID\":\"TZ_GD_ZS_USERMNG\",\"PageID\":\"TZ_UP_PHOTO_STD\",\"OperateType\":\"HTML\",\"comParams\":{\"siteId\":\""
							+ siteId + "\"}}";
					// 修改手机;
					String str_mobile = commonUrl
							+ "?tzParams={\"ComID\":\"TZ_GD_ZS_USERMNG\",\"PageID\":\"TZ_CHANGE_MOBILE\",\"OperateType\":\"HTML\",\"comParams\":{\"siteId\":\""
							+ siteId + "\"}}";
					// 获取基本信息;
					String str_userInfo = commonUrl
							+ "?tzParams={\"ComID\":\"TZ_GD_ZS_USERMNG\",\"PageID\":\"TZ_ZS_USERMNG_STD\",\"OperateType\":\"USERINFO\",\"comParams\":{\"siteId\":\""
							+ siteId + "\"}}";
					// 保存提醒设置;
					String SaveRemind = commonUrl;

					String oprid = tzWebsiteLoginServiceImpl.getLoginedUserOprid(request);
					// 是否绑定了手机;
					String strBindPhone = "";
					String strBindPhoneFlg = "";
					String strBindPhoneEvent = "";
					String strChangePhoneShow = "";
					String bdMobileSQL = "select TZ_MOBILE from PS_TZ_AQ_YHXX_TBL where TZ_JG_ID=? and OPRID=? and TZ_RYLX='ZCYH' and TZ_SJBD_BZ='Y'";
					strBindPhone = jdbcTemplate.queryForObject(bdMobileSQL, new Object[] { jgId, oprid }, "String");

					// 是否绑定了邮箱;
					String strBindEmail = "";
					String strBindEmailFlg = "";
					String strBindEmailEvent = "";
					String strChangeEmailShow = "";
					String bdEmailSQL = "select TZ_EMAIL from PS_TZ_AQ_YHXX_TBL where TZ_JG_ID=? and OPRID=? and TZ_RYLX='ZCYH' and TZ_YXBD_BZ='Y'";
					strBindEmail = jdbcTemplate.queryForObject(bdEmailSQL, new Object[] { jgId, oprid }, "String");

					if (strBindPhone != null && !"".equals(strBindPhone)) {
						strBindPhoneFlg = strRelease;
						strBindPhoneEvent = "Y";
						strChangePhoneShow = "";
					} else {
						strBindPhone = strAbsence;
						strBindPhoneFlg = strBind;
						strBindPhoneEvent = "N";
						strChangePhoneShow = "none";
					}

					if (strBindEmail != null && !"".equals(strBindEmail)) {
						strBindEmailFlg = strRelease;
						strBindEmailEvent = "Y";
						strChangeEmailShow = "";
					} else {
						strBindEmail = strAbsence;
						strBindEmailFlg = strBind;
						strBindEmailEvent = "N";
						strChangeEmailShow = "none";
					}

					// 账号绑定;
					// 根据siteid得到机构id,根据机构id得到注册项是否可以绑定手机或邮箱;
					String TZ_ACTIVATE_TYPE = "";
					String activateTypeSQL = "select TZ_ACTIVATE_TYPE from PS_TZ_USERREG_MB_T a,PS_TZ_SITEI_DEFN_T b where a.TZ_SITEI_ID=b.TZ_SITEI_ID and b.TZ_SITEI_ID=?";
					TZ_ACTIVATE_TYPE = jdbcTemplate.queryForObject(activateTypeSQL, new Object[] { siteId }, "String");
					// 是否要显示邮箱;
					String isShowBindEmail = "", isShowBindPhone = "";
					if (TZ_ACTIVATE_TYPE != null && !"".equals(TZ_ACTIVATE_TYPE)) {
						if (TZ_ACTIVATE_TYPE.indexOf("EMAIL") < 0) {
							isShowBindEmail = "none";
						}

						if (TZ_ACTIVATE_TYPE.indexOf("MOBILE") < 0) {
							isShowBindPhone = "none";
						}
					} else {
						isShowBindEmail = "none";
						isShowBindPhone = "none";
					}
					/* 去除微信绑定功能 */
					String zhbd = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_ZHBD_HTML1", strBindPhone,
							strBindPhoneFlg, strBindPhoneEvent, strBindEmail, strBindEmailFlg, strBindEmailEvent,
							strChangePhoneShow, strChangeEmailShow, strChange, strPhone, strEmail, isShowBindEmail,
							isShowBindPhone);
					// 获取要显示的字段;
					String fields = "";
					// 面试申请号和项目是不需要显示修改的;
					String sql = "SELECT TZ_REG_FIELD_ID,TZ_RED_FLD_YSMC,TZ_REG_FIELD_NAME,(SELECT TZ_REG_FIELD_NAME FROM PS_TZ_REGFIELD_ENG WHERE TZ_SITEI_ID=PT.TZ_SITEI_ID AND TZ_REG_FIELD_ID=PT.TZ_REG_FIELD_ID AND LANGUAGE_CD=?) TZ_REG_FIELD_ENG_NAME,TZ_IS_REQUIRED,TZ_SYSFIELD_FLAG,TZ_FIELD_TYPE,TZ_DEF_VAL FROM PS_TZ_REG_FIELD_T PT WHERE TZ_ENABLE='Y' AND TZ_IS_ZHGL='Y' AND TZ_SITEI_ID=? AND TZ_REG_FIELD_ID NOT IN ('TZ_MSSQH','TZ_PROJECT') ORDER BY TZ_ORDER ASC";
					List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[] { language, siteId });
					if (list != null && list.size() > 0) {
						for (int i = 0; i < list.size(); i++) {
							Map<String, Object> map = list.get(i);

							String combox = "<option value =\"\">请选择</option>";
							String img = "";
							// 名称是否修改;
							String regFldYsmc = (String) map.get("TZ_RED_FLD_YSMC");
							String regFieldName = (String) map.get("TZ_REG_FIELD_NAME");
							String regFieldEngName = (String) map.get("TZ_REG_FIELD_ENG_NAME");
							if ("ENG".equals(language)) {
								if (regFieldEngName != null && !"".equals(regFieldEngName)) {
									regFldYsmc = regFieldEngName;
								}
							} else {
								if (regFieldName != null && !"".equals(regFieldName)) {
									regFldYsmc = regFieldName;
								}
							}

							// 是否必填;
							String isRequired = (String) map.get("TZ_IS_REQUIRED");
							String isRequiredLabel = "";
							if ("Y".equals(isRequired)) {
								isRequiredLabel = "*";
							} else {
								isRequiredLabel = "";
							}

							String regFieldId = (String) map.get("TZ_REG_FIELD_ID");
							String regDefValue = (String) map.get("TZ_DEF_VAL");
							if (regDefValue == null) {
								regDefValue = "";
							} else {
								regDefValue = regDefValue.trim();
							}

							ArrayList<String> fieldsArr = new ArrayList<>();
							fieldsArr.add("TZ_GENDER");
							fieldsArr.add("TZ_EMAIL");
							fieldsArr.add("TZ_MOBILE");
							fieldsArr.add("BIRTHDATE");
							fieldsArr.add("TZ_COUNTRY");
							fieldsArr.add("TZ_SCH_CNAME");
							fieldsArr.add("TZ_LEN_PROID");
							fieldsArr.add("TZ_LEN_CITY");
							ArrayList<String> doNotShowFieldsArr = new ArrayList<>();
							doNotShowFieldsArr.add("TZ_PASSWORD");
							doNotShowFieldsArr.add("TZ_REPASSWORD");
							if (doNotShowFieldsArr.contains(regFieldId)) {
								continue;
							}

							String fieldTip = "";
							fieldTip = fieldTip + "<span id='" + regFieldId + "Style' class='alert_display_none semUserTip'>";
							fieldTip = fieldTip + "		<img src='" + imgPath + "/alert.png' width='16' height='16' class='alert_img'>";
							fieldTip = fieldTip + "		<label id='" + regFieldId + "_status'></label>";
							fieldTip = fieldTip + "</span>";
							if (fieldsArr.contains(regFieldId)) {
								// 性别;
								if ("TZ_GENDER".equals(regFieldId)) {
									if ("ENG".equals(language)) {
										fields = fields + tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_PKMSEX_FILD_EN_HTML", regFldYsmc,regFieldId, isRequiredLabel);
									} else {
										fields = fields + tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_PKMSEX_FILD_HTML", regFldYsmc,	regFieldId, isRequiredLabel);
									}
								}

								// TZ_EMAIL;
								if ("TZ_EMAIL".equals(regFieldId)) {
									fieldTip = "";
									fieldTip = fieldTip
											+ "<span id='userEmailStyle' class='alert_display_none semUserTip'>";
									fieldTip = fieldTip + "	<img src='" + imgPath
											+ "/alert.png' width='16' height='16' class='alert_img'>";
									fieldTip = fieldTip + "	<label id='userEmail_status'></label>";
									fieldTip = fieldTip + "</span>";
									fields = fields + tzGdObject.getHTMLText(
											"HTML.TZWebSiteRegisteBundle.TZ_GD_PKMUSERFIELD_HTML", regFldYsmc, "userEmail",
											"", "", isRequiredLabel, fieldTip, "required=\"" + isRequired + "\"");
								}

								// TZ_MOBILE;
								if ("TZ_MOBILE".equals(regFieldId)) {
									fieldTip = "";
									fieldTip = fieldTip
											+ "<span id='userMoblieStyle' class='alert_display_none semUserTip'>";
									fieldTip = fieldTip + "	<img src='" + imgPath
											+ "/alert.png' width='16' height='16' class='alert_img'>";
									fieldTip = fieldTip + "	<label id='userMobliestatus'></label>";
									fieldTip = fieldTip + "</span>";
									fields = fields + tzGdObject.getHTMLText(
											"HTML.TZWebSiteRegisteBundle.TZ_GD_PKMUSERFIELD_HTML", regFldYsmc, "userMoblie",
											"", "", isRequiredLabel, fieldTip, "required=\"" + isRequired + "\"");
								}

								// BIRTHDATE;
								if ("BIRTHDATE".equals(regFieldId)) {
									fields = fields
											+ tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_PKMUSERFIELD_HTML",
													regFldYsmc, regFieldId, "", "readonly=\"true\"", isRequiredLabel,
													fieldTip, "required=\"" + isRequired + "\"");
								}

								// TZ_COUNTRY;
								if ("TZ_COUNTRY".equals(regFieldId)) {
									img = "<img src=\"" + imgPath
											+ "/chazhao.png\" class=\"serch-ico\" id=\"TZ_COUNTRY_click\"/>";
									fields = fields
											+ tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_PKMUSERFIELD_HTML",
													regFldYsmc, regFieldId, img, "readonly=\"true\"", isRequiredLabel,
													fieldTip, "required=\"" + isRequired + "\"");
								}

								// TZ_SCH_CNAME;
								if ("TZ_SCH_CNAME".equals(regFieldId)) {
									img = "<img src=\"" + imgPath
											+ "/chazhao.png\" class=\"serch-ico\" id=\"TZ_SCH_CNAME_click\"/ style=\"top:0px;left:-35px;\">";
									fields = fields + tzGdObject.getHTMLText(
											"HTML.TZMobileSitePageBundle.TZ_GD_USERFIELD_HTML2", regFldYsmc, regFieldId,
											img, "readonly=\"true\"", regFieldId + "_Country", isRequiredLabel, fieldTip,
											"required=\"" + isRequired + "\"");

								}

								// TZ_LEN_PROID;
								if ("TZ_LEN_PROID".equals(regFieldId)) {
									img = "<img src=\"" + imgPath
											+ "/chazhao.png\" class=\"serch-ico\" id=\"TZ_LEN_PROID_click\" style=\"top:0px;left:-42px;\"/>";
									fields = fields
											+ tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_PKMUSERFIELD_HTML",
													regFldYsmc, regFieldId, img, "readonly=\"true\"", isRequiredLabel,
													fieldTip, "required=\"" + isRequired + "\"");
								}

								// TZ_LEN_CITY;
								if ("TZ_LEN_CITY".equals(regFieldId)) {
									img = "<img src=\"" + imgPath
											+ "/chazhao.png\" class=\"serch-ico\" id=\"TZ_LEN_CITY_click\"/>";
									fields = fields
											+ tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_PKMUSERFIELD_HTML",
													regFldYsmc, regFieldId, img, "readonly=\"true\"", isRequiredLabel,
													fieldTip, "required=\"" + isRequired + "\"");
								}
							} else {
								// 是否下拉框;
								String fieldType = (String) map.get("TZ_FIELD_TYPE");
								if ("DROP".equals(fieldType)) {
									// String dropSQL = "SELECT
									// TZ_OPT_ID,TZ_OPT_VALUE,(SELECT TZ_OPT_VALUE
									// FROM PS_TZ_YHZC_XXZ_ENG WHERE
									// TZ_JG_ID=PT.TZ_JG_ID AND
									// TZ_REG_FIELD_ID=PT.TZ_REG_FIELD_ID AND
									// TZ_OPT_ID=PT.TZ_OPT_ID AND LANGUAGE_CD=? )
									// TZ_OPT_EN_VALUE ,TZ_SELECT_FLG FROM
									// PS_TZ_YHZC_XXZ_TBL PT WHERE TZ_JG_ID=? AND
									// TZ_REG_FIELD_ID=? ORDER BY TZ_ORDER ASC";
									String dropSQL = "SELECT TZ_OPT_ID,TZ_OPT_VALUE,(SELECT TZ_OPT_VALUE FROM PS_TZ_YHZC_XXZ_ENG WHERE TZ_SITEI_ID=PT.TZ_SITEI_ID AND TZ_REG_FIELD_ID=PT.TZ_REG_FIELD_ID AND TZ_OPT_ID=PT.TZ_OPT_ID AND LANGUAGE_CD=? ) TZ_OPT_EN_VALUE ,TZ_SELECT_FLG FROM PS_TZ_YHZC_XXZ_TBL PT WHERE TZ_SITEI_ID=? AND TZ_REG_FIELD_ID=? ORDER BY TZ_ORDER ASC";
									List<Map<String, Object>> dropList = jdbcTemplate.queryForList(dropSQL,
											new Object[] { language, siteId, regFieldId });

									for (int j = 0; j < dropList.size(); j++) {
										String optId = (String) dropList.get(j).get("TZ_OPT_ID");
										String optValue = (String) dropList.get(j).get("TZ_OPT_VALUE");
										String optEngValue = (String) dropList.get(j).get("TZ_OPT_EN_VALUE");
										if (optEngValue == null || "".equals(optEngValue)) {
											optEngValue = optValue;
										}
										// String selectFlg =
										// (String)dropList.get(j).get("TZ_SELECT_FLG");
										if ("ENG".equals(language)) {
											combox = combox + "<option value =\"" + optId + "\">" + optEngValue
													+ "</option>";
										} else {
											combox = combox + "<option value =\"" + optId + "\">" + optValue + "</option>";
										}
									}
									fields = fields + tzGdObject.getHTMLText(
											"HTML.TZWebSiteRegisteBundle.TZ_GD_PKMCOMBOX_HTML", regFldYsmc, regFieldId,
											combox, fieldTip, isRequiredLabel, "required=\"" + isRequired + "\"");
								} else {
									fields = fields + tzGdObject.getHTMLText(
											"HTML.TZWebSiteRegisteBundle.TZ_GD_PKMUSERFIELD_HTML", regFldYsmc, regFieldId,
											"", "", isRequiredLabel, fieldTip, "required=\"" + isRequired + "\"");
								}
							}
						}

					}

					// 选择省份;
					String Province = commonUrl;
					Province = Province
							+ "?tzParams={\"ComID\":\"TZ_COMMON_COM\",\"PageID\":\"TZ_PROVINCE_STD\",\"OperateType\":\"HTML\",\"comParams\":{\"TZ_PROV_ID\":\"TZ_LEN_PROID\",\"siteId\":\""
							+ siteId + "\"}}";
					// 选择城市;
					String City1 = commonUrl;
					City1 = City1
							+ "?tzParams={\"ComID\":\"TZ_COMMON_COM\",\"PageID\":\"TZ_CITY_STD\",\"OperateType\":\"HTML\",\"comParams\":{\"OType\":\"CITY\",\"TZ_CITY_ID\":\"TZ_LEN_CITY\",\"siteId\":\""
							+ siteId + "\"}}";

					// 头像;
					String TZ_ATT_A_URL = "", TZ_ATTACHSYSFILENA = "";
					String userPhoto = "";
					String photoSQL = "SELECT TZ_ATT_A_URL,A.TZ_ATTACHSYSFILENA FROM PS_TZ_OPR_PHT_GL_T A,PS_TZ_OPR_PHOTO_T B WHERE A.TZ_ATTACHSYSFILENA=B.TZ_ATTACHSYSFILENA AND A.OPRID=?";
					Map<String, Object> photoMap = jdbcTemplate.queryForMap(photoSQL, new Object[] { oprid });
					if (photoMap != null) {
						TZ_ATT_A_URL = (String) photoMap.get("TZ_ATT_A_URL");
						TZ_ATTACHSYSFILENA = (String) photoMap.get("TZ_ATTACHSYSFILENA");
						if (TZ_ATT_A_URL != null && !"".equals(TZ_ATT_A_URL) && TZ_ATTACHSYSFILENA != null
								&& !"".equals(TZ_ATTACHSYSFILENA)) {
							if ((TZ_ATT_A_URL.lastIndexOf("/") + 1) == TZ_ATT_A_URL.length()) {
								userPhoto = TZ_ATT_A_URL + TZ_ATTACHSYSFILENA;
							} else {
								userPhoto = TZ_ATT_A_URL + "/" + TZ_ATTACHSYSFILENA;
							}
							userPhoto = contextPath + userPhoto;
						}
					}
					if (userPhoto == null || "".equals(userPhoto)) {
						userPhoto = imgPath + "/bjphoto.jpg";
					}

					String strImgC = "";
					if ("ENG".equals(language)) {
						strImgC = imgPath + "/edituser-pic-en.png";
					} else {
						strImgC = imgPath + "/edituser-pic.png";
					}

					String returnHtml = "";
					// 判断是否需要显示头像，如果为Y则显示，否则不显示;
					// String strIsShowPhotoSQL = "SELECT TZ_IS_SHOW_PHOTO_2 FROM
					// PS_TZ_USERREG_MB_T WHERE TZ_JG_ID =?";
					String strIsShowPhotoSQL = "SELECT TZ_IS_SHOW_PHOTO_2 FROM PS_TZ_USERREG_MB_T WHERE TZ_SITEI_ID =?";
					String strIsShowPhoto = jdbcTemplate.queryForObject(strIsShowPhotoSQL, new Object[] { siteId },
							"String");
					//有照片无照片不在账户管理中显示				
					returnHtml = tzGdObject.getHTMLText("HTML.TZMobileSitePageBundle.TZ_MENU_ZHGL_HTML",saveActivate_url,phoUrl,commonUrl,str_mobile,str_userInfo,SaveRemind,Province,City1,strBind,strRelease,strAbsence,strPassSucTips,countryUrl,imgPath,fields,contextPath,updpassword);

					return returnHtml;
				} else {
					return "站点不存在";
				}
			} catch (Exception e) {
				e.printStackTrace();
				return "获取数据异常";
			}

		}
		// 修改密码初始化HTML;
		public String userPhoneFixPasswordHTML(String imgPath, String jgId, String strLang) {

			String strOldPass = validateUtil.getMessageTextWithLanguageCd(jgId, strLang, "TZ_SITE_MESSAGE", "8", "旧密码",
					"Old Password");
			String strNewPass = validateUtil.getMessageTextWithLanguageCd(jgId, strLang, "TZ_SITE_MESSAGE", "9", "新密码",
					"New Password");
			String strConPass = validateUtil.getMessageTextWithLanguageCd(jgId, strLang, "TZ_SITE_MESSAGE", "10", "确认密码",
					"Confirm Password");
			String strSaveBtn = validateUtil.getMessageTextWithLanguageCd(jgId, strLang, "TZ_SITE_MESSAGE", "7", "保存",
					"Save");
			String strBlankTips = validateUtil.getMessageTextWithLanguageCd(jgId, strLang, "TZ_SITE_MESSAGE", "26", "不能为空",
					"cannot be blank");
			String strErrorTips = validateUtil.getMessageTextWithLanguageCd(jgId, strLang, "TZ_SITE_MESSAGE", "27", "不正确",
					"is incorrect");
			String strNotSameTips = validateUtil.getMessageTextWithLanguageCd(jgId, strLang, "TZ_SITE_MESSAGE", "28",
					"两次密码不一致", "New Password and Confirm Password is not consistent");
			String strPassSucTips = validateUtil.getMessageTextWithLanguageCd(jgId, strLang, "TZ_SITE_MESSAGE", "29",
					"修改成功", "The modification is successful");
			String strWeakTips = validateUtil.getMessageTextWithLanguageCd(jgId, strLang, "TZ_SITE_MESSAGE", "32", "修改成功",
					"weak");
			String strMiddleTips = validateUtil.getMessageTextWithLanguageCd(jgId, strLang, "TZ_SITE_MESSAGE", "31", "修改成功",
					"middle");
			String strStrongTips = validateUtil.getMessageTextWithLanguageCd(jgId, strLang, "TZ_SITE_MESSAGE", "30", "修改成功",
					"strong");
			String strPassStrengthTips = validateUtil.getMessageTextWithLanguageCd(jgId, strLang, "TZ_SITE_MESSAGE", "33",
					"密码强度", "Password Strength");
			String strPassTips1 = validateUtil.getMessageTextWithLanguageCd(jgId, strLang, "TZ_SITE_MESSAGE", "34",
					"6-32个字符", "6-32 characters");
			String strPassTips2 = validateUtil.getMessageTextWithLanguageCd(jgId, strLang, "TZ_SITE_MESSAGE", "35",
					"只能包含字母、数字以及下划线", "Can only contain letters, numbers, and underscores.");
			String strPassTips3 = validateUtil.getMessageTextWithLanguageCd(jgId, strLang, "TZ_SITE_MESSAGE", "36",
					"字母、数字和下划线至少包含2种", "Letters, numbers and underscores at least two .");
			/*
			 * String strStrongMsg = validateUtil.getMessageTextWithLanguageCd(jgId,
			 * strLang, "TZ_SITE_MESSAGE", "122", "密码强度不够",
			 * "Stronger password needed.");
			 */
			String strStrongMsg = validateUtil.getMessageTextWithLanguageCd(jgId, strLang, "TZ_SITE_MESSAGE", "134",
					"密码格式不符合要求", "Wrong password format.");

			// 通用链接;
			String contextPath = request.getContextPath();
			String FixPassword = contextPath + "/dispatcher";

			try {
				return tzGdObject.getHTMLText("HTML.TZMobileSitePageBundle.TZ_GD_FIXPASS_HTML", FixPassword, strOldPass,
						strNewPass, strConPass, imgPath, strBlankTips, strErrorTips, strNotSameTips, strPassSucTips,
						strWeakTips, strMiddleTips, strStrongTips, strPassStrengthTips, strPassTips1, strPassTips2,
						strPassTips3, strStrongMsg, imgPath, contextPath);
			} catch (TzSystemException e) {
				e.printStackTrace();
				return "【TZ_GD_FIXPASS_HTML】html对象未定义";
			}
		}
}
