package com.lingx.core.service;

import com.lingx.core.engine.IContext;
import com.lingx.core.exception.LingxCascadeException;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月5日 下午3:10:50 
 * 级联处理
 */
public interface ICascadeService {

	public void getCascade(IContext context)throws LingxCascadeException;
}
