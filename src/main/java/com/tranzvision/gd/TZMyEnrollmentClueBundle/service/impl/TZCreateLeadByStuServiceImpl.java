package com.tranzvision.gd.TZMyEnrollmentClueBundle.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.dao.PsTzXsxsInfoTMapper;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl;
import com.tranzvision.gd.util.cfgdata.GetHardCodePoint;
import com.tranzvision.gd.util.httpclient.CommonUtils;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * EMBA，学员推荐
 * @author yuds
 *
 */
@Service("com.tranzvision.gd.TZMyEnrollmentClueBundle.service.impl.TZCreateLeadByStuServiceImpl")
public class TZCreateLeadByStuServiceImpl  extends FrameworkImpl {
	@Autowired
	private SiteRepCssServiceImpl objRep;
	@Autowired
	private TZGDObject tzGDObject;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private PsTzXsxsInfoTMapper psTzXsxsInfoTMapper;
	@Autowired
	private GetHardCodePoint GetHardCodePoint;
	
	public String tzGetHtmlContent(String strParams) {
		
		//是否为手机访问
		Boolean isMobile = CommonUtils.isMobile(request);	
						
		String strRtn = "";
		try{
			//线索展示默认的机构信息			
			String currentOrgId = GetHardCodePoint.getHardCodePointVal("TZ_LEADS_DEFAULT_ORG");
			//开发过程中使用EMBA招生站点的样式文件
			String strSiteId = GetHardCodePoint.getHardCodePointVal("TZ_EMBA_DEFAULT_SITE");
			//手机版返回方法
			String strMobileBack = GetHardCodePoint.getHardCodePointVal("TZ_EMBA_XSBACK_URL");
			
			String ctxPath = request.getContextPath();
			
			if(isMobile){
				strRtn = tzGDObject.getHTMLText("HTML.TZMyEnrollmentClueBundle.TZ_M_CREATE_LEADS_HTML",ctxPath,currentOrgId,strSiteId,strMobileBack);
			}else{
				strRtn = tzGDObject.getHTMLText("HTML.TZMyEnrollmentClueBundle.TZ_P_CREATE_LEADS_HTML",ctxPath,currentOrgId,strSiteId);
			}
						
			strRtn = objRep.repCss(strRtn, strSiteId);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return strRtn;
	}
	
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "";
		try {
			/*不用了*/
		}catch(Exception e){
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			strRet = "{\"result\":\"创建线索失败，请联系管理员。错误信息为："+e.toString()+"\"}";
			e.printStackTrace();
		}
		return strRet;
	}
}
