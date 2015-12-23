/**
 * 
 */
package com.tranzvision.gd.TZSitePageBundle.service.impl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzWebsiteLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 原PS：TZ_SITE_DECORATED_APP:TZ_PI_DECORATED_CLS
 * 
 * @author SHIHUA
 * @since 2015-12-17
 */
@Service("com.tranzvision.gd.TZSitePageBundle.service.impl.TzPiDecoratedServiceImpl")
public class TzPiDecoratedServiceImpl extends FrameworkImpl {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private JacksonUtil jacksonUtil;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzGDObject;

	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	@Autowired
	private TzWebsiteLoginServiceImpl tzWebsiteLoginServiceImpl;

	@Override
	public String tzGetHtmlData(String strParams) {
		String strRet = "";

		try {

			jacksonUtil.json2Map(strParams);

			String siteId = jacksonUtil.getString("siteId");

			// 根据站点实例id ， 找站点语言
			String sysDefaultLang = getSysHardCodeVal.getSysDefaultLanguage();
			String sql = "select TZ_SITE_LANG from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID = ?";
			String strLangID = sqlQuery.queryForObject(sql, new Object[] { siteId }, "String");

			// 由于英文的描述可能较长，所以中文和英文的信息项描述的长度不同 , 编辑照片的中英文路径
			String edituserpt_url = "";
			int td_long = 0;
			String websiteImgCommonPath = getSysHardCodeVal.getWebsiteImgPath();
			if (sysDefaultLang.equals(strLangID)) {
				td_long = 120;
				edituserpt_url = websiteImgCommonPath + "/common/edituser-pic.png";
			} else {
				td_long = 160;
				edituserpt_url = websiteImgCommonPath + "/common/edituser-pic-en.png";
			}

			// 当前用户ID（此用户是前台登录用户）
			String todoCheck;
			String m_curOPRID = tzWebsiteLoginServiceImpl.getLoginedUserOprid(request);
			String m_curOrgID = tzWebsiteLoginServiceImpl.getLoginedUserOrgid(request);

			// 处理头像部分 - 开始
			// sql = "select TZ_GENDER from PS_TZ_REG_USER_T where OPRID=?";
			// String strGender = sqlQuery.queryForObject(sql, new Object[] {
			// m_curOPRID }, "String");

			sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetUserHeadImg");
			Map<String, Object> mapUserHeadImg = sqlQuery.queryForMap(sql, new Object[] { m_curOPRID });
			String strPhoto = "";
			if (null != mapUserHeadImg) {
				String strPhotoDir = mapUserHeadImg.get("TZ_ATT_A_URL") == null ? ""
						: String.valueOf(mapUserHeadImg.get("TZ_ATT_A_URL"));
				String strPhotoName = mapUserHeadImg.get("TZ_ATTACHSYSFILENA") == null ? ""
						: String.valueOf(mapUserHeadImg.get("TZ_ATTACHSYSFILENA"));

				if (!"".equals(strPhotoDir) && !"".equals(strPhotoName)) {
					strPhoto = strPhotoDir + strPhotoName;
				}

			}
			if ("".equals(strPhoto)) {
				strPhoto = websiteImgCommonPath + "/common/bjphoto.jpg";
			}

			String strResultHeadImg = tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzPerPhotoCard", strPhoto,
					edituserpt_url);
					// 处理头像部分 - 结束

			// 处理信息项部分 - 开始
			sql = "select count(1) from PS_TZ_REG_FIELD_T where TZ_JG_ID =? and TZ_ENABLE = 'Y' and TZ_IS_SHOWWZSY = 'Y' order by TZ_ORDER";
			int tz_fld_num = sqlQuery.queryForObject(sql, new Object[] { m_curOrgID }, "int");

			sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetUserInfoFieldsList");
			List<Map<String, Object>> listFields = sqlQuery.queryForList(sql, new Object[] { m_curOrgID });

			String strResult_fld = "";
			String strResult_fld_aleft = ""; // 样式为左对齐对应的html

			for (Map<String, Object> mapField : listFields) {

				String str_TZ_REG_FIELD_ID = mapField.get("TZ_REG_FIELD_ID") == null ? ""
						: String.valueOf(mapField.get("TZ_REG_FIELD_ID"));
				String str_TZ_REG_FIELD_NAME = mapField.get("TZ_REG_FIELD_NAME") == null ? ""
						: String.valueOf(mapField.get("TZ_REG_FIELD_NAME"));
				String str_TZ_FIELD_TYPE = mapField.get("TZ_FIELD_TYPE") == null ? ""
						: String.valueOf(mapField.get("TZ_FIELD_TYPE"));

				// 双语化信息项代码 - 开始
				if (!sysDefaultLang.equals(strLangID)) {
					// 如果在英文环境下 ， 取英文字段（如果英文为空，用中文）;
					sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetUserFieldInEng");
					String str_TZ_REG_FIELD_EN_NAME = sqlQuery.queryForObject(sql,
							new Object[] { m_curOrgID, str_TZ_REG_FIELD_ID, strLangID }, "String");
					if (null != str_TZ_REG_FIELD_EN_NAME && !"".equals(str_TZ_REG_FIELD_EN_NAME)) {
						str_TZ_REG_FIELD_NAME = str_TZ_REG_FIELD_EN_NAME;
					}
				}
				// 双语化信息项代码 - 结束

				switch (str_TZ_REG_FIELD_ID) {
				case "TZ_MOBILE":

					sql = "select TZ_ZY_SJ from PS_TZ_LXFSINFO_TBL where TZ_LXFS_LY = 'ZCYH' and TZ_LYDX_ID = ?";
					String str_phone = sqlQuery.queryForObject(sql, new Object[] { m_curOPRID }, "String");

					strResult_fld = strResult_fld + tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzPerInfoFld",
							str_TZ_REG_FIELD_NAME, str_phone, String.valueOf(td_long));

					strResult_fld_aleft = strResult_fld_aleft
							+ tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzPerInfoFldAleft", str_TZ_REG_FIELD_NAME,
									str_phone, String.valueOf(td_long));

					break;

				case "TZ_EMAIL":

					sql = "select TZ_ZY_EMAIL from PS_TZ_LXFSINFO_TBL where TZ_LXFS_LY = 'ZCYH' and TZ_LYDX_ID = ?";
					String str_Email = sqlQuery.queryForObject(sql, new Object[] { m_curOPRID }, "String");

					strResult_fld = strResult_fld + tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzPerInfoFld",
							str_TZ_REG_FIELD_NAME, str_Email, String.valueOf(td_long));

					strResult_fld_aleft = strResult_fld_aleft
							+ tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzPerInfoFldAleft", str_TZ_REG_FIELD_NAME,
									str_Email, String.valueOf(td_long));

					break;

				case "TZ_SKYPE":

					sql = "select TZ_SKYPE from PS_TZ_LXFSINFO_TBL where TZ_LXFS_LY = 'ZCYH' and TZ_LYDX_ID = ?";
					String str_Skype = sqlQuery.queryForObject(sql, new Object[] { m_curOPRID }, "String");

					strResult_fld = strResult_fld + tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzPerInfoFld",
							str_TZ_REG_FIELD_NAME, str_Skype, String.valueOf(td_long));

					strResult_fld_aleft = strResult_fld_aleft
							+ tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzPerInfoFldAleft", str_TZ_REG_FIELD_NAME,
									str_Skype, String.valueOf(td_long));

					break;

				default:

					if ("DROP".equals(str_TZ_FIELD_TYPE)) {

						sql = "select " + str_TZ_REG_FIELD_ID + " from PS_TZ_REG_USER_T where OPRID = ?";
						String str_TZ_REG_FIELD_ID_value = sqlQuery.queryForObject(sql, new Object[] { m_curOPRID },
								"String");

						sql = "select TZ_OPT_VALUE from PS_TZ_YHZC_XXZ_TBL where TZ_JG_ID =? and TZ_REG_FIELD_ID =? and TZ_OPT_ID =?";
						String str_TZ_REG_FIELD_ID_opt = sqlQuery.queryForObject(sql,
								new Object[] { m_curOrgID, str_TZ_REG_FIELD_ID, str_TZ_REG_FIELD_ID_value }, "String");

						// 双语化下拉框代码 - 开始
						if (!sysDefaultLang.equals(strLangID)) {
							// 如果在英文环境下 ， 取英文字段（如果英文为空，用中文）;
							sql = tzGDObject.getSQLText("SQL.TZSitePageBundle.TzGetUserDropListOptInEng");
							String str_TZ_REG_FIELD_ID_optEN = sqlQuery.queryForObject(sql, new Object[] { strLangID,
									m_curOrgID, str_TZ_REG_FIELD_ID, str_TZ_REG_FIELD_ID_value }, "String");
							if (null != str_TZ_REG_FIELD_ID_optEN && !"".equals(str_TZ_REG_FIELD_ID_optEN)) {
								str_TZ_REG_FIELD_ID_opt = str_TZ_REG_FIELD_ID_optEN;
							}
						}
						// 双语化下拉框代码 - 结束

						strResult_fld = strResult_fld + tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzPerInfoFld",
								str_TZ_REG_FIELD_NAME, str_TZ_REG_FIELD_ID_opt, String.valueOf(td_long));

						strResult_fld_aleft = strResult_fld_aleft
								+ tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzPerInfoFldAleft",
										str_TZ_REG_FIELD_NAME, str_TZ_REG_FIELD_ID_opt, String.valueOf(td_long));

					} else {

						sql = "select " + str_TZ_REG_FIELD_ID + " from PS_TZ_REG_USER_T where OPRID = ?";
						String str_TZ_REG_FIELD_ID_value = sqlQuery.queryForObject(sql, new Object[] { m_curOPRID },
								"String");

						strResult_fld = strResult_fld + tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzPerInfoFld",
								str_TZ_REG_FIELD_NAME, str_TZ_REG_FIELD_ID_value, String.valueOf(td_long));

						strResult_fld_aleft = strResult_fld_aleft
								+ tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzPerInfoFldAleft",
										str_TZ_REG_FIELD_NAME, str_TZ_REG_FIELD_ID_value, String.valueOf(td_long));

					}

					break;
				}

			}
			// 处理信息项部分 - 结束

			sql = "select TZ_IS_SHOW_PHOTO from PS_TZ_USERREG_MB_T where TZ_JG_ID=?";
			String isShowPhoto = sqlQuery.queryForObject(sql, new Object[] { m_curOrgID }, "String");
			if ("Y".equals(isShowPhoto)) {
				strRet = tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzPerInfoCard", strResultHeadImg, strResult_fld,
						String.valueOf(tz_fld_num));
			} else {
				strRet = tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzPerInfoCardNoHeadImg", strResult_fld_aleft,
						String.valueOf(tz_fld_num));
			}

			strRet = strRet.replace((char) (10), ' ');
			strRet = strRet.replace((char) (13), ' ');
			strRet = strRet.replace("\\", "\\\\");
			strRet = strRet.replace("'", "\\'");
			strRet = strRet.replace("\"", "\\\"");

			strRet = "\"" + strRet + "\"";

		} catch (Exception e) {
			e.printStackTrace();

		}

		return strRet;
	}

}