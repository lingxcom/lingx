package com.lingx.core.workflow.impl.method;

import java.util.HashMap;
import java.util.Map;

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
 * @version 创建时间：2015年10月13日 下午12:00:50 
 * 抄送
 */
@Component
public class CCWorkflow implements IWorkflowMethod {

	@Resource
	private IWorkflowService workflowService;
	@Resource
	private IPageService pageService;
	@Override
	public String getCode() {
		return "cc";
	}

	@Override
	public String execute(IWorkflow workflow, IContext context,
			IPerformer performer) {
		String msg=this.workflowService.cc(workflow.getTaskId(), context.getRequest().getParameter("user_ids"));
		Map<String,Object>map=new HashMap<String,Object>();
		map.put("code", 1);
		map.put("message", msg);
		return this.pageService.getJsonPage(map,context);
	}

	public void setWorkflowService(IWorkflowService workflowService) {
		this.workflowService = workflowService;
	}


}
