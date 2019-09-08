package com.lingx.core.service.impl.task;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.lingx.Lingx;
import com.lingx.core.service.ILingxService;
import com.lingx.core.utils.HttpUtils;

/** 
 * @author www.lingx.com
 * @version 创建时间：2017年1月20日 上午10:07:32 
 * 类说明 
 */
@Component
public class AutoUpdateJob {
	@Resource(name="jdbcTemplate")
	private JdbcTemplate JdbcTemplate;
	@Resource
	private ILingxService lingxService;
	@Scheduled(cron="0 30 4 * * ?")
	public void execute() {
		if("true".equals(this.lingxService.getConfigValue("lingx.update.auto", "true"))){
			
		}
		//HttpUtils.get("http://www.lingx.com?version="+Lingx.VERSION);
	}
}
