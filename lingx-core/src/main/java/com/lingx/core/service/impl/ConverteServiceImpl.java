package com.lingx.core.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IHttpRequest;
import com.lingx.core.exception.LingxConvertException;
import com.lingx.core.model.IField;
import com.lingx.core.service.IConverteService;
import com.lingx.core.utils.LingxUtils;

/**
 * @author www.lingx.com
 * @version 创建时间：2015年4月5日 下午3:33:14 类说明
 */
@Component(value="lingxConverteService")
public class ConverteServiceImpl implements IConverteService {
	private String spaceMark;
	private String paramName;
	public ConverteServiceImpl(){
		spaceMark=",";
		paramName="_params";
	}
	@Override
	public Map<String, String> convert(List<IField> fields, IContext context)throws LingxConvertException {
		Map<String, String> params = new HashMap<String, String>();//方法需要的参数
		IHttpRequest request=context.getRequest();
		Set<String> fieldCodeSets=new HashSet<String>();
		for(IField field:fields){
			fieldCodeSets.add(field.getCode());
		}
		Map<String,String[]> requestParams=request.getParameters();
		String value=null;
		for(String key:requestParams.keySet()){
			value=getValue(requestParams.get(key));
			if(fieldCodeSets.contains(key)){
				params.put(key,value );
			}
			request.setAttribute(key, value);//回写到下一个界面
		}
		for(IField field:fields){//写入方法内部方便验证
			field.setValue(params.get(field.getCode()));
		}
		request.setAttribute(paramName, JSON.toJSONString(params));//全部回写到下一个界面
		fieldCodeSets.clear();
		return params;
	}
	
	public String getValue(String array[]){
		if(array==null)return null;
		StringBuilder sb=new StringBuilder();
		for(String str:array){
			sb.append(str).append(this.spaceMark);
		}
		LingxUtils.deleteLastChar(sb);
		return sb.toString();
	}
	public void setSpaceMark(String spaceMark) {
		this.spaceMark = spaceMark;
	}
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
}
