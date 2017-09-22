package com.lingx.core.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.exception.LingxScriptException;
import com.lingx.core.model.IEntity;
import com.lingx.core.model.IField;
import com.lingx.core.model.IInterpreter;
import com.lingx.core.service.IInterpretService;
import com.lingx.core.service.IModelService;
import com.lingx.core.utils.Utils;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月27日 下午2:54:46 
 * 类说明 
 */
@Component(value="lingxInterpretService")
public class InterpretServiceImpl implements IInterpretService {

	public static final Logger log=LogManager.getLogger(InterpretServiceImpl.class);
	@Resource
	private IInterpreter[] defaultInterpreter;
	@Resource
	private IModelService modelService;
	@Resource(name="jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	private List<Map<String,Object>> comboData;
	@Override
	public Map<String, String> inputFormat(Map<String, String> map,List<IField> fields, IEntity entity, IContext context,
			IPerformer performer) {
		//System.out.println(JSON.toJSONString(map));
		for(IField field :fields){
			//System.out.println(field.getCode());
			List<IInterpreter> inters=field.getInterpreters().getList();

			//System.out.println(inters.size());
			if(inters.size()>0){
				Object value=map.get(field.getCode());
				for(IInterpreter inte:inters){
					if(!IInterpreter.TYPE_EXPRESSION.equals(inte.getType())){
						inte=getInterpreterTemplate(inte.getType());
					}
					try {
						//System.out.println(value);
						value=inte.input(value, context, performer);
						//System.out.println(value);
					} catch (LingxScriptException e) {
						e.printStackTrace();
					}
				}
				map.put(field.getCode(), value.toString());
			}
		}
		return map;
	}

	@Override
	public void outputFormat(List<Map<String, Object>> list,List<IField> fields, IEntity entity, IContext context,
			IPerformer performer) {
		if (list != null)
			for (Map<String, Object> map : list) {
				outputFormat(map,fields,entity,context,performer);
			}

	}

	public void outputFormat(Map<String, Object> m,List<IField> fields, IEntity entity, IContext context,
			IPerformer performer) {
		if (m == null)
			return;
		List<Map<String,Object>> listMap=null;
		IEntity subEntity=null;
		String etype=null;
		String vField=null;
		StringBuilder params=null;
		String tableName=null;
		String sql = "select * from %s where %s='%s' %s";
		String insql = "select * from %s where %s in(%s) %s";
		List<String> listField=null;

		for(IField f:fields){
			performer.addParam(f.getCode(), m.get(f.getCode()));
		}
		
		for (IField field : fields) {
			 if(!field.getEscape())continue;//是否转义
			listMap=null;subEntity=null;etype=null;vField=null;params=null;tableName=null; listField=null;
			 params = new StringBuilder();
			 etype = field.getRefEntity();
			 if(!field.getEscape())continue;
			if (Utils.isNotNull(etype)) {
				 subEntity = modelService.getCacheEntity(etype);
				if (subEntity == null)
					continue;

				 tableName = modelService.getTableName(subEntity);
				 vField = modelService.getValueField(subEntity);
				listField = modelService.getTextField(subEntity);
				if (m.get(field.getCode()) == null)
					continue;
				if ("tlingx_optionitem".equals(etype)) {//"sysoption".equals(field.getInputType())||
					// 当输入控件为系统选择项时，特殊处理
					params.append(
							" and option_id in(select id from tlingx_option where code='")
							.append(field.getInputOptions()).append("')");
				}
				
				try {
					if (m.get(field.getCode()).toString().indexOf(",") != -1){
						listMap =this.jdbcTemplate.queryForList(
								String.format(insql, tableName, vField,toArrayString(m.get(field.getCode())), params));
						
					}else{
						log.debug("Interpret Single:"+String.format(sql, tableName, vField,m.get(field.getCode()), params));
						listMap = this.jdbcTemplate.queryForList(
								String.format(sql, tableName, vField,m.get(field.getCode()), params));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				if ("tlingx_optionitem_NO".equals(etype)) {//这里本来的处理，是为了字典在界面上不能点击，但会导致，需要该字段字时，会取不到，所以取消
					StringBuilder sb=new StringBuilder();
					if (listMap.size()>0){
						for(Map<String,Object> map:listMap){
							sb.append(map.get("name")).append(",");
						}
					}
					if(sb.length()>0){
						sb.deleteCharAt(sb.length()-1);
					}
					m.put(field.getCode(), sb.toString());
					//field.setValue(sb.toString());
				}else{
						
					if(listMap==null)listMap=new ArrayList<Map<String,Object>>();
					if (listMap.size()==0){//listMap == null
						
						Map<String,Object> map=new HashMap<String,Object>();
						map.put("text", m.get(field.getCode()));
						map.put("value", m.get(field.getCode()));
						map.put("id", m.get(field.getCode()));
						map.put("etype", etype);
						//continue;
						listMap.add(map);
					}else{
						for(Map<String,Object> map:listMap){
							StringBuilder sb = new StringBuilder();
							for (String s : listField) {
								sb.append(map.get(s.toUpperCase())).append("-");
							}
							sb.deleteCharAt(sb.length() - 1);
							map.put("text", sb.toString());
							map.put("value", m.get(field.getCode()));
							map.put("etype", etype);
						}
					}
					// m.put(field.getCode(), map);
					m.put(field.getCode(), listMap);
				}
			}else{
				//当etype==null时，不处理引用显示，反做解释器
				if(field.getInterpreters()!=null&&field.getInterpreters().getList().size()>0){
					List<IInterpreter> list=field.getInterpreters().getList();
					Object val=m.get(field.getCode());
					for(IInterpreter temp:list){
						try {
							if(IInterpreter.TYPE_EXPRESSION.equals(temp.getType())){
								val=temp.output(val, context,performer);
							}else{
								IInterpreter obj=getInterpreterTemplate(temp.getType());//defaultInterpreter.get(temp.getType());
								val=obj.output(val, context,performer);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					//field.setValue(val);
					m.put(field.getCode(), val);
				}
				field.setValue(m.get(field.getCode()));
				//m.put(field.getCode(), interpreter.interpret(performer, field, null));
				
			}
		}
	}
	public static String toArrayString(Object obj){
		StringBuilder sb=new StringBuilder();
		sb.append("\"").append(obj.toString().replace(",", "\",\"")).append("\"");
		return sb.toString();
	}
	@Override
	public Object outputFormat(IField field, IContext context, IPerformer performer) {

		if(field.getInterpreters()==null||field.getInterpreters().getList().size()==0)return field.getValue();
		List<IInterpreter> list=field.getInterpreters().getList();
		Object val=field.getValue();
		try {
			for(IInterpreter temp:list){
				if(IInterpreter.TYPE_EXPRESSION.equals(temp.getType())){
					val=temp.output(val, context,performer);
				}else{
					IInterpreter obj=getInterpreterTemplate(temp.getType());//defaultInterpreter.get(temp.getType());
					val=obj.output(val, context,performer);
				}
			}
		} catch (LingxScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return val;
		
	}
	
	private IInterpreter getInterpreterTemplate(String key){
		IInterpreter obj=null;
		for(IInterpreter temp:this.defaultInterpreter){
			if(temp.getType().equals(key)){
				obj=temp;
				break;
			}
		}
		return obj;
	}

	@Override
	public List<Map<String,Object>> getComboData() {
		if(comboData==null){
			comboData=new ArrayList<Map<String,Object>>();
			for(IInterpreter temp:this.defaultInterpreter){
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("value", temp.getType());
				map.put("text", temp.getName());
				this.comboData.add(map);
			}
		}
		return comboData;
	}

	public void setDefaultInterpreter(IInterpreter[] defaultInterpreter) {
		this.defaultInterpreter = defaultInterpreter;
	}

	public void setModelService(IModelService modelService) {
		this.modelService = modelService;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void setComboData(List<Map<String, Object>> comboData) {
		this.comboData = comboData;
	}

}
