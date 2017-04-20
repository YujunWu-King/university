/**
 * 
 */
package com.tranzvision.gd.TZEventsBundle.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzWebsiteLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdObjectServiceImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.CreateTaskServiceImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.SendSmsOrMalServiceImpl;
import com.tranzvision.gd.TZEventsBundle.dao.PsTzLxfsinfoTblMapper;
import com.tranzvision.gd.TZEventsBundle.dao.PsTzNaudlistTMapper;
import com.tranzvision.gd.TZEventsBundle.model.PsTzLxfsinfoTbl;
import com.tranzvision.gd.TZEventsBundle.model.PsTzNaudlistT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.captcha.Patchca;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.security.TzFilterIllegalCharacter;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.MySqlLockService;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 报名注册页面显示，信息项根据活动配置动态显示，原PS：TZ_APPONLINE_PKG:AppRegAndSubmint
 * 
 * @author SHIHUA
 * @since 2016-03-01
 */
@Service("com.tranzvision.gd.TZEventsBundle.service.impl.TzEventApplyFormServiceImpl")
public class TzEventApplyFormServiceImpl extends FrameworkImpl {
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
	private MySqlLockService mySqlLockService;

	@Autowired
	private GetSeqNum getSeqNum;

	@Autowired
	private TzEventActCodeServiceImpl tzEventActCodeServiceImpl;

	@Autowired
	PsTzNaudlistTMapper psTzNaudlistTMapper;

	@Autowired
	PsTzLxfsinfoTblMapper psTzLxfsinfoTblMapper;
	
	@Autowired
	private GetHardCodePoint getHardCodePoint;
	
	@Autowired
	private CreateTaskServiceImpl createTaskServiceImpl;
	
	@Autowired
	private SendSmsOrMalServiceImpl sendSmsOrMalServiceImpl;

	/**
	 * 显示在线报名注册页面
	 */
	@Override
	public String tzGetHtmlContent(String strParams) {
		String strRet = "";
		try {
			JacksonUtil jacksonUtil = new JacksonUtil();
			jacksonUtil.json2Map(strParams);

			// 活动ID
			String strApplyId = jacksonUtil.getString("APPLYID");
			strApplyId = tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(strApplyId);

			// 当前登录人登录账号
			String userDLZH = tzWebsiteLoginServiceImpl.getLoginedUserDlzhid(request);

			// 当前登录人所属机构
			String orgid = tzWebsiteLoginServiceImpl.getLoginedUserOrgid(request);
			
			String oprid = tzWebsiteLoginServiceImpl.getLoginedUserOprid(request);

			// 统一URL
			String strUrl = request.getContextPath() + "/dispatcher";

			String sql = "select TZ_REALNAME,TZ_EMAIL,TZ_MOBILE from PS_TZ_AQ_YHXX_TBL where TZ_DLZH_ID=? and TZ_JG_ID=?";
			Map<String, Object> mapUserInfo = sqlQuery.queryForMap(sql, new Object[] { userDLZH, orgid });

			// 姓名
			String name = "";
			// 邮箱
			String email = "";
			// 手机
			String mobile = "";

			if (null != mapUserInfo) {
				name = mapUserInfo.get("TZ_REALNAME") == null ? "" : String.valueOf(mapUserInfo.get("TZ_REALNAME"));
				//email = mapUserInfo.get("TZ_EMAIL") == null ? "" : String.valueOf(mapUserInfo.get("TZ_EMAIL"));
				//mobile = mapUserInfo.get("TZ_MOBILE") == null ? "" : String.valueOf(mapUserInfo.get("TZ_MOBILE"));
			}
			
			/*手机邮箱信息从联系方式表中获取*/
			sql = "select TZ_ZY_EMAIL,TZ_ZY_SJ from PS_TZ_LXFSINFO_TBL where TZ_LXFS_LY='ZCYH' and TZ_LYDX_ID=?";
			Map<String, Object> mapUserEmlPhone = sqlQuery.queryForMap(sql, new Object[] { oprid });
			if(mapUserEmlPhone != null){
				email = mapUserEmlPhone.get("TZ_ZY_EMAIL") == null ? "" : String.valueOf(mapUserEmlPhone.get("TZ_ZY_EMAIL"));
				mobile = mapUserEmlPhone.get("TZ_ZY_SJ") == null ? "" : String.valueOf(mapUserEmlPhone.get("TZ_ZY_SJ"));
			}

			// 必填项的html标识
			String strBtHtml = "<span style=\"color:#F00\">*</span>";

			// 活动ID的隐藏域
			String str_items_html = "<input type=\"hidden\" id=\"TZ_APPLY_ID\" name=\"TZ_APPLY_ID\" value=\""
					+ strApplyId + "\"/>";

			// 双语化
			sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzGetSiteLang");
			String tzSiteLang = sqlQuery.queryForObject(sql, new Object[] { strApplyId }, "String");

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
			String closeBtn = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPLICATION_MSG", "MSG12",
					tzSiteLang, "关闭", "Close");
			String backBtn = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPLICATION_MSG", "MSG13",
					tzSiteLang, "返回", "Back");
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
					strItemName = strBtHtml + strItemName;
					required = "required";
				}

				switch (strType) {
				case "1":
					if ("TZ_CYR_NAME".equals(strItemId)) {
						str_items_html = str_items_html + tzGDObject.getHTMLText(
								"HTML.TZEventsBundle.TZ_APPLY_REG_TEXT_HTML", strItemName, required, strItemId, name);
					} else if ("TZ_ZY_SJ".equals(strItemId)) {
						str_items_html = str_items_html + tzGDObject.getHTMLText(
								"HTML.TZEventsBundle.TZ_APPLY_REG_TEXT_HTML", strItemName, required, strItemId, mobile);
					} else if ("TZ_ZY_EMAIL".equals(strItemId)) {
						str_items_html = str_items_html + tzGDObject.getHTMLText(
								"HTML.TZEventsBundle.TZ_APPLY_REG_TEXT_HTML", strItemName, required, strItemId, email);
					} else {
						str_items_html = str_items_html + tzGDObject.getHTMLText(
								"HTML.TZEventsBundle.TZ_APPLY_REG_TEXT_HTML", strItemName, required, strItemId, "");
					}
					break;

				case "2":
					sql = "select TZ_XXX_TRANS_ID,TZ_XXX_TRANS_NAME from PS_TZ_XXX_TRANS_T where TZ_ART_ID=? and TZ_ZXBM_XXX_ID=? order by TZ_PX_XH";
					List<Map<String, Object>> listOpts = sqlQuery.queryForList(sql,
							new Object[] { strApplyId, strItemId });

					String strOptHtml = "<option></option>";

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
							+ tzGDObject.getHTMLText("HTML.TZEventsBundle.TZ_APPLY_REG_SELECT_HTML", strItemName,
									required, strItemId, strOptHtml);

					break;

				}

			}

			// 验证码
			str_items_html = str_items_html + "<li><strong>" + strBtHtml + authCode + "：</strong>"
					+ "<input type=\"text\" class=\"apply-items inptype\" id=\"tz_regCode\" name=\"tz_regCode\" required style=\"width:120px\" />"
					+ "<img id=\"regCodeImg\" src=\"\" onclick=\"createCode()\" alt=\"" + changeAuthCode
					+ "\" style=\"height:37px; margin-left:20px; margin-right:10px; vertical-align:middle;\"/>"
					+ "</li>";

			strRet = tzGDObject.getHTMLText("HTML.TZEventsBundle.TZ_APPLY_REG_FORM_HEAD", str_items_html, strUrl, "",
					"", timeOut, serverError, onlineApplyText, tipsMsg, closeBtn, backBtn, submitBtn, requireTips, request.getContextPath());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return strRet;
	}

	/*********************************************************************************
	 * 提交在线报名注册信息： 1、在线报名，如果当前报名人员登录了，不能对同一个活动报名>=2次，只有撤销后才能再次报名；
	 * 2、撤销后再次报名时，直接修改撤销报名记录，不新增记录； 3、如果登录了，同一个活动邮箱不能重复，手机如果填了值，也不能重复；
	 * 4、对于没有登录的报名人，直接根据邮箱查询该人员是否已经报名；
	 *********************************************************************************/
	@Override
	@Transactional
	public String tzGetJsonData(String strParams) {

		String strRet = "";
		String strResult = "";
		String strResultMsg = "";
		String strApplyId = "";
		String strBmrId = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			jacksonUtil.json2Map(strParams);

			// 活动ID
			strApplyId = jacksonUtil.getString("TZ_APPLY_ID");
			// 姓名
			String str_bmr_name = jacksonUtil.getString("TZ_CYR_NAME");
			// 手机
			String str_bmr_phone = jacksonUtil.getString("TZ_ZY_SJ");
			// 邮箱
			String str_bmr_email = jacksonUtil.getString("TZ_ZY_EMAIL");

			// 验证码
			String strAuthCode = jacksonUtil.getString("tz_regCode");

			// 是否重复报名，根据姓名和手机查重
			String isRept = "";
			String reptDesc = "";

			// 双语化
			String sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzGetSiteLang");
			String tzSiteLang = sqlQuery.queryForObject(sql, new Object[] { strApplyId }, "String");

			String authCodeError = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPLICATION_MSG",
					"MSG17", tzSiteLang, "您输入的验证码有误！", "Auth Code Error");
			String emailError = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPLICATION_MSG",
					"MSG18", tzSiteLang, "您输入的邮箱在当前活动中已经报过名！", "Your Email has been Registered");
			String applySuccess = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPLICATION_MSG",
					"MSG19", tzSiteLang, "报名成功！", "Apply successfully");
			String waitingStatus = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPLICATION_MSG",
					"MSG20", tzSiteLang, "报名席位数已满，您现在处于等候状态！",
					"Sign up to post is full, you are now in a state of waiting!");
			String applyError = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPLICATION_MSG",
					"MSG21", tzSiteLang, "在当前活动您已经报名，不能重复报名！", "You have registered for the event!");
			String mobileError = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPLICATION_MSG",
					"MSG22", tzSiteLang, "您输入的手机在当前活动中已经报过名！", "Your mobile phone has been registered!");

			// 校验验证码
			Patchca patchca = new Patchca();
			if (!patchca.verifyToken(request, strAuthCode)) {
				strResult = "1";
				strResultMsg = authCodeError;
			} else {

				String oprid = tzWebsiteLoginServiceImpl.getLoginedUserOprid(request);

				String mobileRept = "";
				String emailRept = "";

				if ("".equals(oprid)) {
					// 未登录
					// 判断是否重复报名
					sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzCheckNotLoginBmrEmail");
					isRept = sqlQuery.queryForObject(sql, new Object[] { strApplyId, str_bmr_email }, "String");
					if ("Y".equals(isRept)) {
						reptDesc = emailError;
					} else if (null != str_bmr_phone && !"".equals(str_bmr_phone)) {
						sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzCheckNotLoginBmrMobile");
						mobileRept = sqlQuery.queryForObject(sql,
								new Object[] { strApplyId, str_bmr_phone, str_bmr_email }, "String");
					}

				} else {
					// 已登录
					// 判断是否重复报名
					sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzCheckBmrOprid");
					isRept = sqlQuery.queryForObject(sql, new Object[] { strApplyId, oprid }, "String");

					if ("Y".equals(isRept)) {
						reptDesc = applyError;
					} else {

						sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzCheckBmrEmailByOprid");
						emailRept = sqlQuery.queryForObject(sql, new Object[] { strApplyId, str_bmr_email, oprid },
								"String");

						if (null != str_bmr_phone && !"".equals(str_bmr_phone)) {
							sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzCheckBmrMobileByOprid");
							mobileRept = sqlQuery.queryForObject(sql, new Object[] { strApplyId, str_bmr_phone, oprid },
									"String");
						}

					}

				}

				if ("Y".equals(isRept)) {
					// 重复报名
					strResult = "1";
					strResultMsg = reptDesc;
				} else if ("Y".equals(emailRept)) {
					// 邮箱重复
					strResult = "1";
					strResultMsg = emailError;
				} else if ("Y".equals(mobileRept)) {
					// 手机重复
					strResult = "1";
					strResultMsg = mobileError;
				} else {
					sql = "select TZ_XWS from PS_TZ_ART_HD_TBL where TZ_ART_ID=?";
					int num_seats = sqlQuery.queryForObject(sql, new Object[] { strApplyId }, "int");

					// 当前报名人是否曾经报名过，但被撤销报名
					int createOrupdate = 1;
					if ("".equals(oprid)) {
						// 未登录
						sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzGetEventNotLoginBmrId");
						strBmrId = sqlQuery.queryForObject(sql, new Object[] { strApplyId, str_bmr_email }, "String");
					} else {
						sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzGetEventLoginBmrId");
						strBmrId = sqlQuery.queryForObject(sql, new Object[] { strApplyId, oprid }, "String");
					}

					if ("".equals(strBmrId) || null == strBmrId) {
						strBmrId = String.valueOf(getSeqNum.getSeqNum("TZ_LXFSINFO_TBL", "TZ_LYDX_ID"));
						createOrupdate = 0;
					}

					/* 查询报名人数前就要锁表，不然同时报名的话，就可能超过允许报名的人数 */
					mySqlLockService.lockRow(sqlQuery, "TZ_NAUDLIST_T");

					// 已报名数
					sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzGetEventAppliedNum");
					int num_apply = sqlQuery.queryForObject(sql, new Object[] { strApplyId }, "int");

					// 活动报名表
					PsTzNaudlistT psTzNaudlistT = new PsTzNaudlistT();
					psTzNaudlistT.setTzArtId(strApplyId);
					psTzNaudlistT.setTzHdBmrId(strBmrId);
					psTzNaudlistT.setTzCyrName(str_bmr_name);
					psTzNaudlistT.setTzRegTime(new Date());
					// 报名来源为:网上报名
					psTzNaudlistT.setTzZxbmLy("B");
					psTzNaudlistT.setOprid(oprid);

					// 联系方式表
					PsTzLxfsinfoTbl psTzLxfsinfoTbl = new PsTzLxfsinfoTbl();
					psTzLxfsinfoTbl.setTzLxfsLy("HDBM");
					psTzLxfsinfoTbl.setTzLydxId(strBmrId);

					sql = "select TZ_ZXBM_XXX_ID from PS_TZ_ZXBM_XXX_T where TZ_ART_ID = ? order by TZ_PX_XH";
					List<Map<String, Object>> listItems = sqlQuery.queryForList(sql, new Object[] { strApplyId });

					for (Map<String, Object> mapItem : listItems) {
						String str_field_id = mapItem.get("TZ_ZXBM_XXX_ID") == null ? ""
								: String.valueOf(mapItem.get("TZ_ZXBM_XXX_ID"));
						if ("".equals(str_field_id)) {
							continue;
						}
						// 报名人联系信息存储在联系方式表TZ_LXFSINFO_TBL中，其他字段写入报名表中
						String strXXXVal = jacksonUtil.getString(str_field_id);
						switch (str_field_id) {
						case "TZ_ZY_SJ":
							psTzLxfsinfoTbl.setTzZySj(strXXXVal);
							break;
						case "TZ_ZY_EMAIL":
							psTzLxfsinfoTbl.setTzZyEmail(strXXXVal);
							break;

						case "TZ_CYR_NAME":
							psTzNaudlistT.setTzCyrName(strXXXVal);
							break;
						case "TZ_ZXBM_XXX_001":
							psTzNaudlistT.setTzZxbmXxx001(strXXXVal);
							break;
						case "TZ_ZXBM_XXX_002":
							psTzNaudlistT.setTzZxbmXxx002(strXXXVal);
							break;
						case "TZ_ZXBM_XXX_003":
							psTzNaudlistT.setTzZxbmXxx003(strXXXVal);
							break;
						case "TZ_ZXBM_XXX_004":
							psTzNaudlistT.setTzZxbmXxx004(strXXXVal);
							break;
						case "TZ_ZXBM_XXX_005":
							psTzNaudlistT.setTzZxbmXxx005(strXXXVal);
							break;
						case "TZ_ZXBM_XXX_006":
							psTzNaudlistT.setTzZxbmXxx006(strXXXVal);
							break;
						case "TZ_ZXBM_XXX_007":
							psTzNaudlistT.setTzZxbmXxx007(strXXXVal);
							break;
						case "TZ_ZXBM_XXX_008":
							psTzNaudlistT.setTzZxbmXxx008(strXXXVal);
							break;
						case "TZ_ZXBM_XXX_009":
							psTzNaudlistT.setTzZxbmXxx009(strXXXVal);
							break;
						case "TZ_ZXBM_XXX_010":
							psTzNaudlistT.setTzZxbmXxx010(strXXXVal);
							break;
						case "TZ_ZXBM_XXX_011":
							psTzNaudlistT.setTzZxbmXxx011(strXXXVal);
							break;
						case "TZ_ZXBM_XXX_012":
							psTzNaudlistT.setTzZxbmXxx012(strXXXVal);
							break;
						case "TZ_ZXBM_XXX_013":
							psTzNaudlistT.setTzZxbmXxx013(strXXXVal);
							break;
						case "TZ_ZXBM_XXX_014":
							psTzNaudlistT.setTzZxbmXxx014(strXXXVal);
							break;
						case "TZ_ZXBM_XXX_015":
							psTzNaudlistT.setTzZxbmXxx015(strXXXVal);
							break;
						case "TZ_ZXBM_XXX_016":
							psTzNaudlistT.setTzZxbmXxx016(strXXXVal);
							break;
						case "TZ_ZXBM_XXX_017":
							psTzNaudlistT.setTzZxbmXxx017(strXXXVal);
							break;
						case "TZ_ZXBM_XXX_018":
							psTzNaudlistT.setTzZxbmXxx018(strXXXVal);
							break;
						case "TZ_ZXBM_XXX_019":
							psTzNaudlistT.setTzZxbmXxx019(strXXXVal);
							break;
						case "TZ_ZXBM_XXX_020":
							psTzNaudlistT.setTzZxbmXxx020(strXXXVal);
							break;
						}

					}

					// 生成活动签到码
					String act_qd_id = tzEventActCodeServiceImpl.generateActCode(strApplyId,
							psTzLxfsinfoTbl.getTzZySj());
					psTzNaudlistT.setTzHdQdm(act_qd_id);

					/* 席位数为0表示不限制人数 */
					if (num_seats == 0 || num_seats > num_apply) {
						// 报名成功
						psTzNaudlistT.setTzNregStat("1");
						strResult = "3";
						strResultMsg = applySuccess;
						
						//发送报名成功站内信
						try{
							sql = "SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=?";
							String name = sqlQuery.queryForObject(sql, new Object[]{ oprid }, "String");
							//报名成功成功站内信模板
							String znxModel = getHardCodePoint.getHardCodePointVal("TZ_HDBM_CG_ZNX_TMP");
							//当前机构
							String jgid = tzWebsiteLoginServiceImpl.getLoginedUserOrgid(request);
							
							//创建邮件任务实例
							String taskId = createTaskServiceImpl.createTaskIns(jgid, znxModel, "ZNX", "A");
							// 创建邮件发送听众
							String crtAudi = createTaskServiceImpl.createAudience(taskId,jgid,"活动报名成功站内信通知", "JSRW");
							//添加听众成员
							boolean bl = createTaskServiceImpl.addAudCy(crtAudi, name, "", "", "", "", "", "", oprid, "", strApplyId, "");
							if(bl){
								sendSmsOrMalServiceImpl.send(taskId, "");
							}
						}catch(NullPointerException nullEx){
							//没有配置邮件模板
							nullEx.printStackTrace();
						}
					} else {
						// 等待队列
						psTzNaudlistT.setTzNregStat("4");
						strResult = "4";
						strResultMsg = waitingStatus;
					}

					if (createOrupdate == 0) {
						psTzNaudlistTMapper.insertSelective(psTzNaudlistT);
						psTzLxfsinfoTblMapper.insertSelective(psTzLxfsinfoTbl);
					} else {
						psTzNaudlistTMapper.updateByPrimaryKeySelective(psTzNaudlistT);
						psTzLxfsinfoTblMapper.updateByPrimaryKeySelective(psTzLxfsinfoTbl);
					}

					// 解锁
					mySqlLockService.unlockRow(sqlQuery);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			strResult = "1";
			strResultMsg = e.getMessage();
		}

		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("result", strResult);
		mapRet.put("resultDesc", strResultMsg);
		mapRet.put("artid", strApplyId);
		mapRet.put("bmrid", strBmrId);

		strRet = jacksonUtil.Map2json(mapRet);

		return strRet;

	}

}
