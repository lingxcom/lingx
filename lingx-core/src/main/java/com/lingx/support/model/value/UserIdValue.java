package com.lingx.support.model.value;

import com.lingx.core.engine.IContext;
import com.lingx.core.model.IValue;

/** 
 * @author www.lingx.me
 * @version 创建时间：2015年10月6日 下午6:52:47 
 * 类说明 
 */
public class UserIdValue implements IValue {

	@Override
	public String getSourceValue() {
		return "${CUser.getId()}";
	}

	@Override
	public String getTargetValue(IContext context) {
		return String.valueOf(context.getUserBean().getId());
	}

}
