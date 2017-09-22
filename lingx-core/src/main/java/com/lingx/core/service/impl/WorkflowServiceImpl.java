package com.lingx.core.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
//import org.activiti.engine.ProcessEngine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.lingx.core.Constants;
import com.lingx.core.Page;
import com.lingx.core.SpringContext;
import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.service.IFormService;
import com.lingx.core.service.ILingxService;
import com.lingx.core.service.IPageService;
import com.lingx.core.service.ITaskService;
import com.lingx.core.service.IWorkflowService;
import com.lingx.core.utils.DateUtils;
import com.lingx.core.utils.Utils;
import com.lingx.core.workflow.IWorkflow;
import com.lingx.core.workflow.event.ClaimEvent;
/** 
 * @author www.lingx.com
 * @version 创建时间：2015年9月4日 上午10:32:53 
 * 类说明 
 */
@Component(value="lingxWorkflowService")
@Transactional 
public class WorkflowServiceImpl implements IWorkflowService{
	public static final Logger logger=LogManager.getLogger(WorkflowServiceImpl.class);
	public static final SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	//@Resource
	//private ProcessEngine processEngine;
	@Resource(name="jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	@Resource
	private ILingxService lingxService;
	@Resource
	private IPageService pageService;
	@Resource
	private IFormService formService;
	@Resource
	private ITaskService taskService;
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	@Override
	public void updateLinePoint(String defineId, int sourceTop,
			int sourceLeft, int targetTop, int targetLeft) {
		int c=this.jdbcTemplate.update("update tlingx_wf_define_line set top1=?,left1=? where define_id=? and top1=? and left1=?",targetTop,targetLeft,defineId,sourceTop,sourceLeft);
		if (c > 0) {
			List<Map<String, Object>> list = this.jdbcTemplate.queryForList("select id from tlingx_wf_define_line where define_id=? and top1=? and left1=? ",defineId,targetTop,targetLeft);
			for (Map<String, Object> map : list) {
				this.lineTest(map.get("id"));
			}
		}
		c=this.jdbcTemplate.update("update tlingx_wf_define_line set top2=?,left2=? where define_id=? and top2=? and left2=?",targetTop,targetLeft,defineId,sourceTop,sourceLeft);
		if (c > 0) {
			List<Map<String, Object>> list = this.jdbcTemplate.queryForList("select id from tlingx_wf_define_line where define_id=? and top2=? and left2=? ",defineId,targetTop,targetLeft);
			for (Map<String, Object> map : list) {
				this.lineTest(map.get("id"));
			}
		}
	}
	@Override
	public void updateLinePoint(String taskId, int targetTop,
			int targetLeft) {
		Map<String,Object> task=this.jdbcTemplate.queryForMap("select * from tlingx_wf_define_task where id=?",taskId);
		int top=Integer.parseInt(task.get("top").toString());
		int left=Integer.parseInt(task.get("left").toString());
		//int width=Integer.parseInt(task.get("width").toString());
		//int height=Integer.parseInt(task.get("height").toString());

		
		this.updateLinePoint(task.get("define_id").toString(), top, left, targetTop, targetLeft);
		
		
	}
	@Override
	public String round(String temp) {
		return String.valueOf(round(Integer.parseInt(temp)));
	}
	@Override
	public int round(int temp) {
		int ys=temp%20;
		temp=temp-ys;
		if(ys>10)temp+=20;
		return temp;
	}
	@Override
	public String createInstance(IWorkflow workflow,IContext context,IPerformer performer) {
		String defineId=workflow.getDefineId();
		
		String taskId=this.createInstance(defineId, context, performer);
		String isMobile="";
		if("true".equals(context.getRequest().getParameter("is_mobile"))){
			isMobile="&is_mobile=true";
		}
		context.getRequest().setAttribute(Constants.REQUEST_ATTR, "w?m=form&_TASK_ID="+taskId+isMobile);
		return this.pageService.getUrlPage();
	}
	
	private String createInstance(String defineId,IContext context,IPerformer performer){

		String currentInstId=this.lingxService.uuid();
		String currentTaskId=this.lingxService.uuid();
		String time=Utils.getTime();
		String defineTaskId=this.taskService.getStartTaskDefineId(defineId,context,performer);
		Map<String,Object> defineMap=this.jdbcTemplate.queryForMap("select * from tlingx_wf_define where id=?",defineId);
		String title=defineMap.get("name")+"-"+context.getUserBean().getName()+"-"+DateUtils.format14To8(time);
		this.jdbcTemplate.update("insert into tlingx_wf_instance(id,name,define_id,user_id,task_id,status,create_time,modify_time,serial_number) values(?,?,?,?,?,?,?,?,nextval('WORKFLOW_INSTANCE'))",
				currentInstId,title,defineId,context.getUserBean().getId(),currentTaskId,3,time,time);
		this.taskService.addParam("_TITLE", title, currentInstId);
		this.taskService.addParam("_USER_ID", context.getUserBean().getId(), currentInstId);
		this.taskService.addParam("_USER_NAME", context.getUserBean().getName(), currentInstId);
		this.taskService.addParam("_ORG_ID", context.getUserBean().getOrgId(), currentInstId);
		this.taskService.addParam("_ORG_NAME", context.getUserBean().getOrgName(), currentInstId);
		this.taskService.addParam("_ROLE_ID", context.getUserBean().getRegexp().getCurrentRole(), currentInstId);
		this.taskService.addParam("_START_TIME", time, currentInstId);
		this.taskService.addParam("_START_TIME_F", sdf.format(new Date()), currentInstId);
		this.taskService.addParam("_DEFINE_ID", defineId, currentInstId);
		this.taskService.addParam("_INSTANCE_ID", currentInstId, currentInstId);
		this.taskService.addParam("_YEAR", String.valueOf(1900+new Date().getYear()), currentInstId);
		//this.taskService.addParam("_TASK_ID", currentTaskId, currentInstId);
		
		
		String array[]=this.taskService.create(defineTaskId,currentInstId,context,performer);
		this.jdbcTemplate.update("update tlingx_wf_instance set task_id=? where id=? ",array[0],currentInstId);
		//context.getRequest().setAttribute(Constants.REQUEST_ATTR, "w?m=submit&_TASK_ID="+array[0]);
		this.submitTask(array[0], context, performer);
		String taskId=this.getParams(currentInstId,"_TASK_ID");
		return taskId;
	}
	
	
	public List<Map<String,Object>> getParams(String instanceId){
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select name,value from tlingx_wf_instance_value where instance_id=?",instanceId);
		return list;
	}

	public String getParams(String instanceId,String name){
		return this.jdbcTemplate.queryForObject("select value from tlingx_wf_instance_value where instance_id=? and name=?",String.class,instanceId,name);
	}
	/**
	 * 提交任务
	 * @param taskId
	 * @return
	 */
	
	public Map<String,Object> submitTask(String taskId,IContext context,IPerformer performer){
		//System.out.println("===>"+taskId);
		Map<String,Object> ret=new HashMap<String,Object>();
		int taskType =0;
		boolean isCompelete=this.taskService.submit(taskId, context, performer);
		//System.out.println("isCompelete:"+isCompelete);
		if(isCompelete){

			Map<String,Object> task=this.jdbcTemplate.queryForMap("select * from tlingx_wf_instance_task where id=?",taskId);
			Map<String,Object> taskDefine=this.jdbcTemplate.queryForMap("select * from tlingx_wf_define_task where id=?",task.get("task_id"));
			if(taskDefine.get("script_after")!=null){
				try {
					String temp=taskDefine.get("script_after").toString();
					if(!"".equals(temp))
					this.taskService.script(temp, taskId,context, performer);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					this.taskService.log(task.get("instance_id").toString(), taskId, e.getLocalizedMessage(), context);
				}
			}
			
			
			
			String array[]= this.taskService.nextTaskDefineIds(taskId, context, performer);
			//以上是找出流程处理的下一个节点
			//以下是创建任务实例

			if(array.length==0){
				ret.put("code", 3);
				ret.put("message", "该流程已经处理完成，结束流程。");
				return ret;
			}
			//System.out.println("1、array[0]:"+array.length);
			String instanceId=(this.jdbcTemplate.queryForObject("select instance_id from tlingx_wf_instance_task where id=?", String.class,taskId));
			 array=	this.taskService.create(array, instanceId, context, performer);
			
			
			for(String targetTaskId:array ){//下一个任务，如果是单个就看要不要自动处理
				if(Utils.isNull(targetTaskId))continue;
				taskType=this.jdbcTemplate.queryForInt("select t.type from tlingx_wf_define_task t ,tlingx_wf_instance_task a where t.id=a.task_id and a.id=?",targetTaskId);
				if(taskType==2||taskType==4){//自动处理
					//this.taskService.execute(targetTaskId, context, performer);//因为在this.taskService.submit()有执行脚本，所以这里就不执行了
					if(array.length==1){
						return this.submitTask(targetTaskId, context, performer);
					}
				}
			}
			
			
			
			//System.out.println("2、array[0]:"+array[0]);
			if(array.length==1&&isOwn(array[0],context.getUserBean().getId())&&taskType==3){//又是自己的任务，必须是手工任务，重新打开表单
				this.taskService.addParam("_TASK_ID", array[0], instanceId);
				//return this.formTask(array[0], workflow, context, performer);
				ret.put("code", 2);
				ret.put("message", "自己的手工任务，重新打开表单");
				ret.put("_TASK_ID", array[0]);

				ret.put("taskId", array[0]);
			}else{
//				ret.put("code", 3);
//				ret.put("message", "成功转交给下一人处理！");
//				ret.put("taskId", array[0]);

				taskId=array[0];
				 task=this.jdbcTemplate.queryForMap("select user_id,name from tlingx_wf_instance_task where id=?",taskId);
				if(task.get("user_id")!=null&&!"".equals(task.get("user_id").toString())){
					String userName="";
					//System.out.println(task.get("user_id"));
					if(task.get("user_id").toString().indexOf(",")==-1){
						userName=this.jdbcTemplate.queryForObject("select name from tlingx_user where id=?", String.class,task.get("user_id"));
						userName+=array.length>1?"等":"";
								}else{
						userName=this.jdbcTemplate.queryForObject("select name from tlingx_user where id=?", String.class,task.get("user_id").toString().split(",")[0])+"等";
					}
					ret.put("code", 3);
					ret.put("userName", userName);
					ret.put("message", "任务提交成功，下一处理人:"+userName+"。");
				}else{
					ret.put("code", 3);//下一任务没有处理人
					ret.put("message", "下一任务没有处理人");
					ret.put("needUser", true);
				}
				ret.put("taskId", taskId);
			}
			
		}else{
			ret.put("code", 3);
			ret.put("message", "任务已提交，但需等待其他人员处理!");
		}

		return ret;
	}
	public Map<String,Object> quickEnd(String taskId){
		Map<String,Object> ret=new HashMap<String,Object>();
		String instanceId=this.jdbcTemplate.queryForObject("select instance_id from tlingx_wf_instance_task where id=?",String.class,taskId);
		this.jdbcTemplate.update("update tlingx_wf_instance set status=4 where id=?",instanceId);
		ret.put("code", 1);
		ret.put("message", "操作成功！");
		return ret;
	}
	private boolean isOwn(String taskId,String userId){
		return this.jdbcTemplate.queryForInt("select count(*) from tlingx_wf_instance_task where id=? and user_id=?",taskId,userId)==1;
	}
	
	private String getDefineTaskIdByType(int type,String defineId){
		String temp=null;
		if(this.jdbcTemplate.queryForInt("select count(*) from tlingx_wf_define_task where define_id=? and type=?",defineId,type)==1){
			temp=this.jdbcTemplate.queryForObject("select id from tlingx_wf_define_task where define_id=? and type=?", String.class,defineId,type);
		}
		return temp;
	}
	@Override
	public String formTask(String taskId,IWorkflow workflow, IContext context, IPerformer performer) {
		if(taskId.indexOf(",")!=-1)taskId=taskId.split(",")[0];
		Map<String,Object> task=this.jdbcTemplate.queryForMap("select * from tlingx_wf_instance_task where id=?",taskId);
		Map<String,Object> defineTask=this.jdbcTemplate.queryForMap("select * from tlingx_wf_define_task where id=?",task.get("task_id"));
		Map<String,Object> instance=this.jdbcTemplate.queryForMap("select * from tlingx_wf_instance where id=?",task.get("instance_id"));
		Map<String,Object> define=this.jdbcTemplate.queryForMap("select * from tlingx_wf_define where id=?",instance.get("define_id"));
		/*if(instance.get("task_id").toString().indexOf(taskId)==-1){
			context.addMessage("1566", "执行失败，非当前任务");
			return this.pageService.getPage(Page.PAGE_WF_ERROR);
		}
		if(!task.get("status").toString().equals("2")){//非待办
			context.addMessage("1567", "执行失败，非待办任务");
			return this.pageService.getPage(Page.PAGE_WF_ERROR);
		}*/
		if(!context.getUserBean().getId().equals(task.get("user_id").toString())){
			context.addMessage("1568", "执行失败，非当前任务处理人");
			return this.pageService.getPage(Page.PAGE_WF_ERROR);
		}
		Map<String,Object> contentMap=this.jdbcTemplate.queryForMap("select content,content2,content3,attach_html from tlingx_wf_define_form where id=(select form_id from tlingx_wf_define_task where id=(select task_id from tlingx_wf_instance_task where id=?))", taskId);
		
		String content="", content2="",content3="",attach_html="";
		if(contentMap.get("content")!=null)
			content=contentMap.get("content").toString();
		if(contentMap.get("content2")!=null)
			 content2=contentMap.get("content2").toString();
		if(contentMap.get("content3")!=null)
			 content3=contentMap.get("content3").toString();
		if(contentMap.get("attach_html")!=null)
			attach_html=contentMap.get("attach_html").toString();
		workflow.setInstanceId(task.get("instance_id").toString());
		content=this.formService.formWorkflowRendering(content, workflow.getInstanceId(), context);
		content2=this.formService.formWorkflowRendering(content2, workflow.getInstanceId(), context);
		content3=this.formService.formWorkflowRendering(content3, workflow.getInstanceId(), context);
		context.getRequest().setAttribute("content", content);
		context.getRequest().setAttribute("content2", content2);
		context.getRequest().setAttribute("content3", content3);
		context.getRequest().setAttribute("attach_html", attach_html);
		List<Map<String,Object>> list=this.getParams(workflow.getInstanceId());
		context.getRequest().setAttribute("task", task);
		context.getRequest().setAttribute("task_id", taskId);
		context.getRequest().setAttribute("auth_tag", defineTask.get("auth_tag"));
		context.getRequest().setAttribute("task_type", defineTask.get("type"));
		context.getRequest().setAttribute("auth_type", defineTask.get("auth"));
		context.getRequest().setAttribute("define_type", define.get("type"));
		context.getRequest().setAttribute("params", list);
		context.getRequest().setAttribute("params_json", JSON.toJSONString(list));
		context.getRequest().setAttribute("instance_id", workflow.getInstanceId());
		context.getRequest().setAttribute("define_task_id", task.get("task_id"));
		context.getRequest().setAttribute("history", this.jdbcTemplate.queryForList("select t.*,a.name user_name from tlingx_wf_instance_task t,tlingx_user a  where t.user_id=a.id and instance_id=? order by stime desc,etime desc",workflow.getInstanceId()));
		context.getRequest().setAttribute("attachment", this.jdbcTemplate.queryForList("select t.*,a.name user_name from tlingx_wf_instance_attachment t,tlingx_user a where t.user_id=a.id and instance_id=? order by create_time desc",workflow.getInstanceId()));
		
		List<Map<String,Object>> listOpinion=this.jdbcTemplate.queryForList("select t.content,t.create_time,t.ret,a.name user_name from tlingx_wf_instance_opinion t,tlingx_user a  where t.user_id=a.id and instance_id=? order by create_time asc",workflow.getInstanceId());
		context.getRequest().setAttribute("opinion_length",listOpinion.size());
		context.getRequest().setAttribute("opinionList",listOpinion);
		context.getRequest().setAttribute("opinion", JSON.toJSONString(listOpinion));
		context.getRequest().setAttribute("model", "form");
		
		context.getRequest().setAttribute("instance", instance);
		context.getRequest().setAttribute("define", define);
		context.getRequest().setAttribute("user", this.jdbcTemplate.queryForMap("select t.id, t.name, a.name orgname from tlingx_user t,tlingx_org a where t.org_id=a.id and t.id=?",instance.get("user_id")));
		
		
		if(instance.get("task_id").toString().indexOf(taskId)==-1){
			context.getRequest().setAttribute("model", "view");
		}
		if(!task.get("status").toString().equals("2")){//非待办
			context.getRequest().setAttribute("model", "view");
		}
		String page=this.pageService.getPage(Page.PAGE_WF_FORM);
		if("true".equals(context.getRequest().getParameter("is_mobile"))){
			//只有创建流程时，或者指定要编辑才会跳到编辑表单
			if(3==Integer.parseInt(instance.get("status").toString())||"true".equals(context.getRequest().getParameter("is_edit"))){
				page=this.pageService.getPage(Page.PAGE_WF_FORM2);
			}else{
				page=this.pageService.getPage(Page.PAGE_WF_FORM2_APPROVAL);
				
			}
			
		}
		return page;
	}
	@Override
	public Map<String,Object> claimTask(String taskId, IWorkflow workflow,IContext context, IPerformer performer) {
		this.taskService.claim(taskId, context.getUserBean().getId(), context, performer);
		Map<String,Object> ret=new HashMap<String,Object>();
		//return this.pageService.getPage(Page.PAGE_WF_SUCCESS);
		ret.put("code", 3);
		ret.put("message", "操作成功");
		return ret;
	}
	@Override
	public String saveTask(String taskId, IWorkflow workflow, IContext context,
			IPerformer performer) {
			this.taskService.save(taskId, context, performer);
		return this.formTask(taskId, workflow, context, performer);
	}
	
	public void lineTest(Object id){
		Map<String,Object>line=this.jdbcTemplate.queryForMap("select * from tlingx_wf_define_line where id=?",id);
		int top1=Integer.parseInt(line.get("top1").toString());
		int left1=Integer.parseInt(line.get("left1").toString());
		int top2=Integer.parseInt(line.get("top2").toString());
		int left2=Integer.parseInt(line.get("left2").toString());
		
		Point4 pt1=new Point4(top1,left1);
		Point4 pt2=new Point4(top2,left2);
		Point temp1=null,temp2=null;int tempLen=-1,l;
		for(Point p1:pt1.ponts){
			for(Point p2:pt2.ponts){
				l=Math.abs(p1.top-p2.top)+Math.abs(p1.left-p2.left);
				if(tempLen==-1){
					tempLen=l;
					temp1=p1;
					temp2=p2;
				}else {
					if(l<tempLen){
						tempLen=l;
						temp1=p1;
						temp2=p2;
					}
				}
			}
		}
		this.jdbcTemplate.update("update tlingx_wf_define_line set source_point=?,target_point=? where id=? and is_auto=1",temp1.p,temp2.p,id);
	}
	
	class Point4{
		public Point4(int t,int l){
			int h=48,w=98;
			t=t-h/2;
			l=l-w/2;
			int pt=t,pl=l+(w/2);//上
			ponts[0]=new Point(0,pt,pl);
			pt=t+(h/2);pl=l+w;//右
			ponts[1]=new Point(1,pt,pl);
			pt=t+h;pl=l+(w/2);//下
			ponts[2]=new Point(2,pt,pl);
			pt=t+(h/2);pl=l;//左
			ponts[3]=new Point(3,pt,pl);
		}
		Point ponts[]=new Point[4];
	}
	class Point{
		public Point(int p1,int t,int l){
			p=p1;
			top=t;
			left=l;
		}
		int p;//方位
		int top;
		int left;
	}
	@Override
	public Map<String, Object> getInstanceAll(String instanceId,
			IWorkflow workflow, IContext context, IPerformer performer) {
		Map<String,Object> ret=new HashMap<String,Object>();
		Map<String,Object> instance=this.jdbcTemplate.queryForMap("select * from tlingx_wf_instance where id=?",instanceId);
		Map<String,Object> define=new HashMap<String,Object>();
		define.put("tasks", this.jdbcTemplate.queryForList("select * from tlingx_wf_define_task where define_id=?",instance.get("define_id")));
		define.put("lines", this.jdbcTemplate.queryForList("select * from tlingx_wf_define_line where define_id=?",instance.get("define_id")));
		
		List<Map<String,Object>> history=this.jdbcTemplate.queryForList("select * from tlingx_wf_instance_task where instance_id=? order by stime asc, etime asc",instanceId);
		
		ret.put("define", define);
		ret.put("history", history);
		ret.put("instance", instance);
		ret.put("currentTaskId", this.getCurrentDefineTaskIds(instance.get("task_id")));
		return ret;
	}
	private String getCurrentDefineTaskIds(Object task_id){
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select task_id from tlingx_wf_instance_task where id in('"+task_id.toString().replaceAll(",", "','")+"')" );
		StringBuilder sb=new StringBuilder();
		for(Map<String,Object> map:list){
			sb.append(map.get("task_id")).append(",");
		}
		if(sb.length()>0)sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
	@Override
	public Map<String,Object> fallback(String taskId, IWorkflow workflow, IContext context,
			IPerformer performer) {
		Map<String,Object> ret=new HashMap<String,Object>();
		String taskIdNew=this.taskService.fallback(taskId, context, performer);
		//return this.pageService.getPage(Page.PAGE_WF_SUCCESS);
		ret.put("code", 3);
		ret.put("message", "操作成功");
		ret.put("taskId", taskIdNew);
		return ret;
	}
	@Override
	public Map<String,Object> delegate(String taskId, IWorkflow workflow, IContext context,
			IPerformer performer) {
		Map<String,Object> ret=new HashMap<String,Object>();
		String targetUserId=context.getRequest().getParameter("user_id");
		this.jdbcTemplate.update("update tlingx_wf_instance_task set user_id=? where id=?",targetUserId,taskId);
		ret.put("code", 3);
		ret.put("message", "已成功将任务委托给【"+this.jdbcTemplate.queryForObject("select name from tlingx_user where id=?", String.class,targetUserId)+"】");
		return ret;
		//return this.pageService.getPage(Page.PAGE_WF_SUCCESS);
	}
	@Override
	public Map<String, Object> forward(String taskId, IWorkflow workflow,
			IContext context, IPerformer performer) {
		Map<String,Object> task=this.jdbcTemplate.queryForMap("select * from tlingx_wf_instance_task where id=?",taskId);
		String ret=context.getRequest().getParameter("ret_approve");
		String content=context.getRequest().getParameter("content_approve");
		try {
			if(content!=null)content=URLDecoder.decode(content,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		this.taskService.approve(taskId, content, ret, context, performer);
		String newTaskId=this.taskService.copyTaskInstance(taskId);
		String nextUserId=context.getRequest().getParameter("nextUserId");
		this.jdbcTemplate.update("update tlingx_wf_instance set task_id=? where id=?",newTaskId,task.get("instance_id"));

		//this.jdbcTemplate.update("update tlingx_wf_instance_task set user_id=? where id=?",nextUserId,taskId);
		this.taskService.claim(newTaskId, nextUserId, context, performer);

		Map<String,Object> retMap=new HashMap<String,Object>();

		retMap.put("code", 3);
		retMap.put("message", "操作成功");
		return retMap;
	}
	
	@Override
	public Map<String,Object> approve(String taskId, IWorkflow workflow, IContext context,
			IPerformer performer) {
		Map<String,Object> retMap=new HashMap<String,Object>();
		Map<String,Object> task=this.jdbcTemplate.queryForMap("select * from tlingx_wf_instance_task where id=?",taskId);
		Map<String,Object> defineTask=this.jdbcTemplate.queryForMap("select * from tlingx_wf_define_task where id=?",task.get("task_id"));
		
		String ret=context.getRequest().getParameter("ret_approve");
		String content=context.getRequest().getParameter("content_approve");
		try {
			if(content!=null)content=URLDecoder.decode(content,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		this.taskService.approve(taskId, content, ret, context, performer);
		int sign=this.signCurrent(taskId);//1、全为同意 2、全为不同意 3、有同意 4、有不同意 5、其他 6、未处理完
		this.taskService.addParam("_SIGN", String.valueOf(sign), task.get("instance_id").toString());
		int defineSignRet=Integer.parseInt(defineTask.get("sign_ret").toString());
		
		logger.debug("=============>sign={},defineSignRet={}",sign,defineSignRet);
		if(sign!=6){//全部审核
				
			switch(defineSignRet){
			case 1:
				
				if(sign==defineSignRet){
					return this.submitTask(taskId, context, performer);
				}else if(sign==4||sign==2){//有人不同意就回退
					setTaskComplete(taskId);
					return this.fallback(taskId, workflow, context, performer);
				}
				break;
			case 3:
				if(sign==defineSignRet){//有个通过就通过
					setTaskComplete(taskId);
					return this.submitTask(taskId, context, performer);
				}else if(sign==4||sign==2){//有人不同意就回退
					setTaskComplete(taskId);
					return this.fallback(taskId, workflow, context, performer);
				}
				break;
			default:
				
			}

		}
		{
			retMap.put("code", 3);
			retMap.put("message", "操作成功");
			//return this.pageService.getPage(Page.PAGE_WF_SUCCESS);
			return retMap;
		}
		
	}
	@Override
	public Map<String, Object> comment(String taskId, IWorkflow workflow,
			IContext context, IPerformer performer) {
		Map<String,Object> retMap=new HashMap<String,Object>();
		String time=Utils.getTime();
		String content=context.getRequest().getParameter("content");
		Map<String,Object> task=this.jdbcTemplate.queryForMap("select * from tlingx_wf_instance_task where id=?",taskId);
		//评论不存任务节点 2016-04-28
		String sql="insert into tlingx_wf_instance_opinion(id,instance_id,task_id,fid,content,ret,user_id,create_time,modify_time) values(uuid(),?,?,?,?,?,?,?,?)";
		this.jdbcTemplate.update(sql,task.get("instance_id"),"","0",content,0,context.getUserBean().getId(),time,time);

		retMap.put("code", 3);
		retMap.put("message", "操作成功");
		return retMap;
	}
	private void setTaskComplete(String taskId){
		String time=Utils.getTime();
		Map<String,Object> task=this.jdbcTemplate.queryForMap("select * from tlingx_wf_instance_task where id=?",taskId);
		String taskIds=this.jdbcTemplate.queryForObject("select task_id from tlingx_wf_instance where id=? ",String.class,task.get("instance_id"));
		String array[]=taskIds.split(",");
		for(String s:array){
			if(!taskId.equals(s)){
				this.jdbcTemplate.update("update tlingx_wf_instance_task set status=?,etime=?,modify_time=? where id=?",3,time,time,s);
			}
		}
	}
	/**
	 * 1全部通过
	 * 2全部不通过
	 * 3都有
	 * @param taskId
	 * @return
	 */
	public int sign(Object taskId){
		String sourceIds=this.getTaskIdsFormTaskId(taskId);
		String array[]=sourceIds.split(",");
		int total=0,yes=0,no=0,ret;
		for(String temp:array){
			if(Utils.isNull(temp))continue;
			total++;
			ret=this.jdbcTemplate.queryForInt("select ret_code from tlingx_wf_instance_task where id=? ",temp);
			if(ret==1||ret==3){//已阅算是同意
				yes++;
			}else{
				no++;
			}
		}
		
		if(total==yes){
			ret=1;
		}else if(total==no){
			ret=2;
		}else{
			ret=3;
		}
		return ret;
	}
	/**
	 * 1、全为同意
	 * 2、全为不同意
	 * 3、有同意
	 * 4、有不同意
	 * 5、其他
	 * @param taskId
	 * @return
	 */
	public int signCurrent(Object taskId){
		String sourceIds=this.jdbcTemplate.queryForObject("select task_id from tlingx_wf_instance where id=(select instance_id from tlingx_wf_instance_task where id=?) ",String.class,taskId);
		String array[]=sourceIds.split(",");
		int total=0,yes=0,no=0,db=0,ret;
		for(String temp:array){
			if(Utils.isNull(temp))continue;
			total++;
			try {
				ret=this.jdbcTemplate.queryForInt("select ret_code from tlingx_wf_instance_task where id=?",temp);
			} catch (DataAccessException e) {
				continue;//有些会签还没处理，取不到意见
			}
			if(ret==1||ret==3){//已阅算是同意
				yes++;
			}else if(ret==2){
				no++;
			}else {
				db++;//待办
			}
		}
		
		if(db>0){
			return 6;//未处理完
		}
		
		if(total==yes){
			ret=1;
		}else if(total==no){
			ret=2;
		}else if(yes>0&&no==0){
			ret=3;
		}else if(no>0){
			ret=4;
		}else{
			ret=5;
		}
		return ret;
		
	}
	
	private String getTaskIdsFormTaskId(Object taskId){
		Map<String,Object> task=this.jdbcTemplate.queryForMap("select * from tlingx_wf_instance_task where id=?",taskId);
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select source_id from tlingx_wf_define_line where target_id =?",task.get("task_id"));
		StringBuilder sb=new StringBuilder();
		for(Map<String,Object> map:list){
			sb.append(this.jdbcTemplate.queryForObject("select id from tlingx_wf_instance_task where instance_id=? and task_id =? order by create_time desc limit 1 ",String.class,task.get("instance_id"),map.get("source_id"))).append(",");
		}
		if(sb.length()>0)sb=sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
	@Override
	public String view(String taskId, IWorkflow workflow, IContext context,
			IPerformer performer) {
		Map<String,Object> task=this.jdbcTemplate.queryForMap("select * from tlingx_wf_instance_task where id=?",taskId);
		Map<String,Object> defineTask=this.jdbcTemplate.queryForMap("select * from tlingx_wf_define_task where id=?",task.get("task_id"));
		Map<String,Object> instance=this.jdbcTemplate.queryForMap("select * from tlingx_wf_instance where id=?",task.get("instance_id"));
		Map<String,Object> define=this.jdbcTemplate.queryForMap("select * from tlingx_wf_define where id=?",instance.get("define_id"));
		
		String content=this.jdbcTemplate.queryForObject("select content from tlingx_wf_define_form where id=(select form_id from tlingx_wf_define_task where id=(select task_id from tlingx_wf_instance_task where id=?))", String.class,taskId);
		workflow.setInstanceId(task.get("instance_id").toString());
		content=this.formService.formWorkflowRendering(content, workflow.getInstanceId(), context);
		context.getRequest().setAttribute("content", content);
		List<Map<String,Object>> list=this.getParams(workflow.getInstanceId());
		context.getRequest().setAttribute("task", task);
		context.getRequest().setAttribute("task_id", taskId);
		context.getRequest().setAttribute("auth_tag", defineTask.get("auth_tag"));
		context.getRequest().setAttribute("task_type", defineTask.get("type"));
		context.getRequest().setAttribute("auth_type", defineTask.get("auth"));
		context.getRequest().setAttribute("define_type", define.get("type"));
		context.getRequest().setAttribute("params", list);
		context.getRequest().setAttribute("params_json", JSON.toJSONString(list));
		context.getRequest().setAttribute("instance_id", workflow.getInstanceId());
		context.getRequest().setAttribute("define_task_id", task.get("task_id"));
		context.getRequest().setAttribute("history", this.jdbcTemplate.queryForList("select t.*,a.name user_name from tlingx_wf_instance_task t,tlingx_user a  where t.user_id=a.id and instance_id=? order by stime asc,etime asc",workflow.getInstanceId()));
		context.getRequest().setAttribute("attachment", this.jdbcTemplate.queryForList("select t.*,a.name user_name from tlingx_wf_instance_attachment t,tlingx_user a where t.user_id=a.id and instance_id=? order by create_time desc",workflow.getInstanceId()));
		context.getRequest().setAttribute("opinion", JSON.toJSONString(this.jdbcTemplate.queryForList("select t.content,t.create_time,t.ret,a.name user_name from tlingx_wf_instance_opinion t,tlingx_user a  where t.user_id=a.id and instance_id=? order by create_time asc",workflow.getInstanceId())));
		
		context.getRequest().setAttribute("model", "view");
		return this.pageService.getPage(Page.PAGE_WF_FORM);
	}
	@Override
	public String cc(Object taskId, String userIds) {
		Map<String,Object> instance=this.jdbcTemplate.queryForMap("select * from tlingx_wf_instance where id=(select instance_id from tlingx_wf_instance_task where id=?)",taskId);
		String array[]=userIds.split(",");
		String time=Utils.getTime();
		int c1=0,c2=0;
		for(String userId:array){
			if(Utils.isNull(userId))continue;
			if(this.jdbcTemplate.queryForInt("select count(*) from tlingx_wf_instance_task where instance_id=? and user_id=? and status=5",instance.get("id"),userId)==0){
				 c1=c1+this.jdbcTemplate.update("insert into tlingx_wf_instance_task(id,name,instance_id,task_id,user_id,status,stime,etime,create_time,modify_time) values(uuid(),?,?,?,?,?,?,?,?,?)",
					"抄送任务",instance.get("id"),"",userId,5,time,time,time,time);
			}else{
				c2++;
			}
		}
		String ret="";
		if(c2==0){
			ret="成功抄送了"+c1+"份任务";
		}else if(c1==0){
			ret="已抄送，不可再抄送。";
		}else{
			ret="成功抄送了"+c1+"份；"+c2+"份抄送失败，原因：已抄送。";
		}
		return ret;
	}
	@Scheduled(cron="0 0 4 * * ?")
	public void deleteWorkflowJob(){
		//System.out.println("执行删除初始化流程");
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select id from tlingx_wf_instance where status=3");
		for(Map<String,Object> map:list){
			this.jdbcTemplate.update("delete from tlingx_wf_instance_opinion where instance_id=?",map.get("id"));
			this.jdbcTemplate.update("delete from tlingx_wf_instance_value where instance_id=?",map.get("id"));
			this.jdbcTemplate.update("delete from tlingx_wf_instance_task where instance_id=?",map.get("id"));
			this.jdbcTemplate.update("delete from tlingx_wf_instance where id=?",map.get("id"));
		}
	}
	@Override
	public Map<String, Object> quickCreate(String defineId,Map<String, Object> params,IContext context,IPerformer performer) {
		Map<String, Object> ret=new HashMap<String, Object>();
		String taskId=this.createInstance(defineId, context, performer);
		String instanceId=(this.jdbcTemplate.queryForObject("select instance_id from tlingx_wf_instance_task where id=?", String.class,taskId));
		String attachments="",time=Utils.getTime();
		if(params.containsKey("attachments")){
			attachments=params.get("attachments").toString();
			params.remove("attachments");
			List<Map<String,Object>> attList=(List<Map<String,Object>>)JSON.parse(attachments);
			for(Map<String,Object> map:attList){
				this.jdbcTemplate.update("insert into tlingx_wf_instance_attachment(id,instance_id,name,path,length,user_id,create_time,modify_time) values(uuid(),?,?,?,?,?,?,?)",
						instanceId,map.get("name"),map.get("path"),map.get("length"),context.getUserBean().getId(),time,time);
			}
		}
		for(String key:params.keySet()){
			this.taskService.addParam(key, params.get(key).toString(), instanceId);
		}
		this.submitTask(taskId, context, performer);
		
		ret.put("instanceId", instanceId);
		//取出当前任务ID
		taskId=this.jdbcTemplate.queryForObject("select task_id from tlingx_wf_instance where id=?", String.class,instanceId);
		Map<String,Object> task=this.jdbcTemplate.queryForMap("select user_id,name from tlingx_wf_instance_task where id=?",taskId);
		if(task.get("user_id")!=null&&!"".equals(task.get("user_id").toString())){
			String userName=this.jdbcTemplate.queryForObject("select name from tlingx_user where id=?", String.class,task.get("user_id"));
			ret.put("code", 1);
			ret.put("userName", userName);
			ret.put("message", "任务提交成功，下一处理人:"+userName+"。");
		}else{
			ret.put("code", 3);//下一任务没有处理人
			ret.put("message", "下一任务没有处理人");
			ret.put("needUser", true);
		}
		ret.put("taskId", taskId);
		return ret;
	}
	public Map<String, Object> quickSubmit(String taskId,IContext context,IPerformer performer){
		Map<String, Object> ret=new HashMap<String, Object>();
		Map<String,Object> map=(this.jdbcTemplate.queryForMap("select instance_id,user_id from tlingx_wf_instance_task where id=?",taskId));
		if(map.get("user_id")==null||!context.getUserBean().getId().equals(map.get("user_id").toString())){
			ret.put("code", -1);
			ret.put("message", "该任务暂不能提交，或已提交！");
			return ret;
		}
		String instanceId=map.get("instance_id").toString();
		
		this.submitTask(taskId, context, performer);

		taskId=this.jdbcTemplate.queryForObject("select task_id from tlingx_wf_instance where id=?", String.class,instanceId);
		Map<String,Object> task=this.jdbcTemplate.queryForMap("select user_id,name from tlingx_wf_instance_task where id=?",taskId);
		if(task.get("user_id")!=null&&!"".equals(task.get("user_id").toString())){
			String userName=this.jdbcTemplate.queryForObject("select name from tlingx_user where id=?", String.class,task.get("user_id"));
			ret.put("code", 1);
			ret.put("userName", userName);
			ret.put("message", "任务提交成功，下一处理人:"+userName+"。");
		}else{
			ret.put("code", 3);//下一任务没有处理人
			ret.put("message", "下一任务没有处理人，请指定。");
			ret.put("needUser", true);
		}
		ret.put("taskId", taskId);
		return ret;
	}
	public Map<String,Object> quickCC(String taskId,String userIds){
		Map<String, Object> ret=new HashMap<String, Object>();
		String msg=this.cc(taskId, userIds);
		ret.put("code", 1);
		ret.put("message", msg);
		
		return ret;
	}
	@Override
	public boolean quickSetApprover(String taskId,String userId,IContext context){
		String sql="update tlingx_wf_instance_task set user_id=?,status=2,stime=? where id=?";
		String instIdSql="select instance_id from tlingx_wf_instance_task where id=?";
		if(userId.indexOf(",")==-1){
			this.jdbcTemplate.update(sql,userId,Utils.getTime(),taskId);
			String title=this.getParams(this.jdbcTemplate.queryForObject(instIdSql, String.class,taskId), "_TITLE");
			SpringContext.getApplicationContext().publishEvent(new ClaimEvent(userId,taskId,title,context,this));
		}else{
			String array[]=userId.split(",");
			StringBuilder ids=new StringBuilder();
			String newId;
			for(String uid:array){
				newId=this.taskService.copyTaskInstance(taskId);
				ids.append(newId).append(",");
				this.jdbcTemplate.update(sql,uid,Utils.getTime(),newId);
			}
			Map<String,Object> taskInst=this.jdbcTemplate.queryForMap("select * from tlingx_wf_instance_task where id=?",taskId);
			this.jdbcTemplate.update("delete from tlingx_wf_instance_task where id=?",taskId);
			
			if(ids.length()>0){ids.deleteCharAt(ids.length()-1);}
			this.jdbcTemplate.update("update tlingx_wf_instance set task_id=? where id=?",ids,taskInst.get("instance_id"));
		}
		return true;
	}
	
	@Override
	public Map<String, Object> quickApprove(String taskId,Map<String, String> params,IContext context,IPerformer performer) {
		return null;
	}
	@Override
	public String output(String id, boolean isForm) {
		Map<String,Object> all=new HashMap<String,Object>();
		Map<String,Object> define=this.jdbcTemplate.queryForMap("select * from tlingx_wf_define where id=?",id);
		Map<String,Object> form=this.jdbcTemplate.queryForMap("select * from tlingx_wf_define_form where id=?",define.get("form_id"));
		List<Map<String,Object>> listTask=this.jdbcTemplate.queryForList("select * from tlingx_wf_define_task where define_id=?",id);
		List<Map<String,Object>> listLine=this.jdbcTemplate.queryForList("select * from tlingx_wf_define_line where define_id=?",id);
		
		all.put("define", define);
		all.put("form", form);
		all.put("tasks", listTask);
		all.put("lines", listLine);
		return JSON.toJSONString(all);
	}
	@Override
	public boolean input(String code,String path,String basePath,String type,String appid) {
		try {
			String time=Utils.getTime();
			String formId=this.lingxService.uuid();
			String defineId=this.lingxService.uuid();
			String json=FileUtils.readFileToString(new File(basePath+"/"+path), "UTF-8");
			Map<String,Object> all=JSON.parseObject(json);
			Map<String,Object> define=(Map<String,Object>)all.get("define");
			Map<String,Object> form=(Map<String,Object>)all.get("form");
			List<Map<String,Object>> tasks=(List<Map<String,Object>>)all.get("tasks");
			List<Map<String,Object>> lines=(List<Map<String,Object>>)all.get("lines");
			
			String sql="insert into tlingx_wf_define_form(id,define_id,code,name,type,path,content,content2,content3,attach_html,app_id,create_time,modify_time)values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
			this.jdbcTemplate.update(sql,formId,"none",code,form.get("name"),form.get("type"),form.get("path"),form.get("content"),form.get("content2"),form.get("content3"),form.get("attach_html"),appid,time,time);
			sql="insert into tlingx_wf_define(id,code,name,type,status,user_id,remark,user_ids,org_ids,role_ids,create_time,modify_time,form_id,app_id) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			this.jdbcTemplate.update(sql,defineId,code,define.get("name"),define.get("type"),define.get("status"),define.get("user_id")
					,define.get("remark"),define.get("user_ids"),define.get("org_ids"),define.get("role_ids"),time,time,formId,appid);
		
			sql="insert into tlingx_wf_define_task(id,define_id,name,content,type,auth,user_ids,org_ids,role_ids,form_id,create_time,modify_time,top,`left`,width,height,expire,is_remind,auth_tag,script_before,script_after,content_type,sign_ret)"
					+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			for(Map<String,Object> task:tasks){
				this.jdbcTemplate.update(sql,task.get("id"),defineId,task.get("name"),task.get("content"),task.get("type"),task.get("auth"),task.get("user_ids"),task.get("org_ids"),task.get("role_ids"),formId,task.get("create_time"),task.get("modify_time"),task.get("top"),task.get("left"),task.get("width"),task.get("height"),task.get("expire"),task.get("is_remind"),task.get("auth_tag"),task.get("script_before"),task.get("script_after"),task.get("content_type"),task.get("sign_ret"));
			}
			
			sql="insert into tlingx_wf_define_line(id,name,define_id,source_id,target_id,content,top1,left1,top2,left2,source_point,target_point,is_auto,type)"
			+"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			for(Map<String,Object> line:lines){
				this.jdbcTemplate.update(sql,line.get("id"),line.get("name"),defineId,line.get("source_id"),line.get("target_id"),line.get("content"),line.get("top1"),line.get("left1"),line.get("top2"),line.get("left2"),line.get("source_point"),line.get("target_point"),line.get("is_auto"),line.get("type"));
			}
			
			sql="insert into tlingx_wf_define_category(id,define_id,category_id)values(?,?,?)";
			String arr[]=type.split(",");
			for(String s:arr){
				this.jdbcTemplate.update(sql,this.lingxService.uuid(),defineId,s);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	@Override
	public Map<String, Object> quickGetParams(String taskId) {
		Map<String, Object> ret=new HashMap<String,Object>();
		String instanceId=this.jdbcTemplate.queryForObject("select instance_id from tlingx_wf_instance_task where id=?", String.class,taskId);
		List<Map<String,Object>> list=this.getParams(instanceId);
		for(Map<String,Object> map:list){
			ret.put(map.get("name").toString(), map.get("value"));
		}
		return ret;
	}
	@Override
	public List<Map<String,Object>> quickGetHistory(String taskId) {
		String instanceId=this.jdbcTemplate.queryForObject("select instance_id from tlingx_wf_instance_task where id=?", String.class,taskId);
		
		List<Map<String,Object>> list= this.jdbcTemplate.queryForList("select t.*,a.name user_name,b.content from tlingx_user a ,(tlingx_wf_instance_task t LEFT JOIN tlingx_wf_instance_opinion b ON t.id=b.task_id) where t.user_id=a.id and t.instance_id=? order by stime asc,etime asc",instanceId);
		
		return list;
	}
	@Override
	public List<Map<String, Object>> quickGetAttachment(String taskId) {
		String instanceId=this.jdbcTemplate.queryForObject("select instance_id from tlingx_wf_instance_task where id=?", String.class,taskId);
		List<Map<String,Object>> list= this.jdbcTemplate.queryForList("select name,path,length from tlingx_wf_instance_attachment where instance_id=? order by create_time asc",instanceId);
		return list;
	}
	@Override
	public boolean push(String instanceId, String paramName, String paramValue) {
		this.taskService.addParam(paramName, paramValue, instanceId);
		return true;
	}
	@Override
	public boolean quickSaveComment(String taskId, String comment,String userId) {
		String content=comment,time=Utils.getTime();
		Map<String,Object> task=this.jdbcTemplate.queryForMap("select * from tlingx_wf_instance_task where id=?",taskId);
		//评论不存任务节点 2016-04-28
		String sql="insert into tlingx_wf_instance_opinion(id,instance_id,task_id,fid,content,ret,user_id,create_time,modify_time) values(uuid(),?,?,?,?,?,?,?,?)";
		this.jdbcTemplate.update(sql,task.get("instance_id"),"","0",content,0,userId,time,time);

		return true;
	}
	@Override
	public String page(String page) {
		return this.page(this.pageService.getPage(page));
	}
	@Override
	public Map<String, Object> delete(String taskId, IWorkflow workflow,
			IContext context, IPerformer performer) {
		String instanceId=this.jdbcTemplate.queryForObject("select instance_id from tlingx_wf_instance_task where id=?", String.class,taskId);
		this.jdbcTemplate.update("delete from tlingx_wf_instance where id=?",instanceId);
		Map<String, Object> ret=new HashMap<String, Object>();
		ret.put("code", 3);
		ret.put("message", "删除成功");
		
		return ret;
	}
	@Override
	public Map<String, Object> isvalid(String taskId, IWorkflow workflow,
			IContext context, IPerformer performer) {
		Map<String, Object> ret=new HashMap<String, Object>();
		ret.put("code", 1);
		ret.put("message","1");//为1的话为有效，不为1则无效，并显示该消息
		try {
			Map<String,Object> task=this.jdbcTemplate.queryForMap("select * from tlingx_wf_instance_task where id=?",taskId);
			Map<String,Object> defineTask=this.jdbcTemplate.queryForMap("select * from tlingx_wf_define_task where id=?",task.get("task_id"));
			String script=defineTask.get("script_validation").toString();
			if(Utils.isNotNull(script)){
				Object obj=this.taskService.script(script, taskId, context, performer);
				ret.put("message", obj.toString());//为1的话为有效，不为1则无效，并显示该消息
			}
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			return ret;
		}
	}
	
	
}
