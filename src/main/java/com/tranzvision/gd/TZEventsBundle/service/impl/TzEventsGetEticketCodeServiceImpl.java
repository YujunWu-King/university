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
import com.tranzvision.gd.util.sql.TZGDObject;
/***
 * 生产活动签到码二维码
 * @author tzhjl
 *
 */

public  class TzEventsGetEticketCodeServiceImpl   {
	private String charset = "UTF-8";
	
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
			
			ticketCode=this.encodeQRCode(encoderContent, fileName,charset, 150, 150);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		return ticketCode;  
		
	}
	
	
	
	/**
	 * 完整参数
	 * @param qrCodeInfo
	 * @param fileName
	 * @param charSet
	 * @param qrCodeWidth
	 * @param qrCodeHeight
	 * @return String 二维码图片访问路径
	 */
	public String encodeQRCode(String qrCodeInfo, String fileName, String charSet, int qrCodeWidth,int qrCodeHeight) {
		String strReturn = "";
		
		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		TZGDObject tzGDObject = (TZGDObject) getSpringBeanUtil.getSpringBeanByID("TZGDObject");

		String webPath = tzGDObject.getWebAppRootPath();
		String accessPath = "statics" + File.separator + "download" + File.separator + "events" + File.separator + "qrcode";
		
		String filePath = webPath + accessPath;
		File dir = new File(filePath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		
		String qrcodeFilePath = filePath + File.separator + fileName;

//		Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
//		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		
		Map<EncodeHintType, Object> hintMap = new HashMap<EncodeHintType, Object>();
		//设置白边
		hintMap.put(EncodeHintType.MARGIN, 1);
		//指定编码格式    
		hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		// 指定纠错等级    
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

		try {
//			QRCode.createQRCode(qrCodeInfo, qrcodeFilePath, charSet, hintMap, qrCodeWidth, qrCodeHeight);
			QRCode.createQRCode2(qrCodeInfo, qrcodeFilePath, charSet, hintMap, qrCodeWidth, qrCodeHeight);
			
			strReturn = File.separator + accessPath + File.separator + fileName;
			
		} catch (WriterException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return strReturn;
	}

}
