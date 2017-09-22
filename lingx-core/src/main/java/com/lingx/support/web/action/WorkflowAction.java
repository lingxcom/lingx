package com.lingx.support.web.action;

import javax.annotation.Resource;

import com.lingx.core.Page;
import com.lingx.core.action.IAction;
import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IProcessEngine;
import com.lingx.core.service.IPageService;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年9月4日 上午9:59:03 
 * 类说明 
 */
public class WorkflowAction  implements IAction {
	@Resource(name="workflowProcessEngine")
	private IProcessEngine processEngine;
	@Resource
	private IPageService pageService;
	public String action(IContext context) {
		String page="";
		try {
			page= this.processEngine.process(context);
		} catch (Exception e) {
			e.printStackTrace();
			context.getRequest().setAttribute("e", e);
			page=this.pageService.getPage(Page.PAGE_EXCEPTION);
		}
		return page;
	}
	public void setProcessEngine(IProcessEngine processEngine) {
		this.processEngine = processEngine;
	}
	public void setPageService(IPageService pageService) {
		this.pageService = pageService;
	}

}
