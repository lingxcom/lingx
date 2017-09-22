package com.lingx.core.model.impl;

import com.lingx.core.model.IField;
import com.lingx.core.model.INode;
import com.lingx.core.model.IScript;
import com.lingx.core.model.annotation.TreeNode;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月28日 下午8:26:31 
 * 类说明 
 */
public class QueryEntity extends DefaultEntity {

	private static final long serialVersionUID = 4406122903720842411L;
	public QueryEntity(){
		super();
		this.setType("QueryEntity");
		this.setDisplayMode("grid_query");
		this.setQueryScript(new JavaScript("查询脚本"));
		this.setParams(new DefaultNode<IField>("查询参数"));
	}
	@TreeNode
	private IScript queryScript;
	@TreeNode
	private INode<IField> params;
	
	public IScript getQueryScript() {
		return queryScript;
	}
	public void setQueryScript(IScript queryScript) {
		this.queryScript = queryScript;
	}
	public INode<IField> getParams() {
		return params;
	}
	public void setParams(INode<IField> params) {
		this.params = params;
	}

}
