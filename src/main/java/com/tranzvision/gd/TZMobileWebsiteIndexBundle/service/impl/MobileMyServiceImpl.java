package com.tranzvision.gd.TZMobileWebsiteIndexBundle.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 娓呭崕mba鎷涚敓鎵嬫満鐗堟垜鐨� classid: mMy
 * 
 * @author tang
 *
 */
@Service("com.tranzvision.gd.TZMobileWebsiteIndexBundle.service.impl.MobileMyServiceImpl")
public class MobileMyServiceImpl extends FrameworkImpl {
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TZGDObject tzGDObject;
	@Autowired
	private SqlQuery sqlQuery;
	/* 鎵嬫満鐗堟嫑鐢熺綉绔欓椤� */
	@Override
	public String tzGetHtmlContent(String strParams) {

		String indexHtml = "";
		String title = "鎴戠殑";

		String ctxPath = request.getContextPath();

		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);
		String siteId = "";
		if (jacksonUtil.containsKey("siteId")) {
			siteId = jacksonUtil.getString("siteId");
		} else {
			siteId = request.getParameter("siteId");
		}

		try {
			// 棣栭〉
			String indexUrl = ctxPath + "/dispatcher?classid=mIndex&siteId=" + siteId;
			// 宸叉姤鍚嶆椿鍔�;
			String myActivityYetUrl = ctxPath + "/dispatcher?classid=myActivity&siteId=" + siteId + "&lx=back";
			// 绯荤粺绔欏唴淇�;
			String znxListUrl = ctxPath + "/dispatcher?classid=znxList&siteId=" + siteId + "&lx=back";
			// 鏌ョ湅鍘嗗彶鎶ュ悕
			String lsbmUrl = ctxPath + "/dispatcher?classid=mAppHistory&siteId=" + siteId;
			// 鐢宠濂栧閲�;
			String sqJxjUrlb = ctxPath + "/dispatcher?classid=schlrView&siteId=" + siteId + "&oprate=R";
			// 璐︽埛绠＄悊;
			String accountMngUrl = ctxPath + "/dispatcher?classid=phZhgl&siteId=" + siteId;
			String JGID = sqlQuery.queryForObject("select TZ_JG_ID from PS_TZ_SITEI_DEFN_T WHERE TZ_SITEI_ID=?",new Object[]{siteId},"String");
			
			if (JGID.equals("SEM")) {
				JGID="";
			} else {
				JGID.toLowerCase();
			}
			
			indexHtml = tzGDObject.getHTMLTextForDollar("HTML.TZMobileWebsiteIndexBundle.TZ_M_MY_HTML", title, ctxPath,
					"", siteId, "5", myActivityYetUrl, znxListUrl, sqJxjUrlb, accountMngUrl, lsbmUrl, indexUrl,JGID);
		} catch (Exception e) {
			e.printStackTrace();
			indexHtml = "";
		}

		return indexHtml;
	}

}
