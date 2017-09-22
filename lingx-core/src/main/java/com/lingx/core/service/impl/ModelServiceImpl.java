package com.lingx.core.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lingx.core.Constants;
import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IHttpRequest;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.model.IConfig;
import com.lingx.core.model.IEntity;
import com.lingx.core.model.IField;
import com.lingx.core.model.IMethod;
import com.lingx.core.model.IModel;
import com.lingx.core.model.INode;
import com.lingx.core.model.IScript;
import com.lingx.core.model.annotation.FieldModelConfig;
import com.lingx.core.model.annotation.TreeNode;
import com.lingx.core.model.bean.Function;
import com.lingx.core.model.bean.UserBean;
import com.lingx.core.model.impl.DefaultField;
import com.lingx.core.model.impl.DefaultMethod;
import com.lingx.core.model.impl.ExpressionExecutor;
import com.lingx.core.model.impl.ExpressionInterpreter;
import com.lingx.core.model.impl.ExpressionValidator;
import com.lingx.core.model.impl.GridConfig;
import com.lingx.core.model.impl.QueryEntity;
import com.lingx.core.model.impl.RuleConfig;
import com.lingx.core.service.IChooseService;
import com.lingx.core.service.IDatabaseService;
import com.lingx.core.service.IDefaultValueService;
import com.lingx.core.service.IFuncService;
import com.lingx.core.service.II18NService;
import com.lingx.core.service.IInterpretService;
import com.lingx.core.service.IModelService;
import com.lingx.core.service.impl.model.ModelIO;
import com.lingx.core.service.impl.model.ModelTemplate;
import com.lingx.core.utils.AnnoUtils;
import com.lingx.core.utils.Utils;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月9日 下午9:40:19 
 * 类说明 
 */
@Component(value="lingxModelService")
public class ModelServiceImpl implements IModelService {
	public static final Logger log=LogManager.getLogger(ModelServiceImpl.class);
	
	public static CopyBean COPY_ENTITY_BEAN_CACHE=null;
	
	@Resource(name="jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	@Resource
	private IInterpretService interpretService;
	@Resource
	private IDatabaseService databaseService;
	@Resource
	private IChooseService chooseService;
	@Resource
	private IDefaultValueService defaultValueService;
	@Resource
	private IFuncService funcService;
	@Resource
	private II18NService i18n;
	private static Map<String,IEntity> ENTITY_CACHE = Collections .synchronizedMap(new HashMap<String,IEntity>());
	
	public void saveModelToDisk(){
		String path=new File("").getAbsolutePath();
		path=path+"\\model\\"+Utils.getTime();
		File dir=new File(path);
		if(!dir.exists())dir.mkdirs();
		System.out.println("PATH>>"+path);
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select id from tlingx_model ");
		for(Map<String,Object> map:list){
			String id=map.get("id").toString();
			IEntity entity=this.getEntity(id);
			ModelIO.writeDisk(path+"/"+id+".lingx", entity);
		}
		
	}
	public void recoveryModelForDisk(){
		String path=new File("").getAbsolutePath();
		path=path+"\\model\\"+Utils.getTime().substring(0, 8);
		File dir=new File(path);
		if(!dir.exists())dir.mkdirs();
		System.out.println("PATH>>"+path);
		File[] list=dir.listFiles();
		for(File file:list){
			String id=file.getName().substring(0,file.getName().lastIndexOf("."));
			IEntity entity=ModelIO.readDisk(path+"/"+id+".lingx");
			this.save(entity);
		}
	}
	@Override
	public IEntity getCacheEntity(String code) {
		IEntity temp=ENTITY_CACHE.get(code);
		try {
			if(temp==null){
				temp=getEntity(code);
			}
			if(temp!=null){
				temp=(IEntity)ModelIO.clone(temp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return temp;
	}

	@Override
	public IEntity getEntity(String code) {
		try {
			IEntity temp=ModelIO.readEntity(code, jdbcTemplate);
			ENTITY_CACHE.put(code, temp);
			return temp;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public IEntity get(String code) {
		return this.getEntity(code);
	}
	@Override
	public IMethod getMethod(String methodCode,IEntity entity) {
		IMethod temp=null;
		if(entity==null)return null;
		for(IMethod method:entity.getMethods().getList()){
			if(methodCode.equals(method.getCode())){
				temp=method;
			}
		}
		return temp;
	}

	@Override
	public List<Map<String, Object>> getTreeList(IEntity entity,String id) {
		IModel model=this.getById(entity, id);
		List<Object> list=AnnoUtils.getField(model.getClass(), TreeNode.class, model);
		List<Map<String,Object>> nodes=new ArrayList<Map<String,Object>>();
		for(Object obj:list){
			if(obj instanceof IModel){
				Map<String,Object> map=new HashMap<String,Object>();
				IModel m=(IModel)obj;
				map.put("id",m.getId() );
				if(obj instanceof IEntity || obj instanceof IField ||obj instanceof IMethod ){
					map.put("text",m.getName()+" ["+m.getCode()+"]" );
				}else{
					map.put("text",m.getName() );
				}
				map.put("iconCls",m.getIconCls());
				map.put("leaf", m.getLeaf());
				if(obj instanceof INode){
					map.put("expanded", true);
				}
				nodes.add(map);
			}else if(obj instanceof List){
				List<IModel> ll=(List<IModel>)obj;
				for(IModel m:ll){
					Map<String,Object> map=new HashMap<String,Object>();
					map.put("id",m.getId() );
					if(m instanceof IEntity || m instanceof IField ||m instanceof IMethod ){
						map.put("text",m.getName()+" ["+m.getCode()+"]" );
					}else{
						map.put("text",m.getName() );
					}
					map.put("iconCls",m.getIconCls());
					map.put("leaf", m.getLeaf());
					nodes.add(map);
				}
			}else{
			}
		}
		return nodes;
	}
	
	public IEntity createScriptEntity(String table,String name,String author,int idtype,String dbName,JdbcTemplate jdbc){
		return ModelTemplate.createEntity(table, name, author, idtype, dbName,jdbc);
	}
	
	public void save(IEntity entity){
		try {
			ENTITY_CACHE.put(entity.getCode(), entity);
			ModelIO.writeEntity(entity, jdbcTemplate);
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}
	
	public IModel clone(IModel model){
		return (IModel)ModelIO.clone(model);
	}

	@Override
	public IModel getById(IModel entity, String id) {
		IModel model=null;
		if(entity==null){return null;}
		//Field fields[]=entity.getClass().getDeclaredFields();
		Field[] fields1=entity.getClass().getDeclaredFields();
		Field[] fields2=entity.getClass().getSuperclass().getDeclaredFields();
		Field[] fields=new Field[fields1.length+fields2.length];
		System.arraycopy(fields1, 0, fields, 0, fields1.length);
		System.arraycopy(fields2, 0, fields, fields1.length, fields2.length);
		if(id.equals(entity.getId())){return entity;}
		for(Field field:fields){
			try {
				field.setAccessible(true); 
				Object o=field.get(entity);
				if(o instanceof IModel){
					IModel m=(IModel)o;
					if(id.equals(m.getId())){
						model= m;
						if(model!=null)return model;
					}else{
						model= getById(m,id);
						if(model!=null)return model;
					}
				}else if(o instanceof List){
					List<IModel> ll=(List<IModel>)o;
					for(IModel m:ll){
						if(id.equals(m.getId())){
							model= m;
							if(model!=null)return model;
						}else{
							model= getById(m,id);
							if(model!=null)return model;
						}
					}
				}else{
					//System.out.println("--getById--未处理--"+field.getName());
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return model;
	}

	@Override
	public Map<String, Object> getBySort(Object o) {

		Map<String,Object> map=new HashMap<String,Object>();
		if(o==null)return map;
		java.lang.reflect.Method[] methods=o.getClass().getMethods();
		for(java.lang.reflect.Method m:methods){
			String name=m.getName();
			if(name.indexOf("get")==0){
				try {
					Object value=m.invoke(o);
					if(value==null)value="";
					if(value instanceof List)continue;
					if(value instanceof Class)continue;
					if(value instanceof IScript)continue;
						java.lang.reflect.Field f=null;
						try {
							f=o.getClass().getDeclaredField(getDojoFieldByMethodName(name));
						} catch (Exception e1) {
							f=o.getClass().getSuperclass().getDeclaredField(getDojoFieldByMethodName(name));
						}
						FieldModelConfig anno=f.getAnnotation(FieldModelConfig.class);
						
						if(anno!=null){
							if("hidden".equals(anno.editor()))continue;
							map.put(anno.sort()+"_"+getDojoFieldByMethodName(name), value);
						}else{
							map.put(getDojoFieldByMethodName(name), value);
						}
				} catch (Exception e1) {
					//e1.printStackTrace();
				} 
			}
		}
		return map;
	}

	@Override
	public Map<String, Object> getByAnno(Object o) {

		Map<String,Object> map=new HashMap<String,Object>();
		if(o==null)return map;
		java.lang.reflect.Method[] methods=o.getClass().getMethods();
		for(java.lang.reflect.Method m:methods){
			String name=m.getName();
			if(name.indexOf("get")==0){
				try {
					Object value=m.invoke(o);
					if(value instanceof List)continue;
					if(value instanceof Class)continue;
					if(value instanceof IScript)continue;
					Map<String,Object> prop=new HashMap<String,Object>();
					
						java.lang.reflect.Field f=null;
						try {
							f=o.getClass().getDeclaredField(getDojoFieldByMethodName(name));
						} catch (Exception e1) {
							f=o.getClass().getSuperclass().getDeclaredField(getDojoFieldByMethodName(name));
						}
						FieldModelConfig anno=f.getAnnotation(FieldModelConfig.class);
						if(anno!=null){
							map.put(anno.sort()+"_"+getDojoFieldByMethodName(name), prop);
							prop.put("displayName", anno.name());
							if( anno.editor()!=null&&!"".equals(anno.editor())){
								if("text".equals(anno.editor())||"hidden".equals(anno.editor())||"text".equals(anno.editor()))continue;
									if(anno.editor().indexOf("new ")>-1){
										prop.put("editor", anno.editor());
									}
							}
						}else{
							map.put(getDojoFieldByMethodName(name), prop);
						}
				} catch (Exception e1) {
					//e1.printStackTrace();
				} 
			}
		}
		return map;
	}

	@Override
	public Map<String, Object> getProperty(String code, String id) {
		IEntity entity=get(code);
		IModel o=getById(entity, id);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("prop", getBySort(o));
		map.put("propAnno", getByAnno(o));
		return map;
	}
	
	@Override
	public boolean saveProperty(String code, String id, String prop,
			String value, String oldvalue, String author) {
		IEntity entity=get(code);
		try {
			IModel ae= getById(entity, id);
			prop=getPropertyNameFor_(prop);
			invokeSet(ae,prop,value);
			fieldSynchro(entity,ae,prop,value);
			ae.setAuthor(author);
			ae.setLastModifyTime(Utils.getTime());
			save(entity);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private String addModel(IEntity entity,String id ,IModel model){
		IModel m=this.getById(entity, id);
		if(m instanceof INode){
			INode n=(INode)m;
			n.getList().add(model);
			save(entity);
			return model.getId();
		}else{
			//System.out.println(m.getClass().getName());
			return null;
		}
	}
	public String addFieldGlobal(String code,String id,String fieldCode,String order){
		IEntity entity = get(code);
		
		this.add(entity.getFields().getList(), fieldCode, order);
		for(IMethod m:entity.getMethods().getList()){
			this.add(m.getFields().getList(), fieldCode, order);
		}
		this.save(entity);
		return "reload";
	}
	
	private void add(List<IField> list,String code,String order){
		int index=0;
		for(IField field:list ){
			index++;
			if(field.getCode().equals(order)){
				break;
			}
		}
		DefaultField field=new DefaultField();
		field.setCode(code);
		field.setName(code);
		list.add(index, field);
	}
	@Override
	public String addField(String code, String id) {
		return this.addModel(get(code), id, new DefaultField());
	}

	@Override
	public String addMethod(String code, String id) {
		return this.addModel(get(code), id, new DefaultMethod());
	}

	@Override
	public String addValidator(String code, String id) {
		return this.addModel(get(code), id, new ExpressionValidator());
	}

	@Override
	public String addInterpreter(String code, String id) {
		return this.addModel(get(code), id, new ExpressionInterpreter());
	}

	@Override
	public String addExecutor(String code, String id) {
		return this.addModel(get(code), id, new ExpressionExecutor());
	}

	private void removeForeach(Object source,Object target){
		List<Object> list=AnnoUtils.getField(source.getClass(), TreeNode.class, source);
		for(Object obj:list){
			if(obj instanceof INode){
				INode node=(INode)obj;
				if(node.getList().contains(target)){
					node.getList().remove(target);
					break;
				}else{
					for(Object o:node.getList()){
						removeForeach(o,target);
					}
				}
			}
		}
	}
	@Override
	public boolean remove(String code, String ids) {
		IEntity entity=get(code);
		String array[]=ids.split(",");
		for(String id:array){
			IModel model=this.getById(entity, id);
			removeForeach(entity,model);
		}
		save(entity);
		return true;
	}

	@Override
	public void reset(IModel model) {
		model.setId(Utils.getRandomString(IModel.ID_LEN));
		if(model instanceof INode){
			INode n=(INode)model;
			List<Object> l=n.getList();
			for(Object obj:l){
				if(obj instanceof IModel){
					reset((IModel)obj);
				}
			}
		}
		List<Object> list=AnnoUtils.getField(model.getClass(), TreeNode.class, model);
		for(Object obj:list){
			if(obj instanceof IModel){
				reset((IModel)obj);
			}
		}
	}

	@Override
	public boolean copyEntity(String code, String ids) {
		COPY_ENTITY_BEAN_CACHE= new CopyBean(code,ids);
		return true;
	}

	@Override
	public boolean pasteEntity(String code, String id) {
		IEntity targetentity=get(code);
		IEntity sourceEntity=get(COPY_ENTITY_BEAN_CACHE.getCode());
		for(String sourceId:COPY_ENTITY_BEAN_CACHE.getList()){
			IModel model=this.getById(sourceEntity, sourceId);
			IModel newModel=(IModel)clone(model);
			reset(newModel);
			this.addModel(targetentity, id,newModel);
		}
		return true;
	}

	@Override
	public boolean move(String code, String id, int type) {
		IEntity entity=get(code);
		IModel ae=getById(entity,id);
		sortModel(entity,ae,type);
		save(entity);
		return true;
	}
	private boolean sortModel(IModel source,IModel target,int type){
		List<Object> list=AnnoUtils.getField(source.getClass(), TreeNode.class, source);
		for(Object obj:list){
			if(obj instanceof IModel){
				if(obj instanceof INode){
					INode node=(INode)obj;
					if(node.getList().contains(target)){
						sort(node.getList(),target,type);
						return true;
					}
					for(Object o:node.getList()){
						if(sortModel((IModel)o,target,type)) return true;
					}
				}else{
					sortModel((IModel)obj,target,type);
				}
			}
		}
		return false;
	}
	
	private void sort(List list, Object o, int type) {
		int index=list.indexOf(o);
		int target=index+type;
		if(target<0||target>=list.size())return;
		Object to=list.get(target);
		list.set(target, o);
		list.set(index, to);
	}

	@Override
	public String getPropertyNameFor_(String name) {
		return name.substring(name.indexOf('_')+1);
	}

	@Override
	public String getDojoFieldByMethodName(String methodName) {
		String temp=methodName.substring(4);
		String a3=String.valueOf(methodName.charAt(3));
		return a3.toLowerCase()+temp;
	}
	private  void invokeSet(Object obj,String field,String value)throws Exception{
		java.lang.reflect.Method[] methods=obj.getClass().getMethods();
		
		for(java.lang.reflect.Method m:methods){
			String name=m.getName();
			if(name.indexOf("set")==0){
				Class c[] = m.getParameterTypes();
				Class clazz= c[0];
				if(clazz.isInterface())continue;
				if(getDojoFieldByMethodName(name).equals(field)){
					initObjectParam(clazz,m,obj,value);
				}
			}
		}
	}
	private  void initObjectParam(Class clazz, java.lang.reflect.Method m,
			Object exeObject,String value) {
		
		String param=value;
		try {
			if (clazz.equals(String.class)) {
				if (Utils.isNull(param))
					param = "";
				m.invoke(exeObject, param);
			} else if (clazz.equals(Integer.class)) {
				if (Utils.isNull(param))
					param = "0";
				m.invoke(exeObject, Integer.parseInt(param));
			} else if (clazz.equals(Long.class)) {
				if (Utils.isNull(param))
					param = "0";
				m.invoke(exeObject, Long.parseLong(param));
			} else if (clazz.equals(Float.class)) {
				if (Utils.isNull(param))
					param = "0";
				m.invoke(exeObject, Float.parseFloat(param));
			} else if (clazz.equals(Double.class)) {
				if (Utils.isNull(param))
					param = "0";
				m.invoke(exeObject, Double.parseDouble(param));
			} else if (clazz.equals(Boolean.class)) {
				if (Utils.isNull(param))
					param = "false";
				m.invoke(exeObject, "true".equals(param));
			} else {
				System.out.println("找不到:"+clazz.toString());
				m.invoke(exeObject, "true".equals(param));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private  void fieldSynchro(IEntity entity,IModel f,String prop,String value){
		if(f instanceof IField){
			IField field=(IField)f;
			if(field.getFieldSynchro()){
				for(IField temp:entity.getFields().getList()){
					if(temp==field)continue;
					if(temp.getCode().equals(field.getCode())&&temp.getFieldSynchro()){
						try {
							invokeSet(temp,prop,value);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				for(IMethod method:entity.getMethods().getList()){
					for(IField temp:method.getFields().getList()){
						if(temp==field)continue;
						if(temp.getCode().equals(field.getCode())&&temp.getFieldSynchro()){
							try {
								invokeSet(temp,prop,value);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
	}
	@Override
	public Map<String, Object> getSourceConfig(String code) {
		Map<String,Object> map=new HashMap<String,Object>();
		IEntity entity=getEntity(code);
		for(IField field : entity.getFields().getList()){
			Map<String,Object> prop=new HashMap<String,Object>();
			prop.put("displayName",field.getName());
			//prop.put("editor", "new Ext.form.field.Text() ");
			map.put(field.getCode(), prop);
		}
		return map;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	static class CopyBean{
		private String code;
		private List<String> list;
		public CopyBean(String code,String ids){
			this.code=code;
			this.list=new ArrayList<String>();
			String array[]=ids.split(",");
			for(String s:array){
				if(s!=null)
				this.list.add(s);
			}
		}
		public String getCode() {
			return code;
		}
		public List<String> getList() {
			return list;
		}
	}
	public IField getFieldByEntity(IEntity entity,IEntity refEntity){
		return getFieldByEntity(entity,refEntity.getCode());
	}
	/**
	 * 取出entity指向refEntity的字段
	 * @param entity
	 * @param refEntity
	 * @return
	 */
	public IField getFieldByEntity(IEntity entity,String refEntityCode){
		IField field=null;
		for(IField f:entity.getFields().getList()){
			if(f.getRefEntity()!=null&&f.getRefEntity().equals(refEntityCode)){
				field=f;break;
			}
		}
		return field;
	}
	public List<String> getTextField(IEntity scriptEntity){
		List<IField> fs=scriptEntity.getFields().getList();
		List<String> list=new ArrayList<String>();
		String temp=Constants.REF_DEFAULT;
		if(fs!=null)
		for(IField f:fs){
			if(Constants.REF_DISPLAY.equals(f.getComboType())){
				temp=f.getCode();
				list.add(temp);
			}
		}
		if(list.size()==0)
		{
			list.add(temp);
		}		
		return list;
	}
	public String getValueField(IEntity scriptEntity){
		List<IField> fs=scriptEntity.getFields().getList();
		String temp=Constants.REF_DEFAULT;
		if(fs!=null)
		for(IField f:fs){
			if(Constants.REF_VALUE.equals(f.getComboType())){
				temp=f.getCode();
				break;
			}
		}
		return temp;
	}

	public  String getRuleDataAuth(IEntity se,UserBean userBean){
		String sql="";
		List<IConfig> configs=se.getConfigs().getList();
		RuleConfig rule=null;
		for(IConfig config:configs){
			if("RuleConfig".equals(config.getModelType())){
				rule=(RuleConfig)config;
			}
		}
		if(rule!=null){
			String ruleJSON=rule.getDataRule();
			if("{\"group\":[{\"op\":\"and\",\"type\":\"op\"}],\"type\":\"group\"}".equals(ruleJSON))return "";
			Map<String,Object> map=(Map<String,Object>)JSON.parse(ruleJSON);
			sql=ruleMapToSql(map,userBean);
			//System.out.println(sql);
			sql=formatRule(sql,userBean);
			sql=" and "+sql.replaceAll("  ", " ");
		}
		return sql;
	}
	private static String formatRule(String sql,UserBean userBean){
		if(userBean==null)return sql;
		sql=sql.replaceAll("\\{authOrg\\}", "(select org_id from tlingx_roleorg where role_id in(select role_id from tlingx_userrole where user_id='"+userBean.getId()+"'))");
		sql=sql.replaceAll("\\{authRole\\}", "(select refrole_id from tlingx_rolerole where role_id in(select role_id from tlingx_userrole where user_id='"+userBean.getId()+"'))");
		sql=sql.replaceAll("\\{authFunc\\}", "(select func_id from tlingx_rolefunc where role_id in(select role_id from tlingx_userrole where user_id='"+userBean.getId()+"'))");
		sql=sql.replaceAll("\\{currentDevApp\\}", "(select id from tlingx_app where status=1)");
		sql=sql.replaceAll("\\{authMenu\\}", "(select menu_id from tlingx_rolemenu where role_id in(select role_id from tlingx_userrole where user_id='"+userBean.getId()+"'))");
		
		sql=sql.replaceAll("\\{currentOrg\\}","(select org_id from tlingx_userorg where user_id='"+userBean.getId()+"')");
		sql=sql.replaceAll("\\{currentApp\\}","'"+userBean.getApp().getId()+"'");
		sql=sql.replaceAll("\\{currentUser\\}","'"+userBean.getId()+"'");
		sql=sql.replaceAll("\\{currentRole\\}","(select role_id from tlingx_userrole where user_id='"+userBean.getId()+"')");
		
		sql=sql.replaceAll("\\{currentUserOrg\\}","'"+userBean.getOrgId()+"'");
		sql=sql.replaceAll("\\{currentRoleOrg\\}","(select org_id from tlingx_userrole where user_id='"+userBean.getId()+"')");
		
		sql=sql.replaceAll("\\{authOrg2\\}","'"+userBean.getRegexp().getAuthOrg()+"'");
		sql=sql.replaceAll("\\{authRole2\\}","'"+userBean.getRegexp().getAuthRole()+"'");
		sql=sql.replaceAll("\\{currentOrg2\\}","'"+userBean.getRegexp().getCurrentOrg()+"'");
		sql=sql.replaceAll("\\{currentRole2\\}","'"+userBean.getRegexp().getCurrentRole()+"'");
		sql=sql.replaceAll("\\{subOrg2\\}","'"+userBean.getRegexp().getSubOrg()+"'");
		sql=sql.replaceAll("\\{appOrg2\\}","'"+userBean.getRegexp().getAppOrg()+"'");
		sql=sql.replaceAll("\\{roleOrg2\\}","'"+userBean.getRegexp().getRoleOrg()+"'");
		
		sql=sql.replaceAll("\\{subOrg\\}","('"+userBean.getRegexp().getSubOrg().replace("|", "','")+"')");
		sql=sql.replaceAll("\\{appOrg\\}","('"+userBean.getRegexp().getAppOrg().replace("|", "','")+"')");
		sql=sql.replaceAll("\\{roleOrg\\}","('"+userBean.getRegexp().getRoleOrg().replace("|", "','")+"')");
		
		return sql;
	}
	private  String ruleMapToSql(Map<String,Object> map,UserBean userBean){
		StringBuilder sb=new StringBuilder();
		if(map==null||map.get("type")==null)return " 1=1 ";
		if(map.get("type").toString().equals("group")){
			List<Map<String,Object>> list=(List<Map<String,Object>>)map.get("group");
			sb.append(" ( ");
			String op="";
			for(Map<String,Object> m:list){
				if(m.get("type").toString().equals("op")){
					op=m.get("op").toString();
				}
			}
			for(Map<String,Object> m:list){
				if(m.get("type").toString().equals("op"))continue;
				sb.append(ruleMapToSql(m,userBean)).append("  ").append(op);
			}
			sb.delete(sb.length()-3, sb.length());
			sb.append(" ) ");
		}else if(map.get("type").toString().equals("condi")){
			String right=map.get("right").toString();
			right=formatRight(right,userBean);//对右边的参数特别处理，如：tuserorg.userid
			sb.append(" ").append(formatLeft(map.get("left").toString())).append(" ").append(map.get("op")).append(" ").append(right).append(" ");
		}else if(map.get("type").toString().equals("op")){
			sb.append(" ").append(map.get("op")).append(" ");
		}
		return sb.toString().replaceAll("  ", " ");
	}

	private String formatLeft(String left){
		if(left.length()==36&&left.indexOf("-")>0){
			left="'"+left+"'";
		}
		return left;
	}
	private  String formatRight(String right,UserBean userBean){
		int len=right.indexOf(".");
		if(len==-1)return right;
		else{
			//属于对象引用
			IEntity entity=this.getCacheEntity(right.substring(0,len));
			String subRule=getRuleDataAuth(entity,userBean);//and  ( ( org_id in (select org_id from tuserorg where userid=198) ) ) 
			
			StringBuilder sb=new StringBuilder();
			sb.append("( select ");
			sb.append(right.substring(len+1)).append(" from ");
			sb.append(entity.getTableName()).append(" where 1=1 ");
			sb.append(subRule);
			sb.append(" )");
			
			return sb.toString();
		}
	}
	
	public  Map<String,Object> getExtJSGridParams(IEntity entity,UserBean userBean){
		Map<String,Object> params=new HashMap<String,Object>();
		
		List<Map<String,Object>> model=new ArrayList<Map<String,Object>>();

		List<Map<String,Object>> columns=new ArrayList<Map<String,Object>>();

		List<Map<String,Object>> toolbar=new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> rightmenu=new ArrayList<Map<String,Object>>();
		int roleNumber=this.jdbcTemplate.queryForInt("select count(*) from tlingx_role where id in(select role_id from tlingx_userrole where user_id=?)",userBean.getId());
		int troleFieldNumber=0;
		//log.info("role number of current user:"+roleNumber);
		for(IField field:entity.getFields().getList()){
			if(!field.getVisible())continue;
			troleFieldNumber=this.jdbcTemplate.queryForInt("select count(*) from tlingx_rolefield where role_id in(select role_id from tlingx_userrole where user_id=?) and entitycode=? and fieldcode=?",userBean.getId(),entity.getCode(),field.getCode());
			if(troleFieldNumber>=roleNumber)continue;
			field.setName(i18n.getText(field.getName()));
			Map<String,Object> m=new HashMap<String,Object>();
			m.put("name", field.getCode());
			m.put("type", "string");
			
			Map<String,Object> c=new HashMap<String,Object>();
			c.put("header",i18n.text( field.getName()));
			c.put("dataIndex", field.getCode());
			if(Utils.isNotNull(field.getWidth())){
				try{
				c.put("width", Integer.parseInt(field.getWidth()));
				}catch(Exception e){}
			}
			//c.put("flex", 1);
			//c.put("width", 10);
			if(field.getRefEntity()!=null&&!"".equals(field.getRefEntity())&&field.getEscape()&&!"tlingx_optionitem_NO".equals(field.getRefEntity())){//tlingx_optionitem_NO 这里本来的处理，是为了字典在界面上不能点击，但会导致，需要该字段字时，会取不到，所以取消
				if(field.getIsEntityLink()){
					IEntity refEntity=getCacheEntity(field.getRefEntity());
					m.put("type", "object");
					c.put("renderer", new Function("function(value, p, record){	var temp='';	if(!value)return temp;	for(var i=0;i<value.length;i++){		temp=temp+'<a href=\"javascript:;\" onclick=\"openViewWindow(\\'"+refEntity.getCode()+"\\',\\'"+refEntity.getName()+"\\',\\''+value[i].id+'\\');\">'+value[i].text+'</a>,';	}	temp=temp.substring(0,temp.length-1); 	return temp;}"));
				
				}else{
					m.put("type", "object");
					c.put("renderer", new Function("function(value, p, record){	var temp='';	if(!value)return temp;	for(var i=0;i<value.length;i++){		temp=temp+value[i].text+',';	}	temp=temp.substring(0,temp.length-1); 	return temp;}"));
				
				}
				}
			if("file".equals(field.getInputType())){
				//m.put("type", "object");
				c.put("renderer", new Function("function(value, p, record){	if(!value)return '';  var temp=value; var fvalue=''; if(temp&&temp.length>1&&temp.charAt(0)=='['){ var tempText=\"\"; var tempArr=Ext.JSON.decode(temp); for(var ii=0;ii<tempArr.length;ii++){ tempText+=\"<a target='_blank' href='\"+tempArr[ii].value+\"' >\"+tempArr[ii].text+\"</a>,\"; } if(tempText.length>0){ tempText=tempText.substring(0,tempText.length-1); } fvalue=tempText; }else{ fvalue=\"<a target='_blank' href='\"+value+\"' >文件下载</a>\"; }return fvalue;}"));
			}
			if("image".equals(field.getInputType())){
				//m.put("type", "object");
				//c.put("renderer", new Function("function(value, p, record){	if(!value)return '';  var temp=value; var fvalue=''; if(temp&&temp.length>1&&temp.charAt(0)=='['){ var tempText=\"\"; var tempArr=Ext.JSON.decode(temp); for(var ii=0;ii<tempArr.length;ii++){ tempText+=\"<a target='_blank' href='\"+tempArr[ii].value+\"' >\"+tempArr[ii].text+\"</a>,\"; } if(tempText.length>0){ tempText=tempText.substring(0,tempText.length-1); } fvalue=tempText; }else{ fvalue=\"<a target='_blank' href='\"+value+\"' ><img height='60' src=\"'+value+'\" /></a>\"; }return fvalue;}"));
				c.put("renderer", new Function("function(value, p, record){	if(!value)return '';  var temp=value; var fvalue=''; if(temp&&temp.length>1&&temp.charAt(0)=='['){ var tempText=\"\"; var tempArr=Ext.JSON.decode(temp); for(var ii=0;ii<tempArr.length;ii++){ tempText+=\"<a target='_blank' href='\"+tempArr[ii].value+\"' ><img height=\'60\' src='\"+tempArr[ii].value+\"' /></a>,\"; } if(tempText.length>0){ tempText=tempText.substring(0,tempText.length-1); } fvalue=tempText; }else{ fvalue=\"<a target='_blank' href='\"+value+\"' ><img height=\'60\' src='\"+value+\"' /></a>\"; }return fvalue;}"));
				
				//c.put("renderer", new Function("function(value, p, record){	var temp='';	if(!value)return temp;	temp='<a target=\"_blank\" href=\"'+value+'\" ><img height=\"60\" src=\"'+value+'\" /></a>'; 	return temp;}"));
			}
			if(LingxServiceImpl.SN){
			model.add(m);
			columns.add(c);}
		}
		List<IMethod> methodList=new ArrayList<IMethod>();
		methodList.addAll(entity.getMethods().getList());
		methodList.addAll(this.chooseService.getDefaultMethods().values());
		for(IMethod method:methodList){
			if(!this.funcService.getAuth(userBean.getId(), entity.getCode(), method.getCode()))continue;
			if(method.getVisible()){
				Map<String,Object> m=new HashMap<String,Object>();
				m.put("xtype", "button");
				m.put("text", i18n.text(method.getName()));
				m.put("iconCls",method.getIconCls());
				m.put("code", method.getCode());
				if(!method.getEnabled())m.put("disabled", true); //disabled
				m.put("handler",new Function("function(){methodWindow(this,"+JSON.toJSONString(entity, SerializerFeature.DisableCircularReferenceDetect)+","+JSON.toJSONString(method, SerializerFeature.DisableCircularReferenceDetect)+")}"));
				if(LingxServiceImpl.SN)
				toolbar.add(m);
			}
			if(method.getRightmenu()!=null&&method.getRightmenu()){
				Map<String,Object> m2=new HashMap<String,Object>();
				m2.put("text", i18n.text(method.getName()));
				m2.put("iconCls",method.getIconCls());
				m2.put("code", method.getCode());
				if(!method.getEnabled())m2.put("disabled", true); //disabled
				m2.put("handler",new Function("function(){methodWindow(this,"+JSON.toJSONString(entity, SerializerFeature.DisableCircularReferenceDetect)+","+JSON.toJSONString(method, SerializerFeature.DisableCircularReferenceDetect)+")}"));
				if(LingxServiceImpl.SN)
				rightmenu.add(m2);
			}
			
		}
		params.put("isSearch", this.funcService.getAuth(userBean.getId(), entity.getCode(), "search"));
		params.put("GridConfig", entity.getConfigs().getList().get(0));
		params.put("model", model);
		params.put("columns",columns);
		params.put("toolbar",toolbar);
		params.put("rightmenu",rightmenu);
		params.put("fields", entity.getFields());
		params.put("code", entity.getCode());
		params.put("name", i18n.text(entity.getName()));
		
		if(entity instanceof QueryEntity){
			QueryEntity qe=(QueryEntity)entity;
			params.put("params", qe.getParams().getList());
		}else{
		}
		return params;
	}
	
	public void removeDefaultValueTransformTag(List<IField> listFields){
		String temp;
		for(IField field:listFields){
			if(field.getValue()!=null){
				temp=field.getValue().toString();
				//temp=temp.replaceAll("\\$\\{[^}]*\\}", "");
				temp=FormServiceImpl.removeTags(temp);
				field.setValue(temp);
			}
		}
	}
	
	public  void setValueForDefaultValue(List<IField> listFields,IContext context,IHttpRequest request){
		String temp;
		for(IField field:listFields){
			if(Utils.isNotNull(field.getDefaultValue())){
				temp=this.defaultValueService.transform(field.getDefaultValue(),context);
				for(String key:request.getParameterNames()){
					temp=temp.replaceAll("\\$\\{"+key+"\\}", request.getParameter(key));
				}
				if(Utils.isNotNull(temp))
				field.setValue(temp);
			}
		}
	}
	public  void setValueForRequest(List<IField> listFields,IHttpRequest request){
		for(IField field:listFields){
			if(Utils.isNotNull(request.getParameter(field.getCode())))field.setValue(request.getParameter(field.getCode()));
		}
		
		/*
		 * 根据外关键从数据库里取值
		 */
		String _refEntity_=request.getParameter("_refEntity_");
		String _refId_=request.getParameter("_refId_");
		//System.out.println("_refEntity_:"+_refEntity_);
		if(Utils.isNotNull(_refEntity_)){
			IEntity entity=this.getCacheEntity(_refEntity_);
			String code=null;
			for(IField field :listFields){
				if(_refEntity_.equals(field.getRefEntity())){
					code=field.getCode();
					break;
				}
			}

			//System.out.println("code:"+code);
			if(code!=null){
				try {
					String temp,sql="select * from "+entity.getTableName()+" where "+this.getValueField(entity)+"=?";
					Map<String,Object> map=this.jdbcTemplate.queryForMap(sql,_refId_!=null?_refId_:request.getParameter(code));
					for(IField field:listFields){
						if(Utils.isNotNull(field.getDefaultValue())){
							temp=field.getDefaultValue();
							if(field.getValue()!=null&&!"".equals(field.getValue().toString())){
								temp=field.getValue().toString();
							}
							for(String key:map.keySet()){
								if( map.get(key)!=null&&!"".equals(map.get(key).toString())){
									//System.out.println(temp+":"+"\\$\\{"+entity.getCode()+"."+key+"\\}"+":"+map.get(key).toString());
									temp=temp.replace("${"+entity.getCode()+"."+key+"}", map.get(key).toString());
								}
							}
							field.setValue(temp);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	public void roleFieldAndSetValue(List<IField> listFields,String ecode,UserBean userBean){
		int roleNumber=this.jdbcTemplate.queryForInt("select count(*) from tlingx_role where id in(select role_id from tlingx_userrole where user_id=?)",userBean.getId());
		int troleFieldNumber=0;
		for(IField field:listFields){
			/*if(!field.getVisible()){
				field.setInputType("hidden");
				continue;
			}*/
			if(!field.getEnabled()){
				field.setInputType("hidden");
				continue;
			}
			
			troleFieldNumber=this.jdbcTemplate.queryForInt("select count(*) from tlingx_rolefield where role_id in(select role_id from tlingx_userrole where user_id=?) and entitycode=? and fieldcode=?",userBean.getId(),ecode,field.getCode());
			if(troleFieldNumber>=roleNumber){
				field.setInputType("hidden");
					try {
						Map<String,Object> map=this.jdbcTemplate.queryForMap("select defaultvalue from tlingx_rolefield where role_id in(select role_id from tlingx_userrole where user_id=?) and entitycode=? and fieldcode=? order by level desc limit 1",userBean.getId(),ecode,field.getCode());
						if(map!=null&&map.get("defaultvalue")!=null&&!"".equals(map.get("defaultvalue").toString()))field.setValue(map.get("defaultvalue"));
					} catch (Exception e) {
						e.printStackTrace();
					}
			}
		}
	}
	
	public void setValueForFields(List<IField> listFields,String ecode,String valueField,String eid,IContext context,IPerformer performer){
		
		//String sqlForId="select * from %s where id='%s'";
		
		try {
			//valueField="id";
			IEntity mainEntity=getCacheEntity(ecode);
			String sqlForId=this.databaseService.getSelectSqlById(mainEntity, context, performer);
			//Map<String,Object> map=this.jdbcTemplate.queryForMap(String.format(sqlForId, mainEntity.getTableName(),eid));
			Map<String,Object> map=this.jdbcTemplate.queryForMap(sqlForId,eid);
			for(IField field:listFields){
				//20151222
				if(Utils.isNotNull(field.getDefaultValue())){
					String temp=field.getDefaultValue();
					if(field.getValue()!=null&&!"".equals(field.getValue().toString())){
						temp=field.getValue().toString();
					}
					for(String key:map.keySet()){
						if(map.get(key)==null)continue;
						temp=temp.replaceAll("\\$\\{"+key+"\\}", map.get(key).toString());
					}
					field.setValue(temp);
					//20160803
					//20160920 加入||map.get(field.getCode())==null||"".equals(map.get(field.getCode()).toString())；如果从数据库提出来的字段是空值时会被默认值替换
					if(!map.containsKey(field.getCode())||map.get(field.getCode())==null||"".equals(map.get(field.getCode()).toString()))
					map.put(field.getCode(), temp);
				}
				//20151222 end
				if(!map.containsKey(field.getCode()))continue;
				if(Utils.isNull(field.getRefEntity())){
					field.setValue(map.get(field.getCode()));
				}else{
					String sql="select * from %s where %s=?";

					String sql2="select * from %s where %s in (%s)";
					if("tlingx_optionitem".equals(field.getRefEntity())){
						sql+=" and option_id in(select id from tlingx_option where code='"+field.getInputOptions()+"')";
						sql2+=" and option_id in(select id from tlingx_option where code='"+field.getInputOptions()+"')";
					}
					IEntity entity=getCacheEntity(field.getRefEntity());
					List<String> text=getTextField(entity);
					String value=getValueField(entity);
					Map<String,Object> m=null;
					try {
						String realValue=map.get(field.getCode())==null?field.getValue().toString():map.get(field.getCode()).toString();
						if(realValue.indexOf(",")==-1){//一个值
							IEntity temp=getCacheEntity(field.getRefEntity());
						 m=this.jdbcTemplate.queryForMap(String.format(sql,temp.getTableName(),value),map.get(field.getCode()));

							StringBuilder sb=new StringBuilder();
							for(String s:text){
								sb.append(m.get(s)).append("-");
							}
							sb.deleteCharAt(sb.length()-1);
							m.put("text", sb.toString());
							m.put("value", m.get(value));
							m.put("etype", entity.getCode());
							m.put("ename", entity.getName());
							m.put("exists", true);
						}else{//多个值
							//System.out.println(String.format(sql2, field.getRefEntity(),value,realValue));
							IEntity temp=getCacheEntity(field.getRefEntity());
							List<Map<String,Object>> list=this.jdbcTemplate.queryForList(String.format(sql2, temp.getTableName(),value,formatValue(realValue)));
							StringBuilder texts=new StringBuilder();
							StringBuilder values=new StringBuilder();
							for(Map<String,Object> obj:list){
								texts.append(obj.get(text.get(0))).append(",");
								values.append(obj.get(value)).append(",");
							}
							texts.deleteCharAt(texts.length()-1);
							values.deleteCharAt(values.length()-1);
							m=new HashMap<String,Object>();
							m.put("id", values.toString());
							m.put("text", texts.toString());
							m.put("value", values.toString());
							m.put("etype", entity.getCode());
							m.put("ename", entity.getName());
							m.put("exists", true);
						}
					} catch (Exception e) {
						//e.printStackTrace();
						m=new HashMap<String,Object>();
						m.put("id", map.get(field.getCode()));
						m.put("text",map.get(field.getCode()));
						m.put("value", map.get(field.getCode()));
						m.put("etype", field.getRefEntity());
						m.put("ename", "");
						m.put("exists", false);
					}
					field.setValue(m);
				}
			}
			
			for(IField field:listFields){
				performer.addParam(field.getCode(), field.getValue());
			}
			
			for(IField field:listFields){
				
				field.setValue(this.interpretService.outputFormat(field, null,performer));
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}
	public String getCode(){
		try {
			InetAddress ia = InetAddress.getLocalHost();			byte[] m = NetworkInterface.getByInetAddress(ia).getHardwareAddress();			StringBuffer sb = new StringBuffer("");			for(int i=0; i<m.length; i++) {				if(i!=0) {					sb.append("-");				}								int temp = m[i]&0xff;				String str = Integer.toHexString(temp);				if(str.length()==1) {					sb.append("0"+str);				}else {					sb.append(str);				}			}			return Utils.md5(sb.toString()+"5").substring(2);
		} catch (Exception e) {
			String c=getLinuxMACAddress() ;
			if(Utils.isNull(c)){
				c=getUnixMACAddress();
			}
			if(Utils.isNull(c)){
				c="none;";
			}
			return  Utils.md5(c+"5").substring(2);
		}
	}
	private static String getLinuxMACAddress() {  
        String mac = null;  
        BufferedReader bufferedReader = null;  
        Process process = null;  
        try {  
            // linux下的命令，一般取eth0作为本地主网卡 显示信息中包含有mac地址信息  
            process = Runtime.getRuntime().exec("ifconfig eth0");  
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));  
            String line = null;  
            int index = -1;  
            while ((line = bufferedReader.readLine()) != null) {  
                index = line.toLowerCase().indexOf("硬件地址");  
                if (index != -1) {  
                    // 取出mac地址并去除2边空格  
                    mac = line.substring(index + 4).trim();  
                    break;  
                }  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                if (bufferedReader != null) {  
                    bufferedReader.close();  
                }  
            } catch (IOException e1) {  
                e1.printStackTrace();  
            }  
            bufferedReader = null;  
            process = null;  
        }  
        return mac;  
    }  
      
    /** 
    * @MethodName: getUnixMACAddress  
    * @description : 获取Unix网卡的mac地址 
    * @author：liming 
    * @date： 2013-5-5 下午04:49:59 
    * @return String 
    */  
	private static String getUnixMACAddress() {  
        String mac = null;  
        BufferedReader bufferedReader = null;  
        Process process = null;  
        try {  
            // Unix下的命令，一般取eth0作为本地主网卡 显示信息中包含有mac地址信息  
            process = Runtime.getRuntime().exec("ifconfig eth0");  
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));  
            String line = null;  
            int index = -1;  
            while ((line = bufferedReader.readLine()) != null) {  
                // 寻找标示字符串[hwaddr]  
                index = line.toLowerCase().indexOf("hwaddr");  
                if (index != -1) {  
                    // 取出mac地址并去除2边空格  
                    mac = line.substring(index + "hwaddr".length() + 1).trim();  
                    break;  
                }  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                if (bufferedReader != null) {  
                    bufferedReader.close();  
                }  
            } catch (IOException e1) {  
                e1.printStackTrace();  
            }  
            bufferedReader = null;  
            process = null;  
        }  
  
        return mac;  
    }  
      
	private String formatValue(String realValue){
		
		return "'"+realValue.replaceAll(",", "','")+"'";
	}
	/**
	 * 当GetServlet没有传eid时，对默认值进行转换
	 * @param listFields
	 * @param jdbc
	 * @param jsper
	 * @param interpreter
	 */
public   void setValueForFields(List<IField> listFields,IPerformer performer){
		
		try {
			for(IField field:listFields){
				if(Utils.isNull(field.getRefEntity())){
					
				}else{
					String sql="select * from %s where %s=?";

					String sql2="select * from %s where %s in (%s)";
					if("tlingx_optionitem".equals(field.getRefEntity())){
						sql+=" and option_id in(select id from tlingx_option where code='"+field.getInputOptions()+"')";
						sql2+=" and option_id in(select id from tlingx_option where code='"+field.getInputOptions()+"')";
					}
					IEntity entity=getCacheEntity(field.getRefEntity());
					List<String> text=getTextField(entity);
					String value=getValueField(entity);
					Map<String,Object> m=null;
					try {
						if(field.getValue()==null)continue;
						String realValue=field.getValue().toString();
						if(realValue.indexOf(",")==-1){//一个值
							IEntity temp=getCacheEntity(field.getRefEntity());
						 m=this.jdbcTemplate.queryForMap(String.format(sql,temp.getTableName(),value),realValue);

							StringBuilder sb=new StringBuilder();
							for(String s:text){
								sb.append(m.get(s)).append("-");
							}
							sb.deleteCharAt(sb.length()-1);
							m.put("text", sb.toString());
							m.put("value", m.get(value));
							m.put("etype", entity.getCode());
							m.put("ename", entity.getName());
							m.put("exists", true);
						}else{//多个值
							//System.out.println(String.format(sql2, field.getRefEntity(),value,realValue));
							List<Map<String,Object>> list=this.jdbcTemplate.queryForList(String.format(sql2, field.getRefEntity(),value,formatValue(realValue)));
							StringBuilder texts=new StringBuilder();
							StringBuilder values=new StringBuilder();
							for(Map<String,Object> obj:list){
								texts.append(obj.get(text.get(0))).append(",");
								values.append(obj.get(value)).append(",");
							}
							texts.deleteCharAt(texts.length()-1);
							values.deleteCharAt(values.length()-1);
							m=new HashMap<String,Object>();
							m.put("id", values.toString());
							m.put("text", texts.toString());
							m.put("value", values.toString());
							m.put("etype", entity.getCode());
							m.put("ename", entity.getName());
							m.put("exists", true);
						}
					} catch (Exception e) {
						//e.printStackTrace();
						m=new HashMap<String,Object>();
						m.put("id", "");
						m.put("text","");
						m.put("value", "");
						m.put("etype", field.getRefEntity());
						m.put("ename", "");
						m.put("exists", false);
					}
					field.setValue(m);
				}
			}
			
			for(IField field:listFields){
				performer.addParam(field.getCode(), field.getValue());
			}
			
			for(IField field:listFields){
				field.setValue(this.interpretService.outputFormat(field, null,performer));
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}

	@Override
	public boolean createQueryEntity(String code, String name,String author,String app) {
		IEntity entity=ModelTemplate.createQueryEntity(code, name, author);
		this.save(entity);
		if(this.jdbcTemplate.queryForInt("select count(*) from tlingx_entity where code=?",code)==0)
		this.jdbcTemplate.update("insert into tlingx_entity(id,name,code,type,status,app_id,create_time)values(uuid(),?,?,1,1,?,?)",name,code,app,Utils.getTime());
		return true;
	}

	@Override
	public String getTableName(IEntity entity) {
		return entity.getTableName();
	}

	public  String getEntityOrderField(IEntity entity){
		GridConfig c=null;
		for(IConfig config :entity.getConfigs().getList()){
			if(config instanceof GridConfig){
				c=(GridConfig)config;
			}
		}
		if(c!=null){
			return c.getSortName();
		}else
		return "id";
	}
	public  String getEntityOrderType(IEntity entity){
		GridConfig c=null;
		for(IConfig config :entity.getConfigs().getList()){
			if(config instanceof GridConfig){
				c=(GridConfig)config;
			}
		}
		if(c!=null){
			return c.getSortOrder();
		}else
		return "asc";
	}
	public void setInterpretService(IInterpretService interpretService) {
		this.interpretService = interpretService;
	}
	public void setDatabaseService(IDatabaseService databaseService) {
		this.databaseService = databaseService;
	}
	public void setChooseService(IChooseService chooseService) {
		this.chooseService = chooseService;
	}
	public void setDefaultValueService(IDefaultValueService defaultValueService) {
		this.defaultValueService = defaultValueService;
	}

	
	
}
