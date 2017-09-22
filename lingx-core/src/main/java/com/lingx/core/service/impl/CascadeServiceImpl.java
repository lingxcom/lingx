package com.lingx.core.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lingx.core.Page;
import com.lingx.core.engine.IContext;
import com.lingx.core.exception.LingxCascadeException;
import com.lingx.core.model.IEntity;
import com.lingx.core.model.IField;
import com.lingx.core.model.bean.CascaderBean;
import com.lingx.core.service.ICascadeService;
import com.lingx.core.service.II18NService;
import com.lingx.core.service.IModelService;
import com.lingx.core.service.IPageService;
import com.lingx.core.utils.Utils;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月5日 下午10:22:23 
 * 类说明 
 */
@Component(value="lingxCascadeService")
public class CascadeServiceImpl implements ICascadeService{
	
	public static final String GRID_CASCADE="grid_cascade";
	
	@Resource
	private IModelService modelService;
	@Resource
	private IPageService pageService;
	@Resource
	private II18NService i18n;
	
	@Override
	public void getCascade(IContext context) throws LingxCascadeException {
		IEntity entity=context.getEntity();
		List<Map<String, Object>> list =null;
		String ecodes = entity.getCascade();
		if(Utils.isNull(ecodes)){
			context.getRequest().setAttribute(GRID_CASCADE, JSON.toJSON(new ArrayList()));
			return ;}
		list=this.getCascaderList(ecodes,entity);
		context.getRequest().setAttribute(GRID_CASCADE, JSON.toJSON(list));
		
	}
	private List<Map<String, Object>> getCascaderList(String text,IEntity entity){
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		text=text.trim();
		if(text==null||"".equals(text))return list;
		if(text.charAt(0)=='['){
			List<CascaderBean> configs=JSON.parseArray(text,CascaderBean.class);
			for(CascaderBean config:configs){
				String code=config.getEntity();
				IEntity se =this.modelService.getCacheEntity(code);
				if (se != null && se.getFields() != null) {
				
					String refField = "";
					for (IField f : se.getFields().getList()) {
						if (this.modelService.getCacheEntity(f.getRefEntity())!=null&&entity.getTableName().equals(this.modelService.getCacheEntity(f.getRefEntity()).getTableName())) {
							refField = f.getCode();
							break;
						}
					}
					
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("entity", code);
						map.put("name", i18n.text(config.getName()!=null?config.getName():se.getName()));
						map.put("refField", refField);
						map.put("method", config.getMethod()!=null?config.getMethod():"grid");
						map.put("rule",config.getRule()!=null?config.getRule():"");
						map.put("where",config.getWhere()!=null?config.getWhere():"{}");
						list.add(map);
					
				}
			}
			
		}else{
			String codes[] = text.split(",");
			for (String code : codes) {
				IEntity se =this.modelService.getCacheEntity(code);
				if (se != null && se.getFields() != null) {
					boolean b1 = false;
					String refField = "";
					for (IField f : se.getFields().getList()) {
						if (this.modelService.getCacheEntity(f.getRefEntity())!=null&&entity.getTableName().equals(this.modelService.getCacheEntity(f.getRefEntity()).getTableName())) {
							b1 = true;
							refField = f.getCode();
							break;
						}
					}
					if (b1) {
						//b = true;
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("entity", code);
						map.put("name", i18n.text(se.getName()));
						map.put("refField", refField);
						map.put("method", "grid");
						map.put("rule", "");
						map.put("where","{}");
						list.add(map);
					}
				}
			}
		}
		return list;
	}
	public void setModelService(IModelService modelService) {
		this.modelService = modelService;
	}
	public void setPageService(IPageService pageService) {
		this.pageService = pageService;
	}

}
