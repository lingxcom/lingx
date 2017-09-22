package com.lingx.support.model.interpreter;

import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.exception.LingxScriptException;

public class DateTime14Interpreter extends AbstractInterpreter {

	private static final long serialVersionUID = -2479086106471842107L;


	@Override
	public Object input(Object value, IContext context, IPerformer jsper)
			throws LingxScriptException {
		if(value!=null){
			String temp=value.toString();
			 temp=temp.replaceAll("[-]|[ ]|[:]", "");
			return temp;
		}else{
			return "";
		}
		
	}


	@Override
	public Object output(Object value, IContext context, IPerformer jsper)
			throws LingxScriptException {
		String temp="";
		StringBuilder sb=new StringBuilder();
		if(value!=null&&value.toString().length()==14){
			temp=value.toString();
			sb.append(temp.substring(0, 4)).append("-").append(temp.substring(4, 6)).append("-").append(temp.substring(6, 8)).append(" ")
			.append(temp.substring(8, 10)).append(":").append(temp.substring(10, 12)).append(":").append(temp.substring(12, 14));
		}else{
			return value;
		}
		return sb.toString();
	}

}
