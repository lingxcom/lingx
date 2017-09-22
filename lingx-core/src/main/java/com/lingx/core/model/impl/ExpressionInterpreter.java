package com.lingx.core.model.impl;

import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.exception.LingxScriptException;
import com.lingx.core.model.IInterpreter;
import com.lingx.core.model.IScript;
import com.lingx.core.model.annotation.FieldModelConfig;
import com.lingx.core.model.annotation.TreeNode;

public class ExpressionInterpreter extends AbstractModel implements IInterpreter{

	private static final long serialVersionUID = 6807459315870008602L;

	@FieldModelConfig(sort="5",name="解释类型",inputType="string",editor="new Ext.form.field.ComboBox({displayField: 'text',valueField: 'value',store: new Ext.data.Store({proxy:{ model:'Option',type:'ajax',url:'lingx/model/editor/handler.jsp?c=getComboData&type=interpreter',reader:{type:'json'}},autoLoad:true})})")
	private String type;
	public ExpressionInterpreter(){		
		super();
		this.setModelType("Interpreter");
		this.setName("解释器");
		type=TYPE_EXPRESSION;
		this.setIconCls("icon-4");
		this.inputScript=new JavaScript("输入处理脚本");
		this.outputScript=new JavaScript("输出处理脚本");
	}

	@TreeNode
	private IScript inputScript;

	@TreeNode
	private IScript outputScript;
	

	@Override
	public Object input(Object value, IContext context, IPerformer performer)
			throws LingxScriptException {
		Object obj=null;
		
		obj = performer.script(this.inputScript, context);
	
		return obj;
	}
	@Override
	public Object output(Object value, IContext context, IPerformer performer)
			throws LingxScriptException {
		Object obj=null;
		
		obj = performer.script(this.outputScript, context);
	
		return obj;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public IScript getInputScript() {
		return inputScript;
	}
	public void setInputScript(IScript inputScript) {
		this.inputScript = inputScript;
	}
	public IScript getOutputScript() {
		return outputScript;
	}
	public void setOutputScript(IScript outputScript) {
		this.outputScript = outputScript;
	}
	

}
