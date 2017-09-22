package com.lingx.support.model.interpreter;

import com.lingx.core.engine.IPerformer;
import com.lingx.core.exception.LingxScriptException;

public class DateTimeToDateInterpreter extends AbstractInterpreter {

	private static final long serialVersionUID = 457313855805051399L;

	@Override
	public Object input(Object value, com.lingx.core.engine.IContext context,
			IPerformer jsper) throws LingxScriptException {
		if(value!=null){
			String temp=value.toString();
			 temp=temp.replaceAll("[-]|[ ]|[:]", "");
			return temp;
		}else{
			return "";
		}
	}

	@Override
	public Object output(Object value, com.lingx.core.engine.IContext context,
			IPerformer jsper) throws LingxScriptException {
		String temp="";
		StringBuilder sb=new StringBuilder();
		if(value!=null&&value.toString().length()>=8){
			temp=value.toString();
			sb.append(temp.substring(0, 4)).append("-").append(temp.substring(4, 6)).append("-").append(temp.substring(6, 8));
		}else{
			return value;
		}
		return sb.toString();
	}
	
	public static void main(String args[]){
		String d="2015-09-20";
		DateTimeToDateInterpreter obj=new DateTimeToDateInterpreter();
		try {
			System.out.println(obj.input(d, null, null));
		} catch (LingxScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
