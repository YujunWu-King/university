package com.tranzvision.gd.TZEventsMobileBundle.service.impl;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzWebsiteLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

import freemarker.core.ParseException;

/**
 * 手机版活动详情页面
 * @author zhanglang
 * 2017/03/01
 */
@Service("com.tranzvision.gd.TZEventsMobileBundle.service.impl.TzEventsDetailsServiceImpl")
public class TzEventsDetailsServiceImpl extends FrameworkImpl{
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private TZGDObject tzGDObject;

	@Autowired
	private SqlQuery sqlQuery;
	
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private TzWebsiteLoginServiceImpl tzWebsiteLoginServiceImpl;
	
	
	@Override
	public String tzGetHtmlContent(String strParams) {
		String eventsDetailsHtml = "";
		try {
			/*
			String contextPath = request.getContextPath();
			// 通用链接;
			String ZSGL_URL = contextPath + "/dispatcher";
			//活动ID
			String actID = request.getParameter("actId");
			
			eventsDetailsHtml = tzGDObject.getHTMLText("HTML.TZEventsMobileBundle.TZ_M_EVENTS_DETAILS_HTML",contextPath,ZSGL_URL);
			*/

			String siteId = request.getParameter("siteId");
			String columnId = request.getParameter("columnId");
			String artId = request.getParameter("artId");

			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

			// 校验 用户是否已经登录，如果未登录 则 跳到登录页面，用户登录完成以后在跳转回来
			if (siteId != null && !"".equals(siteId) && columnId != null && !"".equals(columnId) && artId != null
					&& !"".equals(artId)) {

				// 检查是否有听众控制
				String AudError = "N";
				String sqlAud = "select C.TZ_PROJECT_LIMIT,'Y' AUDFLG from PS_TZ_ART_AUDIENCE_T A,PS_TZ_AUD_LIST_T B,PS_TZ_ART_REC_TBL C WHERE B.TZ_DXZT='A' AND A.TZ_AUD_ID=B.TZ_AUD_ID AND A.TZ_ART_ID=C.TZ_ART_ID AND B.OPRID=? AND A.TZ_ART_ID=? LIMIT 1";
				Map<String, Object> mapAud = sqlQuery.queryForMap(sqlAud, new Object[] { oprid, artId });
				if (mapAud != null) {

					// 发布对象
					String pubFlg = (String) mapAud.get("TZ_PROJECT_LIMIT");
					// 是否当前人存在于该听众
					String audFlg = (String) mapAud.get("AUDFLG");

					if (pubFlg == "B" && audFlg != "Y") {
						// 如果设置发布对象为听众，不在听众中，不能访问
						eventsDetailsHtml = " 您无权限查看";
						AudError = "Y";
					} else {
						AudError = "N";
					}

				} else {
					AudError = "N";
				}

				if (AudError == "N") {
					// 查看是否是外部链接;
					String sql = "SELECT TZ_ART_TYPE1,TZ_OUT_ART_URL FROM PS_TZ_ART_REC_TBL WHERE TZ_ART_ID = ?";
					Map<String, Object> map = sqlQuery.queryForMap(sql, new Object[] { artId });
					if (map != null) {
						String artType = (String) map.get("TZ_ART_TYPE1");
						if ("B".equals(artType)) {
							String outurl = (String) map.get("TZ_OUT_ART_URL");
							if (outurl == null || "".equals(outurl)) {
								eventsDetailsHtml = "未定义外部链接";
							} else {
								eventsDetailsHtml = "<script type=\"text/javascript\">;location.href=\"" + outurl + "\"</script>";
							}
						} else {
							String htmlSQL = "select TZ_ART_SJ_CONT_SCR from PS_TZ_LM_NR_GL_T where TZ_SITE_ID=? and TZ_COLU_ID=? and TZ_ART_ID=? and TZ_ART_NEWS_DT <= now()";
							Map<String, Object> contentMap = sqlQuery.queryForMap(htmlSQL,new Object[] { siteId, columnId, artId });
							if (contentMap == null) {
								eventsDetailsHtml = "当前时间不可查看该内容";
							} else {
								eventsDetailsHtml = (String) contentMap.get("TZ_ART_SJ_CONT_SCR");
							}
						}

					} else {
						eventsDetailsHtml = "参数错误，请联系系统管理员";
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return "无法获取相关数据";
		}
		return eventsDetailsHtml;
	}
	

	@Override
	public String tzOther(String strType, String strParams, String[] errorMsg) {
		String strRet = "";
		try {
			switch (strType) {
				case "getActInfoData":
					//保存批次面试预约安排 
					strRet = this.getActInfoData(strParams,errorMsg);
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。" + e.getMessage();
		}
		return strRet;
	}
	
	
	/**
	 * 获取活动信息
	 * @param strParams
	 * @param errorMsg
	 * @return
	 */
	private String getActInfoData(String strParams, String[] errorMsg) throws ParseException{
		String strRet = "";
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("diaplayAppBar", "N");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			Date dateNow = new Date();
			jacksonUtil.json2Map(strParams);
			//活动编号
			String actId = jacksonUtil.getString("actId");
			
			String oprid = tzWebsiteLoginServiceImpl.getLoginedUserOprid(request);
			
			//活动听众判断
			boolean isInAud = false;
			String audSql = "select TZ_AUD_ID from PS_TZ_ART_AUDIENCE_T where TZ_ART_ID=? and exists(select 'X' from PS_TZ_ART_REC_TBL where TZ_ART_ID=PS_TZ_ART_AUDIENCE_T.TZ_ART_ID and TZ_PROJECT_LIMIT='B')";
			List<Map<String,Object>> audList = sqlQuery.queryForList(audSql, new Object[]{ actId });
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
			
			
			// 获取活动显示模式
			String sql = tzGDObject.getSQLText("SQL.TZEventsBundle.TzGetEventDisplayMode");
			Map<String, Object> mapData = sqlQuery.queryForMap(sql, new Object[] { dateNow, dateNow, dateNow, dateNow, actId });
			
			// 是否有效记录
			String validTD = "";
			// 是否启用在线报名
			String strQy_zxbm = "";
			//活动是否未开始
			String actNoStart = "";
			if (mapData != null) {
				validTD = mapData.get("VALID_TD") == null ? "" : String.valueOf(mapData.get("VALID_TD"));
				strQy_zxbm = mapData.get("TZ_QY_ZXBM") == null ? "" : String.valueOf(mapData.get("TZ_QY_ZXBM"));
				
				//活动是否未开始
				actNoStart = mapData.get("IS_NOT_START") == null ? "" : String.valueOf(mapData.get("IS_NOT_START"));
			}
			
			// 只有启用在线报名才显示在线报名条,如果设置听众当前人在听众内显示报名条
			if ("Y".equals(strQy_zxbm) && isInAud) {
				rtnMap.replace("diaplayAppBar","Y");
				
				sql = "select 'Y' REG_FLAG,TZ_HD_BMR_ID,TZ_NREG_STAT FROM PS_TZ_NAUDLIST_T where OPRID=? and TZ_ART_ID=? and TZ_NREG_STAT IN('1','4')";
				Map<String, Object> mapBM = sqlQuery.queryForMap(sql, new Object[] { oprid, actId });

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
				
				//已报名活动，活动尚未开始，可以撤销报名
				if("Y".equals(regFlag) && "Y".equals(actNoStart)){
					validTD = "Y";
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
					int waitNum = sqlQuery.queryForObject(sql, new Object[]{ actId, strBmrId }, "int");
					statusText = "等候席第"+ waitNum +"位";
					break;
				}
				
				rtnMap.put("statusText", statusText);

				rtnMap.put("regFlag", regFlag);
				rtnMap.put("bmrId", strBmrId);
				rtnMap.put("valid_dt", validTD);
				
				
				String strBaseUrl = request.getServletContext().getContextPath() + "/dispatcher?tzParams=";
				//在线报名URL
				String strAppFormUrl = request.getContextPath() + "/dispatcher?classid=eventsAppForm&actId="+actId;
				
				// 构造链接参数
				Map<String, Object> mapComParams = new HashMap<String, Object>();
				Map<String, Object> mapParams = new HashMap<String, Object>();
				
				mapComParams.put("APPLYID", actId);
				mapComParams.put("BMRID", strBmrId);

				mapParams.put("ComID", "TZ_APPONL_COM");
				mapParams.put("PageID", "TZ_APPBAR_VIEW_STD");
				mapParams.put("OperateType", "EJSON");
				mapParams.put("comParams", mapComParams);

				String strUrlParams = jacksonUtil.Map2json(mapParams);
				//撤销报名URL
				String cancelApplyUrl = strBaseUrl + URLEncoder.encode(strUrlParams, "UTF-8");
				
				rtnMap.put("appFormUrl", strAppFormUrl);
				rtnMap.put("cancelApplyUrl", cancelApplyUrl);
			}
			
			String actAddr = "";
			String startTime = "";
			String endTime = "";
			SimpleDateFormat simpleDatetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			sql = "select TZ_NACT_ADDR,concat(TZ_START_DT,' ',TZ_START_TM) as TZ_START_DT,concat(TZ_END_DT,' ',TZ_END_TM) as TZ_END_DT from PS_TZ_ART_HD_TBL where TZ_ART_ID=?";
			Map<String, Object> actMap = sqlQuery.queryForMap(sql, new Object[] { actId });
			if(actMap != null){
				actAddr = actMap.get("TZ_NACT_ADDR") == null ? "" : actMap.get("TZ_NACT_ADDR").toString();
				startTime = actMap.get("TZ_START_DT") == null ? "" : actMap.get("TZ_START_DT").toString();
				endTime = actMap.get("TZ_END_DT") == null ? "" : actMap.get("TZ_END_DT").toString();
				
				SimpleDateFormat simpleDttmFormat = new SimpleDateFormat("MM/dd HH:mm");
				if(!"".equals(startTime)){
					startTime = simpleDttmFormat.format(simpleDatetimeFormat.parse(startTime)); 
				}
				if(!"".equals(endTime)){
					endTime = simpleDttmFormat.format(simpleDatetimeFormat.parse(endTime)); 
				}
			}
			
			if("".equals(actAddr) && "".equals(startTime) && "".equals(endTime)){
				//如果不是活动，地点取内容表中的TZ_LONG1字段，时间就取TZ_DATE1
				sql = "select TZ_LONG1,date_format(TZ_DATE1,'%Y/%m/%d') as TZ_DATE1 from PS_TZ_ART_REC_TBL where TZ_ART_ID=?";
				actMap = sqlQuery.queryForMap(sql, new Object[] { actId });
				if(actMap != null){
					actAddr = actMap.get("TZ_LONG1") == null ? "" : actMap.get("TZ_LONG1").toString();
					startTime = actMap.get("TZ_DATE1") == null ? "" : actMap.get("TZ_DATE1").toString();
				}
			}
			
			if(!"".equals(endTime)){
				startTime = startTime+ " - " +endTime;
			}
			
			rtnMap.put("location", actAddr);
			rtnMap.put("dateTime", startTime);

		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。"+e.getMessage();
		}
		strRet = jacksonUtil.Map2json(rtnMap);
		return strRet;
	}
	
}
