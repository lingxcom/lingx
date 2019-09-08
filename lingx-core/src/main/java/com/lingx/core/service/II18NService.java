package com.lingx.core.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/** 
 * @author www.lingx.com
 * @version 创建时间：2016年9月6日 下午5:11:03 
 * 类说明 
 */
public interface II18NService {
	/**
	 * 设置当前语言
	 * @param lanuage
	 */
	public void setLanuage(String lanuage);
	/**
	 * 判断当前是否有相应的语言包
	 * @param lanuage
	 * @return
	 */
	public boolean hasLanuage(String lanuage);
	/**
	 * 获取转换值
	 * @param text
	 * @return
	 */
	public String getText(String text);
	
	/**
	 * getText的简写
	 * @param text
	 * @return
	 */
	public String text(String text);
	public String text(Object text);

	public String text(String text,String lanuage1) ;
	public String text(String text,HttpSession session);
	public String text(String text,HttpServletRequest request) ;
	/**
	 * 自动根据逗号分隔翻译
	 * @param text
	 * @return
	 */
	public String textSplit(Object text);
	public String textSplit(Object text,String language);
	/**
	 * 获取当前支持的语言列表
	 * @return
	 */
	public List<Map<String,Object>> getLanuages();
	
}
