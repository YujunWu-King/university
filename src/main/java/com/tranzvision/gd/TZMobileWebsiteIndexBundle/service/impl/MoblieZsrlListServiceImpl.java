package com.tranzvision.gd.TZMobileWebsiteIndexBundle.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 清华mba招生手机版招生日历,招生活动,报考通知;
 * classId:  mZsrl
 * @author tang
 *
 */
@Service("com.tranzvision.gd.TZMobileWebsiteIndexBundle.service.impl.MoblieZsrlListServiceImpl")
public class MoblieZsrlListServiceImpl extends FrameworkImpl{
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TZGDObject tzGDObject;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	//清华mba招生手机版招生活动列表
	@Override
	public String tzGetHtmlContent(String strParams) {
		String listHtml = "";
		String m_curOPRID = tzLoginServiceImpl.getLoginedManagerOprid(request);
		
		//rootPath;
		String ctxPath = request.getContextPath();

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);
		String siteId = "",currentColumnId = "";
		if(jacksonUtil.containsKey("siteId")){
			siteId = jacksonUtil.getString("siteId");
			currentColumnId = jacksonUtil.getString("columnId");
		}else{
			siteId = request.getParameter("siteId");
			currentColumnId = request.getParameter("columnId");
		}
		
		String menuId = "";
		if(jacksonUtil.containsKey("menuId")){
			menuId = jacksonUtil.getString("menuId");
		}else{
			menuId = request.getParameter("menuId");
		}
		if(menuId == null || "".equals(menuId)){
			menuId = "2";
		}
		
		
		//获取栏目名称;
		String columnNameSQL = "SELECT TZ_COLU_NAME FROM PS_TZ_SITEI_COLU_T WHERE TZ_SITEI_ID=? and TZ_COLU_ID=?";
		String columnName = sqlQuery.queryForObject(columnNameSQL, new Object[] { siteId,currentColumnId }, "String");
		String jsCss = "";

		try {
			//css和js														
			jsCss = tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_ZSRL_LIST_JS_CSS",ctxPath,siteId,currentColumnId);
			
			//跳转首页url
			String indexUrl = ctxPath+"/dispatcher?classid=mIndex&siteId="+siteId;
			listHtml = tzGDObject.getHTMLTextForDollar("HTML.TZMobileWebsiteIndexBundle.TZ_M_ZSRL_LIST_HTML", columnName,"",indexUrl);
			listHtml = tzGDObject.getHTMLTextForDollar("HTML.TZMobileWebsiteIndexBundle.TZ_MOBILE_BASE_HTML",columnName,ctxPath,jsCss,siteId,menuId,listHtml);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			listHtml = "";
			e.printStackTrace();
		}
				
		
		return listHtml;
	}
	
	@Override
	public String tzOther(String oprType, String strParams, String[] errorMsg) {
		Map<String, Object> returnMap = new HashMap<>();
		returnMap.put("resultNum", 0);
		returnMap.put("result", "");
		int resultNum = 0;
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		jacksonUtil.json2Map(strParams);
		String siteId = "",currentColumnId = "";
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
			currentColumnId = jacksonUtil.getString("columnId");
		}
	
		String m_curOPRID = tzLoginServiceImpl.getLoginedManagerOprid(request);
		String ctxPath = request.getContextPath();
		String titleLi = "";
		try {
			int limit = 10;
			int startNum = (pagenum - 1) * limit;
			String artListSql = tzGDObject.getSQLText("SQL.TZMobileWebsiteIndexBundle.TZ_HD_TZ_ART_LIST");
			List<Map<String, Object>> artList = sqlQuery.queryForList(artListSql,new Object[] { siteId,currentColumnId,m_curOPRID,startNum,limit});
			if (artList != null && artList.size()>0){
				for(int j=0;j<artList.size();j++){	
					String artId = (String) artList.get(j).get("TZ_ART_ID");
					String artTitle = (String) artList.get(j).get("TZ_ART_TITLE");
					String artTitleStyle = (String) artList.get(j).get("TZ_ART_TITLE_STYLE");
					String artDate = (String) artList.get(j).get("TZ_DATE1");
					
					//查看是不是活动，如果是则取活动开始时间;
					String hdStartDate = sqlQuery.queryForObject("select DATE_FORMAT(TZ_START_DT,'%Y/%m/%d') TZ_START_DT from PS_TZ_ART_HD_TBL WHERE TZ_ART_ID=?",new Object[]{artId},"String");
					if(hdStartDate != null && !"".equals(hdStartDate)){
						artDate = hdStartDate;
					}
					//通知链接;
					String artUrl = (String) artList.get(j).get("TZ_ART_URL");
					if(artUrl == null){
						artUrl = "";
					}
					
					
					String hotAndNewImg = "";
					//int showImgNum = 0;
					if(artTitleStyle!=null&&!"".equals(artTitleStyle)){
						if(artTitleStyle.indexOf("HOT") > -1){
							hotAndNewImg = tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_HD_TZ_HOT_NEW_HTML", ctxPath + "/statics/css/website/m/images/hot.png");
							//showImgNum ++;
						}
						if(artTitleStyle.indexOf("NEW") > -1){
							hotAndNewImg = tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_HD_TZ_HOT_NEW_HTML", ctxPath + "/statics/css/website/m/images/new.png");
							//showImgNum ++;
						}
					}
					/*
					String width = "";
					if(showImgNum == 2){
						width = "57%";
						titleLi = titleLi + tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_HD_TZ_LI_HTML",artUrl,artTitle,hotAndNewImg,artDate,width );
					}else{
						if(showImgNum == 1){
							width = "65%";
							titleLi = titleLi + tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_HD_TZ_LI_HTML",artUrl, artTitle,hotAndNewImg,artDate,width );
						}else{
							width = "73%";
							titleLi = titleLi + tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_HD_TZ_LI_HTML", artUrl,artTitle,"",artDate,width);
						}
					}
					*/
					String month = "";
					String day = "";
					if(artDate != null && !"".equals(artDate)){
						String[] artDateList = artDate.split("/");
						month = artDateList[1];
						day = artDateList[2];
					}
					titleLi = titleLi + tzGDObject.getHTMLTextForDollar("HTML.TZMobileWebsiteIndexBundle.TZ_M_ZSRL_LI",day,month,artTitle,artUrl,hotAndNewImg);
					
					resultNum = resultNum +1;
				}
			}
		} catch (TzSystemException e) {
			// TODO Auto-generated catch block
			titleLi = "";
			e.printStackTrace();
		}
		
		returnMap.replace("resultNum", resultNum);
		returnMap.replace("result", titleLi);
		return jacksonUtil.Map2json(returnMap);
	}
}
