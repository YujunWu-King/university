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
 * 生成录取通知书分享二维码
 * 
 * @para 报名表编号
 * @ret  录取通知书二维码图片地址
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
	public String genQrcode(Long tzAppInsID) {
		String qrcodeFilePath="";
		String siteId="";
		String oprid="";
		String jgId="";
		
		try {
			//oprid
			String opridSql="SELECT ROW_ADDED_OPRID FROM PS_TZ_APP_INS_T WHERE TZ_APP_INS_ID=?";
			oprid= sqlQuery.queryForObject(opridSql, new Object[] {tzAppInsID}, "String");
			//站点
			String siteIdSql="SELECT TZ_SITEI_ID FROM PS_TZ_REG_USER_T WHERE OPRID=?";
			siteId= sqlQuery.queryForObject(siteIdSql, new Object[] {oprid}, "String");
			//机构
			String jgIdSql="SELECT TZ_JG_ID FROM PS_TZ_SITEI_DEFN_T WHERE TZ_SITEI_ID=? AND TZ_SITEI_ENABLE='Y'";
			jgId= sqlQuery.queryForObject(jgIdSql, new Object[] {siteId}, "String");
				
			String qrcodeFileName = "TZ_ADMISSION_PREVIEW_" + siteId + "_" + oprid + "_" + tzAppInsID + ".png";
				
			String ctxPath = request.getContextPath();
	
			String qrcodeUrl = request.getScheme() + "://" + request.getServerName() + ":"
						+ String.valueOf(request.getServerPort()) + ctxPath + "/admission/" +jgId+"/" + siteId + "/" + oprid
						+ "/" + tzAppInsID;
	
			qrcodeFilePath = createQRCode.encodeQRCode(jgId, qrcodeUrl, qrcodeFileName,200,200);
			qrcodeFilePath=ctxPath+qrcodeFilePath;
		
		}catch (Exception e) {			
			e.printStackTrace();
			return "无法获取相关数据";
		}
		return qrcodeFilePath;
	}
}

	