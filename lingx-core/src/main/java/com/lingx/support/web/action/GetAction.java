package com.lingx.support.web.action;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IHttpRequest;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.engine.impl.DefaultPerformer;
import com.lingx.core.model.IEntity;
import com.lingx.core.model.IField;
import com.lingx.core.model.IMethod;
import com.lingx.core.model.bean.UserBean;
import com.lingx.core.service.IChooseService;
import com.lingx.core.service.II18NService;
import com.lingx.core.service.IModelService;
import com.lingx.core.service.IScriptApisService;
import com.lingx.core.utils.Utils;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月27日 下午5:29:51 
 * 类说明 
 */
public class GetAction extends AbstractJsonAction {
	@Resource
	private IModelService modelService;
	@Resource
	private IChooseService chooseService;
	@Resource
	private IScriptApisService scriptApisService;
	@Resource
	private II18NService i18n;
	@Override
	public String execute(IContext context) {
		IHttpRequest request=context.getRequest();
		UserBean userBean=context.getUserBean();
		String entityCode = request.getParameter("e");
		String methodCode = request.getParameter("m");
		String entityId = request.getParameter("eid");
		
		if (Utils.isNull(entityCode)) {
			return "{}";
		}
		IEntity scriptEntity = null;
		try {
			scriptEntity = modelService.getCacheEntity(entityCode);
		} catch (Exception e) {
			return "{}";
		}
		IMethod scriptMethod = null;
		if (scriptEntity == null) {
			return "{}";
		}
		if (Utils.isNull(methodCode)) {
		
			return JSON.toJSONString(modelService.getExtJSGridParams(scriptEntity,userBean));
		} else {
			IPerformer performer= new DefaultPerformer(scriptApisService.getScriptApis(),context.getRequest());
			// 取方法属性
			try{
				scriptMethod=chooseService.getMethod(methodCode, scriptEntity);
			}catch(Exception e){
				e.printStackTrace();
			}
			
			if (scriptMethod == null) {
				return "{}";
			}
			modelService.setValueForDefaultValue(scriptMethod.getFields().getList(),context,context.getRequest());
			modelService.roleFieldAndSetValue(scriptMethod.getFields().getList(), entityCode,  userBean);
			modelService.setValueForRequest(scriptMethod.getFields().getList(),request);
			modelService.removeDefaultValueTransformTag(scriptMethod.getFields().getList());
			if(Utils.isNotNull(entityId))
				modelService.setValueForFields(scriptMethod.getFields().getList(),entityCode,modelService.getValueField(scriptEntity) ,entityId,context,performer);
			else
				modelService.setValueForFields(scriptMethod.getFields().getList(), performer);

			for(IField field:scriptMethod.getFields().getList()){
				field.setName(i18n.text(field.getName()));
			}
			return JSON.toJSONString(scriptMethod);
		}
	}
	public void setModelService(IModelService modelService) {
		this.modelService = modelService;
	}
	public void setChooseService(IChooseService chooseService) {
		this.chooseService = chooseService;
	}
	public void setScriptApisService(IScriptApisService scriptApisService) {
		this.scriptApisService = scriptApisService;
	}

}
