package com.lingx.support.web.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.jdbc.core.JdbcTemplate;

import com.alibaba.fastjson.JSON;
import com.lingx.core.Constants;
import com.lingx.core.Page;
import com.lingx.core.SpringContext;
import com.lingx.core.action.IAction;
import com.lingx.core.action.ISessionAware;
import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.engine.impl.DefaultPerformer;
import com.lingx.core.event.LoginEvent;
import com.lingx.core.event.LogoutEvent;
import com.lingx.core.model.IEntity;
import com.lingx.core.model.IMethod;
import com.lingx.core.model.bean.Function;
import com.lingx.core.model.bean.UserBean;
import com.lingx.core.service.IDefaultValueService;
import com.lingx.core.service.II18NService;
import com.lingx.core.service.ILingxService;
import com.lingx.core.service.ILoginService;
import com.lingx.core.service.IModelService;
import com.lingx.core.service.IPageService;
import com.lingx.core.service.IScriptApisService;
import com.lingx.core.service.IUserService;
import com.lingx.core.service.IVerifyCodeService;
import com.lingx.core.utils.LingxUtils;
import com.lingx.core.utils.Utils;

public class DefaultAction implements IAction,ISessionAware {
	@Resource
	private JdbcTemplate jdbcTemplate;
	@Resource
	private ILoginService loginService;
	@Resource
	private ILingxService lingxService;
	@Resource
	private IVerifyCodeService verifyCodeService;
	@Resource
	private IPageService pageService;
	@Resource
	private IDefaultValueService defaultValueService;
	@Resource 
	private IUserService userService;
	@Resource
	private II18NService i18n;
	@Resource
	private IModelService modelService;
	@Resource
	private IScriptApisService scriptApisService;
	
	private HttpSession session;
	public String action(IContext context) {
		String cmd=context.getRequest().getParameter("c");
		if("menu".equals(cmd)){
			String json= menu(context);
			context.getRequest().setAttribute(Constants.REQUEST_JSON,json);
			return Page.PAGE_JSON;
		}else if("method_script".equals(cmd)){
			String json= script(context);
			context.getRequest().setAttribute(Constants.REQUEST_JSON,json);
			return Page.PAGE_JSON;
		}else if("login".equals(cmd)){
			Map<String,Object> res=login(context);
			jdbcTemplate.update("insert into tlingx_login_log(userid,ip,message,ts) values(?,?,?,?)",context.getRequest().getParameter("userid"), context.getRequest().getAttribute(IContext.CLIENT_IP), res.get("message"), Utils.getTime());
			context.getRequest().setAttribute(Constants.REQUEST_JSON,JSON.toJSONString(res));
			return Page.PAGE_JSON;
		}else if("i".equals(cmd)){
			this.lingxService.call("be516eac-aa22-4e16-9d46-cb34dc5713e5", "dfc2620b-de0c-11e5-be8f-74d02b6b", new HashMap<String,String>(), context);
			return context.getUserBean().getApp().getViewModel();
		}else if("l".equals(cmd)){
			return this.pageService.getPage(Page.PAGE_LOGIN);
		}else if("sn".equals(cmd)){
			return "lingx/common/sn.jsp";
		}else if("password".equals(cmd)){
			return this.pageService.getPage(Page.PAGE_PASSWORD);
		}else if("logout".equals(cmd)){
			SpringContext.getApplicationContext().publishEvent(new LogoutEvent(this,context.getUserBean()));
			context.getSession().clear();
			session.invalidate();
			return this.pageService.getPage(Page.PAGE_LOGIN);
		}else if("getNewMessageCount".equals(cmd)){
			int msg_count=jdbcTemplate.queryForInt("select count(*) from tlingx_message where to_user_id=? and status=1",context.getUserBean().getId());
			Map<String,Object> ret=new HashMap<String,Object>();
			ret.put("code", 1);
			ret.put("message", "SUCCESS");
			ret.put("count", msg_count);

			context.getRequest().setAttribute(Constants.REQUEST_JSON,JSON.toJSONString(ret));
			return Page.PAGE_JSON;
		}else{
			return Page.PAGE_JSON;
		}
		
	}
	public Map<String,Object> login(IContext context){
		Map<String,Object> res=new HashMap<String,Object>();
		String userid=context.getRequest().getParameter("userid");
		String password=context.getRequest().getParameter("password");
		context.getRequest().setAttribute("userid", userid);
		if (Utils.isNull(userid) || Utils.isNull(password)) {
			res.put("code", -1);
			res.put("message", "账号或密码不可为空");
			return res;
		}
		userid=userid.trim();
		password=password.trim();
		if(!this.verifyCodeService.verify(context)){
			res.put("code", -1);
			res.put("message", "验证码无效");
			return res;
		}
		
		if(this.loginService.before(userid, context)){
			if(this.loginService.login(userid, password, context)){
				UserBean userBean=this.userService.getUserBean(userid,context.getRequest().getAttribute(IContext.CLIENT_IP).toString());
				if (userBean.getStatus() != 1) {

					res.put("code", -1);
					res.put("message", "禁止登录");
					return res;
				}
				session.setAttribute(Constants.SESSION_USER, userBean);
				//context.getSession().put(Constants.SESSION_USER, userBean);
				res.put("code", 1);
				res.put("message", "登录成功");
				String afterUrl=this.loginService.after(userid, context);
				if(Utils.isNotNull(afterUrl)){
					res.put("page", afterUrl);
				}else{
					res.put("page", "d?c=i");
				}

				SpringContext.getApplicationContext().publishEvent(new LoginEvent(this,userBean));
				return res;
			}else{
				res.put("code", -1);
				res.put("message", "账号或密码无效");
				return res;
			}
		}else{
			res.put("code", -1);
			res.put("message", "登录前置条件验证失败");
			return res;
		}
	}
	
	public String menu(IContext context) {
		UserBean userBean=context.getUserBean();
		boolean isDelIcon=!"true".equals(context.getRequest().getParameter("menu_icon_1"));
		String userid = userBean.getId();
		String menuLinkFunc = " and a.type<>3 and ( a.type<>2 or (a.type=2 and ( func_id in (select func_id from tlingx_userfunc where user_id='"+userid+"') or func_id in(select func_id from tlingx_rolefunc where role_id in(select role_id from tlingx_userrole where user_id='"+userid+"'))))) ";

		List<Map<String, Object>> menu = jdbcTemplate
				.queryForList("select t.id,t.name as text,t.short_name,t.iconcls as iconCls from tlingx_menu t,tlingx_func a where t.func_id=a.id and t.fid=? and t.status=1 and (t.id in(select menu_id from tlingx_usermenu where user_id='"+userid+"') or t.id in(select menu_id from tlingx_rolemenu where role_id in(select role_id from tlingx_userrole where user_id='"
						+ userid + "'))) "+menuLinkFunc+" order by t.orderindex asc",userBean.getApp().getMenuRootId());
		for (Map<String, Object> map : menu) {
			String id = map.get("id").toString();
			List<Object> sub = subMenu(id, jdbcTemplate, menuLinkFunc, userid,context);
			map.put("menu", sub);
			map.put("text", i18n.text(map.get("text")));
			if (sub.size() == 0) {
				map.put("disabled", true);
			}
			map.remove("id");
			if(isDelIcon)map.remove("iconCls");
		}
		return JSON.toJSONString(menu);
	}

	public List<Object> subMenu(String fid, JdbcTemplate jdbc, String condi,
			String userid,IContext context) {
		List<Object> menu = new ArrayList<Object>();

		List<Map<String, Object>> list = jdbc
				.queryForList(LingxUtils
						.sqlInjection("select t.* ,a.module,a.func from tlingx_menu t,tlingx_func a where t.func_id=a.id  and t.status=1 and (t.id in(select menu_id from tlingx_usermenu where user_id='"+userid+"') or t.id in (select menu_id from tlingx_rolemenu where role_id in(select role_id from tlingx_userrole where user_id='"
								+ userid
								+ "'))) and t.fid='"
								+ fid
								+ "' and ( ( 1=1 "
								+ condi
								+ ") or (a.type=1) ) order by orderindex asc"));
		for (Map<String, Object> map : list) {
			if ("2".equals(map.get("type").toString())) {
				menu.add("-");
				continue;
			}
			Map<String, Object> temp = new HashMap<String, Object>();
			String id = map.get("id").toString();
			temp.put("itemId",map.get("id"));
			temp.put("text", i18n.text(map.get("name")));
			temp.put(
					"handler",
					new Function("function(){ addTab('"
							+ id
							+ "','"
							+ i18n.text(map.get("name"))
							+ "','"
							+ String.format("e?e=%s&m=%s"+getExtWhere(map.get("remark").toString(),context),
									map.get("module"), map.get("func")) + "')}"));
			List<Object> sub = subMenu(id, jdbc, condi, userid,context);
			temp.put("iconCls", map.get("iconcls"));
			temp.put("uri", String.format("e?e=%s&m=%s"+getExtWhere(map.get("remark").toString(),context),
					map.get("module"), map.get("func")) );
			if (sub.size() > 0) {
				temp.remove("handler");
				temp.put("menu", sub);
			}
			menu.add(temp);
		}
		return menu;
	}

	private String getExtWhere(String temp,IContext context){
		temp=this.defaultValueService.transforms(temp, context);
		if(Utils.isNull(temp))return "";
		int sindex=temp.indexOf("${");
		if(sindex!=0)return"";
		int eindex=temp.lastIndexOf("}");
		if(eindex==-1)return"";
		return "&"+temp.substring(sindex+2,eindex);
	}
	public String script(IContext context){

		Map<String,Object> ret=new HashMap<String,Object>();
		ret.put("code", 1);
		ret.put("message", "SUCCESS");
		ret.put("ret", true);
		
		String e=context.getRequest().getParameter("e");
		String m=context.getRequest().getParameter("m");
		String id=context.getRequest().getParameter("id");
		IEntity entity=this.modelService.get(e);
		IMethod method=this.modelService.getMethod(m, entity);
		if(method==null){//系统默认方法，不做这操作
			ret.put("ret", "true");
			return JSON.toJSONString(ret);
		}
		String script=method.getBeforeScript();
		if(Utils.isNotNull(script)){
			Map<String,Object> data=this.jdbcTemplate.queryForMap("select * from "+entity.getTableName()+" where id=?",id);
			IPerformer performer=new DefaultPerformer(this.scriptApisService.getScriptApis(),context.getRequest());
			performer.addParam(data);
			try {
				Object obj=performer.script(script, context);
				ret.put("ret", obj);
				ret.put("msg", method.getBeforeMessage());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		return JSON.toJSONString(ret);
	}
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public void setSession(HttpSession session) {
		this.session=session;
	}
	public void setLoginService(ILoginService loginService) {
		this.loginService = loginService;
	}
	public void setVerifyCodeService(IVerifyCodeService verifyCodeService) {
		this.verifyCodeService = verifyCodeService;
	}
	public void setPageService(IPageService pageService) {
		this.pageService = pageService;
	}
}
