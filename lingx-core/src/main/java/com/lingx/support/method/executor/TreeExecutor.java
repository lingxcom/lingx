package com.lingx.support.method.executor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.lingx.core.SpringContext;
import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.event.TreeExecutorEvent;
import com.lingx.core.exception.LingxScriptException;
import com.lingx.core.model.IEntity;
import com.lingx.core.model.IExecutor;
import com.lingx.core.model.IScript;
import com.lingx.core.model.impl.AbstractModel;
import com.lingx.core.service.IModelService;
import com.lingx.core.service.IQueryService;
import com.lingx.core.service.impl.LingxServiceImpl;
import com.lingx.core.utils.LingxUtils;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年6月12日 上午10:12:10 
 * 类说明 
 */
public class TreeExecutor extends AbstractModel implements IExecutor {

	private static final long serialVersionUID = 7485731095480944923L;
	@Resource
	private IModelService modelService;

	private JdbcTemplate jdbcTemplate;
	private IQueryService queryService;
	@Override
	public Object execute(IContext context, IPerformer performer)
			throws LingxScriptException {
		boolean isTreeRoot=false;//因为树型数据做了特殊处理，所以做个标志，根节点默认展开
		List<Map<String, Object>> list;
		String id = context.getRequest().getParameter("node");
		String checkbox=context.getRequest().getParameter("checkbox");
		if (id == null)
			return null;
		IEntity scriptEntity = (IEntity) context.getEntity();
		String tableName = modelService.getTableName(scriptEntity);
		String textField = modelService.getTextField(scriptEntity).get(0);
		String valueField = modelService.getValueField(scriptEntity);
		StringBuilder subCondition = new StringBuilder();
		subCondition.append(this.queryService.getQueryString(context, performer));
		if(!LingxServiceImpl.SN)return new HashMap<String,Object>();
		if(jdbcTemplate.queryForInt(LingxUtils.sqlInjection(String.format("select count(*) from %s where fid='%s' %s  ",tableName, id,subCondition)))==0||"0".equals(id)){
			isTreeRoot=true;
			if("0".equals(id)&&jdbcTemplate.queryForInt(LingxUtils.sqlInjection(String.format("select count(*) from %s where fid='%s' %s  ",tableName, id,subCondition)))>0){
				//无需智能取ID
				list = jdbcTemplate.queryForList(LingxUtils.sqlInjection(String
						.format("select * from %s where fid='%s' %s ",tableName, id,subCondition)));
			}else{
				//智能取ID
				id=getRootID(tableName, id,subCondition.toString(),context);
				if("0".equals(id)){
					list = jdbcTemplate.queryForList(LingxUtils.sqlInjection(String
							.format("select * from %s where fid='%s' %s ",tableName, id,subCondition)));
				}else{
					list= this.jdbcTemplate.queryForList(LingxUtils.sqlInjection(String.format("select * from %s where id='%s' %s ",tableName, id,subCondition)));
				}
			}
		}else{

			list = jdbcTemplate.queryForList(LingxUtils.sqlInjection(String
					.format("select * from %s where fid='%s' %s ",tableName, id,subCondition)));
		}
		

		SpringContext.getApplicationContext().publishEvent(new TreeExecutorEvent(this,LingxUtils.sqlInjection(String.format("select * from %s where 1=1 %s ",tableName, subCondition)),context,performer));
		//context.getSession().put(Constants.SESSION_LAST_QUERY_SQL, LingxUtils.sqlInjection(String.format("select * from %s where 1=1 %s ",tableName, subCondition)));
		if (list != null) {
			for (Map<String, Object> map : list) {
				if (jdbcTemplate.queryForInt(LingxUtils.sqlInjection(String.format("select count(*) from %s where fid='%s' %s", tableName,map.get("id"),subCondition))) > 0) {
					map.put("leaf", false);
				}else{
					map.put("leaf", true);
				}
				if(map.get("state")!=null){
					if(isTreeRoot){
						map.put("expanded",true);
					}else{
						map.put("expanded", "open".equals(map.get("state").toString()));
					}
				}
				if(map.get("icon_cls")!=null){
					map.put("iconCls", map.get("icon_cls"));
				}
				if("1".equals(checkbox)){
					map.put("checked", false);
				}
				map.put("text", map.get(textField));
				map.put("id", map.get(valueField));
			}
		}
		
		return list;
	}

	public String getRootID(String tableName,String id,String condition,IContext context){
		//特殊处理开始 当要取系统权限四树的RootID时，首要取应用配置值
		Set<String> ids=new HashSet<String>();
		ids.add(context.getUserBean().getApp().getOrgRootId());
		ids.add(context.getUserBean().getApp().getRoleRootId());
		ids.add(context.getUserBean().getApp().getMenuRootId());
		ids.add(context.getUserBean().getApp().getFuncRootId());
		if("tlingx_org,tlingx_role,tlingx_menu,tlingx_func".indexOf(tableName)>-1&&!ids.contains(id)){
			String tmp="";
			if("tlingx_org".equals(tableName)){
				tmp=context.getUserBean().getApp().getOrgRootId();
			}else if("tlingx_role".equals(tableName)){
				tmp=context.getUserBean().getApp().getRoleRootId();
			}else if("tlingx_menu".equals(tableName)){
				tmp=context.getUserBean().getApp().getMenuRootId();
			}else if("tlingx_func".equals(tableName)){
				tmp=context.getUserBean().getApp().getFuncRootId();
			}
			return tmp;
			
		}
		//特殊处理结束
		String temp=id;
		String sql="select id,fid from %s where 1=1 %s";
		List<Map<String,Object>> list=jdbcTemplate.queryForList(LingxUtils.sqlInjection(String.format(sql, tableName,condition)));
		Set<String> set=new HashSet<String>();
		for(Map<String,Object> map:list){
			set.add(map.get("id").toString());
		}
		
		for(Map<String,Object> map:list){
			if(set.contains(map.get("fid"))){
				
			}else{
				temp=map.get("fid").toString();
				break;
			}
		}
		return temp;
	}
	
	private List<Map<String, Object>> tree(JdbcTemplate jdbcTemplate,String tableName, int fid,
			String textField, String valueField, String subCondition) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList(String
				.format("select * from %s where fid=%s %s order by %s asc ",
						tableName, fid,subCondition, textField));
		if (list != null) {
			for (Map<String, Object> map : list) {
				String id = map.get("id").toString();
				List<Map<String, Object>> l = tree(jdbcTemplate,tableName,
						Integer.parseInt(id), textField, valueField,
						subCondition);
				map.put("children", l);
				if (l.size() > 0) {
					map.put("state", "closed");
				}
				map.put("text", map.get(textField));
				map.put("id", map.get(valueField));
			}
		}
		return list;
	}

	@Override
	public IScript getScript() {
		return null;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void setModelService(IModelService modelService) {
		this.modelService = modelService;
	}

	public void setQueryService(IQueryService queryService) {
		this.queryService = queryService;
	}

}
