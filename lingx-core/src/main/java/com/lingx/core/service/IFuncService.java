package com.lingx.core.service;
/** 
 * @author www.lingx.com
 * @version 创建时间：2016年2月22日 上午11:50:53 
 * 用户与功能的权限控制
 */
public interface IFuncService {
	/**
	 * 判断当前是否有权限
	 * @param userId
	 * @param funcId
	 * @return
	 */
	boolean getAuth(String userId,String module,String func);
	/**
	 * 刷新缓存，不然新的授权不能及时感知
	 */
	void refresh();
	/**
	 * 按用户进行刷新
	 * @param userId
	 */
	void refresh(String userId);
}
