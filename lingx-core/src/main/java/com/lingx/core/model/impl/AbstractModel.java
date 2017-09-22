package com.lingx.core.model.impl;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;
import com.lingx.core.model.IModel;
import com.lingx.core.model.annotation.FieldModelConfig;
import com.lingx.core.utils.Utils;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月5日 上午11:34:40 
 * 类说明 
 */
public abstract class AbstractModel implements IModel,Serializable{

	private static final long serialVersionUID = -7345301259049496644L;

	@FieldModelConfig(sort="11",name="代码",inputType="string")
	private String code;
	@FieldModelConfig(sort="22",name="名称",inputType="string")
	private String name;

	//@JSONField(serialize=false)
	@FieldModelConfig(sort="za",name="备注",inputType="string")
	private String comment;
	@JSONField(serialize=false)
	@FieldModelConfig(sort="zb",name="图标样式",inputType="string")
	private String iconCls="";//在树型控件用到

	@JSONField(serialize=false)
	@FieldModelConfig(sort="zc",name="模型类型",inputType="string",editor="new Ext.form.field.Display()")
	private String modelType;
	@JSONField(serialize=false)
	@FieldModelConfig(sort="zd",name="模型ID",inputType="string",editor="hidden")
	private String id;
	@JSONField(serialize=false)
	@FieldModelConfig(sort="ze",name="作者",inputType="string",editor="new Ext.form.field.Display()")
	private String author;
	@JSONField(serialize=false)
	@FieldModelConfig(sort="zf",name="创建时间",inputType="string",editor="new Ext.form.field.Display()")
	private String createTime;
	@JSONField(serialize=false)
	@FieldModelConfig(sort="zg",name="最后修改时间",inputType="string",editor="new Ext.form.field.Display()")
	private String lastModifyTime;
	@JSONField(serialize=false)
	@FieldModelConfig(sort="zh",name="版本",inputType="string",editor="hidden")
	private String version;
	@JSONField(serialize=false)
	@FieldModelConfig(sort="zz",name="是否叶子节点",editor="hidden")
	private Boolean leaf;
	public AbstractModel(){
		String time=Utils.getTime();
		this.code=Utils.getRandomString(ID_LEN);
		this.name="";
		this.createTime=time;
		this.lastModifyTime=time;
		this.version="1.0.0";
		this.author="Lingx";
		this.id=Utils.getRandomString(ID_LEN);
		this.comment="";
		this.leaf=false;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public String getModelType() {
		return modelType;
	}

	public void setModelType(String modelType) {
		this.modelType = modelType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(String lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getLeaf() {
		return leaf;
	}

	public void setLeaf(Boolean leaf) {
		this.leaf = leaf;
	}

}
