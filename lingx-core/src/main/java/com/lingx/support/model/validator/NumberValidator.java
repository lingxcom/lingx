package com.lingx.support.model.validator;

import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.exception.LingxScriptException;

public class NumberValidator extends  RegexValidator {

	private static final long serialVersionUID = -5857934181435918071L;

	@Override
	public boolean valid(String code,Object value, String param, 
			IContext context,IPerformer performer) throws LingxScriptException {
		
		return super.valid(code,value, "[0-9]*", context,performer);
	}


}
