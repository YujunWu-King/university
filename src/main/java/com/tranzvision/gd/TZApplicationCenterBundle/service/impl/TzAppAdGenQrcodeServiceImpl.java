package com.tranzvision.gd.TZApplicationCenterBundle.service.impl;

import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tranzvision.gd.util.qrcode.CreateQRCode;
import com.tranzvision.gd.util.sql.SqlQuery;

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
			
			String urlStr = request.getScheme() + "://" + request.getServerName() + ":"
						+ String.valueOf(request.getServerPort()) + ctxPath + qrcodeFilePath ;
			this.isImgExist(urlStr, 10);

			qrcodeFilePath=ctxPath+qrcodeFilePath;
		
		}catch (Exception e) {			
			e.printStackTrace();
			return "无法获取相关数据";
		}
		return qrcodeFilePath;
	}
	
	
	private boolean isImgExist(String urlStr, int num){
		try{
			if(num > 0){
				URL url = new URL(urlStr);
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
		        conn.setConnectTimeout(1000); 
		        conn.getInputStream();
		        return true;
			}else{
				return false;
			}
			
		}catch(Exception e){
			num = num -1;
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			isImgExist(urlStr, num);
		}
		
		return false;
		
	}
}

	