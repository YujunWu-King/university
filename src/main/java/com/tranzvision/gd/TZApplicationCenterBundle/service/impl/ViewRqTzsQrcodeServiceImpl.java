package com.tranzvision.gd.TZApplicationCenterBundle.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.sql.TZGDObject;

/*显示录取通知书二维码*/
@Service("com.tranzvision.gd.TZApplicationCenterBundle.service.impl.ViewRqTzsQrcodeServiceImpl")
public class ViewRqTzsQrcodeServiceImpl extends FrameworkImpl {
	@Autowired
	private TzAppAdGenQrcodeServiceImpl tzAppAdGenQrcodeServiceImpl;
	@Autowired
	private TZGDObject tzGDObject;
	@Autowired
	private HttpServletRequest request;
	
	@Override
	public String tzGetHtmlContent(String strParams) {
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		jacksonUtil.json2Map(strParams);
		Long tzAppInsID = Long.parseLong(String.valueOf(jacksonUtil.getString("appIns")));
				
		String qrcodeUrl = tzAppAdGenQrcodeServiceImpl.genQrcode(tzAppInsID);

		String QrcodeHtml = "";
		try {
			String ctxPath = request.getContextPath();
			QrcodeHtml = tzGDObject.getHTMLText("HTML.TZApplicationCenterBundle.TZ_RQTZS_QRCODE_HTML",qrcodeUrl,ctxPath);
		} catch (TzSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return QrcodeHtml;
		
	}
}
