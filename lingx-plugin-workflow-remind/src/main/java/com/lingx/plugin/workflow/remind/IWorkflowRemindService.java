package com.lingx.plugin.workflow.remind;

public interface IWorkflowRemindService {
	public void setTitle(String title);

	public void setBody(String body);

	public void setEnable(boolean enable);
	public String[] getEmailByUserId(String userid);
}
