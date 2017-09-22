 package com.lingx.plugin.sso.cas;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lingx.core.Constants;
import com.lingx.core.model.bean.UserBean;
import com.lingx.core.service.ISsoCasService;
import com.lingx.core.service.IUserService;
import com.lingx.core.service.impl.ContextServiceImpl;
import com.lingx.core.utils.HttpUtils;
import com.lingx.core.utils.Utils;
import com.lingx.support.web.action.DefaultAction;
@Component(value="lingxSsoCasService")
public class SsoCasServiceImpl implements ISsoCasService {
	private String appid;
	private String casUrl;
	private String authUrl;
	private String logoutUrl;
	@Resource
	private JdbcTemplate jdbcTemplate;
	@Resource
	private IUserService userService;
	@Override
	public String getCasUrl(HttpServletRequest request) {
		String targetUrl=getBackUrl(request);
		//System.out.println("getBackUrl:"+targetUrl);
		StringBuilder sb=new StringBuilder();
		if(this.casUrl.indexOf("?")==-1){
			sb.append(this.casUrl).append("?").append("appid=").append(this.appid).append("&targetUrl=").append(targetUrl);
		}else{
			sb.append(this.casUrl).append("&").append("appid=").append(this.appid).append("&targetUrl=").append(targetUrl);
		}

		//System.out.println("casUrl:"+sb);
		return sb.toString();
	}
	
	public String removeTicketKey(String url){
		url=url.replace("?ticket=", "?ticket_bak=");
		url=url.replace("&ticket=", "&ticket_bak=");
		return url;
	}
	@Override
	public boolean auth(String ticket, HttpServletRequest request) {
		StringBuilder sb=new StringBuilder();
		if(this.authUrl.indexOf("?")==-1){
			sb.append(this.authUrl).append("?").append("appid=").append(this.appid).append("&ticket=").append(ticket);
		}else{
			sb.append(this.authUrl).append("&").append("appid=").append(this.appid).append("&ticket=").append(ticket);
		}
		try {
			String json=HttpUtils.get(sb.toString());
			System.out.println("令牌验证返回："+json);
			Map<String,Object> map=JSON.parseObject(json);
			String account=map.get("account").toString();
			UserBean userBean=this.userService.getUserBean( account, ContextServiceImpl.getRequestClientIP(request));
			request.getSession().setAttribute(Constants.SESSION_USER, userBean);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public void setCasUrl(String casUrl) {
		this.casUrl = casUrl;
	}

	public void setAuthUrl(String authUrl) {
		this.authUrl = authUrl;
	}

	public void setLogoutUrl(String logoutUrl) {
		this.logoutUrl = logoutUrl;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public String getBackUrl(javax.servlet.http.HttpServletRequest request)  {
        String strBackUrl = "";    
        try {
        strBackUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + request.getServletPath();// + "?" +(request.getQueryString()); // this.codeToString   
        String temp=request.getQueryString();
        if(Utils.isNotNull(temp)){
        	strBackUrl=strBackUrl+"?"+temp;
        }

        strBackUrl=removeTicketKey(strBackUrl);
        
        strBackUrl = java.net.URLEncoder.encode(strBackUrl,"UTF-8");
        } catch(Exception e) {
           e.printStackTrace();
        }
        return strBackUrl;
    }
	public  String codeToString(String str) {
        String strString = str;
        try {
            byte tempB[] = strString.getBytes("ISO-8859-1");
            strString = new String(tempB);
            return strString;
        } catch (Exception e) {
            return strString;
        }
    }

	@Override
	public String getLogoutUrl() {
		return this.logoutUrl;
	} 
}
