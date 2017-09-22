package com.lingx.plugin.email.send;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.lingx.core.plugin.IPlugin;
@Component
public class EmailSendPlugin implements IPlugin {
	private boolean enable;
	@Resource
	private IEmailSendService emailSendService;
	public String getId() {
		return "send-mail-plugin";
	}

	public String getName() {
		return "发送邮件插件";
	}

	public String getDetail() {
		return "可以发送文本与HTML两种消息，需要host,username,password参数；分别是发送主机、发送着邮箱、邮箱密码\n\n<br><br>在调用execute(),需要必须填title,body,to；其中title,body为String类型,to可以是String也可以是String[]。可选项type是String类型,值为text与html；默认为text";
	}

	public String getAuthor() {
		return "www.lingx.com";
	}

	public String getVersion() {
		return "1.0";
	}

	public boolean isEnable() {
		return this.enable;
	}

	public void setEnable(boolean enable) {
		this.enable=enable;
	}

	public void init(Map<String, Object> params) {
		try {
			if(params.get("host")==null){return;}
			this.emailSendService.setHost(params.get("host").toString());
			this.emailSendService.setUsername(params.get("username").toString());
			this.emailSendService.setPassword(params.get("password").toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void start() {

	}

	public void stop() {
		// TODO Auto-generated method stub

	}

	public void destory() {
		// TODO Auto-generated method stub

	}
	/**
	 * type
	 * title
	 * body
	 * to
	 */
	public Map<String, Object> execute(Map<String, Object> params) {
		String type="text";
		String title="";
		String body="";
		Object to=null;
		Map<String, Object> ret=new HashMap<String,Object>();
		boolean b=false;
		if(this.isEnable()){
			try {
				if(params.containsKey("type")){type=params.get("type").toString();}
				title=params.get("title").toString();
				body=params.get("body").toString();
				to=params.get("to");
				if("text".equals(type)){
					if(to instanceof String){
						b=this.emailSendService.sendText((String)to, title, body);
					}else{
						b=this.emailSendService.sendText((String[])to, title, body);
					}
				}else{
					if(to instanceof String){
						b=this.emailSendService.sendHtml((String)to, title, body);
					}else{
						b=this.emailSendService.sendHtml((String[])to, title, body);
					}
				}
				ret.put("code", 1);
				ret.put("message", "SUCCESS");
				ret.put("send",  b);
			} catch (Exception e) {
				e.printStackTrace();
				ret.put("code", -1);
				ret.put("message", "Error:"+e.getLocalizedMessage());
			}
		}else{
			ret.put("code", -1);
			ret.put("message", "发送失败，邮件发送插件已被禁用");
		}
		return ret;
	}

}
