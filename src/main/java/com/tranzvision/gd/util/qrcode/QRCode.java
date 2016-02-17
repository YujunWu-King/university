/**
 * 
 */
package com.tranzvision.gd.util.qrcode;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * 生成、读取二维码图片
 * 
 * @author SHIHUA
 * @since 2016-02-17
 */
public class QRCode {

	/**
	 * 生成二维码图片
	 * 
	 * @param qrCodeData
	 * @param filePath
	 * @param charset
	 * @param hintMap
	 * @param qrCodeheight
	 * @param qrCodewidth
	 * @throws WriterException
	 * @throws IOException
	 */
	public static void createQRCode(String qrCodeData, String filePath, String charset,
			Map<EncodeHintType, ErrorCorrectionLevel> hintMap, int qrCodeheight, int qrCodewidth)
					throws WriterException, IOException {

		BitMatrix matrix = new MultiFormatWriter().encode(new String(qrCodeData.getBytes(charset), charset),
				BarcodeFormat.QR_CODE, qrCodewidth, qrCodeheight, hintMap);

		Path pathToFile = FileSystems.getDefault().getPath(filePath);

		MatrixToImageWriter.writeToPath(matrix, filePath.substring(filePath.lastIndexOf('.') + 1), pathToFile);
	}

	/**
	 * 读取二维码图片
	 * 
	 * @param filePath
	 * @param hintMap
	 * @return String
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws NotFoundException
	 */
	public static String readQRCode(String filePath, Map<DecodeHintType, ErrorCorrectionLevel> hintMap)
			throws FileNotFoundException, IOException, NotFoundException {

		BinaryBitmap binaryBitmap = new BinaryBitmap(
				new HybridBinarizer(new BufferedImageLuminanceSource(ImageIO.read(new FileInputStream(filePath)))));

		Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap, hintMap);

		return qrCodeResult.getText();
	}

}
