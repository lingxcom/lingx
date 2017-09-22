package com.lingx.core.service;

import java.util.List;
import java.util.Map;

import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.workflow.IWorkflow;

/** 
 * @author www.lingx.me
 * @version 创建时间：2015年9月4日 上午10:32:25 
 * 类说明 
 */
public interface IWorkflowService {

	
	public String page(String page);
	
	public void updateLinePoint(String taskId,int targetTop,int targetLeft);
	public void updateLinePoint(String defineId,int sourceTop,int sourceLeft,int targetTop,int targetLeft);
	
	public String round(String temp);
	public int round(int temp);
	/**
	 * 创建流程实例，并返回界面
	 * @param defineId
	 * @param userId
	 * @param context
	 * @param performer
	 * @return 界面
	 */
	public String createInstance(IWorkflow workflow,IContext context,IPerformer performer);
	
	/**
	 * 签收任务
	 * @param taskId
	 * @param workflow
	 * @param context
	 * @param performer
	 * @return
	 */
	public Map<String,Object> claimTask(String taskId,IWorkflow workflow, IContext context, IPerformer performer);
	/**
	 * 填写表单
	 * @param taskId
	 * @param context
	 * @param performer
	 * @return
	 */
	public String formTask(String taskId,IWorkflow workflow, IContext context, IPerformer performer);
	/**
	 * 保存任务
	 * @param taskId
	 * @param workflow
	 * @param context
	 * @param performer
	 * @return
	 */
	public String saveTask(String taskId,IWorkflow workflow, IContext context, IPerformer performer);
	/**
	 * 提交任务
	 * @param taskId
	 * @return
	 */
	public Map<String,Object> submitTask(String taskId,IContext context,IPerformer performer);
	/**
	 * 任务回退
	 * @param taskId
	 * @param workflow
	 * @param context
	 * @param performer
	 * @return
	 */
	public Map<String,Object> fallback(String taskId,IWorkflow workflow,IContext context,IPerformer performer);
	/**
	 * 任务委托
	 * @param taskId
	 * @param workflow
	 * @param context
	 * @param performer
	 * @return
	 */
	public Map<String,Object> delegate(String taskId,IWorkflow workflow,IContext context,IPerformer performer);
	/**
	 * 任务审批
	 * @param taskId
	 * @param workflow
	 * @param context
	 * @param performer
	 * @return
	 */
	public Map<String,Object> approve(String taskId,IWorkflow workflow,IContext context,IPerformer performer);
	/**
	 * 转发任务
	 * @param taskId
	 * @param workflow
	 * @param context
	 * @param performer
	 * @return
	 */
	public Map<String,Object> forward(String taskId,IWorkflow workflow,IContext context,IPerformer performer);
	/**
	 * 删除流程
	 * @param taskId
	 * @param workflow
	 * @param context
	 * @param performer
	 * @return
	 */
	public Map<String,Object> delete(String taskId,IWorkflow workflow,IContext context,IPerformer performer);
	/**
	 * 表单数据验证
	 * @param taskId
	 * @param workflow
	 * @param context
	 * @param performer
	 * @return
	 */
	public Map<String,Object> isvalid(String taskId,IWorkflow workflow,IContext context,IPerformer performer);
	/**
	 * 任务评论
	 * @param taskId
	 * @param workflow
	 * @param context
	 * @param performer
	 * @return
	 */
	public Map<String,Object> comment(String taskId,IWorkflow workflow,IContext context,IPerformer performer);
	/**
	 * 任务查看
	 * @param taskId
	 * @param workflow
	 * @param context
	 * @param performer
	 * @return
	 */
	public String view(String taskId,IWorkflow workflow,IContext context,IPerformer performer);
	/**
	 * 1全部通过
	 * 2全部不通过
	 * 3都有
	 * @param taskId
	 * @return
	 */
	public int sign(Object taskId);
	/**
	 * 抄送
	 * @param taskId
	 * @param userIds
	 * @return
	 */
	public String cc(Object taskId,String userIds);
	public void lineTest(Object id);
	
	public Map<String,Object> getInstanceAll(String instance,IWorkflow workflow,IContext context,IPerformer performer);
	/**
	 * 快速创建
	 * @param defineId
	 * @param params
	 * @param context
	 * @param performer
	 * @return
	 */
	public Map<String,Object> quickCreate(String defineId,Map<String,Object> params,IContext context,IPerformer performer);
	/**
	 * 快速提交
	 * @param taskId
	 * @param context
	 * @param performer
	 * @return
	 */
	public Map<String, Object> quickSubmit(String taskId,IContext context,IPerformer performer);
	/**
	 * 抄送
	 * @param taskId
	 * @param userIds
	 * @return
	 */
	public Map<String,Object> quickCC(String taskId,String userIds);
	/**
	 * 结束
	 * @param taskId
	 * @return
	 */
	public Map<String,Object> quickEnd(String taskId);
	/**
	 * 设置审核人
	 * @param taskId
	 * @param userId
	 * @return
	 */
	public boolean quickSetApprover(String taskId,String userId,IContext context);
	/**
	 * 快速审批
	 * @param taskId
	 * @param params
	 * @param context
	 * @param performer
	 * @return
	 */
	public Map<String,Object> quickApprove(String taskId,Map<String,String> params,IContext context,IPerformer performer);
	/**
	 * 获取流程所有参数
	 * @param taskId
	 * @return
	 */
	public Map<String,Object> quickGetParams(String taskId);
	/**
	 * 获取流程处理记录
	 * @param taskId
	 * @return
	 */
	public List<Map<String,Object>> quickGetHistory(String taskId);
	/**
	 * 获取流程所有附件
	 * @param taskId
	 * @return
	 */
	public List<Map<String,Object>> quickGetAttachment(String taskId);
	/**
	 * 快速设置流程评论
	 * @param taskId
	 * @param comment
	 * @return
	 */
	public boolean quickSaveComment(String taskId,String comment,String userId);
	/**
	 * 导出流程定义
	 * @param id 流程定义ID
	 * @param isForm 是否包括表单
	 * @return JSON数据
	 */
	
	public String output(String id,boolean isForm);
	/**
	 * 导入流程定义
	 * @param code 代码
	 * @param path 文件路径
	 * @param basePath 运行目录
	 * @param type 类型
	 * @param appid 隶属应用
	 * @return
	 */
	public boolean input(String code,String path,String basePath,String type,String appid);
	/**
	 * 将参数写入流程实例
	 * @param instanceId
	 * @param paramName
	 * @param paramValue
	 * @return
	 */
	public boolean push(String instanceId,String paramName,String paramValue);
}
