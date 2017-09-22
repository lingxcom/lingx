package com.lingx.core.model.bean;
/**
 * 
*    
* 项目名称：lingx-core   
* 类名称：AppBean   
* 类描述：   
* 创建人：lingx   
* 创建时间：2015年6月18日 上午10:03:17   
* 修改人：lingx   
* 修改时间：2015年6月18日 上午10:03:17   
* 修改备注：   
* @version    
*
 */
public class AppBean {

	private String id;
	private String name;
	private String logo;
	private String company;
	private String tel;
	private String email;
	private String indexPage;
	private String viewModel;
	private String orgRootId;
	private String roleRootId;
	private String funcRootId;
	private String menuRootId;
	public String getIndexPage() {
		return indexPage;
	}
	public void setIndexPage(String indexPage) {
		this.indexPage = indexPage;
	}
	public String getViewModel() {
		return viewModel;
	}
	public void setViewModel(String viewModel) {
		this.viewModel = viewModel;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
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
	public String getOrgRootId() {
		return orgRootId;
	}
	public void setOrgRootId(String orgRootId) {
		this.orgRootId = orgRootId;
	}
	public String getRoleRootId() {
		return roleRootId;
	}
	public void setRoleRootId(String roleRootId) {
		this.roleRootId = roleRootId;
	}
	public String getFuncRootId() {
		return funcRootId;
	}
	public void setFuncRootId(String funcRootId) {
		this.funcRootId = funcRootId;
	}
	public String getMenuRootId() {
		return menuRootId;
	}
	public void setMenuRootId(String menuRootId) {
		this.menuRootId = menuRootId;
	}
}
