/**
 * 
 */
package com.tranzvision.gd.util.imaScaling;

import java.io.File;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

/**
 * 获取图片文件的真实格式
 * 
 * @author SHIHUA
 * @since 2016-04-12
 */
public class ImgSuffixUtil {

	public static String getImgSuffix(String imgPath) {

		String imgsuffix = "";
		try {

			// Read Image File
			File imageFile = new File(imgPath);

			// Create ImageInputStream using Image File
			ImageInputStream imageInputStream = ImageIO.createImageInputStream(imageFile);

			// Get the image readers for that file
			Iterator<ImageReader> imageReadersList = ImageIO.getImageReaders(imageInputStream);

			if (!imageReadersList.hasNext()) {
				throw new RuntimeException("Image Readers Not Found!!!");
			}

			// Get the image type
			ImageReader reader = imageReadersList.next();
			imgsuffix = reader.getFormatName();
			//System.out.println("Image Format: " + reader.getFormatName());

			// Close stream
			imageInputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return imgsuffix;
	}
	
}
