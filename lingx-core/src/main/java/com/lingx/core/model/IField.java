package com.lingx.core.model;


/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月5日 上午11:32:47 
 * 类说明 
 */
public interface IField extends IModel{
	public String getCode();
	public Boolean getFieldSynchro();
	public String getRefEntity();
	public Boolean getIsNotNull();
	public String getName();
	public Object getValue();
	public String getType();
	public String getComboType();
	public Boolean getVisible();
	public String getWidth();
	public String getInputType() ;
	public String getDefaultValue();
	public Boolean getEnabled() ;
	public Boolean getEscape();
	public String getInputOptions();
	//public INode<IConfig> getConfigs();
	public INode<IValidator> getValidators();
	public INode<IInterpreter> getInterpreters();
	public void setName(String name);
	public void setValue(Object value);
	public void setInputType(String inputType);
	public void setDefaultValue(String defaultValue);
	public Boolean getIsEntityLink() ;
}
