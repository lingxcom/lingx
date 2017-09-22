package com.lingx.core.test.spring;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lingx.core.model.IEntity;
import com.lingx.core.model.IMethod;
import com.lingx.core.model.impl.DefaultField;
import com.lingx.core.model.impl.DefaultMethod;
import com.lingx.core.model.impl.ExpressionExecutor;
import com.lingx.core.model.impl.JavaScript;
import com.lingx.core.service.impl.model.ModelIO;
import com.lingx.core.service.impl.model.ModelTemplate;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月25日 下午6:47:07 
 * 类说明 
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:lingx-database.xml"})
public class TestCrerateEntity {
	@Resource
	private JdbcTemplate jdbcTemplate;
	@Test
	public void testCreateEntity(){
		ModelTemplate mt=new ModelTemplate();
		
		try {/*
			jdbcTemplate.update("delete from tlingx_model where id=?","tlingx_user");
			IEntity user=mt.createEntity("tlingx_user", "用户管理", "admin", 2, "lingx2", this.jdbcTemplate);
			ModelIO.writeEntity( user, jdbcTemplate);

			jdbcTemplate.update("delete from tlingx_model where id=?","tlingx_option");
			IEntity option=mt.createEntity("tlingx_option", "系统字典", "admin", 2, "lingx2", this.jdbcTemplate);
			option.getMethods().getList().add(getOptionItemsMethod());
			ModelIO.writeEntity( option, jdbcTemplate);

			jdbcTemplate.update("delete from tlingx_model where id=?","tlingx_optionitem");
			IEntity optionItems=mt.createEntity("tlingx_optionitem", "字典子项", "admin", 2, "lingx2", this.jdbcTemplate);
			ModelIO.writeEntity( optionItems, jdbcTemplate);

			jdbcTemplate.update("delete from tlingx_model where id=?","tlingx_entity");
			IEntity entity=mt.createEntity("tlingx_entity", "模型管理", "admin", 2, "lingx2", this.jdbcTemplate);
			ModelIO.writeEntity( entity, jdbcTemplate);
			

			jdbcTemplate.update("delete from tlingx_model where id=?","tlingx_app");
			IEntity app=mt.createEntity("tlingx_app", "应用管理", "admin", 2, "lingx2", this.jdbcTemplate);
			ModelIO.writeEntity( app, jdbcTemplate);
			

			jdbcTemplate.update("delete from tlingx_model where id=?","tlingx_org");
			IEntity org=mt.createEntity("tlingx_org", "组织管理", "admin", 2, "lingx2", this.jdbcTemplate);
			ModelIO.writeEntity( org, jdbcTemplate);

			jdbcTemplate.update("delete from tlingx_model where id=?","tlingx_menu");
			IEntity menu=mt.createEntity("tlingx_menu", "菜单管理", "admin", 2, "lingx2", this.jdbcTemplate);
			ModelIO.writeEntity( menu, jdbcTemplate);
			
			jdbcTemplate.update("delete from tlingx_model where id=?","tlingx_func");
			IEntity func=mt.createEntity("tlingx_func", "功能管理", "admin", 2, "lingx2", this.jdbcTemplate);
			ModelIO.writeEntity( func, jdbcTemplate);*/

			jdbcTemplate.update("delete from tlingx_model where id=?","tlingx_role");
			IEntity role=mt.createEntity("tlingx_role", "角色管理", "admin", 2, "lingx2", this.jdbcTemplate);
			ModelIO.writeEntity( role, jdbcTemplate);
			//IEntity entity=mt.createEntity("tlingx_option", "系统字典", "admin", 2, "lingx2", this.jdbcTemplate);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public IMethod getOptionItemsMethod(){
		DefaultMethod method=new DefaultMethod();
		method.setCode("items");
		method.setName("按代码取子项");
		ExpressionExecutor de=new ExpressionExecutor();
		JavaScript script=new JavaScript();
		script.setScript("var list=JDBC.queryForList(\"select name text,value from tlingx_optionitem where enabled=1 and option_id in (select id from tlingx_option where code=?) order by orderindex asc \",[code]);list;");
		de.setScript(script);
		method.getExecutors().getList().add(de);
		
		DefaultField field=new DefaultField();
		field.setCode("code");
		field.setName("代码");
		method.getFields().getList().add(field);
		return method;
	}
}
