package com.lingx.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lingx.core.model.IPage;
import com.lingx.core.model.impl.DefaultPage;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年8月18日 下午3:50:57 
 * 类说明 
 */
@Configuration
public class PageConfig {
	
	@Bean(name="page_login")
	@Value("#{configs['page.login']}")
	public IPage getLoginPage(String uri){
		return page(uri);
	}

	@Bean(name="page_login_mobile")
	@Value("#{configs['page.login.mobile']}")
	public IPage getLoginMobilePage(String uri){
		return page(uri);
	}
	
	@Bean(name="page_password")
	@Value("#{configs['page.password']}")
	public IPage getPasswordPage(String uri){
		return page(uri);
	}
	@Bean(name="page_main")
	@Value("#{configs['page.main']}")
	public IPage getMainPage(String uri){
		return page(uri);
	}
	@Bean(name="page_exception")
	@Value("#{configs['page.exception']}")
	public IPage getExceptionPage(String uri){
		return page(uri);
	}
	@Bean(name="page_no_permission")
	@Value("#{configs['page.no.permission']}")
	public IPage getNoPermissionPage(String uri){
		return page(uri);
	}

	@Bean(name="page_timeout")
	@Value("#{configs['page.timeout']}")
	public IPage getTimeoutPage(String uri){
		return page(uri);
	}
	@Bean(name="page_upload")
	@Value("#{configs['page.upload']}")
	public IPage getUploadPage(String uri){
		return page(uri);
	}
	
	//////////////////////////流程
	@Bean(name="page_wf_form")
	@Value("#{configs['page.wf.form']}")
	public IPage getWorkflowFormPage(String uri){
		return page(uri);
	}
	@Bean(name="page_wf_form2")
	@Value("#{configs['page.wf.form2']}")
	public IPage getWorkflowFormPage2(String uri){
		return page(uri);
	}
	@Bean(name="page_wf_form2_approval")
	@Value("#{configs['page.wf.form2.approval']}")
	public IPage getWorkflowFormPage2Approval(String uri){
		return page(uri);
	}
	@Bean(name="page_wf_success")
	@Value("#{configs['page.wf.success']}")
	public IPage getWorkflowSubmitPage(String uri){
		return page(uri);
	}
	@Bean(name="page_wf_error")
	@Value("#{configs['page.wf.error']}")
	public IPage getWorkflowErrorPage(String uri){
		return page(uri);
	}
	
	
	private IPage page(String uri){
		DefaultPage page=new DefaultPage();
		page.setUri(uri);
		return page;
	}
	
}
