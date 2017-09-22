package com.lingx.core.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.lingx.core.engine.IContext;
import com.lingx.core.model.IValue;
import com.lingx.core.service.IDefaultValueService;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年10月6日 下午6:35:24 
 * 类说明 
 */
@Component(value="lingxDefaultValueService")
public class DefaultValueServiceImpl implements IDefaultValueService {
	@Resource
	private List<IValue> listValues;
	@Override
	public String transform(String source,IContext context) {
		if(source==null||"".equals(source)||source.charAt(0)!='$')return source;
		String temp=source.trim().replaceAll(" ", "");
		for(IValue value:this.listValues){
			if(temp.equals(value.getSourceValue())){
				temp=value.getTargetValue(context);
				break;
			}
		}
		return temp;
	}
	public void setListValues(List<IValue> listValues) {
		this.listValues = listValues;
	}
	@Override
	public String transforms(String source, IContext context) {
		for(IValue value:this.listValues){
			source=source.replace(value.getSourceValue(), value.getTargetValue(context));
		}
		return source;
	}
	

	public static void main(String args[]){
		String content="${org_id=${CUser.getOrgId()}&corg_id=${CUser.getOrgId()}}";
		String key="${CUser.getOrgId()}";
		content=content.replace(key, "有志者事竟成");
		System.out.println(content);
	}
	public static void main3(String args[]){
		String content="${id}";
		String key="id";
		content=content.replaceAll("\\$\\{"+key+"\\}", "11");
		System.out.println(content);
	}

}
