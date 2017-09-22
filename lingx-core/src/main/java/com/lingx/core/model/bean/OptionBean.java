package com.lingx.core.model.bean;

import java.util.List;

public class OptionBean {
	public OptionBean(){
		
	}
	private String name;
	private String code;
	private String appid;
	private List<ItemBean> items;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public List<ItemBean> getItems() {
		return items;
	}
	public void setItems(List<ItemBean> items) {
		this.items = items;
	}
	
}
