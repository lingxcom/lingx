package com.lingx.core.service.impl.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import com.lingx.core.model.IConfig;
import com.lingx.core.model.IEntity;
import com.lingx.core.model.IExecutor;
import com.lingx.core.model.IField;
import com.lingx.core.model.IMethod;
import com.lingx.core.model.INode;
import com.lingx.core.model.impl.DefaultEntity;
import com.lingx.core.model.impl.DefaultField;
import com.lingx.core.model.impl.DefaultMethod;
import com.lingx.core.model.impl.DefaultNode;
import com.lingx.core.model.impl.ExpressionExecutor;
import com.lingx.core.model.impl.ExpressionInterpreter;
import com.lingx.core.model.impl.GridConfig;
import com.lingx.core.model.impl.QueryEntity;
import com.lingx.core.model.impl.RuleConfig;
import com.lingx.core.utils.Utils;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月10日 上午11:15:01 
 * 类说明 
 */
public class ModelTemplate {

	public static  IEntity createQueryEntity(String code,String name,String author){
		QueryEntity entity=new QueryEntity();
		entity.setCode(code);
		entity.setName(name);
		entity.setAuthor(author);
		return entity;
	}
	public static  IEntity createEntity(String table,String name,String author,int idtype,String dbName,JdbcTemplate jdbc){
		
		DefaultEntity entity=new DefaultEntity();
		entity.setCode(table);
		entity.setName(name);
		entity.setTableName(table);
		entity.setAuthor(author);
		String sql="SELECT column_name as id,table_schema,table_name, column_name,is_nullable,column_type,column_key,extra,column_default,column_comment from information_schema.columns where table_name= ? and table_schema=? order by ordinal_position asc";//(column_name LIKE ? AND) 

		List<Map<String,Object>> list=jdbc.queryForList(sql,new Object[]{table,dbName});
		List<IField> fields=getFields(list,author);
		List<IConfig> configs=getConfigs(author);
		List<IMethod> methods=getMethods(table,list, author,idtype);

		DefaultNode<IConfig> configNode=new DefaultNode<IConfig>("配置");
		configNode.setList(configs);
		
		DefaultNode<IField> fieldNode=new DefaultNode<IField>("属性");
		fieldNode.setList(fields);
		
		DefaultNode<IMethod> methodNode=new DefaultNode<IMethod>("方法");
		methodNode.setList(methods);
		
		entity.setConfigs(configNode);
		entity.setFields(fieldNode);
		entity.setMethods(methodNode);
		return entity;
	}
	private static List<IConfig> getConfigs(String author){
		List<IConfig> configs=new ArrayList<IConfig>();
		GridConfig con=new GridConfig();
		con.setAuthor(author);
		configs.add(con);
//		规则配置
		RuleConfig rule=new RuleConfig();
		con.setAuthor(author);
		configs.add(rule);
		return configs;
	}
	
	private static INode<IField> getFieldNode(List<IField> list){
		DefaultNode<IField> node=new DefaultNode<IField>("属性");
		node.setList(list);
		return node;
	}
	private static INode<IExecutor> getExecutorNode(List<IExecutor> list){
		DefaultNode<IExecutor> node=new DefaultNode<IExecutor>("执行器");
		node.setList(list);
		return node;
	}
	private static List<IMethod> getMethods(String code,List<Map<String,Object>> list,String author,int idtype){
		List<IMethod> methodList=new ArrayList<IMethod>();
		List<IField> fields;
		DefaultMethod m1=new DefaultMethod();
		DefaultMethod m2=new DefaultMethod();
		DefaultMethod m3=new DefaultMethod();

		fields=getFields(list,author);
		m1.setCode("add");
		m1.setName("添加");
		//m1.setOperType("c");
		m1.setCurrentRow(false);
		//m1.setRetType("jsp");
		m1.setAuthor(author);
		m1.setIconCls("Add");
		m1.setFields(getFieldNode(resetFields(fields,false)));
		m1.setViewUri("lingx/template/edit.jsp");;

		List<IExecutor> exeList1=new ArrayList<IExecutor>();
		ExpressionExecutor ce1=new ExpressionExecutor();
		ce1.getScript().setScript(getAddSql(code,fields,idtype));
		exeList1.add(ce1);
		m1.setExecutors(getExecutorNode(exeList1));		

		fields=getFields(list,author);
		m2.setCode("edit");
		m2.setName("修改");
		m2.setIconCls("Pencil");
		m2.setAuthor(author);
		m2.setRightmenu(true);
		//m2.setRetType("jsp");
		m2.setViewUri("lingx/template/edit.jsp");;
		m2.setFields(getFieldNode(resetFields(fields,false)));

		List<IExecutor> exeList2=new ArrayList<IExecutor>();
		ExpressionExecutor ce2=new ExpressionExecutor();
		ce2.getScript().setScript(getEditSql(code,fields));
		exeList2.add(ce2);
		m2.setExecutors(getExecutorNode(exeList2));


		fields=getFields(list,author);
		m3.setCode("del");
		m3.setName("删除");
		m3.setIconCls("Delete");
		m3.setAuthor(author);
		m3.setValidation(false);
		//m3.setRetType("jsp");
		m3.setFields(getFieldNode(resetFields(fields,true)));

		m3.setViewUri("lingx/template/delete.jsp");;
		List<IExecutor> exeList3=new ArrayList<IExecutor>();
		ExpressionExecutor ce3=new ExpressionExecutor();
		ce3.getScript().setScript(String.format("var c=LINGX.countForeignKey(ENTITY_CODE,id); var ret=null; if(c==0){ JDBC.update('delete from %s where id=?',[id]);'操作成功';  }else{ ret={code:-1,success:true,messages:[\"该记录不可删除,外关联数:\"+c]}; ret; }",code));
		exeList3.add(ce3);
		m3.setExecutors(getExecutorNode(exeList3));
		methodList.add(m1);
		methodList.add(m2);
		methodList.add(m3);
		return methodList;
	}
	public static List<IField> resetFields(List<IField> fields,boolean readonly){
		List<IField> newList=new ArrayList<IField>();
		for(IField f:fields){
			DefaultField newField=new DefaultField();
			BeanUtils.copyProperties(f, newField);
			newField.setId(Utils.getRandomString(16));
			if(readonly){
				newField.setInputType("readonly");
			}
			if("id".equalsIgnoreCase(newField.getCode())){
				newField.setInputType("hidden");
				newField.setVisible(false);
			}
			newList.add(newField);
		}
		return newList;
	}
	private static String getAddSql(String code,List<IField> fields,int idtype){
		StringBuilder sb=new StringBuilder();
		sb.append("JDBC.update('insert into ").append(code)
		.append("(");
		switch(idtype){
		case 1://自动递增的主键

			for(IField field:fields){
				
				if("id".equalsIgnoreCase(field.getCode()))continue;
				sb.append(field.getCode()).append(",");
			}
			sb.deleteCharAt(sb.length()-1);
			sb.append(") values(");
			for(IField field:fields){
				if("id".equalsIgnoreCase(field.getCode()))continue;
				sb.append("?,");
			}
			sb.deleteCharAt(sb.length()-1);
			sb.append(")',[");
			for(IField field:fields){
				if("id".equalsIgnoreCase(field.getCode()))continue;
				sb.append(field.getCode()).append(",");
			}
			sb.deleteCharAt(sb.length()-1);
			sb.append("]);'操作成功';");
			break;
		case 2:// UUID
	for(IField field:fields){
				
				sb.append(field.getCode()).append(",");
			}
			sb.deleteCharAt(sb.length()-1);
			sb.append(") values(");
			for(IField field:fields){
				sb.append("?,");
			}
			sb.deleteCharAt(sb.length()-1);
			sb.append(")',[");
			for(IField field:fields){
				if("id".equalsIgnoreCase(field.getCode())){
					sb.append("LINGX.uuid()").append(",");
				}else{
					sb.append(field.getCode()).append(",");
				}
			}
			sb.deleteCharAt(sb.length()-1);
			sb.append("]);'操作成功';");
			break;
		}
		return sb.toString();
	}
	/**
	 * JDBC.update('update %s set name=? where id=?',[name,id]);'操作成功';
	 * @param code
	 * @param fields
	 * @return
	 */
	private static String getEditSql(String code,List<IField> fields){
		StringBuilder sb=new StringBuilder();
		sb.append("JDBC.update('update ").append(code).append(" set ");
		for(IField field:fields){
			if("id".equalsIgnoreCase(field.getCode()))continue;
			sb.append(field.getCode()).append("=?,");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append(" where id=?',[");
		for(IField field:fields){
			if("id".equalsIgnoreCase(field.getCode()))continue;
			sb.append(field.getCode()).append(",");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append(",id]);'操作成功';");
		return sb.toString();
	}
	private static List<IField> getFields(List<Map<String,Object>>  columns,String author){
		String code="",comment="",type="",is_nullable="";
		List<IField> list=new ArrayList<IField>();
		for(Map<String,Object> map:columns){
			if(map.get("is_nullable")!=null)
				is_nullable=map.get("is_nullable").toString();
			if(map.get("column_name")!=null)
			 code=map.get("column_name").toString();
			if(map.get("column_comment")!=null)
			 comment=map.get("column_comment").toString();
			else
				comment=code;
			if(map.get("column_type")!=null)
			 type=map.get("column_type").toString();
			if("".equals(comment))comment=code;
			DefaultField f2=new DefaultField();
			f2.setCode(code);
			//f2.setDbcode(code);
			f2.setName(comment);
			f2.setLength(getLength(type));
			f2.setType(getType(type));
			f2.setAuthor(author);
			if("NO".equals(is_nullable)){
				f2.setIsNotNull(true);
			}
			if("id".equalsIgnoreCase(code)){
				f2.setInputType("hidden");
				f2.setIsNotNull(false);
				f2.setVisible(false);
			}else if(type.indexOf("int")>=0||type.indexOf("decimal")>=0){
				f2.setIsNotNull(true);
			}
			if("name".equalsIgnoreCase(code)){
				f2.setIsNotNull(true);
			}
			if("create_time".equals(code)||"modify_time".equals(code)){
				f2.setDefaultValue("${CDateTime}");
				f2.setWidth("180");
				f2.setEnabled(false);
				ExpressionInterpreter aa=new ExpressionInterpreter();
				aa.setType("datetime");
				f2.getInterpreters().getList().add(aa);
			}
			
			
			
			if("orderindex".equalsIgnoreCase(code)){
				f2.setDefaultValue("100");
			}
			
			if("user_id".equalsIgnoreCase(code)){
				f2.setInputType("dialogoption");
				f2.setRefEntity("tlingx_user");
			}
			if("org_id".equalsIgnoreCase(code)){
				f2.setInputType("dialogtree");
				f2.setRefEntity("tlingx_org");
			}
			list.add(f2);
		}
		return list;
		
	}
	private static String getType(String temp){
		if(temp.indexOf("(")==-1)return temp;
		return temp.substring(0,temp.indexOf("("));
	}
	private static String getLength(String temp){
		if(temp.indexOf("(")==-1)return "20";
		String len=temp.substring(temp.indexOf("("));
		return len.replace("(", "").replace(")", "");
	}
}
