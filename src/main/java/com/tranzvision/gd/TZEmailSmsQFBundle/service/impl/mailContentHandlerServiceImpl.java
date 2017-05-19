package com.tranzvision.gd.TZEmailSmsQFBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZEmailSmsQFBundle.dao.PsTzLinkParTblMapper;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzLinkParTbl;
import com.tranzvision.gd.util.encrypt.DESUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * TZ_SMSMAL_QF_PKG:mailContentHandler
 * 
 * @author Administrator
 *
 */
@Service("com.tranzvision.gd.TZEmailSmsQFBundle.service.impl.mailContentHandlerServiceImpl")
public class mailContentHandlerServiceImpl {
	@Autowired
	private PsTzLinkParTblMapper psTzLinkParTblMapper;
	@Autowired
	private SqlQuery jdbcTemplate;

	/* 邮件内容中的链接替换为服务端中转链接 */
	public String substituteLinkHref(String strPicID, String taskId, String emailContent, int strat) {
		int startIndex = 0;
		int endIndex = 0;
		String strUrl = "";
		/* 查找<a>开头位置 */
		startIndex = emailContent.indexOf("<a ", strat);
		if (startIndex >= 0) {
			/* 该<a>标签对应结尾标签位置 */
			endIndex = emailContent.indexOf("</a>", startIndex);
			if (endIndex > startIndex) {
				// 取<a>标签链接字符串;
				String strLink = emailContent.substring(startIndex, endIndex);

				/* 链接url的开始结束位置 */
				int urlPosit1 = 0, urlPosit2 = 0;
				int hrefPosition = strLink.indexOf("href=");
				if ("\"".equals(strLink.substring(hrefPosition + 5, hrefPosition + 6))) {
					urlPosit1 = hrefPosition + 6;
					urlPosit2 = strLink.indexOf("\"", urlPosit1);
				}

				if ("'".equals(strLink.substring(hrefPosition + 5, hrefPosition + 6))) {
					urlPosit1 = hrefPosition + 6;
					urlPosit2 = strLink.indexOf("'", urlPosit1);
				}

				if (urlPosit1 > 0 && urlPosit2 > 0 && urlPosit2 > urlPosit1) {
					/* 链接目标网址URL */
					strUrl = strLink.substring(urlPosit1, urlPosit2);
					// 将链接参数存储在邮件内容链接参数存储表中;
					PsTzLinkParTbl psTzLinkParTbl = new PsTzLinkParTbl();
					SimpleDateFormat datetimeFormate = new SimpleDateFormat("yyyyMMddHHmmss");
					String link_key = this.getRandomString(15) + datetimeFormate.format(new Date());
					psTzLinkParTbl.setTzLinkpraId(link_key);
					psTzLinkParTbl.setTzLinkType("CLICK");
					psTzLinkParTbl.setTzMlsmQfpcId(strPicID);
					psTzLinkParTbl.setTzEmlSmsTaskId(taskId);
					psTzLinkParTbl.setTzRedtUrl(strUrl);
					psTzLinkParTbl.setTzAddDttm(new Date());
					psTzLinkParTblMapper.insert(psTzLinkParTbl);
					/* 服务器中转链接URL */
					String newTransUrl = "";

					String sql = "select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT='TZ_ZSGL_ISCRIPT_URL'";
					newTransUrl = jdbcTemplate.queryForObject(sql, "String");
					if (newTransUrl == null || "".equals(newTransUrl)) {
						newTransUrl = "HARDCODE点：TZ_ZSGL_ISCRIPT_URL未定义";
					}
					newTransUrl = newTransUrl + "?classid=mailLinkHandler&params=" + link_key;
					newTransUrl = newTransUrl + "_[邮件群发.听众成员编号占位符]";
					
					String strNewLink = strLink.replace(strUrl, newTransUrl);
					String newMailContent = emailContent.replace(strLink, strNewLink);

					emailContent = this.substituteLinkHref(strPicID, taskId, newMailContent, endIndex);
				}

			}
		}
		return emailContent;

	}

	/* 添加取消订阅链接 */
	public String addCancelDyLink(String strPicID, String taskId) {
		String strCancelLink = "";

		strCancelLink = strCancelLink
				+ "<div style='text-align:center;font-size: 12px;background: #F8F8F8;padding: 10px;'>不想再次收到此类邮件，请";
		strCancelLink = strCancelLink + "<a href=\"";

		// 将链接参数存储在邮件内容链接参数存储表中;
		PsTzLinkParTbl psTzLinkParTbl = new PsTzLinkParTbl();
		SimpleDateFormat datetimeFormate = new SimpleDateFormat("yyyyMMddHHmmss");
		String link_key = this.getRandomString(15) + datetimeFormate.format(new Date());
		psTzLinkParTbl.setTzLinkpraId(link_key);
		psTzLinkParTbl.setTzLinkType("CANCEL");
		psTzLinkParTbl.setTzMlsmQfpcId(strPicID);
		psTzLinkParTbl.setTzEmlSmsTaskId(taskId);
		psTzLinkParTbl.setTzAddDttm(new Date());
		psTzLinkParTblMapper.insert(psTzLinkParTbl);

		/* 服务器中转链接URL */
		String cancelUrl = "";

		String sql = "select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT='TZ_ZSGL_ISCRIPT_URL'";
		cancelUrl = jdbcTemplate.queryForObject(sql, "String");
		if (cancelUrl == null || "".equals(cancelUrl)) {
			cancelUrl = "HARDCODE点：TZ_ZSGL_ISCRIPT_URL未定义";
		}
		cancelUrl = cancelUrl + "?classid=mailLinkHandler&params=" + link_key;
		cancelUrl = cancelUrl + "_[邮件群发.听众成员编号占位符]";

		strCancelLink = strCancelLink + cancelUrl;
		strCancelLink = strCancelLink + "\">点此退订</a>";
		strCancelLink = strCancelLink + "该邮件</div>";

		return strCancelLink;
	}
	
	/*邮件内容预处理*/
	public String emailConPreprocess(String strPicID , String taskId){
		String mailContent = "";
		String strQxdyFlag = ""; /*取消订阅标识*/
		String strEdmFlag = ""; /*启用EDM统计标识*/
		
		String sql = "SELECT TZ_MAL_CONTENT,TZ_QXDY_FLAG,TZ_EDM_FLAG FROM PS_TZ_DXYJQF_DY_T WHERE TZ_MLSM_QFPC_ID=?";   
		Map<String, Object> map = jdbcTemplate.queryForMap(sql,new Object[]{strPicID});
		if(map != null){
			mailContent = map.get("TZ_MAL_CONTENT") == null ? "" : (String)map.get("TZ_MAL_CONTENT");
			strQxdyFlag = map.get("TZ_QXDY_FLAG") == null ? "" : (String)map.get("TZ_QXDY_FLAG");
			strEdmFlag = map.get("TZ_EDM_FLAG") == null ? "" : (String)map.get("TZ_EDM_FLAG");
		}
		
		if("Y".equals(strEdmFlag)){
			/*邮件内容中所有链接替换为中转链接*/
		    mailContent = this.substituteLinkHref(strPicID, taskId, mailContent, 0);
		    
			// 将链接参数存储在邮件内容链接参数存储表中;
			PsTzLinkParTbl psTzLinkParTbl = new PsTzLinkParTbl();
			SimpleDateFormat datetimeFormate = new SimpleDateFormat("yyyyMMddHHmmss");
			String link_key = this.getRandomString(15) + datetimeFormate.format(new Date());
			psTzLinkParTbl.setTzLinkpraId(link_key);
			psTzLinkParTbl.setTzLinkType("OPEN");
			psTzLinkParTbl.setTzMlsmQfpcId(strPicID);
			psTzLinkParTbl.setTzEmlSmsTaskId(taskId);
			psTzLinkParTbl.setTzAddDttm(new Date());
			psTzLinkParTblMapper.insert(psTzLinkParTbl);
			
			/*记录打开邮件图标*/
			String sql2 = "select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT='TZ_ZSGL_ISCRIPT_URL'";
			String openUrl = jdbcTemplate.queryForObject(sql2, "String");
			if (openUrl == null || "".equals(openUrl)) {
				openUrl = "HARDCODE点：TZ_ZSGL_ISCRIPT_URL未定义";
			}
			openUrl = openUrl + "?classid=mailLinkHandler&params=" + link_key;
			openUrl = openUrl + "_[邮件群发.听众成员编号占位符]";
			
			String openImgHtml = "<img src=\"" + openUrl + "\" width='1' height='1'/>";
		      
		    mailContent = mailContent + openImgHtml;
		    
		}
		
		/*邮件内容包含取消订阅链接*/
		String cancelLink = "";
		if("Y".equals(strQxdyFlag)){
			cancelLink = this.addCancelDyLink(strPicID, taskId);
		}
		   
		/*邮件批次标识，退信引擎解析*/
		String strPicID2 = DESUtil.encrypt(strPicID, "Tranzvision_Mail");
		String strQfpc_ID_BZ = "<p style='display:none'>【TRANZVISION_YJQF_PC_ID_BZ】【" + strPicID2 + "】【[TRANZVISION_YJQF_PC_ID_BZ.邮件群发.加密收件邮箱地址]】<p>";
		mailContent = mailContent + strQfpc_ID_BZ + cancelLink;
		  
		return mailContent;
	}
	   

	public String getRandomString(int len) {
		String strDefn = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		String strRtn = "";

		for (int i = 0; i < len; i++) {
			int posi = (int) Math.floor(Math.random() * 52);
			strRtn = strRtn + strDefn.substring(posi, posi + 1);

		}
		return strRtn;
	}

}
