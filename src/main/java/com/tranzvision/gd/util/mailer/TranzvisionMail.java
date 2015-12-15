package com.tranzvision.gd.util.mailer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class TranzvisionMail
{
	class MailAuthenticator extends Authenticator
	{

		private String UserName;
		private String Password;

		public PasswordAuthentication getPasswordAuthentication()
		{
			return new PasswordAuthentication(UserName, Password);
		}

		public MailAuthenticator(String s, String s1)
		{
			UserName = s;
			Password = s1;
		}
	}
	
  private String mailHost;
  private String fromAddress;
  private String fromAddressAlias;
  private String userName;
  private String userPwd;
  private String toAddress;
  private Address[] addrAddress;
  private String toCCAddr;
  private Address[] addrCC;
  private String toBCCAddr;
  private Address[] addrBCC;
  private String mailSubject;
  private String mailBody;
  private boolean isHtml;
  private String attachment;
  private int anum;
  private int snum;
  private int tnum;
  Vector vfile;
  Vector vfileName;
  Vector vmsg;
  private String logFileUrl;
  private String bodyCharsetHtml;
  private String bodyCharsetTxt;
  private String attachCharset1;
  private String attachCharset2;
  private String errDescription;
  private boolean addrReady;
  private boolean sessionReady;
  private String emailstr;
  Session mailSession;
  Properties props;
  public int int_sendmailnum;
  public int int_recvmailnum;

  public TranzvisionMail()
  {
    this.addrAddress = null;

    this.addrCC = null;

    this.addrBCC = null;

    this.vfile = new Vector(10, 10);

    this.vfileName = new Vector(10, 10);

    this.vmsg = new Vector(10, 10);

    this.bodyCharsetHtml = "GB18030";

    this.bodyCharsetTxt = "GB18030";

    this.attachCharset1 = "GB18030";

    this.attachCharset2 = "GB18030";

    this.errDescription = "";

    this.addrReady = false;

    this.sessionReady = false;

    this.emailstr = "";

    this.mailSession = null;

    this.props = null;

    this.int_sendmailnum = 0;

    this.int_recvmailnum = 0;
  }

  public String getErrorInfo() {
    return this.errDescription;
  }

  public void setMailHost(String paramString)
  {
    this.mailHost = paramString;
  }

  public void setFromAddress(String paramString)
  {
    this.fromAddress = paramString;
  }

  public void setFromAddress(String paramString1, String paramString2)
  {
    this.fromAddress = paramString1;
    this.fromAddressAlias = paramString2;
  }

  public void setUserName(String paramString)
  {
    this.userName = paramString;
  }

  public void setUserPwd(String paramString)
  {
    this.userPwd = paramString;
  }

  public void setToAddress(String paramString)
  {
    this.toAddress = null;
    this.addrAddress = null;
    this.anum = 0;

    if (paramString.indexOf(';') != -1)
    {
      addr3(paramString);
    }
    else
    {
      this.toAddress = paramString;
      this.emailstr = paramString;
      this.addrReady = true;
    }
  }

  public void setToCCAddr(String paramString)
  {
    this.toCCAddr = null;
    this.addrCC = null;
    this.snum = 0;

    if (paramString.indexOf(';') != -1)
    {
      addr1(paramString);
    }
    else
    {
      this.toCCAddr = paramString;
      this.addrReady = true;
    }
  }

  public void setToBCCAddr(String paramString)
  {
    this.toBCCAddr = null;
    this.addrBCC = null;
    this.tnum = 0;

    if (paramString.indexOf(';') != -1)
    {
      addr2(paramString);
    }
    else
    {
      this.toBCCAddr = paramString;
      this.addrReady = true;
    }
  }

  public void setMailSubject(String paramString)
  {
    this.mailSubject = paramString;
  }

  public void setMailBody(String paramString, boolean paramBoolean)
  {
    this.mailBody = paramString;
    this.isHtml = paramBoolean;
  }

  public void setAttachment(String paramString1, String paramString2)
  {
    this.vfile.addElement(paramString1);

    paramString2 = paramString2 == null ? "" : paramString2;
    this.vfileName.addElement(paramString2);
  }

  public void setLogFileUrl(String paramString)
  {
    this.logFileUrl = paramString;
  }

  public void setCharset(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    if ((paramString1 != null) && (!paramString1.equals("")))
    {
      this.bodyCharsetHtml = paramString1;
    }
    if ((paramString2 != null) && (!paramString2.equals("")))
    {
      this.bodyCharsetTxt = paramString2;
    }
    if ((paramString3 != null) && (!paramString3.equals("")))
    {
      this.attachCharset1 = paramString3;
    }
    if ((paramString4 != null) && (!paramString4.equals("")))
    {
      this.attachCharset2 = paramString4;
    }
  }

  public boolean openConnect()
    throws Exception
  {
    try
    {
      this.props = new Properties();

      this.props.put("mail.smtp.host", this.mailHost);
      this.props.put("mail.smtp.auth", "true");
      this.mailSession = Session.getInstance(this.props, new MailAuthenticator(this.userName, this.userPwd));

      this.sessionReady = true;

      return true;
    }
    catch (Exception localException)
    {
      this.errDescription = ("Failed to open a connect to the mail server[" + this.mailHost + "," + this.userName + "]:" + localException.getMessage());
      this.sessionReady = false;
      writeLog(this.errDescription);
      localException.printStackTrace();
    }
    return false;
  }

  public boolean closeConnect()
    throws Exception
  {
    try
    {
      this.sessionReady = false;

      return true;
    }
    catch (Exception localException)
    {
      this.errDescription = ("Failed to close the connect to the mail server[" + this.mailHost + "]:" + localException.getMessage());
      writeLog(this.errDescription);
      localException.printStackTrace();
    }
    return false;
  }

  public void addr1(String paramString)
  {
    int i = 0;

    StringTokenizer localStringTokenizer = new StringTokenizer(paramString, ";");
    this.snum = localStringTokenizer.countTokens();
    try
    {
      this.addrCC = new Address[this.snum];
      while (localStringTokenizer.hasMoreTokens())
      {
        this.addrCC[(i++)] = new InternetAddress(localStringTokenizer.nextToken());
      }

      this.addrReady = true;
    }
    catch (Exception localException)
    {
      this.addrCC = null;
      this.snum = 0;
      this.errDescription = ("Failed to set the cc email addresses for ecipients :" + localException.getMessage());
      this.addrReady = false;
      writeLog(this.errDescription);
    }
  }

  public void addr2(String paramString)
  {
    int i = 0;

    StringTokenizer localStringTokenizer = new StringTokenizer(paramString, ";");
    this.tnum = localStringTokenizer.countTokens();
    try
    {
      this.addrBCC = new Address[this.tnum];
      while (localStringTokenizer.hasMoreTokens())
      {
        this.addrBCC[(i++)] = new InternetAddress(localStringTokenizer.nextToken());
      }

      this.addrReady = true;
    }
    catch (Exception localException)
    {
      this.addrBCC = null;
      this.tnum = 0;
      this.errDescription = ("Failed to set the bcc email addresses for ecipients :" + localException.getMessage());
      this.addrReady = false;
      writeLog(this.errDescription);
    }
  }

  public void addr3(String paramString)
  {
    int i = 0;

    String str = "";
    StringTokenizer localStringTokenizer = new StringTokenizer(paramString, ";");
    this.anum = localStringTokenizer.countTokens();
    try
    {
      this.addrAddress = new Address[this.anum];
      this.emailstr = "";
      while (localStringTokenizer.hasMoreTokens())
      {
        str = localStringTokenizer.nextToken();
        this.addrAddress[(i++)] = new InternetAddress(str);
        if (this.emailstr.equals("") == true)
        {
          this.emailstr = str;
        }
        else
        {
          this.emailstr = (this.emailstr + ";" + str);
        }
      }

      this.addrReady = true;
    }
    catch (Exception localException)
    {
      this.addrAddress = null;
      this.anum = 0;
      this.errDescription = ("Failed to set the email addresses for ecipients :" + localException.getMessage());
      this.addrReady = false;
      writeLog(this.errDescription);
    }
  }

  public boolean sendMail()
  {
    try
    {
      int i = 0;
      InternetAddress localInternetAddress1;
      if (this.fromAddressAlias != null)
      {
        if (!this.fromAddressAlias.equals(""))
        {
          localInternetAddress1 = new InternetAddress(this.fromAddress, MimeUtility.encodeText(this.fromAddressAlias, "UTF-8", "B"));
        }
        else
        {
          localInternetAddress1 = new InternetAddress(this.fromAddress);
        }
      }
      else
      {
        localInternetAddress1 = new InternetAddress(this.fromAddress);
      }

      MimeMessage localMimeMessage = new MimeMessage(this.mailSession);
      MimeMultipart localMimeMultipart = new MimeMultipart();

      localMimeMessage.setFrom(localInternetAddress1);
      localMimeMessage.setSentDate(new Date());

      if ((this.toAddress != null) && (!this.toAddress.equals("")))
      {
        InternetAddress localInternetAddress2 = new InternetAddress(this.toAddress);
        localMimeMessage.setRecipient(Message.RecipientType.TO, localInternetAddress2);
        i = 1;
      }
      else if (this.addrAddress != null)
      {
        localMimeMessage.setRecipients(Message.RecipientType.TO, this.addrAddress);
        i = 1;
      }

      if ((this.toCCAddr != null) && (!this.toCCAddr.equals("")))
      {
        InternetAddress localInternetAddress3 = new InternetAddress(this.toCCAddr);
        localMimeMessage.setRecipient(Message.RecipientType.CC, localInternetAddress3);
        i = 1;
      }
      else if (this.addrCC != null)
      {
        localMimeMessage.setRecipients(Message.RecipientType.CC, this.addrCC);
        i = 1;
      }

      if ((this.toBCCAddr != null) && (!this.toBCCAddr.equals("")))
      {
        InternetAddress localInternetAddress4 = new InternetAddress(this.toBCCAddr, "");
        localMimeMessage.setRecipient(Message.RecipientType.BCC, localInternetAddress4);
        i = 1;
      }
      else if (this.addrBCC != null)
      {
        localMimeMessage.setRecipients(Message.RecipientType.BCC, this.addrBCC);
        i = 1;
      }

      if (this.isHtml)
      {
    	  MimeBodyPart localObject = new MimeBodyPart();

        ((BodyPart)localObject).setContent(this.mailBody, "text/html;charset=" + this.bodyCharsetHtml);

        localMimeMultipart.addBodyPart((BodyPart)localObject);
      }
      else
      {
    	  MimeBodyPart localObject = new MimeBodyPart();

        ((BodyPart)localObject).setContent(this.mailBody, "text/plain;charset=" + this.bodyCharsetTxt);

        localMimeMultipart.addBodyPart((BodyPart)localObject);
      }

      localMimeMessage.setSubject(MimeUtility.encodeText(this.mailSubject, "UTF-8", "B"));

      Object localObject = this.vfile.elements();
      Enumeration localEnumeration = this.vfileName.elements();

      while (((Enumeration)localObject).hasMoreElements())
      {
        MimeBodyPart localMimeBodyPart = new MimeBodyPart();

        this.attachment = ((Enumeration)localObject).nextElement().toString();
        FileDataSource localFileDataSource = new FileDataSource(this.attachment);
        localMimeBodyPart.setDataHandler(new DataHandler(localFileDataSource));

        String str = "";
        if (localEnumeration.hasMoreElements())
        {
          str = localEnumeration.nextElement().toString();
        }
        if (!str.equals(""))
        {
          localMimeBodyPart.setFileName(MimeUtility.encodeText(str, "UTF-8", "B"));
        }
        else
        {
          localMimeBodyPart.setFileName(MimeUtility.encodeText(localFileDataSource.getName(), "UTF-8", "B"));
        }

        localMimeMultipart.addBodyPart(localMimeBodyPart);
      }

      this.vfile.removeAllElements();
      localMimeMessage.setContent(localMimeMultipart);
      localMimeMessage.saveChanges();
      localMimeMessage.setSentDate(new Date());

      if ((i == 1) && (this.addrReady == true) && (this.sessionReady == true))
      {
        this.int_recvmailnum += 1;
        writeLog_recv("Receiver " + this.int_recvmailnum + " Email:" + this.emailstr);

        Transport.send(localMimeMessage);
        this.int_sendmailnum += 1;
        writeLog_send("Send " + this.int_sendmailnum + " Email:" + this.emailstr);

        this.toAddress = null;
        this.toCCAddr = null;
        this.toBCCAddr = null;

        this.addrCC = null;
        this.snum = 0;
        this.addrBCC = null;
        this.tnum = 0;
        this.addrAddress = null;
        this.anum = 0;

        return true;
      }
      if (i == 0)
      {
        this.errDescription = "The email address is not set.";
        writeLog(this.errDescription);
      }
      else if (!this.sessionReady)
      {
        this.errDescription = "The method openConnect must be called before call the method sendMail.";
        writeLog(this.errDescription);
      }

      this.toAddress = null;
      this.toCCAddr = null;
      this.toBCCAddr = null;

      this.addrCC = null;
      this.snum = 0;
      this.addrBCC = null;
      this.tnum = 0;
      this.addrAddress = null;
      this.anum = 0;

      return false;
    }
    catch (Exception localException)
    {
      this.toAddress = null;
      this.toCCAddr = null;
      this.toBCCAddr = null;

      this.addrCC = null;
      this.snum = 0;
      this.addrBCC = null;
      this.tnum = 0;
      this.addrAddress = null;
      this.anum = 0;

      localException.printStackTrace();

      this.errDescription = ("exception occured when send mail:" + localException.getMessage());
      writeLog(this.errDescription);
      localException.printStackTrace();
    }
    return false;
  }

  private void writeLog_recv(String paramString)
  {
    if ((this.logFileUrl != null) && (!this.logFileUrl.equals("")))
    {
      StringBuffer localStringBuffer1 = new StringBuffer();
      String str = "";

      localStringBuffer1.append(new SimpleDateFormat("yyyyMMdd").format(new Date()));
      str = this.logFileUrl + "/recv_mail_" + localStringBuffer1.toString() + ".log";
      str = str.replaceAll("//", "/");

      File localFile = new File(str);
      try
      {
        FileOutputStream localFileOutputStream = new FileOutputStream(localFile, true);
        PrintWriter localPrintWriter = new PrintWriter(localFileOutputStream, true);

        SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        StringBuffer localStringBuffer2 = new StringBuffer();
        localStringBuffer2.append(localSimpleDateFormat.format(new Date()));
        localStringBuffer2.append(" : ");
        localStringBuffer2.append(paramString);

        localPrintWriter.println(localStringBuffer2);
        localPrintWriter.close();
      }
      catch (FileNotFoundException localFileNotFoundException)
      {
        localFileNotFoundException.printStackTrace();
      }
    }
  }

  private void writeLog_send(String paramString)
  {
    if ((this.logFileUrl != null) && (!this.logFileUrl.equals("")))
    {
      StringBuffer localStringBuffer1 = new StringBuffer();
      String str = "";

      localStringBuffer1.append(new SimpleDateFormat("yyyyMMdd").format(new Date()));
      str = this.logFileUrl + "/send_mail_" + localStringBuffer1.toString() + ".log";
      str = str.replaceAll("//", "/");

      File localFile = new File(str);
      try
      {
        FileOutputStream localFileOutputStream = new FileOutputStream(localFile, true);
        PrintWriter localPrintWriter = new PrintWriter(localFileOutputStream, true);

        SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        StringBuffer localStringBuffer2 = new StringBuffer();
        localStringBuffer2.append(localSimpleDateFormat.format(new Date()));
        localStringBuffer2.append(" : ");
        localStringBuffer2.append(paramString);

        localPrintWriter.println(localStringBuffer2);
        localPrintWriter.close();
      }
      catch (FileNotFoundException localFileNotFoundException)
      {
        localFileNotFoundException.printStackTrace();
      }
    }
  }

  private void writeLog(String paramString)
  {
    if ((this.logFileUrl != null) && (!this.logFileUrl.equals("")))
    {
      StringBuffer localStringBuffer1 = new StringBuffer();
      String str = "";

      localStringBuffer1.append(new SimpleDateFormat("yyyyMMdd").format(new Date()));
      str = this.logFileUrl + "/mail_engine_" + localStringBuffer1.toString() + ".log";
      str = str.replaceAll("//", "/");

      File localFile = new File(str);
      try
      {
        FileOutputStream localFileOutputStream = new FileOutputStream(localFile, true);
        PrintWriter localPrintWriter = new PrintWriter(localFileOutputStream, true);

        SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        StringBuffer localStringBuffer2 = new StringBuffer();
        localStringBuffer2.append(localSimpleDateFormat.format(new Date()));
        localStringBuffer2.append(" : ");
        localStringBuffer2.append(paramString);

        localPrintWriter.println(localStringBuffer2);
        localPrintWriter.close();
      }
      catch (FileNotFoundException localFileNotFoundException)
      {
        localFileNotFoundException.printStackTrace();
      }
    }
  }
}