package com.tranzvision.gd.util.base;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Wrapper to always return a reference to the Spring Application Context from
 * within non-Spring enabled beans. Unlike Spring MVC's
 * WebApplicationContextUtils we do not need a reference to the Servlet context
 * for this. All we need is for this bean to be initialized during application
 * startup.
 */
public class SpringApplicationContext implements ApplicationContextAware {

	private static ApplicationContext CONTEXT;

	/**
	 * This method is called from within the ApplicationContext once it is done
	 * starting up, it will stick a reference to itself into this bean.
	 * 
	 * @param context
	 *            a reference to the ApplicationContext.
	 */
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		CONTEXT = context;
	}

	/**
	 * This is about the same as context.getBean("beanID"), except it has its
	 * own static handle to the Spring context, so calling this method
	 * statically will give access to the beans by name in the Spring
	 * application context. As in the context.getBean("beanID") call, the
	 * caller must cast to the appropriate target class. If the bean does not
	 * exist, then a Runtime error will be thrown.
	 * 
	 * @param beanID
	 *            the id of the bean to get.
	 * @return an Object reference to the named bean.
	 */
	public static Object getBean(String beanID) {
		try {
			return CONTEXT.getBean(beanID);
		} catch (NoSuchBeanDefinitionException nsbde) {
			nsbde.printStackTrace();
		} catch (BeansException be) {
			be.printStackTrace();
		}
		return null;
	}
	
	/**
	 * return current applicationContext 
	 * @return 
	 */
	public static ApplicationContext getApplicationContext(){
		return CONTEXT;
	}
}