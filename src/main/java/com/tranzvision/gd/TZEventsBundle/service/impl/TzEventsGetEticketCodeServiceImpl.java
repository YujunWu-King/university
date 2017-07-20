package com.tranzvision.gd.TZEventsBundle.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.qrcode.QRCode;
import com.tranzvision.gd.util.sql.SqlQuery;
/***
 * 生产活动签到码二维码
 * @author tzhjl
 *
 */

public  class TzEventsGetEticketCodeServiceImpl   {
	private String charset = "UTF-8";
	
	private static String TZ_ACTCODE_REPATCH="";
	
	private static String TZ_ACTCODE_TUREPATCH="";
	
	private static String TZ_COMTENTURL="";
	
	/***
	 * @param actId 活动ID
	 * @param actQdid 活动签到码ID
	 * @return 生产二维码的路径 
	 */
	public String getHdTicketCode( String actId ,String actQdid) {
		String ticketCode="";
		try {
			
			//二维码图片名称
			String fileName="TZEVENTS_"+actId+'_'+actQdid+".png";
			//二维码图片 签到码内容
			String encoderContent = "{\"QDM\":"+actQdid+"}";
			
			ticketCode=this.encodeQRCode("SEM", encoderContent, fileName,charset, 200, 200);
			
			System.out.println("ticketCode==="+ticketCode);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		return ticketCode;  
		
	}
	
	
	
	/**
	 * 完整参数
	 * 
	 * @param orgid
	 * @param qrCodeInfo
	 * @param fileName
	 * @param charSet
	 * @param qrCodeWidth
	 * @param qrCodeHeight
	 * @return String 二维码图片访问路径
	 */
	public String encodeQRCode(String orgid, String qrCodeInfo, String fileName, String charSet, int qrCodeWidth,
			int qrCodeHeight) {
		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		SqlQuery sqlQuery = (SqlQuery) getSpringBeanUtil.getAutowiredSpringBean("SqlQuery");

		String strReturn="";
		if (null == orgid || "".equals(orgid)) {
			return "";
		}				
		String hardcodeSql = "select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT where TZ_HARDCODE_PNT=?";
		//相对路径
		TZ_ACTCODE_REPATCH = sqlQuery.queryForObject(hardcodeSql, new Object[] { "TZ_ACTCODE_REPATCH" },
				"String");
		//服务器路径
		TZ_ACTCODE_TUREPATCH = sqlQuery.queryForObject(hardcodeSql, new Object[] { "TZ_ACTCODE_TUREPATCH" },
				"String");
		//访问路径
		TZ_COMTENTURL = sqlQuery.queryForObject(hardcodeSql, new Object[] { "TZ_COMTENTURL" },
				"String");
		//String qrcodeFilePath = "/statics/images/website/qrcode/" + orgid.toLowerCase();
		String qrcodeFilePath = TZ_ACTCODE_REPATCH + orgid.toLowerCase();
		//String dirRealPath = "F:\\qhjg_2\\tzgd_s_java_com\\src\\mainwebapp"+qrcodeFilePath;
		String dirRealPath = TZ_ACTCODE_TUREPATCH+qrcodeFilePath;
		File dir = new File(dirRealPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		
		qrcodeFilePath = dirRealPath + "/" + fileName;
				
		String fileRealPath = qrcodeFilePath;

		Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();

		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

		try {
			QRCode.createQRCode(qrCodeInfo, fileRealPath, charSet, hintMap, qrCodeWidth, qrCodeHeight);
			
			strReturn=TZ_COMTENTURL+TZ_ACTCODE_REPATCH+orgid.toLowerCase()+"/"+fileName;
			
		} catch (WriterException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return strReturn;
	}

}
