package com.lingx.core.model.bean;
/** 
 * @author www.lingx.com
 * @version 创建时间：2015年9月17日 下午9:41:36 
 * 类说明 
 */
public class RegexpBean {

	private String currentOrg;
	private String currentRole;
	private String authOrg;
	private String authRole;
	/**
	 * 行政组织及所有子节点ID的拼接
	 */
	private String subOrg;
	/**
	 * 应用组织及所有子节点ID的拼接
	 */
	private String appOrg;
	/**
	 * 角色组织及所有子节点ID的拼接
	 */
	private String roleOrg;
	public String getCurrentOrg() {
		return currentOrg;
	}
	public void setCurrentOrg(String currentOrg) {
		this.currentOrg = currentOrg;
	}
	public String getCurrentRole() {
		return currentRole;
	}
	public void setCurrentRole(String currentRole) {
		this.currentRole = currentRole;
	}
	public String getAuthOrg() {
		return authOrg;
	}
	public void setAuthOrg(String authOrg) {
		this.authOrg = authOrg;
	}
	public String getAuthRole() {
		return authRole;
	}
	public void setAuthRole(String authRole) {
		this.authRole = authRole;
	}
	public String getSubOrg() {
		return subOrg;
	}
	public void setSubOrg(String subOrg) {
		this.subOrg = subOrg;
	}
	public String getAppOrg() {
		return appOrg;
	}
	public void setAppOrg(String appOrg) {
		this.appOrg = appOrg;
	}
	public String getRoleOrg() {
		return roleOrg;
	}
	public void setRoleOrg(String roleOrg) {
		this.roleOrg = roleOrg;
	}
	
}
