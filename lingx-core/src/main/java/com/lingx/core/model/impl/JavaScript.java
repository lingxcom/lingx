package com.lingx.core.model.impl;

import com.alibaba.fastjson.annotation.JSONField;
import com.lingx.core.model.IScript;
import com.lingx.core.model.annotation.FieldModelConfig;
import com.lingx.core.utils.Utils;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月9日 下午3:43:55 
 * 类说明 
 */
public class JavaScript  extends AbstractModel implements IScript {
	private static final long serialVersionUID = -2161790470000669894L;
	
	@FieldModelConfig(sort="1",name="代码",inputType="string")
	private String code;
	@FieldModelConfig(sort="2",name="名称",inputType="string")
	private String name;
	@JSONField(serialize=false)
	@FieldModelConfig(sort="3",name="代码",inputType="string",editor="hidden")
	private String script;
	public JavaScript(){
		this.code=Utils.getRandomString(ID_LEN);
		this.setModelType("JavaScript");
		this.name="脚本代码";
		this.script="";
		this.setIconCls("icon-7");
		this.setLeaf(true);
	}
	public JavaScript(String name){
		this();
		this.setName(name);
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
	public String getScript() {
		return script;
	}
	public void setScript(String script) {
		this.script = script;
	}
	

}
