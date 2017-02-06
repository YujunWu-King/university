package com.tranzvision.gd.TZApplicationCenterBundle.service.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzWebsiteLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZSitePageBundle.service.impl.TzWebsiteServiceImpl;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.qrcode.CreateQRCode;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 生成录取通知书分享二维码页面
 * 生成录取通知书分享html页面
 * 
 * @author YTT
 * @since 2017-01-16
 */
@Service("com.tranzvision.gd.TZApplicationCenterBundle.service.impl.TzAppAdGenQrcodeServiceImpl")

@RequestMapping(value = { "/admission" })
public class TzAppAdGenQrcodeServiceImpl {
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private SqlQuery sqlQuery;	

	@Autowired
	private TzWebsiteServiceImpl tzWebsiteServiceImpl;
	
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	
	@Autowired
	private TZGDObject tzGDObject;
	
	@Autowired
	private CreateQRCode createQRCode;	
	
	//生成录取通知书二维码
	public String genQrcode(String siteId,String oprid,Long tzAppInsID) {
		String qrcodeFilePath="";
		
		try {
			String language = "", jgId = "", skinId = "";
			String siteSQL = "SELECT TZ_JG_ID,TZ_SITE_LANG,TZ_SKIN_ID FROM PS_TZ_SITEI_DEFN_T WHERE TZ_SITEI_ID=? AND TZ_SITEI_ENABLE='Y'";
			Map<String, Object> siteMap = sqlQuery.queryForMap(siteSQL, new Object[] { siteId });
			if (siteMap != null) {
				language = (String) siteMap.get("TZ_SITE_LANG");
				jgId = (String) siteMap.get("TZ_JG_ID");
				skinId = (String) siteMap.get("TZ_SKIN_ID");
				if (language == null || "".equals(language)) {
					language = "ZHS";
				}
				if (jgId == null || "".equals(jgId)) {
					jgId = "ADMIN";
				}
			}
				
			String qrcodeFileName = "TZ_ADMISSION_PREVIEW_" + siteId + "_" + oprid + "_" + tzAppInsID + ".png";
				
			String ctxPath = request.getContextPath();
	
			String qrcodeUrl = request.getScheme() + "://" + request.getServerName() + ":"
						+ String.valueOf(request.getServerPort()) + ctxPath + "/event/preview/m/" + siteId + "/" + oprid
						+ "/" + tzAppInsID;
	
			qrcodeFilePath = createQRCode.encodeQRCode(jgId, qrcodeUrl, qrcodeFileName);
			qrcodeFilePath="/university"+qrcodeFilePath;
		
		}catch (Exception e) {			
			e.printStackTrace();
			return "无法获取相关数据";
		}
		return qrcodeFilePath;
	}
}

	