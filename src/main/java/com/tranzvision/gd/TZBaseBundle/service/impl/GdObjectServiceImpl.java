package com.tranzvision.gd.TZBaseBundle.service.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAccountMgBundle.model.PsTzAqYhxxTbl;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.GdObjectService;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
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
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	
	@Autowired
	private TZGDObject tzGDObject;
	
	@Override
	/* 获取当前登录会话语言代码的方法 TODO ***/
	public String getLoginLanguage(HttpServletRequest request, HttpServletResponse response) {
		/**
		 * String retLanguageCd = ""; String tmpExtLanguageCd = m_ExtLanguageCd;
		 * if(tmpExtLanguageCd == null || "".equals(tmpExtLanguageCd)){
		 * 
		 * }else{ retLanguageCd = m_ExtLanguageCd; }
		 **/
		return "ZHS";
	}

	@Override
	/* 获取当前登录人归属机构的方法 TODO */
	public String getLoginOrgID(HttpServletRequest request, HttpServletResponse response) {
		String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		return orgId;
	}

	@Override
	/* 获取超级机构ID的方法 */
	public String getSuperOrgId(HttpServletRequest request, HttpServletResponse response) {
		String superOrgId = this.getHardCodeValue("TZGD_SUPERORG_ID");
		if ("".equals(superOrgId)) {
			superOrgId = "Admin";
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

	/* 设置当前用户访问的组件编号和页面编号 TODO */
	public void setCurrentAccessComponentPage(HttpServletRequest request, String accessComponentID,
			String accessPageID) {
		// TODO;
	}

	/* 判断当前登录会话是否超时失效的方法 TODO */
	public boolean isSessionValid(HttpServletRequest request) {
		return true;
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

	/* 切换当前登录人上下文信息语言环境代码的方法 TODO */
	public void switchLanguageCd(HttpServletRequest request, HttpServletResponse response, String loginLanguageCD) {
		// TODO
	}

	/* 获取当前登录人账号的方法 TODO */
	public String getLoginAccount(HttpServletRequest request, HttpServletResponse response) {
		// TODO;
		PsTzAqYhxxTbl psTzAqYhxxTbl = tzLoginServiceImpl.getLoginedManagerInfo(request);
		if (psTzAqYhxxTbl != null) {
			return psTzAqYhxxTbl.getTzDlzhId();
		}
		return "";
	}

	/* 获取当前登录人对应的OPRID的方法TODO */
	@Override
	public String getOPRID(HttpServletRequest request) {
		// TODO ;
		String oprid = "";
		oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

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
	public String getMessageTextWithLanguageCd(HttpServletRequest request, String msgSetId, String msgId, String langCd, String defaultCNMsg,
			String defaultENMsg) {
		String retMsgText = "";
		String defaultLang = getSysHardCodeVal.getSysDefaultLanguage();
		if(null==langCd || "".equals(langCd)){
			langCd = getSysHardCodeVal.getSysDefaultLanguage();
		}
		
		try{
			String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			String ptOrid = getSysHardCodeVal.getPlatformOrgID();
			String sql = tzGDObject.getSQLText("SQL.TZBaseBundle.TzGetMsgText");
			retMsgText = jdbcTemplate.queryForObject(sql, new Object[]{msgSetId, langCd, msgId, orgid}, "String");
			if(null==retMsgText || "".equals(retMsgText)){
				retMsgText = jdbcTemplate.queryForObject(sql, new Object[]{msgSetId, langCd, msgId, ptOrid}, "String");
			}
			
			if(null==retMsgText || "".equals(retMsgText)){
				if(defaultLang.equals(langCd)){
					retMsgText = defaultCNMsg;
				}else{
					retMsgText = defaultENMsg;
				}
			}else{
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
			retMsgText = "取数失败！" + e.getMessage();
		}
		return retMsgText;
	}
}
