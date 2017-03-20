package com.tranzvision.gd.TZEventsSignInBundle.service.impl;

import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 活动管理列表
 * @author tranzvision
 *
 */
@Service("com.tranzvision.gd.TZEventsSignInBundle.service.impl.TzEventsListServiceImpl")
public class TzEventsListServiceImpl extends FrameworkImpl{

	@Autowired
	private TZGDObject tzGdObject;
	
	@Autowired
	private SqlQuery sqlQuery;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	
	@Override
	public String tzGetHtmlContent(String strParams) {
		String eventListHtml = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			//当前登录机构
			String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			
			Date currDate = new Date();
			Date preDate = this.addToDateForDays(currDate,-7);
			Date afterDate = this.addToDateForDays(currDate,7);
			
			String contextPath = request.getContextPath();
			// 通用链接;
			String ZSGL_URL = contextPath + "/dispatcher";
			
			List<Map<String,Object>> eventsList;
			String hd_li_html = "";
			
			//前一周活动
			String preSql = tzGdObject.getSQLText("SQL.TZEventsSignInBundle.TzHappenedEventsListSql");
			eventsList = sqlQuery.queryForList(preSql, new Object[]{ preDate, currDate, orgId });
			for(Map<String,Object> eventMap : eventsList){
				hd_li_html = hd_li_html + parseEventsListHtml(eventMap,ZSGL_URL);
			}

			//后一周活动
			String afterSql = tzGdObject.getSQLText("SQL.TZEventsSignInBundle.TzUnhappenEventsListSql");
			eventsList = sqlQuery.queryForList(afterSql, new Object[]{ currDate, afterDate, orgId });
			for(Map<String,Object> eventMap : eventsList){
				hd_li_html = hd_li_html + parseEventsListHtml(eventMap,ZSGL_URL);
			}
			
			//String str_main_url = ZSGL_URL + "?classid=eventsSign";
			String str_main_url = contextPath + "/signIn/"+orgId;
			
			//ajax请求url
			Map<String,Object> ajaxParamsMap = new HashMap<String,Object>();
			ajaxParamsMap.put("ComID", "TZ_HD_SIGN_COM");
			ajaxParamsMap.put("PageID", "TZ_HD_SIGN_STD");
			ajaxParamsMap.put("OperateType", "EJSON");
			ajaxParamsMap.put("comParams", new HashMap<String,String>());
			String str_ajax_url  =  ZSGL_URL + "?tzParams=" + URLEncoder.encode(jacksonUtil.Map2json(ajaxParamsMap),"UTF-8");
			
			eventListHtml = tzGdObject.getHTMLText("HTML.TZEventsSignInBundle.TZ_HDQD_HD_LIST_HTML",contextPath,hd_li_html,str_main_url,str_ajax_url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return eventListHtml;
	}
	
	
	/**
	 * 解析生成活动列表
	 * @param eventMap
	 * @param commUrl
	 * @return
	 */
	private String parseEventsListHtml(Map<String,Object> eventMap,String commUrl){
		String liHtml = "";
		try{
			int yqdCount,wqdCount;
			int bmCount; 
			String liLink = "";
			
			String actId = eventMap.get("TZ_ART_ID").toString();
			String actTitle = eventMap.get("TZ_NACT_NAME") == null ? "" : eventMap.get("TZ_NACT_NAME").toString();
			//String actDate = eventMap.get("TZ_START_DT") == null ? "" : eventMap.get("TZ_START_DT").toString();
			String month = eventMap.get("TZ_MONTH") == null ? "" : eventMap.get("TZ_MONTH").toString();
			String day = eventMap.get("TZ_DAY") == null ? "" : eventMap.get("TZ_DAY").toString();
			
			yqdCount = Integer.valueOf(eventMap.get("TZ_YQD_COUNT").toString());
			wqdCount = Integer.valueOf(eventMap.get("TZ_WQD_COUNT").toString());
			/*报名人数 = 已签到人数  + 未签到人数*/
			bmCount = yqdCount + wqdCount;
			//活动报名人URL
			liLink = commUrl + "?classid=eventsBmrList&tz_act_id="+actId;
			
			liHtml = tzGdObject.getHTMLText("HTML.TZEventsSignInBundle.TZ_HDQD_HD_LIST_LI_HTML",actTitle,month,day,String.valueOf(bmCount),String.valueOf(yqdCount),String.valueOf(wqdCount),liLink);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return liHtml;
	}
	
	
	public Date addToDateForDays(Date date, int days){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		calendar.add(Calendar.DATE, days);
		date = calendar.getTime();
		
		return date;
	}
}
