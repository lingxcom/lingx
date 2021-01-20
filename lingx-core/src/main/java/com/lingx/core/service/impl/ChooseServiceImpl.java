package com.lingx.core.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.lingx.core.exception.LingxChooseException;
import com.lingx.core.model.IEntity;
import com.lingx.core.model.IMethod;
import com.lingx.core.service.IChooseService;
import com.lingx.core.service.IModelService;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月9日 下午9:36:08 
 * 类说明 
 */
@Component(value="lingxChooseService")
public class ChooseServiceImpl implements IChooseService {
	@Value("#{configs['lingx.get_method_defalut_field']}")
	private String get_method_defalut_field="import";//例外取模型属性，因为xml有可能配置属性
	@Resource
	private IModelService modelService;
	@Resource
	private Map<String,IMethod> defaultMethodSets;
	@Override
	public IEntity getEntity(String code) throws LingxChooseException {
		return this.modelService.getCacheEntity(code);
	}

	@Override
	public IMethod getMethod(String code, IEntity entity){
		IMethod method=this.modelService.getMethod(code, entity);
		if(method==null){
			method=this.defaultMethodSets.get(code);
			if(get_method_defalut_field.indexOf(code)==-1)//
			method.getFields().setList(entity.getFields().getList());
		}
		return method;
	}

	@Override
	public boolean defaultMethodContains(String code) {
		return this.defaultMethodSets.containsKey(code);
	}

	@Override
	public Map<String, IMethod> getDefaultMethods() {
		return this.defaultMethodSets;
	}

	public void setModelService(IModelService modelService) {
		this.modelService = modelService;
	}
	public void setDefaultMethodSets(Map<String,IMethod> defaultMethodSets) {
		this.defaultMethodSets = defaultMethodSets;
	}

	
}
