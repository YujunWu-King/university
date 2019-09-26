/**
 * 
 */
package com.tranzvision.gd.TZEventsBundle.service.impl;

import java.net.URLEncoder;
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

import com.tranzvision.gd.TZAuthBundle.service.impl.TzWebsiteLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdObjectServiceImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.CreateTaskServiceImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.SendSmsOrMalServiceImpl;
import com.tranzvision.gd.TZEventsBundle.dao.PsTzNaudlistTMapper;
import com.tranzvision.gd.TZEventsBundle.model.PsTzNaudlistT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzException;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
import com.tranzvision.gd.util.sql.type.TzRecord;

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
	private GetHardCodePoint getHardCodePoint;
	
	@Autowired
	private CreateTaskServiceImpl createTaskServiceImpl;
	
	@Autowired
	private SendSmsOrMalServiceImpl sendSmsOrMalServiceImpl;
	
	//用于控制访问量的信号变量，避免活动报名席位数过度竞争对服务器造成过大压力
	private static Semaphore registrationLockCounter = new Semaphore(10,true);
	

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
				String siteId = ""; //站点
				String chnlId = "";	//栏目
				if(jacksonUtil.containsKey("siteId")){
					siteId = jacksonUtil.getString("siteId");
				}
				if(jacksonUtil.containsKey("chnlId")){
					chnlId = jacksonUtil.getString("chnlId");
				}
				
				//活动发布对象，A-无限制，B-听众
				String sql = "select TZ_PROJECT_LIMIT from PS_TZ_ART_REC_TBL where TZ_ART_ID=?";
				String artLimitType = sqlQuery.queryForObject(sql, new Object[] { strApplyId }, "String");
				
				if(!"".equals(siteId) && !"".equals(chnlId)){
					// 根据siteId得到机构ID
					sql = "select TZ_JG_ID from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID=?";
					String jgid = sqlQuery.queryForObject(sql, new Object[] { siteId }, "String");

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

				
				// 已报名人数
				sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzGetEventAppliedNum");
				int numYBM = sqlQuery.queryForObject(sql, new Object[] { strApplyId }, "int");

				// 等待人数
				sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzGetEventWaitingNum");
				int numWait = sqlQuery.queryForObject(sql, new Object[] { strApplyId }, "int");

				// 获取活动显示模式
				sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzGetEventDisplayMode");
				Map<String, Object> mapData = sqlQuery.queryForMap(sql, new Object[] { dateNow, dateNow, dateNow, dateNow, strApplyId });

				// 是否有效记录,Y-在报名时间内，B-报名为开始，E-报名已结束
				String validTD = "";
				// 显示模式
				String tzXSMS = "";
				// 是否启用在线报名
				String strQy_zxbm = "";
				// 活动席位数
				int numActXW = 0;
				// 若席位数为0，则代表不限制席位数，前台显示为“-”
				String strActXW = "-";
				//活动是否未开始
				String actNoStart = "";
				if (mapData != null) {

					validTD = mapData.get("VALID_TD") == null ? "" : String.valueOf(mapData.get("VALID_TD"));
					tzXSMS = mapData.get("TZ_XSMS") == null ? "1" : String.valueOf(mapData.get("TZ_XSMS"));
					strQy_zxbm = mapData.get("TZ_QY_ZXBM") == null ? "" : String.valueOf(mapData.get("TZ_QY_ZXBM"));
					numActXW = mapData.get("TZ_XWS") == null ? 0
							: Integer.parseInt(String.valueOf(mapData.get("TZ_XWS")));
					if (numActXW > 0) {
						strActXW = String.valueOf(numActXW);
					}
				
					//活动是否未开始
					actNoStart = mapData.get("IS_NOT_START") == null ? "" : String.valueOf(mapData.get("IS_NOT_START"));
				}

				// 双语化
				sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzGetSiteLang");
				String tzSiteLang = sqlQuery.queryForObject(sql, new Object[] { strApplyId }, "String");

				String onlineApplyText = gdObjectServiceImpl.getMessageTextWithLanguageCd(request,
						"TZGD_APPLICATION_MSG", "MSG01", tzSiteLang, "立即报名", "Online Application");
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
				
				
				// 只有启用在线报名并且在有效报名时间内才显示在线报名条
				if ("Y".equals(strQy_zxbm) && isInAud) {
					//报名时间限制，只有在报名时间内才可报名，否则按钮不可点击
					 boolean clickEnable;
					 String tipMsg = "";
					 String btnDisabledClass = "";/*按钮不可点class*/
					 if("Y".equals(validTD)){
						 clickEnable = true;
					 }else if("B".equals(validTD)){
						 clickEnable = false;
						 tipMsg = "报名未开始";
						 btnDisabledClass = "click-disabled";
					 }else if("E".equals(validTD)){
						 clickEnable = false;
						 tipMsg = "报名已结束";
						 btnDisabledClass = "click-disabled";
					 }else{
						 clickEnable = false;
						 btnDisabledClass = "click-disabled";
					 }
					 
					Map<String, Object> mapBM = null;
					if(oprid != null && !"".equals(oprid)){
						sql = "select 'Y' REG_FLAG,TZ_HD_BMR_ID,TZ_NREG_STAT FROM PS_TZ_NAUDLIST_T where OPRID=? and TZ_ART_ID=? and TZ_NREG_STAT IN('1','4')";
						mapBM = sqlQuery.queryForMap(sql, new Object[] { oprid, strApplyId });
					}

					// 是否已注册报名标识
					String regFlag = "";
					// 报名人ID
					String strBmrId = "";
					//报名状态
					String applySta = "";
					if (mapBM != null) {
						regFlag = mapBM.get("REG_FLAG") == null ? "" : String.valueOf(mapBM.get("REG_FLAG"));
						strBmrId = mapBM.get("TZ_HD_BMR_ID") == null ? "" : String.valueOf(mapBM.get("TZ_HD_BMR_ID"));
						applySta = mapBM.get("TZ_NREG_STAT") == null ? "" : String.valueOf(mapBM.get("TZ_NREG_STAT"));
					}

					// 构造链接参数
					Map<String, Object> mapComParams = new HashMap<String, Object>();

					Map<String, Object> mapParams = new HashMap<String, Object>();

					String strBaseUrl = request.getContextPath() + "/dispatcher?tzParams=";
					// 报名按钮
					String btnHtml = "";
					//报名状态显示
					String statusHtml = "";
					if ("Y".equals(regFlag)) {

						mapComParams.put("APPLYID", strApplyId);
						mapComParams.put("BMRID", strBmrId);

						mapParams.put("ComID", "TZ_APPONL_COM");
						mapParams.put("PageID", "TZ_APPBAR_VIEW_STD");
						mapParams.put("OperateType", "EJSON");
						mapParams.put("comParams", mapComParams);

						String strUrlParams = jacksonUtil.Map2json(mapParams);

						String strUrl = strBaseUrl + URLEncoder.encode(strUrlParams, "UTF-8");

						if("Y".equals(actNoStart)){
							//活动未开始，可以撤销
							btnDisabledClass = ""; /*撤销按钮可点击*/
						}else{
							//按钮显示修改
							if(!clickEnable && !"".equals(tipMsg)){
								cancelText = tipMsg;
							}
						}
						
						//显示报名状态
						String statusText = "";
						switch(applySta){
						case "1":
							statusText = "已报名";
							break;
						case "4":
							//等候席位数
							sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzGetWaitingNumber");
							int waitNum = sqlQuery.queryForObject(sql, new Object[]{ strApplyId, strBmrId }, "int");
							statusText = "等候席第"+ waitNum +"位";
							break;
						}
						if(!"".equals(statusText)){
							statusHtml = tzGDObject.getHTMLText("HTML.TZEventsBundle.TZ_APPLY_ONLINE_DISPLAY_STATUES","状态",statusText);
						}
						
						
						btnHtml = tzGDObject.getHTMLText("HTML.TZEventsBundle.TZ_GD_EVENT_CANCEL_BTN", strUrl,
								cancelText, btnDisabledClass);

					} else {

						mapComParams.put("APPLYID", strApplyId);
						mapComParams.put("siteId", siteId);
						mapComParams.put("chnlId", chnlId);

						mapParams.put("ComID", "TZ_APPONL_COM");
						mapParams.put("PageID", "TZ_APPREG_STD");
						mapParams.put("OperateType", "HTML");
						mapParams.put("comParams", mapComParams);

						String strUrlParams = jacksonUtil.Map2json(mapParams);

						String strUrl = strBaseUrl + URLEncoder.encode(strUrlParams, "UTF-8");

						//按钮显示修改
						if(!clickEnable && !"".equals(tipMsg)){
							onlineApplyText = tipMsg;
						}
						
						btnHtml = tzGDObject.getHTMLText("HTML.TZEventsBundle.TZ_GD_EVENT_APPLY_BTN", strUrl,
								onlineApplyText, btnDisabledClass);

					}

					String strHtml = "";
					switch (tzXSMS) {
					case "1":
						/* 不显示 */
						strHtml = btnHtml + statusHtml;
						break;

					case "2":
						/* 显示总席位数，已报名人数 */
						strHtml = btnHtml + tzGDObject.getHTMLText("HTML.TZEventsBundle.TZ_APPLY_ONLINE_DISPLAY",
								strActXW, String.valueOf(numYBM), String.valueOf(numWait), allSeatText, appliedNumText,
								waitingNumText,statusHtml);
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
									waitingNumText,statusHtml);

						} else {

							strHtml = btnHtml + tzGDObject.getHTMLText("HTML.TZEventsBundle.TZ_APPLY_ONLINE_DISPLAY",
									strActXW, String.valueOf(numYBM), String.valueOf(numWait), allSeatText,
									appliedNumText, waitingNumText,statusHtml);

						}
						break;
					}
					//System.out.println("strHtml:" + strHtml);
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
					errorCode = "0";
					errorMsg = cancelSuccess;
					
					String oprid = tzWebsiteLoginServiceImpl.getLoginedUserOprid(request);
					String orgId = tzWebsiteLoginServiceImpl.getLoginedUserOrgid(request);

					//如果撤销的报名人为已报名，将报名最早的等候报名人设置为已报名
					try{
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
							boolean sendBmZnx = false;
							// 若撤销报名的人是已报名状态，则撤销成功后自动进补
							// 查询报名人数前就要锁表，不然同时报名的话，就可能超过允许报名的人数
							//同一个应用服务内只允许10个考生同时进入面试预约排队，否则报系统忙，请稍候再试。
							if(registrationLockCounter.getQueueLength() >= 10 || registrationLockCounter.tryAcquire(500,TimeUnit.MILLISECONDS) == false)
							{
								throw new Exception("系统忙，请稍候再试。");
							}
							
							Semaphore tmpSemaphore = null;
							boolean isLocked = false;
							boolean hasTmpSemaphore = false;
							try{
								//获取当前面试时间段对应的信号灯
								Map.Entry<String,Semaphore> tmpSemaphoreObject = tzGDObject.getSemaphore("com.tranzvision.gd.TZEventsBundle.service.impl.TzEventApplyFormServiceImpl-20170717", strAPPLYID);
								
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
								
								//利用主键冲突异常来控制同一时刻只能有一个人来预约某个时间段
								try
								{
									TzRecord lockRecord = tzGDObject.createRecord("PS_TZ_HDBM_LOCK_TBL");
									lockRecord.setColumnValue("TZ_HD_ID", strAPPLYID);
									lockRecord.setColumnValue("OPRID", waitOprid);
									
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
										sendBmZnx = true;
									}
								}
							}catch(Exception e){
								throw e;
							}finally {
								if(isLocked){
									//报名完成后删除插入PS_TZ_HDBM_LOCK_TBL中的数据
									sqlQuery.update("delete from PS_TZ_HDBM_LOCK_TBL where TZ_HD_ID=?", new Object[]{ strAPPLYID });
								}
								
								if(hasTmpSemaphore){
									tmpSemaphore.release();
								}
								
								registrationLockCounter.release();
							}

							if(sendBmZnx){
								//活动报名成功站内信
								try{
									sql = "SELECT TZ_REALNAME FROM PS_TZ_REG_USER_T WHERE OPRID=?";
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
					}catch(Exception e){
						e.printStackTrace();
					}
					
					//撤销报名成功站内信
					try{
						sql = "SELECT TZ_REALNAME FROM PS_TZ_REG_USER_T WHERE OPRID=?";
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
				} else {
					errorCode = "1";
					errorMsg = cancelFailed;
				}
			}else{
				errorCode = "1";
				errorMsg = "您已撤销报名";
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
