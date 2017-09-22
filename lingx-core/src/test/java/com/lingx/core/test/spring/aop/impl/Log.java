package com.lingx.core.test.spring.aop.impl;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月11日 下午11:40:50 
 * 类说明 
 */
public class Log implements   MethodInterceptor{
	//,MethodBeforeAdvice,AfterReturningAdvice,ThrowsAdvice
	@Override
	public Object invoke(MethodInvocation arg0) throws Throwable {
		System.out.println("Interceptor");
		return arg0.proceed();
	}



}
