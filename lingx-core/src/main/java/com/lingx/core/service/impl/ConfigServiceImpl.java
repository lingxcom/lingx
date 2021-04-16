package com.lingx.core.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.lingx.core.service.IConfigService;
import com.lingx.core.service.ILingxService;
import com.lingx.core.utils.Utils;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年8月25日 上午10:05:15 
 * 类说明 
 */
@Component(value="lingxConfigService")
public class ConfigServiceImpl implements IConfigService {
	public static final String LINGX_APPID="335ec1fc-1011-11e5-b7ab-74d02b6b5f61";
	private static final Map<String,String> configs=Collections.synchronizedMap(new HashMap<String,String>());
	@Resource(name="jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	@Resource
	private ILingxService lingxService;
	@PostConstruct
	public void init(){
		this.reset();
	}
	@Override
	public String getValue(String key, String defaultValue) {
		
		return getValue(key,defaultValue,LINGX_APPID);
	}

	@Override
	public String getValue(String key) {
		return this.getValue(key, "");
	}

	@Override
	public int getIntValue(String key, int defaultValue) {
		return Integer.parseInt(getValue(key,String.valueOf(defaultValue)));
	}

	@Override
	public int getIntValue(String key) {
		return this.getIntValue(key, 0);
	}

	@Override
	public void reset() {
		configs.clear();
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select config_key,config_value from tlingx_config where status=1 ");
		for(Map<String,Object> map:list){
			configs.put(map.get("config_key").toString(), map.get("config_value").toString());
		}
	}
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	@Override
	public String getValue(String key, String defaultValue, String appid) {
		if (configs.containsKey(key)) {
			return configs.get(key);
		} else {
			String time=Utils.getTime();
			if(this.lingxService.queryForInt("select count(*) from tlingx_config where config_key=?",key)==0){
				this.jdbcTemplate.update("insert into tlingx_config(id,name,config_key,config_value,status,create_time,modify_time,remark,app_id) values(?,?,?,?,?,?,?,?,?)"
						,lingxService.uuid(),"未设置",key,defaultValue,1,time,time,"自动生成",appid);
			}
			configs.put(key, defaultValue);
			return defaultValue;
		}
	}
	@Override
	public int getIntValue(String key, int defaultValue, String appid) {
		return Integer.parseInt(getValue(key,String.valueOf(defaultValue),appid));
	}

	public void saveValue(String key,String value){
		this.getValue(key, "1");
		jdbcTemplate.update("update tlingx_config set config_value=? where config_key=?",value,key);
		this.reset();
	}
}
