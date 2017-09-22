package com.lingx.support.database;

import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;

public interface ICondition{
	public String getCondition(IContext context,IPerformer performer );
}
