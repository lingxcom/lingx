package com.lingx.core.model.impl;

import java.util.ArrayList;
import java.util.List;

import com.lingx.core.model.INode;
import com.lingx.core.model.annotation.TreeNode;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月10日 下午12:45:54 
 * 类说明 
 */
public class DefaultNode<T> extends AbstractModel implements INode<T> {
	
	private static final long serialVersionUID = -2092246062872114298L;

	public DefaultNode(String name){
		super();
		this.setName(name);
		this.setModelType("Node");
		this.setIconCls("icon-6");
		this.list=new ArrayList<T>();
	}
	
	@TreeNode
	private List<T> list;

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}
	

}
