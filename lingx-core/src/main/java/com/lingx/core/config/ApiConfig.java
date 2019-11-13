package com.lingx.core.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lingx.core.model.IScriptApi;
import com.lingx.core.model.impl.DefaultScriptApi;
import com.lingx.core.service.II18NService;
import com.lingx.core.service.ILingxService;
import com.lingx.core.service.IWorkflowService;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年8月18日 下午5:16:40 
 * 类说明 
 */
@Configuration
public class ApiConfig {
	@Resource
	private ILingxService lingxService;
	@Resource
	private IWorkflowService workflowService;

	@Resource
	private II18NService i18n;
	@Bean(name="LINGX")
	public IScriptApi getLingxApi(){
		DefaultScriptApi lingx=new DefaultScriptApi();
		lingx.setBean(this.lingxService);
		return lingx;
		
	}
	@Bean(name="SPRING")
	public IScriptApi getSpringApi(){
		DefaultScriptApi lingx=new DefaultScriptApi();
		lingx.setBean(this.lingxService);
		return lingx;
		
	}
	@Bean(name="WORKFLOW")
	public IScriptApi getWorkflowApi(){
		DefaultScriptApi wl=new DefaultScriptApi();
		wl.setBean(this.workflowService);
		return wl;
	}
	@Bean(name="I18N")
	public IScriptApi getI18N(){
		DefaultScriptApi i18n=new DefaultScriptApi();
		i18n.setBean(this.i18n);
		return i18n;
		
	}
	@Bean(name="LANGUAGE")
	public IScriptApi getI18N2(){
		DefaultScriptApi i18n=new DefaultScriptApi();
		i18n.setBean(this.i18n);
		return i18n;
		
	}
	
}
