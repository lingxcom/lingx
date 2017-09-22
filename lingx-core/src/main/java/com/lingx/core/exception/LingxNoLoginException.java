package com.lingx.core.exception;

public class LingxNoLoginException extends Exception {

	private static final long serialVersionUID = 4024446900049242387L;

	public LingxNoLoginException(String msg,Throwable e){
		super(msg,e);
	}
}
