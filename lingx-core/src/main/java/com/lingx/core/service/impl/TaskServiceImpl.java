package com.lingx.core.service.impl;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.lingx.core.SpringContext;
import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.exception.LingxScriptException;
import com.lingx.core.service.ILingxService;
import com.lingx.core.service.IPageService;
import com.lingx.core.service.ITaskService;
import com.lingx.core.utils.Utils;
import com.lingx.core.workflow.event.ClaimEvent;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年6月30日 下午2:25:51 
 */
@Component(value="lingxTaskService")
public class TaskServiceImpl implements ITaskService {
	public static final String NOT_SAVE_PARAMNAME=",_CURRENT_TASK_ID,_,";
	@Resource(name="jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	@Resource
	private ILingxService lingxService;
	@Resource
	private IPageService pageService;
	@Override
	public String[] create(String defineTaskId, String instanceId,IContext context,IPerformer performer) {
		String instanceTaskId=this.lingxService.uuid();
		String time=Utils.getTime();
		Map<String,Object> defineTask=this.jdbcTemplate.queryForMap("select * from tlingx_wf_define_task where id=?",defineTaskId);
		int type=Integer.parseInt(defineTask.get("type").toString());
		String users[]=null;
		String rets[]=null;
		StringBuilder sb=new StringBuilder();
		switch(type){
		case 1://开始
		case 2://结束
			time=MessageFormat.format("{0,date,yyyyMMddHHmmss}",new Object[] { new java.sql.Date(System.currentTimeMillis()-1000) });//因为排序问题，所以减1秒
			users=new String[]{getValue("_USER_ID",instanceId)};
			this.jdbcTemplate.update("insert into tlingx_wf_instance_task(id,name,instance_id,task_id,user_id,status,stime,etime,create_time,modify_time) values(?,?,?,?,?,?,?,?,?,?)",
					instanceTaskId,defineTask.get("name"),instanceId,defineTaskId,"",1,"","",time,time);
			this.jdbcTemplate.update("update tlingx_wf_instance_value set value=? where instance_id=? and name=?",instanceTaskId,instanceId,"_TASK_ID");
			
			this.claim(instanceTaskId, users[0], context, performer);
			sb.append(instanceTaskId).append(",");
			break;
		case 3://人工处理
			users=this.getCailmUserIds(defineTaskId,instanceId, context, performer);//取出节点处理人
			users=delegateConfig(users,defineTask.get("define_id").toString());//委托人替换
			
			this.jdbcTemplate.update("insert into tlingx_wf_instance_task(id,name,instance_id,task_id,user_id,status,stime,etime,create_time,modify_time) values(?,?,?,?,?,?,?,?,?,?)",
					instanceTaskId,defineTask.get("name"),instanceId,defineTaskId,"",1,"","",time,time);
			this.jdbcTemplate.update("update tlingx_wf_instance_value set value=? where instance_id=? and name=?",instanceTaskId,instanceId,"_TASK_ID");
			if(users.length>0)
			if(users.length==1){//一个人的时候直接签收
				this.claim(instanceTaskId, users[0], context, performer);
			}else{
				int auth=Integer.parseInt(defineTask.get("auth").toString());
				switch(auth){
				case 1://串行处理
					//多个人的时候，进入待领
					StringBuilder userIds=new StringBuilder();
					for(String id:users){
						userIds.append(id).append(",");
					}
					userIds=userIds.deleteCharAt(userIds.length()-1);
					this.jdbcTemplate.update("update tlingx_wf_instance_task set user_id=? where id=?",userIds.toString(),instanceTaskId);

					break;
				case 4://并行处理
					String oldTaskId=instanceTaskId;
					for(String user:users){
						instanceTaskId=this.lingxService.uuid();
						this.jdbcTemplate.update("insert into tlingx_wf_instance_task(id,name,instance_id,task_id,user_id,status,stime,etime,create_time,modify_time) values(?,?,?,?,?,?,?,?,?,?)",
								instanceTaskId,defineTask.get("name"),instanceId,defineTaskId,"",1,"","",time,time);
						this.claim(instanceTaskId, user, context, performer);
						sb.append(instanceTaskId).append(",");
						
					}
					this.jdbcTemplate.update("delete from tlingx_wf_instance_task where id=?",oldTaskId);
					break;
				}
			}
			sb.append(instanceTaskId).append(",");
			break;
		case 4://自动处理
		case 6://子流程
			users=new String[]{getValue("_USER_ID",instanceId)};
			this.jdbcTemplate.update("insert into tlingx_wf_instance_task(id,name,instance_id,task_id,user_id,status,stime,etime,create_time,modify_time) values(?,?,?,?,?,?,?,?,?,?)",
					instanceTaskId,defineTask.get("name"),instanceId,defineTaskId,"",1,"","",time,time);
			this.jdbcTemplate.update("update tlingx_wf_instance_value set value=? where instance_id=? and name=?",instanceTaskId,instanceId,"_TASK_ID");
			if(users.length==1){
				this.claim(instanceTaskId, users[0], context, performer);
			}
			sb.append(instanceTaskId).append(",");
			break;
		case 5://会签任务

			users=this.getCailmUserIds(defineTaskId,instanceId, context, performer);//取出节点处理人
			users=delegateConfig(users,defineTask.get("define_id").toString());//委托人替换
			
			for(String user:users){
				instanceTaskId=this.lingxService.uuid();
				this.jdbcTemplate.update("insert into tlingx_wf_instance_task(id,name,instance_id,task_id,user_id,status,stime,etime,create_time,modify_time) values(?,?,?,?,?,?,?,?,?,?)",
						instanceTaskId,defineTask.get("name"),instanceId,defineTaskId,"",1,"","",time,time);
				this.claim(instanceTaskId, user, context, performer);
				sb.append(instanceTaskId).append(",");
				
			}
			break;
		
		}
		if(sb.length()>0)sb.deleteCharAt(sb.length()-1);
		this.jdbcTemplate.update("update tlingx_wf_instance_value set value=? where instance_id=? and name=?",sb.toString(),instanceId,"_TASK_ID");
		this.jdbcTemplate.update("update tlingx_wf_instance set task_id=? where id=?",sb.toString(),instanceId);
		rets=sb.toString().split(",");
		
		//前置脚本处理
		{
			for(String taskId:rets){
			Map<String,Object> task=this.jdbcTemplate.queryForMap("select * from tlingx_wf_instance_task where id=?",taskId);
			Map<String,Object> taskDefine=this.jdbcTemplate.queryForMap("select * from tlingx_wf_define_task where id=?",task.get("task_id"));
			if(taskDefine.get("script_before")!=null){
				try {
					String temp=taskDefine.get("script_before").toString();
					if("".equals(temp))continue;
					this.script(temp, taskId,context, performer);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					this.log(task.get("instance_id").toString(), taskId, e.getLocalizedMessage(), context);
				}
			}
			}
		}
		return rets;
	}
	private String getValue(String paramName,String instanceId){
		String tmp="";
		try {
			tmp=this.jdbcTemplate.queryForObject("select value from tlingx_wf_instance_value where instance_id=? and name=?", String.class,instanceId,paramName);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return tmp;
	}
	@Override
	public String[] create(String[] defineTaskIds, String instanceId,IContext context,IPerformer performer) {
		StringBuilder sb=new StringBuilder();
		for(String s:defineTaskIds){
			if(Utils.isNull(s))continue;
			String[] ids=this.create(s, instanceId,context,performer);
			for(String id:ids){
				sb.append(id).append(",");
			}
		}
		if(sb.length()>0){
			sb.deleteCharAt(sb.length()-1);
			this.jdbcTemplate.update("update tlingx_wf_instance set task_id=? where id=?",sb.toString(),instanceId);
		}
		return sb.toString().split(",");
	}

	@Override
	public String claim(String instanceTaskId, String userId,IContext context,IPerformer performer) {
		String time=Utils.getTime();
		this.jdbcTemplate.update("update tlingx_wf_instance_task set user_id=?,stime=?,status=2 where id=?",userId,time,instanceTaskId);
		
		String instanceId=this.jdbcTemplate.queryForObject("select instance_id from tlingx_wf_instance_task where id=?", String.class,instanceTaskId);
		String title=this.jdbcTemplate.queryForObject("select name from tlingx_wf_instance where id=?", String.class,instanceId);

		Map<String,Object> taskDefine=this.jdbcTemplate.queryForMap("select * from tlingx_wf_define_task where id=(select task_id from tlingx_wf_instance_task where id=?)",instanceTaskId);
		
		if(taskDefine.get("is_remind")!=null&&"1".equals(taskDefine.get("is_remind").toString())){
			//有提醒时再触发
			SpringContext.getApplicationContext().publishEvent(new ClaimEvent(userId,instanceTaskId,title,context,this));
		}
		
		String saveEventScript=this.jdbcTemplate.queryForObject("select event_save from tlingx_wf_define where id in(select define_id from tlingx_wf_instance where id=? )", String.class,instanceId);
		this.addParam("_TASK_ID", instanceTaskId, instanceId);
		if(Utils.isNotNull(saveEventScript)){
			try {
				//performer.script(saveEventScript, context);
				this.scriptByInstanceId(saveEventScript, instanceId, context, performer);
			} catch (Exception e) {
				this.log(instanceId, instanceTaskId, "保存事件异常："+e.getLocalizedMessage(), context);
			}
		}
		return null;
	}

	@Override
	public String save(String instanceTaskId,IContext context,IPerformer performer) {
		if(instanceTaskId.indexOf(",")!=-1)instanceTaskId=instanceTaskId.split(",")[0];
		String instanceId=this.jdbcTemplate.queryForObject("select instance_id from tlingx_wf_instance_task where id=?", String.class,instanceTaskId);
		this.jdbcTemplate.update("update tlingx_wf_instance set status=1 where id=? and status=?",instanceId,3);//点击保存，自动将初始化转为活动
		this.log(instanceId, instanceTaskId, "保存表单数据", context);
		for(String key:context.getRequest().getParameterNames()){
			this.addParam(key, context.getRequest().getParameter(key), instanceId);
		}
		String saveEventScript=this.jdbcTemplate.queryForObject("select event_save from tlingx_wf_define where id in(select define_id from tlingx_wf_instance where id=? )", String.class,instanceId);
		if(Utils.isNotNull(saveEventScript)){
			try {
				this.scriptByInstanceId(saveEventScript, instanceId, context, performer);
			} catch (Exception e) {
				this.log(instanceId, instanceTaskId, "保存事件异常："+e.getLocalizedMessage(), context);
			}
		}
		return null;
	}
	public void addParam(String paramName,String paramValue,String instanceId){
		if(NOT_SAVE_PARAMNAME.indexOf(","+paramName+",")!=-1)return;
		int c=this.jdbcTemplate.update("update tlingx_wf_instance_value set value=? where instance_id=? and name=?",paramValue,instanceId,paramName);
		if(c==0)
		this.jdbcTemplate.update("insert into tlingx_wf_instance_value(id,instance_id,name,value) values(uuid(),?,?,?)",instanceId,paramName,paramValue);
	}
	@Override
	public String execute(String instanceTaskId,IContext context,IPerformer performer) {
		String content=this.jdbcTemplate.queryForObject("select t.content from tlingx_wf_define_task t,tlingx_wf_instance_task a where t.id=a.task_id and a.id=?", String.class,instanceTaskId);
		if(Utils.isNotNull(content))
		this.script(content, instanceTaskId, context, performer);
		return null;
	}
	private String getTaskIdsFormTaskId(Object taskId){
		Map<String,Object> task=this.jdbcTemplate.queryForMap("select * from tlingx_wf_instance_task where id=?",taskId);
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select source_id from tlingx_wf_define_line where target_id =? and type=1",task.get("task_id"));
		StringBuilder sb=new StringBuilder();
		for(Map<String,Object> map:list){
			sb.append(this.jdbcTemplate.queryForObject("select id from tlingx_wf_instance_task where instance_id=? and task_id =? order by create_time desc limit 1 ",String.class,task.get("instance_id"),map.get("source_id"))).append(",");
		}
		if(sb.length()>0)sb=sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
	@Override
	public String fallback(String instanceTaskId,IContext context,IPerformer performer) {
		String time=Utils.getTime();
		this.jdbcTemplate.update("update tlingx_wf_instance_task set etime=?,status=4 where id=?",time,instanceTaskId);
		String targetTaskId=null;//context.getRequest().getParameter("target_task_id");
		String array[]=this.prevTaskDefineIds(instanceTaskId, context, performer);
		switch(array.length){
		case 0:
			String temp=this.getTaskIdsFormTaskId(instanceTaskId);
			//默认是取上一节点，目前没有考虑多节点
			targetTaskId=this.jdbcTemplate.queryForObject("select task_id from tlingx_wf_instance_task where id=?", String.class,temp);
			break;
		case 1:
			targetTaskId=array[0];
			break;
		default:
			targetTaskId=array[0];
			
		}
		String instanceId=this.jdbcTemplate.queryForObject("select instance_id from tlingx_wf_instance_task where id=?", String.class,instanceTaskId);
		
		array=this.create(targetTaskId, instanceId, context, performer);
		

		this.log(instanceId, instanceTaskId, "任务回退", context);
/*
		instanceTaskId=this.lingxService.uuid();
		Map<String,Object> targetMap=this.jdbcTemplate.queryForMap("select * from tlingx_wf_instance_task where id=?",targetTaskId);
		
		Map<String,Object> defineTask=this.jdbcTemplate.queryForMap("select * from tlingx_wf_define_task where id=?",targetMap.get("task_id"));
		
		this.jdbcTemplate.update("insert into tlingx_wf_instance_task(id,name,instance_id,task_id,user_id,status,stime,etime,create_time,modify_time) values(?,?,?,?,?,?,?,?,?,?)",
				instanceTaskId,defineTask.get("name"),targetMap.get("instance_id"),targetMap.get("task_id"),"",1,"","",time,time);
		this.jdbcTemplate.update("update tlingx_wf_instance_value set value=? where instance_id=? and name=?",instanceTaskId,targetMap.get("instance_id"),"_TASK_ID");
		
		this.jdbcTemplate.update("update tlingx_wf_instance_task set user_id=?,stime=?,status=2 where id=?",targetMap.get("user_id"),time,instanceTaskId);
		
		this.jdbcTemplate.update("update tlingx_wf_instance set task_id=? where id=?",instanceTaskId,targetMap.get("instance_id"));
	*/	
		return array[0];
	}

	@Override
	public boolean submit(String instanceTaskId,IContext context,IPerformer performer) {
		Map<String,Object> currentTask=this.jdbcTemplate.queryForMap("select * from tlingx_wf_instance_task where id=?",instanceTaskId);
		Map<String,Object> defineTask=this.jdbcTemplate.queryForMap("select * from tlingx_wf_define_task where id=?",currentTask.get("task_id"));
		
		int c=this.jdbcTemplate.update("update tlingx_wf_instance_task set status=3,etime=? where id=?",Utils.getTime(),instanceTaskId);
		//System.out.println("update:"+instanceTaskId+","+c);
		
		switch(Integer.parseInt(defineTask.get("type").toString())){
		case 1://开始
			this.execute(instanceTaskId,context,performer);

			this.log(currentTask.get("instance_id").toString(), instanceTaskId, "启动流程", context);
			break;
		case 2://结束

			this.execute(instanceTaskId,context,performer);
			String time=Utils.getTime();
			this.jdbcTemplate.update("update tlingx_wf_instance_task set status=?,etime=?,modify_time=? where id=?",3,time,time,instanceTaskId);
			this.jdbcTemplate.update("update tlingx_wf_instance set status=?,modify_time=? where id=?",2,time,currentTask.get("instance_id"));
			context.addMessage("10032", "工作流程结束");

			this.log(currentTask.get("instance_id").toString(), instanceTaskId, "结束流程", context);
			break;
		case 3://手动
			this.save(instanceTaskId,context,performer);
			
			String currentTaskIds=this.jdbcTemplate.queryForObject("select task_id from tlingx_wf_instance where id=?", String.class,currentTask.get("instance_id"));
			//System.out.println(currentTaskIds);
			if(currentTaskIds.indexOf(",")!=-1){//当前是多任务时，需要所有任务都执行才能往下走
				String array[]=currentTaskIds.split(",");
				int total=0,compelete=0;
				for(String id:array){
					if(Utils.isNull(id))continue;
					total++;
					if(this.lingxService.queryForInt("select status from tlingx_wf_instance_task where id=?",id)==3){
						compelete++;
					}
					
				}
				this.log(currentTask.get("instance_id").toString(), instanceTaskId, "提交流程", context);
				//System.out.println(total+" vs "+compelete);
				if(total!=compelete){//总任务量等于完成任务量
					return false;
				}else{
					return true;
				}
			}

			break;
		case 4://自动，这里需要处理
			try {
				String temp=null;
				if(defineTask.get("content")!=null)temp=defineTask.get("content").toString();
				if(Utils.isNotNull(temp)){
					this.script(temp, instanceTaskId,context, performer);
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				context.addMessage("异常信息", e1.getLocalizedMessage());
				context.addMessage("102044", "脚本执行异常，请告知系统管理员");
				//return this.pageService.getPage(Page.PAGE_WF_ERROR);
			}

			this.log(currentTask.get("instance_id").toString(), instanceTaskId, "自动处理", context);
			//return true;
			break;
		case 5://会签任务
			 currentTaskIds=this.jdbcTemplate.queryForObject("select task_id from tlingx_wf_instance where id=?", String.class,currentTask.get("instance_id"));
			if(currentTaskIds.indexOf(",")!=-1){//当前是多任务时，需要所有任务都执行才能往下走
				String array[]=currentTaskIds.split(",");
				int total=0,compelete=0;
				for(String id:array){
					if(Utils.isNull(id))continue;
					total++;
					if(this.lingxService.queryForInt("select status from tlingx_wf_instance_task where id=?",id)==3){
						compelete++;
					}
					
				}

				this.log(currentTask.get("instance_id").toString(), instanceTaskId, String.format("会签流程，共%s，当前%s", total,compelete), context);
				if(total!=compelete){//总任务量等于完成任务量
					return false;
				}else{
					return true;
				}
			}
			break;
		case 6://子流程任务
			break;
		}
		
		return true;
	}

	@Override
	public String approve(String instanceTaskId, String content, String ret,IContext context,IPerformer performer) {
		String time=Utils.getTime();
		//String sql="insert into tlingx_wf_instance_opinion(id,instance_id,task_id,fid,content,ret,user_id,create_time,modify_time) values(uuid(),?,?,?,?,?,?,?,?)";
	//	if(Utils.isNotNull(content)){}
		
		//this.jdbcTemplate.update(sql,task.get("instance_id"),instanceTaskId,"0",content,ret,context.getUserBean().getId(),time,time);
		
		this.jdbcTemplate.update("update tlingx_wf_instance_task set status=?,etime=?,modify_time=?,ret_code=?,ret_message=? where id=?",3,time,time,ret,content,instanceTaskId);
		
		return null;
	}

	@Override
	public String getStartTaskDefineId(String defineId,IContext context,IPerformer performer) {
		return this.jdbcTemplate.queryForObject("select id from tlingx_wf_define_task where define_id=? and type=?", String.class,defineId,1);
	}
	
	private Set<String> getScriptUsers(int type,String script,String instanceId,IContext context,IPerformer performer){
		if(script==null||"".equals(script.trim()))return new HashSet<String>();
		Object obj=this.scriptByInstanceId(script, instanceId, context, performer);
		if(obj==null)return new HashSet<String>();
		String temp=obj.toString();
		Set<String> sets=null;
		switch(type){
		case 1:
			sets=getUserIdsByStr(temp);
			break;
		case 2:
			sets=getUserIdsByOrgIds(temp);
			break;
		case 3:
			sets=getUserIdsByRoleIds(temp);
			break;
		}
		return sets;
	}
	private Set<String> getUserIdsByStr(String str){
		String array[]=str.split(",");
		Set<String> sets=new HashSet<String>();
		for(String s:array){
			if(Utils.isNull(s))continue;
			sets.add(s);
		}
		return sets;
	}
	private Set<String> getUserIdsByOrgIds(String orgIds){
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select id from tlingx_user where id in(select user_id from tlingx_userorg where org_id in('"+orgIds.replaceAll(",", "','")+"'))");
		Set<String> sets=new HashSet<String>();
		for(Map<String,Object> map:list){
			sets.add(map.get("id").toString());
		}
		return sets;
	}
	private Set<String> getUserIdsByRoleIds(String roleIds){
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select id from tlingx_user where  id in(select user_id from tlingx_userrole where role_id in('"+roleIds.replaceAll(",", "','")+"'))");
		Set<String> sets=new HashSet<String>();
		for(Map<String,Object> map:list){
			sets.add(map.get("id").toString());
		}
		return sets;
	}
	@Override
	public String[] getCailmUserIds(String defineTaskId,String instanceId,IContext context,IPerformer performer) {
		Map<String,Object> defineTask=this.jdbcTemplate.queryForMap("select t.* from tlingx_wf_define_task t where t.id=?",defineTaskId);
		List<Set<String>> users=new ArrayList<Set<String>>();
		if (defineTask.get("user_id") != null) {
			return new String[] { defineTask.get("user_id").toString() };
		} else {
			int content_type = Integer.parseInt(defineTask.get("content_type")
					.toString());
			// 执行正则表达式，值为处理人ID
			if (defineTask.get("content") != null&& !"".equals(defineTask.get("content").toString().trim()))
				users.add(this.getScriptUsers(content_type,defineTask.get("content").toString(), instanceId,context, performer));
			// 其他情况
			if (defineTask.get("user_ids") != null&& !"".equals(defineTask.get("user_ids").toString().trim()))
				users.add(this.getUserIdsByStr(defineTask.get("user_ids").toString()));
			if (defineTask.get("org_ids") != null&& !"".equals(defineTask.get("org_ids").toString().trim()))
				users.add(this.getUserIdsByOrgIds(defineTask.get("org_ids").toString()));
			if (defineTask.get("role_ids") != null&& !"".equals(defineTask.get("role_ids").toString().trim()))
				users.add(this.getUserIdsByRoleIds(defineTask.get("role_ids").toString()));

			if(users.size()==0)return new String[0];
			Set<String> ret=new HashSet<String>();
			ret.addAll(users.get(0));
			for(int i=1;i<users.size();i++){
				ret.retainAll(users.get(i));
			}
			if(ret.size()==0){//取交集没有一个用户时，取并集
				for(int i=0;i<users.size();i++){
					ret.addAll(users.get(i));
				}
			}
			String array[]=new String[ret.size()];
			ret.toArray(array);
			return array;
		}
			
	}
	/**
	 * 委托设置处理
	 * @param array 用户ID
	 * @param defineId 流程定义ID
	 * @return
	 */
	public String[] delegateConfig(String array[],String defineId){
		String time=Utils.getTime();
		String sql="select target_user_id from tlingx_wf_delegate where define_id=? and source_user_id=? and stime<? and etime>? and status=1";
		List<Map<String,Object>> list=null;
		String newArray[]=new String[array.length];
		
		for(int i=0;i<array.length;i++){
			list=this.jdbcTemplate.queryForList(sql,defineId,array[i],time,time);
			if(list.size()==0){
				newArray[i]=array[i];
			}else{
				newArray[i]=list.get(0).get("target_user_id").toString();
			}
		}
		return newArray;
	}

	@Override
	public String[] nextTaskDefineIds(String instanceTaskId,IContext context,IPerformer performer) {
		List<Map<String,Object>> targetLines=this.jdbcTemplate.queryForList("select t.* from tlingx_wf_define_line t,tlingx_wf_instance_task a where t.source_id=a.task_id and t.type=1 and a.id=? ",instanceTaskId);
		StringBuilder sb=new StringBuilder();
		if(targetLines.size()==1){//当只有一条下线时，直接转下线
			sb.append(targetLines.get(0).get("target_id").toString()).append(",");
		}else if(targetLines.size()==0){
			return new String[0];
		}else{
			
			for(Map<String,Object> map:targetLines){
				try {
					boolean b=true;
					if(map.get("content")!=null&&Utils.isNotNull(map.get("content").toString()))
						b=(boolean)this.script(map.get("content").toString(), instanceTaskId,context, performer);
					if(b){
						sb.append(map.get("target_id").toString()).append(",");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if(sb.length()>0)sb.deleteCharAt(sb.length()-1);
		return sb.toString().split(",");
	}

	@Override
	public String[] prevTaskDefineIds(String instanceTaskId,IContext context,IPerformer performer) {
		List<Map<String,Object>> targetLines=this.jdbcTemplate.queryForList("select t.* from tlingx_wf_define_line t,tlingx_wf_instance_task a where t.source_id=a.task_id and t.type=2 and a.id=? ",instanceTaskId);
		StringBuilder sb=new StringBuilder();
		if(targetLines.size()==1){//当只有一条下线时，直接转下线
			sb.append(targetLines.get(0).get("target_id").toString()).append(",");
		}else if(targetLines.size()==0){
			return new String[0];
		}else{
			
			for(Map<String,Object> map:targetLines){
				try {
					boolean b=true;
					if(map.get("content")!=null&&Utils.isNotNull(map.get("content").toString()))
						b=(boolean)this.script(map.get("content").toString(), instanceTaskId,context, performer);
					if(b){
						sb.append(map.get("target_id").toString()).append(",");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if(sb.length()>0)sb.deleteCharAt(sb.length()-1);
		return sb.toString().split(",");
	}

	@Override
	public Object script(String script, String instanceTaskId,IContext context,IPerformer performer) {
		Object obj=this.scriptByInstanceId(script, this.jdbcTemplate.queryForObject("select instance_id from tlingx_wf_instance_task where id=?", String.class,instanceTaskId), context, performer);
		return obj;
	}

	@Override
	public Object scriptByInstanceId(String script, String instanceId,
			IContext context, IPerformer performer) {
		Date date=new Date();
		SimpleDateFormat sdf14=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf8=new SimpleDateFormat("yyyy-MM-dd");
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select name,value from tlingx_wf_instance_value where instance_id=?",instanceId);
		for(Map<String,Object> map:list){
			performer.addParam(map.get("name").toString(), map.get("value"));
		}
		performer.addParam("CDate",sdf8.format(date));
		performer.addParam("CDateTime",sdf14.format(date));
		performer.addParam(context);
		Object obj=null;
		try {
			obj=performer.script(script, context);
		} catch (LingxScriptException e) {
			e.printStackTrace();
		}
		return obj;
	}

	/**
	 * 
	 * @param instance
	 * @param task
	 * @param comment
	 * @param context
	 */
	public void log(String instance, String task,String comment,IContext context) {
		String  user=context.getUserBean().getId(),  ip=context.getRequest().getAttribute(IContext.CLIENT_IP).toString();
		this.jdbcTemplate.update("insert into tlingx_wf_instance_log(instance_id,task_id,user_id,ts,ip,comment) values(?,?,?,?,?,?)",
				instance,task,user,Utils.getTime(),ip,comment);
		
	}
	@Override
	public String copyTaskInstance(String taskId) {
		String newId=this.lingxService.uuid();
		String sql="insert into tlingx_wf_instance_task(id,name,instance_id,task_id,user_id,status,stime,etime,create_time,modify_time)"
		+"select ?,name,instance_id,task_id,user_id,status,stime,etime,create_time,modify_time from tlingx_wf_instance_task where id=?";
		this.jdbcTemplate.update(sql,newId,taskId);
		return newId;
	}
}
