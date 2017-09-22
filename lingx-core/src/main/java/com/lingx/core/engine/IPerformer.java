package com.lingx.core.engine;

import java.util.Map;

import com.lingx.core.exception.LingxScriptException;
import com.lingx.core.model.IScript;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月5日 下午1:32:39 
 * 类说明 
 */
public interface IPerformer {
	/**
	 * 执行脚本
	 * @param script
	 * @param context
	 * @return
	 * @throws LingxScriptException
	 */
	public Object script(IScript script,IContext context) throws LingxScriptException;
	public Object script(String script,IContext context) throws LingxScriptException;
	public Object script(String script) throws LingxScriptException;
	public void addParam(String key, Object val);
	public void addParam(Map<String,Object> map);
	public void addParam(IContext context);
	public void addRequestParam(Map<String,String> map);
}
