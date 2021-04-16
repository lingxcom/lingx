package com.lingx.core.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lingx.core.model.IEntity;
import com.lingx.core.model.IField;
import com.lingx.core.service.IAppService;
import com.lingx.core.service.ILingxService;
import com.lingx.core.service.IModelService;
import com.lingx.core.service.IUserService;
import com.lingx.core.utils.Utils;

/** 
 * @author www.lingx.com
 * @version 创建时间：2016年2月24日 下午5:01:26 
 * 类说明 
 */
@Transactional
@Component(value="lingxAppService")
public class AppServiceImpl implements IAppService {
	public static final String ORG_ROOT_ID="6689ae6a-140f-11e5-b650-74d02b6b5f61";
	public static final String ROLE_ROOT_ID="6e0367dc-100e-11e5-b7ab-74d02b6b5f61";
	public static final String FUNC_ROOT_ID="ae79f6c4-1019-11e5-b7ab-74d02b6b5f61";
	public static final String MENU_ROOT_ID="cc575f33-1301-11e5-b8aa-74d02b6b5f61";
	public static final String APP_ROOT_ID="335ec1fc-1011-11e5-b7ab-74d02b6b5f61";
	//系统管理
	public static final String MENU_ARRAY_1[]=new String[]{"977493e3-1303-11e5-b8aa-74d02b6b5f61","977489ae-1303-11e5-b8aa-74d02b6b5f61","97749ce4-1303-11e5-b8aa-74d02b6b5f61","9774a00a-1303-11e5-b8aa-74d02b6b5f61"};
	//开发工具
	public static final String MENU_ARRAY_2[]=new String[]{"97748df3-1303-11e5-b8aa-74d02b6b5f61","d69e7450-c0da-409b-8f02-32cfd6280d2d","ae2d9b81-8899-4037-95ab-aa30d2d9db19","97749be8-1303-11e5-b8aa-74d02b6b5f61","97748aca-1303-11e5-b8aa-74d02b6b5f61"};
	public static final String template1="a%s_%s";
	@Resource(name="jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	@Resource
	private ILingxService lingxService;
	@Resource
	private IUserService userService;
	@Resource
	private IModelService modelService;
	@Override
	public boolean createApp(String name) {
		String time=Utils.getTime();
		String pinyin=this.lingxService.uuid();//this.lingxService.getPinyin(name);
		String appId=this.lingxService.uuid();
		int appSN=this.lingxService.queryForInt("select nextval('APP_SN')");
		String sql="insert into tlingx_app(id,sn,name,logo,company,tel,email,indexpage,viewmodel,status,org_root_id,role_root_id,func_root_id,menu_root_id) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		String orgRootId[]=this.createOrg(name, pinyin, appSN);
		String roleRootId[]=this.createRole(name, pinyin, appSN);
		String funcRootId[]=this.createFunc(name,appId);
		String menuRootId[]=this.createMenu(name);
		
		this.jdbcTemplate.update(sql,appId,appSN,name,"","","","","e?e=tlingx_tools&m=workflow","lingx/main.jsp",1,orgRootId[0],roleRootId[0],funcRootId[0],menuRootId[0]);
		sql="insert into tlingx_user(id,account,name,password,status,tel,email,login_count,last_login_time,last_login_ip,create_time,modify_time,org_id,app_id,token,remark) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		String userId=this.lingxService.uuid();
		this.jdbcTemplate.update(sql,userId,String.format(template1, appSN,"admin"),"管理员",this.lingxService.passwordEncode("000000", String.format(template1, appSN,"admin")),1
				,"","",0,"","",time,time,orgRootId[1],appId,Utils.getRandomNumber(32),"");
		this.userService.addUserOrg(userId, orgRootId[1]);
		roleAuth(roleRootId[1],orgRootId,roleRootId,menuRootId);
		this.jdbcTemplate.update("insert into tlingx_userrole(id,user_id,role_id,org_id)values(uuid(),?,?,?)",userId,roleRootId[1],orgRootId[1]);
		
		this.jdbcTemplate.update("insert into tlingx_roleapp(id,role_id,app_id)values(uuid(),?,?)",roleRootId[1],APP_ROOT_ID);
		this.jdbcTemplate.update("insert into tlingx_roleapp(id,role_id,app_id)values(uuid(),?,?)",roleRootId[1],appId);
		this.jdbcTemplate.update("insert into tlingx_roleapp(id,role_id,app_id)values(uuid(),?,?)","6e0362e8-100e-11e5-b7ab-74d02b6b5f61",appId);
		
		this.createWorkflowCategory(appId);
		
		return true;
	}
	public boolean deleteApp(String appId){//org_root_id,role_root_id,func_root_id,menu_root_id
		Map<String,Object> app=this.jdbcTemplate.queryForMap("select * from tlingx_app where id=?",appId);
		Object org_root_id=app.get("org_root_id");
		Object role_root_id=app.get("role_root_id");
		Object func_root_id=app.get("func_root_id");
		Object menu_root_id=app.get("menu_root_id");
		
		deleteSubTree(org_root_id,"tlingx_org");
		deleteSubTree(role_root_id,"tlingx_role");
		deleteSubTree(func_root_id,"tlingx_func");
		deleteSubTree(menu_root_id,"tlingx_menu");
		this.jdbcTemplate.update("delete from tlingx_user where app_id=?",appId);
		this.jdbcTemplate.update("delete from tlingx_wf_category where app_id=?",appId);
		this.jdbcTemplate.update("delete from tlingx_app where id=?",appId);
		clearAllLingxEntity();
		return true;
	}
	public boolean clearAllLingxEntity(){
		String sql="select code from tlingx_entity where code like 'tlingx_%'";
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList(sql);
		for(Map<String,Object> map:list){
			IEntity entity=this.modelService.get(map.get("code").toString());
			clearNoRefRecord(entity);
		}
		return true;
	}
	public boolean clearNoRefRecord(IEntity entity){
		String sql="delete from %s where %s not in(select id from %s) or %s not in(select id from %s)";
		if(entity==null)return false;
		List<IField> list=entity.getFields().getList();
		if(list.size()==3){
			if(Utils.isNotNull(list.get(1).getRefEntity())&&Utils.isNotNull(list.get(2).getRefEntity())){
				sql=String.format(sql, entity.getTableName(),list.get(1).getCode(),list.get(1).getRefEntity(),list.get(2).getCode(),list.get(2).getRefEntity());
				this.jdbcTemplate.update(sql);
			}
		}
		return true;
	}
	
	private void deleteSubTree(Object id,String tableName){
		if(this.lingxService.queryForInt("select count(*) from "+tableName+" where fid=?",id)==0){
			this.jdbcTemplate.update("delete from "+tableName+" where id=?",id);
		}else{
			List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select id from "+tableName+" where fid=?",id);
			for(Map<String,Object> map:list){
				deleteSubTree(map.get("id"),tableName);
			}
			this.jdbcTemplate.update("delete from "+tableName+" where id=?",id);
		}
	}
	
	public void roleAuth(String id,String orgids[],String roleids[],String menuids[]){
		String sql="insert into tlingx_rolefunc(id,role_id ,func_id) select uuid(),'"+id+"',func_id from tlingx_rolefunc t where role_id='6e0362e8-100e-11e5-b7ab-74d02b6b5f61' and not EXISTS (select 1 from tlingx_rolefunc a where a.role_id='"+id+"' and a.func_id=t.func_id)";
		this.jdbcTemplate.update(sql);
		
		sql="insert into tlingx_roleorg(id,role_id,org_id) values(uuid(),?,?)";
		for(String orgid:orgids){
			this.jdbcTemplate.update(sql,id,orgid);
		}
		sql="insert into tlingx_rolerole(id,role_id,refrole_id) values(uuid(),?,?)";
		for(String roleid:roleids){
			this.jdbcTemplate.update(sql,id,roleid);
		}
		sql="insert into tlingx_rolemenu(id,role_id,menu_id) values(uuid(),?,?)";
		for(String menuid:menuids){
			this.jdbcTemplate.update(sql,id,menuid);
		}
	}

	public String[] createOrg(String name,String pinyin,int appSN){
		String[] ret=new String[2];
		String sql="insert into tlingx_org(id,name,code,fid,state,orderindex) values(?,?,?,?,?,?)";
		String id=this.lingxService.uuid();
		ret[0]=id;
		this.jdbcTemplate.update(sql,id,name,String.format(template1, appSN,pinyin),ORG_ROOT_ID,"open",1);
		id=this.lingxService.uuid();
		ret[1]=id;
		this.jdbcTemplate.update(sql,id,"办公室",String.format(template1, appSN,this.lingxService.uuid()),ret[0],"open",1);
		sql="insert into tlingx_roleorg(id,org_id,role_id) select uuid(),t.id,a.role_id from tlingx_org t,tlingx_roleorg a where a.org_id=t.fid and fid=? and not exists (select 1 from tlingx_roleorg where org_id=t.id)";
		this.jdbcTemplate.update(sql,ORG_ROOT_ID);
		this.jdbcTemplate.update(sql,ret[0]);
		return ret;
	}
	public String[] createRole(String name,String pinyin,int appSN){
		String[] ret=new String[2];
		String sql="insert into tlingx_role(id,name,code,fid,state,orderindex) values(?,?,?,?,?,?)";
		String id=this.lingxService.uuid();
		ret[0]=id;
		this.jdbcTemplate.update(sql,id,name,String.format(template1, appSN,pinyin),ROLE_ROOT_ID,"open",1);
		id=this.lingxService.uuid();
		ret[1]=id;
		this.jdbcTemplate.update(sql,id,"管理员",String.format(template1, appSN,this.lingxService.uuid()),ret[0],"open",1);
		sql = "insert into tlingx_rolerole(id,refrole_id,role_id) select uuid(),t.id,a.role_id from tlingx_role t,tlingx_rolerole a where a.refrole_id=t.fid and fid=? and not exists (select 1 from tlingx_rolerole where refrole_id=t.id)";
		this.jdbcTemplate.update(sql, ROLE_ROOT_ID);
		this.jdbcTemplate.update(sql, ret[0]);
		return ret;
	}
	public String[] createFunc(String name,String appId){
		String[] ret=new String[1];
		String id=this.lingxService.uuid();
		ret[0]=id;
		String sql="insert into tlingx_func(id,name,module,func,type,fid,state) values(?,?,?,?,?,?,?)";
		this.jdbcTemplate.update(sql,id,name,appId+"_app","-",2,FUNC_ROOT_ID,"open");
		this.jdbcTemplate.update("INSERT into tlingx_rolefunc(id,role_id,func_id)select uuid(),'6e0362e8-100e-11e5-b7ab-74d02b6b5f61',id from tlingx_func t where not EXISTS (select 1 from tlingx_rolefunc where role_id='6e0362e8-100e-11e5-b7ab-74d02b6b5f61' and func_id=t.id)");
		
		return ret;
	}
	public String[] createMenu(String name){
		String[] ret=null;
		List<String> list=new ArrayList<String>();
		String id=this.lingxService.uuid();
		String sql="insert into tlingx_menu(id,name,type,iconcls,fid,orderindex,status,state,func_id,remark) values(?,?,?,?,?,?,?,?,?,?)";
		list.add(id);
		this.jdbcTemplate.update(sql,id,name,1,"",MENU_ROOT_ID,999,1,"open","7903b684-7945-4b8c-b5d8-6762cbb959d7","");
		String fid=id;
		String id1=this.lingxService.uuid();
		list.add(id1);
		this.jdbcTemplate.update(sql,id1,"系统管理",1,"",fid,998,1,"open","7903b684-7945-4b8c-b5d8-6762cbb959d7","");
		for(String menuid:MENU_ARRAY_1){
			list.add(copyMenu(menuid,id1));
		}
		
		String id2=this.lingxService.uuid();
		list.add(id2);
		this.jdbcTemplate.update(sql,id2,"开发工具",1,"",fid,999,1,"open","7903b684-7945-4b8c-b5d8-6762cbb959d7","");
		for(String menuid:MENU_ARRAY_2){
			list.add(copyMenu(menuid,id2));
		}
		
		sql="insert into tlingx_rolemenu(id,menu_id,role_id) select uuid(),t.id,a.role_id from tlingx_menu t,tlingx_rolemenu a where a.menu_id=t.fid and fid=? and not exists (select 1 from tlingx_rolemenu where menu_id=t.id)";
		this.jdbcTemplate.update(sql,MENU_ROOT_ID);
		this.jdbcTemplate.update(sql,fid);
		this.jdbcTemplate.update(sql,id1);
		this.jdbcTemplate.update(sql,id2);
		ret=new String[list.size()];
		ret=list.toArray(ret);
		return ret;
	}
	
	private String copyMenu(String id,String fid){
		String newid=this.lingxService.uuid();
		String sql="insert into tlingx_menu(id,name,type,iconcls,fid,orderindex,status,state,func_id,remark) select '"+newid+"',name,type,iconcls,'"+fid+"',orderindex,status,state,func_id,remark from tlingx_menu where id=?";
		this.jdbcTemplate.update(sql,id);
		return newid;
	}
	
	public void createWorkflowCategory(String appid){
		String rootId=this.lingxService.uuid(),time=Utils.getTime();
		String sql="insert into tlingx_wf_category(id,name,fid,orderindex,state,create_time,modify_time,app_id) values(?,?,?,?,?,?,?,?)";
		this.jdbcTemplate.update(sql,rootId,"流程分类",0,1,"open",time,time,appid);
		this.jdbcTemplate.update(sql,this.lingxService.uuid(),"普通流程",rootId,1,"open",time,time,appid);
		
	}
}
