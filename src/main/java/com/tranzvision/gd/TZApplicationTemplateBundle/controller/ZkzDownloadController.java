package com.tranzvision.gd.TZApplicationTemplateBundle.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/")
public class ZkzDownloadController {

	// 下载准考证doc;
	public  void zkzDownloadFile(HttpServletRequest request, HttpServletResponse response, String url) {
		String filePath = request.getServletContext().getRealPath(url);
		// 下载的文件名
		String fileName = "zkz_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".pdf";
		//File file = new File("E:\\zkz_20170907_164110.pdf");// 测试使用
		File file = new File(filePath);
		if (file.exists()) {
			// 设置文件头
			response.setContentType("multipart/form-data");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			try {
				FileInputStream fileInputStream = new FileInputStream(file);
				BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
				byte[] b = new byte[bufferedInputStream.available()];
				bufferedInputStream.read(b);
				ServletOutputStream outputStream = response.getOutputStream();
				outputStream.write(b);

				bufferedInputStream.close();
				outputStream.flush();
				outputStream.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
