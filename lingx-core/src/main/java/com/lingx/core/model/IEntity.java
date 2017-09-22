package com.lingx.core.model;


/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月6日 上午8:00:51 
 * 类说明 
 */
public interface IEntity extends IModel {

	public String getCode();
	public String getTableName();
	public String getCascade();
	public String getDisplayMode();
	public String getScript();
	
	public INode<IConfig> getConfigs();
	public INode<IField> getFields();
	public INode<IMethod> getMethods();
	

	public void setCode(String code);
	public void setName(String name);
	public void setTableName(String tableName);
}
