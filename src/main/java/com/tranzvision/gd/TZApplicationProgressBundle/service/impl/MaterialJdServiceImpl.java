package com.tranzvision.gd.TZApplicationProgressBundle.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.MessageTextServiceImpl;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * PS: TZ_GD_BJXZQ_APP:ApplicationCenter
 * @author tang
 * 资料申请进度
 */
@Service("com.tranzvision.gd.TZApplicationProgressBundle.service.impl.MaterialJdServiceImpl")
public class MaterialJdServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private MessageTextServiceImpl messageTextServiceImpl;
	@Autowired
	private TZGDObject tzGDObject;
	
	@Override
	public String tzGetHtmlContent(String strParams) {
		String infoScheduleHtml = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			jacksonUtil.json2Map(strParams);
			String instanceIdStr = jacksonUtil.getString("instanceId");
			
			int instanceId = 0;
			if(StringUtils.isNumeric(instanceIdStr)){
				instanceId = Integer.parseInt(instanceIdStr);
			}

			String classId = jacksonUtil.getString("bmClassId");
			String language = jacksonUtil.getString("language");
			String viewType = jacksonUtil.getString("viewType");
			if(language == null || "".equals(language)){
				language = "ZHS";
			}
			
			String noSQJD = "N/A";
			String TZ_OPEN_WINDOW_TABLE_TH = "";
			String TZ_GD_ZLSQ_TR = "";
			
			if("TJX".equals(viewType)){
				
				// 推荐人;
				String tjr = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_SITE_MESSAGE", "114", language, "推荐人", "Referee");
			    // 发送状态;
				String fszt = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_SITE_MESSAGE", "115", language, "发送状态", "Email Status");
			    // 推荐信提交状态;
				String tjxtjzt = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_SITE_MESSAGE", "116", language, "推荐信提交状态", "Refes Status");
			    // 已经提交;
				String tjsSubmit = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_SITE_MESSAGE", "117", language, "已提交", "Submitted");
			    // 未提交;
				String tjsUSubmit = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_SITE_MESSAGE", "118", language, "未提交", "Unsubmitted");
			    // 发送;
				String send = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_SITE_MESSAGE", "121", language, "发送", "Send");
			    // 未发送;
				//String unsend = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_SITE_MESSAGE", "120", language, "未发送", "Unsent");
			      
			    // 推荐人已在线填写;
				String fillOnline = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_SITE_MESSAGE", "125", language, "推荐人已在线填写", "Filling in online");
			      
			    // 推荐人上传附件;
				String uploadTjx = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_SITE_MESSAGE", "126", language, "手工上传推荐信附件", "Upload");
				
				// 报名人填写的推荐人数;
			    int totalTjxNum = jdbcTemplate.queryForObject("select COUNT(1) from PS_TZ_KS_TJX_TBL where TZ_APP_INS_ID=? and TZ_MBA_TJX_YX='Y'", new Object[]{instanceId},"Integer");
			    if(totalTjxNum > 0){
			    	 String tjrxx = "";
			         long tjxInstId = 0L;
			         String TZ_REF_LETTER_ID = "";
			         String TZ_REFLETTERTYPE = "";
			         String tjxSql = "select TZ_REFERRER_NAME,TZ_TJX_APP_INS_ID,TZ_REF_LETTER_ID,TZ_REFLETTERTYPE from PS_TZ_KS_TJX_TBL where TZ_APP_INS_ID=? and TZ_MBA_TJX_YX='Y' order by TZ_TJR_ID asc";
			         List<Map<String, Object>> tjxList = jdbcTemplate.queryForList(tjxSql,new Object[]{instanceId});
			         if(tjxList != null && tjxList.size() > 0){
			        	for(int i = 0 ; i < tjxList.size(); i++){
			        		tjrxx = (String)tjxList.get(i).get("TZ_REFERRER_NAME")==null?"":(String)tjxList.get(i).get("TZ_REFERRER_NAME");
			        		try{
			        			tjxInstId = Long.parseLong(tjxList.get(i).get("TZ_TJX_APP_INS_ID").toString());
			        		}catch(Exception e){
			        			tjxInstId = 0L;
			        		}
			        		TZ_REF_LETTER_ID = (String)tjxList.get(i).get("TZ_REF_LETTER_ID")==null?"":(String)tjxList.get(i).get("TZ_REF_LETTER_ID"); 
			        		TZ_REFLETTERTYPE = (String)tjxList.get(i).get("TZ_REFLETTERTYPE")==null?"":(String)tjxList.get(i).get("TZ_REFLETTERTYPE"); 

			        		// 是否提交;
			                int isTj = jdbcTemplate.queryForObject("SELECT count(1) FROM PS_TZ_KS_TJX_TBL A WHERE ((A.ATTACHSYSFILENAME <> ' ' AND A.ATTACHUSERFILE <> ' ') OR  EXISTS (SELECT 'Y' FROM PS_TZ_APP_INS_T B WHERE A.TZ_TJX_APP_INS_ID = B.TZ_APP_INS_ID AND B.TZ_APP_FORM_STA = 'U' AND A.TZ_TJX_APP_INS_ID > 0)) AND A.TZ_APP_INS_ID = ? and A.TZ_REF_LETTER_ID=? and A.TZ_MBA_TJX_YX='Y'", new Object[]{instanceId, TZ_REF_LETTER_ID},"Integer");
			                if(isTj > 0){
			                	if("S".equals(TZ_REFLETTERTYPE)){
			                		/*已发送推荐信邮件，并完成了推荐信*/
			                		TZ_GD_ZLSQ_TR = TZ_GD_ZLSQ_TR + tzGDObject.getHTMLText(
			        						"HTML.TZApplicationProgressBundle.TZ_OPEN_WINDOW_TR2", tjrxx, fillOnline, tjsSubmit, "icon-yes");
			                	}else{
			                		TZ_GD_ZLSQ_TR = TZ_GD_ZLSQ_TR + tzGDObject.getHTMLText(
			        						"HTML.TZApplicationProgressBundle.TZ_OPEN_WINDOW_TR2", tjrxx, uploadTjx, tjsSubmit, "icon-yes");
			                	}
			                }else{
			                	if("S".equals(TZ_REFLETTERTYPE)){
			                		if(tjxInstId > 0){
			                			TZ_GD_ZLSQ_TR = TZ_GD_ZLSQ_TR + tzGDObject.getHTMLText(
				        						"HTML.TZApplicationProgressBundle.TZ_OPEN_WINDOW_TR2",tjrxx, fillOnline, tjsUSubmit, "icon-no");
			                		}else{
			                			TZ_GD_ZLSQ_TR = TZ_GD_ZLSQ_TR + tzGDObject.getHTMLText(
				        						"HTML.TZApplicationProgressBundle.TZ_OPEN_WINDOW_TR2",tjrxx, send, tjsUSubmit, "icon-no");
			                		}
			                	}else{
			                		TZ_GD_ZLSQ_TR = TZ_GD_ZLSQ_TR + tzGDObject.getHTMLText(
			        						"HTML.TZApplicationProgressBundle.TZ_OPEN_WINDOW_TR2",tjrxx, uploadTjx, tjsUSubmit, "icon-no");
			                	}
			                }
			        	}
			         }
			    }
			    TZ_OPEN_WINDOW_TABLE_TH = tzGDObject.getHTMLText(
						"HTML.TZApplicationProgressBundle.TZ_OPEN_WINDOW_TABLE_TH",tjr, fszt, tjxtjzt);
			}else{
				if("DJZL".equals(viewType)){
					//资料递交;
				    String zldj = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_SITE_MESSAGE", "111", language, "资料递交", "Materials Submitted");
				    // 审核结果;
				    String shjg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_SITE_MESSAGE", "112", language, "审核结果", "Review Result");
				    // 不通过原因;
				    String btgYy = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_SITE_MESSAGE", "113", language, "不通过原因", "Reason why not accepted");
				
				    //int TZ_SORT_NUM = 0;
			        String TZ_CONT_INTRO = "", TZ_ZL_AUDIT_STATUS = "", TZ_AUDIT_NOPASS_RS = "";
			        if(instanceId > 0 && classId != null && !"".equals(classId)){
			        	int total = 0;
			        	String sql = "select a.TZ_SORT_NUM,a.TZ_CONT_INTRO,b.TZ_ZL_AUDIT_STATUS,b.TZ_AUDIT_NOPASS_RS from PS_TZ_CLS_DJZL_T a LEFT JOIN (select * from PS_TZ_FORM_ZLSH_T where TZ_APP_INS_ID=?) b ON a.TZ_SBMINF_ID=b.TZ_SBMINF_ID WHERE  a.TZ_CLASS_ID = ? order by a.TZ_SORT_NUM asc";
			        	List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[]{instanceId, classId});
			        	if(list != null  && list.size() > 0){
			        		for(int i = 0; i < list.size();i++){
			        			//TZ_SORT_NUM =  (int)list.get(i).get("TZ_SORT_NUM");
			        			TZ_CONT_INTRO =  (String)list.get(i).get("TZ_CONT_INTRO") == null ? "":(String)list.get(i).get("TZ_CONT_INTRO");
			        			TZ_ZL_AUDIT_STATUS =  (String)list.get(i).get("TZ_ZL_AUDIT_STATUS")== null ? "":(String)list.get(i).get("TZ_ZL_AUDIT_STATUS");
			        			TZ_AUDIT_NOPASS_RS =  (String)list.get(i).get("TZ_AUDIT_NOPASS_RS")== null ? "":(String)list.get(i).get("TZ_AUDIT_NOPASS_RS");
			        			
			        			total = total + 1;
			        			
			        			//String result = "";
			        			if(TZ_ZL_AUDIT_STATUS == null || "".equals(TZ_ZL_AUDIT_STATUS)){
			        				TZ_ZL_AUDIT_STATUS = "A";
			        			}
			        			
			        			// 查找审核状态描述值;
			        			String zlSpZtDesc = "";
			        			if("ZHS".equals(language)){
			        				zlSpZtDesc = jdbcTemplate.queryForObject("SELECT TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID='TZ_BMB_DJWJSPZT' AND TZ_ZHZ_ID=?", new Object[]{TZ_ZL_AUDIT_STATUS},"String");
			        			}else{
			        				zlSpZtDesc = jdbcTemplate.queryForObject("SELECT COALESCE(B.TZ_ZHZ_DMS,A.TZ_ZHZ_DMS) TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL A LEFT JOIN (SELECT * FROM PS_TZ_PT_ZHZXX_LNG WHERE TZ_LANGUAGE_ID=?) B ON  A.TZ_ZHZJH_ID=B.TZ_ZHZJH_ID AND A.TZ_ZHZ_ID=B.TZ_ZHZ_ID WHERE A.TZ_ZHZJH_ID='TZ_BMB_DJWJSPZT' AND A.TZ_ZHZ_ID=?", new Object[]{language,TZ_ZL_AUDIT_STATUS},"String");
			        			}
			        			
			        			String class_css = "";
			        			if("A".equals(TZ_ZL_AUDIT_STATUS)){
			        				class_css = "icon-clock";
			        			}
			        			
			        			if("B".equals(TZ_ZL_AUDIT_STATUS)){
			        				class_css = "icon-yes";
			        			}
			        			
			        			if("C".equals(TZ_ZL_AUDIT_STATUS)){
			        				class_css = "icon-no";
			        			}
			        			
			        			if(TZ_CONT_INTRO.contains("\\")){
			        				TZ_CONT_INTRO = TZ_CONT_INTRO.replace("\\", "\\\\");
								}
								if(TZ_CONT_INTRO.contains("$")){
									TZ_CONT_INTRO = TZ_CONT_INTRO.replace("$", "\\$");
								}
								
								if(TZ_AUDIT_NOPASS_RS.contains("\\")){
									TZ_AUDIT_NOPASS_RS = TZ_AUDIT_NOPASS_RS.replace("\\", "\\\\");
								}
								if(TZ_AUDIT_NOPASS_RS.contains("$")){
									TZ_AUDIT_NOPASS_RS = TZ_AUDIT_NOPASS_RS.replace("$", "\\$");
								}
			        			
			        			TZ_GD_ZLSQ_TR = TZ_GD_ZLSQ_TR + tzGDObject.getHTMLText(
		        						"HTML.TZApplicationProgressBundle.TZ_OPEN_WINDOW_TR", TZ_CONT_INTRO, zlSpZtDesc, TZ_AUDIT_NOPASS_RS, class_css);
			                    
			        		}
			        	}
			        	
			        	if(total == 0){
			        		TZ_GD_ZLSQ_TR = TZ_GD_ZLSQ_TR + tzGDObject.getHTMLText(
	        						"HTML.TZApplicationProgressBundle.TZ_OPEN_WINDOW_TR", noSQJD, noSQJD, noSQJD, "");
			        	}
			        }else{
			        	TZ_GD_ZLSQ_TR = TZ_GD_ZLSQ_TR + tzGDObject.getHTMLText(
        						"HTML.TZApplicationProgressBundle.TZ_OPEN_WINDOW_TR",noSQJD, noSQJD, noSQJD, "");
			        }
			        
			        TZ_OPEN_WINDOW_TABLE_TH =  tzGDObject.getHTMLText(
    						"HTML.TZApplicationProgressBundle.TZ_OPEN_WINDOW_TABLE_TH", zldj, shjg, btgYy);
				}
			}
			
			if(TZ_GD_ZLSQ_TR.contains("\\")){
				TZ_GD_ZLSQ_TR = TZ_GD_ZLSQ_TR.replace("\\", "\\\\");
			}
			if(TZ_GD_ZLSQ_TR.contains("$")){
				TZ_GD_ZLSQ_TR = TZ_GD_ZLSQ_TR.replace("$", "\\$");
			}
			infoScheduleHtml =  tzGDObject.getHTMLText(
					"HTML.TZApplicationProgressBundle.TZ_GD_MATERIAL_SQJD_HTML",TZ_OPEN_WINDOW_TABLE_TH + TZ_GD_ZLSQ_TR);
		}catch(Exception e){
			e.printStackTrace();
			infoScheduleHtml = "无法获取数据";
		}
		return infoScheduleHtml;
	}
}
