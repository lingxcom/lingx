package com.lingx.core.service;

import com.lingx.core.engine.IContext;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年10月6日 下午6:21:12 
 * 类说明 
 */
public interface IDefaultValueService {
	/**
	 * 根据定义的缺省值，转成实际需要的值，单个替换
	 * @param source
	 * @return
	 */
	public String transform(String source,IContext context);
	/**
	 * 根据定义的缺省值，转成实际需要的值，多个替换
	 * @param source
	 * @return
	 */
	public String transforms(String source,IContext context);
}
