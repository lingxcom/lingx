package com.lingx.core.service;

import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月26日 下午1:31:10 
 * 类说明 
 */
public interface IQueryService {
	/**
	 * 获得查询条件
	 * @param context
	 * @param performer
	 * @return
	 */
	public String getQueryString(IContext context,IPerformer performer );
	/**
	 * 查询列表的SQL
	 * @param context
	 * @param performer
	 * @return
	 */
	public String getSelectSql(IContext context,IPerformer performer);
	/**
	 * 通过查询列表SQL转换取总记录数SQL
	 * @param selectSql
	 * @return
	 */
	public String getCountSql(String selectSql);
	
}
