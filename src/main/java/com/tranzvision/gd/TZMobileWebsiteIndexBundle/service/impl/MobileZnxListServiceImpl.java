package com.tranzvision.gd.TZMobileWebsiteIndexBundle.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.encrypt.DESUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 清华mba招生手机版系统站内信列表
 * 
 * @author 琚峰 classid: znxList
 */
@Service("com.tranzvision.gd.TZMobileWebsiteIndexBundle.service.impl.MobileZnxListServiceImpl")
public class MobileZnxListServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TZGDObject tzGDObject;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	// 清华mba招生手机版系统消息列表
	@Override
	public String tzGetHtmlContent(String strParams) {
		// rootPath;
		String ctxPath = request.getContextPath();

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);
		String siteId = "";
		if (jacksonUtil.containsKey("siteId")) {
			siteId = jacksonUtil.getString("siteId");
		} else {
			siteId = request.getParameter("siteId");
		}

		String menuId = "";
		if (jacksonUtil.containsKey("menuId")) {
			menuId = jacksonUtil.getString("menuId");
		} else {
			menuId = request.getParameter("menuId");
		}
		if (menuId == null || "".equals(menuId)) {
			menuId = "1";
		}

		String lx = "";
		if (jacksonUtil.containsKey("lx")) {
			lx = jacksonUtil.getString("lx");
		} else {
			lx = request.getParameter("lx");
		}

		String content = "";
		String title = "站内信";
		try {
			// css和js
			String jsCss = tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_ZNX_TZ_LIST_JS_CSS", ctxPath,
					siteId);
			// 跳转首页url
			String indexUrl = ctxPath + "/dispatcher?classid=mIndex&siteId=" + siteId;
			if ("back".equals(lx)) {
				indexUrl = "javascript:history.back(-1);";
			}
			//content = tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_MY_ZNX_LIST", title, indexUrl, "");
			content = tzGDObject.getHTMLTextForDollar("HTML.TZMobileWebsiteIndexBundle.TZ_M_ZNX_LIST_HTML", title, ctxPath,
					jsCss, siteId, menuId, title, indexUrl, content);
		} catch (TzSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return content;
	}

	@Override
	public String tzOther(String oprType, String strParams, String[] errorMsg) {

		Map<String, Object> returnMap = new HashMap<>();
		returnMap.put("resultNum", 0);
		returnMap.put("lastMsgId", "");
		returnMap.put("result", "");
		int resultNum = 0;

		JacksonUtil jacksonUtil = new JacksonUtil();
		String tmpPwdKey = "TZGD_@_!_*_StationLetter_20170821_Tranzvision";
		jacksonUtil.json2Map(strParams);
		String siteId = "";
		int pagenum = 0;
		String lastMsgId = "";
		if (jacksonUtil.containsKey("siteId")) {
			siteId = jacksonUtil.getString("siteId");
			lastMsgId = jacksonUtil.getString("lastMsgId");
			try {
				pagenum = jacksonUtil.getInt("pagenum");
			} catch (Exception e) {
				pagenum = 1;
			}
			
			try{
				String encryptMailId = DESUtil.decrypt(lastMsgId, tmpPwdKey);
				if(encryptMailId == null){
					throw new Exception("站内信不存在");
				}
				
				String mailIdArr[] = encryptMailId.split("==");
				if(mailIdArr.length != 2 && mailIdArr[0] != mailIdArr[1]){
					throw new Exception("站内信不存在");
				}else{
					lastMsgId = mailIdArr[0];
				}
			}catch(Exception ee){
				ee.printStackTrace();
			}
		}

		// 当前登录人;
		String m_curOPRID = tzLoginServiceImpl.getLoginedManagerOprid(request);

		String content = "";
		try {
			int limit = 10;
			//int startNum = (pagenum - 1) * limit;

			//String znxSql = "SELECT A.TZ_ZNX_MSGID,A.TZ_ZNX_STATUS,DATE_FORMAT(B.ROW_ADDED_DTTM,'%Y-%m-%d %k:%i')TZ_SEND_TIME,(SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID = B.TZ_ZNX_SENDID)TZ_ZNX_SENDNAME,B.TZ_MSG_SUBJECT,A.TZ_MSG_TEXT FROM PS_TZ_ZNX_REC_T A,PS_TZ_ZNX_MSG_T B WHERE A.TZ_ZNX_MSGID = B.TZ_ZNX_MSGID AND A.TZ_REC_DELSTATUS <> 'Y' AND A.TZ_ZNX_RECID = ? ORDER BY B.ROW_ADDED_DTTM DESC LIMIT ?,?";
			String znxSql = "SELECT A.TZ_ZNX_MSGID,A.TZ_ZNX_STATUS,DATE_FORMAT(B.ROW_ADDED_DTTM,'%Y-%m-%d %k:%i:%s')TZ_SEND_TIME,(SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID = B.TZ_ZNX_SENDID)TZ_ZNX_SENDNAME,B.TZ_MSG_SUBJECT,A.TZ_MSG_TEXT FROM PS_TZ_ZNX_REC_T A,PS_TZ_ZNX_MSG_T B WHERE A.TZ_ZNX_MSGID = B.TZ_ZNX_MSGID AND A.TZ_REC_DELSTATUS <> 'Y' AND A.TZ_ZNX_RECID = ? ORDER BY B.ROW_ADDED_DTTM DESC LIMIT ?";
			String znxSql2 = "SELECT A.TZ_ZNX_MSGID,A.TZ_ZNX_STATUS,DATE_FORMAT(B.ROW_ADDED_DTTM,'%Y-%m-%d %k:%i:%s')TZ_SEND_TIME,(SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID = B.TZ_ZNX_SENDID)TZ_ZNX_SENDNAME,B.TZ_MSG_SUBJECT,A.TZ_MSG_TEXT FROM PS_TZ_ZNX_REC_T A,PS_TZ_ZNX_MSG_T B WHERE A.TZ_ZNX_MSGID = B.TZ_ZNX_MSGID AND A.TZ_REC_DELSTATUS <> 'Y' AND A.TZ_ZNX_RECID = ? and convert(A.TZ_ZNX_MSGID,SIGNED) < convert(?,SIGNED) ORDER BY B.ROW_ADDED_DTTM DESC LIMIT ?";
			
			List<Map<String, Object>> list = null;
			if(pagenum == 1){
				list = sqlQuery.queryForList(znxSql,new Object[] { m_curOPRID, limit });
			}else{
				list = sqlQuery.queryForList(znxSql2,new Object[] { m_curOPRID, lastMsgId, limit });
			}
			 
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					// 消息id;
					String znxMsgId = String.valueOf(list.get(i).get("TZ_ZNX_MSGID"));
					// 发送时间;
					String sendTime = String.valueOf(list.get(i).get("TZ_SEND_TIME"));
					// 发件人姓名
					//String sendName = String.valueOf(list.get(i).get("TZ_ZNX_SENDNAME"));
					// 消息查看状态;
					String znxStatus = String.valueOf(list.get(i).get("TZ_ZNX_STATUS"));
					// 消息主题
					String znxSubject = String.valueOf(list.get(i).get("TZ_MSG_SUBJECT"));
					// 消息内容
					//String msgText = String.valueOf(list.get(i).get("TZ_MSG_TEXT"));
					String znxStyle = "";
					// znxStatus:N-未读
					if ("N".equals(znxStatus)) {
						znxStyle = "";
					} else {
						znxStyle = "newz_read";
					}
					
					//站内信ID加密
					znxMsgId = DESUtil.encrypt(znxMsgId + "==" + znxMsgId, tmpPwdKey);
					content = content 
							+ tzGDObject.getHTMLTextForDollar("HTML.TZMobileWebsiteIndexBundle.TZ_M_MY_SYSINFO_DIV",
									 znxMsgId, znxStyle, znxSubject, sendTime);
					
					if(i == list.size()-1){
						returnMap.replace("lastMsgId", znxMsgId);
					}
					resultNum = resultNum + 1;
				}
			}
		} catch (TzSystemException e) {
			// TODO Auto-generated catch block
			content = "";
			e.printStackTrace();
		}

		returnMap.replace("resultNum", resultNum);
		returnMap.replace("result", content);

		return jacksonUtil.Map2json(returnMap);
	}

	/* 更新站内信状态 */
	@Override
	public String tzUpdate(String[] znxData, String[] errMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("result", "success");
		mapRet.put("message", "删除成功");
		
		String strMailId = "";
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		JacksonUtil jacksonUtil = new JacksonUtil();
		String tmpPwdKey = "TZGD_@_!_*_StationLetter_20170821_Tranzvision";
		
		// 若参数为空，直接返回;
		if (znxData == null || znxData.length == 0) {
			mapRet.replace("result", "error");
			mapRet.replace("message", "删除失败，参数为空");
			
			return jacksonUtil.Map2json(mapRet);
		}
		try {
			int num = 0;
			int notDelCount = 0;
			for (num = 0; num < znxData.length; num++) {
				jacksonUtil.json2Map(znxData[num]);
				//strMailId = jacksonUtil.getString("mailId");
				
				String encryptMailId = jacksonUtil.getString("mailId");
				try{
					encryptMailId = DESUtil.decrypt(encryptMailId, tmpPwdKey);
					if(encryptMailId == null){
						throw new Exception("站内信不存在");
					}
					
					String mailIdArr[] = encryptMailId.split("==");
					if(mailIdArr.length != 2 && mailIdArr[0] != mailIdArr[1]){
						throw new Exception("站内信不存在");
					}else{
						strMailId = mailIdArr[0];
					}
				}catch(Exception ee){
					notDelCount ++;
					continue;
				}
				
				String znxStatusSql = "select TZ_ZNX_STATUS from PS_TZ_ZNX_REC_T WHERE TZ_ZNX_MSGID = ? and TZ_ZNX_RECID=?";
				String znxStatus = sqlQuery.queryForObject(znxStatusSql, new Object[] { strMailId, oprid }, "String");
				znxStatus = znxStatus == null ? "" : znxStatus;
				if (znxStatus.equals("N")) {
					String updateStatusSql = "UPDATE PS_TZ_ZNX_REC_T SET TZ_ZNX_STATUS = 'Y' WHERE TZ_ZNX_MSGID = ? and TZ_ZNX_RECID=?";
					int rtn = jdbcTemplate.update(updateStatusSql, new Object[] { strMailId, oprid });
					if(rtn <= 0){
						notDelCount ++;
					}
				}
			}
			
			if(notDelCount > 0){
				mapRet.replace("result", "error");
				mapRet.replace("message", "站内信删除失败。");
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(mapRet);
	}
}
