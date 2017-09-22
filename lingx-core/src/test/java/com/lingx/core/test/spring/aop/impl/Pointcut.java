package com.lingx.core.test.spring.aop.impl;

import java.lang.reflect.Method;

import org.springframework.aop.support.NameMatchMethodPointcut;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月12日 上午12:17:21 
 * 类说明 
 */
public class Pointcut extends NameMatchMethodPointcut {
	
	private static final long serialVersionUID = 8797636166825076714L;
	private String[] names;
	@Override
    public boolean matches(Method method, Class targetClass){
		this.setMappedNames(names);
		return super.matches(method, targetClass);
	}
	public void setNames(String[] names) {
		this.names = names;
	}
}
