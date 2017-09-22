package com.lingx.support.model.value;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.lingx.core.engine.IContext;
import com.lingx.core.model.IValue;

/** 
 * @author www.lingx.me
 * @version 创建时间：2015年10月6日 下午6:52:47 
 * 类说明 
 */
public class DateTimeValue implements IValue {
	public static final SimpleDateFormat sdfDateTime=new SimpleDateFormat("yyyyMMddHHmmss");

	@Override
	public String getSourceValue() {
		return "${CDateTime}";
	}

	@Override
	public String getTargetValue(IContext context) {
		return sdfDateTime.format(new Date());
	}

}
