package com.lingx.core.service;

import javax.servlet.http.HttpServletRequest;

/** 
 * @author www.lingx.com
 * @version 创建时间：2016年8月30日 上午10:27:20 
 * 类说明 
 */
public interface ISsoCasService {
	/**
	 * 获取CAS指定的跳转地址
	 * @param request，因为可能要从request获取参数
	 * @return
	 */
	public String getCasUrl(HttpServletRequest request);
	/**
	 * 验证登陆令牌，验证后要将UserBean 写入Session
	 * @param ticket 令牌
	 * @param request
	 * @return 验证成功为true
	 */
	public boolean auth(String ticket,HttpServletRequest request);
	
	public void setAppid(String appid);
	public void setCasUrl(String casUrl);
	public void setAuthUrl(String authUrl);
	public void setLogoutUrl(String logoutUrl);
	public String getLogoutUrl();
}
