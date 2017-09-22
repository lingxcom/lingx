package com.lingx.core.service;

import java.util.List;
import java.util.Map;

import com.lingx.core.engine.IContext;
import com.lingx.core.exception.LingxConvertException;
import com.lingx.core.model.IField;

/** 
 * @author www.lingx.com
 * @version 创建时间：2015年4月5日 下午3:09:55 
 * 参数转换
 */
public interface IConverteService {
	public Map<String,String> convert(List<IField> fields,IContext context)throws LingxConvertException;
}
