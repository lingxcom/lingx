package com.lingx.core.exception;

public class LingxChooseException extends Exception{

	private static final long serialVersionUID = 5554451917299145696L;

	public LingxChooseException(String msg){
		super(msg);
	}
	public LingxChooseException(String msg,Throwable e){
		super(msg,e);
	}
}
