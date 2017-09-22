package com.lingx.core;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

public class SpringContext {
	private static ApplicationContext applicationContext; // Spring应用上下文环境

	public static void setApplicationContext(
			ApplicationContext applicationContext) {
		SpringContext.applicationContext = applicationContext;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static Object getBean(String name) throws BeansException {
		return applicationContext.getBean(name);
	}

	public static <T> T getBean(Class<T> clazz) throws BeansException {
		return applicationContext.getBean(clazz);
	}
	public static <T> T getBean(String name,Class<T> clazz) throws BeansException {
		return applicationContext.getBean(name,clazz);
	}
	
	public void refresh(){
		XmlWebApplicationContext context = (XmlWebApplicationContext)applicationContext;
		context.refresh();
	}
	
	
	
}
