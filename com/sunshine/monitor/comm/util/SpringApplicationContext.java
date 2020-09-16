package com.sunshine.monitor.comm.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class SpringApplicationContext implements ApplicationContextAware {

	public final Logger log = LoggerFactory.getLogger(SpringApplicationContext.class);

	ApplicationContext applicationContext;

	static ApplicationContext staticApplicationContext;

	/**
	 * SPRING AUTO INVOKE
	 */
	public void setApplicationContext(ApplicationContext applicationContext) {
		if (this.applicationContext == null) {
			this.applicationContext = applicationContext;
			if (staticApplicationContext == null) {
				staticApplicationContext = this.applicationContext;
			}
		}
	}

	/**
	 * TOMCAT startup invoke
	 * 
	 * @param applicationContext
	 */
	public static void initApplicationContext(
			ApplicationContext applicationContext) {

		staticApplicationContext = applicationContext;
	}

	/**
	 * GET VALUE METHOD
	 * 
	 * @param beanName
	 * @return
	 */
	public Object getBean(String beanName) {
		Object obj = this.applicationContext.getBean(beanName);
		if (obj == null) {
			throw new java.lang.IllegalArgumentException(
					" The bean's name of  " + beanName
							+ " can not be found in spring container!");
		}
		return obj;
	}

	/**
	 * 获取远程数据库连接
	 * 
	 * @param cityname
	 *            db_link name,example:"gz" means "广州"
	 * @param context
	 *            "jcbk" or "dc"
	 * @param ignore
	 *            是否忽略异常，true为：如果context为jcbk的数据源没找到，则寻找context为dc的数据源
	 * @return
	 */
	public static JdbcTemplate getRemoteSourse(String cityname, String context,
			boolean ignore) {
		Object obj = null;
		if ("jcbk".equalsIgnoreCase(context)) {
			try {
				obj = staticApplicationContext.getBean("jdbcTemplate" + cityname.toUpperCase() + "JCBK");
			} catch (Exception e) {
				e.printStackTrace();
				if (ignore) {
					obj = staticApplicationContext.getBean("jdbcTemplate" + cityname.toUpperCase());
				}
			}
		} else {
			obj = staticApplicationContext.getBean("jdbcTemplate" + cityname.toUpperCase());
		}
		if (obj == null) {
			return null;
		} else {
			return (JdbcTemplate) obj;
		}

	}

	/**
	 * 如果BeanFactory包含一个与所给名称匹配的bean定义，则返回true
	 * 
	 * @param name
	 * @return boolean
	 */
	public static boolean containsBean(String name) {

		return staticApplicationContext.containsBean(name);
	}

	/**
	 * 判断以给定名字注册的bean定义是一个singleton还是一个prototype。
	 * 如果与给定名字相应的bean定义没有被找到，将会抛出一个异常（NoSuchBeanDefinitionException）
	 * 
	 * @param name
	 * @return boolean
	 * @throws NoSuchBeanDefinitionException
	 */
	public static boolean isSingleton(String name) throws Exception {

		return staticApplicationContext.isSingleton(name);
	}

	/***
	 * 类似于getBean(String name)只是在参数中提供了需要返回到的类型。
	 * 
	 * @param name
	 * @param requiredType
	 * @return
	 * @throws BeansException
	 */
	public static <T> T getBean(String name, Class<T> requiredType)
			throws BeansException {

		return staticApplicationContext.getBean(name, requiredType);
	}

	public static ApplicationContext getStaticApplicationContext() {

		return staticApplicationContext;
	}
}
