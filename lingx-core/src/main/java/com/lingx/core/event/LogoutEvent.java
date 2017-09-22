package com.lingx.core.event;

import org.springframework.context.ApplicationEvent;

import com.lingx.core.model.bean.UserBean;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年6月30日 上午11:46:22 
 * 类说明 
 */
public class LogoutEvent extends ApplicationEvent{

	private static final long serialVersionUID = -3811605853174062944L;
	private UserBean userBean;
	public LogoutEvent(Object source,UserBean userBean) {
		super(source);
		this.userBean=userBean;
	}
	public UserBean getUserBean() {
		return userBean;
	}

}
