package com.lingx.support.database.impl;

import javax.annotation.Resource;

import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.model.IEntity;
import com.lingx.core.service.IModelService;
import com.lingx.support.database.ICondition;

public class CascadeRuleCondition implements ICondition {
	@Resource
	private IModelService modelService;
	@Override
	public String getCondition(IContext context,IPerformer performer ){//sb.append(EntityUtils.getValueField(context.getEntity())).append(" in (select )");
		StringBuilder sb=new StringBuilder();
		String ecode=context.getEntity().getCode();
		String refEntity=context.getRequest().getParameter("_refEntity_");
		String refId=context.getRequest().getParameter("_refId_");
		String rule=context.getRequest().getParameter("rule");
		if(refEntity==null||"".equals(refEntity)||"_none_".equals(rule))return "";
		
		IEntity entity=modelService.getCacheEntity(rule);
		String valueFieldCode=modelService.getFieldByEntity(entity, ecode).getCode();
		String tiaojianFieldCode=modelService.getFieldByEntity(entity, refEntity).getCode();
		sb.append(" and ").append(modelService.getValueField(context.getEntity())).append(" in (");
		sb.append(this.getCascadeRule_1(valueFieldCode, entity.getTableName(), tiaojianFieldCode, refId));
		sb.append(") ");
		return sb.toString();
	}
	/**
	 * 
	 * @param valueField 值
	 * @param tableName 表
	 * @param tianjianField 条件字段
	 * @param tianjian 条件
	 * @return
	 */
	private String getCascadeRule_1(String valueField,String tableName,String tiaojianField,String tiaojian){
		StringBuilder sb=new StringBuilder();
		sb.append("select ").append(valueField).append(" from ").append(tableName).append(" where ").append(tiaojianField).append(" in ('").append(tiaojian).append("')");
		return sb.toString();
	}
	public void setModelService(IModelService modelService) {
		this.modelService = modelService;
	}
	
}
