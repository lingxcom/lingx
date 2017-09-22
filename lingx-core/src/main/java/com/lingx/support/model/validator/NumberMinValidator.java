package com.lingx.support.model.validator;

import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.exception.LingxScriptException;

public class NumberMinValidator extends AbstractValidator {

	private static final long serialVersionUID = 8822034499434602849L;

	@Override
	public boolean valid(String code,Object value, String param, 
			IContext context,IPerformer performer) throws LingxScriptException {
		
		boolean b=true;
		try {
			int temp=Integer.parseInt(value.toString());
			b=temp>=Integer.parseInt(param);
		} catch (Exception e) {
			b=false;
		}
		return b;
	}

}