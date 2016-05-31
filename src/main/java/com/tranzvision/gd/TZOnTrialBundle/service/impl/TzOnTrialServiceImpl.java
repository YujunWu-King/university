package com.tranzvision.gd.TZOnTrialBundle.service.impl;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.CreateTaskServiceImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.SendSmsOrMalServiceImpl;
import com.tranzvision.gd.TZOnTrialBundle.dao.PsTzOnTrialTMapper;
import com.tranzvision.gd.TZOnTrialBundle.model.PsTzOnTrialTWithBLOBs;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.captcha.Patchca;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 
 * @author tang
 * 试用申请前台页面
 */
@Service("com.tranzvision.gd.TZOnTrialBundle.service.impl.TzOnTrialServiceImpl")
public class TzOnTrialServiceImpl extends FrameworkImpl {
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private TZGDObject tzGDObject;
	
	@Autowired
	private GetSeqNum getSeqNum;
	
	@Autowired
	private PsTzOnTrialTMapper psTzOnTrialTMapper;
	
	@Autowired
	private CreateTaskServiceImpl createTaskServiceImpl;
	@Autowired
	private SendSmsOrMalServiceImpl sendSmsOrMalServiceImpl;
	
	@Autowired
	private SqlQuery jdbcTemplate;
	
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		String strSen = "";
		String strResponse = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try{
			jacksonUtil.json2Map(strParams);
			if(jacksonUtil.containsKey("sen")){
				strSen = jacksonUtil.getString("sen");
				if("1".equals(strSen)){
					strResponse = this.checkCodeVerifyByActive(strParams, errMsg);
				}
				
				if("2".equals(strSen)){
					strResponse = this.applyOnTrialInfo(strParams, errMsg);
				}

			}
			
		}catch(Exception e){
			e.printStackTrace();
			errMsg[0] = "100";
			errMsg[1] = "获取数据失败，请联系管理员";
		}
		
		return strResponse;
	}
	
	public String applyOnTrialInfo(String strParams, String[] errMsg){
		String strResult = "\"failure\"";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try{
			jacksonUtil.json2Map(strParams);

			if(jacksonUtil.containsKey("data")){
				Map<String, Object> dataMap  = jacksonUtil.getMap("data");
				
				//验证码;
			    String strCheckedEmail = "";
			    if(dataMap.containsKey("yzmEmail")){ 
			    	strCheckedEmail = ((String)dataMap.get("yzmEmail")).trim();
			    }
			     
			    
			    //验证码
			    if(strCheckedEmail != null && !"".equals(strCheckedEmail)){
			    	// 校验验证码
					Patchca patchca = new Patchca();
					if (!patchca.verifyToken(request, strCheckedEmail)) {
						errMsg[0] = "1";
						errMsg[1] = "验证码不正确";
						return strResult;
					}
					
			    }else{
			    	errMsg[0] = "1";
					errMsg[1] = "验证码不正确";
					return strResult;
			    }
			    
				//机构名称;
			    String orgName = "";
			    if(dataMap.containsKey("TZ_ORG_NAME")){ 
			    	orgName = ((String)dataMap.get("TZ_ORG_NAME")).trim();
			    }
			    if(orgName == null  || "".equals(orgName)){
			    	errMsg[0] = "1";
					errMsg[1] = "机构名称不能为空";
					return strResult;
			    }
			    
			    //联系人;
			    String contactName = "";
			    if(dataMap.containsKey("TZ_CONTACT_NAME")){ 
			    	contactName = ((String)dataMap.get("TZ_CONTACT_NAME")).trim();
			    }
			    if(contactName == null  || "".equals(contactName)){
			    	errMsg[0] = "1";
					errMsg[1] = "联系人不能为空";
					return strResult;
			    }
			    
			    
			    //联系电话;
			    String contactPhone = "";
			    if(dataMap.containsKey("TZ_CONTACT_PHONE")){ 
			    	contactPhone = ((String)dataMap.get("TZ_CONTACT_PHONE")).trim();
			    }
			    if(contactPhone == null  || "".equals(contactPhone)){
			    	errMsg[0] = "1";
					errMsg[1] = "联系电话不能为空";
					return strResult;
			    }
			    
			    //Email;
			    String email = "";
			    if(dataMap.containsKey("TZ_EMAIL")){ 
			    	email = ((String)dataMap.get("TZ_EMAIL")).trim();
			    }
			    if(email == null  || "".equals(email)){
			    	errMsg[0] = "1";
					errMsg[1] = "Email不能为空";
					return strResult;
			    }
			    
			    //座机;
			    String tel = "";
			    if(dataMap.containsKey("TZ_CONTACT_TEL")){ 
			    	tel = ((String)dataMap.get("TZ_CONTACT_TEL")).trim();
			    }
			    
			    //机构网址;
			    String website = "";
			    if(dataMap.containsKey("TZ_ORG_WEBSITE")){ 
			    	website = ((String)dataMap.get("TZ_ORG_WEBSITE")).trim();
			    }
			    
			    //来源;
			    String hmsr = "";
			    if(dataMap.containsKey("hmsr")){ 
			    	hmsr = ((String)dataMap.get("hmsr")).trim();
			    }
			    //如果没值，则读取cookie，看是不是从百度推广等渠道过来的;
			    if(hmsr == null || "".equals(hmsr)){
			    	Cookie[] cookies = request.getCookies();
		            for(Cookie c :cookies ){
		                String cookieName = c.getName();
		                if("TZ_HMSR".equals(cookieName)){
		                	hmsr = c.getValue();
		                }
		            }
			    }
			    
			    int seqnum = getSeqNum.getSeqNum("PS_TZ_ON_TRIAL_T", "TZ_SEQ_NUM");
			    PsTzOnTrialTWithBLOBs psTzOnTrialT = new PsTzOnTrialTWithBLOBs();
			    psTzOnTrialT.setTzSeqNum(seqnum);
			    psTzOnTrialT.setTzOrgName(orgName);
			    psTzOnTrialT.setTzContactName(contactName);
			    psTzOnTrialT.setTzContactPhone(contactPhone);
			    psTzOnTrialT.setTzTel(tel);
			    psTzOnTrialT.setTzEmail(email);
			    psTzOnTrialT.setTzOrgWebsite(website);
			    psTzOnTrialT.setTzHmsr(hmsr);
			    psTzOnTrialT.setRowAddTime(new Date());
			    psTzOnTrialTMapper.insert(psTzOnTrialT);
			    
			    String strJgid = "ADMIN";
			    // 发送邮件;
				String taskId = createTaskServiceImpl.createTaskIns(strJgid, "TZ_ON_TRIAL", "MAL", "A");

				if (taskId == null || "".equals(taskId)) {
					errMsg[0] = "1";
					errMsg[1] = "申请试用发送邮件失败，请于管理员联系";
					return strResult;
				}

				// 创建短信、邮件发送的听众;
				String createAudience = createTaskServiceImpl.createAudience(taskId,strJgid,"试用申请通知审核邮件模板", "JSRW");
				if (createAudience == null || "".equals(createAudience)) {
					errMsg[0] = "1";
					errMsg[1] = "申请试用发送邮件失败，请于管理员联系";
					return strResult;
				}

				// 为听众添加听众成员;
				String sjrEmail = jdbcTemplate.queryForObject(" select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT='TZ_ONTRIAL_EMAIL'", "String");
				if(sjrEmail == null || "".equals(sjrEmail)){
					sjrEmail = "Jun.Gao@tranzvision.com.cn";
				}
				String[] emailList = sjrEmail.split(";");
				for(int i = 0; i < emailList.length; i++){
					if(emailList[i] != null && !"".equals(emailList[i])){
						boolean addAudCy = createTaskServiceImpl.addAudCy(createAudience,"管理员", "管理员", "", "", emailList[i], "", "", "","", "", String.valueOf(seqnum));
						if (addAudCy == false) {
							errMsg[0] = "1";
							errMsg[1] = "申请试用发送邮件失败，请于管理员联系";
							return strResult;
						}
					}
					
				}

				sendSmsOrMalServiceImpl.send(taskId, "");
			    
			    // 通过所有校验，发送邮件;
			    strResult = "\"success\"";
			    errMsg[0] = "0";
				errMsg[1] = "试用申请邮件已经发送，请等待审核";
		        return strResult;
			}

		}catch(Exception e){
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "获取数据失败，请联系管理员";
		}
		
		return strResult;
	}
	
	/*校验验证码*/
	public String checkCodeVerifyByActive(String strParams, String[] errMsg){
		String strCheckCode = "";
		
		String strResult = "\"failure\"";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try{
			jacksonUtil.json2Map(strParams);
			if(jacksonUtil.containsKey("checkCode"))
				strCheckCode = jacksonUtil.getString("checkCode").trim();
				// 校验验证码
				Patchca patchca = new Patchca();
				if (!patchca.verifyToken(request, strCheckCode)) {
					errMsg[0] = "1";
					errMsg[1] = "验证码不正确";
					return strResult;
				}
				
		      	
		      	strResult = "\"success\"";
		        return strResult;
		}catch(Exception e){
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "验证码不正确";
		}
		
		return strResult;
	}
	
	/*使用申请页面*/
	public String tzApply(){
		String contextPath = request.getContextPath();
		String hmsr = request.getParameter("hmsr");
		if(hmsr == null){
			hmsr = "";
		}
		String loginHtml = "";
		try {
			loginHtml = tzGDObject.getHTMLText("HTML.TZOnTrialBundle.TZ_ON_TRIAL_HTML", true,contextPath,hmsr);
		} catch (TzSystemException e) {
			e.printStackTrace();
			loginHtml = "申请试用访问失败，请于管理员联系";
		}
		return loginHtml;
	}
	
	
	/*使用申请页面*/
	public String tzMApply(){
		String contextPath = request.getContextPath();
		String hmsr = request.getParameter("hmsr");
		if(hmsr == null){
			hmsr = "";
		}
		String loginHtml = "";
		try {
			loginHtml = tzGDObject.getHTMLText("HTML.TZOnTrialBundle.TZ_ON_TRIAL_M_HTML", true,contextPath,hmsr);
		} catch (TzSystemException e) {
			e.printStackTrace();
			loginHtml = "申请试用访问失败，请于管理员联系";
		}
		return loginHtml;
	}
}
