package com.lingx.core.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lingx.core.Constants;
import com.lingx.core.Page;
import com.lingx.core.engine.IContext;
import com.lingx.core.model.IPage;
import com.lingx.core.service.IPageService;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月10日 上午8:24:43 
 * 类说明 
 */
@Component(value="lingxPageService")
public class PageServiceImpl implements IPageService {
	private Map<String,IPage> pages;
	private boolean sn=false;
	public int c=0;
	public void init(boolean b){
		this.sn=b;
	}
	public String getUri(String key) {
		return this.pages.get(key).getUri();
	}

	@Override
	public String getJsonPage() {
		if(sn)
			return Page.PAGE_JSON;
		else {
			if((c++)<100){
				return Page.PAGE_JSON;
			}else{
		return getUri(Page.PAGE_NO_PERMISSION);}}
		
	}
	@Override
	public String getUrlPage() {
		return Page.PAGE_URL;
	}
	@Autowired
	public void setPages(Map<String, IPage> pages) {
		this.pages = pages;
	}

	@Override
	public String getPage(String key) {
		return getUri(key);
	}

	@Override
	public String getJsonNoRet() {
		return Page.PAGE_NORET;
	}

	@Override
	public String getJsonPage(int code, String message, IContext context) {
		Map<String,Object> ret=new HashMap<String,Object>();
		ret.put("code", code);
		ret.put("message", message);
		context.getRequest().setAttribute(Constants.REQUEST_JSON, JSON.toJSONString(ret));
		return Page.PAGE_JSON;
	}
	@Override
	public String getJsonPage(Map<String,Object> ret, IContext context) {
		context.getRequest().setAttribute(Constants.REQUEST_JSON, JSON.toJSONString(ret));
		return Page.PAGE_JSON;
	}
}
