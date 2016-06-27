package com.tranzvision.gd.TZApplicationTemplateBundle.service.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Properties;
import javax.servlet.ServletOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 * @author caoy
 * @version 创建时间：2016年6月20日 下午4:26:01 类说明 PDF 模版打印
 */
public class PdfPrintbyModel {
	private Connection conn = null;

	public PdfPrintbyModel() {
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

	/**
	 * 获取模板ID
	 * 
	 * @param TZ_APP_INS_ID
	 * @param conn
	 * @return 模板ID
	 */
	private String getTemplateID(String TZ_APP_INS_ID, Connection conn) {
		Statement stmt = null;
		ResultSet rt = null;
		String templateID = null;
		try {
			stmt = conn.createStatement();
			String sql = "select TZ_APP_TPL_ID from PS_TZ_APP_INS_T where TZ_APP_INS_ID='" + TZ_APP_INS_ID + "'";
			rt = stmt.executeQuery(sql);
			if ((rt != null) && rt.next()) {
				templateID = rt.getString("TZ_APP_TPL_ID");
			}
			rt.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return templateID;
	}

	/**
	 * 获取推荐信实例ID
	 * 
	 * @param TZ_APP_INS_ID
	 * @param conn
	 * @return
	 */
	private String getRecommendID(String TZ_APP_INS_ID, Connection conn) {
		Statement stmt = null;
		ResultSet rt = null;
		String bmbInsId = null;
		try {
			stmt = conn.createStatement();
			String sql = "SELECT TZ_APP_INS_ID FROM PS_TZ_KS_TJX_TBL WHERE TZ_TJX_APP_INS_ID = '" + TZ_APP_INS_ID
					+ "' LIMIT 1";
			rt = stmt.executeQuery(sql);
			if ((rt != null) && rt.next()) {
				bmbInsId = rt.getString("TZ_APP_INS_ID");
			}
			rt.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bmbInsId;
	}

	public void addFile(String templateID, String path, String FileName) {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = this.getConnection();
			stmt = conn.createStatement();
			String sql = "insert into PS_TZ_APP_PDFFIELD_T  (TZ_APP_TPL_ID,TZ_FIELD_PATH,TZ_FIELD_NAME,TZ_FIELD_STATUS) values ";
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
			String sql = "select TZ_FIELD_PATH,TZ_FIELD_NAME from PS_TZ_APP_PDFFIELD_T where TZ_APP_TPL_ID='"
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

	/**
	 * 获取用户姓名
	 * 
	 * @param TZ_APP_INS_ID
	 * @param conn
	 * @return 用户姓名
	 */
	private String getUserName(String TZ_APP_INS_ID, Connection conn) {
		Statement stmt = null;
		ResultSet rt = null;
		String userName = null;
		try {
			stmt = conn.createStatement();
			String sql = "select a.TZ_REALNAME from PS_TZ_AQ_YHXX_TBL a,PS_TZ_FORM_WRK_T b where a.OPRID=b.OPRID and b.TZ_APP_INS_ID='"
					+ TZ_APP_INS_ID + "'";
			rt = stmt.executeQuery(sql);
			if ((rt != null) && rt.next()) {
				userName = rt.getString("TZ_REALNAME");
			}
			rt.close();
			stmt.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return userName;
	}

	/**
	 * 获取班级名称
	 * 
	 * @param TZ_APP_INS_ID
	 * @param conn
	 * @return 班级名称
	 */
	private String getClassName(String TZ_APP_INS_ID, Connection conn) {
		Statement stmt = null;
		ResultSet rt = null;
		String className = null;
		try {
			stmt = conn.createStatement();
			String sql = "select a.TZ_CLASS_NAME from PS_TZ_CLASS_INF_T a,PS_TZ_FORM_WRK_T b where a.TZ_CLASS_ID=b.TZ_CLASS_ID and b.TZ_APP_INS_ID='"
					+ TZ_APP_INS_ID + "'";
			rt = stmt.executeQuery(sql);
			if ((rt != null) && rt.next()) {
				className = rt.getString("TZ_CLASS_NAME");
			}
			rt.close();
			stmt.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return className;
	}

	/**
	 * 获取模板里面的内容
	 * 
	 * @param TZ_APP_INS_ID
	 * @param conn
	 * @return
	 */
	private Hashtable<String, String> getInstance(String TZ_APP_INS_ID, Connection conn) {
		Hashtable<String, String> ht = new Hashtable<String, String>();
		Statement stmt = null;
		ResultSet rt = null;
		try {
			stmt = conn.createStatement();
			String TZ_APP_S_TEXT = "";
			String sql = "select TZ_XXX_BH,TZ_APP_S_TEXT,TZ_APP_L_TEXT from PS_TZ_APP_CC_T where TZ_APP_INS_ID='"
					+ TZ_APP_INS_ID + "'";
			rt = stmt.executeQuery(sql);
			while ((rt != null) && rt.next()) {
				TZ_APP_S_TEXT = rt.getString("TZ_APP_S_TEXT");
				if (TZ_APP_S_TEXT != null && !TZ_APP_S_TEXT.equals("")) {
					ht.put(rt.getString("TZ_XXX_BH"), TZ_APP_S_TEXT);
				} else {
					ht.put(rt.getString("TZ_XXX_BH"), rt.getString("TZ_APP_L_TEXT"));
				}
			}
			rt.close();
			stmt.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return ht;
	}

	/**
	 * 获取机构ID
	 * 
	 * @param templateID
	 * @param conn
	 * @return 机构ID
	 */
	private String getJGID(String templateID, Connection conn) {
		Statement stmt = null;
		ResultSet rt = null;
		String orgid = null;
		try {
			stmt = conn.createStatement();
			String sql = "select TZ_JG_ID,TZ_PDF_TYPE from PS_TZ_APPTPL_DY_T where TZ_APP_TPL_ID='" + templateID
					+ "' and TZ_PDF_TYPE='TPDF'";
			rt = stmt.executeQuery(sql);
			if ((rt != null) && rt.next()) {
				orgid = rt.getString("TZ_JG_ID");
			}
			rt.close();
			stmt.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return orgid;
	}

	/**
	 * 获取替代PDF模板文件的字符1串
	 * 
	 * @param templateID
	 * @param conn
	 * @param ht
	 * @return 机构ID
	 */
	private String getPdfFieldsValue(String templateID, Connection conn, Hashtable<String, String> ht) {
		Statement stmt = null;
		ResultSet rt = null;
		String fieldsV = null;
		try {
			StringBuffer fieldsValue = new StringBuffer();// 实例字符串

			stmt = conn.createStatement();
			String TZ_APP_PDF_FIELD = "";
			String sql = "select TZ_XXX_BH,TZ_APP_PDF_FIELD from PS_TZ_APP_PDFFIELDITEM_T where TZ_APP_TPL_ID='"
					+ templateID + "'";
			rt = stmt.executeQuery(sql);
			// fieldName∨∨fieldValue∧∧fieldName∨∨fieldValue∧∧*/
			while ((rt != null) && rt.next()) {
				TZ_APP_PDF_FIELD = rt.getString("TZ_APP_PDF_FIELD");
				if (TZ_APP_PDF_FIELD != null && !TZ_APP_PDF_FIELD.equals("")) {
					fieldsValue.append(TZ_APP_PDF_FIELD);
					fieldsValue.append("∨∨");
					fieldsValue.append(ht.get(rt.getString("TZ_XXX_BH")));
					fieldsValue.append("∧∧");
				}
			}
			fieldsV = fieldsValue.toString();
			if (fieldsV != null && !fieldsV.equals("")) {
				fieldsV = fieldsV.substring(0, fieldsV.length() - 2);
			}
			rt.close();
			stmt.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return fieldsV;
	}

	/**
	 * 校验并获取中间参数
	 * 
	 * @param TZ_APP_INS_ID
	 *            PDF文件存放路径
	 * @param path
	 *            存在的跟路径 可不填写
	 * @param downloadFileName
	 *            下载的文件名称 可以不填写，如不填写 则程序生成 为 班级名_报名人姓名.pdf
	 * @param type
	 *            类型("A":报名表， "B": 推荐信)
	 * @return
	 */
	public DataBean checkDateAndGetPdfData(String TZ_APP_INS_ID, String path, String downloadFileName, String type) {
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
		String templateID = ""; // 模版ID
		String fieldName = ""; // pdf模版路径
		String orgid = ""; // 机构ID
		String rootpath = "";
		String fieldsV = "";

		String bmbInsId = TZ_APP_INS_ID;

		Connection conn = null;
		try {
			conn = this.getConnection();

			if (StringUtils.equals("B", type)) {
				bmbInsId = this.getRecommendID(TZ_APP_INS_ID, conn);
			}

			templateID = this.getTemplateID(bmbInsId, conn);
			if (templateID == null || templateID.equals("")) {
				bean.setRs(-2);
				return bean;
			} else {
				bean.setTemplateID(templateID);
			}

			fieldName = this.getFIELD_PATH(templateID, conn);

			if (fieldName == null || fieldName.equals("")) {
				bean.setRs(-3);
				return bean;
			}

			fieldName = StringUtils.split(fieldName, "|||")[0];

			// 如果方法没有传下载文件名，那么需要自己拼装
			if (downloadFileName == null || downloadFileName.equals("") || downloadFileName.equals("null")) {
				// 报名的班级名称_报名人姓名.pdf
				String userName = this.getUserName(bmbInsId, conn);
				if (userName == null || userName.equals("")) {
					bean.setRs(-10); // 报名人姓名不存在
					return bean;
				}
				bean.setUserName(userName);
				String className = this.getClassName(bmbInsId, conn);
				if (className == null || className.equals("")) {
					bean.setRs(-11); // 报名的班级名称不存在
					return bean;
				}
				// System.out.println("aaa="+className + "_" + userName +
				// ".pdf");
				bean.setDownloadFileName(className + "_" + userName + ".pdf");
			} else {
				bean.setDownloadFileName(downloadFileName);
				// System.out.println("bb="+downloadFileName);
			}

			rootpath = System.getProperty(webAppRootKey);
			// 非容器内，获取系统路径是失败的
			if (rootpath == null) {
				rootpath = path;
				// rootpath =
				// "D:/workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/university";
			}
			bean.setRootpath(rootpath);
			fieldName = rootpath + fieldName;
			bean.setTemplateFileName(fieldName);

			orgid = this.getJGID(templateID, conn);
			if (orgid == null || orgid.equals("")) {
				bean.setRs(-4);
				return bean;
			} else {
				bean.setOrgid(orgid);
			}

			Hashtable<String, String> ht = this.getInstance(bmbInsId, conn);

			if (ht == null || ht.size() <= 0) {
				bean.setRs(-9);
				return bean;
			}
			fieldsV = this.getPdfFieldsValue(templateID, conn, ht);
			if (fieldsV == null || fieldsV.equals("")) {
				bean.setRs(-5);
				return bean;
			} else {
				bean.setPdfFieldsValues(fieldsV);
			}

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

	/**
	 * 下载模板
	 * 
	 * @param out
	 *            输出流
	 * @param bean
	 *            存放中间参数的bean
	 * @return
	 */
	public void pdfPrintAndDownLoad(ServletOutputStream out, DataBean bean) {
		if (bean.getRs() == 0) {
			TzITextUtil t = new TzITextUtil();
			boolean b = t.setFieldsValue(bean.getTemplateFileName(), bean.getPdfFieldsValues(), out);
			if (!b) {
				bean.setRs(-6);
			}
		}
	}

	/**
	 * 打印PDF文件
	 * 
	 * @param path
	 *            PDF文件存放路径
	 * @param TZ_APP_INS_ID
	 *            实例编号
	 * @param type
	 *            类型("A":报名表， "B": 推荐信)
	 * @return
	 */
	public String createPdf(String path, String TZ_APP_INS_ID, String type) {
		DataBean bean = this.checkDateAndGetPdfData(TZ_APP_INS_ID, null, null, type);
		boolean b = false;
		if (bean.getRs() == 0) {

			// 检测文件夹是否存在，如果不存在，创建文件夹
			File dir = new File(path);
			// //System.out.println(dir.getAbsolutePath());
			if (!dir.exists()) {
				dir.mkdirs();
			}

			if (!StringUtils.endsWith(path, "/")) {
				path = path + "/";
			}

			// 报名人姓名_报名表.pdf 或 报名表姓名_推荐信.pdf
			if (StringUtils.equals("A", type)) {
				path = path + bean.getUserName() + "_报名表.pdf";
			} else {
				path = path + bean.getUserName() + "_推荐信.pdf";
			}
			System.out.println("path = " + path);
			TzITextUtil t = new TzITextUtil();
			b = t.setFieldsValue(bean.getTemplateFileName(), bean.getPdfFieldsValues(), path);
			if (!b) {
				bean.setRs(-6);
			}
		}
		if (bean.getRs() == 0) {
			return bean.getMsg() + "|||" + path;
		} else {
			return bean.getMsg();
		}
	}

	/**
	 * 打印报名表到指定位置
	 * 
	 * @param TZ_APP_INS_ID
	 *            模版实例ID
	 * @param path
	 *            path为系统根路径 可传可不传（在容器里面调用不需要传，外部调用需要）
	 * @return
	 */
	public String pdfPrint(String TZ_APP_INS_ID, String path) {
		DataBean bean = this.checkDateAndGetPdfData(TZ_APP_INS_ID, path, "a", "A");

		boolean b = false;
		String outputfile = null;
		if (bean.getRs() == 0) {
			/// bmb/singlepdf/" + orgid + "/时间/" + instance+/+TZ_APP_INS_ID
			outputfile = bean.getRootpath() + "/bmb/singlepdf/" + bean.getOrgid() + "/" + this.getDateNow()
					+ "/instance/" + TZ_APP_INS_ID + "/";
			String suffix = bean.getTemplateFileName().substring(bean.getTemplateFileName().lastIndexOf(".") + 1);
			String sysFileName = (new StringBuilder(String.valueOf(getNowTime()))).append(".").append(suffix)
					.toString();
			if (sysFileName.indexOf('/') != -1) {
				sysFileName = sysFileName.substring(sysFileName.lastIndexOf('/') + 1);
			}
			// 检测文件夹是否存在，如果不存在，创建文件夹
			File dir = new File(outputfile);
			// //System.out.println(dir.getAbsolutePath());
			if (!dir.exists()) {
				dir.mkdirs();
			}
			outputfile = outputfile + sysFileName;
			System.out.println("outputfile = " + outputfile);

			TzITextUtil t = new TzITextUtil();
			b = t.setFieldsValue(bean.getTemplateFileName(), bean.getPdfFieldsValues(), outputfile);
			if (!b) {
				bean.setRs(-6);
			}
		}
		if (bean.getRs() == 0) {
			return bean.getMsg() + "|||" + outputfile;
		} else {
			return bean.getMsg();
		}

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

	public static void main(String[] args) {
		// TODO Auto-generated method stub22
		// 251
		// PdfPrintbyModel a = new PdfPrintbyModel();
		// System.out.println(a.createPdf("D:\\", "252", "A"));

		String a = "111|||222";
		System.out.println(StringUtils.split(a, "|||")[0]);
		System.out.println(StringUtils.split(a, "|||")[1]);
	}

}
