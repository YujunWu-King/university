package com.tranzvision.gd.TZCallCenterBundle.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
//import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZCallCenterBundle.service.impl.TZCallCenterServiceImpl;

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
	
	@Autowired
	private TZCallCenterServiceImpl tzCallCenterServiceImpl;
	
	@RequestMapping(value = { "/{orgid}" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public void viewCallCenterPage(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "orgid") String orgid) {
		
		try {
			
			Calendar c = new GregorianCalendar();
			c.set(1970, 0, 1, 0, 0, 0);		
			Date beginDTime = c.getTime();
			
			String currentDlzhId = tzLoginServiceImpl.getLoginedManagerDlzhid(request);
		//	System.out.println("currentDlzhId="+currentDlzhId);
			orgid = tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(orgid).toUpperCase();
		//	System.out.println("orgid="+orgid);
			String strUPDRRECID = request.getParameter("UPDRRECID");
		//	System.out.println("strUPDRRECID="+strUPDRRECID);
			//不使用加密，使用base64位编码
			//String tmpUserDlzh = DESUtil.decrypt(strUPDRRECID, "TZGDSSOFLG");
			String tmpUserDlzh = new String(org.apache.commons.codec.binary.Base64.decodeBase64(strUPDRRECID));
			System.out.println("tmpUserDlzh="+tmpUserDlzh);
			//System.out.println("原数据：" + strUPDRRECID + "，解析数据为：" + tmpUserDlzh);
			String strUserId = "";
			Date loginEndDTime = null;
			//VB中加密用户名组成为：USERID + "|" + 时间戳
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			try{
				int pos = tmpUserDlzh.indexOf("|");
				System.out.println("pos="+pos);
				if(pos>0){
					strUserId = tmpUserDlzh.substring(0, pos);
					System.out.println("strUserId="+strUserId);
					pos = pos + 1;
					String strTimeStap = tmpUserDlzh.substring(pos, tmpUserDlzh.length());
					System.out.println("strTimeStap="+strTimeStap);
					int time = Integer.valueOf(strTimeStap);
					System.out.println("time="+time);
					//当前加密结果，在时间戳20分钟内有效
					time = time + 20 * 60;
					loginEndDTime = addSecond(beginDTime, time);
					System.out.println("loginEndDTime="+loginEndDTime);
				}
			}catch(Exception e){
				/**/
			}
//			System.out.println("原用户名：" + tmpUserDlzh);
//			System.out.println("用户名：" + strUserId);
//			System.out.println("登录有效截止时间：" + sdf.format(loginEndDTime));
			String strTel = request.getParameter("tel");
			System.out.println("strTel="+strTel);
			String strType = request.getParameter("type");		
			if(strType==null||"".equals(strType)){
				strType = "A";
			}
			//来电接听后，将基本信息写入接待单表中;
			PsTzPhJddTbl psTzPhJddTbl = new PsTzPhJddTbl();
			String strSemNum = String.valueOf(getSeqNum.getSeqNum("TZ_PH_JDD_TBL", "TZ_XH"));
			System.out.println("strSemNum="+strSemNum);
			String strExistFlg = sqlQuery.queryForObject("SELECT 'Y' FROM PS_TZ_PH_JDD_TBL WHERE TZ_XH=?", new Object[]{strSemNum}, "String");
			while ("Y".equals(strExistFlg)){
				strSemNum = String.valueOf(getSeqNum.getSeqNum("TZ_PH_JDD_TBL", "TZ_XH"));
				strExistFlg = sqlQuery.queryForObject("SELECT 'Y' FROM PS_TZ_PH_JDD_TBL WHERE TZ_XH=?", new Object[]{strSemNum}, "String");
			}
			
			psTzPhJddTbl.setTzXh(strSemNum);
			psTzPhJddTbl.setTzPhone(strTel);
			psTzPhJddTbl.setTzCallType(strType);
			String strOprId = tzCallCenterServiceImpl.findOprID(strSemNum, strTel);
			System.out.println("strOprId="+strOprId);
			if(strOprId!=null&&!"".equals(strOprId)){
				psTzPhJddTbl.setTzOprid(strOprId);
			}
			psTzPhJddTbl.setTzCallDtime(new Date());
			psTzPhJddTbl.setTzDlzhId(strUserId);
			psTzPhJddTbl.setTzDealwithZt("A");
			psTzPhJddTblMapper.insert(psTzPhJddTbl);
			
			String domain = getCookieProps.getCookieDomain();
			System.out.println("domain="+domain);
			String cookiePath = getCookieProps.getCookiePath();
			System.out.println("cookiePath="+cookiePath);
			tzCookie.addCookie(response, "callCenterXh", strSemNum,36000,domain,cookiePath,false,false);
			tzCookie.addCookie(response, "callCenterPhone", strTel,36000,domain,cookiePath,false,false);
			tzCookie.addCookie(response, "callCenterType", strType,36000,domain,cookiePath,false,false);
			tzCookie.addCookie(response, "callCenterOprid", strOprId,36000,domain,cookiePath,false,false);
			
			String strUrl = request.getContextPath() + "/index";
			
			//登录成功						
			String strHardSQL = "SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?";
			String mobileUrlMenu = sqlQuery.queryForObject(strHardSQL, new Object[]{"TZ_SEM_PHONE_USERMENU"},"String");
			System.out.println("mobileUrlMenu="+mobileUrlMenu);
			strUrl = strUrl + "#" + mobileUrlMenu;
			
			boolean boolLogin = false;
			
			Date currentDTime = new Date();
			
			if("".equals(currentDlzhId)||"TZ_GUEST".equals(currentDlzhId)||"Admin".equals(currentDlzhId)){				
				if("".equals(strUserId)||loginEndDTime.before(currentDTime)){
					boolLogin = true;
				}else{
					String tmpSQLText = "SELECT OPERPSWD FROM PSOPRDEFN WHERE OPRID=(SELECT OPRID FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_DLZH_ID=? AND TZ_JG_ID=?)";
					String passwordJm = sqlQuery.queryForObject(tmpSQLText, new Object[]{strUserId,orgid},"String");
					String password = passwordJm == null ? "" : DESUtil.decrypt(passwordJm, "TZGD_Tranzvision");
					if(!"".equals(password)){
						
						ArrayList<String> aryErrorMsg = new ArrayList<String>();
						
						boolean boolAutoLogin = tzCallCenterLoginServiceImpl.doLogin(request, response, orgid, strUserId, password, "", aryErrorMsg);
						
						if(boolAutoLogin){

						}else{
							boolLogin = true;
						}
					}else{
						boolLogin = true;
					}	
				}						
			}
			
			if(boolLogin) {
				strUrl =  request.getContextPath() + "/login/" + orgid.toLowerCase();	
			}
			
			try
			{		
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
	
	public static Date addSecond(Date date, int seconds) { 
		Calendar calendar = Calendar.getInstance(); 
		calendar.setTime(date); 
		calendar.add(Calendar.SECOND, seconds); 
		return calendar.getTime(); 
	}
}
