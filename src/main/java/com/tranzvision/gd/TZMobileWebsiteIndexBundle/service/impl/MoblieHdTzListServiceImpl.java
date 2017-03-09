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
 * 清华mba招生手机版招生资料专区
 * classId:  mEventNotice
 * @author tang
 *
 */
@Service("com.tranzvision.gd.TZMobileWebsiteIndexBundle.service.impl.MoblieHdTzListServiceImpl")
public class MoblieHdTzListServiceImpl extends FrameworkImpl{
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TZGDObject tzGDObject;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	//清华mba招生手机版招生资料专区
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
			menuId = "1";
		}
		
		
		//获取栏目名称;
		String columnNameSQL = "SELECT TZ_COLU_NAME FROM PS_TZ_SITEI_COLU_T WHERE TZ_SITEI_ID=? and TZ_COLU_ID=?";
		String columnName = sqlQuery.queryForObject(columnNameSQL, new Object[] { siteId,currentColumnId }, "String");
		String jsCss = "";
		
		
		//String artListSql;
		try {
			/*
			artListSql = tzGDObject.getSQLText("SQL.TZMobileWebsiteIndexBundle.TZ_HD_TZ_ART_LIST");
			String titleLi = "";
			System.out.println(siteId + "====>" + currentColumnId + "====>" + m_curOPRID);
			List<Map<String, Object>> artList = sqlQuery.queryForList(artListSql,new Object[] { siteId,currentColumnId,m_curOPRID,0,3});
			if (artList != null && artList.size()>0){
				for(int j=0;j<artList.size();j++){	
					String artId = (String) artList.get(j).get("TZ_ART_ID");
					String artTitle = (String) artList.get(j).get("TZ_ART_TITLE");
					String artTitleStyle = (String) artList.get(j).get("TZ_ART_TITLE_STYLE");
					String artDate = (String) artList.get(j).get("TZ_DATE1");
					//活动通知链接;
					String artUrl = (String) artList.get(j).get("TZ_ART_URL");
					if(artUrl == null){
						artUrl = "";
					}
					
					System.out.println("===================>"+artTitleStyle);
					String hotAndNewImg = "";
					int showImgNum = 0;
					if(artTitleStyle!=null&&!"".equals(artTitleStyle)){
						if(artTitleStyle.indexOf("HOT") > 0){
							hotAndNewImg = tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_HD_TZ_HOT_NEW_HTML", ctxPath + "/statics/css/website/m/images/hot.png");
							showImgNum ++;
						}
						if(artTitleStyle.indexOf("NEW") > 0){
							hotAndNewImg = tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_HD_TZ_HOT_NEW_HTML", ctxPath + "/statics/css/website/m/images/new.png");
							showImgNum ++;
						}
					}
					
					if(j==0){
						hotAndNewImg = tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_HD_TZ_HOT_NEW_HTML", ctxPath + "/statics/css/website/m/images/hot.png");
						showImgNum ++;
						hotAndNewImg = hotAndNewImg + tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_HD_TZ_HOT_NEW_HTML", ctxPath + "/statics/css/website/m/images/new.png");
						showImgNum ++;
					}
					
					if(j==1){
						hotAndNewImg = tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_HD_TZ_HOT_NEW_HTML", ctxPath + "/statics/css/website/m/images/new.png");
						showImgNum ++;
					}
					
					String width = "";
					if(showImgNum == 2){
						width = "57%";
						titleLi = titleLi + tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_HD_TZ_LI_HTML",artUrl,artTitle,hotAndNewImg,artDate,width );
					}else{
						if(showImgNum == 1){
							width = "65%";
							titleLi = titleLi + tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_HD_TZ_LI_HTML",artUrl,artTitle,hotAndNewImg,artDate,width );
						}else{
							width = "73%";
							titleLi = titleLi + tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_HD_TZ_LI_HTML", artUrl,artTitle,"",artDate,width);
						}
					}
				}
			}
			*/
			//css和js
			jsCss = tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_HD_TZ_LIST_JS_CSS",ctxPath,siteId,currentColumnId);
			listHtml = tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_HD_TZ_LIST_HTML", columnName,"");
			listHtml = tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_MOBILE_BASE_HTML",columnName,ctxPath,jsCss,siteId,menuId,listHtml);
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

					//链接;
					String artUrl = (String) artList.get(j).get("TZ_ART_URL");
					if(artUrl == null){
						artUrl = "";
					}
					
					String hotAndNewImg = "";
					int showImgNum = 0;
					if(artTitleStyle!=null&&!"".equals(artTitleStyle)){
						if(artTitleStyle.indexOf("HOT") > 0){
							hotAndNewImg = tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_HD_TZ_HOT_NEW_HTML", ctxPath + "/statics/css/website/m/images/hot.png");
							showImgNum ++;
						}
						if(artTitleStyle.indexOf("NEW") > 0){
							hotAndNewImg = tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_HD_TZ_HOT_NEW_HTML", ctxPath + "/statics/css/website/m/images/new.png");
							showImgNum ++;
						}
					}
					
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
