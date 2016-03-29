package com.tranzvision.gd.TZApplicationTemplateBundle.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tranzvision.gd.TZApplicationTemplateBundle.service.impl.AppFormExportClsServiceImpl;
import com.tranzvision.gd.TZApplicationTemplateBundle.service.impl.AppFormListClsServiceImpl;
import com.tranzvision.gd.TZApplicationVerifiedBundle.dao.PsTzExcelDattTMapper;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzExcelDattT;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzComPageAuthServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdKjComServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.GdObjectServiceImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 报名表模板配置控制器
 * 
 * @author WRL
 * @since 2016-02-25
 */
@Controller
@RequestMapping(value = { "/admission" })
public class AppFormController {
	@Autowired
	private AppFormListClsServiceImpl appFormListClsServiceImpl;
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private TzComPageAuthServiceImpl tzComPageAuthServiceImpl;

	@Autowired
	private GdKjComServiceImpl gdKjComService;

	@Autowired
	private AppFormExportClsServiceImpl appFormExportClsServiceImpl;

	@Autowired
	private GdObjectServiceImpl gdObjectServiceImpl;
	
	@Autowired
	private PsTzExcelDattTMapper psTzExcelDattTMapper;

	private static final int BUFFER_SIZE = 4096;

	@RequestMapping(value = { "/diyform/{tplId}" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String dispatcher(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "tplId") String tplId) {
		// 组件名称
		String comName = "TZ_ONLINE_REG_COM";
		// 页面名称
		String pageName = "TZ_ONREG_EDIT_STD";
		// OPRID
		String oprid = gdKjComService.getOPRID(request);
		// 报文内容;
		String strComContent = "";
		// 错误描述;
		String strErrorDesc = "";
		// 错误码;
		String errorCode = "0";
		// 返回值;
		String strRetContent = "";

		// 错误信息
		String[] errorMsg = { "0", "" };
		// 校验组件页面的读写访问权限
		boolean permission = tzComPageAuthServiceImpl.checkUpdatePermission(oprid, comName, pageName, errorMsg);

		if (permission) {
			// 更新权限
			if (StringUtils.isBlank(tplId)) {
				errorCode = "1";
				strErrorDesc = "参数-报名表模板ID为空！";
			} else {
				Map<String, Object> comParams = new HashMap<String, Object>();
				comParams.put("TZ_APP_TPL_ID", tplId);
				JacksonUtil jacksonUtil = new JacksonUtil();
				jacksonUtil.Map2json(comParams);
				strComContent = appFormListClsServiceImpl.tzGetHtmlContent(jacksonUtil.Map2json(comParams));
			}
		} else {
			// 无更新权限
			errorCode = errorMsg[0];
			strErrorDesc = errorMsg[1];
		}

		if ("0".equals(errorCode)) {
			strRetContent = strComContent;
		} else {
			if (gdKjComService.isSessionValid(request)) {
				strRetContent = strErrorDesc;
			} else {
				strRetContent = gdObjectServiceImpl.getTimeoutHTML(request, "HTML.TZBaseBundle.TZGD_SQR_HTML_TIMEOUT");
				if (strRetContent == null || "".equals(strRetContent)) {
					strRetContent = strErrorDesc;
				}
			}
		}

		return strRetContent;
	}

	@RequestMapping(value = { "/expform/{insid}" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportAppForm(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "insid") String insid) throws IOException {
		// 错误时的返回值
		String strRetContent = "";
		// 错误描述;
		String strErrorDesc = "";
		// 错误码;
		String errorCode = "0";
		// 组件名称
		String comName = "TZ_ONLINE_REG_COM";
		// 页面名称
		String pageName = "TZ_ONREG_EXP_STD";
		// OPRID
		String oprid = gdKjComService.getOPRID(request);
		// OrgId
		String orgid = gdKjComService.getLoginOrgID(request, response);
		// 错误信息
		String[] errorMsg = { "0", "" };

		if (StringUtils.isBlank(orgid)) {
			errorCode = "1";
			strErrorDesc = "您当前没有机构，不能更新报名表模板页头页尾数据！";
		}

		if (StringUtils.equals("0", errorCode)) {
			if (StringUtils.isBlank(insid)) {
				errorCode = "1";
				strErrorDesc = "参数-报名表实例编号为空！";
			}
		}

		if (StringUtils.equals("0", errorCode)) {
			String sql = "SELECT TZ_JG_ID FROM PS_TZ_APPTPL_DY_T TPL, PS_TZ_APP_INS_T INS WHERE TPL.TZ_APP_TPL_ID = INS.TZ_APP_TPL_ID AND INS.TZ_APP_INS_ID = ? LIMIT 0,1";
			String tplOrgId = sqlQuery.queryForObject(sql, new Object[] { insid }, "String");
			if (!StringUtils.equals(orgid, tplOrgId)) {
				errorCode = "1";
				strErrorDesc = "您无权导出其他机构的报名表数据！";
			}
		}
		if (StringUtils.equals("0", errorCode)) {
			// 校验组件页面的读写访问权限
			boolean permission = tzComPageAuthServiceImpl.checkUpdatePermission(oprid, comName, pageName, errorMsg);
			if (permission) {
				// 更新权限
				String retJson = appFormExportClsServiceImpl.tzGetJsonData("{\"insid\":\"" + insid + "\"}");
				JacksonUtil jacksonUtil = new JacksonUtil();
				Map<String, Object> retMap = jacksonUtil.parseJson2Map(retJson);
				String code = retMap.get("code") == null ? "" : String.valueOf(retMap.get("code"));
				String msg = retMap.get("msg") == null ? "" : String.valueOf(retMap.get("msg"));
				String url = retMap.get("url") == null ? "" : String.valueOf(retMap.get("url"));
				String filename = retMap.get("filename") == null ? "" : String.valueOf(retMap.get("filename"));

				if (!StringUtils.equals(code, "0")) {
					errorCode = code;
					strErrorDesc = msg;
				} else {
					// get absolute path of the application
					ServletContext context = request.getServletContext();
					String appPath = context.getRealPath("");
					System.out.println("appPath = " + appPath);

					// construct the complete absolute path of the file
					String fullPath = appPath + url;
					File downloadFile = new File(fullPath);
					FileInputStream inputStream = new FileInputStream(downloadFile);

					// get MIME type of the file
					String mimeType = context.getMimeType(fullPath);
					if (mimeType == null) {
						// set to binary type if MIME mapping not found
						mimeType = "application/octet-stream";
					}
					System.out.println("MIME type: " + mimeType);

					// set content attributes for the response
					response.setContentType(mimeType + ";charset=UTF-8");
					response.setContentLength((int) downloadFile.length());

					// set headers for the response
					String headerKey = "Content-Disposition";
					filename = new String(filename.getBytes(), "ISO8859-1");

					String headerValue = String.format("attachment; filename=\"%s\"", filename);
					response.setHeader(headerKey, headerValue);

					// get output stream of the response
					OutputStream outStream = response.getOutputStream();

					byte[] buffer = new byte[BUFFER_SIZE];
					int bytesRead = -1;

					// write bytes read from the input stream into the output
					// stream
					while ((bytesRead = inputStream.read(buffer)) != -1) {
						outStream.write(buffer, 0, bytesRead);
					}

					inputStream.close();
					outStream.close();
				}
			} else {
				// 无更新权限
				errorCode = errorMsg[0];
				strErrorDesc = errorMsg[1];
			}
		}

		if ("1".equals(errorCode)) {
			strRetContent = strErrorDesc;
		}

		return strRetContent;
	}

	@RequestMapping(value = { "/exprar/{seqnum}" }, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportAppFormRar(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "seqnum") String seqnum) throws IOException {
		// 错误时的返回值
		String strRetContent = "";
		// 错误描述;
		String strErrorDesc = "";
		// 错误码;
		String errorCode = "0";
		// 组件名称
		String comName = "TZ_ONLINE_REG_COM";
		// 页面名称
		String pageName = "TZ_ONREG_EXP_STD";
		// OPRID
		String oprid = gdKjComService.getOPRID(request);
		// OrgId
		//String orgid = gdKjComService.getLoginOrgID(request, response);
		// 错误信息
		String[] errorMsg = { "0", "" };

		if (StringUtils.isBlank(seqnum)) {
			errorCode = "1";
			strErrorDesc = "下载的文件不存在！";
		}

		if (StringUtils.equals("0", errorCode)) {
			// 校验组件页面的读写访问权限
			boolean permission = tzComPageAuthServiceImpl.checkUpdatePermission(oprid, comName, pageName, errorMsg);
			if (permission) {
				
				
				// get absolute path of the application
				ServletContext context = request.getServletContext();
				//String appPath = context.getRealPath("");
				

				// construct the complete absolute path of the file
				//String fullPath = appPath + url;
				//File downloadFile = new File(fullPath);
				PsTzExcelDattT psTzExcelDattT = psTzExcelDattTMapper.selectByPrimaryKey(Integer.parseInt(seqnum));
				if(psTzExcelDattT == null){
					// 无更新权限
					errorMsg[0] = "1";
					errorMsg[1] = "不存在下载的文件";
					return "不存在下载的文件";
				}
				String fullPath = "";
				String lj = psTzExcelDattT.getTzFwqFwlj();
				String filename = psTzExcelDattT.getTzSysfileName();
				if(lj.lastIndexOf("/") + 1 == lj.length()){
					lj = lj + filename;
				}else{
					lj = lj + "/" + filename;
				}
				fullPath = request.getServletContext().getRealPath(lj);
				File downloadFile = new File(fullPath);
				FileInputStream inputStream = new FileInputStream(downloadFile);

				// get MIME type of the file
				String mimeType = context.getMimeType(fullPath);
				if (mimeType == null) {
					// set to binary type if MIME mapping not found
					mimeType = "application/octet-stream";
				}
				System.out.println("MIME type: " + mimeType);

				// set content attributes for the response
				response.setContentType(mimeType + ";charset=UTF-8");
				response.setContentLength((int) downloadFile.length());

				// set headers for the response
				String headerKey = "Content-Disposition";
				filename = new String(filename.getBytes(), "ISO8859-1");

				String headerValue = String.format("attachment; filename=\"%s\"", filename);
				response.setHeader(headerKey, headerValue);

				// get output stream of the response
				OutputStream outStream = response.getOutputStream();

				byte[] buffer = new byte[BUFFER_SIZE];
				int bytesRead = -1;

				// write bytes read from the input stream into the output
				// stream
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					outStream.write(buffer, 0, bytesRead);
				}

				inputStream.close();
				outStream.close();

			} else {
				// 无更新权限
				errorCode = errorMsg[0];
				strErrorDesc = errorMsg[1];
			}
		}

		if ("1".equals(errorCode)) {
			strRetContent = strErrorDesc;
		}

		return strRetContent;
	}
}
