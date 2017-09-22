package com.lingx.core.plugin.impl;

import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lingx.core.plugin.IPlugin;
//@Component
@Aspect
public class LoginPlugin implements IPlugin {
	private boolean enable;
	//@Pointcut("execution(* com.lingx.core.service.impl.LoginServiceImpl.*(..))")  
	@Pointcut("execution(* com.lingx.core.service.impl.LoginServiceImpl.login(..))")  
    private void aspect(){}//定义一个切入点  
	
	public String getName() {
		return "登录认证插件";
	}

	public String getVersion() {
		return "1.0";
	}

	@Around("aspect()")  
    public Object around(ProceedingJoinPoint pjp) throws Throwable{  
        System.out.println("进入环绕通知,入参:"+JSON.toJSONString(pjp.getArgs()));  
        Object object = pjp.proceed();//执行该方法  
        System.out.println("出参:"+JSON.toJSONString(object));  
        return object; 
    }

	@Override
	public String getId() {
		return "demo_login";
	}

	@Override
	public boolean isEnable() {
		return this.enable;
	}

	@Override
	public Map<String, Object> execute(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(Map<String, Object> params) {
		System.out.println("-------INIT--------:"+JSON.toJSONString(params));
	}

	@Override
	public void destory() {
		System.out.println("-------Destory--------");
	}

	@Override
	public void setEnable(boolean enable) {
		this.enable=enable;
		
	}

	@Override
	public void start() {

		System.out.println("-------start--------");
		
	}

	@Override
	public void stop() {

		System.out.println("-------stop--------");
		
	}

	@Override
	public String getDetail() {
		// TODO Auto-generated method stub
		return "测试用的";
	}

	@Override
	public String getAuthor() {
		// TODO Auto-generated method stub
		return "www.lingx.com";
	}  
	
}
