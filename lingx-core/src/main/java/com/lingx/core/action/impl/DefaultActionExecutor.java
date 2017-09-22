package com.lingx.core.action.impl;

import com.lingx.core.action.IAction;
import com.lingx.core.action.IActionExecutor;
import com.lingx.core.action.IFilter;
import com.lingx.core.action.IFilterChain;
import com.lingx.core.engine.IContext;

public class DefaultActionExecutor implements IActionExecutor {
	private String urlPatterns[];
	private IAction action;
	private IFilter filters[];
	private IFilterChain filterChain;
	public String execute(IContext context) {
		this.filterChain=new DefaultFilterChain(this.action,this.filters);
		return this.filterChain.doFilter(context);
	}
	public void init() {
	}
	public void destroy() {
		this.action=null;
		this.filters=null;
		this.filterChain=null;
	}
	public void setAction(IAction action) {
		this.action = action;
	}
	public void setFilters(IFilter[] filters) {
		this.filters = filters;
	}
	public void setUrlPatterns(String[] urlPatterns) {
		this.urlPatterns = urlPatterns;
	}

	public String[] getUrlPatterns() {
		return this.urlPatterns;
	}
	public IAction getAction() {
		return action;
	}
	
}
