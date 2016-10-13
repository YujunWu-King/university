/**
 * 
 */
package com.tranzvision.gd.TZBaseBundle.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.controller.FileUploadController;
import com.tranzvision.gd.TZBaseBundle.service.FileManageService;

/**
 * 文件管理相关方法实现类
 * 
 * @author SHIHUA
 * @since 2015-11-26
 */
@Service
public class FileManageServiceImpl implements FileManageService {

	@Autowired
	private HttpServletRequest request;

	private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

	/**
	 * 获取给定文件夹、文件的绝对路径；
	 * 
	 * @param path
	 * @return
	 */
	public String getRealPath(String path) {
		String realPath = "";
		String ctxPath = request.getContextPath();
		if (!path.startsWith(ctxPath)) {
			path = ctxPath + path;
		}

		realPath = request.getServletContext().getRealPath(path);
		if (null == realPath) {
			try {
				realPath = request.getSession().getServletContext().getRealPath(path);
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		}
		if (!"".equals(ctxPath)) {
			String ctxPathName = ctxPath.replace("/", "");
			if (realPath.contains("/")) {
				realPath = realPath.replace("/" + ctxPathName + "/" + ctxPathName + "/", "/" + ctxPathName + "/");
			} else {
				realPath = realPath.replace("\\" + ctxPathName + "\\" + ctxPathName + "\\", "\\" + ctxPathName + "\\");
			}
		}

		return realPath;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tranzvision.gd.TZBaseBundle.service.FileManageService#CreateFile(java
	 * .lang.String, java.lang.String, byte[])
	 */
	@Override
	public boolean CreateFile(String parentPath, String fileName, byte[] fileBytes) throws Exception {

		String parentRealPath = this.getRealPath(parentPath);

		File dir = new File(parentRealPath);
		// System.out.println(dir.getAbsolutePath());
		if (!dir.exists()) {
			dir.mkdirs();
		}

		File serverFile = new File(dir.getAbsolutePath() + File.separator + fileName);
		if (serverFile.exists()) {
			// 文件已存在则返回
			return false;
		}

		BufferedOutputStream stream;

		try {
			stream = new BufferedOutputStream(new FileOutputStream(serverFile));
			stream.write(fileBytes);
			stream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}

		if (serverFile.exists()) {
			// 文件存在则创建成功
			logger.info("Saved file:" + parentRealPath + File.separator + fileName + " success.");
			return true;
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tranzvision.gd.TZBaseBundle.service.FileManageService#UpdateFile(java
	 * .lang.String, java.lang.String, byte[])
	 */
	@Override
	public boolean UpdateFile(String parentPath, String fileName, byte[] fileBytes) throws Exception {

		// String parentRealPath =
		// request.getServletContext().getRealPath(parentPath);
		String parentRealPath = this.getRealPath(parentPath);

		File dir = new File(parentRealPath);
		// System.out.println(dir.getAbsolutePath());
		if (!dir.exists()) {
			dir.mkdirs();
		}

		File serverFile = new File(dir.getAbsolutePath() + File.separator + fileName);
		if (serverFile.exists()) {
			// 文件存在则删除
			serverFile.delete();
		}

		BufferedOutputStream stream;

		try {
			stream = new BufferedOutputStream(new FileOutputStream(serverFile));
			stream.write(fileBytes);
			stream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}

		if (serverFile.exists()) {
			// 文件存在则创建成功
			logger.info("Updated file:" + parentRealPath + File.separator + fileName + " success.");
			return true;
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tranzvision.gd.TZBaseBundle.service.FileManageService#DeleteFile(java
	 * .lang.String, java.lang.String)
	 */
	@Override
	public boolean DeleteFile(String parentPath, String fileName) {
		// String parentRealPath =
		// request.getServletContext().getRealPath(parentPath);
		//System.out.println("parentPath:"+parentPath);
		String parentRealPath = this.getRealPath(parentPath);

		File dir = new File(parentRealPath);
		// System.out.println(dir.getAbsolutePath());

		String filePath = parentRealPath + File.separator + fileName;

		File serverFile = new File(dir.getAbsolutePath() + File.separator + fileName);
		if (!serverFile.exists()) {
			logger.info("Deleted file:" + parentRealPath + File.separator + fileName + ".But the file is not exists");
			return true;
		}

		if (serverFile.delete()) {
			logger.info("Deleted file:" + filePath + " success.");
			return true;
		}

		logger.info("Deleted file:" + filePath + " failed.");
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tranzvision.gd.TZBaseBundle.service.FileManageService#DeleteFile(java
	 * .lang.String)
	 */
	@Override
	public boolean DeleteFile(String filePath) {

		// String fileRealPath =
		// request.getServletContext().getRealPath(filePath);
		String fileRealPath = this.getRealPath(filePath);

		File delFile = new File(fileRealPath);
		// System.out.println(delFile.getAbsolutePath());
		if (!delFile.exists()) {
			logger.info("Deleted file:" + fileRealPath + ".But the file is not exists");
			return true;
		}

		File serverFile = new File(delFile.getAbsolutePath());
		if (serverFile.delete()) {
			logger.info("Deleted file:" + fileRealPath + " success.");
			return true;
		}

		logger.info("Deleted file:" + fileRealPath + " failed.");
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tranzvision.gd.TZBaseBundle.service.FileManageService#
	 * getImageWidthHeight(java.lang.String)
	 */
	@Override
	public ArrayList<Integer> getImageWidthHeight(String filePath) {

		ArrayList<Integer> aryImgWH = new ArrayList<Integer>();

		// String fileRealPath =
		// request.getServletContext().getRealPath(filePath);
		String fileRealPath = this.getRealPath(filePath);
		File imageFile = new File(fileRealPath);
		if (!imageFile.exists()) {
			logger.info("Get image width & height failed.The image:" + fileRealPath + " is not exists");
			return aryImgWH;
		}

		try {
			java.awt.image.BufferedImage img = ImageIO.read(imageFile);
			aryImgWH.add(0, img.getWidth());
			aryImgWH.add(1, img.getHeight());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return aryImgWH;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tranzvision.gd.TZBaseBundle.service.FileManageService#
	 * encodeChineseDownloadFileName(javax.servlet.http.HttpServletRequest,
	 * java.lang.String)
	 */
	public String encodeChineseDownloadFileName(HttpServletRequest request, String pFileName)
			throws UnsupportedEncodingException {

		String filename = null;
		String agent = request.getHeader("USER-AGENT");
		if (null != agent) {
			if (-1 != agent.indexOf("Firefox")) {// Firefox
				filename = "=?UTF-8?B?"
						+ (new String(org.apache.commons.codec.binary.Base64.encodeBase64(pFileName.getBytes("UTF-8"))))
						+ "?=";
			} else if (-1 != agent.indexOf("Chrome")) {// Chrome
				filename = new String(pFileName.getBytes(), "ISO8859-1");
			} else {// IE7+
				filename = java.net.URLEncoder.encode(pFileName, "UTF-8");
				filename = StringUtils.replace(filename, "+", "%20");// 替换空格
			}
		} else {
			filename = pFileName;
		}
		return filename;
	}

}
