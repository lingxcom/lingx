package com.lingx.core.model;

import java.util.List;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月10日 下午12:11:50 
 * 类说明 
 */
public interface INode<T> extends IModel{
	
	public List<T> getList();
	public void setList(List<T> list);
}
