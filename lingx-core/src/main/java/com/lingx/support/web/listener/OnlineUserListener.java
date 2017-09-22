package com.lingx.support.web.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.jdbc.core.JdbcTemplate;

import com.lingx.core.Constants;
import com.lingx.core.utils.Utils;

public class OnlineUserListener implements HttpSessionListener ,ServletRequestListener{
	private HttpServletRequest request;
	public void sessionCreated(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		//Object userObj=session.getAttribute(Constants.SESSION_USER);
		//ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(session.getServletContext());
		//JdbcTemplate jdbc=applicationContext.getBean("jdbcTemplate",JdbcTemplate.class);
		/*if(userObj==null){
			UserBean bean=new UserBean();
			bean.setAccount("未登录");
			bean.setName("未登录");
			bean.setLoginTime(Utils.getTime());
			bean.setLoginIp(LingxUtils.getRequestClientIP(request));
			bean.setLoginCount("0");
			userObj=bean;
		}*/
		//UserBean bean=(UserBean)userObj;
		OnlineUserManager oUser=OnlineUserManager.getOnlineUserManager(session);
		//oUser.addUser(session.getId(), bean);
		oUser.addSession(session);
		//this.addLog1(jdbc,session.getId(), bean.getLoginTime(), bean.getLoginIp());
	}
	private void addLog1(JdbcTemplate jdbc,String sessionID,String time,String ip){
		String sql="insert into tlingx_sessionlog(sessionid,createtime,ip)values(?,?,?)";
		jdbc.update(sql, new Object[]{sessionID,time,ip});
	}
	private void addLog2(JdbcTemplate jdbc,String sessionID){
		String sql="update tlingx_sessionlog set destorytime=? where sessionid=?";
		jdbc.update(sql, new Object[]{Utils.getTime(),sessionID});
	}
	public void sessionDestroyed(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		//ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(session.getServletContext());
		//JdbcTemplate jdbc=applicationContext.getBean("jdbcTemplate",JdbcTemplate.class);
		ServletContext application = session.getServletContext();
		Object obj=application.getAttribute(Constants.APPLICATION_ONLINE_USER);
		OnlineUserManager oUser=(OnlineUserManager)obj;
		///oUser.removeUser(session.getId());
		if(oUser!=null&&session!=null){
			oUser.removeSession(session.getId());
			//this.addLog2(jdbc,session.getId());
		}
		//System.out.println(session.getId() + "超时退出。");

	}

	public void requestDestroyed(ServletRequestEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void requestInitialized(ServletRequestEvent arg0) {
		// TODO Auto-generated method stub
		this.request=(HttpServletRequest)arg0.getServletRequest();
	}

}
