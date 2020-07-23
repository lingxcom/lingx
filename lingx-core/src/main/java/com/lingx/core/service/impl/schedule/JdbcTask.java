package com.lingx.core.service.impl.schedule;

import org.springframework.dao.DataAccessException;

/** 
* @author www.lingx.com
* @version 创建时间：2020年6月13日 上午10:24:31 
* 类说明 
*/
public class JdbcTask  extends LingxScheduler{

	private String sqls;
	
	@Override
	public String execute() {
		String array[]=sqls.split(";");
		int count=0;
		int exceptionCount=0;
		for(String sql:array){
			try {
				count+=this.jdbcTemplate.update(sql);
			} catch (Exception e) {
				exceptionCount++;
			}
		}
		return "成功执行影响记录数:"+count+",执行异常数:"+exceptionCount;
	}

	public String getSqls() {
		return sqls;
	}

	public void setSqls(String sqls) {
		this.sqls = sqls;
	}

}
