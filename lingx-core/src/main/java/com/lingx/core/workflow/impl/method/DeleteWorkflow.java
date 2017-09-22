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
 * @version 创建时间：2017年5月23日 下午3:33:03 
 * 类说明 
 */
@Component
public class DeleteWorkflow implements IWorkflowMethod {

	@Resource
	private IWorkflowService workflowService;
	@Resource
	private IPageService pageService;
	@Override
	public String getCode() {
		return "delete";
	}
	@Override
	public String execute(IWorkflow workflow, IContext context,
			IPerformer performer) {
		Map<String,Object> ret=this.workflowService.delete(workflow.getTaskId(), workflow, context, performer);
		return this.pageService.getJsonPage(ret, context);
	}

}
