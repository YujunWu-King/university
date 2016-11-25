package com.tranzvision.gd.TZEventsBundle.service.impl;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzWebsiteLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdObjectServiceImpl;
import com.tranzvision.gd.TZEventsBundle.dao.PsTzLxfsinfoTblMapper;
import com.tranzvision.gd.TZEventsBundle.dao.PsTzNaudlistTMapper;
import com.tranzvision.gd.util.cms.FreeMarkertUtils;
import com.tranzvision.gd.util.cms.action.directive.MenuDirective;
import com.tranzvision.gd.util.cms.entity.main.CmsMenu;
import com.tranzvision.gd.util.security.TzFilterIllegalCharacter;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * @author caoy
 * @version 创建时间：2016年11月11日 下午5:24:05 类说明
 */
@Service("com.tranzvision.gd.TZEventsBundle.service.impl.TzEventApplyFormPhoneServiceImpl")
public class TzEventApplyFormPhoneServiceImpl extends FrameworkImpl {
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzGDObject;

	@Autowired
	private TzWebsiteLoginServiceImpl tzWebsiteLoginServiceImpl;

	@Autowired
	private TzFilterIllegalCharacter tzFilterIllegalCharacter;

	@Autowired
	private GdObjectServiceImpl gdObjectServiceImpl;

	@Autowired
	PsTzNaudlistTMapper psTzNaudlistTMapper;

	@Autowired
	PsTzLxfsinfoTblMapper psTzLxfsinfoTblMapper;
	
	//final String filterPattern = "[<>{}\\[\\];\\&]";

	/**
	 * 显示在线报名注册页面
	 */
	@Override
	public String tzGetHtmlContent(String strParams) {
		String strRet = "";
		try {
			// JacksonUtil jacksonUtil = new JacksonUtil();
			// jacksonUtil.json2Map(strParams);
			// 活动ID
			// String strApplyId = jacksonUtil.getString("APPLYID");

			String strApplyId = request.getParameter("APPLYID");
			String MenuId = request.getParameter("MenuId");
			String SiteId = request.getParameter("SiteId");
			
			
			strApplyId = tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(strApplyId);
			MenuId = tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(MenuId);
			SiteId = tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(SiteId);

			// 当前登录人登录账号
			String userDLZH = tzWebsiteLoginServiceImpl.getLoginedUserDlzhid(request);

			// 当前登录人所属机构
			String orgid = tzWebsiteLoginServiceImpl.getLoginedUserOrgid(request);

			// 得到站的跟目录

			String sql = "select TZ_SITEI_PATH from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID in (select TZ_SITE_ID from PS_TZ_LM_NR_GL_T where TZ_ART_ID=?) ";
			String path = null;
			List<Map<String, Object>> pathList = sqlQuery.queryForList(sql.toString(), new Object[] { strApplyId });
			if (pathList != null && pathList.size() == 1) {
				Map<String, Object> mapNode = pathList.get(0);
				if (mapNode.get("TZ_SITEI_PATH") != null) {
					path = mapNode.get("TZ_SITEI_PATH").toString();
					if (path.endsWith("/")) {
						path = path.substring(0, path.length() - 1);
					}
				}
			}

			// 统一URL
			String strUrl = request.getContextPath() + "/dispatcher";

			sql = "select TZ_REALNAME,TZ_EMAIL,TZ_MOBILE from PS_TZ_AQ_YHXX_TBL where TZ_DLZH_ID=? and TZ_JG_ID=?";
			Map<String, Object> mapUserInfo = sqlQuery.queryForMap(sql, new Object[] { userDLZH, orgid });

			// 姓名
			String name = "";
			// 邮箱
			String email = "";
			// 手机
			String mobile = "";

			if (null != mapUserInfo) {
				name = mapUserInfo.get("TZ_REALNAME") == null ? "" : String.valueOf(mapUserInfo.get("TZ_REALNAME"));
				email = mapUserInfo.get("TZ_EMAIL") == null ? "" : String.valueOf(mapUserInfo.get("TZ_EMAIL"));
				mobile = mapUserInfo.get("TZ_MOBILE") == null ? "" : String.valueOf(mapUserInfo.get("TZ_MOBILE"));
			}

			// 必填项的html标识
			String strBtHtml = "<span class=\"red\">*</span>";

			// 活动ID的隐藏域
			String str_items_html = "<input type=\"hidden\" id=\"TZ_APPLY_ID\" name=\"TZ_APPLY_ID\" value=\""
					+ strApplyId + "\"/>";

			// 双语化
			sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzGetSiteLang");
			String tzSiteLang = sqlQuery.queryForObject(sql, new Object[] { strApplyId }, "String");

			String titleName = "";
			if (tzSiteLang.endsWith("ENG")) {
				titleName = "Events";
			} else {
				titleName = "活动";
			}

			String onlineApplyText = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPLICATION_MSG",
					"MSG01", tzSiteLang, "在线报名", "Online Application");
			String timeOut = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPLICATION_MSG", "MSG06",
					tzSiteLang, "服务端请求超时。", "Server Request Timeout");
			String serverError = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPLICATION_MSG",
					"MSG07", tzSiteLang, "服务端请求发生错误。", "Server Request Error");
			String authCode = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPLICATION_MSG", "MSG10",
					tzSiteLang, "验证码", "Auth Code");
			String tipsMsg = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPLICATION_MSG", "MSG11",
					tzSiteLang, "请输入报名注册信息，报名成功之后将发送确认短信或邮件到以下手机号码或电子邮箱（您可以更改手机号码或电子邮箱地址）",
					"Please fill this form.You will receive a message or an email when applied success.(You can change the mobile or the email address)");

			String submitBtn = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPLICATION_MSG",
					"MSG14", tzSiteLang, "提交", "Submit");
			String requireTips = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPLICATION_MSG",
					"MSG15", tzSiteLang, "带*号字段必须填写！", "The fields with * are required");
			String changeAuthCode = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPLICATION_MSG",
					"MSG16", tzSiteLang, "看不清楚？点击更换", "Change");

			sql = "select TZ_ZXBM_XXX_ID,TZ_ZXBM_XXX_NAME,TZ_ZXBM_XXX_BT,TZ_ZXBM_XXX_ZSXS from PS_TZ_ZXBM_XXX_T where TZ_ART_ID=? order by TZ_PX_XH";
			List<Map<String, Object>> listItems = sqlQuery.queryForList(sql, new Object[] { strApplyId });

			for (Map<String, Object> mapItem : listItems) {
				// 信息项编号
				String strItemId = mapItem.get("TZ_ZXBM_XXX_ID") == null ? ""
						: String.valueOf(mapItem.get("TZ_ZXBM_XXX_ID"));
				// 信息项名称
				String strItemName = mapItem.get("TZ_ZXBM_XXX_NAME") == null ? ""
						: String.valueOf(mapItem.get("TZ_ZXBM_XXX_NAME"));
				// 信息项是否必填
				String strBT = mapItem.get("TZ_ZXBM_XXX_BT") == null ? ""
						: String.valueOf(mapItem.get("TZ_ZXBM_XXX_BT"));
				// 信息项显示模式
				String strType = mapItem.get("TZ_ZXBM_XXX_ZSXS") == null ? ""
						: String.valueOf(mapItem.get("TZ_ZXBM_XXX_ZSXS"));

				// 判断站点语言
				if ("ENG".equals(tzSiteLang)) {
					sql = "select TZ_ZXBM_XXX_NAME from PS_TZ_ZXBM_XXX_E_T where TZ_ART_ID=? and TZ_ZXBM_XXX_ID=? and LANGUAGE_CD='ENG'";
					strItemName = sqlQuery.queryForObject(sql, new Object[] { strApplyId, strItemId }, "String");
				}

				String required = "";
				if ("Y".equals(strBT)) {
					strItemName = strItemName + strBtHtml;
					required = "required";
				}

				switch (strType) {
				case "1":
					if ("TZ_CYR_NAME".equals(strItemId)) {
						str_items_html = str_items_html
								+ tzGDObject.getHTMLText("HTML.TZEventsBundle.TZ_APPLY_REG_TEXT_PHONE_HTML",
										strItemName, strItemId, name, required);
					} else if ("TZ_ZY_SJ".equals(strItemId)) {
						str_items_html = str_items_html
								+ tzGDObject.getHTMLText("HTML.TZEventsBundle.TZ_APPLY_REG_TEXT_PHONE_HTML",
										strItemName, strItemId, mobile, required);
					} else if ("TZ_ZY_EMAIL".equals(strItemId)) {
						str_items_html = str_items_html
								+ tzGDObject.getHTMLText("HTML.TZEventsBundle.TZ_APPLY_REG_TEXT_PHONE_HTML",
										strItemName, strItemId, email, required);
					} else {
						str_items_html = str_items_html
								+ tzGDObject.getHTMLText("HTML.TZEventsBundle.TZ_APPLY_REG_TEXT_PHONE_HTML",
										strItemName, strItemId, "", required);
					}
					break;

				case "2":
					sql = "select TZ_XXX_TRANS_ID,TZ_XXX_TRANS_NAME from PS_TZ_XXX_TRANS_T where TZ_ART_ID=? and TZ_ZXBM_XXX_ID=? order by TZ_PX_XH";
					List<Map<String, Object>> listOpts = sqlQuery.queryForList(sql,
							new Object[] { strApplyId, strItemId });

					String strOptHtml = "<option><option>";

					for (Map<String, Object> mapOpt : listOpts) {
						String strOptId = mapOpt.get("TZ_XXX_TRANS_ID") == null ? ""
								: String.valueOf(mapOpt.get("TZ_XXX_TRANS_ID"));
						String strOptVal = mapOpt.get("TZ_XXX_TRANS_NAME") == null ? ""
								: String.valueOf(mapOpt.get("TZ_XXX_TRANS_NAME"));

						// 下拉值双语化
						if ("ENG".equals(tzSiteLang)) {
							sql = "select TZ_OPT_VALUE from PS_TZ_XXX_TR_EN_T where TZ_ART_ID=? and TZ_ZXBM_XXX_ID=? and TZ_XXX_TRANS_ID=? and LANGUAGE_CD='ENG'";
							strOptVal = sqlQuery.queryForObject(sql, new Object[] { strApplyId, strItemId, strOptId },
									"String");
						}

						strOptId = tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(strOptId);
						strOptHtml = strOptHtml + "<option value=\"" + strOptId + "\">" + strOptVal + "</option>";

					}

					str_items_html = str_items_html
							+ tzGDObject.getHTMLText("HTML.TZEventsBundle.TZ_APPLY_REG_SELECT_PHONE_HTML", strItemName,
									strItemId, strOptHtml, required);

					break;

				}

			}
			// <div class="Inquirycon-list">
			// <div class="Inquiry-list-label">%bind(:1)</div>
			// <div class="Inquiry-list-input"><input type="text"
			// class="Inquirylist-input-con" id="%bind(:2)" name="%bind(:2)"
			// value="%bind(:3)" ></div>
			// </div>

			// 验证码
			str_items_html = str_items_html + "<div class=\"Inquirycon-list\">" + authCode + strBtHtml + "</div>"
					+ "<div class=\"Inquiry-list-input\"><input type=\"text\" class=\"Inquirylist-input-con\" required id=\"tz_regCode\" name=\"tz_regCode\"/>"
					+ "<img id=\"regCodeImg\" src=\"\" onclick=\"createCode()\" alt=\"" + changeAuthCode
					+ "\" style=\"height:37px; margin-left:20px; margin-right:10px; vertical-align:middle;\"/>"
					+ "</div></div>";

			strRet = tzGDObject.getHTMLText("HTML.TZEventsBundle.TZ_APPLY_REG_FORM_PHONE_HEAD", onlineApplyText,
					str_items_html, submitBtn, strUrl, requireTips, timeOut, serverError, request.getContextPath(),
					path, titleName, tipsMsg);

			// 调用CMS的部分
			if (StringUtils.isBlank(strRet)) {
				return null;
			}
			Map<String, Object> root = new HashMap<String, Object>();
			CmsMenu menu = new CmsMenu();
			menu.setId(MenuId);
			menu.setSiteId(SiteId);
			root.put("menu", menu);
			root.put("CmsMenu", new MenuDirective());
			StringWriter out = new StringWriter();
			try {
				FreeMarkertUtils.processTemplate(strRet, "", root, out);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			strRet = out.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strRet;
	}

}
