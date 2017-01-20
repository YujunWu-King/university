package com.tranzvision.gd.TZEmailSmsQFBundle.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZEmailSmsQFBundle.dao.PsTzYjqftxjlTMapper;
import com.tranzvision.gd.TZEmailSmsQFBundle.dao.PsTzYjqftxrzTMapper;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzYjqftxjlT;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzYjqftxrzT;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzYjqftxrzTKey;
import com.tranzvision.gd.util.encrypt.DESUtil;
import com.tranzvision.gd.util.mailer.TranzReceiveOneMail;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 *  PS:TZ_SMSMAL_QF_PKG:analysisBounce;
 * @author Administrator
 *
 */
@Service("com.tranzvision.gd.TZEmailSmsQFBundle.service.impl.analysisBounceServiceImpl")
public class analysisBounceServiceImpl  {
	@Autowired
	private mailContentHandlerServiceImpl  getRandomObj;
	
	@Autowired
	private SqlQuery jdbcTemplate;

	@Autowired
	private  PsTzYjqftxjlTMapper psTzYjqftxjlTMapper;
	
	@Autowired
	private PsTzYjqftxrzTMapper psTzYjqftxrzTMapper;
	
	@Autowired
	private HttpServletRequest request;
	
	public void analysisBounceByMailServId(String strPicId,String mailServId,int prcsInsId){
		String  TZ_EML_ADDR100 = "",TZ_CHS_SNAME="",TZ_SMTP_ADDR="",TZ_USR_NAME="",TZ_USR_PWD="";

		TranzReceiveOneMail mail;

		boolean isconnect = false;
		int count = 0;
		int i = 0, j = 0;
		int bounceCount = 0;
		ArrayList<Integer> delMailIndexArr = new ArrayList<>();
		SimpleDateFormat datetimeFormate = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		try{
			Map<String, Object> urlMap = jdbcTemplate.queryForMap("select TZ_ABSLT_URL,TZ_REL_URL from PS_TX_LJ_AET where PRCSINSTANCE=?",new Object[]{prcsInsId});
			if(urlMap == null){
				return ;
			}
			String dirPath = (String)urlMap.get("TZ_ABSLT_URL");
			String relPath = (String)urlMap.get("TZ_REL_URL");
			/*
			String portalUrlSql = "select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT='TZ_LOG_URL'";
			String TZ_PORTAL_URL = jdbcTemplate.queryForObject(portalUrlSql, "String");
			if(TZ_PORTAL_URL != null && !"".equals(TZ_PORTAL_URL)){
				if(TZ_PORTAL_URL.indexOf("/") != (TZ_PORTAL_URL.length()-1)){
					TZ_PORTAL_URL = TZ_PORTAL_URL + "/";
				}
			}
			
			
			String s_dtm = datetimeFormate.format(new Date());
			String fileName = getRandomObj.getRandomString(10) + "_" + s_dtm;
			String dirPath = TZ_PORTAL_URL + "linkfiles/mailLog";
			
			dirPath = request.getServletContext().getRealPath(dirPath);
			File tF = new File(dirPath);
			if (!tF.exists()) {
				tF.mkdirs();
			}
			*/
			String s_dtm = datetimeFormate.format(new Date());
			String fileName = getRandomObj.getRandomString(10) + "_" + s_dtm;
			
			String strFileFullName = "";
			
			//查看当前路径是以什么分割的;
			/*
			if("/".equals(File.separator)){
				strFileFullName = dirPath + "/" + fileName + ".log";
			}
			if("\\".equals(File.separator)){
				strFileFullName = dirPath + "\\" + fileName + ".log";
			}
			*/
			strFileFullName = dirPath + fileName + ".log";
			System.out.println("----->"+strFileFullName);
			
			//String strAccessFilePath = TZ_PORTAL_URL + "linkfiles/mailLog/" + fileName + ".log";
			String strAccessFilePath =  relPath + fileName + ".log";
			File fileLog = new File(strFileFullName);
			if(!fileLog.exists())    
			{    
			    try {   
			    	fileLog.createNewFile();    
			    } catch (IOException e) {  
			        e.printStackTrace();  
			        return ;
			    }    
			}
			
			String rwBeginDtStr = jdbcTemplate.queryForObject("SELECT DATE_FORMAT(MIN(TZ_RWZX_DT) ,'%Y-%m-%d %H:%i') TZ_RWZX_DT FROM PS_TZ_DXYJFSRW_TBL WHERE TZ_MLSM_QFPC_ID=?", new Object[]{strPicId},"String");
			Date rwBeginDt = null;
			if(rwBeginDtStr != null && !"".equals(rwBeginDtStr)){
				rwBeginDt = sdf.parse(rwBeginDtStr);
			}
			if(rwBeginDt == null){
				return;
			}
			
			Map<String,Object> map = jdbcTemplate.queryForMap("SELECT TZ_EML_ADDR100,TZ_CHS_SNAME,TZ_SMTP_ADDR,TZ_USR_NAME,TZ_USR_PWD FROM PS_TZ_EMLS_DEF_TBL WHERE TZ_EMLSERV_ID=?",new Object[]{mailServId});
			if(map != null){
				TZ_EML_ADDR100 = (String)map.get("TZ_EML_ADDR100");
				TZ_CHS_SNAME = (String)map.get("TZ_CHS_SNAME");
				TZ_SMTP_ADDR = (String)map.get("TZ_SMTP_ADDR");
				TZ_USR_NAME = (String)map.get("TZ_USR_NAME");
				TZ_USR_PWD = (String)map.get("TZ_USR_PWD");
			}
			
			String TZ_POP3_ARR = "";
			if(TZ_SMTP_ADDR != null && !"".equals(TZ_SMTP_ADDR) ){
				TZ_POP3_ARR = TZ_SMTP_ADDR.toLowerCase().replaceAll("smtp", "pop3");
			}
			
		    // 链接邮件服务器;
		    mail = new TranzReceiveOneMail(); 
		    mail.setMailHost(TZ_SMTP_ADDR);
		    mail.setPopHost(TZ_POP3_ARR);
		    mail.setUserName(TZ_USR_NAME);
		    mail.setUserPwd(TZ_USR_PWD);
		    isconnect = mail.connectServer();
		    
		    FileWriter fw = new FileWriter(strFileFullName);
		    fw.write("SMTP：" + TZ_SMTP_ADDR + "\r\n"); 
		    fw.write("POP3：" + TZ_POP3_ARR + "\r\n");  
		    
		    if(isconnect){
		    	
		    	String currentDateTimeStr = sdf.format(new Date());
		    	fw.write("链接邮件服务器成功，邮件服务器地址为：" + TZ_USR_NAME + "---" + currentDateTimeStr);
		    	count = mail.openFolder();
		    	if (count > 0){
		    		for(i = count - 1 ; i >= 0; i--){
		    			mail.Getmail(i);
		    			mail.analysisMailContent(); /*解析邮件内容*/
		    			String sendDateStr = mail.getSentDate();  
		    			Date sendDatetime = sdf.parse(sendDateStr);
		    			if(sendDatetime.before(rwBeginDt)){
		    				 /*++| 退信邮件发送时间要大于群发任务开始执行时间，如果解析的邮件发送时	   ++*/
			                  /*++| 间要小于任务开始执行时间，之后的邮件不可能是退信，退出循环             ++*/
		    				break;
		    			}
		    			
		    			 /*获取邮件内容，读取邮件内容中的批次编号，如果批次编号存在且为当前给定批次，然后判断邮件是否为退信*/
			             String bodyPicID = ""; /*短信邮件群发批次*/
			             String bounceAddr = ""; /*退信邮箱*/
			             String content = mail.getBodyText();
			             int pcFlagIndex = content.indexOf("【TRANZVISION_YJQF_PC_ID_BZ】");
			             int sIndex,eIndex;
			             if(pcFlagIndex >=0){
			            	 sIndex = content.indexOf("【",pcFlagIndex + 1);
			            	 eIndex = content.indexOf("】",sIndex);
			            	 /*邮件正文中隐藏的群发批次ID*/
			            	 bodyPicID = content.substring(sIndex + 1, eIndex);
			            	 //解密;
			            	 bodyPicID = DESUtil.decrypt(bodyPicID, "Tranzvision_Mail");
			            	 
			            	 /*解析收件箱*/
			                 int sMailPosi, eMailPosi;
			                 sMailPosi = content.indexOf("【",eIndex);
			                 eMailPosi = content.indexOf("】",sMailPosi);
			                 bounceAddr = content.substring(sMailPosi + 1,eMailPosi);
			                 //解密;
			                 bounceAddr = DESUtil.decrypt(bounceAddr, "Tranzvision_Mail");
			                 
			                 fw.write("解析邮件群发批次：" + bodyPicID + "---，收件地址：" + bounceAddr + "---，发件地址：" + mail.getFrom() + "\r\n"); 

			             }
			             
			             if(bodyPicID != null && !"".equals(bodyPicID) && bodyPicID.equals(strPicId)){
			            	 String txTypeId = "", txMode = "", txDesc="";
			                 List<Map<String, Object>> list = jdbcTemplate.queryForList("SELECT TZ_TX_TYPE_ID,TZ_TX_SSLX,TZ_TX_DESC FROM PS_TZ_TX_TYPE_TBL WHERE TZ_IS_USED='Y' order by TZ_TX_TYPE_ID");
			                 if(list != null && list.size() > 0){
			                	 for(int k =0; k < list.size(); k++){
			                		 Map<String,Object> txmap = list.get(k);
			                		 txTypeId = (String)txmap.get("TZ_TX_TYPE_ID");
			                		 txMode = (String)txmap.get("TZ_TX_SSLX");
			                		 txDesc = (String)txmap.get("TZ_TX_DESC");
			                		 
			                		 String matchType = "",matchKey="";
			                		 boolean satisfy = false;
			                		 
			                		 List<Map<String, Object>> list2 = jdbcTemplate.queryForList("SELECT TZ_TX_PPLX,TZ_TXITEM_KEY FROM PS_TZ_TX_LBTJGX_T A,PS_TZ_TX_RULE_TBL B WHERE A.TZ_TX_TYPE_ID=? AND A.TZ_TX_RULE_ID=B.TZ_TX_RULE_ID",new Object[]{txTypeId});
			                		 if(list2 != null && list2.size()>0){
			                			 for(int h=0;h<list2.size();h++){
			                				 matchType = (String)list2.get(h).get("TZ_TX_PPLX");
			                				 matchKey = (String)list2.get(h).get("TZ_TXITEM_KEY");
			                				
			                				 /*邮件地址匹配*/
			                				 if("ADD".equals(matchType)){
			                					 String add = mail.getFrom();
			                					 if(add.indexOf(matchKey) >= 0){
			                						 satisfy = true;
			                						 fw.write("邮箱地址匹配成功，匹配关键词：" + matchKey + "\r\n"); 
			                					 }else{
			                						 satisfy = false;
			                					 }						                          
			                				 }
			                				 /*邮件主题匹配*/
			                				 if("SUB".equals(matchType)){
			                					 String con = mail.getSubject();
			                					 if(con.indexOf(matchKey) >= 0){
			                						 satisfy = true;
			                						 fw.write("邮箱主题匹配成功，匹配关键词：" + matchKey + "\r\n"); 
			                					 }else{
			                						 satisfy = false;
			                					 }						                          
			                				 }
			                				 /*邮件内容匹配*/
			                				 if("CON".equals(matchType)){
			                					 String sub = mail.getBodyText();
			                					 if(sub.indexOf(matchKey) >= 0){
			                						 satisfy = true;
			                						 fw.write("邮箱内容匹配成功，匹配关键词：" + matchKey + "\r\n"); 
			                					 }else{
			                						 satisfy = false;
			                					 }						                          
			                				 }
			                				 if(satisfy){
			                					 break;
			                				 }
			                				
			                			 }
			                		 }
			                		 if(satisfy){
			                			 /*匹配成功*/
					                     String messageId = mail.getMessageId();
					                     fw.write("匹配成功，邮箱唯一标识：" + messageId + "\r\n"); 
					                     /*是否已经解析过*/
					                     int isAnalysis = 0; 
					                     /*是否为同一个人*/
					                     int isSameAddr = 0; 
					                     isAnalysis = jdbcTemplate.queryForObject("SELECT count(1) FROM PS_TZ_YJQFTXJL_T WHERE TZ_TX_MSG_ID=?", new Object[]{messageId},"Integer");
					                     isSameAddr = jdbcTemplate.queryForObject("SELECT count(1) FROM PS_TZ_YJQFTXJL_T WHERE TZ_MLSM_QFPC_ID=? AND TZ_EMAIL=?", new Object[]{strPicId,bounceAddr},"Integer"); 
					                     if(isAnalysis == 0 && isSameAddr== 0){
					                    	 PsTzYjqftxjlT psTzYjqftxjlT = new PsTzYjqftxjlT();
					                    	 psTzYjqftxjlT.setTzMlsmQfpcId(strPicId);
					                    	 psTzYjqftxjlT.setTzTxMsgId(messageId);
					                    	 psTzYjqftxjlT.setTzEmail(bounceAddr);
					                    	 psTzYjqftxjlT.setTzTxType(txMode);
					                    	 psTzYjqftxjlT.setTzTxDttm(sendDatetime);
					                    	 psTzYjqftxjlT.setTzTxDesc(txDesc);
					                    	 psTzYjqftxjlT.setTzMalContent(content);
					                    	 int bl = psTzYjqftxjlTMapper.insert(psTzYjqftxjlT);
					                    	 if(bl > 0){
					                    		 /*记录退信邮件信息*/
					                    		 fw.write("以下为退信邮件详情：" + matchKey + "\r\n"); 
					                    		 fw.write("\r\n"); 
					                    		 fw.write("===================================================================================\r\n"); 
					                    		 fw.write("【邮件地址】：" + mail.getFrom() + "\r\n"); 
					                    		 fw.write("【邮件主题】：" + mail.getSubject() + "\r\n"); 
					                    		 fw.write("【邮件内容】：" + mail.getBodyText() + "\r\n"); 
					                    		 bounceCount = bounceCount + 1;
					                    		 delMailIndexArr.add(i);
					                    	 }
					                     }else{
					                    	 fw.write("该匹配成功的退信邮件已解析过了，不在解析。"+ "\r\n"); 
				                    	 }
					                     
					                     break;
			                		 }
			                	 }
			                 }
			             }
			                 
		    		}
		    		/*删除解析成功的退信*/
		    		for(j = 0; j < delMailIndexArr.size(); j++){
		    			 /*从收件箱中删除退信邮件*/
		    			mail.Delmail(j);
		    		}
		    		/*没有读取到退信*/
		    		if(bounceCount == 0){
		    			 fw.write("群发任务批次" + strPicId + "没有新的退信。----" + sdf.format(new Date()) + "\r\n"); 
		    		}
		            
		    	}
		    }else{
		    	fw.write("链接邮件服务器失败---" + sdf.format(new Date()) + "\r\n"); 
		    }
		    
		    /*记录邮件群发退信日志表*/
		    PsTzYjqftxrzTKey psTzYjqftxrzTKey = new PsTzYjqftxrzTKey();
		    psTzYjqftxrzTKey.setTzMlsmQfpcId(strPicId);
		    psTzYjqftxrzTKey.setPrcsinstance(prcsInsId);
		    PsTzYjqftxrzT psTzYjqftxrzT = psTzYjqftxrzTMapper.selectByPrimaryKey(psTzYjqftxrzTKey);
		    if(psTzYjqftxrzT != null){
		    	psTzYjqftxrzT.setTzFilePath(strAccessFilePath);
		    	psTzYjqftxrzTMapper.updateByPrimaryKey(psTzYjqftxrzT);
		    }
		    fw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
    
	}
 
}
