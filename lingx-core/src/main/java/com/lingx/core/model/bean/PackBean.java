package com.lingx.core.model.bean;

import java.util.List;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年9月26日 上午1:24:29 
 * 类说明 
 */
public class PackBean {
	/**
	 * tlingx_app的ID
	 */
	private String appid;
	/**
	 * 上传密钥
	 */
	private String secret;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 更新内容
	 */
	private String content;
	/**
	 * 作者
	 */
	private String author;
	/**
	 * 字典更新
	 */
	private boolean isOption;
	/**
	 * 是否重启
	 */
	private boolean isReboot;
	/**
	 * 更新类型，1补丁，2功能包，3插件
	 */
	private int type;
	/**
	 * 数据库更新
	 */
	private String sql;
	/**
	 * 模型更新
	 */
	private List<String> modelList;
	/**
	 * 文件更新
	 */
	private List<String> fileList;
	/**
	 * 功能树的JSON
	 */
	private String funcJson;
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public boolean isOption() {
		return isOption;
	}
	public void setOption(boolean isOption) {
		this.isOption = isOption;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public List<String> getModelList() {
		return modelList;
	}
	public void setModelList(List<String> modelList) {
		this.modelList = modelList;
	}
	public List<String> getFileList() {
		return fileList;
	}
	public void setFileList(List<String> fileList) {
		this.fileList = fileList;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public boolean isReboot() {
		return isReboot;
	}
	public void setReboot(boolean isReboot) {
		this.isReboot = isReboot;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getFuncJson() {
		return funcJson;
	}
	public void setFuncJson(String funcJson) {
		this.funcJson = funcJson;
	}
}
