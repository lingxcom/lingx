package com.lingx.core.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;

import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.exception.LingxPluginException;
import com.lingx.core.plugin.IPlugin;
import com.lingx.core.plugin.PluginManager;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月5日 上午11:30:24 
 * lingx框架的核心服务类
 */
public interface ILingxService {
	/**
	 * 获取Spring环境
	 * @return
	 */
	public ApplicationContext getSpringContext();
	/**
	 * 取得配置信息
	 * @param key
	 * @return
	 */
	public String getConfigValue(String key,String defaultValue);
	/**
	 * 取得配置信息
	 * @param key
	 * @return
	 */
	public int getConfigValue(String key,int defaultValue);
	/**
	 * 从Spring容器中取出对象
	 * @param key
	 * @return
	 */
	public Object getBean(String key);
	/**
	 * 根据ID取出系统插件
	 * @param id
	 * @return
	 */
	public IPlugin getPlugin(String id)throws LingxPluginException;
	/**
	 * 取得插件管理器
	 */
	public PluginManager getPluginManager();
	/**
	 * 调用模型中的方法
	 * @param entityCode
	 * @param methodCode
	 * @param params
	 * @return
	 */
	public Object call(String entityCode,String methodCode,Map<String,String> params,IContext context);
	public Object call(String entityCode,String methodCode,Map<String,String> params,HttpServletRequest request);
	/**
	 * 判断当前用户是否是超管人员
	 * @param request
	 * @return
	 */
	public boolean isSuperman(HttpServletRequest request);
	/**
	 * 统计外关联数
	 * @param entityCode
	 * @param id
	 * @return
	 */
	public int countForeignKey(String entityCode, Object id);
	/**
	 * 生成UUID
	 * @return
	 */
	public String uuid();
	/**
	 * 获取时间戳:20150616165723
	 * @return
	 */
	public String ts();
	/**
	 * 获取时间戳:20150616165723
	 * @return
	 */
	public String getTime();
	/**
	 * 密码加密
	 * @param password 密码明文
	 * @param userid 账号明文
	 * @return
	 */
	public String passwordEncode(String password, String userid);
	/**
	 * 根据上下文取出对应规则下的列表，上下文中必须包含实体对象
	 * @param code
	 * @param isDeocde 是否解码、转义
	 * @return
	 */
	public List<Map<String, Object>> getListByEntity(boolean isDeocde, IContext context,IPerformer performer);
	/**
	 * 根据对象代码取出对应规则下的列表
	 * @param ecode
	 * @param isDeocde
	 * @param request
	 * @return
	 */
	public List<Map<String, Object>> getListByEntity(String ecode,boolean isDeocde,HttpServletRequest request);
	/**
	 * 自动获取拼音，如拼音个数超过10个，将自动取首字母
	 * @param temp
	 * @return
	 */
	//public String getPinyin(String temp);
	/**
	 * 基于MD5算法加密
	 * @param temp
	 * @return
	 */
	public String md5(String temp);
	/**
	 * 数组转字符串
	 * @param arr
	 * @return
	 */
	public String arrayToString(String[] arr);
	/**
	 * 字符串转数组
	 * @param str
	 * @return
	 */
	public String[] stringToArray(String str);
	/**
	 * 树型分类与记录之中间表的添加，根据树规则
	 * 例如：addMiddleTableRecord(1,2,"tlingx_user","org_id","user_id","tlingx_org");
	 * @param userId
	 * @param orgId
	 * @param middleTable
	 * @param orgField
	 * @param userField
	 * @param treeTable
	 */
	public void addMiddleTableRecord(Object userId, Object orgId,String middleTable,String orgField,String userField,String treeTable) ;
	/**
	 * 树型分类与记录之中间表的添加，根据树规则
	 * 例如：delMiddleTableRecord(1,2,"tlingx_user","org_id","user_id","tlingx_org");
	 * @param userId
	 * @param orgId
	 * @param middleTable
	 * @param orgField
	 * @param userField
	 * @param treeTable
	 */
	public void delMiddleTableRecord(Object userId, Object orgId,String middleTable,String orgField,String userField,String treeTable) ;
			
	/**
	 * 快速获取执行时返回错误消息，不会关闭当前操作窗口
	 * @param msg
	 * @return
	 */
	public Map<String,Object> getErrorProcessResult(String msg);
}
