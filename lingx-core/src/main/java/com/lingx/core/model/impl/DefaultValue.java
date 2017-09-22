package com.lingx.core.model.impl;

import com.lingx.core.engine.IContext;
import com.lingx.core.model.IValue;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年10月6日 下午6:46:42 
 * 类说明 
 */
public class DefaultValue implements IValue {
	private String sourceValue;
	private String targetValue;
	public String getSourceValue() {
		return sourceValue;
	}
	public void setSourceValue(String sourceValue) {
		this.sourceValue = sourceValue;
	}
	public String getTargetValue(IContext context) {
		return targetValue;
	}
	public void setTargetValue(String targetValue) {
		this.targetValue = targetValue;
	}

}
