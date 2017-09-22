package com.lingx.support.model.validator;

import com.lingx.core.model.IValidator;
import com.lingx.core.model.impl.AbstractModel;

public abstract class AbstractValidator extends AbstractModel implements IValidator {

	private static final long serialVersionUID = -533984635732321664L;

	private String type;
	private String message;
	public AbstractValidator(){
		this.message="{}参数无效";
	}
	@Override
	public String getParam() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
