package com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.CreateTaskServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 原PS类：TZ_GD_BMGL_BMBSH_PKG:TZ_GD_BMGL_YJDX_CLS
 * @author tang
 * 报名管理-报名表审核邮件短信发送相关类
 */
@Service("com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl.TzGdBmglYjdxClsServiceImpl")
public class TzGdBmglYjdxClsServiceImpl extends FrameworkImpl {
	@Autowired
	private CreateTaskServiceImpl createTaskServiceImpl;
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private TZGDObject tzGdObject;
	@Autowired
	private GetSeqNum getSeqNum;
	
	/* 添加听众 */
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		// 返回值;
		String audID= "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		if (actData.length == 0) {
			return audID;
		}
		String str_jg_id = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		try {
			for (int num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				String strType = jacksonUtil.getString("type");
				
				
				boolean bMultiType = false;
			      
				if("DJZL".equals(strType)){
					audID = createTaskServiceImpl.createAudience("",str_jg_id,"报名表递交资料审核", "JSRW");
				}
				
				if("TJX".equals(strType)){
					audID = createTaskServiceImpl.createAudience("",str_jg_id,"推荐信未完全提交提醒", "JSRW");
				}

				if("MULTI".equals(strType)){
					audID = createTaskServiceImpl.createAudience("",str_jg_id,"报名表审核批量发送邮件", "JSRW");
					bMultiType = true;
				}
				
				String sOprID = "";
				
				if(bMultiType){
					long sAppInsID;
					//群发邮件添加听众;
					@SuppressWarnings("unchecked")
					List<Map<String, Object>> list = (List<Map<String, Object>>) jacksonUtil.getList("personList");
					if(list != null && list.size() > 0){
						int audCyId = getSeqNum.getSeqNum("TZ_AUDCYUAN_T", "TZ_AUDCY_ID",list.size(),0)-list.size();
						for(int num_1 = 0; num_1 < list.size(); num_1 ++){
							Map<String, Object> map = list.get(num_1);
				            sOprID = (String)map.get("oprID");
				            sAppInsID = Long.parseLong((String)map.get("appInsID"));
//				            if(sOprID != null && !"".equals(sOprID)
//				            		&& sAppInsID != 0){
				            if(sOprID != null && !"".equals(sOprID)){
				            	/*为听众添加成员:姓名，称谓，报名人联系方式*/
				                String strName = jdbcTemplate.queryForObject("SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=? limit 0,1",new Object[]{sOprID},"String");
				 
				                String mainMobilePhone = "", backupMobilePhone = "", mainEmail = "", backupEmail="",  wechat="";
				                Map<String, Object> lxfsMap = jdbcTemplate.queryForMap("SELECT TZ_ZY_SJ,TZ_CY_SJ,TZ_ZY_DH,TZ_CY_DH,TZ_ZY_EMAIL,TZ_CY_EMAIL,TZ_ZY_TXDZ,TZ_CY_TXDZ,TZ_WEIXIN,TZ_SKYPE FROM PS_TZ_LXFSINFO_TBL WHERE TZ_LXFS_LY='ZCYH' AND TZ_LYDX_ID=?", new Object[]{sOprID});
				                if(lxfsMap != null){
				                	mainMobilePhone = (String)lxfsMap.get("TZ_ZY_SJ");
				                	backupMobilePhone = (String)lxfsMap.get("TZ_CY_SJ");
				                	//mainPhone = (String)lxfsMap.get("TZ_ZY_DH");
				                	//backupPhone = (String)lxfsMap.get("TZ_CY_DH");
				                	mainEmail = (String)lxfsMap.get("TZ_ZY_EMAIL");
				                	backupEmail = (String)lxfsMap.get("TZ_CY_EMAIL");
				                	//mainAddress = (String)lxfsMap.get("TZ_ZY_TXDZ");
				                	//backupAddress = (String)lxfsMap.get("TZ_CY_TXDZ");
				                	wechat = (String)lxfsMap.get("TZ_WEIXIN");
				                	//skype = (String)lxfsMap.get("TZ_SKYPE");
				                }
				               
				                createTaskServiceImpl.addAudCy2(audID,strName, "", mainMobilePhone, backupMobilePhone, mainEmail, backupEmail, wechat, sOprID, "", "", String.valueOf(sAppInsID),audCyId);
				                audCyId++;
				            }
				            
						}
					}
			        
				}else{
					//单发邮件添加听众;
					String sAppInsID = "";
			        sOprID = jacksonUtil.getString("oprID");
			        sAppInsID = jacksonUtil.getString("appInsID");
			        /*为听众添加成员:姓名，称谓，报名人联系方式*/
		            String strName = jdbcTemplate.queryForObject("SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=? limit 0,1",new Object[]{sOprID},"String");
		 
		            String mainMobilePhone = "", backupMobilePhone = "", mainEmail = "", backupEmail="",  wechat="";
		            Map<String, Object> lxfsMap = jdbcTemplate.queryForMap("SELECT TZ_ZY_SJ,TZ_CY_SJ,TZ_ZY_DH,TZ_CY_DH,TZ_ZY_EMAIL,TZ_CY_EMAIL,TZ_ZY_TXDZ,TZ_CY_TXDZ,TZ_WEIXIN,TZ_SKYPE FROM PS_TZ_LXFSINFO_TBL WHERE TZ_LXFS_LY='ZCYH' AND TZ_LYDX_ID=?", new Object[]{sOprID});
		            if(lxfsMap != null){
		               mainMobilePhone = (String)lxfsMap.get("TZ_ZY_SJ");
		               backupMobilePhone = (String)lxfsMap.get("TZ_CY_SJ");
		               //mainPhone = (String)lxfsMap.get("TZ_ZY_DH");
		               //backupPhone = (String)lxfsMap.get("TZ_CY_DH");
		               mainEmail = (String)lxfsMap.get("TZ_ZY_EMAIL");
		               backupEmail = (String)lxfsMap.get("TZ_CY_EMAIL");
		               //mainAddress = (String)lxfsMap.get("TZ_ZY_TXDZ");
		               //backupAddress = (String)lxfsMap.get("TZ_CY_TXDZ");
		               wechat = (String)lxfsMap.get("TZ_WEIXIN");
		               //skype = (String)lxfsMap.get("TZ_SKYPE");
		             }
		             createTaskServiceImpl.addAudCy(audID,strName, "", mainMobilePhone, backupMobilePhone, mainEmail, backupEmail, wechat, sOprID, "", "", sAppInsID);

				}
			}
		}catch(Exception e){
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return audID;
	}
	
	// 获得递交资料料内容;
	/*生成发送内容方法*/
	public String getDjzlContent(String audId, String audCyId){
		
		String strDjzlList = "";
		try{
			String sAppInsID = jdbcTemplate.queryForObject("SELECT TZ_BMB_ID FROM PS_TZ_AUDCYUAN_T WHERE TZ_AUDIENCE_ID=? AND TZ_AUDCY_ID=?", new Object[]{audId, audCyId},"String");
			String strClassID = jdbcTemplate.queryForObject("SELECT TZ_CLASS_ID FROM PS_TZ_FORM_WRK_T WHERE TZ_APP_INS_ID=?",new Object[]{Long.valueOf(sAppInsID)},"String");
			String strLanguageId = tzLoginServiceImpl.getSysLanaguageCD(request);
			if("ENG".equals(strLanguageId)){
				strLanguageId = "ENG";
			}else{
				strLanguageId = "ZHS";
			}
			
			String sqlContent = "SELECT TZ_SBMINF_ID,TZ_CONT_INTRO,TZ_REMARK FROM PS_TZ_CLS_DJZL_T WHERE TZ_CLASS_ID=? ORDER BY TZ_SORT_NUM ";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlContent,new Object[]{strClassID});
			if(list != null && list.size() > 0){
				for(int i = 0; i < list.size(); i++){
					String strFileID = (String)list.get(i).get("TZ_SBMINF_ID");
					String strContentIntro = (String)list.get(i).get("TZ_CONT_INTRO");
					//String strRemark = (String)list.get(i).get("TZ_REMARK");
					
				    String strAuditState = "", strAuditStateDesc = "", strFailedReason = "";
				    Map<String, Object> map = jdbcTemplate.queryForMap("SELECT TZ_ZL_AUDIT_STATUS,TZ_AUDIT_NOPASS_RS FROM PS_TZ_FORM_ZLSH_T A WHERE TZ_APP_INS_ID=? AND TZ_SBMINF_ID=?",new Object[]{sAppInsID, strFileID});
				    if(map != null){
				    	strAuditState = (String)map.get("TZ_ZL_AUDIT_STATUS");
				    	strFailedReason = (String)map.get("TZ_AUDIT_NOPASS_RS");
				    	
				    	strAuditStateDesc = jdbcTemplate.queryForObject("select if(B.TZ_ZHZ_CMS IS NULL ,A.TZ_ZHZ_CMS,B.TZ_ZHZ_CMS) TZ_ZHZ_CMS FROM PS_TZ_PT_ZHZXX_TBL A LEFT JOIN (select * from PS_TZ_PT_ZHZXX_LNG where TZ_LANGUAGE_ID=?) B ON A.TZ_ZHZJH_ID=B.TZ_ZHZJH_ID AND A.TZ_ZHZ_ID = B.TZ_ZHZ_ID WHERE A.TZ_EFF_STATUS='A' AND A.TZ_ZHZJH_ID = 'TZ_BMB_DJWJSPZT' AND A.TZ_ZHZ_ID = ?", new Object[]{strLanguageId,strAuditState,},"String");
				    	
				    	strDjzlList = strDjzlList + "<tr>" + tzGdObject.getHTMLText(
								"HTML.TZApplicationVerifiedBundle.TZ_GD_DJZL_TR_TD_HTML",strContentIntro) 
				    			+ tzGdObject.getHTMLText(
				    					"HTML.TZApplicationVerifiedBundle.TZ_GD_DJZL_TR_TD_HTML",strAuditStateDesc)
				    			+ tzGdObject.getHTMLText(
										"HTML.TZApplicationVerifiedBundle.TZ_GD_DJZL_TR_TD_HTML",strFailedReason) + "</tr>";
				    
				    }
				}
			}
			
			return tzGdObject.getHTMLText(
					"HTML.TZApplicationVerifiedBundle.TZ_GD_DJZL_CONTENT_HTML",strDjzlList);
		}catch(Exception e){
			return e.toString();
		}
		
	}
}
