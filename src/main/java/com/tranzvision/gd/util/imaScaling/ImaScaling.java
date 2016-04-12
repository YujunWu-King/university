package com.tranzvision.gd.util.imaScaling;

import com.mortennobel.imagescaling.ResampleOp;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class ImaScaling {
	private static String getExtention(String fileName) {
		int pos = fileName.lastIndexOf(".");
		return fileName.substring(pos + 1);
	}

	public static void scissor(int x1, int y1, int width, int height, String originPath, String savePath)
			throws IOException {
		FileInputStream is = null;
		ImageInputStream iis = null;
		try {
			is = new FileInputStream(originPath);

			String imgSuffix = ImgSuffixUtil.getImgSuffix(originPath);

			Iterator<?> it = ImageIO.getImageReadersByFormatName(imgSuffix);
			ImageReader reader = (ImageReader) it.next();

			iis = ImageIO.createImageInputStream(is);

			reader.setInput(iis, true);

			ImageReadParam param = reader.getDefaultReadParam();

			Rectangle rect = new Rectangle(x1, y1, width, height);

			param.setSourceRegion(rect);

			BufferedImage bi = reader.read(0, param);

			ImageIO.write(bi, imgSuffix, new File(savePath));
		} finally {
			if (is != null)
				is.close();
			if (iis != null)
				iis.close();
		}
	}

	public static void scaleImage(int width, int height, String originPath, String savePath) throws IOException {
		File file = new File(originPath);

		BufferedImage sourceImage = ImageIO.read(file);

		ResampleOp resampleOp = new ResampleOp(width, height);
		BufferedImage rescaledTomato = resampleOp.filter(sourceImage, null);
		ImageIO.write(rescaledTomato, getExtention(originPath).toLowerCase(), new File(savePath));
	}

}