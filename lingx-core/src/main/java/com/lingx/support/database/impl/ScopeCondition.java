package com.lingx.support.database.impl;

import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;
import com.lingx.support.database.ICondition;

public class ScopeCondition implements ICondition {

	@Override
	public String getCondition(IContext context,IPerformer performer ){
		String extQueryParam=context.getRequest().getParameter("extparam");
		if(extQueryParam==null)return "";
		extQueryParam=extQueryParam.replace("notin", "not in");
		String temp=extQueryParam;
		String arr[]=temp.split(",");
		String sql=" and id %s (select %s from %s where %s='%s') ";
		
		if(!"in".equals(arr[0])&&!"not in".equals(arr[0])){
			return "";
		}
		
		if(arr.length>=5)
			return String.format(sql, arr[0], arr[1], arr[2], arr[3], arr[4]);
		else return "";
	}

}
