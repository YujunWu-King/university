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
 * 清华mba招生手机版系统站内信列表
 * @author 琚峰 
 * classid: znxList
 */
@Service("com.tranzvision.gd.TZMobileWebsiteIndexBundle.service.impl.MobileZnxListServiceImpl")
public class MobileZnxListServiceImpl extends FrameworkImpl {
	
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TZGDObject tzGDObject;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	//清华mba招生手机版系统消息列表
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
		String title = "站内信";
		try {
			//css和js
			String jsCss = tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_ZNX_TZ_LIST_JS_CSS",ctxPath,siteId);

			
			content = tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_MY_ACTIVITY_LIST",title,"");
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
		try {
			int limit = 10;
			int startNum = (pagenum - 1) * limit;
			
		    String znxSql = "SELECT A.TZ_ZNX_MSGID,A.TZ_ZNX_STATUS,DATE_FORMAT(B.ROW_ADDED_DTTM,'%Y/%m/%d')TZ_SEND_TIME,(SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID = B.TZ_ZNX_SENDID)TZ_ZNX_SENDNAME,B.TZ_MSG_SUBJECT,A.TZ_MSG_TEXT FROM PS_TZ_ZNX_REC_T A,PS_TZ_ZNX_MSG_T B WHERE A.TZ_ZNX_MSGID = B.TZ_ZNX_MSGID AND A.TZ_REC_DELSTATUS <> 'Y' AND A.TZ_ZNX_RECID = ? ORDER BY B.ROW_ADDED_DTTM DESC LIMIT ?,?";
			List<Map<String, Object>> list = sqlQuery.queryForList(znxSql,new Object[]{m_curOPRID,startNum,limit});
			if(list!=null && list.size()>0){
				for(int i=0; i < list.size(); i++){
					//消息id;
					String znxMsgId =String.valueOf(list.get(i).get("TZ_ZNX_MSGID")) ;
					//发送时间;
					String sendTime =String.valueOf(list.get(i).get("TZ_SEND_TIME")) ;
					//发件人姓名
					String sendName =String.valueOf(list.get(i).get("TZ_ZNX_SENDNAME")) ;
					//消息查看状态;
					String znxStatus =String.valueOf(list.get(i).get("TZ_ZNX_STATUS")) ;
					//消息主题
					String znxSubject =String.valueOf(list.get(i).get("TZ_MSG_SUBJECT")) ;
					//消息内容
					String msgText =String.valueOf(list.get(i).get("TZ_MSG_TEXT")) ;
					
					content = content + tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_MY_SYSINFO_DIV",true,znxMsgId,sendTime,sendName,znxStatus,znxSubject,msgText);
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
