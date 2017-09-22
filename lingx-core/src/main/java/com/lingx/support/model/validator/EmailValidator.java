package com.lingx.support.model.validator;

import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.exception.LingxScriptException;

public class EmailValidator extends RegexValidator {

	private static final long serialVersionUID = 4975152421310308135L;
	public static final  String regexTemplate="^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
    
	@Override
	public boolean valid(String code,Object value, String param, 
			IContext context,IPerformer performer) throws LingxScriptException {
			return super.valid(code,value, regexTemplate, context, performer);
	}

}
