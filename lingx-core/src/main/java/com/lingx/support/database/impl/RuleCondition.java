package com.lingx.support.database.impl;

import javax.annotation.Resource;

import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.service.IModelService;
import com.lingx.support.database.ICondition;

public class RuleCondition implements ICondition {
	@Resource
	private IModelService modelService;

	@Override
	public String getCondition( IContext context,IPerformer performer ){
		String ruleSql=this.modelService.getRuleDataAuth(context.getEntity(), context.getUserBean());
		return ruleSql;
	}

	public void setModelService(IModelService modelService) {
		this.modelService = modelService;
	}

}
