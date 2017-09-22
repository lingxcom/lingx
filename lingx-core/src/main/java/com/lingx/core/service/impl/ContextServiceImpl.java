package com.lingx.core.service.impl;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

import com.lingx.core.Constants;
import com.lingx.core.engine.ContextHelper;
import com.lingx.core.engine.IContext;
import com.lingx.core.model.bean.UserBean;
import com.lingx.core.service.IContextService;
import com.lingx.core.utils.Utils;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年6月29日 上午7:44:37 
 * 类说明 
 */
@Component(value="lingxContextService")
public class ContextServiceImpl implements IContextService {

	@Override
	public UserBean getUserBean(HttpServletRequest request){
		if(request.getSession().getAttribute(Constants.SESSION_USER)!=null){
			return (UserBean)request.getSession().getAttribute(Constants.SESSION_USER);
		}else{
			return null;
		}
	}
	@Override
	public Map<String, String[]> getRequestParameters(HttpServletRequest request) {
		Map<String,String[]> params=new HashMap<String,String[]>();
		params.putAll(request.getParameterMap());
		return params;
	}

	@Override
	public Map<String, Object> getRequestAttributes(HttpServletRequest request) {
		Map<String,Object> params=new HashMap<String,Object>();
		Enumeration<String> keys=request.getAttributeNames();
		String key=null;
		while(keys.hasMoreElements()){
			key=keys.nextElement();
			params.put(key, request.getAttribute(key));
		}
		params.put(IContext.LOCAL_PATH, request.getServletContext().getRealPath("/"));
		params.put(IContext.CLIENT_IP,getRequestClientIP(request));
		return params;
	}

	@Override
	public Map<String, Object> getSesssionAttributes(HttpSession session) {
		Map<String,Object> params=new HashMap<String,Object>();
		Enumeration<String> keys=session.getAttributeNames();
		String key=null;
		while(keys.hasMoreElements()){
			key=keys.nextElement();
			params.put(key, session.getAttribute(key));
		}
		return params;
	}

	public static String getRequestClientIP(HttpServletRequest request){
			String ip=request.getHeader("X-Real-IP");
			if(Utils.isNull(ip)){
				ip=request.getRemoteAddr();
			}
			return ip;
		}
	@Override
	public IContext getContext(HttpServletRequest request) {
		Map<String,Object> requestMap=this.getRequestAttributes(request);
		Map<String,Object> sessionMap=this.getSesssionAttributes(request.getSession());
		IContext context=ContextHelper.createContext(this.getUserBean(request), this.getRequestParameters(request),requestMap,sessionMap);
		return context;
	}
}
