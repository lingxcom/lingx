package com.lingx.core.workflow.event;

import org.springframework.context.ApplicationEvent;

import com.lingx.core.engine.IContext;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年12月21日 下午3:43:56 
 * 类说明 
 */
public class ClaimEvent extends ApplicationEvent {

	private static final long serialVersionUID = -6249501132243295014L;
	
	private String userId;
	private String taskId;
	private String title;
	public String getTitle() {
		return title;
	}
	private IContext context;
	public ClaimEvent(String userId,String taskId,String title,IContext context,Object source) {
		super(source);
		this.userId=userId;
		this.taskId=taskId;
		this.title=title;
		this.context=context;
	}
	public String getUserId() {
		return userId;
	}
	public String getTaskId() {
		return taskId;
	}
	public IContext getContext() {
		return context;
	}
}
