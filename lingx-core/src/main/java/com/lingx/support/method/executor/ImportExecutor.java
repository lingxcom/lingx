package com.lingx.support.method.executor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.exception.LingxScriptException;
import com.lingx.core.model.IEntity;
import com.lingx.core.model.IExecutor;
import com.lingx.core.model.IField;
import com.lingx.core.model.IInterpreter;
import com.lingx.core.model.IMethod;
import com.lingx.core.model.IScript;
import com.lingx.core.model.impl.AbstractModel;
import com.lingx.core.service.IChooseService;
import com.lingx.core.service.IInterpretService;
import com.lingx.core.service.ILingxService;
import com.lingx.core.service.IModelService;
import com.lingx.core.utils.ExcelUtils;
import com.lingx.core.utils.Utils;

/** 
* @author www.lingx.com
* @version 创建时间：2021年1月19日 下午5:56:14 
* 类说明 
*/
public class ImportExecutor extends AbstractModel implements IExecutor {

	private static final long serialVersionUID = 1585075416843435641L;
	@Resource
	private IModelService modelService;
	@Resource
	private IChooseService chooseService;
	@Resource
	private ILingxService lingxService;
	@Resource
	private JdbcTemplate jdbcTemplate;
	@Resource 
	private IInterpretService interpretService;
	@Override
	public Object execute(IContext context, IPerformer performer) throws LingxScriptException {
		String filepath=context.getRequest().getParameter("file");
		String type=context.getRequest().getParameter("type");
		filepath=context.getRequest().getLocalPath()+filepath;
		Object ret="导入成功";
		try{
			IMethod	method=chooseService.getMethod(type, context.getEntity());
			
			List<List<Object>> list=ExcelUtils.readList(filepath);
			List<Object> fields=list.get(0);
			List<IField> listCode=getMethodField(fields,method.getFields().getList());
			for(int i=1;i<list.size();i++){
				List<Object> list1=list.get(i);
				Map<String, String> params =new HashMap<>();
				for(int j=0;j<listCode.size();j++){
					params.put(listCode.get(j).getCode(), getValue(list1.get(j),listCode.get(j),context,performer));
				}
				this.lingxService.call(context.getEntity().getCode(), type, params, context);
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
			return this.lingxService.retErr(e.getLocalizedMessage());
		}
		
		
		return ret;
	}
	
	private String getValue(Object obj,IField field,IContext context, IPerformer performer){
		String ret=obj==null?"":obj.toString();
		if(Utils.isNotNull(field.getRefEntity())){
			String sql="select %s from %s where %s like '%s'";
			IEntity entity=this.modelService.get(field.getRefEntity());
			
			if("tlingx_optionitem".equals(field.getRefEntity())){
				sql+=" and option_id in(select id from tlingx_option where code='"+field.getInputOptions()+"')";
			}
			String textField=this.modelService.getTextField(entity).get(0);
			String valueField=this.modelService.getValueField(entity);
			ret=this.jdbcTemplate.queryForObject(String.format(sql, valueField,entity.getTableName(),textField,"%"+obj+"%"), String.class);
		}else if(field.getInterpreters().getList().size()>0){
			
			try {
				obj=this.interpretService.inputFormat(obj, field,  context, performer);
				ret=obj==null?"":obj.toString();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ret;
	}

	private List<IField> getMethodField(List<Object> list,List<IField> fields)throws Exception{
		List<IField> listCode=new ArrayList<>();
		for(Object obj:list){
			IField temp=null;
			for(IField field:fields){
				if(obj.toString().equals(field.getName())){
					temp=field;
					break;
				}
			}
			if(temp==null){
				throw new Exception(obj.toString()+"没找到相应的属性代码");
			}
			listCode.add(temp);
		}
		
		return listCode;
	}
	@Override
	public IScript getScript() {
		// TODO Auto-generated method stub
		return null;
	}

}
