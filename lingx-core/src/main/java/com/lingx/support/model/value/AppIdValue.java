package com.lingx.support.model.value;

import com.lingx.core.engine.IContext;
import com.lingx.core.model.IValue;

/** 
 * @author www.lingx.com
 * @version 创建时间：2016年2月22日 下午4:39:45 
 * 类说明 
 */
public class AppIdValue implements IValue {

	@Override
	public String getSourceValue() {
		return "${CUser.getApp().getId()}";
	}

	@Override
	public String getTargetValue(IContext context) {
		return String.valueOf(context.getUserBean().getApp().getId());
	}

}