package com.lingx.support.database.impl;

import javax.annotation.Resource;

import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.exception.LingxScriptException;
import com.lingx.core.model.IEntity;
import com.lingx.core.service.IScriptApisService;
import com.lingx.core.utils.Utils;
import com.lingx.support.database.ICondition;

public class ExtendCondition implements ICondition {
	@Resource
	private IScriptApisService scriptApisService;
	@Override
	public String getCondition(IContext context,IPerformer performer ) {
		//IPerformer performer =new DefaultPerformer(scriptApisService.getScriptApis(),context.getRequest());
		IEntity entity=context.getEntity();
		StringBuilder sb=new StringBuilder();
		/*while(entity.getType()==2){//子集对象条件累积
			if(Utils.isNotNull(entity.getScript())){
				String sql=String.valueOf(this.scriptPerformer.script(entity.getScript()));
				sb.append(" ").append(sql).append(" ");
			}
			entity=EntityCache.getEntity(entity.getFcode());
		}*/
		//主体的条件也生效
		try {
			if(Utils.isNotNull(entity.getScript())){
				String sql=String.valueOf(performer.script(entity.getScript()));
				sb.append(" ").append(sql).append(" ");
			}
		} catch (LingxScriptException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	public void setScriptApisService(IScriptApisService scriptApisService) {
		this.scriptApisService = scriptApisService;
	}
}
