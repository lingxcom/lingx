package com.lingx.support.database.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.model.IEntity;
import com.lingx.core.model.IField;
import com.lingx.core.model.impl.GridConfig;
import com.lingx.core.service.IModelService;
import com.lingx.core.utils.Utils;
import com.lingx.support.database.ICondition;

public class SreachCondition implements ICondition {

	public static final String NOT_COND=",page,rows,sort,order,";
	@Resource
	private IModelService modelService;
	@Override
	public String getCondition(IContext context,IPerformer performer ) {
		StringBuilder sb=new StringBuilder();
		String params=((GridConfig)context.getEntity().getConfigs().getList().get(0)).getQueryField();
		//System.out.println(params);
		//System.out.println(context.getRequest().getParameter("isGridSearch"));
		if("_true".equals(context.getRequest().getParameter("isGridSearch"))&&params.trim().charAt(0)=='['){
			//自定义扩展的列表查询 2019-03-12
			List<Map<String,Object>> listJSON=(List<Map<String,Object>>)JSON.parse(params);
			Set<String>keys=new HashSet<>();
			for(Map<String,Object> mapJSON:listJSON){
				String pparam=mapJSON.get("code").toString();
				keys.add(pparam);
				String pvalue=context.getRequest().getParameter(pparam);
				if(pvalue==null||"_".equals(pvalue))continue;
				pvalue=pvalue.trim();
				if(pvalue.charAt(0)=='_'){
					pvalue=(pvalue.substring(1));
				}else{
					//前缀没有下划线_的是字段对应真值，不采用配置里边的sql语句 2021-01-23
					sb.append(" and ").append(pparam).append("='").append(pvalue).append("' ");
					continue;
				}
				
				if(mapJSON.get("sql")!=null){
					sb.append(" and ").append(formatString(mapJSON.get("sql").toString(),pparam, pvalue));
				}else{
					sb.append(" and ").append(pparam).append("='").append(pvalue).append("' ");
				}
			}
			
			for(IField field:context.getEntity().getFields().getList()){
				if(keys.contains(field.getCode()))continue;
				field.setValue(context.getRequest().getParameter(field.getCode()));
				if(field.getValue()==null)continue;
				String fieldValue=String.valueOf(field.getValue()).trim();
				if(Utils.isNotNull(fieldValue)&&fieldValue.startsWith("__")){//高级查询处理
					sb.append(analyze(field.getCode(),fieldValue));
				}else if(Utils.isNotNull(fieldValue)&&!"_".equals(fieldValue)&&NOT_COND.indexOf(","+field.getCode()+",")==-1){
					//if("id".equals(field.getCode())){continue;}
					if(Utils.isNotNull(field.getRefEntity())&&fieldValue.charAt(0)=='_'&&!"_true".equals(context.getRequest().getParameter("isGridSearch"))){//&&!"true".equals(context.getRequest().getParameter("isGridSearch"))不是在列表中查询的
						IEntity ref=modelService.getCacheEntity(field.getRefEntity());
						if("id".equals(modelService.getTextField(ref).get(0))){continue;}
						sb.append(" and ").append(field.getCode()).append(" in (select ").append(modelService.getValueField(ref)).append(" from ").append(field.getRefEntity()).append(" where ").append(modelService.getTextField(ref).get(0)).append(" like '%").append(fieldValue.substring(1)).append("%')");
					}else if(!field.getCode().endsWith("_id")&&("varchar2".equalsIgnoreCase(field.getType())||"varchar".equalsIgnoreCase(field.getType())||"char".equalsIgnoreCase(field.getType()))){
						if(fieldValue.charAt(0)=='_'){field.setValue(fieldValue.substring(1));}
						sb.append(" and ").append(field.getCode()).append(" like '%").append(field.getValue().toString().trim()).append("%' ");
						
					}else{
						
						if(fieldValue.charAt(0)=='_'){field.setValue(fieldValue.substring(1));}
						sb.append(" and ").append(field.getCode()).append("='").append(field.getValue().toString().trim()).append("' ");
					}
				}
			}
			
			
		}else{
			//常规处理
		for(IField field:context.getEntity().getFields().getList()){
			field.setValue(context.getRequest().getParameter(field.getCode()));
			if(field.getValue()==null)continue;
			String fieldValue=String.valueOf(field.getValue()).trim();
			if(Utils.isNotNull(fieldValue)&&fieldValue.startsWith("__")){//高级查询处理
				sb.append(analyze(field.getCode(),fieldValue));
			}else if(Utils.isNotNull(fieldValue)&&!"_".equals(fieldValue)&&NOT_COND.indexOf(","+field.getCode()+",")==-1){
				//if("id".equals(field.getCode())){continue;}
				if(Utils.isNotNull(field.getRefEntity())&&fieldValue.charAt(0)=='_'&&!"_true".equals(context.getRequest().getParameter("isGridSearch"))){//&&!"true".equals(context.getRequest().getParameter("isGridSearch"))不是在列表中查询的
					IEntity ref=modelService.getCacheEntity(field.getRefEntity());
					if("id".equals(modelService.getTextField(ref).get(0))){continue;}
					sb.append(" and ").append(field.getCode()).append(" in (select ").append(modelService.getValueField(ref)).append(" from ").append(field.getRefEntity()).append(" where ").append(modelService.getTextField(ref).get(0)).append(" like '%").append(fieldValue.substring(1)).append("%')");
				}else if(!field.getCode().endsWith("_id")&&("varchar2".equalsIgnoreCase(field.getType())||"varchar".equalsIgnoreCase(field.getType())||"char".equalsIgnoreCase(field.getType()))){
					if(fieldValue.charAt(0)=='_'){field.setValue(fieldValue.substring(1));}
					sb.append(" and ").append(field.getCode()).append(" like '%").append(field.getValue().toString().trim()).append("%' ");
					
				}else{
					
					if(fieldValue.charAt(0)=='_'){field.setValue(fieldValue.substring(1));}
					sb.append(" and ").append(field.getCode()).append("='").append(field.getValue().toString().trim()).append("' ");
				}
			}
		}
		}
/*		System.out.println("=========SreachCondition=S================");
		System.out.println("===>"+sb.toString());
		
		for(String key:context.getRequest().getParameterNames()){
			System.out.println(key+":"+context.getRequest().getParameter(key));
		}

		System.out.println("=========SreachCondition=E================");
		*/
		//System.out.println("===>"+sb.toString());
		return sb.toString();
	}
	public void setModelService(IModelService modelService) {
		this.modelService = modelService;
	}
	
	public static final String formatString(String temp,String key,String value){
		temp=temp.replaceAll("\\$\\{"+key+"\\}", value);
		return temp;
	}
	public static String analyze(String code,String params){
		StringBuilder sb=new StringBuilder();
		params=params.replace("__", "_");
		String array1[]=params.split("[|]");
		for(String str1:array1){
			String array2[]=str1.split("_");
			if("等于".equals(array2[1])){
				if(array2.length>=3){
					sb.append(" and ").append(code).append(" = '").append(array2[2]).append("'");
				}else{
					sb.append(" and ").append(code).append(" = ''");
				}
			}else if("包含".equals(array2[1])){
				if(array2.length>=3)
				sb.append(" and ").append(code).append(" like '%").append(array2[2]).append("%'");
			}else if("大于".equals(array2[1])){
				if(array2.length>=3)
				sb.append(" and ").append(code).append(" > '").append(array2[2]).append("'");
			}else if("小于".equals(array2[1])){
				if(array2.length>=3)
				sb.append(" and ").append(code).append(" < '").append(array2[2]).append("'");
			}else if("不等于".equals(array2[1])){
				if(array2.length>=3){
					sb.append(" and ").append(code).append(" <> '").append(array2[2]).append("'");
				}else{
					sb.append(" and ").append(code).append(" <> ''");
				}
			}else if("不包含".equals(array2[1])){
				if(array2.length>=3)
				sb.append(" and ").append(code).append(" not like '%").append(array2[2]).append("%'");
			}else if("大于等于".equals(array2[1])){
				if(array2.length>=3)
				sb.append(" and ").append(code).append(" >= '").append(array2[2]).append("'");
			}else if("小于等于".equals(array2[1])){
				if(array2.length>=3)
				sb.append(" and ").append(code).append(" <= '").append(array2[2]).append("'");
			}else if("匹配".equals(array2[1])){
				if(array2.length>=3)
				sb.append(" and ").append(code).append(" REGEXP '").append(array2[2]).append("'");
			}else if("为空".equals(array2[1])){
				sb.append(" and ").append(code).append(" is null ");
			}else if("不为空".equals(array2[1])){
				sb.append(" and ").append(code).append(" is not null ");
			}
		}
		return sb.toString();
	}

	public static void main(String args[]){
		String param="__等于_1|_不等于_2";
		System.out.println(analyze("status",param));
	}
}
