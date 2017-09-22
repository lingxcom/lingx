package com.lingx.plugin.sso.cas;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.lingx.core.plugin.IPlugin;
import com.lingx.core.service.ISsoCasService;
@Component
public class SsoCasPlugin implements IPlugin{
	private boolean enable;
	@Resource
	private ISsoCasService ssoCasService;
	@Override
	public String getId() {
		return "sso-cas-plugin";
	}

	@Override
	public String getName() {
		return "基于CAS技术的单点登录插件";
	}

	@Override
	public String getDetail() {
		return "需要appid:应用ID由CAS分配,casurl:CAS受理地址,authurl:令牌验证地址,logouturl:系统退出后跳转的地址";
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
		return enable;
	}

	@Override
	public void setEnable(boolean enable) {
		this.enable=enable;
	}

	@Override
	public void init(Map<String, Object> params) {
		try {
			String appid=params.get("appid").toString();
			String casurl=params.get("casurl").toString();
			String authurl=params.get("authurl").toString();
			String logouturl=params.get("logouturl").toString();
			this.ssoCasService.setAppid(appid);
			this.ssoCasService.setCasUrl(casurl);
			this.ssoCasService.setAuthUrl(authurl);
			this.ssoCasService.setLogoutUrl(logouturl);
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
