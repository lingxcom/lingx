package com.lingx.core.model.impl;

import com.alibaba.fastjson.annotation.JSONField;
import com.lingx.core.model.IExecutor;
import com.lingx.core.model.IField;
import com.lingx.core.model.IMethod;
import com.lingx.core.model.INode;
import com.lingx.core.model.annotation.FieldModelConfig;
import com.lingx.core.model.annotation.TreeNode;
import com.lingx.core.utils.Utils;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月6日 上午8:53:35 
 * 类说明 
 */
public class DefaultMethod  extends AbstractModel implements IMethod{
	private static final long serialVersionUID = -2621448668063718027L;
	public DefaultMethod(){
		super();
		this.code="";
		this.name="";
		this.viewUri="lingx/template/edit.jsp";
		this.setModelType("Method");
		this.setIconCls("icon-3");
		//this.setOperType("r");
		this.setType("JSON");
		this.setEnabled(true);
		this.setPrompt("");
		this.setConfirm("");
		this.setWinStyle("default");
		this.toField="id";
		this.validation=true;
		this.currentRow=true;
		this.visible=true;
		this.rightmenu=false;
		this.fields=new DefaultNode<IField>("属性");
		this.executors=new DefaultNode<IExecutor>("执行器");
	}
	@FieldModelConfig(sort="11",name="操作代码",inputType="string")
	private String code;
	@FieldModelConfig(sort="22",name="操作名称",inputType="string")
	private String name;
	@FieldModelConfig(sort="3",name="是否验证",inputType="string")
	private Boolean validation;
	@FieldModelConfig(sort="41",name="当前记录",inputType="string")
	private Boolean currentRow;
	@FieldModelConfig(sort="42",name="映射字段",inputType="string")
	private String toField;
	@FieldModelConfig(sort="5",name="返回类型",inputType="string",editor="new Ext.form.field.ComboBox({displayField: 'text',valueField: 'value',store: new Ext.data.Store({proxy:{ model:'Option',type:'ajax',url:'e?e=tlingx_option&m=items&lgxsn=1&code=FHLX',reader:{type:'json'}},autoLoad:true})})")
	private String type;//类型:jsp,json,url,javascript
	@FieldModelConfig(sort="52",name="是否可用",inputType="string")
	private Boolean enabled;
	@FieldModelConfig(sort="51",name="是否可见",inputType="string")
	private Boolean visible;
	@FieldModelConfig(sort="53",name="窗体样式",inputType="string",editor="new Ext.form.field.ComboBox({displayField: 'text',valueField: 'value',store: new Ext.data.Store({proxy:{ model:'Option',type:'ajax',url:'e?e=tlingx_option&m=items&lgxsn=1&code=CTYS',reader:{type:'json'}},autoLoad:true})})")
	private String winStyle;
	@FieldModelConfig(sort="50",name="右键菜单",inputType="string")
	private Boolean rightmenu;
	@FieldModelConfig(sort="6",name="提示消息",inputType="string")
	private String prompt;
	@FieldModelConfig(sort="7",name="操作确认消息",inputType="string")
	private String confirm;
	@FieldModelConfig(sort="81",name="自定义界面",inputType="string")
	private String viewUri;
	@FieldModelConfig(sort="82",name="请求地址")
	private String requestUrl="";
	@FieldModelConfig(sort="91",name="扩展参数",inputType="string")
	private String extParamJson;
	@FieldModelConfig(sort="98",name="前置脚本",inputType="string")
	private String beforeScript;
	@FieldModelConfig(sort="99",name="前置消息",inputType="string")
	private String beforeMessage;
	//@JSONField(serialize=false)
	@TreeNode
	private INode<IField> fields;
	@JSONField(serialize=false)
	@TreeNode
	private INode<IExecutor> executors;

	public String getRequestUrl() {
		return requestUrl;
	}
	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}
	public Boolean getRightmenu() {
		return rightmenu;
	}
	public void setRightmenu(Boolean rightmenu) {
		this.rightmenu = rightmenu;
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
	public Boolean getValidation() {
		return validation;
	}
	public void setValidation(Boolean validation) {
		this.validation = validation;
	}
	public Boolean getCurrentRow() {
		return currentRow;
	}
	public void setCurrentRow(Boolean currentRow) {
		this.currentRow = currentRow;
	}
	public String getToField() {
		return toField;
	}
	public void setToField(String toField) {
		this.toField = toField;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public String getPrompt() {
		return prompt;
	}
	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}
	public String getConfirm() {
		return confirm;
	}
	public void setConfirm(String confirm) {
		this.confirm = confirm;
	}
	public String getViewUri() {
		return viewUri;
	}
	public void setViewUri(String viewUri) {
		this.viewUri = viewUri;
	}
	public INode<IField> getFields() {
		return fields;
	}
	public void setFields(INode<IField> fields) {
		this.fields = fields;
	}
	public INode<IExecutor> getExecutors() {
		return executors;
	}
	public void setExecutors(INode<IExecutor> executors) {
		this.executors = executors;
	}
	public String getExtParamJson() {
		return extParamJson;
	}
	public void setExtParamJson(String extParamJson) {
		this.extParamJson = extParamJson;
	}
	public String getWinStyle() {
		if(Utils.isNull(this.winStyle))return "default";
		else
		return winStyle;
	}
	public void setWinStyle(String winStyle) {
		this.winStyle = winStyle;
	}
	public String getBeforeScript() {
		return beforeScript;
	}
	public void setBeforeScript(String beforeScript) {
		this.beforeScript = beforeScript;
	}
	public String getBeforeMessage() {
		return beforeMessage;
	}
	public void setBeforeMessage(String beforeMessage) {
		this.beforeMessage = beforeMessage;
	}
	
}
