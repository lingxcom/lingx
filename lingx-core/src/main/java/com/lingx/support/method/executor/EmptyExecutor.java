package com.lingx.support.method.executor;

import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.exception.LingxScriptException;
import com.lingx.core.model.IExecutor;
import com.lingx.core.model.IScript;
import com.lingx.core.model.impl.AbstractModel;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年6月16日 上午9:28:02 
 * 类说明 
 */
public class EmptyExecutor extends AbstractModel implements IExecutor{

	private static final long serialVersionUID = -5086265529224571388L;

	@Override
	public Object execute(IContext context, IPerformer performer)
			throws LingxScriptException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IScript getScript() {
		// TODO Auto-generated method stub
		return null;
	}

}
