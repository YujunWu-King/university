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
	
	private String webAppRootKey;

	private int cookieMaxAge;

	private String cookieDomain;

	private String cookiePath;

	private boolean cookieHttpOnly;

	private boolean cookieSecure;
	
	private int sessionMaxInactive;
	
	private boolean debug;

	/**
	 * 构造函数，加载Cookie配置默认值
	 */
	public GetCookieSessionProps() {
		this.doGetCookieSessionProps();
	}
	
	public void doGetCookieSessionProps(){
		Resource resource = new ClassPathResource("conf/cookieSession.properties");
		try {
			cookieSessioinProps = PropertiesLoaderUtils.loadProperties(resource);

			webAppRootKey = cookieSessioinProps.getProperty("webAppRootKey");
			
			cookieMaxAge = Integer.parseInt(cookieSessioinProps.getProperty("cookieMaxAge"));
			cookieDomain = cookieSessioinProps.getProperty("cookieDomain");
			cookiePath = cookieSessioinProps.getProperty("cookiePath");
			cookieHttpOnly = Boolean.parseBoolean(cookieSessioinProps.getProperty("cookieHttpOnly"));
			cookieSecure = Boolean.parseBoolean(cookieSessioinProps.getProperty("cookieSecure"));
			
			sessionMaxInactive = Integer.parseInt(cookieSessioinProps.getProperty("sessionMaxInactive"));
			
			debug = Boolean.parseBoolean(cookieSessioinProps.getProperty("debug"));

		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getWebAppRootKey(){
		return webAppRootKey;
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
	
	public boolean getDebug(){
		return debug;
	}

}
