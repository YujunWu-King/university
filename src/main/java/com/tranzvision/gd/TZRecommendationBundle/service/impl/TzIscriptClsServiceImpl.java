package com.tranzvision.gd.TZRecommendationBundle.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzKsTjxTblMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzKsTjxTbl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.MessageTextServiceImpl;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * PS:TZ_GD_TJX_PKG:TZ_ISCRIPT_CLS
 * @author tang
 *
 */
@Service("com.tranzvision.gd.TZRecommendationBundle.service.impl.TzIscriptClsServiceImpl")
public class TzIscriptClsServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private MessageTextServiceImpl messageTextServiceImpl;
	@Autowired
	private TzTjxClsServiceImpl tzTjxClsServiceImpl;
	@Autowired
	private PsTzKsTjxTblMapper psTzKsTjxTblMapper;
	@Autowired
	private TZGDObject tzGdObject;

	/* 推荐信接口  	*/
	@Override
	public String tzOther(String operateType, String strParams, String[] errorMsg) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		String mess = "";
		try{
			jacksonUtil.json2Map(strParams);
			String strTjxType="";
			String str_app_ins_version = "";
			String strTjrId = "", strEmail= "";
			String strTitle = "", strGname = "", strName = "", strCompany = "", strPosition = "", strPhone_area = "", strPhone_no = "", strGender = "", strAdd1 = "", strAdd2 = "", strAdd3 = "", strAdd4 = "", strAdd5 = "", strTjrgx = "", str_sysfilename = "", str_filename = "",accessPath="",tzAttAUrl="";
			String strTzsqrFlg = "";
			String str_tjx_valid = "";
			// 报名表ID;
			long numAppinsId = Long.parseLong(jacksonUtil.getString("rec_app_ins_id"));
			if( !"DELETE".equals(operateType)){
				//推荐人编号;
			    strTjrId = jacksonUtil.getString("rec_num");
			      
			    strEmail = jacksonUtil.getString("rec_email");
			      
			    strTjxType = jacksonUtil.getString("rec_language");
			    strTitle = jacksonUtil.getString("rec_title");
			    strGname = jacksonUtil.getString("rec_gname");
			    strName = jacksonUtil.getString("rec_name");
			    strCompany = jacksonUtil.getString("rec_company");
			    strPosition = jacksonUtil.getString("rec_post");
			    strPhone_area = jacksonUtil.getString("rec_phone_area");
			    strPhone_no = jacksonUtil.getString("rec_phone_no");
			    strGender = jacksonUtil.getString("rec_sex");
			      
			    
			      
			    strAdd1 = "";
			    strAdd2 = "";
			    strAdd3 = "";
			    strAdd4 = "";
			    strAdd5 = "";
			      
			    /**/
			    String str_tpl_lang = jdbcTemplate.queryForObject("SELECT B.TZ_APP_TPL_LAN FROM PS_TZ_APP_INS_T A,PS_TZ_APPTPL_DY_T B WHERE A.TZ_APP_INS_ID = ? AND A.TZ_APP_TPL_ID = B.TZ_APP_TPL_ID", new Object[]{numAppinsId},"String");
			    String str_value_s = "" , str_value_l = "";
			    Map<String, Object> zhzMap = jdbcTemplate.queryForMap("SELECT TZ_ZHZ_DMS,TZ_ZHZ_CMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID = 'TZ_APP_REF_TITLE' AND TZ_ZHZ_ID = ?",new Object[]{strTitle});
			    if(zhzMap != null){
			    	str_value_s = (String)zhzMap.get("TZ_ZHZ_DMS");
			    	str_value_l = (String)zhzMap.get("TZ_ZHZ_CMS");
			    	if("ENG".equals(str_tpl_lang)){
			    		strTitle = str_value_l;
			    		if(strTjxType == null || "".equals(strTjxType)){
			    			strTjxType = "E";
			    		}
			    	}else{
			    		strTitle = str_value_s;
			    	}
			    }
			    if(jacksonUtil.containsKey("tjx_valid")){
			    	str_tjx_valid = jacksonUtil.getString("tjx_valid");
			    }else{
			    	str_tjx_valid = "Y";
			    }
			    
			    if(jacksonUtil.containsKey("rec_by1")){
			    	strAdd1 = jacksonUtil.getString("rec_by1");
			    }else{
			    	strAdd1 = "";
			    }
			    
			    if(jacksonUtil.containsKey("rec_by2")){
			    	strAdd2 = jacksonUtil.getString("rec_by2");
			    }else{
			    	strAdd2 = "";
			    }
			    
			    if(jacksonUtil.containsKey("rec_by3")){
			    	strAdd3 = jacksonUtil.getString("rec_by3");
			    }else{
			    	strAdd3 = "";
			    }
			    
			    if(jacksonUtil.containsKey("rec_by4")){
			    	strAdd4 = jacksonUtil.getString("rec_by4");
			    }else{
			    	strAdd4 = "";
			    }
			    
			    if(jacksonUtil.containsKey("rec_by5")){
			    	strAdd5 = jacksonUtil.getString("rec_by5");
			    }else{
			    	strAdd5 = "";
			    }

			    str_filename = "";
			    if(jacksonUtil.containsKey("filename")){
			    	str_filename = jacksonUtil.getString("filename");
			    }else{
			    	str_filename = "";
			    }
			    
			    
			    str_sysfilename = "";
			    if(jacksonUtil.containsKey("sysfilename")){
			    	str_sysfilename = jacksonUtil.getString("sysfilename");
			    }else{
			    	str_sysfilename = "";
			    }
			    
			    accessPath = "";
			    if(jacksonUtil.containsKey("accessPath")){
			    	accessPath = jacksonUtil.getString("accessPath");
			    }else{
			    	accessPath = "";
			    }
			    tzAttAUrl = "";
				if(accessPath != null && !"".equals(accessPath)){
					tzAttAUrl = request.getServletContext().getRealPath(accessPath);
				}
			    
			    
			    strTjrgx = jacksonUtil.getString("rec_relation");
			      
			    // 是否邮件通知申请人;
			    strTzsqrFlg = jacksonUtil.getString("email_tx");
			    str_app_ins_version = jacksonUtil.getString("TZ_APP_INS_VERSION");  
			    
			}
			
			String str_app_ins_version_db = "";
			String str_refLetterType = "";
			String strOprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			if(numAppinsId == 0){
				if("E".equals(strTjxType)){
					mess = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_APPONLINE_MSGSET", "SEND_FAILD", "ENG", "", "");
				}else{
					mess = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_APPONLINE_MSGSET", "SEND_FAILD", "CHN", "", "");
				}
				
				return "\"" + mess + "\"";
			}
			
			/*当前报名表实例版本是否和数据库一致*/
			if(!"DELETE".equals(operateType)
					&& !"SAVE".equals(operateType)){
				if(numAppinsId > 0){
					str_app_ins_version_db = jdbcTemplate.queryForObject("SELECT TZ_APP_INS_VERSION FROM PS_TZ_APP_INS_T WHERE TZ_APP_INS_ID = ? limit 0,1", new Object[]{numAppinsId},"String");
				}
				if(!str_app_ins_version_db.equals(str_app_ins_version)){
					if("E".equals(strTjxType)){
						mess = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_APPONLINE_MSGSET", "PAGE_INVALID", "ENG", "", "");
					}else{
						mess = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_APPONLINE_MSGSET", "PAGE_INVALID", "CHN", "", "");
					}
					
					return "\"" + mess + "\"";
				}
			}
			
			if("SEND".equals(operateType)){
				//保存并发送邮件;
			     str_refLetterType = "S";
			     mess = tzTjxClsServiceImpl.saveTJX(numAppinsId, strOprid, strTjrId, strEmail, strTjxType, strTitle, strGname, strName, strCompany, strPosition, strPhone_area, strPhone_no, strGender, strAdd1, strAdd2, strAdd3, strAdd4, strAdd5, strTjrgx, str_sysfilename, str_filename, "S", "Y",accessPath,tzAttAUrl);
			     if("SUCCESS".equals(mess)){
			    	 mess =  tzTjxClsServiceImpl.sendTJX(numAppinsId, strOprid, strTjrId);
			    	 if("Y".equals(strTzsqrFlg)){
			    		 // 发送邮件通知给申请人;
			    		 if("SUCCESS".equals(mess)){
			    			 tzTjxClsServiceImpl.sendTZ(numAppinsId, strOprid, strTjrId);
			    		 }
			    	 }
			     }
			}
			
			if("CHANGE".equals(operateType)){
				//更换推荐人;
			    mess = tzTjxClsServiceImpl.changeTJR(numAppinsId, strTjrId, strOprid);
			}
			
			if("DELETE".equals(operateType)){
				jdbcTemplate.update("DELETE FROM PS_TZ_KS_TJX_TBL  WHERE TZ_APP_INS_ID = ? AND TZ_MBA_TJX_YX = 'N'" ,new Object[]{numAppinsId});
				jdbcTemplate.update("DELETE FROM PS_TZ_KS_TJX_TBL  WHERE TZ_APP_INS_ID = ? AND TZ_REFLETTERTYPE = 'U'" ,new Object[]{numAppinsId});
			}
			
			if("SAVE".equals(operateType)){
				str_refLetterType = jacksonUtil.getString("rec_type");
				String str_update = "", str_ref_id = "";
				Map<String, Object> map = jdbcTemplate.queryForMap("SELECT 'Y' IS_EXIST,TZ_REF_LETTER_ID FROM PS_TZ_KS_TJX_TBL WHERE TZ_APP_INS_ID=? AND TZ_TJR_ID=? limit 0,1",new Object[]{numAppinsId,strTjrId});
				if(map != null){
					str_update = (String)map.get("IS_EXIST");
					str_ref_id = (String)map.get("TZ_REF_LETTER_ID");
				}
				if("Y".equals(str_update)){
					PsTzKsTjxTbl psTzKsTjxTbl = psTzKsTjxTblMapper.selectByPrimaryKey(str_ref_id);
					if(psTzKsTjxTbl != null){
						psTzKsTjxTbl.setTzRefLetterId(str_ref_id);
						psTzKsTjxTbl.setTzAppInsId(numAppinsId);
						psTzKsTjxTbl.setOprid(strOprid);
						psTzKsTjxTbl.setTzTjxType(strTjxType);
						psTzKsTjxTbl.setTzTjrId(strTjrId);
						psTzKsTjxTbl.setTzMbaTjxYx(str_tjx_valid);
						psTzKsTjxTbl.setTzTjxTitle(strTitle);
						psTzKsTjxTbl.setTzReferrerGname(strGname);
						psTzKsTjxTbl.setTzReferrerName(strName);
						psTzKsTjxTbl.setTzCompCname(strCompany);
						psTzKsTjxTbl.setTzPosition(strPosition);
						psTzKsTjxTbl.setTzEmail(strEmail);
						psTzKsTjxTbl.setTzPhoneArea(strPhone_area);
						psTzKsTjxTbl.setTzPhone(strPhone_no);
						psTzKsTjxTbl.setTzGender(strGender);
						psTzKsTjxTbl.setTzReflettertype(str_refLetterType);
						psTzKsTjxTbl.setTzTjxYl1(strAdd1);
						psTzKsTjxTbl.setTzTjxYl2(strAdd2);
						psTzKsTjxTbl.setTzTjxYl3(strAdd3);
						psTzKsTjxTbl.setTzTjxYl4(strAdd4);
						psTzKsTjxTbl.setTzTjxYl5(strAdd5);
						psTzKsTjxTbl.setTzTjrGx(strTjrgx);
						
						psTzKsTjxTbl.setAttachsysfilename(str_sysfilename);
						psTzKsTjxTbl.setAttachuserfile(str_filename);
						psTzKsTjxTbl.setTzAccessPath(accessPath);
						psTzKsTjxTbl.setTzAttAUrl(tzAttAUrl);

						psTzKsTjxTbl.setRowLastmantDttm(new Date());
						psTzKsTjxTbl.setRowLastmantOprid(strOprid);
						psTzKsTjxTblMapper.updateByPrimaryKeySelective(psTzKsTjxTbl);
					}
				}else{
					mess = tzTjxClsServiceImpl.saveTJX(numAppinsId, strOprid, strTjrId, strEmail, strTjxType, strTitle, strGname, strName, strCompany, strPosition, strPhone_area, strPhone_no, strGender, strAdd1, strAdd2, strAdd3, strAdd4, strAdd5, strTjrgx, str_sysfilename, str_filename, str_refLetterType, str_tjx_valid,accessPath,tzAttAUrl);
				}
			    
			}
			
		}catch(Exception e){
			mess = "发生错误";
			e.printStackTrace();
		}
		return "\"" + mess + "\"";
	}
	
	@Override
	public String tzGetJsonData(String strParams){
		Map<String, Object> returnMap = new HashMap<>();
		returnMap.put("TJX_ZT", "");
		 returnMap.put("zhs_qy", "");
		 returnMap.put("eng_qy", "");
		 returnMap.put("tjxAppInsID", "");
		 returnMap.put("refLetterId", "");
		 returnMap.put("refAppTplId", "");
		 returnMap.put("refFileName", "");
		 returnMap.put("refFileUrl", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try{
			jacksonUtil.json2Map(strParams);
			
			 /*将字符串转换成json*/
			 long str_app_ins_id = Long.parseLong(jacksonUtil.getString("APP_INS_ID"));
			 String str_rownum = jacksonUtil.getString("rownum");
			 String str_class_id = jacksonUtil.getString("class_id");
			 String str_app_tpl_id = jacksonUtil.getString("TZ_APP_TPL_ID");
			 long str_tjx_app_ins_id = 0L;
			 String str_y= "", str_tjx_zt= "", str_ref_letter_id= "";
			 String str_tjx_language = "";
			 String str_tjx_app_tpl_id = "";
			 String str_refLetterSysFile = "", str_refLetterUserFile = "";
			 String str_att_a_url = "";
			 
			 String sql = "SELECT TZ_TJX_APP_INS_ID,'Y' STR_Y,TZ_REF_LETTER_ID,TZ_TJX_TYPE,ATTACHSYSFILENAME,ATTACHUSERFILE,TZ_ACCESS_PATH FROM PS_TZ_KS_TJX_TBL WHERE TZ_APP_INS_ID=? AND TZ_MBA_TJX_YX='Y' AND TZ_TJR_ID=? limit 0,1";
			 Map<String, Object> map = jdbcTemplate.queryForMap(sql,new Object[]{str_app_ins_id,str_rownum});
			 if(map != null){
				 try{
					 str_tjx_app_ins_id = Long.parseLong(map.get("TZ_TJX_APP_INS_ID").toString());
				 }catch(Exception e){
					 e.printStackTrace();
					 str_tjx_app_ins_id = 0L;
				 }
				 
				 str_y = (String)map.get("STR_Y");
				 str_ref_letter_id = (String)map.get("TZ_REF_LETTER_ID");
				 str_tjx_language = (String)map.get("TZ_TJX_TYPE");
				 str_refLetterSysFile = (String)map.get("ATTACHSYSFILENAME");
				 str_refLetterUserFile = (String)map.get("ATTACHUSERFILE");
				 str_att_a_url = (String)map.get("TZ_ACCESS_PATH");
			 }
			 
			 if("Y".equals(str_y)){
				 if(str_tjx_app_ins_id > 0){
					str_tjx_zt = jdbcTemplate.queryForObject("SELECT TZ_APP_FORM_STA FROM PS_TZ_APP_INS_T WHERE TZ_APP_INS_ID=? limit 0,1", new Object[]{str_tjx_app_ins_id},"String");
					if("U".equals(str_tjx_zt)){
						str_tjx_zt = "已完成";
					}else{
						if(str_att_a_url != null && !"".equals(str_att_a_url)){
							str_tjx_zt = "已完成";
						}else{
							str_tjx_app_ins_id = 0;
				            str_ref_letter_id = "0";
				            str_tjx_zt = "已发送";
						}
					}
				 }else{
					 str_tjx_zt = "已发送";
				 }
			 }
			 
			 if(str_tjx_zt == null || "".equals(str_tjx_zt)){
				str_tjx_zt = "未发送";	
			 }
			 
			 String str_qy_zhs = "", str_qy_eng = "";
			 
			 Map<String, Object> map2 = jdbcTemplate.queryForMap("SELECT B.TZ_CHN_QY,B.TZ_ENG_QY FROM PS_TZ_CLASS_INF_T A,PS_TZ_APPTPL_DY_T B WHERE A.TZ_CLASS_ID=? AND A.TZ_APP_MODAL_ID=B.TZ_APP_TPL_ID limit 0,1",new Object[]{str_class_id});
			 if(map2 != null){
				 str_qy_zhs = (String)map2.get("TZ_CHN_QY");
				 str_qy_eng = (String)map2.get("TZ_ENG_QY");
			 }
			 if(str_qy_zhs == null){
				 str_qy_zhs = "";
			 }
			 if(str_qy_eng == null){
				 str_qy_eng = "";
			 }
			 
			 if("C".equals(str_tjx_language)){
				 str_tjx_app_tpl_id = jdbcTemplate.queryForObject("SELECT TZ_CHN_MODAL_ID FROM PS_TZ_APPTPL_DY_T WHERE TZ_APP_TPL_ID = ? limit 0,1", new Object[]{str_app_tpl_id},"String");
			 }else{
				 str_tjx_app_tpl_id = jdbcTemplate.queryForObject("SELECT TZ_ENG_MODAL_ID FROM PS_TZ_APPTPL_DY_T WHERE TZ_APP_TPL_ID = ? limit 0,1", new Object[]{str_app_tpl_id},"String");
			 }
			 if(str_tjx_app_tpl_id == null){
				 str_tjx_app_tpl_id = "";
			 }
			 if(str_refLetterSysFile == null){
				 str_refLetterSysFile = ""; 
			 }
			 if(str_refLetterUserFile == null){
				 str_refLetterUserFile = ""; 
			 }
			 if(str_att_a_url == null){
				 str_att_a_url = ""; 
			 }
			 if("".equals(str_refLetterSysFile) || "".equals(str_refLetterUserFile) || "".equals(str_att_a_url)){
				 str_refLetterSysFile = ""; 
				 str_refLetterUserFile = ""; 
				 str_att_a_url = ""; 
			 }
			 returnMap.replace("TJX_ZT", str_tjx_zt);
			 returnMap.replace("zhs_qy", str_qy_zhs);
			 returnMap.replace("eng_qy", str_qy_eng);
			 returnMap.replace("tjxAppInsID", String.valueOf(str_tjx_app_ins_id));
			 returnMap.replace("refLetterId", str_ref_letter_id);
			 returnMap.replace("refAppTplId", str_tjx_app_tpl_id);
			 returnMap.replace("refFileName", str_refLetterSysFile);
			 returnMap.replace("refFileUrl", str_att_a_url);
			 
		}catch(Exception e){
			e.printStackTrace();
		}
		return jacksonUtil.Map2json(returnMap);
	}
	
	/*
	private String getRefLetterFiles(long str_app_ins_id,String sysFileName){
		String url = "";
		if(sysFileName == null || "".equals(sysFileName)){
			return "";
		}else{
			url = jdbcTemplate.queryForObject("SELECT TZ_ACCESS_PATH FROM PS_TZ_FORM_ATT_T WHERE TZ_APP_INS_ID=? AND ATTACHSYSFILENAME=?", new Object[]{str_app_ins_id,sysFileName},"String");
			if(url != null && !"".equals(url)){
				if(url.lastIndexOf("/") + 1 != url.length()){
					url = url + "/";
				}
				if(request.getServerPort() == 80){
					url = "http://" + request.getServerName() + request.getContextPath() + url + sysFileName;
				}else{
					url = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + url + sysFileName;
				}
			}
			
			
		}
		return url;
	}
	*/
	
	
	@Override
	public String tzGetHtmlContent(String strParams) {
		String reutrnHtml = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try{
			jacksonUtil.json2Map(strParams);
			
			/*报名表实例ID*/
			long strAppInsId = Long.parseLong(jacksonUtil.getString("tz_app_ins_id"));
			/*PDF地址*/
			String pdfFileUrl = jacksonUtil.getString("pdfFileUrl");
			   
			/*窗口宽度、高度*/
			//String winWidth = jacksonUtil.getString("winWidth");
			String winHeight = jacksonUtil.getString("winHeight");
			
			String lan = jdbcTemplate.queryForObject("SELECT TZ_APP_TPL_LAN FROM PS_TZ_APP_INS_T A left join PS_TZ_APPTPL_DY_T B on  A.TZ_APP_TPL_ID=B.TZ_APP_TPL_ID WHERE TZ_APP_INS_ID=? limit 0,1", new Object[]{strAppInsId},"String");
		
			// 双语化;
			String readerTitle = messageTextServiceImpl.getMessageTextWithLanguageCd("TZGD_APPONLINE_MSGSET", "PDF_VIEW", lan, "PDF预览", "PDF preview");
			//文件FTP临时存放路径/export/home/PT852/webserv/ALTZDEV/applications/peoplesoft/PORTAL.war//linkfile/FileUpLoad/appFormAttachment/;
			int height = Integer.parseInt(winHeight) - 40;
			reutrnHtml = tzGdObject.getHTMLText("HTML.TZRecommendationBundle.TZ_PDF_READER_HTML", true,
					pdfFileUrl, readerTitle, String.valueOf(height),request.getContextPath());
		}catch(Exception e){
			e.printStackTrace();
			reutrnHtml = "";
		}
		return reutrnHtml;
	}

}
