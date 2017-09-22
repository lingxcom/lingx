package com.lingx.core.service.impl.task;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/** 
 * @author www.lingx.com
 * @version 创建时间：2017年1月20日 上午9:10:30 
 * 类说明 
 */
@Component
public class ClearDataJob {
	@Resource(name="jdbcTemplate")
	private JdbcTemplate JdbcTemplate;
	
	@PostConstruct
	public void init(){
		this.execute();
	}
	@Scheduled(cron="0 30 4 * * ?")
	public void execute() {
		this.JdbcTemplate.update("delete from tlingx_userrole where user_id not in (select id from tlingx_user)");
		this.JdbcTemplate.update("delete from tlingx_userrole where role_id not in (select id from tlingx_role)");
		this.JdbcTemplate.update("delete from tlingx_rolefunc where role_id not in (select id from tlingx_role)");
		this.JdbcTemplate.update("delete from tlingx_rolefunc where func_id not in (select id from tlingx_func)");
		this.JdbcTemplate.update("delete from tlingx_userorg  where user_id not in (select id from tlingx_user)");
		this.JdbcTemplate.update("delete from tlingx_userorg  where  org_id not in (select id from tlingx_org)");

	}
	
}
