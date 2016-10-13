package com.tranzvision.gd.TZBaseBundle.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tranzvision.gd.TZApplicationTemplateBundle.service.impl.DataBean;
import com.tranzvision.gd.TZApplicationTemplateBundle.service.impl.PdfPrintbyModel;
import com.tranzvision.gd.util.base.JacksonUtil;

/**
 * @author caoy
 * @version 创建时间：2016年6月22日 上午11:20:08 类说明
 */
@Controller
@RequestMapping(value = "/")
public class FileDownloadController {

	@RequestMapping(value = "DownPdfServlet", produces = "text/html;charset=UTF-8")
	public @ResponseBody String orgDownloadFileHandler(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, Object> allRequestParams) {
		// System.out.println("PDF实例下载");
		String instanceID = String.valueOf(allRequestParams.get("instanceID"));
		String fileName = String.valueOf(allRequestParams.get("fileName"));

		// System.out.println("instanceID：" + instanceID);
		// System.out.println("fileName：" + fileName);

		PdfPrintbyModel ppm = new PdfPrintbyModel();
		DataBean bean = ppm.checkDateAndGetPdfData(instanceID, "", fileName, "A");
		if (bean.getRs() == 0) {
			try {
				String userAgent = request.getHeader("User-Agent").toUpperCase();
				if (userAgent != null && (userAgent.indexOf("MSIE") > 0 || userAgent.indexOf("LIKE GECKO")>0)) {
					fileName = URLEncoder.encode(bean.getDownloadFileName(), "UTF-8");
					if (fileName.length() > 150) {
						// 根据request的locale 得出可能的编码， 中文操作系统通常是gb2312
						String guessCharset = "gb2312";
						fileName = new String(bean.getDownloadFileName().getBytes(guessCharset), "ISO8859-1");
					}
				} else {
					fileName = new String(bean.getDownloadFileName().getBytes("UTF-8"), "ISO8859-1");
				}
				

			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			response.setContentType("multipart/form-data");
			// 2.设置文件头：最后一个参数是设置下载文件名
			response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
			// response.reset();
			ServletOutputStream out;
			try {

				// 3.通过response获取ServletOutputStream对象(out)
				out = response.getOutputStream();
				ppm.pdfPrintAndDownLoad(out, bean);
				out.close();
				out.flush();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// System.out.println("msg：" + bean.getMsg());

		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("success", bean.getRs() == 0 ? true : false);
		mapRet.put("msg", bean.getMsg());
		JacksonUtil jacksonUtil = new JacksonUtil();
		return jacksonUtil.Map2json(mapRet);
		// return retJson;
	}

	/**
	 * 下载上传过的PDF模板文件
	 * 
	 * @param request
	 * @param response
	 * @param allRequestParams
	 * @return
	 */
	@RequestMapping(value = "DownPdfTServlet", produces = "text/html;charset=UTF-8")
	public @ResponseBody String orgDownloadPdfTHandler(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, Object> allRequestParams) {
		// System.out.println("PDF实例下载");
		String templateID = String.valueOf(allRequestParams.get("templateID"));

		PdfPrintbyModel ppm = new PdfPrintbyModel();
		DataBean bean = ppm.getPdfTemplateURL(templateID);
		String fileName = "";
		if (bean.getRs() == 0) {
			try {
				String userAgent = request.getHeader("User-Agent").toUpperCase();
				if (userAgent != null && (userAgent.indexOf("MSIE") > 0 || userAgent.indexOf("LIKE GECKO")>0)) {
					fileName = URLEncoder.encode(bean.getDownloadFileName(), "UTF-8");
					if (fileName.length() > 150) {
						// 根据request的locale 得出可能的编码， 中文操作系统通常是gb2312
						String guessCharset = "gb2312";
						fileName = new String(bean.getDownloadFileName().getBytes(guessCharset), "ISO8859-1");
					}
				} else {
					fileName = new String(bean.getDownloadFileName().getBytes("UTF-8"), "ISO8859-1");
				}

			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			ServletOutputStream out;
			try {

				byte[] buffer = ppm.downLoadPdfTemplate(bean.getTemplateFileName());

				if (buffer == null || buffer.length <=0) {
					Map<String, Object> mapRet = new HashMap<String, Object>();
					mapRet.put("success", false);
					mapRet.put("msg", "模板文件不存在");
					JacksonUtil jacksonUtil = new JacksonUtil();
					return jacksonUtil.Map2json(mapRet);
				} else {

					response.setContentType("multipart/form-data");
					// 2.设置文件头：最后一个参数是设置下载文件名
					response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
					// response.reset();
					// 3.通过response获取ServletOutputStream对象(out)
					out = response.getOutputStream();
					out.write(buffer);// 输出文件
					out.close();
					out.flush();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// System.out.println("msg：" + bean.getMsg());

		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("success", bean.getRs() == 0 ? true : false);
		mapRet.put("msg", bean.getMsg());
		JacksonUtil jacksonUtil = new JacksonUtil();
		return jacksonUtil.Map2json(mapRet);
		// return retJson;
	}
}