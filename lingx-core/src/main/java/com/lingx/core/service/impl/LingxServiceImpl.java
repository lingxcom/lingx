package com.lingx.core.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lingx.core.Constants;
import com.lingx.core.Page;
import com.lingx.core.SpringContext;
import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IMethodProcess;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.engine.impl.DefaultPerformer;
import com.lingx.core.exception.LingxPluginException;
import com.lingx.core.model.IEntity;
import com.lingx.core.model.IField;
import com.lingx.core.model.IMethod;
import com.lingx.core.model.bean.CascaderBean;
import com.lingx.core.model.bean.UserBean;
import com.lingx.core.plugin.IPlugin;
import com.lingx.core.plugin.PluginManager;
import com.lingx.core.service.IChooseService;
import com.lingx.core.service.IConfigService;
import com.lingx.core.service.IContextService;
import com.lingx.core.service.IInterpretService;
import com.lingx.core.service.ILingxService;
import com.lingx.core.service.IModelService;
import com.lingx.core.service.IQueryService;
import com.lingx.core.service.IScriptApisService;
import com.lingx.core.service.IValidateService;
import com.lingx.core.utils.LingxUtils;
import com.lingx.core.utils.Utils;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月11日 下午8:54:50 
 * 类说明 
 */
@Component(value="lingxService")
public class LingxServiceImpl implements ILingxService ,ApplicationContextAware{

	public static boolean SN=true;
	public static final String countForeignKeySQL="select count(*) from %s where %s=?";
	@Resource
	private IModelService modelService;
	@Resource
	private IScriptApisService scriptApisService;
	@Resource
	private IValidateService validateService;
	@Resource(name="jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	@Resource
	private Map<String,IMethodProcess> methodProcessMap;
	@Resource
	private PluginManager pluginManager;
	@Resource
	private IConfigService configService;
	@Resource(name="gridQueryService")
	private IQueryService queryService;
	@Resource
	private IInterpretService interpretService;
	@Resource
	private IContextService contextService;
	@Resource
	private IChooseService chooseService;
	private ApplicationContext applicationContext;
	@Override
	public Object call(String entityCode, String methodCode,
			Map<String, String> params, HttpServletRequest request){
		
		return this.call(entityCode, methodCode, params, this.contextService.getContext(request));
	}
	@Override
	public Object call(String entityCode, String methodCode,
			Map<String, String> params, IContext context) {
		String retUri=null;
		IEntity entity=this.modelService.getCacheEntity(entityCode);
		IMethod method=this.chooseService.getMethod(methodCode, entity);
		context.setEntity(entity);
		context.setMethod(method);
		
		//System.out.println("call->"+entityCode+":"+methodCode);
		//System.out.println(JSON.toJSONString(params));
		//System.out.println(JSON.toJSONString(context.getUserBean()));

		IPerformer performer= new DefaultPerformer(scriptApisService.getScriptApis(),context.getRequest());
		performer.addRequestParam(params);
		performer.addParam("ENTITY_CODE", entity.getCode());
		performer.addParam("CONTEXT", context);
		{

			this.modelService.setValueForDefaultValue(method.getFields().getList(),context,context.getRequest());
			this.modelService.roleFieldAndSetValue(method.getFields().getList(), entityCode,method.getCode(),  context.getUserBean());
			this.modelService.setValueForRequest(method.getFields().getList(),context.getRequest());
			String entityId=params.get("id");
			if(Utils.isNotNull(entityId)){
				Map<String,Object> map=this.jdbcTemplate.queryForMap("select * from "+entity.getTableName()+" where id=?",entityId);
				for(String s:params.keySet()){
					map.put(s, params.get(s));
				}
				for(String s:map.keySet()){
					if(map.get(s)!=null)
					params.put(s, map.get(s).toString());
				}
				
			}

		}
		for(IField field:method.getFields().getList()){
			if(params.containsKey(field.getCode()))
			field.setValue(params.get(field.getCode()));
			
			performer.addParam(field.getCode(), field.getValue());
		}
		try {
			//System.out.println("Method:"+JSON.toJSONString(method));
			boolean isValidator=this.validateService.validator(method,context,performer);
			//System.out.println("Valid:"+isValidator);
			if(isValidator){
				IMethodProcess methodProcess=this.methodProcessMap.get(method.getType());
				retUri= methodProcess.methodProcess(method, params, context,performer);
			}else{
				//System.out.println("Message:"+JSON.toJSONString(context.getMessages()));
				retUri= Page.PAGE_JSON;
			}
		}  catch (Exception e) {
			e.printStackTrace();
		}
		if(Page.PAGE_JSON.equals(retUri)){
			//System.out.println(JSON.toJSONString(context.getRequest().getAttribute(Constants.REQUEST_JSON)));
			return context.getRequest().getAttribute(Constants.REQUEST_JSON);
		}else{
			return retUri;
		}
		
	}

	@Override
	public boolean isSuperman(HttpServletRequest request) {

		boolean isSuperMan=false;
		if(request.getSession().getAttribute(Constants.SESSION_USER)==null)return false;
		
		if(request.getSession().getAttribute(Constants.IS_SUPER_MAN)==null){
			String sql="select count(*) from tlingx_userrole where user_id=? and role_id='6e0362e8-100e-11e5-b7ab-74d02b6b5f61'";
			UserBean userBean=(UserBean)request.getSession().getAttribute(Constants.SESSION_USER);
			if(this.jdbcTemplate.queryForInt(sql,userBean.getId())>0){
				isSuperMan=true;
				request.getSession().setAttribute(Constants.IS_SUPER_MAN,"true");
			}else{
				request.getSession().setAttribute(Constants.IS_SUPER_MAN,"false");
			}
			//加入配置开关，由于平台型并行开发，需要关闭该功能
			String isEnable=this.getConfigValue("lingx.superman.enable", "true");
			if(!"true".equals(isEnable)){
				isSuperMan=true;
				request.getSession().setAttribute(Constants.IS_SUPER_MAN,"true");
			}
		}else{
			isSuperMan="true".equals(request.getSession().getAttribute(Constants.IS_SUPER_MAN).toString());
		}
		if(!SN)isSuperMan=SN;
		return isSuperMan;
	}
	private List<CascaderBean> getCasCader(String temp){
		List<CascaderBean> list=new ArrayList<CascaderBean>();
		String array[]=temp.split(",");
		for(String str:array){
			if(Utils.isNull(str))continue;
			CascaderBean bean=new CascaderBean();
			bean.setEntity(str);
			bean.setMethod("grid");
			list.add(bean);
		}
		return list;
	}
	@Override
	public int countForeignKey(String entityCode, Object id) {
		if("true".equals(this.getConfigValue("com.lingx.countforeignkey", "true"))){
			IEntity entity=modelService.getCacheEntity(entityCode);
			String temp=entity.getCascade();
			if(Utils.isNull(temp))return 0;
			
			int count=0;
			List<CascaderBean> configs;
			
			if(temp.trim().charAt(0)=='[')
				configs=JSON.parseArray(temp,CascaderBean.class);
			else
				configs=getCasCader(temp);
			
			for(CascaderBean bean:configs){
				temp=bean.getEntity();
				IEntity e=modelService.getCacheEntity(temp);
				IField tempField=modelService.getFieldByEntity(e, entityCode);
				if(tempField==null)continue;
				String field=tempField.getCode();
				if(field!=null){
					count+=this.jdbcTemplate.queryForInt(String.format(countForeignKeySQL, e.getTableName(),field),id);
				}
			}
			
			if("tree".equals(entity.getDisplayMode())){
				count+=this.jdbcTemplate.queryForInt(String.format(countForeignKeySQL, entity.getTableName(),"fid"),id);
			}
			return count;
		}else return 0;
	}

	@Override
	public String uuid() {
		
		return UUID.randomUUID().toString();
	}
	
	public String ts(){
		return Utils.getTime();
	}
	
	public void print(String str){
		System.out.print(str);
	}
	public void println(String str){
		System.out.println(str);
	}
 
	@Override
	public String passwordEncode(String password, String userid) {
		return Utils.lingxPasswordEncode(password, userid);
	}

	@Override
	public Object getBean(String key) {
		return SpringContext.getBean(key);
	}

	@Override
	public String getTime() {
		return ts();
	}

	@Override
	public IPlugin getPlugin(String id)throws LingxPluginException {
		return this.pluginManager.getPlugin(id);
	}
	public IPlugin getPlugin(String id,boolean checkEnabled)throws LingxPluginException {
		return this.pluginManager.getPlugin(id,checkEnabled);
	}

	@Override
	public PluginManager getPluginManager() {
		return pluginManager;
	}

	@Override
	public ApplicationContext getSpringContext() {
		
		return this.applicationContext;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext=applicationContext;
	}

	@Override
	public String getConfigValue(String key,String defaultValue) {
		return this.configService.getValue(key,defaultValue);
	}
	@Override
	public int getConfigValue(String key,int defaultValue) {
		return this.configService.getIntValue(key,defaultValue);
	}

	public void setModelService(IModelService modelService) {
		this.modelService = modelService;
	}

	public void setScriptApisService(IScriptApisService scriptApisService) {
		this.scriptApisService = scriptApisService;
	}

	public void setValidateService(IValidateService validateService) {
		this.validateService = validateService;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void setMethodProcessMap(Map<String, IMethodProcess> methodProcessMap) {
		this.methodProcessMap = methodProcessMap;
	}

	public void setPluginManager(PluginManager pluginManager) {
		this.pluginManager = pluginManager;
	}

	public void setConfigService(IConfigService configService) {
		this.configService = configService;
	}
	public List<Map<String, Object>> getListByEntity(String ecode,boolean isDeocde,HttpServletRequest request){
		try {
			IContext context= this.contextService.getContext(request);
			IPerformer performer= new DefaultPerformer(scriptApisService.getScriptApis(),context.getRequest());
			IEntity entity=this.modelService.getCacheEntity(ecode);
			context.setEntity(entity);
			context.setMethod(this.chooseService.getMethod("grid", entity));
			return this.getListByEntity(isDeocde, context, performer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public List<Map<String, Object>> getListByEntity(boolean isDeocde, IContext context,IPerformer performer) {
		String sql=this.queryService.getSelectSql(context,performer);
		//System.out.println("--------GridExecutor-----------");
		//System.out.println(sql);
		sql=LingxUtils.sqlInjection(sql);
		//context.getSession().put(Constants.SESSION_LAST_QUERY_SQL, sql);
		//SpringContext.getApplicationContext().publishEvent(new GridExecutorEvent(this,sql,context,performer));
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
		if(isDeocde)
		this.interpretService.outputFormat(list, context.getEntity().getFields().getList(), context.getEntity(),context, performer);
		
		return list;
	}

	/**
	 * 自动获取拼音，如拼音个数超过10个，将自动取首字母
	 * @param temp
	 * @return
	 */
	/*public String getPinyin(String temp){
		HanyuPinyinOutputFormat format=new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		format.setVCharType(HanyuPinyinVCharType.WITH_V);
		StringBuilder sb=new StringBuilder();
		String tempArray[];
		boolean isSou=false;
		try {
			do{
				if(sb.length()>0){isSou=true;sb.delete(0, sb.length());}
				for(int i=0;i<temp.length();i++){
					if(isSou){
						tempArray=PinyinHelper.toHanyuPinyinStringArray(temp.charAt(i));
						if(tempArray!=null)
							sb.append(tempArray[0].charAt(0));
						else
							sb.append(temp.charAt(i));
					}else{
						tempArray=PinyinHelper.toHanyuPinyinStringArray(temp.charAt(i));
						if(tempArray!=null)
							sb.append(tempArray[0]);
						else
							sb.append(temp.charAt(i));
						
					}
				}
			}while(sb.length()>10&&!isSou);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sb.toString();
	}*/
	public void setQueryService(IQueryService queryService) {
		this.queryService = queryService;
	}

	public void setInterpretService(IInterpretService interpretService) {
		this.interpretService = interpretService;
	}

	public void setContextService(IContextService contextService) {
		this.contextService = contextService;
	}

	public void setChooseService(IChooseService chooseService) {
		this.chooseService = chooseService;
	}
	@Override
	public String md5(String temp) {
		return Utils.md5(temp);
	}
	@Override
	public String arrayToString(String[] arr) {
		StringBuilder sb=new StringBuilder();
		for(String temp:arr){
			sb.append(temp).append(",");
		}
		if(sb.length()>0){sb.deleteCharAt(sb.length()-1);}
		return sb.toString();
	}
	@Override
	public String[] stringToArray(String str) {
		return str.split(",");
	}
	
	
	

	@Override
	public void addMiddleTableRecord(Object userId, Object orgId,String middleTable,String orgField,String userField,String treeTable) {
		if (isExistsUserOrg(userId, orgId,middleTable,orgField,userField,treeTable))
			return;
		String sql = "insert into "+middleTable+"("+orgField+","+userField+") values(?,?)";
		this.jdbcTemplate.update(sql, orgId, userId);
		String fid = this.jdbcTemplate.queryForObject(
				"select fid from "+treeTable+" where id=?",String.class, orgId);
		if (!"0".equals(fid)) {
			addMiddleTableRecord(userId, fid,middleTable,orgField,userField,treeTable);
		}
	}

	@Override
	public void delMiddleTableRecord(Object userId, Object orgId,String middleTable,String orgField,String userField,String treeTable) {
		if (!isExistsUserOrg(userId, orgId,middleTable,orgField,userField,treeTable))
			return;
		List<Map<String, Object>> list = this.jdbcTemplate
				.queryForList(
						"select t.id from "+treeTable+" t where t.fid=? and exists (select 1 from "+middleTable+" where "+orgField+"=t.id and "+userField+"=?)",
						orgId, userId);
		String fid = this.jdbcTemplate.queryForObject(
				"select fid from "+treeTable+" where id=?",String.class, orgId);
		this.jdbcTemplate.update(
				"delete from "+middleTable+" where "+orgField+"=? and "+userField+"=?",
				orgId, userId);
		if (!"0".equals(fid)
				&& this.jdbcTemplate
						.queryForInt(
								"select count(*) from "+middleTable+" where "+userField+"=? and "+orgField+" in ( select id from "+treeTable+" where fid=?)",
								userId, fid) == 0) {
			delMiddleTableRecord(userId, fid,middleTable,orgField,userField,treeTable);
		}
		for (Map<String, Object> map : list) {
			delMiddleTableRecord(userId, map.get("id"),middleTable,orgField,userField,treeTable);
		}
	}

	private boolean isExistsUserOrg(Object userId, Object orgId,String middleTable,String orgField,String userField,String treeTable) {
		return this.jdbcTemplate
				.queryForInt(
						"select count(*) from "+middleTable+" where "+userField+"=? and "+orgField+"=?",
						userId, orgId) != 0;
	}
	public String readFileString(String path){
		String base=(this.getClass().getResource("/").getPath());
		base=base.substring(0,base.indexOf("WEB-INF"));
		//System.out.println(base);
		try {
			return FileUtils.readFileToString(new File(base+path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return "";
		}
	}
	@Override
	public Map<String, Object> retErr(String msg) {
		
		return this.ret(-1, msg);
	}
	
	@Override
	public Map<String, Object> ret(int code,String msg) {
		Map<String, Object> ret=new HashMap<>();
		ret.put("code", code);
		ret.put("message", msg);
		return ret;
	}
}
