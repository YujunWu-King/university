package com.tranzvision.gd.TZEventsBundle.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.HTML;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tranzvision.gd.TZEventsBundle.service.impl.TzEventsGetEticketCodeServiceImpl;
import com.tranzvision.gd.TZEventsBundle.service.impl.TzEventshortCancelServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.encrypt.DESUtil;
import com.tranzvision.gd.util.sql.TZGDObject;



@Controller
@RequestMapping(value = { "/" })
public class TzEventCancelTickeController {

	@Autowired
	private TzEventshortCancelServiceImpl tzEventshortCancelServiceImpl;
	@Autowired
	private TZGDObject tZGDObject;
	
	
	@RequestMapping(value = "cancel", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String cancel(HttpServletRequest request, HttpServletResponse response){
		    String strRet="";
		try {
			String strparameter=request.getParameter("HD");
	        String [] strArray=strparameter.split("_");
	        String actId="";
	        String actQdId="";
	       
	        if (strArray.length>0) {
	        	actId=strArray[0];
	        	actQdId=strArray[1];				
			}
	        actQdId=DESUtil.decrypt(actQdId, "HD_TRANZVISION");
			
	        strRet=tzEventshortCancelServiceImpl.eventCancel(actId, actQdId);
	        JacksonUtil jacksonUtil=new JacksonUtil();
	        jacksonUtil.json2Map(strRet);
	        String result=jacksonUtil.getString("result");
	        String resultDesc=jacksonUtil.getString("resultDesc");
            if (result.equals("1")||result.equals("0")) {
            	strRet=tZGDObject.getHTMLText("HTML.TZEventsBundle.TZ_HDBM_MSG_HTML", resultDesc);
            }else{
            	strRet=tZGDObject.getHTMLText("HTML.TZEventsBundle.TZ_HDBM_MSG_HTML", "您已取消报名");
            }
			
		} catch (Exception e) {
			e.printStackTrace();
			strRet=e.toString();
		}
		return strRet;
			
		
	}
	
	
	@RequestMapping(value = "ticket", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String dispatcher(HttpServletRequest request, HttpServletResponse response){
		    String strRet="";
		try {
			String strparameter=request.getParameter("HD");
	        String [] strArray=strparameter.split("_");
	        String actId="";
	        String actQdId="";
	       
	        if (strArray.length>0) {
	        	actId=strArray[0];
	        	actQdId=strArray[1];				
			}
	        actQdId=DESUtil.decrypt(actQdId, "HD_TRANZVISION");
	        TzEventsGetEticketCodeServiceImpl obj=new TzEventsGetEticketCodeServiceImpl();
	        strRet=obj.getHdTicketCode(actId, actQdId);	
	        
			
		} catch (Exception e) {
			e.printStackTrace();
			strRet=e.toString();
		}
		return strRet;
			
		
	}
	
	
	
	
}



