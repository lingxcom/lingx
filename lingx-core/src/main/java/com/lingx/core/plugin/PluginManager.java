package com.lingx.core.plugin;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lingx.core.exception.LingxPluginException;
import com.lingx.core.utils.Utils;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年7月6日 下午10:38:08 
 * 类说明 
 */
@Component 
public class PluginManager {
	public static Logger logger = LogManager.getLogger(PluginManager.class);
	@Resource
	private List<IPlugin> plugins;
	@Resource
	private JdbcTemplate jdbcTemplate;
	/**
	 * 初始化
	 */
	@PostConstruct 
	public void init(){
		try {
			StringBuilder ids=new StringBuilder();
			List<Map<String,Object>> list=this.jdbcTemplate.queryForList(IPlugin.SQL_SELECT_PLUGIN);

			for(IPlugin plugin:this.plugins){
				logger.info("Plugin Init:"+plugin.getId()+","+plugin.getName());
				Map<String,Object> params=getParams(plugin.getId(),list);
				if(params==null){
					String time=Utils.getTime();
					this.jdbcTemplate.update(IPlugin.SQL_INSERT_PLUGIN,plugin.getId(),plugin.getName(),plugin.getDetail(),plugin.getAuthor(),plugin.getVersion(),time,time);
				}else{
					this.jdbcTemplate.update(IPlugin.SQL_UPDATE_PLUGIN,plugin.getName(),plugin.getDetail(),plugin.getAuthor(),plugin.getVersion(),plugin.getId());
					configPlugin(plugin,params);//参数设置
				}
				ids.append("'").append(plugin.getId()).append("',");
			}
			
			if(ids.length()>0){
				//拔出插件后，也不删除配置 2016-04-28
				//this.jdbcTemplate.update(String.format(IPlugin.SQL_DELETE_PLUGIN, ids.deleteCharAt(ids.length()-1)));
			}
			list.clear();
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 取出数据库的参数
	 * @param id
	 * @param list
	 * @return
	 */
	private Map<String,Object> getParams(String id,List<Map<String,Object>> list){
		Map<String,Object> map=null;
		for(Map<String,Object> m:list){
			if(id.equals(m.get(IPlugin.ID).toString())){
				map=m;
			}
		}
		
		return map;
	}
	private void configPlugin(IPlugin plugin,Map<String,Object> params){
		try {
			plugin.init((Map<String,Object>)JSON.parse(params.get(IPlugin.OPTIONS).toString()));
			plugin.setEnable("true".equals(params.get(IPlugin.IS_ENABLE).toString()));
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			//e.printStackTrace();
		}
	}
	/**
	 * 取出插件
	 * @param id
	 * @return
	 * @throws LingxPluginException
	 */
	public IPlugin getPlugin(String id)throws LingxPluginException{
		return getPlugin(id,true);
	}
	/**
	 * 取出插件
	 * @param id
	 * @param checkEnable
	 * @return
	 * @throws LingxPluginException
	 */
	public IPlugin getPlugin(String id,boolean checkEnable)throws LingxPluginException{
		IPlugin plugin=null;
		for(IPlugin p:this.plugins){
			if(p.getId().equals(id)){
				plugin=p;
				break;
			}
		}
		if(plugin==null){throw new LingxPluginException("插件不存在"); }
		if(!plugin.isEnable()&&checkEnable){throw new LingxPluginException("插件禁用"); }
		return plugin;
	}
	/**
	 * 刷新插件设置
	 * @param id
	 * @return
	 */
	public boolean refreshPlugin(String id)throws LingxPluginException{
		IPlugin plugin=this.getPlugin(id,false);
		Map<String,Object> params=this.jdbcTemplate.queryForMap(IPlugin.SQL_SELECT_PLUGIN_BY_ID,id);
		this.configPlugin(plugin, params);
		return true;
	}
	/**
	 * 注销
	 */
	@PreDestroy
	public void destroy(){
		for(IPlugin plugin:this.plugins){
			plugin.destory();
		}
		plugins.clear();
	}

	public void setPlugins(List<IPlugin> plugins) {
		this.plugins = plugins;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
}
