package com.lingx.support.web.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alibaba.fastjson.JSON;
import com.lingx.core.Constants;
import com.lingx.core.Page;
import com.lingx.core.action.IAction;
import com.lingx.core.action.IActionExecutor;
import com.lingx.core.action.IRequestAware;
import com.lingx.core.action.IResponseAware;
import com.lingx.core.action.ISessionAware;
import com.lingx.core.engine.ContextHelper;
import com.lingx.core.engine.IContext;
import com.lingx.core.model.bean.UserBean;
import com.lingx.core.service.IContextService;
import com.lingx.core.service.ILingxService;
import com.lingx.core.service.ISsoCasService;
import com.lingx.core.service.IUserService;
import com.lingx.core.service.impl.ContextServiceImpl;
import com.lingx.core.utils.Utils;
import com.lingx.support.web.action.DefaultAction;

public class LingxFilter implements Filter{
	public static Logger logger = LogManager.getLogger(LingxFilter.class);
	private ApplicationContext applicationContext;
	private IContextService contextService;
	private IUserService userService;
	private Map<String,IActionExecutor> actions;
	private Map<String,String> actionMap;
	private String encoding;

	public void init(FilterConfig filterConfig) throws ServletException {
		System.setProperty("druid.mysql.usePingMethod","false");
		this.encoding="UTF-8";
		actionMap=new HashMap<String,String>();
		applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(filterConfig.getServletContext());
		actions=this.applicationContext.getBeansOfType(IActionExecutor.class);
		this.contextService=this.applicationContext.getBean(IContextService.class);
		this.userService=this.applicationContext.getBean(IUserService.class);
	}
	public int oauth(HttpServletRequest request,HttpServletResponse response){
		String spath=request.getServletPath();
		//System.out.println("Path:"+spath);
		if(spath.indexOf("no_auth/")>0||spath.indexOf("cas/")>0||spath.endsWith("cas.jsp")||spath.endsWith("api")||spath.endsWith(".js")||spath.endsWith(".css")||spath.endsWith("login.jsp")||spath.equals("")||spath.endsWith("verifyCodeImage")||(spath.endsWith("d")&&("login".equals(request.getParameter("c"))
				||"logout".equals(request.getParameter("c"))))||spath.endsWith(".txt")||spath.endsWith(".png")||spath.endsWith(".jpg")||spath.endsWith(".gif")||spath.endsWith(".ico")||spath.endsWith("api")){
			return 0;
		}
		ILingxService lingxService=this.applicationContext.getBean(ILingxService.class);
		if(request.getParameter("lingx_index_uri")!=null){
			request.getSession().setAttribute("lingx_index_uri", request.getParameter("lingx_index_uri"));
			if(request.getSession().getAttribute(Constants.SESSION_USER)!=null){
				UserBean userBean=(UserBean)request.getSession().getAttribute(Constants.SESSION_USER);
				userBean.getApp().setIndexPage(request.getParameter("lingx_index_uri"));
			}
		}
		
		if(request.getParameter("lingx_user_token")!=null&&request.getSession().getAttribute(Constants.SESSION_USER)==null){
			//开启令牌登陆功能
			if("true".equals(lingxService.getConfigValue("lingx.login.user.token", "false"))){
				try {
					JdbcTemplate jdbc=this.applicationContext.getBean("jdbcTemplate",JdbcTemplate.class);
					String userid=jdbc.queryForObject("select account from tlingx_user where token=?", String.class,request.getParameter("lingx_user_token"));
					String language="zh_CN";
					if(request.getSession().getAttribute(Constants.SESSION_LANGUAGE)!=null){
						language=request.getSession().getAttribute(Constants.SESSION_LANGUAGE).toString();
					}		
							
					UserBean userBean=this.userService.getUserBean(userid, ContextServiceImpl.getRequestClientIP(request),language);
					if(request.getParameter("lingx_index_uri")!=null){
						userBean.getApp().setIndexPage(request.getParameter("lingx_index_uri"));
					}
					
					request.getSession().setAttribute(Constants.SESSION_USER, userBean);
				} catch (Exception e) {
					//e.printStackTrace();
				} 
			}
			
		}
		
		if(request.getParameter("lingx_weixin_id")!=null&&request.getSession().getAttribute(Constants.SESSION_USER)==null){
			//开启微信绑定登陆功能
			if("true".equals(lingxService.getConfigValue("lingx.login.sso.weixin", "false"))){
				ISsoCasService ssoCasService =this.applicationContext.getBean(ISsoCasService.class);
				String ticket=request.getParameter("lingx_weixin_id");
				try{
				if(!ssoCasService.auth(ticket, request)){
					String url=ssoCasService.getCasUrl(request);
					response.sendRedirect(url);
					return 92;
				}
				}catch(Exception e){}
			}
			
		}
		//开启基于CAS的单点登录功能
		if("true".equals(lingxService.getConfigValue("lingx.login.sso.cas", "false"))&&request.getSession().getAttribute(Constants.SESSION_USER)==null){
			try{
				ISsoCasService ssoCasService =this.applicationContext.getBean(ISsoCasService.class);
				String ticket=request.getParameter("ticket");
				if(Utils.isNull(ticket)){
					String url=ssoCasService.getCasUrl(request);
					response.sendRedirect(url);
					return 92;
				}else{
					//String targetUrl=request.getParameter("targetUrl"); boolean b=
					/*
					 * 由于是在目标URL基础上加ticket所以不需要重定向
					 * 但令牌失效需要跳到CAS
					 */
					if(!ssoCasService.auth(ticket, request)){
						String url=ssoCasService.getCasUrl(request);
						response.sendRedirect(url);
						return 92;
					}
					/*if( b&&Utils.isNotNull(targetUrl)){
						//System.out.println("sendRedirect:"+targetUrl);
						response.sendRedirect(targetUrl);
						return 92;
					}*/
				}
			}catch(Exception e){
				
			}
		}
		return 0;
	}
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain filterChain) throws IOException, ServletException {

		ILingxService lingxService=this.applicationContext.getBean(ILingxService.class);
		HttpServletRequest request=(HttpServletRequest)servletRequest;
		HttpServletResponse response=(HttpServletResponse)servletResponse;
		if("true".equals(lingxService.getConfigValue("lingx.security.referer", "false"))){
			String referer = request.getHeader("Referer");
			String baseDomain = lingxService.getConfigValue("lingx.security.referer.domain", "http://127.0.0.1");
			//System.out.println("referer:"+referer+"     domain:"+baseDomain);
			if(referer != null && (!(referer.trim().startsWith(baseDomain)))){
			//System.out.println("请求伪造，回登录页");
			response.sendRedirect(request.getContextPath()+"/login.jsp");
			return;
			}
		}
		/*if("true".equals(lingxService.getConfigValue("lingx.filter.firewall", "false"))){
			Firewall.fire(request);
		}*/
		this.doFilterEncode(servletRequest, servletResponse);//设置字符集，必须放在前面

		if(request.getSession().getAttribute(Constants.SESSION_USER)==null){
			if(oauth(request,response)==92){return;};//特殊登录,92为重定向
		}
		
		if(this.isStaticResource(request)){filterChain.doFilter(servletRequest, servletResponse); return ;}//为静态资源时，跳出些过虑器

		
		//logger.info("---------------------------------------");
		//logger.info(request.getRequestURI());
		
		String key=this.getActionKey(request.getServletPath());
		//request.getSession().setAttribute(Constants.SESSION_USER, context.getUserBean());
		if(key!=null){
			Map<String,Object> requestMap=this.contextService.getRequestAttributes(request);
			Map<String,Object> sessionMap=this.contextService.getSesssionAttributes(request.getSession());
			IContext context=ContextHelper.createContext(this.contextService.getUserBean(request), this.contextService.getRequestParameters(request),requestMap,sessionMap);
			
			IActionExecutor actionExecutor=applicationContext.getBean(key,IActionExecutor.class);//this.actions.get(key);
			IAction action=actionExecutor.getAction();
			if(action instanceof IRequestAware ){
				IRequestAware aware=(IRequestAware)action;
				aware.setRequest(request);
			}
			if(action instanceof IResponseAware ){
				IResponseAware aware=(IResponseAware)action;
				aware.setResponse(response);
			}
			if(action instanceof ISessionAware ){
				ISessionAware aware=(ISessionAware)action;
				aware.setSession(request.getSession());
			}
			String page=actionExecutor.execute(context);
			returnToRequest(requestMap,request);
			returnToSession(sessionMap,request.getSession());
			action=null;
			
			if(Utils.isNull(page))return;
			if(Page.PAGE_JSON.equals(page)){
				if(request.getAttribute(Constants.REQUEST_JSON)==null){
					response.getWriter().print("{}");
				}else{
					response.setContentType("text/html;charset=UTF-8"); 
					response.setStatus(HttpServletResponse.SC_OK);
					response.getWriter().print(request.getAttribute(Constants.REQUEST_JSON));
				}
			}else if(Page.PAGE_URL.equals(page)){
				logger.info(request.getAttribute(Constants.REQUEST_ATTR).toString());
				response.sendRedirect(request.getAttribute(Constants.REQUEST_ATTR).toString());
			}else if(Page.PAGE_NORET.equals(page)){
				//无输出
			}else{
				request.setAttribute(Constants.REQUEST_MESSAGES, context.getMessages());
				request.getRequestDispatcher(page).forward(servletRequest, servletResponse);
			}
			
		}else{
			filterChain.doFilter(servletRequest, servletResponse);
		}
	}
	private boolean isStaticResource(HttpServletRequest request){
		return request.getRequestURI().indexOf(".")!=-1;
	}
	private void returnToRequest(Map<String,Object> attrs,HttpServletRequest servletRequest){
		//Map<String,Object> attrs=lingxRequest.getAttributes();
		for(String subkey:attrs.keySet()){
			servletRequest.setAttribute(subkey, attrs.get(subkey));
		}
		Enumeration<String> keys=servletRequest.getParameterNames();
		String key;
		Map<String,String> map=new HashMap<String,String>();
		while(keys.hasMoreElements()){
			key=keys.nextElement();
			map.put(key, servletRequest.getParameter(key));
		}
		servletRequest.setAttribute(Constants.REQUEST_PARAMS, JSON.toJSONString(map));
	}
	private void returnToSession(Map<String,Object> attrs,HttpSession session){
		//Map<String,Object> attrs=lingxRequest.getAttributes();
		for(String subkey:attrs.keySet()){
			session.setAttribute(subkey, attrs.get(subkey));
		}
	}
	private String getActionKey(String uri){
		if(this.actionMap.containsKey(uri)){
			return this.actionMap.get(uri);
		}else{
			String temp=null;
			Set<String> keys=this.actions.keySet();
			for(String key:keys){
				IActionExecutor ae=this.actions.get(key);
				if(matchFiltersURL(ae.getUrlPatterns(),uri)){
					temp=key;
					break;
				}
			}
			
			return temp;
		}
	}
	public void destroy() {
		
	}
	 private boolean matchFiltersURL(String testPaths[], String requestPath){
		 for(String test:testPaths){
			 if(matchFiltersURL(test,requestPath)){
				 return true;
			 }
		 }
		 return false;
	 }
	 private static boolean matchFiltersURL(String testPath, String requestPath) {
	        if (testPath == null)
	            return false;
	        if (testPath.equals(requestPath))
	            return true;
	        if (testPath.equals("/*"))
	            return true;
	        if (testPath.endsWith("/*")) {
	            if (testPath.regionMatches(0, requestPath, 0,
	                                       testPath.length() - 2)) {
	                if (requestPath.length() == (testPath.length() - 2)) {
	                    return true;
	                } else if ('/' == requestPath.charAt(testPath.length() - 2)) {
	                    return true;
	                }
	            }
	            return false;
	        }
	        if (testPath.startsWith("*.")) {
	            int slash = requestPath.lastIndexOf('/');
	            int period = requestPath.lastIndexOf('.');
	            if ((slash >= 0) && (period > slash)
	                && (period != requestPath.length() - 1)
	                && ((requestPath.length() - period)
	                    == (testPath.length() - 1))) {
	                return (testPath.regionMatches(2, requestPath, period + 1,
	                                               testPath.length() - 2));
	            }
	        }
	        return false; 

	    }
	 private void doFilterEncode(ServletRequest servletRequest, ServletResponse servletResponse){
			try {
				servletRequest.setCharacterEncoding(this.encoding);
				servletResponse.setCharacterEncoding(this.encoding);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
		}


}
