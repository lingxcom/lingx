package com.lingx.core.service;

import com.lingx.core.model.bean.RegexpBean;
import com.lingx.core.model.bean.UserBean;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年9月15日 下午4:29:48 
 * 类说明 
 */
public interface IUserService {
	/**
	 * 添加用户与组织的关联，树形级联
	 * @param userId
	 * @param orgId
	 */
	public void addUserOrg(Object userId, Object orgId);
	/**
	 * 删除用户与组织的关联，树形级联
	 * @param userId
	 * @param orgId
	 */
	public void delUserOrg(Object userId, Object orgId);
	/**
	 * 超级管理员的权限刷新，重新赋予所有权限
	 */
	public void superManagerAuthRefresh();
	/**
	 * 根据用户ID取出邮箱信息数组，用户ID可以多个，以,隔开
	 * @param userid
	 * @return
	 */
	public String[] getEmailByUserId(String userid);
	/**
	 * 获取应用下的所有用户ID
	 * @param appid
	 * @return
	 */
	public String[] getAllUserByAppid(String appid);
	/**
	 * 获取组织及所有子节点的用户ID
	 * @param orgid
	 * @return
	 */
	public String[] getAllUserByOrgid(String orgid);
	/**
	 * 重置用户的隶属组织
	 * @param userId
	 */
	public void resetUserOrg(Object userId);
	/**
	 * 得到Regexp 算法查询右边的值
	 * @param id
	 * @param userBean
	 * @return
	 */
	public RegexpBean getRegexpBean(Object id,UserBean userBean);
	/**
	 * 得到 in 算法查询右边的值 ，in算法效率远远高于 regexp
	 * @param id
	 * @param userBean
	 * @return
	 */
	public RegexpBean getSqlinBean(Object id,UserBean userBean);
	
	public UserBean getUserBean(String userid,String ip);
}
