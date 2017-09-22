package com.lingx.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lingx.core.model.IValue;
import com.lingx.support.model.value.AppIdValue;
import com.lingx.support.model.value.AppNameValue;
import com.lingx.support.model.value.DateTimeValue;
import com.lingx.support.model.value.DateValue;
import com.lingx.support.model.value.OrgIdValue;
import com.lingx.support.model.value.OrgNameValue;
import com.lingx.support.model.value.UUIDValue;
import com.lingx.support.model.value.UserIdValue;
import com.lingx.support.model.value.UserNameValue;
import com.lingx.support.model.value.YearValue;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年8月18日 下午5:16:40 
 * 类说明 
 */
@Configuration
public class ValueConfig {
	@Bean
	public IValue getValue1(){
		IValue value=new DateValue();
		return value;
		
	}
	@Bean
	public IValue getValue2(){
		IValue value=new DateTimeValue();
		return value;
	}
	@Bean
	public IValue getValue3(){
		IValue value=new UserIdValue();
		return value;
	}
	@Bean
	public IValue getValue4(){
		IValue value=new UserNameValue();
		return value;
	}
	@Bean
	public IValue getValue5(){
		IValue value=new OrgIdValue();
		return value;
	}
	@Bean
	public IValue getValue6(){
		IValue value=new OrgNameValue();
		return value;
	}
	@Bean
	public IValue getValue7(){
		IValue value=new AppIdValue();
		return value;
	}
	@Bean
	public IValue getValue8(){
		IValue value=new AppNameValue();
		return value;
	}
	@Bean
	public IValue getValue9(){
		IValue value=new YearValue();
		return value;
	}
	@Bean
	public IValue getValue10(){
		IValue value=new UUIDValue();
		return value;
	}
}
