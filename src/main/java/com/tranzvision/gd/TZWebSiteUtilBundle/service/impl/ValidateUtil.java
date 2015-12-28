package com.tranzvision.gd.TZWebSiteUtilBundle.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.ValidateUtil")
public class ValidateUtil {
	@Autowired
	private SqlQuery jdbcTemplate;
	
	public boolean validateEmail(String email){
		if(email == null ){
			return false;
		}
		
		//邮箱地址中是否存在@符号;
		int i = email.indexOf("@",1);
		int j = email.indexOf(".",i + 2);
		int last = email.lastIndexOf(".");
		if(i > 0 && j > 0 && (last+1) < email.length()){
			return true;
		}
		return false;
	}
	
	public boolean validatePhone(String phone){
		if(phone == null ){
			return false;
		}

		if(phone.length() < 6 || phone.length() > 16){
			return false;
		}
		return true;
	}
	
	public String getMessageTextWithLanguageCd(String jgId,String strLang,String msgSetId,String msgId, String defaultCNMsg, String defaultENMsg){
		String tmpMsgText = "";
		String superOrgId = "ADMIN";
		String sql = "SELECT ifnull(B.TZ_MSG_TEXT,A.TZ_MSG_TEXT) TZ_MSG_TEXT from PS_TZ_PT_XXDY_TBL A left join PS_TZ_PT_XXDY_TBL B on A.TZ_XXJH_ID = B.TZ_XXJH_ID and A.TZ_JG_ID=B.TZ_JG_ID and A.TZ_MSG_ID=B.TZ_MSG_ID where upper(B.TZ_LANGUAGE_ID)=upper(?) and upper(A.TZ_LANGUAGE_ID)=(SELECT UPPER(TZ_HARDCODE_VAL) TZ_LANGUAGE_CD FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT='TZGD_BASIC_LANGUAGE' ) AND A.TZ_XXJH_ID=? AND A.TZ_MSG_ID=? AND  UPPER(A.TZ_JG_ID)=UPPER(?)";
		try{
			tmpMsgText = jdbcTemplate.queryForObject(sql, new Object[]{strLang,msgSetId,msgId,jgId},"String");
		}catch(Exception e){
			
		}
		
		
		if(tmpMsgText == null || "".equals(tmpMsgText)){
			try{
				tmpMsgText = jdbcTemplate.queryForObject(sql, new Object[]{strLang,msgSetId,msgId,superOrgId},"String");
			}catch(Exception e){
				
			}
		}

		if (tmpMsgText == null || "".equals(tmpMsgText)) {
			if (!"ZHS".equals(strLang)) {
				tmpMsgText = defaultENMsg;
			} else {
				tmpMsgText = defaultCNMsg;
			}
		}
		return tmpMsgText;
	}
}
