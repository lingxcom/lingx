package com.lingx.core.action;

import com.lingx.core.engine.IContext;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月23日 下午8:45:04 
 * 类说明 
 */
public interface IFilter {
	/**
	 * 过滤方法
	 * @param context 环境上下文
	 * @param filterChain 链 
	 * @return 要显示的界面
	 */
	public String doFilter(IContext context,IFilterChain filterChain);
}
