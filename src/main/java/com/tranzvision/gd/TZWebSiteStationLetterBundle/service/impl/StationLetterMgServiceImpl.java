package com.tranzvision.gd.TZWebSiteStationLetterBundle.service.impl;

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

/**
 * 
 * @author jufeng 站内信管理
 */
@Service("com.tranzvision.gd.TZWebSiteStationLetterBundle.service.impl.StationLetterMgServiceImpl")
public class StationLetterMgServiceImpl extends FrameworkImpl {
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

	// 站内信管理HTML;
	public String stationLetterHTML(String imgPath, String jgId, String strLang) {

		String stationLetter = validateUtil.getMessageTextWithLanguageCd(jgId, strLang, "TZ_SITE_MESSAGE", "8", "站内信",
				"StationLetter");
		String have = validateUtil.getMessageTextWithLanguageCd(jgId, strLang, "TZ_SITE_MESSAGE", "9", "有",
				"Have");
		String Unread = validateUtil.getMessageTextWithLanguageCd(jgId, strLang, "TZ_SITE_MESSAGE", "10", "未读",
				"Unread");
		String strDeleteBtn = validateUtil.getMessageTextWithLanguageCd(jgId, strLang, "TZ_SITE_MESSAGE", "7", "删除",
				"Remove");
		String strSender = validateUtil.getMessageTextWithLanguageCd(jgId, strLang, "TZ_SITE_MESSAGE", "26", "发件人",
				"Sender");
		String strTheme = validateUtil.getMessageTextWithLanguageCd(jgId, strLang, "TZ_SITE_MESSAGE", "27", "主题",
				"Theme");
		String stReceived = validateUtil.getMessageTextWithLanguageCd(jgId, strLang, "TZ_SITE_MESSAGE", "28",
				"时间", "Received");
		String strUnselected = validateUtil.getMessageTextWithLanguageCd(jgId, strLang, "TZ_SITE_MESSAGE", "29",
				"请选择要删除的记录", "The modification is successful");
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
		String strOldPass ="";
		String strNewPass ="";
		String strConPass ="";
		String strSaveBtn ="";
		String strBlankTips ="";
		String strErrorTips = "";
		String strNotSameTips ="";
		String strPassSucTips ="";
		// 通用链接;
		String contextPath = request.getContextPath();
		String FixPassword = contextPath + "/dispatcher";
		
		try {
			return tzGdObject.getHTMLText("HTML.TZWebSiteStationLetterBundle.TZ_GD_ZNX_MG_HTML", FixPassword, strOldPass,
					strNewPass, strConPass, strSaveBtn, strBlankTips, strErrorTips, strNotSameTips, strPassSucTips,
					strWeakTips, strMiddleTips, strStrongTips, strPassStrengthTips, strPassTips1, strPassTips2,
					strPassTips3, strStrongMsg, imgPath, contextPath);
		} catch (TzSystemException e) {
			e.printStackTrace();
			return "【TZ_GD_FIXPASS_HTML】html对象未定义";
		}
	}
}
