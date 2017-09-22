package com.lingx.core.model;

import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.exception.LingxScriptException;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月5日 下午12:50:57 
 * 类说明 
 */
public interface IExecutor extends IModel{

	public Object execute(IContext context,IPerformer performer) throws LingxScriptException;
	public IScript getScript();
	
}
