package com.lingx.core.model;


/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月5日 上午11:32:06 
 * 类说明 
 */
public interface IModel {

	public static final int ID_LEN=16;
	public String getId();
	public String getCode();
	public String getName();
	public String getIconCls();
	public Boolean getLeaf();
	public String getModelType();
	public void setId(String id);
	public void setAuthor(String author);
	public void setLastModifyTime(String lastModifyTime);
}
