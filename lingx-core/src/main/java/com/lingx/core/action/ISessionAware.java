package com.lingx.core.action;

import javax.servlet.http.HttpSession;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月25日 下午3:50:09 
 * 类说明 
 */
public interface ISessionAware {

	public void setSession(HttpSession session);
}
