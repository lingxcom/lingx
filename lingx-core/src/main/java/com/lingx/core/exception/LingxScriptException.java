package com.lingx.core.exception;

public class LingxScriptException  extends Exception{

	private static final long serialVersionUID = -7102544222182707988L;

	public LingxScriptException(String msg){
		super(msg);
	}
	public LingxScriptException(String msg,Throwable e){
		super(msg,e);
	}
	
}
