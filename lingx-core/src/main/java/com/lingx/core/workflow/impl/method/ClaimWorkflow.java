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
 * @author www.lingx.com
 * @version 创建时间：2015年10月13日 下午5:40:54 
 * 类说明 
 */
@Component
public class ClaimWorkflow implements IWorkflowMethod {

	@Resource
	private IWorkflowService workflowService;
	@Resource
	private IPageService pageService;
	@Override
	public String getCode() {
		return "claim";
	}

	@Override
	public String execute(IWorkflow workflow, IContext context,
			IPerformer performer) {
		Map<String,Object> ret=this.workflowService.claimTask(workflow.getTaskId(),workflow, context, performer);
		return this.pageService.getJsonPage(ret, context);
	}

	public void setWorkflowService(IWorkflowService workflowService) {
		this.workflowService = workflowService;
	}

}
