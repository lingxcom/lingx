package com.lingx.core.engine.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.lingx.core.Page;
import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IHttpRequest;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.engine.IProcessEngine;
import com.lingx.core.exception.LingxCascadeException;
import com.lingx.core.exception.LingxChooseException;
import com.lingx.core.exception.LingxConvertException;
import com.lingx.core.exception.LingxProcessException;
import com.lingx.core.exception.LingxValidatorException;
import com.lingx.core.service.IPageService;
import com.lingx.core.service.IScriptApisService;
import com.lingx.core.workflow.IWorkflow;
import com.lingx.core.workflow.IWorkflowMethod;
import com.lingx.core.workflow.impl.Workflow;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年10月13日 上午10:18:04 
 * 类说明 
 */
@Component(value="workflowProcessEngine")
public class WorkflowProcessEngine implements IProcessEngine {
	@Resource
	private List<IWorkflowMethod> methods;
	@Resource
	private IScriptApisService scriptApisService;
	@Resource
	private IPageService pageService;
	@Resource
	private JdbcTemplate jdbcTemplate;
	@Override
	public String process(IContext context) throws LingxProcessException,LingxChooseException, LingxConvertException,LingxValidatorException, LingxCascadeException {
		String retUri;
		IHttpRequest request=context.getRequest();
		String d=request.getParameter("d");//defineCode
		String m=request.getParameter("m");//操作代码，方法
		IWorkflow workflow=getWorkflow(context.getRequest());
		if(workflow.getDefineId()==null&&d!=null){workflow.setDefineId(this.jdbcTemplate.queryForObject("select id from tlingx_wf_define where code=?", String.class,d));}
		if(m==null&&workflow.getInstanceId()==null){
			m="start";
		}else if(m==null&&workflow.getInstanceId()!=null){
			m="form";
		}
		IWorkflowMethod method=this.getMethod(m);
		IPerformer performer =new DefaultPerformer(scriptApisService.getScriptApis(),context.getRequest());
		retUri=method.execute(workflow, context, performer);
		return retUri;
	}

	private IWorkflow getWorkflow(IHttpRequest request){
		IWorkflow workflow=new Workflow();
		workflow.setDefineId(request.getParameter("_DEFINE_ID"));
		workflow.setInstanceId(request.getParameter("_INSTANCE_ID"));
		workflow.setTaskId(request.getParameter("_TASK_ID")); 
		workflow.setCurrentTaskId(request.getParameter("_CURRENT_TASK_ID"));
		return workflow;
	}
	private IWorkflowMethod getMethod(String code){
		IWorkflowMethod temp=null;
		for(IWorkflowMethod t:this.methods){
			if(t.getCode().equals(code)){
				temp=t;
				break;
			}
		}
		return temp;
	}

	public void setMethods(List<IWorkflowMethod> methods) {
		this.methods = methods;
	}

	public void setScriptApisService(IScriptApisService scriptApisService) {
		this.scriptApisService = scriptApisService;
	}

	public void setPageService(IPageService pageService) {
		this.pageService = pageService;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}
