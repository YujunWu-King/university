/**
 * 
 */
package com.tranzvision.gd.TZSitePageBundle.service.impl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzWebsiteLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.ValidateUtil;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 原PS：TZ_SITE_DECORATED_APP:TZ_PI_DECORATED_CLS
 * 申请首页个人信息编辑器-清华MBA特殊改造
 * @author yuds
 * @since 2017-01-20
 */
@Service("com.tranzvision.gd.TZSitePageBundle.service.impl.TzSemPiDecoratedServiceImpl")
public class TzSemPiDecoratedServiceImpl extends FrameworkImpl {

	@Autowired
	private SqlQuery jdbcTemplate;
	
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzGDObject;

	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	@Autowired
	private TzWebsiteLoginServiceImpl tzWebsiteLoginServiceImpl;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private ValidateUtil validateUtil;
	
	@Override
	public String tzGetHtmlData(String strParams) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			String ctxPath = request.getContextPath();

			jacksonUtil.json2Map(strParams);

			String siteId = jacksonUtil.getString("siteId");
			String orgId = jacksonUtil.getString("orgId").toUpperCase();
			// 判断是否为站点装修的请求
			String isd = jacksonUtil.getString("isd");

			// 根据站点实例id ， 找站点语言
			String sysDefaultLang = getSysHardCodeVal.getSysDefaultLanguage();
			String sql = "select TZ_SITE_LANG,TZ_SKIN_ID from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID = ?";
			Map<String, Object> mapSiteiInfo = sqlQuery.queryForMap(sql, new Object[] { siteId });
			String strLangID = "";
			String strSkinID = "";
			if (mapSiteiInfo != null) {
				strLangID = mapSiteiInfo.get("TZ_SITE_LANG") == null ? "": String.valueOf(mapSiteiInfo.get("TZ_SITE_LANG"));
				strSkinID = mapSiteiInfo.get("TZ_SKIN_ID") == null ? ""	: String.valueOf(mapSiteiInfo.get("TZ_SKIN_ID"));
			}
			//个人维护信息双语
			String strModifyLabel = validateUtil.getMessageTextWithLanguageCd(orgId, strLangID, "TZ_MBASITE_MESSAGE", "1","修改", "Modify");
			String strMshXhLabel = validateUtil.getMessageTextWithLanguageCd(orgId, strLangID, "TZ_MBASITE_MESSAGE", "2","面试申请号", "Application number");
			String strRegEmailLabel = validateUtil.getMessageTextWithLanguageCd(orgId, strLangID, "TZ_MBASITE_MESSAGE", "3","注册邮箱", "Registered mail");
			String strCityLabel = validateUtil.getMessageTextWithLanguageCd(orgId, strLangID, "TZ_MBASITE_MESSAGE", "4","所在城市", "City");
			String strSiteMsgLabel = validateUtil.getMessageTextWithLanguageCd(orgId, strLangID, "TZ_MBASITE_MESSAGE", "5","系统消息", "System message");
			String strMyActLabel = validateUtil.getMessageTextWithLanguageCd(orgId, strLangID, "TZ_MBASITE_MESSAGE", "6","我的活动", "My activities");
			
			
			String websiteImgCommonPath = ctxPath + getSysHardCodeVal.getWebsiteSkinsImgPath();

			// 当前用户ID（此用户是前台登录用户）
			String m_curOPRID = "";
			String m_curOrgID = "";
			if ("Y".equals(isd)) {
				// 如果是站点装修的请求，则使用后台的session
				m_curOPRID = tzLoginServiceImpl.getLoginedManagerOprid(request);
				m_curOrgID = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			} else {
				// 如果不是站点装修的请求，则取前台用户的session
				m_curOrgID = tzWebsiteLoginServiceImpl.getLoginedUserOrgid(request);
				m_curOPRID = tzWebsiteLoginServiceImpl.getLoginedUserOprid(request);
			}
			
			if (!m_curOrgID.equals(orgId)) {
				// 如果当前用户登录的机构与请求的机构不一致，则返回空
				return "";
			}

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
					strPhoto = ctxPath + strPhotoDir + strPhotoName;
				}

			}
			if ("".equals(strPhoto)) {
				strPhoto = websiteImgCommonPath + "/" + strSkinID + "/bjphoto.jpg";
			}
			String strName="";
			String strApplicationNum="";
			String strRegEmail="";
			String strCity="";
			String infoSql = "SELECT OPRID,TZ_REALNAME,TZ_EMAIL,TZ_MSH_ID FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_DLZH_ID=? AND TZ_JG_ID=?";
			Map<String, Object> siteMap = jdbcTemplate.queryForMap(infoSql, new Object[] { m_curOPRID,orgId });
			
			if (siteMap != null) {
				strName = (String) siteMap.get("TZ_REALNAME");
				strRegEmail = (String) siteMap.get("TZ_EMAIL");
				strApplicationNum = (String) siteMap.get("TZ_MSH_ID");
			}
			
			String cityInfo="select TZ_LEN_PROID from ps_TZ_REG_USER_T where OPRID=?";
			strCity = jdbcTemplate.queryForObject(cityInfo, new Object[] { m_curOPRID}, "String");
			//sql = "select TZ_IS_SHOW_PHOTO from PS_TZ_USERREG_MB_T where TZ_JG_ID=?";
			sql = "select TZ_IS_SHOW_PHOTO from PS_TZ_USERREG_MB_T where TZ_SITEI_ID=?";
			String isShowPhoto = sqlQuery.queryForObject(sql, new Object[] { siteId }, "String");
			if ("Y".equals(isShowPhoto)) {
				strRet = tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzSemPerInfoCard",strName,strModifyLabel,siteId,"384",
						strMshXhLabel,strApplicationNum,strRegEmailLabel,strRegEmail,strCityLabel,strCity,"6",strSiteMsgLabel,"6",strMyActLabel,strPhoto);
			} else {
				strRet = tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzSemPerInfoCardNoHeadImg", strName,strModifyLabel,siteId,"384",
						strMshXhLabel,strApplicationNum,strRegEmailLabel,strRegEmail,strCityLabel,strCity,"6",strSiteMsgLabel,"6",strMyActLabel);
			}

			strRet = strRet.replace((char) (10), ' ');
			strRet = strRet.replace((char) (13), ' ');
			strRet = strRet.replace("\\", "\\\\");
			//strRet = strRet.replace("'", "\\'");
			strRet = strRet.replace("\"", "\\\"");

			strRet = "\"" + strRet + "\"";


		} catch (Exception e) {
			e.printStackTrace();

		}
		
		return strRet;
	}

}
