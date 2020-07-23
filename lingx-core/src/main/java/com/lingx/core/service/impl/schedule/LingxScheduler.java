package com.lingx.core.service.impl.schedule;

import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import com.lingx.core.utils.Utils;

/** 
* @author www.lingx.com
* @version 创建时间：2020年6月12日 下午5:15:35 
* 类说明 
*/
public abstract class LingxScheduler implements Runnable {
	
	protected int id;
	protected ApplicationContext spring;
	protected JdbcTemplate jdbcTemplate;
	public LingxScheduler(){}
	public LingxScheduler(int id,JdbcTemplate jdbcTemplate,ApplicationContext spring){
		this.id=id;
		this.jdbcTemplate=jdbcTemplate;
		this.spring=spring;
	}
	@Override
	public void run() {
		String ret="";
		try {
			ret=this.execute();
		} catch (Exception e) {
			ret=e.getLocalizedMessage();
		}
		if(Utils.isNull(ret))ret="执行成功";
		this.jdbcTemplate.update("insert into tlingx_schedule_log(schedule_id,ret,ts) values(?,?,?)",id,ret,Utils.getTime());
	}
	/**
	 * 
	 * @return 返回执行结果
	 */
	public abstract String execute();
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public ApplicationContext getSpring() {
		return spring;
	}
	public void setSpring(ApplicationContext spring) {
		this.spring = spring;
	}
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	
}
