package com.lingx.support.model.interpreter;

import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.exception.LingxScriptException;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年9月20日 上午4:00:42 
 * 类说明 
 */
public class Time4Interpreter extends AbstractInterpreter{

	private static final long serialVersionUID = 3727950655388868120L;

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
		String temp=null;
		StringBuilder sb=new StringBuilder();
		if(value!=null){
			temp=value.toString();
			sb.append(temp.substring(0,2)).append(":").append(temp.substring(2));
		}else{
			return value;
		}
		return sb.toString();
	}

}
