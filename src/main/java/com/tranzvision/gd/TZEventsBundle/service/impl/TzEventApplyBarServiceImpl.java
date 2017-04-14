/**
 * 
 */
package com.tranzvision.gd.TZEventsBundle.service.impl;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
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
import com.tranzvision.gd.TZEventsBundle.dao.PsTzNaudlistTMapper;
import com.tranzvision.gd.TZEventsBundle.model.PsTzNaudlistT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.sql.MySqlLockService;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 显示在线报名条，原PS：TZ_APPONLINE_PKG:AppbarDisplay
 * 
 * @author SHIHUA
 * @since 2016-03-01
 */
@Service("com.tranzvision.gd.TZEventsBundle.service.impl.TzEventApplyBarServiceImpl")
public class TzEventApplyBarServiceImpl extends FrameworkImpl {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TZGDObject tzGDObject;

	@Autowired
	private TzWebsiteLoginServiceImpl tzWebsiteLoginServiceImpl;

	@Autowired
	private GdObjectServiceImpl gdObjectServiceImpl;

	@Autowired
	private PsTzNaudlistTMapper psTzNaudlistTMapper;

	@Autowired
	private MySqlLockService mySqlLockService;
	
	@Autowired
	private GetHardCodePoint getHardCodePoint;
	
	@Autowired
	private CreateTaskServiceImpl createTaskServiceImpl;
	
	@Autowired
	private SendSmsOrMalServiceImpl sendSmsOrMalServiceImpl;

	/**
	 * 显示在线报名条
	 * 
	 * @param strParams
	 * @return
	 */
	@Override
	public String tzGetHtmlContent(String strParams) {

		String strRet = "";

		try {

			Date dateNow = new Date();

			JacksonUtil jacksonUtil = new JacksonUtil();
			jacksonUtil.json2Map(strParams);

			String strApplyId = jacksonUtil.getString("APPLYID");

			String oprid = tzWebsiteLoginServiceImpl.getLoginedUserOprid(request);

			if (null != strApplyId && !"".equals(strApplyId)) {

				// 已报名人数
				String sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzGetEventAppliedNum");
				int numYBM = sqlQuery.queryForObject(sql, new Object[] { strApplyId }, "int");

				// 等待人数
				sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzGetEventWaitingNum");
				int numWait = sqlQuery.queryForObject(sql, new Object[] { strApplyId }, "int");

				// 获取活动显示模式
				sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzGetEventDisplayMode");
				Map<String, Object> mapData = sqlQuery.queryForMap(sql, new Object[] { strApplyId, dateNow, dateNow });

				// 是否有效记录
				String validTD = "";
				// 显示模式
				String tzXSMS = "";
				// 是否启用在线报名
				String strQy_zxbm = "";
				// 活动席位数
				int numActXW = 0;
				// 若席位数为0，则代表不限制席位数，前台显示为“-”
				String strActXW = "-";

				if (mapData != null) {

					validTD = mapData.get("VALID_TD") == null ? "" : String.valueOf(mapData.get("VALID_TD"));
					tzXSMS = mapData.get("TZ_XSMS") == null ? "" : String.valueOf(mapData.get("TZ_XSMS"));
					strQy_zxbm = mapData.get("TZ_QY_ZXBM") == null ? "" : String.valueOf(mapData.get("TZ_QY_ZXBM"));
					numActXW = mapData.get("TZ_XWS") == null ? 0
							: Integer.parseInt(String.valueOf(mapData.get("TZ_XWS")));
					if (numActXW > 0) {
						strActXW = String.valueOf(numActXW);
					}
				}

				// 双语化
				sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzGetSiteLang");
				String tzSiteLang = sqlQuery.queryForObject(sql, new Object[] { strApplyId }, "String");

				String onlineApplyText = gdObjectServiceImpl.getMessageTextWithLanguageCd(request,
						"TZGD_APPLICATION_MSG", "MSG01", tzSiteLang, "在线报名", "Online Application");
				String allSeatText = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPLICATION_MSG",
						"MSG02", tzSiteLang, "总席位数", "Seats number");
				String appliedNumText = gdObjectServiceImpl.getMessageTextWithLanguageCd(request,
						"TZGD_APPLICATION_MSG", "MSG03", tzSiteLang, "已报名人数", "Registration number");
				String waitingNumText = gdObjectServiceImpl.getMessageTextWithLanguageCd(request,
						"TZGD_APPLICATION_MSG", "MSG04", tzSiteLang, "等待区人数", "Waiting number");
				String cancelText = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPLICATION_MSG",
						"MSG05", tzSiteLang, "撤销报名", "Cancel Application");
				String timeOut = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPLICATION_MSG",
						"MSG06", tzSiteLang, "服务端请求超时。", "Server Request Timeout");
				String serverError = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPLICATION_MSG",
						"MSG07", tzSiteLang, "服务端请求发生错误。", "Server Request Error");

				// 只有启用在线报名并且在有效报名时间内才显示在线报名条
				if ("Y".equals(strQy_zxbm) && "Y".equals(validTD)) {

					sql = "select 'Y' REG_FLAG,TZ_HD_BMR_ID FROM PS_TZ_NAUDLIST_T where OPRID=? and TZ_ART_ID=? and TZ_NREG_STAT IN('1','4')";
					Map<String, Object> mapBM = sqlQuery.queryForMap(sql, new Object[] { oprid, strApplyId });

					// 是否已注册报名标识
					String regFlag = "";
					// 报名人ID
					String strBmrId = "";
					if (mapBM != null) {
						regFlag = mapBM.get("REG_FLAG") == null ? "" : String.valueOf(mapBM.get("REG_FLAG"));
						strBmrId = mapBM.get("TZ_HD_BMR_ID") == null ? "" : String.valueOf(mapBM.get("TZ_HD_BMR_ID"));
					}

					// 构造链接参数
					Map<String, Object> mapComParams = new HashMap<String, Object>();

					Map<String, Object> mapParams = new HashMap<String, Object>();

					String strBaseUrl = request.getContextPath() + "/dispatcher?tzParams=";
					// 报名按钮
					String btnHtml = "";
					System.out.println("regFlag:" + regFlag);
					if ("Y".equals(regFlag)) {

						mapComParams.put("APPLYID", strApplyId);
						mapComParams.put("BMRID", strBmrId);

						mapParams.put("ComID", "TZ_APPONL_COM");
						mapParams.put("PageID", "TZ_APPBAR_VIEW_STD");
						mapParams.put("OperateType", "EJSON");
						mapParams.put("comParams", mapComParams);

						String strUrlParams = jacksonUtil.Map2json(mapParams);

						String strUrl = strBaseUrl + URLEncoder.encode(strUrlParams, "UTF-8");

						btnHtml = tzGDObject.getHTMLText("HTML.TZEventsBundle.TZ_GD_EVENT_CANCEL_BTN", strUrl,
								cancelText);

					} else {

						mapComParams.put("APPLYID", strApplyId);

						mapParams.put("ComID", "TZ_APPONL_COM");
						mapParams.put("PageID", "TZ_APPREG_STD");
						mapParams.put("OperateType", "HTML");
						mapParams.put("comParams", mapComParams);

						String strUrlParams = jacksonUtil.Map2json(mapParams);

						String strUrl = strBaseUrl + URLEncoder.encode(strUrlParams, "UTF-8");

						btnHtml = tzGDObject.getHTMLText("HTML.TZEventsBundle.TZ_GD_EVENT_APPLY_BTN", strUrl,
								onlineApplyText);

					}
					System.out.println("btnHtml:" + btnHtml);
					System.out.println("tzXSMS:" + tzXSMS);
					String strHtml = "";
					switch (tzXSMS) {
					case "1":
						/* 不显示 */
						strHtml = btnHtml;
						break;

					case "2":
						/* 显示总席位数，已报名人数 */
						strHtml = btnHtml + tzGDObject.getHTMLText("HTML.TZEventsBundle.TZ_APPLY_ONLINE_DISPLAY",
								strActXW, String.valueOf(numYBM), String.valueOf(numWait), allSeatText, appliedNumText,
								waitingNumText);
						break;
					case "3":
						/* 显示总席位数，已报名人数，且可以查看已报名人的姓名 */
						if (numYBM > 0) {
							// 生成查看报名人的URL

							mapComParams.replace("APPLYID", strApplyId);

							mapParams.replace("ComID", "TZ_APPONL_COM");
							mapParams.replace("PageID", "TZ_APPINFO_STD");
							mapParams.replace("OperateType", "HTML");
							mapParams.replace("comParams", mapComParams);

							String strUrlParams = jacksonUtil.Map2json(mapParams);

							String strUrl = strBaseUrl + URLEncoder.encode(strUrlParams, "UTF-8");

							String strYBMurl = tzGDObject.getHTMLText("HTML.TZEventsBundle.TZ_APPLY_ONLINE_DISPLAY_A",
									strUrl, String.valueOf(numYBM));

							strHtml = btnHtml + tzGDObject.getHTMLText("HTML.TZEventsBundle.TZ_APPLY_ONLINE_DISPLAY",
									strActXW, strYBMurl, String.valueOf(numWait), allSeatText, appliedNumText,
									waitingNumText);

						} else {

							strHtml = btnHtml + tzGDObject.getHTMLText("HTML.TZEventsBundle.TZ_APPLY_ONLINE_DISPLAY",
									strActXW, String.valueOf(numYBM), String.valueOf(numWait), allSeatText,
									appliedNumText, waitingNumText);

						}
						break;
					}
					System.out.println("strHtml:" + strHtml);
					strRet = tzGDObject.getHTMLText("HTML.TZEventsBundle.TZ_APPLY_ONLINE_DISPLAY_HEAD", strHtml,
							timeOut, serverError, "", request.getContextPath());

				} else {
					// 没有启用在线报名 或 超过报名时间
					strRet = tzGDObject.getHTMLText("HTML.TZEventsBundle.TZ_APPLY_ONLINE_DISPLAY_HEAD", "", timeOut,
							serverError, "display:none", request.getContextPath());
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("strRet:" + strRet);
		return strRet;

	}

	public String tzOther(String oprType, String strParams, String[] errMsg) {
		Map<String, Object> mapRet = new HashMap<String, Object>();

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);
		String strApplyId = jacksonUtil.getString("APPLYID");
		try {
			// 显示报名条
			System.out.println("strApplyId:" + strApplyId);
			System.out.println("oprType:" + oprType);
			if ("eventBarShow".equals(oprType)) {
				if (null != strApplyId && !"".equals(strApplyId)) {
					Date dateNow = new Date();

					// 已报名人数
					String sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzGetEventAppliedNum");
					int numYBM = sqlQuery.queryForObject(sql, new Object[] { strApplyId }, "int");

					// 等待人数
					sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzGetEventWaitingNum");
					int numWait = sqlQuery.queryForObject(sql, new Object[] { strApplyId }, "int");

					// 获取活动显示模式
					sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzGetEventDisplayMode");
					Map<String, Object> mapData = sqlQuery.queryForMap(sql,
							new Object[] { strApplyId, dateNow, dateNow });

					// 是否有效记录
					String validTD = "";
					// 显示模式 CMS不需要 全部显示
					// String tzXSMS = "";
					// 是否启用在线报名
					String strQy_zxbm = "";
					// 活动席位数
					int numActXW = 0;
					// 若席位数为0，则代表不限制席位数，前台显示为“-”
					String strActXW = "-";

					if (mapData != null) {

						validTD = mapData.get("VALID_TD") == null ? "" : String.valueOf(mapData.get("VALID_TD"));
						// tzXSMS = mapData.get("TZ_XSMS") == null ? "" :
						// String.valueOf(mapData.get("TZ_XSMS"));
						strQy_zxbm = mapData.get("TZ_QY_ZXBM") == null ? "" : String.valueOf(mapData.get("TZ_QY_ZXBM"));
						numActXW = mapData.get("TZ_XWS") == null ? 0
								: Integer.parseInt(String.valueOf(mapData.get("TZ_XWS")));
						if (numActXW > 0) {
							strActXW = String.valueOf(numActXW);
						}
					}

					// 只有启用在线报名并且在有效报名时间内才显示在线报名条
					if ("Y".equals(strQy_zxbm) && "Y".equals(validTD)) {

						mapRet.put("ActXW", strActXW);
						mapRet.put("ActYBM", String.valueOf(numYBM));
						mapRet.put("ActWait", String.valueOf(numWait));
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("strRet:" + jacksonUtil.Map2json(mapRet));
		return jacksonUtil.Map2json(mapRet);
	}

	/**
	 * 撤销报名
	 * 
	 * @param strParams
	 * @return
	 */
	@Override
	@Transactional
	public String tzGetJsonData(String strParams) {
		String strRet = "";
		String errorCode = "";
		String errorMsg = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		String strAPPLYID = "";
		String strBMRID = "";
		String cancelSuccess = "";
		String cancelFailed = "";
		try {

			jacksonUtil.json2Map(strParams);
			strAPPLYID = jacksonUtil.getString("APPLYID");
			strBMRID = jacksonUtil.getString("BMRID");

			// 双语化
			String sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzGetSiteLang");
			String tzSiteLang = sqlQuery.queryForObject(sql, new Object[] { strAPPLYID }, "String");

			cancelSuccess = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPLICATION_MSG", "MSG08",
					tzSiteLang, "撤销报名成功", "Cancel Application Successful");
			cancelFailed = gdObjectServiceImpl.getMessageTextWithLanguageCd(request, "TZGD_APPLICATION_MSG", "MSG09",
					tzSiteLang, "撤销报名失败：", "Online Application Failed");

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {

			strAPPLYID = jacksonUtil.getString("APPLYID");
			strBMRID = jacksonUtil.getString("BMRID");

			// 报名状态
			String sql = "select TZ_NREG_STAT from PS_TZ_NAUDLIST_T where TZ_ART_ID=? and TZ_HD_BMR_ID=?";
			String strRegStatus = sqlQuery.queryForObject(sql, new Object[] { strAPPLYID, strBMRID }, "String");

			// 报名状态为1-已报名，4-等候
			if ("1".equals(strRegStatus) || "4".equals(strRegStatus)) {
				
				PsTzNaudlistT psTzNaudlistT = new PsTzNaudlistT();
				psTzNaudlistT.setTzArtId(strAPPLYID);
				psTzNaudlistT.setTzHdBmrId(strBMRID);
				psTzNaudlistT.setTzNregStat("3");
				int updNum = psTzNaudlistTMapper.updateByPrimaryKeySelective(psTzNaudlistT);
				if (updNum > 0) {

					String oprid = tzWebsiteLoginServiceImpl.getLoginedUserOprid(request);
					String orgId = tzWebsiteLoginServiceImpl.getLoginedUserOrgid(request);
					
					//撤销报名成功站内信
					try{
						sql = "SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=?";
						String name = sqlQuery.queryForObject(sql, new Object[]{ oprid }, "String");
						//报名成功成功站内信模板
						String znxModel = getHardCodePoint.getHardCodePointVal("TZ_HDBM_CX_ZNX_TMP");
						//创建邮件任务实例
						String taskId = createTaskServiceImpl.createTaskIns(orgId, znxModel, "ZNX", "A");
						// 创建邮件发送听众
						String crtAudi = createTaskServiceImpl.createAudience(taskId,orgId,"活动撤销报名成功站内信通知", "JSRW");
						//添加听众成员
						boolean bl = createTaskServiceImpl.addAudCy(crtAudi, name, "", "", "", "", "", "", oprid, "", strAPPLYID, "");
						if(bl){
							sendSmsOrMalServiceImpl.send(taskId, "");
						}
					}catch(NullPointerException nullEx){
						//没有配置邮件模板
						nullEx.printStackTrace();
					}
					
					// 报名最早的状态为“等待”的报名人
					sql = "select TZ_HD_BMR_ID,OPRID from PS_TZ_NAUDLIST_T where TZ_ART_ID=? and TZ_NREG_STAT='4' order by TZ_REG_TIME limit 0,1";
					String waitBmr = "";
					String waitOprid = "";
					Map<String,Object> bmrInfoMap = sqlQuery.queryForMap(sql, new Object[]{ strAPPLYID });
					if(bmrInfoMap != null){
						waitBmr = bmrInfoMap.get("TZ_HD_BMR_ID").toString();
						waitOprid = bmrInfoMap.get("OPRID").toString();
					}
					
					if (null != waitBmr && !"".equals(waitBmr) && "1".equals(strRegStatus)) {
						// 若撤销报名的人是已报名状态，则撤销成功后自动进补
						// 查询报名人数前就要锁表，不然同时报名的话，就可能超过允许报名的人数
						mySqlLockService.lockRow(sqlQuery, "TZ_NAUDLIST_T");

						// 活动席位数
						sql = "select TZ_XWS from PS_TZ_ART_HD_TBL where TZ_ART_ID=?";
						int numXW = sqlQuery.queryForObject(sql, new Object[] { strAPPLYID }, "int");

						// 已报名人数
						sql = "select count(1) from PS_TZ_NAUDLIST_T where TZ_ART_ID=? and TZ_NREG_STAT='1'";
						int numYbm = sqlQuery.queryForObject(sql, new Object[] { strAPPLYID }, "int");

						if (numYbm < numXW) {
							// 将等待的报名人设置为已报名
							psTzNaudlistT = new PsTzNaudlistT();
							psTzNaudlistT.setTzArtId(strAPPLYID);
							psTzNaudlistT.setTzHdBmrId(waitBmr);
							psTzNaudlistT.setTzNregStat("1");
							psTzNaudlistTMapper.updateByPrimaryKeySelective(psTzNaudlistT);
							
							if(waitOprid != null && !"".equals(waitOprid)){
								//活动报名成功站内信
								try{
									sql = "SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=?";
									String name = sqlQuery.queryForObject(sql, new Object[]{ waitOprid }, "String");
									//报名成功成功站内信模板
									String znxModel = getHardCodePoint.getHardCodePointVal("TZ_HDBM_CG_ZNX_TMP");
									//创建邮件任务实例
									String taskId = createTaskServiceImpl.createTaskIns(orgId, znxModel, "ZNX", "A");
									// 创建邮件发送听众
									String crtAudi = createTaskServiceImpl.createAudience(taskId,orgId,"活动报名成功站内信通知", "JSRW");
									//添加听众成员
									boolean bl = createTaskServiceImpl.addAudCy(crtAudi, name, "", "", "", "", "", "", waitOprid, "", strAPPLYID, "");
									if(bl){
										sendSmsOrMalServiceImpl.send(taskId, "");
									}
								}catch(NullPointerException nullEx){
									//没有配置邮件模板
									nullEx.printStackTrace();
								}
							}
						}

						// 解锁
						mySqlLockService.unlockRow(sqlQuery);
					}
					errorCode = "0";
					errorMsg = cancelSuccess;
				} else {
					errorCode = "1";
					errorMsg = cancelFailed;
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			errorCode = "1";
			errorMsg = cancelFailed + e.getMessage();
		}

		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("result", errorCode);
		mapRet.put("resultDesc", errorMsg);
		mapRet.put("artid", strAPPLYID);
		mapRet.put("bmrid", strBMRID);

		strRet = jacksonUtil.Map2json(mapRet);

		return strRet;
	}

}
