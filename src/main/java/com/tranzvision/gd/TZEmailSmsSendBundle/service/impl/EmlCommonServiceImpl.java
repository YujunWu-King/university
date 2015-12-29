package com.tranzvision.gd.TZEmailSmsSendBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 邮件短信发送；原：TZ_GD_COM_EMLSMS_APP:emlCommon
 * 
 * @author tang
 * @since 2015-11-30
 */
@Service("com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.EmlCommonServiceImpl")
public class EmlCommonServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private CreateTaskServiceImpl createTaskServiceImpl;
	@Autowired
	private SendSmsOrMalServiceImpl sendSmsOrMalServiceImpl;
	
	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("formData", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			String jgId = jacksonUtil.getString("jgId");
			List<?> tmpNames = jacksonUtil.getList("tmpNames");
			Map<String, Object> tmpNamesMap = new HashMap<>();
			ArrayList<Map<String, Object>> listData = new ArrayList<>();
			tmpNamesMap.put("tmpNames", listData);

			if(tmpNames != null){
				for(int i=0; i < tmpNames.size(); i++){
					String tmpId = (String) tmpNames.get(i);
					String sql = "select TZ_TMPL_ID,TZ_TMPL_NAME from PS_TZ_EMALTMPL_TBL where TZ_JG_ID=? AND TZ_TMPL_ID=? AND TZ_USE_FLAG='Y'";
					Map<String, Object> map = jdbcTemplate.queryForMap(sql,new Object[]{jgId,tmpId});
					Map<String, Object> jsonMap = new HashMap<>();
					if(map != null){
						jsonMap.put("tmpId", map.get("TZ_TMPL_ID"));
						jsonMap.put("tmpName", map.get("TZ_TMPL_NAME"));
					}
					listData.add(jsonMap);
				}
				tmpNamesMap.replace("tmpNames", listData);
				mapRet.replace("formData", tmpNamesMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jacksonUtil.Map2json(mapRet);
	}
	
	
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRet = "";
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("formData", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			String jgId = jacksonUtil.getString("jgId");
			String tmpId = jacksonUtil.getString("tmpId");
			String audienceId = jacksonUtil.getString("audienceId");
			
			// 发件人;
			String senderEmail= "";
			// 邮件主题;
			String emailTheme = "";
			// 邮件内容;
			String emailContent = "";
			String emailSQL = "select A.TZ_EML_ADDR100,B.TZ_MAL_SUBJUECT,B.TZ_MAL_CONTENT from PS_TZ_EMLS_DEF_TBL A , PS_TZ_EMALTMPL_TBL B  WHERE B.TZ_JG_ID=? AND B.TZ_TMPL_ID=? AND B.TZ_EMLSERV_ID = A.TZ_EMLSERV_ID";
			Map<String, Object> emailMap = jdbcTemplate.queryForMap(emailSQL, new Object[]{jgId,tmpId});
			if(emailMap != null){
				senderEmail = (String) emailMap.get("TZ_EML_ADDR100");
				emailTheme = (String) emailMap.get("TZ_MAL_SUBJUECT");
				emailContent = (String) emailMap.get("TZ_MAL_CONTENT");
			}
			
			// 收件人;
			String addresseeEmail = "";
			String zyMail = "";
			String mainEmailSQL = "select TZ_ZY_EMAIL from PS_TZ_AUDCYUAN_T where TZ_AUDIENCE_ID=?";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(mainEmailSQL, new Object[]{audienceId});
			if(list != null){
				for(int i = 0; i < list.size(); i++){
					zyMail = (String) list.get(i).get("TZ_ZY_EMAIL");
					if(zyMail != null && !"".equals(zyMail)){
						addresseeEmail =  addresseeEmail + ";" + zyMail;
					}
				}
			}
			
			if(!"".equals(addresseeEmail)){
				addresseeEmail = addresseeEmail.substring(1);
			}
			
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put("senderEmail", senderEmail);
			jsonMap.put("AddresseeEmail", addresseeEmail);
			jsonMap.put("emailTheme", emailTheme);
			jsonMap.put("emailContent", emailContent);
			returnJsonMap.replace("formData", jsonMap);
			
			// 查看是否单个收件人，如果只有一个则直接解析邮件;
			/**********TODO*******/
		}catch(Exception e){
			e.printStackTrace();		
		}
		strRet = jacksonUtil.Map2json(returnJsonMap);
		return strRet;
	}
	
	/* 发送邮件 */
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
		Map<String , Object> map = new HashMap<>();
		map.put("success", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				jacksonUtil.json2Map(strForm);
				if(jacksonUtil.containsKey("jgId")
						&& jacksonUtil.containsKey("audienceId")
						&& jacksonUtil.containsKey("data")){
					// 机构ID;
				    String jgId = jacksonUtil.getString("jgId");
				    // 发送的听众Id;
				    String audienceId = jacksonUtil.getString("audienceId");
				    //发送信息;
				    Map<String , Object> dataMap = jacksonUtil.getMap("data");
				    if(dataMap != null){
				    	//模板ID;
				        String tmpId = (String) dataMap.get("emailTmp");
				        // 发送人;
				        //String senderEmail = (String) dataMap.get("senderEmail");
				        // 抄送;
				        String ccAddresseeEmail = (String) dataMap.get("ccAddresseeEmail");
				        // 密送
				        String bcAddresseeEmail = (String) dataMap.get("bcAddresseeEmail");
				        // 主题;
				        String emailTheme = (String) dataMap.get("emailTheme");
				        // 内容;
				        String emailContent = (String) dataMap.get("emailContent");
				        // 附件;
				        String fjLj = (String) dataMap.get("fjLj");
				        String fjMc = (String) dataMap.get("fjMc");
				        
				        boolean bl = createTaskServiceImpl.createTaskIns(jgId, tmpId, "MAL", "A");
				        if( bl == false ){
				        	map.replace("success", "创建邮件发送任务失败");
				        	strRet = jacksonUtil.Map2json(map);
				    		return strRet;
				        }

				        //更新任务关联的听众ID;
				        createTaskServiceImpl.updateAudId(audienceId);
				        //添加抄送;
				        if(ccAddresseeEmail != null && !"".equals(ccAddresseeEmail)){
				        	bl = createTaskServiceImpl.addCCAddr(ccAddresseeEmail);
				        }
				        if( bl == false ){
				        	map.replace("success", "添加抄送失败");
				        	strRet = jacksonUtil.Map2json(map);
				    		return strRet;
				        }
				        //添加密送;
				        if(bcAddresseeEmail != null && !"".equals(bcAddresseeEmail)){
				        	bl = createTaskServiceImpl.addBCAddr(bcAddresseeEmail);
				        }
				        if( bl == false ){
				        	map.replace("success", "添加抄送失败");
				        	strRet = jacksonUtil.Map2json(map);
				    		return strRet;
				        }
				        //更新主题;
				        bl = createTaskServiceImpl.updateEmailSendTitle(emailTheme);
				        if( bl == false ){
				        	map.replace("success", "更新邮件主题失败");
				        	strRet = jacksonUtil.Map2json(map);
				    		return strRet;
				        }
				        //更新内容;
				        bl = createTaskServiceImpl.updateEmailSendContent(emailContent);
				        if( bl == false ){
				        	map.replace("success", "更新邮件内容失败");
				        	strRet = jacksonUtil.Map2json(map);
				    		return strRet;
				        }
				        //写附件表;
				        if(fjLj != null && !"".equals(fjLj)
				        		&& fjMc != null && !"".equals(fjMc) ){
				        	bl = createTaskServiceImpl.addAttach(fjMc, fjLj);
				        	if( bl == false ){
					        	map.replace("success", "更新邮件附件失败");
					        	strRet = jacksonUtil.Map2json(map);
					    		return strRet;
					        }
				        }
				        // 任务ID;
				        String taskId = createTaskServiceImpl.getTaskId();
				        sendSmsOrMalServiceImpl.send(taskId, "");
				        
				    }else{
				    	map.replace("success", "参数有误");
				    }
				}else{
					map.replace("success", "参数有误");
				} 
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		strRet = jacksonUtil.Map2json(map);
		return strRet;
	}
	
}
