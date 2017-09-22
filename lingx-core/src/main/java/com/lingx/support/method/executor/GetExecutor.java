package com.lingx.support.method.executor;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.exception.LingxScriptException;
import com.lingx.core.model.IExecutor;
import com.lingx.core.model.IScript;
import com.lingx.core.model.impl.AbstractModel;
import com.lingx.core.service.IInterpretService;

public class GetExecutor extends AbstractModel implements IExecutor {
	
	private static final long serialVersionUID = 2658025210422869808L;
	
	@Resource
	private IInterpretService interpretService;
	@Resource
	private JdbcTemplate jdbcTemplate;
	public GetExecutor(){
		super();
		this.setName("GetExecutor");
	}
	@Override
	public Object execute( IContext context,IPerformer performer)
			throws LingxScriptException {
		String id=context.getRequest().getParameter("id");
		Map<String,Object> obj=this.jdbcTemplate.queryForMap("select * from "+context.getEntity().getTableName()+" where id=?",id);
		this.interpretService.outputFormat(obj, context.getEntity().getFields().getList(), context.getEntity(), context, performer);
		return obj;
	}
	
	@Override
	public IScript getScript() {
		return null;
	}

}
