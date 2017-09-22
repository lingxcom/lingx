package com.lingx.core.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.exception.LingxScriptException;
import com.lingx.core.exception.LingxValidatorException;
import com.lingx.core.model.IField;
import com.lingx.core.model.IMethod;
import com.lingx.core.model.IValidator;
import com.lingx.core.service.IValidateService;
import com.lingx.core.utils.Utils;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月9日 下午9:41:24 
 * 类说明 
 */
@Component(value="lingxValidateService")
public class ValidateServiceImpl implements IValidateService {
	@Resource
	private IValidator[] defaultValidator;
	private List<Map<String,Object> > comboData;
	@Override
	public boolean validator(IMethod method,
			IContext context,IPerformer performer) throws LingxValidatorException {

		boolean isValidation=true;
		if(method.getValidation()){
			List<IField> fields=method.getFields().getList();
			for (IField field : fields) {
				//System.out.println(field.getCode()+"-"+field.getName());
				if(field.getIsNotNull()){
					IValidator notNullValidator=this.getValidatorTemplate(IValidator.TYPE_NO_NULL);
					boolean b=true;
					try {
						b=notNullValidator.valid(field.getCode(),field.getValue(), null, context, performer);
					} catch (LingxScriptException e) {
						b=false;
					}
					if (isValidation)
						isValidation = b;
					if(!b){
						context.addMessage(field.getCode(), Utils.formatString("{}不可为空", field.getName()));
					}
				}
				List<IValidator> vList = field.getValidators().getList();
				if (vList != null)
					for (IValidator v : vList) {
						//System.out.println(v.getCode()+"-"+v.getName());
						if(IValidator.TYPE_EXPRESSION.equals(v.getType())){//
							try {
								boolean b1 = v.valid(field.getCode(),field.getValue(),v.getParam(),context,performer);
								if (isValidation)
									isValidation = b1;
								if (!b1) {
									context.addMessage(field.getCode(), Utils.formatString(v.getMessage(), field.getName()));
									break;
								}
							} catch (LingxScriptException e) {
								isValidation= false;
								throw new LingxValidatorException(e.getMessage());
								//e.printStackTrace();
							}
						}else{//if(v.getType()==1)
							IValidator temp=getValidatorTemplate(v.getType());
							try {
								boolean b1 =temp.valid(field.getCode(),field.getValue(),v.getParam(),context,performer);
								if (isValidation)
									isValidation = b1;
								if (!b1) {
									context.addMessage(field.getCode(), Utils.formatString(getMessage(v,temp), field.getName(),v.getParam()==null?"":v.getParam().split(",")));
									break;
								}
							} catch (LingxScriptException e) {
								isValidation= false;
								throw new LingxValidatorException(e.getMessage());
								//e.printStackTrace();
							}
						}
					}
			}
		}
		return isValidation;
	}
	/**
	 * 取出验证消息
	 * @param source 被设置的验证器
	 * @param target 目标验证器
	 * @return
	 */
	public String getMessage(IValidator source,IValidator target){
		String msg=null;
		if(IValidator.DEFAULT_MESSAGE.equals(source.getMessage())){
			msg=target.getMessage();
		}else{
			msg=source.getMessage();
		}
		return msg;
	}

	public void setDefaultValidator(IValidator[] defaultValidator) {
		this.defaultValidator = defaultValidator;
	}

	private IValidator getValidatorTemplate(String key){
		IValidator temp=null;
		for(IValidator valid:this.defaultValidator){
			if(valid.getType().equals(key)){
				temp=valid;
				break;
			}
		}
		return temp;
	}

	@Override
	public List<Map<String,Object>> getComboData() {
		if(comboData==null){
			comboData=new ArrayList<Map<String,Object>>();
			for(IValidator valid:this.defaultValidator){
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("value", valid.getType());
				map.put("text", valid.getName());
				this.comboData.add(map);
			}
		}
		return comboData;
	}
}
