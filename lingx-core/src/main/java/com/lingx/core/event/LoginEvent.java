package com.lingx.core.event;

import org.springframework.context.ApplicationEvent;

import com.lingx.core.model.bean.UserBean;

/** 
 * @author www.lingx.com
 * @version 创建时间：2017年5月16日 下午5:53:09 
 * 类说明 
 */
public class LoginEvent  extends ApplicationEvent{
	private static final long serialVersionUID = -1400732983313152359L;
	private UserBean userBean;
	public LoginEvent(Object source,UserBean userBean) {
		super(source);
		this.userBean=userBean;
	}
	public UserBean getUserBean() {
		return userBean;
	}
	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}


}
