package com.lingx.core.service;

import com.lingx.core.engine.IContext;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年6月11日 下午10:08:08 
 * 类说明 
 */
public interface ILoginService {
	/**
	 * 登录之前的一些特殊操作，不影响认证流程
	 * @param userid
	 */
	public boolean before(String userid,IContext context);
	/**
	 * 
	 * @param userid
	 * @param password
	 * @param context
	 * @return
	 */
	public boolean login(String userid,String password,IContext context);
	/**
	 * 
	 * @param userid
	 * @return
	 */
	public String after(String userid,IContext context);
}
