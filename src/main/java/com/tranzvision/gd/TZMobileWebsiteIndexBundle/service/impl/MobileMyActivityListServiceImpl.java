package com.tranzvision.gd.TZMobileWebsiteIndexBundle.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.ValidateUtil;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 清华mba招生手机版我已报名的活动列表
 * @author tang
 * classid: myActivity
 */
@Service("com.tranzvision.gd.TZMobileWebsiteIndexBundle.service.impl.MobileMyActivityListServiceImpl")
public class MobileMyActivityListServiceImpl extends FrameworkImpl {
	
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TZGDObject tzGDObject;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private ValidateUtil validateUtil;
	
	//清华mba招生手机版我已报名的活动列表
	@Override
	public String tzGetHtmlContent(String strParams) {
		//rootPath;
		String ctxPath = request.getContextPath();
		
		//当前登录人;
		String m_curOPRID = tzLoginServiceImpl.getLoginedManagerOprid(request);
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);
		String siteId = "";
		if(jacksonUtil.containsKey("siteId")){
			siteId = jacksonUtil.getString("siteId");
		}else{
			siteId = request.getParameter("siteId");
		}
		
		String menuId = "";
		if(jacksonUtil.containsKey("menuId")){
			menuId = jacksonUtil.getString("menuId");
		}else{
			menuId = request.getParameter("menuId");
		}
		if(menuId == null || "".equals(menuId)){
			menuId = "1";
		}
		
		String content = "";
		String title = "已报名活动";
		try {
			//css和js
			String jsCss = tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_ACTIVITY_JS_CSS",ctxPath,siteId);

			//我已报名但未过期的活动数量
			/*
			int actCount = 0;
			String actSql = "select A.TZ_ART_ID,B.TZ_HD_BMR_ID,DATE_FORMAT(B.TZ_REG_TIME,'%Y/%m/%d')TZ_REG_TIME,DATE_FORMAT(A.TZ_START_DT,'%Y/%m/%d')TZ_START_DT,DATE_FORMAT(A.TZ_START_TM,'%H:%i') TZ_START_TM,DATE_FORMAT(A.TZ_END_DT,'%Y/%m/%d')TZ_END_DT,DATE_FORMAT(A.TZ_END_TM,'%H:%i') TZ_END_TM,A.TZ_NACT_NAME,A.TZ_NACT_ADDR from PS_TZ_ART_HD_TBL A,PS_TZ_NAUDLIST_T B  where A.TZ_ART_ID=B.TZ_ART_ID and B.TZ_NREG_STAT = '1' and A.TZ_START_DT IS NOT NULL AND A.TZ_START_TM IS NOT NULL AND A.TZ_END_DT IS NOT NULL AND A.TZ_END_TM IS NOT NULL  AND str_to_date(concat(DATE_FORMAT(A.TZ_START_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(A.TZ_START_TM,'%H:%i'),':00'),'%Y/%m/%d %H:%i:%s') <= now()  AND str_to_date(concat(DATE_FORMAT(A.TZ_END_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(A.TZ_END_TM,'%H:%i'),':59'),'%Y/%m/%d %H:%i:%s') >= now() AND B.OPRID=? order by A.TZ_START_DT,A.TZ_START_TM desc";
			List<Map<String, Object>> list = sqlQuery.queryForList(actSql,new Object[]{m_curOPRID});
			//TODO 下面的sql测试用,正式使用上面的sql;
			//String actSql = "select A.TZ_ART_ID,B.TZ_HD_BMR_ID,DATE_FORMAT(B.TZ_REG_TIME,'%Y/%m/%d')TZ_REG_TIME, DATE_FORMAT(A.TZ_START_DT,'%Y/%m/%d')TZ_START_DT,DATE_FORMAT(A.TZ_START_TM,'%H:%i') TZ_START_TM, DATE_FORMAT(A.TZ_END_DT,'%Y/%m/%d')TZ_END_DT,DATE_FORMAT(A.TZ_END_TM,'%H:%i') TZ_END_TM,A.TZ_NACT_NAME, A.TZ_NACT_ADDR from PS_TZ_ART_HD_TBL A,PS_TZ_NAUDLIST_T B  where A.TZ_ART_ID=B.TZ_ART_ID and B.TZ_NREG_STAT = '1' order by A.TZ_START_DT,A.TZ_START_TM desc";
			//List<Map<String, Object>> list = sqlQuery.queryForList(actSql);
			if(list!=null && list.size()>0){
				for(int i=0; i < list.size(); i++){
					//活动id;
					String artId =String.valueOf(list.get(i).get("TZ_ART_ID")) ;
					//报名id;
					String hdbmrId =String.valueOf(list.get(i).get("TZ_HD_BMR_ID")) ;
					//报名时间;
					String regTime =String.valueOf(list.get(i).get("TZ_REG_TIME")) ;
					//活动开始日期
					String startDate =String.valueOf(list.get(i).get("TZ_START_DT")) ;
					//活动开始时间
					String startTime =String.valueOf(list.get(i).get("TZ_START_TM")) ;
					//活动结束日期
					String endDate =String.valueOf(list.get(i).get("TZ_END_DT")) ;
					//活动结束时间
					String endTime =String.valueOf(list.get(i).get("TZ_END_TM")) ;
					//活动名称
					String hdName =String.valueOf(list.get(i).get("TZ_NACT_NAME")) ;
					//活动地点
					String hdAddr =String.valueOf(list.get(i).get("TZ_NACT_ADDR")) ;
					
					content = content + tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_MY_ACTIVITY_DIV",startDate,hdName,startTime + "-" + endTime,hdAddr,artId,hdbmrId);
				}
			}
			*/
			String indexUrl = ctxPath + "/dispatcher?classid=mIndex&siteId=" + siteId;
			content = tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_MY_ACTIVITY_LIST",title,"",indexUrl);
			content = tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_MOBILE_BASE_HTML",title,ctxPath,jsCss,siteId,menuId,content);
		} catch (TzSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return content;
	}
	
	@Override
	public String tzOther(String oprType, String strParams, String[] errorMsg) {
		
		Map<String, Object> returnMap = new HashMap<>();
		returnMap.put("resultNum", 0);
		returnMap.put("result", "");
		int resultNum = 0;
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		jacksonUtil.json2Map(strParams);
		String siteId = "";
		int pagenum = 0;
		if(jacksonUtil.containsKey("siteId")){
			siteId = jacksonUtil.getString("siteId");
			try {
				pagenum = jacksonUtil.getInt("pagenum");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				pagenum = 1;
			}
		}
		
		//rootPath;
		String ctxPath = request.getContextPath();
				
		//当前登录人;
		String m_curOPRID = tzLoginServiceImpl.getLoginedManagerOprid(request);
				
		String content = "";
		String title = "已报名活动";
		try {
			int limit = 10;
			int startNum = (pagenum - 1) * limit;
			
			//css和js
			String jsCss = tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_ACTIVITY_JS_CSS",ctxPath);

			//我已报名但未过期的活动数量
			int actCount = 0;
			String actSql = "select A.TZ_ART_ID,B.TZ_HD_BMR_ID,DATE_FORMAT(B.TZ_REG_TIME,'%Y/%m/%d')TZ_REG_TIME,DATE_FORMAT(A.TZ_START_DT,'%Y/%m/%d')TZ_START_DT,DATE_FORMAT(A.TZ_START_TM,'%H:%i') TZ_START_TM,DATE_FORMAT(A.TZ_END_DT,'%Y/%m/%d')TZ_END_DT,DATE_FORMAT(A.TZ_END_TM,'%H:%i') TZ_END_TM,A.TZ_NACT_NAME,A.TZ_NACT_ADDR from PS_TZ_ART_HD_TBL A,PS_TZ_NAUDLIST_T B  where A.TZ_ART_ID=B.TZ_ART_ID and B.TZ_NREG_STAT = '1' and A.TZ_START_DT IS NOT NULL AND A.TZ_START_TM IS NOT NULL AND A.TZ_END_DT IS NOT NULL AND A.TZ_END_TM IS NOT NULL  AND str_to_date(concat(DATE_FORMAT(A.TZ_START_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(A.TZ_START_TM,'%H:%i'),':00'),'%Y/%m/%d %H:%i:%s') <= now()  AND str_to_date(concat(DATE_FORMAT(A.TZ_END_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(A.TZ_END_TM,'%H:%i'),':59'),'%Y/%m/%d %H:%i:%s') >= now() AND B.OPRID=? order by A.TZ_START_DT,A.TZ_START_TM desc limit ?,?";
			List<Map<String, Object>> list = sqlQuery.queryForList(actSql,new Object[]{m_curOPRID,startNum,limit});
			if(list!=null && list.size()>0){
				for(int i=0; i < list.size(); i++){
					//活动id;
					String artId =String.valueOf(list.get(i).get("TZ_ART_ID")) ;
					//报名id;
					String hdbmrId =String.valueOf(list.get(i).get("TZ_HD_BMR_ID")) ;
					//报名时间;
					String regTime =String.valueOf(list.get(i).get("TZ_REG_TIME")) ;
					//活动开始日期
					String startDate =String.valueOf(list.get(i).get("TZ_START_DT")) ;
					//活动开始时间
					String startTime =String.valueOf(list.get(i).get("TZ_START_TM")) ;
					//活动结束日期
					String endDate =String.valueOf(list.get(i).get("TZ_END_DT")) ;
					//活动结束时间
					String endTime =String.valueOf(list.get(i).get("TZ_END_TM")) ;
					//活动名称
					String hdName =String.valueOf(list.get(i).get("TZ_NACT_NAME")) ;
					//活动地点
					String hdAddr =String.valueOf(list.get(i).get("TZ_NACT_ADDR")) ;
					
					content = content + tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_MY_ACTIVITY_DIV",startDate,hdName,startTime + "-" + endTime,hdAddr,artId,hdbmrId);
					resultNum = resultNum + 1;
				}
			}
		} catch (TzSystemException e) {
			// TODO Auto-generated catch block
			content = "";
			e.printStackTrace();
		}
		
		returnMap.replace("resultNum", resultNum);
		returnMap.replace("result", content);
		
		return jacksonUtil.Map2json(returnMap);
	}
}
