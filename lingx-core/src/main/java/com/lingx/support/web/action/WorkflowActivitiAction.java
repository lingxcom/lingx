package com.lingx.support.web.action;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
/*
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;*/
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import com.alibaba.fastjson.JSON;
import com.lingx.core.Constants;
import com.lingx.core.Page;
import com.lingx.core.SpringContext;
import com.lingx.core.action.IResponseAware;
import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IHttpRequest;
import com.lingx.core.model.bean.UserBean;
import com.lingx.core.utils.Utils;
import com.lingx.support.workflow.ProcessDefinitionGeneratorEx;
import com.lingx.support.workflow.TaskJumpCmd;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年9月4日 上午9:59:03 
 * 类说明 
 */
public class WorkflowActivitiAction  extends AbstractJsonAction implements IResponseAware{

	@Override
	public void setResponse(HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String execute(IContext context) {
		// TODO Auto-generated method stub
		return null;
	}
	/*private HttpServletResponse response;
	@Override
	public String execute(IContext context) {
		IHttpRequest request=context.getRequest();
		String cmd=request.getParameter("_c");
		Map<String,Object> ret=new HashMap<String,Object>();ret.put("code", 1);ret.put("success", true);ret.put("message", "操作成功");
		ApplicationContext applicationContext = SpringContext.getApplicationContext();
		ProcessEngine processEngine=applicationContext.getBean(ProcessEngine.class);	
		JdbcTemplate jdbc=applicationContext.getBean("jdbcTemplate",JdbcTemplate.class);
		boolean isReturnJSON=true;
		if(context.getSession().get(Constants.SESSION_USER)==null)return JSON.toJSONString(new HashMap<String,Object>());
		UserBean userBean=(UserBean)context.getSession().get(Constants.SESSION_USER);
		if("claim".equals(cmd)){//签收
			isReturnJSON=this.claim(request.getParameter("taskid"), processEngine, userBean, ret);
		}else if("start".equals(cmd)){//启动流程
			isReturnJSON=this.start(request, response, processEngine, userBean, ret);
		}else if("save".equals(cmd)){//保存表单
			isReturnJSON=this.save(request, response, processEngine, userBean, ret);
		}else if("submit".equals(cmd)){//提交表单
			isReturnJSON=this.submit(request, response, processEngine, userBean, ret);
		}else if("complete".equals(cmd)){//强制完成任务
			isReturnJSON=this.complete(request.getParameter("taskid"), processEngine, userBean, ret);
		}else if("traceImage".equals(cmd)){//跟踪图
			isReturnJSON=this.traceImage(request, response, processEngine);
		}else if("image".equals(cmd)){
			isReturnJSON=this.image(request, response, processEngine);
		}else if("history".equals(cmd)){//历史
			isReturnJSON=this.history(request, response, processEngine, userBean, ret);
		}else if("variable".equals(cmd)){//变量
			isReturnJSON=this.getVariable(request, response, processEngine, userBean, ret);
		}else if("assignee".equals(cmd)){//委派
			isReturnJSON=this.assignee(request.getParameter("taskid"),request.getParameter("userid"),jdbc, processEngine, ret);
		}else if("rollback".equals(cmd)){//回退
			isReturnJSON=this.jump(request.getParameter("taskid"), processEngine, ret);
		}else if("delete".equals(cmd)){//删除流程
			isReturnJSON=this.delete(request.getParameter("taskid"), processEngine,userBean, ret);
		}else if("strat".equals(cmd)){
			
		}else if("strat".equals(cmd)){
			
		}else{
			ret.put("message", "参数_c有误");
		}
		if(isReturnJSON){
			return JSON.toJSONString(ret);
		}else return Page.PAGE_NORET;
	}

	*//**
	 * 删除
	 * @param taskid
	 * @param processEngine
	 * @param userBean
	 * @param ret
	 * @return
	 *//*
	private boolean delete(String taskid,ProcessEngine processEngine,UserBean userBean,Map<String,Object> ret){
		Task task=processEngine.getTaskService().createTaskQuery().taskId(taskid).singleResult();
		processEngine.getRuntimeService().deleteProcessInstance(task.getProcessInstanceId(), userBean.getAccount());
		return true;
	}
	*//**
	 * 任务回退
	 * @param taskid
	 * @param processEngine
	 * @param ret
	 * @return
	 *//*
	private boolean jump(String taskid,ProcessEngine processEngine,Map<String,Object> ret){
		Task task=processEngine.getTaskService().createTaskQuery().taskId(taskid).singleResult();
		List historicTasks = ((HistoricTaskInstanceQuery)processEngine.getHistoryService().createHistoricTaskInstanceQuery().processInstanceId(task.getProcessInstanceId())).list();
		String targetKey=null;
		//task.get
		if(historicTasks.size()>=2){
			HistoricTaskInstanceEntity t=(HistoricTaskInstanceEntity)historicTasks.get(historicTasks.size()-2);
			targetKey=t.getTaskDefinitionKey();
		}
		if(targetKey!=null){
			TaskJumpCmd cmd=new TaskJumpCmd(taskid,targetKey);
			processEngine.getManagementService().executeCommand(cmd);
		}else{
			ret.put("message", "该任务不可回退");
		}
		return true;
	}
	*//**
	 * 委派任务
	 * @param taskid 
	 * @param processEngine
	 * @param userBean
	 * @param ret
	 * @return 是否返回JSON ret
	 *//*
	private boolean assignee(String taskid,String userid,JdbcTemplate jdbc,ProcessEngine processEngine,Map<String,Object> ret) {
		String sql="select account from tlingx_user where id=?";
		Map<String,Object> user=jdbc.queryForMap(sql,userid);
		 processEngine.getTaskService().setAssignee(taskid,user.get("account").toString());//.claim(taskid,user.get("account").toString());
		 ret.put("message", "任务委派成功");
		 return true;
	}
	*//**
	 * 取出流程变量
	 * @param request
	 * @param response
	 * @param processEngine
	 * @param userBean
	 * @param ret
	 * @return
	 *//*
	public boolean getVariable(IHttpRequest request,HttpServletResponse response,ProcessEngine processEngine,UserBean userBean,Map<String,Object> ret){
		
		String taskid=request.getParameter("taskid");
		if(Utils.isNotNull(taskid)){
			ret.put("taskid",taskid);
			try {
				Task task=processEngine.getTaskService().createTaskQuery().taskId(taskid).singleResult();
				Map<String,Object> map=processEngine.getRuntimeService().getVariables(task.getExecutionId());
				ret.putAll(map);
				
			} catch (Exception e) {
				HistoricTaskInstance t=processEngine.getHistoryService().createHistoricTaskInstanceQuery().taskId(taskid).singleResult();
				List<HistoricVariableInstance> lists=processEngine.getHistoryService().createHistoricVariableInstanceQuery().processInstanceId(t.getProcessInstanceId()).list();
				for(HistoricVariableInstance temp:lists){
					ret.put(temp.getVariableName(), temp.getValue());
				}
			}
		}
		return true;
	}
	*//**
	 * 流程历史记录
	 * @param request
	 * @param response
	 * @param processEngine
	 * @param userBean
	 * @param ret
	 * @return
	 *//*
	private boolean history(IHttpRequest request,HttpServletResponse response,ProcessEngine processEngine,UserBean userBean,Map<String,Object> ret){
		String processInstanceId=request.getParameter("processInstanceId");
		List historicTasks = ((HistoricTaskInstanceQuery)processEngine.getHistoryService().createHistoricTaskInstanceQuery().processInstanceId(processInstanceId)).list();
		List historicVariableInstances = processEngine.getHistoryService().createHistoricVariableInstanceQuery().processInstanceId(processInstanceId).list();

		ret.put("tasks", historicTasks);//任务
		ret.put("vars", historicTasks);//变量
		
		return true;
	}
	*//**
	 * 签收任务
	 * @param taskid 
	 * @param processEngine
	 * @param userBean
	 * @param ret
	 * @return 是否返回JSON ret
	 *//*
	private boolean claim(String taskid,ProcessEngine processEngine,UserBean userBean,Map<String,Object> ret) {
		 processEngine.getTaskService().claim(taskid, userBean.getAccount());
		 ret.put("message", "任务已签收");
		 return true;
	}
	*//**
	 * 强制完成任务
	 * @param taskid
	 * @param processEngine
	 * @param userBean
	 * @param ret
	 * @return
	 *//*
	private boolean complete(String taskid,ProcessEngine processEngine,UserBean userBean,Map<String,Object> ret) {
		 processEngine.getTaskService().complete(taskid);
		 ret.put("message", "任务已完成");
		 return true;
	}
	*//**
	 * 启动流程
	 * @param request
	 * @param response
	 * @param processEngine
	 * @param userBean
	 * @param ret
	 *//*
	private boolean start(IHttpRequest request,
			HttpServletResponse response,ProcessEngine processEngine,UserBean userBean,Map<String,Object> ret){
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("_REQ_USER", userBean.getAccount());//启动用户，流程环境变量
		params.put("_REQ_USER_NAME", userBean.getName());//启动用户，流程环境变量
		params.put("_REQ_DATE", Utils.getTime());//启动用户，流程环境变量
		String workflowid=request.getParameter("workflowid");
		ProcessInstance pi=processEngine.getRuntimeService().startProcessInstanceById(workflowid, params);
		 return true;
	}
	*//**
	 * 保存表单
	 * @param request
	 * @param response
	 * @param processEngine
	 * @param userBean
	 * @param ret
	 *//*
	private boolean save(IHttpRequest request,
			HttpServletResponse response,ProcessEngine processEngine,UserBean userBean,Map<String,Object> ret){
		String taskid=request.getParameter("taskid");
		processEngine.getFormService().saveFormData(taskid, getRequestParamsMapString(request));
		 return true;
	}
	*//**
	 * 提交表单
	 * @param request
	 * @param response
	 * @param processEngine
	 * @param userBean
	 * @param ret
	 *//*
	private boolean submit(IHttpRequest request,
			HttpServletResponse response,ProcessEngine processEngine,UserBean userBean,Map<String,Object> ret){
		String taskid=request.getParameter("taskid");
		processEngine.getFormService().submitTaskFormData(taskid, getRequestParamsMapString(request));
		 return true;
	}
	*//**
	 * 输出跟踪图
	 * @param request
	 * @param response
	 * @return
	 *//*
	private boolean traceImage(IHttpRequest request,
			HttpServletResponse response,ProcessEngine processEngine){
		String processInstanceId=request.getParameter("processInstanceId");
		ProcessDefinitionGeneratorEx ext=new ProcessDefinitionGeneratorEx(processEngine);
		InputStream is=null;
		try {
			is=ext.generateDiagramWithHighLight(processInstanceId);//自己山寨的，需要左上顶边
			//is=getDiagram(processInstanceId,processEngine);//这是官方接口，目前会中文乱码
			response.setContentType("image/png");
			byte bytes[]=this.getImageBytes(is);
			response.getOutputStream().write(bytes);
			response.getOutputStream().flush();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(is!=null)is.close();
				response.getOutputStream().close();
			} catch (IOException e) {
			}
		}
		return false;
	}
	private boolean traceProcess(String processInstanceId,ProcessEngine processEngine){
		// List<Map<String, Object>> activityInfos = .traceProcess(processInstanceId);
		return true;
	}
	private boolean image(IHttpRequest request,
			HttpServletResponse response,ProcessEngine processEngine){
		String processDefinitionId=request.getParameter("processDefinitionId");
		ProcessDefinition processDefinition = processEngine.getRepositoryService().createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
	    
		String resourceName = processDefinition.getDiagramResourceName();
		InputStream is = processEngine.getRepositoryService().getResourceAsStream(processDefinition.getDeploymentId(), resourceName);

		ByteArrayOutputStream byteStream=new ByteArrayOutputStream();
		try {
			BufferedImage image=ImageIO.read(is);
			ImageIO.write(image, "png", byteStream);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		byte bytes[]=byteStream.toByteArray();
		try{
		byteStream.close();
		is.close();
		}catch(Exception e){}
		response.setContentType("image/png");
		try {
			response.getOutputStream().write(bytes);
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	private InputStream getDiagram(String processInstanceId,ProcessEngine processEngine){
		DefaultProcessDiagramGenerator processDiagramGenerator=new DefaultProcessDiagramGenerator();
		ProcessInstance pi=processEngine.getRuntimeService().createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		RepositoryServiceImpl rs=(RepositoryServiceImpl)processEngine.getRepositoryService();
		//ProcessDefinitionEntity pd=(ProcessDefinitionEntity)rs.getDeployedProcessDefinition(pi.getProcessDefinitionId());
		BpmnModel bpmnModel = rs.getBpmnModel(pi.getProcessDefinitionId());
		
		Context.setProcessEngineConfiguration((ProcessEngineConfigurationImpl)processEngine.getProcessEngineConfiguration()); 
		InputStream is=processDiagramGenerator.generateDiagram(bpmnModel,"png",processEngine.getRuntimeService().getActiveActivityIds(processInstanceId));
		//InputStream is=processDiagramGenerator.generatePngDiagram(bpmnModel);
		return is;
	}
	private byte[] getImageBytes(InputStream is)throws Exception{
		ByteArrayOutputStream byteStream=new ByteArrayOutputStream();
		int b;
		while((b=is.read())!=-1){
			byteStream.write(b);
		}
		byte[] bs=byteStream.toByteArray();
		byteStream.close();
		is.close();
		return bs;
	}
	private Map<String,Object> getRequestParamsMapObject(IHttpRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		Enumeration<String> keys=request.getParameterNames();
		String key,value;
		while(keys.hasMoreElements()){
			key=keys.nextElement();
			value=request.getParameter(key);
			//System.out.println(String.format("%s:%s", key,value));
			map.put(key, value);
		}
		
		return map;
	}
	private Map<String,String> getRequestParamsMapString(IHttpRequest request){
		Map<String,String> params=new HashMap<String,String>();
		Set<String> set=request.getParameterNames();
		for(String key:set){
			params.put(key, request.getParameter(key));
		}
		
		return params;
	}

	@Override
	public void setResponse(HttpServletResponse response) {
		this.response=response;
	}*/
}
