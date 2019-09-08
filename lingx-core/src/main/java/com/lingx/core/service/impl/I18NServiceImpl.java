package com.lingx.core.service.impl;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import com.lingx.core.Constants;
import com.lingx.core.service.II18NService;
import com.lingx.core.utils.Utils;

/** 
 * @author www.lingx.com
 * @version 创建时间：2016年9月6日 下午5:17:36 
 * 类说明 
 */
@Component(value="I18N")
public class I18NServiceImpl implements II18NService {
	/**
	 * 默认语言
	 */
	public static final String DEFAULT_LANUAGE="zh_CN";
	private Map<String,Map<String,String>> lanuages;
	private Map<String,String> currentLanuage;
	private String lanuage;
	
	@PostConstruct
	public void init(){
		this.lanuage=DEFAULT_LANUAGE;
		this.lanuages=new HashMap<String,Map<String,String>>();
		try {
			File dir=org.apache.commons.io.FileUtils.toFile(I18NServiceImpl.class.getResource("/i18n"));
			File[] files=dir.listFiles(new FilenameFilter(){
				@Override
				public boolean accept(File file, String name) {
					return name.endsWith(".txt");
				}});
			for(File file:files){
				this.lanuages.put(file.getName().substring(0,file.getName().length()-4), this.toMap(file));
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}
	@Override
	public void setLanuage(String lanuage) {
		this.lanuage=lanuage;
		if(!DEFAULT_LANUAGE.equals(lanuage)){
			this.currentLanuage=this.lanuages.get(this.lanuage);
		}
	}

	@Override
	public String getText(String text) {
		if(DEFAULT_LANUAGE.equals(lanuage))return text;
		String temp="";
		if(this.currentLanuage.containsKey(text)){
			temp=this.currentLanuage.get(text);
		}else{
			temp=text;
		}
		return temp;
	}

	public String text(String text,String lanuage1) {
		if(Utils.isNull(lanuage1)||DEFAULT_LANUAGE.equals(lanuage1))return text;
		String temp="";
		if(this.lanuages.get(lanuage1).containsKey(text)){
			temp=this.lanuages.get(lanuage1).get(text);
		}else{
			temp=text;
		}
		return temp;
	}
	public String text(String text,HttpSession session) {
		if(session.getAttribute(Constants.SESSION_LANGUAGE)==null)return text;
		String lanuage1=session.getAttribute(Constants.SESSION_LANGUAGE).toString();
		if(Utils.isNull(lanuage1)||DEFAULT_LANUAGE.equals(lanuage1))return text;
		String temp="";
		if(this.lanuages.get(lanuage1).containsKey(text)){
			temp=this.lanuages.get(lanuage1).get(text);
		}else{
			temp=text;
		}
		return temp;
	}
	public String text(String text,HttpServletRequest request) {
		return this.text(text,request.getSession());
	}
	@Override
	public List<Map<String, Object>> getLanuages() {
		List<Map<String, Object>>list=new ArrayList<Map<String, Object>>();
		Map<String, Object> temp=new HashMap<String,Object>();
		temp.put("lanuage",DEFAULT_LANUAGE );
		temp.put("name", "简体中文");
		list.add(temp);
		
		for(String key:this.lanuages.keySet()){
			temp=new HashMap<String,Object>();
			temp.put("lanuage",key );
			temp.put("name", this.lanuages.get(key).get("lanuage"));
			list.add(temp);
		}
		return list;
	}

	private Map<String,String> toMap(File file){
		Map<String,String> map=new HashMap<String,String>();
		try {
			List<String> list=FileUtils.readLines(file, "UTF-8");
			int index=-1;
			String key,value;
			for(String temp:list){
				temp=temp.trim();
				if(temp.startsWith("#"))continue;//注释
				index=temp.indexOf("=");
				if(index==-1)continue;
				key=temp.substring(0,index);
				value=temp.substring(index+1);
				map.put(key, value);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
	@Override
	public String text(String text) {
		return this.getText(text);
	}
	@Override
	public String text(Object text) {
		if(text!=null){
			return this.getText(text.toString());
		}else{
			return null;
		}
		
	}
	@Override
	public boolean hasLanuage(String lanuage) {
		return this.lanuages.containsKey(lanuage);
	}
	@Override
	public String textSplit(Object text) {
		StringBuilder sb=new StringBuilder();
		if(text!=null){
			String array[]=text.toString().split(",");
			for(String str:array){
				sb.append(this.text(str)).append(",");
			}
			if(sb.length()>1)sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();
	}
	@Override
	public String textSplit(Object text, String language) {
		StringBuilder sb=new StringBuilder();
		if(text!=null){
			String array[]=text.toString().split(",");
			for(String str:array){
				sb.append(this.text(str,language)).append(",");
			}
			if(sb.length()>1)sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();
	}
}
