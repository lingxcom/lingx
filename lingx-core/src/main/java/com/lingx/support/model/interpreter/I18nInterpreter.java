package com.lingx.support.model.interpreter;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.lingx.core.SpringContext;
import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.exception.LingxScriptException;
import com.lingx.core.service.II18NService;

/** 
* @author www.lingx.com
* @version 创建时间：2019年9月22日 下午7:47:10 
* 类说明 
*/
public class I18nInterpreter extends AbstractInterpreter{

	private static final long serialVersionUID = -9222432537661501218L;
	private II18NService i18NService=null;
	
	@Override
	public Object input(Object value, IContext context, IPerformer jsper) throws LingxScriptException {
		String ret="";
		if(value!=null){
			ret=value.toString();
		}
		return ret;
	}

	@Override
	public Object output(Object value, IContext context, IPerformer jsper) throws LingxScriptException {
		if(i18NService==null){
			i18NService=SpringContext.getBean(II18NService.class);
		}
		String ret="";
		if(value!=null){
			ret=this.i18NService.textSplit(value, context.getUserBean().getI18n());
		}
		return ret;
	}

}
