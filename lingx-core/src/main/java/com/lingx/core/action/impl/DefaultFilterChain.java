package com.lingx.core.action.impl;

import com.lingx.core.action.IAction;
import com.lingx.core.action.IFilter;
import com.lingx.core.action.IFilterChain;
import com.lingx.core.engine.IContext;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月23日 下午9:50:08 
 * 类说明 
 */
public class DefaultFilterChain implements IFilterChain {
	private IAction action;
	private IFilter filters[];
	private int index;
	public DefaultFilterChain( IAction action,IFilter filters[]){
		this.action=action;
		this.filters=filters;
		this.index=0;
	}
	@Override
	public String doFilter(IContext context) {
		String ret=null;
		if(this.filters==null||this.filters.length==index){
			ret=this.action.action(context);
		}else{
			ret=this.filters[index++].doFilter(context, this);
		}
		return ret;
	}

}
