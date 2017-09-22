package com.lingx.core.model.impl;

import com.alibaba.fastjson.annotation.JSONField;
import com.lingx.core.model.IConfig;
import com.lingx.core.model.annotation.FieldModelConfig;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月10日 上午11:31:35 
 * 类说明 
 */
public class RuleConfig  extends AbstractModel implements IConfig {

	private static final long serialVersionUID = 8500058144499092483L;

	public RuleConfig(){
		super();
		this.setModelType("RuleConfig");
		this.setIconCls("icon-8");
		this.setName("权限规则");
		this.setDataRule("");
		this.setLeaf(true);
	}
	@JSONField(serialize=false)
	@FieldModelConfig(name="数据权限",inputType="string")
	private String dataRule;

	public String getDataRule() {
		return dataRule;
	}

	public void setDataRule(String dataRule) {
		this.dataRule = dataRule;
	}


	
}
