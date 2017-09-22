package com.lingx.core.service;

import java.util.Map;

import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年6月30日 下午2:24:30 
 * 定时调度
 */
public interface ITaskService {
	/**
	 * 创建任务
	 * @return
	 */
	public String[] create(String defineTaskId,String instanceTaskId,IContext context,IPerformer performer);
	/**
	 * 创建任务
	 * @return
	 */
	public String[] create(String defineTaskIds[],String instanceTaskId,IContext context,IPerformer performer);
	/**
	 * 签收
	 * @return
	 */
	public String claim(String instanceTaskId,String userId,IContext context,IPerformer performer);
	/**
	 * 保存
	 * @return
	 */
	public String save(String instanceTaskId,IContext context,IPerformer performer);
	/**
	 * 执行
	 * @return
	 */
	public String execute(String instanceTaskId,IContext context,IPerformer performer);
	/**
	 * 回退
	 * @return
	 */
	public String fallback(String instanceTaskId,IContext context,IPerformer performer);
	/**
	 * 提交
	 * @return true 代表进入下一任务，false该任务还未完成，需等待
	 */
	public boolean submit(String instanceTaskId,IContext context,IPerformer performer);
	/**
	 * 审核
	 * @return
	 */
	public String approve(String instanceTaskId,String content,String ret,IContext context,IPerformer performer);
	/**
	 * 获得开始任务的定义ID
	 * @param defineId
	 * @return
	 */
	public String getStartTaskDefineId(String defineId,IContext context,IPerformer performer);
	/**
	 * 获取签收人
	 * @return
	 */
	public String[] getCailmUserIds(String defineTaskId,String instanceId,IContext context,IPerformer performer);
	/**
	 * 获取下一节点的任务ID
	 * @param instanceTaskId
	 * @return
	 */
	public String[] nextTaskDefineIds(String instanceTaskId,IContext context,IPerformer performer);
	/**
	 * 获取上一节点的任务ID
	 * @param instanceTaskId
	 * @return
	 */
	public String[] prevTaskDefineIds(String instanceTaskId,IContext context,IPerformer performer);
	/**
	 * 脚本执行
	 * @param script
	 * @param instanceTaskId
	 * @param performer
	 * @return
	 */
	public Object script(String script,String instanceTaskId,IContext context,IPerformer performer);
	public Object scriptByInstanceId(String script,String instanceId,IContext context,IPerformer performer);
	public void addParam(String paramName,String paramValue,String instanceId);
	/**
	 * 复制任务实例，传入要被复制的实例ID，返回新ID
	 * @param taskId
	 * @return
	 */
	public String copyTaskInstance(String taskId);
	
	public void log(String instance, String task,String comment,IContext context);
}
