/**
 * 
 */
package com.tranzvision.gd.util.cfgdata;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Service;

/**
 * 读取Cookie默认配置参数
 * 
 * @author SHIHUA
 * @since 2015-11-16
 */
@Service
public class GetSysHardCodeVal {

	private Properties sysHardCodeValProps;

	/**
	 * 系统默认语言
	 */
	private String sysDefaultLanguage;

	/**
	 * 平台机构ID
	 */
	private String platformOrgID;

	/**
	 * 菜单树名称
	 */
	private String menuTreeName;

	/**
	 * 日期格式
	 */
	private String dateFormat;

	/**
	 * 日期时间（小时:分钟）格式
	 */
	private String dateTimeHMFormat;

	/**
	 * 日期时间格式
	 */
	private String dateTimeFormat;

	/**
	 * 时间格式
	 */
	private String timeFormat;

	/**
	 * 时间（小时:分钟）格式
	 */
	private String timeHMFormat;

	/**
	 * 上传文件，禁止的后缀名
	 */
	private ArrayList<String> fileUploadDeniedExtensions;

	/**
	 * 图片文件的后缀
	 */
	public ArrayList<String> imageSuffix;

	/**
	 * 上传文件，允许的最大文件
	 */
	private long fileUploadMaxSize;

	/**
	 * 机构管理平台上传文件的路径
	 */
	private String orgFileUploadPath;

	/**
	 * 前台网站上传文件的路径
	 */
	private String websiteFileUploadPath;

	/**
	 * 临时文件存储路径
	 */
	private String tmpFileUploadPath;

	/**
	 * 前台网站css样式路径前缀
	 */
	private String websiteCssPath;

	/**
	 * 前台网站图片路径前缀
	 */
	private String websiteImgPath;

	/**
	 * 网站模板皮肤图片路径前缀
	 */
	private String websiteSkinsImgPath;

	/**
	 * 前台注册页面路径前缀
	 */
	private String websiteEnrollPath;

	/**
	 * 下载文件存储路径
	 */
	private String downloadPath;

	/**
	 * WkHtml2Pdf可执行文件
	 */
	private String wkHtml2Pdf;

	/**
	 * 存储单个导出PDF报名表的路径
	 */
	private String bmbSinglePdfDir;

	/**
	 * 存储批量打包报名表RAR文件的路径
	 */
	private String bmbPackRarDir;

	/**
	 * 构造函数，系统固定参数配置
	 */
	public GetSysHardCodeVal() {
		this.doGetSysHardCodeValProps();
	}

	public void doGetSysHardCodeValProps() {
		Resource resource = new ClassPathResource("conf/sysHardCodeVal.properties");
		try {
			sysHardCodeValProps = PropertiesLoaderUtils.loadProperties(resource);

			sysDefaultLanguage = sysHardCodeValProps.getProperty("SysDefaultLanguage");

			platformOrgID = sysHardCodeValProps.getProperty("PlatformOrgID");

			menuTreeName = sysHardCodeValProps.getProperty("MenuTreeName");

			dateFormat = sysHardCodeValProps.getProperty("DateFormat");

			dateTimeHMFormat = sysHardCodeValProps.getProperty("DateTimeHMFormat");

			dateTimeFormat = sysHardCodeValProps.getProperty("DateTimeFormat");

			timeFormat = sysHardCodeValProps.getProperty("TimeFormat");

			timeHMFormat = sysHardCodeValProps.getProperty("TimeHMFormat");

			imageSuffix = this.stringToArrayList(sysHardCodeValProps.getProperty("ImageSuffix"));

			fileUploadDeniedExtensions = this
					.stringToArrayList(sysHardCodeValProps.getProperty("FileUploadDeniedExtensions"));

			fileUploadMaxSize = Long.parseLong(sysHardCodeValProps.getProperty("FileUploadMaxSize"));

			orgFileUploadPath = sysHardCodeValProps.getProperty("OrgFileUploadPath");

			websiteFileUploadPath = sysHardCodeValProps.getProperty("WebsiteFileUploadPath");

			tmpFileUploadPath = sysHardCodeValProps.getProperty("TmpFileUploadPath");

			websiteCssPath = sysHardCodeValProps.getProperty("WebsiteCssPath");

			websiteImgPath = sysHardCodeValProps.getProperty("WebsiteImgPath");

			websiteSkinsImgPath = sysHardCodeValProps.getProperty("WebsiteSkinsImgPath");

			websiteEnrollPath = sysHardCodeValProps.getProperty("WebsiteEnrollPath");

			downloadPath = sysHardCodeValProps.getProperty("DownloadPath");

			wkHtml2Pdf = sysHardCodeValProps.getProperty("WkHtml2Pdf");

			bmbSinglePdfDir = sysHardCodeValProps.getProperty("BmbSinglePdfDir");

			bmbPackRarDir = sysHardCodeValProps.getProperty("BmbPackRarDir");

		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private ArrayList<String> stringToArrayList(String str) {
		String strArr[] = str.split("\\|");
		ArrayList<String> tmp = new ArrayList<String>();
		if (str.length() > 0) {
			for (int i = 0; i < strArr.length; i++)
				tmp.add(strArr[i].toLowerCase());

		}
		return tmp;
	}

	public String getSysDefaultLanguage() {
		return sysDefaultLanguage;
	}

	public String getPlatformOrgID() {
		return platformOrgID;
	}

	public String getMenuTreeName() {
		return menuTreeName;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public String getDateTimeHMFormat() {
		return dateTimeHMFormat;
	}

	public String getDateTimeFormat() {
		return dateTimeFormat;
	}

	public String getTimeFormat() {
		return timeFormat;
	}

	public String getTimeHMFormat() {
		return timeHMFormat;
	}

	public ArrayList<String> getImageSuffix() {
		return imageSuffix;
	}

	public ArrayList<String> getFileUploadDeniedExtensions() {
		return fileUploadDeniedExtensions;
	}

	public long getFileUploadMaxSize() {
		return fileUploadMaxSize;
	}

	public String getOrgFileUploadPath() {
		return orgFileUploadPath;
	}

	public String getWebsiteFileUploadPath() {
		return websiteFileUploadPath;
	}

	public String getTmpFileUploadPath() {
		return tmpFileUploadPath;
	}

	public String getWebsiteCssPath() {
		return websiteCssPath;
	}

	public String getWebsiteImgPath() {
		return websiteImgPath;
	}

	public String getWebsiteSkinsImgPath() {
		return websiteSkinsImgPath;
	}

	public String getWebsiteEnrollPath() {
		return websiteEnrollPath;
	}

	public String getDownloadPath() {
		return downloadPath;
	}

	public String getWkHtml2Pdf() {
		return wkHtml2Pdf;
	}

	public String getBmbSinglePdfDir() {
		return bmbSinglePdfDir;
	}

	public String getBmbPackRarDir() {
		return bmbPackRarDir;
	}

}
