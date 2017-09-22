package com.lingx.core.model.impl;

import com.alibaba.fastjson.annotation.JSONField;
import com.lingx.core.model.IField;
import com.lingx.core.model.IInterpreter;
import com.lingx.core.model.INode;
import com.lingx.core.model.IValidator;
import com.lingx.core.model.annotation.FieldModelConfig;
import com.lingx.core.model.annotation.TreeNode;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月6日 上午8:48:21 
 * 类说明 
 */
public class DefaultField extends AbstractModel implements IField{


	private static final long serialVersionUID = 808324787162895921L;
	
	public DefaultField(){
		super();
		this.setCode("");
		this.setName("");
		this.setModelType("Field");
		this.setIconCls("icon-2");
		this.setType("varchar");
		this.setLength("32");
		this.setInputType("textfield");
		this.setInputOptions("");
		this.setComboType("");
		this.setRefEntity("");
		this.setIsNotNull(false);
		this.setFieldSynchro(true);
		this.setEscape(true);
		this.setVisible(true);
		this.setEnabled(true);
		this.setValue(null);
		this.setDefaultValue("");
		this.setWidth(null);
		this.setIsEntityLink(true);
		this.setInterpreters(new DefaultNode<IInterpreter>("解释器"));
		this.setValidators(new DefaultNode<IValidator>("验证器"));
	}
	@FieldModelConfig(sort="11",name="属性代码",inputType="string")
	private String code;

	//@FieldModelConfig(sort="12",name="映射字段",inputType="string")
	//private String dbcode;
	
	@FieldModelConfig(sort="22",name="属性名称",inputType="string")
	private String name;
	@FieldModelConfig(sort="3",name="值",inputType="string",editor="hidden")
	private Object value;
	@FieldModelConfig(sort="4",name="数据类型",inputType="string",editor="new Ext.form.field.ComboBox({displayField: 'text',valueField: 'value',store: new Ext.data.Store({proxy:{ model:'Option',type:'ajax',url:'e?e=tlingx_option&m=items&lgxsn=1&code=SJKLX',reader:{type:'json'}},autoLoad:true})}) ")//e?e=toption&m=items&lgxsn=1&code=BJKKJ
	private String type;//数据库中的类型
	@FieldModelConfig(sort="5",name="长度",inputType="number")
	private String length;//长度
	@FieldModelConfig(sort="6",name="不可为空",inputType="string")
	private Boolean isNotNull;

	//@FieldModelConfig(name="输入控件",inputType="string",editor="{\"type\":\"combobox\",\"options\":{\"url\":\"e?e=toption&m=items&lgxsn=1&code=BJKKJ\"}}")//e?e=toption&m=items&lgxsn=1&code=BJKKJ
	@FieldModelConfig(sort="7",name="输入控件",inputType="string",editor="new Ext.form.field.ComboBox({displayField: 'text',valueField: 'value',store: new Ext.data.Store({proxy:{ model:'Option',type:'ajax',url:'e?e=tlingx_option&m=items&lgxsn=1&code=BJKKJ',reader:{type:'json'}},autoLoad:true})}) ")//e?e=toption&m=items&lgxsn=1&code=BJKKJ
	private String inputType;//指定输入控件
	@FieldModelConfig(sort="8",name="控件参数",inputType="string")
	private String inputOptions;//输入控件的参数
	@FieldModelConfig(sort="9",name="引用类型",inputType="string",editor="new Ext.form.field.ComboBox({displayField: 'text',valueField: 'value',store: new Ext.data.Store({proxy:{ model:'Option',type:'ajax',url:'e?e=tlingx_option&m=items&lgxsn=1&code=YYLX',reader:{type:'json'}},autoLoad:true})}) ")//,{\"text\":\"无\",\"value\":\"\"},
	private String comboType;//1为输入，2为显示
	@FieldModelConfig(sort="A",name="指向对象模型",inputType="string",editor="new Ext.form.field.ComboBox({displayField: 'name',valueField: 'code',store: new Ext.data.Store({proxy:{ model:'Entity',type:'ajax',url:'lingx/model/editor/handler.jsp?c=getEntityListForRef',reader:{type:'json'}},autoLoad:true})}) ")//e?e=tlingx_entity&m=combo&is_ref=1&lgxsn=1
	private String refEntity;//指向哪个实体【数据库表】
	
	@FieldModelConfig(sort="B",name="是否可用",inputType="string")
	private Boolean enabled;
	@FieldModelConfig(sort="C",name="是否可见",inputType="string")
	private Boolean visible;
	@JSONField(serialize=false)
	@FieldModelConfig(sort="D",name="字段同步",inputType="string")
	private Boolean fieldSynchro;
	@JSONField(serialize=false)
	@FieldModelConfig(sort="E",name="默认值",inputType="string")
	private String defaultValue="";

	@FieldModelConfig(sort="F",name="列表宽度",inputType="string")
	private String width="";
	
	@JSONField(serialize=false)
	@FieldModelConfig(sort="G",name="是否转义",inputType="string")
	private Boolean escape;
	
	@JSONField(serialize=false)
	@FieldModelConfig(sort="H",name="是否连接",inputType="string")
	private Boolean isEntityLink;
	
	//@TreeNode
	//private INode<IConfig> configs;
	@JSONField(serialize=false)
	@TreeNode
	private INode<IValidator> validators;
	@JSONField(serialize=false)
	@TreeNode
	private INode<IInterpreter> interpreters;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	/*public String getDbcode() {
		return dbcode;
	}
	public void setDbcode(String dbcode) {
		this.dbcode = dbcode;
	}*/
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public Boolean getIsNotNull() {
		return isNotNull;
	}
	public void setIsNotNull(Boolean isNotNull) {
		this.isNotNull = isNotNull;
	}
	public String getInputType() {
		return inputType;
	}
	public void setInputType(String inputType) {
		this.inputType = inputType;
	}
	public String getInputOptions() {
		return inputOptions;
	}
	public void setInputOptions(String inputOptions) {
		this.inputOptions = inputOptions;
	}
	public String getComboType() {
		return comboType;
	}
	public void setComboType(String comboType) {
		this.comboType = comboType;
	}
	public String getRefEntity() {
		return refEntity;
	}
	public void setRefEntity(String refEntity) {
		this.refEntity = refEntity;
	}
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	public Boolean getVisible() {
		return visible;
	}
	public void setVisible(Boolean visible) {
		this.visible = visible;
	}
	public Boolean getFieldSynchro() {
		return fieldSynchro;
	}
	public void setFieldSynchro(Boolean fieldSynchro) {
		this.fieldSynchro = fieldSynchro;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}/*
	public INode<IConfig> getConfigs() {
		return configs;
	}
	public void setConfigs(INode<IConfig> configs) {
		this.configs = configs;
	}*/
	public INode<IValidator> getValidators() {
		return validators;
	}
	public void setValidators(INode<IValidator> validators) {
		this.validators = validators;
	}
	public INode<IInterpreter> getInterpreters() {
		return interpreters;
	}
	public void setInterpreters(INode<IInterpreter> interpreters) {
		this.interpreters = interpreters;
	}
	public Boolean getEscape() {
		if(this.escape==null)return true;
		return escape;
	}
	public void setEscape(Boolean escape) {
		this.escape = escape;
	}
	public Boolean getIsEntityLink() {
		if(this.isEntityLink==null)return true;
		return isEntityLink;
	}
	public void setIsEntityLink(Boolean isEntityLink) {
		this.isEntityLink = isEntityLink;
	}
	
}
