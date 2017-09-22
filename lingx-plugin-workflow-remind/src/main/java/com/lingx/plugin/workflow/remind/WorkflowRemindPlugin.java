package com.lingx.plugin.workflow.remind;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.lingx.core.plugin.IPlugin;
@Component
public class WorkflowRemindPlugin implements IPlugin{

	@Resource
	private IWorkflowRemindService workflowRemindService;
	private boolean enable=true;
	@Override
	public String getId() {
		return "workflow-remind-plugin";
	}

	@Override
	public String getName() {
		return "流程操作提醒插件";
	}

	@Override
	public String getDetail() {
		return "由于该插件是用邮件提醒，所以该插件依赖于【电子邮件发送插件】。\n<br>\n<br>在流程流转时会对相应人进行邮件提醒。需要参数:title【邮件标题】,body【正文】，标题与正文中可用{0}表示为流程标题。\n<br>\n<br>注：邮件是发送到用户的邮件字段";
	}

	@Override
	public String getAuthor() {
		return "www.lingx.com";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public boolean isEnable() {
		return this.enable;
	}

	@Override
	public void setEnable(boolean enable) {
		this.enable=enable;
	}

	@Override
	public void init(Map<String, Object> params) {
		String title="", body="";
		try {
			if(params.get("title")==null){return;}
			title=params.get("title").toString();
			body=params.get("body").toString();
			this.workflowRemindService.setTitle(title);
			this.workflowRemindService.setBody(body);
			this.workflowRemindService.setEnable(enable);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void start() {
		
	}

	@Override
	public void stop() {
		
	}

	@Override
	public void destory() {
		
	}

	@Override
	public Map<String, Object> execute(Map<String, Object> params) {
		return null;
	}

}
