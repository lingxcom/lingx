package com.lingx.core.service.impl;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.model.IEntity;
import com.lingx.core.service.IDatabaseService;
import com.lingx.core.service.IQueryService;
import com.lingx.support.database.ICondition;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月26日 下午3:05:18 
 * 类说明 
 */
public class QueryServiceImpl implements IQueryService {
	public static Logger logger = LogManager.getLogger(QueryServiceImpl.class);
	private ICondition conditions[];
	@Resource
	private IDatabaseService databaseService;
	//private Cache<String, String> cache = CacheBuilder.newBuilder().maximumSize(1000).build();  //后期要加入缓存不然计算查询条件太费时
	@Override
	public String getQueryString(IContext context,IPerformer performer ) {
		
		StringBuilder sb=new StringBuilder();
		for(ICondition condition:this.conditions){
			sb.append(condition.getCondition(context,performer ));
		}
		return sb.toString();
	}
	public void setConditions(ICondition[] conditions) {
		this.conditions = conditions;
	}
	@Override
	public String getSelectSql(IContext context,IPerformer performer) {
		IEntity entity=context.getEntity();
		StringBuilder sb=new StringBuilder();
		sb.append(this.databaseService.getSelectSql(entity, context, performer));
		sb.append(this.getQueryString(context,performer));
		logger.debug(sb.toString());
		return sb.toString();
	}
	
	@Override
	public String getCountSql(String selectSql) {
		StringBuilder sb=new StringBuilder();
		int slen=selectSql.toLowerCase().indexOf(" from ");
		int elen=selectSql.toLowerCase().indexOf(" order ");
		if(elen==-1){
			sb.append("select count(*) ").append(selectSql.substring(slen));
		}else{
			sb.append("select count(*) ").append(selectSql.substring(slen,elen));
		}
		return sb.toString();
	}
	public void setDatabaseService(IDatabaseService databaseService) {
		this.databaseService = databaseService;
	}

}
