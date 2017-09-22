package com.lingx.core.engine;

import java.util.Map;

import com.lingx.core.exception.LingxProcessException;
import com.lingx.core.model.IMethod;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月5日 下午2:58:00 
 * 类说明 
 */
public interface IMethodProcess {

	public String methodProcess(IMethod method,Map<String,String> params,IContext context,IPerformer performer)throws LingxProcessException;
}
