package com.tranzvision.gd.TZWebSiteRegisteBundle.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZLeaguerDataItemBundle.dao.PsTzUserregMbTMapper;
import com.tranzvision.gd.TZLeaguerDataItemBundle.model.PsTzUserregMbT;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiDefnTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiDefnTWithBLOBs;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzMsiteiDefnTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzMsiteiDefnTWithBLOBs;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 
 * @author tang 发布注册页静态化相关页面
 * 
 *         PS:TZ_GD_USERMG_PKG:TZ_GD_USER_REG
 *
 */
@Service("com.tranzvision.gd.TZWebSiteRegisteBundle.service.impl.RegisteServiceImpl")
public class RegisteServiceImpl {
	@Autowired
	private TZGDObject tzGdObject;
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private SiteRepCssServiceImpl objRep;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	@Autowired
	private PsTzSiteiDefnTMapper psTzSiteiDefnTMapper;
	@Autowired
	private PsTzUserregMbTMapper psTzUserregMbTMapper;
	@Autowired
	private PsTzMsiteiDefnTMapper psTzMsiteiDefnTMapper;

	// 生成用户注册页源代码，PS类：TZ_GD_USERMG_PKG:TZ_GD_USER_REG;
	public String userRegister(String strJgid, String strSiteId) {
		String fields = "";
		try {
			// 得到皮肤图片的路径;
			PsTzSiteiDefnTWithBLOBs psTzSiteiDefnT = psTzSiteiDefnTMapper.selectByPrimaryKey(strSiteId);
			if (psTzSiteiDefnT == null) {
				return "";
			}
			String strLang = psTzSiteiDefnT.getTzSiteLang();
			String skinId = psTzSiteiDefnT.getTzSkinId();
			String imgPath = getSysHardCodeVal.getWebsiteSkinsImgPath();
			imgPath = request.getContextPath() + imgPath + "/" + skinId;

			// 注册加载报名项目js;
			String prjJs = "";

			// 获取要显示的字段;
			// String sql = "SELECT
			// TZ_REG_FIELD_ID,TZ_RED_FLD_YSMC,TZ_REG_FIELD_NAME,(SELECT
			// TZ_REG_FIELD_NAME FROM PS_TZ_REGFIELD_ENG WHERE
			// TZ_JG_ID=PT.TZ_JG_ID AND TZ_REG_FIELD_ID=PT.TZ_REG_FIELD_ID AND
			// LANGUAGE_CD=?)
			// TZ_REG_FIELD_ENG_NAME,TZ_IS_REQUIRED,TZ_SYSFIELD_FLAG,TZ_FIELD_TYPE,TZ_DEF_VAL
			// FROM PS_TZ_REG_FIELD_T PT WHERE TZ_ENABLE='Y' AND TZ_JG_ID=?
			// ORDER BY TZ_ORDER ASC";
			String sql = "SELECT TZ_REG_FIELD_ID,TZ_RED_FLD_YSMC,TZ_REG_FIELD_NAME,(SELECT TZ_REG_FIELD_NAME FROM PS_TZ_REGFIELD_ENG WHERE TZ_SITEI_ID=PT.TZ_SITEI_ID AND TZ_REG_FIELD_ID=PT.TZ_REG_FIELD_ID AND LANGUAGE_CD=?) TZ_REG_FIELD_ENG_NAME,TZ_IS_REQUIRED,TZ_SYSFIELD_FLAG,TZ_FIELD_TYPE,TZ_DEF_VAL FROM PS_TZ_REG_FIELD_T PT WHERE TZ_ENABLE='Y' AND TZ_IS_REG='Y' AND TZ_SITEI_ID=? ORDER BY TZ_ORDER ASC";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[] { strLang, strSiteId });
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> map = list.get(i);

					String combox = "<option value =\"\">请选择</option>";
					String img = "";
					// 名称是否修改;
					String regFldYsmc = (String) map.get("TZ_RED_FLD_YSMC");
					String regFieldName = (String) map.get("TZ_REG_FIELD_NAME");
					String regFieldEngName = (String) map.get("TZ_REG_FIELD_ENG_NAME");
					if ("ENG".equals(strLang)) {
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
					if ("Y".equals(isRequired)) {
						isRequired = "*";
					} else {
						isRequired = "";
					}

					String regFieldId = (String) map.get("TZ_REG_FIELD_ID");
					String regDefValue = (String) map.get("TZ_DEF_VAL");
					if (regDefValue == null) {
						regDefValue = "";
					} else {
						regDefValue = regDefValue.trim();
					}

					ArrayList<String> fieldsArr = new ArrayList<>();
					fieldsArr.add("TZ_PASSWORD");
					fieldsArr.add("TZ_REPASSWORD");
					fieldsArr.add("TZ_GENDER");
					fieldsArr.add("BIRTHDATE");
					fieldsArr.add("TZ_COUNTRY");
					fieldsArr.add("TZ_SCH_CNAME");
					fieldsArr.add("TZ_LEN_PROID");
					fieldsArr.add("TZ_LEN_CITY");
					fieldsArr.add("TZ_MSSQH");

					if (fieldsArr.contains(regFieldId)) {
						// 密码
						if ("TZ_PASSWORD".equals(regFieldId) || "TZ_REPASSWORD".equals(regFieldId)) {
							fields = fields + tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_PASSWORD_HTML",
									"*", regFldYsmc, regFieldId, regDefValue, imgPath);
						}

						// 性别;
						if ("TZ_GENDER".equals(regFieldId)) {
							String str1 = "";
							String str2 = "";
							if ("M".equals(regDefValue)) {
								str1 = "checked=\"checked\"";
							}

							if ("F".equals(regDefValue)) {
								str2 = "checked=\"checked\"";
							}

							if ("ENG".equals(strLang)) {
								fields = fields
										+ tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_SEX_EN_HTML",
												isRequired, regFldYsmc, regFieldId, str1, str2);
							} else {
								fields = fields + tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_SEX_HTML",
										isRequired, regFldYsmc, regFieldId, str1, str2);
							}
						}

						// BIRTHDATE;
						if ("BIRTHDATE".equals(regFieldId)) {
							fields = fields + tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_FIELD_HTML",
									isRequired, regFldYsmc, regFieldId, "readonly=\"true\"", "", regDefValue, imgPath);
						}

						// TZ_COUNTRY;
						if ("TZ_COUNTRY".equals(regFieldId)) {
							img = "<img src=\"" + imgPath
									+ "/chazhao.png\" class=\"serch-ico\" id=\"TZ_COUNTRY_click\"/>";
							fields = fields + tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_FIELD_HTML2",
									isRequired, regFldYsmc, regFieldId, "readonly=\"true\"", "", regDefValue, imgPath);
						}

						// TZ_SCH_CNAME;
						if ("TZ_SCH_CNAME".equals(regFieldId)) {
							img = "<img src=\"" + imgPath
									+ "/chazhao.png\" class=\"serch-ico\" id=\"TZ_SCH_CNAME_click\"/>";
							fields = fields + tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_FIELD_HTML3",
									isRequired, regFldYsmc, regFieldId, "readonly=\"true\"", "", regDefValue, imgPath,
									regFieldId + "_Country", regFieldId + "_Country", "");
						}

						// TZ_LEN_PROID;
						if ("TZ_LEN_PROID".equals(regFieldId)) {
							img = "<img src=\"" + imgPath
									+ "/chazhao.png\" class=\"serch-ico\" id=\"TZ_LEN_PROID_click\"/>";
							fields = fields + tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_FIELD_HTML2",
									isRequired, regFldYsmc, regFieldId, "readonly=\"true\"", "", regDefValue, imgPath);
						}

						// TZ_LEN_CITY;
						if ("TZ_LEN_CITY".equals(regFieldId)) {
							img = "<img src=\"" + imgPath
									+ "/chazhao.png\" class=\"serch-ico\" id=\"TZ_LEN_CITY_click\"/>";
							fields = fields + tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_FIELD_HTML2",
									isRequired, regFldYsmc, regFieldId, "readonly=\"true\"", "", regDefValue, imgPath);
						}

						// 如果启用面试申请号，则注册页面隐藏一个面试申请号字段，程序自动生成面试申请号;
						if ("TZ_MSSQH".equals(regFieldId)) {
							fields = fields + tzGdObject.getHTMLText(
									"HTML.TZWebSiteRegisteBundle.TZ_GD_HIDE_FIELD_HTML", regFieldId, "CREATE");
						}

					} else {
						// 是否下拉框;
						String fieldType = (String) map.get("TZ_FIELD_TYPE");
						if ("DROP".equals(fieldType)) {
							// 如果是项目则动态加载;
							if ("TZ_PROJECT".equals(regFieldId)) {
								prjJs = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_PRJ_SELECT_JS",
										strSiteId);
							} else {
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
										new Object[] { strLang, strSiteId, regFieldId });

								for (int j = 0; j < dropList.size(); j++) {
									String optId = (String) dropList.get(j).get("TZ_OPT_ID");
									String optValue = (String) dropList.get(j).get("TZ_OPT_VALUE");
									String optEngValue = (String) dropList.get(j).get("TZ_OPT_EN_VALUE");
									if (optEngValue == null || "".equals(optEngValue)) {
										optEngValue = optValue;
									}
									String selectFlg = (String) dropList.get(j).get("TZ_SELECT_FLG");
									if ("ENG".equals(strLang)) {
										if ("Y".equals(selectFlg) || "1".equals(selectFlg)) {
											combox = combox + "<option value =\"" + optId + "\" selected=\"selected\">"
													+ optEngValue + "</option>";
										} else {
											combox = combox + "<option value =\"" + optId + "\">" + optEngValue
													+ "</option>";
										}
									} else {
										if ("Y".equals(selectFlg) || "1".equals(selectFlg)) {
											combox = combox + "<option value =\"" + optId + "\" selected=\"selected\">"
													+ optValue + "</option>";
										} else {
											combox = combox + "<option value =\"" + optId + "\">" + optValue
													+ "</option>";
										}
									}
								}
							}

							fields = fields + tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_COMBOXR_HTML",
									isRequired, regFldYsmc, regFieldId, combox, imgPath);
						} else {
							fields = fields + tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_FIELD_HTML",
									isRequired, regFldYsmc, regFieldId, "", "", regDefValue, imgPath);
						}
					}
				}

			}

			// 是否只是手机验证;
			boolean phoneBl = false;

			// 是否只是邮箱验证;
			boolean emailBl = false;
			String strActHtml = "";
			// String regMbSQL = "SELECT TZ_ACTIVATE_TYPE FROM
			// PS_TZ_USERREG_MB_T WHERE TZ_JG_ID=?";
			String regMbSQL = "SELECT TZ_ACTIVATE_TYPE FROM PS_TZ_USERREG_MB_T WHERE TZ_SITEI_ID=?";
			String strActType = jdbcTemplate.queryForObject(regMbSQL, new Object[] { strSiteId }, "String");
			if (strActType != null && !"".equals(strActType)) {
				if (strActType.indexOf("MOBILE") >= 0 && strActType.indexOf("EMAIL") >= 0) {
					System.out.println("strActType" + strActType);
					if ("ENG".equals(strLang)) {
						strActHtml = "<select name='yzfs' id='yzfs'  class='chosen-select combox_351px' style='width:351px;'><option value ='M'>Phone</option><option value ='E'>Email</option></select>";
						strActHtml = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_JHFS_ENG_HTML",
								strActHtml, imgPath);
					} else {
						System.out.println("注册页");
						strActHtml = "<select name='yzfs' id='yzfs'  class='chosen-select combox_351px' style='width:351px;'><option value ='M'>手机验证</option><option value ='E'>邮箱验证</option></select>";
						strActHtml = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_JHFS_ZHS_HTML",
								strActHtml, imgPath);
					}
				} else {
					if (strActType.indexOf("EMAIL") >= 0) {
						emailBl = true;
						if ("ENG".equals(strLang)) {
							strActHtml = "<input name='yzfs1' type='hidden' class='input_351px' id='yzfs1' value='Email' readonly='readonly'><input name='yzfs' type='hidden' class='input_351px' id='yzfs' value='E'>";
						} else {
							strActHtml = "<input name='yzfs1' type='hidden' class='input_351px' id='yzfs1' value='邮箱验证' readonly='readonly'><input name='yzfs' type='hidden' class='input_351px' id='yzfs' value='E'>";
						}
					} else {
						if (strActType.indexOf("MOBILE") >= 0) {
							phoneBl = true;
							if ("ENG".equals(strLang)) {
								strActHtml = "<input name='yzfs1' type='hidden' class='input_351px' id='yzfs1' value='Phone' readonly='readonly'><input name='yzfs' type='hidden' class='input_351px' id='yzfs' value='M'>";
							} else {
								strActHtml = "<input name='yzfs1' type='hidden' class='input_351px' id='yzfs1' value='手机验证' readonly='readonly'><input name='yzfs' type='hidden' class='input_351px' id='yzfs' value='M'>";
							}
						}
					}
				}
			}

			String emialYzDisplay = "", phoneYzDisplay = "";
			if (phoneBl) {
				emialYzDisplay = "style=\"display:none\"";
				phoneYzDisplay = "";
			} else if (emailBl) {
				emialYzDisplay = "";
				phoneYzDisplay = "style=\"display:none\"";
			} else {
				emialYzDisplay = "style=\"display:none\"";
				phoneYzDisplay = "";
			}

			// 登录页面链接;
			String loginUrl = request.getContextPath() + "/user/login/" + strJgid.toLowerCase() + "/" + strSiteId;

			// explorerHtml = explorerHtml.replaceAll("\\$", "\\\\\\$");
			if (prjJs.contains("$")) {
				prjJs = prjJs.replace("$", "\\$");
			}
			if ("ENG".equals(strLang)) {
				fields = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_REG_EN_HTML", fields, strJgid,
						strActHtml, imgPath, request.getContextPath(), loginUrl, emialYzDisplay, phoneYzDisplay, prjJs,
						strSiteId);
			} else {
				fields = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_REG_HTML", fields, strJgid,
						strActHtml, imgPath, request.getContextPath(), loginUrl, emialYzDisplay, phoneYzDisplay, prjJs,
						strSiteId);
			}

			// fields = tzGdObject.getHTMLText("HTML.test.test",
			// "test111","test222");
		} catch (TzSystemException e) {
			e.printStackTrace();
			fields = "";
		}
		return fields;
	}

	// 处理要发布的注册页内容,PS类：TZ_SITE_DECORATED_APP:TZ_SITE_MG_CLS;
	public String handleEnrollPage(String strSiteId) {
		String strContent = "";
		try {
			PsTzSiteiDefnTWithBLOBs psTzSiteiDefnT = psTzSiteiDefnTMapper.selectByPrimaryKey(strSiteId);
			if (psTzSiteiDefnT == null) {
				return "";
			}
			String strOrgId = psTzSiteiDefnT.getTzJgId();
			String skinId = psTzSiteiDefnT.getTzSkinId();
			String imgPath = getSysHardCodeVal.getWebsiteSkinsImgPath();
			imgPath = request.getContextPath() + imgPath + "/" + skinId;

			strContent = this.userRegister(strOrgId, strSiteId);
			strContent = strContent.replaceAll("\\$", "\\\\\\$");
			strContent = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_SETREGISTEPAGE_HTML", strContent,
					request.getContextPath());
			// 截取body内容
			int numCharstart = 0;
			int numCharend = 0;
			numCharstart = strContent.indexOf("<body");
			numCharend = strContent.indexOf("</body>", numCharstart);
			if (numCharstart >= 0 && numCharend > numCharstart) {
				strContent = strContent.substring(numCharstart, numCharend + 7);
			} else {
				strContent = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			strContent = "";
		}
		return strContent;
	}

	public boolean saveEnrollpage(String strReleasContent, String strSiteId, String[] errMsg) {
		try {
			PsTzSiteiDefnTWithBLOBs psTzSiteiDefnT = psTzSiteiDefnTMapper.selectByPrimaryKey(strSiteId);
			if (psTzSiteiDefnT != null) {
				String jgid = psTzSiteiDefnT.getTzJgId();
				String siteLang = psTzSiteiDefnT.getTzSiteLang();

				String doctypeHtml = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_REALEAS_DOCTYPE_HTML");
				String explorerHtml = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_PANDUAN_LIULANQI_HTML",
						request.getContextPath());
				// explorerHtml = explorerHtml.replaceAll("\\$", "\\\\\\$");

				String strReleasContent1 = doctypeHtml + "<html>"
						+ tzGdObject.getHTMLTextForDollar("HTML.TZWebSiteRegisteBundle.TZ_REALEAS_HEAD_HTML",
								explorerHtml, request.getContextPath())
						+ strReleasContent + "</html>";

				strReleasContent1 = objRep.repWelcome(strReleasContent1, "");
				strReleasContent1 = objRep.repSdkbar(strReleasContent1, "");
				strReleasContent1 = objRep.repSiteid(strReleasContent1, strSiteId);

				if (jgid != null && !"".equals(jgid)) {
					strReleasContent1 = objRep.repJgid(strReleasContent1, jgid.toUpperCase());
				} else {
					strReleasContent1 = objRep.repJgid(strReleasContent1, "");
				}

				if (siteLang != null && !"".equals(siteLang)) {
					strReleasContent1 = objRep.repLang(strReleasContent1, siteLang.toUpperCase());
				} else {
					strReleasContent1 = objRep.repLang(strReleasContent1, "");
				}

				String strReleasContent2 = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_REALEAS_DOCTYPE_HTML")
						+ "<html>" + tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_SAVE_HEAD_HTML",
								request.getContextPath())
						+ strReleasContent + "</html>";

				strReleasContent2 = objRep.repWelcome(strReleasContent2, "");
				strReleasContent2 = objRep.repSiteid(strReleasContent2, strSiteId);
				if (jgid != null && !"".equals(jgid)) {
					strReleasContent2 = objRep.repJgid(strReleasContent2, jgid.toUpperCase());
				} else {
					strReleasContent2 = objRep.repJgid(strReleasContent2, "");
				}

				if (siteLang != null && !"".equals(siteLang)) {
					strReleasContent2 = objRep.repLang(strReleasContent2, siteLang.toUpperCase());
				} else {
					strReleasContent2 = objRep.repLang(strReleasContent2, "");
				}

				psTzSiteiDefnT.setTzEnrollPrecode(strReleasContent1);
				psTzSiteiDefnT.setTzEnrollSavecode(strReleasContent2);
				int success = psTzSiteiDefnTMapper.updateByPrimaryKeySelective(psTzSiteiDefnT);
				if (success > 0) {
					return true;
				} else {
					errMsg[0] = "1";
					errMsg[1] = "更新失败！";
					return false;
				}
			} else {
				errMsg[0] = "1";
				errMsg[1] = "站点不存在！";
				return false;
			}

		} catch (Exception e) {
			errMsg[0] = "2";
			errMsg[1] = "站点注册页保存异常！";
			return false;
		}
	}

	public boolean releasEnrollpage(String strReleasContent, String strSiteId, String[] errMsg) {
		try {
			PsTzSiteiDefnTWithBLOBs psTzSiteiDefnT = psTzSiteiDefnTMapper.selectByPrimaryKey(strSiteId);
			if (psTzSiteiDefnT != null) {
				String jgid = psTzSiteiDefnT.getTzJgId();
				String siteLang = psTzSiteiDefnT.getTzSiteLang();
				String doctypeHtml = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_REALEAS_DOCTYPE_HTML");
				String explorerHtml = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_PANDUAN_LIULANQI_HTML",
						request.getContextPath());
				// explorerHtml = explorerHtml.replaceAll("\\$", "\\\\\\$");

				strReleasContent = doctypeHtml + "<html>"
						+ tzGdObject.getHTMLTextForDollar("HTML.TZWebSiteRegisteBundle.TZ_REALEAS_HEAD_HTML",
								explorerHtml, request.getContextPath())
						+ strReleasContent + "</html>";
				strReleasContent = objRep.repTitle(strReleasContent, strSiteId);
				strReleasContent = objRep.repCss(strReleasContent, strSiteId);
				strReleasContent = objRep.repWelcome(strReleasContent, "");
				strReleasContent = objRep.repSdkbar(strReleasContent, "");
				strReleasContent = objRep.repSiteid(strReleasContent, strSiteId);
				if (jgid != null && !"".equals(jgid)) {
					strReleasContent = objRep.repJgid(strReleasContent, jgid.toUpperCase());
				} else {
					strReleasContent = objRep.repJgid(strReleasContent, "");
				}

				if (siteLang != null && !"".equals(siteLang)) {
					strReleasContent = objRep.repLang(strReleasContent, siteLang.toUpperCase());
				} else {
					strReleasContent = objRep.repLang(strReleasContent, "");
				}
				psTzSiteiDefnT.setTzEnrollPrecode(strReleasContent);
				psTzSiteiDefnT.setTzEnrollPubcode(strReleasContent);
				int success = psTzSiteiDefnTMapper.updateByPrimaryKeySelective(psTzSiteiDefnT);
				if (success > 0) {
					PsTzUserregMbT psTzUserregMbT = psTzUserregMbTMapper.selectByPrimaryKey(strSiteId);
					String enrollDir = psTzUserregMbT.getTzEnrollDir();
					String dir = getSysHardCodeVal.getWebsiteEnrollPath();
					dir = request.getServletContext().getRealPath(dir);
					if (enrollDir == null || "".equals(enrollDir)) {
						dir = dir + File.separator + jgid.toLowerCase();
					} else {
						if ((dir.lastIndexOf(File.separator) + 1) != dir.length()) {
							dir = dir + File.separator + enrollDir;
						} else {
							dir = dir + enrollDir;
						}
					}
					// 一个机构下可以有多个站点
					dir = dir + File.separator + strSiteId;
					boolean bl = this.staticFile(strReleasContent, dir, "enroll.html", errMsg);
					return bl;
				} else {
					errMsg[0] = "1";
					errMsg[1] = "更新失败！";
					return false;
				}
			} else {
				errMsg[0] = "1";
				errMsg[1] = "站点不存在！！";
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "2";
			errMsg[1] = "站点注册页发布异常！";
			return false;
		}
	}

	// 生成用户注册页源代码，PS类：TZ_GD_USERMG_PKG:TZ_GD_USER_REG;
	public String userPerfect(String strJgid, String strSiteId) {
		String fields = "";
		try {
			// 得到皮肤图片的路径;
			PsTzSiteiDefnTWithBLOBs psTzSiteiDefnT = psTzSiteiDefnTMapper.selectByPrimaryKey(strSiteId);
			if (psTzSiteiDefnT == null) {
				return "";
			}
			String strLang = psTzSiteiDefnT.getTzSiteLang();
			String skinId = psTzSiteiDefnT.getTzSkinId();
			String imgPath = getSysHardCodeVal.getWebsiteSkinsImgPath();
			imgPath = request.getContextPath() + imgPath + "/" + skinId;

			// 注册加载报名项目js;
			String prjJs = "";

			// 获取要显示的字段;
			// String sql = "SELECT
			// TZ_REG_FIELD_ID,TZ_RED_FLD_YSMC,TZ_REG_FIELD_NAME,(SELECT
			// TZ_REG_FIELD_NAME FROM PS_TZ_REGFIELD_ENG WHERE
			// TZ_JG_ID=PT.TZ_JG_ID AND TZ_REG_FIELD_ID=PT.TZ_REG_FIELD_ID AND
			// LANGUAGE_CD=?)
			// TZ_REG_FIELD_ENG_NAME,TZ_IS_REQUIRED,TZ_SYSFIELD_FLAG,TZ_FIELD_TYPE,TZ_DEF_VAL
			// FROM PS_TZ_REG_FIELD_T PT WHERE TZ_ENABLE='Y' AND TZ_JG_ID=?
			// ORDER BY TZ_ORDER ASC";
			String sql = "SELECT TZ_REG_FIELD_ID,TZ_RED_FLD_YSMC,TZ_REG_FIELD_NAME,(SELECT TZ_REG_FIELD_NAME FROM PS_TZ_REGFIELD_ENG WHERE TZ_SITEI_ID=PT.TZ_SITEI_ID AND TZ_REG_FIELD_ID=PT.TZ_REG_FIELD_ID AND LANGUAGE_CD=?) TZ_REG_FIELD_ENG_NAME,TZ_IS_REQUIRED,TZ_SYSFIELD_FLAG,TZ_FIELD_TYPE,TZ_DEF_VAL FROM PS_TZ_REG_FIELD_T PT WHERE TZ_ENABLE='Y' AND TZ_IS_PREFECT='Y' AND TZ_SITEI_ID=? ORDER BY TZ_ORDER ASC";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[] { strLang, strSiteId });
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> map = list.get(i);

					String combox = "<option value =\"\">请选择</option>";
					String img = "";
					// 名称是否修改;
					String regFldYsmc = (String) map.get("TZ_RED_FLD_YSMC");
					String regFieldName = (String) map.get("TZ_REG_FIELD_NAME");
					String regFieldEngName = (String) map.get("TZ_REG_FIELD_ENG_NAME");
					if ("ENG".equals(strLang)) {
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
					if ("Y".equals(isRequired)) {
						isRequired = "*";
					} else {
						isRequired = "";
					}

					String regFieldId = (String) map.get("TZ_REG_FIELD_ID");
					String regDefValue = (String) map.get("TZ_DEF_VAL");
					if (regDefValue == null) {
						regDefValue = "";
					} else {
						regDefValue = regDefValue.trim();
					}

					ArrayList<String> fieldsArr = new ArrayList<>();
					// fieldsArr.add("TZ_PASSWORD");
					// fieldsArr.add("TZ_REPASSWORD");
					fieldsArr.add("TZ_GENDER");
					fieldsArr.add("BIRTHDATE");
					fieldsArr.add("TZ_COUNTRY");
					fieldsArr.add("TZ_SCH_CNAME");
					fieldsArr.add("TZ_LEN_PROID");
					fieldsArr.add("TZ_LEN_CITY");
					fieldsArr.add("TZ_MSSQH");

					if (fieldsArr.contains(regFieldId)) {
						// 密码
						/*
						 * if("TZ_PASSWORD".equals(regFieldId) ||
						 * "TZ_REPASSWORD".equals(regFieldId)){ fields = fields
						 * + tzGdObject.getHTMLText(
						 * "HTML.TZWebSiteRegisteBundle.TZ_GD_PASSWORD_HTML",
						 * "*", regFldYsmc, regFieldId,regDefValue,imgPath ); }
						 */

						// 性别;
						if ("TZ_GENDER".equals(regFieldId)) {
							String str1 = "";
							String str2 = "";
							if ("M".equals(regDefValue)) {
								str1 = "checked=\"checked\"";
							}

							if ("F".equals(regDefValue)) {
								str2 = "checked=\"checked\"";
							}

							if ("ENG".equals(strLang)) {
								fields = fields
										+ tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_SEX_EN_HTML",
												isRequired, regFldYsmc, regFieldId, str1, str2);
							} else {
								fields = fields + tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_SEX_HTML",
										isRequired, regFldYsmc, regFieldId, str1, str2);
							}
						}

						// BIRTHDATE;
						if ("BIRTHDATE".equals(regFieldId)) {
							fields = fields + tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_FIELD_HTML",
									isRequired, regFldYsmc, regFieldId, "readonly=\"true\"", "", regDefValue, imgPath);
						}

						// TZ_COUNTRY;
						if ("TZ_COUNTRY".equals(regFieldId)) {
							img = "<img src=\"" + imgPath
									+ "/chazhao.png\" class=\"serch-ico\" id=\"TZ_COUNTRY_click\"/>";
							fields = fields + tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_FIELD_HTML2",
									isRequired, regFldYsmc, regFieldId, "readonly=\"true\"", img, regDefValue, imgPath);
						}

						// TZ_SCH_CNAME;
						if ("TZ_SCH_CNAME".equals(regFieldId)) {
							img = "<img src=\"" + imgPath
									+ "/chazhao.png\" class=\"serch-ico\" id=\"TZ_SCH_CNAME_click\"/>";
							fields = fields + tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_FIELD_HTML3",
									isRequired, regFldYsmc, regFieldId, "readonly=\"true\"", img, regDefValue, imgPath,
									regFieldId + "_Country", regFieldId + "_Country", "");
						}

						// TZ_LEN_PROID;
						if ("TZ_LEN_PROID".equals(regFieldId)) {
							img = "<img src=\"" + imgPath
									+ "/chazhao.png\" class=\"serch-ico\" id=\"TZ_LEN_PROID_click\"/>";
							fields = fields + tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_FIELD_HTML2",
									isRequired, regFldYsmc, regFieldId, "readonly=\"true\"", img, regDefValue, imgPath);
						}

						// TZ_LEN_CITY;
						if ("TZ_LEN_CITY".equals(regFieldId)) {
							img = "<img src=\"" + imgPath
									+ "/chazhao.png\" class=\"serch-ico\" id=\"TZ_LEN_CITY_click\"/>";
							fields = fields + tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_FIELD_HTML2",
									isRequired, regFldYsmc, regFieldId, "readonly=\"true\"", img, regDefValue, imgPath);
						}

						// 如果启用面试申请号，则注册页面隐藏一个面试申请号字段，程序自动生成面试申请号;
						if ("TZ_MSSQH".equals(regFieldId)) {
							fields = fields + tzGdObject.getHTMLText(
									"HTML.TZWebSiteRegisteBundle.TZ_GD_HIDE_FIELD_HTML", regFieldId, "CREATE");
						}

					} else {
						// 是否下拉框;
						String fieldType = (String) map.get("TZ_FIELD_TYPE");
						if ("DROP".equals(fieldType)) {
							// 如果是项目则动态加载;
							if ("TZ_PROJECT".equals(regFieldId)) {
								prjJs = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_PRJ_SELECT_JS",
										strSiteId);
							} else {
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
										new Object[] { strLang, strSiteId, regFieldId });

								for (int j = 0; j < dropList.size(); j++) {
									String optId = (String) dropList.get(j).get("TZ_OPT_ID");
									String optValue = (String) dropList.get(j).get("TZ_OPT_VALUE");
									String optEngValue = (String) dropList.get(j).get("TZ_OPT_EN_VALUE");
									if (optEngValue == null || "".equals(optEngValue)) {
										optEngValue = optValue;
									}
									String selectFlg = (String) dropList.get(j).get("TZ_SELECT_FLG");
									if ("ENG".equals(strLang)) {
										if ("Y".equals(selectFlg) || "1".equals(selectFlg)) {
											combox = combox + "<option value =\"" + optId + "\" selected=\"selected\">"
													+ optEngValue + "</option>";
										} else {
											combox = combox + "<option value =\"" + optId + "\">" + optEngValue
													+ "</option>";
										}
									} else {
										if ("Y".equals(selectFlg) || "1".equals(selectFlg)) {
											combox = combox + "<option value =\"" + optId + "\" selected=\"selected\">"
													+ optValue + "</option>";
										} else {
											combox = combox + "<option value =\"" + optId + "\">" + optValue
													+ "</option>";
										}
									}
								}
							}
							fields = fields + tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_COMBOXR_HTML",
									isRequired, regFldYsmc, regFieldId, combox, imgPath);
						} else {
							fields = fields + tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_FIELD_HTML",
									isRequired, regFldYsmc, regFieldId, "", "", regDefValue, imgPath);
						}
					}
				}

			}

			// 是否只是手机验证;
			boolean phoneBl = false;

			String strActHtml = "";
			// String regMbSQL = "SELECT TZ_ACTIVATE_TYPE FROM
			// PS_TZ_USERREG_MB_T WHERE TZ_JG_ID=?";
			String regMbSQL = "SELECT TZ_ACTIVATE_TYPE FROM PS_TZ_USERREG_MB_T WHERE TZ_SITEI_ID=?";
			String strActType = jdbcTemplate.queryForObject(regMbSQL, new Object[] { strSiteId }, "String");
			// System.out.println("strActType"+strActType);
			if (strActType != null && !"".equals(strActType)) {
				if (strActType.indexOf("MOBILE") >= 0 && strActType.indexOf("EMAIL") >= 0) {
					if ("ENG".equals(strLang)) {
						strActHtml = "<select name='yzfs' id='yzfs'  class='chosen-select combox_351px' style='width:351px;'><option value ='E'>Email</option><option value ='M'>Phone</option></select>";
						strActHtml = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_JHFS_ENG_HTML",
								strActHtml, imgPath);
					} else {
						strActHtml = "<select name='yzfs' id='yzfs'  class='chosen-select combox_351px' style='width:351px;'><option value ='M'>手机验证</option><option value ='E'>邮箱验证</option></select>";
						strActHtml = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_JHFS_ZHS_HTML",
								strActHtml, imgPath);
					}
				} else {
					if (strActType.indexOf("EMAIL") >= 0) {
						if ("ENG".equals(strLang)) {
							strActHtml = "<input name='yzfs1' type='hidden' class='input_351px' id='yzfs1' value='Email' readonly='readonly'><input name='yzfs' type='hidden' class='input_351px' id='yzfs' value='E'>";
						} else {
							strActHtml = "<input name='yzfs1' type='hidden' class='input_351px' id='yzfs1' value='邮箱验证' readonly='readonly'><input name='yzfs' type='hidden' class='input_351px' id='yzfs' value='E'>";
						}
					} else {
						if (strActType.indexOf("MOBILE") >= 0) {
							phoneBl = true;
							if ("ENG".equals(strLang)) {
								strActHtml = "<input name='yzfs1' type='hidden' class='input_351px' id='yzfs1' value='Phone' readonly='readonly'><input name='yzfs' type='hidden' class='input_351px' id='yzfs' value='M'>";
							} else {
								strActHtml = "<input name='yzfs1' type='hidden' class='input_351px' id='yzfs1' value='手机验证' readonly='readonly'><input name='yzfs' type='hidden' class='input_351px' id='yzfs' value='M'>";
							}
						}
					}
				}
			}
			String emialYzDisplay = "", phoneYzDisplay = "";
			if (phoneBl) {
				emialYzDisplay = "style=\"display:none\"";
				phoneYzDisplay = "";
			} else {
				emialYzDisplay = "";
				phoneYzDisplay = "style=\"display:none\"";
			}

			// 登录页面链接;
			String loginUrl = request.getContextPath() + "/user/login/" + strJgid.toLowerCase() + "/" + strSiteId;

			// explorerHtml = explorerHtml.replaceAll("\\$", "\\\\\\$");
			if (prjJs.contains("$")) {
				prjJs = prjJs.replace("$", "\\$");
			}
			if ("ENG".equals(strLang)) {
				fields = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_PERFECT_EN_HTML", fields, strJgid,
						strActHtml, imgPath, request.getContextPath(), loginUrl, emialYzDisplay, phoneYzDisplay, prjJs,
						strSiteId);
			} else {
				fields = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_PERFECT_HTML", fields, strJgid,
						strActHtml, imgPath, request.getContextPath(), loginUrl, emialYzDisplay, phoneYzDisplay, prjJs,
						strSiteId);
			}
			// fields = tzGdObject.getHTMLText("HTML.test.test",
			// "test111","test222");
		} catch (TzSystemException e) {
			e.printStackTrace();
			fields = "";
		}
		return fields;
	}

	// 处理要发布的注册页内容,PS类：TZ_SITE_DECORATED_APP:TZ_SITE_MG_CLS;
	public String handlePerfectPage(String strSiteId) {
		String strContent = "";
		try {
			PsTzSiteiDefnTWithBLOBs psTzSiteiDefnT = psTzSiteiDefnTMapper.selectByPrimaryKey(strSiteId);
			if (psTzSiteiDefnT == null) {
				return "";
			}
			String strOrgId = psTzSiteiDefnT.getTzJgId();
			String skinId = psTzSiteiDefnT.getTzSkinId();
			String imgPath = getSysHardCodeVal.getWebsiteSkinsImgPath();
			imgPath = request.getContextPath() + imgPath + "/" + skinId;

			strContent = this.userPerfect(strOrgId, strSiteId);
			strContent = strContent.replaceAll("\\$", "\\\\\\$");
			strContent = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_SETREGISTEPAGE_HTML", strContent,
					request.getContextPath());
			// 截取body内容
			int numCharstart = 0;
			int numCharend = 0;
			numCharstart = strContent.indexOf("<body");
			numCharend = strContent.indexOf("</body>", numCharstart);
			if (numCharstart >= 0 && numCharend > numCharstart) {
				strContent = strContent.substring(numCharstart, numCharend + 7);
			} else {
				strContent = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			strContent = "";
		}
		return strContent;
	}

	public boolean savePerfectpage(String strReleasContent, String strSiteId, String[] errMsg) {
		try {
			PsTzSiteiDefnTWithBLOBs psTzSiteiDefnT = psTzSiteiDefnTMapper.selectByPrimaryKey(strSiteId);
			if (psTzSiteiDefnT != null) {
				String jgid = psTzSiteiDefnT.getTzJgId();
				String siteLang = psTzSiteiDefnT.getTzSiteLang();

				String doctypeHtml = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_REALEAS_DOCTYPE_HTML");
				String explorerHtml = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_PANDUAN_LIULANQI_HTML",
						request.getContextPath());
				// explorerHtml = explorerHtml.replaceAll("\\$", "\\\\\\$");

				String strReleasContent1 = doctypeHtml + "<html>"
						+ tzGdObject.getHTMLTextForDollar("HTML.TZWebSiteRegisteBundle.TZ_REALEAS_HEAD_HTML",
								explorerHtml, request.getContextPath())
						+ strReleasContent + "</html>";

				strReleasContent1 = objRep.repWelcome(strReleasContent1, "");
				strReleasContent1 = objRep.repSdkbar(strReleasContent1, "");
				strReleasContent1 = objRep.repSiteid(strReleasContent1, strSiteId);

				if (jgid != null && !"".equals(jgid)) {
					strReleasContent1 = objRep.repJgid(strReleasContent1, jgid.toUpperCase());
				} else {
					strReleasContent1 = objRep.repJgid(strReleasContent1, "");
				}

				if (siteLang != null && !"".equals(siteLang)) {
					strReleasContent1 = objRep.repLang(strReleasContent1, siteLang.toUpperCase());
				} else {
					strReleasContent1 = objRep.repLang(strReleasContent1, "");
				}

				String strReleasContent2 = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_REALEAS_DOCTYPE_HTML")
						+ "<html>" + tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_SAVE_HEAD_HTML",
								request.getContextPath())
						+ strReleasContent + "</html>";

				strReleasContent2 = objRep.repWelcome(strReleasContent2, "");
				strReleasContent2 = objRep.repSiteid(strReleasContent2, strSiteId);
				if (jgid != null && !"".equals(jgid)) {
					strReleasContent2 = objRep.repJgid(strReleasContent2, jgid.toUpperCase());
				} else {
					strReleasContent2 = objRep.repJgid(strReleasContent2, "");
				}

				if (siteLang != null && !"".equals(siteLang)) {
					strReleasContent2 = objRep.repLang(strReleasContent2, siteLang.toUpperCase());
				} else {
					strReleasContent2 = objRep.repLang(strReleasContent2, "");
				}

				psTzSiteiDefnT.setTzPerfectPrecode(strReleasContent1);
				psTzSiteiDefnT.setTzPerfectSavecode(strReleasContent1);
				int success = psTzSiteiDefnTMapper.updateByPrimaryKeySelective(psTzSiteiDefnT);
				if (success > 0) {
					return true;
				} else {
					errMsg[0] = "1";
					errMsg[1] = "更新失败！";
					return false;
				}
			} else {
				errMsg[0] = "1";
				errMsg[1] = "站点不存在！";
				return false;
			}

		} catch (Exception e) {
			errMsg[0] = "2";
			errMsg[1] = "站点注册页保存异常！";
			return false;
		}
	}

	/**
	 * 发布完善信息页面，2-17-01-16，yuds
	 * 
	 * @param strReleasContent
	 * @param strSiteId
	 * @param errMsg
	 * @return
	 */
	public boolean releasPerfectpage(String strReleasContent, String strSiteId, String[] errMsg) {
		try {
			PsTzSiteiDefnTWithBLOBs psTzSiteiDefnT = psTzSiteiDefnTMapper.selectByPrimaryKey(strSiteId);
			if (psTzSiteiDefnT != null) {
				String jgid = psTzSiteiDefnT.getTzJgId();
				String siteLang = psTzSiteiDefnT.getTzSiteLang();
				String doctypeHtml = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_REALEAS_DOCTYPE_HTML");
				String explorerHtml = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_PANDUAN_LIULANQI_HTML",
						request.getContextPath());
				// explorerHtml = explorerHtml.replaceAll("\\$", "\\\\\\$");

				strReleasContent = doctypeHtml + "<html>"
						+ tzGdObject.getHTMLTextForDollar("HTML.TZWebSiteRegisteBundle.TZ_REALEAS_HEAD_HTML",
								explorerHtml, request.getContextPath())
						+ strReleasContent + "</html>";
				strReleasContent = objRep.repTitle(strReleasContent, strSiteId);
				strReleasContent = objRep.repCss(strReleasContent, strSiteId);
				strReleasContent = objRep.repWelcome(strReleasContent, "");
				strReleasContent = objRep.repSdkbar(strReleasContent, "");
				strReleasContent = objRep.repSiteid(strReleasContent, strSiteId);
				if (jgid != null && !"".equals(jgid)) {
					strReleasContent = objRep.repJgid(strReleasContent, jgid.toUpperCase());
				} else {
					strReleasContent = objRep.repJgid(strReleasContent, "");
				}

				if (siteLang != null && !"".equals(siteLang)) {
					strReleasContent = objRep.repLang(strReleasContent, siteLang.toUpperCase());
				} else {
					strReleasContent = objRep.repLang(strReleasContent, "");
				}
				psTzSiteiDefnT.setTzPerfectPrecode(strReleasContent);
				psTzSiteiDefnT.setTzPerfectPubcode(strReleasContent);
				int success = psTzSiteiDefnTMapper.updateByPrimaryKeySelective(psTzSiteiDefnT);
				if (success > 0) {
					PsTzUserregMbT psTzUserregMbT = psTzUserregMbTMapper.selectByPrimaryKey(strSiteId);
					String enrollDir = psTzUserregMbT.getTzEnrollDir();
					String dir = getSysHardCodeVal.getWebsiteEnrollPath();
					dir = request.getServletContext().getRealPath(dir);
					if (enrollDir == null || "".equals(enrollDir)) {
						dir = dir + File.separator + jgid.toLowerCase();
					} else {
						if ((dir.lastIndexOf(File.separator) + 1) != dir.length()) {
							dir = dir + File.separator + enrollDir;
						} else {
							dir = dir + enrollDir;
						}
					}
					// 一个机构可以有多个站点
					dir = dir + File.separator + strSiteId;
					boolean bl = this.staticFile(strReleasContent, dir, "perfect.html", errMsg);
					return bl;
				} else {
					errMsg[0] = "1";
					errMsg[1] = "更新失败！";
					return false;
				}
			} else {
				errMsg[0] = "1";
				errMsg[1] = "站点不存在！！";
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "2";
			errMsg[1] = "站点完善信息页发布异常！";
			return false;
		}
	}

	// 生成用户注册页源代码，PS类：TZ_GD_USERMG_PKG:TZ_GD_USER_REG;
	public String userMRegister(String strJgid, String strSiteId) {
		String fields = "";
		try {
			String inputPlaceholeder = "请输入";
			String selectPlaceholeder = "请选择";
			// 得到皮肤图片的路径;
			PsTzSiteiDefnTWithBLOBs psTzSiteiDefnT = psTzSiteiDefnTMapper.selectByPrimaryKey(strSiteId);
			if (psTzSiteiDefnT == null) {
				return "";
			}
			String strLang = psTzSiteiDefnT.getTzSiteLang();
			String skinId = psTzSiteiDefnT.getTzSkinId();
			String imgPath = getSysHardCodeVal.getWebsiteSkinsImgPath();
			imgPath = request.getContextPath() + imgPath + "/" + skinId;

			// 注册加载报名项目js;
			String prjJs = "";

			// 获取要显示的字段;
			String sql = "SELECT TZ_REG_FIELD_ID,TZ_RED_FLD_YSMC,TZ_REG_FIELD_NAME,(SELECT TZ_REG_FIELD_NAME FROM PS_TZ_REGFIELD_ENG WHERE TZ_SITEI_ID=PT.TZ_SITEI_ID AND TZ_REG_FIELD_ID=PT.TZ_REG_FIELD_ID AND LANGUAGE_CD=?) TZ_REG_FIELD_ENG_NAME,TZ_IS_REQUIRED,TZ_SYSFIELD_FLAG,TZ_FIELD_TYPE,TZ_DEF_VAL FROM PS_TZ_REG_FIELD_T PT WHERE TZ_ENABLE='Y' AND TZ_IS_REG='Y' AND TZ_SITEI_ID=? ORDER BY TZ_ORDER ASC";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[] { strLang, strSiteId });
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> map = list.get(i);

					String combox = "<option value =\"\">请选择</option>";
					String img = "";
					// 名称是否修改;
					String regFldYsmc = (String) map.get("TZ_RED_FLD_YSMC");
					String regFieldName = (String) map.get("TZ_REG_FIELD_NAME");
					String regFieldEngName = (String) map.get("TZ_REG_FIELD_ENG_NAME");
					if ("ENG".equals(strLang)) {
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
					String regWarm = "";
					if ("Y".equals(isRequired)) {
						isRequired = "*";
						regWarm = "<img class='reg_right' src='" + request.getContextPath()
								+ "/statics/css/website/m/images/reg_warm.png'>";
					} else {
						isRequired = "";
					}

					String regFieldId = (String) map.get("TZ_REG_FIELD_ID");
					String regDefValue = (String) map.get("TZ_DEF_VAL");
					if (regDefValue == null) {
						regDefValue = "";
					} else {
						regDefValue = regDefValue.trim();
					}

					ArrayList<String> fieldsArr = new ArrayList<>();
					fieldsArr.add("TZ_EMAIL");
					fieldsArr.add("TZ_MOBILE");
					fieldsArr.add("TZ_PASSWORD");
					fieldsArr.add("TZ_REPASSWORD");
					fieldsArr.add("TZ_GENDER");
					fieldsArr.add("BIRTHDATE");
					fieldsArr.add("TZ_COUNTRY");
					fieldsArr.add("TZ_SCH_CNAME");
					fieldsArr.add("TZ_LEN_PROID");
					fieldsArr.add("TZ_LEN_CITY");
					fieldsArr.add("TZ_MSSQH");

					if (fieldsArr.contains(regFieldId)) {
						if (("TZ_EMAIL").equals(regFieldId) || "TZ_MOBILE".equals(regFieldId)) {
							fields = fields + tzGdObject.getHTMLText("HTML.TZWebSiteMRegisteBundle.TZ_GD_MFIELD_HTML",
									regFldYsmc, isRequired, regFieldId, regWarm, selectPlaceholeder + regFldYsmc);
						}
						// 密码
						if ("TZ_PASSWORD".equals(regFieldId) || "TZ_REPASSWORD".equals(regFieldId)) {
							fields = fields
									+ tzGdObject.getHTMLText("HTML.TZWebSiteMRegisteBundle.TZ_GD_MPASSWORD_HTML",
											regFldYsmc, "*", regFieldId, regWarm, inputPlaceholeder + regFldYsmc);
						}

						// 性别;
						if ("TZ_GENDER".equals(regFieldId)) {
							String str1 = "";
							String str2 = "";
							if ("F".equals(regDefValue)) {
								str2 = "checked=\"checked\"";
							} else {
								str1 = "checked=\"checked\"";
							}
							if ("ENG".equals(strLang)) {
								fields = fields
										+ tzGdObject.getHTMLText("HTML.TZWebSiteMRegisteBundle.TZ_GD_MSEX_EN_HTML",
												regFldYsmc, isRequired, regFieldId, str1, str2);
							} else {
								fields = fields + tzGdObject.getHTMLText("HTML.TZWebSiteMRegisteBundle.TZ_GD_MSEX_HTML",
										regFldYsmc, isRequired, regFieldId, str1, str2);
							}
						}

						// BIRTHDATE;
						if ("BIRTHDATE".equals(regFieldId)) {
							fields = fields + tzGdObject.getHTMLText("HTML.TZWebSiteMRegisteBundle.TZ_GD_MFIELD_HTML",
									regFldYsmc, isRequired, regFieldId, regWarm, selectPlaceholeder + regFldYsmc);
						}

						// TZ_COUNTRY;
						if ("TZ_COUNTRY".equals(regFieldId)) {
							String countryClick = "TZ_COUNTRY_CLICK";
							fields = fields + tzGdObject.getHTMLText("HTML.TZWebSiteMRegisteBundle.TZ_GD_MFIELD_HTML2",
									regFldYsmc, isRequired, regFieldId, regWarm, selectPlaceholeder + regFldYsmc, "",
									countryClick);
						}

						// TZ_SCH_CNAME;
						if ("TZ_SCH_CNAME".equals(regFieldId)) {
							fields = fields + tzGdObject.getHTMLText("HTML.TZWebSiteMRegisteBundle.TZ_GD_MFIELD_HTML3",
									regFldYsmc, isRequired, regFieldId + "_Country", selectPlaceholeder + "院校国家",
									regFieldId, selectPlaceholeder + regFldYsmc, regWarm);
						}

						// TZ_LEN_PROID;
						if ("TZ_LEN_PROID".equals(regFieldId)) {
							String lenProidClick = "TZ_LEN_PROID_CLICK";
							fields = fields + tzGdObject.getHTMLText("HTML.TZWebSiteMRegisteBundle.TZ_GD_MFIELD_HTML2",
									regFldYsmc, isRequired, regFieldId, regWarm, inputPlaceholeder + regFldYsmc, "",
									lenProidClick);
						}

						// TZ_LEN_CITY;
						if ("TZ_LEN_CITY".equals(regFieldId)) {
							String lenCityClick = "TZ_LEN_CITY_CLICK";
							fields = fields + tzGdObject.getHTMLText("HTML.TZWebSiteMRegisteBundle.TZ_GD_MFIELD_HTML2",
									regFldYsmc, isRequired, regFieldId, regWarm, selectPlaceholeder + regFldYsmc, "",
									lenCityClick);
						}

						// 如果启用面试申请号，则注册页面隐藏一个面试申请号字段，程序自动生成面试申请号;
						if ("TZ_MSSQH".equals(regFieldId)) {
							fields = fields + tzGdObject.getHTMLText(
									"HTML.TZWebSiteMRegisteBundle.TZ_GD_HIDE_MFIELD_HTML", regFieldId, "CREATE");
						}

					} else {
						// 是否下拉框;
						String fieldType = (String) map.get("TZ_FIELD_TYPE");
						if ("DROP".equals(fieldType)) {
							// 如果是项目则动态加载;
							if ("TZ_PROJECT".equals(regFieldId)) {
								prjJs = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_PRJ_SELECT_JS",
										strSiteId);
							} else {
								String dropSQL = "SELECT TZ_OPT_ID,TZ_OPT_VALUE,(SELECT TZ_OPT_VALUE FROM PS_TZ_YHZC_XXZ_ENG WHERE TZ_SITEI_ID=PT.TZ_SITEI_ID AND TZ_REG_FIELD_ID=PT.TZ_REG_FIELD_ID AND TZ_OPT_ID=PT.TZ_OPT_ID AND LANGUAGE_CD=? ) TZ_OPT_EN_VALUE ,TZ_SELECT_FLG FROM PS_TZ_YHZC_XXZ_TBL PT WHERE TZ_SITEI_ID=? AND TZ_REG_FIELD_ID=? ORDER BY TZ_ORDER ASC";
								List<Map<String, Object>> dropList = jdbcTemplate.queryForList(dropSQL,
										new Object[] { strLang, strSiteId, regFieldId });

								for (int j = 0; j < dropList.size(); j++) {
									String optId = (String) dropList.get(j).get("TZ_OPT_ID");
									String optValue = (String) dropList.get(j).get("TZ_OPT_VALUE");
									String optEngValue = (String) dropList.get(j).get("TZ_OPT_EN_VALUE");
									if (optEngValue == null || "".equals(optEngValue)) {
										optEngValue = optValue;
									}
									String selectFlg = (String) dropList.get(j).get("TZ_SELECT_FLG");
									if ("ENG".equals(strLang)) {
										if ("Y".equals(selectFlg) || "1".equals(selectFlg)) {
											combox = combox + "<option value =\"" + optId + "\" selected=\"selected\">"
													+ optEngValue + "</option>";
										} else {
											combox = combox + "<option value =\"" + optId + "\">" + optEngValue
													+ "</option>";
										}
									} else {
										if ("Y".equals(selectFlg) || "1".equals(selectFlg)) {
											combox = combox + "<option value =\"" + optId + "\" selected=\"selected\">"
													+ optValue + "</option>";
										} else {
											combox = combox + "<option value =\"" + optId + "\">" + optValue
													+ "</option>";
										}
									}
								}
							}

							fields = fields + tzGdObject.getHTMLText("HTML.TZWebSiteMRegisteBundle.TZ_GD_MCOMBOXR_HTML",
									regFldYsmc, isRequired, regFieldId, combox, regWarm);
						} else {
							fields = fields + tzGdObject.getHTMLText("HTML.TZWebSiteMRegisteBundle.TZ_GD_MFIELD_HTML",
									regFldYsmc, isRequired, regFieldId, regWarm, inputPlaceholeder + regFldYsmc);
						}
					}
				}

			}

			// 是否只是手机验证;
			boolean phoneBl = true;

			String strActHtml = "";
			String regMbSQL = "SELECT TZ_ACTIVATE_TYPE FROM PS_TZ_USERREG_MB_T WHERE TZ_SITEI_ID=?";
			String strActType = jdbcTemplate.queryForObject(regMbSQL, new Object[] { strSiteId }, "String");
			if (strActType != null && !"".equals(strActType)) {
				if (strActType.indexOf("MOBILE") >= 0 && strActType.indexOf("EMAIL") >= 0) {
					if ("ENG".equals(strLang)) {
						strActHtml = "<select name='yzfs' id='yzfs' class='select_pros'><option value ='M'>Phone</option><option value ='E'>Email</option></select>";
						strActHtml = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_MJHFS_ENG_HTML",
								strActHtml, imgPath);
					} else {
						strActHtml = "<select name='yzfs' id='yzfs' class='select_pros'><option value ='M'>手机验证</option><option value ='E'>邮箱验证</option></select>";
						strActHtml = tzGdObject.getHTMLText("HTML.TZWebSiteMRegisteBundle.TZ_GD_MJHFS_ZHS_HTML",
								strActHtml, imgPath);
					}
				} else {
					if (strActType.indexOf("EMAIL") >= 0) {
						if ("ENG".equals(strLang)) {
							strActHtml = "<input name='yzfs1' type='hidden' id='yzfs1' value='Email' readonly='readonly'><input name='yzfs' type='hidden' id='yzfs' value='E'>";
						} else {
							strActHtml = "<input name='yzfs1' type='hidden' id='yzfs1' value='邮箱验证' readonly='readonly'><input name='yzfs' type='hidden' id='yzfs' value='E'>";
						}
					} else {
						if (strActType.indexOf("MOBILE") >= 0) {
							phoneBl = true;
							if ("ENG".equals(strLang)) {
								strActHtml = "<input name='yzfs1' type='hidden' id='yzfs1' value='Phone' readonly='readonly'><input name='yzfs' type='hidden' id='yzfs' value='M'>";
							} else {
								strActHtml = "<input name='yzfs1' type='hidden' id='yzfs1' value='手机验证' readonly='readonly'><input name='yzfs' type='hidden' id='yzfs' value='M'>";
							}
						}
					}
				}
			}

			String emialYzDisplay = "", phoneYzDisplay = "";
			if (phoneBl) {
				emialYzDisplay = "style=\"display:none\"";
				phoneYzDisplay = "";
			} else {
				emialYzDisplay = "";
				phoneYzDisplay = "style=\"display:none\"";
			}

			// 登录页面链接;
			String loginUrl = request.getContextPath() + "/user/login/" + strJgid.toLowerCase() + "/" + strSiteId;

			// explorerHtml = explorerHtml.replaceAll("\\$", "\\\\\\$");
			if (prjJs.contains("$")) {
				prjJs = prjJs.replace("$", "\\$");
			}
			if ("ENG".equals(strLang)) {
				fields = tzGdObject.getHTMLText("HTML.TZWebSiteMRegisteBundle.TZ_GD_MREG_EN_HTML",
						request.getContextPath(), fields, strActHtml, phoneYzDisplay, emialYzDisplay, loginUrl);
			} else {
				String JGID = jdbcTemplate.queryForObject("select TZ_JG_ID from PS_TZ_SITEI_DEFN_T WHERE TZ_SITEI_ID=?",
						new Object[] { strSiteId }, "String");

				if (JGID.equals("SEM")) {
					JGID = "";
				} else {
					JGID.toLowerCase();
				}
				System.out.println(this.getClass().getName() + ":" + JGID);
				fields = tzGdObject.getHTMLText("HTML.TZWebSiteMRegisteBundle.TZ_GD_MREG_HTML",
						request.getContextPath(), fields, strActHtml, phoneYzDisplay, emialYzDisplay, loginUrl, JGID);
			}

			// fields = tzGdObject.getHTMLText("HTML.test.test",
			// "test111","test222");
		} catch (TzSystemException e) {
			System.out.println(e.toString());
			e.printStackTrace();
			fields = "";
		}
		return fields;
	}

	// 处理要发布的注册页内容,PS类：TZ_SITE_DECORATED_APP:TZ_SITE_MG_CLS;
	public String handleMEnrollPage(String strSiteId) {
		String strContent = "";
		try {
			PsTzSiteiDefnTWithBLOBs psTzSiteiDefnT = psTzSiteiDefnTMapper.selectByPrimaryKey(strSiteId);
			if (psTzSiteiDefnT == null) {
				return "";
			}
			String strOrgId = psTzSiteiDefnT.getTzJgId();
			String skinId = psTzSiteiDefnT.getTzSkinId();
			String imgPath = getSysHardCodeVal.getWebsiteSkinsImgPath();
			imgPath = request.getContextPath() + imgPath + "/" + skinId;

			strContent = this.userMRegister(strOrgId, strSiteId);
			strContent = strContent.replaceAll("\\$", "\\\\\\$");
			strContent = tzGdObject.getHTMLText("HTML.TZWebSiteMRegisteBundle.TZ_SETMREGISTEPAGE_HTML", strContent,
					request.getContextPath());
			// 截取body内容
			int numCharstart = 0;
			int numCharend = 0;
			numCharstart = strContent.indexOf("<body");
			numCharend = strContent.indexOf("</body>", numCharstart);
			if (numCharstart >= 0 && numCharend > numCharstart) {
				strContent = strContent.substring(numCharstart, numCharend + 7);
			} else {
				strContent = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			strContent = "";
		}
		return strContent;
	}

	public boolean saveMEnrollpage(String strReleasContent, String strSiteId, String[] errMsg) {
		try {
			PsTzSiteiDefnTWithBLOBs psTzSiteiDefnT = psTzSiteiDefnTMapper.selectByPrimaryKey(strSiteId);
			if (psTzSiteiDefnT != null) {
				String jgid = psTzSiteiDefnT.getTzJgId();
				String siteLang = psTzSiteiDefnT.getTzSiteLang();

				String doctypeHtml = tzGdObject.getHTMLText("HTML.TZWebSiteMRegisteBundle.TZ_MREALEAS_DOCTYPE_HTML");

				String strReleasContent1 = doctypeHtml + "<html>" + tzGdObject
						.getHTMLText("HTML.TZWebSiteMRegisteBundle.TZ_MREALEAS_HEAD_HTML", "", request.getContextPath())
						+ strReleasContent + "</html>";

				strReleasContent1 = objRep.repWelcome(strReleasContent1, "");
				strReleasContent1 = objRep.repSdkbar(strReleasContent1, "");
				strReleasContent1 = objRep.repSiteid(strReleasContent1, strSiteId);

				if (jgid != null && !"".equals(jgid)) {
					strReleasContent1 = objRep.repJgid(strReleasContent1, jgid.toUpperCase());
				} else {
					strReleasContent1 = objRep.repJgid(strReleasContent1, "");
				}

				if (siteLang != null && !"".equals(siteLang)) {
					strReleasContent1 = objRep.repLang(strReleasContent1, siteLang.toUpperCase());
				} else {
					strReleasContent1 = objRep.repLang(strReleasContent1, "");
				}

				String sqlM = "SELECT COUNT(1) FROM PS_TZ_MSITEI_DEFN_T WHERE TZ_SITEI_ID=?";
				int count = jdbcTemplate.queryForObject(sqlM, new Object[] { strSiteId }, "int");
				int success = 0;
				if (count > 0) {
					PsTzMsiteiDefnTWithBLOBs psTzMSiteiDefnT = psTzMsiteiDefnTMapper.selectByPrimaryKey(strSiteId);

					psTzMSiteiDefnT.setTzMenrollPrecode(strReleasContent1);
					psTzMSiteiDefnT.setTzMenrollSavecode(strReleasContent1);
					success = psTzMsiteiDefnTMapper.updateByPrimaryKeySelective(psTzMSiteiDefnT);
				} else {
					PsTzMsiteiDefnTWithBLOBs psTzMSiteiDefnT = new PsTzMsiteiDefnTWithBLOBs();
					psTzMSiteiDefnT.setTzSiteiId(strSiteId);
					psTzMSiteiDefnT.setTzJgId(jgid);
					psTzMSiteiDefnT.setTzMenrollPrecode(strReleasContent1);
					psTzMSiteiDefnT.setTzMenrollSavecode(strReleasContent1);
					success = psTzMsiteiDefnTMapper.insert(psTzMSiteiDefnT);
				}
				if (success > 0) {
					return true;
				} else {
					errMsg[0] = "1";
					errMsg[1] = "更新失败！";
					return false;
				}
			} else {
				errMsg[0] = "1";
				errMsg[1] = "站点不存在！";
				return false;
			}

		} catch (Exception e) {
			errMsg[0] = "2";
			errMsg[1] = "站点手机注册页保存异常！";
			return false;
		}
	}

	public boolean releasMEnrollpage(String strReleasContent, String strSiteId, String[] errMsg) {
		try {
			PsTzSiteiDefnTWithBLOBs psTzSiteiDefnT = psTzSiteiDefnTMapper.selectByPrimaryKey(strSiteId);
			if (psTzSiteiDefnT != null) {
				String jgid = psTzSiteiDefnT.getTzJgId();
				String siteLang = psTzSiteiDefnT.getTzSiteLang();
				String doctypeHtml = tzGdObject.getHTMLText("HTML.TZWebSiteMRegisteBundle.TZ_MREALEAS_DOCTYPE_HTML");
				// explorerHtml = explorerHtml.replaceAll("\\$", "\\\\\\$");

				strReleasContent = doctypeHtml + "<html>"
						+ tzGdObject.getHTMLText("HTML.TZWebSiteMRegisteBundle.TZ_MREALEAS_HEAD_HTML", "",
								request.getContextPath(), jgid.toUpperCase(), strSiteId)
						+ strReleasContent + "</html>";
				strReleasContent = objRep.repTitle(strReleasContent, strSiteId);
				strReleasContent = objRep.repCss(strReleasContent, strSiteId);
				strReleasContent = objRep.repWelcome(strReleasContent, "");
				strReleasContent = objRep.repSdkbar(strReleasContent, "");
				strReleasContent = objRep.repSiteid(strReleasContent, strSiteId);
				if (jgid != null && !"".equals(jgid)) {
					strReleasContent = objRep.repJgid(strReleasContent, jgid.toUpperCase());
				} else {
					strReleasContent = objRep.repJgid(strReleasContent, "");
				}

				if (siteLang != null && !"".equals(siteLang)) {
					strReleasContent = objRep.repLang(strReleasContent, siteLang.toUpperCase());
				} else {
					strReleasContent = objRep.repLang(strReleasContent, "");
				}
				PsTzMsiteiDefnTWithBLOBs psTzMSiteiDefnT = psTzMsiteiDefnTMapper.selectByPrimaryKey(strSiteId);

				psTzMSiteiDefnT.setTzMenrollPrecode(strReleasContent);
				psTzMSiteiDefnT.setTzMenrollPubcode(strReleasContent);
				int success = psTzMsiteiDefnTMapper.updateByPrimaryKeySelective(psTzMSiteiDefnT);
				if (success > 0) {
					PsTzUserregMbT psTzUserregMbT = psTzUserregMbTMapper.selectByPrimaryKey(strSiteId);
					String enrollDir = psTzUserregMbT.getTzEnrollDir();
					String dir = getSysHardCodeVal.getWebsiteEnrollPath();
					dir = request.getServletContext().getRealPath(dir);
					if (enrollDir == null || "".equals(enrollDir)) {
						dir = dir + File.separator + jgid.toLowerCase();
					} else {
						if ((dir.lastIndexOf(File.separator) + 1) != dir.length()) {
							dir = dir + File.separator + enrollDir;
						} else {
							dir = dir + enrollDir;
						}
					}
					// 一个机构下可以有多个站点
					dir = dir + File.separator + strSiteId;
					boolean bl = this.staticFile(strReleasContent, dir, "menroll.html", errMsg);
					return bl;
				} else {
					errMsg[0] = "1";
					errMsg[1] = "更新失败！";
					return false;
				}
			} else {
				errMsg[0] = "1";
				errMsg[1] = "站点不存在！！";
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "2";
			errMsg[1] = "站点注册页发布异常！";
			return false;
		}
	}

	// 生成用户注册页源代码，PS类：TZ_GD_USERMG_PKG:TZ_GD_USER_REG;
	public String userMPerfect(String strJgid, String strSiteId) {
		String fields = "";
		try {
			String inputPlaceholeder = "请输入";
			String selectPlaceholeder = "请选择";
			// 得到皮肤图片的路径;
			PsTzSiteiDefnTWithBLOBs psTzSiteiDefnT = psTzSiteiDefnTMapper.selectByPrimaryKey(strSiteId);
			if (psTzSiteiDefnT == null) {
				return "";
			}
			String strLang = psTzSiteiDefnT.getTzSiteLang();
			String skinId = psTzSiteiDefnT.getTzSkinId();
			String imgPath = getSysHardCodeVal.getWebsiteSkinsImgPath();
			imgPath = request.getContextPath() + imgPath + "/" + skinId;

			// 注册加载报名项目js;
			String prjJs = "";

			// 获取要显示的字段;
			String sql = "SELECT TZ_REG_FIELD_ID,TZ_RED_FLD_YSMC,TZ_REG_FIELD_NAME,(SELECT TZ_REG_FIELD_NAME FROM PS_TZ_REGFIELD_ENG WHERE TZ_SITEI_ID=PT.TZ_SITEI_ID AND TZ_REG_FIELD_ID=PT.TZ_REG_FIELD_ID AND LANGUAGE_CD=?) TZ_REG_FIELD_ENG_NAME,TZ_IS_REQUIRED,TZ_SYSFIELD_FLAG,TZ_FIELD_TYPE,TZ_DEF_VAL FROM PS_TZ_REG_FIELD_T PT WHERE TZ_ENABLE='Y' AND TZ_IS_PREFECT='Y' AND TZ_SITEI_ID=? ORDER BY TZ_ORDER ASC";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[] { strLang, strSiteId });
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> map = list.get(i);

					String combox = "<option value =\"\">请选择</option>";
					String img = "";
					// 名称是否修改;
					String regFldYsmc = (String) map.get("TZ_RED_FLD_YSMC");
					String regFieldName = (String) map.get("TZ_REG_FIELD_NAME");
					String regFieldEngName = (String) map.get("TZ_REG_FIELD_ENG_NAME");
					if ("ENG".equals(strLang)) {
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
					String regWarm = "";
					if ("Y".equals(isRequired)) {
						isRequired = "*";
						regWarm = "<img class='reg_right' src='" + request.getContextPath()
								+ "/statics/css/website/m/images/reg_warm.png'>";
					} else {
						isRequired = "";
					}

					String regFieldId = (String) map.get("TZ_REG_FIELD_ID");
					String regDefValue = (String) map.get("TZ_DEF_VAL");
					if (regDefValue == null) {
						regDefValue = "";
					} else {
						regDefValue = regDefValue.trim();
					}

					ArrayList<String> fieldsArr = new ArrayList<>();
					fieldsArr.add("TZ_EMAIL");
					fieldsArr.add("TZ_MOBILE");
					fieldsArr.add("TZ_PASSWORD");
					fieldsArr.add("TZ_REPASSWORD");
					fieldsArr.add("TZ_GENDER");
					fieldsArr.add("BIRTHDATE");
					fieldsArr.add("TZ_COUNTRY");
					fieldsArr.add("TZ_SCH_CNAME");
					fieldsArr.add("TZ_LEN_PROID");
					fieldsArr.add("TZ_LEN_CITY");
					fieldsArr.add("TZ_MSSQH");

					if (fieldsArr.contains(regFieldId)) {
						if (("TZ_EMAIL").equals(regFieldId) || "TZ_MOBILE".equals(regFieldId)) {
							fields = fields + tzGdObject.getHTMLText("HTML.TZWebSiteMRegisteBundle.TZ_GD_MFIELD_HTML",
									regFldYsmc, isRequired, regFieldId, regWarm, selectPlaceholeder + regFldYsmc);
						}
						// 密码
						if ("TZ_PASSWORD".equals(regFieldId) || "TZ_REPASSWORD".equals(regFieldId)) {
							fields = fields
									+ tzGdObject.getHTMLText("HTML.TZWebSiteMRegisteBundle.TZ_GD_MPASSWORD_HTML",
											regFldYsmc, "*", regFieldId, regWarm, inputPlaceholeder + regFldYsmc);
						}

						// 性别;
						if ("TZ_GENDER".equals(regFieldId)) {
							String str1 = "";
							String str2 = "";
							if ("F".equals(regDefValue)) {
								str2 = "checked=\"checked\"";
							} else {
								str1 = "checked=\"checked\"";
							}
							if ("ENG".equals(strLang)) {
								fields = fields
										+ tzGdObject.getHTMLText("HTML.TZWebSiteMRegisteBundle.TZ_GD_MSEX_EN_HTML",
												regFldYsmc, isRequired, regFieldId, str1, str2);
							} else {
								fields = fields + tzGdObject.getHTMLText("HTML.TZWebSiteMRegisteBundle.TZ_GD_MSEX_HTML",
										regFldYsmc, isRequired, regFieldId, str1, str2);
							}
						}

						// BIRTHDATE;
						if ("BIRTHDATE".equals(regFieldId)) {
							fields = fields + tzGdObject.getHTMLText("HTML.TZWebSiteMRegisteBundle.TZ_GD_MFIELD_HTML",
									regFldYsmc, isRequired, regFieldId, regWarm, selectPlaceholeder + regFldYsmc);
						}

						// TZ_COUNTRY;
						if ("TZ_COUNTRY".equals(regFieldId)) {
							String countryClick = "TZ_COUNTRY_CLICK";
							fields = fields + tzGdObject.getHTMLText("HTML.TZWebSiteMRegisteBundle.TZ_GD_MFIELD_HTML2",
									regFldYsmc, isRequired, regFieldId, regWarm, selectPlaceholeder + regFldYsmc, "",
									countryClick);
						}

						// TZ_SCH_CNAME;
						if ("TZ_SCH_CNAME".equals(regFieldId)) {
							fields = fields + tzGdObject.getHTMLText("HTML.TZWebSiteMRegisteBundle.TZ_GD_MFIELD_HTML3",
									regFldYsmc, isRequired, regFieldId + "_Country", selectPlaceholeder + "院校国家",
									regFieldId, selectPlaceholeder + regFldYsmc, regWarm);
						}

						// TZ_LEN_PROID;
						if ("TZ_LEN_PROID".equals(regFieldId)) {
							String lenProidClick = "TZ_LEN_PROID_CLICK";
							fields = fields + tzGdObject.getHTMLText("HTML.TZWebSiteMRegisteBundle.TZ_GD_MFIELD_HTML2",
									regFldYsmc, isRequired, regFieldId, regWarm, inputPlaceholeder + regFldYsmc, "",
									lenProidClick);
						}

						// TZ_LEN_CITY;
						if ("TZ_LEN_CITY".equals(regFieldId)) {
							String lenCityClick = "TZ_LEN_CITY_CLICK";
							fields = fields + tzGdObject.getHTMLText("HTML.TZWebSiteMRegisteBundle.TZ_GD_MFIELD_HTML2",
									regFldYsmc, isRequired, regFieldId, regWarm, selectPlaceholeder + regFldYsmc, "",
									lenCityClick);
						}

						// 如果启用面试申请号，则注册页面隐藏一个面试申请号字段，程序自动生成面试申请号;
						if ("TZ_MSSQH".equals(regFieldId)) {
							fields = fields + tzGdObject.getHTMLText(
									"HTML.TZWebSiteMRegisteBundle.TZ_GD_HIDE_MFIELD_HTML", regFieldId, "CREATE");
						}

					} else {
						// 是否下拉框;
						String fieldType = (String) map.get("TZ_FIELD_TYPE");
						if ("DROP".equals(fieldType)) {
							// 如果是项目则动态加载;
							if ("TZ_PROJECT".equals(regFieldId)) {
								prjJs = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_PRJ_SELECT_JS",
										strSiteId);
							} else {
								String dropSQL = "SELECT TZ_OPT_ID,TZ_OPT_VALUE,(SELECT TZ_OPT_VALUE FROM PS_TZ_YHZC_XXZ_ENG WHERE TZ_SITEI_ID=PT.TZ_SITEI_ID AND TZ_REG_FIELD_ID=PT.TZ_REG_FIELD_ID AND TZ_OPT_ID=PT.TZ_OPT_ID AND LANGUAGE_CD=? ) TZ_OPT_EN_VALUE ,TZ_SELECT_FLG FROM PS_TZ_YHZC_XXZ_TBL PT WHERE TZ_SITEI_ID=? AND TZ_REG_FIELD_ID=? ORDER BY TZ_ORDER ASC";
								List<Map<String, Object>> dropList = jdbcTemplate.queryForList(dropSQL,
										new Object[] { strLang, strSiteId, regFieldId });

								for (int j = 0; j < dropList.size(); j++) {
									String optId = (String) dropList.get(j).get("TZ_OPT_ID");
									String optValue = (String) dropList.get(j).get("TZ_OPT_VALUE");
									String optEngValue = (String) dropList.get(j).get("TZ_OPT_EN_VALUE");
									if (optEngValue == null || "".equals(optEngValue)) {
										optEngValue = optValue;
									}
									String selectFlg = (String) dropList.get(j).get("TZ_SELECT_FLG");
									if ("ENG".equals(strLang)) {
										if ("Y".equals(selectFlg) || "1".equals(selectFlg)) {
											combox = combox + "<option value =\"" + optId + "\" selected=\"selected\">"
													+ optEngValue + "</option>";
										} else {
											combox = combox + "<option value =\"" + optId + "\">" + optEngValue
													+ "</option>";
										}
									} else {
										if ("Y".equals(selectFlg) || "1".equals(selectFlg)) {
											combox = combox + "<option value =\"" + optId + "\" selected=\"selected\">"
													+ optValue + "</option>";
										} else {
											combox = combox + "<option value =\"" + optId + "\">" + optValue
													+ "</option>";
										}
									}
								}
							}

							fields = fields + tzGdObject.getHTMLText("HTML.TZWebSiteMRegisteBundle.TZ_GD_MCOMBOXR_HTML",
									regFldYsmc, isRequired, regFieldId, combox, regWarm);
						} else {
							fields = fields + tzGdObject.getHTMLText("HTML.TZWebSiteMRegisteBundle.TZ_GD_MFIELD_HTML",
									regFldYsmc, isRequired, regFieldId, regWarm, inputPlaceholeder + regFldYsmc);
						}
					}
				}

			}

			// 是否只是手机验证;
			boolean phoneBl = true;

			String strActHtml = "";
			String regMbSQL = "SELECT TZ_ACTIVATE_TYPE FROM PS_TZ_USERREG_MB_T WHERE TZ_SITEI_ID=?";
			String strActType = jdbcTemplate.queryForObject(regMbSQL, new Object[] { strSiteId }, "String");
			if (strActType != null && !"".equals(strActType)) {
				if (strActType.indexOf("MOBILE") >= 0 && strActType.indexOf("EMAIL") >= 0) {
					if ("ENG".equals(strLang)) {
						strActHtml = "<select name='yzfs' id='yzfs' class='select_pros'><option value ='M'>Phone</option><option value ='E'>Email</option></select>";
						strActHtml = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_MJHFS_ENG_HTML",
								strActHtml, imgPath);
					} else {
						strActHtml = "<select name='yzfs' id='yzfs' class='select_pros'><option value ='M'>手机验证</option><option value ='E'>邮箱验证</option></select>";
						strActHtml = tzGdObject.getHTMLText("HTML.TZWebSiteMRegisteBundle.TZ_GD_MJHFS_ZHS_HTML",
								strActHtml, imgPath);
					}
				} else {
					if (strActType.indexOf("EMAIL") >= 0) {
						if ("ENG".equals(strLang)) {
							strActHtml = "<input name='yzfs1' type='hidden' id='yzfs1' value='Email' readonly='readonly'><input name='yzfs' type='hidden' id='yzfs' value='E'>";
						} else {
							strActHtml = "<input name='yzfs1' type='hidden' id='yzfs1' value='邮箱验证' readonly='readonly'><input name='yzfs' type='hidden' id='yzfs' value='E'>";
						}
					} else {
						if (strActType.indexOf("MOBILE") >= 0) {
							phoneBl = true;
							if ("ENG".equals(strLang)) {
								strActHtml = "<input name='yzfs1' type='hidden' id='yzfs1' value='Phone' readonly='readonly'><input name='yzfs' type='hidden' id='yzfs' value='M'>";
							} else {
								strActHtml = "<input name='yzfs1' type='hidden' id='yzfs1' value='手机验证' readonly='readonly'><input name='yzfs' type='hidden' id='yzfs' value='M'>";
							}
						}
					}
				}
			}

			String emialYzDisplay = "", phoneYzDisplay = "";
			if (phoneBl) {
				emialYzDisplay = "style=\"display:none\"";
				phoneYzDisplay = "";
			} else {
				emialYzDisplay = "";
				phoneYzDisplay = "style=\"display:none\"";
			}

			// 登录页面链接;
			String loginUrl = request.getContextPath() + "/user/login/" + strJgid.toLowerCase() + "/" + strSiteId;

			// explorerHtml = explorerHtml.replaceAll("\\$", "\\\\\\$");
			if (prjJs.contains("$")) {
				prjJs = prjJs.replace("$", "\\$");
			}
			if ("ENG".equals(strLang)) {
				fields = tzGdObject.getHTMLText("HTML.TZWebSiteMRegisteBundle.TZ_GD_MPERFECT_EN_HTML",
						request.getContextPath(), fields, strActHtml, phoneYzDisplay, emialYzDisplay, loginUrl);
			} else {
				String JGID = jdbcTemplate.queryForObject("select TZ_JG_ID from PS_TZ_SITEI_DEFN_T WHERE TZ_SITEI_ID=?",
						new Object[] { strSiteId }, "String");

				if (JGID.equals("SEM")) {
					JGID = "";
				} else {
					JGID.toLowerCase();
				}
				System.out.println(this.getClass().getName() + ":" + JGID);
				fields = tzGdObject.getHTMLText("HTML.TZWebSiteMRegisteBundle.TZ_GD_MPERFECT_HTML",
						request.getContextPath(), fields, strActHtml, phoneYzDisplay, emialYzDisplay, loginUrl, JGID);
			}

			// fields = tzGdObject.getHTMLText("HTML.test.test",
			// "test111","test222");
		} catch (TzSystemException e) {
			System.out.println(e.toString());
			e.printStackTrace();
			fields = "";
		}
		return fields;
	}

	// 处理要发布的注册页内容,PS类：TZ_SITE_DECORATED_APP:TZ_SITE_MG_CLS;
	public String handleMPerfectPage(String strSiteId) {
		String strContent = "";
		try {
			PsTzSiteiDefnTWithBLOBs psTzSiteiDefnT = psTzSiteiDefnTMapper.selectByPrimaryKey(strSiteId);
			if (psTzSiteiDefnT == null) {
				return "";
			}
			String strOrgId = psTzSiteiDefnT.getTzJgId();
			String skinId = psTzSiteiDefnT.getTzSkinId();
			String imgPath = getSysHardCodeVal.getWebsiteSkinsImgPath();
			imgPath = request.getContextPath() + imgPath + "/" + skinId;

			strContent = this.userMPerfect(strOrgId, strSiteId);
			strContent = strContent.replaceAll("\\$", "\\\\\\$");
			strContent = tzGdObject.getHTMLText("HTML.TZWebSiteMRegisteBundle.TZ_SETMREGISTEPAGE_HTML", strContent,
					request.getContextPath());
			// 截取body内容
			int numCharstart = 0;
			int numCharend = 0;
			numCharstart = strContent.indexOf("<body");
			numCharend = strContent.indexOf("</body>", numCharstart);
			if (numCharstart >= 0 && numCharend > numCharstart) {
				strContent = strContent.substring(numCharstart, numCharend + 7);
			} else {
				strContent = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			strContent = "";
		}
		return strContent;
	}

	public boolean saveMPerfectpage(String strReleasContent, String strSiteId, String[] errMsg) {
		try {
			PsTzSiteiDefnTWithBLOBs psTzSiteiDefnT = psTzSiteiDefnTMapper.selectByPrimaryKey(strSiteId);
			if (psTzSiteiDefnT != null) {
				String jgid = psTzSiteiDefnT.getTzJgId();
				String siteLang = psTzSiteiDefnT.getTzSiteLang();

				String doctypeHtml = tzGdObject.getHTMLText("HTML.TZWebSiteMRegisteBundle.TZ_MREALEAS_DOCTYPE_HTML");

				String strReleasContent1 = doctypeHtml + "<html>" + tzGdObject
						.getHTMLText("HTML.TZWebSiteMRegisteBundle.TZ_MREALEAS_HEAD_HTML", "", request.getContextPath())
						+ strReleasContent + "</html>";

				strReleasContent1 = objRep.repWelcome(strReleasContent1, "");
				strReleasContent1 = objRep.repSdkbar(strReleasContent1, "");
				strReleasContent1 = objRep.repSiteid(strReleasContent1, strSiteId);

				if (jgid != null && !"".equals(jgid)) {
					strReleasContent1 = objRep.repJgid(strReleasContent1, jgid.toUpperCase());
				} else {
					strReleasContent1 = objRep.repJgid(strReleasContent1, "");
				}

				if (siteLang != null && !"".equals(siteLang)) {
					strReleasContent1 = objRep.repLang(strReleasContent1, siteLang.toUpperCase());
				} else {
					strReleasContent1 = objRep.repLang(strReleasContent1, "");
				}

				String sqlM = "SELECT COUNT(1) FROM PS_TZ_MSITEI_DEFN_T WHERE TZ_SITEI_ID=?";
				int count = jdbcTemplate.queryForObject(sqlM, new Object[] { strSiteId }, "int");
				int success = 0;
				if (count > 0) {
					PsTzMsiteiDefnTWithBLOBs psTzMSiteiDefnT = psTzMsiteiDefnTMapper.selectByPrimaryKey(strSiteId);
					psTzMSiteiDefnT.setTzMperfectPrecode(strReleasContent1);
					psTzMSiteiDefnT.setTzMperfectSavecode(strReleasContent1);
					success = psTzMsiteiDefnTMapper.updateByPrimaryKeySelective(psTzMSiteiDefnT);
				} else {
					PsTzMsiteiDefnTWithBLOBs psTzMSiteiDefnT = new PsTzMsiteiDefnTWithBLOBs();
					psTzMSiteiDefnT.setTzSiteiId(strSiteId);
					psTzMSiteiDefnT.setTzJgId(jgid);
					psTzMSiteiDefnT.setTzMperfectPrecode(strReleasContent1);
					psTzMSiteiDefnT.setTzMperfectSavecode(strReleasContent1);
					success = psTzMsiteiDefnTMapper.insert(psTzMSiteiDefnT);
				}
				if (success > 0) {
					return true;
				} else {
					errMsg[0] = "1";
					errMsg[1] = "更新失败！";
					return false;
				}
			} else {
				errMsg[0] = "1";
				errMsg[1] = "站点不存在！";
				return false;
			}

		} catch (Exception e) {
			errMsg[0] = "2";
			errMsg[1] = "站点手机注册页保存异常！";
			return false;
		}
	}

	public boolean releasMPerfectpage(String strReleasContent, String strSiteId, String[] errMsg) {
		try {
			PsTzSiteiDefnTWithBLOBs psTzSiteiDefnT = psTzSiteiDefnTMapper.selectByPrimaryKey(strSiteId);
			if (psTzSiteiDefnT != null) {
				String jgid = psTzSiteiDefnT.getTzJgId();
				String siteLang = psTzSiteiDefnT.getTzSiteLang();
				String doctypeHtml = tzGdObject.getHTMLText("HTML.TZWebSiteMRegisteBundle.TZ_MREALEAS_DOCTYPE_HTML");
				// explorerHtml = explorerHtml.replaceAll("\\$", "\\\\\\$");

				strReleasContent = doctypeHtml + "<html>"
						+ tzGdObject.getHTMLText("HTML.TZWebSiteMRegisteBundle.TZ_MREALEAS_HEAD_HTML", "",
								request.getContextPath(), jgid.toUpperCase(), strSiteId)
						+ strReleasContent + "</html>";
				strReleasContent = objRep.repTitle(strReleasContent, strSiteId);
				strReleasContent = objRep.repCss(strReleasContent, strSiteId);
				strReleasContent = objRep.repWelcome(strReleasContent, "");
				strReleasContent = objRep.repSdkbar(strReleasContent, "");
				strReleasContent = objRep.repSiteid(strReleasContent, strSiteId);
				if (jgid != null && !"".equals(jgid)) {
					strReleasContent = objRep.repJgid(strReleasContent, jgid.toUpperCase());
				} else {
					strReleasContent = objRep.repJgid(strReleasContent, "");
				}

				if (siteLang != null && !"".equals(siteLang)) {
					strReleasContent = objRep.repLang(strReleasContent, siteLang.toUpperCase());
				} else {
					strReleasContent = objRep.repLang(strReleasContent, "");
				}
				PsTzMsiteiDefnTWithBLOBs psTzMSiteiDefnT = psTzMsiteiDefnTMapper.selectByPrimaryKey(strSiteId);

				psTzMSiteiDefnT.setTzMperfectPrecode(strReleasContent);
				psTzMSiteiDefnT.setTzMperfectPubcode(strReleasContent);
				int success = psTzMsiteiDefnTMapper.updateByPrimaryKeySelective(psTzMSiteiDefnT);
				if (success > 0) {
					PsTzUserregMbT psTzUserregMbT = psTzUserregMbTMapper.selectByPrimaryKey(strSiteId);
					String enrollDir = psTzUserregMbT.getTzEnrollDir();
					String dir = getSysHardCodeVal.getWebsiteEnrollPath();
					dir = request.getServletContext().getRealPath(dir);
					if (enrollDir == null || "".equals(enrollDir)) {
						dir = dir + File.separator + jgid.toLowerCase();
					} else {
						if ((dir.lastIndexOf(File.separator) + 1) != dir.length()) {
							dir = dir + File.separator + enrollDir;
						} else {
							dir = dir + enrollDir;
						}
					}
					// 一个机构下可以有多个站点
					dir = dir + File.separator + strSiteId;
					boolean bl = this.staticFile(strReleasContent, dir, "mperfect.html", errMsg);
					return bl;
				} else {
					errMsg[0] = "1";
					errMsg[1] = "更新失败！";
					return false;
				}
			} else {
				errMsg[0] = "1";
				errMsg[1] = "站点不存在！！";
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "2";
			errMsg[1] = "站点注册页发布异常！";
			return false;
		}
	}

	public boolean staticFile(String strReleasContent, String dir, String fileName, String[] errMsg) {
		try {
			System.out.println(dir);
			File fileDir = new File(dir);
			if (!fileDir.exists()) {
				fileDir.mkdirs();
			}

			String filePath = "";
			if ((dir.lastIndexOf(File.separator) + 1) != dir.length()) {
				filePath = dir + File.separator + fileName;
			} else {
				filePath = dir + fileName;
			}

			File file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(strReleasContent);
			bw.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "3";
			errMsg[1] = "静态化文件时异常！";
			return false;
		}
	}
}
