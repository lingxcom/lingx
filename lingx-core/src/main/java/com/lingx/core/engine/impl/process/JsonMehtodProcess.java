package com.lingx.core.engine.impl.process;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lingx.core.Constants;
import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IMethodProcess;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.exception.LingxProcessException;
import com.lingx.core.exception.LingxScriptException;
import com.lingx.core.model.IExecutor;
import com.lingx.core.model.IMethod;
import com.lingx.core.service.II18NService;
import com.lingx.core.service.IPageService;
import com.lingx.core.utils.LingxUtils;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月9日 下午5:24:48 
 * 类说明 
 */
@Component(value="JSON")
public class JsonMehtodProcess implements IMethodProcess {
	@Resource
	private IPageService pageService;
	@Resource
	private II18NService i18nService;
	@Override
	public String methodProcess(IMethod method, Map<String, String> params,
			IContext context,IPerformer performer) throws LingxProcessException {
		Object ret=null;
		try {
			for(IExecutor exe:method.getExecutors().getList()){
				performer.addParam("ExeRet", ret);
				ret=exe.execute(context,performer);
				if(ret instanceof String){
					ret=this.i18nService.text(ret.toString(), context.getUserBean().getI18n());
				}
			}
			context.getRequest().setAttribute(Constants.REQUEST_JSON, JSON.toJSONString(LingxUtils.format(ret)));
		} catch (LingxScriptException e) {
			e.printStackTrace();
			throw new LingxProcessException(e.getMessage());
		}
		return this.pageService.getJsonPage();
	}
	public void setPageService(IPageService pageService) {
		this.pageService = pageService;
	}
	
}
