package com.lingx.core.workflow;

import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年10月13日 上午10:39:15 
 * 类说明 
 */
public interface IWorkflowMethod {
	
	public String getCode();
	/**
	 * 返回提示界面或操作界面
	 * @param workflow
	 * @param context
	 * @param performer
	 * @return
	 */
	public String execute(IWorkflow workflow,IContext context,IPerformer performer);
}
