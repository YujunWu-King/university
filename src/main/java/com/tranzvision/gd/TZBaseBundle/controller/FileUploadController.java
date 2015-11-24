/**
 * 
 */
package com.tranzvision.gd.TZBaseBundle.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.security.TzFilterIllegalCharacter;

/**
 * @author SHIHUA
 * @since 2015-11-23
 * @version http://www.journaldev.com/2573/spring-mvc-file-upload-example-
 *          tutorial-single-and-multiple-files
 * 
 *          Handles requests for the application file upload requests
 */
@Controller
@RequestMapping(value = "/")
public class FileUploadController {

	private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

	@Autowired
	private JacksonUtil jacksonUtil;

	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private TzFilterIllegalCharacter tzFilterIllegalCharacter;

	/**
	 * Upload single file using Spring Controller
	 */
	@RequestMapping(value = "UpdServlet", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public @ResponseBody String uploadFileHandler(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, Object> allRequestParams, @RequestParam("orguploadfile") MultipartFile file) {

		Map<String, Object> mapRet = new HashMap<String, Object>();

		// String limitSize = allRequestParams.get("limitSize");
		String language = String.valueOf(allRequestParams.get("language"));
		String funcdir = String.valueOf(allRequestParams.get("filePath"));
		String istmpfile = String.valueOf(allRequestParams.get("tmp"));

		//过滤功能目录名称中的特殊字符
		if (null != funcdir && !"".equals(funcdir) && !"null".equals(funcdir)) {
			funcdir = "/" + tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(funcdir);
		}else{
			funcdir = "";
		}
		
		//是否临时文件的标记
		if(null == istmpfile ||  !"null".equals(funcdir)){
			istmpfile = "0";
		}

		/*
		 * System.out.println(allRequestParams); for(String
		 * paramName:Collections.list(request.getParameterNames())){ // Whatever
		 * you want to do with your map // Key : params // Value :
		 * httpServletRequest.getParameter(paramName)
		 * System.out.println("-------------------------");
		 * System.out.println(allRequestParams.get(paramName).getClass().
		 * getTypeName()); System.out.println("-------------------------"); }
		 */

		boolean success = false;
		Object messages = null;
		String filename = "";
		try {
			filename = file.getOriginalFilename();
		} catch (Exception e) {

		}

		if (!file.isEmpty()) {
			try {

				int imgWidth = 0;
				int imgHeight = 0;
				long fileSize = 0L;
				String suffix = filename.substring(filename.lastIndexOf(".") + 1);
				success = this.checkFormat(suffix);
				if (!success) {
					if ("ENG".equals(language)) {
						messages = "Invalid file format.";
					} else {
						messages = "上传的文件格式错误";
					}
				} else {
					fileSize = file.getSize();
					// System.out.println(fileSize);
					success = this.checkSize(fileSize);
					if (!success) {
						if ("ENG".equals(language)) {
							messages = "The file is too large. Please re-upload.";
						} else {
							messages = "上传的文件太大";
						}
					}
				}

				if (success) {

					byte[] bytes = file.getBytes();

					// Creating the directory to store file
					String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
					String rootPath = getSysHardCodeVal.getOrgFileUploadPath();
					String tmpFilePath = getSysHardCodeVal.getTmpFileUploadPath();
					String parentPath = "";
					
					if("1".equals(istmpfile)){
						//若是临时文件，则存储在临时文件目录
						parentPath = tmpFilePath + "/" + orgid + "/" + this.getDateNow() + funcdir;
					}else{
						parentPath = rootPath + "/" + orgid + "/" + this.getDateNow() + funcdir;
					}
					String accessPath = parentPath + "/";
					parentPath = request.getServletContext().getRealPath(parentPath);
					File dir = new File(parentPath);
					System.out.println(dir.getAbsolutePath());
					if (!dir.exists()) {
						dir.mkdirs();
					}

					// Create the file on server
					String sysFileName = (new StringBuilder(String.valueOf(getNowTime()))).append(".").append(suffix)
							.toString();
					if (sysFileName.indexOf('/') != -1)
						sysFileName = sysFileName.substring(sysFileName.lastIndexOf('/') + 1);

					File serverFile = new File(dir.getAbsolutePath() + File.separator + sysFileName);
					BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
					stream.write(bytes);
					stream.close();

					// 看是否是图片，如果是，则获取图片的宽、高
					if (getSysHardCodeVal.getImageSuffix().contains(suffix.toLowerCase())) {
						java.awt.image.BufferedImage img = ImageIO.read(serverFile);
						imgWidth = img.getWidth();
						imgHeight = img.getHeight();
					}

					logger.info("Server File Location=" + serverFile.getAbsolutePath());

					Map<String, Object> mapFile = new HashMap<String, Object>();
					mapFile.put("filename", filename);
					mapFile.put("sysFileName", sysFileName);
					mapFile.put("size", String.valueOf(fileSize / 1024L) + "k");
					mapFile.put("path", parentPath);
					mapFile.put("accessPath", accessPath);
					mapFile.put("imgWidth", imgWidth);
					mapFile.put("imgHeight", imgHeight);

					messages = mapFile;

				}

			} catch (Exception e) {
				e.printStackTrace();
				success = false;
				if ("ENG".equals(language)) {
					messages = "Server Exception.";
				} else {
					messages = "服务器发生异常";
				}
			}
		} else {
			if ("ENG".equals(language)) {
				messages = "You failed to upload [" + filename + "] because the file was empty.";
			} else {
				messages = "上传失败，文件 [" + filename + "] 是一个空文件。";
			}
		}

		mapRet.put("success", success);
		mapRet.put("msg", messages == null ? "" : messages);

		return jacksonUtil.Map2json(mapRet);
	}

	/**
	 * Upload multiple file using Spring Controller
	 */
	/*---- 未完成
	@RequestMapping(value = "uploadMultipleFile", method = RequestMethod.POST)
	public @ResponseBody String uploadMultipleFileHandler(@RequestParam("name") String[] names,
			@RequestParam("file") MultipartFile[] files) {
	
		if (files.length != names.length)
			return "Mandatory information missing";
	
		String message = "";
		for (int i = 0; i < files.length; i++) {
			MultipartFile file = files[i];
			String name = names[i];
			try {
				byte[] bytes = file.getBytes();
	
				// Creating the directory to store file
				String rootPath = System.getProperty("catalina.home");
				File dir = new File(rootPath + File.separator + "tmpFiles");
				if (!dir.exists())
					dir.mkdirs();
	
				// Create the file on server
				File serverFile = new File(dir.getAbsolutePath() + File.separator + name);
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();
	
				logger.info("Server File Location=" + serverFile.getAbsolutePath());
	
				message = message + "You successfully uploaded file=" + name + "<br />";
			} catch (Exception e) {
				return "You failed to upload " + name + " => " + e.getMessage();
			}
		}
		return message;
	}
	*/

	private boolean checkFormat(String fileSuffix) {

		if (getSysHardCodeVal.getFileUploadDeniedExtensions().contains(fileSuffix.toLowerCase())) {
			return false;
		}

		return true;
	}

	private boolean checkSize(long filesize) {
		if (getSysHardCodeVal.getFileUploadMaxSize() < filesize) {
			return false;
		}
		return true;
	}

	protected String getDateNow() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(1);
		int month = cal.get(2) + 1;
		int day = cal.get(5);
		return (new StringBuilder()).append(year).append(month).append(day).toString();
	}

	protected String getNowTime() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(1);
		int month = cal.get(2) + 1;
		int day = cal.get(5);
		int hour = cal.get(10);
		int minute = cal.get(12);
		int second = cal.get(13);
		int mi = cal.get(14);
		long num = cal.getTimeInMillis();
		int rand = (int) (Math.random() * 899999 + 100000);
		return (new StringBuilder()).append(year).append(month).append(day).append(hour).append(minute).append(second)
				.append(mi).append(num).append("_").append(rand).toString();
	}

}
