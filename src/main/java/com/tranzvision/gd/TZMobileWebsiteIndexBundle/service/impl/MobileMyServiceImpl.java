package com.tranzvision.gd.TZMobileWebsiteIndexBundle.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 清华mba招生手机版我的
 * classid:  mMy
 * @author tang
 *
 */
@Service("com.tranzvision.gd.TZMobileWebsiteIndexBundle.service.impl.MobileMyServiceImpl")
public class MobileMyServiceImpl extends FrameworkImpl {
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TZGDObject tzGDObject;

	/* 手机版招生网站首页 */
	@Override
	public String tzGetHtmlContent(String strParams) {

		String indexHtml = "";
		String title = "我的";
		
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
			//首页
			String indexUrl = ctxPath + "/dispatcher?classid=mIndex&siteId="+siteId;
			//已报名活动;
			String myActivityYetUrl = ctxPath + "/dispatcher?classid=myActivity&siteId="+siteId;
			//系统站内信;
			String znxListUrl = ctxPath+"/dispatcher?classid=znxList&siteId="+siteId+"&lx=back";
			//查看历史报名
			String lsbmUrl = ctxPath + "/dispatcher?classid=mAppHistory&siteId="+siteId;
			//申请奖学金;
			String sqJxjUrlb = ctxPath+"/dispatcher?classid=schlrView&siteId="+siteId+"&oprate=R"; 
			//账户管理;
			String accountMngUrl =  ctxPath + "/dispatcher?classid=phZhgl&siteId="+siteId;
			indexHtml = tzGDObject.getHTMLText("HTML.TZMobileWebsiteIndexBundle.TZ_M_MY_HTML", title, ctxPath,
					"", siteId, "5", myActivityYetUrl,znxListUrl,sqJxjUrlb,accountMngUrl,lsbmUrl,indexUrl);
		} catch (Exception e) {
			e.printStackTrace();
			indexHtml = "";
		}

		return indexHtml;
	}

}
