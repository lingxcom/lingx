package com.lingx.core.service;

import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.model.IEntity;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月6日 上午8:29:21 
 * 类说明 
 */
public interface IDatabaseService {

	public String getDatabaseName();

	public String getSelectSql(IEntity entity,IContext context,IPerformer performer);
	
	public String getSelectSqlById(IEntity entity,IContext context,IPerformer performer);
}
