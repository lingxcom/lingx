package com.lingx.core.service;

import com.lingx.core.model.bean.UserBean;

/** 
 * @author www.lingx.com
 * @version 创建时间：2016年2月24日 上午9:26:40 
 * 类说明 
 */
public interface ICreateService {
	/**
	 * 根据配置来创建表与对象
	 * @param name
	 * @param options
	 * @return
	 */
	public boolean create(String name,String options,String appid,UserBean userBean);
}
