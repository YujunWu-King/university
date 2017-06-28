package com.tranzvision.gd.TZCallCenterBundle.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetCookieSessionProps;
import com.tranzvision.gd.util.cookie.TzCookie;
import com.tranzvision.gd.util.encrypt.DESUtil;
import com.tranzvision.gd.util.security.TzFilterIllegalCharacter;
import com.tranzvision.gd.util.session.TzSession;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzCallCenterLoginServiceImpl;
import com.tranzvision.gd.TZCallCenterBundle.dao.PsTzPhJddTblMapper;
import com.tranzvision.gd.TZCallCenterBundle.model.PsTzPhJddTbl;

/**
 * 电话盒子弹屏链接
 * 
 * @author yuds
 * @since 2017-06-21
 */
@Controller
@RequestMapping(value = { "/callcenter/user/info" })
public class TZCallCenterController {

	@Autowired
	private TzCallCenterLoginServiceImpl tzCallCenterLoginServiceImpl;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private TzCookie tzCookie;
	
	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private TzFilterIllegalCharacter tzFilterIllegalCharacter;

	@Autowired
	private TZGDObject tzGDObject;
	
	@Autowired
	private PsTzPhJddTblMapper psTzPhJddTblMapper;
	
	@Autowired
	private GetSeqNum getSeqNum;
	
	@Autowired
	private GetCookieSessionProps getCookieProps;
	
	@RequestMapping(value = { "/{orgid}" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public void viewCallCenterPage(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "orgid") String orgid) {
		
		try {
			
			String currentDlzhId = tzLoginServiceImpl.getLoginedManagerDlzhid(request);
			
			orgid = tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(orgid).toUpperCase();

			String strUPDRRECID = request.getParameter("UPDRRECID");
			strUPDRRECID="367B31D97D80FF93";
			
			String tmpUserDlzh = DESUtil.decrypt(strUPDRRECID, "TZGDSSOFLG");
			
			String strTel = request.getParameter("tel");
			String strType = request.getParameter("type");		
			
			System.out.println("----------------------------");
			System.out.println(strTel + "--->" + strType);
			System.out.println(request);
			//来电接听后，将基本信息写入接待单表中;
			PsTzPhJddTbl psTzPhJddTbl = new PsTzPhJddTbl();
			String strSemNum = String.valueOf(getSeqNum.getSeqNum("TZ_PH_JDD_TBL", "TZ_XH"));
			psTzPhJddTbl.setTzXh(strSemNum);
			psTzPhJddTbl.setTzPhone(strTel);
			psTzPhJddTbl.setTzCallType(strType);
			psTzPhJddTbl.setTzCallDtime(new Date());
			psTzPhJddTbl.setTzDlzhId(tmpUserDlzh);
			psTzPhJddTblMapper.insert(psTzPhJddTbl);
			
			String domain = getCookieProps.getCookieDomain();
			String cookiePath = getCookieProps.getCookiePath();
			
			tzCookie.addCookie(response, "callCenterXh", strSemNum,36000,domain,cookiePath,false,false);
			tzCookie.addCookie(response, "callCenterPhone", strTel,36000,domain,cookiePath,false,false);
			tzCookie.addCookie(response, "callCenterType", strType,36000,domain,cookiePath,false,false);
			
			String strUrl = request.getContextPath() + "/index";
			
			//登录成功						
			String strHardSQL = "SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?";
			String mobileUrlMenu = sqlQuery.queryForObject(strHardSQL, new Object[]{"TZ_SEM_PHONE_USERMENU"},"String");
			
			strUrl = strUrl + "#" + mobileUrlMenu;
			
			boolean boolLogin = false;
			
			if("".equals(currentDlzhId)||"TZ_GUEST".equals(currentDlzhId)){				
				
				String tmpSQLText = "SELECT OPERPSWD FROM PSOPRDEFN WHERE OPRID=(SELECT OPRID FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_DLZH_ID=? AND TZ_JG_ID=?)";
				String passwordJm = sqlQuery.queryForObject(tmpSQLText, new Object[]{tmpUserDlzh,orgid},"String");
				String password = passwordJm == null ? "" : DESUtil.decrypt(passwordJm, "TZGD_Tranzvision");
				if(!"".equals(password)){
					
					ArrayList<String> aryErrorMsg = new ArrayList<String>();
					
					boolean boolAutoLogin = tzCallCenterLoginServiceImpl.doLogin(request, response, orgid, tmpUserDlzh, password, "", aryErrorMsg);
					
					if(boolAutoLogin){
						
					}else{
						boolLogin = true;
					}
				}else{
					boolLogin = true;
				}				
			}
			
			if(boolLogin) {
				strUrl =  request.getContextPath() + "/login/" + orgid.toLowerCase();	
			}
			
			try
			{
				//Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + strUrl);				
				response.sendRedirect(strUrl);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
