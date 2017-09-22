package com.lingx.core.engine.impl.process;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lingx.core.Constants;
import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IMethodProcess;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.exception.LingxProcessException;
import com.lingx.core.exception.LingxScriptException;
import com.lingx.core.model.IMethod;
import com.lingx.core.service.IPageService;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月9日 下午5:24:48 
 * 类说明 
 */
@Component(value="JAVASCRIPT")
public class JavascriptMehtodProcess implements IMethodProcess {
	private IPageService pageService;
	@Override
	public String methodProcess(IMethod method, Map<String, String> params,
			IContext context,IPerformer performer) throws LingxProcessException {
		String temp=null;// method.getExecutors().getList().get(0).getScript().getScript();
		try {
			temp=method.getExecutors().getList().get(0).execute(context,performer).toString();
		} catch (LingxScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String,Object> ret=new HashMap<String,Object>();
		ret.put("success", true);
		ret.put("javascript", temp);
		context.getRequest().setAttribute(Constants.REQUEST_JSON, JSON.toJSONString(ret));
		return this.pageService.getJsonPage();
	}
	@Autowired
	public void setPageService(IPageService pageService) {
		this.pageService = pageService;
	}
	public int a=-1;
}
