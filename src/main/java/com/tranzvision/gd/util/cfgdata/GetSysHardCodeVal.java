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
	 * 菜单树名称
	 */
	private String menuTreeName;

	/**
	 * 日期格式
	 */
	private String dateFormat;

	/**
	 * 日期时间格式
	 */
	private String dateTimeFormat;

	/**
	 * 上传文件，禁止的后缀名
	 */
	private ArrayList<String> fileUploadDeniedExtensions;

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
	 * 图片文件的后缀
	 */
	public ArrayList<String> imageSuffix;

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

			menuTreeName = sysHardCodeValProps.getProperty("MenuTreeName");

			dateFormat = sysHardCodeValProps.getProperty("DateFormate");

			dateTimeFormat = sysHardCodeValProps.getProperty("DateTimeFormate");

			imageSuffix = this
					.stringToArrayList(sysHardCodeValProps.getProperty("ImageSuffix"));
			
			fileUploadDeniedExtensions = this
					.stringToArrayList(sysHardCodeValProps.getProperty("FileUploadDeniedExtensions"));

			fileUploadMaxSize = Long.parseLong(sysHardCodeValProps.getProperty("FileUploadMaxSize"));

			orgFileUploadPath = sysHardCodeValProps.getProperty("OrgFileUploadPath");

			websiteFileUploadPath = sysHardCodeValProps.getProperty("WebsiteFileUploadPath");

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

	public String getMenuTreeName() {
		return menuTreeName;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public String getDateTimeFormat() {
		return dateTimeFormat;
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

}
