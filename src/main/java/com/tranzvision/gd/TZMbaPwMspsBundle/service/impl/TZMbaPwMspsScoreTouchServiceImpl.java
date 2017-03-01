package com.tranzvision.gd.TZMbaPwMspsBundle.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiDefnT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.sql.TZGDObject;

@Service("com.tranzvision.gd.TZMbaPwMspsBundle.service.impl.TZMbaPwMspsScoreTouchServiceImpl")
public class TZMbaPwMspsScoreTouchServiceImpl extends FrameworkImpl{
	
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TZGDObject tzGdObject;

	@Override
	public String tzGetHtmlContent(String strParams) {
		String strRet = "";
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			jacksonUtil.json2Map(strParams);
			
			String strClassId = jacksonUtil.getString("classId");
			String strBatchId = jacksonUtil.getString("batchId");
			String bmbId = jacksonUtil.getString("bmbId");
			
			try {
				String contextPath = request.getContextPath();
				String commonUrl = contextPath + "/dispatcher";
				
				//返回考生评审页面URL
				String backToKspsUrl = "";
				//返回首页URL
				String backToHomeUrl = "";
				//退出URL
				String logoutUrl = "";
				//修改密码URL
				String modifyPswdUrl = "";
				
				//批次班级名称
				String batchClassName = "";
				
				String examineeSqh = "";
				String examineeName = "";
				String examineeBmbId = bmbId;
				
				String iframeId = "bmb_iframe_"+strBatchId+"_"+bmbId;
				String iframeDivId = "div_"+iframeId;
				
				String bmbUrl = commonUrl
						+ "?tzParams={\"ComID\":\"TZ_ONLINE_REG_COM\",\"PageID\":\"TZ_ONLINE_APP_STD\",\"OperateType\":\"HTML\",\"comParams\":{\"TZ_APP_INS_ID\":\""
						+ bmbId + "\"}}";
				
				strRet = tzGdObject.getHTMLText("HTML.TZInterviewReviewTouchBundle.TZ_MS_DF_TC_HTML", contextPath,commonUrl,backToKspsUrl,backToHomeUrl,logoutUrl,modifyPswdUrl
						,examineeSqh,examineeName,examineeBmbId,strClassId,strBatchId,iframeId,iframeDivId,bmbUrl);
			} catch (TzSystemException e) {
				e.printStackTrace();
				strRet = "【TZ_MS_DF_TC_HTML】html对象未定义";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return strRet;
	}
}
