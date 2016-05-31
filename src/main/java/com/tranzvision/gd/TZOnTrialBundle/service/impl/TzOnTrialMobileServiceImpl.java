package com.tranzvision.gd.TZOnTrialBundle.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.CreateTaskServiceImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.SendSmsOrMalServiceImpl;
import com.tranzvision.gd.TZOnTrialBundle.dao.PsTzOnTrialTMapper;
import com.tranzvision.gd.TZOnTrialBundle.model.PsTzOnTrialTWithBLOBs;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZOnTrialBundle.service.impl.TzOnTrialMobileServiceImpl")
public class TzOnTrialMobileServiceImpl {
	@Autowired
	private HttpServletRequest request;

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

	public String applyOnTrialMobileInfo() {
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String, Object> map = new HashMap<>();
		map.put("resultFlg", "fail");
		map.put("loginURL", "");
		map.put("errorDescr", "");

		String errorDescr = "";

		try {
			// 机构名称;
			String orgName = request.getParameter("TZ_ORG_NAME");
			if (orgName == null || "".equals(orgName)) {
				errorDescr = "机构名称必填";
				map.replace("errorDescr", errorDescr);
				return jacksonUtil.Map2json(map);
			}

			// 联系人;
			String contactName = request.getParameter("TZ_CONTACT_NAME");
			if (contactName == null || "".equals(contactName)) {
				errorDescr = "联系人必填";
				map.replace("errorDescr", errorDescr);
				return jacksonUtil.Map2json(map);
			}

			// 联系电话;
			String contactPhone = request.getParameter("TZ_CONTACT_PHONE");
			if (contactPhone == null || "".equals(contactPhone)) {
				errorDescr = "联系电话必填";
				map.replace("errorDescr", errorDescr);
				return jacksonUtil.Map2json(map);
			}

			// Email;
			String email = request.getParameter("TZ_EMAIL");
			if (email == null || "".equals(email)) {
				errorDescr = "邮箱必填";
				map.replace("errorDescr", errorDescr);
				return jacksonUtil.Map2json(map);
			}
			
			String tel = request.getParameter("TZ_CONTACT_TEL");
			String website = request.getParameter("TZ_ORG_WEBSITE");
			//来源;
			String hmsr = request.getParameter("hmsr");
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
				errorDescr = "申请试用发送邮件失败，请于管理员联系";
				map.replace("errorDescr", errorDescr);
				return jacksonUtil.Map2json(map);
				
			}

			// 创建短信、邮件发送的听众;
			String createAudience = createTaskServiceImpl.createAudience(taskId, strJgid, "试用申请通知审核邮件模板", "JSRW");
			if (createAudience == null || "".equals(createAudience)) {
				errorDescr = "申请试用发送邮件失败，请于管理员联系";
				map.replace("errorDescr", errorDescr);
				return jacksonUtil.Map2json(map);
			}

			// 为听众添加听众成员;
			String sjrEmail = jdbcTemplate.queryForObject(
					" select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT='TZ_ONTRIAL_EMAIL'", "String");
			if (sjrEmail == null || "".equals(sjrEmail)) {
				sjrEmail = "Jun.Gao@tranzvision.com.cn";
			}
			String[] emailList = sjrEmail.split(";");
			for (int i = 0; i < emailList.length; i++) {
				if (emailList[i] != null && !"".equals(emailList[i])) {
					boolean addAudCy = createTaskServiceImpl.addAudCy(createAudience, "管理员", "管理员", "", "",
							emailList[i], "", "", "", "", "", String.valueOf(seqnum));
					if (addAudCy == false) {
						errorDescr = "申请试用发送邮件失败，请于管理员联系";
						map.replace("errorDescr", errorDescr);
						return jacksonUtil.Map2json(map);
					}
				}

			}

			sendSmsOrMalServiceImpl.send(taskId, "");

			// 通过所有校验，发送邮件;
			String loginURL = request.getContextPath() + "/m/trialSuccess";
			map.replace("resultFlg", "success");
			map.replace("loginURL", loginURL);
			map.replace("errorDescr", "");
			return jacksonUtil.Map2json(map);

		} catch (Exception e) {			
			errorDescr = "获取数据失败，请联系管理员";
			map.replace("errorDescr", errorDescr);
			return jacksonUtil.Map2json(map);
		}
	}
}
