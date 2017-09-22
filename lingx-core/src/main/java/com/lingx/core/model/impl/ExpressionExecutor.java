package com.lingx.core.model.impl;

import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.exception.LingxScriptException;
import com.lingx.core.model.IExecutor;
import com.lingx.core.model.IScript;
import com.lingx.core.model.annotation.TreeNode;

public class ExpressionExecutor extends AbstractModel implements IExecutor {
	
	private static final long serialVersionUID = -4485152402747428526L;
	
	@TreeNode
	private IScript script;
	public ExpressionExecutor(){
		super();
		script=new JavaScript();
		this.setModelType("Executor");
		this.setName("表达式执行器");
		this.setIconCls("icon-5");
	}
	public void setScript(IScript script) {
		// TODO Auto-generated method stub
		this.script=script;
	}

	public IScript getScript() {
		// TODO Auto-generated method stub
		return this.script;
	}

	public Object execute(IContext context,IPerformer jsper) throws LingxScriptException {
		// TODO Auto-generated method stub
		Object obj=jsper.script(script,context);
		return obj;
	}
}
