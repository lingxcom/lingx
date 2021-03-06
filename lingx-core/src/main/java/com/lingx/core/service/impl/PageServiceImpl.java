package com.lingx.core.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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
	@Resource(name="jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	private Map<String,IPage> pages;
	
	public String getUri(String key) {
		return this.pages.get(key).getUri();
	}

	@Override
	public String getJsonPage() {
		return Page.PAGE_JSON;
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
	@Override
	public void genExtStyle(String basePath) {
		String template=".%s{ background:url('../%s') no-repeat !important;}";
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select path,code from tlingx_icon");
		StringBuilder sb =new StringBuilder();
		for(Map<String,Object> map:list){
			sb.append(String.format(template, map.get("code"), map.get("path"))).append("\r\n");
		}
		try {
			FileUtils.write(new File(basePath+"/js/lingx-ext-icon.css"), sb.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
