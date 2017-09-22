package com.lingx.core.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lingx.core.model.bean.AppBean;
import com.lingx.core.model.bean.RegexpBean;
import com.lingx.core.model.bean.UserBean;
import com.lingx.core.service.IUserService;
import com.lingx.core.utils.Utils;
import com.lingx.support.web.action.DefaultAction;

/**
 * @author www.lingx.com
 * @version 创建时间：2015年9月15日 下午4:30:04 类说明
 */
@Component(value="lingxUserService")
public class UserServiceImpl implements IUserService {

	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	@Override
	public void addUserOrg(Object userId, Object orgId) {
		try {
			if (isExistsUserOrg(userId, orgId))
				return;
			String sql = "insert into tlingx_userorg(id,org_id,user_id) values(uuid(),?,?)";
			this.jdbcTemplate.update(sql, orgId, userId);
			String fid = this.jdbcTemplate.queryForObject(
					"select fid from tlingx_org where id=?",String.class, orgId);
			if (!"0".equals(fid)) {
				addUserOrg(userId, fid);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void delUserOrg(Object userId, Object orgId) {
		if (!isExistsUserOrg(userId, orgId))
			return;
		List<Map<String, Object>> list = this.jdbcTemplate
				.queryForList(
						"select t.id from tlingx_org t where t.fid=? and exists (select 1 from tlingx_userorg where org_id=t.id and user_id=?)",
						orgId, userId);
		String fid = this.jdbcTemplate.queryForObject(
				"select fid from tlingx_org where id=?",String.class, orgId);
		this.jdbcTemplate.update(
				"delete from tlingx_userorg where org_id=? and user_id=?",
				orgId, userId);
		if (!"0".equals(fid)
				&& this.jdbcTemplate
						.queryForInt(
								"select count(*) from tlingx_userorg where user_id=? and org_id in ( select id from tlingx_org where fid=?)",
								userId, fid) == 0) {
			delUserOrg(userId, fid);
		}
		for (Map<String, Object> map : list) {
			delUserOrg(userId, map.get("id"));
		}
	}

	private boolean isExistsUserOrg(Object userId, Object orgId) {
		return this.jdbcTemplate
				.queryForInt(
						"select count(*) from tlingx_userorg where user_id=? and org_id=?",
						userId, orgId) != 0;
	}
	
	public void superManagerAuthRefresh(){
		String id="6e0362e8-100e-11e5-b7ab-74d02b6b5f61";
		this.jdbcTemplate.update("insert into tlingx_roleorg(id,role_id ,org_id) select uuid(),'"+id+"',id from tlingx_org t where  not EXISTS (select 1 from tlingx_roleorg a where a.role_id='"+id+"' and a.org_id=t.id)");
	
		this.jdbcTemplate.update("insert into tlingx_rolefunc(id,role_id ,func_id) select uuid(),'"+id+"',id from tlingx_func t where not EXISTS (select 1 from tlingx_rolefunc a where a.role_id='"+id+"' and a.func_id=t.id)");
		
		this.jdbcTemplate.update("insert into tlingx_rolerole(id,role_id ,refrole_id) select uuid(),'"+id+"',id from tlingx_role t where  not EXISTS (select 1 from tlingx_rolerole a where a.role_id='"+id+"' and a.refrole_id=t.id)");
		
		this.jdbcTemplate.update("insert into tlingx_rolemenu(id,role_id ,menu_id) select uuid(),'"+id+"',id from tlingx_menu t where  not EXISTS (select 1 from tlingx_rolemenu a where a.role_id='"+id+"' and a.menu_id=t.id)");
		
		this.jdbcTemplate.update("update tlingx_app set org_root_id=?,role_root_id=?,func_root_id=?,menu_root_id=? where id=?","6689ae6a-140f-11e5-b650-74d02b6b5f61","6e0367dc-100e-11e5-b7ab-74d02b6b5f61","ae79f6c4-1019-11e5-b7ab-74d02b6b5f61","cc575f33-1301-11e5-b8aa-74d02b6b5f61","335ec1fc-1011-11e5-b7ab-74d02b6b5f61");
	}
	
	public String[] getEmailByUserId(String userid) {
		userid=userid.replace(",", "','");
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select email from tlingx_user where email is not null and id in('"+userid+"')");
		Set<String> set=new HashSet<String>();
		for(Map<String,Object> map:list){
			if(!"".equals(map.get("email").toString())){
				set.add(map.get("email").toString());
			}
		}
		String arr[]=new String[set.size()];
		set.toArray(arr);
		return arr;
	}

	@Override
	public String[] getAllUserByAppid(String appid) {
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select id from tlingx_user where app_id=?",appid);
		
		Set<String> set=new HashSet<String>();
		for(Map<String,Object> map:list){
			set.add(map.get("id").toString());
		}
		String arr[]=new String[set.size()];
		set.toArray(arr);
		return arr;
	}

	@Override
	public String[] getAllUserByOrgid(String orgid) {
		StringBuilder sb=new StringBuilder();
		treeOrg(sb, this.jdbcTemplate, orgid);
		String temp=sb.toString();
		temp=temp.replace("|", "','");
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select id from tlingx_user where org_id in('"+temp+"')");
		Set<String> set=new HashSet<String>();
		for(Map<String,Object> map:list){
			set.add(map.get("id").toString());
		}
		String arr[]=new String[set.size()];
		set.toArray(arr);
		return arr;
	}

	public void resetUserOrg(Object userId){
		this.jdbcTemplate.update("delete from tlingx_userorg where user_id=?",userId);
		String orgId=this.jdbcTemplate.queryForObject("select org_id from tlingx_user where id=?", String.class,userId);
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select org_id from tlingx_userrole where user_id=?",userId);
		this.addUserOrg(userId, orgId);
		for(Map<String,Object> map:list){
			if(map.get("org_id")!=null&&!"".equals(map.get("org_id").toString())){
				this.addUserOrg(userId, map.get("org_id").toString());
			}
		}
	}

	@Override
	public RegexpBean getSqlinBean(Object id,UserBean userBean) {
		JdbcTemplate jdbc=this.jdbcTemplate;
		RegexpBean bean=new RegexpBean();
		List<Map<String,Object>> list=null;
		list=jdbc.queryForList("select id from tlingx_org where id in(select org_id from tlingx_userorg where user_id=?)",id);
		bean.setCurrentOrg(toSqlinString(list));
		list=jdbc.queryForList("select id from tlingx_role where id in(select role_id from tlingx_userrole where user_id=?)",id);
		bean.setCurrentRole(toSqlinString(list));
		list=jdbc.queryForList("select id from tlingx_org where id in(select org_id from tlingx_roleorg where role_id in(select role_id from tlingx_userrole where user_id=?))",id);
		bean.setAuthOrg(toSqlinString(list));
		list=jdbc.queryForList("select id from tlingx_role where id in(select refrole_id from tlingx_rolerole where role_id in(select role_id from tlingx_userrole where user_id=?))",id);
		bean.setAuthRole(toSqlinString(list));
		/*
		 *当前用户行政组织及下级节点 
		 */
		StringBuilder subOrg=new StringBuilder();
		subOrg.append("(");
		treeOrgSqlin(subOrg,jdbc,userBean.getOrgId());
		if(subOrg.length()>0){
			subOrg.deleteCharAt(subOrg.length()-1);
		}
		subOrg.append(")");
		bean.setSubOrg(subOrg.toString());
		/*
		 * 当前应用组织及下级节点
		 */
		StringBuilder appOrg=new StringBuilder();
		appOrg.append("(");
		treeOrgSqlin(appOrg,jdbc,userBean.getApp().getOrgRootId());
		if(appOrg.length()>0){
			appOrg.deleteCharAt(appOrg.length()-1);
		}
		appOrg.append(")");
		bean.setAppOrg(appOrg.toString());
		return bean;
	}
	public static void treeOrgSqlin(StringBuilder sb,JdbcTemplate jdbc,Object orgid){
		sb.append("'").append(orgid.toString()).append("',");
		List<Map<String,Object>> list=jdbc.queryForList("select id from tlingx_org where fid=?",orgid);
		for(Map<String,Object> map:list){
			treeOrgSqlin(sb,jdbc,map.get("id"));
		}
	}
	private static String toSqlinString(List<Map<String,Object>> list){
		StringBuilder sb=new StringBuilder();
		sb.append("(");
		for(Map<String,Object> map:list){
			sb.append("'").append(map.get("id").toString()).append("',");
		}
		if(sb.length()>0)sb.deleteCharAt(sb.length()-1);
		sb.append(")");
		return sb.toString();
	}
	

	public RegexpBean getRegexpBean(Object id,UserBean userBean){
		JdbcTemplate jdbc=this.jdbcTemplate;
		RegexpBean bean=new RegexpBean();
		List<Map<String,Object>> list=null;
		list=jdbc.queryForList("select id from tlingx_org where id in(select org_id from tlingx_userorg where user_id=?)",id);
		bean.setCurrentOrg(toRegexpString(list));
		list=jdbc.queryForList("select id from tlingx_role where id in(select role_id from tlingx_userrole where user_id=?)",id);
		bean.setCurrentRole(toRegexpString(list));
		list=jdbc.queryForList("select id from tlingx_org where id in(select org_id from tlingx_roleorg where role_id in(select role_id from tlingx_userrole where user_id=?))",id);
		bean.setAuthOrg(toRegexpString(list));
		list=jdbc.queryForList("select id from tlingx_role where id in(select refrole_id from tlingx_rolerole where role_id in(select role_id from tlingx_userrole where user_id=?))",id);
		bean.setAuthRole(toRegexpString(list));
		/*
		 *当前用户行政组织及下级节点 
		 */
		StringBuilder subOrg=new StringBuilder();
		treeOrg(subOrg,jdbc,userBean.getOrgId());
		if(subOrg.length()>0){
			subOrg.deleteCharAt(subOrg.length()-1);
		}
		bean.setSubOrg(subOrg.toString());
		/*
		 * 当前应用组织及下级节点
		 */
		StringBuilder appOrg=new StringBuilder();
		treeOrg(appOrg,jdbc,userBean.getApp().getOrgRootId());
		if(appOrg.length()>0){
			appOrg.deleteCharAt(appOrg.length()-1);
		}
		bean.setAppOrg(appOrg.toString());
		/*
		 * 角色组织及下级节点
		 */
		StringBuilder roleOrg=new StringBuilder();
		list=jdbc.queryForList("select org_id from tlingx_userrole where user_id=?",userBean.getId());
		for(Map<String,Object> map:list){
			treeOrg(roleOrg,jdbc,map.get("org_id"));
		}
		if(roleOrg.length()>0){
			roleOrg.deleteCharAt(roleOrg.length()-1);
		}
		bean.setRoleOrg(roleOrg.toString());
		
		return bean;
	}
	public static void treeOrg(StringBuilder sb,JdbcTemplate jdbc,Object orgid){
		if(orgid==null)return;
		if(sb.indexOf(orgid.toString())==-1)
		sb.append(orgid.toString()).append("|");
		List<Map<String,Object>> list=jdbc.queryForList("select id from tlingx_org where fid=?",orgid);
		for(Map<String,Object> map:list){
			treeOrg(sb,jdbc,map.get("id"));
		}
	}
	private static String toRegexpString(List<Map<String,Object>> list){
		StringBuilder sb=new StringBuilder();
		for(Map<String,Object> map:list){
			sb.append(map.get("id").toString()).append("|");
		}
		if(sb.length()>0)sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
	
	public UserBean getUserBean(String userid,String ip){
		JdbcTemplate jdbc=this.jdbcTemplate;
		Map<String,Object> map=jdbc.queryForMap("select * from tlingx_user where account=?",userid);
		UserBean userBean = new UserBean();
		userBean.setId(map.get("id").toString());
		userBean.setAccount(map.get("account").toString());
		userBean.setName(map.get("name").toString());
		userBean.setLoginTime(Utils.getTime());
		userBean.setLoginIp(ip);
		userBean.setLoginCount(map.get("login_count").toString());
		userBean.setStatus(Integer.parseInt(map.get("status").toString()));
		userBean.setToken(map.get("token").toString());
		userBean.setApp(getAppBean(
				map.get("APP_ID").toString(),userBean));
		userBean.setOrgId(map.get("org_id").toString());
		userBean.setRegexp(this.getRegexpBean(map.get("id"),userBean));
		userBean.setSqlin(this.getSqlinBean(map.get("id"), userBean));
		try {
			userBean.setOrgName(jdbc.queryForObject("select name from tlingx_org where id=?", String.class,map.get("org_id")));
		} catch (Exception e) {
		}
		return userBean;
	}
	public  AppBean getAppBean( String id,UserBean userBean) {
		JdbcTemplate jdbc=this.jdbcTemplate;
		AppBean bean = new AppBean();
		Map<String, Object> app = jdbc.queryForMap(
				"select * from tlingx_app where id=?", id );
		bean.setId(id);
		bean.setName(app.get("name").toString());
		bean.setLogo(app.get("logo").toString());
		bean.setCompany(app.get("company").toString());
		bean.setTel(app.get("tel").toString());
		bean.setEmail(app.get("email").toString());
		
		bean.setIndexPage(app.get("indexPage").toString().replace("${CUser.getId()}", userBean.getId()));// indexPage
		bean.setViewModel(app.get("viewModel").toString());
		bean.setOrgRootId(app.get("org_root_id").toString());
		bean.setRoleRootId(app.get("role_root_id").toString());
		bean.setFuncRootId(app.get("func_root_id").toString());
		bean.setMenuRootId(app.get("menu_root_id").toString());
		return bean;
	}
}
