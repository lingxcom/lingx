package com.lingx.core.workflow.impl.method;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.service.IWorkflowService;
import com.lingx.core.workflow.IWorkflow;
import com.lingx.core.workflow.IWorkflowMethod;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年11月4日 下午2:55:40 
 * 类说明 
 */
@Component
public class ViewWorkflow implements IWorkflowMethod{

	@Resource
	private IWorkflowService workflowService;
	@Override
	public String getCode() {
		return "view";
	}

	@Override
	public String execute(IWorkflow workflow, IContext context,
			IPerformer performer) {
		return this.workflowService.view(workflow.getTaskId(), workflow, context, performer);
	}

	public void setWorkflowService(IWorkflowService workflowService) {
		this.workflowService = workflowService;
	}

}
