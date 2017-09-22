package com.lingx.core.engine;

import java.util.Map;

import com.lingx.core.model.IEntity;
import com.lingx.core.model.IMethod;
import com.lingx.core.model.bean.UserBean;
/**
 * 数据暂存器
 * 执行上下文
 * @author lingx
 *
 */
public interface IContext {
	
	/**
	 * 存在request.attribute的客户端IP
	 */
	public static final String CLIENT_IP="ClientIP";
	/**
	 * 存在request.attrbute的项目运行根目录
	 */
	public static final String LOCAL_PATH="LocalPath";
	/**
	 * 获取RQEUEST对象，等同HttpServletRequest
	 * @return
	 */
	public IHttpRequest getRequest();
	/**
	 * 获取SESSION对象，等同HttpSession
	 * @return
	 */
	public Map<String,Object> getSession();
	/**
	 * 设置当前对象模型
	 * @param entity
	 */
	public void setEntity(IEntity entity);
	/**
	 * 设置当前方法模型
	 * @param method
	 */
	public void setMethod(IMethod method);
	/**
	 * 取出当前对象模型
	 * @return
	 */
	public IEntity getEntity();
	/**
	 * 取出当前方法模型
	 * @return
	 */
	public IMethod getMethod();
	/**
	 * 取出当前登录用户
	 * @return
	 */
	public UserBean getUserBean();
	
	public void addMessage(String key,String message);
	public Map<String,Object> getMessages();
}
