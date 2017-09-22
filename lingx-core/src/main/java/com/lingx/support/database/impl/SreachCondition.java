package com.lingx.support.database.impl;

import javax.annotation.Resource;

import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.model.IEntity;
import com.lingx.core.model.IField;
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
		for(IField field:context.getEntity().getFields().getList()){
			field.setValue(context.getRequest().getParameter(field.getCode()));
			if(field.getValue()==null)continue;
			String fieldValue=String.valueOf(field.getValue());
			if(Utils.isNotNull(fieldValue)&&fieldValue.startsWith("__")){
				sb.append(analyze(field.getCode(),fieldValue));
			}else if(Utils.isNotNull(fieldValue)&&!"_".equals(fieldValue)&&NOT_COND.indexOf(","+field.getCode()+",")==-1){
				//if("id".equals(field.getCode())){continue;}
				if(Utils.isNotNull(field.getRefEntity())&&fieldValue.charAt(0)=='_'&&!"_true".equals(context.getRequest().getParameter("isGridSearch"))){//&&!"true".equals(context.getRequest().getParameter("isGridSearch"))不是在列表中查询的
					IEntity ref=modelService.getCacheEntity(field.getRefEntity());
					if("id".equals(modelService.getTextField(ref).get(0))){continue;}
					sb.append(" and ").append(field.getCode()).append(" in (select ").append(modelService.getValueField(ref)).append(" from ").append(field.getRefEntity()).append(" where ").append(modelService.getTextField(ref).get(0)).append(" like '%").append(fieldValue.substring(1)).append("%')");
				}else if(!field.getCode().endsWith("_id")&&("varchar2".equalsIgnoreCase(field.getType())||"varchar".equalsIgnoreCase(field.getType())||"char".equalsIgnoreCase(field.getType()))){
					if(fieldValue.charAt(0)=='_'){field.setValue(fieldValue.substring(1));}
					sb.append(" and ").append(field.getCode()).append(" like '%").append(field.getValue()).append("%' ");
					
				}else{
					
					if(fieldValue.charAt(0)=='_'){field.setValue(fieldValue.substring(1));}
					sb.append(" and ").append(field.getCode()).append("='").append(field.getValue()).append("' ");
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
		return sb.toString();
	}
	public void setModelService(IModelService modelService) {
		this.modelService = modelService;
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
