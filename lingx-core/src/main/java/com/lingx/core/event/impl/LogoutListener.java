package com.lingx.core.event.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.lingx.core.event.LogoutEvent;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年6月30日 下午12:00:29 
 * 类说明 
 */
@Component
public class LogoutListener implements ApplicationListener<LogoutEvent> {
	public static Logger logger = LogManager.getLogger(LogoutListener.class);

	@Override
	public void onApplicationEvent(LogoutEvent event) {
		logger.debug(event.getUserBean().getAccount()+"退出系统");
	}

}
