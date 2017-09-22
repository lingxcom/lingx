package com.lingx.core.model;

import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.exception.LingxScriptException;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月5日 下午12:51:42 
 * 类说明 
 */
public interface IValidator extends IModel{
	public static final String TYPE_NO_NULL="no_null";
	/**
	 * 表达式验证器
	 */
	public static final String TYPE_EXPRESSION="expression";//expression
	/**
	 * 默认的提示消息
	 */
	public static final String DEFAULT_MESSAGE="{}参数无效";
	
	public boolean valid(String code,Object value,String param,IContext context,IPerformer performer)throws LingxScriptException;
	public String getParam();
	public String getMessage();
	public String getType();
	
}
