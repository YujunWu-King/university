/**
 * 
 */
package com.tranzvision.gd.util.cfgdata;

import java.io.IOException;
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

	private String sysDefaultLanguage;

	private String menuTreeName;

	private String dateFormat;

	private String dateTimeFormat;

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

		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
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

}
