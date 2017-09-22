package com.lingx.core.exception;

public class LingxProcessException extends Exception {

	private static final long serialVersionUID = -7102544667182707988L;

	public LingxProcessException(String msg){
		super(msg);
	}
	public LingxProcessException(String msg,Throwable e){
		super(msg,e);
	}
}
