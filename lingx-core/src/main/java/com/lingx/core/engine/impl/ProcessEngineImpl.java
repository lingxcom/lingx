package com.lingx.core.engine.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lingx.core.Constants;
import com.lingx.core.Page;
import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IHttpRequest;
import com.lingx.core.engine.IMethodProcess;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.engine.IProcessEngine;
import com.lingx.core.exception.LingxCascadeException;
import com.lingx.core.exception.LingxChooseException;
import com.lingx.core.exception.LingxConvertException;
import com.lingx.core.exception.LingxProcessException;
import com.lingx.core.exception.LingxValidatorException;
import com.lingx.core.model.IEntity;
import com.lingx.core.model.IMethod;
import com.lingx.core.service.ICascadeService;
import com.lingx.core.service.IChooseService;
import com.lingx.core.service.IConverteService;
import com.lingx.core.service.IInterpretService;
import com.lingx.core.service.IPageService;
import com.lingx.core.service.IScriptApisService;
import com.lingx.core.service.IValidateService;
import com.lingx.core.utils.LingxUtils;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月5日 下午1:41:48 
 * 类说明 
 */
@Component(value="lingxProcessEngine")
public class ProcessEngineImpl implements IProcessEngine{
	public static final Logger logger = LogManager.getLogger(ProcessEngineImpl.class);
	@Resource
	private Map<String,IMethodProcess> methodProcessMap;
	@Resource
	private IChooseService chooseService;
	@Resource
	private IConverteService converteService;
	@Resource
	private IValidateService validateService;
	@Resource
	private IInterpretService interpretService;
	@Resource
	private ICascadeService cascadeService;
	@Resource
	private IScriptApisService scriptApisService;
	@Resource
	private IPageService pageService;
	public String process(IContext context)throws LingxProcessException,LingxChooseException,LingxConvertException,LingxValidatorException,LingxCascadeException{
		String retUri=null;
		IHttpRequest request;
		IEntity entity=null;
		IMethod method=null;
		Map<String,String> requestParams=null;
		boolean isValidator=false;

		request=context.getRequest();
		String type=request.getParameter(KEY_TYPE);
		entity=this.chooseService.getEntity(request.getParameter(KEY_ENTITY));
		/*
		 * 在GRID中多并发时，有可能出现方法生成有误，造成筛选条件组合不能正常完成；未解决
		 */
		method=this.chooseService.getMethod(request.getParameter(KEY_METHOD), entity);
		if(!method.getEnabled()){
			return this.pageService.getPage(Page.PAGE_NO_PERMISSION);
		}
		context.setEntity(entity);
		context.setMethod(method);
		requestParams=this.converteService.convert(method.getFields().getList(), context);
		if(type==null)type=String.valueOf(TYPE_VIEW);
		if("1".equals(request.getParameter("lgxsn"))||!"JSON".equalsIgnoreCase(method.getType()))type=String.valueOf(TYPE_PROCESS);
		switch(Integer.parseInt(type)){
			case TYPE_VIEW:
				this.cascadeService.getCascade(context);
				retUri=method.getViewUri();
				if(context.getRequest().getParameter("prefix")!=null){//为了界面能够支持extjs以外的
					retUri=context.getRequest().getParameter("prefix")+"/"+retUri;
				}
				break;
		
			case TYPE_PROCESS:
				IPerformer performer =new DefaultPerformer(scriptApisService.getScriptApis(),context.getRequest());
				requestParams=this.interpretService.inputFormat(requestParams, method.getFields().getList(), entity, context, performer);
				//System.out.println(JSON.toJSONString(requestParams));
				performer.addRequestParam(requestParams);
				performer.addParam("ENTITY_CODE", entity.getCode());
				performer.addParam("CONTEXT", context);
				performer.addParam("REQUEST", context.getRequest());
				isValidator=this.validateService.validator(method,context,performer);
				if(isValidator){
					IMethodProcess methodProcess=this.methodProcessMap.get(method.getType());
					try {
						retUri= methodProcess.methodProcess(method, requestParams, context,performer);
					} catch (Exception e) {
						//e.printStackTrace();
						logger.error(e);
						context.getRequest().setAttribute(Constants.REQUEST_JSON,JSON.toJSONString( LingxUtils.format(-99, e.getMessage())));
						retUri=Page.PAGE_JSON;
					}
				}else{
					context.getRequest().setAttribute(Constants.REQUEST_JSON,JSON.toJSONString( LingxUtils.format(-1, context.getMessages().values().toArray()[0])));
					return Page.PAGE_JSON;
				}
				break;
		}
		
		return retUri;
	}
	public void setMethodProcessMap(Map<String, IMethodProcess> methodProcessMap) {
		this.methodProcessMap = methodProcessMap;
	}
	public void setChooseService(IChooseService chooseService) {
		this.chooseService = chooseService;
	}
	public void setConverteService(IConverteService converteService) {
		this.converteService = converteService;
	}
	public void setValidateService(IValidateService validateService) {
		this.validateService = validateService;
	}
	public void setInterpretService(IInterpretService interpretService) {
		this.interpretService = interpretService;
	}
	public void setCascadeService(ICascadeService cascadeService) {
		this.cascadeService = cascadeService;
	}
	public void setScriptApisService(IScriptApisService scriptApisService) {
		this.scriptApisService = scriptApisService;
	}
	public void setPageService(IPageService pageService) {
		this.pageService = pageService;
	}
}
