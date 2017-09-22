package com.lingx.support.model.interpreter;

import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.exception.LingxScriptException;
import com.lingx.core.utils.DateUtils;

public class DT14BIInterpreter extends AbstractInterpreter {

	private static final long serialVersionUID = -2479086106471842107L;


	@Override
	public Object input(Object value, IContext context, IPerformer jsper)
			throws LingxScriptException {
		return value;
		
	}


	@Override
	public Object output(Object value, IContext context, IPerformer jsper)
			throws LingxScriptException {
		
		return DateUtils.formatBi(value.toString());
	}

}
