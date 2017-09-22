package com.lingx.support.model.validator;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.exception.LingxScriptException;
import com.lingx.core.utils.Utils;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年10月7日 下午9:41:28 
 * 类说明 
 */
public class TreeFidValidator  extends AbstractValidator {
	
	public static final String sql="select id from %s where fid=?";

	private static final long serialVersionUID = -2378034202921820146L;
	@Resource
	private JdbcTemplate jdbcTemplate;
	@Override
	public boolean valid(String code, Object value, String param,
			IContext context, IPerformer performer) throws LingxScriptException {
		if(!"fid".equals(code.toLowerCase()))return true;
		if(Utils.isNull(context.getRequest().getParameter("id"))) return true;
		if(value==null)return false;
		boolean b=false;
		Set<String> sets=new HashSet<String>();
		tree(context.getRequest().getParameter("id"),context.getEntity().getTableName(),sets);
		b=!sets.contains(value.toString());
		sets.clear();
		return b;
	}
	
	private void tree(String id,String table,Set<String> sets){
		sets.add(id);
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList(String.format(sql, table),id);
		for(Map<String,Object> map:list){
			tree(map.get("id").toString(),table,sets);
		}
	}
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
}
