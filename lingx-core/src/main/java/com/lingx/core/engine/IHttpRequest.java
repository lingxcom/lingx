package com.lingx.core.engine;

import java.util.Map;
import java.util.Set;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月5日 下午1:19:41 
 * 类说明 
 */
public interface IHttpRequest {
	/**
	 * 根据参数名获取请求参数值
	 * @param key
	 * @return
	 */
	String getParameter(String key);
	/**
	 * 根据参数名获取请求参数值，如果参数为空时，返回缺省值
	 * @param key
	 * @param defaultValue 
	 * @return
	 */
	String getParameter(String key,String defaultValue);
	/**
	 * 设置请求周期内属性
	 * @param key
	 * @param value
	 */
	void setAttribute(String key,Object value);
	/**
	 * 获取属性
	 * @param key
	 * @return
	 */
	Object getAttribute(String key);
	/**
	 * 获取请求所有参数值数组
	 * @param key
	 * @return
	 */
	String[] getParameterValues(String key);
	/**
	 * 获取所有属性
	 * @return
	 */
	Map<String,Object> getAttributes();
	/**
	 * 获取所有参数
	 * @return
	 */
	Map<String,String[]> getParameters();
	/**
	 * 获取所有参数名
	 * @return
	 */
	Set<String> getParameterNames();
}
