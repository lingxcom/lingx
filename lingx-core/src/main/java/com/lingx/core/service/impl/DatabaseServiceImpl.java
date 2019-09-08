package com.lingx.core.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.exception.LingxScriptException;
import com.lingx.core.model.IEntity;
import com.lingx.core.model.IField;
import com.lingx.core.model.impl.DefaultEntity;
import com.lingx.core.model.impl.QueryEntity;
import com.lingx.core.service.IDatabaseService;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月25日 下午9:01:05 
 * 类说明 
 */
@Component(value="lingxDatabaseService")
public class DatabaseServiceImpl implements IDatabaseService {

	@Value("#{configs['database.name']}")
	private String databaseName;
	public DatabaseServiceImpl(){
		this.databaseName="lingx";
	}
	@Override
	public String getDatabaseName() {
		return this.databaseName;
	}
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
	
	
	public String getSelectSql(IEntity entity, IPerformer performer) {
		return this.getSelectSql(entity, null, performer);
	}
	@Override
	public String getSelectSql(IEntity entity, IContext context,IPerformer performer) {
		StringBuilder sb=new StringBuilder();
		if(entity instanceof QueryEntity){
			QueryEntity qe=(QueryEntity)entity;
			Set<String>reqParams=context.getRequest().getParameterNames();
			for(IField field:qe.getParams().getList()){
				if(!reqParams.contains(field.getCode())){
					performer.addParam(field.getCode(), null);
				}
			}
			try {
				sb.append(performer.script(qe.getQueryScript().getScript(), context));
			} catch (LingxScriptException e) {
				e.printStackTrace();
			}
		}else if(entity instanceof DefaultEntity){
			sb.append("select * from ").append(entity.getTableName()).append(" where 1=1 ");
		}else {
			
		}
		return sb.toString();
	}
	@Override
	public String getSelectSqlById(IEntity entity, IContext context,
			IPerformer performer) {
		StringBuilder sb=new StringBuilder();
		sb.append(this.getSelectSql(entity, context, performer)).append(" and id=? limit 1");
		return sb.toString();
	}

}
