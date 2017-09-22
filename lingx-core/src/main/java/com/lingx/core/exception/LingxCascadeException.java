package com.lingx.core.exception;

public class LingxCascadeException extends Exception{

	private static final long serialVersionUID = 844748453048671754L;
	public LingxCascadeException(String msg){
		super(msg);
	}
	public LingxCascadeException(String msg,Throwable e){
		super(msg,e);
	}
}
