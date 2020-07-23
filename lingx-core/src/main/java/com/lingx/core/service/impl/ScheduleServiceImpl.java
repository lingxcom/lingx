package com.lingx.core.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import com.lingx.core.service.IScheduleService;
import com.lingx.core.service.impl.schedule.JdbcTask;
import com.lingx.core.service.impl.schedule.LingxScheduler;
import com.lingx.core.utils.Utils;

/** 
* @author www.lingx.com
* @version 创建时间：2020年6月12日 下午4:37:54 
* 类说明 
*/
@Component(value="lingxScheduleServiceImpl")
public class ScheduleServiceImpl implements IScheduleService {
	@Resource(name="jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	@Resource
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;
	@Resource
	private ApplicationContext spring;
    /**
     * 存放动态定时任务结果，用于关闭定时任务
     */
    private Map<Integer, ScheduledFuture> futureMap = new HashMap<>();

	@PostConstruct
	public void init(){
		this.reload();
	}
	
	@Scheduled(cron="0 0/10 * * * ?")
	public void reload(){
		int mapKey=0;
		this.jdbcTemplate.update("update tlingx_schedule set status='2' where type='1' and expression<?",Utils.getTime());
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select * from tlingx_schedule");
		for(Map<String,Object> map:list){
			try {
				mapKey=Integer.parseInt(map.get("id").toString());
				String clazzPath=map.get("clazz_path").toString();
				String sqlText=map.get("sql_text").toString();
				String type=map.get("type").toString();
				String status=map.get("status").toString();
				String expression=map.get("expression").toString();
				if("1".equals(status)){
					if(Utils.isNotNull(clazzPath)){//执行代码
						Class<LingxScheduler> clazz=(Class<LingxScheduler>)Class.forName(clazzPath);
						LingxScheduler ls=clazz.newInstance();
						ls.setId(mapKey);
						ls.setJdbcTemplate(jdbcTemplate);
						ls.setSpring(spring);
						if(!futureMap.containsKey(mapKey)){
							//不存在且状态正常的任务才添加
							startTask(mapKey,ls,type,expression);
						}
					}
					if(Utils.isNotNull(sqlText)){// 执行SQL
						JdbcTask ls=new JdbcTask();
						ls.setId(mapKey);
						ls.setJdbcTemplate(jdbcTemplate);
						ls.setSpring(spring);
						ls.setSqls(sqlText);
						if(!futureMap.containsKey(mapKey)){
							//不存在且状态正常的任务才添加
							startTask(mapKey,ls,type,expression);
						}
					}
				}else{
					//存在
					if("2".equals(status)){//禁用
						if(futureMap.containsKey(mapKey)){
							futureMap.get(mapKey).cancel(true);
							futureMap.remove(mapKey);
							this.log(mapKey, "禁用成功");
						}
					}
					
				}
			} catch (Exception e) {
				this.log(mapKey, "初始化失败："+e.getLocalizedMessage());
			} 
		}
		
	}
	private void startTask(int id,Runnable task,String type,String expression)throws Exception{
		if("1".equals(type)){
			SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
			Date date=sdf.parse(expression);
			//大于当前时间才放到任务堆里
			if(date.getTime()>System.currentTimeMillis()){
				futureMap.put(
						id,
			            threadPoolTaskScheduler.schedule(
			                    new Thread(task),
			                    date
			            )
			    );
				this.log(id, "初始化成功");
			}else{
				this.log(id, "初始化失败，执行时间必须大于当前时间。");
			}
		}else{
			futureMap.put(
					id,
		            threadPoolTaskScheduler.schedule(
		                    new Thread(task),
		                    new CronTrigger(expression)
		            )
		    );
			this.log(id, "初始化成功");
		}
		
	}
	
	private void log(Object id,String message){
		this.jdbcTemplate.update("insert into tlingx_schedule_log(schedule_id,ret,ts) values(?,?,?)",id,message,Utils.getTime());
		
	}
}
