package com.lingx.support.model.validator;

import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.exception.LingxScriptException;

public class NotNullValidator extends AbstractValidator {

	private static final long serialVersionUID = 5104545985585423221L;

	@Override
	public boolean valid(String code,Object value, String param, 
			IContext context,IPerformer performer) throws LingxScriptException {
		
		return value!=null&&!"".equals(value.toString());
	}



}
