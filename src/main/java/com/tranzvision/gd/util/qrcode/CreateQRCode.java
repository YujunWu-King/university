/**
 * 
 */
package com.tranzvision.gd.util.qrcode;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;

/**
 * 生成二维码图片
 * 
 * @author SHIHUA
 * @since 2016-02-17
 */
@Service
public class CreateQRCode {

	@Autowired
	HttpServletRequest request;

	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	private String charset = "UTF-8";

	private int width = 100;

	private int height = 100;

	/**
	 * 完整参数
	 * 
	 * @param orgid
	 * @param qrCodeInfo
	 * @param fileName
	 * @param charSet
	 * @param qrCodeWidth
	 * @param qrCodeHeight
	 * @return String 二维码图片绝对路径
	 */
	public String encodeQRCode(String orgid, String qrCodeInfo, String fileName, String charSet, int qrCodeWidth,
			int qrCodeHeight) {

		if (null == orgid || "".equals(orgid)) {
			return "";
		}

		String qrcodeFilePath = getSysHardCodeVal.getWebsiteImgPath() + "/qrcode/" + orgid.toLowerCase();
		String dirRealPath = request.getServletContext().getRealPath(qrcodeFilePath);
		File dir = new File(dirRealPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		
		qrcodeFilePath = qrcodeFilePath + "/" + fileName;

		String fileRealPath = request.getServletContext().getRealPath(qrcodeFilePath);

		Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();

		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

		try {
			QRCode.createQRCode(qrCodeInfo, fileRealPath, charSet, hintMap, qrCodeWidth, qrCodeHeight);
		} catch (WriterException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return qrcodeFilePath;
	}

	/**
	 * 自定义字符集，使用默认宽、高
	 * 
	 * @param orgid
	 * @param qrCodeInfo
	 * @param fileName
	 * @param charSet
	 * @return String 二维码图片绝对路径
	 */
	public String encodeQRCode(String orgid, String qrCodeInfo, String fileName, String charSet) {

		return this.encodeQRCode(orgid, qrCodeInfo, fileName, charSet, width, height);

	}

	/**
	 * 使用默认字符集、宽、高
	 * 
	 * @param orgid
	 * @param qrCodeInfo
	 * @param fileName
	 * @return String 二维码图片绝对路径
	 */
	public String encodeQRCode(String orgid, String qrCodeInfo, String fileName) {

		return this.encodeQRCode(orgid, qrCodeInfo, fileName, charset, width, height);

	}

	/**
	 * 使用默认字符集，自定义宽、高
	 * 
	 * @param orgid
	 * @param qrCodeInfo
	 * @param fileName
	 * @param qrCodeWidth
	 * @param qrCodeHeight
	 * @return String 二维码图片绝对路径
	 */
	public String encodeQRCode(String orgid, String qrCodeInfo, String fileName, int qrCodeWidth, int qrCodeHeight) {

		return this.encodeQRCode(orgid, qrCodeInfo, fileName, charset, qrCodeWidth, qrCodeHeight);

	}

}
