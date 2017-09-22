package com.lingx.support.web.action;

import com.lingx.core.Constants;
import com.lingx.core.Page;
import com.lingx.core.action.IAction;
import com.lingx.core.engine.IContext;

public abstract class AbstractJsonAction implements IAction {

	public String action(IContext context) {
		String ret=execute(context);
		if(Page.PAGE_NORET.equals(ret)){return ret;}
		context.getRequest().setAttribute(Constants.REQUEST_JSON, ret);
		return Page.PAGE_JSON;
	}

	public abstract String execute(IContext context);
}
