package com.lingx.core.model;


/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月5日 上午11:32:55 
 * 类说明 
 */
public interface IMethod extends IModel{
	/**
	 * 方法类型
	 * @return
	 */
	public String getType();
	public String getViewUri();
	public Boolean getValidation();
	public Boolean getVisible();
	public Boolean getEnabled();
	public INode<IField> getFields();
	public INode<IExecutor> getExecutors();
	public Boolean getRightmenu();
	public String getBeforeScript();
	public String getBeforeMessage();
}
