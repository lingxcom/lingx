package com.lingx.support.method.executor;

import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.exception.LingxScriptException;
import com.lingx.core.model.IExecutor;
import com.lingx.core.model.IScript;
import com.lingx.core.model.impl.AbstractModel;

public class ViewExecutor extends AbstractModel implements IExecutor {
	
	private static final long serialVersionUID = 2658795210422869808L;
	
	public ViewExecutor(){
		super();
		this.setName("GridExecutor");
	}
	@Override
	public Object execute( IContext context,IPerformer performer)
			throws LingxScriptException {
		return null;
	}
	
	@Override
	public IScript getScript() {
		return null;
	}

}
