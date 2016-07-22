package com.tranzvision.gd.TZApplicationProgressBundle.service.impl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.PsShowPrjNewsTMapper;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsShowPrjNewsTKey;
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
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private PsShowPrjNewsTMapper psShowPrjNewsTMapper;
	
	@Override
	public String tzGetHtmlContent(String strParams) {
		String infoScheduleHtml = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		String str_jg_id = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		try {
			jacksonUtil.json2Map(strParams);
			String instanceIdStr = "";
			if(jacksonUtil.containsKey("instanceId")){
				instanceIdStr = jacksonUtil.getString("instanceId");
			}

			int instanceId = 0;
			if(!"".equals(instanceIdStr) && StringUtils.isNumeric(instanceIdStr)){
				instanceId = Integer.parseInt(instanceIdStr);
			}

			String classId = "";
			if(jacksonUtil.containsKey("bmClassId")){
				classId = jacksonUtil.getString("bmClassId");
			}
			
			String language = "";
			if(jacksonUtil.containsKey("language")){
				language = jacksonUtil.getString("language");
			}
			
			String viewType = "";
			if(jacksonUtil.containsKey("viewType")){
				viewType = jacksonUtil.getString("viewType");
			}
			
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
				}else if("CLASS".equals(viewType)){
					String selectMsg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_SITE_MESSAGE", "200", language, "选择", "选择");
					String classNameMsg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_SITE_MESSAGE", "201", language, "班级名称", "班级名称");
					String desMsg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_SITE_MESSAGE", "202", language, "说明", "说明");
					
					String sql = "SELECT TZ_CLASS_ID,TZ_CLASS_NAME,TZ_CLASS_DESC from  PS_TZ_CLASS_INF_T where TZ_JG_ID=? and TZ_IS_APP_OPEN='Y' and TZ_APP_START_DT IS NOT NULL AND TZ_APP_START_TM IS NOT NULL AND TZ_APP_END_DT IS NOT NULL AND TZ_APP_END_TM IS NOT NULL AND str_to_date(concat(DATE_FORMAT(TZ_APP_START_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(TZ_APP_START_TM,'%H:%i'),':00'),'%Y/%m/%d %H:%i:%s') <= now() AND str_to_date(concat(DATE_FORMAT(TZ_APP_END_DT,'%Y/%m/%d'),' ',  DATE_FORMAT(TZ_APP_END_TM,'%H:%i'),':59'),'%Y/%m/%d %H:%i:%s') >= now() ORDER BY TZ_APP_START_DT,TZ_APP_END_DT ASC";
					List<Map<String, Object>> classList = jdbcTemplate.queryForList(sql, new Object[] { str_jg_id });
					if (classList != null && classList.size() > 0) {
						for (int i = 0; i < classList.size(); i++) {
							classId = (String) classList.get(i).get("TZ_CLASS_ID");
							String className = (String) classList.get(i).get("TZ_CLASS_NAME");
							String classDesc = (String) classList.get(i).get("TZ_CLASS_DESC");
							if(className == null){
								className = "";
							}
							if(classDesc == null){
								classDesc = "";
							}
							
							TZ_GD_ZLSQ_TR = TZ_GD_ZLSQ_TR + tzGDObject.getHTMLText(
	        						"HTML.TZApplicationProgressBundle.TZ_OPEN_WINDOW_TR3",classId, className, classDesc);
						}
					}
					//新增申请班级;
					TZ_OPEN_WINDOW_TABLE_TH = tzGDObject.getHTMLText(
							"HTML.TZApplicationProgressBundle.TZ_OPEN_WINDOW_TABLE_TH2",selectMsg, classNameMsg, desMsg);
				}else if("SELECTPROJECT".equals(viewType)){
					String siteid = "";
					if(jacksonUtil.containsKey("siteid")){
						siteid = jacksonUtil.getString("siteid");
					}
					language = jdbcTemplate.queryForObject("select TZ_SITE_LANG from PS_TZ_SITEI_DEFN_T where TZ_SITEI_ID = ?", new Object[]{siteid},"String");
					String projMsg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_SITE_MESSAGE", "206", language, "选择查看新闻及活动的项目范围", "选择查看新闻及活动的项目范围");
					
					String sql = "SELECT TZ_PRJ_ID,TZ_PRJ_NAME FROM PS_TZ_PRJ_INF_T where TZ_IS_OPEN='Y' AND TZ_JG_ID=?";
					List<Map<String, Object>> classList = jdbcTemplate.queryForList(sql, new Object[] { str_jg_id });
					if (classList != null && classList.size() > 0) {
						for (int i = 0; i < classList.size(); i++) {
							String prjId = (String) classList.get(i).get("TZ_PRJ_ID");
							String prjName = (String) classList.get(i).get("TZ_PRJ_NAME");
							
							if(prjName == null){
								prjName = "";
							}

							//查询是否已经选择;
							int isPrjSelect = jdbcTemplate.queryForObject("SELECT count(1) FROM PS_SHOW_PRJ_NEWS_T where OPRID=? and TZ_PRJ_ID=?", new Object[]{oprid,prjId},"Integer");
							String checked = "";
							if(isPrjSelect > 0){
								checked = "checked=\"checked\"";
							}
							TZ_GD_ZLSQ_TR = TZ_GD_ZLSQ_TR + tzGDObject.getHTMLText(
	        						"HTML.TZApplicationProgressBundle.TZ_OPEN_WINDOW_TR4",prjId,prjName,checked);
						}
					}
					//新增申请班级;
					TZ_OPEN_WINDOW_TABLE_TH = tzGDObject.getHTMLText(
							"HTML.TZApplicationProgressBundle.TZ_OPEN_WINDOW_TABLE_TH3", projMsg);
				}else if("ADDSELECTPROJECT".equals(viewType)){
					String selectPrjs = "";
					if(jacksonUtil.containsKey("selectPrjs")){
						selectPrjs = jacksonUtil.getString("selectPrjs");
						if("".equals(selectPrjs)){
							jdbcTemplate.update("delete from PS_SHOW_PRJ_NEWS_T where OPRID=?",new Object[]{oprid});
						}else{
							jdbcTemplate.update("delete from PS_SHOW_PRJ_NEWS_T where OPRID=?",new Object[]{oprid});
							String[] selectPrjsArr = selectPrjs.split(";");
							int kk = 0;
							for(kk =0; kk < selectPrjsArr.length; kk++ ){
								String tzPrjId = selectPrjsArr[kk];
								if(tzPrjId != null && !"".equals(tzPrjId)){
									PsShowPrjNewsTKey psShowPrjNewsT = new PsShowPrjNewsTKey();
									psShowPrjNewsT.setOprid(oprid);
									psShowPrjNewsT.setTzPrjId(tzPrjId);
									psShowPrjNewsTMapper.insert(psShowPrjNewsT);
								}
							}
						}
						return "SUCCESS";
					}
					
				}
			}
			
			if(TZ_GD_ZLSQ_TR.contains("\\")){
				TZ_GD_ZLSQ_TR = TZ_GD_ZLSQ_TR.replace("\\", "\\\\");
			}
			if(TZ_GD_ZLSQ_TR.contains("$")){
				TZ_GD_ZLSQ_TR = TZ_GD_ZLSQ_TR.replace("$", "\\$");
			}
			if("CLASS".equals(viewType)){
				String addMsg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_SITE_MESSAGE", "203", language, "确定", "确定");
				String cancleMsg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_SITE_MESSAGE", "204", language, "取消", "取消");
				String classTableMsg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_SITE_MESSAGE", "205", language, "选择申请班级", "选择申请班级");
				String noSelect = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_SITE_MESSAGE", "209", language, "请选择要报名的班级", "请选择要报名的班级");
				
				infoScheduleHtml =  tzGDObject.getHTMLText(
						"HTML.TZApplicationProgressBundle.TZ_GD_MATERIAL_SQJD_HTML2",TZ_OPEN_WINDOW_TABLE_TH + TZ_GD_ZLSQ_TR,addMsg,cancleMsg,classTableMsg,noSelect,language);
			}else if("SELECTPROJECT".equals(viewType)){
				String addMsg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_SITE_MESSAGE", "203", language, "确定", "确定");
				String cancleMsg = messageTextServiceImpl.getMessageTextWithLanguageCd("TZ_SITE_MESSAGE", "204", language, "取消", "取消");
				infoScheduleHtml =  tzGDObject.getHTMLText(
						"HTML.TZApplicationProgressBundle.TZ_GD_MATERIAL_SQJD_HTML3",TZ_OPEN_WINDOW_TABLE_TH + TZ_GD_ZLSQ_TR,addMsg,cancleMsg,"");
			}else{
				infoScheduleHtml =  tzGDObject.getHTMLText(
						"HTML.TZApplicationProgressBundle.TZ_GD_MATERIAL_SQJD_HTML",TZ_OPEN_WINDOW_TABLE_TH + TZ_GD_ZLSQ_TR);
			}
			
		}catch(Exception e){
			e.printStackTrace();
			infoScheduleHtml = "无法获取数据";
		}
		return infoScheduleHtml;
	}
}
