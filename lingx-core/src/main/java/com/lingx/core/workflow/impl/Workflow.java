package com.lingx.core.workflow.impl;

import com.lingx.core.workflow.IWorkflow;

/** 
 * @author www.lingx.me
 * @version 创建时间：2015年10月13日 上午10:52:42 
 * 类说明 
 */
public class Workflow implements IWorkflow {

	private String defineId;
	private String instanceId;
	private String taskId;
	private String currentTaskId;
	/*public Workflow(String dId,String iId,String tId){
		this.defineId=dId;
		this.instanceId=iId;
		this.taskId=tId;
	}*/
	public String getDefineId() {
		return defineId;
	}
	public void setDefineId(String defineId) {
		this.defineId = defineId;
	}
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getCurrentTaskId() {
		return currentTaskId;
	}
	public void setCurrentTaskId(String currentTaskId) {
		this.currentTaskId = currentTaskId;
	}
	
}
