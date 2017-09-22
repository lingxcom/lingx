package com.lingx.core.service;

import java.util.Map;

import com.lingx.core.engine.IContext;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月5日 下午1:34:57 
 * Lingx平台的实现的界面服务，Key为Page.key
 */
public interface IPageService {
	public String getPage(String key);
	public String getJsonPage();
	public String getJsonPage(int code,String message,IContext context);
	public String getJsonPage(Map<String,Object> ret,IContext context);
	public String getJsonNoRet();
	public String getUrlPage();
}
