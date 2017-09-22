package com.lingx.support.web.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import com.lingx.core.Constants;
import com.lingx.core.model.bean.UserBean;

public class OnlineUserManager {

	private Map<String, UserBean> map;
	private Map<String,HttpSession> sessionList;

	public OnlineUserManager() {
		map = new HashMap<String, UserBean>();
		sessionList = new HashMap<String, HttpSession>();
	}

	public void addUser(String sessionID, UserBean bean) {
		this.map.put(sessionID, bean);
	}
	public void addSession(HttpSession session){
		sessionList.put(session.getId(), session);
	}
	public void invalidateSession(String sessionID){
		this.sessionList.get(sessionID).invalidate();
	}
	public void removeSession(String sessionID){
		if(this.sessionList.containsKey(sessionID)){
			//HttpSession session=this.sessionList.get(sessionID);
			this.sessionList.remove(sessionID);
			this.map.remove(sessionID);
			
		}
	}
	public void updateUser(String sessionID, UserBean bean) {
		this.map.put(sessionID, bean);
	}
	public void removeUser(String sessionID) {
		this.map.remove(sessionID);
	}

	public List<UserBean> getListUser() {
		for(String key:sessionList.keySet()){
			HttpSession session=this.sessionList.get(key);
			if(session.getAttribute(Constants.SESSION_USER)!=null){
				this.map.put(key, (UserBean)session.getAttribute(Constants.SESSION_USER));
			}
		}
		for(String key:map.keySet()){
			map.get(key).setRemark(key);
		}
		List<UserBean> list = new ArrayList<UserBean>();
		list.addAll(map.values());
		return list;
	}
	
	public static OnlineUserManager getOnlineUserManager(HttpSession session){
		ServletContext application = session.getServletContext();
		Object obj=application.getAttribute(Constants.APPLICATION_ONLINE_USER);
		if(obj==null){
			OnlineUserManager oUser=new OnlineUserManager();
			application.setAttribute(Constants.APPLICATION_ONLINE_USER, oUser);
			obj=oUser;
		}
		return (OnlineUserManager)obj;
	}
}
