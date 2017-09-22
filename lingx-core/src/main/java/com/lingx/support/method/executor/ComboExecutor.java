package com.lingx.support.method.executor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.exception.LingxScriptException;
import com.lingx.core.model.IEntity;
import com.lingx.core.model.IExecutor;
import com.lingx.core.model.IScript;
import com.lingx.core.model.impl.AbstractModel;
import com.lingx.core.service.IModelService;
import com.lingx.core.service.IQueryService;
import com.lingx.core.utils.LingxUtils;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年6月11日 下午8:26:15 
 * 类说明 
 */
public class ComboExecutor extends AbstractModel implements IExecutor {

	private static final long serialVersionUID = 4266855366142478064L;
	
	private IQueryService queryService;

	private JdbcTemplate jdbcTemplate;
	@Resource
	private IModelService modelService;
	@Override
	public Object execute(IContext context, IPerformer performer)
			throws LingxScriptException {

		IEntity entity=(IEntity)context.getEntity();
		List<String> textField=modelService.getTextField(entity);
		String valueField=modelService.getValueField(entity);
		try {
			String sql=this.queryService.getSelectSql(context,performer);
			//System.out.println("--------ComboExecutor-----------");
			//System.out.println(sql);
			List<Map<String,Object>> list=jdbcTemplate.queryForList(LingxUtils.sqlInjection(sql));
			if(list!=null){
				for(Map<String,Object> map:list){
					map.put("value", map.get(valueField.toUpperCase()));
					StringBuilder sb=new StringBuilder();
					for(String s:textField){
						sb.append(map.get(s.toUpperCase())).append("-");
					}
					sb.deleteCharAt(sb.length()-1);
					map.put("text", sb.toString());
				}
				if(context.getRequest().getParameter("issearch")!=null){
					Map<String,Object> all=new HashMap<String,Object>();
					all.put("text", "全部");
					all.put("value","");
					list.add(0,all);
				}
				
			}
			return list;
		} catch (Exception e) {
			throw new LingxScriptException("数据读取异常",e);
		} 
	}

	@Override
	public IScript getScript() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setQueryService(IQueryService queryService) {
		this.queryService = queryService;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void setModelService(IModelService modelService) {
		this.modelService = modelService;
	}

}
