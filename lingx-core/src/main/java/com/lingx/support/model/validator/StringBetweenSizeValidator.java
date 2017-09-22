package com.lingx.support.model.validator;

import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.exception.LingxScriptException;

public class StringBetweenSizeValidator  extends AbstractValidator {

	private static final long serialVersionUID = -912483879342937548L;

	@Override
	public boolean valid(String code,Object value, String param, 
			IContext context,IPerformer performer) throws LingxScriptException {
		boolean b=true;
		try {
			String array[]=param.split(",");
			
			int min=Integer.parseInt(array[0]);
			int max=Integer.parseInt(array[1]);
			int val=value.toString().length();
			b=max>=val&&min<=val;
		} catch (Exception e) {
			b=false;
		}
		return b;
	}


}