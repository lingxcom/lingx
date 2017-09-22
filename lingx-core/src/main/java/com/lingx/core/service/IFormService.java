package com.lingx.core.service;

import com.lingx.core.engine.IContext;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年10月13日 下午6:01:20 
 * 类说明 
 */
public interface IFormService {
	/**
	 * 表单渲染
	 * @param content
	 * @param instanceId
	 * @return
	 */
	public String formWorkflowRendering(String content,String instanceId,IContext context);
}
