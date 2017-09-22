package com.lingx.core.model.impl;

import com.lingx.core.model.IScriptApi;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月11日 下午7:17:46 
 * 类说明 
 */
public class DefaultScriptApi implements IScriptApi {
	private Object bean;
	@Override
	public Object getApi() {
		return bean;
	}
	public void setBean(Object bean) {
		this.bean = bean;
	}
	
}
