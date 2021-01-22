package com.lingx.core.model.impl;

import com.alibaba.fastjson.annotation.JSONField;
import com.lingx.core.model.IConfig;
import com.lingx.core.model.IEntity;
import com.lingx.core.model.IField;
import com.lingx.core.model.IMethod;
import com.lingx.core.model.INode;
import com.lingx.core.model.annotation.FieldModelConfig;
import com.lingx.core.model.annotation.TreeNode;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月6日 上午8:32:20 
 * 类说明 
 */
public class DefaultEntity extends AbstractModel implements IEntity{

	private static final long serialVersionUID = 831142280388427885L;

	public DefaultEntity(){
		super();
		this.setTableName("");
		this.setModelType("Entity");
		this.setIconCls("icon-1");
		this.setType("DefaultEntity");//普通模型
		this.setCascade("");
		this.setDisplayMode("grid");
		//this.setSynchro(false);
		DefaultNode<IConfig> configs=new DefaultNode<IConfig>("配置");
		configs.getList().add(new GridConfig());
		configs.getList().add(new RuleConfig());
		this.setConfigs(configs);
		this.setFields(new DefaultNode<IField>("属性"));
		this.setMethods(new DefaultNode<IMethod>("操作"));
	}
	
	@FieldModelConfig(sort="1",name="模型代码",inputType="string")
	private String code;
	@FieldModelConfig(sort="2",name="模型名称",inputType="string")
	private String name;
	@JSONField(serialize=false)
	@FieldModelConfig(sort="3",name="模型类型",inputType="string",editor="new Ext.form.field.Display()")
	private String type;
	@JSONField(serialize=false)
	@FieldModelConfig(sort="4",name="数据表名",inputType="string")
	private String tableName;
	@JSONField(serialize=false)
	@FieldModelConfig(sort="5",name="级联模型",inputType="string")
	private String cascade;//editor="{\"type\":\"combobox\",\"options\":{\"multiple\":true,\"url\":\"e?e=tsysentity&m=combo\"}}"
	
	//@FieldModelConfig(sort="6",name="父级模型",inputType="string")
	//private String fcode;
	@JSONField(serialize=false)
	@FieldModelConfig(sort="7",name="查询条件",inputType="string")
	private String script;
	
	@FieldModelConfig(sort="9",name="显示模式",inputType="string",editor="new Ext.form.field.ComboBox({displayField: 'text',valueField: 'value',store: new Ext.data.Store({proxy:{ model:'Option',type:'ajax',url:'e?e=tlingx_option&m=items&lgxsn=1&code=XSMS',reader:{type:'json'}},autoLoad:true})}) ")//,{\"text\":\"无\",\"value\":\"\"},
	private String displayMode;
	
	@JSONField(serialize=false)
	@FieldModelConfig(sort="A",name="关联资源",inputType="string")
	private String pages;
/*	@FieldModelConfig(sort="A",name="左栏宽度",inputType="string")
	private Integer leftWidth;

	@FieldModelConfig(sort="D",name="模型同步",inputType="string")
	private Boolean synchro;*/

	///@FieldModelConfig(sort="A",name="分组字段",inputType="string")
	//private String groupField;
	@JSONField(serialize=false)
	@TreeNode
	private INode<IConfig> configs;
	@JSONField(serialize=false)
	@TreeNode
	private INode<IField> fields;
	@JSONField(serialize=false)
	@TreeNode
	private INode<IMethod> methods;
	
	
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getCascade() {
		return cascade;
	}
	public void setCascade(String cascade) {
		this.cascade = cascade;
	}
	public String getScript() {
		return script;
	}
	public void setScript(String script) {
		this.script = script;
	}
	public String getDisplayMode() {
		return displayMode;
	}
	public void setDisplayMode(String displayMode) {
		this.displayMode = displayMode;
	}
	public INode<IConfig> getConfigs() {
		return configs;
	}
	public void setConfigs(INode<IConfig> configs) {
		this.configs = configs;
	}
	public INode<IField> getFields() {
		return fields;
	}
	public void setFields(INode<IField> fields) {
		this.fields = fields;
	}
	public INode<IMethod> getMethods() {
		return methods;
	}
	public void setMethods(INode<IMethod> methods) {
		this.methods = methods;
	}
	public String getPages() {
		if(pages==null)pages="";
		return pages;
	}
	public void setPages(String pages) {
		this.pages = pages;
	}
	
}
