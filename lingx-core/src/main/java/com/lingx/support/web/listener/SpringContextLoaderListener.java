package com.lingx.support.web.listener;

import javax.servlet.ServletContextEvent;

import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.lingx.core.SpringContext;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年6月30日 上午11:53:30 
 * 类说明 
 */
public class SpringContextLoaderListener  extends ContextLoaderListener{

	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		SpringContext.setApplicationContext(WebApplicationContextUtils
				.getWebApplicationContext(event.getServletContext()));
	}
	
	public void  contextDestroyed(ServletContextEvent event) {
		super.contextDestroyed(event);
	}
}
