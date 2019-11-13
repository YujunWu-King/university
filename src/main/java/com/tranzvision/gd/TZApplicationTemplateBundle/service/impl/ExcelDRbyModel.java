package com.tranzvision.gd.TZApplicationTemplateBundle.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class ExcelDRbyModel {
	
	private Connection conn = null;

	public ExcelDRbyModel() {
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
	
	public void addFile(String templateID, String path, String FileName) {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = this.getConnection();
			stmt = conn.createStatement();
			String sql = "insert into PS_TZ_APP_EXCEL_T  (TZ_APP_TPL_ID,TZ_FIELD_PATH,TZ_FIELD_NAME,TZ_FIELD_STATUS) values ";
			sql = sql + "('" + templateID + "','" + path + "','" + FileName + "','A')";
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
	

	public DataBean getExcelTemplateURL(String templateID) {
		DataBean bean = new DataBean();


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

			System.out.println("templateID=====" + templateID);
			System.out.println("conn=====" + conn);

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
			String sql = "select TZ_FIELD_PATH,TZ_FIELD_NAME from PS_TZ_APP_EXCEL_T where TZ_APP_TPL_ID='"
					+ templateID + "' and TZ_FIELD_STATUS='A'";
			rt = stmt.executeQuery(sql);
			if ((rt != null) && rt.next()) {
				fieldName = rt.getString("TZ_FIELD_PATH") + "|||" + rt.getString("TZ_FIELD_NAME");
			}
			rt.close();
			stmt.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return fieldName;
	}

}
