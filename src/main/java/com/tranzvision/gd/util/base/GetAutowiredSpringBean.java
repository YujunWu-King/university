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

import com.tranzvision.gd.util.cfgdata.GetCookieSessionProps;

/**
 * 获取自动注册的Spring Bean（java类实例）
 * 
 * @author SHIHUA
 * @since 2015-11-05
 */
public class GetAutowiredSpringBean {

	@Autowired
	private GetCookieSessionProps getCookieSessionProps;

	@Autowired
	private ApplicationContext ctx;

	private Map<String, Object> springBeanMap;

	public void init() {
		springBeanMap = new HashMap<String, Object>();
		springBeanMap.put("GetCookieSessionProps", getCookieSessionProps);
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
