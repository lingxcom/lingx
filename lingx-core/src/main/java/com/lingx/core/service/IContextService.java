package com.lingx.core.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.lingx.core.engine.IContext;
import com.lingx.core.model.bean.UserBean;

/** 
 * @author www.lingx.me
 * @version 创建时间：2015年6月29日 上午7:36:32 
 * 类说明 
 */
public interface IContextService {
	/**
	 * 获取request对象的请求参数
	 * @param request
	 * @return
	 */
	public Map<String,String[]> getRequestParameters(HttpServletRequest request);
	/**
	 * 获取request对象的属性及扩展属性
	 * @param request
	 * @return
	 */
	public Map<String,Object> getRequestAttributes(HttpServletRequest request);/**
	 * 获取request对象的属性及扩展属性
	 * @param request
	 * @return
	 */
	public Map<String,Object> getSesssionAttributes(HttpSession session);
	
	public IContext getContext(HttpServletRequest request);
	public UserBean getUserBean(HttpServletRequest request);
}
