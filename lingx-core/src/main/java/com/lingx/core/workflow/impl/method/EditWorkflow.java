package com.lingx.core.workflow.impl.method;

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
 * @version 创建时间：2017年5月9日 上午10:42:24 
 * 类说明 
 */
@Component
public class EditWorkflow implements IWorkflowMethod{

	@Resource
	private IWorkflowService workflowService;
	@Resource
	private IPageService pageService;
	
	@Override
	public String getCode() {
		return "edit";
	}

	@Override
	public String execute(IWorkflow workflow, IContext context,
			IPerformer performer) {
		 this.workflowService.formTask(workflow.getTaskId(),workflow, context, performer);
		return "lingx/workflow/page/form2-approval-edit.jsp";
	}

}
