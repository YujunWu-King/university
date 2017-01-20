package com.tranzvision.gd.TZWebSiteRegisteBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzWebsiteLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiDefnTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiDefnT;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.ValidateUtil;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.encrypt.DESUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
import com.tranzvision.gd.util.qrcode.CreateQRCode;

/**
 * 
 * @author tang 招生账号管理 PS:TZ_GD_USERMG_PKG:TZ_USER_MANAGEMENT
 */
@Service("com.tranzvision.gd.TZWebSiteRegisteBundle.service.impl.SemUserManagementServiceImpl")
public class SemUserManagementServiceImpl extends FrameworkImpl { 
	@Autowired
	private TZGDObject tzGdObject;
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private SiteRepCssServiceImpl objRep;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private ValidateUtil validateUtil;
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private TzWebsiteLoginServiceImpl tzWebsiteLoginServiceImpl;
	@Autowired
	private PsTzSiteiDefnTMapper psTzSiteiDefnTMapper;
	@Autowired
	private CreateQRCode createQRCode;	

	// 个人信息管理;
	public String userInformation(String siteId) {
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

				String strWeChatBind = validateUtil.getMessageTextWithLanguageCd(jgId, language, "TZ_SITE_MESSAGE", "222",
						"扫二维码，绑定微信账号", "Scan the QR code,bind your wechat account.");
				
				String strPassSucTips = validateUtil.getMessageTextWithLanguageCd(jgId, language, "TZ_SITE_MESSAGE",
						"29", "修改成功", "The modification is successful");

				// 通用链接;
				String contextPath = request.getContextPath();
				String commonUrl = contextPath + "/dispatcher";
				String imgPath = getSysHardCodeVal.getWebsiteSkinsImgPath();
				imgPath = contextPath + imgPath + "/" + skinId;

				// 修改密码;
				String updpassword = this.userFixPasswordHTML(imgPath, jgId, language);

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
						+ "?tzParams={\"ComID\":\"TZ_GD_ZS_USERMNG\",\"PageID\":\"TZ_SEM_UPPHOTO_STD\",\"OperateType\":\"HTML\",\"comParams\":{\"siteId\":\""
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

				String bindHtml = "";
				//增加微信绑定
				String qrcodeFileUrl = "";
				String bdWeChatSQL = "SELECT 'Y' FROM PS_TZ_OPRD_WX_T WHERE OPRID=? AND TZ_BIND_FLAG='Y'";
				String strBindWeChat = jdbcTemplate.queryForObject(bdWeChatSQL, new Object[] { oprid }, "String");
				if(!"Y".equals(strBindWeChat)){
					String ctxPath = request.getContextPath();
					
					String qrcodeFileName = "TZ_PER_WECHAT_" + siteId + "_" + oprid + ".png";
					//String qrcodeUrl =  request.getScheme() + "://" + request.getServerName() + ":"	+ String.valueOf(request.getServerPort()) + ctxPath + "/person/wechat/" + siteId + "/" + oprid;
					String qrcodeUrl = "https://www.baidu.com";
					String qrcodeFilePath = createQRCode.encodeQRCode(jgId, qrcodeUrl, qrcodeFileName, 150, 150);
					qrcodeFileUrl = ctxPath + qrcodeFilePath;					
				}else{
					strWeChatBind = "绑定的微信账号";		
				}
												
				String zhbd = "";
				if(!"Y".equals(strBindWeChat)){
					zhbd = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_ZHBD_HTML2",  strBindPhone,
						strBindPhoneFlg, strBindPhoneEvent, strBindEmail, strBindEmailFlg, strBindEmailEvent,
						strChangePhoneShow, strChangeEmailShow, strChange, strPhone, strEmail, isShowBindEmail,
						isShowBindPhone,strWeChat,strWeChatBind,qrcodeFileUrl);
				}else{
					zhbd = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_ZHBD_HTML3",  strBindPhone,
							strBindPhoneFlg, strBindPhoneEvent, strBindEmail, strBindEmailFlg, strBindEmailEvent,
							strChangePhoneShow, strChangeEmailShow, strChange, strPhone, strEmail, isShowBindEmail,
							isShowBindPhone,strWeChat,strWeChatBind,strRelease);
				}

				// 获取要显示的字段;
				String fields = "";
				//面试申请号和项目是不需要显示修改的;
				//String sql = "SELECT TZ_REG_FIELD_ID,TZ_RED_FLD_YSMC,TZ_REG_FIELD_NAME,(SELECT TZ_REG_FIELD_NAME FROM PS_TZ_REGFIELD_ENG WHERE TZ_JG_ID=PT.TZ_JG_ID AND TZ_REG_FIELD_ID=PT.TZ_REG_FIELD_ID AND LANGUAGE_CD=?) TZ_REG_FIELD_ENG_NAME,TZ_IS_REQUIRED,TZ_SYSFIELD_FLAG,TZ_FIELD_TYPE,TZ_DEF_VAL FROM PS_TZ_REG_FIELD_T PT WHERE TZ_ENABLE='Y' AND TZ_JG_ID=? AND TZ_REG_FIELD_ID NOT IN ('TZ_MSSQH','TZ_PROJECT') ORDER BY TZ_ORDER ASC";
				String sql = "SELECT TZ_REG_FIELD_ID,TZ_RED_FLD_YSMC,TZ_REG_FIELD_NAME,(SELECT TZ_REG_FIELD_NAME FROM PS_TZ_REGFIELD_ENG WHERE TZ_SITEI_ID=PT.TZ_SITEI_ID AND TZ_REG_FIELD_ID=PT.TZ_REG_FIELD_ID AND LANGUAGE_CD=?) TZ_REG_FIELD_ENG_NAME,TZ_IS_REQUIRED,TZ_SYSFIELD_FLAG,TZ_FIELD_TYPE,TZ_DEF_VAL FROM PS_TZ_REG_FIELD_T PT WHERE TZ_ENABLE='Y' AND TZ_SITEI_ID=? AND TZ_REG_FIELD_ID NOT IN ('TZ_MSSQH','TZ_PROJECT') ORDER BY TZ_ORDER ASC";
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
						if(doNotShowFieldsArr.contains(regFieldId)){
							continue;
						}
						if (fieldsArr.contains(regFieldId)) {
							// 性别;
							if ("TZ_GENDER".equals(regFieldId)) {
								if ("ENG".equals(language)) {
									fields = fields + tzGdObject.getHTMLText(
											"HTML.TZWebSiteRegisteBundle.TZ_GD_SEX_FILD_EN_HTML", regFldYsmc,
											regFieldId);
								} else {
									fields = fields
											+ tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_SEX_FILD_HTML",
													regFldYsmc, regFieldId);
								}
							}

							// TZ_EMAIL;
							if ("TZ_EMAIL".equals(regFieldId)) {
								fields = fields
										+ tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_USERFIELD_HTML",
												regFldYsmc, "userEmail", "", "");
							}

							// TZ_MOBILE;
							if ("TZ_MOBILE".equals(regFieldId)) {
								fields = fields
										+ tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_USERFIELD_HTML",
												regFldYsmc, "userMoblie", "", "");
							}

							// BIRTHDATE;
							if ("BIRTHDATE".equals(regFieldId)) {
								fields = fields
										+ tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_USERFIELD_HTML",
												regFldYsmc, regFieldId, "", "readonly=\"true\"");
							}

							// TZ_COUNTRY;
							if ("TZ_COUNTRY".equals(regFieldId)) {
								img = "<img src=\"" + imgPath
										+ "/chazhao.png\" class=\"serch-ico\" id=\"TZ_COUNTRY_click\"/>";
								fields = fields
										+ tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_USERFIELD_HTML",
												regFldYsmc, regFieldId, img, "readonly=\"true\"");
							}

							// TZ_SCH_CNAME;
							if ("TZ_SCH_CNAME".equals(regFieldId)) {
								img = "<img src=\"" + imgPath
										+ "/chazhao.png\" class=\"serch-ico\" id=\"TZ_SCH_CNAME_click\"/ style=\"top:0px;left:-42px;\">";
								fields = fields
										+ tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_USERFIELD_HTML",
												regFldYsmc, regFieldId, img, "readonly=\"true\"");
							}

							// TZ_LEN_PROID;
							if ("TZ_LEN_PROID".equals(regFieldId)) {
								img = "<img src=\"" + imgPath
										+ "/chazhao.png\" class=\"serch-ico\" id=\"TZ_LEN_PROID_click\" style=\"top:0px;left:-42px;\"/>";
								fields = fields
										+ tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_USERFIELD_HTML",
												regFldYsmc, regFieldId, img, "readonly=\"true\"");
							}

							// TZ_LEN_CITY;
							if ("TZ_LEN_CITY".equals(regFieldId)) {
								img = "<img src=\"" + imgPath
										+ "/chazhao.png\" class=\"serch-ico\" id=\"TZ_LEN_CITY_click\"/>";
								fields = fields
										+ tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_USERFIELD_HTML",
												regFldYsmc, regFieldId, img, "readonly=\"true\"");
							}
						} else {
							// 是否下拉框;
							String fieldType = (String) map.get("TZ_FIELD_TYPE");
							if ("DROP".equals(fieldType)) {
								//String dropSQL = "SELECT TZ_OPT_ID,TZ_OPT_VALUE,(SELECT TZ_OPT_VALUE FROM PS_TZ_YHZC_XXZ_ENG WHERE TZ_JG_ID=PT.TZ_JG_ID AND TZ_REG_FIELD_ID=PT.TZ_REG_FIELD_ID AND TZ_OPT_ID=PT.TZ_OPT_ID AND LANGUAGE_CD=? ) TZ_OPT_EN_VALUE ,TZ_SELECT_FLG FROM PS_TZ_YHZC_XXZ_TBL PT WHERE TZ_JG_ID=? AND TZ_REG_FIELD_ID=? ORDER BY TZ_ORDER ASC";
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
								fields = fields
										+ tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_COMBOX_HTML", 
												regFldYsmc, regFieldId, combox);
							} else {
								fields = fields
										+ tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_USERFIELD_HTML",
												regFldYsmc, regFieldId, "", "");
							}
						}
					}

				}

				// 选择省份;
				String Province = commonUrl;
				Province = Province
						+ "?tzParams={\"ComID\":\"TZ_COMMON_COM\",\"PageID\":\"TZ_PROVINCE_STD\",\"OperateType\":\"HTML\",\"comParams\":{\"TZ_PROV_ID\":\"TZ_LEN_PROID\",\"siteId\":\"" + siteId + "\"}}";
				// 选择城市;
				String City1 = commonUrl;
				City1 = City1
						+ "?tzParams={\"ComID\":\"TZ_COMMON_COM\",\"PageID\":\"TZ_CITY_STD\",\"OperateType\":\"HTML\",\"comParams\":{\"OType\":\"CITY\",\"TZ_CITY_ID\":\"TZ_LEN_CITY\",\"siteId\":\"" + siteId + "\"}}";

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

				// 判断是否需要显示头像，如果为Y则显示，否则不显示;
				//String strIsShowPhotoSQL = "SELECT TZ_IS_SHOW_PHOTO_2 FROM PS_TZ_USERREG_MB_T WHERE TZ_JG_ID =?";
				String strIsShowPhotoSQL = "SELECT TZ_IS_SHOW_PHOTO_2 FROM PS_TZ_USERREG_MB_T WHERE TZ_SITEI_ID =?";
				String strIsShowPhoto = jdbcTemplate.queryForObject(strIsShowPhotoSQL, new Object[] { siteId }, "String");
				if ("Y".equals(strIsShowPhoto)) {
					return tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_WDZH_HTML2", saveActivate_url,
							phoUrl, updpassword, commonUrl, str_mobile, msgmail_html, zhbd, str_userInfo, SaveRemind,
							fields, Province, City1, strColuTitle, strTab1, strTab2, strTab3, strTab4, strPhoto,
							strSaveBtn, strBind, strRelease, strAbsence, strPassSucTips, countryUrl, strImgC, userPhoto,
							contextPath);
				} else {
					return tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_WDZH_NOT_PHOTO_HTML",
							saveActivate_url, phoUrl, updpassword, commonUrl, str_mobile, msgmail_html, zhbd,
							str_userInfo, SaveRemind, fields, Province, City1, strColuTitle, strTab1, strTab2, strTab3,
							strTab4, strPhoto, strSaveBtn, strBind, strRelease, strAbsence, strPassSucTips, countryUrl,
							contextPath);
				}

			} else {
				return "站点不存在";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "获取数据异常";
		}

	}

	// 修改密码初始化HTML;
	public String userFixPasswordHTML(String imgPath, String jgId, String strLang) {

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
		String strStrongMsg = validateUtil.getMessageTextWithLanguageCd(jgId, strLang, "TZ_SITE_MESSAGE", "122",
				"密码强度不够", "Stronger password needed.");

		// 通用链接;
		String contextPath = request.getContextPath();
		String FixPassword = contextPath + "/dispatcher";

		try {
			return tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_FIXPASS_HTML", FixPassword, strOldPass,
					strNewPass, strConPass, strSaveBtn, strBlankTips, strErrorTips, strNotSameTips, strPassSucTips,
					strWeakTips, strMiddleTips, strStrongTips, strPassStrengthTips, strPassTips1, strPassTips2,
					strPassTips3, strStrongMsg, imgPath, contextPath);
		} catch (TzSystemException e) {
			e.printStackTrace();
			return "【TZ_GD_FIXPASS_HTML】html对象未定义";
		}
	}

	@Override
	public String tzGetHtmlContent(String strParams) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			String strSiteId = jacksonUtil.getString("siteId");
			String strResultConten = "";

			strResultConten = this.userInformation(strSiteId);

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
				String contextPath = request.getContextPath();
				strResultConten = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_MENU_ZHGL_HTML2", contextPath,
						strOrgId, strSiteId, strResultConten);
			} catch (TzSystemException e) {
				e.printStackTrace();
				return "【TZ_MENU_ZHGL_HTML】html对象未定义";
			}

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

	@Override
	public String tzOther(String oprType, String comParams, String[] errorMsg) {
		String strResultConten = "";
		if ("USERINFO".equals(oprType)) {
			strResultConten = this.getUserInfo(comParams, errorMsg);
		}else if ("SAVEUSERINFO".equals(oprType)) {
			strResultConten = this.saveUserInfo(comParams, errorMsg);
		}else if ("PWD".equals(oprType)) {
			strResultConten = this.savePassword(comParams, errorMsg);
		}else if ("NOTICE".equals(oprType)) {
			strResultConten = this.saveNotice(comParams, errorMsg);
		}else if ("EMAIL".equals(oprType)) {
			strResultConten = this.bindOrUnbindEmail(comParams, errorMsg);
		}else if ("MOBILE".equals(oprType)) {
			strResultConten = this.bindOrUnbindMobile(comParams, errorMsg);
		}
		return strResultConten;
	}

	// 获取基本信息;
	public String getUserInfo(String comParams, String[] errorMsg) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			String fields = "";

			jacksonUtil.json2Map(comParams);
			// 站点ID;
			String siteId = jacksonUtil.getString("siteId");
			// 获取机构id;
			//String strJgid = "";
			String skinId = "";

			PsTzSiteiDefnT psTzSiteiDefnT = psTzSiteiDefnTMapper.selectByPrimaryKey(siteId);
			if (psTzSiteiDefnT == null) {
				return "";
			}
			//strJgid = psTzSiteiDefnT.getTzJgId();
			skinId = psTzSiteiDefnT.getTzSkinId();
			// 通用链接;
			String contextPath = request.getContextPath();
			String commonUrl = contextPath + "/dispatcher";
			String imgPath = getSysHardCodeVal.getWebsiteSkinsImgPath();
			imgPath = contextPath + imgPath + "/" + skinId;

			//String sql = "SELECT TZ_REG_FIELD_ID,TZ_FIELD_TYPE FROM PS_TZ_REG_FIELD_T WHERE TZ_ENABLE='Y' AND TZ_JG_ID=? ORDER BY TZ_ORDER ASC";
			String sql = "SELECT TZ_REG_FIELD_ID,TZ_FIELD_TYPE FROM PS_TZ_REG_FIELD_T WHERE TZ_ENABLE='Y' AND TZ_SITEI_ID=? ORDER BY TZ_ORDER ASC";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[] { siteId });

			ArrayList<String> arryField = new ArrayList<>();
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					String regFieldId = (String) list.get(i).get("TZ_REG_FIELD_ID");
					//面试申请号和项目不需要显示;
					if("TZ_MSSQH".equals(regFieldId) || "TZ_PROJECT".equals(regFieldId)){
						continue;
					}
					if (regFieldId != null && !"".equals(regFieldId) && !"TZ_EMAIL".equals(regFieldId)
							&& !"TZ_MOBILE".equals(regFieldId) && !"TZ_PASSWORD".equals(regFieldId)
							&& !"TZ_REPASSWORD".equals(regFieldId)) {
						if ("".equals(fields)) {
							fields = regFieldId;
						} else {
							fields = fields + "," + regFieldId;
						}
						arryField.add(regFieldId);
					}
				}
			}

			Map<String, Object> returnMap = new HashMap<>();
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			if (fields != null && !"".equals(fields)) {
				String fieldsValueSQL = "SELECT " + fields + " FROM PS_TZ_REG_USER_T WHERE OPRID=?";
				returnMap = jdbcTemplate.queryForMap(fieldsValueSQL, new Object[] { oprid });
			}
			if (returnMap == null) {
				returnMap = new HashMap<>();
			}

			String yhxxSQL = "SELECT TZ_EMAIL,TZ_MOBILE,TZ_YXBD_BZ,TZ_SJBD_BZ,TZ_BJS_EML,TZ_BJS_SMS FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=?";
			Map<String, Object> yhxxMap = jdbcTemplate.queryForMap(yhxxSQL, new Object[] { oprid });
			if (yhxxMap != null) {

				returnMap.put("isEmail", yhxxMap.get("TZ_BJS_EML"));
				returnMap.put("isMobile", yhxxMap.get("TZ_BJS_SMS"));
				returnMap.put("isBindEmail", yhxxMap.get("TZ_YXBD_BZ"));
				returnMap.put("isBindMobile", yhxxMap.get("TZ_SJBD_BZ"));
			}

			// Mabc，20150610，邮箱手机应该取联系信息表中的;
			String lxfsSQL = "select TZ_ZY_SJ,TZ_ZY_EMAIL from PS_TZ_LXFSINFO_TBL where TZ_LXFS_LY='ZCYH' and TZ_LYDX_ID=?";
			Map<String, Object> lxfsMap = jdbcTemplate.queryForMap(lxfsSQL, new Object[] { oprid });
			if (lxfsMap != null) {
				returnMap.put("userEmail", lxfsMap.get("TZ_ZY_EMAIL"));
				returnMap.put("userMoblie", lxfsMap.get("TZ_ZY_SJ"));
			}

			// 绑定/解除绑定邮箱;
			String BindEmail = commonUrl;
			// 绑定/解除绑定手机;
			String BindMobile = commonUrl;

			returnMap.put("bindEmailURL", BindEmail);
			returnMap.put("bindMobileURL", BindMobile);

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

				}
			}
			if (userPhoto == null || "".equals(userPhoto)) {
				userPhoto = imgPath + "/bjphoto.jpg";
			}
			returnMap.put("userPhoto", userPhoto);
			return jacksonUtil.Map2json(returnMap);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	// 招生账号管理修改相关链接;
	public String saveUserInfo(String comParams, String[] errorMsg) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(comParams);

			String strUserEmail = "";
			String strUserMoblie = "";
			String strUserSkype = "";
			// 获取机构id;
			String strJgid = "";
			String strLang = "";

			if (jacksonUtil.containsKey("userEmail")) {
				strUserEmail = jacksonUtil.getString("userEmail");
			}

			if (jacksonUtil.containsKey("userMoblie")) {
				strUserMoblie = jacksonUtil.getString("userMoblie");
			}
			
			if (jacksonUtil.containsKey("TZ_SKYPE")) {
				strUserSkype = jacksonUtil.getString("TZ_SKYPE");
			}

			if (jacksonUtil.containsKey("jgid")) {
				strJgid = jacksonUtil.getString("jgid");
			}

			if (jacksonUtil.containsKey("lang")) {
				strLang = jacksonUtil.getString("lang");
			}
			
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			//得到用户注册的siteid;
			String siteId = jdbcTemplate.queryForObject("SELECT TZ_SITEI_ID FROM PS_TZ_REG_USER_T where OPRID=?", new Object[]{oprid},"String");
			if(siteId == null || "".equals(siteId)){
				return "当前登录人员没有对应的注册站点id";
			}
			
			// 页面文字双语化
			String strBlankTips = validateUtil.getMessageTextWithLanguageCd(strJgid, strLang, "TZ_SITE_MESSAGE", "26",
					"不能为空", "cannot be blank");

			String strFirstName = "";
			String strLastName = "";
			String tzRealName = "";

			ArrayList<String> updateList = new ArrayList<>();
			String updateRegSql = "";
			// 注册字段;
			//String sql = "SELECT TZ_REG_FIELD_ID,TZ_IS_REQUIRED,TZ_RED_FLD_YSMC,TZ_REG_FIELD_NAME,(SELECT TZ_REG_FIELD_NAME FROM PS_TZ_REGFIELD_ENG WHERE TZ_JG_ID=PT.TZ_JG_ID AND TZ_REG_FIELD_ID=PT.TZ_REG_FIELD_ID AND LANGUAGE_CD=?) TZ_REG_FIELD_ENG_NAME FROM PS_TZ_REG_FIELD_T PT WHERE TZ_ENABLE='Y' AND TZ_JG_ID=? AND TZ_REG_FIELD_ID NOT IN ('TZ_MSSQH','TZ_PROJECT') ORDER BY TZ_ORDER ASC";
			String sql = "SELECT TZ_REG_FIELD_ID,TZ_IS_REQUIRED,TZ_RED_FLD_YSMC,TZ_REG_FIELD_NAME,(SELECT TZ_REG_FIELD_NAME FROM PS_TZ_REGFIELD_ENG WHERE TZ_SITEI_ID=PT.TZ_SITEI_ID AND TZ_REG_FIELD_ID=PT.TZ_REG_FIELD_ID AND LANGUAGE_CD=?) TZ_REG_FIELD_ENG_NAME FROM PS_TZ_REG_FIELD_T PT WHERE TZ_ENABLE='Y' AND TZ_SITEI_ID=? AND TZ_REG_FIELD_ID NOT IN ('TZ_MSSQH','TZ_PROJECT') ORDER BY TZ_ORDER ASC";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[] { strLang, siteId });
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					String field = "";
					String regFieldId = (String) list.get(i).get("TZ_REG_FIELD_ID");
					String regFieldYsmc = (String) list.get(i).get("TZ_RED_FLD_YSMC");
					String regFieldName = (String) list.get(i).get("TZ_REG_FIELD_NAME");
					String regFieldEngName = (String) list.get(i).get("TZ_REG_FIELD_ENG_NAME");
					String required = (String) list.get(i).get("TZ_IS_REQUIRED");
					if ("ENG".equals(strLang)) {
						if (regFieldEngName != null && !"".equals(regFieldEngName)) {
							regFieldYsmc = regFieldEngName;
						}
					} else {
						if (regFieldName != null && !"".equals(regFieldName)) {
							regFieldYsmc = regFieldName;
						}
					}
					if (regFieldId != null && !"".equals(regFieldId) && !"TZ_EMAIL".equals(regFieldId)
							&& !"TZ_MOBILE".equals(regFieldId) && !"TZ_PASSWORD".equals(regFieldId)
							&& !"TZ_REPASSWORD".equals(regFieldId)) {
						field = jacksonUtil.getString(regFieldId);

						if ("TZ_REALNAME".equals(regFieldId)) {
							tzRealName = field;
						}

						if ("TZ_FIRST_NAME".equals(regFieldId)) {
							strFirstName = field;
						}

						if ("TZ_LAST_NAME".equals(regFieldId)) {
							strLastName = field;
						}

						if ("Y".equals(required) && (field == null || "".equals(field))) {
							return tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_USERMG_JSON", 
									regFieldYsmc + " " + strBlankTips);
						}

						if ("".equals(updateRegSql)) {
							updateRegSql = "UPDATE PS_TZ_REG_USER_T SET " + regFieldId + " = ?";
						} else {
							updateRegSql = updateRegSql + "," + regFieldId + " = ?";
						}
						updateList.add(field);

					}

					if ("TZ_EMAIL".equals(regFieldId) && (strUserEmail == null || "".equals(strUserEmail))) {
						return tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_USERMG_JSON", 
								regFieldYsmc + " " + strBlankTips);
					}

					if ("TZ_MOBILE".equals(regFieldId) && (strUserMoblie == null || "".equals(strUserMoblie))) {
						return tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_USERMG_JSON", 
								regFieldYsmc + " " + strBlankTips);
					}
				}
			}

			
			if (!"".equals(updateRegSql)) {
				updateRegSql = updateRegSql + " where OPRID = ?";
				Object[] obj = new Object[updateList.size() + 1];
				for (int j = 0; j < updateList.size(); j++) {
					obj[j] = updateList.get(j);
				}
				obj[updateList.size()] = oprid;
				jdbcTemplate.update(updateRegSql, obj);
			}

			// 更新用户信息记录表;
			if (tzRealName == null || "".equals(tzRealName)) {
				tzRealName = strFirstName + " " + strLastName;
			}
			String updateYhxxSQL = "UPDATE PS_TZ_AQ_YHXX_TBL SET TZ_REALNAME=? WHERE OPRID=?";
			jdbcTemplate.update(updateYhxxSQL, new Object[] { tzRealName, oprid });

			if (strUserEmail != null && !"".equals(strUserEmail)) {
				String updateLxfsSQL = "update PS_TZ_LXFSINFO_TBL set TZ_ZY_EMAIL=? where TZ_LXFS_LY='ZCYH' and TZ_LYDX_ID=?";
				jdbcTemplate.update(updateLxfsSQL, new Object[] { strUserEmail, oprid });
			}

			if (strUserMoblie != null && !"".equals(strUserMoblie)) {
				String updateLxfsSQL = "update PS_TZ_LXFSINFO_TBL set TZ_ZY_SJ=? where TZ_LXFS_LY='ZCYH' and TZ_LYDX_ID=?";
				jdbcTemplate.update(updateLxfsSQL, new Object[] { strUserMoblie, oprid });
			}
			
			if (strUserSkype != null && !"".equals(strUserSkype)) {
				String updateLxfsSQL = "update PS_TZ_LXFSINFO_TBL set TZ_SKYPE=? where TZ_LXFS_LY='ZCYH' and TZ_LYDX_ID=?";
				jdbcTemplate.update(updateLxfsSQL, new Object[] { strUserSkype, oprid });
			}
			

			String strPassSucTips = validateUtil.getMessageTextWithLanguageCd(strJgid, strLang, "TZ_SITE_MESSAGE", "29",
					"修改成功", "The modification is successful");
			return tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_USERMG_JSON",  strPassSucTips);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				return tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_USERMG_JSON",  e.toString());
			} catch (TzSystemException e1) {
				e1.printStackTrace();
				return e1.toString();
			}

		}
	}

	// 修改密码;
	public String savePassword(String comParams, String[] errorMsg) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(comParams);

			String strOldPass = jacksonUtil.getString("oldPass");
			String strNewPass = jacksonUtil.getString("newPass");
			String strLang = jacksonUtil.getString("lang");

			String strOldPassMsg = validateUtil.getMessageTextWithLanguageCd("", strLang, "TZ_SITE_MESSAGE", "8", "旧密码",
					"Old Password");
			String strErrorTips = validateUtil.getMessageTextWithLanguageCd("", strLang, "TZ_SITE_MESSAGE", "27", "不正确",
					"is incorrect");
			String strPassSucTips = validateUtil.getMessageTextWithLanguageCd("", strLang, "TZ_SITE_MESSAGE", "29",
					"修改成功", "The modification is successful");
			String strPassLoginTips = validateUtil.getMessageTextWithLanguageCd("", strLang, "TZ_SITE_MESSAGE", "51",
					"请先登录", "Please Login");

			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			if ("TZ_GUEST".equals(oprid)) {
				return tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_USERMG_JSON",  strPassLoginTips);
			}

			// 获得系统中旧密码;
			String strOldHashPassSQL = "SELECT OPERPSWD FROM PSOPRDEFN WHERE OPRID=? AND ACCTLOCK=0";
			String strOldHashPass = jdbcTemplate.queryForObject(strOldHashPassSQL, new Object[] { oprid }, "String");

			if (strOldPass == null || "".equals(strOldPass.trim())) {
				return tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_USERMG_JSON", 
						strOldPassMsg + " " + strErrorTips);
			}
			strOldPass = DESUtil.encrypt(strOldPass.trim(), "TZGD_Tranzvision");

			// 如果用户提交的旧密码hash加密后和系统原有的旧密码不同，则返回false终止程序;
			if (!strOldPass.equals(strOldHashPass)) {
				return tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_USERMG_JSON", 
						strOldPassMsg + " " + strErrorTips);
			}

			// 对新密码hash加密;
			String strNewHashPass = DESUtil.encrypt(strNewPass.trim(), "TZGD_Tranzvision");

			// 存储新密码到数据库;
			String updatePassword = "UPDATE PSOPRDEFN SET OPERPSWD=? WHERE OPRID=?";
			jdbcTemplate.update(updatePassword, new Object[] { strNewHashPass, oprid });
			return tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_USERMG_JSON",  strPassSucTips);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				return tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_USERMG_JSON",  e.toString());
			} catch (TzSystemException e1) {
				e1.printStackTrace();
				return e1.toString();
			}
		}
	}

	// Notice;
	public String saveNotice(String comParams, String[] errorMsg) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(comParams);
			String isEmail = jacksonUtil.getString("isEmail");
			String isMoblie = jacksonUtil.getString("isMoblie");
			String strLang = jacksonUtil.getString("lang");

			String strSuccessTips = validateUtil.getMessageTextWithLanguageCd("", strLang, "TZ_SITE_MESSAGE", "29",
					"修改成功", "The modification is successful");
			if ("true".equals(isEmail)) {
				isEmail = "Y";
			} else {
				isEmail = "N";
			}
			if ("true".equals(isMoblie)) {
				isMoblie = "Y";
			} else {
				isMoblie = "N";
			}

			// 更新用户信息记录表;
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			String sql = "UPDATE PS_TZ_AQ_YHXX_TBL SET TZ_BJS_EML=?,TZ_BJS_SMS=? WHERE OPRID=?";
			jdbcTemplate.update(sql, new Object[] { isEmail, isMoblie, oprid });
			return tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_USERMG_JSON",  strSuccessTips);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				return tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_USERMG_JSON",  e.toString());
			} catch (TzSystemException e1) {
				e1.printStackTrace();
				return e1.toString();
			}
		}
	}

	// 绑定、解绑email;
	public String bindOrUnbindEmail(String comParams, String[] errorMsg) {
		String is_Bind = "";
		String strLang = "";
		String bindEmail = request.getContextPath() + "/dispatcher";
		String strEmail = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(comParams);
			strLang = jacksonUtil.getString("lang");
			is_Bind = jacksonUtil.getString("BIND");

			String strBingTips1 = validateUtil.getMessageTextWithLanguageCd("", strLang, "TZ_SITE_MESSAGE", "38",
					"解绑失败，必须绑定一个手机或邮箱", "Release failure, you must bind a cell phone or a mailbox");
			String strBingTips2 = validateUtil.getMessageTextWithLanguageCd("", strLang, "TZ_SITE_MESSAGE", "39",
					"绑定失败，邮箱不能为空", "Binding failed, the mailbox can not be empty");
			String strBingTips3 = validateUtil.getMessageTextWithLanguageCd("", strLang, "TZ_SITE_MESSAGE", "40",
					"绑定失败，邮箱已被占用", "Binding failed, the mailbox has been bound");

			// 获得当前登录者UserId;
			String strUser = tzLoginServiceImpl.getLoginedManagerOprid(request);
			// 当前机构;
			String strOrgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);

			// 主要邮箱；主要手机;

			String lxfsSQL = "select TZ_ZY_SJ,TZ_ZY_EMAIL from PS_TZ_LXFSINFO_TBL where TZ_LXFS_LY=? and TZ_LYDX_ID=?";
			Map<String, Object> lxfsMap = jdbcTemplate.queryForMap(lxfsSQL, new Object[] { "ZCYH", strUser });
			if (lxfsMap != null) {
				strEmail = lxfsMap.get("TZ_ZY_EMAIL") == null ? "" : (String) lxfsMap.get("TZ_ZY_EMAIL");
			}
			//
			int bindPhoneFlg = 0;
			int bindEmailFlg = 0;
			// 是否绑定了手机;
			String yhxxSQL = "select count(1) from PS_TZ_AQ_YHXX_TBL where TZ_JG_ID=? and OPRID=? and TZ_RYLX='ZCYH' and TZ_SJBD_BZ='Y'";
			bindPhoneFlg = jdbcTemplate.queryForObject(yhxxSQL, new Object[] { strOrgid, strUser }, "Integer");
			// 是否绑定了邮箱;
			yhxxSQL = "select count(1) from PS_TZ_AQ_YHXX_TBL where TZ_JG_ID=? and OPRID=? and TZ_RYLX='ZCYH' and TZ_YXBD_BZ='Y'";
			bindEmailFlg = jdbcTemplate.queryForObject(yhxxSQL, new Object[] { strOrgid, strUser }, "Integer");
			// 未绑定邮箱，进行绑定;
			if (bindEmailFlg <= 0) {
				// 未录入邮箱;
				if (strEmail == null || "".equals(strEmail)) {
					return tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_BIND_EMAIL_JSON",  "N",
							bindEmail, is_Bind, strBingTips2, strEmail);
				} else {
					// 是否已经被绑定;
					int rept = 0;
					String reptSQL = "select count(1) from PS_TZ_AQ_YHXX_TBL where TZ_JG_ID=? and OPRID<>? and TZ_RYLX='ZCYH' and TZ_YXBD_BZ='Y' and TZ_EMAIL=?";
					rept = jdbcTemplate.queryForObject(reptSQL, new Object[] { strOrgid, strUser, strEmail },
							"Integer");
					if (rept > 0) {
						return tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_BIND_EMAIL_JSON",  "N",
								bindEmail, is_Bind, strBingTips3, strEmail);
					} else {
						// 更新;
						String updateSQL = "update PS_TZ_AQ_YHXX_TBL set TZ_EMAIL= ? ,TZ_YXBD_BZ='Y',ROW_LASTMANT_DTTM =current_timestamp(),ROW_LASTMANT_OPRID=? where TZ_JG_ID=? and OPRID=? and TZ_RYLX='ZCYH'";
						jdbcTemplate.update(updateSQL, new Object[] { strEmail, strUser, strOrgid, strUser });
						return tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_BIND_EMAIL_JSON",  "Y",
								bindEmail, "Y", "", strEmail);
					}
				}
			} else {
				// 已绑定邮箱，进行解绑;
				if (bindPhoneFlg <= 0) {
					// 判断是否绑定了手机，如果未绑定手机，则禁止解绑邮箱;
					return tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_BIND_EMAIL_JSON",  "N",
							bindEmail, is_Bind, strBingTips1, strEmail);
				} else {
					// 如果已绑定手机，则可以解绑邮箱;
					// 更新;
					String updateSQL = "update PS_TZ_AQ_YHXX_TBL set TZ_EMAIL='',TZ_YXBD_BZ='N',ROW_LASTMANT_DTTM=current_timestamp(),ROW_LASTMANT_OPRID=? where TZ_JG_ID=? and OPRID=? and TZ_RYLX='ZCYH'";
					jdbcTemplate.update(updateSQL, new Object[] { strUser, strOrgid, strUser });
					return tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_BIND_EMAIL_JSON",  "Y",
							bindEmail, "N", "", strEmail);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				return tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_BIND_EMAIL_JSON",  "N", bindEmail,
						is_Bind, e.toString(), strEmail);
			} catch (TzSystemException e1) {
				e1.printStackTrace();
				return e1.toString();
			}

		}
	}

	// 绑定、解绑phone;
	public String bindOrUnbindMobile(String comParams, String[] errorMsg) {
		String is_Bind = "";
		String strLang = "";
		String bindMobile = request.getContextPath() + "/dispatcher";
		String strPhone = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(comParams);
			strLang = jacksonUtil.getString("lang");
			is_Bind = jacksonUtil.getString("BIND");

			String strBingTips1 = validateUtil.getMessageTextWithLanguageCd("", strLang, "TZ_SITE_MESSAGE", "38",
					"解绑失败，必须绑定一个手机或邮箱", "Release failure, you must bind a cell phone or a mailbox");
			String strBingTips2 = validateUtil.getMessageTextWithLanguageCd("", strLang, "TZ_SITE_MESSAGE", "41",
					"绑定失败，手机不能为空", "Binding failed, the phone can not be empty");
			String strBingTips3 = validateUtil.getMessageTextWithLanguageCd("", strLang, "TZ_SITE_MESSAGE", "42",
					"绑定失败，手机已被占用", "Binding failed, the mobile phone has been bound");

			// 获得当前登录者UserId;
			String strUser = tzLoginServiceImpl.getLoginedManagerOprid(request);
			// 当前机构;
			String strOrgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);

			// 主要邮箱；主要手机;

			String lxfsSQL = "select TZ_ZY_SJ,TZ_ZY_EMAIL from PS_TZ_LXFSINFO_TBL where TZ_LXFS_LY=? and TZ_LYDX_ID=?";
			Map<String, Object> lxfsMap = jdbcTemplate.queryForMap(lxfsSQL, new Object[] { "ZCYH", strUser });
			if (lxfsMap != null) {
				strPhone = lxfsMap.get("TZ_ZY_SJ") == null ? "" : (String) lxfsMap.get("TZ_ZY_SJ");
			}
			//
			int bindPhoneFlg = 0;
			int bindEmailFlg = 0;
			// 是否绑定了手机;
			String yhxxSQL = "select count(1) from PS_TZ_AQ_YHXX_TBL where TZ_JG_ID=? and OPRID=? and TZ_RYLX='ZCYH' and TZ_SJBD_BZ='Y'";
			bindPhoneFlg = jdbcTemplate.queryForObject(yhxxSQL, new Object[] { strOrgid, strUser }, "Integer");
			// 是否绑定了邮箱;
			yhxxSQL = "select count(1) from PS_TZ_AQ_YHXX_TBL where TZ_JG_ID=? and OPRID=? and TZ_RYLX='ZCYH' and TZ_YXBD_BZ='Y'";
			bindEmailFlg = jdbcTemplate.queryForObject(yhxxSQL, new Object[] { strOrgid, strUser }, "Integer");
			// 没有绑定的手机，进行绑定;
			if (bindPhoneFlg <= 0) {
				// 未录入手机;
				if (strPhone == null || "".equals(strPhone)) {
					return tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_BIND_MOBILE_JSON",  "N", "N",
							bindMobile, is_Bind, strBingTips1);
				} else {
					// 是否已经被绑定;
					int rept = 0;
					String reptSQL = "select count(1) from PS_TZ_AQ_YHXX_TBL where TZ_JG_ID=? and OPRID<>? and TZ_RYLX='ZCYH' and TZ_SJBD_BZ='Y' and TZ_MOBILE=?";
					rept = jdbcTemplate.queryForObject(reptSQL, new Object[] { strOrgid, strUser, strPhone },
							"Integer");
					if (rept > 0) {
						return tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_BIND_MOBILE_JSON",  "N",
								bindMobile, is_Bind, strBingTips2);
					} else {
						// 更新;
						String updateSQL = "update PS_TZ_AQ_YHXX_TBL set TZ_MOBILE=?,TZ_SJBD_BZ='Y',ROW_LASTMANT_DTTM=current_timestamp(),ROW_LASTMANT_OPRID=? where TZ_JG_ID=? and OPRID=? and TZ_RYLX='ZCYH'";
						jdbcTemplate.update(updateSQL, new Object[] { strPhone, strUser, strOrgid, strUser });
						return tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_BIND_MOBILE_JSON",  "Y",
								bindMobile, "Y", "");
					}
				}
			} else {
				// 已绑定邮箱，进行解绑;
				if (bindEmailFlg <= 0) {
					// 判断是否绑定了邮箱，如果未绑定邮箱，则禁止解绑手机;
					return tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_BIND_MOBILE_JSON",  "N",
							bindMobile, is_Bind, strBingTips1);
				} else {
					// 如果已绑定邮箱，则可以解绑手机;
					// 更新;
					String updateSQL = "update PS_TZ_AQ_YHXX_TBL set TZ_MOBILE='',TZ_SJBD_BZ='N',ROW_LASTMANT_DTTM=current_timestamp(),ROW_LASTMANT_OPRID=? where TZ_JG_ID=? and OPRID=? and TZ_RYLX='ZCYH'";
					jdbcTemplate.update(updateSQL, new Object[] { strUser, strOrgid, strUser });
					return tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_BIND_MOBILE_JSON",  "Y",
							bindMobile, "N", "");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				return tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_BIND_MOBILE_JSON",  "N", bindMobile,
						is_Bind, e.toString());
			} catch (TzSystemException e1) {
				e1.printStackTrace();
				return e1.toString();
			}

		}
	}

}
