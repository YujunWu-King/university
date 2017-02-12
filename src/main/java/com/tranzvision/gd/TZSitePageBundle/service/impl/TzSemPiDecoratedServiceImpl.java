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
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
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
	private GetHardCodePoint getHardCodePoint;

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
			//项目跟目录;
			String rootPath = request.getContextPath();

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
			//个人账号管理为菜单跳转，通过菜单类型为账户管理获取
			//String personInfoMenuId = getHardCodePoint.getHardCodePointVal("TZ_" + siteId + "_SEMUSER_MENUID");
			String sqlMenuId = "SELECT A.TZ_MENU_ID FROM PS_TZ_SITEI_MENU_T A,PS_TZ_SITEI_MTYP_T B WHERE A.TZ_MENU_TYPE_ID=B.TZ_MENU_TYPE_ID AND A.TZ_SITEI_ID=B.TZ_SITEI_ID AND B.TZ_TYPE_STATE='Y' AND B.TZ_MENU_TYPE_NAME='账户管理' AND A.TZ_SITEI_ID=?";
			String personInfoMenuId = jdbcTemplate.queryForObject(sqlMenuId, new Object[] { siteId}, "String");
			
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
						
			String cityInfo="select TZ_LEN_PROID from PS_TZ_REG_USER_T where OPRID=?";
			strCity = jdbcTemplate.queryForObject(cityInfo, new Object[] { m_curOPRID}, "String");
			//未读站内信数量
			int MsgCount = 0;
			String MsgSql = "select count(*) from PS_TZ_ZNX_REC_T where TZ_ZNX_RECID=? and TZ_ZNX_STATUS='N' and TZ_REC_DELSTATUS<>'Y'";
			MsgCount = jdbcTemplate.queryForObject(MsgSql, new Object[] { m_curOPRID}, "int");
			String MsgDisplay = "";
			String strMsgCount="";
			if(MsgCount==0){
				MsgDisplay = "display:none;";
			}else{
				strMsgCount = String.valueOf(MsgCount);
			}
			//我已报名但未过期的活动
			int actCount = 0;
			String actSql = "select count(*) from PS_TZ_ART_HD_TBL A,PS_TZ_NAUDLIST_T B where A.TZ_ART_ID=B.TZ_ART_ID and (A.TZ_START_DT<=DATE_FORMAT(CURDATE(), 'yyyy-MM-dd') and A.TZ_START_TM<=DATE_FORMAT(CURDATE(), 'hh24:00:00')) and (A.TZ_END_DT>=DATE_FORMAT(CURDATE(), 'yyyy-MM-dd') and A.TZ_END_TM>=DATE_FORMAT(CURDATE(), 'hh24:00:00')) and B.OPRID=? and B.TZ_NREG_STAT='1'";
			actCount = jdbcTemplate.queryForObject(actSql, new Object[] { m_curOPRID}, "int");
			String ActDisplay = "";
			String strActCount = "";
			if(actCount==0){
				ActDisplay = "display:none;";
			}else{
				strActCount = String.valueOf(actCount);
			}
			System.out.println("strActCount=" + strActCount);
			//站内信管理页面;
			String znxUrl = rootPath + "/dispatcher?classid=znxGl&siteId="+siteId;
			//sql = "select TZ_IS_SHOW_PHOTO from PS_TZ_USERREG_MB_T where TZ_JG_ID=?";
			sql = "select TZ_IS_SHOW_PHOTO from PS_TZ_USERREG_MB_T where TZ_SITEI_ID=?";
			String isShowPhoto = sqlQuery.queryForObject(sql, new Object[] { siteId }, "String");
			if ("Y".equals(isShowPhoto)) {
				strRet = tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzSemPerInfoCard",strName,strModifyLabel,siteId,personInfoMenuId,
						strMshXhLabel,strApplicationNum,strRegEmailLabel,strRegEmail,strCityLabel,strCity,strMsgCount,strSiteMsgLabel,strActCount,strMyActLabel,MsgDisplay,ActDisplay,strPhoto,znxUrl);
			} else {
				strRet = tzGDObject.getHTMLText("HTML.TZSitePageBundle.TzSemPerInfoCardNoHeadImg", strName,strModifyLabel,siteId,personInfoMenuId,
						strMshXhLabel,strApplicationNum,strRegEmailLabel,strRegEmail,strCityLabel,strCity,strMsgCount,strSiteMsgLabel,strActCount,strMyActLabel,MsgDisplay,ActDisplay,znxUrl);
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
		System.out.println(strRet);
		return strRet;
	}

}
