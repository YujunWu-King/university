package com.tranzvision.gd.TZEmailSmsQFBundle.service.impl;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZEmailSmsQFBundle.dao.PsTzYjqfdjjlTMapper;
import com.tranzvision.gd.TZEmailSmsQFBundle.dao.PsTzYjqfdkyjjlTMapper;
import com.tranzvision.gd.TZEmailSmsQFBundle.dao.PsTzYjqftdTblMapper;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzYjqfdjjlT;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzYjqfdjjlTKey;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzYjqfdkyjjlT;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzYjqfdkyjjlTKey;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzYjqftdTbl;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzYjqftdTblKey;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/*
 * PS:  TZ_GK_EDM_PKG:TZ_GK_TD_CLS
 * 退订
 */

@Service("com.tranzvision.gd.TZEmailSmsQFBundle.service.impl.TzGkTdClsServiceImpl")
public class TzGkTdClsServiceImpl extends FrameworkImpl {
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private SqlQuery jdbcTemplate;
	
	@Autowired
	private PsTzYjqfdkyjjlTMapper psTzYjqfdkyjjlTMapper;
	
	@Autowired
	private PsTzYjqfdjjlTMapper psTzYjqfdjjlTMapper;
	
	@Autowired
	private PsTzYjqftdTblMapper psTzYjqftdTblMapper;
	
	@Autowired
	private TZGDObject tzGDObject;
	
	//点击链接及退订处理程序
	@Override
	public String tzGetHtmlContent(String strParams) {
		String strReturn = "";
		
		String params = request.getParameter("params");
		if(params != null && !"".equals(params)){
			String[] tdParams = params.split("_");
			String strLinkId = tdParams [0];
			String strAudRyId = tdParams [1];
			
			Map<String, Object> map = jdbcTemplate.queryForMap("select TZ_LINK_TYPE,TZ_MLSM_QFPC_ID,TZ_REDT_URL,TZ_ADD_DTTM,TZ_EML_SMS_TASK_ID from PS_TZ_LINKPAR_TBL where TZ_LINKPRA_ID=?",new Object[]{strLinkId});
			String strLinkType = String.valueOf(map.get("TZ_LINK_TYPE"));
			String strDxYjId= String.valueOf(map.get("TZ_MLSM_QFPC_ID"));
			String strLinkSite= String.valueOf(map.get("TZ_REDT_URL"));
			//Date strAddTime= (Date) map.get("TZ_ADD_DTTM");
			String strTaskId = String.valueOf(map.get("TZ_EML_SMS_TASK_ID"));
			
			/*获得听众ID*/
			String strAudId = jdbcTemplate.queryForObject("select TZ_AUDIENCE_ID from PS_TZ_DXYJFSRW_TBL where TZ_EML_SMS_TASK_ID=?", new Object[]{strTaskId},"String");
			String strEmail = jdbcTemplate.queryForObject("select TZ_ZY_EMAIL from PS_TZ_AUDCYUAN_T where TZ_AUDIENCE_ID=? and TZ_AUDCY_ID=?", new Object[]{strAudId, strAudRyId},"String");
			  
			String tzIpAddr =  this.getIpAddress(request);
			/*邮件群发打开邮件记录表*/
			if("OPEN".equals(strLinkType)){
				PsTzYjqfdkyjjlTKey psTzYjqfdkyjjlTKey = new PsTzYjqfdkyjjlTKey();
				psTzYjqfdkyjjlTKey.setTzMlsmQfpcId(strDxYjId);
				psTzYjqfdkyjjlTKey.setTzEmail(strEmail);
				psTzYjqfdkyjjlTKey.setTzOpenDttm(new Date());
				PsTzYjqfdkyjjlT psTzYjqfdkyjjlT = psTzYjqfdkyjjlTMapper.selectByPrimaryKey(psTzYjqfdkyjjlTKey);
				if(psTzYjqfdkyjjlT != null){
					psTzYjqfdkyjjlT.setTzIpAddr(tzIpAddr);
					psTzYjqfdkyjjlTMapper.updateByPrimaryKey(psTzYjqfdkyjjlT);
					
				}else{
					psTzYjqfdkyjjlT = new PsTzYjqfdkyjjlT();
					psTzYjqfdkyjjlT.setTzMlsmQfpcId(strDxYjId);
					psTzYjqfdkyjjlT.setTzEmail(strEmail);
					psTzYjqfdkyjjlT.setTzOpenDttm(new Date());
					psTzYjqfdkyjjlT.setTzIpAddr(tzIpAddr);
					psTzYjqfdkyjjlTMapper.insert(psTzYjqfdkyjjlT);
				}
			}
			
			/*邮件群发点击记录表*/
			if("CLICK".equals(strLinkType)){
				PsTzYjqfdjjlTKey psTzYjqfdjjlTKey = new PsTzYjqfdjjlTKey();
				psTzYjqfdjjlTKey.setTzMlsmQfpcId(strDxYjId);
				psTzYjqfdjjlTKey.setTzEmail(strEmail);
				psTzYjqfdjjlTKey.setTzDjDttm(new Date());
				PsTzYjqfdjjlT psTzYjqfdjjlT =  psTzYjqfdjjlTMapper.selectByPrimaryKey(psTzYjqfdjjlTKey);
				if(psTzYjqfdjjlT != null){
					psTzYjqfdjjlT.setTzRedUrl(strLinkSite);
					psTzYjqfdjjlT.setTzIpAddr(tzIpAddr);
					psTzYjqfdjjlTMapper.updateByPrimaryKey(psTzYjqfdjjlT);
				}else{
					psTzYjqfdjjlT = new PsTzYjqfdjjlT();
					psTzYjqfdjjlT.setTzMlsmQfpcId(strDxYjId);
					psTzYjqfdjjlT.setTzEmail(strEmail);
					psTzYjqfdjjlT.setTzDjDttm(new Date());
					psTzYjqfdjjlT.setTzRedUrl(strLinkSite);
					psTzYjqfdjjlT.setTzIpAddr(tzIpAddr);
					psTzYjqfdjjlTMapper.insert(psTzYjqfdjjlT);
				}
				
				System.out.println("==========邮件群发点击记录表 ===========Start======================>");
				System.out.println("=================com.tranzvision.gd.TZEmailSmsQFBundle.service.impl.TzGkTdClsServiceImpl==========================>");
				System.out.println("=====================TODO======================>");
				System.out.println("====================end=======================>");

				//TODO
				//String hmch = GetHardcodePointValue("TZ_EDM_HMCH");
				//%Response.RedirectURL(EncodeURL("/clueSource/edmRedirect.html?hmch=" | &hmch | "&hmpl=" | &strDxYjId | "&redirect=" | &strLinkSite));
			}
			
			/*邮件群发退订信息表*/
			if("CANCEL".equals(strLinkType)){
				PsTzYjqftdTblKey psTzYjqftdTblKey = new PsTzYjqftdTblKey();
				psTzYjqftdTblKey.setTzMlsmQfpcId(strDxYjId);
				psTzYjqftdTblKey.setTzEmail(strEmail);
				PsTzYjqftdTbl psTzYjqftdTbl = psTzYjqftdTblMapper.selectByPrimaryKey(psTzYjqftdTblKey);
				if(psTzYjqftdTbl != null ){
					psTzYjqftdTbl.setTzQxdyFlag("Y");
					psTzYjqftdTbl.setTzTdqd("MAL");
					psTzYjqftdTblMapper.updateByPrimaryKey(psTzYjqftdTbl);
				}else{
					psTzYjqftdTbl = new PsTzYjqftdTbl();
					psTzYjqftdTbl.setTzMlsmQfpcId(strDxYjId);
					psTzYjqftdTbl.setTzEmail(strEmail);
					psTzYjqftdTbl.setTzQxdyFlag("Y");
					psTzYjqftdTbl.setTzTdqd("MAL");
					psTzYjqftdTblMapper.insert(psTzYjqftdTbl);
				}
				
				try {
					strReturn = tzGDObject.getHTMLText("HTML.TZEmailSmsQFBundle.TZ_ADD_BM_CLASS_TABLE");
				} catch (TzSystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		return strReturn;
		
		
	}
	
	
	private String getIpAddress(HttpServletRequest request) { 
	    String ip = request.getHeader("x-forwarded-for"); 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getHeader("Proxy-Client-IP"); 
	    } 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getHeader("WL-Proxy-Client-IP"); 
	    } 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getHeader("HTTP_CLIENT_IP"); 
	    } 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getHeader("HTTP_X_FORWARDED_FOR"); 
	    } 
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	      ip = request.getRemoteAddr(); 
	    } 
	    return ip; 
	  } 

}
