package com.lingx.core.workflow;
/** 
 * @author www.lingx.com
 * @version 创建时间：2015年10月13日 上午10:42:09 
 * 类说明 
 */
public interface IWorkflow {
	public String getDefineId();
	public String getInstanceId() ;
	public String getTaskId();
	public String getCurrentTaskId();
	
	public void setDefineId(String defineId) ;
	public void setInstanceId(String instanceId) ;
	public void setTaskId(String taskId);
	public void setCurrentTaskId(String currentTaskId);
}
