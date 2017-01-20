package com.tranzvision.gd.util.mailer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;


/**
 * 接收邮件
 * @author zhangL
 *
 */
public class TranzReceiveOneMail {   
	private MimeMessage mimeMessage;   
    private String saveAttachPath = ""; //附件下载后的存放目录   
    private StringBuffer bodytext;//存放邮件内容   
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
     * 链接邮件服务器
     * @return
     * @throws Exception
     */
    public boolean connectServer()throws Exception{
        try{
            properties = System.getProperties();
            properties.put("mail.smtp.host", mailHost);
            properties.put("mail.smtp.auth", "true");
            session = Session.getDefaultInstance(properties, null);
            urlName = new URLName("pop3", popHost, 110, null, userName, userPwd);
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
	        System.out.println("读写异常？");
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
/*  
    public void setMimeMessage(MimeMessage mimeMessage) {   
        this.mimeMessage = mimeMessage;   
    }   
  */
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
    
    /**
     * 邮件内容，包含。eml附件
     */
    public void analysisMailContent() throws Exception {   
        try {    
        	MimeMessage mime = mimeMessage;
            Object o = mime.getContent();   
            if(o instanceof Multipart) {  
                Multipart multipart = (Multipart) o ;   
                reMultipart(multipart);  
            } else if (o instanceof Part){  
                Part part = (Part) o;   
                rePart(part);  
            } else {  
                //System.out.println("类型" + mimeMessage.getContentType());  
                //System.out.println("内容" + mimeMessage.getContent());
            	String mType = mimeMessage.getContentType();
                if(mType.startsWith("text/plain") || mType.startsWith("text/html")){
                	bodytext.append((String)mimeMessage.getContent());
                }
            }  
        } catch (Exception exce) {
        	exce.printStackTrace();
        }   
    }   
    
    public void analysisMailContent(MimeMessage mime) throws Exception {   
        try {    
            Object o = mime.getContent();   
            if(o instanceof Multipart) {  
                Multipart multipart = (Multipart) o ;  
                reMultipart(multipart);  
            } else if (o instanceof Part){  
                Part part = (Part) o;   
                rePart(part);  
            } else {  
                String mType = mimeMessage.getContentType();
                if(mType.startsWith("text/plain") || mType.startsWith("text/html")){
                	bodytext.append((String)mimeMessage.getContent());
                }
            }  
        } catch (Exception exce) {
        	exce.printStackTrace();
        }   
    }  
    
    private void rePart(Part part) throws Exception {  
		if (part.getDisposition() != null) {  
		  
		    String strFileNmae = MimeUtility.decodeText(part.getFileName()); //MimeUtility.decodeText解决附件名乱码问题  
		   // System.out.println("发现附件: " +  MimeUtility.decodeText(part.getFileName()));  
		   // System.out.println("内容类型: " + MimeUtility.decodeText(part.getContentType()));  
		   // System.out.println("附件内容:" + part.getContent());  
		    if(strFileNmae.toLowerCase().endsWith(".eml")){
		    	//解析。eml附件
			    InputStream in = part.getInputStream();// 打开附件的输入流  
			    Session mailSession = Session.getDefaultInstance(System.getProperties(), null);
	            MimeMessage msg = new MimeMessage(mailSession,in);
			    //重新读取附件内容
	            analysisMailContent(msg);
		    }	    
		} else {  
			 if(part.getContentType().startsWith("text/plain") || part.getContentType().startsWith("text/html")) {  
	                bodytext.append((String)part.getContent());
	         } 
		}  
	}  

	/** 
	* @param multipart // 接卸包裹（含所有邮件内容(包裹+正文+附件)） 
	* @throws Exception 
	*/  
    private void reMultipart(Multipart multipart) throws Exception {  
	//System.out.println("邮件共有" + multipart.getCount() + "部分组成");  
	// 依次处理各个部分  
	for (int j = 0, n = multipart.getCount(); j < n; j++) {  
	    //System.out.println("处理第" + j + "部分");  
	    Part part = multipart.getBodyPart(j);//解包, 取出 MultiPart的各个部分, 每部分可能是邮件内容,  
	    // 也可能是另一个小包裹(MultipPart)  
	    // 判断此包裹内容是不是一个小包裹, 一般这一部分是 正文 Content-Type: multipart/alternative  
	    if (part.getContent() instanceof Multipart) {  
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
  
    /**  
     * 解析邮件，把得到的邮件内容保存到一个StringBuffer对象中，解析邮件 主要是根据MimeType类型的不同执行不同的操作，一步一步的解析  
     */  
    public void getMailContent(Part part) throws Exception {   
    	try{
	        String contenttype = part.getContentType();   
	        int nameindex = contenttype.indexOf("name");   
	        boolean conname = false;   
	        if (nameindex != -1)   
	            conname = true;   
	        if (part.isMimeType("text/plain") && !conname) {   
	            bodytext.append((String) part.getContent());   
	        } else if (part.isMimeType("text/html") && !conname) {   
	            bodytext.append((String) part.getContent());   
	        } else if (part.isMimeType("multipart/*")) {   
	            Multipart multipart = (Multipart) part.getContent();   
	            int counts = multipart.getCount();   
	            for (int i = 0; i < counts; i++) {   
	                getMailContent(multipart.getBodyPart(i));   
	            }   
	        } else if (part.isMimeType("message/rfc822")) {   
	            getMailContent((Part) part.getContent());   
	        } else {}   
    	}catch(Exception exce) {
        	exce.printStackTrace();
    	}
    }   
  
    /**   
     * 判断此邮件是否需要回执，如果需要回执返回"true",否则返回"false"  
     */   
    public boolean getReplySign() throws MessagingException {   
        boolean replysign = false;   
        String needreply[] = mimeMessage   
                .getHeader("Disposition-Notification-To");   
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
        System.out.println("flags's length: " + flag.length);   
        for (int i = 0; i < flag.length; i++) {   
            if (flag[i] == Flags.Flag.SEEN) {   
                isnew = true;   
                System.out.println("seen Message.......");   
                break;   
            }   
        }   
        return isnew;   
    }   
  
    /**  
     * 判断此邮件是否包含附件  
     */  
    public boolean isContainAttach(Part part) throws Exception {   
        boolean attachflag = false;   
		if (part.isMimeType("multipart/*")) {   
            Multipart mp = (Multipart) part.getContent();   
            for (int i = 0; i < mp.getCount(); i++) {   
                BodyPart mpart = mp.getBodyPart(i);   
                String disposition = mpart.getDisposition();   
                if ((disposition != null)   
                        && ((disposition.equals(Part.ATTACHMENT)) || (disposition   
                                .equals(Part.INLINE))))   
                    attachflag = true;   
                else if (mpart.isMimeType("multipart/*")) {   
                    attachflag = isContainAttach((Part) mpart);   
                } else {   
                    String contype = mpart.getContentType();   
                    if (contype.toLowerCase().indexOf("application") != -1)   
                        attachflag = true;   
                    if (contype.toLowerCase().indexOf("name") != -1)   
                        attachflag = true;   
                }   
            }   
        } else if (part.isMimeType("message/rfc822")) {   
            attachflag = isContainAttach((Part) part.getContent());   
        }   
        return attachflag;   
    }   
  
    /**   
     * 【保存附件】   
     */   
    public void saveAttachMent(Part part) throws Exception {   
        String fileName = "";   
        if (part.isMimeType("multipart/*")) {   
            Multipart mp = (Multipart) part.getContent();   
            for (int i = 0; i < mp.getCount(); i++) {   
                BodyPart mpart = mp.getBodyPart(i);   
                String disposition = mpart.getDisposition();   
                if ((disposition != null)   
                        && ((disposition.equals(Part.ATTACHMENT)) || (disposition   
                                .equals(Part.INLINE)))) {   
                    fileName = mpart.getFileName();   
                    if (fileName.toLowerCase().indexOf("gb2312") != -1 && (fileName != null)) {   
                        fileName = MimeUtility.decodeText(fileName);   
                        saveFile(fileName, mpart.getInputStream());   
                    }   
                } else if (mpart.isMimeType("multipart/*")) {   
                    saveAttachMent(mpart);   
                } else {   
                    fileName = mpart.getFileName();   
                    if ((fileName != null)   
                            && (fileName.toLowerCase().indexOf("GB2312") != -1)) {   
                        fileName = MimeUtility.decodeText(fileName);   
                        saveFile(fileName, mpart.getInputStream());   
                    }   
                }   
            }   
        } else if (part.isMimeType("message/rfc822")) {   
            saveAttachMent((Part) part.getContent());   
        }   
    }   
  
    /**   
     * 【设置附件存放路径】   
     */   
  
    public void setAttachPath(String attachpath) {   
        this.saveAttachPath = attachpath;   
    }   
  
    /**  
     * 【设置日期显示格式】  
     */  
    public void setDateFormat(String format) throws Exception {   
        this.dateformat = format;   
    }   
  
    /**  
     * 【获得附件存放路径】  
     */  
    public String getAttachPath() {   
        return saveAttachPath;   
    }   
  
    /**  
     * 【真正的保存附件到指定目录里】  
     */  
    private void saveFile(String fileName, InputStream in) throws Exception {   
        String osName = System.getProperty("os.name");   
        String storedir = getAttachPath();   
        String separator = "";   
        if (osName == null)   
            osName = "";   
        if (osName.toLowerCase().indexOf("win") != -1) {   
            separator = "\\";  
            if (storedir == null || storedir.equals(""))  
                storedir = "c:\\tmp";  
        } else {  
            separator = "/";  
            storedir = "/tmp";  
        }  
        File storefile = new File(storedir + separator + fileName);  
        BufferedOutputStream bos = null;  
        BufferedInputStream bis = null;  
        try {  
            bos = new BufferedOutputStream(new FileOutputStream(storefile));  
            bis = new BufferedInputStream(in);  
            int c;  
            while ((c = bis.read()) != -1) {  
                bos.write(c);  
                bos.flush();  
            }  
        } catch (Exception exception) {  
            exception.printStackTrace();  
            throw new Exception("文件保存失败!");  
        } finally {  
            bos.close();  
            bis.close();  
        }  
    }  
    
	public MimeMessage getMimeMessage() {
		return mimeMessage;
	}
	public void setMimeMessage(MimeMessage mimeMessage) {
		this.mimeMessage = mimeMessage;
	}  
 
    /**  
     * PraseMimeMessage类测试  
     */  
    public static void main(String args[]) throws Exception {  
    	TranzReceiveOneMail mail = new TranzReceiveOneMail(); 
    	mail.setMailHost("smtp.aliyun.com");
    	mail.setPopHost("pop3.aliyun.com");
    	mail.setUserName("cloud_admissions@aliyun.com");
        mail.setUserPwd("Tranzvision123");
        
        int mailCount;
        if(mail.connectServer()){
        	System.out.println(new Date());
        	mailCount = mail.openFolder();
        	System.out.println("读取邮件数量："+mailCount);
        	for(int i=0; i<mailCount; i++){
        		mail.Getmail(i);
        		System.out.println("========================================================================");
        		System.out.println("发件箱地址："+mail.getFrom());
        		System.out.println("收件人："+mail.getMailAddress("TO"));
        		System.out.println("主题："+mail.getSubject());
        		System.out.println("MessageID："+mail.getMessageId());
        		mail.analysisMailContent();
        		System.out.println("邮件内容："+mail.getBodyText());
        		System.out.println("邮件时间："+mail.getSentDate());
        	}
        	System.out.println(new Date());
        }
    } 
}  
