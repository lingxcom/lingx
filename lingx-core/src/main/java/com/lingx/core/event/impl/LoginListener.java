package com.lingx.core.event.impl;

import javax.annotation.Resource;

import org.springframework.context.ApplicationListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.lingx.core.event.LoginEvent;

/** 
 * @author www.lingx.com
 * @version 创建时间：2017年5月16日 下午5:55:27 
 * 类说明 
 */
@Component
public class LoginListener implements ApplicationListener<LoginEvent>  {
	@Resource
	private JdbcTemplate jdbcTemplate;
	@Override
	public void onApplicationEvent(LoginEvent event) {
		if(this.jdbcTemplate.queryForObject("select count(*) from tlingx_file_category where user_id=?",Integer.class,event.getUserBean().getId())==0){
			this.jdbcTemplate.update("insert into tlingx_file_category(name,count_num,user_id,fid) values(?,?,?,?)","常用文件",0,event.getUserBean().getId(),0);
		}
	}

}
