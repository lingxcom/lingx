package com.lingx.core.service;

import java.util.Map;

import com.lingx.core.exception.LingxChooseException;
import com.lingx.core.model.IEntity;
import com.lingx.core.model.IMethod;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月5日 下午3:08:05 
 * 选择器
 */
public interface IChooseService {

	public IEntity getEntity(String code)throws LingxChooseException;

	public IMethod getMethod(String code,IEntity entity);
	
	public boolean defaultMethodContains(String code);
	
	public Map<String, IMethod> getDefaultMethods();
}
