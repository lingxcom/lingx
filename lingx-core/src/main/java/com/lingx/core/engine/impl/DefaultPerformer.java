package com.lingx.core.engine.impl;

import java.util.Map;
import java.util.Set;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IHttpRequest;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.exception.LingxScriptException;
import com.lingx.core.model.IScript;
import com.lingx.core.model.IScriptApi;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月5日 下午4:11:05 
 * 类说明 
 */
public class DefaultPerformer implements IPerformer{
	public static Logger logger = LogManager.getLogger(DefaultPerformer.class);
	//private static final long serialVersionUID = -2309670081720734059L;
	private ScriptEngine engine;
	
	
	public DefaultPerformer(Map<String,Object> apis,IHttpRequest request) {
		ScriptEngineManager sem = new ScriptEngineManager();
		engine = sem.getEngineByName("JavaScript");
		engine.put("LOG", logger);
		for(String key:apis.keySet()){
			Object obj= apis.get(key);
			if(obj instanceof IScriptApi){
				IScriptApi api=(IScriptApi)obj;
				engine.put(key,api.getApi());
			}else{

				engine.put(key,obj);
			}
		}
		if(request!=null){
			Set<String> keys=request.getParameterNames();
			for(String key:keys){
				engine.put(key, request.getParameter(key));
			}
		}
	}


	public Object script(IScript script, IContext context)
			throws LingxScriptException {
		return this.script(script.getScript(), context);
	}


	public Object script(String script, IContext context)
			throws LingxScriptException {
		if(context!=null){
			engine.put("CUser", context.getUserBean());
		}
		Object obj = null;
		try {
			obj = engine.eval(script);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LingxScriptException(e.getLocalizedMessage(),e);
		}
		return obj;
	}

	public Object script(String script)
			throws LingxScriptException {
		return this.script(script, null);
	}

	public void addParam(Map<String, Object> map) {
		if (map == null)
			return;
		for (String key : map.keySet()) {
			this.addParam(key, map.get(key));
		}
	}

	public void addParam(String key, Object val) {
		this.engine.put(key, val);
	}


	@Override
	public void addRequestParam(Map<String, String> map) {
		if (map == null)
			return;
		for (String key : map.keySet()) {
			this.addParam(key, map.get(key));
		}
		
	}


	@Override
	public void addParam(IContext context) {
		if(context==null)return ;
		Set<String> sets=context.getRequest().getParameterNames();
		for(String temp:sets){
			this.addParam(temp,context.getRequest().getParameter(temp));
		}
		
	}

}
