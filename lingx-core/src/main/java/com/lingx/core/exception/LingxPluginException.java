package com.lingx.core.exception;

public class LingxPluginException extends Exception{

	private static final long serialVersionUID = -4805419737496163512L;
	public LingxPluginException(String msg){
		super(msg);
	}
	public LingxPluginException(String msg,Throwable e){
		super(msg,e);
	}
}
