package com.lingx.core.model.impl;

import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.exception.LingxScriptException;
import com.lingx.core.model.IScript;
import com.lingx.core.model.IValidator;
import com.lingx.core.model.annotation.FieldModelConfig;
import com.lingx.core.model.annotation.TreeNode;
import com.lingx.core.utils.Utils;

public class ExpressionValidator extends AbstractModel implements IValidator {
	private static final long serialVersionUID = 8668060014174526727L;

	@FieldModelConfig(sort="3",name="返回消息",inputType="string")
	private String message;
	@FieldModelConfig(sort="5",name="验证类型",inputType="string",editor="new Ext.form.field.ComboBox({displayField: 'text',valueField: 'value',store: new Ext.data.Store({proxy:{ model:'Option',type:'ajax',url:'lingx/model/editor/handler.jsp?c=getComboData&type=validater',reader:{type:'json'}},autoLoad:true})})")
	private String type;
	@FieldModelConfig(sort="7",name="验证参数",inputType="string")
	private String param;

	@TreeNode
	private IScript script;
	

	public ExpressionValidator(){	
		super();
		script=new JavaScript();
		this.setModelType("Validator");
		this.setCode(Utils.getRandomString(ID_LEN));
		this.setName("验证器");
		message=DEFAULT_MESSAGE;
		this.type=TYPE_EXPRESSION;
		param="";
		this.setIconCls("icon-4");
	}

	public boolean valid(String code,Object value,String param,IContext context,IPerformer jsper)throws LingxScriptException {
		// TODO Auto-generated method stub
		Object obj=false;
	
			obj = jsper.script(script, context);
		if(obj==null)return true;
		else
		return (Boolean)obj;
	}

	public IScript getScript() {
		return script;
	}

	public void setScript(IScript script) {
		this.script = script;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

}
