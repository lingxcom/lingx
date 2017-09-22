package com.lingx.support.model.value;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.lingx.core.engine.IContext;
import com.lingx.core.model.IValue;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年10月6日 下午6:52:47 
 * 类说明 
 */
public class YearValue implements IValue {
	public static final SimpleDateFormat sdfDate=new SimpleDateFormat("yyyy");

	@Override
	public String getSourceValue() {
		return "${CYear}";
	}

	@Override
	public String getTargetValue(IContext context) {
		return sdfDate.format(new Date());
	}

}
