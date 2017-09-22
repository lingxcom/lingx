package com.lingx.core.workflow.impl.method;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.service.IPageService;
import com.lingx.core.service.IWorkflowService;
import com.lingx.core.workflow.IWorkflow;
import com.lingx.core.workflow.IWorkflowMethod;

/** 
 * @author www.lingx.me
 * @version 创建时间：2015年11月2日 上午9:08:40 
 * 类说明 
 */
@Component
public class FallbackWorkflow implements IWorkflowMethod{
	@Resource
	private IWorkflowService workflowService;
	@Resource
	private IPageService pageService;
	@Override
	public String getCode() {
		return "fallback";
	}

	@Override
	public String execute(IWorkflow workflow, IContext context,
			IPerformer performer) {
		Map<String,Object> ret=this.workflowService.fallback(workflow.getTaskId(), workflow, context, performer);
		return this.pageService.getJsonPage(ret, context);
	}

	public void setWorkflowService(IWorkflowService workflowService) {
		this.workflowService = workflowService;
	}

}
