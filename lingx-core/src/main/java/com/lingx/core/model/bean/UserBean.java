package com.lingx.core.model.bean;
/**
 * 
*    
* 项目名称：lingx-core   
* 类名称：UserBean   
* 类描述：   
* 创建人：lingx   
* 创建时间：2015年6月18日 上午10:03:06   
* 修改人：lingx   
* 修改时间：2015年6月18日 上午10:03:06   
* 修改备注：   
* @version    
*
 */
public class UserBean {

	private String id;
	private String account;
	private String name;
	private String password;
	private String tel;
	private String email;
	private String remark;
	private String loginTime;
	private String loginIp;
	private String loginCount;
	private Integer status;
	private String token;
	private String i18n;
	private AppBean app;
	private RegexpBean regexp;
	private RegexpBean sqlin;//由于  in的执行效率远远高于regexp所以实现该功能，值包括()
	
	private String orgId;
	private String orgName;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}
	public String getLoginIp() {
		return loginIp;
	}
	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public AppBean getApp() {
		return app;
	}
	public void setApp(AppBean app) {
		this.app = app;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public RegexpBean getRegexp() {
		return regexp;
	}
	public void setRegexp(RegexpBean regexp) {
		this.regexp = regexp;
	}
	public void setLoginCount(String loginCount) {
		this.loginCount = loginCount;
	}
	public String getLoginCount() {
		return loginCount;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public RegexpBean getSqlin() {
		return sqlin;
	}
	public void setSqlin(RegexpBean sqlin) {
		this.sqlin = sqlin;
	}
	public String getI18n() {
		return i18n;
	}
	public void setI18n(String i18n) {
		this.i18n = i18n;
	}
	
}
