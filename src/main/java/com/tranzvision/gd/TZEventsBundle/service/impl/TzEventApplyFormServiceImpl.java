/**
 * 
 */
package com.tranzvision.gd.TZEventsBundle.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzWebsiteLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdObjectServiceImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.CreateTaskServiceImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.SendSmsOrMalServiceImpl;
import com.tranzvision.gd.TZEventsBundle.dao.PsTzHdbmrClueTMapper;
import com.tranzvision.gd.TZEventsBundle.dao.PsTzLxfsinfoTblMapper;
import com.tranzvision.gd.TZEventsBundle.dao.PsTzNaudlistTMapper;
import com.tranzvision.gd.TZEventsBundle.model.PsTzHdbmrClueTKey;
import com.tranzvision.gd.TZEventsBundle.model.PsTzLxfsinfoTbl;
import com.tranzvision.gd.TZEventsBundle.model.PsTzNaudlistT;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.dao.PsTzXsxsBmbTMapper;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.dao.PsTzXsxsInfoTMapper;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.model.PsTzXsxsBmbT;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.model.PsTzXsxsInfoTWithBLOBs;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzException;
import com.tranzvision.gd.util.captcha.Patchca;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.security.TzFilterIllegalCharacter;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
import com.tranzvision.gd.util.sql.type.TzRecord;

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
	private GetSeqNum getSeqNum;

	@Autowired
	private TzEventActCodeServiceImpl tzEventActCodeServiceImpl;

	@Autowired
	PsTzNaudlistTMapper psTzNaudlistTMapper;
	
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	PsTzLxfsinfoTblMapper psTzLxfsinfoTblMapper;
	
	@Autowired
	private GetHardCodePoint getHardCodePoint;
	
	@Autowired
	private CreateTaskServiceImpl createTaskServiceImpl;
	
	@Autowired
	private SendSmsOrMalServiceImpl sendSmsOrMalServiceImpl;
	
	@Autowired
	private PsTzXsxsInfoTMapper psTzXsxsInfoTMapper;
	
	@Autowired
	private PsTzXsxsBmbTMapper psTzXsxsBmbTMapper;
	
	@Autowired
	private PsTzHdbmrClueTMapper psTzHdbmrClueTMapper;
	
	//用于控制访问量的信号变量，避免活动报名席位数过度竞争对服务器造成过大压力
	private static Semaphore registrationLockCounter = new Semaphore(10,true);
	

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
			
			
			String siteId = ""; //站点
			String chnlId = "";	//栏目
			if(jacksonUtil.containsKey("siteId")){
				siteId = jacksonUtil.getString("siteId");
			}
			if(jacksonUtil.containsKey("chnlId")){
				chnlId = jacksonUtil.getString("chnlId");
			}
			
			if(!"".equals(siteId) && !"".equals(chnlId)){
				// 根据siteId得到机构ID
				String sql = "select TZ_JG_ID from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
				String jgid = sqlQuery.queryForObject(sql, new Object[] { siteId }, "String");
				
				//活动发布对象，A-无限制，B-听众
				sql = "select TZ_PROJECT_LIMIT from PS_TZ_ART_REC_TBL where TZ_ART_ID=?";
				String artLimitType = sqlQuery.queryForObject(sql, new Object[] { strApplyId }, "String");
				
				// 如果用户未登录 直接 跳到登录页面
				if ("B".equals(artLimitType) 
						&& (oprid == null || oprid.equals(""))) {
					String contextUrl = request.getContextPath();
					if (!contextUrl.endsWith("/")) {
						contextUrl = contextUrl + "/";
					}
					contextUrl = contextUrl + "user/login/" + jgid + "/" + siteId;
					String code = "classid=art_view___"+ chnlId +"___" + strApplyId;
					contextUrl = contextUrl + "?" + code;
					
					StringBuffer html = new StringBuffer();
					html.append("<html><head><title></title></head>");
					html.append("<script language='javascript'>window.parent.document.location = '");
					html.append(contextUrl);
					html.append("'</script></body></html>");
					return html.toString();
				}
			}
			

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
			if("MEM".equals(orgid)){
				strRet = tzGDObject.getHTMLText("HTML.TZEventsBundle.TZ_APPLY_REG_FORM_HEAD2", str_items_html, strUrl, "",
						"", timeOut, serverError, onlineApplyText, tipsMsg, closeBtn, backBtn, submitBtn, requireTips, request.getContextPath());
			}if("MPACC".equals(orgid)){
				strRet = tzGDObject.getHTMLText("HTML.TZEventsBundle.TZ_APPLY_REG_FORM_HEAD3", str_items_html, strUrl, "",
						"", timeOut, serverError, onlineApplyText, tipsMsg, closeBtn, backBtn, submitBtn, requireTips, request.getContextPath());
			}if("IMBA".equals(orgid)){
				strRet = tzGDObject.getHTMLText("HTML.TZEventsBundle.TZ_APPLY_REG_FORM_HEAD4", str_items_html, strUrl, "",
						"", timeOut, serverError, onlineApplyText, tipsMsg, closeBtn, backBtn, submitBtn, requireTips, request.getContextPath());
			}else{
				strRet = tzGDObject.getHTMLText("HTML.TZEventsBundle.TZ_APPLY_REG_FORM_HEAD", str_items_html, strUrl, "",
						"", timeOut, serverError, onlineApplyText, tipsMsg, closeBtn, backBtn, submitBtn, requireTips, request.getContextPath());
			}
			

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
			String regFrom = "";//报名来源，B-网上报名,A-手机报名
			
			//M-手机活动报名，否则为PC活动报名
			String form = request.getParameter("FORM");
			if("M".equals(form)){
				regFrom = "A";
			}else{
				regFrom = "B";
			}

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
			
			String unEnableError = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPLICATION_MSG",
					"MSG23", tzSiteLang, "当前活动尚未启用在线报名！", "Online registration is not currently enabled for the event!");
			String timeError = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPLICATION_MSG",
					"MSG24", tzSiteLang, "当前时间不在活动报名报名时间内，在线报名未开始或已结束！", "The online registration has not started or has finished!");

			// 校验验证码
			Patchca patchca = new Patchca();
			if (!patchca.verifyToken(request, strAuthCode)) {
				strResult = "1";
				strResultMsg = authCodeError;
			} else {
				// 获取活动显示模式
				Date dateNow = new Date();
				sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzGetEventDisplayMode");
				Map<String, Object> mapData = sqlQuery.queryForMap(sql, new Object[] { dateNow, dateNow, dateNow, dateNow, strApplyId });

				// 是否有效记录,Y-在报名时间内，B-报名为开始，E-报名已结束
				String validTD = "";
				// 是否启用在线报名
				String strQy_zxbm = "";
				
				if (mapData != null) {
					validTD = mapData.get("VALID_TD") == null ? "" : String.valueOf(mapData.get("VALID_TD"));
					strQy_zxbm = mapData.get("TZ_QY_ZXBM") == null ? "" : String.valueOf(mapData.get("TZ_QY_ZXBM"));
				}
				
				if ("Y".equals(strQy_zxbm)) {
					//报名时间限制，只有在报名时间内才可报名
					 if(!"Y".equals(validTD)){
						 throw new TzException(timeError);
					 }
				}else{
					throw new TzException(unEnableError);
				}
				
				
				String oprid = tzWebsiteLoginServiceImpl.getLoginedUserOprid(request);
				
				//活动发布对象，A-无限制，B-听众
				sql = "select TZ_PROJECT_LIMIT from PS_TZ_ART_REC_TBL where TZ_ART_ID=?";
				String artLimitType = sqlQuery.queryForObject(sql, new Object[] { strApplyId }, "String");

				if("B".equals(artLimitType) 
						&& (oprid == null || "".equals(oprid))){
					//未登录
					strResult = "1";
					strResultMsg = "报名失败，您尚未登录或登录超时，请重新登录。";
				}else{
					//活动听众判断
					boolean isInAud = false;
					String audSql = "select TZ_AUD_ID from PS_TZ_ART_AUDIENCE_T where TZ_ART_ID=? and exists(select 'X' from PS_TZ_ART_REC_TBL where TZ_ART_ID=PS_TZ_ART_AUDIENCE_T.TZ_ART_ID and TZ_PROJECT_LIMIT='B')";
					List<Map<String,Object>> audList = sqlQuery.queryForList(audSql, new Object[]{ strApplyId });
					if(audList != null && audList.size() > 0){
						for(Map<String,Object> audMap: audList){
							String audId = audMap.get("TZ_AUD_ID") == null ? "" : audMap.get("TZ_AUD_ID").toString();
							String inAudSql = "select 'Y' from PS_TZ_AUD_LIST_T where TZ_AUD_ID=? and TZ_DXZT<>'N' and OPRID=? limit 1";
							String inAud = sqlQuery.queryForObject(inAudSql, new Object[]{ audId, oprid }, "String");
							if("Y".equals(inAud)){
								isInAud = true;
							}
						}
					}else{
						isInAud = true;
					}
					
					if(isInAud){
						
						/* 查询报名人数前就要锁表，不然同时报名的话，就可能超过允许报名的人数 */
						//同一个应用服务内只允许10个考生同时进入面试预约排队，否则报系统忙，请稍候再试。
						if(registrationLockCounter.getQueueLength() >= 10 || registrationLockCounter.tryAcquire(3000,TimeUnit.MILLISECONDS) == false)
						{
							throw new Exception("系统忙，请稍候再试。");
						}
						
						Semaphore tmpSemaphore = null;
						boolean isLocked = false;
						boolean hasTmpSemaphore = false;
						try{
							//获取当前活动对应的信号灯
							Map.Entry<String,Semaphore> tmpSemaphoreObject = tzGDObject.getSemaphore("com.tranzvision.gd.TZEventsBundle.service.impl.TzEventApplyFormServiceImpl-20170717", strApplyId);
							
							if(tmpSemaphoreObject == null || tmpSemaphoreObject.getKey() == null || tmpSemaphoreObject.getValue() == null)
							{
								//如果返回的信号灯为空，报系统忙，请稍后再试
								throw new Exception("系统忙，请稍候再试。");
							}else{
								tmpSemaphore = tmpSemaphoreObject.getValue();
								
								//获取的信号灯
								tmpSemaphore.acquire();
								
								hasTmpSemaphore = true;
							}
							
							//利用主键冲突异常来控制同一时刻只能有一个人活动报名
							try
							{
								TzRecord lockRecord = tzGDObject.createRecord("PS_TZ_HDBM_LOCK_TBL");
								lockRecord.setColumnValue("TZ_HD_ID", strApplyId);
								lockRecord.setColumnValue("OPRID", oprid);
								
								if(lockRecord.insert() == false){
									throw new TzException("系统忙，请稍候再试。");
								}else{
									isLocked = true;
								}
							}
							catch(Exception e)
							{
								 throw new TzException("系统忙，请稍候再试。");
							}
						
							
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
									mobileRept = sqlQuery.queryForObject(sql,new Object[] { strApplyId, str_bmr_phone, str_bmr_email }, "String");
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
									emailRept = sqlQuery.queryForObject(sql, new Object[] { strApplyId, str_bmr_email, oprid },"String");
			
									if (null != str_bmr_phone && !"".equals(str_bmr_phone)) {
										sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzCheckBmrMobileByOprid");
										mobileRept = sqlQuery.queryForObject(sql, new Object[] { strApplyId, str_bmr_phone, oprid },"String");
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
									
									//如果strBmrId=0,获取报名人编号失败
									if("0".equals(strBmrId)){
										throw new Exception("系统忙，请稍候再试。");
									}
									createOrupdate = 0;
								}
		
							
								// 已报名数
								sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzGetEventAppliedNum");
								int num_apply = sqlQuery.queryForObject(sql, new Object[] { strApplyId }, "int");
			
								// 活动报名表
								PsTzNaudlistT psTzNaudlistT = new PsTzNaudlistT();
								psTzNaudlistT.setTzArtId(strApplyId);
								psTzNaudlistT.setTzHdBmrId(strBmrId);
								psTzNaudlistT.setTzCyrName(str_bmr_name);
								psTzNaudlistT.setTzRegTime(new Date());
								// 报名来源为:B-网上报名,A-手机报名
								psTzNaudlistT.setTzZxbmLy(regFrom);
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
								String act_qd_id = tzEventActCodeServiceImpl.generateActCode(strApplyId,psTzLxfsinfoTbl.getTzZySj());
								if("0".equals(act_qd_id)){
									//生成签到码失败
									throw new Exception("系统忙，请稍候再试。");
								}
								psTzNaudlistT.setTzHdQdm(act_qd_id);
			
								/* 席位数为0表示不限制人数 */
								if (num_seats == 0 || num_seats > num_apply) {
									// 报名成功
									psTzNaudlistT.setTzNregStat("1");
									strResult = "3";
									strResultMsg = applySuccess;
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
							}
						}catch(Exception e){
							strResult = "1";
							strResultMsg = "系统忙，请稍候再试。";
						}finally {
							if(isLocked){
								//报名完成后删除插入PS_TZ_HDBM_LOCK_TBL中的数据
								sqlQuery.update("delete from PS_TZ_HDBM_LOCK_TBL where TZ_HD_ID=?", new Object[]{ strApplyId });
							}
							
							if(hasTmpSemaphore){
								tmpSemaphore.release();
							}
							
							registrationLockCounter.release();
						}
						
						//报名成功发送站内信
						if("3".equals(strResult)){
							//发送报名成功站内信
							try{
								sql = "SELECT TZ_REALNAME FROM PS_TZ_REG_USER_T WHERE OPRID=?";
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
								
								//报名成功创建线索
								tzCreateClue(strApplyId, strBmrId);
							}catch(NullPointerException nullEx){
								//没有配置邮件模板
								nullEx.printStackTrace();
							}
						}
					}else{
						//不在听众内，报名失败
						strResult = "1";
						strResultMsg = "报名失败！很抱歉，活动尚未对您开放报名。";
					}
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

	/**
	 * 创建线索
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private void tzCreateClue(String activityId, String bmrId){
		System.out.println("activityId：" + activityId);
		System.out.println("bmrId：" + bmrId);
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		
		String orgId = "";
		int count = 0;
		if(jacksonUtil.containsKey("orgId")){
			orgId = jacksonUtil.getString("orgId");
		}else{
			orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		}
		
		String currOprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		
		String hasLeadName = "";
		String sql = "select A.TZ_CYR_NAME,A.OPRID,B.TZ_ZY_SJ,B.TZ_ZY_EMAIL from PS_TZ_NAUDLIST_T A left join PS_TZ_LXFSINFO_TBL B on(B.TZ_LXFS_LY='HDBM' and B.TZ_LYDX_ID=A.TZ_HD_BMR_ID) where TZ_ART_ID=? and TZ_HD_BMR_ID=?";
		Map<String,Object> bmrMap = sqlQuery.queryForMap(sql, new Object[]{ activityId, bmrId });
		
		if(bmrMap != null){
			String name = bmrMap.get("TZ_CYR_NAME") == null ? "" : bmrMap.get("TZ_CYR_NAME").toString();
			String oprid = bmrMap.get("OPRID") == null ? "" : bmrMap.get("OPRID").toString();
			String mobile = bmrMap.get("TZ_ZY_SJ") == null ? "" : bmrMap.get("TZ_ZY_SJ").toString();
			String email = bmrMap.get("TZ_ZY_EMAIL") == null ? "" : bmrMap.get("TZ_ZY_EMAIL").toString();
			
			String leadSql = "select TZ_LEAD_ID from PS_TZ_HDBMR_CLUE_T A where TZ_HD_BMR_ID=? and exists(select 'Y' from PS_TZ_XSXS_INFO_T where TZ_LEAD_ID=A.TZ_LEAD_ID and TZ_JG_ID=?) limit 0,1";
			String leadId = sqlQuery.queryForObject(leadSql, new Object[]{ bmrId, orgId }, "String");
			
			if(leadId != null && !"".equals(leadId)){
				count ++;
				if(count < 10){
					if("".equals(hasLeadName)){
						hasLeadName = name;
					}else{
						hasLeadName += "，" + name;
					}
				}
			}else{
				String TZ_LEAD_ID = String.valueOf(getSeqNum.getSeqNum("TZ_XSXS_INFO_T", "TZ_LEAD_ID"));
				
				PsTzXsxsInfoTWithBLOBs psTzXsxsInfoT = new PsTzXsxsInfoTWithBLOBs();
				psTzXsxsInfoT.setTzLeadId(TZ_LEAD_ID);
				psTzXsxsInfoT.setTzJgId(orgId);

				psTzXsxsInfoT.setTzRsfcreateWay("E"); /*营销活动*/
				psTzXsxsInfoT.setTzLeadStatus("A");
				
				psTzXsxsInfoT.setTzRealname(name);
				psTzXsxsInfoT.setTzKhOprid(oprid);
				psTzXsxsInfoT.setTzEmail(email);
				psTzXsxsInfoT.setTzMobile(mobile);
				psTzXsxsInfoT.setRowAddedDttm(new Date());
				psTzXsxsInfoT.setRowAddedOprid(currOprid);
				psTzXsxsInfoT.setRowLastmantDttm(new Date());
				psTzXsxsInfoT.setRowLastmantOprid(currOprid);
				
				int rtn = psTzXsxsInfoTMapper.insert(psTzXsxsInfoT);
				
				if(rtn > 0){
					PsTzHdbmrClueTKey psTzHdbmrClueTKey = new PsTzHdbmrClueTKey();
					psTzHdbmrClueTKey.setTzHdBmrId(bmrId);
					psTzHdbmrClueTKey.setTzLeadId(TZ_LEAD_ID);
					
					psTzHdbmrClueTMapper.insert(psTzHdbmrClueTKey);
					
					//线索关联报名表
					sql = "select max(TZ_APP_INS_ID) as TZ_APP_INS_ID from PS_TZ_FORM_WRK_T where OPRID=?";
					Long appInsId = sqlQuery.queryForObject(sql, new Object[]{ oprid }, "Long");
					if(appInsId != null && appInsId > 0){
						PsTzXsxsBmbT psTzXsxsBmbT = new PsTzXsxsBmbT(); 
						psTzXsxsBmbT.setTzLeadId(TZ_LEAD_ID);
						psTzXsxsBmbT.setTzAppInsId(appInsId);
						psTzXsxsBmbT.setRowAddedDttm(new Date());
						psTzXsxsBmbT.setRowAddedOprid(currOprid);
						psTzXsxsBmbT.setRowLastmantDttm(new Date());
						psTzXsxsBmbT.setRowLastmantOprid(currOprid);
						
						psTzXsxsBmbTMapper.insert(psTzXsxsBmbT);
					}
				}
			}
		}
	}
}
