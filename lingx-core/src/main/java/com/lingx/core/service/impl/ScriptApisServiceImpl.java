package com.lingx.core.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lingx.core.model.IScriptApi;
import com.lingx.core.service.IScriptApisService;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月9日 下午9:44:24 
 * 类说明 
 */
@Component(value="lingxScriptApisService")
public class ScriptApisServiceImpl implements IScriptApisService {
	private Map<String,IScriptApi> apis;
	private Map<String, Object> apiMap;
	public ScriptApisServiceImpl(){
		this.apiMap=new HashMap<String,Object>();
	}
	@Override
	public Map<String, Object> getScriptApis() {
		if(apiMap.size()==0){
			for(String key:this.apis.keySet()){
				this.apiMap.put(key, this.apis.get(key));
			}
		}
		return apiMap;
	}
	@Autowired
	public void setApis(Map<String, IScriptApi> apis) {
		this.apis = apis;
	}
	
}
