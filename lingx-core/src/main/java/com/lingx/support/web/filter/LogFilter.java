package com.lingx.support.web.filter;

import javax.annotation.Resource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.alibaba.fastjson.JSON;
import com.lingx.core.Constants;
import com.lingx.core.Page;
import com.lingx.core.action.IFilter;
import com.lingx.core.action.IFilterChain;
import com.lingx.core.engine.IContext;
import com.lingx.core.model.IEntity;
import com.lingx.core.model.IMethod;
import com.lingx.core.service.IModelService;
import com.lingx.core.utils.Utils;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年6月18日 下午12:03:33 
 * 类说明 
 */
public class LogFilter implements IFilter{
	/**
	 * req与res字段长度
	 */
	public static final int FIELD_LENGTH=1000;
	public static final String sql="insert into tlingx_operate_log(user_id,content,ip,time,req,res)values(?,?,?,?,?,?)";
	@Resource
	private JdbcTemplate jdbcTemplate;
	@Resource
	private IModelService modelService;
	@Override
	public String doFilter(IContext context, IFilterChain filterChain) {
		String content="自动记录",req,res,page=null;
		String e=context.getRequest().getParameter("e"),m=context.getRequest().getParameter("m"),id=context.getRequest().getParameter("id");
		if(e==null||m==null)return filterChain.doFilter(context);
		try {
			IEntity entity=this.modelService.getCacheEntity(e);
			IMethod method=this.modelService.getMethod(m, entity);
			String name="";
			if(Utils.isNotNull(id)){
				name=this.jdbcTemplate.queryForObject(String.format("select %s from %s where id=?", this.modelService.getTextField(entity).get(0),entity.getTableName()), String.class,id);
			}else{
				name=context.getRequest().getParameter(this.modelService.getTextField(entity).get(0));
			}

			page=filterChain.doFilter(context);
			
			if(Utils.isNull(name)||!Page.PAGE_JSON.equals(page))return page;
			req=JSON.toJSONString(context.getRequest().getParameters());
			res=JSON.toJSONString(context.getRequest().getAttribute(Constants.REQUEST_JSON));
			if(req.length()>FIELD_LENGTH){
				req=req.substring(0,FIELD_LENGTH);
			}
			if(res.length()>FIELD_LENGTH){
				res=res.substring(0,FIELD_LENGTH);
			}
			
			content=String.format("%s-%s->%s", entity.getName(),method.getName(),name);
			this.jdbcTemplate.update(sql,context.getUserBean().getId(),content,context.getRequest().getAttribute(IContext.CLIENT_IP),Utils.getTime(),req,res);
			
		} catch (Exception e1) {
			if(page==null)
			page=filterChain.doFilter(context);
		}
		
		
		return page;
	}
	
	
}
