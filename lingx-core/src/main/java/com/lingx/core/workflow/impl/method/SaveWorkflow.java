package com.lingx.core.workflow.impl.method;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.lingx.core.Constants;
import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.service.IPageService;
import com.lingx.core.service.IWorkflowService;
import com.lingx.core.workflow.IWorkflow;
import com.lingx.core.workflow.IWorkflowMethod;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年10月14日 下午10:24:37 
 * 类说明 
 */
@Component
public class SaveWorkflow implements IWorkflowMethod {

	@Resource
	private IWorkflowService workflowService;
	@Resource
	private IPageService pageService;
	@Override
	public String getCode() {
		return "save";
	}

	@Override
	public String execute(IWorkflow workflow, IContext context,
			IPerformer performer) {
		// TODO Auto-generated method stub
		this.workflowService.saveTask(workflow.getTaskId(), workflow, context, performer);
		return this.pageService.getJsonPage(1,"保存成功",context);
	}

	public void setWorkflowService(IWorkflowService workflowService) {
		this.workflowService = workflowService;
	}

}
