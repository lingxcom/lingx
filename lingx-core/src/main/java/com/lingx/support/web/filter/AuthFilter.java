package com.lingx.support.web.filter;

import java.util.Set;

import javax.annotation.Resource;

import com.lingx.core.Constants;
import com.lingx.core.Page;
import com.lingx.core.action.IFilter;
import com.lingx.core.action.IFilterChain;
import com.lingx.core.engine.IContext;
import com.lingx.core.service.IPageService;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年6月12日 上午9:33:24 
 * 类说明 
 */
public class AuthFilter implements IFilter{

	@Resource
	private IPageService pageService;
	@Override
	public String doFilter(IContext context, IFilterChain filterChain) {
		if(context.getSession().get(Constants.SESSION_USER)==null){
			if("login".equals(context.getRequest().getParameter("c")))return filterChain.doFilter(context); 
			
			if("true".equals(context.getRequest().getParameter("is_mobile"))){
				return this.pageService.getPage(Page.PAGE_LOGIN_MOBILE);
			}else{
				return this.pageService.getPage(Page.PAGE_LOGIN);
			}
			
		}
		return filterChain.doFilter(context);
	}

}
