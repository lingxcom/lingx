package com.lingx.support.model.validator;

import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.exception.LingxScriptException;

public class StringMinSizeValidator extends AbstractValidator {

	private static final long serialVersionUID = 3513917010268801673L;

	@Override
	public boolean valid(String code,Object value, String param, 
			IContext context,IPerformer performer) throws LingxScriptException {
		boolean b=true;
		try {
			String temp=value.toString();
			b=(temp.length()>=Integer.parseInt(param));
		} catch (Exception e) {
			e.printStackTrace();
			b=false;
		}
		return b;
	}


}
