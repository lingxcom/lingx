package com.lingx.core.service;

import java.util.List;
import java.util.Map;

import com.lingx.core.engine.IContext;
import com.lingx.core.engine.IPerformer;
import com.lingx.core.exception.LingxValidatorException;
import com.lingx.core.model.IMethod;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月5日 下午3:07:20 
 * 验证服务
 */
public interface IValidateService {

	public boolean validator(IMethod method, IContext context,IPerformer performer) throws LingxValidatorException;
	
	public List<Map<String,Object>> getComboData();

}
