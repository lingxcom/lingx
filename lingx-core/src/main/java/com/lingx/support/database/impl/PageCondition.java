package com.lingx.support.database.impl;

import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;
import com.lingx.support.database.ICondition;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月26日 下午10:30:22 
 * 类说明 
 */
public class PageCondition implements ICondition {
	//@Resource
	//private IModelService modelService;
	
	@Override
	public String getCondition(IContext context,IPerformer performer ) {
		 String page="1";
		 String rows="20";
		StringBuilder sb=new StringBuilder();
		String temp=null;
		
		temp=context.getRequest().getParameter("page");
		page=temp==null?page:temp;
		temp=context.getRequest().getParameter("limit");
		rows=temp==null?rows:temp;
		
		sb.append(" LIMIT ").append(((Integer.parseInt(page) - 1) * Integer.parseInt(rows))).append(",").append(rows);
		return sb.toString();
	}
}
