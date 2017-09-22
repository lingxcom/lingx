package com.lingx.support.database.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.model.IConfig;
import com.lingx.core.model.IEntity;
import com.lingx.core.model.impl.GridConfig;
import com.lingx.core.service.IModelService;
import com.lingx.support.database.ICondition;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月26日 下午10:30:22 
 * 类说明 
 */
public class OrderCondition implements ICondition {
	@Resource
	private IModelService modelService;
	@Override
	public String getCondition(IContext context,IPerformer performer ) {
		String order = "desc";
		String sort = "id";
		StringBuilder sb=new StringBuilder();
		sort=context.getRequest().getParameter("sort");
		if("combo".equals(context.getRequest().getParameter("m"))){
			sort="convert("+this.modelService.getTextField(context.getEntity()).get(0)+" using gbk)";//convert(name using gbk)
			order="asc";
		}else
		if(sort!=null){//EXTJS的传参方式
			List<Map<String,String>> list=(List<Map<String,String>>)JSON.parse(sort);
			Map<String,String>map=list.get(0);
			sort=map.get("property");
			order=map.get("direction");
		}else{//当Request没有传入排序参数时，从配置中读取
			IEntity entity=context.getEntity();
			GridConfig gridConfig=null;
			for(IConfig obj:entity.getConfigs().getList()){
				if(obj instanceof GridConfig){
					gridConfig=(GridConfig)obj;
					break;
				}
			}
			if(gridConfig!=null){
				sort=gridConfig.getSortName();
				order=gridConfig.getSortOrder();
			}
			
		}
		sb.append(" order by ").append(sort).append(" ").append(order);
		return sb.toString();
	}
	

}
