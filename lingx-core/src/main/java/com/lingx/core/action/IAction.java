package com.lingx.core.action;

import com.lingx.core.engine.IContext;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月17日 上午11:10:58 
 * 通过Servlet Filter调用各URL的处理
 */
public interface IAction {

	public String action(IContext context);
}
