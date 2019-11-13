/**
 * 
 */
package com.tranzvision.gd.TZBaseBundle.controller;

import com.tranzvision.gd.TZApplicationTemplateBundle.service.impl.ExcelDRbyModel;
import com.tranzvision.gd.TZApplicationTemplateBundle.service.impl.PdfPrintbyModel;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzWebsiteLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FileManageServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.security.TzFilterIllegalCharacter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

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
@CrossOrigin(origins = "*", maxAge = 3600)
public class FileUploadController {

	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private TzWebsiteLoginServiceImpl tzWebsiteLoginServiceImpl;

	@Autowired
	private TzFilterIllegalCharacter tzFilterIllegalCharacter;

	@Autowired
	private FileManageServiceImpl fileManageServiceImpl;

	/**
	 * Upload single file using Spring Controller 存储在 orgupload目录
	 * 
	 * @param request
	 * @param response
	 * @param allRequestParams
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "UpdServlet", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public @ResponseBody String orgUploadFileHandler(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, Object> allRequestParams, @RequestParam("orguploadfile") MultipartFile file) {

		// String limitSize = allRequestParams.get("limitSize");
		String language = String.valueOf(allRequestParams.get("language"));
		String funcdir = String.valueOf(allRequestParams.get("filePath"));
		String istmpfile = String.valueOf(allRequestParams.get("tmp"));
		String impl = String.valueOf(allRequestParams.get("impl"));
		String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		String rootPath = getSysHardCodeVal.getOrgFileUploadPath();

		String retJson = this.doSaveFile(orgid, rootPath, funcdir, language, istmpfile, file, "", impl);

		return retJson;
	}

	/**
	 * Upload single file using Spring Controller 存储在 orgupload目录
	 * 
	 * @param request
	 * @param response
	 * @param allRequestParams
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "UpMobileDServlet", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public @ResponseBody String orgUploadMobileFileHandler(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, Object> allRequestParams,
			@RequestParam("orguploadMobilefile") MultipartFile file) {
		// String limitSize = allRequestParams.get("limitSize");
		String language = String.valueOf(allRequestParams.get("language"));
		String funcdir = String.valueOf(allRequestParams.get("filePath"));
		String istmpfile = String.valueOf(allRequestParams.get("tmp"));
		String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		String rootPath = getSysHardCodeVal.getOrgFileUploadPath();

		String retJson = this.doSaveFile(orgid, rootPath, funcdir, language, istmpfile, file, "", "");

		return retJson;
	}

	/**
	 * Upload single file using Spring Controller 存储在 bmb目录
	 * 
	 * @param request
	 * @param response
	 * @param allRequestParams
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "UpPdfServlet", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public @ResponseBody String bmbUploadFileHandler(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, Object> allRequestParams, @RequestParam("pdfuploadfile") MultipartFile file) {

		// String limitSize = allRequestParams.get("limitSize");
		String language = String.valueOf(allRequestParams.get("language"));
		String tplid = String.valueOf(allRequestParams.get("tplid"));
		String istmpfile = String.valueOf(allRequestParams.get("tmp"));
		String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		String rootPath = getSysHardCodeVal.getBmbSinglePdfDir();
		// 最终生成文件路径/bmb/singlepdf/" + orgid + "/时间/" + Template+"/"+tplid + 里面

		String retJson = this.doSaveFile(orgid, rootPath, tplid, language, istmpfile, file, 0);

		return retJson;
	}

	@RequestMapping(value = "UploadExcelServlet", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public @ResponseBody String bmbUploadExcelHandler(HttpServletRequest request, HttpServletResponse response,
														  @RequestParam Map<String, Object> allRequestParams, @RequestParam("pdfuploadfile") MultipartFile file) {
		// String limitSize = allRequestParams.get("limitSize");

		String language = String.valueOf(allRequestParams.get("language"));
		String tplid = String.valueOf(allRequestParams.get("tplid"));
		String istmpfile = String.valueOf(allRequestParams.get("tmp"));
		String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		String rootPath = getSysHardCodeVal.getBmbSinglePdfDir();
		// 最终生成文件路径/bmb/singlepdf/" + orgid + "/时间/" + Template+"/"+tplid + 里面

		String retJson = this.doSaveFile(orgid, rootPath, tplid, language, istmpfile, file, 2);

		return retJson;
	}

	@RequestMapping(value = "UpExcelServlet", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public @ResponseBody String bmbUploadExcelFileHandler(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, Object> allRequestParams, @RequestParam("pdfuploadfile") MultipartFile file) {
		// String limitSize = allRequestParams.get("limitSize");

		String language = String.valueOf(allRequestParams.get("language"));
		String tplid = String.valueOf(allRequestParams.get("tplid"));
		String istmpfile = String.valueOf(allRequestParams.get("tmp"));
		String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		String rootPath = getSysHardCodeVal.getBmbSinglePdfDir();
		// 最终生成文件路径/bmb/singlepdf/" + orgid + "/时间/" + Template+"/"+tplid + 里面

		String retJson = this.doSaveFile(orgid, rootPath, tplid, language, istmpfile, file, 1);

		return retJson;
	}

	/**
	 * 上传报名报 PDF模板 存储在/bmb/singlepdf/" + orgid + "/时间/" + Template+/+tplid + "里面
	 * 
	 * @param orgid
	 * @param rootPath
	 *            根目录 从配置文件读取 是 /bmb/singlepdf/
	 * @param tplid
	 *            模板ID
	 * @param language
	 * @param istmpfile
	 * @param file
	 * @param type
	 *            0 PDF 1 EXcel
	 * @return
	 */
	private String doSaveFile(String orgid, String rootPath, String tplid, String language, String istmpfile,
			MultipartFile file, int type) {

		String templateID = tplid;

		// 过滤功能目录名称中的特殊字符
		if (null != tplid && !"".equals(tplid) && !"null".equals(tplid)) {
			tplid = "/template/" + tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(tplid);
		} else {
			tplid = "";
		}

		if (null == orgid || "".equals(orgid)) {
			orgid = "orgidnull";
		}
		orgid = orgid.toLowerCase();

		// 是否临时文件的标记
		if (null == istmpfile || !"null".equals(tplid)) {
			istmpfile = "0";
		}

		boolean success = false;
		Object messages = null;
		String filename = "";
		try {
			filename = file.getOriginalFilename();
		} catch (Exception e) {

		}

		if (!file.isEmpty()) {
			try {
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
					String tmpFilePath = getSysHardCodeVal.getTmpFileUploadPath();
					String parentPath = "";

					if ("1".equals(istmpfile)) {
						// 若是临时文件，则存储在临时文件目录
						parentPath = tmpFilePath + "/" + orgid + "/" + this.getDateNow() + tplid;
					} else {
						// /bmb/singlepdf/" + orgid + "/时间/" + Template+/+tplid
						// + "
						parentPath = rootPath + "/" + orgid + "/" + this.getDateNow() + tplid;
					}
					String accessPath = parentPath + "/";

					boolean createResult = false;
					int createTimes = 5;
					String sysFileName = "";
					while (!createResult && createTimes > 0) {
						// Create the file on server
						sysFileName = (new StringBuilder(String.valueOf(getNowTime()))).append(".").append(suffix)
								.toString();
						if (sysFileName.indexOf('/') != -1)
							sysFileName = sysFileName.substring(sysFileName.lastIndexOf('/') + 1);

						createResult = fileManageServiceImpl.CreateFile(parentPath, sysFileName, bytes, 1);

						createTimes--;
					}
					if (createResult) {
						if (type == 0) {
							PdfPrintbyModel ppm = new PdfPrintbyModel();

							ppm.addFile(templateID, accessPath + sysFileName, filename);
						} else if (type == 1) {
							ExcelDRbyModel ppm = new ExcelDRbyModel();
							ppm.addFile(templateID, accessPath + sysFileName, filename);
						}

						Map<String, Object> mapFile = new HashMap<String, Object>();
						mapFile.put("filename", filename);
						mapFile.put("sysFileName", sysFileName);
						mapFile.put("size", String.valueOf(fileSize / 1024L) + "k");
						// mapFile.put("path", parentPath);
						mapFile.put("accessPath", accessPath);
						messages = mapFile;
					} else {
						if ("ENG".equals(language)) {
							messages = "Upload failed. Please re-try.";
						} else {
							messages = "上传失败，请重试。";
						}
					}
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

		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("success", success);
		mapRet.put("msg", messages == null ? "" : messages);
		JacksonUtil jacksonUtil = new JacksonUtil();
		return jacksonUtil.Map2json(mapRet);

	}

	/**
	 * Upload single file using Spring Controller 存储在 website目录
	 * 
	 * @param request
	 * @param response
	 * @param allRequestParams
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "UpdWebServlet", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public @ResponseBody String webUploadHandler(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, Object> allRequestParams, @RequestParam("websitefile") MultipartFile file) {

		// String limitSize = allRequestParams.get("limitSize");
		String language = String.valueOf(allRequestParams.get("language"));
		String funcdir = String.valueOf(allRequestParams.get("filePath"));
		String istmpfile = String.valueOf(allRequestParams.get("tmp"));
		String siteid = allRequestParams.get("siteid") == null ? "" : String.valueOf(allRequestParams.get("siteid"));
		String orgid = tzWebsiteLoginServiceImpl.getLoginedUserOrgid(request);
		String rootPath = getSysHardCodeVal.getWebsiteFileUploadPath();

		String retJson = "";

		if ("".equals(siteid)) {
			// Map<String, Object> mapRet = new HashMap<String, Object>();
			// mapRet.put("success", false);
			// mapRet.put("msg", "缺少参数siteid。");
			retJson = this.doSaveFile(orgid, rootPath, funcdir, language, istmpfile, file, siteid, "");
		} else {
			retJson = this.doSaveFile(orgid, rootPath, funcdir, language, istmpfile, file, siteid, "");
		}
		return retJson;
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

	private String doSaveFile(String orgid, String rootPath, String funcdir, String language, String istmpfile,
			MultipartFile file, String siteid, String impl) {

		// 过滤功能目录名称中的特殊字符
		if (null != funcdir && !"".equals(funcdir) && !"null".equals(funcdir)) {
			funcdir = "/" + tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(funcdir);
		} else {
			funcdir = "";
		}

		if (null != siteid && !"".equals(siteid)) {
			siteid = "/" + tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(siteid);
		} else {
			siteid = "";
		}

		if (null == orgid || "".equals(orgid)) {
			orgid = "orgidnull";
		}
		orgid = orgid.toLowerCase();

		// 是否临时文件的标记
		if (null == istmpfile || !"null".equals(funcdir)) {
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
					String tmpFilePath = getSysHardCodeVal.getTmpFileUploadPath();
					String parentPath = "";

					if ("1".equals(istmpfile)) {
						// 若是临时文件，则存储在临时文件目录
						parentPath = tmpFilePath + "/" + orgid + siteid + "/" + this.getDateNow() + funcdir;
					} else {
						parentPath = rootPath + "/" + orgid + siteid + "/" + this.getDateNow() + funcdir;
					}
					String accessPath = parentPath + "/";

					boolean createResult = false;
					int createTimes = 5;
					String sysFileName = "";
					while (!createResult && createTimes > 0) {

						// Create the file on server
						sysFileName = (new StringBuilder(String.valueOf(getNowTime()))).append(".").append(suffix)
								.toString();
						if (sysFileName.indexOf('/') != -1)
							sysFileName = sysFileName.substring(sysFileName.lastIndexOf('/') + 1);
						createResult = fileManageServiceImpl.CreateFile(parentPath, sysFileName, bytes, 1);
						createTimes--;
					}
					if (createResult) {
						// 看是否是图片，如果是，则获取图片的宽、高
						if (getSysHardCodeVal.getImageSuffix().contains(suffix.toLowerCase())) {
							ArrayList<Integer> imgWH = fileManageServiceImpl
									.getImageWidthHeight(parentPath + File.separator + sysFileName);
							if (imgWH.size() > 0) {
								imgWidth = imgWH.get(0);
								imgHeight = imgWH.get(1);
							}
						}

						Map<String, Object> mapFile = new HashMap<String, Object>();
						mapFile.put("filename", filename);
						mapFile.put("sysFileName", sysFileName);
						mapFile.put("size", String.valueOf(fileSize / 1024L) + "k");
						// mapFile.put("path", parentPath);
						mapFile.put("accessPath", accessPath);
						mapFile.put("imgWidth", imgWidth);
						mapFile.put("imgHeight", imgHeight);

						messages = mapFile;

					} else {
						if ("ENG".equals(language)) {
							messages = "Upload failed. Please re-try.";
						} else {
							messages = "上传失败，请重试。";
						}
					}

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

		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("success", success);
		mapRet.put("msg", messages == null ? "" : messages);

		JacksonUtil jacksonUtil = new JacksonUtil();
		return jacksonUtil.Map2json(mapRet);

	}

	private boolean checkFormat(String fileSuffix) {

		if (getSysHardCodeVal.getFileUploadDeniedExtensions().contains(fileSuffix.toLowerCase())) {
			return false;
		}
		if (getSysHardCodeVal.getFileUploadAllowedExtensions() != null
				|| !"".equals(getSysHardCodeVal.getFileUploadAllowedExtensions())) {
			if (getSysHardCodeVal.getFileUploadAllowedExtensions().contains(fileSuffix.toLowerCase())) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
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

	@RequestMapping(value = "SingleUpdWebServlet", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public @ResponseBody String webUploadHandler(MultipartHttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, Object> allRequestParams) {

		String language = String.valueOf(allRequestParams.get("language"));
		String funcdir = String.valueOf(allRequestParams.get("filePath"));
		String istmpfile = String.valueOf(allRequestParams.get("tmp"));
		String siteid = allRequestParams.get("siteid") == null ? "" : String.valueOf(allRequestParams.get("siteid"));
		String orgid = tzWebsiteLoginServiceImpl.getLoginedUserOrgid(request);
		String rootPath = getSysHardCodeVal.getWebsiteFileUploadPath();

		String keyName = String.valueOf(allRequestParams.get("keyName"));
		MultipartFile file = (MultipartFile) request.getFile(keyName);
		String retJson = "";

		if ("".equals(siteid)) {
			retJson = this.doSaveFile(orgid, rootPath, funcdir, language, istmpfile, file, siteid, "");
		} else {
			retJson = this.doSaveFile(orgid, rootPath, funcdir, language, istmpfile, file, siteid, "");
		}
		return retJson;
	}
}
