package com.lingx.support.model.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.exception.LingxScriptException;

public class RegexValidator extends AbstractValidator {

	private static final long serialVersionUID = 2094588328378342183L;

	@Override
	public boolean valid(String code,Object value, String param, 
			IContext context,IPerformer performer) throws LingxScriptException {
		
		boolean b=true;
	    try {
			Pattern p = Pattern.compile(param);     
			Matcher m = p.matcher(value.toString());  
			b= m.matches();
		} catch (Exception e) {
			b=false;
		}
        return b;   
	}

}
