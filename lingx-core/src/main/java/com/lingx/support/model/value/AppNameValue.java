package com.lingx.support.model.value;

import com.lingx.core.engine.IContext;
import com.lingx.core.model.IValue;

/** 
 * @author www.lingx.com
 * @version 创建时间：2016年2月22日 下午4:39:45 
 * 类说明 
 */
public class AppNameValue implements IValue {

	@Override
	public String getSourceValue() {
		return "${CUser.getApp().getName()}";
	}

	@Override
	public String getTargetValue(IContext context) {
		return String.valueOf(context.getUserBean().getApp().getName());
	}

}