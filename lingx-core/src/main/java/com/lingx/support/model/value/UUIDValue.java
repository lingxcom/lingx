package com.lingx.support.model.value;

import java.util.UUID;

import com.lingx.core.engine.IContext;
import com.lingx.core.model.IValue;

/** 
 * @author www.lingx.com
 * @version 创建时间：2016年9月6日 下午3:07:41 
 * 类说明 
 */
public class UUIDValue implements IValue{
	
	@Override
	public String getSourceValue() {
		return "${UUID}";
	}

	@Override
	public String getTargetValue(IContext context) {
		
		return UUID.randomUUID().toString();
	}

}
