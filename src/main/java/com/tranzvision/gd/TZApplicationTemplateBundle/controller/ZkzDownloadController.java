package com.tranzvision.gd.TZApplicationTemplateBundle.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tranzvision.gd.util.sql.SqlQuery;

@Controller
@RequestMapping(value = "/")
public class ZkzDownloadController {
	@Autowired
	private SqlQuery jdbcTemplate;
	// 下载准考证doc;
	public  void zkzDownloadFile(HttpServletRequest request, HttpServletResponse response, String url, String instanceID) {
		String filePath = request.getServletContext().getRealPath(url);
		// 获取面试申请号和姓名2017-12-12 
		String fileName = "";
		String sqlSelectOprid = "SELECT ROW_ADDED_OPRID FROM PS_TZ_APP_INS_T WHERE TZ_APP_INS_ID=?";
		Map<String, Object> mapDataOprid = jdbcTemplate.queryForMap(sqlSelectOprid, new Object[] { instanceID });
		String strOPRID = mapDataOprid.get("ROW_ADDED_OPRID") == null ? "" : mapDataOprid.get("ROW_ADDED_OPRID").toString();

		String sqlSelectName = "SELECT TZ_REALNAME,TZ_MSSQH FROM PS_TZ_REG_USER_T A WHERE OPRID=?";
		Map<String, Object> mapDataName = jdbcTemplate.queryForMap(sqlSelectName, new Object[] { strOPRID });
		if (mapDataName != null) {
			String strName = mapDataName.get("TZ_REALNAME") == null ? "" : mapDataName.get("TZ_REALNAME").toString();
			String strMssqh = mapDataName.get("TZ_MSSQH") == null ? "" : mapDataName.get("TZ_MSSQH").toString();
			// 下载的文件名：面试申请号_姓名
			fileName = strMssqh + "_" + strName + ".pdf";
		}else{
			// 下载的文件名
			fileName = "zkz_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".pdf";
		}
		// 中文解码
		/*try {
			fileName = URLEncoder.encode(fileName,"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}*/
		// 判断浏览器
		try {
			String userAgent = request.getHeader("User-Agent").toUpperCase();
			if (userAgent != null && (userAgent.indexOf("MSIE") > 0 || userAgent.indexOf("LIKE GECKO")>0)) {
				fileName = URLEncoder.encode(fileName, "UTF-8");
				if (fileName.length() > 150) {
					// 根据request的locale 得出可能的编码， 中文操作系统通常是gb2312
					String guessCharset = "gb2312";
					fileName = new String(fileName.getBytes(guessCharset), "ISO8859-1");
				}
			} else {
				fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
			}
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
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
