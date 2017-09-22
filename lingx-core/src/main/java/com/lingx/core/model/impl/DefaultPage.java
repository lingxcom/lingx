package com.lingx.core.model.impl;

import com.lingx.core.model.IPage;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月10日 下午1:44:02 
 * 类说明 
 */
public class DefaultPage implements IPage {
	private String uri;

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
}
