package com.lingx.plugin.workflow.remind;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.context.ApplicationListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.lingx.core.plugin.IPlugin;
import com.lingx.core.service.ILingxService;
import com.lingx.core.workflow.event.ClaimEvent;
@Component(value="lingxWorkflowRemindService")
public class WorkflowRemindServiceImpl implements IWorkflowRemindService, ApplicationListener<ClaimEvent>  {
	@Resource
	private ILingxService lingxService;
	@Resource
	private JdbcTemplate jdbcTemplate;
	private String title,body;
	private boolean enable;
	@Override
	public void onApplicationEvent(ClaimEvent event) {
		if(this.enable){
			if(event.getUserId().equals(event.getContext().getUserBean().getId())){
				return ;//当签收人等于当前操作人时，不用提醒
			}
			try {
				IPlugin send=this.lingxService.getPlugin("send-mail-plugin");
				Map<String,Object> param=new HashMap<String,Object>();
				param.put("to", this.getEmailByUserId(event.getUserId()));
				param.put("title", this.title.replace("{0}", event.getTitle()));
				param.put("body", this.body.replace("{0}", event.getTitle()));
				send.execute(param);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public boolean isEnable() {
		return enable;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	public String[] getEmailByUserId(String userid) {
		userid=userid.replace(",", "','");
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select email from tlingx_user where email is not null and id in('"+userid+"')");
		Set<String> set=new HashSet<String>();
		for(Map<String,Object> map:list){
			if(!"".equals(map.get("email").toString())){
				set.add(map.get("email").toString());
			}
		}
		String arr[]=new String[set.size()];
		set.toArray(arr);
		return arr;
	}

}
