package com.lingx.core.workflow.impl.method;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lingx.core.Constants;
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
public class GetWorkflow implements IWorkflowMethod {

	@Resource
	private IWorkflowService workflowService;
	@Resource
	private IPageService pageService;
	@Override
	public String getCode() {
		return "get";
	}

	@Override
	public String execute(IWorkflow workflow, IContext context,
			IPerformer performer) {
		context.getRequest().setAttribute(Constants.REQUEST_JSON, JSON.toJSONString(this.workflowService.getInstanceAll(workflow.getInstanceId(),workflow, context, performer)));
		return this.pageService.getJsonPage();
	}

	public void setWorkflowService(IWorkflowService workflowService) {
		this.workflowService = workflowService;
	}

	public void setPageService(IPageService pageService) {
		this.pageService = pageService;
	}

}
