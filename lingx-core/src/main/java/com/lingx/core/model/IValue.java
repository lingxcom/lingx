package com.lingx.core.model;

import com.lingx.core.engine.IContext;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年10月6日 下午6:38:09 
 * 类说明 
 */
public interface IValue {
	public String getSourceValue();
	public String getTargetValue(IContext context);
}
