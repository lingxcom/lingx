package com.lingx.core.model;

import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.exception.LingxScriptException;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月5日 下午12:51:21 
 * 类说明    解释器
 */
public interface IInterpreter extends IModel{

	public static final String TYPE_EXPRESSION="expression";//expression
	public String getType();
	/**
	 * 输入格式化
	 * @param value
	 * @param jsper
	 * @param context
	 * @return
	 * @throws LingxScriptException
	 */
	public Object input(Object value,IContext context,IPerformer jsper)throws LingxScriptException;
	/**
	 * 输出格式化
	 * @param value
	 * @param jsper
	 * @param context
	 * @return
	 * @throws LingxScriptException
	 */
	public Object output(Object value,IContext context,IPerformer jsper)throws LingxScriptException;
	
}
