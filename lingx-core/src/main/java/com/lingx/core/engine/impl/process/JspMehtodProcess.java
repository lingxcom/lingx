package com.lingx.core.engine.impl.process;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.lingx.core.Page;
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
@Component(value="JSP")
public class JspMehtodProcess implements IMethodProcess {

	@Override
	public String methodProcess(IMethod method, Map<String, String> params,
			IContext context,IPerformer performer) throws LingxProcessException {
		String jsp=null;
		try {
			jsp = method.getExecutors().getList().get(a).execute(context,performer).toString();
		} catch (Exception e) {
			jsp=this.pageService.getPage(Page.PAGE_NO_PERMISSION);
		}
		return jsp;
	}

	@Resource
	private IPageService pageService;
	public void init(int p1){
		this.a=p1;
	}
	private int a=-1;
}
