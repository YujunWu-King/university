package com.tranzvision.gd.TZBaseBundle.service.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.GdObjectService;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.cookie.TzCookie;
import com.tranzvision.gd.util.session.TzSession;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 高端产品顶层service父类
 * 
 * @author tang
 * @version 1.0, 2015/09/28
 */
@Service
public class GdObjectServiceImpl implements GdObjectService {
	
	/**
	 * 当前访问组件编号
	 */
	private static final String gbl_CurrentAccessCmpntID = "gbl_CurrentAccessCmpntID";
	/**
	 * 当前访问页面编号
	 */
	private static final String gbl_CurrentAccessCPageID = "gbl_CurrentAccessCmpntID";  
	/**
	 * Session存储的用户当前登录语言
	 */
	private final static String sysLanguage = "sysLanguage";

	/**
	 * Cookie存储的系统语言信息
	 */
	private final static String cookieLang = "tzlang";
	/**
	 * Cookie存储的机构信息 TODO 名称待定
	 */
	private final static String cookieJgId = "TZGD_CONTEXT_LOGIN_ORGID";
	
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	@Autowired
	private TZGDObject tzGDObject;
	
	@Autowired
	private TzCookie tzCookie;
	
	@Autowired
	private SiteRepCssServiceImpl siteRepCssServiceImpl;

	@Override
	// 获取当前登录会话语言代码的方法
	public String getLoginLanguage(HttpServletRequest request, HttpServletResponse response) {
		String language = tzLoginServiceImpl.getSysLanaguageCD(request);
		if(language == null || "".contentEquals(language)){
			language = this.getBaseLanguageCd();
		}
		return language;
	}
	
	public String getBaseLanguageCd(){
		String baseLanguageCd = getHardCodeValue("TZGD_BASIC_LANGUAGE");
		   
		if(baseLanguageCd == null || "".equals(baseLanguageCd))	{	
			baseLanguageCd = "ZHS";
		}else{
			baseLanguageCd = baseLanguageCd.toUpperCase();
		}
		return baseLanguageCd;
	}

	@Override
	/* 获取当前登录人归属机构的方法*/
	public String getLoginOrgID(HttpServletRequest request, HttpServletResponse response) {
		String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		return orgId;
	}

	@Override
	/* 获取超级机构ID的方法 */
	public String getSuperOrgId(HttpServletRequest request, HttpServletResponse response) {
		String superOrgId = this.getHardCodeValue("TZGD_SUPERORG_ID");
		if ("".equals(superOrgId)) {
			superOrgId = "ADMIN";
		}
		return superOrgId;
	}

	@Override
	/* 获取LOGO样式表的方法 */
	public String getLogoStyle(HttpServletRequest request, HttpServletResponse response) {
		String tmpStyle = "";
		String appContext = request.getContextPath();
		String logoPath = this.getMessageText(request, response, "TZGD_FWDEFAULT_MSGSET", "TZGD_FWD_003",
				appContext + "/statics/images/logo/admin", appContext + "/statics/images/logo/admin");
		String logoName = this.getMessageText(request, response, "TZGD_FWDEFAULT_MSGSET", "TZGD_FWD_004",
				"logo-admin-w.png", "logo-admin-w.png");
		String logoSize = this.getMessageText(request, response, "TZGD_FWDEFAULT_MSGSET", "TZGD_FWD_005",
				"width:190px;height:44px;", "width:190px;height:44px;");
		logoPath = logoPath.replace('\\', '/');
		if ((logoPath.lastIndexOf("/") + 1) != logoPath.length()) {
			logoPath = logoPath + "/";
		}
		tmpStyle = logoSize + "background:url(" + logoPath + logoName + ") no-repeat center 0px";
		return tmpStyle;
	}

	@Override
	/* 获取个性化主题编号 */
	public String getUserGxhTheme(HttpServletRequest request, HttpServletResponse response) {

		String tmpThemeID = this.getUserGXHSXValue(request, response, "THEMEID");
		if (this.isThemeIDValid(tmpThemeID) == false) {
			tmpThemeID = this.getMessageText(request, response, "TZGD_FWDEFAULT_MSGSET", "TZGD_FWD_001", "tranzvision",
					"tranzvision");
			if (this.isThemeIDValid(tmpThemeID) == false) {
				tmpThemeID = "tranzvision";
			}
		}

		return tmpThemeID;
	}

	@Override
	/* 获取个性化环境语言代码 */
	public String getUserGxhLanguage(HttpServletRequest request, HttpServletResponse response) {
		String tmpLanguageCd = this.getUserGXHSXValue(request, response, "LANGUAGECD");
		if (this.isLanguageCdValid(tmpLanguageCd) == false) {
			tmpLanguageCd = this.getMessageText(request, response, "TZGD_FWDEFAULT_MSGSET", "TZGD_FWD_002", "ZHS",
					"ZHS");
			if (this.isLanguageCdValid(tmpLanguageCd) == false) {
				tmpLanguageCd = "ZHS";
			}
		}

		return tmpLanguageCd;
	}

	/* 根据指定消息集合号和消息ID获取消息文本的方法 */
	public String getMessageText(HttpServletRequest request, HttpServletResponse response, String msgSetId,
			String msgId, String defaultCNMsg, String defaultENMsg) {
		String tmpMsgText = "";
		String loginLanguage = this.getLoginOrgID(request, response);
		String jgId = this.getLoginOrgID(request, response);
		String superOrgId = this.getSuperOrgId(request, response);

		String sql = "SELECT ifnull(B.TZ_MSG_TEXT,A.TZ_MSG_TEXT) TZ_MSG_TEXT from PS_TZ_PT_XXDY_TBL A left join PS_TZ_PT_XXDY_TBL B on A.TZ_XXJH_ID = B.TZ_XXJH_ID and A.TZ_JG_ID=B.TZ_JG_ID and A.TZ_MSG_ID=B.TZ_MSG_ID where upper(B.TZ_LANGUAGE_ID)=upper(?) and upper(A.TZ_LANGUAGE_ID)=(SELECT UPPER(TZ_HARDCODE_VAL) TZ_LANGUAGE_CD FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT='TZGD_BASIC_LANGUAGE' ) AND A.TZ_XXJH_ID=? AND A.TZ_MSG_ID=? AND  UPPER(A.TZ_JG_ID)=UPPER(?)";
		tmpMsgText = jdbcTemplate.queryForObject(sql, new Object[] { loginLanguage, msgSetId, msgId, jgId }, "String");

		if (tmpMsgText == null || "".equals(tmpMsgText)) {
			tmpMsgText = jdbcTemplate.queryForObject(sql, new Object[] { loginLanguage, msgSetId, msgId, superOrgId },
					"String");
		}

		if (tmpMsgText == null || "".equals(tmpMsgText)) {
			if (!"ZHS".equals(jgId)) {
				tmpMsgText = defaultENMsg;
			} else {
				tmpMsgText = defaultCNMsg;
			}
		}
		return tmpMsgText;
	}

	/* 设置当前用户访问的组件编号和页面编号 */
	public void setCurrentAccessComponentPage(HttpServletRequest request, String accessComponentID,
			String accessPageID) {
		TzSession tzSession = new TzSession(request);
		tzSession.addSession(gbl_CurrentAccessCmpntID, accessComponentID);
		tzSession.addSession(gbl_CurrentAccessCPageID, accessPageID);
	}

	/* 判断当前登录会话是否超时失效的方法 */
	public boolean isSessionValid(HttpServletRequest request) {
		boolean bSessionValid = false;
		//判断用户
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		String lang = tzLoginServiceImpl.getSysLanaguageCD(request);
		String zhid = tzLoginServiceImpl.getLoginedManagerDlzhid(request);
		if(oprid != null && !"".equals(oprid)
			&& orgid != null && !"".equals(orgid)
			&& lang != null && !"".equals(lang)
			&& zhid != null && !"".equals(zhid)){
			bSessionValid = true;
		}else{
			/*当前会话对应的上下文全局变量均不存在的情况下，还要判断一下当前登录用户（理论上应该是匿名访问用户）对当前访问的组件页面是否有访问权限*/
			TzSession tzSession = new TzSession(request);
			String comid = (String)tzSession.getSession(gbl_CurrentAccessCmpntID);
			String pageid = (String)tzSession.getSession(gbl_CurrentAccessCPageID);
			String tmpAuthFlag = this.getAPByCPU(request,comid,pageid,this.getOPRID(request));
			if("W".equals(tmpAuthFlag) || "R".equals(tmpAuthFlag)){
		         /*如果当前用户（理论上应该是匿名访问用户）对当前访问的组件页面有访问权限，则仍然返回会话有效*/
		         bSessionValid = true;
			}else{
				 /*如果当前用户（理论上应该是匿名访问用户）对当前访问的组件页面无访问权限，则返回会话已过有效期*/
		         bSessionValid = false;
			}
			
		}
		return bSessionValid;
	}
	
	public String getAPByCPU(HttpServletRequest request,String comId,String pageId, String userId){
		String retAccessPermission = "N";

		 /*如果传入的用户为空，则默认为当前登录用户*/
		if(userId == null || "".equals(userId)){
			userId = this.getOPRID(request);
		}
		if(comId == null || "".equals(comId) 
				|| pageId == null || "".equals(pageId)){
			return "N";
		}
		
		// 更新权限;
		int update = 0;
		String haveUpdateSQL = "SELECT C.TZ_EDIT_FLG FROM PSROLEUSER A,PSROLECLASS B,PS_TZ_AQ_COMSQ_TBL C WHERE A.ROLEUSER=? AND A.DYNAMIC_SW='N' AND A.ROLENAME = B.ROLENAME AND B.CLASSID=C.CLASSID AND (C.TZ_COM_ID=? OR C.TZ_COM_ID LIKE ?) AND C.TZ_PAGE_ID=? ORDER BY C.TZ_EDIT_FLG DESC limit 0,1";
		try {
			update = jdbcTemplate.queryForObject(haveUpdateSQL,
								new Object[] { userId, comId, comId + "$%", pageId }, "Integer");
		} catch (Exception e) {
			update = 0;
		}
		if (update != 1) {
			// 查看权限;
			int view = 0;
			String haveReadSQL = "SELECT  C.DISPLAYONLY FROM PSROLEUSER A,PSROLECLASS B,PS_TZ_AQ_COMSQ_TBL C WHERE A.ROLEUSER=? AND A.DYNAMIC_SW='N' AND A.ROLENAME = B.ROLENAME AND B.CLASSID=C.CLASSID AND (C.TZ_COM_ID=? OR C.TZ_COM_ID LIKE ?) AND C.TZ_PAGE_ID=? ORDER BY  C.DISPLAYONLY limit 0,1";
			try {
				view = jdbcTemplate.queryForObject(haveReadSQL,
									new Object[] { userId, comId, comId + "$%", pageId }, "Integer");
			} catch (Exception e) {
				view = 0;
			}
			if (view != 1) {
				retAccessPermission = "N";
			}else{
				retAccessPermission = "R";
			}
		}else{
			retAccessPermission = "W";
		}
		return retAccessPermission;
		
	}

	/* 判断主题是否合法的方法 */
	public boolean isThemeIDValid(String theme) {
		String sql = "SELECT 'X' FROM PS_TZ_PT_ZTXX_TBL WHERE UPPER(TZ_ZT_ID)=UPPER(?)";
		String validFlag = "";
		validFlag = jdbcTemplate.queryForObject(sql, new Object[] { theme }, "String");

		return ("X".equals(validFlag));
	}

	/* 保存指定用户个性化属性设置值的方法 */
	public boolean setUserGXHSXValue(HttpServletRequest request, HttpServletResponse response, String userGxhsxName,
			String userGxhsxValue) {
		boolean successFlag = true;
		String tmpUserId = this.getLoginAccount(request, response);
		String tmpOrgId = this.getLoginOrgID(request, response);

		/* 判断用户账号、机构是否合法 */
		String sql = "SELECT COUNT(1) FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_DLZH_ID=? AND TZ_JG_ID=?";
		int isValidateNum = 0;
		isValidateNum = jdbcTemplate.queryForObject(sql, new Object[] { tmpUserId, tmpOrgId }, "Integer");

		if (isValidateNum > 0) {
			// 是否存在用户个性化记录;
			String gxhSxqz = "";
			sql = "SELECT TZ_GXH_SXQZ FROM PS_TZ_YHGXH_JGJL_T WHERE TZ_DLZH_ID=? AND Upper(TZ_JG_ID)=Upper(?) AND Upper(TZ_GXH_SXMC)=Upper(?)";
			gxhSxqz = jdbcTemplate.queryForObject(sql, new Object[] { tmpUserId, tmpOrgId, userGxhsxName }, "String");

			// 设置用户个性化设置结果记录表;
			if (gxhSxqz != null && !"".equals(gxhSxqz)) {
				if (!gxhSxqz.equals(userGxhsxValue)) {
					String updateSQL = "UPDATE PS_TZ_YHGXH_JGJL_T SET TZ_GXH_SXQZ = ? WHERE TZ_DLZH_ID=? AND Upper(TZ_JG_ID)=Upper(?) AND Upper(TZ_GXH_SXMC)=Upper(?)";
					jdbcTemplate.update(updateSQL, new Object[] { userGxhsxValue, tmpUserId, tmpOrgId, userGxhsxName });

				}

			} else {
				String insertSQL = "INSERT INTO PS_TZ_YHGXH_JGJL_T(TZ_DLZH_ID, TZ_JG_ID, TZ_GXH_SXMC, TZ_GXH_SXQZ) VALUES(?,?,?,?)";
				jdbcTemplate.update(insertSQL, new Object[] { tmpUserId, tmpOrgId.toUpperCase(),
						userGxhsxName.toUpperCase(), userGxhsxValue });
			}

		} else {
			successFlag = false;
		}

		return successFlag;
	}

	/* 设置个性化主题编号 */
	public void setUserGxhTheme(HttpServletRequest request, HttpServletResponse response, String theme) {
		if (this.isThemeIDValid(theme)) {
			this.setUserGXHSXValue(request, response, "THEMEID", theme);
		}

	}

	/* 检查语言环境是否有效 */
	public boolean isLanguageCdValid(String language) {
		/* 判断用户账号、机构是否合法 */
		String validFlag = "";

		String sql = "SELECT 'X' FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID='TZ_GD_LANGUAGE' AND TZ_EFF_STATUS='A' AND UPPER(TZ_ZHZ_ID)=UPPER(?)";
		validFlag = jdbcTemplate.queryForObject(sql, new Object[] { language }, "String");

		return ("X".equals(validFlag));
	}

	/* 设置个性化环境语言代码 */
	public void setUserGxhLanguage(HttpServletRequest request, HttpServletResponse response, String language) {
		if (this.isLanguageCdValid(language)) {
			this.setUserGXHSXValue(request, response, "LANGUAGECD", language);
		}
	}

	/* 切换当前登录人上下文信息语言环境代码的方法*/
	public void switchLanguageCd(HttpServletRequest request, HttpServletResponse response, String loginLanguageCD) {
		// 设置session变量
		TzSession tzSession = new TzSession(request);
		tzSession.addSession(sysLanguage, loginLanguageCD);

		// 设置cookie变量
		int cookieMaxAge = 3600 * 24 * 30; // cookie期限是30天
		tzCookie.addCookie(response, cookieLang, loginLanguageCD, cookieMaxAge);
	
		// 更新系统记录
		this.setUserGXHSXValue(request, response, "LANGUAGECD", loginLanguageCD);
	}

	/* 获取当前登录人账号的方法 */
	public String getLoginAccount(HttpServletRequest request, HttpServletResponse response) {
		return tzLoginServiceImpl.getLoginedManagerDlzhid(request);
	}

	/* 获取当前登录人对应的OPRID的方法 */
	@Override
	public String getOPRID(HttpServletRequest request) {
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		if(oprid == null || "".equals(oprid)){
			oprid = "TZ_GUEST";
		}
		return oprid;
	}

	/* 根据HardCode代码获取HardCode值的方法 */
	@Override
	public String getHardCodeValue(String hCode) {
		String tmpHardCodeValue = "";
		String hardCodeSQL = "SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?";
		tmpHardCodeValue = jdbcTemplate.queryForObject(hardCodeSQL, new Object[] { hCode }, "String");

		if (tmpHardCodeValue == null) {
			tmpHardCodeValue = "";
		}

		return tmpHardCodeValue;
	}

	/* 获取指定用户个性化属性设置值的方法 */
	private String getUserGXHSXValue(HttpServletRequest request, HttpServletResponse response, String userGxhsxName) {
		String tmpGxhsxValue = "";
		String tmpUserId = this.getLoginAccount(request, response);
		String tmpOrgId = this.getLoginOrgID(request, response);
		String sql = "SELECT TZ_GXH_SXQZ FROM PS_TZ_YHGXH_JGJL_T WHERE TZ_DLZH_ID=? AND TZ_JG_ID=UPPER(?) AND TZ_GXH_SXMC=UPPER(?)";
		tmpGxhsxValue = jdbcTemplate.queryForObject(sql, new Object[] { tmpUserId, tmpOrgId, userGxhsxName }, "String");

		if (tmpGxhsxValue == null) {
			tmpGxhsxValue = "";
		}

		return tmpGxhsxValue;
	}

	/**
	 * 根据指定消息集合号、消息ID、语言代码获取消息文本的方法
	 * 
	 * @author SHIHUA
	 * @param msgSetId
	 * @param msgId
	 * @param langCd
	 * @param defaultCNMsg
	 * @param defaultENMsg
	 * @return String
	 */
	public String getMessageTextWithLanguageCd(HttpServletRequest request, String msgSetId, String msgId, String langCd,
			String defaultCNMsg, String defaultENMsg) {
		String retMsgText = "";
		String defaultLang = getSysHardCodeVal.getSysDefaultLanguage();
		if (null == langCd || "".equals(langCd)) {
			langCd = getSysHardCodeVal.getSysDefaultLanguage();
		}

		try {
			String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			String ptOrid = getSysHardCodeVal.getPlatformOrgID();

			if (null != msgSetId && !"".equals(msgSetId) && null != msgId && !"".equals(msgId)) {
				String sql = tzGDObject.getSQLText("SQL.TZBaseBundle.TzGetMsgText");
				retMsgText = jdbcTemplate.queryForObject(sql, new Object[] { msgSetId, langCd, msgId, orgid },
						"String");
				if (null == retMsgText || "".equals(retMsgText)) {
					retMsgText = jdbcTemplate.queryForObject(sql, new Object[] { msgSetId, langCd, msgId, ptOrid },
							"String");
				}
			}

			if (null == retMsgText || "".equals(retMsgText)) {
				if (defaultLang.equals(langCd)) {
					retMsgText = defaultCNMsg;
				} else {
					retMsgText = defaultENMsg;
				}
			} else {

			}

		} catch (Exception e) {
			e.printStackTrace();
			retMsgText = "取数失败！" + e.getMessage();
		}
		return retMsgText;
	}
	
	/*获取处理会话超时的HTML代码*/
	public String getTimeoutHTML(HttpServletRequest request,String htmlObject){
		String strRetContent = "";
		String tmpLoginURL = "";
		//得到机构的cookie;
		String tmpOrgID = tzCookie.getStringCookieVal(request, cookieJgId);
		//得到语言;
		String tmpLanguageCd = tzCookie.getStringCookieVal(request, cookieLang);

		if(tmpOrgID != null && !"".equals(tmpOrgID)){
			//查询机构是不是存在;
			String sql = "SELECT count(1) FROM PS_TZ_JG_BASE_T WHERE TZ_JG_EFF_STA='Y' AND LOWER(TZ_JG_ID)=LOWER(?)";
			int count = jdbcTemplate.queryForObject(sql, new Object[]{tmpOrgID},"Integer");
			if(count > 0){
				tmpLoginURL = request.getContextPath() + "/login/"+tmpOrgID.toLowerCase();
			}else{
				tmpLoginURL = request.getContextPath() + "/login";
			}
		}else{
			tmpLoginURL = request.getContextPath() + "/login";
		}
		
		if(tmpLanguageCd != null){
			String langSQL = "SELECT COUNT(1) FROM PS_TZ_PT_ZHZXX_TBL WHERE UPPER(TZ_ZHZJH_ID)=UPPER(?) AND TZ_ZHZ_ID=? AND TZ_EFF_DATE<= curdate()";
			
			int languageCount = jdbcTemplate.queryForObject(langSQL, new Object[]{tmpLanguageCd,tmpLanguageCd},"Integer");
			if(languageCount == 0){
				tmpLanguageCd = this.getBaseLanguageCd();
			}
		}else{
			tmpLanguageCd = this.getBaseLanguageCd();
		}
		
		String tempDefaultPrefixCN = "当前会话已超时或者非法访问，重新登录请点击";
		String tempDefaultPrefixEN = "The current session is timeout or the current access is invalid.<br>Please click";
		String tempDefaultMiddleCN = "这里";
		String tempDefaultMiddleEN = "here";
		String tempDefaultPostfixCN = "。";
		String tempDefaultPostfixEN = "to relogin.";
		
		String tmpInvalidSessionPrefix = this.getMessageTextWithLanguageCd(request,"TZGD_FWINIT_MSGSET", "TZGD_FWINIT_00037", tmpLanguageCd, tempDefaultPrefixCN, tempDefaultPrefixEN);
		String tmpInvalidSessionMiddle = this.getMessageTextWithLanguageCd(request,"TZGD_FWINIT_MSGSET", "TZGD_FWINIT_00038", tmpLanguageCd, tempDefaultMiddleCN, tempDefaultMiddleEN);
		String tmpInvalidSessionPostfix = this.getMessageTextWithLanguageCd(request,"TZGD_FWINIT_MSGSET", "TZGD_FWINIT_00039", tmpLanguageCd, tempDefaultPostfixCN, tempDefaultPostfixEN);
	    
		try {
			strRetContent = tzGDObject.getHTMLText(htmlObject, true, tmpInvalidSessionPrefix, tmpLoginURL, tmpInvalidSessionMiddle, tmpInvalidSessionPostfix, tmpOrgID,request.getContextPath());
			strRetContent = siteRepCssServiceImpl.repCssByJg(strRetContent, tmpOrgID);
		} catch (TzSystemException e) {
			e.printStackTrace();
			strRetContent = "";
		}
		
		return strRetContent;
	}
}
