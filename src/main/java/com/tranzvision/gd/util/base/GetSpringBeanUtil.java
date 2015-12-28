/**
 * 
 */
package com.tranzvision.gd.util.base;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

/**
 * 获取SpringBean工具类
 * 
 * @author SHIHUA
 * @since 2015-11-05
 */
public class GetSpringBeanUtil {

	private GetAutowiredSpringBean getASBean;

	/**
	 * 构造函数，获取GetAutowiredSpringBean类实例
	 */
	public GetSpringBeanUtil() {
		try {

			getASBean = (GetAutowiredSpringBean) SpringApplicationContext.getBean("springAutoWiredBeanFactory");

		} catch (NoSuchBeanDefinitionException nsbde) {
			nsbde.printStackTrace();
		} catch (BeansException be) {
			be.printStackTrace();
		}
	}

	/**
	 * 获取自动注册的SpringBean
	 * 
	 * @param beanKey
	 * @return Object
	 */
	public Object getAutowiredSpringBean(String beanKey) {
		try {
			return getASBean.getBeanByKey(beanKey);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("getASBean is null!");
		}
		return null;
	}

	/**
	 * 获取指定id的spring bean
	 * 
	 * @param beanID
	 * @return
	 */
	public Object getSpringBeanByID(String beanID) {
		return getASBean.getBeanByID(beanID);
	}

}
