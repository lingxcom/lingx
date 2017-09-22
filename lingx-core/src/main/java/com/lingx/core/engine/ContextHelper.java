package com.lingx.core.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.lingx.core.model.IEntity;
import com.lingx.core.model.IMethod;
import com.lingx.core.model.bean.UserBean;
import com.lingx.core.utils.Utils;

public final class ContextHelper {

	public static final IContext createContext(final UserBean userBean,final Map<String,String[]> params,final Map<String,Object> attrs,final Map<String,Object> session){

		return new IContext(){
			private IEntity entity;
			private IMethod method;
			private Map<String,Object> messages=new TreeMap<String,Object>();
			private IHttpRequest request=new IHttpRequest(){
				@Override
				public String getParameter(String key) {
					return getParameterByMap(params,key);
				}

				@Override
				public void setAttribute(String key, Object value) {
					attrs.put(key, value);
				}
				@Override
				public String[] getParameterValues(String key) {
					return params.get(key);
				}

				@Override
				public Map<String, Object> getAttributes() {
					return attrs;
				}

				@Override
				public Map<String,String[]> getParameters() {
					return params;
				}

				@Override
				public String getParameter(String key, String defaultValue) {
					String value=getParameterByMap(params,key);
					if(Utils.isNull(value))value=defaultValue;
					return value;
				}

				@Override
				public Set<String> getParameterNames() {
					return params.keySet();
				}

				@Override
				public Object getAttribute(String key) {
					return attrs.get(key);
				}
				
			};
			public IHttpRequest getRequest() {
				return this.request;
			}

			public UserBean getUserBean(){
				return userBean;
			}


			public IMethod getMethod() {
				return method;
			}

			public void setMethod(IMethod method) {
				this.method = method;
			}

			public IEntity getEntity() {
				return entity;
			}

			public void setEntity(IEntity entity) {
				this.entity = entity;
			}

			@Override
			public Map<String, Object> getSession() {
				return session;
			}

			@Override
			public void addMessage(String key, String message) {
				this.messages.put(key, message);
			}

			@Override
			public Map<String, Object> getMessages() {
				return this.messages;
			}



		};
	}
	public static final String getParameterByMap(Map<String,String[]> params,String key){

		String array[]=params.get(key);
		if(array==null){
			return null;
		}else if(array.length==1){
			return array[0];
		}else{
			StringBuilder sb=new StringBuilder();
			for(String s:array){
				sb.append(s).append(",");
			}
			sb.deleteCharAt(sb.length()-1);
			return sb.toString();
		}
	}
}
