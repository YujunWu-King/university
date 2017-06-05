package com.tranzvision.gd.util.mailer;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPMessage;
import com.sun.mail.pop3.POP3Folder;


/**
 * 读取收件箱内邮件，并解析.eml附件内容
 * @author zhangL
 * 2017-05-26，张浪修改：邮件.eml附件解析修改，并增加带.eml附件邮件内容
 */
public class TranzReceiveOneMail {   
	private MimeMessage mimeMessage;   
    private StringBuffer bodytext;//存放邮件内容   
    private StringBuffer bodyAttText; //存放邮件内容，包含.eml内容
    private String isAnalysisEml; //正在解析。eml附件
    private String dateformat = "yyyy-MM-dd HH:mm"; //默认的日前显示格式   
    
    private String mailHost;
    private String popHost;
    private String userName;
    private String userPwd;
	private Properties properties;
    private Session session;
    private URLName urlName;
    private Folder folder;
    private Store store;
    private Message message[];
  
    //邮件协议SMTP、IMAP4、POP3,端口号
    private Map<String,Integer> mailProtclMap;
    
    /**
     * 构造函数，初始化变量
     */
    public TranzReceiveOneMail() {   
    	properties = null;
        session = null;
        urlName = null;
        store = null;
        folder = null;
        message = null;
        mimeMessage = null;
        bodytext = new StringBuffer();
        bodyAttText = new StringBuffer();
        isAnalysisEml = "N";
        
        mailProtclMap = new HashMap<String,Integer>();
        mailProtclMap.put("pop3", 110);
        mailProtclMap.put("smtp", 25);
        mailProtclMap.put("imap", 143);
    }   
    /**
     * 设置邮件smtp
     * @param s_MailHost
     */
    public void setMailHost(String s_MailHost)
    {
        mailHost = s_MailHost;
    }
    /**
     * 设置邮件Pop
     * @param s_Pop3Host
     */
    public void setPopHost(String s_Pop3Host)
    {
        popHost = s_Pop3Host;
    }
    /**
     * 设置用户名
     * @param s_UserName
     */
    public void setUserName(String s_UserName)
    {
        userName = s_UserName;
    }
    /**
     * 设置密码
     * @param s_UserPwd
     */
    public void setUserPwd(String s_UserPwd)
    {
        userPwd = s_UserPwd;
    }
    
    /**
     * 根据邮件服务器获取邮件协议和端口号
     * @param strPopHost
     * @return
     */
    private String[] getProtocolAndPort(String strPopHost){
    	String[] protPort = new String[2];
    	protPort[0] = "pop3";
		protPort[1] = mailProtclMap.get("pop3").toString();
		
    	if(strPopHost != null && !"".equals(strPopHost)){
    		String[] hostArr = strPopHost.split("\\.");
    		if(hostArr.length>0){
    			String hostId = hostArr[0].toLowerCase();
    			
    			if(mailProtclMap.containsKey(hostId)){
    				protPort[0] = hostId;
    				protPort[1] = mailProtclMap.get(hostId).toString();
    			}
    		}
    	}
    	return protPort;
    }
    
    
    /**
     * 链接邮件服务器
     * @return
     * @throws Exception
     */
    public boolean connectServer()throws Exception{
        try{
            properties = System.getProperties();
            properties.put("mail.smtp.host", mailHost);
            properties.put("mail.smtp.auth", "true");
            
            Authenticator auth =  new PopupAuthenticator(userName, userPwd);
            session = Session.getDefaultInstance(properties, auth);
            
            String[] protPortArr = getProtocolAndPort(popHost);
            String protocol = protPortArr[0];
            int port = Integer.valueOf(protPortArr[1]);

            urlName = new URLName(protocol, popHost, port, null, userName, userPwd);
            store = session.getStore(urlName);
            store.connect();
        }catch(Exception exception){
            exception.printStackTrace();
            return false;
        }
        return true;
    }
    
    /**
     * 打开收件箱
     * @return
     * @throws Exception
     */
    public int openFolder()throws Exception
    {
        int mailcount = 0;
        try{
	        folder = store.getFolder("INBOX");
	        folder.open(Folder.READ_WRITE);
	        message = folder.getMessages();
	        mailcount = message.length;

	        return mailcount;
        }catch(Exception exception){
	        exception.printStackTrace();
	        return mailcount;
        }
    }
    /**
     * 关闭收件箱
     * @throws Exception
     */
    public void closeFolder()throws Exception{
        try{
	        folder.close(true);
        }catch(Exception exception){
	        exception.printStackTrace();
        }
    }
    /**
     * 获取邮件
     * @param idx
     * @throws Exception
     */
    public void Getmail(int idx)throws Exception{
        try{
        	bodytext = new StringBuffer();
        	bodyAttText = new StringBuffer();
        	isAnalysisEml = "N";
            mimeMessage = (MimeMessage)message[idx];
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }
    /**
     * 删除邮件
     * @param idx
     * @return
     * @throws Exception
     */
    public boolean Delmail(int idx)throws Exception{
        boolean bool = false;
        try{
            mimeMessage = (MimeMessage)message[idx];
            mimeMessage.setFlag(javax.mail.Flags.Flag.DELETED, true);
            //folder.close(true);
        }catch(Exception exception){
            exception.printStackTrace();
        }
        if(mimeMessage.isSet(javax.mail.Flags.Flag.DELETED))
            bool = true;
        return bool;
    }

    /**  
     * 获得发件人的地址和姓名  
     */  
    public String getFrom() throws Exception {  
    	String fromaddr = "";
    	try{
	        InternetAddress address[] = (InternetAddress[]) mimeMessage.getFrom();   
	        String from = address[0].getAddress();   
	        if (from == null)   
	            from = "";   
	        String personal = address[0].getPersonal();   
	        if (personal == null)   
	            personal = "";   
	        fromaddr = personal + "<" + from + ">";   
    	}catch(Exception exce){
    		exce.printStackTrace();
    	}
    	return fromaddr;
    }   
  
    /**  
     * 获得邮件的收件人，抄送，和密送的地址和姓名，根据所传递的参数的不同 "to"----收件人 "cc"---抄送人地址 "bcc"---密送人地址  
     */  
    public String getMailAddress(String type) throws Exception {   
        String mailaddr = "";   
        try{
	        String addtype = type.toUpperCase();   
	        InternetAddress[] address = null;   
	        if (addtype.equals("TO") || addtype.equals("CC")|| addtype.equals("BCC")) {   
	            if (addtype.equals("TO")) {   
	                address = (InternetAddress[]) mimeMessage.getRecipients(Message.RecipientType.TO);   
	            } else if (addtype.equals("CC")) {   
	                address = (InternetAddress[]) mimeMessage.getRecipients(Message.RecipientType.CC);   
	            } else {   
	                address = (InternetAddress[]) mimeMessage.getRecipients(Message.RecipientType.BCC);   
	            }   
	            if (address != null) {   
	                for (int i = 0; i < address.length; i++) {   
	                    String email = address[i].getAddress();   
	                    if (email == null)   
	                        email = "";   
	                    else {   
	                        email = MimeUtility.decodeText(email);   
	                    }   
	                    String personal = address[i].getPersonal();   
	                    if (personal == null)   
	                        personal = "";   
	                    else {   
	                        personal = MimeUtility.decodeText(personal);   
	                    }   
	                    String compositeto = personal + "<" + email + ">";   
	                    mailaddr += "," + compositeto;   
	                }   
	                mailaddr = mailaddr.substring(1);   
	            }   
	        } else {   
	            throw new Exception("Error emailaddr type!");   
	        }   
        }catch(Exception exce){
    		exce.printStackTrace();
        }
        return mailaddr;   
    }   
  
    /**  
     * 获得邮件主题  
     */  
    public String getSubject() throws MessagingException {   
        String subject = "";   
        try {   
            subject = MimeUtility.decodeText(mimeMessage.getSubject());   
            if (subject == null)   
                subject = "";   
        } catch (Exception exce) {}   
        return subject;   
    }   
    
    
    private Object getContent(Message email) throws IOException, MessagingException{
    	try {
            return email.getContent();
        } catch (MessagingException e) {
            // handling the bug
            if (email instanceof MimeMessage && "Unable to load BODYSTRUCTURE".equalsIgnoreCase(e.getMessage())) {
            	IMAPMessage imapMessage = (IMAPMessage) email;  
            	MimeMessage cmsg = new MimeMessage((MimeMessage) imapMessage);
            	return cmsg.getContent();
            } else {
                throw e;
            }
        }
    }
    
    
    /**
     * 邮件内容，包含。eml附件
     */
    public void analysisMailContent() throws Exception {   
        try {    
        	MimeMessage mime = mimeMessage;
           // Object o = mime.getContent();   
            Object o = getContent(mime);
            
            if(o instanceof Multipart) {  
                Multipart multipart = (Multipart) o ;   
                reMultipart(multipart);  
            } else if (o instanceof Part){  
                Part part = (Part) o;   
                rePart(part);
            } else {  
            	String mType = mime.getContentType().toLowerCase();
                if(mType.startsWith("text/plain") || mType.startsWith("text/html")){
                	if("Y".equals(isAnalysisEml)){
                		bodyAttText.append((String)mime.getContent());
    				}else{
    					bodytext.append((String)mime.getContent());
    					bodyAttText.append((String)mime.getContent());
    				}
                }
            }  
        } catch (Exception exce) {
        	exce.printStackTrace();
        }   
    }   
    
    public void analysisMailContent(MimeMessage mime) throws Exception {   
        try {    
           // Object o = mime.getContent();
        	Object o = getContent(mime);
        	
            if(o instanceof Multipart) {  
                Multipart multipart = (Multipart) o ;  
                reMultipart(multipart);  
            } else if (o instanceof Part){  
                Part part = (Part) o;   
                rePart(part);
            } else {  
                String mType = mime.getContentType().toLowerCase();
                if(mType.startsWith("text/plain") || mType.startsWith("text/html")){
                	if("Y".equals(isAnalysisEml)){
                		String content = (String) mime.getContent();
                    	String emailContent = new String(content.getBytes("iso-8859-1"),"GBK");
                    	
                		bodyAttText.append(emailContent);
    				}else{
    					bodytext.append((String)mime.getContent());
    					bodyAttText.append((String)mime.getContent());
    				}
                }
            }  
        } catch (Exception exce) {
        	exce.printStackTrace();
        }   
    }  
    
    
	private void rePart(Part part) throws Exception {  
		String disposition = part.getDisposition(); //用于判断是否为附件
		String attachment = Part.ATTACHMENT.toUpperCase();
		String inline = Part.INLINE.toUpperCase();
		
		if (disposition != null 
				&& (disposition.toUpperCase().equals(attachment) || disposition.toUpperCase().equals(inline))) {  
			
			if(part.getContentType().toLowerCase().startsWith("text/html")) {  
				if("Y".equals(isAnalysisEml)){
					bodyAttText.append((String)part.getContent());
				}
		    }
			
			String fileName = part.getFileName();
		    String strFileNmae = "";
		    if(fileName != null){
		    	strFileNmae = MimeUtility.decodeText(part.getFileName()); //MimeUtility.decodeText解决附件名乱码问题  
		    }

		    if(strFileNmae.toLowerCase().endsWith(".eml")){
		    	//解析。eml附件
			    InputStream in = part.getInputStream();// 打开附件的输入流  
	            MimeMessage msg = new MimeMessage(session,in);
	            
	            isAnalysisEml = "Y";
			    //重新读取附件内容
	            analysisMailContent(msg);
		    }	    
		} else {  
			String conType = part.getContentType().toLowerCase();
			 if(conType.startsWith("text/plain") || conType.startsWith("text/html")) {
				 	if("Y".equals(isAnalysisEml)){
				 		bodyAttText.append((String)part.getContent());
				 	}else{
				 		bodytext.append((String)part.getContent());
			            bodyAttText.append((String)part.getContent());
				 	}
	         }else if(part.isMimeType("message/rfc822")){
	        	 	String fileName = part.getFileName();
		 		    String strFileNmae = "";
		 		    if(fileName != null){
		 		    	strFileNmae = MimeUtility.decodeText(part.getFileName()); //MimeUtility.decodeText解决附件名乱码问题  
		 		    }
	
		 		    if(strFileNmae.toLowerCase().endsWith(".eml")){
		 		    	//解析。eml附件
		 			    InputStream in = part.getInputStream();// 打开附件的输入流  
		 	            MimeMessage msg = new MimeMessage(session,in);
		 	            
		 	            isAnalysisEml = "Y";
		 			    //重新读取附件内容
		 	            analysisMailContent(msg);
		 		    }	 
	         }
		}  
	}  

	/** 
	* @param multipart // 接卸包裹（含所有邮件内容(包裹+正文+附件)） 
	* @throws Exception 
	*/  
    private void reMultipart(Multipart multipart) throws Exception {  
	// 依次处理各个部分  
	for (int j = 0, n = multipart.getCount(); j < n; j++) {  

	    Part part = multipart.getBodyPart(j);//解包, 取出 MultiPart的各个部分, 每部分可能是邮件内容,  
	    // 也可能是另一个小包裹(MultipPart)  
	    // 判断此包裹内容是不是一个小包裹, 一般这一部分是 正文 Content-Type: multipart/alternative  
	    
	    String partType = part.getContentType().toLowerCase();
	    if(partType.startsWith("text/plain") || partType.startsWith("text/html")) {
	    	if("Y".equals(isAnalysisEml)){
		 		bodyAttText.append((String)part.getContent());
		 	}else{
		 		bodytext.append((String)part.getContent());
	            bodyAttText.append((String)part.getContent());
		 	}
	    }else if (part.getContent() instanceof Multipart) {  
	        Multipart p = (Multipart) part.getContent();// 转成小包裹  
	        //递归迭代  
	        reMultipart(p);  
	    } else {  
	        rePart(part);
	    }  
	 }  
	}  
		  
    /**  
     * 获得邮件发送日期  
     */  
    public String getSentDate() throws Exception {   
    	String sendDate = null;
    	try{
	    	java.util.Date date = mimeMessage.getSentDate();
	        SimpleDateFormat simpledateformat = new SimpleDateFormat(dateformat);
	        sendDate = simpledateformat.format(date);
    	}catch(Exception exce) {
        	exce.printStackTrace();
    	}
    	return sendDate;
    }   
  
    /**  
     * 获得邮件正文内容  
     */  
    public String getBodyText() {   
        return bodytext.toString();   
    }  
    
    public String getBodyWithEmlText(){
    	return bodyAttText.toString();
    }
  
    /**   
     * 判断此邮件是否需要回执，如果需要回执返回"true",否则返回"false"  
     */   
    public boolean getReplySign() throws MessagingException {   
        boolean replysign = false;   
        String needreply[] = mimeMessage.getHeader("Disposition-Notification-To");   
        if (needreply != null) {   
            replysign = true;   
        }   
        return replysign;   
    }   
  
    /**  
     * 获得此邮件的Message-ID  
     */  
    public String getMessageId() throws MessagingException {   
        return mimeMessage.getMessageID();   
    }   
  
    /**  
     * 【判断此邮件是否已读，如果未读返回返回false,反之返回true】  
     */  
    public boolean isNew() throws MessagingException {   
        boolean isnew = false;   
        Flags flags = ((Message) mimeMessage).getFlags();   
        Flags.Flag[] flag = flags.getSystemFlags();   
        
        for (int i = 0; i < flag.length; i++) {   
            if (flag[i] == Flags.Flag.SEEN) {   
                isnew = true;   
                break;   
            }   
        }   
        return isnew;   
    }   

    /**  
     * 【设置日期显示格式】  
     */  
    public void setDateFormat(String format) throws Exception {   
        this.dateformat = format;   
    }   
    
	public MimeMessage getMimeMessage() {
		return mimeMessage;
	}
	public void setMimeMessage(MimeMessage mimeMessage) {
		this.mimeMessage = mimeMessage;
	}  
	
	/**
	 * 获取邮件UID-标识你这个账户的每一封邮件
	 * @return
	 */
	public String getMailUID(){
		String uid = "";
		try{
			if(folder instanceof POP3Folder){
				uid = ((POP3Folder)folder).getUID(mimeMessage);
			}else if(folder instanceof IMAPFolder){
				long uidL = ((IMAPFolder)folder).getUID(mimeMessage);
				uid = Long.toString(uidL);
			}
		} catch (MessagingException e1) {
			e1.printStackTrace();
		}
		return uid;
	}
	
    /**  
     * PraseMimeMessage类测试  
     */  
    public static void main(String args[]) throws Exception {  
    	TranzReceiveOneMail mail = new TranzReceiveOneMail(); 
        
        mail.setMailHost("smtp.exmail.qq.com");
    	mail.setPopHost("imap.exmail.qq.com");
    	mail.setUserName("mis@snai.edu.cn");
        mail.setUserPwd("xxxxx");
        
        int mailCount;
        boolean isConnect = mail.connectServer();
        if(isConnect){
        	mailCount = mail.openFolder();
        	System.out.println("读取邮件数量："+mailCount);
        	for(int i=mailCount-1; i>=0; i--){
        		mail.Getmail(i);
        		System.out.println("========================================================================>"+i);
        		
        		System.out.println("发件箱地址："+mail.getFrom());
        		System.out.println("收件人："+mail.getMailAddress("TO"));
        		System.out.println("主题："+mail.getSubject());
        		System.out.println("UID："+mail.getMailUID());
        		
        		mail.analysisMailContent();
        		System.out.println("邮件内容--------------------------------------：");
        		System.out.println(mail.getBodyText());
        		System.out.println();
        		System.out.println("邮件with .eml内容--------------------------------------：");
        		System.out.println(mail.getBodyWithEmlText());
        	}
        }
    } 
    
    
    
    class PopupAuthenticator extends Authenticator{
    	private String username;
    	private String password;
    	public PopupAuthenticator(String username,String pwd){
    		this.username=username;
    		this.password=pwd;
    	}
    	public PasswordAuthentication getPasswordAuthentication(){
    		return new PasswordAuthentication(this.username,this.password);
    	}
    }
}  