package com.lingx.core.model.bean;
/**
 * [{name:'所属用户',entity:'tuser',method:'list',rule:'torguser'}]
 * @author lingx
 *
 */
public class CascaderBean {
	private String name;
	private String entity;
	private String method;
	private String rule;
	private String where;
	public String getWhere() {
		return where;
	}
	public void setWhere(String where) {
		this.where = where;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEntity() {
		return entity;
	}
	public void setEntity(String entity) {
		this.entity = entity;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getRule() {
		return rule;
	}
	public void setRule(String rule) {
		this.rule = rule;
	}
}
