package com.lingx.plugin.email.send;

public interface IEmailSendService {

	public boolean sendText(String email,String title,String text);
	public boolean sendHtml(String email,String title,String html);
	public boolean sendText(String email[],String title,String text);
	public boolean sendHtml(String email[],String title,String html);
	
	public void setHost(String host);
	public void setUsername(String username);
	public void setPassword(String password);
}
