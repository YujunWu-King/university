package com.tranzvision.gd.TZAuthBundle.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.tranzvision.gd.TZBaseBundle.service.impl.FileManageServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.security.TzFilterIllegalCharacter;
import com.tranzvision.gd.util.security.collectsecurity.SecurityForCollectFee;

/**
 * 电话盒子相关接口
 * 
 * @author caoy
 *
 */
@Controller
@RequestMapping(value = "box")
public class TzLoginForBoxController {

	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;

	@Autowired
	private TzFilterIllegalCharacter tzFilterIllegalCharacter;

	@Autowired
	private FileManageServiceImpl fileManageServiceImpl;

	/**
	 * 登录接口
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	// @RequestMapping(value = "login", method = RequestMethod.POST, produces =
	// "text/html;charset=UTF-8")
	// @ResponseBody

	@RequestMapping(value = { "/login/{orgid}" }, method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String doLogin(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "orgid") String orgid) throws Exception {
		System.out.println("doLogin");
		Map<String, Object> jsonMap = null;
		JacksonUtil jacksonUtil = new JacksonUtil();

		System.out.println("orgid:"+orgid);
		
		String username = request.getParameter("username");
		String pwd = request.getParameter("pwd");
		String timestamp = request.getParameter("timestamp");
		String authenticator = request.getParameter("authenticator");
		//String jgid = request.getParameter("jgid");
		// username + pwd + timestamp
		LinkedHashMap<String, String> tm = new LinkedHashMap<String, String>();
		tm.put("username", username);
		tm.put("pwd", pwd);
		tm.put("timestamp", timestamp);
		//tm.put("jgid", jgid);
		tm.put("authenticator", authenticator);

		// 校验传入参数
		jsonMap = this.checkParameter(tm);
		if (jsonMap != null) {
			return jacksonUtil.Map2json(jsonMap);
		}
		// 登录处理 稍后更新

		// 生产返回的
		String usertoken = "TEST";
		String oprid = "TEST";
		jsonMap = new HashMap<String, Object>();
		jsonMap.put("res_code", "0");
		jsonMap.put("res_message", "Success");
		jsonMap.put("usertoken", usertoken);
		jsonMap.put("oprid", oprid);

		return jacksonUtil.Map2json(jsonMap);
	}

	/**
	 * 传入参数的校验
	 * 
	 * @param tm
	 * @return
	 */
	private Map<String, Object> checkParameter(LinkedHashMap<String, String> tm) {
		Map<String, Object> jsonMap = null;
		String key = "";
		int len = tm.size();
		int i = 0;
		StringBuffer sb = new StringBuffer();
		String Authenticator = "";
		for (Map.Entry<String, String> entry : tm.entrySet()) {
			key = entry.getKey();
			System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
			if (entry.getValue() == null || StringUtils.isBlank(entry.getValue().toString())) {
				jsonMap = new HashMap<String, Object>();
				jsonMap.put("res_code", "-0001");
				jsonMap.put("res_message", "传入参数" + key + "为空");
				return jsonMap;
			}
			// 是否超时
			if (key.equals("timestamp")) {
				try {
					long timep = Long.parseLong(entry.getValue().toString());
					long time = System.currentTimeMillis();
					if (time - timep > 2 * 60 * 1000) {
						jsonMap = new HashMap<String, Object>();
						jsonMap.put("res_code", "-0001");
						jsonMap.put("res_message", "登录超时");
						return jsonMap;
					}
				} catch (Exception e) {
					jsonMap = new HashMap<String, Object>();
					jsonMap.put("res_code", "-0001");
					jsonMap.put("res_message", "时间戳格式错误");
					return jsonMap;
				}
			}
			if (i < len - 1) {
				sb.append(entry.getValue().toString());
			}
			if (i == len - 1 && key.equals("authenticator")) {
				Authenticator = entry.getValue().toString();
			}
			i++;
		}

		// 3 校验 加密是否正确
		String securitykey = getSysHardCodeVal.getBoxKey();

		System.out.println("Authenticator:" + Authenticator);
		System.out.println("securitykey:" + securitykey);
		System.out.println("dev:" + sb.toString());
		int r = SecurityForCollectFee.checkAuthenticator(Authenticator, securitykey, sb.toString());
		if (r != 0) {
			jsonMap = new HashMap<String, Object>();
			jsonMap.put("res_code", "-0001");
			jsonMap.put("res_message", "解密失败");
			return jsonMap;
		}
		return jsonMap;
	}

	/**
	 * 弹屏请求接口
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "bombScreen", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String doBombScreen(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("bombScreen");
		Map<String, Object> jsonMap = null;
		JacksonUtil jacksonUtil = new JacksonUtil();

		String usertoken = request.getParameter("usertoken");
		String tel = request.getParameter("tel");
		String callid = request.getParameter("callid");
		String authenticator = request.getParameter("authenticator");
		String timestamp = request.getParameter("timestamp");

		// 校验传入参数
		// usertoken+tel+callid+timestamp
		LinkedHashMap<String, String> tm = new LinkedHashMap<String, String>();
		tm.put("usertoken", usertoken);
		tm.put("tel", tel);
		tm.put("callid", callid);
		tm.put("timestamp", timestamp);
		tm.put("authenticator", authenticator);

		// 校验传入参数
		jsonMap = this.checkParameter(tm);
		if (jsonMap != null) {
			return jacksonUtil.Map2json(jsonMap);

		}

		jsonMap = new HashMap<String, Object>();
		return "The Page OK";
	}

	@RequestMapping(value = "UpVoidServlet", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public @ResponseBody String orgUploadFileHandler(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, Object> allRequestParams, @RequestParam("orguploadfile") MultipartFile file) {
		System.out.println("UploadServlet");
		Map<String, Object> jsonMap = null;
		JacksonUtil jacksonUtil = new JacksonUtil();

		String callid = String.valueOf(allRequestParams.get("callid"));
		String authenticator = String.valueOf(allRequestParams.get("authenticator"));
		String timestamp = String.valueOf(allRequestParams.get("timestamp"));

		// 校验传入参数
		// usertoken+tel+callid+timestamp
		LinkedHashMap<String, String> tm = new LinkedHashMap<String, String>();
		tm.put("callid", callid);
		tm.put("timestamp", timestamp);
		tm.put("authenticator", authenticator);

		// 校验传入参数
		jsonMap = this.checkParameter(tm);
		if (jsonMap != null) {
			return jacksonUtil.Map2json(jsonMap);
		}

		String rootPath = getSysHardCodeVal.getOrgVoiceFileUploadPath();
		// String rootPath = "/statics/uploadfiles/voice";
		System.out.println(rootPath);

		String retJson = this.doSaveFile(rootPath, "", "", "", file);

		return retJson;
	}

	private String doSaveFile(String rootPath, String funcdir, String language, String istmpfile, MultipartFile file) {

		// 过滤功能目录名称中的特殊字符
		if (null != funcdir && !"".equals(funcdir) && !"null".equals(funcdir)) {
			funcdir = "/" + tzFilterIllegalCharacter.filterDirectoryIllegalCharacter(funcdir);
		} else {
			funcdir = "";
		}

		// 是否临时文件的标记
		if (null == istmpfile || !"null".equals(funcdir)) {
			istmpfile = "0";
		}

		String success = "-0002";

		boolean flag = false;
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
				flag = this.checkFormat(suffix);
				if (!flag) {
					if ("ENG".equals(language)) {
						messages = "Invalid file format.";
					} else {
						messages = "上传的文件格式错误";
					}
				} else {
					fileSize = file.getSize();
					// System.out.println(fileSize);
					flag = this.checkSize(fileSize);
					if (!flag) {
						if ("ENG".equals(language)) {
							messages = "The file is too large. Please re-upload.";
						} else {
							messages = "上传的文件太大";
						}
					}
				}

				if (flag) {

					byte[] bytes = file.getBytes();

					// Creating the directory to store file
					String tmpFilePath = getSysHardCodeVal.getTmpFileUploadPath();
					String parentPath = "";

					if ("1".equals(istmpfile)) {
						// 若是临时文件，则存储在临时文件目录
						parentPath = tmpFilePath + "/" + this.getDateNow() + funcdir;
					} else {
						parentPath = rootPath + "/" + this.getDateNow() + funcdir;
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

						createResult = fileManageServiceImpl.CreateFile(parentPath, sysFileName, bytes);

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

						// messages = mapFile;

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
				flag = false;
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

		if (flag) {
			success = "0";
			messages = "Success";
		}
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("res_code", success);
		mapRet.put("res_message", messages == null ? "" : messages);

		JacksonUtil jacksonUtil = new JacksonUtil();
		return jacksonUtil.Map2json(mapRet);

	}

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
