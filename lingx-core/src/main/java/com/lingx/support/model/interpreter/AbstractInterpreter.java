package com.lingx.support.model.interpreter;

import com.lingx.core.model.IInterpreter;
import com.lingx.core.model.impl.AbstractModel;

public abstract class AbstractInterpreter extends AbstractModel implements IInterpreter {

	private static final long serialVersionUID = -8661065176542670644L;
	private String type;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
