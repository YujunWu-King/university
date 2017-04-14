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
 * 清华mba招生手机版系统站内信详情
 * @author 琚峰 
 * classid: znxContent
 */
@Service("com.tranzvision.gd.TZMobileWebsiteIndexBundle.service.impl.MobileZnxContentServiceImpl")
public class MobileZnxContentServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TZGDObject tzGDObject;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	//清华mba招生手机版系统消息内容
	@Override
	public String tzGetHtmlContent(String strParams) {
		//rootPath;
		String ctxPath = request.getContextPath();
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);
		String siteId = "";
		if(jacksonUtil.containsKey("siteId")){
			siteId = jacksonUtil.getString("siteId");
		}else{
			siteId = request.getParameter("siteId");
		}
		String znxMsgId = "";
		if(jacksonUtil.containsKey("znxMsgId")){
			znxMsgId = jacksonUtil.getString("znxMsgId");
		}else{
			znxMsgId = request.getParameter("znxMsgId");
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
		String title = "站内信详情";
	
		//当前登录人;
		String m_curOPRID = tzLoginServiceImpl.getLoginedManagerOprid(request);
		try {
			//css和js
			//String jsCss = tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_ZNX_TZ_LIST_JS_CSS",ctxPath,siteId);
			String jsCss = "";
			//跳转首页url
			//String indexUrl = ctxPath+"/dispatcher?classid=mIndex&siteId="+siteId;
			String znxListUrl = ctxPath+"/dispatcher?classid=znxList&siteId="+siteId;
			String znxSubject = "";
			String znxText = "";
			String znxTime = "";
		    String znxSql = "SELECT TZ_MSG_SUBJECT,TZ_MSG_TEXT,DATE_FORMAT(ROW_ADDED_DTTM,'%Y-%m-%d %h:%i')TZ_SEND_TIME FROM PS_TZ_ZNX_MSG_VW WHERE TZ_ZNX_MSGID =? AND TZ_ZNX_RECID=?";
		    Map<String, Object> msyyMap  = jdbcTemplate.queryForMap(znxSql,new Object[] { znxMsgId, m_curOPRID });
		    if(msyyMap != null){
		    	znxSubject = msyyMap.get("TZ_MSG_SUBJECT") == null ? "" : (String)msyyMap.get("TZ_MSG_SUBJECT");
		    	znxTime = msyyMap.get("TZ_SEND_TIME") == null ? "" : (String)msyyMap.get("TZ_SEND_TIME");
		    	znxText = msyyMap.get("TZ_MSG_TEXT") == null ? "" : (String)msyyMap.get("TZ_MSG_TEXT");
		    }
			content = tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_MY_ZNX_CONTENT",true,znxSubject,znxTime,znxText);
			content = tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_MY_ZNX_INFO",true,title,znxListUrl,content);
			content = tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_MOBILE_BASE_HTML",title,ctxPath,jsCss,siteId,menuId,content);
		} catch (TzSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return content;
	}

}
