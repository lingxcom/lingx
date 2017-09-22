package com.lingx.core.test.spring.aop.impl;

import com.lingx.core.test.spring.aop.IPople;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月11日 下午11:24:16 
 * 类说明 
 */
public class Lingx implements IPople {
	private String city;
	@Override
	public IPople say() {
		System.out.println("my name is lingx !");
		return this;
	}
	@Override
	public IPople in() {

		System.out.println("in "+city+" now !");
		return this;
	}
	public void setCity(String city) {
		this.city = city;
	}

}
