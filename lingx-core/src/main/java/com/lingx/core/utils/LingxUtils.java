package com.lingx.core.utils;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.lingx.core.service.impl.LingxServiceImpl;
import com.sun.management.OperatingSystemMXBean;


public class LingxUtils {

	public static Object format(Object obj){
		return format(1,obj);
	}
	public static Object format(int code,Object obj){
		if(obj instanceof Map){
			return obj;
		}else if(obj instanceof List){
			return obj;
		}else{
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("code", code);
			map.put("message", obj);
			return map;
		}
	}
	public static String getRequestClientIP(HttpServletRequest request){
		String ip=request.getHeader("X-Real-IP");
		if(Utils.isNull(ip)){
			ip=request.getRemoteAddr();
		}
		return ip;
	}

	public static void returnJSON(HttpServletResponse response, Object result) {
		try {
			response.setContentType("text/html;charset=UTF-8");
			response.setStatus(HttpServletResponse.SC_OK);
			String ret = null;
			if(result instanceof String){
				ret =String.valueOf(result);
			}else{
				ret =JSON.toJSONString(result);
			}
			response.getWriter().print(ret);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getBasePath(HttpServletRequest request){
		StringBuilder sb=new StringBuilder();
		sb.append(request.getScheme()).append("://").append(request.getServerName()).append(":" )
		.append(request.getServerPort()).append(request.getContextPath()).append("/");
		return sb.toString();
	}
	/**
	 * 防止 druid的注入检测，检测到sql拼接代码，所以先把1=1之类的先做处理
	 * @param str
	 * @return
	 */
	public static final String sqlInjection(String str){
		str=dblspace(str);
		str=str.replace(" ( 1=1 ) ", " 1=1 ");
		str=dblspace(str);
		str=str.replaceAll(" 1=1 and ", " ");
		str=str.replaceAll(" and 1=1 ", " ");
		str=dblspace(str);
		str=str.replaceAll(" 1=1 or ", " ");
		str=dblspace(str);
		str=str.replaceAll("where 1=1 order by", "order by");
		return str;
	}
	
	static final String dblspace(String temp){
		while(temp.indexOf("  ")>-1){
			temp=temp.replaceAll("  ", " ");
		}
		return temp;
	}
	
	public static final void deleteLastChar(StringBuilder sb){
		if(sb.length()>0){
			sb.deleteCharAt(sb.length()-1);
		}
	}

public static String getEMS() {  
    StringBuffer sb=new StringBuffer();  
    OperatingSystemMXBean osmb = (OperatingSystemMXBean) ManagementFactory  
            .getOperatingSystemMXBean();  
       sb.append("系统物理内存总计：" + osmb.getTotalPhysicalMemorySize()  
            / 1024 / 1024 + "MB<br>");  
       sb.append("系统物理可用内存总计：" + osmb.getFreePhysicalMemorySize()  
            / 1024 / 1024 + "MB");  
    return sb.toString();  
}  
}
