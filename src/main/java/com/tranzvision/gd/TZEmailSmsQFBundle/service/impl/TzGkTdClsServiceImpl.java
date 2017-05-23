package com.tranzvision.gd.TZEmailSmsQFBundle.service.impl;

import java.util.Date;
import java.util.HashMap;
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
import com.tranzvision.gd.util.base.JacksonUtil;
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
			String strLinkType = "";
			String strDxYjId= "";
			String strLinkSite = "";
			String strTaskId = "";
			if(map != null){
				strLinkType = map.get("TZ_LINK_TYPE") == null ? "":String.valueOf(map.get("TZ_LINK_TYPE"));
				strDxYjId= map.get("TZ_MLSM_QFPC_ID") == null ?"":String.valueOf(map.get("TZ_MLSM_QFPC_ID"));
				strLinkSite= map.get("TZ_REDT_URL")==null ?"":String.valueOf(map.get("TZ_REDT_URL"));
				//Date strAddTime= (Date) map.get("TZ_ADD_DTTM");
				strTaskId = map.get("TZ_EML_SMS_TASK_ID")==null ?"":String.valueOf(map.get("TZ_EML_SMS_TASK_ID"));
			}
			
			
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
				
				try {
					/*
					String root = request.getContextPath();
					String hmch = jdbcTemplate.queryForObject("select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT='TZ_EDM_HMCH'", "String");
					if(hmch == null){
						hmch = "EDM";
					}
					strLinkSite = strLinkSite.replace("&", "%26");
					strLinkSite = strLinkSite.replace("?", "%3F");
					
					strReturn = tzGDObject.getHTMLText("HTML.TZEmailSmsQFBundle.TZ_QF_REDIRECT_HTML",root + "/statics/clueSource/edmRedirect.html?hmch=" + hmch + "&hmpl=" + strDxYjId +  "&redirect=" + strLinkSite);
					*/
					strReturn = tzGDObject.getHTMLText("HTML.TZEmailSmsQFBundle.TZ_QF_REDIRECT_HTML",strLinkSite);
				} catch (TzSystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
	
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);
		String fileUrl, AEId, emailQfpcId;
		Map<String, Object> returnMap = new HashMap<>();  
		
		
		AEId = jacksonUtil.getString("AEId");
		emailQfpcId = jacksonUtil.getString("yjqfId");
		
		fileUrl = jdbcTemplate.queryForObject("select TZ_FILE_PATH from PS_TZ_YJQFTXRZ_T where TZ_MLSM_QFPC_ID=? and PRCSINSTANCE=?", new Object[]{emailQfpcId,AEId},"String");
		if(fileUrl != null && !"".equals(fileUrl)){
			String RUNSTATUS = jdbcTemplate.queryForObject("SELECT PSQ.RUNSTATUS FROM PSPRCSRQST PSQ WHERE PSQ.PRCSINSTANCE=?", new Object[]{AEId},"String");
			if("9".equals(RUNSTATUS)){
				returnMap.put("fileUrl", fileUrl);
			}else{
				errMsg[0] = "1";
				errMsg[1] = "没有找到日志";
			}
		}else{
			errMsg[0] = "1";
			errMsg[1] = "没有找到日志";
		}
		return jacksonUtil.Map2json(returnMap);
		
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
