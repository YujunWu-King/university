/**
 * 
 */
package com.tranzvision.gd.util.base;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.tranzvision.gd.TZWeChatBundle.service.impl.TzWeChartOAuth2;
import com.tranzvision.gd.TZWeChatBundle.service.impl.TzWxApiObject;
import com.tranzvision.gd.TZWebSiteInfoBundle.service.impl.ArtContentHtml;
import com.tranzvision.gd.util.cfgdata.GetCookieSessionProps;
import com.tranzvision.gd.util.cookie.TzCookie;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 缓存并获取自动注册的Spring Bean（java类实例） 若要新增SpringBean，则用@Autowired注入后，
 * 在init方法中对该bean进行注册，加入到springBeanMap成员变量中 key是bean的类名
 * 
 * @author SHIHUA
 * @since 2015-11-05
 */
public class GetAutowiredSpringBean {

	@Autowired
	private GetCookieSessionProps getCookieSessionProps;

	@Autowired
	private TzCookie tzCookie;

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private ArtContentHtml artContentHtml;

	// 2017-5-25添加 tzGDObject
	@Autowired
	private TZGDObject tzGDObject;
	
	@Autowired
	private TzWeChartOAuth2 tzWeChartOAuth2;
	
	@Autowired
	private TzWxApiObject tzWxApiObject;

	@Autowired
	private ApplicationContext ctx;

	private Map<String, Object> springBeanMap;

	public void init() {
		springBeanMap = new HashMap<String, Object>();
		springBeanMap.put("GetCookieSessionProps", getCookieSessionProps);
		springBeanMap.put("TzCookie", tzCookie);
		springBeanMap.put("SqlQuery", sqlQuery);
		springBeanMap.put("ArtContent", artContentHtml);
		// 2017-5-25添加 tzGDObject
		springBeanMap.put("tzGDObject", tzGDObject);
		springBeanMap.put("tzWeChartOAuth2", tzWeChartOAuth2);
		springBeanMap.put("tzWxApiObject", tzWxApiObject);
	}

	/**
	 * 根据beanKey获取Bean对象
	 * 
	 * @param beanKey
	 * @return Object
	 */
	public Object getBeanByKey(String beanKey) {

		try {
			return springBeanMap.get(beanKey);
		} catch (ClassCastException cce) {
			cce.printStackTrace();
		} catch (NullPointerException npe) {
			npe.printStackTrace();
		}

		return null;
	}

	/**
	 * 根据beanID获取Bean对象
	 * 
	 * @param beanID
	 * @return
	 */
	public Object getBeanByID(String beanID) {
		try {
			return ctx.getBean(beanID);
		} catch (NoSuchBeanDefinitionException nsbde) {
			nsbde.printStackTrace();
		} catch (BeansException be) {
			be.printStackTrace();
		}
		return null;
	}

}
