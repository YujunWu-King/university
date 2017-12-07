package com.tranzvision.gd.TZUniPrintBundle.service.impl;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.tranzvision.gd.TZApplicationTemplateBundle.service.impl.DataBean;

/**
 * PDF模板信息
 * @author LuYan 2017-12-6
 *
 */
public class PdfTemplateInfo {

	private Connection conn = null;

	public PdfTemplateInfo() {
	}

	/**
	 * 创建连接
	 * 
	 * @return
	 */
	private Connection getConnection() {
		try {
			Resource resource = new ClassPathResource("conf/jdbc.properties");
			Properties cookieSessioinProps = PropertiesLoaderUtils.loadProperties(resource);
			String driver = cookieSessioinProps.getProperty("jdbc_driverClassName").trim();
			String url = cookieSessioinProps.getProperty("jdbc_url").trim();
			String user = cookieSessioinProps.getProperty("jdbc_username").trim();
			String pass = cookieSessioinProps.getProperty("jdbc_password").trim();
			Class.forName(driver);
			this.conn = DriverManager.getConnection(url, user, pass);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.conn;
	}
	
	
	public DataBean getPdfTemplateURL(String templateID) {
		DataBean bean = new DataBean();
		// 连接符改为fieldName∨∨fieldValue∧∧fieldName∨∨fieldValue∧∧
		// 思路 1.根据模板实例ID 获取模板ID
		// 2.根据模板ID，获取PDF模板文件路径
		// 3.根据模板ID，得到 PDF模板文件 的填充项目 和 模板项之间的关系
		// 4.根据实例ID，获取该实例 模板项里面的具体内容
		// 5.调用PDF程序，生产PDF文件

		// 服务器路径
		String webAppRootKey = "";
		try {
			Resource resource = new ClassPathResource("conf/cookieSession.properties");
			Properties cookieSessioinProps = null;
			cookieSessioinProps = PropertiesLoaderUtils.loadProperties(resource);
			webAppRootKey = cookieSessioinProps.getProperty("webAppRootKey");
		} catch (IOException e) {
			e.printStackTrace();
			bean.setRs(-1);
			return bean;
		}

		String fieldName = ""; // pdf模版路径
		String filePath = ""; // 文件名
		String rootpath = "";
		Connection conn = null;
		try {
			conn = this.getConnection();

			String temp = this.getFIELD_PATH(templateID, conn);

			if (temp == null || temp.equals("")) {
				bean.setRs(-3);
				return bean;
			}

			filePath = StringUtils.split(temp, "|||")[0];
			fieldName = StringUtils.split(temp, "|||")[1];

			bean.setDownloadFileName(fieldName);

			bean.setFilePath(filePath);
			rootpath = System.getProperty(webAppRootKey);
			bean.setRootpath(rootpath);

			bean.setTemplateFileName(rootpath + filePath);
		} catch (Exception e) {
			e.printStackTrace();
			bean.setRs(-7);
			return bean;
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception ex) {
					ex.printStackTrace();
					bean.setRs(-7);
					return bean;
				}
			}
		}
		bean.setRs(0);
		return bean;
	}
	
	
	public byte[] downLoadPdfTemplate(String path) {

		try {
			InputStream fis = new BufferedInputStream(new FileInputStream(path));
			byte[] buffer = new byte[fis.available()];

			fis.read(buffer);
			fis.close();
			return buffer;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 获取PDF模板路径,以及文件的真是名
	 * 
	 * @param templateID
	 * @param conn
	 * @return PDF模板路径
	 */
	private String getFIELD_PATH(String templateID, Connection conn) {
		Statement stmt = null;
		ResultSet rt = null;
		String fieldName = null;
		try {
			stmt = conn.createStatement();
			String sql = " SELECT TZ_DYMB_PDF_NAME,TZ_DYMB_PDF_URL FROM PS_TZ_DYMB_T WHERE TZ_DYMB_ID='"
					+ templateID + "'";
			rt = stmt.executeQuery(sql);
			if ((rt != null) && rt.next()) {
				fieldName = rt.getString("TZ_DYMB_PDF_URL") + "|||" + rt.getString("TZ_DYMB_PDF_NAME");
			}
			rt.close();
			stmt.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return fieldName;
	}

	
	public void addFile(String templateID, String path, String FileName) {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = this.getConnection();
			stmt = conn.createStatement();
			String sql = "update PS_TZ_DYMB_T SET TZ_DYMB_PDF_NAME='"+ FileName +"',TZ_DYMB_PDF_URL='" + path + "' WHERE TZ_DYMB_ID='" + templateID +"'";
			stmt.execute(sql);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}
	
}
