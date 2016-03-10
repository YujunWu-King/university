package com.tranzvision.gd.TZEmailSmsSendBundle.service.impl;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.dao.PsTzAudcyuanTMapper;
import com.tranzvision.gd.TZEmailSmsSendBundle.dao.PsTzAudienceTMapper;
import com.tranzvision.gd.TZEmailSmsSendBundle.dao.PsTzDxmbshliTblMapper;
import com.tranzvision.gd.TZEmailSmsSendBundle.dao.PsTzDxyjfsrwTblMapper;
import com.tranzvision.gd.TZEmailSmsSendBundle.dao.PsTzMalBcAddTMapper;
import com.tranzvision.gd.TZEmailSmsSendBundle.dao.PsTzMalCcAddTMapper;
import com.tranzvision.gd.TZEmailSmsSendBundle.dao.PsTzRwFjianTblMapper;
import com.tranzvision.gd.TZEmailSmsSendBundle.dao.PsTzYjmbshliTblMapper;
import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzAudcyuanT;
import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzAudienceT;
import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzDxmbshliTbl;
import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzDxyjfsrwTbl;
import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzMalBcAddT;
import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzMalCcAddT;
import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzRwFjianTbl;
import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzYjmbshliTbl;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 创建邮件短信任务；原：TZ_SMS_MAL:CreateTask
 * 
 * @author tang
 * @since 2015-11-30
 */
@Service("com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.CreateTaskServiceImpl")
public class CreateTaskServiceImpl {
	// 发送任务ID;
	//private String taskId;
	// 听众ID；
	//private String audId;
	// 听众ID；
	//private String jgId;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private PsTzDxyjfsrwTblMapper psTzDxyjfsrwTblMapper;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private PsTzAudienceTMapper psTzAudienceTMapper;
	@Autowired
	private PsTzAudcyuanTMapper psTzAudcyuanTMapper;
	@Autowired
	private PsTzMalCcAddTMapper psTzMalCcAddTMapper;
	@Autowired
	private PsTzMalBcAddTMapper psTzMalBcAddTMapper;
	@Autowired
	private PsTzYjmbshliTblMapper psTzYjmbshliTblMapper;
	@Autowired
	private PsTzRwFjianTblMapper psTzRwFjianTblMapper;
	@Autowired
	private PsTzDxmbshliTblMapper psTzDxmbshliTblMapper;


	// 创建发送任务***参数：strJgId：机构ID；strTmpId:邮件或短信模板ID；taskType：发送类型，邮件MAL,短信SMS；sendtype:使用的发送手机或邮箱,A主要，B次要，C所有;
	@SuppressWarnings("deprecation")
	public String createTaskIns(String strJgId, String strTmpId, String taskType, String sendtype) {
		String taskId = "";
		try {
			if (strJgId == null || "".equals(strJgId) || strTmpId == null || "".equals(strTmpId) || taskType == null
					|| "".equals(taskType) || sendtype == null || "".equals(sendtype)) {
				return "";
			}

			taskId = String.valueOf(getSeqNum.getSeqNum("TZ_DXYJFSRW_TBL", "TZ_EML_SMS_TASK_ID"));
			PsTzDxyjfsrwTbl psTzDxyjfsrwTbl = new PsTzDxyjfsrwTbl();
			psTzDxyjfsrwTbl.setTzEmlSmsTaskId(taskId);
			psTzDxyjfsrwTbl.setTzTaskLx(taskType);
			psTzDxyjfsrwTbl.setTzTmplId(strTmpId);
			psTzDxyjfsrwTbl.setTzJgId(strJgId);
			psTzDxyjfsrwTbl.setTzSyyxLx(sendtype);
			//jgId = strJgId;
			String emailZt = "";
			String emailContent = "";
			String smsConent = "";
			psTzDxyjfsrwTbl.setTzBatchBz("");
			if ("MAL".equals(taskType)) {
				String emailsql = " select a.TZ_DYNAMIC_FLAG, a.TZ_WEBMAL_FLAG , a.TZ_EMLSERV_ID, a.TZ_MAL_SUBJUECT,a.TZ_MAL_CONTENT, a.TZ_YMB_ID,a.TZ_EML_IF_PRT from PS_TZ_EMALTMPL_TBL a, PS_TZ_TMP_DEFN_TBL b where a.TZ_YMB_ID=b.TZ_YMB_ID and a.TZ_JG_ID=? and a.TZ_TMPL_ID=?";
				Map<String, Object> eMap = jdbcTemplate.queryForMap(emailsql, new Object[] { strJgId, strTmpId });
				String emlServId = (String) eMap.get("TZ_EMLSERV_ID");
				emailZt = (String) eMap.get("TZ_MAL_SUBJUECT");
				emailContent = (String) eMap.get("TZ_MAL_CONTENT");
				psTzDxyjfsrwTbl.setTzEmlservId((String) eMap.get("TZ_EMLSERV_ID"));
				psTzDxyjfsrwTbl.setTzWebmalFlag((String) eMap.get("TZ_WEBMAL_FLAG"));
				psTzDxyjfsrwTbl.setTzDynamicFlag((String) eMap.get("TZ_DYNAMIC_FLAG"));
				psTzDxyjfsrwTbl.setTzEmlIfPrt((String) eMap.get("TZ_EML_IF_PRT"));

				String emailServSQL = "select TZ_EML_ADDR100,TZ_EML_ALIAS from PS_TZ_EMLS_DEF_TBL where TZ_EMLSERV_ID=?";
				Map<String, Object> eMap2 = jdbcTemplate.queryForMap(emailServSQL, new Object[] { emlServId });
				if( eMap2 != null){
					psTzDxyjfsrwTbl.setTzEmailSender((String) eMap2.get("TZ_EML_ADDR100"));
					psTzDxyjfsrwTbl.setTzSenderAlias((String) eMap2.get("TZ_EML_ALIAS"));
				}
				
			} else {
				String smsServId = "";
				Map<String, Object> eMap3 = jdbcTemplate.queryForMap("select a.TZ_DYNAMIC_FLAG , a.TZ_SMS_SERV_ID,a.TZ_SMS_CONTENT, a.TZ_YMB_ID from PS_TZ_SMSTMPL_TBL a, PS_TZ_TMP_DEFN_TBL b where a.TZ_YMB_ID=b.TZ_YMB_ID and a.TZ_JG_ID=? and a.TZ_TMPL_ID=?", new Object[] { strJgId, strTmpId });
			    if(eMap3 != null){
			    	String dynimicFlg = (String)eMap3.get("TZ_DYNAMIC_FLAG");
			    	smsServId = (String)eMap3.get("TZ_SMS_SERV_ID");
			    	smsConent = (String)eMap3.get("TZ_SMS_CONTENT");
			    	//String strYmbId = (String)eMap3.get("TZ_YMB_ID");
			    	
			    	psTzDxyjfsrwTbl.setTzSmsServId(smsServId);
			    	psTzDxyjfsrwTbl.setTzDynamicFlag(dynimicFlg);
			    	psTzDxyjfsrwTbl.setTzSysjLx(sendtype);
			    }
			}
			psTzDxyjfsrwTbl.setTzRwtjDt(new Date());
			psTzDxyjfsrwTbl.setTzRwksDt(new Date(1900, 1, 1));
			psTzDxyjfsrwTbl.setTzRwjsDt(new Date(2900, 12, 31));
			psTzDxyjfsrwTbl.setTzRwzxZt("A");
			psTzDxyjfsrwTbl.setRowAddedDttm(new Date());
			psTzDxyjfsrwTbl.setRowAddedOprid(tzLoginServiceImpl.getLoginedManagerOprid(request));
			int i = psTzDxyjfsrwTblMapper.insert(psTzDxyjfsrwTbl);
			if (i > 0) {
				
				if("MAL".equals(taskType)){
					PsTzYjmbshliTbl psTzYjmbshliTbl = new PsTzYjmbshliTbl();
					psTzYjmbshliTbl.setTzEmlSmsTaskId(taskId);
					psTzYjmbshliTbl.setTzMalSubjuect(emailZt);
					psTzYjmbshliTbl.setTzMalContent(emailContent);
					psTzYjmbshliTblMapper.insert(psTzYjmbshliTbl);
				}else{
					PsTzDxmbshliTbl psTzDxmbshliTbl = new PsTzDxmbshliTbl();
					psTzDxmbshliTbl.setTzEmlSmsTaskId(taskId);
					psTzDxmbshliTbl.setTzSmsContent(smsConent);
					psTzDxmbshliTblMapper.insert(psTzDxmbshliTbl);
				}
			}
		} catch (Exception e) {
			return "";
		}

		return taskId;
	}

	// 创建听众
	public String createAudience(String taskId,String strJgId,String strAudienceDesc, String strAudLy) {
		String audId = "";
		try {
			audId = String.valueOf(getSeqNum.getSeqNum("TZ_AUDIENCE_T", "TZ_AUDIENCE_ID"));
			PsTzAudienceT psTzAudienceT = new PsTzAudienceT();
			psTzAudienceT.setTzAudienceId(audId);
			psTzAudienceT.setTzAudMs(strAudienceDesc);
			psTzAudienceT.setTzAudLy(strAudLy);
			psTzAudienceT.setTzJgId(strJgId);
			psTzAudienceT.setRowAddedDttm(new Date());
			psTzAudienceT.setRowAddedOprid(tzLoginServiceImpl.getLoginedManagerOprid(request));
			psTzAudienceT.setRowLastmantDttm(new Date());
			psTzAudienceT.setRowLastmantOprid(tzLoginServiceImpl.getLoginedManagerOprid(request));
			int i = psTzAudienceTMapper.insert(psTzAudienceT);
			if (i <= 0) {
				audId = "";
			}else{
				if(taskId != null && !"".equals(taskId)){
					PsTzDxyjfsrwTbl psTzDxyjfsrwTbl = new PsTzDxyjfsrwTbl();
					psTzDxyjfsrwTbl.setTzEmlSmsTaskId(taskId);
					psTzDxyjfsrwTbl.setTzAudienceId(audId);
					psTzDxyjfsrwTblMapper.updateByPrimaryKeySelective(psTzDxyjfsrwTbl);
				}
				
			}
		} catch (Exception e) {
			audId = "";
		}
		return audId;
	}

	// 添加听众成员;
	public boolean addAudCy(String audId,String name, String ch, String mainPhone, String cyPhone, String mainEmail, String cyEmail,
			String wxh, String oprId, String xsxxId, String hdId, String bmbId) {
		boolean bl = false;
		try {
			String audCyId = String.valueOf(getSeqNum.getSeqNum("TZ_AUDCYUAN_T", "TZ_AUDCY_ID"));
			PsTzAudcyuanT psTzAudcyuanT = new PsTzAudcyuanT();
			psTzAudcyuanT.setTzAudienceId(audId);
			psTzAudcyuanT.setTzAudcyId(audCyId);
			psTzAudcyuanT.setTzAudXm(name);
			psTzAudcyuanT.setTzAudCh(ch);
			psTzAudcyuanT.setTzZySj(mainPhone);
			psTzAudcyuanT.setTzCySj(cyPhone);
			psTzAudcyuanT.setTzZyEmail(mainEmail);
			psTzAudcyuanT.setTzCyEmail(cyEmail);
			psTzAudcyuanT.setTzWeixin(wxh);
			psTzAudcyuanT.setOprid(oprId);
			psTzAudcyuanT.setTzXsxsId(xsxxId);
			psTzAudcyuanT.setTzHuodId(hdId);
			psTzAudcyuanT.setTzBmbId(bmbId);
			int i = psTzAudcyuanTMapper.insert(psTzAudcyuanT);
			if (i > 0) {
				bl = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			bl = false;
		}

		return bl;
	}

	// 添加抄送EMAIL;
	public boolean addCCAddr(String taskId,String mailCCAddr) {
		boolean bl = false;
		try {
			PsTzMalCcAddT psTzMalCcAddT = new PsTzMalCcAddT();
			psTzMalCcAddT.setTzEmlSmsTaskId(taskId);
			psTzMalCcAddT.setTzMalCcAddr(mailCCAddr);
			int i = psTzMalCcAddTMapper.insert(psTzMalCcAddT);
			if (i > 0) {
				bl = true;
			}
		} catch (Exception e) {
			bl = false;
		}
		return bl;
	}

	// 添加密送EMAIL;
	public boolean addBCAddr(String taskId,String mailBCAddr) {
		boolean bl = false;
		try {
			PsTzMalBcAddT psTzMalBcAddT = new PsTzMalBcAddT();
			psTzMalBcAddT.setTzEmlSmsTaskId(taskId);
			psTzMalBcAddT.setTzMalBcAddr(mailBCAddr);
			int i = psTzMalBcAddTMapper.insert(psTzMalBcAddT);
			if (i > 0) {
				bl = true;
			}
		} catch (Exception e) {
			bl = false; 
		}
		return bl;
	}
	
	//更新任务中的听众ID;
	public void updateAudId(String taskId,String updateAudId){
		PsTzDxyjfsrwTbl psTzDxyjfsrwTbl = new PsTzDxyjfsrwTbl();
		psTzDxyjfsrwTbl.setTzEmlSmsTaskId(taskId);
		psTzDxyjfsrwTbl.setTzAudienceId(updateAudId);
		
		psTzDxyjfsrwTblMapper.updateByPrimaryKeySelective(psTzDxyjfsrwTbl);
	}
	
	//更新主题;
	public boolean updateEmailSendTitle(String taskId,String title){
		PsTzYjmbshliTbl psTzYjmbshliTbl = new PsTzYjmbshliTbl();
		psTzYjmbshliTbl.setTzEmlSmsTaskId(taskId);
		psTzYjmbshliTbl.setTzMalSubjuect(title);
		int i = psTzYjmbshliTblMapper.updateByPrimaryKeySelective(psTzYjmbshliTbl);
		if(i > 0 ){
			return true;
		}else{
			return false;
		}
	}
	
	//更新内容;
	public boolean updateEmailSendContent(String taskId,String content){
		PsTzYjmbshliTbl psTzYjmbshliTbl = new PsTzYjmbshliTbl();
		psTzYjmbshliTbl.setTzEmlSmsTaskId(taskId);
		psTzYjmbshliTbl.setTzMalContent(content);
		int i = psTzYjmbshliTblMapper.updateByPrimaryKeySelective(psTzYjmbshliTbl);
		if(i > 0 ){
			return true;
		}else{
			return false;
		}
	}
	
	//更新短信内容
	public boolean updateSmsSendContent(String taskId,String content){
	   PsTzDxmbshliTbl psTzDxmbshliTbl = new PsTzDxmbshliTbl();
	   psTzDxmbshliTbl.setTzEmlSmsTaskId(taskId);
	   psTzDxmbshliTbl.setTzSmsContent(content);
	   int i = psTzDxmbshliTblMapper.updateByPrimaryKeySelective(psTzDxmbshliTbl);
		if(i > 0 ){
			return true;
		}else{
			return false;
		}
	}
	
	//新增附件;
	public boolean addAttach(String taskId,String attachName,String attachUrl){
		PsTzRwFjianTbl psTzRwFjianTbl = new PsTzRwFjianTbl();
		String fjId = String.valueOf(getSeqNum.getSeqNum("TZ_RW_FJIAN_TBL", "TZ_FJIAN_ID"));
		psTzRwFjianTbl.setTzEmlSmsTaskId(taskId);
		psTzRwFjianTbl.setTzFjianId(fjId);
		psTzRwFjianTbl.setTzFjianLj(attachUrl);
		psTzRwFjianTbl.setTzFjianMc(attachName);
		int i = psTzRwFjianTblMapper.insert(psTzRwFjianTbl);
		if(i > 0 ){
			return true;
		}else{
			return false;
		}
	}
	
	//修改发件人;
	public boolean updateEmailSender(String taskId,String emailAddr, String emailAlias){
		PsTzDxyjfsrwTbl psTzDxyjfsrwTbl = psTzDxyjfsrwTblMapper.selectByPrimaryKey(taskId);
		if(psTzDxyjfsrwTbl != null){
			psTzDxyjfsrwTbl.setTzEmailSender(emailAddr);
			psTzDxyjfsrwTbl.setTzSenderAlias(emailAlias);
			int i = psTzDxyjfsrwTblMapper.updateByPrimaryKeySelective(psTzDxyjfsrwTbl);
			if(i > 0 ){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}
}
