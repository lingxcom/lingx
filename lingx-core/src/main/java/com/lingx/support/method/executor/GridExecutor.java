package com.lingx.support.method.executor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.lingx.core.SpringContext;
import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.event.GridExecutorEvent;
import com.lingx.core.exception.LingxScriptException;
import com.lingx.core.model.IExecutor;
import com.lingx.core.model.IScript;
import com.lingx.core.model.impl.AbstractModel;
import com.lingx.core.service.IInterpretService;
import com.lingx.core.service.IQueryService;
import com.lingx.core.service.impl.LingxServiceImpl;
import com.lingx.core.utils.LingxUtils;

public class GridExecutor extends AbstractModel implements IExecutor {
	
	private static final long serialVersionUID = 2658795210422869808L;
	public static Logger logger = LogManager.getLogger(GridExecutor.class);
	private IQueryService queryService;
	@Resource
	private IInterpretService interpretService;
	@Resource
	private JdbcTemplate jdbcTemplate;
	public GridExecutor(){
		super();
		this.setName("GridExecutor");
	}
	@Override
	public Object execute( IContext context,IPerformer performer)
			throws LingxScriptException {
			Map<String,Object> map=new HashMap<String,Object>();
			String sql=this.queryService.getSelectSql(context,performer);
			//System.out.println("--------GridExecutor-----------");
			//System.out.println(sql);
			sql=LingxUtils.sqlInjection(sql);
			if(!LingxServiceImpl.SN)return new HashMap<String,Object>();
			//context.getSession().put(Constants.SESSION_LAST_QUERY_SQL, sql);
			SpringContext.getApplicationContext().publishEvent(new GridExecutorEvent(this,sql,context,performer));
			logger.debug(sql);
			List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
			this.interpretService.outputFormat(list, context.getEntity().getFields().getList(), context.getEntity(),context, performer);
			map.put("rows", list);
			map.put("total", jdbcTemplate.queryForInt(this.queryService.getCountSql(sql)));
			
		return map;
	}
	
	@Override
	public IScript getScript() {
		return null;
	}
	public void setQueryService(IQueryService queryService) {
		this.queryService = queryService;
	}
	public void setInterpretService(IInterpretService interpretService) {
		this.interpretService = interpretService;
	}
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

}
