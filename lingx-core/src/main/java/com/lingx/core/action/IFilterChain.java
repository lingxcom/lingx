package com.lingx.core.action;

import com.lingx.core.engine.IContext;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月23日 下午9:13:02 
 * 类说明 
 */
public interface IFilterChain {

	public String doFilter(IContext context);
}
