package com.lingx.support.model.validator;

import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.exception.LingxScriptException;

public class UniqueValidator extends AbstractValidator {
	
	private static final long serialVersionUID = -8725588796516672989L;

	@Override
	public boolean valid(String code,Object value, String param, 
			IContext context,IPerformer performer) throws LingxScriptException {
		try {
			String temp="var c=JDBC.queryForInt('select count(*) from '+ENTITY_CODE+' where "+code+"=?'+(id?' and id<>\\\''+id+'\\\'':''),['"+value+"']); c==0;";
			//System.out.println(temp);
			Object obj=performer.script(temp);
			return Boolean.parseBoolean(obj.toString());
		} catch (Exception e) {
			return false;
		}
	}


}
