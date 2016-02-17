/**
 * 
 */
package com.tranzvision.gd.util.qrcode;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.zxing.DecodeHintType;
import com.google.zxing.NotFoundException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * 读取二维码图片内容
 * 
 * @author SHIHUA
 * @since 2016-02-17
 */
@Service
public class ReadQRCode {

	@Autowired
	HttpServletRequest request;

	/**
	 * 读取指定二维码内容
	 * 
	 * @param filePath
	 *            二维码图片文件路径
	 * @return 二维码内容
	 */
	public String getQRCodeInfo(String filePath) {

		String fileRealPath = request.getServletContext().getRealPath(filePath);

		Map<DecodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<DecodeHintType, ErrorCorrectionLevel>();

		hintMap.put(DecodeHintType.TRY_HARDER, ErrorCorrectionLevel.L);

		try {
			return QRCode.readQRCode(fileRealPath, hintMap);
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (NotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

		return "";
	}

}
