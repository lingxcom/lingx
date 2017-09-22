package com.lingx.core.event;

import org.springframework.context.ApplicationEvent;

import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年9月17日 下午1:00:04 
 * 类说明 
 */
public class GridExecutorEvent  extends ApplicationEvent {

	private static final long serialVersionUID = -103772989874537906L;
	private String sql;
	private IContext context;
	private IPerformer performer;
	public GridExecutorEvent(Object source,String sql,IContext context,IPerformer performer) {
		super(source);
		this.sql=sql;
		this.context=context;
		this.performer=performer;
	}
	public String getSql() {
		return sql;
	}
	public IContext getContext() {
		return context;
	}
	public IPerformer getPerformer() {
		return performer;
	}

}
