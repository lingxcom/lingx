package com.lingx.support.web.filter;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.lingx.core.Page;
import com.lingx.core.action.IFilter;
import com.lingx.core.action.IFilterChain;
import com.lingx.core.engine.IContext;
import com.lingx.core.model.bean.UserBean;
import com.lingx.core.service.IFuncService;
import com.lingx.core.service.IPageService;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年6月17日 下午2:21:19 
 * 类说明 
 */
public class ModelFilter  implements IFilter{

	@Resource
	private JdbcTemplate jdbcTemplate;
	@Resource
	private IPageService pageService;
	@Resource
	private IFuncService funcService;
	@Override
	public String doFilter(IContext context, IFilterChain filterChain) {
		UserBean user = context.getUserBean();
		if(user==null){
			return this.pageService.getPage(Page.PAGE_TIMEOUT);
		}
		String entityCode = context.getRequest().getParameter("e");
		String methodCode = context.getRequest().getParameter("m");
		boolean isAuth=this.funcService.getAuth(user.getId(), entityCode, methodCode);
		String page="";
		if(isAuth){
			page=filterChain.doFilter(context);
		}else{
			page=this.pageService.getPage(Page.PAGE_NO_PERMISSION);
		}
		return page;
	}

}
