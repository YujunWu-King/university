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
 * @since 2015-11-02
 */
@Service
public class GetCookieSessionProps {

	private Properties cookieSessioinProps;

	private int cookieMaxAge;

	private String cookieDomain;

	private String cookiePath;

	private boolean cookieHttpOnly;

	private boolean cookieSecure;
	
	private int sessionMaxInactive;

	/**
	 * 构造函数，加载Cookie配置默认值
	 */
	public GetCookieSessionProps() {
		Resource resource = new ClassPathResource("classpath:conf/cookie.properties");
		try {
			cookieSessioinProps = PropertiesLoaderUtils.loadProperties(resource);

			cookieMaxAge = Integer.parseInt(cookieSessioinProps.getProperty("cookieMaxAge"));
			cookieDomain = cookieSessioinProps.getProperty("cookieDomain");
			cookiePath = cookieSessioinProps.getProperty("cookiePath");
			cookieHttpOnly = Boolean.parseBoolean(cookieSessioinProps.getProperty("cookieHttpOnly"));
			cookieSecure = Boolean.parseBoolean(cookieSessioinProps.getProperty("cookieSecure"));
			
			sessionMaxInactive = Integer.parseInt(cookieSessioinProps.getProperty("sessionMaxInactive"));

		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getCookieMaxAge() {
		return cookieMaxAge;
	}

	public String getCookieDomain() {
		return cookieDomain;
	}

	public String getCookiePath() {
		return cookiePath;
	}

	public boolean getCookieHttpOnly() {
		return cookieHttpOnly;
	}

	public boolean getCookieSecure() {
		return cookieSecure;
	}
	
	public int getSessionMaxInactive(){
		return sessionMaxInactive;
	}

}
