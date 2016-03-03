package com.tranzvision.gd.TZRecommendationBundle.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.CreateTaskServiceImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.SendSmsOrMalServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.MessageTextServiceImpl;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * PS:TZ_GD_TJX_PKG:TZ_TJX_THANKS
 * @author tang
 *
 */
@Service("com.tranzvision.gd.TZRecommendationBundle.service.impl.TzTjxThanksServiceImpl")
public class TzTjxThanksServiceImpl extends FrameworkImpl{
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private MessageTextServiceImpl messageTextServiceImpl;
	@Autowired
	private CreateTaskServiceImpl createTaskServiceImpl;
	@Autowired
	private SendSmsOrMalServiceImpl sendSmsOrMalServiceImpl;
	
	//发送推荐信邮件;
	public String sendTJX_Thanks(int numAppinsId){
		String strRtn = "";
		//numAppinsId 推荐信报名表编号;
		// 推荐信姓名,考生oprid,推荐人邮箱;
		String str_tjr_gname = "";
		String str_tjr_title = "";
		String str_name_suff = "";
		String str_tjr_name = "", str_ks_oprid = "", str_tjr_email = "";
		Map<String, Object> map = jdbcTemplate.queryForMap("SELECT OPRID,TZ_TJX_TITLE,TZ_REFERRER_NAME,TZ_REFERRER_GNAME,TZ_EMAIL FROM PS_TZ_KS_TJX_TBL WHERE TZ_TJX_APP_INS_ID=? AND TZ_MBA_TJX_YX='Y'",new Object[]{numAppinsId});
		if(map != null ){
			str_ks_oprid = (String)map.get("OPRID");
			str_tjr_title = (String)map.get("TZ_TJX_TITLE");
			str_tjr_name = (String)map.get("TZ_REFERRER_NAME");
			str_tjr_gname = (String)map.get("TZ_REFERRER_GNAME");
			str_tjr_email = (String)map.get("TZ_EMAIL");
		}
		
		String str_none_blank = jdbcTemplate.queryForObject("SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT = ?", new Object[]{"TZ_REF_TITLE_NONE_BLANK"},"String");
		String str_none_blank_c = jdbcTemplate.queryForObject("SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT = ?", new Object[]{"TZ_REF_TITLE_NONE_BLANK_C"},"String");
		
		if(str_tjr_title != null && !"".equals(str_tjr_title)
				&& !str_tjr_title.equals(str_none_blank)
				&& !str_tjr_title.equals(str_none_blank_c)){
			str_name_suff = str_tjr_title;
		}
		
		if(str_tjr_gname != null && !"".equals(str_tjr_gname)){
			if(str_name_suff != null && !"".equals(str_name_suff)){
				str_name_suff = str_name_suff + " " + str_tjr_gname;
			}else{
				str_name_suff = str_tjr_gname;
			}
		}
		
		if(str_name_suff != null && !"".equals(str_name_suff)){
			str_tjr_name = str_name_suff + " " + str_tjr_name;
		}
		
		// 语言,机构ID;
		String str_language = "" , strJgid = "";
		Map<String, Object> jgLangMap = jdbcTemplate.queryForMap("SELECT B.TZ_APP_TPL_LAN,B.TZ_JG_ID FROM PS_TZ_APP_INS_T A,PS_TZ_APPTPL_DY_T B WHERE A.TZ_APP_INS_ID=? AND A.TZ_APP_TPL_ID=B.TZ_APP_TPL_ID limit 0,1",new Object[]{numAppinsId});
		if(jgLangMap != null){
			str_language = (String)jgLangMap.get("TZ_APP_TPL_LAN");
			strJgid = (String)jgLangMap.get("TZ_JG_ID");
		}
		if(numAppinsId == 0){
			strRtn = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_APPONLINE_MSGSET", "NO_INS_ID", str_language, "", "");
		    return strRtn;
		}
		
		//邮件模板;
		String str_email_mb = "";
		if("ENG".equals(str_language)){
			str_email_mb = jdbcTemplate.queryForObject("SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT = ?", new Object[]{"TZ_TJX_THANKS_ENG"},"String");
		}else{
			str_email_mb = jdbcTemplate.queryForObject("SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT = ?", new Object[]{"TZ_TJX_THANKS_ZHS"},"String");
		}
		
		String mess = "";
		// 发送邮件;
		String taskId = createTaskServiceImpl.createTaskIns(strJgid, str_email_mb, "MAL", "A");
		if (taskId == null || "".equals(taskId)) {
			mess = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_APPONLINE_MSGSET", "CR_E_FAIL", str_language, "", "");
			return mess;
		}

		// 创建短信、邮件发送的听众;
		String createAudience = createTaskServiceImpl.createAudience(taskId,strJgid,"推荐信感谢邮件", "TJXS");
		if (createAudience == null || "".equals(createAudience)) {
			mess = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_APPONLINE_MSGSET", "CR_L_FAIL", str_language, "", "");
			return mess;
		}

		// 为听众添加听众成员;
		boolean addAudCy = createTaskServiceImpl.addAudCy(taskId,str_tjr_name, str_tjr_name, "", "", str_tjr_email, "", "", str_ks_oprid, "", "", String.valueOf(numAppinsId));
		if (addAudCy == false) {
			mess = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_APPONLINE_MSGSET", "ADD_L_FAIL", str_language, "", "");
			return mess;
		}

		// 得到创建的任务ID;
		if (taskId == null || "".equals(taskId)) {
			mess = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_APPONLINE_MSGSET", "CR_ID_FAIL", str_language, "", "");
			return mess;
		}

		sendSmsOrMalServiceImpl.send(taskId, "");
		mess = "SUCCESS";
		return mess;
	}
	
	@Override
	public String tzGetJsonData(String strParams){
		Map<String, Object> returnMap = new HashMap<>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try{
			jacksonUtil.json2Map(strParams);
			
			// 人员id;
			String str_oprid = jacksonUtil.getString("OPRID");
			// 班级id;
			String str_bj_id = jacksonUtil.getString("bj_id");
			String str_jg_id = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			
			int str_bmb_id = jdbcTemplate.queryForObject("SELECT TZ_APP_INS_ID FROM PS_TZ_FORM_WRK_T WHERE TZ_CLASS_ID=? AND OPRID=? limit 0,1", new Object[]{str_bj_id, str_oprid},"Integer");
			Map<String, Object> map = jdbcTemplate.queryForMap("SELECT TZ_REALNAME,TZ_EMAIL FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=? AND TZ_JG_ID=? limit 0,1",new Object[]{str_oprid, str_jg_id});
			String str_name = "",str_email="";
			if(map != null ){
				str_name = (String)map.get("TZ_REALNAME");
				str_email = (String)map.get("TZ_EMAIL");
			}
			
			// 创建短信、邮件发送的听众;
			String crtAudi = createTaskServiceImpl.createAudience("",str_jg_id,"推荐信催促邮件", "TJXC");
			if (crtAudi != null && !"".equals(crtAudi)) {
				// 为听众添加听众成员;
				boolean addAudCy = createTaskServiceImpl.addAudCy(crtAudi,str_name, str_name, "", "", str_email, str_email, "", str_oprid, "", "", String.valueOf(str_bmb_id));
				if(addAudCy){
					returnMap.put("audienceId", crtAudi);
				}
				
			}
			
		}catch(Exception e){
			
		}
		return jacksonUtil.Map2json(returnMap);
	}
}
