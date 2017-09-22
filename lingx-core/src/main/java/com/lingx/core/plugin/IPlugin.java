package com.lingx.core.plugin;

import java.util.Map;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月17日 上午11:10:01 
 * 1、通过IPlugin的execute方法来实现特殊功能
 * 2、通过SPRING的AOP功能，接管系统接口
 * 3、通过事件监听，触发相应业务逻辑
 * 4、通过IOC注入获取平台的所有资源
 * 5、通过Spring注解方式实现任务调度
 */
public interface IPlugin {
	public static final String SQL_INSERT_PLUGIN="insert into tlingx_plugin(id,name,detail,author,version,create_time,modify_time)values(?,?,?,?,?,?,?)";
	public static final String SQL_SELECT_PLUGIN="select * from tlingx_plugin";
	public static final String SQL_SELECT_PLUGIN_BY_ID="select * from tlingx_plugin where id=?";
	public static final String SQL_UPDATE_PLUGIN="update tlingx_plugin set name=?,detail=?,author=?,version=? where id =?";
	//public static final String SQL_DELETE_PLUGIN="delete from tlingx_plugin where id not in (%s)";

	public static final String ID="id";
	public static final String IS_ENABLE="is_enable";
	public static final String OPTIONS="options";
	
	/**
	 * 根据ID来调用插件的功能
	 * @return
	 */
	public String getId();
	/**
	 * 插件名称
	 * @return
	 */
	public String getName();
	/**
	 * 插件详情
	 * @return
	 */
	public String getDetail();
	/**
	 * 插件作者
	 * @return
	 */
	public String getAuthor();
	/**
	 * 版本标识
	 * @return
	 */
	public String getVersion();
	/**
	 * 当前是否可用
	 * @return
	 */
	public boolean isEnable();
	/**
	 * 设置插件的可用性
	 * @param enable
	 */
	public void setEnable(boolean enable);
	/**
	 * 插件初始化
	 * @param params
	 */
	public void init(Map<String,Object> params);
	/**
	 * 启动
	 */
	public void start();
	/**
	 * 停止
	 */
	public void stop();
	/**
	 * 插件销毁
	 */
	public void destory();
	/**
	 * 插件业务执行
	 * @param params
	 * @return
	 */
	public Map<String,Object> execute(Map<String,Object> params);
}
