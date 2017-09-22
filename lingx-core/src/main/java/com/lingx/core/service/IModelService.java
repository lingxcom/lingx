package com.lingx.core.service;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IHttpRequest;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.model.IConfig;
import com.lingx.core.model.IEntity;
import com.lingx.core.model.IField;
import com.lingx.core.model.IMethod;
import com.lingx.core.model.IModel;
import com.lingx.core.model.bean.UserBean;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月5日 下午10:23:35 
 * 类说明 
 */
public interface IModelService {
	public void saveModelToDisk();
	public void recoveryModelForDisk();
	public IEntity getCacheEntity(String code);
	public IEntity getEntity(String code);
	public IEntity get(String code);
	public IMethod getMethod(String methodCode,IEntity entity);
	
	public List<Map<String,Object>> getTreeList(IEntity entity,String id);
	public IEntity createScriptEntity(String table,String name,String author,int idtype,String dbName,JdbcTemplate jdbc);
	public void save(IEntity entity);
	public IModel clone(IModel model);
	public IModel getById(IModel entity,String id);
	public Map<String,Object> getBySort(Object o);
	public Map<String,Object> getByAnno(Object o);
	public Map<String,Object> getProperty(String code,String id);
	public boolean saveProperty(String code,String id,String prop,String value,String oldvalue,String author);
	
	public String addFieldGlobal(String code,String id,String fieldCode,String order);
	public String addField(String code,String id);
	public String addMethod(String code,String id);
	public String addValidator(String code,String id);
	public String addInterpreter(String code,String id);
	public String addExecutor(String code,String id);
	public boolean remove(String code,String ids);
	public void reset(IModel model);
	public boolean copyEntity(String code,String ids);
	public boolean pasteEntity(String code,String id);
	public boolean move(String code,String id,int type);
	public String getPropertyNameFor_(String name);
	public String getDojoFieldByMethodName(String methodName);
	public Map<String,Object> getSourceConfig(String code);
	public String getTableName(IEntity entity);
	/**
	 * 取出entity指向refEntity的字段
	 * @param entity
	 * @param refEntity
	 * @return
	 */
	public IField getFieldByEntity(IEntity entity,IEntity refEntity);
	/**
	 * 取出entity指向refEntity的字段
	 * @param entity
	 * @param refEntity
	 * @return
	 */
	public IField getFieldByEntity(IEntity entity,String refEntityCode);
	public List<String> getTextField(IEntity scriptEntity);
	public String getValueField(IEntity scriptEntity);
	public String getRuleDataAuth(IEntity se,UserBean userBean);
	public  Map<String,Object> getExtJSGridParams(IEntity entity,UserBean userBean);
	public  void setValueForDefaultValue(List<IField> listFields,IContext context,IHttpRequest request);
	public  void setValueForRequest(List<IField> listFields,IHttpRequest request);
	public void removeDefaultValueTransformTag(List<IField> listFields);
	public void roleFieldAndSetValue(List<IField> listFields,String ecode,UserBean userBean);
	public   void setValueForFields(List<IField> listFields,String ecode,String valueField,String eid,IContext context,IPerformer performer);
	public   void setValueForFields(List<IField> listFields,IPerformer performer);
	
	public boolean createQueryEntity(String code,String name,String author,String app);
	
	public String getEntityOrderField(IEntity entity);
	public String getEntityOrderType(IEntity entity);
	
}
