package com.lingx.core.service;
/** 
 * @author www.lingx.com
 * @version 创建时间：2015年8月25日 上午9:39:07 
 * 类说明 
 */
public interface IConfigService {
	/**
	 * 取值
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public String getValue(String key,String defaultValue);

	public String getValue(String key);

	public int getIntValue(String key,int defaultValue);
	
	public int getIntValue(String key);
	

	public String getValue(String key,String defaultValue,String appid);

	public int getIntValue(String key,int defaultValue,String appid);
	
	/**
	 * 重置参数
	 */
	public void reset();
}
