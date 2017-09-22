package com.lingx.core.service;
/** 
 * @author www.lingx.com
 * @version 创建时间：2016年2月24日 下午4:58:30 
 * APP创建与初始化，删除
 */
public interface IAppService {

	public boolean createApp(String name);
	public boolean deleteApp(String appId);
}
